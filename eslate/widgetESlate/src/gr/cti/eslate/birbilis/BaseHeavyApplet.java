package gr.cti.eslate.birbilis;

import java.applet.*;
import java.awt.*;
import java.util.*;
import java.io.*;

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
 * <LI>Components extend class <CODE>BaseApplet</CODE> instead of
 * class <CODE>ESlateComponent</CODE>.</LI>
 * <LI>In plug constructors, replace the reference to the ESlateComponent
 * owning the plug (usually <CODE>this</CODE>) with <CODE>getHandle()</CODE>.
 * If the plug is an input plug then add <CODE>this</CODE> as an additional
 * argument in the invocation of the plug onstructor.</LI>
 * <LI>To invoke the shared object event handler of the component owned by
 * another plug (e.g., in connection listeners that transfer data to the
 * connected component upon connection), replace calls such as
 * <BR>
 * <CODE>((SharedObjectListener)(e.getPin().getComponent())).handleSharedObjectEvent(soe);</CODE>
 * <BR>
 * i.e., calls to the <CODE>handleSharedObjectEvent</CODE> method of the other
 * component, with calls such as
 * <BR>
 * <CODE>e.getPin().getSharedObjectListener().handleSharedObjectEvent(soe);</CODE>
 * <BR>
 * i.e., with calls to the <CODE>handleSharedObjectEvent</CODE> method of the
 * default shared object listener associated with the other plug.</LI>
 * <LI>If the component's <CODE>getInfo()</CODE> method returns localized
 * strings, make sure that the appropriate resource bundle has been opened in
 * the constructor and not in the <CODE>init()</CODE> method; otherwise you
 * may get a NullPointerException in <CODE>BaseApplet.init()</CODE>
 * where <CODE>getInfo()</CODE> is invoked().</LI>
 * </UL>
 * <P>
 * In addition to the above, the following changes should be made to the
 * shared objects used by the ported components:
 * <UL>
 * <LI>As with components, <CODE>gr.cti.eslate.compatibility.*</CODE> must be
 * imported.</LI>
 * <LI>Constructors must take an <CODE>BaseApplet</CODE> instead of an
 * <CODE>ESlateComponent</CODE> as an argument. In the invocation of super()
 * in the constructor, instead of passing the BaseApplet as an
 * argument, the result of the invocation of its <CODE>getHandle()</CODE>
 * method should be passed instead. E.g.:
 * <PRE>
 * public ExampleSO(BaseApplet comp)
 * {
 *   super(comp.getHandle());
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
 * @author      George Birbilis, based on OldESlateComponent by Kriton Kyrimis
 * @version     1.0, 2Jun1999
 */
public abstract class BaseHeavyApplet //3Aug1999: for TV
  extends Applet //extends the java.awt.Applet instead of javax.swing.JApplet (to support heavy-weight content)
  implements ESlatePart, java.io.Externalizable //20May1999: added Externalizable
{
  private transient ESlateHandle avHandle; //20May1999: made transient, in case the component is used as Serializable
  private JPanel normalViewPanel;

  /**
   * Creates a new component.
   */
  public BaseHeavyApplet()
  {
    super();
  }

  /**
   * Returns Copyright information.
   * @return    The Copyright information.
   */
  public ESlateInfo getInfo()
  {
    return new ESlateInfo("E-Slate AWT Applet Skeleton (1.0)",new String[]{"(C)opyright 1999","birbilis@cti.gr"});
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
    getESlateHandle().setInfo(getInfo());

    // Ditto for calling getAppletContext(), which is invoked when laying
    // out the component; it always fails when called from the constructor.
    // Thus, we lay out the component in init().
    normalViewPanel = new JPanel(new BorderLayout());
    if (!UIManager.getLookAndFeel().getName().equals("CDE/Motif")) {
      normalViewPanel.setBackground(Color.lightGray);
    }
    normalViewPanel.setForeground(Color.green);
    normalViewPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    Container cp = this; //AWT-no-SWING//getContentPane();
//    try {
//      cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
//      } catch (AWTError e) {
//    }
//    cp.add(getESlateHandle().getMenuPanel());
//    cp.add(normalViewPanel);
    GridBagLayout layout = new GridBagLayout();
    try {
      cp.setLayout(layout);
    } catch (AWTError e) {
    }

    // Check if we are running as an applet
    boolean isApplet;
    try {
      /*AppletContext ac = */getAppletContext();
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
       getESlateHandle().setDebugStatus(Boolean.valueOf(str).booleanValue());
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
      layout.setConstraints(cp.add(getESlateHandle().getMenuPanel()), constraints);
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
    getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
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
    getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
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
    getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
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
   * @param     plug     The plug to be added to the component.
   * @exception PinExistsException      This exception is thrown when trying
   *                    to add to a component a plug that has the same name as
   *                    another plug already attached to the component at the
   *                    same level as this plug.
   * @exception IllegalArgumentException        if <code>plug</code> is null
   */
  public void addPlug(Plug plug)
    throws PlugExistsException, IllegalArgumentException
  {
    getESlateHandle().addPlug(plug);
  }

  /**
   * Removes a plug from the component.
   * @param     plug     The plug to be removed from the component.
   * @exception NoSuchPinException      This exception is thrown when trying
   *                    to remove from a component a plug that it doesn't
   *                    actually have.
   * @exception IllegalArgumentException        if <code>plug</code> is null
   */
  public void removePlug(Plug plug)
    throws NoSuchPlugException, IllegalArgumentException
  {
    getESlateHandle().removePlug(plug);
  }

  /**
   * Returns a reference to a given plug. Only top level plugs are scanned.
   * @param     plugName The name of the plug.
   * @return    A reference to the requested plug.
   */
  public Plug getPlug(String plugName)
  {
    return getESlateHandle().getPlug(plugName);
  }

  /**
   * Returns a copy of the list of all top level plugs that the component
   * contains.
   * @return    The list of plugs.
   */
  public Plug[] getPlugs()
  {
    return getESlateHandle().getPlugs();
  }

  /**
   * Returns a reference to the E-Slate connection data.
   * @return    The requested reference.
   */
  public ESlateMicroworld getESlateMicroworld()
  {
    return getESlateHandle().getESlateMicroworld();
  }

  /**
   * Returns the name of the component.
   * @return    The component's name.
   */
  public String getComponentName()
  {
   return getESlateHandle().getComponentName();
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
    getESlateHandle().setComponentName(name);
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
    getESlateHandle().setUniqueComponentName(suggestedName);
  }

  /**
   * Returns the locale under which the component is running.
   * Currently, this is a reference to the default locale.
   * @return    A reference to the locale.
   */
  public Locale getLocale()
  {
    return getESlateHandle().getLocale();
  }

  /**
   * Cleans up when the component is destroyed.
   */
  public void destroy()
  {
    // Notify state changed listeners
    getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      this, ESlateMicroworld.COMPONENT_DESTROYED);

    ESlateHandle.removeAllRecursively(normalViewPanel);
    normalViewPanel = null;
    disposeESlateHandle();
    ESlateHandle.removeAllRecursively(this);
    super.destroy();
  }

  /**
   * Specifies whether debugging messages should be printed.
   * @param     status  True if yes, false if not.
   */
  public void setDebugStatus(boolean status)
  {
    getESlateHandle().setDebugStatus(status);
  }

  /**
   * Returns whether debugging messages are printed.
   * @return    True if yes, false if not.
   */
  public boolean getDebugStatus()
  {
    return getESlateHandle().getDebugStatus();
  }

  /**
   * Returns the component's E-Slate handle.
   * @return    The requested handle.
   */
  public ESlateHandle getESlateHandle()
  {
    if (avHandle!=null) return avHandle;
    avHandle = ESlate.registerPart(this);
    return avHandle;
  }

  private void disposeESlateHandle(){
   avHandle.dispose();
   avHandle=null;
  }


  //Externalizable//
  public void readExternal(ObjectInput p0) throws IOException, ClassNotFoundException {};
  public void writeExternal(ObjectOutput p0) throws IOException {};

}
