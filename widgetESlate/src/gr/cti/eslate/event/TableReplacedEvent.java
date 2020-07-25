package gr.cti.eslate.event;

import java.util.EventObject;
import gr.cti.eslate.database.engine.Table;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.database.engine.event.TableReplacedEvent.
 */
public class TableReplacedEvent extends EventObject {
    private Table oldCTable;
    private Table newCTable;

    public TableReplacedEvent(Object source, Table newCTable, Table oldCTable) {
        super(source);
        this.newCTable = newCTable;
        this.oldCTable = oldCTable;
    }

    public Table getNewCTable() {
        return newCTable;
    }

    public Table getReplacedCTable() {
        return oldCTable;
    }

}
