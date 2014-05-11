package nl.rug.search.opr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 */
public class FileJanitor implements HttpSessionListener {

    public static final Log logger = LogFactory.getLog(FileJanitor.class);
    public static final String FILE_UPLOAD_DIRECTORY = "upload";

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        logger.info("Cleaning up files for destroyed session.");
        String sessionId = event.getSession().getId();

        String applicationPath = event.getSession().getServletContext().getRealPath(
                event.getSession().getServletContext().getServletContextName());

        File userDirectory = new File(applicationPath + FILE_UPLOAD_DIRECTORY + sessionId);

        if (userDirectory.isDirectory()) {
            try {
                userDirectory.delete();
            }
            catch (SecurityException e) {
                logger.error("Error deleting file upload directory: ", e);
            }
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // Nothing to do here
    }

}
