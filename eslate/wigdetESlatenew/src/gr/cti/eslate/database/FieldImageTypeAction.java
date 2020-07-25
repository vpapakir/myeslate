package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import gr.cti.eslate.database.engine.*;
import java.awt.Cursor;
import javax.swing.JFrame;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import java.awt.Color;

public class FieldImageTypeAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    public FieldImageTypeAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public FieldImageTypeAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        dBase.menu.imageType.setSelected(false);
        TableColumn col = dBase.activeDBTable.tableColumns.get(dBase.activeDBTable.jTable.getSelectedColumn()).tableColumn;
        String identifier = (String) col.getIdentifier();
        try{
            dBase.statusToolbarController.setMessageLabelInWaitState();
            dBase.dbComponent.setCursor(waitCursor);
            AbstractTableField f = dBase.activeDBTable.table.getTableField(identifier);
            dBase.activeDBTable.changeFieldType(f, CImageIcon.class, true);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            dBase.dbComponent.setCursor(defaultCursor);
        }catch (InvalidFieldNameException e1) {
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            dBase.dbComponent.setCursor(defaultCursor);
            System.out.println("Serious inconsistency error in Database createMenuBar(): (33)");
            return;
         }
         dBase.updateDataTypes();
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.imageType.setEnabled(b);
    }
}
