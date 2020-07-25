package gr.cti.eslate.mapModel;

//Java
import gr.cti.eslate.base.BadPlugAliasException;
import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.LeftMultipleConnectionProtocolPlug;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.SingleInputPlug;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.DBase;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.event.DatabaseAdapter;
import gr.cti.eslate.database.engine.event.DatabaseListener;
import gr.cti.eslate.database.engine.event.TableRemovedEvent;
import gr.cti.eslate.mapModel.geom.Point;
import gr.cti.eslate.mapModel.geom.PolyLine;
import gr.cti.eslate.mapModel.geom.Polygon;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Access;
import gr.cti.structfile.Copy;
import gr.cti.structfile.StructFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A Map implements a tree of Regions.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 6-Aug-1999
 * @see		gr.cti.eslate.mapModel.Region
 */
public class Map extends JPanel implements ESlatePart, Externalizable, TreeModel {
	/**
	 * Constructs a Map with a null region in the root.
	 */
	public Map() {
		super();
		attachTimers();
		PerformanceManager pm = PerformanceManager.getPerformanceManager();
		pm.constructionStarted(this);
		pm.init(constructorTimer);
		removeAll();
		//This listener listens to when a child is added to the component. If
		//the component is a database and no database is provided to the map, it becomes
		//its database.
		/*handle.addMembershipListener(new java.beans.beancontext.BeanContextMembershipListener() {
			public void childrenAdded(java.beans.beancontext.BeanContextMembershipEvent e) {
				Iterator it=e.iterator();
				while (it.hasNext() && db==null) {
					Object ch=it.next();
					if (ch instanceof DBase) {
						setDatabase((DBase) ch);
					}
				}
			}
			public void childrenRemoved(java.beans.beancontext.BeanContextMembershipEvent e) {}
		});*/

		RegionInfo.confirmSaveBack=true;
		menuCreated=false;
		isNewMap=true;
		saved=false;
		setName(bundleMessages.getString("mymap"));
		setPreferredSize(new Dimension(170,130));
		setLayout(new BorderLayout());
		setBorder(new gr.cti.eslate.utils.NoTopOneLineBevelBorder(javax.swing.border.BevelBorder.RAISED));
		borderChanged=false;
		//Menu
		menu=new JMenuBar();
		file=new JMenu(bundleMenu.getString("file"));
		file.addMouseListener((fml=new FileMenuListener()));
		menu.add(file);

		add(menu,BorderLayout.NORTH);

		pm.constructionEnded(this);
		pm.stop(constructorTimer);
		pm.displayTime(constructorTimer, bundleMessages.getString("ConstructorTimer"), "ms"); // GT
	}
	/**
	 * Constucts a Map with the specific root node.
	 */
	public Map(MapNode root) {
		this();
		this.root=root;
		root.setParent(null);
		root.setMap(this);
	}

	public MapView getMapView(int i) {
		return (MapView) views.get(i);
	}

	//javax.swing.tree.TreeModel method;
	public Object getRoot() {
		return root;
	}
	//javax.swing.tree.TreeModel method;
	public Object getChild(Object parent, int index) {
		return ((MapNode) parent).getChildAt(index);
	}
	//javax.swing.tree.TreeModel method;
	public int getChildCount(Object parent) {
		return ((MapNode) parent).getChildCount();
	}
	//javax.swing.tree.TreeModel method;
	public boolean isLeaf(Object node) {
		return ((MapNode) node).isLeaf();
	}
	//javax.swing.tree.TreeModel method;
	public void valueForPathChanged(TreePath path, Object newValue) {
		MutableTreeNode aNode=(MutableTreeNode) path.getLastPathComponent();
		aNode.setUserObject(newValue);
		nodeChanged(aNode);
	}
	//javax.swing.tree.TreeModel method;
	public int getIndexOfChild(Object parent, Object child) {
		return ((MapNode) parent).getIndex((TreeNode) child);
	}
	/**
	  * Invoke this method after you've changed how node is to be
	  * represented in the tree.
	  */
	public void nodeChanged(TreeNode node) {
		if(listenerList != null && node != null) {
			TreeNode parent=node.getParent();

			if(parent!=null) {
				int anIndex=parent.getIndex(node);
				if(anIndex!=-1) {
					int[] cIndexs=new int[1];
					cIndexs[0]=anIndex;
					nodesChanged(parent,cIndexs);
				}
			} else if (node==getRoot()) {
				nodesChanged(node,null);
			}
		}
	}
	/**
	  * Invoke this method after you've changed how the children identified by
	  * childIndicies are to be represented in the tree.
	  */
	public void nodesChanged(TreeNode node, int[] childIndices) {
		if (node!=null) {
			if (childIndices != null) {
				int cCount=childIndices.length;
				if (cCount>0) {
					Object[] cChildren=new Object[cCount];
					for(int counter=0;counter<cCount;counter++)
						cChildren[counter] = node.getChildAt(childIndices[counter]);
					fireTreeNodesChanged(this,getPathToRoot(node),childIndices,cChildren);
				}
			} else if (node==getRoot()) {
				fireTreeNodesChanged(this,getPathToRoot(node),null,null);
			}
		}
	}
	/**
	  * Invoke this method after you've inserted some TreeNodes into
	  * node.  childIndices should be the index of the new elements and
	  * must be sorted in ascending order.
	  */
	public void nodesWereInserted(TreeNode node, int[] childIndices) {
		if(listenerList != null && node != null && childIndices != null
		   && childIndices.length > 0) {
			int               cCount = childIndices.length;
			Object[]          newChildren = new Object[cCount];

			for(int counter = 0; counter < cCount; counter++)
				newChildren[counter] = node.getChildAt(childIndices[counter]);
			fireTreeNodesInserted(this, getPathToRoot(node), childIndices,
								  newChildren);
		}
	}
	/**
	  * Invoke this method after you've removed some TreeNodes from
	  * node.  childIndices should be the index of the removed elements and
	  * must be sorted in ascending order. And removedChildren should be
	  * the array of the children objects that were removed.
	  */
	public void nodesWereRemoved(TreeNode node, int[] childIndices,
								 Object[] removedChildren) {
		if(node != null && childIndices != null) {
			fireTreeNodesRemoved(this, getPathToRoot(node), childIndices,
								 removedChildren);
		}
	}
	/**
	 * Builds the parents of node up to and including the root node,
	 * where the original node is the last element in the returned array.
	 * The length of the returned array gives the node's depth in the
	 * tree.
	 *
	 * @param aNode the TreeNode to get the path for
	 * @return an array of TreeNodes giving the path from the root to the
	 *        specified node.
	 */
	public TreeNode[] getPathToRoot(TreeNode aNode) {
		return getPathToRoot(aNode, 0);
	}

	/**
	 * Builds the parents of node up to and including the root node,
	 * where the original node is the last element in the returned array.
	 * The length of the returned array gives the node's depth in the
	 * tree.
	 *
	 * @param aNode  the TreeNode to get the path for
	 * @param depth  an int giving the number of steps already taken towards
	 *        the root (on recursive calls), used to size the returned array
	 * @return an array of TreeNodes giving the path from the root to the
	 *         specified node
	 */
	protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
		TreeNode[]              retNodes;
	// This method recurses, traversing towards the root in order
	// size the array. On the way back, it fills in the nodes,
	// starting from the root and working back to the original node.

		/* Check for null, in case someone passed in a null node, or
		   they passed in an element that isn't rooted at root. */
		if(aNode == null) {
			if(depth == 0)
				return null;
			else
				retNodes = new TreeNode[depth];
		}
		else {
			depth++;
			if(aNode == root)
				retNodes = new TreeNode[depth];
			else
				retNodes = getPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}

	//
	//  Events
	//

