package gr.cti.eslate.services.name;

import java.math.*;
import java.util.*;
import java.io.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.services.name.event.*;

/**
 * This class implements a name service context, where names can be associated
 * with arbitrary objects. Contexts can be nested (by associating a context
 * with a name), and names can refer to this nesting using delimiter-separated
 * paths (the default delimiter is '.').
 * <P>
 * E.g., If the top-level context contains a context named "dir" and this
 * second context contains an object named "item", the name of the object,
 * relative to the top-level context is "dir.item".
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public class NameServiceContext
  implements Serializable, Externalizable
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private HashMap<String, Object> hashtable;
  private boolean caseSensitive;
  private ResourceBundle resources;
  private Locale locale;
  private ArrayList<NameServiceListener> nSListeners;
  private final static int NAMEBOUND = 0;
  private final static int NAMEUNBOUND = 1;
  private final static int NAMEREBOUND = 2;
  private final static int NAMECHANGED = 3;
  private String separator;
  private NameServiceContext parentContext = null;
  private NameServiceContext pathContext = null;
  private String pathName = null;
  private Object boundObject = null;

  /**
   * Creates a new name service context.
   * @param     caseSensitive   Indicates whether managed names are treated in
   *                            a case sensitive manner or not.
   */
  public NameServiceContext(boolean caseSensitive)
  {
    hashtable = new HashMap<String, Object>();
    this.caseSensitive = caseSensitive;
    locale =  ESlateMicroworld.getCurrentLocale();
    nSListeners = new ArrayList<NameServiceListener>();
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.services.name.NameServiceResource", locale
    );
    separator = ".";
  }

  /**
   * Creates a new name service context. The managed names are treated in a
   * case insensitive manner.
   */
  public NameServiceContext()
  {
    this(false);
  }

  /**
   * Checks whether the name service context is case sensitive or not.
   * @return    True if yes, false if no.
   */
  public boolean isCaseSensitive()
  {
    return caseSensitive;
  }

  /**
   * Returns the object associated with a given name.
   * @param     name    The name of the object.
   * @return    If the name is bound to a plain object, then that object is
   *            returned. If the name is bound to a name service context
   *            (i.e., it is a sub-context), then the object associated
   *            with the sub-context is returned, rather than the sub-context
   *            itself. If the name is not bound to any object,
   *            <code>null<code> is returned. <EM>Hint:</EM> for normal
   *            operations, this is the method to use.
   * @exception NameServiceException    Thrown if name is null.
   */
  public Object lookup(String name) throws NameServiceException
  {
    Object ob = get(name);
    if (ob instanceof NameServiceContext) {
      return ((NameServiceContext)ob).boundObject;
    }else{
      return ob;
    }
  }

  /**
   * Returns the object associated with a given name.
   * @param     name The name of the object.
   * @return    If the name is bound to a plain object, then that object is
   *            returned. If the name is bound to a name service context
   *            (i.e., it is a sub-context), then that subcontext
   *            is returned. If no object is associated with the given name,
   *            null is returned. <EM>Hint:</EM> for normal operations, use
   *            the <CODE><A HREF="#lookup(java.lang.String)">lookup</A></CODE>
   *            method.
   * @exception NameServiceException    Thrown if name is null.
   */
  public Object get(String name) throws NameServiceException
  {
    if (name == null) {
      throw new NameServiceException(
        resources.getString("noNullName")
      );
    }
    String error = parsePathNoException(name);
    if (error == null) {
      return pathContext.fastLookup(pathName);
    }else{
      return null;
    }
  }

  /**
   * Ensures that a name is bound to a name service context rather than a
   * a plain object.
   * @param     name    The name to check.
   */
  public void changeIntoContext(String name)
  {
    try {
      Object obj = get(name);
      if (obj instanceof NameServiceContext) {
        return;
      }
      synchronized (pathContext.hashtable) {
        pathContext.fastUnbind(pathName);
        pathContext.createSubContext(pathName, obj);
      }
    } catch (NameServiceException e) {
    }
  }

  /**
   * Returns the object associated with a given name without performing any
   * checks and without parsing the name for path components.
   * @param     name    The name of the object.
   * @return    The requested object. If no object is associated with the
   *            given name, this method returns null.
   */
  Object fastLookup(String name)
  {
    synchronized (hashtable) {
      return hashtable.get(makeKey(name));
    }
  }

  /**
   * Associates a name with an object.
   * @param     name    The name to be associated with the object.
   * @param     object  The object.
   * @exception NameServiceException    Thrown if name or object is null,
   *                    or if name is already associated with an object.
   */
  public void bind(String name, Object object)
    throws NameServiceException
  {
    if (name == null) {
      throw new NameServiceException(
        resources.getString("noNullName")
      );
    }
    if (object == null) {
      throw new NameServiceException(
        resources.getString("noNullObject")
      );
    }
    parsePath(name);
    if (pathContext.fastLookup(pathName) != null) {
      throw new NameServiceException(
        resources.getString("nameInUse1") +
        name +
        resources.getString("nameInUse2")
      );
    }
    pathContext.fastBind(pathName, object);
    NameBoundEvent nbe = new NameBoundEvent(pathContext, pathName, object);
    pathContext.fireListeners(NAMEBOUND, nbe);
  }

  /**
   * Creates a name service context with the same properties as this context
   * and associates it with a name, essentially creating a sub-context of this
   * context (or the context specified by the name). The specified name is
   * also associated with a name, so that all the names contained in a context
   * are bound to user-specified object, regardless of whether the names are
   * associated with plain objects or naming contexts.
   * @param     name    The name to be associated with the new context.
   * @param     obj     The object to bind additionally to the name.
   * @return    The created name service context.
   * @exception NameServiceException    Thrown if name is null or if name is
   *                    already associated with an object.
   */
  public NameServiceContext createSubContext(String name, Object obj)
    throws NameServiceException
  {
    NameServiceContext subContext = new NameServiceContext();
    subContext.setSeparator(getSeparator());
    subContext.setParentContext(this);
    bind(name, subContext);
    subContext.boundObject = obj;
    return subContext;
  }

  /**
   * Set the object that the parent context's listObjectsResolved will return
   * for this context.
   * @param     obj     The object to set.
   */
  public void setBoundObject(Object obj)
  {
    boundObject = obj;
  }

  /**
   * Associates a name with an object without performing any checks,
   * without parsing the name for path components, and without invoking any
   * listeners.
   * @param     name    The name to be associated with the object.
   * @param     object  The object.
   */
  void fastBind(String name, Object object)
  {
    synchronized (hashtable) {
      hashtable.put(makeKey(name), object);
    }
  }

  /**
   * Disassociates a name from the object to which it is associated. If the
   * name is not associated to an object, this method does nothing. If the
   * associated object is a name service context instance, that instance is
   * recursively disposed.
   * @param     name    The name to be disassociated.
   * @exception NameServiceException    Thrown if name is null.
   */
  public void unbind(String name) throws NameServiceException
  {
    if (hashtable == null) {
      return;
    }
    if (name == null) {
      Thread.dumpStack();
      throw new NameServiceException(
        resources.getString("noNullName")
      );
    }
    String error = parsePathNoException(name);
    if (error == null) {
      Object oldObj = pathContext.fastLookup(pathName);
      //if (oldObj != null && oldObj instanceof NameServiceContext) {
      //  ((NameServiceContext)oldObj).dispose();
      //}
      pathContext.fastUnbind(name);
      NameUnboundEvent nue =
        new NameUnboundEvent(pathContext, pathName, oldObj);
      pathContext.fireListeners(NAMEUNBOUND, nue);
    }
  }

  /**
   * Disassociates a name from the object to which it is associated. If the
   * name is not associated to an object, this method does nothing.
   * This method does not perform any checks, does not parse the name for path
   * components, does not invoke any listeners and does not dispose of any
   * objects.
   * @param     name    The name to be disassociated.
   */
  void fastUnbind(String name)
  {
    synchronized (hashtable) {
      hashtable.remove(makeKey(name));
    }
  }

  /**
   * Associates a name with an object, overriding any existing association.
   * If the previously associated object was a name service context instance,
   * that instance is recursively disposed.
   * @param     name    The name to be associated with the object.
   * @param     object  The object.
   * @exception NameServiceException    Thrown if name or object is null.
   */
  public void rebind(String name, Object object) throws NameServiceException
  {
    if (name == null) {
      throw new NameServiceException(
        resources.getString("noNullName")
      );
    }
    if (object == null) {
      throw new NameServiceException(
        resources.getString("noNullObject")
      );
    }
    parsePath(name);
    pathContext.fastUnbind(pathName);
    pathContext.fastBind(pathName, object);
    NameReboundEvent nre = new NameReboundEvent(pathContext, pathName, object);
    pathContext.fireListeners(NAMEREBOUND, nre);
  }

  /**
   * Changes the name with which an object is associated.
   * @param     oldName The name with which the object is currently associated.
   * @param     newName The name with which the object will be associated.
   * @return    True if renaming succeeded, false otherwise.
   */
  public boolean renameNoException(String oldName, String newName)
  {
    String error = internalRename(oldName, newName);
    return (error == null);
  }
    
  /**
   * Changes the name with which an object is associated.
   * @param     oldName The name with which the object is currently associated.
   * @param     newName The name with which the object will be associated.
   * @exception NameServiceException    Thrown if oldName or newName is null,
   *                    or if no object is associated with oldName, or if
   *                    newName is already associated with an object.
   */
  public void rename(String oldName, String newName)
    throws NameServiceException
  {
    String error = internalRename(oldName, newName);
    if (error != null) {
      throw new NameServiceException(error);
    }
  }
    
  /**
   * Changes the name with which an object is associated.
   * @param     oldName The name with which the object is currently associated.
   * @param     newName The name with which the object will be associated.
   * @return    <code>null</code> if renaming succeeded, otherwise an
   *            appropriate error message.
   */
  private String internalRename(String oldName, String newName)
  {
    if (oldName == null || newName == null) {
      return resources.getString("noNullName");
    }
    String error = parsePathNoException(oldName);
    if (error != null) {
      return error;
    }
    NameServiceContext oldContext = pathContext;
    String oldPathName = pathName;
    error = parsePathNoException(newName);
    if (error != null) {
      return error;
    }
    NameServiceContext newContext = pathContext;
    String newPathName = pathName;
    Object oldObject = oldContext.fastLookup(oldPathName);
    if (oldObject == null) {
      return
        resources.getString("nameNotUsed1") +
        oldName +
        resources.getString("nameNotUsed2");
    }
    if (newContext.fastLookup(newPathName) != null) {
      return
        resources.getString("nameInUse1") +
        newName +
        resources.getString("nameInUse2");
    }
    oldContext.fastUnbind(oldPathName);
    newContext.fastBind(newPathName, oldObject);
    // If the new name was in the same context as the old one, send a rename
    // event to the context's listeners, otherwise send an unbind event to the
    // listeners of the old context and a bind event to the listeners of the
    // new context.
    if (oldContext.equals(newContext)) {
      NameChangedEvent nce =
        new NameChangedEvent(this, oldPathName, newPathName);
      oldContext.fireListeners(NAMECHANGED, nce);
    }else{
      NameUnboundEvent nue =
        new NameUnboundEvent(oldContext, oldPathName, oldObject);
      oldContext.fireListeners(NAMEUNBOUND, nue);
      NameBoundEvent nbe =
        new NameBoundEvent(newContext, newPathName, oldObject);
      newContext.fireListeners(NAMEBOUND, nbe);
    }
    return null;
  }

  /**
   * Creates an appropriate hash table key to use for a given name.
   * @param     name    The name for which a hash table key will be returned.
   * @return    If the name service context is case insensitive, then this
   *            method returns an upper case version of name, otherwise it
   *            returns name.
   */
  private String makeKey(String name)
  {
    if (caseSensitive) {
      return name;
    }else{
      return ESlateStrings.upperCase(name, locale);
    }
  }

  /**
   * Returns an array containing the names and associated objects managed by
   * the name service context.
   * @return    The requested array.
   */
  public NameObjectPair[] list()
  {
    synchronized (hashtable) {
      int size = hashtable.size();
      NameObjectPair[] pair = new NameObjectPair[size];
      for (int i=0; i<size; i++) {
        pair[i] = new NameObjectPair();
      }

      Iterator<String> keys = hashtable.keySet().iterator();
      for (int i=0; keys.hasNext(); i++) {
        pair[i].name = keys.next();
      }
      Iterator<Object> elements = hashtable.values().iterator();
      for (int i=0; elements.hasNext(); i++) {
        pair[i].object = elements.next();
      }
      return pair;
    }
  }

  /**
   * Dumps the name hierarchy in the name service context. Useful for
   * debugging.
   */
  public void dumpNames()
  {
    NameObjectPair[] p = list();
    int n = p.length;
    for (int i=0; i<n; i++) {
      dumpNames(p[i], 0);
    }
  }

  /**
   * Dumps the name hierarchy in the name service context, starting from a
   * given name/object pair.
   * @param     pair    The name/object pair from which to start.
   * @param     indent  The indentation level.
   */
  private void dumpNames(NameObjectPair pair, int indent)
  {
    for (int i=0; i<indent; i++) {
      System.out.print("  ");
    }
    System.out.println(pair.name);
    if (pair.object instanceof NameServiceContext) {
      NameObjectPair[] p = ((NameServiceContext)(pair.object)).list();
      int n = p.length;
      indent++;
      for (int i=0; i<n; i++) {
        dumpNames(p[i], indent);
      }
    }
  }

  /**
   * Returns an array containing the names managed by  the name service
   * context.
   * @return    The requested array.
   */
  public String[] listNames()
  {
    synchronized (hashtable) {
      int size = hashtable.size();
      String[] names = new String[size];

      Iterator<String> keys = hashtable.keySet().iterator();
      for (int i=0; keys.hasNext(); i++) {
        names[i] = keys.next();
      }
      return names;
    }
  }

  /**
   * Returns an array containing the objects associated to the names managed by
   * the name service context. Managed objects that are subcontexts are
   * returned as such.
   * @return    The requested array.
   */
  public Object[] listObjectsUnresolved()
  {
    synchronized (hashtable) {
      int size = hashtable.size();
      Object[] objects = new Object[size];

      Iterator<Object> elements = hashtable.values().iterator();
      for (int i=0; elements.hasNext(); i++) {
        objects[i] = elements.next();
      }
      return objects;
    }
  }

  /**
   * Returns an array containing the objects associated to the names managed by
   * the name service context. If a managed object is a subcontext, then the
   * object that is additionally bound to the subcontext's name is returned
   * instead of the subcontext.
   * @return    The requested array.
   */
  public Object[] listObjectsResolved()
  {
    synchronized (hashtable) {
      int size = hashtable.size();
      Object[] objects = new Object[size];

      Iterator<Object> elements = hashtable.values().iterator();
      for (int i=0; elements.hasNext(); i++) {
        Object obj = elements.next();
        if (obj instanceof NameServiceContext) {
          objects[i] = ((NameServiceContext)obj).boundObject;
        }else{
          objects[i] = obj;
        }
      }
      return objects;
    }
  }

  /**
   * Returns the number of names managed by the name service context.
   * @return    The requested number.
   */
  int size()
  {
    synchronized (hashtable)
    {
      return hashtable.size();
    }
  }

  /**
   * Add a listener for name service events.
   * @param     listener        The listener to add.
   */
  public void addNameServiceListener(NameServiceListener listener)
  {
    synchronized (nSListeners) {
      if (!nSListeners.contains(listener)) {
        nSListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener for name service events.
   * @param     listener        The listener to remove.
   */
  public void removeNameServiceListener(NameServiceListener listener)
  {
    synchronized (nSListeners) {
      if (nSListeners.contains(listener)) {
        nSListeners.remove(listener);
      }
    }
  }

  /**
   * Fires all registered listeners.
   * @param     what    Specifies which method of the listeners to invoke.
   * @param     event   The event to send to the listeners.
   */
  @SuppressWarnings(value={"unchecked"})
  private void fireListeners(int what, EventObject event)
  {
    ArrayList<NameServiceListener> listeners;
    synchronized (nSListeners) {
      listeners = (ArrayList<NameServiceListener>)(nSListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      NameServiceListener l = listeners.get(i);
      switch (what) {
        case NAMEBOUND:
          l.nameBound((NameBoundEvent)event);
          break;
        case NAMEUNBOUND:
          l.nameUnbound((NameUnboundEvent)event);
          break;
        case NAMEREBOUND:
          l.nameRebound((NameReboundEvent)event);
          break;
        case NAMECHANGED:
          l.nameChanged((NameChangedEvent)event);
          break;
      }
    }
  }

  /**
   * Free resources. After invoking this method, the class is unusable;
   */
  public void dispose()
  {
    if (hashtable == null) {
      // Resources have been already freed.
      return;
    }
    synchronized (hashtable) {
      // Remove all elements form the hash table. If any of the removed objects
      // is a name service context, recursively dispose of it as well.
      Iterator<String> keys = hashtable.keySet().iterator();
      ArrayList<String> a = new ArrayList<String>();
      while (keys.hasNext()) {
        a.add(keys.next());
      }
      int size = a.size();
      for (int i=0; i<size; i++) {
        String name = a.get(i);
        Object obj = hashtable.get(name);
        if (obj instanceof NameServiceContext && !this.equals(obj)) {
          ((NameServiceContext)obj).dispose();
        }
        hashtable.remove(name);
      }

      nSListeners.clear();
      resources = null;
      locale = null;
      pathContext = null;
      pathName = null;
      boundObject = null;
      parentContext = null;
    }
    hashtable = null;
  }

  /**
   * Sets the separator character used to delimit path names.
   * @param     separator       The new separator character.
   */
  public void setSeparator(char separator)
  {
    this.separator = String.valueOf(separator);
  }

  /**
   * Returns the current separator character.
   * @return    The requested path.
   */
  public char getSeparator()
  {
    return separator.charAt(0);
  }

  /**
   * Sets a reference to the name service context of which this context is a
   * subcontext.
   * @param     parentContext   The parent name service context.
   */
  void setParentContext(NameServiceContext parentContext)
  {
    this.parentContext = parentContext;
  }

  /**
   * Returns the name service context of which this context is a subcontext.
   * @return    The requested context. If this context is a top-level context,
   *            null is returned.
   */
  NameServiceContext getParentContext()
  {
    return parentContext;
  }

  /**
   * Parses a delimiter-separated path into a name service context and a name
   * relative to that context.
   * After this method is executed, variable
   * <code>pathContext</code> contains the name service context
   * described by the path, and variable <code>pathName</code>
   * contains the name to which the path refers, relative to this
   * context.
   * @param     path    The path to parse.
   * @exception NameServiceException    Thrown if the specified path does not
   *                                    exist.
   */
  private void parsePath(String path) throws NameServiceException
  {
    String error = parsePathNoException(path);
    if (error != null) {
      throw new NameServiceException(error);
    }
  }


  /**
   * Parses a delimiter-separated path into a name service context and a name
   * relative to that context. Use this method when you do not want to
   * generate an exception if the parsing fails.
   * @param     path    The path to parse.
   * @return    <code>null</code> if the path was parsed successfully,
   *            otherwise a string describing the error that occurred.
   *            After this method is executed, variable
   *            <code>pathContext</code> contains the name service context
   *            described by the path, and variable <code>pathName</code>
   *            contains the name to which the path refers, relative to this 
   *            context.
   */
  private String parsePathNoException(String path)
  {
    StringTokenizer st = new StringTokenizer(path, separator);
    int pathComponents = st.countTokens() - 1;
    NameServiceContext currentContext = this;
    String currentPath = "";
    for (int i=0; i<pathComponents; i++) {
      String nextName = st.nextToken();
      if (i == 0) {
        currentPath = currentPath + nextName;
      }else{
        currentPath = currentPath + separator + nextName;
      }
      Object nextObject = currentContext.fastLookup(nextName);
      if (nextObject == null) {
        return
          resources.getString("noPath1") +
          currentPath + 
          resources.getString("noPath2");
      }
      if (!(nextObject instanceof NameServiceContext)) {
        return
          resources.getString("notDir1") +
          currentPath +
          resources.getString("notDir2");
      }
      currentContext = (NameServiceContext)nextObject;
    }
    pathContext = currentContext;
    pathName = st.nextToken();
    return null;
  }

  /**
   * Save the state of the name service context. Listeners for name service
   * events are not stored.
   * @param     out     The object output stream where the context is saved.
   */
  public void writeObject(ObjectOutputStream out)
    throws IOException
  {
    writeExternal(out);
  }

  /**
   * Read the state of the name service context. Listeners for name service
   * events are not restored.
   * @param     in      The object input stream from which the context is
   *                    restored.
   */
  public void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    readExternal(in);
  }

  /**
   * Save the state of the name service context. Listeners for name service
   * events are not stored.
   * @param     out     The output stream where the context is saved.
   */
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    NameObjectPair[] p = list();
    out.writeObject(new Integer(p.length));
    out.writeObject(new Boolean(caseSensitive));
    out.writeObject(separator);
    out.writeObject(locale);
    for (int i=0; i<p.length; i++) {
      String objectName = p[i].name;
      Object object = p[i].object;
      out.writeObject(object.getClass().getName());
      out.writeObject(objectName);
      if (object instanceof Externalizable) {
        ((Externalizable)object).writeExternal(out);
      }else{
        out.writeObject(object);
      }
    }
  }

  /**
   * Returns a name guaranteed not to exist in this context. The returned name
   * is a suggested name, possibly followed by "_" and a number, to guarantee
   * uniqueness. If the suggested name is already in that format, the "_" and
   * the following number are trimmed off before creating the name.
   * @param     suggestedName   The suggested name.
   * @return    The requested name.
   */
  public String constructUniqueName(String suggestedName)
  {
    int underscore = suggestedName.lastIndexOf('_');
    if (underscore >= 0) {
      int length = suggestedName.length();
      boolean digit = true;
      for (int i=underscore+1; i<length; i++) {
        if (!Character.isDigit(suggestedName.charAt(i))) {
          digit = false;
          break;
        }else{
        }
      }
      if (digit) {
        suggestedName = suggestedName.substring(0, underscore);
      }
    }

    synchronized (hashtable) {
      Object o = null;
      try {
        o = lookup(suggestedName);
      } catch (NameServiceException nse) {
      }
      String actualName = null;
      String tryName;
      if (o == null) {
        actualName = suggestedName;
      }else{
        // Use Big Integers, to avoid having to deal with overflow and running
        // out of numbers.
        BigInteger n = BigInteger.ZERO;
        do {
          n = n.add(BigInteger.ONE);
          tryName = suggestedName + "_" + n.toString();
          try {
            o = lookup(tryName);
          } catch (NameServiceException nse) {
          }
        } while (o != null);
        actualName = tryName;
      }
      return actualName;
    }
  }

  /**
   * Read the state of the name service context. Listeners for name service
   * events are not restored.
   * @param     in      The input stream from which the context is restored.
   */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    synchronized (hashtable) {
      int nElements = ((Integer)(in.readObject())).intValue();
      caseSensitive = ((Boolean)(in.readObject())).booleanValue();
      separator = (String)(in.readObject());
      locale = (Locale)(in.readObject());
      hashtable = new HashMap<String, Object>();
      for (int i=0; i<nElements; i++) {
        String objectClassName = (String)(in.readObject());
        Class<?> objectClass = Class.forName(objectClassName);
        String objectName = (String)(in.readObject());
        Object object = null;
        if ((java.io.Externalizable.class).isAssignableFrom(objectClass)) {
          try {
            object = objectClass.newInstance();
          } catch (Exception e) {
            throw new IOException(e.getMessage());
          }
          ((Externalizable)object).readExternal(in);
        }else{
          object = in.readObject();
        }
        hashtable.put(objectName, object);
      }
    }
  }
}
