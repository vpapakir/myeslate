package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import gr.cti.eslate.database.engine.*;
import java.awt.Cursor;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import java.awt.Color;

public class TableRemoveAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

    public TableRemoveAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public TableRemoveAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
//        dBase.statusBar.setMessageLabelInWaitState();
//        dBase.dbComponent.setCursor(waitCursor);

        dBase.removeDBTable(dBase.activeDBTable, true);
//        dBase.statusBar.messageLabel.setForeground(Color.white);
        if (dBase.dbTables.size() != 0)
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
//        dBase.dbComponent.setCursor(defaultCursor);
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miTableRemove.setEnabled(b);
    }
}
