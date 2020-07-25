package gr.cti.eslate.base;

import com.ibm.bsf.*;
import org.mozilla.javascript.*;

/**
 * This class encapsulates the JavaScript scripting functionality available to
 * components. Components obtain references to a JavaScript manager by
 * invoking the <code>getJavaScriptManager</code> method of class
 * <code>ScriptManager</code>.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.base.ScriptManager
 */
public class JavaScriptManager
{
  /**
   * The unique instance of JavaScriptManager.
   */
  private static JavaScriptManager javaScriptManager = null;
  /**
   * The Bean Scripting Framework manager used by the JavaScript manager.
   */
  ESlateBSFManager bsfManager = null;

  /**
   * Construct a JavaScript manager. The constructor is private.
   * Instances of JavaScriptManager are obtained by invoking the
   * </code>getJavaScriptManager</code> method.
   */
  private JavaScriptManager()
  {
    super();
    bsfManager = new ESlateBSFManager();

    // Import all the packages in the javax and gr trees, so that they
    // are available to scripts.
    NativeJavaPackage javax = new NativeJavaPackage("javax");
    registerName("javax", javax);
    NativeJavaPackage gr = new NativeJavaPackage("gr");
    registerName("gr", gr);
  }

  /**
   * Executes a JavaScript script.
   * @param     script  The JavaScript to execute.
   * @return    The result of the execution of the script. Void javascript
   *            expressions return instances of class
   *            org.mozilla.javascript.Undefined.
   * @exception Exception       Thrown if the execution of the script fails.
   */
  public Object evaluateJavaScript(String script) throws Exception
  {
    Object result = bsfManager.eval("javascript", "SCRIPT", 0, 0, script);
    return result;
  }

  /**
   * Obtain a reference to a JavaScript manager instance. Only one such
   * instance is active at any time.
   * @return    A reference to the uinique instance of JavaScriptManager.
   */
  static JavaScriptManager getJavaScriptManager()
  {
    if (javaScriptManager == null) {
      javaScriptManager = new JavaScriptManager();
    }
    return javaScriptManager;
  }

  /**
   * Bind a variable name to an object, so that the variable may be used
   * during scripting operations to refer to the object. Components should
   * invoke the <code>registerName</code> method of class
   * <code>ScriptManager</code> instead of this method.
   * @param     name    The name to bind to the object.
   * @param     obj     The object to be bound to the name.
   */
  void registerName(String name, Object obj)
  {
    try {
      bsfManager.declareBean(name , obj, obj.getClass());
    } catch (BSFException e) {
      System.err.println("Failed to register " + name + " for JavaScript");
    }
  }

  /**
   * Remove a name from the scripting variable name space, so that it no
   * longer refer to any objects.  Components should
   * invoke the <code>unregisterName</code> method of class
   * <code>ScriptManager</code> instead of this method.
   * @param     name    The name to remove from the variable name space.
   */
  void unregisterName(String name)
  {
    try {
      bsfManager.undeclareBean(name);
    } catch (BSFException e) {
      System.err.println("Failed to unregister " + name + " for JavaScript");
    }
  }

  /**
   * Removes all names from the scripting variable name space, so that they
   * no longer refer to any objects. Components should invoke the
   * <code>clearRegisteredNames</code> method of class <code>ScriptManager</code>
   * instead of this method.
   */
  void clearRegisteredNames()
  {
    try {
      bsfManager.undeclareAllBeans();
    } catch (BSFException e) {
      System.err.println("Failed to unregister all beans: " + e.getMessage());
    }
  }

  /**
   * Returns the object that is bound to a scripting variable.
   * @param     name    The name of the scripting variable.
   * @return    The requested object. If no object is bound to the given name,
   *            this method returns null.
   */
  Object lookup(String name)
  {
    return bsfManager.lookupBean(name);
  }

  /**
   * Release resources allocated by the JavaScript manager. After invoking
   * this method, any references to the JavaScript manager are invalid, and a
   * fresh copy should be obtained by invoking
   * <code>getJavaScriptManager()</code>.
   */
  void dispose()
  {
    bsfManager.terminate();
    bsfManager = null;
    javaScriptManager = null;
  }
}
