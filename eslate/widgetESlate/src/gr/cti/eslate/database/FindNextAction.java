package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class FindNextAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;

    public FindNextAction(Database db, String name){
        super(name);
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/findNext2.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/findNextDisabled2.gif"));
        dBase = db;
    }

    public FindNextAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        super(name);
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
    }

    public void actionPerformed(ActionEvent e) {
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
        dBase.standardToolbarController.setFindNextEnabled(b);
        dBase.menu.miTableFindNext.setEnabled(b);
    }

    protected void execute() {
        DBTable dbTable = dBase.activeDBTable;
        if (dbTable != null)
            dbTable.findNextAction.execute();
    }
}
