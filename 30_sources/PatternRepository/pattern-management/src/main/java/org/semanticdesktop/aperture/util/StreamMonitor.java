/*
 * Copyright (c) 2009 Aduna and Deutsches Forschungszentrum fuer Kuenstliche Intelligenz DFKI GmbH.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Monitors whether a thread that consumes an input stream works or has it stalled. It can be used
 * to protect an application against unreliable external libraries.
 * 
 * @author antheque
 *
 */
public class StreamMonitor {
    
    private StreamConsumer thread;
    
    private MonitoredStream monitoredStream;
    
    private long maxProcessingTimePerMb;
        
    private long minimumMaxProcessingTime;

    private long maxIdleReadTime;
    
    // I declare this as volatile because it will be changed by one thread and checked by another thread, no
    // caching is to be done
    private volatile boolean suspendMonitoring;
    
    public StreamMonitor(InputStream stream, StreamConsumerFactory threadFac, StopRequestor stopRequestor, 
            long maxProcessingTimePerMb, long minimumMaxProcessingTime, long maxIdleReadTime) {
        this.monitoredStream = new MonitoredStream(stream,stopRequestor);
        if (threadFac != null) {
            this.thread = threadFac.getConsumer(monitoredStream,this);
        }
        this.maxProcessingTimePerMb = maxProcessingTimePerMb;
        this.minimumMaxProcessingTime = minimumMaxProcessingTime;
        this.maxIdleReadTime = maxIdleReadTime;
        this.suspendMonitoring = false;
    }
    
    public void setStreamConsumerFactory(StreamConsumerFactory abortableThreadFactory) {
        this.thread = abortableThreadFactory.getConsumer(monitoredStream, this);
    }
    
