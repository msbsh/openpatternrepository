package nl.rug.search.opr;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebFilter(filterName = "ResourceCacheFilter",
           urlPatterns = {"/css/*", "/lib/*", "/images/*"})
public class ResourceCacheFilter implements Filter {

    private static final Logger logger =
            Logger.getLogger(ResourceCacheFilter.class.getName());
    private static final String EXPIRATION_DELTA_PROPERTY_KEY = "opr.cache.resources.expiration";
    private static final long EXPIRATION_DELTA_DEFAULT = 86400000; // milliseconds

    private static final String DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";


    private long expirationDelta;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Long expirationDeltaRegistry = Long.getLong(EXPIRATION_DELTA_PROPERTY_KEY);

        if (expirationDeltaRegistry == null) {
            logger.info("System property "
                    + "'" + EXPIRATION_DELTA_PROPERTY_KEY + "'. This property"
                    + " should define the expiration time for Icefaces "
                    + "static resources (milliseconds till expiration). "
                    + "Falling back to default value.");
            expirationDelta = EXPIRATION_DELTA_DEFAULT;
        } else {
            expirationDelta = expirationDeltaRegistry;
        }
    }




    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

        httpResponse.setHeader("Expires", sdf.format(new Date(now.getTime()
                + expirationDelta)));

        httpResponse.setHeader("Cache-Control", "max-age=".
                concat(Long.toString(expirationDelta / 1000)).
                concat(";public;must-revalidate;"));

        chain.doFilter(request, response);
    }




    @Override
    public void destroy() {
    }




}



