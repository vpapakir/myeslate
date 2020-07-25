package gr.cti.eslate.propertyEditors;

import gr.cti.eslate.spinButton.*;
import java.awt.*;
import java.beans.*;
import java.util.Date;

public class DatePropertyEditor extends PropertyEditorSupport {

    private PropertyChangeSupport pcs;
    SpinButton spinButton = new SpinButton();;
    Date day = new Date();

    public DatePropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        spinButton.setModelType(0);
        spinButton.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged(ValueChangedEvent e) {
                    Date oldDay = day;
                    Date day = (Date) spinButton.getValue();

                    pcs.firePropertyChange("Date", oldDay, day);
                }
            }
        );
    }

    public void setValue(Object value) {
        if (!Date.class.isInstance(value))
            return;
        if ((day != null) && day.equals(value))
            return;
        day = (Date) value;
        if (day != null && spinButton != null) {
            spinButton.setValue(day);
        }
    }

    /**
     * Return the current value of the property.
     * @return	The requested value.
     */
    public Object getValue() {
        return (Date) day;
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

    public String getAsText() {
        return null;
    }
}
