package nl.rug.search.opr.validator;

import com.sun.faces.util.MessageFactory;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import nl.rug.search.opr.backingbean.PatternBackingBean;
import nl.rug.search.opr.pattern.PatternLocal;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 */
@FacesValidator(value = "WikiNameValidator")
public class WikiNameValidator implements Validator {

    private PatternLocal pl;

    public WikiNameValidator() {
        try {
            InitialContext ic = new InitialContext();
            pl = (PatternLocal) ic.lookup("java:comp/env/" + PatternBackingBean.JNDI_NAME);
        } catch (NamingException ex) {
            throw new FacesException(ex);
        }
    }
    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the unique name already exists.</p>
     */
    public static final String USEDWIKINAME_ID =
            "nl.rug.search.opr.validator.WikiNameValidator.DUPLICATEWIKINAME";

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (value == null || value.toString().equals("")) {
            return;
        }

        if (pl.getByUniqueName("" + value.toString()) != null) {
            throw new ValidatorException(MessageFactory.getMessage(
                    context,
                    USEDWIKINAME_ID,
                    new Object[]{
                        MessageFactory.getLabel(context, component)
                    }));
        }


    }
}
