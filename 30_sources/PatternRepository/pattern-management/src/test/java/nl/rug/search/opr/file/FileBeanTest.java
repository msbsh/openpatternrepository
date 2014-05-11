package nl.rug.search.opr.file;

import java.io.IOException;
import nl.rug.search.opr.Ejb;
import java.util.List;
import java.io.FileNotFoundException;
import nl.rug.search.opr.entities.pattern.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import javax.naming.NamingException;
import nl.rug.search.opr.EJBTestBase;
import nl.rug.search.opr.EMWrapperLocal;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.pattern.LicenseLocal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Georg Fleischer
 * @author Martin Verspai <google@verspai.de>
 */
public class FileBeanTest extends EJBTestBase {

    private FileLocal fileBean;
    private LicenseLocal licenseBean;
    private java.io.File imageFile;
    private java.io.File exeFile;
    private java.io.File pdfFile;
    private java.io.File tooBigFile;
    private File persistedImageFileWithRef;
    private File persistedImageFileWithoutRef;
    private EMWrapperLocal em;

    public FileBeanTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws FileException, FileNotFoundException, IOException {
        database.setupLicences();

        fileBean = Ejb.createFileBean();
        assertNotNull(fileBean);
        licenseBean = Ejb.createLicenseBean();
        assertNotNull(licenseBean);

        em = Ejb.createEMWrapperBean();
        assertNotNull(em);

        imageFile = new java.io.File("./src/test/resources/files/homer.jpg");
        exeFile = new java.io.File("./src/test/resources/files/ex01.exe");
        pdfFile = new java.io.File("./src/test/resources/files/pdf-file.pdf");
        tooBigFile = new java.io.File("./src/test/resources/files/file-too-big.PDF");

        // file with reference to a patternversion
        java.io.File fPersist = new java.io.File("./src/test/resources/files/homer.jpg");
        persistedImageFileWithRef = fileBean.add(database.getLicense(0), "image-ref", new FileInputStream(fPersist));

        PatternVersion patternVersion = new PatternVersion();

        List<File> files = new ArrayList<File>();
        files.add(persistedImageFileWithRef);
        patternVersion.setFiles(files);

        em.persist(patternVersion);

        // file with reference to a patternversion
        fPersist = new java.io.File("./src/test/resources/files/homer.jpg");
        persistedImageFileWithoutRef = fileBean.add(database.getLicense(0), "image-non-ref", new FileInputStream(fPersist));
    }

    @After
    public void tearDown() throws NamingException {
        imageFile = null;
    }

    @Test
    public void canAddFile() throws FileNotFoundException, FileException, IOException {

        assertTrue(imageFile.exists());

        File file = fileBean.add(database.getLicense(0), "homer.jpg", new FileInputStream(imageFile));
        assertNotNull(file);
        assertEquals(imageFile.getName(), file.getName());
        assertEquals(imageFile.length(), file.getSizeInBytes());
        assertEquals("image/jpeg", file.getMime());
    }

    @Test
    public void cannotGetThumbnailOfNonImageFile() throws FileException, FileNotFoundException, IOException {
        // when
        File file = fileBean.add(database.getLicense(1), "testH", new FileInputStream(pdfFile));

        // then
        byte[] thumbnail = fileBean.getThumbnail(file, 900);
        assertNull(thumbnail);
    }

    @Test
    public void getThumbnailNotNull() throws FileException, FileNotFoundException, IOException {
        File file = fileBean.add(database.getLicense(1), "testH", new FileInputStream(imageFile));

        byte[] thumbnail = fileBean.getThumbnail(file, 900);
        assertNotNull(thumbnail);
    }

    @Test
    public void hasReference() throws FileNotFoundException, FileException {
        boolean hasReferences = fileBean.hasReference(persistedImageFileWithRef);
        assertTrue(hasReferences);
    }

    @Test
    public void hasNoReferences() throws FileException, FileNotFoundException {
        boolean hasReferences = fileBean.hasReference(persistedImageFileWithoutRef);
        assertFalse(hasReferences);
    }

    @Test
    public void canDeleteFileWithoutReferences() throws FileException, FileNotFoundException {
        // when
        fileBean.removeFile(persistedImageFileWithoutRef);

        // then
        File removedFile = fileBean.getById(persistedImageFileWithoutRef.getId());
        assertNull(removedFile);
    }

    @Test
    public void cannotDeleteFileWithReferences() {
        // when
        fileBean.removeFile(persistedImageFileWithRef);

        // then
        File removedFile = fileBean.getById(persistedImageFileWithRef.getId());
        assertNotNull(removedFile);
    }

    @Test
    public void canCleanUpFile() {
        persistedImageFileWithoutRef.setDateCreated(new Date(0));
        persistedImageFileWithoutRef = em.merge(persistedImageFileWithoutRef);

        fileBean.cleanUpFiles();

        assertNull(em.find(File.class, persistedImageFileWithoutRef.getId()));
        assertNotNull(persistedImageFileWithRef);
    }

    @Test
    public void cannotCleanUpFile() {
        fileBean.cleanUpFiles();

        assertNotNull(em.find(File.class, persistedImageFileWithoutRef.getId()));
        assertNotNull(persistedImageFileWithRef);
    }

    @Test
    public void canGetSupportedMimeTypes(){
        // given

        // when
        List<String>mimeTypes = fileBean.getSupportedMimeTypes();

        // then
        assertThat(mimeTypes.size(), is(4));
        assertThat(mimeTypes.get(0), is("image/png"));
        assertThat(mimeTypes.get(1), is("image/jpeg"));
        assertThat(mimeTypes.get(2), is("image/gif"));
        assertThat(mimeTypes.get(3), is("application/pdf"));
    }

    @Test(expected=FileException.class)
    public void cannotAddUnsupportedFile() throws FileException, IOException{
        // when
        fileBean.add(database.getLicense(1), "testH", new FileInputStream(exeFile));
    }

    @Test
    public void canGetMaximumFileSize(){
        // when
        int fileSizeMb = fileBean.getMaximumFileSizeMb();

        // then
        assertEquals(3, fileSizeMb);
    }

    @Test(expected=FileException.class)
    public void cannotAddFileTooBig() throws FileException, IOException{
        // given
        FileInputStream is = new FileInputStream(tooBigFile);

        // when
        File file = fileBean.add(database.getLicense(1), "testH", is);
    }
}
