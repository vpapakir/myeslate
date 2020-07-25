/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Mar 26, 2003
 * Time: 5:24:11 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TableSortAction extends AbstractAction {
    DBTable dbTable;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

    public TableSortAction(DBTable dbTable, String name){
        super(name);
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
        Frame frame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, dbTable);
        TableSortDialog tid = new TableSortDialog(frame, dbTable);
        dbTable.setCursor(waitCursor);
        try{
            dbTable.sortOnMultipleFields(tid.getFieldsToSortOn(), tid.getSortDirections());
        }catch (Throwable thr) {
            thr.printStackTrace();
        }
        dbTable.setCursor(defaultCursor);
    }
}
