package gr.cti.eslate.mapModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class Entry {
	Object child;

	abstract void setStartX(double d);
	abstract double getStartX();
	abstract void setStartY(double d);
	abstract double getStartY();
	abstract void setEndX(double d);
	abstract double getEndX();
	abstract void setEndY(double d);
	abstract double getEndY();

	boolean isOverlap(double ptAx,double ptAy,double ptBx,double ptBy) {
		return !((ptBx<getStartX()) || (ptAx>getEndX()) || (ptAy>getEndY()) || (ptBy<getStartY()));
	}

	boolean same2Rect(double ptAx,double ptAy,double ptBx,double ptBy) {
		return !((ptAx!=getStartX()) || (ptAy!=getStartY()) || (ptBx!=getEndX()) || (ptBy!=getEndY()));
	}

	boolean contains(double ptAx,double ptAy,double ptBx,double ptBy) {
		return !((ptAx<getStartX()) || (ptAx>getEndX()) || (ptAy<getStartY()) || (ptAy>getEndY()) ||
			(ptBx<getStartX()) || (ptBx>getEndX()) || (ptBy<getStartY()) || (ptBy>getEndY()));
	}

	public String toString() {
		return "Entry ["+getStartX()+","+getStartY()+"] - ["+getEndX()+","+getEndY()+"]";
	}

	/**
	 * Single precision R-Tree Entry object.
	 * @author  Giorgos Vasiliou
	 * @version 2.0, 20-Apr-2002
	 */
	public static class Float extends Entry implements Externalizable {

		Float(double startX,double startY,double endX,double endY,Object child) {
			this((float) startX,(float) startY,(float) endX,(float) endY,child);
		}

		Float(float startX,float startY,float endX,float endY,Object child) {
			this.startX=startX;
			this.startY=startY;
			this.endX=endX;
			this.endY=endY;
			this.child=child;
		}

		public Float() {
			this.startX=0;
			this.startY=0;
			this.endX=0;
			this.endY=0;
			child=null;
		}

		public String toString() {
			return ("Entry ("+startX+","+startY+") - ("+endX+","+endY+")");
		}

		double getStartX() {
			return startX;
		}

		void setStartX(double d) {
			startX=(float) d;
		}

		double getStartY() {
			return startY;
		}

		void setStartY(double d) {
			startY=(float) d;
		}

		double getEndX() {
			return endX;
		}

		void setEndX(double d) {
			endX=(float) d;
		}

		double getEndY() {
			return endY;
		}

		void setEndY(double d) {
			endY=(float) d;
		}

		float startX,startY;
		float endX,endY;

		/**
		 * Externalization input.
		 */
		public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
			startX=in.readFloat();
			startY=in.readFloat();
			endX=in.readFloat();
			endY=in.readFloat();
			child=in.readObject();
		}
		/**
		 * Externalization output.
		 */
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeFloat(startX);
			out.writeFloat(startY);
			out.writeFloat(endX);
			out.writeFloat(endY);
			out.writeObject(child);
		}
		/**
		 * Serial version.
		 */
		private static final long serialVersionUID=20000917L;
	}
	//###End of Float implementation of Entry.



	/**
	 * Double precision R-Tree Entry object.
	 * @author  Giorgos Vasiliou
	 * @version 2.0, 20-Apr-2002
	 */
	public static class Double extends Entry {
		Double(double startX,double startY,double endX,double endY,Object child) {
			this.startX=startX;
			this.startY=startY;
			this.endX=endX;
			this.endY=endY;
			this.child=child;
		}

		public Double() {
			this.startX=0;
			this.startY=0;
			this.endX=0;
			this.endY=0;
			child=null;
		}

		public String toString() {
			return ("Entry ("+startX+","+startY+") - ("+endX+","+endY+")");
		}

		double getStartX() {
			return startX;
		}

		void setStartX(double d) {
			startX=d;
		}

		double getStartY() {
			return startY;
		}

		void setStartY(double d) {
			startY=d;
		}

		double getEndX() {
			return endX;
		}

		void setEndX(double d) {
			endX=d;
		}

		double getEndY() {
			return endY;
		}

		void setEndY(double d) {
			endY=d;
		}

		double startX,startY;
		double endX,endY;
		/**
		 * Externalization input.
		 */
		public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
			startX=in.readDouble();
			startY=in.readDouble();
			endX=in.readDouble();
			endY=in.readDouble();
			child=in.readObject();
		}
		/**
		 * Externalization output.
		 */
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeDouble(startX);
			out.writeDouble(startY);
			out.writeDouble(endX);
			out.writeDouble(endY);
			out.writeObject(child);
		}
		/**
		 * Serial version.
		 */
		private static final long serialVersionUID=20000917L;
	}
	//###End of Double implementation of Entry.
}
