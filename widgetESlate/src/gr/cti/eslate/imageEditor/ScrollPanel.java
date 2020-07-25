// ScrollPanel by Drossos Nikos @ 4/2000
// Constructs a panel having the ability to host another panel
// within a speciall scrollPane. This scrollPane has spin buttons
// instead of scrollBars.
// Status: Only a colorPalette can be added

package gr.cti.eslate.imageEditor;

import gr.cti.eslate.colorPalette.ColorPalette;
import gr.cti.eslate.iconPalette.IconPalette;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pv.jfcx.JPVSpin;
import pv.jfcx.JPVSpinPlus;

public class ScrollPanel extends JPanel {
	int width, height;

	int increment;

	int mode;

	JScrollPane scrollPane;

	JPVSpinPlus spinControl;

	private final int HEIGHT_OFFSET=25;

	private final int WIDTH_OFFSET=10;

	int limitHeight=100;

	ColorPalette colorPalette;

	IconPalette iconPalette;

	public ScrollPanel(int wid,int heig) {
		scrollPane=new JScrollPane();
		spinControl=new JPVSpinPlus(JPVSpinPlus.VERTICAL_CENTER);

		width=wid;
		height=heig;
		setPreferredSize(new Dimension(width,height));
		setMaximumSize(new Dimension(width,height));
		setMinimumSize(new Dimension(width,height));

		scrollPane.setPreferredSize(new Dimension(width - WIDTH_OFFSET,height - HEIGHT_OFFSET));
		scrollPane.setMaximumSize(new Dimension(width - WIDTH_OFFSET,height - HEIGHT_OFFSET));
		scrollPane.setMinimumSize(new Dimension(width - WIDTH_OFFSET,height - HEIGHT_OFFSET));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		spinControl.setButtonWidth(HEIGHT_OFFSET);

		increment=35;

		spinControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mode == 0)
					limitHeight=colorPalette.getDimension().height;
				else
					limitHeight=iconPalette.getDimension().height;

				int previousYPos=scrollPane.getViewport().getViewPosition().y;
				if (e.getModifiers() == JPVSpin.DECREMENT) {
					if (previousYPos + increment <= (limitHeight - scrollPane.getViewport().getExtentSize().height))
						scrollPane.getViewport().setViewPosition(new Point(0,previousYPos + increment));
					else {
						if (limitHeight - scrollPane.getViewport().getExtentSize().height > 0)
							scrollPane.getViewport().setViewPosition(new Point(0,(limitHeight - scrollPane.getViewport().getExtentSize().height)));
					}
				} else {
					if (previousYPos - increment >= 0)
						scrollPane.getViewport().setViewPosition(new Point(0,previousYPos - increment));
					else
						scrollPane.getViewport().setViewPosition(new Point(0,0));
				}
				scrollPane.getGraphics().setClip(0,0,width,height - HEIGHT_OFFSET);
				repaint();
			}
		});
	}

	public void addPalette(ColorPalette palette) {
		mode=0;
		colorPalette=palette;
		scrollPane.setViewportView(palette);
		spinControl.setObject(scrollPane);
		limitHeight=palette.getDimension().height;
		add(spinControl);
	}

	public void addPalette(IconPalette palette) {
		mode=1;
		iconPalette=palette;
		scrollPane.setViewportView(palette);
		spinControl.setObject(scrollPane);
		limitHeight=palette.getDimension().height;
		add(spinControl);
	}

	/*
	 * public void setPanelDimension(int w, int h) { width = w; height = h; setPreferredSize(new
	 * Dimension(width,height)); setMaximumSize(new Dimension(width,height)); setMinimumSize(new
	 * Dimension(width,height));
	 * 
	 * scrollPane.setPreferredSize(new Dimension(width-WIDTH_OFFSET,height-HEIGHT_OFFSET));
	 * scrollPane.setMaximumSize(new Dimension(width-WIDTH_OFFSET,height-HEIGHT_OFFSET)); scrollPane.setMinimumSize(new
	 * Dimension(width-WIDTH_OFFSET,height-HEIGHT_OFFSET));
	 * 
	 * scrollPane.setViewportView(colorPalette); this.remove(spinControl); spinControl.setObject(scrollPane);
	 * limitHeight = colorPalette.getDimension().height; add(spinControl); doLayout(); }
	 */

}