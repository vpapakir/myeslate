package gr.cti.eslate.mapModel;

import gr.cti.eslate.mapModel.geom.Point;
import gr.cti.eslate.mapModel.geom.PolyLine;
import gr.cti.eslate.mapModel.geom.Polygon;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.IncompatibleObjectTypeException;

import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import java.util.ArrayList;

/**
 * The abstract superclass of all RTree classes.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 6-Aug-1999
 * @see gr.cti.eslate.mapModel.PointRTree
 * @see gr.cti.eslate.mapModel.PolyLineRTree
 * @see gr.cti.eslate.mapModel.PolygonRTree
 */
abstract class RTree implements Externalizable {
	/**
	 * Creates an RTree from a shapefile.
	 * @param shapefile The shapefile URL.
	 * @param shapefileIndex The shapefile index URL.
	 * @param precision The data precision, either Map.SINGLE_PRECISION or Map.DOUBLE_PRECISION.
	 * @return A PointRTree, PolyLineRTree or PolygonRTree.
	 * @see gr.cti.eslate.mapModel.PointRTree
	 * @see gr.cti.eslate.mapModel.PolyLineRTree
	 * @see gr.cti.eslate.mapModel.PolygonRTree
	 */
	static RTree createRTree(URL shapefile,URL shapefileIndex,int precision) throws CannotCreateRTreeException,IOException {
		if (precision!=IMapView.SINGLE_PRECISION && precision!=IMapView.DOUBLE_PRECISION)
			throw new CannotCreateRTreeException("Invalid precision parameter.");

		double Xmin,Ymin,Xmax,Ymax;
		int shapeType;
		ArrayList SHXrecs=new ArrayList();
		RTree rtree=null;

		byte b[]=new byte[4];

		//Open SHX stream
		BigLittleDataInputStream SHXstream=new BigLittleDataInputStream(new BufferedInputStream(new DataInputStream(shapefileIndex.openConnection().getInputStream()),bufferSize));
		// read SHX header
		SHXstream.readFully(b);
		// validate SHX
		if ((b[0]!=0x00) || (b[1]!=0x00) || (b[2]!=0x27) || (b[3]!=0x0a))
			throw new CannotCreateRTreeException("The given shapefile does not have a valid Index.");
		SHXstream.skipBytes(96);
		/**READ SHX***/
		int offset;
		Integer r;
		try {
			while (true) {
				offset=SHXstream.readInt();
				r=new Integer(offset);
				SHXstream.skipBytes(4);
				SHXrecs.add(r);
			}
		} catch(EOFException exc) {/*EOF*/}
		SHXstream.close();
		/***END READING SHX***/

		//Open SHP stream
		BigLittleDataInputStream SHPstream=new BigLittleDataInputStream(new BufferedInputStream(new DataInputStream(shapefile.openConnection().getInputStream()),bufferSize));

		// read SHP header
		SHPstream.readFully(b);
		// validate SHP
		if ((b[0]!=0x00) || (b[1]!=0x00) || (b[2]!=0x27) || (b[3]!=0x0a))
			throw new CannotCreateRTreeException("The given file is not a valid ShapeFile.");
		// Get Region Extend
		SHPstream.skipBytes(28);
		shapeType=SHPstream.readLittleInt();
		Xmin=SHPstream.readLittleDouble();
		Ymin=SHPstream.readLittleDouble();
		Xmax=SHPstream.readLittleDouble();
		Ymax=SHPstream.readLittleDouble();
		SHPstream.skipBytes(32);
		/***READ SHP***/
		int i=0;
		int id;
		double bXmin,bYmin,bXmax,bYmax;

		try {
		switch (shapeType) {
			case 1: {
				Point pp;
				double X=0,Y=0;
				if (precision==IMapView.SINGLE_PRECISION)
					rtree=new PointRTree.Float();
				else
					rtree=new PointRTree.Double();
				while (i < SHXrecs.size()) {
					id=SHPstream.readBigInt()-1;
					SHPstream.skipBytes(4+4); //Record Length & Shape Type
					X=SHPstream.readLittleDouble();
					Y=SHPstream.readLittleDouble();
					if (precision==IMapView.SINGLE_PRECISION)
						rtree.insert(X,Y,X,Y,pp=new Point.Float(X,Y,id));
					else
						rtree.insert(X,Y,X,Y,pp=new Point.Double(X,Y,id));
					i++;
				}
				break;
			}
			case 3: {
				if (precision==IMapView.SINGLE_PRECISION) {
					PolyLine pl; int[] parts; float[] points;
					rtree=new PolyLineRTree.Float();
					while (i < SHXrecs.size()) {
						id=SHPstream.readBigInt()-1;
						SHPstream.skipBytes(4+4); //Record Length, Shape Type
						bXmin=SHPstream.readLittleDouble();
						bYmin=SHPstream.readLittleDouble();
						bXmax=SHPstream.readLittleDouble();
						bYmax=SHPstream.readLittleDouble();
						parts=new int[SHPstream.readLittleInt()];
						points=new float[2*SHPstream.readLittleInt()];
						for (int j=0;j<parts.length;j++)
							parts[j]=SHPstream.readLittleInt();
						for (int j=0;j<points.length;j++)
							points[j]=(float) SHPstream.readLittleDouble();
						rtree.insert(bXmin,bYmin,bXmax,bYmax,pl=(new PolyLine.Float(id,parts,points,(float)bXmin,(float)bYmin,(float)bXmax,(float)bYmax)));
						i++;
					}
				} else {
					PolyLine pl; int[] parts; double[] points;
					rtree=new PolyLineRTree.Double();
					while (i < SHXrecs.size()) {
						id=SHPstream.readBigInt()-1;
						SHPstream.skipBytes(4+4); //Record Length, Shape Type
						bXmin=SHPstream.readLittleDouble();
						bYmin=SHPstream.readLittleDouble();
						bXmax=SHPstream.readLittleDouble();
						bYmax=SHPstream.readLittleDouble();
						parts=new int[SHPstream.readLittleInt()];
						points=new double[2*SHPstream.readLittleInt()];
						for (int j=0;j<parts.length;j++)
							parts[j]=SHPstream.readLittleInt();
						for (int j=0;j<points.length;j++)
							points[j]=SHPstream.readLittleDouble();
						rtree.insert(bXmin,bYmin,bXmax,bYmax,pl=(new PolyLine.Double(id,parts,points,bXmin,bYmin,bXmax,bYmax)));
						i++;
					}
				}
				break;
			}
			case 5: {
				if (precision==IMapView.SINGLE_PRECISION) {
					float[] points; int[] parts;
					rtree=new PolygonRTree.Float();
					while (i < SHXrecs.size()) {
						id=SHPstream.readBigInt()-1;
						SHPstream.skipBytes(4+4); //Record Length, Shape Type
						bXmin=SHPstream.readLittleDouble();
						bYmin=SHPstream.readLittleDouble();
						bXmax=SHPstream.readLittleDouble();
						bYmax=SHPstream.readLittleDouble();
						parts=new int[SHPstream.readLittleInt()];
						points=new float[2*SHPstream.readLittleInt()];
						for (int j=0;j<parts.length;j++)
							parts[j]=SHPstream.readLittleInt();
						for (int j=0;j<points.length;j++)
							points[j]=(float) SHPstream.readLittleDouble();
						rtree.insert(bXmin,bYmin,bXmax,bYmax,new Polygon.Float(id,parts,points,(float)bXmin,(float)bYmin,(float)bXmax,(float)bYmax));
						i++;
					}
				} else {
					double[] points; int[] parts;
					rtree=new PolygonRTree.Double();
					while (i < SHXrecs.size()) {
						id=SHPstream.readBigInt()-1;
						SHPstream.skipBytes(4+4); //Record Length, Shape Type
						bXmin=SHPstream.readLittleDouble();
						bYmin=SHPstream.readLittleDouble();
						bXmax=SHPstream.readLittleDouble();
						bYmax=SHPstream.readLittleDouble();
						parts=new int[SHPstream.readLittleInt()];
						points=new double[2*SHPstream.readLittleInt()];
						for (int j=0;j<parts.length;j++)
							parts[j]=SHPstream.readLittleInt();
						for (int j=0;j<points.length;j++)
							points[j]=SHPstream.readLittleDouble();
						rtree.insert(bXmin,bYmin,bXmax,bYmax,new Polygon.Double(id,parts,points,bXmin,bYmin,bXmax,bYmax));
						i++;
					}
				}
				break;
			}
		}
		} catch(IncompatibleObjectTypeException e) {
			System.err.println("Not all features of shapefile "+shapefile+" can be read!");
			e.printStackTrace();
		}
		/*** END READ SHP ***/

		SHPstream.close();

		// free all unneeded resources
		SHXrecs.clear();
		b=new byte[0];

		return rtree;
	}
	/**
	 * Gets the shapefile information.
	 * @param shapefile The shapefile URL.
	 * @param shapefileIndex The shapefile index URL.
	 * @return A ShapeFileInfo object.
	 * @see gr.cti.eslate.mapModel.PointRTree
	 * @see gr.cti.eslate.mapModel.PolyLineRTree
	 * @see gr.cti.eslate.mapModel.PolygonRTree
	 */
	static ShapeFileInfo getShapeFileInfo(URL shapefile,URL shapefileIndex) throws CannotCreateRTreeException, IOException {
		byte b[]=new byte[4];

		//Open SHX stream
		BigLittleDataInputStream SHXstream=new BigLittleDataInputStream(new BufferedInputStream(new DataInputStream(shapefileIndex.openConnection().getInputStream()),bufferSize));
		// read SHX header
		SHXstream.readFully(b);
		// validate SHX
		if ((b[0]!=0x00) || (b[1]!=0x00) || (b[2]!=0x27) || (b[3]!=0x0a))
			throw new CannotCreateRTreeException("The given shapefile does not have a valid Index.");
		//Calculate number of records
		SHXstream.skipBytes(20);
		int size=(SHXstream.readBigInt()-50)/4;
		SHXstream.skipBytes(4);
		int shapeType=SHXstream.readLittleInt();
		SHXstream.close();
		/***END READING SHX***/

		//Open SHP stream to test whether the shapefile exists.
		BigLittleDataInputStream SHPstream=new BigLittleDataInputStream(new BufferedInputStream(new DataInputStream(shapefile.openConnection().getInputStream()),bufferSize));
		// read SHP header
		SHPstream.readFully(b);
		// validate SHP
		if ((b[0]!=0x00) || (b[1]!=0x00) || (b[2]!=0x27) || (b[3]!=0x0a))
			throw new CannotCreateRTreeException("The given shapefile is not valid.");
		SHPstream.close();

		return new ShapeFileInfo(shapefile.toString(),shapeType,size);
	}

