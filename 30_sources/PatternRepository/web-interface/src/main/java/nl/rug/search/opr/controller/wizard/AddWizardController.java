package nl.rug.search.opr.controller.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import nl.rug.search.opr.AbstractFormBean;
import nl.rug.search.opr.ActionResult;
import nl.rug.search.opr.JsfUtil;
import nl.rug.search.opr.MessageUtil;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.template.Component;
import nl.rug.search.opr.pattern.PatternLocal;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
@ManagedBean
@SessionScoped
public class AddWizardController extends AbstractFormBean {

    @EJB
    private PatternLocal patternEJB;
    private String failMsg;
    private Pattern pattern;
    private String paste = "";
    private String mandatorie = "";

    @Override
    public String successMessage() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        return MessageUtil.getMessageResourceString(
                "nl.rug.search.opr.localization.form",
                "info.pattern.stored",
                null,
                ec.getRequestLocale());
    }

    @Override
    public String failMessage() {
        return failMsg;
    }

    @Override
    public String getFormId() {
        return "addWizardPatternForm";
    }

    @Override
    public void reset() {
        pattern = null;
        paste = "";
    }

    @Override
    public void execute() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (!checkMandatories()) {
            failMsg = MessageUtil.getMessageResourceString(
                    "nl.rug.search.opr.localization.validation",
                    "missingMandatories",
                    null,
                    ec.getRequestLocale());
            failMsg += " (Click 'Next' to describe in the next steps following mandatories: " + mandatorie + ")";
            throw new ValidatorException(new FacesMessage("missing mandatory"));
        } else {
            failMsg = MessageUtil.getMessageResourceString(
                    "nl.rug.search.opr.localization.form",
                    "info.pattern.notStored",
                    null,
                    ec.getRequestLocale());
        }

        patternEJB.add(pattern);

        JsfUtil.redirect("/wiki/".concat(pattern.getUniqueName()));
    }

    public Pattern getPattern() {
        if (pattern == null) {
            pattern = new Pattern();
            pattern.setCurrentVersion(new PatternVersion());
        }
        return pattern;
    }

    public PatternVersion getPatternVersion() {
        return getPattern().getCurrentVersion();
    }

    public String getPaste() {
        return paste;
    }

    public void setPaste(String paste) {
        this.paste = paste;
    }

    // Action methods
    public ActionResult next() {
        return ActionResult.NEXT;
    }

    public ActionResult previous() {
        return ActionResult.PREVIOUS;
    }

    private boolean checkMandatories() {

        PatternVersion version = pattern.getCurrentVersion();

        List<Component> mandatories = new ArrayList<Component>();
        for (Component c : version.getTemplate().getComponents()) {
            if (c.isMandatory()) {
                mandatories.add(c);
            }
        }

        for (TextBlock block : version.getBlocks()) {
            if (mandatories.contains(block.getComponent())) {
                if (block.getText().length() <= 0) {
                    return false;
                }
                mandatories.remove(block.getComponent());
            }
        }
        if (!mandatories.isEmpty()) {
            Iterator<Component> it = mandatories.iterator();
            while (it.hasNext()) {
                Component next = it.next();
                mandatorie += next.getName() + " ";
            }
        }

        return mandatories.isEmpty();
    }
}
