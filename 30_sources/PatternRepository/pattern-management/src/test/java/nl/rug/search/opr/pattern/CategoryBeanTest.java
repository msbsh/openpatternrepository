package nl.rug.search.opr.pattern;

import java.util.List;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.util.Collection;
import nl.rug.search.opr.Ejb;
import static org.junit.Assert.*;
import javax.naming.NamingException;
import nl.rug.search.opr.EJBTestBase;
import nl.rug.search.opr.EMWrapperLocal;
import nl.rug.search.opr.entities.pattern.Category;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date Nov 10, 2010
 */
public class CategoryBeanTest extends EJBTestBase {

    private CategoryLocal categoryBean;
    private EMWrapperLocal em;
    private Category firstRootCategory;
    private Category secondRootCategory;
    private Category childCategory;

    @Before
    public void setUp() {

        categoryBean = Ejb.createCategoryBean();
        assertNotNull(categoryBean);

        em = Ejb.createEMWrapperBean();
        assertNotNull(em);

        firstRootCategory = new Category("Test");
        firstRootCategory.setParent(firstRootCategory);

        secondRootCategory = new Category("2nd Root");
        secondRootCategory.setParent(secondRootCategory);

        childCategory = new Category("Child");
        childCategory.setParent(firstRootCategory);

    }

    @After
    public void tearDown() throws NamingException {
        categoryBean = null;
        assertNull(categoryBean);

        em = null;
        assertNull(em);
    }

    @Test
    public void cannotFindRootCategory() {
        assertNull(categoryBean.getRootCategory());
    }

    @Test
    public void canFindRootCategory() {

        em.persist(firstRootCategory);

        Category received = categoryBean.getRootCategory();

        assertNotNull(received);
        assertEquals(firstRootCategory, received);
    }

    @Test
    public void provokeDoubleRootCategory() {

        em.persist(firstRootCategory);
        em.persist(secondRootCategory);

        Category received = categoryBean.getRootCategory();

        assertNotNull(received);
        assertEquals("Test", received.getName());
    }

    @Test
    public void cannotFindChildrenOfRoot() {

        em.persist(firstRootCategory);

        Category root = categoryBean.getRootCategory();
        List<Category> rootChildren = categoryBean.getChildrenOf(root);

        assertEquals(1, rootChildren.size());
        assertEquals("Test", rootChildren.get(0).getName());
    }

    @Test
    public void canFindChildrenOfRoot() {

        em.persist(firstRootCategory);

        Category root = categoryBean.getRootCategory();

        em.persist(childCategory);

        Collection<Category> rootChildren = root.getChildren();
        rootChildren.add(childCategory);

        em.merge(root);
        em.flush();

        Category newRoot = categoryBean.getRootCategory();

        assertEquals(1, newRoot.getChildren().size());
        assertEquals("Child", newRoot.getChildren().iterator().next().getName());
    }

    @Test
    public void cannotFindCategoryByName() {

        Category cat = categoryBean.getByName("unavailable name");
        assertNull(cat);
    }

    @Test
    public void canFindCategoryByName() {

        em.persist(firstRootCategory);
        em.persist(childCategory);

        Category cat = categoryBean.getByName("Child");

        assertEquals("Child", cat.getName());
        assertEquals(categoryBean.getRootCategory(), cat.getParent());
    }
}
