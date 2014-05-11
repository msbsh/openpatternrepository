package nl.rug.search.opr.controller;

import nl.rug.search.opr.template.*;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.opr.AbstractFormBean;
import nl.rug.search.opr.entities.template.Template;

/**
 *
 * @author cm
 */
@ManagedBean(name="addTemplateCtrl")
@RequestScoped
public class AddTemplateController extends AbstractFormBean {

    @EJB
    private TemplateLocal tm;
    private String name;
    private String author;
    private String description;
    private String xmlData;

    public String getAuthor() {
        return author;

    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXmlData() {
        return xmlData;
    }

    public void setXmlData(String xmlData) {
        this.xmlData = xmlData;
    }
    
    @Override
    public String successMessage() {
        return "Template has been stored";
    }

    @Override
    public String failMessage() {
        return "Template could not be stored";
    }

    @Override
    public void reset() {
        author = null;
        description = null;
        name = null;
        xmlData = null;
    }

    @Override
    public void execute() {
        
        Template t  = TemplateAssembler.xmlToTemplate(xmlData);
        t.setName(name);
        t.setAuthor(author);
        t.setDescription(description);
        
        tm.add(t);
    }
}
