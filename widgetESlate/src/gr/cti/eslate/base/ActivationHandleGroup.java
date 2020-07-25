package gr.cti.eslate.base;

import java.util.*;

/**
 * This class implements a group of E-Slate handles, maintaining the
 * <EM>active</EM> handle among these handles.
 * Components that maintain the notion of an active handle should use this
 * class, instead of implementing ad hoc solutions, so that activation of a
 * handle can be synchronized among all such components.
 *
 * @version     2.0.21, 28-Sep-2007
 * @author      Kriton Kyrimis
 */
@SuppressWarnings("unchecked")
public class ActivationHandleGroup
{
  /**
   * The E-Slate handle to use as a base for collecting handles.
   */
  private ESlateHandle handle;
  /**
   * The permissible classes for objects corresponding to the collected
   * handles.
   */
  private Class[] classes;
  /**
   * Classes to which the collected handles should <em>not</em>correspond.
   */
  private Class[] exclude;
  /**
   * The policy for collecting handles.
   */
  private int policy;
  /**
   * The collected handles.
   */
  private HashMap<ESlateHandle, ESlateHandle> handles =
      new HashMap<ESlateHandle, ESlateHandle>();
  /**
   * The active handle.
   */
  private ESlateHandle activeHandle;
  /**
   * Specifies whether the group should ensure that there is always an active
   * handle, as long as the group is not empty.
   */
  private boolean activeHandleAlwaysExists = false;
  /**
   * Indicates that an ActivationHandleGroup is currently firing its
   * ActivationListeners. Used to ensure that these listeners will not fire
   * other suchlisteners, causing an exponential burst of, essentially
   * useless, ActivationListener invocations, or even a loop.
   */
  private static boolean inFireListeners = false;

  /**
   * The attached ActiveHandleListeners.
   */
  private ActiveHandleListenerBaseArray listeners =
    new ActiveHandleListenerBaseArray();

  // Use values corresponding to recursion depth, so that we can add other
  // values, if needed. (E.g., 0 for "self", 2 for "children and
  // grandchildren", etc. In this sense, -1 is "infinite".
  /**
   * Only direct children of a handle will be added to the collection.
   */
  public final static int DIRECT_CHILDREN = 1;
  /**
   * All descendants of a handle will be added to teh collection.
   */
  public final static int ALL_CHILDREN = -1;

  /**
   * Construct an ActivationHandleGroup instance.
   * @param     handle  The handle to use as the starting point for adding
   *                    E-Slate handles to the collection. The handles that
   *                    will be added to the collection will be either direct
   *                    children or arbitrary descendants of
   *                    <code>handle</code>, depending on the value of
   *                    <code>policy</code>. Both the E-Slate handle hierarchy
   *                    and the hosted component hierarchy are considered when
   *                    building the collection. Subsequent modifications in
   *                    either hierarchy will make corresponding changes to
   *                    the collection, as well.
   * @param     classes An array of classes, specifying that only E-Slate
   *                    handles corresponding to objects of one of these
   *                    classes should be added to the list. If
   *                    <code>classes</code> is <code>null</code>, no
   *                    exclusion of handles based on the component class will
   *                    be made.
   * @param     exclude An array of classes, specifying that E-Slate handles
   *                    corresponding to objects of one of these classes
   *                    should <em>not</em> be added to the list. This
   *                    parameter can be <code>null</code>.
   * @param     policy  Specifies which handles qualify for addition into the
   *                    collection. This can be one of:
   *                    <UL>
   *                    <LI>
   *                    <code>DIRECT_CHILDREN</code>: only direct children of
   *                    <code>handle</code> and handles hosted by <code>handle
   *                    will be considered for addition to the collection.
   *                    </LI>
   *                    <LI>
   *                    <code>ALL_CHILDREN</code>: all descendants of
   *                    <code>handle</code> will be considered for addition to
   *                    the collection.
   *                    </LI>
   *                    </UL>
   * @exception IllegalArgumentException        Thrown if <code>policy</code>
   *                    is not one of <code>DIRECT_CHILDREN</code> or
   *                    <code>ALL_CHILDREN</code>.
   */
  public ActivationHandleGroup(
    ESlateHandle handle, Class[] classes, Class[] exclude, int policy)
    throws IllegalArgumentException
  {
    if (policy == DIRECT_CHILDREN || policy == ALL_CHILDREN) {
      this.policy = policy;
    }else{
      throw new IllegalArgumentException("policy = " + policy);
    }
    this.handle = handle;
    if (classes != null) {
      int nClasses = classes.length;
      this.classes = new Class[nClasses];
      System.arraycopy(classes, 0, this.classes, 0, nClasses);
    }else{
      this.classes = new Class[1];
      this.classes[0] = Object.class;
    }
    if (exclude == null) {
      exclude = new Class[0];
    }
    this.exclude = exclude;
    collectHandles(handle, policy);
    handle.addAssociatedActivationHandleGroup(this);
  }

