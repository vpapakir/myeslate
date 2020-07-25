package gr.cti.eslate.base;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.beans.beancontext.*;
import java.io.*;
import java.lang.reflect.*;
import java.math.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import javax.help.*;

import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.help.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.services.name.*;

import gr.cti.structfile.*;
import gr.cti.typeArray.*;

/**
 * This class provides a handle to E-Slate, enabling arbitrary components
 * to become E-Slate-aware.
 *
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 * @see         gr.cti.eslate.base.ESlateMicroworld
 */
@SuppressWarnings("unchecked")
public class ESlateHandle implements LogoScriptable, Access
{
  /**
   * Creates a new E-Slate handle and registers it with E-Slate. The
   * constructor should be called by ESlate.registerPart(ESlateHandle).
   * @param     owner   The component that created the handle.
   */
  ESlateHandle(Object owner)
  {
    super();

    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.base.ESlateResource",
      ESlateMicroworld.getCurrentLocale()
    );

    if (owner != null) {
      myName = ClassNameString.getClassNameStringWOPackage(
        owner.getClass().getName()
      );
    }else{
      // Should be unique in any context.
      myName = "handle@" + hashCode();
    }
    myAlias = null;
    inMousePressedListener = false;
    nameFieldShown = false;
    renamingComponent = false;
    componentInfo = null;
    myComponent = owner;
    eSlateListeners = new ESlateListenerBaseArray();
    microworldChangeListeners = new MicroworldChangedListenerBaseArray();
    treeList = new ArrayList<PlugTree>();
    primitiveGroups = new ArrayList<String>();
    beanContext = new ESlateBeanContext(this);

    // Initialize the views
    plugViewPopup = null;
    plugMenu = null;

    // Initialize the plugs
    plugs = new PlugTreeNode(this);

    // Create panels and pop-up menus.

    // Defer creating the menu panel until it is needed.
    menuPanel = null;

    // Defer creating plug view popup and plug menu until they are needed.
    redoPlugView = true;
    redoPlugMenu = true;

    enableRenaming();
    try {
      if (nextName != null) {
        // For reasons similar to those for nextParent (see below), we
        // may need to set the component's actual name at construction time.
        changeComponentName(nextName);
        nextName = null;
        inInstantiateComponent = true;
      }else{
        // If we don't do this, the component name may not appear correctly
        // on the menu bar.
        setComponentName(getComponentName());
      }
    } catch (NameUsedException e) {
    } catch (RenamingForbiddenException e) {
    }
    restoreRenaming();

    // Create associated PerformanceTimerGroup. If the performance manager
    // is active, this is done by simply requesting the group. If it is
    // inactive, the group will be created when the performance manager is
    // activated.
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    if (pm.isEnabled()) {
      pm.getPerformanceTimerGroup(this);
      addESlateListener(pm);
    }

    // Kludge: for components created via
    // ESlateMicroworld.instantiateComponent(), we have to set the handle's
    // parent immediately, otherwise the components will not be in their
    // correct place in the component hierarchy, and restoring their state
    // will fail.
    if (nextParent != null) {
      nextParent.add(this);
      if (!nextParentExternallySet) {
        nextParent = null;
      }
    }

    // Register for scripting.
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) { // Shouldn't happen any more.
      myMW.reregisterComponentsForScripting();
    }

    // Add platform-related Logo primitive group.
    addPrimitiveGroup("gr.cti.eslate.scripting.logo.ESlatePrimitives");

    if (myMW != null) { // Shouldn't happen any more.
      // Notify state changed listeners
      myMW.fireComponentStateChangedListeners(
        myComponent, ESlateMicroworld.COMPONENT_CREATED);
    
      // Notify primitive group added listeners.
      if (myMW.primitiveGroupAddedListeners.size() > 0) {
        String[] groups = getSupportedPrimitiveGroups();
        if (groups.length > 0) {
          PrimitiveGroupAddedEvent e =
            new PrimitiveGroupAddedEvent(this, groups);
          myMW.firePrimitiveGroupAddedListeners(e);
        }
      }
    }
    Object o = owner;
    if (o != null) {
      o = o.getClass().getName();
    }
  }

  /**
   * Sets the initial parent of all constructed handles.
   * If, for some reason, you need to have an E-Slate handle to be created
   * having a specific parent, you can use this method. Note that in most
   * cases you can simply create the handle (i.e., via
   * <code>ESlate.registerPart()</code>), then add it to its parent. <EM>Think
   * twice before using this method, then think again and don't use it!</EM>
   * If you do use this method, ake sure that you invoke
   * <code>setNextParent(null)</code> when you have created the handles that
   * you want, so that subsequently created handles are created without a
   * parent, as they were meant to be.
   * @param     parentHandle    The E-Slate handle that will become the parent
   *                            of subsequently created E-Slate handles.
   *                            Use <code>null</code> to restore the default
   *                            behavior.
   */
  public static void setNextParent(ESlateHandle parentHandle)
  {
    nextParent = parentHandle;
    if (parentHandle != null) {
      nextParentExternallySet = true;
    }else{
      nextParentExternallySet = false;
    }
  }

  /**
   * Returns the initial parent of all constructed handles.
   * @return    The E-Slate handle that will be the initial parent of newly
   *            constructed E-Slate handles. This is usually
   *            <code>null</code>, as E-Slate handles are usuall constructed
   *            without a parent.
   */
  public ESlateHandle getNextParent()
  {
    return nextParent;
  }

  /**
   * Sets the microworld to a given microworld.
   * @param     microworld      The microworld to which to switch.
   *                            If microworld is null, then the component is
   *                            removed from the microworld without being
   *                            added to another microworld.
   */
  void setESlateMicroworld(ESlateMicroworld microworld)
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW == null || !myMW.equals(microworld)) {
      if (myMW != null) {
        myMW.getESlateHandle().remove(this, true, false);
      }
      // This will also set the microworld recursively for the component's
      // children.
      fastSetESlateMicroworld(microworld);
      myMW = getESlateMicroworld();
      // Register the component's alias with the new microworld.
      if (myMW != null) {
        if (myAlias != null) {
          try {
            setAlias(myAlias);
          } catch (NameUsedException nue) {
            System.err.println("Alias " + myAlias + " already in use");
          }
        }
      }
    }
  }

  /**
   * Sets the microworld to a given microworld without sending any
   * notifications.
   */
  private void fastSetESlateMicroworld(ESlateMicroworld microworld)
  {
    myESlateMicroworld = microworld;
    ESlateHandle[] children = toArray();
    for (int i=0; i<children.length; i++) {
      if (children[i] != null) {
        children[i].fastSetESlateMicroworld(microworld);
      }
    }
  }

  /**
   * Overrides Object.finalize(), to clean up and invoke any component state
   * changed listeners.
   * @exception Throwable       Thrown by super.finalize().
   */
  protected void finalize() throws Throwable
  {
    // Clean up
    synchronized (disposeCalled) {
      if (!disposeCalled.booleanValue()) {;
        dispose();
      }
      disposeCalled = null;     // So that it, too, can be freed.
    }
    // Notify state changed listeners
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) {
      myMW.fireComponentStateChangedListeners(
        myComponent, ESlateMicroworld.COMPONENT_FINALIZED);
    }
    myComponent = null;
    // Better invoke super.finalize() *after* the listeners have been invoked.
    // After invoking it, the component should no longer exist!
    super.finalize();
  }

  /**
   * This method should be called by containers before disposing the handle,
   * giving the opportunity to components to veto the disposal and to inform
   * the container that their state has changed.
   * @param     cancellationRespected   This value will be copied to the
   *                    cancellationRespected attribute of the generated
   *                    HandleDisposalEvent. If true, then disposingHandle
   *                    method of an attached ESlateListener may veto the
   *                    disposal by setting the vetoDisposal attribute of the
   *                    generated HandleDisposalEvent, in which case the
   *                    container will abort the disposal.
   * @param     stateChanged    When disposing all handles in order to close a
   *                    microworld, components can set the stateChanged
   *                    attribute of the generated HandleDisposalEvent to
   *                    true, to indicate that their state has changed. This
   *                    value is copied to this parameter, so that the
   *                    container may know that the component's state has
   *                    changed, and provide an appropriate notification to
   *                    the user.
   * @return    True if an ESlateListener has vetoed the disposal of the
   *            handle, false otherwise. The container will only honor this
   *            result if <code>cancellationRespected</code> is true.
   */
  public boolean toBeDisposed(boolean cancellationRespected,
                              BooleanWrapper stateChanged)
  {
    boolean veto = false;
    boolean sc = false;
    if ((eSlateListeners != null) && (eSlateListeners.size() > 0)) {
      ESlateListenerBaseArray listeners;
      synchronized(eSlateListeners) {
        listeners = (ESlateListenerBaseArray)(eSlateListeners.clone());
      }
      int nListeners = listeners.size();
      for (int i=0; i<nListeners; i++) {
        ESlateListener l = listeners.get(i);
        HandleDisposalEvent e =
          new HandleDisposalEvent(this, cancellationRespected);
        l.disposingHandle(e);
        veto |= e.vetoDisposal;
        sc |= e.stateChanged;
      }
    }
    BooleanWrapper bw = new BooleanWrapper(false);
    ESlateHandle[] h = toArray();
    for (int i=0; i<h.length; i++) {
      h[i].toBeDisposed(false, bw);
      sc |= bw.getValue();
    }
    stateChanged.setValue(sc);
    return veto;
  }

  /**
   * Destroys the handle, and performs clean-up. Should be called by a
   * component when it is about to exit, e.g., in the case of Applets, in their
   * destroy() method. After invoking this method, the handle is unusable.
   */
  public void dispose()
  {
    // We check before entering the synchronized area, because if components
    // dispose their E-Slate handles in their clean-up routines, we are going
    // to enter this method again and block.
    if (disposeCalled.booleanValue()) {
      return;
    }

    synchronized (disposeCalled) {
      if (!disposeCalled.booleanValue()) {
        // If the handle still has a parent, then, instead of disposing the
        // handle, simply remove it from its parent.
        ESlateHandle parent = getParentHandle();
        if (parent != null) {
          parent.remove(this);
          return;
        }
        disposeCalled = Boolean.TRUE;
        // Clean up
        ESlateMicroworld myMW = getESlateMicroworld();
        if ((myMW != null) && (myMW.ptvWindow != null)) {
          removeMembershipListener(
            ((PlugViewDesktop)
              (myMW.ptvWindow.getContentPane())).getContentPane()
          );
        }
        //if (ScriptManager.active) {
        //  ScriptManager.scriptManager.unregisterComponent(this);
        //}
        Plug[] currentPlugs;
        synchronized(plugs) {
          currentPlugs = getPlugs();
        }
        ESlateMicroworld.suppressAudio = true;
        ESlateHandle[] h = toArray();
        for (int i=0; i<h.length; i++) {
          remove(h[i]);
        }
        int nPlugs = currentPlugs.length;
        for (int i=0; i<nPlugs; i++) {
          try {
            removePlug(currentPlugs[i]);
          } catch (NoSuchPlugException npe) {
          } catch (IllegalArgumentException iae) {
          }
        }
        // Dispose plugs. Do it after disconnecting all plugs, to avoid
        // getting null pointer exceptions.
        for (int i=0; i<nPlugs; i++) {
          currentPlugs[i].dispose();
        }
        if (myMW != null) {
          ESlateMicroworld.suppressAudio = true;
          myMW.getESlateHandle().remove(this);
          myMW.reregisterComponentsForScripting();
          ESlateMicroworld.suppressAudio = false;
        }
        if (menuPanel != null) {
          removeAllRecursively(menuPanel);
          menuPanel = null;
        }
        if (plugViewPopup != null) {
          removeAllRecursively(plugViewPopup);
          plugViewPopup = null;
        }
        if (plugMenu != null) {
          removeAllRecursively(plugMenu);
          plugMenu = null;
        }
        myAlias = null;
        if (!debug) {
          myName = null;
        }
        plugs = null;
        myESlateMicroworld = null;
        infoButton = null;
        plugButton = null;
        helpButton = null;
        nameLabel = null;
        nameField = null;
        componentInfo = null;
        resources = null;
        myComponent = null;
        microworldChangeListeners.clear();
        microworldChangeListeners = null;
        treeList = null;
        primitiveGroups.clear();
        primitiveGroups = null;
        mausListener = null;
        oldBounds = null;
        handleId = null;
        componentImage = null;
        if (currentDir != null) {
          currentDir.clear();
          currentDir = null;
        }
        // Close any streams that are left open.
        int nOpenStreams = openStreams.size();
        for (int i=0; i<nOpenStreams; i++) {
          Object stream = openStreams.get(i);
          try {
            if (stream instanceof ESlateStructInputStream) {
              ((ESlateStructInputStream)stream).close();
            }else{
              ((ESlateStructOutputStream)stream).close();
            }
          } catch (IOException ioe) {
          }
        }
        openStreams.clear();
        openStreams = null;
        try {
          if (helpViewer != null) {
            helpViewer.setVisible(false);
            helpViewer.dispose();
            helpViewer = null;
          }
        } catch (NoClassDefFoundError e) {
          // E-Slate help classes are not in class path
        }

        beanContext.dispose();
        beanContext = null;
        if (childComponents != null) {
          childComponents.dispose();
          childComponents = null;
        }
        hostedComponents.clear();
        hostedComponents = null;

        activationGroups.clear();
        activationGroups = null;
        associatedActivationGroups.clear();
        associatedActivationGroups = null;
/*
        java.lang.Runtime rt = java.lang.Runtime.getRuntime();
        rt.runFinalization();
        rt.gc();
        rt.gc();
        rt.gc();
        if (debug) {
          System.out.println(
            myName + ": " +
            "Total mem = " + rt.totalMemory() +
            ", free mem = " + rt.freeMemory() +
            ", used mem = " + (rt.totalMemory() - rt.freeMemory()));
          myName = null;
        }
*/
        myName = null;
      }
    }

    ESlateListenerBaseArray listeners;
    if (eSlateListeners.size() > 0) {
      synchronized(eSlateListeners) {
        listeners = (ESlateListenerBaseArray)(eSlateListeners.clone());
      }
      int nListeners = listeners.size();
      for (int i=0; i<nListeners; i++) {
        ESlateListener l = listeners.get(i);
        HandleDisposalEvent e = new HandleDisposalEvent(this);
        l.handleDisposed(e);
      }
    }
    // We can't delete the references to the E-Slate event listeners
    // before we send the disposal notification, so we do it afterwards.
    eSlateListeners.clear();
    eSlateListeners = null;
  }

  /**
   * Checks whether the E-Slate handle's <code>dispose()</code> method has
   * been called.
   * @return    True if yes, false if no. If true is returned, then the
   *            E-Slate handle is unusable.
   */
  public boolean isDisposed()
  {
    if (disposeCalled != null) {
      return disposeCalled.booleanValue();
    }else{
      return true;
    }
  }

  /**
   * Removes recursively all the Containers and Components from a Container.
   * Used for cleaning up after we have finished using a Container.
   * @param     c       The Container to clean up.
   */
  public static void removeAllRecursively(Container c)
  {
    if (c != null) {
      Component[] cc = c.getComponents();
      for (int i=0; i<cc.length; i++) {
        if (cc[i] instanceof Container) {
          removeAllRecursively((Container)(cc[i]));
          //((Container)(cc[i])).removeAll();
        }
      }
      try {
        c.removeAll();
      } catch (Exception e) {
      }
    }
  }

  /**
   * Creates the standard E-Slate component menu bar.
   * @return    A JPanel containing the menu bar.
   */
  private MenuPanel createMenuPanel()
  {
    return new MenuPanel();
  }

  /**
   * Sets up the menu bar to its normal look: component name and various
   * buttons.
   */
  private void setNormalMenuBar()
  {
    if (menuPanel != null) {
      menuPanel.cardPanel.showCard("label");
      menuPanel.add(menuPanel.buttonPanel, BorderLayout.EAST);
      menuPanel.invalidate();
      menuPanel.validate();
    }
  }

  /**
   * Sets up the menu bar so that the user can use it to rename the
   * component.
   */
  private void setRenameMenuBar()
  {
    if (menuPanel != null) {
      menuPanel.cardPanel.showCard("field");
      menuPanel.remove(menuPanel.buttonPanel);
      menuPanel.invalidate();
      menuPanel.validate();
      nameField.requestFocus();
    }
  }

  /**
   * Returns the panel where the menu bar is drawn.
   * @return    The requested panel.
   */
  public MenuPanel getMenuPanel()
  {
    if (menuPanel == null) {
      menuPanel = createMenuPanel();
    }
    return menuPanel;
  }

  /**
   * Returns whether the panel where the menu bar is drawn has been created.
   * @return    True if yes, false if no.
   */
  public boolean isMenuPanelCreated()
  {
    return (menuPanel != null);
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
    if (plug == null) {
      IllegalArgumentException iae =
        new IllegalArgumentException(resources.getString("nullPlug"));
      throw iae;
    } else {
      if (getPlug(plug.getName()) != null) {
        PlugExistsException pee =
          new PlugExistsException(
            resources.getString("aTopPlugNamed") + " " + plug.getName() + " " +
            resources.getString("alreadyAttached"));
        throw pee;
      } else {
        if (getPlugLocaleIndependent(plug.getInternalName()) != null) {
          PlugExistsException pee =
            new PlugExistsException(
              resources.getString("aTopPlugInternallyNamed") + " " +
              plug.getName() + " " + resources.getString("alreadyAttached"));
          throw pee;
        }else{
          synchronized(plugs) {
            plugs.add(plug.getPlugTreeNode());
          }
          // Invalidate the plug view and plug menu of this and all parent
          // components, so that whichever is displayed will show this
          // component's plugs correctly. Also reload the tree models of this
          // and all parent components, so that whichever is displayed on the
          // plug editor is updated.
          for (ESlateHandle h=this; h != null; h = h.getParentHandle()) {
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
    if (plug == null) {
      IllegalArgumentException iae =
        new IllegalArgumentException(resources.getString("nullPlug"));
      throw iae;
    } else {
      boolean plugExists;
      synchronized(plugs) {
        plugExists = plugs.isNodeChild(plug.getPlugTreeNode());
      }
      if (!plugExists) {
        NoSuchPlugException nspe =
          new NoSuchPlugException(
            resources.getString("component") + " " + myName + " " +
            resources.getString("dontHavePlug") + " " + plug.getName());
        throw nspe;
      } else {
        plug.disconnect();
        synchronized (plugs) {
          plugs.remove(plug.getPlugTreeNode());
        }
        // Invalidate the plug view and plug menu of this and all parent
        // components, so that whichever is displayed will show this
        // component's plugs correctly. Also reload the tree models of this
        // and all parent components, so that whichever is displayed on the
        // plug editor is updated.
        for (ESlateHandle h=this; h != null; h = h.getParentHandle()) {
          h.redoPlugView = true;
          h.redoPlugMenu = true;
          h.reloadModels();
          h.makeVisible(null);
        }
      }
    }
  }

  /**
   * Returns a reference to a given plug. Only top level plugs are scanned.
   * @param     plugName        The name of the plug.
   * @return    A reference to the requested plug.
   */
  public Plug getPlug(String plugName)
  {
    synchronized(plugs) {
      Vector plugv = plugs.getChildPlugs();
      int nChildPlugs = plugv.size();
      for (int i = 0; i < nChildPlugs; i++) {
        if (plugName != null &&
            plugName.equals(((Plug) plugv.get(i)).getName())) {
          return ((Plug) plugv.elementAt(i));
        }
      }
    }
    return null;
  }

  /**
   * Returns a reference to a given plug. Only top level plugs are scanned.
   * @param     internalPlugName        The locale-independent name of the plug.
   *                            (Usually a resource bundle key.)
   * @return    A reference to the requested plug.
   */
  public Plug getPlugLocaleIndependent(String internalPlugName)
  {
    synchronized(plugs) {
      Vector plugv = plugs.getChildPlugs();
      int nChildPlugs = plugv.size();
      for (int i = 0; i < nChildPlugs; i++) {
        if (internalPlugName != null &&
            internalPlugName.equals(((Plug) plugv.get(i)).getInternalName())) {
          return ((Plug) plugv.elementAt(i));
        }
      }
    }
    return null;
  }

  /**
   * Returns a copy of the list of all top level plugs that the component
   * contains.
   * @return    The list of plugs.
   */
  public synchronized Plug[] getPlugs()
  {
    Vector v = plugs.getChildPlugs();
    int size = v.size();
    Plug[] p = new Plug[size];
    for (int i=0; i<size; i++) {
      p[i] = (Plug)(v.elementAt(i));
    }
    return p;
  }

  /**
   * Recursively print the names of all the plugs of the component.
   */
  void dumpPlugs()
  {
    Plug[] v = getPlugs();
    int n = v.length;
    System.out.println("*** " + n + " plugs");
    for (int i=0; i<n; i++) {
      dumpPlugs(v[i], "");
    }
  }

  /**
   * Recursively print the names of a plug and all its children.
   * @param     p               The plug.
   * @param     indentation     Printed before the name of each plug. Used to
   *                            indent plug names according to their nesting
   *                            depth.
   */
  private void dumpPlugs(Plug p, String indentation)
  {
    System.out.println(
      indentation + p.getName() +" [" + p.getInternalName() + "]"
    );
    indentation = indentation + "  ";
    Plug[] v = p.getChildPlugs();
    int n = v.length;
    for (int i=0; i<n; i++) {
      dumpPlugs(v[i], indentation);
    }
  }

  /**
   * Builds the pop-up menu containing the plugs of the component.
   */
  private void createPlugViewPopup()
  {
    PlugTreeNode node = plugs;
    plugViewPopup = new ESlateJPopupMenu();
    plugViewPopup.setLightWeightPopupEnabled(lightWeightPopup);
    plugViewPopup.setBorderPainted(true);
    constructPopup(plugViewPopup, node);
  }

  /**
   * Builds the menu containing the plugs of the component.
   */
  private void createPlugMenu()
  {
    PlugTreeNode node = plugs;
    plugMenu = new ESlateJMenu();
    JPopupMenu pop = plugMenu.getPopupMenu();
    if (pop != null) {
      pop.setLightWeightPopupEnabled(lightWeightPopup);
    }
    plugMenu.setBorderPainted(true);
    constructPopup(plugMenu, node);
  }

  /**
   * Recursively constructs the popup menu.
   * @param     parentMenu      Either the popup menu itself, or a JMenu
   *                            corresponding to one of the items or sub-items
   *                            in the popup menu in any depth.
   * @param     node            The node in the plug tree structure,
   *                            corresponding to <code>parentmenu</code>.
   */
  private void constructPopup(Object parentMenu, PlugTreeNode node)
  {
    Vector children = node.getChildObjects();
    int nObjects = children.size();
    ESlateJPopupMenu popup;
    JMenu menu;
    if (parentMenu instanceof ESlateJPopupMenu) {
      popup = (ESlateJPopupMenu)parentMenu;
      menu = null;
    }else{
      popup = null;
      menu = (JMenu)parentMenu;
    }

    JMenuItem menuItem = null;

    for (int i=0; i<nObjects; i++) {
      Object nextObject = children.elementAt(i);
      Plug nextPlug;
      ESlateHandle nextHandle;
      if (nextObject instanceof Plug) {
        nextPlug = (Plug)nextObject;
        nextHandle = null;
      }else{
        nextPlug = null;
        nextHandle = (ESlateHandle)nextObject;
      }
      if (((nextPlug != null) && nextPlug.isVisible()) ||
          ((nextHandle != null) && nextHandle.isVisible())) {
        Icon icon;
        if (nextPlug != null) {
          icon = new PlugButton(nextPlug.getIcon());
        }else{
          Image im = nextHandle.getImage();
          if (im == null) {
            im = ESlate.getIconImage();
          }
          icon = new ESlateRootIcon(im);
        }
        if (popup != null) {
          if (nextPlug != null) {
            if (nextPlug.getChildPlugs().length > 0) {
              menuItem = popup.add(new ESlateMenu(nextPlug, icon));
            }else{
              menuItem = popup.add(new ESlateMenuItem(nextPlug, icon));
            }
          }else{
            if (nextHandle.plugs.getChildObjects().size() > 0) {
              menuItem = popup.add(new JMenu(nextHandle.getComponentName()));
              menuItem.setIcon(icon);
            }else{
              menuItem =
                popup.add(new JMenuItem(nextHandle.getComponentName(), icon));
            }
          }
        }else{
          if (nextPlug != null) {
            if (nextPlug.getChildPlugs().length > 0) {
              menuItem = menu.add(new ESlateMenu(nextPlug, icon));
            }else{
              menuItem = menu.add(new ESlateMenuItem(nextPlug, icon));
            }
          }else{
            if (nextHandle.plugs.getChildObjects().size() > 0) {
              menuItem = menu.add(new JMenu(nextHandle.getComponentName()));
              menuItem.setIcon(icon);
            }else{
              menuItem =
                menu.add(new JMenuItem(nextHandle.getComponentName(), icon));
            }
          }
        }
        // Fix an alignment problem with the Windows Look and Feel.
        if (menuItem instanceof ESlateMenuItem && 
            UIManager.getLookAndFeel().getName().equals("Windows")) {
          Insets ins = menuItem.getBorder().getBorderInsets(menuItem);
          menuItem.setBorder(
            new EmptyBorder(ins.top, ins.left + 1, ins.bottom, ins.right)
          );
        }
        menuItem.setHorizontalTextPosition(JButton.RIGHT);
        menuItem.setHorizontalAlignment(JButton.LEFT);
        if (menuItem instanceof JMenu) {
          ((JMenu)menuItem).getPopupMenu().setLightWeightPopupEnabled(
            lightWeightPopup
          );
        }
        if (nextPlug != null) {
          menuItem.setActionCommand(nextPlug.getName());
          menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
              MenuSelectionManager.defaultManager().clearSelectedPath();
              Object source = e.getSource();
              Plug p = null;
              if (source instanceof ESlateMenu) {
                p = ((ESlateMenu)source).getPlug();
              }else{
                if (source instanceof ESlateMenuItem) {
                  p = ((ESlateMenuItem)source).getPlug();
                }
              }
              if (p != null) {
                JComponent top = getPopupTopLevelParent();
                // Connect the components in a separate thread, so that
                // swing can restore the area behind the popup menus
                // immediately: when using lightweight menus, the area is not
                // updated until the action  is completely performed.
                Thread th = new ConnectThread(p, top);
                th.start();
              }
            }
          });
        }
      }
      if (nextPlug != null) {
        if(nextPlug.isVisible() && (nextPlug.getChildPlugs().length > 0)) {
          constructPopup(menuItem, nextPlug.getPlugTreeNode());
        }
      }else{
        if (nextHandle != null) {
          if (nextHandle.isVisible() &&
              (nextHandle.plugs.getChildObjects().size() > 0)) {
            constructPopup(menuItem, nextHandle.plugs);
          }
        }
      }
    }
  }

  /**
   * Returns the topmost JComponent in which the component's plug view menu
   * appears.
   * @return    The requested JComponent.
   */
  private JComponent getPopupTopLevelParent()
  {
    Component inv;
    if (showingMenu) {
      if (myComponent instanceof Component) {
        inv = (Component)myComponent;
      }else{
        ESlateMicroworld mw = getESlateMicroworld();
        if (mw != null) {
          inv = mw.getMicroworldEnvironmentAsComponent();
        }else{
          return null;
        }
      }
    }else{
      inv = plugViewPopup.getInvoker();
    }
    Component parent = null;
    do {
      if (inv instanceof JComponent) {
        parent = inv;
      }
      inv = inv.getParent();
    } while (inv != null);
    return (JComponent)parent;
  }

  /**
   * Check whether the plug view menu is a light weight component.
   * @return    True if yes, false if not.
   */
  public boolean isMenuLightWeight()
  {
    return lightWeightPopup;
  }

  /**
   * Specify whether, when displaying the plug view menu, E-Slate will try to
   * use a light weight popup if it fits. This feature is off by default, as
   * it can produce garbage on screen when a dialog appears over the plug menu
   * or when a component mixes light weight and heavy weight components.
   * @param     status  True to enable this feature, false to disable it
   *                    (default).
   */
  public void setMenuLightWeight(boolean status)
  {
    if (status == true) {
      System.out.println(
        "Warning: setting menus to light weight may produce garbage on screen."
      );
    }
    if (status != lightWeightPopup) {
      lightWeightPopup = status;
      redoPlugView = true;
      redoPlugMenu = true;
    }
  }

  /**
   * Displays the pop-up menu containing the plugs of the component.
   */
  void showPlugViewPopup()
  {
    showPlugViewPopup(plugButton, 0, 0);
  }

  /**
   * Displays the pop-up menu containing the plugs of the component
   * at a given position relative to a given component.
   * @param     comp    The component relative to which the pop-up menu will
   *                    be displayed.
   * @param     x       The x offset of the pop-up menu relative to the
   *                    component.
   * @param     y       The y offset of the pop-up menu relative to the
   *                    component.
   */
  public void showPlugViewPopup(Component comp, int x, int y)
  {
    showingMenu = false;
    if (redoPlugView) {
      if (plugViewPopup != null) {
        removeAllRecursively(plugViewPopup);
      } 
      synchronized (plugs) {
        createPlugViewPopup();
        redoPlugView = false;
      }
    }
    plugViewPopup.show(comp, x, y);
  }

  /**
   * Returns a menu containing the plugs of a component, suitable for
   * incorporating in other menus.
   * @return    A menu containing the plugs of a component, suitable for
   *            incorporating in other menus. Make sure that you invoke this
   *            method each time you want to display this menu.
   */
  public ESlateJMenu getPlugMenu()
  {
    showingMenu = true;
    if (redoPlugMenu) {
      if (plugMenu != null) {
        removeAllRecursively(plugMenu);
      } 
      synchronized (plugs) {
        createPlugMenu();
        redoPlugMenu = false;
      }
    }
    return plugMenu;
  }

  /**
   * Returns the microworld to which the component belongs.
   * @return    The requested microworld. If the component does not belong to
   *            any microworld, this method returns null.
   */
  public ESlateMicroworld getESlateMicroworld()
  {
    ESlateMicroworld myMW = myESlateMicroworld;
    if (myMW == null) {
      for (ESlateHandle h=this; h!=null; h=h.getParentHandle()) {
        Object compo = h.getComponent();
        if (compo instanceof ESlateMicroworld) {
          myMW = (ESlateMicroworld)compo;
          break;
        }
      }
    }
    return myMW;
  }

  /**
   * Returns the microworld to which the component belongs. If the component
   * does not belong to a microworld, all its children are searched
   * recursively, until a microworld is found. Thus, this method will return
   * the microworld on which the container is operating.
   * @return    The rquested microworld. If neither the component nor any of
   *            its children belong to a microworld, this method returns
   *            <code>null</code>.
   */
  public ESlateMicroworld getEnvironmentMicroworld()
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) {
      return myMW;
    }
    ESlateHandle h[] = toArray();
    for (int i=0; i<h.length; i++) {
      myMW = h[i].getEnvironmentMicroworld();
      if (myMW != null) {
        return myMW;
      }
    }
    return null;
  }

  /**
   * Returns the microworld associated with this component.
   * @param     handle  The component whose microworld will be returned.
   * @return    The microworld to which the component belongs. If the
   *            component is a microworld, a reference to itself is returned,
   *            instead of the microworld to which the microworld belongs.
   */
  private static ESlateMicroworld getESlateMicroworld(ESlateHandle handle)
  {
    return handle.getESlateMicroworld();
  }

  /**
   * Returns the name of the component.
   * @return    The component's name.
   */
  public String getComponentName()
  {
    return myName;
  }

  /**
   * Returns the full path name of the component in the component hierarchy.
   * @return    The requested name.
   */
  public String getComponentPathName()
  {
    if (isTopLevelComponent()) {
      return getComponentName();
    }else{
      return getParentHandle().getComponentPathName() +
             getParentNameServiceContext().getSeparator() +
             getComponentName();
    }
  }

  /**
   * Returns the path name of the component relative to a given component.
   * E.g., for component a.b.c.d, its name relative to component b is c.d.
   * The path name of a component relative to itself is ".".
   * @param     parent  The component relative to which the name will be
   *                    returned.
   * @return    The requested path name. If the <code>parent</code> is not an
   *            ancestor of this component, or the component itself, this
   *            method returns <code>null</code>.
   */
  public String getComponentPath(ESlateHandle parent)
  {
    if (equals(parent)) {
      return ".";
    }else{
      StringBuffer name = new StringBuffer(getComponentName());
   
      for (ESlateHandle h=getParentHandle(); h!=null; h=h.getParentHandle()) {
        if (h.equals(parent)) {
          return name.toString();
        }
        name.insert(0, h.childComponents.getSeparator());
        name.insert(0, h.getComponentName());
      }
      // If the loop exits, the provided handle was not an ancestor of the
      // component.
      return null;
    }
  }

