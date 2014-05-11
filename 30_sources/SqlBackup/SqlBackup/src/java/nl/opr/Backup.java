/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.opr;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gauner
 */
public class Backup extends HttpServlet {

    private final String MYSQL_USER = "opr";
    private final String MYSQL_HOST = "localhost";
    private final String DATABASE_NAME = "patternRepository";
    private final String MYSQL_OUTPUT_TEMPFILE = "/tmp/opr.sql";
    private final String ARCHIVE_INPUT_BASE = "/tmp";
    private final String ARCHIVE_INPUT_FILENAME = "opr.sql";
    private final String ARCHIVE_OUTPUT_FILENAME = "/tmp/opr.sql.tar.bz2";


    @RolesAllowed("AuthorizedClients")
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        try {
            Runtime.getRuntime().exec("mysqldump -u "+MYSQL_USER+" -h "+MYSQL_HOST+" "+DATABASE_NAME+" -r "+MYSQL_OUTPUT_TEMPFILE).waitFor();
            // tar --directory /tmp --create --bzip2 --file /tmp/opr.sql.tar.bz2 opr.sql
            Runtime.getRuntime().exec("tar --directory "+ARCHIVE_INPUT_BASE+" --create --bzip2 --file "+ARCHIVE_OUTPUT_FILENAME+" "+ARCHIVE_INPUT_FILENAME).waitFor();
            File f = new File(ARCHIVE_OUTPUT_FILENAME);
            response.setContentType("application/x-bzip");
            response.setHeader("Location", f.getName());
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
            get(new FileInputStream(f), response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            f.delete();
            new File(MYSQL_OUTPUT_TEMPFILE).delete();
        } catch (InterruptedException ex) {
            Logger.getLogger(Backup.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            response.getOutputStream().close();
        }
    }

    private void get(InputStream in, OutputStream out) throws IOException {
          if (in == null || out == null) {
              return;
          }
          byte[] buffer = new byte[1024];
          int i = -1;
          BufferedOutputStream gzip = new BufferedOutputStream(out);
          while ((i = in.read(buffer)) != -1) {
              gzip.write(buffer, 0, i);
          }
          gzip.flush();
          gzip.close();
      }

     /**
 * This method returns true if the HttpServletRequest contains a valid
 * authorisation header
 * @param req The HttpServletRequest to test
 * @return true if the Authorisation header is valid
 **/





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
