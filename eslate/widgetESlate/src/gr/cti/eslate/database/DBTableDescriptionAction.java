/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 7 Íןו 2002
 * Time: 6:58:27 לל
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.table.TableColumn;
import javax.swing.*;

public class DBTableDescriptionAction extends AbstractAction {
    DBTable dbTable;

    public DBTableDescriptionAction(DBTable dbTable, String name){
        super(name);
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    void execute() {
        Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, dbTable);
        MetadataDialog metadataDialog = new MetadataDialog(topLevelFrame, null, dbTable, 1, true);
    }
}
