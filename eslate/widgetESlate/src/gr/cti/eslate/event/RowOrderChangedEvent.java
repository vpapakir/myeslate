package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.RowOrderChangedEvent.
 */
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
