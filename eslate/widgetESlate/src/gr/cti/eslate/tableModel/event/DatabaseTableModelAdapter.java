package gr.cti.eslate.tableModel.event;


public class DatabaseTableModelAdapter implements DatabaseTableModelListener {
    public void columnAdded(ColumnAddedEvent e) {}
    public void columnRemoved(ColumnRemovedEvent e) {}
    public void columnRenamed(ColumnRenamedEvent e) {}
    public void columnTypeChanged(ColumnTypeChangedEvent e) {}
    public void columnKeyChanged(ColumnKeyChangedEvent e) {}
    public void calcColumnReset(CalcColumnResetEvent e) {}
    public void columnEditableStateChanged(ColumnEditableStateChangedEvent e) {}
    public void columnRemovableStateChanged(ColumnRemovableStateChangedEvent e) {}
    public void columnHiddenStateChanged(ColumnHiddenStateChangedEvent e) {}
    public void calcColumnFormulaChanged(CalcColumnFormulaChangedEvent e) {}
    public void selectedRecordSetChanged(SelectedRecordSetChangedEvent e) {}
    public void cellValueChanged(CellValueChangedEvent e) {}
    public void recordAdded(RecordAddedEvent e) {}
    public void recordRemoved(RecordRemovedEvent e) {}
    public void activeRecordChanged(ActiveRecordChangedEvent e) {}
    public void rowOrderChanged(RowOrderChangedEvent e) {}
    public void tableRenamed(TableRenamedEvent e) {}
    public void tableHiddenStateChanged(TableHiddenStateChangedEvent e) {}
    public void columnReplaced(ColumnReplacedEvent e) {}
    public void currencyFieldChanged(ColumnEvent e) {}

}