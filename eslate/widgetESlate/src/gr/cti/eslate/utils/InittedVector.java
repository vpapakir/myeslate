package gr.cti.eslate.utils;

import java.util.*;

/**
 * Implements a vector whose constructor adds a given argument to the vector.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
@SuppressWarnings(value={"unchecked"})
public class InittedVector extends Vector
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Construct an InittedVector and add a given element to it.
   * @param     x       The object to add to the vector. If x is null, then
   *                    the vector is left empty.
   */
  public InittedVector(Object x)
  {
    super();
    if (x != null) {
      this.addElement(x);
    }
  }
}
