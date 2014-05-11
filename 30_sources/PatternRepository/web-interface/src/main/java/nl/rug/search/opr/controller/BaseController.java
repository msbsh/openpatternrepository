package nl.rug.search.opr.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


@ManagedBean(name="baseCtrl")
@RequestScoped
public class BaseController {


    private String baseHREF = System.getProperty("base.href");

    public BaseController() {
    }

    /**
     * @return the baseHREF
     */
    public String getBaseHREF() {
        return baseHREF;
    }

    /**
     * @param baseHREF the baseHREF to set
     */
    public void setBaseHREF(String baseHREF) {
        this.baseHREF = baseHREF;
    }

    public String getViewId() {
        String result = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        result = facesContext.getViewRoot().getViewId();
        return result;
    }
    
}
