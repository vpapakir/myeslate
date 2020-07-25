package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IncompatibleObjectTypeException;

import java.awt.geom.Rectangle2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

public abstract class Node {
	Entry entry[];
	int howMany;
	boolean isLeaf;
	Node parent;
	int parentIndex;
	int level;

	private Node(Node parent,int level) {
		howMany=0;
		this.parent=parent;
		parentIndex=-1;
		this.level=level;
	}

	public Node() {
		howMany=0;
		parent=null;
		parentIndex=-1;
		level=0;
	}

	void split(Entry newEntry, Node group1, Node group2) {
		Node poorGroup;
		int last;
		Entry[] nodes;
		Entry temp1,temp2,temp3,temp4,first,second;
		temp1=temp2=temp3=temp4=null;

		if (this instanceof Float) {
			nodes=new Entry.Float[howMany+1];
		} else {
			nodes=new Entry.Double[howMany+1];
		}

		System.arraycopy(entry,0,nodes,0,howMany);
		nodes[howMany]=newEntry;
		last=howMany;

		group1.howMany=0;
		group1.isLeaf=isLeaf;
		group1.parentIndex=parentIndex;
		group1.parent=parent;
		group1.level=level;

		if (parent!=null) parent.entry[parentIndex].child=group1;

		/********Linear PickSeeds Algorithm********/
		double lhs=java.lang.Double.MAX_VALUE,hls=-lhs;
		double Xseparate,Yseparate;
		double Xmin,Ymin,Xmax,Ymax;

		Xmin=Ymin=lhs;
		Xmax=Ymax=-lhs;

		for (int i=0;i<=last;i++) {
			if (nodes[i].getStartX()<Xmin) Xmin=nodes[i].getStartX();
			if (nodes[i].getEndX()>Xmax) Xmax=nodes[i].getEndX();
		}

		for (int i=0;i<=last;i++) {
			if (nodes[i].getStartY()<Ymin) Ymin=nodes[i].getStartY();
			if (nodes[i].getEndY()>Ymax) Ymax=nodes[i].getEndY();
		}

		//Find the extreme rectangles along the x dimension
		for (int i=0;i<=last;i++) {
			if (nodes[i].getEndX()<lhs) {
				lhs=nodes[i].getEndX();
				temp1=nodes[i];
			}
			if (nodes[i].getStartX()>hls) {
				hls=nodes[i].getStartX();
				temp2=nodes[i];
			}
		}

		//Normalize the x dimension separation
		Xseparate=Math.abs(lhs-hls)/(Xmax-Xmin);

		//Find the extreme rectangles along the y dimension
		lhs=java.lang.Double.MAX_VALUE; hls=-lhs;
		for (int i=0;i<=last;i++) {
			if (nodes[i].getEndY()<lhs) {
				lhs=nodes[i].getEndY();
				temp3=nodes[i];
			}
			if (nodes[i].getStartY()>hls) {
				hls=nodes[i].getStartY();
				temp4=nodes[i];
			}
		}

		//Normalize the y dimension separation
		Yseparate=Math.abs(lhs-hls)/(Ymax-Ymin);

		//Choose the pair with the greatest normalized separation
		if (Xseparate<Yseparate) {
			first=temp3;
			second=temp4;
		} else {
			first=temp1;
			second=temp2;
		}

		/********End of Linear PickSeeds Algorithm********/

		group1.entry[group1.howMany]=first;
		if (first.child instanceof Node) {
			((Node) first.child).parentIndex=group1.howMany;
			((Node) first.child).parent=group1;
		}
		group1.howMany++;

		group2.howMany=0;
		group2.isLeaf=isLeaf;
		group2.entry[group2.howMany]=second;
		if (second.child instanceof Node) {
			((Node) second.child).parentIndex=group2.howMany;
			((Node) second.child).parent=group2;
		}
		group2.howMany++;
		group2.level=level;

		if (group1.howMany<group2.howMany) poorGroup=group1;
		else poorGroup=group2;

		//Remove the Seeds from the list of entries
		for (int i=0;i<=last;i++)
			if (nodes[i]==first) {
				for (int k=i;k<last;k++) nodes[k]=nodes[k+1];
				i=last+5; /*Exit for*/
				last--;
			}
		for (int i=0;i<=last;i++)
			if (nodes[i]==second) {
				for (int k=i;k<last;k++) nodes[k]=nodes[k+1];
				i=last+5; /*Exit for*/
				last--;
			}

		double d1,d2,maxDiff;
		int choicenum=0;
		while (((last+1)+poorGroup.howMany>RTree.m) && (last>0)) {
			/********Algorithm PickNext********/
			maxDiff=-java.lang.Double.MAX_VALUE;
			for (int i=0;i<=last;i++) {
				d1=group1.nodeEnlargement(nodes[i]);
				d2=group2.nodeEnlargement(nodes[i]);
				if (Math.abs(d1-d2)>maxDiff) {
					maxDiff=Math.abs(d1-d2);
					choicenum=i;
				}
			}
			/********End of Algorithm PickNext********/
			if (group1.nodeEnlargement(nodes[choicenum])>group2.nodeEnlargement(nodes[choicenum])) {
				group2.entry[group2.howMany]=nodes[choicenum];
				if (nodes[choicenum].child instanceof Node) {
					((Node) nodes[choicenum].child).parentIndex=group2.howMany;
					((Node) nodes[choicenum].child).parent=group2;
				}
				group2.howMany++;
			} else {
				group1.entry[group1.howMany]=nodes[choicenum];
				if (nodes[choicenum].child instanceof Node) {
					((Node) nodes[choicenum].child).parentIndex=group1.howMany;
					((Node) nodes[choicenum].child).parent=group1;
				}
				group1.howMany++;
			}
			nodes[choicenum]=nodes[last]; /*delete the node that is picked up*/
			last--;

			if (group1.howMany<group2.howMany) poorGroup=group1;
			else poorGroup=group2;
		}

		for (int i=0;i<=last;i++) {
			poorGroup.entry[poorGroup.howMany]=nodes[i];
			if (nodes[i].child instanceof Node) {
				((Node) nodes[i].child).parentIndex=poorGroup.howMany;
				((Node) nodes[i].child).parent=poorGroup;
			}
			poorGroup.howMany++;
		}
	}

