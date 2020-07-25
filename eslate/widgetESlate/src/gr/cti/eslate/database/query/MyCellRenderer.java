package gr.cti.eslate.database.query;

import javax.swing.ListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import java.awt.Component;
import java.awt.Color;
import gr.cti.eslate.database.engine.Table;


public class MyCellRenderer extends JLabel implements ListCellRenderer {
    Table ctable;
    static Color darkBlue = new Color(0, 0, 128);

    public MyCellRenderer(Table ctable) {
        super();
        this.ctable = ctable;
        setBackground(Color.white);
        setOpaque(true);
    }

    // This is the only method defined by ListCellRenderer.  We just
    // reconfigure the Jlabel each time we're called.
    public Component getListCellRendererComponent(JList list,
                                                Object value,            // value to display
                                                int index,               // cell index
                                                boolean isSelected,      // is the cell selected
                                                boolean cellHasFocus)    // the list and the cell have the focus
    {
        String s;
        if (value.getClass().equals(Double.class) || value.getClass().equals(Float.class))
            s = ctable.getNumberFormat().format((Number) value);
        else
            s = value.toString();

        setText(s);
        if (isSelected) {
            setBackground(darkBlue);
            setForeground(Color.white);
        }else{
            setBackground(Color.white);
            setForeground(Color.black);
        }
        if (cellHasFocus)
            setBorder(new LineBorder(Color.yellow));
        else
            setBorder(null);

        return this;
    }
}

