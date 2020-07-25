package gr.cti.eslate.database.engine.event;

import java.util.EventListener;


public class DatabaseAdapter implements DatabaseListener {
    public void activeTableChanged(ActiveTableChangedEvent e) {}
    public void tableAdded(TableAddedEvent e)  {}
    public void tableRemoved(TableRemovedEvent e) {}
    public void databaseRenamed(DatabaseRenamedEvent e) {}
    public void tableReplaced(TableReplacedEvent e) {}
}
