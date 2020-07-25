package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class PromoteSelRecsAction extends AbstractAction {
    Icon iconDisabled, iconEnabled;
    Database dBase;

    public PromoteSelRecsAction(Database db, String name) {
        super(name);
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/promoteSelected.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/promoteSelectedDisabled.gif"));
        dBase = db;
    }

    public PromoteSelRecsAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        super(name);
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.activeDBTable != null)
            dBase.activeDBTable.promoteSelRecsAction.execute();
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setPromoteSelectedRecsEnabled(b);
        dBase.menu.miRecordSelectedFirst.setEnabled(b);
    }

}
