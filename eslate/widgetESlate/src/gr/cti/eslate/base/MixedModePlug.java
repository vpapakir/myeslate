package gr.cti.eslate.base;

import gr.cti.eslate.base.sharedObject.*;
import java.awt.Color;
import java.util.*;
import java.lang.Class;

/**
 * Base class for plugs associated with both a shared object and with
 * protocols.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public abstract class MixedModePlug extends SharedObjectPlug
  implements IProtocolPlug
{
  /**
   * Returns the object implementing the protocols that this plug uses to
   * communicate with other plugs.
   * @return    The requested object. This object can be a reference to the
   *            component owning the plug, or to another object to which the
   *            component has delegated the implementation of the protocol.
   */
  public Object getProtocolImplementor()
  {
    return internals.getProtocolImplementor();
  }

  /**
   * Returns a reference to the protocol plug that is connected to this plug
   * and has a given name. Protocol plugs are plugs that in addition to (or
   * instead of) providing communication via a shared object, force
   * each of two objects connected via the plug to implement a specific
   * Java interface (different for each of the two components).
   * @param     protocolPlugName        The name of the requested plug.
   * @return    The requested plug.
   */
  public IProtocolPlug getProtocolPlug(String protocolPlugName)
  {
    return internals.getProtocolPlug(protocolPlugName);
  }

  /**
   * Returns a list of all protocol plugs connected to this plug.
   * Protocol plugs are plugs that in addition to (or
   * instead of) providing communication via a shared object, force
   * each of two objects connected via the plug to implement a specific
   * Java interface (different for each of the two components).
   * associated with a shared object.
   * @return    The protocol plugs.
   */
  public IProtocolPlug[] getProtocolPlugs()
  {
    return internals.getProtocolPlugs();
  }

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
    throws NoSingleConnectedComponentException
  {
    return internals.getProtocolPlug();
  }

  /**
   * Adds a plug to the list of protocol plugs connected to this plug.
   * @param     protocolPlug    The plug to add.
   */
  void addProtocolPlug(Plug protocolPlug)
  {
    internals.addProtocolPlug(protocolPlug);
  }

  /**
   * Removes a plug from the list of protocol plugs connected to this plug.
   * @param     protocolPlug    The plug to remove.
   */
  void removeProtocolPlug(Plug protocolPlug)
  {
    internals.removeProtocolPlug(protocolPlug);
  }

  /**
   * Returns whether a given plug is used to communicate with this plug
   * via a Java interface.
   * @param     p       The plug to check.
   * @return    True if yes, otherwise false.
   */
  boolean hasProtocolPlug(Plug p)
  {
    return internals.hasProtocolPlug(p);
  }

  /**
   * Returns the required Java interfaces used to communicate with this plug.
   * @return    A list of Class instances corresponding to the requested
   *            interfaces.
   */
  @SuppressWarnings("unchecked")
  public Vector getRequiredInterfaces()
  {
    return internals.getRequiredInterfaces();
  }

  /**
   * Returns the Java interfaces that can be optionally used to communicate
   * with this plug. This is a copy of the list of interfaces specified during
   * the plug's construction. The interfaces that a component connected to this
   * plug implements is a subset of this list.
   * @return    A list of Class instances corresponding to the requested
   *            interfaces.
   */
  @SuppressWarnings("unchecked")
  public Vector getOptionalInterfaces()
  {
    return internals.getOptionalInterfaces();
  }

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
  @SuppressWarnings("unchecked")
  public void setExportedInterfaces(Vector exported)
    throws InvalidPlugParametersException
  {
    internals.setExportedInterfaces(exported);
  }

  /**
   * Returns the Java interfaces that the plug declares that the protocol
   * implementor implements. If no interfaces have been explicitly declared,
   * this method returns a list of all the interfaces implemented by the
   * protocol implementor.
   * @return    A vector containing the requested interfaces.
   */
  @SuppressWarnings("unchecked")
  public Vector getExportedInterfaces()
  {
    return internals.getExportedInterfaces();
  }

  /**
   * Returns a list of the handles of the components connected to this plug
   * that communicate with this plug through a Java interface.
   * @return    The E-Slate handles of the components.
   */
  public ESlateHandle[] getProtocolHandles()
  {
    return internals.getProtocolHandles();
  }

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
  public Object getProtocolHandle() throws NoSingleConnectedComponentException
  {
    return internals.getProtocolHandle();
  }

  // Inherit all the constructors of the Plug class, so that they can be
  // invoked by subclasses.

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, l);
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, Object implementor,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, implementor, l);
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, String name, Color cl,
                Class shObjClass, SharedObject so, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, name, cl, shObjClass, so, l);
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObject so,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, l);
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObject so,
                Object implementor, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, implementor, l);
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, Class protocol,
                int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, protocol, role, l);
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, Class protocol,
                Object implementor, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, protocol, implementor, role, l);
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, Vector reqProtocols,
                Vector optProtocols, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(
      handle, bundle, key, cl, shObjClass, reqProtocols, optProtocols, role, l
    );
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, Vector reqProtocols,
                Vector optProtocols, Object implementor, int role,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(
      handle, bundle, key, cl, shObjClass, reqProtocols, optProtocols,
      implementor, role, l
    );
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObject so,
                Class protocol, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, protocol, role, l);
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObject so,
                Class protocol, Object implementor, int role,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(
      handle, bundle, key, cl, shObjClass, so, protocol, implementor, role, l
    );
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObject so,
                Vector reqProtocols, Vector optProtocols, int role,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(
      handle, bundle, key, cl, shObjClass, so, reqProtocols, optProtocols,
      role, l
    );
  }

  @SuppressWarnings("unchecked")
  protected MixedModePlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObject so,
                Vector reqProtocols, Vector optProtocols, Object implementor,
                int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(
      handle, bundle, key, cl, shObjClass, so, reqProtocols, optProtocols,
      implementor, role, l
    );
  }
}
