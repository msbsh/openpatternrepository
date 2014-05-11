package nl.rug.search.opr.controller;


import javax.swing.tree.DefaultMutableTreeNode;
import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.entities.pattern.Pattern;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @date 22.10.2009
 */
public class PatternTreeObject extends NodeUserObject {

    private Pattern pattern;
    private Category category;

    public PatternTreeObject(DefaultMutableTreeNode defaultMutableTreeNode) {
        super(defaultMutableTreeNode);

        this.pattern  = null;
        this.category = null;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
