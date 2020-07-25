package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import gr.cti.eslate.database.engine.*;
import com.objectspace.jgl.Array;

public class RightAction extends AbstractAction {
    DBTable dbTable;
    String actionName;

    public RightAction(DBTable table, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false));
        dbTable = table;
    }

    public RightAction(DBTable table, String name, Icon iconEnabled, Icon iconDisabled) {
        super(name);
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        if (dbTable.table.getRecordCount() == 0)
            return;

        int i = dbTable.jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
//1        Array columnOrder = dbTable.viewStructure.tableView.getColumnOrder();

        dbTable.stopCellEditing();

        /* Find the next in series non-hidden column that will be selected.
         */
        int nextColIndex = i+1;
        if (!dbTable.table.isHiddenFieldsDisplayed()) {
            while (nextColIndex <= dbTable.jTable.getColumnCount()-1) {
//1                try{
                    AbstractTableField fld1 = dbTable.getTableField(nextColIndex);
//1                    AbstractTableField fld1 = dbTable.table.getTableField(((Integer) columnOrder.at(nextColIndex)).intValue());
                    if (!fld1.isHidden()) {
                        break;
                    }
//1                }catch (InvalidFieldIndexException exc) {
//1                    System.out.println("Serious inconsistency error in DBTable DBTable(): 1.5");
//1                }
                nextColIndex++;
            }
       }

        if (nextColIndex <= dbTable.jTable.getColumnCount()-1) {
/* When "simultaneousFieldRecordActivation" is "false", column selection is not allowed.
             * However in the special case, when a field header is clicked, we set "columnSelectionAllowed"
             * to "true", so that the column is selected. "columnSelectionAllowed" turns again to "false"
             * when VK_LEFT/VK_RIGHT/VK_UP/VK_DOWN is pressed or the user clicks inside the jTable.
             * Turning "columnSelectionAllowed" to false, resets all te existing selections (both
             * row and column) in the jTable, so we keep track of them and reselect them afterwards.
             */
            if (dbTable.jTable.getColumnSelectionAllowed() != dbTable.isSimultaneousFieldRecordActivation()) {
                int[] selectedRows = dbTable.jTable.getSelectedRows();
                dbTable.jTable.setColumnSelectionAllowed(false);
                for (int z=0; z<dbTable.colSelectionStatus.size(); z++) {
                    if (((Boolean) dbTable.colSelectionStatus.get(z)).booleanValue())
                        dbTable.jTable.getColumnModel().getSelectionModel().setSelectionInterval(z, z);
                }
                for (int k=0; k<selectedRows.length; k++) {
                    dbTable.jTable.getSelectionModel().addSelectionInterval(selectedRows[k], selectedRows[k]);
                }
            }

            for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                dbTable.colSelectionStatus.set(m, Boolean.FALSE);
            dbTable.colSelectionStatus.set(nextColIndex, Boolean.TRUE);
            dbTable.jTable.getColumnModel().getSelectionModel().setSelectionInterval(nextColIndex, nextColIndex);
            dbTable.jTable.scrollRectToVisible(dbTable.jTable.getCellRect(dbTable.activeRow, nextColIndex, true));
        }else{
            if (dbTable.table.getRecordEntryStructure().isRecordAdditionPending()) {
                if (!dbTable.commitNewRecord())
                    return;
            }
/*            if (dbTable.dbComponent.pendingNewRecord)
                if (!dbTable.checkAndStorePendingRecord2())
                    return;
*/
            /* Find the first non-hidden column in the DBTable, if hidden fields
             * are not displayed.
             */
            nextColIndex = 0;
            if (!dbTable.table.isHiddenFieldsDisplayed()) {
                while (nextColIndex <= dbTable.jTable.getColumnCount()-1) {
//1                    try{
                        AbstractTableField fld1 = dbTable.getTableField(nextColIndex);
//                        AbstractTableField fld1 = dbTable.table.getTableField(((Integer) columnOrder.at(nextColIndex)).intValue());
                        if (!fld1.isHidden()) {
                            break;
                        }
//1                    }catch (InvalidFieldIndexException exc) {
//1                        System.out.println("Serious inconsistency error in DBTable DBTable(): 1.6");
//1                    }
                    nextColIndex++;
                }
            }
            if (nextColIndex != dbTable.jTable.getColumnCount()) {
                int nextRow = dbTable.activeRow + 1;
                if (nextRow < dbTable.table.getRecordCount()) {
                    for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                        dbTable.colSelectionStatus.set(m, Boolean.FALSE);
                    dbTable.colSelectionStatus.set(nextColIndex, Boolean.TRUE);
                    dbTable.jTable.getColumnModel().getSelectionModel().setSelectionInterval(nextColIndex, nextColIndex);
                    dbTable.jTable.scrollRectToVisible(dbTable.jTable.getCellRect(nextRow, nextColIndex, true));
                    dbTable.setActiveRow(nextRow);
                }
            }
        }
    }
}
