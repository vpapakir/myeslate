package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class DescriptionAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public DescriptionAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public DescriptionAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        actionName = name;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.dbComponent.db == null)
            return;
        MetadataDialog metadataDialog = new MetadataDialog(dBase.topLevelFrame, dBase.dbComponent, null, 0, true); //!dBase.foreignDatabase);
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        dBase.menu.miDBDescription.setEnabled(b);
    }
}
