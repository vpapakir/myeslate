package gr.cti.eslate.database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class TableFieldSelectAllAction extends AbstractAction {
    DBTable dbTable = null;

    public TableFieldSelectAllAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK | InputEvent.SHIFT_MASK, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        int columnCount;
        if ((columnCount = dbTable.tableColumns.size()) > 0) {
            int[] selectedRows = dbTable.jTable.getSelectedRows();
            int selectedColumn = -1;
            selectedColumn = dbTable.jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
            if (!dbTable.isSimultaneousFieldRecordActivation())
            dbTable.jTable.setColumnSelectionAllowed(true);
            for (int k=0; k<selectedRows.length; k++)
                dbTable.jTable.getSelectionModel().addSelectionInterval(selectedRows[k], selectedRows[k]);

            for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                dbTable.colSelectionStatus.set(m, Boolean.TRUE);
            dbTable.jTable.getColumnModel().getSelectionModel().setSelectionInterval(0, columnCount-1); //setColumnSelectionInterval(0, columnCount-1);
            if (selectedColumn != -1) {
                dbTable.colSelectionStatus.set(selectedColumn, Boolean.TRUE);
                dbTable.jTable.getColumnModel().getSelectionModel().addSelectionInterval(selectedColumn, selectedColumn);
            }
        }
    }
}
