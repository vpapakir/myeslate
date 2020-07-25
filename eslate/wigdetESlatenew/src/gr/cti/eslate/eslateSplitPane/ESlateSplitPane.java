package gr.cti.eslate.eslateSplitPane;

import java.awt.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.base.effect.*;
import gr.cti.eslate.utils.*;

/**
 * Split pane component.
 * <P>
 * <B>Component plugs:</B>
 * <P>
 * None.
 * <P>
 * <B>Logo primitives:</B>
 * None.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 */
public class ESlateSplitPane extends JSplitPane
  implements ESlatePart, Externalizable, Serializable
{
  /**
   * The component's E-Slate handle.
   */
  private ESlateHandle handle;
  /**
   * Localized resources.
   */
  static ResourceBundle resources =
    ResourceBundle.getBundle(
    "gr.cti.eslate.eslateSplitPane.SplitPaneResource", Locale.getDefault()
  );
  /**
   * Component version.
   */
  private final static String version = "2.0.3";
  /**
   * Save format version.
   */
  private final static int saveVersion = 1;
  /**
   * Add a component to the left/top side of the component.
   */
  private final static int LEFT = 0;
  /**
   * Add a component to the right/bottom side of the component.
   */
  private final static int RIGHT = 1;
  /**
   * Implements visual effects on the panel.
   */
  private EffectRunner effectRunner = null;
  /**
   * Initial size of the split pane.
   */
  private final static Dimension winSize = new Dimension(640, 240);
   
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

  // StorageStructure keys.
  private final static String MINIMUM_SIZE = "0";
  private final static String MAXIMUM_SIZE = "1";
  private final static String PREFERRED_SIZE = "2";
  private final static String LEFT_NAME = "3";
  private final static String RIGHT_NAME = "4";
  private final static String CHILDREN = "5";
  private final static String CONTINUOUS_LAYOUT = "6";
  private final static String DIVIDER_LOCATION = "7";
  private final static String DIVIDER_SIZE = "8";
  private final static String ONE_TOUCH_EXPANDABLE = "9";
  private final static String ORIENTATION = "10";
  private final static String RESIZE_WEIGHT = "11";
  private final static String LAST_DIVIDER_LOCATION = "12";
  //private final static String BORDER = "12";

  static final long serialVersionUID = 1L;

  /*
   * Returns Copyright information.
   * @return    The Copyright information.
   */
  private ESlateInfo getInfo()
  {
    String[] info = {
      resources.getString("credits1"),
      resources.getString("credits2"),
      resources.getString("credits3"),
    };
    return new ESlateInfo(
      resources.getString("componentName") + ", " +
        resources.getString("version") + " " + version,
      info);
  }

  /**
   * Create a split pane componnet component.
   */
  public ESlateSplitPane()
  {
    super(JSplitPane.HORIZONTAL_SPLIT);

    initialize();
  }

  /**
   * Sets the component to the left of (or above) the divider.
   * @param     comp    The component to display in that position.
   */
  public void setLeftComponent(Component comp)
  {
    Component oldComp = getLeftComponent();
    if ((oldComp != null) && (handle != null)) {
      handle.remove(getESlateHandle(oldComp));
    }
    super.setLeftComponent(comp);
    if ((handle != null) && (comp != null)) {
      ESlateHandle h = getESlateHandle(comp);
      handle.add(h);
      if (h.isMenuPanelCreated()) {
        ESlateUtils.removeMenuPanel(comp);
      }
    }
  }

  /**
   * Sets the component above (or to the left of) the divider.
   * @param     comp    The component to display in that position.
   */
  public void setTopComponent(Component comp)
  {
    setLeftComponent(comp);
  }

  /**
   * Sets the component to the left of (or above) the divider to be a
   * component of a given class.
   * @param     name    The name of the class of the component to the left of
   *                    (or above) the divider. If there is already a component
   *                    of this type in this position, it is left undisturbed.
   */
  public void setFirstComponentClassName(String name)
  {
    setComponentClassName(LEFT, name);
  }

  /**
   * Sets the component to the right of (or below) the divider to be a
   * component of a given class.
   * @param     name    The name of the class of the component to the right of
   *                    (or below) the divider. If there is already a component
   *                    of this type in this position, it is left undisturbed.
   */
  public void setSecondComponentClassName(String name)
  {
    setComponentClassName(RIGHT, name);
  }

  /**
   * Sets one of the two components in the pane to be a component of a given
   * class.
   * @param     where   One of <code>LEFT</code>, <code>RIGHT</code>,
   *                    specifying whether the left/top or right/bottom
   *                    component should be set.
   * @param     name    The name of the class of the component. If there is
   *                    already a component of this type in the specified
   *                    position, it is left undisturbed.
   */
  private void setComponentClassName(int where, String name)
  {
    Class<?> c;
    boolean error = false;
    if ((name == null) || name.equals("")) {
      name = null;
      c = null;
    }else{
      try {
        c = Class.forName(name);
        if (!Component.class.isAssignableFrom(c)) {
          c = null;
          error = true;
          String message =
            resources.getString("notCompo1") +
            name +
            resources.getString("notCompo2");
          ESlateOptionPane.showMessageDialog(
            this, message, resources.getString("error"),
            JOptionPane.ERROR_MESSAGE
          );
        }
      }catch (ClassNotFoundException e) {
        c = null;
        error = true;
        String message =
          resources.getString("notFound1") +
          name +
          resources.getString("notFound2");
        ESlateOptionPane.showMessageDialog(
          this, message, resources.getString("error"),
          JOptionPane.ERROR_MESSAGE
        );
      }
    }
    if (error) {
      return;
    }

    String cName;
    if (where == LEFT) {
      cName = getFirstComponentClassName();
    }else{
      cName = getSecondComponentClassName();
    }
    if (!equalObjects(name, cName)) {
      Component comp;
      if (c != null) {
        try {
          comp = (Component)(c.newInstance());
        } catch (Exception e) {
          e.printStackTrace();
          comp = null;
        }
      }else{
        comp = null;
      }
      if (where == LEFT) {
        setLeftComponent(comp);
      }else{
        setRightComponent(comp);
      }
    }
  }

  /**
   * Sets the component to the right of (or below) the divider.
   * @param     comp    The component to display in that position.
   */
  public void setRightComponent(Component comp)
  {
    Component oldComp = getRightComponent();
    if ((oldComp != null) && (handle != null)) {
      handle.remove(getESlateHandle(oldComp));
    }
    super.setRightComponent(comp);
    if ((handle != null) && (comp != null)) {
      ESlateHandle h = getESlateHandle(comp);
      handle.add(h);
      if (h.isMenuPanelCreated()) {
        ESlateUtils.removeMenuPanel(comp);
      }
    }
  }

  /**
   * Sets the component below (or to the right of) the divider.
   * @param     comp    The component to display in that position.
   */
  public void setBottomComponent(Component comp)
  {
    setRightComponent(comp);
  }

  /**
   * Check whether two objects are equal, taking <code>null</code>s into
   * account.
   * @param     o1      The first object.
   * @param     o2      The second object.
   * @return    True if the obkects are equal (or both <code>null</code>),
   *            false otherwise.
   */
  private static boolean equalObjects(Object o1, Object o2)
  {
    if (o1 == null) {
      return (o2 == null);
    }else{
      return o1.equals(o2);
    }
  }

  /**
   * Returns the name of the class of the component to the left of (or above)
   * the divider.
   * @return    The name of the requested class. If there is no component in
   *            that position, <code>null</code> is returned.
   */
  public String getFirstComponentClassName()
  {
    Component comp = getLeftComponent();
    if (comp != null) {
      return comp.getClass().getName();
    }else{
      return null;
    }
  }

  /**
   * Returns the name of the class of the component to the right of (or below)
   * the divider.
   * @return    The name of the requested class. If there is no component in
   *            that position, <code>null</code> is returned.
   */
  public String getSecondComponentClassName()
  {
    Component comp = getRightComponent();
    if (comp != null) {
      return comp.getClass().getName();
    }else{
      return null;
    }
  }

  /**
   * Initialize component and create component GUI.
   */
  private void initialize()
  {
    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);

    setPreferredSize(winSize);

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Returns the component's E-Slate handle.
   * @return    The requested handle. If the component's constructor has not
   *            been called, this method returns null.
   */
  public ESlateHandle getESlateHandle()
  {
    if (handle == null) {
      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      pm.eSlateAspectInitStarted(this);
      pm.init(initESlateAspectTimer);

      initESlate();

      pm.stop(initESlateAspectTimer);
      pm.eSlateAspectInitEnded(this);
      pm.displayTime(initESlateAspectTimer, handle, "", "ms");
    }
    return handle;
  }

  /**
   * Initializes the E-Slate functionality of the component.
   */
  private void initESlate()
  {
    handle = ESlate.registerPart(this);

    handle.setInfo(getInfo());

    try {
      handle.setUniqueComponentName(resources.getString("name"));
    } catch (RenamingForbiddenException e) {
    }

    Component comp = getLeftComponent();
    if (comp != null) {
      handle.add(getESlateHandle(comp));
    }
    comp = getRightComponent();
    if (comp != null) {
      handle.add(getESlateHandle(comp));
    }

    handle.addESlateListener( new ESlateAdapter() {
      public void handleDisposed(HandleDisposalEvent e)
      {
        dispose();
      }
    });
  }

  /**
   * Returns the E-Slate handle of an arbitrary component. This method will
   * try to get the component's handle by hook or by crook:
   * <UL>
   * <LI>If the component is an ESlatePart, it will call the component's
   * <code>getESlateHandle()</code> method.</LI>
   * <LI>If the component has a <code>getESlateHandle()</code> method, it will
   * find it and invoke it.</LI>
   * <LI>If the component is registered in the current microworld, the
   * corresponding handle will be retrieved.</LI>
   * <LI>If all else fails, the component will be registered with E-Slate, and
   * the resulting handle will be returned.</LI>
   * </UL>
   * @param     compo   The component.
   * @return    The requested handle. If the component is not an E-Slate
   *            component, this method return null.
   */
  private ESlateHandle getESlateHandle(Object compo)
  {
    // Can't do anything if the component is null...
    if (compo == null) {
      return null;
    }

    // First, do the obvious: if the component implements the ESlatePart
    // interface, simply call its getESlateHandle() method.
    if (compo instanceof ESlatePart) {
      return ((ESlatePart)(compo)).getESlateHandle();
    }

    // Then check if the component implements the getESlateHandle() method,
    // even if it doesn't declare that it implements the ESlatePart interface.
    Method m = null;
    try {
      Class<?>[] args = new Class[0];
      Class<?> cl = compo.getClass();
      m = cl.getMethod("getESlateHandle", args);
      if (m.getReturnType() != ESlateHandle.class) {
        m = null;
      }
    } catch (Exception e) {
      m = null;
    }
    if (m != null) {
      try {
        return (ESlateHandle)(m.invoke(compo, new Object[0]));
      } catch (Exception e) {
      }
    }

/*
    // Then check the default microworld, to see if the component is
    // registered there.
    ESlateMicroworld defaultMw = ESlateMicroworld.getDefaultMicroworld();
    ESlateHandle h = defaultMw.getComponentHandle(compo);
    if (h != null) {
      return h;
    }
*/

    // Then check the current microworld, to see if the component is
    // registered there.
    if (handle != null) {
      ESlateMicroworld mw = handle.getESlateMicroworld();
      // Return whatever we got: either the handle, or null.
      if (mw != null) {
        ESlateHandle h = mw.getComponentHandle(compo);
        if (h != null) {
          return h;
        }
      }
    }

    return ESlate.registerPart(compo);
  }

  /**
   * Save the component's state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(saveTimer);

    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 7);

    map.put(MINIMUM_SIZE, getMinimumSize());
    map.put(MAXIMUM_SIZE, getMaximumSize());
    map.put(PREFERRED_SIZE, getPreferredSize());
    map.put(CONTINUOUS_LAYOUT, isContinuousLayout());
    map.put(DIVIDER_LOCATION, getDividerLocation());
    map.put(LAST_DIVIDER_LOCATION, getLastDividerLocation());
    map.put(DIVIDER_SIZE, getDividerSize());
    map.put(ONE_TOUCH_EXPANDABLE, isOneTouchExpandable());
    map.put(ORIENTATION, getOrientation());
    map.put(RESIZE_WEIGHT, getResizeWeight());
    //map.put(BORDER, ESlateUtils.getBorderDescriptor(getBorder(), this));

    ArrayList<ESlateHandle> children = new ArrayList<ESlateHandle>();
    if (handle != null) {
      Component comp = getLeftComponent();
      if (comp != null) {
        ESlateHandle h = getESlateHandle(comp);
        map.put(LEFT_NAME, h.getComponentName());
        children.add(h);
      }
      comp = getRightComponent();
      if (comp != null) {
        ESlateHandle h = getESlateHandle(comp);
        map.put(RIGHT_NAME, h.getComponentName());
        children.add(h);
      }
      handle.saveChildObjects(map, CHILDREN, children);
    }

    oo.writeObject(map);

    pm.stop(saveTimer);
    pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(loadTimer);

    StorageStructure map = (StorageStructure)(oi.readObject());

    setMinimumSize(map.get(MINIMUM_SIZE, (Dimension)null));
    setMaximumSize(map.get(MAXIMUM_SIZE, (Dimension)null));
    setPreferredSize(map.get(PREFERRED_SIZE, (Dimension)null));
    setContinuousLayout(map.get(CONTINUOUS_LAYOUT, false));
    setDividerLocation(map.get(DIVIDER_LOCATION, -1));
    Integer l = (Integer)map.get(LAST_DIVIDER_LOCATION);
    if (l != null) {
      setLastDividerLocation(l.intValue());
    }
    setDividerSize(map.get(DIVIDER_SIZE, 5));
    setOneTouchExpandable(map.get(ONE_TOUCH_EXPANDABLE, false));
    setOrientation(map.get(ORIENTATION, JSplitPane.HORIZONTAL_SPLIT));
    setResizeWeight(map.get(RESIZE_WEIGHT, 0.0d));
/*
    BorderDescriptor bd = (BorderDescriptor)(map.get(BORDER));
    if (bd != null) {
      setBorder(bd.getBorder());
    }
*/

    if (handle != null) {
      String leftName = map.get(LEFT_NAME, (String)null);
      String rightName = map.get(RIGHT_NAME, (String)null);
      if ((leftName != null) || (rightName != null)) {
        handle.restoreChildObjects(map, CHILDREN);
        if (leftName != null) {
          ESlateHandle h = handle.getChildHandle(leftName);
          if (h != null) {
            setLeftComponent((Component)(h.getComponent()));
          }
          h = handle.getChildHandle(rightName);
          if (h != null) {
            setRightComponent((Component)(h.getComponent()));
          }
        }
      }
    }

    pm.stop(loadTimer);
    pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Save the component's state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeObject(ObjectOutputStream oo) throws IOException
  {
    writeExternal(oo);
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readObject(ObjectInputStream oi)
    throws IOException, ClassNotFoundException
  {
    readExternal(oi);
  }

  /**
   * Paint the component.
   * @param     g       The graphics context in which to paint the component.
   */
  public void paint(Graphics g)
  {
    if ((effectRunner != null) && effectRunner.isEffectRunning()) {
      effectRunner.getEffect().realizeEffect(g);
    }
    super.paint(g);
  }

  /**
   * Set a UI effect.
   * @param     effect  The effect to set.
   */
  public void setEffect(EffectInterface effect)
  {
    if (effectRunner == null) {
      effectRunner = new EffectRunner(this);
    }
    effectRunner.setEffect(effect);
  }

  /**
   * Retrieve the current UI effect associated with this component.
   * @return    The current UI effect associated with this component.
   */
  public EffectInterface getEffect()
  {
    if (effectRunner != null) {
      return effectRunner.getEffect();
    }else{
      return null;
    }
  }

  /**
   * Free resources. After invoking this method, the component is unusable.
   */
  public void dispose()
  {
    ESlateHandle.removeAllRecursively(this);

    effectRunner = null;

    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.removePerformanceListener(perfListener);
    perfListener = null;

    if (handle != null) {
      handle.dispose();
      handle = null;
    }
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
  private void attachTimers()
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
        compoTimerGroup, resources.getString("ConstructorTimer"), true
      );
      loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("LoadTimer"), true
      );
      saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("SaveTimer"), true
      );
      initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("InitESlateAspectTimer"), true
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
}
