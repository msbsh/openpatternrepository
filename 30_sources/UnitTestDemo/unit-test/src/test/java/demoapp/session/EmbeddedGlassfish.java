/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demoapp.session;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

/**
 *
 * @author Georg Fleischer
 */
public class EmbeddedGlassfish {

    private static EmbeddedGlassfish singleton = null;
    private EJBContainer container;
    private Context context;

    private EmbeddedGlassfish(){
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(EJBContainer.MODULES, new java.io.File("./target/classes"));
        properties.put("org.glassfish.ejb.embedded.glassfish.installation.root",
                "./src/test/glassfish");
        this.container = EJBContainer.createEJBContainer(properties);
        this.context = container.getContext();
    }

    public static EmbeddedGlassfish getInstance(){
        if (singleton == null){
            singleton = new EmbeddedGlassfish();
        }
        return singleton;
    }

    public EJBContainer getContainer() {
        return container;
    }

    public Context getContext() {
        return context;
    }

}
