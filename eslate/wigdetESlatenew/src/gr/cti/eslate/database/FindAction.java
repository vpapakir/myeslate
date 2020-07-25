package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class FindAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public FindAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/find.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/findDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public FindAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
            dBase.visiblePopupMenu.setVisible(false);
/* If a jTable cell was edited when the button was pressed, then stop the cell editing.
         * This results in the evaluation of the edited value of the cell.
         */
        if (dBase.activeDBTable != null && dBase.activeDBTable.jTable.isEditing())
            ((DefaultCellEditor) dBase.activeDBTable.tableColumns.get(dBase.activeDBTable.jTable.getEditingColumn()).tableColumn.getCellEditor()).stopCellEditing();

        execute();
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setFindEnabled(b);
        dBase.menu.miTableFind.setEnabled(b);
    }

    protected void execute() {
        DBTable dBtable = dBase.activeDBTable;

        if (dBtable != null) {
            FindDialog findDialog = dBase.getFindDialog();
            dBase.findDialog.display(dBtable);
            if (dBase.activeDBTable != null)
                dBase.activeDBTable.requestFocus();
        }
    }
}
