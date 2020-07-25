// PreviewColorPanels by Drossos Nikos @ 4/2000
// Convenient way for displaying current background and foreground colors.
// Ability to switch foreground - background colors by double clicking the line
// between the panels.

package gr.cti.eslate.imageEditor;

import gr.cti.eslate.colorPalette.ColorPalette;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class PreviewColorPanels extends JPanel {
	CustomPanel forePanel, backPanel;

	Color fore, back;

	ColorPalette colorPalette;

	public PreviewColorPanels(ColorPalette colorPalette) {
		this.colorPalette=colorPalette;
		fore=colorPalette.getSelectedForegroundColor();
		back=colorPalette.getSelectedBackgroundColor();
		forePanel=new CustomPanel(colorPalette.getBundle());
		backPanel=new CustomPanel(colorPalette.getBundle());

		setForegroundColor(fore);
		setBackgroundColor(back);

		Dimension panelsDim=new Dimension(20,20);
		// foreground Panel
		forePanel.setBorder(new BevelBorder(BevelBorder.LOWERED,Color.gray,Color.black));
		forePanel.setSize(panelsDim);
		forePanel.setBackground(fore);
		forePanel.setLocation(0,0);

		// background Panel
		backPanel.setBorder(new BevelBorder(BevelBorder.LOWERED,Color.gray,Color.black));
		backPanel.setSize(panelsDim);
		backPanel.setBackground(back);
		backPanel.setLocation(9,9);

		setLayout(null);
		setPreferredSize(new Dimension(30,30));
		setMaximumSize(new Dimension(30,30));
		setMinimumSize(new Dimension(30,30));

		// setBorder(new LineBorder(Color.red));
		add(forePanel);
		add(backPanel);

		this.addMouseListener(clickListener);
	}

	// public boolean trans;
	// protected final AlphaColor outlineColor = new AlphaColor(150,75,75);

	public void setForegroundColor(Color color) {
		fore=color;
		if (color.getAlpha() != 255)
			forePanel.setTrans(true);
		else {
			forePanel.setTrans(false);
			forePanel.setBackground(color);
		}
		repaint();
	}

	public void setBackgroundColor(Color color) {
		back=color;
		if (color.getAlpha() != 255)
			backPanel.setTrans(true);
		else {
			backPanel.setTrans(false);
			backPanel.setBackground(color);
		}
		repaint();
	}

	public void update(Graphics g) {
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		// up arrow
		g.drawLine(1,22,3,20);
		g.drawLine(3,20,5,22);
		// right arrow
		g.drawLine(6,24,8,26);
		g.drawLine(6,28,8,26);

		// main line
		g.drawLine(3,20,3,26);
		g.drawLine(3,26,8,26);
	}

	MouseListener clickListener=new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			if (e.getX() > 1 && e.getX() < 8 && e.getY() < 28 && e.getY() > 20) {
				Color tmp=fore;
				fore=back;
				back=tmp;
				setForegroundColor(fore);
				setBackgroundColor(back);
				colorPalette.setSelectedForegroundColor(fore);
				colorPalette.setSelectedBackgroundColor(back);
			}
		}
	};

}

class CustomPanel extends JPanel {
	boolean transparent=false;

	ResourceBundle bundle;

	public CustomPanel(ResourceBundle bundle) {
		this.bundle=bundle;
	}

	public void setTrans(boolean trans) {
		transparent=trans;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (transparent) {
			g.setColor(Color.white);
			g.fillRect(2,2,16,16);
			g.setColor(Color.black);
			String trans=bundle.getString("trans");
			int TLength=g.getFontMetrics().stringWidth(trans);
			// g.drawString(trans, i*squareSize+squareSize/2 - TLength/2-1,j*squareSize+12);

			g.drawString(trans,9 - TLength / 2,13);
			g.drawString(trans,9 - TLength / 2 + 1,13);
			/*
			 * g.drawString("T", 7, 13); g.drawString("T", 8, 13);
			 */
		}
	}
}
