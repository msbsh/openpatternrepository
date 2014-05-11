package nl.rug.search.opr.search.solr;

import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.search.api.IndexingTask;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.rug.search.opr.search.api.Result;
import nl.rug.search.opr.search.api.ResultList;
import nl.rug.search.opr.search.api.SearchException;
import nl.rug.search.opr.search.api.SearchFactory;
import nl.rug.search.opr.search.api.SearchQuery;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import static nl.rug.search.opr.search.solr.SolrConnectionHelper.getServer;

/**
 *
 * @author cm
 */
public class SolrSearchFactory extends SearchFactory {
    private static Logger logger = LoggerFactory.getLogger(SearchFactory.class);

    
    @Override
    public ResultList query(SearchQuery q) throws SearchException {

        if (q == null) {
            return null;
        }

        SolrQueryBuilder lq = new SolrQueryBuilder();
        q.visit(lq);

        SolrQuery solrQuery = lq.getQuery();

        ResultList results = null;
        QueryResponse response = null;
        try {
            response = getServer().query(solrQuery);
        } catch (SolrServerException ssex) {
            logger.warn(ssex.getMessage());
            throw new SearchException(ssex);
        }

        logger.info("Search: query(" + solrQuery.getQuery() + ") took about " + response.getElapsedTime() + "ms");



        if (response != null) {

            int count = (int) response.getResults().getNumFound();
            long time = response.getElapsedTime();

            results = new ResultList(q, count, time);

            for (SolrDocument doc : response.getResults()) {

                Result tmp = new Result();
                long id = 0;
                try {
                    id = Long.parseLong(doc.getFieldValue("id").toString());
                } catch (NumberFormatException e) {
                    logger.error("Could not parse pattern id string to long");
                }
                tmp.setId(id);
                tmp.setName(doc.getFieldValue("name").toString());
                tmp.setUniquename(doc.getFieldValue("uniquename").toString());

                Collection<String> highlights = new ArrayList<String>();
                for (List<String> h : response.getHighlighting().get(id + "").values()) {
                    highlights.addAll(h);
                }
                tmp.setHighlighted(highlights);

                float score = 0;
                try {
                    score = Float.parseFloat(doc.getFieldValue("score").toString());
                } catch (NumberFormatException e) {
                    logger.error("Could not parse score string to float");
                }
                tmp.setScore(score);

                results.add(tmp);
            }

        }
        return results;
    }

    @Override
    public IndexingTask createAddIndexTask(Pattern pattern) {
        return new SolrAddUpdatePatternTask(pattern);
    }

    @Override
    public IndexingTask createUpdateIndexTask(Pattern pattern) {
        return new SolrAddUpdatePatternTask(pattern);
    }

    @Override
    public IndexingTask createDeleteIndexTask(long id) {
        return new SolrDeletePatternTask(id);
    }

    @Override
    public IndexingTask createBuildIndexTask(Collection<Pattern> patterns) {
        return new SolrCreateIndexTask(patterns);
    }

}
