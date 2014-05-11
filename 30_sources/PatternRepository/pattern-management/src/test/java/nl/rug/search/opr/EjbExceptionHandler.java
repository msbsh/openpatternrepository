/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.opr;

import javax.ejb.EJBException;

/**
 *
 * @author Georg Fleischer
 */
public class EjbExceptionHandler {

    public EjbExceptionHandler() {
    }

    public void execute(Runnable runnable) throws Exception{
        try {
            runnable.run();
        } catch (EJBException ejbEx) {
            throw ejbEx.getCausedByException();
        }
    }
}
