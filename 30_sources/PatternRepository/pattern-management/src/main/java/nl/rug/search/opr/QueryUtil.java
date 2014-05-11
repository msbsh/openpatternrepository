package nl.rug.search.opr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 09.12.2009
 * @version 2.0
 */
public class QueryUtil {

    private static final Logger logger = Logger.getLogger(QueryUtil.class.getName());

    public static String getProperty(String fileName, String propertyName) {
        String propertyString = "";
        Properties property = new Properties();

        try {
            InputStream stream = QueryUtil.class.getClassLoader().getResourceAsStream("META-INF/" + fileName + ".properties");
        
            property.load(stream);
            propertyString = property.getProperty(propertyName);
            
        } catch (IOException ex) {
           logger.log(Level.SEVERE, null, ex);
        }

        return propertyString;
    }
    
}
