package gr.cti.eslate.base.container;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/* This is a tree node in the script listener hierarchy, which is used to
 * store intermmediate methods through which java objects are accessed.
 */
public class ScriptListenerUINode implements MutableTreeNode, ScriptListenerTreeNode {
    String compoName = null;
    ArrayList childListeners = new ArrayList();
    ArrayList childMethodNodes = new ArrayList();
    ArrayList childUINodes = new ArrayList();
    MutableTreeNode parent = null;
    private String nodeName = ""; //This is the user object of the node
    boolean displayListenerNodes = false;

    public ScriptListenerUINode(String compoName) {
        this.compoName = compoName;
        nodeName = compoName;
    }

    public void add(ScriptListenerMethodNode node) {
        String nodeName = node.getNodeName();
        if (nodeName == null || nodeName.trim().length() == 0)
            throw new RuntimeException("Cannot add a node without a name");

        int index = 0;
        for (int i=0; i<childMethodNodes.size(); i++) {
            if (nodeName.compareTo( ((ScriptListenerMethodNode) childMethodNodes.get(i)).getNodeName()) > 0)
                index++;
            else
                break;
        }
        childMethodNodes.add(index, node);
        node.parent = this;
    }

    public void add(ScriptListenerUINode node) {
        String nodeName = node.getNodeName();
        if (nodeName == null || nodeName.trim().length() == 0)
            throw new RuntimeException("Cannot add a node without a name");

        int index = 0;
        for (int i=0; i<childUINodes.size(); i++) {
            if (nodeName.compareTo( ((ScriptListenerUINode) childUINodes.get(i)).getNodeName()) > 0)
                index++;
            else
                break;
        }
        childUINodes.add(index, node);
        node.parent = this;
    }

    public void add(ScriptListenerNode node) {
        String nodeName = node.getNodeName();
        if (nodeName == null || nodeName.trim().length() == 0)
            throw new RuntimeException("Cannot add a node without a name");

        int index = 0;
        for (int i=0; i<childListeners.size(); i++) {
            if (nodeName.compareTo( ((ScriptListenerNode) childListeners.get(i)).getNodeName()) > 0)
                index++;
            else
                break;
        }
        childListeners.add(index, node);
        node.parent = this;
    }

    public void remove(TreeNode node) {
        int index = childMethodNodes.indexOf(node);
        if (index != -1) {
            ScriptListenerMethodNode nd = (ScriptListenerMethodNode) childMethodNodes.remove(index);
            nd.clear();
            return;
        }else{
            index = childUINodes.indexOf(node);
            if (index != -1) {
                ScriptListenerUINode nd = (ScriptListenerUINode) childUINodes.remove(index);
                nd.clear();
                return;
            }else{
                index = childListeners.indexOf(node);
                if (index != -1) {
                    ScriptListenerNode nd = (ScriptListenerNode) childListeners.remove(index);
                    nd.clear();
                }
            }
        }
    }

    public ScriptListenerMethodNode[] getMethodNodes() {
        ScriptListenerMethodNode[] methodNodes = new ScriptListenerMethodNode[childMethodNodes.size()];
        for (int i=0; i<methodNodes.length; i++)
            methodNodes[i] = (ScriptListenerMethodNode) childMethodNodes.get(i);
        return methodNodes;
    }

    public int getMethodNodeCount() {
        return childMethodNodes.size();
    }

    /** Returns the child node for the supplied handle, if exists.
     */
    public ScriptListenerMethodNode getMethodNode(String methodName) {
        for (int i=0; i<childMethodNodes.size(); i++) {
            ScriptListenerMethodNode methodNode = (ScriptListenerMethodNode) childMethodNodes.get(i);
            if (methodNode.methodName.equals(methodName))
                return methodNode;
        }
        return null;
    }

    public ScriptListenerUINode[] getUINodes() {
        ScriptListenerUINode[] uiNodes = new ScriptListenerUINode[childUINodes.size()];
        for (int i=0; i<uiNodes.length; i++)
            uiNodes[i] = (ScriptListenerUINode) childUINodes.get(i);
        return uiNodes;
    }

    public int getUINodeCount() {
        return childUINodes.size();
    }

    /** Returns the child node for the supplied handle, if exists.
     */
    public ScriptListenerUINode getUINode(String compoName) {
        for (int i=0; i<childUINodes.size(); i++) {
            ScriptListenerUINode uiNode = (ScriptListenerUINode) childUINodes.get(i);
            if (uiNode.compoName.equals(compoName))
                return uiNode;
        }
        return null;
    }

    public ScriptListenerNode[] getScriptListenerNodes() {
        ScriptListenerNode[] listenerNodes = new ScriptListenerNode[childListeners.size()];
        for (int i=0; i<childListeners.size(); i++)
            listenerNodes[i] = (ScriptListenerNode) childListeners.get(i);
        return listenerNodes;
    }

