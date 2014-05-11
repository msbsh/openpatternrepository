package nl.rug.search.opr.pattern;

import nl.rug.search.opr.entities.pattern.License;
import javax.ejb.Local;
import nl.rug.search.opr.dao.GenericDaoLocal;

/**
 *
 * @author cm
 */
@Local
public interface LicenseLocal extends GenericDaoLocal<License, Long> {
}
