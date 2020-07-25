/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 30 Οκτ 2002
 * Time: 8:15:44 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class TableCutAction extends AbstractAction {
    DBTable dbTable = null;

    public TableCutAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
    }

    void execute() {
        dbTable.cutCellContents(dbTable.activeRow, dbTable.jTable.getSelectedColumn());
    }
}
