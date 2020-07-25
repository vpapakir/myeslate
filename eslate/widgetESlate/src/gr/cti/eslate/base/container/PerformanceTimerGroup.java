package gr.cti.eslate.base.container;

import java.util.*;

import gr.cti.eslate.base.container.event.*;
import gr.cti.typeArray.*;

/**
 * PerformanceTimerGroup allows the grouping of performance measurements into
 * logical (user defined) hierachies. It also allows the management of all the
 * measurements that belong to a group. This class is part of the inner
 * workings of the performance framework. The user of the framework is not
 * allowed access to its methods. All access to a PerformanceTimerGroup
 * is accomplished via the PerformanceManager.
 * <P>
 * A PerformanceTimerGroup may have several children and it may also belong
 * to several parents. Thus, the created hierarchy is a graph. In
 * this graph however, no cycles are allowed, which means that no parent of a
 * node is allowed to be a descendant of this node.
 *
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 21-Sep-2007
 */
@SuppressWarnings("unchecked")
public class PerformanceTimerGroup implements Comparable
{
  /**
   * Constant which represents a policy for getting the accumulative time of
   * a PerformanceTimerGroup. According to this policy, only the time
   * measurements of the immediate children of the PerformanceTimerGroup
   * contribute to the accumulative time of the group.
   */
  public static final int CHILDREN_ONLY = 0;
  /**
   * Constant which represents a policy for getting the accumulative time of a
   * PerformanceTimerGroup. According to this policy, the time measurements
   * of all descendants in the hierarchy of the PerformanceTimerGroup
   * contribute to the accumulative time of the group.
   */
  public static final int THOROUGH = 1;

  /**
   * This constant specifies that when a PerformanceTimerGroup is
   * (de)activated, its sub-groups are not (de)activated.
   */
  public static final int DONT_ENABLE = 0;
  /**
   * This constant specifies that when a PerformanceTimerGroup is
   * (de)activated, only its immediate children-groups are (de)activated.
   */
  public static final int ENABLE_IMMEDIATE = 1;
  /**
   * This constant specifies that when a PerformanceTimerGroup is
   * (de)activated, all the groups in its hierarchy are (de)activated as well.
   */
  public static final int ENABLE_ALL = 2;

  /**
   * The id of the PerformanceTimerGroup.
   */
  int id;
  /**
   * The list of this PerformanceTimerGroup' children.
   */
  PTGBaseArray parents = new PTGBaseArray();
  /**
   * The list of this PerformanceTimerGroup' children.
   */
  PTGBaseArray children = new PTGBaseArray();
  /**
   * A hash table containing this PerformanceTimerGroup's children.
   * Used for fast access by id.
   */
  private HashMap<IntWrapper, PerformanceTimerGroup> childHash =
    new HashMap<IntWrapper, PerformanceTimerGroup>();
  /**
   * A hash table containing this PerformanceTimerGroup' parents.
   * Used for fast access by id.
   */
  private HashMap<IntWrapper, PerformanceTimerGroup> parentHash =
    new HashMap<IntWrapper, PerformanceTimerGroup>();
  /**
   * Used for accessing the hash table without creating new objects all the
   * time.
   */
  IntWrapper tmp = new IntWrapper(0);
  /**
   * The name of the PerformanceTimerGroup.
   */
  String name;
  /**
   * Indicates whether the PerformanceManager has applied a saved state to
   * this PerformanceTimerGroup.
   */
  boolean stateApplied = false;
  /**
   * <code>getTime()</code> will return the elapsed time of the timer.
   */
  static int ELAPSED = 0;
  /**
   * <code>getTime()</code> will return the sum of its children's times.
   */
  static int ACCUMULATIVE = 1;
  /**
   * The time mode of the PerformanceTimerGroup.
   */
  int timeMode = ACCUMULATIVE;  // ELAPSED does not make sense for
                                // PerformanceTimerGroups that are not
                                // PerformanceTimers.
  /**
   * The minimum id that is assigned automaticly;
   */
  final static int MIN_ID = 1000;
  /**
   * The next id to assign to a new PerformanceTimerGroup.
   */
  private static int nextID = MIN_ID;
  /**
   * Used for synchronization of objects specific to this class.
   */
  private final static Object globalSync = new Object();

