package nl.rug.search.opr.webservices;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import nl.rug.search.opr.pattern.PatternLocal;
import nl.rug.search.opr.webservices.types.PatternDTO;

/**
 * REST Web Service
 *
 * @author cm
 */

@Path("pattern")
@EJB(name="ejb/Pattern", beanInterface=PatternLocal.class)
public class PatternResource {

    private PatternLocal pb;

    @Context
    private UriInfo context;

    /** Creates a new instance of GenericResource */
    public PatternResource() {
        try {
            InitialContext ic = new InitialContext();
            pb = (PatternLocal) ic.lookup("java:comp/env/ejb/Pattern");
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Retrieves representation of an instance of nl.rug.search.opr.ws.GenericResource
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    @Path(value="/{id}")
    public PatternDTO getSinglePattern(@PathParam(value="id")int id) {
        
        return PatternDTO.assemble(pb.getById((long)id));
    }

    @GET
    @Produces("application/xml")
    @Path(value="/wiki/{name}")
    public PatternDTO getSinglePattern(@PathParam(value="name")String name) {
        return PatternDTO.assemble(pb.getByUniqueName(name));
    }

}
