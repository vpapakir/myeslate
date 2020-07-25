package gr.cti.eslate.database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import gr.cti.eslate.database.engine.*;

public class HomeAction extends AbstractAction {
    DBTable dbTable;

    public HomeAction(DBTable table, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0, false));
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        Table ctable = dbTable.table;
        JTable table = dbTable.jTable;
//fc        ArrayList tableColumns = dbTable.tableColumns;

        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();

        if (dbTable.isEditing()) {
            dbTable.stopCellEditing();
            return;
        }else{
            /* Sometimes the DELETE key starts cell editing ??? */
            dbTable.cancelCellEditing();
        }

        if (dbTable.table.getRecordEntryStructure().isRecordAdditionPending()) {
            if (!dbTable.commitNewRecord())
                return;
        }
/*1        if (dbComponent.pendingNewRecord)
            if (!dbTable.checkAndStorePendingRecord2())
                return;
1*/
        int recordCount = ctable.getRecordCount();
        if (recordCount == 0)
            return;

        int i;
        if (table.getSelectedColumn() != -1)
            i = table.getSelectedColumn();
        else
            i =0;
        table.scrollRectToVisible(table.getCellRect(0, i, true));

/* When "simultaneousFieldRecordActivation" is "false", column selection is not allowed.
         * However in the special case, when a field header is clicked, we set "columnSelectionAllowed"
         * to "true", so that the column is selected. "columnSelectionAllowed" turns again to "false"
         * when VK_LEFT/VK_RIGHT/VK_UP/VK_DOWN is pressed or the user clicks inside the jTable.
         * Turning "columnSelectionAllowed" to false, resets all te existing selections (both
         * row and column) in the jTable, so we keep track of them and reselect them afterwards.
         */
/*1        if (table.getColumnSelectionAllowed() != dbTable.isSimultaneousFieldRecordActivation()) {
            int[] selectedRows = table.getSelectedRows();
//            int selectedColumn = -1;
//            selectedColumn = jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
            table.setColumnSelectionAllowed(false);
            for (int z=0; z<dbTable.colSelectionStatus.size(); z++) {
                if (((Boolean) dbTable.colSelectionStatus.get(z)).booleanValue())
                    table.getColumnModel().getSelectionModel().setSelectionInterval(z, z);
            }
//            jTable.getColumnModel().getSelectionModel().clearSelection();
//            if (selectedColumn != -1)
//                jTable.getColumnModel().getSelectionModel().setSelectionInterval(selectedColumn, selectedColumn);
            for (int k=0; k<selectedRows.length; k++)
                table.getSelectionModel().addSelectionInterval(selectedRows[k], selectedRows[k]);
        }
1*/
/*1        if (dbTable.activeRow != 0)
            dbTable.setActiveRow(0);
1*/
    }
}
