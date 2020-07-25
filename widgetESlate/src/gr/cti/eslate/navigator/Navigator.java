package gr.cti.eslate.navigator;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.birbilis.BaseComponent;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.utils.browser.*;
import gr.cti.utils.*;

import java.awt.*;
import java.beans.*;
import java.io.*;
//import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.navigator.gui.NavigatorGUI;
import gr.cti.eslate.navigator.models.INavigator;
import gr.cti.eslate.navigator.models.INavigatorEventSource;
import gr.cti.eslate.navigator.models.INavigatorEventSink;

import gr.cti.eslate.scripting.LogoScriptable;
/**
 * Navigator (web browser etc.) component for E-Slate.
 * <P>
 * <B>Component Plugs:</B>
 * <UL>
 * <LI><B>URL.</B> This is a multiple input multiple output plug associated
 * with a UrlSO shared object. The plug's color is [188, 143, 143].
 * </LI>
 * <LI><B>URL as string.</B> This is a multiple input multiple output plug
 * associated with a StringSO shared object. The plug's color is
 * [139, 117, * 0].
 * </LI>
 * </UL>
 * <P>
 * <B>Logo primitives:</B>
 * <UL>
 * <LI><B>NAVIGATOR.SETLOCATION url</B> Instructs the navigator to visit the
 * specified URL.</LI>
 * <LI><B>NAVIGATOR.LOCATION</B> Returns the navigator's current URL.</LI>
 * <LI><B>NAVIGATOR.GOHOME</B> Instructs the navigator to visit the home
 * URL.</LI>
 * <LI>NAVIGATOR.FORWARD</B> Instructs the navigator to visit the next URL in
 * its history.</LI>
 * <LI>NAVIGATOR.BACK</B> Instructs the navigator to visit the previous URL in
 * its history.</LI>
 * <LI>NAVIGATOR.STOP</B> Instructs the navigator to stop loading the current
 * URL.</LI>
 * <LI>NAVIGATOR.REFRESH</B> Instructs the navigator to reload the current
 * URL.</LI>
 * </UL>
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.5, 23-Jan-2008
 */
