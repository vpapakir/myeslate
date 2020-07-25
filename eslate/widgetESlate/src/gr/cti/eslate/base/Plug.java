package gr.cti.eslate.base;

import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.utils.*;

import java.awt.*;
import java.awt.image.*;
import java.lang.Class;
import java.net.*;
import java.security.*;
import java.util.*;

import javax.swing.*;

/**
 * Implements the general mechanism of plugs.
 *
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
@SuppressWarnings("unchecked")
public abstract class Plug
{
  /**
   * Constructs a plug without an associated object (input plug).
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     name            The name of the plug.
   * @param     internalName    The locale independent name of the plug.
   *                            (Usually a resource bundle key.)
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
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
  private Plug(ESlateHandle handle, String name, String internalName,
                Color cl, Class shObjClass, Object implementor,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    visible = true;

    // is shObjClass not null?
    if (shObjClass == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(
          resources.getString("noSharedObject")
        );
      throw e;
    }
    // Is shObjClass a subclass of SharedObject?
    if (!(SharedObject.class).isAssignableFrom(shObjClass)) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(
          resources.getString("class") + " " + shObjClass + " " +
          resources.getString("notShobjSubClass"));
      throw e;
    }
    myHandle = handle;
    this.name = new String(name);
    this.internalName = new String(internalName);
    color = cl;
    internals = new PlugInternals(null, shObjClass, l, implementor, NO_ROLE);
    internals.setRequiredInterfaces(new Vector());
    internals.setOptionalInterfaces(new Vector());
    connectionListeners = new ArrayList<ConnectionListener>();
    disconnectionListeners = new ArrayList<DisconnectionListener>();

    plugTreeNode = new PlugTreeNode(this);
  }

  /**
   * Constructs a plug without an associated object (input plug).
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
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass,
      ((handle != null) ?  handle.getComponent() : null),
      l
    );
  }

  /**
   * Constructs a plug without an associated object (input plug).
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
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, Object implementor,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, implementor, l
    );
  }

  /**
   * Constructs a plug with an associated a shared object (output plug).
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     name            The name of the plug.
   * @param     internalName    The locale independent name of the plug.
   *                            (Usually a resource bundle key.)
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The sharable object shared by the plug.
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
  private Plug(ESlateHandle handle, String name, String internalName, Color cl,
                Class shObjClass, SharedObject so, Object implementor,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    visible = true;

    // is shObjClass not null?
    if (shObjClass == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(
          resources.getString("noSharedObject")
        );
      throw e;
    }
    // is SharedObject not null?
    if (shObjClass == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(
          resources.getString("noSharedObject")
        );
      throw e;
    }
    // Is shObjClass a subclass of SharedObject?
    if (!(SharedObject.class).isAssignableFrom(shObjClass)) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(
          resources.getString("class") + " " + shObjClass + " " +
          resources.getString("notShobjSubClass"));
      throw e;
    }
    myHandle = handle;
    this.name = new String(name);
    this.internalName = new String(internalName);
    color = cl;
    internals = new PlugInternals(so, shObjClass, l, implementor, NO_ROLE);
    internals.setRequiredInterfaces(new Vector());
    internals.setOptionalInterfaces(new Vector());
    connectionListeners = new ArrayList<ConnectionListener>();
    disconnectionListeners = new ArrayList<DisconnectionListener>();

    so.setPlug(this);
    plugTreeNode = new PlugTreeNode(this);
  }

  /**
   * Constructs a plug with an associated a shared object (output plug).
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     name            The name of the plug.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The sharable object shared by the plug.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, String name, Color cl, Class shObjClass,
              SharedObject so, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle, name, name, cl, shObjClass, so,
      ((handle != null) ?  handle.getComponent() : null),
      l
    );
  }

  /**
   * Constructs a plug with an associated a shared object (output plug).
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
   *                            plug.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The sharable object shared by the plug.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, SharedObject so,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, so,
      ((handle != null) ?  handle.getComponent() : null),
      l
    );
  }

  /**
   * Constructs a plug with an associated a shared object (output plug).
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
   * @param     so              The sharable object shared by the plug.
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
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, SharedObject so,
                Object implementor, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, so, implementor, l
    );
  }

  /**
   * Constructs a plug without an associated object (input plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     name            The name of the plug.
   * @param     internalName    The locale independent name of the plug.
   *                            (Usually a resource bundle key.)
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter would have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  private Plug(ESlateHandle handle, String name, String internalName,
                Color cl, Class shObjClass, Class protocol, Object implementor,
                int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(handle, name, internalName, cl, shObjClass,
         new InittedVector(protocol), new Vector(), implementor, role, l);
  }

  /**
   * Constructs a plug without an associated object (input plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
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
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, Class protocol, int role,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, protocol,
      ((handle != null) ?  handle.getComponent() : null),
      role, l
    );
  }

  /**
   * Constructs a plug without an associated object (input plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
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
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, Class protocol, Object
                implementor, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, protocol, implementor, role, l
    );
  }

  /**
   * Constructs a plug without an associated object (input plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     name            The name of the plug.
   * @param     internalName    The locale independent name of the plug.
   *                            (Usually a resource bundle key.)
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     reqProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  private Plug(ESlateHandle handle, String name, String internalName, Color cl,
                Class shObjClass, Vector reqProtocols, Vector optProtocols,
                Object implementor, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    visible = true;

    // This constructor is called from the constructors of
    // SingleConnectionProtocolPlug and MultipleConnectionProtocolPlug with
    // shObjClass==null. (Omitting the shObjClass argument would make the
    // constructor identical to the one for input plugs, so we can't
    // define a new constructor for the two subclasses above.
    // Therefore, the check for shObjClass == null should be done in the
    // subclasses of class Plug, where appropriate.

    //if (shObjClass == null) {
    //  InvalidPlugParametersException e =
    //    new InvalidPlugParametersException("No shared object class specified");
    //  throw e;
    //}
    // Is shObjClass a subclass of SharedObject?
    if ((shObjClass != null) &&
        !(SharedObject.class).isAssignableFrom(shObjClass)) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(
          resources.getString("class") + " " + shObjClass + " " +
          resources.getString("notShobjSubClass"));
      throw e;
    }

    // Provide empty vectors for null protocol vectors
    if (reqProtocols == null) {
      reqProtocols = new Vector();
    }
    if (optProtocols == null) {
      optProtocols = new Vector();
    }

    // Check that none of the protocols provided is null
    for (int i=0; i<reqProtocols.size(); i++) {
      if (reqProtocols.elementAt(i) == null) {
        InvalidPlugParametersException e =
          new InvalidPlugParametersException(
            resources.getString("nullProtocol"));
        throw e;
      }
    }
    for (int i=0; i<optProtocols.size(); i++) {
      if (optProtocols.elementAt(i) == null) {
        InvalidPlugParametersException e =
          new InvalidPlugParametersException(
            resources.getString("nullProtocol"));
        throw e;
      }
    }

/*
    // Are protocols Java interfaces?
    for (int i=0; i<reqProtocols.size(); i++) {
      Object t = reqProtocols.elementAt(i);
      if (!(t instanceof Class) || !((Class)t).isInterface()) {
        InvalidPlugParametersException e =
          new InvalidPlugParametersException(
            resources.getString("class") + " " + t + " " +
            resources.getString("notInterface"));
        throw e;
      }
    }
    for (int i=0; i<optProtocols.size(); i++) {
      Object t = optProtocols.elementAt(i);
      if (!(t instanceof Class) || !((Class)t).isInterface()) {
        InvalidPlugParametersException e =
          new InvalidPlugParametersException(
            resources.getString("class") + " " + t + " " +
            resources.getString("notInterface"));
        throw e;
      }
    }
*/

    // Does role have a valid value?
    boolean badRole = false;
    switch (role) {
      case NO_ROLE:
        // Pure protocol plugs must have a role
        if (shObjClass == null) {
          badRole = true;
        }
        break;
      case LEFT_ROLE:
      case RIGHT_ROLE:
        // Protocol+shared object plugs should not have a role
        if (shObjClass != null) {
          badRole = true;
        }
        break;
      default:
        // Illegal role value
        badRole = true;
    }
    if (badRole) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("badRole"));
      throw e;
    }
    internals = new PlugInternals(null, shObjClass, l, implementor, role);
    myHandle = handle;
    this.name = new String(name);
    this.internalName = new String(internalName);
    color = cl;
    internals.setRequiredInterfaces((Vector)(reqProtocols.clone()));
    internals.setOptionalInterfaces((Vector)(optProtocols.clone()));
    connectionListeners = new ArrayList<ConnectionListener>();
    disconnectionListeners = new ArrayList<DisconnectionListener>();

    plugTreeNode = new PlugTreeNode(this);
  }

  /**
   * Constructs a plug without an associated object (input plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
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
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     reqProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, Vector reqProtocols,
                Vector optProtocols, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, reqProtocols,
      optProtocols,
      ((handle != null) ?  handle.getComponent() : null),
      role, l
    );
  }

  /**
   * Constructs a plug without an associated object (input plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
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
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     reqProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, Vector reqProtocols,
                Vector optProtocols, Object implementor, int role,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, reqProtocols, optProtocols, implementor, role, l
    );
  }

  /**
   * Constructs a plug with an associated a shared object (output plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     name            The name of the plug.
   * @param     internalName    The locale independent name of the plug.
   *                            (Usually a resource bundle key.)
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     so              The sharable object shared by the plug.
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  private Plug(ESlateHandle handle, String name, String internalName, Color cl,
                Class shObjClass, SharedObject so, Class protocol,
                Object implementor, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle, name, internalName, cl, shObjClass, so,
      new InittedVector(protocol), new Vector(), implementor, role, l
    );
  }

  /**
   * Constructs a plug with an associated a shared object (output plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
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
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     so              The sharable object shared by the plug.
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, SharedObject so, Class protocol,
                int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, so, protocol,
      ((handle != null) ?  handle.getComponent() : null),
      role, l
    );
  }

  /**
   * Constructs a plug with an associated a shared object (output plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
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
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     so              The sharable object shared by the plug.
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, SharedObject so, Class protocol,
                Object implementor, int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, so, protocol, implementor, role, l
    );
  }

  /**
   * Constructs a plug with an associated a shared object (output plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     name            The name of the plug.
   * @param     internalName    The locale independent name of the plug.
   *                            (Usually a resource bundle key.)
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     so              The sharable object shared by the plug.
   * @param     reqProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  private Plug(ESlateHandle handle, String name, String internalName, Color cl,
                Class shObjClass, SharedObject so, Vector reqProtocols,
                Vector optProtocols, Object implementor, int role,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    visible = true;

    // is shObjClass not null?
    if (shObjClass == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noSharedObject"));
      throw e;
    }
    // is SharedObject not null?
    if (shObjClass == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noSharedObject"));
      throw e;
    }

    // Is shObjClass a subclass of SharedObject?
    if (!(SharedObject.class).isAssignableFrom(shObjClass)) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(
          resources.getString("class") + " " + shObjClass + " " +
          resources.getString("notShobjSubClass"));
      throw e;
    }

    // Provide empty vectors for null protocol vectors
    if (reqProtocols == null) {
      reqProtocols = new Vector();
    }
    if (optProtocols == null) {
      optProtocols = new Vector();
    }

    // Check that none of the protocols provided is null
    for (int i=0; i<reqProtocols.size(); i++) {
      if (reqProtocols.elementAt(i) == null) {
        InvalidPlugParametersException e =
          new InvalidPlugParametersException(
            resources.getString("nullProtocol"));
        throw e;
      }
    }

/*
    // Are protocols Java interfaces?
    for (int i=0; i<reqProtocols.size(); i++) {
      Object t = reqProtocols.elementAt(i);
      if (!(t instanceof Class) || !((Class)t).isInterface()) {
        InvalidPlugParametersException e =
          new InvalidPlugParametersException(
            resources.getString("class") + " " + t + " " +
            resources.getString("notInterface"));
        throw e;
      }
    }
    for (int i=0; i<optProtocols.size(); i++) {
      Object t = optProtocols.elementAt(i);
      if (!(t instanceof Class) || !((Class)t).isInterface()) {
        InvalidPlugParametersException e =
          new InvalidPlugParametersException(
            resources.getString("class") + " " + t + " " +
            resources.getString("notInterface"));
        throw e;
      }
    }
*/

    // Does role have a valid value?
    boolean badRole = false;
    switch (role) {
      case NO_ROLE:
        // Pure protocol plugs must have a role
        if (shObjClass == null) {
          badRole = true;
        }
        break;
      case LEFT_ROLE:
      case RIGHT_ROLE:
        // Protocol+shared object plugs should not have a role
        if (shObjClass != null) {
          badRole = true;
        }
        break;
      default:
        // Illegal role value
        badRole = true;
    }
    if (badRole) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("badRole"));
      throw e;
    }
    internals = new PlugInternals(so, shObjClass, l, implementor, role);
    myHandle = handle;
    this.name = new String(name);
    this.internalName = new String(internalName);
    color = cl;
    internals.setRequiredInterfaces((Vector)(reqProtocols.clone()));
    internals.setOptionalInterfaces((Vector)(optProtocols.clone()));
    connectionListeners = new ArrayList<ConnectionListener>();
    disconnectionListeners = new ArrayList<DisconnectionListener>();

    so.setPlug(this);
    plugTreeNode = new PlugTreeNode(this);
  }

  /**
   * Constructs a plug with an associated a shared object (output plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
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
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     so              The sharable object shared by the plug.
   * @param     reqProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, SharedObject so,
                Vector reqProtocols, Vector optProtocols, int role,
                SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, so, reqProtocols, optProtocols,
      ((handle != null) ?  handle.getComponent() : null),
      role, l
    );
  }

  /**
   * Constructs a plug with an associated a shared object (output plug),
   * specifying that the objects connecting through this plug must
   * implement a specific Java interface.
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
   *                            by the plug. Specify null if communication is
   *                            to be done only via the specified interface.
   * @param     so              The sharable object shared by the plug.
   * @param     reqProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A vector of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   * @param     implementor     The object implementing the interfaces
   *                            that are part of this plug's communication
   *                            protocol. This object can be a reference to
   *                            the component owning the plug, or to another
   *                            object to which the component has delegated
   *                            the implementation of the protocol.
   * @param     role            If this plug is a pure protocol plug, this
   *                            parameter whoulc have one of the values
   *                            <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
   *                            indicating that the plug is playin the "left"
   *                            or "right" side role of the protocol,
   *                            respectively. Otherwise, this parameter should
   *                            have the value <CODE>NO_ROLE</CODE>.
   *                            The role is used, like color, to provide the
   *                            user with a visual hint as to what plugs can be
   *                            connected together.
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  protected Plug(ESlateHandle handle, ResourceBundle bundle, String key,
                Color cl, Class shObjClass, SharedObject so,
                Vector reqProtocols, Vector optProtocols, Object implementor,
                int role, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    this(
      handle,
      ((bundle != null) ? bundle.getString(checkKey(key)) : checkKey(key)),
      key, cl, shObjClass, so, reqProtocols, optProtocols, implementor, role, l
    );
  }

  /**
   * Checks that a resource bundle key is not null.
   * @param     key     The resource bundle key to check.
   * @return    The <code>key</code> argument.
   * @exception InvalidPlugParametersException  Thrown if the <code>key</code>
   *                    argument is <code>null</code>.
   */
  private static String checkKey(String key)
    throws InvalidPlugParametersException
  {
    if (key == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("nullKey"));
      throw e;
    }
    return key;
  }

  /**
   * Specifies whether a plug is visible or not. This can be used to
   * "hide" plugs that should not be shown to the user, but must not be removed
   * from the component. (E.g., only show the plug associated with the
   * currently active document for a multiple-document editor.)
   * @param     isVisible       Specifies whether the plug should be visible or not.
   */
  public void setVisible(boolean isVisible)
  {
    visible = isVisible;
    myHandle.redoPlugView = true;
    myHandle.redoPlugMenu = true;
    myHandle.reloadModels();
  }

  /**
   * Checks whether a plug is visible or not.
   * @return    True if yes, otherwise false.
   */
  public boolean isVisible()
  {
    return visible;
  }

  /**
   * Returns the name of the plug.
   * @return    The plug's name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the locale independent name of the plug. (Usually a resource
   * bundle key.)
   * @return    The requested name.
   */
  public String getInternalName()
  {
    return internalName;
  }

  /**
   * String representation of the plug.
   * @return    The plug's name.
   */
  public String toString()
  {
    return getName();
  }

  /**
   * Sets the name of the plug.
   * @param     name    The new name of the plug.
   * @exception PlugExistsException     This exception is thrown when trying
   *                                    to rename the plug using a name that
   *                                    is used by another plug already
   *                                    attached to the component at the same
   *                                    level as this plug.
   */
  public void setName(String name) throws PlugExistsException
  {
    if (name == null) {
      name = "";
    }
    if (name.equals(this.name)) {
      return;
    }
    Plug parentPlug = getParentPlug();
    if (parentPlug == null) {  // Top level plug
      if (myHandle.getPlug(name) != null) {
        PlugExistsException pee =
          new PlugExistsException(
            resources.getString("aTopPlugNamed") + " " + name + " " +
            resources.getString("alreadyAttached")
          );
        throw pee;
      }
    } else {  // sub-plug
      if (parentPlug.getPlug(name) != null) {
        PlugExistsException pee =
          new PlugExistsException(
            resources.getString("aPlugNamed") + " " + name + " " +
            resources.getString("alreadyAttachedToPlug")
          );
        throw pee;
      }
    }
    this.name = new String(name);
    // Invalidate the plug view and plug menu of this plug's component and
    // all its parent components, so that whichever is displayed will show the
    // plugs of this plug's component's correctly. Also reload the tree
    // models of this plug's component and all its parent components,
    // so that whichever is displayed on the plug editor is updated.
    for (ESlateHandle h=myHandle; h != null; h = h.getParentHandle()) {
      h.redoPlugView = true;
      h.redoPlugMenu = true;
      h.reloadModels();
    }
    myHandle.makeVisible(getPlugTreeNode());
  }

  /**
   * Sets the locale-independent name of the plug. Use this method in cases
   * where you cannot use one of the constructors that takes a resource bundle
   * and a resource bundle key as arguments.
   * @param     name    The new locale-independent name of the plug.
   * @exception PlugExistsException     This exception is thrown when trying
   *                                    to rename the plug using a
   *                                    locale-independent name that
   *                                    is used by another plug already
   *                                    attached to the component at the same
   *                                    level as this plug.
   */
  public void setNameLocaleIndependent(String name) throws PlugExistsException
  {
    if (name == null) {
      name = "";
    }
    if (name.equals(internalName)) {
      return;
    }
    Plug parentPlug = getParentPlug();
    if (parentPlug == null) {  // Top level plug
      if (myHandle.getPlugLocaleIndependent(name) != null) {
        PlugExistsException pee =
          new PlugExistsException(
            resources.getString("aTopPlugInternallyNamed") + " " + name + " " +
            resources.getString("alreadyAttached")
          );
        throw pee;
      }
    } else {  // sub-plug
      if (parentPlug.getPlugLocaleIndependent(name) != null) {
        PlugExistsException pee =
          new PlugExistsException(
            resources.getString("aPlugInternallyNamed") + " " + name + " " +
            resources.getString("alreadyAttachedToPlug")
          );
        throw pee;
      }
    }
    internalName = new String(name);
  }


  /**
   * Returns the color of the plug.
   * @return    The plug's color.
   */
  public Color getColor()
  {
    return color;
  }

  // Providers handling
  /**
   * Returns a reference to the provider plug that is connected to this plug
   * and has a given name. In the case of shared object or mixed mode plugs,
   * a provider plug is a plug that provides, i.e., is associated with a
   * shared object. In the case of protocol plugs, a provider plug is simply
   * the first plug in a pair of connected plugs.
   * @param     providerName    The name of the requested plug.
   * @return    The requested plug.
   */
  public Plug getProvider(String providerName)
  {
    return internals.getProvider(providerName);
  }

  /**
   * Returns a list of all provider plugs connected to this plug.
   * In the case of shared object or mixed mode plugs,
   * a provider plug is a plug that provides, i.e., is associated with a
   * shared object. In the case of protocol plugs, a provider plug is simply
   * the first plug in a pair of connected plugs.
   * @return    An array containing references to the provider plugs.
   *            If the plug has no providers, an empty array is returned.
   */
  public Plug[] getProviders()
  {
    return internals.getProviders();
  }

  /**
   * Tests whether the plug has any provider plugs connected to it.
   * @return    True if yes, false if not.
   */
  public boolean hasProvidersConnected()
  {
    return internals.hasProvidersConnected();
  }

  /**
   * Adds a plug to the list of provider plugs connected to this plug.
   * @param     providerPlug    The plug to add.
   */
  public void addProvider(Plug providerPlug)
  {
    internals.addProvider(providerPlug);
  }

  /**
   * Removes a plug from the list of provider plugs connected to this plug.
   * @param     providerPlug    The plug to remove.
   */
  public void removeProvider(Plug providerPlug)
  {
    internals.removeProvider(providerPlug);
  }

  /**
   * Checks whether a given plug is a provider plug connected to this plug.
   * @param     providerPlug    The plug to check.
   * @return    True if yes, false otherwise.
   */
  public boolean containsProvider(Plug providerPlug)
  {
    return internals.containsProvider(providerPlug);
  }

  // Dependents handling
  /**
   * Returns a reference to the dependent plug that is connected to this plug
   * and has a given name.
   * In the case of shared object or mixed mode plugs,
   * a dependent plug is a plug that is not associated with a
   * shared object. In the case of protocol plugs, a provider plug is simply
   * the second plug in a pair of connected plugs.
   * @param     dependentName   The name of the requested plug.
   * @return    The requested plug.
   */
  public Plug getDependent(String dependentName)
  {
    return internals.getDependent(dependentName);
  }

  /**
   * Returns a list of all dependent plugs connected to this plug.
   * In the case of shared object or mixed mode plugs,
   * a dependent plug is a plug that is not associated with a
   * shared object. In the case of protocol plugs, a provider plug is simply
   * the second plug in a pair of connected plugs.
   * @return    An array containing references to the dependent plugs.
   *            If the plug has no dependents, an empty array is returned.
   */
  public Plug[] getDependents()
  {
    if (internals != null) {
      return internals.getDependents();
    }else{
      return new Plug[0];
    }
  }

  /**
   * Tests whether the plug has any dependent plugs connected to it.
   * @return    True if yes, false if not.
   */
  public boolean hasDependentsConnected()
  {
    return internals.hasDependentsConnected();
  }

  /**
   * Adds a plug to the list of dependent plugs connected to this plug.
   * @param     dependentPlug   The plug to add.
   */
  public void addDependent(Plug dependentPlug)
  {
    internals.addDependent(dependentPlug);
  }

  /**
   * Removes a plug from the list of dependent plugs connected to this plug.
   * @param     dependentPlug   The plug to remove.
   */
  public void removeDependent(Plug dependentPlug)
  {
    internals.removeDependent(dependentPlug);
  }

  /**
   * Checks whether a given plug is a dependent plug connected to this plug.
   * @param     dependentPlug   The plug to check.
   * @return    True if yes, false otherwise.
   */
  public boolean containsDependent(Plug dependentPlug)
  {
    return internals.containsDependent(dependentPlug);
  }

  /**
   * Associates the plug with a plug tree node.
   * @param     node    The plug tree node. This method is called from the
   *                    constructor of the plug tree node, so that plugs and
   *                    associated nodes contain a reference to each other.
   *                    Plugs are associated with a plug tree node at
   *                    construction time, so there is no need to invoke
   *                    this method for ordinary use.
   */
  void setPlugTreeNode(PlugTreeNode node)
  {
    plugTreeNode = node;
  }

  /**
   * Returns the plug tree node associated with the plug.
   */
  public PlugTreeNode getPlugTreeNode()
  {
    return plugTreeNode;
  }

  /**
   * Returns the handle owned by the component to which the plug belongs.
   * @return    The component.
   */
  public ESlateHandle getHandle()
  {
    return myHandle;
  }

  // Handling of connection/disconnection events

  /**
   * Add a listener to be fired when the plug is connected to another plug.
   * @param     l       The listener to add.
   */
  public void addConnectionListener(ConnectionListener l)
  {
    synchronized (connectionListeners) {
      if (!connectionListeners.contains(l)){
        connectionListeners.add(l);
      }
    }
  }

  /**
   * Remove a listener from the plug's connection listener list.
   * @param     l       The listener to remove.
   */
  public void removeConnectionListener(ConnectionListener l)
  {
    synchronized (connectionListeners) {
      int i = connectionListeners.indexOf(l);
      if (i >= 0) {
        connectionListeners.remove(i);
      }
    }
  }

  /**
   * Add a listener to be fired when the plug is disconnected from another plug.
   * @param     l       The listener to add.
   */
  public void addDisconnectionListener(DisconnectionListener l)
  {
    synchronized (disconnectionListeners) {
      if (!disconnectionListeners.contains(l)){
        disconnectionListeners.add(l);
      }
    }
  }

  /**
   * Remove a listener from the plug's disconnection listener list.
   * @param     l       The listener to remove.
   */
  public void removeDisconnectionListener(DisconnectionListener l)
  {
    synchronized (disconnectionListeners) {
      int i = connectionListeners.indexOf(l);
      if (i >= 0) {
        disconnectionListeners.remove(i);
      }
    }
  }

  /**
   * Fire the listeners listening for a connection event.
   * @param     p       The plug that has just been connected to this plug.
   * @param     type    Indicates to which part (input, output, or pure
   *                    protocol) of this plug the above plug was connected.
   *                    Its value is one of INPUT_CONNECTION,
   *                    OUTPUT_CONNECTION, and PROTOCOL_CONNECTION.
   * @param     optInt  A list of the optional interfaces specified at the
   *                    construction of this plug that the component to which
   *                    plug p belongs actually implements.
   */
  void fireConnectionListeners(Plug p, int type, Vector optInt)
  {
    ConnectionEvent ce = new ConnectionEvent(this, p, type, optInt);

    ArrayList<ConnectionListener> listeners;
    synchronized (connectionListeners) {
      listeners = (ArrayList<ConnectionListener>)(connectionListeners.clone());
    }

    for (int i=0; i<listeners.size(); i++) {
      listeners.get(i).handleConnectionEvent(ce);
    }
  }

  /**
   * Fire the listeners listening for a disconnection event.
   * @param     p       The plug that has just been disconnected from this plug.
   * @param     type    Indicates from which part (input, output, or pure
   *                    protocol) of this plug the above plug was disconnected.
   *                    Its value is one of INPUT_CONNECTION,
   *                    OUTPUT_CONNECTION, and PROTOCOL_CONNECTION.
   */
  void fireDisconnectionListeners(Plug p, int type)
  {
    DisconnectionEvent ce = new DisconnectionEvent(this, p, type);

    ArrayList<DisconnectionListener> listeners;
    synchronized (disconnectionListeners) {
      listeners =
        (ArrayList<DisconnectionListener>)disconnectionListeners.clone();
    }

    for (int i=0; i<listeners.size(); i++) {
      listeners.get(i).handleDisconnectionEvent(ce);
    }
  }

  // Plug type tests

  /**
   * Checks whether a plug allows only a single input connection.
   * @return    True if yes, false otherwise.
   */
  public boolean isSingleInputPlug()
  {
    if (this instanceof SingleInputMultipleOutputPlug ||
        this instanceof SingleInputSingleOutputPlug ||
        this instanceof SingleInputPlug ||
        this instanceof SingleInputMultipleOutputProtocolPlug ||
        this instanceof SingleInputSingleOutputProtocolPlug ||
        this instanceof SingleInputProtocolPlug) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Checks whether a plug allows only a single output connection.
   * @return    True if yes, false otherwise.
   */
  public boolean isSingleOutputPlug()
  {
    if (this instanceof MultipleInputSingleOutputPlug ||
        this instanceof SingleInputSingleOutputPlug ||
        this instanceof SingleOutputPlug ||
        this instanceof MultipleInputSingleOutputProtocolPlug ||
        this instanceof SingleInputSingleOutputProtocolPlug ||
        this instanceof SingleOutputProtocolPlug) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Checks whether a plug allows multiple input connections.
   * @return    True if yes, false otherwise.
   */
  public boolean isMultipleInputPlug()
  {
    if (this instanceof MultipleInputMultipleOutputPlug ||
        this instanceof MultipleInputPlug ||
        this instanceof MultipleInputSingleOutputPlug ||
        this instanceof MultipleInputMultipleOutputProtocolPlug ||
        this instanceof MultipleInputProtocolPlug ||
        this instanceof MultipleInputSingleOutputProtocolPlug) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Checks whether a plug allows multiple output connections.
   * @return    True if yes, false otherwise.
   */
  public boolean isMultipleOutputPlug()
  {
    if (this instanceof MultipleInputMultipleOutputPlug ||
        this instanceof MultipleOutputPlug ||
        this instanceof SingleInputMultipleOutputPlug ||
        this instanceof MultipleInputMultipleOutputProtocolPlug ||
        this instanceof MultipleOutputProtocolPlug ||
        this instanceof SingleInputMultipleOutputProtocolPlug) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Checks whether a plug allows one or more input connections.
   * @return    True if yes, false otherwise.
   */
  public boolean isInputPlug()
  {
    return this.isSingleInputPlug() || this.isMultipleInputPlug();
  }

  /**
   * Checks whether a plug allows one or more output connections.
   * @return    True if yes, false otherwise.
   */
  public boolean isOutputPlug()
  {
    return this.isSingleOutputPlug() || this.isMultipleOutputPlug();
  }

  /**
   * Checks whether a plug allows only one connection, and that this connection
   * is done via the protocol mechanism.
   * @return    True if yes, false otherwise.
   */
  public boolean isSingleConnectionProtocolPlug()
  {
    if (this instanceof LeftSingleConnectionProtocolPlug ||
        this instanceof RightSingleConnectionProtocolPlug ||
        this instanceof MaleSingleIFSingleConnectionProtocolPlug ||
        this instanceof FemaleSingleIFSingleConnectionProtocolPlug) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Checks whether a plug allows multiple connection, all of which
   * are done via the protocol mechanism.
   * @return    True if yes, false otherwise.
   */
  public boolean isMultipleConnectionProtocolPlug()
  {
    if (this instanceof LeftMultipleConnectionProtocolPlug ||
        this instanceof RightMultipleConnectionProtocolPlug ||
        this instanceof MaleSingleIFMultipleConnectionProtocolPlug ||
        this instanceof FemaleSingleIFMultipleConnectionProtocolPlug) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Checks whether a plug allows connections <EM>only</EM>
   * via the protocol mechanism.
   * @return    True if yes, false otherwise.
   */
  public boolean isOnlyProtocolPlug()
  {
    return (this instanceof ProtocolPlug);
  }

  /**
   * Checks whether a plug allows connections via the protocol mechanism.
   * @return    True if yes, false otherwise.
   */
  public boolean isProtocolPlug()
  {
    if (this instanceof IProtocolPlug) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Checks whether a plug allows connections via the protocol mechanism
   * with only one side requiring that the other side implements a set of
   * interfaces.
   * @return    True if yes, false otherwise.
   */
  public boolean isSingleIFProtocolPlug()
  {
    if (this instanceof ISingleIFProtocolPlug) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Checks whether two plugs can be connected to each other.
   * This method assumes that the first plug will be the provider plug
   * and the second plug will be the dependent plug.
   * @param     p1      The provider plug.
   * @param     p2      The dependent plug.
   * @return    <UL>
   * <LI><code>INCOMPATIBLE</code>: The plugs are incompatible.</LI>
   * <LI><code>COMPATIBLE</code>: The plugs are compatible and can be
   * connected.</LI>
   * <LI><code>ALREADY_CONNECTED</code>: The plugs are compatible, but one or
   * both of them is a single input or single output plug, and is already
   * connected to another plug.</LI>
   * <LI><code>SAME_PLUG</code>: The two plugs are really the same plug, and
   * cannot be connected.</LI>
   * <LI><code>NO_HANDLE</code>: One of the plugs does not have an E-Slate
   * handle.</LI>
   * <LI><code>DIFFERENT_MICROWORLDS</code>: Plugs belong to different
   * microworlds.</LI>
   * </UL>
   */
  public static int plugCompatibility(Plug p1, Plug p2)
  {
    boolean haveProtocol = false;

    // If any of the two plugs is null, they cannot be connected, so they are
    // incompatible.
    if (p1 == null || p2 == null) {
      return INCOMPATIBLE;
    }

    // Check whether the two plugs are identical. If so, the plugs cannot be
    // connected.
    if (p1.equals(p2)) {
      return SAME_PLUG;
    }

    // Check whether the plugs belong to E-Slate handles. If not, they cannot
    // be connected.
    ESlateHandle h1 = p1.getHandle();
    ESlateHandle h2 = p2.getHandle();
    if ((h1 == null) || (h2 == null)) {
      return NO_HANDLE;
    }

    // Check whether the plugs belong to different microworlds. If so, they
    // cannot be connected.
    ESlateMicroworld mw1 = h1.getESlateMicroworld();
    ESlateMicroworld mw2 = h2.getESlateMicroworld();
    boolean possibleHosting = false;
    if (((mw1 == null) && (mw2 != null)) || ((mw2 == null) && (mw1 != null))) {
      if (p1.internals.isHostingPlug() && p2.internals.isHostingPlug()) {
        possibleHosting = true;
      }
    }

    if (!possibleHosting) {
      if ((mw1 != null) && !mw1.equals(mw2)) {
        return DIFFERENT_MICROWORLDS;
      }
      if ((mw2 != null) && !mw2.equals(mw1)) {
        return DIFFERENT_MICROWORLDS;
      }
      if (((mw1 == null) && (mw2 != null)) ||
          ((mw2 == null) && (mw1 != null))) {
        return DIFFERENT_MICROWORLDS;
      }
    }

    // Check whether the two plugs have the same color. If not, the plugs
    // cannot be connected.
    if (!p1.color.equals(p2.color)) {
      return INCOMPATIBLE;
    }

    // Check whether the protocol implementor of the component to which the
    // second plug belongs implements the required Java interfaces specified
    // by the first plug. If not, the plugs cannot be connected.
    Vector exported2 = p2.internals.getExportedInterfaces();
    Vector required1 = p1.internals.requiredInterfaces;
    int nRequired1 = required1.size();
    int nExported2 = exported2.size();
    for (int i=0; i<nRequired1; i++) {
      Class<?> reqClass = (Class)(required1.elementAt(i));
      boolean ok = false;
      for (int j=0; j<nExported2; j++) {
        Class<?> expClass = (Class)(exported2.elementAt(j));
        if (reqClass.isAssignableFrom(expClass)) {
          ok = true;
          break;
        }
      }
      if (!ok) {
        return INCOMPATIBLE;
      }
    }

    // Check whether the protocol implementor of the component to which the
    // first plug belongs implements the required Java interfaces specified by
    // the second plug. If not, the plugs cannot be connected.
    Vector exported1 = p1.internals.getExportedInterfaces();
    Vector required2 = p2.internals.requiredInterfaces;
    int nRequired2 = required2.size();
    int nExported1 = exported1.size();
    for (int i=0; i<nRequired2; i++) {
      Class<?> reqClass = (Class)(required2.elementAt(i));
      boolean ok = false;
      for (int j=0; j<nExported1; j++) {
        Class<?> expClass = (Class)(exported1.elementAt(j));
        if (reqClass.isAssignableFrom(expClass)) {
          ok = true;
          break;
        }
      }
      if (!ok) {
        return INCOMPATIBLE;
      }
    }

    // Check whether only one of the two plugs is set up to communicate via
    // a shared object. If so, then the two plugs cannot be connected.
    if ((p1.internals.sharedObjectsClass == null &&
         p2.internals.sharedObjectsClass != null) ||
        (p2.internals.sharedObjectsClass == null &&
         p1.internals.sharedObjectsClass != null)) {
      return INCOMPATIBLE;
    }

    // Check that both or neither of the two plugs are single interface
    // protocol plugs.
    //boolean if1 = p1.isSingleIFProtocolPlug();
    boolean if2 = p2.isSingleIFProtocolPlug();
    if (if2 != if2) {
      return INCOMPATIBLE;
    }

    // Is at least one of the two communicating components required to
    // implement a specific Java interface?
    if (p1.internals.requiredInterfaces.size() > 0 ||
        p2.internals.requiredInterfaces.size() > 0) {
      haveProtocol = true;
    }

    // Check if communication is to be done only via Java interfaces.
    // At this point, we are sure that both sides implement what the other
    // side expects.
    if (p1.isOnlyProtocolPlug() && p2.isOnlyProtocolPlug()) {
      // If any of the two plugs can accept only one connection, and that
      // plug is already connected to some other plug, then the two plugs
      // cannot be connected.
      synchronized (p1.internals.protocolPlugs) {
        synchronized (p2.internals.protocolPlugs) {
          if ((p1.isSingleConnectionProtocolPlug() &&
               p1.internals.protocolPlugs.size() > 0) ||
              (p2.isSingleConnectionProtocolPlug() &&
               p2.internals.protocolPlugs.size() > 0)) {
            return ALREADY_CONNECTED;
          }else{
            // Check if the two plugs play matching roles.
            if ((p1.internals.plugRole == LEFT_ROLE &&
                 p2.internals.plugRole == RIGHT_ROLE) ||
                (p2.internals.plugRole == LEFT_ROLE &&
                 p1.internals.plugRole == RIGHT_ROLE)) {
              return COMPATIBLE;
            } else {
              return INCOMPATIBLE;
            }
          }
        }
      }
    }
    if ((p1.internals.sharedObjectsClass == null &&
         p2.internals.sharedObjectsClass == null) &&
        haveProtocol) {
      return COMPATIBLE;
    }

    // At this point we know that we are communicating via shared objects,
    // either exclusively or in addition to using Java interfaces.
    // As far as the latter are concerned, we know that things are OK.

    // Check whether the shared objects read and written by the two plugs are
    // of the same type.
    if (!p1.internals.sharedObjectsClass.equals(
          p2.internals.sharedObjectsClass)) {
      return INCOMPATIBLE;
    }

    // We can always connect a multiple output to a multiple input plug
    if (p1.isMultipleOutputPlug() && p2.isMultipleInputPlug()) {
      return COMPATIBLE;
    }
    // A single output plug can be connected to a multiple input plug if the
    // former is not connected anywhere else
    if (p1.isSingleOutputPlug() && p2.isMultipleInputPlug()) {
      synchronized (p1.internals.dependentPlugs) {
        if (p1.internals.dependentPlugs.size() < 1) {
          return COMPATIBLE;
        }else{
          return ALREADY_CONNECTED;
        }
      }
    }
    // A multiple output plug can be connected to a single input plug if the
    // latter is not connected anywhere else
    if (p1.isMultipleOutputPlug() && p2.isSingleInputPlug()) {
      synchronized (p2.internals.providerPlugs) {
        if (p2.internals.providerPlugs.size() < 1) {
          return COMPATIBLE;
        }else{
          return ALREADY_CONNECTED;
        }
      }
    }
    // A single output plug can be connected to a single input plug if none
    // of them is connected anywhere else
    if (p1.isSingleOutputPlug() && p2.isSingleInputPlug()) {
      synchronized (p1.internals.dependentPlugs) {
        synchronized (p2.internals.providerPlugs) {
          if ((p1.internals.dependentPlugs.size() < 1) &&
              (p2.internals.providerPlugs.size() < 1)) {
            return COMPATIBLE;
          }else{
            return ALREADY_CONNECTED;
          }
        }
      }
    }
    // All other types are incompatible
    return INCOMPATIBLE;
  }

  /**
   * Returns a reference to a given child plug.
   * @param     plugName        The name of the child plug.
   * @return    A reference to the requested plug.
   */
  public Plug getPlug(String plugName)
  {
    synchronized (plugTreeNode) {
      Vector childPlugs = plugTreeNode.getChildPlugs();
      for (int i=0; i<childPlugs.size(); i++) {
        Plug p = (Plug)(childPlugs.elementAt(i));
        if (plugName != null && plugName.equals(p.getName())) {
          return p;
        }
      }
    }
    return null;
  }

  /**
   * Returns a reference to a given child plug.
   * @param     internalPlugName        The locale independent name of the
   *                                    child plug. (Usually a resource bundle
   *                                    key.)
   * @return    A reference to the requested plug.
   */
  public Plug getPlugLocaleIndependent(String internalPlugName)
  {
    synchronized (plugTreeNode) {
      Vector childPlugs = plugTreeNode.getChildPlugs();
      for (int i=0; i<childPlugs.size(); i++) {
        Plug p = (Plug)(childPlugs.elementAt(i));
        if (internalPlugName != null &&
            internalPlugName.equals(p.getInternalName())) {
          return p;
        }
      }
    }
    return null;
  }

  /**
   * Add a child plug to the plug.
   * @param     plug    The plug to add.
   * @exception PlugExistsException     This exception is thrown when trying
   *                    to add to a component a plug that has the same name as
   *                    another plug already attached to the component at the
   *                    same level as this plug.
   * @exception IllegalArgumentException        if <code>plug</code> is null
   * @exception IllegalStateException   if plug tree node associated with
   *                                    <code>plug</code> does not allow
   *                                    children.
   */
  public void addPlug(Plug plug)
    throws PlugExistsException, IllegalArgumentException, IllegalStateException
  {
    if (plug == null) {
      IllegalArgumentException iae =
        new IllegalArgumentException(resources.getString("nullPlug"));
      throw iae;
    } else {
      if (getPlug(plug.getName()) != null) {
        PlugExistsException e =
          new PlugExistsException(
            resources.getString("aPlugNamed") + " " + plug.getName() +
            " " + resources.getString("alreadyAttachedToPlug"));
        throw e;
      } else {
        if (getPlugLocaleIndependent(plug.getInternalName()) != null) {
          PlugExistsException e =
            new PlugExistsException(
              resources.getString("aPlugInternallyNamed") + " " +
              plug.getInternalName() + " " +
              resources.getString("alreadyAttachedToPlug"));
          throw e;
        } else {
          synchronized (plugTreeNode) {
            plugTreeNode.add(plug.getPlugTreeNode());
          }
          // Invalidate the plug view and plug menu of this plug's component
          // and all its parent components, so that whichever is displayed
          // will show the plugs of this plug's component's correctly. Also
          // reload the tree models of this plug's component and all its
          // parent components, so that whichever is displayed on the plug
          // editor is updated.
          for (ESlateHandle h=myHandle; h != null; h = h.getParentHandle()) {
            h.redoPlugView = true;
            h.redoPlugMenu = true;
            h.reloadModels();
            h.makeVisible(plug.getPlugTreeNode());
          }
        }
      }
    }
  }

  /**
   * Remove a child plug from the plug.
   * @param     plug    The plug to remove.
   * @exception NoSuchPlugException     This exception is thrown when trying
   *                    to remove from a component a plug that it does not
   *                    actually have.
   * @exception IllegalArgumentException        if <code>plug</code> is null
   */
  public void removePlug(Plug plug)
    throws NoSuchPlugException, IllegalArgumentException
  {
    if (plug == null) {
      IllegalArgumentException iae =
        new IllegalArgumentException(resources.getString("nullPlug"));
      throw iae;
    } else {
      boolean plugExists;
      synchronized (plugTreeNode) {
        plugExists = plugTreeNode.isNodeChild(plug.getPlugTreeNode());
      }
      if (!plugExists) {
        NoSuchPlugException nspe =
          new NoSuchPlugException(
            resources.getString("noSubPlug") + " " + plug.getName());
        throw nspe;
      } else {
        plug.disconnect();
        synchronized (plugTreeNode) {
          plugTreeNode.remove(plug.getPlugTreeNode());
        }
        // Invalidate the plug view and plug menu of this plug's component
        // and all its parent components, so that whichever is displayed
        // will show the plugs of this plug's component's correctly. Also
        // reload the tree models of this plug's component and all its parent
        // components, so that whichever is displayed on the plug editor is
        // updated.
        for (ESlateHandle h=myHandle; h != null; h = h.getParentHandle()) {
          h.redoPlugView = true;
          h.redoPlugView = true;
          h.reloadModels();
          h.makeVisible(plug.getPlugTreeNode());
        }
      }
    }
  }

  /**
   * Returns a list of the plug's child plugs.
   * @return    The requested plugs.
   */
  public Plug[] getChildPlugs()
  {
    Vector v = plugTreeNode.getChildPlugs();
    int size = v.size();
    Plug p[] = new Plug[size];
    for (int i=0; i<size; i++) {
      p[i] = (Plug)(v.elementAt(i));
    }
    return p;
  }

  /**
   * Returns a plug's parent plug.
   * @return    The requested plug or null if the plug has no parent.
   */
  public Plug getParentPlug()
  {
    return plugTreeNode.getParentPlug();
  }

  /**
   * Recursively disconnect the plug from any plugs to which it is connected.
   */
  public void disconnect()
  {
    // Disable audio feedback.
    ESlateMicroworld.suppressAudio = true;

    // Ignore any plug the user may have selected as a first plug before
    // disconnecting.
    ESlateMicroworld.resetSelectedPlug(); 

    // Disconnect all sub-plugs.
    Vector childPlugs = plugTreeNode.getChildPlugs();
    for (int i=0; i<childPlugs.size(); i++) {
      Plug childPlug = (Plug)(childPlugs.elementAt(i));
      childPlug.disconnect();
    }

    // Disconnect all provider plugs.
    Plug[] providers = getProviders();
    for (int i = 0; i < providers.length; i++) {
      Plug providerPlug = providers[i];
      ESlateMicroworld.connectComponent(providerPlug);
      ESlateMicroworld.connectComponent(this);
    }
    
    // Disconnect all dependent plugs.
    Plug[] dependents = getDependents();
    for (int i = 0; i < dependents.length; i++) {
      Plug dependentPlug = dependents[i];
      ESlateMicroworld.connectComponent(this);
      ESlateMicroworld.connectComponent(dependentPlug);
    }

    // Re-enable audio feedback.
    ESlateMicroworld.suppressAudio = false;
  }

  /**
   * Disconnect the plug from a given plug.
   * @param     plug    The plug to disconnect.
   * @exception PlugNotConnectedException       If the current plug had not
   *                    been connected to the given plug or if this plug's
   *                    microworld is null.
   */
  public synchronized void disconnectPlug(Plug plug)
    throws PlugNotConnectedException
  {
    // Disable audio feedback.
    ESlateMicroworld.suppressAudio = true;

    // Ignore any plug the user may have selected as a first plug before
    // disconnecting.
    ESlateMicroworld.resetSelectedPlug(); 

    boolean disconnected = false;

    if (containsProvider(plug)) {
      ESlateMicroworld.connectComponent(plug);
      ESlateMicroworld.connectComponent(this);
      disconnected = true;
    }
    if (containsDependent(plug)) {
      ESlateMicroworld.connectComponent(this);
      ESlateMicroworld.connectComponent(plug);
      disconnected = true;
    }

    // Re-enable audio feedback.
    ESlateMicroworld.suppressAudio = false;

    if (!disconnected) {
      PlugNotConnectedException e =
        new PlugNotConnectedException(
          resources.getString("plug") + " " + plug.getName() + " " +
          resources.getString("notConnected"));
      throw e;
    }
  }

  /**
   * Connect the plug to a given plug. Both plugs must belong to the same,
   * non-null microworld.
   * @param     plug    The plug to which to connect this plug.
   * @exception PlugNotConnectedException       If the current plug could not
   *                    be connected to the given plug.
   */
  public synchronized void connectPlug(Plug plug)
    throws PlugNotConnectedException
  {
    // Disable audio feedback.
    ESlateMicroworld.suppressAudio = true;

    // Ignore any plug the user may have selected as a first plug before
    // connecting.
    ESlateMicroworld.resetSelectedPlug(); 

    boolean connected = false;

    Plug p1 = this;
    Plug p2 = plug;
    // The shared object will be provided by one of the two components.
    // If it is not provided by the first component, try swapping it
    // with the second.
    SharedObject so1 = p1.internals.getSharedObject();
    SharedObject so2 = p2.internals.getSharedObject();
    if (so1 == null && so2 != null) {
      Plug tmp = p1;
      p1 = p2;
      p2 = tmp;
    }

    int compatibility = plugCompatibility(p1, p2);
    if (compatibility == COMPATIBLE) {
      ESlateMicroworld.connectComponent(p1);
      ESlateMicroworld.connectComponent(p2);
      connected = true;
    }

    // Re-enable audio feedback.
    ESlateMicroworld.suppressAudio = false;

    if (!connected) {
      PlugNotConnectedException e =
        new PlugNotConnectedException(
          plugConnectionError(p1, p2, compatibility)
        );
      throw e;
    }
  }

  /**
   * Returns an error message explaining why two plugs cannot be connected.
   * @param     plug1           The first plug.
   * @param     plug2           The second plug.
   * @param     compatibility   The value returned by the plugCompatibility()
   *                            method, which describes why the two plugs
   *                            cannot be connected.
   */
  private String plugConnectionError(Plug plug1, Plug plug2, int compatibility)
  {
    String message;

    switch (compatibility) {
      case ALREADY_CONNECTED:
        Plug cp;
        if (plug1.isSingleConnectionProtocolPlug() &&
            plug2.internals.protocolPlugs.size() > 0) {
          cp = plug1;
        }else{
          cp = plug2;
        }
        message =
          resources.getString("singleConnected1") + cp.toString() +
          resources.getString("singleConnected2");
        break;
      case SAME_PLUG:
        message = resources.getString("notSelf");
        break;
      case INCOMPATIBLE:
        message = resources.getString("incompatible1");
        break;
      case NO_HANDLE:
        message = resources.getString("noHandle");
        break;
      case DIFFERENT_MICROWORLDS:
        message = resources.getString("differentMicroworlds");
        break;
      default:
        // Shouldn't happen.
        message = resources.getString("cantConnect1") + plug1.getName() +
                  resources.getString("cantConnect2") + plug2.getName();
    }
    return message;
  }

  /**
   * Disconnect either the input or the output part of the plug from a given
   * plug.
   * @param     plug    The plug to disconnect.
   * @param     what    If this is set to <code>INPUT_CONNECTION<code>, then
   *                    only the input part of the current plug is
   *                    disconnected. If this is set to
   *                    <code>OUTPUT_CONNECTION</code>, then only the output
   *                    part of the current plug is disconnected.
   * @exception PlugNotConnectedException       If the current plug had not
   *                    been connected to the given plug or if this plug's
   *                    microworld is null.
   * @exception java.security.InvalidParameterException If <code>what</code>
   *                    is anything other than <code>INPUT_CONNECTION</code>
   *                    or <code>OUTPUT_CONNECTION</code>.
   */
  public synchronized void disconnectPlug(Plug plug, int what)
    throws PlugNotConnectedException, InvalidParameterException
  {
    // Disable audio feedback.
    ESlateMicroworld.suppressAudio = true;

    switch (what) {
      case INPUT_CONNECTION:
        if (containsProvider(plug)) {
          // Disable audio feedback.
          ESlateMicroworld.suppressAudio = true;

          ESlateMicroworld.connectComponent(plug);
          ESlateMicroworld.connectComponent(this);

          // Re-enable audio feedback.
          ESlateMicroworld.suppressAudio = false;
        }else{
          PlugNotConnectedException e =
            new PlugNotConnectedException(
              resources.getString("plug") + " " + plug.getName() + " " +
              resources.getString("notConnectedToInput"));
          throw e;
        }
        break;
      case OUTPUT_CONNECTION:
        if (containsDependent(plug)) {
          // Disable audio feedback.
          ESlateMicroworld.suppressAudio = true;

          ESlateMicroworld.connectComponent(this);
          ESlateMicroworld.connectComponent(plug);

          // Re-enable audio feedback.
          ESlateMicroworld.suppressAudio = false;
        }else{
          PlugNotConnectedException e =
            new PlugNotConnectedException(
              resources.getString("plug") + " " + plug.getName() + " " +
              resources.getString("notConnectedToOutput"));
          throw e;
        }
        break;
      default:
        InvalidParameterException e =
          new InvalidParameterException(
            resources.getString("onlyIO"));
        throw e;
    }
  }

  /**
   * Specifies whether the plug has been selected for connecting to another
   * plug. If true, the plug's name will be highlit in the plug tree view.
   * @param     status  True or false.
   */
  void setSelected(boolean status)
  {
    selected = status;
    if (status) {
      myHandle.makeVisible(plugTreeNode);
    }
    myHandle.repaintTrees();
  }

  /**
   * Makes the plug visible in the parent component's plug view window.
   */
  void makeVisible()
  {
    myHandle.makeVisible(plugTreeNode);
  }

  /**
   * Clears the flag specifying compatibility with another plug.
   */
  void resetCompatibilityFlag()
  {
    compatibilityFlag = CANT_CONNECT_OR_DISCONNECT;
  }

  /*
   * Sets the flag specifying compatibility with another plug.
   * @param     p       The other plug. This method assumes that p will be the
   *                    provider plug and this plug will be the dependent plug.
   *                    In addition, this method swaps tries swapping the two,
   *                    if p is an input only plug and this plug is an output
   *                    plug.
   * @param     markExisting    Indicates whether plugs that can be
   *                            disconnected from the given plug will be
   *                            actually marked.
   * @param     markPossible    Indicates whether plugs that can be connected
   *                            to the given plug will be actually marked.
   */
  void setCompatibilityFlag(Plug p, boolean markExisting, boolean markPossible)
  {
    if (p.isOutputPlug() && p.containsDependent(this)) {
      if (markExisting) {
        compatibilityFlag = CAN_DISCONNECT;
      }else{
        compatibilityFlag = CANT_CONNECT_OR_DISCONNECT;
      }
      return;
    }
    if (p.isOnlyProtocolPlug() &&
        ((p.containsProvider(this) || p.containsDependent(this)))) {
      if (markExisting) {
        compatibilityFlag = CAN_DISCONNECT;
      }else{
        compatibilityFlag = CANT_CONNECT_OR_DISCONNECT;
      }
      return;
    }
    if (plugCompatibility(p, this) == COMPATIBLE) {
      if (markPossible) {
        compatibilityFlag = CAN_CONNECT;
      }else{
        compatibilityFlag = CANT_CONNECT_OR_DISCONNECT;
      }
      return;
    }
    if (p.isInputPlug() && !p.isOutputPlug() && isOutputPlug()) {
      if (containsDependent(p)) {
        if (markExisting) {
          compatibilityFlag = CAN_DISCONNECT;
        }else{
          compatibilityFlag = CANT_CONNECT_OR_DISCONNECT;
        }
        return;
      }
      if (plugCompatibility(this, p) == COMPATIBLE) {
        if (markPossible) {
          compatibilityFlag = CAN_CONNECT;
        }else{
          compatibilityFlag = CANT_CONNECT_OR_DISCONNECT;
        }
        return;
      }
    }
    compatibilityFlag = CANT_CONNECT_OR_DISCONNECT;
  }

  /**
   * Returns the value of the flag specifying compatibility with another plug.
   * @return    CAN_CONNECT, if the other plug can be connected to the plug,
   *            CAN_DISCONNECT, of the other plug can be disconnected from
   *            the plug, otherwise CANT_CONNECT_OR_DISCONNECT.
   */
  int getCompatibilityFlag()
  {
    return compatibilityFlag;
  }

  /**
   * Checks whether the plug has been selected for connecting to another
   * plug.
   * @return    The selection status of the plug.
   */
  boolean isSelected()
  {
    return selected;
  }

  /**
   * Creates a plug icon.
   * @param     url             The url containing the image of the icon.
   * @return    The plug's icon, colored with the color of the plug.
   *            <EM>Note:</EM> The coloring will be done by replacing all
   *            white pixels of the original image with the plug's color.
   */
  protected ImageIcon createPlugIcon(URL url)
  {
    ImageIcon icon = new ImageIcon(url);
    int w = icon.getIconWidth();
    int h = icon.getIconHeight();
    int nPixels = w * h;
    int[] pixels = new int[nPixels];
    PixelGrabber pg =
      new PixelGrabber(icon.getImage(), 0, 0, w, h, pixels, 0, w);
    try {
      pg.grabPixels();
    }catch (InterruptedException e) {
    }
    int replaceColor = Color.white.getRGB();
    int plugColor = getColor().getRGB();
    for (int i=0; i<nPixels; i++) {
      if (pixels[i] == replaceColor) {
        pixels[i] = plugColor;
      }
    }
    BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    im.setRGB(0, 0, w, h, pixels, 0, w);
    return new ImageIcon(im);
  }

  /**
   * Check whether this plug is connected to another plug.
   * @param     plug    The plug to check.
   * @return    True if yes, false if no. For protocol plugs and mixed mode
   *            plugs, the plug on which this method is invoked is considered
   *            as the provider, and the argument is considered as the
   *            dependent. In such cases, plug1.isConnected(plug2) may be
   *            different from plug2.isConnected(plug1).
   */
  public boolean isConnected(Plug plug)
  {
    return
      (containsDependent(plug) && plug.containsProvider(this)) ||
      (internals.hasProtocolPlug(plug) && plug.internals.hasProtocolPlug(this));
  }

  /**
   * Free resources. After invoking this method, the plug becomes unusable.
   */
  void dispose()
  {
    internals.dispose();
    internals = null;
    name = null;
    internalName = null;
    color = null;
    plugTreeNode = null;
    myHandle = null;
    connectionListeners.clear();
    connectionListeners = null;
    disconnectionListeners.clear();
    disconnectionListeners = null;
  }

  /**
   * Returns the icon that represents the plug.
   * @return    The icon.
   */
  public abstract ImageIcon getIcon();

  /**
   * Plug internals.
   */
  PlugInternals internals;

  /**
   * Plug name (the label).
   */
  protected String name;

  /**
   * Locale independent plug name (usually a resource bundle key).
   */
  protected String internalName;

  /**
   * Plug color.
   */
  protected Color color;

  /**
   * A reference to the plug tree node associated with the plug.
   */
  protected PlugTreeNode plugTreeNode;

  /**
   * A reference to the handle owned by the component to which the plug belongs.
   */
  protected ESlateHandle myHandle;

  /**
   * The set of listeners for a connection event.
   */
  protected ArrayList<ConnectionListener> connectionListeners;

  /**
   * The set of listeners for a discconnection event.
   */
  protected ArrayList<DisconnectionListener> disconnectionListeners;

  // Some constants describing plug compatibility
  /**
   * Plugs are compatible.
   */
  public final static int COMPATIBLE = 0;
  /**
   * Plugs are compatible, but one or both of them is a single input or single
   * output plug, and is already connected to another plug.
   */
  public final static int ALREADY_CONNECTED = 1;
  /**
   * Plugs are incompatible.
   */
  public final static int INCOMPATIBLE = 2;
  /**
   * Plugs are identical.
   */
  public final static int SAME_PLUG = 3;
  /**
   * One of the plugs does not have an E-Slate handle.
   */
  public final static int NO_HANDLE = 4;
  /**
   * Plugs belong to different microworlds.
   */
  public final static int DIFFERENT_MICROWORLDS = 5;

  /**
   * Indicates that a plug was connected to the input part of this plug.
   */
  public final static int INPUT_CONNECTION = 0;
  /**
   * Indicates that a plug was connected to the output part of this plug.
   */
  public final static int OUTPUT_CONNECTION = 1;
  /**
   * Indicates that a pure protocol plug was connected to this plug.
   */
  public final static int PROTOCOL_CONNECTION = 2;

  /**
   * Specifies that this plug is not a pure protocol plug, and that therefore no
   * role is required to be specified during the construction of the plug.
   */
  public final static int NO_ROLE = 0;

  /**
   * Specifies that this plug is a pure protocol plug playing the "left" side
   * role.
   */
  public final static int LEFT_ROLE = 1;

  /**
   * Specifies that this plug is a pure protocol plug playing the "right" side
   * role.
   */
  public final static int RIGHT_ROLE = 2;

  /**
   * Localized resources.
   */
  protected static ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.base.ESlateResource",
    Locale.getDefault()
  );

  /**
   * Specifies whether a plug is visible or not. This can be used to
   * "hide" plugs that should not be shown to the user, but must not be removed
   * from the component. (E.g., only show the plug associated with the
   * currently active document for a multiple-document editor.)
   */
  private boolean visible;

  /**
   * Specifies whether the plug has been selected for connecting to another
   * plug.
   */
  private boolean selected = false;

  /**
   * Specifies whether this plug can be connected to or disconnected from a
   * specified plug.
   */
  private int compatibilityFlag = CANT_CONNECT_OR_DISCONNECT;

  /**
   * The plug cannot be connected or disconnected from the specified plug.
   */
  final static int CANT_CONNECT_OR_DISCONNECT = 0;

  /**
   * The plug can be connected to the specified plug.
   */
  final static int CAN_CONNECT = 1;

  /**
   * The plug can be disconnected from the specified plug.
   */
  final static int CAN_DISCONNECT = 2;


  /**
   * This class encapsulates the shared object and the protocols that may be
   * associated with a plug, but should not be visible unless the particular
   * Plug subclass supports them.
   */
  class PlugInternals
  {
    /**
     * A reference to the object shared by the plug.
     */
    protected SharedObject shObj;
    /**
     * The name of the class of the object shared by the plug.
     */
    protected Class sharedObjectsClass;
    /**
     * The listener for receiving notification events whenever the shared
     * object of the plug connected to this plug changes.
     */
    SharedObjectListener sharedObjectListener;
    /**
     * The object implementing the protocols that the plug uses to communicate
     * with other plugs.
     */
    protected Object protocolImplementor;
    /**
     * The Java interfaces that the other Plug's component must implement.
     */
    protected Vector requiredInterfaces;
    /**
     * The Java interfaces that the other Plug's component may optionally
     * implement.
     */
    protected Vector optionalInterfaces;
    /**
     * The Java interfaces that the protocol implementor declares that it
     * implements.
     */
    protected Vector exportedInterfaces = null;

    /**
     * The role that the plug plays if it is a pure protocol plug.
     */
    int plugRole;

    /**
     * The list of dependents.
     */
    protected ArrayList<Plug> dependentPlugs = new ArrayList<Plug>();

    /**
     * The list of providers.
     */
    protected ArrayList<Plug> providerPlugs = new ArrayList<Plug>();

    /**
     * The list of protocol plugs.
     */
    protected ArrayList<Plug> protocolPlugs = new ArrayList<Plug>();

    /**
     * Specifies whether this plug is used for hosting components.
     */
    protected boolean hostingPlug = false;

    /**
     * Construct a PlugInternals instance.
     * @param   so      The sharable object shared by the plug.
     * @param   cl      The class of the sharable object shared by the plug.
     * @param   implementor     The object implementing the interfaces
     *                          that are part of this plug's communication
     *                          protocol. This object can be a reference to
     *                          the component owning the plug, or to another
     *                          object to which the component has delegated
     *                          the implementation of the protocol.
     * @param   role            If this plug is a pure protocol plug, this
     *                          parameter would have one of the values
     *                          <CODE>LEFT_ROLE</CODE> or <CODE>RIGHT<ROLE>,
     *                          indicating that the plug is playin the "left"
     *                          or "right" side role of the protocol,
     *                          respectively. Otherwise, this parameter should
     *                          have the value <CODE>NO_ROLE</CODE>.
     *                          The role is used, like color, to provide the
     *                          user with a visual hint as to what plugs can be
     *                          connected together.
    */
    PlugInternals(SharedObject so, Class cl, SharedObjectListener l,
                  Object implementor, int role)
      throws InvalidPlugParametersException
    {
      super();
      if ((so != null) && (cl != null) && !cl.isInstance(so)) {
        InvalidPlugParametersException e =
          new InvalidPlugParametersException(resources.getString("badShObj"));
        throw e;
      }
      shObj = so;
      sharedObjectsClass = cl;
      sharedObjectListener = l;
      protocolImplementor = implementor;
      plugRole = role;
    }

    /**
     * Specifies the Java interfaces that objects connecting through this plug
     * must implement.
     * @param   required        A vector of the Java interfaces that objects
     *                          connecting through this plug must implement.
     */
    void setRequiredInterfaces(Vector required)
    {
      requiredInterfaces = required;
    }

    /**
     * Specifies the Java interfaces that objects connecting through this plug
     * may optionally implement.
     * @param   optional        A vector of the Java interfaces that objects
     *                          connecting through this plug may optionally
     *                          implement.
     */
    void setOptionalInterfaces(Vector optional)
    {
      optionalInterfaces = optional;
    }

    /**
     * Specifies the Java interfaces that the plug declares that the protocol
     * implementor implements.
     * @param   exported        A vector of the Java interfaces that the
     *                          plug declares that the protocol implementor
     *                          implements. Setting this to null is equivalent
     *                          to declaring that the protocol implementor
     *                          implements all the interfaces that it actually
     *                          implements.
     * @exception       InvalidPlugParametersException  This
     *                          exception is thrown when the declared
     *                          interfaces cannot be accepted.
     */
    void setExportedInterfaces(Vector exported)
      throws InvalidPlugParametersException
    {
      if (exported != null) {
        int nExported = exported.size();

        // Does the plug have a protocol implementor?
        if (protocolImplementor == null) {
          InvalidPlugParametersException e =
            new InvalidPlugParametersException(
              resources.getString("noImplementor")
            );
          throw e;
        }
        // Check that none of the provided interfaces is null
        for (int i=0; i<nExported; i++) {
          if (exported.elementAt(i) == null) {
            InvalidPlugParametersException e =
              new InvalidPlugParametersException(
                resources.getString("nullInterface")
              );
            throw e;
          }
        }
/*
        // Are the provided classes actually Java interfaces?
        for (int i=0; i<nExported; i++) {
          Object t = exported.elementAt(i);
          if (!(t instanceof Class) || !((Class)t).isInterface()) {
            InvalidPlugParametersException e =
              new InvalidPlugParametersException(
                resources.getString("class") + " " + t + " " +
                resources.getString("notInterface")
              );
            throw e;
          }
        }
*/
        // Are the provided interfaces actually implemented by the protocol
        // implementor?
        for (int i=0; i<nExported; i++) {
          Class c = (Class)(exported.elementAt(i));
          if (!c.isInstance(protocolImplementor)) {
            InvalidPlugParametersException e =
              new InvalidPlugParametersException(
                resources.getString("notImplements") + " " + c.getName()
              );
              throw e;
          }
        }
      }
      exportedInterfaces = exported;
    }

    /**
     * Returns the object implementing the protocols that this plug uses to
     * communicate with other plugs.
     * @return  The requested object. This object can be a reference to the
     *          component owning the plug, or to another object to which the
     *          component has delegated the implementation of the protocol.
     */
    Object getProtocolImplementor()
    {
      return protocolImplementor;
    }

    /**
     * Returns a reference to the protocol plug that is connected to this plug
     * and has a given name. Protocol plugs are plugs that in addition to (or
     * instead of) providing communication via a shared object, force
     * each of two objects connected via the plug to implement a specific
     * Java interface (different for each of the two components).
     * @param   protocolPlugName        The name of the requested plug.
     * @return  The requested plug.
     */
    IProtocolPlug getProtocolPlug(String protocolPlugName)
    {
      if (protocolPlugs == null) {
        return null;
      }
      synchronized (protocolPlugs) {
        Plug protocolPlug = null;
        int nPlugs = protocolPlugs.size();
        for (int i = 0; i < nPlugs; i++) {
          protocolPlug = protocolPlugs.get(i);
          if (protocolPlug.getName().equals(protocolPlugName)) {
            return (IProtocolPlug)protocolPlug;
          }
        }
      }
      return null;
    }

    /**
     * Returns a list of all protocol plugs connected to this plug.
     * Protocol plugs are plugs that in addition to (or
     * instead of) providing communication via a shared object, force
     * each of two objects connected via the plug to implement a specific
     * Java interface (different for each of the two components).
     * associated with a shared object.
     * @return  The protocol plugs.
     */
    IProtocolPlug[] getProtocolPlugs()
    {
      if (protocolPlugs == null) {
        return new IProtocolPlug[0];
      }
      synchronized (protocolPlugs) {
        int size = protocolPlugs.size();
        IProtocolPlug[] p = new IProtocolPlug[size];
        for (int i=0; i<size; i++) {
          p[i] = (IProtocolPlug)(protocolPlugs.get(i));
        }
        return p;
      }
    }

    /**
     * For plugs where there is only one component connected to them that
     * communicates with these plugs through a Java interface, this method
     * provides a shortcut that returns the protocol plug directly, rather than
     * the array that is returned by
     * {@link #getProtocolPlugs()}.
     * @return  The protocol plug.
     * @exception       NoSingleConnectedComponentException     This exception
     *          is thrown when there is not exactly one protocol plug connected
     *          to this plug.
     *
     */
    IProtocolPlug getProtocolPlug()
      throws NoSingleConnectedComponentException
    {
      IProtocolPlug[] p = getProtocolPlugs();
      String s;
      int size = p.length;
      if (size < 1 || size > 1) {
        
        if (size < 1) {
          s = resources.getString("noComponent");
        }else{
          s = resources.getString("manyComponents");
        }
        NoSingleConnectedComponentException e =
          new NoSingleConnectedComponentException(s);
        throw e;
      }else{
        return p[0];
      }
    }

    /**
     * Adds a plug to the list of protocol plugs connected to this plug.
     * @param   protocolPlug    The plug to add.
     */
    void addProtocolPlug(Plug protocolPlug)
    {
      synchronized (protocolPlugs) {
        if (!protocolPlugs.contains(protocolPlug)) {
          protocolPlugs.add(protocolPlug);
        }
      }
    }

    /**
     * Removes a plug from the list of protocol plugs connected to this plug.
     * @param   protocolPlug    The plug to remove.
     */
    void removeProtocolPlug(Plug protocolPlug)
    {
      synchronized (protocolPlugs) {
        if (protocolPlugs.contains(protocolPlug)) {
          protocolPlugs.remove(protocolPlugs.indexOf(protocolPlug));
       }
      }
    }

    /**
     * Returns whether a given plug is used to communicate with this plug
     * via a Java interface.
     * @param   p       The plug to check.
     * @return  True if yes, otherwise false.
     */
    boolean hasProtocolPlug(Plug p)
    {
      synchronized (protocolPlugs) {
        return protocolPlugs.contains(p);
      }
    }

    /**
     * Returns a reference to the provider plug that is connected to this plug
     * and has a given name. In the case of shared object or mixed mode plugs,
     * a provider plug is a plug that provides, i.e., is associated with a
     * shared object. In the case of protocol plugs, a provider plug is simply
     * the first plug in a pair of connected plugs.
     * @param   providerName    The name of the requested plug.
     * @return  The requested plug.
     */
    Plug getProvider(String providerName)
    {
      if (providerPlugs == null) {
        return null;
      }
      synchronized (providerPlugs) {
        Plug providerPlug = null;
        int nPlugs = providerPlugs.size();
        for (int i = 0; i < nPlugs; i++) {
          providerPlug = providerPlugs.get(i);
          if (providerPlug.getName().equals(providerName)) {
            return providerPlug;
          }
        }
      }
      return null;
    }

    /**
     * Returns a list of all provider plugs connected to this plug.
     * In the case of shared object or mixed mode plugs,
     * a provider plug is a plug that provides, i.e., is associated with a
     * shared object. In the case of protocol plugs, a provider plug is simply
     * the first plug in a pair of connected plugs.
     * @return  An array containing references to the provider plugs.
     *          If the plug has no providers, an empty array is returned.
     */
    Plug[] getProviders()
    {
      if (providerPlugs == null) {
        return new Plug[0];
      }
      synchronized (providerPlugs) {
        int size = providerPlugs.size();
        Plug[] p = new Plug[size];
        for (int i=0; i<size; i++) {
          p[i] = providerPlugs.get(i);
        }
        return p;
      }
    }

    /**
     * Tests whether the plug has any provider plugs connected to it.
     * @return  True if yes, false if not.
     */
    boolean hasProvidersConnected()
    {
      boolean result;
      synchronized (providerPlugs) {
        if (providerPlugs != null && providerPlugs.size() != 0) {
          result = true;
        }else{
          result = false;
        }
      }
      return result;
    }

    /**
     * Adds a plug to the list of provider plugs connected to this plug.
     * @param   providerPlug    The plug to add.
     */
    void addProvider(Plug providerPlug)
    {
      synchronized (providerPlugs) {
        if (!providerPlugs.contains(providerPlug)) {
          providerPlugs.add(providerPlug);
        }
      }
    }

    /**
     * Removes a plug from the list of provider plugs connected to this plug.
     * @param   providerPlug    The plug to remove.
     */
    void removeProvider(Plug providerPlug)
    {
      synchronized (providerPlugs) {
        if (providerPlugs.contains(providerPlug)) {
          providerPlugs.remove(providerPlugs.indexOf(providerPlug));
        }
      }
    }

    /**
     * Checks whether a given plug is a provider plug connected to this plug.
     * @param   providerPlug    The plug to check.
     * @return  True if yes, false otherwise.
     */
    boolean containsProvider(Plug providerPlug)
    {
      synchronized (providerPlugs) {
        if (providerPlugs.contains(providerPlug)) {
          return true;
        }else{
          return false;
        }
      }
    }

    /**
     * Returns a reference to the dependent plug that is connected to this plug
     * and has a given name.
     * In the case of shared object or mixed mode plugs,
     * a dependent plug is a plug that is not associated with a
     * shared object. In the case of protocol plugs, a provider plug is simply
     * the second plug in a pair of connected plugs.
     * @param   dependentName   The name of the requested plug.
     * @return  The requested plug.
     */
    Plug getDependent(String dependentName)
    {
      if (dependentPlugs == null) {
        return null;
      }
      synchronized (dependentPlugs) {
        Plug dependentPlug = null;
        int nPlugs = dependentPlugs.size();
        for (int i = 0; i < nPlugs; i++) {
          dependentPlug = dependentPlugs.get(i);
          if (dependentPlug.getName().equals(dependentName)) {
            return dependentPlug;
          }
        }
      }
      return null;
    }

    /**
     * Returns a list of all dependent plugs connected to this plug.
     * In the case of shared object or mixed mode plugs,
     * a dependent plug is a plug that is not associated with a
     * shared object. In the case of protocol plugs, a provider plug is simply
     * the second plug in a pair of connected plugs.
     * @return  An array containing references to the dependent plugs.
     *          If the plug has no dependents, an empty array is returned.
     */
    Plug[] getDependents()
    {
      if (dependentPlugs == null) {
        return new Plug[0];
      }
      synchronized (dependentPlugs) {
        int size = dependentPlugs.size();
        Plug[] p = new Plug[size];
        for (int i=0; i<size; i++) {
          p[i] = dependentPlugs.get(i);
        }
        return p;
      }
    }

    /**
     * Tests whether the plug has any dependent plugs connected to it.
     * @return  True if yes, false if not.
     */
    boolean hasDependentsConnected()
    {
      boolean result;
      synchronized (dependentPlugs) {
        if (dependentPlugs != null && dependentPlugs.size() != 0) {
          result = true;
        }else{
          result = false;
        }
      }
      return result;
    }

    /**
     * Adds a plug to the list of dependent plugs connected to this plug.
     * @param   dependentPlug   The plug to add.
     */
    void addDependent(Plug dependentPlug)
    {
      synchronized (dependentPlugs) {
        if (!dependentPlugs.contains(dependentPlug)) {
          dependentPlugs.add(dependentPlug);
        }
      }
    }

    /**
     * Removes a plug from the list of dependent plugs connected to this plug.
     * @param   dependentPlug   The plug to remove.
     */
    void removeDependent(Plug dependentPlug)
    {
      synchronized (dependentPlugs) {
        if (dependentPlugs.contains(dependentPlug)) {
          dependentPlugs.remove(dependentPlugs.indexOf(dependentPlug));
        }
      }
    }

    /**
     * Checks whether a given plug is a dependent plug connected to this plug.
     * @param   dependentPlug   The plug to check.
     * @return  True if yes, false otherwise.
     */
    boolean containsDependent(Plug dependentPlug)
    {
      synchronized (dependentPlugs) {
        if (dependentPlugs.contains(dependentPlug)) {
          return true;
        }else{
          return false;
        }
      }
    }

    /**
     * Returns the class name of the shared object associated with the plug.
     * @return  The class name.
     */
    String getSharedObjectsClassName()
    {
      if (sharedObjectsClass != null) {
        return sharedObjectsClass.getName();
      }else{
        return null;
      }
    }

    /**
     * Returns a reference to the shared object associated with the plug.
     * @return  The shared object.
     */
    SharedObject getSharedObject()
    {
      return shObj;
    }

    /**
     * Returns the required Java interfaces used to communicate with this plug.
     * @return  A list of Class instances corresponding to the requested
     *          interfaces.
     */
    public Vector getRequiredInterfaces()
    {
      return (Vector)(requiredInterfaces.clone());
    }

    /**
     * Returns the Java interfaces that can be optionally used to communicate
     * with this plug. This is a copy of the list of interfaces specified
     * during the plug's construction. The interfaces that a component
     * connected to this plug implements is a subset of this list.
     * @return  A list of Class instances corresponding to the requested
     *          interfaces.
     */
    public Vector getOptionalInterfaces()
    {
      return (Vector)(optionalInterfaces.clone());
    }

    /**
     * Returns the Java interfaces that the plug declares that the protocol
     * implementor implements. If no interfaces have been explicitly declared,
     * this method returns a list of all the interfaces implemented by the
     * protocol implementor.
     * @return  A vector containing the requested interfaces.
     */
    public Vector getExportedInterfaces()
    {
      if (exportedInterfaces == null) {
        exportedInterfaces = getAllExportedInterfaces();
      }
      return (Vector)(exportedInterfaces.clone());
    }

    /**
     * Returns a vector containing all the interfaces that the protocol
     * implementor implements.
     * @return  The requested vector.
     */
    private Vector getAllExportedInterfaces()
    {
      if (protocolImplementor == null) {
        return new Vector(0);
      }else{
        HashSet<Class> s = new HashSet<Class>();
        getAllExportedInterfaces(protocolImplementor.getClass(), s);
        int n = s.size();
        Vector v = new Vector(n);
        Iterator<Class> it = s.iterator();
        for (int i=0; i<n; i++) {
          v.add(it.next());
        }
        return v;
      }
    }

    /**
     * Recursively fills in a vector with the interfaces implemented by a
     * class.
     * @param   c       The class.
     * @param   s       A set where the interfaces will be placed.
     */
    private void getAllExportedInterfaces(Class c, HashSet<Class> s)
    {
      s.add(c);
      Class[] in = c.getInterfaces();
      for (int i=0; i<in.length; i++) {
        s.add(in[i]);
      }
      c = c.getSuperclass();
      if (c != null) {
        getAllExportedInterfaces(c, s);
      }
    }

    /**
     * Returns a list of the handles of the components connected to this plug
     * that communicate with this plug through a Java interface.
     * @return  The E-Slate handles of the components.
     */
    public ESlateHandle[] getProtocolHandles()
    {
      synchronized (protocolPlugs) {
        int size = protocolPlugs.size();
        ESlateHandle[] handles = new ESlateHandle[size];
        for (int i=0; i<size; i++) {
          handles[i] = protocolPlugs.get(i).getHandle();
        }
        return handles;
      }
    }

    /**
     * For plugs where there is only one component connected to them that
     * communicates with these plugs through a Java interface, this method
     * provides a shortcut that returns the component directly, rather than
     * through an array.
     * @return  The E-Slate handle of the component.
     * @exception       NoSingleConnectedComponentException     This exception
     *          is thrown when there is not exactly one component connected to
     *          this plug.
     */
    public Object getProtocolHandle() throws NoSingleConnectedComponentException
    {
      ESlateHandle[] h = getProtocolHandles();
      String s;
      int size = h.length;
      if (size < 1 || size > 1) {
        if (size < 1) {
          s = resources.getString("noComponent");
        }else{
          s = resources.getString("manyComponents");
        }
        NoSingleConnectedComponentException e =
          new NoSingleConnectedComponentException(s);
        throw e;
      }else{
        return h[0];
      }
    }

    /**
     * Return the listener for receiving notification events whenever the
     * shared object of the plug connected to this plug changes.
     * @return  The requested listener.
     */
    public SharedObjectListener getSharedObjectListener()
    {
      return sharedObjectListener;
    }

    /**
     * Specifies whether the plug participates in the hosting mechanism.
     * @param   hostingPlug     True if yes, false otherwise.
     */
    void setHostingPlug(boolean hostingPlug)
    {
      this.hostingPlug = hostingPlug;
    }

    /**
     * Checks whether the plug participates in the hosting mechanism.
     * @return  True if yes, false otherwise.
     */
    boolean isHostingPlug()
    {
      return hostingPlug;
    }

    /**
     * Free resources. After invoking this method, the PlugInternals instance
     * becomes unusable.
     */
    void dispose()
    {
      shObj = null;
      sharedObjectsClass = null;
      sharedObjectListener = null;
      protocolImplementor = null;
      if (requiredInterfaces != null) {
        requiredInterfaces.clear();
        requiredInterfaces = null;
      }
      if (optionalInterfaces != null) {
        optionalInterfaces.clear();
        optionalInterfaces = null;
      }
      if (exportedInterfaces != null) {
        exportedInterfaces.clear();
        exportedInterfaces = null;
      }
      dependentPlugs.clear();
      dependentPlugs = null;
      providerPlugs.clear();
      providerPlugs = null;
    }
  }
}
