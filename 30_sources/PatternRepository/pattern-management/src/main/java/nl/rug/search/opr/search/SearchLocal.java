
package nl.rug.search.opr.search;

import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.search.api.ResultList;
import nl.rug.search.opr.search.api.SearchQuery;

/**
 *
 * @author cm
 */
@Local
public interface SearchLocal {

    ResultList search(SearchQuery query);

    SearchQuery createSearchQuery(String searchString)  throws QueryParseException;

    @Asynchronous
    void addPatternToIndex(Pattern pattern);

    @Asynchronous
    void updatePatternInIndex(Pattern pattern);

    @Asynchronous
    void deletePatternFromIndex(long patternId);

    @Asynchronous
    void buildIndex(List<Pattern> patterns);

    public boolean isRebuildIndexRequired();
}
