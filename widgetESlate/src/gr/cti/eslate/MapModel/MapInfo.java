package gr.cti.eslate.mapModel;

import gr.cti.eslate.database.engine.DBase;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.utils.ESlateFileDialog;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class MapInfo extends JPanel {

	MapInfo(Map map,MapCreator crtor,JLabel label) {
		this.map=map;
		this.creator=crtor;
		this.label=label;
		modified=false;

		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		//This prevents a null pointer exception from SmartLayout
		setPreferredSize(new Dimension(1,1));

		//Localization
		lblDate.setText(MapCreator.bundleCreator.getString("date"));
		lblAuthor.setText(MapCreator.bundleCreator.getString("author"));
		lblComments.setText(MapCreator.bundleCreator.getString("comments"));
		lblDatabase.setText(MapCreator.bundleCreator.getString("database"));
		lblDatabaseLocation.setText(MapCreator.bundleCreator.getString("mustdefdatabase"));
		btnBrowse.setText(MapCreator.bundleCreator.getString("browse"));
		btnNew.setText(MapCreator.bundleCreator.getString("newdb"));
		lblPrecision.setText(MapCreator.bundleCreator.getString("precision"));
		rbnSingle.setText(MapCreator.bundleCreator.getString("singleprecision"));
		rbnDouble.setText(MapCreator.bundleCreator.getString("doubleprecision"));

		//Name Field
		txtName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					MapInfo.this.map.setName(txtName.getText());
					MapInfo.this.creator.setTitle(txtName.getText()+": "+MapCreator.bundleCreator.getString("title"));
					MapInfo.this.label.setText(txtName.getText());
					MapInfo.this.label.repaint();
				}
			}
		});
		txtName.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				MapInfo.this.map.setName(txtName.getText());
				MapInfo.this.creator.setTitle(txtName.getText()+": "+MapCreator.bundleCreator.getString("title"));
				MapInfo.this.label.setText(txtName.getText());
				MapInfo.this.label.repaint();
			}
		});

		//Date Field
		txtDate.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					MapInfo.this.map.setCreationDate(txtDate.getText());
			}
		});
		txtDate.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				 MapInfo.this.map.setCreationDate(txtDate.getText());
			}
		});

		//Author Field
		txtAuthor.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					MapInfo.this.map.setAuthor(txtAuthor.getText());
			}
		});
		txtAuthor.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				MapInfo.this.map.setAuthor(txtAuthor.getText());
			}
		});

		//Comments Field
		txtComments=new JTextArea();
		txtComments.setLineWrap(true);
		txtComments.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				setModified(true);
			}
		});
		txtComments.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				MapInfo.this.map.setComments(txtComments.getText());
			}
		});
		scrlComments.getViewport().setView(txtComments);
		scrlComments.setBorder(txtName.getBorder());

		//New database Button
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (db!=null) {
					int a=JOptionPane.showConfirmDialog(MapInfo.this,MapCreator.bundleCreator.getString("erasealltables"),"",JOptionPane.YES_NO_OPTION);
					if (a!=JOptionPane.YES_OPTION)
						//The user doesn't want to change the database.
						return;
				}
				String s=JOptionPane.showInputDialog(MapInfo.this,MapCreator.bundleCreator.getString("givedbname"),"",JOptionPane.QUESTION_MESSAGE);
				if ((s!=null) && (!s.equals(""))) {
					try {
						DBase tempdb=new DBase(s);
						lblDatabaseLocation.setText(s);
						lblDatabaseLocation.setForeground(new Color(255,120,0));
						setModified(true);
						db=tempdb;
						tempdb=null;
						MapInfo.this.creator.setDatabase(db);
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(MapInfo.this,MapCreator.bundleCreator.getString("dbnotcreated"),"",JOptionPane.ERROR_MESSAGE);
						System.err.println("MAPCREATOR#MI0001251845: Invalid state! Information message follows. You may continue working.");
						ex.printStackTrace();
					}
				}
			}
		});

		//Browse Button
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MapInfo.this.creator.setCursor(Helpers.waitCursor);
				ESlateFileDialog fd=new ESlateFileDialog(creator,"",FileDialog.LOAD,".cdb");
				fd.setTitle(MapCreator.bundleCreator.getString("database"));
				if (MapCreator.globalDir!=null)
					fd.setDirectory(MapCreator.globalDir);
				fd.setFile("*.cdb");
				fd.show();
				if (fd.getFile()!=null) {
					MapCreator.globalDir=fd.getDirectory();
					try {
						DBase tempDB=DBase.openDBase(creator.map.getESlateHandle(),fd.getDirectory()+fd.getFile(),null,null);
						//Check if the new database has all associated with layer tables compatible
						boolean isNotCompatible=false;
						ArrayList a=creator.getAllLayers();
						for (int i=0;i<a.size() && !isNotCompatible;i++) {
							if (((LayerInfo) a.get(i)).getTable()!=null) {
								String s=((LayerInfo) a.get(i)).getTable().getTitle();
								Table t=tempDB.getTable(s);
								if (t!=null)
									isNotCompatible=isNotCompatible || !((LayerInfo) a.get(i)).checkTable(t);
								else
									isNotCompatible=true;
							}
						}
						if (isNotCompatible) {
							JOptionPane.showMessageDialog(MapInfo.this,MapCreator.bundleCreator.getString("dbnotcompatible"),"",JOptionPane.ERROR_MESSAGE);
						} else {
							db=tempDB;
							lblDatabaseLocation.setText(db.getTitle());
							lblDatabaseLocation.setForeground(new Color(255,120,0));
							MapInfo.this.creator.setDatabase(db);
							setModified(true);
						}
					} catch(Exception ex) {
						System.err.println("MAPCREATOR#MI0001301130: Invalid state! Information message follows. You may continue working.");
						ex.printStackTrace();
					}
				}
				MapInfo.this.creator.setCursor(Helpers.normalCursor);
			}
		});

		//Precision
		ButtonGroup bg=new ButtonGroup();
		bg.add(rbnSingle);
		bg.add(rbnDouble);
		if (map.getDataPrecision()==IMapView.SINGLE_PRECISION)
			rbnSingle.setSelected(true);
		else
			rbnDouble.setSelected(true);
		rbnSingle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (MapInfo.this.map.getDataPrecision()==IMapView.SINGLE_PRECISION)
					return;
				int a=JOptionPane.showConfirmDialog(MapInfo.this,MapCreator.bundleCreator.getString("confirmsingle"),txtName.getText(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (a==JOptionPane.YES_OPTION)
					try {
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						MapInfo.this.map.setDataPrecision(IMapView.SINGLE_PRECISION);
					} catch(Exception ex) {
						ex.printStackTrace();
					} finally {
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				else
					rbnDouble.setSelected(true);
			}
		});
		rbnDouble.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (MapInfo.this.map.getDataPrecision()==IMapView.DOUBLE_PRECISION)
					return;
				int a=JOptionPane.showConfirmDialog(MapInfo.this,MapCreator.bundleCreator.getString("confirmdouble"),txtName.getText(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (a==JOptionPane.YES_OPTION)
					try {
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						MapInfo.this.map.setDataPrecision(IMapView.DOUBLE_PRECISION);
					} catch(Exception ex) {
						ex.printStackTrace();
					} finally {
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				else
					rbnSingle.setSelected(true);
			}
		});

		//Initializations
		if (map.getName()==null) {
			txtName.setText(MapCreator.bundleCreator.getString("newmapname"));
			MapInfo.this.map.setName(txtName.getText());
		} else
			txtName.setText(map.getName());
		if (map.getCreationDate()==null) {
			txtDate.setText((new Date()).toString());
			MapInfo.this.map.setCreationDate(txtDate.getText());
		} else
			txtDate.setText(map.getCreationDate());
		txtAuthor.setText(map.getAuthor());
		txtComments.setText(map.getComments());
		if (map.getDatabase()!=null) {
			db=map.getDatabase();
			lblDatabaseLocation.setForeground(new Color(255,120,0));
			lblDatabaseLocation.setText(db.getTitle());
		}
	}

	private void jbInit() throws Exception {
		txtName.setFont(new java.awt.Font(txtName.getFont().getName(), 1, (int) (txtName.getFont().getSize()*1.5)));
		txtName.setForeground(new java.awt.Color(0, 0, 160));
		modifiedLabel.setFont(new java.awt.Font(modifiedLabel.getFont().getName(), 1, (int) (modifiedLabel.getFont().getSize()*1.6)));
		modifiedLabel.setForeground(Color.gray);
		modifiedLabel.setText("*");
		this.setLayout(smartLayout1);
		Font labelFont=new java.awt.Font(lblDate.getFont().getName(), 1, lblDate.getFont().getSize());
		lblDate.setFont(labelFont);
		lblAuthor.setFont(labelFont);
		lblComments.setFont(labelFont);
		scrlComments.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		lblDatabase.setFont(labelFont);
		lblPrecision.setFont(labelFont);
		this.add(modifiedLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(txtName, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(txtName, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 15),
					new com.thwt.layout.EdgeAnchor(modifiedLabel, Anchor.Left, Anchor.Left, Anchor.Right, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(lblDate, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(txtName, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(txtDate, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(lblDate, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(lblAuthor, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(txtDate, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(txtAuthor, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(lblAuthor, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(lblComments, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(txtAuthor, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(scrlComments, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(lblComments, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(lblPrecision, Anchor.Top, Anchor.Above, Anchor.Bottom, 10)));
		this.add(lblPrecision, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(rbnSingle, Anchor.Top, Anchor.Above, Anchor.Bottom, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(rbnSingle, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(lblDatabase, Anchor.Top, Anchor.Above, Anchor.Bottom, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(rbnDouble, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(lblDatabase, Anchor.Top, Anchor.Above, Anchor.Bottom, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(lblDatabase, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(lblDatabaseLocation, Anchor.Top, Anchor.Above, Anchor.Bottom, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(lblDatabaseLocation, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 10),
					new com.thwt.layout.EdgeAnchor(btnBrowse, Anchor.Left, Anchor.Left, Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(btnBrowse, Anchor.Height, Anchor.Same, Anchor.Height, 0)));
		this.add(btnBrowse, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 10),
					new com.thwt.layout.EdgeAnchor(btnNew, Anchor.Left, Anchor.Left, Anchor.Right, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(btnNew, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
	}

	public DBase getDatabase() {
		return db;
	}

	public String getName() {
		return txtName.getText();
	}

	public String getDate() {
		return txtDate.getText();
	}

	public String getAuthor() {
		return txtAuthor.getText();
	}

	public String getComments() {
		return txtComments.getText();
	}

	public String toString() {
		return txtName.getText();
	}

	public void setModified(boolean value) {
		modified=value;
		if (value)
			modifiedLabel.setForeground(Color.red);
		else
			modifiedLabel.setForeground(Color.gray);
	}

	public boolean isModified() {
		return modified;
	}

	//Local members
	private Map map;
	private MapCreator creator;
	private boolean modified;
	private DBase db;
	private JTextArea txtComments;
	private JLabel label;
	//UI Elements generated by JBuilder
	private SmartLayout smartLayout1 = new SmartLayout();
	private JLabel modifiedLabel = new JLabel();
	private JTextField txtName = new JTextField();
	private JLabel lblDate = new JLabel();
	private JTextField txtDate = new JTextField();
	private JLabel lblAuthor = new JLabel();
	private JTextField txtAuthor = new JTextField();
	private JLabel lblComments = new JLabel();
	private JScrollPane scrlComments = new JScrollPane();
	private JLabel lblDatabase = new JLabel();
	private JLabel lblDatabaseLocation = new JLabel();
	private JButton btnBrowse = new JButton();
	private JLabel lblPrecision=new JLabel();
	private JRadioButton rbnSingle=new JRadioButton();
	private JRadioButton rbnDouble=new JRadioButton();

	JButton btnNew = new JButton();
}
