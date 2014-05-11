package nl.rug.search.opr.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author cm
 */
@FacesValidator(value="CollectionValidator")
public class CollectionValidator implements Validator {

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        /*
        System.out.println("\n\n\n\n\n\n");
        System.out.println("+++++++++++++++++++++++++++ I was notified so i can validate :-)");
        System.out.println("\n\n\n\n\n\n");
        */

    }

}
