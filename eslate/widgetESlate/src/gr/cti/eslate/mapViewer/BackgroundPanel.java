package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

class BackgroundPanel extends gr.cti.eslate.utils.NoBorderToggleButton {
	private Icon ic;
	String id;
	private static FontMetrics fm;

	BackgroundPanel(Icon ic,String id) {
		try  {
			jbInit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		this.id=id;
		fm=getFontMetrics(getFont());

		setFocusPainted(false);
		setSize(150,50);
		setMargin(new Insets(0,0,0,0));
		doLayout();
		//Find the border insets and the width and height to shrink the image
		JToggleButton tb=new JToggleButton();
		Insets in=tb.getInsets();
		int wd=getWidth()-in.left-in.right;
		int hd=getHeight()-in.top-in.bottom;

		if (ic instanceof NewRestorableImageIcon)
			this.ic=new ImageIcon(Helpers.scaleImageOnRect(((NewRestorableImageIcon) ic).getImage(),wd,hd));
		else if (ic instanceof ImageIcon)
			this.ic=new ImageIcon(Helpers.scaleImageOnRect(((ImageIcon) ic).getImage(),wd,hd));
		else {
			BufferedImage bi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ic.getIconWidth(),ic.getIconHeight(),Transparency.TRANSLUCENT);
			ic.paintIcon(this,bi.getGraphics(),0,0);
			this.ic=new ImageIcon(Helpers.scaleImageOnRect(bi,wd,hd));
		}
	}
	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		for (int i=0;i<getComponentCount();i++)
			((Component) getComponents()[i]).setFont(f);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ic.paintIcon(this,g,(getWidth()-ic.getIconWidth())/2,(getHeight()-ic.getIconHeight())/2);
		g.setColor(Color.white);
		g.drawString(id,4,fm.getAscent()+4);
		g.setColor(Color.black);
		g.drawString(id,3,fm.getAscent()+3);
	}

	public void processMouseEvent(MouseEvent e) {
		if (e.getClickCount()<2)
			super.processMouseEvent(e);
	}

	public Dimension getPreferredSize() {
		if (getParent()==null)
			return new Dimension(getSize().width,getSize().height);
		else
			return new Dimension(getParent().getWidth(),getSize().height);
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	private void jbInit() throws Exception {
	}
}
