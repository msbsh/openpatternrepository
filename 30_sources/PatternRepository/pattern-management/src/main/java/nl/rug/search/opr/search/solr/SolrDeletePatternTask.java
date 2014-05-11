
package nl.rug.search.opr.search.solr;

import java.io.IOException;
import java.util.logging.Logger;
import nl.rug.search.opr.search.api.IndexingException;
import nl.rug.search.opr.search.api.IndexingTask;
import static nl.rug.search.opr.search.solr.SolrConnectionHelper.getServer;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
class SolrDeletePatternTask extends IndexingTask {

    private long patternId;
    private static final Logger logger = Logger.getLogger(SolrDeletePatternTask.class.getName());

    SolrDeletePatternTask(long patternId) {
        this.patternId = patternId;
    }

    @Override
    public void flow() throws IndexingException {
        logger.fine(String.format(
                "DeletePattern task was started: Pattern %d", patternId));
        try {
            getServer().deleteById("id:"+patternId);
            getServer().commit();
        } catch (SolrServerException ex) {
            throw new IndexingException(ex);
        } catch (IOException ex) {
            throw new IndexingException(ex);
        }

    }



}
