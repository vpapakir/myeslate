package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.ColumnTypeChangedEvent.
 */
public class FieldTypeChangedEvent extends EventObject {
//t    private int tableIndex;
    private String fieldName;
    private Class oldType;
    private Class newType;
    private boolean isDate;
    private boolean wasDate;

//t    public FieldTypeChangedEvent(Object source, int tableIndex, String fieldName, Class oldType, boolean wasDate, Class newType, boolean isDate) {
    public FieldTypeChangedEvent(Object source, String fieldName, Class oldType, boolean wasDate, Class newType, boolean isDate) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.fieldName = fieldName;
        this.oldType = oldType;
        this.wasDate = wasDate;
        this.newType = newType;
        this.isDate = isDate;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getFieldName() {
        return fieldName;
    }

    public Class getOldType() {
        return oldType;
    }

    public Class getNewType() {
        return newType;
    }

    public boolean isDate() {
        return isDate;
    }

    public boolean wasDate() {
        return wasDate;
    }

}
