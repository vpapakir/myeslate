package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class RemoveFieldAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public RemoveFieldAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/removeField.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/removeFieldDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public RemoveFieldAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
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
        dBase.standardToolbarController.setRemoveFieldEnabled(b);
        dBase.menu.ciFieldRemove.setEnabled(b);
    }

    protected void execute() {
        DBTable activeDBTable = dBase.activeDBTable;
        if (activeDBTable != null)
            activeDBTable.removeFieldAction.execute();
/*        int[] selectedColumnIndices = activeDBTable.jTable.getSelectedColumns();
        if (selectedColumnIndices.length != 0) {
            Object[] conf_no = {dBase.infoBundle.getString("Confirmation"), dBase.infoBundle.getString("Cancel")};

            JOptionPane pane = new JOptionPane(dBase.infoBundle.getString("DatabaseMsg75"),
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                UIManager.getIcon("OptionPane.questionIcon"),
                conf_no,
                dBase.infoBundle.getString("Confirmation"));
            JDialog dialog = pane.createDialog(dBase, dBase.infoBundle.getString("Confirm removal"));
            dialog.show();
            Object option = pane.getValue();

            if (option != conf_no[0])
                return;
        }

        int index;
        int k = 0;
        for (int i=0; i<selectedColumnIndices.length; i++) {
            index = selectedColumnIndices[i]-i+k;
            String identifier = (String) ((TableColumn) activeDBTable.tableColumns.get(index)).getIdentifier(); //tableModel.getColumnIdentifier(index);
            if (!activeDBTable.removeColumn(identifier))
                k++;
        }

        for (int m=0; m<activeDBTable.colSelectionStatus.size(); m++)
            activeDBTable.colSelectionStatus.set(m, Boolean.FALSE);
        activeDBTable.jTable.getColumnModel().getSelectionModel().clearSelection();
*/
        if (activeDBTable.jTable.getColumnCount() == 0 && dBase.addRecordAction.isEnabled())
            dBase.addRecordAction.setEnabled(false);
    }
}
