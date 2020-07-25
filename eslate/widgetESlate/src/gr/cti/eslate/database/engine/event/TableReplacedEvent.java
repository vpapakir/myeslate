package gr.cti.eslate.database.engine.event;

import java.util.EventObject;
import gr.cti.eslate.database.engine.Table;

public class TableReplacedEvent extends EventObject {
    private Table oldTable;
    private Table newTable;

    public TableReplacedEvent(Object source, Table newTable, Table oldTable) {
        super(source);
        this.newTable = newTable;
        this.oldTable = oldTable;
    }

    public Table getNewTable() {
        return newTable;
    }

    public Table getReplacedTable() {
        return oldTable;
    }

}