    public void start() throws Exception {
        thread.start();
        while (true) {
            long waitTime;
            if (monitoredStream.allBytesRead()) {
                /*
                 * This means that the underlying extractor has read all bytes within the underlying stream,
                 * we must wait until the processing ends. We assume that the processing time is linearly
                 * dependent on the file length (with the maxProcessingTimePerMb coefficient).
                 */
                waitTime = (long)(maxProcessingTimePerMb * monitoredStream.getTotalBytesRead() / 
                        (1024.0 * 1024.0));
                /*
                 * Very small files don't fit into this linear heuristic, so we place a bound, that allows
                 * very small files to be processed in fixed time (minimumMaxProcessingTime)
                 */
                waitTime = Math.max(waitTime, minimumMaxProcessingTime);
            }
            else {
                /*
                 * This means that the extractor hasn't stopped reading. We try to detect if if has hanged
                 * during reading.
                 */
                waitTime = maxIdleReadTime;
            }

            /*
             * We may be in the middle of an interval between two reads. We need to subtract the time that
             * has already elapsed since the last read from the overall waiting time.
             */
            waitTime -= (System.currentTimeMillis() - monitoredStream.getLastAccessTime());

            /*
             * This indicates that the extraction thread has stopped, which in turn means that the extractor
             * has finished extracting data. This is OK, we may safely bail out from this loop. 
             */
            boolean breakCondition = false;
            if (!thread.isAlive()) {
                breakCondition = true;
            }
            
            /*
             * This will occur if the time that has elapsed since the last read is greater than the time we
             * may wait. E.g. the processing time is longer than the max processing time for the given file
             * length, or the time between reads is longer than the max time between reads. This means that
             * we officially declare this extractor as stalled and bail out from this loop.
             */
            if (waitTime <= 0L) {
                breakCondition = true;
            }

            try {
                /*
                 * The suspendMonitoring flag is consulted only once - here. I did this to avoid any race 
                 * conditions that might arise if we check the flag twice inside the loop and the value 
                 * changes between checks.
                 */
                if (suspendMonitoring) {
                    /*
                     * This means that whatever happens we wait
                     */
                    safelySleep(1000);
                } else if (breakCondition) {
                    /*
                     * This means that the monitoring isn't suspended and that we may break from this loop,
                     * either because the thread finished correctly or we detected a stall and the thread is
                     * to be aborted.
                     */
                    break;
                } else {
                    /*
                     * Now we wait for the computed amount of miliseconds. We use the join method, so if the
                     * extractor finishes earlier, the join method will also finish and the isAlive() check will
                     * make this loop end gracefully. On the other hand, if the extractor doesn't finish within
                     * the prescribed time, we'll get to decide later what to do with it.
                     */
                    thread.join(waitTime);
                }
            }
            catch (InterruptedException e) {
                throw e;
            }
        }

        if (thread.isAlive()) {
            /*
             * This indicates that the previous loop ended with a decision that the extractor has hanged. We
             * must abort the extraction and throw an appropriate exception.
             */
            thread.abortProcessing();
            try {
                /*
                 * Since we decided to abort the thread by force, we need to wait until it ends. 
                 */
                thread.join(1000);
            } catch (InterruptedException e) {
                // do nothing
            }
            if (thread.isAlive()) {
                /*
                 * This means that we need to be even more brutal. the Thread.interrupt() method won't really
                 * do anything if the thread has gone into an infinite loop. Thread.stop() will.
                 */
                thread.stop();
                try {
                    /*
                     * Since we decided to abort the thread by force, we need to wait until it ends. 
                     */
                    thread.join(1000);
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
            throw new ProcessingAbortedException();
        }
        else {
            /*
             * This means that the extraction thread has ended, which in turn means that the extraction
             * process has ended with a success, or the extractor has thrown some exception, if there are any
             * exceptions we need to propagate them further to the user.
             */
            Exception e = thread.getException();
            if (e != null) {
                throw e;
            }
        }
    }
    
    public void suspendMonitoring() {
        suspendMonitoring = true;
    }
    
    public void resumeMonitoring() {
        monitoredStream.touch();
        suspendMonitoring = false;
    }
    
    private void safelySleep(long ms) {
        long start = System.currentTimeMillis();
        long now = System.currentTimeMillis();
        while (now < start + ms) {
            try {
                Thread.sleep(start + ms - now);
            }
            catch (InterruptedException e) {
                // do nothing
            }
            now = System.currentTimeMillis();
        }
    }
    
    public static abstract class StreamConsumer extends Thread {
        public abstract void abortProcessing();
        public abstract Exception getException();
    }

    public static interface StreamConsumerFactory {
        public StreamConsumer getConsumer(InputStream stream, StreamMonitor monitor);
    }
    
    public static interface StopRequestor {
        public boolean isStopRequested();
    }
    
    
    /**
     * Thrown during processing when the {@link StreamMonitor} decides that the stream consumer has stalled,
     * and has to be killed.
     */
    public static class ProcessingAbortedException extends Exception {
        private static final long serialVersionUID = 5699102874255697906L;
    }
    
    /** Thrown during processing when {@link StopRequestor#isStopRequested()} returns true */
    public static class ProcessingInterruptedException extends IOException {
        private static final long serialVersionUID = 7927176466287498836L;
    }
    
    private static class MonitoredStream extends FilterInputStream {

        private long lastAccessTime;

        private boolean allBytesRead;

        private int totalBytesRead;
        
        private StreamMonitor.StopRequestor stopRequestor;

        /**
         * Main constructor
         * 
         * @param in the {@link InputStream} that is to be monitored
         * @param stopRequestor an optional argument, can be null, if an implementation is provided, it will be
         *            consulted before each read operation. If it returns true, the read operation will throw a
         *            {@link MonitoringInterruptedException} to the caller.
         */
        public MonitoredStream(InputStream in, StreamMonitor.StopRequestor stopRequestor) {
            super(in);

            touch();
            allBytesRead = false;
            totalBytesRead = 0;
            this.stopRequestor = stopRequestor;
        }

        /**
         * Synchronized because java doesn't guarantee that reads and writes to a long are atomic
         */
        public synchronized void touch() {
            lastAccessTime = System.currentTimeMillis();
        }

        /**
         * Synchronized because java doesn't guarantee that reads and writes to a long are atomic
         */
        public synchronized long getLastAccessTime() {
            return lastAccessTime;
        }

        public boolean allBytesRead() {
            return allBytesRead;
        }

        public int getTotalBytesRead() {
            return totalBytesRead;
        }

        public int read() throws IOException {
            checkStopRequested();

            int result = super.read();

            if (result >= 0) {
                touch();
                totalBytesRead++;
            }
            else {
                allBytesRead = true;
            }

            return result;
        }

        public int read(byte[] b) throws IOException {
            checkStopRequested();

            int result = super.read(b);

            if (result >= 0) {
                touch();
                totalBytesRead += result;
            }
            else {
                allBytesRead = true;
            }

            return result;
        }

        public int read(byte[] b, int off, int len) throws IOException {
            checkStopRequested();

            int result = super.read(b, off, len);

            if (result >= 0) {
                touch();
                totalBytesRead += result;
            }
            else {
                allBytesRead = true;
            }

            return result;
        }

        public void close() throws IOException {
            super.close();
            allBytesRead = true;
        }

        private void checkStopRequested() throws StreamMonitor.ProcessingInterruptedException {
            if (stopRequestor != null && stopRequestor.isStopRequested()) {
                throw new StreamMonitor.ProcessingInterruptedException();
            }
        }
    }
}