	int findLessEnlargement(double ptAx,double ptAy,double ptBx,double ptBy) {
		int choiceIndex=0;
		double enlarge, tempEnlarge;
		double Xmin,Ymin,Xmax,Ymax;
		enlarge=java.lang.Double.MAX_VALUE;

		for (int i=0;i<howMany;++i) {
			Xmin=Math.min(ptAx,entry[i].getStartX());
			Ymin=Math.min(ptAy,entry[i].getStartY());
			Xmax=Math.max(ptBx,entry[i].getEndX());
			Ymax=Math.max(ptBy,entry[i].getEndY());

			tempEnlarge=(Xmax-Xmin)*(Ymax-Ymin) -
						(entry[i].getEndX()-entry[i].getStartX())*(entry[i].getEndY()-entry[i].getStartY());
			if (enlarge>tempEnlarge) {
				enlarge=tempEnlarge;
				choiceIndex=i;
			}
		}

		return choiceIndex;
	}

	Rectangle2D.Double getTightRect() {
		double Xmin,Ymin,Xmax,Ymax;

		Xmin=Ymin=java.lang.Double.MAX_VALUE;
		Xmax=Ymax=-Xmin;
		for (int i=0;i<howMany;i++) {
			Xmin=Math.min(Xmin,entry[i].getStartX());
			Ymin=Math.min(Ymin,entry[i].getStartY());
			Xmax=Math.max(Xmax,entry[i].getEndX());
			Ymax=Math.max(Ymax,entry[i].getEndY());
		}
		return new Rectangle2D.Double(Xmin,Ymin,Xmax-Xmin,Ymax-Ymin);
	}

