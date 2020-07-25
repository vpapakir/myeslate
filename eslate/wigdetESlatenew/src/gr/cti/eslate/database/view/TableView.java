/* Stores all the UI settings for a DBEngine Table.
 */
package gr.cti.eslate.database.view;

import java.awt.Color;
import java.awt.Font;
import com.objectspace.jgl.Array;
import javax.swing.JTable;
import java.io.IOException;
import java.io.Externalizable;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;


public class TableView implements Externalizable {
//    public static final String STR_FORMAT_VERSION = "1.0";
    public static final int FORMAT_VERSION = 2;
    static final long serialVersionUID = 12L;
    private boolean nonEditableFieldsHighlighted = false;
    private Color integerColor = Color.black;
    private Color doubleColor = Color.black;
    private Color booleanColor = Color.black;
    private Color stringColor = Color.black;
    private Color dateColor = Color.black;
    private Color timeColor = Color.black;
    private Color urlColor = Color.black;
    private Font tableFont = new Font("Helvetica", Font.PLAIN, 12);
    private Color highlightColor = Color.yellow;
    private Color selectionColor = new Color(0,0,128);
    private Color gridColor = Color.black;
    private Color backgroundColor = Color.white;
    private int rowHeight = 20;
    private boolean horizontalLinesVisible = true;
    private boolean verticalLinesVisible = true;
    private boolean simultaneousFieldRecordSelection = false;
    private boolean headerIconsVisible = false;
    private int autoResizeMode = JTable.AUTO_RESIZE_OFF;
    private Color activeRecordColor = Color.yellow;
    private Array columnOrder = new Array();

    public TableView() {
    }

    public TableView(int fieldCount) {
        for (int i=0; i<fieldCount; i++)
            columnOrder.add(new Integer(i));
    }

    public TableView(TableView copiedView, int fieldCount) {
        nonEditableFieldsHighlighted = copiedView.nonEditableFieldsHighlighted;
        integerColor = copiedView.integerColor;
        doubleColor = copiedView.doubleColor;
        booleanColor = copiedView.booleanColor;
        stringColor = copiedView.stringColor;
        dateColor = copiedView.dateColor;
        timeColor = copiedView.timeColor;
        urlColor = copiedView.urlColor;
        Font copiedFont = copiedView.tableFont;
        tableFont = new Font(copiedFont.getFamily(), copiedFont.getStyle(), copiedFont.getSize());
        highlightColor = copiedView.highlightColor;
        selectionColor = copiedView.selectionColor;
        gridColor = copiedView.gridColor;
        backgroundColor = copiedView.backgroundColor;
        rowHeight = copiedView.rowHeight;
        horizontalLinesVisible = copiedView.horizontalLinesVisible;
        verticalLinesVisible = copiedView.verticalLinesVisible;
        simultaneousFieldRecordSelection = copiedView.simultaneousFieldRecordSelection;
        headerIconsVisible = copiedView.headerIconsVisible;
        autoResizeMode = copiedView.autoResizeMode;
        activeRecordColor = copiedView.activeRecordColor;
        if (copiedView.columnOrder != null)
            columnOrder = (Array) copiedView.columnOrder.clone();
        else{
            for (int i=0; i<fieldCount; i++)
                columnOrder.add(new Integer(i));
        }
    }