  /**
   * Construct an ActivationHandleGroup instance.
   * @param     handle  The handle to use as the starting point for adding
   *                    E-Slate handles to the collection. The handles that
   *                    will be added to the collection will be either direct
   *                    children or arbitrary descendants of
   *                    <code>handle</code>, depending on the value of
   *                    <code>policy</code>. Both the E-Slate handle hierarchy
   *                    and the hosted component hierarchy are considered when
   *                    building the collection. Subsequent modifications in
   *                    either hierarchy will make corresponding changes to
   *                    the collection, as well.
   * @param     classes An array of classes, specifying that only E-Slate
   *                    handles corresponding to objects of one of these
   *                    classes should be added to the list. If
   *                    <code>classes</code> is <code>null</code>, no
   *                    exclusion of handles based on the component class will
   *                    be made.
   * @param     policy  Specifies which handles qualify for addition into the
   *                    collection. This can be one of:
   *                    <UL>
   *                    <LI>
   *                    <code>DIRECT_CHILDREN</code>: only direct children of
   *                    <code>handle</code> and handles hosted by <code>handle
   *                    will be considered for addition to the collection.
   *                    </LI>
   *                    <LI>
   *                    <code>ALL_CHILDREN</code>: all descendants of
   *                    <code>handle</code> will be considered for addition to
   *                    the collection.
   *                    </LI>
   *                    </UL>
   * @exception IllegalArgumentException        Thrown if <code>policy</code>
   *                    is not one of <code>DIRECT_CHILDREN</code> or
   *                    <code>ALL_CHILDREN</code>.
   */
  public ActivationHandleGroup(ESlateHandle handle, Class[] classes, int policy)
    throws IllegalArgumentException
  {
    this(handle, classes, null, policy);
  }

  /**
   * Add E-Slate handles to the collection.
   * @param     handle  The handle to use as a starting point.
   * @param     policy  The policy to use for adding handles. One of
   *                    <code>DIRECT_CHILDREN</code>,
   *                    <code>ALL_CHILDREN</code>.
   */
  private void collectHandles(ESlateHandle handle, int policy)
  {
    ESlateHandle children[] = handle.getChildHandles();
    addHandles(children);
    ESlateHandle hosted[] = handle.getHostedHandles();
    addHandles(hosted);
    if (policy == ALL_CHILDREN) {
      int n = children.length;
      for (int i=0; i<n; i++) {
        collectHandles(children[i], ALL_CHILDREN);
      }
      n = hosted.length;
      for (int i=0; i<n; i++) {
        collectHandles(hosted[i], ALL_CHILDREN);
      }
    }
  }

