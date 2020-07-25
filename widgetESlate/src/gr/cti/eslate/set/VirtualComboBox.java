package gr.cti.eslate.set;

import gr.cti.typeArray.*;

/**
 * This class implements a fake combo box, which acts as a placeholder for
 * the names that would appear in a combo box, keeping track of the selected
 * item.
 *
 * @author      Kriton Kyrimis.
 * @version     2.0.0, 29-May-2006
 */
class VirtualComboBox
{
  /**
   * The names to manage.
   */
  StringBaseArray names = new StringBaseArray();
  /**
   * The index of the selected item.
   */
  int selIndex = -1;

  /**
   * Construct a VirtualComboBox instance.
   */
  VirtualComboBox()
  {
    super();
  }

  /**
   * Add a name.
   * @param     name    The name to add.
   */
  void addItem(String name)
  {
    names.add(name);
  }

  /**
   * Set the selected item.
   * @param     name    The selected item.
   */
  void setSelectedItem(String name)
  {
    selIndex = names.indexOf(name);
  }

  /**
   * Set the index of the selected item.
   * @param     index   The index of the selected item.
   */
  void setSelectedIndex(int index)
  {
    selIndex = index;
  }

  /**
   * Returns the index of the selected item.
   * @return    The index of the selected item.
   */
  int getSelectedIndex()
  {
    return selIndex;
  }

  /**
   * Returns the selected item.
   * @return    The selected item.
   */
  String getSelectedItem()
  {
    if (selIndex >= 0) {
      return names.get(selIndex);
    }else{
      return "";
    }
  }

  /**
   * Removes all items.
   */
  void removeAllItems()
  {
    names.clear();
    selIndex = -1;
  }

  /**
   * Returns the number of items.
   * @return    The number of items.
   */
  int getItemCount()
  {
    return names.size();
  }

  /**
   * Returns the item at the given index.
   * @param     index   The index.
   * @return    The item at the given index.
   */
  String getItemAt(int index)
  {
    return names.get(index);
  }
}
