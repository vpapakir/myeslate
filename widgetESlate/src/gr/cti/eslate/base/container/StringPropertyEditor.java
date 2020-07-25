package gr.cti.eslate.base.container;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;

import javax.swing.JTextField;


public class StringPropertyEditor extends PropertyEditorSupport {
    String oldValue;
    String value;
    PropertyChangeSupport pcs;
    JTextField textField;

    public StringPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                oldValue = ((JTextField) e.getSource()).getText();
            }
            public void keyReleased(KeyEvent e) {
                value = ((JTextField) e.getSource()).getText();
                pcs.firePropertyChange("Text", oldValue, value);
            }
        });
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
        if (!String.class.isInstance(value))
            return;
        this.value = (String) value;
        textField.setText(this.value);
    }

    public Object getValue() {
        return value;
    }

    public java.awt.Component getCustomEditor() {
        return textField;
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
