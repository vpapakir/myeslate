package gr.cti.eslate.spinButton;


import java.util.Locale;
import java.util.Vector;


public class EnumeratedDataModel  implements DataModelInterface {
    int step;
    String selectedItem;
    SpinModelDataEventMulticaster spinModelDataListener = new SpinModelDataEventMulticaster();
    Locale locale;
    Vector v;
    //   SpinButton spinButton;
    int position = 0;

    /**
     * Constructs a new EnumeeratedDataModel with an empty set of elements.
     *
     */

    public EnumeratedDataModel(/*SpinButton spinButton*/) {
        v = new Vector();
        //      this.spinButton = spinButton;
    }

    /**
     * Returns a String represending the selected element from the model's set of elements
     *
     */

    public Object getValue() {
        return selectedItem;
    }

    /**
     * Sets the model's selected element.
     *@param value The elemnt to be selected (string value)
     */

    public void setValue(Object value) {
        String old = new String(selectedItem);
        selectedItem = (String) value;
        if (spinModelDataListener != null) {
            SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.VALUE_CHANGED, value, old);
            spinModelDataListener.spinModelDataChanged(en);
        }
    }

    /**
     * Same as getValue() for this model.
     *
     */

    public String getStringValue() {
        return (String) selectedItem;

    }

    /**
     * Returns a String specifying the model's data type
     *
     */

    public int getDataModelType() {
        return SpinButton.ENUMERATED_DATA_MODEL;
        //      return new String("EnumeratedDataModel");
    }

    /**
     * Makes selected the element that is d.intValue() positions after the current selected element
     * E.g increase(10.8) selects the 10nth element after the current element
     *
     */

    public void increase(double d) {
        //      System.out.println("increasing");
        step = (new Double(d)).intValue();
        if ((position + step <= v.size() - 1) && (position + step >= 0)) {
            setValue((String) v.elementAt(position + step));
            position = position + step;
        }
        
    }

    /**
     * Makes selected the element that is d.intValue() positions before the current selected element
     * E.g increase(10.8) selects the 10nth element before the current element
     *
     */

    public void decrease(double d) {
        step = (new Double(d)).intValue();
        if ((position - step <= v.size() - 1) && (position - step >= 0)) {
            setValue((String) v.elementAt(position - step));
            position = position - step;
        }

    }

    /**
     * Sets the vector that defines the set of discrete elements for this model
     *@param v The elements vector
     *
     */

    public void setElements(Vector v) {
        Object previousSelectedItem = selectedItem;

        this.v = v;
        selectedItem = (String) v.elementAt(0);
        if (spinModelDataListener != null) {
            SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.VALUE_CHANGED, selectedItem, previousSelectedItem);

            spinModelDataListener.spinModelDataChanged(en);
        }
    }

    /**
     * Returns the vector that defines the set of discrete elements for this model
     *
     */

    public Vector getElements() {
        return v;
    }

    /**
     * Adds a SpinModelDataListener to this enumeratedDataModel
     * @param listener The SpinModelDataListener
     *
     */

    public void addSpinModelDataListener(SpinModelDataListener listener) {
        spinModelDataListener.add(listener);
    }

    /**
     * Removes a SpinModelDataListener from this enumeratedDataModel
     * @param listener The SpinModelDataListener
     *
     */

    public void removeSpinModelDataListener(SpinModelDataListener listener) {
        spinModelDataListener.remove(listener);
    }

    public Object getMinimumValue() {
        return null;
    }

    public Object getMaximumValue() {
        return null;
    }

    public void setMinimumValue(Object o) {}

    public void setMaximumValue(Object o) {}

}
