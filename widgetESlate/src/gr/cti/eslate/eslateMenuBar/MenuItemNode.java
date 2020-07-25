package gr.cti.eslate.eslateMenuBar;


import javax.swing.tree.*;


public class MenuItemNode extends DefaultMutableTreeNode {

    public MenuItemNode() {
        super();
    }

    public MenuItemNode(Object obj) {
        super(obj);
    }

    public String toString() {
        if (getUserObject() instanceof String) {
            String item = (String) getUserObject();
            return item;
        } else if (getUserObject() instanceof ESlateMenu) {
            ESlateMenu item = (ESlateMenu) getUserObject();
            return item.getText();
        } else if (getUserObject() instanceof RollMenuButton) {
            return null;
        } else if (getUserObject() instanceof ESlateCheckMenuItem) {
            ESlateCheckMenuItem item = (ESlateCheckMenuItem) getUserObject();
            return item.getText();
        } else if (getUserObject() instanceof ESlateMenuItem){
            ESlateMenuItem item = (ESlateMenuItem) getUserObject();
            return item.getText();
        } else
            return "UnIdentified :"+getUserObject();

    }

    /*public TreeNode getChildAt(int i){
     MyDefaultMutableTreeNode node = (MyDefaultMutableTreeNode) getChildAt(i);
     //String text = ((ESlateMenuItem) node.getUserObject()).getText();
     //MyDefaultMutableTreeNode newNode = new MyDefaultMutableTreeNode(text);
     return node;
     } */
}
