package gr.cti.eslate.registry;

import java.io.*;

/**
 * This class implements a pair consisting of an object and a flag specifying
 * whether this object is persistent. The variable registry, when saving its
 * state, will not save objects that have not been marked as persistent.
 *
 * @version     2.0.0, 25-May-2006
 * @author      Kriton Kyrimis
 */
public class ValuePair implements Externalizable
{
  Object value;
  boolean persistent;

  static final long serialVersionUID = 1L;

  /**
   * Construct a pair.
   */
  public ValuePair()
  {
    this(null, false);
  }

  /**
   * Construct a pair.
   * @param     value           The object.
   * @param     persistent      True, if the object is persistent,
   *                            false otherwise.
   */
  public ValuePair(Object value, boolean persistent)
  {
    this.value = value;
    this.persistent = persistent;
  }

  /**
   * Save the pair.
   * @param     oo      The stream where the pair should be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    if (value == null ||
        !(value instanceof Externalizable || value instanceof Serializable)) {
      oo.writeObject(null);
    }else{
      oo.writeObject(value.getClass().getName());
      if (value instanceof Externalizable) {
        ((Externalizable)value).writeExternal(oo);
      }else{
        oo.writeObject(value);
      }
    }
  }

  /**
   * Load the pair.
   * @param     oi      The stream from where the pair should be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    try {
      persistent = true;
      String className = (String)(oi.readObject());
      if (className == null) {
        value = null;
      }else{
        Class c = Class.forName(className);
        if (Externalizable.class.isAssignableFrom(c)) {
          value = c.newInstance();
          ((Externalizable)value).readExternal(oi);
        }else{
          value = oi.readObject();
        }
      }
    } catch (IllegalAccessException iae) {
      System.out.println("***BEGIN STACK TRACE");
      iae.printStackTrace();
      System.out.println("***END STACK TRACE");
      throw new IOException(iae.getMessage());
    } catch (InstantiationException iae) {
      System.out.println("***BEGIN STACK TRACE");
      iae.printStackTrace();
      System.out.println("***END STACK TRACE");
      throw new IOException(iae.getMessage());
    }
  }
}
