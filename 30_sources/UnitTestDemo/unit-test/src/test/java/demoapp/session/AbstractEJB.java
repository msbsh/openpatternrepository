/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demoapp.session;

import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import org.junit.BeforeClass;

/**
 *
 * @author Georg Fleischer
 */
public class AbstractEJB {

    EmbeddedGlassfish glassfish;

    public AbstractEJB() {
        glassfish = EmbeddedGlassfish.getInstance();
    }

    protected Object lookUp(Class classType ,Class interfaceType){

        StringBuilder lookUpName = new StringBuilder();
        lookUpName.append("java:global/classes/");
        lookUpName.append(classType.getSimpleName());
        lookUpName.append("!");
        lookUpName.append(interfaceType.getName());

        try {
            return glassfish.getContext().lookup(lookUpName.toString());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
