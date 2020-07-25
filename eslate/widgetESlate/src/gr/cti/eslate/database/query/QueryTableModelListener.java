package gr.cti.eslate.database.query;

import gr.cti.eslate.tableModel.event.*;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.base.*;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import javax.swing.table.*;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;


public class QueryTableModelListener implements DatabaseTableModelListener {
    QueryComponent queryComponent;

    public QueryTableModelListener(QueryComponent comp) {
        queryComponent = comp;
    }

    public void columnKeyChanged(ColumnKeyChangedEvent e) {
        Table table = (Table) e.getSource();
        int index = queryComponent.queryViewStructure.getTableIndex(table);
        if (index < 0) {
            return;
        }

        JTable qTable = queryComponent.queryViewStructure.getQTablePanel(table).getQTable();
        TableColumn col = qTable.getColumn(e.getColumnName());
        AbstractTableField f = null;
        try{
            f = table.getTableField(e.getColumnName());
        } catch (InvalidFieldNameException e1) {
            System.out.println("Serious inconsistence error in QueryComponent fieldTypeChanged: (1)");
            return;
        }
        ((QueryHeaderRenderer) col.getHeaderRenderer()).setIcon(f.getDataType(), !table.isPartOfTableKey(f)/*1f.isKey()*/, queryComponent);
        qTable.getTableHeader().paintImmediately(qTable.getTableHeader().getVisibleRect());
    }

    public void calcColumnReset(CalcColumnResetEvent e) {
        Table table = (Table) e.getSource();
        int index = queryComponent.queryViewStructure.getTableIndex(table);
        if (index < 0) {
            return;
        }

        JTable qTable = queryComponent.queryViewStructure.getQTablePanel(table).getQTable();
        TableColumn col = qTable.getColumn(e.getColumnName());
        ((QueryHeaderRenderer) col.getHeaderRenderer()).setNormalFieldHeaderFont();
        JTableHeader h = qTable.getTableHeader();
        h.validate();
        h.paintImmediately(h.getVisibleRect());
    }

    public void calcColumnFormulaChanged(CalcColumnFormulaChangedEvent parm1) {
    }

    public void columnEditableStateChanged(ColumnEditableStateChangedEvent parm1) {
    }

    public void columnRemovableStateChanged(ColumnRemovableStateChangedEvent parm1) {
    }

    public void columnHiddenStateChanged(ColumnHiddenStateChangedEvent e) {
        Table table = (Table) e.getSource();
        if (table.isHiddenFieldsDisplayed()) {
            return;
        }
        String fieldName = e.getColumnName();
        AbstractTableField fld;
        try {
            fld = table.getTableField(fieldName);
        } catch (InvalidFieldNameException exc) {
            return;
        }
        int index = queryComponent.queryViewStructure.getTableIndex(table);
        if (index == -1) {
            return;
        }
        JTable qTable = queryComponent.queryViewStructure.getQTablePanel(table).getQTable();
        TableColumn col = qTable.getColumn(e.getColumnName());

        if (e.isHidden()) {
            col.setMinWidth(0);
            col.setWidth(0);
            col.setMaxWidth(0);
            col.setResizable(false);
            queryComponent.queryViewStructure.getTextViewInfo(table).myListModel.removeElement(fld.getName());
    //        ((DefaultListModel) ((JList) queryComponent.fieldListArray.at(visibleIndex)).getModel()).removeElement(fld.getName());
        } else {
            if (!fld.getDataType().getName().equals("gr.cti.eslate.database.engine.CImageIcon")) {
                col.setMaxWidth(1000);
                col.setWidth(70);
                col.setMinWidth(40);
                col.setResizable(true);
                queryComponent.queryViewStructure.getTextViewInfo(table).myListModel.addElement(fld.getName());
         //       ((DefaultListModel) ((JList) queryComponent.fieldListArray.at(visibleIndex)).getModel()).addElement(fld.getName());
            }
        }
        if (index == queryComponent.queryViewStructure.activeTableIndex) {
             queryComponent.textView.loadNewInfo(queryComponent.queryViewStructure.getTextViewInfo(table));
      //  ((JList) queryComponent.fieldListArray.at(visibleIndex)).repaint();
            qTable.getTableHeader().repaint();
            qTable.repaint();
        }
    }

    public void recordAdded(RecordAddedEvent parm1) {
    }

    public void emptyRecordAdded(RecordAddedEvent parm1) {
    }

    public void recordRemoved(RecordRemovedEvent parm1) {
    }

    public void activeRecordChanged(ActiveRecordChangedEvent parm1) {
    }

