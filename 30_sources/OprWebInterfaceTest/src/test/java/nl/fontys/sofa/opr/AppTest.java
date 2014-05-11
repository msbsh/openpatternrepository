package nl.fontys.sofa.opr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author daniel
 */
public class AppTest extends TestCase {

    public AppTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        File file = new File("conf/configuration.properties");
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(file));
            
            for(Object key : props.keySet()){
                if(System.getProperty((String)key) == null){
                    System.setProperty((String) key, props.getProperty((String)key));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TestSuite suite = new TestSuite("OPRTestSuite");
        suite.addTestSuite(AddPatternBasicInformation.class);
        suite.addTestSuite(AddPatternWizzard.class);
        suite.addTestSuite(EditPattern.class);
        suite.addTestSuite(EditDescription.class);
        suite.addTestSuite(SearchUI.class);
        suite.addTestSuite(ViewASpecificPattern.class);

        return suite;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
