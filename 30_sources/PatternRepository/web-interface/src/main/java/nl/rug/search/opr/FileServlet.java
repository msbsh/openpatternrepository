package nl.rug.search.opr;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.file.FileLocal;

/**
 *
 * @author cm
 */
public class FileServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final Logger logger = Logger.getLogger(FileServlet.class.getName());
    public static final String FILE_DIRECTORY = "opr.FILE_DIRECTORY";

    @EJB
    private FileLocal fb;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OutputStream os = null;
        try {
        String requestedFile = request.getPathInfo().substring(1);
        String filename = requestedFile;

        int size = -1;
        boolean scaleCubic = false;
        if (requestedFile.matches("(xy)?[1-9][0-9]*px-.*")) {

            if (requestedFile.startsWith("xy")) {
                scaleCubic = true;
                requestedFile = requestedFile.substring(2);
            }

            String[] parts = requestedFile.split("px-");
            try {
                size = Integer.parseInt(parts[0]);
            } catch (NumberFormatException ex) {
                size = -1;
            }

            if(parts.length == 2) {
                filename = parts[1];
            }
        }

        long id;
        try {
            id = Long.parseLong(filename);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        File file = fb.getById(id);


        if (file == null) {
            response.sendError(404);
            return;
        }

        response.setContentType(file.getMime());
        response.addHeader("Content-Disposition", "filename="+ file.getName() +";");
        os = response.getOutputStream();


        if (size > 0 && file != null) {

            byte[] bytes;
            if( (bytes = fb.getThumbnail(file, size, scaleCubic)) != null) {
                os.write(bytes);
            }

            return;
        }


        os.write(file.getContent());

        return;

        } finally {
            try {
                os.close();
            } catch(Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
    }// </editor-fold>
}
