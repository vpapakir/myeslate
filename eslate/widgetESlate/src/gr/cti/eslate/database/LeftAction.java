package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import gr.cti.eslate.database.engine.*;
import com.objectspace.jgl.Array;

public class LeftAction extends AbstractAction {
    DBTable dbTable;

    public LeftAction(DBTable table, String name) {
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false));
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        if (dbTable.table.getRecordCount() == 0)
            return;

        int i = dbTable.jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
//1        Array columnOrder = dbTable.viewStructure.tableView.getColumnOrder();

        if (dbTable.isEditing()) {
            dbTable.stopCellEditing();
        }

        /* Find the previous in series non-hidden column that will be selected.
         */
        int prevColIndex = i-1;
        if (!dbTable.table.isHiddenFieldsDisplayed()) {
            while (prevColIndex >= 0) {
//1                try{
                    AbstractTableField fld1 = dbTable.getTableField(prevColIndex);
//1                    AbstractTableField fld1 = dbTable.table.getTableField(((Integer) columnOrder.at(prevColIndex)).intValue());
                    if (!fld1.isHidden()) {
                        break;
                    }
//1                }catch (InvalidFieldIndexException exc) {
//1                    System.out.println("Serious inconsistency error in DBTable DBTable(): 1.5");
//1                }
                prevColIndex--;
            }
        }

        if (prevColIndex >= 0) {
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
                for (int k=0; k<selectedRows.length; k++)
                    dbTable.jTable.getSelectionModel().addSelectionInterval(selectedRows[k], selectedRows[k]);
            }

            for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                dbTable.colSelectionStatus.set(m, Boolean.FALSE);
            dbTable.colSelectionStatus.set(prevColIndex, Boolean.TRUE);
            dbTable.jTable.getColumnModel().getSelectionModel().setSelectionInterval(prevColIndex, prevColIndex);
            dbTable.jTable.scrollRectToVisible(dbTable.jTable.getCellRect(dbTable.activeRow, prevColIndex, true));
        }else{
            if (dbTable.table.getRecordEntryStructure().isRecordAdditionPending()) {
                if (!dbTable.commitNewRecord())
                    return;
            }
/*1            if (dbTable.dbComponent.pendingNewRecord)
                if (!dbTable.checkAndStorePendingRecord2())
                    return;
1*/
            /* Find the first non-hidden column in the DBTable, starting from its end, if
             * the hidden fields are not displayed.
             */
            prevColIndex = dbTable.jTable.getColumnCount()-1;
            if (!dbTable.table.isHiddenFieldsDisplayed()) {
                while (prevColIndex >= 0) {
//1                    try{
                        AbstractTableField fld1 = dbTable.getTableField(prevColIndex);
//1                        AbstractTableField fld1 = dbTable.table.getTableField(((Integer) columnOrder.at(prevColIndex)).intValue());
                        if (!fld1.isHidden()) {
                            break;
                        }
//1                    }catch (InvalidFieldIndexException exc) {
//1                        System.out.println("Serious inconsistency error in DBTable DBTable(): 1.6");
//1                    }
                    prevColIndex--;
                }
            }
            if (prevColIndex >= 0) {
                int prevRow = dbTable.activeRow - 1;
                if (prevRow >= 0) {
                    for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                        dbTable.colSelectionStatus.set(m, Boolean.FALSE);
                    dbTable.colSelectionStatus.set(prevColIndex, Boolean.TRUE);
                    dbTable.jTable.getColumnModel().getSelectionModel().setSelectionInterval(prevColIndex, prevColIndex);
                    dbTable.jTable.scrollRectToVisible(dbTable.jTable.getCellRect(prevRow, prevColIndex, true));
                    dbTable.setActiveRow(prevRow);
                }
            }
        }
    }

}
