package gr.cti.eslate.registry;

import java.awt.*;
import java.beans.*;
import java.io.*;
import java.util.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.registry.event.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.utils.*;

/**
 * This class implements a variable registry.
 * <P>
 * <B>Component plugs:</B>
 * <UL>
 * <LI>
 * <B>Export registry.</B> This is a multiple output plug associated with a
 * <A HREF="gr.cti.eslate.sharedObject.RegistrySO.html">RegistrySO</A>
 * shared object. The plug's color is Color(139, 69, 19).
 * </LI>
 * </UL>
 * <P>
 * <B>Logo primitives:</B>
 * <UL>
 * <LI>
 * <B>REGISTRY.REGISTER name value ispersistent</B> Registers a variable with
 * a given name and value, specifying if it is persistent or not.
 * </LI>
 * <LI>
 * <B>REGISTRY.UNREGISTER name</B> Unregisters a variable with a given name.
 * </LI>
 * <LI>
 * <B>REGISTRY.SETCOMMENT name comment</B> Associates a comment with a
 * variable having a given name.
 * </LI>
 * <LI>
 * <B>REGISTRY.LOOKUP name</B> Returns the value of a variable having a given
 * name.
 * </LI>
 * </UL>
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 25-May-2006
 */
public class Registry
  implements AsRegistry, Map<String, Object>, ESlatePart, Externalizable,
             Serializable
{
  /**
   * The component's E-Slate handle.
   */
  private ESlateHandle handle;
  /**
   * Localized resources.
   */
  static ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.registry.RegistryResource", Locale.getDefault()
  );
  /**
   * The component's version.
   */
  private final static String version = "2.0.0";
  /**
   * Storage version.
   */
  private final int storageVersion = 1;
  /**
   * The hash table where the variables are registered.
   */
  private HashMap<String, ValuePair> variables;
  /**
   * The hash table where the comments associated with the variables are
   * registered.
   */
  private HashMap<String, String> comments;
  /**
   * The initial capacity of the registry.
   */
  private int initialCapacity;
  /**
   * The list of attached <code>RegistryListener</code>s.
   */
  private RegistryListenerBaseArray listeners = new RegistryListenerBaseArray();

  /**
   * Timer which measures the time required for loading the state of the
   * component.
   */
  PerformanceTimer loadTimer;
  /**
   * Timer which measures the time required for saving the state of the
   * component.
   */
  PerformanceTimer saveTimer;
  /**
   * Timer which measures the time required for the construction of the
   * component.
   */
  PerformanceTimer constructorTimer;
  /**
   * Timer which measures the time required for initializing the E-Slate
   * aspect of the component.
   */
  PerformanceTimer initESlateAspectTimer;
  /**
   * The listener that notifies about changes to the state of the
   * Performance Manager.
   */
  PerformanceListener perfListener = null;

  private final static String INITIAL_CAPACITY = "initialCapacity";
  private final static String NAMES = "names";
  private final static String VALUES = "values";
  private final static String COMMENTS = "comments";

  static final long serialVersionUID = 1L;

  /*
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
   * Create a registry component.
   */
  public Registry()
  {
    initialize(0);
  }

  /**
   * Create a registry component with a specified initial capacity.
   * @param     capacity        The initial capacity of the registry.
   */
  public Registry(int capacity)
  {
    initialize(capacity);
  }

  /**
   * Initializes the registry.
   * @param     capacity        The initial capacity of the registry. If this
   *                            number is less than or equal to zero, a
   *                            default capacity will be used.
   */
  private void initialize(int capacity)
  {

    if (capacity <= 0) {
      variables = new HashMap<String, ValuePair>();
      comments = new HashMap<String, String>();
    }else{
      variables = new HashMap<String, ValuePair>(capacity);
      comments = new HashMap<String, String>(capacity);
    }
    // Invoke attachTimers() *after* initializing the variables and comments
    // attributes, so that the hashCode() function will work.
    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);

    initialCapacity = capacity;

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }

  /**
   * Register a variable. This variable will not be persistent.
   * @param     name    The name of the variable. If a variable having this
   *                    name had already been registered, it is replaced with
   *                    the new variable.
   * @param     value   The value of the variable. This value can be
   *                    <code>null</code>.
   */
  public void registerVariable(String name, Object value)
  {
    registerVariable(name, value, false);
  }

  /**
   * Register a variable.
   * @param     name            The name of the variable. If a variable having
   *                            this name had already been registered, it is
   *                            replaced with the new variable.
   * @param     value           The value of the variable. This value can be
   *                            <code>null</code>.
   * @param     persistent      Specifies whether this variable will be
   *                            persistent or not.
   */
  public void registerVariable(String name, Object value, boolean persistent)
  {
    ValuePair vp = new ValuePair(value, persistent);
    registerValuePair(name, vp);
  }

  /**
   * Register a variable in internal format.
   * @param     name    The name of the variable. If a variable having
   *                    this name had already been registered, it is
   *                    replaced with the new variable.
   * @param     vp      The vlue format in internal <code>ValuePair</code>
   *                    format.
   */
  private void registerValuePair(String name, ValuePair vp)
  {
    boolean update;
    if (containsVariable(name)) {
      update = true;
    }else{
      update = false;
    }
    variables.put(name, vp);
    if (update) {
      valueChanged(name, vp.value, vp.persistent);
    }else{
      variableAdded(name, vp.value, vp.persistent);
    }
  }

  /**
   * Register a character variable. This variable will not be persistent.
   * @param     name    The name of the variable. If a variable having this
   *                    name had already been registered, it is replaced with
   *                    the new variable.
   * @param     value   The value of the variable.
   */
  public void registerVariable(String name, char value)
  {
    registerVariable(name, value, false);
  }

  /**
   * Register a character variable.
   * @param     name            The name of the variable. If a variable having
   *                            this name had already been registered, it is
   *                            replaced with the new variable.
   * @param     value           The value of the variable.
   * @param     persistent      Specifies whether this variable will be
   *                            persistent or not.
   */
  public void registerVariable(String name, char value, boolean persistent)
  {
    registerVariable(name, new Character(value), persistent);
  }

  /**
   * Register a byte variable. This variable will not be persistent.
   * @param     name    The name of the variable. If a variable having this
   *                    name had already been registered, it is replaced with
   *                    the new variable.
   * @param     value   The value of the variable.
   */
  public void registerVariable(String name, byte value)
  {
    registerVariable(name, value, false);
  }

  /**
   * Register a byte variable.
   * @param     name            The name of the variable. If a variable having
   *                            this name had already been registered, it is
   *                            replaced with the new variable.
   * @param     value           The value of the variable.
   * @param     persistent      Specifies whether this variable will be
   *                            persistent or not.
   */
  public void registerVariable(String name, byte value, boolean persistent)
  {
    registerVariable(name, new Byte(value), persistent);
  }

  /**
   * Register a short integer variable. This variable will not be persistent.
   * @param     name    The name of the variable. If a variable having this
   *                    name had already been registered, it is replaced with
   *                    the new variable.
   * @param     value   The value of the variable.
   */
  public void registerVariable(String name, short value)
  {
    registerVariable(name, value, false);
  }

  /**
   * Register a short integer variable.
   * @param     name            The name of the variable. If a variable having
   *                            this name had already been registered, it is
   *                            replaced with the new variable.
   * @param     value           The value of the variable.
   * @param     persistent      Specifies whether this variable will be
   *                            persistent or not.
   */
  public void registerVariable(String name, short value, boolean persistent)
  {
    registerVariable(name, new Short(value), persistent);
  }

  /**
   * Register an integer variable. This variable will not be persistent.
   * @param     name    The name of the variable. If a variable having this
   *                    name had already been registered, it is replaced with
   *                    the new variable.
   * @param     value   The value of the variable.
   */
  public void registerVariable(String name, int value)
  {
    registerVariable(name, value, false);
  }

  /**
   * Register an integer variable.
   * @param     name            The name of the variable. If a variable having
   *                            this name had already been registered, it is
   *                            replaced with the new variable.
   * @param     value           The value of the variable.
   * @param     persistent      Specifies whether this variable will be
   *                            persistent or not.
   */
  public void registerVariable(String name, int value, boolean persistent)
  {
    registerVariable(name, new Integer(value), persistent);
  }

  /**
   * Register a long integer variable. This variable will not be persistent.
   * @param     name    The name of the variable. If a variable having this
   *                    name had already been registered, it is replaced with
   *                    the new variable.
   * @param     value   The value of the variable.
   */
  public void registerVariable(String name, long value)
  {
    registerVariable(name, value, false);
  }

  /**
   * Register a long integer variable.
   * @param     name            The name of the variable. If a variable having
   *                            this name had already been registered, it is
   *                            replaced with the new variable.
   * @param     value           The value of the variable.
   * @param     persistent      Specifies whether this variable will be
   *                            persistent or not.
   */
  public void registerVariable(String name, long value, boolean persistent)
  {
    registerVariable(name, new Long(value), persistent);
  }

  /**
   * Register a floating point variable. This variable will not be persistent.
   * @param     name    The name of the variable. If a variable having this
   *                    name had already been registered, it is replaced with
   *                    the new variable.
   * @param     value   The value of the variable.
   */
  public void registerVariable(String name, float value)
  {
    registerVariable(name, value, false);
  }

  /**
   * Register a floating point variable.
   * @param     name            The name of the variable. If a variable having
   *                            this name had already been registered, it is
   *                            replaced with the new variable.
   * @param     value           The value of the variable.
   * @param     persistent      Specifies whether this variable will be
   *                            persistent or not.
   */
  public void registerVariable(String name, float value, boolean persistent)
  {
    registerVariable(name, new Float(value), persistent);
  }

  /**
   * Register a double precision variable. This variable will not be persistent.
   * @param     name    The name of the variable. If a variable having this
   *                    name had already been registered, it is replaced with
   *                    the new variable.
   * @param     value   The value of the variable.
   */
  public void registerVariable(String name, double value)
  {
    registerVariable(name, value, false);
  }

  /**
   * Register a double precision variable.
   * @param     name            The name of the variable. If a variable having
   *                            this name had already been registered, it is
   *                            replaced with the new variable.
   * @param     value           The value of the variable.
   * @param     persistent      Specifies whether this variable will be
   *                            persistent or not.
   */
  public void registerVariable(String name, double value, boolean persistent)
  {
    registerVariable(name, new Double(value), persistent);
  }

  /**
   * Register a boolean variable. This variable will not be persistent.
   * @param     name    The name of the variable. If a variable having this
   *                    name had already been registered, it is replaced with
   *                    the new variable.
   * @param     value   The value of the variable.
   */
  public void registerVariable(String name, boolean value)
  {
    registerVariable(name, value, false);
  }

  /**
   * Register a boolean variable.
   * @param     name            The name of the variable. If a variable having
   *                            this name had already been registered, it is
   *                            replaced with the new variable.
   * @param     value           The value of the variable.
   * @param     persistent      Specifies whether this variable will be
   *                            persistent or not.
   */
  public void registerVariable(String name, boolean value, boolean persistent)
  {
    registerVariable(name, new Boolean(value), persistent);
  }

  /**
   * Unregister a variable.
   * @param     name            The name of the variable.
   */
  public void unregisterVariable(String name)
  {
    internalUnregisterVariable(name);
  }

  /**
   * Do the actual unregistration of a variable.
   * @param     name    The name of the variable.
   * @return    The previous value of the variable. If the registry did not
   *            contain this variable, or the value of the variable was
   *            <code>null</code>, <code>null</code> is returned.
   */
  private Object internalUnregisterVariable(String name)
  {
    if (variables.containsKey(name)) {
      Object value = lookupVariable(name);
      comments.remove(name);
      variables.remove(name);
      variableRemoved(name);
      return value;
    }else{
      return null;
    }
  }

  /**
   * Sets the persistence state of a variable in the registry.
   * @param     name            The name of the variable whose persistence
   *                            state will be set.
   * @param     persistent      True if the variable will be persistent, false
   *                            otherwise.
   */
  public void setPersistent(String name, boolean persistent)
  {
    if (variables.containsKey(name)) {
      ValuePair vp = variables.get(name);
      if (vp != null) {
        boolean oldPersistent = vp.persistent;
        if (oldPersistent != persistent) {
          vp.persistent = persistent;
          persistenceChanged(name, persistent);
        }
      }
    }
  }

  /**
   * Returns the persitence state of a variable in the registry.
   * @param     name    The name of the variable.
   * @return    True, if the variable is persistent, false if the variable is
   *            not persistent or if there is no such variable in the
   *            registry.
   */
  public boolean isPersistent(String name)
  {
    ValuePair vp = variables.get(name);
    if (vp != null) {
      return vp.persistent;
    }else{
      return false;
    }
  }

  /**
   * Associates a comment with a variable in the registry.
   * @param     name    The name of the variable with which to associate the
   *                    comment. If there is no such variable in the registry,
   *                    the comment is ignored.
   * @param     comment The comment to associate with the variable.
   */
  public void setComment(String name, String comment)
  {
    if (variables.containsKey(name)) {
      String oldComment = comments.get(name);
      if (unEqual(comment, oldComment)) {
        if (comment != null) {
          comments.put(name, comment);
        }else{
          comments.remove(name);
        }
        commentChanged(name, comment);
      }
    }
  }

  /**
   * Checks whether two, possibly null, strings are unequal.
   * @param     s1      The first string.
   * @param     s2      The second string.
   * @return    True if the strings are not equal, false if tehy are equal.
   */
  private static boolean unEqual(String s1, String s2)
  {
    if (s1 == null) {
      if (s2 != null) {
        return true;
      }else{
        return false;
      }
    }else{
      return !s1.equals(s2);
    }
  }

  /**
   * Returns the comment associated with a variable in the registry.
   * @param     name    The name of the variable.
   * @return    The comment associated with the <code>name</code>. If there is
   *            no variable with that name in the registry, or if there is no
   *            comment associated with that variable, <code>null</code> is
   *            returned.
   */
  public String getComment(String name)
  {
    return comments.get(name);
  }

  /**
   * Returns the value of a variable.
   * @param     name    The name of the variable.
   * @return    The value of the variable. If no such variable has been
   *            registered, this method returns <code>null</code>.
   */
  public Object lookupVariable(String name)
  {
    ValuePair vp = variables.get(name);
    if (vp != null) {
      return vp.value;
    }else{
      return null;
    }
  }

  /**
   * Returns the value of a character variable.
   * @param     name    The name of the variable.
   * @param     def     The default value of the variable.
   * @return    The value of the variable. If no such variable has been
   *            registered, the specified default value is returned.
   */
  public char lookupVariable(String name, char def)
  {
    ValuePair vp = variables.get(name);
    if (vp != null) {
      return ((Character)(vp.value)).charValue();
    }else{
      return def;
    }
  }

  /**
   * Returns the value of a byte variable.
   * @param     name    The name of the variable.
   * @param     def     The default value of the variable.
   * @return    The value of the variable. If no such variable has been
   *            registered, the specified default value is returned.
   */
  public byte lookupVariable(String name, byte def)
  {
    ValuePair vp = variables.get(name);
    if (vp != null) {
      return ((Byte)(vp.value)).byteValue();
    }else{
      return def;
    }
  }

  /**
   * Returns the value of a short integer variable.
   * @param     name    The name of the variable.
   * @param     def     The default value of the variable.
   * @return    The value of the variable. If no such variable has been
   *            registered, the specified default value is returned.
   */
  public short lookupVariable(String name, short def)
  {
    ValuePair vp = variables.get(name);
    if (vp != null) {
      return ((Short)(vp.value)).shortValue();
    }else{
      return def;
    }
  }

  /**
   * Returns the value of an integer variable.
   * @param     name    The name of the variable.
   * @param     def     The default value of the variable.
   * @return    The value of the variable. If no such variable has been
   *            registered, the specified default value is returned.
   */
  public int lookupVariable(String name, int def)
  {
    ValuePair vp = variables.get(name);
    if (vp != null) {
      return ((Integer)(vp.value)).intValue();
    }else{
      return def;
    }
  }

  /**
   * Returns the value of a long integer variable.
   * @param     name    The name of the variable.
   * @param     def     The default value of the variable.
   * @return    The value of the variable. If no such variable has been
   *            registered, the specified default value is returned.
   */
  public long lookupVariable(String name, long def)
  {
    ValuePair vp = variables.get(name);
    if (vp != null) {
      return ((Long)(vp.value)).longValue();
    }else{
      return def;
    }
  }

  /**
   * Returns the value of a floating point variable.
   * @param     name    The name of the variable.
   * @param     def     The default value of the variable.
   * @return    The value of the variable. If no such variable has been
   *            registered, the specified default value is returned.
   */
  public float lookupVariable(String name, float def)
  {
    ValuePair vp = variables.get(name);
    if (vp != null) {
      return ((Float)(vp.value)).floatValue();
    }else{
      return def;
    }
  }

  /**
   * Returns the value of a double precision variable.
   * @param     name    The name of the variable.
   * @param     def     The default value of the variable.
   * @return    The value of the variable. If no such variable has been
   *            registered, the specified default value is returned.
   */
  public double lookupVariable(String name, double def)
  {
    ValuePair vp = variables.get(name);
    if (vp != null) {
      return ((Double)(vp.value)).doubleValue();
    }else{
      return def;
    }
  }

  /**
   * Returns the value of a boolean variable.
   * @param     name    The name of the variable.
   * @param     def     The default value of the variable.
   * @return    The value of the variable. If no such variable has been
   *            registered, the specified default value is returned.
   */
  public boolean lookupVariable(String name, boolean def)
  {
    ValuePair vp = variables.get(name);
    if (vp != null) {
      return ((Boolean)(vp.value)).booleanValue();
    }else{
      return def;
    }
  }

  /**
   * Checks whether the registry contains a given variable.
   * @param     name    The name of the variable.
   * @return    True if the variable has been registered, otherwise false.
   */
  public boolean containsVariable(String name)
  {
    return variables.containsKey(name);
  }
    
  /**
   * Returns the component's E-Slate handle.
   * @return    The requested handle.
   */
  public ESlateHandle getESlateHandle()
  {
    if (handle == null) {
      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      pm.eSlateAspectInitStarted(this);
      pm.init(initESlateAspectTimer);

      initESlate();

      pm.stop(initESlateAspectTimer);
      pm.eSlateAspectInitEnded(this);
      pm.displayTime(initESlateAspectTimer, handle, "", "ms");
    }
    return handle;
  }

  /**
   * Initilaizes the E-Slate functionality of the component.
   */
  private void initESlate()
  {
    handle = ESlate.registerPart(this);
    handle.setInfo(getInfo());

    try {
      handle.setUniqueComponentName(resources.getString("name"));
    } catch (RenamingForbiddenException e) {
    }

    handle.addESlateListener(new ESlateAdapter() {
      public void handleDisposed(HandleDisposalEvent e)
      {
        dispose();
      }
    });

    try {
      ProtocolPlug plug = new RightMultipleConnectionProtocolPlug(
        handle, resources, "exportRegistry", new Color(139, 69, 19),
        null, this
      );
      plug.setHostingPlug(true);
      handle.addPlug(plug);
    } catch (InvalidPlugParametersException e) {
      System.out.println(e.getMessage());
    } catch (PlugExistsException e) {
      System.out.println(e.getMessage());
    }

    // Register supported Logo primitive group.
    handle.addPrimitiveGroup(
      "gr.cti.eslate.scripting.logo.RegistryPrimitives"
    );
  }

  /**
   * Save the component's state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(saveTimer);

    StorageStructure m = getState();
    oo.writeObject(m);

    pm.stop(saveTimer);
    pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Returns a snapshot of the registry.
   * @return    A snapshot of the registry.
   */
  public StorageStructure getState()
  {
    ESlateFieldMap2 m = new ESlateFieldMap2(storageVersion, 2);
    String[] names;
    ValuePair[] values;
    String[] comms;

    synchronized(variables) {
      int nPersistent = 0;
      Set<Map.Entry<String, ValuePair>> s = variables.entrySet();
      Iterator<Map.Entry<String, ValuePair>> it = s.iterator();
      while (it.hasNext()) {
        Map.Entry<String, ValuePair> me = it.next();
        ValuePair vp = me.getValue();
        if (vp.persistent &&
            (vp.value instanceof Externalizable ||
             vp.value instanceof Serializable)) {
          nPersistent++;
        }
      }
      it = s.iterator();
      names = new String[nPersistent];
      values = new ValuePair[nPersistent];
      comms = new String[nPersistent];
      int n = 0;
      while (it.hasNext()) {
        Map.Entry<String, ValuePair> me = it.next();
        ValuePair vp = me.getValue();
        if (vp.persistent &&
            (vp.value instanceof Externalizable ||
             vp.value instanceof Serializable)) {
          names[n] = me.getKey();
          values[n] = vp;
          comms[n] = comments.get(me.getKey());
          n++;
        }
      }
    }

    m.put(INITIAL_CAPACITY, initialCapacity);
    m.put(NAMES, names);
    m.put(VALUES, values);
    m.put(COMMENTS, comms);

    return m;
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(loadTimer);

    StorageStructure m = (StorageStructure)(oi.readObject());
    setState(m);

    pm.stop(loadTimer);
    pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Sets the state of the registry to a snapshot taken using
   * <code>getRegistry()</code>. After executing this method, the contents of
   * the registry will be set to the contents that the registry had when the
   * snapshot was taken.
   * @param     m       The snapshot to which to set the registry's state.
   */
  public void setState(StorageStructure m)
  {
    initialCapacity = m.get(INITIAL_CAPACITY, 0);
    String[] names = (String[])(m.get(NAMES));
    ValuePair[] values = (ValuePair[])(m.get(VALUES));
    String[] comms = (String[])(m.get(COMMENTS));

    if (variables.size() < initialCapacity) {
      variables = new HashMap<String, ValuePair>(initialCapacity);
      comments = new HashMap<String, String>(initialCapacity);
    }else{
      variables.clear();
      comments.clear();
    }
    registryCleared();
    int n = names.length;
    for (int i=0; i<n; i++) {
      registerValuePair(names[i], values[i]);
      if (comms != null) {
        setComment(names[i], comms[i]);
      }
    }
  }

  /**
   * Save the component's state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeObject(ObjectOutputStream oo) throws IOException
  {
    writeExternal(oo);
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readObject(ObjectInputStream oi)
    throws IOException, ClassNotFoundException
  {
    readExternal(oi);
  }

  /**
   * Free resources. After invoking this method, the component is unusable.
   */
  public void dispose()
  {
    if (handle != null) {
      handle.dispose();
    }
    listeners.clear();
    listeners = null;

    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.removePerformanceListener(perfListener);
    perfListener = null;
  }

  /**
   * Clears the registry.
   */
  public void clear()
  {
    variables.clear();
    comments.clear();
    registryCleared();
  }

  /**
   * Checks whether the registry contains a given variable. This is equivalent
   * to <code>containsVariable</code>, defined to satisfy the requirements of
   * the <code>Map</code> interface.
   * @param     key     The name of the variable.
   * @return    True if the variable has been registered, otherwise false.
   */
  public boolean containsKey(Object key)
  {
    return containsVariable(key.toString());
  }

  /**
   * Checks whether the registry contains a variable having a given value.
   * @param     value   The value to check.
   * @return    True if the registry contains a variable having the given
   *            value, false otherwise.
   */
  public boolean containsValue(Object value)
  {
    Set<Map.Entry<String, ValuePair>> s = variables.entrySet();
    Iterator<Map.Entry<String, ValuePair>> it = s.iterator();
    while (it.hasNext()) {
      Map.Entry<String, ValuePair> me = it.next();
      ValuePair vp = me.getValue();
      if (value == null) {
        if (vp.value == null) {
          return true;
        }
      }else{
        if (value.equals(vp.value)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns a set view of the mappings contained in the registry. Each
   * element in the returned set is a Map.Entry. These elements cannot be
   * modified.
   * @return    A set view of the mappings contained in the registry.
   */
  public Set<Map.Entry<String, Object>> entrySet()
  {
    ArrayList<MapEntry> l = new ArrayList<MapEntry>(variables.size());
    Set<Map.Entry<String, ValuePair>> s = variables.entrySet();
    Iterator<Map.Entry<String, ValuePair>> it = s.iterator();
    while (it.hasNext()) {
      Map.Entry<String, ValuePair> me = it.next();
      String name = me.getKey();
      String comment = getComment(name);
      ValuePair vp = me.getValue();
      MapEntry newMe = new MapEntry(name, vp.value, vp.persistent, comment);
      l.add(newMe);
    }
    return new HashSet<Map.Entry<String, Object>>(l);
  }

  /**
   * Compares the specified object with this registry for equality.
   * @return    True if the object is equal to this registry, false otherwise.
   */
  public boolean equals(Object o)
  {
    if (o instanceof Registry) {
      return variables.equals(((Registry)o).variables) &&
             comments.equals(((Registry)o).comments);
    }else{
      return false;
    }
  }

  /**
   * Returns the value of a variable.
   * @param     key     The name of the variable. This is equivalent to
   * <code>lookupVariable</code>, defined to satisfy the requirements of the
   * <code>Map</code> interface.
   * @return    The value of the variable. If no such variable has been
   *            registered, this method returns <code>null</code>.
   */
  public Object get(Object key)
  {
    return lookupVariable(key.toString());
  }

  /**
   * Returns the hash code for this registry.
   * @return    The hash code for this registry.
   */
  public int hashCode()
  {
    int result = 17;
    result = 37 * result + variables.hashCode();
    result = 37 * result + comments.hashCode();
    return result;
  }

  /**
   * Checks whether the registry is empty.
   * @return    True if the registry is empty, false otherwise.
   */
  public boolean isEmpty()
  {
    return variables.isEmpty();
  }

  /**
   * Returns a set view of the variable names contained in this registry.
   * @return    A set view of the variable names contained in this registry.
   */
  public Set<String> keySet()
  {
    return variables.keySet();
  }

  /**
   * Register a variable. This variable will not be persistent.
   * @param     key     The name of the variable. If a variable having this
   *                    name had already been registered, it is replaced with
   *                    the new variable.
   * @param     value   The value of the variable. This value can be
   *                    <code>null</code>.
   * @return    The previous value of the variable, or <code>null</code> if
   *            no such variable had been registered. A <code>null</code>
   *            return can also indicate that the previous value of the
   *            variable was <code>null</code>.
   */
  public Object put(String key, Object value)
  {
    Object oldValue = lookupVariable(key);
    registerVariable(key, value);
    return oldValue;
  }

  /**
   * Registers all the variables from the specified map to this registry. These
   * variables will replace any variables that had already been registered
   * under a name contained in the specified map.
   * @param     t       A map containing a set of name / value mappings.
   */
  public void putAll(Map<? extends String, ? extends Object> t)
  {
    // No, I do not understand all this wildcard madness.

    Iterator<? extends Map.Entry<? extends String, ? extends Object>> it =
      t.entrySet().iterator();

    while (it.hasNext()) {
      Map.Entry< ? extends String, ? extends Object> me = it.next();
      String key = me.getKey();
      Object value = me.getValue();
      registerVariable(key, value, false);
    }
  }

  /**
   * Unregister a variable. This is equivalent to
   * <code>registerVariable</code>, defined to satisfy the requirements of
   * the <code>Map</code> interface.
   * @return    The previous value of the variable, or <code>null</code> if
   *            no such variable had been registered. A <code>null</code>
   *            return can also indicate that the previous value of the
   *            variable was <code>null</code>.
   * @param     name    The name of the variable.
   */
  public Object remove(Object key)
  {
    String name = key.toString();
    return internalUnregisterVariable(name);
  }

  /**
   * Returns the number of registered variables.
   * @return    The number of registered variables.
   */
  public int size()
  {
    return variables.size();
  }

  /**
   * Returns a collection view of the values contained in this registry.
   * @return    A collection view of the values contained in this registry.
   */
  public Collection<Object> values()
  {
    Set<Map.Entry<String, ValuePair>> s = variables.entrySet();
    Iterator<Map.Entry<String, ValuePair>> it = s.iterator();
    ArrayList<Object> l = new ArrayList<Object>(variables.size());
    while (it.hasNext()) {
      Map.Entry<String, ValuePair> me = it.next();
      ValuePair vp = me.getValue();
      l.add(vp.value);
    }
    return l;
  }

  /**
   * Adds a listener for registry modifications.
   * @param     l       The listener to add.
   */
  public void addRegistryListener(RegistryListener l)
  {
    synchronized(listeners) {
      if (!listeners.contains(l)) {
        listeners.add(l);
      }
    }
  }

  /**
   * Removes a listener for rehgistry modifications.
   * @param     l       The listener to remove.
   */
  public void removeRegistryListener(RegistryListener l)
  {
    synchronized(listeners) {
      int i = listeners.indexOf(l);
      if (i >= 0) {
        listeners.remove(i);
      }
    }
  }

  /**
   * Notify attached listeners that the value of a variable has changed.
   * @param     name            The name of the variable.
   * @param     value           The new value of the variable.
   * @param     persistent      Specifies whether the variable is persistent.
   */
  private void valueChanged(String name, Object value, boolean persistent)
  {
    RegistryListenerBaseArray a;
    synchronized(listeners) {
      a = (RegistryListenerBaseArray)(listeners.clone());
    }
    int nListeners = a.size();
    for (int i=0; i<nListeners; i++) {
      RegistryEvent e = new RegistryEvent(this, name, value, persistent, null);
      a.get(i).valueChanged(e);
    }
  }

  /**
   * Notify attached listeners that a variable was added to the registry.
   * @param     name    The name of the variable.
   * @param     value   The value of the variable.
   * @param     persistent      Specifies whether the variable is persistent.
   */
  private void variableAdded(String name, Object value, boolean persistent)
  {
    RegistryListenerBaseArray a;
    synchronized(listeners) {
      a = (RegistryListenerBaseArray)(listeners.clone());
    }
    int nListeners = a.size();
    for (int i=0; i<nListeners; i++) {
      RegistryEvent e = new RegistryEvent(this, name, value, persistent, null);
      a.get(i).variableAdded(e);
    }
  }

  /**
   * Notify attached listeners that a variable was removed from the registry.
   * @param     name    The name of the variable.
   */
  private void variableRemoved(String name)
  {
    RegistryListenerBaseArray a;
    synchronized(listeners) {
      a = (RegistryListenerBaseArray)(listeners.clone());
    }
    int nListeners = a.size();
    for (int i=0; i<nListeners; i++) {
      RegistryEvent e = new RegistryEvent(this, name, null, false, null);
      a.get(i).variableRemoved(e);
    }
  }

  /**
   * Notify attached listeners that the persistence state of a variable has
   * changed.
   * @param     name            The name of the variable.
   * @param     persistence     The new persistence state of the variable.
   */
  private void persistenceChanged(String name, boolean persistence)
  {
    RegistryListenerBaseArray a;
    synchronized(listeners) {
      a = (RegistryListenerBaseArray)(listeners.clone());
    }
    int nListeners = a.size();
    for (int i=0; i<nListeners; i++) {
      RegistryEvent e = new RegistryEvent(this, name, null, persistence, null);
      a.get(i).persistenceChanged(e);
    }
  }

  /**
   * Notify attached listeners that the comment associated with a variable
   * has changed.
   * @param     name    The name of the variable.
   * @param     comment The new comment associated with the variable.
   */
  private void commentChanged(String name, String comment)
  {
    RegistryListenerBaseArray a;
    synchronized(listeners) {
      a = (RegistryListenerBaseArray)(listeners.clone());
    }
    int nListeners = a.size();
    for (int i=0; i<nListeners; i++) {
      RegistryEvent e = new RegistryEvent(this, name, null, false, comment);
      a.get(i).commentChanged(e);
    }
  }

  /**
   * Notify attached listeners that the registry was cleared.
   */
  private void registryCleared()
  {
    RegistryListenerBaseArray a;
    synchronized(listeners) {
      a = (RegistryListenerBaseArray)(listeners.clone());
    }
    int nListeners = a.size();
    for (int i=0; i<nListeners; i++) {
      RegistryEvent e = new RegistryEvent(this, null, null, false, null);
      a.get(i).registryCleared(e);
    }
  }

  /**
   * This method creates and adds a PerformanceListener to the E-Slate's
   * Performance Manager. The PerformanceListener attaches the component's
   * timers when the Performance Manager becomes enabled.
   */
  private void createPerformanceManagerListener(PerformanceManager pm)
  {
    if (perfListener == null) {
      perfListener = new PerformanceAdapter() {
        public void performanceManagerStateChanged(PropertyChangeEvent e)
        {
          boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
          // When the Performance Manager is enabled, try to attach the
          // timers.
          if (enabled) {
            attachTimers();
          }
        }
      };
      pm.addPerformanceListener(perfListener);
    }
  }

  /**
   * This method creates and attaches the component's timers. The timers are
   * created only once and are assigned to global variables. If the timers
   * have been already created, they are not re-created. If the timers have
   * been already attached, they are not attached again.
   * This method does not create any timers while the PerformanceManager is
   * disabled.
   */
  private void attachTimers()
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    boolean pmEnabled = pm.isEnabled();

    // If the performance manager is disabled, install a listener which will
    // re-invoke this method when the performance manager is enabled.
    if (!pmEnabled && (perfListener == null)) {
      createPerformanceManagerListener(pm);
    }

    // Do nothing if the PerformanceManager is disabled.
    if (!pmEnabled) {
      return;
    }

    boolean timersCreated = (loadTimer != null);
    // If the component's timers have not been constructed yet, then
    // construct them. During construction, the timers are also attached.
    if (!timersCreated) {
      // Get the performance timer group for this component.
      PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);
      // Construct and attach the component's timers.
      constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("ConstructorTimer"), true
      );
      loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("LoadTimer"), true
      );
      saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("SaveTimer"), true
      );
      initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("InitESlateAspectTimer"), true
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.CONSTRUCTOR, constructorTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.LOAD_STATE, loadTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.SAVE_STATE, saveTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
      );
    }
  }

  /**
   * A Map.Entry implementation, to be used in conjunction with the registry's
   * <code>entrySet()</code> method.
   */
  public class MapEntry implements Map.Entry<String, Object>
  {
    /**
     * The name of the entry.
     */
    private String name;
    /**
     * The value of the entry.
     */
    private Object value;
    /**
     * Specifies whether the entry is persistent.
     */
    private boolean persistent;
    /**
     * The comment associated with the entry.
     */
    private String comment;

    /**
     * Create a map entry.
     * @param   name            The name of the entry.
     * @param   value           The value of the entry.
     * @param   persistent      Specifies whether the entry is persistent.
     * @param   comment         The comment associated with the entry.
     */
    MapEntry(String name, Object value, boolean persistent, String comment)
    {
      this.name = name;
      this.value = value;
      this.persistent = persistent;
      this.comment = comment;
    }

    /**
     * Compares the specified object with this entry for equality.
     * @param   o       The object to compare.
     * @return  Returns true if the given object is also a map entry and the
     *          two entries represent the same mapping.
     */
    public boolean equals(Object o)
    {
      if (o instanceof Map.Entry) {
        Map.Entry e1 = this;
        Map.Entry e2 = (Map.Entry)o;
        // Copied from the Map.Entry Javadocs
        return (e1.getKey()==null ?
                e2.getKey()==null : e1.getKey().equals(e2.getKey()))  &&
               (e1.getValue()==null ?
                e2.getValue()==null : e1.getValue().equals(e2.getValue()));
      }else{
        return false;
      }
    }

    /**
     * Returns the key corresponding to this entry.
     * @return  The key corresponding to this entry.
     */
    public String getKey()
    {
      return name;
    }

    /**
     * Returns the name of this entry.
     * @return  The name of this entry.
     */
    public String getName()
    {
      return name;
    }

    /**
     * Returns the value corresponding to this entry.
     * @return  The value corresponding to this entry.
     */
    public Object getValue()
    {
      return value;
    }

    /**
     * Returns the hash code value for this map entry.
     * @return  The hash code value for this map entry.
     */
    public int hashCode()
    {
      Map.Entry e = this;
      // Copied from the Map.Entry Javadocs
      return (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
             (e.getValue()==null ? 0 : e.getValue().hashCode());
    }

    /**
     * Replaces the value corresponding to this entry with the specified
     * value. (This method is optional and has an empty implementation on
     * purpose.)
     * @param   value   The new value.
     * @return  The value of the variable.
     */
    public Object setValue(Object value)
    {
      // Do nothing.
      return value;
    }

    /**
     * Returns the comment associated with this entry.
     * @return  The comment associated with this entry.
     */
    public String getComment()
    {
      return comment;
    }

    /**
     * Checks whether the entry is persistent.
     * @return  True if yes, false if no.
     */
    public boolean isPersistent()
    {
      return persistent;
    }
  }

}
