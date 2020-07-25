package gr.cti.eslate.mapViewer;

import gr.cti.eslate.protocol.IAgent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class PathPanel extends gr.cti.eslate.utils.NoBorderToggleButton {
	private SmartLayout smartLayout1 = new SmartLayout();
	private JLabel lblFace = new JLabel();
	DragArea pnlDrag = new DragArea() {
		//Pass the enter/exit event to the parent. If not, the Toggle Button looses the border.
		protected void processMouseEvent(MouseEvent e) {
			super.processMouseEvent(e);
			if ((e.getID()==MouseEvent.MOUSE_ENTERED || e.getID()==MouseEvent.MOUSE_EXITED) && getParent()!=null) {
				Point p=SwingUtilities.convertPoint((Component) e.getSource(),e.getX(),e.getY(),getParent());
				getParent().dispatchEvent(new MouseEvent(PathPanel.this,e.getID(),System.currentTimeMillis(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),e.isPopupTrigger()));
			}
		}
	};
	/**
	 * Controls the visibility of the layer.
	 */
	JCheckBox cbxVisib = new JCheckBox() {
		//Pass the enter/exit event to the parent. If not, the Toggle Button looses the border.
		protected void processMouseEvent(MouseEvent e) {
			super.processMouseEvent(e);
			if ((e.getID()==MouseEvent.MOUSE_ENTERED || e.getID()==MouseEvent.MOUSE_EXITED) && getParent()!=null) {
				Point p=SwingUtilities.convertPoint((Component) e.getSource(),e.getX(),e.getY(),getParent());
				getParent().dispatchEvent(new MouseEvent(PathPanel.this,e.getID(),System.currentTimeMillis(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),e.isPopupTrigger()));
			}
		}
	};
	/**
	 * Paints the type of the layer.
	 */
	private JLabel lblName = new JLabel() {
		//Pass the enter/exit event to the parent. If not, the Toggle Button looses the border.
		protected void processMouseEvent(MouseEvent e) {
			super.processMouseEvent(e);
			Point p=SwingUtilities.convertPoint((Component) e.getSource(),e.getX(),e.getY(),getParent());
			getParent().dispatchEvent(new MouseEvent(PathPanel.this,e.getID(),System.currentTimeMillis(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),e.isPopupTrigger()));
		}
	};
	private static ImageIcon lampOn;
	private static ImageIcon lampOff;
	private static ImageIcon multiple;
	IAgent agent;
	/**
	 * This is to know whether a point layer with one icon has changed icon.
	 */
	private Icon cachedIcon;
	/**
	 * This holds a scaled image of a point layer with one icon.
	 */
	private ImageIcon scaled;

	PathPanel(IAgent agent) {
		try  {
			jbInit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		//Initializations
		setSize(150,28);
		this.agent=agent;
		lblName.setText(agent.getName());
		lblName.setToolTipText(agent.getName());
		lblName.setHorizontalTextPosition(JLabel.LEFT);
		BufferedImage bi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(agent.getFaceSize().width,agent.getFaceSize().height,Transparency.TRANSLUCENT);
		agent.paintFace(bi.getGraphics(),0);
		lblFace.setIcon(new ImageIcon(Helpers.scaleImageOnRect(bi,20,getSize().height)));
//        if (.hasPathVisible(agent))
			cbxVisib.setSelected(true);
		cbxVisib.setToolTipText(MapViewer.messagesBundle.getString("leg.lampagenttip"));
		pnlDrag.setToolTipText(MapViewer.messagesBundle.getString("leg.dragagenttip"));
		pnlDrag.setOpaque(false);
		pnlDrag.setBorder(new LineBorder(new Color(0,0,0,128)));

		setFocusPainted(false);
		setMargin(new Insets(0,0,0,0));
		lampOff=Helpers.loadImageIcon("images/lampoff.gif");
		lampOn=Helpers.loadImageIcon("images/lampon.gif");
		multiple=Helpers.loadImageIcon("images/multipleicons.gif");
		cbxVisib.setIcon(lampOff);
		cbxVisib.setSelectedIcon(lampOn);
		cbxVisib.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		//The button cannot remain pressed when invisible
		cbxVisib.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cbxVisib.isSelected() && isSelected())
					setSelected(false);
			}
		});
		//Handle the boldness of the label
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Font f=lblName.getFont();
				if (isSelected())
					lblName.setFont(new Font(f.getName(),Font.BOLD,f.getSize()));
				else
					lblName.setFont(new Font(f.getName(),Font.PLAIN,f.getSize()));
			}
		});
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

	public void processMouseEvent(MouseEvent e) {
		if (e.getClickCount()<2)
			super.processMouseEvent(e);
		if (e.getID()==MouseEvent.MOUSE_PRESSED) {
			//When the lamp is off and the button is pressed, the lamp goes on.
			if (!cbxVisib.isSelected())
				cbxVisib.setSelected(true);
			super.processMouseEvent(e);
		} else
			super.processMouseEvent(e);
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
		//First adjust to reflect any changes in the layer.
		//I didn't prefer to add listeners to avoid strugling with disconnections
		//and to avoid loading with extra calls.
		if (!agent.getName().equals(lblName.getText()))
			lblName.setText(agent.getName());
//        if (cbxVisib.isSelected() != lv.isVisible())
//            cbxVisib.setSelected(!cbxVisib.isSelected());
		super.paintComponent(g);
	}

	private void jbInit() throws Exception {
		this.setLayout(smartLayout1);
		pnlDrag.setBackground(new Color((int) (Math.random()*255),(int) (Math.random()*255),(int) (Math.random()*255)));
		cbxVisib.setFocusPainted(false);
		lblName.setText("Layer");
		this.add(pnlDrag, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 10),
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 2),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 2),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 2)));
		this.add(cbxVisib, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(pnlDrag, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.FractionAnchor(null, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, -3, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(lblName, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(cbxVisib, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(lblFace, Anchor.Left, Anchor.Left, Anchor.Right, 2),
					new com.thwt.layout.FractionAnchor(cbxVisib, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(lblFace, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 20),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 3)));
	}
}
