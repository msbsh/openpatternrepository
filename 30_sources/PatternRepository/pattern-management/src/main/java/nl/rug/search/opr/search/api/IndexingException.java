
package nl.rug.search.opr.search.api;

/**
 *
 * @author cm
 */
public class IndexingException extends SearchException {

    public IndexingException() {
    }

    public IndexingException(String message) {
        super(message);
    }

    public IndexingException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexingException(Throwable cause) {
        super(cause);
    }

}
