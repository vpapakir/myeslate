package gr.cti.eslate.agent;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

public class EditorTravelsOnID extends PropertyEditorSupport {
    private JComboBox combo;
    private PropertyChangeSupport c;
    private String old;

    public EditorTravelsOnID() {
        super();
        c=new PropertyChangeSupport(this);
        combo=new JComboBox();
        combo.setEditable(true);
        combo.addItem(AgentBeanInfo.bundle.getString("travelseverywhere"));
        combo.addItem(AgentBeanInfo.bundle.getString("travelsonroads"));
        combo.addItem(AgentBeanInfo.bundle.getString("travelsonrailways"));
        combo.addItem(AgentBeanInfo.bundle.getString("travelsonsea"));
        combo.addItem(AgentBeanInfo.bundle.getString("travelsonair"));
        combo.addItem(AgentBeanInfo.bundle.getString("travelsoncustom"));
        combo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.firePropertyChange("travellingOnMotionLayerID",old,combo.getSelectedItem());
                old=new String((String) combo.getSelectedItem());
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                old=new String((String) getValue());
                combo.getEditor().setItem(getValue());
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