	/*public static void write(OutputStream oSHP,OutputStream oSHX,RTree rtree,Array index,int shapeType,double Xmin,double Ymin,double Xmax,double Ymax) throws Exception {
		BigLittleDataOutputStream outSHP=new BigLittleDataOutputStream(oSHP);
		BigLittleDataOutputStream outSHX=new BigLittleDataOutputStream(oSHX);
		int[] idx=new int[index.size()]; int counter=50;

		//***WRITE HEADER//
		//write file code
		outSHP.writeBigInt(9994);
		outSHP.writeBigInt(0);
		outSHP.writeBigInt(0);
		outSHP.writeBigInt(0);
		outSHP.writeBigInt(0);
		outSHP.writeBigInt(0);
		switch (shapeType) {
		case 1:
			outSHP.writeBigInt(50+index.size()*14); //in words
		}
		outSHP.writeLittleInt(1000);
		outSHP.writeLittleInt(shapeType);
		outSHP.writeLittleDouble(Xmin);
		outSHP.writeLittleDouble(Ymin);
		outSHP.writeLittleDouble(Xmax);
		outSHP.writeLittleDouble(Ymax);
		outSHP.writeLittleDouble(0.0d);
		outSHP.writeLittleDouble(0.0d);
		outSHP.writeLittleDouble(0.0d);
		outSHP.writeLittleDouble(0.0d);
		//**WRITE MAIN FILE//
		switch (shapeType) {
		case 1:
			for (int i=0,s=index.size();i<s;i++) {
				outSHP.writeBigInt(i+1);
				outSHP.writeBigInt(14);
				outSHP.writeLittleInt(1);
				outSHP.writeLittleDouble(((point)index.at(i)).X);
				outSHP.writeLittleDouble(((point)index.at(i)).Y);
				idx[i]=counter;
				counter+=14;
			}
			break;
		case 3:
			break;
		case 5:
		}


		//////////////////////////
		//***WRITE SHX HEADER//
		//write file code
		outSHX.writeBigInt(9994);
		outSHX.writeBigInt(0);
		outSHX.writeBigInt(0);
		outSHX.writeBigInt(0);
		outSHX.writeBigInt(0);
		outSHX.writeBigInt(0);
		outSHX.writeBigInt(50+4*index.size());
		outSHX.writeLittleInt(1000);
		outSHX.writeLittleInt(shapeType);
		outSHX.writeLittleDouble(Xmin);
		outSHX.writeLittleDouble(Ymin);
		outSHX.writeLittleDouble(Xmax);
		outSHX.writeLittleDouble(Ymax);
		outSHX.writeLittleDouble(0.0d);
		outSHX.writeLittleDouble(0.0d);
		outSHX.writeLittleDouble(0.0d);
		outSHX.writeLittleDouble(0.0d);
		//**WRITE MAIN FILE//
		switch (shapeType) {
		case 1:
			for (int i=0,s=index.size();i<s;i++) {
				outSHX.writeBigInt(idx[i]);
				outSHX.writeBigInt(14);
			}
			break;
		case 3:
			break;
		case 5:
		}*/

