package gr.cti.eslate.mapViewer;

import gr.cti.eslate.protocol.ILayerView;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

/**
 * The cell renderer for combos.
 */
class ComboRenderer extends JLabel implements ListCellRenderer {
    ComboRenderer() {
        setHorizontalAlignment(SwingConstants.LEFT);
        setHorizontalTextPosition(SwingConstants.LEFT);
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
    	if (value!=null)
    		setText(((ILayerView) value).getName());
    	else
    		setText("");
        return this;
    }
}

