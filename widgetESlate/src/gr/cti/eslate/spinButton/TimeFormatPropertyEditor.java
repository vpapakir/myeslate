package gr.cti.eslate.spinButton;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import java.util.*;


public class TimeFormatPropertyEditor extends PropertyEditorSupport {

    private PropertyChangeSupport pcs;
    String format = "";
    JComboBox comboBox;
    Vector v;

    public TimeFormatPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        v = new Vector();
        v.addElement("hh:mm:ss");
        v.addElement("hh:mm:ss aaa");
        v.addElement("hh:mm aaa");
        v.addElement("hh:mm");
        v.addElement("H:mm");
        comboBox = new JComboBox(v);
        comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String oldFormat = format;
                    String newFormat = (String) comboBox.getSelectedItem();

                    pcs.firePropertyChange("Format", oldFormat, newFormat);
                    //              System.out.println("actionPerformed and model is : "+newFormat);
                    format = newFormat;
                }
            }
        );
    }

    public void setValue(Object value) {
        if (!String.class.isInstance(value))
            return;
        //        if ((model!=null) && model.equals(value))
        //            return;
        format = (String) value;
        //        if (model !=null)
        comboBox.setSelectedItem(format);
        //            System.out.println("Selected format is : "+format);
    }

    public Object getValue() {
        //        model = (String) comboBox.getSelectedItem();
        //         System.out.println("getValue called and format is : "+format);
        return (String) format;
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