//  /**
//   * Returns the names of the components that are children of this component.
//   * @return  The requested names.
//   */
//  public String[] getChildrenNames()
//  {
//    ESlateHandle[] h = toArray();
//    String[] names = new String[h.length];
//    for (int i=0; i<h.length; i++) {
//      names[i] = h[i].getComponentName();
//    }
//    return names;
//  }

//  /**
//   * Returns the full path names of the component's children in the component
//   * hierarchy.
//   * @return  The requested names.
//   */
//  public String[] getChildrenPathNames()
//  {
//    ESlateHandle[] h = toArray();
//    String[] names = new String[h.length];
//    for (int i=0; i<h.length; i++) {
//      names[i] = h[i].getComponentPathName();
//    }
//    return names;
//  }

  /**
   * Returns the E-Slate handle of a component whose name is given relative
   * to this component.
   * component.
   * @param     name    The name of the component. This name can be
   *                    either a simple name, e.g., "component", or a path
   *                    name relative to this component's hierarchy, e.g.,
   *                    "component.subcomponent.subsubcomponent", or "."
   *                    (i.e., the component itself).
   * @return    The requested name. If no child component having the
   *            specified name can be found, null is returned.
   */
  public ESlateHandle getHandle(String name)
  {
    // Note to implementors: This is the place to add support for stuff like
    // "..", "/", etc., should it be required.

    if ((name != null) && name.equals(".")) {
      return this;
    }else{
      return getChildHandle(name);
    }
  }

  /**
   * Returns the E-Slate handle of a component that is a child of this
   * component.
   * @param     name    The name of the child component. This name can be
   *                    either a simple name, e.g., "component", or a path
   *                    name relative to this component's hierarchy, e.g.,
   *                    "component.subcomponent.subsubcomponent".
   * @return    The requested name. If no child component having the
   *            specified name can be found, null is returned.
   */
  public ESlateHandle getChildHandle(String name)
  {
    // Note to implementors: If you need to add support for stuff like
    // "..", "/", etc., do so in method getHandle() instead of here.

    // First check if the specified name is reachable from this component,
    // i.e., the name refers either to a direct child of this component or is
    // a path name relative to this component leading to a component in this
    // component's hierarchy.
    ESlateHandle child;
    try {
      child = (ESlateHandle)(childComponents.lookup(name));
    } catch (NameServiceException e) {
      child = null;
    }
    if (child != null) {
      return child;
    }

    // If this fails, then check to see if the specified name refers to a
    // unique sub-tree of this component's hierarchy tree.
    ArrayList<ESlateHandle> components = new ArrayList<ESlateHandle>();
    searchNameSpaceForName(childComponents, name, components);
    if (components.size() == 1) {
      return components.get(0);
    }

    // Finally, check if the specified name was an alias. Either this will
    // succeed, in which case we return the E-Slate handle of the
    // corresponding component, or it will fail, indicating that
    // either there is no component having the specified name, or the
    // specified name refers to a sub-tree of the component hierarchy that
    // identifies more than one component.
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) {
      return myMW.getComponentHandleByAlias(name);
    }else{
      return null;
    }
  }

  /**
   * Search a name space recursively for components having a given name. This
   * name can be either a simple name, e.g., "component", or a path name
   * relative to this component's hierarchy, e.g.,
   * "component.subcomponent.subsubcomponent".
   * @param     context         The name space to search.
   * @param     name            The name for which to search.
   * @param     components      An ArrayList where components matching the
   *                            given name are placed.
   */
  private void searchNameSpaceForName(NameServiceContext context, String name,
                               ArrayList<ESlateHandle> components)
  {
    if (context == null) {
      return;
    }
    Object[] handles = context.listObjectsResolved();
    for (int i=0; i<handles.length; i++) {
      ESlateHandle h = (ESlateHandle)(handles[i]);
      NameServiceContext childContext = h.childComponents;
      if (childContext != null) {
        ESlateHandle comp;
        try {
          comp = (ESlateHandle)(childContext.lookup(name));
        } catch (NameServiceException e) {
          comp = null;
        }
        if (comp != null) {
          components.add(comp);
        }
        searchNameSpaceForName(childContext, name, components);
      }
    }
  }

  /**
   * Checks whether the component has another component as a child in any
   * depth in its hierarchy.
   * @param     child   The E-Slate handle of the compoennt to check.
   * @return    true    If <code>child</code> is anywhere in the component's
   *                    hierarchy, false otherwise.
   */
  public boolean hasChild(ESlateHandle child)
  {
    for (ESlateHandle h=child.getParentHandle(); h!=null; h=h.getParentHandle()) {
      if (h.equals(this)) {
        return true;
      }
    }
    return false;
  }

//  /**
//   * Returns the name of the component's parent.
//   * @return  The component's name. If this is a top-level component, null
//   *          is returned.
//   */
//  public String getParentName()
//  {
//    if (isTopLevelComponent()) {
//      return null;
//    }else{
//      return getParentHandle().getComponentName();
//    }
//  }

