package nl.rug.search.opr.controller.wizard;

import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import nl.rug.search.opr.backingbean.TagBackingBean;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.Tag;
import nl.rug.search.opr.pattern.PatternLocal;
import nl.rug.search.opr.pattern.TagLocal;
import nl.rug.search.opr.component.RelationshipHelper;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jan Nikolai Trzeszkowski <info@jn-t.de>
 */
@ManagedBean(name = "additionalStep")
@ViewScoped
public class Additional implements WizardStep {

    @ManagedProperty(value = "#{addWizardController}")
    protected AddWizardController wizard;
    @EJB
    private PatternLocal patternBean;
    private Logger logger = LoggerFactory.getLogger(Additional.class);

    public AddWizardController getWizard() {
        return wizard;
    }

    public void setWizard(AddWizardController wizard) {
        this.wizard = wizard;
    }

    @PostConstruct
    private void load() {
        this.parentPattern = getWizard().getPattern();

        InitialContext ctx = null;
        try {
            ctx = new InitialContext();
            tb = (TagLocal) ctx.lookup("java:comp/env/" + TagBackingBean.JNDI_NAME);
        } catch (NamingException ex) {
            System.out.println("Not found");
        }

        relations = getWizard().getPattern().getRelations();

        Pattern p = getWizard().getPattern();
        tags = p.getTags();

        PatternVersion version = getWizard().getPatternVersion();

        proposedTags.clear();
        undoTags.clear();

        int i = 0;
        for (Tag t : tb.getFavouriteTags(10)) {
            proposedTags.add(t);
            i++;
            if (i >= 10) {
                break;
            }
        }

        proposedTags.addAll(tb.getProposedTags(version, 40));

        for (Tag t : tags) {
            if (proposedTags.contains(t)) {
                proposedTags.remove(t);
                undoTags.add(t);
            }
        }
    }

    @PreDestroy
    private void unload() {
    }
    private Collection<Tag> tags;
    private String tag;
    private Collection<Tag> proposedTags = new ArrayList<Tag>();
    private Collection<Tag> undoTags = new ArrayList<Tag>();
    private Collection<Tag> autoComplete = new ArrayList<Tag>();
    private TagLocal tb;
    private Pattern relatedPattern;
    private String relationDescription;
    private Collection<Relationship> relations;
    private int relationType;
    private Pattern parentPattern;

    public Collection<Tag> getProposedTags() {
        return proposedTags;
    }

    public void setProposedTags(Collection<Tag> proposedTags) {
        this.proposedTags = proposedTags;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void tagValueChanged(ValueChangeEvent e) {
        if (e.getNewValue().equals(e.getOldValue())) {
            return;
        }
        if (e.getNewValue().toString().length() <= 0) {
            autoComplete.clear();
            return;
        }

        autoComplete = tb.getSimiliarTags(e.getNewValue().toString());
    }

    public Collection<SelectItem> getTagPossibilities() {
        Collection<SelectItem> items = new ArrayList<SelectItem>();
        for (Tag t : autoComplete) {
            items.add(new SelectItem(t, t.getName()));
        }
        return items;
    }

    public void addTag(ActionEvent e) {
        if (tag.length() <= 0) {
            return;
        }
        Tag t = new Tag(tag);
        if (!tags.contains(t)) {
            tags.add(t);
            if (proposedTags.contains(t)) {
                proposedTags.remove(t);
                undoTags.add(t);
            }
        }
    }

    public void addProposedTag(ActionEvent e) {
        Tag t = (Tag) e.getComponent().getAttributes().get("TAG");
        if (!tags.contains(t)) {
            tags.add(t);
            if (proposedTags.contains(t)) {
                proposedTags.remove(t);
                undoTags.add(t);
            }
        }
    }

    public void removeTag(ActionEvent e) {
        Tag t = (Tag) e.getComponent().getAttributes().get("TAG");
        tags.remove(t);
        if (undoTags.contains(t)) {
            undoTags.remove(t);
            proposedTags.add(t);
        }
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public String getRelationDescription() {
        return relationDescription;
    }

    public void setRelationDescription(String relationDescription) {
        this.relationDescription = relationDescription;
    }

    public int getRelationType() {
        return relationType;
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

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public Pattern getRelatedPattern() {
        return relatedPattern;
    }

    public void removeRelationship(ActionEvent e) {
        Relationship r = (Relationship) e.getComponent().getAttributes().get("RELATIONSHIP");
        relations.remove(r);
    }

    public void addRelationship(ActionEvent e) {
        Relationship relationship = RelationshipHelper.validateAddNewRelationship(this.relations,
                this.relatedPattern, this.parentPattern, this.relationDescription, this.relationType);
        
        if (relationship != null) {
            this.relations.add(relationship);
        }
    }

    public Collection<Relationship> getRelations() {
        return relations;
    }
}