	/**
	 * Constructor called by implementors of the class.
	 */
	RTree() {
		index=new ArrayList();
	}

	public abstract void setRoot(Node r);

	public abstract Node getRoot();
	/**
	 * Clears the whole contents of the R-Tree.
	 */
	public void clear() {
		index=new ArrayList();
		getRoot().isLeaf=true;
		for (int i=0;i<RTree.M;i++)
			getRoot().entry[i]=null;
		getRoot().howMany=0;
	}
	public abstract void insert(double startX,double startY,double endX,double endY, GeographicObject shape) throws IncompatibleObjectTypeException;
	/**
	 * This is used internally only. It doesn't add the shape in the index and is used by
	 * reInsert and InsertNonLeaf.
	 */
	protected void insert(Entry curEntry) throws IncompatibleObjectTypeException {
		insert(curEntry,true);
	}
	/**
	 * This is used internally only. It doesn't add the shape in the index and is used by
	 * reInsert and InsertNonLeaf.
	 */
	protected void insert(Entry curEntry,boolean addToIndex) throws IncompatibleObjectTypeException {
		Node leaf;
		double startX=curEntry.getStartX();
		double startY=curEntry.getStartY();
		double endX=curEntry.getEndX();
		double endY=curEntry.getEndY();
		GeographicObject shape=(GeographicObject) curEntry.child;
		leaf=chooseLeaf(startX,startY,endX,endY);
		if (leaf.howMany<M) { /*There is space for a new entry*/
			leaf.entry[leaf.howMany]=curEntry;
			if (curEntry.child instanceof Node) {
				((Node) curEntry.child).parentIndex=leaf.howMany;
				((Node) curEntry.child).parent=leaf;
			}
			leaf.howMany++;
			adjustTree(leaf,null);
		} else { /*There is no space for a new entry*/
			Node group1,group2;
			if (curEntry instanceof Entry.Float) {
				group1=new Node.Float(leaf.parent,leaf.level);
				group2=new Node.Float(leaf.parent,leaf.level);
			} else {
				group1=new Node.Double(leaf.parent,leaf.level);
				group2=new Node.Double(leaf.parent,leaf.level);
			}
			leaf.split(curEntry,group1,group2);
			if (group1.parent!=null)
				adjustTree(group1,group2);

			if (leaf.parent==null) {/*The leaf is the root.*/
				rootSplit(group1,group2);
			}
		}
		if (addToIndex)
			index.add(shape.getID(),shape);

	}

