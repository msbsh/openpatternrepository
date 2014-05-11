/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.opr.component;

import java.util.Collection;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.relation.Alternative;
import nl.rug.search.opr.entities.pattern.relation.BelongsTo;
import nl.rug.search.opr.entities.pattern.relation.Combination;
import nl.rug.search.opr.entities.pattern.relation.Variant;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.entities.pattern.relation.RelationshipType;

/**
 *
 * @author Jan Nikolai Trzeszkowski <info@j-n-t.de>
 * @version 1
 */
public class RelationshipHelper {

    public static final int TYPE_ALTERNATIVE = 0;
    public static final int TYPE_COMBINATION = 1;
    public static final int TYPE_VARIANT = 2;
    public static final int TYPE_BELONGS_TO = 3;

    /**
     * Checks if a new Relationship between two patterns can be created.
     * It also generates the new Pattern and returns this new Relationship.
     *
     * @param relations Collection of actually available Relationships
     * @param relatedPattern the related Pattern
     * @param parentPattern the parent Pattern
     * @param relationshipDescription String representing the description of the new Relationship
     * @param relationType indicator for the RelationshipType
     * @return Relationship that can be added to the list of relationship if it is allowed. Otherwise null.
     */
    public static Relationship validateAddNewRelationship(Collection<Relationship> relations,
            Pattern relatedPattern, Pattern parentPattern, String relationshipDescription, int relationType) {

        Relationship relation = new Relationship();

        if (relatedPattern == null || parentPattern == null || relatedPattern.getId().equals(parentPattern.getId())) {
            return null;
        }
        RelationshipType type = null;
        switch (relationType) {
            case TYPE_ALTERNATIVE:
                type = new Alternative();
                break;

            case TYPE_COMBINATION:
                type = new Combination();
                break;

            case TYPE_VARIANT:
                type = new Variant();
                break;
                
            case TYPE_BELONGS_TO:
                type = new BelongsTo();
                break;

        }
        if(type == null) {
            return null;
        }
        for (Relationship r : parentPattern.getRelations()) {
            if(r.getPatternB().getId().equals(relatedPattern.getId())) {
                if(r.getType().toString().equals(type.toString())) {
                   return null;
                }
            }
        }
        relation.setType(type);
        relation.setDescription(relationshipDescription);
        relation.setPatternA(parentPattern);
        relation.setPatternB(relatedPattern);

        return relation;
    }
}
