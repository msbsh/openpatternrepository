package nl.rug.search.opr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.entities.pattern.Force;
import nl.rug.search.opr.entities.pattern.License;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.QualityAttribute;
import nl.rug.search.opr.entities.pattern.Tag;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.pattern.relation.Alternative;
import nl.rug.search.opr.entities.pattern.relation.Combination;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.entities.pattern.relation.RelationshipType;
import nl.rug.search.opr.entities.pattern.relation.Variant;
import nl.rug.search.opr.entities.template.Component;
import nl.rug.search.opr.entities.template.Template;
import nl.rug.search.opr.entities.template.Type;

/**
 *
 * @author Georg Fleischer
 */
public class PatternDatabase {

    List<Category> categories;
    List<License> licences;
    List<Template> templates;
    List<QualityAttribute> qualityAttributes;
    List<Tag> tags;
    List<Pattern> patterns;
    EMWrapperLocal entityManager = null;
    static PatternDatabase instance = null;

    public static PatternDatabase getInstance() {
        if (instance == null) {
            instance = new PatternDatabase();
        }
        return instance;
    }

    private PatternDatabase() {
        entityManager = Ejb.createEMWrapperBean();
    }

    public void clearDatabase() {
        clearAllEntities(Relationship.class);
        clearAllEntities(Pattern.class);
        clearAllEntities(PatternVersion.class);

        clearAllEntities(TextBlock.class);

        clearAllEntities(Consequence.class);
        clearAllEntities(Force.class);

        clearAllEntities(Category.class);
        clearAllEntities(Tag.class);
        clearAllEntities(File.class);
        clearAllEntities(License.class);
        clearAllEntities(QualityAttribute.class);
        clearAllEntities(Template.class);
        clearAllEntities(Component.class);
        clearAllEntities(RelationshipType.class);
    }

    public void clearAllEntities(Class entityType) {
        entityManager.queryExecuteUpdate("delete from " + entityType.getSimpleName());
    }

    public void setupCategories() {

        Category rootCategory = new Category("Categories");
        rootCategory.setParent(rootCategory);
        entityManager.persist(rootCategory);

        Category patternCategory = Category.createCategory("Pattern", rootCategory);
        entityManager.persist(patternCategory);

        entityManager.persist(Category.createCategory("Architectural Pattern", patternCategory));
        entityManager.persist(Category.createCategory("Enterprise Pattern", patternCategory));
        entityManager.persist(Category.createCategory("Design Pattern", patternCategory));
        entityManager.persist(Category.createCategory("Analysis Pattern", patternCategory));

        Category technologyCategory = Category.createCategory("Technology", rootCategory);
        entityManager.persist(technologyCategory);
        entityManager.persist(Category.createCategory("Security", technologyCategory));

        categories = entityManager.getAllEntities(Category.class);
    }

    public void setupLicences() {

        entityManager.persist(new License("Creative Commons", false, "Test DB Setup"));
        entityManager.persist(new License("GNU GPL Version 3", false, "Test DB Setup"));
        entityManager.persist(new License("Reserved Copyright", true, "Test DB Setup"));

        licences = entityManager.getAllEntities(License.class);
    }

    public void setupTags() {

        entityManager.persist(new Tag("tag1"));
        entityManager.persist(new Tag("tag2"));
        entityManager.persist(new Tag("tag3"));
        entityManager.persist(new Tag("additional"));

        tags = entityManager.getAllEntities(Tag.class);
    }

