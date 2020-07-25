package gr.cti.eslate.event;

import java.util.EventListener;


public interface DatabaseTableListener extends EventListener {
    public abstract void fieldRenamed(FieldRenamedEvent e);
    public abstract void fieldTypeChanged(FieldTypeChangedEvent e);
    public abstract void fieldKeyChanged(FieldKeyChangedEvent e);
    public abstract void fieldRemoved(FieldRemovedEvent e);
    public abstract void calcFieldReset(CalcFieldResetEvent e);
    public abstract void calcFieldFormulaChanged(CalcFieldFormulaChangedEvent e);
    public abstract void fieldEditableStateChanged(FieldEditableStateChangedEvent e);
    public abstract void fieldRemovableStateChanged(FieldRemovableStateChangedEvent e);
    public abstract void fieldHiddenStateChanged(FieldHiddenStateChangedEvent e);
    public abstract void fieldAdded(FieldAddedEvent e);
    public abstract void selectedRecordSetChanged(SelectedRecordSetChangedEvent e);
    public abstract void cellValueChanged(CellValueChangedEvent e);
    public abstract void recordAdded(RecordAddedEvent e);
    public abstract void emptyRecordAdded(RecordAddedEvent e);
    public abstract void recordRemoved(RecordRemovedEvent e);
    public abstract void activeRecordChanged(ActiveRecordChangedEvent e);
    public abstract void rowOrderChanged(RowOrderChangedEvent e);
    public abstract void tableRenamed(TableRenamedEvent e);
    public abstract void tableHiddenStateChanged(TableHiddenStateChangedEvent e);
}