package nl.rug.search.opr.entities.pattern.relation;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import nl.rug.search.opr.entities.AmbiguousEntity;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 07.10.2009
 */
@Entity
public abstract class RelationshipType extends AmbiguousEntity<RelationshipType> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_RELATIONSHIPTYPE")
    @TableGenerator(name = "PK_RELATIONSHIPTYPE", allocationSize = 5, initialValue = 0)
    private Long id;
    
    private String description;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    @Override
    public abstract RelationshipType getThis();
}