	//javax.swing.tree.TreeModel method;
	/**
	 * Adds a listener for the TreeModelEvent posted after the tree changes.
	 *
	 * @see     #removeTreeModelListener
	 * @param   l       the listener to add
	 */
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(TreeModelListener.class, l);
	}

	//javax.swing.tree.TreeModel method;
	/**
	 * Removes a listener previously added with <B>addTreeModelListener()</B>.
	 *
	 * @see     #addTreeModelListener
	 * @param   l       the listener to remove
	 */
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(TreeModelListener.class, l);
	}

	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesChanged(Object source, Object[] path,
										int[] childIndices,
										Object[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TreeModelListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path,
										   childIndices, children);
				((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
			}
		}
	}

	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesInserted(Object source, Object[] path,
										int[] childIndices,
										Object[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TreeModelListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path,
										   childIndices, children);
				((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
			}
		}
	}

	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesRemoved(Object source, Object[] path,
										int[] childIndices,
										Object[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TreeModelListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path,
										   childIndices, children);
				((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
			}
		}
	}

	/*
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeStructureChanged(Object source, Object[] path,
										int[] childIndices,
										Object[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TreeModelListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new TreeModelEvent(source, path,
										   childIndices, children);
				((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
			}
		}
	}
	/**
	 * Invoke this method if you've modified the TreeNodes upon which this
	 * model depends.  The model will notify all of its listeners that the
	 * model has changed.
	 */
	public void reload() {
		reload(root);
	}
	/**
	 * Invoke this method if you've modified the TreeNodes upon which this
	 * model depends.  The model will notify all of its listeners that the
	 * model has changed below the node <code>node</code> (PENDING).
	 */
	public void reload(TreeNode node) {
		if (node!=null) {
			fireTreeStructureChanged(this, getPathToRoot(node), null, null);
		}
	}
	/**
	 * Invoked this to insert newChild at location index in parents children.
	 * This will then message nodesWereInserted to create the appropriate
	 * event. This is the preferred way to add children as it will create
	 * the appropriate event.
	 */
	public void insertNodeInto(MutableTreeNode newChild,
							   MutableTreeNode parent, int index){
		parent.insert(newChild, index);

		int[]           newIndexs = new int[1];

		newIndexs[0] = index;
		nodesWereInserted(parent, newIndexs);
	}

	/**

	 * Message this to remove node from its parent. This will message
	 * nodesWereRemoved to create the appropriate event. This is the
	 * preferred way to remove a node as it handles the event creation
	 * for you.
	 */
	public void removeNodeFromParent(MutableTreeNode node) {
		MutableTreeNode         parent = (MutableTreeNode)node.getParent();
		int[]            childIndex = new int[1];
		Object[]         removedArray = new Object[1];

		//Intermediate node
		if (parent!=null) {
			//Prior to deleting, we have to track all the subtree in order to delete it from the file
			trackDeletedNodes((MapNode) node);
			childIndex[0] = parent.getIndex(node);
			parent.remove(childIndex[0]);
		} else {
		//Root node
			//Prior to deleting, we have to track all the subtree in order to delete it from the file
			trackDeletedNodes(root);
			root=null;
		}
		removedArray[0] = node;

		nodesWereRemoved(parent, childIndex, removedArray);
	}
	void trackDeletedNodes(MapNode tn) {
		Enumeration e=tn.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			MapNode mn=(MapNode) e.nextElement();
			deletedRegions.add(mn.getRegion().getGUID());
			deletedNodes.add(mn.getGUID());
		}
	}
	void clearTreeModelListeners() {
		//Remove all tree model listeners. This is done because closing the Creator
		//results in keeping everything on memory because the creator is kept in the listener list.
		boolean cont=true;
		while (cont) {
			cont=false;
			Object[] listeners = listenerList.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==TreeModelListener.class) {
					removeTreeModelListener((TreeModelListener) listeners[i+1]);
					cont=true;
					break;
				}
			}
		}
	}
	protected void setMapRoot(MapNode root) {
		saved=false;
		this.root=root;
		root.setParent(null);
		root.setMap(this);
		PerformanceManager pm = PerformanceManager.getPerformanceManager();
		pm.reset(refreshTimer);
		pm.reset(openRegionTimer);
		pm.reset(regionReadFieldMapTimer);
		pm.reset(regionRestoreAttribsTimer);
		pm.reset(regionChangeDirTimer);
		pm.reset(regionBgrReadTimer);
		pm.reset(openLayerTimer);
		pm.reset(layerChangeDirTimer);
		pm.reset(layerReadFieldMapTimer);
		pm.reset(layerRestoreAttribsTimer);
		pm.reset(checkNLoadTimer);
		pm.reset(checkNLoadChangeDirTimer);
		pm.reset(checkNLoadReadObjectTimer);
		pm.reset(checkNLoadLoadObjectsTimer);
		//Rebuild region views
		for (int i=0;i<views.size();i++) {
			MapView view=(MapView) views.get(i);
			pm.start(refreshTimer);
			view.refresh();
			pm.stop(refreshTimer);
			((MapView) views.get(i)).brokerMapChanged();
		}
	}
	/**
	 * Data precision.
	 */
	public int getDataPrecision() {
		return precision;
	}
	/**
	 * Data precision.
	 */
	public void setDataPrecision(int p) throws Exception {
		if (p!=IMapView.SINGLE_PRECISION && p!=IMapView.DOUBLE_PRECISION)
			throw new IllegalArgumentException("Incorrect data precision parameter");
		if (p==precision)
			return;
		precision=p;
		if (precision==IMapView.SINGLE_PRECISION) {
			Enumeration e=getMapRoot().breadthFirstEnumeration();
			while (e.hasMoreElements()) {
				Layer[] layers=((MapNode) e.nextElement()).getRegion().getLayers();
				for (int i=0;i<layers.length;i++) {
					//Convert to SINGLE Precision
					if (layers[i].rtree.PRECISION==IMapView.DOUBLE_PRECISION) {
						RTree newtree=null;
						ArrayList objects=layers[i].getGeographicObjects(false);
						if (objects.get(0) instanceof Point.Double || objects.get(0) instanceof PolyLine.Double || objects.get(0) instanceof Polygon.Double)
							System.out.println(layers[i].getName()+" needs conversion!");
						else {
							System.out.println(layers[i].getName()+" does not need conversion!");
							continue;
						}
						gr.cti.eslate.protocol.GeographicObject newobj=null;
						if (layers[i] instanceof PointLayer) {
							//Convert Point Layers
							newtree=new PointRTree.Float();
							for (int o=0;o<objects.size();o++) {
								Point.Double old=(Point.Double) objects.get(o);
								newobj=new Point.Float(old.getX(),old.getY(),old.getID());
								newtree.insert(old.getX(),old.getY(),old.getX(),old.getY(),newobj);
							}
							((PointLayer) layers[i]).setRTree(newtree);
						} else if (layers[i] instanceof PolyLineLayer) {
							//Convert PolyLine Layers
							newtree=new PolyLineRTree.Float();
							for (int o=0;o<objects.size();o++) {
								PolyLine.Double old=(PolyLine.Double) objects.get(o);
								//Copy parts
								int[] newparts=new int[old.parts.length];
								System.arraycopy(old.parts,0,newparts,0,newparts.length);
								//Copy points
								float[] newpoints=new float[old.points.length];
								for (int k=0;k<newpoints.length;k++)
									newpoints[k]=(float) old.points[k];
								//Create object
								newobj=new PolyLine.Float(old.getID(),newparts,newpoints,(float)old.getBoundingMinX(),(float)old.getBoundingMinY(),(float)old.getBoundingMaxX(),(float)old.getBoundingMaxY());
								newtree.insert(old.getBoundingMinX(),old.getBoundingMinY(),old.getBoundingMaxX(),old.getBoundingMaxY(),newobj);
							}
							((PolyLineLayer) layers[i]).setRTree(newtree);
						} else if (layers[i] instanceof PolygonLayer) {
							//Convert Polygon Layers
							newtree=new PolygonRTree.Float();
							for (int o=0;o<objects.size();o++) {
								Polygon.Double old=(Polygon.Double) objects.get(o);
								//Copy parts
								int[] newparts=new int[old.parts.length];
								System.arraycopy(old.parts,0,newparts,0,newparts.length);
								//Copy points
								float[] newpoints=new float[old.points.length];
								for (int k=0;k<newpoints.length;k++)
									newpoints[k]=(float) old.points[k];
								//Create object
								newobj=new Polygon.Float(old.getID(),newparts,newpoints,(float)old.getBoundingMinX(),(float)old.getBoundingMinY(),(float)old.getBoundingMaxX(),(float)old.getBoundingMaxY());
								newtree.insert(old.getBoundingMinX(),old.getBoundingMinY(),old.getBoundingMaxX(),old.getBoundingMaxY(),newobj);
							}
							((PolygonLayer) layers[i]).setRTree(newtree);
						}
					}
				}
			}
		} else {
			Enumeration e=getMapRoot().breadthFirstEnumeration();
			while (e.hasMoreElements()) {
				Layer[] layers=((MapNode) e.nextElement()).getRegion().getLayers();
				for (int i=0;i<layers.length;i++) {
					//Convert to SINGLE Precision
					if (layers[i].rtree.PRECISION==IMapView.SINGLE_PRECISION) {
						RTree newtree=null;
						ArrayList objects=layers[i].getGeographicObjects(false);
						gr.cti.eslate.protocol.GeographicObject newobj=null;
						if (layers[i] instanceof PointLayer) {
							//Convert Point Layers
							newtree=new PointRTree.Double();
							for (int o=0;o<objects.size();o++) {
								Point.Float old=(Point.Float) objects.get(o);
								newobj=new Point.Double(old.getX(),old.getY(),old.getID());
								newtree.insert(old.getX(),old.getY(),old.getX(),old.getY(),newobj);
							}
							((PointLayer) layers[i]).setRTree(newtree);
						} else if (layers[i] instanceof PolyLineLayer) {
							//Convert PolyLine Layers
							newtree=new PolyLineRTree.Double();
							for (int o=0;o<objects.size();o++) {
								PolyLine.Float old=(PolyLine.Float) objects.get(o);
								//Copy parts
								int[] newparts=new int[old.parts.length];
								System.arraycopy(old.parts,0,newparts,0,newparts.length);
								//Copy points
								double[] newpoints=new double[old.points.length];
								for (int k=0;k<newpoints.length;k++)
									newpoints[k]=old.points[k];
								//Create object
								newobj=new PolyLine.Double(old.getID(),newparts,newpoints,old.getBoundingMinX(),old.getBoundingMinY(),old.getBoundingMaxX(),old.getBoundingMaxY());
								newtree.insert(old.getBoundingMinX(),old.getBoundingMinY(),old.getBoundingMaxX(),old.getBoundingMaxY(),newobj);
							}
							((PolyLineLayer) layers[i]).setRTree(newtree);
						} else if (layers[i] instanceof PolygonLayer) {
							//Convert Polygon Layers
							newtree=new PolygonRTree.Double();
							for (int o=0;o<objects.size();o++) {
								Polygon.Float old=(Polygon.Float) objects.get(o);
								//Copy parts
								int[] newparts=new int[old.parts.length];
								System.arraycopy(old.parts,0,newparts,0,newparts.length);
								//Copy points
								double[] newpoints=new double[old.points.length];
								for (int k=0;k<newpoints.length;k++)
									newpoints[k]=old.points[k];
								//Create object
								newobj=new Polygon.Double(old.getID(),newparts,newpoints,old.getBoundingMinX(),old.getBoundingMinY(),old.getBoundingMaxX(),old.getBoundingMaxY());
								newtree.insert(old.getBoundingMinX(),old.getBoundingMinY(),old.getBoundingMaxX(),old.getBoundingMaxY(),newobj);
							}
							((PolygonLayer) layers[i]).setRTree(newtree);
						}
					}
				}
			}
		}
	}
	/**
	 * Gets the root of the map with no need to cast as in getRoot().
	 */
	public MapNode getMapRoot() {
		return root;
	}
	/**
	 * @return The node the map is contained.
	 */
	protected MapNode getNode(Region region) {
		if (root==null)
			return null;
		else
			return root.getNode(region);
	}
	/**
	 * Sets the entry region in a structure of regions.
	 */
	protected void setEntryRegion(Region region) {
		saved=false;
		MapNode old=entryNode;
		entryNode=getNode(region);
		previewMap();
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((MapView) views.get(i)).brokerMapEntryNodeChanged(old,entryNode);
	}
	/**
	 * Gets the entry region in a structure of regions or <code>this</code> for a stand-alone map.
	 */
	protected Region getEntryRegion() {
		if (entryNode==null)
			return root.getRegion();
		else
			return entryNode.getRegion();
	}
	/**
	 * Sets the starting MapNode in a structure of maps.
	 */
	protected void setEntryNode(MapNode node) {
		saved=false;
		MapNode old=entryNode;
		entryNode=node;
		previewMap();
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((MapView) views.get(i)).brokerMapEntryNodeChanged(old,entryNode);
	}
	/**
	 * Gets the starting MapNode in a structure of maps or <code>this</code> for a stand-alone map.
	 */
	protected MapNode getEntryNode() {
		if (entryNode==null)
			return root;
		else
			return entryNode;
	}
	/**
	 * Enables or disables the "edit" menu item.
	 */
	protected void setEditable(boolean value) {
		edit.setEnabled(value);
	}
	/**
	 * This method implements the special plug mechanism of this component.
	 * There should always be one free map plug in order for groups of viewers who
	 * need a different view to be connected.
	 */
	protected void createMapPlug(boolean firstConnectionWillBearNewPlug) {
		try {
			//The view is added to the Map automaticaly
			MapView view=new MapView(this);
			Class protocol=Class.forName("gr.cti.eslate.protocol.IMapTreeViewer");
			LeftMultipleConnectionProtocolPlug viewerPlug=new LeftMultipleConnectionProtocolPlug(handle,null,bundlePlug.getString("map")+(views.size())+")",new Color(112,0,64)/*CHANGEDISCONNECTIONLISTENERALSO!!!*/,protocol,view);
			viewerPlug.setNameLocaleIndependent("map"+(views.size()));
			if (firstConnectionWillBearNewPlug)
				viewerPlug.addConnectionListener(new ConnectionListener() {
					public void handleConnectionEvent(ConnectionEvent e) {
						//Create null map if there is no content upon conection
						if (getMapRoot()==null) {
							MapNode defRoot=new MapNode(new Region());
							setMapRoot(defRoot);
							setEntryNode(defRoot);
							for (int i=0;i<views.size();i++)
								((MapView) views.get(i)).refresh();
						}
						e.getOwnPlug().removeConnectionListener(this);
						createMapPlug(true);
					}
				});
			handle.addPlug(viewerPlug);
			//TimeMachine plug
			try {
				Class soClass=Class.forName("gr.cti.eslate.sharedObject.TimeMachineSO");
				Plug timeMachPlug=new SingleInputPlug(handle, bundlePlug, "timemachine", Color.white, soClass, view.getTimeMachineListener());
				timeMachPlug.addDisconnectionListener(view.getTimeMachineDisconnectionListener());
				viewerPlug.addPlug(timeMachPlug);
			} catch(Throwable e) {
				//No time-machine support
			}

		} catch(Exception e) {
			//No need to define the multiple exceptions. Nothing is done when a plug cannot be created.
			e.printStackTrace();
		}
	}
	/**
	 * @return The E-Slate handle.
	 */
	public ESlateHandle getESlateHandle() {
		if (handle==null) {
			//E-Slate stuff
			PerformanceManager pm = PerformanceManager.getPerformanceManager();
			pm.init(initESlateAspectTimer);
			pm.eSlateAspectInitStarted(this);

			handle=ESlate.registerPart(this);
			handle.addESlateListener(new HandleListener());
			String[] info={bundleInfo.getString("part"),bundleInfo.getString("development"),bundleInfo.getString("contribution"),bundleInfo.getString("copyright")};
			handle.setInfo(new ESlateInfo(bundleInfo.getString("compo")+" "+version,info));
			//Name
			if (getName()!=null)
				try {
					handle.setUniqueComponentName(name);
					name=handle.getComponentName();
				} catch(gr.cti.eslate.base.RenamingForbiddenException ex) {}
			//Plugs
			createMapPlug(true);
			pm.eSlateAspectInitEnded(this);
			pm.stop(initESlateAspectTimer); // GT
			pm.displayTime(initESlateAspectTimer, handle, "", "ms");
		}
		return handle;
	}
	/**
	 * Sets the name of the map. This also changes the E-Slate component name in a way like
	 * Map, Map_1, Map_2 etc.
	 */
	public void setName(String newName) {
		//Update only when there is a change
		if (((name!=null) && (name.equals(newName))) || ((name==null) && (newName==null)))
			return;
		String oldName=name;
		if (handle!=null)
			try {
				handle.setUniqueComponentName(newName);
				name=handle.getComponentName();
			} catch(gr.cti.eslate.base.RenamingForbiddenException ex) {
				name=newName;
			}
		else
			name=newName;
		saved=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((MapView) views.get(i)).brokerMapRenamed(oldName,name);
	}
	/**
	 * Gets the name of the map.
	 */
	public String getName() {
		return name;
	}
	/**
	 * toString()
	 */
	public String toString() {
		return name;
	}
	/**
	 * Creates a new view.
	 * @return The new view.
	 */
	public MapView addView() {
		//MapView constructor adds the view itself as a side-effect.
		//The Listeners need not to be informed because they are informed as a side-effect also.
		Object foo=(new MapView(this));
		return (MapView) views.get(views.size()-1);
	}
	/**
	 * Adds a view.
	 */
	protected void addView(MapView mv) {
		for (int i=0;i<views.size();i++)
			if (views.get(i)==mv) {
				return;
			}
		views.add(mv);
	}
	/**
	 * Gets the views of the map.
	 */
	protected ArrayList getViews() {
		return views;
	}
	/**
	 * Gets the views of the map.
	 */
	public MapView[] getMapViews() {
		MapView[] m=new MapView[views.size()];
		for (int i=0;i<views.size();i++)
			m[i]=(MapView) views.get(i);
		return m;
	}
	/**
	 * Sets the creation date of the map.
	 */
	protected void setCreationDate(String newDate) {
		//Update only when there is a change
		if (((date!=null) && (date.equals(newDate))) || ((date==null) && (newDate==null)))
			return;
		String old=date;
		date=newDate;
		saved=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((MapView) views.get(i)).brokerMapCreationDateChanged(old,date);
	}
	/**
	 * Gets the creation date of the map.
	 */
	public String getCreationDate() {
		return date;
	}
	/**
	 * Sets the author of the map.
	 */
	protected void setAuthor(String newAuthor) {
		//Update only when there is a change
		if (((author!=null) && (author.equals(newAuthor))) || ((author==null) && (newAuthor==null)))
			return;
		String old=author;
		author=newAuthor;
		saved=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((MapView) views.get(i)).brokerMapAuthorChanged(old,author);
	}
	/**
	 * Gets the creation date of the map.
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * Sets the comment-text of the map.
	 */
	protected void setComments(String newComments) {
		//Update only when there is a change.
		if (((comments!=null) && (comments.equals(newComments))) || ((comments==null) && (newComments==null)))
			return;
		//Lazily create comment structure
		if (jComments==null && newComments!=null && !newComments.equals("")) {
			//Comments
			JScrollPane scroll=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scroll.setBorder(new EmptyBorder(2,2,2,2));
			jComments=new JTextArea() {
				public void updateUI() {
					super.updateUI();
					this.setEditable(false);
					this.setOpaque(false);
					this.setLineWrap(true);
					this.setWrapStyleWord(true);
				}
			};
			scroll.getViewport().setView(jComments);
			add(scroll,BorderLayout.CENTER);
		}
		String old=comments;
		comments=newComments;
		if (jComments!=null) {
			jComments.setText(comments);
			saved=false;
			//Inform all Listeners.
			for (int i=0;i<views.size();i++)
				((MapView) views.get(i)).brokerMapCommentsChanged(old,comments);
		}
	}
	/**
	 * Gets the comment-text of the map.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * Gets the Database of this map.
	 */
	public DBase getDatabase() {
		return db;
	}
	/**
	 * Informs about replacing the database.
	 */
	public void setDatabase(DBase newDB) {
		if (newDB==db)
			return;
		//If the parent of the database is another component reject it.
		if (newDB!=null && newDB.getESlateHandle().getParentHandle()!=null && newDB.getESlateHandle().getParentHandle()!=handle)
			throw new IllegalArgumentException("The database set to the map already has a parent, so it cannot be a Map Database.");

		//Remove tables from layers.
		Enumeration e=getMapRoot().breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			Region r=((MapNode) e.nextElement()).getRegion();
			//A region is pending when it is being imported and the procedure has not finished yet.
			if (r.pending)
				continue;
			Layer[] l=r.getLayers();
			for (int i=0;i<l.length;i++)
				try {
					l[i].setTable(null);
				} catch(InvalidLayerDataException ex) {
					System.err.println("MAP#200101121406: Could not reset layer table after changing database.");
				}
		}
		e=null;

		DBase old=db;

		if (old!=null)
			handle.remove(old.getESlateHandle());
		db=newDB;

		//Listeners on db.
		if (db!=null) {
			db.setDBFile(null);
			if (dbList==null)
				dbList=new DatabaseMapListener();
			db.addDatabaseListener(dbList);
			//The handle of the database is immediately added to the component handle because a
			//map is always the "owner" of its database.
			handle.add(db.getESlateHandle());
		}

		saved=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((MapView) views.get(i)).brokerMapDatabaseChanged(old,db);
	}
	/**
	 * Sets the bounding rectangle of the map.
	 */
	protected void setBoundingRect(Rectangle2D newBoundRect) {
	   //Update only when there is a change.
		if (((boundRect!=null) && (boundRect.equals(newBoundRect))) || ((boundRect==null) && (newBoundRect==null)))
			return;
		java.awt.geom.Rectangle2D old=boundRect;
		boundRect=newBoundRect;
		saved=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((MapView) views.get(i)).brokerMapBoundingRectChanged(old,boundRect);
	}
	/**
	 * Gets the bounding rectangle of the map.
	 */
	public Rectangle2D getBoundingRect() {
		return boundRect;
	}
	/**
	 * Gets the larger scale maps depended on the current map.
	 */
	protected ZoomRect[] getInnerRegions(Region region) {
		MapNode look=region.getMapNode();
		if ((look==null) || (look.childrenList().size()==0)) return null;
		ZoomRect[] r=new ZoomRect[look.childrenList().size()];
		for (int i=0;i<look.childrenList().size();i++)
			r[i]=new ZoomRect((MapNode) look.childrenList().get(i));
		return r;
	}
	/**
	 * Gets the smaller scale map.
	 */
	protected ZoomRect getOuterRegion(Region region) {
		if (region.getMapNode().getParent()!=null)
			return new ZoomRect((MapNode) region.getMapNode().getParent());
		else
			return null;
	}
	/**
	 * Gets the larger scale map names depended on the given region.
	 */
	protected String[] getInnerRegionNames(Region region) {
		MapNode look=region.getMapNode();
		if ((look==null) || (look.childrenList().size()==0)) return null;
		String[] r=new String[look.childrenList().size()];
		for (int i=0;i<look.childrenList().size();i++)
			r[i]=new String(look.childrenList().get(i).toString());
		return r;
	}
	/**
	 * Scales-down the default image for the map and shows a preview.
	 */
	protected void previewMap() {
/*		Icon icn=null;
		if (getEntryRegion()!=null)
			icn=getEntryRegion().getDefaultBackground();
		if (icn!=null) {
			//Lazily create the preview panel
			if (preview==null) {
				//Map preview
				preview=new PreviewLabel();
				Dimension dim=new Dimension((int) PREVIEW_SIZE,(int) PREVIEW_SIZE);
				preview.setPreferredSize(dim);
				preview.setMaximumSize(dim);
				preview.setMinimumSize(dim);
				preview.setBorder(new EmptyBorder(2,2,2,2));
				add(preview,BorderLayout.WEST);
			}

			if (icn.getIconWidth()>icn.getIconHeight()) {
				preview.setIcon(icn,PREVIEW_SIZE/icn.getIconWidth(),PREVIEW_SIZE/icn.getIconWidth());
			} else {
				preview.setIcon(icn,PREVIEW_SIZE/icn.getIconHeight(),PREVIEW_SIZE/icn.getIconHeight());
			}
		} else
			preview.setIcon(null,1,1);
		preview.repaint();*/
	}
	/**
	 * Invoked by the MapCreator to inform when it is closing.
	 */
	protected void openCreator() {
		(new Thread() {
			public void run() {
				yield();
				setPriority(Thread.NORM_PRIORITY+1);
				edit.setEnabled(false);
				open.setEnabled(false);
				save.setEnabled(false);
				saveAs.setEnabled(false);
				javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
				setCursor(Helpers.waitCursor);
				if (creator!=null)
					creator.dispose();
				//Create null map
				if (getMapRoot()==null) {
					MapNode defRoot=new MapNode(new Region());
					setMapRoot(defRoot);
					setEntryNode(defRoot);
					for (int i=0;i<views.size();i++)
						((MapView) views.get(i)).refresh();
				}
				creator=new MapCreator(Map.this);
				Dimension s=Toolkit.getDefaultToolkit().getScreenSize();
				creator.setLocation((s.width-creator.getWidth())/2,(s.height-creator.getHeight())/2);
				creator.show();
				setCursor(Helpers.normalCursor);
			}
		}).start();
	}
	/**
	 * Invoked by the MapCreator to inform when it is closing.
	 */
	protected void creatorClosing(boolean modified) {
		creator=null;
		//Request the focus to avoid holding of the whole creator structure by the focus manager
		requestFocus();
		if (isNewMap && modified)
			isNewMap=false;
		if (isNewMap)
			open.setEnabled(true);
		edit.setEnabled(true);
		if (!openedMapFromMWD)
			save.setEnabled(true);
		saveAs.setEnabled(true);
		//close.setEnabled(true);
	}
	/**
	 * Menubar visibility.
	 */
	public void setMenubarVisible(boolean b) {
		if (b)
			add(menu,BorderLayout.NORTH);
		else
			remove(menu);
		revalidate();
	}
	/**
	 * Menubar visibility.
	 */
	public boolean isMenubarVisible() {
		Component[] c=getComponents();
		for (int i=0;i<c.length;i++)
			if (c[i].equals(menu))
				return true;
		return false;
	}
	/**
	 * Compatibility method which makes the plug aliases. Exists since the change in the
	 * database engine.
	 */
	private void setPlugAliases(DBase d) {
		//Set plug aliases
		ESlateMicroworld mwd=handle.getESlateMicroworld();
		try {
			//Alias for the database
			mwd.setPlugAliasForLoading(d.getESlateHandle().getPlugs()[0],handle,new String[]{"database"});
			//Aliasing for tables
			ArrayList tb=d.getTables();
			Table t;
			AbstractTableField tf;
			for (int i=0;i<tb.size();i++) {
				t=(Table) tb.get(i);
				mwd.setPlugAliasForLoading(t.getTablePlug(),handle,new String[]{t.getTitle(),"database"});
				//Aliasing for table current record
				mwd.setPlugAliasForLoading(t.getTablePlug().getRecordPlug(),handle,new String[]{"record of "+t.getTitle(),t.getTitle(),"database"});
				//Aliasing for table fields
				for (int j=0;j<t.getColumnCount();j++) {
					try {
						tf=t.getTableField(j);
						mwd.setPlugAliasForLoading(t.getTablePlug().getRecordPlug().getPlug(tf.getName()),handle,new String[]{tf.getName(),"record of "+t.getTitle(),t.getTitle(),"database"});
					} catch(InvalidFieldIndexException ex) {
						System.err.println("MAP#200101121631: Field does not exist.");
					}
				}
			}
		} catch(BadPlugAliasException e) {
			System.err.println("MAP#200101121604: Bad plug alias.");
		}
	}
	/**
	 * This method creates and adds a PerformanceListener to the E-Slate's
	 * Performance Manager. The PerformanceListener attaches the component's
	 * timers when the Performance Manager becomes enabled.
	 */
	private void createPerformanceManagerListener(PerformanceManager pm){
		  if (perfListener == null) {
			 perfListener = new PerformanceAdapter() {
				public void performanceManagerStateChanged(PropertyChangeEvent e){
				   boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
				   // When the Performance Manager is enabled, try to attach the
				   // timers.
				   if (enabled) {
					  attachTimers();
				   }
				}
			 };
			 pm.addPerformanceListener(perfListener);
		  }
	}
	/**
	 * This method creates and attaches the component's timers. The timers are
	 * created only once and are assigned to global variables. If the timers
	 * have been already created, they are not re-created. If the timers have
	 * been already attached, they are not attached again.
	 * This method does not create any timers while the PerformanceManager is
	 * disabled.
	 */
	private void attachTimers(){
		PerformanceManager pm = PerformanceManager.getPerformanceManager();
		boolean pmEnabled = pm.isEnabled();

		// If the performance manager is disabled, install a listener which will
		// re-invoke this method when the performance manager is enabled.
		if (!pmEnabled && (perfListener == null)) {
			createPerformanceManagerListener(pm);
		}

		// Do nothing if the PerformanceManager is disabled.
		if (!pmEnabled) {
			return;
		}

		boolean timersCreated = (loadTimer != null);
		// If the timers have already been constructed and attached, there is
		// nothing to do.
		if (!timersCreated) {
			// If the component's timers have not been constructed yet, then
			// construct them. During constuction, the timers are also attached.
			// Get the performance timer group for this component.
			PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);

			// Construct and attach the component's timers.
		  constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			compoTimerGroup, bundleMessages.getString("ConstructorTimer"), true
		  );
		  loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			compoTimerGroup, bundleMessages.getString("LoadTimer"), true
		  );
		  saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			compoTimerGroup, bundleMessages.getString("SaveTimer"), true
		  );
		  initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			compoTimerGroup, bundleMessages.getString("InitESlateAspectTimer"), true
		  );
		  pm.registerPerformanceTimerGroup(
			PerformanceManager.CONSTRUCTOR, constructorTimer, this
		  );
		  pm.registerPerformanceTimerGroup(
			PerformanceManager.LOAD_STATE, loadTimer, this
		  );
		  pm.registerPerformanceTimerGroup(
			PerformanceManager.SAVE_STATE, saveTimer, this
		  );
		  pm.registerPerformanceTimerGroup(
			PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
		  );
			 performOpenTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   loadTimer, bundleMessages.getString("PerformOpenTimer"), true
			 );
			 mapReadStreamTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   performOpenTimer, bundleMessages.getString("MapReadStreamTimer"), true
			 );
			 mapReadFieldMapTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   mapReadStreamTimer, bundleMessages.getString("MapReadFieldMapTimer"), true
			 );

			 openSubTreeTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   performOpenTimer, bundleMessages.getString("OpenSubTreeTimer"), true
			 );
			 setMapRootTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   performOpenTimer, bundleMessages.getString("SetMapRootTimer"), true
			 );
			 readUnusedLayersTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   performOpenTimer, bundleMessages.getString("ReadUnusedLayersTimer"), true
			 );

			 refreshTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   setMapRootTimer, bundleMessages.getString("RefreshTimer"), true
			 );
			 openRegionTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   refreshTimer, bundleMessages.getString("OpenRegionTimer"), true
			 );

			 regionReadFieldMapTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   openRegionTimer, bundleMessages.getString("RegionReadFieldMapTimer"), true
			 );
			 regionRestoreAttribsTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   openRegionTimer, bundleMessages.getString("RegionRestoreAttribsTimer"), true
			 );
			 regionChangeDirTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   openRegionTimer, bundleMessages.getString("RegionChangeDirTimer"), true
			 );
			 regionBgrReadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   compoTimerGroup, bundleMessages.getString("RegionBgrReadTimer"), true
			 );

			 openLayerTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   compoTimerGroup, bundleMessages.getString("OpenLayerTimer"), true
			 );
			 layerChangeDirTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   openLayerTimer, bundleMessages.getString("LayerChangeDirTimer"), true
			 );
			 layerReadFieldMapTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   openLayerTimer, bundleMessages.getString("LayerReadFieldMapTimer"), true
			 );
			 layerRestoreAttribsTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   openLayerTimer, bundleMessages.getString("LayerRestoreAttribsTimer"), true
			 );
			 checkNLoadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   layerRestoreAttribsTimer, bundleMessages.getString("CheckNLoadTimer"), true
			 );

			 checkNLoadChangeDirTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   checkNLoadTimer, bundleMessages.getString("CheckNLoadChangeDirTimer"), true
			 );
			 checkNLoadReadObjectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   checkNLoadTimer, bundleMessages.getString("CheckNLoadReadObjectTimer"), true
			 );
			 checkNLoadLoadObjectsTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
			   checkNLoadTimer, bundleMessages.getString("CheckNLoadLoadObjectsTimer"), true
			 );

	  }
	}
	/**
	 * Used for saving.
	 */
	protected boolean isSaved() {
		boolean decision=isNewMap || (((db!=null)?(saved && !db.isModified()):saved) && deletedLayers.size()==0 && deletedRegions.size()==0 && deletedNodes.size()==0);
		//See if any of the unused layers needs saving
		for (int i=0;i<unusedLayers.size() && decision;i++)
			decision=decision && ((Layer) unusedLayers.get(i)).isSaved();
		return decision;
	}
	/**
	 * Used for saving.
	 */
	boolean isEverythingSaved() {
		if (isNewMap)
			return true;
		if (!isSaved())
			return false;
		//Write the regions. Layers will be automatically saved.
		Enumeration e=getMapRoot().breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			Region r=((MapNode) e.nextElement()).getRegion();
			if (!r.isSaved())
				return false;
			Layer[] l=r.getLayers();
			for (int i=0;i<l.length;i++)
				if (!l[i].isSaved())
					return false;
		}
		return true;
	}

	//START OF BROKER METHODS
	//These methods broadcast the given event to all the viewers.
	protected void brokerMapRegionAdded(Region r) {
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((MapView) views.get(i)).brokerMapRegionAdded(r);
	}
	//
	protected void brokerMapRegionRemoved(Region r) {
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((MapView) views.get(i)).brokerMapRegionRemoved(r);
	}
	//END OF BROKER METHODS


	/**
	 * Saves the map general info to the disk, if it has been modified.
	 * @return True if the map general info has been modified and saved, false otherwise.
	 */
	protected boolean saveMap(Access saveAccess) throws IOException {
		if (!isSaved()) {
			//Delete from the file the deleted layers
			if (deletedLayers.size()>0) {
				try {
					saveAccess.changeToRootDir();
					saveAccess.changeDir("layers");
					for (int i=0;i<deletedLayers.size();i++) {
						try {
							saveAccess.deleteFile((String) deletedLayers.get(i));
						} catch(Exception ex) {
							//The layer has never been saved.
						}
					}
				} catch(Exception e) {
					//No layers are saved.
				} finally {
					deletedLayers.clear();
				}
			}
			//Delete from the file the deleted regions
			if (deletedRegions.size()>0) {
				try {
					saveAccess.changeToRootDir();
					saveAccess.changeDir("regions");
					for (int i=0;i<deletedRegions.size();i++) {
						try {
							saveAccess.deleteFile((String) deletedRegions.get(i));
						} catch(Exception ex) {
							//The region has never been saved.
						}
					}
				} catch(Exception e) {
					//No regions are saved.
				} finally {
					deletedRegions.clear();
				}
			}
			//Delete from the file the deleted nodes
			if (deletedNodes.size()>0) {
				try {
					saveAccess.changeToRootDir();
					saveAccess.changeDir("nodes");
					for (int i=0;i<deletedNodes.size();i++) {
						try {
							saveAccess.deleteFile((String) deletedNodes.get(i));
						} catch(Exception ex) {
							//The node has never been saved.
						}
					}
				} catch(Exception e) {
					//No nodes are saved.
				} finally {
					deletedNodes.clear();
				}
			}
			saveAccess.changeToRootDir();
			//Write the map tree general information
			writeStream(new BufferedOutputStream(saveAccess.openOutputFile("map")));
			for (int i=0;i<unusedLayers.size();i++)
				saveLayer(saveAccess,(Layer) unusedLayers.get(i));
			saved=true;
			return true;
		} else {
			//It is essential to save the map no matter if it has changed because
			//the database children will not be saved by the platform otherwise.
			saveAccess.changeToRootDir();
			//Write the map tree general information
			writeStream(new BufferedOutputStream(saveAccess.openOutputFile("map")));
		}
		return false;
	}
	/**
	 * Opens a subtree from the disk.
	 * @return The root of the subtree.
	 */
	protected MapNode openSubTree(Access openAccess,String guid) throws ClassNotFoundException,IOException {
		openAccess.changeToRootDir();
		openAccess.changeDir("nodes");
		openAccess.changeDir(guid);
		MapNode n=new MapNode();
		n.readStream(openAccess,new ObjectInputStream(new BufferedInputStream(openAccess.openInputFile("node"))));
		return n;
	}
	/**
	 * Saves a node to the disk, if it has been modified.
	 * @return True if the node has been modified and saved, false otherwise.
	 */
	protected boolean saveNode(Access saveAccess,MapNode n) throws IOException {
		if (!n.isSaved()) {
			saveAccess.changeToRootDir();
			if (!saveAccess.fileExists("nodes"))
				saveAccess.createDirectory("nodes");
			saveAccess.changeDir("nodes");
			if (!saveAccess.fileExists(n.getGUID()))
				saveAccess.createDirectory(n.getGUID());
			saveAccess.changeDir(n.getGUID());
			ObjectOutputStream oout=new ObjectOutputStream(new BufferedOutputStream(saveAccess.openOutputFile("node")));
			n.writeStream(saveAccess,oout);
			oout.close();
			return true;
		}
		return false;
	}
	/**
	 * Opens a region from the disk.
	 * @return The region requested.
	 */
	protected Region openRegion(Access openAccess,String guid,MapNode node) throws ClassNotFoundException,IOException {
		openAccess.changeToRootDir();
		openAccess.changeDir("regions");
		openAccess.changeDir(guid);
		Region region=new Region();
		region.setMapNode(node);
		region.setSF(openAccess);
		region.readStream(openAccess,this);
		return region;
	}
	/**
	 * Saves a region to the disk, if it has been modified, to the default save stream.
	 * @return True if the region has been modified and saved, false otherwise.
	 */
	protected boolean saveRegion(Region r) throws IOException {
		return saveRegion(saveAccess,r);
	}
	/**
	 * Saves a region to the disk, if it has been modified.
	 * @return True if the region has been modified and saved, false otherwise.
	 */
	protected boolean saveRegion(Access saveAccess,Region r) throws IOException {
		if (!r.isSaved()) {
			saveAccess.changeToRootDir();
			if (!saveAccess.fileExists("regions"))
				saveAccess.createDirectory("regions");
			saveAccess.changeDir("regions");
			if (!saveAccess.fileExists(r.getGUID()))
				saveAccess.createDirectory(r.getGUID());
			saveAccess.changeDir(r.getGUID());
			r.writeStream(saveAccess,this);
			return true;
		} else {
			Layer[] lrs=r.getLayers();
			for (int i=0;i<lrs.length;i++)
				saveLayer(saveAccess,lrs[i]);//,false);
			return false;
		}
	}
	/**
	 * Opens a layer from the disk.
	 * @return The layer requested.
	 */
	protected Layer openLayer(Access openAccess,String guid) throws ClassNotFoundException,IOException {
		if (openedLayers.containsKey(guid.substring(2)))
			return (Layer) openedLayers.get(guid.substring(2));
		PerformanceManager pm = PerformanceManager.getPerformanceManager();
		pm.start(layerChangeDirTimer);

		pm.start(openLayerTimer);
		openAccess.changeToRootDir();
		openAccess.changeDir("layers");
		openAccess.changeDir(guid);
		pm.stop(layerChangeDirTimer);
		Layer lr=null;
		String lrType=guid.substring(0,2);
		if (lrType.equals(PointLayer.TYPE_ID))
			lr=new PointLayer(openAccess,getDataPrecision());
		else if (lrType.equals(PolyLineLayer.TYPE_ID))
			lr=new PolyLineLayer(openAccess,getDataPrecision());
		else if (lrType.equals(PolygonLayer.TYPE_ID))
			lr=new PolygonLayer(openAccess,getDataPrecision());
		else if (lrType.equals(RasterLayer.TYPE_ID))
			lr=new RasterLayer(openAccess);
		if (VectorLayer.class.isAssignableFrom(lr.getClass()))
			((VectorLayer) lr).setTimers(this);
		lr.readStream(openAccess,this);
		openedLayers.put(guid.substring(2),lr);
		pm.stop(openLayerTimer);
		return lr;
	}
	/**
	 * Gets an opened layer by its guid.
	 */
	protected Layer getOpenedLayer(String guid) {
		if (guid==null)
			return null;
		return (Layer) openedLayers.get(guid.substring(2));
	}
	/**
	 * Saves a layer to the disk, if it has been modified.
	 * @return True if the layer has been modified and saved, false otherwise.
	 */
	protected boolean saveLayer(Access saveAccess,Layer l) throws IOException {
		if (savedLayers==null /*Saving a region alone*/ || savedLayers.contains(l)) return false;
		if (!l.isSaved()) {
			//Write the layer
			saveAccess.changeToRootDir();
			if (!saveAccess.fileExists("layers"))
				saveAccess.createDirectory("layers");
			saveAccess.changeDir("layers");
			if (!saveAccess.fileExists(l.getGUID()))
				saveAccess.createDirectory(l.getGUID());
			saveAccess.changeDir(l.getGUID());
			l.writeStream(saveAccess);
			savedLayers.add(l);
			return true;
		}
		return false;
	}

	/**
	 * Implements the opening process.
	 */
	public void performOpen() {
		final ESlateFileDialog fd=new ESlateFileDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,this),bundleMenu.getString("opentitle"),ESlateFileDialog.LOAD,MAP_SUFFIX);
		if ((mapDirectory!=null) && !(mapDirectory.equals("")))
			fd.setDirectory(mapDirectory);
		fd.setFile("*"+MAP_SUFFIX);
		fd.show();
		if (fd.getDirectory()!=null)
			try {
				setCursor(Helpers.waitCursor);
				JPanel p=new JPanel();
				JLabel l=new JLabel(bundleMessages.getString("loadingmap"));
				l.setForeground(java.awt.SystemColor.controlLtHighlight);
				p.add(l);
				add(p,BorderLayout.SOUTH);
				validate();
				try {
					Object me=handle.getESlateMicroworld().getMicroworldEnvironment();
					if (me instanceof JComponent)
						((JComponent) me).paintImmediately(((JComponent) me).getVisibleRect());
				} catch(Throwable th) {
					//Cannot repaint the container to fill the hole left by the file dialog.
				}
				Access tempOpen=openAccess;
				openAccess=new StructFile(fd.getDirectory()+fd.getFile(),StructFile.OLD);
				performOpen(openAccess);
				updateSFField(openAccess);
				mapDirectory=fd.getDirectory();
				mapFile=fd.getFile();
				try {
					if ((tempOpen!=null) && (tempOpen instanceof StructFile))
						((StructFile) tempOpen).close();
				} catch(Exception e) {}
				try {
					if ((saveAccess!=null) && (saveAccess instanceof StructFile))
						((StructFile) saveAccess).close();
				} catch(Exception e) {}
				saveAccess=openAccess;
				remove(p);
				setCursor(null);
			} catch(Exception ex) {
			   JOptionPane.showMessageDialog(Map.this,bundleMessages.getString("cantopen"),"",JOptionPane.ERROR_MESSAGE);
			   System.err.println("Information message follows. YOU MAY CONTINUE WORKING!");
			   ex.printStackTrace();
			   System.err.println("Map Component: YOU MAY CONTINUE WORKING!");
			};
	}

