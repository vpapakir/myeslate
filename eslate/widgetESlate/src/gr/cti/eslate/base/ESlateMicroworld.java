package gr.cti.eslate.base;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.math.*;
import java.net.*;
import java.util.*;

import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.help.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.registry.*;
import gr.cti.eslate.services.name.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.utils.help.*;
import gr.cti.structfile.*;
import gr.cti.typeArray.*;

import javax.swing.*;
import javax.swing.undo.*;

import javax.sound.sampled.*;

import javax.help.HelpSet;

/**
 * Implements the functionality of an E-Slate microworld,
 * i.e., component connectivity, and the table of E-Slate handles owned
 * by the components embedded into the current microworld accessible,
 * (the table) from all the components.
 *
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.23, 23-Jan-2008
 * @see         gr.cti.eslate.base.ESlateHandle
 */
@SuppressWarnings(value={"unchecked"})
public class ESlateMicroworld implements ESlatePart, Serializable
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Version string.
   */
  private final static String version = "2.0.23";

  /**
   * The folder where the microworld maintains help files.
   */
  private static DirFile helpDir;
  /**
   * Create the folder where the help files associated with the microworld
   * will be placed, and put it in the class path.
   */
  static {
    helpDir = DirFile.createTempDir(
      "MicroworldHelp_" + System.getProperty("user.language")
    );
    helpDir.deleteOnExit();
    String cp = System.getProperty("java.class.path") +
                System.getProperty("path.separator") +
                helpDir.getAbsolutePath();
    System.setProperty("java.class.path", cp);
  }

  /**
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
   * Returns the microworld's E-Slate handle.
   * @return    The requested E-Slate handle.
   */
  public ESlateHandle getESlateHandle()
  {
    return myHandle;
  }

  /**
   * Constuct an E-Slate microworld running under a given environment
   * (container).
   * @param     container       The environment (container) under which the
   *                            microworld is running. Te container can be
   *                            null.
   */
  public ESlateMicroworld(Object container)
  {
    //System.out.println("*** Clearing state");
    //PerformanceManager.getPerformanceManager().setState(null);
    // Set the current locale
    setCurrentLocale();

    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.base.ESlateResource", currentLocale
    );

    setMicroworldEnvironment(container);

    // Set the document base to the current directory.
    String currentDir =
      new File(System.getProperty("user.dir")).getAbsolutePath();
      documentBase = parentURL(currentDir + File.separator +  "dummy");

    masterHelpSet = null;
    helpSets = new Hashtable<Class, Object>();

    resetSelectedPlug();

    aliases = new NameServiceContext();
    aliases.setSeparator('\0'); // Essentially, no separator.
    stateListeners = new ArrayList<ComponentStateChangedListener>();
    //connectedPairs = new ConnectedPairBaseArray();
    primitiveGroupAddedListeners = new PrimitiveGroupAddedListenerBaseArray();
    componentAddedListeners = new ComponentAddedListenerBaseArray();
    componentRemovedListeners = new ComponentRemovedListenerBaseArray();
    undo = new PlugViewUndoManager();
    undoAction = new UndoAction();
    redoAction = new RedoAction();

    readIcon =
      new ImageIcon(ESlateMicroworld.class.getResource("readIcon.gif"));

    // Register the microworld with E-Slate.
    myHandle = ESlate.registerPart(this);
    myHandle.setInfo(getInfo());

    ahg = new ActivationHandleGroup(
      myHandle, null, ActivationHandleGroup.DIRECT_CHILDREN
    );
    ahg.addActiveHandleListener(new ActiveHandleListener()
    {
      public void activeHandleChanged(ActiveHandleEvent e)
      {
        setSelectedComponent(e.getNewActiveHandle());
      }
    });

