package nl.rug.search.opr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author cm
 */
public class ValidationStylePhaseListener implements PhaseListener {

    private final String ORIGINAL_STYLE = "nl.rug.opr.search.original.style";

    public void afterPhase(PhaseEvent event) {
    }

    public void beforePhase(PhaseEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        restoreOriginalStyles(context, root);
        Iterator<String> i = context.getClientIdsWithMessages();

        while (i.hasNext()) {
            String id = i.next();
            UIComponent component = root.findComponent(id);

            if (component instanceof UIInput) {
                String style = (String) component.getAttributes().get("styleClass");
                style = style == null ? "" : " " + style;
                component.getAttributes().put("styleClass", "error" + style);
                saveOriginalStyle(id, style, context);
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    private void restoreOriginalStyles(FacesContext context, UIViewRoot root) {
        Map session = context.getExternalContext().getSessionMap();

        if (session.containsKey(ORIGINAL_STYLE)) {

            List<Map> list = (List<Map>) session.get(ORIGINAL_STYLE);

            for (Map item : list) {
                Map.Entry entry = (Entry) item.entrySet().iterator().next();
                UIComponent component = root.findComponent((String) entry.getKey());

                if (component != null) {
                    component.getAttributes().put("styleClass", entry.getValue());
                }
            }

            session.remove(ORIGINAL_STYLE);
        }
    }

    private void saveOriginalStyle(String id, String style, FacesContext context) {
        Map session = context.getExternalContext().getSessionMap();
        Map originalStyle = new HashMap();
        originalStyle.put(id, style);

        if (session.get(ORIGINAL_STYLE) == null) {
            session.put(ORIGINAL_STYLE, new ArrayList());
        }

        ((List) session.get(ORIGINAL_STYLE)).add(originalStyle);
    }
}