    public void setupTemplates() {

        List<Component> posaComponents = new LinkedList<Component>();
        posaComponents.add(Component.createComponent("example", "Example", "", false, Type.PROBLEM, 0));
        posaComponents.add(Component.createComponent("context", "Context", "", false, Type.DESCRIPTION, 1));
        posaComponents.add(Component.createComponent("problem", "Problem", "", false, Type.PROBLEM, 2));
        posaComponents.add(Component.createComponent("solution", "Solution", "", false, Type.SOLUTION, 3));
        posaComponents.add(Component.createComponent("structure", "Structure", "", false, Type.SOLUTION, 4));
        posaComponents.add(Component.createComponent("dynamics", "Dynamics", "", false, Type.SOLUTION, 5));
        posaComponents.add(Component.createComponent("implementation", "Implementation", "", false, Type.SOLUTION, 6));
        posaComponents.add(Component.createComponent("consequences", "Consequences", "", false, Type.CONSEQUENCES, 7));
        entityManager.persist(posaComponents);

        Template posaTemplate = Template.createTemplate("POSA", "Georg Fleischer",
                "Template used in Patter Oriented Software Architecture", posaComponents);
        entityManager.persist(posaTemplate);

        List<Component> tecComponents = new LinkedList<Component>();
        tecComponents.add(Component.createComponent("description", "Description", "", true, Type.DESCRIPTION, 0));
        tecComponents.add(Component.createComponent("version", "Version", "", true, Type.DESCRIPTION, 1));
        tecComponents.add(Component.createComponent("context", "Context", "", true, Type.CONTEXT, 2));
        tecComponents.add(Component.createComponent("problem", "Problem", "", true, Type.PROBLEM, 3));
        tecComponents.add(Component.createComponent("forces", "Forces", "", true, Type.FORCES, 4));
        tecComponents.add(Component.createComponent("solution", "Solution", "", true, Type.SOLUTION, 5));
        tecComponents.add(Component.createComponent("consequences", "Consequences", "", true, Type.CONSEQUENCES, 6));
        tecComponents.add(Component.createComponent("known uses", "Known uses", "", true, Type.SOLUTION, 7));
        entityManager.persist(tecComponents);

        Template technologyTemplate = Template.createTemplate("Technology", "Georg Fleischer",
                "Template used to describe technologies, frameworks and other existing software systems", tecComponents);
        entityManager.persist(technologyTemplate);

        templates = entityManager.getAllEntities(Template.class);
    }

    public void setupRelationshipTypes() {
        entityManager.persist(new Alternative());
        entityManager.persist(new Combination());
        entityManager.persist(new Variant());
    }

    public void setupQualityAttributes() {

        List<QualityAttribute> qa = new LinkedList<QualityAttribute>();
        qa.add(new QualityAttribute("Accessibility", ""));
        qa.add(new QualityAttribute("Accountability", ""));
        qa.add(new QualityAttribute("Adaptability", ""));
        qa.add(new QualityAttribute("Availability", ""));
        qa.add(new QualityAttribute("Standardization", ""));
        qa.add(new QualityAttribute("Capability", ""));
        qa.add(new QualityAttribute("Changeability", ""));
        qa.add(new QualityAttribute("Compatibility", ""));
        qa.add(new QualityAttribute("Composability", ""));
        qa.add(new QualityAttribute("Customizability", ""));
        qa.add(new QualityAttribute("Dependability", ""));
        qa.add(new QualityAttribute("Deployability", ""));
        qa.add(new QualityAttribute("Distributability", ""));
        qa.add(new QualityAttribute("Effectiveness", ""));
        qa.add(new QualityAttribute("Efficiency", ""));
        qa.add(new QualityAttribute("Extensibility", ""));
        qa.add(new QualityAttribute("Installability", ""));
        qa.add(new QualityAttribute("Interchangeability", ""));
        qa.add(new QualityAttribute("Interoperability", ""));
        qa.add(new QualityAttribute("Lernability", ""));

        entityManager.persist(qa);

        qualityAttributes = entityManager.getAllEntities(QualityAttribute.class);
    }

    public void setupPatterns() {
        setupPattern1();
        setupPattern2();
        setupPattern3();
        
        patterns = entityManager.getAllEntities(Pattern.class);
    }

