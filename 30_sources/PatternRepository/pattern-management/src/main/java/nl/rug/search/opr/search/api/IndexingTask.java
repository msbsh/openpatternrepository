package nl.rug.search.opr.search.api;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
public abstract class IndexingTask {

    private int executions = 0;

    public abstract void flow() throws IndexingException;

    public final void execute() throws IndexingException {
        executions++;
        flow();
    }

    public int getExecutions() {
        return executions;
    }
}
