package gr.cti.eslate.base;

import java.util.*;

/**
 * Methods implemented by plugs associated with protocols. This interface
 * unifies protocol plugs and mixed mode plugs. This cannot be done by making
 * mixed mode plugs a subclass of protocol plugs, as ther are already a
 * subclass of shared object plugs.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
@SuppressWarnings("unchecked")
public interface IProtocolPlug
{
  /**
   * Returns the object implementing the protocols that this plug uses to
   * communicate with other plugs.
   * @return    The requested object. This object can be a reference to the
   *            component owning the plug, or to another object to which the
   *            component has delegated the implementation of the protocol.
   */
  public Object getProtocolImplementor();

  /**
   * Returns a reference to the protocol plug that is connected to this plug
   * and has a given name. Protocol plugs are plugs that in addition to (or
   * instead of) providing communication via a shared object, force
   * each of two objects connected via the plug to implement a specific
   * Java interface (different for each of the two components).
   * @param     protocolPlugName        The name of the requested plug.
   * @return    The requested plug.
   */
  public IProtocolPlug getProtocolPlug(String protocolPlugName);

  /**
   * Returns a list of all protocol plugs connected to this plug.
   * Protocol plugs are plugs that in addition to (or
   * instead of) providing communication via a shared object, force
   * each of two objects connected via the plug to implement a specific
   * Java interface (different for each of the two components).
   * associated with a shared object.
   * @return    The protocol plugs.
   */
  public IProtocolPlug[] getProtocolPlugs();

  /**
   * For plugs where there is only one component connected to them that
   * communicates with these plugs through a Java interface, this method
   * provides a shortcut that returns the protocol plug directly, rather than
   * through an array.
   * @return    The protocol plug.
   * @exception NoSingleConnectedComponentException     This exception is
   *            thrown when there is not exactly one protocol plug connected to
   *            this plug.
   *
   */
  public IProtocolPlug getProtocolPlug()
    throws NoSingleConnectedComponentException;

  /**
   * Returns the required Java interfaces used to communicate with this plug.
   * @return    A list of Class instances corresponding to the requested
   *            interfaces.
   */
  public Vector getRequiredInterfaces();

  /**
   * Returns the Java interfaces that can be optionally used to communicate
   * with this plug. This is a copy of the list of interfaces specified during
   * the plug's construction. The interfaces that a component connected to this
   * plug implements is a subset of this list.
   * @return    A list of Class instances corresponding to the requested
   *            interfaces.
   */
  public Vector getOptionalInterfaces();

  /**
   * Specifies the Java interfaces that the plug declares that the protocol
   * implementor implements.
   * @param     exported        A vector of the Java interfaces that the
   *                            plug declares that the protocol implementor
   *                            implements. Setting this to null is equivalent
   *                            to declaring that the protocol implementor
   *                            implements all the interfaces that it actually
   *                            implements.
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when the declared
   *                            interfaces cannot be accepted.
   */
  public void setExportedInterfaces(Vector exported)
    throws InvalidPlugParametersException;

  /**
   * Returns the Java interfaces that the plug declares that the protocol
   * implementor implements. If no interfaces have been explicitly declared,
   * this method returns a list of all the interfaces implemented by the
   * protocol implementor.
   * @return    A vector containing the requested interfaces.
   */
  public Vector getExportedInterfaces();

  /**
   * Returns a list of the handles of the components connected to this plug
   * that communicate with this plug through a Java interface.
   * @return    The E-Slate handles of the components.
   */
  public ESlateHandle[] getProtocolHandles();

  /**
   * For plugs where there is only one component connected to them that
   * communicates with these plugs through a Java interface, this method
   * provides a shortcut that returns the component directly, rather than
   * through an array.
   * @return    The E-Slate handle of the component.
   * @exception NoSingleConnectedComponentException     This exception is
   *            thrown when there is not exactly one component connected to
   *            this plug.
   */
  public Object getProtocolHandle() throws NoSingleConnectedComponentException;

}
