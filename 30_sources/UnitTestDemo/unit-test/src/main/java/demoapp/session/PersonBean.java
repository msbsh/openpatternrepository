/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demoapp.session;

import demoapp.entities.Person;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author Georg Fleischer
 */
@Stateless
@LocalBean
public class PersonBean implements PersonBeanLocal {

    @PersistenceContext
    EntityManager em;

    @Override
    public Person addPerson(Person p){
        em.persist(p);
        return p;
    }

    @Override
    public int calculateSum(int a, int b){
        return a + b;
    }

    @Override
    public Person getPersonById(long id) {
        return em.getReference(Person.class, id);
    }
}
