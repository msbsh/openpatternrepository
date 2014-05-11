package nl.rug.search.opr.search.solr;

import nl.rug.search.opr.entities.pattern.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.search.api.IndexingException;
import nl.rug.search.opr.search.api.IndexingTask;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import static nl.rug.search.opr.search.solr.SolrDocumentConverter.convert;
import static nl.rug.search.opr.search.solr.SolrConnectionHelper.getServer;


/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
class SolrCreateIndexTask extends IndexingTask {

    private Collection<Pattern> patterns;
    private static final Logger logger = Logger.getLogger(SolrCreateIndexTask.class.getName());

    SolrCreateIndexTask(Collection<Pattern> patterns) {
        this.patterns = patterns;
    }

    @Override
    public void flow() throws IndexingException {
        logger.fine("Rebuilding Index task was started");

        try {
            getServer().deleteByQuery("*:*");
            getServer().commit();
            Collection<SolrInputDocument> documents = new LinkedList<SolrInputDocument>();
            for (Pattern pattern : patterns) {
                SolrInputDocument doc = convert(pattern);


                StringBuilder fileContent = new StringBuilder();
                SolrFileContentExtractor extractor = new SolrFileContentExtractor();
                for (File f : pattern.getCurrentVersion().getFiles()) {
                    fileContent.append(extractor.getContentFromFile(f));
                }

                if (fileContent.length() > 0) {
                    doc.addField("filecontent", fileContent.toString());
                }
                documents.add(doc);

            }
            if (!documents.isEmpty()) {
                getServer().add(documents);
                getServer().commit();
            }
        } catch (SolrServerException ex) {
            throw new IndexingException(ex);
        } catch (IOException ex) {
            throw new IndexingException(ex);
        }

    }
}
