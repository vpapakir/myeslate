package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.base.CurrencyManager;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.eslate.utils.ESlateFieldMap;

import javax.swing.table.TableColumn;
import javax.swing.*;
import java.awt.*;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

/**
 * Created by IntelliJ IDEA.
 * User: yiorgos
 * Date: 28 Éïõë 2003
 * Time: 11:29:30 ðì
 * To change this template use Options | File Templates.
 */
public class DoubleDBTableColumn extends NumericDBTableColumn {
    public static final int FORMAT_VERSION = 1;

    public DoubleDBTableColumn(DBTable dbTable, TableColumn tableColumn, DoubleTableField tableField) {
        this.dbTable = dbTable;
        this.tableColumn = tableColumn;
        this.tableField = tableField;

        renderer = new DoubleColumnRenderer(dbTable);
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        renderer.setForegroundColor(dbTable.doubleColor);
        tableColumn.setCellRenderer(renderer);

        JTextField tf = new TextCellEditor();
        editorComponent = tf;
        editor = new DoubleColumnEditor(tf, dbTable);
        tableColumn.setCellEditor(editor);

        setCurrency(tableField.getCurrency());
    }

    public ESlateFieldMap2 recordState() {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        super.recordState(fieldMap);
        return fieldMap;
    }

    public void applyState(StorageStructure ss) {
        super.applyState(ss);
    }

    class DoubleColumnRenderer extends DBCellRenderer {
        public DoubleColumnRenderer(DBTable dbTable) {
            super(dbTable);
        }

        public Component getTableCellRendererComponent (JTable jTable,
                                                        Object value,            // value to display
                                                        boolean isSelected,      // is the cell selected
                                                        boolean cellHasFocus,    // the jTable and the cell have the focus
                                                        int row,
                                                        int column)
        {
            if (value != null) {
//System.out.println(tableField.getName() + ", currency: " + currency + ", symbol: " + currencySymbol + ", value: " + value);
                if (currency != -1) {
                    double val = ((Double) value).doubleValue();
                    val = CurrencyManager.getCurrencyManager().convert(val, ((CurrencyField) tableField).getCurrency(), currency);
                    TableNumberFormat numberFormat = table.getNumberFormat();
                    // Record the maximum number of franction digits of the number format of the Table
                    int maxFranctionDigits = numberFormat.getMaximumFractionDigits();
                    // Overwrite the maximum number of digits with the value of the NumericTableColumn
                    numberFormat.setMaximumFractionDigits(maximumFractionDigits);
                    StringBuffer valBuff = new StringBuffer(numberFormat.format(val));
                    // Restore the previous value of the maximumFranctionDigits of the number format of the Table.
                    numberFormat.setMaximumFractionDigits(maxFranctionDigits);
                    if (currencyDisplayMode == DISPLAY_CURRENCY_SYMBOL_IN_CELL)
                        valBuff.append(' ').append(currencySymbol);
                    value = valBuff.toString();
                }else if (percentageFormatUsed) {
                    double val = ((Double) value).doubleValue();
                    val = val * 100d;
                    TableNumberFormat numberFormat = table.getNumberFormat();
                    // Record the maximum number of franction digits of the number format of the Table
                    int maxFranctionDigits = numberFormat.getMaximumFractionDigits();
                    // Overwrite the maximum number of digits with the value of the NumericTableColumn
                    numberFormat.setMaximumFractionDigits(maximumFractionDigits);
                    value = numberFormat.format(val);
                    // Restore the previous value of the maximumFranctionDigits of the number format of the Table.
                    numberFormat.setMaximumFractionDigits(maxFranctionDigits);
                    value = value + "%";
                }else{
                    value = table.getNumberFormat().format(value);
                }
                setText((String)value);
            }else{
                setText("");
                setIcon(null);
            }

            super.configureCell(jTable, row, column, isSelected);
            return this;
        }
    }
}

class DoubleColumnEditor extends DefaultCellEditor {
    DBTable dbTable = null;

    public DoubleColumnEditor(JTextField tf, DBTable dbTable) {
        super(tf);
        this.dbTable = dbTable;
        tf.setFont(dbTable.tableFont);
    }

    public Component getTableCellEditorComponent(JTable jTable, Object value,
                                                 boolean isSelected,
                                                 int row, int column) {

        if (value != null) {
            value = dbTable.table.getNumberFormat().format(value);
        }else{
            value = "";
        }
        return super.getTableCellEditorComponent(jTable, value, isSelected, row, column);
    }
}

