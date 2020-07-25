package gr.cti.eslate.tableModel.event;


public class TableModelAdapter implements TableModelListener {

    public void cellValueChanged(CellValueChangedEvent e) {}
    public void columnTypeChanged(ColumnTypeChangedEvent e) {}
    public void columnRenamed(ColumnRenamedEvent e) {}
    public void columnAdded(ColumnAddedEvent e) {}
    public void columnReplaced(ColumnReplacedEvent e) {}
    public void columnRemoved(ColumnRemovedEvent e) {}
    public void selectedRecordSetChanged(SelectedRecordSetChangedEvent e) {}
    public void rowOrderChanged(RowOrderChangedEvent e) {}
    public void tableRenamed(TableRenamedEvent e) {}
    public void recordRemoved(RecordRemovedEvent e) {}
    public void activeRecordChanged(ActiveRecordChangedEvent e) {}
}
