/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.opr.backingbean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import nl.rug.search.opr.entities.pattern.QualityAttribute;
import nl.rug.search.opr.pattern.QualityAttributeLocal;

/**
 *
 * @author cm
 */
@ManagedBean(name="qualityAttributes")
@ApplicationScoped
@EJB(name=QABackingBean.JNDI_NAME,beanInterface=QualityAttributeLocal.class)
public class QABackingBean {

    public static final String JNDI_NAME = "ejb/QualityAttribute";

    @EJB
    private QualityAttributeLocal qb;

    public List<SelectItem> getSelectItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();

        for (QualityAttribute qa : qb.getAll()) {
            items.add(new SelectItem(qa, qa.getName()));
        }

        return items;
    }

}
