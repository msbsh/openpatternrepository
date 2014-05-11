package nl.rug.search.opr.controller.wizard;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import nl.rug.search.opr.entities.pattern.Category;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
@ManagedBean(name = "basicStep")
@RequestScoped
public class BasicPatternInformation implements  WizardStep {

    @ManagedProperty(value = "#{addWizardController}")
    protected AddWizardController wizard;

    public AddWizardController getWizard() {
        return wizard;
    }

    public void setWizard(AddWizardController wizard) {
        this.wizard = wizard;
    }

    @PostConstruct
    private void load() {
    }

    @PreDestroy
    private void unload() {
    }

    public void removeCategory(ActionEvent e) {
        Category c = (Category) e.getComponent().getAttributes().get("category");
        wizard.getPattern().getCategories().remove(c);
    }

    public void addCategory(ValueChangeEvent e) {
        Category c = (Category) e.getNewValue();

        if (c.equals(e.getOldValue())) {
            return;
        }

        if (c == null) {
            return;
        }
        if (!wizard.getPattern().getCategories().contains(c)) {
            wizard.getPattern().getCategories().add(c);
        }
    }
}
