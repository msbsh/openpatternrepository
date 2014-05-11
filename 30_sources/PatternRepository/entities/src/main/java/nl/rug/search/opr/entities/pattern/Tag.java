package nl.rug.search.opr.entities.pattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.TableGenerator;
import nl.rug.search.opr.entities.BaseEntity;


/**
 *
 * @author cm
 */
@Entity
public class Tag extends BaseEntity<Tag> implements Serializable {


    public final static long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE , generator="PK_TAG")
    @TableGenerator(name="PK_TAG",
                    allocationSize=5,
                    initialValue=0)
    private Long id;
        
    @Column(unique=true,nullable=false)
    private String name;
    
    @ManyToMany(mappedBy = "tags")
    private Collection<Pattern> tagPatterns = new ArrayList<Pattern>();

    public Tag(){};
    public Tag(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Pattern> getTagPatterns() {
        return tagPatterns;
    }

    public void setTagPatterns(Collection<Pattern> tagPatterns) {
        this.tagPatterns = tagPatterns;
    }

    @Override
    protected boolean dataEquals(Tag other) {
        return areEqual(name, other.name, true);
    }

    @Override
    protected Object[] getHashCodeData() {
        return new Object[]{name};
    }

    @Override
    public Serializable getPk() {
        return id;
    }

    @Override
    public Tag getThis() {
        return this;
    }
    
}
