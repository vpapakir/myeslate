package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class AddRecordAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public AddRecordAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/addRecord.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/addRecordDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public AddRecordAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
//System.out.println("AddrecordAction");
        if (dBase.activeDBTable != null)
            dBase.activeDBTable.addRecordAction.execute();
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setAddRecordEnabled(b);
        dBase.menu.miRecordNew.setEnabled(b);
    }
}
