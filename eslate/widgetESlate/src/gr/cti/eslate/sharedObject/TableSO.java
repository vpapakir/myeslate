package gr.cti.eslate.sharedObject;

import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.base.ESlateHandle;

/**
 * Table shared object.
 *
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class TableSO extends SharedObject {
    private Table table;

    public TableSO(ESlateHandle app) {
        super(app);
    }

    public void setTable(Table table) {
        if (areDifferent(this.table, table)) {
            this.table = table;
            // Create an event
            SharedObjectEvent soe = new SharedObjectEvent(this);

            fireSharedObjectChanged(soe);       // Notify the listeners
        }
    }

    public Table getTable() {
        return table;
    }

    public void nullify() {
        table = null;
    }
}
