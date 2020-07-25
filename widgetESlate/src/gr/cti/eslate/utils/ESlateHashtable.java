package gr.cti.eslate.utils;

import java.util.Hashtable;
import java.awt.Image;
import java.awt.Color;
import javax.swing.Icon;

/**
 * Helper class that implements a hashtable
 * where primitive types may be put in. Furthermore
 * there is no need to cast the output or get the original
 * primitive type from a class. There is null checking so
 * null values cannot be inserted, as they would produce an
 * Exception. It is safe to put an object in the hashtable
 * without checking if it is null. In the get methods a default
 * value is provided, thus defining the expected type as well
 * as making the hashtable always provide a value from the get
 * method. This can be useful in externalization and the
 * different versions of the same class.
 *
 * @author      Giorgos Vasiliou
 * @version     2.0.0, 18-May-2006
 */
@SuppressWarnings(value={"unchecked"})
public class ESlateHashtable extends Hashtable {
    public Object put(Object key,boolean b) {
        return super.put(key,new Boolean(b));
    }

    public Object put(Object key,byte b) {
        return super.put(key,new Byte(b));
    }

    public Object put(Object key,short s) {
        return super.put(key,new Short(s));
    }

    public Object put(Object key,int i) {
        return super.put(key,new Integer(i));
    }

    public Object put(Object key,long l) {
        return super.put(key,new Long(l));
    }

    public Object put(Object key,float f) {
        return super.put(key,new Float(f));
    }

    public Object put(Object key,double d) {
        return super.put(key,new Double(d));
    }
    /**
     * If an object is null don't put anything in the Hashtable.
     * This is to avoid null checking from code before inserting
     * into the Hashtable.
     */
    public Object put(Object key,Object o) {
        if (o!=null)
            return super.put(key,o);
        return null;
    }

    public boolean get(Object key,boolean defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof Boolean))
            return defValue;
        else
            return ((Boolean) o).booleanValue();
    }

    public byte get(Object key,byte defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof Byte))
            return defValue;
        else
            return ((Byte) o).byteValue();
    }

    public short get(Object key,short defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof Short))
            return defValue;
        else
            return ((Short) o).shortValue();
    }

    public int get(Object key,int defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof Integer))
            return defValue;
        else
            return ((Integer) o).intValue();
    }

    public long get(Object key,long defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof Long))
            return defValue;
        else
            return ((Long) o).longValue();
    }

    public float get(Object key,float defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof Float))
            return defValue;
        else
            return ((Float) o).floatValue();
    }

    public double get(Object key,double defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof Double))
            return defValue;
        else
            return ((Double) o).doubleValue();
    }

    public String get(Object key,String defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof String))
            return defValue;
        else
            return (String) o;
    }

    public Object get(Object key,Object defValue) {
        Object o=super.get(key);
        if (o==null)
            return defValue;
        else
            return o;
    }

    public Icon get(Object key,Icon defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof Icon))
            return defValue;
        else
            return (Icon) o;
    }

    public Image get(Object key,Image defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof Image))
            return defValue;
        else
            return (Image) o;
    }

    public Color get(Object key,Color defValue) {
        Object o=super.get(key);
        if ((o==null) || !(o instanceof Color))
            return defValue;
        else
            return (Color) o;
    }

    public boolean getBoolean(Object key) {
        return ((Boolean) super.get(key)).booleanValue();
    }

    public byte getByte(Object key) {
        return ((Byte) super.get(key)).byteValue();
    }

    public short getShort(Object key) {
        return ((Short) super.get(key)).shortValue();
    }

    public int getInteger(Object key) {
        return ((Integer) super.get(key)).intValue();
    }

    public long getLong(Object key) {
        return ((Long) super.get(key)).longValue();
    }

    public float getFloat(Object key) {
        return ((Float) super.get(key)).floatValue();
    }

    public double getDouble(Object key) {
        return ((Double) super.get(key)).doubleValue();
    }

    static final long serialVersionUID=-2129349650832032900L;
}
