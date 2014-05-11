package nl.rug.search.opr.search.solr;

import java.io.IOException;
import java.io.InputStream;
import nl.rug.search.opr.entities.pattern.File;
import org.apache.solr.common.util.ContentStreamBase;

class SolrFileContentStream extends ContentStreamBase {

    private File f;

    public SolrFileContentStream(File file) {
        this.f = file;
        setContentType(f.getMime());
        setName(f.getName());
    }

    @Override
    public InputStream getStream() throws IOException {
        return f.getContentAsStream();
    }
}
