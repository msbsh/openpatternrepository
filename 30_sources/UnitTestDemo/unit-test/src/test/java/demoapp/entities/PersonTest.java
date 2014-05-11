///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package demoapp.entities;
//
//import demoapp.entities.Person;
//import java.io.File;
//import java.sql.DriverManager;
//import java.sql.SQLNonTransientConnectionException;
//import java.util.logging.Logger;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.apache.derby.impl.io.VFMemoryStorageFactory;
//import org.junit.After;
//import org.junit.Before;
//
///**
// *
// * @author Georg Fleischer
// */
//public class PersonTest {
//
//    private static final Logger logger = Logger.getLogger(PersonTest.class.getName());
//    private EntityManagerFactory emFactory;
//    private EntityManager em;
//
//    @Before
//    public void setUp() throws Exception {
//        try {
//            logger.info("Starting in-memory database for unit tests");
//            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
//            DriverManager.getConnection("jdbc:derby:memory:unit-testing-jpa;create=true").close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            fail("Exception during database startup.");
//        }
//        try {
//            logger.info("Building JPA EntityManager for unit tests");
//            emFactory = Persistence.createEntityManagerFactory("testPU");
//            em = emFactory.createEntityManager();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            fail("Exception during JPA EntityManager instanciation.");
//        }
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        logger.info("Shuting down Hibernate JPA layer.");
//        if (em != null) {
//            em.close();
//        }
//        if (emFactory != null) {
//            emFactory.close();
//        }
//        logger.info("Stopping in-memory database.");
//        try {
//            DriverManager.getConnection("jdbc:derby:memory:unit-testing-jpa;shutdown=true").close();
//        } catch (SQLNonTransientConnectionException ex) {
//            if (ex.getErrorCode() != 45000) {
//                throw ex;
//            }
//            // Shutdown success
//        }
//        VFMemoryStorageFactory.purgeDatabase(new File("unit-testing-jpa").getCanonicalPath());
//    }
//
//    @Test
//    public void testPersistence() {
//
//        Person person = new Person();
//        person.setName("Peter Pan");
//
//        em.persist(person);
//        assertTrue(em.contains(person));
//
//        em.remove(person);
//        assertFalse(em.contains(person));
//    }
//}
