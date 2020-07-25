package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import gr.cti.eslate.database.engine.*;
import java.awt.Cursor;
import javax.swing.JFrame;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import java.awt.Color;

public class FieldClearSelectionAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public FieldClearSelectionAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
    }

    public FieldClearSelectionAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.activeDBTable.tableColumns.size() > 0 && dBase.activeDBTable.jTable.getSelectedColumnCount() > 0) {
            int[] selectedRows = dBase.activeDBTable.jTable.getSelectedRows();
            int selectedColumn = -1;
            selectedColumn = dBase.activeDBTable.jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
            if (!dBase.activeDBTable.isSimultaneousFieldRecordActivation())
                dBase.activeDBTable.jTable.setColumnSelectionAllowed(false);
            for (int k=0; k<selectedRows.length; k++)
                dBase.activeDBTable.jTable.getSelectionModel().addSelectionInterval(selectedRows[k], selectedRows[k]);

            for (int m=0; m<dBase.activeDBTable.colSelectionStatus.size(); m++)
                dBase.activeDBTable.colSelectionStatus.set(m, Boolean.FALSE);
            dBase.activeDBTable.jTable.getColumnModel().getSelectionModel().clearSelection();

            if (selectedColumn != -1) {
                dBase.activeDBTable.colSelectionStatus.set(selectedColumn, Boolean.TRUE);
                dBase.activeDBTable.jTable.getColumnModel().getSelectionModel().setSelectionInterval(selectedColumn, selectedColumn);
            }
            dBase.activeDBTable.updateColumnHeaderSelection();
            dBase.activeDBTable.jTable.getTableHeader().repaint();
        }
        dBase.setCutCopyPasteStatus();
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miFieldClearSelection.setEnabled(b);
    }
}
