package nl.rug.search.opr.search.api;

/**
 *
 * @author cm
 */

public abstract class Sentence extends Expression {

    private Qualifier qualifier;

    public Sentence(Qualifier q) {
        qualifier=q;
    }

    public Qualifier getQualifier() {
        return qualifier;
    }

    public void setQualifier(Qualifier qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public abstract String toString();

}
