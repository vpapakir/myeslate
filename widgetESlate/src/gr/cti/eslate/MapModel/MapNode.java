package gr.cti.eslate.mapModel;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Access;

import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * A map tree is constructed by objects of this class.
 * Each MapNode has an associated Region object which
 * actually contains the region.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 6-Aug-1999
 * @see		gr.cti.eslate.mapModel.Region
 */

public class MapNode implements TreeNode,MutableTreeNode {
	/**
	 * Creates a MapNode.
	 */
	MapNode() {
		guid=""+hashCode();
		children=new ArrayList();
		modified=true;
		depth=0;
	}
	/**
	 * Creates a MapNode with a specific associated region.
	 */
	public MapNode(Region region) {
		this();
		setRegion(region);
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public TreeNode getChildAt(int childIndex) {
		if ((childIndex>-1) && (childIndex<children.size()))
			return (TreeNode) children.get(childIndex);
		else
			return null;
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public int getChildCount() {
		return children.size();
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public TreeNode getParent() {
		return parent;
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public int getIndex(TreeNode node) {
		if (node!=null)
			return children.indexOf(node);
		else
			return -1;
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public boolean getAllowsChildren() {
		return true;
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public boolean isLeaf() {
		if (children.size()==0)
			return true;
		else
			return false;
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public Enumeration children() {
		return new Enumeration() {
			int index=0;
			public Object nextElement() {
				return children.get(index++);
			}
			public boolean hasMoreElements() {
				return (index<children.size());
			}
		};
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public void insert(MutableTreeNode child, int index) {
		children.add(index,child);
		child.setParent(this);
		modified=true;
		((MapNode) child).setMap(map);
		map.brokerMapRegionAdded(((MapNode) child).getRegion());
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public void remove(int index) {
		if ((index>-1) && (index<children.size())) {
			modified=true;
			map.brokerMapRegionRemoved(((MapNode) children.remove(index)).getRegion());
		}
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public void remove(MutableTreeNode node) {
		int i;
		if ((i=children.indexOf(node))!=-1) {
			modified=true;
			map.brokerMapRegionRemoved(((MapNode) children.remove(i)).getRegion());
		}
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public void setUserObject(Object object) {
		if (!(object instanceof Region))
			throw new RuntimeException("MAPMODEL#200002161809: A MapNode may only hold Regions.");
		setRegion((Region) object);
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public void removeFromParent() {
		if (parent!=null)
			parent.remove(this);
	}
	/**
	 * javax.swing.tree.MutableTreeNode method.
	 */
	public void setParent(MutableTreeNode newParent) {
		this.parent=(MapNode) newParent;
		calculateDepth();
	}
	void calculateDepth() {
		MapNode node=this;
		depth=0;
		while ((node=node.getRegionParent())!=null)
			depth++;
	}
	/**
	 * Gets the depth of the node in the tree.
	 */
	public int getDepth() {
		return depth;
	}
	/**
	  * Returns the path from the root, to get to this node.  The last
	  * element in the path is this node.
	  *
	  * @return an array of TreeNode objects giving the path, where the
	  *         first element in the path is the root and the last
	  *         element is this node.
	  */
	public TreeNode[] getPath() {
		return getPathToRoot(this, 0);
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
			retNodes = getPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}
	public Region getUserObject() {
		return region;
	}
	/**
	 * Adds a child to this node.
	 * @param child The child node.
	 * @return <em>true</em> if the child has been added, <em>false</em> otherwise.
	 */
	public boolean insert(MapNode child) {
		if (children.contains(child))
			return false;
		child.setParent(this);
		children.add(child);
		child.setMap(map);
		modified=true;
		map.brokerMapRegionAdded(child.getRegion());
		return true;
	}

	/**
	 * Creates and returns an enumeration that traverses the subtree rooted at
	 * this node in breadth-first order.  The first node returned by the
	 * enumeration's <code>nextElement()</code> method is this node.<P>
	 *
	 * Modifying the tree by inserting, removing, or moving a node invalidates
	 * any enumerations created before the modification.
	 *
	 * @return	an enumeration for traversing the tree in breadth-first order
	 */
	public Enumeration breadthFirstEnumeration() {
		return new BreadthFirstEnumeration(this);
	}

	final class BreadthFirstEnumeration implements Enumeration {
		protected Queue	queue;

		public BreadthFirstEnumeration(TreeNode rootNode) {
			super();
			java.util.Vector v = new java.util.Vector(1);
			v.add(rootNode);	// PENDING: don't really need a vector
			queue = new Queue();
			queue.enqueue(v.elements());
		}

		public boolean hasMoreElements() {
			return (!queue.isEmpty() &&
				((Enumeration)queue.firstObject()).hasMoreElements());
		}

		public Object nextElement() {
			Enumeration	enumer = (Enumeration)queue.firstObject();
			TreeNode	node = (TreeNode)enumer.nextElement();
			Enumeration	children = node.children();

			if (!enumer.hasMoreElements()) {
			queue.dequeue();
			}
			if (children.hasMoreElements()) {
			queue.enqueue(children);
			}
			return node;
		}
		// A simple queue with a linked list data structure.
		final class Queue {
			QNode head;	// null if empty
			QNode tail;

			final class QNode {
			public Object	object;
			public QNode	next;	// null if end
			public QNode(Object object, QNode next) {
				this.object = object;
				this.next = next;
			}
			}

			public void enqueue(Object anObject) {
			if (head == null) {
				head = tail = new QNode(anObject, null);
			} else {
				tail.next = new QNode(anObject, null);
				tail = tail.next;
			}
			}

			public Object dequeue() {
			if (head == null) {
				throw new java.util.NoSuchElementException("No more elements");
			}

			Object retval = head.object;
			QNode oldHead = head;
			head = head.next;
			if (head == null) {
				tail = null;
			} else {
				oldHead.next = null;
			}
			return retval;
			}

			public Object firstObject() {
			if (head == null) {
				throw new java.util.NoSuchElementException("No more elements");
			}

			return head.object;
			}

			public boolean isEmpty() {
			return head == null;
			}

		} // End of class Queue

	}  // End of class BreadthFirstEnumeration
	/**
	 * @return The dependend nodes.
	 */
	public ArrayList childrenList() {
		return children;
	}
	/**
	 * Sets the associated region.
	 * @param  region The associated region.
	 */
	protected void setRegion(Region region) {
		this.region=region;
		this.region.setMapNode(this);
		modified=true;
		regionguid=region.getGUID();
	}
	/**
	 * Gets the associated Region.
	 * @return The associated Region.
	 */
	protected Region getRegion() {
		if ((region==null) && (regionguid!=null))
			try {
				region=map.openRegion(sf,regionguid,this);
			} catch(Exception e) {
				System.err.println("MAP#200004141629: Region couldnot be loaded from the structfile.");
				e.printStackTrace();
			}
		return region;
	}
	/**
	 * Recursively searches the node and its children to find a specific node.
	 */
	protected MapNode getNode(Region region) {
		MapNode res;
		if (this.region==null)
			return null;
		else if (this.region.equals(region))
			return this;
		for (int i=0;i<children.size();i++) {
			res=((MapNode) children.get(i)).getNode(region);
			if (res!=null)
				return res;
		}
		return null;
	}
	/**
	 * Gets the parent of this node with no need to cast as in getParent().
	 */
	public MapNode getRegionParent() {
		return parent;
	}
	/**
	 * Sets the Map tree this node belongs to. It informs the whole subtree for the change.
	 */
	protected void setMap(Map map) {
		Enumeration e=breadthFirstEnumeration();
		while (e.hasMoreElements())
			((MapNode) e.nextElement()).map=map;
	}
	/**
	 * Gets the Map tree this node belongs to.
	 */
	protected Map getMap() {
		return map;
	}
	/**
	 * Gets the Map name this node belongs to.
	 */
	public String getMapName() {
		return map.getName();
	}
	/**
	 * Sets the zooming rectangle.
	 * The zooming rectangle can be different from Region bounding rectangle to
	 * enlarge the zooming rectangle. If no value is given it defaults to
	 * Region bounding rectangle.
	 * @param  rect The zooming rectangle.
	 */
	protected void setZoomRectangle(Rectangle2D rect) {
		Rectangle2DWrapper zr=null;
		if (rect!=null)
			zr=new Rectangle2DWrapper(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
		modified=true;
		this.zoomRect=zr;
	}
	/**
	 * Gets the zooming rectangle.
	 * @return The zooming rectangle.
	 */
	protected Rectangle2D getZoomRectangle() {
		if ((zoomRect==null) && (region!=null))
			return region.getBoundingRect();
		return zoomRect;
	}

	protected void setModified(boolean value) {
		modified=value;
	}

	protected boolean isModified() {
		return modified;
		/*//If we already know, don't recurse
		if (modified)
			return true;

		//Stop recursion condition
		if (isLeaf())
			return modified;

		//Recursion
		for (int i=0;i<getChildCount();i++)
			if (((MapNode) getChildAt(i)).isModified())
				return true;
		return false;*/
	}

	protected boolean isSaved() {
		return !modified;
	}

	protected void destroying() {
		modified=false;
	}

	public String toString() {
		if (region!=null)
			return region.getName();
		else
			return super.toString();
	}
	/**
	 * Returns an id used in saving.
	 */
	protected String getGUID() {
		return guid;
	}
	/**
	 * Changes the structure file from where the region will request its data.
	 */
	protected void setSF(Access sf) {
		this.sf=sf;
		if (region!=null)
			region.setSF(sf);
	}
	//Linking methods
	/**
	 * Checks if there are any links to this region.
	 */
	boolean hasLinks() {
		return (linkList!=null && linkList.size()>0);
	}
	/**
	 * The number of regions linked to this region.
	 */
	int getNumberOfLinks() {
		if (linkList==null)
			return 0;
		return linkList.size();
	}
	/**
	 * Gets the link in the given index. The link is normally a MapNode but
	 * can be a String GUID while restoring.
	 */
	Object getLink(int i) {
		if (linkList==null)
			return null;
		return linkList.get(i);
	}
	/**
	 * Changes a link. Does not set the modified flag. Used in reconstructing the
	 * links from a saved map.
	 */
	void setLink(int i,MapNode mn) {
		if (linkList==null)
			throw new RuntimeException("Null link list.");
		linkList.set(i,mn);
	}
	/**
	 * Adds a link.
	 */
	void addLink(MapNode mn) {
		if (linkList==null)
			throw new RuntimeException("Null link list.");
		linkList.add(mn);
		modified=true;
	}
	/**
	 * Removes a link.
	 */
	void removeLink(MapNode mn) {
		if (linkList==null)
			throw new RuntimeException("Null link list.");
		linkList.remove(mn);
		modified=true;
	}
	/**
	 * Returns the region that this region is linked to.
	 */
	MapNode getLinkedTo() {
		return linkedTo;
	}
	/**
	 * Sets the region that this region is linked to.
	 */
	void setLinkedTo(MapNode mn) {
		setLinkedTo(mn,true);
	}
	/**
	 * Sets the region that this region is linked to.
	 * If the boolean parameter is set to false, the MapNode will not
	 * set the modified flag, thus it will not be saved by this action.
	 * Used when reconstructing the links from a saved map.
	 */
	void setLinkedTo(MapNode mn,boolean setModifiedFlag) {
		if (mn==linkedTo)
			return;
		linkedTo=mn;
		modified=setModifiedFlag;
	}
	/**
	 * Creates a new link list.
	 */
	void newLinkList() {
		linkList=new ArrayList();
	}




	/**
	 * This method loads the node and its children from a structured stream.
	 */
	public void readStream(Access sf,ObjectInputStream in) throws ClassNotFoundException, IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		guid=ht.get("guid","0");
		//There has been a change from Rectangle to Rectangle2D
		Object zrect=ht.get("zoomRect");
		if (zrect==null)
			setZoomRectangle(null);
		else if (zrect instanceof Rectangle2D)
			setZoomRectangle((Rectangle2D) zrect);
		else if (zrect instanceof Rectangle)
			setZoomRectangle(new Rectangle2D.Double(((Rectangle)zrect).x,((Rectangle)zrect).y,((Rectangle)zrect).width,((Rectangle)zrect).height));
		regionguid=ht.get("regionguid","0");
		//Read the guids of the children nodes
		String[] cld=(String[]) ht.get("childrenguid");
		for (int i=0;i<cld.length;i++) {
			sf.changeToParentDir();
			sf.changeDir(cld[i]);
			MapNode n=new MapNode();
			n.readStream(sf,new ObjectInputStream(new BufferedInputStream(sf.openInputFile("node"))));
			n.setParent(this);
			n.setMap(map);
			children.add(n);
		}
		//The region GUIDs are restored. After finishing loading, they must be translated to the mapNodes themselves.
		linkList=(ArrayList) ht.get("linkList",(ArrayList) null);
		this.sf=sf;
		modified=false;
	}
	/**
	 * This method saves the node in a structured stream.
	 */
	public void writeStream(Access sf,ObjectOutputStream out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("guid",guid);
		ht.put("zoomRect",getZoomRectangle());
		ht.put("regionguid",regionguid);
		//Write the guids of the children nodes
		String[] cld=new String[children.size()];
		for (int i=0;i<children.size();i++)
			cld[i]=((MapNode) children.get(i)).getGUID();
		ht.put("childrenguid",cld);
		if (linkList!=null && linkList.size()>0) {
			//Put the guids to save
			ArrayList l=new ArrayList();
			for (int i=0;i<linkList.size();i++) {
				l.add(((MapNode)linkList.get(i)).getGUID());
			}
			ht.put("linkList",l);
		}
		out.writeObject(ht);
		//out.flush();
		modified=false;
	}

	/**
	 * Used for saving.
	 */
	private String guid;
	/**
	 * The Region object associated with this node.
	 */
	Region region;
	private Access sf;
	/**
	 * The guid of the region object, used to load it asynchronously from the structured file.
	 */
	private String regionguid;
	/**
	 * The zooming rectangle pointing to this node.
	 * Can be different from Region bounding rectangle to enlarge the zooming
	 * rectangle. If no value is given it defaults to Region bounding rectangle.
	 */
	private Rectangle2DWrapper zoomRect;
	/**
	 * The parent node.
	 */
	private MapNode parent;
	/**
	 * The map tree this node belongs to.
	 */
	protected Map map;
	/**
	 * The node's children.
	 */
	private ArrayList children;
	/**
	 * Modified flag.
	 */
	private boolean modified;
	/**
	 * The depth in the tree.
	 */
	private int depth;
	/**
	 * The Region of this MapNode has the same layers as the Region of the linkedTo MapNode.
	 */
	private MapNode linkedTo;
	/**
	 * The list of the MapNodes linked to this MapNode, having the same layers.
	 * It is constructed and initialized in the RegionInfo object.
	 */
	private ArrayList linkList;
}

