
package nl.rug.search.opr.entities.pattern.relation;

import javax.persistence.Entity;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class BelongsTo extends RelationshipType {

    @Override
    public RelationshipType getThis() {
        return this;
    }

    @Override
    public String toString() {
        return "BelongsTo";
    }
}
