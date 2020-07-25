package gr.cti.eslate.base;

import gr.cti.eslate.base.sharedObject.*;
import java.awt.Color;
import java.util.*;

import javax.swing.*;

/**
 * Implements multiple input, multiple output mixed mode plugs.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public class MultipleInputMultipleOutputProtocolPlug extends MixedModePlug
{
  /**
   * Constructs a multiple input, multiple output mixed mode plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MultipleInputMultipleOutputProtocolPlug(ESlateHandle handle,
                                        ResourceBundle bundle, String key,
                                        Color cl, Class shObjClass,
                                        SharedObject so, Object implementor,
                                        SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, implementor, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
  }

  /**
   * Constructs a multiple input, multiple output mixed mode plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MultipleInputMultipleOutputProtocolPlug(ESlateHandle handle,
                                        ResourceBundle bundle, String key,
                                        Color cl, Class shObjClass,
                                        SharedObject so, Class protocol,
                                        SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, protocol, NO_ROLE, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
  }

  /**
   * Constructs a multiple input, multiple output mixed mode plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MultipleInputMultipleOutputProtocolPlug(ESlateHandle handle,
                                        ResourceBundle bundle, String key,
                                        Color cl, Class shObjClass,
                                        SharedObject so, Class protocol,
                                        Object implementor,
                                        SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, protocol, implementor,
          NO_ROLE, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
  }

  /**
   * Constructs a multiple input, multiple output mixed mode plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   * @param     reqProtocols    A list of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A list of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MultipleInputMultipleOutputProtocolPlug(ESlateHandle handle,
                                        ResourceBundle bundle, String key,
                                        Color cl, Class shObjClass,
                                        SharedObject so, Vector reqProtocols,
                                        Vector optProtocols,
                                        SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, reqProtocols, optProtocols,
          NO_ROLE, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
  }

  /**
   * Constructs a multiple input, multiple output mixed mode plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   * @param     reqProtocols    A list of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A list of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MultipleInputMultipleOutputProtocolPlug(ESlateHandle handle,
                                        ResourceBundle bundle, String key,
                                        Color cl, Class shObjClass,
                                        SharedObject so, Vector reqProtocols,
                                        Vector optProtocols,
                                        Object implementor,
                                        SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, reqProtocols, optProtocols,
          implementor, NO_ROLE, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
  }

  /**
   * Constructs a multiple input, multiple output mixed mode plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   * @param     exported        A vector of the Java interfaces that the
   *                            plug declares that the protocol implementor
   *                            implements. Setting this to null is equivalent
   *                            to declaring that the protocol implementor
   *                            implements all the interfaces that it actually
   *                            implements.
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MultipleInputMultipleOutputProtocolPlug(ESlateHandle handle,
                                        ResourceBundle bundle, String key,
                                        Color cl, Class shObjClass,
                                        SharedObject so, Object implementor,
                                        SharedObjectListener l,
                                        Vector exported)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, implementor, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
    setExportedInterfaces(exported);
  }

  /**
   * Constructs a multiple input, multiple output mixed mode plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   * @param     exported        A vector of the Java interfaces that the
   *                            plug declares that the protocol implementor
   *                            implements. Setting this to null is equivalent
   *                            to declaring that the protocol implementor
   *                            implements all the interfaces that it actually
   *                            implements.
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MultipleInputMultipleOutputProtocolPlug(ESlateHandle handle,
                                        ResourceBundle bundle, String key,
                                        Color cl, Class shObjClass,
                                        SharedObject so, Class protocol,
                                        SharedObjectListener l,
                                        Vector exported)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, protocol, NO_ROLE, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
    setExportedInterfaces(exported);
  }

  /**
   * Constructs a multiple input, multiple output mixed mode plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   * @param     exported        A vector of the Java interfaces that the
   *                            plug declares that the protocol implementor
   *                            implements. Setting this to null is equivalent
   *                            to declaring that the protocol implementor
   *                            implements all the interfaces that it actually
   *                            implements.
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MultipleInputMultipleOutputProtocolPlug(ESlateHandle handle,
                                        ResourceBundle bundle, String key,
                                        Color cl, Class shObjClass,
                                        SharedObject so, Class protocol,
                                        Object implementor,
                                        SharedObjectListener l,
                                        Vector exported)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, protocol, implementor,
          NO_ROLE, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
    setExportedInterfaces(exported);
  }

  /**
   * Constructs a multiple input, multiple output mixed mode plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   * @param     reqProtocols    A list of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A list of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   * @param     exported        A vector of the Java interfaces that the
   *                            plug declares that the protocol implementor
   *                            implements. Setting this to null is equivalent
   *                            to declaring that the protocol implementor
   *                            implements all the interfaces that it actually
   *                            implements.
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MultipleInputMultipleOutputProtocolPlug(ESlateHandle handle,
                                        ResourceBundle bundle, String key,
                                        Color cl, Class shObjClass,
                                        SharedObject so, Vector reqProtocols,
                                        Vector optProtocols,
                                        SharedObjectListener l,
                                        Vector exported)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, reqProtocols, optProtocols,
          NO_ROLE, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
    setExportedInterfaces(exported);
  }

  /**
   * Constructs a multiple input, multiple output mixed mode plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   * @param     reqProtocols    A list of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A list of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   * @param     exported        A vector of the Java interfaces that the
   *                            plug declares that the protocol implementor
   *                            implements. Setting this to null is equivalent
   *                            to declaring that the protocol implementor
   *                            implements all the interfaces that it actually
   *                            implements.
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MultipleInputMultipleOutputProtocolPlug(ESlateHandle handle,
                                        ResourceBundle bundle, String key,
                                        Color cl, Class shObjClass,
                                        SharedObject so, Vector reqProtocols,
                                        Vector optProtocols,
                                        Object implementor,
                                        SharedObjectListener l,
                                        Vector exported)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, reqProtocols, optProtocols,
          implementor, NO_ROLE, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
    setExportedInterfaces(exported);
  }

  /**
   * Returns the icon that represents the plug.
   * @return    The icon.
   */
  public ImageIcon getIcon()
  {
    int providers = internals.providerPlugs.size();
    int dependents = internals.dependentPlugs.size();

    if (providers > 0 && dependents > 0) {
      if (icon_io == null) {
        icon_io = createPlugIcon(Plug.class.getResource("mimo_io.gif"));
      }
      return icon_io;
    } else {
      if (providers > 0) {
        if (icon_i == null) {
          icon_i = createPlugIcon(Plug.class.getResource("mimo_i.gif"));
        }
        return icon_i;
      } else {
        if (dependents > 0) {
          if (icon_o == null) {
            icon_o = createPlugIcon(Plug.class.getResource("mimo_o.gif"));
          }
          return icon_o;
        } else {
          if (icon == null) {
            icon = createPlugIcon(Plug.class.getResource("mimo.gif"));
          }
          return icon;
        }
      }
    }
  }

  /**
   * Icon shown when plug is not connected.
   */
  private ImageIcon icon = null;

  /**
   * Icon shown when plug's input part is connected.
   */
  private ImageIcon icon_i = null;

  /**
   * Icon shown when plug's output part is connected.
   */
  private ImageIcon icon_o = null;

  /**
   * Icon shown when plug's input and output parts are both connected.
   */
  private ImageIcon icon_io = null;

}
