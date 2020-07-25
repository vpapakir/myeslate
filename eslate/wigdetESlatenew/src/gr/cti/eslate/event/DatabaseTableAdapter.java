package gr.cti.eslate.event;


public class DatabaseTableAdapter implements DatabaseTableListener {
    public void fieldAdded(FieldAddedEvent e) {}
    public void fieldRemoved(FieldRemovedEvent e) {}
    public void fieldRenamed(FieldRenamedEvent e) {}
    public void fieldTypeChanged(FieldTypeChangedEvent e) {}
    public void fieldKeyChanged(FieldKeyChangedEvent e) {}
    public void calcFieldReset(CalcFieldResetEvent e) {}
    public void fieldEditableStateChanged(FieldEditableStateChangedEvent e) {}
    public void fieldRemovableStateChanged(FieldRemovableStateChangedEvent e) {}
    public void fieldHiddenStateChanged(FieldHiddenStateChangedEvent e) {}
    public void calcFieldFormulaChanged(CalcFieldFormulaChangedEvent e) {}
    public void selectedRecordSetChanged(SelectedRecordSetChangedEvent e) {}
    public void cellValueChanged(CellValueChangedEvent e) {}
    public void recordAdded(RecordAddedEvent e) {}
    public void emptyRecordAdded(RecordAddedEvent e) {}
    public void recordRemoved(RecordRemovedEvent e) {}
    public void activeRecordChanged(ActiveRecordChangedEvent e) {}
    public void rowOrderChanged(RowOrderChangedEvent e) {}
    public void tableRenamed(TableRenamedEvent e) {}
    public void tableHiddenStateChanged(TableHiddenStateChangedEvent e) {}
}