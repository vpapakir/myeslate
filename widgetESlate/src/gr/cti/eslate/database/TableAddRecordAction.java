package gr.cti.eslate.database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TableAddRecordAction extends AbstractAction {
    DBTable dbTable;
    String actionName;

    public TableAddRecordAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
System.out.println("TableAddRecordAction");
        execute();
    }

    void execute() {
        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        dbTable.tipManager.resetTip();
        /* If a jTable cell was edited when the button was pressed, then stop the cell editing.
         * This results in the evaluation of the edited value of the cell.
         */
        dbTable.stopCellEditing();
        dbTable.newRecord();
    }
}
