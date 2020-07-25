package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import com.objectspace.jgl.Array;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import java.awt.Color;
import java.util.ArrayList;

import gr.cti.eslate.database.engine.TableFieldBaseArray;
import gr.cti.typeArray.BoolBaseArray;

public class SortAscAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    String actionName;

    public SortAscAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/sortAsc.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/sortAscDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public SortAscAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
              dBase.visiblePopupMenu.setVisible(false);
/* If a jTable cell was edited when the button was pressed, then stop the cell editing.
           * This results in the evaluation of the edited value of the cell.
           */
          if (dBase.activeDBTable != null && dBase.activeDBTable.jTable.isEditing())
              ((DefaultCellEditor) dBase.activeDBTable.tableColumns.get(dBase.activeDBTable.jTable.getEditingColumn()).tableColumn.getCellEditor()).stopCellEditing();

          if (dBase.activeDBTable != null) {
              int[] fieldIndices = dBase.activeDBTable.jTable.getSelectedColumns();
              if (fieldIndices.length < 1 || fieldIndices.length > 3)
                  return;

              dBase.statusToolbarController.setMessageLabelInWaitState();
              dBase.dbComponent.setCursor(waitCursor);
//1              Array columnOrder = dBase.activeDBTable.viewStructure.tableView.getColumnOrder();
              if (fieldIndices.length == 1) {
                  String fldName = dBase.activeDBTable.getTableField(fieldIndices[0]).getName();
//1                  String fldName = (String) dBase.activeDBTable.tableModel.getColumnName(((Integer) columnOrder.at(fieldIndices[0])).intValue());
                dBase.activeDBTable.sortOnField(fldName, true, 0, dBase.activeDBTable.table.getRecordCount()-1, true);
                dBase.activeDBTable.refresh();
              }else{
                  TableFieldBaseArray fields = new TableFieldBaseArray();
                  BoolBaseArray ascending = new BoolBaseArray();
//                  ArrayList fieldNames = new ArrayList();
//                  ArrayList sortOrder = new ArrayList();
                  for (int i=0; i<fieldIndices.length; i++) {
                      fields.add(dBase.activeDBTable.getTableField(fieldIndices[i]));
//                      fieldNames.add((String) dBase.activeDBTable.tableModel.getColumnName(((Integer) columnOrder.at(fieldIndices[i])).intValue()));
                      ascending.add(true);
                  }
                  try{
                    dBase.activeDBTable.sortOnMultipleFields(fields, ascending);
                  }catch (Throwable thr) {
                      thr.printStackTrace();
                  }
              }
              dBase.dbComponent.setCursor(defaultCursor);
              dBase.statusToolbarController.setMessageLabelColor(Color.white);
              dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
          }
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setSortAscEnabled(b);
        dBase.menu.miSortAscending.setEnabled(b);
    }
}
