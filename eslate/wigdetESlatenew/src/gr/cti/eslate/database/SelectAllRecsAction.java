package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class SelectAllRecsAction extends AbstractAction {
    Icon iconDisabled, iconEnabled;
    Database dBase;

    public SelectAllRecsAction(Database db, String name) {
        super(name);
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/selectAll.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/selectAllDisabled.gif"));
        dBase = db;
    }

    public SelectAllRecsAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        super(name);
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.activeDBTable != null) {
            dBase.activeDBTable.selectAllRecsAction.execute();
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
        super.setEnabled(b);
        dBase.standardToolbarController.setSelectAllRecsEnabled(b);
        dBase.menu.miRecordSelectAll.setEnabled(b);
    }
}
