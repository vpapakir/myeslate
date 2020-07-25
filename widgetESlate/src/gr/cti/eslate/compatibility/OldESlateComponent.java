package gr.cti.eslate.compatibility;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import gr.cti.eslate.base.*;

/**
 * This class implements most of the functionality of the pre-1.5
 * ESlateComponent class. It can be used to make it easier to port existing
 * E-Slate components to E-Slate 1.5.
 * <P>
 * Here is a summary of the changes required to port ESlateComponents to
 * E-Slate 1.5:
 * <UL>
 * <LI>components must import <CODE>gr.cti.eslate.compatibility.*</CODE> in
 * addition to any other E-Slate classes.</LI>
 * <LI>Components extend class <CODE>OldESlateComponent</CODE> instead of
 * class <CODE>ESlateComponent</CODE>.</LI>
 * <LI>In plug constructors, replace the reference to the ESlateComponent
 * owning the plug (usually <CODE>this</CODE>) with <CODE>getHandle()</CODE>.
 * If the plug is an input plug then add <CODE>this</CODE> as an additional
 * argument in the invocation of the plug onstructor.</LI>
 * <LI>To create a shared object, instead of an ESLateComponnet (usually
 * "this"), pass the component's handle (usually "getHandle()") to the shared
 * object's constructor.</LI>
 * <LI>To invoke the shared object event handler of the component owned by
 * another plug (e.g., in connection listeners that transfer data to the
 * connected component upon connection), replace calls such as
 * <BR>
 * <CODE>((SharedObjectListener)(e.getPlug().getComponent())).handleSharedObjectEvent(soe);</CODE>
 * <BR>
 * i.e., calls to the <CODE>handleSharedObjectEvent</CODE> method of the other
 * component, with calls such as
 * <BR>
 * <CODE>e.getPlug().getSharedObjectListener().handleSharedObjectEvent(soe);</CODE>
 * <BR>
 * i.e., with calls to the <CODE>handleSharedObjectEvent</CODE> method of the
 * default shared object listener associated with the other plug.</LI>
 * <LI>If the component's <CODE>getInfo()</CODE> method returns localized
 * strings, make sure that the appropriate resource bundle has been opened in
 * the constructor and not in the <CODE>init()</CODE> method; otherwise you
 * may get a NullPointerException in <CODE>OldESlateComponent.init()</CODE>
 * where <CODE>getInfo()</CODE> is invoked().</LI>
 * </UL>
 * <P>
 * In addition to the above, the following changes should be made to the
 * shared objects used by the ported components:
 * <UL>
 * <LI>As with components, <CODE>gr.cti.eslate.compatibility.*</CODE> must be
 * imported.</LI>
 * <LI>Constructors must take an <CODE>OldESlateHandle</CODE> instead of an
 * <CODE>ESlateComponent</CODE> as an argument. In the invocation of super()
 * in the constructor, instead of passing the OldESlateComponent as an
 * argument, the above ESlateHandle should be passed instead. E.g.:
 * <PRE>
 * public ExampleSO(ESlateHandle handle)
 * {
 *   super(handle);
 *   ...
 * }
 * </PRE></LI>
 * <LI>When creating shared object events, instead of specifying
 * <CODE>getComponent()</CODE> as the event source, specify
 * <CODE>getHandle().getComponent()</CODE> instead.</LI>
 * </UL>
 * <P>
 * Applet parameters:
 * <UL>
 * <LI>
 * <B>MENUBAR</B> (boolean). Indicates whether the bar containing various
 * controls will appear at the top.
 * </LI>
 * <LI>
 * <B>DEBUG</B> (boolean). Indicates whether debugging messages will be
 * printed.
 * </LI>
 * </UL>
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public abstract class OldESlateComponent
  extends JApplet
  implements ESlateInfoInterface, ESlateListener, ESlatePart, Externalizable
{
  transient private ESlateHandle handle;
  transient private JPanel normalViewPanel;

  /**
   * Creates a new component.
   */
  public OldESlateComponent()
  {
    super();
    handle = ESlate.registerPart(this);
    handle.addESlateListener(this);
  }

  /**
   * Initialize the component.
   */
  public void init()
  {
    super.init();

    // This may require having set the locale in the component, so we can't
    // make this call in the constructor, where the component has not yet been
    // given a chance to run any of its code.
    handle.setInfo(getInfo());

    // Ditto for calling getAppletContext(), which is invoked when laying
    // out the component; it always fails when called from the constructor.
    // Thus, we lay out the component in init().
    normalViewPanel = new JPanel(new BorderLayout());
    if (!UIManager.getLookAndFeel().getName().equals("CDE/Motif")) {
      normalViewPanel.setBackground(Color.lightGray);
    }
    normalViewPanel.setForeground(Color.green);
    normalViewPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    Container cp = getContentPane();
//    try {
//      cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
//      } catch (AWTError e) {
//    }
//    cp.add(handle.getMenuPanel());
//    cp.add(normalViewPanel);
    GridBagLayout layout = new GridBagLayout();
    try {
      cp.setLayout(layout);
    } catch (AWTError e) {
    }

    // Check if we are running as an applet
    boolean isApplet;
    try {
      getAppletContext();
      isApplet = true;
    } catch (Exception e) {
      isApplet = false;
    }

    // Check if menu bar is wanted
    boolean needMenuPanel = true;
    if (isApplet) {
      String str;
      str = getParameter("menubar");
      if (str != null) {
        needMenuPanel = Boolean.valueOf(str).booleanValue();
      }
    }

    // Check whether debugging should be enabled
    if (isApplet) {
      String str;
      str = getParameter("debug");
      if (str != null) {
        handle.setDebugStatus(Boolean.valueOf(str).booleanValue());
      }
    }

    // Add the menu panel
    GridBagConstraints constraints = new GridBagConstraints();
    // Place in first column (only one column exists)
    constraints.gridx = 0;
    // Place below previous one
    constraints.gridy = GridBagConstraints.RELATIVE;
    // Take up all the row (1 column)
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    // Expand area for component only in x direction
    constraints.fill = GridBagConstraints.HORIZONTAL;
    // Don't expand component in y direction
    constraints.weighty = 0;
    // Expand component in x direction
    constraints.weightx = 1;
    if (needMenuPanel) {
      layout.setConstraints(cp.add(handle.getMenuPanel()), constraints);
    }

    // Add the normal view panel

    // Take up all remaining y space
    constraints.gridheight = GridBagConstraints.REMAINDER;
    // Expand area for component in both x and y directions
    constraints.fill = GridBagConstraints.BOTH;
    // Expand component in x direction
    constraints.weightx = 1;
    // Expand component in y direction
    constraints.weighty = 1;
    layout.setConstraints(cp.add(normalViewPanel), constraints);

    // Notify state changed listeners
    handle.getESlateMicroworld().fireComponentStateChangedListeners(
      this, ESlateMicroworld.COMPONENT_INITIALIZED);
  }

  /**
   * Overrides JApplet.start(), to invoke any component state changed
   * listeners.
   */
  public void start()
  {
    super.start();
    // Notify state changed listeners
    handle.getESlateMicroworld().fireComponentStateChangedListeners(
      this, ESlateMicroworld.COMPONENT_STARTED);
  }

  /**
   * Overrides JApplet.stop(), to invoke any component state changed
   * listeners.
   */
  public void stop()
  {
    super.stop();
    // Notify state changed listeners
    handle.getESlateMicroworld().fireComponentStateChangedListeners(
      this, ESlateMicroworld.COMPONENT_STOPPED);
  }

  /**
   * Returns the panel on which the component's graphical elements can be
   * drawn.
   * @return    The requested panel.
   */
  public JPanel getNormalViewPanel()
  {
    return normalViewPanel;
  }

  /**
   * Adds a plug to the component.
   * @param     plug    The plug to be added to the component.
   * @exception PlugExistsException     This exception is thrown when trying
   *                    to add to a component a plug that has the same name as
   *                    another plug already attached to the component at the
   *                    same level as this plug.
   * @exception IllegalArgumentException        if <code>plug</code> is null
   */
  public void addPlug(Plug plug)
    throws PlugExistsException, IllegalArgumentException
  {
    handle.addPlug(plug);
  }

  /**
   * Removes a plug from the component.
   * @param     plug    The plug to be removed from the component.
   * @exception NoSuchPlugException     This exception is thrown when trying
   *                    to remove from a component a plug that it doesn't
   *                    actually have.
   * @exception IllegalArgumentException        if <code>plug</code> is null
   */
  public void removePlug(Plug plug)
    throws NoSuchPlugException, IllegalArgumentException
  {
    handle.removePlug(plug);
  }

  /**
   * Returns a reference to a given plug. Only top level plugs are scanned.
   * @param     plugName        The name of the plug.
   * @return    A reference to the requested plug.
   */
  public Plug getPlug(String plugName)
  {
    return handle.getPlug(plugName);
  }

  /**
   * Returns a copy of the list of all top level plugs that the component
   * contains.
   * @return    The list of plugs.
   */
  @SuppressWarnings(value={"unchecked"})
  public Vector getPlugs()
  {
    // We want to return a Vector for compatibility with old ESlateComponent's
    // getPlugs().
    Plug[] p = handle.getPlugs();
    Vector v = new Vector(p.length);
    for (int i=0; i<p.length; i++) {
      v.addElement(p[i]);
    }
    return v;
  }

  /**
   * Returns a reference to the E-Slate connection data.
   * @return    The requested reference. If the component's constructor has
   *            not been called, this method returns null.
   */
  public ESlateMicroworld getESlateMicroworld()
  {
    if (handle == null) {
      return null;
    }else{
      return handle.getESlateMicroworld();
    }
  }

  /**
   * Returns the name of the component.
   * @return    The component's name.
   */
  public String getComponentName()
  {
    if (handle != null) {
      return handle.getComponentName();
    } else {
      return "";
    }
  }

  /**
   * Sets the name of the component.
   * <BR>
   * <B>Note:</B> We can't call this method setName, as it would override
   * java.awt.setName, and Java won't allow us to do this because of the
   * thrown exception.
   * @param     name    The component's name.
   * @exception NameUsedException       This exception is thrown when trying
   *                    to rename a component using a name that is being used
   *                    by another component.
   * @exception RenamingForbiddenException      This exception is thrown when
   *                    renaming the component is not allowed.
   */
  public void setComponentName(String name)
    throws NameUsedException, RenamingForbiddenException
  {
    handle.setComponentName(name);
  }

  /**
   * Sets the name of the component. If the name given as an argument is
   * already used by another component, a " " and a unique number will be
   * appended to the name so that renaming the component is guaranteed to
   * succeed.
   * @exception RenamingForbiddenException      This exception is thrown
   *                    when renaming the component is not allowed.
   */
  public void setUniqueComponentName(String suggestedName)
    throws RenamingForbiddenException
  {
    handle.setUniqueComponentName(suggestedName);
  }

  /**
   * Invoked when the name of the component is changed. Override this method
   * to receive notification when the component is renamed.
   * @param     newName The new name of the component.
   */
  public void componentNameChanged(String newName)
  {
  }

  /**
   * Invoked immediately before an E-Slate handle is disposed. Override this
   * method to perform clean-up.
   */
  public void disposingHandle(HandleDisposalEvent e)
  {
  }

  /**
   * Invoked immediately after an E-Slate handle is disposed. Override this
   * method to perform clean-up.
   */
  public void handleDisposed(HandleDisposalEvent e)
  {
  }

  /**
   * Handler for component name changed events.
   */
  final public void componentNameChanged(ComponentNameChangedEvent e)
  {
    componentNameChanged(e.getNewName());
  }

  /**
   * Returns the locale under which the component is running.
   * Currently, this is a reference to the default locale.
   * @return    A reference to the locale.
   */
  public Locale getLocale()
  {
    return handle.getLocale();
  }

  /**
   * Returns a reference to the E-Slate handle used internally by this
   * component. If all else fails, use this!
   * @return    The requested handle. If the component's constructor has not
   *            been called, this method returns null.
   */
  public ESlateHandle getHandle()
  {
    return handle;
  }

  /**
   * Cleans up when the component is destroyed.
   */
  public void destroy()
  {
    // Notify state changed listeners
    handle.getESlateMicroworld().fireComponentStateChangedListeners(
      this, ESlateMicroworld.COMPONENT_DESTROYED);

    ESlateHandle.removeAllRecursively(normalViewPanel);
    normalViewPanel = null;
    handle.dispose();
    handle = null;
    ESlateHandle.removeAllRecursively(this);
    super.destroy();
  }

  /**
   * Specifies whether debugging messages should be printed.
   * @param     status  True if yes, false if not.
   */
  public void setDebugStatus(boolean status)
  {
    handle.setDebugStatus(status);
  }

  /**
   * Returns whether debugging messages are printed.
   * @return    True if yes, false if not.
   */
  public boolean getDebugStatus()
  {
    return handle.getDebugStatus();
  }

  /**
   * Returns the component's E-Slate handle.
   * @return    The requested handle. If the component's constructor has not
   *            been called, this method returns null.
   */
  public ESlateHandle getESlateHandle()
  {
    return handle;
  }

  /**
   * Save the component's state. Override to save your component's state.
   * @param     oo      The stream to which the state will be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
  }

  /**
   * Load the component's state. Override to load your component's state.
   * @param     oi      The stream from which the state will be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
  } 
}
