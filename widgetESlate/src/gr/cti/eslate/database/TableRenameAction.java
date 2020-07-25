package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.beans.PropertyVetoException;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.utils.ESlateOptionPane;

public class TableRenameAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public TableRenameAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public TableRenameAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
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
        dBase.menu.miTableRename.setEnabled(b);
    }

    protected void execute() {
        DBase db = dBase.db;

        if (db.getActiveTable() == null) return;
        Table table = db.getActiveTable();
        if (!table.isTableRenamingAllowed()) return;

        String currTitle = table.getTitle();
        String newTitle = (String) ESlateOptionPane.showInputDialog(dBase,
                          dBase.infoBundle.getString("DatabaseMsg13"),
                          dBase.infoBundle.getString("DatabaseMsg14"),
                          JOptionPane.QUESTION_MESSAGE,
                          null,
                          null,
                          currTitle);
        if (newTitle != null && !newTitle.equals(currTitle)) {
            try{
                db.getActiveTable().setTitle(newTitle);
            }catch (InvalidTitleException exc) {
                ESlateOptionPane.showMessageDialog(dBase, exc.getMessage(), dBase.errorStr, JOptionPane.ERROR_MESSAGE);
                return;
            }
              catch (PropertyVetoException exc) {
                ESlateOptionPane.showMessageDialog(dBase, exc.getMessage(), dBase.errorStr, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
}
