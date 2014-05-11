/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demoapp.session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Georg Fleischer
 */
public class DerbyMemoryDatabase {

    private EntityManagerFactory emFactory;
    private EntityManager em;

    public DerbyMemoryDatabase() throws SQLException {

        emFactory = Persistence.createEntityManagerFactory("testPU");
        em = emFactory.createEntityManager();


    }
}
