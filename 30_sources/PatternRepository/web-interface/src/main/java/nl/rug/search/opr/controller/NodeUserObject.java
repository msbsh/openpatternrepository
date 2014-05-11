package nl.rug.search.opr.controller;

import com.icesoft.faces.component.tree.IceUserObject;
import javax.swing.tree.DefaultMutableTreeNode;

public class NodeUserObject extends IceUserObject {

    public NodeUserObject(DefaultMutableTreeNode defaultMutableTreeNode) {
        super(defaultMutableTreeNode);

        setLeafIcon("li.png");
        setBranchContractedIcon("rightArrow.png");
        setBranchExpandedIcon("downArrow.png");
    }

}
