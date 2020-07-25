package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.table.TableColumn;

import gr.cti.eslate.database.engine.*;
import java.awt.Cursor;
import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import java.awt.Color;

public class EscapeAction extends AbstractAction {
    DBTable dbTable;

    public EscapeAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    void execute() {
        dbTable.closeDatabasePopupMenu();
        dbTable.tipManager.resetTip();
        if (dbTable.table.getRecordCount() == 0)
            return;
        dbTable.cancelCellEditing();

        if (dbTable.table.getRecordEntryStructure().isRecordAdditionPending()) {
            dbTable.cancelRecordAddition();
        }
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
    }
}
