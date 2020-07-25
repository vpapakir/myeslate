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

public class FieldWidthAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public FieldWidthAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public FieldWidthAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        DBTable dBtable = dBase.activeDBTable;

        int minWidth, maxWidth;
        int[] indices =dBtable.jTable.getSelectedColumns();
        TableColumn col = dBtable.tableColumns.get(indices[0]).tableColumn;
        minWidth = col.getMinWidth();
        maxWidth = col.getMaxWidth();
        for (int i=1; i<indices.length; i++) {
            col = dBtable.tableColumns.get(indices[i]).tableColumn;
            if (minWidth != -1 && minWidth != col.getMinWidth())
                minWidth = -1;
            if (maxWidth != -1 && maxWidth != col.getMaxWidth())
                maxWidth = -1;
        }

        FieldWidthDialog fwd = new FieldWidthDialog(dBase.topLevelFrame, minWidth, maxWidth, dBase.dbComponent);

        if (FieldWidthDialog.clickedButton == 1) {
            boolean refreshNeeded = false;
            if (minWidth != FieldWidthDialog.minWidth) {
              if (FieldWidthDialog.minWidth != -1) {
                  minWidth = FieldWidthDialog.minWidth;
                  for (int i=0; i<indices.length; i++) {
                      TableColumn col1 = dBtable.tableColumns.get(indices[i]).tableColumn;
                      col1.setMinWidth(minWidth);
//1                      ((AbstractTableField) dBtable.tableFields.get(col1.getModelIndex())).UIProperties.put(1, new Integer(minWidth));
                  }
                  refreshNeeded = true;
              }
              dBtable.table.setModified();
            }
            if (maxWidth != FieldWidthDialog.maxWidth) {
              if (FieldWidthDialog.maxWidth != -1) {
                  maxWidth = FieldWidthDialog.maxWidth;
                  for (int i=0; i<indices.length; i++) {
                      TableColumn col1 = dBtable.tableColumns.get(indices[i]).tableColumn;
                      col1.setMaxWidth(maxWidth);
//1                      ((AbstractTableField) dBtable.tableFields.get(col1.getModelIndex())).UIProperties.put(2, new Integer(maxWidth));
                      col1.setResizable(true);
                  }
                  refreshNeeded = true;
              }
              dBtable.table.setModified();
            }

            if (refreshNeeded) {
                dBtable.jTable.getTableHeader().resizeAndRepaint();
                dBtable.jTable.sizeColumnsToFit(dBtable.tableColumns.size()-1);
                dBtable.jTable.revalidate();
                dBtable.refresh();
            }
        }
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miFieldWidth.setEnabled(b);
    }
}
