package gr.cti.eslate.eslateList;


import javax.swing.ListModel;
import java.util.Vector;
import javax.swing.event.*;


public class ListVector extends Vector implements ListModel {

	private static final long serialVersionUID = -7060672128328662983L;
	Object selectedItem = null;
    int i;

    public ListVector() {
        super();
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object item) {
        selectedItem = item;
    }

    public void removeListDataListener(ListDataListener listener) {}

    public void addListDataListener(ListDataListener listener) {}

    public Object getElementAt(int i) {
        return elementAt(i);
    }

    public int getSize() {
        return size();
    }
}
