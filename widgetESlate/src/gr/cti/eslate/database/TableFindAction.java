/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 30 Οκτ 2002
 * Time: 9:02:41 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.*;

public class TableFindAction extends AbstractAction {
    DBTable dbTable;

    public TableFindAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
System.out.println("TableFindAction");
        /* If a jTable cell was edited when the button was pressed, then stop the cell editing.
         * This results in the evaluation of the edited value of the cell.
         */
        dbTable.stopCellEditing();
        execute();
    }

    protected void execute() {
        Frame frame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, dbTable);
        FindDialog findDialog = dbTable.getFindDialog();
        findDialog.display(dbTable);
        dbTable.jTable.requestFocus();
    }
}
