package gr.cti.eslate.base.container;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * Cell renderer for the PerformanceManagerPanel's tree.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class TimerCellRenderer extends JPanel implements TreeCellRenderer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The icon displayed before the name of PerformanceTimers when they are in
   * the <code>ELAPSED</code> state.
   */
  final static ImageIcon icon1 =
    new ImageIcon(TimerCellRenderer.class.getResource("clock.gif"));
  /**
   * The icon displayed before the name of PerformanceTimers when they are in
   * the <code>ACCUMULATIVE</code> state.
   */
  final static ImageIcon icon2 =
    new ImageIcon(TimerCellRenderer.class.getResource("clock2.gif"));
  /**
   * The label with the name of the PerformanceTimerGroup corresponding to the
   * node that is currently being displayed.
   */
  private JLabel label;
  /**
   * The check box indicating the activation state of the
   * PerformanceTimerGroup corresponding to the node that is currently being
   * displayed.
   */
  private JCheckBox cBox;
  /**
   * The label used for the root of the tree. The root has no meaning, and we
   * only display a line of text for it, so that the "+" marks will appear for
   * its children, which are the global PerformanceTimerGroups.
   */
  private JLabel rootLabel;

  /**
   * Create the cell renderer.
   */
  TimerCellRenderer()
  {
    label = new JLabel((Icon)null);
    setLayout(new BorderLayout(0, 0));
    cBox = new JCheckBox();
    add(cBox, BorderLayout.WEST);
    add(label, BorderLayout.CENTER);
    rootLabel = new JLabel(
      PerformanceManagerPanel.resources.getString("globalGroups")
    );
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
    TimerTreeNode node = (TimerTreeNode)value;
    Component comp;
    if (node.isRoot()) {
      comp = rootLabel;
    }else{
      PerformanceTimerGroup timer = node.timer;
      if (timer != null) {
        if (timer instanceof PerformanceTimer) {
          if ((timer.timeMode == PerformanceTimerGroup.ELAPSED)) {
            label.setIcon(icon1);
          }else{
            label.setIcon(icon2);
          }
        }else{
          label.setIcon(null);
        }
        if (timer == null) {
          label.setText("***NULL***");
          cBox.setSelected(false);
        }else{
          label.setText(timer.toString());
          cBox.setSelected(timer.isDisplayEnabled());
        }
        label.setEnabled(timer.active);
      }else{
        ClassTimer ct = node.classTimer;
        label.setIcon(null);
        label.setText(ct.className);
        label.setEnabled(true);
        cBox.setSelected(ct.enabled);
      }
      comp = this;
    }
    revalidate();
    return comp;
  }
}
