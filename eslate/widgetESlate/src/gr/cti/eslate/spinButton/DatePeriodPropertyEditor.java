package gr.cti.eslate.spinButton;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Vector;

import javax.swing.JComboBox;


public class DatePeriodPropertyEditor extends PropertyEditorSupport {

    private PropertyChangeSupport pcs;
    String period = "";
    JComboBox comboBox;
    Vector v;

    public DatePeriodPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        v = new Vector();
        v.addElement("YEARS");
        v.addElement("MONTHS");
        v.addElement("DAYS");
        comboBox = new JComboBox(v);
        comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String oldPeriod = period;
                    String newPeriod = (String) comboBox.getSelectedItem();

                    pcs.firePropertyChange("Period", oldPeriod, newPeriod);
                    period = newPeriod;
                }
            }
        );
    }

    public void setValue(Object value) {
        if (!String.class.isInstance(value))
            return;
        //        if ((model!=null) && model.equals(value))
        //            return;
        period = (String) value;
        //        if (model !=null)
        comboBox.setSelectedItem(period);
    }

    public Object getValue() {
        //        model = (String) comboBox.getSelectedItem();
        return (String) period;
    }

    public Component getCustomEditor() {
        return comboBox;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);

    }

}
