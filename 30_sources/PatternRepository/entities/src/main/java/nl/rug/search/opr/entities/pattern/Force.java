package nl.rug.search.opr.entities.pattern;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 06.10.2009
 */

@Entity
@Table(name = "OPRFORCE")
public class Force extends Driver implements Impact, Serializable {

    private static final long serialVersionUID = 1L;

    @Lob
    private String description;
    private String functionality;

    @Enumerated(value = EnumType.STRING)
    private Indicator impactIndication;

    @ManyToOne
    private QualityAttribute qualityAttribute;

    public Force() {}

    public Force(String description, String functionality, QualityAttribute qualityAttribute, Indicator impactIndication) {
        this.description = description;
        this.functionality = functionality;
        this.qualityAttribute = qualityAttribute;
        this.impactIndication = impactIndication;
    }

    /* Getter and Setter methods */
    public String getFunctionality() {
        return functionality;
    }

    public void setFunctionality(String functionality) {
        this.functionality = functionality;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Indicator getImpactIndication() {
        return impactIndication;
    }

    public void setImpactIndication(Indicator impactIndication) {
        this.impactIndication = impactIndication;
    }

    @Override
    public QualityAttribute getQualityAttribute() {
        return qualityAttribute;
    }

    public void setQualityAttribute(QualityAttribute qualityAttribute) {
        this.qualityAttribute = qualityAttribute;
    }
    
    @Override
    public Driver getThis() {
        return this;
    }
}
