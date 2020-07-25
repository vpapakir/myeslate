package gr.cti.eslate.event;

import java.util.EventListener;


/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.database.engine.event.DatabaseAdapter.
 */
public class DatabaseAdapter implements DatabaseListener {
    public void activeTableChanged(ActiveTableChangedEvent e) {}
    public void tableAdded(TableAddedEvent e)  {}
    public void tableRemoved(TableRemovedEvent e) {}
    public void tableRenamed(TableRenamedEvent e) {}
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
    public void databaseRenamed(DatabaseRenamedEvent e) {}
    public void selectedRecordSetChanged(SelectedRecordSetChangedEvent e) {}
    public void tableReplaced(TableReplacedEvent e) {}
    public void cellValueChanged(CellValueChangedEvent e) {}
    public void recordAdded(RecordAddedEvent e) {}
    public void emptyRecordAdded(RecordAddedEvent e) {}
    public void recordRemoved(RecordRemovedEvent e) {}
    public void activeRecordChanged(ActiveRecordChangedEvent e) {}
    public void tableHiddenStateChanged(TableHiddenStateChangedEvent e) {}
/*    public void activeTableChanged(ActiveTableChangedEvent e) {}
    public void tableAdded(TableAddedEvent e)  {}
    public void tableRemoved(TableRemovedEvent e) {}
    public void databaseRenamed(DatabaseRenamedEvent e) {}
    public void tableReplaced(TableReplacedEvent e) {}
*/
}
