package gr.cti.eslate.graph2;

import javax.swing.*;

/**
 * Utility methods used by both the Graph2 and BarChart components.
 *
 * @version     1.0.6, 17-Jan-2008
 * @author      Kriton Kyrimis
 */
class Utils
{
  /**
   * Constructor is private, as only static methods are provided.
   */
  private Utils()
  {
  }

  /**
   * Returns the index of an item in a combo box.
   * @param       cb      The combo box.
   * @param       item    The item.
   * @return      The index of the given item in the specified combo box.
   *              If the item is not contained in the combo box, -1
   *              is returned.
   */
  static int getItemIndex(JComboBox cb, Object item)
  {
    int nItems = cb.getItemCount();
    for (int i=0; i<nItems; i++) {
      Object it = cb.getItemAt(i);
      if (it.equals(item)) {
        return i;
      }
    }
    return -1;
  }

}
