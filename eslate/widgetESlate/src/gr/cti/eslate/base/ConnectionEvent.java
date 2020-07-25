package gr.cti.eslate.base;

import java.util.*;

/**
 * Event triggered when a plug is connected to another plug.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
@SuppressWarnings("unchecked")
public class ConnectionEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private Plug myPlug;
  private Plug otherPlug;
  private int partType;
  private Vector optionalInterfaces;

  /**
   * Constructs a plug connection event.
   * @param     ownPlug         The plug (of the current component) that was
   *                            connected.
   * @param     connectedPlug   The plug (of the component that was connected
   *                            to the current component) that was connected
   *                            to the above plug.
   * @param     type            Indicates to which part (input, output, or pure
   *                            protocol) of a plug the above plug was
   *                            connected.
   *                            Its value is one of Plug.INPUT_CONNECTION,
   *                            Plug.OUTPUT_CONNECTION, and
   *                            Plug.PROTOCOL_CONNECTION.
   * @param     optInt          A list of the optional interfaces specified at
   *                            the construction of the plug, to which plug
   *                            connectedPlug is connected, that the component,
   *                            to which plug connectedPlug belongs, actually
   *                            implements.
   */
  public ConnectionEvent(Plug ownPlug, Plug connectedPlug, int type,
         Vector optInt)
  {
    super(connectedPlug.getHandle().getComponent());
    myPlug = ownPlug;
    otherPlug = connectedPlug;
    partType = type;
    optionalInterfaces = optInt;
  }

  /**
   * Returns the plug (of the component that was connected to this component)
   * that has just been connected.
   */

  public Plug getPlug()
  {
    return otherPlug;
  }

  /**
   * Returns the plug (of the current component) that has just been connected
   * to a plug of another component.
   */
  public Plug getOwnPlug()
  {
    return myPlug;
  }

  /**
   * Returns the part of the plug (input, output, or pure protocol) to which
   * the plug to which the event refers was connected.
   */
  public int getType()
  {
    return partType;
  }

  /**
   * Returns the list of the optional interfaces specified at the
   * construction of the plug, to which the plug returned by getPlug() is
   * connected, that the component, to which plug connectedPlug belongs,
   * actually implements.
   */
  public Vector getOptionalInterfaces()
  {
    return (Vector)(optionalInterfaces.clone());
  }
}
