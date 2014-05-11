
package nl.rug.search.opr.search.api.expressions;

import nl.rug.search.opr.entities.pattern.Indicator;
import nl.rug.search.opr.entities.pattern.QualityAttribute;
import nl.rug.search.opr.search.api.*;

/**
 *
 * @author cm
 */
public class ImpactSentence extends Sentence {

    private Indicator indicator;
    private String qualityAttribute;

    public ImpactSentence(Qualifier q, Indicator indicator, String qa) {
        super(q);
        setIndicator(indicator);
        setQualityAttribute(qa);
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator inidicator) {
        this.indicator = inidicator;
    }


    public String getQualityAttribute() {
        return qualityAttribute;
    }

    public void setQualityAttribute(String qualityAttribute) {
        this.qualityAttribute = qualityAttribute;
    }

    @Override
    public String toString() {
        return getQualifier() + " a " + getIndicator() + " impact on " + getQualityAttribute();
    }

    @Override
    public void visit(QueryBuilder builder) {
        builder.process(this);
    }    

}
