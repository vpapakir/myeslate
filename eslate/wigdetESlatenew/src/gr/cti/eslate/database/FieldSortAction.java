/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 7 Íןו 2002
 * Time: 4:30:26 לל
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.AbstractTableField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class FieldSortAction extends AbstractAction {
    DBTable dbTable = null;

    public FieldSortAction(DBTable dbTable, String name) {
        super(name);
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
            boolean asc = false;
            if (field.getSortDirection() == AbstractTableField.ASCENDING)
                asc = false;
            else
                asc = true;
//System.out.println("FieldSortAction asc: " + asc + ", field.getSortDirection() == AbstractTableField.ASCENDING: " + (field.getSortDirection() == AbstractTableField.ASCENDING));
            dbTable.sortOnField(field.getName(), asc, 0,
                    dbTable.table.getRecordCount()-1, false);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
