package nl.rug.search.opr.pattern;

import nl.rug.search.opr.entities.pattern.PatternVersion;
import nl.rug.search.opr.entities.pattern.File;
import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.pattern.Tag;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.Force;
import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.entities.pattern.Consequence;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import nl.rug.search.opr.ConfigConstants;
import nl.rug.search.opr.dao.GenericDaoBean;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.entities.pattern.relation.RelationshipType;
import nl.rug.search.opr.entities.template.Component;
import nl.rug.search.opr.search.SearchLocal;

/**
 * The Pattern bean is concerned with all kinds of pattern interactions like
 * storing, editing and deleting a pattern.
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 08.12.2009
 * @version 3.0
 */
@Stateless
public class PatternBean extends GenericDaoBean<Pattern, Long> implements PatternLocal {

    private static final Logger logger = Logger.getLogger(PatternBean.class.getName());

    @EJB
    private CategoryLocal categoryLocal;

    @EJB(beanName = "TagBean")
    private TagLocal tagLocal;

    @EJB
    private SearchLocal searchBean;

    @EJB
    private RelationshipTypeLocal relationBean;

    /**
     * Adds a pattern to the persistence context and returns its managed
     * enitity. This method is not appropriate if a pattern should be edited!
     *
     * @param p The pattern to be stored.
     * @return A managed instance of the added pattern.
     */
    @Override
    public Pattern add(Pattern p) {
        checkConsistency(p);

        Pattern uniqueNameCheck = getByUniqueName(p.getUniqueName());
        if (uniqueNameCheck != null) {
            throw new IllegalArgumentException(String.format("Unique name '%s' already exists", p.getUniqueName()));
        }

        /*
         * Swap relations, because the pattern itself has to be stored
         * before its relations can be stored
         *
         * In the current JPA implementation the parent of a many-to-one
         * relation is the "many" part. This is considered a bug and may be
         * fixed in following releases.
         */
        Collection<Relationship> relations = p.getRelations();
        p.setRelations(new ArrayList<Relationship>());


        /*
         * First store the pattern version, which is concerned with content,
         * attached files, driver etc.
         */
        p.getVersions().add(this.persistPatternVersion(p.getCurrentVersion()));


        // Persist tags
        Collection tags = tagLocal.persistByName(p.getTags());
        p.setTags(tags);


        if (p.getCategories().isEmpty()) {
            p.getCategories().add(categoryLocal.getRootCategory());
        }

        getManager().persist(p);


        /*
         * Store relationships. This is possible now, since the pattern has been
         * stored beforehand.
         */
        for (Relationship r : relations) {
            RelationshipType type = relationBean.getRelation(r);
            //getManager().persist(r.getType());
            r.setType(type);
            getManager().persist(r);
        }


        /*
         * Let the pattern know, which relations it has
         */
        p.setRelations(relations);
        Pattern rp = getManager().merge(p);

        /*
         * All related patterns need to be updated as well
         */
        for (Relationship r : relations) {
            Pattern related = r.getRelatedPattern(rp);
            related.getRelations().add(r);
            getManager().merge(related);
        }

        /*
         * At last the tags and categories can be assigned to the pattern.
         */
        for (Category c : p.getCategories()) {
            c.getCategoryPatterns().add(p);
            getManager().merge(c);
        }

        for (Tag t : p.getTags()) {
            t.getTagPatterns().add(p);
            getManager().merge(t);
        }

        getManager().flush();

        searchBean.addPatternToIndex(rp);

        return rp;
    }

    /**
     * Checks the consistency of a pattern
     * @param p
     * @throws IllegalArgumentException
     */
    private void checkConsistency(Pattern p) throws IllegalArgumentException {

        if (p.getCurrentVersion() == null) {
            throw new IllegalArgumentException("No current version");
        }
        if (p.getCurrentVersion().getLicense() == null) {
            throw new IllegalArgumentException("No license selected");
        }
        if ((p.getName() == null) || (p.getName().equals(""))) {
            throw new IllegalArgumentException("No name specified");
        }
        if ((p.getUniqueName() == null) || (p.getUniqueName().equals(""))) {
            throw new IllegalArgumentException("No uniquename specified");
        }
        if (p.getCurrentVersion().getTemplate() == null) {
            throw new IllegalArgumentException("No template specified");
        }

        // check relations
        List<Relationship> checkedRelations = new LinkedList<Relationship>();
        for (Relationship relation : p.getRelations()) {

            // check for relationships that are not bound to this pattern
            if (!relation.belongsToPattern(p)) {
                throw new IllegalArgumentException("Relationship does not belong to this pattern");
            }

            // check selfrelation
            if (relation.getPatternA().equals(relation.getPatternB())) {
                throw new IllegalArgumentException("Relationship referes to itsself");
            }

            // Check for multiple relationships
            for (Relationship checkedRelation : checkedRelations) {
                if (relation.hasSamePatternCombinationAs(checkedRelation)) {
                    throw new IllegalArgumentException("Found duplicate pattern combination in Relationships");
                }
            }
            checkedRelations.add(relation);
        }
    }