  /**
   * Specifies whether the PerformanceTimerGroup is active.
   */
  protected boolean active = false;
  /**
   * Specifies whether the PerformanceTimerGroup is display enabled.
   */
  protected boolean display = false;
  /**
   * Specifies whether registration of the PerformanceTimerGroup has been
   * deferred until after the microworld being monitored has finished
   * loading.
   */
  protected boolean deferred = false;

  /**
   * An object associated with the PerformanceTimerGroup.
   */
  Object object = null;

  /**
   * Localized resources.
   */
  private static ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.base.container.PerformanceResource", Locale.getDefault()
  );

  /**
   * Cosntructs a PerformanceTimerGroup. The group is automatically assigned
   * an id, which is unique among all the ids of all the groups of the
   * PerformanceTimerGroup graph. The PerformanceTimerGroup is created in the
   * inactive state.
   */
  PerformanceTimerGroup()
  {
    super();
    name = null;
    id = newID();
  }

  /**
   * Cosntructs a PerformanceTimerGroup. The group is automatically assigned
   * an id, which is unique among all the ids of all the groups of the
   * PerformanceTimerGroup graph. The PerformanceTimerGroup is created in the
   * inactive state.
   * @param     name    The name of the new PerformanceTimerGroup.
   */
  PerformanceTimerGroup(String name)
  {
    super();
    this.name = name;
    id = newID();
  }

  /**
   * Constructs a PerformanceTimerGroup. The PerformanceTimerGroup is created
   * in the inactive state.
   * @param     id      The internal id of the PerformanceTimerGroup.
   * @exception IllegalArgumentException        Thrown if <code>id</code>
   *                    is not unique within the whole graph.
   */
  PerformanceTimerGroup(int id) throws IllegalArgumentException
  {
    super();
    name = null;
    this.id = checkID(id);
  }

  /**
   * Constructs a PerformanceTimerGroup. The PerformanceTimerGroup is created
   * in the inactive state.
   * @param     id      The internal id of the PerformanceTimerGroup.
   * @param     name    The name of the new PerformanceTimerGroup.
   * @exception IllegalArgumentException        Thrown if <code>id</code>
   *                    is not unique within the whole graph.
   */
  PerformanceTimerGroup(int id, String name) throws IllegalArgumentException
  {
    super();
    this.name = name;
    this.id = checkID(id);
  }

  /**
   * Returns an id for a new PerformanceTimerGroup.
   * @return    The new id.
   */
  private int newID()
  {
    synchronized(globalSync) {
      return nextID++;
    }
  }

  /**
   * Checks whether a given id has been assigned to some
   * PerformanceTimerGroup.
   * @param     n       The id to check.
   * @return    <code>n</code>.
   * @exception IllegalArgumentException        Thrown if <code>n</code> is in
   *                    use by some PerformanceTimerGroup.
   */
  private int checkID(int n) throws IllegalArgumentException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    PerformanceTimerGroup ptg = pm.getPerformanceTimerGroupByID(n);
    if (ptg == null) {
      return n;
    }else{
      throw new IllegalArgumentException(
        resources.getString("idInUse1") + n + resources.getString("idInUse2")
      );
    }
  }

  /**
   * Makes <code>ptg</code> a sub-group of this PerformanceTimerGroup.
   * @param     ptg     The PerformanceTimerGroup to be added.
   * @exception CycleException  Thrown when this PerformanceTimerGroup already
   *                    belongs to the hierarchy of the child which is
   *                    added to it.
   */
  void add(PerformanceTimerGroup ptg) throws CycleException
  {
    synchronized (this) {
      children.add(ptg);
      if (checkForCycles(this, this)) {
        children.remove(children.indexOf(ptg));
        throw new CycleException(resources.getString("cycle"));
      }else{
        IntWrapper t = new IntWrapper(ptg.id);
        childHash.put(t, ptg);
        ptg.parents.add(this);
        t = new IntWrapper(id);
        ptg.parentHash.put(t, this);
        if (!isTimer() || (timeMode == ACCUMULATIVE)) {
          if (active) {
            if (ptg.isTimer()) {
              ptg.active = true;
            }else{
              activate(ptg);
            }
          }
        }
      }
    }
  }

  /**
   * Checks whether there is a cycle beneath a certain node of the
   * PerformanceTimerGroup graph.
   * @param     start   The node from which to start looking for a cycle.
   * @param     target  If this node is detected as a descendant of the
   *                    starting node, then this signifies that a cycle has
   *                    been detected.
   * @return    <code>True</code> if a cycle is detected, <code>false</code>
   *            otherwise.
   */
  private boolean checkForCycles(
    PerformanceTimerGroup start, PerformanceTimerGroup target)
  {
    PTGBaseArray ch = start.children;
    int n = ch.size();
    for (int i=0; i<n; i++) {
      PerformanceTimerGroup child = ch.get(i);
      if ((target == child) || checkForCycles(child, target)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the array of the parents of the PerformanceTimerGroup.
   */
  PerformanceTimerGroup[] getParents()
  {
    synchronized (this) {
      return parents.toArray();
    }
  }

  /**
   * Returns the number of the parents of the PerformanceTimerGroup.
   */
  int getParentCount()
  {
    synchronized (this) {
      return parents.size();
    }
  }

  /**
   * Returns the children of the PerformanceTimerGroup whith the specified name.
   * <EM>CAUTION:</EM> the performance framework does not guarantee unique
   * names among the childen of a PerformanceTimerGroup, so this method
   * may return more than one PerformanceTimerGroup.
   * @param     name    The name of the child PerformanceTimerGroup.
   * @return    An array containing the child PerformanceTimerGroups.
   *            Returns <code>null</code> an empty array if there is no child
   *            with the specified name.
   * @exception NullPointerException    If <code>name</code> is null.
   */
  PerformanceTimerGroup[] getChild(String name) throws NullPointerException
  {
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    int nChildren;
    PTGBaseArray ch = new PTGBaseArray();
    synchronized (this) {
      nChildren = children.size();
      for (int i=0; i<nChildren; i++) {
        PerformanceTimerGroup ptg = children.get(i);
        if (((name == null) && (ptg.name == null)) ||
            ((name != null) && name.equals(ptg.name))) {
          ch.add(ptg);
        }
      }
    }
    return ch.toArray();
  }

  /**
   * Returns the child of the PerformanceTimerGroup at the specified path.
   * @param     path    The path to the child PerformanceTimeGroup. The path
   *                    starts from this PerformanceTimerGroup.
   * @return    The child PerformanceTimerGroup. Returns <code>null</code>
   *            if the path does not exist. Returns the PerformanceTimerGroup
   *            if the path is empty.
   * @exception NullPointerException    If <code>path</code> is
   *                    <code>null</code>.
   */
  PerformanceTimerGroup getChild(IntBaseArray path) throws NullPointerException
  {
    if (path == null) {
      throw new NullPointerException(resources.getString("nullPath"));
    }
    int n = path.size();
    if (n == 0) {
      return this;
    }else{
      PerformanceTimerGroup ptg = null;
      synchronized (this) {
        for (int i=0; i<n; i++) {
          tmp.n = path.get(i);
          ptg = childHash.get(tmp);
          if (ptg == null) {
            return null;
          }
        }
      }
      return ptg;
    }
  }

  /**
   * Returns the child of the PerformanceTimerGroup that has a given id.
   * @param     id      The id of the child PerformanceTimeGroup.
   * @return    The child PerformanceTimerGroup. Returns <code>null</code>
   *            if the child does not exist.
   */
  PerformanceTimerGroup getChildById(int id)
  {
    tmp.n = id;
    synchronized (this) {
      return childHash.get(tmp);
    }
  }

  /**
   * Returns the parent of the PerformanceTimerGroup that has a given id.
   * @param     id      The id of the child PerformanceTimeGroup.
   * @return    The parent PerformanceTimerGroup. Returns <code>null</code>
   *            if the parent does not exist.
   */
  PerformanceTimerGroup getParentById(int id)
  {
    tmp.n = id;
    synchronized (this) {
      return parentHash.get(tmp);
    }
  }

  /**
   * Returns the number of the PerformanceTimerGroup's children.
   * @return    The number of the PerformanceTimerGroup's children.
   */
  int getChildCount()
  {
    synchronized (this) {
      return children.size();
    }
  }

  /**
   * Returns the array of the children of the PerformanceTimerGroup. Returns
   * an empty array if the PerformanceTimerGroup has no children.
   */
  PerformanceTimerGroup[] getChildren()
  {
    synchronized (this) {
      return children.toArray();
    }
  }

  /**
   * Returns the child at the specified index.
   * @param     index   The index of the child PerformanceTimeGroup.
   * @return    The child PerformanceTimerGroup at the specified index.
   * @exception ArrayIndexOutOfBoundsException  If <code>index</code>
   *                    is invalid.
   */
  PerformanceTimerGroup getChild(int index)
    throws ArrayIndexOutOfBoundsException
  {
    return children.get(index);
  }

  /**
   * Removes all the children of the PerformanceTimerGroup.
   * This method generates PerformanceTimerGroupEvents for the removal of each
   * child.
   */
  void clearChildren()
  {
    clearChildren(true);
  }

  /**
   * Removes all the children of the PerformanceTimerGroup.
   * This method does not generate PerformanceTimerGroupEvents for the
   * removal of each child.
   */
  void fastClearChildren()
  {
    clearChildren(false);
  }

  /**
   * Removes all the children of the PerformanceTimerGroup.
   * @param     notify  Specifies whether PerformanceTimerGroupEvents for the
   *                    removal of each child will be generated.
   */
  private void clearChildren(boolean notify)
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    synchronized (this) {
      int n = children.size();
      for (int i=n-1; i>=0; i--) {
        PerformanceTimerGroup child = children.get(i);
        children.remove(i);
        removeSelfFromChildsParents(child);
        tmp.n = child.id;
        childHash.remove(tmp);
        pm.removeGroupFromGUI(null, child);
        if (notify) {
          pm.fireEvent(
            PerformanceTimerGroupEvent.PERFORMANCE_GROUP_REMOVED, this, child
          );
        }
      }
      //resetNextID();
    }
  }

  /**
   * Removes the child of the PerformanceTimerGroup at the specified index.
   * Removing this method does not generate a PerformanceTimerGroupEvent.
   * If you want to have a PerformanceTimerGroupEvent, use the
   * PerformanceManager.
   * @param     index   The index of the child to remove.
   * @return    The removed PerformanceTimerGroup.
   * @exception ArrayIndexOutOfBoundsException  If <code>index</code>
   *                    is invalid.
   */
  PerformanceTimerGroup removeChild(int index)
    throws ArrayIndexOutOfBoundsException
  {
    synchronized (this) {
      PerformanceTimerGroup child = children.get(index);
      synchronized (child) {
        children.remove(index);
        tmp.n = child.id;
        childHash.remove(tmp);
        removeSelfFromChildsParents(child);
        //resetNextID();
        deactivate(child);
      }
      return child;
    }
  }

  /**
   * Removes the specified immediate child of the PerformanceTimerGroup.
   * Invoking this method does not generate a PerformanceTimerGroupEvent.
   * If you want to have a PerformanceTimerGroupEvent, use the
   * PerformanceManager.
   * @param     child   The child PerformanceTimeGroup to be removed.
   * @return    The removed PerformanceTimerGroup, or <code>null</code>
   *            if <code>child</code> is not a child of the
   *            PerformanceTimerGroup.
   * @exception NullPointerException    If <code>child</code> is
   *            <code>null</code>.
   */
  PerformanceTimerGroup removeChild(PerformanceTimerGroup child)
    throws NullPointerException
  {
    if (child == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    synchronized (this) {
      synchronized (child) {
        children.remove(children.indexOf(child));
        tmp.n = child.id;
        childHash.remove(tmp);
        removeSelfFromChildsParents(child);
        //resetNextID();
      }
    }
    return child;
  }

  /**
   * Removes the references that a child PerformanceTimerGroup has to this
   * PerformanceTimerGroup as the child's parent.
   */
  private void removeSelfFromChildsParents(PerformanceTimerGroup child)
  {
    PTGBaseArray p = child.parents;
    int nParents = p.size();
    for (int i=0; i<nParents; i++) {
      if (p.get(i).id == id) {
        p.remove(i);
        break;
      }
    }
    tmp.n = id;
    child.parentHash.remove(tmp);
  }

/*
  // Set nextID to the maximum id in use, plus 1.
  private void resetNextID()
  {
    // Don't bother.
  }
*/

  /**
   * Sets the name of the PerformanceTimerGroup.
   * @param     name    The new name of the PerformanceTimeGroup.
   * @exception NullPointerException    If <code>name</code> is
   *                    <code>null</code>.
   */
  void setName(String name) throws NullPointerException
  {
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    this.name = name;
  }

  /**
   * Returns the name of the PerformanceTimerGroup.
   * @return    The name of the PerformanceTimerGroup.
   */
  String getName()
  {
    return name;
  }

  /**
   * Returns a string representation of the PerformanceTimerGroup.
   * @return    The requested string.
   */
  public String toString()
  {
    if (name != null) {
      return name;
    }else{
      return "<id " + id + ">";
    }
  }

  /**
   * Returns the paths of the PerformanceTimerGroup. Every path starts from
   * a global (top-level) PerformanceTimerGroup.
   * @return    An IntBaseArray containing all possible paths from a top-level
   *            PerformanceTimerGroup to this PerformanceTimerGroup.
   */
  IntBaseArray[] getPath()
  {
    ArrayList<IntBaseArray> paths = new ArrayList<IntBaseArray>();
    IntBaseArray currentPath = new IntBaseArray();
    synchronized (this) {
      identifyPaths(this, currentPath, paths);
    }
    int nPaths = paths.size();
    IntBaseArray[] returnedPaths = new IntBaseArray[nPaths];
    for (int i=0; i<nPaths; i++) {
      returnedPaths[i] = paths.get(i);
    }
    return returnedPaths;
  }

  /**
   * Finds all the paths that lead from top-level PerformanceTimerGroups to a
   * given PerformanceTimerGroup, and places them in an ArrayList.
   * @param     ptg             The PerformanceTimerGroup whose paths are
   *                            being found.
   * @param     currentPath     The path that is currently being followed.
   * @param     paths           The array list to which ewach identified path
   *                            is placed.
   */
  private void identifyPaths(
    PerformanceTimerGroup ptg, IntBaseArray currentPath,
    ArrayList<IntBaseArray> paths)
  {
    currentPath.add(0, ptg.id);
    int nParents = ptg.parents.size();
    if (nParents == 0) {
      paths.add((IntBaseArray)currentPath.clone());
    }else{
      for (int i=0; i<nParents; i++) {
        identifyPaths(parents.get(i), currentPath, paths);
      }
    }
    currentPath.remove(currentPath.size()-1);
  }

  /**
   * Returns the internal id of the PerformanceTimerGroup.
   * @return    id      The internal id of the PerformanceTimerGroup.
   */
  int getID()
  {
    return id;
  }

  /**
   * Returns the accumulative time of the PerformanceTimerGroup. The time
   * reported is the sum of the time measurements of the children of the
   * PerformanceTimerGroup.
   * @param     policy  One of <code>CHILDREN_ONLY</code>,
   *                    <code>THOROUGH</code>.
   * @return    The accumulative time of the PerformanceTimerGroup.
   * @exception IllegalArgumentException        Thrown if the specified policy
   *                    is not one of <code>CHILDREN_ONLY</code>,
   *                    <code>THOROUGH</code>.
   */
  long getAccumulativeTime(int policy) throws IllegalArgumentException
  {
    long total = 0L;
    switch (policy) {
      case CHILDREN_ONLY:
        synchronized (this) {
          int nChildren = children.size();
          for (int i=0; i<nChildren; i++) {
            PerformanceTimerGroup ptg = children.get(i);
            if (ptg.isTimer()) {
              total += ((PerformanceTimer)ptg).getElapsedTime();
            }
          }
        }
        break;
      case THOROUGH:
        synchronized (this) {
          int nChildren = children.size();
          for (int i=0; i<nChildren; i++) {
            PerformanceTimerGroup ptg = children.get(i);
            if (ptg.children.size() > 0) {
              total += ptg.getAccumulativeTime(policy);
            }else{
              if (ptg.isTimer()) {
                total += ((PerformanceTimer)ptg).getElapsedTime();
              }
            }
          }
        }
        break;
      default:
        throw new IllegalArgumentException(resources.getString("badPolicy"));
    }
    total += getClassTimerTime();
    return total;
  }

  /**
   * Returns the time of the PerformanceTimerGroup, according to the time
   * mode. If the PerformanceTimerGroup is not a PerformanceTimer, or its time
   * mode is <code>ACCUMULATIVE</code>, the time is calculated as the sum of
   * the elapsed times of the PerformanceTimerGroup's children. (For empty
   * PerformanceTimerGroup of this type, 0 is returned. If any of the
   * children are not PerformanceTimers, </code>getTime()</code> is applied to
   * them recursively.) If the PerformanceTimerGroup is in the
   * <code>ELAPSED<code> state, then its elapsed time is returned, instead.
   * @return    The time of the PerformanceTimerGroup.
   */
  long getTime()
  {
    long total = 0L;
    synchronized(this) {
      if (!isTimer() || (timeMode == ACCUMULATIVE)) {
        int nChildren = children.size();
        for (int i=0; i<nChildren; i++) {
          PerformanceTimerGroup ptg = children.get(i);
          if (ptg.isTimer()) {
            total += ((PerformanceTimer)ptg).getElapsedTime();
          }else{
            total += ptg.getTime();
          }
        }
        total += getClassTimerTime();
      }else{
        total += ((PerformanceTimer)this).getElapsedTime();
      }
    }
    return total;
  }

  /**
   * Returns the total time for the class timers that may be associated with
   * this group.
   * @return    The requested time. If no class timers are associated with
   *            this group, 0 is returned.
   */
  private long getClassTimerTime()
  {
    long total = 0L;
    if (id == PerformanceManager.CONSTRUCTOR) {
      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      Iterator it = pm.constructorTimers.values().iterator();
      while (it.hasNext()) {
        ClassTimer t = (ClassTimer)(it.next());
        if (t.measurements > 0) {
          total += t.totalTime;
        }
      }
    }else{
      if (id == PerformanceManager.INIT_ESLATE_ASPECT) {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        Iterator it = pm.eSlateTimers.values().iterator();
        while (it.hasNext()) {
          ClassTimer t = (ClassTimer)(it.next());
          if (t.measurements > 0) {
            total += t.totalTime;
          }
        }
      }
    }
    return total;
  }

  /**
   * Toggles the time mode of a PerformanceTimerGroup between
   * <code>ELAPSED</code> and <code>ACCUMULATIVE</code>. This method does
   * nothing when invoked on a PerformanceTimerGroup that is not a
   * PerformanceTimer.
   */
  void toggleTimeMode()
  {
    if (isTimer()) {
      if (timeMode == ELAPSED) {
        timeMode = ACCUMULATIVE;
      }else{
        timeMode = ELAPSED;
      }
      if (active) {
        // Recalculate activation status of the subtree rooted at the
        // PerformanceTimerGroup, as this status depends on the
        // PerformanceTimerGroup time mode.
        //setDisplayEnabled(false, DONT_ENABLE);
        //setDisplayEnabled(true, DONT_ENABLE);
        if (timeMode == ACCUMULATIVE) {
          activate(this);
        }else{
          deactivate(this);
        }
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.redoGUI();
      }
    }
  }

  /**
   * Makes the PerformanceTimerGroup display enabled or disabled.
   * @param     enabled True if the PerformanceTimerGroup will be made display
   *                    enabled, false otherwise.
   * @param     policy  Specifies how the display enabling/disablinng of the
   *                    PerformanceTimerGroup will affect its children.
   *                    One of <code>DONT_ENABLE</code>, 
   *                    <code>ENABLE_IMMEDIATE</code>,
   *                    <code>ENABLE_ALL</code>.
   * @exception IllegalArgumentException        Thrown if the specified 
   *                    <code>policy</code> is not one of
   *                    <code>DONT_ENABLE</code>,
   *                    <code>ENABLE_IMMEDIATE</code>,
   *                    <code>ENABLE_ALL</code>.
   */
  void setDisplayEnabled(boolean enabled, int policy)
    throws IllegalArgumentException
  {
    display = enabled;
    switch (policy) {
      case DONT_ENABLE:
        break;
      case ENABLE_IMMEDIATE:
        synchronized (this) {
          int nChildren = children.size();
          for (int i=0 ;i<nChildren; i++) {
            PerformanceTimerGroup ptg = children.get(i);
            ptg.display = enabled;
          }
        }
        break;
      case ENABLE_ALL:
        synchronized (this) {
          int nChildren = children.size();
          for (int i=0 ;i<nChildren; i++) {
            PerformanceTimerGroup ptg = children.get(i);
            ptg.setDisplayEnabled(enabled, policy);
          }
        }
        break;
    }
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    if (policy != DONT_ENABLE) {
      HashMap timers;
      switch (id) {
        case PerformanceManager.CONSTRUCTOR:
          timers = pm.constructorTimers;
          break;
        case PerformanceManager.INIT_ESLATE_ASPECT:
          timers = pm.eSlateTimers;
          break;
        default:
          timers = null;
      }
      if (timers != null) {
        Iterator it = timers.values().iterator();
        while (it.hasNext()) {
          ClassTimer t = (ClassTimer)(it.next());
          t.enabled = enabled;
        }
      }
    }
    setActive(enabled);
  }

  /**
   * Makes the PerformanceTimerGroup display enabled or disabled.
   * This method does not affect the activation state of the
   * PerformanceTimerGroup or of any of its children.
   * @param     enabled True if the PerformanceTimerGroup will be made display
   *                    enabled, false otherwise.
   */
  void fastSetDisplayEnabled(boolean enabled)
  {
    display = enabled;
  }

  /**
   * Checks whether the PerformanceTimerGroup is display enabled.
   * @return    True if yes, false otherwise.
   */
  boolean isDisplayEnabled()
  {
    return display;
  }

  /**
   * Specifies whether the PerformanceTimerGroup is active or not.
   * Activating a PerformanceTimer in the <code>ACCUMULATIVE</code> will also
   * activate its children. (If a child is not a PerformanceTimer, its
   * children will be activated recursively.) Activating a
   * PerformanceTimerGroup that is not a PerformanceTimer will activate its
   * children recursuvely.
   * @param     active  True if yes, false otherwise.
   */
  protected void setActive(boolean active)
  {
    if (active) {
      activate(this);
    }else{
      deactivate(this);
    }
  }

  /**
   * Activates a PerformanceTimerGroup.
   * Activating a PerformanceTimer in the <code>ACCUMULATIVE</code> will also
   * activate its children. (If a child is not a PerformanceTimer, its
   * children will be activated recursively.) Activating a
   * PerformanceTimerGroup that is not a PerformanceTimer will activate its
   * children recursuvely.
   */
  private static void activate(PerformanceTimerGroup ptg)
  {
    ptg.active = true;
    if (ptg.isTimer() && (ptg.timeMode == ELAPSED)) {
      return;
    }else{
      int n = ptg.children.size();
      for (int i=0; i<n; i++) {
        PerformanceTimerGroup child = ptg.children.get(i);
        if (child.isTimer()) {
          child.active = true;
        }else{
          activate(child);
        }
      }
    }
  }

  /**
   * Deactivates a PerformanceTimerGroup.
   * The PerformanceTimerGroup will not be deactivated if any other
   * PerformanceTimerGroups depend on it. After deactivating (or not) the
   * PerformanceTimerGroup, this method is applied recursively to all the
   * children of the PerformanceTimerGroup.
   * @param     ptg     The performanceTimerGroup.
   */
  private static void deactivate(PerformanceTimerGroup ptg)
  {
    if (okToDeactivate(ptg)) {
      ptg.active = false;
    }
    int n = ptg.children.size();
    for (int i=0; i<n; i++) {
      PerformanceTimerGroup child = ptg.children.get(i);
      deactivate(child);
    }
  }

  /**
   * Checks whether a PerformanceTimerGroup can be deactivated without
   * affecting other PerformanceTimerGroups that depend on it.
   * @param     ptg     The PerformanceTimerGroup.
   */
  private static boolean okToDeactivate(PerformanceTimerGroup ptg)
  {
    if (ptg.display) {
      return false;
    }else{
      int n = ptg.parents.size();
      boolean ok = true;
      for (int i=0; i<n; i++) {
        PerformanceTimerGroup parent = ptg.parents.get(i);
        if (parent.isTimer()) {
          if (parent.active && (parent.timeMode == ACCUMULATIVE)) {
            ok = false;
            break;
          }
        }else{
          if (parent.display) {
            ok = false;
            break;
          }else{
            ok &= okToDeactivate(parent);
          }
        }
      }
      return ok;
    }
  }

  /**
   * Specifies whether the PerformanceTimerGroup is active or not.
   * This method simply changes the activation state of the
   * PerformanceTimerGroup, without affecting the state of any of its
   * children.
   * @param     active  True if yes, false otherwise.
   */
  protected void fastSetActive(boolean active)
  {
    this.active = active;
  }

  /**
   * Returns the activation state of the PerformanceTimerGroup.
   * @return    True if the PerformanceTimerGroup is active, false otherwise.
   */
  boolean isActive()
  {
    return active;
  }

  /**
   * Checks whether the PerformanceTimerGroup is also a PerformanceTimer.
   * @return    True if yes, false otherwise.
   */
  boolean isTimer()
  {
    return (this instanceof PerformanceTimer);
  }

  /**
   * Compare this PerformanceTimerGroup with another PerformanceTimerGroup,
   * based on their ids.
   * @param     o       The PerformanceTimerGroup.
   * @return    Returns -1 if <code>this.id &lt; ptg.id</code>, 0 if
   *            <code>this.id == ptg.id</code>, and 1 if
   *            <code>this.id &gt; ptg.id</code>.
   */
  public int compareTo(Object o)
  {
    PerformanceTimerGroup ptg = (PerformanceTimerGroup)o;
    if (id < ptg.id) {
      return -1;
    }else{
      if (id == ptg.id) {
        return 0;
      }else{
        return 1;
      }
    }
  }

}
