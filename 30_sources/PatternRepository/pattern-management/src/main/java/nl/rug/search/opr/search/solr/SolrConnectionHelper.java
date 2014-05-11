
package nl.rug.search.opr.search.solr;

import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cm
 */
class SolrConnectionHelper {

    private static final Logger logger = LoggerFactory.getLogger(SolrConnectionHelper.class);
    private static final SolrServer server;
    public  static final String DEFAULTSERVERADDR = "http://localhost:8143/solr/";

    static {
        String serverUri = "";

        if ((serverUri = System.getProperty("solr.uri")) == null) {
            logger.info("Didn't find entry solr.uri in the system properties");
            serverUri = DEFAULTSERVERADDR;
        }

        logger.info("Using Solr uri: ".concat(serverUri));

        server = initializeConnection(serverUri);
    }

    static SolrServer getServer() throws SolrServerException {
        if (server==null) {
            throw new SolrServerException("No Connection to SolrServer");
        }
        return server;
    }

    static SolrServer initializeConnection(String serverUri) {

        if (server!=null) return server;
        SolrServer tmp = null;
        try {
            logger.info("Initialize connection to Solr Server");
            tmp = new CommonsHttpSolrServer(serverUri);
            SolrPingResponse pong = tmp.ping();
            logger.info("Solr Search Server responded within " + pong.getElapsedTime() + "ms");
        } catch (MalformedURLException mue) {
            logger.error(mue.getMessage(), mue);
            return null;
        } catch (SolrServerException sse) {
            logger.error(sse.getMessage(), sse);
            return null;
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
            return null;
        }
        return tmp;
    }
}
