package nl.rug.search.opr.search.api;

import nl.rug.search.opr.search.api.expressions.CategorySentence;
import nl.rug.search.opr.search.api.expressions.ImpactSentence;
import nl.rug.search.opr.search.api.expressions.LimitRefinement;
import nl.rug.search.opr.search.api.expressions.FullTextSentence;
import nl.rug.search.opr.search.api.expressions.KeywordSentence;
import nl.rug.search.opr.search.api.expressions.RelationSentence;

/**
 *
 * @author cm
 */
public interface QueryBuilder {

    void process(CategorySentence s);

    void process(KeywordSentence s);

    void process(ImpactSentence s);

    void process(FullTextSentence s);

    void process(ConjunctionSentence s);

    void process(RelationSentence s);

    void process(LimitRefinement lr);

    void process(SubQuery sq);

    void process(SearchQuery searchquery);

    void process(Queryable queryable);

    Object getQuery();

    
}