	public ArrayList search(double startX,double startY,double endX,double endY) {
		ArrayList found=new ArrayList();
		getRoot().search(startX,startY,endX,endY,found);
		return found;
	}
	/**
	 * The third argument of this method is a rectangle (a point with a tolerance) which must
	 * be contained in the actual shape and not in its bounding rectangle. This limits the
	 * results of the simple search method.
	 */
	public ArrayList search(double startX,double startY,double endX,double endY,java.awt.Shape hotspot) {
		ArrayList found=new ArrayList();
		getRoot().search(startX,startY,endX,endY,found);
		if (hotspot!=null && found.size()>0) {
			//Intersects gets only Rectangles as parameters.
			//Have three "for" loops to speed up the regular rectangular intersection
			//rather than intersection with an arbitrary shape.
			if (hotspot instanceof Rectangle2D) {
				for (int i=found.size()-1;i>-1;i--) {
					IVectorGeographicObject vgo=(IVectorGeographicObject) found.get(i);
					if (!vgo.intersects((Rectangle2D)hotspot))
						found.remove(i);
				}
			} else {
				if (this instanceof PointRTree) {
					for (int i=found.size()-1;i>-1;i--) {
						if (!hotspot.contains(((Point)found.get(i)).getX(),((Point)found.get(i)).getY()))
							found.remove(i);
					}
				} else {
					//Arbitrary shape hotspot
					double[] d=new double[6];
					boolean intersects=false;
					for (int i=found.size()-1;i>-1;i--) {
						PathIterator it=((Shape)found.get(i)).getPathIterator(null);
						while (!it.isDone() && !intersects) {
							it.currentSegment(d);
							if (hotspot.contains(d[0],d[1]))
								intersects=true;
							it.next();
						}
						if (found.get(i) instanceof gr.cti.eslate.mapModel.geom.Polygon)
							Polygon.unlockIterator();
						else if (found.get(i) instanceof gr.cti.eslate.mapModel.geom.PolyLine)
							((PolyLine)found.get(i)).unlockIterator(it);
						if (!intersects)
							found.remove(i);
					}
				}
			}
		}
		return found;
	}

