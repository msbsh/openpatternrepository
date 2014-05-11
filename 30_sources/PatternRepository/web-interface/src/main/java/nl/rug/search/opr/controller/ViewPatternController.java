package nl.rug.search.opr.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.opr.Collapsable;
import nl.rug.search.opr.ContentWrapper;
import nl.rug.search.opr.entities.pattern.Consequence;
import nl.rug.search.opr.entities.pattern.Force;
import nl.rug.search.opr.entities.pattern.Indicator;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.pattern.PatternLocal;
import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.entities.template.Component;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 27.10.2009
 */
@ManagedBean(name="viewPatternCtrl")
@RequestScoped
public class ViewPatternController {

    private static final String PATTERN_ID = "patternId";
    private static final String VERSION_ID = "versionId";
    private static final String LIST_START = "<ul>";
    private static final String LIST_END = "</ul>";
    private static final String LISTELEMENT_START = "<li>";
    private static final String LISTELEMENT_END = "</li>";


    @EJB
    private PatternLocal pl;

    private Pattern pattern;
    private Collection<RatedQualityAttribute> qualityAttributes;
    private Collection<Collapsable<Relationship>> relations;
    private PatternVersion patternVersion;
    private List<ContentWrapper> patternContent;

    public ViewPatternController() {
        this.qualityAttributes = new ArrayList<RatedQualityAttribute>();
        this.relations = new ArrayList<Collapsable<Relationship>>();
        this.patternContent = new LinkedList<ContentWrapper>();
    }

    public String removePattern(ActionEvent event) {
        pl.remove(pattern);
        return "START";
    }

    public Pattern getPattern() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String patternIdStr = request.getParameter(PATTERN_ID);
        String versionIdStr = request.getParameter(VERSION_ID);

        if (patternIdStr != null && !patternIdStr.equals("")) {
            try {
                Long tmpId = Long.parseLong(patternIdStr);

                if ((this.pattern = pl.getById(tmpId)) == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No pattern was found!"));
                } else {
                    this.patternVersion = this.pattern.getCurrentVersion();

                    if (versionIdStr != null && !versionIdStr.equals("")) {
                        long versionId = Integer.parseInt(versionIdStr);

                        for (PatternVersion pv : this.pattern.getVersions()) {
                            if (pv.getId().equals(versionId)) {
                                this.patternVersion = pv;
                            }
                        }
                    }

                    setQualityAttributes();
                    setCollapsableRelations();
                    setContent();
                }
            } catch (NumberFormatException nfe) { /* TODO: Do something? */ }
        }

