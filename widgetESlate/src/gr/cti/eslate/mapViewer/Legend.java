package gr.cti.eslate.mapViewer;

import gr.cti.eslate.protocol.IAgent;
import gr.cti.eslate.protocol.ILayerView;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.StringTokenizer;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


class Legend extends JInternalFrame implements ChangeListener,ActionListener,MouseListener {
	private BorderLayout borderLayout1 = new BorderLayout();
	private JScrollPane scrAll = new JScrollPane();
	private JPanel pnlAll;
	private CategoryPane backgrounds,layers,paths;
	private ButtonGroup bbg = new ButtonGroup();
	private ButtonGroup pbg = new ButtonGroup();
	private boolean firstTimeShown=true;
	private MapPane mapPane;

	//Construct the frame
	Legend(MapPane mapPane) {
		super();
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		this.mapPane=mapPane;
		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		setTitle(MapViewer.messagesBundle.getString("leg.legend"));
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);

		//The glass pane is used to provide a wait cursor when forwaring an event
		//It is made visible and invisible when needed.
		getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		scrAll.setBorder(null);
		scrAll.getVerticalScrollBar().setBlockIncrement(30);
		scrAll.getVerticalScrollBar().setUnitIncrement(15);

		//Path panel
		paths=new CategoryPane(MapViewer.messagesBundle.getString("leg.paths"));
		paths.setLayout(new BoxLayout(paths,BoxLayout.Y_AXIS));

		//Layers panel
		layers=new CategoryPane(MapViewer.messagesBundle.getString("leg.layers"));
		layers.setLayout(new BoxLayout(layers,BoxLayout.Y_AXIS));

		//Background panel
		backgrounds=new CategoryPane(MapViewer.messagesBundle.getString("leg.backgrounds"));
		backgrounds.setLayout(new BoxLayout(backgrounds,BoxLayout.Y_AXIS));

		//The panel that joints everything
		pnlAll=new MyPanel();
		pnlAll.setLayout(new FullWidthLayout());
		pnlAll.add(paths);
		pnlAll.add(Box.createVerticalStrut(4));
		pnlAll.add(layers);
		pnlAll.add(Box.createVerticalStrut(4));
		pnlAll.add(backgrounds);
		pnlAll.setOpaque(false);
		scrAll.getViewport().setView(pnlAll);

