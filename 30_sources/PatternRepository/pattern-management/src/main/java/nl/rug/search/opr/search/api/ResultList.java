package nl.rug.search.opr.search.api;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cm
 */
@XmlRootElement
public class ResultList extends ArrayList<Result> implements List<Result> {

    public static final long serialVersionUID = 1l;
    private int offset;
    private int limit;
    private int count;
    private long time;
    @XmlTransient
    private SearchQuery query;

    public ResultList() {}

    public ResultList(SearchQuery q, int c, long t) {
        this.query = q;
        this.offset = q.getOffset();
        this.limit = q.getLimit();
        this.time = t;
        this.count = c;
    }

    public static ResultList getNullResult() {
        ResultList nil = new ResultList();
        nil.offset = 0;
        nil.limit = 0;
        nil.count = 0;
        nil.time = 0;
        nil.query = new SearchQuery();
        return nil;
    }

    public int getCount() {
        return count;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public long getTime() {
        return time;
    }

    @XmlTransient
    public SearchQuery getQuery() {
        return query;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setQuery(SearchQuery query) {
        this.query = query;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public SearchQuery getNext(int items) {
        return getPage(offset + limit, items);
    }

    public SearchQuery getPrevious(int items) {
        return getPage(offset - items, items);
    }

    public SearchQuery getPage(int start, int limit) {

        if (start >= count) {
            return null;
        }

        if (start < 0) {
            return null;
        }

        SearchQuery result = null;
        try {
            result = query.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ResultList.class.getName()).log(Level.SEVERE, null, ex);
        }


        result.setOffset(start);
        result.setLimit(limit);

        return result;
    }

    public List<Result> getResult() {
        return this;
    }

    public void setResult(List<Result> result) {
        this.addAll(result);
    }


}
