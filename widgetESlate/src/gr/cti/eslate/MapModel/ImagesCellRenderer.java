package gr.cti.eslate.mapModel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

class ImagesCellRenderer extends JLabel implements ListCellRenderer {
	private static ImageIcon defaultIcon;
	ImagesCellRenderer() {
		setHorizontalAlignment(SwingConstants.LEFT);
		setBorder(new CompoundBorder(new EmptyBorder(1,1,1,1),new LineBorder(new Color(225,225,225))));
		setForeground(Color.black);
		setBackground(new Color(198,255,198));
		setOpaque(true);
		defaultIcon=new ImageIcon(Map.class.getResource("images/defaultback.gif"));
	}

	public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
		if (isSelected)
			setOpaque(true);
		else
			setOpaque(false);
		MapBackground id=(MapBackground) value;
		if ((id.getDateFrom()!=null) && (id.getDateTo()!=null)) {
			java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat(id.getDateFormatPattern());
			if ((id.getFilename()==null) || (id.getFilename().equals("")))
				setText(MapCreator.bundleCreator.getString("image")+" ("+sdf.format(id.getDateFrom())+" - "+sdf.format(id.getDateTo())+")");
			else
				setText(id.getFilename()+" ("+sdf.format(id.getDateFrom())+" - "+sdf.format(id.getDateTo())+")");
		} else {
			if ((id.getFilename()==null) || (id.getFilename().equals("")))
				setText(MapCreator.bundleCreator.getString("image")+" ("+MapCreator.bundleCreator.getString("nodate")+")");
			else
				setText(id.getFilename()+" ("+MapCreator.bundleCreator.getString("nodate")+")");
		}
		if (id.isDefault()) {
			setIcon(defaultIcon);
			setToolTipText(MapCreator.bundleCreator.getString("isdefault"));
		} else {
			setIcon(null);
			setToolTipText(null);
		}
		return this;
	}
}

