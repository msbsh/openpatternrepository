package nl.rug.search.opr.search.solr;

import nl.rug.search.opr.entities.pattern.relation.Alternative;
import nl.rug.search.opr.entities.pattern.relation.Combination;
import nl.rug.search.opr.entities.pattern.relation.RelationshipType;
import nl.rug.search.opr.entities.pattern.relation.Variant;
import java.util.Iterator;
import nl.rug.search.opr.entities.pattern.Indicator;
import nl.rug.search.opr.search.api.Conjunction;
import nl.rug.search.opr.search.api.ConjunctionSentence;
import nl.rug.search.opr.search.api.Qualifier;
import nl.rug.search.opr.search.api.expressions.FullTextSentence;
import nl.rug.search.opr.search.api.expressions.LimitRefinement;
import nl.rug.search.opr.search.api.QueryBuilder;
import nl.rug.search.opr.search.api.Queryable;
import nl.rug.search.opr.search.api.SearchQuery;
import nl.rug.search.opr.search.api.expressions.CategorySentence;
import nl.rug.search.opr.search.api.Expression;
import nl.rug.search.opr.search.api.Sentence;
import nl.rug.search.opr.search.api.expressions.ImpactSentence;
import nl.rug.search.opr.search.api.expressions.KeywordSentence;
import nl.rug.search.opr.search.api.SubQuery;
import nl.rug.search.opr.search.api.expressions.RelationSentence;
import org.apache.solr.client.solrj.SolrQuery;
import static org.apache.solr.client.solrj.util.ClientUtils.escapeQueryChars;

/**
 *
 * @author cm
 */
class SolrQueryBuilder implements QueryBuilder {

    public static final int MAX_ALLOWED_TIME = 1000;
    private SolrQuery query;
    private int row = 10;
    private int start = 0;
    StringBuilder queryString = new StringBuilder();
    private static final String FIELDDELIMITER = ":";
    private static final String EXPDELIMITER = " ";
    private static final String FIELD_CATEGORY = "category";
    private static final String SUBQUERY_OPEN = "(";
    private static final String SUBQUERY_CLOSE = ")";

    public SolrQueryBuilder() {
        query = new SolrQuery();
    }

    public void process(CategorySentence s) {
        if (s.getCategory() != null) {
            queryString.append(printQ(s.getQualifier()) + FIELD_CATEGORY + FIELDDELIMITER + s.getCategory().getId());
        }
    }

    public void process(KeywordSentence s) {
        queryString.append(printQ(s.getQualifier()) + s.getSearchArea() + FIELDDELIMITER + escapeQueryChars(s.getKeyword()));
    }

    public void process(ImpactSentence s) {
        queryString.append(printQ(s.getQualifier()) + printI(s.getIndicator()) + FIELDDELIMITER + s.getQualityAttribute());
    }

    public void process(RelationSentence s) {
        queryString.append(printQ(s.getQualifier()) + printR(s.getType()) + "id" + FIELDDELIMITER + s.getPattern().getId());
    }

    public void process(LimitRefinement lr) {
        row = lr.getLimit();
    }

    public void process(FullTextSentence s) {
        String text = escapeQueryChars(s.getText());

        text = allowWildcard(text);
        text = allowAnychar(text);
        text = allowFuzzy(text);

        queryString.append(printQ(s.getQualifier()) + text);
    }

    public void process(SearchQuery sq) {

        row = sq.getLimit();
        start = sq.getOffset();

        for (Iterator<Expression> it = sq.getElements().iterator(); it.hasNext();) {
            it.next().visit(this);
            if (it.hasNext()) {
                queryString.append(EXPDELIMITER);
            }
        }
    }

    public void process(SubQuery sq) {
        queryString.append(printC(sq.getConjunction()) + SUBQUERY_OPEN);
        for (Iterator<Sentence> it = sq.getElements().iterator(); it.hasNext();) {
            it.next().visit(this);
            if (it.hasNext()) {
                queryString.append(EXPDELIMITER);
            }
        }
        queryString.append(SUBQUERY_CLOSE);
    }

    public void process(ConjunctionSentence s) {
        queryString.append(s.getConjunction() + EXPDELIMITER);
        s.getSentence().visit(this);
    }

    public SolrQuery getQuery() {

        query = new SolrQuery(queryString.toString());
        query.setRows(row);
        query.setStart(start);
        query.addField("*,score");
        query.addField("stats=true");
        query.addField("stats.field=id");
        query.addHighlightField("text");
        query.setHighlight(true);
        query.setHighlightFragsize(200);
        query.setHighlightSnippets(3);
        query.setTimeAllowed(MAX_ALLOWED_TIME);

        return query;
    }

    public void process(Queryable queryable) {
        /* ignore */
    }

    private static String printQ(Qualifier q) {
        switch (q) {
            case DONOTHAVE:
                return "-";
            case HAVE:
                return "+";
            case MAYHAVE:
            default:
                return "";
        }
    }

    private static String printC(Conjunction c) {
        if (c == null) {
            return "";
        }
        switch (c) {
            case AND:
                return "&&";
            case OR:
                return "||";
            case NOT:
                return "!";
            default:
                return "";
        }
    }

    private static String printI(Indicator i) {
        if (i == null) {
            return "";
        }
        return i.toString();
    }

    private static String printR(RelationshipType t) {
        if (t instanceof Combination) {
            return "combination";
        }
        if (t instanceof Alternative) {
            return "alternative";
        }
        if (t instanceof Variant) {
            return "variant";
        }
        return "relationship";
    }

    private static String allowWildcard(String original) {
        original = original.replace("\\*", "*");
        return original;
    }

    private static String allowAnychar(String original) {
        original = original.replace("\\?", "?");
        return original;
    }

    private static String allowFuzzy(String original) {
        original = original.replace("\\~", "~");
        return original;
    }
}
