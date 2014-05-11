
package nl.rug.search.opr.search.api;

/**
 *
 * @author cm
 */
public class SearchException extends Exception {

    public static final long serialVersionUID = 1l;

    public SearchException(Throwable cause) {
        super(cause);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchException(String message) {
        super(message);
    }

    public SearchException() {
    }



}
