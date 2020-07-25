package gr.cti.eslate.eslateToolBar;

import javax.swing.*;
import javax.swing.tree.*;

/**
 * Tree node for the visual group tree.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
class VisualGroupNode extends DefaultMutableTreeNode
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Information about the visual group to which the node corresponds.
   */
  VisualGroupEditorGroupInfo group;
  /**
   * Information about the E-Slate handle of the tool to which the node
   * corresponds.
   */
  VisualGroupEditorToolInfo tool;
  /**
   * The icon corresponding to the tool to which the node corresponds.
   * This is <code>null</code> if the node corresponds to a visual group
   * or is the root of the tree.
   */
  ImageIcon imageIcon;
  /**
   * The icon used for nodes representing visual groups.
   */
  private static ImageIcon folderIcon = new ImageIcon(
    VisualGroupCellRenderer.class.getResource("images/group.gif")
  );
  /**
   * The icon used for nodes representing tools that do not have an icon of
   * their own.
   */
  private static ImageIcon toolIcon = new ImageIcon(
    VisualGroupCellRenderer.class.getResource("images/tool.gif")
  );

  /**
   * Construct a node corresponding to a visual group.
   * @param     info    The description of the visual group to which the
   *                    node corresponds.
   */
  VisualGroupNode(VisualGroupEditorGroupInfo info)
  {
    super();
    this.group = info;
    this.tool = null;
    imageIcon = folderIcon;
  }

  /**
   * Construct a node corresponding to a tool.
   * @param     info    A description of the tool to which the node
   *                    corresponds.
   */
  VisualGroupNode(VisualGroupEditorToolInfo info)
  {
    super();
    this.group = null;
    this.tool = info;
    if (info.image != null) {
      imageIcon = new ImageIcon(info.image);
    }else{
      imageIcon = toolIcon;
    }
  }

  /**
   * Construct a node that does not correspond to a visual group or a tool.
   * (Use this constructor to build the root of the visual group tree.)
   */
  VisualGroupNode()
  {
    super();
    this.group = null;
    this.tool = null;
    imageIcon = null;
  }

  /**
   * Returns a string representation of the node.
   * @return    The name of the corresponding tool or visual group.
   */
  public String toString()
  {
    if (group != null) {
      return group.name;
    }else{
      if (tool != null) {
        return tool.name;
      }else{
        return null;
      }
    }
  }

}
