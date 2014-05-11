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
import nl.rug.search.opr.template.TemplateLocal;
import nl.rug.search.opr.entities.template.Template;

/**
 *
 * @author cm
 */
@ManagedBean(name="template")
@ApplicationScoped
@EJB(name=TemplateBackingBean.JNDI_NAME, beanInterface=TemplateLocal.class)
public class TemplateBackingBean {

    public static final String JNDI_NAME = "ejb/Template";


    @EJB
    private TemplateLocal tm;

    public List<Template> getTemplates() {
        return tm.getAll();
    }

    public List<SelectItem> getTemplateSelectItems() {

        List<SelectItem> items = new ArrayList<SelectItem>();
        for (Template t : getTemplates()) {
            items.add(new SelectItem(t, t.getName()));
        }
        return items;
    }

    public Template getTemplate(Long templateId) {
        return tm.getById(templateId);
    }

}