    public void setNonEditableFieldsHighlighted(boolean highlighted) {
        nonEditableFieldsHighlighted = highlighted;
    }
    public boolean isNonEditableFieldsHighlighted() {
        return nonEditableFieldsHighlighted;
    }
    public void setIntegerColor(Color intColor) {
        integerColor = intColor;
    }
    public Color getIntegerColor() {
        return integerColor;
    }
    public void setDoubleColor(Color dColor) {
        doubleColor = dColor;
    }
    public Color getDoubleColor() {
        return doubleColor;
    }
    public void setBooleanColor(Color blColor) {
        booleanColor = blColor;
    }
    public Color getBooleanColor() {
        return booleanColor;
    }
    public void setStringColor(Color strColor) {
        stringColor = strColor;
    }
    public Color getStringColor() {
        return stringColor;
    }
    public void setDateColor(Color dtColor) {
        dateColor = dtColor;
    }
    public Color getDateColor() {
        return dateColor;
    }
    public void setTimeColor(Color tmColor) {
        timeColor = tmColor;
    }
    public Color getTimeColor() {
        return timeColor;
    }
    public void setUrlColor(Color urlColor) {
        this.urlColor = urlColor;
    }
    public Color getUrlColor() {
        return urlColor;
    }
    public void setTableFont(Font f) {
        tableFont = f;
    }
    public Font getTableFont() {
        return tableFont;
    }
    public void setHighlightColor(Color hColor) {
        highlightColor = hColor;
    }
    public Color getHighlightColor() {
        return highlightColor;
    }
    public void setSelectionColor(Color sColor) {
        selectionColor = sColor;
    }
    public Color getSelectionColor() {
        return selectionColor;
    }
    public void setGridColor(Color gColor) {
        gridColor = gColor;
    }
    public Color getGridColor() {
        return gridColor;
    }
    public void setBackgroundColor(Color bgrColor) {
        backgroundColor = bgrColor;
    }
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    public void setRowHeight(int height) {
        rowHeight = height;
    }
    public int getRowHeight() {
        return rowHeight;
    }
    public void setHorizontalLinesVisible(boolean visible) {
        horizontalLinesVisible = visible;
    }
    public boolean isHorizontalLinesVisible() {
        return horizontalLinesVisible;
    }
    public void setVerticalLinesVisible(boolean visible) {
        verticalLinesVisible = visible;
    }
    public boolean isVerticalLinesVisible() {
        return verticalLinesVisible;
    }
    public void setSimultaneousFieldRecordSelection(boolean simultaneous) {
        simultaneousFieldRecordSelection = simultaneous;
    }
    public boolean isSimultaneousFieldRecordSelection() {
        return simultaneousFieldRecordSelection;
    }
    public void setHeaderIconsVisible(boolean visible) {
        headerIconsVisible = visible;
    }
    public boolean isHeaderIconsVisible() {
        return headerIconsVisible;
    }
    public void setAutoResizeMode(int mode) {
        autoResizeMode = mode;
    }
    public int getAutoResizeMode() {
        return autoResizeMode;
    }
    public void setActiveRecordColor(Color color) {
        activeRecordColor = color;
    }
    public Color getActiveRecordColor() {
        return activeRecordColor;
    }
    public void setColumnOrder(Array order) {
        columnOrder = order;
    }
    public Array getColumnOrder() {
        return columnOrder;
    }

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);//, 21);
        fieldMap.put("NonEditableFieldsHighlighted", nonEditableFieldsHighlighted);
        fieldMap.put("IntegerColor", integerColor);
        fieldMap.put("DoubleColor", doubleColor);
        fieldMap.put("BooleanColor", booleanColor);
        fieldMap.put("StringColor", stringColor);
        fieldMap.put("DateColor", dateColor);
        fieldMap.put("TimeColor", timeColor);
        fieldMap.put("URLColor", urlColor);
        fieldMap.put("TableFont", tableFont);
        fieldMap.put("HighlightColor", highlightColor);
        fieldMap.put("SelectionColor", selectionColor);
        fieldMap.put("GridColor", gridColor);
        fieldMap.put("BackgroundColor", backgroundColor);
        fieldMap.put("RowHeight", rowHeight);
        fieldMap.put("HorizontalLinesVisible", horizontalLinesVisible);
        fieldMap.put("VerticalLinesVisible", verticalLinesVisible);
        fieldMap.put("SimultaneousFieldRecordSelection", simultaneousFieldRecordSelection);
        fieldMap.put("HeaderIconsVisible", headerIconsVisible);
        fieldMap.put("AutoResizeMode", autoResizeMode);
        fieldMap.put("ActiveRecordColor", activeRecordColor);
        fieldMap.put("ColumnOrder", columnOrder);

        out.writeObject(fieldMap);
    }

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        nonEditableFieldsHighlighted = fieldMap.get("NonEditableFieldsHighlighted", false);
        integerColor = fieldMap.get("IntegerColor", Color.black);
        doubleColor = fieldMap.get("DoubleColor", Color.black);
        booleanColor = fieldMap.get("BooleanColor", Color.black);
        stringColor = fieldMap.get("StringColor", Color.black);
        dateColor = fieldMap.get("DateColor", Color.black);
        timeColor = fieldMap.get("TimeColor", Color.black);
        urlColor = fieldMap.get("URLColor", Color.black);
        tableFont = (Font) fieldMap.get("TableFont", new Font("Helvetica", Font.PLAIN, 12));
        highlightColor = fieldMap.get("HighlightColor", Color.yellow);
        selectionColor = fieldMap.get("SelectionColor", new Color(0, 0, 128));
        gridColor = fieldMap.get("GridColor", Color.black);
        backgroundColor = fieldMap.get("BackgroundColor", Color.white);
        rowHeight = fieldMap.get("RowHeight", 20);
        horizontalLinesVisible = fieldMap.get("HorizontalLinesVisible", true);
        verticalLinesVisible = fieldMap.get("VerticalLinesVisible", true);
        simultaneousFieldRecordSelection = fieldMap.get("SimultaneousFieldRecordSelection", false);
        headerIconsVisible = fieldMap.get("HeaderIconsVisible", false);
        autoResizeMode = fieldMap.get("AutoResizeMode", JTable.AUTO_RESIZE_OFF);
        activeRecordColor = fieldMap.get("ActiveRecordColor", Color.yellow);
        columnOrder = (Array) fieldMap.get("ColumnOrder");
    }
}