package nl.rug.search.opr.validator;

import com.sun.faces.util.MessageFactory;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import nl.rug.search.opr.template.Message;
import nl.rug.search.opr.template.TemplateAssembler;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 * @author Martin Verspai <martin@verspai.de>
 * @version 2
 */
@FacesValidator(value="XMLTemplateValidator")
public class TemplateValidator implements Validator {

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the mandatory check fails.   The message format string for
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the label.</li>
     * </ul></p>
     */
    public static final String MISSING_MANDATORY_ID =
         "nl.rug.search.opr.validator.TemplateValidator.MISSINGMANDATORY";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * a xml error occurs.   The message format string for
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the label.</li>
     * <li><code>{1}</code> replaced by error reason.</li>
     * <li><code>{2}</code> replaced by line number of error.</li>
     * </ul></p>
     */
    public static final String XML_ERROR_ID =
         "nl.rug.search.opr.validator.TemplateValidator.XMLERROR";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * a unknown mapping is discovered.   The message format string for
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the label.</li>
     * <li><code>{0}</code> replaced by the unknown mapping.</li>
     * </ul></p>
     */
    public static final String UNKNOWN_MAPPING_ID =
         "nl.rug.search.opr.validator.TemplateValidator.UNKNOWNMAPPING";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * a unknown type was found.   The message format string for
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the label.</li>
     * </ul></p>
     */
    public static final String UNKOWN_TYPE_ID =
         "nl.rug.search.opr.validator.TemplateValidator.UNKNOWNTYPE";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * a mapping is unused.   The message format string for
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the label.</li>
     * <li><code>{1}</code> replaced by the mappings name.</li>
     * </ul></p>
     */
    public static final String UNUSED_WARNING_ID =
         "nl.rug.search.opr.validator.TemplateValidator.UNUSEDMAPPING";

    // ------------------------------------------------------------

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (value == null) {
            return;
        }

        Map<Message, String> messages = TemplateAssembler.validate(value.toString());

        for (Entry<Message, String> entry : messages.entrySet()) {

            ArrayList<Object> obj = new ArrayList<Object>();
            obj.add(MessageFactory.getLabel(context, component));

            for(Object o : entry.getKey().getDetails()) {
                obj.add(o);
            }

            if (entry.getValue().equals(TemplateAssembler.TYPE_ERROR)) {
                throwMessage(entry.getKey(), context, obj.toArray());
            }

            if (entry.getValue().equals(TemplateAssembler.TYPE_WARNING)) {
                addMessage(entry.getKey(), context, obj.toArray());
            }
        }
    }

    private void throwMessage(Message message, FacesContext context, Object[] params) {
        String error = "";

        switch (message.getCode()) {

            case TemplateAssembler.ERROR_MANDATORY:
                error = MISSING_MANDATORY_ID;
                break;
            case TemplateAssembler.ERROR_PARSE:
                error = XML_ERROR_ID;
                break;
            case TemplateAssembler.ERROR_REFERENCE:
                error = UNKNOWN_MAPPING_ID;
                break;
            case TemplateAssembler.ERROR_UNUSED:
                error = MISSING_MANDATORY_ID;
                break;
            case TemplateAssembler.ERROR_XML:
                error = XML_ERROR_ID;
                break;
            case TemplateAssembler.ERROR_UNKOWNTYPE:
                error = UNKOWN_TYPE_ID;
                break;
            case TemplateAssembler.ERROR_UNKOWN:
            default:
                error = XML_ERROR_ID;
                break;
        }

        throw new ValidatorException(MessageFactory.getMessage(context, error, params));
    }

    private void addMessage(Message message, FacesContext context, Object[] params) {
        if(message.getCode() == TemplateAssembler.WARNING_UNUSED) {
            throw new ValidatorException(MessageFactory.getMessage(context, UNUSED_WARNING_ID, FacesMessage.SEVERITY_WARN, params));
        }
    }
}
