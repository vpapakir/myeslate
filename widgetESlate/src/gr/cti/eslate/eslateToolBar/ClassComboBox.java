package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

/**
 * A combo box for selecting classes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
class ClassComboBox extends JComboBox
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  /**
   * Construct a ClassComboBox instance.
   * @param     c       The classes that will be added to teh combo box.
   */
  ClassComboBox(ClassLabel[] classes)
  {
    super();
    int n = classes.length;
    for (int i=0; i<n; i++) {
      addItem(classes[i]);
    }
    setEditable(true);
    setEditor(new MyEditor());
    setRenderer(new MyRenderer());
  }

  /**
   * Cell renderer for the combo box.
   */
  private class MyRenderer implements ListCellRenderer
  {
    private Color fg = UIManager.getColor("textText");
    private Color bg = UIManager.getColor("List.background");
    private Color selFg = UIManager.getColor("textHighlightText");
    private Color selBg = UIManager.getColor("textHighlight");

    /**
     * Returns the selected ClassLabel with appropriate colors depending on
     * its selection status.
     */
    public Component getListCellRendererComponent(
      JList list, Object value, int index, boolean isSelected,
      boolean cellHasFocus)
    {
      ClassLabel label = (ClassLabel)value;
      if (isSelected) {
        label.setForeground(selFg);
        label.setBackground(selBg);
      }else{
        label.setForeground(fg);
        label.setBackground(bg);
      }
      return label;
    }
  }

  /**
   * Editor for the combo box.
   */
  private class MyEditor extends BasicComboBoxEditor
  {
    /**
     * Sets the text for the editing field to that of the specified
     * ClassLabel.
     */
    public void setItem(Object item)
    {
      if (item instanceof JLabel) {
        super.setItem(((JLabel)item).getText());
      }else{
        super.setItem(item);
      }
    }
  }
}
