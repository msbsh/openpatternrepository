/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.opr.backingbean;

import javax.faces.event.ActionEvent;





/**
 *
 * @author cm
 */
public class PopupHelper {

    private boolean rendered = false;
    private boolean autoCenterd = false;
    private boolean draggable = false;

    private String name = "Select Element";
    private String title = "";

    public PopupHelper() {

    }

    public PopupHelper(boolean rendered, boolean autoCentred, boolean draggable) {
        this.autoCenterd = autoCentred;
        this.rendered = rendered;
        this.draggable = draggable;
    }

    public void toggle(ActionEvent e) {
        rendered=!rendered;
    }

    public boolean isAutoCenterd() {
        return autoCenterd;
    }

    public void setAutoCenterd(boolean autoCenterd) {
        this.autoCenterd = autoCenterd;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    



}
