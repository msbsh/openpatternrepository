package nl.rug.search.opr;

import org.junit.Before;

/**
 *
 * @author Georg Fleischer
 */
public class EJBTestBase {

    final protected EmbeddedGlassfish glassfish;
    final protected PatternDatabase database;

    public EJBTestBase() {
        glassfish = EmbeddedGlassfish.getInstance();
        database = PatternDatabase.getInstance();
    }

    @Before
    public void setupCleanDatabase(){
        database.clearDatabase();
    }
}
