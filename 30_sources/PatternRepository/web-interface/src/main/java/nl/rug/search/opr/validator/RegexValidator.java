package nl.rug.search.opr.validator;

import com.sun.faces.util.MessageFactory;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 * @author Martin Verspai <martin@verspai.de>
 * @version 2
 */
@FacesValidator(value="RegexValidator")
public class RegexValidator implements Validator {

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the regex check fails.   The message format string for
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the label.</li>
     * </ul></p>
     */
    public static final String REGEX_MISMATCH_ID =
         "nl.rug.search.opr.validator.RegexValidator.REGEX";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the regex mismatch check fails.   The message format string for
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the label.</li>
     * </ul></p>
     */
    public static final String INPUTCHARSET_MISMATCH_ID =
         "nl.rug.search.opr.validator.RegexValidator.INPUTCHARS";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the starts with regex fails.   The message format string for
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the label.</li>
     * </ul></p>
     */
    public static final String STARTSWITH_MISMATCH_ID =
         "nl.rug.search.opr.validator.RegexValidator.STARTSWITH";

    
    private static final String REGEX        = "regex";
    private static final String INPUTCHARSET = "inputCharset";
    private static final String STARTSWITH   = "startsWith";

    // ------------------------------------------------------------

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (value == null) { return; }

        if (component.getAttributes().containsKey(REGEX)) {
            String regex = component.getAttributes().get(REGEX).toString();
            if (!value.toString().matches(regex)) {
                throw new ValidatorException(MessageFactory.getMessage
                                                    (context,
                                                    REGEX_MISMATCH_ID,
                                                    new Object[] {
                                                        MessageFactory.getLabel(context, component)}
                                                    ));
            }
        }

        if (component.getAttributes().containsKey(INPUTCHARSET)) {
            String regex = "[" + component.getAttributes().get(INPUTCHARSET).toString() + "]";

            for (Character c : value.toString().toCharArray()) {
                if (!c.toString().matches(regex)) {
                    throw new ValidatorException(MessageFactory.getMessage
                                                    (context,
                                                    INPUTCHARSET_MISMATCH_ID,
                                                    new Object[] {
                                                        MessageFactory.getLabel(context, component)}
                                                    ));
                }
            }
        }

        if (component.getAttributes().containsKey(STARTSWITH)) {
            String regex = component.getAttributes().get(STARTSWITH).toString();

            Character c = value.toString().charAt(0);
            if (!c.toString().matches("^[" + regex + "]*")) {
                throw new ValidatorException(MessageFactory.getMessage
                                                    (context,
                                                    STARTSWITH_MISMATCH_ID,
                                                    new Object[] {
                                                        MessageFactory.getLabel(context, component)}
                                                    ));
            }
        }
    }
}
















































