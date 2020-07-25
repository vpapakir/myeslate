package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import java.awt.Cursor;

public class CloseAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    public CloseAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public CloseAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        actionName = name;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
    }

    public void actionPerformed(ActionEvent e) {
        //dBase.menu.requestFocus();
        if (dBase.closeDatabase(false, true) == 0) {
//            if (dBase.eSlateHandle != null)
//                dBase.databaseSO.setDatabase(null);
        }
        dBase.dbComponent.setCursor(defaultCursor);
        dBase.componentModified = true;
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        dBase.menu.miDBClose.setEnabled(b);
    }
}
