package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import gr.cti.eslate.database.engine.*;

public class F2Action extends AbstractAction {
    DBTable dbTable;

    public F2Action(DBTable table, String name) {
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, false));
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    protected void execute() {
        JTable jTable = dbTable.jTable;
        Table table = dbTable.table;

        if (!dbTable.isEditing()) {
            if (dbTable.activeRow == -1) //jTable.getSelectedRow();
                return;
            dbTable.editCellAt(dbTable.activeRow, jTable.getSelectedColumn());
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
                    ((DBTableModel) jTable.getModel()).setValueAt(fileName, dbTable.activeRow, fieldIndex);
//                    jTable.paintImmediately(jTable.getCellRect(selectedRecord, fieldIndex, true));
                    jTable.repaint(jTable.getCellRect(dbTable.activeRow, fieldIndex, true));
                }
            }
        }
    }

}
