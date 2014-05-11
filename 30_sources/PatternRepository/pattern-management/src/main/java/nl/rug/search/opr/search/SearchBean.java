package nl.rug.search.opr.search;

import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import nl.rug.search.opr.ConfigConstants;
import nl.rug.search.opr.Configuration;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.search.api.IndexingTask;
import nl.rug.search.opr.search.api.ResultList;
import nl.rug.search.opr.search.api.SearchException;
import nl.rug.search.opr.search.api.SearchFactory;
import nl.rug.search.opr.search.api.SearchQuery;
import nl.rug.search.opr.search.lexer.SQLexer;
import nl.rug.search.opr.search.parser.Parser;
import nl.rug.search.opr.search.parser.SQParser;

/**
 *
 * @author cm
 */
@Stateless
public class SearchBean implements SearchLocal {

    private static final Logger logger = Logger.getLogger(SearchBean.class.getName());
    private static SearchFactory search;
    @EJB
    private IndexLocal indexBean;

    public SearchBean() {
        String searchFactoryName = (String) Configuration.getInstance().
                getProperty(ConfigConstants.SEARCH_FACTORY_NAME);
        search = SearchFactory.getSearchFactory(searchFactoryName);
    }

    @Override
    public ResultList search(SearchQuery query) {
        try {
            return search.query(query);
        } catch (SearchException ex) {
            logger.log(Level.SEVERE, "Search Exception: query could not be performed", ex);
        }
        return ResultList.getNullResult();
    }

    @Override
    public SearchQuery createSearchQuery(String searchString) throws QueryParseException {

        if (searchString == null || searchString.isEmpty()) {
            return null;
        }

        String lowerCaseSearch = searchString.toLowerCase();

        Parser p = new SQParser(new SQLexer(new StringReader(lowerCaseSearch)));
        if (p.yyparse() != 0) {
            throw new QueryParseException("Could not parse query string " + lowerCaseSearch);
        }

        return p.getQuery();
    }

    @Override
    public void addPatternToIndex(Pattern pattern) {
        IndexingTask task = search.createAddIndexTask(pattern);
        indexBean.indexTask(task);
    }

    @Override
    public void updatePatternInIndex(Pattern pattern) {
        IndexingTask task = search.createUpdateIndexTask(pattern);
        indexBean.indexTask(task);
    }

    @Override
    public void deletePatternFromIndex(long patternId) {
        IndexingTask task = search.createDeleteIndexTask(patternId);
        indexBean.indexTask(task);
    }

    @Override
    public void buildIndex(List<Pattern> patterns) {
        IndexingTask task = search.createBuildIndexTask(patterns);

        indexBean.resetRebuildIndexRequired();
        indexBean.indexTask(task);
    }

    @Override
    public boolean isRebuildIndexRequired() {
        return indexBean.isRebuildIndexRequired();
    }
}
