package nl.rug.search.opr.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.util.Collection;
import java.util.Properties;
import nl.rug.search.opr.Ejb;
import static org.junit.Assert.*;
import javax.naming.NamingException;
import javax.persistence.Query;
import nl.rug.search.opr.EJBTestBase;
import nl.rug.search.opr.EMWrapperLocal;
import nl.rug.search.opr.pattern.CategoryLocal;
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
        firstRootCategory.setId(new Long(1));
        firstRootCategory.setParent(firstRootCategory);

        secondRootCategory = new Category("2nd Root");
        secondRootCategory.setId(new Long(2));
        secondRootCategory.setParent(secondRootCategory);

        childCategory = new Category("Child");
        childCategory.setId(new Long(3));
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

        Category root = (Category) createQuery("rootCategory").get(0);
        List<Category> rootChildren = categoryBean.getChildrenOf(root);

        assertEquals(1, rootChildren.size());
        assertEquals("Test", rootChildren.get(0).getName());
    }

    @Test
    public void canFindChildrenOfRoot() {

        em.persist(firstRootCategory);

        Category root = (Category) createQuery("rootCategory").get(0);

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
        assertEquals(new Long(3), cat.getId());
        assertEquals(categoryBean.getRootCategory(), cat.getParent());
    }

    private List createQuery(String property) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("META-INF/categoryQueries.properties");
        Properties properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException ex) {
            Logger.getLogger(CategoryBeanTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        return em.queryGetResultList(properties.getProperty(property));
    }
}
