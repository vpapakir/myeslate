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

class LayerInfoCellRenderer2 extends JLabel implements ListCellRenderer {

    LayerInfoCellRenderer2() {
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(new CompoundBorder(new EmptyBorder(1,1,1,1),new LineBorder(new Color(225,225,225))));
        setForeground(Color.black);
        setBackground(new Color(181,222,255));
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
        if (isSelected)
            setOpaque(true);
        else
            setOpaque(false);
        if (((LayerInfo) value).getLayer()!=null && ((LayerInfo) value).getLayer().isMotionLayer()) {
            setIcon(RegionInfo.motionIcon);
            setText(((LayerInfo) value).getName()+" ("+Helpers.getHumanReadableMotionKey(((LayerInfo) value).getLayer().getMotionID())+")");
            setToolTipText(MapCreator.bundleCreator.getString("motionlayer"));
        } else {
            setIcon(null);
            setText(((LayerInfo) value).getName());
            setToolTipText(null);
        }
        return this;
    }
}