        return this.pattern;
    }

    public List<ContentWrapper> getContent() {
        return this.patternContent;
    }

    private void setContent() {
        this.patternContent.clear();
        List<Component> sortedComponents = pl.getSortedComponentList(this.patternVersion);
        List<ContentWrapper> content = new LinkedList<ContentWrapper>();

        for (Component c : sortedComponents) {
            ContentWrapper contentBlock = new ContentWrapper(c.getName(), this.pattern.getUniqueName(), c.getIdentifier());

            switch (c.getType()) {
                case FORCES:
                    contentBlock.setBody(forces2String());
                    break;
                case CONSEQUENCES:
                    contentBlock.setBody(consequences2String());
                    break;
                default:
                    //System.out.println("ComponentID: " + c.getId());
                    for (TextBlock tb : this.patternVersion.getBlocks()) {
                        //System.out.println("Textblock ComponentID: " + tb.getComponent().getId());
                        if (tb.getComponent().getId().equals(c.getId())) {
                            contentBlock.setBody(tb.getText());
                        }
                    }
                    break;
            }

            content.add(contentBlock);
        }

        this.patternContent = content;
    }

    private String forces2String() {
        if (this.patternVersion == null) {
            return "";
        }

        String forcesString = LIST_START;
        for (Force f : this.patternVersion.getForces()) {
            forcesString = forcesString.concat(LISTELEMENT_START + f.getDescription() + LISTELEMENT_END);
        }
        forcesString = forcesString.concat(LIST_END);

        return forcesString;
    }

    private String consequences2String() {
        if (this.patternVersion == null) {
            return "";
        }

        String consequenceString = LIST_START;
        for (Consequence c : this.patternVersion.getConsequences()) {
            consequenceString = consequenceString.concat(LISTELEMENT_START + c.getDescription() + LISTELEMENT_END);
        }
        consequenceString = consequenceString.concat(LIST_END);

        return consequenceString;
    }

    public PatternVersion getVersion() {
        return this.patternVersion;
    }

    private void setCollapsableRelations() {
        this.relations.clear();

        for (Relationship r : this.pattern.getRelations()) {
            this.relations.add(new Collapsable<Relationship>(r));
        }
    }

    public Collection<Collapsable<Relationship>> getRelations() {
        return this.relations;
    }

    private void setQualityAttributes() {
        this.qualityAttributes.clear();

        Collection<RatedQualityAttribute> unsortedQualities = new ArrayList<RatedQualityAttribute>();

        for (Force f : this.pattern.getCurrentVersion().getForces()) {
            if (f.getQualityAttribute() != null) {
                unsortedQualities.add(
                        new RatedQualityAttribute(f.getQualityAttribute().getName(), f.getImpactIndication()));
            }
        }

        for (Consequence c : this.pattern.getCurrentVersion().getConsequences()) {
            if (c.getQualityAttribute() != null) {
                unsortedQualities.add(
                        new RatedQualityAttribute(c.getQualityAttribute().getName(), c.getImpactIndication()));
            }
        }

        Collection<RatedQualityAttribute> verynegative = new ArrayList<RatedQualityAttribute>();
        Collection<RatedQualityAttribute> negative = new ArrayList<RatedQualityAttribute>();
        Collection<RatedQualityAttribute> neutral = new ArrayList<RatedQualityAttribute>();
        Collection<RatedQualityAttribute> positive = new ArrayList<RatedQualityAttribute>();
        Collection<RatedQualityAttribute> verypositive = new ArrayList<RatedQualityAttribute>();
        Collection<RatedQualityAttribute> unrated = new ArrayList<RatedQualityAttribute>();

        for (RatedQualityAttribute rqa : unsortedQualities) {
            if (rqa.getIndicator() == null) {
                checkedAdd(unrated, rqa);
                continue;
            }

            switch (rqa.getIndicator()) {
                case verynegative:
                    checkedAdd(verynegative, rqa);
                    continue;
                case negative:
                    checkedAdd(negative, rqa);
                    continue;
                case neutral:
                    checkedAdd(neutral, rqa);
                    continue;
                case positive:
                    checkedAdd(positive, rqa);
                    continue;
                case verypositive:
                    checkedAdd(verypositive, rqa);
                    continue;
            }
        }

        this.qualityAttributes.addAll(verypositive);
        this.qualityAttributes.addAll(positive);
        this.qualityAttributes.addAll(neutral);
        this.qualityAttributes.addAll(negative);
        this.qualityAttributes.addAll(verynegative);
        this.qualityAttributes.addAll(unrated);
    }

    private void checkedAdd(Collection<RatedQualityAttribute> collection, RatedQualityAttribute qa) {
        for (RatedQualityAttribute rqa : collection) {
            if (rqa.getIndicator() == qa.getIndicator()
                    && rqa.getName().equals(qa.getName())) {
                return;
            }
        }

        collection.add(qa);
    }

    public Collection<RatedQualityAttribute> getQualityAttributes() {
        return this.qualityAttributes;
    }

    public String getDocumentationDate() {
        if (this.pattern == null) {
            return null;
        }

        Date documentationDate = this.pattern.getCurrentVersion().getDocumentedWhen();

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(documentationDate);

        return new String(
                gc.get(GregorianCalendar.DAY_OF_MONTH) + " "
                + gc.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.SHORT, Locale.ENGLISH) + ". "
                + gc.get(GregorianCalendar.YEAR));
    }

    public class RatedQualityAttribute {

        private String name;
        private Indicator indicator;

        public RatedQualityAttribute(String name, Indicator indicator) {
            this.name = name;
            this.indicator = indicator;
        }

        public Indicator getIndicator() {
            return indicator;
        }

        public void setIndicator(Indicator indicator) {
            this.indicator = indicator;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
