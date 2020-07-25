/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 30 Οκτ 2002
 * Time: 8:22:06 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class TableCopyAction extends AbstractAction {
    DBTable dbTable = null;

    public TableCopyAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    void execute() {
        dbTable.copyCellContents(dbTable.activeRow, dbTable.jTable.getSelectedColumn());
    }

}
