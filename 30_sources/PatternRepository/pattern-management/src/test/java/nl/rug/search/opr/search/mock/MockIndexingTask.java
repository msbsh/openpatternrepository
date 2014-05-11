package nl.rug.search.opr.search.mock;

import java.util.logging.Logger;
import nl.rug.search.opr.search.api.IndexingException;
import nl.rug.search.opr.search.api.IndexingTask;

/**
 *
 * @author Georg Fleischer
 */
public class MockIndexingTask extends IndexingTask {

    private static final Logger logger = Logger.getLogger(MockIndexingTask.class.getName());

    private int timesExecuted = 0;
    private boolean throwIndexingException = false;
    private boolean throwRuntimeException = false;
            
    public MockIndexingTask(boolean throwIndexingException) {
        this.throwIndexingException = throwIndexingException;
        this.throwRuntimeException = false;

    }
    public MockIndexingTask(boolean throwIndexingException, boolean throwRuntimeException) {
        this.throwIndexingException = throwIndexingException;
        this.throwRuntimeException = throwRuntimeException;
    }

    @Override
    public void flow() throws IndexingException {
        logger.info("Mock Index Task executed");
        timesExecuted++;
        if(throwIndexingException){
            throw new IndexingException("Wanted for test!");
        }
        if (throwRuntimeException){
            throw new RuntimeException("Serious Runtime Exception");
        }
    }

    public int getTimesExecuted() {
        return timesExecuted;
    }

    public void resetTimesExecuted(){
        timesExecuted = 0;
    }
}
