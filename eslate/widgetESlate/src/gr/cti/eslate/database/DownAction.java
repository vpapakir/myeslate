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

public class DownAction extends AbstractAction {
    DBTable dbTable;

    public DownAction(DBTable table, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false));
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        Database dbComponent = dbTable.dbComponent;
        Table table = dbTable.table;
        JTable jTable = dbTable.jTable;
        int activeRow = dbTable.activeRow;
//        DBTableColumnBaseArray tableColumns = dbTable.tableColumns;

        dbTable.closeDatabasePopupMenu();
        dbTable.tipManager.resetTip();
        if (table.getRecordCount() == 0)
            return;
        /* Handles editing of the JComboBox, which displays boolean values. This code is executed
         * only for edited cells of Boolean fields.
         */
/*1        if (dbTable.isEditing) {
            if (((TableColumn) tableColumns.get(jTable.getEditingColumn())).getCellEditor() != null) {
                Component editorComponent = ((DefaultCellEditor) ((TableColumn) tableColumns.get(jTable.getEditingColumn())).getCellEditor()).getComponent();
                if (BooleanValues.class.isInstance(editorComponent)) {
                    BooleanValues ec = (BooleanValues) editorComponent;
                    int col = jTable.getEditingColumn();
                    int row = jTable.getEditingRow();
                    ec.selectNext(); //setSelectedIndex(index + 1);
                    dbTable.editCellAt(row, col);
                    return;
                }
            }
        }
*/
        int selCol = jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();

        /* If the jTable is being edited, stop cell editing.
         */
        dbTable.stopCellEditing();

/* When "simultaneousFieldRecordActivation" is "false", column selection is not allowed.
         * However in the special case, when a field header is clicked, we set "columnSelectionAllowed"
         * to "true", so that the column is selected. "columnSelectionAllowed" turns again to "false"
         * when VK_LEFT/VK_RIGHT/VK_UP/VK_DOWN is pressed or the user clicks inside the jTable.
         * Turning "columnSelectionAllowed" to false, resets all te existing selections (both
         * row and column) in the jTable, so we keep track of them and reselect them afterwards.
         */
/*1        if (jTable.getColumnSelectionAllowed() != dbTable.isSimultaneousFieldRecordActivation()) {
            int[] selectedRows = jTable.getSelectedRows();
            jTable.setColumnSelectionAllowed(false);
            for (int i=0; i<dbTable.colSelectionStatus.size(); i++) {
                if (((Boolean) dbTable.colSelectionStatus.get(i)).booleanValue())
                    jTable.getColumnModel().getSelectionModel().setSelectionInterval(i, i);
            }
            for (int k=0; k<selectedRows.length; k++)
                jTable.getSelectionModel().addSelectionInterval(selectedRows[k], selectedRows[k]);
        }
*/
        int currActiveRecord = table.getActiveRecord();
        int recordToActivate = -1;
        if (currActiveRecord == -1)
            recordToActivate = table.recordForRow(0);
        else{
            if (activeRow == table.getRecordCount()-1)
                recordToActivate = table.recordForRow(0);  // back to top
            else
                recordToActivate = table.recordForRow(activeRow+1);
        }
        table.setActiveRecord(recordToActivate);
    }
}
