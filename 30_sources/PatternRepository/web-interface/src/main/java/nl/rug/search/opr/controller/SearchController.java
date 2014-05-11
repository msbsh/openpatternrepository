package nl.rug.search.opr.controller;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.opr.search.QueryParseException;
import nl.rug.search.opr.search.SearchLocal;
import nl.rug.search.opr.search.api.ResultList;
import nl.rug.search.opr.search.api.SearchQuery;

/**
 *
 * @author cm
 */
@ManagedBean(name = "searchCtrl")
@ViewScoped
public class SearchController {

    @EJB
    private SearchLocal searchBean;
    private ResultList resultList;
    private List<Integer> pages = new LinkedList<Integer>();
    private String benchmark;
    private int max;
    private int start;
    private static final String QUERY = "query";
    private int limit = 7;
    private int count = 0;
    private int pageCount = 0;
    private int currentPage = 0;
    private String fulltext;

    @PostConstruct
    private void initialSearch() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String query = request.getParameter(QUERY);

        if (query != null && query.length() > 0) {
            setFulltext(query);
            SearchQuery sq;

            try {
                sq = searchBean.createSearchQuery(fulltext.toLowerCase());
                sq.setLimit(limit);
            } catch (QueryParseException ex) {
                return;
            }
            query(sq);
        }
    }

    public String getFulltext() {
        return fulltext;
    }

    public void setFulltext(String fulltext) {
        this.fulltext = fulltext;
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

    public String getBenchmark() {
        return benchmark;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public ResultList getResults() {
        return resultList;
    }

    public boolean isNextPageAvailable() {
        return start+limit < count;
    }

    public boolean isPreviousPageAvailable() {
        return start >= limit;
        }

    public void searchIt(ActionEvent e) {
        if (fulltext == null || fulltext.trim().equals("")) {
            return;
        }

        SearchQuery sq;
        try {
            sq = searchBean.createSearchQuery(fulltext);
            sq.setLimit(limit);
        } catch (QueryParseException ex) {
            ex.printStackTrace();
            return;
        }
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
        start = resultList.getOffset() + 1;
        benchmark = (double) resultList.getTime() / 1000.0 + "";


        pageCount = (int) Math.ceil(max / (double) limit);
        currentPage = (int) Math.ceil((start - 1) / (double) limit) + 1;

        pages.clear();
        for (int i = 1; i <= pageCount; i++) {
            pages.add(i);
        }

    }

    public void next(ActionEvent e) {
        SearchQuery sq = resultList.getNext(limit);
        query(sq);
    }

    public void prev(ActionEvent e) {
        SearchQuery sq = resultList.getPrevious(limit);
        query(sq);
    }

    public void page(ActionEvent e) {
        int page = (Integer) e.getComponent().getAttributes().get("page");

        int s = limit * (page - 1);
        SearchQuery sq = resultList.getPage(s, limit);
        query(sq);
    }
}
