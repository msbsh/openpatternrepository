
package nl.rug.search.opr.search;

import javax.ejb.Timer;
import javax.ejb.Local;
import nl.rug.search.opr.search.api.IndexingTask;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
@Local
public interface IndexLocal {

    public void indexTask(IndexingTask index);

    public void resetRebuildIndexRequired();

    public boolean isRebuildIndexRequired();

    public void resetIndexing();

    public void ejbTimeout(Timer timer);
}
