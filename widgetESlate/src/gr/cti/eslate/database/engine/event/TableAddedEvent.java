package gr.cti.eslate.database.engine.event;

import java.util.EventObject;
import gr.cti.eslate.database.engine.Table;

public class TableAddedEvent extends EventObject {
    private Table table;

    public TableAddedEvent(Object source, Table table) {
        super(source);
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

}
