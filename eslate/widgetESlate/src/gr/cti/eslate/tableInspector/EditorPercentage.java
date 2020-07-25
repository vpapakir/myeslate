package gr.cti.eslate.tableInspector;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;

import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EditorPercentage extends PropertyEditorSupport {
    private JSlider slider;
    private PropertyChangeSupport pcs;
    private Integer myInt;

    public EditorPercentage() {
        super();
        slider=new JSlider(0,100);
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(25);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(false);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                slider.setToolTipText(slider.getValue()+"%");
                setValue(new Integer(slider.getValue()));
            }
        });
        pcs=new PropertyChangeSupport(this);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                slider.setValue(((Integer) getValue()).intValue());
            }
        });
    }

    public void setValue(Object value) {
        if (!Integer.class.isInstance(value))
            return;
        if ((myInt!=null) && myInt.equals(value))
            return;
        Integer oldInt=myInt;
        myInt=(Integer) value;
        pcs.firePropertyChange("percentFields",oldInt,myInt);
    }

    /**
     * Return the current value of the property.
     * @return	The requested value.
     */
    public Object getValue() {
        return myInt;
    }
    /**
     * @return The editor is a button which brings up a dialog.
     */
    public Component getCustomEditor() {
        return slider;
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
