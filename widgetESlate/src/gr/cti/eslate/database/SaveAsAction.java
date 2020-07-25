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
import gr.cti.eslate.utils.*;
import javax.swing.JFrame;
import java.awt.FileDialog;

public class SaveAsAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String errorStr;
    transient ResourceBundle infoBundle;
    transient Locale locale;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    String actionName;

    public SaveAsAction(Database db, String name){
        iconEnabled = null;;
        iconDisabled = null;
        dBase = db;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
        actionName = name;
    }

    public SaveAsAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
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
        if (dBase.db.getTitle() == null || dBase.db.getTitle().trim().length() == 0) {
           dialogTitle = infoBundle.getString("DatabaseMsg40");
           initialValue = "*.cdb";
        }else{
           dialogTitle = infoBundle.getString("DatabaseMsg40") + '\"' + dBase.db.getTitle() + infoBundle.getString("DatabaseMsg42");
           initialValue = dBase.db.getTitle();        }


        ESlateFileDialog fileDialog = dBase.getFileDialog();
        fileDialog.setTitle(dialogTitle);
        fileDialog.setMode(FileDialog.SAVE);

        String[] str = new String[1];
        str[0] = "cdb";
        fileDialog.setDefaultExtension(str);
        fileDialog.setFile(initialValue);
        fileDialog.show();

        String dbFileName = fileDialog.getFile();
        if (dbFileName == null) return;

        dBase.statusToolbarController.setMessageLabelInWaitState();
        dBase.dbComponent.setCursor(waitCursor);

        String currDirectory = fileDialog.getDirectory();
        dbFileName = currDirectory+dbFileName;

        try{
            dBase.db.saveAs(dbFileName);
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
        dBase.menu.miDBSaveAs.setEnabled(b);
    }
}
