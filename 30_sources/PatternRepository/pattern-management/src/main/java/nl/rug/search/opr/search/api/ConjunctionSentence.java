
package nl.rug.search.opr.search.api;

/**
 *
 * @author cm
 */
public class ConjunctionSentence extends Expression {

    private Conjunction conjunction;
    private Sentence sentence;

    public ConjunctionSentence(Conjunction c, Sentence s) {
        setConjunction(c);
        setSentence(s);
    }

    public Conjunction getConjunction() {
        return conjunction;
    }

    public void setConjunction(Conjunction conjunction) {
        this.conjunction = conjunction;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    @Override
    public void visit(QueryBuilder builder) {
        builder.process(this);
    }
}
