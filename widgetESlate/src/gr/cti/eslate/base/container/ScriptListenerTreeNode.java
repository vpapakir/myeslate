package gr.cti.eslate.base.container;

import javax.swing.tree.TreeNode;

/** This interface is implemented by the 3 types of nodes (there are 4 node types. This
 *  i/f is implemented by all except the leaf nodes, which are the nodes for the actual
 *  script listeners) which are used in the tree that contains the script listeners of
 *  a microworld. It is used to assure some common functionality among the 4 node types
 *  (ScriptListenerHandleNode, ScriptListenerUINode, ScriptListenerMethodNode).
 */
public interface ScriptListenerTreeNode {
    public ScriptListenerNode[] getScriptListenerNodes();
    public ScriptListenerMethodNode[] getMethodNodes();
    public ScriptListenerUINode[] getUINodes();
    public void remove(TreeNode node);
    public String getNodeName();
}