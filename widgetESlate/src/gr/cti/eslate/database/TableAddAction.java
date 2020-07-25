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
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.FileDialog;
import javax.swing.JOptionPane;

public class TableAddAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;
    transient ResourceBundle infoBundle;
    transient Locale locale;
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    String errorStr, warningStr;


    public TableAddAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        warningStr = infoBundle.getString("Warning");
        errorStr = infoBundle.getString("Error");
    }

    public TableAddAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
        warningStr = infoBundle.getString("Warning");
    }

    public void actionPerformed(ActionEvent e) {
        ESlateFileDialog fileDialog = dBase.getFileDialog();
        if (fileDialog.isShowing()) {
            return;
        }
        fileDialog.setTitle(infoBundle.getString("DatabaseMsg50"));
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setDefaultExtension("ctb");
        fileDialog.setFile("*.ctb");
        fileDialog.show();

        String dbFileName = fileDialog.getFile();
        if (dbFileName == null) return;
        String currDirectory = fileDialog.getDirectory();
        dbFileName = currDirectory+dbFileName;

        dBase.statusToolbarController.setMessageLabelInWaitState();
        dBase.dbComponent.setCursor(waitCursor);

        try{
            dBase.db.loadTable(dbFileName);
        }catch (UnableToOpenException e1) {
            dBase.dbComponent.setCursor(defaultCursor);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
            return;
         }
         catch (InvalidPathException e1) {
            dBase.dbComponent.setCursor(defaultCursor);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
            return;
         }
         catch (InsufficientPrivilegesException exc) {
            dBase.dbComponent.setCursor(defaultCursor);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
         }

        if (dBase.db.getActiveTable().isHidden()) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, infoBundle.getString("DatabaseMsg74"), warningStr, JOptionPane.WARNING_MESSAGE);
/* The loaded jTable became the active one, so we must reset it.
             */
            dBase.iterateEvent = false;
            dBase.activateDBTableAt(dBase.activeDBTableIndex);
            dBase.iterateEvent = false;
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            dBase.dbComponent.setCursor(defaultCursor);
            return;
        }

        dBase.statusToolbarController.setMessageLabelColor(Color.white);
        dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
        dBase.dbComponent.setCursor(defaultCursor);

        if (dBase.activeDBTable != null)
            dBase.tableRemoveAction.setEnabled(true);
        else
            dBase.tableRemoveAction.setEnabled(false);
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miTableAdd.setEnabled(b);
    }
}
