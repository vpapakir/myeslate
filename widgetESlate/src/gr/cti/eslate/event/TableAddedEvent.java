package gr.cti.eslate.event;

import java.util.EventObject;
import gr.cti.eslate.database.engine.Table;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.database.engine.event.TableAddedEvent.
 */
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
