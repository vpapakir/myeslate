package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.DateTableField;
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
 * Time: 11:39:55 ðì
 * To change this template use Options | File Templates.
 */
public class DateDBTableColumn extends AbstractTimeDBTableColumn {
    public static final int FORMAT_VERSION = 1;

    public DateDBTableColumn(DBTable dbTable, TableColumn tableColumn, DateTableField tableField) {
        this.tableColumn = tableColumn;
        this.tableField = tableField;

        renderer = new DateColumnRenderer(dbTable);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setForegroundColor(dbTable.dateColor);
        tableColumn.setCellRenderer(renderer);

        JTextField tf = new TextCellEditor();
        editorComponent = tf;
        editor = new DateColumnEditor(tf, dbTable);
        tableColumn.setCellEditor(editor);
    }

    public ESlateFieldMap2 recordState() {
        return null;
    }

    public void applyState(StorageStructure ss) {
    }
}

class DateColumnRenderer extends DBCellRenderer {
    public DateColumnRenderer(DBTable dbTable) {
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
            setText(value.toString());
        }else{
            setText("");
            setIcon(null);
        }

        super.configureCell(jTable, row, column, isSelected);
        return this;
    }
}

class DateColumnEditor extends DefaultCellEditor {
    DBTable dbTable = null;

    public DateColumnEditor(JTextField tf, DBTable dbTable) {
        super(tf);
        this.dbTable = dbTable;
        tf.setFont(dbTable.tableFont);
    }

    public Component getTableCellEditorComponent(JTable jTable, Object value,
                                                 boolean isSelected,
                                                 int row, int column) {

        if (value != null) {
            value = dbTable.table.getDateFormat().format(value);
        }else{
            value = "";
        }
        return super.getTableCellEditorComponent(jTable, value, isSelected, row, column);
    }
}


