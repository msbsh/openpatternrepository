package nl.rug.search.opr.controller;

import com.icesoft.faces.component.tree.Tree;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import nl.rug.search.opr.entities.pattern.Category;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.pattern.CategoryLocal;


@ManagedBean(name="browseCtrl")
@RequestScoped
public class BrowsePatternController {

    private static final String EMPTYTREE = "No categories found!";
    private static final String EVENTTYPE = "expand";
    private static final Long ROOT_CATEGORY_ID = new Long(1);

    @EJB
    private CategoryLocal persistence;
    private DefaultTreeModel model;

    public void expandCategory(ActionEvent e) {
        Tree tree = (Tree)e.getSource();

        Node node = (Node) tree.getNavigatedNode();
        if (tree.getNavigationEventType().equals(EVENTTYPE)) {
            PatternTreeObject categoryObject = (PatternTreeObject) node.getUserObject();
            addContent(categoryObject.getCategory(), node);
        }
    }

    public DefaultTreeModel getCategories() {
        if (this.model == null) {
            Category defaultCategory = new Category();
            defaultCategory.setName(EMPTYTREE);

            Node rootNode = createCategoryNode(defaultCategory, true);

            Category rootCategory = persistence.getRootCategory();

            if (rootCategory != null) {
                rootNode = createCategoryNode(rootCategory, true);
            }

            this.model = new DefaultTreeModel(rootNode);
        }

        return model;
    }

    private void addContent(Category parentCategory, Node parentNode) {

        parentNode.removeAllChildren();

        for (Category category : persistence.getChildrenOf(parentCategory)) {

            if(!category.getId().equals(ROOT_CATEGORY_ID)) {
                Node categoryNode = createCategoryNode(category, true);
                parentNode.add(categoryNode);
            }
        }

        // Populate collection by calling size
        parentCategory.getCategoryPatterns().size();

        for (Pattern pattern : parentCategory.getCategoryPatterns()) {
            parentNode.add(createPatternNode(pattern));
        }
    }

    private Node createCategoryNode(Category category, boolean expanded) {
        return createNode(null, category, false, expanded);
    }

    private Node createPatternNode(Pattern pattern) {
        return createNode(pattern, null, true, false);
    }

    private Node createNode(Pattern pattern, Category category, boolean leaf, boolean expanded) {
        Node node = new Node();
        PatternTreeObject nodeObject = new PatternTreeObject(node);

        if (pattern != null) {
            nodeObject.setPattern(pattern);
            nodeObject.setText(pattern.getName());
        }

        if (category != null) {
            nodeObject.setCategory(category);
            nodeObject.setText(category.getName());
        }

        nodeObject.setLeaf(leaf);
        nodeObject.setExpanded(expanded);

        node.setUserObject(nodeObject);

        if (expanded) {
            addContent(category, node);
        }

        return node;
    }

    private class Node extends DefaultMutableTreeNode {
    }
}
































