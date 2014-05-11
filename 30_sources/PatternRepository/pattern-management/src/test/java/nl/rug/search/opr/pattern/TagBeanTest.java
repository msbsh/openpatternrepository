package nl.rug.search.opr.pattern;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import nl.rug.search.opr.entities.pattern.Pattern;
import java.util.Collection;
import nl.rug.search.opr.Ejb;
import java.util.ArrayList;
import nl.rug.search.opr.entities.pattern.Tag;
import org.junit.Test;
import org.junit.After;
import javax.naming.NamingException;
import nl.rug.search.opr.EJBTestBase;
import nl.rug.search.opr.EMWrapperLocal;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.template.Type;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Jan Nikolai Trzeszkowski <info@j-n-t.de>
 */
public class TagBeanTest extends EJBTestBase {

    private TagLocal tagBean;
    private EMWrapperLocal em;

    @Before
    public void setUp() throws NamingException {

        tagBean = Ejb.createTagBean();
        assertNotNull(tagBean);

        em = Ejb.createEMWrapperBean();
        assertNotNull(em);

        database.clearDatabase();
        database.setupTags();
    }

    @After
    public void tearDown() throws NamingException {
        tagBean = null;
        assertNull(tagBean);

        em = null;
        assertNull(em);
    }

    @Test
    public void canFindTagByName() {
        String tagString = "tag1";

        Tag received = tagBean.getByName(tagString);

        assertNotNull(received);
        assertEquals(tagString, received.getName());
    }

    @Test
    public void cannotFindTagByName() {
        Tag received = tagBean.getByName("tagTest");

        assertNull(received);
    }

    @Test
    public void canFindSimilarTags() {
        Collection<Tag> tagCollection = tagBean.getSimiliarTags("tag");

        assertNotNull(tagCollection);
        assertEquals(3, tagCollection.size());
    }

    @Test
    public void cannotFindSimilarTags() {
        Collection<Tag> tagCollection = tagBean.getSimiliarTags("tags");

        assertNotNull(tagCollection);
        assertTrue(tagCollection.isEmpty());
    }

    @Test
    public void canFindUsedTags() {
        Collection<Tag> usedTags = tagBean.getUsedTags();

        assertNotNull(usedTags);
        assertEquals(database.getTags().size(), usedTags.size());
    }

    @Test
    public void cannotFindUsedTags() {
        database.clearAllEntities(Tag.class);

        Collection<Tag> usedTags = tagBean.getUsedTags();

        assertNotNull(usedTags);
        assertTrue(usedTags.isEmpty());
    }

    @Test
    public void canFindProposedTags() {
        PatternVersion pattern = new PatternVersion();
        Collection<TextBlock> blocks = new ArrayList<TextBlock>();

        TextBlock context = new TextBlock();
        context.setText("context test");
        context.setComponent(new nl.rug.search.opr.entities.template.Component());
        context.getComponent().setType(Type.CONTEXT);
        blocks.add(context);

        TextBlock description = new TextBlock();
        description.setText("description test");
        description.setComponent(new nl.rug.search.opr.entities.template.Component());
        description.getComponent().setType(Type.DESCRIPTION);
        blocks.add(description);

        TextBlock problem = new TextBlock();
        problem.setText("problem text");
        problem.setComponent(new nl.rug.search.opr.entities.template.Component());
        problem.getComponent().setType(Type.PROBLEM);
        blocks.add(problem);

        TextBlock solution = new TextBlock();
        solution.setText("solution text");
        solution.setComponent(new nl.rug.search.opr.entities.template.Component());
        solution.getComponent().setType(Type.SOLUTION);
        blocks.add(solution);

        pattern.setBlocks(blocks);
        Collection<Tag> proposedTags = tagBean.getProposedTags(pattern);

        assertNotNull(proposedTags);
        assertEquals(6, proposedTags.size());
    }

    @Test
    public void canFindProposedTagsCheckBlacklist() {
        PatternVersion pattern = new PatternVersion();
        Collection<TextBlock> blocks = new ArrayList<TextBlock>();

        TextBlock context = new TextBlock();
        context.setText("context test and");
        context.setComponent(new nl.rug.search.opr.entities.template.Component());
        context.getComponent().setType(Type.CONTEXT);
        blocks.add(context);

        TextBlock description = new TextBlock();
        description.setText("description test or");
        description.setComponent(new nl.rug.search.opr.entities.template.Component());
        description.getComponent().setType(Type.DESCRIPTION);
        blocks.add(description);

        TextBlock problem = new TextBlock();
        problem.setText("problem text is");
        problem.setComponent(new nl.rug.search.opr.entities.template.Component());
        problem.getComponent().setType(Type.PROBLEM);
        blocks.add(problem);

        TextBlock solution = new TextBlock();
        solution.setText("solution text");
        solution.setComponent(new nl.rug.search.opr.entities.template.Component());
        solution.getComponent().setType(Type.SOLUTION);
        blocks.add(solution);

        pattern.setBlocks(blocks);
        Collection<Tag> proposedTags = tagBean.getProposedTags(pattern);

        assertNotNull(proposedTags);
        assertEquals(6, proposedTags.size());
    }