    public void tableHiddenStateChanged(TableHiddenStateChangedEvent e) {
        Table table = (Table) e.getSource();
        if (queryComponent.db.isHiddenTablesDisplayed()) {
            return;
        }

        if (e.isHidden()) {
            int index = queryComponent.queryViewStructure.getTableIndex(table);
            if (index == -1) {
                return;
            }

            queryComponent.deactivateTableAt(index);
            queryComponent.queryViewStructure.removeTable(table);
            queryComponent.tabs.removeTabAt(index);
            queryComponent.tabs.validate();
            queryComponent.tabs.repaint();

            if (queryComponent.tabs.getTabCount() == 0) {
                queryComponent.queryViewStructure.activeTableIndex = -1;

                if (queryComponent.emptyDB == null) {
                    queryComponent.createEmptyDBPanel();
                    queryComponent.emptyDB.setBounds(0, 0, queryComponent.getSize().width, queryComponent.getSize().height); //-(menu.getHeight()));
                }
                queryComponent.componentPanel.remove(queryComponent.mainPanel);
                queryComponent.mainPanel = null;
                queryComponent.tabs = null;
                queryComponent.componentPanel.add(queryComponent.emptyDB, BorderLayout.CENTER);
                queryComponent.emptyDBVisible = true;

                queryComponent.headerStatusButton.setEnabled(false);
                queryComponent.execute.setEnabled(false);
                queryComponent.clearQuery.setEnabled(false);
                queryComponent.headerStatusButton.setEnabled(false);
                queryComponent.addRow.setEnabled(false);
                queryComponent.queryPane.setSelected(false);
                queryComponent.queryPane.setEnabled(false);
            }

            queryComponent.destroyTableHandle(table.getTitle());
        } else {
            int index = queryComponent.db.toVisibleTableIndex(queryComponent.db.indexOf(table));
            queryComponent.addTable(table, index);
            queryComponent.activateTableAt(index); //queryComponent.queryViewStructure.size()-1);
        }
        queryComponent.refreshQueryComponent();

    }

    public void cellValueChanged(CellValueChangedEvent parm1) {
    }

    public void columnTypeChanged(ColumnTypeChangedEvent e) {
        Table table = (Table) e.getSource();
        int index1 = queryComponent.queryViewStructure.getTableIndex(table);
        if (index1 < 0) {
            return;
        }

        JTable qTable = queryComponent.queryViewStructure.getQTablePanel(table).getQTable();
        TableColumn col = qTable.getColumn(e.getColumnName());
        AbstractTableField f = null;
        try {
            f = table.getTableField(e.getColumnName());
        } catch (InvalidFieldNameException e1) {
            System.out.println("Serious inconsistence error in QueryComponent fieldTypeChanged: (1)");
            return;
        }
        if (f.getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
            col.setMinWidth(0);
            col.setMaxWidth(0);
            col.setWidth(0);
            col.setResizable(false);
//            int index = ((DefaultListModel) ((JList) queryComponent.fieldListArray.at(visibleIndex)).getModel()).indexOf(f.getName());
//          ((DefaultListModel) ((JList) queryComponent.fieldListArray.at(visibleIndex)).getModel()).removeElementAt(index);
            QueryTextViewInfo info = queryComponent.queryViewStructure.getTextViewInfo(table);
            int index = info.myListModel.indexOf(f.getName());
            info.myListModel.removeElementAt(index);
            if (index == queryComponent.queryViewStructure.activeTableIndex) {
                queryComponent.textView.loadNewInfo(info);
//            ((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).validate();
//          ((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).paintImmediately(((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).getVisibleRect());
                qTable.validate();
                qTable.paintImmediately(qTable.getVisibleRect());
            }
        }
        if (e.getPrevType().equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
            col.setMinWidth(40);
            col.setMaxWidth(1000);
            col.setWidth(70);
            col.setResizable(true);

            QueryTextViewInfo info = queryComponent.queryViewStructure.getTextViewInfo(table);
            info.myListModel.addElement(f.getName());
            if (index1 == queryComponent.queryViewStructure.activeTableIndex) {
                queryComponent.textView.loadNewInfo(info);

//            ((DefaultListModel) ((JList) queryComponent.fieldListArray.at(visibleIndex)).getModel()).addElement(f.getName());
  //          ((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).validate();
    //        ((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).paintImmediately(((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).getVisibleRect());
                qTable.validate();
                qTable.paintImmediately(qTable.getVisibleRect());
            }
        }

        ((QueryHeaderRenderer) col.getHeaderRenderer()).setIcon(f.getDataType(), !table.isPartOfTableKey(f)/*1f.isKey()*/, queryComponent);
        qTable.getTableHeader().paintImmediately(qTable.getTableHeader().getVisibleRect());
    }

