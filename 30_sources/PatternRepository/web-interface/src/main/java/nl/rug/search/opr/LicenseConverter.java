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
import nl.rug.search.opr.backingbean.LicenseBackingBean;
import nl.rug.search.opr.entities.pattern.License;
import nl.rug.search.opr.pattern.LicenseLocal;

/**
 *
 * @author cm
 */
@FacesConverter(value="licenseConverter")
public class LicenseConverter implements Converter {

    private LicenseLocal lb;

    public LicenseConverter() {
        try {
            InitialContext ic = new InitialContext();
            lb = (LicenseLocal)ic.lookup("java:comp/env/" + LicenseBackingBean.JNDI_NAME);
        } catch (NamingException ex) {
            throw new FacesException(ex);
        }
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        License result = null;
        if (value == null || value.length() < 1) {
            return null;
        }

        long tid;
        try {
            tid = Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }

        result = lb.getById(tid);
        return result;


    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = null;
        if (value != null && value instanceof License) {
            result = ((License) value).getId() + "";
        }
        return result;
    }

}
