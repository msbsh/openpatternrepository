package nl.rug.search.opr.search.api.expressions;

import nl.rug.search.opr.search.api.*;

/**
 *
 * @author cm
 */
public class FullTextSentence extends Sentence {

    private String text;

    public FullTextSentence(Qualifier q) {
        this(q,"");
    }

    public FullTextSentence(String text) {
        this(Qualifier.MAYHAVE, text);
    }

    public FullTextSentence(Qualifier q, String text) {
        super(q);
        setText(text);

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

   

    @Override
    public String toString() {
        String result = "";
        result += getQualifier() + " " + getText();

        return result;

    }

    @Override
    public void visit(QueryBuilder builder) {
        builder.process(this);
    }
}