	void increaseLevel() {
		level++;
		if (isLeaf) return;

		for (int i=0;i<howMany;i++)
			((Node) (entry[i].child)).increaseLevel();
	}

	void decreaseLevel() {
		level--;
		if (isLeaf) return;
		for (int i=0;i<howMany;i++)
			((Node) (entry[i].child)).decreaseLevel();
	}


	private double nodeEnlargement(Entry entry) {
		double Xmin,Ymin,Xmax,Ymax;

		Xmin=Ymin=java.lang.Double.MAX_VALUE;
		Xmax=Ymax=-Xmin;
		Rectangle2D.Double r=getTightRect();

		Xmin=Math.min(r.x,entry.getStartX());
		Ymin=Math.min(r.y,entry.getStartY());
		Xmax=Math.max(r.x+r.width,entry.getEndX());
		Ymax=Math.max(r.y+r.height,entry.getEndY());

		return (Xmax-Xmin)*(Ymax-Ymin)-r.width*r.height;
	}

	private String print() {
		String s;
		s="Node level " + level +"\n";
		for (int i=0;i<howMany;i++)
			s+="[("+entry[i].getStartX()+","+entry[i].getStartY()+"),("+entry[i].getEndX()+","+entry[i].getEndY()+")] ";
		s+="\n";
		if (isLeaf) return s;
		for (int i=0;i<howMany;i++)
			s+=((Node) (entry[i].child)).print();
		return s;
	}

	void search(double startX,double startY,double endX,double endY,ArrayList found) {
		for (int i=0;i<howMany;i++) {
			if (entry[i].isOverlap(startX,startY,endX,endY)) {
				if (isLeaf) {
					found.add(entry[i].child);
				} else
					((Node) entry[i].child).search(startX,startY,endX,endY,found);
			}
		}
	}

	protected ArrayList findLeaf(double ptLeftx,double ptLefty,double ptRightx,double ptRighty) {
		for (int i=0;i<howMany;++i) {
			if (entry[i].contains(ptLeftx,ptLefty,ptRightx,ptRighty)) {
				if ((isLeaf) && (entry[i].same2Rect(ptLeftx,ptLefty,ptRightx,ptRighty))) {
					ArrayList a=new ArrayList(2);
					a.add(this);
					a.add(new Integer(i));
					return a;
				}
				if (!isLeaf) {
					ArrayList temp=((Node) (entry[i].child)).findLeaf(ptLeftx,ptLefty,ptRightx,ptRighty);
					if (temp!=null)
						return temp;
				}
			}
		}
		return null;
	}

	protected void removeFromParent(ArrayList Q) {
		if (parent==null) return; /* This is the root */
		if (howMany<RTree.m) {
			parent.howMany--;
			parent.entry[parentIndex]=parent.entry[parent.howMany];
			((Node) parent.entry[parentIndex].child).parentIndex=parentIndex;
			Q.add(this);
		} else {
			Rectangle2D.Double r=getTightRect();
			parent.entry[parentIndex].setStartX(r.x);
			parent.entry[parentIndex].setStartY(r.y);
			parent.entry[parentIndex].setEndX(r.x+r.width);
			parent.entry[parentIndex].setEndY(r.y+r.height);
		}
		parent.removeFromParent(Q);
	}

	protected void reInsert(RTree rtree) {
		if (isLeaf) {
			if (this instanceof Float) {
				for (int i=0;i<howMany;i++)
					try {
						rtree.insert(new Entry.Float(entry[i].getStartX(),entry[i].getStartY(),entry[i].getEndX(),entry[i].getEndY(),(GeographicObject) entry[i].child),false);
					} catch(IncompatibleObjectTypeException e) {e.printStackTrace();}
			} else {
				for (int i=0;i<howMany;i++)
					try {
						rtree.insert(new Entry.Double(entry[i].getStartX(),entry[i].getStartY(),entry[i].getEndX(),entry[i].getEndY(),(GeographicObject) entry[i].child),false);
					} catch(IncompatibleObjectTypeException e) {e.printStackTrace();}
			}
		} else {
			insertNonLeaf(rtree);
		}
	}

