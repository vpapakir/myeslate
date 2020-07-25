package gr.cti.eslate.mapViewer;

import gr.cti.eslate.eslateToolBar.ESlateToolBar;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.NoBorderButton;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

public class ZoomSlider extends JPanel implements Tool,Externalizable {
	private BorderLayout blayout = new BorderLayout();
	private JButton lblSmall = new JButton() {
		public void updateUI() {
			setOpaque(false);
			setBorder(null);
		}
	};
	PercentJSlider sldZoom=new PercentJSlider();
	private JButton lblBig=new JButton() {
		public void updateUI() {
			setOpaque(false);
			setBorder(null);
		}
	};
	private NoBorderButton lblMore = new NoBorderButton();
	private JPanel pnlHlp = new JPanel();
	MapViewer viewer;
	private boolean ttchanged;
	private Dimension horzDim,vertDim;
	private JPopupMenu popup;
	static final long serialVersionUID=13112000L;

	public ZoomSlider() {
		super();
		ttchanged=false;
		horzDim=new Dimension(100,ToolComponent.SIZE.height);
		vertDim=new Dimension(ToolComponent.SIZE.width,100);
		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		setOpaque(false);
		setSize(horzDim);
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);
		lblSmall.setOpaque(false);
		lblSmall.setBorder(null);
		lblSmall.setRolloverIcon(new ImageIcon(gr.cti.eslate.mapViewer.ZoomSlider.class.getResource("images/lenssmallover.gif")));
		lblSmall.setPressedIcon(lblSmall.getRolloverIcon());
		lblSmall.setRequestFocusEnabled(false);
		lblSmall.setMargin(new Insets(0,0,0,0));
		lblSmall.setPreferredSize(new Dimension(11,11));
		lblSmall.setAlignmentX(CENTER_ALIGNMENT);
		lblSmall.setAlignmentY(CENTER_ALIGNMENT);
		lblSmall.setUI(new BasicButtonUI());
		lblSmall.addMouseListener(new MouseAdapter() {
			ZoomDownThread it;
			//Ensures that the painting will happen once when pressing once.
			//Used to improve the perception of the effect of zooming which
			//happened on mouseReleased.
			int startValue;
			public void mousePressed(MouseEvent e) {
				if (!isEnabled())
					return;
				startValue=sldZoom.getValue();
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				sldZoom.setValue(sldZoom.getValue()-1);
				viewer.getMapPane().layers.viewZoomChanged(true);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				it=new ZoomDownThread();
				it.start();
			}
			public void mouseReleased(MouseEvent e) {
				if (!isEnabled())
					return;
				if (it!=null) {
					it.stopped=true;
					it=null;
				}
				//The user kept the mouse button down for more than one zoom level.
				if (startValue-sldZoom.getValue()>1) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					viewer.getMapPane().layers.viewZoomChanged(true);
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
		lblBig.setOpaque(false);
		lblBig.setBorder(null);
		lblBig.setRolloverIcon(new ImageIcon(gr.cti.eslate.mapViewer.ZoomSlider.class.getResource("images/lensbigover.gif")));
		lblBig.setPressedIcon(lblBig.getRolloverIcon());
		lblBig.setRequestFocusEnabled(false);
		lblBig.setMargin(new Insets(0,0,0,0));
		lblBig.setPreferredSize(new Dimension(18,18));
		lblBig.setAlignmentX(CENTER_ALIGNMENT);
		lblBig.setAlignmentY(CENTER_ALIGNMENT);
		lblBig.setUI(new BasicButtonUI());
		lblBig.addMouseListener(new MouseAdapter() {
			ZoomUpThread it;
			//Ensures that the painting will happen once when pressing once.
			//Used to improve the perception of the effect of zooming which
			//happened on mouseReleased.
			int startValue;
			public void mousePressed(MouseEvent e) {
				if (!isEnabled())
					return;
				startValue=sldZoom.getValue();
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				sldZoom.setValue(sldZoom.getValue()+1);
				viewer.getMapPane().layers.viewZoomChanged(true);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				it=new ZoomUpThread();
				it.start();
			}
			public void mouseReleased(MouseEvent e) {
				if (!isEnabled())
					return;
				if (it!=null) {
					it.stopped=true;
					it=null;
				}
				//The user kept the mouse button down for more than one zoom level.
				if (sldZoom.getValue()-startValue>1) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					viewer.getMapPane().layers.viewZoomChanged(true);
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		sldZoom.setOpaque(false);
		sldZoom.setFont(new Font(sldZoom.getFont().getName(),sldZoom.getFont().getStyle(),(int) (sldZoom.getFont().getSize()*0.85)));
		sldZoom.setMinimum(10);
		sldZoom.setMaximum(1000);
		sldZoom.setValue(100);
		sldZoom.setMinorTickSpacing(1);
		sldZoom.setMajorTickSpacing(10);
		sldZoom.setRequestFocusEnabled(false);
		sldZoom.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				//Null on reading from state
				if (sldZoom!=null)
					sldZoom.repaint();
				if (viewer!=null)
					viewer.getMapPane().setZoom(sldZoom.getValue()/100d);
			}
		});
		//Send the paint vectors event only when the user releases the clutch
		sldZoom.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (!isEnabled())
					return;
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				viewer.getMapPane().layers.viewZoomChanged(true);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		sldZoomKludge();

		//Properties menu creation
		popup=new JPopupMenu();
		popup.setLightWeightPopupEnabled(false);

		JMenuItem val=new JMenuItem(MapViewer.messagesBundle.getString("zoom.value"));
		val.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String s=JOptionPane.showInputDialog(lblMore,MapViewer.messagesBundle.getString("zoom.gvvalue"),"",JOptionPane.QUESTION_MESSAGE);
					setValue(Integer.parseInt(s));
					viewer.getMapPane().layers.viewZoomChanged(true);
				} catch(Throwable t) {}
			}
		});

		JMenuItem hun=new JMenuItem(MapViewer.messagesBundle.getString("zoom.hundred"));
		hun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setValue(100);
				viewer.getMapPane().layers.viewZoomChanged(true);
			}
		});

		JMenuItem fif=new JMenuItem(MapViewer.messagesBundle.getString("zoom.fifty"));
		fif.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setValue(50);
				viewer.getMapPane().layers.viewZoomChanged(true);
			}
		});

		JMenuItem two=new JMenuItem(MapViewer.messagesBundle.getString("zoom.twohundred"));
		two.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setValue(200);
				viewer.getMapPane().layers.viewZoomChanged(true);
			}
		});

		JMenuItem fiv=new JMenuItem(MapViewer.messagesBundle.getString("zoom.fivehundred"));
		fiv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setValue(500);
				viewer.getMapPane().layers.viewZoomChanged(true);
			}
		});

		JMenuItem min=new JMenuItem(MapViewer.messagesBundle.getString("zoom.min"));
		min.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String s=JOptionPane.showInputDialog(lblMore,MapViewer.messagesBundle.getString("zoom.gvmin"),"",JOptionPane.QUESTION_MESSAGE);
					setMinimum(Integer.parseInt(s));
				} catch(Throwable t) {}
			}
		});

		JMenuItem max=new JMenuItem(MapViewer.messagesBundle.getString("zoom.max"));
		max.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String s=JOptionPane.showInputDialog(lblMore,MapViewer.messagesBundle.getString("zoom.gvmax"),"",JOptionPane.QUESTION_MESSAGE);
					setMaximum(Integer.parseInt(s));
				} catch(Throwable t) {}
			}
		});

		popup.add(val);
		popup.add(fif);
		popup.add(hun);
		popup.add(two);
		popup.add(fiv);
		popup.add(new JPopupMenu.Separator());
		popup.add(min);
		popup.add(max);

		lblMore.setOpaque(false);
		lblMore.setMargin(new Insets(0,0,0,0));
		lblMore.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				//If the user releases the mouse outside the button return
				if (!lblMore.contains(e.getX(),e.getY()) || !isEnabled())
					return;
				//The popup has no size when first shown. Trick it.
				if (popup.getWidth()==0)
					popup.show(lblMore,1000000,1000000);
				//Force the popup to always appear inside the screen
				Point p=new Point(lblMore.getWidth()-popup.getWidth(),lblMore.getHeight()+popup.getHeight());
				SwingUtilities.convertPointToScreen(p,lblMore);
				Dimension s=Toolkit.getDefaultToolkit().getScreenSize();
				if (p.x<0) {
					Point p2=new Point(0,0);
					SwingUtilities.convertPointFromScreen(p2,lblMore);
					p.x=p2.x;
				} else
					p.x=lblMore.getWidth()-popup.getWidth();
				if (p.y>s.height) {
					Point p2=new Point(0,s.height-popup.getHeight());
					SwingUtilities.convertPointFromScreen(p2,lblMore);
					p.y=p2.y;
				} else
					p.y=lblMore.getHeight();
				popup.show(lblMore,p.x,p.y);
			}
		});
	}

	public ZoomSlider(String tt,MapViewer viewr) {
		this();

		this.viewer=viewr;
		super.setToolTipText(tt);
		sldZoom.setToolTipText(tt);
	}
	/**
	 * Overriden to show the tooltip in the panel font.
	 */
	public JToolTip createToolTip() {
		JToolTip t = super.createToolTip();
		t.setFont(getFont());
		return t;
	}
	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		for (int i=0;i<getComponentCount();i++)
			((Component) getComponents()[i]).setFont(f);
		if (sldZoom!=null)
			sldZoom.setFont(new Font(f.getName(),f.getStyle(),(int) (f.getSize()*0.85)));
	}

	public Dimension getPreferredSize() {
		return ((sldZoom.getOrientation()==JSlider.HORIZONTAL)?horzDim:vertDim);
	}

	public Dimension getMinimumSize() {
		return ((sldZoom.getOrientation()==JSlider.HORIZONTAL)?horzDim:vertDim);
	}

	public Dimension getMaximumSize() {
		return ((sldZoom.getOrientation()==JSlider.HORIZONTAL)?horzDim:vertDim);
	}

	public void setEnabled(boolean b) {
		super.setEnabled(b);
		lblSmall.setEnabled(b);
		lblBig.setEnabled(b);
		sldZoom.setEnabled(b);
		lblMore.setEnabled(b);
	}

	public void setVisible(boolean b) {
		super.setVisible(b);
		if (viewer!=null) {
			viewer.getToolBar().invalidate();
			viewer.getToolBar().validate();
			viewer.getToolBar().repaint();
		}
	}

	public void setHelpText(String ht) {
	}

	public String getHelpText() {
		return "";
	}

	public boolean isHelpTextChanged() {
		return false;
	}

	public void setToolTipText(String tt) {
		super.setToolTipText(tt);
		sldZoom.setToolTipText(tt);
		ttchanged=true;
	}

	public void setBorderPolicy(int bp) {
	}

	public int getBorderPolicy() {
		return NEVER;
	}


	public void setValue(int value) {
		int old=sldZoom.getValue();
		if (value>sldZoom.getMaximum() && sldZoom.getValue()!=sldZoom.getMaximum())
			sldZoom.setValue(sldZoom.getMaximum());
		else if (value<sldZoom.getMinimum() && sldZoom.getValue()!=sldZoom.getMinimum())
			sldZoom.setValue(sldZoom.getMinimum());
		else if (sldZoom.getValue()!=value)
			sldZoom.setValue(value);
		if (sldZoom.getValue()!=old && viewer.getMapPane()!=null && viewer.getMapPane().layers!=null)
			viewer.getMapPane().layers.viewZoomChanged(true);
	}

	public int getValue() {
		return sldZoom.getValue();
	}

	public void setMaximum(int m) {
		if (m<sldZoom.getMinimum())
			m=sldZoom.getMinimum();
		if (sldZoom.getValue()>m)
			setValue(m);
		sldZoom.setMaximum(m);
		/*try {
			viewer.getMapPane().layers.viewSizeChanged(true,false);
		} catch(NullPointerException e) {/*On startup/}*/
	}

	public int getMaximum() {
		return sldZoom.getMaximum();
	}

	public void setMinimum(int m) {
		if (m>sldZoom.getMaximum())
			m=sldZoom.getMaximum();
		if (sldZoom.getValue()<m)
			setValue(m);
		sldZoom.setMinimum(m);
		/*try {
			viewer.getMapPane().layers.viewSizeChanged(true,false);
		} catch(NullPointerException e) {/*On startup/}*/
	}

	public int getMinimum() {
		return sldZoom.getMinimum();
	}

	public void setOrientation(int o) {
		removeAll();
		pnlHlp.removeAll();
		if (o==ESlateToolBar.VERTICAL) {
			this.add(lblBig,BorderLayout.NORTH);
			this.add(sldZoom,BorderLayout.CENTER);
			this.add(lblSmall,BorderLayout.SOUTH);
			sldZoom.setOrientation(JSlider.HORIZONTAL);
			sldZoom.setOrientation(JSlider.VERTICAL);
			pnlHlp.setLayout(new BoxLayout(pnlHlp,BoxLayout.Y_AXIS));
			pnlHlp.add(lblBig);
			pnlHlp.add(lblMore);
			this.add(pnlHlp,BorderLayout.NORTH);
			setSize(vertDim);
		} else {
			this.add(lblSmall,BorderLayout.WEST);
			this.add(sldZoom,BorderLayout.CENTER);
			this.add(lblBig,BorderLayout.EAST);
			sldZoom.setOrientation(JSlider.VERTICAL);
			sldZoom.setOrientation(JSlider.HORIZONTAL);
			pnlHlp.setLayout(new BoxLayout(pnlHlp,BoxLayout.X_AXIS));
			pnlHlp.add(lblBig);
			pnlHlp.add(lblMore);
			this.add(pnlHlp,BorderLayout.EAST);
			setSize(horzDim);
		}
	}
	/**
	 * Property that shows or hides the small arrow button, which
	 * pops-up the drop down menu.
	 * @param b True shows, false hides.
	 */
	public void setDropDownButtonVisible(boolean b) {
		if (b!=lblMore.isVisible())
			lblMore.setVisible(b);
	}
	/**
	 * Property which tells if the small arrow button, which
	 * pops-up the drop down menu is visible or not.
	 */
	public boolean isDropDownButtonVisible() {
		return lblMore.isVisible();
	}

	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		//Be careful! The actual restore of the state is not happening here!
		//It is happening in Toolbar.readExternal(ObjectInput) .
		StorageStructure ht=(StorageStructure) in.readObject();
		setOpaque(ht.get("opaque",isOpaque()));
		setVisible(ht.get("visible",isVisible()));
		ttchanged=ht.get("ttchanged",false);
		if (ttchanged)
			setToolTipText(ht.get("tooltiptext",getToolTipText()));
		setMinimum(ht.get("minimum",sldZoom.getMinimum()));
		setMaximum(ht.get("maximum",sldZoom.getMaximum()));
		sldZoom.setValue(ht.get("value",100));
		lblMore.setVisible(ht.get("dropdownbuttonvisible",true));
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("opaque",isOpaque());
		ht.put("visible",isVisible());
		ht.put("ttchanged",ttchanged);
		if (ttchanged)
			ht.put("tooltiptext",getToolTipText());
		ht.put("value",sldZoom.getValue());
		ht.put("minimum",sldZoom.getMinimum());
		ht.put("maximum",sldZoom.getMaximum());
		ht.put("dropdownbuttonvisible",lblMore.isVisible());

		out.writeObject(ht);
	}

	private void jbInit() throws Exception {
		this.setLayout(blayout);
		lblSmall.setIcon(new ImageIcon(gr.cti.eslate.mapViewer.ZoomSlider.class.getResource("images/lenssmall.gif")));
		lblBig.setIcon(new ImageIcon(gr.cti.eslate.mapViewer.ZoomSlider.class.getResource("images/lensbig.gif")));
		lblMore.setIcon(new ImageIcon(gr.cti.eslate.mapViewer.ZoomSlider.class.getResource("images/smallextendarrow.gif")));
		pnlHlp.setOpaque(false);
		if (viewer!=null && viewer.getToolBar().getOrientation()==ESlateToolBar.VERTICAL) {
			this.add(lblSmall,BorderLayout.NORTH);
			this.add(sldZoom,BorderLayout.CENTER);
			pnlHlp.setLayout(new BoxLayout(pnlHlp,BoxLayout.Y_AXIS));
			pnlHlp.add(lblBig);
			pnlHlp.add(lblMore);
			this.add(pnlHlp,BorderLayout.SOUTH);
			sldZoom.setOrientation(JSlider.VERTICAL);
		} else {
			this.add(lblSmall,BorderLayout.WEST);
			this.add(sldZoom,BorderLayout.CENTER);
			pnlHlp.setLayout(new BoxLayout(pnlHlp,BoxLayout.X_AXIS));
			pnlHlp.add(lblBig);
			pnlHlp.add(lblMore);
			this.add(pnlHlp,BorderLayout.EAST);
			sldZoom.setOrientation(JSlider.HORIZONTAL);
		}
	}
	/**
	 * Version of a JSlider that paints its value as a percentage in the middle.
	 */
	class PercentJSlider extends JSlider {
		/**
		 * Overriden to show the tooltip in the panel font.
		 */
		public JToolTip createToolTip() {
			JToolTip t = super.createToolTip();
			t.setFont(getFont());
			return t;
		}
		public String getTooltipText() {
			return this.getValue()+"%";
		}
		/*Commented out because we didn't want it to show the percentage on the slider
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			String mes=this.getValue()+"%";
			int mw=getFontMetrics(getFont()).stringWidth(mes);
			int mh=getFontMetrics(getFont()).getAscent();

			g.setColor(new Color(255,255,255,175));
			g.fillRect((getWidth()-mw)/2,(getHeight()-mh)/2,mw,mh);
			g.setColor(Color.black);
			g.drawString(mes,(getWidth()-mw)/2,(getHeight()+mh)/2-2);
		}*/
	};
  /**
   * This method works around a Swing(?) bug, where if a component containing
   * a JSlider is added to E-Slate, only the first JSlider is visible, and no
   * combination of invalidate()/revalidate()/validateTree()/doLayout()
   * seems to fix the problem.
   * The workaround consists of switching the orientation of the slider to the
   * opposite of what it is and back to the original, thus forcing Swing to
   * make it visible.
   */
  private void sldZoomKludge()
  {
	addAncestorListener(new AncestorListener(){
	  public void ancestorAdded(AncestorEvent event)
	  {
		SwingUtilities.invokeLater(new Runnable(){
	  public void run()
	  {
		sldZoom.setOrientation(
		  (sldZoom.getOrientation() == JSlider.HORIZONTAL) ?
			JSlider.VERTICAL : JSlider.HORIZONTAL
		);
		sldZoom.setOrientation(
		  (sldZoom.getOrientation() == JSlider.HORIZONTAL) ?
			JSlider.VERTICAL : JSlider.HORIZONTAL
		);
	  }
	});
	  }
	  public void ancestorRemoved(AncestorEvent event)
	  {
	  }
	  public void ancestorMoved(AncestorEvent event)
	  {
	  }
	});
  }
	private class ZoomUpThread extends Thread {
		protected boolean stopped=false;
		public void run() {
			try {
				sleep(500);
			} catch(InterruptedException e) {}

			while (!stopped) {
				sldZoom.setValue(sldZoom.getValue()+1);
				yield();
				try {
					sleep(10);
				} catch(InterruptedException e) {}
			}
		}
	};

	private class ZoomDownThread extends Thread {
		protected boolean stopped=false;
		public void run() {
			try {
				sleep(500);
			} catch(InterruptedException e) {}

			while (!stopped) {
				sldZoom.setValue(sldZoom.getValue()-1);
				yield();
				try {
					sleep(10);
				} catch(InterruptedException e) {}
			}
		}
	};
}
