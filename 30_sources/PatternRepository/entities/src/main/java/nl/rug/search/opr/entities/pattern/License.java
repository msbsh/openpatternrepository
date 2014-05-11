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
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 06.10.2009
 */

@Entity
public class License extends BaseEntity<License> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator="PK_LICENSE")
    @TableGenerator(name="PK_LICENSE",allocationSize=5,initialValue=0)
    private Long id;

    @Column(unique=true, nullable=false) 
    private String name;
    private boolean restrictive;
    private String licenseSource;

    public License() {}

    public License(String name, boolean restrictive, String licenseSource) {
        this.name = name;
        this.restrictive = restrictive;
        this.licenseSource = licenseSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getlicenseSource() {
        return licenseSource;
    }

    public void setlicenseSource(String licenseSource) {
        this.licenseSource = licenseSource;
    }

    public boolean isRestrictive() {
        return restrictive;
    }

    public void setRestrictive(boolean restrictive) {
        this.restrictive = restrictive;
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
    protected boolean dataEquals(License other) {
        return areEqual(name, name, true);
    }

    @Override
    protected Object[] getHashCodeData() {
        return new Object[]{name};
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    @Override
    public License getThis() {
        return this;
    }
    
}
