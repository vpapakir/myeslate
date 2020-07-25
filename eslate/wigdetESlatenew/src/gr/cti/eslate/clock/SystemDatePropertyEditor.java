package gr.cti.eslate.clock;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyEditorSupport;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.awt.Dimension;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import gr.cti.eslate.utils.*;


public class SystemDatePropertyEditor extends PropertyEditorSupport {
    PropertyChangeSupport pcs;
    ResourceBundle bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.clock.ClockBundleMessages", Locale.getDefault());
    NoBorderButton button;
    boolean b = true;
    Boolean fired = new Boolean(b);
    static boolean changedValues;
    int size;

    Object item = new Object();

    public SystemDatePropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void setValue(Object value) {
        fired = (Boolean) value;
    }

    public Object getValue() {
        return fired;
    }

    public java.awt.Component getCustomEditor() {

        button = new NoBorderButton(bundleMessages.getString("SystemDate"));
        button.setMargin(new Insets(0, 0, 0, 0));

        Dimension buttonSize = new Dimension(80, 22);

        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Boolean oldfired = new Boolean(false);

                    pcs.firePropertyChange("SystemDate", oldfired, fired);
                }
            }
        );
        return button;
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
