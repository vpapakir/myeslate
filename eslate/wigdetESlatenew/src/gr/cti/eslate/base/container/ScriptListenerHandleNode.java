package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/* This is a tree node in the script listener hierarchy, which is used to
 * store intermmediate handles.
 */
public class ScriptListenerHandleNode implements MutableTreeNode, ScriptListenerTreeNode {
    ESlateHandle handle = null;
    ArrayList childHandles = new ArrayList();
    ArrayList childListeners = new ArrayList();
    ArrayList childMethodNodes = new ArrayList();
    ArrayList childUINodes = new ArrayList();
    MutableTreeNode parent = null;
    private String nodeName = ""; //This is the user object of the node
    boolean displayListenerNodes = false;

    public ScriptListenerHandleNode(String name) {
        nodeName = name;
    }

    public ScriptListenerHandleNode(ESlateHandle handle) {
        nodeName = handle.getComponentName();
        this.handle = handle;
    }

    public void add(ScriptListenerHandleNode node) {
        String nodeName = node.getNodeName();
        if (nodeName == null || nodeName.trim().length() == 0)
            throw new RuntimeException("Cannot add a node without a name");

        int index = 0;
        for (int i=0; i<childHandles.size(); i++) {
            if (nodeName.compareTo( ((ScriptListenerHandleNode) childHandles.get(i)).getNodeName()) > 0)
                index++;
            else
                break;
        }
        childHandles.add(index, node);
        node.parent = this;
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
        int index = childHandles.indexOf(node);
        if (index != -1) {
            ScriptListenerHandleNode nd = (ScriptListenerHandleNode) childHandles.remove(index);
            nd.clear();
        }else{
            index = childMethodNodes.indexOf(node);
            if (index != -1) {
                ScriptListenerMethodNode nd = (ScriptListenerMethodNode) childMethodNodes.remove(index);
                nd.clear();
            }else{
                index = childUINodes.indexOf(node);
                if (index != -1) {
                    ScriptListenerUINode nd = (ScriptListenerUINode) childUINodes.remove(index);
                    nd.clear();
                }else{
                    index = childListeners.indexOf(node);
                    if (index != -1) {
                        ScriptListenerNode nd = (ScriptListenerNode) childListeners.remove(index);
                        nd.clear();
                    }
                }
            }
        }
    }

    public ScriptListenerHandleNode[] getHandleNodes() {
        ScriptListenerHandleNode[] handleNodes = new ScriptListenerHandleNode[childHandles.size()];
        for (int i=0; i<handleNodes.length; i++)
            handleNodes[i] = (ScriptListenerHandleNode) childHandles.get(i);
        return handleNodes;
    }

    public ESlateHandle getHandle() {
        return handle;
    }

    public int getHandleNodeCount() {
        return childHandles.size();
    }

