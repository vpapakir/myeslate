package gr.cti.eslate.database;

import javax.swing.JTextField;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.beans.PropertyEditorSupport;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.Locale;


public class TaggedUserModePropEditor extends PropertyEditorSupport {
    Integer mode;
    PropertyChangeSupport pcs;
    ResourceBundle bundle;
    String noviceStr, advancedStr;

    public TaggedUserModePropEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        bundle=ResourceBundle.getBundle("gr.cti.eslate.database.TaggedUserModePropEditorBundle", Locale.getDefault());
        noviceStr = bundle.getString("Novice");
        advancedStr = bundle.getString("Advanced");
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
        if (!Integer.class.isInstance(value))
            return;
        if (mode != null && mode.equals(value)) return;

        Integer oldMode = mode;
        this.mode = (Integer) value;
        pcs.firePropertyChange("mode", oldMode, mode);
        
    }

    public Object getValue() {
        return mode;
    }

    public java.awt.Component getCustomEditor() {
        return null;
    }

    public String getAsText() {
        int m = mode.intValue();
        if (m == Database.NOVICE_USER_MODE)
            return noviceStr;
        else
            return advancedStr;
    }

    public void setAsText(String value) {
        Integer prevMode = mode;
        if (value.toLowerCase().equals(noviceStr.toLowerCase())) {
            if (mode.intValue() == Database.NOVICE_USER_MODE) return;
            mode = new Integer(Database.NOVICE_USER_MODE);
        }else if (value.toLowerCase().equals(advancedStr.toLowerCase())) {
            if (mode.intValue() == Database.ADVANCED_USER_MODE) return;
            mode = new Integer(Database.ADVANCED_USER_MODE);
        }else
            throw new IllegalArgumentException("Bad value for userMode. Should be one of \"" + noviceStr + "\", \"" + advancedStr + "\"");
        pcs.firePropertyChange("userMode", prevMode, mode);
    }


    public boolean supportsCustomEditor() {
        return false;
    }

    public String[] getTags() {
        return new String[] {noviceStr, advancedStr};
    }
}