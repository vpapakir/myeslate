package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.table.TableColumn;
import javax.swing.*;

import gr.cti.typeArray.IntBaseArray;

public class TableRemoveSelRecordAction extends AbstractAction {
    DBTable dbTable;
    String actionName;

    public TableRemoveSelRecordAction(DBTable dbTable, String name) {
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.SHIFT_MASK, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        /* If a jTable cell was edited when the button was pressed, then stop the cell editing.
         * This results in the evaluation of the edited value of the cell.
         */
        dbTable.stopCellEditing();

        IntBaseArray recIndices = dbTable.table.getSelectedSubset();
        if (recIndices.size() == 0) return;

        Object[] conf_no = {dbTable.bundle.getString("Confirmation"), dbTable.bundle.getString("Cancel")};

        JOptionPane pane = new JOptionPane(dbTable.bundle.getString("DatabaseMsg76"),
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION,
            UIManager.getIcon("OptionPane.questionIcon"),
            conf_no,
            dbTable.bundle.getString("Confirmation"));
        JDialog dialog = pane.createDialog(dbTable, dbTable.bundle.getString("Confirm removal"));
        dialog.show();
        Object option = pane.getValue();

        if (option != conf_no[0])
            return;

        dbTable.removeSelectedRecords();
    }

}