    @Test
    public void cannotFindProposedTags() {
        PatternVersion pattern = new PatternVersion();
        Collection<Tag> proposedTags = tagBean.getProposedTags(pattern);

        assertNotNull(proposedTags);
        assertEquals(0, proposedTags.size());


        Collection<TextBlock> blocks = new ArrayList<TextBlock>();
        TextBlock solution = new TextBlock();
        solution.setText("");
        solution.setComponent(new nl.rug.search.opr.entities.template.Component());
        solution.getComponent().setType(Type.SOLUTION);
        blocks.add(solution);

        pattern.setBlocks(blocks);
        Collection<Tag> proposedTags2 = tagBean.getProposedTags(pattern);

        assertNotNull(proposedTags2);
        assertEquals(0, proposedTags2.size());
    }

    @Test
    public void cannotFindProposedTagsCheckBlacklist() {
        PatternVersion pattern = new PatternVersion();
        Collection<TextBlock> blocks = new ArrayList<TextBlock>();

        TextBlock context = new TextBlock();
        context.setText("and");
        context.setComponent(new nl.rug.search.opr.entities.template.Component());
        context.getComponent().setType(Type.CONTEXT);
        blocks.add(context);

        TextBlock description = new TextBlock();
        description.setText("or");
        description.setComponent(new nl.rug.search.opr.entities.template.Component());
        description.getComponent().setType(Type.DESCRIPTION);
        blocks.add(description);

        TextBlock problem = new TextBlock();
        problem.setText("why is");
        problem.setComponent(new nl.rug.search.opr.entities.template.Component());
        problem.getComponent().setType(Type.PROBLEM);
        blocks.add(problem);

        TextBlock solution = new TextBlock();
        solution.setText("each got into");
        solution.setComponent(new nl.rug.search.opr.entities.template.Component());
        solution.getComponent().setType(Type.SOLUTION);
        blocks.add(solution);

        pattern.setBlocks(blocks);
        Collection<Tag> proposedTags = tagBean.getProposedTags(pattern);

        assertNotNull(proposedTags);
        assertEquals(0, proposedTags.size());
    }

    @Test
    public void canFindProposedTagsAmount() {
        PatternVersion pattern = new PatternVersion();
        Collection<TextBlock> blocks = new ArrayList<TextBlock>();

        TextBlock context = new TextBlock();
        context.setText("context test");
        context.setComponent(new nl.rug.search.opr.entities.template.Component());
        context.getComponent().setType(Type.CONTEXT);
        blocks.add(context);

        TextBlock description = new TextBlock();
        description.setText("description test");
        description.setComponent(new nl.rug.search.opr.entities.template.Component());
        description.getComponent().setType(Type.DESCRIPTION);
        blocks.add(description);

        TextBlock problem = new TextBlock();
        problem.setText("problem text");
        problem.setComponent(new nl.rug.search.opr.entities.template.Component());
        problem.getComponent().setType(Type.PROBLEM);
        blocks.add(problem);

        TextBlock solution = new TextBlock();
        solution.setText("solution text");
        solution.setComponent(new nl.rug.search.opr.entities.template.Component());
        solution.getComponent().setType(Type.SOLUTION);
        blocks.add(solution);

        pattern.setBlocks(blocks);
        int maxresults = 4;
        Collection<Tag> proposedTags = tagBean.getProposedTags(pattern, maxresults);

        assertNotNull(proposedTags);
        assertTrue(proposedTags.size() <= maxresults);
    }

    @Test
    public void cannotFindProposedTagsAmount() {
        PatternVersion pattern = new PatternVersion();
        Collection<Tag> proposedTags = tagBean.getProposedTags(pattern);

        assertNotNull(proposedTags);
        assertEquals(0, proposedTags.size());


        Collection<TextBlock> blocks = new ArrayList<TextBlock>();
        TextBlock solution = new TextBlock();
        solution.setText("");
        solution.setComponent(new nl.rug.search.opr.entities.template.Component());
        solution.getComponent().setType(Type.SOLUTION);
        blocks.add(solution);

        pattern.setBlocks(blocks);
        Collection<Tag> proposedTags2 = tagBean.getProposedTags(pattern, Integer.MAX_VALUE);

        assertNotNull(proposedTags2);
        assertEquals(0, proposedTags2.size());
    }

