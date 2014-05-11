package nl.rug.search.opr.entities.pattern;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import nl.rug.search.opr.entities.BaseEntity;

/**
 * @author Christian Manteuffel <cm@notagain.de>
 * @author Martin Verspai <martin@verspai.de>
 * @version 2
 */
@Entity
public class QualityAttribute extends BaseEntity<QualityAttribute> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_QA")
    @TableGenerator(name = "PK_QA", allocationSize = 5, initialValue = 0)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    public QualityAttribute() {}
    public QualityAttribute(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    /* Getter and Setter methods */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected boolean dataEquals(QualityAttribute other) {
        return areEqual(name, other.name, true);
    }

    @Override
    protected Object[] getHashCodeData() {
        return new Object[]{name};
    }

    @Override
    public QualityAttribute getThis() {
        return this;
    }


}
