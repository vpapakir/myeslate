package gr.cti.eslate.mapModel;

import gr.cti.eslate.mapModel.geom.Point;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IncompatibleObjectTypeException;

/**
 * A Point RTree.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	2.0, 17-May-2002
 * @see gr.cti.eslate.mapModel.RTree
 */
public abstract class PointRTree extends RTree {
	/**
	 * Single presicion implementation of PointRTrees.
	 * @author  Giorgos Vasiliou
	 * @version 2.0, 22-Apr-2002
	 */
	public static class Float extends PointRTree {
	public Float() {
		super();
		root=new Node.Float(null,1);
		root.isLeaf=true;
		PRECISION=IMapView.SINGLE_PRECISION;
	}

	public void insert(double startX,double startY,double endX,double endY,GeographicObject shape) throws IncompatibleObjectTypeException {
		if ((shape instanceof Point) || (shape instanceof point))
			super.insert(new Entry.Float(startX,startY,endX,endY,shape));
		else
			throw new IncompatibleObjectTypeException(shape.getClass().toString());
	}

	public void setRoot(Node r) {
		root=(Node.Float) r;
	}

	public Node getRoot() {
		return root;
	}

	/**
	 * The tree root.
	 */
	private Node.Float root;
	/**
	 * Indicates the data precision of the tree.
	 */
	protected int PRECISION=IMapView.SINGLE_PRECISION;
	}



	/**
	 * Double presicion implementation of PointRTrees.
	 * @author  Giorgos Vasiliou
	 * @version 2.0, 22-Apr-2002
	 */
	public static class Double extends PointRTree {
	public Double() {
		super();
		root=new Node.Double(null,1);
		root.isLeaf=true;
		PRECISION=IMapView.DOUBLE_PRECISION;
	}

	public void insert(double startX,double startY,double endX,double endY,GeographicObject shape) throws IncompatibleObjectTypeException {
		if ((shape instanceof Point) || (shape instanceof point))
			super.insert(new Entry.Double(startX,startY,endX,endY,shape));
		else
			throw new IncompatibleObjectTypeException(shape.getClass().toString());
	}

	public void setRoot(Node r) {
		root=(Node.Double) r;
	}

	public Node getRoot() {
		return root;
	}

	/**
	 * The tree root.
	 */
	private Node.Double root;
	}
}
