package gr.cti.eslate.mapViewer;

import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.IZoomRect;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Stroke;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

/**
 * In this layer things like the selection rectangle are drawn.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
class AuxiliaryPane extends JPanel implements TransparentMouseInput {
	AuxiliaryPane(MapPane mapPane) {
		super();
		setOpaque(false);
		setLayout(new AuxiliaryLayout());
		this.mapPane=mapPane;
	}

	/**
	 * Should make public access to processMouseEvent.
	 * @param e The event.
	 */
	public void processMouseEvent(MouseEvent e) {
		super.processMouseEvent(e);
	}

	/**
	 * Should make public access to processMouseMotionEvent.
	 * @param e The event.
	 */
	public void processMouseMotionEvent(MouseEvent e) {
		super.processMouseMotionEvent(e);
	}

	/**
	 * Adds a MouseInputListener. This is different from adding a MouseListener and a MouseMotionListener seperately.
	 */
	public void addMouseInputListener(MouseInputListener l) {
		if (l==null) return;
		if (ml==null)
			ml=new ArrayList();
		ml.add(l);
		addMouseListener(l);
		addMouseMotionListener(l);
	}
	/**
	 * Removes a MouseInputListener.
	 */
	public void removeMouseInputListener(MouseInputListener l) {
		if (ml==null)
			return;
		ml.remove(l);
		removeMouseListener(l);
		removeMouseMotionListener(l);
	}

	/**
	 * The selection auxiliary shape one can manipulate from other classes.
	 */
	public SelectionShape getSelectionShape() {
		return selectionShape;
	}
	/**
	 * The selection auxiliary shape one can manipulate from other classes.
	 */
	public void setSelectionShape(SelectionShape s) {
		selectionShape=s;
	}
	/**
	 * Removes all Mouse and MouseMotion Listeners.
	 * @return A list containing the listeners removed.
	 */
	public java.util.List removeAllListeners() {
		ArrayList copy=new ArrayList();
		if (ml==null)
			return copy;
		for (int i=0;i<ml.size();i++) {
			removeMouseListener((MouseListener) ml.get(i));
			removeMouseMotionListener((MouseMotionListener) ml.get(i));
			copy.add(ml.get(i));
		}
		ml.clear();
		return copy;
	}
	/**
	 * Adds a shape to be drawn.
	 */
	protected void addShape(AuxiliaryShape r) {
		if (shapes==null)
			shapes=new ArrayList(5);
		if (r!=null)
			shapes.add(r);
	}
	/**
	 * Removes a shape.
	 */
	protected void removeShape(AuxiliaryShape r) {
		if (shapes==null)
			return;
		for (int i=0;i<shapes.size();i++)
			if (shapes.get(i).equals(r))
				shapes.remove(i);
	}
	/**
	 * Remove all shapes.
	 */
	protected void removeAllShapes() {
		if (shapes==null)
			return;
		shapes.clear();
	}
	/**
	 * Remove all not zoom shapes.
	 */
	protected void removeNotZoomShapes() {
		if (shapes==null)
			return;
		for (int i=0;i<shapes.size();i++) {
			if (!(((AuxiliaryShape) shapes.get(i)).shape instanceof IZoomRect)) {
				shapes.remove(i);
			}
		}
	}
	/**
	 * @return The number of shapes contained in the pane.
	 */
	protected int countShapes() {
		if (shapes==null)
			return 0;
		return shapes.size();
	}
	/**
	 * @return The shapes.
	 */
	protected ArrayList getShapes() {
		return shapes;
	}
	/**
	 * Sets the shapes.
	 */
	protected void setShapes(ArrayList l) {
		shapes=l;
	}

	void setCoords(String s) {
		if (coordsW==null) {
			coordsW=new JLabel();
			coordsW.setForeground(new Color(212,212,212));
			coordsB=new JLabel();
			coordsB.setForeground(Color.black);
			add(coordsW);
			add(coordsB);
		}
		coordsW.setText(s);
		coordsB.setText(s);
		Dimension dimC=coordsW.getPreferredSize();
		coordsW.setBounds(getWidth()-dimC.width-3,0,dimC.width,dimC.height);
		coordsB.setBounds(getWidth()-dimC.width-2,1,dimC.width,dimC.height);
	}

	void setScale(double s) {
		if (s==oldScale)
			return;
		oldScale=s;
		if (scaleW==null) {
			scaleW=new JLabel();
			Font font=scaleW.getFont().deriveFont(10f);
			scaleW.setFont(font);
			scaleW.setForeground(new Color(212,212,212));
			scaleB=new JLabel();
			scaleB.setFont(font);
			scaleB.setForeground(Color.black);
			add(scaleW);
			add(scaleB);
		}
        s=Math.round(s);
		NumberFormat nf=NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(0);
		scaleW.setText("1 : "+nf.format(s));
		scaleB.setText("1 : "+nf.format(s));
		Dimension pref=scaleW.getPreferredSize();
		scaleW.setBounds((getWidth()-pref.width)/2-3,getHeight()-pref.height-5,pref.width,pref.height);
		scaleW.setBounds((getWidth()-pref.width)/2-2,getHeight()-pref.height-4,pref.width,pref.height);
	}
	private double oldScale=Double.MAX_VALUE;

	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		for (int i=0;i<getComponentCount();i++)
			((Component) getComponents()[i]).setFont(f);
	}

	boolean hasUp() {
		return up!=null;
	}

	boolean hasDown() {
		return down!=null;
	}

	boolean hasLeft() {
		return left!=null;
	}

	boolean hasRight() {
		return right!=null;
	}
	/**
	 * Gets a button for scroll panning. If it has not been created, the method creates it.
	 */
	protected JButton getUp() {
		if (up==null) {
			//Up button
			up=new JButton(mapPane.loadImageIcon("images/uparrows.gif"));
			up.setCursor(mapPane.getCustomCursor("rollupcursor"));
			up.setPressedIcon(mapPane.loadImageIcon("images/uparrowsselect.gif"));
			up.setFocusPainted(false);
			up.setBackground(new Color(255,255,255,96));
			up.setBorder(new CompoundBorder(new LineBorder(SystemColor.control),new LineBorder(Color.white)));
			up.setToolTipText(MapViewer.messagesBundle.getString("scrollpan"));
			up.setAlignmentX(CENTER_ALIGNMENT);
			add(up);
			//These listeners are used for moving to the neighbouring map
			up.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IRegionView rv=mapPane.map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_UP);
					if (rv!=null)
						mapPane.roll(rv,IRegionView.NEIGHBOUR_UP);
				}
			});
		}
		return up;
	}
	/**
	 * Gets a button for scroll panning. If it has not been created, the method creates it.
	 */
	protected JButton getDown() {
		if (down==null) {
			//Down button
			down=new JButton(mapPane.loadImageIcon("images/downarrows.gif"));
			down.setCursor(mapPane.getCustomCursor("rolldowncursor"));
			down.setPressedIcon(mapPane.loadImageIcon("images/downarrowsselect.gif"));
			down.setFocusPainted(false);
			down.setBackground(new Color(255,255,255,96));
			down.setBorder(new CompoundBorder(new LineBorder(SystemColor.control),new LineBorder(Color.white)));
			down.setToolTipText(MapViewer.messagesBundle.getString("scrollpan"));
			down.setAlignmentX(CENTER_ALIGNMENT);
			add(down);
			down.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IRegionView rv=mapPane.map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_DOWN);
					if (rv!=null)
						mapPane.roll(rv,IRegionView.NEIGHBOUR_DOWN);
				}
			});
		}
		return down;
	}
	/**
	 * Gets a button for scroll panning. If it has not been created, the method creates it.
	 */
	protected JButton getLeft() {
		if (left==null) {
			//Left button
			left=new JButton(mapPane.loadImageIcon("images/leftarrows.gif"));
			left.setCursor(mapPane.getCustomCursor("rollleftcursor"));
			left.setPressedIcon(mapPane.loadImageIcon("images/leftarrowsselect.gif"));
			left.setFocusPainted(false);
			left.setBackground(new Color(255,255,255,96));
			left.setBorder(new CompoundBorder(new LineBorder(SystemColor.control),new LineBorder(Color.white)));
			left.setToolTipText(MapViewer.messagesBundle.getString("scrollpan"));
			left.setAlignmentY(CENTER_ALIGNMENT);
			add(left);
			left.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IRegionView rv=mapPane.map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_LEFT);
					if (rv!=null)
						mapPane.roll(rv,IRegionView.NEIGHBOUR_LEFT);
				}
			});
		}
		return left;
	}
	/**
	 * Gets a button for scroll panning. If it has not been created, the method creates it.
	 */
	protected JButton getRight() {
		if (right==null) {
			//Right button
			right=new JButton(mapPane.loadImageIcon("images/rightarrows.gif"));
			right.setCursor(mapPane.getCustomCursor("rollrightcursor"));
			right.setPressedIcon(mapPane.loadImageIcon("images/rightarrowsselect.gif"));
			right.setFocusPainted(false);
			right.setBackground(new Color(255,255,255,96));
			right.setBorder(new CompoundBorder(new LineBorder(SystemColor.control),new LineBorder(Color.white)));
			right.setToolTipText(MapViewer.messagesBundle.getString("scrollpan"));
			right.setAlignmentY(CENTER_ALIGNMENT);
			add(right);
			right.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IRegionView rv=mapPane.map.getActiveRegionView().getNeighbour(IRegionView.NEIGHBOUR_RIGHT);
					if (rv!=null)
						mapPane.roll(rv,IRegionView.NEIGHBOUR_RIGHT);
				}
			});
		}
		return right;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setRenderingHints(mapPane.getRenderingHints());
		if (mapPane.layers==null || mapPane.layers.currentView==null)
			return;
		//If the current view is smaller than the component view, clipping must occur
		/*if (mapPane.layers.currentView.width!=getWidth() && mapPane.layers.currentView.height!=getHeight()) {
			clipRect.x=(int) Math.round(mapPane.getTransform().getTranslateX());
			clipRect.y=(int) Math.round(mapPane.getTransform().getTranslateY());
			clipRect.width=mapPane.layers.currentView.width;
			clipRect.height=mapPane.layers.currentView.height;
			g2.setClip(clipRect);
		}*/
		//Paint the selection shape. Don't draw if outside the visible area.
		if (selectionShape!=null) {
			java.awt.Rectangle r=getVisibleRect();
			mapPane.transformRect(mapPane.getInverseTransform(),r);
			mapPane.transformRect(mapPane.getInversePositionTransform(),r);
			if (selectionShape.getBounds2D().intersects(r)) {
				AffineTransform old=new AffineTransform(g2.getTransform());
				Stroke oldStroke=g2.getStroke();
				g2.transform(mapPane.getTransform());
				g2.transform(mapPane.getPositionTransform());
                float zz=(float)Math.min(Math.abs(mapPane.getPositionTransform().getScaleX()),Math.abs(mapPane.getPositionTransform().getScaleY()));
				if (paintSelectionInterior) {
					g2.setColor(selectFill);
					g2.fill(selectionShape);
				}
				g2.setColor(selectColor1);
				g2.setStroke(new BasicStroke(3/zz));
				g2.draw(selectionShape);
				g2.setColor(selectColor2);
				g2.setStroke(new BasicStroke(1/zz,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,new float[] {9/zz,9/zz},1));
				g2.draw(selectionShape);
				g2.setTransform(old);
				g2.setStroke(oldStroke);
			}
		}
		//Paint the other shapes
		if (shapes!=null) {
			g2.transform(mapPane.getTransform());
			AuxiliaryShape s;
			for (int i=0;i<shapes.size();i++) {
				s=(AuxiliaryShape) shapes.get(i);
				if (s.lv!=null) {
					ArrayList a=new ArrayList();
					a.add(s.shape);
					//It is a geographic object.
					g2.transform(mapPane.getPositionTransform());
					mapPane.loom(g2,s.lv,a);
					g2.transform(mapPane.getInversePositionTransform());
				} else {
					//Don't draw shapes outside the visible area
					if (s.shape.getBounds().intersects(mapPane.layers.currentView)) {
						if (s.getFillColor()!=null && s.getFillColor().getAlpha()>0) {
							g2.setPaint(s.getFillColor());
							g2.fill(s.shape);
						}
						if (s.isBorderPainted() && s.getOutlineColor().getAlpha()>0) {
							g2.setPaint(new Color(0,0,0,150));
							if (s.getStroke() instanceof BasicStroke)
								g2.setStroke(new BasicStroke((((BasicStroke)s.getStroke()).getLineWidth()+2)));
							g2.draw(s.shape);
						}
						if (s.getOutlineColor().getAlpha()>0) {
							g2.setPaint(s.getOutlineColor());
							g2.setStroke(s.getStroke());
							g2.draw(s.shape);
						}
					}
				}
			}

			g2.transform(mapPane.getInverseTransform());
		}
		//g2.setClip(getVisibleRect());
		super.paintComponent(g);
	}

	private ArrayList shapes;
	private JLabel coordsW,coordsB;
	private JLabel scaleW,scaleB;
	private MapPane mapPane;
	private JButton up,down,right,left;
	private SelectionShape selectionShape;
	private Color selectColor1=new Color(192,0,0,208);
	private Color selectColor2=new Color(255,255,255);
	private Color selectFill=new Color(255,255,255,64);
	protected boolean paintSelectionInterior=false;
	//Listener management variables
	ArrayList ml;

	/**
	 * A custom layout manager that is responsible for the layout of the auxiliary pane.
	 * Code taken from Sun but modified to make the glassPane smaller.
	 */
	class AuxiliaryLayout implements LayoutManager2, java.io.Serializable {
		/**
		 * Returns the amount of space the layout would like to have.
		 *
		 * @param parent the Container for which this layout manager is being used
		 * @return a Dimension object containing the layout's preferred size
		 */
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(getWidth(),getHeight());
		}

		/**
		 * Returns the minimum amount of space the layout needs.
		 *
		 * @param parent the Container for which this layout manager is being used
		 * @return a Dimension object containing the layout's minimum size
		 */
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(getWidth(),getHeight());
		}

		/**
		 * Returns the maximum amount of space the layout can use.
		 *
		 * @param target the Container for which this layout manager is being used
		 * @return a Dimension object containing the layout's maximum size
		 */
		public Dimension maximumLayoutSize(Container target) {
			return new Dimension(getWidth(),getHeight());
		}

		/**
		 * Instructs the layout manager to perform the layout for the specified
		 * container.
		 *
		 * @param parent the Container for which this layout manager is being used
		 */
		public void layoutContainer(Container parent) {
			java.awt.Rectangle b=parent.getBounds();
			Insets i=getInsets();
			int contentY = 0;
			int x=b.x;
			int y=b.y;
			int w=b.width-i.right-i.left;
			int h=b.height-i.top-i.bottom;

			if (coordsW!=null) {
				if (coordsW.isVisible()) {
					Dimension pref=coordsW.getPreferredSize();
					coordsW.setBounds(w-pref.width-3,0,pref.width,pref.height);
				} else
					coordsW.setBounds(0,0,0,0);
			}

			if (coordsB!=null) {
				if (coordsB.isVisible()) {
					Dimension pref=coordsB.getPreferredSize();
					coordsB.setBounds(getWidth()-pref.width-2,1,pref.width,pref.height);
				} else
					coordsB.setBounds(0,0,0,0);
			}

			if (scaleW!=null) {
				if (scaleW.isVisible()) {
					Dimension pref=scaleW.getPreferredSize();
					scaleW.setBounds((w-pref.width)/2-3,h-pref.height-5,pref.width,pref.height);
				} else
					scaleW.setBounds(0,0,0,0);
			}

			if (scaleB!=null) {
				if (scaleB.isVisible()) {
					Dimension pref=scaleB.getPreferredSize();
					scaleW.setBounds((w-pref.width)/2-2,h-pref.height-4,pref.width,pref.height);
				} else
					scaleB.setBounds(0,0,0,0);
			}

			if (up!=null) {
				if (up.isVisible()) {
					Dimension d=up.getPreferredSize();
					up.setBounds(x+w/2-d.width/2,1,d.width,d.height);
				} else
					up.setBounds(0,0,0,0);
			}

			if (down!=null) {
				if (down.isVisible()) {
					Dimension d=down.getPreferredSize();
					down.setBounds(x+w/2-d.width/2,y+h-d.height-1,d.width,d.height);
				} else
					down.setBounds(0,0,0,0);
			}

			if (left!=null) {
				if (left.isVisible()) {
					Dimension d=left.getPreferredSize();
					left.setBounds(1,y+h/2-d.height/2,d.width,d.height);
				} else
					left.setBounds(0,0,0,0);
			}

			if (right!=null) {
				if (right.isVisible()) {
					Dimension d=right.getPreferredSize();
					right.setBounds(x+w-d.width-1,y+h/2-d.height/2,d.width,d.height);
				} else
					right.setBounds(0,0,0,0);
			}
		}

		public void addLayoutComponent(String name, Component comp) {}
		public void removeLayoutComponent(Component comp) {}
		public void addLayoutComponent(Component comp, Object constraints) {}
		public float getLayoutAlignmentX(Container target) { return 0.0f; }
		public float getLayoutAlignmentY(Container target) { return 0.0f; }
		public void invalidateLayout(Container target) {}
	}
}