		//Add this listener to repaint the contents when both the miniature and the legend are shown
		addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent e) {
				repaint();
				if (Legend.this.mapPane.viewer.miniaturePane!=null)
					Legend.this.mapPane.viewer.miniaturePane.repaint();
			}
		});
	}

	void clearPaths() {
		paths.removeAll();
		invalidate();
		validate();
		repaint();
		pbg=new ExtendedButtonGroup();
	}

	void clearLayers() {
		layers.removeAll();
		invalidate();
		validate();
		repaint();
	}

	void clearBackgrounds() {
		backgrounds.removeAll();
		invalidate();
		validate();
		repaint();
		bbg=new ButtonGroup();
	}

	void addPath(IAgent agent) {
		/*
		PathPanel e=new PathPanel(agent);
		e.setFont(getFont());
		e.cbxVisib.addChangeListener(this);
		e.pnlDrag.addActionListener(this);
		paths.add(e);
		pbg.add(e);
		invalidate();
		validate();
		repaint();
		*/
	}

	void removePath(IAgent agent) {
		/*
		Component[] c=paths.getComponents();
		for (int i=0;i<c.length;i++)
			if (((PathPanel) c[i]).agent==agent) {
				paths.remove(c[i]);
				((PathPanel) c[i]).cbxVisib.removeChangeListener(this);
				((PathPanel) c[i]).pnlDrag.removeActionListener(this);
				pbg.remove((AbstractButton) c[i]);
				break;
			}
		invalidate();
		validate();
		repaint();
		*/
	}

	void addLayer(ILayerView lv) {
		final ElementPanel e=new ElementPanel(lv);
		e.setFont(getFont());
		e.addMouseListener(this);
		e.cbxVisib.addChangeListener(this);
		e.pnlDrag.addActionListener(this);
		layers.add(e);
		if (lv.isObjectSelectable())
			e.setSelected(true);
		invalidate();
		validate();
		repaint();
	}
	/**
	 * Adds a layer inserting it in the proper index position.
	 */
	void addLayer(ILayerView lv,int index) {
		index=mapPane.map.getActiveRegionView().getLayerViews().length-1-index;
		if (index==layers.getComponentCount())
			addLayer(lv);
		else {
			Component[] c=layers.getComponents();
			layers.removeAll();
			for (int i=0;i<index;i++)
				layers.add(c[i]);
			addLayer(lv);
			for (int i=index;i<c.length;i++)
				layers.add(c[i]);
			invalidate();
			validate();
			repaint();
		}
	}
	/**
	 * Swaps two layers in the legend.
	 */
	void swapLayers(int ff,int tt) {
		ff=mapPane.map.getActiveRegionView().getLayerViews().length-1-ff;
		tt=mapPane.map.getActiveRegionView().getLayerViews().length-1-tt;
		int f=Math.min(ff,tt);
		int t=Math.max(ff,tt);
		Component[] c=layers.getComponents();
		layers.removeAll();
		for (int i=0;i<f;i++)
			layers.add(c[i]);
		layers.add(c[t]);
		for (int i=f+1;i<t;i++)
			layers.add(c[i]);
		layers.add(c[f]);
		for (int i=t+1;i<c.length;i++)
			layers.add(c[i]);
		invalidate();
		validate();
		repaint();
	}

	void removeLayer(ILayerView lv) {
		Component[] c=layers.getComponents();
		for (int i=0;i<c.length;i++)
			if (((ElementPanel) c[i]).lv==lv) {
				layers.remove(c[i]);
				((ElementPanel) c[i]).removeMouseListener(this);
				((ElementPanel) c[i]).cbxVisib.removeChangeListener(this);
				((ElementPanel) c[i]).pnlDrag.removeActionListener(this);
				break;
			}
		invalidate();
		validate();
		repaint();
	}

	void rebuildLayerPanel(ILayerView lv) {
		Component[] c=layers.getComponents();
		for (int i=0;i<c.length;i++)
			if (((ElementPanel) c[i]).lv==lv) {
				removeLayer(lv);
				addLayer(lv,c.length-1-i);
				break;
			}
	}

	void layersReordered() {
		clearLayers();
		ILayerView[] lv=mapPane.map.getActiveRegionView().getLayerViews();
		for (int i=lv.length-1;i>-1;i--)
			addLayer(lv[i]);
	}

	BackgroundPanel addBackground(Icon ic,String id) {
		BackgroundPanel e=new BackgroundPanel(ic,id);
		e.setFont(getFont());
		e.addChangeListener(this);
		backgrounds.add(e);
		bbg.add(e);
		invalidate();
		validate();
		repaint();
		return e;
	}

	void removeBackground(String id) {
		Component[] c=backgrounds.getComponents();
		for (int i=0;i<c.length;i++)
			if (((BackgroundPanel) c[i]).id==id) {
				backgrounds.remove(c[i]);
				((BackgroundPanel) c[i]).removeChangeListener(this);
				bbg.remove((AbstractButton) c[i]);
				break;
			}
		invalidate();
		validate();
		repaint();
	}

	void objectSelectabilityChanged(ILayerView lv) {
		Component[] c=layers.getComponents();
		for (int i=0;i<c.length;i++)
			if (((ElementPanel) c[i]).lv==lv) {
				((ElementPanel) c[i]).setSelected(lv.isObjectSelectable());
				break;
			}
		repaint();
	}

	void activateBackground(String id) {
		Component[] c=backgrounds.getComponents();
		for (int i=0;i<c.length;i++)
			if (((BackgroundPanel) c[i]).id==id) {
				((BackgroundPanel) c[i]).setSelected(true);
				break;
			}
	}

	public void setVisible(boolean b) {
		Component p=getParent();
		if (b && firstTimeShown && p!=null) {
			//Initially, give the minimum of the prefered height and its containers height
			setSize(getSize().width,Math.min(p.getHeight(),2+getContentPane().getPreferredSize().height+getBorder().getBorderInsets(this).top+getBorder().getBorderInsets(this).bottom));
			firstTimeShown=false;
			setLocation(0,p.getHeight()-getSize().height);
		}
		//If it is outside the bounds (invisible) bring it inside
		if (p!=null) {
			int x=getX(); int y=getY();
			if (getX()+getWidth()>p.getWidth())
				x=p.getWidth()-getWidth();
			else if (getX()<0)
				x=0;
			if (getY()+getHeight()>p.getHeight())
				y=Math.max(p.getHeight()-getHeight(),0);
			else if (getY()<0)
				y=0;
			if (getX()!=x || getY()!=y)
				setLocation(x,y);
		}

		super.setVisible(b);
	}

	public void setBusy(boolean busy) {
		if (busy)
			getGlassPane().setVisible(true);
		else
			getGlassPane().setVisible(false);
	}

	public void stateChanged(ChangeEvent e) {
		getGlassPane().setVisible(true);
		Component b=(Component) e.getSource();
		Component c=((Component) e.getSource()).getParent();
		if (c instanceof ElementPanel) {
			mapPane.legendLayerVisibilityChanged(((ElementPanel) c).lv,((ElementPanel) c).cbxVisib.isSelected());
		} else if (c instanceof PathPanel) {
			mapPane.legendPathVisibilityChanged(((PathPanel) c).agent,((ElementPanel) c).cbxVisib.isSelected());
		} else if (b instanceof BackgroundPanel) {
			if (((AbstractButton) b).isSelected())
				mapPane.legendBackgroundChanged(((BackgroundPanel) b).id);
		}
		getGlassPane().setVisible(false);
	}

	public void mouseClicked(MouseEvent e) {
//        getGlassPane().setVisible(true);
		if (((AbstractButton) e.getSource()).isSelected())
			(((ElementPanel) e.getSource()).lv).setObjectSelectable(true);
		else
			(((ElementPanel) e.getSource()).lv).setObjectSelectable(false);
//        getGlassPane().setVisible(false);
	}

	public void mouseReleased(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void actionPerformed(ActionEvent e) {
		getGlassPane().setVisible(true);
		Component c=(Component) e.getSource();
		if (c instanceof ElementPanel) {
			StringTokenizer st=new StringTokenizer(e.getActionCommand());
			int[] o=new int[st.countTokens()];
			for (int i=o.length-1;i>-1;i--)
				o[i]=o.length-1-(Integer.valueOf((String) st.nextToken())).intValue();
			mapPane.legendLayersReordered(o);
		} else if (c instanceof PathPanel) {
System.out.println("Paths reordered");
		}
		getGlassPane().setVisible(false);
		repaint();
	}
	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		if (pnlAll!=null)
			pnlAll.setFont(f);
	}

	//Component initialization
	private void jbInit() throws Exception  {
		this.getContentPane().setLayout(borderLayout1);
		this.setClosable(true);
		this.setFrameIcon(Helpers.loadImageIcon("images/mapViewerBeanIcon.gif"));
		this.setIconifiable(false);
		this.setMaximizable(false);
		this.setResizable(true);
		this.setSize(new Dimension(200, 360));
		this.getContentPane().add(scrAll, BorderLayout.CENTER);
	}

	private class MyPanel extends JPanel {
		/**
		 * Known method.
		 */
		public void setFont(Font f) {
			super.setFont(f);
			for (int i=0;i<getComponentCount();i++)
				((Component) getComponents()[i]).setFont(f);
		}
	}
}
