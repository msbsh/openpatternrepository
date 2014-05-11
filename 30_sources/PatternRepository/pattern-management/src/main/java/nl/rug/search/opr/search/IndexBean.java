package nl.rug.search.opr.search;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import nl.rug.search.opr.ConfigConstants;
import nl.rug.search.opr.Configuration;
import nl.rug.search.opr.search.api.IndexingException;
import nl.rug.search.opr.search.api.IndexingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
@Singleton
public class IndexBean implements IndexLocal, TimedObject {

    private int MAXEXECUTIONS = 3;
    private final Queue<IndexingTask> failedTasks = new ArrayDeque<IndexingTask>();
    private final Logger logger = LoggerFactory.getLogger(IndexBean.class);
    private Timer timer;
    private boolean rebuildIndexRequired = true;
    @Resource
    private TimerService timerService;

    @PostConstruct
    private void init() {
        initTimer();
    }

    @PreDestroy
    private void shutdown(){
        cancelAllTimer();
    }

    private void initTimer() {
        logger.info("IndexBean: create recurring indexing job");

        if (timer == null) {
            int timeout = Configuration.getInstance().getIntProperty(
                    ConfigConstants.INDEX_TIMEOUT_MS, 90000);

            if (Configuration.getInstance().isProperty(
                    ConfigConstants.AUTO_RESTORE_INDEX, true) == false) {
                logger.warn("Index restoring was disabled by configuration");
                return;
            }
            timer = timerService.createTimer(timeout, null);
        }
    }

    private void cancelAllTimer() {
        for (Timer t : timerService.getTimers()) {
            t.cancel();
        }
        timer = null;
    }

    @Override
    public void indexTask(IndexingTask task) {

        try {
            task.execute();
        } catch (IndexingException ex) {
            if (task.getExecutions() <= MAXEXECUTIONS) {
                synchronized (failedTasks) {
                    failedTasks.add(task);
                }
                logger.warn("IndexBean: Task failed but will be rescheduled");
            } else {
                synchronized (failedTasks) {
                    failedTasks.clear();
                }
                rebuildIndexRequired = true;
                logger.warn("IndexBean: Task failed. The index will be recreated.", ex);
            }
        } catch (Exception ex) {
            logger.error("IndexBean: Task failed. Serious error. The index task will be ommited.", ex);
        }
    }

    @Override
    public void ejbTimeout(Timer timer) {
        //logger.info("IndexBean: Executing indexing indexing job");
        cancelAllTimer();
        restoreIndex();
        initTimer();
    }

    @Override
    public boolean isRebuildIndexRequired() {
        return rebuildIndexRequired;
    }

    @Override
    public void resetRebuildIndexRequired() {
        rebuildIndexRequired = false;
    }

    @Override
    public void resetIndexing() {
        resetRebuildIndexRequired();
        synchronized (failedTasks) {
            failedTasks.clear();
        }
    }

    private void restoreIndex() {
        Collection<IndexingTask> tasks = new LinkedList<IndexingTask>();

        synchronized (failedTasks) {
            tasks.addAll(failedTasks);
            failedTasks.clear();
        }

        for (IndexingTask task : tasks) {
            indexTask(task);
        }
    }
}
