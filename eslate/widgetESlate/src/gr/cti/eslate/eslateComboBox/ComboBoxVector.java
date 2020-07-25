package gr.cti.eslate.eslateComboBox;


import javax.swing.ComboBoxModel;
import java.util.Vector;
import javax.swing.event.*;


public class ComboBoxVector extends Vector implements ComboBoxModel {
	private static final long serialVersionUID = -3131455220054376120L;
	Object selectedItem = null;
    int i;

    /**
     * Constructs a new ComboBoxVector (a Vector implementing ComboBoxModel interface)
     *
     */

    public ComboBoxVector() {
        super();
    }

    /**
     * Returns the combobox models selected item
     */

    public Object getSelectedItem() {
        return selectedItem;
    }

    /**
     * Sets the combobox models selected item
     * @param item The selected (object) item
     */

    public void setSelectedItem(Object item) {
        selectedItem = item;
    }

    /**
     * Removes a  ListDataListener
     * @param listener The ListDataListener to be removed
     */

    public void removeListDataListener(ListDataListener listener) {}

    /**
     * Adds a new ListDataListener
     * @param listener The ListDataListener to be added
     */

    public void addListDataListener(ListDataListener listener) {}

    /**
     * Returns the element at the specified position
     * @param i The position where the element to be returned is
     */

    public Object getElementAt(int i) {
        return elementAt(i);
    }

    /**
     * Returns the size of the combobox vector
     */

    public int getSize() {
        return size();
    }
}