    public void columnRenamed(ColumnRenamedEvent e) {
        Table table = (Table) e.getSource();
        int index1 = queryComponent.queryViewStructure.getTableIndex(table);
        if (index1 < 0) {
            return;
        }

        JTable qTable = queryComponent.queryViewStructure.getQTablePanel(table).getQTable();
        TableColumn col = qTable.getColumn(e.getOldName());
        col.setIdentifier(e.getNewName());
        col.setHeaderValue(e.getNewName());
        ((QueryHeaderRenderer) col.getHeaderRenderer()).setText(e.getNewName());

        // Update the "fieldListModel" of the "fieldList" in the "textQueryPanel" for this
        // table.
        // Change Working fine just needs proper repaint
        QueryTextViewInfo info = queryComponent.queryViewStructure.getTextViewInfo(table);
        int index = info.myListModel.indexOf(e.getOldName());
        //int index = ((DefaultListModel) ((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).getModel()).indexOf(e.getOldName());
        //((DefaultListModel) ((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).getModel()).setElementAt(e.getNewName(), index);
        info.myListModel.setElementAt(e.getNewName(), index);
        if (index == queryComponent.queryViewStructure.activeTableIndex) {
            queryComponent.textView.loadNewInfo(info);
            JTableHeader h = qTable.getTableHeader();
            h.validate();
            h.paintImmediately(h.getVisibleRect());
        }
    }

    public void columnAdded(ColumnAddedEvent e) {
        Table table = (Table) e.getSource();
        int index = queryComponent.queryViewStructure.getTableIndex(table);
        if (index < 0) {
            return;
        }

        QTablePanel qTablePanel = queryComponent.queryViewStructure.getQTablePanel(table);
        JTable qTable = qTablePanel.getQTable();

        AbstractTableField f = null;
        try{
            f = table.getTableField(table.getFieldCount()-1);
        } catch (InvalidFieldIndexException exc) {
//            System.out.println(errorStr);
        }

        // Exclude "Image" fields.
        TableColumn col = new TableColumn();
        String columnName = null;
        columnName = f.getName();
        col.setIdentifier(columnName);
        col.setHeaderValue(columnName);
        col.setHeaderRenderer(new QueryHeaderRenderer(f.getName(), f.getDataType(), !table.isPartOfTableKey(f)/*1f.isKey()*/, f.isCalculated(), queryComponent));
        qTable.getColumnModel().addColumn(col);
        qTablePanel.getQTableModel().addNewColumn(table.getFieldCount()-1);
//        ((QTableModel) queryComponent.QTableModels.at(visibleIndex)).addNewColumn(table.getFieldCount()-1);
        col.setModelIndex(table.getFieldCount()-1);

        // Setting the cell editor for the new column.
        JTextField tf = new JTextField();
        queryComponent.createTextFieldKeyListener(tf);

        DefaultTableCellRenderer dcr = new DefaultTableCellRenderer();
        dcr.setHorizontalAlignment(SwingConstants.LEFT);
        col.setCellRenderer(dcr);
        DefaultCellEditor dce = new DefaultCellEditor(tf);
        dce.setClickCountToStart(1);
        col.setCellEditor(dce);

        // Exclude "Image" fields and hidden fields.
        if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CImageIcon") ||
           (f.isHidden() && !table.isHiddenFieldsDisplayed())) {
            col.setMinWidth(0);
            col.setMaxWidth(0);
            col.setWidth(0);
            col.setResizable(false);
        } else {
            col.setMinWidth(40);
            col.setMaxWidth(1000);
        }

        int colWidth;
        if ((colWidth = (queryComponent.getSize().width-20) / (qTable.getColumnCount() - table.getImageFieldCount())) > 70) {
            for (int i=0; i<qTable.getColumnCount(); i++) {
                qTable.getColumnModel().getColumn(i).setWidth(colWidth);
            }
        } else {
            for (int i=0; i<qTable.getColumnCount(); i++) {
                qTable.getColumnModel().getColumn(i).setWidth(110);
            }
        }

        qTable.validate();
        qTable.paintImmediately(qTable.getVisibleRect());

