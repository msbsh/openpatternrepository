package nl.rug.search.opr.webservices.types;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.entities.pattern.Force;
import nl.rug.search.opr.entities.pattern.Indicator;
import nl.rug.search.opr.entities.pattern.License;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.QualityAttribute;
import nl.rug.search.opr.entities.pattern.Tag;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.entities.template.Component;
import nl.rug.search.opr.entities.template.Template;
import nl.rug.search.opr.entities.template.Type;

/**
 *
 * @author Lydia
 */
public class PatternData {

    private Collection<TextBlock> blocks;
    private List<File> files;

    public PatternData() {
        files = createFiles();
        blocks = createBlocks();
    }

    public List<Component> createComponents() {
        List<Component> posaComponents = new LinkedList<Component>();
        posaComponents.add(Component.createComponent("example", "Example", "", false, Type.PROBLEM, 0));
        posaComponents.add(Component.createComponent("context", "Context", "", false, Type.CONTEXT, 1));
        posaComponents.add(Component.createComponent("problem", "Problem", "", false, Type.PROBLEM, 2));
        posaComponents.add(Component.createComponent("solution", "Solution", "", false, Type.SOLUTION, 3));
        posaComponents.add(Component.createComponent("structure", "Structure", "", false, Type.SOLUTION, 4));
        posaComponents.add(Component.createComponent("dynamics", "Dynamics", "", false, Type.SOLUTION, 5));
        posaComponents.add(Component.createComponent("implementation", "Implementation", "", false, Type.SOLUTION, 6));
        posaComponents.add(Component.createComponent("consequences", "Consequences", "", false, Type.CONSEQUENCES, 7));

        return posaComponents;
    }

    private List<TextBlock> createBlocks() {
        List<TextBlock> blocksCollection = new LinkedList<TextBlock>();

        TextBlock block1 = new TextBlock();
        block1.setText("example Block");
        block1.setComponent(createComponents().get(0));

        TextBlock block2 = new TextBlock();
        block2.setText("context Block");
        block2.setComponent(createComponents().get(1));

        TextBlock block3 = new TextBlock();
        block3.setText("problem Block");
        block3.setComponent(createComponents().get(2));

        TextBlock block4 = new TextBlock();
        block4.setText("solution Block");
        block4.setComponent(createComponents().get(3));

        TextBlock block5 = new TextBlock();
        block5.setText("structure Block");
        block5.setComponent(createComponents().get(4));

        TextBlock block6 = new TextBlock();
        block6.setText("dynamics Block");
        block6.setComponent(createComponents().get(5));

        TextBlock block7 = new TextBlock();
        block7.setText("implementation Block");
        block7.setComponent(createComponents().get(6));

        TextBlock block8 = new TextBlock();
        block8.setText("consequences Block");
        block8.setComponent(createComponents().get(7));

        blocksCollection.add(block1);
        blocksCollection.add(block2);
        blocksCollection.add(block3);
        blocksCollection.add(block4);
        blocksCollection.add(block5);
        blocksCollection.add(block6);
        blocksCollection.add(block7);
        blocksCollection.add(block8);

        return blocksCollection;
    }

    public List<Consequence> createConsequences() {
        List<Consequence> consequencesCreated = new LinkedList<Consequence>();
        Consequence consequence1 = new Consequence("Description 1", getQualityAttributes().get(4), Indicator.negative);
        Consequence consequence2 = new Consequence("Description 2", getQualityAttributes().get(7), Indicator.verypositive);

        consequencesCreated.add(consequence1);
        consequencesCreated.add(consequence2);

        return consequencesCreated;
    }

    private List<File> createFiles() {
        List<File> filesCreated = new LinkedList<File>();

        File file1 = new File("image-ref", "image");
        License license = new License("Creative Commons", false, "Test DB Setup");
        file1.setLicense(license);
        file1.setId(new Long("2"));

        filesCreated.add(file1);

        return filesCreated;
    }

    public List<Force> createForces() {
        List<Force> forcesCreated = new LinkedList<Force>();

        forcesCreated.add(new Force("Description 1", "Functionality 1", getQualityAttributes().get(2), Indicator.negative));
        forcesCreated.add(new Force("Description 2", "Functionality 2", getQualityAttributes().get(6), Indicator.verypositive));

        return forcesCreated;
    }

    public List<QualityAttribute> getQualityAttributes() {
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

        return qa;
    }

    public Collection<Tag> createTags() {
        Collection<Tag> tags = new LinkedList<Tag>();
        Tag tag1 = new Tag("additional");
        Tag tag2 = new Tag("work");
        Tag tag3 = new Tag("testTag");

        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);

        return tags;
    }

    public List<String> getTags() {
        List<String> returnTags = new LinkedList<String>();
        Collection<Tag> tags = createTags();
        for (Tag tag : tags) {
            returnTags.add(tag.getName());
        }

        return returnTags;
    }

    public List<Content> createContent() {
        List<Content> contents = new LinkedList<Content>();

        for (TextBlock tb : blocks) {
            contents.add(Content.assemble(tb));
        }

        return contents;
    }

    public List<FileDTO> createFileDTO() {
        List<FileDTO> fileDTOs = new LinkedList<FileDTO>();

        for (File file : files) {
            fileDTOs.add(FileDTO.assemble(file));
        }

        return fileDTOs;
    }

    public Collection<Category> createCategories() {
        Collection<Category> categories = new LinkedList<Category>();
        Category categorie1 = new Category("Java");
        Category categorie2 = new Category("Web");
        categories.add(categorie1);
        categories.add(categorie2);
        return categories;
    }

    public PatternVersion createPatternVersion(List<Consequence> consequences, Date documentedWhen, List<Force> forces, License versionLicense) {
        return PatternVersion.createPatternVersion("Lydia Wall", createBlocks(), consequences, documentedWhen, createFiles(), forces, versionLicense, "DTO-Test", Template.createTemplate("Technology", "Lilly", "Template used to describe technologies, frameworks and other existing software systems", createComponents()));
    }

//    public Pattern getPattern(String uniquName, PatternVersion patternVersion) {
//        Pattern pattern = new Pattern();
//        pattern.setCategories(createCategories());
//        pattern.setCurrentVersion(patternVersion);
//        pattern.setId(new Long("4"));
//        pattern.setName("Pattern very nice");
//        pattern.setRelations(new LinkedList<Relationship>());
//        pattern.setTags(createTags());
//        pattern.setUniqueName(uniquName);
//        return pattern;
//    }

    Pattern getPattern(String uniqueName, String id, String name, PatternVersion patternVersion) {
        Pattern pattern = new Pattern();
        pattern.setCategories(createCategories());
        pattern.setCurrentVersion(patternVersion);
        pattern.setId(new Long(id));
        pattern.setName(name);
        pattern.setRelations(new LinkedList<Relationship>());
        pattern.setTags(createTags());
        pattern.setUniqueName(uniqueName);
        return pattern;
    }
}
