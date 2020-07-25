package gr.cti.eslate.database.query;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableColumnModel;

public class TextFieldKeyListener implements KeyListener{

    private QueryComponent queryComponent;

    public TextFieldKeyListener() {
    }
    public void keyReleased(KeyEvent e) {
        String s = ((JTextField) e.getSource()).getText();
        if (s == null || s.length() == 0) {
            if (queryComponent.execute.isEnabled()) {
                int activeTableIndex = queryComponent.queryViewStructure.activeTableIndex;
                JTable qTable1 = queryComponent.queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
                int colIndex = qTable1.getEditingColumn();
                int rowIndex = qTable1.getEditingRow();
                if (rowIndex != -1 && colIndex != -1) {
                       ((QTableModel) qTable1.getModel()).setValueAt("", rowIndex, colIndex);
                }
                String s1 = queryComponent.createQuery(activeTableIndex);
                if (s1 == null || s1.trim().length() == 0) {
                    queryComponent.execute.setEnabled(false);
                    queryComponent.clearQuery.setEnabled(false);
                }
            }
        } else {
            if (!queryComponent.execute.isEnabled()) {
                queryComponent.execute.setEnabled(true);
                queryComponent.clearQuery.setEnabled(true);
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            queryComponent.removeRow.setEnabled(false);
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            int activeTableIndex = queryComponent.queryViewStructure.activeTableIndex;
            JTable qTable1 = queryComponent.queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
            ((DefaultCellEditor) ((DefaultTableColumnModel) qTable1.getColumnModel()).getColumn(qTable1.getEditingColumn()).getCellEditor()).cancelCellEditing();
            queryComponent.removeRow.setEnabled(false);
            queryComponent.checkToolStatus();
        }
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
            int activeTableIndex = queryComponent.queryViewStructure.activeTableIndex;
            if (!e.isShiftDown()) {
                JTable qTable1 = queryComponent.queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
                int editingRow = qTable1.getEditingRow();
                int editingColumn = qTable1.getEditingColumn();
                ((DefaultCellEditor) ((DefaultTableColumnModel) qTable1.getColumnModel()).getColumn(qTable1.getEditingColumn()).getCellEditor()).stopCellEditing();
                if (qTable1.getColumnCount() == editingColumn + 1) {
                    if (qTable1.getRowCount() == editingRow + 1)
                        return;
                    else{
                        qTable1.setColumnSelectionInterval(0, 0);
                        qTable1.setRowSelectionInterval(editingRow+1, editingRow+1);
                        qTable1.editCellAt(editingRow+1, 0);
                        qTable1.scrollRectToVisible(qTable1.getCellRect(editingRow+1, 0, true));
                    }
                }else{
                    qTable1.setColumnSelectionInterval(editingColumn+1, editingColumn+1);
                    qTable1.setRowSelectionInterval(editingRow, editingRow);
                    qTable1.editCellAt(editingRow, editingColumn+1);
                    qTable1.scrollRectToVisible(qTable1.getCellRect(editingRow, editingColumn+1, true));
                }
            }else{ // isShiftDown()
                JTable qTable1 = queryComponent.queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
                int editingRow = qTable1.getEditingRow();
                int editingColumn = qTable1.getEditingColumn();
                ((DefaultCellEditor) ((DefaultTableColumnModel) qTable1.getColumnModel()).getColumn(qTable1.getEditingColumn()).getCellEditor()).stopCellEditing();
                if (editingColumn == 0) {
                    if (editingRow == 0)
                        return;
                    else{
                        int lastColumnIndex = qTable1.getColumnCount()-1;
                        qTable1.setColumnSelectionInterval(lastColumnIndex, lastColumnIndex);
                        qTable1.setRowSelectionInterval(editingRow-1, editingRow-1);
                        qTable1.editCellAt(editingRow-1, lastColumnIndex);
                        qTable1.scrollRectToVisible(qTable1.getCellRect(editingRow-1, lastColumnIndex, true));
                    }
                }else{
                    qTable1.setColumnSelectionInterval(editingColumn-1, editingColumn-1);
                    qTable1.setRowSelectionInterval(editingRow, editingRow);
                    qTable1.editCellAt(editingRow, editingColumn-1);
                    qTable1.scrollRectToVisible(qTable1.getCellRect(editingRow, editingColumn-1, true));
                }
            }
        }
    }
    public void keyTyped(KeyEvent e) {
        return;
    }


}