package gr.cti.eslate.database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TableSelectAllRecsAction extends AbstractAction {
    DBTable dbTable = null;

    public TableSelectAllRecsAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    final void execute() {
        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        /* If a jTable cell was edited when the button was pressed, then stop the cell editing.
         * This results in the evaluation of the edited value of the cell.
         */
        dbTable.stopCellEditing();

        dbTable.table.selectAll();
        dbTable.refresh();
    }
}
