package nl.rug.search.opr;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import nl.rug.search.opr.entities.pattern.Indicator;

/**
 *
 * @author cm
 */
@FacesConverter(value="indicatorConverter")
public class IndicatorConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Indicator i = null;

        try {
            i = Indicator.valueOf(value.toLowerCase());
        } catch (IllegalArgumentException e) {
        }

        return i;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
}