	private void insertNonLeaf(RTree rtree) {
		for (int i=0;i<howMany;i++) {
			if (((Node) entry[i].child).isLeaf)
				((Node) entry[i].child).reInsert(rtree);
			else
				((Node) entry[i].child).insertNonLeaf(rtree);
		}
	}

	protected void checkRectangle() {
		if (!isLeaf) {
			for (int i=0;i<howMany;i++) {
				Rectangle2D.Double r=((Node) entry[i].child).getTightRect();
				if (!(r.x==entry[i].getStartX() && r.y==entry[i].getStartY()) || !((r.x+r.width)==entry[i].getEndX() && (r.y+r.height)==entry[i].getEndY()))
					System.out.println("Incorrect Rectangle in level "+level+"!!!!!");
				((Node) entry[i].child).checkRectangle();
			}
		}
	}

	/**
	 * Debuging method.
	 */
	protected int countNodes() {
		int c=0;
		if (isLeaf) return 1;
		for (int i=0;i<howMany;i++) {
			c=c+((Node) entry[i].child).countNodes();
		}
		return c;
	}
	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		howMany=in.readInt();
		isLeaf=in.readBoolean();
		parentIndex=in.readInt();
		level=in.readInt();
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(howMany);
		out.writeBoolean(isLeaf);
		out.writeInt(parentIndex);
		out.writeInt(level);
	}

	protected void reconstructIndex(ArrayList arr) {
		for (int i=entry.length-1;i>-1;i--)
			if (entry[i]!=null) {
				if (entry[i].child instanceof Node)
					((Node)entry[i].child).reconstructIndex(arr);
				else if (entry[i].child instanceof GeographicObject)
					arr.set(((GeographicObject)entry[i].child).getID(),((GeographicObject)entry[i].child));
			}
	}


	/**
	 * Single precision implementation of RTree Node.
	 * @author  Giorgos Vasiliou
	 * @version 1.0, 22-Apr-2002
	 */
	public static class Float extends Node implements Externalizable {
		Float(Node parent,int level) {
			super(parent,level);
			entry=new Entry.Float[RTree.M];
		}

		public Float() {
			super();
		}
		/**
		 * Externalization input.
		 */
		public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
			super.readExternal(in);
			entry=(Entry.Float[])in.readObject();
			for (int i=entry.length-1;i>-1;i--)
				if (entry[i]!=null && entry[i].child instanceof Node)
					((Node)entry[i].child).parent=this;
		}
		/**
		 * Externalization output.
		 */
		public void writeExternal(ObjectOutput out) throws IOException {
			super.writeExternal(out);
			out.writeObject(entry);
		}
		/**
		 * Serial version.
		 */
		private static final long serialVersionUID=20000917L;
	}


	/**
	 * Double precision implementation of RTree Node.
	 * @author  Giorgos Vasiliou
	 * @version 1.0, 22-Apr-2002
	 */
	public static class Double extends Node implements Externalizable {
		Double(Node parent,int level) {
			super(parent,level);
			entry=new Entry.Double[RTree.M];
		}

		public Double() {
			super();
		}

		/**
		 * Externalization input.
		 */
		public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
			super.readExternal(in);
			entry=(Entry.Double[])in.readObject();
		}
		/**
		 * Externalization output.
		 */
		public void writeExternal(ObjectOutput out) throws IOException {
			super.writeExternal(out);
			out.writeObject(entry);
		}
		/**
		 * Serial version.
		 */
		private static final long serialVersionUID=20000917L;
	}
}
