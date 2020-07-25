package gr.cti.eslate.database.engine.event;

import java.util.EventObject;

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
