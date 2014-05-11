package nl.rug.search.opr.pattern;

import java.util.List;
import javax.ejb.Local;
import nl.rug.search.opr.dao.GenericDaoLocal;
import nl.rug.search.opr.entities.pattern.Category;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 22.10.2009
 */
@Local
public interface CategoryLocal extends GenericDaoLocal<Category, Long> {

    /**
     * Searches the database for one root category
     * @return The first root category if any, otherwise 'null'
     */
    public Category getRootCategory();

    /**
     * Looks up children of given category
     * @param parent The parent category
     * @return A List of Categories, which are children of the given one
     */
    public List<Category> getChildrenOf(Category parent);

    /**
     * Searches for a category by its name.
     * @param name Name of the category to be found
     * @return Currently returns the first found category with this name.
     */
    public Category getByName(String name);

}
