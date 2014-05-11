/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.opr.search.mock;

import java.util.Collection;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.search.api.IndexingTask;
import nl.rug.search.opr.search.api.ResultList;
import nl.rug.search.opr.search.api.SearchException;
import nl.rug.search.opr.search.api.SearchFactory;
import nl.rug.search.opr.search.api.SearchQuery;

/**
 *
 * @author Georg Fleischer
 */
public class MockSearchFactory extends SearchFactory {

    @Override
    public ResultList query(SearchQuery q) throws SearchException {
        return ResultList.getNullResult();
    }

    @Override
    public IndexingTask createAddIndexTask(Pattern pattern) {
        return new MockIndexingTask(false);
    }

    @Override
    public IndexingTask createUpdateIndexTask(Pattern pattern) {
        return new MockIndexingTask(false);
    }

    @Override
    public IndexingTask createDeleteIndexTask(long id) {
        return new MockIndexingTask(false);
    }

    @Override
    public IndexingTask createBuildIndexTask(Collection<Pattern> patterns) {
        return new MockIndexingTask(false);
    }
}
