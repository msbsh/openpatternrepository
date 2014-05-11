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
import nl.rug.search.opr.backingbean.QABackingBean;
import nl.rug.search.opr.entities.pattern.QualityAttribute;
import nl.rug.search.opr.pattern.QualityAttributeLocal;

/**
 *
 * @author cm
 */
@FacesConverter(value="qualityAttributeConverter")
public class QualityAttributeConverter implements Converter {

    private QualityAttributeLocal qb;

    public QualityAttributeConverter() {
        try {
            InitialContext ic = new InitialContext();
            qb = (QualityAttributeLocal) ic.lookup("java:comp/env/" + QABackingBean.JNDI_NAME);
        } catch (NamingException ex) {
            throw new FacesException(ex);
        }
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        QualityAttribute result = null;
        if (value == null || value.length() < 1) {
            return null;
        }

        Long tid;
        try {
            tid = Long.parseLong(value);
            result = qb.getById(tid);
        } catch (NumberFormatException e) {
        }

        
        if (result == null) {
            result = new QualityAttribute(value, "");
        }

        return result;


    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = null;
        if (value != null && value instanceof QualityAttribute) {
            result = ((QualityAttribute) value).getId() + "";
        }        

        return result;
    }
}
