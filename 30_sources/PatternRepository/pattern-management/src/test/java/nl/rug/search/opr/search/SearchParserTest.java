package nl.rug.search.opr.search;

import java.io.StringReader;
import nl.rug.search.opr.search.parser.SQParser;
import nl.rug.search.opr.search.lexer.SQLexer;
import org.junit.Assert;
import nl.rug.search.opr.search.api.Conjunction;
import javax.naming.NamingException;
import nl.rug.search.opr.search.api.ConjunctionSentence;
import nl.rug.search.opr.search.api.Expression;
import nl.rug.search.opr.search.api.Qualifier;
import nl.rug.search.opr.search.api.SearchQuery;
import nl.rug.search.opr.search.api.Sentence;
import nl.rug.search.opr.search.parser.Parser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lydia Wall
 */
public class SearchParserTest {
    
    public SearchParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws NamingException {

    }

    @After
    public void tearDown() throws NamingException {
    }


//########### createSearchQuery Tests ################
    private SearchQuery createSearchQuery(String searchString) throws QueryParseException{
        Parser p = new SQParser(new SQLexer(new StringReader(searchString)));
        if ( p.yyparse() != 0) {
            throw new QueryParseException("Could not parse query string "+ searchString);
        }
        return p.getQuery();
    }
    
    @Test
    public void canCreateSearchQuery() throws QueryParseException {
        String searchString = "layer";
        SearchQuery query = createSearchQuery(searchString);
        assertNotNull(query);
    }

    @Test
    public void canCreateStartQuery() throws QueryParseException{
        String searchString = "*";
        SearchQuery query = createSearchQuery(searchString);
        assertEquals(1, query.getElements().size());
    }
    
    @Test(expected = QueryParseException.class)
    public void throwQueryParseExceptionAND() throws QueryParseException {
        createSearchQuery("AND layer");
    }

    @Test(expected = QueryParseException.class)
    public void throwNoQueryParseExceptionAnd() throws QueryParseException {
        createSearchQuery("and layer");
    }

    @Test(expected = QueryParseException.class)
    public void throwQueryParseExceptionOR() throws QueryParseException {
        createSearchQuery("OR layer");
    }

    @Test(expected = QueryParseException.class)
    public void throwNoQueryParseExceptionOr() throws QueryParseException {
        createSearchQuery("or layer");
    }

    @Test(expected = QueryParseException.class)
    public void throwQueryParseExceptionNOT() throws QueryParseException {
        createSearchQuery("NOT layer");
    }

    @Test(expected = QueryParseException.class)
    public void throwNoQueryParseExceptionNot() throws QueryParseException {
        createSearchQuery("not layer");
    }

    @Test
    public void canCreateSearchQueryNOT() throws QueryParseException {
        String searchString = "layer NOT shared";
        SearchQuery query = createSearchQuery(searchString);
        assertEquals(2, query.getElements().size());
        for (Expression ex : query.getElements()) {
            if (ex instanceof Sentence) {
                assertEquals(Qualifier.MAYHAVE, ((Sentence) ex).getQualifier());
            } else if (ex instanceof ConjunctionSentence) {
                assertEquals(Conjunction.NOT, ((ConjunctionSentence) ex).getConjunction());
                assertEquals(Qualifier.MAYHAVE, ((ConjunctionSentence) ex).getSentence().getQualifier());
            }
        }
    }

    @Test
    public void canCreateSearchQueryNot() throws QueryParseException {
        String searchString = "layer not shared";
        SearchQuery query = createSearchQuery(searchString);
        assertEquals(2, query.getElements().size());
        Expression expression = query.getElements().get(0);
        if (!(expression instanceof Sentence)) {
            Assert.fail();
        }
        assertEquals(Qualifier.MAYHAVE, ((Sentence) expression).getQualifier());

        expression = query.getElements().get(1);
        if (!(expression instanceof ConjunctionSentence)) {
            Assert.fail();
        }
        assertEquals(Conjunction.NOT, ((ConjunctionSentence) expression).getConjunction());
        assertEquals(Qualifier.MAYHAVE, ((ConjunctionSentence) expression).getSentence().getQualifier());
    }

    @Test
    public void canCreateSearchQueryAND() throws QueryParseException {
        String searchString = "layer AND shared";
        SearchQuery query = createSearchQuery(searchString);
        assertEquals(2, query.getElements().size());
        for (Expression ex : query.getElements()) {
            if (ex instanceof Sentence) {
                assertEquals(Qualifier.MAYHAVE, ((Sentence) ex).getQualifier());
            } else if (ex instanceof ConjunctionSentence) {
                assertEquals(Conjunction.AND, ((ConjunctionSentence) ex).getConjunction());
                assertEquals(Qualifier.MAYHAVE, ((ConjunctionSentence) ex).getSentence().getQualifier());
            }
        }
    }

    @Test
    public void canCreateSearchQueryAnd() throws QueryParseException {
        String searchString = "layer and shared";
        SearchQuery query = createSearchQuery(searchString);
        assertEquals(2, query.getElements().size());
        Expression expression = query.getElements().get(0);
        if (!(expression instanceof Sentence)) {
            Assert.fail();
        }
        assertEquals(Qualifier.MAYHAVE, ((Sentence) expression).getQualifier());

        expression = query.getElements().get(1);
        if (!(expression instanceof ConjunctionSentence)) {
            Assert.fail();
        }
        assertEquals(Conjunction.AND, ((ConjunctionSentence) expression).getConjunction());
        assertEquals(Qualifier.MAYHAVE, ((ConjunctionSentence) expression).getSentence().getQualifier());
    }

    @Test
    public void canCreateSearchQueryOR() throws QueryParseException {
        String searchString = "layer OR shared";
        SearchQuery query = createSearchQuery(searchString);
        assertEquals(2, query.getElements().size());
        for (Expression ex : query.getElements()) {
            if (ex instanceof Sentence) {
                assertEquals(Qualifier.MAYHAVE, ((Sentence) ex).getQualifier());
            } else if (ex instanceof ConjunctionSentence) {
                assertEquals(Conjunction.OR, ((ConjunctionSentence) ex).getConjunction());
                assertEquals(Qualifier.MAYHAVE, ((ConjunctionSentence) ex).getSentence().getQualifier());
            }
        }
    }

    @Test
    public void canCreateSearchQueryor() throws QueryParseException {
        String searchString = "layer or shared";
        SearchQuery query = createSearchQuery(searchString);
        assertEquals(2, query.getElements().size());
        Expression expression = query.getElements().get(0);
        if (!(expression instanceof Sentence)) {
            Assert.fail();
        }
        assertEquals(Qualifier.MAYHAVE, ((Sentence) expression).getQualifier());

        expression = query.getElements().get(1);
        if (!(expression instanceof ConjunctionSentence)) {
            Assert.fail();
        }
        assertEquals(Conjunction.OR, ((ConjunctionSentence) expression).getConjunction());
        assertEquals(Qualifier.MAYHAVE, ((ConjunctionSentence) expression).getSentence().getQualifier());
    }
}
