package gr.cti.eslate.mapModel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

class LayerInfoCellRenderer extends JLabel implements ListCellRenderer {
    LayerInfoCellRenderer() {
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(new CompoundBorder(new EmptyBorder(1,1,1,1),new LineBorder(new Color(225,225,225))));
        setForeground(Color.black);
        setBackground(new Color(198,255,198));
        setHorizontalTextPosition(SwingConstants.LEFT);
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
        if (isSelected)
            setOpaque(true);
        else
            setOpaque(false);
        setText(((LayerInfo) value).getName());
        return this;
    }
}

