package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.base.*;
import gr.cti.eslate.database.engine.*;
import javax.swing.JFrame;
import java.awt.FileDialog;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import java.awt.Color;
import javax.swing.SwingUtilities;


public class ImportTableAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    transient ResourceBundle infoBundle;
    transient Locale locale;
    String currDelimiter = ",";
    String errorStr;

    public ImportTableAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public ImportTableAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        actionName = name;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("import jTable action");
        ESlateFileDialog fileDialog = dBase.getFileDialog();
        if (fileDialog.isShowing()) {
            return;
        }

        fileDialog.setDefaultExtension(new String[0]);
        fileDialog.setTitle(infoBundle.getString("DatabaseMsg43"));
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setFile("*.txt;*.csv");
        fileDialog.show();

        dBase.dbComponent.setCursor(waitCursor);

        String textFileName = fileDialog.getFile();
        if (textFileName == null) {
            dBase.dbComponent.setCursor(defaultCursor);
            return;
        }

        String[] supportedDelimiters = {",", ";"};
        String currDelimiterBck = currDelimiter;
        dBase.dbComponent.setCursor(defaultCursor);
        currDelimiter = (String) ESlateOptionPane.showInputDialog(dBase.dbComponent,
                              infoBundle.getString("DatabaseMsg6") + textFileName + "\"",
                              infoBundle.getString("DatabaseMsg7"),
                              JOptionPane.INFORMATION_MESSAGE,
                              null,
                              supportedDelimiters,
                              currDelimiter);

        if (currDelimiter == null) {
            currDelimiter = currDelimiterBck;
            return;
        }

        if (dBase.db.getTableCount() > 0) {
            dBase.statusToolbarController.setMessageLabelInWaitState();
            dBase.dbComponent.setCursor(waitCursor);
        }

        String currDirectory = fileDialog.getDirectory();
        textFileName = currDirectory+textFileName;

        try{
            dBase.db.importTableFromTextFile(textFileName, currDelimiter.charAt(0));
        }catch (UnableToReadFileException e1) {
            if (dBase.db.getTableCount() > 0) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            }
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
            return;
         }
         catch (InvalidPathException e1) {
            if (dBase.db.getTableCount() > 0) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            }
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
            return;
         }
         catch (EmptyFileException e1) {
            if (dBase.db.getTableCount() > 0) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            }
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
            return;
         }
         catch (InvalidTextFileException e1) {
            if (dBase.db.getTableCount() > 0) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            }
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
            return;
         }
         catch (InvalidDelimeterException e1) {
            if (dBase.db.getTableCount() > 0) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            }
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
            return;
         }
         catch (InsufficientPrivilegesException exc) {
            if (dBase.db.getTableCount() > 0) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            }
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
            return;
         }

        if (dBase.db.getTableCount() > 0) {
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
        }
        dBase.dbComponent.setCursor(defaultCursor);
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        dBase.menu.miDBImport.setEnabled(b);
    }
}
