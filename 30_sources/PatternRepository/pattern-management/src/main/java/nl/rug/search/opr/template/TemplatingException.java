/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.opr.template;

/**
 *
 * @author cm
 */
public class TemplatingException extends RuntimeException {

    public TemplatingException(Throwable cause) {
        super(cause);
    }

    public TemplatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplatingException(String message) {
        super(message);
    }

    public TemplatingException() {
    }

}