	private Node chooseLeaf(double ptAx,double ptAy,double ptBx,double ptBy) {
		Node current;
		int choiceIndex;

		current=getRoot();
		while (current.isLeaf==false) {
			choiceIndex=current.findLessEnlargement(ptAx,ptAy,ptBx,ptBy);
			current=(Node) ((current.entry[choiceIndex]).child);
		}

		return current;
	}

	private void adjustTree(Node L, Node LL) {
		Entry newEntry;

		if (L==getRoot()) return; /*The root is split-No adjustment needed*/

		/*Adjust covering rectangle of L in parent entry*/
		Rectangle2D.Double r=L.getTightRect();
		L.parent.entry[L.parentIndex].setStartX(r.x);
		L.parent.entry[L.parentIndex].setStartY(r.y);
		L.parent.entry[L.parentIndex].setEndX(r.x+r.width);
		L.parent.entry[L.parentIndex].setEndY(r.y+r.height);

		/*Propagate node split upward*/
		if (LL==null) adjustTree(L.parent,null);
		else if (LL!=null) {  /*if L has a partner LL resulting from an earlier split*/
			Rectangle2D.Double r2=LL.getTightRect();
			if (L instanceof Node.Float)
				newEntry=new Entry.Float(r2.x,r2.y,r2.x+r2.width,r2.y+r2.height,LL);
			else
				newEntry=new Entry.Double(r2.x,r2.y,r2.x+r2.width,r2.y+r2.height,LL);
			if (L.parent.howMany<M) {  /*There is space in L's parent*/
				LL.parent=L.parent;
				LL.parentIndex=L.parent.howMany;
				LL.level=L.level;

				L.parent.entry[L.parent.howMany]=newEntry;
				if (newEntry.child instanceof Node) {
					((Node) newEntry.child).parentIndex=L.parent.howMany;
					((Node) newEntry.child).parent=L.parent;
				}
				L.parent.howMany++;
				adjustTree(L.parent,null);
			} else {  /*There is no space*/
				Node group1,group2;
				if (L instanceof Node.Float) {
					group1=new Node.Float(L.parent.parent,L.parent.level);
					group2=new Node.Float(L.parent.parent,L.parent.level);
				} else {
					group1=new Node.Double(L.parent.parent,L.parent.level);
					group2=new Node.Double(L.parent.parent,L.parent.level);
				}
				L.parent.split(newEntry,group1,group2);
				/*Change the parent pointer of the children of group1 and group2
				  Only if groups are not leaves*/
				if (!group1.isLeaf)
					for (int i=0;i<group1.howMany;i++) {
						((Node) (group1.entry[i].child)).parent=group1;
						((Node) (group1.entry[i].child)).parentIndex=i;
					}
				if (!group2.isLeaf)
					for (int i=0;i<group2.howMany;i++) {
						((Node) (group2.entry[i].child)).parent=group2;
						((Node) (group2.entry[i].child)).parentIndex=i;
					}
				if (group1.parent==null) {  /*Root is split*/
					rootSplit(group1,group2);
				} else
					adjustTree(group1,group2);
			}
		}
	}

