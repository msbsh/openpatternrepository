package nl.rug.search.opr.webservices.types;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.entities.pattern.Force;
import nl.rug.search.opr.entities.pattern.License;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.Tag;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.pattern.relation.Relationship;

/**
 *
 * @author cm
 */
@XmlRootElement(name="pattern")
public class PatternDTO {


    public PatternDTO() {}

    private Long id;
    private String name;
    private String wikiName;
    

    private List<RelationshipDTO> relationship;
    private List<String> tag;
    private List<String> category;
    private List<Consequence> consequence;
    private List<FileDTO> file;
    private List<Force> force;
    private List<Content> content;
    private Long versionId;
    private Date documentedWhen;
    private String source;
    private License license;
    private String author;
    private String template;

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RelationshipDTO> getRelationship() {
        return relationship;
    }

    public void setRelationship(List<RelationshipDTO> relations) {
        this.relationship = relations;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tags) {
        this.tag = tags;
    }

    public String getWikiName() {
        return wikiName;
    }

    public void setWikiName(String wikiName) {
        this.wikiName = wikiName;
    }

    public Date getDocumentedWhen() {
        return documentedWhen;
    }

    public void setDocumentedWhen(Date documentedWhen) {
        this.documentedWhen = documentedWhen;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Consequence> getConsequence() {
        return consequence;
    }

    public void setConsequence(List<Consequence> consequence) {
        this.consequence = consequence;
    }

    public List<Force> getForce() {
        return force;
    }

    public void setForce(List<Force> force) {
        this.force = force;
    }

    public List<FileDTO> getFile() {
        return file;
    }

    public void setFile(List<FileDTO> file) {
        this.file = file;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }


    

    public static PatternDTO assemble(Pattern p) {
        if (p==null) return new PatternDTO();
        PatternDTO pdto = new PatternDTO();

        pdto.setName(p.getName());
        pdto.setWikiName(p.getUniqueName());
        pdto.setId(p.getId());

        List<RelationshipDTO> rdto = new LinkedList<RelationshipDTO>();
        for (Relationship r : p.getRelations()) {
            rdto.add(RelationshipDTO.assemble(p, r));
        }
        pdto.setRelationship(rdto);

        List<String> tags = new LinkedList<String>();
        for (Tag t : p.getTags()) {
            tags.add(t.getName());
        }
        pdto.setTag(tags);

        List<String> categories = new LinkedList<String>();
        for (Category c : p.getCategories()) {
            categories.add(c.getName());
        }
        pdto.setCategory(categories);

        PatternVersion v = p.getCurrentVersion();

        pdto.setDocumentedWhen(v.getDocumentedWhen());
        pdto.setLicense(v.getLicense());
        pdto.setVersionId(v.getId());
        pdto.setSource(v.getSource());
        pdto.setAuthor(v.getAuthor());
        pdto.setConsequence(v.getConsequences());
        pdto.setForce(v.getForces());

        List<FileDTO> files = new LinkedList<FileDTO>();
        for (File f : v.getFiles()) {
            files.add(FileDTO.assemble(f));
        }
        pdto.setFile(files);

        List<Content> contents = new LinkedList<Content>();
        for (TextBlock tb : v.getBlocks()) {
            contents.add(Content.assemble(tb));
        }
        pdto.setContent(contents);
        pdto.setTemplate(v.getTemplate().getName());

        return pdto;
    }
}
