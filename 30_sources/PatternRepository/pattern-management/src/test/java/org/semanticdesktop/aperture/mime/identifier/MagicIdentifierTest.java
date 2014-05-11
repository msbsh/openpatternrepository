package org.semanticdesktop.aperture.mime.identifier;

import java.io.FileInputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;
import org.semanticdesktop.aperture.util.IOUtil;
import static org.junit.Assert.*;

/**
 *
 * @author Georg Fleischer
 */
public class MagicIdentifierTest {

    private MimeTypeIdentifier identifier;
    private byte[] imageFileJpg;
    private byte[] imageFileExe;
    private byte[] applicationFileExe;
    private byte[] applicationFileJpg;
    private byte[] libraryFileDll;
    private byte[] libraryFilePng;

    public MagicIdentifierTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {
        identifier = new MagicMimeTypeIdentifier();
        imageFileJpg = getBytes("./src/test/resources/files/homer.jpg");
        imageFileExe = getBytes("./src/test/resources/files/homer.exe");
        applicationFileExe = getBytes("./src/test/resources/files/ex01.exe");
        applicationFileJpg = getBytes("./src/test/resources/files/ex01.jpg");
        libraryFileDll = getBytes("./src/test/resources/files/glut32.dll");
        libraryFilePng = getBytes("./src/test/resources/files/glut32.png");
    }

    private byte[] getBytes(String file) throws IOException {
        return IOUtil.readBytes(new FileInputStream(file), identifier.getMinArrayLength());
    }

    @After
    public void tearDown() {
        identifier = null;
        imageFileJpg = null;
        imageFileExe = null;
        applicationFileExe = null;
        applicationFileJpg = null;
        libraryFileDll = null;
        libraryFilePng = null;
    }

    @Test
    public void canIdentifiyImages() {
        String mime = identifier.identify(imageFileJpg, "", null);
        assertEquals("image/jpeg", mime);

        mime = identifier.identify(imageFileExe, "", null);
        assertEquals("image/jpeg", mime);
    }

    @Test
    public void canIdentifyApplication() {
        String mime = identifier.identify(applicationFileExe, "", null);
        assertEquals("application/x-ms-dos-executable", mime);

        mime = identifier.identify(applicationFileJpg, "", null);
        assertEquals("application/x-ms-dos-executable", mime);
    }

    @Test
    public void canIdentifyLibraries() {
        String mime = identifier.identify(libraryFileDll, "", null);
        assertEquals("application/x-ms-dos-executable", mime);

        mime = identifier.identify(libraryFilePng, "", null);
        assertEquals("application/x-ms-dos-executable", mime);
    }
}