//  /**
//   * Returns the full path name of the component's parent in the component
//   * hierarchy.
//   * @return  The requested name. If this is a top-level component, null
//   *          is returned.
//   */
//  public String getParentPathName()
//  {
//    if (isTopLevelComponent()) {
//      return null;
//    }else{
//      return getParentHandle().getComponentPathName();
//    }
//  }

  /**
   * Returns a string representation of the component.
   * @return    The component's name.
   */
  public String toString()
  {
    String name = getComponentName();
    if (name != null) {
      return name;
    }else{
      if (isDisposed()) {
        return "<disposed handle>";
      }else{
        return "<handle with null name>";
      }
    }
  }

  /**
   * Returns the component that owns this handle.
   * @return    The component.
   */
  public Object getComponent()
  {
    return myComponent;
  }

  /**
   * Sets the component that owns this handle.
   * @param     owner   The component that owns this handle.
   */
  void setComponent(Object owner)
  {
    myComponent = owner;
  }

  /**
   * Sets the name of the component--for internal use only.
   * @param     name    The component's name.
   * @exception NameUsedException       This exception is thrown when trying
   *                    to rename a component using a name that is being used
   *                    by another component.
   * @exception RenamingForbiddenException      This exception is thrown
   *                    when renaming the component is not allowed.
   * @exception IllegalArgumentException        This exception is thrown
   *                    if the name contains the name service separator
   *                    character.
   */
  private void changeComponentName(String name)
    throws NameUsedException, RenamingForbiddenException,
    IllegalArgumentException
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if ((myMW != null) && !myMW.isRenamingAllowed()) {
      if (myComponent instanceof ESlateMicroworld) {
        throw new RenamingForbiddenException(
          resources.getString("renamingMWForbidden")
        );
      }else{
        throw new RenamingForbiddenException(
          resources.getString("renamingForbidden")
        );
      }
    }
    if (name.indexOf(SEPARATOR) != -1) {
      if (menuPanel != null) {
        nameField.setText(myName);
      }
      throw new IllegalArgumentException(
        resources.getString("cantContain1") +
        name +
        resources.getString("cantContain2") +
        SEPARATOR +
        resources.getString("cantContain3")
      );
    }

    ESlateHandle previous;
    NameServiceContext parentContext = getParentNameServiceContext();
    if (parentContext != null) {
      try {
        previous = (ESlateHandle)(parentContext.lookup(name));
      } catch (NameServiceException e) {
        previous = null;
      }
    }else{
      previous = null;
    }
    String oldName = myName;

    if (previous == null || previous.equals(this)) {
      myName = name;
      if (parentContext != null) {
        parentContext.renameNoException(oldName, name);
      }
    }else{
      if (menuPanel != null) {
        nameField.setText(myName);
      }
      /*papakiru*/
      /*throw new NameUsedException(
        resources.getString("nameUsed1") +
        name +
        resources.getString("nameUsed2")
      );*/
    }
    // Update the component's name in the plug view, plug menu, and plug
    // editor window of any parent components.
    for (ESlateHandle h=getParentHandle(); h != null; h = h.getParentHandle()) {
      h.redoPlugView = true;
      h.redoPlugMenu = true;
      h.reloadModels();
      if (myMW != null) {
        myMW.adjustPlugView(h);
      }
    }
    updateComponentName(oldName);
    if (myMW != null) {
      myMW.reregisterComponentsForScripting();
    }
  }

  /**
   * Updates the component's name in the menu bar and notifies any listeners
   * listening for component name changed events. This is part of the
   * functionality of changeComponentName(), detached so that it can be
   * invoked from other places as well (e.g., setESlateMicroworld()).
   * @param     oldName The previous name of the component.
   */
  private void updateComponentName(String oldName)
  {
    if (menuPanel != null) {
      nameField.setText(myName);
      nameLabel.setText(myName);
      nameLabel.setToolTipText(myName);
    }
    repaintTrees();
    makeVisible(null);
    // Don't bother sending a name changed event if we are renaming to the
    // same name. (But do do all the rest, as we may want the side effects,
    // such as updating the menu bar.)
    if (!oldName.equals(myName)) {
      componentNameChanged(oldName, myName);
    }
  }

  /**
   * Sets the name of the component.
   * <P>
   * This method will do nothing
   * while a component is being constructed from within the
   * <code>ESlateMicroworld.instantiateComponent()</code> method, which
   * invokes the component's constructor that takes an
   * <code>ObjectInput</code> as an argument, so that the component will
   * retain the name assigned to it by <code>instantiateComponent</code>.
   * @param     name    The component's name.
   * @exception NameUsedException       This exception is thrown when trying
   *                    to rename a component using a name that is being used
   *                    by another component.
   * @exception RenamingForbiddenException      This exception is thrown
   *                    when renaming the component is not allowed.
   * @exception IllegalArgumentException        This exception is thrown
   *                    when trying to rename the component using a null or
   *                    empty name or if the name contains the name service
   *                    separator character.
   */
  public void setComponentName(String name)
    throws NameUsedException, RenamingForbiddenException,
    IllegalArgumentException
  {
    if (inInstantiateComponent) {
      // If a component is being instantiated from within the
      // ESlateMicroworld.instantiateComponent() method, we don't want its
      // constructor to change its name to a, presumably, default name, as we
      // want the component to have the name given as an argument to the
      // instantiateComponent() method.
      return;
    }
    if (name == null || name.equals("")) {
      IllegalArgumentException iae =
        new IllegalArgumentException(resources.getString("nullName"));
      throw iae;
    }
    synchronized (microworldSync()) {
      changeComponentName(name);
      setNormalMenuBar();  // Causes the recalculation of the name label, so
                           // that the menu bar will respond correctly when the
                           // user clicks anywhere on the new name.
    }
  }

  /**
   * Sets the name of the component. If the name given as an argument is
   * already used by another component, a "_" and a unique number will be
   * appended to the name so that renaming the component is guaranteed to
   * succeed. If the name is already in that format, the "_" and the following
   * number are trimmed off before creating the name of the component.
   * <P>
   * This method will do nothing
   * while a component is being constructed from within the
   * <code>ESlateMicroworld.instantiateComponent()</code> method, which
   * invokes the component's constructor that takes an
   * <code>ObjectInput</code> as an argument, so that the component will
   * retain the name assigned to it by <code>instantiateComponent</code>.
   * @exception RenamingForbiddenException      This exception is thrown
   *                    when renaming the component is not allowed.
   * @exception IllegalArgumentException        This exception is thrown
   *                    when trying to rename the component using a null or
   *                    empty name or if the name contains the name service
   *                    separator character.
   */
  public void setUniqueComponentName(String suggestedName)
    throws RenamingForbiddenException, IllegalArgumentException
  {
    if (inInstantiateComponent) {
      // If a component is being instantiated from within the
      // ESlateMicroworld.instantiateComponent() method, we don't want its
      // constructor to change its name to a, presumably, default name, as we
      // want the component to have the name given as an argument to the
      // instantiateComponent() method.
      return;
    }
    if (suggestedName == null || suggestedName.equals("")) {
      IllegalArgumentException iae =
        new IllegalArgumentException(resources.getString("nullName"));
      throw iae;
    }
    synchronized (microworldSync()) {
      if (!ESlateStrings.areEqualIgnoreCase(suggestedName,myName,getLocale())) {
        NameServiceContext parentContext = getParentNameServiceContext();
        String newName;
        if (parentContext != null) {
          newName = parentContext.constructUniqueName(suggestedName);
        }else{
          newName = suggestedName;
        }
        try {
          changeComponentName(newName);
        } catch (NameUsedException e) {
        }
      }else{
        if (!suggestedName.equals(myName)) { // We are only changing the case
          try {
            changeComponentName(suggestedName);
          } catch (NameUsedException e) {
          }
        }
      }
      setNormalMenuBar();  // Causes the recalculation of the name label, so
                           // that the menu bar will respond correctly when
                           // the user clicks anywhere on the new name.
    }
  }

  /**
   * Returns an object suitable for synchronizing code on the component's
   * microworld.
   * @return    Returns the component's microworld, if not null, and a dummy
   *            object otherwise.
   */
  private Object microworldSync()
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) {
      return myMW;
    }else{
      return dummyObject;
    }
  }

  /**
   * Sets an alias for the component.
   * @param     name    The component's alias. If <code>name</code>,
   *                    is null or "", the alias is cancelled. The provided
   *                    alias overrides any previously defined alias. When
   *                    transferring a component to a new microworld, an
   *                    attempt to register this alias with that microworld
   *                    will be made, but if the alias is already in use, the
   *                    attempt will fail.
   * @exception NameUsedException       This exception is thrown when trying
   *                    assign to component an alias that is being used
   *                    by another component.
   */
  public void setAlias(String name) throws NameUsedException
  {
    synchronized (microworldSync()) {
      ESlateMicroworld myMW = getESlateMicroworld();
      if (name == null || name.equals("")) {
        if (myAlias != null) {
          if (myMW != null) {
            myMW.removeAlias(this);
          }
          myAlias = null;
        } 
        return;
      }

      if (myMW != null) {
        NameServiceContext aliases = myMW.aliases;
        ESlateHandle previous;
        try {
          previous = (ESlateHandle)(aliases.lookup(name));
        } catch (NameServiceException e) {
          previous = null;
        }
        if (previous == null) {
          try {
            if (myAlias == null) {
              aliases.bind(name, this);
            }else{
              aliases.rename(myAlias, name);
            }
          } catch (NameServiceException e) {
            throw new NameUsedException(e.getMessage());
          }
          myAlias = name;
        }
      }else{
        myAlias = name;
      }
    }
  }

  /**
   * Returns the alias associated with this component.
   * @return    The requested alias, if there is one, or null.
   */
  public String getAlias()
  {
    return myAlias;
  }

  /**
   * Sets an alias for the component. If the name given as an argument is
   * already used by another component, a "_" and a unique number will be
   * appended to the name so that associating an alias with the component
   * is guaranteed to succeed.
   */
  public void setUniqueComponentAlias(String suggestedName)
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) {
      synchronized (myMW) {
        NameServiceContext aliases = myMW.aliases;
        if (!ESlateStrings.areEqualIgnoreCase(suggestedName,myAlias,getLocale())){
          try {
            setAlias(aliases.constructUniqueName(suggestedName));
          } catch (NameUsedException e) {
          }
        }
      }
    }else{
      try {
        setAlias(suggestedName);
      } catch (NameUsedException e) {
      }
    }
  }

  /**
   * Add a listener for E-Slate events.
   * @param     listener        The listener to add.
   */
  public void addESlateListener(ESlateListener listener)
  {
    synchronized (eSlateListeners) {
      if (!eSlateListeners.contains(listener)) {
        eSlateListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener for E-Slate events.
   * @param     listener        The listener to remove.
   */
  public void removeESlateListener(ESlateListener listener)
  {
    if (eSlateListeners != null) {
      synchronized (eSlateListeners) {
        int i = eSlateListeners.indexOf(listener);
        if (i >= 0) {
          eSlateListeners.remove(i);
        }
      }
    }
  }

  /**
   * Invoked when the name of the component is changed, to fire all registered
   * name change event listeners.
   * @param     oldName The old name of the component.
   * @param     newName The new name of the component.
   */
  private void componentNameChanged(String oldName, String newName)
  {
    ESlateListenerBaseArray listeners;
    if (eSlateListeners.size() > 0) {
      synchronized(eSlateListeners) {
        listeners = (ESlateListenerBaseArray)(eSlateListeners.clone());
      }
      int nListeners = listeners.size();
      for (int i=0; i<nListeners; i++) {
        ESlateListener l = listeners.get(i);
        ComponentNameChangedEvent e =
          new ComponentNameChangedEvent(this, oldName, newName);
        l.componentNameChanged(e);
      }
    }
  }

  /**
   * Fires all parent changed event listeners.
   * @param     oldParent       The old parent of the component.
   * @param     newParent       The new parent of the component.
   */
  private void parentChanged(ESlateHandle oldParent, ESlateHandle newParent)
  {
    if (eSlateListeners.size() > 0) {
      ESlateListenerBaseArray listeners;
      synchronized(eSlateListeners) {
        listeners = (ESlateListenerBaseArray)(eSlateListeners.clone());
      }
      int nListeners = listeners.size();
      for (int i=0; i<nListeners; i++) {
        ESlateListener l = listeners.get(i);
        ParentChangedEvent e =
          new ParentChangedEvent(this, oldParent, newParent);
        l.parentChanged(e);
      }
    }
  }

  /**
   * Add a listener for microworld change events.
   * @param     listener        The listener to add.
   * @deprecated        As of E-Slate version 1.6.17, replaced by
   * {@link #addMicroworldChangedListener(gr.cti.eslate.base.MicroworldChangedListener)}
   */
  public void addMicroworldChangeListener(MicroworldChangedListener listener)
  {
    addMicroworldChangedListener(listener);
  }

  /**
   * Remove a listener for microworld change events.
   * @param     listener        The listener to remove.
   * @deprecated        As of E-Slate version 1.6.17, replaced by
   * {@link #removeMicroworldChangedListener(gr.cti.eslate.base.MicroworldChangedListener)}
   */
  public void removeMicroworldChangeListener(MicroworldChangedListener listener)
  {
    removeMicroworldChangedListener(listener);
  }

  /**
   * Add a listener for microworld change events.
   * @param     listener        The listener to add.
   */
  public void addMicroworldChangedListener(MicroworldChangedListener listener)
  {
    synchronized (microworldChangeListeners) {
      if (!microworldChangeListeners.contains(listener)) {
        microworldChangeListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener for microworld change events.
   * @param     listener        The listener to remove.
   */
  public void removeMicroworldChangedListener(
    MicroworldChangedListener listener)
  {
    synchronized (microworldChangeListeners) {
      int i = microworldChangeListeners.indexOf(listener);
      if (i >= 0) {
        microworldChangeListeners.remove(i);
      }
    }
  }

  /**
   * Invoked when the microworld to which the component belongs is changed, to
   * fire all registered microworld change event listeners.
   * @param     oldMicroworld   The old microworld of the component.
   * @param     newMicroworld   The new microworld of the component.
   */
  private void microworldChanged(ESlateMicroworld oldMicroworld,
                                 ESlateMicroworld newMicroworld)
  {
    if (microworldChangeListeners.size() > 0) {
      MicroworldChangedListenerBaseArray listeners;
      synchronized(microworldChangeListeners) {
        listeners =
          (MicroworldChangedListenerBaseArray)
            (microworldChangeListeners.clone());
      }
      int nListeners = listeners.size();
      for (int i=0; i<nListeners; i++) {
        MicroworldChangedListener l = listeners.get(i);
        MicroworldChangedEvent e =
          new MicroworldChangedEvent(this.getComponent(), oldMicroworld,
                                     newMicroworld);
        l.microworldChanged(e);
      }
    }
  }

  /**
   * Returns the locale under which the component is running.
   * Currently, this is a reference to the default locale.
   * @return    A reference to the locale.
   */
  public Locale getLocale()
  {
    return ESlateMicroworld.getCurrentLocale();
  }

  /**
   * Specifies the information about the avakeeo component associated with
   * this handle, that will be displayed in the component's "about" dialog box.
   * @param     info    The specified information.
   */
  public void setInfo(ESlateInfo info)
  {
    componentInfo = info;
  }

  /**
   * Returns the information about the component associated with this handle.
   * @return    The requested information.
   */
  private ESlateInfo getInfo()
  {
    return componentInfo;
  }

  /**
   * Presents the credits/copyright information dialog.
   */
  public void showInfoDialog()
  {
    ESlateInfo ai = getInfo();
    String message[];
    if (ai != null && ai.componentInfo != null) {
      int infoLength = ai.componentInfo.length;
      message = new String[1+infoLength];
      message[0] = ai.componentName;
      for (int i=0; i<infoLength; i++) {
        message[1+i] = ai.componentInfo[i];
      }
    }else{
      message = new String[1];
      message[0] = resources.getString("noInfo");
    }
    Image im;

    try {
      String cName = myComponent.getClass().getName() + "BeanInfo";
      BeanInfo bi = (BeanInfo)(Class.forName(cName).newInstance());
      im = bi.getIcon(BeanInfo.ICON_COLOR_32x32);
    } catch (Exception e) {
      im = null;
    }
    if (im == null) {
      im = getImage();
      if (im != null) {
        im = im.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
      }
    }
    if (im == null) {
      im = ESlate.getIconImage32();
    }
    Icon icon = new ImageIcon(im);
    ESlateOptionPane.showMessageDialog(
      getDialogParent(infoButton), message, resources.getString("about"),
      JOptionPane.INFORMATION_MESSAGE, icon);
  }

 /**
   * Presents help to the user.
   */
  public void showHelpWindow()
  {
    try {
      if (helpViewer == null) {
        try {
          HelpSet hs = HelpSetLoader.load(myComponent.getClass());
          int width = 640;
          int height = 400;
          Point origin = centerBoxOrigin(width, height);
          helpViewer = ESlateHelpSystemViewer.createViewer(
            hs, origin.x, origin.y, width, height
          );
          helpViewer.setVisible(true);
        } catch (Exception e) {
          ESlateOptionPane.showMessageDialog(
            getDialogParent(helpButton), e.getMessage(),
            resources.getString("error"), JOptionPane.ERROR_MESSAGE
          );
        }
      }else{
        if (!helpViewer.isVisible()) {
          helpViewer.setVisible(true);
        }
        helpViewer.setState(Frame.NORMAL);
        helpViewer.toFront();
      }
    } catch (NoClassDefFoundError e) {
      // E-Slate help classes are not in class path
        ESlateOptionPane.showMessageDialog(
          getDialogParent(helpButton), resources.getString("noHelpSystem"),
          resources.getString("error"), JOptionPane.ERROR_MESSAGE
        );
    }
  }

  /**
   * Returns the coordinates where the top left corner of a box should be
   * placed, so that it appears centered, relative to either the E-Slate
   * container, if the component belongs to a microworld, or to the screen, if
   * it does not.
   * @param     width   The width of the box.
   * @param     height  The height of the box.
   * @return    The requested coordinates.
   */
  private Point centerBoxOrigin(int width, int height)
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) {
      return myMW.centerBoxOrigin(width, height);
    }else{
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int screenWidth = screenSize.width;
      int screenHeight = screenSize.height;
      int x = (screenWidth - width) / 2;
      int y = (screenHeight - height) / 2;
      return new Point(x, y);
    }
  }

  /**
   * Returns a component that can be used as the parent component
   * of a dialog.
   * @param     comp    The component that should ideally be used as the
   *                    parent.
   * @return    If the E-Slate component is a visible component, then
   *            <code>comp</code> is returned. Otherwise, a reference to the
   *            container is returned.
   */
  private Component getDialogParent(Component comp)
  {
    Component c;
    // If the E-Slate component is an invisible component, then we cannot
    // make the help button the parent component of a message dialog; use
    // the container, instead.
    if ((comp == null) || (SwingUtilities.getRoot(comp) == null)) {
      ESlateMicroworld mw = getESlateMicroworld();
      if (mw != null) {
        c = mw.getMicroworldEnvironmentAsComponent();
      }else{
        c = null;
      }
    }else{
      c = comp;
    }
    return c;
  }

  /**
   * Returns a component that can be used as the parent component of a dialog.
   * @return    If the component's menu panel has been created and is visible,
   *            then the menu panel is returned. Otherwise, a reference to the
   *            container is returned.
   */
  Component getWarningComponent()
  {
    Object o = getComponent();
    Component c;
    if (o instanceof Component) {
      c = (Component)o;
    }else{
      c = null;
    }
    return getDialogParent(c);
  }

  /**
   * Adds a reference to a PlugTree component displaying the plug tree view
   * of the associated E-Slate component, so that E-Slate may update it
   * whenever necessary.
   * @param     tree    The tree to which to add a reference.
   */
  void addTree(PlugTree tree)
  {
    synchronized (treeList) {
      treeList.add(tree);
    }
  }

  /**
   * Removes a reference to a PlugTree component displaying the plug tree view
   * of the associated E-Slate component.
   * @param     tree    The tree to which to remove a reference.
   */
  void removeTree(PlugTree tree)
  {
    if (treeList != null) {
      synchronized (treeList) {
        treeList.remove(treeList.indexOf(tree));
      }
    }
  }

  /**
   * Removes the references to all the PlugTree components displaying the plug
   * tree view of the associated E-Slate component.
   */
  void clearTreeList()
  {
    synchronized (treeList) {
      treeList.clear();
    }
  }

  /**
   * Reloads the models of the PlugTree components displaying the plug tree
   * view of the associated E-Slate component.
   */
  void reloadModels()
  {
    synchronized (treeList) {
      int nTrees = treeList.size();
      for (int i=0; i<nTrees; i++) {
        PlugTree tree = treeList.get(i);

        // Reloading the model does not preserve the expanded
        // state of the tree nodes, so we have to keep track of it ourselves.
        PlugTreeNode root = (PlugTreeNode)(tree.getModel().getRoot());
        TreePath rootPath = new TreePath(root.getPath());
        Enumeration<TreePath> e = tree.getExpandedDescendants(rootPath);
        ArrayList<TreePath> a = new ArrayList<TreePath>();
        if (e != null) {
          while (e.hasMoreElements()) {
            a.add(e.nextElement());
          }
        }

        // Reload the tree model.
        ((DefaultTreeModel)(tree.getModel())).reload();

        // Restore the expanded state of the tree nodes.
        int n = a.size();
        for (int j=0; j<n; j++) {
          tree.expandPath(a.get(j));
        }
      }
    }
  }

  /**
   * Repaints the PlugTree components displaying the plug tree view of the
   * associated E-Slate component.
   */
  void repaintTrees()
  {
    synchronized (treeList) {
      int nTrees = treeList.size();
      for (int i=0; i<nTrees; i++) {
        PlugTree tree = treeList.get(i);
        TreePath[] selections = tree.getSelectionPaths();
        tree.clearSelection();
        tree.repaint();
        if (selections != null) {
          tree.setSelectionPaths(selections);
        }
      }
    }
  }

  /**
   * Ensures that the plug associated with a given node is visible in all
   * PlugTree components displaying the plug tree view of the associated
   * E-Slate component.
   * @param     node    The node to make visible. If node is null, then the
   *                    root of the tree will be made visible;
   */
  void makeVisible(PlugTreeNode node)
  {
    synchronized (treeList) {
      if (treeList.isEmpty()) {
        ESlateHandle h = getParentHandle();
        if (h != null) {
          h.makeVisible(node);
        }
      }else{
        int nTrees = treeList.size();
        for (int i=0; i<nTrees; i++) {
          PlugTree tree = treeList.get(i);
          TreePath tp;
          if (node == null) {
            tp = tree.getPath(tree.getRoot());
          }else{
            tp = tree.getPath(node);
          }
          tree.makeVisible(tp);
          tree.scrollPathToVisible(tp);
        }
      }
    }
  }

  /**
   * Specifies whether debugging messages should be printed.
   * @param     status  True if yes, false if not.
   */
  public void setDebugStatus(boolean status)
  {
    debug = status;
  }

  /**
   * Returns whether debugging messages are printed.
   * @return    True if yes, false if not.
   */
  public boolean getDebugStatus()
  {
    return debug;
  }

  /**
   * Sets the "applet started" flag.
   */
  void start()
  {
    synchronized (synchronizeStart) {
      started = true;
    }
  }

  /**
   * Clears the "applet started" flag.
   */
  void stop()
  {
    synchronized (synchronizeStart) {
      started = false;
    }
  }

  /**
   * Add a Logo primitive group to the component.
   * @param     group   The class name of the primitive group to add.
   */
  public void addPrimitiveGroup(String group)
  {
    addPrimitiveGroup(group, false);
  }

  /**
   * Add a Logo primitive group to the component.
   * @param     group   The class name of the primitive group to add.
   * @param     forceNotification   If true, notification to attached
   *                    primitive group listeners will be sent, even if
   *                    the primitive group has already been registered.
   */
  private void addPrimitiveGroup(String group, boolean forceNotification)
  {
    synchronized(primitiveGroups) {
      ESlateMicroworld myMW = getESlateMicroworld();
      if (!primitiveGroups.contains(group)) {
        primitiveGroups.add(group);
      }
      if (forceNotification || !primitiveGroups.contains(group)) {
        if (myMW != null) {
          PrimitiveGroupAddedEvent e =
            new PrimitiveGroupAddedEvent(this, group);
          myMW.firePrimitiveGroupAddedListeners(e);
        }
      }
    }
  }

  /**
   * Adds all the component's primitives groups, forcing notification of the
   * addition to all attached listeners, then recirsively invokes this method
   * on all the component's children.
   */
  private void addPrimitiveGroups()
  {
    String[] groups = getSupportedPrimitiveGroups();
    for (int i=0; i<groups.length; i++) {
      addPrimitiveGroup(groups[i], true);
    }
    ESlateHandle[] children = getChildHandles();
    for (int i=0; i<children.length; i++) {
      children[i].addPrimitiveGroups();
    }
  }

  /**
   * Add a set of Logo primitive groups to the component.
   * @param     groups  An array containing the class names of the primitive
   * groups to add.
   */
  public void addPrimitiveGroup(String[] groups)
  {
    synchronized (primitiveGroups) {
      ESlateMicroworld myMW = getESlateMicroworld();
      int nGroups = 0;
      // Count primitive groups that are not already supported.
      for (int i=0; i<groups.length; i++) {
        if (!primitiveGroups.contains(groups[i])) {
          nGroups++;
        }
      }
      // Add primitive groups that are not already supported.
      if (nGroups > 0 ) {
        String addedGroups[] = new String[nGroups];
        for (int i=0, j=0; i<groups.length; i++) {
          if (!primitiveGroups.contains(groups[i])) {
            primitiveGroups.add(groups[i]);
            addedGroups[j++] = groups[i];
          }
        }
        if (myMW != null) {
          PrimitiveGroupAddedEvent e =
            new PrimitiveGroupAddedEvent(this, addedGroups);
          myMW.firePrimitiveGroupAddedListeners(e);
        }
      }
    }
  }

  /**
   * Returns a list of the primitive groups supported by the component.
   * @return    The requested list.
   */
  public String[] getSupportedPrimitiveGroups()
  {
    synchronized (primitiveGroups) {
      String compoGroups[] = null;
      int compoGroupSize;
      int ownGroupSize;
      int size;
      try {
        if (myComponent instanceof LogoScriptable) {
          compoGroups =
            ((LogoScriptable)(myComponent)).getSupportedPrimitiveGroups();
        }
      } catch (Throwable th) {
        System.err.println("***BEGIN STACK TRACE");
        th.printStackTrace();
        System.err.println("***END STACK TRACE");
      }
      ownGroupSize = primitiveGroups.size();
      if (compoGroups != null) {
        compoGroupSize = compoGroups.length;
      }else{
        compoGroupSize = 0;
      }
      size = compoGroupSize + ownGroupSize;
      String[] groups = new String[size];

      for (int i=0; i<compoGroupSize; i++) {
        groups[i] = compoGroups[i];
      }
      for (int i=0; i<ownGroupSize; i++) {
        groups[i+compoGroupSize] = primitiveGroups.get(i);
      }
      return groups;
    }
  }

  /**
   * Ensure that the microworld file sub-folder where the component can store
   * its private data exists, and that the subfolders in the path stored in
   * the <code>currentDir</code> variable actually exist.
   * @return    True if the operation was successfull, false otherwise.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  private boolean ensureDirExists() throws IOException
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW == null) {
      return false;
    }
    StructFile sf = myMW.getMicroworldFile();
    if (sf == null) {
      throw new IOException(resources.getString("noMicroworldFile"));
    }
    Entry dir = null;
    synchronized (sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      if (currentDir == null) {
        currentDir = new ArrayList();
      }
      try {
        sf.changeToRootDir();
        try {
          dir = sf.findEntry(ESlateMicroworld.COMPONENT_DATA);
        } catch (IOException ioe) {
          dir = sf.createDir(ESlateMicroworld.COMPONENT_DATA);
        }
        sf.changeDir(dir);
        String name = handleId.toString();
        try {
          dir = sf.findEntry(name);
        } catch (IOException ioe) {
          dir = sf.createDir(name);
        }
        sf.changeDir(dir);
        int size = currentDir.size();
        for (int i=0; i<size; i++) {
          sf.changeDir((String)(currentDir.get(i)));
        }
      } catch (IOException ioe2) {
        sf.changeDir(oldDir);
        throw ioe2;
      }
      sf.changeDir(oldDir);
      return true;
    }
  }

  /**
   * Check if the microworld file sub-folder where the component can store
   * its private data exists.
   * @return    True if the the sub-folder exists, false otherwise.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  private boolean rootDirExists() throws IOException
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW == null) {
      return false;
    }
    StructFile sf = myMW.getMicroworldFile();
    if (sf == null) {
      return false;
    }
    synchronized (sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      try {
        sf.changeToRootDir();
        Entry dir = sf.findEntry(ESlateMicroworld.COMPONENT_DATA);
        sf.changeDir(dir);
        String name = handleId.toString();
        dir = sf.findEntry(name);
      } catch (IOException ioe2) {
        sf.changeDir(oldDir);
        return false;
      }
      sf.changeDir(oldDir);
      return true;
    }
  }

  /**
   * Changes the current directory of the microworld file to the sub-folder
   * pointed to by <code>currentDir</code>.
   * @param     sf      The microworld file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  private void changeToCurrentDir(StructFile sf) throws IOException
  {
    changeToRootDir(sf);
    int size = currentDir.size();
    for (int i=0; i<size; i++) {
      sf.changeDir((String)(currentDir.get(i)));
    }
  }

  /**
   * Changes the current directory of the microworld file to the root
   * sub-folder of the component.
   * @param     sf      The microworld file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  private void changeToRootDir(StructFile sf) throws IOException
  {
    sf.changeToRootDir();
    sf.changeDir(ESlateMicroworld.COMPONENT_DATA);
    sf.changeDir(handleId.toString());
  }

  /**
   * Changes the current directory in the microworld file sub-folder where
   * the component can store its private data.
   * @param     dir     The name of the directory to which to change, relative
   *                    to the current directory.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   * @exception IllegalArgumentException        Not actually thrown.
   */
  @SuppressWarnings(value={"unchecked"})
  public void changeDir(String dir)
    throws IOException, IllegalArgumentException
  {
    Vector path = new Vector(1);
    path.addElement(dir);
    changeDir(path, StructFile.RELATIVE_PATH);
  }

  /**
   * Changes the current directory in the microworld file sub-folder where
   * the component can store its private data.
   * @param     path    A list of successive directory names to which to
   *                    change.
   * @param     isRelative      If true, then <code>path</code> is relative
   *                            to the current directory. If false, then
   *                            <code>path</code> is relative to the root
   *                            sub-folder.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   * @exception IllegalArgumentException        Not actually thrown.
   */
  @SuppressWarnings(value={"unchecked"})
  public void changeDir(Vector path, boolean isRelative) throws IOException
  {
    if (!ensureDirExists()) {
      throw new IOException("noMicroworldFile");
    }
    StructFile sf = getESlateMicroworld().getMicroworldFile();
    int size = path.size();
    synchronized(sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      if (isRelative) {
        changeToCurrentDir(sf);
      }else{
        changeToRootDir(sf);
      }
      try {
        for (int i=0; i<size; i++) {
          sf.changeDir((String)(path.elementAt(i)));
        }
      }catch (IOException ioe) {
        sf.changeDir(oldDir);
        throw ioe;
      }
      sf.changeDir(oldDir);
    }
    if (!isRelative) {
      currentDir.clear();
    }
    for (int i=0; i<size; i++) {
      currentDir.add(path.elementAt(i));
    }
  }

  /**
   * Changes the current directory in the microworld file sub-folder where
   * the component can store its private data to the root sub-folder.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public void changeToRootDir() throws IOException
  {
    Vector path = new Vector(0);
    changeDir(path, StructFile.ABSOLUTE_PATH);
  }

  /**
   * Changes the current directory in the microworld file sub-folder where
   * the component can store its private data to the root of that sub-folder.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  @SuppressWarnings(value={"unchecked"})
  public void changeToParentDir() throws IOException
  {
    int size = currentDir.size() - 1;
    if (size < 0) {
      throw new IOException(resources.getString("rootDir"));
    }
    Vector path = new Vector(size);
    for (int i=0; i<size; i++) {
      path.addElement(currentDir.get(i));
    }
    changeDir(path, StructFile.ABSOLUTE_PATH);
  }

  /**
   * Opens for input a sub-file in the current directory of the sub-folder
   * where the component can store its private data.
   * @param     name    The name of the file.
   * @return    An input stream associated with the file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public InputStream openInputFile(String name) throws IOException
  {
    if (!ensureDirExists()) {
      throw new IOException("noMicroworldFile");
    }
    StructFile sf = getESlateMicroworld().getMicroworldFile();
    Entry file = null;
    synchronized (sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      changeToCurrentDir(sf);
      try {
        file = sf.findEntry(name);
      } catch (IOException ioe) {
        sf.changeDir(oldDir);
        throw ioe;
      }
      sf.changeDir(oldDir);
    }
    ESlateStructInputStream si = new ESlateStructInputStream(sf, file);
    openStreams.add(si);
    return si;
  }

  /**
   * Opens for output a sub-file in the current directory of the sub-folder
   * where the component can store its private data.
   * @param     name    The name of the file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public OutputStream openOutputFile(String name) throws IOException
  {
    if (!ensureDirExists()) {
      throw new IOException("noMicroworldFile");
    }
    StructFile sf = getESlateMicroworld().getMicroworldFile();
    Entry file = null;
    synchronized (sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      changeToCurrentDir(sf);
      try {
        file = sf.createFile(name);
      } catch (IOException ioe) {
        sf.changeDir(oldDir);
        throw ioe;
      }
      sf.changeDir(oldDir);
    }
    ESlateStructOutputStream so = new ESlateStructOutputStream(sf, file);
    openStreams.add(so);
    return so;
  }

  /**
   * Deletes a sub-file in the current directory of the sub-folder
   * where the component can store its private data.
   * @param     name    The name of the file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   * @exception IllegalArgumentException        Not actually thrown.
   */
  public void deleteFile(String name)
    throws IOException, IllegalArgumentException
  {
    if (!ensureDirExists()) {
      throw new IOException("noMicroworldFile");
    }
    StructFile sf = getESlateMicroworld().getMicroworldFile();
    synchronized (sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      changeToCurrentDir(sf);
      try {
        sf.deleteEntry(name);
      } catch (IOException ioe) {
        sf.changeDir(oldDir);
        throw ioe;
      }
      sf.changeDir(oldDir);
    }
  }

  /**
   * Renames a sub-file or sub-directory in the current directory of the
   * sub-folder where the component can store its private data.
   * @param     oldName         The old name of the file.
   * @param     newName         The new name of the file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   * @exception IllegalArgumentException        Not actually thrown.
   */
  public void renameFile(String oldName, String newName)
    throws IOException, IllegalArgumentException
  {
    if (!ensureDirExists()) {
      throw new IOException("noMicroworldFile");
    }
    StructFile sf = getESlateMicroworld().getMicroworldFile();
    synchronized (sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      changeToCurrentDir(sf);
      try {
        sf.renameEntry(oldName, newName);
      } catch (IOException ioe) {
        sf.changeDir(oldDir);
        throw ioe;
      }
      sf.changeDir(oldDir);
    }
  }

  /**
   * Creates a sub-folder in the current directory of the sub-folder
   * where the component can store its private data.
   * @param     name    The name of the sub-folder.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public void createDirectory(String name) throws IOException
  {
    if (!ensureDirExists()) {
      throw new IOException("noMicroworldFile");
    }
    StructFile sf = getESlateMicroworld().getMicroworldFile();
    synchronized (sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      changeToCurrentDir(sf);
      try {
        sf.createDir(name);
      } catch (IOException ioe) {
        sf.changeDir(oldDir);
        throw ioe;
      }
      sf.changeDir(oldDir);
    }
  }

  /**
   * Check whether there is a sub-file or sub-directory with a given name in
   * the current directory of the sub-folder where the component can store its
   * private data.
   * @param     name    The name to check.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   * @return    True if yes, false if no.
   */
  public boolean fileExists(String name) throws IOException
  {
    if (!ensureDirExists()) {
      throw new IOException("noMicroworldFile");
    }
    StructFile sf = getESlateMicroworld().getMicroworldFile();
    synchronized (sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      changeToCurrentDir(sf);
      boolean result = false;;
      try {
        result = sf.fileExists(name);
      } catch (IOException ioe) {
        sf.changeDir(oldDir);
        throw ioe;
      }
      sf.changeDir(oldDir);
      return result;
    }
  }

  /**
   * Check whether an entry in the current directory of the sub-folder where
   * the component can store its private data is a plain file.
   * @param     name    The name to check.
   * @return    True if the name corresponds to a plain file, false if the
   *            name corresponds to a directory.
   * @exception IOException     Thrown if the requested entry cannot be
   *                            found.
   * @exception IllegalArgumentException        Thrown if the specified entry
   *                            name is null or empty.
   */
  public boolean isPlainFile(String name)
    throws IOException, IllegalArgumentException
  {
    if (!ensureDirExists()) {
      throw new IOException("noMicroworldFile");
    }
    StructFile sf = getESlateMicroworld().getMicroworldFile();
    synchronized (sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      changeToCurrentDir(sf);
      boolean result = false;
      try {
        result = sf.isPlainFile(name);
      } catch (IOException ioe) {
        sf.changeDir(oldDir);
        throw ioe;
      } catch (IllegalArgumentException iae) {
        sf.changeDir(oldDir);
        throw iae;
      }
      sf.changeDir(oldDir);
      return result;
    }
  }

  /**
   * Return a list of the names of the entries in the current directory.
   * @return    An array containing the requested names.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public String[] listFiles() throws IOException
  {
    if (!ensureDirExists()) {
      throw new IOException("noMicroworldFile");
    }
    StructFile sf = getESlateMicroworld().getMicroworldFile();
    synchronized (sf) {
      Entry oldDir = sf.getCurrentDirEntry();
      changeToCurrentDir(sf);
      String[] result = null;
      try {
        result = sf.listFiles();
      } catch (IOException ioe) {
        sf.changeDir(oldDir);
        throw ioe;
      }
      sf.changeDir(oldDir);
      return result;
    }
  }

  /**
   * Flush the cache, if there is one, associated with the microworld file.
   */
  public void flushCache()
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) {
      StructFile sf = myMW.getMicroworldFile();
      if (sf != null) {
        sf.flushCache();
      }
    }
  }

  /**
   * Returns the component's icon.
   * @return    An icon desribed in the component's BeanInfo class or null
   *            if no such icon is available.
   */
  public Image getImage()
  {
    if (!haveImage) {
      Image im;
      try {
        String cName = myComponent.getClass().getName() + "BeanInfo";
        BeanInfo bi = (BeanInfo)(Class.forName(cName).newInstance());
        im = bi.getIcon(BeanInfo.ICON_COLOR_16x16);
        if (im == null) {
          im = bi.getIcon(BeanInfo.ICON_MONO_16x16);
        }
      }catch (Exception e) {
        im = null;
      }
      haveImage = true;
      componentImage = im;
    }
    return componentImage;
  }

  /**
   * Returns the bean context encapsulating the components that are children
   * of this component.
   * @return    The requested bean context.
   */
  BeanContextServicesSupport getBeanContext()
  {
    return beanContext;
  }

  /**
   * Removes a component from its parent's name space.
   * @param     handle  The E-Slate handle of the component.
   */
  private void removeFromParentNameSpace(ESlateHandle handle)
  {
    // Remove component from its parent component's name space.
    ESlateHandle parentHandle = handle.getParentHandle();
    if (parentHandle != null) {
      parentHandle.removeName(handle);
      getRootComponentHandle().redoPlugView = true;
      getRootComponentHandle().redoPlugMenu = true;
    }
  }

  /**
   * Add a component to this component's list of child components.
   * @param     handle  The E-Slate handle of the component to add to the
   *                    list.
   * @return    True if the component was actually added to the list,
   *            false otherwise (i.e., if it was already in the list).
   */
  public boolean add(ESlateHandle handle)
  {
    return add(handle, true);
  }
  
  /**
   * Add a component to this component's list of child components.
   * @param     handle  The E-Slate handle of the component to add to the
   *                    list.
   * @param     sendNotification        Specifies whether notification will
   *                    be sent to the registered ESlateListeners.
   * @return    True if the component was actually added to the list,
   *            false otherwise (i.e., if it was already in the list).
   */
  private boolean add(ESlateHandle handle, boolean sendNotification)
  {
    if (contains(handle)) {
      return false;
    }
    if (equals(handle)) {
      return false;
    }
    boolean added;
    ESlateMicroworld mw = handle.getESlateMicroworld();
    ESlateMicroworld currentMW = getESlateMicroworld(this);

    ESlateHandle parentHandle = handle.getParentHandle();

    if (parentHandle != null) {
      parentHandle.remove(handle, true, false);
    }

    // Become a parent of the component.
    added = beanContext.add(handle.getBeanContext());
    if (added) {
      addName(handle);
      getRootComponentHandle().redoPlugView = true;
      getRootComponentHandle().redoPlugMenu = true;
      addToTree(handle);
      // Addition implies hosting. This will also set the microworld of the
      // hosted component to that of the host.
      host(handle);
    }else{
      // If we failed to adopt a component, put it back where it used to be.
      if (parentHandle != null) {
        parentHandle.add(handle);
        if (parentHandle.myComponent instanceof ESlateMicroworld) {
          // Do any additional actions required when a component is added to a
          // microworld.
          ESlateMicroworld parentMW = getESlateMicroworld(parentHandle);
          if (parentMW != null) {
            parentMW.addComponent(handle);
          }
        }
      }
    }
    if (mw != null) {
      mw.reregisterComponentsForScripting();
    }
    if (added) {
      if (myComponent instanceof ESlateMicroworld) {
        // Do any additional actions required when a component is added to a
        // microworld.
        if (currentMW != null) {
          currentMW.addComponent(handle);
        }
      }else{
        if ((currentMW != null) && (handle.handleId == null)) {
          currentMW.assignHandleID(handle);
        }
      }
      if (currentMW != null) {
        currentMW.reregisterComponentsForScripting();
      }
      addPrimitiveGroups();
      if (sendNotification) {
        handle.parentChanged(parentHandle, this);
      }
    }else{
      if (currentMW != null) {
        currentMW.reregisterComponentsForScripting();
      }
    }
    if (currentMW != null) {
      currentMW.adjustPlugView(this);
    }
    if (added) {
      // Update ActivationHandleGroups.
      addInActivationGroups(handle);
      // Send notification if the microworld has changed.
      if (((mw != null) && !mw.equals(currentMW)) ||
          ((currentMW != null) && !currentMW.equals(mw))) {
        microworldChanged(mw, currentMW);
      }
      // After adding the component, register any deferred performance timers,
      // and print their times. Do this here, so that the component is in its
      // correct place in the hierarchy, so that the associated timers can be
      // placed in the correct position in the timer hierarchy and their state
      // can be restored.
      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      pm.displayDeferredTimes(false);
    }
    return added;
  }

  /**
   * Add a collection of components to this component's list of child
   * components.
   * @param     c       A collection containing the E-Slate handles of the
   *                    components to add to the list.
   * @return    True if at least one of the components was actually added
   *            to the list, false otherwise (i.e., if all the components were
   *            already in the list).
   * @exception java.lang.UnsupportedOperationException Thrown if the
   *                    <code>addAll</code> method is not supported. (Not
   *                    thrown.)
   * @exception ClassCastException      Thrown if an element of the specified
   *                    collection is not an E-Slate handle.
   * @exception IllegalArgumentException        Thrown if some aspect of an
   *                    element of the specified collection prevents it from
   *                    being added to the list of child components. (Not
   *                    currently thrown.)
   */
  public boolean addAll(Collection c)
    throws java.lang.UnsupportedOperationException, ClassCastException,
           IllegalArgumentException
  {
    Iterator it = c.iterator();
    boolean status = false;
    while (it.hasNext()) {
      ESlateHandle handle = (ESlateHandle)(it.next());
      status |= add(handle);
    }
    return status;
    //return beanContext.addAll(handleToContext(c));
  }

  /**
   * Remove from the list of child components all the components contained
   * in a specified set of components.
   * @param     c       A collection containing the E-Slate handles of the
   *                    components to remove.
   * @return    True if at least one of the components was actually removed
   *            from the list, false otherwise (i.e., if none of the
   *            components were contained in the list).
   * @exception java.lang.UnsupportedOperationException Thrown if the
   *                    <code>removeALlar</code> method is not supported. (Not
   *                    thrown.)
   */
  public boolean removeAll(Collection c)
  {
    Iterator it = c.iterator();
    boolean status = false;
    while (it.hasNext()) {
      ESlateHandle handle = (ESlateHandle)(it.next());
      status |= remove(handle);
    }
    return status;
    //return beanContext.removeAll(handleToContext(c));
  }

  /**
   * Remove all child components.
   * @exception java.lang.UnsupportedOperationException Thrown if the
   *                    <code>clear</code> method is not supported. (Not
   *                    thrown).
   */
  public void clear() throws java.lang.UnsupportedOperationException
  {
    ESlateHandle[] h = toArray();
    for (int i=0; i<h.length; i++) {
      remove(h[i]);
    }
    //beanContext.clear();
  }

  /**
   * Checks whether a component is a child this component.
   * @param     h       The E-Slate handle of the component to check.
   * @return    True if the component is a child of this component, false
   *            otherwise.
   */
  public boolean contains(ESlateHandle h)
  {
    return beanContext.contains(h.getBeanContext());
  }

  /**
   * Checks whether a set of components are children of this component.
   * @param     c       A collection containing the E-Slate handles of the
   *                    components to check.
   */
  public boolean containsAll(Collection c)
  {
    return beanContext.containsAll(handleToContext(c));
  }

  /**
   * Checks if there are no components whose parent is this component.
   * @return    True if there are no components whose parent is this component,
   *            false otherwise.
   */
  public boolean isEmpty()
  {
    return beanContext.isEmpty();
  }

  /**
   * Returns an iterator over the E-Slate handles of the child components.
   * There are no guarantees concerning the order in which the elements are
   * returned.
   * @return    The requested iterator.
   */
  public Iterator<ESlateHandle> iterator()
  {
    ESlateHandle[] h = toArray();
    ArrayList<ESlateHandle> a = new ArrayList<ESlateHandle>(h.length);
    for (int i=0; i<h.length; i++) {
      a.add(h[i]);
    }
    return a.iterator();
  }

  /**
   * Removes a component from the list of child components.
   * @param     handle  The E-Slate handle of the component to remove from the
   *                    list.
   * @return    True if the component was removed from the list, false if
   *            not (i.e., if the component was not adopted by this component).
   */
  public boolean remove(ESlateHandle handle)
  {
    if (handle != null) {
      return remove(handle, true, true);
    }else{
      return false;
    }
  }

  /**
   * Removes a component from the list of child components.
   * @param     handle  The E-Slate handle of the component to remove from the
   *                    list.
   * @param     sendNotification        Specifies whether notification will
   *                    be sent to the registered ESlateListeners.
   * @param     reparent        Specifies whether the reparenting mechanism
   *                    should be started after the removal of the component.
   * @return    True if the component was removed from the list, false if
   *            not (i.e., if the component was not adopted by this component).
   */
  private boolean remove(ESlateHandle handle, boolean sendNotification,
                         boolean reparent)
  {
    if (beanContext == null) {
      return false;
    }
    if (!beanContext.contains(handle.getBeanContext())) {
      return false;
    }
    removeFromActivationGroups(handle); // Update ActivationHandleGroups.
    removeFromParentNameSpace(handle);
    boolean removed = beanContext.remove(handle.getBeanContext());
    ESlateMicroworld currentMW = getESlateMicroworld(this);
    if (removed) {
      if (myComponent instanceof ESlateMicroworld) {
        // Do any additional actions required when a component is removed from
        // a microworld.
        if (currentMW != null) {
          currentMW.removeComponent(handle);
        }
      }
      getRootComponentHandle().redoPlugView = true;
      getRootComponentHandle().redoPlugMenu = true;
      removeFromTree(handle);
      unhost(handle);
    }else{
      // If we failed to remove the component, put the name back in the name
      // space, to preserve consistency.
      addName(handle);
    }
    if (currentMW != null) {
      currentMW.reregisterComponentsForScripting();
    }

    // Notify listeners that component was removed; defer notification if
    // reparenting was requested, so that only one event is sent to each
    // listener.
    if (!reparent && removed && sendNotification) {
      handle.parentChanged(this, null);
    }

    boolean reparentOK = false;
    if (removed) {
      // Temporarily set the removed handle's reference to the current
      // microworld, so that getESlateMicroworld() returns a meaningful
      // result, so that reparenting / disposing the handle will work.
      handle.myESlateMicroworld = currentMW;
      if (reparent) {
        // If reparenting fails ESlate listeners will be notified before the
        // handle is disposed.
        reparentOK = this.reparent(handle, sendNotification);
      }
      // No need to restore the value of myESlateMicroworld: either it will
      // point to the new parent's microworld, or the handle will have been
      // disposed and myESlateMicroworld: will be set to null.
    }

    // If reparenting was requested and it was successful, notify listeners
    // now, so that we can send a single event to each listener signaling the
    // change of parent, rather than two events signaling the removal from
    // the old parent and the addition to the new parent.
    if (reparentOK && sendNotification) {
      if (reparentOK) {
        handle.parentChanged(this, handle.getParentHandle());
      }else{
        handle.parentChanged(this, null);
      }
    }

    return removed;
  }

  /**
   * Sets the parent of an E-Slate handle to one of its hosts.
   * If the handle has no hosts, the handle is disposed. Otherwise,
   * depending on what has been specified via the <code>setReparentType</code>
   * method, the component is destroyed or moved to another host, which is
   *  either selected at random or is specified by the user via an input dialog
   * (default).
   * @param     handle  The E-Slate handle of the component whose parent will
   *                    be set.
   * @param     sendNotification        Specifies whether notification will
   *                    be sent to the registered ESlateListeners before
   *                    disposing the handle.
   * @return    True if the handle's parent was set, false if the handle was
   *            disposed.
   */
  private boolean reparent(ESlateHandle handle, boolean sendNotification)
  {
    ESlateHandle[] h = handle.getHostHandles();
    int nHosts = h.length;
    for (int i=0; i<nHosts; i++) {
      if (this.equals(h[i])) {
        nHosts--;
        break;
      }
    }
    int reparentType;
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) {
      reparentType = myMW.getReparentType();
    }else{
      reparentType = ESlateMicroworld.REPARENT_NEVER;
    }
    if (nHosts == 0 || reparentType == ESlateMicroworld.REPARENT_NEVER) {
      if (sendNotification) {
        handle.parentChanged(this, null);
      }
      BooleanWrapper bw = new BooleanWrapper(false);
      if (!handle.isDesktopComponent() && !isDisposed()) {
        handle.toBeDisposed(false, bw);
      }
      handle.dispose();
      return false;
    }else{
      if (reparentType == ESlateMicroworld.REPARENT_RANDOM) {
        hosts.get(0).add(handle, false);
        return true;
      }else{
        // reparentType == ESlateMicroworld.REPARENT_ASK)
        String message =
          resources.getString("reparentQuery1") +
          " " + handle.getComponentName() + " " +
          resources.getString("reparentQuery2") + "\n" +
          resources.getString("reparentQuery3");
        Object[] labels = {
          resources.getString("reparentOK"),
          resources.getString("reparentCancel")
        };
        Object[] values = new Object[nHosts];
        for (int i=0, j=0; i<h.length; i++) {
          if (!(this.equals(h[i]))) {
            values[j++] = h[i].getComponentPathName();
          }
        }
        JOptionPane pane = new JOptionPane(
          message, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION,
          null, labels, labels[0]
        );
        pane.setWantsInput(true);
        pane.setSelectionValues(values);
        pane.setInitialSelectionValue(values[0]);
        JDialog dialog = pane.createDialog(
          myMW.getMicroworldEnvironmentAsComponent(),
          resources.getString("query")
        );
        pane.selectInitialValue();
        dialog.setVisible(true);
        Object newParent = pane.getInputValue();
        if (newParent == JOptionPane.UNINITIALIZED_VALUE) {
          if (sendNotification) {
            handle.parentChanged(this, null);
          }
          BooleanWrapper bw = new BooleanWrapper(false);
          if (!handle.isDesktopComponent() && !isDisposed()) {
            handle.toBeDisposed(false, bw);
          }
          handle.dispose();
          return false;
        }else{
          ESlateHandle hh =
            myMW.getESlateHandle().getChildHandle((String)newParent);
            //myMW.getComponentHandleFromTop((String)newParent);
          hh.add(handle, false);
          return true;
        }
      }
    }
  }

  /**
   * Removes the PlugTreeNode associated with an E-Slate handle from the list
   * of the component's PlugTreeNodes.
   * @param     handle  The handle to remove.
   */
  private void removeFromTree(ESlateHandle handle)
  {
    int n = plugs.getChildCount();
    for (int i=0; i<n; i++) {
      PlugTreeNode ptn = (PlugTreeNode)(plugs.getChildAt(i));
      ESlateHandle h = ptn.getHandle();
      if ((h != null) && h.equals(handle)) {
        plugs.remove(ptn);
        reloadModels();
        break;
      }
    }
  }

  /**
   * Adds a PlugTreeNode associated with an E-Slate handle to the list of the
   * component's PlugTreeNodes.
   * @param     handle  The handle to add.
   */
  private void addToTree(ESlateHandle handle)
  {
    plugs.add(handle.plugs);
    reloadModels();
  }

  /**
   * Remove from the list of child components all components except those
   * contained in a specified set of components.
   * @param     c       A collection containing the E-Slate handles of the
   *                    components not to remove.
   * @return    True if at least one of the components was actually removed
   *            from the list, false otherwise (i.e., if the specified
   *            collection contained the E-Slate handles of all the child
   *            components).
   * @exception java.lang.UnsupportedOperationException Thrown if the
   *                    <code>retainAll</code> method is not supported. (Not
   *                    thrown.)
   */
  public boolean retainAll(Collection c)
    throws java.lang.UnsupportedOperationException
  {
    ESlateHandle[] h = toArray();
    boolean status = false;
    for (int i=0; i<h.length; i++) {
      if (!(c.contains(h[i]))) {
        status |= remove(h[i]);
      }
    }
    return status;
    //return beanContext.retainAll(handleToContext(c));
  }

  /**
   * Returns the number of child components.
   * @return    The requested number. If there are more than
   *            <code>Integer.MAX_VALUE</code> child components,
   *            <code>Integer.MAX_VALUE</code> is returned instead.
   */
  public int size()
  {
    return beanContext.size();
  }

  /**
   * Returns an array containing the E-Slate handles of the child components.
   * There are no guarantees concerning the order in which the elements are
   * returned.
   * @return    The requested array.
   */
  public ESlateHandle[] getChildHandles()
  {
    if (beanContext != null) {
      Object[] o = beanContext.toArray();
      ESlateHandle[] h = new ESlateHandle[o.length];
      for (int i=0; i<o.length; i++) {
        h[i] = ((ESlateBeanContext)(o[i])).getESlateHandle();
      }
      return h;
    }else{
      return new ESlateHandle[0];
    }
  }

  /**
   * Returns an array containing the E-Slate handles of the child components.
   * There are no guarantees concerning the order in which the elements are
   * returned. This is a synonym for <code>getChildHandles()</code>.
   * @return    The requested array.
   */
  public ESlateHandle[] toArray()
  {
    return getChildHandles();
  }

  /**
   * Returns an array containing the E-Slate handles of the child components.
   * If the handles to return fit in the specified array, they are returned
   * therein. There are no guarantees concerning the order in which the
   * elements are returned.
   * @param     a       An array of E-Slate handles in which to attempt to
   *                    return the requested handles.
   * @return    The requested handles. If the handles are returned in the
   * specified array, and if the specified array contains is larger than the
   * number of returned handles, the element following the last specified
   * handle is set to null.
   * There are no guarantees concerning the order in which the elements are
   * @return    The requested array.
   */
  public ESlateHandle[] toArray(ESlateHandle[] a)
  {
    Object[] o = beanContext.toArray();
    if (o.length > a.length) {
      a = new ESlateHandle[o.length];
    }
    for (int i=0; i<o.length; i++) {
      System.arraycopy(o, 0, a, 0, o.length);
    }
    if (o.length < a.length) {
      a[o.length] = null;
    }
    return a;
  }

  /**
   * Converts a collection of E-SlateHandles to a collection of their
   * associated ESlateBeanContexts.
   * @param     c       The collection of E-Slate handles to convert.
   * @return    The requested collection of ESlateBeanContexts.
   */
  private Collection<BeanContextServicesSupport> handleToContext(Collection c)
  {
    Object[] o = c.toArray();
    ArrayList<BeanContextServicesSupport> a =
      new ArrayList<BeanContextServicesSupport>(o.length);
    for (int i=0; i<o.length; i++) {
      a.add(((ESlateHandle)(o[i])).getBeanContext());
    }
    return a;
  }

  /**
   * Associate a component with its name. As a side-effect, the name of the
   * component may be changed by appending a "_ and a number, 
   * @param     handle  The E-Slate handle of the component.
   */
  void addName(ESlateHandle handle)
  {
    try {
      if (childComponents == null) {
        NameServiceContext parentContext = getParentNameServiceContext();
        if (parentContext != null) {
          parentContext.changeIntoContext(myName);
          parentContext.setSeparator(SEPARATOR);
          childComponents = (NameServiceContext)(parentContext.get(myName));
        }else{
          ensureNameServiceContext();
          ensureParrentHasCorrectNameServiceContext();
        }
      }
      // Check if parent already has a component with the same name as the
      // component.
      String oldName = handle.getComponentName();
      Object o;
      try {
        o = childComponents.lookup(oldName);
      }catch (Exception e) {
        o = null;
      }
      // If there is such a component, construct a new name for the component,
      // otherwise maintain its current name.
      String newName;
      if (o != null) {
        newName = childComponents.constructUniqueName(oldName);
      }else{
        newName = oldName;
      }
      if (handle.childComponents != null) {
        handle.childComponents.setBoundObject(handle);
        childComponents.bind(newName, handle.childComponents);
      }else{
        childComponents.bind(newName, handle);
      }
      if (!newName.equals(oldName)) {
        handle.changeComponentName(newName);
      }
    } catch (NameServiceException e) {
    } catch (RenamingForbiddenException e) {
    } catch (NameUsedException e) {
    }
  }

  /**
   * Disassociate a component from its name.
   * @param     handle  The E-Slate handle of the component.
   */
  void removeName(ESlateHandle handle)
  {
    try {
      childComponents.unbind(handle.getComponentName());
    } catch (NameServiceException e) {
    }
  }

  /**
   * Returns the E-Slate handle of the parent component of this component.
   * @return    The requested handle. If this component is not the child of
   *            another component, this method returns null.
   */
  public ESlateHandle getParentHandle()
  {
    if (beanContext == null) {
      return null;
    }
    ESlateBeanContext parentContext = beanContext.getParentContext();
    if (parentContext != null) {
      return parentContext.getESlateHandle();
    }else{
      return null;
    }
  }

  /**
   * Returns the E-Slate handle of the component in the root of the component
   * hierarchy in which this component resides.
   * @return    The requested handle. If this component is not the child of
   *            another component, this method returns the handle of this
   *            component.
   */
  public ESlateHandle getRootHandle()
  {
    ESlateHandle parentHandle = this;
    ESlateHandle nextHandle = this;
    for (; nextHandle!= null; nextHandle=parentHandle.getParentHandle()) {
      parentHandle = nextHandle;
    }
    return parentHandle;
  }

  /**
   * Returns the E-Slate handle of the topmost, non-microworld,
   * E-Slate component in the
   * component hierarchy in which this component resides.
   * If the component is part of a microworld, the returned handle is an
   * immediate child of that microworld. In other words, this method returns
   * the E-Slate handle of the top-level component that contains this
   * component.
   * @return    The requested handle. If this component is not the child of
   *            another component, this method returns the handle of this
   *            component.
   */
  public ESlateHandle getRootComponentHandle()
  {
    ESlateHandle parentHandle = this;
    ESlateHandle nextHandle = this;
    for (; nextHandle!= null; nextHandle=parentHandle.getParentHandle()) {
      if (nextHandle.getComponent() instanceof ESlateMicroworld) {
        break;
      }
      parentHandle = nextHandle;
    }
    return parentHandle;
  }

  /**
   * Returns the topmost microworld in the component hierarchy containing this
   * component.
   * @return    The requested microworld. If the component is not contained
   *            within a microworld, this method return <code>null</code>.
   */
  public ESlateMicroworld getTopmostMicroworld()
  {
    ESlateMicroworld topMW = null;
    for (ESlateHandle h=this; h!= null; h=h.getParentHandle()) {
      Object c = h.getComponent();
      if (c instanceof ESlateMicroworld) {
        topMW = (ESlateMicroworld)c;
      }
    }
    return topMW;
  }

  /**
   * Returns the E-Slate handle of the topmost microworld in the component
   * hierarchy containing this component.
   * @return    The requested handle. If the component is not contained
   *            within a microworld, this method return <code>null</code>.
   */
  public ESlateHandle getTopmostMicroworldHandle()
  {
    ESlateMicroworld topMW = getTopmostMicroworld();
    if (topMW != null) {
      return topMW.getESlateHandle();
    }else{
      return null;
    }
  }

  /**
   * Returns the naming service context of the parent of this component.
   * @return    The requested context. If the component does not have a
   *            parent, this method returns null.
   */
  public NameServiceContext getParentNameServiceContext()
  {
    ESlateHandle h = getParentHandle();
    if (h != null) {
      h.ensureNameServiceContext();
      return h.childComponents;
    }else{
      return null;
    }
  }

  /**
   * Ensure that the handle has a naming service context.
   */
  private void ensureNameServiceContext()
  {
    if (childComponents == null) {
      childComponents = new NameServiceContext();
    }
  }

  /**
   * Ensure that the handle's parent has the correct reference to the handle's
   * name service context.
   */
  private void ensureParrentHasCorrectNameServiceContext()
  {
    try {
      ESlateHandle parent = getParentHandle();
      if (parent != null) {
        NameServiceContext parentChildComponents = parent.childComponents;
        NameServiceContext parentsContext =
        (NameServiceContext)parentChildComponents.get(myName);
        if ((childComponents == null) && (parentsContext != null)) {
          parentChildComponents.unbind(myName);
        }
        if ((childComponents != null) &&
            !childComponents.equals(parentsContext)) {
          parentChildComponents.unbind(myName);
          parentChildComponents.bind(myName, childComponents);
        }
      }
    } catch (NameServiceException e) {
      System.out.println("***BEGIN STACK TRACE");
      e.printStackTrace();
      System.out.println("***END STACK TRACE");
    }
  }

  /**
   * Checks whether this component is a top-level component, i.e., if it is
   * either a direct child of a microworld or it is not the child of another
   * component.
   * @return    True if yes, false otherwise.
   */
  public boolean isTopLevelComponent()
  {
    ESlateHandle parent = getParentHandle();

    // Components with no parent are top-level components.
    if (parent == null) {
      return true;
    }

    // Direct children of the topmost microworld in the component hierarchy
    // are top-level components.
    ESlateMicroworld topMW = getTopmostMicroworld();
    if ((topMW != null) && topMW.equals(parent.getComponent())) {
      return true;
    }

    // Everything else is not a top-level component.
    return false;
  }

  /**
   * Checks whether this component appears directly on the E-Slate desktop,
   * i.e., if it is a direct descendant of the topmost microworld.
   * @return    True if yes, false if no or if the component is not running
   *            under the E-Slate container.
   */
  public boolean isDesktopComponent()
  {
    ESlateHandle parentHandle = getParentHandle();
    ESlateMicroworld topMW = getTopmostMicroworld();
    if (parentHandle != null) {
      Object parent = parentHandle.getComponent();
      if ((parent != null) && (topMW != null) && parent.equals(topMW)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the nesting depth of a component in the component hierarchy.
   * @return    The requested depth. For top-level components this depth is 0.
   */
  public int nestingDepth()
  {
    int depth = -1;
    for (ESlateHandle h=this; h!=null; h=h.getParentHandle()) {
      depth++;
    }
    return depth;
  }

  /**
   * Returns the nesting depth of a component in the component hierarchy,
   * starting from the microworld.
   * @return    The requested depth. For components that are direct children
   *            of the microworld, this depth is 0. This depth can also be
   *            negative (e.g., -1 for the microworld, -2 for the container).
   * @exception NullPointerException    Thrown if the component does not
   *            belong to a microworld.
   */
  public int nestingDepthFromMicroworld()
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW == null) {
      throw new NullPointerException(resources.getString("nullMicroworld"));
    }

    int depth = -1;
    boolean found = false;
    ESlateHandle hMW = myMW.getESlateHandle();
    ESlateHandle h = this;

    while (h != null) {
      if (h.equals(hMW)) {
        found = true;
        break;
      }else{
        h = h.getParentHandle();
        depth++;
      }
    }

    if (!found) {
      depth = -1;
      h = hMW;
      while (h != null) {
        if (h.equals(this)) {
          break;
        }else{
          h = h.getParentHandle();
          depth--;
        }
      }
    }

    return depth;
  }

  /**
   * Add a listener for events regarding the addition and removal of child
   * components.
   * @param     listener        The listener to add.
   */
  public void addMembershipListener(BeanContextMembershipListener listener)
  {
    if (beanContext != null) {
      beanContext.addBeanContextMembershipListener(listener);
    }
  }

  /**
   * Remove a listener for events regarding the addition and removal of child
   * components.
   * @param     listener        The listener to remove.
   */
  public void removeMembershipListener(BeanContextMembershipListener listener)
  {
    if (beanContext != null) {
      beanContext.removeBeanContextMembershipListener(listener);
    }
  }

  /**
   * Returns the E-Slate handles of the children of the component that are
   * instances of one of a specified set of classes.
   * @param     c       The set of classes.
   * @return    An array containing the requested handles.
   */
  public ESlateHandle[] getChildrenOfType(Class[] c)
  {
    ESlateHandle[] h = toArray();
    int nClasses = c.length;
    int nHandles = h.length;
    ArrayList<ESlateHandle> a = new ArrayList<ESlateHandle>();
    for (int i=0; i<nHandles; i++) {
      for (int j=0; j<nClasses; j++) {
        if (c[j].isInstance(h[i].getComponent())) {
          a.add(h[i]);
          break;
        }
      }
    }
    int nChildren = a.size();
    h = new ESlateHandle[nChildren];
    for (int i=0; i<nChildren; i++) {
      h[i] = a.get(i);
    }
    return h;
  }

  /**
   * Hosts another component.
   * @param     handle  The E-Slate handle of the component to host.
   *                    If the handle is already hosted this method does
   *                    nothing.
   * @exception NullPointerException    Thrown if handle is null.
   */
  public void host(ESlateHandle handle) throws NullPointerException
  {
    if (handle == null) {
      throw new NullPointerException(resources.getString("nullHandle"));
    }
    int i = hostedComponents.indexOf(handle);
    if (i < 0) {
      // Host the component if it is not already hosted.
      hostedComponents.add(handle);
      handle.hosts.add(this);
    }
    // Set the microworld of the hosted component to that of the host.
    ESlateMicroworld oldMW = handle.getESlateMicroworld();
    ESlateMicroworld newMW = getESlateMicroworld(this);
    boolean sameMW;
    if (newMW == null) {
      if (oldMW == null) {
        sameMW = true;
      }else{
        sameMW = false;
      }
    }else{
      sameMW = newMW.equals(oldMW);
    }
    if (!sameMW) {
      handle.setESlateMicroworld(newMW);
    }
    if (i < 0) {
      // Update ActivationHandleGroups.
      addInActivationGroups(handle);
    }
  }

  /**
   * Stops hosting another component. If the component had been hosted
   * multiple times, the hosting relationship will be broken only when the
   * last hosting connection is broken.
   * If the handle is not being hosted by the component, this method does
   * nothing.
   * @param     handle  The E-Slate handle of the component to stop hosting.
   * @exception NullPointerException    Thrown if handle is null.
   */
  public void unhost(ESlateHandle handle) throws NullPointerException
  {
    if (handle == null) {
      throw new NullPointerException(resources.getString("nullHandle"));
    }
    int i = hostedComponents.indexOf(handle);
    if (i >= 0) {
      removeFromActivationGroups(handle); // Update ActivationHandleGroups.
      int count;
      ESlateMicroworld myMW = getESlateMicroworld();
      if (myMW != null) {
        count = myMW.hostedComponentsConnections(this, handle);
      }else{
        count = 0;
      }
      if (count == 0) {
        // Unhost the component.
        hostedComponents.removeElements(handle);
        handle.hosts.removeElements(this);
      }
    }
  }

  /**
   * Checks whether a component is hosted by this component.
   * @param     handle  The E-Slate handle of the component to check.
   * @return    True if yes, false otherwise.
   */
  public boolean isHosted(ESlateHandle handle)
  {
    return hostedComponents.contains(handle);
  }

  /**
   * Checks whether a component is hosted by this component.
   * @param     comp    The component to check.
   * @return    True if yes, false otherwise.
   */
  public boolean isHosted(Object comp)
  {
    ESlateHandle h;
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) {
      h = myMW.getComponentHandle(comp);
    }else{
      h = myMW.exhaustiveGetComponentHandle(comp);
    }
    return isHosted(h);
  }

  /**
   * Returns the E-Slate handles of the hosts of this component.
   * @return    An array containing the E-Slate handles of this component's
   *            hosts.
   */
  public ESlateHandle[] getHostHandles()
  {
    return hosts.toArray();
  }

  /**
   * Returns the E-Slate handles of the components hosted by this component.
   * @return    An array containing the E-Slate handles of the components
   *            hosted by this component.
   */
  public ESlateHandle[] getHostedHandles()
  {
    if (hostedComponents != null) {
      return hostedComponents.toArray();
    }else{
      return new ESlateHandle[0];
    }
  }

  /**
   * Returns the hosts of this component.
   * @return    An array containing the hosts of this component.
   */
  public Object[] getHosts()
  {
    int n = hosts.size();
    Object[] o = new Object[n];
    for (int i=0; i<n; i++) {
      o[i] = hosts.get(i).getComponent();
    }
    return o;
  }

  /**
   * Returns the components hosted by this component.
   * @return    An array containing the components hosted by this component.
   */
  public Object[] getHostedComponents()
  {
    int n = hostedComponents.size();
    Object[] o = new Object[n];
    for (int i=0; i<n; i++) {
      o[i] = hostedComponents.get(i).getComponent();
    }
    return o;
  }

  /**
   * Sets a flag indicating that the component contains Java beans produced
   * by BeanXporter, which require special handling by the container.
   * @param     flag    True if the component contains such beans, false if
   *                    not.
   */
  public void setUsesBeanXporterBeans(boolean flag)
  {
    beanXporter = flag;
  }

  /**
   * Checks whether the component contains Java beans produced by BeanXporter.
   * @return    True if the component contains such beans, false if not.
   */
  public boolean usesBeanXporterBeans()
  {
    return beanXporter;
  }

  /**
   * Specifies whether the handle will be visible in the parent component's
   * plug menu and in the plug editor when the component is the child of
   * another component.
   * @param     visible True if yes, false otherwise.
   */
  public void setVisible(boolean visible)
  {
    this.visible = visible;
    for (ESlateHandle h=this; h!=null; h=h.getParentHandle()) {
      h.redoPlugView = true;
      h.redoPlugMenu = true;
      h.reloadModels();
    }
  }

  /**
   * Checks whether the handle is visible in the parent component's plug
   * menu and in the plug editor when the component is the child of another
   * component.
   * @return    True if yes, false otherwise.
   */
  public boolean isVisible()
  {
    return visible;
  }

  /**
   * Save the state of child components. Convenience method that takes
   * care of all the ugly details of saving child components. �� use, merely
   * invoke this method from the component's writeExternal method. This method
   * does not recursively save the state of the child components' children;
   * child components must save the state of their children themselves, e.g.,
   * by invoking this method in their own writeExternal method.
   * @param     oo              Not used.
   * @param     map             A StorageStructure where parts of the state
   *                            will be saved. If the state is not being saved
   *                            in a microworld file, the entire state will be
   *                            saved there.
   * @param     key             The key under which these parts will be saved.
   *                            If the state is not being saved in a microworld
   *                            file, this parameter is ignored.
   * @param     children        The E-Slate handles of the children to save.
   *                            If <code>children</code> is <code>null</code>,
   *                            then all the component's children will be
   *                            saved. Note that this will include the
   *                            component's frame, which is managed by the
   *                            E-Slate workbench, so under normal
   *                            circumstances you do not want to do this.
   * @exception IOException     Thrown if saving the state fails.
   * @deprecated        As of E-Slate version 1.6.22, replaced by the version
   *                    of this method which does not require an output
   *                    stream.
   */
  public void saveChildren(ObjectOutput oo, StorageStructure map, String key,
                           ESlateHandle[] children)
    throws IOException
  {
    saveChildren(map, key, children);
  }

  /**
   * Save the state of child components. Convenience method that takes
   * care of all the ugly details of saving child components. �� use, merely
   * invoke this method from the component's writeExternal method. This method
   * does not recursively save the state of the child components' children;
   * child components must save the state of their children themselves, e.g.,
   * by invoking this method in their own writeExternal method.
   * @param     map             A StorageStructure where parts of the state
   *                            will be saved. If the state is not being saved
   *                            in a microworld file, the entire state will be
   *                            saved there.
   * @param     key             The key under which these parts will be saved.
   *                            If the state is not being saved in a microworld
   *                            file, this parameter is ignored.
   * @param     children        The E-Slate handles of the children to save.
   *                            If <code>children</code> is <code>null</code>,
   *                            then all the component's children will be
   *                            saved. Note that this will include the
   *                            component's frame, which is managed by the
   *                            E-Slate workbench, so under normal
   *                            circumstances you do not want to do this.
   * @exception IOException     Thrown if saving the state fails.
   */
  public void saveChildren(StorageStructure map, String key,
                           ESlateHandle[] children)
    throws IOException
  {
/*
    int nChildren = children.length;
    Object childObjects[] = new Object[nChildren];
    for (int i=0; i<nChildren; i++) {
      childObjects[i] = children[i];
    }
    saveChildObjects(map, key, childObjects);
*/
    saveChildObjects(map, key, children);
  }

  /**
   * Save the state of child components of arbitrary type (e.g., E-Slate
   * sub-components, or AWT components that have been added to the component).
   * Convenience method that takes care of all the ugly details of saving
   * child components. �� use, merely invoke this method from the component's
   * writeExternal method. This method does not recursively save the state of
   * the child components' children; child components must save the state of
   * their children themselves, e.g., by invoking this method in their own
   * writeExternal method.
   * @param     map             A StorageStructure where parts of the state
   *                            will be saved. If the state is not being saved
   *                            in a microworld file, the entire state will be
   *                            saved there.
   * @param     key             The key under which these parts will be saved.
   *                            If the state is not being saved in a microworld
   *                            file, this parameter is ignored.
   * @param     children        An array containing references to the
   *                            children. In most cases, these references will
   *                            be the E-Slate handles of the child
   *                            components. If, for some reason, you need to
   *                            save the state of objects that are not
   *                            E-Slate components, but have somehow been
   *                            associated with this component, then, instead of
   *                            their E-Slate handles, which do not exist, you
   *                            can pass references to the objects themselves.
   *                            <EM>Note:</EM> if you pass a reference to an
   *                            E-Slate component instead of a reference to
   *                            its handle, then, when the parent component is
   *                            restored, the component will <EM>not</EM> be
   *                            made a child of the parent component!
   *                            If <code>children</code> is <code>null</code>,
   *                            then all the component's child E-Slate
   *                            components  will be saved. Note that this
   *                            will include the component's frame, which is
   *                            managed by the E-Slate workbench, so under
   *                            normal circumstances you do not want to do
   *                            this.
   * @exception IOException     Thrown if saving the state fails.
   */
  @SuppressWarnings(value={"unchecked"})
  public void saveChildObjects(StorageStructure map, String key,
                               Object[] children)
    throws IOException
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    boolean savingMicroworld =
      (myMW != null) && (myMW.getState() == ESlateMicroworld.SAVING);
    if (children == null) {
      children = toArray();
    }
    int nChildren = children.length;
    ArrayList data = new ArrayList(nChildren * 2);
    boolean savingArbitraryComponents = false;
    // Convert any ESlate handles of components that are not our children
    // to the corresponding components. This way, we avoid problems is cases
    // where, e.g., an E-Slate component was used as a plain AWT component.
    Object childObjects[] = new Object[nChildren];
    for (int i=0; i<nChildren; i++) {
      childObjects[i] = children[i];
      if (childObjects[i] instanceof ESlateHandle) {
        ESlateHandle h = (ESlateHandle)(childObjects[i]);
        if (!h.getParentHandle().equals(this)) {
          childObjects[i] = h.getComponent();
        }
      }
    }
    for (int i=0; i<nChildren; i++) {
      String cl;
      if (childObjects[i] instanceof ESlateHandle) {
        ESlateHandle h = (ESlateHandle)(childObjects[i]);
        cl = h.getComponent().getClass().getName();
        data.add(h.getComponentName());
        data.add(cl);
      }else{
        cl = childObjects[i].getClass().getName();
        data.add(null);
        data.add(cl);
        savingArbitraryComponents = true;
      }
    }
    ByteArrayOutputStream bos = null;
    ObjectOutputStream oos = null;
    if (!savingMicroworld || savingArbitraryComponents) {
      bos = new ByteArrayOutputStream(1024*16);
      oos = new ObjectOutputStream(bos);
    }
    for (int i=0; i<nChildren; i++) {
      // If saving in a microworld file, save the state of each child in a
      // separate entry in the microworld file. Otherwise, write the
      // state in a byte array output stream and store the bytes of the stream
      // in the field map.
      if (savingMicroworld && (childObjects[i] instanceof ESlateHandle)) {
        myMW.saveState((ESlateHandle)(childObjects[i]));
      }else{
        Object component;
        if (childObjects[i] instanceof ESlateHandle) {
          component = ((ESlateHandle)(childObjects[i])).getComponent();
        }else{
          component = childObjects[i];
        }
        if (component instanceof Externalizable) {
          ((Externalizable)component).writeExternal(oos);
        }else{
          if (component instanceof Serializable) {
            oos.writeObject(component);
          }else{
            throw new IOException(resources.getString("notExtSerializable"));
          }
        }
      }
    }
    ESlateFieldMap2 infoMap = new ESlateFieldMap2(CHILD_STATE_VERSION, 3);
    infoMap.put(DATA, data);
    if (!savingMicroworld || savingArbitraryComponents) {
      infoMap.put(STREAM_DATA, bos.toByteArray());
      oos.close();
    }
    if (!savingMicroworld) {
      saveConnections(infoMap);
      infoMap.put(OWN_NAME, myName);
    }

    infoMap.put(NESTING_DEPTH, nestingDepthFromMicroworld());

    map.put(key, infoMap);
  }

  /**
   * Returns the pairs of connected plugs that refer to plugs owned by
   * components whose nearest common ancestor is this component.
   * @return    A list of ConnectedPair objects.
   */
  private ConnectedPairBaseArray getConnectedPairs()
  {
    ConnectedPairBaseArray v = new ConnectedPairBaseArray();
    ConnectedPairBaseArray mwcp = ESlateMicroworld.connectedPairs;
    int n = mwcp.size();
    for (int i=0; i<n; i++) {
      ConnectedPair p = mwcp.get(i);
      ESlateHandle h = p.findCommonAncestor();
      if (this.equals(h)) {
        v.add(p);
      }
    }
    return v;
  }

  /**
   * Stores the plug connections that are local to the sub-tree with this
   * component as the root.
   * @param     map     The StorageStructure in which the connections will be
   * stored.
   */
  private void saveConnections(StorageStructure map)
  {
    ConnectedPairBaseArray v = getConnectedPairs();
    int n = v.size();
    String[] providers = new String[n];
    String[] dependents = new String[n];
    StringBaseArray[] providerPlugs = new StringBaseArray[n];
    StringBaseArray[] dependentPlugs = new StringBaseArray[n];
    for (int i=0; i<n; i++) {
      ConnectedPair pair = v.get(i);
      providers[i] = pair.provider.getComponentPathName();
      dependents[i] = pair.dependent.getComponentPathName();
      StringBaseArray plugName = new StringBaseArray();
      for (Plug p = pair.providerPlug; p != null; p = p.getParentPlug()) {
        plugName.add(p.getInternalName());
      }
      providerPlugs[i] = plugName;
      plugName = new StringBaseArray();
      for (Plug p = pair.dependentPlug; p != null; p = p.getParentPlug()) {
        plugName.add(p.getInternalName());
      }
      dependentPlugs[i] = plugName;
    }
    map.put(PROVIDERS, providers);
    map.put(DEPENDENTS, dependents);
    map.put(PROVIDER_PLUGS, providerPlugs);
    map.put(DEPENDENT_PLUGS, dependentPlugs);
  }

  /**
   * Save the state of child components. Convenience method that takes
   * care of all the ugly details of saving child components. �� use, merely
   * invoke this method from the component's writeExternal method. This method
   * does not recursively save the state of the child components' children;
   * child components must save the state of their children themselves, e.g.,
   * by invoking this method in their own writeExternal method.
   * @param     oo              Not used.
   * @param     map             A StorageStructure where parts of the state
   *                            will be saved. If the state is not being saved
   *                            in a microworld file, the entire state will be
   *                            saved there.
   *                            in a microworld file, this parameter is ignored.
   * @param     key             The key under which these parts will be saved.
   *                            If the state is not being saved in a microworld
   *                            file, this parameter is ignored.
   * @param     children        The E-Slate handles of the children to save.
   *                            If <code>children</code> is <code>null</code>,
   *                            then all the component's children will be
   *                            saved. Note that this will include the
   *                            component's frame, which is managed by the
   *                            E-Slate workbench, so under normal
   *                            circumstances you do not want to do this.
   * @exception IOException     Thrown if saving the state fails.
   * @deprecated        As of E-Slate version 1.6.22, replaced by the version
   *                    of this method which does not require an output
   *                    stream.
   */
  public void saveChildren(ObjectOutput oo, StorageStructure map, String key,
                           Collection children)
    throws IOException
  {
    saveChildren(map, key, children);
  }

  /**
   * Save the state of child components. Convenience method that takes
   * care of all the ugly details of saving child components. �� use, merely
   * invoke this method from the component's writeExternal method. This method
   * does not recursively save the state of the child components' children;
   * child components must save the state of their children themselves, e.g.,
   * by invoking this method in their own writeExternal method.
   * @param     map             A StorageStructure where parts of the state
   *                            will be saved. If the state is not being saved
   *                            in a microworld file, the entire state will be
   *                            saved there.
   * @param     key             The key under which these parts will be saved.
   *                            If the state is not being saved in a microworld
   *                            file, this parameter is ignored.
   * @param     children        The E-Slate handles of the children to save.
   *                            If <code>children</code> is <code>null</code>,
   *                            then all the component's children will be
   *                            saved. Note that this will include the
   *                            component's frame, which is managed by the
   *                            E-Slate workbench, so under normal
   *                            circumstances you do not want to do this.
   * @exception IOException     Thrown if saving the state fails.
   */
  public void saveChildren(
    StorageStructure map, String key, Collection children)
    throws IOException
  {
    Object[] childObjects;
    if (children == null) {
      childObjects = null;
    }else{
      try {
        int nChildren = children.size();
        childObjects = new Object[nChildren];
        Iterator it = children.iterator();
        for (int i=0; i<nChildren; i++) {
          // Cast to ESlateHandle so that we get an exception if the user
          // puts anything other than ESlateHandles in children.
          childObjects[i] = (ESlateHandle)(it.next());
        }
        saveChildObjects(map, key, childObjects);
      } catch (ClassCastException e) {
        throw new IOException(e.getMessage());
      }
    }
  }

  /**
   * Save the state of child components of arbitrary type (e.g., E-Slate
   * sub-components, or AWT components that have been added to the component).
   * Convenience method that takes care of all the ugly details of saving
   * child components. �� use, merely invoke this method from the component's
   * writeExternal method. This method does not recursively save the state of
   * the child components' children; child components must save the state of
   * their children themselves, e.g., by invoking this method in their own
   * writeExternal method.
   * @param     map             A StorageStructure where parts of the state
   *                            will be saved. If the state is not being saved
   *                            in a microworld file, the entire state will be
   *                            saved there.
   * @param     key             The key under which these parts will be saved.
   *                            If the state is not being saved in a microworld
   *                            file, this parameter is ignored.
   * @param     children        An array containing references to the
   *                            children. In most cases, these references will
   *                            be the E-Slate handles of the child
   *                            components. If, for some reason, you need to
   *                            save the state of objects that are not
   *                            E-Slate components, but have somehow been
   *                            associated with this component, then, instead of
   *                            their E-Slate handles, which do not exist, you
   *                            can pass references to the objects themselves.
   *                            <EM>Note:</EM> if you pass a reference to an
   *                            E-Slate component instead of a reference to
   *                            its handle, then, when the parent component is
   *                            restored, the component will <EM>not</EM> be
   *                            made a child of the parent component!
   *                            If <code>children</code> is <code>null</code>,
   *                            then all the component's child E-Slate
   *                            components  will be saved. Note that this
   *                            will include the component's frame, which is
   *                            managed by the E-Slate workbench, so under
   *                            normal circumstances you do not want to do
   *                            this.
   * @exception IOException     Thrown if saving the state fails.
   */
  public void saveChildObjects(StorageStructure map, String key,
                               Collection children)
    throws IOException
  {
    Object[] childObjects;
    if (children == null) {
      childObjects = null;
    }else{
      try {
        int nChildren = children.size();
        childObjects = new Object[nChildren];
        Iterator it = children.iterator();
        for (int i=0; i<nChildren; i++) {
          childObjects[i] = it.next();
        }
        saveChildObjects(map, key, childObjects);
      } catch (ClassCastException e) {
        throw new IOException(e.getMessage());
      }
    }
  }

  /**
   * Restore the state of any child components. Convenience method that takes
   * care of all the ugly details of restoring child components. This method
   * should only be used if the component's children were saved using the
   * saveChildren() method. �� use, merely invoke this method from the
   * component's readExternal method. This method does not recursively
   * restore the state of the child components' children; child components
   * must restore the state of their children themselves, e.g., by invoking
   * this method in their own readExternal method.
   * @param     oi      Ignored.
   * @param     map     A StorageStructure from where parts of the state will
   *                    be restored. If the state is not being loaded from a
   *                    microworld file, the entire state is contained there.
   * @param     key     The key under which these parts were saved.
   *                    If the state is not being loaded from a microworld
   *                    file, this parameter is ignored.
   * @exception IOException     Thrown if restoring the state fails.
   * @deprecated        As of E-Slate version 1.6.22, replaced by the version
   *                    of this method which does not require an input
   *                    stream.
   */
  public void restoreChildren(ObjectInput oi, StorageStructure map, String key)
    throws IOException
  {
    restoreChildren(map, key);
  }

  /**
   * Restore the state of any child components. Convenience method that takes
   * care of all the ugly details of restoring child components. This method
   * should only be used if the component's children were saved using the
   * saveChildren() method. �� use, merely invoke this method from the
   * component's readExternal method. This method does not recursively
   * restore the state of the child components' children; child components
   * must restore the state of their children themselves, e.g., by invoking
   * this method in their own readExternal method.
   * @param     map     A StorageStructure from where parts of the state will
   *                    be restored. If the state is not being loaded from a
   *                    microworld file, the entire state is contained there.
   * @param     key     The key under which these parts were saved.
   *                    If the state is not being loaded from a microworld
   *                    file, this parameter is ignored.
   * @exception IOException     Thrown if restoring the state fails.
   */
  public void restoreChildren(StorageStructure map, String key)
    throws IOException
  {
    restoreChildObjects(map, key);
  }

  /**
   * Restore the state of any child components of arbitrary type (e.g., E-Slate
   * sub-components, or AWT components that had been added to the component).
   * Convenience method that takes
   * care of all the ugly details of restoring child components. This method
   * should only be used if the component's children were saved using the
   * saveChildren() method. �� use, merely invoke this method from the
   * component's readExternal method. This method does not recursively
   * restore the state of the child components' children; child components
   * must restore the state of their children themselves, e.g., by invoking
   * this method in their own readExternal method.
   * @param     map     A StorageStructure from where parts of the state will
   *                    be restored. If the state is not being loaded from a
   *                    microworld file, the entire state is contained there.
   * @param     key     The key under which these parts were saved.
   *                    If the state is not being loaded from a microworld
   *                    file, this parameter is ignored.
   * @exception IOException     Thrown if restoring the state fails.
   * @return    children        A collection containing references to the
   *                            children. If a child is an E-Slate component,
   *                            use its E-Slate handle as a reference,
   *                            otherwise use the component itself.
   *                            If <code>children</code> is <code>null</code>,
   *                            then all the component's child E-Slate
   *                            components  will be saved. Note that this
   *                            will include the component's frame, which is
   *                            managed by the E-Slate workbench, so under
   *                            normal circumstances you do not want to do
   *                            this.
   */
  public Object[] restoreChildObjects(StorageStructure map, String key)
    throws IOException
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    boolean loadingMicroworld =
      (myMW != null) && (myMW.getState() == ESlateMicroworld.LOADING);

    ArrayList data;
    ObjectInputStream ois = null;
    Object obj = map.get(key);
    String[] providers = null;
    String[] dependents = null;
    Object[] children = null;
    String ownName = null;
    // These two are either instances of Vector[] or of StringBaseArray[].
    Object providerPlugs = null;
    Object dependentPlugs = null;
    int depth;

    if (obj instanceof StorageStructure) {
      map = (StorageStructure)obj;
      data = (ArrayList)(map.get(DATA));
      byte[] b = (byte[])(map.get(STREAM_DATA));
      if (b != null) {
        ois = new ObjectInputStream(new ByteArrayInputStream(b));
      }
      providers = (String[])(map.get(PROVIDERS));
      dependents = (String[])(map.get(DEPENDENTS));
      providerPlugs = map.get(PROVIDER_PLUGS);
      dependentPlugs = map.get(DEPENDENT_PLUGS);
      ownName = (String)(map.get(OWN_NAME));
      depth = map.get(NESTING_DEPTH, 0);
    }else{
      // For compatibilty with earlier versions.
      data = (ArrayList)obj;
      depth = 0;
    }

    if (data != null) {
      int nData = data.size();
      children = new Object[nData/2];
      for (int i=0; i<nData; i+=2) {
        children[i/2] = null;
        String componentName = (String)(data.get(i));
        String componentClass = (String)(data.get(i+1));
        Object component = null;
        ESlateHandle h = null;
        if (componentName != null) {
          try {
            Class cl = Class.forName(componentClass);
            if (loadingMicroworld) {
              component = myMW.instantiateComponent(cl, this, componentName);
            }
            if ((component == null) &&
                Externalizable.class.isAssignableFrom(cl)) {
              component = cl.newInstance();
            }
          } catch (Exception e) {
            System.out.println("***BEGIN STACK TRACE");
            e.printStackTrace();
            System.out.println("***END STACK TRACE");
          }
          h = getESlateHandle(component);
          h.setESlateMicroworld(myMW);
          add(h);
          enableRenaming();
          try {
            h.setComponentName(componentName);
          }catch (Exception e){
            System.out.println("***BEGIN STACK TRACE");
            e.printStackTrace();
            System.out.println("***END STACK TRACE");
          }
          restoreRenaming();
        }else{
          try {
            Class cl = Class.forName(componentClass);
            if (Externalizable.class.isAssignableFrom(cl)) {
              component = cl.newInstance();
            }
          } catch (ClassNotFoundException e) {
            System.out.println("***BEGIN STACK TRACE");
            e.printStackTrace();
            System.out.println("***END STACK TRACE");
          } catch (InstantiationException e) {
            System.out.println("***BEGIN STACK TRACE");
            e.printStackTrace();
            System.out.println("***END STACK TRACE");
          } catch (IllegalAccessException e) {
            System.out.println("***BEGIN STACK TRACE");
            e.printStackTrace();
            System.out.println("***END STACK TRACE");
          }
        }
        if (h != null) {
          children[i/2] = h;
        }else{
          children[i/2] = component;
        }
        // If loading from a microworld file, load the state of each child
        // from a separate entry in the microworld file. Otherwise,
        // read the state from the stream stored in the field map.
        if (loadingMicroworld && (componentName != null)) {
          myMW.loadState(h);
        }else{
          if (ois != null) {
            try {
              if (component instanceof Externalizable) {
                ((Externalizable)component).readExternal(ois);
              }else{
                component = ois.readObject();
                if (h != null) {
                  h.setComponent(component);
                  children[i/2] = h;
                }else{
                  children[i/2] = component;
                }
              }
            } catch (ClassNotFoundException cnfe) {
              IOException ioe = new IOException(cnfe.getMessage());
              ioe.initCause(cnfe);
              throw ioe;
            }
          }else{
            System.out.print("*** Not loading state for ");
            if (h != null) {
              System.out.print(h);
            }else{
              System.out.print(component.getClass().getName());
            }
            System.out.print(": parent microworld is ");
            if (myMW == null) {
              System.out.println("null");
            }else{
              System.out.println("not loading");
            }
            System.out.println(
              "*** and state is not being restored from external data."
            );
          }
        }
      }
      if ((ownName != null) && !myName.equals(ownName)) {
        enableRenaming();
        try {
          // Yes, there are *two* calls to setUniqueComponentName:
          // This is to ensure that we don't get a "_" name, when we can
          // avoid it, or too high a number after the "_" when we can't.
          setUniqueComponentName(ESlateMicroworld.reservedPrefix);
          setUniqueComponentName(ownName);
          restoredOwnName = true;
        } catch(RenamingForbiddenException e) {
        }
        restoreRenaming();
      }
      if ((providers != null) && (dependents != null) &&
          (providerPlugs != null) && (dependentPlugs != null)) {
        reconnectPlugs(
          providers, dependents, providerPlugs, dependentPlugs, depth
        );
      }
    }
    if (ois != null) {
      ois.close();
    }
    return children;
  }

  /**
   * Returns the E-Slate handle of a component using several methods.
   * <UL>
   * <LI>If the component is an ESlatePart, the result of its
   * <code>getESlateHandle()</code> method is returned.</LI>
   * <LI>If the componnst has a <code>getESlateHandle()</code> method, its
   * result is returned.</LI>
   * <LI>If none of the above is true, the component is registered with
   * E-Slate, and the resulting handle is returned.</LI>
   * @param     component       The component.
   * @return    The E-Slate handle of the componnet.
   */
  static ESlateHandle getESlateHandle(Object component)
  {
    ESlateHandle h = null;;

    if (component instanceof ESlatePart) {
      h = ((ESlatePart)component).getESlateHandle();
    }else{
      if (component != null) {
        // Invoke the component's getESlateHandle() method using
        // reflection, as component may implement the getESlateHandle()
        // method without implementing the ESlatePart interface.
        Method m = null;
        try {
          Class[] args = new Class[0];
          Class<?> cl = component.getClass();
          m = cl.getMethod("getESlateHandle", args);
          if (m.getReturnType() != ESlateHandle.class) {
            m = null;
          }
        } catch (Exception e) {
          m = null;
        }
        if (m != null) {
          try {
            h = (ESlateHandle)(m.invoke(component, new Object[0]));
          }catch (Exception e) {
          }
        }
      }
    }
    if (h == null) {
      if (component != null) {
        h = ESlate.registerPart(component);
      } else{
        // Component will be filled in when it is deserialized in
        // loadState.
        h = ESlate.registerPart();
      }
    }
    return h;
  }

  /**
   * Packs the private data files of the component into a byte array.
   * @return    The contents of a zip file containing the private data files
   *            of the component. If there are no data to be packed, this
   *            method returns <code>null</code>.
   * @exception IOException     Thrown when accessing the microworld file
   *                            fails for some reason.
   */
  private byte[] packPrivateFiles() throws IOException
  {
    if (rootDirExists()) {
      StructFile sf = getESlateMicroworld().getMicroworldFile();
      synchronized (sf) {
        Entry oldDir = sf.getCurrentDirEntry();
        try {
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          boolean written;
          try {
            ZipSFDir.zip(this, bos);
            written = true;
          } catch (Exception e) {
            written = false;
          }
          bos.close();
          if (written) {
            return bos.toByteArray();
          }else{
            return null;
          }
        } finally {
          sf.changeDir(oldDir);
        }
      }
    }else{
      return null;
    }
  }

  /**
   * Unpacks the private data files of the component from a byte array
   * into the current microworld file.
   * Use this method to implement saving or copying
   * component sub-trees.
   * @param data        The contents of a zip file containing the private
   *                    data files of the component.
   * @exception IOException     Thrown when accessing the microworld file
   *                            fails for some reason.
   */
  private void unpackPrivateFiles(byte[] data) throws IOException
  {
    boolean doUnpack = false;
    try {
      doUnpack = (data != null) && ensureDirExists();
    } catch (IOException ioe) {
      if (resources.getString("noMicroworldFile").equals(ioe.getMessage())) {
        throw new IOException(resources.getString("needMWToUnpack"));
      }else{
        throw ioe;
      }
    }
    if (doUnpack) {
      StructFile sf = getESlateMicroworld().getMicroworldFile();
      synchronized (sf) {
        Entry oldDir = sf.getCurrentDirEntry();
        try {
          ByteArrayInputStream bis = new ByteArrayInputStream(data);
          UnzipSFDir.unzip(this, bis);
          bis.close();
        } finally {
          sf.changeDir(oldDir);
        }
      }
    }
  }

  /**
   * Packs the private data files of the component and all its descendants
   * into a StorageStructure, by invoking <code>packPrivateFiles()<code> for
   * each component.
   * Use this method to implement saving or copying
   * component sub-trees.
   * @return    A StorageStructure containing the contents of zip files
   *            containing the private data files of all components
   *            that have such files. The key used for each component is
   *            the path name of the component relative to this component ("."
   *            for this component).
   * @exception IOException     Thrown when accessing the microworld file
   *                            fails for some reason.
   */
  public StorageStructure packAllPrivateFiles() throws IOException
  {
    ArrayList<ESlateHandle> a = new ArrayList<ESlateHandle>();
    collectHandles(this, a);
    int n = a.size();
    ESlateFieldMap2 map = new ESlateFieldMap2(1);
    for (int i=0; i<n; i++) {
      ESlateHandle h = a.get(i);
      byte[] b = h.packPrivateFiles();
      if (b != null) {
        String key = h.getComponentPath(this);
        map.put(key, b);
      }
    }
    return map;
  }

  /**
   * Unpacks the private data files of the component and all its descendants
   * from a StorageStructure, by invoking <code>unpackPrivateFiles()<code> for
   * each component.
   * @param     map     The StorageStructure returned by
   *                    <code>packAllPrivateFiles()</code>.
   * @exception IOException     Thrown when accessing the microworld file
   *                            fails for some reason.
   */
  public void unpackAllPrivateFiles(StorageStructure map) throws IOException
  {
    ArrayList<ESlateHandle> a = new ArrayList<ESlateHandle>();
    collectHandles(this, a);
    int n = a.size();
    for (int i=0; i<n; i++) {
      ESlateHandle h = a.get(i);
      String key = h.getComponentPath(this);
      byte[] b = (byte[])(map.get(key));
      if (b != null) {
        h.unpackPrivateFiles(b);
      }
    }
  }

  /**
   * Stores information other than component state, that is relevant to
   * the component sub-tree rooted at this component. This information
   * includes plug connections. Use this method to implement saving or copying
   * component sub-trees.
   * @return    A StorageStructure containing the information.
   * @exception IOException     Thrown when accessing the microworld file
   *                            fails for some reason.
   */
  public StorageStructure saveSubTreeInfo() throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(1, 6);
    saveConnections(map);
    map.put(OWN_NAME, myName);
    map.put(NESTING_DEPTH, nestingDepthFromMicroworld());
    return map;
  }

  /**
   * Restores information other than component state, that is relevant to
   * the component sub-tree rooted at this component. This information
   * includes plug connections. For components that save and restore the
   * state of their children using <code>saveChildObjects()</code> and
   * <code>restoreChildObjects()</code>, plug connections will be restored
   * there. This method will also attempt to restore plug connections, to
   * ensure that connections between plugs of a component without children,
   * which does not invoke <code>restoreChildObjects()</code>, are restored
   * correctly. Use this method to implement reading or pasting component
   * sub-trees.
   * @param     map     A StorageStructure containing the information.
   * @exception IOException     Thrown when accessing the microworld file
   *                            fails for some reason.
   */
  public void restoreSubTreeInfo(StorageStructure map) throws IOException
  {
    String[] providers = (String[])(map.get(PROVIDERS));
    String[] dependents = (String[])(map.get(DEPENDENTS));
    // These two are either instances of Vector[] or of StringBaseArray[].
    Object providerPlugs = map.get(PROVIDER_PLUGS);
    Object dependentPlugs = map.get(DEPENDENT_PLUGS);
    int depth = map.get(NESTING_DEPTH, 0);
    reconnectPlugs(providers, dependents, providerPlugs, dependentPlugs, depth);
    String ownName = (String)map.get(OWN_NAME);
    if ((ownName != null && !restoredOwnName)) {
      enableRenaming();
      try {
        // Yes, there are *two* calls to setUniqueComponentName:
        // This is to ensure that we don't get a "_" name, when we can
        // avoid it, or too high a number after the "_" when we can't.
        setUniqueComponentName(ESlateMicroworld.reservedPrefix);
        setUniqueComponentName(ownName);
      } catch(RenamingForbiddenException e) {
      }
      restoreRenaming();
    }
    restoredOwnName = false;
  }

  /**
   * Recursively collects the E-Slate handles of an entire sub-tree in the
   * microworld hierarchy.
   * @param     h       The root of the sub-tree.
   * @param     a       The list where the handles will be collected.
   */
  private static void collectHandles(ESlateHandle h, ArrayList<ESlateHandle> a)
  {
    a.add(h);
    ESlateHandle[] children = h.getChildHandles();
    int n = children.length;
    for (int i=0; i<n; i++) {
      collectHandles(children[i], a);
    }
  }

  /**
   * Modifies a list of component names, so that the resulting paths are top
   * level paths relative to the microworld. Before modifying the paths,
   * top-level names are changed to the name of the current component.
   * @param     names   The component names.
   * @param     depth   The nesting depth from the microworld of the
   *                    component for which the component names had been
   *                    produced.
   */
  private void makeTopLevelPaths(String[] names, int depth)
  {
    ESlateHandle parent = getParentHandle();
    if (parent != null) {
      char sep;
      if (childComponents != null) {
        sep = childComponents.getSeparator();
      }else{
        NameServiceContext c = getParentNameServiceContext();
        if (c != null) {
          sep = c.getSeparator();
        }else{
          sep = '.';
        }
      }
      String prefix;
      // If this is a top-level component, the path names are correct.
      // Otherwise, append our parent's path to the path name.
      if (parent.equals(getESlateMicroworld().getESlateHandle())) {
        prefix = "";
      }else{
        prefix = parent.getComponentPathName() + sep;
      }
      int n = names.length;
      int nSkip = depth + 1;
      for (int i=0; i<n; i++) {
        String s = names[i];
        int pos = -1;
        // Skip as many path components as correspond to the compponent's
        // depth
        for (int j=0; j<nSkip; j++) {
          pos = s.indexOf(sep, pos+1);
          if (pos < 0) {
            break;
          }
        }
        // Change the first component of each path name to this component's
        // name...
        if (pos < 0) {
          s = myName;
        }else{
          s = myName + s.substring(pos);
        }
        // ...then prepend the parent's path, if necessary.
        names[i] = prefix + s;
      }
    }
  }

  /**
   * Reconnect the plugs in the hierarchy of a component whose state was loaded
   * from a plain input stream rather than a microworld file.
   * @param     providers       A list of the names of the provider components
   *                            in the connection pairs that will be
   *                            reestablished.
   * @param     dependents      A list of the names of the dependent components
   *                            in the connection pairs that will be
   *                            reestablished.
   * @param     providerPlugs   A list of the names of the provider plugs in
   *                            the connection pairs that will be
   *                            reestablished. This is either an instance of
   *                            Vector[] or of StringBaseArray[].
   * @param     dependentPlugs  A list of the names of the dependent plugs in
   *                            the connection pairs that will be
   *                            reestablished. This is either an instance of
   *                            Vector[] or of StringBaseArray[].
   * @param     depth           The nesting depth from the microworld of the
   *                            component that was saved in the input stream.
   */
  private void reconnectPlugs(String[] providers, String[] dependents,
                              Object providerPlugs, Object dependentPlugs,
                              int depth)
    throws IOException
  {
    if ((providers != null) && (dependents != null) &&
        (providerPlugs != null) && (dependentPlugs != null)) {
      makeTopLevelPaths(providers, depth);
      makeTopLevelPaths(dependents, depth);
      ESlateMicroworld myMW = getESlateMicroworld();
      ESlateHandle mwHandle = myMW.getESlateHandle();
      int nConnections = providers.length;
      ESlateHandle[] provH = new ESlateHandle[nConnections];
      ESlateHandle[] depH = new ESlateHandle[nConnections];
      for (int i=0; i<nConnections; i++) {
        provH[i] = mwHandle.getChildHandle(providers[i]);
        depH[i] = mwHandle.getChildHandle(dependents[i]);
      }
      boolean error = false;
      StringBuffer info = new StringBuffer();
      Vector[] providerPlugsV;
      Vector[] dependentPlugsV;
      StringBaseArray[] providerPlugsS;
      StringBaseArray[] dependentPlugsS;
      boolean usesVectors;
      if (providerPlugs instanceof Vector[]) {
        providerPlugsV = (Vector [])providerPlugs;
        dependentPlugsV = (Vector [])dependentPlugs;
        providerPlugsS = null;
        dependentPlugsS = null;
        usesVectors = true;
      }else{
        providerPlugsS = (StringBaseArray [])providerPlugs;
        dependentPlugsS = (StringBaseArray [])dependentPlugs;
        providerPlugsV = null;
        dependentPlugsV = null;
        usesVectors = false;
      }
      for (int i=0; i<nConnections; i++) {
        if (usesVectors) {
          myMW.resolvePlugAliases(
            provH, providerPlugsV, ESlateMicroworld.ALIAS_OUTPUT
          );
          myMW.resolvePlugAliases(
            depH, dependentPlugsV, ESlateMicroworld.ALIAS_INPUT
          );
        }else{
          myMW.resolvePlugAliases(
            provH, providerPlugsS, ESlateMicroworld.ALIAS_OUTPUT
          );
          myMW.resolvePlugAliases(
            depH, dependentPlugsS, ESlateMicroworld.ALIAS_INPUT
          );
        }
        // Identify provider plug
        Plug plug1;
        Vector iName1v;
        StringBaseArray iName1s;
        if (usesVectors) {
          iName1v = providerPlugsV[i];
          iName1s = null;
          plug1 = provH[i].getPlugLocaleIndependent(
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
          iName1s = providerPlugsS[i];
          iName1v = null;
          plug1 = provH[i].getPlugLocaleIndependent(
            iName1s.get(iName1s.size()-1)
          );
          if (plug1 != null) {
            int n = iName1s.size() - 2;
            for (int j=n; j>=0; j--) {
              plug1 = plug1.getPlugLocaleIndependent(iName1s.get(j));
              if (plug1 == null) {
                break;
              }
            }
          }
        }
        if (plug1 == null) {
          if (error) {
            info.append('\n');
          }
          error = true;
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
            if (plugName.length() ==  0) {
              plugName.append(s);
            }else{
              plugName.insert(0, '.');
              plugName.insert(0, s);
            }
          }
          info.append(resources.getString("component"));
          info.append(' ');
          info.append(provH[i].getComponentName());
          info.append(' ');
          info.append(resources.getString("dontHavePlug"));
          info.append(' ');
          info.append(plugName.toString());
        }
        // Identify dependent plug
        Plug plug2;
        Vector iName2v;
        StringBaseArray iName2s;
        if (usesVectors) {
          iName2v = dependentPlugsV[i];
          iName2s = null;
          plug2 = depH[i].getPlugLocaleIndependent(
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
          iName2s = dependentPlugsS[i];
          iName2v = null;
          plug2 = depH[i].getPlugLocaleIndependent(
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
          if (error) {
            info.append('\n');
          }
          error = true;
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
              s = (String)(iName2v.elementAt(j));
            }else{
              s = iName2s.get(j);
            }
            if (plugName.length() ==  0) {
              plugName.append(s);
            }else{
              plugName.insert(0, '.');
              plugName.insert(0, s);
            }
          }
          info.append(resources.getString("component"));
          info.append(' ');
          info.append(depH[i].getComponentName());
          info.append(' ');
          info.append(resources.getString("dontHavePlug"));
          info.append(' ');
          info.append(plugName.toString());
        }
        if (plug1 != null && plug2 != null) {
          // Connect the two plugs. If for some reason they are already
          // connected, leave them that way, instead of toggling their
          // connection state.
          if (!plug1.isConnected(plug2)) {
            boolean suppress = ESlateMicroworld.suppressAudio;
            ESlateMicroworld.suppressAudio = true;
            ESlateMicroworld.connectComponent(plug1);
            ESlateMicroworld.connectComponent(plug2);
            ESlateMicroworld.suppressAudio = suppress;
          }
        }
      }
      if (error) {
        throw new IOException(info.toString());
      }
    }
  }

  /**
   * Bypass the renaming locking mechanism for the component's microworld.
   */
  private void enableRenaming()
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    if (myMW != null) {
      boolean status = myMW.isRenamingAllowed();
      // If renaming has not been locked, we don't need to do anything.
      if (!status) {
        restoreRenamingAllowed = true;
        try {
          myMW.internalSetRenamingAllowed(true);
        } catch (IllegalAccessException iae) {
        }
      }
    }
  }

  /**
   * Restore the locking mechanism to its state before
   * <code>enableRenaming()</code> was invoked.
   */
  private void restoreRenaming()
  {
    ESlateMicroworld myMW = getESlateMicroworld();
    // Only restore the locking state (to false), if we had bypassed it.
    if ((myMW != null) && restoreRenamingAllowed) {
      try {
        myMW.internalSetRenamingAllowed(false);
      } catch (IllegalAccessException iae) {
      }
    }
    restoreRenamingAllowed = false;
  }

  /**
   * Add an <code>ActivationHandleGroup</code> to the list of groups of which
   * this handle is a member.
   * @param     g       The group to add.
   */
  void addActivationHandleGroup(ActivationHandleGroup g)
  {
    synchronized (activationGroups) {
      if (!activationGroups.contains(g)) {
        activationGroups.add(g);
      }
    }
  }

  /**
   * Remove an <code>ActivationHandleGroup</code> from the list of groups
   * of which this handle is a member.
   * @param     g       The group to remove.
   */
  void removeActivationHandleGroup(ActivationHandleGroup g)
  {
    synchronized (activationGroups) {
      int i = activationGroups.indexOf(g);
      if (i >= 0) {
        activationGroups.remove(i);
      }
    }
  }

  /**
   * Add an <code>ActivationHandleGroup</code> to the list of groups that are
   * associated with this E-Slate handle
   * @param     g       The group to add.
   */
  void addAssociatedActivationHandleGroup(ActivationHandleGroup g)
  {
    synchronized (associatedActivationGroups) {
      if (!associatedActivationGroups.contains(g)) {
        associatedActivationGroups.add(g);
      }
    }
  }

  /**
   * Remove an <code>ActivationHandleGroup</code> from the list of groups
   * that are associated with this E-Slate handle.
   * @param     g       The group to remove.
   */
  void removeAssociatedActivationHandleGroup(ActivationHandleGroup g)
  {
    synchronized (associatedActivationGroups) {
      int i = associatedActivationGroups.indexOf(g);
      if (i >= 0) {
        associatedActivationGroups.remove(i);
      }
    }
  }

  /**
   * Activates this handle in all the <code>ActivationHandleGroup</code>s of
   * which it is a mamber.
   */
  void activate()
  {
    ActivationHandleGroupBaseArray ahg;
    synchronized (activationGroups) {
      ahg = (ActivationHandleGroupBaseArray)(activationGroups.clone());
    }
    int n = ahg.size();
    for (int i=0; i<n; i++) {
      ahg.get(i).setActiveHandle(this);
    }
  }

  /**
   * Add a handle's hierarchy to the ActivationHandleGroups to which this
   * handle belongs.
   * @param     h       The handle whose hierarchy will be added.
   */
  private void addInActivationGroups(ESlateHandle h)
  {
    ActivationHandleGroupBaseArray ahg;
    synchronized (associatedActivationGroups) {
      ahg =
        (ActivationHandleGroupBaseArray)(associatedActivationGroups.clone());
    }
    int n = ahg.size();
    for (int i=0; i<n; i++) {
      ActivationHandleGroup g = ahg.get(i);
      g.addHandleTree(h);
    }
    ESlateHandle parent = getParentHandle();
    if (parent != null) {
      parent.addInActivationGroups(h);
    }
    ESlateHandle[] hosts = getHostHandles();
    n = hosts.length;
    for (int i=0; i<n; i++) {
      hosts[i].addInActivationGroups(h);
    }
  }

  /**
   * Remove a handle's hierarchy from the ActivationHandleGroups to which this
   * handle belongs, as well as the ActivationHandleGroups of this handle's
   * ancestors in both the E-Slate and the host hierrachy.
   * @param     h       The handle whose hierarchy will be removed.
   */
  private void removeFromActivationGroups(ESlateHandle h)
  {
    if (associatedActivationGroups != null) {
      ActivationHandleGroupBaseArray ahg;
      synchronized (associatedActivationGroups) {
        ahg =
          (ActivationHandleGroupBaseArray)(associatedActivationGroups.clone());
      }
      int n = ahg.size();
      for (int i=0; i<n; i++) {
        ActivationHandleGroup g = ahg.get(i);
        g.removeHandleTree(h);
      }
    }
    ESlateHandle parent = getParentHandle();
    if (parent != null) {
      parent.removeFromActivationGroups(h);
    }
    ESlateHandle[] hosts = getHostHandles();
    int n = hosts.length;
    for (int i=0; i<n; i++) {
      hosts[i].removeFromActivationGroups(h);
    }
  }

  /**
   * Returns the unique number associated with the component, which is used
   * as the name of the component's data sub-folder in the saved microworld
   * file.
   */
  public BigInteger getHandleID()
  {
    return handleId;
  }

