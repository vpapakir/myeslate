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

public class PageUpAction extends AbstractAction {
    DBTable dbTable;

    public PageUpAction(DBTable table, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0, false));
        KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0);
        dbTable = table;
    }

    public void actionPerformed(ActionEvent e) {
        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        if (dbTable.isEditing()) {
            dbTable.stopCellEditing();
//            return;
        }

        if (dbTable.scrollpane.getViewport().getViewPosition().y == 0)
            return;

        // Find the row at the top of the screen, which is visible.
        JViewport viewport = dbTable.scrollpane.getViewport();
        Point viewportPos = viewport.getViewPosition();
        int startY = viewportPos.y;
        int firstVisibleRow = dbTable.jTable.rowAtPoint(new Point(0, startY));
        // Scroll the viewport, so that firstVisibleRow is at the bottom of the screen.
        int lastVisibleRow = firstVisibleRow;
        int endY = dbTable.jTable.getCellRect(lastVisibleRow, 0, true).y;
        // We want the first visible row to start exactly at the new view position.y, so we make the following correction.
        int mod = viewport.getExtentSize().height % dbTable.getRowHeight();
        viewport.setViewPosition(new Point(viewportPos.x, endY+mod-viewport.getExtentSize().height));
    }
}
