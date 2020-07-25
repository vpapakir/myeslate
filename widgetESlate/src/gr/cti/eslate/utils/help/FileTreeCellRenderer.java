package gr.cti.eslate.utils.help;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * Cell renderer for FileTree nodes. This class extends
 * <code><code>DefaultTreeCellRenderer</code>, changing the icon for leaf
 * nodes to a file icon.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The icon for leaf nodes.
   */
  private static ImageIcon fileIcon = new ImageIcon(
    FileTreeCellRenderer.class.getResource("file.gif")
  );

  /**
   * Construct a FileTreeCellRenderer instance.
   */
  public FileTreeCellRenderer()
  {
    super();
    fileIcon = new ImageIcon(
      FileTreeCellRenderer.class.getResource("file.gif")
    );
  }

  /**
   * Overrides
   * <code>DefaultTreeCellRenderer.getTreeCellRendererComponent</code> to
   * change the icon of leaf nodes to a file icon.
   */
  public Component getTreeCellRendererComponent(
    JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
    int row, boolean hasFocus)
  {
    super.getTreeCellRendererComponent(
      tree, value, false, expanded, leaf, row, hasFocus
    );
    FileTree ft = (FileTree)value;
    if (ft.isLeaf()) {
      setIcon(fileIcon);
    }
    return this;
  }

  /**
   * Returns the height of the file icon. Useful for calculating the row
   * height of trees using this cell renderer.
   */
  public static int getFileIconHeight()
  {
    return fileIcon.getIconHeight();
  }
}
