
package nl.rug.search.opr;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.rug.search.opr.file.FileLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 */
public class InitializationServlet extends HttpServlet {

    private static final long serialVersionUID = 1l;

    private Logger logger = LoggerFactory.getLogger(InitializationServlet.class);

    @EJB
    private FileLocal fileBean;

    @Override
    public void init() {
        /*logger.info("Start Initialization");
        logger.info("Init Search");
        try {
            searchBean.createSearchQuery("test");
        } catch (QueryParseException ex) {
            logger.info("Search Bean initialization error");
        }
        */
        logger.info("Init FileBean Timer");
        try {
            fileBean.init();
        } catch (Exception ex) {
            logger.info("FileBean Timer initialization error");
        }
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    } 

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
