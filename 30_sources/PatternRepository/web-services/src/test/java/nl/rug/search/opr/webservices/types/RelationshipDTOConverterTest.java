package nl.rug.search.opr.webservices.types;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import nl.rug.search.opr.entities.pattern.License;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.relation.Alternative;
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
public class RelationshipDTOConverterTest {

    private PatternData patternData;

    public RelationshipDTOConverterTest() {
        patternData = new PatternData();
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
    public void testRelationshipDTO() {
        License versionLicense = new License("GNU GPL Version 3", false, "Test DB Setup");
        Pattern patternA = patternData.getPattern("PatternA", "3", "Pattern A", patternData.createPatternVersion(patternData.createConsequences(), new Date(), patternData.createForces(), versionLicense));
        Pattern patternB = patternData.getPattern("PatternB", "2", "Pattern B", patternData.createPatternVersion(patternData.createConsequences(), new Date(), patternData.createForces(), versionLicense));
        Relationship relationship = Relationship.createRelationship("PatternRelationship", patternA, patternB, new Alternative());

        RelationshipDTO relationshipDTO = RelationshipDTO.assemble(patternA, relationship);

        List<Relationship> relations = new ArrayList<Relationship>();
        relations.add(relationship);

        patternA.setRelations(relations);
        patternB.setRelations(relations);

        assertEquals("PatternRelationship", relationshipDTO.getDescription());
        assertEquals(new Long("2"), relationshipDTO.getId());
        assertEquals("Pattern B", relationshipDTO.getName());
        assertEquals(RelationType.ALTERNATIVE, relationshipDTO.getType());

        PatternDTO ptr2 = PatternDTO.assemble(patternB);
        assertEquals(new Long("3"), ptr2.getRelationship().get(0).getId());
    }
}
