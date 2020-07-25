package gr.cti.eslate.base;

import java.util.*;

/**
 * This class encapsulates the scripting functionality available to
 * components. Components obtain a reference to the script manager by invoking
 * the <code>getScriptManager</code> method, and then invoking the script
 * manager's <code>getXXXManager</code> to get a reference to a manager
 * implementing scripting for the XXX language (currently only JavaScript is
 * supported).
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.base.JavaScriptManager
 * @see         gr.cti.eslate.base.JavaScriptManagerAdapter
 * @see         gr.cti.eslate.base.JavaScriptManagerEvent
 * @see         gr.cti.eslate.base.JavaScriptManagerListener
 */
public class ScriptManager
{
  /**
   * Indicates whether the script manager has been activated. In particular,
   * if <code>active</code> is true, then the <code>scriptManager</code>
   * attribute should be non-null.
   */
  static boolean active = false;
  /**
   * The unique instance of ScriptManager.
   */
  static ScriptManager scriptManager = null;
  /**
   * The instance of the JavaScript manager that this script manager uses.
   */
  JavaScriptManager javaScriptManager = null;
  /**
   * The list of JavaScript manager event listeners.
   */
  private ArrayList<JavaScriptManagerListener> jsListeners =
    new ArrayList<JavaScriptManagerListener>();
  /**
   * The registry containing a copy of all registered names.
   */
  private Hashtable<String, Object> registry = new Hashtable<String, Object>();

  /**
   * Construct an instance of the script manager. The constructor is private.
   * Components that want to use a script manager should invoke the
   * <code>getScriptManager</code> method.
   */
  private ScriptManager()
  {
    super();
  }

  /**
   * Obtain a reference to a script manager instance. Only one such instance
   * is active at any time.
   * @return    A reference to the unique instance of ScriptManager.
   */
  public static ScriptManager getScriptManager()
  {
    if (scriptManager == null) {
      scriptManager = new ScriptManager();
    }
    return scriptManager;
  }

  /**
   * Deactivates the script manager to minimize the resources used.
   * After invoking this method, all references obtained by previous
   * invocations of <code>getScriptManager()</code> and
   * <code>getJavaScriptManager</code> are invalid, and all registered
   * variables will be unregistered. The manager can be
   * reactivated by invoking <code>getScriptManager()</code> and
   * <code>getJavaScriptManager</code> again, to create new instances of these
   * managers.
   */
  public static void deactivateScriptManager()
  {
    if (scriptManager != null) {
      scriptManager.clearRegisteredNames();
      if (scriptManager.javaScriptManager != null) {
        scriptManager.javaScriptManager.dispose();
        scriptManager.javaScriptManager = null;
        scriptManager.jsManagerTerminated();
        active = false;
      }
      scriptManager = null;
    }
  }

  /**
   * Get a reference to the manager implementing the JavaScript scripting
   * functionality.
   * @param     h       The E-Slate handle of the component that wants to use
   *                    JavaScript scripting.
   */
  public JavaScriptManager getJavaScriptManager(ESlateHandle h)
  {
    if (javaScriptManager == null) {
      javaScriptManager = JavaScriptManager.getJavaScriptManager();
      Set<Map.Entry<String, Object>> s = registry.entrySet();
      int n = s.size();
      Iterator<Map.Entry<String, Object>> it = s.iterator();
      for (int i=0; i<n; i++) {
        Map.Entry<String, Object> e = it.next();
        String name = e.getKey();
        Object value = e.getValue();
        javaScriptManager.registerName(name, value);
      }
      active = true;
      jsManagerActivated();
      // Don't use getESlateMicroworld(), as this method is called by the
      // container, for which getESlateMicroworld() returns null.
      ESlateMicroworld mw = h.getEnvironmentMicroworld();
      if (mw != null) {
        mw.registerComponentsForScripting();
      }
    }
    return javaScriptManager;
  }

  /**
   * Bind a variable name to an object, so that the variable may be used
   * during scripting operations to refer to the object.
   * @param     name    The name to bind to the object.
   * @param     obj     The object to be bound to the name.
   */
  public void registerName(String name, Object obj)
  {
    registry.put(name, obj);
    if (active) {
      javaScriptManager.registerName(name , obj);
    }
  }

  /**
   * Remove a name from the scripting variable name space, so that it no
   * longer refers to any object.
   * @param     name    The name to remove from the variable name space.
   */
  public void unregisterName(String name)
  {
    registry.remove(name);
    if (active) {
      javaScriptManager.unregisterName(name);
    }
  }

  /**
   * Removes all names from the scripting variable name space, so that they
   * no longer refer to any objects.
   */
  public void clearRegisteredNames()
  {
    registry.clear();
    if (active) {
      javaScriptManager.clearRegisteredNames();
    }
  }

  /**
   * Returns the object that is bound to a scripting variable.
   * @param     name    The name of the scripting variable.
   * @return    The requested object. If no object is bound to the given name,
   *            this method returns null.
   */
  public Object lookup(String name)
  {
    return registry.get(name);
  }

  /**
   * Checks whether the JavaScript Manager returned by
   * <code>getJavaScriptManager</code> can be used. The JavaScript manager
   * remains active only as long as the handles that were provided as
   * arguments to <code>getJavaScriptManager</code> are active. When the last
   * of these handles is disposed, the JavaScript Manager is terminated and
   * can no longer be used.
   * @return    True if yes, false if no.
   */
  public boolean isJavaScriptManagerActive()
  {
    return (javaScriptManager != null);
  }

  /**
   * Add a JavaScript manager event listener.
   * @param     l       The listener to add.
   */
  public void addJavaScriptManagerListener(JavaScriptManagerListener l)
  {
    synchronized (jsListeners) {
      if (!jsListeners.contains(l)) {
        jsListeners.add(l);
      }
    }
  }
  
  /**
   * Remove a JavaScript manager event listener.
   * @param     l       The listener to add.
   */
  public void removeJavaScriptManagerListener(JavaScriptManagerListener l)
  {
    synchronized (jsListeners) {
      int i = jsListeners.indexOf(l);
      if (i >= 0) {
        jsListeners.remove(i);
      }
    }
  }

  /**
   * Notify JavaScript manager event listeners that the JavaScript manager
   * was activated.
   */
  @SuppressWarnings(value={"unchecked"})
  private void jsManagerActivated()
  {
    ArrayList<JavaScriptManagerListener> listeners =
      (ArrayList<JavaScriptManagerListener>)(jsListeners.clone());
    int n = listeners.size();
    JavaScriptManagerEvent e = new JavaScriptManagerEvent(this);
    for (int i=0; i<n; i++) {
      JavaScriptManagerListener l = listeners.get(i);
      l.managerActivated(e);
    }
  }

  /**
   * Notify JavaScript manager event listeners that the JavaScript manager
   * was terminated.
   */
  @SuppressWarnings(value={"unchecked"})
  private void jsManagerTerminated()
  {
    ArrayList<JavaScriptManagerListener> listeners =
      (ArrayList<JavaScriptManagerListener>)(jsListeners.clone());
    int n = listeners.size();
    JavaScriptManagerEvent e = new JavaScriptManagerEvent(this);
    for (int i=0; i<n; i++) {
      JavaScriptManagerListener l = listeners.get(i);
      l.managerTerminated(e);
    }
  }
}
