package nl.rug.search.opr.search.api;

import java.util.Collection;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.search.solr.SolrSearchFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 *
 * @author cm
 */
public abstract class SearchFactory {

    private static Logger logger = LoggerFactory.getLogger(SearchFactory.class);

    public abstract ResultList query(SearchQuery q) throws SearchException;

    public abstract IndexingTask createAddIndexTask(Pattern pattern);

    public abstract IndexingTask createUpdateIndexTask(Pattern pattern);

    public abstract IndexingTask createDeleteIndexTask(long id);

    public abstract IndexingTask createBuildIndexTask(Collection<Pattern> patterns);

    public static SearchFactory getSearchFactory(String className) {
        if (className != null) {
            if (!className.isEmpty()) {
                try {
                    Class<?> searchFactory = Class.forName(className);
                    if (!SearchFactory.class.isAssignableFrom(searchFactory)) {
                        throw new ClassCastException();
                    }
                    SearchFactory factory = (SearchFactory) searchFactory.newInstance();
                    return factory;

                } catch (InstantiationException ex) {
                    logger.warn(ex.getMessage());
                } catch (IllegalAccessException ex) {
                    logger.warn(ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    logger.warn(ex.getMessage());
                }
            }
        }
        logger.info(String.format("Unable to use SearchFactory '%s' - using standard SolrSearchFactory", className));
        return new SolrSearchFactory();
    }
}
