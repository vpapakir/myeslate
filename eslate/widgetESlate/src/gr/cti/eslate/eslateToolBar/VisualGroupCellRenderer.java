package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * Cell renderer for the visual group editor of the E-Slate toolbar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class VisualGroupCellRenderer implements TreeCellRenderer
{
  /**
   * The label with the name of the node corresponding to the tool or visual
   * group node that is currently being displayed.
   */
  private JLabel textLabel;
  /**
   * The label with the icon of the node corresponding to the tool or visual
   * group node that is currently being displayed.
   */
  private JLabel iconLabel;
  /**
   * The label used for the root of the tree. The root has no meaning, and we
   * only display a line of text for it, so that the "+" marks will appear for
   * its children, which are the visual groups.
   */
  private JLabel rootLabel;
  /**
   * The panel used for all noeds other than the root of the tree.
   */
  private JPanel nodePanel;
  /**
   * The maximum height of the icons used by the cell renderer.
   */
  public final static int MAX_ICON_HEIGHT = 16;

  /**
   * The color to draw text when the node is not selected.
   */
  private Color textForegroundColor = UIManager.getColor("textText");
  /**
   * The color of the background to the text when the node is not selected.
   */
  private Color textBackgroundColor = UIManager.getColor("Tree.background");
  /**
   * The color to draw text when the node is selected.
   */
  private Color textSelectedForegroundColor =
    UIManager.getColor("textHighlightText");
  /**
   * The color of the background to the text when the node is selected.
   */
  private Color textSelectedBackgroundColor =
    UIManager.getColor("textHighlight");

  /**
   * Create the cell renderer.
   */
  VisualGroupCellRenderer()
  {
    textLabel = new JLabel();
    iconLabel = new JLabel();
    textLabel.setOpaque(true);
    nodePanel = new JPanel();
    nodePanel.setOpaque(false);
    nodePanel.setLayout(new BoxLayout(nodePanel, BoxLayout.X_AXIS));
    nodePanel.add(iconLabel);
    nodePanel.add(Box.createHorizontalStrut(textLabel.getIconTextGap()));
    nodePanel.add(textLabel);
    rootLabel = new JLabel();
    rootLabel.setOpaque(true);
    rootLabel.setText(VisualGroupEditor.resources.getString("root"));
    Font f = rootLabel.getFont().deriveFont(Font.BOLD);
    rootLabel.setFont(f);
  }

  /**
   * Returns the component that the renderer of the tree will use to render a
   * given node.
   * @param     tree            The tree that is being rendered.
   * @param     value           The node that is currently being rendered.
   * @param     isSelected      Not used.
   * @param     expanded        Not used.
   * @param     leaf            Not used.
   * @param     row             Not used.
   * @param     hasFocus        Not used.
   */
  public Component getTreeCellRendererComponent(
    JTree tree, Object value, boolean isSelected, boolean expanded,
    boolean leaf, int row, boolean hasFocus)
  {
    VisualGroupNode node = (VisualGroupNode)value;
    Component comp;
    JLabel lab;
    if (node.isRoot()) {
      comp = rootLabel;
      lab = rootLabel;
    }else{
      iconLabel.setIcon(node.imageIcon);
      textLabel.setText(node.toString());
      comp = nodePanel;
      lab = textLabel;
    }
    if (isSelected) {
      lab.setForeground(textSelectedForegroundColor);
      lab.setBackground(textSelectedBackgroundColor);
    }else{
      lab.setForeground(textForegroundColor);
      lab.setBackground(textBackgroundColor);
    }
    if (((node.group != null) && (node.group.visible)) ||
        ((node.tool != null) && (node.tool.visible))) {
      lab.setEnabled(true);
    }else{
      lab.setEnabled(false);
    }
    return comp;
  }

  /**
   * Returns the font used to render the text for a given node.
   * @param     node    The node.
   * @return    The font used to render the text for the given node.
   */
  Font getFont(VisualGroupNode node)
  {
    if (node.isRoot()) {
      return rootLabel.getFont();
    }else{
      return textLabel.getFont();
    }
  }

  /**
   * Returns the icon used when rendering a given node.
   * @param     node    The node.
   * @return    The icon used when rendering the given node.
   */
  ImageIcon getIcon(VisualGroupNode node)
  {
    return node.imageIcon;
  }

  /**
   * Returns the size of the gap between icon and text when rendering a given
   * node.
   * @param     node    The node.
   * @return    The size of the gap between icon and text when rendering the
   *            given node.
   */
  int getIconTextGap(VisualGroupNode node)
  {
    if (node.isRoot()) {
      return 0;
    }else{
      return textLabel.getIconTextGap();
    }
  }

  /**
   * Returns the preferred size of the component used to render a given node.
   * @param     node    The node.
   * @param     The preferred size of the component used to render the given
   *            node.
   */
  Dimension getPreferredSize(VisualGroupNode node)
  {
    Component c =
      getTreeCellRendererComponent(null, node , false, false, false, 0, false);
    return c.getPreferredSize();
  }
}
