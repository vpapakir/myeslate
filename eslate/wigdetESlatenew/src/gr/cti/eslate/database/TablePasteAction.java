/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 30 ��� 2002
 * Time: 8:24:40 ��
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TablePasteAction extends AbstractAction {
    DBTable dbTable = null;

    public TablePasteAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK, false));
        this.dbTable = dbTable;
    }


    public void actionPerformed(ActionEvent e) {
        execute();
    }

    void execute() {
        dbTable.pasteCellContents(dbTable.activeRow, dbTable.jTable.getSelectedColumn());
    }
}
