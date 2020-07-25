package gr.cti.eslate.database;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.border.Border;
import java.awt.Component;
import java.awt.Color;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.CImageIcon;
import javax.swing.Icon;

public abstract class DBCellRenderer extends JLabel implements TableCellRenderer {
    Table table;
    JTable JTable;
    DBTable dbTable;
//    static Color darkBlue = new Color(0, 0, 128);
    Color foregroundColor = Color.black;
    Color backgroundColor = Color.white;
//    Color selectionBackgroundColor = new Color(0, 0, 128);
//    Color selectionForegroundColor = Color.white;
//    Color activeBackgroundColor = Color.yellow;
    static Color activeForegroundColor = Color.black;
    static Border inactiveCellBorder = null;
    /** Determines if the selected records are highlighted in the DBTable. */
    boolean selectedRecordsBackgroundDrawn = true;
    /** Determines if the active records is highlighted in the DBTable. */
    boolean activeRecordBackgroundDrawn = true;
//    CompoundBorder cb = new CompoundBorder(new LineBorder(Color.yellow), eb);

    public DBCellRenderer(DBTable dbTable) {
        super();
        this.table = dbTable.table;
        this.JTable = dbTable.jTable;
        this.dbTable = dbTable;
        backgroundColor = dbTable.backgroundColor;
        setBackground(backgroundColor);
        setOpaque(true);
    }

    // This is the only method defined by ListCellRenderer.  We just
    // reconfigure the Jlabel each time we're called.
/*    public Component getTableCellRendererComponent (JTable jTable,
                                                Object value,            // value to display
                                                boolean isSelected,      // is the cell selected
                                                boolean cellHasFocus,    // the jTable and the cell have the focus
                                                int row,
                                                int column)
    {

        try{
        String s;
        if (value != null) {
            if (value.getClass().equals(java.lang.Double.class))
                s = table.getNumberFormat().format((Double) value);
            else if (value.getClass().equals(Float.class))
                s = table.getNumberFormat().format((Float) value);
            else if (value.getClass().equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
                s = "";
                Icon temp = ((CImageIcon) value).getIcon();
                setIcon(temp);
            }else if (value.getClass().equals(java.lang.Boolean.class)) {
                s = dbTable.dbComponent.infoBundle.getString(value.toString());
            }else
                s = value.toString();
        }else{
            s = "";
            setIcon(null);
        }

        setText(s);
*/

/*test        if (row == 0 && column == 0)
            setForeground(Color.orange);
*/ /*     }catch(Throwable t){
            t.printStackTrace();
            System.out.println("message: "+t.getMessage());
        }
        return this;
    }
*/
    void configureCell(JTable jTable, int row, int column, boolean isSelected) {
        if (row != table.getRecordEntryStructure().getPendingRecordIndex()) {
            int recIndex = table.recordIndex.get(row);
            boolean isRecordSelected = table.isRecordSelected(recIndex);
            if ((isRecordSelected || (isSelected)) && selectedRecordsBackgroundDrawn) {
                setBorder(inactiveCellBorder);
                setBackground(dbTable.selectionBackground);
                setForeground(dbTable.selectionForeground); //foregroundColor/*selectionForegroundColor*/);
            }else{
                setBorder(inactiveCellBorder);
                if (dbTable.twoColorBackgroundEnabled) {
                    if (row%2 == 1)
                        setBackground(dbTable.oddRowColor);
                    else
                        setBackground(dbTable.evenRowColor);
                }else{
                    setBackground(backgroundColor);
                }
                setForeground(foregroundColor);
            }

            if (recIndex == table.getActiveRecord()) {
                if (column == jTable.getSelectedColumn()) {
//                    setBorder(cb);
                    setBorder(dbTable.activeCellBorder);
                    AbstractTableField fld = dbTable.getTableField(column);
                    if (fld.isEditable() && table.isDataChangeAllowed()) {
//1                        setBackground(backgroundColor);
//1                        setForeground(foregroundColor);
    //                    System.out.println("Column: " + column + ", row: " + row);
                    }else{
                        if (activeRecordBackgroundDrawn)
                            setBackground(dbTable.activeRecordColor);
                        else
                            setBackground(dbTable.backgroundColor);
                        setForeground(activeForegroundColor);
                    }
                }else{
//1                    if (!(isRecordSelected && selectedRecordsBackgroundDrawn)) {
                        if (activeRecordBackgroundDrawn)
                            setBackground(dbTable.activeRecordColor);
                        setForeground(activeForegroundColor);
//1                    }
                }
            }
        }

    }


/*    public void setSelectionBackgroundColor(Color selectionBackgroundColor) {
        this.selectionBackgroundColor = selectionBackgroundColor;
    }

*/

    public void setSelectedRecordsBackgroundDrawn(boolean drawn) {
        this.selectedRecordsBackgroundDrawn = drawn;
    }

    public void setActiveRecordBackgroundDrawn(boolean drawn) {
        this.activeRecordBackgroundDrawn = drawn;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }


    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

/*    public void setActiveRecordBackgroundColor(Color activeRecordBackgroundColor) {
        this.activeBackgroundColor = activeRecordBackgroundColor;
    }
*/
}
