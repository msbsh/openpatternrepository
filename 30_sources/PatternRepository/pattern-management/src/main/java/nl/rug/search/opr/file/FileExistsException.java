
package nl.rug.search.opr.file;

/**
 *
 * @author cm
 */
public class FileExistsException extends FileException {

    public FileExistsException() {
    }

    public FileExistsException(String message) {
        super(message);
    }

    public FileExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileExistsException(Throwable cause) {
        super(cause);
    }

    

}
