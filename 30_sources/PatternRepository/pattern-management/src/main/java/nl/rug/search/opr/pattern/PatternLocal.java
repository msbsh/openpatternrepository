package nl.rug.search.opr.pattern;

import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.Pattern;
import java.util.List;
import javax.ejb.Local;
import nl.rug.search.opr.dao.GenericDaoLocal;
import nl.rug.search.opr.entities.template.Component;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 */

@Local
public interface PatternLocal extends GenericDaoLocal<Pattern,Long> {
    
    public Pattern add(Pattern p);

    public Pattern editVersion(Pattern p);

    public Pattern editCommon(Pattern p);

    @Deprecated
    public List<Pattern> getSimiliarPatterns(String toString);

    public Pattern getByUniqueName(String uniqueName);

    public List<Component> getSortedComponentList(PatternVersion pv);

    public void remove(Pattern p);
}
