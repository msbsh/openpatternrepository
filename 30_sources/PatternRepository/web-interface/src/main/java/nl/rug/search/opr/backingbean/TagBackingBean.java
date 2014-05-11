/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.opr.backingbean;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import nl.rug.search.opr.pattern.TagLocal;

/**
 *
 * @author cm
 */

@ManagedBean(name="tagBackingBean")
@ApplicationScoped
@EJB(name=TagBackingBean.JNDI_NAME, beanInterface=TagLocal.class)
public class TagBackingBean {

    public static final String JNDI_NAME = "ejb/Tag";
    
    /** Creates a new instance of TagBackingBean */
    public TagBackingBean() {
    }

}