//////////////////////////////BEGINNING OF ADDED CODE BY N\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	/**
	 * Method used to open a file using a File (N)
	 *
	 */

	 public void performOpen(File file){
		if (mapFile!=null){
			throw new RuntimeException("Model already uses a map file. No file opening allowed");
		}

		if (!file.exists() || file.isDirectory()){
		   System.err.println("Map Component: Map file not found!");
		   return;
		}else if (file.isFile()){ //!file.getName().equals(mapFile) && !file.getParentFile().getPath().equals(mapDirectory)){
			mapFile=file.getName();
			mapDirectory = file.getParentFile().getPath();
			Access tempOpen=openAccess;
			File dir = file.getParentFile();
			try{
			  openAccess=new StructFile(dir.getPath()+"\\"+file.getName(),StructFile.OLD);
			   performOpen(openAccess);
			}catch (IOException exc){System.out.println("Error in opening file!");
			}catch (ClassNotFoundException exc2){exc2.printStackTrace();}
			updateSFField(openAccess);
			try {
			   if ((tempOpen!=null) && (tempOpen instanceof StructFile))
				  ((StructFile) tempOpen).close();
			} catch(Exception e) {}
			try {
			   if ((saveAccess!=null) && (saveAccess instanceof StructFile))
				  ((StructFile) saveAccess).close();
			} catch(Exception e) {}
			saveAccess=openAccess;
			setCursor(null);
			open.setEnabled(false);
	   }

	}

	/**
	 * Method used to open a file directly from disk using a path (N)
	 *
	 */

	 public void performOpen(String fileName){
		if (mapFile!=null){
			throw new RuntimeException("Model already uses a map file. No file opening allowed");
		}
		File file = new File(fileName);
		if (!file.exists() || file.isDirectory()){
		   System.err.println("Map Component: Map file not found!");
		   return;
		}else if (file.isFile()){ //!file.getName().equals(mapFile) && !file.getParentFile().getPath().equals(mapDirectory)){
			mapFile=file.getName();
			mapDirectory = file.getParentFile().getPath();
			Access tempOpen=openAccess;
			File dir = file.getParentFile();
			try{
			  openAccess=new StructFile(dir.getPath()+"\\"+file.getName(),StructFile.OLD);
			   performOpen(openAccess);
			}catch (IOException exc){
				System.out.println("Error in opening file!");
			}catch (ClassNotFoundException exc2){exc2.printStackTrace();}
			updateSFField(openAccess);
			try {
			   if ((tempOpen!=null) && (tempOpen instanceof StructFile))
				  ((StructFile) tempOpen).close();
			} catch(Exception e) {}
			try {
			   if ((saveAccess!=null) && (saveAccess instanceof StructFile))
				  ((StructFile) saveAccess).close();
			} catch(Exception e) {}
			saveAccess=openAccess;
			setCursor(null);
			open.setEnabled(false);
	   }

	}

