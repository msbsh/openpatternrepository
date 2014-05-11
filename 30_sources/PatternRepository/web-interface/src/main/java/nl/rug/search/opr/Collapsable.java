package nl.rug.search.opr;

import com.icesoft.faces.component.panelcollapsible.PanelCollapsible;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 27.11.2009
 */
public class Collapsable<T> {
    private boolean expanded;
    private T content;

    public Collapsable(T content) {
        this.expanded = false;
        this.content  = content;
    }

    public Collapsable(boolean expanded, T content) {
        this.expanded = expanded;
        this.content  = content;
    }

    public void expandListener(ActionEvent e) {
        PanelCollapsible pc = (PanelCollapsible) e.getSource();

        if (pc.isExpanded()) {
            this.expanded = true;
        } else {
            this.expanded = false;
        }
    }

    public T getContent() {
        return content;
    }

    public boolean isExpanded() {
        return expanded;
    }

}
