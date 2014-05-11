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
import nl.rug.search.opr.backingbean.CategoryBackingBean;
import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.pattern.CategoryLocal;

/**
 *
 * @author cm
 */
@FacesConverter(value="categoryConverter")
public class CategoryConverter implements Converter {

    private CategoryLocal cb;

    public CategoryConverter() {
        try {
            InitialContext ic = new InitialContext();
            cb = (CategoryLocal) ic.lookup("java:comp/env/" + CategoryBackingBean.JNDI_NAME);
        } catch (NamingException ex) {
            throw new FacesException(ex);
        }
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        
        Category result = null;
        if (value == null || value.length() < 1) {
            return null;
        }

        long cid;
        try {
            cid = Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }

        result = cb.getById(cid);
        return result;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = "";
        if (value != null && value instanceof Category) {
            result = ((Category)value).getId()+"";
        }
        return result;
    }
}
