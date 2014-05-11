package nl.rug.search.opr.entities.pattern;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.TableGenerator;
import nl.rug.search.opr.entities.AmbiguousEntity;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 07.10.2009
 */

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Driver extends AmbiguousEntity<Driver> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator="PK_DRIVER")
    @TableGenerator(name="PK_DRIVER",allocationSize=5,initialValue=0)
    private Long id;

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

    @Override
    public abstract Driver getThis();
    
}
