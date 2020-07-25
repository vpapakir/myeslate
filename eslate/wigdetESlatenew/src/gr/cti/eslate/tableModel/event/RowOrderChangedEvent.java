package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class RowOrderChangedEvent extends EventObject {
//t    private int tableIndex = -1;

//t    public RowOrderChangedEvent(Object source, int tableIndex) {
    public RowOrderChangedEvent(Object source) {
        super(source);
//t        this.tableIndex = tableIndex;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
}
