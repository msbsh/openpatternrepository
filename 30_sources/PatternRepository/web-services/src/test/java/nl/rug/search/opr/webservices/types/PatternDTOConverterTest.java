/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.opr.webservices.types;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.Force;
import nl.rug.search.opr.entities.pattern.License;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
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
public class PatternDTOConverterTest {

    private Date documentedWhen;
    private License versionLicense;
    private List<Consequence> consequences;
    private List<Force> forces;
    private PatternData patternData;
    private PatternVersion patternVersion;

    public PatternDTOConverterTest() {
        patternData = new PatternData();
        documentedWhen = new Date();
        versionLicense = new License("GNU GPL Version 3", false, "Test DB Setup");
        consequences = patternData.createConsequences();
        forces = patternData.createForces();
        patternVersion = patternData.createPatternVersion(consequences, documentedWhen, forces, versionLicense);
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
    public void testTest() {
        Collection<String> setA;
        Collection<String> setB;

        setA = new HashSet<String>();
        setA.add("BLA");
        setA.add("Testing...");
        setB = new HashSet<String>();
        setB.add("Testing...");
        setB.add("BLA");
        assertEquals(setA, setB);
    }

    @Test
    public void testPatternDTO() {

        PatternDTO patternDTO = PatternDTO.assemble(patternData.getPattern("NicePattern", "4", "Pattern very nice", patternVersion));

        assertEquals("Lydia Wall", patternDTO.getAuthor());
        List<String> catigoriesName = new LinkedList<String>();
        catigoriesName.add("Java");
        catigoriesName.add("Web");
        assertEquals(catigoriesName, patternDTO.getCategory());
        assertEquals(consequences, patternDTO.getConsequence());
        compareContent(patternVersion.getBlocks(), patternDTO.getContent());
        assertEquals(documentedWhen, patternDTO.getDocumentedWhen());
        compareFileDTO(patternData.createFileDTO(), patternDTO.getFile());
        assertEquals(forces, patternDTO.getForce());
        assertEquals(new Long("4"), patternDTO.getId());
        assertEquals(versionLicense, patternDTO.getLicense());
        assertEquals("Pattern very nice", patternDTO.getName());
        assertEquals(new LinkedList<Relationship>(), patternDTO.getRelationship());
        assertEquals("DTO-Test", patternDTO.getSource());
        assertEquals(patternData.getTags(), patternDTO.getTag());
        assertEquals("Technology", patternDTO.getTemplate());
        assertEquals(null, patternDTO.getVersionId());
        assertEquals("NicePattern", patternDTO.getWikiName());
    }

    private void compareFileDTO(List<FileDTO> createFileDTO, List<FileDTO> file) {
        int index = 0;
        for (FileDTO fileDTO : createFileDTO) {
            assertEquals(fileDTO.getId(), file.get(index).getId());
            assertEquals(fileDTO.getLicense(), file.get(index).getLicense());
            assertEquals(fileDTO.getMime(), file.get(index).getMime());
            assertEquals(fileDTO.getName(), file.get(index).getName());
            index++;
        }
    }

    private void compareContent(Collection<TextBlock> blocks, List<Content> content) {
        List<Content> contents = new LinkedList<Content>();
        for (TextBlock textBlocks : blocks) {
            contents.add(Content.assemble(textBlocks));
        }

        int index = 0;
        for (Content con : contents) {
            assertEquals(con.getName(), content.get(index).getName());
            assertEquals(con.getSort(), content.get(index).getSort());
            assertEquals(con.getText(), content.get(index).getText());
            assertEquals(con.getType(), content.get(index).getType());
            index++;
        }
    }
}
