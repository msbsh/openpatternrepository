
package nl.rug.search.opr;

import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JsfUtil {
    public static void redirect(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        url = externalContext.encodeResourceURL(externalContext.
                getRequestContextPath().concat(url));

        context.responseComplete();
        try {
            externalContext.redirect(url);
        } catch (IOException ex) {
                throw new RuntimeException("Can't navigate to url ".
                        concat(url));
        }
    }
}
