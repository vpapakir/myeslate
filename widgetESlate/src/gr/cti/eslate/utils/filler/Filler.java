package gr.cti.eslate.utils.filler;

import java.awt.Component;
import java.awt.Dimension;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;


/**
 * Ôhe super class of the rest of the classes in this package. This class is
 * provided as a common ancestor. It is not a Bean, so it cannot be
 * instantiated through E-Slate. The classes of this package are used to
 * align components in AWT Containers that use the <code>BoxLayout</code>
 * layout manager.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 */
public class Filler extends Component implements Externalizable
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 7513899257133816425L;
  
  private static int STR_FORMAT_VERSION = 2;
  // ---- member variables ---------------------------------------
  Dimension reqMin;
  Dimension reqPref;
  Dimension reqMax;

  /**
   * Constructor to create shape with the given size ranges.
   *
   * @param min   Minimum size
   * @param pref  Preferred size
   * @param max   Maximum size
   */
  public Filler(Dimension min, Dimension pref, Dimension max)
  {
    reqMin = min;
    reqPref = pref;
    reqMax = max;
  }

  /**
   * Change the size requests for this shape.  An invalidate() is
   * propagated upward as a result so that layout will eventually
   * happen with using the new sizes.
   *
   * @param min   Value to return for getMinimumSize
   * @param pref  Value to return for getPreferredSize
   * @param max   Value to return for getMaximumSize
   */
  public void changeShape(Dimension min, Dimension pref, Dimension max)
  {
    reqMin = min;
    reqPref = pref;
    reqMax = max;
    invalidate();
  }

  // ---- Component methods ------------------------------------------

  /**
   * Returns the minimum size of the component.
   *
   * @return the size
   */
  public Dimension getMinimumSize()
  {
    return reqMin;
  }

  /**
   * Returns the preferred size of the component.
   *
   * @return the size
   */
  public Dimension getPreferredSize()
  {
    return reqPref;
  }

  /**
   * Returns the maximum size of the component.
   *
   * @return the size
   */
  public Dimension getMaximumSize()
  {
    return reqMax;
  }

  /**
   *  Reads from a StorageStructure the properties of the shape.
   */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    Object firstObj = in.readObject();
    StorageStructure fieldMap = (StorageStructure) firstObj;

    reqMin = (Dimension) fieldMap.get("reqMin");
    reqPref = (Dimension) fieldMap.get("reqPref");
    reqMax = (Dimension) fieldMap.get("reqMax");
  }

  /**
    * Writes to the StorageStructure values and properties to be stored.
    */
  public void writeExternal(ObjectOutput out) throws IOException
  {
    ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STR_FORMAT_VERSION, 3);
    fieldMap.put("reqMin", reqMin);
    fieldMap.put("reqPref", reqPref);
    fieldMap.put("reqMax", reqMax);
    out.writeObject(fieldMap);
  }

}
