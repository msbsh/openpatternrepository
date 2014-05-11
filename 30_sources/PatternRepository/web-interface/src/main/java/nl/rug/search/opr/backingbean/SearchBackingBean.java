
package nl.rug.search.opr.backingbean;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import nl.rug.search.opr.search.SearchLocal;

@ManagedBean(name="search")
@ApplicationScoped
@EJB(name=SearchBackingBean.JNDI_NAME, beanInterface=SearchLocal.class)
public class SearchBackingBean {

    @EJB
    private SearchLocal searchBean;

    public static final String JNDI_NAME = "ejb/Search";
}
