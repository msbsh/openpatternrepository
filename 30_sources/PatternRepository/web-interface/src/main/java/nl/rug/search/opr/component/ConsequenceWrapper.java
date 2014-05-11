package nl.rug.search.opr.component;


import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.Indicator;
import nl.rug.search.opr.entities.pattern.QualityAttribute;

/**
 *
 * @author cm
 */
public class ConsequenceWrapper extends Consequence {

    private final Consequence consequence;
    private boolean editMode;

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void toggleMode(ActionEvent e) {
        editMode = !editMode;
    }

    public ConsequenceWrapper(Consequence c) {
        consequence = c;
    }

    @Override
    public String getDescription() {
        return consequence.getDescription();
    }

    @Override
    public Indicator getImpactIndication() {
        return consequence.getImpactIndication();
    }

    @Override
    public QualityAttribute getQualityAttribute() {
        return consequence.getQualityAttribute();
    }

    @Override
    public void setDescription(String description) {
        consequence.setDescription(description);
    }

    @Override
    public void setImpactIndication(Indicator impactIndicator) {
        consequence.setImpactIndication(impactIndicator);
    }

    @Override
    public void setQualityAttribute(QualityAttribute qualityAttribute) {
        consequence.setQualityAttribute(qualityAttribute);
    }

    public Consequence getConsequence() {
        return consequence;
    }

    public void changeQualityAttribute(ActionEvent e) {
        String indicatorString = ((UIComponent) e.getSource()).getAttributes().get("IMPACTINDICATOR").toString();

        if (indicatorString == null || indicatorString.length() > 0);
        try {
            Indicator indicator = Indicator.fromValue(indicatorString);
            setImpactIndication(indicator);
        } catch (IllegalArgumentException ex) {
            //TODO error handling -> silent error seems to be an error in ui
        }   
    }

    public boolean isVeryNegative() {
        return (getImpactIndication()==Indicator.verynegative);
    }

    public boolean isNegative() {
        return (getImpactIndication()==Indicator.negative);
    }

    public boolean isNeutral() {
        return (getImpactIndication()==Indicator.neutral);
    }

    public boolean isPositive() {
        return (getImpactIndication()==Indicator.positive);
    }

    public boolean isVeryPositive() {
        return (getImpactIndication()==Indicator.verypositive);
    }
}