        // Update the "fieldListModel" of the "fieldList" in the "textQueryPanel" for this
        //  table.
        QueryTextViewInfo info = queryComponent.queryViewStructure.getTextViewInfo(table);
        info.myListModel.addElement(f.getName());
        if (index == queryComponent.queryViewStructure.activeTableIndex) {
            queryComponent.textView.loadNewInfo(info);

//        ((DefaultListModel) ((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).getModel()).addElement(f.getName());
            queryComponent.headerStatusButton.setEnabled(true);
            queryComponent.addRow.setEnabled(true);
        }
    }

    public void columnReplaced(ColumnReplacedEvent parm1) {
    }

    public void columnRemoved(ColumnRemovedEvent e) {
        Table table = (Table) e.getSource();
        int index = queryComponent.queryViewStructure.getTableIndex(table);
        if (index < 0) {
            return;
        }
        JTable qTable = queryComponent.queryViewStructure.getQTablePanel(table).getQTable();
        TableColumn col = qTable.getColumn(e.getColumnName());
        int modelIndex = col.getModelIndex();
        qTable.removeColumn(col);
        queryComponent.queryViewStructure.getQTablePanel(table).getQTableModel().removeColumn(modelIndex);
//        ((QTableModel) queryComponent.QTableModels.at(visibleIndex)).removeColumn(modelIndex);

        // Decrease the model indices of the columns with greater model index
        // than the one which was removed.
        for (int i=0; i<qTable.getColumnCount(); i++) {
            col = ((DefaultTableColumnModel) qTable.getColumnModel()).getColumn(i);
            if (col.getModelIndex() > modelIndex) {
                col.setModelIndex(col.getModelIndex() - 1);
            }
        }

        // Update the "fieldListModel" of the "fieldList" in the "textQueryPanel" for this
        // table.
        QueryTextViewInfo info = queryComponent.queryViewStructure.getTextViewInfo(table);
        info.myListModel.removeElement(e.getColumnName());
        if (index == queryComponent.queryViewStructure.activeTableIndex) {
            qTable.validate();
            qTable.paintImmediately(qTable.getVisibleRect());

            queryComponent.textView.loadNewInfo(info);

//        ((DefaultListModel) ((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).getModel()).removeElement(e.getFieldName());
  //      ((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).validate();
    //    ((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).paintImmediately(((JList) queryComponent.fieldListArray.at(queryComponent.activeQTableIndex)).getVisibleRect());

            if (qTable.getColumnCount() > 0) {
                queryComponent.headerStatusButton.setEnabled(true);
                queryComponent.addRow.setEnabled(true);
            } else {
                queryComponent.headerStatusButton.setEnabled(false);
                queryComponent.addRow.setEnabled(false);
            }
        }
    }

    public void selectedRecordSetChanged(SelectedRecordSetChangedEvent parm1) {
    }

    public void rowOrderChanged(RowOrderChangedEvent parm1) {
    }

    public void tableRenamed(TableRenamedEvent e) {
        Table table = (Table) e.getSource();
        int index = queryComponent.queryViewStructure.getTableIndex(table); // db.indexOf(table);
        if (index == -1) {
            return;
        }

        String newTitle = e.getNewTitle();
        Plug resultExportPlug = queryComponent.resultTablePlugs.getPlug(table);
        if (newTitle == null || newTitle.equals("")) {
            queryComponent.tabs.setTitleAt(index, queryComponent.infoBundle.getString("QueryComponentMsg16"));
            try {
                newTitle = queryComponent.infoBundle.getString("QueryComponentMsg16");
                resultExportPlug.setName(queryComponent.infoBundle.getString("ResultTable") + queryComponent.infoBundle.getString("QueryComponentMsg16") + "\"");
            } catch (PlugExistsException exc) {}
        } else {
            try {
                resultExportPlug.setName(queryComponent.infoBundle.getString("ResultTable") + newTitle + "\"");
                queryComponent.tabs.setTitleAt(index, e.getNewTitle());
            } catch (PlugExistsException exc) {}
        }

        // Rename the table which contains the output results, if one is contain in
        // the same db, as the renamed table.
        Table resultTable = queryComponent.db.getTable(queryComponent.infoBundle.getString("ResultTable") + e.getOldTitle() + "\"");
        if (resultTable != null) {
            try {
                resultTable.setTitle(queryComponent.infoBundle.getString("ResultTable") + newTitle + "\"");
            } catch (InvalidTitleException exc) {
                System.out.println("Serious inconsistency error in QueryComponent tableRenamed(): (2)");
            } catch (PropertyVetoException exc) {
                System.out.println("Serious inconsistency error in QueryComponent tableRenamed(): (2)");
            }
        }
        queryComponent.tabs.validate();
        queryComponent.tabs.doLayout();
        queryComponent.tabs.paintImmediately(queryComponent.tabs.getVisibleRect());
    }

    public void currencyFieldChanged(ColumnEvent event) {        
    }
}