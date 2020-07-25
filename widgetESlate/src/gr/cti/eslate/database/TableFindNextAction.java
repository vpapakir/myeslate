package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.CellAddress;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TableFindNextAction extends AbstractAction {
    DBTable dbTable;
    boolean startOver = false;

    public TableFindNextAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0, false));
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
            int rowToSearchFrom = (startOver)? 0:dbTable.activeRow;
            boolean includeCell = (startOver)? true:false;
            int fieldToStartFrom = (startOver)? 0:dbTable.jTable.getSelectedColumn();
            CellAddress cell = dbTable.find(rowToSearchFrom, fieldToStartFrom, includeCell, false);
            if (cell != null) {
                dbTable.setActiveCell(cell.recordIndex, cell.fieldIndex, true);
                startOver = false;
            }else{
                int y = dbTable.scrollpane.getViewport().getExtentSize().height/2;
                if (dbTable.activeRow != -1)
                    y = dbTable.jTable.getCellRect(dbTable.activeRow, 0, false).y - dbTable.scrollpane.getViewport().getViewPosition().y;

                if (startOver)
                    dbTable.tipManager.showTip('\"' + dbTable.getSearchManager().getToken() + '\"' + dbTable.bundle.getString("Not Found"), 50, 20);
                else
                    dbTable.tipManager.showTip(dbTable.bundle.getString("StartSearchFromTop1") + '\"' + dbTable.getSearchManager().getToken() + '\"' + dbTable.bundle.getString("StartSearchFromTop2"), 50, y);
                startOver = true;
            }
        }
    }

}
