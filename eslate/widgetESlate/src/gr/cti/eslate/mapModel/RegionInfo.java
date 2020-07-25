package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.MotionInfo;
import gr.cti.eslate.utils.ESlateFileDialog;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class RegionInfo extends JPanel {
	RegionInfo(MapNode nde,MapCreator crtor,JTree tree) {
		this.node=nde;
		this.region=node.getRegion();
		if (!node.hasLinks())
			node.newLinkList();
		this.creator=crtor;
		this.tree=tree;
		modified=false;

		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		//This prevents a null pointer exception from SmartLayout
		setPreferredSize(new Dimension(1,1));

		//Localization & Initialization
		if (region.getName()==null)
			name.setText(MapCreator.bundleCreator.getString("newregionname"));
		else
			name.setText(region.getName());
		backLabel.setText(MapCreator.bundleCreator.getString("images"));
		browse.setText(MapCreator.bundleCreator.getString("add"));
		btnDate.setText(MapCreator.bundleCreator.getString("properties"));
		btnDate.setToolTipText(MapCreator.bundleCreator.getString("propertiestip"));
		btnDelete.setText(MapCreator.bundleCreator.getString("delete"));
		btnDefault.setText(MapCreator.bundleCreator.getString("default"));
		btnDefault.setToolTipText(MapCreator.bundleCreator.getString("defaulttip"));
		coordsLabel.setText(MapCreator.bundleCreator.getString("coordinates"));
		longBLLabel.setText(MapCreator.bundleCreator.getString("minlong"));
		latBLLabel.setText(MapCreator.bundleCreator.getString("minlat"));
		longTRLabel.setText(MapCreator.bundleCreator.getString("maxlong"));
		latTRLabel.setText(MapCreator.bundleCreator.getString("maxlat"));
		zoomLabel.setText(MapCreator.bundleCreator.getString("zoomrect"));
		zlongBLLabel.setText(MapCreator.bundleCreator.getString("minlongcart"));
		zlatBLLabel.setText(MapCreator.bundleCreator.getString("minlatcart"));
		zlongTRLabel.setText(MapCreator.bundleCreator.getString("maxlongcart"));
		zlatTRLabel.setText(MapCreator.bundleCreator.getString("maxlatcart"));
		orientationLabel.setText(MapCreator.bundleCreator.getString("orientation"));
		linkedLabel.setText(MapCreator.bundleCreator.getString("linked"));
		degreesLabel.setText(MapCreator.bundleCreator.getString("degrees"));
		scaleLabel.setText(MapCreator.bundleCreator.getString("scale"));
		availableLabel.setText(MapCreator.bundleCreator.getString("available"));
		regionLabel.setText(MapCreator.bundleCreator.getString("maplayers"));
		tbnMotion.setToolTipText(MapCreator.bundleCreator.getString("motion"));
		cmbID.setToolTipText(MapCreator.bundleCreator.getString("idtip"));
		lblCoordType.setText(MapCreator.bundleCreator.getString("coordtype"));
		lblUnits.setText(MapCreator.bundleCreator.getString("units"));
		lblUpm.setText(MapCreator.bundleCreator.getString("unitsPerMeter"));
		btnDelete.setDefaultCapable(false);
		btnDate.setDefaultCapable(false);
		btnDefault.setDefaultCapable(false);
		left.setDefaultCapable(false);
		up.setDefaultCapable(false);
		down.setDefaultCapable(false);
		newLayer.setDefaultCapable(false);
		deleteLayer.setDefaultCapable(false);
		previewLayer.setDefaultCapable(false);
		importLayer.setDefaultCapable(false);
		exportLayer.setDefaultCapable(false);
		browse.setDefaultCapable(false);
		//Initialization
		java.text.NumberFormat nf=java.text.NumberFormat.getNumberInstance(Locale.US);
		Rectangle2D bRect=region.getBoundingRect();
		if (bRect==null) {
			longBL.setText("");
			latBL.setText("");
			longTR.setText("");
			latTR.setText("");
		} else {
			longBL.setText(""+nf.format(bRect.getX()));
			latBL.setText(""+nf.format(bRect.getY()));
			longTR.setText(""+nf.format(bRect.getX()+bRect.getWidth()));
			latTR.setText(""+nf.format(bRect.getY()+bRect.getHeight()));
		}
		Rectangle2D zRect=region.mapNode.getZoomRectangle();
		if (zRect==null || zRect.equals(bRect)) {
			zlongBL.setText("");
			zlatBL.setText("");
			zlongTR.setText("");
			zlatTR.setText("");
		} else {
			zlongBL.setText(""+nf.format(zRect.getX()));
			zlatBL.setText(""+nf.format(zRect.getY()));
			zlongTR.setText(""+nf.format(zRect.getX()+zRect.getWidth()));
			zlatTR.setText(""+nf.format(zRect.getY()+zRect.getHeight()));
		}
		orientation.setText(""+region.getOrientation());
		scale.setText(region.getScale());
		right.setIcon(Helpers.loadImageIcon("images/rgtArrow.gif"));
		left.setIcon(Helpers.loadImageIcon("images/lftArrow.gif"));
		up.setIcon(Helpers.loadImageIcon("images/upArrow.gif"));
		down.setIcon(Helpers.loadImageIcon("images/dnArrow.gif"));
		newLayer.setIcon(Helpers.loadImageIcon("images/newLayer.gif"));
		previewLayer.setIcon(Helpers.loadImageIcon("images/previewLayer.gif"));
		deleteLayer.setIcon(Helpers.loadImageIcon("images/deleteLayer.gif"));
		importLayer.setIcon(Helpers.loadImageIcon("images/importLayer.gif"));
		exportLayer.setIcon(Helpers.loadImageIcon("images/saveLayer.gif"));
		tbnMotion.setIcon(motionIcon);
		right.setEnabled(false);
		left.setEnabled(false);
		up.setEnabled(false);
		down.setEnabled(false);
		btnDate.setEnabled(false);
		btnDelete.setEnabled(false);
		btnDefault.setEnabled(false);
		previewLayer.setEnabled(false);
		deleteLayer.setEnabled(false);
		exportLayer.setEnabled(false);
		tbnMotion.setSelected(false);
		tbnMotion.setEnabled(false);
		lblID.setEnabled(false);
		cmbID.getEditor().setItem("");
		cmbID.setEnabled(false);
		cmbID.addItem("");
		cmbID.addItem(MapCreator.bundleCreator.getString(MotionInfo.MOTION_ROAD_KEY));
		cmbID.addItem(MapCreator.bundleCreator.getString(MotionInfo.MOTION_RAILWAY_KEY));
		cmbID.addItem(MapCreator.bundleCreator.getString(MotionInfo.MOTION_SEA_KEY));
		cmbID.addItem(MapCreator.bundleCreator.getString(MotionInfo.MOTION_AIR_KEY));
		cbxCoords.addItem(MapCreator.bundleCreator.getString("cartesian"));
		cbxCoords.addItem(MapCreator.bundleCreator.getString("lambaphi"));
		cbxUnits.addItem(MapCreator.bundleCreator.getString("unknown"));
		cbxUnits.addItem(MapCreator.bundleCreator.getString("meters"));

		//Coordinates labels alignment
		FontMetrics fm=getFontMetrics((new JLabel()).getFont());
		String[] xymm={"minlong","minlat","maxlong","maxlat"};
		int lgst=-Integer.MAX_VALUE; int lo=-Integer.MAX_VALUE;
		for (int r=0;r<xymm.length;r++) {
			if (lgst<(lo=(fm.stringWidth(MapCreator.bundleCreator.getString(xymm[r])))))
				lgst=lo;
		}
		Dimension dim=new Dimension(lgst,longBL.getPreferredSize().height);
		longBL.setPreferredSize(dim);
		latBL.setPreferredSize(dim);
		longTR.setPreferredSize(dim);
		latTR.setPreferredSize(dim);
		longBL.setMinimumSize(dim);
		latBL.setMinimumSize(dim);
		longTR.setMinimumSize(dim);
		latTR.setMinimumSize(dim);
		longBL.setMaximumSize(dim);
		latBL.setMaximumSize(dim);
		longTR.setMaximumSize(dim);
		latTR.setMaximumSize(dim);
		//Zoom labels alignment
		String[] xymm2={"minlongcart","minlatcart","maxlongcart","maxlatcart"};
		lgst=-Integer.MAX_VALUE; lo=-Integer.MAX_VALUE;
		for (int r=0;r<xymm2.length;r++) {
			if (lgst<(lo=(fm.stringWidth(MapCreator.bundleCreator.getString(xymm2[r])))))
				lgst=lo;
		}
		dim.setSize(lgst,zlongBL.getPreferredSize().height);
		zlongBL.setPreferredSize(dim);
		zlatBL.setPreferredSize(dim);
		zlongTR.setPreferredSize(dim);
		zlatTR.setPreferredSize(dim);
		zlongBL.setMinimumSize(dim);
		zlatBL.setMinimumSize(dim);
		zlongTR.setMinimumSize(dim);
		zlatTR.setMinimumSize(dim);
		zlongBL.setMaximumSize(dim);
		zlatBL.setMaximumSize(dim);
		zlongTR.setMaximumSize(dim);
		zlatTR.setMaximumSize(dim);

		//Background Image List
		images=new JList();
		/*images.setSelectionModel(new DefaultListSelectionModel() {
			//Methods overriden to avoid deselecting from this list!!!
			public void clearSelection() {
			}
			public void removeSelectionInterval(int i0,int i1) {
			}
		});*/
		images.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		images.setToolTipText(MapCreator.bundleCreator.getString("clickonimage"));
		images.setCellRenderer(new ImagesCellRenderer());
		images.setModel(region.getBackgrounds());
		//Care for the bullet on the left of the default background
		if (region.getBackgrounds().getDefaultBackground()!=null)
			region.getBackgrounds().getDefaultBackground().setDefault(true);
		images.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		images.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if ((SwingUtilities.isRightMouseButton(e)) && (images.getSelectedIndex()!=-1)) {
					int i=images.locationToIndex(new Point(e.getX(),e.getY()));
					if (i!=-1) {
						//Show a pop preview
						final JPopupMenu pu=new JPopupMenu();
						pu.add(new JLabel(new ImageIcon(((MapBackground) region.getBackgrounds().getElementAt(i)).getImage())));
						pu.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent e) {
								pu.setVisible(false);
							}
							public void mousePressed(MouseEvent e) {
								pu.setVisible(false);
							}
						});
						pu.show(images,e.getX()-10,e.getY()-10);
					}
				}
			}
		});
		images.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (images.getSelectedValue()!=null) {
					btnDate.setEnabled(true);
					btnDelete.setEnabled(true);
					btnDefault.setEnabled(true);
				} else {
					btnDate.setEnabled(false);
					btnDelete.setEnabled(false);
					btnDefault.setEnabled(false);
				}
			}
		});
		backScroll.getViewport().setView(images);
		backScroll.setBorder(name.getBorder());
		if (region.getBackgrounds().getDefaultBackground()!=null)
			images.setSelectedValue(region.getBackgrounds().getDefaultBackground(),true);

		//All Layers List
		allLayers=new JList();
		allLayers.setCellRenderer(new LayerInfoCellRenderer());
		allLayers.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (allLayers.getSelectedIndices().length!=0) {
					right.setEnabled(true);
					deleteLayer.setEnabled(true);
					previewLayer.setEnabled(true);
					exportLayer.setEnabled(true);
				} else {
					right.setEnabled(false);
					deleteLayer.setEnabled(false);
					previewLayer.setEnabled(false);
					if (layers.getSelectedIndices().length!=0)
						exportLayer.setEnabled(true);
					else
						exportLayer.setEnabled(false);
				}
			}
		});
		allLayers.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2) {
					int[] indices=allLayers.getSelectedIndices();
					for (int i=0;i<indices.length;i++) {
						((LayerInfo) allLayersModel.elementAt(indices[i])).setVisible(true);
					}
				}
			}
		});
		allLayersScroll.getViewport().setView(allLayers);
		allLayersScroll.setBorder(name.getBorder());

		//Region Layers List
		layers=new JList();
		layers.setCellRenderer(new LayerInfoCellRenderer2());
		layers.addListSelectionListener(new ListSelectionListener() {
			private void clearMotion() {
				tbnMotion.setSelected(false);
				tbnMotion.setEnabled(false);
				lblID.setEnabled(false);
				cmbID.getEditor().setItem("");
				cmbID.setEnabled(false);
			}
			public void valueChanged(ListSelectionEvent e) {
				if (layers.getSelectedIndices().length!=0) {
					exportLayer.setEnabled(true);
					left.setEnabled(true);
				} else {
					if (allLayers.getSelectedIndices().length!=0)
						exportLayer.setEnabled(true);
					else
						exportLayer.setEnabled(false);
					left.setEnabled(false);
				}
				if (layers.getSelectedIndices().length==1) {
					up.setEnabled(true);
					down.setEnabled(true);
					//Enable or disable the motion-related widgets.
					//Note that a PointLayer cannot currently be a motion layer.
					tbnMotion.setEnabled(true);
					lblID.setEnabled(true);
					cmbID.setEnabled(true);
					if (((LayerInfo) layers.getSelectedValue()).getLayer()!=null) {
						if (((LayerInfo) layers.getSelectedValue()).getLayer() instanceof PointLayer)
							clearMotion();
						else if (((LayerInfo) layers.getSelectedValue()).getLayer().isMotionLayer()) {
							tbnMotion.setSelected(true);
							cmbID.getEditor().setItem(Helpers.getHumanReadableMotionKey(((LayerInfo) layers.getSelectedValue()).getLayer().getMotionID()));
						} else {
							tbnMotion.setSelected(false);
							cmbID.getEditor().setItem("");
						}
					} else
						clearMotion();
				} else {
					up.setEnabled(false);
					down.setEnabled(false);
					clearMotion();
				}
			}
		});
		layers.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2) {
					int[] indices=layers.getSelectedIndices();
					for (int i=0;i<indices.length;i++) {
						((LayerInfo) layersModel.elementAt(indices[i])).setVisible(true);
					}
				}
			}
		});
		layersScroll.getViewport().setView(layers);
		layersScroll.setBorder(name.getBorder());

		//Name Field
		name.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					RegionInfo.this.region.setName(name.getText());
					RegionInfo.this.tree.repaint();
				}
			}
		});
		name.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				RegionInfo.this.region.setName(name.getText());
				RegionInfo.this.tree.repaint();
			}
		});

		//Min Long Field
		longBL.setValidChars("0123456789.-");
		longBL.setHorizontalAlignment(JNumberField.RIGHT);
		longBL.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					produceBoundingRect();
			}
		});
		longBL.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				produceBoundingRect();
			}
		});

		//Min Lat Field
		latBL.setValidChars("0123456789.-");
		latBL.setHorizontalAlignment(JNumberField.RIGHT);
		latBL.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					produceBoundingRect();
			}
		});
		latBL.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				produceBoundingRect();
			}
		});

		//Max Long Field
		longTR.setValidChars("0123456789.-");
		longTR.setHorizontalAlignment(JNumberField.RIGHT);
		longTR.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					produceBoundingRect();
			}
		});
		longTR.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				produceBoundingRect();
			}
		});

		//Max Lat Field
		latTR.setValidChars("0123456789.-");
		latTR.setHorizontalAlignment(JNumberField.RIGHT);
		latTR.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					produceBoundingRect();
			}
		});
		latTR.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				produceBoundingRect();
			}
		});

		//Min Long zoom Field
		zlongBL.setValidChars("0123456789.-");
		zlongBL.setHorizontalAlignment(JNumberField.RIGHT);
		zlongBL.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					produceZoomRect();
			}
		});
		zlongBL.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				produceZoomRect();
			}
		});

		//Min Lat zoom Field
		zlatBL.setValidChars("0123456789.-");
		zlatBL.setHorizontalAlignment(JNumberField.RIGHT);
		zlatBL.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					produceZoomRect();
			}
		});
		zlatBL.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				produceZoomRect();
			}
		});

		//Max Long zoom Field
		zlongTR.setValidChars("0123456789.-");
		zlongTR.setHorizontalAlignment(JNumberField.RIGHT);
		zlongTR.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					produceZoomRect();
			}
		});
		zlongTR.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				produceZoomRect();
			}
		});

		//Max Lat zoom Field
		zlatTR.setValidChars("0123456789.-");
		zlatTR.setHorizontalAlignment(JNumberField.RIGHT);
		zlatTR.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					produceZoomRect();
			}
		});
		zlatTR.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				produceZoomRect();
			}
		});


		//Orientation Field
		orientation.setHorizontalAlignment(JNumberField.RIGHT);
		orientation.setValidChars("0123456789");
		orientation.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (orientation.getInt()>360) {
					orientation.setText("0");
					Toolkit.getDefaultToolkit().beep();
				} else if (orientation.getInt()==0) {
					orientation.setText("0");
				} else
					orientation.setText(truncZeroes(orientation.getText()));
				setModified(true);
				try {
					RegionInfo.this.region.setOrientation(new Integer(orientation.getText()).intValue());
				} catch(NumberFormatException nfe) {
					RegionInfo.this.region.setOrientation(0);
					Toolkit.getDefaultToolkit().beep();
				}
			}

		});

		//Linked
		linked.setEditable(false);
		//Rebuilds the list when the component is shown
		addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent e) {
				constructLinkList();
			}
			public void ancestorMoved(AncestorEvent e) {}
			public void ancestorRemoved(AncestorEvent e) {}
		});
		linkedItemListener=new ItemListener() {
			private Object deselected=null;
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.DESELECTED) {
					deselected=e.getItem();
					return;
				} else {
					int yn=JOptionPane.showConfirmDialog(RegionInfo.this,MapCreator.bundleCreator.getString("confirmlink"),"",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if (yn==JOptionPane.NO_OPTION) {
						linked.removeItemListener(linkedItemListener);
						linked.setSelectedItem(deselected);
						linked.addItemListener(linkedItemListener);
						return;
					}
				}
				deselected=null;
				MapNode sel;
				try {
					sel=(MapNode) linked.getSelectedItem();
				} catch(ClassCastException ex) {
					sel=null;
				}
				if (node.getLinkedTo()!=sel) {
					//Clear from destination
					if (node.getLinkedTo()!=null)
						node.getLinkedTo().removeLink(node);
					//Add to new destination
					if (sel!=null) {
						//Redirect the linked to this region regions
						//Add the region to avoid code duplication
						node.addLink(node);
						for (int rl=node.getNumberOfLinks()-1;rl>-1;rl--) {
							RegionInfo ri=(RegionInfo) creator.hashMap.get(node.getLink(rl));
							Region regEdit=ri.region;
							ri.setLayerWidgetsEnabled(false);
							sel.addLink(regEdit.mapNode);
							//Add layers to allLayers list
							for (int i=0;i<ri.layersModel.size();i++)
								ri.allLayersModel.addElement(ri.layersModel.getElementAt(i));
							//Remove layers from layers list
							for (int i=ri.layersModel.size()-1;i>-1;i--)
								ri.layersModel.removeElementAt(i);
							//Postpone sending to many layer events!
							regEdit.postponeLayerAddRemEvent=true;
							//Remove layers from region
							for (int i=regEdit.getLayers().length-1;i>-1;i--)
								regEdit.removeLayer(regEdit.getLayers()[0]);
							//Add new layers to region
							for (int i=0;i<sel.getRegion().getLayers().length;i++)
								regEdit.addLayer(sel.getRegion().getLayers()[i]);
							regEdit.postponeLayerAddRemEvent=false;
							regEdit.setLink(sel);
							node.removeLink(regEdit.mapNode);
						}
					} else {
						setLayerWidgetsEnabled(true);
						node.getLinkedTo().removeLink(region.mapNode);
						node.setLinkedTo(null);
					}
				}
			}
		};

		//Scale Field
		scale.setValidChars("0123456789");
		scale.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (scale.getText().length()>10) {
					scale.setText(scale.getText().substring(0,10));
					Toolkit.getDefaultToolkit().beep();
				}
				setModified(true);
				try {
					RegionInfo.this.region.setScale(new Long(scale.getText()).longValue());
				} catch(NumberFormatException nfe) {
					RegionInfo.this.region.setScale(0);
					Toolkit.getDefaultToolkit().beep();
				}
			}
		});

		//Browse Button
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (creator.map.getOpenAccess()==null && creator.map.getSaveAccess()==null) {
					JOptionPane.showMessageDialog(RegionInfo.this,MapCreator.bundleCreator.getString("cannotaddback"),"",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (confirmSaveBack) {
					//In E-Slate II disable asking, because it is confusing
					int a=JOptionPane.YES_OPTION;
//					int a=JOptionPane.showConfirmDialog(RegionInfo.this,Map.bundleMessages.getString("backgroundio"),"",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if (a==JOptionPane.NO_OPTION)
						return;
					confirmSaveBack=false;
				}
				ESlateFileDialog fd=new ESlateFileDialog(creator,"",ESlateFileDialog.LOAD,new String[] {".jpg",".gif"});
				fd.setTitle(MapCreator.bundleCreator.getString("images"));
				if (MapCreator.globalDir!=null)
					fd.setDirectory(MapCreator.globalDir);
				fd.setFile("*.jpg;*.gif");
				fd.show();
				if (fd.getFile()!=null) {
					MapCreator.globalDir=fd.getDirectory();
					try {
						MapBackground mb=new MapBackground(fd.getDirectory()+fd.getFile());
						String mbn=JOptionPane.showInputDialog(RegionInfo.this,MapCreator.bundleCreator.getString("backname"));
						if (mbn==null || mbn.equals("")) {
							JOptionPane.showMessageDialog(RegionInfo.this,MapCreator.bundleCreator.getString("nobackname"),"",JOptionPane.ERROR_MESSAGE);
							return;
						}
						mb.setFilename(mbn);
						if (region.getBackgrounds().getDefaultBackground()==null)
							mb.setDefault(true);
						region.addBackground(mb);
						//Show the new background
						images.setSelectedValue(mb,true);
						btnDate.setEnabled(true);
						btnDelete.setEnabled(true);
						btnDefault.setEnabled(true);
						setModified(true);
					} catch(Exception ex) {
						System.err.println("MAPCREATOR#RI0001121930: Invalid state! Information message follows. You may continue working.");
						ex.printStackTrace();
					}
				}
			}
		});

		//Delete Button
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (confirmSaveBack) {
					//In E-Slate II disable asking, because it is confusing
					int a=JOptionPane.YES_OPTION;
//					int a=JOptionPane.showConfirmDialog(RegionInfo.this,Map.bundleMessages.getString("backgroundio"),"",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if (a==JOptionPane.NO_OPTION)
						return;
					confirmSaveBack=false;
				}
				region.removeBackground((MapBackground) images.getSelectedValue());
				images.clearSelection();
				images.repaint();
				btnDelete.setEnabled(false);
				btnDate.setEnabled(false);
				btnDefault.setEnabled(false);
			}
		});

		//Date Button
		btnDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TimeEra dd=new TimeEra((MapBackground) images.getSelectedValue());
				Point p=new Point(getX()+(getWidth()-dd.getWidth())/2,getY()+(getHeight()-dd.getHeight())/2);
				SwingUtilities.convertPointToScreen(p,getParent());
				dd.setLocation(p.x,p.y);
				dd.setVisible(true);
				if (dd.returnCode==dd.ALL_DATES) {
					((MapBackground) images.getSelectedValue()).setDateFrom(null);
					((MapBackground) images.getSelectedValue()).setDateTo(null);
					((MapBackground) images.getSelectedValue()).setFilename(dd.name());
				} else if (dd.returnCode==dd.SPECIFIC) {
					((MapBackground) images.getSelectedValue()).setDateFrom(dd.from);
					((MapBackground) images.getSelectedValue()).setDateTo(dd.to);
					((MapBackground) images.getSelectedValue()).setDateFormatPattern(dd.dateFormatPattern);
					((MapBackground) images.getSelectedValue()).setFilename(dd.name());
				}
				images.repaint();
			}
		});

		//Default button
		btnDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (region.getBackgrounds().getDefaultBackground()!=null)
					region.getBackgrounds().getDefaultBackground().setDefault(false);
				region.setDefaultBackground((MapBackground) images.getSelectedValue());
				((MapBackground) images.getSelectedValue()).setDefault(true);
				images.repaint();
			}
		});

		//Right Button (Add Layer)
		right.setToolTipText(MapCreator.bundleCreator.getString("addlayertip"));
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Helpers.waitCursor);
				setModified(true);
				int[] indices=allLayers.getSelectedIndices();
				allLayers.clearSelection();
				//Add layers to "layers"
				for (int i=0;i<indices.length;i++) {
					layersModel.add(0,allLayersModel.getElementAt(indices[i]));
					region.addLayer(((LayerInfo) allLayersModel.getElementAt(indices[i])).getLayer());
				}
				//Remove layers form "allLayers:
				for (int i=indices.length-1;i>-1;i--)
					allLayersModel.removeElementAt(indices[i]);
				setCursor(Helpers.normalCursor);
			}
		});

		//Left Button (Remove Layer)
		left.setToolTipText(MapCreator.bundleCreator.getString("removelayertip"));
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Helpers.waitCursor);
				setModified(true);
				int[] indices=layers.getSelectedIndices();
				LayerInfo l;
				layers.clearSelection();
				//Add layers to "layers"
				for (int i=0;i<indices.length;i++) {
					l=(LayerInfo) layersModel.getElementAt(indices[i]);
					allLayersModel.addElement(l);
					region.removeLayer(l.getLayer());
				}
				//Remove layers form layers:
				for (int i=indices.length-1;i>-1;i--)
					layersModel.removeElementAt(indices[i]);
				setCursor(Helpers.normalCursor);
			}
		});

		//Up Button
		up.setToolTipText(MapCreator.bundleCreator.getString("layeruptip"));
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Helpers.waitCursor);
				setModified(true);
				int po=layers.getSelectedIndex();
				if (po!=0) {
					//Reverse the numbers because the map view is opposite to the list box.
					region.swapLayers((layersModel.size()-1)-layers.getSelectedIndex(),(layersModel.size()-1)-layers.getSelectedIndex()+1);
					Object obj1=layersModel.getElementAt(po-1);
					Object obj2=layersModel.getElementAt(po);
					layersModel.setElementAt(obj1,po);
					layersModel.setElementAt(obj2,po-1);
					layers.setSelectedIndex(po-1);
				} else
					Toolkit.getDefaultToolkit().beep();
				setCursor(Helpers.normalCursor);
			}
		});

		//Down Button
		down.setToolTipText(MapCreator.bundleCreator.getString("layerdowntip"));
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Helpers.waitCursor);
				setModified(true);
				int po=layers.getSelectedIndex();
				if (po!=layersModel.size()-1) {
					//Reverse the numbers because the map view is oposite to the list box.
					region.swapLayers((layersModel.size()-1)-layers.getSelectedIndex(),(layersModel.size()-1)-layers.getSelectedIndex()-1);
					Object obj1=layersModel.getElementAt(po);
					Object obj2=layersModel.getElementAt(po+1);
					layersModel.setElementAt(obj1,po+1);
					layersModel.setElementAt(obj2,po);
					layers.setSelectedIndex(po+1);
				} else
					Toolkit.getDefaultToolkit().beep();
				setCursor(Helpers.normalCursor);
			}
		});

		//New Layer Button
		newLayer.setToolTipText(MapCreator.bundleCreator.getString("newlayertip"));
		newLayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
				LayerInfo li=new LayerInfo(null,RegionInfo.this.creator,true);
				Dimension s=Toolkit.getDefaultToolkit().getScreenSize();
				li.setLocation((s.width-li.getWidth())/2,(s.height-li.getHeight())/2);
				li.setVisible(true);
			}
		});

		//Preview Layer Button
		previewLayer.setToolTipText(MapCreator.bundleCreator.getString("previewlayertip"));
		previewLayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] indices=allLayers.getSelectedIndices();
				for (int i=indices.length-1;i>-1;i--) {
					((LayerInfo) allLayersModel.elementAt(indices[i])).setVisible(true);
				}
			}
		});

		//Delete Layer Button
		deleteLayer.setToolTipText(MapCreator.bundleCreator.getString("deletelayertip"));
		deleteLayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(RegionInfo.this,MapCreator.bundleCreator.getString("deletelayers"),"",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
					setModified(true);
					int[] indices=allLayers.getSelectedIndices();
					LayerInfo[] removed=new LayerInfo[indices.length];
					for (int i=0;i<indices.length;i++)
						removed[i]=(LayerInfo) allLayersModel.elementAt(indices[i]);
					creator.propagateLayerRemoval(removed);
					exportLayer.setEnabled(false);
					deleteLayer.setEnabled(false);
					previewLayer.setEnabled(false);
				}
			}
		});

		//Import Layer Button
		importLayer.setToolTipText(MapCreator.bundleCreator.getString("importlayertip"));
		importLayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				ESlateFileDialog fd=new ESlateFileDialog(creator,MapCreator.bundleCreator.getString("importlayer"),ESlateFileDialog.LOAD,".lyr");
				if (MapCreator.globalDir!=null)
					fd.setDirectory(MapCreator.globalDir);
				fd.setFile("*.lyr");
				fd.show();
				if (fd.getFile()!=null) {
					MapCreator.globalDir=fd.getDirectory();
					try {
						ObjectInputStream in=new ObjectInputStream(new FileInputStream(fd.getDirectory()+fd.getFile()));
						//Read the layer
						importLayerMthd(Layer.importLayer(in),true);
						setModified(true);
					} catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(RegionInfo.this,MapCreator.bundleCreator.getString("cannotloadfile"),MapCreator.bundleCreator.getString("importlayer"),JOptionPane.ERROR_MESSAGE);
					}
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		//Export Layer Button
		exportLayer.setToolTipText(MapCreator.bundleCreator.getString("exportlayertip"));
		exportLayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				int[] indices=allLayers.getSelectedIndices();
				for (int i=indices.length-1;i>-1;i--)
					export((LayerInfo) allLayersModel.elementAt(indices[i]));
				indices=layers.getSelectedIndices();
				for (int i=indices.length-1;i>-1;i--)
					export((LayerInfo) layersModel.elementAt(indices[i]));
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			private void export(LayerInfo li) {
				ESlateFileDialog fd=new ESlateFileDialog(creator,MapCreator.bundleCreator.getString("exportlayer")+li.getName(),ESlateFileDialog.SAVE,".lyr");
				if (MapCreator.globalDir!=null)
					fd.setDirectory(MapCreator.globalDir);
				fd.setFile(li.getName()+".lyr");
				fd.show();
				if (fd.getFile()!=null) {
					MapCreator.globalDir=fd.getDirectory();
					try {
						ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(fd.getDirectory()+fd.getFile()));
						li.getLayer().exportLayer(out);
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(RegionInfo.this,MapCreator.bundleCreator.getString("cannotcreatefile"),MapCreator.bundleCreator.getString("exportlayer")+li.getName(),JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		//Motion Layer button
		tbnMotion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tbnMotion.isSelected())
					((LayerInfo) layers.getSelectedValue()).getLayer().setMotionID((String) cmbID.getEditor().getItem());
				else
					((LayerInfo) layers.getSelectedValue()).getLayer().setMotionID(null);
				layers.repaint();
			}
		});

		//Motion Layer ID combo
		cmbID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tbnMotion.isSelected()) {
					((LayerInfo) layers.getSelectedValue()).getLayer().setMotionID((String) cmbID.getEditor().getItem());
					layers.repaint();
				}
			}
		});

		//Coords type
		cbxCoords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbxCoords.getSelectedItem().equals(MapCreator.bundleCreator.getString("cartesian"))) {
					lblUnits.setEnabled(true);
					cbxUnits.setEnabled(true);
					lblUpm.setEnabled(true);
					txtUpp.setEnabled(true);
					region.setCoordinateSystem(IRegionView.COORDINATE_CARTESIAN);
					longBLLabel.setText(MapCreator.bundleCreator.getString("minlongcart"));
					latBLLabel.setText(MapCreator.bundleCreator.getString("minlatcart"));
					longTRLabel.setText(MapCreator.bundleCreator.getString("maxlongcart"));
					latTRLabel.setText(MapCreator.bundleCreator.getString("maxlatcart"));
				} else {
					lblUnits.setEnabled(false);
					cbxUnits.setEnabled(false);
					lblUpm.setEnabled(false);
					txtUpp.setEnabled(false);
					region.setCoordinateSystem(IRegionView.COORDINATE_TERRESTRIAL);
					longBLLabel.setText(MapCreator.bundleCreator.getString("minlong"));
					latBLLabel.setText(MapCreator.bundleCreator.getString("minlat"));
					longTRLabel.setText(MapCreator.bundleCreator.getString("maxlong"));
					latTRLabel.setText(MapCreator.bundleCreator.getString("maxlat"));
				}
				setModified(true);
			}
		});
		switch (region.getCoordinateSystem()) {
			case IRegionView.COORDINATE_CARTESIAN:
				cbxCoords.setSelectedIndex(0);
				break;
			case IRegionView.COORDINATE_TERRESTRIAL:
				cbxCoords.setSelectedIndex(1);
				break;
		}

		//Units
		cbxUnits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbxUnits.getSelectedItem().equals(MapCreator.bundleCreator.getString("meters"))) {
					txtUpp.setText("1");
					region.setUnitsPerMeter(1);
				}
				setModified(true);
			}
		});
		if (region.getUnitsPerMeter()==1)
			cbxUnits.setSelectedIndex(1);
		else
			cbxUnits.setSelectedIndex(0);

		//Units Per Meter field
		txtUpp.setValidChars("0123456789.");
		txtUpp.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (txtUpp.getText().length()>7) {
					txtUpp.setText(txtUpp.getText().substring(0,7));
					Toolkit.getDefaultToolkit().beep();
				}
				if (txtUpp.getdouble()==1)
					cbxUnits.setSelectedIndex(1);
				else
					cbxUnits.setSelectedIndex(0);
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
					region.setUnitsPerMeter(txtUpp.getdouble());
				setModified(true);
			}
		});
		txtUpp.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				region.setUnitsPerMeter(txtUpp.getdouble());
			}
		});
		txtUpp.setText(""+region.getUnitsPerMeter());
		scale.setVisible(false);
		scaleLabel.setVisible(false);

		//Read layers
		allLayersModel=new DefaultListModel();
		layersModel=new DefaultListModel();

		ArrayList li=creator.getAllLayers();
		//Populate the layers model.
		if (node.getLinkedTo()==null) {
			Layer l[]=region.getLayers();
			for (int j=0;j<l.length;j++) {
				for (int i=0;i<li.size();i++) {
					if (l[j].equals(((LayerInfo) li.get(i)).getLayer())) {
						layersModel.add(0,li.get(i));
						break;
					}
				}
			}
		}
		//Populate the allLayers model.
		for (int i=0;i<li.size();i++)
			if (!layersModel.contains(li.get(i)))
				allLayersModel.addElement(li.get(i));
		allLayers.setModel(allLayersModel);
		layers.setModel(layersModel);
	}

	private void setLayerWidgetsEnabled(boolean b) {
		up.setEnabled(b);
		down.setEnabled(b);
		left.setEnabled(b);
		right.setEnabled(b);
		allLayers.setEnabled(b);
		if (!b) {
			allLayers.setBackground(new Color(198,255,198));
			layers.setBackground(allLayers.getBackground());
			layers.repaint();
		} else {
			allLayers.setBackground(images.getBackground());
			layers.setBackground(allLayers.getBackground());
			layers.repaint();
		}
		layers.setEnabled(b);
	}
	/**
	 * Rebuilds the link list.
	 */
	void constructLinkList() {
		linked.removeItemListener(linkedItemListener);
		linked.removeAllItems();
		linked.addItem(MapCreator.bundleCreator.getString("notlinked"));
		Iterator it=creator.hashMap.keySet().iterator();
		Object select=null;
		while (it.hasNext()) {
			MapNode o=(MapNode) it.next();
			//Don't put already linked regions to the list, their destination is put
			if (o!=region.mapNode && o.getLinkedTo()==null)
				linked.addItem(o);
			if (o==region.mapNode.getLinkedTo())
				select=o;
		}
		if (select==null) {
			linked.setSelectedIndex(0);
			setLayerWidgetsEnabled(true);
		} else {
			linked.setSelectedItem(select);
			setLayerWidgetsEnabled(false);
		}
		linked.addItemListener(linkedItemListener);
	}
	/**
	 * Imports a layer in the creator. Returns false if the user chose not to import its table.
	 * The method tries to import the table as well when <code>importTable</code> parameter is true.
	 * Throws Exception to indicate that the user blocked (by not creating a db) layer input.
	 */
	boolean importLayerMthd(Layer lr,boolean importTable) throws Exception {
		boolean retval=true;
		ArrayList al=creator.getAllLayers();
		//Check all existing layers
		LayerInfo li=null;
		int sel=0; //Initialize to "Never mind"
		for (int i=0;i<al.size() && li==null;i++) {
			//If one with the same name exists, ask to replace.
			li=(LayerInfo) al.get(i);
			if (li.getName().equals(lr.getName())) {
				Object[] options={MapCreator.bundleCreator.getString("nevermind"),MapCreator.bundleCreator.getString("replace")};
				sel=JOptionPane.showOptionDialog(RegionInfo.this, MapCreator.bundleCreator.getString("layernameexists"),lr.getName(),JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
			} else
				li=null;
		}
		if (sel==0) {
			//Place both layers in the pool of layers.
			//This is the default choice.
			if (lr.getTable()!=null && importTable) {
				//If no database exists, do a click in the "new database" button
				//in the mapinfo object to create a new one!
				if (creator.getDatabase()==null)
					creator.mapInfo.btnNew.doClick();
				//If no database exists and the user canceled the dialog defining
				//the db, the user is prompted to decide if he wants to import the layer
				//with no table.
				if (creator.getDatabase()==null) {
					int yn=JOptionPane.showConfirmDialog(RegionInfo.this, MapCreator.bundleCreator.getString("inslayernotable"),lr.getName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if (yn==JOptionPane.NO_OPTION) {
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						throw new BlockLayerImport("Blocked layer import!"); //%%%Careful! Method may exit here!
					}
					lr.setTable(null);
					retval=false;
				} else
					creator.getDatabase().addTable(lr.getTable(),true);
			}
			creator.addLayer(new LayerInfo(lr,creator,false));
		} else if (sel==1) {
			//Replace the old layer with the new one.
			//First replace the table in the database.
			if (li.getLayer().getTable()!=null) {
				if (lr.getTable()!=null && importTable)
					creator.getDatabase().replaceTable(li.getLayer().getTable(),lr.getTable(),true);
				else
					creator.getDatabase().removeTable(li.getLayer().getTable(),false);
			} else {
				if (lr.getTable()!=null && importTable) {
					//If no database exists, do a click in the "new database" button
					//in the mapinfo object to create a new one!
					if (creator.getDatabase()==null)
						creator.mapInfo.btnNew.doClick();
					//If no database exists and the user canceled the dialog defining
					//the db, the user is prompted to decide if he wants to import the layer
					//with no table.
					if (creator.getDatabase()==null) {
						int yn=JOptionPane.showConfirmDialog(RegionInfo.this, MapCreator.bundleCreator.getString("inslayernotable"),lr.getName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if (yn==JOptionPane.NO_OPTION)
							throw new BlockLayerImport("Blocked layer import!");  //%%%Careful! Method may exit here!
						lr.setTable(null);
						retval=false;
					} else
						creator.getDatabase().addTable(lr.getTable(),true);
				}
			}
			creator.replaceLayer(li,new LayerInfo(lr,creator,false));
		}
		return retval;
	}

	protected Region getRegion() {
		return region;
	}

	public String toString() {
		return name.getText();
	}

	public String getName() {
		return name.getText();
	}

	protected double getMinLong() throws NumberFormatException {
		return (new Double(longBL.getText())).doubleValue();
	}

	protected double getMinLat() throws NumberFormatException {
		return (new Double(latBL.getText())).doubleValue();
	}

	protected double getMaxLong() throws NumberFormatException {
		return (new Double(longTR.getText())).doubleValue();
	}

	protected double getMaxLat() throws NumberFormatException {
		return (new Double(latTR.getText())).doubleValue();
	}

	protected double getZMinLong() throws NumberFormatException {
		return (new Double(zlongBL.getText())).doubleValue();
	}

	protected double getZMinLat() throws NumberFormatException {
		return (new Double(zlatBL.getText())).doubleValue();
	}

	protected double getZMaxLong() throws NumberFormatException {
		return (new Double(zlongTR.getText())).doubleValue();
	}

	protected double getZMaxLat() throws NumberFormatException {
		return (new Double(zlatTR.getText())).doubleValue();
	}

	protected void addToAllLayers(LayerInfo l) {
		allLayersModel.addElement(l);
	}
	/**
	 * Add one layer info to the region info, if it exists in the all layers pool.
	 * This method does not add the layer itself in the region. It is used only
	 * when the region contains the layer and we want to update the region info.
	 */
	protected void addLayerInfo(Layer l) {
		for (int i=0;i<allLayersModel.getSize();i++)
			if (((LayerInfo) allLayersModel.getElementAt(i)).getLayer()==l) {
				layersModel.add(0,allLayersModel.getElementAt(i));
				allLayersModel.removeElementAt(i);
				break;
			}
	}

	protected void setModified(boolean value) {
		modified=value;
		if (value)
			modifiedLabel.setForeground(Color.red);
		else
			modifiedLabel.setForeground(Color.gray);
	}

	protected boolean isModified() {
		return modified;
	}

	protected int countLayers() {
		return layersModel.size();
	}

	private String truncZeroes(String s) {
		if ((s==null) || (s==""))
			return s;

		while (s.charAt(0)=='0')
			s=s.substring(1);
		return s;
	}

	private void produceBoundingRect() {
		try {
			//Not valid coordinates
			if (getMaxLong()-getMinLong()<0 || getMaxLat()-getMinLat()<0)
				return;
			Rectangle2D.Double r=new Rectangle2D.Double(getMinLong(),getMinLat(),getMaxLong()-getMinLong(),getMaxLat()-getMinLat());
			region.setBoundingRect(r);
		} catch(NumberFormatException e) {
			//Do nothing. Don't produce an event.
		}
	}

	private void produceZoomRect() {
		try {
			//Not valid coordinates
			if (getZMaxLong()-getZMinLong()<0 || getZMaxLat()-getZMinLat()<0)
				return;
			Rectangle2D.Double r=new Rectangle2D.Double(getZMinLong(),getZMinLat(),getZMaxLong()-getZMinLong(),getZMaxLat()-getZMinLat());
			region.mapNode.setZoomRectangle(r);
		} catch(NumberFormatException e) {
			region.mapNode.setZoomRectangle(null);
			//Do nothing. Don't produce an event.
		}
	}

	boolean contains(LayerInfo l) {
		return layersModel.contains(l);
	}

	void removeLayers(LayerInfo[] l) {
		for (int i=0;i<l.length;i++) {
			allLayersModel.removeElement(l[i]);
			if (layersModel.removeElement(l[i])) {
				region.removeLayer(l[i].getLayer());
				setModified(true);
			}
		}
	}

	void replaceLayer(LayerInfo oldli,LayerInfo newli) {
		//Replace the layer if the layer is contained in the region layers
		int i=layersModel.indexOf(oldli);
		if (i!=-1) {
			layersModel.set(i,newli);
			region.addLayer(newli.getLayer());
		}
		//Replace the layer in the all layers list, if the layer is not contained in the region layers
		i=allLayersModel.indexOf(oldli);
		if (i!=-1)
			allLayersModel.set(i,newli);
	}

	void die() {
		try {
			region.getBackgrounds().clearListeners();
			removeAll();
			images=null;
		} catch(Throwable t) {
			System.err.println("MAP#200008100042: Cannot free-up RegionInfo resources.");
		}
	}

	private void jbInit() throws Exception {
		name.setText("My Region");
		name.setForeground(new java.awt.Color(0, 0, 160));
		name.setFont(new java.awt.Font(name.getFont().getName(), 1, (int) (name.getFont().getSize()*1.5)));
		modifiedLabel.setText("*");
		modifiedLabel.setForeground(Color.gray);
		modifiedLabel.setFont(new java.awt.Font(modifiedLabel.getFont().getName(), 1, (int) (modifiedLabel.getFont().getSize()*1.6)));
		this.setLayout(smartLayout1);
		Font labelFont=new java.awt.Font(backLabel.getFont().getName(), 1, backLabel.getFont().getSize());
		backLabel.setFont(labelFont);
		backLabel.setText("Background Images");
		browse.setText("Add Background");
		coordsLabel.setFont(labelFont);
		coordsLabel.setText("Geographic coordinates of the region");
		longBLLabel.setText("Bottom-Left longitude");
		longBL.setText("jTextField1");
		latBLLabel.setText("Bottom-Left latitude");
		latBL.setText("jTextField1");
		longTRLabel.setText("Top-Right longitude");
		longTR.setText("jTextField1");
		latTRLabel.setText("Top_Right latitude");
		latTR.setText("jTextField1");
		zoomLabel.setFont(labelFont);
		orientationLabel.setFont(labelFont);
		orientationLabel.setText("orientation");
		orientation.setText("jTextField1");
		degreesLabel.setText("degrees");
		linkedLabel.setFont(labelFont);
		scaleLabel.setFont(labelFont);
		scaleLabel.setText("Scale");
		scale.setText("jTextField1");
		availableLabel.setFont(labelFont);
		availableLabel.setText("Available Layers");
		regionLabel.setFont(labelFont);
		regionLabel.setText("Region Layers");
		right.setRequestFocusEnabled(false);
		right.setFocusPainted(false);
		buttonPanel.setLayout(smartLayout2);
		left.setRequestFocusEnabled(false);
		left.setFocusPainted(false);
		up.setRequestFocusEnabled(false);
		up.setFocusPainted(false);
		down.setRequestFocusEnabled(false);
		down.setFocusPainted(false);
		newLayer.setRequestFocusEnabled(false);
		newLayer.setFocusPainted(false);
		previewLayer.setRequestFocusEnabled(false);
		previewLayer.setFocusPainted(false);
		deleteLayer.setRequestFocusEnabled(false);
		deleteLayer.setFocusPainted(false);
		importLayer.setRequestFocusEnabled(false);
		importLayer.setFocusPainted(false);
		exportLayer.setRequestFocusEnabled(false);
		exportLayer.setFocusPainted(false);
		buttonPanel.setOpaque(false);
		tbnMotion.setRequestFocusEnabled(false);
		tbnMotion.setFocusPainted(false);
		lblID.setText("ID:");
		cmbID.setEditable(true);
		lblCoordType.setText("Coordinates");
		lblUnits.setText("Unit");
		lblUpm.setText("Units/m");
		btnDelete.setText("Delete");
		btnDate.setText("Properties");
		btnDefault.setIcon(new ImageIcon(gr.cti.eslate.mapModel.RegionInfo.class.getResource("images/defaultback.gif")));
		btnDefault.setText("Set as default");
		this.add(modifiedLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(name, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(name, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 15),
					new com.thwt.layout.EdgeAnchor(modifiedLabel, Anchor.Left, Anchor.Left, Anchor.Right, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(backLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(name, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(browse, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(backLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 16, 1.0)));
		this.add(coordsLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(btnDelete, Anchor.Bottom, Anchor.Below, Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(longBLLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(lblCoordType, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.EdgeAnchor(longBL, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(longBL, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 20),
					new com.thwt.layout.EdgeAnchor(longBLLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(longBLLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -3)));
		this.add(latBLLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(longBLLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(latBL, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(latBL, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(latBLLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(longBLLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(longTRLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(longBLLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(longTR, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(longTR, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(longTRLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(longBL, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(latTRLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(latBL, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(latTR, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(latTR, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(latTRLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(latBL, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(zoomLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(latTR, Anchor.Bottom, Anchor.Below, Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(zlongBLLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(zoomLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.EdgeAnchor(zlongBL, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(zlongBL, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 20),
					new com.thwt.layout.EdgeAnchor(zlongBLLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(zlongBLLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -3)));
		this.add(zlatBLLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(zlongBLLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(zlatBL, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(zlatBL, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(zlatBLLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(zlongBLLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(zlongTRLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(zlongBLLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(zlongTR, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(zlongTR, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(zlongTRLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(zlongBL, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(zlatTRLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(zlatBL, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(zlatTR, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(zlatTR, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(zlatTRLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(zlatBL, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(orientationLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(zlongTR, Anchor.Bottom, Anchor.Below, Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(orientation, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(degreesLabel, Anchor.Left, Anchor.Left, Anchor.Right, 3),
					new com.thwt.layout.EdgeAnchor(orientationLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(degreesLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(orientationLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(orientation, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(linkedLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(zlongTR, Anchor.Bottom, Anchor.Below, Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(linked, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(linkedLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Left, Anchor.Left, 0.0, -5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(scaleLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(orientationLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(scale, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(scaleLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(availableLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(orientation, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(regionLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(buttonPanel, Anchor.Right, Anchor.Right, Anchor.Left, 3),
					new com.thwt.layout.EdgeAnchor(availableLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(buttonPanel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 16),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 72),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.FractionAnchor(allLayersScroll, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0)));
		buttonPanel.add(right, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 16),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 16),
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 0),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 0)));
		buttonPanel.add(left, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 16),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 16),
					new com.thwt.layout.EdgeAnchor(right, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(right, Anchor.Left, Anchor.Same, Anchor.Left, 0)));
		buttonPanel.add(up, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 16),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 16),
					new com.thwt.layout.EdgeAnchor(left, Anchor.Bottom, Anchor.Below, Anchor.Top, 8),
					new com.thwt.layout.EdgeAnchor(left, Anchor.Left, Anchor.Same, Anchor.Left, 0)));
		buttonPanel.add(down, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 16),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 16),
					new com.thwt.layout.EdgeAnchor(up, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(left, Anchor.Left, Anchor.Same, Anchor.Left, 0)));
		this.add(newLayer, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 22),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 17),
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 3)));
		this.add(previewLayer, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 22),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 17),
					new com.thwt.layout.EdgeAnchor(newLayer, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(newLayer, Anchor.Top, Anchor.Same, Anchor.Top, 0)));
		this.add(deleteLayer, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 22),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 17),
					new com.thwt.layout.EdgeAnchor(previewLayer, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(newLayer, Anchor.Top, Anchor.Same, Anchor.Top, 0)));
		this.add(importLayer, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 22),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 17),
					new com.thwt.layout.EdgeAnchor(deleteLayer, Anchor.Right, Anchor.Right, Anchor.Left, 4),
					new com.thwt.layout.EdgeAnchor(newLayer, Anchor.Top, Anchor.Same, Anchor.Top, 0)));
		this.add(exportLayer, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 22),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 17),
					new com.thwt.layout.EdgeAnchor(importLayer, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(newLayer, Anchor.Top, Anchor.Same, Anchor.Top, 0)));
		this.add(allLayersScroll, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(buttonPanel, Anchor.Left, Anchor.Left, Anchor.Right, 3),
					new com.thwt.layout.EdgeAnchor(availableLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.EdgeAnchor(newLayer, Anchor.Top, Anchor.Above, Anchor.Bottom, 0)));
		this.add(layersScroll, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(buttonPanel, Anchor.Right, Anchor.Right, Anchor.Left, 3),
					new com.thwt.layout.EdgeAnchor(availableLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.EdgeAnchor(newLayer, Anchor.Top, Anchor.Above, Anchor.Bottom, 0)));
		this.add(backScroll, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(browse, Anchor.Left, Anchor.Left, Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(browse, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(browse, Anchor.Bottom, Anchor.Same, Anchor.Bottom, 0)));
		this.add(tbnMotion, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 22),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 17),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 3),
					new com.thwt.layout.EdgeAnchor(layersScroll, Anchor.Left, Anchor.Same, Anchor.Left, 0)));
		this.add(lblID, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(tbnMotion, Anchor.Right, Anchor.Right, Anchor.Left, 1),
					new com.thwt.layout.EdgeAnchor(tbnMotion, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(tbnMotion, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(cmbID, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblID, Anchor.Right, Anchor.Right, Anchor.Left, 1),
					new com.thwt.layout.EdgeAnchor(tbnMotion, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(layersScroll, Anchor.Right, Anchor.Same, Anchor.Right, 0),
					new com.thwt.layout.EdgeAnchor(tbnMotion, Anchor.Height, Anchor.Same, Anchor.Height, 0)));
		this.add(lblCoordType, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(cbxCoords, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(cbxCoords, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(lblUnits, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(cbxCoords, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(lblCoordType, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(cbxUnits, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(lblUpm, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(cbxUnits, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(lblCoordType, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(lblCoordType, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.add(cbxCoords, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblCoordType, Anchor.Right, Anchor.Right, Anchor.Left, 3),
					new com.thwt.layout.EdgeAnchor(coordsLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.45, Anchor.Right, Anchor.Right, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(cbxUnits, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblUnits, Anchor.Right, Anchor.Right, Anchor.Left, 3),
					new com.thwt.layout.EdgeAnchor(cbxCoords, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.75, Anchor.Right, Anchor.Right, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(txtUpp, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(lblUpm, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(cbxUnits, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(cbxUnits, Anchor.Height, Anchor.Same, Anchor.Height, 0)));
		this.add(btnDate, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(btnDefault, Anchor.Right, Anchor.Right, Anchor.Left, 2),
					new com.thwt.layout.EdgeAnchor(btnDelete, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(btnDelete, Anchor.Height, Anchor.Same, Anchor.Height, 0)));
		this.add(btnDelete, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(backScroll, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.33, Anchor.Right, Anchor.Right, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -4, 1.0)));
		this.add(btnDefault, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnDelete, Anchor.Right, Anchor.Right, Anchor.Left, 2),
					new com.thwt.layout.EdgeAnchor(btnDelete, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(btnDelete, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.66, Anchor.Right, Anchor.Right, 0.0, 2)));
	}

	//Local members
	private MapNode node;
	private Region region;
	private MapCreator creator;
	private boolean modified;
	private JTree tree;
	private JList images,allLayers,layers;
	private DefaultListModel layersModel,allLayersModel;
	protected static final Icon motionIcon=Helpers.loadImageIcon("images/motionLayer.gif");
	/**
	 * Flags if the region will ask before adding or removing a background.
	 */
	static boolean confirmSaveBack=true;

	//UI members generated by JBuilder
	private SmartLayout smartLayout1 = new SmartLayout();
	private JLabel modifiedLabel = new JLabel();
	private JTextField name = new JTextField();
	private JLabel backLabel = new JLabel();
	private JButton browse = new JButton();
	private JLabel coordsLabel = new JLabel();
	private JLabel longBLLabel = new JLabel();
	private JNumberField longBL = new JNumberField("");
	private JLabel latBLLabel = new JLabel();
	private JNumberField latBL = new JNumberField("");
	private JLabel longTRLabel = new JLabel();
	private JNumberField longTR = new JNumberField("");
	private JLabel latTRLabel = new JLabel();
	private JNumberField latTR = new JNumberField("");
	private JLabel zoomLabel = new JLabel();
	private JLabel zlongBLLabel = new JLabel();
	private JNumberField zlongBL = new JNumberField("");
	private JLabel zlatBLLabel = new JLabel();
	private JNumberField zlatBL = new JNumberField("");
	private JLabel zlongTRLabel = new JLabel();
	private JNumberField zlongTR = new JNumberField("");
	private JLabel zlatTRLabel = new JLabel();
	private JNumberField zlatTR = new JNumberField("");
	private JLabel orientationLabel = new JLabel();
	private JNumberField orientation = new JNumberField("");
	private JLabel degreesLabel = new JLabel();
	private JLabel scaleLabel = new JLabel();
	private JNumberField scale = new JNumberField("");
	private JLabel availableLabel = new JLabel();
	private JLabel regionLabel = new JLabel();
	private JPanel buttonPanel = new JPanel();
	private JButton right = new JButton();
	private SmartLayout smartLayout2 = new SmartLayout();
	private JButton left = new JButton();
	private JButton up = new JButton();
	private JButton down = new JButton();
	private JButton newLayer = new JButton();
	private JButton previewLayer = new JButton();
	private JButton deleteLayer = new JButton();
	private JButton importLayer = new JButton();
	private JButton exportLayer = new JButton();
	private JScrollPane allLayersScroll = new JScrollPane();
	private JScrollPane layersScroll = new JScrollPane();
	private JScrollPane backScroll = new JScrollPane();
	private JToggleButton tbnMotion = new JToggleButton();
	private JLabel lblID = new JLabel();
	private JComboBox cmbID = new JComboBox();
	private JLabel lblCoordType = new JLabel();
	private JLabel lblUnits = new JLabel();
	private JLabel lblUpm = new JLabel();
	private JComboBox cbxCoords = new JComboBox();
	private JComboBox cbxUnits = new JComboBox();
	private JNumberField txtUpp = new JNumberField("");
	private JButton btnDelete = new JButton();
	private JButton btnDate = new JButton();
	private JButton btnDefault = new JButton();
	private JLabel linkedLabel = new JLabel("Linked");
	private JComboBox linked = new JComboBox();
	private ItemListener linkedItemListener;
}
