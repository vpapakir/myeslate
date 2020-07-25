/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 7 Íןו 2002
 * Time: 7:10:40 לל
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;

public class DBTableInfoAction extends AbstractAction {
    DBTable dbTable;

    public DBTableInfoAction(DBTable dbTable, String name){
        super(name);
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    void execute() {
        Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, dbTable);
        TableInfoDialog tid = new TableInfoDialog(topLevelFrame, dbTable, null);
    }

    void execute(Database database) {
        Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, dbTable);
        TableInfoDialog tid = new TableInfoDialog(topLevelFrame, null, database);
    }
}
