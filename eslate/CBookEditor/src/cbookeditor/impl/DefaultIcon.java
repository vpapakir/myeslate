package cbookeditor.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class DefaultIcon implements Icon {

	public int getIconHeight() {
		return 16;
	}

	public int getIconWidth() {
		return 16;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(Color.white);
		g.fillRoundRect(x, y, getIconWidth(), getIconHeight(), 2, 2);
		g.setColor(Color.black);
		g.drawRoundRect(x, y, getIconWidth()-1, getIconHeight()-1, 2, 2);
	}

}
