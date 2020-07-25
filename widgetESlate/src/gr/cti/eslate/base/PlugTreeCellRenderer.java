package gr.cti.eslate.base;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;

/**
 * Displays an entry in the plug tree view.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PlugTreeCellRenderer extends DefaultTreeCellRenderer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The icon currently being drawn for the cell.
   */
  private Icon icon;

  /**
   * The image displayed by the cell;
   */
  Image image;

  /**
   * The default color to draw text next to plug icons when the text is not
   * selected.
   */
  private static Color textForegroundColor;

  /**
   * The default color to draw the background next to plug icons when the text
   * is not selected.
   */
  private static Color textBackgroundColor;

  /**
   * The default color to draw the background next to plug icons when the text
   * is selected.
   */
  private static Color textSelectedBackgroundColor;

  /**
   * The background color to use when a node is highlighted.
   */
  private final static Color highlightBackgroundColor = new Color(250, 250, 98);

  /**
   * The background color to use when highlighting a node corresponding to
   * a plug to which the currently selected plug can be connectd.
   */
  private final static Color candidateBackgroundColor =
    new Color(220, 215, 150);

  /**
   * The thickness of the borders around nodes.
   */
  final static int BORDER_THICKNESS = 2;

  /**
   * The border for the root node.
   */
  private static RootNodeBorder border =
    new RootNodeBorder(Color.black, BORDER_THICKNESS);

  /**
   * The border for highlighted nodes.
   */
  private static LineBorder highlightBorder =
    new LineBorder(new Color(250, 212, 5), BORDER_THICKNESS);

  /**
   * The border for nodes that are not highlighted.
   */
  private static LineBorder normalBorder = null;

  /**
   * The font used to display the names of the plugs.
   */
  private static Font plugFont;

  /**
   * The font used to display the name of the component.
   */
  private static Font componentFont;

  /**
   * The PlugTree to which the cell that is currently being rendered belongs.
   */
  private PlugTree tree = null;

  /**
   * Constructs a new PlugTreeCellRenderer.
   */
  public PlugTreeCellRenderer(Image im)
  {
    super();
    image = im;
    setBackgroundNonSelectionColor(UIManager.getColor("Panel.background"));
    if (textForegroundColor == null) {
      updateColorsAndStuff();
    }
    setBorderSelectionColor(null);
    plugFont = UIManager.getFont("Label.font");
    componentFont = plugFont.deriveFont(Font.BOLD);
  }

  /**
   * Updates the various colors and other stuff that depend on the look and
   * feel.
   */
  void updateColorsAndStuff()
  {
    textForegroundColor = UIManager.getColor("controlText");
    textBackgroundColor = UIManager.getColor("control");
    textSelectedBackgroundColor = UIManager.getColor("controlHighlight");
    normalBorder = new LineBorder(textBackgroundColor, BORDER_THICKNESS);
  }

  /**
   * Configures the renderer based on the values passed in components.
   * The value is set from messaging value with toString().
   * The foreground color is set based on the selection and the icon
   * is obtained from the plug represented by the node.
   * @param     tree    The tree to which the node being rendered belongs.
   * @param     sel     Indicates whether the node is selected or not.
   * @param     expanded        Indicates whether the node is expanded or not.
   * @param     leaf            Indicates whether the node is a leaf or not.
   * @param     row             The node's display row.
   * @param     hasFocus        Indicates whether the node has the focus.
   * @return    The component that the renderer uses to draw the value.
   */
  public Component getTreeCellRendererComponent(JTree tree, Object value,       
                                                boolean sel,
                                                boolean expanded,
                                                boolean leaf, int row,
                                                boolean hasFocus)
  {
    PlugTreeNode node = (PlugTreeNode)value;
    // Invoke parent's method, to set everything up. Note that we pass
    // sel=false, as we want to handle selection ourselves, and passing
    // the actual value of sel creates confusion.
    super.getTreeCellRendererComponent(
      tree, value, false, expanded, leaf, row, hasFocus);
    setForeground(textForegroundColor);
    setTextNonSelectionColor(textForegroundColor);

    ESlateHandle handle = node.getHandle();
    if ((handle != null) && handle.isDesktopComponent()) {
      this.tree = (PlugTree)tree;
      icon = new ESlateRootIcon(image);
      setIcon(icon);
      setBackgroundNonSelectionColor(textBackgroundColor);
      setTextSelectionColor(textForegroundColor);
      setBackgroundSelectionColor(textBackgroundColor);
      setFont(componentFont);
      setBorder(null);
      setPreferredSize(null);
      Dimension d = getPreferredSize();
      Dimension newSize = new Dimension(
        getPreferredRootWidth(),
        Math.max(d.height, icon.getIconHeight())
      );
      setPreferredSize(newSize);
      setBorder(border);
      setToolTipText(node.getHandle().getComponentName());
    }else{
      if (handle != null) {
        icon = new ESlateRootIcon(handle.getImage());
        setIcon(icon);
        setBackgroundNonSelectionColor(textBackgroundColor);
        setTextSelectionColor(textForegroundColor);
        setBackgroundSelectionColor(textBackgroundColor);
        setBorder(normalBorder);
        setFont(componentFont);
        setToolTipText(node.getHandle().getComponentName());
      }else{
        icon = new PlugButton(node.getPlug().getIcon());
        setIcon(icon);
        Plug p = node.getPlug();
        int compatibilityFlag = p.getCompatibilityFlag();
        if (compatibilityFlag == Plug.CAN_CONNECT) {
          setBackgroundNonSelectionColor(highlightBackgroundColor);
          setBorder(normalBorder);
        }else{
          if (compatibilityFlag == Plug.CAN_DISCONNECT) {
            setBackgroundNonSelectionColor(candidateBackgroundColor);
            setBorder(normalBorder);
          }else{
            if (node.getPlug().isSelected()) {
              setBackgroundNonSelectionColor(highlightBackgroundColor);
              setBorder(highlightBorder);
            }else{
              setBackgroundNonSelectionColor(textBackgroundColor);
              setBorder(normalBorder);
            }
          }
        }
        setFont(plugFont);
        setTextSelectionColor(textSelectedBackgroundColor);
        setBackgroundSelectionColor(highlightBackgroundColor);
        setToolTipText(node.getPlug().getName());
      }
      this.tree = null;
      setPreferredSize(null);
    }
    return this;
  }

  /**
   * Return the height of the tree cell.
   * @return    The requested height.
   */
  public int getHeight()
  {
    return Math.max(super.getHeight(), icon.getIconHeight());
  }

  boolean calculatingWidth = false;

  /**
   * The preferred width of the tree cell corresponding to the root node.
   * @return    The requested width.
   */
  public int getPreferredRootWidth()
  {
    Component c = tree;
    while (c != null && !(c instanceof JViewport)) {
      c = c.getParent();
    }
    JViewport jv = (JViewport)c;
    JScrollPane jsp;
    if (jv != null) {
      jsp = (JScrollPane)(jv.getParent());
    }else{
      jsp = null;
    }

/*
    if (tree != null && !calculatingWidth) {
      int width;
      if (tree.isExpanded(0)) {
        calculatingWidth = true;
        int treeWidth = tree.getSize().width;
        int treePreferredWidth = tree.getPreferredSize().width;
        calculatingWidth = false;
        // Width is max of tree's actual and preferred width...
        width = Math.max(treeWidth, treePreferredWidth);
        if (jsp != null) {
          Insets jspInsets = jsp.getInsets();
          int jspWidth = jsp.getSize().width - jspInsets.left - jspInsets.right;
          // ... and of the width of the scroll pane.
          width = Math.max(width, jspWidth);
        }
      }else{
        Insets jspInsets = jsp.getInsets();
        int jspWidth = jsp.getSize().width - jspInsets.left - jspInsets.right;
        width = jspWidth;
      }
      // Why do we need to subtract 3 to prevent the node from getting wider
      // and wider each time we open or close it?
      return width - 3;
    }else{
      return getPreferredSize().width;
    }
*/
    // The width of the scroll pane containing the tree, minus the
    // scroll pane's insets.
    Insets jspInsets = jsp.getInsets();
    int jspWidth = jsp.getSize().width - jspInsets.left - jspInsets.right;

    // The mimimum width required to display the node in its entirety.
    Insets nodeInsets = border.getBorderInsets(this);
    String text = getText();
    if (text == null) {
      text = "";
    }
    int nodeWidth = getIcon().getIconWidth() + getIconTextGap() +
      nodeInsets.left + nodeInsets.right +
      SwingUtilities.computeStringWidth(getFontMetrics(getFont()), text);

    // The preferred width is the maximum of the above two widths.
    int width = Math.max(jspWidth, nodeWidth);

    // Why do we need to subtract 3 to prevent the node from getting wider
    // and wider each time we open or close it?
    return width - 3;

  }

}
