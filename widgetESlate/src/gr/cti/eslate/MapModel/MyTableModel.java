package gr.cti.eslate.mapModel;

import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
    public MyTableModel(int rows) {
        super();
        data=new Vector();
        for (int i=0;i<rows;i++)
            data.addElement(new CRow(null,null,null,null));
        c0Name=MapCreator.bundleCreator.getString("value");
        c1Name=MapCreator.bundleCreator.getString("normal");
        c2Name=MapCreator.bundleCreator.getString("selected");
        c3Name=MapCreator.bundleCreator.getString("highlighted");
    }

    public int getRowCount(){
        return data.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int row, int column) {
        try {
            if (column==0)
                return ((CRow) data.elementAt(row)).value;
            else if (column==1)
                return ((CRow) data.elementAt(row)).icon;
            else if (column==2)
                return ((CRow) data.elementAt(row)).selIcon;
            else if (column==3)
                return ((CRow) data.elementAt(row)).highIcon;
            else
                return null;
        } catch(Exception e) {
            return null;
        }
    }

    public void setValueAt(Object obj,int row, int column) {
        try {
            CRow r=(CRow) data.elementAt(row);
            if (column==0)
                r.value=obj;
            else if (column==1)
                r.icon=(Icon) obj;
            else if (column==2)
                r.selIcon=(Icon) obj;
            else if (column==3)
                r.highIcon=(Icon) obj;
        } catch(Exception e) {
            System.err.println("MAP#200003091313: Cannot set cell value.");
            e.printStackTrace();
        }
    }

    public Class getColumnClass(int c) {
        if (c==0)
            return Object.class;
        else if ((c==1) || (c==2) || (c==3))
            return ImageIcon.class;
        else
            return null;
    }

    public String getColumnName(int column) {
        if (column==0)
            return c0Name;
        else if (column==1)
            return c1Name;
        else if (column==2)
            return c2Name;
        else if (column==3)
            return c3Name;
        return null;
    }

    public boolean isCellEditable(int row, int col) {
        return getColumnClass(col) == Object.class;
    }

    public void addRow() {
        data.addElement(new CRow(null,null,null,null));
    }

    public void deleteRow(int i) {
        data.removeElementAt(i);
    }

    public CRow getRow(int row) {
        return (CRow) data.elementAt(row);
    }

    private String c0Name,c1Name,c2Name,c3Name;
    private Vector data;
}


