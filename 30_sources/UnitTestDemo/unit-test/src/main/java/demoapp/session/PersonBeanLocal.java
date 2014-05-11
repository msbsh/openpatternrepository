/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demoapp.session;

import demoapp.entities.Person;
import javax.ejb.Local;

/**
 *
 * @author Georg Fleischer
 */
@Local
public interface PersonBeanLocal  {

    public Person addPerson(Person p);

    public Person getPersonById(long id);

    public int calculateSum(int a, int b);

}
