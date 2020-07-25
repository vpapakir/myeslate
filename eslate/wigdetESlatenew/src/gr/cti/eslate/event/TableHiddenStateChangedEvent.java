package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.TableHiddenStateChangedEvent.
 */
public class TableHiddenStateChangedEvent extends EventObject {
//t    private int tableIndex;
    private int visibleIndex;
    private boolean isHidden;

//t    public TableHiddenStateChangedEvent(Object source, int tableIndex, int visibleIndex, boolean isHidden) {
    public TableHiddenStateChangedEvent(Object source, boolean isHidden) {
        super(source);
//t        this.tableIndex = tableIndex;
//t        this.visibleIndex = visibleIndex;
        this.isHidden = isHidden;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }

    public int getTableVisibleIndex() {
        return visibleIndex;
    }
t*/
    public boolean isHidden() {
        return isHidden;
    }

}