	private void rootSplit(Node group1, Node group2) {
		Node newRoot;
		Entry entry1,entry2;

		if (group1 instanceof Node.Float) {
			newRoot=new Node.Float(null,0);
			entry1=new Entry.Float();
			entry2=new Entry.Float();
		} else {
			newRoot=new Node.Double(null,0);
			entry1=new Entry.Double();
			entry2=new Entry.Double();
		}
		newRoot.howMany=2;
		entry1.child=group1;
		Rectangle2D.Double r=group1.getTightRect();
		entry1.setStartX(r.x);
		entry1.setStartY(r.y);
		entry1.setEndX(r.x+r.width);
		entry1.setEndY(r.y+r.height);

		newRoot.entry[0]=entry1;
		group1.parent=newRoot;
		group1.parentIndex=0;
		group1.level=1;

		entry2.child=group2;
		r=group2.getTightRect();
		entry2.setStartX(r.x);
		entry2.setStartY(r.y);
		entry2.setEndX(r.x+r.width);
		entry2.setEndY(r.y+r.height);

		newRoot.entry[1]=entry2;
		group2.parent=newRoot;
		group2.parentIndex=1;
		group2.level=1;

		newRoot.isLeaf=false;
		newRoot.parent=null;
		newRoot.parentIndex=-1;
		newRoot.level=0;

		newRoot.increaseLevel();
		setRoot(newRoot);
	}

