/*
Build instructions:

    Type the command:
    byaccj -vd -Jpackage=nl.rug.search.opr.search.parser -Jclass=SQParser -Jimplements=Parser SQParser.y

    IMPORTANT:
    Add the keyword public to the yyparse() method to be conform to the interface.
    I haven't found a directive/argument to do that automatically :-(
*/


%{
import nl.rug.search.opr.search.lexer.Lexer;
import nl.rug.search.opr.search.lexer.Token;
import java.util.logging.Logger;
import nl.rug.search.opr.search.api.*;
import nl.rug.search.opr.search.api.expressions.*;
%}

%token WORD AND OR NOT

%%

query   : WORD elements     {
                                SearchQuery query = new SearchQuery( new FullTextSentence( ((Token)$1.obj).getAux()) );
                                query.addList( ((Linked<Expression>)$2.obj).getList() );
                                $$.obj = query;
                            }
        | WORD              {   $$.obj = new SearchQuery( new FullTextSentence( ((Token)$1.obj).getAux()) ); }
        ;


element : WORD { $$.obj = new FullTextSentence( ((Token)$1.obj).getAux()); }
        | conjunction WORD { $$.obj = new ConjunctionSentence((Conjunction)$1.obj, new FullTextSentence(((Token)$2.obj).getAux())); }
        ;

conjunction : AND   {  $$.obj = Conjunction.AND; }
            | OR    {  $$.obj = Conjunction.OR; }
            | NOT   {  $$.obj = Conjunction.NOT; }
            ;

elements : element nextelement { $$.obj = new Linked<Expression>( (Expression)$1.obj  , (Linked<Expression>)$2.obj ); }
         | error nextelement   { $$.obj = $2.obj; }
         ;


nextelement : elements { $$.obj = $1.obj;}
            | {$$.obj = null;}
            ;


%%

private Lexer zzlexer;

public SQParser(Lexer lexer) {
    this(lexer,false);
}

public SQParser(Lexer lexer, boolean debug) {
    this.zzlexer = lexer;
    yydebug = debug;
}

public SearchQuery getQuery() {
    if (yyval != null && yyval.obj instanceof SearchQuery) {
        return (SearchQuery)yyval.obj;
    }
    return null;
}

int yylex() {

    int yy_return = 0;
    this.yylval   = null;
    Token token = null;

    try {
        token = zzlexer.yylex();
    } catch (java.io.IOException ex) {
        yyerror(ex.getMessage());
        return -1;
    }

    if (token!=null) {
        this.yylval = new SQParserVal(token);
        yy_return = token.getToken();
    }
    if (yydebug) {
        System.out.println("Received token: " + yyname[yy_return]);
    }
    return yy_return;
}

void yywarning(String msg) {
    Logger.getLogger(SQParser.class.getName()).fine("Parse Warning: " +msg);
}

void yyerror(String msg) {
    Token token = (Token)yylval.obj;
    Logger.getLogger(SQParser.class.getName()).info("Parse Error: " +msg + " (" + yyname[token.getToken()] + ")");

}
