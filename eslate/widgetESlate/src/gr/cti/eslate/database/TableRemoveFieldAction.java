/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 7 Íןו 2002
 * Time: 5:34:10 לל
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.table.TableColumn;
import javax.swing.*;

public class TableRemoveFieldAction extends AbstractAction {
    DBTable dbTable;

    public TableRemoveFieldAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_MASK, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        dbTable.cancelCellEditing();

        int[] selectedColumnIndices = dbTable.jTable.getSelectedColumns();
        if (selectedColumnIndices.length != 0) {
            Object[] conf_no = {dbTable.bundle.getString("Confirmation"), dbTable.bundle.getString("Cancel")};

            JOptionPane pane = new JOptionPane(dbTable.bundle.getString("DatabaseMsg75"),
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
        }

        int index;
        int k = 0;
        for (int i=0; i<selectedColumnIndices.length; i++) {
            index = selectedColumnIndices[i]-i+k;
            String identifier = (String) dbTable.tableColumns.get(index).tableColumn.getIdentifier(); //tableModel.getColumnIdentifier(index);
            if (!dbTable.removeColumn(identifier))
                k++;
        }

        for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
            dbTable.colSelectionStatus.set(m, Boolean.FALSE);
        dbTable.jTable.getColumnModel().getSelectionModel().clearSelection();

    }
}
