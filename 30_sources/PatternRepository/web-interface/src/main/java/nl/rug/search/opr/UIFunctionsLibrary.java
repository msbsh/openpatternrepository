/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.opr;

import nl.rug.search.utils.TextProcessor;
import com.sun.faces.facelets.tag.AbstractTagLibrary;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * @author cm
 */
public class UIFunctionsLibrary extends AbstractTagLibrary {

    private static final String NAMESPACE = "http://www.rug.nl/serach/opr/uitags";
    public static final UIFunctionsLibrary INSTANCE = new UIFunctionsLibrary();

    public UIFunctionsLibrary() {
        super(NAMESPACE);

        try {
            Method[] methods = TextProcessor.class.getMethods();

            for (int i = 0; i < methods.length; i++) {
                if (Modifier.isStatic(methods[i].getModifiers())) {
                    this.addFunction(methods[i].getName(), methods[i]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
