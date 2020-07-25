package gr.cti.eslate.tableInspector;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

public class EditorQueryType extends PropertyEditorSupport {
    private JComboBox combo;
    private PropertyChangeSupport c;
    private int old=0;

    public EditorQueryType() {
        super();
        c=new PropertyChangeSupport(this);
        combo=new JComboBox();
        combo.setEditable(false);
        combo.addItem(TableInspectorBeanInfo.bundle.getString("queryticks"));
        combo.addItem(TableInspectorBeanInfo.bundle.getString("querycombo"));
        combo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.firePropertyChange("queryType",old,combo.getSelectedIndex());
                old=combo.getSelectedIndex();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                old=((Integer) getValue()).intValue();
                combo.setSelectedIndex(old);
            }
        });
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertychangelistener) {
        c.removePropertyChangeListener(propertychangelistener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertychangelistener) {
        c.addPropertyChangeListener(propertychangelistener);
    }
     /**
     * @return The editor is a combobox.
     */
    public Component getCustomEditor() {
        return combo;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
