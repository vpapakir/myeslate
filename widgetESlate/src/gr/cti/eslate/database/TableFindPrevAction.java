/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 31 ��� 2002
 * Time: 10:46:27 ��
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.CellAddress;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TableFindPrevAction extends AbstractAction {
    DBTable dbTable;
    boolean startFromBottom = false;

    public TableFindPrevAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, KeyEvent.SHIFT_MASK, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        dbTable.closeDatabasePopupMenu();
        /* If a jTable cell was edited when the button was pressed, then stop the cell editing.
         * This results in the evaluation of the edited value of the cell.
         */
        dbTable.stopCellEditing();

        dbTable.tipManager.resetTip();
        if (dbTable.searchManager.isEnabled()) {
            int rowToSearchFrom = (startFromBottom)? dbTable.table.getRecordCount()-1:dbTable.activeRow;
            boolean includeCell = (startFromBottom)? true:false;
            int fieldToStartFrom = (startFromBottom)? dbTable.jTable.getColumnCount()-1:dbTable.jTable.getSelectedColumn();
            CellAddress cell = dbTable.find(rowToSearchFrom, fieldToStartFrom, includeCell, true);
            if (cell != null) {
                dbTable.setActiveCell(cell.recordIndex, cell.fieldIndex, true);
                startFromBottom = false;
            }else{
                int y = dbTable.scrollpane.getViewport().getExtentSize().height/2;
                if (dbTable.activeRow != -1)
                    y = dbTable.jTable.getCellRect(dbTable.activeRow, 0, false).y - dbTable.scrollpane.getViewport().getViewPosition().y;
                if (startFromBottom)
                    dbTable.tipManager.showTip('\"' + dbTable.getSearchManager().getToken() + '\"' + dbTable.bundle.getString("Not Found"), 50, 20);
                else
                    dbTable.tipManager.showTip(dbTable.bundle.getString("StartSearchFromBottom1") + '\"' + dbTable.getSearchManager().getToken() + '\"' + dbTable.bundle.getString("StartSearchFromBottom2"), 50, y);
                startFromBottom = true;
            }
        }
   }

}
