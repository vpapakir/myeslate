package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.base.CurrencyManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This action toggles the currency aspect of a numeric field. A numeric field (DoubleTableField, FloatTableField,
 * IntegerTableField) can become as a currency field (a field which supports multiple currencies).
 * User: yiorgos
 * Date: 30 Ιουλ 2003
 * Time: 3:03:31 μμ
 * This class changes a
 */
public class FieldCurrencyAction extends AbstractAction {
    DBTable dbTable;

    public FieldCurrencyAction(DBTable dbTable, String name){
        super(name);
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        dbTable.closeDatabasePopupMenu();
        dbTable.stopCellEditing();

        int columnIndex = dbTable.jTable.getSelectedColumn();
        if (columnIndex == -1) return;

        AbstractDBTableColumn column = dbTable.tableColumns.get(columnIndex);
//        int fieldIndex = dbTable.jTable.convertColumnIndexToModel(columnIndex);
//        AbstractTableField f = dbTable.table.getFields().get(fieldIndex);
//        Class dataType = f.getDataType();
//        if (dataType != DoubleTableField.DATA_TYPE && dataType != FloatTableField.DATA_TYPE && dataType != IntegerTableField.DATA_TYPE)
        if (!(column.tableField instanceof CurrencyField))
            return;

        CurrencyField f = (CurrencyField) column.tableField;
        if (f.getCurrency() == -1)
            f.setCurrency(CurrencyManager.EUR);
        else
            f.setCurrency(-1);

    }
}
