/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.opr.component;


import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.Force;
import nl.rug.search.opr.entities.pattern.Indicator;
import nl.rug.search.opr.entities.pattern.QualityAttribute;

/**
 *
 * @author cm
 */
public class ForceWrapper extends Force {

    private final Force force;
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

    public ForceWrapper(Force f) {
        force = f;
    }

    @Override
    public String getDescription() {
        return force.getDescription();
    }

    @Override
    public Indicator getImpactIndication() {
        return force.getImpactIndication();
    }

    @Override
    public QualityAttribute getQualityAttribute() {
        return force.getQualityAttribute();
    }

    @Override
    public void setDescription(String description) {
        force.setDescription(description);
    }

    @Override
    public void setImpactIndication(Indicator impactIndicator) {
        force.setImpactIndication(impactIndicator);
    }

    @Override
    public void setQualityAttribute(QualityAttribute qualityAttribute) {
        force.setQualityAttribute(qualityAttribute);
    }

    public Force getForce() {
        return force;
    }

    @Override
    public String getFunctionality() {
        return force.getFunctionality();
    }

    @Override
    public void setFunctionality(String functionality) {
        force.setFunctionality(functionality);
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
