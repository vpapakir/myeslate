package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.FileDialog;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.eslate.database.engine.*;
import javax.swing.JFrame;
import java.util.Locale;
import java.util.ResourceBundle;
import gr.cti.eslate.base.ESlateMicroworld;
import java.awt.Cursor;

public class OpenDBAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    transient ResourceBundle infoBundle;
    transient Locale locale;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    String actionName;

    public OpenDBAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/open.gif"));
        iconDisabled = null;
        dBase = db;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        actionName = name;
    }

    public OpenDBAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.visiblePopupMenu!=null && dBase.visiblePopupMenu.isVisible())
            dBase.visiblePopupMenu.setVisible(false);
/* If a jTable cell was edited when the button was pressed, then stop the cell editing.
         * This results in the evaluation of the edited value of the cell.
         */
        if (dBase.activeDBTable != null && dBase.activeDBTable.jTable.isEditing())
            ((DefaultCellEditor) dBase.activeDBTable.tableColumns.get(dBase.activeDBTable.jTable.getEditingColumn()).tableColumn.getCellEditor()).stopCellEditing();

        //IE tests
        ESlateFileDialog fileDialog = dBase.getFileDialog();
        if (fileDialog.isShowing()) {
            return;
        }

        fileDialog.setTitle(infoBundle.getString("DatabaseMsg39"));
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setFile("*.cdb");
        fileDialog.setDefaultExtension("cdb");
        fileDialog.show();

        String dbFileName = fileDialog.getFile();
        if (dbFileName == null) {
            dBase.dbComponent.setCursor(defaultCursor);
            return;
        }
        String currDirectory = fileDialog.getDirectory();
        dbFileName = currDirectory+dbFileName;

        dBase.openDB(dbFileName);
        dBase.dataBaseSaved = true;
        dBase.componentModified = false;
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setOpenDBEnabled(b);
        dBase.menu.miDBOpen.setEnabled(b);
    }

}