    public int getListenerNodeCount() {
        return childListeners.size();
    }

    public boolean hasChild(String name) {
        ScriptListenerMethodNode[] methodNodes = getMethodNodes();
        for (int i=0; i<methodNodes.length; i++) {
            if (methodNodes[i].methodName == name)
                return true;
        }
        ScriptListenerUINode[] uiNodes = getUINodes();
        for (int i=0; i<uiNodes.length; i++) {
            if (uiNodes[i].compoName == name)
                return true;
        }
        return false;
    }

    public Enumeration children() {
        return new Enumeration() {
            int currentPos = 0;
            int methodNodeCount = childMethodNodes.size();
            int uiNodeCount = childUINodes.size();
            int uiMethodNodeCount = methodNodeCount + uiNodeCount;
            int total = (displayListenerNodes)? uiMethodNodeCount : uiMethodNodeCount+childListeners.size();

            public boolean hasMoreElements() {
                return (currentPos < total);
            }
            public Object nextElement() {
                Object result = null;
                if (currentPos < methodNodeCount)
                    result = childMethodNodes.get(currentPos);
                else if (currentPos >= methodNodeCount && currentPos < uiMethodNodeCount)
                    result = childUINodes.get(currentPos-methodNodeCount);
                else
                    result = childListeners.get(currentPos-uiMethodNodeCount);
                currentPos++;
                return result;
            }
        };
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public TreeNode getChildAt(int childIndex) {
        int methodNodeCount = childMethodNodes.size();
        int uiNodeCount = childUINodes.size();
        if (childIndex < methodNodeCount)
            return (TreeNode) childMethodNodes.get(childIndex);
        else if (childIndex >= methodNodeCount && childIndex < methodNodeCount + uiNodeCount)
            return (TreeNode) childUINodes.get(childIndex-methodNodeCount);
        else
            return (TreeNode) childListeners.get(childIndex-methodNodeCount-uiNodeCount);
    }

    public int getChildCount() {
        if (displayListenerNodes)
            return childMethodNodes.size() + childUINodes.size() + childListeners.size();
        else
            return childMethodNodes.size() + childUINodes.size();
    }

    public int getIndex(TreeNode node) {
        int index = childMethodNodes.indexOf(node);
        if (index == -1) {
            index = childUINodes.indexOf(node);
            if (index == -1) {
                index = childListeners.indexOf(node);
                if (index != -1)
                    index = index + childMethodNodes.size() + childUINodes.size();
            }else
                index = index + childMethodNodes.size();
        }
        return index;
    }

    public TreeNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return (getChildCount() == 0);
    }

    // This is wrong. Insertion with index is not supported properly in this tree model
    public void insert(MutableTreeNode child, int index) {
        if (ScriptListenerMethodNode.class.isAssignableFrom(child.getClass()))
            childMethodNodes.add((ScriptListenerMethodNode) child);
        else if (ScriptListenerUINode.class.isAssignableFrom(child.getClass()))
            childUINodes.add((ScriptListenerUINode) child);
        else if (ScriptListenerNode.class.isAssignableFrom(child.getClass()))
            childListeners.add((ScriptListenerNode) child);
    }

    public void remove(int index) {
        int methodNodeCount = childMethodNodes.size();
        int uiNodeCount = childUINodes.size();
        if (index < methodNodeCount) {
            ScriptListenerMethodNode nd = (ScriptListenerMethodNode) childMethodNodes.remove(index);
            nd.clear();
        }else if (index >= methodNodeCount && index < (methodNodeCount + uiNodeCount)) {
            ScriptListenerUINode nd = (ScriptListenerUINode) childUINodes.remove(index-methodNodeCount);
            nd.clear();
        }else{
            ScriptListenerNode nd = (ScriptListenerNode) childListeners.remove(index-methodNodeCount-uiNodeCount);
            nd.clear();
        }
    }

    public void remove(MutableTreeNode node) {
        remove((TreeNode) node);
    }

    public void removeFromParent() {
        parent.remove(this);
    }

    public void setParent(MutableTreeNode newParent) {
        parent = newParent;
    }

    public void setUserObject(Object object) {
        nodeName = (String) object;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void clear() {
        for (int i=0; i<childListeners.size(); i++)
            ((ScriptListenerNode) childListeners.get(i)).clear();
        for (int i=0; i<childMethodNodes.size(); i++)
            ((ScriptListenerMethodNode) childMethodNodes.get(i)).clear();
        for (int i=0; i<childUINodes.size(); i++)
            ((ScriptListenerUINode) childUINodes.get(i)).clear();
        childListeners.clear();
        childMethodNodes.clear();
        childUINodes.clear();
        parent = null;
        nodeName = null;
        compoName = null;
    }
}