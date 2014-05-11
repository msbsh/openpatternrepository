
package nl.rug.search.opr.search;

/**
 *
 * @author cm
 */
public class QueryParseException extends Exception {

    public static final long serialVersionUID = 1l;

    public QueryParseException(Throwable cause) {
        super(cause);
    }

    public QueryParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryParseException(String message) {
        super(message);
    }

    public QueryParseException() {
    }

}
