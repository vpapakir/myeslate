package gr.cti.eslate.base.sharedObject;

import java.util.*;

/**
 * Event triggered when a shared object is modified.
 *
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
@SuppressWarnings("unchecked")
public class SharedObjectEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private SharedObject sharedObject;
  private Object result;
  private Vector path = new Vector();
   /**
    * For shared objects with many attributes, this attribute can be used to
    * specify which attribute of the shared object has actually changed.
    */
  public int type;

  /**
   * Construct the event.
   * @param     so      A reference to the shared object whose change this
   *                    event signifies. The source of the event will be
   *                    so.getHandle().getComponent().
   * @param     newPath Not used.
   * @deprecated        As of E-Slate 1.7.8, the <code>newPath</code>
   *                    argument is ignored, as the notion of paths for shared
   *                    object events has been removed.
   */
  public SharedObjectEvent(SharedObject so, Vector newPath)
  {
    this(so, 0);
  }

  /**
   * Construct the event.
   *                    This form of the constructor should be used by
   *                    components that have modified a shared attribute
   *                    themselves, and by components that propagate changes
   *                    from the input part of an input/output plug to its
   *                    output art, and do not require checking for loops
   *                    resulting from the input part of a component's
   *                    input/output  plug being connected, directly or
   *                    indirectly, to the plug's output part.
   * @param     so      A reference to the shared object whose change this
   *                    event signifies. The source of the event will be
   *                    so.getHandle().getComponent().
   */
  public SharedObjectEvent(SharedObject so)
  {
    this(so, 0);
  }

  /**
   * Construct the event.
   * @param     so      A reference to the shared object whose change this
   *                    event signifies. The source of the event will be
   *                    so.getHandle().getComponent().
   * @param     newType Specifies which of the shared object's attributes
   *                    has actually changed.
   * @param     newPath Not used.
   * @deprecated        As of E-Slate 1.7.8, the <code>newPath</code>
   *                    argument is ignored, as the notion of paths for shared
   *                    object events has been removed.
   */
  public SharedObjectEvent(SharedObject so, int newType, Vector newPath)
  {
    this(so, newType);
  }

  /**
   * Construct the event.
   *                    This form of the constructor should be used by
   *                    components that have modified a shared attribute
   *                    themselves, and by components that propagate changes
   *                    from the input part of an input/output plug to its
   *                    output art, and do not require checking for loops
   *                    resulting from the input part of a component's
   *                    input/output  plug being connected, directly or
   *                    indirectly, to the plug's output part.
   * @param     so      A reference to the shared object whose change this
   *                    event signifies. The source of the event will be
   *                    so.getHandle().getComponent().
   * @param     newType Specifies which of the shared object's attributes
   *                    has actually changed.
   */
  public SharedObjectEvent(SharedObject so, int newType)
  {
    super(so.getHandle().getComponent());
    sharedObject = so;
    type = newType;
    result = null;
  }

  /**
   * Sets a result associated with an operation on the shared object.
   * Can be used, in conjunction with <A HREF="#getResult()">getResult()</A>
   * to define methods in the shared object that return results.
   * @param     res     The result to be set.
   */
  public void setResult(Object res)
  {
    result = res;
  }

  /**
   * Returns a result associated with an operation on the shared object.
   * Can be used, in conjunction with <A HREF="#setResult(java.lang.Object)">
   * setResult()</A> to define methods in the shared object that return
   * results.
   * @return    The result.
   */
  public Object getResult()
  {
    return result;
  }

  /**
   * Returns the shared object whose change the event signifies.
   */
  public SharedObject getSharedObject()
  {
    return sharedObject;
  }

  /**
   * Returns an empty vector.
   * @deprecated        As of E-Slate 1.7.8, the notion of paths for shared
   *                    object events has been removed. For compatibility with
   *                    existing components, this method has been left as a
   *                    stub.
   */
  public Vector getPath()
  {
    return path;
  }
  /*--------------------------------------------------------------------------*/
  /**
   * Construct the event.
   * @param     source  Source of the event. Should be set to the component
   *                    that triggered the event.
   * @param     so      A reference to the shared object whose change this
   *                    event signifies.
   * @param     newPath Not used.
   * @deprecated        As of E-Slate 1.5.18, the <code>source</code>
   *                    argument is no longer required, as it can be obtained
   *                    from the <code>so</code> argument.
   */
  public SharedObjectEvent(Object source, SharedObject so, Vector newPath)
  {
    this(/*source,*/ so, 0);
  }

  /**
   * Construct the event.
   * @param     source  Source of the event. Should be set to the component
   *                    that triggered the event.
   *                    This form of the constructor should be used by
   *                    components that have modified a shared attribute
   *                    themselves, and by components that propagate changes
   *                    from the input part of an input/output plug to its
   *                    output art, and do not require checking for loops
   *                    resulting from the input part of a component's
   *                    input/output  plug being connected, directly or
   *                    indirectly, to the plug's output part.
   * @param     so      A reference to the shared object whose change this
   *                    event signifies.
   * @deprecated        As of E-Slate 1.5.18, the <code>source</code>
   *                    argument is no longer required, as it can be obtained
   *                    from the <code>so</code> argument.
   */
  public SharedObjectEvent(Object source, SharedObject so)
  {
    this(/*source,*/ so, 0);
  }

  /**
   * Construct the event.
   * @param     source  Source of the event. Should be set to the component
   *                    that triggered the event.
   * @param     so      A reference to the shared object whose change this
   *                    event signifies.
   * @param     newType Specifies which of the shared object's attributes
   *                    has actually changed.
   * @param     newPath Not used.
   * @deprecated        As of E-Slate 1.5.18, the <code>source</code>
   *                    argument is no longer required, as it can be obtained
   *                    from the <code>so</code> argument.
   */
  public SharedObjectEvent(Object source, SharedObject so, int newType,
                           Vector newPath)
  {
    this(/*source,*/ so, newType);
  }

  /**
   * Construct the event.
   * @param     source  Source of the event. Should be set to the component
   *                    that triggered the event.
   *                    This form of the constructor should be used by
   *                    components that have modified a shared attribute
   *                    themselves, and by components that propagate changes
   *                    from the input part of an input/output plug to its
   *                    output art, and do not require checking for loops
   *                    resulting from the input part of a component's
   *                    input/output  plug being connected, directly or
   *                    indirectly, to the plug's output part.
   * @param     so      A reference to the shared object whose change this
   *                    event signifies.
   * @param     newType Specifies which of the shared object's attributes
   *                    has actually changed.
   * @deprecated        As of E-Slate 1.5.18, the <code>source</code>
   *                    argument is no longer required, as it can be obtained
   *                    from the <code>so</code> argument.
   */
  public SharedObjectEvent(Object source, SharedObject so, int newType)
  {
    //super(source);
    super(so.getHandle().getComponent());
    sharedObject = so;
    type = newType;
    result = null;
  }
}
