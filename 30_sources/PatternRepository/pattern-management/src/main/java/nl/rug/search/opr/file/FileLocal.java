package nl.rug.search.opr.file;

import java.io.IOException;
import nl.rug.search.opr.entities.pattern.License;
import nl.rug.search.opr.entities.pattern.File;
import java.io.InputStream;
import java.util.List;
import javax.ejb.Local;
import nl.rug.search.opr.dao.GenericDaoLocal;

/**
 *
 * @author Martin Verspai <google@verspai.de>
 */
@Local
public interface FileLocal extends GenericDaoLocal<File, Long> {

    public void init();

    public byte[] getThumbnail(File file, int size);

    public File add(License license, String name, InputStream is) throws FileException, IOException;

    public byte[] getThumbnail(File file, int size, boolean scaleCubic);

    public boolean hasReference(File f);

    public void removeFile(File file);

    public void cleanUpFiles();

    public List<String> getSupportedMimeTypes();

    public int getMaximumFileSizeMb();
}
