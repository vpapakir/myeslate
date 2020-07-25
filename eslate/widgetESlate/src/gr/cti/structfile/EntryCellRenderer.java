package gr.cti.structfile;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * Displays an entry in a structured file tree view.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class EntryCellRenderer extends DefaultTreeCellRenderer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The icon for the root of the tree.
   */
  static ImageIcon rootIcon;
  /**
   * The icon used for file nodes.
   */
  static ImageIcon fileIcon = null;
  /**
   * The icon used for directory leaf nodes.
   */
  static Icon folderIcon = null;

  /**
   * Constructs a new EntryCellRenderer.
   */
  public EntryCellRenderer()
  {
    super();
    setBackgroundNonSelectionColor(new Color(0xFFFFE4));
    if (fileIcon == null) {
      fileIcon = new ImageIcon(getClass().getResource("File.gif"));
      rootIcon = new ImageIcon(getClass().getResource("SFile.gif"));
      folderIcon = getClosedIcon();
    }
  }

  /**
   * Configures the renderer based on the values passed in components.
   */
  public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                boolean sel,
                                                boolean expanded,
                                                boolean leaf, int row,
                                                boolean hasFocus)
  {
    EntryNode node = (EntryNode)value;
    // Invoke parent's method, to set everything up.
    super.getTreeCellRendererComponent(
      tree, value, sel, expanded, leaf, row, hasFocus
    );
    if (node.isRoot()) {
      setIcon(rootIcon);
    }else{
      if (node.getEntry().getType() == Entry.FILE) {
        setIcon(fileIcon);
      }else{
        if (node.isLeaf() && node.getEntry().getType() == Entry.DIRECTORY) {
          setIcon(getClosedIcon());
        }
      }
    }
    return this;
  }
}