    /** Returns the child node for the supplied handle, if exists.
     */
    public ScriptListenerHandleNode getHandleNode(ESlateHandle handle, boolean checkImmediateChildrenOnly) {
        for (int i=0; i<childHandles.size(); i++) {
            ScriptListenerHandleNode handleNode = (ScriptListenerHandleNode) childHandles.get(i);
//System.out.println("ScriptListenerHandleNode getHandleNode(" + handle + ") checking: " + handleNode.handle + ", equals: " + (handleNode.handle == handle));
            if (handleNode.handle == handle)
                return handleNode;
        }
        if (checkImmediateChildrenOnly)
            return null;
        else{
            for (int i=0; i<childHandles.size(); i++) {
                ScriptListenerHandleNode node = ((ScriptListenerHandleNode) childHandles.get(i)).getHandleNode(handle, checkImmediateChildrenOnly);
                if (node != null)
                    return node;
            }
        }
        return null;
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

    /** Returns the child method node for the supplied method name, if exists.
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

    /** Returns the child UI node for the supplied component name, if exists.
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

    public boolean hasChild(ESlateHandle handle) {
        ScriptListenerHandleNode[] handleNodes = getHandleNodes();
        for (int i=0; i<handleNodes.length; i++) {
            if (handleNodes[i].handle == handle)
                return true;
        }
        return false;
    }

    public boolean hasChild(String name) {
        ScriptListenerMethodNode[] methodNodes = getMethodNodes();
        for (int i=0; i<methodNodes.length; i++) {
            if (methodNodes[i].methodName.equals(name))
                return true;
        }
        ScriptListenerUINode[] uiNodes = getUINodes();
        for (int i=0; i<uiNodes.length; i++) {
            if (uiNodes[i].compoName.equals(name))
                return true;
        }
        return false;
    }

    public Enumeration children() {
        return new Enumeration() {
            int currentPos = 0;
            int handleCount = childHandles.size();
            int methodNodeCount = childMethodNodes.size();
            int uiNodeCount = childUINodes.size();
            int handleMethodNodeCount = handleCount + methodNodeCount;
            int handleMethodUINodeCount = handleMethodNodeCount + uiNodeCount;
            int total = (displayListenerNodes)? handleMethodUINodeCount : handleMethodUINodeCount+childListeners.size();

            public boolean hasMoreElements() {
                return (currentPos < total);
            }
            public Object nextElement() {
                Object result = null;
                if (currentPos < handleCount)
                    result = childHandles.get(currentPos);
                else if (currentPos >= handleCount && currentPos < handleMethodNodeCount)
                    result = childHandles.get(currentPos-handleCount);
                else if (currentPos >= handleMethodNodeCount && currentPos < handleMethodUINodeCount)
                    result = childHandles.get(currentPos-handleMethodNodeCount);
                else
                    result = childListeners.get(currentPos-handleMethodUINodeCount);
                currentPos++;
                return result;
            }
        };
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public TreeNode getChildAt(int childIndex) {
        int handleCount = childHandles.size();
        int methodNodeCount = childMethodNodes.size();
        int uiNodeCount = childUINodes.size();
        int handleMethodNodeCount = handleCount + methodNodeCount;
        int handleMethodUINodeCount = handleMethodNodeCount + uiNodeCount;

        if (childIndex < handleCount)
            return (TreeNode) childHandles.get(childIndex);
        else if (childIndex >= handleCount && childIndex < handleMethodNodeCount)
            return (TreeNode) childMethodNodes.get(childIndex-handleCount);
        else if (childIndex >= handleMethodNodeCount && childIndex < handleMethodUINodeCount)
            return (TreeNode) childUINodes.get(childIndex-handleMethodNodeCount);
        else
            return (TreeNode) childListeners.get(childIndex-handleMethodUINodeCount);
    }

    public int getChildCount() {
        if (displayListenerNodes)
            return childHandles.size() + childMethodNodes.size() + childUINodes.size() + childListeners.size();
        else
            return childHandles.size() + childMethodNodes.size() + childUINodes.size();
    }

    public int getIndex(TreeNode node) {
        int index = childHandles.indexOf(node);
        if (index == -1) {
            index = childMethodNodes.indexOf(node);
            if (index == -1) {
                index = childUINodes.indexOf(node);
                if (index == -1) {
                    index = childListeners.indexOf(node);
                    if (index != -1)
                        index = index + childHandles.size() + childMethodNodes.size() + childUINodes.size();
                }else
                    index = index + childHandles.size() + childMethodNodes.size();
            }else
                index = index + childHandles.size();
        }

        return index;
    }

    public TreeNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return (getChildCount() == 0);
    }

    public void insert(MutableTreeNode child, int index) {
        if (ScriptListenerHandleNode.class.isAssignableFrom(child.getClass()))
            childHandles.add((ScriptListenerHandleNode) child);
        else if (ScriptListenerMethodNode.class.isAssignableFrom(child.getClass()))
            childMethodNodes.add((ScriptListenerMethodNode) child);
        else if (ScriptListenerUINode.class.isAssignableFrom(child.getClass()))
            childUINodes.add((ScriptListenerUINode) child);
        else if (ScriptListenerNode.class.isAssignableFrom(child.getClass()))
            childListeners.add((ScriptListenerNode) child);
        else
            throw new RuntimeException("Unable to insert node. Unknown node type");
    }

    public void remove(int index) {
        int handleCount = childHandles.size();
        int methodNodeCount = childMethodNodes.size();
        int uiNodeCount = childUINodes.size();
        int handleMethodNodeCount = handleCount + methodNodeCount;
        int handleMethodUINodeCount = handleMethodNodeCount + uiNodeCount;

        if (index < handleCount) {
            ScriptListenerHandleNode node = (ScriptListenerHandleNode) childHandles.remove(index);
            node.clear();
        }else if (index >= handleCount && index < handleMethodNodeCount) {
            ScriptListenerMethodNode node = (ScriptListenerMethodNode) childMethodNodes.remove(index-handleCount);
            node.clear();
        }else if (index >= handleMethodNodeCount && index < handleMethodUINodeCount) {
            ScriptListenerUINode node = (ScriptListenerUINode) childUINodes.remove(index-handleMethodNodeCount);
            node.clear();
        }else{
            ScriptListenerNode node = (ScriptListenerNode) childListeners.remove(index-handleMethodUINodeCount);
            node.clear();
        }
    }

    public void remove(MutableTreeNode node) {
        remove((TreeNode) node);
    }

    public void removeFromParent() {
        if (parent != null)
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
        for (int i=0; i<childHandles.size(); i++)
            ((ScriptListenerHandleNode) childHandles.get(i)).clear();
        childHandles.clear();
        childListeners.clear();
        childMethodNodes.clear();
        childUINodes.clear();
        parent = null;
        nodeName = null;
        handle = null;
    }
}