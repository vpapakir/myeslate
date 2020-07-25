package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.DoubleTableField;
import gr.cti.eslate.database.engine.IntegerTableField;
import gr.cti.eslate.database.engine.CurrencyField;
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
 * Time: 11:30:50 ðì
 * To change this template use Options | File Templates.
 */
public class IntegerDBTableColumn extends NumericDBTableColumn {
    public static final int FORMAT_VERSION = 1;
    int maximumFractionDigits = 0;

    public IntegerDBTableColumn(DBTable dbTable, TableColumn tableColumn, IntegerTableField tableField) {
        this.dbTable = dbTable;
        this.tableColumn = tableColumn;
        this.tableField = tableField;

        renderer = new IntegerColumnRenderer(dbTable);
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        renderer.setForegroundColor(dbTable.integerColor);
        tableColumn.setCellRenderer(renderer);

        JTextField tf = new TextCellEditor();
        editorComponent = tf;
        editor = new IntegerColumnEditor(tf, dbTable);
        tableColumn.setCellEditor(editor);

        setCurrency(tableField.getCurrency());
    }


    /**
     * The maximum number of franction digits for a column which displays integer data is always 0.
     * @param numOfDigits
     */
    public void setMaximumFractionDigits(int numOfDigits) {
        super.setMaximumFractionDigits(0);
    }

    public ESlateFieldMap2 recordState() {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        super.recordState(fieldMap);
        return fieldMap;
    }

    public void applyState(StorageStructure ss) {
        super.applyState(ss);
    }

    class IntegerColumnRenderer extends DBCellRenderer {
        public IntegerColumnRenderer(DBTable dbTable) {
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
                if (currency != -1) {
                    int val = ((Integer) value).intValue();
                    val = (int) CurrencyManager.getCurrencyManager().convert(val, ((CurrencyField) tableField).getCurrency(), currency);
                    StringBuffer valBuff = new StringBuffer(table.getNumberFormat().format(val));
                    if (currencyDisplayMode == DISPLAY_CURRENCY_SYMBOL_IN_CELL)
                        valBuff.append(' ').append(currencySymbol);
                    value = valBuff.toString();
                }else if (percentageFormatUsed) {
                    int val = ((Integer) value).intValue();
                    val = val * 100;
                    value = table.getNumberFormat().format(val);
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

class IntegerColumnEditor extends DefaultCellEditor {
    DBTable dbTable = null;

    public IntegerColumnEditor(JTextField tf, DBTable dbTable) {
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


