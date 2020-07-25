package gr.cti.eslate.base.container;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;

import javax.swing.JTextField;


public class ShortPropertyEditor extends PropertyEditorSupport {
    Short oldValue;
    Short value;
    PropertyChangeSupport pcs;
    JTextField textField;

    public ShortPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setNewValue();
                }
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                setNewValue();
            }
        });
    }

    void setNewValue() {
        Short tmp = value;
        try{
            value = new Short(textField.getText());
            if (value.equals(tmp))
                return;
//            System.out.println("Setting new value");
            oldValue = tmp;
            pcs.firePropertyChange("Short", oldValue, value);
        }catch (Exception exc) {
            if (tmp == null)
                textField.setText("");
            else
                textField.setText(tmp.toString());
        }
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public java.awt.Component getCustomEditor() {
        return textField;
    }

    public void setValue(Object value) {
        if (!Short.class.isInstance(value))
            return;
        this.value = (Short) value;
        textField.setText(this.value.toString());
    }

    public Object getValue() {
        return value;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}

