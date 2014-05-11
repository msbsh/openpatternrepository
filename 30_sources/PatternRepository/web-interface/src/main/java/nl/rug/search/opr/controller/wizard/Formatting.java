package nl.rug.search.opr.controller.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import nl.rug.search.opr.backingbean.UploadHelper;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.entities.pattern.Force;
import nl.rug.search.opr.entities.pattern.Indicator;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.template.Component;
import nl.rug.search.opr.entities.template.Template;
import nl.rug.search.opr.file.FileLocal;

/**
 *
 * @author Jan Nikolai Trzeszkowski <info@jn-t.de>
 */
@ManagedBean(name = "formattingStep")
@ViewScoped
public class Formatting implements WizardStep {

    @ManagedProperty(value = "#{addWizardController}")
    protected AddWizardController wizard;

    @EJB
    private FileLocal fl;

    private Consequence editConsequence = null;
    private Force editForce = null;
    private UploadHelper upload = new UploadHelper();
    private TextBlock editTextBlock = null;
    private Map<String, TextBlock> blocks = new HashMap<String, TextBlock>();
    private String reload;

    public String getReload() {
        return reload;
    }

    public void setReload(String reload) {
        this.reload = reload;
    }

    public Map<String, TextBlock> getBlocks() {
        return blocks;
    }

    public TextBlock getEditTextBlock() {
        return editTextBlock;
    }

    public Consequence getEditConsequence() {
        return editConsequence;
    }

    public Force getEditForce() {
        return editForce;
    }

    public AddWizardController getWizard() {
        return wizard;
    }

    public void setWizard(AddWizardController wizard) {
        this.wizard = wizard;
    }

    @PostConstruct
    private void load() {
        PatternVersion v = wizard.getPatternVersion();
        Template template = v.getTemplate();

        blocks.clear();

        for (Component c : template.getTextComponents()) {
            if (!blocks.containsKey(c.getIdentifier())) {
                TextBlock block = new TextBlock();
                block.setComponent(c);
                block.setText("");
                blocks.put(c.getIdentifier(), block);
            }
        }

        for (TextBlock tb : v.getBlocks()) {
            blocks.put(tb.getComponent().getIdentifier(), tb);
        }

        if (template.getTextComponents().size() > 0) {
            editTextBlock = blocks.get(template.getTextComponents().get(0).getIdentifier());
        }

        v.setBlocks(blocks.values());
    }

    public UploadHelper getUpload() {
        return this.upload;
    }

    public void removeFile(ActionEvent e) {
        File f = (File) e.getComponent().getAttributes().get("file");
        if (f != null) {
           wizard.getPatternVersion().getFiles().remove(f);
        }
    }

    public void changeEditTextBlock(ActionEvent e) {
        TextBlock textBlock = (TextBlock) e.getComponent().getAttributes().get("textBlock");
        if (textBlock == editTextBlock) {
            editTextBlock = null;
        } else {
            editTextBlock = textBlock;
        }
    }

    public void changeEditConsequence(ActionEvent e) {
        Consequence c = (Consequence) e.getComponent().getAttributes().get("consequence");
        if (c == editConsequence) {
            editConsequence = null;
        } else {
            editConsequence = c;
        }
    }

    public void addConsequence(ActionEvent e) {
        Consequence consequence = new Consequence();
        editConsequence = consequence;
        wizard.getPatternVersion().getConsequences().add(consequence);
    }

    public void remove(ActionEvent e) {
        Consequence c = (Consequence) e.getComponent().getAttributes().get("consequence");
        if (c != null) {
            List<Consequence> consequences = wizard.getPatternVersion().getConsequences();
            consequences.remove(c);
            if (c == editConsequence) {
                editConsequence = null;
            }
        } else {
            Force f = (Force) e.getComponent().getAttributes().get("force");
            List<Force> forces = wizard.getPatternVersion().getForces();
            forces.remove(f);
            if (f == editForce) {
                editForce = null;
            }
        }
    }

    public void changeQualityAttribute(ActionEvent e) {
        if (((UIComponent) e.getSource()).getAttributes().get("IMPACTINDICATORC") == null) {
            String indicatorString = ((UIComponent) e.getSource()).getAttributes().get("IMPACTINDICATORF").toString();

            if (indicatorString == null || indicatorString.length() > 0);
            try {
                Indicator indicator = Indicator.fromValue(indicatorString);
                editForce.setImpactIndication(indicator);
            } catch (IllegalArgumentException ex) {
                //TODO error handling -> silent error seems to be an error in ui
            }

        } else {
            String indicatorString = ((UIComponent) e.getSource()).getAttributes().get("IMPACTINDICATORC").toString();
            Indicator indicator = Indicator.fromValue(indicatorString);
            editConsequence.setImpactIndication(indicator);
        }
    }

    public void addForce(ActionEvent e) {
        Force force = new Force();
        editForce = force;
        wizard.getPatternVersion().getForces().add(force);
    }

    public void changeEditForce(ActionEvent e) {
        Force f = (Force) e.getComponent().getAttributes().get("force");
        if (f == editForce) {
            editForce = null;
        } else {
            editForce = f;
        }
    }

    public void addUploadedFile(long id) {
        File file = fl.getById(id);
        wizard.getPatternVersion().getFiles().add(file);
    }

    public List<File> getAllUploadedFiles() {
        List<File> allFiles = fl.getAll();
        allFiles.removeAll(wizard.getPatternVersion().getFiles());
        return allFiles;
    }

    public boolean isVeryNegativeForce() {
        return (editForce.getImpactIndication() == Indicator.verynegative);
    }

    public boolean isNegativeForce() {
        return (editForce.getImpactIndication() == Indicator.negative);
    }

    public boolean isNeutralForce() {
        return (editForce.getImpactIndication() == Indicator.neutral);
    }

    public boolean isPositiveForce() {
        return (editForce.getImpactIndication() == Indicator.positive);
    }

    public boolean isVeryPositiveForce() {
        return (editForce.getImpactIndication() == Indicator.verypositive);
    }

    public boolean isVeryNegative() {
        return (editConsequence.getImpactIndication() == Indicator.verynegative);
    }

    public boolean isNegative() {
        return (editConsequence.getImpactIndication() == Indicator.negative);
    }

    public boolean isNeutral() {
        return (editConsequence.getImpactIndication() == Indicator.neutral);
    }

    public boolean isPositive() {
        return (editConsequence.getImpactIndication() == Indicator.positive);
    }

    public boolean isVeryPositive() {
        return (editConsequence.getImpactIndication() == Indicator.verypositive);
    }
    
}
