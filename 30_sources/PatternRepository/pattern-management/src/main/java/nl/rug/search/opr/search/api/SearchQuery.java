package nl.rug.search.opr.search.api;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author cm
 */
public class SearchQuery extends Queryable implements Cloneable {

    public LinkedList<Expression> data;

    private int offset = 0;
    private int limit = 10;

    public SearchQuery(Sentence s) {
        data = new LinkedList<Expression>();
        data.add(s);
    }

    SearchQuery() {
        data = new LinkedList<Expression>();
    }

    public void add(Expression e) {
        if (e == null) {
            return;
        }
        data.add(e);
    }

    public void addList(List<? extends Expression> list) {
        data.addAll(list);
    }

    public Expression remove() {
        return data.removeLast();
    }

    public List<Expression> getElements() {
        return data;
    }

    @Override
    public void visit(QueryBuilder builder) {
        builder.process(this);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        String result = "SearchQuery\n";
        for (Expression e : data) {
            result += e.toString() + "\n";
        }
        return result;
    }

    @Override
    protected SearchQuery clone() throws CloneNotSupportedException {
        SearchQuery clone = new SearchQuery();
        clone.addList(data);
        clone.setLimit(limit);
        clone.setOffset(offset);
        return  clone;
    }


}
