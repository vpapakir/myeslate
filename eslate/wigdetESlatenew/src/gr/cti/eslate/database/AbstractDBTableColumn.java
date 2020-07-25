package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.utils.ESlateClipboard;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import javax.swing.table.TableColumn;
import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.io.Externalizable;

/**
 * Created by IntelliJ IDEA.
 * User: yiorgos
 * Date: 28 Éïõë 2003
 * Time: 11:18:21 ðì
 * To change this template use Options | File Templates.
 */
public abstract class AbstractDBTableColumn {
    TableColumn tableColumn = null;
    AbstractTableField tableField = null;
//    ColumnRendererEditor colRendererEditor = null;
    DBCellRenderer renderer = null;
    DefaultCellEditor editor = null;
    JComponent editorComponent = null;


    public static AbstractDBTableColumn createDBTableColumn(DBTable dbTable, TableColumn column, AbstractTableField tableField) {
        Class dataType = tableField.getDataType();
        AbstractDBTableColumn dbTableColumn = null;
        if (dataType == DoubleTableField.DATA_TYPE)
            dbTableColumn = new DoubleDBTableColumn(dbTable, column, (DoubleTableField) tableField);
        if (dataType == IntegerTableField.DATA_TYPE)
            dbTableColumn = new IntegerDBTableColumn(dbTable, column, (IntegerTableField) tableField);
        if (dataType == FloatTableField.DATA_TYPE)
            dbTableColumn = new FloatDBTableColumn(dbTable, column, (FloatTableField) tableField);
        if (dataType == StringTableField.DATA_TYPE)
            dbTableColumn = new StringDBTableColumn(dbTable, column, (StringTableField) tableField);
        if (dataType == DateTableField.DATA_TYPE)
            dbTableColumn = new DateDBTableColumn(dbTable, column, (DateTableField) tableField);
        if (dataType == TimeTableField.DATA_TYPE)
            dbTableColumn = new TimeDBTableColumn(dbTable, column, (TimeTableField) tableField);
        if (dataType == BooleanTableField.DATA_TYPE)
            dbTableColumn = new BooleanDBTableColumn(dbTable, column, (BooleanTableField) tableField);
        if (dataType == URLTableField.DATA_TYPE)
            dbTableColumn = new URLDBTableColumn(dbTable, column, (URLTableField) tableField);
        if (dataType == ImageTableField.DATA_TYPE)
            dbTableColumn = new ImageDBTableColumn(dbTable, column, (ImageTableField) tableField);

        if (dbTableColumn != null) {
            dbTableColumn.renderer.setFont(dbTable.tableFont);
            dbTableColumn.renderer.setSelectedRecordsBackgroundDrawn(dbTable.selectedRecordDrawMode == DBTable.ON_TABLE_ONLY || dbTable.selectedRecordDrawMode == DBTable.ON_ROW_BAR_AND_TABLE);
            dbTableColumn.renderer.setActiveRecordBackgroundDrawn(dbTable.activeRecordDrawMode == DBTable.ON_TABLE_ONLY || dbTable.activeRecordDrawMode == DBTable.ON_ROW_BAR_AND_TABLE);
//            createRendererEditor(dbTableColumn, dbTable, tableField, column);
        }

        return dbTableColumn;
    }

    public Class getDataType() {
        return tableField.getDataType();
    }

    public AbstractTableField getTableField() {
        return tableField;
    }

    public TableColumn getTableColumn() {
        return tableColumn;
    }

    public abstract ESlateFieldMap2 recordState();


    public abstract void applyState(StorageStructure ss);

/*    private void createCaretListener() {
        if (editorComponent.getClass().getName().equals("javax.swing.JTextField")) {
            final JTextField tf = (JTextField) editorComponent;
            tf.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    if (!dbTable.isEditing())
                        return;
                    if (dbTable.dbComponent == null) return;

                    if (tf.getSelectedText() == null) {
                        dbTable.dbComponent.standardToolbarController.setCutEnabled(false);
                        dbTable.dbComponent.standardToolbarController.setCopyEnabled(false);
                    }else{
                        dbTable.dbComponent.standardToolbarController.setCutEnabled(true);
                        dbTable.dbComponent.standardToolbarController.setCopyEnabled(true);
                    }
                    if (ESlateClipboard.getContents() != null)
                        dbTable.dbComponent.standardToolbarController.setPasteEnabled(true);
                    else
                        dbTable.dbComponent.standardToolbarController.setPasteEnabled(false);
                }
            });
        }
    }
*/
}

// Custom cell editor for text values. This is needed in order to capture the TAB key event to call the
// respective action of the DBTable. Otherwise the Focus manager catches the TAB event and moves the focus
// to the next focusable component.
class TextCellEditor extends JTextField {
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        if (e.getID() != KeyEvent.KEY_PRESSED) return;
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
/*                if (!e.isShiftDown())
                    dbTable.tabAction.execute();
                else
                    dbTable.shiftTabAction.execute();
                e.consume();
*/
        }
    }
}