  /**
   * Add E-Slate handles to the collection.
   * @param     handles An array of handles to consider adding to the
   *                    colection. Only handles corresponding to objects of
   *                    a class in the <code>classes</code> array
   *                    and not corresponding to objects of a class in the
   *                    <code>exclude</code> array will be added.
   */
  private void addHandles(ESlateHandle[] handles)
  {
    int nHandles = handles.length;
    int nClasses = classes.length;
    int nExclude = exclude.length;
    for (int i=0; i<nHandles; i++) {
      for (int j=0; j<nClasses; j++) {
        ESlateHandle h = handles[i];
        Class c = classes[j];
        Object o = h.getComponent();
        if (c.isInstance(o)) {
          boolean ok = true;
          for (int k=0; k<nExclude; k++) {
            Class ec = exclude[k];
            if (ec.isInstance(o)) {
              ok = false;
              break;
            }
          }
          if (ok) {
            addHandle(h);
          }
        }
      }
    }
  }

  /**
   * Add an E-Slate handle to the collection.
   * @param     h       The E-Slate handle to add.
   */
  private void addHandle(ESlateHandle h)
  {
    if (!handles.containsKey(h)) {
      boolean activate;
      if (handles.isEmpty() && activeHandleAlwaysExists) {
        activate = true;
      }else{
        activate = false;
      }
      handles.put(h, h);
      h.addActivationHandleGroup(this);
      if (activate) {
        setActiveHandle(h);
      }
    }
  }

  /**
   * Remove E-Slate handles from the collection.
   * @param     handle  The handle to use as a starting point.
   * @param     policy  The policy to use for removing handles. One of
   *                    <code>DIRECT_CHILDREN</code>,
   *                    <code>ALL_CHILDREN</code>.
   */
  private void uncollectHandles(ESlateHandle handle, int policy)
  {
    removeHandle(handle);
    if (policy == ALL_CHILDREN) {
      ESlateHandle children[] = handle.getChildHandles();
      removeHandles(children);
      ESlateHandle hosted[] = handle.getHostedHandles();
      removeHandles(hosted);
      int n = children.length;
      for (int i=0; i<n; i++) {
        uncollectHandles(children[i], ALL_CHILDREN);
      }
      n = hosted.length;
      for (int i=0; i<n; i++) {
        uncollectHandles(hosted[i], ALL_CHILDREN);
      }
    }
  }

  /**
   * Remove E-Slate handles from the collection.
   * @param     handles An array of handles to remove from the colection.
   */
  protected void removeHandles(ESlateHandle[] handles)
  {
    int nHandles = handles.length;
    for (int i=0; i<nHandles; i++) {
      removeHandle(handles[i]);
    }
    if (activeHandleAlwaysExists) {
      activateSomeHandle();
    }
  }

  /**
   * Remove an E-Slate handle from the collection.
   * @param     h       The E-Slate handle to remove.
   */
  protected void removeHandle(ESlateHandle h)
  {
    handles.remove(h);
  }

  /**
   * Add E-Slate handles to the collection, according to the group's policy
   * for adding handles.
   * @param     h       The handle to use as a starting point.
   */
  void addHandleTree(ESlateHandle h)
  {
    ESlateHandle parent = h.getParentHandle();
    if (parent != null) {
      if ((policy == ALL_CHILDREN) || parent.equals(handle)) {
        collectHandles(parent, policy);
      }
    }
  }

  /**
   * Remove E-Slate handles from the collection, according to the group's
   * policy for adding handles.
   * @param     h       The handle to use as a starting point.
   */
  void removeHandleTree(ESlateHandle h)
  {
    uncollectHandles(h, policy);
    if (activeHandleAlwaysExists) {
      activateSomeHandle();
    }
  }

  /**
   * Activates one of the handles in the group. The handle which will be
   * activated is unspecified. Override this method to provide a policy
   * for selecting the handle to activate.
   */
  protected void activateSomeHandle()
  {
    if (!handles.isEmpty()) {
      ESlateHandle h = handles.keySet().iterator().next();
      setActiveHandle(h);
    }
  }

