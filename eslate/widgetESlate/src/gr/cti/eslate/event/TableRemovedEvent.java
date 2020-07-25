package gr.cti.eslate.event;

import java.util.EventObject;
import gr.cti.eslate.database.engine.Table;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.database.engine.event.TableRemovedEvent.
 */
public class TableRemovedEvent extends EventObject {
    protected int tableIndex;
    private Table table;

    public TableRemovedEvent(Object source, Table table, int tableIndex) {
        super(source);
        this.table = table;
        this.tableIndex = tableIndex;
    }

    public int getTableIndex() {
        return tableIndex;
    }

    public Table getRemovedTable() {
        return table;
    }
}
