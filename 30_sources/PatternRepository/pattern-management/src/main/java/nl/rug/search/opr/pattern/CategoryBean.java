package nl.rug.search.opr.pattern;

import java.util.List;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.Query;
import nl.rug.search.opr.ConfigConstants;
import nl.rug.search.opr.dao.GenericDaoBean;
import nl.rug.search.opr.entities.pattern.Category;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 22.10.2009
 * @version 2.1
 */
@Stateless
public class CategoryBean extends GenericDaoBean<Category, Long> implements CategoryLocal {

    @Override
    public Category getRootCategory() {

        List rootList = createQuery(ConfigConstants.QUERY_GET_ROOT_CATEGORY).getResultList();

        return rootList.isEmpty() ? null : (Category) rootList.get(0);
    }

    @Override
    public List<Category> getChildrenOf(Category parent) {
        List<Category> categories = new ArrayList<Category>();

        Query q = createQuery(ConfigConstants.QUERY_GET_CHILDREN_OF);
            q.setParameter("param", parent);

        categories.addAll(q.getResultList());
        return categories;
    }

    @Override
    public Category getByName(String name) {
        Query q = createQuery(ConfigConstants.QUERY_GET_BY_NAME);
            q.setParameter("param", name);

        List categoryList = q.getResultList();

        return categoryList.isEmpty() ? null : (Category) categoryList.get(0);
    }
    
}
