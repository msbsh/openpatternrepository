package nl.rug.search.opr.webservices.types;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.entities.pattern.License;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lydia
 */
public class FileDTOConverterTest {

    public FileDTOConverterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void fileDTOConverter() {
        File file = new File("image-ref", "image");
        License license = new License("Creative Commons", false, "Test DB Setup");
        file.setLicense(license);
        file.setId(new Long("2"));

        FileDTO fileDTO = FileDTO.assemble(file);

        assertEquals("image-ref", fileDTO.getName());
        assertEquals(new Long("2"), fileDTO.getId());
        assertEquals(license, fileDTO.getLicense());
        assertEquals("image", fileDTO.getMime());
    }
}
