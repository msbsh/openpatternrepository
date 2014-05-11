package nl.rug.search.opr.entities.pattern;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 06.10.2009
 */

@Entity
public class Consequence extends Driver implements Impact, Serializable {

    private static final long serialVersionUID = 1L;

    @Lob
    private String description;

    @Enumerated(value = EnumType.STRING)
    private Indicator impactIndication;

    @ManyToOne
    private QualityAttribute qualityAttribute;

    public Consequence() {}

    public Consequence(String description, QualityAttribute qualityAttribute, Indicator impactIndicator) {
        this.description = description;
        this.qualityAttribute = qualityAttribute;
        this.impactIndication = impactIndicator;
    }

    /* Getter and Setter methods */

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

    public void setImpactIndication(Indicator impactIndicator) {
        this.impactIndication = impactIndicator;
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
