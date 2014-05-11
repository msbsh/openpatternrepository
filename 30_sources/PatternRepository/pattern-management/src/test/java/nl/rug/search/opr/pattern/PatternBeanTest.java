package nl.rug.search.opr.pattern;

import java.util.ArrayList;
import java.util.Collection;
import nl.rug.search.opr.entities.pattern.Tag;
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.entities.pattern.Force;
import java.util.Date;
import nl.rug.search.opr.entities.template.Template;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import java.util.List;
import nl.rug.search.opr.entities.pattern.Category;
import java.util.LinkedList;
import nl.rug.search.opr.EJBTestBase;
import nl.rug.search.opr.EMWrapperLocal;
import nl.rug.search.opr.Ejb;
import nl.rug.search.opr.EjbExceptionHandler;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.Indicator;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.pattern.relation.Alternative;
import nl.rug.search.opr.entities.pattern.relation.Combination;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.entities.template.Component;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Georg Fleischer
 */
public class PatternBeanTest extends EJBTestBase {

    PatternLocal patternBean;
    Pattern basicPattern;
    Pattern minimalPattern;
    EMWrapperLocal em;
    EjbExceptionHandler ejbHandler = new EjbExceptionHandler();

    @Before
    public void setUp() {

        patternBean = Ejb.createPatternBean();
        em = Ejb.createEMWrapperBean();

        database.setupCategories();
        database.setupLicences();
        database.setupTags();
        database.setupQualityAttributes();
        database.setupRelationshipTypes();
        database.setupTemplates();
        database.setupPatterns();

        basicPattern = createBasicPattern();
        minimalPattern = createMinimalPattern();
    }

    private Pattern createMinimalPattern() {
        List<Category> categories = new LinkedList<Category>();
        categories.add(database.getCategory(2));

        Pattern p = new Pattern();
        //p.setCategories(categories);
        p.setName("Pattern without files");
        p.setUniqueName("MinimalPattern");
        p.setCurrentVersion(new PatternVersion());
        p.getCurrentVersion().setAuthor("Georg Fleischer");
        p.getCurrentVersion().setLicense(database.getLicense(2));
        p.getCurrentVersion().setTemplate(database.getTechnologyTemplate());

        return p;
    }

    private Pattern createBasicPattern() {

        Template posaTemplate = database.getPosaTemplate();

        List<TextBlock> textBlocks = new LinkedList<TextBlock>();
        textBlocks.add(posaTemplate.createTextBlock(0, "This is an 'example' block"));
        textBlocks.add(posaTemplate.createTextBlock(1, "This is an 'context' block"));
        textBlocks.add(posaTemplate.createTextBlock(2, "This is an 'problem' block"));
        textBlocks.add(posaTemplate.createTextBlock(3, "This is an 'solution' block"));
        textBlocks.add(posaTemplate.createTextBlock(4, "This is an 'structure' block"));
        textBlocks.add(posaTemplate.createTextBlock(5, "This is an 'dynamics' block"));
        textBlocks.add(posaTemplate.createTextBlock(6, "This is an 'implementation' block"));
        textBlocks.add(posaTemplate.createTextBlock(7, "This is an 'consequences' block"));

        List<Consequence> consequences = new LinkedList<Consequence>();
        consequences.add(new Consequence("Description 1", database.getQualityAttribute(0), Indicator.negative));

        List<Force> forces = new LinkedList<Force>();
        forces.add(new Force("Description 1", "Functionality 1", database.getQualityAttribute(0), Indicator.negative));
        forces.add(new Force("Description 2", "Functionality 2", database.getQualityAttribute(10), Indicator.verypositive));

        PatternVersion basicPatternVersion = PatternVersion.createPatternVersion(
                "Georg Fleischer", textBlocks, consequences, new Date(),
                new LinkedList<File>(), forces, database.getLicense(1), "Unit-Test", posaTemplate);

        List<Category> categories = new LinkedList<Category>();
        categories.add(database.getCategory(0));

        List<Tag> tags = database.getTags();

        List<Relationship> relationships = new LinkedList<Relationship>();

        Pattern pattern = new Pattern();
        pattern.setCategories(categories);
        pattern.setCurrentVersion(basicPatternVersion);
        pattern.setName("Pattern without files");
        pattern.setRelations(relationships);
        pattern.setTags(tags);
        pattern.setUniqueName("BasicPatternName");
        return pattern;
    }

