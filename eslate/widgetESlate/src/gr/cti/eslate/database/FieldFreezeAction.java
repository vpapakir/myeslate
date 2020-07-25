package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import gr.cti.eslate.database.engine.*;
import java.awt.Cursor;
import javax.swing.JFrame;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import java.awt.Color;

public class FieldFreezeAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public FieldFreezeAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public FieldFreezeAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        int[] indices = dBase.activeDBTable.jTable.getSelectedColumns();
        TableColumn col = dBase.activeDBTable.tableColumns.get(indices[0]).tableColumn;
//cf        AbstractTableField f = (AbstractTableField) dBase.activeDBTable.tableFields.get(col.getModelIndex());
        int colWidth = col.getPreferredWidth();
        if (dBase.menu.miFieldFreeze.isSelected()) {
/*
            col.setMinWidth(colWidth);
            col.setMaxWidth(colWidth);
*/
            col.setResizable(false);
//1            f.UIProperties.put(1, new Integer(colWidth));
//1            f.UIProperties.put(2, new Integer(colWidth));
            for (int i=1; i<indices.length; i++) {
                col = dBase.activeDBTable.tableColumns.get(indices[i]).tableColumn;
//cf                f = (AbstractTableField) dBase.activeDBTable.tableFields.get(col.getModelIndex());
/*
                colWidth = col.getPreferredWidth();
                col.setMinWidth(colWidth);
                col.setMaxWidth(colWidth);
*/
//1                f.UIProperties.put(1, new Integer(colWidth));
//1                f.UIProperties.put(2, new Integer(colWidth));
            }
        }else{
            col.setResizable(true);
/*
            col.setMinWidth(40);
            col.setMaxWidth(1000);
*/
//1            f.UIProperties.put(1, new Integer(40));
//1            f.UIProperties.put(2, new Integer(1000));
            for (int i=1; i<indices.length; i++) {
                col = dBase.activeDBTable.tableColumns.get(indices[i]).tableColumn;
//cf                f = (AbstractTableField) dBase.activeDBTable.tableFields.get(col.getModelIndex());
/*
                col.setMinWidth(40);
                col.setMaxWidth(1000);
*/
//1                f.UIProperties.put(1, new Integer(40));
//1                f.UIProperties.put(2, new Integer(1000));
            }
        }
        dBase.activeDBTable.table.setModified();
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miFieldFreeze.setEnabled(b);
    }
}
