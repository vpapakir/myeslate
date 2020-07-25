package gr.cti.eslate.base.container;

import java.io.*;
import java.util.*;

import gr.cti.eslate.utils.*;

/**
 * This class encapsulates the state of the PerformanceManagerGroups managed
 * by the PerformanceManager.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class PerformanceManagerState implements Externalizable
{
  String name;
  PerformanceManagerState[] children;
  boolean active;
  boolean display;
  int timeMode;
  boolean assigned = false;
  StorageStructure constructorTimers;
  StorageStructure eSlateTimers;

  private final static int saveVersion = 2;
  private final static String NAME = "name";
  private final static String CHILDREN = "children";
  private final static String DISPLAY = "display";
  private final static String ACTIVE = "active";
  private final static String TIME_MODE = "timeMode";
  private final static String CONSTRUCTOR_TIMERS = "constructorTimers";
  private final static String ESLATE_TIMERS = "eSlateTimers";

  static final long serialVersionUID = 2L;

  /**
   * Create a PerformanceManagerState instance.
   * @param     a       The list of PerformanceManagerGroups whose state will
   *                    be encapsulated by this instance.
   * @param     constructorTimers       The construction timer descriptors of
   *                    the performance manager.
   * @param     eSlateTimers    The E-Slate aspect initialization timer
   *                    descriptors of the performance manager.
   */
  @SuppressWarnings("unchecked")
  PerformanceManagerState(
    PTGBaseArray a, HashMap constructorTimers, HashMap eSlateTimers)
  {
    super();
    this.name = null;   // Not used.
    int n = a.size();
    children = new PerformanceManagerState[n];
    for (int i=0; i<n; i++) {
      PerformanceTimerGroup ptg = a.get(i);
      children[i] = new PerformanceManagerState(ptg);
    }
    display = false;    // Not used.
    active = false;     // Not used.
    this.constructorTimers = new ESlateFieldMap2(saveVersion);
    this.eSlateTimers = new ESlateFieldMap2(saveVersion);
    Iterator it = constructorTimers.values().iterator();
    while (it.hasNext()) {
      ClassTimer t = (ClassTimer)(it.next());
      this.constructorTimers.put(t.className, t.enabled);
    }
    it = eSlateTimers.values().iterator();
    while (it.hasNext()) {
      ClassTimer t = (ClassTimer)(it.next());
      this.eSlateTimers.put(t.className, t.enabled);
    }
  }

  /**
   * Create a PerformanceManagerState instance corresponding to a specific
   * PerformanceTimerGroup.
   * @param     ptg     The PerformanceTimerGroup.
   */
  private PerformanceManagerState(PerformanceTimerGroup ptg)
  {
    super();
    this.name = ptg.toString();
    int n = ptg.children.size();
    children = new PerformanceManagerState[n];
    for (int i=0; i<n; i++) {
      PerformanceTimerGroup ptg2 = ptg.children.get(i);
      children[i] = new PerformanceManagerState(ptg2);
    }
    active = ptg.active;
    display = ptg.display;
    timeMode = ptg.timeMode;
    constructorTimers = null;   // Not used.
    eSlateTimers = null;        // Not used.
  }

  /**
   * Create a PerformanceManagerState instance. Useless no-argument
   * constructor, required by the externalization mechanism.
   */
  public PerformanceManagerState()
  {
    super();
  }

  /**
   * Save the state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 5);

    map.put(NAME, name);
    map.put(CHILDREN, children);
    map.put(ACTIVE, active);
    map.put(DISPLAY, display);
    map.put(TIME_MODE, timeMode);
    map.put(CONSTRUCTOR_TIMERS, constructorTimers);
    map.put(ESLATE_TIMERS, eSlateTimers);

    oo.writeObject(map);
  }

  /**
   * Load the state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)(oi.readObject());

    name = map.get(NAME, "");
    children = (PerformanceManagerState[])(map.get(CHILDREN));
    active = map.get(ACTIVE, false);
    display = map.get(DISPLAY, active);
    // Calculate a reasonable default for timemode...
    if ((children != null) && (children.length) > 0) {
      timeMode = PerformanceTimerGroup.ACCUMULATIVE;
    }else{
      timeMode = PerformanceTimerGroup.ELAPSED;
    }
    //Integer stored = (Integer)(map.get(TIME_MODE));
    //.. then replace it with the real thing.
    timeMode = map.get(TIME_MODE, timeMode);
    constructorTimers = (StorageStructure)(map.get(CONSTRUCTOR_TIMERS));
    eSlateTimers = (StorageStructure)(map.get(ESLATE_TIMERS));
  }

  /**
   * Save the state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeObject(ObjectOutputStream oo) throws IOException
  {
    writeExternal(oo);
  }

  /**
   * Load the state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readObject(ObjectInputStream oi)
    throws IOException, ClassNotFoundException
  {
    readExternal(oi);
  }
}
