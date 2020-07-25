package gr.cti.eslate.base;

import gr.cti.eslate.base.sharedObject.*;
import java.awt.Color;
import java.util.*;
import java.lang.Class;

/**
 * Base class for plugs associated with a shared object.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public abstract class SharedObjectPlug extends Plug
{
  /**
   * Returns the class name of the shared object associated with the plug.
   * @return    The class name.
   */
  public String getSharedObjectsClassName()
  {
    return internals.getSharedObjectsClassName();
  }

  /**
   * Returns a reference to the shared object associated with the plug.
   * @return    The shared object.
   */
  public SharedObject getSharedObject()
  {
    return internals.getSharedObject();
  }

  /**
   * Return the listener for receiving notification events whenever the shared
   * object of the plug connected to this plug changes.
   * @return    The requested listener.
   */
  public SharedObjectListener getSharedObjectListener()
  {
    return internals.getSharedObjectListener();
  }

  // Inherit all the constructors of the Plug class, so that they can be
  // invoked by subclasses.

  @SuppressWarnings("unchecked")
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, l);
  }

  @SuppressWarnings("unchecked")
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, Object implementor,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, implementor, l);
  }

  @SuppressWarnings("unchecked")
  protected SharedObjectPlug(ESlateHandle handle, String name, Color cl,
                Class shObjClass, SharedObject so, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, name, cl, shObjClass, so, l);
  }

  @SuppressWarnings("unchecked")
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObject so,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, l);
  }

  @SuppressWarnings("unchecked")
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObject so,
                Object implementor, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, implementor, l);
  }

  @SuppressWarnings("unchecked")
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, Class protocol,
                int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, protocol, role, l);
  }

  @SuppressWarnings("unchecked")
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, Class protocol,
                Object implementor, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, protocol, implementor, role, l);
  }

  @SuppressWarnings("unchecked")
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, Vector reqProtocols,
                Vector optProtocols, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(
      handle, bundle, key, cl, shObjClass, reqProtocols, optProtocols, role, l
    );
  }

  @SuppressWarnings("unchecked")
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
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
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
                String key, Color cl, Class shObjClass, SharedObject so,
                Class protocol, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, protocol, role, l);
  }

  @SuppressWarnings("unchecked")
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
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
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
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
  protected SharedObjectPlug(ESlateHandle handle, ResourceBundle bundle,
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
