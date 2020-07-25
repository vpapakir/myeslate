package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.StringTableField;
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
 * Time: 11:37:37 ðì
 * To change this template use Options | File Templates.
 */
public class StringDBTableColumn extends AbstractDBTableColumn {
    public static final int FORMAT_VERSION = 1;

    public StringDBTableColumn(DBTable dbTable, TableColumn tableColumn, StringTableField tableField) {
        this.tableColumn = tableColumn;
        this.tableField = tableField;

        renderer = new StringColumnRenderer(dbTable);
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        tableColumn.setCellRenderer(renderer);

        JTextField tf = new TextCellEditor();
        editorComponent = tf;
        editorComponent.setFont(dbTable.tableFont);
        editor = new DefaultCellEditor(tf);
        tableColumn.setCellEditor(editor);
    }

    public ESlateFieldMap2 recordState() {
        return null;
    }

    public void applyState(StorageStructure ss) {
    }
}

class StringColumnRenderer extends DBCellRenderer {
    public StringColumnRenderer(DBTable dbTable) {
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
            setText((String) value);
        }else{
            setText("");
            setIcon(null);
        }

        super.configureCell(jTable, row, column, isSelected);
        return this;
    }
}

