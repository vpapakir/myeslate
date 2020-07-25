package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.*;

import gr.cti.eslate.utils.ESlateFileDialog;
import java.util.ArrayList;
import gr.cti.eslate.database.engine.*;
import com.objectspace.jgl.Array;


public class ShiftTabAction extends AbstractAction {
    DBTable dbTable;

    public ShiftTabAction(DBTable table, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK, false));
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
        /* Start editing the cell of the first editable column of th active jTable,
         * if only one record is selected.
         */

/* When "simultaneousFieldRecordActivation" is "false", column selection is not allowed.
         * However in the special case, when a field header is clicked, we set "columnSelectionAllowed"
         * to "true", so that the column is selected. "columnSelectionAllowed" turns again to "false"
         * when VK_LEFT/VK_RIGHT/VK_UP/VK_DOWN/VK_TAB is pressed or the user clicks inside the jTable.
         * Turning "columnSelectionAllowed" to false, resets all te existing selections (both
         * row and column) in the jTable, so we keep track of them and reselect them afterwards.
         */
        if (jTable.getColumnSelectionAllowed() != dbTable.isSimultaneousFieldRecordActivation()) {
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

        if (!dbTable.isEditing() & jTable.getColumnCount() > 0) {
            /* find the first editable cell.
             */
            boolean fieldFound = false;
            /* Start looking from the cell in the last selected column, if exists. If it
             * does not exist start from the last column.
             */
            int i = jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
            if (i<0 || i >= tableFields.size())
                i = tableFields.size()-1;
            while (true) {
                AbstractTableField fld1 = (AbstractTableField) tableFields.get(jTable.convertColumnIndexToModel(i));
                if (fld1.isEditable()
                    && !fld1.getDataType().equals(CImageIcon.class)) {
                    if (table.isHiddenFieldsDisplayed() || !fld1.isHidden()) {
                        fieldFound = true;
                        break;
                    }else{
                        i--;
                        if (i == -1)
                            break;
                    }
                }else{
                    i--;
                    if (i == -1)
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
                dbTable.editCellAt(selectedRow, i);
                AbstractTableField f1 = (AbstractTableField) tableFields.get(jTable.convertColumnIndexToModel(i));

                if (f1.getDataType().equals(CImageIcon.class)) {
                    int fieldIndex = jTable.convertColumnIndexToModel(i);
//1                        dbTable.isEditing = false;
                    //IE tests
                    ESlateFileDialog fileDialog = dbTable.getFileDialog();
                    if (fileDialog != null && fileDialog.getFile() != null) {
                        String fileName = fileDialog.getDirectory() + fileDialog.getFile();
                        ((DBTableModel) jTable.getModel()).setValueAt(fileName, selectedRow, fieldIndex);
//                            jTable.paintImmediately(jTable.getCellRect(selectedRow, fieldIndex, true));
                        jTable.repaint(jTable.getCellRect(selectedRow, fieldIndex, true));
                    }
                }
            }
            return;
        }

        /* If the jTable cell was being edited, when SHIFT+TAB was pressed, then edit the next
         * editable cell to the left of this cell, if one exists.
         */
        if (dbTable.isEditing()) {
            int editedColumn = jTable.getEditingColumn();
            dbTable.stopCellEditing();
//            ((DefaultCellEditor) ((TableColumn) tableColumns.get(jTable.getEditingColumn())).getCellEditor()).stopCellEditing();
//1                dbTable.isEditing = false;
            if (editedColumn != 0) { // dbTable.tableFields.size()) {
                /* Find the next field which can be edited
                 */
                boolean fieldFound = false;
                int i = 1;
                while (true) {
                    AbstractTableField fld1 = (AbstractTableField) tableFields.get(jTable.convertColumnIndexToModel(editedColumn - i));
                    if (fld1.isEditable()) {
                        if (table.isHiddenFieldsDisplayed() || !fld1.isHidden()) {
                            fieldFound = true;
                            break;
                        }else{
                            i++;
                            if ((editedColumn-i) < 0)
                                break;
                        }
                    }else{
                        i++;
                        if ((editedColumn-i) < 0)
                            break;
                    }
                }
                if (fieldFound) {
                    int selectedRow = activeRow;
                    for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                        dbTable.colSelectionStatus.set(m, Boolean.FALSE);
                    dbTable.colSelectionStatus.set(editedColumn-i, Boolean.TRUE);
                    jTable.getColumnModel().getSelectionModel().setSelectionInterval(editedColumn-i, editedColumn-i);
                    dbTable.editCellAt(selectedRow, editedColumn-i);
                    jTable.scrollRectToVisible(jTable.getCellRect(selectedRow, editedColumn-i, true));
                    AbstractTableField fld = (AbstractTableField) tableFields.get(jTable.convertColumnIndexToModel(editedColumn-i));

                    if (fld.getDataType().equals(CImageIcon.class)) {
                        int fieldIndex = jTable.convertColumnIndexToModel(editedColumn-i);
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
                    if (dbTable.table.getRecordEntryStructure().isRecordAdditionPending())
                        dbTable.commitNewRecord();
                    else{
                        int prevRowIndex = dbTable.activeRow - 1;
                        if (prevRowIndex >= 0) {
                            /* Find the first non-hidden column in the DBTable, starting from its end, if
                             * the hidden fields are not displayed.
                             */
                            int prevColIndex = jTable.getColumnCount()-1;
                            if (!table.isHiddenFieldsDisplayed()) {
//1                                Array columnOrder = dbTable.viewStructure.tableView.getColumnOrder();
                                while (prevColIndex >= 0) {
                                    AbstractTableField fld1 = dbTable.getTableField(prevColIndex);
//1                                    try{
//1                                        AbstractTableField fld1 = table.getTableField(((Integer) columnOrder.at(prevColIndex)).intValue());
                                        if (!fld1.isHidden()) {
                                            break;
                                        }
//1                                    }catch (InvalidFieldIndexException exc) {
//1                                        System.out.println("Serious inconsistency error in DBTable DBTable(): 1.6");
//1                                    }
                                    prevColIndex--;
                                }
                            }
                            if (prevColIndex >= 0) {
                                for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                                    dbTable.colSelectionStatus.set(m, Boolean.FALSE);
                                dbTable.colSelectionStatus.set(prevColIndex, Boolean.TRUE);
                                jTable.getColumnModel().getSelectionModel().setSelectionInterval(prevColIndex, prevColIndex);
                                jTable.scrollRectToVisible(jTable.getCellRect(prevRowIndex, prevColIndex, true));
                                dbTable.setActiveRow(prevRowIndex);
                            }
                        }
                    }
                }
            }else{
                if (dbTable.table.getRecordEntryStructure().isRecordAdditionPending())
                    if (!dbTable.commitNewRecord())
                        return;
                int prevRowIndex = activeRow - 1; //jTable.getSelectedRow() - 1;
                if (prevRowIndex >= 0) {
                    /* Find the first non-hidden column in the DBTable, starting from its end, if
                     * the hidden fields are not displayed.
                     */
                    int prevColIndex = jTable.getColumnCount()-1;
                    if (!table.isHiddenFieldsDisplayed()) {
//1                        Array columnOrder = dbTable.viewStructure.tableView.getColumnOrder();
                        while (prevColIndex >= 0) {
//1                            try{
                                AbstractTableField fld1 = dbTable.getTableField(prevColIndex);
//1                                AbstractTableField fld1 = table.getTableField(((Integer) columnOrder.at(prevColIndex)).intValue());
                                if (!fld1.isHidden()) {
                                    break;
                                }
//1                            }catch (InvalidFieldIndexException exc) {
//1                                System.out.println("Serious inconsistency error in DBTable DBTable(): 1.6");
//1                            }
                            prevColIndex--;
                        }
                    }
                    if (prevColIndex >= 0) {
                        for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
                            dbTable.colSelectionStatus.set(m, Boolean.FALSE);
                        dbTable.colSelectionStatus.set(prevColIndex, Boolean.TRUE);
                        jTable.getColumnModel().getSelectionModel().setSelectionInterval(prevColIndex, prevColIndex);
                        jTable.scrollRectToVisible(jTable.getCellRect(prevRowIndex, prevColIndex, true));
                        dbTable.setActiveRow(prevRowIndex);
                    }
                }
            }
        }
    }

}