    @Test
    public void canAddBasicPattern() {
        Pattern addedPattern = patternBean.add(basicPattern);
        assertNotNull(addedPattern);

        Pattern dbPattern = em.getReference(Pattern.class, addedPattern.getId());
        assertThat(basicPattern, is(dbPattern));
    }

    @Test
    public void canAddMinimalPattern() {
        Pattern addedPattern = patternBean.add(minimalPattern);
        assertNotNull(addedPattern);

        Pattern dbPattern = em.getReference(Pattern.class, addedPattern.getId());
        assertThat(minimalPattern, is(dbPattern));
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddPatternWithoutVersion() throws Exception {
        final Pattern incompletePattern = new Pattern();
        incompletePattern.setName("Pattern");
        incompletePattern.setUniqueName("unique name");

        ejbHandler.execute(new Runnable() {

            @Override
            public void run() {
                patternBean.add(incompletePattern);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddPatternWithoutLicense() throws Exception {
        final Pattern p = new Pattern();
        p.setName("Pattern");
        p.setUniqueName("Pattern");
        p.setCurrentVersion(new PatternVersion());
        p.getCurrentVersion().setAuthor("Georg Fleischer");
        p.getCurrentVersion().setLicense(null);
        p.getCurrentVersion().setTemplate(database.getTechnologyTemplate());

        ejbHandler.execute(new Runnable() {

            @Override
            public void run() {
                patternBean.add(p);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddPatternWithoutTemplate() throws Exception {
        final Pattern p = new Pattern();
        p.setName("Pattern");
        p.setUniqueName("Pattern");
        p.setCurrentVersion(new PatternVersion());
        p.getCurrentVersion().setAuthor("Georg Fleischer");
        p.getCurrentVersion().setLicense(database.getLicense(2));
        p.getCurrentVersion().setTemplate(null);

        ejbHandler.execute(new Runnable() {

            @Override
            public void run() {
                patternBean.add(p);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddPatternWithoutName() throws Exception {
        final Pattern p = new Pattern();
        p.setName(null);
        p.setUniqueName("Pattern");
        p.setCurrentVersion(new PatternVersion());
        p.getCurrentVersion().setAuthor("Georg Fleischer");
        p.getCurrentVersion().setLicense(database.getLicense(2));
        p.getCurrentVersion().setTemplate(database.getTechnologyTemplate());

        ejbHandler.execute(new Runnable() {

            @Override
            public void run() {
                patternBean.add(p);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddPatternWithoutUniqueName() throws Exception {
        final Pattern p = new Pattern();
        p.setName("Pattern");
        p.setUniqueName(null);
        p.setCurrentVersion(new PatternVersion());
        p.getCurrentVersion().setAuthor("Georg Fleischer");
        p.getCurrentVersion().setLicense(database.getLicense(2));
        p.getCurrentVersion().setTemplate(database.getTechnologyTemplate());

        ejbHandler.execute(new Runnable() {

            @Override
            public void run() {
                patternBean.add(p);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddPatternWithExistingUniqueName() throws Exception {
//        minimalPattern.setUniqueName("ExistingUniqueName");
//        patternBean.add(minimalPattern);

        final Pattern p = new Pattern();
        p.setName("Patern");
        p.setUniqueName("DBPattern1");
        p.setCurrentVersion(new PatternVersion());
        p.getCurrentVersion().setAuthor("Georg Fleischer");
        p.getCurrentVersion().setLicense(database.getLicense(2));
        p.getCurrentVersion().setTemplate(database.getTechnologyTemplate());

        ejbHandler.execute(new Runnable() {

            @Override
            public void run() {
                patternBean.add(p);
            }
        });
    }

    @Test
    public void canAddPatternWithRelationships() {
        Collection<Relationship> relations = new LinkedList<Relationship>();
        
        relations.add(Relationship.createRelationship("Bla", basicPattern,
                database.getPattern(0), new Alternative()));

        relations.add(Relationship.createRelationship("TTT", basicPattern,
                database.getPattern(1), new Combination()));

        basicPattern.setRelations(relations);

        /*
         * TODO: This line exists because the db does not get cleaned up properly
         * after each test execution. In this case the PK for "pattern_category"
         * already exists when trying to persist it! The db should be cleaned up
         * poperly before each test call!
         */
        basicPattern.setCategories(new ArrayList<Category>());

        patternBean.add(basicPattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddPatternWithOtherRelationship() throws Exception {

        Collection<Relationship> relations = new LinkedList<Relationship>();
        relations.add(Relationship.createRelationship("Invalid Relationship", database.getPattern(0), database.getPattern(1), new Combination()));

        basicPattern.setRelations(relations);

        ejbHandler.execute(new Runnable() {

            @Override
            public void run() {
                patternBean.add(basicPattern);
            }
        });
    }

    public void cannotAddPatternWithMultipleRelationshipsToSinglePattern() throws Exception {
        Collection<Relationship> relations = new LinkedList<Relationship>();
        relations.add(Relationship.createRelationship("TTT", basicPattern,
                database.getPattern(1), new Combination()));

        relations.add(Relationship.createRelationship("Bla", basicPattern,
                database.getPattern(1), new Combination()));

        basicPattern.setRelations(relations);

        ejbHandler.execute(new Runnable() {

            @Override
            public void run() {
                patternBean.add(basicPattern);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddPatternWithSelfRelationship() throws Exception {
        Collection<Relationship> relations = new LinkedList<Relationship>();
        relations.add(Relationship.createRelationship("TTT", basicPattern,
                basicPattern, new Combination()));

        basicPattern.setRelations(relations);

        ejbHandler.execute(new Runnable() {

            @Override
            public void run() {
                patternBean.add(basicPattern);
            }
        });
    }

    @Test
    public void canGetPatternFromDatabase() {
        Pattern p = (Pattern) patternBean.getById(database.getPattern(0).getId());
        assertThat(p.getName(), is("DB Pattern 1"));
        assertThat(p.getUniqueName(), is("DBPattern1"));

        p = (Pattern) patternBean.getById(database.getPattern(1).getId());
        assertThat(p.getName(), is("DB Pattern 2"));
        assertThat(p.getUniqueName(), is("DBPattern2"));

        p = (Pattern) patternBean.getById(database.getPattern(2).getId());
        assertThat(p.getName(), is("DB Pattern 3"));
        assertThat(p.getUniqueName(), is("DBPattern3"));
    }

//
//    @Test
//    public void canGetPatternByUniqueName() {
//        patternBean.getByUniqueName("BasicPatternName");
//    }
//    @Test
//    public void canRemove() {
//        Pattern pattern = patternBean.add(minimalPattern);
//        patternBean.remove(pattern);
//
//        Pattern dbPattern = em.getReference(Pattern.class, pattern.getId());
//        assertNotSame(pattern, dbPattern);
//    }
    @Test
    public void canGetSimilarPatterns() {
        patternBean.add(minimalPattern);
        patternBean.add(basicPattern);
        List<Pattern> patternList = patternBean.getSimiliarPatterns("file");
        assertEquals(2, patternList.size());

        patternList = patternBean.getSimiliarPatterns("lay");
        assertEquals(0, patternList.size());
    }

    @Test
    public void canGetByUniqueName() {
        patternBean.add(minimalPattern);
        patternBean.add(basicPattern);

        Pattern pattern = patternBean.getByUniqueName("MinimalPattern");
        assertEquals("MinimalPattern", pattern.getUniqueName());

        pattern = patternBean.getByUniqueName("bla");
        assertNull(pattern);

        pattern = patternBean.getByUniqueName("BasicPatternName");
        assertEquals("BasicPatternName", pattern.getUniqueName());
    }

    @Test
    public void canGetSortedComponentList() {
        PatternVersion patternVersion = basicPattern.getCurrentVersion();
        List<Component> componentList = patternBean.getSortedComponentList(patternVersion);
        assertEquals(8, componentList.size());
        assertEquals("example", componentList.get(0).getIdentifier());
        assertEquals("context", componentList.get(1).getIdentifier());
        assertEquals("problem", componentList.get(2).getIdentifier());
        assertEquals("solution", componentList.get(3).getIdentifier());
        assertEquals("structure", componentList.get(4).getIdentifier());
        assertEquals("dynamics", componentList.get(5).getIdentifier());
        assertEquals("implementation", componentList.get(6).getIdentifier());
        assertEquals("consequences", componentList.get(7).getIdentifier());

        componentList = patternBean.getSortedComponentList(minimalPattern.getCurrentVersion());
        assertEquals(8, componentList.size());
        assertEquals("description", componentList.get(0).getIdentifier());
        assertEquals("version", componentList.get(1).getIdentifier());
        assertEquals("context", componentList.get(2).getIdentifier());
        assertEquals("problem", componentList.get(3).getIdentifier());
        assertEquals("forces", componentList.get(4).getIdentifier());
        assertEquals("solution", componentList.get(5).getIdentifier());
        assertEquals("consequences", componentList.get(6).getIdentifier());
        assertEquals("known uses", componentList.get(7).getIdentifier());
    }
}
