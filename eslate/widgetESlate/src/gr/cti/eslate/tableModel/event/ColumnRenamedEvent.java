package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class ColumnRenamedEvent extends EventObject {
//t    protected int tableIndex;
    private String oldName;
    private String newName;

//t    public FieldRenamedEvent(Object source, int tableIndex, String oldName, String newName) {
    public ColumnRenamedEvent(Object source, String oldName, String newName) {
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
