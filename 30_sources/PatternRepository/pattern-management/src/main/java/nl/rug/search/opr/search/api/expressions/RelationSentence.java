
package nl.rug.search.opr.search.api.expressions;

import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.entities.pattern.relation.RelationshipType;
import nl.rug.search.opr.search.api.Qualifier;
import nl.rug.search.opr.search.api.QueryBuilder;
import nl.rug.search.opr.search.api.Sentence;

/**
 *
 * @author cm
 */
public class RelationSentence extends Sentence {

    private Pattern pattern;
    private RelationshipType type;

    public RelationSentence(Qualifier q, Pattern p) {
        super(q);
        this.pattern = p;
    }

    public RelationSentence(Qualifier q, RelationshipType t,  Pattern p) {
        this(q,p);
        this.type = t;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getQualifier() + " a relation to " + pattern + " of the type " + type;
    }

    @Override
    public void visit(QueryBuilder builder) {
        builder.process(this);
    }



}
