package nl.rug.search.opr.webservices.types;

import nl.rug.search.opr.entities.pattern.TextBlock;
import nl.rug.search.opr.entities.template.Type;

/**
 *
 * @author cm
 */
public class Content {

    private String name;
    private String text;
    private int sort;
    private Type type;

    public Content() {};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static Content assemble(TextBlock tb) {

        if (tb==null) return new Content();

        Content c = new Content();
        c.setName(tb.getComponent().getName());
        c.setText(tb.getText());
        c.setSort(tb.getComponent().getOrder());
        c.setType(tb.getComponent().getType());

        return c;
    }
    
}
