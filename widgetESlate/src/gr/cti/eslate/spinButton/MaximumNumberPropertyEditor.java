package gr.cti.eslate.spinButton;


import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;


public class MaximumNumberPropertyEditor extends PropertyEditorSupport {

    private PropertyChangeSupport pcs;
    SpinButton spinButton = new SpinButton();
    Double n = new Double(0.0);

    public MaximumNumberPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        spinButton.setModelType(2);
        double maxc = 99999999.999999999;
        double minc = -99999999.999999999;

        ((NumberDataModel) spinButton.model).setMaximumValue(new Double(maxc));
        ((NumberDataModel) spinButton.model).setMinimumValue(new Double(minc));
        spinButton.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged(ValueChangedEvent e) {
                    Double oldN = n;
                    Double n = (Double) spinButton.getValue();

                    pcs.firePropertyChange("Number", oldN, n);

                }
            }
        );
    }

    public void setValue(Object value) {
        //       System.out.println("setValue in editor called : "+ value);
        n = (Double) value;
        if (n != null && spinButton != null) {
            spinButton.setValue(n);
        }
    }

    /**
     * Return the current value of the property.
     * @return	The requested value.
     */
    public Object getValue() {
        return n;
    }

    /**
     * @return The editor is a button which brings up a dialog.
     */
    public Component getCustomEditor() {
        return spinButton;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    /**
     * Register a listener for the PropertyChange event. The class will fire a
     * PropertyChange value whenever the value is updated.
     * @param	l	An object to be invoked when a PropertyChange event is
     *			fired.
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * Remove a listener for the PropertyChange event.
     * @param	l	The PropertyChange listener to be removed.
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);

    }
}