    @Test
    public void canFindProposedTagsCheckBlacklistAmount() {
        PatternVersion pattern = new PatternVersion();
        Collection<TextBlock> blocks = new ArrayList<TextBlock>();

        TextBlock context = new TextBlock();
        context.setText("context test and");
        context.setComponent(new nl.rug.search.opr.entities.template.Component());
        context.getComponent().setType(Type.CONTEXT);
        blocks.add(context);

        TextBlock description = new TextBlock();
        description.setText("description test or");
        description.setComponent(new nl.rug.search.opr.entities.template.Component());
        description.getComponent().setType(Type.DESCRIPTION);
        blocks.add(description);

        TextBlock problem = new TextBlock();
        problem.setText("problem text is");
        problem.setComponent(new nl.rug.search.opr.entities.template.Component());
        problem.getComponent().setType(Type.PROBLEM);
        blocks.add(problem);

        TextBlock solution = new TextBlock();
        solution.setText("solution text");
        solution.setComponent(new nl.rug.search.opr.entities.template.Component());
        solution.getComponent().setType(Type.SOLUTION);
        blocks.add(solution);

        pattern.setBlocks(blocks);

        int maxresults = 2;
        Collection<Tag> proposedTags = tagBean.getProposedTags(pattern, maxresults);

        assertNotNull(proposedTags);
        assertTrue(proposedTags.size() <= maxresults);
    }

    @Test
    public void cannotFindProposedTagsAmountCheckBlacklist() {
        PatternVersion pattern = new PatternVersion();
        Collection<TextBlock> blocks = new ArrayList<TextBlock>();

        TextBlock context = new TextBlock();
        context.setText("and");
        context.setComponent(new nl.rug.search.opr.entities.template.Component());
        context.getComponent().setType(Type.CONTEXT);
        blocks.add(context);

        TextBlock description = new TextBlock();
        description.setText("or");
        description.setComponent(new nl.rug.search.opr.entities.template.Component());
        description.getComponent().setType(Type.DESCRIPTION);
        blocks.add(description);

        TextBlock problem = new TextBlock();
        problem.setText("why is");
        problem.setComponent(new nl.rug.search.opr.entities.template.Component());
        problem.getComponent().setType(Type.PROBLEM);
        blocks.add(problem);

        TextBlock solution = new TextBlock();
        solution.setText("each got into");
        solution.setComponent(new nl.rug.search.opr.entities.template.Component());
        solution.getComponent().setType(Type.SOLUTION);
        blocks.add(solution);

        pattern.setBlocks(blocks);
        Collection<Tag> proposedTags = tagBean.getProposedTags(pattern, Integer.MAX_VALUE);

        assertNotNull(proposedTags);
        assertEquals(0, proposedTags.size());
    }

    @Test
    public void canFindFavouriteTags() {
        database.setupCategories();
        database.setupLicences();
        database.setupQualityAttributes();
        database.setupTemplates();
        database.setupPatterns();

        int maxTags = 2;
        Collection<Tag> favouriteTags = tagBean.getFavouriteTags(maxTags);
        assertTrue(favouriteTags.size() <= maxTags);
        
        Iterator<Tag> tIt = favouriteTags.iterator();
        assertEquals("tag3", tIt.next().getName());
        assertEquals("tag2", tIt.next().getName());
    }

    @Test
    public void cannotFindFavouriteTags() {
        int maxTags = 5;

        database.clearAllEntities(Pattern.class);

        Collection<Tag> favouriteTags = tagBean.getFavouriteTags(maxTags);
        assertTrue(favouriteTags.isEmpty());
    }

    @Test
    public void canPersistTagsByName() {
        List<Tag> newTags = new LinkedList<Tag>();
        newTags.add(new Tag("12345"));
        newTags.add(new Tag("12345"));
        newTags.add(new Tag("tag 112"));
        newTags.add(database.getTag(0));

        Collection<Tag> persistedTags = tagBean.persistByName(newTags);
        assertThat(persistedTags.size(), is(3));
    }

    @Test
    public void canRemoveDuplicates() {
        List<Tag> duplicateTags = new LinkedList<Tag>();
        duplicateTags.add(new Tag("111"));
        duplicateTags.add(new Tag("111"));
        duplicateTags.add(new Tag("1"));
        duplicateTags.add(new Tag("1"));
        duplicateTags.add(new Tag("1"));
        duplicateTags.add(new Tag("2"));

        Collection<Tag> noDuplicates = tagBean.removeDuplicateNames(duplicateTags);
        assertThat(noDuplicates.size(), is(3));
    }
}
