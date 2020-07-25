package gr.cti.eslate.base.container;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * A property editor for a swing enumerated type. Handles the case in which the
 * PropertyDescriptor has a value for "enumerationValues".
 * Note: the init() method must be called before the set/get methods can be
 * called.
 *
 * @version %I% %G%
 * @author  Mark Davidson
 */
public class EnumeratedValuesEditor extends PropertyEditorSupport implements ActionListener {

    public JComboBox combobox;
    private JPanel panel;
    PropertyChangeSupport pcs;
    Integer currentValue = null;


    public void setValue(Integer value) {
        if (currentValue != null && currentValue.equals(value)) return;
        Integer oldValue = currentValue;
        currentValue = value;

        // Set combo box if it's a new value. We want to reduce number
        // of extraneous events.
        EnumeratedItem item = (EnumeratedItem)combobox.getSelectedItem();
        if (value != null && !value.equals(item.getValue()))  {
            for (int i = 0; i < combobox.getItemCount(); ++i ) {
                item = (EnumeratedItem)combobox.getItemAt(i);
                if (item.getValue().equals(value)) {
                    // XXX - hack! Combo box shouldn't call action event
                    // for setSelectedItem!!
                    combobox.removeActionListener(this);
                    combobox.setSelectedItem(item);
                    combobox.addActionListener(this);
                    return;
                }
            }
        }
        pcs.firePropertyChange("Int", oldValue, currentValue);
    }

    /**
     * Initializes this property editor with the enumerated items. Instances
     * can be shared but there are issues.
     * <p>
     * This method does a lot of jiggery pokery since enumerated
     * types are unlike any other homogenous types. Enumerated types may not
     * represent the same set of values.
     * <p>
     * One method would be to empty the list of values which would have the side
     * effect of firing notification events. Another method would be to recreate
     * the combobox.
     */
    public void init(PropertyDescriptor descriptor) {
        Object[] en = (Object[])descriptor.getValue( "enumerationValues" );
        pcs = new PropertyChangeSupport(this);
        if (en != null) {
            if (combobox == null)  {
                combobox = new JComboBox();

                panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                panel.add(combobox);
            } else {
                // Remove action listener to reduce extra events.
                combobox.removeActionListener(this);
                combobox.removeAllItems();
            }

            for ( int i = 0; i < en.length; i += 3 ) {
                combobox.addItem(new EnumeratedItem((Integer)en[i+1], (String)en[i] ) );
            }

            combobox.addActionListener(this);
        }
    }

    /**
     * Event is set when a combo selection changes.
     */
    public void actionPerformed(ActionEvent evt)  {
        EnumeratedItem item = (EnumeratedItem)combobox.getSelectedItem();
        if (item != null) {
            setValue(item.getValue());
        }
    }

    /**
     * Object which holds an enumerated item plus its label.
     */
    private class EnumeratedItem  {
        private Integer value;
        private String name;

        public EnumeratedItem(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String toString() {
            return name;
        }
    }

    /** 
     * Returns the panel responsible for rendering the PropertyEditor.
     */
    public java.awt.Component getCustomEditor() {
        return panel;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

}
