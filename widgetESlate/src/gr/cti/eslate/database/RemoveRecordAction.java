package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class RemoveRecordAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public RemoveRecordAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/removeRec.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/removeRecDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public RemoveRecordAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
            dBase.visiblePopupMenu.setVisible(false);
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
        dBase.standardToolbarController.setRemoveRecordEnabled(b);
        dBase.menu.miRecordRemoveActive.setEnabled(b);
    }

    protected void execute() {
        DBTable activeDBTable = dBase.activeDBTable;
        if (activeDBTable != null)
            activeDBTable.removeRecordAction.execute();

/*        int activeRow = activeDBTable.activeRow;
        if (activeRow != -1) {
            Object[] conf_no = {dBase.infoBundle.getString("Confirmation"), dBase.infoBundle.getString("Cancel")};

            JOptionPane pane = new JOptionPane(dBase.infoBundle.getString("DatabaseMsg96"),
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                UIManager.getIcon("OptionPane.questionIcon"),
                conf_no,
                dBase.infoBundle.getString("Confirmation"));
            JDialog dialog = pane.createDialog(dBase, dBase.infoBundle.getString("Confirm removal"));
            dialog.show();
            Object option = pane.getValue();

            if (option == dBase.infoBundle.getString("Cancel") || option == null)
                return;
        }
        activeDBTable.removeActiveRecord();
*/
    }
}
