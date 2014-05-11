
package nl.rug.search.opr.search.api.expressions;

import nl.rug.search.opr.search.api.*;
import nl.rug.search.opr.search.*;

/**
 *
 * @author cm
 */
public class KeywordSentence extends Sentence {

    public String keyword;
    public String searchArea;

    public KeywordSentence(Qualifier q, String searchArea , String keyword) {
        super(q);
        setKeyword(keyword);
        setSearchArea(searchArea);
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSearchArea() {
        return searchArea;
    }

    public void setSearchArea(String searchArea) {
        this.searchArea = searchArea;
    }

    @Override
    public String toString() {
        return getQualifier() + " the keyword " + keyword + " in " + searchArea;

    }

    @Override
    public void visit(QueryBuilder builder) {
        builder.process(this);
    }

    
}
