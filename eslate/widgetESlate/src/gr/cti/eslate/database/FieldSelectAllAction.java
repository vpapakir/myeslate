package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class FieldSelectAllAction extends AbstractAction {
    Icon iconDisabled, iconEnabled;
    Database dBase;

    public FieldSelectAllAction(Database db, String name){
        super(name);
        iconDisabled = null;
        iconEnabled = null;
        dBase = db;
    }

    public FieldSelectAllAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        super(name);
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.activeDBTable != null)
            dBase.activeDBTable.fieldSelectAllAction.execute();
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miFieldSelectAll.setEnabled(b);
    }
}
