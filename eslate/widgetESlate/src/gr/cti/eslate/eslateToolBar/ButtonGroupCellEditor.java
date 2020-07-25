package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * Cell editor for the button group editor of the E-Slate toolbar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class ButtonGroupCellEditor extends DefaultTreeCellEditor
{
  /**
   * The node currently being edited.
   */
  private ButtonGroupNode node;

  /**
   * Constructs a <code>ButtonGroupCellEditor<code> object for the visual
   * group editor tree using the specified renderer and a default editor.
   * @param     tree            The button group editor tree.
   */
  ButtonGroupCellEditor(JTree tree, ButtonGroupCellRenderer renderer)
  {
    super(tree, null);
    this.renderer = new RendererWrapper(renderer);
  }

  /**
   * Configures the editor.
   * @param     tree            The <code>JTree</code> that is asking the
   *                            editor to edit; this parameter can be
   *                            <code>null</code>.
   * @param     value           The value of the cell to be edited.
   * @param     isSelected      True if the cell is to be rendered with
   *                            selection highlighting
   * @param     expanded        True if the node is expanded.
   * @param     leaf            True if the node is a leaf node.
   * @param     row             The row index of the node being edited.
   */
  public Component getTreeCellEditorComponent(
    JTree tree, Object value, boolean isSelected, boolean expanded,
    boolean leaf, int row)
  {
    node = (ButtonGroupNode)value;
    return super.getTreeCellEditorComponent(
      tree, value, isSelected, expanded, leaf, row
    );
  }

  /**
   * A wrapper that fools <code>DefaultTreeCellEditor</code> into thinking
   * that <code>ButtonGroupCellRenderer</code> is a
   * <code>DefaultTreeCellRenderer</code>, by providing the methods that
   * <code>DefaultTreeCellEditor</code> requires.
   */
  private class RendererWrapper extends DefaultTreeCellRenderer
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    /**
     * The wrapped <code>ButtonGroupCellRenderer</code>.
     */
    ButtonGroupCellRenderer bgRenderer;
    /**
     * Indicates whether the object's constructor has finished executing.
     */
    boolean initted = false;

    /**
     * Create a <code>RendererWrapper</code> instance.
     */
    RendererWrapper(ButtonGroupCellRenderer bgRenderer)
    {
      super();
      this.bgRenderer = bgRenderer;
      initted = true;
    }

    /**
     * Returns the renderer's font.
     * @return  The renderer's font.
     */
    public Font getFont()
    {
      if (initted && node != null) {
        return bgRenderer.getFont(node);
      }else{
        return super.getFont();
      }
    }

    /**
     * Returns the renderer's leaf icon.
     * @return  The renderer's font.
     */
    public Icon getLeafIcon()
    {
      if (initted && node != null) {
        return bgRenderer.getIcon(node);
      }else{
        return super.getLeafIcon();
      }
    }

    /**
     * Returns the renderer's open icon.
     * @return  The renderer's open icon.
     */
    public Icon getOpenIcon()
    {
      if (initted && node != null) {
        return bgRenderer.getIcon(node);
      }else{
        return super.getOpenIcon();
      }
    }

    /**
     * Returns the renderer's closed icon.
     * @return  The renderer's closed icon.
     */
    public Icon getClosedIcon()
    {
      if (initted && node != null) {
        return bgRenderer.getIcon(node);
      }else{
        return super.getIcon();
      }
    }

    /**
     * Returns the renderer's icon text gap.
     * @return  The renderer's icon text gap.
     */
    public int getIcontextGap()
    {
      if (initted && node != null) {
        return bgRenderer.getIconTextGap(node);
      }else{
        return super.getIconTextGap();
      }
    }

    /**
     * Returns the renderer's preferred size.
     * @return  The renderer's preferred size.
     */
    public Dimension getPreferredSize()
    {
      if (initted && node != null) {
        return bgRenderer.getPreferredSize(node);
      }else{
        return super.getPreferredSize();
      }
    }
  }

}
