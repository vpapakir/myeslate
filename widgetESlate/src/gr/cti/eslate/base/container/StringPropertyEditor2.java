package gr.cti.eslate.base.container;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;

import javax.swing.JTextField;


/** This is an editor for properties of type String. It is not the standard
  * E-Slate String property editor, which is used for String properties for which
  * no custom property editor has been specified in the component's BeanInfo.
  * The E-Slate standard String property editor fires a PropertyChangeEvent
  * whenever the user edits the value of the property. <i>StringPropertyEditor2</i>
  * fires propertyChangeEvents only when the ENTER key is pressed or when the
  * editor looses the focus. This makes <i>StringPropertyEditor2</i> more suitable
  * in cases like when the value of a URL is edited.
  */
public class StringPropertyEditor2 extends PropertyEditorSupport {
    String oldValue;
    String value;
    PropertyChangeSupport pcs;
    JTextField textField;

    public StringPropertyEditor2() {
        super();
        pcs = new PropertyChangeSupport(this);
        textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    oldValue = value;
                    value = ((JTextField) e.getSource()).getText();
                    if (value == null && oldValue == null) return;
                    if (value != null && !value.equals(oldValue))
                        pcs.firePropertyChange("Text", oldValue, value);
                    if (value == null && oldValue != null)
                        pcs.firePropertyChange("Text", oldValue, value);
                }
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                oldValue = value;
                value = ((JTextField) e.getSource()).getText();
                if (value == null && oldValue == null) return;
                if (value != null && !value.equals(oldValue))
                    pcs.firePropertyChange("Text", oldValue, value);
                if (value == null && oldValue != null)
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
