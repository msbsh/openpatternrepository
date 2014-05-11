/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.opr.backingbean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.pattern.CategoryLocal;

/**
 *
 * @author cm
 */
@ManagedBean(name="category")
@ApplicationScoped
@EJB(name=CategoryBackingBean.JNDI_NAME,beanInterface=CategoryLocal.class)
public class CategoryBackingBean {

    public static final String JNDI_NAME = "ejb/Category";

    @EJB
    private CategoryLocal cb;

    public Collection<Category> getCategories() {
        return cb.getAll();
    }

    public List<SelectItem> getCategorySelectItems() {

        List<SelectItem> items = new ArrayList<SelectItem>();
        for (Category p : getCategories()) {
            items.add(new SelectItem(p, p.getName()));
        }
        return items;
    }

}
