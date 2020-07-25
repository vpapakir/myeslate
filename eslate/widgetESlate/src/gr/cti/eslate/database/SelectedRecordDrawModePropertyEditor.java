/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Nov 12, 2002
 * Time: 7:54:19 PM
 * To change this template use Options | File Templates.
 */

package gr.cti.eslate.database;

import java.util.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import javax.swing.*;


public class SelectedRecordDrawModePropertyEditor extends PropertyEditorSupport {
    private PropertyChangeSupport pcs;
    protected String propertyName;
    private int drawMode = DBTable.ON_ROW_BAR_AND_TABLE;
    private JPanel editor;
    private JComboBox cb;

    /**
     * Localized resources.
     */
    static ResourceBundle bundle = ResourceBundle.getBundle(
      "gr.cti.eslate.database.DBTableBundle", Locale.getDefault());

    public SelectedRecordDrawModePropertyEditor() {
        propertyName = "SelectedRecordDrawMode";
        pcs = new PropertyChangeSupport(this);
        cb = new JComboBox(new String[] {
            bundle.getString("NoIndication"),
            bundle.getString("OnRowBarOnly"),
            bundle.getString("OnTableOnly"),
            bundle.getString("OnRowBarAndTable")
        });
        cb.setEditable(false);
        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionevent) {
                comboAction();
            }
        });
        editor = new JPanel();
        editor.setLayout(new BoxLayout(editor, 0));
        editor.add(cb);
    }

    private void comboAction() {
        int i = cb.getSelectedIndex();
        if (drawMode == DBTable.NO_INDICATION && i == 0)
            return;
        if (drawMode == DBTable.ON_ROW_BAR_ONLY && i == 1)
            return;
        if (drawMode == DBTable.ON_TABLE_ONLY && i == 2)
            return;
        if (drawMode == DBTable.ON_ROW_BAR_AND_TABLE && i == 3)
            return;

        if (i == 0)
            setValue(DBTable.NO_INDICATION);
        else if (i == 1)
            setValue(DBTable.ON_ROW_BAR_ONLY);
        else if (i == 2)
            setValue(DBTable.ON_TABLE_ONLY);
        else if (i == 3)
            setValue(DBTable.ON_ROW_BAR_AND_TABLE);
    }

    public void setValue(int mode) {
        int previousMode = drawMode;
        this.drawMode = mode;
        if (mode == DBTable.NO_INDICATION) {
            cb.setSelectedIndex(0);
        }else if (mode == DBTable.ON_ROW_BAR_ONLY) {
            cb.setSelectedIndex(1);
        }else if (mode == DBTable.ON_TABLE_ONLY) {
            cb.setSelectedIndex(2);
        }else if (mode == DBTable.ON_ROW_BAR_AND_TABLE) {
            cb.setSelectedIndex(3);
        }else{
            cb.setSelectedIndex(0);
        }
        pcs.firePropertyChange(propertyName, previousMode, drawMode);
    }

    public void setValue(Object value) {
        setValue(((Integer) value).intValue());
    }

    public Object getValue() {
        return new Integer(drawMode);
    }

    public Component getCustomEditor() {
        return editor;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertychangelistener) {
        pcs.addPropertyChangeListener(propertychangelistener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertychangelistener) {
        pcs.removePropertyChangeListener(propertychangelistener);
    }
}
