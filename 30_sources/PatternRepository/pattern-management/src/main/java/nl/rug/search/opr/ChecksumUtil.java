package nl.rug.search.opr;

import java.lang.String;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cm
 */
public class ChecksumUtil {
    
    public static final String ALGORITHM = "SHA-1";

    public static String calculate(byte[] convertme) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChecksumUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String(md.digest(convertme));
    }
    
}
