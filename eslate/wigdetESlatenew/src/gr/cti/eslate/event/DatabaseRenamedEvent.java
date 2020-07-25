package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.database.engine.event.DatabaseRenamedEvent.
 */
public class DatabaseRenamedEvent extends EventObject {
    protected String oldTitle;
    protected String newTitle;

    public DatabaseRenamedEvent(Object source, String oldTitle, String newTitle) {
        super(source);
        this.oldTitle = oldTitle;
        this.newTitle = newTitle;
    }

    public String getOldTitle() {
        return oldTitle;
    }

    public String getNewTitle() {
        return newTitle;
    }

}
