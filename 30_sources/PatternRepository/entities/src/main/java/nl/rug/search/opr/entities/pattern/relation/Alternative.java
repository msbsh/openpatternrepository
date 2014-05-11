package nl.rug.search.opr.entities.pattern.relation;

import javax.persistence.Entity;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 09.10.2009
 */

@Entity
public class Alternative extends RelationshipType {

    @Override
    public RelationshipType getThis() {
        return this;
    }

    @Override
    public String toString() {
        return "Alternative";
    }

}
