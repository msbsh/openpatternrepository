package nl.rug.search.opr;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import nl.rug.search.opr.search.api.Qualifier;

/**
 *
 * @author cm
 */
@FacesConverter(value="qualifierConverter")
public class QualifierConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Qualifier q = null;

        try {
            value = value.toUpperCase();
            q = Qualifier.valueOf(value);
        } catch (IllegalArgumentException e) {
        }
        
        return q;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
}
