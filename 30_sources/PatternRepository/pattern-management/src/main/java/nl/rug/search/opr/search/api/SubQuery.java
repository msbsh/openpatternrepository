package nl.rug.search.opr.search.api;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author cm
 */
public class SubQuery extends Expression {

    public LinkedList<Sentence> list;
    private Conjunction conjunction;

    public SubQuery(Conjunction c, Sentence s) {

        if (c == null) {
            c = Conjunction.OR;
        }

        conjunction = c;
        list = new LinkedList<Sentence>();
        list.add(s);
    }

    protected SubQuery(Sentence s) {
        list = new LinkedList<Sentence>();
        list.add(s);
    }

    public Conjunction getConjunction() {
        return conjunction;
    }

    public void setConjunction(Conjunction conjunction) {
        this.conjunction = conjunction;
    }

    public void add(Sentence e) {
        list.add(e);
    }

    public void addList(List<? extends Sentence> l) {
        list.addAll(l);
    }

    public Sentence remove() {
        return list.removeLast();
    }

    public List<Sentence> getElements() {
        return list;
    }

    @Override
    public void visit(QueryBuilder builder) {
        builder.process(this);
    }

    @Override
    public String toString() {
        String result = "SubQuery\n";
        for (Sentence s : getElements()) {
            result += s.toString() + "\n";
        }
        return result;
    }
}
