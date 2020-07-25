package gr.cti.eslate.database;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.dnd.*;

public class VerticalRowBar extends JPanel {
    protected DBTable dbTable;
    protected JTable table;
    protected JScrollPane scrollpane;
    protected boolean resizeEnabled = false, isDragging = false;
    protected int firstDragPosition = -1, maxDragPosition = -1;
    protected int upperCorner, lowerCorner;
    protected static Image activeRecordImg;
    static Image selectedRecImg = null;
    private boolean drawActiveRowOnly = false;
    /** This variable adjusts whether the record selection status is presented by the row bar.*/
    boolean displayRecordSelection = true;
    /** This variable adjusts whether the record activation status is presented by the row bar.*/
    boolean displayActiveRecord = true;
    int preferredWidth = 23;
    /** This flag is set while the left mouse button is pressed and before any dragging occurs. */
    boolean mousePressedNoDragOccured = false;
    DropTargetListener dropTargetListener = null;
    DropTarget dropTarget = null;

    protected VerticalRowBar(DBTable dbTable, JScrollPane scrollpane, JTable table) {
        super(true);
        this.table = table;
        this.scrollpane = scrollpane;
        this.dbTable = dbTable;
        setRequestFocusEnabled(false);

//1        getBackground = scrollpane.getBackground();
//1        getBackground = new Color(getBackground.getRed()-10, getBackground.getGreen()-10, getBackground.getBlue()-10);
//1        verticalRowBarDarkColor = getBackground.darker();
//        verticalRowBarBrightColor = getBackground;
        //int rowHeight = jTable.getRowHeight();

        //int numOfRows = jTable.getRowCount();
        if (activeRecordImg == null)
            activeRecordImg = new ImageIcon(getClass().getResource("images/arrows/activeRec.gif")).getImage();
        if (selectedRecImg == null)
            selectedRecImg = new ImageIcon(VerticalRowBar.class.getResource("images/check.gif")).getImage();
//        int panelHeight = rowHeight + ((numOfRows-1) * (rowHeight+1));
        Dimension d = new Dimension(preferredWidth, 20000000); //scrollpane.getViewport().getViewSize().height); //rowHeight*numOfRows);
        setMaximumSize(d);
        setMinimumSize(d);
        setPreferredSize(d);
//        setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        enableEvents(java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK | java.awt.AWTEvent.MOUSE_EVENT_MASK);
        setTransferHandler(dbTable.jTable.getTransferHandler());
        initializeVerticalRowBarDropTarget();
    }

