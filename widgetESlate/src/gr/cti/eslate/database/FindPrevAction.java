package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class FindPrevAction extends AbstractAction {
    Icon iconDisabled, iconEnabled;
    Database dBase;

    public FindPrevAction(Database db, String name){
        super(name);
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/findPrev2.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/findPrevDisabled2.gif"));
        dBase = db;
    }

    public FindPrevAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
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
        dBase.standardToolbarController.setFindPrevEnabled(b);
        dBase.menu.miTableFindPrev.setEnabled(b);
    }

    protected void execute() {
        DBTable dbTable = dBase.activeDBTable;
        if (dbTable != null)
            dbTable.findPrevAction.execute();
    }

}
