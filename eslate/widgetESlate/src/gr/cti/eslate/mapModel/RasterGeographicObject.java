package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.IRasterGeographicObject;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * An implementation of the IRasterGeographicObject interface, an object that exists in raster layers.
 * <P>
 *
 * @author  Giorgos Vasiliou
 * @version 1.0, 5-Apr-2001
 */
public class RasterGeographicObject implements IRasterGeographicObject,Externalizable {
	/**
	 * No arg-constructor for externalization.
	 */
	public RasterGeographicObject() {
		id=0;
		visible=true;
		selected=false;
	}

	public RasterGeographicObject(int id) {
		this.id=id;
		visible=true;
		selected=false;
	}
	/**
	 * Sets the visibility of the object.
	 * @param value The new visibility status.
	 */
	public void setVisible(boolean value) {
		visible=value;
	}
	/**
	 * Gets the visibility property.
	 * @return The visibility property.
	 */
	public boolean isVisible() {
		return visible;
	}
	/**
	 * Sets the selected state of the object.
	 * @param value The new selected status.
	 */
	public void setSelected(boolean value) {
		selected=value;
	}
	/**
	 * Gets the selected property.
	 * @return The selected property.
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * Sets the object id which is its
	 * reference to the associated database table.
	 */
	public void setID(int i) {
		id=i;
	}
	/**
	 * Gets the object id which is its
	 * reference to the associated database table.
	 */
	public int getID() {
		return id;
	}
	/**
	 * Externalization input.
	 * @param in The output stream.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		id=ht.get("id",0);
		visible=ht.get("visible",true);
		selected=ht.get("selected",false);
	}
	/**
	 * Externalization output.
	 * @param out The input stream.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("id",id);
		ht.put("visible",visible);
		ht.put("selected",selected);
		out.writeObject(ht);
	}

	/**
	 * The serial version UID.
	 */
	static final long serialVersionUID=3000L;
	private int id;
	private boolean visible,selected;
}
