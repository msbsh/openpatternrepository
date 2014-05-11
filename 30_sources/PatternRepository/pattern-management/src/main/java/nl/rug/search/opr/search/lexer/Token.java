
package nl.rug.search.opr.search.lexer;

import nl.rug.search.opr.search.parser.SQParserTokens;

/**
 *
 * @author cm
 */
public class Token  implements SQParserTokens {


    private int token;
    private String aux;

    Token(int token) {
        this.token = token;
    }

    Token(int token, String aux) {
        this.token = token;
        this.aux = aux;
    }

    public String getAux() {
        return aux;
    }

    public void setAux(String aux) {
        this.aux = aux;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }



}
