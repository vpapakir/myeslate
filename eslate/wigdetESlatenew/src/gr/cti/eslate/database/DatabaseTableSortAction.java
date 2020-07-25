package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Cursor;
import java.awt.Color;

public class DatabaseTableSortAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

    public DatabaseTableSortAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public DatabaseTableSortAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.activeDBTable == null) return;
        dBase.activeDBTable.getTableSortAction().actionPerformed(e);
//        TableSortDialog tid = new TableSortDialog(dBase.topLevelFrame, dBase.activeDBTable, dBase.dbComponent);
//        dBase.statusToolbarController.setMessageLabelInWaitState();
/*        dBase.dbComponent.setCursor(waitCursor);
        try{
            dBase.activeDBTable.sortOnMultipleFields(TableSortDialog.fieldsToSortOn, TableSortDialog.ascending);
        }catch (Throwable thr) {
            thr.printStackTrace();
        }
        dBase.dbComponent.setCursor(defaultCursor);
        dBase.statusToolbarController.setMessageLabelColor(Color.white);
*/
        dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miTableSort.setEnabled(b);
    }
}
