package gr.cti.eslate.shapedComponent;

import java.util.*;

/**
 * Exception thrown when a polygon representation of a non-polygonal shape is
 * requested.
 *
 * @version     3.0.0, 24-May-2006
 * @author      Kriton Kyrimis.
 */
public class NotAPolygonException extends RuntimeException
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.shapedComponent.ShapedComponentResource",
    Locale.getDefault()
  );

  /**
   * Construct a <code>NotAPolyGonException</code> instance.
   */
  public NotAPolygonException()
  {
    super(resources.getString("notPolygon"));
  }
}
