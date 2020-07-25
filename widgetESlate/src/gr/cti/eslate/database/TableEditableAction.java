/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Dec 23, 2002
 * Time: 2:41:08 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class TableEditableAction extends AbstractAction {
    DBTable dbTable = null;

    public TableEditableAction(DBTable dbTable, String name){
        super(name);
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
    }

    void execute() {
        try{
            dbTable.table.setDataChangeAllowed(!dbTable.table.isDataChangeAllowed());
        }catch (Throwable thr) {
            thr.printStackTrace();
        }
    }
}