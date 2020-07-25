package gr.cti.eslate.database;

/**
 * Title:        Database
 * Description:  Your description
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 * @version
 */

import java.util.ArrayList;
import gr.cti.eslate.database.engine.Table;
import gr.cti.typeArray.IntBaseArray;


public class RemoteTableModelListenerStructure {
    ArrayList localTables = new ArrayList();
    ArrayList remoteTables = new ArrayList();
    ArrayList listeners = new ArrayList();

    public RemoteTableModelListenerStructure() {
    }

    public void addListener(Table localTable, Table remoteTable, RemoteDatabaseTableModelListener rtml) {
        System.out.println("addListener() localTable: " + localTable + ", remoteTable: " + remoteTable + ", rtml: " + rtml);
        int index = getListenerIndex(localTable, remoteTable);
        if (index == -1) {
            localTables.add(localTable);
            remoteTables.add(remoteTable);
            listeners.add(rtml);
        }else{
            listeners.set(index, rtml);
        }
    }

    public void removeListeners(Table localTable) {
        for (int i=0; i<localTables.size(); i++) {
            if (localTables.get(i) == localTable) {
                localTables.remove(i);
                remoteTables.remove(i);
                RemoteDatabaseTableModelListener rtml = (RemoteDatabaseTableModelListener) listeners.get(i);
                rtml.dBase = null;
                rtml.remoteTable = null;
                i--;
            }
        }
    }

    public void removeListener(Table remoteTable) {
        for (int i=0; i<remoteTables.size(); i++) {
            if (remoteTables.get(i) == remoteTable) {
                localTables.remove(i);
                remoteTables.remove(i);
                RemoteDatabaseTableModelListener rtml = (RemoteDatabaseTableModelListener) listeners.get(i);
                rtml.dBase = null;
                rtml.remoteTable = null;
                break;
            }
        }
    }

    public RemoteDatabaseTableModelListener[] getListenersForRemoteTable(Table remoteTable) {
        IntBaseArray positions = new IntBaseArray();
        for (int i=0; i<remoteTables.size(); i++) {
            if (remoteTables.get(i) == remoteTable)
                positions.add(i);
        }
        if (positions.size() == 0) return null;
        RemoteDatabaseTableModelListener[] l = new RemoteDatabaseTableModelListener[positions.size()];
        for (int i=0; i<l.length; i++)
            l[i] = (RemoteDatabaseTableModelListener) listeners.get(positions.get(i));
        return l;
    }

    public RemoteDatabaseTableModelListener getListenerForLocalTable(Table localTable) {
        IntBaseArray positions = new IntBaseArray();
        System.out.println("getListenerForLocalTable() localTables.size(): " + localTables.size() + ", remoteTables.size(): " + remoteTables.size() + ", listeners.size(): " + listeners.size());
        for (int i=0; i<localTables.size(); i++) {
            System.out.println("getListenerForLocalTable() localTables.get(i): " + localTables.get(i));
            if (localTables.get(i) == localTable)
                return (RemoteDatabaseTableModelListener) listeners.get(i);
        }
        return null;
    }

    public RemoteDatabaseTableModelListener getListener(Table localTable, Table remoteTable) {
        for (int i=0; i<localTables.size(); i++) {
            if (localTables.get(i) == localTable && remoteTables.get(i) == remoteTable)
                return (RemoteDatabaseTableModelListener) listeners.get(i);
        }
        return null;
    }

    public int getListenerIndex(Table localTable, Table remoteTable) {
        for (int i=0; i<localTables.size(); i++) {
            if (localTables.get(i) == localTable && remoteTables.get(i) == remoteTable)
                return i;
        }
        return -1;
    }

    public Table getLocalTableForRemoteTable(Table remoteTable, Object remoteTableOwner) {
        for (int i=0; i<remoteTables.size(); i++) {
            if (remoteTables.get(i) == remoteTable && ((RemoteDatabaseTableModelListener) listeners.get(i)).remoteTableOwner == remoteTableOwner)
                return (Table) localTables.get(i);
        }
        return null;
    }

    public boolean isImportedTable(Table table) {
        boolean imported = false;
        for (int i=0; i<localTables.size(); i++) {
            if (localTables.get(i) == table) {
                imported = true;
                break;
            }
        }
        return imported;
    }

    public void clear() {
        localTables.clear();
        remoteTables.clear();
        listeners.clear();
    }
}