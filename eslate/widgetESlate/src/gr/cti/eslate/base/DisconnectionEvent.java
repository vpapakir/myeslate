package gr.cti.eslate.base;

/**
 * Event triggered when a plug is disconnected from another plug.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class DisconnectionEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private Plug myPlug;
  private Plug otherPlug;
  private int partType;

  /**
   * Constructs a plug disconnection event.
   * @param     ownPlug         The plug (of the current component) that was
   *                            disconnected.
   * @param     disconnectedPlug        The plug (of the component that was
   *                            disconnected from the current component) that
   *                            was disconnected from the above plug.
   * @param     type            Indicates from which part (input, output,
   *                            or pure protocol) of a plug the above plug was
   *                            disconnected.
   *                            Its value is one of Plug.INPUT_CONNECTION,
   *                            Plug.OUTPUT_CONNECTION, and
   *                            Plug.PROTOCOL_CONNECTION.
   */
  public DisconnectionEvent(Plug ownPlug, Plug disconnectedPlug, int type)
  {
    super(disconnectedPlug.getHandle().getComponent());
    myPlug = ownPlug;
    otherPlug = disconnectedPlug;
    partType = type;
  }

  /**
   * Returns the plug (of the component that was disconnected from this
   * component) that has just been connected.
   */
  public Plug getPlug()
  {
    return otherPlug;
  }

  /**
   * Returns the plug (of the current component) that has just been
   * disconnected from a plug of another component.
   */
  public Plug getOwnPlug()
  {
    return myPlug;
  }

  /**
   * Returns the part of the plug (input, output, or pure protocol) from which
   * the plug to which the event refers was disconnected.
   */
  public int getType()
  {
    return partType;
  }
}
