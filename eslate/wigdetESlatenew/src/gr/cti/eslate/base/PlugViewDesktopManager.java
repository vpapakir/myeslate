package gr.cti.eslate.base;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * This class implements the desktop manager for the plug view window.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PlugViewDesktopManager extends DefaultDesktopManager
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Construct a plug view desktop manager.
   */
  public PlugViewDesktopManager()
  {
    super();
  }

  /**
   * Resize a frame.
   * @param     f               The component to resize.
   * @param     newX            The component's new X position.
   * @param     newY            The component's new Y position.
   * @param     newWidth        The component's new width.
   * @param     newHeight       The component's new height.
   */
  public void resizeFrame(JComponent f, int newX, int newY, int newWidth, int
                          newHeight)
  {
    super.resizeFrame(f, newX, newY, newWidth, newHeight);

    //
    // Make sure that the displayed tree is repainted.
    //
    if (f instanceof PlugViewFrame) {
      PlugTree tree = ((PlugViewFrame)f).getTree();

      // Nothing to do if the tree is collapsed.
      if (!tree.isExpanded(0)) {
        return;
      }

      // Just repainting the tree does not work for some reason.
      // Reloading the model does, but this does not preserve the expanded
      // state of the tree nodes, so we have to keep track of it ourselves.
      PlugTreeNode root = (PlugTreeNode)(tree.getModel().getRoot());
      TreePath rootPath = new TreePath(root.getPath());
      Enumeration<TreePath> e = tree.getExpandedDescendants(rootPath);
      ArrayList<TreePath> a = new ArrayList<TreePath>();
      if (e != null) {
        while (e.hasMoreElements()) {
          a.add(e.nextElement());
        }
      }

      // Reload the tree model.
      ((DefaultTreeModel)(tree.getModel())).reload();

      // Restore the expanded state of the tree nodes.
      int n = a.size();
      for (int i=0; i<n; i++) {
        tree.expandPath(a.get(i));
      }
    }
  }

  /**
   * Deactivate a frame.
   * @param     f       The frame to deactivate.
   */
  public void deactivateFrame(JInternalFrame f)
  {
    super.deactivateFrame(f);
    if (f instanceof PlugViewFrame) {
      ((PlugViewFrame)f).setNormalBorder();
    }
  }

  /**
   * Activate a farme.
   * @param     f       The frame to activate.
   */
  public void activateFrame(JInternalFrame f)
  {
    super.activateFrame(f);
    if (f instanceof PlugViewFrame) {
      ((PlugViewFrame)f).setHighlightBorder();
    }
  }

  /**
   * Close an internal frame, making sure that that the currently selected
   * frame stays selected.  (Under swing 1.1.1fcs, closing an internal frame
   * deselects all frames. This does not seem to be the case in Java 1.3, but
   * we leave the code in, just in case.)
   * @param     f       The internal frame to close.
   */
  public void closeFrame(JInternalFrame f)
  {
    if (f instanceof PlugViewFrame) {
      ESlateMicroworld mw = ((PlugViewDesktopPane)(f.getParent())).microworld;
      super.closeFrame(f);
      mw.setSelectedComponent(mw.getActiveHandle());
    }else{
      super.closeFrame(f);
    }
  }

}
