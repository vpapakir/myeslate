package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.ColumnRenamedEvent.
 */
public class FieldRenamedEvent extends EventObject {
//t    protected int tableIndex;
    protected String oldName;
    protected String newName;

//t    public FieldRenamedEvent(Object source, int tableIndex, String oldName, String newName) {
    public FieldRenamedEvent(Object source, String oldName, String newName) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.oldName = oldName;
        this.newName = newName;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }

}
