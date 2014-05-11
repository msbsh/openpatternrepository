
package nl.rug.search.opr.search.api.expressions;

import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.search.api.*;

/**
 *
 * @author cm
 */
public class CategorySentence extends Sentence {

    private Category category;
    
    public CategorySentence(Qualifier q, Category category) {
        super(q);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }    

    @Override
    public String toString() {
        return getQualifier() + " the category " + category;
    }

    @Override
    public void visit(QueryBuilder builder) {
        builder.process(this);
    }

}
