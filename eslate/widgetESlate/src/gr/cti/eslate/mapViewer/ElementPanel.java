package gr.cti.eslate.mapViewer;

import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IPointLayerView;
import gr.cti.eslate.protocol.IPolyLineLayerView;
import gr.cti.eslate.protocol.IPolygonLayerView;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class ElementPanel extends gr.cti.eslate.utils.NoBorderToggleButton {
	private SmartLayout smartLayout1 = new SmartLayout();
	DragArea pnlDrag = new DragArea() {
		//Pass the enter/exit event to the parent. If not, the Toggle Button looses the border.
		protected void processMouseEvent(MouseEvent e) {
			super.processMouseEvent(e);
			if ((e.getID()==MouseEvent.MOUSE_ENTERED || e.getID()==MouseEvent.MOUSE_EXITED) && pnlDrag.getParent()!=null) {
				Point p=SwingUtilities.convertPoint((Component) e.getSource(),e.getX(),e.getY(),pnlDrag.getParent());
				pnlDrag.getParent().dispatchEvent(new MouseEvent(ElementPanel.this,e.getID(),System.currentTimeMillis(),e.getModifiers(),p.x,p.y,e.getClickCount(),e.isPopupTrigger()));
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
			/* THIS PIECE OF SHIT DOESN'T WORK
			if (e.getID()==MouseEvent.MOUSE_RELEASED) {
				//The button cannot remain pressed when invisible
				if (!cbxVisib.isSelected() && ElementPanel.this.isSelected())
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							ElementPanel.this.dispatchEvent(new MouseEvent(ElementPanel.this,MouseEvent.MOUSE_CLICKED,System.currentTimeMillis(),0,0,0,1,false));
						}
					});
			}
			*/
			if (e.getID()==MouseEvent.MOUSE_RELEASED || e.getID()==MouseEvent.MOUSE_PRESSED || e.getID()==MouseEvent.MOUSE_CLICKED)
				e.consume();
			if ((e.getID()==MouseEvent.MOUSE_ENTERED || e.getID()==MouseEvent.MOUSE_EXITED) && cbxVisib.getParent()!=null) {
				Point p=SwingUtilities.convertPoint((Component) e.getSource(),e.getX(),e.getY(),cbxVisib.getParent());
				cbxVisib.getParent().dispatchEvent(new MouseEvent(ElementPanel.this,e.getID(),System.currentTimeMillis(),e.getModifiers(),p.x,p.y,e.getClickCount(),e.isPopupTrigger()));
			}
		}

		public boolean isOpaque() {
			return false;
		}
	};
	/**
	 * Paints the type of the layer.
	 */
	private JPanel pnlStyle = new JPanel() {
		//Pass the enter/exit event to the parent. If not, the Toggle Button looses the border.
		protected void processMouseEvent(MouseEvent e) {
			super.processMouseEvent(e);
			Point p=SwingUtilities.convertPoint((Component) e.getSource(),e.getX(),e.getY(),getParent());
			getParent().dispatchEvent(new MouseEvent(ElementPanel.this,e.getID(),System.currentTimeMillis(),e.getModifiers(),p.x,p.y,e.getClickCount(),e.isPopupTrigger()));
		}
		public void paintComponent(Graphics g) {
			//Paint the shape type
			Graphics2D g2=(Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

			if (lv instanceof IPointLayerView) {
				IPointLayerView plv=(IPointLayerView) lv;
				if (plv.getPaintMode()==IPointLayerView.PAINT_AS_CIRCLE) {
					if (plv.isCircleFilled()) {
						g2.setColor(plv.getNormalFillColor());
						g2.fillOval(getWidth()/2-3,getHeight()/2-3,6,6);
					}
					g2.setColor(plv.getNormalOutlineColor());
					g2.drawOval(getWidth()/2-3,getHeight()/2-3,6,6);
				} else if (plv.getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS) {
					//Has the icon changed?
					if (cachedIcon!=(cachedIcon=plv.getNormalIcon(null))) {
						BufferedImage bi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(cachedIcon.getIconWidth(),cachedIcon.getIconHeight(),Transparency.TRANSLUCENT);
						cachedIcon.paintIcon(this,bi.getGraphics(),0,0);
						scaled=new ImageIcon(Helpers.scaleImageOnRect(bi,getWidth(),getHeight()));
					}
					scaled.paintIcon(this,g2,(getWidth()-multiple.getIconWidth())/2,(getHeight()-multiple.getIconHeight())/2);
				}  else if (plv.getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS) {
					multiple.paintIcon(this,g2,(getWidth()-multiple.getIconWidth())/2,(getHeight()-multiple.getIconHeight())/2);
				}
			} else if (lv instanceof IPolyLineLayerView) {
				IPolyLineLayerView llv=(IPolyLineLayerView) lv;
				g2.setColor(llv.getNormalOutlineColor());
				if (llv.getPaintMode()==IPolyLineLayerView.PAINT_AS_STRAIGHT_LINE)
					g2.setStroke(new BasicStroke(2));
				else if (llv.getPaintMode()==IPolyLineLayerView.PAINT_AS_DASHED_LINE)
					g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,new float[]{2,4},0));
				else if (llv.getPaintMode()==IPolyLineLayerView.PAINT_AS_DOTTED_LINE)
					g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,new float[]{0.66f,6},0));
				int w2=getWidth()/2;
				int h2=getHeight()/2;
				g2.drawLine(0,h2+4,w2-4,h2-4);
				g2.drawLine(w2-4,h2-4,w2+4,h2+4);
				g2.drawLine(w2+4,h2+4,getWidth(),h2-4);
			} else if (lv instanceof IPolygonLayerView) {
				int h2=getHeight()/2;
				if (((IPolygonLayerView) lv).isPolygonFilled()) {
					g2.setColor(((IPolygonLayerView) lv).getNormalFillColor());
					g2.fillRect(2,h2-4,getWidth()-4,8);
				}
				g2.setColor(((IPolygonLayerView) lv).getNormalOutlineColor());
				g2.drawRect(1,h2-5,getWidth()-3,9);
			}
		}
	};
	private JLabel lblName = new JLabel() {
		//Pass the enter/exit event to the parent. If not, the Toggle Button looses the border.
		protected void processMouseEvent(MouseEvent e) {
			super.processMouseEvent(e);
			Point p=SwingUtilities.convertPoint((Component) e.getSource(),e.getX(),e.getY(),getParent());
			getParent().dispatchEvent(new MouseEvent(ElementPanel.this,e.getID(),System.currentTimeMillis(),e.getModifiers(),p.x,p.y,e.getClickCount(),e.isPopupTrigger()));
		}
	};
	private static ImageIcon multiple;
	ILayerView lv;
	/**
	 * This is to know whether a point layer with one icon has changed icon.
	 */
	private Icon cachedIcon;
	/**
	 * This holds a scaled image of a point layer with one icon.
	 */
	private ImageIcon scaled;

	ElementPanel(ILayerView lv) {
		try  {
			jbInit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		//Initializations
		this.lv=lv;
		lblName.setText(lv.getName());
		if (lv.isVisible())
			cbxVisib.setSelected(true);
		cbxVisib.setToolTipText(MapViewer.messagesBundle.getString("leg.lamptip"));
		pnlDrag.setToolTipText(MapViewer.messagesBundle.getString("leg.dragtip"));

		setFocusPainted(false);
		setSize(150,28);
		setMargin(new Insets(0,0,0,0));
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		multiple=Helpers.loadImageIcon("images/multipleicons.gif");
		/*
		Change the cursor on the checkbox (former lamp)
		cbxVisib.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (cbxVisib.isSelected())
					cbxVisib.setCursor(offCursor);
				else
					cbxVisib.setCursor(onCursor);
			}
		});
		*/
		cbxVisib.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		//Handle the boldness of the label and the caption
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Font f=lblName.getFont();
				if (isSelected()) {
					lblName.setFont(new Font(f.getName(),Font.BOLD,f.getSize()));
					pnlDrag.setBackground(SystemColor.activeCaption);
				} else {
					lblName.setFont(new Font(f.getName(),Font.PLAIN,f.getSize()));
					pnlDrag.setBackground(SystemColor.inactiveCaption);
				}
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
		//If it is being dragged, don't show the bordet etc and don't show the cursor
		try {
			if (((CategoryPane) getParent()).isDragging) {
				setCursor(pnlDrag.presCursor);
				return;
			}
		} catch (Exception ex) {}
		//If it cannot be pressed, don't show the border etc
		if (!lv.mayChangeObjectSelectability()) {
			setCursor(null);
			return;
		}
		if (e.getClickCount()>1)
			return;
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
		if (!lv.getName().equals(lblName.getText()))
			lblName.setText(lv.getName());
		if (cbxVisib.isSelected() != lv.isVisible())
			cbxVisib.setSelected(!cbxVisib.isSelected());
		if (lv.mayChangeObjectSelectability())
			lblName.setEnabled(true);
		else
			lblName.setEnabled(false);

		super.paintComponent(g);
	}

	private void jbInit() throws Exception {
		this.setLayout(smartLayout1);
		cbxVisib.setFocusPainted(false);
		pnlStyle.setOpaque(false);
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
		this.add(pnlStyle, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 25),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 20),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 2),
					new com.thwt.layout.FractionAnchor(null, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0)));
		this.add(lblName, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(cbxVisib, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(pnlStyle, Anchor.Left, Anchor.Left, Anchor.Right, 2),
					new com.thwt.layout.FractionAnchor(cbxVisib, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
	}
}
