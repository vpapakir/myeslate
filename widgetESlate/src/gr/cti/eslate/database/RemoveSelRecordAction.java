package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class RemoveSelRecordAction extends AbstractAction {
    Icon iconDisabled, iconEnabled;
    Database dBase;

    public RemoveSelRecordAction(Database db, String name){
        super(name);
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/removeSelRecs.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/removeSelRecsDisabled.gif"));
        dBase = db;
    }

    public RemoveSelRecordAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        super(name);
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.activeDBTable != null)
            dBase.activeDBTable.removeSelRecordAction.execute();
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setRemoveSelectedRecordsEnabled(b);
        dBase.menu.miRecordRemoveSelected.setEnabled(b);
    }
}
