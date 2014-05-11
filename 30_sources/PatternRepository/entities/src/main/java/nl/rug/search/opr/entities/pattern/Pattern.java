package nl.rug.search.opr.entities.pattern;

import java.util.List;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import nl.rug.search.opr.entities.BaseEntity;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 06.10.09
 */

@Entity
public class Pattern extends BaseEntity<Pattern> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator="PK_PATTERN")
    @TableGenerator(name="PK_PATTERN",allocationSize=5,initialValue=0)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false,unique=true)
    private String uniqueName;

    @OneToOne
    private PatternVersion currentVersion;

    @ManyToMany
    private Collection<Tag> tags = new ArrayList<Tag>();

    @ManyToMany
    private Collection<Category> categories = new ArrayList<Category>();

    @OneToMany
    @JoinTable(name="PATTERN_VERSIONS")
    private Collection<PatternVersion> versions = new ArrayList<PatternVersion>();
    
    @OneToMany(mappedBy = "patternA")
    private Collection<Relationship> relations = new ArrayList<Relationship>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }

    public Collection<PatternVersion> getVersions() {
        return versions;
    }

    public void setVersions(Collection<PatternVersion> versions) {
        this.versions = versions;
    }

    public PatternVersion getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(PatternVersion currentVersion) {
        this.currentVersion = currentVersion;
    }

    public Collection<Relationship> getRelations() {
        return relations;
    }

    public void setRelations(Collection<Relationship> relations) {
        this.relations = relations;
    }

    @Override
    public boolean dataEquals(Pattern other) {
        //TODO define unique attributes
        return areEqual(uniqueName, other.uniqueName, true);
    }

    @Override
    protected Object[] getHashCodeData() {
        return new Object[]{uniqueName};
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    @Override
    public Pattern getThis() {
        return this;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
