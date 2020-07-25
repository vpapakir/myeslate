package gr.cti.eslate.spinButton;


import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import java.util.*;


public class TimeRatePropertyEditor extends PropertyEditorSupport {

    private PropertyChangeSupport pcs;
    int period = TimeDataModel.SECONDSTIMERATE;
    JComboBox comboBox;
    Vector v;
    ResourceBundle bundleMessages;

    public TimeRatePropertyEditor() {
        super();
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.spinButton.SpinButtonBundleMessages", Locale.getDefault());
        pcs = new PropertyChangeSupport(this);
        v = new Vector();
        v.addElement(bundleMessages.getString("SECONDS"));
        v.addElement(bundleMessages.getString("MINUTES"));
        v.addElement(bundleMessages.getString("HOURS"));
        comboBox = new JComboBox(v);
        comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int oldPeriod = period;
                    int newPeriod = comboBox.getSelectedIndex();

                    pcs.firePropertyChange("Period", oldPeriod, newPeriod);
                    //              System.out.println("actionPerformed and model is : "+newPeriod);
                    period = newPeriod;
                }
            }
        );
    }

    public void setValue(Object value) {
        String s = value.toString();
        java.text.DecimalFormat formatter = (java.text.DecimalFormat) java.text.DecimalFormat.getInstance(Locale.getDefault());

        try {
            period = (formatter.parse(s)).intValue();
        } catch (java.text.ParseException e) {}
        comboBox.setSelectedIndex(period);
    }

    public Object getValue() {
        return new Integer(period);
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
