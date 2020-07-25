package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class PropertiesAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public PropertiesAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public PropertiesAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        actionName = name;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
    }

    public void actionPerformed(ActionEvent e) {
        DatabasePropertiesDialog dbpr = new DatabasePropertiesDialog(dBase.topLevelFrame, dBase.dbComponent);
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        dBase.menu.miDBProperties.setEnabled(b);
    }
}
