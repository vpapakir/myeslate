package gr.cti.eslate.database;

import javax.swing.JFrame;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.database.view.FieldView;
import gr.cti.eslate.database.view.TableView;


public class TableSaveUISettingsAction extends AbstractAction {
    Database dBase;
    String actionName;

    public TableSaveUISettingsAction(Database db, String name){
        dBase = db;
        actionName = name;
    }


    public void actionPerformed(ActionEvent e) {
/*1        DBTable dbTable = dBase.activeDBTable;
        Table ctable = dbTable.table;
        int fieldCount = ctable.getFieldCount();
        TableViewStructure structure = dbTable.viewStructure;
        ctable.setTableView(new TableView(structure.tableView, fieldCount));
        for (int i=0; i<fieldCount; i++) {
            try{
                AbstractTableField f = ctable.getTableField(i);
                f.setFieldView(new FieldView(structure.getFieldView(f.getName())));
            }catch (InvalidFieldIndexException exc) {
                System.out.println("Inconsistency error in TableSaveUISettingsAction actionPerformed()");
            }
        }
1*/
    }

/*1    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miTableSaveUISettings.setEnabled(b);
    }
1*/
}