package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import gr.cti.eslate.database.engine.*;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import java.awt.Color;
import java.util.Locale;
import java.util.ResourceBundle;
import gr.cti.eslate.base.*;
import javax.swing.JOptionPane;
import gr.cti.eslate.utils.ESlateOptionPane;

public class SaveDBAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String errorStr;
    transient ResourceBundle infoBundle;
    transient Locale locale;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    String actionName;

    public SaveDBAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/save.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/saveDisabled.gif"));
        dBase = db;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
        actionName = name;
    }

    public SaveDBAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    public void execute() {
        if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
            dBase.visiblePopupMenu.setVisible(false);
/* If a jTable cell was edited when the button was pressed, then stop the cell editing.
         * This results in the evaluation of the edited value of the cell.
         */
        if (dBase.activeDBTable != null && dBase.activeDBTable.jTable.isEditing())
            ((DefaultCellEditor) dBase.activeDBTable.tableColumns.get(dBase.activeDBTable.jTable.getEditingColumn()).tableColumn.getCellEditor()).stopCellEditing();

        try{
            dBase.statusToolbarController.setMessageLabelInWaitState();
            dBase.dbComponent.setCursor(waitCursor);
            dBase.db.save();
            dBase.updateRecentDBList();
        }catch (UnableToSaveException e1) {
            dBase.dbComponent.setCursor(defaultCursor);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
        }
         catch (InvalidPathException e1) {
            dBase.dbComponent.setCursor(defaultCursor);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
         }
         catch (UnableToOverwriteException e1) {
            dBase.dbComponent.setCursor(defaultCursor);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
         }
        dBase.statusToolbarController.setMessageLabelColor(Color.white);
        dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
        dBase.dbComponent.setCursor(defaultCursor);
        dBase.dataBaseSaved = true;
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setSaveDBEnabled(b);
        dBase.menu.miDBSave.setEnabled(b);
    }
}
