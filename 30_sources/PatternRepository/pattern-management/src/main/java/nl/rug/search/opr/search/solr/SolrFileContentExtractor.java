
package nl.rug.search.opr.search.solr;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.search.api.IndexingException;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.NamedList;
import static nl.rug.search.opr.search.solr.SolrConnectionHelper.getServer;
import static nl.rug.search.utils.TextProcessor.stripHTML;
/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
public class SolrFileContentExtractor {
    
    public String getContentFromFile(File f) throws IndexingException {
        ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
        SolrFileContentStream fileStream = new SolrFileContentStream(f);
        try {
            up.addContentStream(fileStream);
            up.setParam("literal.id", f.getId().toString());
            up.setParam("literal.version", f.getId().toString());
            up.setParam("extractOnly", "true");
            NamedList<Object> result = getServer().request(up);
            if (result != null) {
                Object o = result.get(null);
                if (o instanceof String) {
                    return stripHTML(o.toString());
                }
            }
        } catch (SolrServerException ex) {
            throw new IndexingException(ex);
        } catch (IOException ex) {
            throw new IndexingException(ex);
        }finally{
            try {
                fileStream.getStream().close();
            } catch (IOException ex) {
                Logger.getLogger(SolrFileContentExtractor.class.getName()).log(Level.SEVERE, null, ex);
                throw new IndexingException(ex);
            }
        }
        return "";
    }
}