    public void paint(Graphics grph) {
        if (grph == null) return;
        upperCorner = scrollpane.getViewport().getViewPosition().y;

        int portHeight = scrollpane.getViewport().getExtentSize().height;
        lowerCorner = upperCorner + portHeight;

        int rowHeight = table.getRowHeight();

        if (drawActiveRowOnly) {
            drawActiveRowOnly = false;
            drawActiveRow(grph, false);
            return;
        }

        if (!isDragging) {
            grph.setColor(dbTable.verticalRowBarColor); //Color.lightGray);
            grph.fillRect(0, upperCorner, preferredWidth, portHeight);

            if (table.getRowCount() > 0) {
                int topRow = table.rowAtPoint(new Point(0, upperCorner))+1;

                int lastRow = table.getRowCount();
                //int topRowHeight = rowHeight + ((topRow-1)*(rowHeight+1));
                int topRowHeight = rowHeight + ((topRow-1)*rowHeight);
                int lastRowHeight = rowHeight + lastRow*rowHeight;
                int startDrawingFrom = topRowHeight-upperCorner;

                if (startDrawingFrom == rowHeight)
                    startDrawingFrom = 0;
                int stopDrawingAt = (lastRowHeight < lowerCorner)? (lastRowHeight-rowHeight): lowerCorner;

                int pos, counter=0;
                /*if (startDrawingFrom == 0){
                counter--;
                } */

                int currRow;
                //for (int i=startDrawingFrom+upperCorner; i+counter<=stopDrawingAt; i+=rowHeight) {
                for (int i=startDrawingFrom+upperCorner; i<=stopDrawingAt; i+=rowHeight) {
                    //pos = i+counter;
                    pos = i-1;

                    grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
                    grph.drawLine(1, pos, preferredWidth-2, pos);
                    grph.setColor(dbTable.verticalRowBarBrightColor);
                    pos++;
                    grph.drawLine(1, pos, preferredWidth-2, pos);
                    currRow = table.rowAtPoint(new Point(0, pos+(rowHeight/2)));

//System.out.println("currRow: " + currRow + ", dbTable.table.getRecordEntryStructure().getPendingRecordIndex(): " + dbTable.table.getRecordEntryStructure().getPendingRecordIndex());
                    if (currRow != -1 &&
                            currRow != dbTable.table.getRecordEntryStructure().getPendingRecordIndex() &&
                            dbTable.table.isRecordSelected(dbTable.table.recordIndex.get(currRow))) {
                        if (displayRecordSelection)
//                        if (dbTable.selectedRecordDrawMode == DBTable.ON_ROW_BAR_AND_TABLE || dbTable.selectedRecordDrawMode == DBTable.ON_ROW_BAR_ONLY)
                            grph.drawImage(selectedRecImg, 6, pos+((rowHeight-10)/2), null);
                    }
                    counter++;
                }

                if (lastRowHeight < lowerCorner) {
                    grph.setColor(dbTable.verticalRowBarColor); //Color.lightGray);
                    grph.fillRect(0, lastRowHeight, preferredWidth-1, lowerCorner-lastRowHeight);
                }
            }
            grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
            grph.drawLine(preferredWidth-1, upperCorner, preferredWidth-1, lowerCorner);
            grph.setColor(dbTable.verticalRowBarBrightColor);
            grph.drawLine(1, upperCorner, 1, lowerCorner);
            grph.drawLine(preferredWidth, upperCorner, preferredWidth, lowerCorner);

/* Drawing an 'verticalRowBarBrightColor' line at the top of the VerticalRowBar
            * and a 'darkGray' line at its bottom. These lines make the corner
            * panels more dinstict.
            */
            grph.setColor(dbTable.verticalRowBarBrightColor);
            grph.drawLine(2, upperCorner, preferredWidth-2, upperCorner);
            grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
            grph.drawLine(2, lowerCorner-1, preferredWidth, lowerCorner-1);

            /* Draw the active record mark.
            */

            drawActiveRow(grph, false);
        }else{
            grph.setColor(dbTable.verticalRowBarColor); //Color.lightGray);
            int tmp = firstDragPosition;
            if (firstDragPosition > maxDragPosition) {
                firstDragPosition = firstDragPosition-(rowHeight+1);
                if (firstDragPosition < upperCorner) {
                    firstDragPosition = upperCorner;
                }
            }

            grph.fillRect(0, firstDragPosition, preferredWidth, java.lang.Math.abs(maxDragPosition-firstDragPosition));

            int pos, counter=0;
            for (int i=firstDragPosition; i<=maxDragPosition; i=i+rowHeight) {
                pos = i+counter;
                grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
                grph.drawLine(1, pos, preferredWidth-2, pos);
                grph.setColor(dbTable.verticalRowBarBrightColor);
                pos++;
                grph.drawLine(1, pos, preferredWidth-2, pos);
                counter++;
            }
            grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
            grph.drawLine(preferredWidth-1, firstDragPosition, preferredWidth-1, maxDragPosition);
            grph.setColor(dbTable.verticalRowBarBrightColor);
            grph.drawLine(1, firstDragPosition, 1, maxDragPosition);
            grph.drawLine(preferredWidth, firstDragPosition, preferredWidth, maxDragPosition);
            firstDragPosition = tmp;
        }
    }

