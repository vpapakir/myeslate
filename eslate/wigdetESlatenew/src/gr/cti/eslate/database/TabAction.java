package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import gr.cti.eslate.utils.ESlateFileDialog;
import java.util.ArrayList;
import gr.cti.eslate.database.engine.*;
import com.objectspace.jgl.Array;

public class TabAction extends AbstractAction {
    DBTable dbTable;

    public TabAction(DBTable table, String name) {
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false));
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        Table table = dbTable.table;
        JTable jTable = dbTable.jTable;
        TableFieldBaseArray tableFields = dbTable.table.getFields();
//cf        ArrayList tableColumns = dbTable.tableColumns;

        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        if (!table.isDataChangeAllowed())
            return;

        int activeRow = dbTable.activeRow;
        if (table.getRecordEntryStructure().isRecordAdditionPending())
            activeRow = table.getRecordEntryStructure().getPendingRecordIndex();
        if (activeRow == -1) return;

/* When "simultaneousFieldRecordActivation" is "false", column selection is not allowed.
         * However in the special case, when a field header is clicked, we set "columnSelectionAllowed"
         * to "true", so that the column is selected. "columnSelectionAllowed" turns again to "false"
         * when VK_LEFT/VK_RIGHT/VK_UP/VK_DOWN/VK_TAB is pressed or the user clicks inside the jTable.
         * Turning "columnSelectionAllowed" to false, resets all te existing selections (both
         * row and column) in the jTable, so we keep track of them and reselect them afterwards.
         */
