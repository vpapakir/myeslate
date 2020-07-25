package gr.cti.eslate.database;

import javax.swing.JFrame;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import gr.cti.eslate.database.engine.TableField;

public class AddFieldAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public AddFieldAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/addField.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/addFieldDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public AddFieldAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
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
        dBase.standardToolbarController.setAddFieldEnabled(b);
        //dBase.standardToolBar.addField.setEnabled(b);

        dBase.menu.ciFieldNew.setEnabled(b);
    }

    protected void execute() {
        DBTable dbTable = dBase.activeDBTable;
        if (dbTable != null)
            dbTable.addFieldAction.execute(dBase.userMode);
    }
}