	public void delete(IVectorGeographicObject gf) throws CouldntDeleteFeatureException {
		ArrayList result=getRoot().findLeaf(gf.getBoundingMinX(),gf.getBoundingMinY(),gf.getBoundingMaxX(),gf.getBoundingMaxY());
		if (result==null) {
			throw new CouldntDeleteFeatureException();
		}
		Node node2Delete=(Node) (result.get(0));
		int entry2Delete=((Integer) (result.get(1))).intValue();
		node2Delete.howMany--;
		node2Delete.entry[entry2Delete]=node2Delete.entry[node2Delete.howMany];

		condenseTree(node2Delete);

		if ((getRoot().howMany==1) && (!getRoot().isLeaf)) {
			if (getRoot().entry[0].child!=null) {
				Node xnewroot=(Node) (getRoot().entry[0].child);
				xnewroot.parent=null;
				xnewroot.decreaseLevel();
				setRoot(xnewroot);
			}
		}
		index.remove(gf);
	}

	private void condenseTree(Node n2d) {
		ArrayList Q=new ArrayList();
		n2d.removeFromParent(Q);
		for (int i=0;i<Q.size();i++)
			((Node) Q.get(i)).reInsert(this);
	}

	private void print(Node current) {
		if (current==null) return;
		System.out.println("Node "+current);
		for (int i=0;i<current.howMany;i++) {
			System.out.println("level: "+current.level+", isLeaf:"+current.isLeaf+", entry#: "+i+" "+current.entry[i]);
			if (current.entry[i].child instanceof Node)
				print((Node) current.entry[i].child);
			else
				System.out.println("Leaflevel: "+(current.level+1)+", Object: "+current.entry[i].child);
		}
	}
	/**
	 * Debuging method.
	 */
	private void findAllObjects() {
		boolean found=true;
		for (int i=0;i<index.size();i++) {
			IVectorGeographicObject vg=(IVectorGeographicObject) index.get(i);
			ArrayList a=getRoot().findLeaf(vg.getBoundingMinX(),vg.getBoundingMinY(),vg.getBoundingMaxX(),vg.getBoundingMaxY());
			if (a==null) {
				found=false;
				System.out.print(" "+vg.getID());
			}
		}
		if (found)
			System.out.println(" All "+index.size()+" objects found correctly.");
		else
			System.out.println(" NOT all "+index.size()+" objects found correctly!!!");
	}
	/**
	 * Debuging method.
	 */
	private void searchAllObjects() {
		boolean found=true;
		for (int i=0;i<index.size();i++) {
			IVectorGeographicObject vg=(IVectorGeographicObject) index.get(i);
			ArrayList a=search(vg.getBoundingMinX(),vg.getBoundingMinY(),vg.getBoundingMaxX(),vg.getBoundingMaxY());
			if (a.size()==0) {
				found=false;
				break;
			}
		}
		if (found)
			System.out.println("All "+index.size()+" objects searched correctly.");
		else
			System.out.println("NOT all "+index.size()+" objects searched correctly!!!");
	}
	/**
	 * @return The total number of features in this rtree.
	 */
	public int getFeatureCount() {
		if (index!=null)
			return index.size();
		else
			return 0;
	}
	/**
	 * WARNING: This method will return an empty array if it is used with the memory saving mechanism
	 * of Map layers when a layer is not loaded in memory.
	 * @return All the objects contained in the R-Tree.
	 */
	public ArrayList getGeographicObjects() {
		return index;
	}

	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		int size=in.readInt();
		Node root=(Node) in.readObject();
		setRoot(root);

long s=System.currentTimeMillis();
		index=new ArrayList(size);
		for (int i=0;i<size;i++)
			index.add(null);
		root.reconstructIndex(index);
long f=System.currentTimeMillis();
System.out.println("Index reconstruction time "+(f-s));
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(index.size());
		out.writeObject(getRoot());
	}
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID=20000917L;
	/**
	 * The minimum number of allowed entries in a Node.
	 * Must be m <= M/2
	 */
	static final int m=2;
	/**
	 * The maximum number of allowed entries in a Node.
	 */
	static final int M=10;
	/**
	 * The buffer size for reading the shapefile.
	 */
	static final int bufferSize=(int) Math.pow(2,15);
	/**
	 * Stores the geographic features in a row.
	 */
	private ArrayList index;
	/**
	 * Indicates the data precision of the tree.
	 */
	protected int PRECISION;
}