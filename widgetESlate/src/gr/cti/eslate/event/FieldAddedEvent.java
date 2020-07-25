package gr.cti.eslate.event;

import java.util.EventObject;
import gr.cti.eslate.database.engine.TableField;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.ColumnAddedEvent.
 */
public class FieldAddedEvent extends EventObject {
//t    protected int tableIndex;
    protected TableField field;

//t    public FieldAddedEvent(Object source, int tableIndex, CTableField field) {
    public FieldAddedEvent(Object source, TableField field) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.field = field;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public TableField getField() {
        return field;
    }
}
