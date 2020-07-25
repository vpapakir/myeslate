package gr.cti.eslate.spinButton;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import java.util.*;


public class TimePeriodPropertyEditor extends PropertyEditorSupport {

    private PropertyChangeSupport pcs;
    String period = "";
    JComboBox comboBox;
    Vector v;

    public TimePeriodPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        v = new Vector();
        v.addElement("HOURS");
        v.addElement("MINUTES");
        v.addElement("SECONDS");
        comboBox = new JComboBox(v);
        comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String oldPeriod = period;
                    String newPeriod = (String) comboBox.getSelectedItem();

                    pcs.firePropertyChange("Period", oldPeriod, newPeriod);
                    //              System.out.println("actionPerformed and model is : "+newPeriod);
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