    @Override
    @Deprecated
    public void remove(Pattern p) {
//        getManager().remove(getManager().merge(p));
//
//        // Cascading relationships did not work
//        for (Relationship rel : p.getRelations()) {
//            getManager().remove(getManager().merge(rel));
//        }
//
//        // Cascading relationships did not work
//        for (PatternVersion version : p.getVersions()) {
//            getManager().remove(getManager().merge(version));
//        }
//
//        for (Category cat : p.getCategories()) {
//            getManager().refresh(getManager().merge(cat));
//        }
    }

    @Override
    public List<Component> getSortedComponentList(PatternVersion pv) {
        List<Component> sortedComponents = new LinkedList<Component>();

        try {
            Query q = createQuery(ConfigConstants.QUERY_GET_SORTED_COMPONENTS);
            q.setParameter("templateId", pv.getTemplate().getId());

            sortedComponents = q.getResultList();

        } catch (IllegalStateException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return sortedComponents;
    }

    private void handleNewRelationships(Pattern updatedPattern) {
        Collection<Relationship> newRelations = new ArrayList<Relationship>(updatedPattern.getRelations());
        for (Relationship newRelation : newRelations) {
            if (newRelation.getId() == null || getManager().find(Relationship.class, newRelation.getId()) == null) {
                // Relationship did not exist yet
                RelationshipType type = relationBean.getRelation(newRelation);
                //getManager().persist(newRelation.getType());
                newRelation.setType(type);

                getManager().persist(newRelation);
                // Wire other side of the relationship
                Pattern relatedPattern = newRelation.getRelatedPattern(updatedPattern);
                relatedPattern.getRelations().add(newRelation);
                getManager().merge(relatedPattern);
            } else {
                // relationship already existed
                getManager().merge(newRelation);
            }
        }
        /*
         * Let the pattern know, which relations it has
         */
        updatedPattern.setRelations(newRelations);
        getManager().merge(updatedPattern);
    }

    private void handleNewTags(Pattern updatedPattern) {
        /* Save new Tags and Categories and add them to a collection in
         * order to add them to the pattern
         */
        Collection<Tag> tags = new ArrayList<Tag>();
        for (Tag updatedTag : updatedPattern.getTags()) {
            Tag existingTag = tagLocal.getByName(updatedTag.getName());
            if (existingTag == null) {
                tagLocal.makePersistent(updatedTag);
                tags.add(updatedTag);
            } else {
                tags.add(existingTag);
            }
        }
        updatedPattern.setTags(tags);
    }

    private void handleRemovedCategories(Pattern storedPattern, Pattern updatedPattern) {
        Collection<Category> oldCategories = new ArrayList<Category>(storedPattern.getCategories());
        for (Category oldCategory : oldCategories) {
            if (!updatedPattern.getCategories().contains(oldCategory)) {
                oldCategory.getCategoryPatterns().remove(storedPattern);
                getManager().merge(oldCategory);
            }
        }
    }

    private void handleRemovedRelationships(Pattern storedPattern, Pattern updatedPattern) {
        Collection<Relationship> oldRelations = new ArrayList<Relationship>(storedPattern.getRelations());

        for (Relationship oldRelation : oldRelations) {
            // if an existing relation is not assigned anymore
            if (!updatedPattern.getRelations().contains(oldRelation)) {
                storedPattern.getRelations().remove(oldRelation);
                // also remove relationship from related pattern
                Pattern relatedPattern = oldRelation.getRelatedPattern(storedPattern);
                relatedPattern.getRelations().remove(oldRelation);
                getManager().merge(relatedPattern);
                getManager().remove(oldRelation);
            }
        }
    }

    private void handleRemovedTags(Pattern storedPattern, Pattern updatedPattern) {
        Collection<Tag> oldTags = new ArrayList<Tag>(storedPattern.getTags());
        for (Tag oldTag : oldTags) {
            if (!updatedPattern.getTags().contains(oldTag)) {
                oldTag.getTagPatterns().remove(storedPattern);
                tagLocal.makePersistent(oldTag);
                if (oldTag.getTagPatterns().isEmpty()) {
                    tagLocal.makeTransient(oldTag);
                }
            }
        }
    }

    private enum EditType {

        common, version;
    }

    @Override
    public Pattern editCommon(Pattern p) {
        return edit(p, EditType.common);
    }

    @Override
    public Pattern editVersion(Pattern p) {
        return edit(p, EditType.version);
    }

    private Pattern edit(Pattern updatedPattern, EditType type) {

        checkConsistency(updatedPattern);

        // Retrieve existing pattern to find changes
        Pattern storedPattern = getManager().find(Pattern.class, updatedPattern.getId());

        if (storedPattern == null) {
            throw new EJBException("Pattern has no stored version");
        }

        /*
         * First store the pattern version, which is concerned with content,
         * attached files, driver etc.
         */
        if (type != EditType.common) {
            updatedPattern.getVersions().add(this.persistPatternVersion(updatedPattern.getCurrentVersion()));
        }


        handleRemovedTags(storedPattern, updatedPattern);
        handleRemovedCategories(storedPattern, updatedPattern);
        handleRemovedRelationships(storedPattern, updatedPattern);
        handleNewTags(updatedPattern);

        handleNewRelationships(updatedPattern);

        updatedPattern = getManager().merge(updatedPattern);

        for (Tag t : updatedPattern.getTags()) {
            if (!t.getTagPatterns().contains(updatedPattern)) {
                t.getTagPatterns().add(updatedPattern);
                tagLocal.makePersistent(t);
            }
        }

        for (Category c : updatedPattern.getCategories()) {
            if (!c.getCategoryPatterns().contains(updatedPattern)) {
                c.getCategoryPatterns().add(updatedPattern);
                getManager().merge(c);
            }
        }

        getManager().flush();

        return updatedPattern;
    }

    /**
     * This method persists a pattern version with all containing information.
     * 
     * @param pv The Pattern Version to be persisted.
     * @return The managed Pattern Version object.
     */
    private PatternVersion persistPatternVersion(PatternVersion pv) {

        for (TextBlock b : pv.getBlocks()) {
            getManager().persist(b);
        }

        /*
         * A force or consequence at least needs some valuable information
         * in order to get persisted.
         */
        for (Force f : pv.getForces()) {
            if (!f.getDescription().equals("")
                    || !f.getFunctionality().equals("")
                    || f.getQualityAttribute() != null
                    || f.getImpactIndication() != null) {
                getManager().persist(f);
            }
        }

        for (Consequence c : pv.getConsequences()) {
            if (!c.getDescription().equals("")
                    || c.getQualityAttribute() != null
                    || c.getImpactIndication() != null) {
                getManager().persist(c);
            }
        }

        List<File> files = new ArrayList<File>();

        for (File f : pv.getFiles()) {
            File file = null;

            if ((file = getManager().find(File.class, f.getId())) == null) {
                getManager().persist(f);
                files.add(f);
            } else {
                files.add(file);
            }
        }

        pv.setFiles(files);

        pv.setDocumentedWhen(new Date());

        getManager().persist(pv);

        return pv;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Pattern> getSimiliarPatterns(String search) {
        List<Pattern> patterns = new ArrayList<Pattern>();

        try {
            Query q = createQuery(ConfigConstants.QUERY_GET_SIMILAR);
            q.setParameter("param", "%" + search.toLowerCase() + "%");

            patterns = q.getResultList();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return patterns;
    }

    @Override
    public Pattern getByUniqueName(String uniqueName) {
        try {
            Query q = createQuery(ConfigConstants.QUERY_GET_BY_UNIQUE_NAME);
            q.setParameter("uniqueName", uniqueName);

            return (Pattern) q.getSingleResult();

        } catch (NoResultException e) {
            /* The uniqueName is not yet registered */
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
