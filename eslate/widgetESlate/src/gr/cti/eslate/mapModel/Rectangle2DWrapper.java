package gr.cti.eslate.mapModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * A Serializable Rectangle2D.Double.
 * @author Giorgos Vasiliou
 * @version 1.0
 */
public class Rectangle2DWrapper extends java.awt.geom.Rectangle2D.Double implements Serializable {

	public Rectangle2DWrapper() {
		super();
	}

	public Rectangle2DWrapper(double x,double y,double width,double height) {
		super(x,y,width,height);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		DataInputStream din=new DataInputStream(in);
		din.readInt();
		x=din.readDouble();
		y=din.readDouble();
		width=din.readDouble();
		height=din.readDouble();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		DataOutputStream dout=new DataOutputStream(out);
		dout.writeInt(1);
		dout.writeDouble(x);
		dout.writeDouble(y);
		dout.writeDouble(width);
		dout.writeDouble(height);
	}
}