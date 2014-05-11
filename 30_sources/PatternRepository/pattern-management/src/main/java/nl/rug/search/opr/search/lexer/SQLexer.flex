package nl.rug.search.opr.search.lexer;

%%

%public
%implements Lexer
%class SQLexer

%type Token

%{
private StringBuilder pcdata = new StringBuilder();
private Token Symbol(int token) {
    return new Token(token);
}

private Token Symbol(int token, String aux) {
    return new Token(token, aux);
}

private String flush(StringBuilder buffer) {
    String result = buffer.toString();
    buffer.delete(0,buffer.length());
    return result;
}

%}

OR     = ([Oo][Rr]" ")|("||")
AND    = ([Aa][Nn][Dd]" ")|("&&")
NOT    = ([Nn][Oo][Tt]" ")|("!")
SINGLEQUOTE = "'"
DOUBLEQUOTE = "\""
DELIMETER = " "
%states WORD,SQUOTE,DQUOTE

%%

<YYINITIAL>	{
    {AND}               { return Symbol(Token.AND); }
    {OR}                { return Symbol(Token.OR); }
    {NOT}               { return Symbol(Token.NOT); }
    {SINGLEQUOTE}       { yybegin(SQUOTE);}
    {DOUBLEQUOTE}       { yybegin(DQUOTE);}
    {DELIMETER}         { /* ignore*/ }
    .                   {   yypushback(1);
                            yybegin(WORD);
                        }
}

<<EOF>>                 { return null; }

<WORD> {
    {DELIMETER}         {
                            yybegin(YYINITIAL);
                            return Symbol(Token.WORD,flush(pcdata));
                        }
    .                   { pcdata.append(yytext());}
    <<EOF>>             {
                            yybegin(YYINITIAL);
                            return Symbol(Token.WORD,flush(pcdata));
                        }
}

<SQUOTE> {
    {SINGLEQUOTE}       {
                            yybegin(YYINITIAL);
                            return Symbol(Token.WORD,flush(pcdata));
                        }
    .                   { pcdata.append(yytext());}
    <<EOF>>             {
                            yybegin(YYINITIAL);
                            return Symbol(Token.WORD,flush(pcdata));
                        }
}

<DQUOTE> {
    {DOUBLEQUOTE}       {
                            yybegin(YYINITIAL);
                            return Symbol(Token.WORD,flush(pcdata));
                        }
    .                   { pcdata.append(yytext());}
    <<EOF>>             {
                            yybegin(YYINITIAL);
                            return Symbol(Token.WORD,flush(pcdata));
                        }
}