    protected void processMouseMotionEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_MOVED) {
            int currY = e.getY();
            int currRow = table.rowAtPoint(e.getPoint()) + 1;
            int rowHeight = table.getRowHeight();
            //int rowBottomLine = rowHeight + ((currRow-1) * (rowHeight+1));
            int rowBottomLine = rowHeight + ((currRow-1) * rowHeight);
            if (currY >= rowBottomLine-2 || currY <= rowBottomLine-rowHeight+2) {
                setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                resizeEnabled = true;
                firstDragPosition = currY;
                maxDragPosition = -1;
            }else{
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //getDefaultCursor());
                resizeEnabled = false;
                firstDragPosition = -1;
            }
        }else if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
            if (resizeEnabled) {
                if (!isDragging)
                    isDragging = true;

                Graphics grph = getGraphics();
                if (e.getY() < firstDragPosition-table.getRowHeight() ||
                        e.getY() < upperCorner ||
                        e.getY() > lowerCorner) {
                    paint(grph);
                    return;
                }

                if (maxDragPosition < e.getY())
                    maxDragPosition = e.getY();

                paint(grph);
                grph.setColor(Color.black);
                grph.drawLine(1, e.getY(), preferredWidth, e.getY());
                grph.drawLine(1, e.getY()+1, preferredWidth, e.getY()+1);
            }else{
                if (mousePressedNoDragOccured) {
                    TransferHandler th = getTransferHandler();
                    th.exportAsDrag(this, e, TransferHandler.COPY);
                    mousePressedNoDragOccured = false;
                }
            }
        }
    }


    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            mousePressedNoDragOccured = false;
            if (isDragging) {
                isDragging = false;
//                resizeEnabled = false;
                maxDragPosition = -1;
                int newHeight = table.getRowHeight() + e.getY()-firstDragPosition;
                if (newHeight>=7 && newHeight<=299) {
                    dbTable.setRowHeight(newHeight);
                    dbTable.scrollpane.getViewport().validate();
                    dbTable.scrollpane.getViewport().doLayout();
                    dbTable.scrollpane.getViewport().repaint(dbTable.scrollpane.getViewport().getVisibleRect());
                }

                paint(getGraphics());
                dbTable.ucp.paint(dbTable.ucp.getGraphics());
                dbTable.lcp.paint(dbTable.lcp.getGraphics());

                /* Check if the cursor is above a line
                */
                int currY = e.getY();
                int currRow = table.rowAtPoint(e.getPoint()) + 1;
                int rowHeight = table.getRowHeight();
                int rowBottomLine = rowHeight + ((currRow-1) * (rowHeight+1));
                if (currY >= rowBottomLine-2 && currY <= rowBottomLine+2) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                    resizeEnabled = true;
                    firstDragPosition = currY;
                    maxDragPosition = -1;
                }else{
                    setCursor(Cursor.getDefaultCursor());
                    resizeEnabled = false;
                    firstDragPosition = -1;
                }
            }
        }

        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            if (dbTable.dbComponent != null && dbTable.dbComponent.visiblePopupMenu != null && dbTable.dbComponent.visiblePopupMenu.isVisible())
                dbTable.dbComponent.visiblePopupMenu.setVisible(false);

            mousePressedNoDragOccured = true;

            int currY = e.getY();
            int currRow = table.rowAtPoint(e.getPoint());
            if (currRow == -1)
                return;
            int rowHeight = table.getRowHeight();
            //int rowBottomLine = rowHeight + ((currRow) * (rowHeight+1));
            int rowBottomLine = rowHeight + ((currRow) * rowHeight);
            if (currY >= rowBottomLine-2 && currY <= rowBottomLine+2) {
                return;
            }else{
                int currRecord = dbTable.table.recordIndex.get(currRow);
                if (e.getModifiers() == e.BUTTON3_MASK) {
                    int prevActiveRow = dbTable.activeRow;
                    if (currRecord == dbTable.table.getActiveRecord())
                        dbTable.table.setActiveRecord(-1);
                    else{
                        dbTable.setActiveRow(currRow);
                        if (!dbTable.table.isRecordSelected(currRecord))
                            resetActiveRow(currRow);
                        drawActiveRow(getGraphics(), false);
                        if (prevActiveRow == dbTable.activeRow)
                            dbTable.refreshRow(currRow, false);
                    }
                }else{
                    dbTable.iterateEvent = false;
                    if (dbTable.recordSelectionChangeAllowed) {
                        if (dbTable.table.isRecordSelected(currRecord))
                            dbTable.table.removeFromSelectedSubset(currRecord);
                        else
                            dbTable.table.addToSelectedSubset(currRecord);
                    }

                    int prevActiveRow = dbTable.activeRow;
                    dbTable.setActiveRow(currRow);
                    if (!dbTable.table.isRecordSelected(currRecord))
                        resetActiveRow(currRow);
                    drawActiveRow(getGraphics(), false);
                    if (prevActiveRow == dbTable.activeRow)
                        dbTable.refreshRow(currRow, false);
                }
            }
        }
    }


    protected void resetActiveRow(int activeRow) {
//        int activeRecord = dbTable.dbTable.getActiveRecord();
        if (activeRow == -1)
            return;

        upperCorner = scrollpane.getViewport().getViewPosition().y;
        int portHeight = scrollpane.getViewport().getExtentSize().height;
        lowerCorner = upperCorner + portHeight;
        Graphics grph = getGraphics();
        if (grph == null) return;
        int rowHeight = table.getRowHeight();
        int rowBottomLine = rowHeight + (activeRow * rowHeight); //(rowHeight+1));
        int rowTopLine = rowBottomLine - rowHeight;

        /* If the active record is not contained in the current viewport
        * then don't draw it.
        */
        if (rowBottomLine < upperCorner || rowTopLine > lowerCorner)
            return;

        if (rowTopLine + rowHeight > lowerCorner) {
            rowHeight = lowerCorner-rowTopLine;
            rowBottomLine = rowTopLine+rowHeight;
        }

        if (rowTopLine < upperCorner) {
            rowHeight = rowHeight - (upperCorner - rowTopLine);
            rowTopLine = upperCorner;
        }

        grph.setColor(dbTable.verticalRowBarColor); //Color.lightGray);
        grph.fillRect(2, rowTopLine, preferredWidth-2, rowHeight);

        grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
        grph.drawLine(1, rowBottomLine-1, preferredWidth-2, rowBottomLine-1);
        grph.setColor(dbTable.verticalRowBarBrightColor);
        grph.drawLine(1, rowTopLine, preferredWidth-2, rowTopLine);

        grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
        grph.drawLine(preferredWidth-1, rowTopLine, preferredWidth-1, rowBottomLine);
        grph.setColor(dbTable.verticalRowBarBrightColor);
        grph.drawLine(1, rowTopLine, 1, rowBottomLine);
        grph.drawLine(preferredWidth, rowTopLine, preferredWidth, rowBottomLine);

        if (activeRow < dbTable.table.getRecordCount() && dbTable.table.isRecordSelected(dbTable.table.recordIndex.get(activeRow))) {
            if (displayRecordSelection)
//            if (dbTable.selectedRecordDrawMode == DBTable.ON_ROW_BAR_AND_TABLE || dbTable.selectedRecordDrawMode == DBTable.ON_ROW_BAR_ONLY)
                grph.drawImage(selectedRecImg, 6, rowTopLine+((rowHeight-10)/2), null);
        }
    }

    public Rectangle getRectangle(int row, Rectangle rect) {
        if (rect == null)
            rect = new Rectangle();
        if (row < 0 || row >= dbTable.table.getRecordCount()) {
            rect.width = 0;
            rect.height = 0;
            return rect;
        }
        int rowHeight = table.getRowHeight();
        //int rowBottomLine = rowHeight + ((activeRow) * (rowHeight+1));
        int rowBottomLine = rowHeight + row*rowHeight;
        rect.x = 0;
        rect.y = rowBottomLine - rowHeight;
        rect.width = preferredWidth;
        rect.height = rowHeight;
        return rect;
    }

    protected void drawActiveRow(Graphics grph, boolean scroll) {
        int activeRow = dbTable.activeRow;
        if (activeRow == -1)
            return;
        int rowHeight = table.getRowHeight();
        //int rowBottomLine = rowHeight + ((activeRow) * (rowHeight+1));
        int rowBottomLine = rowHeight + activeRow*rowHeight;

        int rowTopLine = rowBottomLine - rowHeight;
        int middle = rowBottomLine-(rowHeight/2);

        /* If the active record is not contained in the current viewport
        * then don't draw it.
        */
        if (rowBottomLine <= upperCorner || rowTopLine >= lowerCorner)
            return;

        if (scroll) {
            int selColumn = table.getSelectedColumn();
            if (selColumn == -1)
                selColumn = 0;
            if (rowTopLine < upperCorner) {
                table.scrollRectToVisible(table.getCellRect(activeRow, selColumn, true));
                return;
            }

            if (rowBottomLine > lowerCorner) {
                table.scrollRectToVisible(table.getCellRect(activeRow, selColumn, true));
                return;
            }
        }else{
            if (rowTopLine < upperCorner) {
                rowHeight = rowHeight-(upperCorner-rowTopLine);
                rowTopLine = upperCorner;
            }
            if (rowBottomLine > lowerCorner) {
                rowHeight = rowHeight-(rowBottomLine-lowerCorner);
                rowBottomLine = lowerCorner;
            }
        }

        if (middle-4 < lowerCorner && middle-4 > upperCorner) {
            if (displayActiveRecord)
//            if (dbTable.activeRecordDrawMode == DBTable.ON_ROW_BAR_AND_TABLE || dbTable.activeRecordDrawMode == DBTable.ON_ROW_BAR_ONLY)
                grph.drawImage(activeRecordImg, 7, middle-4, null);

            if (dbTable.table.isRecordSelected(dbTable.table.recordIndex.get(activeRow))) {
                if (displayRecordSelection)
//                if (dbTable.selectedRecordDrawMode == DBTable.ON_ROW_BAR_AND_TABLE || dbTable.selectedRecordDrawMode == DBTable.ON_ROW_BAR_ONLY)
                    grph.drawImage(selectedRecImg, 6, rowTopLine+((rowHeight-10)/2), null);
            }
        }
    }

    private void initializeVerticalRowBarDropTarget() {
        if (dropTargetListener == null) {
            dropTargetListener = new DropTargetListener() {
                public void dragEnter(DropTargetDragEvent e) {
                    if (!((DBTableTransferHandler) getTransferHandler()).canImport(VerticalRowBar.this, e.getCurrentDataFlavors(), e.getLocation()))
                        e.rejectDrag();
                }

                public void dragOver(DropTargetDragEvent e) {
                    if (!((DBTableTransferHandler) getTransferHandler()).canImport(VerticalRowBar.this, e.getCurrentDataFlavors(), e.getLocation()))
                        e.rejectDrag();
                }

                public void dropActionChanged(DropTargetDragEvent e) {
                }

                public void dragExit(DropTargetEvent e) {
                }

                public void drop(DropTargetDropEvent e) {
                    if (((DBTableTransferHandler) getTransferHandler()).importData(VerticalRowBar.this, e.getTransferable(), e.getLocation()))
                        e.acceptDrop(e.getDropAction());
                    e.dropComplete(true);
                }

                private boolean isDragOK(Component comp, Transferable transferable) {
                    if (comp == VerticalRowBar.this && transferable.isDataFlavorSupported(TransferableRow.DBTABLE_ROW_FLAVOR))
                        return true;
                    return false;
                }
            };
        }
        dropTarget = new DropTarget(this, dropTargetListener);
        setDropTarget(dropTarget);
    }

    public boolean isFocusable() {
        return false;
    }
}
