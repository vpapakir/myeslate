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

public class TableSaveAsAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    transient ResourceBundle infoBundle;
    transient Locale locale;
    String errorStr;

    public TableSaveAsAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
        actionName = name;
    }

    public TableSaveAsAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        String dialogTitle, initialValue;
        Table t = dBase.db.getActiveTable();
        boolean tableUntitled = false;

        if (t.getTitle() == null || t.getTitle().trim().length() == 0) {
            dialogTitle = infoBundle.getString("DatabaseMsg51");
            initialValue = "*.ctb";
            tableUntitled = true;
        }else{
            dialogTitle = infoBundle.getString("DatabaseMsg52") + t.getTitle() + infoBundle.getString("DatabaseMsg53");
            initialValue = t.getTitle();
        }

        ESlateFileDialog fileDialog = dBase.getFileDialog();
        if (fileDialog.isShowing()) {
            return;
        }
        fileDialog.setTitle(dialogTitle);
        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setDefaultExtension("ctb");

        fileDialog.setFile(initialValue);
        fileDialog.show();

        String tableFileName = fileDialog.getFile();
        if (tableFileName == null) return;
        String currDirectory = fileDialog.getDirectory();
        tableFileName = currDirectory+tableFileName;

        dBase.statusToolbarController.setMessageLabelInWaitState();
        dBase.dbComponent.setCursor(waitCursor);

        try{
            if (dBase.db.exportTable(t, tableFileName, false) && tableUntitled) {
                String newTitle = dBase.db.getActiveTable().getTitle();
                if (newTitle != null && newTitle.trim().length() != 0) {
                    dBase.tabs.setTitleAt(dBase.tabs.getSelectedIndex(), newTitle);
                    dBase.tabs.revalidate();
                }
            }

        }catch (UnableToSaveException e1) {
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

        dBase.statusToolbarController.setMessageLabelColor(Color.white);
        dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
        dBase.dbComponent.setCursor(defaultCursor);
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miTableSaveAs.setEnabled(b);
    }
}
