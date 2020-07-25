package gr.cti.eslate.database.engine;

import java.util.ListResourceBundle;


public class CTableBeanInfoBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"ActiveRecord",              "Active record"},
        {"ActiveRecordTip",           "The active record of the table"},
        {"DataChangeAllowed",         "Data change allowed"},
        {"DataChangeAllowedTip",      "Defines if the data of the table can be edited"},
        {"TableRenamingAllowed",      "Table rename allowed"},
        {"TableRenamingAllowedTip",   "Defines if the title of the table can be edited"},
        {"FieldAdditionAllowed",      "Field addition allowed"},
        {"FieldAdditionAllowedTip",   "Defines if new fields can be added to the table"},
        {"FieldRemovalAllowed",       "Field removal allowed"},
        {"FieldRemovalAllowedTip",    "Defines if fields can be removed from the table"},
        {"FieldReorderingAllowed",    "Field re-ordering allowed"},
        {"FieldReorderingAllowedTip", "Defines if the table's fields can be re-ordered"},
        {"FieldPropertyEditingAllowed",   "Field property editing allowed"},
        {"FieldPropertyEditingAllowedTip","Defines if the properties of the table's fields can be edited"},
        {"KeyChangedAllowed",         "Key change allowed"},
        {"KeyChangedAllowedTip",      "Defines if the table's key is editable"},
        {"Metadata",                  "Table metadata"},
        {"MetadataTip",               "Table description and comments"},
        {"RecordAdditionAllowed",     "Record addition allowed"},
        {"RecordAdditionAllowedTip",  "Defines if new records can be added to the table"},
        {"RecordRemovalAllowed",      "Record removal allowed"},
        {"RecordRemovalAllowedTip",   "Defines if records can be removed from the table"},
        {"Title",                     "Table title"},
        {"TitleTip",                  "The title of the table"},
        {"SynchronizedActiveRecord",  "Synchronize active record"},
        {"SynchronizedActiveRecordTip", "Synchronizes the active record of the TableView with the active record of the underlying Table"},
        {"CellValueChanged",          "Cell valued changed"},
        {"ColumnTypeChanged",         "Field data type changed"},
        {"ColumnRenamed",             "Field renamed"},
        {"ColumnAdded",               "Field added"},
        {"ColumnRemoved",             "Field removed"},
        {"ColumnRemoved",             "Field removed"},
        {"SelectedRecordSetChanged",  "Set of selected records changed"},
        {"RowOrderChanged",           "Row order changed"},
        {"TableRenamed",              "Table renamed"},
        {"ColumnKeyChanged",          "Table key changed"},
        {"CalcColumnReset",           "Calculated field reset"},
        {"ColumnEditableStateChanged","Field editable state changed"},
        {"ColumnRemovableStateChanged","Field removable state changed"},
        {"ColumnHiddenStateChanged",  "Field visibility changed"},
        {"CalcColumnFormulaChanged",  "Calculated field formula changed"},
        {"RecordAdded",               "Record added"},
        {"EmptyRecordAdded",          "Empty record added"},
        {"RecordRemoved",             "Record removed"},
        {"ActiveRecordChanged",       "Active record changed"},
        {"TableHiddenStateChanged",   "Table visibility changed"},
//    public void columnKeyChanged(ColumnKeyChangedEvent e) {}
//    public void calcColumnReset(CalcColumnResetEvent e) {}
//    public void columnEditableStateChanged(ColumnEditableStateChangedEvent e) {}
//    public void columnRemovableStateChanged(ColumnRemovableStateChangedEvent e) {}
//    public void columnHiddenStateChanged(ColumnHiddenStateChangedEvent e) {}
//    public void calcColumnFormulaChanged(CalcColumnFormulaChangedEvent e) {}
//    public void recordAdded(RecordAddedEvent e) {}
//    public void emptyRecordAdded(RecordAddedEvent e) {}
//    public void recordRemoved(RecordRemovedEvent e) {}
//    public void activeRecordChanged(ActiveRecordChangedEvent e) {}
//    public void tableHiddenStateChanged(TableHiddenStateChangedEvent e) {}
    };
}