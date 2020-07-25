package gr.cti.eslate.spinButton;


import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.GregorianCalendar;


public class MinimumTimePropertyEditor extends PropertyEditorSupport {

    private PropertyChangeSupport pcs;
    SpinButton spinButton = new SpinButton();
    Date time = new Date();

    public MinimumTimePropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        spinButton.setModelType(1);
        GregorianCalendar maxc = new GregorianCalendar(2000, 1, 1, 23, 59, 59);
        GregorianCalendar minc = new GregorianCalendar(2000, 1, 1, 0, 0, 0);

        ((TimeDataModel) spinButton.model).setMaximumValue(maxc.getTime());
        ((TimeDataModel) spinButton.model).setMinimumValue(minc.getTime());
        spinButton.addValueChangedListener(new ValueChangedListener() {
                public void valueChanged(ValueChangedEvent e) {
                    Date oldTime = time;
                    Date time = (Date) spinButton.getValue();

                    pcs.firePropertyChange("Date", oldTime, time);

                }
            }
        );
    }

    public void setValue(Object value) {
        if (!Date.class.isInstance(value))
            return;
        if ((time != null) && time.equals(value))
            return;
        time = (Date) value;
        if (time != null && spinButton != null) {
            spinButton.setValue(time);
        }
    }

    /**
     * Return the current value of the property.
     * @return	The requested value.
     */
    public Object getValue() {
        return (Date) time;
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
