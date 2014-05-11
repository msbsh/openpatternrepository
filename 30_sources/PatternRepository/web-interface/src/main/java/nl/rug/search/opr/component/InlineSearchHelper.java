package nl.rug.search.opr.component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import nl.rug.search.opr.search.QueryParseException;
import nl.rug.search.opr.search.SearchLocal;
import nl.rug.search.opr.search.api.Result;
import nl.rug.search.opr.search.api.ResultList;
import nl.rug.search.opr.search.api.SearchQuery;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cm
 */
@ManagedBean
@ViewScoped
public class InlineSearchHelper {

    private String searchString;
    private ResultList resultList;
    private int limit = 5;
    private int count;
    
    private org.slf4j.Logger logger = LoggerFactory.getLogger(InlineSearchHelper.class);

    @EJB
    private SearchLocal searchBean;
    private int max;
    private int num;
    private int start;
    private String benchmark;
    private int pageCount;
    private int currentPage;

    @PostConstruct
    private void reset() {
        count = -1;
        resultList = null;
    }

    public void search(ActionEvent e) {
        if (searchString.length() <= 0) {
            reset();
            return;
        }
        SearchQuery query = null;
        try {
            query = searchBean.createSearchQuery(searchString);
            query.setLimit(limit);
        } catch (QueryParseException ex) {
            Logger.getLogger(InlineSearchHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        query(query);
    }

    public void next(ActionEvent e) {
        SearchQuery sq = resultList.getNext(limit);
        query(sq);
    }

    public void prev(ActionEvent e) {
        SearchQuery sq = resultList.getPrevious(limit);
        query(sq);
    }

    private void query(SearchQuery q) {

        if (q == null) {
            return;
        }

        resultList = searchBean.search(q);

        if (resultList == null) {
            return;
        }

        count = resultList.getCount();
        max = resultList.getCount();
        num = resultList.getOffset() + resultList.size();
        start = resultList.getOffset() + 1;
        benchmark = (double) resultList.getTime() / 1000.0 + "";

        
        pageCount = (int) Math.ceil(max / (double) limit);
        currentPage = (int) Math.ceil((start-1) / (double) limit)+1;
    }

    public boolean isNextPageAvailable() {
        return start+limit < count;
    }

    public boolean isPreviousPageAvailable() {
        return start >= limit;
    }
    
    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public List<Result> getResults() {
        if (resultList==null) {
            return null;
        } else {
            return resultList.getResult();
        }
        
    }

    public int getCount() {
        return count;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }
}
