package gr.cti.structfile;

import javax.swing.*;
import javax.swing.tree.*;

/**
 * This class provides some useful tools for handling JTrees.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class TreeTools
{
  /**
   * We only provide static methods, so make the constructor private.
   */
  private TreeTools()
  {
  }

  /**
   * Sort the children of a node according to their name.
   * @param     node    The node whose children will be sorted.
   */
  public static void sort(EntryNode node)
  {
    // Stupid sort (TM).
    int length = node.getChildCount();
    for (int i=0; i<length-1; i++) {
      for (int j=i+1; j<length; j++) {
        EntryNode n1 = (EntryNode)(node.getChildAt(i));
        EntryNode n2 = (EntryNode)(node.getChildAt(j));
        if (n1.compareTo(n2) > 0) {
          node.remove(j);
          node.remove(i);
          node.insert(n2, i);
          node.insert(n1, j);
        }
      }
    }
  }

  /**
   * Make a node visible.
   * @param     tree    The JTree to which the node belongs.
   * @param     node    The node to make visible.
   */
  public static void makeVisible(JTree tree, DefaultMutableTreeNode node)
  {
    if (node != null) {
      if (!node.isRoot()) {
        DefaultMutableTreeNode parent =
          (DefaultMutableTreeNode)(node.getParent());
        tree.expandPath(new TreePath(parent.getPath()));
      }
      tree.scrollPathToVisible(new TreePath(node.getPath()));
    }
  }

  /**
   * Reloads the model of a JTree component.
   * @param     tree    The JTree whose model will be reloaded.
   */
  public static void reloadModel(JTree tree)
  {
    ((DefaultTreeModel)(tree.getModel())).reload();
  }

  /**
   * Selects a node in a JTree component.
   * @param     tree    The JTree whose model will be reloaded.
   * @param     node    The node to select.
   */
  public static void select(JTree tree, DefaultMutableTreeNode node)
  {
    if (node != null) {
      tree.setSelectionPath(new TreePath(node.getPath()));
    }
  }
}
