
package nl.rug.search.opr.search.lexer;

/**
 *
 * @author cm
 */
public interface Lexer {
    
    Token yylex() throws java.io.IOException;
    
}
