
package nl.rug.search.opr.search.parser;

import nl.rug.search.opr.search.api.SearchQuery;

/**
 *
 * @author cm
 */
public interface Parser {

    int yyparse();
    SearchQuery getQuery();

}
