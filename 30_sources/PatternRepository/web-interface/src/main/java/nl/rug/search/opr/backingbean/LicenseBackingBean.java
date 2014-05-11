package nl.rug.search.opr.backingbean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import nl.rug.search.opr.entities.pattern.License;
import nl.rug.search.opr.pattern.LicenseLocal;

/**
 *
 * @author cm
 */
@ManagedBean(name="license")
@ApplicationScoped
@EJB(name=LicenseBackingBean.JNDI_NAME, beanInterface=LicenseLocal.class)
public class LicenseBackingBean {

    public static final String JNDI_NAME = "ejb/License";

    @EJB
    private LicenseLocal lb;

    public List<License> getLicenses() {
        return lb.getAll();
    }

    public List<SelectItem> getLicenseSelectItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (License l : getLicenses()) {
            items.add(new SelectItem(l,l.getName()));
        }

        return items;
    }

}





























