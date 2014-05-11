package nl.rug.search.opr.webservices;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import nl.rug.search.opr.search.QueryParseException;
import nl.rug.search.opr.search.SearchLocal;
import nl.rug.search.opr.search.api.ResultList;
import nl.rug.search.opr.search.api.SearchQuery;

/**
 *
 * @author cm
 */

@Path(value="search")
@EJB(name="ejb/Search", beanInterface=SearchLocal.class)
public class SearchResource {

    private SearchLocal sb;

    @Context
    private UriInfo context;

    /** Creates a new instance of GenericResource */
    public SearchResource() {
        try {
            InitialContext ic = new InitialContext();
            sb = (SearchLocal) ic.lookup("java:comp/env/ejb/Search");
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param query
     * @return
     */
    @GET
    @Produces("application/xml")
    public ResultList query(@QueryParam(value="q") String query) {

        ResultList rs = ResultList.getNullResult();
        SearchQuery q;
        try {
            q = sb.createSearchQuery(query);
            rs= sb.search(q);
        } catch (QueryParseException ex) {
            Logger.getLogger(SearchResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(rs.getCount());
        return rs;
    }

}
