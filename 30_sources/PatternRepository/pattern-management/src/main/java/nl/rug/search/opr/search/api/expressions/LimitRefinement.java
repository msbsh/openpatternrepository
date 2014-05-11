
package nl.rug.search.opr.search.api.expressions;

import nl.rug.search.opr.search.api.*;
import nl.rug.search.opr.search.*;

/**
 *
 * @author cm
 */
public class LimitRefinement extends Refinement {

    private int limit;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void visit(QueryBuilder builder) {
       builder.process(this);
    }

    @Override
    public String toString() {
        return " with a limit of " + limit;
    }

}