//////////////////////////////END OF ADDED CODE BY N\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\




	/**
	 * Implements the opening process from a structured file.
	 */
	public void performOpen(Access sf) throws ClassNotFoundException,IOException {
		setCursor(Helpers.waitCursor);
		//Open file, caching the values in order to replace them in case of failure.
		String cName=name;
		Rectangle2D cRect=boundRect;
		String cDate=date;
		String cAuthor=author;
		String cComments=comments;
		DBase cDatabase=db;
		MapNode cEntryNode=entryNode;

		//Read map tree general information
		try {
			sf.changeToRootDir();
			String rootNode=readStream(new BufferedInputStream(sf.openInputFile("map")));
			MapNode newRoot=openSubTree(sf,rootNode);
			//Everything has been successful! Set the database and the root.
			//Database set here to avoid destroying the plugs if loading is not successful.
			setDatabase(db);
			//Set Entry Node.
			Enumeration e=newRoot.breadthFirstEnumeration();
			int cn=0;
			while (cn++<entryndno)
				e.nextElement();
			entryNode=(MapNode) e.nextElement();
			//This must be done after setting the entry node, for the MapView objects
			//to have the correct entry region information.
			setMapRoot(newRoot);
			//Calculate depth and find guids
			e=newRoot.breadthFirstEnumeration();
			HashMap hm=new HashMap();
			while (e.hasMoreElements()) {
				MapNode mn=(MapNode) e.nextElement();
				mn.calculateDepth();
				hm.put(mn.getGUID(),mn);
			}
			//Do links
			e=newRoot.breadthFirstEnumeration();
			while (e.hasMoreElements()) {
				MapNode mn=(MapNode) e.nextElement();
				if (mn.hasLinks()) {
					for (int i=0;i<mn.getNumberOfLinks();i++) {
						String lmn=(String) mn.getLink(i);
						MapNode lkd=(MapNode)hm.get(lmn);
						//May be null when the node has been deleted
						if (lkd!=null) {
							mn.setLink(i,lkd);
							lkd.setLinkedTo(mn,false);
						}
					}
				}
			}
			//Open unused layers
			unusedLayers.clear();
			if (lids!=null) {
				for (int i=0;i<lids.size();i++)
					unusedLayers.add(openLayer(sf,(String) lids.get(i)));
			}
			updateSFField(sf);
			previewMap();
			//Set menu elements enabled or disabled.
			if (edit!=null)
				edit.setEnabled(true);
			if (open!=null)
				open.setEnabled(false);
			if (!openedMapFromMWD && save!=null)
				save.setEnabled(true);
			if (saveAs!=null)
				saveAs.setEnabled(true);
			saved=true;
			isNewMap=false;
		} catch(ClassNotFoundException e1) {
			name=cName;
			boundRect=cRect;
			date=cDate;
			author=cAuthor;
			comments=cComments;
			db=cDatabase;
			entryNode=cEntryNode;
			setCursor(Helpers.normalCursor);
			throw e1;
		} catch(IOException e2) {
			name=cName;
			boundRect=cRect;
			date=cDate;
			author=cAuthor;
			comments=cComments;
			db=cDatabase;
			entryNode=cEntryNode;
			setCursor(Helpers.normalCursor);
			throw e2;
		}
		setCursor(Helpers.normalCursor);
	}
	/**
	 * Implements the saving process in a structured file.
	 */
	public void performSave(Access sf) throws IOException {
		setCursor(Helpers.waitCursor);
		//Close the creator. If the creator is open, it is not guaranteed that all the data will
		//be written in the file!!!
		if (creator!=null) {
			creator.dispose();
			creator=null;
		}

		//First copy the existing file, if any.
		if (openAccess!=null && openAccess!=sf) {
			openAccess.changeToRootDir();
			sf.changeToRootDir();
			Copy.copy(openAccess,sf);
		}

		//Then save the changes
		//This array list prevents a layer from saving multiple times
		savedLayers=new ArrayList();
		//Write the map tree general information
		saveMap(sf);
		//Write the map tree node information
		Enumeration e=getMapRoot().breadthFirstEnumeration();
		while (e.hasMoreElements())
			saveNode(sf,(MapNode) e.nextElement());
		//Write the regions. Layers will be automatically saved.
		e=getMapRoot().breadthFirstEnumeration();
		while (e.hasMoreElements())
			saveRegion(sf,((MapNode) e.nextElement()).getRegion());
		//Done
		savedLayers=null;

		//This line was added because of a bug which came up after a new Structfile version.
		//Saved .esm files were corrupted and impossible to open (N).
		sf.flushCache();

		setCursor(Helpers.normalCursor);
	}
	/**
	 * Implements the "save as" process.
	 */
	public void performSaveAs() {
		ESlateFileDialog fd=new ESlateFileDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,this),bundleMenu.getString("savetitle"),ESlateFileDialog.SAVE,MAP_SUFFIX);
		if ((mapDirectory!=null) && !(mapDirectory.equals(""))) {
			fd.setDirectory(mapDirectory);
			fd.setFile(mapFile);
		}
		fd.show();
		if (fd.getDirectory()!=null && fd.getFile()!=null) {
			JPanel p=null;
			try {
				setCursor(Helpers.waitCursor);
				p=new JPanel();
				JLabel l=new JLabel(bundleMessages.getString("savingmap"));
				l.setForeground(java.awt.SystemColor.controlLtHighlight);
				p.add(l);
				add(p,BorderLayout.SOUTH);
				validate();
				try {
					Object me=handle.getESlateMicroworld().getMicroworldEnvironment();
					if (me instanceof JComponent)
						((JComponent) me).paintImmediately(((JComponent) me).getVisibleRect());
				} catch(Throwable th) {
					//Cannot repaint the container to fill the hole left by the file dialog.
				}
				String file=fd.getFile();
				if ((file.length()<4) || !(file.substring(file.length()-4).equalsIgnoreCase(MAP_SUFFIX)))
					file=file+MAP_SUFFIX;
				try {
					if (saveAccess!=null && saveAccess!=openAccess && (saveAccess instanceof StructFile)) {
						((StructFile) saveAccess).close();
					}
				} catch(Exception e) {}
				saveAccess=new StructFile(fd.getDirectory()+file,StructFile.NEW);
				performSave(saveAccess);
				try {
					if ((openAccess!=null) && (openAccess instanceof StructFile)) {
						((StructFile) openAccess).close();
					}
				} catch(Exception e) {}
				mapDirectory=fd.getDirectory();
				mapFile=file;
				//Defragment.defragment(mapDirectory+mapFile,mapDirectory+mapFile+".tmp");
				//Close the file in order to defragment it
				//((StructFile) sf).close();
				//Reopen it
				//sf=new StructFile(fd.getDirectory()+file,StructFile.OLD);
				if (!openedMapFromMWD)
					updateSFField(saveAccess);
				openAccess=saveAccess;
			} catch(Exception ex) {
			   JOptionPane.showMessageDialog(Map.this,bundleMessages.getString("cantsave"),"",JOptionPane.ERROR_MESSAGE);
			   ex.printStackTrace();
			} finally {
				remove(p);
				setCursor(null);
			}
		}
		revalidate();
	}
	/**
	 * Informs the internal structures about a change in the structfile.
	 */
	private void updateSFField(Access sf) {
		Enumeration e=getMapRoot().breadthFirstEnumeration();
		while (e.hasMoreElements())
			((MapNode) e.nextElement()).setSF(sf);
	}
	/**
	 * Given an input stream this method loads the whole map tree.
	 * @return The UID of the root node.
	 */
	public String readStream(InputStream in) throws ClassNotFoundException, IOException {
		//Save Format Version 1: DB is saved as a regular object in the ESlateFieldMap.
		//Save Format Version 2: DB is saved using saveChildObjects.
		ObjectInputStream oin=new ObjectInputStream(in);
		StorageStructure ht=(StorageStructure) oin.readObject();
		setName(ht.get("name",""));
		//There has been a change from Rectangle to Rectangle2D
		Object brect=ht.get("boundRect");
		if (brect==null)
			setBoundingRect(null);
		else if (brect instanceof Rectangle2D)
			setBoundingRect((Rectangle2D) brect);
		else if (brect instanceof Rectangle)
			setBoundingRect(new Rectangle2D.Double(((Rectangle)brect).x,((Rectangle)brect).y,((Rectangle)brect).width,((Rectangle)brect).height));
		setCreationDate(ht.get("date",""));
		setAuthor(ht.get("author",""));
		setComments(ht.get("comments",""));
		if (ht.getDataVersion()!=null && ht.getDataVersion().startsWith("1.")) {
			if (ht.containsKey("db")) {
				boolean converted=false;
				//Remains after the big change in db engine, from CDatabase to DBase
				Object tmpdb=ht.get("db");
				if (tmpdb!=null && !(tmpdb instanceof DBase)) {
					db=new DBase(tmpdb);
					converted=true;
				} else
					db=(DBase) tmpdb;
				if (db!=null) {
					//Add the DBase component to my handle
					ESlateHandle dbhandle=db.getESlateHandle();
					if (dbhandle==null)
						dbhandle=ESlate.registerPart(db);
					handle.add(dbhandle);
					try {
						if (ht.containsKey("dbname"))
							dbhandle.setComponentName((String) ht.get("dbname"));
					} catch(Exception e) {
						System.err.println("MAP#200104201436: Cannot set database name after adding it to the map handle.");
						Thread.currentThread().dumpStack();
					}

					//Make sure that the change in the saving format will be saved
					db.setModified();
					if (dbList==null)
						dbList=new DatabaseMapListener();
					db.addDatabaseListener(dbList);
					handle.add(db.getESlateHandle());
					if (converted)
						setPlugAliases(db);
				}
			}
		} else {
			Object[] dbchild=getESlateHandle().restoreChildObjects(ht,"dbsco");
			if (dbchild!=null && dbchild.length>0) {
				db=((DBase)((ESlateHandle) dbchild[0]).getComponent());
				if (dbList==null)
					dbList=new DatabaseMapListener();
				db.addDatabaseListener(dbList);
			}
		}
		entryndno=ht.get("entrynode",0);
		precision=ht.get("dataprecision",IMapView.DOUBLE_PRECISION);
		//Load unused layers
		lids=(ArrayList) ht.get("unusedLayersIDs",(ArrayList) null);
		return ht.get("root","");
	}
	/**
	 * Given an output stream this method saves the whole map tree.
	 */
	public void writeStream(OutputStream out) throws IOException {
		ObjectOutputStream oout=new ObjectOutputStream(out);
		ESlateFieldMap2 ht=new ESlateFieldMap2(2);
		ht.put("name",getName());
		ht.put("boundRect",getBoundingRect());
		ht.put("date",getCreationDate());
		ht.put("author",getAuthor());
		ht.put("comments",getComments());
		ht.put("dataprecision",precision);
		ht.put("root",getMapRoot().getGUID());
		if (entryNode!=null) {
			Enumeration e=getMapRoot().breadthFirstEnumeration();
			int counter=0;
			while (e.hasMoreElements()) {
				if (e.nextElement().equals(entryNode)) {
					ht.put("entrynode",counter);
					break;
				}
				counter++;
			}
		}
		ArrayList lids=new ArrayList();
		for (int i=0;i<unusedLayers.size();i++)
			lids.add(((Layer) unusedLayers.get(i)).getGUID());
		if (lids.size()>0)
			ht.put("unusedLayersIDs",lids);

		if (db!=null)
			getESlateHandle().saveChildObjects(ht,"dbsco",new Object[]{db.getESlateHandle()});

		oout.writeObject(ht);
		//oout.flush();
		oout.close();
	}
	/**
	 * Known method.
	 */
	public void setBorder(Border b) {
		borderChanged=true;
		super.setBorder(b);
	}
	/**
	 * Lazily creates structures needed.
	 */
	private void createStructures() {
		if (openedLayers==null) {
			openedLayers=new Hashtable();
			unusedLayers=new ArrayList();
			deletedLayers=new ArrayList();
			deletedRegions=new ArrayList();
			deletedNodes=new ArrayList();
		}
	}
	Access getSaveAccess() {
		return saveAccess;
	}
	Access getOpenAccess() {
		return openAccess;
	}
	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		createStructures();
		StorageStructure ht=(StorageStructure) in.readObject();
		setMenubarVisible(ht.get("menubarvisible",true));
		//If map data had been saved, reload them.
		if (ht.get("savedmap",false)) {
			openedMapFromMWD=true;
			openAccess=handle;
			performOpen(handle);
		}
		if (ht.containsKey("border")){
			try {
				BorderDescriptor bd=(BorderDescriptor) ht.get("border");
				setBorder(bd.getBorder());
			} catch (Throwable thr) {/*No Border*/}
		}
		int v=ht.get("views#",-1);
		if (v>0) {
			for (int i=0;i<v;i++) {
				if (i!=0)
					//Emulate the creation of plugs so that the views state gets loaded...
					createMapPlug(false);
				((MapView) views.get(views.size()-1)).readExternal(in);
			}
			//No need to create a new plug, because the connection of the first one will produce it.
		}
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		//Close the creator. If the creator is open, it is not guaranteed that all the data will
		//be written in the file!!!
		if (creator!=null) {
			creator.dispose();
			creator=null;
		}
		//If there are no data, don't save anything. The new map will be recreated by code.
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("menubarvisible",isMenubarVisible());
		if (isNewMap) {
			ht.put("savedmap",false);
		} else {
			ht.put("savedmap",true);
			saveAccess=handle;
			performSave(handle);
			openAccess=handle;
			savedMapToFile=false;
		}
		ht.put("views#",views.size()-1);
		if (borderChanged) {
			try {
				BorderDescriptor bd=ESlateUtils.getBorderDescriptor(getBorder(),this);
				ht.put("border",bd);
			} catch (Throwable thr) {}
		}

		out.writeObject(ht);
		//Save the views state in different objects, except for the last one, which belongs to the unused plug.
		if (views.size()>1)
			for (int i=0;i<views.size()-1;i++)
				((MapView) views.get(i)).writeExternal(out);

		if (!isNewMap)
			updateSFField(handle);
	}

	private class DatabaseMapListener extends DatabaseAdapter {
		public void tableRemoved(TableRemovedEvent e) {
			//Remove tables from layers.
			Enumeration en=getMapRoot().breadthFirstEnumeration();
			while (en.hasMoreElements()) {
				Region r=((MapNode) en.nextElement()).getRegion();
				Layer[] l=r.getLayers();
				for (int i=0;i<l.length;i++)
					if (l[i].getTable()==e.getRemovedTable())
						try {
							l[i].setTable(null);
						} catch(InvalidLayerDataException ex) {
							System.err.println("MAP#200101121419: Could not reset layer table after removing it from the database.");
						}
			}
			en=null;
		}
	}
	/**
	 * Keep it to remove on disposing the handle because it keeps the Map alive!
	 */
	private FileMenuListener fml=null;
	/**
	 * Listener that lazily creates the menu items.
	 */
	private class FileMenuListener extends MouseAdapter {
		public void mouseEntered(MouseEvent e) {
			if (!menuCreated) {
				menuCreated=true;

				//The component is being used! Create the structures!
				createStructures();
				edit=new JMenuItem(bundleMenu.getString("edit"),Helpers.loadImageIcon("images/edit.gif"));
				edit.setEnabled(true);
				edit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						openCreator();
					}
				});
				file.add(edit);
				file.add(new JSeparator());
				open=new JMenuItem(bundleMenu.getString("open"),Helpers.loadImageIcon("images/open.gif"));
				open.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						performOpen();
					}
				});
				file.add(open);
				save=new JMenuItem(bundleMenu.getString("save"),Helpers.loadImageIcon("images/save.gif"));
				save.setEnabled(false);
				save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (saveAccess==null)
							performSaveAs();
						else
							try {
								performSave(saveAccess);//,false);
							} catch(Exception ex) {
							   JOptionPane.showMessageDialog(Map.this,bundleMessages.getString("cantsave"),"",JOptionPane.ERROR_MESSAGE);
							   ex.printStackTrace();
							};
					}
				});
				file.add(save);
				saveAs=new JMenuItem(bundleMenu.getString("saveas"),Helpers.loadImageIcon("images/null.gif"));
				saveAs.setEnabled(false);
				saveAs.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						performSaveAs();
					}
				});
				file.add(saveAs);
				if (!isNewMap) {
					edit.setEnabled(true);
					open.setEnabled(false);
					save.setEnabled(true);
					saveAs.setEnabled(true);
				}

				/*close=new JMenuItem(bundleMenu.getString("close"),Helpers.loadImageIcon("images/null.gif"));
				close.setEnabled(false);
				close.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (save.isEnabled()) {
							int o=JOptionPane.showConfirmDialog(Map.this,bundleMessages.getString("mapnotsaved"),"",JOptionPane.YES_NO_CANCEL_OPTION);
							if (o==JOptionPane.YES_OPTION) {
								performSaveAs();
							} else if (o==JOptionPane.CANCEL_OPTION)
								return;
							//Inform all Listeners.
							for (int i=0;i<views.size();i++)
								((MapView) views.get(i)).brokerMapClosed();
							isNewMap=true;
							setName(bundleMessages.getString("mymap"));
							boundRect=null;
							date=null;
							author=null;
							comments=null;
							if (db!=null)
								db.removeDatabaseListener(dbList);
							db=null;
							handle.remove(db.getESlateHandle());
							MapNode defRoot=new MapNode(new Region());
							setMapRoot(defRoot);
							setEntryNode(defRoot);
							preview.setIcon(null);
							jComments.setText("");
							edit.setEnabled(true);
							save.setEnabled(false);
							saveAs.setEnabled(false);
							close.setEnabled(false);
						}
					}
				});
				//Closing disabled in order to see if it is useful.
				file.add(new JSeparator());
				file.add(close);*/
			}
		}
		public void mousePressed(MouseEvent e) {
			if (isSaved() || creator!=null)
				save.setEnabled(false);
			else
				save.setEnabled(true);
		}
	}

	private class HandleListener extends ESlateAdapter {
		public void disposingHandle(HandleDisposalEvent e) {
			if (e.cancellationRespected) {
			// Component is closing atomically
				if (!isEverythingSaved()){
					e.stateChanged=true;
					int question=JOptionPane.showConfirmDialog(Map.this,bundleMessages.getString("mapnotsaved"),bundleMessages.getString("confirm"),JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (question==JOptionPane.YES_OPTION) {
						try {
							if (saveAccess==null || (saveAccess instanceof ESlateHandle))
								performSaveAs();
							else
								performSave(saveAccess);
						} catch(IOException ioe) {
							JOptionPane.showMessageDialog(Map.this,bundleMessages.getString("cantsave"),"",JOptionPane.ERROR_MESSAGE);
						}
					}
					else if (question == JOptionPane.CANCEL_OPTION) {
						e.vetoDisposal=true;
					} else if (question==JOptionPane.NO_OPTION && root!=null) {
						//Set the "saved" flag for all objects so that they are removed from memory
						Enumeration en=root.breadthFirstEnumeration();
						while (en.hasMoreElements()) {
							MapNode mn=(MapNode) en.nextElement();
							mn.destroying();
							mn.getRegion().destroying();
							for (int i=0;i<mn.getRegion().getLayers().length;i++)
								mn.getRegion().getLayers()[i].destroying();
						}

					}
				}
			} else {
			// close microworld
				if (!isEverythingSaved()) {
					e.stateChanged=true;
				}
			}
		}

		public void handleDisposed(HandleDisposalEvent e) {
			menu.removeAll();
			file.removeMouseListener(fml);
			fml=null;
			removeAll();
			handle.removeESlateListener(this);
			PerformanceManager.getPerformanceManager().removePerformanceListener(perfListener);
			perfListener=null;
			if (db!=null && dbList!=null)
				db.removeDatabaseListener(dbList);
			if (creator!=null) {
				creator.dispatchEvent(new WindowEvent(creator,WindowEvent.WINDOW_CLOSED));
				creator.dispose();
				creator=null;
			}
		}
	}

	private static final String MAP_SUFFIX=".esm";
	private static float PREVIEW_SIZE=70f;

	/**
	 * The menubar and its menus.
	 */
	private JMenuBar menu;
	private JMenu file;
	private JMenuItem edit,open,save,saveAs,close;

	/**
	 * Is it a new map?
	 */
	private boolean isNewMap;
	/**
	 * Delays menu creation until it is needed.
	 */
	private boolean menuCreated;
	/**
	 * Used in saving.
	 */
	private boolean saved;
	/**
	 * Used in saving.
	 */
	private Access openAccess;
	/**
	 * Used in saving.
	 */
	private Access saveAccess;
	/**
	 * Used in saving.
	 */
	private boolean openedMapFromMWD;
	/**
	 * Used in saving.
	 */
	private boolean savedMapToFile;
	/**
	 * The name of the map.
	 */
	private String name;
	/**
	 * The E-Slate handle.
	 */
	private ESlateHandle handle;
	/**
	 * This list holds all the different views of the map.
	 */
	private ArrayList views;
	/**
	 * The bounding rectangle of the whole set of maps.
	 */
	private Rectangle2D boundRect;
	/**
	 * Creation date.
	 */
	private String date;
	/**
	 * Author.
	 */
	private String author;
	/**
	 * Comments.
	 */
	private String comments;
	/**
	 * Database.
	 */
	protected DBase db;
	/**
	 * Resource bundles.
	 */
	protected static java.util.ResourceBundle bundlePlug,bundleMenu,bundleMessages,bundleInfo;
	/**
	 * The Map tree root node.
	 */
	private MapNode root;
	/**
	 * The entry node, i.e. the first map shown which may not always be the root.
	 */
	private MapNode entryNode;
	private int entryndno;
	/**
	 * The GUI elements.
	 */
	private PreviewLabel preview;
	private JTextArea jComments;
	private static final Color tablePlugColor=new Color(102,88,187);
	/**
	 * Timer which measures the time required for loading the state of the
	 * component.
	 */
	PerformanceTimer loadTimer;
	/**
	 * Timer which measures the time required for saving the state of the
	 * component.
	 */
	PerformanceTimer saveTimer;
	/**
	 * Timer which measures the Map construction time.
	 */
	PerformanceTimer constructorTimer;
	/**
	 * Timer which measures the construction time of the E-Slate side of the Map, typically getESlateHandle().
	 */
	PerformanceTimer initESlateAspectTimer;
	/**
	 * The listener that notifies about changes to the state of the
	 * Performance Manager.
	 */
	PerformanceListener perfListener = null;
	// Timers which time the measure the map file opening procedure
	PerformanceTimer performOpenTimer, openSubTreeTimer, setMapRootTimer, readUnusedLayersTimer;
	PerformanceTimer mapReadStreamTimer, mapReadFieldMapTimer;
	PerformanceTimer refreshTimer, openRegionTimer;
	// Timers in the Region's readStream() method
	PerformanceTimer regionReadFieldMapTimer, regionRestoreAttribsTimer, regionChangeDirTimer, regionBgrReadTimer;
	// Timers in the Map's openLayer()
	PerformanceTimer openLayerTimer, layerChangeDirTimer;
	// Timers in the Layer's readStream()
	PerformanceTimer layerReadFieldMapTimer, layerRestoreAttribsTimer;
	// Vector layer load timers
	PerformanceTimer checkNLoadTimer, checkNLoadChangeDirTimer, checkNLoadReadObjectTimer, checkNLoadLoadObjectsTimer;
	/**
	 * The disk directory from which the map has been loaded or to which the map has been saved.
	 */
	private String mapDirectory;
	/**
	 * The disk filename from which the map has been loaded or to which the map has been saved.
	 */
	private String mapFile;
	/**
	 * Holds all the opened layers as a cache to disk.
	 */
	private Hashtable openedLayers;
	/**
	 * Prevents a layer from saving multiple times.
	 */
	private ArrayList savedLayers;
	/**
	 * Array to pass values from readStream to performOpen.
	 */
	private ArrayList lids;
	/**
	 * Keeps all the unused layers that where created in the creator but not used in any region.
	 * !!The array is being updated by the MapCreator with direct access!!
	 */
	ArrayList unusedLayers;
	/**
	 * Keeps all the layers that have been deleted in this session. They will be removed
	 * from the file when saving.
	 * !!The array is being updated by the MapCreator with direct access!!
	 */
	ArrayList deletedLayers;
	/**
	 * Keeps all the regions that have been deleted in this session. They will be removed
	 * from the file when saving.
	 */
	ArrayList deletedRegions;
	/**
	 * Keeps all the map nodes that have been deleted in this session. They will be removed
	 * from the file when saving.
	 */
	ArrayList deletedNodes;
	/**
	 * The one and only creator instance open.
	 */
	private MapCreator creator;
	/**
	 * The serial version UID.
	 * Currently 3000.
	 */
	static final long serialVersionUID=3000L;
	/**
	 * Flags a change in the border.
	 */
	private boolean borderChanged;
	/**
	 * Database listener.
	 */
	private DatabaseListener dbList;
	/**
	 * Data precision indicator.
	 */
	private int precision;
	/**
	 * The component version.
	 */
	public static final String version="3";

	{
		Locale loc=Locale.getDefault();
		//Localization
		bundleInfo=java.util.ResourceBundle.getBundle("gr.cti.eslate.mapModel.BundleInfo",loc);
		String[] info={bundleInfo.getString("part"),bundleInfo.getString("development"),bundleInfo.getString("contribution"),bundleInfo.getString("copyright")};
		//Variable initialization
		openedMapFromMWD=false;
		bundlePlug=java.util.ResourceBundle.getBundle("gr.cti.eslate.mapModel.BundlePlug",loc);
		bundleMenu=java.util.ResourceBundle.getBundle("gr.cti.eslate.mapModel.BundleMenu",loc);
		bundleMessages=java.util.ResourceBundle.getBundle("gr.cti.eslate.mapModel.BundleMessages",loc);
		precision=IMapView.DOUBLE_PRECISION;
		views=new ArrayList(3);
	}
}
