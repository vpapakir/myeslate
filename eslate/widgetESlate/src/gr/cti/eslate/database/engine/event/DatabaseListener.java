package gr.cti.eslate.database.engine.event;

import java.util.EventListener;


public interface DatabaseListener extends EventListener {
    public abstract void activeTableChanged(ActiveTableChangedEvent e);
    public abstract void tableAdded(TableAddedEvent e);
    public abstract void tableRemoved(TableRemovedEvent e);
    public abstract void databaseRenamed(DatabaseRenamedEvent e);
    public abstract void tableReplaced(TableReplacedEvent e);
}