  /**
   * Specifies the activeHandle within the group.
   * @param     h       The new active handle. If <code>h</code> does not
   *                    belong to this group, nothing happens.
   */
  public void setActiveHandle(ESlateHandle h)
  {
    if ((h != null) && containsHandle(h) && !h.equals(activeHandle)) {
      ESlateHandle oldActiveHandle = activeHandle;
      activeHandle = h;
      ActiveHandleEvent e =
        new ActiveHandleEvent(this, oldActiveHandle, activeHandle);
      fireListeners(e);
      h.activate();
    }
  }

  /**
   * Fires attached <code>ActiveHandleListener</code>s.
   * @param     e       The event to send.
   */
  private void fireListeners(ActiveHandleEvent e)
  {
    if (!inFireListeners) {
      inFireListeners = true;
      ESlateMicroworld mw = handle.getESlateMicroworld();
      // Do not send events while components are being restored or while
      // microworld is closing, when it doesn't make sense to send events.
      if ((mw != null) &&
          (mw.getState() != ESlateMicroworld.LOADING) &&
          (mw.getState() != ESlateMicroworld.CLOSING)) {
        ActiveHandleListenerBaseArray ahl;
        synchronized (listeners) {
          ahl = (ActiveHandleListenerBaseArray)(listeners.clone());
        }
        int n = ahl.size();
        for (int i=0; i<n; i++) {
          ActiveHandleListener l = ahl.get(i);
          l.activeHandleChanged(e);
        }
      }
      inFireListeners = false;
    }
  }

  /**
   * Returns the current active handle within the group.
   * @return    The current active handle within the group.
   */
  public ESlateHandle getActiveHandle()
  {
    return activeHandle;
  }

  /**
   * Checks if a specific E-Slate handle is part of the group.
   * @param     h       The handle to check.
   */
  public boolean containsHandle(ESlateHandle h)
  {
    return handles.containsKey(h);
  }

  /**
   * Returns the E-Slate handles that are part of this group.
   * @return    An array containing the E-Slate handles that are part of this
   *            group.
   */
  public ESlateHandle[] getHandles()
  {
    Set<ESlateHandle> s = handles.keySet();
    int n = s.size();
    ESlateHandle[] h = new ESlateHandle[n];
    Iterator<ESlateHandle> it = s.iterator();
    for (int i=0; i<n; i++) {
      h[i] = it.next();
    }
    return h;
  }

  /**
   * Adds a listener for changes in activation status.
   * @param     l       The listener to add.
   */
  public void addActiveHandleListener(ActiveHandleListener l)
  {
    synchronized (listeners) {
      if (!listeners.contains(l)) {
        listeners.add(l);
      }
    }
  }

  /**
   * Removes a listener for changes in activation status.
   * @param     l       The listener to remove.
   */
  public void removeActiveHandleListener(ActiveHandleListener l)
  {
    synchronized (listeners) {
      int i = listeners.indexOf(l);
      if (i >= 0) {
        listeners.remove(i);
      }
    }
  }

  /**
   * Returns the E-Slate handle of the component that is used as a base for
   * collecting E-Slate handles in this group.
   * @return    The E-Slate handle of the component that is used as a base for
   *            collecting E-Slate handles in this group.
   */
  public ESlateHandle getHandle()
  {
    return handle;
  }

  /**
   * Specifies that, as long as there is at least one E-Slate handle in the
   * goup, one of them will always be active, even if an active handle has not
   * been explicitly provided.
   * @param     flag    True if yes, false if no.
   */
  public void setActiveHandleAlwaysExists(boolean flag)
  {
    activeHandleAlwaysExists = flag;
    if (activeHandleAlwaysExists && (activeHandle == null)) {
      activateSomeHandle();
    }
  }

  /**
   * Checks whether, as long as there is at least one E-Slate handle in the
   * goup, one of them will always be active, even if an active handle has not
   * been explicitly provided.
   * @return    True if yes, false if no.
   */
  public boolean activeHandleAlwaysExists()
  {
    return activeHandleAlwaysExists;
  }

}