//-    PerformanceManager pm = PerformanceManager.getPerformanceManager();
//-    PerformanceTimerGroup ptg = pm.getPerformanceTimerGroup(myHandle);
//-    saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
//-      ptg, "Save microworld", true
//-    );
//-    savePrepTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
//-      saveTimer, "Setup", true
//-    );
//-    saveStateTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
//-      saveTimer, "Save state", true
//-    );
//-    copyCompoDataTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
//-      saveTimer, "Copy component data", true
//-    );
//-    saveComposTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
//-      saveTimer, "Save components", true
//-    );

    // Assign a name to the microworld
    try {
      setUniqueName(resources.getString("microworld"));
    } catch (RenamingForbiddenException rfe) {
    }
    synchronized(microworlds) {
      microworlds.add(this);
    }
    setInfoWindowEnabled(true);

    buf = new byte[508];
  }

  /**
   * Constuct an E-Slate microworld without specifying the environment
   * (container) under which it is running.
   */
  public ESlateMicroworld()
  {
    this(null);
  }

  /**
   * Add a component state changed event listener.
   * @param     listener        The listener to add.
   */
  public void
    addComponentStateChangedListener(ComponentStateChangedListener listener)
  {
    synchronized(stateListeners) {
      if (!stateListeners.contains(listener)) {
        stateListeners.add(listener);
      }
    }
  }

  /**
   * Remove a component state changed event listener.
   * @param     listener        The listener to remove.
   */
  public void
    removeComponentStateChangedListener(ComponentStateChangedListener listener)
  {
    if (state != DEAD) {
      synchronized(stateListeners) {
        int i = stateListeners.indexOf(listener);
        if (i >= 0) {
          stateListeners.remove(i);
        }
      }
    }
  }

  /**
   * Invokes the appropriate method of the state changed event listeners.
   * @param     component       The component whose state has changed.
   * @param     what    Which of the various possible state changes has
   *                    occured. It can be one of COMPONENT_CREATED,
   *                    COMPONENT_FINALIZED, COMPONENT_INITIALIZED,
   *                    COMPONENT_STARTED, COMPONENT_STOPPED, and
   *                    COMPONENT_DESTROYED. This method is invoked
   *                    automatically by E-Slate for the first two types of
   *                    state change. For the remaining four, applets should
   *                    invoke this method at the end of their init(),
   *                    start(), stop(), and destroy(), methods, respectively.
   */
  public void fireComponentStateChangedListeners(Object component, int what)
  {
    if (what == COMPONENT_STARTED || what == COMPONENT_STOPPED) {
      ESlateHandle h = getComponentHandle(component);
      if (h != null) {
        if (what == COMPONENT_STARTED) {
          h.start();
        }else{
          h.stop();
        }
      }
    }
    if (stateListeners.size() > 0) {
      ArrayList<ComponentStateChangedListener> listeners;
      synchronized(stateListeners) {
        listeners =
          (ArrayList<ComponentStateChangedListener>)(stateListeners.clone());
      }
      int nListeners = listeners.size();
      for (int i=0; i<nListeners; i++) {
        ComponentStateChangedListener l = listeners.get(i);
        ComponentStateChangedEvent e =
          new ComponentStateChangedEvent(component);
        switch (what) {
          case COMPONENT_CREATED:
            l.componentCreated(e);
            break;
          case COMPONENT_INITIALIZED:
            l.componentInitialized(e);
            break;
          case COMPONENT_STARTED:
            l.componentStarted(e);
            break;
          case COMPONENT_STOPPED:
            l.componentStopped(e);
            break;
          case COMPONENT_DESTROYED:
            l.componentDestroyed(e);
            break;
          case COMPONENT_FINALIZED:
            l.componentFinalized(e);
            break;
        }
      }
    }
  }

  /**
   * Does the bookkeeping associated with the addition of a component to
   * the microworld. Invoke after adding the component's handle to the handle
   * of the microworld.
   * @param     handle          The E-Slate handle of the component.
   */
  void addComponent(ESlateHandle handle)
  {
    // If this is the first component of its class, add its help set to the
    // master help set.
    Class cl = handle.getComponent().getClass();
    int n = getTotalComponentCount(cl);
    if (n == 1) {
      mergeHelpSet(handle.getComponent().getClass());
    }
    // Invoke component added event listeners
    ComponentAddedEvent e = new ComponentAddedEvent(handle);
    fireComponentAddedListeners(e);
    assignHandleID(handle);
    // Invoke primitive group added listeners for the primitive groups
    // supported by the component and all its subcomponents.
    firePrimitiveGroupAddedListeners(handle);
  }

  /**
   * Assigns a handle ID to an E-Slate handle and to all its children that do
   * not already have a handle ID.
   * @param     handle  The handle to which to assign the ID.
   */
  void assignHandleID(ESlateHandle handle)
  {
    if (handle.handleId == null) {
      lastId = lastId.add(BigInteger.ONE);
      handle.handleId = lastId;
    }

    ESlateHandle[] children = handle.getChildHandles();
    int n = children.length;
    for (int i=0; i<n; i++) {
      assignHandleID(children[i]);
    }
  }

  /**
   * Merge the help set corresponding to a component with the master help set.
   * @param     cl      The class of the component.
   */
  private void mergeHelpSet(Class cl)
  {
    if (masterHelpSet == null) {
      // If the help set has not been displayed yet, just remember the
      // class, so that we can construct the help set when it is
      // displayed, thus avoiding the help start-up message at E-Slate
      // start-up.
      helpSets.put(cl, cl);
    }else{
      try {
        HelpSet hs = HelpSetLoader.load(cl);
        helpSets.put(cl, hs);
        masterHelpSet.add(hs);
      } catch (NoClassDefFoundError e) {
        // E-Slate help classes are not in class path.
      }
    }
  }

  /**
   * Does the bookkeeping associated with the removal of a component from
   * the microworld. Invoke after removing the component's handle from the
   * handle of the microworld.
   * @param     handle  The E-Slate handle of the component.
   */
  void removeComponent(ESlateHandle handle)
  {
    suppressAudio = true;
    removeAlias(handle);
    // If this was the last component of its class, remove its help set
    // from the master help set.
    Object compo = handle.getComponent();
    if (compo != null) {
      Class cl = compo.getClass();
      int n = getTotalComponentCount(cl);
      if (n == 0) {
        unmergeHelpSet(cl);
      }
    }
    //
    // Invoke component removed listeners
    ComponentRemovedEvent e = new ComponentRemovedEvent(handle);
    fireComponentRemovedListeners(e);
    undo.removeEdits(handle);
    suppressAudio = false;
  }

  /**
   * Remove the help set corresponding to a component from the master help set.
   * @param     cl      The class of the component.
   */
  private void unmergeHelpSet(Class cl)
  {
    if (masterHelpSet == null) {
      helpSets.remove(cl);
    }else{
      try {
        HelpSet hs = (HelpSet)(helpSets.get(cl));
        helpSets.remove(cl);
        if (hs != null) {
          masterHelpSet.remove(hs);
        }
      } catch (NoClassDefFoundError e) {
        // E-Slate help classes are not in class path.
      }
    }
  }

  /**
   * Disassociate a component from its alias.
   * @param     handle  The E-Slate handle of the component.
   */
  void removeAlias(ESlateHandle handle)
  {
    String alias = handle.getAlias();
    if (alias != null) {
      try {
        aliases.unbind(alias);
      } catch (NameServiceException nse) {
      }
    }
  }

  /**
   * Removes all the components from the microworld.
   */
  void removeAllComponents()
  {
    ESlateHandle[] handles = myHandle.getChildHandles();
    for (int i=0; i<handles.length; i++) {
      myHandle.remove(handles[i]);
      removeComponent(handles[i]);
    }
  }

  /**
   * Display help for all the components in the microworld.
   */
  public void showHelp()
  {
    try {
      // Construct the master help set if help is shown for the first time.
      if (masterHelpSet == null) {
        masterHelpSet = HelpSetLoader.getMasterHelpSet();
        Enumeration<Class> classes = helpSets.keys();
        ArrayList<Class> a = new ArrayList<Class>();
        while (classes.hasMoreElements()) {
          a.add(classes.nextElement());
        }
        int n = a.size();
        for (int i=0; i<n; i++) {
          Class cl = a.get(i);
          HelpSet hs = HelpSetLoader.load(cl);
          helpSets.remove(cl);
          helpSets.put(cl, hs);
          masterHelpSet.add(hs);
        }
      }
      if (viewer == null) {
        try {
          int width = 640;
          int height = 400;
          Point origin = centerBoxOrigin(width, height);
          viewer = ESlateHelpSystemViewer.createViewer(
            masterHelpSet, origin.x, origin.y,width, height
          );
        } catch (Exception e) {
          ESlateOptionPane.showMessageDialog(
            getMicroworldEnvironmentAsComponent(), e.getMessage(),
            resources.getString("error"), JOptionPane.ERROR_MESSAGE);
        }
      }else{
        if (!viewer.isVisible()) {
          viewer.setVisible(true);
        }
      }
    }catch (NoClassDefFoundError e) {
      // E-Slate help classes are not in class path.
      ESlateOptionPane.showMessageDialog(
        getMicroworldEnvironmentAsComponent(),
        resources.getString("noHelpSystem"),
        resources.getString("error"), JOptionPane.ERROR_MESSAGE
      );
    }
  }

  /**
   * Hide the window containing the help for all the components in the
   * microworld.
   */
  public void hideHelp()
  {
    try {
      if (viewer != null && viewer.isVisible()) {
        viewer.setVisible(false);
      }
    } catch (NoClassDefFoundError e) {
      // E-Slate help classes are not in class path.
    }
  }

  /**
   * Frees all resources associated with the microworld. After calling this
   * method, the microworld is unusable.
   */
  public void dispose()
  {
    setState(CLOSING);

    if (currentMicroworldStructFile != null) {
      //try {
      //  currentMicroworldStructFile.close();
      //} catch (Exception e) {
      //}
      currentMicroworldStructFile = null;
    }
    if (ptvWindow != null) {
      ((PlugViewDesktop)(ptvWindow.getContentPane())).dispose();
      ptvWindow.dispose();
      ptvWindow = null;
    }
    ptvShown = null;
    removeAllComponents();
    synchronized (microworlds) {
      microworlds.remove(microworlds.indexOf(this));
    }
    try {
      if (viewer != null) {
        viewer.setVisible(false);
        viewer.dispose();
        viewer = null;
      }
      masterHelpSet = null;
    }catch (NoClassDefFoundError e) {
      // E-Slate help classes are not in class path.
    }
    if (helpDir != null) {
      helpDir.clear();
    }
    helpSets.clear();
    helpSets = null;
    firstHandle = null;
    firstPlug = null;
    aliases.dispose();
    aliases = null;
    stateListeners.clear();
    stateListeners = null;
    //connectedPairs.clear();
    //connectedPairs = null;
    currentSaveFile = null;
    currentLoadFile = null;
    previousMicroworldFile = null;
    currentMicroworldFile = null;
    if (infoWindow != null) {
      hideInfoWindow();
    }
    readIcon = null;
    primitiveGroupAddedListeners.clear();
    primitiveGroupAddedListeners = null;
    componentAddedListeners.clear();
    componentAddedListeners = null;
    componentRemovedListeners.clear();
    componentRemovedListeners = null;
    ptvSize = null;
    viewportPos = null;
    ptvWinBounds = null;
    documentBase = null;
    container = null;
    undo.discardAllEdits();
    undo = null;
    undoAction = null;
    redoAction = null;
    lastId = null;
    buf = null;
    setState(DEAD);
    scriptingComponents.clear();
    scriptingComponents = null;
    origInputHandles.clear();
    origInputPlugNames.clear();
    targetInputHandles.clear();
    targetInputPlugNames.clear();
    origOutputHandles.clear();
    origOutputPlugNames.clear();
    targetOutputHandles.clear();
    targetOutputPlugNames.clear();
    if (myHandle != null) {
      myHandle.dispose();
      myHandle = null;
    }
    mwHelpDir = null;
    previousMicroworldStructFile = null;
  }

  /**
   * Returns the number of top-level components in the microworld.
   * @return    The number of components in the microworld.
   */
  public int getComponentCount()
  {
    return myHandle.getChildHandles().length;
  }

  /**
   * Returns the number of top-level components in the microworld that are
   * instances of a given class.
   * @param     c       The class in whose instances we are interested.
   * @return    The number of components in the microworld that are instances
   *            of the above class.
   */
  public int getComponentCount(Class c)
  {
    int count = 0;
    ESlateHandle[] handles = myHandle.getChildHandles();
    int nHandles = handles.length;
    for (int i=0; i<nHandles; i++) {
      Object o = handles[i].getComponent();
      if (c.isInstance(o)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Returns the number of components anywhere in the microworld component
   * hierarchy.
   * @return    The number of components in the microworld.
   */
  public int getTotalComponentCount()
  {
    ESlateHandle[] h = getAllHandlesInHierarchy();
    return h.length;
  }

  /**
   * Returns the number of components anywhere in the microworld component
   * hierarchy that are instances of a given class.
   * @param     c       The class in whose instances we are interested.
   * @return    The number of components in the microworld that are instances
   *            of the above class.
   */
  public int getTotalComponentCount(Class c)
  {
    int count = 0;
    ESlateHandle[] h = getAllHandlesInHierarchy();
    int nHandles = h.length;
    //ESlateHandle containerHandle = getMicroworldEnvironmentHandle();
    for (int i=0; i<nHandles; i++) {
      Object o = h[i].getComponent();
      if (c.isInstance(o)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Returns an array containing the names of all components in the
   * microworld.
   * @return    The requested array.
   * @deprecated        As of E-Slate 1.7.3, replaced by invoking
   * {@link gr.cti.eslate.base.ESlateHandle#getComponentName()} on each of the
   * handles returned by
   * {@link gr.cti.eslate.base.ESlateHandle#getChildHandles()}
   * invoked on the microworld's E-Slate handle.
   */
  public String[] getComponentNames()
  {
    ESlateHandle[] handles = myHandle.getChildHandles();
    int nHandles = handles.length;
    String[] names = new String[nHandles];
    for (int i=0; i<nHandles; i++) {
      names[i] = handles[i].getComponentName();
    }
    return names;
  }

  /**
   * Returns an array containing references to all the components in the
   * microworld.
   * @return    The requested array.
   * @deprecated        As of E-Slate 1.7.3, replaced by invoking
   * {@link gr.cti.eslate.base.ESlateHandle#getComponent()} on each of the
   * handles returned by
   * {@link gr.cti.eslate.base.ESlateHandle#getChildHandles()}
   * invoked on the microworld's E-Slate handle.
   */
  public Object[] getComponents()
  {
    ESlateHandle[] handles = myHandle.getChildHandles();
    int nHandles = handles.length;
    Object[] components = new Object[nHandles];
    for (int i=0; i<nHandles; i++) {
      components[i] = handles[i].getComponent();
    }
    return components;
  }

  /**
   * Returns an array containing references to the E-Slate handles of all
   * the top-level components in the microworld.
   * @deprecated        As of ESlate version 1.7.0, as its function has become
   *                    identical to that of {@link #getHandles()}.
   * @return    The requested array.
   */
  public ESlateHandle[] getAllHandles()
  {
    return myHandle.getChildHandles();
  }

  /**
   * Returns an array containing references to the E-Slate handles of all the
   * components in the microworld and those of their child components.
   * @return    The requested array.
   */
  public ESlateHandle[] getAllHandlesInHierarchy()
  {
    ESlateHandleBaseArray a = new ESlateHandleBaseArray();
    if (myHandle != null) {  // I.e., when we are still building myHandle.
      getHandlesFromHandle(myHandle, a, false);
    }
    int nHandles = a.size();
    ESlateHandle[] handles = new ESlateHandle[nHandles];
    for (int i=0; i<nHandles; i++) {
      handles[i] = a.get(i);
    }
    return handles;
  }

  /**
   * Recursively traverses an E-Slate handle, placing it and all its
   * children in a list.
   * @param     h       The E-Slate handle to traverse.
   * @param     a       The list where E-Slate handles will be placed.
   * @param     include If true, include <code>h</code> in the list, otherwise
   *                    omit it.
   */
  private void getHandlesFromHandle(
    ESlateHandle h, ESlateHandleBaseArray a, boolean include)
  {
    if (include) {
      a.add(h);
    }
    ESlateHandle[] hh = h.toArray();
    for (int i=0; i<hh.length; i++) {
      getHandlesFromHandle(hh[i], a, true);
    }
  }

  /**
   * Returns an array containing references to the E-Slate handles of all
   * the top-level components in the microworld.
   * @return    The requested array.
   * @deprecated        As of E-Slate 1.7.3, replaced by
   * {@link gr.cti.eslate.base.ESlateHandle#getChildHandles()}
   * invoked on the microworld's E-Slate handle.
   */
  public ESlateHandle[] getHandles()
  {
    return myHandle.getChildHandles();
  }

  /**
   * Returns the handle IDs of all components.
   * @return   The requested array.
   */
  private BigInteger[] getAllHandleIDs()
  {
    ESlateHandle h[] = myHandle.getChildHandles();
    int length = h.length;
    BigInteger id[] = new BigInteger[length];
    for (int i=0; i<length; i++) {
      id[i] = h[i].handleId;
    }
    return id;
  }

  /**
   * Renames a component in the microworld.
   * @param     oldName The name of the component to rename.
   * @param     newName The new name of the component.
   * @exception NameUsedException       Thrown when trying to rename a
   *                    component using a name that is being used by another
   *                    component.
   * @exception RenamingForbiddenException      This exception is thrown
   *                    when renaming the component is not allowed.
   * @exception NoSuchComponentException        Thrown if there is no
   *                    component named <code>oldname</code> in the
   *                    microworld.
   * @exception IllegalArgumentException        Thrown if either of the names
   *                    specified is null.
   * @deprecated        As of E-Slate 1.7.3, replaced by
   * {@link gr.cti.eslate.base.ESlateHandle#setComponentName(java.lang.String)}.
   */
  public void renameComponent(String oldName, String newName)
    throws NameUsedException, RenamingForbiddenException,
           NoSuchComponentException, IllegalArgumentException
  {
    if (oldName == null) {
      throw new IllegalArgumentException(resources.getString("nullOldName"));
    }
    if (newName == null) {
      throw new IllegalArgumentException(resources.getString("nullNewName"));
    }
    ESlateHandle h = myHandle.getChildHandle(oldName);
    if (h == null) {
      throw new NoSuchComponentException(
        resources.getString("noSuchComponent") + " " + oldName
      );
    }
    h.setComponentName(newName);
  }

  /**
   * Returns a reference to the component of the microworld that has a given
   * name.
   * @param     name The name of the component.
   * @return    The requested component. If there is no such component in the
   *            microworld, this method returns null.
   * @deprecated        As of E-Slate 1.7.3, replaced by
   * {@link gr.cti.eslate.base.ESlateHandle#getComponent()} invoked on the
   * E-Slate handle returned by invoking
   * {@link gr.cti.eslate.base.ESlateHandle#getChildHandle(java.lang.String)}
   * on the microworld's E-Slate handle.
   */
  public Object getComponent(String name)
  {
    ESlateHandle handle = myHandle.getChildHandle(name);
    if (handle != null) {
      return handle.getComponent();
    }else{
      return null;
    }
  }

  /**
   * Returns a reference to the handle of a component connected to E-Slate
   * that has a given name. This name can be either a simple name, e.g.,
   * "component", or a path name relative to this component's hierarchy, e.g.,
   * "component.subcomponent.subsubcomponent".
   * @param     name    The name of the component.
   * @return    The component or null if there was no component having
   *            the given name.
   * @deprecated        As of E-Slate 1.7.3, replaced by
   * {@link gr.cti.eslate.base.ESlateHandle#getChildHandle(java.lang.String)}
   * invoked on the microworld's E-Slate handle.
   */
  public ESlateHandle getComponentHandle(String name)
  {
    return myHandle.getChildHandle(name);
  }

  /**
   * Returns a reference to the handle of a component connected to E-Slate
   * that has a given name. This name is an absolute path in the component
   * hierarchy, e.g, "topcomponent.child.innerchild".
   * @param     name    The name of the component.
   * @return    The component or null if there was no component having
   *            the given name.
   */
  ESlateHandle getComponentHandleFromTop(String name)
  {
    ESlateHandle h;

    int ind = name.indexOf(ESlateHandle.SEPARATOR);
    String firstName;
    if (ind >= 0) {
      firstName = name.substring(0, ind);
      name = name.substring(ind + 1);
    }else{
      firstName = name;
    }
    ESlateHandle rootHandle = myHandle.getRootHandle();
    boolean sameFirstName = ESlateStrings.areEqualIgnoreCase(
      firstName, rootHandle.getComponentName(), currentLocale
    );
    if (sameFirstName) {
      try {
        h = (ESlateHandle)(rootHandle.childComponents.lookup(name));
      } catch (NameServiceException nse) {
        h = null;
      }
    }else{
      h = null;
    }
/*
    if (rootHandle.childComponents != null) {
      try {
        h = (ESlateHandle)(rootHandle.childComponents.lookup(name));
      } catch (NameServiceException nse) {
        h = null;
      }
    }else{
      h = null;
    }
*/
    return h;
  }

  /**
   * Returns a reference to the component of the microworld that has a given
   * alias.
   * @param     name    The alias of the component.
   * @return    The requested component. If there is no such component in the
   *            microworld, this method returns null.
   */
  public Object getComponentByAlias(String name)
  {
    ESlateHandle handle = getComponentHandleByAlias(name);
    if (handle != null) {
      return handle.getComponent();
    }else{
      return null;
    }
  }

  /**
   * Returns a reference to the handle of a component connected to E-Slate
   * that has a given alias.
   * @param     name    The alis of the component.
   * @return    The component or null if there was no component having
   *            the given alias.
   */
  public ESlateHandle getComponentHandleByAlias(String name)
  {
    try {
      return (ESlateHandle)(aliases.lookup(name));
    } catch (NameServiceException nse) {
      return null;
    }
  }

  /**
   * Keeps a reference to the E-Slate handle of the first of the two
   * components selected for connection. When the user selects the first of
   * two plugs that he wants to connect, E-Slate stores a reference to the
   * object that owns that plug by invoking this method.
   * @param     fc      The E-Slate handle of the object that owns the first
   *                    of the two plugs selected by the user who wants to
   *                    connect them.
   */
  static void setFirstComponent(ESlateHandle fc)
  {
    if (fc != null) {
      firstHandle = fc;
    }else{
      System.err.println("Invalid firstHandle");
    }
  }

  /**
   * Keeps a reference to the first of the two plugs selected for connection.
   * When the user selects the first of two plugs that he wants to connect,
   * E-Slate stores a reference to that plug by invoking this method.
   * @param     p       The first of the two plugs selected by the user who
   *                    wants to connect them.
   */
  private static void setFirstPlug(Plug p)
  {
    if (p != null) {
      firstPlug = p;
    }else{
      System.err.println("Invalid firstPlug");
    }
  }

  /**
   * Establish a connection between two components.
   * Invoking this method is equivalent to selecting the plug from the plug
   * menu. To connect two plugs, invoke this method twice, once for each plug.
   * Any warning dialog that appears will be attached to the plug view button
   * of the component's plug view button. The connection will be logged for
   * undo/redo.
   * @param     p       The plug that is to be connected.
   */
  public static void connectComponent(Plug p)
  {
    connectComponent(p, p.getHandle().getWarningComponent());
  }

  /**
   * Establish a connection between two components.
   * Invoking this method is equivalent to selecting the plug from the plug
   * menu. To connect two plugs, invoke this method twice, once for each plug.
   * The connection will be logged for undo/redo.
   * @param     p                       The plug that is to be connected.
   * @param     warningComponent        The AWT component to which any warning
   *                                    dialog that appears will be attached.
   */
  public static synchronized void connectComponent(
    Plug p, Component warningComponent)
  {
    connectComponent(p, warningComponent, true);
  }

  /**
   * Establish a connection between two components.
   * Invoking this method is equivalent to selecting the plug from the plug
   * menu. To connect two plugs, invoke this method twice, once for each plug.
   * @param     p                       The plug that is to be connected.
   * @param     warningComponent        The AWT component to which any warning
   *                                    dialog that appears will be attached.
   * @param     log                     Indicates whether the connection will
   *                                    be logged for undo/redo.
   */
  public static synchronized void connectComponent(
    Plug p, Component warningComponent, boolean log)
  {
    ESlateHandle handle = p.getHandle();

    if (firstPlug == null) {
      // I feel confident that handle is null too
      setFirstComponent(handle);
      setFirstPlug(p);
    }else{
      // This is the second component-plug.
      //
      // The shared object will be provided by one of the two components.
      // If it is not provided by the first component, try swapping it
      // with the second.
      SharedObject so;
      SharedObject so2;
      Plug secondPlug = p;
      so = firstPlug.internals.getSharedObject();
      so2 = secondPlug.internals.getSharedObject();
      ESlateHandle secondHandle = handle;
      ESlateMicroworld mw = p.getHandle().getESlateMicroworld();
      if (so == null && so2 != null) {
        ESlateHandle tc = firstHandle;
        Plug tp = firstPlug;
        firstHandle = secondHandle;
        firstPlug = secondPlug;
        secondHandle = tc;
        secondPlug = tp;
        so = so2;
      }
      // If the two components are already connected
      // then disconnect them
      if (firstPlug.isConnected(secondPlug)) {
        // Disconnect shared object part of the connection
        if (firstPlug.containsDependent(secondPlug) &&
            secondPlug.containsProvider(firstPlug))  {
          if (so != null) {
            so.removeSharedObjectChangedListener(
              secondPlug.internals.sharedObjectListener);
          }
          firstPlug.removeDependent(secondPlug);
          secondPlug.removeProvider(firstPlug);
        }
        if (so == null && secondPlug.containsDependent(firstPlug) &&
                          firstPlug.containsProvider(secondPlug)) {
          // For pure protocol plugs the terms "provider" and "dependent"
          // are only used for bookkeeping, and are essentially assigned
          // randomly to the two connected components. To make sure that
          // the providers and dependents lists are updated correctly,
          // do the provider/dependent disassociation both ways.
          // (The methods are no-ops if there is nothing to remove, so
          // calling them in both directions is safe.)
          secondPlug.removeDependent(firstPlug);
          firstPlug.removeProvider(secondPlug);
        }
        // Disconnect protocol part of the connection
        if (firstPlug.internals.hasProtocolPlug(secondPlug)) {
          firstPlug.internals.removeProtocolPlug(secondPlug);
        }
        if (secondPlug.internals.hasProtocolPlug(firstPlug)) {
          secondPlug.internals.removeProtocolPlug(firstPlug);
        }

        // Make copies of the two plugs. If there are subplugs to remove
        // and firstPlug is not null, then connectComponents()
        // will attempt to disconnect firstPlug from the first subplug
        // that should be disconnected. Thus, one of the two
        // calls to connectComponents() will not be consumed, and
        // the disconnection will fail as well.
        // Thus, we make local copies of the two plugs, and use these
        // for any remaining actions.
        Plug p1 = firstPlug;
        Plug p2 = secondPlug;
        ESlateHandle h1 = firstHandle;
        ESlateHandle h2 = secondHandle;

        // Reset
        resetSelectedPlug();


        // Notify the components
        if (p1.isOnlyProtocolPlug()) {
          p1.fireDisconnectionListeners(
            p2, Plug.PROTOCOL_CONNECTION);
          p2.fireDisconnectionListeners(
            p1, Plug.PROTOCOL_CONNECTION);
        }else{
          p1.fireDisconnectionListeners(
            p2, Plug.OUTPUT_CONNECTION);
          p2.fireDisconnectionListeners(
            p1, Plug.INPUT_CONNECTION);
        }

        // Update connected pairs vector. Do this before breaking any hosting
        // relationships, so that hostedComponentsConnections() returns the
        // correct value.
        connectedPairs.removeElements(new ConnectedPair(h1, h2, p1, p2));

        ESlateHandle r1 = h1.getRootComponentHandle();
        ESlateHandle r2 = h2.getRootComponentHandle();

        // Make a list of all the E-Slate handles from the root of the
        // component hierarchy to each of the disconnected components.
        // Do this at this point, before possibly making any disconnections,
        // as we will not be able to construct this list after that.
        ArrayList<ESlateHandle> hh = new ArrayList<ESlateHandle>();
        for (ESlateHandle h=h1; h!=null; h=h.getParentHandle()) {
          if (!hh.contains(h)) {
            hh.add(h);
          }
        }
        for (ESlateHandle h=h2; h!=null; h=h.getParentHandle()) {
          if (!hh.contains(h)) {
            hh.add(h);
          }
        }

        // If the plugs participate in the hosting mechanism, undo
        // the appropriate hosting relationship.
        if (p1.internals.isHostingPlug() &&
            p2.internals.isHostingPlug()) {
          ESlateHandle host, hosted;
          if (p1.internals.plugRole == Plug.LEFT_ROLE) {
            host = h1;
            hosted = h2;
          }else{
            host = h2;
            hosted = h1;
          }
          if (host.contains(hosted)) {
            host.remove(hosted);
          }else{
            host.unhost(hosted);
          }
        }

        // Invalidate the plug view and plug menu of all the components from
        // the root of the component hierarchy to each of the disconnected
        // components, so that whichever menu is displayed will show the
        // plugs of the corresponding component correctly. Also reload the
        // tree models of all these components, so that whichever is
        // displayed on the plug editor is updated.
        int n = hh.size();
        for (int i=0; i<n; i++) {
          ESlateHandle h = hh.get(i);
          if (!h.isDisposed()) {
            h.redoPlugView = true;
            h.redoPlugMenu = true;
            h.reloadModels();
          }
        }

        // Update plug tree view
        if ((mw != null) && (mw.ptvWindow != null)) {
          mw.getPlugViewDeskTop().getContentPane().updatePlugViews();
        }
        if (!r1.isDisposed()) {
          r1.repaintTrees();
          r1.makeVisible(p1.getPlugTreeNode());
        }
        if (!r2.isDisposed()) {
          r2.repaintTrees();
          r2.makeVisible(p2.getPlugTreeNode());
        }
        // Provide an aural feedback to the user
        if (!suppressAudio) {
          if (((mw == null) || ((mw.state != LOADING) && (mw.state != SAVING)))
              && !h1.disposeCalled.booleanValue()
              && !h2.disposeCalled.booleanValue()) {
            playSound(resources.getString("disconnectSound"));
          }
        }

        // Inform components that they must reconstruct their plug view
        // window and plug menu, as some of their plugs' icons may have
        // changed because of the disconnection.
        if (r1 != null) {
          r1.redoPlugView = true;
          r1.redoPlugMenu = true;
        }
        if (r2 != null) {
          r2.redoPlugView = true;
          r2.redoPlugMenu = true;
        }

        // Update undo manager
        if ((mw != null) && log) {
          mw.undo.addEdit(mw.createPlugViewUndoableEdit(false, p1, p2));
          mw.undoAction.updateUndoState();
          mw.redoAction.updateRedoState();
        }
      }else{
        // Check plug compatibility
        int plugCompatibility = Plug.plugCompatibility(firstPlug, secondPlug);
        if (plugCompatibility != Plug.COMPATIBLE) {
          // Provide an aural feedback to the user
          if (!suppressAudio) {
            if (((mw == null) || ((mw.state != LOADING) && (mw.state != SAVING)))
                && !firstHandle.disposeCalled.booleanValue()
                && !secondHandle.disposeCalled.booleanValue()) {
              playSound(resources.getString("cantconnectSound"));
            }
          }
          switch (plugCompatibility) {
            case Plug.ALREADY_CONNECTED:
              Plug cp;
              if (firstPlug.isSingleConnectionProtocolPlug() &&
                  firstPlug.internals.protocolPlugs.size() > 0) {
                cp = firstPlug;
              }else{
                cp = secondPlug;
              }
              ESlateOptionPane.showMessageDialog(
                warningComponent,
                resources.getString("singleConnected1") +
                  cp.toString() +
                  resources.getString("singleConnected2"),
                resources.getString("error"), JOptionPane.ERROR_MESSAGE);
              break;
            case Plug.SAME_PLUG:
              ESlateOptionPane.showMessageDialog(
                warningComponent,
                resources.getString("notSelf"),
                resources.getString("error"), JOptionPane.ERROR_MESSAGE);
              break;
            case Plug.INCOMPATIBLE:
              JPanel messagePanel = new JPanel();
              messagePanel.setLayout(
                new BoxLayout(messagePanel, BoxLayout.Y_AXIS)
              );
              JLabel lab = new JLabel(resources.getString("incompatible1"));
              lab.setAlignmentX(Component.CENTER_ALIGNMENT);
              messagePanel.add(lab);
              lab = new JLabel(
                new ImageIcon(ESlateMicroworld.class.getResource("compat.gif"))
              );
              lab.setAlignmentX(Component.CENTER_ALIGNMENT);
              messagePanel.add(lab);
              lab = new JLabel(resources.getString("incompatible2"));
              lab.setAlignmentX(Component.CENTER_ALIGNMENT);
              messagePanel.add(lab);
              ESlateOptionPane.showMessageDialog(
                warningComponent,
                messagePanel,
                resources.getString("error"), JOptionPane.ERROR_MESSAGE);
              break;
            case Plug.NO_HANDLE:
              ESlateOptionPane.showMessageDialog(
                warningComponent,
                resources.getString("noHandle"),
                resources.getString("error"), JOptionPane.ERROR_MESSAGE);
              break;
            case Plug.DIFFERENT_MICROWORLDS:
              ESlateOptionPane.showMessageDialog(
                warningComponent,
                resources.getString("differentMicroworlds"),
                resources.getString("error"), JOptionPane.ERROR_MESSAGE);
              break;
          }
          // Reset
          resetSelectedPlug();
        }else{
          // connect the two components

          // First check if the two components implement any optional
          // interfaces.
          Vector optInt1 = new Vector();
          Vector optInt2 = new Vector();
          // Optional interfaces implemented by the first component
          Vector opt2 = secondPlug.internals.optionalInterfaces;
          int nOpt2 = opt2.size();
          //Object implementor1 = firstPlug.internals.getProtocolImplementor();
          Vector exported1 = firstPlug.internals.getExportedInterfaces();
          for (int i=0; i<nOpt2; i++) {
            Class c = (Class)(opt2.elementAt(i));
            if (exported1.contains(c)) {
              optInt1.addElement(c);
            }
          }
          // Optional interfaces implemented by the second component
          Vector opt1 = firstPlug.internals.optionalInterfaces;
          int nOpt1 = opt1.size();
          //Object implementor2 = secondPlug.internals.getProtocolImplementor();
          Vector exported2 = firstPlug.internals.getExportedInterfaces();
          for (int i=0; i<nOpt1; i++) {
            Class c = (Class)(opt1.elementAt(i));
            if (exported2.contains(c)) {
              optInt2.addElement(c);
            }
          }

          // Check if the two plugs are pure protocol plugs and there is a
          // protocol defined between them. If not, the plugs cannot be
          // coonected.
          if (firstPlug.isOnlyProtocolPlug() &&
              secondPlug.isOnlyProtocolPlug() &&
              (firstPlug.internals.requiredInterfaces.size() == 0) &&
              (secondPlug.internals.requiredInterfaces.size() == 0) &&
              (optInt1.size() == 0) &&
              (optInt2.size() == 0) ) {
            ESlateOptionPane.showMessageDialog(
              warningComponent,
              resources.getString("noProtocolDefined"),
              resources.getString("error"), JOptionPane.ERROR_MESSAGE);
           }else{
            //
            if (so == null &&
                (!firstPlug.isOnlyProtocolPlug() ||
                 !secondPlug.isOnlyProtocolPlug())) {
              ESlateOptionPane.showMessageDialog(
                warningComponent,
                resources.getString("noShobjEither"),
                resources.getString("error"), JOptionPane.ERROR_MESSAGE);
            }else{
               if (so != null) {
                 // Set the listener
                 so.addSharedObjectChangedListener(
                   secondPlug.internals.sharedObjectListener);
               }
              // For pure protocol plugs the term provider and dependent does
              // not apply. However, we arbitrarily name the first plug as
              // provider and the second as dependent, so that we can identify
              // connected plug pairs when saving microworlds.

              // Set the provider
              firstPlug.addDependent(secondPlug);
              // Set the depenedent
              secondPlug.addProvider(firstPlug);

              // Set the protocol plugs
              // ...both tests should be true...
              if (firstPlug.isProtocolPlug()) {
                secondPlug.internals.addProtocolPlug(firstPlug);
              }
              if (secondPlug.isProtocolPlug()) {
                firstPlug.internals.addProtocolPlug(secondPlug);
              }

              // If the plugs participate in the hosting mechanism, establish
              // the appropriate hosting relationship.
              if (firstPlug.internals.isHostingPlug() &&
                  secondPlug.internals.isHostingPlug()) {
                ESlateHandle host, hosted;
                if (firstPlug.internals.plugRole == Plug.LEFT_ROLE) {
                  host = firstHandle;
                  hosted = secondHandle;
                }else{
                  host = secondHandle;
                  hosted = firstHandle;
                }
                if ((hosted.getParentHandle() == null) &&
                    !hosted.isDesktopComponent()) {
                  host.add(hosted);
                }else{
                  host.host(hosted);
                }
              }

              // Make copies of the two plugs. If The user decides to
              // break the connection from a connection listener,
              // and firstPlug is not null, then connectComponents()
              // will attempt to disconnect firstPlug from the plug
              // that the user attempted to disconnect. Thus, one of the two
              // calls to connectComponents() will not be consumed, and
              // depending on which of the two plugs the user tried to
              // disconnect, the disconnection may fail as well.
              // Thus, we make local copies of the two plugs, and use these
              // for any remaining actions.
              Plug p1 = firstPlug;
              Plug p2 = secondPlug;
              ESlateHandle h1 = firstHandle;
              ESlateHandle h2 = secondHandle;

              // Update connected pairs vector
              connectedPairs.add(new ConnectedPair(h1, h2, p1, p2));

              // Reset
              resetSelectedPlug();

              // Update plug tree view
              if ((mw != null) && (mw.ptvWindow != null)) {
                mw.getPlugViewDeskTop().getContentPane().updatePlugViews();
              }
              ESlateHandle r1 = h1.getRootComponentHandle();
              ESlateHandle r2 = h2.getRootComponentHandle();
              r1.repaintTrees();
              r1.makeVisible(p1.getPlugTreeNode());
              if (!(r1.equals(r2))) {
                r2.repaintTrees();
                r2.makeVisible(p2.getPlugTreeNode());
              }

              // Notify the components.
              if (p1.isOnlyProtocolPlug()) {
                p1.fireConnectionListeners(
                  p2, Plug.PROTOCOL_CONNECTION, optInt2);
                p2.fireConnectionListeners(
                  p1, Plug.PROTOCOL_CONNECTION, optInt1);
              }else{
                // Mark the exported shared object as currently being
                // processed. This way, if the second component reads
                // it in the connection listener, and transmits it to other
                // components, the first component will not retransmit it,
                // resulting in an extra transmission before a loop is
                // detected.
                so.markAsUsedInCurrentThread();
                // First fire the connection listeners of the input plug,
                // so that they may get a chance to add a shared object
                // event listener to get the value of the output plug at
                // connection time.
                p2.fireConnectionListeners(
                  p1, Plug.INPUT_CONNECTION, optInt1);
                // Now unmark the shared object, so that it can be
                // transmitted to other components.
                so.unmarkAsUsedInCurrentThread();
                p1.fireConnectionListeners(
                  p2, Plug.OUTPUT_CONNECTION, optInt2);
              }
              // Provide an aural feedback to the user
              if (!suppressAudio) {
                if (((mw == null) || ((mw.state != LOADING) && (mw.state != SAVING)))
                    && !h1.disposeCalled.booleanValue()
                    && !h2.disposeCalled.booleanValue()) {
                  playSound(resources.getString("connectSound"));
                }
              }

              // Inform components that they must reconstruct their plug view
              // window and plug menu, as some of their plugs' icons may have
              // changed because of the connection.
              r1.redoPlugView = true;
              r1.redoPlugMenu = true;
              r2.redoPlugView = true;
              r2.redoPlugMenu = true;

              // Update undo manager
              if ((mw != null) && log) {
                mw.undo.addEdit(mw.createPlugViewUndoableEdit(true, p1, p2));
                mw.undoAction.updateUndoState();
                mw.redoAction.updateRedoState();
              }
            }
          }
        }
      }
    }
  }

  /**
   * Attempts to connect two plugs if they are not connected, or to disconnect
   * them if they are connected.
   * @param     p1      The first plug.
   * @param     p2      The second plug.
   */
  public static void toggleConnect(Plug p1, Plug p2)
  {
    if (p1 != null && p2 != null) {
      resetSelectedPlug();
      suppressAudio = true;
      connectComponent(p1);
      connectComponent(p2);
      suppressAudio = false;
    }
  }

  /**
   * Plays a sound sample.
   * @param     name    The name of the resource (file) containing the sound
   *                    sample.
   */
  private static void playSound(String name)
  {
    // This code has been ripped from the JDK 1.3 sound demo. Do not use the
    // simpler Applet.newAudioClip() approach, as this takes over the sound
    // system, preventing other applications from using it while we are
    // running.
    try {
      AudioInputStream ais = AudioSystem.getAudioInputStream(
        ESlateMicroworld.class.getResource(name)
      );
      AudioFormat format = ais.getFormat();
      // We can't yet open the device for ALAW/ULAW playback,
      // convert ALAW/ULAW to PCM
      if ((format.getEncoding() == AudioFormat.Encoding.ULAW) ||
        (format.getEncoding() == AudioFormat.Encoding.ALAW))
      {
        AudioFormat tmp = new AudioFormat(
          AudioFormat.Encoding.PCM_SIGNED,
          format.getSampleRate(),
          format.getSampleSizeInBits() * 2,
            format.getChannels(),
          format.getFrameSize() * 2,
          format.getFrameRate(),
          true
        );
        ais = AudioSystem.getAudioInputStream(tmp, ais);
        format = tmp;
      }
      DataLine.Info info = new DataLine.Info(
        Clip.class,
        ais.getFormat(),
        ((int) ais.getFrameLength() * format.getFrameSize())
      );
      Clip clip = (Clip) AudioSystem.getLine(info);
      clip.open(ais);
      clip.start();
      //Thread thread = Thread.currentThread();
      try { Thread.sleep(99); } catch (Exception e) { }
      while (clip.isActive()) {
        try { Thread.sleep(99); } catch (Exception e) {break;}
      }
      clip.stop();
      clip.close();
      ais.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }

  /**
   * Displays the plug tree view window.
   */
  public void showPlugViewWindow()
  {
    boolean populate = false;
    synchronized(ptvShown) {
      if (ptvWindow == null) {
        populate = true;
        ptvWindow = new JFrame(resources.getString("plugView"));
        ptvWindow.setIconImage(ESlate.getIconImage());
        ptvWindow.pack();       // To initialize frame's insets.
        if (ptvWinBounds != null) {
          ptvWidth = ptvWinBounds.width;
          ptvHeight = ptvWinBounds.height;
        }
        ptvWindow.setSize(ptvWidth, ptvHeight);
        PlugViewDesktop pvd = new PlugViewDesktop(this);
        ptvWindow.setContentPane(pvd);
        int menuHeight = pvd.getMenuBar().getPreferredSize().height;
        JScrollPane scrollPane = pvd.getScrollPane();
        Insets ptvInsets = ptvWindow.getInsets();
        Insets spInsets = scrollPane.getInsets();
        int pvdHeight = ptvHeight - ptvInsets.top - ptvInsets.bottom
                                  - spInsets.top - spInsets.bottom
                                  - menuHeight;
        int pvdWidth = ptvWidth - ptvInsets.left - ptvInsets.right
                                - spInsets.left - spInsets.right;
        PlugViewDesktopPane ptv =
          plugTreeView(new Dimension(pvdWidth, pvdHeight));
        pvd.setContentPane(ptv);
        pvd.setResizeFrames(resizeFrames);
        pvd.setHighlightingExistingConnections(showExist);
        pvd.setHighlightingPossibleConnections(showNew);
        pvd.setAutoOpen(autoOpen);
        pvd.setDelayedAutoOpen(delayedAutoOpen);
        pvd.setAutoOpenCompatible(autoOpenCompatible);
        ptvWindow.setBackground(ptv.getBackground());
        ptvWindow.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e)
          {
            ptvShown = Boolean.FALSE;
          }
        });
        ptvWindow.getRootPane().registerKeyboardAction(
          new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
              ptvWindow.setVisible(false);
              ptvShown = Boolean.FALSE;
            }
          },
          KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false),
          JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        // Center Plug tree view window.
        Point ptvOrg = centerBoxOrigin(ptvWidth, ptvHeight);
        ptvWindow.setLocation(ptvOrg.x, ptvOrg.y);
      }
      if (!ptvShown.booleanValue()) {
        ptvWindow.setVisible(true);
        if (viewportPos != null) {
          JScrollPane scrollPane =
            ((PlugViewDesktop)(ptvWindow.getContentPane())).getScrollPane();
          scrollPane.getViewport().setViewPosition(viewportPos);
          scrollPane.revalidate();
        }
        if (ptvWinBounds != null) {
          ptvWindow.setBounds(ptvWinBounds);
        }
        ptvShown = Boolean.TRUE;
      }else{
        ptvWindow.setState(Frame.NORMAL);
        ptvWindow.toFront();
      }
      if (populate) {
        ignoreActivations = true;
        populatePlugTreeView(
          ((PlugViewDesktop)(ptvWindow.getContentPane())).getContentPane()
        );
        ignoreActivations = false;
      }
      activateComponent(ahg.getActiveHandle());
    }
  }

  /**
   * Hides the plug view window.
   */
  protected void hidePlugViewWindow()
  {
    if (ptvWindow !=null && ptvShown.booleanValue()) {
      ptvWindow.setVisible(false);
      ptvShown = Boolean.FALSE;
    }
  }

  /**
   * Returns the coordinates where the top left corner of a box should be
   * placed, so that it appears centered.
   * @param     width   The width of the box.
   * @param     height  The height of the box.
   * @return    The requested coordinates.
   */
  Point centerBoxOrigin(int width, int height)
  {
    Object environment = getMicroworldEnvironment();
    Component container = null;
    if (environment != null && environment instanceof Component) {
      container = (Component)environment;
    }
    int x, y;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;
    if (container == null || !container.isVisible()) {
      x = (screenWidth - width) / 2;
      y = (screenHeight - height) / 2;
    }else{
      int containerWidth = container.getWidth();
      int containerHeight = container.getHeight();
      Point containerLocation = container.getLocationOnScreen();
      x = containerLocation.x + (containerWidth - width) / 2;
      y = containerLocation.y + (containerHeight - height) / 2;
      if ((x + width) > screenWidth) {
        x = screenWidth - width;
      }
      if ((y + height) > screenHeight) {
        y = screenHeight - height;
      }
      if (x < 0) {
        x = 0;
      }
      if (y < 0) {
        y = 0;
      }
    }
    return new Point(x, y);
  }

  /**
   * Creates a new structured file for saving the currently active microworld.
   * This consists of the set of E-Slate components being displayed on the
   * current browser page (or by appletviewer) or the set of E-Slate
   * components running as applications under the same Java VM.
   * @param     saveFile        The file to which the microworld will be
   *                            saved.
   * @exception WritingException        Thrown if something goes wrong while
   *                                    creating the file.
   * @return    A StructFile where the caller may write additional
   *            information, and on which it should invoke saveMicroworld().
   *            If saving fails, this will be null. When finishing saving
   *            the microworld, the container should indicate this to the
   *            platform by invoking <code>finishedLoadingOrSaving()</code>.
   */
  public StructFile createMicroworldFile(String saveFile)
    throws WritingException
  {
    showInfoWindow(
      resources.getString("saving"),
      null,
      resources.getString("pleaseWait")
    );

    currentSaveFile = saveFile;

    resetSelectedPlug();        // Ignore any plug the user may have selected
                                // as a first plug before loading.
    StructFile sf = null;
    String oldName = null;
    String newName = null;
    if (currentMicroworldFile != null) {
      oldName = currentMicroworldFile.getAbsolutePath();
    }
    newName = new File(saveFile).getAbsolutePath();
    // If we are saving on the same microworld file, simply erase any previous
    // component info, rather than creating the file anew, so that the
    // component data area is preserved.
    if ((oldName != null) && oldName.equals(newName)) {
      sf = currentMicroworldStructFile;
      try {
        sf.changeToRootDir();
        try {
          sf.createDir(COMPONENT_INFO);
        } catch (IOException ioe) {
          // If saving on the same microworld file had failed, then the
          // component info folder may not have had a chance to be created,
          // so it is possible that the deletion may fail.
        }
        sf.changeToRootDir();
        previousMicroworldFile = currentMicroworldFile;
      } catch (Exception e) {
        hideInfoWindow();
        String message = e.getMessage();
        if (message == null) {
          message = resources.getString("cantDeleteInfo");
        }
        throw new WritingException(message, e);
      }
    }else{
      try {
        sf = new StructFile(saveFile, StructFile.NEW);
        sf.createDir(COMPONENT_INFO);
        sf.changeToRootDir();
      } catch (Exception e) {
        hideInfoWindow();
        String message = e.getMessage();
        if (message == null) {
          message = resources.getString("cantCreate1") +
                    saveFile +
                    resources.getString("cantCreate2");
        }
        throw new WritingException(message, e);
      }
      previousMicroworldFile = currentMicroworldFile;
      currentMicroworldFile = new File(saveFile);
      //if (currentMicroworldStructFile != null) {
      //  try {
      //    currentMicroworldStructFile.close();
      //  } catch (IOException ioe) {
      //  }
      //}
      previousMicroworldStructFile = currentMicroworldStructFile;
      currentMicroworldStructFile = sf;
    }
    // Moved to saveMicroworld.
    // setState(SAVING);
    isSaving = true;
    return sf;
  }

  /**
   * Saves a microworld to a structured file. When invoking this method from
   * outside a container, the microworld is considered to
   * consist of the set of E-Slate components being displayed on the
   * current browser page (or by appletviewer) or the set of E-Slate
   * components running as applications under the same Java VM.
   * @param     sf      The structured file to which the microworld will be
   *                    saved. It must have been opened with
   *                    createMicroworldFile(), which adds the correct header
   *                    to the file, identifying it as a saved microworld
   *                    file.
   * @exception WritingException        Thrown if something goes wrong while
   *                                    writing to the file.
   * @exception PartialWriteException   Thrown if the microworld was only
   *                                    partially saved.
   */
  public void saveMicroworld(StructFile sf)
    throws WritingException, PartialWriteException
  {
    saveMicroworld(sf, null);
  }

  /**
   * Saves a microworld to a structured file. When invoking this method from
   * outside a container, the microworld is considered to
   * consist of the set of E-Slate components being displayed on the
   * current browser page (or by appletviewer) or the set of E-Slate
   * components running as applications under the same Java VM.
   * @param     sf      The structured file to which the microworld will be
   *                    saved. It must have been opened with
   *                    createMicroworldFile(), which adds the correct header
   *                    to the file, identifying it as a saved microworld
   *                    file.
   * @param     pBar    A progress bar to update while saving the microworld.
   *                    This can be null, if no progress bar should be
   *                    updated.
   * @exception WritingException        Thrown if something goes wrong while
   *                                    writing to the file.
   * @exception PartialWriteException   Thrown if the microworld was only
   *                                    partially saved.
   */
  public void saveMicroworld(StructFile sf, ProgressBarInterface pBar)
    throws WritingException, PartialWriteException
  {
//-    PerformanceManager pm = PerformanceManager.getPerformanceManager();
//-    pm.init(saveTimer);
//-    pm.init(savePrepTimer);
    setState(SAVING);

    boolean havePBar;
    int pBarMin = 0;
    int pBarMax = 0;
    int pBarValue = 0;
    int pBarSteps = 0;
    int pBarAvail = 0;
    int pBarStep = 0;
    if (pBar != null) {
      havePBar = true;
      pBarMin = pBar.getMinimum();
      pBarMax = pBar.getMaximum();
      pBarValue = pBar.getProgress();
    }else{
      havePBar = false;
    }

    try {
      sf.changeToRootDir();
    } catch (Exception e) {
      hideInfoWindow();
      setState(RUNNING);
      throw new WritingException(resources.getString("saveFailed"), e);
    }
    ESlateHandle[] microworldHandles = null;
    int activeComponents = 0;
    int pass;
    StructOutputStream so = null;
    ObjectOutputStream oo = null;

    // Store version of save file format being used.
    try {
      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      oo = new ObjectOutputStream(bo);
      oo.writeObject(saveFileString);
      microworldVersion = saveFileVersion;
      microworldRevision = saveFileRevision;
      oo.writeObject(new Integer(microworldVersion));
      oo.writeObject(new Integer(microworldRevision));
      sf.setComment(bo.toByteArray());
    } catch (Exception e) {
      try {
        oo.close();
        //sf.close();
      } catch (Exception e2) {
      }
      hideInfoWindow();
      throw new WritingException(resources.getString("saveFailed"), e);
    }

    try {
      so = new StructOutputStream(sf, sf.createFile(ESLATE_INFO));
      oo = new ObjectOutputStream(so);
    } catch (Exception e) {
      try {
        oo.close();
        //sf.close();
      } catch (Exception e2) {
      }
      hideInfoWindow();
      setState(RUNNING);
      throw new WritingException(resources.getString("saveFailed"), e);
    }

    // Identify and save active components (components in current microworld).
    // The first pass counts them and saves the number of components,
    // the second pass saves the components.
    ESlateHandle[] handles = myHandle.getChildHandles();
    String[] componentClass = null;
    String[] componentName = null;
    Rectangle[] plugViewFrameBounds = null;
    Rectangle[] iconifiedPlugViewFrameBounds = null;
    boolean[] plugViewFrameIconified = null;
    Rectangle[] componentBounds = null;
    String[] hIds = null;
    boolean[] beanXporter = null;
    Rectangle[] oldBounds = null;
    boolean[] isCollapsed = null;
    ESlateFieldMap2 map =
      new ESlateFieldMap2(saveFileVersion/* + "." + saveFileRevision*/, 29);

    for (pass=0; pass<2; pass++) {
      activeComponents = 0;
      for (int i=0; i<handles.length; i++) {
        ESlateHandle nextHandle = handles[i];
        if (pass != 0) {
          componentClass[activeComponents] =
            nextHandle.getComponent().getClass().getName();
          componentName[activeComponents] = nextHandle.getComponentName();
          plugViewFrameBounds[activeComponents] =
            nextHandle.plugViewFrameBounds;
          iconifiedPlugViewFrameBounds[activeComponents] =
            nextHandle.iconifiedPlugViewFrameBounds;
          plugViewFrameIconified[activeComponents] =
            nextHandle.plugViewFrameIconified;
          // Write component bounds, as a hint for differentiating among
          // components of the same class.
          Rectangle bounds = null;
          Object owner = nextHandle.getComponent();
          if (owner instanceof Component) {
            try {
              Component comp =
                ((Component)owner).getParent().getParent().getParent().getParent();
              // For iconified components, the above will give the
              // container, so we use the actual component.
              if (comp.equals(container)) {
                comp = (Component)owner;
              }
              if (comp.isVisible()) {
                bounds = comp.getBounds();
              }
            } catch (Exception e) {
            }
          }
          componentBounds[activeComponents] = bounds;
          hIds[activeComponents] = nextHandle.handleId.toString();
          beanXporter[activeComponents] = nextHandle.beanXporter;
          microworldHandles[i] = nextHandle;
        }
        activeComponents++;
      }
      if (pass == 0) {
        microworldHandles = new ESlateHandle[activeComponents];
        componentClass = new String[activeComponents];
        componentName = new String[activeComponents];
        plugViewFrameBounds = new Rectangle[activeComponents];
        iconifiedPlugViewFrameBounds = new Rectangle[activeComponents];
        plugViewFrameIconified = new boolean[activeComponents];
        componentBounds = new Rectangle[activeComponents];
        hIds = new String[activeComponents];
        beanXporter = new boolean[activeComponents];
        oldBounds = new Rectangle[activeComponents];
        isCollapsed = new boolean[activeComponents];
        map.put(ACTIVE_COMPONENTS, activeComponents);
        ptvSize = null;
        viewportPos = null;
        ptvWinBounds = null;
        if (ptvWindow != null) {
          PlugViewDesktopPane ptv =
            ((PlugViewDesktop)(ptvWindow.getContentPane())).getContentPane();
          ptvSize = ptv.getPreferredSize();
          JScrollPane scrollPane =
            ((PlugViewDesktop)(ptvWindow.getContentPane())).getScrollPane();
          viewportPos = scrollPane.getViewport().getViewPosition();
          ptvWinBounds = ptvWindow.getBounds();
        }
        map.put(PTV_SIZE, ptvSize);
        map.put(VIEWPORTPOS, viewportPos);
        map.put(PTV_WIN_BOUNDS, ptvWinBounds);
      }else{
        map.put(COMPONENT_CLASS, componentClass);
        map.put(COMPONENT_NAME, componentName);
        map.put(PLUG_VIEW_FRAME_BOUNDS, plugViewFrameBounds);
        map.put(ICONIFIED_PLUG_VIEW_FRAME_BOUNDS, iconifiedPlugViewFrameBounds);
        map.put(PLUG_VIEW_FRAME_ICONIFIED, plugViewFrameIconified);
        map.put(COMPONENT_BOUNDS, componentBounds);
        map.put(H_IDS, hIds);
        map.put(BEANXPORTER, beanXporter);
      }
    }

//-    pm.stop(savePrepTimer);
//-    pm.displayTime(savePrepTimer, myHandle, "", "ms");
//-
//-    pm.init(saveStateTimer);

    if (havePBar) {
      pBarSteps = activeComponents + 1;
      if ((previousMicroworldFile != null)) {
        pBarSteps++;
      }
      pBarAvail = pBarMax - pBarMin - pBarValue;
      pBar.setMessage(resources.getString("savingState"));
    }

    // Count connected plug pairs and save the number of pairs.
    int nPairs = 0;
    ConnectedPairBaseArray myPairs = new ConnectedPairBaseArray();
    int nConnectedPairs = connectedPairs.size();
    for (int i=0; i<nConnectedPairs; i++) {
      ConnectedPair pair = connectedPairs.get(i);
      if (this.equals(pair.provider.getESlateMicroworld())) {
        myPairs.add(pair);
      }
    }
    nPairs = myPairs.size();
    map.put(N_PAIRS, nPairs);

    // Save the plug pairs.
    StringBaseArray plugName;
    String[] providerName = new String[nPairs];
    String[] providerClass = new String[nPairs];
    StringBaseArray[] providerPlugName = new StringBaseArray[nPairs];
    StringBaseArray[] providerInternalPlugName = new StringBaseArray[nPairs];
    String[] dependentName = new String[nPairs];
    String[] dependentClass = new String[nPairs];
    StringBaseArray[] dependentPlugName = new StringBaseArray[nPairs];
    StringBaseArray[] dependentInternalPlugName = new StringBaseArray[nPairs];
    for (int i=0, pairNo=0; i<nPairs; i++,pairNo++) {
      ConnectedPair pair;
      pair = myPairs.get(i);
      providerName[pairNo] = pair.provider.getComponentPathName();
      providerClass[pairNo] = pair.provider.getClass().getName();
      // Write name of provider plug
      plugName = new StringBaseArray();
      for (Plug p = pair.providerPlug; p != null; p = p.getParentPlug()) {
        plugName.add(p.getName());
      }
      providerPlugName[pairNo] = plugName;
      // Write internal name of provider plug
      plugName = new StringBaseArray();
      for (Plug p = pair.providerPlug; p != null; p = p.getParentPlug()) {
        plugName.add(p.getInternalName());
      }
      providerInternalPlugName[pairNo] = plugName;

      dependentName[pairNo] = pair.dependent.getComponentPathName();
      dependentClass[pairNo] = pair.dependent.getClass().getName();
      // Write name of dependent plug
      plugName = new StringBaseArray();
      for (Plug p = pair.dependentPlug; p != null; p = p.getParentPlug()) {
        plugName.add(p.getName());
      }
      dependentPlugName[pairNo] = plugName;
      // Write internal name of dependent plug
      plugName = new StringBaseArray();
      for (Plug p = pair.dependentPlug; p != null; p = p.getParentPlug()) {
        plugName.add(p.getInternalName());
      }
      dependentInternalPlugName[pairNo] = plugName;
    }
    map.put(PROVIDER_NAME, providerName);
    map.put(PROVIDER_CLASS, providerClass);
    map.put(PROVIDER_PLUG_NAME, providerPlugName);
    map.put(PROVIDER_INTERNAL_PLUG_NAME, providerInternalPlugName);
    map.put(DEPENDENT_NAME, dependentName);
    map.put(DEPENDENT_CLASS, dependentClass);
    map.put(DEPENDENT_PLUG_NAME, dependentPlugName);
    map.put(DEPENDENT_INTERNAL_PLUG_NAME, dependentInternalPlugName);

    // Store collapsed state of plug editor trees and the corresponding
    // uncollapsed size.
    for (int i=0; i<handles.length; i++) {
      ESlateHandle nextHandle = handles[i];
      oldBounds[i] = nextHandle.oldBounds;
      isCollapsed[i] = nextHandle.isCollapsed;
    }
    map.put(OLD_BOUNDS, oldBounds);
      map.put(IS_COLLAPSED, isCollapsed);

    // Store microworld name and plug editor preferences.
    map.put(MICROWORLD_NAME, getName());
    if (ptvWindow != null) {
      PlugViewDesktop pvd = (PlugViewDesktop)(ptvWindow.getContentPane());
      resizeFrames = pvd.isResizeFrames();
      showExist = pvd.isHighlightingExistingConnections();
      showNew = pvd.isHighlightingPossibleConnections();
      autoOpen = pvd.isAutoOpen();
      delayedAutoOpen = pvd.isDelayedAutoOpen();
      autoOpenCompatible = pvd.isAutoOpenCompatible();
    }
    map.put(RESIZE_FRAMES, resizeFrames);
    map.put(SHOW_EXIST, showExist);
    map.put(SHOW_NEW, showNew);
    map.put(AUTO_OPEN, autoOpen);
    map.put(DELAYED_AUTO_OPEN, delayedAutoOpen);
    map.put(AUTO_OPEN_COMPATIBLE, autoOpenCompatible);
    map.put(REPARENT_TYPE, reparentType);
    if (ESlate.runningOnJava14()) {
      CurrencyManager cm = CurrencyManager.currencyManager;
      if (cm != null) {
        map.put(CURRENCY_MANAGER_STATE, cm.getState());
      }
    }
    ESlateHandle activeHandle = ahg.getActiveHandle();
    if (activeHandle != null) {
      map.put(SELECTED_COMPONENT, activeHandle.getComponentName());
    }
    try {
      if (mwHelpDir != null) {
        byte[] helpBytes = packHelp();
        map.put(HELP_FILES, helpBytes);
      }
    } catch (Exception e) {
      try {
        oo.close();
        //sf.close();
      } catch (Exception e2) {
      }
      hideInfoWindow();
      setState(RUNNING);
      throw new WritingException(resources.getString("saveFailed"), e);
    }

    try {
      oo.writeObject(map);
    } catch (Exception e) {
      try {
        oo.close();
        //sf.close();
      } catch (Exception e2) {
      }
      hideInfoWindow();
      setState(RUNNING);
      throw new WritingException(resources.getString("saveFailed"), e);
    }

    try{
      oo.close();
    } catch (Exception e) {
      //try {
      //  sf.close();
      //} catch (Exception e2) {
      //}
      hideInfoWindow();
      setState(RUNNING);
      throw new WritingException(resources.getString("saveFailed"), e);
    }
    if (havePBar) {
      pBar.setProgress(pBarValue + pBarAvail * (++pBarStep) / pBarSteps);
    }

//-    pm.stop(saveStateTimer);
//-    pm.displayTime(saveStateTimer, myHandle, "", "ms");
//-
//-    pm.init(copyCompoDataTimer);

    // If saving to a different microworld file, copy the data from from the
    // component data area of the previous microworld file to the component
    // data area of the new microworld file. If saving to the same microworld
    // file, delete data corresponding to components that are no longer part
    // of this microworld.
    try {
      if ((previousMicroworldFile != null)) {
        if (!previousMicroworldFile.getAbsolutePath().equals(
            currentMicroworldFile.getAbsolutePath())) {
          if (havePBar) {
            pBar.setMessage(resources.getString("copyComponentData"));
          }
          copyComponentData();
        }else{
          if (havePBar) {
            pBar.setMessage(
              resources.getString("deleteRedundantComponentData")
            );
          }
          deleteRedundantComponentData();
        }
        if (havePBar) {
          pBar.setProgress(pBarValue + pBarAvail * (++pBarStep) / pBarSteps);
        }
      }
    } catch (Exception e) {
      hideInfoWindow();
      setState(RUNNING);
      throw new WritingException(resources.getString("saveFailed"), e);
    }

//-    pm.stop(copyCompoDataTimer);
//-    pm.displayTime(copyCompoDataTimer, myHandle, "", "ms");
//-
//-    pm.init(saveComposTimer);

    // Allow each component to save its state
    try {
      sf.changeToRootDir();
      sf.changeDir(sf.findEntry(COMPONENT_INFO));
    } catch (Exception e) {
      hideInfoWindow();
      setState(RUNNING);
      throw new WritingException(resources.getString("saveFailed"), e);
    }
    int failedToSave = 0;
    StringBuffer info = new StringBuffer();
    for (int i=0; i<activeComponents; i++) {
      Object component = microworldHandles[i].getComponent();
      if (component instanceof Externalizable) {
        try {
          if (havePBar) {
            pBar.setMessage(
              resources.getString("savingComponentState") +
              " '" + microworldHandles[i].getComponentName() + "'"
            );
          }
          saveState(microworldHandles[i]);
        } catch (Exception e) {
          //hideInfoWindow();
          //setState(RUNNING);
          System.err.println("***BEGIN STACK TRACE");
          e.printStackTrace();
          System.err.println("***END STACK TRACE");
          //throw new WritingException(resources.getString("saveFailed"), e);
          notSaved(microworldHandles[i], info, failedToSave);
          failedToSave++;
        }
      }
      if (havePBar) {
        pBar.setProgress(pBarValue + pBarAvail * (++pBarStep) / pBarSteps);
      }
    }
    currentSaveFile = null;
    hideInfoWindow();
    setState(RUNNING);

//-    pm.stop(saveComposTimer);
//-    pm.displayTime(saveComposTimer, myHandle, "", "ms");
//-    pm.displayTime(saveTimer, myHandle, "", "ms");

    try {
      sf.changeToParentDir();
    } catch (Exception e) {
      hideInfoWindow();
      throw new WritingException(resources.getString("saveFailed"), e);
    }

    if (failedToSave > 0) {
      throw new PartialWriteException(info.toString());
    }
  }

  /**
   * Copies the data from the component data area of the previous
   * microworld file to the component data area of the new microworld file.
   * @exception IOException     Thrown if the copying fails.
   */
  private void copyComponentData() throws IOException
  {
    StructFile in;
    boolean reopenedFile;
    // The reference to the previous microworld's structured file should be
    // valid and open, so that we can use it to copy the data...
    if ((previousMicroworldStructFile != null) &&
        previousMicroworldStructFile.isOpen()) {
      in = previousMicroworldStructFile;
      reopenedFile = false;
    }else{
      // ... but if it isn't, we can try reopening the file and pray that it
      // is possible. (This should never happen!)
      in = new StructFile(
        previousMicroworldFile.getAbsolutePath(), StructFile.OLD
      );
      reopenedFile = true;
    }
    StructFile out = currentMicroworldStructFile;
    in.changeToRootDir();
    try {
      in.changeDir(COMPONENT_DATA);
    } catch (IOException ioe) {
      // Nothing to copy.
      if (reopenedFile) {       // This should never happen!
        in.close();
      }
      return;
    }
    out.changeToRootDir();
    out.changeDir(out.createDir(COMPONENT_DATA));
    copyComponentData(in, out, in.list(), 0);
    if (reopenedFile) { // This should never happen!
      in.close();
    }
  }

  /**
   * Recursively copies the current directory entry from the component data
   * area of the previous microworld file to the component data area of the
   * new microworld file.
   * @param     in      The previous microworld file.
   * @param     out     The current microworld file.
   * @param     v       The list of directory entries to copy.
   * @param     depth   The recursion depth.
   * @exception IOException     Thrown if the copying fails.
   */
  private void copyComponentData(StructFile in, StructFile out, Vector v,
                                 int depth)
    throws IOException
  {
    int nEntries = v.size();
    BigInteger[] id = null;
    int nIds = 0;
    if (depth == 0) {
      id = getAllHandleIDs();
      nIds = id.length;
    }
    for (int i=0; i<nEntries; i++) {
      Entry e = (Entry)(v.elementAt(i));
      if (e.getType() == Entry.DIRECTORY) {
        boolean found = false;
        if (depth == 0) {
          for (int j=0; j<nIds; j++) {
            if (e.getName().equals(id[j].toString())) {
              found = true;
              break;
            }
          }
        }
        // Skip first-level directories that correspond to components that
        // are no longer part of this microworld.
        if ((depth > 0) || found) {
          in.changeDir(e);
          out.changeDir(out.createDir(e.getName()));
          copyComponentData(in, out, in.list(), depth+1);
          in.changeToParentDir();
          out.changeToParentDir();
        }
      }else{
        StructInputStream is = new StructInputStream(in, e);
        StructOutputStream os =
          new StructOutputStream(out, out.createFile(e.getName()));
        int n;
        do {
          n = is.read(buf);
          if (n >= 0) {
            os.write(buf, 0, n);
          }
        } while (n >= 0);
        os.close();
        is.close();
      }
    }
  }

  /**
   * Delete data corresponding to components that are no longer part of this
   * microworld.
   * @exception        IOException     Thrown if the deletion fails.
   */
  private void deleteRedundantComponentData() throws IOException
  {
    StructFile out =  currentMicroworldStructFile;
    out.changeToRootDir();
    try {
      out.changeDir(COMPONENT_DATA);
    } catch (IOException ioe) {
      return;   // Nothing to delete.
    }
    Vector entries = out.list();
    int nEntries = entries.size();
    BigInteger[] id = getAllHandleIDs();
    int nIds = id.length;
    for (int i=0; i<nEntries; i++) {
      Entry e = (Entry)(entries.elementAt(i));
      String name = e.getName();
      boolean found = false;
      for (int j=0; j<nIds; j++) {
        if (name.equals(id[j].toString())) {
          found = true;
          break;
        }
      }
      if (!found) {
        out.deleteEntry(name);
      }
    }
  }

  /**
   * Closes a structured file that had been opened for loading or saving a
   * microworld.
   * @param     sf      The structured file to close.
   * @deprecated        As of E-Slate 1.7.14, replaced by
   * {@link gr.cti.eslate.base.ESlateMicroworld#finishedLoadingOrSaving(gr.cti.structfile.StructFile)},
   * as this method has stopped actually closing the file for some time now.
   */
  public void closeMicroworldFile(StructFile sf)
  {
     finishedLoadingOrSaving(sf);
  }

  /**
   * Indicates to the platform that the microworld file that loading or saving
   * the microworld from the currently open file has completed.
   * @param     sf      The currently open microworld file. The file will
   *                    remain open, and it is the responsibility of the
   *                    invoker (i.e., the container) to close it.
   */
  public void finishedLoadingOrSaving(StructFile sf)
  {
    // Since we do not close the file, flush its cache, so that the data are
    // written to disk.
    sf.flushCache();

    // Clear the hash table containing the components whose state has been
    // restored at construction time.
    inittedObjects.clear();

    isLoading = false;
    isSaving = false;
    setState(RUNNING);
  }

  /**
   * Opens a structured file for loading the entire microworld. This
   * method should only be invoked by containers.
   * @param     loadFile        The file from which the microworld will be
   *                            loaded.
   * @exception ReadingException        Thrown if something goes wrong while
   *                                    opening the file.
   * @return    A StructFile from where the container may read additional
   *            information, and from which it should invoke loadMicroworld().
   *            If loading fails, this will be null. When finishing loading
   *            the microworld, the container should indicate this to the
   *            platform by invoking <code>finishedLoadingOrSaving()</code>.
   */
  public StructFile openMicroworldFile(String loadFile)
    throws ReadingException
  {
    return openMicroworldFile(loadFile, parentURL(loadFile));
  }

  /**
   * Opens a structured file for loading the currently active microworld.
   * @param     loadFile        The file from which the microworld will be
   *                            loaded.
   * @param     documentBase    The microworld's document base, where
   *                            external files can be found.
   * @exception ReadingException        Thrown if something goes wrong while
   *                                    opening the file.
   * @return    A StructFile from where the container may read additional
   *            information, and from which it should invoke loadMicroworld().
   *            If loading fails, this will be null. When finishing loading
   *            the microworld, the container should indicate this to the
   *            platform by invoking <code>finishedLoadingOrSaving()</code>.
   */
  public StructFile openMicroworldFile(String loadFile, URL documentBase)
    throws ReadingException
  {
    showInfoWindow(
      resources.getString("loading"),
      readIcon,
      resources.getString("pleaseWait")
    );

    currentLoadFile = loadFile;

    resetSelectedPlug();        // Ignore any plug the user may have selected
                                // as a first plug before loading.
    FileInputStream is = null;
    try {
      is = new FileInputStream(loadFile);
    } catch (Exception e3) {
      hideInfoWindow();
      throw new ReadingException(
        resources.getString("cantOpen1") +
          loadFile +
          resources.getString("cantOpen2"), e3);
    }
    ObjectInputStream oi;
    try {
      oi = new ObjectInputStream(is);
    } catch (Exception e) {
      try {
        is.close();
      } catch (Exception e2) {
      }
      hideInfoWindow();
      throw new ReadingException(resources.getString("notArchive"), e);
      //oi = null;
    }

    // Load version of save file format used in file, and check for
    // compatibility.
    String idString;
    try {
      idString = (String)(oi.readObject());
      microworldVersion = ((Integer)(oi.readObject())).intValue();
      microworldRevision = ((Integer)(oi.readObject())).intValue();
    } catch (Exception e) {
      try {
        oi.close();
        is.close();
      } catch (Exception e2) {
      }
      hideInfoWindow();
      throw new ReadingException(resources.getString("notArchive"), e);
    }
    try {
      oi.close();
      is.close();
    } catch (Exception e) {
      hideInfoWindow();
      throw new ReadingException(resources.getString("notArchive"), e);
    }

    if (idString == null || !idString.equals(saveFileString)) {
      hideInfoWindow();
      throw new ReadingException(resources.getString("notArchive"));
    }
    if (microworldVersion != saveFileVersion) {
      hideInfoWindow();
      throw new ReadingException(
        resources.getString("badVersion") + " " + microworldVersion + "." +
        microworldRevision +
        resources.getString("required1") + saveFileVersion + ".x" +
        resources.getString("required2"));
    }

    StructFile sf = null;

    try {
      sf = new StructFile(loadFile, StructFile.OLD);
    } catch (Exception e) {
      hideInfoWindow();
      throw new ReadingException(resources.getString("notArchive"), e);
    }

    this.documentBase = documentBase;
    previousMicroworldFile = currentMicroworldFile;
    currentMicroworldFile = new File(loadFile);
    //if (currentMicroworldStructFile != null) {
    //  try {
    //    currentMicroworldStructFile.close();
    //  } catch (IOException ioe) {
    //  }
    //}
    previousMicroworldStructFile = currentMicroworldStructFile;
    currentMicroworldStructFile = sf;
    // Moved to loadMicroworld.
    // setState(LOADING);
    isLoading = true;
    return sf;
  }

  /**
   * Loads a microworld from a structured file. Components are identified
   * using heuristics.
   * @param     sf      The structured file form which the microworld will be
   *                    loaded. It must have neen opened with
   *                    openMicroworldFile(), which checks for the existence
   *                    of the correct header in the file, identifying it as a
   *                    saved microworld file.
   * @exception ReadingException        Thrown if something goes wrong while
   *                                    reading from the file.
   * @exception PartialReadException    Thrown if the microworld was only
   *                                    partially restored.
   */
  public void loadMicroworld(StructFile sf)
    throws ReadingException, PartialReadException
  {
    loadMicroworld(sf, true, null);
  }

  /**
   * Loads a microworld from a structured file.
   * @param     sf      The structured file form which the microworld will be
   *                    loaded. It must have neen opened with
   *                    openMicroworldFile(), which checks for the existence
   *                    of the correct header in the file, identifying it as a
   *                    saved microworld file.
   * @param     useHeuristics   Specifies whether this method should us
   *                    heuristics to identify loaded components. If set to
   *                    false, the caller must have named the components in
   *                    the microworld before calling this method, and the
   *                    components will be identified using these names.
   * @exception ReadingException        Thrown if something goes wrong while
   *                                    reading from the file.
   * @exception PartialReadException    Thrown if the microworld was only
   *                                    partially restored.
   */
  public void loadMicroworld(StructFile sf, boolean useHeuristics)
    throws ReadingException, PartialReadException
  {
    loadMicroworld(sf, useHeuristics, null);
  }

  /**
   * Loads a microworld from a structured file.
   * @param     sf      The structured file form which the microworld will be
   *                    loaded. It must have neen opened with
   *                    openMicroworldFile(), which checks for the existence
   *                    of the correct header in the file, identifying it as a
   *                    saved microworld file.
   * @param     useHeuristics   Specifies whether this method should us
   *                    heuristics to identify loaded components. If set to
   *                    false, the caller must have named the components in
   *                    the microworld before calling this method, and the
   *                    components will be identified using these names.
   * @param     pBar    A progress bar to update while loading the microworld.
   *                    This can be null, if no progress bar should be
   *                    updated.
   * @exception ReadingException        Thrown if something goes wrong while
   *                                    reading from the file.
   * @exception PartialReadException    Thrown if the microworld was only
   *                                    partially restored.
   */
  public void loadMicroworld(StructFile sf, boolean useHeuristics,
                             ProgressBarInterface pBar)
    throws ReadingException, PartialReadException
  {
    setState(LOADING);

    boolean havePBar;
    int pBarMin = 0;
    int pBarMax = 0;
    int pBarValue = 0;
    int pBarSteps = 0;
    int pBarAvail = 0;
    int pBarStep = 0;
    if (pBar != null) {
      havePBar = true;
      pBarMin = pBar.getMinimum();
      pBarMax = pBar.getMaximum();
      pBarValue = pBar.getProgress();
    }else{
      havePBar = false;
    }

    try {
      sf.changeToRootDir();
    } catch (Exception e) {
      setState(RUNNING);
      throw new ReadingException(resources.getString("loadFailed"), e);
    }
    StructInputStream si = null;
    ObjectInputStream oi = null;
    try {
      si = new StructInputStream(sf, sf.findEntry(ESLATE_INFO));
    } catch (Exception e) {
      //try {
      //  sf.close();
      //} catch (Exception e2) {
      //}
      hideInfoWindow();
      setState(RUNNING);
      throw new ReadingException(resources.getString("notArchive"), e);
    }
    try {
      oi = new ObjectInputStream(si);
    } catch (Exception e) {
      try {
        oi.close();
        //sf.close();
      } catch (Exception e2) {
      }
      hideInfoWindow();
      setState(RUNNING);
      throw new ReadingException(resources.getString("notArchive"), e);
    }

    StorageStructure map;
    try {
      if (microworldRevision >= SUPPORTSFIELDMAP) {
        map = (StorageStructure)(oi.readObject());
      }else{
        map = createFieldMapFromFile(oi);
      }
    } catch (Exception e) {
      try {
        oi.close();
        //sf.close();
      } catch (Exception e2) {
      }
      hideInfoWindow();
      setState(RUNNING);
      throw new ReadingException(resources.getString("loadFailed"), e);
    }

    // Read number of components in current microworld and plug view window
    // information.
    int activeComponents;
    activeComponents = map.get(ACTIVE_COMPONENTS, 0);
    if (havePBar) {
      pBarSteps = activeComponents + 2;
      pBarAvail = pBarMax - pBarMin - pBarValue;
      pBar.setMessage(resources.getString("restoringState"));
    }
    ptvSize = (Dimension)(map.get(PTV_SIZE));
    viewportPos = (Point)(map.get(VIEWPORTPOS));
    ptvWinBounds = (Rectangle)(map.get(PTV_WIN_BOUNDS));

    // Read components contained in loaded microworld
    String componentClass[] = new String[activeComponents];
    String componentName[] = new String[activeComponents];
    Rectangle plugViewFrameBounds[] = new Rectangle[activeComponents];
    Rectangle iconifiedPlugViewFrameBounds[] = new Rectangle[activeComponents];
    boolean plugViewFrameIconified[] = new boolean[activeComponents];
    Rectangle componentBounds[] = new Rectangle[activeComponents];
    BigInteger hIds[] = new BigInteger[activeComponents];
    boolean beanXporter[] = new boolean[activeComponents];

    componentClass = (String[])(map.get(COMPONENT_CLASS));
    componentName = (String[])(map.get(COMPONENT_NAME));
    plugViewFrameBounds = (Rectangle[])(map.get(PLUG_VIEW_FRAME_BOUNDS));
    iconifiedPlugViewFrameBounds =
      (Rectangle[])(map.get(ICONIFIED_PLUG_VIEW_FRAME_BOUNDS));
    plugViewFrameIconified =
      (boolean[])(map.get(PLUG_VIEW_FRAME_ICONIFIED));
    componentBounds = (Rectangle[])(map.get(COMPONENT_BOUNDS));
    String[] sbi = (String[])(map.get(H_IDS));
    for (int i=0; i<activeComponents; i++) {
      if (sbi[i] != null) {
        hIds[i] = new BigInteger(sbi[i]);
      }else{
        hIds[i] = null;
      }
    }
    beanXporter = (boolean[])(map.get(BEANXPORTER));

    // Identify loaded components
    ESlateHandle[] identifiedHandle = new ESlateHandle[activeComponents];
    for (int i=0; i<activeComponents; i++) {
      identifiedHandle[i] = null;
    }
    int identifiedHandles = 0;
    ESlateHandle handles[] = myHandle.getChildHandles();
    for (int hid=0; hid<handles.length; hid++) {
      ESlateHandle nextHandle = handles[hid];
      boolean identified = false;
      int tentative = -1;
      Object owner = nextHandle.getComponent();
      if (!useHeuristics) {
        // Identify components by class and name.
        for (int i=0; i<activeComponents; i++) {
          if (identifiedHandle[i] == null &&
              owner.getClass().getName().equals(componentClass[i]) &&
              componentName[i].equals(nextHandle.getComponentName())) {
            identifiedHandle[i] = nextHandle;
            identifiedHandles++;
            identified = true;
            break;
          }
        }
      }else{
        // Identify components using heuristics
        for (int i=0; i<activeComponents; i++) {
          if (identifiedHandle[i] == null &&
              owner.getClass().getName().equals(componentClass[i])) {
            if (tentative < 0 ) {
              tentative = i;
            }
            if (componentBounds[i] != null && owner instanceof Component) {
              try {
                Component comp =
                  ((Component)owner).getParent().getParent().getParent().getParent();
                // For iconified components, the above will give the
                // container, so we use the actual component.
                if (comp.equals(container)) {
                  comp = (Component)owner;
                }
                if (comp.isVisible() &&
                    componentBounds[i].equals(comp.getBounds())) {
                  identifiedHandle[i] = nextHandle;
                  identifiedHandles++;
                  identified = true;
                  tentative = -1;
                  break;
                }
              } catch (Exception e) {
              }
            }
          }
        }
      }
      if (!identified && tentative >= 0) {
        identifiedHandle[tentative] = nextHandle;
        identifiedHandles++;
        identified = true;
        tentative = -1;
      }
      // If the current microworld contains a component not in the loaded
      // microworld, then the microworld in the file is from a different
      // microworld.  Abort.
      if (!identified) {
        try {
          oi.close();
          //sf.close();
        } catch (Exception e) {
        }
        hideInfoWindow();
        setState(RUNNING);
        throw new ReadingException(resources.getString("notThis"));
      }
    }
    // If the loaded microworld contains a different number of  components
    // than the current microworld, then the microworld in the file is from
    // a different microworld. Abort. (There is one exception:
    // In some format 6.0 microworld files, a reference to the
    // gr.cti.eslate.base.container.ESlateContainer container was saved in the
    // microworld file, as the container was considered to belong to the
    // microworld in the same capacity as other components. Before aborting,
    // check if this is the case, and the component that is unaccounted
    // for is the container.)
    boolean skippluggContainer = false;
    if (identifiedHandles != activeComponents) {
      if (microworldRevision == 0 &&
          identifiedHandles == (activeComponents - 1)) {
        for (int i=0; i<activeComponents; i++) {
          if (identifiedHandle[i] == null) {
              if (componentClass[i].equals(ESLATE_CONTAINER_CLASS_NAME)) {
              identifiedHandle[i] = getMicroworldEnvironmentHandle();
              identifiedHandles++;
              skippluggContainer = true;
              break;
            }
          }
        }
      }
      if (!skippluggContainer) {
        try {
          oi.close();
          //sf.close();
        } catch (Exception e) {
        }
        hideInfoWindow();
        setState(RUNNING);
        throw new ReadingException(resources.getString("notThis"));
      }else{
      }
    }

    // Read number of connected component pairs
    int nPairs;
    nPairs = map.get(N_PAIRS, 0);

    // Read info about connected component pairs
    String[] firstHandleName;
    //String[] firstHandleClass;
    String[] secondHandleName;
    //String[] secondHandleClass;

    // These are either instances of Vector[] or StringBaseArrauy[], depending
    // on the version of the platform under which the microworld was saved.
    /*
     * Not used.
    Object secondPlugName;
    Object firstPlugName;
    */
    Object firstPlugInternalName;
    Object secondPlugInternalName;

    firstHandleName = (String[])(map.get(PROVIDER_NAME));
    // Currently not used.
    //firstHandleClass = (String[])(map.get(PROVIDER_CLASS));
    secondHandleName = (String[])(map.get(DEPENDENT_NAME));
    // Currently not used.
    //secondHandleClass = (String[])(map.get(DEPENDENT_CLASS));

    /**
     * We don't use them, so we don't read them!
    firstPlugName = map.get(PROVIDER_PLUG_NAME);
    secondPlugName = map.get(DEPENDENT_PLUG_NAME);
    */
    firstPlugInternalName = map.get(PROVIDER_INTERNAL_PLUG_NAME);
    secondPlugInternalName = map.get(DEPENDENT_INTERNAL_PLUG_NAME);
    Vector[] fpin_v;
    Vector[] spin_v;
    StringBaseArray[] fpin_s;
    StringBaseArray[] spin_s;
    boolean usesVectors;
    if (firstPlugInternalName instanceof Vector[]) {
      fpin_v = (Vector [])firstPlugInternalName;
      spin_v = (Vector [])secondPlugInternalName;
      fpin_s = null;
      spin_s = null;
      usesVectors = true;
    }else{
      fpin_s = (StringBaseArray [])firstPlugInternalName;
      spin_s = (StringBaseArray [])secondPlugInternalName;
      fpin_v = null;
      spin_v = null;
      usesVectors = false;
    }

    // Read state of plug editor trees and the corresponding
    // uncollapsed size.
    Rectangle oldBounds[] = new Rectangle[activeComponents];
    boolean isCollapsed[] = new boolean[activeComponents];
    oldBounds = (Rectangle[])(map.get(OLD_BOUNDS));
    isCollapsed = (boolean[])(map.get(IS_COLLAPSED));

    // Read name of microworld and plug editor preferences.
    boolean oldRenamingAllowed = renamingAllowed;
    renamingAllowed = true;
    try {
      setUniqueName(map.get(MICROWORLD_NAME, ""));
    } catch (RenamingForbiddenException rfe) {
    }
    renamingAllowed = oldRenamingAllowed;
    resizeFrames = map.get(RESIZE_FRAMES, true);
    showExist = map.get(SHOW_EXIST, true);
    showNew = map.get(SHOW_NEW, true);
    autoOpen = map.get(AUTO_OPEN, true);
    delayedAutoOpen = map.get(DELAYED_AUTO_OPEN, false);
    autoOpenCompatible = map.get(AUTO_OPEN_COMPATIBLE, true);
    reparentType = map.get(REPARENT_TYPE, REPARENT_ASK);
    if (ESlate.runningOnJava14()) {
      StorageStructure cmState =
        (StorageStructure)map.get(CURRENCY_MANAGER_STATE);
      if (cmState == null) {
        // We used to store the currency manager instance, which would cause
        // problems if one used the currency manager both before and after
        // loading a microworld. If the currency manager cannot be read, check
        // if there is a cuurency manager stored, and, if so, use its state
        CurrencyManager oldCM = (CurrencyManager)map.get(CURRENCY_MANAGER);
        if (oldCM != null) {
          cmState = oldCM.getState();
        }
      }
      if (cmState != null) {
        CurrencyManager cm = CurrencyManager.getCurrencyManager();
        cm.setState(cmState);
      }
    }
    String activeCompo = (String)map.get(SELECTED_COMPONENT);
    if (activeCompo != null) {
      ESlateHandle activeHandle = myHandle.getChildHandle(activeCompo);
      ahg.setActiveHandle(activeHandle);
    }
    byte[] helpBytes = (byte[])map.get(HELP_FILES);
    if (helpBytes != null) {
      try {
        mwHelpDir = unpackHelp(helpBytes, helpDir);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }else{
      mwHelpDir = null;
    }
    if (ptvWindow != null) {
      PlugViewDesktop pvd = (PlugViewDesktop)(ptvWindow.getContentPane());
      pvd.setResizeFrames(resizeFrames);
      pvd.setHighlightingExistingConnections(showExist);
      pvd.setHighlightingPossibleConnections(showNew);
      pvd.setAutoOpen(autoOpen);
      pvd.setDelayedAutoOpen(delayedAutoOpen);
      pvd.setAutoOpenCompatible(autoOpenCompatible);
    }

/*
    // Disconnect previously connected components
    handles = getAllHandlesInHierarchy();
    ESlateHandle containerHandle = getMicroworldEnvironmentHandle();
    for (int hid=0; hid<handles.length; hid++) {
      ESlateHandle handle = handles[hid];
      if (handle != null && !handle.equals(containerHandle)) {
        Plug[] plugs = handle.getPlugs();
        for (int i=0; i<plugs.length; i++) {
          Plug plug = plugs[i];
          plug.disconnect();
        }
      }
    }
*/

    if (useHeuristics) {
      // Rename components.
      // Do this by first renaming them to a series of reserved names, then
      // renaming them again to their actual names. This is to avoid problems
      // that arise from yet unrenamed components having the name that we are
      // trying to assign to another component.
      for (int i=0; i<activeComponents; i++) {
        oldRenamingAllowed = renamingAllowed;
        renamingAllowed = true;
        try {
          identifiedHandle[i].setComponentName(reservedPrefix + i);
        } catch (Exception e) {
        }
        renamingAllowed = oldRenamingAllowed;
      }
      for (int i=0; i<activeComponents; i++) {
        oldRenamingAllowed = renamingAllowed;
        renamingAllowed = true;
        try {
          identifiedHandle[i].setComponentName(componentName[i]);
        } catch (Exception e) {
        }
        renamingAllowed = oldRenamingAllowed;
      }
      renamingAllowed = oldRenamingAllowed;
    }

    // Restore handle ids, collapsed state of plug editor trees,
    // corresponding uncollapsed size variables, and beanXporter flags.
    BigInteger max = MINUS_1;
    for (int i=0; i<activeComponents; i++) {
      if (hIds[i] != null) {
        identifiedHandle[i].handleId = hIds[i];
        if (max.compareTo(hIds[i]) < 0) {
          max = hIds[i];
        }
      }
      identifiedHandle[i].oldBounds = oldBounds[i];
      identifiedHandle[i].isCollapsed = isCollapsed[i];
      identifiedHandle[i].beanXporter = beanXporter[i];
    }
    if (max.compareTo(BigInteger.ZERO) >= 0 ) {
      lastId = max;
    }

    // Set bounds of internal frames in the plug view window.
    for (int i=0; i<activeComponents; i++) {
      identifiedHandle[i].plugViewFrameBounds = plugViewFrameBounds[i];
      identifiedHandle[i].iconifiedPlugViewFrameBounds =
        iconifiedPlugViewFrameBounds[i];
      identifiedHandle[i].usePlugViewFrameBounds = true;
    }
    if (ptvWindow != null) {
      PlugViewDesktopPane ptv =
        ((PlugViewDesktop)(ptvWindow.getContentPane())).getContentPane();
      JInternalFrame iFrame[] = ptv.getAllFrames();
      for (int i=0; i<iFrame.length; i++) {
        for (int j=0; j<activeComponents; j++) {
          if (identifiedHandle[j].getComponentName().equals(iFrame[i].getTitle())) {
            if (plugViewFrameBounds[j] != null) {
              iFrame[i].setBounds(plugViewFrameBounds[j]);
            }
            if (iconifiedPlugViewFrameBounds[j] != null) {
              iFrame[i].getDesktopIcon().setBounds(
                iconifiedPlugViewFrameBounds[j]
              );
            }
            try {
              iFrame[i].setIcon(plugViewFrameIconified[j]);
            } catch (PropertyVetoException e) {
            }
          }
        }
      }
    }
    // Set size of plug view window.
    if (ptvSize != null && ptvWindow != null) {
      PlugViewDesktopPane ptv =
        ((PlugViewDesktop)(ptvWindow.getContentPane())).getContentPane();
      JScrollPane scrollPane =
        ((PlugViewDesktop)(ptvWindow.getContentPane())).getScrollPane();
      ptv.setPreferredSize(ptvSize);
      ptv.setMinimumSize(ptvSize);
      ptv.setMaximumSize(ptvSize);
      if (viewportPos != null) {
        scrollPane.getViewport().setViewPosition(viewportPos);
      }
      scrollPane.revalidate();
      if (ptvWinBounds != null) {
        ptvWindow.setBounds(ptvWinBounds);
      }
    }
    if (havePBar) {
      pBar.setProgress(pBarValue + pBarAvail * (++pBarStep) / pBarSteps);
    }

    // Load component state
    try {
      oi.close();
      sf.changeDir(sf.findEntry(COMPONENT_INFO));
    } catch (Exception e) {
      hideInfoWindow();
      setState(RUNNING);
      throw new ReadingException(resources.getString("loadFailed"), e);
    }
    int failedToLoad = 0;
    StringBuffer info = new StringBuffer();
    for (int i=0; i<activeComponents; i++) {
      if (havePBar) {
        pBar.setMessage(
          resources.getString("restoringComponentState") +
          " '" + identifiedHandle[i].getComponentName() + "'"
        );
      }
      Object component = identifiedHandle[i].getComponent();
      if (component instanceof Externalizable) {
        try {
          loadState(identifiedHandle[i]);
        } catch (Exception e) {
          //hideInfoWindow();
          //setState(RUNNING);
          System.err.println("***BEGIN STACK TRACE");
          e.printStackTrace();
          System.err.println("***END STACK TRACE");
          //throw new ReadingException(resources.getString("loadFailed"), e);
          notLoaded(identifiedHandle[i], info, failedToLoad);
          failedToLoad++;
        }
      }
      if (havePBar) {
        pBar.setProgress(pBarValue + pBarAvail * (++pBarStep) / pBarSteps);
      }
    }
    if (failedToLoad > 0) {
      info.append("\n");
    }
    currentLoadFile = null;;

    ESlateHandle[] firstCompoHandle = new ESlateHandle[nPairs];
    ESlateHandle[] secondCompoHandle = new ESlateHandle[nPairs];

    // Restore connections
    if (havePBar) {
      pBar.setMessage(resources.getString("restoringConnections"));
    }
    int failedToConnect = 0;
    for (int i=0; i<nPairs; i++) {
      firstCompoHandle[i] = myHandle.getChildHandle(firstHandleName[i]);
      secondCompoHandle[i] = myHandle.getChildHandle(secondHandleName[i]);
      System.out.println("DBG: getChildHandle -> "+firstHandleName[i]+" "+secondHandleName[i]);
      if (firstCompoHandle[i] == null) {
        notConnected(
          info,
          resources.getString("component") + " " +
            firstHandleName[i] + " " +
            resources.getString("notInMW"),
          failedToConnect
        );
        failedToConnect++;
      }
      if (secondCompoHandle[i] == null) {
        notConnected(
          info,
          resources.getString("component") + " " +
            secondHandleName[i] + " " +
            resources.getString("notInMW"),
          failedToConnect
        );
        failedToConnect++;
      }
      if ((firstCompoHandle[i] == null) || (secondCompoHandle[i] == null)) {
        continue;
      }
      //
      // Account for plug aliases. Do this at each iteration, as components
      // may assign more aliases while connections are being restored, based
      // on the result of the connection.
      if (usesVectors) {
        resolvePlugAliases(firstCompoHandle, fpin_v, ALIAS_OUTPUT);
        resolvePlugAliases(secondCompoHandle, spin_v, ALIAS_INPUT);
      }else{
        resolvePlugAliases(firstCompoHandle, fpin_s, ALIAS_OUTPUT);
        resolvePlugAliases(secondCompoHandle, spin_s, ALIAS_INPUT);
      }
      //
      // Identify provider plug
      Plug plug1;
      Vector iName1v;
      StringBaseArray iName1s;
      if (usesVectors) {
        iName1v = fpin_v[i];
        iName1s = null;
        plug1 = firstCompoHandle[i].getPlugLocaleIndependent(
          (String)(iName1v.elementAt(iName1v.size()-1))
        );
        if (plug1 != null) {
          int n = iName1v.size() - 2;
          for (int j=n; j>=0; j--) {
            plug1 =
              plug1.getPlugLocaleIndependent((String)(iName1v.elementAt(j)));
            if (plug1 == null) {
              break;
            }
          }
        }
      }else{
        iName1s = fpin_s[i];
        iName1v = null;
        plug1 = firstCompoHandle[i].getPlugLocaleIndependent(
          iName1s.get(iName1s.size()-1)
        );
        if (plug1 != null) {
          int n = iName1s.size() - 2;
          for (int j=n; j>=0; j--) {
            plug1 =
              plug1.getPlugLocaleIndependent(iName1s.get(j));
            if (plug1 == null) {
              break;
            }
          }
        }
      }
      if (plug1 == null) {
        if ((failedToConnect == 0) && (failedToLoad > 0)) {
          info.append("\n");
        }
        StringBuffer plugName = new StringBuffer();
        int n;
        if (usesVectors) {
          n = iName1v.size();
        }else{
          n = iName1s.size();
        }
        for (int j=0; j<n; j++) {
          String s;
          if (usesVectors) {
            s = (String)(iName1v.get(j));
          }else{
            s = iName1s.get(j);
          }
          if (plugName.length() == 0) {
            plugName.append(s);
          }else{
            plugName.insert(0, '.');
            plugName.insert(0, s);
          }
        }
        notConnected(
          info,
          resources.getString("component") + " " +
            firstCompoHandle[i].getComponentName() + " " +
            resources.getString("dontHavePlug") + " " +
            plugName.toString(),
          failedToConnect
        );
        failedToConnect++;
      }

      // Identify dependent plug
      Plug plug2;
      Vector iName2v;
      StringBaseArray iName2s;
      if (usesVectors) {
        iName2v = spin_v[i];
        iName2s = null;
        plug2 = secondCompoHandle[i].getPlugLocaleIndependent(
          (String)(iName2v.elementAt(iName2v.size()-1))
        );
        if (plug2 != null) {
          int n = iName2v.size() - 2;
          for (int j=n; j>=0; j--) {
            plug2 =
              plug2.getPlugLocaleIndependent((String)(iName2v.elementAt(j)));
            if (plug2 == null) {
              break;
            }
          }
        }
      }else{
        iName2s = spin_s[i];
        iName2v = null;
        plug2 = secondCompoHandle[i].getPlugLocaleIndependent(
          iName2s.get(iName2s.size()-1)
        );
        if (plug2 != null) {
          int n = iName2s.size() - 2;
          for (int j=n; j>=0; j--) {
            plug2 = plug2.getPlugLocaleIndependent(iName2s.get(j));
            if (plug2 == null) {
              break;
            }
          }
        }
      }
      if (plug2 == null) {
        if ((failedToConnect == 0) && (failedToLoad > 0)) {
          info.append("\n");
        }
        StringBuffer plugName = new StringBuffer();
        int n;
        if (usesVectors) {
          n = iName2v.size();
        }else{
          n = iName2s.size();
        }
        for (int j=0; j<n; j++) {
          String s;
          if (usesVectors) {
            s = (String)(iName2v.get(j));
          }else{
            s = iName2s.get(j);
          }
          if (plugName.length() == 0) {
            plugName.append(s);
          }else{
            plugName.insert(0, '.');
            plugName.insert(0, s);
          }
        }
        notConnected(
          info,
          resources.getString("component") + " " +
            secondCompoHandle[i].getComponentName() + " " +
            resources.getString("dontHavePlug") + " " +
            plugName.toString(),
          failedToConnect
        );
        failedToConnect++;
      }

      if (plug1 != null && plug2 != null) {
        // Connect the two plugs. If for some reason they are already
        // connected, leave them that way, instead of toggling their
        // connection state.
        if (!plug1.isConnected(plug2)) {
          connectComponent(plug1);
          connectComponent(plug2);
        }
      }
    }

    origInputHandles.clear();
    origInputPlugNames.clear();
    targetInputHandles.clear();
    targetInputPlugNames.clear();
    origOutputHandles.clear();
    origOutputPlugNames.clear();
    targetOutputHandles.clear();
    targetOutputPlugNames.clear();

    hideInfoWindow();
    setState(RUNNING);

    try {
      sf.changeToParentDir();
    } catch (Exception e) {
      throw new ReadingException(resources.getString("loadFailed"), e);
    }

    if (havePBar) {
      pBar.setProgress(pBarValue + pBarAvail * (++pBarStep) / pBarSteps);
    }

    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.displayConstructionTimes();
    pm.displayESlateInitTimes();
    pm.displayDeferredTimes(true);

    if ((failedToLoad > 0) || (failedToConnect > 0)) {
      throw new PartialReadException(info.toString());
    }
  }

  /**
   * This method replaces plugs retrieved from a saved microworld file with
   * their aliases.
   * @param     handles         An array of E-Slate handles owning the
   *                            corresponding plugs specified in in the
   *                            <code>plugNames</code> argument.
   *                            This argument  is modified in place.
   * @param     plugNames       An array containing plug names. Each name
   *                            is a StringBaseArray, containing the internal,
   *                            locale-independent names of all plugs in the
   *                            plug hierarchy from the actual plug to a
   *                            top-level plug.
   *                            This argument is modified in place.
   * @param     mode            Specifies if input plug aliases (ALIAS_INPUT)
   *                            or output plug aliases (ALIAS_OUTPUT) should
   *                            be used.
   */
  void resolvePlugAliases(ESlateHandle[] handles, StringBaseArray[] plugNames,
                          int mode)
  {
    ESlateHandleBaseArray targetHandles;
    SBABaseArray targetPlugNames;
    ESlateHandleBaseArray origHandles;
    SBABaseArray origPlugNames;
    if (mode == ALIAS_INPUT) {
      targetHandles = targetInputHandles;
      targetPlugNames = targetInputPlugNames;
      origHandles = origInputHandles;
      origPlugNames = origInputPlugNames;
    }else{
      targetHandles = targetOutputHandles;
      targetPlugNames = targetOutputPlugNames;
      origHandles = origOutputHandles;
      origPlugNames = origOutputPlugNames;
    }
    int nHandles = handles.length;
    int nAliases = origHandles.size();
    for (int alias=0; alias<nAliases; alias++) {
      for (int handle=0; handle<nHandles; handle++) {
        if (matchPlugAlias(handles[handle], plugNames[handle],
                          origHandles.get(alias), origPlugNames.get(alias))) {
          handles[handle] = targetHandles.get(alias);
          plugNames[handle] = targetPlugNames.get(alias);
          break;
        }
      }
    }
  }

  /**
   * This method replaces plugs retrieved from a saved microworld file with
   * their aliases. This version of the method is used for compatibility with
   * previous versions of the platform, and should not be used in new code.
   * @param     handles         An array of E-Slate handles owning the
   *                            corresponding plugs specified in in the
   *                            <code>plugNames</code> argument.
   *                            This argument  is modified in place.
   * @param     plugNames       An array containing plug names. Each name
   *                            is a Vector, containing the internal,
   *                            locale-independent names of all plugs in the
   *                            plug hierarchy from the actual plug to a
   *                            top-level plug.
   *                            This argument is modified in place.
   * @param     mode            Specifies if input plug aliases (ALIAS_INPUT)
   *                            or output plug aliases (ALIAS_OUTPUT) should
   *                            be used.
   */
  void resolvePlugAliases(ESlateHandle[] handles, Vector[] plugNames,
                          int mode)
  {
    // Convert old style Vectors of Strings to new style StringBaseArrays.
    int nNames = plugNames.length;
    StringBaseArray[] plugNamesS = new StringBaseArray[nNames];
    for (int i=0; i<nNames; i++) {
      Vector v = plugNames[i];
      int n = v.size();
      StringBaseArray sba = new StringBaseArray(n);
      for (int j=0; j<n; j++) {
        sba.add((String)(v.elementAt(j)));
      }
      plugNamesS[i] = sba;
    }

    // Resolve plug aliases.
    resolvePlugAliases(handles, plugNamesS, mode);

    // Convert StringBaseArrays back to Vectors of Strings.
    for (int i=0; i<nNames; i++) {
      StringBaseArray sba = plugNamesS[i];
      int n = sba.size();
      Vector v = new Vector(n);
      for (int j=0; j<n; j++) {
        v.addElement(sba.get(j));
      }
      plugNames[i] = v;
    }
  }

  /**
   * Checks if two plugs are the same. This method is used to match the names
   * of plugs retrieved from a saved microworld with any defined aliases.
   * @param     handle          The E-Slate handle of the component owning a
   *                            plug retrieved from the saved microworld file.
   * @param     plugName        The name of a plug retrieved from the saved
   *                            microworld file. This name is a
   *                            StringBaseArray
   *                            containing the internal, locale-independent
   *                            names of all plugs in the plug hierarchy,
   *                            from the actual plug to a top-level plug.
   * @param     testHandle      The handle owning the original plug of a plug
   *                            alias.
   * @param     testName        The name of the original plug of a plug alias.
   *                            This name is a vector
   *                            containing the internal, locale-independent
   *                            names of all plugs in the plug hierarchy,
   *                            from the actual plug to a top-level plug.
   */
  private boolean matchPlugAlias(ESlateHandle handle, StringBaseArray plugName,
                                ESlateHandle testHandle,
                                StringBaseArray testName)
  {
    if ((handle == null) || (!handle.equals(testHandle))) {
      return false;
    }
    int n = plugName.size();
    int testN = testName.size();
    if (n != testN) {
      return false;
    }
    for (int i=0; i<n; i++) {
      String s1 = plugName.get(i);
      String s2 = testName.get(i);
      if ((s1 == null) || (!s1.equals(s2))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Defines an alias for a plug. These aliases are only used during loading a
   * microworld from a file, to redirect plug connections from plugs specified
   * in the file to other plugs. After loading a microworld file, any defined
   * aliases are cleared. The intended use of this method is to be invoked from
   * within a component's <code>readExternal</code> method, to maintain
   * compatibility with previous versions of itself when its plugn hierarchy is
   * modified, e.g., when a plug of the component is moved to one of its
   * children.
   * @param     targetPlug      A plug that actually exists in the microworld,
   *                            and to which references to a plug stored in the
   *                            microworld will be redirected.
   * @param     origComponent   The component owning the plug, references to
   *                            which will be redirected.
   * @param     origPlugName    The name of the plug, references to which will
   *                            be redirected. The name is a string array,
   *                            containing the <em>internal,
   *                            locale-independent</em> names of all plugs in
   *                            the plug hierarchy, from the actual plug to a
   *                            top-level plug.
   * @exception BadPlugAliasException   Thrown when something goes wrong
   *                            during the definition of the alias.
   */
  public void setPlugAliasForLoading(Plug targetPlug,
                ESlateHandle origComponent, String[] origPlugName)
    throws BadPlugAliasException
  {
    setPlugAliasForLoading(targetPlug, origComponent, origPlugName,
                           ALIAS_INPUT_OUTPUT);
  }

  /**
   * Defines an alias for a plug. These aliases are only used during loading a
   * microworld from a file, to redirect plug connections from plugs specified
   * in the file to other plugs. After loading a microworld file, any defined
   * aliases are cleared. The intended use of this method is to be invoked from
   * within a component's <code>readExternal</code> method, to maintain
   * compatibility with previous versions of itself when its plugn hierarchy is
   * modified, e.g., when a plug of the component is moved to one of its
   * children.
   * @param     targetPlug      A plug that actually exists in the microworld,
   *                            and to which references to a plug stored in the
   *                            microworld will be redirected.
   * @param     origComponent   The component owning the plug, references to
   *                            which will be redirected.
   * @param     origPlugName    The name of the plug, references to which will
   *                            be redirected. The name is a string array,
   *                            containing the <em>internal,
   *                            locale-independent</em> names of all plugs in
   *                            the plug hierarchy, from the actual plug to a
   *                            top-level plug.
   * @param     mode            Specifies if the alias is valid for the input
   *                            part of a plug (ALIAS_INPUT), the output part
   *                            of a plug (ALIAS_OUTPUT), or both parts of a
   *                            plug (ALIAS_INPUT_OUTPUT). Specifying
   *                            ALIAS_INPUT or ALIAS_OUTPUT for a pure
   *                            protocol plug will have undefined results.
   *                            (I.e., don't do it!)
   * @exception BadPlugAliasException   Thrown when something goes wrong
   *                            during the definition of the alias.
   */
  public void setPlugAliasForLoading(Plug targetPlug,
                ESlateHandle origComponent, String[] origPlugName,
                int mode)
    throws BadPlugAliasException
  {
    if (targetPlug == null) {
      throw new BadPlugAliasException(resources.getString("noTarget"));
    }
    if (targetPlug.getHandle() == null) {
      throw new BadPlugAliasException(resources.getString("noTarget2"));
    }
    if (origComponent == null) {
      throw new BadPlugAliasException(resources.getString("noOrig"));
    }
    if ((targetPlug == null) ||
        (origPlugName == null) || (origPlugName.length == 0)) {
      throw new BadPlugAliasException(resources.getString("noOrigPlug"));
    }
    StringBaseArray target = new StringBaseArray();
    Plug p = targetPlug;
    while (p != null) {
      target.add(p.getInternalName());
      p = p.getParentPlug();
    }
    int origN = origPlugName.length;
    StringBaseArray orig = new StringBaseArray(origN);
    p = null;
    for (int i=0; i<origN; i++) {
      orig.add(origPlugName[i]);
    }
    if ((mode == ALIAS_INPUT) || (mode == ALIAS_INPUT_OUTPUT)) {
      targetInputHandles.add(targetPlug.getHandle());
      targetInputPlugNames.add(target);
      origInputHandles.add(origComponent);
      origInputPlugNames.add(orig);
    }
    if ((mode == ALIAS_OUTPUT) || (mode == ALIAS_INPUT_OUTPUT)) {
      targetOutputHandles.add(targetPlug.getHandle());
      targetOutputPlugNames.add(target);
      origOutputHandles.add(origComponent);
      origOutputPlugNames.add(orig);
    }
  }

  /**
   * Creates a StorageStructure containing the microworld data in a pre-6.5
   * format microworld file.
   * @param     oi      The stream from where the data will be read.
   * @return    A StorageStructure containing the data in the file.
   */
  private StorageStructure createFieldMapFromFile(ObjectInputStream oi)
    throws Exception
  {
    ESlateFieldMap2 map =
      new ESlateFieldMap2(microworldVersion/* + "." + microworldRevision*/, 29);
    int activeComponents = ((Integer)(oi.readObject())).intValue();
    map.put(ACTIVE_COMPONENTS, activeComponents);
    Dimension d = (Dimension)(oi.readObject());
    map.put(PTV_SIZE, d);
    Point p = (Point)(oi.readObject());
    map.put(VIEWPORTPOS, p);
    Rectangle r = (Rectangle)(oi.readObject());
    map.put(PTV_WIN_BOUNDS, r);

    String componentClass[] = new String[activeComponents];
    String componentName[] = new String[activeComponents];
    Rectangle plugViewFrameBounds[] = new Rectangle[activeComponents];
    Rectangle iconifiedPlugViewFrameBounds[] = new Rectangle[activeComponents];
    boolean plugViewFrameIconified[] = new boolean[activeComponents];
    Rectangle componentBounds[] = new Rectangle[activeComponents];
    String hIds[] = new String[activeComponents];
    boolean beanXporter[] = new boolean[activeComponents];
    for (int i=0; i<activeComponents; i++) {
      componentClass[i] = (String)(oi.readObject());
      componentName[i] = (String)(oi.readObject());
      plugViewFrameBounds[i] = (Rectangle)oi.readObject();
      iconifiedPlugViewFrameBounds[i] = (Rectangle)oi.readObject();
      plugViewFrameIconified[i] = oi.readBoolean();
      // Component bounds are stored from version 6.1 onward.
      if (microworldRevision >= SUPPORTSCOMPONENTBOUNDS) {
        componentBounds[i] = (Rectangle)(oi.readObject());
      }else{
        componentBounds[i] = null;
      }
      // Handle IDs are stored from version 6.2 onward.
      if (microworldRevision >= SUPPORTSHANDLEIDS) {
        hIds[i] = (String)(oi.readObject());
      }else{
        hIds[i] = null;
      }
      // The beanXporter flag is stored form version 6.4 onward.
      if (microworldRevision >= SUPPORTSBEANXPORTER) {
        beanXporter[i] = oi.readBoolean();
      }else{
        beanXporter[i] = false;
      }
    }
    map.put(COMPONENT_CLASS, componentClass);
    map.put(COMPONENT_NAME, componentName);
    map.put(PLUG_VIEW_FRAME_BOUNDS, plugViewFrameBounds);
    map.put(ICONIFIED_PLUG_VIEW_FRAME_BOUNDS, iconifiedPlugViewFrameBounds);
    map.put(PLUG_VIEW_FRAME_ICONIFIED, plugViewFrameIconified);
    map.put(COMPONENT_BOUNDS, componentBounds);
    map.put(H_IDS, hIds);
    map.put(BEANXPORTER, beanXporter);

    int nPairs = ((Integer)(oi.readObject())).intValue();
    String[] firstHandleName = new String[nPairs];
    String[] firstHandleClass = new String[nPairs];
    Vector[] firstPlugName = new Vector[nPairs];
    Vector[] firstPlugInternalName = new Vector[nPairs];
    String[] secondHandleName = new String[nPairs];
    String[] secondHandleClass = new String[nPairs];
    Vector[] secondPlugName = new Vector[nPairs];
    Vector[] secondPlugInternalName = new Vector[nPairs];
    for (int i=0; i<nPairs; i++) {
      firstHandleName[i] = (String)(oi.readObject());
      firstHandleClass[i] = (String)(oi.readObject());
      firstPlugName[i] = (Vector)(oi.readObject());
      firstPlugInternalName[i] = (Vector)(oi.readObject());
      secondHandleName[i] = (String)(oi.readObject());
      secondHandleClass[i] = (String)(oi.readObject());
      secondPlugName[i] = (Vector)(oi.readObject());
      secondPlugInternalName[i] = (Vector)(oi.readObject());
    }
    map.put(N_PAIRS, nPairs);
    map.put(PROVIDER_NAME, firstHandleName);
    map.put(PROVIDER_CLASS, firstHandleClass);
    map.put(PROVIDER_PLUG_NAME, firstPlugName);
    map.put(PROVIDER_INTERNAL_PLUG_NAME, firstPlugInternalName);
    map.put(DEPENDENT_NAME, secondHandleName);
    map.put(DEPENDENT_CLASS, secondHandleClass);
    map.put(DEPENDENT_PLUG_NAME, secondPlugName);
    map.put(DEPENDENT_INTERNAL_PLUG_NAME, secondPlugInternalName);

    Rectangle oldBounds[] = new Rectangle[activeComponents];
    boolean isCollapsed[] = new boolean[activeComponents];
    for (int i=0; i<activeComponents; i++) {
      if (microworldRevision >= SUPPORTSPLUGEDITORPREFERENCES) {
        oldBounds[i] = (Rectangle)(oi.readObject());
        isCollapsed[i] = oi.readBoolean();
      }else{
        oldBounds[i] = null;
        isCollapsed[i] = false;
      }
    }
    map.put(OLD_BOUNDS, oldBounds);
    map.put(IS_COLLAPSED, isCollapsed);

    String name = (String)(oi.readObject());
    map.put(MICROWORLD_NAME, name);
    if (microworldRevision >= SUPPORTSPLUGEDITORPREFERENCES) {
      boolean b = oi.readBoolean();
      map.put(RESIZE_FRAMES, b);
      b = oi.readBoolean();
      map.put(SHOW_EXIST, b);
      b = oi.readBoolean();
      map.put(SHOW_NEW, b);
      b = oi.readBoolean();
      map.put(AUTO_OPEN, b);
      b = oi.readBoolean();
      map.put(AUTO_OPEN_COMPATIBLE, b);
    }
    return map;
  }

  /**
   * Updates a message buffer with information about a component whose state
   * failed to load.
   * @param     h       The E-Slate handle of the component.
   * @param     buf     The message buffer.
   * @param     nFailed The number of components, not counting the current
   *                    one, whose state has failed to load.
   */
  private void notLoaded(ESlateHandle h, StringBuffer buf, int nFailed)
  {
    if (nFailed == 0) {
      buf.append(resources.getString("failedToLoad"));
      buf.append("\n        ");
    }else{
      buf.append(", ");
    }
    buf.append(h.getComponentName());
  }

  /**
   * Updates a message buffer with information about a component whose state
   * failed to be saved.
   * @param     h       The E-Slate handle of the component.
   * @param     buf     The message buffer.
   * @param     nFailed The number of components, not counting the current
   *                    one, whose state has failed to load.
   */
  private void notSaved(ESlateHandle h, StringBuffer buf, int nFailed)
  {
    if (nFailed == 0) {
      buf.append(resources.getString("failedToSave"));
      buf.append("\n        ");
    }else{
      buf.append(", ");
    }
    buf.append(h.getComponentName());
  }

  /**
   * Updates a message buffer with information about connections that failed
   * to reestablish during the restoration of a microworld.
   * @param     buf     The message buffer.
   * @param     message The message to append to the buffer.
   * @param     nFailed The number of connections, not counting the current
   *                    one, that failed to reestablish.
   */
  private void notConnected(StringBuffer buf, String message, int nFailed)
  {
    if (nFailed == 0) {
      buf.append(resources.getString("failedToConnect"));
    }
    buf.append("\n        ");
    buf.append(message);
  }

  /**
   * Saves the state of a given component in the microworld file.
   * This method is called by components, when saving their state,
   * to save the state of their children. It opens an appropriate output
   * stream on the microworld file and calls the component's writeExternal
   * method to save its state.
   * @param     h       The E-Slate handle of the component whose state will
   *                    be saved.
   * @exception IOException     Thrown if saving the state fails.
   */
  void saveState(ESlateHandle h) throws IOException
  {
    if (state != SAVING) {
      throw new IOException(resources.getString("notOpenSave"));
    }
    StructOutputStream so = new StructOutputStream(
      currentMicroworldStructFile, getEntryFromHandle(h)
    );
    ObjectOutputStream oo = new ObjectOutputStream(so);
    Object component = h.getComponent();
    if ((component instanceof Externalizable) ||
        (component instanceof Serializable)) {
      try {
        if (component instanceof Externalizable) {
          ((Externalizable)component).writeExternal(oo);
        }else{
          oo.writeObject(component);
        }
        oo.flush();
      } catch (IOException ioe) {
        oo.close();
        throw ioe;
      }
    }else{
      oo.close();
      throw new IOException(resources.getString("notExtSerialiazble"));
    }
    oo.close();
  }

  /**
   * Loads the state of a given component from the microworld file.
   * This method is called by components, when loading their state,
   * to load the state of their children.
   * @param     h       The E-Slate handle of the component whose state will
   *                    be loaded
   * @exception IOException     Thrown if loading the state fails.
   */
  void loadState(ESlateHandle h) throws IOException
  {
    if (state != LOADING) {
      throw new IOException(resources.getString("notOpenLoad"));
    }
    Object comp = h.getComponent();
    if ((comp != null) && inittedObjects.containsKey(comp)) {
      // State already restored in constructor.
      return;
    }
    StructInputStream si = new StructInputStream(
      currentMicroworldStructFile, getEntryFromHandle(h)
    );
    ObjectInputStream oi = new ObjectInputStream(si);
    Object component = h.getComponent();
    if ((component instanceof Externalizable) || (component == null)) {
      try {
        boolean oldRenamingAllowed = renamingAllowed;
        renamingAllowed = true;
        if (component instanceof Externalizable) {
          ((Externalizable)component).readExternal(oi);
        }else{
          h.setComponent(oi.readObject());
        }
        renamingAllowed = oldRenamingAllowed;
      } catch (IOException ioe) {
        oi.close();
        throw ioe;
      } catch (ClassNotFoundException cnfe) {
        oi.close();
        IOException ioe = new IOException(cnfe.getMessage());
        ioe.initCause(cnfe);
        throw ioe;
      }
    }else{
      oi.close();
      throw new IOException(resources.getString("notExtSerialiazble"));
    }
    oi.close();
  }

  /**
   * Returns the structured file entry associated with a particular component,
   * taking the component hierarchy into account. This method should only
   * called when loading or saving a microworld. In the latter case, the entry
   * is created, deleting any previous entry.
   * @param     h       The E-Slate handle of the component.
   * @return    The requested structured file entry.
   */
  private Entry getEntryFromHandle(ESlateHandle h)
  {
    int depth;
    if ((microworldVersion == 6) &&
        (microworldRevision < MICROWORLDISCOMPONENT0)) {
      // Component structure is saved up to but not including the microworld.
      // Anything higher is ignored.
      depth = h.nestingDepthFromMicroworld();
    }else{
      if ((microworldVersion == 6) &&
          (microworldRevision == MICROWORLDISCOMPONENT0)) {
        // The entire component structure is saved, including the container.
        // This was only used during development, but since it is trivial to
        // support, we do so.
        depth = h.nestingDepth();
      }else{
        // Component structure is saved up to and including the microworld.
        // Anything higher is ignored.
        depth = h.nestingDepthFromMicroworld() + 1;
      }
    }
    if (depth < 0) {
      return null;
    }
    ESlateHandle[] names = new ESlateHandle[depth/*+1*/];
    ESlateHandle next = h;
    for (int i=0; i<depth; i++) {
      next = next.getParentHandle();
      names[i] = next;
    }
    Entry oldDir = currentMicroworldStructFile.getCurrentDirEntry();
    Entry ent = null;
    try {
      currentMicroworldStructFile.changeToRootDir();
      currentMicroworldStructFile.changeDir(
        currentMicroworldStructFile.findEntry(COMPONENT_INFO)
      );
      for (int i=depth-1; i>=0; i--) {
        next = names[i];
        try {
          ent = currentMicroworldStructFile.findEntry(next.getComponentName());
        } catch (IOException ioe) {
          if (state == SAVING) {
            ent =
              currentMicroworldStructFile.createDir(next.getComponentName());
            currentMicroworldStructFile.changeDir(ent);
            currentMicroworldStructFile.createDir(SUBSTATES);
            currentMicroworldStructFile.createFile(STATE);
          }else{
            throw ioe;
          }
        }
        currentMicroworldStructFile.changeDir(ent);
        ent = currentMicroworldStructFile.findEntry(SUBSTATES);
        currentMicroworldStructFile.changeDir(ent);
      }
      if (state == LOADING) {
        ent = currentMicroworldStructFile.findEntry(h.getComponentName());
        if (ent.getType() == Entry.DIRECTORY) {
          currentMicroworldStructFile.changeDir(ent);
          ent = currentMicroworldStructFile.findEntry(STATE);
        }
      }else{
        if (h.isEmpty()) {
          ent = currentMicroworldStructFile.createFile(h.getComponentName());
        }else{
          ent = currentMicroworldStructFile.createDir(h.getComponentName());
          currentMicroworldStructFile.changeDir(ent);
          ent = currentMicroworldStructFile.createDir(SUBSTATES);
          ent = currentMicroworldStructFile.createFile(STATE);
        }
      }
    } catch (IOException ioe) {
      System.err.println("***BEGIN STACK TRACE");
      ioe.printStackTrace();
      System.err.println("***END STACK TRACE");
      ent = null;
    }
    try {
      currentMicroworldStructFile.changeDir(oldDir);
    } catch (IOException ioe) {
      System.err.println("***BEGIN STACK TRACE");
      ioe.printStackTrace();
      System.err.println("***END STACK TRACE");
    }
    return ent;
  }

  /**
   * Returns the structured file entry associated with a particular component,
   * taking the component hierarchy into account. This method should only
   * called when loading a microworld.
   * @param     parent  The E-Slate handle of the parent of the component.
   * @param     name    The name of the component.
   * @return    The requested structured file entry.
   */
  private Entry getEntryFromName(ESlateHandle parent, String name)
  {
    Entry oldDir = currentMicroworldStructFile.getCurrentDirEntry();

    Entry ent;

    if ((microworldVersion == 6) &&
        (microworldRevision < MICROWORLDISCOMPONENT0)) {
      try {
        // Component states are stored as individual files in the
        // COMPONENT_INFO folder. There is no component hierarchy.
        currentMicroworldStructFile.changeToRootDir();
        currentMicroworldStructFile.changeDir(
          currentMicroworldStructFile.findEntry(COMPONENT_INFO)
        );
        ent = currentMicroworldStructFile.findEntry(name);
      }catch (IOException ioe) {
        System.err.println("***BEGIN STACK TRACE");
        ioe.printStackTrace();
        System.err.println("***END STACK TRACE");
        ent = null;
      }
    }else{
      // Component states are stored hierarchically.
      ent = getEntryFromHandle(parent);
      if (ent == null) {
        return null;
      }
      // getEntryFromHandle() will return the parent's "State" subfile, while
      // we want the component's subfolder, which is in the parent of "State".
      ent = ent.getParent();
      try {
        currentMicroworldStructFile.changeDir(ent);
        ent = currentMicroworldStructFile.findEntry(SUBSTATES);
        currentMicroworldStructFile.changeDir(ent);
        ent = currentMicroworldStructFile.findEntry(name);
        if (ent.getType() == Entry.DIRECTORY) {
          currentMicroworldStructFile.changeDir(ent);
          ent = currentMicroworldStructFile.findEntry(STATE);
        }
      } catch (IOException ioe) {
        System.err.println("***BEGIN STACK TRACE");
        ioe.printStackTrace();
        System.err.println("***END STACK TRACE");
        ent = null;
      }
    }

    try {
      currentMicroworldStructFile.changeDir(oldDir);
    } catch (IOException ioe) {
      System.err.println("***BEGIN STACK TRACE");
      ioe.printStackTrace();
      System.err.println("***END STACK TRACE");
    }
    return ent;
  }

  /**
   * Creates a new instance of a component whose class has a constructor that
   * takes an <code>ObjectInput</code> as an argument. The created component
   * is registered with E-Slate and assigned the specified name.
   * @param     cl      The class of the component.
   * @param     parent  The E-Slate handle of the component's parent.
   * @param     name    The name of the component.
   * @return    The created component. If <cl>code</code> does not have
   *            this kind of constructor, <code>null</code> is returned.
   * @exception Exception       Thrown when something goes wrong.
   */
  public Object instantiateComponent(Class cl, ESlateHandle parent, String name)
    throws Exception
  {
    int oldState = getState();
    setState(LOADING);
    try {
      // Kludge: we have to tell E-SlateHandle what the parent of the next
      // handle it creates is, otherwise the handle will not be in its correct
      // place in the E-Slate handle hierarchy, and restoring the component's
      // state will fail. Ditto for the new Handle's name.
      ESlateHandle.nextParent = parent;
      ESlateHandle.nextName = name;
      Constructor constr;
      try {
        Class<?> c = cl;
        //constr = cl.getConstructor(esr2ConstructorArgs);
        constr = c.getConstructor(ObjectInput.class);
      } catch (Exception ex) {
        constr = null;
      }
      Object obj = null;
      if (constr != null) {
        Entry ent = getEntryFromName(parent, name);
        if (ent != null) {
          StructInputStream si = new StructInputStream(
            currentMicroworldStructFile, ent
          );
          ObjectInputStream oi = new ObjectInputStream(si);
          try {
            //esr2ConstructorArg[0] = oi;
            //obj = constr.newInstance(esr2ConstructorArg);
            obj = constr.newInstance(oi);
            inittedObjects.put(obj, null);
          } finally {
            try {
              oi.close();
            } catch (Exception ex) {
            }
          }
        }
      }
      if (obj != null) {
        ESlateHandle h;
        //h = getComponentHandle(obj);
        //if (h == null) {
          h = ESlateHandle.getESlateHandle(obj);
        //}
        if (!parent.contains(h)) {
          parent.add(h);
        }
        // Let the component's handle know that it can now honor rename
        // requests.
        h.inInstantiateComponent = false;
        h.setComponentName(name);       // Not needed?
      }
      return obj;
    }finally{
      // End kludge.
      ESlateHandle.nextParent = null;
      ESlateHandle.nextName = null;
      setState(oldState);
    }
  }

  /**
   * Ensures that no plug is selected as the first canmdidate plug for a
   * connection.
   */
  static void resetSelectedPlug()
  {
    firstPlug = null;
    firstHandle = null;
  }

  /**
   * Returns the name of the file where the current microworld is being saved.
   * If the current microworld is <EM>not</EM> being saved, this method
   * returns null.
   * @return    The requested file name.
   */
  public String getSaveFile()
  {
    return currentSaveFile;
  }

  /**
   * Returns the name or the URL of the file from which the current
   * microworld is being loaded.
   * If the current microworld is <EM>not</EM> being loaded, this method
   * returns null.
   * @return    The requested file name.
   */
  public String getLoadFile()
  {
    return currentLoadFile;
  }

  /**
   * Returns a reference to the structured file from where the microworld was
   * last loaded or to where it was last saved.
   * @return   The requested reference. If The microworld is a new one that
   *           has never been saved or loaded from a file, this method
   *           returns null.
   */
  StructFile getMicroworldFile()
  {
    return currentMicroworldStructFile;
  }

  /**
   * Returns the document base of the microworld. This is usually the URL of
   * the parent directory of the currently open microworld file.
   * @return    The requested URL.
   */
  public URL getDocumentBase()
  {
    return documentBase;
  }

  /**
   * Sets the document base of the microworld. This value is only returned
   * by getDocumentBase() and is not used by E-Slate.
   * @param     directory       The name of the directory to which the
   *                            microworld's document base will point.
   */
  public void setDocumentBase(File directory)
  {
    String dir = directory.getAbsolutePath();
    documentBase = parentURL(dir + File.separator +  "dummy");
  }

  /**
   * Sets the document base of the microworld. This value is only returned
   * by getDocumentBase() and is not used by E-Slate.
   * @param     documentBase    The URL of the microworld's document base.
   */
  public void setDocumentBase(URL documentBase)
  {
    this.documentBase = documentBase;
  }

  /**
   * Returns the locale under which E-Slate is running.
   * @return    A reference to the locale.
   */
  public static Locale getCurrentLocale()
  {
    setCurrentLocale();
    return currentLocale;
  }

  /**
   * Sets the current locale. Currently, this is a reference to the default
   * locale.
   */
  private static void setCurrentLocale()
  {
    if (currentLocale == null) {
      currentLocale = Locale.getDefault();
    }
  }

  /**
   * Returns a desktop pane that can be used for the plug editor.
   * @param     initialSize     The initial size of the desktop pane,
   *                            provided as a hint
   * @return    The requested pane.
   */
  private PlugViewDesktopPane plugTreeView(Dimension initialSize)
  {
    PlugViewDesktopPane ptv = new PlugViewDesktopPane(this, initialSize);

    addComponentAddedListener(ptv);
    addComponentRemovedListener(ptv);

    return ptv;
  }

  /**
   * Adds the tree representations of the plugs of the components in the
   * currently active microworld to the desktop pane of the plug editor.
   * @param     ptv     The desktop pane.
   */
  private void populatePlugTreeView(PlugViewDesktopPane ptv)
  {
    ESlateHandle[] handles = myHandle.toArray();
    for (int i=0; i<handles.length; i++) {
      if (handles[i] == null) {
        continue;
      }
      PlugViewFrame f = new PlugViewFrame(handles[i]);
      ptv.add(f, JLayeredPane.PALETTE_LAYER);
      f.setVisible(true);
      f.moveToFront();
    }
  }

  /**
   * Returns the ESlateHandle of a given component.
   * @param     component       The component whose handle should be returned.
   * @return    The requested handle. If the component does not belong to this
   *            microworld, this method returns null.
   */
  public ESlateHandle getComponentHandle(Object component)
  {
    ESlateHandle[] handles = getAllHandlesInHierarchy();
    for (int i=0; i<handles.length; i++) {
      ESlateHandle handle = handles[i];
      Object comp = handle.getComponent();
      if (comp.equals(component)) {
        return handle;
      }
    }
    return null;
  }

  /**
   * Returns the ESlateHandle of a given component by searching in all created
   * microworlds. This function may be potentially more expensive than
   * <code>getComponentHandle()</code>. Use only if you do not have a
   * reference to a microworld on which to invoke the latter.
   * @param     component       The component whose handle should be returned.
   * @return    The requested handle. If the component does not belong to any
   *            microworld, this method returns null.
   */
  public ESlateHandle exhaustiveGetComponentHandle(Object component)
  {
    synchronized (microworlds) {
      int n = microworlds.size();
      for (int i=0; i<n; i++) {
        ESlateMicroworld mw = microworlds.get(i);
        ESlateHandle h = mw.getComponentHandle(component);
        if (h != null) {
          return h;
        }
      }
      return null;
    }
  }

  /**
   * Display an informational message in a new window. The contents of the
   * window are specified in three parts, which are displayed in three rows.
   * @param     info1   The first part of the window's contents.
   * @param     info2   The second part of the window's contents.
   *                    It can be a string, an icon, or any object that has a
   *                    to_String() method.
   * @param     info3   The third part of the window's contents.
   */
  private void showInfoWindow(String info1, Object info2, String info3)
  {
    if (infoWindowEnabled) {
      Frame fr = new Frame();
      fr.setIconImage(ESlate.getIconImage());
      infoWindow = new Window(fr);
      JPanel p = new JPanel(true);
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      p.setBorder(BorderFactory.createRaisedBevelBorder());

      if (info1 != null) {
        JLabel lab1 = new JLabel(info1);
        Font font = lab1.getFont();
        //lab1.setFont(new Font("Helvetica", Font.BOLD, 14));
        lab1.setFont(font.deriveFont(Math.max(font.getSize(), 14), Font.BOLD));
        lab1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        lab1.setForeground(new Color(0, 110, 52));
        p.add(lab1);
      }

      if (info2 != null) {
        JLabel lab2;
        if (info2 instanceof Icon) {
          lab2 = new JLabel((Icon)info2);
        }else{
          if (info2 instanceof String) {
            lab2 = new JLabel((String)info2);
          }else{
            lab2 = new JLabel(info2.toString());
          }
        }
        lab2.setHorizontalAlignment(JLabel.CENTER);
        lab2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        p.add(lab2);
      }

      if (info3 != null) {
        JLabel lab3 = new JLabel(info3);
        Font font = lab3.getFont();
        //lab3.setFont(new Font("Helvetica", Font.ITALIC, 14));
        lab3.setFont(
          font.deriveFont(Math.max(font.getSize(), 14), Font.ITALIC)
        );
        lab3.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        lab3.setForeground(new Color(0, 110, 52)); //0, 48, 159
        p.add(lab3);
      }

      infoWindow.add(p);
      infoWindow.pack();
      // Center window on screen--This may not work under X11, where the window
      // manager may do as it pleases!
      Dimension screen = infoWindow.getToolkit().getScreenSize();
      Dimension win = infoWindow.getPreferredSize();
      infoWindow.setLocation((screen.width - win.width) / 2,
                         (screen.height - win.height) / 2);
      infoWindow.setVisible(true);
      infoWindow.toFront();
      p.revalidate();
      p.paintImmediately(p.getVisibleRect());
    }
  }

  /**
   * Hide the informational window displayed by showInfoWindow().
   */
  private void hideInfoWindow()
  {
    // Always hide the window if it exists, without checking infoWindowEnabled.
    // This way, we cover the case where the infoWindowEnabled flag. is set to
    // false while the window is being displayed.
    if (infoWindow != null) {
      infoWindow.setVisible(false);
      ESlateHandle.removeAllRecursively(infoWindow);
      infoWindow.dispose();
      infoWindow = null;
    }
  }

  /**
   * Specifies whether an informational window should be displayed when
   * loading or saving the microworld (default = true).
   * @param     status  Yes if true, no if false.
   */
  public void setInfoWindowEnabled(boolean status)
  {
    infoWindowEnabled = status;
  }

  /**
   * Checks whether an informational window will be displayed when loading
   * the microworld.
   * @return    True if yes, false if no.
   */
  public boolean isInfoWindowEnabled()
  {
    return infoWindowEnabled;
  }

  /**
   * Add a listener for primitive group added events.
   * @param     listener        The listener to add.
   */
  public void addPrimitiveGroupAddedListener(
    PrimitiveGroupAddedListener listener)
  {
    synchronized (primitiveGroupAddedListeners) {
      if (!primitiveGroupAddedListeners.contains(listener)) {
        primitiveGroupAddedListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener for primitive group added events.
   * @param     listener        The listener to remove.
   */
  public void removePrimitiveGroupAddedListener(
    PrimitiveGroupAddedListener listener)
  {
    synchronized(primitiveGroupAddedListeners) {
      int i = primitiveGroupAddedListeners.indexOf(listener);
      if (i >= 0) {
        primitiveGroupAddedListeners.remove(i);
      }
    }
  }

  /**
   * Fire the listeners for primitive group added events.
   * @param     e       The event to send to all listeners.
   */
  public void firePrimitiveGroupAddedListeners(PrimitiveGroupAddedEvent e)
  {
    if (primitiveGroupAddedListeners.size() > 0) {
      PrimitiveGroupAddedListenerBaseArray listeners;
      synchronized (primitiveGroupAddedListeners) {
        listeners =
          (PrimitiveGroupAddedListenerBaseArray)
            (primitiveGroupAddedListeners.clone());
      }
      int nListeners = listeners.size();
      for (int i=0; i<nListeners; i++) {
        PrimitiveGroupAddedListener l = listeners.get(i);
        l.primitiveGroupAdded(e);
      }
    }
  }

  /**
   * Generate primitive group added events for all the primitive groups
   * supported by a component and all its subcomponents. This method is
   * invoked when a component is added to a microworld, to ensure that
   * and primitive groups that the microworld's primitive group added
   * listeners are invoked, even when the primitive groups were added to the
   * component's handle when the component did not belong to any microworld.
   * @param     h       The E-Slate handle of the component.
   */
  private void firePrimitiveGroupAddedListeners(ESlateHandle h)
  {
    String[] pg = h.getSupportedPrimitiveGroups();
    PrimitiveGroupAddedEvent e = new PrimitiveGroupAddedEvent(h, pg);
    firePrimitiveGroupAddedListeners(e);
    ESlateHandle[] children = h.getChildHandles();
    int n = children.length;
    for (int i=0; i<n; i++) {
      firePrimitiveGroupAddedListeners(children[i]);
    }
  }

  /**
   * Add a listener for component added events.
   * @param     listener        The listener to add.
   */
  public void addComponentAddedListener(ComponentAddedListener listener)
  {
    synchronized (componentAddedListeners) {
      if (!componentAddedListeners.contains(listener)) {
        componentAddedListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener for component added events.
   * @param     listener        The listener to remove.
   */
  public void removeComponentAddedListener(ComponentAddedListener listener)
  {
    synchronized(componentAddedListeners) {
      int i = componentAddedListeners.indexOf(listener);
      if (i >= 0) {
        componentAddedListeners.remove(i);
      }
    }
  }

  /**
   * Fire the listeners for component added events.
   * @param     e       The event to send to all listeners.
   */
  public void fireComponentAddedListeners(ComponentAddedEvent e)
  {
    if (componentAddedListeners.size() > 0) {
      ComponentAddedListenerBaseArray listeners;
      synchronized (componentAddedListeners) {
        listeners =
          (ComponentAddedListenerBaseArray)(componentAddedListeners.clone());
      }
      int nListeners = listeners.size();
      for (int i=0; i<nListeners; i++) {
        ComponentAddedListener l = listeners.get(i);
        l.componentAdded(e);
      }
    }
  }

  /**
   * Add a listener for component removed events.
   * @param     listener        The listener to add.
   */
  public void addComponentRemovedListener(ComponentRemovedListener listener)
  {
    synchronized (componentRemovedListeners) {
      if (!componentRemovedListeners.contains(listener)) {
        componentRemovedListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener for component removed events.
   * @param     listener        The listener to remove.
   */
  public void removeComponentRemovedListener(ComponentRemovedListener listener)
  {
    synchronized(componentRemovedListeners) {
      int i = componentRemovedListeners.indexOf(listener);
      if (i >= 0) {
        componentRemovedListeners.remove(i);
      }
    }
  }

  /**
   * Fire the listeners for component removed events.
   * @param     e       The event to send to all listeners.
   */
  public void fireComponentRemovedListeners(ComponentRemovedEvent e)
  {
    if (componentRemovedListeners.size() > 0) {
      ComponentRemovedListenerBaseArray listeners;
      synchronized (componentRemovedListeners) {
        listeners =
          (ComponentRemovedListenerBaseArray)
            (componentRemovedListeners.clone());
      }
      int nListeners = listeners.size();
      for (int i=0; i<nListeners; i++) {
        ComponentRemovedListener l = listeners.get(i);
        l.componentRemoved(e);
      }
    }
  }

  /**
   * Sets the name of the microworld.
   * @param     name    The name of the microworld.
   * @exception NameUsedException       This exception is thrown when trying
   *                    to rename a microworld using a name that is being used
   *                    by another microworld.
   * @exception RenamingForbiddenException      This exception is thrown when
   *                    renaming components in the microworld owning this
   *                    microworld is not allowed, or when renaming the
   *                    microworld has been explicitly forbidden using the
   *                    <code>setMicroworldRenamingAllowed</code> method.
   * @exception IllegalArgumentException        This exception is thrown
   *                    when trying to rename the microworld using a null or
   *                    empty name or if the name contains the name service
   *                    separator character.
   */
  public void setName(String name)
    throws NameUsedException, RenamingForbiddenException,
           IllegalArgumentException
  {
    boolean oldRenamingAllowed = renamingAllowed;
    try {
      if (microworldRenamingAllowed != null) {
        renamingAllowed = microworldRenamingAllowed.booleanValue();
      }
      myHandle.setComponentName(name);
    } finally {
      renamingAllowed = oldRenamingAllowed;
    }
  }

  /**
   * Sets the name of the microworld. If there is another microworld with the
   * same name, and underscore and an appropriate digit is added, to ensure
   * that the name is unique.
   * @param     name    The name of the microworld.
   * @exception RenamingForbiddenException      This exception is thrown when
   *                    renaming components in the microworld owning this
   *                    microworld is not allowed.
   */
  public void setUniqueName(String name) throws RenamingForbiddenException
  {
    myHandle.setUniqueComponentName(name);
  }

  /**
   * Returns the name of the microworld.
   * @return    The requested name.
   */
  public String getName()
  {
    return myHandle.getComponentName();
  }

  /**
   * Returns a reference to a microworld that has a given name.
   * @param     name    The name of the microworld.
   * @return    The requested reference or null if such a microworld does not
   *            exist.
   */
  public static ESlateMicroworld getMicroworldByName(String name)
  {
    if (name == null) {
      name = "";
    }
    synchronized (microworlds) {
      int nMicroworlds = microworlds.size();
      for (int i=0; i<nMicroworlds; i++) {
        ESlateMicroworld mw = microworlds.get(i);
        if (ESlateStrings.upperCase(name, currentLocale).equals(
            ESlateStrings.upperCase(mw.getName(), currentLocale))){
          return mw;
        }
      }
    }
    return null;
  }

  /**
   * Returns a list of all known microworlds.
   * @return    The requested list.
   */
  public static ESlateMicroworld[] getMicroworlds()
  {
    ESlateMicroworld[] mWorlds = null;

    synchronized (microworlds) {
      int size = microworlds.size();
      mWorlds = new ESlateMicroworld[size];
      for (int i=0; i<size; i++) {
        mWorlds[i] = microworlds.get(i);
      }
    }
    return mWorlds;
  }

  /**
   * Returns the microworld in which a given component belongs.
   * @param     component       The component whose microworld will be
   *                            returned.
   * @return    The requested microworld. If the component does not belong to
   *            any microworld, this method returns null.
   */
  public static ESlateMicroworld getComponentMicroworld(Object component)
  {
    synchronized (microworlds) {
      int size = microworlds.size();
      for (int i=0; i<size; i++) {
        ESlateMicroworld mw = microworlds.get(i);
        if (mw.getComponentHandle(component) != null) {
          return mw;
        }
      }
      return null;
    }
  }

  /**
   * Sets the environment (container) under which the microworld is running.
   * If the container implements the ESlatePart interface or it has a
   * getESlateHandle() method, its E-Slate handle is added to this microworld.
   * @param     container       The environment (container) under which the
   *                            microworld is running.
   */
  public void setMicroworldEnvironment(Object container)
  {
    this.container = container;
  }

  /**
   * Returns a reference to the environment (container) under which the
   * microworld is running.
   * @return    The requested reference.
   */
  public Object getMicroworldEnvironment()
  {
    return container;
  }

  /**
   * Returns a reference to the environment (container) under which the
   * microworld is running. Convienience method for use in JDialog
   * constructors.
   * @return    The requested reference. If the container is not an instance
   *            of java.awt.Component, this method returns null.
   */
  public Component getMicroworldEnvironmentAsComponent()
  {
    if (container instanceof Component) {
      return (Component)container;
    }else{
      return null;
    }
  }

  /**
   * Returns a reference to the Frame containing the environment (container)
   * under which the microworld is running. Convenience method for use in
   * dialog constructors.
   * @return    The requested reference. If the container is not an instance
   *            of java.awt.Frame or an instance of java.awt.Component running
   *            within a Frame, this method returns null.
   */
  public Frame getMicroworldEnvironmentFrame()
  {
    if (container == null) {
      return null;
    }
    if (container instanceof Frame) {
      return (Frame)container;
    }
    if (container instanceof Component) {
      return (Frame)SwingUtilities.getAncestorOfClass(
        Frame.class, (Component)container
      );
    }
    return null;
  }

  /**
   * Returns the E-Slate handle of the environment (container) under which the
   * microworld is running.
   * @return    The requested handle. If the environment has not been
   *            specified, or it does not implement the ESlatePart iterface,
   *            or it does not have a getESlateHandle() method, so that its
   *            handle can be retrieved, this method returns null.
   */
  public ESlateHandle getMicroworldEnvironmentHandle()
  {
    if (container == null) {
      return null;
    }

    // If the container implements ESlatePart, invoke its getESlateHandle()
    // method.
/*
    if (container instanceof ESlatePart) {
      return ((ESlatePart)container).getESlateHandle();
    }
*/

    // Otherwise, see if it has a getESlateHandle() method, and invoke that,
    // instead.
    Method m = null;
    try {
      Class[] args = new Class[0];
      Class<?> cl = container.getClass();
      m = cl.getMethod("getESlateHandle", args);
      if (m.getReturnType() != ESlateHandle.class) {
        m = null;
      }
    } catch (Exception e) {
      m = null;
    }
    if (m != null) {
      try {
        return (ESlateHandle)(m.invoke(container, new Object[0]));
      } catch (Exception e) {
      }
    }

    // If no handle can be obtained, return null.
    return null;
  }

  /**
   * Returns a reference to the desktop pane used by the plug view window.
   * @return    The requested desktop pane.
   */
  public PlugViewDesktop getPlugViewDeskTop()
  {
    if (ptvWindow == null) {
      showPlugViewWindow();
      ptvWindow.setVisible(false);
      ptvShown = Boolean.FALSE;
    }
    return (PlugViewDesktop)(ptvWindow.getContentPane());
  }

  /**
   * Returns a reference to the window where the plug view window is displayed.
   * @return    The requested window.
   */
  public JFrame getPlugViewWindow()
  {
    if (ptvWindow == null) {
      showPlugViewWindow();
      ptvWindow.setVisible(false);
      ptvShown = Boolean.FALSE;
    }
    return ptvWindow;
  }

  /**
   * Specifies whether renaming components is allowed.
   * @param     status  Yes if true, no if false.
   * @exception BadKeyException Thrown if a security key has been specified
   *                            using the <code>setRenamingAllowed(boolean,
   *                            String)</code> method.
   */
  final public void setRenamingAllowed(boolean status) throws BadKeyException
  {
    setRenamingAllowed(status, null);
  }

  /**
   * Specifies whether renaming components is allowed.
   * @param     status  Yes if true, no if false.
   * @param     key     A key, used for confirmation. The first time a
   *                    non-null key is provided, it is recorded. Subsequent
   *                    calls to this method will fail if the provided key
   *                    does not match the recorded key. The specified key is
   *                    used for both the <code>setRenamingAllowed</code>
   *                    and the <code>setMicroworldRenamingAllowed</code>
   *                    methods.
   * @exception BadKeyException Thrown if the provided key does not match the
   *                    stored key.
   */
  final public void setRenamingAllowed(boolean status, String key)
    throws BadKeyException
  {
    if (renamingAllowedKey == null) {
      renamingAllowedKey = key;
    }
    if ((renamingAllowedKey != null) && !renamingAllowedKey.equals(key)) {
      if (key == null) {
        throw new BadKeyException(resources.getString("noKey"));
      }else{
        throw new BadKeyException(resources.getString("badKey"));
      }
    }else{
      renamingAllowed = status;
    }
  }

  /**
   * Specifies whether renaming components is allowed, bypassing the key
   * mechanism. This method only works if invoked from within the ESLateHandle
   * class.
   * @param     status  Yes if true, no if false.
   * @exception IllegalAccessException  Thrown if this method is called from
   *                    within any class other than ESlateHandle. No exception
   *                    will be thrown if executing this method will not
   *                    result in any change, as the check for the invoking
   *                    class is expensive.
   */
  void internalSetRenamingAllowed(boolean status) throws IllegalAccessException
  {
    if (renamingAllowedKey != null) {
      if (renamingAllowed != status) {
        StackTrace st = new StackTrace();
        int depth = st.getDepth();
        if ((depth < 2) ||
            !st.getClassName(1).equals(ESlateHandle.class.getName())) {
          throw new IllegalAccessException("fromESlateHandle");
        }
      }
    }
    renamingAllowed = status;
  }

  /**
   * Checks whether renaming components is allowed.
   * @return    True if yes, false otherwise.
   */
  final public boolean isRenamingAllowed()
  {
    return renamingAllowed;
  }

  /**
   * Specifies whether renaming the microworld is allowed.
   * This setting affects the <code>setName</code> method, overriding the
   * setting specified by <code>setRenamingAllowed<code>meythod.
   * @param     status  Yes if true, no if false.
   * @exception BadKeyException Thrown if a security key has been specified
   *                            using the
   *                            <code>setMicroworldRenamingAllowed(boolean,
   *                            String)</code> method.
   */
  final public void setMicroworldRenamingAllowed(boolean status)
    throws BadKeyException
  {
    setMicroworldRenamingAllowed(status, null);
  }

  /**
   * Specifies whether renaming the microworld is allowed.
   * This setting affects the <code>setName</code> method, overriding the
   * setting specified by <code>setRenamingAllowed<code>meythod.
   * @param     status  Yes if true, no if false.
   * @param     key     A key, used for confirmation. The first time a
   *                    non-null key is provided, it is recorded. Subsequent
   *                    calls to this method will fail if the provided key
   *                    does not match the recorded key. The specified key is
   *                    used for both the
   *                    <code>setMicroworldRenamingAllowed</code> and the
   *                    <code>setRenamingAllowed</code> methods.
   * @exception BadKeyException Thrown if the provided key does not match the
   *                    stored key.
   */
  final public void setMicroworldRenamingAllowed(boolean status, String key)
    throws BadKeyException
  {
    if (renamingAllowedKey == null) {
      renamingAllowedKey = key;
    }
    if ((renamingAllowedKey != null) && !renamingAllowedKey.equals(key)) {
      if (key == null) {
        throw new BadKeyException(resources.getString("noKey"));
      }else{
        throw new BadKeyException(resources.getString("badKey"));
      }
    }else{
      microworldRenamingAllowed = new Boolean(status);
    }
  }

  /**
   * Checks whether renaming the microworld is allowed.
   * @return    True if yes, false otherwise.
   */
  final public boolean isMicroworldRenamingAllowed()
  {
    if (microworldRenamingAllowed != null) {
      return microworldRenamingAllowed.booleanValue();
    }else{
      return renamingAllowed;
    }
  }

  /**
   * Returns the URL of the parent directory of a file.
   * @param file        The name of the file.
   * @return    The requested URL.
   */
  private static URL parentURL(String file)
  {
/*
    URL url;
    String absPath = new File(file).getAbsolutePath();
    String parentDir = new File(absPath).getParent() + File.separator;
    // Separator in URLs is actually "/" and not the system-specific character.
    parentDir = parentDir.replace(File.separatorChar, '/');
    try {
      // The resulting URL has too many "/"s in it, but it works under
      // Netscape and IE.
      // Using "" or null for the host name produces a URL that works under
      // UNIX but fails under Windows.
      url = new URL("file", "/", parentDir);
      //Making a new URL from the above URL cleans up the extra "/"s and makes
      //it work under Java as well.
      url = new URL(url.toString());
    } catch (MalformedURLException e) {
      url = null;
    }
    return url;
*/
    try {
      File parentDir = new File(file).getParentFile();
      if (parentDir != null) {
        return parentDir.toURI().toURL();
      }else{
        return null;
      }
    } catch (MalformedURLException e) {
      return null;
    }
  }

  /**
   * Checks whether the microworld is currently being loaded from a file.
   * @return    True if yes, false if no.
   */
  public boolean isLoading()
  {
    //return (state == LOADING);
    return isLoading;
  }

  /**
   * Checks whether the microworld is currently being saved to a file.
   * @return    True if yes, false if no.
   */
  public boolean isSaving()
  {
    //return (state == SAVING);
    return isSaving;
  }

  /**
   * Sets the state of the microworld.
   * @param     newState        The new state of the microworld.
   *                            One of RUNNING, LOADING, SAVING, CLOSING,
   *                            or DEAD.
   */
  private void setState(int newState)
  {
    state = newState;
    if (state != RUNNING) {
      reregisteringAllowed = false;
    }else{
      reregisteringAllowed = true;
      reregisterComponentsForScripting();
    }
  }

  /**
   * Returns the state of the microworld.
   * @return    One of RUNNING, LOADING, SAVING, CLOSING, or DEAD.
   */
  int getState()
  {
    return state;
  }

  /**
   * Marks an E-Slate component as selected, highlighting it appropriately in
   * the plug view window.
   * @param     handle  The E-Slate handle of the component to mark as
   *                    selected. If handle is null, then all components
   *                    in the plug view window are marked as unselected.
   */
  public void setSelectedComponent(ESlateHandle handle)
  {
    if (!ignoreActivations &&
        (handle != null) && (!handle.equals(ahg.getActiveHandle()))) {
      ahg.setActiveHandle(handle);
      if (ptvWindow != null) {
        activateComponent(handle);
      }
    }
  }

  /**
   * Highlights an E-Slate component in the plug view window.
   * @param     handle  The E-Slate handle of the component.
   */
  private void activateComponent(ESlateHandle handle)
  {
    PlugViewDesktopPane pvdp =
      ((PlugViewDesktop)(ptvWindow.getContentPane())).getContentPane();
    JInternalFrame[] frames = pvdp.getAllFrames();
    int nFrames = frames.length;
    for (int i=0; i<nFrames; i++) {
      PlugViewFrame f = (PlugViewFrame)(frames[i]);
      if (handle == null) {
        try {
          f.setSelected(false);
        } catch (PropertyVetoException pve) {
        }
      }else{
        if (f.myHandle.equals(handle)) {
          try {
            f.setSelected(true);
          } catch (PropertyVetoException pve) {
          }
          break;
        }
      }
    }
  }

  /**
   * Returns the active handle of the microworld.
   * @return    The active handle of the microworld.
   */
  ESlateHandle getActiveHandle()
  {
    return ahg.getActiveHandle();
  }

  /**
   * Autoadjusts the size of the plug editor window corresponding to a given
   * E-Slate handle. If automatic resizing is turned off in the plug editor,
   * this method does nothing.
   * @param     h       The E-Slate handle corresponding to the window to
   *                    autoadjust.
   */
  void adjustPlugView(ESlateHandle h)
  {
    if (ptvWindow != null) {
      PlugViewDesktopPane pvdp =getPlugViewDeskTop().getContentPane();
      if (pvdp.isResizeFrames()) {
        JInternalFrame[] frames = pvdp.getAllFrames();
        int nFrames = frames.length;
        for (int i=0; i<nFrames; i++) {
          PlugViewFrame f = (PlugViewFrame)(frames[i]);
          if (f.myHandle.equals(h)) {
            f.autoAdjust();
            break;
          }
        }
      }
    }
  }

  /**
   * The JavaScript name of a component.
   * @param     name    The actual name of the component.
   * @param     sep     The character used as separator between components
   *                    in the hierarchy.
   * @return    The name of the component with "$" signs used as separators
   *            and underscores substituted for spaces.
   */
  private static String javaScriptName(String name, char sep)
  {
    if (name != null) {
      return name.replace(sep, '$').replace(' ', '_');
    }else{
      return null;
    }
  }

  /**
   * Register a component for JavaScript scripting.
   * @param     name    The name of the component to register.
   * @param     handle  The E-slate handle of the component to register.
   * @param     sep     The character used as separator between components
   *                    in the hierarchy.
   */
  private void registerForScripting(String name, ESlateHandle handle, char sep)
  {
    name = javaScriptName(name, sep);
    if (name != null) {
      Object compo = handle.getComponent();
      if (compo != null) {
        scriptingManager.registerName(name, compo);
      }
    }
  }

  /**
   * Unregister all components from scripting.
   */
  private void unregisterComponentsForScripting()
  {
    if (ScriptManager.active) {
      scriptingManager = ScriptManager.scriptManager;
    }else{
      scriptingManager = null;
    }
    if (scriptingManager != null) {
      int n = scriptingComponents.size();
      for (int i=0; i<n; i++) {
        String name = scriptingComponents.get(i);
        scriptingManager.unregisterName(name);
      }
      scriptingComponents.clear();
      scriptingManager = null;
    }
  }

  /**
   * Registers for scripting all the components in the microworld's hierarchy.
   */
  void registerComponentsForScripting()
  {
    if (ScriptManager.active) {
      scriptingManager = ScriptManager.scriptManager;
    }else{
      scriptingManager = null;
    }
    if (scriptingManager != null) {
      ESlateHandle h[] = getAllHandlesInHierarchy();
      int l = h.length;
      Hashtable<String, HandleAndInt> ht =
        new Hashtable<String, HandleAndInt>();
      char separator = ESlateHandle.SEPARATOR;
      for (int i=0; i<l; i++) {
        if (h[i] != null) {
          // Full path names should always be registered, so we use an invalid
          // count to identify them, and put them in the hash table
          // unconditionally.
          HandleAndInt hi = new HandleAndInt(-1, h[i]);
          String name = h[i].getComponentPathName();
          ht.put(name, hi);
          int sep = name.indexOf(separator);
          if (sep >= 0) {
            extractComponentNames(h[i], name.substring(sep+1), ht, separator);
          }
        }
      }
      Enumeration<String> e = ht.keys();
      while (e.hasMoreElements()) {
        String name = e.nextElement();
        HandleAndInt hi = ht.get(name);
        int n = hi.i;
        // Only register full path names and unique sub-path names.
        if ((n < 0) || (n == 1)) {
          registerForScripting(name, hi.handle, separator);
        }
      }
      scriptingManager = null;
    }
  }

  /**
   * Extracts all the possible names of a component. E.g., for component
   * "a.b.c", these names are "a.b.c", "b.c", and "c". For each of these
   * names, an entry in a hash table is updated: the hash key is the name, and
   * the value is the number of times it has been encountered.
   * @param     h               The E-Slate handle of the component.
   * @param     name            The name of the component.
   * @param     ht              The hashtable.
   * @param     separator       The separator used between componnt names in
   *                            the hierarchy.
   */
  private void extractComponentNames
    (ESlateHandle h, String name, Hashtable<String, HandleAndInt> ht,
     char separator)
  {
    if (name != null) {
      HandleAndInt n = ht.get(name);
      if (n == null) {
        n = new HandleAndInt(1, h);
        ht.put(name, n);
      }else{
        // Do not increment the count for full path names.
        if (n.i > 0) {
          n.i++;
        }
      }
      int sep = name.indexOf(separator);
      if (sep >= 0) {
        extractComponentNames(h, name.substring(sep+1), ht, separator);
      }
    }
  }

  /**
   * Unregisters all components from scripting, then recursively
   * registers all components again, ensuring that the registrations are
   * current.
   */
  void reregisterComponentsForScripting()
  {
    if (reregisteringAllowed) {
      unregisterComponentsForScripting();
      registerComponentsForScripting();
    }
  }

  /**
   * Specifies what to do when a component is removed from its parent.
   * @param     what    One of REPARENT_NEVER, REPARENT_RANDOM, or
   *                    REPARENT_ASK. Any other value is considered equivalent
   *                    to REPARENT_ASK.
   */
  public void setReparentType(int what)
  {
    if ((what != REPARENT_NEVER) && (what != REPARENT_RANDOM)) {
      what = REPARENT_ASK;
    }
    reparentType = what;
  }

  /**
   * Returns an indication of what will happen when a component is removed
   * from its parent.
   * @return    One of REPARENT_NEVER, REPARENT_RANDOM, or REPARENT_ASK.
   */
  public int getReparentType()
  {
    return reparentType;
  }

  /**
   * Returns the number of times a component has been hosted in another
   * component by connecting plugs.
   * @param     host    The host component.
   * @param     hosted  The hosted component.
   */
  int hostedComponentsConnections(ESlateHandle host, ESlateHandle hosted)
  {
    int nPairs = connectedPairs.size();
    int n = 0;
    for (int i=0; i<nPairs; i++) {
      ConnectedPair p = connectedPairs.get(i);
      Plug p1 = p.providerPlug;
      Plug p2 = p.dependentPlug;
      if (p1 == null || p1.internals == null ||
          p2 == null || p2.internals == null) {
        continue;
      }
      if (p1.internals.plugRole == Plug.RIGHT_ROLE) {
        Plug t = p1;
        p1 = p2;
        p2 = t;
      }
      if (p1.internals.plugRole != Plug.LEFT_ROLE) {
        //break;
        continue;
      }
      if (host.equals(p1.getHandle()) && hosted.equals(p2.getHandle()) &&
          p1.internals.isHostingPlug() && p2.internals.isHostingPlug()) {
        n++;
      }
    }
    return n;
  }

  /**
   * Update the Look and Feel of the platform's UI elements.
   */
  public void updateLookAndFeel()
  {
    // Update the L&F of the plug view editor.
    if (ptvWindow != null) {
      SwingUtilities.updateComponentTreeUI(ptvWindow);
      ptvWindow.pack();
    }
    ESlateHandle[] h = getAllHandlesInHierarchy();
    int nHandles = h.length;
    for (int i=0; i<nHandles; i++) {
      // Don't bother with the cached plug menus; simply invalidate them.
      h[i].redoPlugView = true;
      h[i].redoPlugMenu = true;
      // Update the Look and Feel of the help viewers.
      if (h[i].helpViewer != null) {
        SwingUtilities.updateComponentTreeUI(h[i].helpViewer);
      }
    }
  }

  /**
   * Returns a microworld-independent variable registry.
   * Convenience method, equivalent to ESlate.getGlobalRegistry().
   * @return    The requested registry.
   */
  public static Registry getGlobalRegistry()
  {
    return ESlate.getGlobalRegistry();
  }

  /**
   * Recursively print the names of all the ESlate handles in the microworld.
   */
  void dumpHandles()
  {
    ESlateHandle[] v = myHandle.getChildHandles();
    int n = v.length;
    System.out.println("*** " + n + " handles");
    for (int i=0; i<n; i++) {
      dumpHandles(v[i], "");
    }
  }

  /**
   * Recursively print the names of an ESlate handle and all its children.
   * @param     h               The handle.
   * @param     indentation     Printed before the name of each handle. Used
   *                            to indent handle names according to their
   *                            nesting depth.
   */
  private void dumpHandles(ESlateHandle h, String indentation)
  {
    System.out.println(indentation + h.getComponentName());
    indentation = indentation + "  ";
    ESlateHandle[] v = h.toArray();
    int n = v.length;
    for (int i=0; i<n; i++) {
      dumpHandles(v[i], indentation);
    }
  }
  /**
   * Constructs a PlugViewUndoableEdit from within a nmicroworld's context.
   * This method is required because we need to construct such instances from
   * within a static context, where PlugViewUndoableEdit's references to
   * ESlateMicroworld.this will fail.
   * @param     connect Indicates whether the edit refers to a connection or
   *                    disconnection.
   * @param     plug1   The first plug in the connection or disconnection.
   * @param     plug2   The second plug in the connection or disconnection.
   */
  private PlugViewUndoableEdit createPlugViewUndoableEdit(
    boolean connect, Plug plug1, Plug plug2)
  {
    return new PlugViewUndoableEdit(connect, plug1, plug2);
  }

  /**
   * Presents the user with a series of dialogs which allow the user to
   * associate the microworld with a set of help files in HTML format. These
   * files are incorporated in the microworld file when the microworld is
   * saved.
   * @return    <code>True</code>, if a set of help files was successfully
   *            associated, false otherwise.
   * @exception IOException     Thrown if something goes wrong.
   */
  public boolean addHelp() throws IOException
  {
    File f = HelpBuilder.buildHelp(
      getMicroworldEnvironmentAsComponent(), ESlateMicroworld.class.getName()
    );
    if (f != null) {
      // Copy help files to help directory.
      mwHelpDir = f;
      byte[] b = packHelp();
      helpDir.clear();
      mwHelpDir = unpackHelp(b, helpDir);

      // Delete help description files from user directory.
      HelpBuilder.deleteDescriptionFiles(f);

      // Associate help with the microworld.
      HelpSystemViewer hsv = myHandle.helpViewer;
      if (hsv != null) {
        hsv.dispose();
        myHandle.helpViewer = null;
      }
      mergeHelpSet(ESlateMicroworld.class);

      return true;
    }else{
      return false;
    }
  }

  /**
   * Presents the user with a series of dialogs which allow the user to
   * modify the set of help files associated with the microworld.
   * @return    <code>True</code>, if the editing was successful,
   *            <code>false</code> otherwise.
   * @exception IOException     Thrown if something goes wrong.
   */
  public boolean editHelp() throws IOException
  {
    if (mwHelpDir != null) {
      Component c = getMicroworldEnvironmentAsComponent();
      Frame fr;
      if (c instanceof Frame) {
        fr = (Frame)c;
      }else{
        fr = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, c);
      }

      JFileChooser fd;
      if (lastHelpDir == null) {
        fd = new JFileChooser(System.getProperty("user.dir"));
      }else{
        File dir = lastHelpDir.getParentFile();
        if (dir.exists()) {
          fd = new JFileChooser(dir);
          if (lastHelpDir.exists()) {
            fd.setSelectedFile(lastHelpDir);
          }
        }else{
          fd = new JFileChooser(System.getProperty("user.dir"));
        }
      }
      fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fd.setDialogTitle(resources.getString("selectDirToEditHelp"));
      int ret = fd.showOpenDialog(fr);
      if (ret == JFileChooser.APPROVE_OPTION) {
        File editDir = fd.getSelectedFile();
        lastHelpDir = editDir;

        byte[] b = packHelp();
        editDir = unpackHelp(b, editDir);

        ESlateOptionPane.showMessageDialog(
          fr,
          resources.getString("editAndHitOK1") + editDir +
            resources.getString("editAndHitOK2"),
          resources.getString("editHelp"),
          JOptionPane.PLAIN_MESSAGE
        );

        boolean result;
        File f = HelpBuilder.buildHelp(
          editDir, fr, ESlateMicroworld.class.getName(), false
        );
        if (f != null) {
          // Copy help files to help directory.
          mwHelpDir = f;
          b = packHelp();
          helpDir.clear();
          mwHelpDir = unpackHelp(b, helpDir);

          // Associate help with the microworld.
          HelpSystemViewer hsv = myHandle.helpViewer;
          if (hsv != null) {
            hsv.dispose();
            myHandle.helpViewer = null;
          }
          mergeHelpSet(ESlateMicroworld.class);

          result = true;
        }else{
          result = false;
        }

        // Clean up.
        int confirm = ESlateOptionPane.showConfirmDialog(
          fr,
          resources.getString("removeHelpDir1") + editDir +
            resources.getString("removeHelpDir2"),
          resources.getString("removeHelp"),
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
          new DirFile(editDir.getAbsolutePath()).delete();
        }

        return result;
      }
    }
    return false;
  }

  /**
   * Disassociates any help files from the microworld.
   */
  public void clearHelp()
  {
    unmergeHelpSet(ESlateMicroworld.class);
    mwHelpDir = null;
    helpDir.clear();
  }

  /**
   * Checks whether help files have been associated with the microworld.
   * @return    <code>True</code> if yes, false otherwise.
   */
  public boolean hasHelp()
  {
    return (mwHelpDir != null);
  }

  /**
   * Copies the help from the folder specified by the user to a byte array.
   * @return    A byte array containing the packed help files.
   * @exception IOException     Thrown if something goes wrong.
   */
  private byte[] packHelp() throws IOException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ZipDir.zip(mwHelpDir, out);
    out.close();
    return out.toByteArray();
  }

  /**
   * Unpacks the help from a byte array to the folder where the microworld
   * maintains help files.
   * @param     helpBytes       A byte array containing the packed help files.
   * @param     dir     The folder where the help folder will be created.
   * @return    The folder where the help was unpacked.
   * @exception IOException     Thrown if something goes wrong.
   */
  private File unpackHelp(byte[] helpBytes, File dir) throws IOException
  {
    File f = new File(
      dir,
      ClassNameString.getClassNameStringWOPackage(
        ESlateMicroworld.class.getName()
      )
    );
    f.mkdir();
    ByteArrayInputStream in = new ByteArrayInputStream(helpBytes);
    UnzipDir.unzip(f, in);
    in.close();
    return f;
  }

  /**
   * Used for component connection.
   */
  private static ESlateHandle firstHandle;

  /**
   * Used for component connection.
   */
  static Plug firstPlug = null;

  /**
   * Optional mapping of the E-Slate handles of all the components in the
   * microworld, in a flat name space.
   */
  NameServiceContext aliases;

  /**
   * The current locale.
   */
  private static Locale currentLocale;

  /**
   * Version of save file, used for consistency check. Different file versions
   * are incompatible.
   */
  private final static int saveFileVersion = 6;

  /**
   * Revision of save file supporting storage of component bounds.
   */
  private final static int SUPPORTSCOMPONENTBOUNDS = 1;

  /**
   * Revision of save file supporting storage of handle IDs.
   */
  private final static int SUPPORTSHANDLEIDS = 2;

  /**
   * Revision of save file supporting saving of plug editor preferences.
   */
  private final static int SUPPORTSPLUGEDITORPREFERENCES = 3;

  /**
   * Revision of save file supporting saving of ESlateHandle's beanXporter
   * flag.
   */
  private final static int SUPPORTSBEANXPORTER = 4;

  /**
   * Revision of save file that stores data in a StorageStructure.
   */
  private final static int SUPPORTSFIELDMAP = 5;

  /**
   * Revision of save file created fom a version of the platform where
   * the microworld is itself a component and the entire component structure
   * is saved. This was only used during development.
   */
  private final static int MICROWORLDISCOMPONENT0 = 6;

  /**
   * Revision of save file created fom a version of the platform where
   * the microworld is itself a component and the only the component structure
   * fromn the microworld and below is saved.
   */
  private final static int MICROWORLDISCOMPONENT = 7;

  /**
   * Revision of save file, used for consistency check. Different revisions
   * of the same version must be upwards compatible.
   */
  private final static int saveFileRevision = MICROWORLDISCOMPONENT;

  /**
   * Save file format version of the microworld being loaded.
   */
  private int microworldVersion;

  /**
   * Save file format revision of the microworld being loaded.
   */
  private int microworldRevision;

  /**
   * Save file identification string, used for consistency check. This should
   * <EM>never</EM> change.
   */
  private final static String saveFileString = "E-Slate microworld";

  /**
   * Reserved component name prefix used when renaming components to their
   * stored names.
   */
  final static String reservedPrefix =
    "This+is+a+@#$%^&*()_+|~reserved+name+don't+use!";

  /**
   * Localized resources.
   */
  static ResourceBundle resources;

  /**
   * The listeners for component state changed events;
   */
  private ArrayList<ComponentStateChangedListener> stateListeners;

  /**
   * The active connection pairs.
   */
  static ConnectedPairBaseArray connectedPairs =
    new ConnectedPairBaseArray();

  /**
   * Indicates that a component has just been created.
   */
  final public static int COMPONENT_CREATED = 0;
  /**
   * Indicates that a component (if it is an Applet) has just been initialized.
   */
  final public static int COMPONENT_INITIALIZED = 1;
  /**
   * Indicates that a component (if it is an Applet) has just been started.
   */
  final public static int COMPONENT_STARTED = 2;
  /**
   * Indicates that a component (if it is an Applet) has just been stopped.
   */
  final public static int COMPONENT_STOPPED = 3;
  /**
   * Indicates that a component (if it is an Applet) has just been destroyed.
   */
  final public static int COMPONENT_DESTROYED = 4;
  /**
   * Indicates that a component has just been finalized.
   */
  final static int COMPONENT_FINALIZED = 5;

  /**
   * The width of the plug tree view window.
   */
  private int ptvWidth = 640;

  /**
   * The height of the plug tree view window.
   */
  private int ptvHeight = 480;

  /**
   * Indicates whether the plug tree view window is currently being displayed.
   */
  private Boolean ptvShown = Boolean.FALSE;

  /**
   * The plug tree view window.
   */
  JFrame ptvWindow = null;

  /**
   * The file where the current microworld is being saved.
   */
  private String currentSaveFile = null;

  /**
   * The file from which the current microworld is being loaded.
   */
  private String currentLoadFile = null;

  /**
   * The file from where the microworld was loaded the time before
   * last or to where it was last saved.
   */
  private File previousMicroworldFile = null;

  /**
   * The structured file from where the microworld was loaded the time before
   * last or to where it was last saved.
   */
  private StructFile previousMicroworldStructFile = null;

  /**
   * The file from where the microworld was last loaded or to where
   * it was last saved.
   */
  private File currentMicroworldFile = null;

  /**
   * The structured file from where the microworld was last loaded or to where
   * it was last saved.
   */
  private StructFile currentMicroworldStructFile = null;

  /**
   * The document base of the current microworld. This is usually the URL of
   * the parent directory of the currently open microworld file.
   */
  private URL documentBase = null;

  /**
   * The window used to display informational messages.
   */
  private Window infoWindow = null;

  /**
   * The icon used in the "Loading microworld" window.
   */
  private ImageIcon readIcon;

  /**
   * The name of the subfile where the container stores information
   * about the microworld.
   */
  final public static String CONTAINER_INFO = "ContainerInfo";

  /**
   * The name of the subfile where E-Slate stores information about the
   * microworld.
   */
  final public static String ESLATE_INFO = "ESlateInfo";

  /**
   * The name of the subdirectory where components save their state.
   */
  final public static String COMPONENT_INFO = "ComponentInfo";

  /**
   * The name of the subdirectory where components save their private data.
   */
  final public static String COMPONENT_DATA = "ComponentData";

  /**
   * The name of the subdirectory in a component's state directory where it
   * stores its state if it has children.
   */
  final public static String STATE = "State";

  /**
   * The name of the subdirectory in a component's stae directory where it
   * stores the state of its children.
   */
  final public static String SUBSTATES = "Substates";

  /**
   * The list of listeners for the addition of new primitive groups to
   * components in the microworld.
   */
  PrimitiveGroupAddedListenerBaseArray primitiveGroupAddedListeners;

  /**
   * The list of listeners for the addition of components to the microworld.
   */
  private ComponentAddedListenerBaseArray componentAddedListeners;

  /**
   * The list of listeners for the removal of components from the microworld.
   */
  private ComponentRemovedListenerBaseArray componentRemovedListeners;

  /**
   * The list of all known microworlds.
   */
  private static ArrayList<ESlateMicroworld> microworlds =
    new ArrayList<ESlateMicroworld>();

  /**
   * The size of the plug view frame.
   */
  private Dimension ptvSize = null;

  /**
   * The viewport position of the scroll pane of the plug view frame.
   */
  private Point viewportPos = null;

  /**
   * The bounds of the plug view frame.
   */
  private Rectangle ptvWinBounds = null;

  /**
   * Indicates whether renaming components is allowed.
   */
  private boolean renamingAllowed = true;

  /**
   * Key used for accessing the "renaming allowed" function.
   */
  private String renamingAllowedKey = null;

  /**
   * Indicates whether renaming the microworld is allowed.
   */
  private Boolean microworldRenamingAllowed = null;

  /**
   * Indicates whether an informational window should be displayed while
   * loading or saving the microworld.
   */
  private boolean infoWindowEnabled = true;

  /**
   * Indicates whether audio feedback should be suppressed during plug
   * connection and disconnection.
   */
  static boolean suppressAudio = false;

  /**
   * The environment (container) under which the microworld is running.
   */
  private Object container = null;

  /**
   * The master help set.
   */
  private HelpSet masterHelpSet;

  /**
   * The frame where the master help set is displayed.
   */
  private HelpSystemViewer viewer = null;

  /**
   * Hash table associating classes with their help sets.
   */
  private Hashtable<Class, Object> helpSets;

  /**
   * The plug view window undo manager.
   */
  PlugViewUndoManager undo;

  /**
   * The -1 constatnt as a BigInteger.
   */
  private final static BigInteger MINUS_1 = new BigInteger("-1");

  /**
   * The last generated handle ID.
   */
  private BigInteger lastId = MINUS_1;

  /**
   * Buffer used for copying the component data area between two microworld
   * files.
   */
  private byte[] buf;

  /**
   * Specifies whether frames in the plug editor are resized automatically.
   */
  private boolean resizeFrames = true;

  /**
   * Specifies whether existing connections will be highlighted during
   * selection in the plug editor.
   */
  private boolean showExist = true;

  /**
   * Specifies whether compatible tree nodes are opened automatically during
   * connection/disconnection in the plug editor.
   */
  private boolean showNew = true;

  /**
   * Specifies whether tree nodes are opened automatically when a plug is
   * dragged over them in the plug editor.
   */
  private boolean autoOpen = true;

  /**
   * Specifies whether tree nodes are opened automatically, after a delay,
   * when a plug is dragged over them in the plug editor.
   */
  private boolean delayedAutoOpen = true;

  /**
   * Specifies whether compatible tree nodes are opened automatically during
   * connection/disconnection.
   */
  private boolean autoOpenCompatible = false;

  /**
   * The undo action for the plug view window.
   */
  UndoAction undoAction;

  /**
   * The redo action for the plug view window.
   */
  RedoAction redoAction;

  /**
   * State of the microworld. <EM>Always</EM> change its value using
   * <code>setState</code>. Set this variable to <code>LOADING</code> or
   * <code>SAVING</code> <EM>only</EM> while platform-specific information is
   * being read or written.
   */
  private int state = RUNNING;

  /**
   * Normal microworld state.
   */
  public final static int RUNNING = 0;
  /**
   * Microworld is being loaded from a file.
   */
  public final static int LOADING = 1;
  /**
   * Microworld is being saved to a file.
   */
  public final static int SAVING = 2;
  /**
   * Microworld is closing.
   */
  public final static int CLOSING = 3;
  /**
   * Microworld is unusable; all its resources have been deallocated.
   */
  public final static int DEAD = 4;

  /**
   * The scripting manager used for JavaScript scripting.
   */
  private ScriptManager scriptingManager = null;
  /**
   * A list of the names of the components registered for scripting.
   */
  private ArrayList<String> scriptingComponents = new ArrayList<String>();

  /**
   * The list of the original handles in input plug aliases.
   */
  private ESlateHandleBaseArray origInputHandles = new ESlateHandleBaseArray();
  /**
   * The list of the original plug names in input plug aliases.
   */
  private SBABaseArray origInputPlugNames = new SBABaseArray();
  /**
   * The list of the target handles in input plug aliases.
   */
  private ESlateHandleBaseArray targetInputHandles =
    new ESlateHandleBaseArray();
  /**
   * The list of the target plug names in input plug aliases.
   */
  private SBABaseArray targetInputPlugNames = new SBABaseArray();
  /**
   * The list of the original handles in output plug aliases.
   */
  private ESlateHandleBaseArray origOutputHandles = new ESlateHandleBaseArray();
  /**
   * The list of the original plug names in output plug aliases.
   */
  private SBABaseArray origOutputPlugNames = new SBABaseArray();
  /**
   * The list of the target handles in output plug aliases.
   */
  private ESlateHandleBaseArray targetOutputHandles =
    new ESlateHandleBaseArray();
  /**
   * The list of the target plug names in output plug aliases.
   */
  private SBABaseArray targetOutputPlugNames = new SBABaseArray();

  /**
   * Specify an alias for the input part of a shared object plug.
   */
  public final static int ALIAS_INPUT = 0;
  /**
   * Specify an alias for the output part of a shared object plug.
   */
  public final static int ALIAS_OUTPUT = 1;
  /**
   * Specify an alias for both parts of a shared object plug or for a pure
   * protocol plug.
   */
  public final static int ALIAS_INPUT_OUTPUT = 2;

  /**
   * Always destroy components that are removed from their parents.
   */
  public final static int REPARENT_NEVER = 0;
  /**
   * When a component is removed from its parent, transfer it to one of its
   * other hosts, selected at random.
   */
  public final static int REPARENT_RANDOM = 1;
  /**
   * Ask the user what to do when a component is removed from its parent.
   */
  public final static int REPARENT_ASK = 2;
  /**
   * Specifies what to do when a component is removed from its parent.
   */
  private int reparentType = REPARENT_ASK;

  /**
   * Specifies whether reregistering scripting variables is allowed.
   */
  private boolean reregisteringAllowed = true;

  /**
   * The microworld's E-Slate handle.
   */
  private ESlateHandle myHandle = null;
  /**
   * The class of the E-Slate container.
   */
  private final static String ESLATE_CONTAINER_CLASS_NAME =
    "gr.cti.eslate.base.container.ESlateContainer";

  /**
   * The folder which contains the help files for this
   * microworld.
   */
  private File mwHelpDir = null;

  /**
   * The folder where the user last chose to extract the microworld help
   * files.
   */
  private File lastHelpDir = null;

  ///**
  // * The classes of the arguments of the constructor that takes an
  // * <code>ObjectInput</code> as an argument.
  // */
  //private static Class<?>[] esr2ConstructorArgs = new Class<?>[] {ObjectInput.class};
  ///**
  // * The arguments of the constructor that takes an <code>ObjectInput</code>
  // * as an argument. We allocate the array once, so that we do not have to
  // * allocate it each time we need to invoke the constructor.
  // */
  //private Object[] esr2ConstructorArg = new Object[1];
  /**
   * A hash table containing the objects that have been created using the
   * constructor that takes an <code>ObjectInput</code> as an argument.
   * We use the objects themselves as the key, and the mapped value is
   * <code>null</code>. This way, we mark objects that have been created using
   * the special constructor, so that we do not read their state when restoring
   * the microworld.
   */
  private HashMap<Object, Object> inittedObjects =
    new HashMap<Object, Object>();
  /**
   * Indicates that the microworld is being loaded from a file.
   * Unlike the state variable which is equal to LOADING while only platform
   * specific information is being read, this variable is true from the time
   * the microworld file is opened to the time it is closed.
   */
  private boolean isLoading = false;
  /**
   * Indicates that the microworld is being saved to a file.
   * Unlike the state variable which is equal to SAVING while only platform
   * specific information is being written, this variable is true from the time
   * the microworld file is created to the time it is closed.
   */
  private boolean isSaving = false;
  /**
   * Manages the active component.
   */
  private ActivationHandleGroup ahg;
  /**
   * Specifies that calls to setSelectedComponent should be ignored.
   */
  private boolean ignoreActivations = false;

//-  PerformanceTimer saveTimer;
//-  PerformanceTimer savePrepTimer;
//-  PerformanceTimer saveStateTimer;
//-  PerformanceTimer copyCompoDataTimer;
//-  PerformanceTimer saveComposTimer;

  // DO NOT CHANGE THESE STRINGS ON PAIN OF INCOMPATIBILITY WITH PREVIOUS
  // VERSIONS. E.g., do not change "pin" into "plug"!
  private final static String ACTIVE_COMPONENTS = "activeComponents";
  private final static String PTV_SIZE = "ptvSize";
  private final static String VIEWPORTPOS = "viewportPos";
  private final static String PTV_WIN_BOUNDS = "ptvWinBounds";
  private final static String COMPONENT_CLASS = "componentClass";
  private final static String COMPONENT_NAME = "componentName";
  private final static String PLUG_VIEW_FRAME_BOUNDS = "pinViewFrameBounds";
  private final static String ICONIFIED_PLUG_VIEW_FRAME_BOUNDS =
    "iconifiedPinViewFrameBounds";
  private final static String PLUG_VIEW_FRAME_ICONIFIED =
    "pinViewFrameIconified";
  private final static String COMPONENT_BOUNDS = "componentBounds";
  private final static String H_IDS = "hIds";
  private final static String BEANXPORTER = "beanXporter";
  private final static String N_PAIRS = "nPairs";
  private final static String PROVIDER_NAME = "providerName";
  private final static String PROVIDER_CLASS = "providerClass";
  private final static String PROVIDER_PLUG_NAME = "providerPinName";
  private final static String PROVIDER_INTERNAL_PLUG_NAME =
    "providerInternalPinName";
  private final static String DEPENDENT_NAME = "dependentName";
  private final static String DEPENDENT_CLASS = "dependentClass";
  private final static String DEPENDENT_PLUG_NAME = "dependentPinName";
  private final static String DEPENDENT_INTERNAL_PLUG_NAME =
    "dependentInternalPinName";
  private final static String OLD_BOUNDS = "oldBounds";
  private final static String IS_COLLAPSED = "isCollapsed";
  private final static String MICROWORLD_NAME = "microworldName";
  private final static String RESIZE_FRAMES = "resizeFrames";
  private final static String SHOW_EXIST = "showExist";
  private final static String SHOW_NEW = "showNew";
  private final static String AUTO_OPEN = "autoOpen";
  private final static String DELAYED_AUTO_OPEN = "delayedAutoOpen";
  private final static String AUTO_OPEN_COMPATIBLE = "autoOpenCompatible";
  private final static String REPARENT_TYPE = "reparentType";
  private final static String HELP_FILES = "helpFiles";
  private final static String SELECTED_COMPONENT = "selectedComponent";
  private final static String CURRENCY_MANAGER = "currencyManager";
  private final static String CURRENCY_MANAGER_STATE = "currencyManagerState";

  /**
   * Undo action for plug view window undo/redo.
   */
  class UndoAction extends AbstractAction
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    /**
     * Create a new undo action.
     */
    UndoAction()
    {
      super(ESlateMicroworld.resources.getString("undo"));
      setEnabled(false);
    }

    /**
     * Perform the undo action.
     * @param   e       The associated action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      try {
        undo.undo();
      } catch (CannotUndoException ex) {
        System.out.println("Unable to undo: " + ex);
        ex.printStackTrace();
      }
      updateUndoState();
      redoAction.updateRedoState();
    }

    /**
     * Update the state of the action after an undo.
     */
    protected void updateUndoState()
    {
      if (undo.canUndo()) {
        setEnabled(true);
        putValue(Action.NAME, undo.getUndoPresentationName());
      } else {
        setEnabled(false);
        ResourceBundle resources = ESlateMicroworld.resources;
        putValue(Action.NAME, resources.getString("undo"));
      }
    }
  }

  /**
   * Redo action for plug view window undo/redo.
   */
  class RedoAction extends AbstractAction
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    RedoAction()
    {
      super(ESlateMicroworld.resources.getString("redo"));
      setEnabled(false);
    }

    /**
     * Perform the redo action.
     * @param   e       The associated action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      try {
        undo.redo();
      } catch (CannotRedoException ex) {
        System.out.println("Unable to redo: " + ex);
        ex.printStackTrace();
      }
      updateRedoState();
      undoAction.updateUndoState();
    }

    /**
     * Update the state of the action after a redo.
     */
    protected void updateRedoState()
    {
      if (undo.canRedo()) {
        setEnabled(true);
        putValue(Action.NAME, undo.getRedoPresentationName());
      } else {
        setEnabled(false);
        ResourceBundle resources = ESlateMicroworld.resources;
        putValue(Action.NAME, resources.getString("redo"));
      }
    }
  }

  /**
   * Undoable edit for the plug view window. Edits can be either plug
   * connections or plug disconnections.
   */
  class PlugViewUndoableEdit extends AbstractUndoableEdit
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    /**
     * Indicates whether the edit refers to a connection or disconnection.
     */
    private boolean connect;
    /**
     * The first plug in a connection or disconnection.
     */
    Plug plug1;
    /**
     * The second plug in a connection or disconnection.
     */
    Plug plug2;

    /**
     * Redo the edit.
     */
    public void redo()
    {
      super.redo();
      ESlateMicroworld mw = ESlateMicroworld.this;
      connectComponent(plug1, mw.getPlugViewDeskTop(), false);
      connectComponent(plug2, mw.getPlugViewDeskTop(), false);
    }

    /**
     * Undo the edit.
     */
    public void undo()
    {
      super.undo();
      ESlateMicroworld mw = ESlateMicroworld.this;
      connectComponent(plug1, mw.getPlugViewDeskTop(), false);
      connectComponent(plug2, mw.getPlugViewDeskTop(), false);
    }

    /**
     * Create a new edit.
     * @param   connect Indicates whether the edit refers to a connection or
     *                  disconnection.
     * @param   plug1   The first plug in the connection or disconnection.
     * @param   plug2   The second plug in the connection or disconnection.
     */
    PlugViewUndoableEdit(boolean connect, Plug plug1, Plug plug2)
    {
      super();
      this.connect = connect;
      this.plug1 = plug1;
      this.plug2 = plug2;
    }

    /**
     * Provide a localized, human readable description of the undoable form of
     * this edit, for use as an Undo menu item.
     * @return  The requested description.
     */
    public String getUndoPresentationName()
    {
      ResourceBundle resources = ESlateMicroworld.resources;
      if (connect) {
        return
          resources.getString("undoConnect") + plug1.getName() +
          resources.getString("toPlug") + plug2.getName();
      }else{
        return
          resources.getString("undoDisconnect") + plug1.getName() +
          resources.getString("fromPlug") + plug2.getName();
      }
    }

    /**
     * Provide a localized, human readable description of the redoable form of
     * this edit, for use as a Redo menu item.
     * @return  The requested description.
     */
    public String getRedoPresentationName()
    {
      ResourceBundle resources = ESlateMicroworld.resources;
      if (connect) {
        return
          resources.getString("redoConnect") + plug1.getName() +
          resources.getString("toPlug") + plug2.getName();
      }else{
        return
          resources.getString("redoDisconnect") + plug1.getName() +
          resources.getString("fromPlug") + plug2.getName();
      }
    }
  }

  /**
   * The undo manager for the plug view window undo/redo.
   */
  class PlugViewUndoManager extends UndoManager
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    /**
     * Remove all edits associated with a given E-Slate handle.
     * @param   h       The E-Slate handle.
     */
    void removeEdits(ESlateHandle h)
    {
      int n = edits.size();
      for (int i=n-1; i>=0; i--) {
        PlugViewUndoableEdit e = (PlugViewUndoableEdit)(edits.elementAt(i));
        if (h.equals(e.plug1.getHandle()) || h.equals(e.plug2.getHandle())) {
          trimEdits(i, i);
        }
      }
      undoAction.updateUndoState();
      redoAction.updateRedoState();
    }
  }

  /**
   * A wrapper for an E-Slate handle and an integer.
   */
  private class HandleAndInt
  {
    /**
     * The encapsulated integer.
     */
    int i;
    /**
     * The encapsulated handle.
     */
    ESlateHandle handle;

    /**
     * Create a handle-integer pair.
     */
    HandleAndInt(int n, ESlateHandle h)
    {
      i = n;
      handle = h;
    }
  }
}
