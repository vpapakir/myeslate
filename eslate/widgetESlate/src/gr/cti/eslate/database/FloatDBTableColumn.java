package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.FloatTableField;
import gr.cti.eslate.database.engine.CurrencyField;
import gr.cti.eslate.database.engine.TableNumberFormat;
import gr.cti.eslate.base.CurrencyManager;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

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
 * Time: 11:36:40 ðì
 * To change this template use Options | File Templates.
 */
public class FloatDBTableColumn extends NumericDBTableColumn {
    public static final int FORMAT_VERSION = 1;

    public FloatDBTableColumn(DBTable dbTable, TableColumn tableColumn, FloatTableField tableField) {
        this.dbTable = dbTable;
        this.tableColumn = tableColumn;
        this.tableField = tableField;

        renderer = new FloatColumnRenderer(dbTable);
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        renderer.setForegroundColor(dbTable.floatColor);
        tableColumn.setCellRenderer(renderer);

        JTextField tf = new TextCellEditor();
        editorComponent = tf;
        editor = new FloatColumnEditor(tf, dbTable);
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

    class FloatColumnRenderer extends DBCellRenderer {
        public FloatColumnRenderer(DBTable dbTable) {
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
//System.out.println("currency: " + currency + ", symbol: " + currencySymbol + ", value: " + value);
                if (currency != -1) {
                    float val = ((Float) value).floatValue();
                    val = (float) CurrencyManager.getCurrencyManager().convert(val, ((CurrencyField) tableField).getCurrency(), currency);
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
                    float val = ((Float) value).floatValue();
                    val = val * 100f;
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

class FloatColumnEditor extends DefaultCellEditor {
    DBTable dbTable = null;

    public FloatColumnEditor(JTextField tf, DBTable dbTable) {
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

