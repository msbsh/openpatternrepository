package nl.rug.search.opr.pattern;

import nl.rug.search.opr.entities.pattern.QualityAttribute;
import javax.ejb.Local;
import nl.rug.search.opr.dao.GenericDaoLocal;

/**
 *
 * @author cm
 */
@Local
public interface QualityAttributeLocal extends GenericDaoLocal<QualityAttribute,Long> {
}