/*1        if (jTable.getColumnSelectionAllowed() != dbTable.isSimultaneousFieldRecordActivation()) {
            int[] selectedRows = jTable.getSelectedRows();
//                        int selectedColumn = -1;
//                        selectedColumn = jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
            jTable.setColumnSelectionAllowed(false);
            for (int i=0; i<dbTable.colSelectionStatus.size(); i++) {
                if (((Boolean) dbTable.colSelectionStatus.get(i)).booleanValue())
                    jTable.getColumnModel().getSelectionModel().setSelectionInterval(i, i);
            }
//                        if (selectedColumn != -1)
//                            jTable.getColumnModel().getSelectionModel().setSelectionInterval(selectedColumn, selectedColumn);
            for (int k=0; k<selectedRows.length; k++)
                jTable.getSelectionModel().addSelectionInterval(selectedRows[k], selectedRows[k]);
        }
1*/
        if (!dbTable.isEditing() & jTable.getColumnCount() > 0) {
            /* find the first editable cell.
             */
            boolean fieldFound = false;
            /* If there is no cell being editing, then edit the focused cell, if this is
             * editable, or some editable cell to its right. If there is no focused cell,
             * start looking for a cell from the first one of the row.
             */
            int i = jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
            if (i<0 || i >= tableFields.size())
                i = 0;
            while (true) {
                AbstractTableField fld1 = (AbstractTableField) tableFields.get(jTable.convertColumnIndexToModel(i));
                if (fld1.isEditable()
                    && !fld1.getDataType().equals(CImageIcon.class)) {
                    if (table.isHiddenFieldsDisplayed() || !fld1.isHidden()) {
                        fieldFound = true;
                        break;
                    }else{
                        i++;
                        if (i == tableFields.size())
                            break;
                    }
                }else{
                    i++;
                    if (i == tableFields.size())
                        break;
                }
            }

            if (fieldFound) {
                int selectedRow = activeRow;
                for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                    dbTable.colSelectionStatus.set(m, Boolean.FALSE);
                dbTable.colSelectionStatus.set(i, Boolean.TRUE);
                jTable.getColumnModel().getSelectionModel().setSelectionInterval(i, i);
                jTable.scrollRectToVisible(jTable.getCellRect(selectedRow, i, true));
//System.out.println("TabAction editCellAt() selectedRow: " + selectedRow + ", col: " + i);
                dbTable.editCellAt(selectedRow, i);
                AbstractTableField f1 = (AbstractTableField) tableFields.get(jTable.convertColumnIndexToModel(i));

                if (f1.getDataType().equals(CImageIcon.class)) {
                    int fieldIndex = jTable.convertColumnIndexToModel(i);
//1                        dbTable.isEditing = false;
                    //IE tests
                    ESlateFileDialog fileDialog = dbTable.getFileDialog();
                    if (fileDialog.getFile() != null) {
                        String fileName = fileDialog.getDirectory() + fileDialog.getFile();
                        ((DBTableModel) jTable.getModel()).setValueAt(fileName, selectedRow, fieldIndex);
//                            jTable.paintImmediately(jTable.getCellRect(selectedRow, fieldIndex, true));
                        jTable.repaint(jTable.getCellRect(selectedRow, fieldIndex, true));
                    }
                }
            }
//                        jTable.editCellAt(jTable.getSelectedRow(), 0);
            return;
        }

        /* If the a jTable cell was being edited, when TAB was pressed then edit the next editable
         * cell to its right, if one exists.
         */
        if (dbTable.isEditing()) {
            int editedColumn = jTable.getEditingColumn();
            dbTable.stopCellEditing();
//            ((DefaultCellEditor) ((TableColumn) tableColumns.get(jTable.getEditingColumn())).getCellEditor()).stopCellEditing();
            jTable.requestFocus();
//1                dbTable.isEditing = false;
            if (editedColumn+1 < tableFields.size()) {
                /* Find the next field which can be edited
                 */
                boolean fieldFound = false;
                int i = 1;
                while (true) {
                    AbstractTableField fld1 = (AbstractTableField) tableFields.get(jTable.convertColumnIndexToModel(editedColumn+i));
                    if (fld1.isEditable()) {
                        if (table.isHiddenFieldsDisplayed() || !fld1.isHidden()) {
                            fieldFound = true;
                            break;
                        }else{
                            i++;
                            if ((editedColumn+i) == tableFields.size())
                                break;
                        }
                    }else{
                        i++;
                        if ((editedColumn+i) == tableFields.size())
                            break;
                    }
                }

//System.out.println("fieldFound: " + fieldFound);
                if (fieldFound) {
                    for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                        dbTable.colSelectionStatus.set(m, Boolean.FALSE);
                    dbTable.colSelectionStatus.set(editedColumn+i, Boolean.TRUE);
                    jTable.getColumnModel().getSelectionModel().setSelectionInterval(editedColumn+i, editedColumn+i);
                    int l = editedColumn+i;
                    int selectedRow = activeRow;
                    jTable.scrollRectToVisible(jTable.getCellRect(selectedRow, editedColumn+i, true));
//System.out.println("TabAction editCellAt() selectedRow: " + selectedRow + ", col: " + (editedColumn+i));
                    dbTable.editCellAt(selectedRow, editedColumn+i);
                    AbstractTableField fld = (AbstractTableField) tableFields.get(jTable.convertColumnIndexToModel(editedColumn+i));

                    if (fld.getDataType().equals(CImageIcon.class)) {
                        int fieldIndex = jTable.convertColumnIndexToModel(editedColumn+i);
//1                            dbTable.isEditing = false;
                        //IE tests
                        ESlateFileDialog fileDialog = dbTable.getFileDialog();
                        if (fileDialog != null && fileDialog.getFile() != null) {
                            String fileName = fileDialog.getDirectory() + fileDialog.getFile();
                            ((DBTableModel) jTable.getModel()).setValueAt(fileName, selectedRow, fieldIndex);
//                                jTable.paintImmediately(jTable.getCellRect(selectedRow, fieldIndex, true));
                            jTable.repaint(jTable.getCellRect(selectedRow, fieldIndex, true));
                        }
                    }
                }else{
                    int nextRowIndex = activeRow + 1;
                    if (nextRowIndex < jTable.getRowCount()) {
                        /* Find the first non-hidden field in the Table, if hidden
                         * field are not displayed.
                         */
                        int nextColIndex = 0;
                        if (!table.isHiddenFieldsDisplayed()) {
//1                            Array columnOrder = dbTable.viewStructure.tableView.getColumnOrder();
                            while (nextColIndex <= jTable.getColumnCount()-1) {
//1                                try{
                                    AbstractTableField fld1 = dbTable.getTableField(nextColIndex);
//1                                    AbstractTableField fld1 = table.getTableField(((Integer) columnOrder.at(nextColIndex)).intValue());
                                    if (!fld1.isHidden()) {
                                        break;
                                    }
//1                                }catch (InvalidFieldIndexException exc) {
//1                                    System.out.println("Serious inconsistency error in DBTable DBTable(): 1.6");
//1                                }
                                nextColIndex++;
                            }
                        }
                        if (nextColIndex <= jTable.getColumnCount()-1) {
                            for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                                dbTable.colSelectionStatus.set(m, Boolean.FALSE);
                            dbTable.colSelectionStatus.set(nextColIndex, Boolean.TRUE);
                            jTable.getColumnModel().getSelectionModel().setSelectionInterval(nextColIndex, nextColIndex);
                            dbTable.setActiveRow(nextRowIndex);
                            jTable.scrollRectToVisible(jTable.getCellRect(nextRowIndex, nextColIndex, true));
                        }
                    }

                    /* If a new record was being added, when the last field of the record was
                     * inserted, then create another empty record for the user to fill. This
                     * will happen only if the insertion of the current record is succesful,
                     * which means that the "pendingNewRecord" flag is reset.
                     */
                    if (dbTable.table.getRecordEntryStructure().isRecordAdditionPending())
                        dbTable.commitNewRecord();
/*1                        boolean isNewRecordPending = dbComponent.pendingNewRecord;
                    dbTable.checkAndStorePendingRecord2();
                    if (isNewRecordPending && !dbComponent.pendingNewRecord && nextRowIndex >= jTable.getRowCount()) {
                      try{
                            if (dbTable.newRecord()) {
                                dbComponent.menu.databaseMenu.setEnabled(false);
                                dbComponent.menu.tableMenu.setEnabled(false);
                                dbComponent.menu.fieldMenu.setEnabled(false);
                                dbComponent.menu.recordMenu.setEnabled(false);
                                dbComponent.blockingNewRecord = true;
                            }
                            dbComponent.pendingNewRecord = true;
                        }catch (Exception e1) {
                            ESlateOptionPane.showMessageDialog(dbComponent, e1.getMessage(), dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        }
                    }
1*/
                }
            }else{
                int nextRowIndex = activeRow + 1;
                if (nextRowIndex < jTable.getRowCount()) {
                    jTable.getSelectionModel().setSelectionInterval(nextRowIndex, nextRowIndex);
                    /* Find the first non-hidden field in the Table, if hidden
                     * field are not displayed.
                     */
                    int nextColIndex = 0;
                    if (!table.isHiddenFieldsDisplayed()) {
//1                        Array columnOrder = dbTable.viewStructure.tableView.getColumnOrder();
                        while (nextColIndex <= jTable.getColumnCount()-1) {
//1                            try{
                                AbstractTableField fld1 = dbTable.getTableField(nextColIndex);
//1                                AbstractTableField fld1 = table.getTableField(((Integer) columnOrder.at(nextColIndex)).intValue());
                                if (!fld1.isHidden()) {
                                    break;
                                }
//1                            }catch (InvalidFieldIndexException exc) {
//1                                System.out.println("Serious inconsistency error in DBTable DBTable(): 1.6");
//1                            }
                            nextColIndex++;
                        }
                    }
                    if (nextColIndex <= jTable.getColumnCount()-1) {
                        for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                            dbTable.colSelectionStatus.set(m, Boolean.FALSE);
                        dbTable.colSelectionStatus.set(nextColIndex, Boolean.TRUE);
                        jTable.getColumnModel().getSelectionModel().setSelectionInterval(nextColIndex, nextColIndex);
                        dbTable.setActiveRow(nextRowIndex);
                        jTable.scrollRectToVisible(jTable.getCellRect(nextRowIndex, nextColIndex, true));
                    }
                }else{
                    /* If a new record was being added, when the last field of the record was
                     * inserted, then create another empty record for the user to fill. This
                     * will happen only if the insertion of the current record is succesful,
                     * which means that the "pendingNewRecord" flag is reset.
                     */
                    if (table.getRecordEntryStructure().isRecordAdditionPending()) {
                        if (dbTable.commitNewRecord())
                            dbTable.newRecord();
                    }
                }
            }
        }
    }
}
