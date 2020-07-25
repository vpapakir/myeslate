package gr.cti.eslate.base;

import javax.swing.tree.*;
import gr.cti.eslate.base.sharedObject.*;
import java.util.*;

/**
 * This class implements nodes in a Plug tree data structure. Plugs should be
 * added/removed using the methods provided by the Plug class, and not by the
 * add()/remove() methods.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 * @see         gr.cti.eslate.base.sharedObject.SharedObject
 */
class PlugTreeNode extends DefaultMutableTreeNode
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The plug which is associated with the node when the node is not the root
   * of the tree of a component's plugs, or the E-Slate handle which is
   * associated with the node when the node is the root of the tree of a
   * component's plugs.
   */
  private Object myObject;

  /**
   * Constructs a PlugTreeNode.
   * @param     obj     Either a plug, which will be associated with the node,
   *                    or an E-Slate handle, when the node is the root of
   *                    the tree of a component's plugs.
   */
  PlugTreeNode(Object obj)
  {
    super(obj);
    myObject = obj;
  }

  /**
   * Returns the plug associated with this node, or null if the node is the
   * root of the plug view tree.
   * @return    The requested plug.
   */
  Plug getPlug()
  {
    if (myObject instanceof Plug) {
      return (Plug)myObject;
    }else{
      return null;
    }
  }

  /**
   * Returns the E-Slate handle associated with this node or null if the node
   * is not the root of the plug view tree.
   * @return    The requested handle.
   */
  ESlateHandle getHandle()
  {
    if (myObject instanceof ESlateHandle) {
      return (ESlateHandle)myObject;
    }else{
      return null;
    }
  }

  /**
   * Returns the plug which is associated with the node when the node is not
   * the root of the tree of a component's plugs, or the E-Slate handle which
   * is associated with the node when the node is the root of the tree of a
   * component's plugs. 
   * @return    The requested object.
   */
  Object getObject()
  {
    return myObject;
  }

  /**
   * Returns the shared object associated with the plug associated with this
   * node.
   * @return    The shared object associated with the plug associated with this
   *            node.
   */
  SharedObject getSharedObject()
  {
    Plug plug = getPlug();

    if (plug != null) {
      return plug.internals.getSharedObject();
    }else{
      return null;
    }
  }

  /**
   * Returns the parent node.
   * @return    The parent node.
   */
  PlugTreeNode getParentNode()
  {
    return (PlugTreeNode)(super.getParent());
  }

  /**
   * Returns the plug of the parent node. If this node has no parent or it
   * refers to an E-Slate handle, this method returns null.
   * @return    The plug of the parent node.
   */
  Plug getParentPlug()
  {
    PlugTreeNode parentNode = (PlugTreeNode)getParent();
    if (parentNode != null) {
      return parentNode.getPlug();
    }else{
      return null;
    }
  }

  /**
   * Returns the shared object of the parent node. If this node has no parent
   * or it refers to an E-Slate handle, this method returns null.
   * or null if this node has no parent.
   * @return    The shared object of the parent node.
   */
  SharedObject getParentSharedObject()
  {
    PlugTreeNode parentNode = (PlugTreeNode)getParent();
    if (parentNode != null) {
      return parentNode.getPlug().internals.getSharedObject();
    }else{
      return null;
    }
  }

  /**
   * Returns a vector of the node's child plugs or E-Slate handles.
   * @return    The node's child plugs or E-Slate handles.
   */
  @SuppressWarnings(value={"unchecked"})
  public Vector getChildObjects()
  {
    Enumeration childNodes;
    Vector childObjects;
    synchronized(this) {
      childNodes = children();
      childObjects = new Vector(getChildCount());
    }
    while (childNodes.hasMoreElements()) {
      PlugTreeNode node = (PlugTreeNode)(childNodes.nextElement());
      Object o = node.getPlug();
      if (o == null) {
        o = node.getHandle();
      }
      if (o != null) {
        childObjects.addElement(o);
      }
    }
    return childObjects;
  }

  /**
   * Returns a vector of the node's child plugs.
   * @return    The node's child plugs.
   */
  @SuppressWarnings(value={"unchecked"})
  public Vector getChildPlugs()
  {
    Enumeration childNodes;
    Vector childPlugs;
    synchronized(this) {
      childNodes = children();
      childPlugs = new Vector(getChildCount());
    }
    while (childNodes.hasMoreElements()) {
      Plug p = ((PlugTreeNode)(childNodes.nextElement())).getPlug();
      if (p != null) {
        childPlugs.addElement(p);
      }
    }
    return childPlugs;
  }

  /**
   * Returns a vector of the node's child E-Slate handles.
   * @return    The node's child E-Slate handles.
   */
  @SuppressWarnings("unchecked")
  public Vector getChildHandles()
  {
    Enumeration childNodes;
    Vector<ESlateHandle> childHandles;
    synchronized(this) {
      childNodes = children();
      childHandles = new Vector<ESlateHandle>(getChildCount());
    }
    while (childNodes.hasMoreElements()) {
      ESlateHandle h = ((PlugTreeNode)(childNodes.nextElement())).getHandle();
      if (h != null) {
        childHandles.addElement(h);
      }
    }
    return childHandles;
  }

  /**
   * Returns a list of the shared objects associated with the node's
   * child plugs.
   * @return    The shared objects associated with the node's child plugs.
   */
  @SuppressWarnings("unchecked")
  ArrayList<SharedObject> getChildSharedObjects()
  {
    Enumeration childPlugs = getChildPlugs().elements();
    ArrayList<SharedObject> childSharedObjects = new ArrayList<SharedObject>();

    while (childPlugs.hasMoreElements()) {
      childSharedObjects.add(
        ((Plug)childPlugs.nextElement()).internals.getSharedObject()
      );
    }
    return childSharedObjects;
  }

  /**
   * Removes newChild from its parent and makes it a child of this node by
   * adding it to the end of this node's child array.
   * @param     newChild        The node to add as a child of thsi node.
   */
  public void add(PlugTreeNode newChild)
  {
    ESlateHandle h = newChild.getHandle();
    if (h != null) {
      // Node refers to an E-Slate handle. Add it at the end.
      super.add(newChild);
    }else{
      // Node refers to a plug. Add it after the last node referring to a plug
      // and before the first node referring to an E-Slate handle.
      int position = 0;
      int n = getChildCount();
      for (int i=0; i<n; i++) {
        PlugTreeNode next = (PlugTreeNode)(getChildAt(i));
        if (next.getHandle() != null) {
          break;
        }
        position++;
      }
      super.insert(newChild, position);
    }
  }

  /**
   * Returns the path from the root, to get to this node.
   * This method overrides DefaultMutableTreeNode.getPath(), to ensure that
   * when the node corresponds to a component,
   * nodes corresponding to components higher than the top-level component in
   * the component's hierarchy (namely the container and the microworld) are
   * not erroneously present in the path.
   * @return    An array of TreeNode objects giving the path, where the
   *            first element in the path is the root and the last element
   *            is this node.
   */
  public TreeNode[] getPath()
  {
    TreeNode origPath[] = super.getPath();
    int l = origPath.length;
    ESlateHandle h = ((PlugTreeNode)(origPath[l-1])).getHandle();
    if (h != null) {
      ESlateMicroworld mw = h.getESlateMicroworld();
      if (mw != null) {
        ESlateHandle mwH = mw.getESlateHandle();
        if (mwH != null) {
          for (int i=0; i<l; i++) {
            h = ((PlugTreeNode)(origPath[i])).getHandle();
            if (mwH.equals(h)) {
              int actualLength = l - i - 1;
              TreeNode[] actualPath = new TreeNode[actualLength];
              System.arraycopy(origPath, i+1, actualPath, 0, actualLength);
              return actualPath;
            }
          }
        }
      }
    }
    return origPath;
  }
}
