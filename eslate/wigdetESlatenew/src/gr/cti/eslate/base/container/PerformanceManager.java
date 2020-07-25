package gr.cti.eslate.base.container;

import java.beans.*;
import java.util.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.utils.*;
import gr.cti.typeArray.*;

/**
 * The PerformanceManager is the basic class of the performance framework.
 * It provides methods for the components to declare their timers and
 * organize them in hierarchies. The manager provides also the means for the
 * component to manipulate its timers, i.e., initialize, start, or stop them
 * and to report the ellapsed (real or accumulative) time for each timer.
 * <P>
 * PerformanceTimers are organized into hierarchies, so that the microworld
 * designer can enable or disable whole chains of timers, in order to watch
 * the performance of different aspects of the microworld, or of the same
 * aspect from different views. The use of PerformanceTimers makes it
 * possible to isolate performance hogs,
 * by providing the means to narrow the scope of the application (microworld)
 * which is being monitored. A PerformanceTimer may include more timers and
 * may be included in many timer groups.
 * <P>
 * There is only one instance of the PerformanceManager associated with the
 * entire E-Slate environment. This instance can be enabled or disabled. When
 * disabled, the performance framework puts very little performance
 * overhead on the operation of the microworld. The performance penalty of the
 * PerformanceManager, while it is active, is minimal, much smaller than
 * the time of the actual tasks which are being monitored.
 *
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
@SuppressWarnings("unchecked")
public class PerformanceManager
  implements ESlateListener
{
  /**
   * The id of the default global PerformanceGroup for measuring the time to
   * construct a component. All component constructors should declare a
   * PerformanceTimer in this PerformanceTimerGroup.
   */
  public static final int CONSTRUCTOR = 0;
  /**
   * The id of the default global PerformanceGroup for measuring the load time
   * of a component. Every component should declare a PerformanceTimer in
   * this PerformanceTimerGroup.
   */
  public static final int LOAD_STATE = 1;
  /**
   * The id of the default global PerformanceGroup for measuring the save time
   * of a component. Every component should declare a PerformanceTimer in
   * this PerformanceTimerGroup.
   */
  public static final int SAVE_STATE = 2;
  /**
   * The id of the default global PerformanceGroup for measuring the time
   * required to initialize the E-Slate aspect of a component. Every component
   * should declare a PerformanceTimer in this PerformanceTimerGroup.
   */
  public static final int INIT_ESLATE_ASPECT = 3;
  /**
   * The number of default global PerformanceTimerGroups.
   */
  int nDefaultGroups = 4;
  /**
   * A set of E-Slate handle to PerformanceTimerGroup mappings, one for each
   * of the default global PerformanceTimerGroups.
   */
  private MyHashMap[] globalGroupMembers;
  /**
   * The unique instance of PerformanceManager.
   */
  private static PerformanceManager pm = null;
  /**
   * The list of global PerformanceTimerGroups.
   */
  PTGBaseArray globalGroups = new PTGBaseArray();
  /**
   * A hash table containing the global PerformanceTimerGroups.
   * Used for fast access by id.
   */
  private HashMap<IntWrapper, PerformanceTimerGroup> globalHash =
    new HashMap<IntWrapper, PerformanceTimerGroup>();
  /**
   * The E-Slate microworld which is being analyzed by the performance
   * framework.
   */
  private ESlateMicroworld mw = null;
  /**
   * Used for accessing the hash table without creating new objects all the
   * time.
   */
  IntWrapper tmp = new IntWrapper(0);
  /**
   * Specifies whether the PerformanceManager is enabled.
   */
  private boolean enabled = false;
  /**
   * The name of the property associated with the enabled/disabled state of
   * the PerformanceManager.
   */
  private final static String STATE_PROPERTY = "enabled";
  /**
   * The list of PerformanceListeners that have been added to the
   * PerformanceManager.
   */
  private PLBaseArray performanceListeners = new PLBaseArray();
  /**
   * A hash table associating component E-Slate handles with their
   * PerformanceTimerGroup.
   */
  private HashMap<ESlateHandle, PerformanceTimerGroup> handleHash =
    new HashMap<ESlateHandle, PerformanceTimerGroup>();
  /**
   * A hash table associating components with their PerformanceTimerGroup.
   */
  private HashMap<Object, PerformanceTimerGroup> compoHash =
    new HashMap<Object, PerformanceTimerGroup>();
  /**
   * The global PerformanceTimerGroup associated with E-Slate.
   */
  private PerformanceTimerGroup eslateGroup;
  /**
   * The PerformanceManager's GUI.
   */
  private PerformanceManagerPanel gui;
  /**
   * Externally set state of the PerformanceTimerGroups.
   */
  private PerformanceManagerState state = null;
  /**
   * This constant specifies that when a PerformanceTimerGroup is
   * activated or activated, its sub-groups are not activated or activated.
   */
  public static final int DONT_ENABLE = PerformanceTimerGroup.DONT_ENABLE;
  /**
   * This constant specifies that when a PerformanceTimerGroup is
   * activated or activated, only its immediate children-groups are activated
   * or activated.
   */
  public static final int ENABLE_IMMEDIATE =
    PerformanceTimerGroup.ENABLE_IMMEDIATE;
  /**
   * This constant specifies that when a PerformanceTimerGroup is
   * activated or activated, all the groups in its hierarchy are activated or
   * activated as well.
   */
  public static final int ENABLE_ALL = PerformanceTimerGroup.ENABLE_ALL;
  /**
   * <code>displayTime()</code> will display the elapsed time of the timer.
   */
  static int ELAPSED = PerformanceTimerGroup.ELAPSED;
  /**
   * <code>displayTime()</code> will display the sum of its children's times.
   */
  static int ACCUMULATIVE = PerformanceTimerGroup.ACCUMULATIVE;
  /**
   * The class of the E-Slate container.
   */
  private final static String ESLATE_CONTAINER_CLASS_NAME =
      "gr.cti.eslate.base.container.ESlateContainer";
  /**
   * Parents of PerformanceTimerGroups whose removal was deferred.
   */
  private PTGBaseArray deferredParent = new PTGBaseArray();
  /**
   * PerformanceTimerGroups whose removal was deferred.
   */
  private ObjectBaseArray deferredChild = new ObjectBaseArray();
  /**
   * The timer used to measure class-specific times.
   */
  private gr.cti.eslate.utils.Timer timer = new gr.cti.eslate.utils.Timer();
  /**
   * Start times for components currently being constructed. This field is in
   * sync with <code>constructedComponents</code>. These two fields should
   * have been a hash table, but we can't do that, as a component's hashCode()
   * function will not work until the component has been initialized.
   */
  private LngBaseArray constructionTimes = new LngBaseArray();
  /**
   * Components currently being constructed. This field is in sync with
   * <code>constructionTimes</code>. These two fields should have been a hash
   * table, but we can't do that, as a component's hashCode() function will
   * not work until the component has been initialized.
   */
  private ObjectBaseArray constructedComponents = new ObjectBaseArray();
  /**
   * ClassTimer objects associated with the classes of the components in the
   * microworld and their constructors.
   */
  HashMap constructorTimers = new HashMap();
  /**
   * Start times for components whose E-Slate apsect is currently being
   * initialized.
   */
  private HashMap<Object, Long> eSlateTimes = new HashMap<Object, Long>();
  /**
   * ClassTimer objects associated with the classes of the components in the
   * microworld and the initialization of their E-Slate aspect.
   */
  HashMap eSlateTimers = new HashMap();
  /**
   * Information about PerformanceTimerGroups whose registration has been
   * deferred until after the microworld being monitored has finished loading.
   */
  DeferredPTGBaseArray dptg = new DeferredPTGBaseArray();
  /**
   * Information about deferred invocations to the various display methods.
   */
  DisplayDataBaseArray deferredDisplay = new DisplayDataBaseArray();

  /**
   * Localized resources.
   */
  private static ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.base.container.PerformanceResource", Locale.getDefault()
  );

  /**
   * The constructor is private; there is only one instance of
   * PerformanceManager, obtainable by invoking the getPerformanceManager
   * method.
   */
  private PerformanceManager()
  {
    super();
    globalGroupMembers = new MyHashMap[nDefaultGroups];
    for (int i=0; i<nDefaultGroups; i++) {
      globalGroupMembers[i] = new MyHashMap();
    }
  }

  /**
   * Returns the shared instance of the PerformanceManager for the whole
   * environment. The performance manager is created in the disabled state,
   * and must be enabled the first time it is used.
   */
  public static PerformanceManager getPerformanceManager()
  {
    if (pm == null) {
      pm = new PerformanceManager();
    }
    return pm;
  }

  /**
   * Returns the PerformanceTimerGroup at the specified path.
   * @return    The PerformaceTimerGroup, if the PerformanceManager is
   *            enabled, otherwise <code>null</code>. If the path is
   *            <code>null</code> or invalid, <code>null</code> is returned.
   */
  PerformanceTimerGroup getPerformanceTimerGroup(IntBaseArray path)
  {
    if (enabled) {
      if (path != null && (path.size() > 0)) {
        tmp.n = path.get(0);
        PerformanceTimerGroup ptg = globalHash.get(tmp);
        if (ptg != null) {
          IntBaseArray localPath = (IntBaseArray)(path.clone());
          localPath.remove(0);
          return ptg.getChild(localPath);
        }
      }
    }
    return null;
  }

  /**
   * Returns the PerformanceTimerGroups under group <code>p</code>, with the
   * specified name.
   * <EM>CAUTION:</EM> the performance framework does not guarantee unique
   * names among the childen of a PerformanceTimerGroup, so this method
   * may return more than one PerformanceTimerGroup.
   * @param     ptg     The PerformanceTimerGroup whose children having the
   *                    specified name will be returned.
   * @param     name    The name of the PerformanceTimerGroups.
   * @return    An array containing the PerformaceTimerGroups, if the
   *            PerformanceManager is enabled, otherwise <code>null</code>.
   *            If <code>ptg</code> is <code>null<code> or invalid,
   *            <code>null</code> is returned.
   */
  PerformanceTimerGroup[] getPerformanceTimerGroup(
    PerformanceTimerGroup ptg, String name)
  {
    if (enabled && (ptg != null) && (name != null)) {
      return ptg.getChild(name);
    }else{
      return null;
    }
  }

  /**
   * Returns the PerformanceTimerGroup under group <code>ptg</code>, with the
   * specified internal code.
   * @param     ptg     The PerformanceTimerGroup whose child is returned.
   * @param     index   The index of the child PerformanceTimerGroup.
   * @return    The PerformaceTimerGroup, if the PerformanceManager is
   *            enabled, otherwise <code>null</code>. If <code>ptg</code> is
   *            <code>null<code>, the method returns <code>null</code>.
   * @exception ArrayIndexOutOfBoundsException  Thrown if <code>index</code>
   *                                            is invalid.
   */
  PerformanceTimerGroup getPerformanceTimerGroup(
    PerformanceTimerGroup ptg, int index) throws ArrayIndexOutOfBoundsException
  {
    if (enabled && (ptg != null)) {
      return ptg.getChild(index);
    }else{
      return null;
    }
  }

  /**
   * Returns the global (i.e., first-level) PerformanceTimerGroups having the
   * specified name.
   * <EM>CAUTION:</EM> the performance framework does not guarantee unique
   * names among the childen of a PerformanceTimerGroup, so this method
   * may return more than one PerformanceTimerGroup.
   * @param     name    The name of the global PerformanceTimerGroups.
   * @return    An array containing the PerformaceTimerGroups, if the
   *            PerformanceManager is enabled, otherwise <code>null</code>.
   *            If no global PerformanceTimerGroups with this name exist,
   *            an empty array is returned.
   */
  public PerformanceTimerGroup[] getGlobalPerformanceTimerGroup(String name)
  {
    if (enabled) {
      int n;
      PTGBaseArray gl = new PTGBaseArray();
      synchronized (this) {
        n = globalGroups.size();
        for (int i=0; i<n; i++) {
          PerformanceTimerGroup ptg = globalGroups.get(i);
          if (((name == null) && (ptg.name == null)) ||
              ((name != null) && name.equals(ptg.name))) {
            gl.add(ptg);
          }
        }
      }
      return gl.toArray();
    }else{
      return null;
    }
  }

  /**
   * Creates a new PerformanceTimerGroup and adds it to the hierarchy.
   * The new group can either be a PerformanceTimer or a PerformanceTimerGroup.
   * It becomes a child of the group <code>ptg</code>. The framework does not
   * enforce unique names for the children of a PerformanceTimerGroup, so the
   * insertion of the new group in the hierarchy will succeed, even if the
   * supplied <code>name</code> is already in use by another child of
   * <code>ptg</code>.
   * @param     ptg     The PerformanceTimerGroup to which the new group will
   *                    be added.
   * @param     name    The name of the new PerformanceTimerGroup.
   * @param     isTimer Determines whether a PerformanceTimerGroup or a
   *                    PerformanceTimer will be created. <code>true</code>
   *                    for PerformanceTimer, <code>false</code> for
   *                    PerformanceTimerGroup.
   * @return    The new PerformanceTimerGroup. Returns <code>null</code>,
   *            if <code>ptg</code> is i<code>null</code>, or if the
   *            PerformanceManager is disabled.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   */
  public PerformanceTimerGroup createPerformanceTimerGroup(
    PerformanceTimerGroup ptg, String name, boolean isTimer)
    throws NullPointerException
  {
    if (enabled && (ptg != null)) {
      if (name == null) {
        throw new NullPointerException(resources.getString("nullName"));
      }
      PerformanceTimerGroup newPTG;
      if (isTimer) {
        newPTG = new PerformanceTimer(name);
      }else{
        newPTG = new PerformanceTimerGroup(name);
      }
      try {
        ptg.add(newPTG);
      } catch (CycleException ce) {
        // Can't happen.
      }
      if ((mw != null) && mw.isLoading()) {
        newPTG.active = true;
        newPTG.deferred = true;
      }else{
        applyStateRecursively(/*newPTG*/);
      }
      addGroupToGUI(ptg, newPTG);
      fireEvent(
        PerformanceTimerGroupEvent.PERFORMANCE_GROUP_ADDED, ptg, newPTG
      );
      return newPTG;
    }else{
      return null;
    }
  }

  /**
   * Creates a new PerformanceTimerGroup and adds it to the hierarchy.
   * The new group can either be a PerformanceTimer or a PerformanceTimerGroup.
   * It becomes a child of the group <code>ptg</code>. The framework does not
   * enforce unique names for the children of a PerformanceTimerGroup, so the
   * insertion of the new group in the hierarchy will succeed, even if the
   * supplied <code>name</code> is already in use by another child of
   * <code>ptg</code>.
   * @param     ptg     The PerformanceTimerGroup to which the new group will
   *                    be added.
   * @param     id      The internal id of the PerformanceTimerGroup.
   * @param     name    The name of the new PerformanceTimerGroup.
   * @param     isTimer Determines whether a PerformanceTimerGroup or a
   *                    PerformanceTimer will be created. <code>true</code>
   *                    for PerformanceTimer, <code>false</code> for
   *                    PerformanceTimerGroup.
   * @return    The new PerformanceTimerGroup. Returns <code>null</code>,
   *            if <code>ptg</code> is i<code>null</code>, or if the
   *            PerformanceManager is disabled.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if <code>id</code>
   *                    is not unique within the whole graph.
   */
  PerformanceTimerGroup createPerformanceTimerGroup(
    PerformanceTimerGroup ptg, int id, String name, boolean isTimer)
    throws NullPointerException
  {
    if (enabled && (ptg != null)) {
      if (name == null) {
        throw new NullPointerException(resources.getString("nullName"));
      }
      PerformanceTimerGroup newPTG;
      if (isTimer) {
        newPTG = new PerformanceTimer(id, name);
      }else{
        newPTG = new PerformanceTimerGroup(id, name);
      }
      try {
        ptg.add(newPTG);
      } catch (CycleException ce) {
        // Can't happen.
      }
      if ((mw != null) && mw.isLoading()) {
        newPTG.active = true;
        newPTG.deferred = true;
      }else{
        applyStateRecursively(/*newPTG*/);
      }
      addGroupToGUI(ptg, newPTG);
      fireEvent(
        PerformanceTimerGroupEvent.PERFORMANCE_GROUP_ADDED, ptg, newPTG
      );
      return newPTG;
    }else{
      return null;
    }
  }

  /**
   * Creates a new global (i.e., top-level) PerformanceTimerGroup. The new
   * group can either be a PerformanceTimer or a PerformanceTimerGroup. The
   * framework does not enforce unique names for the children of a
   * PerformanceTimerGroup, so the new group will be created, no matter if
   * there already exists another global group with the same name.
   * @param     name    The name of the new PerformanceTimerGroup.
   * @param     isTimer Determines whether a PerformanceTimerGroup or a
   *                    PerformanceTimer will be created: <code>true</code>
   *                    for PerformanceTimer, <i>false</i> for
   *                    PerformanceTimerGroup.
   * @return    The new PerformanceTimerGroup. Returns <code>null</code>, if
   *            the PerformanceManager is disabled.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   */
  public PerformanceTimerGroup createGlobalPerformanceTimerGroup(
    String name, boolean isTimer) throws NullPointerException
  {
    if (enabled) {
      if (name == null) {
        throw new NullPointerException(resources.getString("nullName"));
      }
      PerformanceTimerGroup newPTG;
      if (isTimer) {
        newPTG = new PerformanceTimer(name);
      }else{
        newPTG = new PerformanceTimerGroup(name);
      }
      synchronized (this) {
        globalGroups.add(newPTG);
        IntWrapper t = new IntWrapper(newPTG.id);
        globalHash.put(t, newPTG);
      }
      if ((mw != null) && mw.isLoading()) {
        newPTG.active = true;
        newPTG.deferred = true;
      }else{
        //applyState(newPTG);
        applyStateRecursively();
      }
      addGlobalGroupToGUI(newPTG);
      fireEvent(
        PerformanceTimerGroupEvent.PERFORMANCE_GROUP_ADDED, null, newPTG
      );
      return newPTG;
    }else{
      return null;
    }
  }

  /**
   * Creates a new global (i.e., top-level) PerformanceTimerGroup. The new
   * group can either be a PerformanceTimer or a PerformanceTimerGroup. The
   * framework does not enforce unique names for the children of a
   * PerformanceTimerGroup, so the new group will be created, no matter if
   * there already exists another global group with the same name.
   * @param     id      The internal id of the PerformanceTimerGroup.
   * @param     name    The name of the new PerformanceTimerGroup.
   * @param     isTimer Determines whether a PerformanceTimerGroup or a
   *                    PerformanceTimer will be created: <code>true</code>
   *                    for PerformanceTimer, <i>false</i> for
   *                    PerformanceTimerGroup.
   * @return    The new PerformanceTimerGroup. Returns <code>null</code>, if
   *            the PerformanceManager is disabled.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if <code>id</code>
   *                    is not unique within the whole graph.
   */
  PerformanceTimerGroup createGlobalPerformanceTimerGroup(
    int id, String name, boolean isTimer) throws NullPointerException
  {
    if (enabled) {
      if (name == null) {
        throw new NullPointerException(resources.getString("nullName"));
      }
      PerformanceTimerGroup newPTG;
      if (isTimer) {
        newPTG = new PerformanceTimer(id, name);
      }else{
        newPTG = new PerformanceTimerGroup(id, name);
      }
      synchronized (this) {
        globalGroups.add(newPTG);
        IntWrapper t = new IntWrapper(newPTG.id);
        globalHash.put(t, newPTG);
      }
      //applyState(newPTG);
      applyStateRecursively();
      addGlobalGroupToGUI(newPTG);
      fireEvent(
        PerformanceTimerGroupEvent.PERFORMANCE_GROUP_ADDED, null, newPTG
      );
      return newPTG;
    }else{
      return null;
    }
  }

  /**
   * Removes a global (top-level) PerformanceTimerGroup.
   * If any the PerformanceTimerGroup is <code>null</code>, or it is not a
   * global PerformanceTimerGroup, the method returns <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup to be removed.
   * @return    The removed PerformanceTimerGroup. Returns <code>null</code>,
   *            when the PerformanceManager is disabled or the
   *            PerformanceTimerGroup is <code>null</code>, or the
   *            PerformanceTimerGroup is not a global PerformanceTimerGroup.
   */
  public PerformanceTimerGroup removeGlobalPerformanceTimerGroup(
    PerformanceTimerGroup ptg)
  {
    if (enabled && (ptg != null)) {
      tmp.n = ptg.id;
      if (globalHash.get(tmp) != null) {
        synchronized (this) {
          int n = globalGroups.size();
          int id = ptg.id;
          for (int i=0; i<n; i++) {
            if (globalGroups.get(i).id == id) {
              globalGroups.remove(i);
              break;
            }
          }
          globalHash.remove(tmp);
          removeGlobalGroupFromGUI(ptg);
          fireEvent(
            PerformanceTimerGroupEvent.PERFORMANCE_GROUP_REMOVED, null, ptg
          );
          return ptg;
        }
      }
    }
    return null;
  }

  /**
   * Makes the second PerformanceTimerGroup a child of the
   * <code>target</code> PerformanceTimerGroup. The child is not removed from
   * any of its previous parents.
   * This method does nothing if any of the groups is <code>null</code>.
   * @param     target  The new parent PerformanceTimerGroup.
   * @param     child   The PerformanceTimerGroup to make a child of
   *                    <code>target</code>.
   * @exception CycleException  Thrown if the PerformanceTimerGroup hierarchy
   *                    that starts from <code>child</code>, already contains
   *                    <code>target</code> at any depth.
   */
  public void addPerformanceTimerGroup(
    PerformanceTimerGroup target, PerformanceTimerGroup child)
    throws CycleException
  {
    if (enabled && (target != null) && (child != null)) {
      tmp.n = target.id;
      synchronized (this) {
        tmp.n = child.id;
        PerformanceTimerGroup ptg = globalHash.get(tmp);
        if (ptg != null) {
          int n = globalGroups.size();
          int id = ptg.id;
          for (int i=0; i<n; i++) {
            PerformanceTimerGroup group = globalGroups.get(i);
            if (group.id == id) {
              globalGroups.remove(i);
              break;
            }
          }
          removeGlobalGroupFromGUI(ptg);
          globalHash.remove(tmp);
          fireEvent(
            PerformanceTimerGroupEvent.PERFORMANCE_GROUP_REMOVED, null, ptg
          );
        }
        target.add(child);
        addGroupToGUI(target, child);
        applyStateRecursively(/*child*/);
        fireEvent(
          PerformanceTimerGroupEvent.PERFORMANCE_GROUP_ADDED, ptg, child
        );
      }
    }
  }

  /**
   * Removes a PerformanceTimerGroup from all its parents and
   * makes it global (top-level). This method does nothing if
   * <code>ptg</code> is <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup to become global.
   */
  public void addGlobalPerformanceTimerGroup(PerformanceTimerGroup ptg)
  {
    if (enabled && (ptg != null)) {
      synchronized (this) {
        PTGBaseArray p = (PTGBaseArray)(ptg.parents.clone());
        int nParents = p.size();
        for (int i=0; i<nParents; i++) {
          PerformanceTimerGroup parent = p.get(i);
          parent.removeChild(ptg);
          fireEvent(
            PerformanceTimerGroupEvent.PERFORMANCE_GROUP_REMOVED, parent, ptg
          );
        }
        globalGroups.add(ptg);
        IntWrapper t = new IntWrapper(ptg.id);
        globalHash.put(t, ptg);
        updateGUI();
        fireEvent(
          PerformanceTimerGroupEvent.PERFORMANCE_GROUP_ADDED, null, ptg
        );
      }
    }
  }

  /**
   * Removes from PerformanceTimerGroup <code>parent</code> the child group
   * <code>child</code>. If any of the groups is <code>null</code>, the method
   * returns <code>null</code>.
   * @param     parent  The parent PerformanceTimerGroup.
   * @param     child   The PerformanceTimerGroup to be removed.
   * @return    The removed PerformanceTimerGroup. Returns <code>null</code>,
   *            when the PerformanceManager is disabled or any of the groups
   *            is <code>null</code> or no direct parent-child relationship
   *            between the two PerformanceTimerGroups exists.
   */
  public PerformanceTimerGroup removePerformanceTimerGroup(
    PerformanceTimerGroup parent, PerformanceTimerGroup child)
  {
    if (enabled && (parent != null) && (child != null)) {
      parent.removeChild(child);
      removeGroupFromGUI(parent, child);
      fireEvent(
        PerformanceTimerGroupEvent.PERFORMANCE_GROUP_REMOVED, parent, child
      );
      return child;
    }else{
      if ((parent != null) && (child != null)) {
        deferredParent.add(parent);
        deferredChild.add(child);
      }
      return null;
    }
  }

  /**
   * Removes from PerformanceTimerGroup <code>ptg</code> the child groups
   * with the supplied <code>name</code>.
   * <EM>CAUTION:</EM> the performance framework does not guarantee unique
   * names among the childen of a PerformanceTimerGroup, so this method
   * may remove more than one PerformanceTimerGroup. If <code>ptg</code> is
   * <code>null</code>, the method returns <code>null</code>.
   * @param     parent  The parent PerformanceTimerGroup.
   * @param     name    The name of the PerformanceTimerGroup to be removed.
   * @return    An array containing the removed PerformanceTimerGroups.
   *            Returns <code>null</code> if the PerformanceManager is
   *            disabled, or <code>parent</code> is <code>null</code>.
   *            Returns an empty array if there is no child with this name.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   */
  PerformanceTimerGroup[] removePerformanceTimerGroup(
    PerformanceTimerGroup parent, String name) throws NullPointerException
  {
    if (enabled && (parent != null)) {
      if (name == null) {
        throw new NullPointerException(resources.getString("nullName"));
      }
      PerformanceTimerGroup[] p = parent.getChild(name);
      int nChildren = p.length;
      for (int i=0; i<nChildren; i++) {
        parent.removeChild(p[i]);
        removeGroupFromGUI(parent, p[i]);
        fireEvent(
          PerformanceTimerGroupEvent.PERFORMANCE_GROUP_REMOVED, parent, p[i]
        );
      }
      updateGUI();
      return p;
    }else{
      deferredParent.add(parent);
      deferredChild.add(name);
      return null;
    }
  }

  /**
   * Notify all registered PerformanceListeners of changes in the
   * PerformanceTimerGroup hierarchy.
   * @param     what    One of
   *    <code>PerformanceTimerGroupEvent.PERFORMANCE_GROUP_ADDED</code>,
   *    <code>PerformanceTimerGroupEvent.PERFORMANCE_GROUP_REMOVED</code>.
   * @param     parent  The PerformanceTimerGroup to which another
   *                    PerformanceTimerGroup was added, or from which another
   *                    PerformanceTimerGroup was removed. If
   *                    <code>parent<code> is null, then this signifies
   *                    that the change was in a global (top-level)
   *                    PerformanceTimerGroup.
   * @param     child   The PerformanceTimerGroup that was added to or removed
   *                    from <code>parent</code>.
   */
  void fireEvent(
    int what, PerformanceTimerGroup parent, PerformanceTimerGroup child)
  {
    PerformanceTimerGroupEvent ptge =
      new PerformanceTimerGroupEvent(this, what, parent, child);
    synchronized (performanceListeners) {
      int n = performanceListeners.size();
      for (int i=0; i<n; i++) {
        switch (what) {
          case PerformanceTimerGroupEvent.PERFORMANCE_GROUP_ADDED:
            performanceListeners.get(i).performanceTimerGroupAdded(ptge);
            break;
          case PerformanceTimerGroupEvent.PERFORMANCE_GROUP_REMOVED:
            performanceListeners.get(i).performanceTimerGroupRemoved(ptge);
            break;
        }
      }
    }
  }

  /**
   * Places a PerformanceTimerGroup, associated with a component, in the
   * appropriate place in the hierarchy of one of the default global
   * PerformanceTimerGroups. You can use
   * <code>unregisterPerformanceTimerGroup</code> to undo the effects of this
   * method, without having to keep track of the PerformanceTimerGroup's
   * parent.
   * @param     id      One of CONSTRUCTOR, LOAD_STATE, SAVE_STATE,
   *                    INIT_ESLATE_ASPECT.
   * @param     ptg     The PerformanceTimerGroup.
   * @param     h       The E-Slate handle of the component associated with
   *                    the PerformanceTimerGroup. The PerformanceTimerGroup
   *                    will be made a child of the PerformanceTimerGroup
   *                    corresponding to the PerformanceTimerGroup of
   *                    <code>h</code>'s parent in the hierarchy of the
   *                    default global performance timer group specified by
   *                    <code>id</code>. If there is no such group, the
   *                    PerformanceTimerGroup will be made a direct child of
   *                    the specified default PerformanceTimerGroup.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>id</code> is
   *                    not one of CONSTRUCTOR, LOAD_STATE, SAVE_STATE,
   *                    INIT_ESLATE_ASPECT.
   * @exception IllegalArgumentException        Thrown if there is already a
   *                    PerformanceTimerGroup associated with the given handle
   *                    under the specified default global
   *                    PerformanceTimerGroup.
   */
  public void registerPerformanceTimerGroup(
    int id, PerformanceTimerGroup ptg, ESlateHandle h)
    throws ArrayIndexOutOfBoundsException, IllegalArgumentException
  {
    registerPerformanceTimerGroup(id, ptg, h, false);
  }

  /**
   * Places a PerformanceTimerGroup, associated with a component, in the
   * appropriate place in the hierarchy of one of the default global
   * PerformanceTimerGroups. You can use
   * <code>unregisterPerformanceTimerGroup</code> to undo the effects of this
   * method, without having to keep track of the PerformanceTimerGroup's
   * parent.
   * @param     id      One of CONSTRUCTOR, LOAD_STATE, SAVE_STATE,
   *                    INIT_ESLATE_ASPECT.
   * @param     ptg     The PerformanceTimerGroup.
   * @param     h       The E-Slate handle of the component associated with
   *                    the PerformanceTimerGroup. The PerformanceTimerGroup
   *                    will be made a child of the PerformanceTimerGroup
   *                    corresponding to the PerformanceTimerGroup of
   *                    <code>h</code>'s parent in the hierarchy of the
   *                    default global performance timer group specified by
   *                    <code>id</code>. If there is no such group, the
   *                    PerformanceTimerGroup will be made a direct child of
   *                    the specified default PerformanceTimerGroup.
   * @param     ignoreOrphans   When false, PerformanceTimerGroups
   *                    corresponding to components whose parents do not have
   *                    a corresponding PerformanceTimerGroup (perhaps because
   *                    it has not been created yet) are ignored. When true,
   *                    such PerformanceTimerGroups are placed directly under
   *                    the global group corresponding to <code>id</code>.
   * @return    True if the PerformanceTimerGroup was registered, false if it
   *            wasn't.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>id</code> is
   *                    not one of CONSTRUCTOR, LOAD_STATE, SAVE_STATE,
   *                    INIT_ESLATE_ASPECT.
   * @exception IllegalArgumentException        Thrown if there is already a
   *                    PerformanceTimerGroup associated with the given handle
   *                    under the specified default global
   *                    PerformanceTimerGroup.
   */
  private boolean registerPerformanceTimerGroup(
    int id, PerformanceTimerGroup ptg, ESlateHandle h, boolean ignoreOrphans)
    throws ArrayIndexOutOfBoundsException, IllegalArgumentException
  {
    if (enabled && (ptg != null) && (h != null)) {
      MyHashMap map = globalGroupMembers[id];
      if (map.get(h) != null) {
        // There is already a PerformanceTimerGroup associated with the given
        // handle under the specified default global PerformanceTimerGroup.
        throw new IllegalArgumentException(resources.getString("exists"));
      }
      PerformanceTimerGroup parentPtg;
      // Don't place timers referring to constructors or E-Slate
      // initializaton in the hierarchy, as this doesn't make much sense.
      if ((id != CONSTRUCTOR) && (id != INIT_ESLATE_ASPECT)) {
        ESlateHandle parentHandle = h.getParentHandle();
        parentPtg = map.get(parentHandle);
        if ((parentPtg == null) && ignoreOrphans) {
          if ((mw != null) && !mw.getESlateHandle().equals(parentHandle)) {
            return false;
          }
        }
      }else{
        parentPtg = null;
      }
      if (parentPtg == null) {
        parentPtg = globalGroups.get(id);
      }
      try {
        addPerformanceTimerGroup(parentPtg, ptg);
      } catch (CycleException ce) {
        // Can't happen.
      }
      map.put(h, ptg);
    }
    return true;
  }

  /**
   * Places a PerformanceTimerGroup, associated with a component, in the
   * appropriate place in the hierarchy of one of the default global
   * PerformanceTimerGroups. This method will work even
   * when the E-Slate handle of the component associated with the
   * PerformanceTimerGroup is not yet available. When the microworld being
   * monitored is being loaded, this method will place the given
   * PerformanceTimerGroup in a list, and the performance manager will
   * consider the PerformanceTimerGroup to be enabled, so that measurements
   * are taken. Invocations to the various <code>display<code> methods will be
   * deferred until the loading of the microworld is completed. When the
   * loading of the microworld is completed, the performance manager will
   * invoke <code>registerPerformanceTimerGroup</code> using the, now existing,
   * E-Slate handle of the component, so that the registerPerformanceTimerGroup
   * is placed in the appropriate position in the registerPerformanceTimerGroup
   * hierarchy and its state is restored. Finally, the deferred calls to the
   * various <code>display<code> methods will be made for those
   * PerformanceTimerGroups that are display enabled.
   * <P>
   * When the microworld being monitored is not being loaded, this method
   * behaves like a slow version of <code>registerPerformanceTimerGroup</code>.
   * @param     id      One of CONSTRUCTOR, LOAD_STATE, SAVE_STATE,
   *                    INIT_ESLATE_ASPECT.
   * @param     ptg     The PerformanceTimerGroup.
   * @param     comp    The component associated with
   *                    the PerformanceTimerGroup. The PerformanceTimerGroup
   *                    will be made a child of the PerformanceTimerGroup
   *                    corresponding to the PerformanceTimerGroup of
   *                    <code>h</code>'s parent in the hierarchy of the
   *                    default global performance timer group specified by
   *                    <code>id</code>. If there is no such group, the
   *                    PerformanceTimerGroup will be made a direct child of
   *                    the specified default PerformanceTimerGroup.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>id</code> is
   *                    not one of CONSTRUCTOR, LOAD_STATE, SAVE_STATE,
   *                    INIT_ESLATE_ASPECT.
   * @exception IllegalArgumentException        Thrown if there is already a
   *                    PerformanceTimerGroup associated with the given handle
   *                    under the specified default global
   *                    PerformanceTimerGroup.
   */
  public void registerPerformanceTimerGroup(
    int id, PerformanceTimerGroup ptg, Object comp)
    throws ArrayIndexOutOfBoundsException, IllegalArgumentException
  {
    if (enabled && (ptg != null) && (comp != null)) {
      // Defer registration if the microworld being monitored is loading, or
      // if the component has not been registered with E-Slate yet.
      ESlateHandle h;
      boolean loading;
      if (mw != null) {
        h = mw.getComponentHandle(comp);
        loading = mw.isLoading();
      }else{
        h = null;
        loading = false;
      }
      if ((h == null) || loading) {
        if ((id < 0) || (id >= nDefaultGroups)) {
          throw new ArrayIndexOutOfBoundsException(
            resources.getString("badGroupID")
          );
        }
        DeferredPTG d = new DeferredPTG(ptg, id, comp);
        dptg.add(d);
        ptg.active = true;
        ptg.deferred = true;
      }else{
        registerPerformanceTimerGroup(id, ptg, h);
      }
    }
  }

  /**
   * Removes the PerformanceTimerGroup, associated with a component, from
   * the hierarchy of one of the default global PerformanceTimerGroups.
   * You can use this method to undo the effects of
   * <code>registerPerformanceTimerGroup</code>, without having to keep track
   * of the PerformanceTimerGroup's parent.
   * @param     id      One of CONSTRUCTOR, LOAD_STATE, SAVE_STATE,
   *                    INIT_ESLATE_ASPECT.
   * @param     handle  The E-Slate handle of the component associated with
   *                    the PerformanceTimerGroup. The PerformanceTimerGroup
   *                    will be removed from the PerformanceTimerGroup
   *                    corresponding to the PerformanceTimerGroup of
   *                    <code>h</code>'s parent in the hierarchy of the
   *                    default global performance timer group specified by
   *                    <code>id</code>. If there is no such group, the
   *                    PerformanceTimerGroup will be removed from
   *                    the specified default PerformanceTimerGroup.
   * @param     parentHandle    The E-Slate handle of the component associated
   *                    with the parent of <code>ptg</code>. This has to be
   *                    supplied, as <code>parentHandle</code> is no longer
   *                    the parent of <code>h</code> when
   *                    <code>unregisterPerformanceTimerGroup</code> is
   *                    invoked.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>id</code> is
   *                    not one of CONSTRUCTOR, LOAD_STATE, SAVE_STATE,
   *                    INIT_ESLATE_ASPECT.
   * @exception IllegalArgumentException        Thrown if there is already a
   *                    PerformanceTimerGroup associated with the given handle
   *                    under the specified default global
   *                    PerformanceTimerGroup.
   */
  private void unregisterPerformanceTimerGroup(
    int id, ESlateHandle handle, ESlateHandle parentHandle)
    throws ArrayIndexOutOfBoundsException, IllegalArgumentException
  {
    if (enabled && (handle != null)) {
      if (handle.getComponent().getClass().getName().equals("eslate.ete.controlBar.MyButton")) {
      }
      MyHashMap map = globalGroupMembers[id];
      PerformanceTimerGroup ptg = map.get(handle);
      if (ptg != null) {
        PerformanceTimerGroup parentPtg;
        if ((id != CONSTRUCTOR) && (id != INIT_ESLATE_ASPECT)) {
          parentPtg = map.get(parentHandle);
        }else{
          parentPtg = null;
        }
        if (parentPtg == null) {
          parentPtg = globalGroups.get(id);
        }
        if (parentPtg.children.contains(ptg)) {
          removePerformanceTimerGroup(parentPtg, ptg);
        }
        map.remove(handle);
      }
    }
  }

  /**
   * Returns all the global (top-level) PerformanceTimerGroups.
   * @return    An array containing the global PerformanceTimerGroups.
   *            Returns <code>null</code> if the PerformanceManager is
   *            disabled.
   */
  PerformanceTimerGroup[] getGlobalPerformanceTimerGroups()
  {
    if (enabled) {
      synchronized (this) {
        return globalGroups.toArray();
      }
    }else{
      return null;
    }
  }

  /**
   * Returns the number of the global (top-level) PerformanceTimerGroups.
   * @return    The number of global PerformanceTimerGroups.
   *            Returns 0 if the PerformanceManager is disabled.
   */
  int getGlobalPerformanceTimerGroupCount()
  {
    if (enabled) {
      synchronized (this) {
        return globalGroups.size();
      }
    }else{
      return 0;
    }
  }

  /**
   * Returns the global (top-level) PerformanceTimerGroup at the specified
   * index.
   * @param     index   The index of the requested PerformanceTimerGroup.
   * @return    The PerformanceTimerGroup at the given index. Returns
   *            <code>null</code> when the PerformanceManager is disabled.
   * @exception ArrayIndexOutOfBoundsException  Thrown if <code>index</code>
   *                                            is invalid.
   */
  PerformanceTimerGroup getGlobalPerformanceTimerGroup(int index)
    throws ArrayIndexOutOfBoundsException
  {
    if (enabled) {
      return globalGroups.get(index);
    }else{
      return null;
    }
  }

  /**
   * Removes all the roots of the PerformanceTimerGroup hierarchy, apart from
   * the group belonging to the E-Slate container.
   * This method does nothing when the PerformanceManager is disabled.
   */
  void clearGlobalPerformanceTimerGroups()
  {
    if (enabled) {
      PerformanceTimerGroup esg = eslateGroup;
      if (esg == null) {
        for (int i=0; i<globalGroups.size(); i++) {
          PerformanceTimerGroup ptg = globalGroups.get(i);
          Object o = ptg.object;
          if ((o != null) && (o instanceof ESlateHandle) &&
              ((ESlateHandle)o).getComponent().getClass().getName().equals(
                ESLATE_CONTAINER_CLASS_NAME)
              ) {
            esg = ptg;
            break;
          }
        }
      }
      if (esg != null) {
        int n = globalGroups.size();
        for (int i=n-1; i>=0; i--) {
          PerformanceTimerGroup g = globalGroups.get(i);
          if (g.id != esg.id) {
            globalGroups.remove(i);
            tmp.n = g.id;
            globalHash.remove(tmp);
            removeGlobalGroupFromGUI(g);
            fireEvent(
              PerformanceTimerGroupEvent.PERFORMANCE_GROUP_REMOVED, null, g
            );
          }
        }
        for (int i=0; i<nDefaultGroups; i++) {
          globalGroupMembers[i].clear();
        }
      }
      deferredParent.clear();
      deferredChild.clear();
    }
  }

  /**
   * Removes all the roots of the PerformanceTimerGroup hierarchy, apart from
   * the default global PerformanceTimerGroups and the group belonging to
   * the E-Slate container, leaving these groups empty.
   * This method works regardless of the state of the performance manager.
   */
  void resetGlobalPerformanceTimerGroups()
  {
    PerformanceTimerGroup esg = eslateGroup;
    if (esg == null) {
      for (int i=0; i<globalGroups.size(); i++) {
        PerformanceTimerGroup ptg = globalGroups.get(i);
        Object o = ptg.object;
        if ((o != null) && (o instanceof ESlateHandle) &&
            ((ESlateHandle)o).getComponent().getClass().getName().equals(
              ESLATE_CONTAINER_CLASS_NAME)
            ) {
          esg = ptg;
          break;
        }
      }
    }
    int[] keep;
    if (esg != null) {
      keep = new int[]{
        CONSTRUCTOR, LOAD_STATE, SAVE_STATE, INIT_ESLATE_ASPECT, esg.id
      };
    }else{
      keep = new int[]{
        CONSTRUCTOR, LOAD_STATE, SAVE_STATE, INIT_ESLATE_ASPECT
      };
    }
    int n = globalGroups.size();
    for (int i=n-1; i>=0; i--) {
      PerformanceTimerGroup g = globalGroups.get(i);
      int id = g.id;
      int nKeep = keep.length;
      boolean found = false;
      for (int j=0; j<nKeep; j++) {
        if (id == keep[j]) {
          found = true;
          break;
        }
      }
      if (!found) {
        globalGroups.remove(i);
        tmp.n = g.id;
        globalHash.remove(tmp);
        removeGlobalGroupFromGUI(g);
        fireEvent(
          PerformanceTimerGroupEvent.PERFORMANCE_GROUP_REMOVED, null, g
        );
      }else{
        g.clearChildren();
      }
    }
    for (int i=0; i<nDefaultGroups; i++) {
      globalGroupMembers[i].clear();
    }
    deferredParent.clear();
    deferredChild.clear();
  }

  /**
   * Creates the default global PerformanceTimerGroups. Only those groups that
   * do not already exist will be created.
   */
  void createDefaultGlobalPerformanceTimerGroups()
  {
    if (enabled) {
      PerformanceTimerGroup ptg;

      tmp.n = CONSTRUCTOR;
      ptg = globalHash.get(tmp);
      if (ptg == null) {
        createGlobalPerformanceTimerGroup(
          CONSTRUCTOR, resources.getString("constructor"), false
        );
      } 

      tmp.n = LOAD_STATE;
      ptg = globalHash.get(tmp);
      if (ptg == null) {
        createGlobalPerformanceTimerGroup(
          LOAD_STATE, resources.getString("loadState"), false
        );
      }

      tmp.n = SAVE_STATE;
      ptg = globalHash.get(tmp);
      if (ptg == null) {
        ptg = createGlobalPerformanceTimerGroup(
          SAVE_STATE, resources.getString("saveState"), false
        );
      }

      tmp.n = INIT_ESLATE_ASPECT;
      ptg = globalHash.get(tmp);
      if (ptg == null) {
        ptg = createGlobalPerformanceTimerGroup(
          INIT_ESLATE_ASPECT, resources.getString("initESlate"), false
        );
      }
    }
  }

  /**
   * Returns the PerformanceTimerGroup associated with the specified E-Slate
   * handle. Each component has is own PerformanceTimerGroup, by default.
   * These are automatically constucted by the PerformanceManager, when it
   * is enabled. Also these groups are created and removed from the hierarchy,
   * as components are entered in to the microworld and removed from it. The
   * component PerformanceTimerGroups are organized in the same hierarchy as
   * the components themselves. These groups are not constructed while the
   * PerformanceManager is disabled.
   * @param     handle  The ESlateHandle of the component.
   * @return    The PerformanceTimerGroup associated with the specified
   *            E-Slate component. Returns <code>null</code> if the
   *            PerformanceManager disabled or handle is <code>null</code>.
   */
  public PerformanceTimerGroup getPerformanceTimerGroup(ESlateHandle handle)
  {
    if (enabled && (handle != null)) {
      PerformanceTimerGroup ptg = handleHash.get(handle);
      if (ptg == null) {
        Object compo = handle.getComponent();
        ptg = compoHash.get(compo);
        if (ptg != null) {
          if ((mw != null) && !mw.isLoading()) {
            //compoHash.remove(compo);
            ptg.setName(handle.getComponentName());
          }
        }else{
          ptg = createGlobalPerformanceTimerGroup(
            handle.getComponentName(), false
          );
        }
        ptg.object = handle;
        handleHash.put(handle, ptg);
      }
      return ptg;
    }else{
      return null;
    }
  }

  /**
   * Returns the PerformanceTimerGroup associated with the specified
   * component.  Each component has is own PerformanceTimerGroup, by default.
   * These are automatically constucted by the PerformanceManager, when it
   * is enabled. Also these groups are created and removed from the hierarchy,
   * as components are entered in to the microworld and removed from it. The
   * component PerformanceTimerGroups are organized in the same hierarchy as
   * the components themselves. These groups are not constructed while the
   * PerformanceManager is disabled.
   * @param     component       The ESlateHandle of the component.
   * @return    The PerformanceTimerGroup associated with the specified
   *            E-Slate component. Returns <code>null</code> if the
   *            PerformanceManager disabled or handle is <code>null</code>.
   */
  public PerformanceTimerGroup getPerformanceTimerGroup(Object component)
  {
    if (enabled && (component != null)) {
      ESlateHandle h = mw.getComponentHandle(component);
      if (h != null) {
        return getPerformanceTimerGroup(h);
      }else{
        PerformanceTimerGroup ptg = compoHash.get(component);
        if (ptg == null) {
          ptg = createGlobalPerformanceTimerGroup(
            component.getClass().getName() + "@" +
              Integer.toHexString(component.hashCode()),
            false
          );
          ptg.object = component;
          compoHash.put(component, ptg);
        }
        return ptg;
      }
    }else{
      return null;
    }
  }

  /**
   * Returns the PerformanceTimerGroup that has the specified internal id.
   * @param     id      The internal id of the PerformanceTimerGroup.
   * @return    The requested PerformanceTimerGroup. If no such group exists,
   *            <code>null</code> is returned.
   */
  public PerformanceTimerGroup getPerformanceTimerGroupByID(int id)
  {
    synchronized (this) {
      return internalGetPerformanceTimerGroupByID(id, globalGroups);
    }
  }

  /**
   * Returns the PerformanceTimerGroup that has the specified internal id.
   * @param     id      The internal id of the PerformanceTimerGroup.
   * @param     groups  A list of PerformanceTimerGroup to search recursively
   *                    for the requested PerformanceTimerGroup.
   * @return    The requested PerformanceTimerGroup. If no such group exists,
   *            <code>null</code> is returned.
   */
  private PerformanceTimerGroup internalGetPerformanceTimerGroupByID(
    int id, PTGBaseArray groups)
  {
    int n = groups.size();
    PerformanceTimerGroup ptg;
    for (int i=0; i<n; i++) {
      ptg = groups.get(i);
      if (ptg.id == id) {
        return ptg;
      }else{
        synchronized (ptg) {
          ptg = internalGetPerformanceTimerGroupByID(id, ptg.children);
        }
        if (ptg != null) {
          return ptg;
        }
      }
    }
    return null;
  }

  /**
   * Sets the E-Slate microworld which is being analyzed by the performance
   * framework. This method creates all the default hierarchy for the
   * microworld. Does nothing  when the PerformanceManager is disabled.
   * @param     microworld      The ESlateMicroworld to be monitored.
   */
  void setESlateMicroworld(ESlateMicroworld microworld)
  {
    if (enabled) {
      if (microworld == null) {
        if (mw != null) {
          stopMonitoringMicroworld();
        }
        mw = null;
      }else{
        if (!microworld.equals(mw)) {
          if (mw != null) {
            stopMonitoringMicroworld();
          }
          mw = microworld;
          startMonitoringMicroworld();
        }
      }
    }else{
      mw = microworld;
    }
    constructorTimers.clear();
    eSlateTimers.clear();
    dptg.clear();
  }

  /**
   * Starts a PerformanceTimer after reseting it. Does nothing if the
   * PerformanceManager is disabled or the PerformanceTimer is
   * <code>null</code>.
   * @param     pt      The PerformanceTimer to be initialized.
   */
  public void init(PerformanceTimer pt)
  {
    if (enabled && (pt != null)) {
      pt.init();
    }
  }

  /**
   * Starts a PerformanceTimer, without reseting it. Does nothing if the
   * PerformanceManager is disabled or the PerformanceTimer is
   * <code>null</code>.
   * @param     pt      The PerformanceTimer to be started.
   */
  public void start(PerformanceTimer pt)
  {
    if (enabled && (pt != null)) {
      pt.start();
    }
  }

  /**
   * Stops a PerformanceTimer. Does nothing if the PerformanceManager is
   * disabled or the PerformanceTimer is <code>null</code>.
   * @param     pt      The PerformanceTimer to be stopped.
   */
  public void stop(PerformanceTimer pt)
  {
    if (enabled && (pt != null)) {
      pt.stop();
    }
  }

  /**
   * Resets a PerformanceTimer to zero. Does nothing if the PerformanceManager
   * is disabled or the PerformanceTimer is <code>null</code>. If the
   * PerformanceTimer is running, it is not stopped.
   * @param     pt      The PerformanceTimer to be reset.
   */
  public void reset(PerformanceTimer pt)
  {
    if (enabled && (pt != null)) {
      pt.reset();
    }
  }

  /**
   * Returns the current time of the PerformanceTimer. This method can be
   * called while the timer is counting. Returns -1 if the PerformanceManager
   * is disabled or the PerformanceTimer is <code>null</code>.
   * @param     pt      The PerformanceTimer which reports its current time.
   * @return    The elapsed time in milliseconds. Returns -1 if the
   *            PerformanceManager is disabled or the PerformanceTimer is
   *            <code>null</code>.
   */
  public long getElapsedTime(PerformanceTimer pt)
  {
    if (enabled && (pt != null)) {
      return pt.getElapsedTime();
    }else{
      return -1;
    }
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimer's elapsed time in milliseconds, preceded by the
   * supplied prefix and followed by the supplied suffix. Does nothing if
   * the PerformanceManager is disabled or the PerformanceTimer is
   * <code>null</code>.
   * @param     pt      The PerformanceTimer which reports its elapsed time.
   * @param     prefix  The text to print before the elapsed time.
   * @param     suffix  The text to print after the elapsed time.
   */
  public void displayElapsedTime(
    PerformanceTimer pt, String prefix, String suffix)
  {
    //System.out.println("*** displayElapsedTime " + pt);
    if (enabled && (pt != null)) {
      if (pt.deferred) {
        DisplayData d = new DisplayData(
          DisplayData.ELAPSED_TIME, pt, prefix, suffix, null, 0,
          pt.getElapsedTime(), 0
        );
        deferredDisplay.add(d);
      }else{
        internalDisplayElapsedTime(pt, prefix, suffix, pt.getElapsedTime());
      }
    }
    //System.out.println("***");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimer's elapsed time in milliseconds, preceded by the
   * supplied prefix. Does nothing if the PerformanceManager is disabled
   * or the PerformanceTimer is <code>null</code>.
   * PerformanceTimer is <code>null</code>.
   * @param     pt      The PerformanceTimer which reports its current time.
   * @param     prefix  The text to print before the elapsed time.
   */
  public void displayElapsedTime(PerformanceTimer pt, String prefix)
  {
    displayElapsedTime(pt, prefix, "");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimer's elapsed time in milliseconds, preceded by the
   * supplied prefix and followed by the supplied suffix. Does nothing if
   * the PerformanceManager is disabled or the PerformanceTimer is
   * <code>null</code>.
   * @param     pt      The PerformanceTimer which reports its elapsed time.
   * @param     prefix  The text to print before the elapsed time.
   * @param     suffix  The text to print after the elapsed time.
   * @param     time    The elapsed tiem to display.
   */
  private void internalDisplayElapsedTime(
    PerformanceTimer pt, String prefix, String suffix, long time)
  {
    if (enabled && (pt != null)) {
      if (pt.isDisplayEnabled()) {
        System.out.println(
          resources.getString("realTime1") + "\"" + pt.toString() + "\"" +
          resources.getString("realTime3") + ": " +
          prefix + " " + time + " " + suffix
        );
      }
    }
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimer's elapsed time in milliseconds, preceded by the
   * supplied prefix and followed by the supplied suffix. Does nothing if
   * the PerformanceManager is disabled or the PerformanceTimer is
   * <code>null</code>.
   * @param     pt      The PerformanceTimer which reports its elapsed time.
   * @param     h       The E-Slate handle to which the the PerformanceTimer
   *                    belongs.
   * @param     prefix  The text to print before the elapsed time.
   * @param     suffix  The text to print after the elapsed time.
   */
  public void displayElapsedTime(
    PerformanceTimer pt, ESlateHandle h, String prefix, String suffix)
  {
    //System.out.println("*** displayElapsedTime " + pt);
    if (enabled && (pt != null)) {
      if (pt.deferred) {
        DisplayData d = new DisplayData(
          DisplayData.ELAPSED_TIME_H, pt, prefix, suffix, h, 0,
          pt.getElapsedTime(), 0
        );
        deferredDisplay.add(d);
      }else{
        internalDisplayElapsedTime(pt, h, prefix, suffix, pt.getElapsedTime());
      }
    }
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimer's elapsed time in milliseconds, preceded by the
   * supplied prefix. Does nothing if the PerformanceManager is disabled
   * or the PerformanceTimer is <code>null</code>.
   * PerformanceTimer is <code>null</code>.
   * @param     pt      The PerformanceTimer which reports its current time.
   * @param     h       The E-Slate handle to which the the PerformanceTimer
   *                    belongs.
   * @param     prefix  The text to print before the elapsed time.
   */
  public void displayElapsedTime(
    PerformanceTimer pt, ESlateHandle h, String prefix)
  {
    displayElapsedTime(pt, h, prefix, "");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimer's elapsed time in milliseconds, preceded by the
   * supplied prefix and followed by the supplied suffix. Does nothing if
   * the PerformanceManager is disabled or the PerformanceTimer is
   * <code>null</code>.
   * @param     pt      The PerformanceTimer which reports its elapsed time.
   * @param     h       The E-Slate handle to which the the PerformanceTimer
   *                    belongs.
   * @param     prefix  The text to print before the elapsed time.
   * @param     suffix  The text to print after the elapsed time.
   * @param     time    The elapsed tiem to display.
   */
  private void internalDisplayElapsedTime(
    PerformanceTimer pt, ESlateHandle h, String prefix, String suffix,
    long time)
  {
    if (enabled && (pt != null)) {
      if (pt.isDisplayEnabled()) {
        System.out.println(
          resources.getString("realTime1") + "\"" + pt.toString() + "\"" +
          resources.getString("realTime2") +
          "\"" + h.getComponentPathName() + "\"" +
          resources.getString("realTime3") + ": " +
          prefix + " " + time + " " + suffix
        );
      }
    }
  }

  /**
   * Returns the accumulative time, in milliseconds, of all the
   * PerformanceTimers in the hierarchy whose root is <code>ptg</code>.
   * The accumulative time is the sum of the currently recorded time of all
   * the PerformanceTimer's children, calculated using the specified
   * policy. Returns -1 if the PerformanceManager is disabled or the
   * PerformanceTimer is <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its
   *                    accumulative time.
   * @param     policy  Determines whether the accumulative time will be
   *                    calculated only from the immediate children of the
   *                    PerformanceTimerGroup or all its hierarchy. Valid
   *                    values: PerformanceTimerGroup.THOROUGH,
   *                    PerformanceTimerGroup.CHILDREN_ONLY.
   * @return    The accumulative time, or -1, if the PerformanceManager is
   *            disabled.
   * @exception IllegalArgumentException        Thrown if the specified policy
   *                    is not one of
   *                    <code>PerformanceTimerGroup.CHILDREN_ONLY</code>,
   *                    <code>PerformanceTimerGroup.THOROUGH</code>.
   */
  public long getAccumulativeTime(PerformanceTimerGroup ptg, int policy)
    throws IllegalArgumentException
  {
    if (enabled && (ptg != null)) {
      return ptg.getAccumulativeTime(policy);
    }else{
      return -1;
    }
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroup's accumulative time in milliseconds, preceded by the
   * supplied prefix and followed by the supplied suffix. Does nothing if
   * the PerformanceManager is disabled or the PerformanceTimerGroup is
   * <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its
   *                    accumulative time.
   * @param     policy  Determines whether the accumulative time will be
   *                    calculated only from the immediate children of the
   *                    PerformanceTimerGroup or all its hierarchy. Valid
   *                    values: PerformanceTimerGroup.THOROUGH,
   *                    PerformanceTimerGroup.CHILDREN_ONLY.
   * @param     prefix  The text to print before the accumulative time.
   * @param     suffix  The text to print after the accumulative time.
   * @exception IllegalArgumentException        Thrown if the specified policy
   *                    is not one of
   *                    <code>PerformanceTimerGroup.CHILDREN_ONLY</code>,
   *                    <code>PerformanceTimerGroup.THOROUGH</code>.
   */
  public void displayAccumulativeTime(
    PerformanceTimerGroup ptg, int policy, String prefix, String suffix)
      throws IllegalArgumentException
  {
    //System.out.println("*** displayAccumulativeTime " + ptg);
    if (enabled && (ptg != null)) {
      if (ptg.deferred) {
        DisplayData d = new DisplayData(
          DisplayData.ACCUMULATIVE_TIME, ptg, prefix, suffix, null, policy,
          ptg.getAccumulativeTime(policy), 0
        );
        deferredDisplay.add(d);
      }else{
        internalDisplayAccumulativeTime(
          ptg, prefix, suffix, ptg.getAccumulativeTime(policy)
        );
      }
    }
    //System.out.println("***");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroup's accumulative time in milliseconds, preceded by
   * the supplied prefix. Does nothing if the PerformanceManager is disabled
   * or the PerformanceTimerGroup is <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its
   *                    accumulative time.
   * @param     policy  Determines whether the accumulative time will be
   *                    calculated only from the immediate children of the
   *                    PerformanceTimerGroup or all its hierarchy. Valid
   *                    values: PerformanceTimerGroup.THOROUGH,
   *                    PerformanceTimerGroup.CHILDREN_ONLY.
   * @param     prefix  The text to print before the accumulative time.
   * @exception IllegalArgumentException        Thrown if the specified policy
   *                    is not one of
   *                    <code>PerformanceTimerGroup.CHILDREN_ONLY</code>,
   *                    <code>PerformanceTimerGroup.THOROUGH</code>.
   */
  public void displayAccumulativeTime(
    PerformanceTimerGroup ptg, int policy, String prefix)
      throws IllegalArgumentException
  {
    displayAccumulativeTime(ptg, policy, prefix, "");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroup's accumulative time in milliseconds, preceded by the
   * supplied prefix and followed by the supplied suffix. Does nothing if
   * the PerformanceManager is disabled or the PerformanceTimerGroup is
   * <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its
   *                    accumulative time.
   * @param     prefix  The text to print before the accumulative time.
   * @param     suffix  The text to print after the accumulative time.
   * @param     time    The accumulative time to display.
   */
  private void internalDisplayAccumulativeTime(
    PerformanceTimerGroup ptg, String prefix, String suffix,
    long time)
  {
    if (enabled && (ptg != null)) {
      if (ptg.isDisplayEnabled()) {
        System.out.println(
          resources.getString("accTime1") + "\"" + ptg.toString() + "\"" +
          resources.getString("accTime3") + ": " +
          prefix + " " + time + " " + suffix
        );
      }
    }
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroup's accumulative time in milliseconds, preceded by the
   * supplied prefix and followed by the supplied suffix. Does nothing if
   * the PerformanceManager is disabled or the PerformanceTimerGroup is
   * <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its
   *                    accumulative time.
   * @param     policy  Determines whether the accumulative time will be
   *                    calculated only from the immediate children of the
   *                    PerformanceTimerGroup or all its hierarchy. Valid
   *                    values: PerformanceTimerGroup.THOROUGH,
   *                    PerformanceTimerGroup.CHILDREN_ONLY.
   * @param     h       The E-Slate handle to which the the PerformanceTimer
   *                    belongs.
   * @param     prefix  The text to print before the accumulative time.
   * @param     suffix  The text to print after the accumulative time.
   * @exception IllegalArgumentException        Thrown if the specified policy
   *                    is not one of
   *                    <code>PerformanceTimerGroup.CHILDREN_ONLY</code>,
   *                    <code>PerformanceTimerGroup.THOROUGH</code>.
   */
  public void displayAccumulativeTime(
    PerformanceTimerGroup ptg, int policy, ESlateHandle h,
    String prefix, String suffix)
      throws IllegalArgumentException
  {
    //System.out.println("*** displayAccumulativeTime " + ptg);
    if (enabled && (ptg != null)) {
      if (ptg.deferred) {
        DisplayData d = new DisplayData(
          DisplayData.ACCUMULATIVE_TIME_H, ptg, prefix, suffix, h, policy,
          ptg.getAccumulativeTime(policy), 0
        );
        deferredDisplay.add(d);
      }else{
        internalDisplayAccumulativeTime(
          ptg, h, prefix, suffix, ptg.getAccumulativeTime(policy)
        );
      }
    }
    //System.out.println("***");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroup's accumulative time in milliseconds, preceded by
   * the supplied prefix. Does nothing if the PerformanceManager is disabled
   * or the PerformanceTimerGroup is <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its
   *                    accumulative time.
   * @param     policy  Determines whether the accumulative time will be
   *                    calculated only from the immediate children of the
   *                    PerformanceTimerGroup or all its hierarchy. Valid
   *                    values: PerformanceTimerGroup.THOROUGH,
   *                    PerformanceTimerGroup.CHILDREN_ONLY.
   * @param     h       The E-Slate handle to which the the PerformanceTimer
   *                    belongs.
   * @param     prefix  The text to print before the accumulative time.
   * @exception IllegalArgumentException        Thrown if the specified policy
   *                    is not one of
   *                    <code>PerformanceTimerGroup.CHILDREN_ONLY</code>,
   *                    <code>PerformanceTimerGroup.THOROUGH</code>.
   */
  public void displayAccumulativeTime(
    PerformanceTimerGroup ptg, int policy, ESlateHandle h, String prefix)
      throws IllegalArgumentException
  {
    displayAccumulativeTime(ptg, policy, h, prefix, "");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroup's accumulative time in milliseconds, preceded by the
   * supplied prefix and followed by the supplied suffix. Does nothing if
   * the PerformanceManager is disabled or the PerformanceTimerGroup is
   * <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its
   *                    accumulative time.
   * @param     h       The E-Slate handle to which the the PerformanceTimer
   *                    belongs.
   * @param     prefix  The text to print before the accumulative time.
   * @param     suffix  The text to print after the accumulative time.
   * @param     time    The accumulative time to display.
   */
  private void internalDisplayAccumulativeTime(
    PerformanceTimerGroup ptg, ESlateHandle h,
    String prefix, String suffix, long time)
      throws IllegalArgumentException
  {
    if (enabled && (ptg != null)) {
      if (ptg.isDisplayEnabled()) {
        System.out.println(
          resources.getString("accTime1") + "\"" + ptg.toString() + "\"" +
          resources.getString("accTime2") + "\"" +
          h.getComponentPathName() + "\"" +
          resources.getString("accTime3") + ": " +
          prefix + " " + time + " " + suffix
        );
      }
    }
  }

  /**
   * Returns the time of a PerformanceTimerGroup, according to the modes
   * specified in the PerformanceManager's UI.
   * If the mode of the timer is <code>ELAPSED</code>, or if the timer
   * does not contain any children, then the time returned is the elapsed
   * time of the timer. If the time mode of the timer is
   * <code>ACCUMULATIVE</code>, then the time is calculated recursively
   * as the times of the timer's children. 
   * This method can be called while the timer is counting. Returns -1 if
   * the PerformanceManager is disabled or the PerformanceTimer is
   * <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its time.
   * @return    The time in milliseconds. Returns -1 if the
   *            PerformanceManager is disabled or the PerformanceTimer is
   *            <code>null</code>.
   */
  public long getTime(PerformanceTimerGroup ptg)
  {
    if (enabled && (ptg != null)) {
      return ptg.getTime();
    }else{
      return -1;
    }
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroup's time in milliseconds, according to the modes
   * specified in the PerformanceManager's UI, preceded by the supplied
   * prefix and followed by the supplied suffix.
   * If the mode of the timer is <code>ELAPSED</code>, or if the timer
   * does not contain any children, then the time displayed is the elapsed
   * time of the timer. If the time mode of the timer is
   * <code>ACCUMULATIVE</code>, then the time displayed is calculated
   * recursively as the times of the timer's children. 
   * Does nothing if the PerformanceManager
   * is disabled or the PerformanceTimer is <code>null</code>.
   *
   * @param     ptg     The PerformanceTimerGroup which reports its time.
   * @param     prefix  The text to print before the time.
   * @param     suffix  The text to print after the time.
   */
  public void displayTime(
    PerformanceTimerGroup ptg, String prefix, String suffix)
  {
    //System.out.println("*** displayTime " + ptg);
    if (enabled && (ptg != null)) {
      boolean isTimer = ptg.isTimer();
      long time1;
      if (!isTimer || (ptg.timeMode == ACCUMULATIVE)) {
        time1 = ptg.getTime();
      }else{
        time1 = 0L;
      }
      long time2;
      if (isTimer) {
        PerformanceTimer pt = (PerformanceTimer)ptg;
        time2 = pt.getElapsedTime();
      }else{
        time2 = 0L;
      }
      if (ptg.deferred) {
        DisplayData d = new DisplayData(
          DisplayData.TIME, ptg, prefix, suffix, null, 0,
          time1, time2
        );
        deferredDisplay.add(d);
      }else{
        internalDisplayTime(ptg, prefix, suffix, time1, time2);
      }
    }
    //System.out.println("***");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroup's time in milliseconds, according to the modes
   * specified in the PerformanceManager's UI, preceded by the supplied prefix.
   * If the mode of the timer is <code>ELAPSED</code>, or if the timer
   * does not contain any children, then the time displayed is the elapsed
   * time of the timer. If the time mode of the timer is
   * <code>ACCUMULATIVE</code>, then the time displayed is calculated
   * recursively as the times of the timer's children. 
   * Does nothing if the PerformanceManager is disabled or the PerformanceTimer
   * is <code>null</code>.
   * PerformanceTimer is <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its time.
   * @param     prefix  The text to print before the time.
   */
  public void displayTime(PerformanceTimerGroup ptg, String prefix)
  {
    displayTime(ptg, prefix, "");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroup's time in milliseconds, according to the modes
   * specified in the PerformanceManager's UI, preceded by the supplied
   * prefix and followed by the supplied suffix.
   * If the mode of the timer is <code>ELAPSED</code>, or if the timer
   * does not contain any children, then the time displayed is the elapsed
   * time of the timer. If the time mode of the timer is
   * <code>ACCUMULATIVE</code>, then the time displayed is calculated
   * recursively as the times of the timer's children. 
   * Does nothing if the PerformanceManager
   * is disabled or the PerformanceTimer is <code>null</code>.
   *
   * @param     ptg     The PerformanceTimerGroup which reports its time.
   * @param     prefix  The text to print before the time.
   * @param     suffix  The text to print after the time.
   * @param     time1   The accumulative time to display.
   * @param     time2   The elapsed time to display.
   */
  private void internalDisplayTime(
    PerformanceTimerGroup ptg, String prefix, String suffix,
    long time1, long time2)
  {
    if (enabled && (ptg != null)) {
      if (ptg.isDisplayEnabled()) {
        boolean isTimer = ptg.isTimer();
        if (!isTimer || (ptg.timeMode == ACCUMULATIVE)) {
          System.out.println(
            resources.getString("accTime1") + "\"" + ptg.toString() + "\"" +
            resources.getString("accTime3") + ": " +
            prefix + " " + time1 + " " + suffix
          );
        }
        if (isTimer) {
          System.out.println(
            resources.getString("time1") + "\"" + ptg.toString() + "\"" +
            resources.getString("time3") + ": " +
            prefix + " " + time2 + " " + suffix
          );
        }
      }
    }
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroups's time in milliseconds, according to the modes
   * specified in the PerformanceManager's UI, preceded by the supplied
   * prefix and followed by the supplied suffix.
   * If the mode of the timer is <code>ELAPSED</code>, or if the timer
   * does not contain any children, then the time displayed is the elapsed
   * time of the timer. If the time mode of the timer is
   * <code>ACCUMULATIVE</code>, then the time displayed is calculated
   * recursively as the times of the timer's children. 
   * Does nothing if the PerformanceManager
   * is disabled or the PerformanceTimer is <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its time.
   * @param     h       The E-Slate handle to which the the PerformanceTimer
   *                    belongs.
   * @param     prefix  The text to print before the time.
   * @param     suffix  The text to print after the time.
   */
  public void displayTime(
    PerformanceTimerGroup ptg, ESlateHandle h, String prefix, String suffix)
  {
    //System.out.println("*** displayTime " + ptg);
    if (enabled && (ptg != null)) {
      boolean isTimer = ptg.isTimer();
      long time1;
      if (!isTimer || (ptg.timeMode == ACCUMULATIVE)) {
        time1 = ptg.getTime();
      }else{
        time1 = 0L;
      }
      long time2;
      if (isTimer) {
        PerformanceTimer pt = (PerformanceTimer)ptg;
        time2 = pt.getElapsedTime();
      }else{
        time2 = 0;
      }
      if (ptg.deferred) {
        DisplayData d = new DisplayData(
          DisplayData.TIME, ptg, prefix, suffix, h, 0,
          time1, time2
        );
        deferredDisplay.add(d);
      }else{
        internalDisplayTime(ptg, h, prefix, suffix, time1, time2);
      }
    }
    //System.out.println("***");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroups's time in milliseconds, according to the modes
   * specified in the PerformanceManager's UI, preceded by the supplied prefix.
   * If the mode of the timer is <code>ELAPSED</code>, or if the timer
   * does not contain any children, then the time displayed is the elapsed
   * time of the timer. If the time mode of the timer is
   * <code>ACCUMULATIVE</code>, then the time displayed is calculated
   * recursively as the times of the timer's children. 
   * Does nothing if the PerformanceManager is disabled or the PerformanceTimer
   * is <code>null</code>.
   * PerformanceTimer is <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its time.
   * @param     h       The E-Slate handle to which the the PerformanceTimer
   *                    belongs.
   * @param     prefix  The text to print before the time.
   */
  public void displayTime(
    PerformanceTimerGroup ptg, ESlateHandle h, String prefix)
  {
    displayTime(ptg, h, prefix, "");
  }

  /**
   * Displays a message to the current standard output which contains a
   * PerformanceTimerGroups's time in milliseconds, according to the modes
   * specified in the PerformanceManager's UI, preceded by the supplied
   * prefix and followed by the supplied suffix.
   * If the mode of the timer is <code>ELAPSED</code>, or if the timer
   * does not contain any children, then the time displayed is the elapsed
   * time of the timer. If the time mode of the timer is
   * <code>ACCUMULATIVE</code>, then the time displayed is calculated
   * recursively as the times of the timer's children. 
   * Does nothing if the PerformanceManager
   * is disabled or the PerformanceTimer is <code>null</code>.
   * @param     ptg     The PerformanceTimerGroup which reports its time.
   * @param     h       The E-Slate handle to which the the PerformanceTimer
   *                    belongs.
   * @param     prefix  The text to print before the time.
   * @param     suffix  The text to print after the time.
   * @param     time1   The accumulative time to display.
   * @param     time2   The elapsed time to display.
   */
  private void internalDisplayTime(
    PerformanceTimerGroup ptg, ESlateHandle h, String prefix, String suffix,
    long time1, long time2)
  {
    if (enabled && (ptg != null)) {
      if (ptg.isDisplayEnabled()) {
        boolean isTimer = ptg.isTimer();
        if (!isTimer || (ptg.timeMode == ACCUMULATIVE)) {
          System.out.println(
            resources.getString("accTime1") + "\"" + ptg.toString() + "\"" +
            resources.getString("accTime2") +
            "\"" + h.getComponentPathName() + "\"" +
            resources.getString("accTime3") + ": " +
            prefix + " " + time1 + " " + suffix
          );
        }
        if (isTimer) {
          System.out.println(
            resources.getString("time1") + "\"" + ptg.toString() + "\"" +
            resources.getString("time2") +
            "\"" + h.getComponentPathName() + "\"" +
            resources.getString("time3") + ": " +
            prefix + " " + time2 + " " + suffix
          );
        }
      }
    }
  }

  /**
   * Sets the state of the PerformanceManager. When the PerformanceManager
   * is disabled, no performance monitoring occurs on the microworld.
   * This results into minimum runtime overhead.
   * @param     enabled The new state of the PerformanceManager.
   */
  void setEnabled(boolean enabled)
  {
    if (enabled != this.enabled) {
      this.enabled = enabled;
      createDefaultGlobalPerformanceTimerGroups();
      if (enabled) {
        startMonitoringMicroworld();
      }else{
        stopMonitoringMicroworld();
      }
      stateChanged();
    }
  }

  /**
   * Checks whether a PerformanceTimerGroup is a global (top-level)
   * PerformanceTimerGroup.
   * @param     ptg     The PerformanceTimerGroup to check.
   * @return    True if <code>ptg</code> is global, false otherwise.
   *            Alwaqys returns false if the PerformanceManager is disabled
   *            or <code>ptg</code> is <code>null</code>.
   */
  public boolean isPerformanceTimerGroupGlobal(PerformanceTimerGroup ptg)
  {
    if (enabled && (ptg != null)) {
      tmp.n = ptg.id;
      if (globalHash.get(tmp) != null) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the state of the managed PerformanceTimerGroups.
   * @return    The state of the managed PerformanceTimerGroups.
   * @see gr.cti.eslate.base.container.PerformanceManager#setState(gr.cti.eslate.base.container.PerformanceManagerState)
   */
  PerformanceManagerState getState()
  {
    PerformanceManagerState pms = new PerformanceManagerState(
      globalGroups, constructorTimers, eSlateTimers
    );
    return pms;
  }

  /**
   * Returns a list of all PerformanceTimerGroups.
   * @return    A list of all PerformanceTimerGroups.
   */
  PTGBaseArray getAllPerformanceTimerGroups()
  {
    // Collect the PerformanceTimerGroups in a hash table, to avoid getting
    // duplicates, as we may encounter a PerformanceTimerGroup more than once
    // when traversing the PerformanceTimerGroup graph.
    HashMap<IntWrapper, PerformanceTimerGroup> allPTGs =
      new HashMap<IntWrapper, PerformanceTimerGroup>();
    synchronized (this) {
      getPTGs(globalGroups, allPTGs);
    }
    PTGBaseArray a = new PTGBaseArray();
    Iterator<PerformanceTimerGroup> it = allPTGs.values().iterator();
    while (it.hasNext()) {
      a.add(it.next());
    }
    return a;
  }

  /**
   * Recursively fills in a list of  PerformanceTimerGroups from another list
   * of PerformanceTimerGroups.
   * @param     groups  The input list of PerformanceTimerGroups.
   * @param     out     The output list of PerformanceTimerGroups.
   * @
   */
  private void getPTGs
    (PTGBaseArray groups, HashMap<IntWrapper, PerformanceTimerGroup> out)
  {
    int nGroups = groups.size();
    for (int i=0; i<nGroups; i++) {
      PerformanceTimerGroup ptg = groups.get(i);
      IntWrapper n = new IntWrapper(ptg.id);
      out.put(n, ptg);
      getPTGs(ptg.children, out);
    }
  }

  /**
   * Sets the state of the managed PerformanceTimerGroups. Invoking this
   * method will update the PerformanceManager's GUI.
   * @param     state   The state of the managed PerformanceTimerGroups,
   *                    obtained by invoking the
   *    {@link gr.cti.eslate.base.container.PerformanceManager#getState()}
   *                    method.
   * @see       gr.cti.eslate.base.container.PerformanceManager#getState()
   */
  public void setState(PerformanceManagerState state)
  {
    this.state = state;
    clearStateApplied();
    int nGroups = globalGroups.size();
    for (int i=0; i<nGroups; i++) {
      PerformanceTimerGroup ptg = globalGroups.get(i);
      applyStateRecursively(ptg);
    }
    updateGUI();
  }

  /**
   * Sets the state of all the PerformanceTimerGroups in the hierarchy.
   * Ideally, there should be no need for this function, and
   * <code>applyStateRecursively(PerformanceTimerGroup ptg)</code> should
   * suffice. Unfortunately, any change in the hierarchy may invalidate state
   * assignments or make it possible to make assignments that had been
   * impossible before. These assignments may be anywhere, so we have to apply
   * the state to the entire hierarchy.
   */
  private void applyStateRecursively()
  {
    int nGroups = globalGroups.size();
    for (int i=0; i<nGroups; i++) {
      PerformanceTimerGroup p = globalGroups.get(i);
      applyStateRecursively(p);
    }
  }

  /**
   * Sets the state of a PerformanceTimerGroup and all its children,
   * according to the state that was specified using <code>setState()</code>.
   * @param     ptg     The PerformanceTimerGroup.
   */
  private void applyStateRecursively(PerformanceTimerGroup ptg)
  {
    applyState(ptg);
    int nGroups = ptg.children.size();
    for (int i=0; i<nGroups; i++) {
      PerformanceTimerGroup ptg2 = ptg.children.get(i);
      applyStateRecursively(ptg2);
    }
  }

  /**
   * Sets the state of a PerformanceTimerGroup according to the state that
   * was specified using <code>setState()</code>.
   * @param     ptg     The PerformanceTimerGroup.
   */
  private void applyState(PerformanceTimerGroup ptg)
  {
    if (!ptg.stateApplied && (state != null) && !ptg.deferred) {
      SBABaseArray a = new SBABaseArray();
      findNamePaths(ptg, a, new StringBaseArray());
      assignState(ptg, a);
    }
  }

  /**
   * Clears the <code>stateApplied</code> flag of all PerformanceTimerGroups.
   */
  private void clearStateApplied()
  {
    int n = globalGroups.size();
    for (int i=0; i<n; i++) {
      clearStateApplied(globalGroups.get(i));
    }
  }

  /**
   * Recursively clears the <code>stateApplied</code> flag of a
   * PerformanceTimerGroup and its children.
   */
  private static void clearStateApplied(PerformanceTimerGroup ptg)
  {
    ptg.stateApplied = false;
    int n = ptg.children.size();
    for (int i=0; i<n; i++) {
      clearStateApplied(ptg.children.get(i));
    }
  }

  /**
   * Finds all the name paths of a PerformanceTimerGroup, from top-level
   * (global) groups.
   * @param     ptg     The PerformanceTimerGroup.
   * @param     a       A list where the paths will be accumulated.
   * @param     s       A StringBaseArray used as temporrary storage to
   *                    construct the various paths.
   */
  private static void findNamePaths(
    PerformanceTimerGroup ptg, SBABaseArray a, StringBaseArray s)
  {
    int nS = s.size();
    s.add(ptg.name);
    if (ptg.parents.size() == 0) {
      a.add((StringBaseArray)(s.clone()));
    }else{
      int n = ptg.parents.size();
      for (int i=0; i<n; i++) {
        findNamePaths(ptg.parents.get(i), a, s);
      }
    }
    s.remove(nS);
  }

  /**
   * Assigns a state to a PerformanceTimerGroup, based on the state stored in
   * the <code>state</code> variable. The assignment is made using the
   * following heuristic: if one of the name paths of the
   * PerformanceTimerGroup from a global group is unique within the saved
   * state, then the corresponding state is assigned to the group; if one of
   * the name paths is not unique, but all the corresponding saved states are
   * identical, then the value of these states is assigned to the group;
   * otherwise, no assignment is made.
   * @param     ptg     The PerformanceTimerGroup.
   * @param     a       The name paths of <code>ptg</code> from top-level
   *                    (global) groups, as returned by
   *                    <code>findNamePaths()</code>.
   */
  private void assignState(PerformanceTimerGroup ptg, SBABaseArray a)
  {
    int nPaths = a.size();
    // Try to find a match for each of the group's name paths.
    for (int i=0; i<nPaths; i++) {
      // Get next name path.
      StringBaseArray s = a.get(i);
      // a1 contains the states of the groups that match names up to one level
      // of depth above the depth that is currently being checked.
      PMSBaseArray a1 = new PMSBaseArray();
      a1.add(state);
      // a2 wll contain the states of the groups that match names up to the
      // depth that is currently being checked.
      PMSBaseArray a2 = new PMSBaseArray();
      // Check one by one the path components for a match in the state.
      int n = s.size();
      for (int j=0; j<n; j++) {
        // Update a1 and a2.
        int nGroups = a1.size();
        for (int k=0; k<nGroups; k++) {
          PerformanceManagerState pms = a1.get(k);
          int nChildren = pms.children.length;
          for (int l=0; l<nChildren; l++) {
            a2.add(pms.children[l]);
          }
        }
        PMSBaseArray tmp;
        tmp = a2;
        a2 = a1;
        a1 = tmp;
        a2.clear();
        nGroups = a1.size();

        // Name at current path depth.
        String name = s.get(n-j-1);
        // Find which of the groups from the previous iteration match the name
        // of this iteration.
        for (int k=0; k<nGroups; k++) {
          PerformanceManagerState pms = a1.get(k);
          if (name.equals(pms.name)) {
            a2.add(pms);
          }
        }

        // Update a1 and a2.
        tmp = a2;
        a2 = a1;
        a1 = tmp;
        a2.clear();
      }

      int nFound = a1.size();
/*
      // Discard states that have already been assigned. This will prevent
      // new timers from getting the state of similarly named timers.
      for (int j=nFound-1; j>=0; j--) {
        if (a1.get(j).assigned) {
          a1.remove(j);
        }
      }
      nFound = a1.size();
*/

/*
      for(int j=s.size()-1; j>=0; j--) {
        if (j != (s.size()-1)) {
          System.out.print(".");
        }
        System.out.print(s.get(j));
      }
      System.out.println(": " + nFound);
*/
      if (nFound == 0) {
        // No match found; we'll try with the next name path.
      }else{
        if (nFound == 1) {
          // Exactly one match found; assign its state to the
          // PerformanecTimerGroup.
          PerformanceManagerState pms = a1.get(0);
          ptg.fastSetActive(pms.active);
          ptg.fastSetDisplayEnabled(pms.display);
          ptg.timeMode = pms.timeMode;
/*
          ptg.stateApplied = true;
          pms.assigned = true;
*/
          break;
        }else{
          // Multiple matches found.
          // Check if all matches have the same state.
          PerformanceManagerState pms = a1.get(0);
          boolean display = pms.display;
          boolean active = pms.active;
          boolean same = true;
          for (int j=1; j<nFound; j++) {
            pms = a1.get(j);
            if ((pms.display != display) || (pms.active != active)) {
              same = false;
              break;
            }
          }
          if (same) {
            // All matches have the same state; assign this state to the
            // PerformanecTimerGroup.
            ptg.fastSetActive(pms.active);
            ptg.fastSetDisplayEnabled(pms.display);
            ptg.timeMode = pms.timeMode;
/*
            ptg.stateApplied = true;
            pms.assigned = true;
*/
            break;
          }else{
            // Matches have different states; we'll try with the next name
            // path.
          }
        }
      }
    }
  }

  /**
   * Creates/updates the structure of PerformanceTimerGroups associated with
   * the structure of the currently monitored microworld and installs the
   * required listeners so that the structure reflects subsequent changes to
   * the microworld.
   */
  private void startMonitoringMicroworld()
  {
    if (mw != null) {
      ESlateHandle mwHandle = mw.getESlateHandle();
      if (eslateGroup == null) {
        eslateGroup = getPerformanceTimerGroup(mwHandle.getParentHandle());
      }
      PerformanceTimerGroup mwGroup = getPerformanceTimerGroup(mwHandle);
      mwGroup.object = mw;
      if (!(eslateGroup.children.contains(mwGroup))) {
        try {
          addPerformanceTimerGroup(eslateGroup, mwGroup);
        } catch (CycleException ce) {
          // Can't happen.
        }
      }
      synchronized (this) {
        deferredRemovals();
        updateHandleHierarchy(mwHandle, mwGroup);
        pruneHandleHierarchy(mwGroup);
        for (int i=0; i<nDefaultGroups; i++) {
          pruneGlobalGroupHierarchy(globalGroups.get(i), globalGroupMembers[i]);
        }
      }
      Iterator it = constructorTimers.values().iterator();
      while (it.hasNext()) {
        ClassTimer t = (ClassTimer)(it.next());
        t.clear();
      }
      it = eSlateTimers.values().iterator();
      while (it.hasNext()) {
        ClassTimer t = (ClassTimer)(it.next());
        t.clear();
      }
      installESlateListeners((ESlateHandle)(eslateGroup.object));
    }
  }

  /*
   * Removes the listeners that had been installed to ensure that the
   * structure of PerformanceTimerGroups associated with the structure of the
   * currently monitored microworld reflects subsequent changes to
   * the microworld.
   */
  private void stopMonitoringMicroworld()
  {
    if (mw != null) {
      removeESlateListeners((ESlateHandle)(eslateGroup.object));
    }
  }

  /**
   * Adds the PerformanceManager to the list of ESlateListsners of all the
   * E-Slate handles in the hierarchy of the currently monitored microworld.
   * @param     h       The E-Slate handle below which the addition will take
   *                    place.
   */
  private void installESlateListeners(ESlateHandle h)
  {
    h.addESlateListener(this);
    ESlateHandle[] children = h.getChildHandles();
    int n = children.length;
    for (int i=0; i<n; i++) {
      installESlateListeners(children[i]);
    }
  }

  /**
   * Removes the PerformanceManager from the list of ESlateListsners of all the
   * E-Slate handles in the hierarchy of the currently monitored microworld.
   * @param     h       The E-Slate handle below which the removal will take
   *                    place.
   */
  private void removeESlateListeners(ESlateHandle h)
  {
    h.removeESlateListener(this);
    ESlateHandle[] children = h.getChildHandles();
    int n = children.length;
    for (int i=0; i<n; i++) {
      removeESlateListeners(children[i]);
    }
  }

  /**
   * Update the name of the PerformanceTimerGroup associated with an E-Slate
   * handle whose name was changed.
   * @param     e       The event received when the name of an E-Slate handle
   *                    is changed.
   */
  public void componentNameChanged(ComponentNameChangedEvent e)
  {
    ESlateHandle h = e.getHandle();
    PerformanceTimerGroup group = handleHash.get(h);
    group.setName(h.getComponentName());
    applyStateRecursively(/*group*/);
    updateGUI();
  }

  /**
   * Update the PerformanceTimerGroup hierarchy whenever the parent of a
   * component in the microworld that is being monitored changes.
   * @param     e       The event received when the the parent of a component
   * in the microworld that is being monitored changes.
   */
  public void parentChanged(ParentChangedEvent e)
  {
    ESlateHandle childHandle = e.getComponent();
    ESlateHandle oldParentHandle = e.getOldParent();
    ESlateHandle newParentHandle = e.getNewParent();
    changeParent(childHandle, oldParentHandle, newParentHandle);
  }

  /**
   * Update the PerformanceTimerGroup hierarchy after the parent of a
   * component in the microworld that is being monitored changes.
   * @param     childHandle     The E-Slate handle of the component whose
   *                            parent has changed.
   * @param     oldParentHandle The E-Slate handle of the component's previous
   *                            parent.
   * @param     newParentHandle The E-Slate handle of the component's new
   *                            parent.
   */
  private void changeParent(
    ESlateHandle childHandle, ESlateHandle oldParentHandle,
    ESlateHandle newParentHandle)
  {
    PerformanceTimerGroup child = getPerformanceTimerGroup(childHandle);
    tmp.n = child.id;
    boolean isGlobal;
    if (globalHash.get(tmp) != null) {
      isGlobal = true;
    }else{
      isGlobal = false;
    }
    if (!isGlobal) {
      if (oldParentHandle != null) {
        PerformanceTimerGroup oldParent =
          getPerformanceTimerGroup(oldParentHandle);
        removePerformanceTimerGroup(oldParent, child);
      }
    }else{
      removeGlobalPerformanceTimerGroup(child);
    }
    if (newParentHandle != null) {
      PerformanceTimerGroup newParent =
        getPerformanceTimerGroup(newParentHandle);
      try {
        addPerformanceTimerGroup(newParent, child);
      } catch (CycleException ce) {
        // Can't happen.
      }
    }else{
      addGlobalPerformanceTimerGroup(child);
    }
    for (int i=0; i<nDefaultGroups; i++) {
      MyHashMap map = globalGroupMembers[i];
      PerformanceTimerGroup ptg = map.get(childHandle);
      if (ptg != null) {
        unregisterPerformanceTimerGroup(i, childHandle, oldParentHandle);
        if (newParentHandle != null) {
          registerPerformanceTimerGroup(i, ptg, childHandle);
        }
      }
    }

    Class<?> cl = childHandle.getComponent().getClass();
    String className = cl.getName();
    if (newParentHandle != null) {
      // Component possibly added.  Make sure that the ClassTimers associated
      // with the class of the newly created object exists.
      getConstructorTimer(className);
      getESlateTimer(className);
      // After updating the hierarchy, it might be possible to identify the
      // state of more PerformanceTimerGroups.
      applyStateRecursively(/*child*/);
    }else{
      // Component removed.
      if (mw != null) {
        int n = mw.getTotalComponentCount(cl);
        if (n == 0) {
          ClassTimer t = getConstructorTimer(className);
          constructorTimers.remove(className);
          removeClassTimerFromGUI(CONSTRUCTOR, t);
          t = getESlateTimer(className);
          eSlateTimers.remove(className);
          removeClassTimerFromGUI(INIT_ESLATE_ASPECT, t);
        }
      }
    }
  }

  /**
   * Update the PerformanceTimerGroup hierarchy whenever an E-Slate component
   * is disposed.
   * @param     e       The event received when a component is disposed.
   */
  public void handleDisposed(HandleDisposalEvent e)
  {
    ESlateHandle h = (ESlateHandle)(e.getSource());
    PerformanceTimerGroup ptg = handleHash.get(h);
    if (ptg != null) {
      PTGBaseArray parents = ptg.parents;
      int n = parents.size();
      if (n == 0) {
        removeGlobalPerformanceTimerGroup(ptg);
      }else{
        for (int i=n-1; i>-0; i--) {
          removePerformanceTimerGroup(parents.get(i), ptg);
        }
      }
      handleHash.remove(h);
    }
  }

  /**
   * Required by ESlateListener interface--not used.
   */
  public void disposingHandle(HandleDisposalEvent e)
  {
  }

  /**
   * Updates the structure of PerformanceTimerGroups associated with the
   * structure of the currently monitored microworld.
   * @param     h       The E-slate handle of the component below which the
   *                    structure will be updated.
   * @param     ptg     The PerformanceTimerGroup corresponding to
   *                    <code>h</code>.
   */
  private void updateHandleHierarchy(ESlateHandle h, PerformanceTimerGroup ptg)
  {
    String className = h.getComponent().getClass().getName();
    // This will ensure that the ClassTimers associated with the owner of the
    // handle exists.
    getConstructorTimer(className);
    getESlateTimer(className);

    ESlateHandle[] children = h.getChildHandles();
    int n = children.length;
    for (int i=0; i<n; i++) {
      ESlateHandle childHandle = children[i];
      PerformanceTimerGroup childGroup = handleHash.get(childHandle);
      if (childGroup == null) {
        childGroup = createPerformanceTimerGroup(
          ptg, childHandle.getComponentName(), false
        );
        childGroup.object = childHandle;
        handleHash.put(childHandle, childGroup);
      }
      updateHandleHierarchy(children[i], childGroup);
    }
  }

  /**
   * Removes from the structure of PerformanceTimerGroups associated with the
   * structure of the currently monitored microworld any
   * PerformanceTimerGroups associated with E-Slate handles that have been
   * disposed.
   * @param     ptg     The PerformanceTimerGroup below which the pruning will
   *                    take place.
   */
  private void pruneHandleHierarchy(PerformanceTimerGroup ptg)
  {
    PTGBaseArray children = ptg.children;
    int nChildren = children.size();
    for (int i=nChildren-1; i>=0; i--) {
      PerformanceTimerGroup child = children.get(i);
      pruneHandleHierarchy(child);
      Object obj = child.object;
      if ((obj != null) && (obj instanceof ESlateHandle)) {
        ESlateHandle h = (ESlateHandle)obj;
        if (h.isDisposed()) {
          removePerformanceTimerGroup(ptg, child);
          handleHash.remove(h);
        }
      }
    }
  }

  /**
   * Removes from the structure of PerformanceTimerGroups under one of the
   * default global categories any PerformanceTimerGroups associated with
   * E-Slate handles that have been disposed.
   * @param     ptg     The PerformanceTimerGroup below which the pruning will
   *                    take place.
   * @param     map     The E-Slate handle to PerformanceTimerGroup map for
   *                    the global category being pruned.
   */
  private void pruneGlobalGroupHierarchy(
    PerformanceTimerGroup ptg, MyHashMap map)
  {
    PTGBaseArray children = ptg.children;
    int nChildren = children.size();
    for (int i=nChildren-1; i>=0; i--) {
      PerformanceTimerGroup child = children.get(i);
      pruneGlobalGroupHierarchy(child, map);
      ESlateHandle h = (ESlateHandle)(map.getKey(child));
      if ((h != null) && h.isDisposed()) {
        removePerformanceTimerGroup(ptg, child);
        map.remove(h);
      }
    }
  }

  /**
   * Perform the PerformanceTimerGroup removals that were deferred because the
   * performance manager was disabled when they were requested.
   */
  private void deferredRemovals()
  {
    int n = deferredParent.size();
    for (int i=0; i<n; i++) {
      PerformanceTimerGroup parent = deferredParent.get(i);
      Object child = deferredChild.get(i);
      if (child instanceof PerformanceTimerGroup) {
        removePerformanceTimerGroup(parent, (PerformanceTimerGroup)child);
      }else{
        removePerformanceTimerGroup(parent, (String)child);
      }
    }
    deferredParent.clear();
    deferredChild.clear();
  }

  /**
   * Invoke the PerformanceManagerStateChanged method of all registered
   * PerformanceListeners.
   */
  private void stateChanged()
  {
    PropertyChangeEvent pce = new PropertyChangeEvent(
      this, STATE_PROPERTY, new Boolean(!enabled), new Boolean(enabled)
    );
    synchronized (performanceListeners) {
      int n = performanceListeners.size();
      for (int i=0; i<n; i++) {
        performanceListeners.get(i).performanceManagerStateChanged(pce);
      }
    }
  }

  /**
   * Returns the current state of the PerformanceManager.
   * @return    True if the PerformanceManager is enabled, false otherwise.
   */
  public boolean isEnabled()
  {
    return enabled;
  }

  /**
   * Convenience method, which makes all PerformanceTimerGroups in the
   * PerformanceTimerGroup graph display disabled. If the PerformanceManager
   * is disabled, this method does nothing.
   */
  void displayDisableAllTimers()
  {
    synchronized (this) {
      int n = globalGroups.size();
      for (int i=0; i<n; i++) {
        globalGroups.get(i).setDisplayEnabled(
          false, PerformanceTimerGroup.ENABLE_ALL
        );
      }
      updateGUI();
    }
  }

  /**
   * Makes a PerformanceTimerGroup display enabled or disabled.
   * @param     ptg     The PerformanceTimerGroup.
   * @param     display True if the PerformanceTimerGroup will be display
   *                    enabled, false otherwise.
   * @param     policy  Specifies how the display enabling/disablinng of the
   *                    PerformanceTimerGroup will affect its children. One of
   *                    <code>DONT_ENABLE</code>,
   *                    <code>ENABLE_IMMEDIATE</code>,
   *                    <code>ENABLE_ALL</code>.
   * @exception IllegalArgumentException        Thrown if the specified
   *                    <code>policy</code> is not one of
   *                    <code>DONT_ENABLE</code>,
   *                    <code>ENABLE_IMMEDIATE</code>,
   *                    <code>ENABLE_ALL</code>.
   */
  void displayEnable(PerformanceTimerGroup ptg, boolean display, int policy)
    throws IllegalArgumentException
  {
    ptg.setDisplayEnabled(display, policy);
    updateGUI();
  }

  /**
   * Adds a PerformanceListener to the PerformanceManager.
   * This method works regardless of the state of the performance manager.
   * @param     pl      The listener to add.
   * @see       gr.cti.eslate.base.container.event.PerformanceListener
   */
  public void addPerformanceListener(PerformanceListener pl)
  {
    synchronized (performanceListeners) {
      if (!performanceListeners.contains(pl)) {
        performanceListeners.add(pl);
      }
    }
  }

  /**
   * Removes a PerformanceListener from the PerformanceManager.
   * This method works regardless of the state of the performance manager.
   * @param     pl      The listener to remove.
   * @see       gr.cti.eslate.base.container.event.PerformanceListener
   */
  public void removePerformanceListener(PerformanceListener pl)
  {
    synchronized (performanceListeners) {
      int i = performanceListeners.indexOf(pl);
      if (i >= 0) {
        performanceListeners.remove(i);
      }
    }
  }

  /**
   * Returns a panel with the PerformanceManager's GUI.
   * @return    The requested jPanel.
   */
  PerformanceManagerPanel getGUI()
  {
    if (gui == null) {
      if (!enabled) {
        // This will update the ClassTimer structure.
        startMonitoringMicroworld();
        stopMonitoringMicroworld();
      }
      gui = new PerformanceManagerPanel();
    }
    return gui;
  }

  /**
   * Checks whether a PerformanceTimerGroup is the child of other
   * PerformanceTimerGroups.
   * @param     ptg     The PerformanceTimerGroup to check.
   */
  public boolean hasParent(PerformanceTimerGroup ptg)
  {
    return (ptg.getParentCount() > 0);
  }

  /**
   * Updates the GUI to reflect changes in the PerformanceManager structure.
   */
  private void updateGUI()
  {
    if (gui != null) {
      gui.updateStructure();
    }
  }

  /**
   * Revalidates and repaints the GUI to reflect changes in the
   * PerformanceManager state.
   */
  void redoGUI()
  {
    if (gui != null) {
      gui.redoGUI();
    }
  }

  /**
   * Adds a new global PerformanceTimerGroup to the GUI.
   * @param     ptg     The new global PerformanceTimerGroup.
   */
  private void addGlobalGroupToGUI(PerformanceTimerGroup ptg)
  {
    if (gui != null) {
      gui.addGlobalGroup(ptg);
    }
  }

  /**
   * Adds a new PerformanceTimerGroup to the GUI.
   * @param     parent  The PerformanceTimerGroup to which the new
   *                    PerformanceTimerGroup was added.
   * @param     child   The PerformanceTimerGroup that was added to
   *                    <code>parent</code>.
   */
  private void addGroupToGUI(
    PerformanceTimerGroup parent, PerformanceTimerGroup child)
  {
    if (gui != null) {
      gui.addGroup(parent, child);
    }
  }

  /**
   * Removes a global PerformanceTimerGroup from the GUI.
   * @param     ptg     The global PerformanceTimerGroup to remove
   */
  private void removeGlobalGroupFromGUI(PerformanceTimerGroup ptg)
  {
    if (gui != null) {
      gui.removeGlobalGroup(ptg);
    }
  }

  /**
   * Adds a new ClassTimer to the GUI.
   * @param     group   The group under which the ClassTimer should be added.
   * @param     ct      The ClassTimer to add.
   */
  private void addClassTimerToGUI(int group, ClassTimer ct)
  {
    if (gui != null) {
      gui.addClassTimer(group, ct);
    }
  }

  /**
   * Removes a ClassTimer from the GUI.
   * @param     group   The group from which the ClassTimer should be removed.
   * @param     ct      The ClassTimer to remove.
   */
  private void removeClassTimerFromGUI(int group, ClassTimer ct)
  {
    if (gui != null) {
      gui.removeClassTimer(group, ct);
    }
  }
  /**
   * Removes a PerformanceTimerGroup from the GUI. <EM>Note:</EM> This method
   * is package private in the sense of the performance manager.
   * Do not use from the container.
   * @param     parent  The parent PerformanceTimerGroup of the
   *                    PerformanceTimerGroup to remove.
   * @param     child   The PerformanceTimerGroup to remove.
   */
  void removeGroupFromGUI(
    PerformanceTimerGroup parent, PerformanceTimerGroup child)
  {
    if (gui != null) {
      gui.removeGroup(parent, child);
    }
  }

  /**
   * Prints the entire performance timer group structure. Useful for debugging.
   */
  public void dumpStructure()
  {
    int n = globalGroups.size();
    for (int i=0; i<n; i++) {
      dumpStructure(globalGroups.get(i), 0);
    }
  }

  /**
   * Prints the structure of a PerformanceTimerGroup.
   * @param     ptg     The PerformanceTimerGroup.
   * @param     indent  The indentation level.
   */
  private void dumpStructure(PerformanceTimerGroup ptg, int indent)
  {
    for (int i=0; i<indent; i++) {
      System.out.print("    ");
    }
    System.out.println(ptg.toString());
    int n = ptg.children.size();
    for (int i=0; i<n; i++) {
      dumpStructure(ptg.children.get(i), indent+1);
    }
  }

  /**
   * Prints the entire node structure of the GUI. Useful for debugging.
   */
  public void dumpGUIStructure()
  {
    if (gui != null) {
      gui.dumpGUIStructure();
    }else{
      System.out.println("GUI is null");
    }
  }

  /**
   * Sets the time mode of a PerformanceTimer.
   * @param     pt      The PerformanceTimer.
   * @param     mode    One of <code>ELAPSED</code>, <code>ACCUMULATIVE</code>.
   */
  public void setTimeMode(PerformanceTimer pt, int mode)
  {
    if (pt.timeMode != mode) {
      pt.toggleTimeMode();
    }
  }

  /**
   * Returns the time mode of a PerformanceTimer.
   * @param     pt      The PerformanceTimer.
   * @return    One of <code>ELAPSED</code>, <code>ACCUMULATIVE</code>.
   */
  public int getTimeMode(PerformanceTimer pt)
  {
    return pt.timeMode;
  }

  /**
   * Notifies the performance manager that the construction of a new component
   * has started.
   * @param     component       The component whose construction had started.
   */
  public void constructionStarted(Object component)
  {
    if (enabled) {
      ClassTimer t = getConstructorTimer(component.getClass().getName());
      if (t.enabled) {
        constructionTimes.add(timer.getTime());
        constructedComponents.add(component);
      }
    }
  }

  /**
   * Notifies the performance manager that the construction of a component has
   * finished.
   * @param     component       The component whose construction had finished.
   */
  public void constructionEnded(Object component)
  {
    if (enabled) {
      ClassTimer t = getConstructorTimer(component.getClass().getName());
      if (t.enabled) {
        long end = timer.getTime();
        int ind = constructedComponents.indexOf(component);
        long start = constructionTimes.get(ind);
        t.addTime(end - start);
        constructionTimes.remove(ind);
        constructedComponents.remove(ind);
      }
    }
  }

  /**
   * Returns the constructor time descriptor for a given class. If no such
   * descriptor exists, one is created.
   * @param     className       The name of the class.
   * @return    The constructor time descriptor for the given class.
   */
  private ClassTimer getConstructorTimer(String className)
  {
    ClassTimer t = (ClassTimer)(constructorTimers.get(className));
    if (t == null) {
      t = new ClassTimer(className);
      if ((state != null)) {
        StorageStructure cs = state.constructorTimers;
        if (cs != null) {
          t.enabled = cs.get(className, false);
        }
      }
      constructorTimers.put(className, t);
      addClassTimerToGUI(CONSTRUCTOR, t);
    }
    return t;
  }

  /**
   * Displays the construction times for each component class.
   * This method is invoked by the platform after a microworld is loaded.
   */
  public void displayConstructionTimes()
  {
    if (enabled) {
      Iterator it = constructorTimers.values().iterator();
      while (it.hasNext()) {
        ClassTimer t = (ClassTimer)(it.next());
        if (t.enabled) {
          int n = t.measurements;
          if (n > 0) {
            long totalTime = t.totalTime;
            long averageTime = totalTime / n;
            System.out.println(
              resources.getString("constrTime1") +
              n +
              ((n == 1) ?
                resources.getString("object") :
                resources.getString("objects")) + 
              resources.getString("constrTime2") +
              t.className +
              resources.getString("constrTime3") +
              totalTime +
              resources.getString("constrTime4") +
              averageTime +
              resources.getString("constrTime5")
            );
          }
        }
      }
    }
  }

  /**
   * Notifies the performance manager that the initialization of the E-Slate
   * aspect of a component has started.
   * @param     component       The component whose E-Slate aspect
   *                            initialization has started.
   */
  public void eSlateAspectInitStarted(Object component)
  {
    if (enabled) {
      ClassTimer t = getESlateTimer(component.getClass().getName());
      if (t.enabled) {
        eSlateTimes.put(component, new Long(timer.getTime()));
      }
    }
  }

  /**
   * Notifies the performance manager that the initialization of the E-Slate
   * aspect of a component has finished.
   * @param     component       The component whose E-Slate aspect
   *                            initialization has finished.
   */
  public void eSlateAspectInitEnded(Object component)
  {
    if (enabled) {
      ClassTimer t = getESlateTimer(component.getClass().getName());
      if (t.enabled) {
        long end = timer.getTime();
        long start = eSlateTimes.get(component).longValue();
        t.addTime(end - start);
        eSlateTimes.remove(component);
      }
    }
  }

  /**
   * Returns the E-Slate aspect initialization time descriptor for a given
   * class. If no such descriptor exists, one is created.
   * @param     className       The name of the class.
   * @return    The E-Slate aspect initialization time descriptor for the
   *            given class.
   */
  private ClassTimer getESlateTimer(String className)
  {
    ClassTimer t = (ClassTimer)(eSlateTimers.get(className));
    if (t == null) {
      t = new ClassTimer(className);
      if ((state != null)) {
        StorageStructure cs = state.eSlateTimers;
        if (cs != null) {
          t.enabled = cs.get(className, false);
        }
      }
      eSlateTimers.put(className, t);
      addClassTimerToGUI(INIT_ESLATE_ASPECT, t);
    }
    return t;
  }

  /**
   * Displays the E-Slate aspect initialization times for each component class.
   * This method is invoked by the platform after a microworld is loaded.
   */
  public void displayESlateInitTimes()
  {
    if (enabled) {
      Iterator it = eSlateTimers.values().iterator();
      while (it.hasNext()) {
        ClassTimer t = (ClassTimer)(it.next());
        if (t.enabled) {
          int n = t.measurements;
          if (n > 0) {
            long totalTime = t.totalTime;
            long averageTime = totalTime / n;
            System.out.println(
              resources.getString("eSlateTime1") +
              n +
              ((n == 1) ?
                resources.getString("object") :
                resources.getString("objects")) + 
              resources.getString("constrTime2") +
              t.className +
              resources.getString("constrTime3") +
              totalTime +
              resources.getString("constrTime4") +
              averageTime +
              resources.getString("constrTime5")
            );
          }
        }
      }
    }
  }

  /**
   * Registers PerformanceTimerGroups whose registration had been deferred
   * until after the microworld being monitored has finished loading, and
   * performs the deferred calls to the various display methods for those
   * timers which are display enabled after being registered.
   * This method is invoked by the platform after a microworld is loaded.
   * @param     clear   If true, the list of deferred calls will be cleared,
   *                    even if it has not been possible to perform all of
   *                    them.
   *                    
   */
  public void displayDeferredTimes(boolean clear)
  {
    if (enabled) {
      HashMap<Object, PerformanceTimerGroup> newCompoHash =
        new HashMap <Object, PerformanceTimerGroup>();
      // Associate groups, that had been associated with components, with
      // the E-Slate handles of their components...
      Iterator<Map.Entry<Object, PerformanceTimerGroup>> it =
        compoHash.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<Object, PerformanceTimerGroup> e = it.next();
        Object compo = e.getKey();
        PerformanceTimerGroup ptg = e.getValue();
        ESlateHandle h;
        if (mw != null) {
          h = mw.getComponentHandle(compo);
          ptg.object = h;
        }else{
          h = null;
        }
        if (h != null) {
          ptg.setName(h.getComponentName());
          handleHash.put(h, ptg);
        }else{
          newCompoHash.put(compo, ptg);
        }
      }
      // ...and place these groups in the correct point in the hierarchy.
      it = compoHash.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<Object, PerformanceTimerGroup> e = it.next();
        Object compo = e.getKey();
        PerformanceTimerGroup ptg = e.getValue();
        ESlateHandle h;
        if (mw != null) {
          h = mw.getComponentHandle(compo);
        }else{
          h = null;
        }
        if (h != null) {
          PerformanceTimerGroup parentPtg = handleHash.get(h.getParentHandle());
          if (parentPtg != null) {
            if (!parentPtg.children.contains(ptg)) {
              addPerformanceTimerGroup(parentPtg, ptg);
            }
          }
        }
      }
      compoHash.clear();
      compoHash = newCompoHash;

      int n = dptg.size();
      int oldN;
      // Register PerformanceTimerGroups whose registration had been deferred.
      // Perform the loop multiple times, until no more registrations are
      // possible.
      do {
        oldN = n;
        for (int i=n-1; i>=0; i--) {
          DeferredPTG d = dptg.get(i);
          PerformanceTimerGroup ptg = d.ptg;
          ptg.deferred = false;
          ptg.active = false;
          try {
            ESlateHandle h = mw.getComponentHandle(d.compo);
            if (h != null) {
              if (registerPerformanceTimerGroup(d.id, ptg, h, true)) {
                //applyStateRecursively(/*ptg*/);
                dptg.remove(i);
              }
            }
          } catch (Exception ex) {
            System.err.println(ex.getMessage());
          }
        }
        n = dptg.size();
      } while (n != oldN);
      applyStateRecursively();

      n = deferredDisplay.size();
      for (int i=n-1; i>=0; i--) {
        DisplayData d = deferredDisplay.get(i);
        PerformanceTimerGroup ptg = d.ptg;
        ptg.deferred = false;
        if (ptg.display) {
          switch (d.type) {
            case DisplayData.ELAPSED_TIME:
              internalDisplayElapsedTime(
                (PerformanceTimer)ptg, d.prefix, d.suffix, d.time1
              );
              break;
            case DisplayData.ELAPSED_TIME_H:
              internalDisplayElapsedTime(
                (PerformanceTimer)ptg, d.h, d.prefix, d.suffix, d.time1
              );
              break;
            case DisplayData.ACCUMULATIVE_TIME:
              internalDisplayAccumulativeTime(
                ptg, d.prefix, d.suffix, d.time1
              );
              break;
            case DisplayData.ACCUMULATIVE_TIME_H:
              internalDisplayAccumulativeTime(
                ptg, d.h, d.prefix, d.suffix, d.time1
              );
              break;
            case DisplayData.TIME:
              internalDisplayTime(
                ptg, d.prefix, d.suffix, d.time1, d.time2
              );
              break;
            case DisplayData.TIME_H:
              internalDisplayTime(
                ptg, d.h, d.prefix, d.suffix, d.time1, d.time2
              );
              break;
          }
          if (!clear) {
            deferredDisplay.remove(i);
          }
        }
      }
      if (clear) {
        deferredDisplay.clear();
      }
    }
  }

  /**
   * Checks whether the PerformanceTimerGroup is display enabled.
   * @param     ptg     The PerformanceTimerGroup.
   * @return    True if yes, false if no or if <code>ptg</code> is null.
   */
  public boolean isDisplayEnabled(PerformanceTimerGroup ptg)
  {
    if (ptg != null) {
      return ptg.isDisplayEnabled();
    }else{
      return false;
    }
  }

  /**
   * A hash map that allows obtaining the key corresponding to a value in
   * constant time.
   */
  private class MyHashMap extends HashMap<ESlateHandle, PerformanceTimerGroup>
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    /**
     * HashMap used for mapping values to keys.
     */
    private HashMap<PerformanceTimerGroup, ESlateHandle> reverseMap;

    /**
     * Construct a hash map.
     */
    private MyHashMap()
    {
      super();
      reverseMap = new HashMap<PerformanceTimerGroup, ESlateHandle>();
    }

    /**
     * Removes all mappings from this map.
     * @exception UnsupportedOperationException Thrown if clear is not
     *                          supported by this map. (Never thrown.)
     */
    public void clear()
    {
      super.clear();
      reverseMap.clear();
    }

    /**
     * Returns <code>true</code> if this map maps one or more keys to the
     * specified value.
     * @param   value   Value whose presence in this map is to be tested.
     * @return  <code>True</code> if this map maps one or more keys to the
     *          specified value.
     */
    public boolean containsValue(Object value)
    {
      return reverseMap.containsKey(value);
    }

    /**
     * Associates the specified value with the specified key in this map. If
     * the map previously contained a mapping for this key, the old value is
     * replaced.
     * @param   key     Key with which the specified value is to be
     *                  associated.
     * @param   value   Value to be associated with the specified key.
     * @return  Previous value associated with specified key, or
     *          <code>null</code> if there was no mapping for key.
     */
    public PerformanceTimerGroup put
      (ESlateHandle key, PerformanceTimerGroup value)
    {
      PerformanceTimerGroup old = super.put(key, value);
      reverseMap.remove(old);
      reverseMap.put(value, key);
      return old;
    }

    /**
     * 
     * Removes the mapping for this key from this map if present.
     * @param   key     Key whose mapping is to be removed from the map.
     * @return  Previous value associated with specified key, or null  if
     *          there was no mapping for key.
     */
    public PerformanceTimerGroup remove(Object key)
    {
      PerformanceTimerGroup old = super.remove(key);
      reverseMap.remove(old);
      return old;
    }

    /**
     * Returns the key that this map maps to the specified value.
     * Returns <code>null</code> if no key has been mapped to this value.
     * @return  The key that this map maps to the specified value or
     *          <code>null</code> if no key has been mapped to this value.
     * @exception       ClassCastException - If the value is of an
     *          inappropriate type for this map.
     * @exception       NullPointerException    Thrown if key is
     *                  <code>null</code>and this map does not not permit
     *                  null values.
     */
    public Object getKey(Object value)
      throws ClassCastException, NullPointerException
    {
      return reverseMap.get(value);
    }
  }
}