public class Navigator extends BaseComponent
  implements INavigator, INavigatorEventSink, LogoScriptable,
             HistoryChangeListener, LinkFollowedListener
{
  private NavigatorThread thread = null;
  public final static String VERSION = "3.0.5";
  public final static int SAVE_VERSION = 2;

  private final static Dimension preferredSize = new Dimension(640, 400);

  /**
   * Timer which measures the time required for loading the state of the
   * component.
   */
  PerformanceTimer loadTimer;
  /**
   * Timer which measures the time required for saving the state of the
   * component.
   */
  PerformanceTimer saveTimer;
  /**
   * Timer which measures the time required for the construction of the
   * component.
   */
  PerformanceTimer constructorTimer;
  /**
   * Timer which measures the time required for initializing the E-Slate
   * aspect of the component.
   */
  PerformanceTimer initESlateAspectTimer;
  /**
   * The listener that notifies about changes to the state of the
   * Performance Manager.
   */
  PerformanceListener perfListener = null;

  // serial-version, so that new versions load OK
  static final long serialVersionUID = 10032000L;

  // LogoScriptable //
  public String[] getSupportedPrimitiveGroups() //14Mar2000
  {
    return new String[]{"gr.cti.eslate.scripting.logo.NavigatorPrimitives"};
  }

  /**
   * Returns Copyright information.
   * @return    The Copyright information.
   */
  public ESlateInfo getInfo()
  {
    return new ESlateInfo(
      Res.localize("title") + ", " + Res.localize("version") + " " + VERSION,
      Res.localizeArray("info")
    );
  }

  /////////////

  INavigator navigator;
  NavigatorGUI gui;
  Plugs plugs;
  // this is set to an appropriate value by "setHomeToDefault()" called at
  // the "Navigator" constructor
  String homeURL;

  public static final String SWTNavigator =
    "gr.cti.eslate.navigator.suppliers.SWTNavigator"; //26Jun2000
  public static final String SwingNavigator =
    "gr.cti.eslate.navigator.suppliers.SwingNavigator";
  public static final String WWNavigator =
    "gr.cti.eslate.navigator.suppliers.WWNavigator";

  public Navigator()
  {
    super();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(constructorTimer);

    setPreferredSize(Navigator.preferredSize);

    try{
      setUniqueComponentName(Res.localize("Navigator"));
    } catch(RenamingForbiddenException e){
    }

    setHomeToDefault();

    Class<?> c;

    // Under Windows, first try using Internet explorer.
    if (getNavigator() == null) {
      String os = System.getProperty("os.name").toLowerCase();
      if (os.startsWith("windows")) {
        try {
          c = Class.forName("org.eclipse.swt.browser.Browser");
        } catch (Exception e) {
          c = null;
        }
        if (c != null) {
          setNavigatorClass(SWTNavigator);
        }
      }
      if (getNavigator() != null) {
        getESlateHandle().setUsesBeanXporterBeans(true);
      }else{
        getESlateHandle().setUsesBeanXporterBeans(false);
      }
    }

    // Next, try the WebWindow browser.
    if (getNavigator() == null) {
      try {
        c = Class.forName("horst.webwindow.WebWindow");
      } catch (Exception e) {
        c = null;
      }
      if (c != null) {
        setNavigatorClass(WWNavigator);
      }
    }

/*
    // If no navigator supplier class was set, try the ICE Browser.
    if (getNavigator() == null) {
      try {
        c = Class.forName("ice.iblite.Browser");
      } catch (Exception e) {
        c = null;
      }
      if (c != null) {
        setNavigatorClass(ICENavigator); //14Jun2000
      }
    }
*/

    // If no navigator supplier class was set, try the JTextPane-based
    // browser.
    if (getNavigator() == null) {
      setNavigatorClass(SwingNavigator);
    }
  
    // ...could also use HotJava if present...

    // Don't pass the navigator field's value as the 1st param, pass "this"
    // instead, so that when changing service supplier dynamically, the plugs
    // will still work, since they will be talking to this class which
    // delegates to the "current" instantiated service supplier.
    plugs = new Plugs(this, getESlateHandle());

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }

  ///////

  //15Mar2000
  private void setupEventSink(Object source, INavigatorEventSink sink)
  {
    // check if an appropriate "source" was passed-in
    if (source != null && source instanceof INavigatorEventSource) {
      // "sink" can be null
      ((INavigatorEventSource)source).setNavigatorEventSink(sink);
    }
  }

  public INavigator getNavigator() //14Jun2000
  {
    return navigator;
  }

  public void setNavigator(INavigator nav) //14Mar2000 //"nav" can be null
  {
    String oldLocation;
    if (navigator != null) {
      oldLocation = navigator.getCurrentLocation();
    }else{
      oldLocation = null;
    }
    if (navigator != null) {
      if (navigator.getClass().getName().equals(SWTNavigator)) {
        navigator.getESlateBrowser().removeHistoryChangeListener(this);
      }
      navigator.getESlateBrowser().removeLinkFollowedListener(this);
    }
    nav.getESlateBrowser().addHistoryChangeListener(this);
    nav.getESlateBrowser().addLinkFollowedListener(this);
    // unregister as event sink from old supplier
    setupEventSink(getNavigator(), null);
    disposeNavigator();
    navigator = nav;
    setupEventSink(nav, this); //start getting events from new supplier
    // 15Mar2000: passing ourselves as INavigator for address bar etc. so that
    // we get to contol the addresses (support relative paths to the microworld
    // document base etc.)
    gui.setNavigator((Component)nav, this);
    if (oldLocation != null) {
      //try {
      //  nav.setCurrentLocation(oldLocation);
      //} catch (Exception e) {
      //}
      gotoURL(nav, oldLocation);
    }
  }

  public JComponent getContent()
  {
    if (gui == null) {
      gui = new NavigatorGUI();
      gui.enableBack(false);
      gui.enableForward(false);
    }
    return gui;
  }

  public void writeExternal(ObjectOutput oo) throws IOException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(saveTimer);

    StorageStructure map = getProperties();
    oo.writeObject(map);

    pm.stop(saveTimer);
    pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
  }

  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(loadTimer);

    StorageStructure map = (StorageStructure)(oi.readObject());
    String currentLocation = (String)(map.get(CURRENT_LOCATION_PROPERTY));
    boolean menuBarVisible = map.get(MENUBAR_VISIBLE_PROPERTY, true);
    boolean toolBarVisible = map.get(TOOLBAR_VISIBLE_PROPERTY, true);
    boolean addressBarVisible = map.get(ADDRESSBAR_VISIBLE_PROPERTY, true);
    boolean statusBarVisible = map.get(STATUSBAR_VISIBLE_PROPERTY, true);
    String home = (String)(map.get(HOME_PROPERTY));
    String navigatorClass = (String)(map.get(NAVIGATOR_CLASS_PROPERTY));
    setMinimumSize(map.get(MINIMUM_SIZE, getMinimumSize()));
    setMaximumSize(map.get(MAXIMUM_SIZE, getMaximumSize()));
    setPreferredSize(map.get(PREFERRED_SIZE, Navigator.preferredSize));

    setMenuBarVisible(menuBarVisible);
    setToolBarVisible(toolBarVisible);
    setAddressBarVisible(addressBarVisible);
    setStatusBarVisible(statusBarVisible);
    setHome(home);
    if (navigatorClass != null) {
      setNavigatorClass(navigatorClass);
    }
    if (currentLocation != null) {
      try {
        setCurrentLocation(currentLocation);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    pm.stop(loadTimer);
    pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
  }

  public void writeObject(ObjectOutputStream oo) throws IOException
  {
    writeExternal(oo);
  }

  public void readObject(ObjectInputStream oi)
    throws IOException, ClassNotFoundException
  {
    readExternal(oi);
  }

  // INavigator implementation //

  // currentLocation property
  public void setCurrentLocation(String location) throws Exception
  {
    // 15Mar2000: added support for paths relative to the current document base
    String url = Res.makeURL(getESlateHandle(), location).toString();
    //getNavigator().setCurrentLocation(url);
    gotoURL(getNavigator(), url);
    plugs.exportURL(url);
  }

  // currentLocation property
  public String getCurrentLocation()
  {
    return getNavigator().getCurrentLocation();
  }

  public void stop()
  {
    getNavigator().stop();
  }

  public void refresh()
  {
    //getNavigator().refresh();
    if (thread != null) {
      thread.halt();
    }
    thread = new NavigatorRefreshThread(getNavigator());
    SwingUtilities.invokeLater(thread);
  }

  public ESlateBrowser getESlateBrowser()
  {
    return getNavigator().getESlateBrowser();
  }

  public String home()
  {
    try{
      // Ignore the supplier's possible knowledge of a "home" setting
      // (e.g. the IE supplier uses settings from registry) and use the
      // "home" property value.
      String home = getHome();
      //getNavigator().setCurrentLocation(home);
      gotoURL(getNavigator(), home);
      plugs.exportURL(home);
    } catch(Exception e) {
      e.printStackTrace();
      return "";
    }
    return homeURL;
  }

  /**
   * Visit the next URL in the browser's history. The URL is visited in a
   * separate thread.
   */
  public String forward()
  {
    //String url = getNavigator().forward();
    Vector<?> v = getNavigator().getESlateBrowser().getForwardHistory();
    int n = v.size();
    String url;
    if (n > 0) {
      url = (String)(v.elementAt(n-1));
      if (thread != null) {
        thread.halt();
      }
      thread = new NavigatorForwardThread(getNavigator(), url);
      SwingUtilities.invokeLater(thread);
      plugs.exportURL(url);
    }else{
      url = "";
    }
    return url;
  }

  /**
   * Visit the previous URL in the browser's history. The URL is visited in a
   * separate thread.
   */
  public String back()
  {
    //String url = getNavigator().back();
    Vector<?> v = getNavigator().getESlateBrowser().getBackHistory();
    int n = v.size();
    String url;
    if (n > 0) {
      url = (String)(v.elementAt(n-1));
      if (thread != null) {
        thread.halt();
      }
      thread = new NavigatorBackThread(getNavigator(), url);
      SwingUtilities.invokeLater(thread);
      plugs.exportURL(url);
    }else{
      url = "";
    }
    return url;
  }

  // INavigatorEventSink implementation //

  private transient String lastLocation;

  // 15Mar2000
  public void beforeNavigation(String newLocation, boolean[] cancel) 
  {
    lastLocation = getCurrentLocation();
    if (gui != null) {
      gui.setAddressBarText(newLocation);
    }
    // fire "currentLocation" property change
    firePropertyChange(
      CURRENT_LOCATION_PROPERTY, getCurrentLocation(), newLocation
    );
  }

  public void navigationComplete(String newLocation)  //15Mar2000
  {
    firePropertyChange(CURRENT_LOCATION_PROPERTY, lastLocation, newLocation);
    ESlateBrowser b = navigator.getESlateBrowser();
    boolean back = b.getBackHistory().size() > 0;
    boolean forward = b.getForwardHistory().size() > 0;
    gui.enableBack(back);
    gui.enableForward(forward);
  }

  public void titleChange(String text) //14Jun2000
  {
    if (gui != null) {
      gui.setTitle(text); //???
    }
  }

  public void statusTextChange(String text) //14Jun2000
  {
    if (gui != null) {
      gui.setStatusBarText(text);
    }
  }

  
  // HistoryChangeListener implementation

  public void historyChanged(HistoryChangedEvent e)
  {
    ESlateBrowser b = (ESlateBrowser)(e.getSource());
    boolean back = b.getBackHistory().size() > 0;
    boolean forward = b.getForwardHistory().size() > 0;
    gui.enableBack(back);
    gui.enableForward(forward);
    gui.setAddressBarText(b.getCurrentLocation());
  }


  // LinkFollowedListener implementation

  public void linkFollowed(LinkFollowedEvent e)
  {
    plugs.exportURL(e.getLink());
  }

  //////// PROPERTIES ///////////////

  // menuBarVisible property //

  public void setMenuBarVisible(boolean flag)
  {
    if (gui != null) {
      gui.setMenuBarVisible(flag);
    }
  }

  public boolean isMenuBarVisible()
  {
    return (gui == null) ? false : gui.isMenuBarVisible();
  }

  // toolBarVisible property //

  public void setToolBarVisible(boolean flag)
  {
    if (gui != null) {
      gui.setToolBarVisible(flag);
    }
  }

  public boolean isToolBarVisible()
  {
    return (gui == null) ? false : gui.isToolBarVisible();
  }

  // addressBarVisible property //

  public void setAddressBarVisible(boolean flag)
  {
    if (gui != null) {
      gui.setAddressBarVisible(flag);
    }
  }

  public boolean isAddressBarVisible()
  {
    return (gui == null) ? false : gui.isAddressBarVisible();
  }

  // statusBarVisible property //

  public void setStatusBarVisible(boolean flag)
  {
    if (gui != null) {
      gui.setStatusBarVisible(flag);
    }
  }

  public boolean isStatusBarVisible()
  {
    return (gui == null) ? false : gui.isStatusBarVisible();
  }

  // navigatorClass property //

  public void setNavigatorClass(String navigatorClass) //14Jun2000
  {
    // just for safety
    if (navigatorClass == null) {
      return;
    }
    String oldNavigatorClass = getNavigatorClass();
    if (!oldNavigatorClass.equals(navigatorClass)) {
      try{
        setNavigator((INavigator)Class.forName(navigatorClass).newInstance());
      } catch(Throwable t) {
        t.printStackTrace();
        System.err.println(
          Res.localize("warning1") + navigatorClass + Res.localize("warning2")
        );
        System.err.println(
          Res.localize("warning3") + oldNavigatorClass +
          Res.localize("warning4")
        );
        // 26Jun2000: if failed to set new navigatorClass, restore the old one
        // (at start, old one would be "")
        setNavigatorClass(oldNavigatorClass);
      }
    }
    // 26Jun2000: made a bound property
    firePropertyChange(
      NAVIGATOR_CLASS_PROPERTY, oldNavigatorClass, navigatorClass
    );
  }

  public String getNavigatorClass() //14Jun2000
  {
    INavigator c = getNavigator();
    // 26Jun2000: not throwing a null-pointer-exception, but returning an
    // empty string instead if "getNavigator()" returns null
    return (c != null) ? c.getClass().getName() : "";
  }

  // Home property //

  public void setHome(String homeURL)
  {
    this.homeURL = homeURL;
    if (homeURL != null && !homeURL.equals("")) {
      gui.enableHomeButton(true);
    }else{
      gui.enableHomeButton(false);
    }
  }

  public String getHome()
  {
    return homeURL;
  }

  public void setHomeToDefault() //24Jun2000
  {
    /*
    // 16Jun2000: may have different URLs for Greek site and Enlish site
    // 24Jun2000: encapsulated the default home setting logic in a method to
    // reuse at "state loading"
    setHome(Res.localize("homeURL"));
    */

    // CTI's E-Slate site is no longer maintained, so do not use it by default.
    setHome("");
  }

  ////////////////////////////////////////////////////////////////////////////

  public static final String CURRENT_LOCATION_PROPERTY = "currentLocation";

  //14Mar2000
  public static final String MENUBAR_VISIBLE_PROPERTY = "menuBarVisible";
  //14Mar2000
  public static final String TOOLBAR_VISIBLE_PROPERTY = "toolBarVisible";
  //24Jun2000
  public static final String ADDRESSBAR_VISIBLE_PROPERTY = "addressBarVisible";
  //24Jun2000
  public static final String STATUSBAR_VISIBLE_PROPERTY = "statusBarVisible";

  //24Jun2000
  public static final String HOME_PROPERTY = "home";
  //14Jun2000 (non-storable property, want to use the best browsing-supplier
  // available)
  public static final String NAVIGATOR_CLASS_PROPERTY = "navigatorClass";

  private final static String MINIMUM_SIZE = "minimumSize";
  private final static String MAXIMUM_SIZE = "maximumSIze";
  private final static String PREFERRED_SIZE = "preferredSize";

  ////////////////////////////////////////////////////////////////////////////

  public StorageStructure getProperties()
  {
    // get properties from ancestors
    StorageStructure properties = super.getProperties();
    // just in case we have a "bad" ancestor (the BaseComponent is supposed to
    // never return null)
    if (properties == null) {
      properties=new ESlateFieldMap2(SAVE_VERSION);
    }

    properties.put(CURRENT_LOCATION_PROPERTY, getCurrentLocation());

    properties.put(MENUBAR_VISIBLE_PROPERTY, isMenuBarVisible()); //14Mar2000
    properties.put(TOOLBAR_VISIBLE_PROPERTY, isToolBarVisible()); //14Mar2000

    //24Jun2000
    properties.put(ADDRESSBAR_VISIBLE_PROPERTY, isAddressBarVisible());
    //24Jun2000
    properties.put(STATUSBAR_VISIBLE_PROPERTY, isStatusBarVisible());
    //24Jun2000
    properties.put(HOME_PROPERTY, getHome());

    properties.put(NAVIGATOR_CLASS_PROPERTY, getNavigatorClass());

    properties.put(MINIMUM_SIZE, getMinimumSize());
    properties.put(MAXIMUM_SIZE, getMaximumSize());
    properties.put(PREFERRED_SIZE, getPreferredSize());

    return properties;
  }

  public void setProperties(StorageStructure properties)
  {
    if (properties == null) {
      return;
    }
    //allow ancestors to set their properties too from the same hash
    super.setProperties(properties);

    try {
      setCurrentLocation(properties.get(CURRENT_LOCATION_PROPERTY, ""));
    } catch (Exception e) {
    }
    setMenuBarVisible(properties.get(MENUBAR_VISIBLE_PROPERTY, true));
    setToolBarVisible(properties.get(TOOLBAR_VISIBLE_PROPERTY, true));
    setAddressBarVisible(properties.get(ADDRESSBAR_VISIBLE_PROPERTY, true));
    //24Jun2000
    setStatusBarVisible(properties.get(STATUSBAR_VISIBLE_PROPERTY, true));
    //24Jun2000
    try {
      setHome((String)(properties.get(HOME_PROPERTY)));
    } catch (Exception e) {
      setHomeToDefault();
    }
  }


  //////////////

  public static void main(String[] args)
  {
    JComponentFrame.startJComponent(
      "gr.cti.eslate.navigator.Navigator", Res.localize("title"),
      new String[]{}
    );
    //start any additional applets requested by the user
    JComponentFrame.startJComponents(args, args);
  }

  /**
   * Calls any disposal methods that the hosted navigator may have.
   */
  private void disposeNavigator()
  {
    /*
    if (navigator != null) {
      Object browser = navigator.getESlateBrowser().getBrowser();
      Class c;
      try {
        c = Class.forName("org.jdesktop.jdic.browser.WebBrowser");
      } catch (Exception e) {
        c = null;
      }
      if ((c != null) && c.isInstance(browser)) {
        try {
          Method m = c.getMethod("dispose", new Class[0]);
          m.invoke(browser);
        }catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    */
  }

  /**
   * Returns the component's E-SlateHandle.
   * @return    The requested handle.
   */
  public ESlateHandle getESlateHandle()
  {
    ESlateHandle h;
    if (avHandle == null) {
      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      pm.eSlateAspectInitStarted(this);
      pm.init(initESlateAspectTimer);

      h = super.getESlateHandle();
      h.addESlateListener(new ESlateAdapter(){
        public void handleDisposed(HandleDisposalEvent e)
        {
          disposeNavigator();
          navigator = null;
          if (thread != null) {
            thread.halt();
          }
          thread = null;
          pmCleanup();
        }
      });

      pm.stop(initESlateAspectTimer);
      pm.eSlateAspectInitEnded(this);
      pm.displayTime(initESlateAspectTimer, avHandle, "", "ms");
    }else{
      h = avHandle;
    }
    return h;
  }

  /**
   * Performance manager-related cleanup.
   */
  private void pmCleanup()
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.removePerformanceListener(perfListener);
    perfListener = null;
  }

  /**
   * Starts a new thread in which a navigator visits a URL.
   * @param     navigator       The navigator.
   * @param     url             The url to visit.
   */
  private void gotoURL(INavigator navigator, String url)
  {
    if (thread != null) {
      thread.halt();
    }
    thread = new NavigatorThread(navigator, url);
    SwingUtilities.invokeLater(thread);
  }

  /**
   * This method creates and adds a PerformanceListener to the E-Slate's
   * Performance Manager. The PerformanceListener attaches the component's
   * timers when the Performance Manager becomes enabled.
   */
  private void createPerformanceManagerListener(PerformanceManager pm)
  {
    if (perfListener == null) {
      perfListener = new PerformanceAdapter() {
        public void performanceManagerStateChanged(PropertyChangeEvent e)
        {
          boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
          // When the Performance Manager is enabled, try to attach the
          // timers.
          if (enabled) {
            attachTimers();
          }
        }
      };
      pm.addPerformanceListener(perfListener);
    }
  }

  /**
   * This method creates and attaches the component's timers. The timers are
   * created only once and are assigned to global variables. If the timers
   * have been already created, they are not re-created. If the timers have
   * been already attached, they are not attached again.
   * This method does not create any timers while the PerformanceManager is
   * disabled.
   */
  protected void attachTimers()
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    boolean pmEnabled = pm.isEnabled();

    // If the performance manager is disabled, install a listener which will
    // re-invoke this method when the performance manager is enabled.
    if (!pmEnabled && (perfListener == null)) {
      createPerformanceManagerListener(pm);
    }

    // Do nothing if the PerformanceManager is disabled.
    if (!pmEnabled) {
      return;
    }

    boolean timersCreated = (loadTimer != null);
    // If the component's timers have not been constructed yet, then
    // construct them. During construction, the timers are also attached.
    if (!timersCreated) {
      // Get the performance timer group for this component.
      PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);
      // Construct and attach the component's timers.
      constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, Res.localize("ConstructorTimer"), true
      );
      loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, Res.localize("LoadTimer"), true
      );
      saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, Res.localize("SaveTimer"), true
      );
      initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, Res.localize("InitESlateAspectTimer"), true
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.CONSTRUCTOR, constructorTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.LOAD_STATE, loadTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.SAVE_STATE, saveTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
      );
    }
  }

  /**
   * This class implements a thread in which a navigator visits a URL.
   */
  private class NavigatorThread extends Thread
  {
    private String url;
    private INavigator navigator;

    /**
     * Construct the thread.
     * @param   navigator       The navigator.
     * @param   url             The URL to visit.
     */
    private NavigatorThread(INavigator navigator, String url)
    {
      this.navigator = navigator;
      this.url = url;
    }

    /**
     * Stop loading the current url, so that the thread may exit.
     */
    void halt()
    {
      navigator.stop();
    }

    /**
     * Visit the URL.
     */
    public void run()
    {
      try {
        navigator.setCurrentLocation(url);
        gui.setAddressBarText(url);
      } catch (Exception e) {
      }
    }
  }

  /**
   * This class implements a thread in which a navigator visits the previous
   * URL in its history.
   */
  private class NavigatorBackThread extends NavigatorThread
  {
    /**
     * Construct the thread.
     * @param   navigator       The navigator.
     * @param   url             The URL to visit.
     */
    private NavigatorBackThread(INavigator navigator, String url)
    {
      super(navigator, url);
    }

    /**
     * Visit the URL.
     */
    public void run()
    {
      navigator.back();
    }
  }

  /**
   * This class implements a thread in which a navigator visits the next
   * URL in its history.
   */
  private class NavigatorForwardThread extends NavigatorThread
  {
    /**
     * Construct the thread.
     * @param   navigator       The navigator.
     * @param   url             The URL to visit.
     */
    private NavigatorForwardThread(INavigator navigator, String url)
    {
      super(navigator, url);
    }

    /**
     * Visit the URL.
     */
    public void run()
    {
      navigator.forward();
    }
  }

  /**
   * This class implements a thread in which a navigator refreshes its current
   * URL.
   */
  private class NavigatorRefreshThread extends NavigatorThread
  {
    /**
     * Construct the thread.
     * @param   navigator       The navigator.
     */
    private NavigatorRefreshThread(INavigator navigator)
    {
      super(navigator, null);
    }

    /**
     * Refresh the current URL.
     */
    public void run()
    {
      navigator.refresh();
    }
  }

}
