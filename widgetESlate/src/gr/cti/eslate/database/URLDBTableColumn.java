package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.URLTableField;
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
 * Time: 11:38:57 ðì
 * To change this template use Options | File Templates.
 */
public class URLDBTableColumn extends AbstractDBTableColumn {
    public static final int FORMAT_VERSION = 1;

    public URLDBTableColumn(DBTable dbTable, TableColumn tableColumn, URLTableField tableField) {
        this.tableColumn = tableColumn;
        this.tableField = tableField;

        renderer = new URLColumnRenderer(dbTable);
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        renderer.setForegroundColor(dbTable.urlColor);
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

class URLColumnRenderer extends DBCellRenderer {
    public URLColumnRenderer(DBTable dbTable) {
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

