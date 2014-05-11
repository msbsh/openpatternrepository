package nl.rug.search.opr.entities.pattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import nl.rug.search.opr.entities.AmbiguousEntity;
import nl.rug.search.opr.entities.template.Template;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @version 2.0
 */

@Entity
public class PatternVersion extends AmbiguousEntity<PatternVersion> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_PATTERNVERSION")
    @TableGenerator(name = "PK_PATTERNVERSION", allocationSize = 5, initialValue = 0)
    private Long id;
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "PATTERNVERSION_DESCRIPTIONS")
    private List<TextBlock> description = new ArrayList<TextBlock>();
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "PATTERNVERSION_PROBLEMS")
    private List<TextBlock> problem = new ArrayList<TextBlock>();
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "PATTERNVERSION_CONTEXTS")
    private List<TextBlock> context = new ArrayList<TextBlock>();
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "PATTERNVERSION_SOLUTIONS")
    private List<TextBlock> solution = new ArrayList<TextBlock>();
    private String source;
    private String author;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date documentedWhen;
    @ManyToOne
    private License license;
    @ManyToMany
    private List<File> files = new ArrayList<File>();
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Consequence> consequences = new ArrayList<Consequence>();
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Force> forces = new ArrayList<Force>();
    @ManyToOne
    private Template template;

    public static PatternVersion createPatternVersion(String author,
            Collection<TextBlock> textBlocks, List<Consequence> consequences,
            Date documentedWhen, List<File> files, List<Force> forces,
            License license, String source, Template template) {
        PatternVersion p = new PatternVersion();
        p.setAuthor(author);
        p.setBlocks(textBlocks);
        p.setConsequences(consequences);
        p.setDocumentedWhen(documentedWhen);
        p.setFiles(files);
        p.setForces(forces);
        p.setLicense(license);
        p.setSource(source);
        p.setTemplate(template);
        return p;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Date getDocumentedWhen() {
        return documentedWhen;
    }

    public void setDocumentedWhen(Date documentedWhen) {
        this.documentedWhen = documentedWhen;
    }

    public Collection<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<Consequence> getConsequences() {
        return consequences;
    }

    public void setConsequences(List<Consequence> consequences) {
        this.consequences = consequences;
    }

    public List<Force> getForces() {
        return forces;
    }

    public void setForces(List<Force> forces) {
        this.forces = forces;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public List<TextBlock> getContext() {
        return context;
    }

    public void setContext(List<TextBlock> context) {
        this.context = context;
    }

    public List<TextBlock> getDescription() {
        return description;
    }

    public void setDescription(List<TextBlock> description) {
        this.description = description;
    }

    public List<TextBlock> getProblem() {
        return problem;
    }

    public void setProblem(List<TextBlock> problem) {
        this.problem = problem;
    }

    public List<TextBlock> getSolution() {
        return solution;
    }

    public void setSolution(List<TextBlock> solution) {
        this.solution = solution;
    }

    public Collection<TextBlock> getBlocks() {
        Collection<TextBlock> blocks = new ArrayList<TextBlock>();
        for (TextBlock b : problem) {
            blocks.add(b);
        }
        for (TextBlock b : solution) {
            blocks.add(b);
        }
        for (TextBlock b : description) {
            blocks.add(b);
        }
        for (TextBlock b : context) {
            blocks.add(b);
        }

        return blocks;
    }

    public void setBlocks(Collection<TextBlock> blocks) {
        problem = new ArrayList<TextBlock>();
        description = new ArrayList<TextBlock>();
        context = new ArrayList<TextBlock>();
        solution = new ArrayList<TextBlock>();


        for (TextBlock block : blocks) {

            switch (block.getComponent().getType()) {
                case CONTEXT:
                    context.add(block);
                    break;
                case DESCRIPTION:
                    description.add(block);
                    break;
                case PROBLEM:
                    problem.add(block);
                    break;
                case SOLUTION:
                    solution.add(block);
                    break;
            }

        }
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    @Override
    public PatternVersion getThis() {
        return this;
    }
}
