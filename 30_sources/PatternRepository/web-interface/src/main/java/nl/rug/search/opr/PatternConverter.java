/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.opr;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import nl.rug.search.opr.backingbean.PatternBackingBean;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.pattern.PatternLocal;

/**
 *
 * @author cm
 */
@FacesConverter(value="patternConverter")
public class PatternConverter implements Converter {

    
    private PatternLocal pb;

    public PatternConverter() {
        try {
            InitialContext ic = new InitialContext();
            pb = (PatternLocal) ic.lookup("java:comp/env/" + PatternBackingBean.JNDI_NAME);
        } catch (NamingException ex) {
            throw new FacesException(ex);
        }
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Pattern result = null;
        if (value == null || value.length() < 1) {
            return null;
        }

        Long pid;
        try {
            pid = Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
        
        result = pb.getById(pid);
        return result;


    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = null;
        if (value != null && value instanceof Pattern) {
            result = ((Pattern) value).getId()+"";
        }
        return result;
    }
}