    private void setupPattern1() {
        PatternVersion version = new PatternVersion();
        version.setAuthor("PatternDatabase");
        version.setLicense(getLicense(2));
        version.setTemplate(getPosaTemplate());

        LinkedList<Tag> patternTags = new LinkedList<Tag>();
        patternTags.add(tags.get(0));
        patternTags.add(tags.get(1));
        patternTags.add(tags.get(2));

        Pattern p = new Pattern();
        p.setName("DB Pattern 1");
        p.setUniqueName("DBPattern1");
        p.setCurrentVersion(version);
        p.setTags(patternTags);

        entityManager.persist(version);
        entityManager.persist(p);

        Tag tag0 = entityManager.merge(tags.get(0));
        Tag tag1 = entityManager.merge(tags.get(1));
        Tag tag2 = entityManager.merge(tags.get(2));


        Collection<Pattern> tagPattern = new ArrayList<Pattern>();
        tagPattern.add(p);
        tagPattern.addAll(tag0.getTagPatterns());
        tag0.setTagPatterns(tagPattern);
        tagPattern = new ArrayList<Pattern>();
        tagPattern.add(p);
        tagPattern.addAll(tag1.getTagPatterns());
        tag1.setTagPatterns(tagPattern);
        tagPattern = new ArrayList<Pattern>();
        tagPattern.add(p);
        tagPattern.addAll(tag2.getTagPatterns());
        tag2.setTagPatterns(tagPattern);

        entityManager.merge(tag0);
        entityManager.merge(tag1);
        entityManager.merge(tag2);
    }

    private void setupPattern2() {
        PatternVersion version = new PatternVersion();
        version.setAuthor("PatternDatabase");
        version.setLicense(getLicense(1));
        version.setTemplate(getTechnologyTemplate());

        LinkedList<Tag> patternTags = new LinkedList<Tag>();
        patternTags.add(tags.get(1));
        patternTags.add(tags.get(2));
        patternTags.add(tags.get(3));

        Pattern p = new Pattern();
        p.setName("DB Pattern 2");
        p.setUniqueName("DBPattern2");
        p.setCurrentVersion(version);
        p.setTags(patternTags);

        entityManager.persist(version);
        entityManager.persist(p);

        Tag tag1 = entityManager.merge(tags.get(1));
        Tag tag2 = entityManager.merge(tags.get(2));
        Tag tag3 = entityManager.merge(tags.get(3));

        Collection<Pattern> tagPattern = new ArrayList<Pattern>();
        tagPattern.add(p);
        tagPattern.addAll(tag1.getTagPatterns());
        tag1.setTagPatterns(tagPattern);
        tagPattern = new ArrayList<Pattern>();
        tagPattern.add(p);
        tagPattern.addAll(tag2.getTagPatterns());
        tag2.setTagPatterns(tagPattern);
        tagPattern = new ArrayList<Pattern>();
        tagPattern.add(p);
        tagPattern.addAll(tag3.getTagPatterns());
        tag3.setTagPatterns(tagPattern);

        entityManager.merge(tag1);
        entityManager.merge(tag2);
        entityManager.merge(tag3);

    }

    private void setupPattern3() {
        PatternVersion version = new PatternVersion();
        version.setAuthor("PatternDatabase");
        version.setLicense(getLicense(0));
        version.setTemplate(getTechnologyTemplate());

        LinkedList<Tag> patternTags = new LinkedList<Tag>();
        patternTags.add(tags.get(2));

        Pattern p = new Pattern();
        p.setName("DB Pattern 3");
        p.setUniqueName("DBPattern3");
        p.setCurrentVersion(version);
        p.setTags(patternTags);

        entityManager.persist(version);
        entityManager.persist(p);
        
        Tag tag2 = entityManager.merge(tags.get(2));

        Collection<Pattern> tagPattern = new ArrayList<Pattern>();
        tagPattern.add(p);
        tagPattern.addAll(tag2.getTagPatterns());

        tag2.setTagPatterns(tagPattern);

        entityManager.merge(tag2);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category getCategory(int index) {
        return categories.get(index);
    }

    public List<License> getLicences() {
        return licences;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Tag getTag(int index) {
        return tags.get(index);
    }

    public Template getPosaTemplate() {
        return templates.get(0);
    }

    public Template getTechnologyTemplate() {
        return templates.get(1);
    }

    public List<QualityAttribute> getQualityAttributes() {
        return qualityAttributes;
    }

    public QualityAttribute getQualityAttribute(int index) {
        return qualityAttributes.get(index);
    }

    public License getLicense(int index) {
        return licences.get(index);
    }

    public Pattern getPattern(int index) {
        return patterns.get(index);
    }
}
