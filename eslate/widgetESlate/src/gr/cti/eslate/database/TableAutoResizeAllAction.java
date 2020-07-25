package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class TableAutoResizeAllAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public TableAutoResizeAllAction(Database db, String name){
        iconEnabled = new ImageIcon(Database.class.getResource("images/purple-bullet.gif"));
        iconDisabled = new ImageIcon(Database.class.getResource("images/lightGray2.gif"));;
        dBase = db;
        actionName = name;
    }

    public TableAutoResizeAllAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        dBase.menu.miTableAutoResizeAll.setSelected(false);
        dBase.activeDBTable.setAutoResizeMode(dBase.activeDBTable.jTable.AUTO_RESIZE_ALL_COLUMNS);
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
        dBase.menu.miTableAutoResizeAll.setEnabled(b);
    }
}
