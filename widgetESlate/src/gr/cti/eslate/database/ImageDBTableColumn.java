package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.ImageTableField;
import gr.cti.eslate.database.engine.CImageIcon;
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
 * Time: 11:41:14 ðì
 * To change this template use Options | File Templates.
 */
public class ImageDBTableColumn extends AbstractDBTableColumn {
    public static final int FORMAT_VERSION = 1;

    public ImageDBTableColumn(DBTable dbTable, TableColumn tableColumn, ImageTableField tableField) {
        this.tableColumn = tableColumn;
        this.tableField = tableField;

        renderer = new ImageColumnRenderer(dbTable);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        tableColumn.setCellRenderer(renderer);
    }

    public ESlateFieldMap2 recordState() {
        return null;
    }

    public void applyState(StorageStructure ss) {
    }
}

class ImageColumnRenderer extends DBCellRenderer {
    public ImageColumnRenderer(DBTable dbTable) {
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
            setText("");
            Icon temp = ((CImageIcon) value).getIcon();
            setIcon(temp);
        }else{
            setText("");
            setIcon(null);
        }

        super.configureCell(jTable, row, column, isSelected);
        return this;
    }
}


