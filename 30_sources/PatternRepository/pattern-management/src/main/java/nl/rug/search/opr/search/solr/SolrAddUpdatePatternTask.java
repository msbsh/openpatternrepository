package nl.rug.search.opr.search.solr;

import java.util.Collection;
import nl.rug.search.opr.entities.pattern.File;
import java.io.IOException;
import java.util.logging.Logger;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.search.api.IndexingException;
import nl.rug.search.opr.search.api.IndexingTask;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import static nl.rug.search.opr.search.solr.SolrDocumentConverter.convert;
import static nl.rug.search.opr.search.solr.SolrConnectionHelper.getServer;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
class SolrAddUpdatePatternTask extends IndexingTask {

    private SolrInputDocument pattern;
    private Pattern originalPattern;
    private static final Logger logger = Logger.getLogger(SolrAddUpdatePatternTask.class.getName());
    private Collection<File> files;

    SolrAddUpdatePatternTask(Pattern pattern) {
        this.pattern = convert(pattern);
        this.originalPattern = pattern;
        files = pattern.getCurrentVersion().getFiles();
    }

    @Override
    public void flow() throws IndexingException {
        logger.fine(String.format(
                "AddUpdatePattern task was started: Pattern %s", originalPattern.getName()));
        try {

            StringBuilder fileContent = new StringBuilder();
            SolrFileContentExtractor extractor = new SolrFileContentExtractor();
            for (File f : files) {
                fileContent.append(extractor.getContentFromFile(f));
            }

            if (fileContent.length() > 0) {
                pattern.addField("filecontent",fileContent.toString());
            }

            getServer().add(pattern);
            UpdateResponse response = getServer().commit();
            getServer().commit();
        } catch (SolrServerException ex) {
            throw new IndexingException(ex);
        } catch (IOException ex) {
            throw new IndexingException(ex);
        }

    }
}
