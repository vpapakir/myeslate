package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.BooleanTableField;
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
 * Date: 28 Ιουλ 2003
 * Time: 12:25:55 μμ
 * To change this template use Options | File Templates.
 */
public class BooleanDBTableColumn extends AbstractDBTableColumn {
    public static final int FORMAT_VERSION = 1;
    BooleanValues booleanValues = null;

    public BooleanDBTableColumn(DBTable dbTable, TableColumn tableColumn, BooleanTableField tableField) {
        this.tableColumn = tableColumn;
        this.tableField = tableField;

        renderer = new BooleanColumnRenderer(dbTable);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setForegroundColor(dbTable.booleanColor);
        tableColumn.setCellRenderer(renderer);

        booleanValues = new BooleanValues();
        editorComponent = booleanValues;
        editor = new DefaultCellEditor(booleanValues);
        editorComponent.setFont(dbTable.tableFont);
        tableColumn.setCellEditor(editor);
    }

    public ESlateFieldMap2 recordState() {
        return null;
    }

    public void applyState(StorageStructure ss) {
    }
}

class BooleanColumnRenderer extends DBCellRenderer {
    public BooleanColumnRenderer(DBTable dbTable) {
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
            setText(dbTable.dbComponent.infoBundle.getString(value.toString()));
        }else{
            setText("");
            setIcon(null);
        }

        super.configureCell(jTable, row, column, isSelected);
        return this;
    }
}