//
// Attributes
//

  /**
   * The component's name. It is formed as follows: <classname>_<global_counter>
   * This is the name of a particular instance of a component, e.g., "red
   * clock". This name is unique for each component, and is case insensitive.
   */
  private String myName;

  /**
   * The component's alias.
   */
  private String myAlias;

  /**
   * The plugs attached to the component.
   */
  PlugTreeNode plugs;

  /**
   * The component's menu bar.
   */
  MenuPanel menuPanel;

  /**
   * The component's plug view.
   */
  private ESlateJPopupMenu plugViewPopup;

  /**
   * The component's plug menu.
   */
  private ESlateJMenu plugMenu;

  /**
   * Indicates whether the component's plugview should be a lightweight
   * component.
   */
  private boolean lightWeightPopup = false;

  /**
   * A reference to the ESlateMicroworld object.
   */
  private ESlateMicroworld myESlateMicroworld;

  /**
   * Indicates whether the plug view popup window needs to be rebuilt.
   * (I.e., if plugs have been added to the component after the last
   * time the popup window was built.)
   */
  boolean redoPlugView;

  /**
   * Indicates whether the plug menu needs to be rebuilt.
   * (I.e., if plugs have been added to the component after the last
   * time the popup window was built.)
   */
  boolean redoPlugMenu;

  /**
   * Indicates whether the plug menu (when true) or plug view popup (when
   * false) was last displayed.
   */
  private boolean showingMenu = false;

  /**
   * The "info" button at the top of each component.
   */
  private JButton infoButton;

  /**
   * The "show plugs" button at the top of each component.
   */
  private JButton plugButton;

  /**
   * The "help" button at the top of each component.
   */
  private JButton helpButton;

  /**
   * The "name" label at the top of each component.
   */
  private JLabel nameLabel;

  /**
   * The text field used to input a new name for each component.
   */
  private JTextField nameField;

  /**
   * The margin between the menu bar buttons and the edges of the menu bar.
   */
  private final static int margin = 1;

  /**
   * The menu bar's foreground color.
   */
  private final static Color fgColor = Color.black;

  /**
   * The menu bar's background color.
   */
  private final static Color bgColor = new Color(49, 170, 152);

  /**
   * Indicates whether we are currently renaming the component. Used to avoid
   * putting up a second error message if we lose the focus by displaying an
   * error message.
   */
  private boolean renamingComponent;

  /**
   * Indicates whether the "name" text field is currently being shown.
   */
  private boolean nameFieldShown;

  /**
   * Indicates whether we are currently in the "name" text field's
   * mousePressed listener.
   */
  private boolean inMousePressedListener;

  /**
   * The information associated with the component that owns this handle.
   */
  private ESlateInfo componentInfo;

  /**
   * The localized resources.
   */
  ResourceBundle resources;

  /**
   * The component that owns the handle.
   */
  private Object myComponent;

  /**
   * The listeners for E-Slate events.
   */
  ESlateListenerBaseArray eSlateListeners;

  /**
   * The listeners for microworld change events.
   */
  private MicroworldChangedListenerBaseArray microworldChangeListeners;

  /**
   * A list of all the PlugTree components displaying the plug tree of the
   * associated E-Slate component.
   */
  private ArrayList<PlugTree> treeList;

  /**
   * If the associated component is an applet, this flag indicates that the
   * applet's start() method has been invoked.
   */
  boolean started = false;

  /**
   * Used to synchronize assigning values to the "applet started" flag.
   */
  static Object synchronizeStart = new Object();

  /**
   * Indicates whether the dispose method can be called from within finalize.
   * Used to avoid calling dispose from finalize if the user has invoked
   * dispose explicitly.
   */
  Boolean disposeCalled = Boolean.FALSE;

  /**
   * Indicates whether printing of debugging messages is enabled.
   */
  private boolean debug = false;

  /**
   * The list of Logo primitive groups supported by the component.
   */
  private ArrayList<String> primitiveGroups;

  /**
   * A copy of the bounds of the internal frame of the E-Slate plug view window
   * corresponding to this component. Class PlugViewFrame tracks changes to the
   * bounds of said internal frame, so that they can be stored to and
   * retrieved from saved microworld files.
   */
  Rectangle plugViewFrameBounds = null;

  /**
   * The bounds of the internal frame of the E-Slate plug view window
   * corresponding to this component when the frame was not in the iconified
   * state. This is used to initialize the corresponding variable in the
   * frame when the frame is created.
   */
  Rectangle oldBounds = null;

  /**
   * Indicates whether the tree in the internal frame of the E-Slate plug view
   * window corresponding to this componenent is collapsed.
   */
  boolean isCollapsed = false;

  /**
   * Indicates whether plugViewFrameBounds should be taken into account when
   * adding the internal frame of the E-Slate plug view window corresponding
   * to this component.
   */
  boolean usePlugViewFrameBounds = false;

  /**
   * A copy of the bounds of the iconified internal frame of the E-Slate plug
   * view window corresponding to this component. Class PlugViewFrame tracks
   * changes to the bounds of said iconified internal frame, so that they
   * can be stored to and retrieved from saved microworld files.
   */
  Rectangle iconifiedPlugViewFrameBounds = null;

  /**
   * Indicates whether the internal frame of the E-Slate plug view window
   * corresponding to this component is iconified or not.
   */
  boolean plugViewFrameIconified = false;

  /**
   * The listener for mouse events on the name label of the menu bar.
   */
  private MouseListener mausListener = null;

  ///**
  // * Indicates whether the listener for mouse events on the name label of the
  // * menu bar is currently installed.
  // */
  //private boolean mouseListenerInstalled;

  /**
   * The window where help for the component is shown.
   */
  HelpSystemViewer helpViewer = null;

  /**
   * A unique number associated with the component, used as the name of the
   * component's data sub-folder in the saved microworld file.
   */
  BigInteger handleId = null;

  /**
   * The current directory in the component's data sub-folder in the
   * saved microworld file, relative to the subfolder to which the component
   * can store its data.
   */
  private ArrayList currentDir = null;

  /**
   * A list of the input and output streams that have been opened on sub-files
   * of the component's data sub-folder in the saved microworld file.
   */
  private ArrayList<Object> openStreams = new ArrayList<Object>();

  /**
   * A bean context, used to encapsulate nested E_Slate parts.
   */
  private ESlateBeanContext beanContext;

  /**
   * Mapping of the E-Slate handles of the child components to their names.
   */
  NameServiceContext childComponents;

  /**
   * A list of the E-Slate handles of the components hosting this component.
   */
  private ESlateHandleBaseArray hosts = new ESlateHandleBaseArray();

  /**
   * A list of the E-Slate handles of the hosted components.
   */
  private ESlateHandleBaseArray hostedComponents = new ESlateHandleBaseArray();

  /**
   * The component's icon.
   */
  private Image componentImage = null;

  /**
   * Indicates that the component's image has been obtained from the
   * component's BeanInfo class.
   */
  private boolean haveImage = false;

  /**
   * Indicates that the component contains Java beans produced by BeanXporter,
   * which require special handling by the container.
   */
  boolean beanXporter = false;

  /**
   * Specifies whether the handle will be visible in the component's plug
   * menu and in the plug editor.
   */
  private boolean visible = true;

  /**
   * The separator in component path names.
   */
  final static char SEPARATOR = '.';

  /**
   * Version of field map containing child state information.
   */
  private final static int CHILD_STATE_VERSION = 1;
  /**
   * Field map key for children information.
   */
  private final static String DATA = "data";
  /**
   * Field map key for child state stream data.
   */
  private final static String STREAM_DATA = "streamData";
  /**
   * Field map key for list of provider component names.
   */
  private final static String PROVIDERS = "providers";
  /**
   * Field map key for list of dependent component names.
   */
  private final static String DEPENDENTS = "dependents";
  /**
   * Field map key for list of provider plug names.
   */
  private final static String PROVIDER_PLUGS = "providerPlugs";
  /**
   * Field map key for list of dependent plug names.
   */
  private final static String DEPENDENT_PLUGS = "dependentPlugs";
  /**
   * Field map key for component's own name.
   */
  private final static String OWN_NAME = "ownName";
  /**
   * Field map key for nesting depth.
   */
  private final static String NESTING_DEPTH = "nestingDepth";

  /**
   * A dummy object, used for synchronization.
   */
  private Object dummyObject = new Object();

  /**
   * Indicates whether we need to restore the state of the renaming locking
   * mechanism of the current microworld after bypassing it.
   */
  private boolean restoreRenamingAllowed = false;

  /**
   * Indicates whether the component's name was restored in
   * <code>restoreChildObjects()</code>, so that we can skip doing so again in
   * <code>restoreSubTreeInfo()</code>.
   */
  private boolean restoredOwnName = false;

  /**
   * For components created via ESlateMicroworld.instantiateComponent(), this
   * variable holds the E-Slate handle of their parent. We have to set the
   * handle's parent at the time the component's handle is constructed,
   * otherwise the component will not be in its correct place in the
   * component hierarchy, and restoring irs state will fail.
   */
  public static ESlateHandle nextParent = null;
  /**
   * Specifies that <code>nextParent</code> has been set via
   * <code>setNextParent()</code>.
   */
  private static boolean nextParentExternallySet = false;
  /**
   * For reasons similar to those for <code>nextParent</code>, we may need to
   * set the component's name at construction time.
   */
  public static String nextName = null;
  /**
   * Indicates that a component is being created from within the
   * <code>ESlateMicroworld.instantiateComponent()</code> method.
   */
  boolean inInstantiateComponent = false;
  /**
   * The <code>ActivationHandleGroup</code>s of which this handle is a member.
   */
  private ActivationHandleGroupBaseArray activationGroups =
    new ActivationHandleGroupBaseArray();
  /**
   * The <code>ActivationHandleGroup</code>s associated with this handle.
   */
  private ActivationHandleGroupBaseArray associatedActivationGroups =
    new ActivationHandleGroupBaseArray();

  /**
   * This class implements the normal "face" of the menu bar that can appear
   * on top of E-Slate components.
   *
   * @author    Petros Kourouniotis
   * @author    Kriton Kyrimis
   * @version   1.6.17, 5-Apr-2001
   */
  public class NameLabelPanel extends JPanel
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    public NameLabelPanel()
    {
      nameLabel = new JLabel();
      nameLabel.setForeground(fgColor);
      Font oldFont = (Font)(UIManager.get("Panel.font"));
      Font newFont = oldFont.deriveFont(10.0f);
      nameLabel.setFont(newFont);
      nameLabel.setText(myName);
      nameLabel.setToolTipText(myName);

      setOpaque(true);
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      this.add(nameLabel);
      this.add(Box.createHorizontalGlue());
    }

    public void paintComponent(Graphics g)
    {
      Component c = this;
      while (!(c instanceof MenuPanel)) {
       c = c.getParent();
      }
      setBackground(c.getBackground());
      super.paintComponent(g);
    }
  }

  /**
   * This class implements the "face" of the menu bar that can appear on top
   * of E-Slate components that allows the user to rename the component.
   *
   * @author    Petros Kourouniotis
   * @author    Kriton Kyrimis
   * @version   1.6.16, 5-Apr-2001
   */
  public class NameFieldPanel extends JPanel
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    public NameFieldPanel()
    {
      nameField = new JTextField();
      Font oldFont = (Font)(UIManager.get("Panel.font"));
      Font newFont = oldFont.deriveFont(10.0f);
      nameField.setFont(newFont);
      nameField.setText(myName);
      nameLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
      nameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

      setOpaque(true);
      setLayout(new BorderLayout());
      this.add(nameField, BorderLayout.CENTER);
    }
  }

  /**
   * This class implements the menu bar that can appear on top of E-Slate
   * components.
   *
   * @author    Petros Kourouniotis
   * @author    Kriton Kyrimis
   * @version   1.7.0, 15-May-2001
   */
  public class MenuPanel extends JPanel
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    /*
     * The cardPanel.
     */
    CardPanel cardPanel = new CardPanel();
    /*
     * The buttonPanel.
     */
    JPanel buttonPanel;

    /**
     * Create the menu bar.
     */
    MenuPanel()
    {
      super();
      try {
        setLayout(new BorderLayout());
      } catch(AWTError e) {
      }
      setForeground(fgColor);
      setBackground(bgColor);
      setOpaque(true);

      Class avClass = gr.cti.eslate.base.ESlateHandle.class;

      mausListener = new MouseListener() {
        public void mousePressed(MouseEvent e)
        {
          if (e.isControlDown() &&
              javax.swing.SwingUtilities.isLeftMouseButton(e)) {
            ESlateMicroworld myMW = getESlateMicroworld();
            boolean mwRenamingAllowed =
              (myMW == null) || myMW.isRenamingAllowed();
            if (!inMousePressedListener &&
                mwRenamingAllowed && renamingAllowed) {
              inMousePressedListener = true;
              setRenameMenuBar();
              nameFieldShown = true;
              // Set the caret position properly in the JTextField.
              int caretPos = nameField.viewToModel(e.getPoint());
              if (caretPos >= 0) {
                nameField.setCaretPosition(caretPos);
              }else{
                nameField.setCaretPosition(0);
              }
            }
          }
        }
        public void mouseClicked(MouseEvent e)
        {
        }
        public void mouseEntered(MouseEvent e)
        {
        }
        public void mouseExited(MouseEvent e)
        {
        }
        public void mouseReleased(MouseEvent e)
        {
        }
      };

      cardPanel.add(new NameLabelPanel(), "label");
      cardPanel.add(new NameFieldPanel(), "field");

      //if (renamingAllowed) {
        nameLabel.addMouseListener(mausListener);
        //mouseListenerInstalled = true;
      //}

      nameField.setAlignmentY(JTextField.CENTER_ALIGNMENT);
      nameField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

      nameField.setCaretPosition(0);
      nameField.setScrollOffset(0);
      nameField.setCaretColor(bgColor);
      nameField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          renamingComponent = true;
          try {
            changeComponentName(nameField.getText());
          } catch (NameUsedException nue) {
            ESlateOptionPane.showMessageDialog(
              getDialogParent(nameField), nue.getMessage(),
              resources.getString("error"), JOptionPane.ERROR_MESSAGE
            );
          } catch (RenamingForbiddenException rfe) {
            ESlateOptionPane.showMessageDialog(
              getDialogParent(nameField), rfe.getMessage(),
              resources.getString("error"), JOptionPane.ERROR_MESSAGE
            );
          } catch (IllegalArgumentException iae) {
            ESlateOptionPane.showMessageDialog(
              getDialogParent(nameField), iae.getMessage(),
              resources.getString("error"), JOptionPane.ERROR_MESSAGE
            );
          }
          renamingComponent = false;
          setNormalMenuBar();
          inMousePressedListener = false;
          nameFieldShown = false;
        }
      });

      nameField.addFocusListener(new FocusListener() {
        public void focusGained(FocusEvent e)
        {
        }
        public void focusLost(FocusEvent e) {
          if (!renamingComponent && nameFieldShown) {
            // If we pop up an error message when renaming the component, we
            // lose the focus and the listener is called, resulting in a
            // second error message popping up.  We use the renamingComponent
            // variable to avoid this. We also ignore focus lost events when the
            // name field is not shown.
            renamingComponent = true;
            try {
              changeComponentName(nameField.getText());
            } catch (NameUsedException nue) {
              ESlateOptionPane.showMessageDialog(
                getDialogParent(nameField), nue.getMessage(),
                resources.getString("error"), JOptionPane.ERROR_MESSAGE
              );
            } catch (IllegalArgumentException iae) {
              ESlateOptionPane.showMessageDialog(
                getDialogParent(nameField), iae.getMessage(),
                resources.getString("error"), JOptionPane.ERROR_MESSAGE
              );
            } catch (RenamingForbiddenException rfe) {
            }
            renamingComponent = false;
            setNormalMenuBar();
            inMousePressedListener = false;
            nameFieldShown = false;
          }
        }
      });

      ImageIcon plug1 = new ImageIcon(avClass.getResource("plug1.gif"));
      ImageIcon plug2 = new ImageIcon(avClass.getResource("plug2.gif"));
      plugButton = new JButton(plug1);
      plugButton.setPressedIcon(plug2);
      plugButton.setToolTipText(resources.getString("showPlugs"));
      plugButton.setBorder(BorderFactory.createEmptyBorder(margin, 0, margin, 0));
      plugButton.setBorderPainted(false);
      plugButton.setFocusPainted(false);
      plugButton.setRequestFocusEnabled(false);
      plugButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
      plugButton.setAlignmentX(JButton.RIGHT_ALIGNMENT);
      plugButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showPlugViewPopup();
        }
      });
      plugButton.addMouseListener(new MouseListener() {
        private boolean pressed = false;
        public void mousePressed(MouseEvent e)
        {
          // Check for popup menu mouse button. Since isPopupTrigger does not
          // appear to be working under Windows, we also check explicitly for
          // the right button.
          if (e.isPopupTrigger() ||
              (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
            if (plugViewWindowEnabled) {
              pressed = true;
            }
          }
        }
        public void mouseClicked(MouseEvent e)
        {
        }
        public void mouseEntered(MouseEvent e)
        {
        }
        public void mouseExited(MouseEvent e)
        {
        }
        public void mouseReleased(MouseEvent e)
        {
          if (pressed) {
            pressed = false;
            ESlateMicroworld myMW = getESlateMicroworld();
            if (myMW != null) {
              myMW.showPlugViewWindow();
            }
          }
        }
      });

      ImageIcon help1 = new ImageIcon(avClass.getResource("help1.gif"));
      ImageIcon help2 = new ImageIcon(avClass.getResource("help2.gif"));
      helpButton = new JButton(help1);
      helpButton.setPressedIcon(help2);
      helpButton.setToolTipText(resources.getString("help"));
      helpButton.setBorder(
        BorderFactory.createEmptyBorder(margin, 0, margin, 0));
      helpButton.setBorderPainted(false);
      helpButton.setFocusPainted(false);
      helpButton.setRequestFocusEnabled(false);
      helpButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
      helpButton.setAlignmentX(JButton.RIGHT_ALIGNMENT);
      helpButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showHelpWindow();
        }
      });

      ImageIcon info1 = new ImageIcon(avClass.getResource("info1.gif"));
      ImageIcon info2 = new ImageIcon(avClass.getResource("info2.gif"));
      infoButton = new JButton(info1);
      infoButton.setPressedIcon(info2);
      infoButton.setToolTipText(resources.getString("about"));
      infoButton.setBorder(
        BorderFactory.createEmptyBorder(margin, 0, margin, margin));
      infoButton.setBorderPainted(false);
      infoButton.setFocusPainted(false);
      infoButton.setRequestFocusEnabled(false);
      infoButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
      infoButton.setAlignmentX(JButton.RIGHT_ALIGNMENT);
      infoButton.addActionListener(new ActionListener() {
        private Boolean active = Boolean.FALSE;

        public void actionPerformed(ActionEvent e) {
          // Prevent double clicks on the button from running this code a second
          // time.
          synchronized(active) {
            if (!active.booleanValue()) {
              active = Boolean.TRUE;
            }else{
              return;
            }
          }
          showInfoDialog();
          active = Boolean.FALSE;
        }
      });

      buttonPanel = new JPanel(true);
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
      buttonPanel.setOpaque(false);
      buttonPanel.add(plugButton);
      buttonPanel.add(helpButton);
      buttonPanel.add(infoButton);
      buttonPanel.add(Box.createHorizontalStrut(margin));

      this.add(cardPanel, BorderLayout.CENTER);
      this.add(buttonPanel, BorderLayout.EAST);

      setBorder(BorderFactory.createRaisedBevelBorder());

      // Limit the height of the menu panel to its "natural" height.
      Dimension sizeLimit = new Dimension(
        getPreferredSize().width, getMinimumSize().height);
      setPreferredSize(sizeLimit);
      sizeLimit = new Dimension(
        getMaximumSize().width, getMinimumSize().height);
      setMaximumSize(sizeLimit);
    }

    /**
     * Specifies whether clicking on the component name in the menu bar
     * allows renaming the component.
     * @param   status  True if yes, false otherwise.
     */
    public void setRenamingAllowed(boolean status)
    {
      renamingAllowed = status;
    }

    /**
     * Checks whether clicking on the component name in the menu bar
     * allows renaming the component.
     * @return  True if yes, false otherwise.
     */
    public boolean isRenamingAllowed()
    {
      return renamingAllowed;
    }

    /**
     * Specifies whether clicking with the right button on the plug button in
     * the menu bar will cause the plug view window to appear.
     * @param   status  True if yes, false if no.
     */
    public void setPlugViewWindowEnabled(boolean status)
    {
      plugViewWindowEnabled = status;
      if (!plugViewWindowEnabled) {
        ESlateMicroworld myMW = getESlateMicroworld();
        if (myMW != null) {
          myMW.hidePlugViewWindow();
        }
      }
    }

    /**
     * Checks whether clicking with the right button on the plug button in
     * the menu bar will cause the plug view window to appear.
     * @return  True if yes, false if no.
     */
    public boolean isPlugViewWindowEnabled()
    {
      return plugViewWindowEnabled;
    }

    /**
     * Specifies whether the plug menu button is visible.
     * @param   status  True if yes, false otherwise.
     */
    public void setPlugButtonVisible(boolean status)
    {
      boolean old_status = plugButtonVisible;
      plugButtonVisible = status;
      // Rebuild the button panel
      if (old_status != status) {
        rebuildButtonPanel();
      }
    }

    /**
     * Checks whether the plug menu button is visible.
     * @return  True if yes, false otherwise.
     */
    public boolean isPlugButtonVisible()
    {
      return plugButtonVisible;
    }

    /**
     * Specifies whether the help button is visible.
     * @param   status  True if yes, false otherwise.
     */
    public void setHelpButtonVisible(boolean status)
    {
      boolean old_status = helpButtonVisible;
      helpButtonVisible = status;
      // Rebuild the button panel
      if (old_status != status) {
        rebuildButtonPanel();
      }
    }

    /**
     * Checks whether the help button is visible.
     * @return  True if yes, false otherwise.
     */
    public boolean isHelpButtonVisible()
    {
      return helpButtonVisible;
    }

    /**
     * Specifies whether the information button is visible.
     * @param   status  True if yes, false otherwise.
     */
    public void setInfoButtonVisible(boolean status)
    {
      boolean old_status = infoButtonVisible;
      infoButtonVisible = status;
      // Rebuild the button panel
      if (old_status != status) {
        rebuildButtonPanel();
      }
    }

    /**
     * Checks whether the information button is visible.
     * @return  True if yes, false otherwise.
     */
    public boolean isInfoButtonVisible()
    {
      return infoButtonVisible;
    }

    /**
     * Rebuilds the menu panel's button panel, to reflect the new visibility
     * status of its buttons.
     */
    private void rebuildButtonPanel()
    {
      if (menuPanel != null) {
        JPanel bp = menuPanel.buttonPanel;
        bp.removeAll();
        if (plugButtonVisible) {
          bp.add(plugButton);
        }
        if (helpButtonVisible) {
          bp.add(helpButton);
        }
        if (infoButtonVisible) {
          bp.add(infoButton);
        }
      }
    }

    /**
     * Returns a reference to the label displaying the component's name.
     * <EM>Use with caution!</EM> This may be null if the component' smenu
     * pane has not been created.
     * @return  The requested reference.
     */
    public JLabel getNameLabel()
    {
      return nameLabel;
    }

    /**
     * Returns a reference to the plug menu button. <EM>Use with caution!</EM>
     * @return  The requested reference. This may be null if the component'
     * smenu pane has not been created.
     */
    public JButton getPlugButton()
    {
      return plugButton;
    }

    /**
     * Returns a reference to the help button. <EM>Use with caution!</EM>
     * @return  The requested reference. This may be null if the component'
     * smenu pane has not been created.
     */
    public JButton getHelpButton()
    {
      return helpButton;
    }

    /**
     * Returns a reference to the information button. <EM>Use with caution!</EM>
     * @return  The requested reference. This may be null if the component'
     * smenu pane has not been created.
     */
    public JButton getInfoButton()
    {
      return infoButton;
    }

    public void updateUI()
    {
      super.updateUI();
      if (initted && (menuPanel != null)) {
        Font oldFont = (Font)(UIManager.get("Panel.font"));
        Font newFont = oldFont.deriveFont(10.0f);
        nameField.setFont(newFont);
        nameLabel.setFont(newFont);
      }
    }

    /**
     * Indicates whether renaming the component from the menu bar is allowed.
     */
    private boolean renamingAllowed = true;
    /**
     * Indicates whether the plug menu button is visible
     */
    private boolean plugButtonVisible = true;
    /**
     * Indicates whether the help button is visible
     */
    private boolean helpButtonVisible = true;
    /**
     * Indicates whether the information button is visible
     */
    private boolean infoButtonVisible = true;
    /**
     * Specifies whether clicking with the right button on the plug button in
     * the menu bar will cause the plug view window to appear.
     */
    private boolean plugViewWindowEnabled = true;
    /**
     * Indicates that the menu panel's constructor has finished executing.
     */
    private boolean initted = true;
  }

  /**
   * This class implements the functionality for connecting plugs from within a
   * separate thread. This is done so that swing can restore the area behind
   * the plug menus immediately: when using lightweight menus, the area is not
   * updated until the menu action is completely performed.
   *
   * @author    Kriton Kyrimis
   * @version   1.7.21, 10-May-2002
   */
  public class ConnectThread extends Thread
  {
    Plug p;
    JComponent top;

    /**
     * Construct a thread.
     * @param   p       The plug to connect.
     * @param   top     The topmost JComponent in which appeared the popup
     *                  menu containing the plug.
     */
    ConnectThread(Plug p, JComponent top)
    {
      super();
      this.p = p;
      this.top = top;
    }

    /**
     * Perform the connection.
     */
    public void run()
    {
/*
      // We need this call to sleep() to schedule ourselves out, allowing
      // the dispatcher thread to repaint the area behind the menu. If we
      // don't do this, then the dispatcher will happily execute our code
      // before doing the repaint!
      try {
        // What is the best value here? 
        sleep(99);
      } catch (InterruptedException e) {
      }
*/
      // If the reasoning in the commented out code above is correct, then the
      // minimum time we need to wait is the time required for the dispatcher
      // to finish all its work, i.e., the time required for the AWT event
      // queue to empty. This is much better, as we don't have to rely on
      // "magic" numbers that are CPU-dependent.
      AWTEvent ev;
      EventQueue q = Toolkit.getDefaultToolkit().getSystemEventQueue();
      do {
        ev = q.peekEvent();
        if (ev != null) {
          try {
            sleep(1);
          } catch (InterruptedException e) {
          }
        }
      } while (ev != null);

      // One way or another, we can now perform the connection.
      Runnable r = new ConnectRunnable();
      SwingUtilities.invokeLater(r);
    }

    /**
     * This class implements the Runnable used as an argument to
     * SwingUtilities.invokeLater in the body of the ConnectThread class.
     *
     * @author  Kriton Kyrimis
     * @version 1.6.5, 4-Sep-2000
     */
    public class ConnectRunnable implements Runnable
    {
      /**
       * Do the actual connection.
       */
      public void run()
      {
        ESlateMicroworld.connectComponent(p, getWarningComponent());
      }
    }
  }
}
