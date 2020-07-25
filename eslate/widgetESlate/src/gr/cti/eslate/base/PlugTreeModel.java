package gr.cti.eslate.base;

import java.util.*;
import javax.swing.tree.*;

/**
 * This class implements the tree model for the plug view trees.
 * It is an extension of Swing's default tree model that skips invisible plugs,
 * so that they are not shown in the plug editor.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PlugTreeModel extends DefaultTreeModel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * A list containing the visible children of the last visited node.
   * The list is cached, so that it is not rebuilt when visiting the same node
   * multiple times.
   */
  private ArrayList<PlugTreeNode> visibleChildren =
    new ArrayList<PlugTreeNode>();
  /**
   * The last visited node.
   */
  private Object currentParent = null;

  /**
   * Creates a tree in which any node can have children.
   * @param     root    A TreeNode object that is the root of the tree.
   */
  public PlugTreeModel(TreeNode root)
  {
    super(root);
  }

  /**
   * Creates a tree specifying whether any node can have children, or whether
   * only certain nodes can have children.
   * @param     root                    A TreeNode object that is the root of
   *                                    the tree.
   * @param     asksAllowsChildren      A boolean, false if any node can have
   *                                    children, true if each node is asked
   *                                    to see if it can have children.
   */
  public PlugTreeModel(TreeNode root, boolean asksAllowsChildren)
  {
    super(root, asksAllowsChildren);
  }

  /**
   * Returns a list of a node's visible children. Visible children are those
   * referring to components or to visible plugs.
   * @param     parent  A node in the tree, obtained from this data source.
   */
  private ArrayList<PlugTreeNode> getVisibleChildren(Object parent)
  {
    if (!(parent.equals(currentParent))) {
      currentParent = parent;
      visibleChildren.clear();
      int n = super.getChildCount(parent);
      for (int i=0; i<n; i++) {
        PlugTreeNode child = (PlugTreeNode)(super.getChild(parent, i));
        if (child != null) {
          Plug p = child.getPlug();
          ESlateHandle h = child.getHandle();
          if (((p != null) && p.isVisible()) ||
              ((h != null) && h.isVisible())) {
            visibleChildren.add(child);
          }
        }
      }
    }
    return visibleChildren;
  }

  /**
   * Returns the number of visible children of <code>parent</code>. Returns 0
   * if the node is a leaf or if it has no visible children.
   * <code>parent</code> must be a node previously obtained from this data
   * source.
   * @param     parent  A node in the tree, obtained from this data source.
   * @return    The number of children of the node <code>parent</code>.
   */
  private int getVisibleChildCount(Object parent)
  {
    int n = super.getChildCount(parent);
    int nVisible = 0;
    for (int i=0; i<n; i++) {
      PlugTreeNode child = (PlugTreeNode)(super.getChild(parent, i));
      if (child != null) {
        Plug p = child.getPlug();
        ESlateHandle h = child.getHandle();
        if (((p != null) && p.isVisible()) ||
            ((h != null) && h.isVisible())) {
          nVisible++;
        }
      }
    }
    return nVisible;
  }

  /**
   * Returns the visible child of <code>parent</code> at index
   * <code>index</code> in
   * the parent's child array.  <code>parent</code> must be a node previously
   * obtained from this data source. This should not return null if
   * <code>index</code> is a valid index for <code>parent</code> (that is
   * <code>index</code> &gt;= 0 &amp;&amp; <coe>index</code> &lt;
   * getChildCount(<code>parent</code>)). Only visible
   * @return    The visible child of <code>parent</code> at index
   *            <code>index</code>.
   */
  public Object getChild(Object parent, int index)
  {
    ArrayList<PlugTreeNode> visibleChildren = getVisibleChildren(parent);
    int n = visibleChildren.size();
    if (index <= n) {
      return visibleChildren.get(index);
    }else{
      return null;
    }
  }

  /**
   * Returns the number of visible children of <code>parent</code>. Returns 0
   * if the
   * node is a leaf or if it has no children. <code>parent</code> must be a
   * node previously obtained from this data source.
   * @param     parent  A node in the tree, obtained from this data source.
   * @return    The number of visible children of the node <code>parent</code>.
   */
  public int getChildCount(Object parent)
  {
    return getVisibleChildCount(parent);
  }

  /**
   * Returns the index of <code>child</code> in <code>parent</code>.
   * @return    The index of <code>child</code> in <code>parent</code>.
   */
  public int getIndexOfChild(Object parent, Object child)
  {
    ArrayList<PlugTreeNode> visibleChildren = getVisibleChildren(parent);
    int n = visibleChildren.size();
    for (int i=0; i<n; i++) {
      if (visibleChildren.get(i).equals(child)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Invoke this method if you've modified the TreeNodes upon which this model
   * depends. The model will notify all of its listeners that the model has
   * changed.
   */
  public void reload()
  {
    super.reload();
    currentParent = null;
  }

  /**
   * Invoke this method if you've modified the TreeNodes upon which this model
   * depends. The model will notify all of its listeners that the model has
   * changed below the node node. <EM>(Note: The parent method has been marked
   * as "pending" in the 1.3 autodocs.)</EM>
   * @param     node    The node below which the model will notify all of its
   *                    listeners that the model has changed.
   */
  public void reload(TreeNode node)
  {
    currentParent = null;
    super.reload(node);
  }
}
