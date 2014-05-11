package nl.rug.search.opr;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 *
 * @author Georg Fleischer
 */
public class EmbeddedGlassfish {

    private static EmbeddedGlassfish singleton = null;
    private EJBContainer container;
    private Context context;

    private EmbeddedGlassfish() {
        try {
            moveFile("./target/classes/META-INF/persistence.xml", "./target/classes/META-INF/persistence.xml.backup", true);
            moveFile("./target/test-classes/META-INF/test-persistence.xml", "./target/classes/META-INF/persistence.xml", false);

            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(EJBContainer.MODULES, new java.io.File("./target/classes"));
            properties.put("org.glassfish.ejb.embedded.glassfish.installation.root",
                    "./src/test/glassfish");
            this.container = EJBContainer.createEJBContainer(properties);
            this.context = container.getContext();

            moveFile("./target/classes/META-INF/persistence.xml", "./target/test-classes/META-INF/test-persistence.xml", false);
            moveFile("./target/classes/META-INF/persistence.xml.backup", "./target/classes/META-INF/persistence.xml", false);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean moveFile(String source, String target, boolean override) throws IOException {
        File sourceFile = new File(source);
        File temp = new File(target);

        if (temp.exists()) {
            if (override) {
                temp.delete();
            } else {
                throw new java.io.IOException("Destination file exists: " + target);
            }
        }

        return sourceFile.renameTo(temp);
    }

    public static EmbeddedGlassfish getInstance() {
        if (singleton == null) {
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

    final protected Object lookUp(Class classType, Class interfaceType) {

        StringBuilder lookUpName = new StringBuilder();
        lookUpName.append("java:global/classes/");
        lookUpName.append(classType.getSimpleName());
        lookUpName.append("!");
        lookUpName.append(interfaceType.getName());

        try {
            return context.lookup(lookUpName.toString());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
