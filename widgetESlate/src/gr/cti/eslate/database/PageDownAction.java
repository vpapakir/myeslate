package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.table.TableColumn;

import gr.cti.eslate.database.engine.*;
import java.awt.Cursor;
import javax.swing.*;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import java.awt.Color;
import java.awt.Point;

public class PageDownAction extends AbstractAction {
    DBTable dbTable;

    public PageDownAction(DBTable table, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0, false));
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        dbTable.stopCellEditing();

        if (dbTable.scrollpane.getViewport().getViewPosition().y == dbTable.scrollpane.getViewport().getViewSize().height-dbTable.scrollpane.getViewport().getExtentSize().height) {
            return;
        }

        // Find the row at the end of the screen, which is visible.
        JViewport viewport = dbTable.scrollpane.getViewport();
        Point viewportPos = viewport.getViewPosition();
        int endY = viewportPos.y + viewport.getExtentSize().height;
        int lastVisibleRow = dbTable.jTable.rowAtPoint(new Point(0, endY));
        // Scroll the viewport, so that the lastVisibleRow is at the top of the screen.
        int firstVisibleRow = lastVisibleRow;
        int startY = dbTable.jTable.getCellRect(firstVisibleRow, 0, true).y;
        viewport.setViewPosition(new Point(viewportPos.x, startY));
    }
}
