package gr.cti.eslate.mapModel;

import gr.cti.eslate.database.engine.DBase;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.utils.ESlateFileDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class MapCreator extends JFrame {

	public MapCreator() {
	}

	public MapCreator(Map map) throws IllegalArgumentException {
		if (map==null) throw new IllegalArgumentException();

		try {
			setIconImage((new ImageIcon(Map.class.getResource("images/mapModelBeanIcon.gif")).getImage()));
		} catch(Exception e) {/*No icon! Sniff!*/}

		this.map=map;
		db=map.getDatabase();
		hashMap=new HashMap();
		((JComponent) getContentPane()).setPreferredSize(new Dimension(600,575));
		setTitle(((map.getName()==null)?"":(map.getName()+": "))+bundleCreator.getString("title"));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//Focus requested sothat the last field edited gets its value
				requestFocus();
				if (listeners==null)
					return;
				for (int i=0;i<listeners.size();i++)
					((MapCreatorListener) listeners.get(i)).mapCreatorClosing();
			}
			public void windowClosed(WindowEvent e) {
				if (listeners==null)
					return;
				for (int i=0;i<listeners.size();i++)
					((MapCreatorListener) listeners.get(i)).mapCreatorClosed();
				listeners.clear();
				listeners=null;
			}
		});

		//JSplitPane
		JSplitPane pane=new JSplitPane();
		pane.setDividerSize(5);
		JScrollPane left=new JScrollPane();
		right=new JScrollPane();

		//JTree
		tree=new JTree(map);
		tree.setRowHeight(-1);
		ToolTipManager.sharedInstance().registerComponent(tree);
		//Define our own renderer to change the icons.
		DefaultTreeCellRenderer dtcr=new DefaultTreeCellRenderer() {
			private ImageIcon regionIcon=Helpers.loadImageIcon("images/region.gif");
			private ImageIcon entryRegionIcon=Helpers.loadImageIcon("images/entryRegion.gif");
			public java.awt.Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,int row,boolean hasFocus) {
				super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);
				if (value.equals(MapCreator.this.map.getEntryNode())) {
					setIcon(entryRegionIcon);
					setToolTipText(bundleCreator.getString("entryregiontreetip"));
				} else {
					setIcon(regionIcon);
					setToolTipText(null);
				}
				return this;
			}
			public Color getBackgroundNonSelectionColor() {
				return bnsc;
			}
			private final Color bnsc=tree.getBackground();
		};
		tree.setCellRenderer(dtcr);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		mapInfoLabel=new MyJLabel(map.getName(),Helpers.loadImageIcon("images/map.gif"),SwingConstants.LEADING);
		mapInfoLabel.setOpaque(true);
		mapInfoLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mapInfoLabel.setSelected(true);
				//if (MapCreator.this.map.getRoot()==null)
					newRegion.setEnabled(true);
				//else
				//    newRegion.setEnabled(false);
				deleteRegion.setEnabled(false);
				importRegion.setEnabled(true);
				exportRegion.setEnabled(false);
				entryRegionT.setEnabled(false);
				tree.clearSelection();
			}
		});

		JPanel p=new JPanel();
		p.setLayout(new BorderLayout());
		p.add(mapInfoLabel,BorderLayout.NORTH);
		p.add(tree,BorderLayout.CENTER);

		left.getViewport().setView(p);
		pane.setLeftComponent(left);
		pane.setRightComponent(right);

		//Toolbar
		toolbar=new JPanel();
		toolbar.setLayout(new BoxLayout(toolbar,BoxLayout.X_AXIS));
		toolbar.setBorder(new EmptyBorder(1,1,1,1));
		toolbar.setPreferredSize(new Dimension(200,24));
		toolbar.setMaximumSize(new Dimension(200,24));
		newRegion=new JButton(Helpers.loadImageIcon("images/newRegion.gif"));
		newRegion.setToolTipText(MapCreator.bundleCreator.getString("newregiontip"));
		newRegion.setFocusPainted(false);
		newRegion.setRequestFocusEnabled(false);
		newRegion.setMaximumSize(new Dimension(22,22));
		newRegion.setEnabled(false);
		newRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addObject(new Region());
				((RegionInfo)right.getViewport().getView()).constructLinkList();
			}
			public MapNode addObject(Region child) {
				MapNode parentNode = null;
				TreePath parentPath = tree.getSelectionPath();
				if (parentPath!=null)
					parentNode=(MapNode) (parentPath.getLastPathComponent());
				return addObject(parentNode, child, true);
			}
			public MapNode addObject(MapNode parent,Region child,boolean shouldBeVisible) {
				MapNode newNode=new MapNode(child);
				if (parent==null) {
					//Change the root
					MapNode oldroot=MapCreator.this.map.getMapRoot();
					MapCreator.this.map.setMapRoot(newNode);
					MapCreator.this.map.insertNodeInto(oldroot,newNode,0);
					hashMap.put(newNode,new RegionInfo(newNode,MapCreator.this,tree));
					MapCreator.this.map.reload();
				} else {
					MapCreator.this.map.insertNodeInto(newNode,parent,parent.getChildCount());
					hashMap.put(newNode,new RegionInfo(newNode,MapCreator.this,tree));
				}

				// Make sure the user can see the lovely new node.
				if (shouldBeVisible) {
					TreePath tp=new TreePath(newNode.getPath());
					tree.scrollPathToVisible(tp);
					tree.setSelectionPath(tp);
				}
				return newNode;
			}
		});
		toolbar.add(newRegion);
		//
		deleteRegion=new JButton(Helpers.loadImageIcon("images/deleteRegion.gif"));
		deleteRegion.setToolTipText(MapCreator.bundleCreator.getString("deleteregiontip"));
		deleteRegion.setFocusPainted(false);
		deleteRegion.setRequestFocusEnabled(false);
		deleteRegion.setPreferredSize(new Dimension(22,22));
		deleteRegion.setMaximumSize(new Dimension(22,22));
		deleteRegion.setEnabled(false);
		deleteRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//** Remove the currently selected node.
				TreePath currentSelection=tree.getSelectionPath();
				MapNode currentNode=(MapNode) (currentSelection.getLastPathComponent());
				int i=JOptionPane.showConfirmDialog(MapCreator.this,bundleCreator.getString("confirmdeleteregion"),"",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
				if (i==JOptionPane.NO_OPTION)
					return;
				//** Search if the entry node belongs to the deleted node subtree
				Enumeration en=currentNode.breadthFirstEnumeration();
				while (en.hasMoreElements())
					if (((MapNode)en.nextElement()).equals(MapCreator.this.map.getEntryNode())) {
						MapCreator.this.map.setEntryRegion(null);
						break;
					}
				MapCreator.this.map.removeNodeFromParent(currentNode);
				hashMap.remove(currentNode.getRegion());
			}
		});
		toolbar.add(deleteRegion);
		//
		entryRegionT=new JButton(Helpers.loadImageIcon("images/entryRegionTool.gif"));
		entryRegionT.setToolTipText(MapCreator.bundleCreator.getString("entryregiontip"));
		entryRegionT.setFocusPainted(false);
		entryRegionT.setRequestFocusEnabled(false);
		entryRegionT.setPreferredSize(new Dimension(22,22));
		entryRegionT.setMaximumSize(new Dimension(22,22));
		entryRegionT.setEnabled(false);
		entryRegionT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MapCreator.this.map.setEntryNode((MapNode) (tree.getSelectionPath().getLastPathComponent()));
				tree.repaint();
			}
		});
		toolbar.add(entryRegionT);

		toolbar.add(Box.createRigidArea(new Dimension(6,22)));

		//
		importRegion=new JButton(Helpers.loadImageIcon("images/importRegion.gif"));
		importRegion.setToolTipText(MapCreator.bundleCreator.getString("importregiontip"));
		importRegion.setFocusPainted(false);
		importRegion.setRequestFocusEnabled(false);
		importRegion.setPreferredSize(new Dimension(22,22));
		importRegion.setMaximumSize(new Dimension(22,22));
		importRegion.setEnabled(false);
		importRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				ESlateFileDialog fd=new ESlateFileDialog(MapCreator.this,MapCreator.bundleCreator.getString("importregion"),ESlateFileDialog.LOAD,".rgn");
				if (MapCreator.globalDir!=null)
					fd.setDirectory(MapCreator.globalDir);
				fd.setFile("*.rgn");
				fd.show();
				if (fd.getFile()!=null) {
					MapCreator.globalDir=fd.getDirectory();
					JFrame f=new JFrame(MapCreator.bundleCreator.getString("wait"));
					try {
						ObjectInputStream in=new ObjectInputStream(new FileInputStream(fd.getDirectory()+fd.getFile()));
						f.setResizable(false);
						JLabel lll=new JLabel(MapCreator.bundleCreator.getString("importregioninfo"));
						f.getContentPane().add(lll);
						f.pack();
						Dimension s=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
						f.setLocation((s.width-f.getWidth())/2,(s.height-f.getHeight())/2);
						f.show();
						lll.paintImmediately(lll.getVisibleRect());
						//Read the region
						Region rg=Region.importRegion(in);
						rg.pending=true;
						MapNode newNode=new MapNode(rg);
						RegionInfo ri=new RegionInfo(newNode,MapCreator.this,tree);
						hashMap.put(newNode,ri);

						if (tree.getLastSelectedPathComponent()!=null) {
							//Importing a region depended to another one
							MapNode parent=(MapNode) tree.getLastSelectedPathComponent();
							MapCreator.this.map.insertNodeInto(newNode,parent,parent.getChildCount());
						} else {
							//Importing a region as a new root
							MapNode oldroot=(MapNode) MapCreator.this.map.getRoot();
							MapCreator.this.map.setMapRoot(newNode);
							MapCreator.this.map.insertNodeInto(oldroot,newNode,0);
							MapCreator.this.map.reload();
						}

						//Importing its layers
						boolean impTb=true;
						try {
							for (int i=0;i<rg.getLayers().length;i++) {
								//This trick with impTb ensures that if the user decides once
								//to insert a layer with no table, no further similar questions
								//will arise.
								boolean wantImp=ri.importLayerMthd(rg.getLayers()[i],impTb);
								impTb=impTb && wantImp;
								ri.addLayerInfo(rg.getLayers()[i]);
							}
						} catch(BlockLayerImport exx) {
							//Stop importing layers, the user chose so.
						}

						rg.pending=false;
						TreePath tp=new TreePath(newNode.getPath());
						tree.scrollPathToVisible(tp);
						tree.setSelectionPath(tp);
					} catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(MapCreator.this,MapCreator.bundleCreator.getString("cannotloadfile"),MapCreator.bundleCreator.getString("importregion"),JOptionPane.ERROR_MESSAGE);
					} finally {
						f.dispose();
					}
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		toolbar.add(importRegion);

		//
		exportRegion=new JButton(Helpers.loadImageIcon("images/exportRegion.gif"));
		exportRegion.setToolTipText(MapCreator.bundleCreator.getString("exportregiontip"));
		exportRegion.setFocusPainted(false);
		exportRegion.setRequestFocusEnabled(false);
		exportRegion.setPreferredSize(new Dimension(22,22));
		exportRegion.setMaximumSize(new Dimension(22,22));
		exportRegion.setEnabled(false);
		exportRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MapNode node=(MapNode) tree.getLastSelectedPathComponent();
				if (node==null)
					return; //%%% Method may exit here!
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Region ri=node.getRegion();
				ESlateFileDialog fd=new ESlateFileDialog(MapCreator.this,MapCreator.bundleCreator.getString("exportregion")+ri.getName(),ESlateFileDialog.SAVE,".rgn");
				if (MapCreator.globalDir!=null)
					fd.setDirectory(MapCreator.globalDir);
				fd.setFile(ri.getName()+".rgn");
				fd.show();
				if (fd.getFile()!=null) {
					MapCreator.globalDir=fd.getDirectory();
					try {
						int exportLayersAsWell;
						if (ri.getLayers().length>0)
							exportLayersAsWell=JOptionPane.showConfirmDialog(MapCreator.this,MapCreator.bundleCreator.getString("exportlayrsaswell"),MapCreator.bundleCreator.getString("exportregion")+ri.getName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						else
							exportLayersAsWell=JOptionPane.NO_OPTION;
						ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(fd.getDirectory()+fd.getFile()));
						ri.exportRegion(out,exportLayersAsWell!=JOptionPane.NO_OPTION);
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(MapCreator.this,MapCreator.bundleCreator.getString("cannotcreatefile"),MapCreator.bundleCreator.getString("exportregion")+ri.getName(),JOptionPane.ERROR_MESSAGE);
					}
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		toolbar.add(exportRegion);

		toolbar.add(Box.createRigidArea(new Dimension(6,22)));

		//
		check=new JButton(Helpers.loadImageIcon("images/check.gif"));
		check.setToolTipText(MapCreator.bundleCreator.getString("checktip"));
		check.setFocusPainted(false);
		check.setRequestFocusEnabled(false);
		check.setPreferredSize(new Dimension(22,22));
		check.setMaximumSize(new Dimension(22,22));
		check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OutputDialog outDialog=new OutputDialog(MapCreator.this);
				outDialog.setTitle(MapCreator.bundleCreator.getString("integritycheck"));
				Dimension s=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				outDialog.setLocation((s.width-outDialog.getWidth())/2,(s.height-outDialog.getHeight())/2);
				//Output starting messages
				OutputDialog.Console log=outDialog.getConsole();
				log.append(MapCreator.bundleCreator.getString("checking"));
				log.append(MapCreator.bundleCreator.getString("version"));
				log.append(" ");
				outDialog.show();
			}
		});
		toolbar.add(check);

		//Listen for a change in the selection.
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				MapNode node=(MapNode) tree.getLastSelectedPathComponent();
				if (node==null) {
					mapInfoLabel.setSelected(true);
					newRegion.setEnabled(true);
					deleteRegion.setEnabled(false);
					entryRegionT.setEnabled(false);
					importRegion.setEnabled(true);
					exportRegion.setEnabled(false);
					return;
				}
				mapInfoLabel.setSelected(false);
				newRegion.setEnabled(true);
				if (node.equals(MapCreator.this.map.getRoot()))
					deleteRegion.setEnabled(false);
				else
					deleteRegion.setEnabled(true);
				entryRegionT.setEnabled(true);
				importRegion.setEnabled(true);
				exportRegion.setEnabled(true);

				right.getViewport().setView((JComponent) hashMap.get(node));
			}
		});
		tree.setSelectionRow(0);

		//Dekor
		JLabel l=new JLabel(new ImageIcon(Map.class.getResource("images/corner.gif")));
		l.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolbar,BorderLayout.NORTH);
		getContentPane().add(pane,BorderLayout.CENTER);
		getContentPane().add(l,BorderLayout.SOUTH);

		//Create the MapInfo panel.
		mapInfo=new MapInfo(map,this,mapInfoLabel);
		//Add the unused layers of the map to the pool of layers
		for (int i=0;i<map.unusedLayers.size();i++)
			allLayers.add(new LayerInfo((Layer) map.unusedLayers.get(i),this,false));
		//Find all the Layers. This must be done before creating the RegionInfo panels.
		Enumeration allNodes=((MapNode) map.getRoot()).breadthFirstEnumeration();
		if (allNodes!=null) {
			while (allNodes.hasMoreElements()) {
				MapNode node=(MapNode) allNodes.nextElement();
				boolean found;
				for (int i=0;i<node.getRegion().getLayers().length;i++) {
					found=false;
					for (int j=0;j<allLayers.size();j++)
						if (((LayerInfo) allLayers.get(j)).getLayer()==node.getRegion().getLayers()[i]) {
							found=true;
							break;
						}
					if (!found)
						allLayers.add(new LayerInfo(node.getRegion().getLayers()[i],this,false));
				}
			}
			//Create the RegionInfo panels for all the regions of the map.
			allNodes=((MapNode) map.getRoot()).breadthFirstEnumeration();
			while (allNodes.hasMoreElements()) {
				MapNode node=(MapNode) allNodes.nextElement();
				hashMap.put(node,new RegionInfo(node,this,tree));
			}
		}

		tree.expandPath(tree.getPathForRow(0));
		tree.clearSelection();
		mapInfoLabel.setSelected(true);
		pack();
	}

	protected void addLayer(LayerInfo l) {
		allLayers.add(l);
		Iterator it=hashMap.keySet().iterator();
		while (it.hasNext())
			((RegionInfo) hashMap.get(it.next())).addToAllLayers(l);
	}
	/**
	 * Informs all layers for the addition of a new table.
	 */
	protected void addTable(Table table) {
		for (int i=0;i<listeners.size();i++)
			((MapCreatorListener) listeners.get(i)).tableDefinition(table);
	}
	/**
	 * Informs all layers for the declaration of a new database.
	 */
	protected void setDatabase(DBase db) {
		this.db=db;
		for (int i=0;i<listeners.size();i++)
			((MapCreatorListener) listeners.get(i)).databaseDefinition();
		map.setDatabase(db);
	}

	protected DBase getDatabase() {
		return db;
	}

	protected ArrayList getAllLayers() {
		return allLayers;
	}

	public void addMapCreatorListener(MapCreatorListener ml) {
		listeners.add(ml);
	}

	public void removeMapCreatorListener(MapCreatorListener ml) {
		listeners.remove(ml);
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID()==WindowEvent.WINDOW_CLOSING) {
			int checkInt=checkIntegrity(new OutputDialog(MapCreator.this).getConsole());
			int decision=1;
			Object[] options={MapCreator.bundleCreator.getString("closeandexit"),MapCreator.bundleCreator.getString("returntocreator"),MapCreator.bundleCreator.getString("viewerrors")};
			if (checkInt==ERRORS_IN_MAP)
				decision=JOptionPane.showOptionDialog(MapCreator.this,MapCreator.bundleCreator.getString("errorsencountered"),"",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
			else if (checkInt==WARNINGS_IN_MAP)
				decision=JOptionPane.showOptionDialog(MapCreator.this,MapCreator.bundleCreator.getString("warningsencountered"),"",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
			else if (checkInt==NO_ERRORS)
				decision=0;
			if (decision==2)
				check.doClick();
			else if (decision==0)
				dispose();
		} else
			super.processWindowEvent(e);
	}

	public void dispose() {
//		try {
			//finalize();
		//} catch(Throwable t) {
			//t.printStackTrace();
		//}
		super.dispose();
		boolean modified=mapInfo.isModified();
		if (!modified)
			for (int i=0;i<allLayers.size();i++)
				if (((LayerInfo) allLayers.get(i)).isModified()) {
					modified=true;
					break;
				}
		if (!modified) {
			Iterator it=hashMap.values().iterator();
			while (it.hasNext()) {
				if (((RegionInfo) it.next()).isModified()) {
					modified=true;
					break;
				}
			}
		}
		//Put all the unused layers back to the map
		map.unusedLayers.clear();
		for (int i=0;i<allLayers.size();i++) {
			boolean contained=false;
			Iterator it=hashMap.values().iterator();
			while (it.hasNext()) {
				if (((RegionInfo) it.next()).contains((LayerInfo) allLayers.get(i))) {
					contained=true;
					break;
				}
			}
			if (!contained)
				map.unusedLayers.add(((LayerInfo) allLayers.get(i)).getLayer());
		}
		//Cleanup MapInfo object
		mapInfo.removeAll();
		//Cleanup RegionInfo objects
		Iterator it=hashMap.values().iterator();
		while (it.hasNext())
			((RegionInfo) it.next()).die();
		//Cleanup LayerInfo objects
		for (int i=0;i<allLayers.size();i++)
			((LayerInfo) allLayers.get(i)).die();
		map.clearTreeModelListeners();
		//Cleanup Creator object
		toolbar=null;
		tree=null;
		mapInfoLabel=null;
		right=null;
		mapInfo=null;
		hashMap.clear();
		hashMap=null;
		newRegion=deleteRegion=entryRegionT=importRegion=exportRegion=check=null;
		allLayers.clear();
		allLayers=null;
		db=null;
		removeAll();
		map.creatorClosing(modified);
		map=null;
	}

	protected int checkIntegrity(OutputDialog.Console log) {
		boolean error=false,warning=false;
		//**Check entry map**
		if (map.getEntryNode()==null) {
			log.append("* "+MapCreator.bundleCreator.getString("errnoentryregion")+" \""+(mapInfo.getName())+"\".");
			error=true;
		}
		//**Check database**
		DBase db;
		if ((db=getDatabase())==null) {
			log.append("* "+MapCreator.bundleCreator.getString("warnnodatabase")+" \""+(mapInfo.getName())+"\".");
			warning=true;
		}
		//**Check regions**
		Enumeration e=((MapNode) map.getRoot()).breadthFirstEnumeration();
		RegionInfo region;
		boolean regionsExist=false;
		while (e.hasMoreElements()) {
			regionsExist=true;
			region=(RegionInfo) hashMap.get(e.nextElement());
			//Check Bottom-Left longitude
			try {
				region.getMinLong();
			} catch(NumberFormatException nfe) {
				log.append("* "+MapCreator.bundleCreator.getString("errminlong")+" \""+region.getName()+"\".");
				error=true;
			}
			//Check Bottom-Left latitude
			try {
				region.getMinLat();
			} catch(NumberFormatException nfe) {
				log.append("* "+MapCreator.bundleCreator.getString("errminlat")+" \""+region.getName()+"\".");
				error=true;
			}
			//Check Top-Right longitude
			try {
				region.getMaxLong();
			} catch(NumberFormatException nfe) {
				log.append("* "+MapCreator.bundleCreator.getString("errmaxlong")+" \""+region.getName()+"\".");
				error=true;
			}
			//Check Top-Right latitude
			try {
				region.getMaxLat();
			} catch(NumberFormatException nfe) {
				log.append("* "+MapCreator.bundleCreator.getString("errmaxlat")+" \""+region.getName()+"\".");
				error=true;
			}
			//Check images
			if (region.getRegion().getBackgrounds().getSize()==0) {
				log.append("* "+MapCreator.bundleCreator.getString("warnnoimages")+" \""+region.getName()+"\".");
				warning=true;
			}
			//Check layer existence
			if (region.countLayers()==0) {
				log.append("* "+MapCreator.bundleCreator.getString("warnnolayersinregion")+" \""+region.getName()+"\".");
				warning=true;
			}
		}
		//Check whether there are no regions in the map
		if (!regionsExist) {
			log.append("* "+MapCreator.bundleCreator.getString("errnoregions"));
			error=true;
		}
		//**Check layers**
		if (allLayers.size()==0)
			log.append("* "+MapCreator.bundleCreator.getString("warnnolayers"));
		LayerInfo li;
		for (int i=0;i<allLayers.size();i++) {
			li=(LayerInfo) allLayers.get(i);
			if (li.getShapefileInfo()==null) {
				log.append("* "+MapCreator.bundleCreator.getString("errnoshapefile")+" \""+li.getName()+"\".");
				error=true;
			}
			if (li.getTable()==null) {
				log.append("* "+MapCreator.bundleCreator.getString("warnnotable")+" \""+li.getName()+"\".");
				warning=true;
			} else if (db!=null && db.indexOf(li.getTable())==-1) {
				log.append("* "+MapCreator.bundleCreator.getString("errdiffdb")+" \""+li.getName()+"\".");
				error=true;
			}
		}

		if (error)
			return ERRORS_IN_MAP;
		else if (warning)
			return WARNINGS_IN_MAP;
		else
			return NO_ERRORS;
	}

	void propagateLayerRemoval(LayerInfo[] layers) {
		for (int i=0;i<layers.length;i++) {
			map.deletedLayers.add(layers[i].getLayer().getGUID());
			allLayers.remove(layers[i]);
		}
		Iterator it=hashMap.values().iterator();
		while (it.hasNext()) {
			RegionInfo ri=(RegionInfo) it.next();
			ri.removeLayers(layers);
		}
	}

	void replaceLayer(LayerInfo oldli,LayerInfo newli) {
		allLayers.remove(oldli);
		Iterator it=hashMap.values().iterator();
		while (it.hasNext())
			((RegionInfo) it.next()).replaceLayer(oldli,newli);
   }

	protected void finalize() throws Throwable {
		try {
			super.finalize();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	private class MyJLabel extends JLabel {
		boolean selected=false;
		private MyJLabel(String text,Icon icon,int ha) {
			super(text,icon,ha);
		}
		public void paintComponent(java.awt.Graphics g) {
			if (selected) {
				setBackground(((DefaultTreeCellRenderer) tree.getCellRenderer()).getBackgroundSelectionColor());
				setForeground(((DefaultTreeCellRenderer) tree.getCellRenderer()).getTextSelectionColor());
			} else {
				setBackground(tree.getBackground());
				setForeground(((DefaultTreeCellRenderer) tree.getCellRenderer()).getTextNonSelectionColor());
			}
			super.paintComponent(g);
		}
		protected void setSelected(boolean value) {
			selected=value;
			if (value) {
				right.getViewport().setView(mapInfo);
				importRegion.setToolTipText(MapCreator.bundleCreator.getString("importregionroottip"));
				newRegion.setToolTipText(MapCreator.bundleCreator.getString("newregionroottip"));
			} else {
				importRegion.setToolTipText(MapCreator.bundleCreator.getString("importregiontip"));
				newRegion.setToolTipText(MapCreator.bundleCreator.getString("newregiontip"));
			}
			repaint();
		}
	};
	/**
	 * No errors in the map.
	 */
	protected static final int NO_ERRORS=0;
	/**
	 * Warnings in the map.
	 */
	protected static final int WARNINGS_IN_MAP=1;
	/**
	 * Errors in the map.
	 */
	protected static final int ERRORS_IN_MAP=2;

	Map map;
	private JPanel toolbar;
	private JTree tree;
	private MyJLabel mapInfoLabel;
	private JScrollPane right;
	/**
	 * The MapInfo panel.
	 */
	MapInfo mapInfo;
	/**
	 * Tallies Regions with RegionInfo panels.
	 */
	HashMap hashMap;
	protected JButton newRegion,deleteRegion,entryRegionT,importRegion,exportRegion,check;
	protected static ResourceBundle bundleCreator;
	protected static String globalDir;
	/**
	 * A very simple listener interface is used to inform the structures for changes in the attributes of the map.
	 */
	private ArrayList listeners;
	private ArrayList allLayers;
	private DBase db;

	{
		java.util.Locale locale=java.util.Locale.getDefault();
		//Localize
		bundleCreator=ResourceBundle.getBundle("gr.cti.eslate.mapModel.BundleCreator",locale);

		listeners=new ArrayList();
		allLayers=new ArrayList();
	}
}
