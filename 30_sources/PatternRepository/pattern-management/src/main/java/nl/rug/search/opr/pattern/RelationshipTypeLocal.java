package nl.rug.search.opr.pattern;

import javax.ejb.Local;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.entities.pattern.relation.RelationshipType;

/**
 *
 * @author mv
 */
@Local
public interface RelationshipTypeLocal {

    public RelationshipType getRelation(final Relationship relation);
    
}
