package gr.cti.eslate.base.container;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;

import javax.swing.JTextField;


public class IntPropertyEditor extends PropertyEditorSupport {
    Integer oldValue;
    Integer value;
    PropertyChangeSupport pcs;
    JTextField textField;
    // Property editor to use if the Integer represents an Enumerated type.
    private EnumeratedValuesEditor enumEditor = new EnumeratedValuesEditor();
    private boolean isEnumeration = false;

    /**
     * Initializes this property editor with the enumerated items.
     */
    public void init(PropertyDescriptor descriptor) {
        Object[] en = (Object[])descriptor.getValue("enumerationValues");
        if (en != null) {
            // The property descriptor describes an enumerated item.
            isEnumeration = true;
            enumEditor.init(descriptor);
        }else{
            pcs = new PropertyChangeSupport(this);
            // This is an integer item
            isEnumeration = false;

            if (textField == null)  {
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
        }
    }

    void setNewValue() {
        Integer tmp = value;
        try{
            value = new Integer(textField.getText());
            if (value.equals(tmp))
                return;

            oldValue = tmp;
            pcs.firePropertyChange("Int", oldValue, value);
        }catch (Exception exc) {
            if (tmp == null)
                textField.setText("");
            else
                textField.setText(tmp.toString());
        }
    }

    /**
     * Must overload the PropertyChangeListener registration because
     * this class is the only interface to the EnumeratedValuesEditor.
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (isEnumeration)
            enumEditor.pcs.addPropertyChangeListener(listener);
        else
            pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (isEnumeration)
            enumEditor.pcs.removePropertyChangeListener(listener);
        else
            pcs.removePropertyChangeListener(listener);
    }

    /**
     * Return the custom editor for the enumeration or the integer.
     */
    public java.awt.Component getCustomEditor() {
        if (isEnumeration) {
            return enumEditor.getCustomEditor();
        }else{
            return textField;
        }
    }

    public void setValue(Object value) {
        if (!Integer.class.isInstance(value))
            return;
        if (isEnumeration) {
            enumEditor.setValue((Integer) value);
        }else{
            this.value = (Integer) value;
            textField.setText(this.value.toString());
        }
    }

    public Object getValue() {
        if (isEnumeration)  {
            return enumEditor.getValue();
        }else{
            return value;
        }
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}

