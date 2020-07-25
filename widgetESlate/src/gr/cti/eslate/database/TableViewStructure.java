package gr.cti.eslate.database;

import gr.cti.eslate.database.view.TableView;
import gr.cti.eslate.database.view.FieldView;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.utils.ESlateFieldMap;
import java.util.HashMap;
import com.objectspace.jgl.Array;
import java.io.Externalizable;
import java.io.IOException;


public class TableViewStructure implements Externalizable {
    public static final String STR_FORMAT_VERSION = "1.0";
    static final long serialVersionUID = 12L;
//    Table table;
//1    TableView tableView;
//1    HashMap fieldViews = new HashMap();

    public TableViewStructure() {
    }

    public TableViewStructure(Table table) {
//        this.table = table;
/*1        if (table.getTableView() == null)
            tableView = new TableView(table.getFieldCount());
        else
            tableView = new TableView(table.getTableView(), table.getFieldCount()); // Create a clone of the table's view

        int fieldCount = table.getFieldCount();
        Array fields = table.getFields();
        for (int i=0; i<fieldCount; i++) {
            FieldView existingFieldView = ((TableField) fields.at(i)).getFieldView();
            fieldViews.put(((TableField) fields.at(i)).getName(), new FieldView(existingFieldView));
        }
1*/
    }

/*1    public void fieldRenamed(String oldName, String newName) {
        FieldView view = (FieldView) fieldViews.remove(oldName);
        fieldViews.put(newName, view);

    }

    public void fieldRemoved(String name) {
//1        fieldViews.remove(name);
    }

    public FieldView fieldAdded(String name) {
        FieldView fView = new FieldView();
        fieldViews.put(name, fView);
        return fView;

    }

    public FieldView getFieldView(String name) {
        return (FieldView) fieldViews.get(name);
    }
1*/
    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        ESlateFieldMap fieldMap = new ESlateFieldMap(STR_FORMAT_VERSION, 7);
//1        fieldMap.put("Table view", tableView);
//1        fieldMap.put("Field views", fieldViews);
        out.writeObject(fieldMap);
    }

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        ESlateFieldMap fieldMap = (ESlateFieldMap) in.readObject();
//1        tableView = (TableView) fieldMap.get("Table view");
//1        fieldViews = (HashMap) fieldMap.get("Field views");
    }
}