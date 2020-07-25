/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Apr 22, 2003
 * Time: 5:57:16 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database;

import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.eslate.database.engine.UnableToWriteFileException;
import gr.cti.eslate.database.engine.InvalidDelimeterException;
import gr.cti.eslate.database.engine.InsufficientPrivilegesException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;

public class DBTableExportToTextFileAction extends AbstractAction {
    DBTable dbTable;
    String currDelimiter = ",";
    String errorStr;

    public DBTableExportToTextFileAction(DBTable dbTable, String name){
        super(name);
        this.dbTable = dbTable;
        errorStr = Database.infoBundle.getString("Error");
    }

    public void actionPerformed(ActionEvent e) {
        if (dbTable.table == null) return;

        String tableName = dbTable.table.getTitle();
        String fileDialogTitle;
        if (tableName == null || tableName.trim().length() == 0) {
            tableName = Database.infoBundle.getString("DatabaseMsg44");
            fileDialogTitle = Database.infoBundle.getString("DatabaseMsg45");
        }else
            fileDialogTitle = Database.infoBundle.getString("DatabaseMsg46") + tableName + Database.infoBundle.getString("DatabaseMsg47");

        String[] supportedDelimiters = {",", ";"};
        String currDelimiterBck = currDelimiter;
        currDelimiter = (String) ESlateOptionPane.showInputDialog(dbTable,
                              Database.infoBundle.getString("DatabaseMsg8"),
                              Database.infoBundle.getString("DatabaseMsg9"),
                              JOptionPane.INFORMATION_MESSAGE,
                              null,
                              supportedDelimiters,
                              currDelimiter);
        if (currDelimiter == null) {
            currDelimiter = currDelimiterBck;
            return;
        }

        Object[] yes_no = {Database.infoBundle.getString("Yes"), Database.infoBundle.getString("No")};

        JOptionPane pane = new JOptionPane(Database.infoBundle.getString("DatabaseMsg10"),
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION,
            UIManager.getIcon("OptionPane.questionIcon"),
            yes_no,
            Database.infoBundle.getString("Yes"));
        JDialog dialog = pane.createDialog(dbTable, Database.infoBundle.getString("DatabaseMsg11"));
        dialog.show();
        Object option = pane.getValue();

        if (option != yes_no[0]) return;
        boolean quoteData = false;
        if (pane.getValue().equals(Database.infoBundle.getString("Yes")))
            quoteData = true;

        Database databaseEditor = dbTable.dbComponent;
        // The component whose cursor will change
        Component componentToChangeCursor = (databaseEditor==null)?(Component)dbTable:(Component)databaseEditor;
        ESlateFileDialog fileDialog = null;
        if (databaseEditor != null)
            fileDialog = databaseEditor.getFileDialog();
        else
            fileDialog = dbTable.getFileDialog();
        if (fileDialog.isShowing()) {
            return;
        }
        fileDialog.setTitle(fileDialogTitle);
        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setDefaultExtension("txt");
        fileDialog.setFile(tableName + ".txt");
        fileDialog.show();

        String textFileName = fileDialog.getFile();
        if (textFileName == null) return;

        Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        Cursor defaultCursor = Cursor.getDefaultCursor();
        if (databaseEditor != null)
            databaseEditor.statusToolbarController.setMessageLabelInWaitState();
        componentToChangeCursor.setCursor(waitCursor);

        String currDirectory = fileDialog.getDirectory();
        textFileName = currDirectory+textFileName;

        try{
            dbTable.table.exportTableToTextFile(textFileName, currDelimiter.charAt(0), quoteData);
        }catch (UnableToWriteFileException e1) {
            componentToChangeCursor.setCursor(defaultCursor);
            if (databaseEditor != null) {
                databaseEditor.statusToolbarController.setMessageLabelColor(Color.white);
                databaseEditor.updateNumOfSelectedRecords(databaseEditor.activeDBTable);
            }
            ESlateOptionPane.showMessageDialog(dbTable, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
         }
         catch (InvalidDelimeterException e1) {
            componentToChangeCursor.setCursor(defaultCursor);
            if (databaseEditor != null) {
                databaseEditor.statusToolbarController.setMessageLabelColor(Color.white);
                databaseEditor.updateNumOfSelectedRecords(databaseEditor.activeDBTable);
            }
            ESlateOptionPane.showMessageDialog(dbTable, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
         }
         catch (InsufficientPrivilegesException exc) {
            componentToChangeCursor.setCursor(defaultCursor);
            if (databaseEditor != null) {
                databaseEditor.statusToolbarController.setMessageLabelColor(Color.white);
                databaseEditor.updateNumOfSelectedRecords(databaseEditor.activeDBTable);
            }
            ESlateOptionPane.showMessageDialog(dbTable, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
         }

        if (databaseEditor != null) {
            databaseEditor.statusToolbarController.setMessageLabelColor(Color.white);
            databaseEditor.updateNumOfSelectedRecords(databaseEditor.activeDBTable);
        }
        componentToChangeCursor.setCursor(defaultCursor);
    }
}
