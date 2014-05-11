package nl.rug.search.opr.pattern;

import nl.rug.search.opr.entities.pattern.QualityAttribute;
import javax.ejb.Stateless;
import nl.rug.search.opr.dao.GenericDaoBean;

/**
 *
 * @author cm
 */
@Stateless
public class QualityAttributeBean extends GenericDaoBean<QualityAttribute,Long> implements QualityAttributeLocal {
    
}
