package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;


/**
 * Editor for the <code>customSize</code> property of SkinPane.
 * <p>
 * @author  Giorgos Vasiliou
 * @version 1.0, 23-Apr-2001
 */
public class EditorPanelSize extends PropertyEditorSupport {
    private SpinField spin;
    private PropertyChangeSupport c;
    private int old;

    public EditorPanelSize() {
        super();
        c=new PropertyChangeSupport(this);
        spin=new SpinField(true,"",1000);
        spin.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent e) {
                int newValue = ((Number) e.getValue()).intValue();
                c.firePropertyChange("customSize",old, newValue);
                old = newValue;
            }
        });
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                old=((Integer) getValue()).intValue();
                spin.spin.setValue(getValue());
            }
        });
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertychangelistener) {
        c.removePropertyChangeListener(propertychangelistener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertychangelistener) {
        c.addPropertyChangeListener(propertychangelistener);
    }

    public Component getCustomEditor() {
        return spin;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
