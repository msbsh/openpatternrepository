/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demoapp.session;

import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import demoapp.entities.Person;
import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Georg Fleischer
 */
public class PersonBeanTest extends AbstractEJB {

    private PersonBeanLocal personBean;

    public PersonBeanTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws NamingException {
        personBean = (PersonBeanLocal) lookUp(PersonBean.class, PersonBeanLocal.class);
        assertNotNull(personBean);
    }

    @After
    public void tearDown() throws NamingException {
    }

    @Test
    public void canDeploySimpleSessionBean() {

        int x = personBean.calculateSum(15, 12);
        assertEquals(27, x);
    }

    @Test
    public void canAddPerson() {
        Person p = new Person();
        p.setName("Peter Pan");
        Person newPerson = personBean.addPerson(p);
        assertNotNull(newPerson);
        assertEquals("Peter Pan", newPerson.getName());
    }

    @Test
    public void canAddAndRetrievePerson() {

        Person p = new Person();
        p.setName("new person");

        Person newPerson = personBean.addPerson(p);
        assertNotNull(newPerson);

        Person retrievePerson = personBean.getPersonById(newPerson.getId());
        assertNotNull(retrievePerson);

        assertEquals(newPerson, retrievePerson);

    }
}
