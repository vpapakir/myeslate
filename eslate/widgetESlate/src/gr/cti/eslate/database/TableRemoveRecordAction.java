/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 29 Οκτ 2002
 * Time: 7:43:13 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class TableRemoveRecordAction extends AbstractAction {
    DBTable dbTable = null;

    public TableRemoveRecordAction(DBTable dbTable, String name) {
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE"));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        int activeRow = dbTable.activeRow;
        if (activeRow == -1) return;

        Object[] conf_no = {dbTable.bundle.getString("Confirmation"), dbTable.bundle.getString("Cancel")};
        JOptionPane pane = new JOptionPane(dbTable.bundle.getString("DBTableMsg8"),
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION,
            UIManager.getIcon("OptionPane.questionIcon"),
            conf_no,
            dbTable.bundle.getString("Confirmation"));
        JDialog dialog = pane.createDialog(dbTable, dbTable.bundle.getString("Confirm removal"));
        dialog.show();
        Object option = pane.getValue();
        if (option == conf_no[0])
            dbTable.removeActiveRecord();
    }
}
