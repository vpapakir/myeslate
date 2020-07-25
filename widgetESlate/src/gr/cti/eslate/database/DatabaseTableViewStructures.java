package gr.cti.eslate.database;

import java.util.HashMap;
import java.io.Externalizable;
import java.io.IOException;
import gr.cti.eslate.utils.ESlateFieldMap;


/* This class is used to persist the TableViewStructures of the dbTables in the
 * Database component. It is created during externalization and is used only during
 * the save and the restore process of the Database component. It shouldn't be used
 * in any other place or way.
 */
public class DatabaseTableViewStructures implements Externalizable {
    public static final String STR_FORMAT_VERSION = "1.0";
    static final long serialVersionUID = 12L;
//1    HashMap structures = new HashMap();

    public DatabaseTableViewStructures() {
    }

    public DatabaseTableViewStructures(Database database) {
//        if (database.dbTables == null) return;
//1        for (int i=0; i<database.dbTables.size(); i++) {
//1            DBTable dbTable = (DBTable) database.dbTables.at(i);
//1            structures.put(dbTable.table.getTitle(), dbTable.viewStructure);
//1        }
    }

/*1    public TableViewStructure getTableViewStructure(String title) {
        return (TableViewStructure) structures.get(title);
    }
1*/
    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        ESlateFieldMap fieldMap = new ESlateFieldMap(STR_FORMAT_VERSION, 1);
//1        fieldMap.put("Database TableViewStructures", structures);
        out.writeObject(fieldMap);
    }

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        ESlateFieldMap fieldMap = (ESlateFieldMap) in.readObject();
//1        structures = (HashMap) fieldMap.get("Database TableViewStructures");
    }

}