package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class TableAutoResizeLastAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public TableAutoResizeLastAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public TableAutoResizeLastAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        dBase.menu.miTableAutoResizeLast.setSelected(false);
        dBase.activeDBTable.setAutoResizeMode(dBase.activeDBTable.jTable.AUTO_RESIZE_LAST_COLUMN);
        dBase.updateAutoResizeActions();
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miTableAutoResizeLast.setEnabled(b);
    }
}
