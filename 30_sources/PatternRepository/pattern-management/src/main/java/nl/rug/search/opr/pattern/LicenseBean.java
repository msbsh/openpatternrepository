package nl.rug.search.opr.pattern;

import nl.rug.search.opr.entities.pattern.License;
import javax.ejb.Stateless;
import nl.rug.search.opr.dao.GenericDaoBean;

/**
 *
 * @author cm
 */
@Stateless
public class LicenseBean extends GenericDaoBean<License,Long> implements LicenseLocal {  
 
}
