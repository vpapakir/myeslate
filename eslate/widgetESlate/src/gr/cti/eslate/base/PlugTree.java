package gr.cti.eslate.base;

import javax.swing.*;
import javax.swing.tree.*;

/**
 * This class implements the trees in the plug editor.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class PlugTree extends JTree
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The root node of the tree.
   */
  PlugTreeNode root;

  /**
   * Construct a PlugTree.
   */
  PlugTree(PlugTreeNode root)
  {
    super(root);
    this.root = root;
  }

  /**
   * Return the root node of the tree.
   * @return    The root node of the tree.
   */
  PlugTreeNode getRoot()
  {
    return root;
  }

  /**
   * Returns the path from the root, to get to a given node. The last element
   * in the path is that node. Any parents of the root are trimmed off.
   * @param     node    The node.
   * @return    An array of TreeNode objects giving the path, where the
   *            first element in the path is the root and the last element
   *            is the given node.
   * @return    The <code>TreePath</code> for the given node.
   */
  TreePath getPath(PlugTreeNode node)
  {
    return new TreePath(trimPath(node.getPath()));
  }

  /**
   * Trims a node path, removing any parents of the root.
   * @param     path    An array of nodes, starting from the topmost node,
   *                    which may be the root of the tree or one of its
   *                    ancestors.
   * @return    An array of nodes, starting from the root of the tree.
   *            If the root is not contained in the given path, the given path
   *            is returned unmodified.
   */
  private TreeNode[] trimPath(TreeNode[] path)
  {
    int start = -1;
    int n = path.length;
    for (int i=0; i<n; i++) {
      if (path[i].equals(root)) {
        start = i;
        break;
      }
    }
    if (start >= 0) {
      int nNew = n - start;
      TreeNode[] newPath = new TreeNode[nNew];
      System.arraycopy(path, start, newPath, 0, nNew);
      return newPath;
    }else{
      return path;
    }
  }

  /**
   * Checks whether a given node is expanded.
   * @param     node    The node.
   * @return    True of the node is expanded, false otherwise. For leaf nodes,
   *            this method always returns true.
   */
  boolean isExpanded(PlugTreeNode node)
  {
    if (node.isLeaf()) {
      return true;
    }else{
      return isExpanded(getPath(node));
    }
  }
}
