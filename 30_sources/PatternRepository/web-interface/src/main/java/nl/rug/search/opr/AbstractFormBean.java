package nl.rug.search.opr;

import com.icesoft.faces.context.effects.Appear;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.EffectQueue;
import com.icesoft.faces.context.effects.Fade;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cm
 */
public abstract class AbstractFormBean {

    public static final int RESULT_DURATION = 4;

    private static final String formId = "abstractFormBase";

    @Deprecated
    private UIForm formComponent;
    private EffectQueue resultEffect = new EffectQueue("appearfade");
    private Logger logger = LoggerFactory.getLogger(AbstractFormBean.class);
    

    public AbstractFormBean() {
        Effect appear = new Appear();
        appear.setDuration(RESULT_DURATION);
        Effect fade = new Fade();
        resultEffect.add(appear);
        resultEffect.add(fade);
    }

    public Effect getResultEffect() {
        return resultEffect;
    }

    public String getFormId() {
        return formId;
    }

    @Deprecated
    public UIForm getFormComponent() {
        resultEffect.setFired(false);
        return formComponent;
    }

    @Deprecated
    public void setFormComponent(UIForm formComponent) {
        this.formComponent = formComponent;
    }

    public abstract String successMessage();

    public abstract String failMessage();

    public abstract void reset();

    public abstract void execute();

    public ActionResult resetForm() {
        reset();
        return ActionResult.RESET;
    }

    public ActionResult submitForm() {

        clearMessages();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, successMessage(), null);
        
        try {
            execute();
            reset();
            formComponent = null;
        } catch (ValidatorException ex) {
            fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, failMessage(), null);
        } catch (Exception ex) {
            logger.error(failMessage(), ex);
            fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, failMessage(), null);
        } finally {
            FacesContext.getCurrentInstance().addMessage(formId, fm);
            resultEffect.setFired(false);
        }
        return ActionResult.SUBMITTED;
    }

    private void clearMessages() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Iterator<String> clients = context.getClientIdsWithMessages();
            while (clients.hasNext()) {
                String clientId = clients.next();
                Iterator<FacesMessage> messages = context.getMessages(clientId);

                while (messages.hasNext()) {
                    messages.next();
                    messages.remove();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
