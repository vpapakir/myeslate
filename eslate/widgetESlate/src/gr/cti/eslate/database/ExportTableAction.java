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
import javax.swing.JDialog;
import javax.swing.UIManager;


public class ExportTableAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
//    String actionName;
//    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
//    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
//    transient ResourceBundle infoBundle;
//    transient Locale locale;
//    String currDelimiter = ",";
//    String errorStr;

    public ExportTableAction(Database db, String name){
        super(name);
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
//        actionName = name;
//        locale=ESlateMicroworld.getCurrentLocale();
//        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
//        errorStr = infoBundle.getString("Error");
    }

    public ExportTableAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        super(name);
        dBase = db;
//        actionName = name;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
//        locale=ESlateMicroworld.getCurrentLocale();
//        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
//        errorStr = infoBundle.getString("Error");
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase == null || dBase.activeDBTable == null) return;
        dBase.activeDBTable.exportToTextFileAction.actionPerformed(e);
/*        String tableName = dBase.db.getActiveTable().getTitle();
        String fileDialogTitle;
        if (tableName == null || tableName.trim().length() == 0) {
            tableName = infoBundle.getString("DatabaseMsg44");
            fileDialogTitle = infoBundle.getString("DatabaseMsg45");
        }else
            fileDialogTitle = infoBundle.getString("DatabaseMsg46") + tableName + infoBundle.getString("DatabaseMsg47");

        String[] supportedDelimiters = {",", ";"};
        String currDelimiterBck = currDelimiter;
        currDelimiter = (String) ESlateOptionPane.showInputDialog(dBase.dbComponent,
                              infoBundle.getString("DatabaseMsg8"),
                              infoBundle.getString("DatabaseMsg9"),
                              JOptionPane.INFORMATION_MESSAGE,
                              null,
                              supportedDelimiters,
                              currDelimiter);
        if (currDelimiter == null) {
            currDelimiter = currDelimiterBck;
            return;
        }

        Object[] yes_no = {infoBundle.getString("Yes"), infoBundle.getString("No")};

        JOptionPane pane = new JOptionPane(infoBundle.getString("DatabaseMsg10"),
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION,
            UIManager.getIcon("OptionPane.questionIcon"),
            yes_no,
            infoBundle.getString("Yes"));
        JDialog dialog = pane.createDialog(dBase.dbComponent, infoBundle.getString("DatabaseMsg11"));
        dialog.show();
        Object option = pane.getValue();

        if (option != yes_no[0]) return;
        boolean quoteData = false;
        if (pane.getValue().equals(infoBundle.getString("Yes")))
            quoteData = true;

        ESlateFileDialog fileDialog = dBase.getFileDialog();
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

        dBase.statusToolbarController.setMessageLabelInWaitState();
        dBase.dbComponent.setCursor(waitCursor);

        String currDirectory = fileDialog.getDirectory();
        textFileName = currDirectory+textFileName;

        try{
            dBase.db.exportTableToTextFile(dBase.db.getActiveTable(), textFileName, currDelimiter.charAt(0), quoteData);
        }catch (UnableToWriteFileException e1) {
            dBase.dbComponent.setCursor(defaultCursor);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
         }
         catch (InvalidDelimeterException e1) {
            dBase.dbComponent.setCursor(defaultCursor);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
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
*/
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        dBase.menu.miDBExport.setEnabled(b);
    }
}
