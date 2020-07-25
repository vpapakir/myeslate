package gr.cti.eslate.sharedObject;

import java.util.*;

import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.base.*;
import gr.cti.eslate.database.engine.DBase;
//import gr.cti.eslate.database.Database;

/**
 * Database shared object.
 *
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class DatabaseSO extends SharedObject {
    private DBase database;
    private Object dbComponent = null;
    private boolean databaseEditable = true;

    public DatabaseSO(ESlateHandle app) {
        super(app);
    }

    @SuppressWarnings(value={"deprecation"})
    public void setDatabase(DBase database, Vector path) {
        if (areDifferent(this.database, database)) {
            this.database = database;
            // Create an event
            SharedObjectEvent soe = new SharedObjectEvent(this, path);

            fireSharedObjectChanged(soe);       // Notify the listeners
        }
    }

    public void setDatabase(DBase database) {
        if (areDifferent(this.database, database)) {
            this.database = database;
            // Create an event
            SharedObjectEvent soe = new SharedObjectEvent(this);

            fireSharedObjectChanged(soe);       // Notify the listeners
        }
    }

    public void setDBComponent(Object dbComponent) {
        this.dbComponent = dbComponent;
    }

    public DBase getDatabase() {
        return database;
    }

    public Object getDBComponent() {
        return dbComponent;
    }

    public boolean isDatabaseEditable() {
        return databaseEditable;
    }

    public void setDatabaseEditable(boolean editable) {
        databaseEditable = editable;
    }

    public void nullify() {
        database = null;
        dbComponent = null;
    }

}
