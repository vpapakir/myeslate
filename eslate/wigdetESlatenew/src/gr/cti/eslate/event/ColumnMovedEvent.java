package gr.cti.eslate.event;

import java.util.EventObject;

public class ColumnMovedEvent extends EventObject {
    protected String fromColumn;
    protected String toColumn;
    protected int tableIndex;

    public ColumnMovedEvent(Object source, int tableIndex, String fromColumn, String toColumn) {
        super(source);
        this.fromColumn = fromColumn;
        this.toColumn = toColumn;
        this.tableIndex = tableIndex;
    }

    public String getToColumn() {
        return toColumn;
    }

    public int getTableIndex() {
        return tableIndex;
    }

    public String getFromColumn() {
        return fromColumn;
    }

}
