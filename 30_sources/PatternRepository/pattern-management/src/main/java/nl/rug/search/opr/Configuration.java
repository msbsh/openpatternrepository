package nl.rug.search.opr;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

/**
 *
 * @author daniel
 * Created on: 01.12.2010
 */
public class Configuration {

    private CompositeConfiguration config = null;
    private static Configuration instance = null;
    private static final Logger logger = Logger.getLogger(Configuration.class.getName());

    private Configuration() {
        try {
            config = new CompositeConfiguration();
            //InitBean.class.getClassLoader().get
            config.addConfiguration(new XMLConfiguration("META-INF/FileUploadConfig.xml"));
            config.addConfiguration(new PropertiesConfiguration("META-INF/fileJanitor.properties"));
            config.addConfiguration(new PropertiesConfiguration("META-INF/patternQueries.properties"));
            config.addConfiguration(new PropertiesConfiguration("META-INF/categoryQueries.properties"));
            config.addConfiguration(new PropertiesConfiguration("META-INF/fileQueries.properties"));
            config.addConfiguration(new PropertiesConfiguration("META-INF/tagQueries.properties"));
            config.addConfiguration(new PropertiesConfiguration("META-INF/templateQueries.properties"));
        } catch (org.apache.commons.configuration.ConfigurationException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public Object getProperty(String key) {
        if (System.getProperties().containsKey(key)) {
            return System.getProperty(key);
        }
        return config.getProperty(key);
    }

    public boolean isProperty(String key, boolean defaultValue) {
        Object propertyValue = getProperty(key);
        if (propertyValue == null){
            return defaultValue;
        }
        String check = propertyValue.toString().toUpperCase();
        if (check.equals("TRUE")) {
            return true;
        }
        if (check.equals("FALSE")) {
            return false;
        }
        throw new IllegalStateException(String.format(
                "Invalid configuration value for property %s. Supported are TRUE and FALSE",
                key));
    }

    public int getIntProperty(String key, int defaultValue){
        Object propertyValue = getProperty(key);
        if (propertyValue == null){
            logger.info(String.format("Property %s not defined - using default value", key));
            return defaultValue;
        }
        String check = propertyValue.toString();
        try{
            return Integer.parseInt(check);
        }catch(NumberFormatException ex){
            logger.log(Level.WARNING, String.format(
                    "Integer property %s could not be read - using default value", key), ex);
        }
        return defaultValue;
    }

    public List getList(String key) {
        if (System.getProperties().contains(key)) {
        }
        return config.getList(key);
    }
}
