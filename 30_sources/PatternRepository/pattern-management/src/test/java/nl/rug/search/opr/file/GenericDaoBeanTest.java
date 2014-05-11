package nl.rug.search.opr.file;

import nl.rug.search.opr.Ejb;
import nl.rug.search.opr.EMWrapperLocal;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import nl.rug.search.opr.EJBTestBase;
import nl.rug.search.opr.entities.pattern.QualityAttribute;
import nl.rug.search.opr.pattern.QualityAttributeLocal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sebastian Heinecke
 */
public class GenericDaoBeanTest extends EJBTestBase {
	
    private QualityAttributeLocal qaBean;
    private EMWrapperLocal em;
    private List<QualityAttribute> testList = new ArrayList<QualityAttribute>();
	
    @BeforeClass
    public static void setUpClass() throws Exception {
    }
	
    @AfterClass
    public static void tearDownClass() throws Exception {
    }
	
    @Before
    public void setUp() throws NamingException {
        qaBean = Ejb.createQualityAttributeBean();
        assertNotNull(qaBean);
		
        em = Ejb.createEMWrapperBean();
        assertNotNull(em);
		
        if (testList.isEmpty()) {
            testList.add(new QualityAttribute("test1", "blubb1"));
            testList.add(new QualityAttribute("test12", "blubb12"));
        }
    }
	
    private void persistTestObjects() {
        ArrayList<QualityAttribute> persistList = new ArrayList<QualityAttribute>();
        for (QualityAttribute qa : testList) {
            QualityAttribute persisted = (QualityAttribute) em.persist(qa);
            persistList.add(persisted);
        }
		
        testList.clear();
        for (QualityAttribute qa : persistList) {
            testList.add(em.getReference(QualityAttribute.class, qa.getId()));
        }
        em.flush();
    }
	
    @After
    public void tearDown() throws NamingException {
        qaBean = null;
        assertNull(qaBean);
		
        em = null;
        assertNull(em);
    }
	
    @Test
    public void canGetAll() {
        persistTestObjects();
        List<QualityAttribute> fetchedList = qaBean.getAll();
        for (QualityAttribute qa : fetchedList) {
            assertTrue(testList.contains(qa));
        }
    }
	
    @Test
    public void cannotGetAll() {
        List<QualityAttribute> fetchedList = qaBean.getAll();
		
        assertNotNull(fetchedList);
        assertTrue(fetchedList.isEmpty());
    }
	
    @Test
    public void canGetById() {
        persistTestObjects();
        for(QualityAttribute qa : testList){
            assertEquals(qa, qaBean.getById(qa.getId()));
        }
    }
	
    @Test
    public void cannotGetById() {
        QualityAttribute qa = (QualityAttribute) qaBean.getById(new Long(1));
		
        assertNull(qa);
    }
	
    @Test
    public void canMakePersistent() {
        QualityAttribute test = new QualityAttribute("test1234234", "blubb14535435");
        test = qaBean.makePersistent(test);
        QualityAttribute receivedTest = em.getReference(QualityAttribute.class, test.getId());
        assertEquals(receivedTest.getId(), test.getId());
        assertEquals(receivedTest.getName(), test.getName());
        assertEquals(receivedTest.getDescription(), test.getDescription());
    }
	
    @Test
    public void canMakeTransient() {
        database.clearAllEntities(QualityAttribute.class);
        QualityAttribute test = new QualityAttribute("test1234234", "blubb14535435");
        test = (QualityAttribute) em.persist(test);
        qaBean.makeTransient(test);
        assertEquals(qaBean.getAll().size(), 0);
    }
}
