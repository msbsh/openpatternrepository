package nl.rug.search.opr.pattern;

import javax.ejb.Stateless;
import javax.persistence.Query;
import nl.rug.search.opr.dao.GenericDaoBean;
import nl.rug.search.opr.entities.pattern.relation.Relationship;
import nl.rug.search.opr.entities.pattern.relation.RelationshipType;

/**
 *
 * @author mv
 */
@Stateless
public class RelationshipTypeBean extends GenericDaoBean<RelationshipType, Long> implements RelationshipTypeLocal {

    @Override
    public RelationshipType getRelation(final Relationship relation) {
        Query q = getManager().createNativeQuery("select rt.id from RELATIONSHIPTYPE as rt where rt.dtype=?");
        q.setParameter(1, relation.getType().toString());

        long result = Long.parseLong(((java.util.AbstractCollection<Object>) q.getSingleResult()).iterator().next().toString());

        return getManager().find(RelationshipType.class, result);
    }

}
