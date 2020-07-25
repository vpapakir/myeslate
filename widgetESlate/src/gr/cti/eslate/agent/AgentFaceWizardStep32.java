package gr.cti.eslate.agent;

import gr.cti.eslate.imageEditor.ImageEditorDialog;
import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class AgentFaceWizardStep32 extends JPanel {

	AgentFaceWizardStep32(Agent a) {
		try  {
			jbInit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		this.agent=a;

		//Initializations
		scrImages.setViewportView(pnlCircle.table);

		//Localization
		txtDesc.setText(AgentBeanInfo.bundle.getString("step32desc"));
	}

	protected void setSlices(int i) {
		pnlCircle.setSlices(i);
	}

	TableModel getIcons() {
		return pnlCircle.table.getModel();
	}

	private void jbInit() throws Exception {
		this.setLayout(smartLayout1);
		pnlCircle.setOpaque(true);
		txtDesc.setLineWrap(true);
		txtDesc.setWrapStyleWord(true);
		txtDesc.setOpaque(false);
		txtDesc.setText("jTextArea1");
		this.add(pnlCircle, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 3),
					new com.thwt.layout.EdgeAnchor(txtDesc, Anchor.Bottom, Anchor.Below, Anchor.Top, 15),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.45, Anchor.Right, Anchor.Right, 0.0, -3)));
		this.add(txtDesc, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(scrImages, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 3),
					new com.thwt.layout.EdgeAnchor(txtDesc, Anchor.Bottom, Anchor.Below, Anchor.Top, 15),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.45, Anchor.Right, Anchor.Left, 0.0, 3)));
	}

	private Agent agent;
	private SmartLayout smartLayout1 = new SmartLayout();
	private TriCircleJPanel pnlCircle = new TriCircleJPanel();
	private LargeFontJTextArea txtDesc = new LargeFontJTextArea();
	private JScrollPane scrImages = new JScrollPane();

	private class TriCircleJPanel extends JPanel {
		private int slices=16;
		private int active=-1;
		protected MyTable table;
		private ImageEditorDialog ie;
		TriCircleJPanel() {
			super();
			colors[0]=new Color(255,0,0);
			colors[1]=new Color(255,150,0);
			colors[2]=new Color(210,255,0);
			colors[3]=new Color(190,255,0);
			colors[4]=new Color(0,255,0);
			colors[5]=new Color(0,255,240);
			colors[6]=new Color(0,120,255);
			colors[7]=new Color(0,0,255);
			colors[8]=new Color(180,0,255);
			colors[9]=new Color(255,0,180);
			colors[10]=new Color(255,128,128);
			colors[11]=new Color(255,220,128);
			colors[12]=new Color(230,255,128);
			colors[13]=new Color(160,255,128);
			colors[14]=new Color(128,255,170);
			colors[15]=new Color(128,255,255);
			colors[16]=new Color(128,128,255);
			colors[17]=new Color(144,128,255);
			colors[18]=new Color(220,128,255);
			colors[19]=new Color(255,128,220);
			colors[20]=new Color(128,0,0);
			colors[21]=new Color(128,75,0);
			colors[22]=new Color(105,128,0);
			colors[23]=new Color(80,128,0);
			colors[24]=new Color(0,128,0);
			colors[25]=new Color(0,128,120);
			colors[26]=new Color(0,60,128);
			colors[27]=new Color(0,0,128);
			colors[28]=new Color(90,0,128);
			colors[29]=new Color(128,0,90);
			colors[30]=new Color(0,0,0);
			colors[31]=new Color(50,50,50);
			colors[32]=new Color(100,100,100);
			colors[33]=new Color(150,150,150);
			colors[34]=new Color(200,200,200);
			colors[35]=new Color(255,255,255);

			MouseInputListener list=new MouseInputAdapter() {
				//This listener activates the circle and the table on clicking and draging
				public void mousePressed(MouseEvent e) {
					removeMouseListener(this);
					activate(e.getX(),e.getY(),e.getClickCount());
					addMouseListener(this);
				}
				public void mouseDragged(MouseEvent e) {
					activate(e.getX(),e.getY(),e.getClickCount());
				}
				private void activate(int ex,int ey,int clc) {
					//The center of the panel
					int cx=getWidth()/2;
					int cy=getHeight()/2;
					//Migrate the click co-ordinates to the co-ordinate space of the center of the panel
					int x=(ex-cx);
					int y=(ey-cy);
					//Find the angle in degrees
					int ang;
					if (x!=0)
						ang=(int) (180*Math.atan((double) y/(double) x)/Math.PI);
					else
						ang=0;
					//Find the quadrant. Remember: The drawArc method gets angles counter-clockwise.
					if (x<0)
						ang=90-ang;
					else if (x>0)
						ang=270-ang;
					else if (x==0 && y>0)
						ang=180;
					//Find the active slice according the angle
					double angleStep=360d/slices;
					double startAngle=-(angleStep/2);
					active=0;
					for (int i=0;i<slices;i++) {
						if (startAngle<=ang && (startAngle+angleStep)>ang) {
							active=i;
							break;
						}
						startAngle+=angleStep;
					}

					table.setRowSelectionInterval(active,active);

					repaint();

					if (clc==2)
						table.editRow(active);
				}
			};
			addMouseListener(list);
			addMouseMotionListener(list);

			//Table initialization
			table=new MyTable();
			table.setRowHeight(34);
			table.setRowSelectionAllowed(true);
			table.setColumnSelectionAllowed(false);
			table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

			//Icon editor "caching", so that it pops-up immediately. In thread to save time.
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ie=new ImageEditorDialog((JFrame) SwingUtilities.getAncestorOfClass(JFrame.class,AgentFaceWizardStep32.this),32,32);
					ie.setLocation(Math.max(0,getX()+(getWidth()-ie.getWidth())/2),Math.max(0,getY()+(getHeight()-ie.getHeight())/2));
				}
			});
		}
		protected void setSlices(int s) {
			slices=s;
			table.setSize(s);
			//Initialize with the most proper icons from the agent
			double angleStep=360d/slices;
			double angle=0;
			for (int i=0;i<slices;i++) {
				BufferedImage bi;
				Dimension d;
				d=agent.getFaceSize(angle);
				if (d.width>0 && d.height>0) {
					bi=new BufferedImage(d.width,d.height,BufferedImage.TYPE_INT_ARGB);
					agent.paintFace(bi.getGraphics(),angle);
					table.setValueAt(new ImageIcon(bi),i,1);
				}
				angle+=angleStep;
			}
			repaint();
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2=(Graphics2D) g;
			int r=Math.min(getWidth(),getHeight())-12;
			int x=(getWidth()-r)/2;
			int y=(getHeight()-r)/2;
			double angleStep=360d/slices;
			double startAngle=90-(angleStep/2);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			//Draw the slices
			for (int i=0;i<slices;i++) {
				g2.setPaint(colors[i%36]);
				g2.fillArc(x,y,r,r,(int) Math.round(startAngle),(int) Math.round(angleStep));
				startAngle+=angleStep;
			}
			//Draw the circle around
			g2.setStroke(new BasicStroke(2));
			g2.setPaint(Color.black);
			g2.drawOval(x,y,r,r);
			//Paint the active slice
			if (active!=-1) {
				int c;
				startAngle=90-(int) (angleStep/2);
				for (c=0;c<active;c++)
					startAngle+=angleStep;
				g2.setPaint(Color.yellow);
				g2.setStroke(new BasicStroke(3));
				g2.drawArc(x,y,r,r,(int) Math.round(startAngle),(int) Math.round(angleStep));
			}
		}

		private Color[] colors=new Color[36];

		/**
		 * The table holding the image data.
		 */
		private class MyTable extends JTable {
			MyTable() {
				super();
				setModel(new MyTableModel(slices));
				setDefaultRenderer(Color.class,new ColorRenderer());
				setDefaultRenderer(ImageIcon.class,new IconRenderer());
				//Is this a bug of JTable? Although I have defined SINGLE_SELECTION_MODE
				//the selection model returns an interval of selections.
				setSelectionModel(new DefaultListSelectionModel() {
					public void setSelectionInterval(int start,int end) {
						super.setSelectionInterval(end,end);
					}
					public void addSelectionInterval(int start,int end) {
						super.setSelectionInterval(end,end);
					}
				});
				//These two listeners certify that the circle will always show the correct slice
				//no matter if the mouse is pressing, releasing or dragging.
				addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						removeMouseListener(this);
						int r=MyTable.this.rowAtPoint(e.getPoint());
						setRowSelectionInterval(r,r);
						active=r;
						TriCircleJPanel.this.repaint();
						if (getModel().getColumnClass(MyTable.this.columnAtPoint(e.getPoint())).equals(ImageIcon.class))
							editRow(r);
						addMouseListener(this);
					}
					public void mouseReleased(MouseEvent e) {
						int r=MyTable.this.rowAtPoint(e.getPoint());
						setRowSelectionInterval(r,r);
						active=r;
						TriCircleJPanel.this.repaint();
					}
				});
				addMouseMotionListener(new MouseMotionAdapter() {
					public void mouseDragged(MouseEvent e) {
						int r=MyTable.this.rowAtPoint(e.getPoint());
						setRowSelectionInterval(r,r);
						active=r;
						TriCircleJPanel.this.repaint();
					}
				});
			}

			public void setRowSelectionInterval(int start,int end) {
				clearSelection();
				//Scroll the table to show the cell.
				scrollRectToVisible(getCellRect(end,1,true));
				getSelectionModel().setSelectionInterval(end,end);
			}

			protected void editRow(int row) {
				setCursor(Helpers.waitCursor);
				TriCircleJPanel.this.setCursor(Helpers.waitCursor);
				//Check if the IconEditor initializing thread finished
				while (ie==null)
					; //Do nothing until the initializing thread finishes
				if (getModel().getValueAt(row,1)!=null) {
					//try {
						Object icn=getModel().getValueAt(row,1);
					if (icn instanceof Icon)
						ie.setIcon((Icon) icn);
					else
						ie.setIcon(new NewRestorableImageIcon((Image) icn));
					//} catch(gr.cti.eslate.iconEditor.IconTooBigException ex) {}
				} else
					ie.clear();
				ie.setVisible(true);
				if (ie.getReturnCode()==ImageEditorDialog.IMAGE_EDITOR_OK) {
					getModel().setValueAt(ie.getImage(),row,1);
					MyTable.this.repaint();
				}
				setCursor(Helpers.normalCursor);
				TriCircleJPanel.this.setCursor(Helpers.normalCursor);
			}

			protected void setSize(int rows) {
				if (getModel().getRowCount()>rows)
					((MyTableModel) getModel()).deleteRows(rows,getModel().getRowCount()-1);
				else
					((MyTableModel) getModel()).addRows(rows-getModel().getRowCount());
			}

			/**
			 * The table model.
			 */
			private class MyTableModel extends AbstractTableModel {
				private MyTableModel(int rows) {
					super();
					data=new ArrayList();
					for (int i=0;i<rows;i++)
						data.add(null);
					c0Name=AgentBeanInfo.bundle.getString("step3phase");
					c1Name=AgentBeanInfo.bundle.getString("step3icon");
				}

				public int getRowCount(){
					return data.size();
				}

				public int getColumnCount() {
					return 2;
				}

				public Object getValueAt(int row, int column) {
					try {
						if (column==0)
							return colors[row%36];
						else if (column==1)
							return data.get(row);
						else
							return null;
					} catch(Exception e) {
						return null;
					}
				}

				public void setValueAt(Object obj,int row, int column) {
					try {
						if (column==1)
							data.set(row,obj);
					} catch(Exception e) {
						System.err.println("AGENT#200005101618: Cannot set cell value.");
						e.printStackTrace();
					}
				}

				public Class getColumnClass(int c) {
					if (c==0)
						return Color.class;
					else if (c==1)
						return ImageIcon.class;
					else
						return null;
				}

				public String getColumnName(int column) {
					if (column==0)
						return c0Name;
					else if (column==1)
						return c1Name;
					return null;
				}

				public boolean isCellEditable(int row, int col) {
					return false;
				}

				public void addRow() {
					data.add(null);
				}

				public void addRows(int i) {
					for (;i>0;i--)
						data.add(null);
				}

				public void deleteRow(int i) {
					data.remove(i);
				}
				/**
				 * Removes row from s to e inclusive.
				 */
				public void deleteRows(int s,int e) {
					for (;e>=s;)
						data.remove(e--);
					data.trimToSize();
				}

				private String c0Name,c1Name;
				private ArrayList data;
			}

			private class ColorRenderer extends JLabel implements TableCellRenderer {
				ColorRenderer() {
					setOpaque(true);
				}

				public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col) {
					setBackground((Color) table.getValueAt(row,0));
					return this;
				}
			}

			private class IconRenderer extends JLabel implements TableCellRenderer {
				IconRenderer() {
					setOpaque(true);
				}

				public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col) {
					if (isSelected)
						setBackground(Color.yellow);
					else
						setBackground(Color.white);
					Object icn=table.getValueAt(row,1);
					if (icn instanceof Icon)
						setIcon((Icon) icn);
					else
						setIcon(new NewRestorableImageIcon((BufferedImage) icn));
					setHorizontalAlignment(SwingConstants.CENTER);
					return this;
				}
			}
		}
	}
}
