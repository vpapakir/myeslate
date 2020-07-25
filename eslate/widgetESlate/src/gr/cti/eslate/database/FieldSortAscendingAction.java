/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 7 Íןו 2002
 * Time: 5:08:11 לל
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.AbstractTableField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.*;

public class FieldSortAscendingAction extends AbstractAction {
    DBTable dbTable = null;

    public FieldSortAscendingAction(DBTable dbTable, String name) {
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
//        dbTable.clickCounter.stop();
        int column = dbTable.jTable.getSelectedColumn();
        if (column == -1) return;
        int fieldIndex = dbTable.jTable.convertColumnIndexToModel(column);
        AbstractTableField field = dbTable.table.getFields().get(fieldIndex);
        try{
            dbTable.sortOnField(field.getName(), true, 0,
                    dbTable.table.getRecordCount()-1, false);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
