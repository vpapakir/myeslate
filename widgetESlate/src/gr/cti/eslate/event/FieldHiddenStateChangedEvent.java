package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.ColumnHiddenStateChangedEvent.
 */
public class FieldHiddenStateChangedEvent extends EventObject {
//t    protected int tableIndex;
    protected String fieldName;
    protected boolean isHidden;

//t    public FieldHiddenStateChangedEvent(Object source, int tableIndex, String fieldName, boolean isHidden) {
    public FieldHiddenStateChangedEvent(Object source, String fieldName, boolean isHidden) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.fieldName = fieldName;
        this.isHidden = isHidden;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getFieldName() {
        return fieldName;
    }

    public boolean isHidden() {
        return isHidden;
    }

}
