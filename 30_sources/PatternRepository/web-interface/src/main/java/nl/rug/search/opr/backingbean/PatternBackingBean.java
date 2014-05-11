package nl.rug.search.opr.backingbean;

import nl.rug.search.opr.pattern.*;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.opr.entities.pattern.Pattern;


/**
 *
 * @author cm
 */
@ManagedBean(name="pattern")
@ApplicationScoped
@EJB(name=PatternBackingBean.JNDI_NAME, beanInterface=PatternLocal.class)
public class PatternBackingBean {
    public static final String PARAMETER_ID = "patternId";
    public static final String JNDI_NAME = "ejb/Pattern";

    @EJB
    private PatternLocal pb;

    public Pattern getPattern() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String idString = request.getParameter(PARAMETER_ID);
        
        try {
            Long id = Long.parseLong(idString);
            return pb.getById(id);
        } catch (Exception e) {
            //add message 
        }
        
        return null;
    }

     public List<SelectItem> getPatternSelectItems() {

        List<SelectItem> items = new ArrayList<SelectItem>();
        for (Pattern p : getPatterns()) {
            items.add(new SelectItem(p, p.getName()));
        }
        return items;
    }

    public List<Pattern> getPatterns() {
        return pb.getAll();
    }

    
}
