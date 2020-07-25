package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import gr.cti.eslate.database.engine.*;
import java.util.ArrayList;

public class EnterAction extends AbstractAction {
    DBTable dbTable;

    public EnterAction(DBTable table, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false));
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        JTable jTable = dbTable.jTable;
        Table table = dbTable.table;
//cf        ArrayList tableColumns = dbTable.tableColumns;

        dbTable.tipManager.resetTip();
        if (!dbTable.isEditing()) {
            int selectedRecord = dbTable.activeRow; // jTable.getSelectedRow();
            dbTable.editCellAt(selectedRecord, jTable.getSelectedColumn());
            AbstractTableField fld;
            int fieldIndex;
            try{
                fieldIndex = jTable.convertColumnIndexToModel(jTable.getSelectedColumn());
                fld = table.getTableField(fieldIndex);

            }catch (InvalidFieldIndexException exc) {return;}

            /* If the field is of Boolean data type, show the cell editor's pop-up
             */
//                         if (fld.getFieldType().equals(java.lang.Boolean.class))
//                            ((BooleanValues) ((DefaultCellEditor) ((TableColumn) tableColumns.at(jTable.getEditingColumn())).getCellEditor()).getComponent()).showPopup();

            if (fld.getDataType().equals(CImageIcon.class)) {
//1                dbTable.isEditing = false;
                //IE tests
                if (dbTable.iconFileDialog != null && dbTable.iconFileDialog.getFile() != null) {
                    String fileName = dbTable.iconFileDialog.getDirectory() + dbTable.iconFileDialog.getFile();
                    ((DBTableModel) jTable.getModel()).setValueAt(fileName, selectedRecord, fieldIndex);
//                    jTable.paintImmediately(jTable.getCellRect(selectedRecord, fieldIndex, true));
                    jTable.repaint(jTable.getCellRect(selectedRecord, fieldIndex, true));
                }
            }
        }else{
           /* If the edited cell is a Boolean one, then the editorComponent does not have the focus.
            * So there is no KeyListener attached to it, as for the rest of the editor components
            * -see ColumnRendererEditor. Therefore here cell editing is stopped.
            */
            dbTable.stopCellEditing();
//1            ((DefaultCellEditor) ((TableColumn) tableColumns.get(jTable.getEditingColumn())).getCellEditor()).stopCellEditing();
//1            dbTable.isEditing = false;
        }
    }
}
