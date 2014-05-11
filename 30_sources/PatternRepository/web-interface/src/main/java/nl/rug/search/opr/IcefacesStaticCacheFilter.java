package nl.rug.search.opr;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebFilter(filterName = "IcefacesStaticCacheFilter",
           urlPatterns = {"/javax.faces.resource/*"})
public class IcefacesStaticCacheFilter implements Filter {

    private static final Logger logger =
            Logger.getLogger(IcefacesStaticCacheFilter.class.getName());

    private static final String EXPIRATION_DELTA_PROPERTY_KEY = "opr.cache.ice.expiration";
    private static final long EXPIRATION_DELTA_DEFAULT = 86400; // seconds

    private static final String ICE_CACHE_ACTIVE_PROPERTY_KEY = "opr.cache.ice.active";

    private Date deploymentDate;
    private long expirationDelta;
    private boolean cacheActive;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        cacheActive = Boolean.getBoolean(ICE_CACHE_ACTIVE_PROPERTY_KEY);

        logger.log(Level.INFO, "Caching for Icefaces static resource is now "
                + "set to {0}. This can be modified through the '" +
                ICE_CACHE_ACTIVE_PROPERTY_KEY + "' system property.",
                cacheActive);

        if (!cacheActive) {
            return;
        }

        // TODO maybe it is more appropriate to retrieve this from a
        // properties file?
        deploymentDate = new Date();

        Long expirationDeltaRegistry = Long.getLong(EXPIRATION_DELTA_PROPERTY_KEY);

        if (expirationDeltaRegistry == null) {
            logger.info("System property "
                    + "'" + "opr.cache.ice.expiration" + ". This property"
                    + " should define the expiration time for Icefaces "
                    + "static resources (seconds till expiration). "
                    + "Falling back to default value.");
            expirationDelta = EXPIRATION_DELTA_DEFAULT;
        } else {
            expirationDelta = expirationDeltaRegistry;
        }
    }




    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (!cacheActive) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        long time = 0;
        try {
            time = httpRequest.getDateHeader("if-modified-since");
        } catch (IllegalArgumentException ex) {
        }

        Date date = new Date(time);

        if (date.before(deploymentDate)) {
            // this allows the client to use the resources direct from cache
            httpResponse.setHeader("Cache-Control", "max-age=".concat(Long.
                    toString(expirationDelta)).
                    concat(";public;must-revalidate;"));

            chain.doFilter(request, response);
        } else {
            // not modified header
            httpResponse.setStatus(304);
        }
    }




    @Override
    public void destroy() {
    }




}



