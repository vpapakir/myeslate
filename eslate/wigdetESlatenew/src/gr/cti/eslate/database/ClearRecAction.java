package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class ClearRecAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public ClearRecAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/clearSelection.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/clearSelectionDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public ClearRecAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
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

        if (dBase.activeDBTable.recordSelectionChangeAllowed) {
            dBase.activeDBTable.iterateEvent = false;
            dBase.activeDBTable.table.resetSelectedSubset();
            dBase.activeDBTable.refresh();
            //dBase.standardToolBar.setCutCopyPasteStatus();
            dBase.setCutCopyPasteStatus();
        }
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        boolean recordSelectionAllowed = true;
        if (dBase.activeDBTable != null)
            recordSelectionAllowed = dBase.activeDBTable.recordSelectionChangeAllowed;
        super.setEnabled(b && recordSelectionAllowed);
        dBase.standardToolbarController.setClearRecSelectionEnabled(b);
        dBase.menu.miRecordClearSelection.setEnabled(b);
    }
}
