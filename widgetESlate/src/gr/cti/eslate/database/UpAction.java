package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.table.TableColumn;
import gr.cti.eslate.database.engine.*;
import java.awt.Cursor;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.*;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import java.awt.Color;
import java.awt.Point;
import com.objectspace.jgl.Array;

public class UpAction extends AbstractAction {
    DBTable dbTable;

    public UpAction(DBTable table, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false));
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        Database dbComponent = dbTable.dbComponent;
        JTable jTable = dbTable.jTable;
        Table table = dbTable.table;
        DBTableColumnBaseArray tableColumns = dbTable.tableColumns;

        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();

        /* Handles editing of the JComboBox, which displays boolean values. This code is executed
         * only for edited cells of Boolean fields.
         */
        if (dbTable.isEditing()) {
            if (tableColumns.get(jTable.getEditingColumn()).tableColumn.getCellEditor() != null) {
                Component editorComponent = ((DefaultCellEditor) tableColumns.get(jTable.getEditingColumn()).tableColumn.getCellEditor()).getComponent();
                if (BooleanValues.class.isInstance(editorComponent)) {
                    BooleanValues ec = (BooleanValues) editorComponent;
                    int col = jTable.getEditingColumn();
                    int row = jTable.getEditingRow();
//                    int index = ec.getSelectedIndex();
                    ec.selectPrevious();
                    dbTable.editCellAt(row, col);
                    return;
                }
            }
        }

        int recordToActivate = -1;
        if (dbTable.table.getRecordEntryStructure().isRecordAdditionPending()) {
            recordToActivate = dbTable.table.getRecordEntryStructure().getPendingRecordIndex()-1;
            if (!dbTable.commitNewRecord())
                return;
        }

        if (table.getRecordCount() == 0)
            return;

//1        int selCol = jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();

/* When "simultaneousFieldRecordActivation" is "false", column selection is not allowed.
         * However in the special case, when a field header is clicked, we set "columnSelectionAllowed"
         * to "true", so that the column is selected. "columnSelectionAllowed" turns again to "false"
         * when VK_LEFT/VK_RIGHT/VK_UP/VK_DOWN is pressed or the user clicks inside the jTable.
         * Turning "columnSelectionAllowed" to false, resets all te existing selections (both
         * row and column) in the jTable, so we keep track of them and reselect them afterwards.
         */
/*1        if (jTable.getColumnSelectionAllowed() != dbTable.isSimultaneousFieldRecordActivation()) {
            int[] selectedRows = jTable.getSelectedRows();
//            int selectedColumn = -1;
//            selectedColumn = jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
            jTable.setColumnSelectionAllowed(false);
            for (int i=0; i<dbTable.colSelectionStatus.size(); i++) {
                if (((Boolean) dbTable.colSelectionStatus.get(i)).booleanValue())
                    jTable.getColumnModel().getSelectionModel().setSelectionInterval(i, i);
            }
//            jTable.getColumnModel().getSelectionModel().clearSelection();
//            if (selectedColumn != -1)
//                jTable.getColumnModel().getSelectionModel().setSelectionInterval(selectedColumn, selectedColumn);
            for (int k=0; k<selectedRows.length; k++)
                jTable.getSelectionModel().addSelectionInterval(selectedRows[k], selectedRows[k]);
        }
1*/
        if (recordToActivate == -1) {
            int currActiveRecord = table.getActiveRecord();
            if (currActiveRecord == -1)
                recordToActivate = table.recordForRow(0);
            else{
                int currActiveRow = dbTable.activeRow;
                if (currActiveRow == 0)
                    recordToActivate = table.recordForRow(table.getRecordCount()-1); // Wrap
                else
                    recordToActivate = table.recordForRow(currActiveRow-1);
            }
        }
        table.setActiveRecord(recordToActivate);
    }
}
