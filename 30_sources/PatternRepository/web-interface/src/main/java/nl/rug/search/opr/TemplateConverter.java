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
import nl.rug.search.opr.backingbean.TemplateBackingBean;
import nl.rug.search.opr.entities.template.Template;
import nl.rug.search.opr.template.TemplateLocal;

/**
 *
 * @author cm
 */
@FacesConverter(value="templateConverter")
public class TemplateConverter implements Converter {

    private TemplateLocal tb;

    public TemplateConverter() {
        try {
            InitialContext ic = new InitialContext();
            tb = (TemplateLocal)ic.lookup("java:comp/env/" + TemplateBackingBean.JNDI_NAME);
        } catch (NamingException ex) {
            throw new FacesException(ex);
        }
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Template result = null;
        if (value == null || value.length() < 1) {
            return null;
        }

        Long tid;
        try {
            tid = Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }

        result = tb.getById(tid);
        return result;


    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = null;
        if (value != null && value instanceof Template) {
            result = ((Template) value).getId() + "";
        }
        return result;
    }
}
