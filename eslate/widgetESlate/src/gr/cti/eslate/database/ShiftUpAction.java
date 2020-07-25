package gr.cti.eslate.database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import gr.cti.eslate.database.engine.*;

public class ShiftUpAction extends AbstractAction {
    DBTable dbTable;

    public ShiftUpAction(DBTable table, String name) {
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK, false));
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        if (dbTable.table.getRecordCount() == 0)
            return;
        int selCol = dbTable.jTable.getSelectedColumn();
        if (dbTable.firstShiftUpDownKey == -1) {
            if (dbTable.activeRow == -1)
                dbTable.firstShiftUpDownKey = 0;
            else
                dbTable.firstShiftUpDownKey = dbTable.activeRow;
        }

        if (dbTable.activeRow == -1) {
            if (dbTable.jTable.getRowCount() != 0) {
                dbTable.jTable.scrollRectToVisible(dbTable.jTable.getCellRect(0, 0, true));
                if (dbTable.recordSelectionChangeAllowed) {
                    dbTable.iterateEvent = false;
                    dbTable.table.addToSelectedSubset(dbTable.table.recordIndex.get(0));
                }
                dbTable.setActiveRow(0);
            }
        }else{
            if (dbTable.activeRow != 0) {
                if (dbTable.activeRow-1 < dbTable.firstShiftUpDownKey) {
                    if (dbTable.recordSelectionChangeAllowed) {
                        dbTable.iterateEvent = false;
                        dbTable.table.addToSelectedSubset(dbTable.table.recordIndex.get(dbTable.activeRow-1));
                    }
                }else if (dbTable.activeRow-1 == dbTable.firstShiftUpDownKey) {
                    if (dbTable.recordSelectionChangeAllowed) {
                        dbTable.iterateEvent = false;
                        dbTable.table.addToSelectedSubset(dbTable.table.recordIndex.get(dbTable.activeRow-1));
                        dbTable.iterateEvent = false;
                        dbTable.table.removeFromSelectedSubset(dbTable.table.recordIndex.get(dbTable.activeRow));
                    }
                }else{
                    if (dbTable.recordSelectionChangeAllowed) {
                        dbTable.iterateEvent = false;
                        dbTable.table.removeFromSelectedSubset(dbTable.table.recordIndex.get(dbTable.activeRow));
                    }
                }
                dbTable.jTable.scrollRectToVisible(dbTable.jTable.getCellRect(dbTable.activeRow-1, selCol, true));
                dbTable.setActiveRow(dbTable.activeRow-1);
            }
        }
    }
}
