package nl.rug.search.opr.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.opr.AbstractFormBean;
import nl.rug.search.opr.JsfUtil;
import nl.rug.search.opr.component.InlineSearchHelper;
import nl.rug.search.opr.component.RelationshipHelper;
import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.pattern.PatternLocal;
import nl.rug.search.opr.entities.pattern.Tag;
import nl.rug.search.opr.pattern.TagLocal;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.validator.WikiNameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 */
@ManagedBean(name = "editCommonCtrl")
@ViewScoped
public class EditCommonController extends AbstractFormBean {

    private static final String PATTERN_ID = "patternId";
    private static final int TYPE_ALTERNATIVE = 0;
    private static final int TYPE_COMBINATION = 1;
    private static final int TYPE_VARIANT = 2;
    private static final String successMsg = "Pattern has been edited!";
    private static final String failMsg = "Pattern has not been edited!";
    private static final Logger logger = LoggerFactory.getLogger(EditCommonController.class);
    @EJB
    private PatternLocal patternBean;
    @EJB
    private TagLocal tagBean;
    
    private Pattern pattern;
    private Pattern relatedPattern;
    private String relationDescription;
    private InlineSearchHelper selectPattern = new InlineSearchHelper();
    private String tag;
    private int relationType;
    private Collection<Tag> proposedTags = new ArrayList<Tag>();
    private Collection<Tag> undoTags = new ArrayList<Tag>();
    private Collection<Tag> autoComplete = new ArrayList<Tag>();

    private InlineSearchHelper helper;

    @Override
    public String getFormId() {
        return "editPattern";
    }

    public Pattern getPattern() {
        if (pattern == null) {
            pattern = getPatternByQueryString();
            initData();
        }
        return pattern;
    }

    public InlineSearchHelper getHelper() {
        if (helper==null) {
            helper = new InlineSearchHelper();
        }
        return helper;
    }

    

    public void addCategory(ValueChangeEvent e) {
        Category c = (Category) e.getNewValue();

        if (c == null || c.equals(e.getOldValue())) {
            return;
        }

        if (!pattern.getCategories().contains(c)) {
            pattern.getCategories().add(c);
        }
    }

    public void removeCategory(ActionEvent e) {
        Category c = (Category) e.getComponent().getAttributes().get("category");
        logger.info("Will remove category " + c.getName());
        pattern.getCategories().remove(c);
        logger.info("did remove it");
    }

    public void select(ActionEvent e) {
        String idString = e.getComponent().getAttributes().get("PATTERNID").toString();

        try {
            long id = Long.parseLong(idString);
            relatedPattern = patternBean.getById(id);
        } catch (NumberFormatException nfe) {
            logger.error("Could not parse idString " + idString + ".\nUnable to retrieve pattern.");
        }
    }

    public String getRelationDescription() {
        return relationDescription;
    }

    public void setRelationDescription(String relationDescription) {
        this.relationDescription = relationDescription;
    }

    public int getRelationType() {
        return this.relationType;
    }

    public Collection<Tag> getProposedTags() {
        return this.proposedTags;
    }

    public void setProposedTags(Collection<Tag> proposedTags) {
        this.proposedTags = proposedTags;
    }

    public void addProposedTag(ActionEvent e) {
        Tag t = (Tag) e.getComponent().getAttributes().get("TAG");
        if (!pattern.getTags().contains(t)) {
            pattern.getTags().add(t);
            if (proposedTags.contains(t)) {
                proposedTags.remove(t);
                undoTags.add(t);
            }
        }
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public Pattern getRelatedPattern() {
        return this.relatedPattern;
    }

    public void setRelatedPattern(Pattern relatedPattern) {
        this.relatedPattern = relatedPattern;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Collection<SelectItem> getTagPossibilities() {
        Collection<SelectItem> items = new ArrayList<SelectItem>();
        for (Tag t : this.autoComplete) {
            items.add(new SelectItem(t, t.getName()));
        }
        return items;
    }

    public void tagValueChanged(ValueChangeEvent e) {
        if (e.getNewValue().equals(e.getOldValue())) {
            return;
        }
        if (e.getNewValue().toString().length() <= 0) {
            this.autoComplete.clear();
            return;
        }

        this.autoComplete = tagBean.getSimiliarTags(e.getNewValue().toString());
    }

    public void addTag(ActionEvent e) {
        Tag dbTag = null;

        if (this.tag.length() <= 0) {
            return;
        }

        Tag t = new Tag(this.tag);

        if ((dbTag = tagBean.getByName(tag)) != null) {
            t = dbTag;
        }

        if (!pattern.getTags().contains(t)) {
            pattern.getTags().add(t);
            if (this.proposedTags.contains(t)) {
                this.proposedTags.remove(t);
                this.undoTags.add(t);
            }
        }
    }

    public void removeTag(ActionEvent e) {
        Tag t = (Tag) e.getComponent().getAttributes().get("TAG");
        pattern.getTags().remove(t);
        if (this.undoTags.contains(t)) {
            this.undoTags.remove(t);
            this.proposedTags.add(t);
        }
    }

    public void removeRelationship(ActionEvent e) {
        Relationship r = (Relationship) e.getComponent().getAttributes().get("RELATIONSHIP");
        pattern.getRelations().remove(r);
    }

    @SuppressWarnings("element-type-mismatch")
    public void addRelationship(ActionEvent e) {
        Relationship relationship = RelationshipHelper.validateAddNewRelationship(this.pattern.getRelations(),
                this.relatedPattern, this.pattern, this.relationDescription, this.relationType);
        
        if (relationship != null) {
            pattern.getRelations().add(relationship);
        }
    }

    @Override
    public String successMessage() {
        return EditCommonController.successMsg;
    }

    @Override
    public String failMessage() {
        return EditCommonController.failMsg;
    }

    public void validateUniqueName(FacesContext context, UIComponent toValidate, Object uniqueName) {
        if (!uniqueName.equals(pattern.getUniqueName())) {
            new WikiNameValidator().validate(context, toValidate, uniqueName);
        }
    }

    @Override
    public void reset() {
        initData();
    }

    @Override
    public void execute() {

        patternBean.editCommon(this.pattern);
        this.pattern = patternBean.getById(this.pattern.getId());

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        JsfUtil.redirect("/wiki/".concat(pattern.getUniqueName()));
    }

    private Pattern getPatternByQueryString() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String patternIdStr = request.getParameter(PATTERN_ID);
        Pattern result = null;
        if (patternIdStr != null && !patternIdStr.equals("")) {

            try {

                long id = Long.parseLong(patternIdStr);
                result = patternBean.getById(id);

                if (result == null) {
                    logger.info("No pattern found with id " + id);
                }

            } catch (NumberFormatException nfe) {
                logger.error("Pattern id is not a number");
            }
        } else {
            logger.warn("No Pattern id found in Query String");
        }



        return result;
    }

    private void initData() {
        if (pattern == null) {
            logger.error("Could not initialize pattern, no pattern available");
            return;
        }
        this.proposedTags.clear();
        this.undoTags.clear();

        int i = 0;
        for (Tag t : tagBean.getFavouriteTags(10)) {
            this.proposedTags.add(t);
            i++;
            if (i >= 10) {
                break;
            }
        }

        this.proposedTags.addAll(tagBean.getProposedTags(pattern.getCurrentVersion(), 40));

        for (Tag t : pattern.getTags()) {
            if (this.proposedTags.contains(t)) {
                this.proposedTags.remove(t);
                this.undoTags.add(t);
            }
        }

    }
}
