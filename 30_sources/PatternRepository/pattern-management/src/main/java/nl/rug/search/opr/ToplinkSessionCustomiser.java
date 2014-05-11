package nl.rug.search.opr;

import oracle.toplink.essentials.sessions.DatabaseLogin;
import oracle.toplink.essentials.sessions.Session;
import oracle.toplink.essentials.tools.sessionconfiguration.SessionCustomizer;

/**
 *
 * @author cm
 */
public class ToplinkSessionCustomiser implements SessionCustomizer {

    public void customize(Session session) throws Exception {
        
        DatabaseLogin login = session.getLogin();
        login.useStreamsForBinding();
        login.useByteArrayBinding();

    }

}
