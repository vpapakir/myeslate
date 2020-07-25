package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.eslateToolBar.event.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.services.name.*;
import gr.cti.eslate.utils.*;

/**
 * Toolbar component.
 * <P>
 * <B>Component plugs:</B>
 * <P>
 * None.
 * <P>
 * <B>Logo primitives:</B>
 * <UL>
 * <LI>
 * <B>TOOLBAR.SHOWTOOL name</B> Show the tool named <code>name</code> on the
 * toolbar.
 * </LI>
 * <LI>
 * <B>TOOLBAR.HIDETOOL name</B> Hide the tool named <code>name</code> from the
 * toolbar.
 * </LI>
 * <LI>
 * <B>TOOLBAR.SHOWGROUP name</B> Show the components contained in the visual
 * group named <code>name</code> on the toolbar.
 * </LI>
 * <LI>
 * <B>TOOLBAR.HIDETOOL name</B> Hide the components contained in the visual
 * group named <code>name</code> from the toolbar.
 * </LI>
 * </UL>
 *
 * @version     2.0.4, 23-Jan-2008
 * @author      Kriton Kyrimis
 */
public class ESlateToolBar extends JToolBar
  implements ESlatePart, AsToolBar, Serializable, Externalizable,
  ComponentListener
{
  /**
   * Component version.
   */
  private final static String version = "2.0.4";
  /**
   * Component E-Slate handle.
   */
  private ESlateHandle handle = null;
  /**
   * Localized resources.
   */
  private ResourceBundle resources = null;
  /**
   * The visual groups contained in the toolbar.
   */
  VisualGroupBaseArray vGroups = new VisualGroupBaseArray();
  /**
   * The name service context for maintaining unique visual group names.
   */
  private NameServiceContext vNames = new NameServiceContext();
  /**
   * The name service context for maintaining unique button group names.
   */
  private NameServiceContext bNames = new NameServiceContext();
  /**
   * The button groups contained in the toolbar.
   */
  ButtonGroupBaseArray bGroups = new ButtonGroupBaseArray();
  /**
   * The value of the separation between visual groups.
   */
  private Dimension separation;
  /**
   * Information associated with hosted tools.
   */
  private HashMap <Component, ToolInfo> toolInfo =
    new HashMap<Component, ToolInfo>();
  /**
   * The separator that will be used before the first tool in the
   * toolbar.
   */
  private Dimension leadingSeparation;
  /**
   * Indicates whether there have been changes in the component's layout.
   */
  private boolean layoutChanged;
  /**
   * The preferred size of the toolbar, as returned by the parent JToolBar.
   */
  Dimension actualPrefSize;
  /**
   * The preferred size of the toolbar, as specified via the
   * <code>setPreferredSize()</code> method.
   */
  private Dimension prefSize = null;
  /**
   * The list of registered ToolBarListeners.
   */
  private TBListenerBaseArray tbListeners = new TBListenerBaseArray();
  /**
   * The list of TBLWrappers corresponding to the registered ToolBarListeners.
   */
  private TBLWrapperBaseArray tblWrappers = new TBLWrapperBaseArray();
  /**
   * Indicates whether the contents of the toolbar should be centered within
   * the toolbar.
   */
  private boolean centered;
  /**
   * The "scroll forward" button.
   */
  private ScrollButton fdButton = null;
  /**
   * The "scroll back" button.
   */
  private ScrollButton bkButton = null;
  /**
   * The "glue" before the "scroll forward" button.
   */
  private Component fdGlue = null;
  /**
   * Indicates whether the "scroll forward button is shown.
   */
  private boolean haveFdButton = false;
  /**
   * Indicates whether the "scroll back button is shown.
   */
  private boolean haveBkButton = false;
  /**
   * The components after the end of the toolbar.
   */
  private ComponentBaseArray fdComponents = new ComponentBaseArray();
  /**
   * The components before the beginning of the toolbar.
   */
  private ComponentBaseArray bkComponents = new ComponentBaseArray();
  /**
   * Indicates that the toolbar is currently being scrolled.
   */
  private Boolean scrolling = Boolean.FALSE;
  /**
   * Indicates that the component's constructor has finished executing.
   */
  private boolean created = false;
  /**
   * The object that can set the toolbar to its default state.
   */
  private DefaultStateSetter defaultStateSetter = null;
  /**
   * Specifies whether the toolbar has been modified.
   */
  private boolean modified;
  /**
   * Specifies whether a popup menu will appear when pressing the mouse on
   * unused space.
   */
  private boolean menuEnabled;
  /**
   * The popup menu.
   */
  private ESlateJPopupMenu popup = null;
  /**
   * Indicates that the writeExternal method is currently executing, and that
   * any (recursive) calls to it should be skipped to avoid falling in a loop.
   */
  private boolean skipWriteExternal = false;
  /**
   * Indicates that the object read by readExternal should be discarded.
   * Hosted components may try to save references to the panel. We avoid
   * infinite recursion by saving a null in invocations of writeExternal from
   * within itself, but this is not enough. When such components are restored,
   * they try to recreate such references by creating new Panel instances and
   * invoking readExternal on them. This can cause all sorts of weird
   * problems, so we use this flag to implement a readResolve method that
   * returns null when the null that was saved by writeExternal is read, so
   * that the bogus Panel instance is discarded.
   */
  private boolean isBogus = false;

  /**
   * Save format version.
   */
  private final static int saveVersion = 2;

  /**
   * Timer which measures the time required for loading the state of the
   * component.
   */
  protected PerformanceTimer loadTimer;
  /**
   * Timer which measures the time required for saving the state of the
   * component.
   */
  protected PerformanceTimer saveTimer;
  /**
   * Timer which measures the time required for the construction of the
   * component.
   */
  protected PerformanceTimer constructorTimer;
  /**
   * Timer which measures the time required for initializing the E-Slate
   * aspect of the component.
   */
  protected PerformanceTimer initESlateAspectTimer;
  /**
   * The listener that notifies about changes to the state of the
   * Performance Manager.
   */
  PerformanceListener perfListener = null;

  // StorageStructure keys
  private final static String ORIENTATION = "1";
  private final static String TOOLS = "2";
  private final static String VISUAL_GROUPS = "3";
  private final static String VISUAL_GROUP_SIZES = "4";
  private final static String TOOL_NAMES = "5";
  private final static String TOOL_INFO = "6";
  private final static String LEADING_SEPARATION = "7";
  private final static String BUTTON_GROUPS = "8";
  private final static String BUTTON_GROUP_TOOL_NAMES = "9";
  private final static String DYNAMIC_BORDERS = "10";
  private final static String BORDER = "11";
  private final static String SEPARATION = "12";
  private final static String CENTERED = "13";
  private final static String BORDER_PAINTED = "14";
  private final static String FLOATABLE = "15";
  private final static String NAME = "16";
  private final static String MINIMUM_SIZE = "17";
  private final static String MAXIMUM_SIZE = "18";
  private final static String PREFERRED_SIZE = "19";
  private final static String MODIFIED = "20";
  private final static String MENU_ENABLED = "21";

  /**
   * Serialization format version.
   */
  static final long serialVersionUID = 1L;

  /**
   * Creates a new toolbar; orientation defaults to <code>HORIZONTAL</code>.
   */
  public ESlateToolBar()
  {
    this(null, HORIZONTAL);
  }

  /**
   * Creates a new toolbar with the specified orientation. The orientation
   * must be either <code>HORIZONTAL</code> or <code>VERTICAL</code>.
   * @param     orientation     The orientation desired.
   */
  public ESlateToolBar(int orientation)
  {
    this(null, orientation);
  }

  /**
   * Creates a new toolbar with the specified name. The name is used as the
   * title of the undocked toolbar. The default orientation is
   * <code>HORIZONTAL</code>.
   * @param     name    The name of the toolbar.
   */
  public ESlateToolBar(String name)
  {
    this(name, HORIZONTAL);
  }

  /**
   * Creates a new toolbar from an <code>ObjectInput</code>. The toolbar's
   * state is initialized directly from the contents of the provided input
   * stream.
   * @param     oi      The <code>ObjectInput</code> from which to initialize
   *                    the toolbar.
   * @exception Exception       Thrown if constructing the toolbar fails.
   */
  public ESlateToolBar(ObjectInput oi) throws Exception
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.eslateToolBar.ToolBarResource", Locale.getDefault()
    );

    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);

    initializeCommon();

    pm.stop(constructorTimer);
    pm.constructionEnded(this);

    getESlateHandle();  // Make sure E-Slate aspect has been initialized.
    initialize(oi);
    pm.start(constructorTimer);
    addComponentListener(this);
    pm.stop(constructorTimer);

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }

  /**
   * Creates a new toolbar with a specified name and orientation. All other
   * constructors call this constructor. If <code>orientation</code> is an
   * invalid value an exception will be thrown.
   * @param     name    The name of the toolbar.
   * @param     orientation     The initial orientation -- it must be either
   *                            <code>HORIZONTAL</code> or
   *                            <code>VERTICAL</code>.
   * @exception IllegalArgumentException        Thrown if orientation is
   *                            neither <code>HORIZONTAL</code> nor
   *                            <code>VERTICAL</code>.
   */
  public ESlateToolBar(String name, int orientation)
    throws IllegalArgumentException
  {
    super(name, orientation);

    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.eslateToolBar.ToolBarResource", Locale.getDefault()
    );

    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);
    pm.stop(constructorTimer);

    initializeCommon();
    initialize();
    pm.start(constructorTimer);
    addComponentListener(this);
    pm.stop(constructorTimer);

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }

  /**
   * Initialize the toolbar.
   */
  private void initialize()
  {
    actualPrefSize = null;
    leadingSeparation = new Dimension(0, 0);
    separation = null;
    centered = false;
    setRollover(true);
    layoutChanged = false;
    created = true;
    modified = false;
    menuEnabled = false;
  }

  /**
   * Common initializations for all toolbar constructors.
   */
  private void initializeCommon()
  {
    addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent e)
      {
        processMousePress(e);
      }
    });
  }

  /**
   * Initialize the toolbar from an <code>ObjectInput</code>.
   * @param     oi      The <code>ObjectInput</code> from which to initialize
   *                    the toolbar.
   * @exception IOException     Thrown if reading from <code>oi</code> fails.
   * @exception ClassNotFoundException  Thrown if one of the classes being
   *                    read from <code>oi</code> is not in the class path.
   */
  private void initialize(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.eslateToolBar.ToolBarResource", Locale.getDefault()
    );

    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(loadTimer);

    StorageStructure map = (StorageStructure)(oi.readObject());
    int version = map.getDataVersionID();

    // Our writeExternal method will simply write a null when invoked from
    // within itself. When reading this null, do nothing.
    if (map == null) {
      isBogus = true;
      pm.stop(loadTimer);
      pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
      return;
    }

    if (created) {
      removeAll();
    }

    // Invalidate old preferred size.
    actualPrefSize = null;

    // Restore orientation.
    setOrientation(map.get(ORIENTATION, HORIZONTAL));

    // Restore tools.
    ESlateHandle h = getESlateHandle();
    h.restoreChildren(map, TOOLS);

    // Restore visual groups.
    VisualGroup[] viGroups = (VisualGroup [])(map.get(VISUAL_GROUPS));
    int nVGroups = viGroups.length;
    int n = vGroups.size();
    for (int i=0; i<n; i++) {
      try {
        vNames.unbind(vGroups.get(i).getName());
      } catch (NameServiceException e) {
      }
    }
    vGroups.clear();
    for (int i=0; i<nVGroups; i++) {
      VisualGroup group = viGroups[i];
      try {
        vNames.bind(group.getName(), group);
      } catch (NameServiceException nse) {
      }
      vGroups.add(group);
    }

    // Restore number of components in each visual group.
    int[] groupSizes = (int[])(map.get(VISUAL_GROUP_SIZES));

    // Restore button groups.
    ESlateButtonGroup[] buGroups;
    Object bgs = map.get(BUTTON_GROUPS);
    if (version == 1) {
      buGroups = (ESlateButtonGroup [])bgs;
    }else{
      // version == 2
      String[] bgNames = (String[])bgs;
      int nBgs = bgNames.length;
      buGroups = new ESlateButtonGroup[nBgs];
      for (int i=0; i<nBgs; i++) {
        buGroups[i] = new ESlateButtonGroup(bgNames[i]);
      }
    }
    int nBGroups = buGroups.length;
    n = bGroups.size();
    for (int i=0; i<n; i++) {
      try {
        bNames.unbind(bGroups.get(i).getName());
      } catch (NameServiceException e) {
      }
    }
    bGroups.clear();
    for (int i=0; i<nBGroups; i++) {
      ESlateButtonGroup group = buGroups[i];
      try {
        bNames.bind(group.getName(), group);
      } catch (NameServiceException nse) {
      }
      bGroups.add(group);
    }

    // Restore the names of the components in each button group.
    String[][] bgNames = (String[][])(map.get(BUTTON_GROUP_TOOL_NAMES));

    // Restore tool names.
    String[] names = (String [])(map.get(TOOL_NAMES));

    // Restore tool information.
    ToolInfo[] info = (ToolInfo [])(map.get(TOOL_INFO));

    // Restore leading separation.
    leadingSeparation = (Dimension)(map.get(LEADING_SEPARATION));

    // Restore border.
    BorderDescriptor bd = (BorderDescriptor)(map.get(BORDER));
    if (bd != null) {
      setBorder(bd.getBorder());
    }

    // Restore separation.
    separation = (Dimension)(map.get(SEPARATION));

    // Restore "centered" property.
    centered = map.get(CENTERED, false);

    // Restore "border painted" property.
    setBorderPainted(map.get(BORDER_PAINTED, true));

    // Restore "floatable" property.
    setFloatable(map.get(FLOATABLE, true));

    // Restore name.
    setName(map.get(NAME, (String)null));

    // Restore sizes.
    Dimension size;
    size = map.get(MINIMUM_SIZE, (Dimension)null);
    if (size != null) {
      setMinimumSize(size);
    }
    size = map.get(MAXIMUM_SIZE, (Dimension)null);
    if (size != null) {
      setMaximumSize(size);
    }
    size = map.get(PREFERRED_SIZE, (Dimension)null);
    if (size != null) {
      setPreferredSize(size);
    }

    // Restore "modified" property.
    modified = map.get(MODIFIED, false);

    // Restore "menu enabled" property.
    menuEnabled = map.get(MENU_ENABLED, false);

    // Place tools in visual groups.
    for (int i=0, c=0; i<nVGroups; i++) {
      VisualGroup group = vGroups.get(i);
      int nCompos = groupSizes[i];
      for (int j=0; j<nCompos; j++) {
        ESlateHandle child = h.getChildHandle(names[c]);
        if (child != null) {
          Component comp = (Component)(child.getComponent());
          group.add(comp);
          toolInfo.put(comp, info[c]);
        }
        c++;
      }
    }

    // Place tools in button groups.
    for (int i=0; i<nBGroups; i++) {
      ESlateButtonGroup group = bGroups.get(i);
      n = bgNames[i].length;
      for (int j=0; j<n; j++) {
        ESlateHandle toolH = handle.getChildHandle(bgNames[i][j]);
        if (toolH != null) {
          AbstractButton ab = (AbstractButton)(toolH.getComponent());
          group.add(ab);
        }else{
          System.err.println("** Can't find tool " + bgNames[i][j]);
        }
      }
    }

    // Restore dynamic borders property. Do this after placing the tools in
    // the toolbar, so that setDynamicBorders() will be able to find them.
    boolean dynBorders = map.get(DYNAMIC_BORDERS, true);
    setDynamicBorders(dynBorders);

    // Make the toolbar a component listener for changes in the tools.
    Component[] tools = getTools();
    n = tools.length;
    for (int i=0; i<n; i++) {
      tools[i].addComponentListener(this);
    }

    layoutChanged = true;
    created = true;

    pm.stop(loadTimer);
    pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
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
   * E-Slate specific initializations.
   */
  private void eSlateInit()
  {
    //handle = ESlate.registerPart(this);

    // Register supported Logo primitive group.
    handle.addPrimitiveGroup(
      "gr.cti.eslate.scripting.logo.ToolBarPrimitives"
    );

    handle.setInfo(getInfo());

    try {
      handle.setUniqueComponentName(resources.getString("name"));
    } catch (RenamingForbiddenException e) {
    }

    //add(handle.getMenuPanel(), BorderLayout.NORTH);

    handle.addESlateListener(new ESlateAdapter() {
      public void handleDisposed(HandleDisposalEvent e)
      {
        dispose();
      }
    });
  }

  /**
   * Returns the component's E-Slate handle. The first time this method is
   * called, all E-Slate specific initializations take place.
   * @return    The requested handle. If the component's constructor has not
   *            been called, this method returns null.
   */
  public ESlateHandle getESlateHandle()
  {
    if (handle == null) {
      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      pm.eSlateAspectInitStarted(this);
      pm.init(initESlateAspectTimer);

      handle = ESlate.registerPart(this);

      eSlateInit();

      pm.stop(initESlateAspectTimer);
      pm.eSlateAspectInitEnded(this);
      pm.displayTime(initESlateAspectTimer, handle, "", "ms");
    }
    return handle;
  }

  /**
   * Creates a new visual group and adds it to the toolbar.
   * @return    The created visual group.
   */
  public VisualGroup addVisualGroup()
  {
    String name = vNames.constructUniqueName(resources.getString("group"));
    VisualGroup vg = new VisualGroup(name);
    try {
      vNames.bind(name, vg);
    } catch (NameServiceException nse) {
    }
    vGroups.add(vg);
    modified = true;
    return vg;
  }

  /**
   * Creates a new visual group and adds it to the toolbar.
   * @param     name    The name of the visual group.
   * @return    The created visual group.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   * @exception NameUsedException       Thrown if a visual group having this
   *                                    name already exists.
   */
  public VisualGroup addVisualGroup(String name)
    throws NullPointerException, NameUsedException
  {
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    Object old = null;
    try {
      old = vNames.lookup(name);
    } catch (NameServiceException nse) {
    }
    if (old != null) {
      throw new NameUsedException(resources.getString("groupExists"));
    }
    VisualGroup vg = new VisualGroup(name);
    try {
      vNames.bind(name, vg);
    } catch (NameServiceException nse) {
    }
    vGroups.add(vg);
    modified = true;
    return vg;
  }

  /**
   * Creates a new visual group and adds it to the toolbar. The suggested name
   * for the group may be modified, by appending a "_" and a number, to ensure
   * that the name is unique among the visual groups of the toolbar.
   * @param     name    The suggested name of the visual group.
   * @return    The created visual group.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   */
  public VisualGroup addUniqueVisualGroup(String name)
    throws NullPointerException
  {
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    name = vNames.constructUniqueName(name);
    VisualGroup vg = new VisualGroup(name);
    try {
      vNames.bind(name, vg);
    } catch (NameServiceException nse) {
    }
    vGroups.add(vg);
    modified = true;
    return vg;
  }

  /**
   * Removes a visual group from the toolbar. Any components contained in the
   * group will be removed from the toolbar.
   * @param     group   The visual group to remove.
   * @return    True if the group was removed, false otherwise.
   */
  public boolean remove(VisualGroup group)
  {
    if (group != null) {
      String name = group.getName();
      VisualGroup vg = null;
      try {
        vg = (VisualGroup)(vNames.lookup(name));
      } catch (NameServiceException nse) {
      }
      if (vg != null) {
        ComponentBaseArray components = vg.components;
        int n = components.size();
        for (int i=n-1; i>=0; i--) {
          remove(vg, components.get(i));
        }
        try {
          vNames.unbind(name);
        } catch (NameServiceException nse) {
        }
        layoutChanged = true;
        modified = true;
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the number of visual groups in the toolbar.
   * @return    The number of visual groups in the tool bar.
   */
  public int getVisualGroupCount()
  {
    return vGroups.size();
  }

  /**
   * Returns the visual groups in the toolbar.
   * @return    An array containing the visual groups in the toolbar.
   */
  public VisualGroup[] getVisualGroups()
  {
    return vGroups.toArray();
  }

  /**
   * Returns a visual group by its position.
   * @param     i       The position of the visual group.
   * @return    The <code>i</code>-th visual group.
   * @exception ArrayIndexOutOfBoundsException  Thrown if there is no such
   *                                            visual group.
   */
  public VisualGroup getVisualGroup(int i)
  {
    return vGroups.get(i);
  }

  /**
   * Returns a visual group by its name.
   * @param     name    The name of the visual group.
   * @return    The visual group that has the given name, or <code>null</code>
   *            if no such component exists.
   */
  public VisualGroup getVisualGroup(String name)
  {
    VisualGroup vg;
    try {
      vg = (VisualGroup)(vNames.lookup(name));
    } catch (NameServiceException nse) {
      vg = null;
    }
    return vg;
  }

  /**
   * Renames a visual group.
   * @param     group   The group to rename.
   * @param     name    The new name of the group.
   * @exception NullPointerException    Thrown if <code>group</code> or
   *                    <code>name</code> is <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   * @exception NameUsedException       Thrown if another visual group having
   *                    this name already exists.
   */
  public void setName(VisualGroup group, String name)
    throws NullPointerException, IllegalArgumentException, NameUsedException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    if (!vGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    String oldName = group.getName();
    if (!ESlateStrings.areEqualIgnoreCase(name, oldName, Locale.getDefault())) {
      Object o = null;
      try {
        o = vNames.lookup(name);
      } catch (NameServiceException e) {
      }
      if (o != null) {
        throw new NameUsedException(resources.getString("groupExists"));
      }
    }
    try {
      vNames.rename(oldName, name);
    } catch (NameServiceException nse) {
    }
    group.setName(name);
    modified = true;
  }

  /**
   * Renames a visual group.  The suggested name for the group may be
   * modified, by appending a "_" and a number, to ensure that the name
   * is unique among the visual groups of the toolbar.
   * @param     group   The group to rename.
   * @param     name    The suggested new name of the group.
   * @exception NullPointerException    Thrown if <code>group</code> or
   *                    <code>name</code> is <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   */
  public void setUniqueName(VisualGroup group, String name)
    throws NullPointerException, IllegalArgumentException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    if (!vGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    String oldName = group.getName();
    try {
      vNames.unbind(oldName);
    } catch (NameServiceException e) {
    }
    name = vNames.constructUniqueName(name);
    try {
      vNames.bind(name, group);
    } catch (NameServiceException nse) {
    }
    group.setName(name);
    modified = true;
  }

  /**
   * Sets the value of the separation between components in a visual group.
   * @param     group   The visual group.
   * @param     sep     The value of the separation between components in the
   *                    visual group. Use <code>null</code> to specify that a
   *                    default value should be used.
   * @exception NullPointerException    Thrown if <code>group</code> is
   *                    <code>null</code>.
   */
  public void setSeparation(VisualGroup group, Dimension sep)
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    group.setSeparation(sep);
    layoutChanged = true;
    repaint();
    modified = true;
  }

  /**
   * Retuns the value of the separation between components in a visual group.
   * @param     group   The visual group.
   * @return    The value of the separation between components in the visual
   *            group. A <code>null</code> return value signifies that a
   *            default value is being used.
   * @exception NullPointerException    Thrown if <code>group</code> is
   *                    <code>null</code>.
   */
  public Dimension getSeparation(VisualGroup group)
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    return group.getSeparation();
  }

  /**
   * Sets the value of the separation between visual groups.
   * @param     sep     The value of the separation between visual groups.
   *                    Use <null>code</code> to specify that a default value
   *                    should be used.
   */
  public void setSeparation(Dimension sep)
  {
    separation = sep;
    layoutChanged = true;
    repaint();
    modified = true;
  }

  /**
   * Returns the value of the separation between visual groups.
   * @return    The value of the separation between visual groups.
   *            A <code>null</code> return value signifies that a default
   *            value is being used.
   */
  public Dimension getSeparation()
  {
    return separation;
  }

  /**
   * Specifies the size of the separation between the toolbar and the first
   * displayed tool.
   * @param     sep     The value of the separation between the toolbar and
   *                    the first displayed tool. Use <null>code</code> to
   *                    specify that a default value should be used.
   */
  public void setLeadingSeparation(Dimension sep)
  {
    leadingSeparation = sep;
    layoutChanged = true;
    repaint();
    modified = true;
  }

  /**
   * Returns the size of the separation between the toolbar and the first
   * displayed tool.
   * @return    The size of the separation between the toolbar and the first
   *            displayed tool.
   *            A <code>null</code> return value signifies that a default
   *            value is being used.
   */
  public Dimension getLeadingSeparation()
  {
    return leadingSeparation;
  }

  /**
   * Creates a new button group and adds it to the toolbar.
   * @return    The created button group.
   */
  public ESlateButtonGroup addButtonGroup()
  {
    String name = bNames.constructUniqueName(resources.getString("group"));
    ESlateButtonGroup bg = new ESlateButtonGroup(name);
    try {
      bNames.bind(name, bg);
    } catch (NameServiceException nse) {
    }
    bGroups.add(bg);
    modified = true;
    return bg;
  }

  /**
   * Creates a new button group and adds it to the toolbar.
   * @param     name    The name of the button group.
   * @return    The created button group.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   * @exception NameUsedException       Thrown if a button group having this
   *                                    name already exists.
   */
  public ESlateButtonGroup addButtonGroup(String name)
    throws NullPointerException, NameUsedException
  {
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    Object old = null;
    try {
      old = bNames.lookup(name);
    } catch (NameServiceException nse) {
    }
    if (old != null) {
      throw new NameUsedException(resources.getString("groupExists"));
    }
    ESlateButtonGroup bg = new ESlateButtonGroup(name);
    try {
      bNames.bind(name, bg);
    } catch (NameServiceException nse) {
    }
    bGroups.add(bg);
    modified = true;
    return bg;
  }

  /**
   * Creates a new button group and adds it to the toolbar. The suggested name
   * for the group may be modified, by appending a "_" and a number, to ensure
   * that the name is unique among the button groups of the toolbar.
   * @param     name    The suggested name of the button group.
   * @return    The created button group.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   */
  public ESlateButtonGroup addUniqueButtonGroup(String name)
    throws NullPointerException
  {
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    name = bNames.constructUniqueName(name);
    ESlateButtonGroup bg = new ESlateButtonGroup(name);
    try {
      bNames.bind(name, bg);
    } catch (NameServiceException nse) {
    }
    bGroups.add(bg);
    modified = true;
    return bg;
  }

  /**
   * Adds a button group to the toolbar.
   * @param     bg      The button group to add to the toolbar.
   * @exception NullPointerException    Thrown if <code>bg</code> is
   *                                    <code>null</code>.
   */
  public void addButtonGroup(ESlateButtonGroup bg) throws NullPointerException
  {
    if (bg == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (!bGroups.contains(bg)) {
      String name = bNames.constructUniqueName(resources.getString("group"));
      try {
        bNames.bind(name, bg);
      } catch (NameServiceException nse) {
      }
      bGroups.add(bg);
      bg.name = name;
      modified = true;
    }
  }

  /**
   * Adds a button group to the toolbar.
   * @param     bg      The button group to add to the toolbar.
   * @param     name    The name of the button group.
   * @exception NullPointerException    Thrown if <code>name</code> or
   *                                    <code>bg</code> is <code>null</code>.
   * @exception NameUsedException       Thrown if a button group having this
   *                                    name already exists.
   */
  public void addButtonGroup(ESlateButtonGroup bg, String name)
    throws NullPointerException, NameUsedException
  {
    if (bg == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    if (!bGroups.contains(bg)) {
      Object old = null;
      try {
        old = bNames.lookup(name);
      } catch (NameServiceException nse) {
      }
      if (old != null) {
        throw new NameUsedException(resources.getString("groupExists"));
      }
      try {
        bNames.bind(name, bg);
      } catch (NameServiceException nse) {
      }
      bGroups.add(bg);
      bg.name = name;
      modified = true;
    }
  }

  /**
   * Adds a button group to the toolbar. The suggested name
   * for the group may be modified, by appending a "_" and a number, to ensure
   * that the name is unique among the button groups of the toolbar.
   * @param     bg      The button group to add to the toolbar.
   * @param     name    The suggested name of the button group.
   * @exception NullPointerException    Thrown if <code>name</code> or
   *                                    <code>bg</code> is <code>null</code>.
   */
  public void addUniqueButtonGroup(ESlateButtonGroup bg, String name)
    throws NullPointerException
  {
    if (bg == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    if (!bGroups.contains(bg)) {
      name = bNames.constructUniqueName(name);
      try {
        bNames.bind(name, bg);
      } catch (NameServiceException nse) {
      }
      bGroups.add(bg);
      bg.name = name;
      modified = true;
    }
  }

  /**
   * Removes a button group from the toolbar.
   * @param     group   The button group to remove.
   * @return    True if the group was removed, false otherwise.
   */
  public boolean remove(ESlateButtonGroup group)
  {
    if (group != null) {
      String name = group.getName();
      Object o = null;
      try {
        bNames.lookup(name);
      } catch (NameServiceException nse) {
      }
      if (o != null) {
        try {
          bNames.unbind(name);
        } catch (NameServiceException nse) {
        }
        bGroups.removeElements(group);
        layoutChanged = true;
        modified = true;
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the number of button groups in the toolbar.
   * @return    The number of button groups in the tool bar.
   */
  public int getButtonGroupCount()
  {
    return bGroups.size();
  }

  /**
   * Returns the button groups in the toolbar.
   * @return    An array containing the button groups in the toolbar.
   */
  public ESlateButtonGroup[] getButtonGroups()
  {
    return bGroups.toArray();
  }

  /**
   * Returns a button group by its position.
   * @param     i       The position of the button group.
   * @return    The <code>i</code>-th button group.
   * @exception ArrayIndexOutOfBoundsException  Thrown if there is no such
   *                                            button group.
   */
  public ESlateButtonGroup getButtonGroup(int i)
  {
    return bGroups.get(i);
  }

  /**
   * Returns a button group by its name.
   * @param     name    The name of the button group.
   * @return    The button group that has the given name, or <code>null</code>
   *            if no such component exists.
   */
  public ESlateButtonGroup getButtonGroup(String name)
  {
    ESlateButtonGroup bg;
    try {
      bg = (ESlateButtonGroup)(bNames.lookup(name));
    } catch (NameServiceException nse) {
      bg = null;
    }
    return bg;
  }

  /**
   * Returns the button group containing a given button.
   * @param     button  The button.
   * @return    The button group containing the button. If bo group contains
   *            the button, <code>null</code> is returned.
   */
  ESlateButtonGroup getButtonGroup(AbstractButton button)
  {
    int n = bGroups.size();
    for (int i=0; i<n; i++) {
      ESlateButtonGroup group = bGroups.get(i);
      if (group.contains(button)) {
        return group;
      }
    }
    return null;
  }

  /**
   * Renames a button group.
   * @param     group   The group to rename.
   * @param     name    The new name of the group.
   * @exception NullPointerException    Thrown if <code>group</code> or
   *                    <code>name</code> is <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the button group
   *                    does not belong to this toolbar.
   * @exception NameUsedException       Thrown if another button group having
   *                    this name already exists.
   */
  public void setName(ESlateButtonGroup group, String name)
    throws NullPointerException, IllegalArgumentException, NameUsedException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    if (!bGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    String oldName = group.getName();
    if (!ESlateStrings.areEqualIgnoreCase(name, oldName, Locale.getDefault())) {
      Object o = null;
      try {
        o = bNames.lookup(name);
      } catch (NameServiceException e) {
      }
      if (o != null) {
        throw new NameUsedException(resources.getString("groupExists"));
      }
    }
    try {
      bNames.rename(oldName, name);
    } catch (NameServiceException nse) {
    }
    group.setName(name);
    modified = true;
  }

  /**
   * Renames a button group.  The suggested name for the group may be
   * modified, by appending a "_" and a number, to ensure that the name
   * is unique among the button groups of the toolbar.
   * @param     group   The group to rename.
   * @param     name    The suggested new name of the group.
   * @exception NullPointerException    Thrown if <code>group</code> or
   *                    <code>name</code> is <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the button group
   *                    does not belong to this toolbar.
   */
  public void setUniqueName(ESlateButtonGroup group, String name)
    throws NullPointerException, IllegalArgumentException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    if (!bGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    String oldName = group.getName();
    try {
      bNames.unbind(oldName);
    } catch (NameServiceException e) {
    }
    name = bNames.constructUniqueName(name);
    try {
      bNames.bind(name, group);
    } catch (NameServiceException nse) {
    }
    group.setName(name);
    modified = true;
  }

  /**
   * Redo the layout of the toolbar after its contents are modified.
   */
  private void relayout()
  {
    super.removeAll();
    //removeComponentListeners();
    int orientation = getOrientation();

    int nGroups = vGroups.size();
    boolean needGroupSeparator = false;
    boolean first = true;
    if (centered) {
      if (orientation == HORIZONTAL) {
        addAwtComponent(Box.createHorizontalGlue());
      }else{
        addAwtComponent(Box.createVerticalGlue());
      }
    }
    for (int vGroup=0; vGroup<nGroups; vGroup++) {
      VisualGroup group = vGroups.get(vGroup);
      if (group.isVisible()) {
        int nComponents = group.getComponentCount();
        int nDrawn = 0;
        for (int c=0; c<nComponents; c++) {
          Component comp = group.getComponent(c);
          if (isVisible(comp)) {
            if (nDrawn == 0 && needGroupSeparator) {
              addSeparator(separation, orientation);
              needGroupSeparator = false;
            }
            if (nDrawn > 0) {
              addSeparator(group.separation, orientation);
            }
            if (first && !centered) {
              addSeparator(leadingSeparation, orientation);
              first = false;
            }
            //revalidate(comp);
            addAwtComponent(comp);
            nDrawn++;
          }
          if (nDrawn > 0) {
            needGroupSeparator = true;
          }
        }
      }
    }
    if (centered) {
      if (orientation == HORIZONTAL) {
        addAwtComponent(Box.createHorizontalGlue());
      }else{
        addAwtComponent(Box.createVerticalGlue());
      }
    }

    super.revalidate();
    actualPrefSize = super.getPreferredSize();

/*
    // FIXME: scrolling is very buggy, so we might as well remove it. Either
    // fix it, or wait until Sun implement this feature themselves (see
    // http://developer.java.sun.com/developer/bugParade/bugs/4314200.html).

    haveFdButton = false;
    haveBkButton = false;
    if (!toolsFit()) {
      removeComponentsFromEnd();
    }
*/

    layoutChanged = false;
    //addComponentListeners();
  }

  /**
   * Removes components from the end of the toolbar, until what's left fits.
   * The removed components are replaced by a scroll button.
   */
  private void removeComponentsFromEnd()
  {
    int n = getComponentCount();
    if (fdButton == null) {
      fdButton = getFdButton();
    }
    haveFdButton = true;
    int first;
    if (haveBkButton) {
      first = 1;
    }else{
      first = 0;
    }
    for (int i=n-1; i>=first; i--) {
      Component c = super.getComponent(i);
      fdComponents.add(c);
      super.remove(i);
      super.revalidate();
      if (scrolledToolsFit()) {
        break;
      }
    }
    n = super.getComponentCount();
    super.addImpl(fdGlue, null, n);
    super.addImpl(fdButton, null, n+1);
    super.revalidate();
  }

//  /**
//   * Checks if all the tools fit in the toolbar.
//   * @return    True if yes, false otherwise.
//   */
//  private boolean toolsFit()
//  {
//    if(getParent() == null) {
//      // Until the toolbar is added to its parent, its size is invalid, so we
//      // can't check.
//      return true;
//    }
//    Dimension size = getSize();
//    int orientation = getOrientation();
//    switch (orientation) {
//      case HORIZONTAL:
//        int buttonWidth = 0;
//        if (haveBkButton) {
//          buttonWidth += bkButton.getPreferredSize().width;
//        }
//        if (haveFdButton) {
//          buttonWidth +=
//            (fdButton.getPreferredSize().width +
//             fdGlue.getPreferredSize().width);
//        }
//        return size.width >= (buttonWidth + actualPrefSize.width);
///*
//        return size.width >= actualPrefSize.width;
//*/
//      case VERTICAL:
//      default:
//        int buttonHeight = 0;
//        if (haveBkButton) {
//          buttonHeight += bkButton.getPreferredSize().height;
//        }
//        if (haveFdButton) {
//          buttonHeight +=
//            (fdButton.getPreferredSize().height +
//             fdGlue.getPreferredSize().height);
//        }
//        return size.height >= (buttonHeight + actualPrefSize.height);
///*
//        return size.height >= actualPrefSize.height;
//*/
//    }
//  }

  /**
   * Checks if all the tools, minus those that have been scrolled to the left
   * fit in the toolbar.
   * @return    True if yes, false otherwise.
   */
  private boolean scrolledToolsFit()
  {
    int orientation = getOrientation();
    Dimension size = getSize();
    switch (orientation) {
      case HORIZONTAL:
        int buttonWidth = 0;
        if (haveBkButton) {
          buttonWidth += bkButton.getPreferredSize().width;
        }
        if (haveFdButton) {
          buttonWidth +=
            (fdButton.getPreferredSize().width +
             fdGlue.getPreferredSize().width);
        }
        return size.width >= (buttonWidth + super.getPreferredSize().width);
/*
        return size.width >= super.getPreferredSize().width;
*/
      case VERTICAL:
      default:
        int buttonHeight = 0;
        if (haveBkButton) {
          buttonHeight += bkButton.getPreferredSize().height;
        }
        if (haveFdButton) {
          buttonHeight +=
            (fdButton.getPreferredSize().height +
             fdGlue.getPreferredSize().height);
        }
        return size.height >= (buttonHeight + super.getPreferredSize().height);
/*
        return size.height >= super.getPreferredSize().height;
*/
    }
  }

/*
  private void removeComponentListeners()
  {
    int nGroups = vGroups.size();
    for (int vGroup=0; vGroup<nGroups; vGroup++) {
      VisualGroup group = vGroups.get(vGroup);
      int nComponents = group.getComponentCount();
      for (int c=0; c<nComponents; c++) {
        Component comp = group.getComponent(c);
        comp.removeComponentListener(this);
      }
    }
  }

  private void addComponentListeners()
  {
    int nGroups = vGroups.size();
    for (int vGroup=0; vGroup<nGroups; vGroup++) {
      VisualGroup group = vGroups.get(vGroup);
      int nComponents = group.getComponentCount();
      boolean groupVisible = group.isVisible();
      for (int c=0; c<nComponents; c++) {
        Component comp = group.getComponent(c);
        if (groupVisible && isVisible(comp) &&
            !fdComponents.contains(comp) && !bkComponents.contains(comp)) {
          comp.addComponentListener(this);
        }
      }
    }
  }
*/

  /**
   * Revalidates a component.
   * @param     c       The component to revalidate.
   */
  private static void revalidate(Component c)
  {
    if (c instanceof JComponent) {
      ((JComponent)c).revalidate();
    }else{
      c.invalidate();
      c.validate();
    }
  }

  /**
   * Returns the "scroll forward" button.
   * @return    The "scroll forward" button. The button is created, if
   * necessary.
   */
  private ScrollButton getFdButton()
  {
    if (fdButton == null) {
      fdButton = new ScrollButton(this, ScrollButton.FORWARD);
      if (getOrientation() == HORIZONTAL) {
        fdGlue = Box.createHorizontalGlue();
      }else{
        fdGlue = Box.createVerticalGlue();
      }
    }
    return fdButton;
  }

  /**
   * Returns the "scroll back" button.
   * @return    The "scroll back" button. The button is created, if
   * necessary.
   */
  private ScrollButton getBkButton()
  {
    if (bkButton == null) {
      bkButton = new ScrollButton(this, ScrollButton.BACK);
    }
    return bkButton;
  }

  /**
   * Scrolls the toolbar by one AWT component.
   * @param     direction       One of <code>ScrollButton.FORWARD</code>,
   *                            <code>ScrollButton.BACK</code>.
   */
  void scroll(int direction)
  {
    synchronized(scrolling) {
      if (scrolling.booleanValue()) {
        return;
      }else{
        scrolling = Boolean.TRUE;
      }
    }

    //removeComponentListeners();

    if (direction == ScrollButton.FORWARD) {
      int n = fdComponents.size();
      if (n > 0) {
        int nComponents = getComponentCount();
        if (haveFdButton) {
          // Remove "scroll forward" button and preceding glue.
          // It will be restored by removeComponentsFromEnd(), if necessary.
          super.remove(nComponents-1);
          super.remove(nComponents-2);
          nComponents -= 2;
          haveFdButton = false;
        }
        if (!haveBkButton) {
          bkButton = getBkButton();
          super.addImpl(bkButton, null, 0);
          haveBkButton = true;
          nComponents++;
        }
        if (nComponents > 1) {
          Component c = super.getComponent(1);
          //revalidate(c);
          bkComponents.add(c);
          super.remove(1);
        }
        for (int i=n-1; i>=0; i--) {
          Component comp = fdComponents.get(i);
          //revalidate(comp);
          super.addImpl(comp, null, -1);
        }
        super.revalidate();
        fdComponents.clear();
        if (!scrolledToolsFit()) {
          removeComponentsFromEnd();
        }else{
          fdButton.cleanup();
        }
      }
    }else{
      int n = bkComponents.size();
      if (n > 0) {
        int nComponents = getComponentCount();
        if (haveFdButton) {
          // Remove "scroll forward" button and preceding glue.
          // It will be restored by removeComponentsFromEnd(), if necessary.
          super.remove(nComponents-1);
          super.remove(nComponents-2);
          nComponents -= 2;
          haveFdButton = false;
        }
        int comp = n - 1;
        Component c = bkComponents.get(comp);
        bkComponents.remove(comp);
        //revalidate(c);
        super.addImpl(c, null, 1);
        if (comp == 0) {
          super.remove(0);
          bkButton.cleanup();
          haveBkButton = false;
        }
        int nFd = fdComponents.size();
        for (int i=nFd-1; i>=0; i--) {
          Component compo = fdComponents.get(i);
          //revalidate(compo);
          super.addImpl(compo, null, -1);
        }
        super.revalidate();
        removeComponentsFromEnd();
      }
    }
    repaint();
    scrolling = Boolean.FALSE;
    //addComponentListeners();
  }

  /**
   * Adds a separator at the end of the toolbar, by invoking an appropriate
   * <code>JToolBar</code> method.
   * @param     d       The size of the separator. If <code>d</code> is
   *                    <code>null</code>, then a default separator is added.
   *                    If the width or height of the separator, according to
   *                    whether the toolbar's orientation is
   *                    <code>HORIZONTAL</code> or <code>VERTICAL</code>
   *                    respectively, is zero, then no separator will be
   *                    added.
   * @param     orientation     One of <code>HORIZONTAL</code> or
   *                    <code>VERTICAL</code>.
   */
  private void addSeparator(Dimension d, int orientation)
  {
    JToolBar.Separator s = null;
    if (d == null) {
      s = new JToolBar.Separator();
    }else{
      // Ignore non-positive separations.
      if (((orientation == HORIZONTAL) && (d.width > 0)) ||
          ((orientation == VERTICAL) && (d.height > 0))) {
        s = new JToolBar.Separator(d);
      }
    }
    if (s != null) {
      if (orientation == HORIZONTAL) {
        s.setOrientation(SwingConstants.VERTICAL);
      }else{
        s.setOrientation(SwingConstants.HORIZONTAL);
      }
      addAwtComponent(s);
    }
  }

  /**
   * 
   */
   public void addSeparator() throws Error
   {
     addSeparator((Dimension)null);
   }

   public void addSeparator(Dimension d) throws Error
   {
     System.out.println(resources.getString("noAddSeparator"));
   }

  /**
   * Adds an AWT component at the end of the toolbar, by invoking an
   * appropriate <code>JToolBar</code> method.
   * @param     c       The component to add.
   */
  private void addAwtComponent(Component c)
  {
    // AddImpl() is the basic method for adding components in the Container
    // class. We can't use add(Component), as it will invoke addImpl(),
    // creating an infinite loop.
    super.addImpl(c, null, -1);
  }

  /**
   * Revalidates the component.
   */
  public void revalidate()
  {
    layoutChanged = true;
    super.revalidate();
  }

  /**
   * Free resources. After invoking this method, the component is unusable.
   */
  public void dispose()
  {
    if (handle != null) {
      handle.dispose();
      handle = null;
    }
    int n = tbListeners.size();
    for (int i=0; i<n; i++) {
      removeToolBarListener(tbListeners.get(i));
    }
    tbListeners = null;
    super.removeAll();
    resources = null;
    vGroups.clear();
    vGroups = null;
    vNames.dispose();
    vNames = null;
    bGroups.clear();
    bGroups = null;
    bNames.dispose();
    bNames = null;
    separation = null;
    toolInfo = null;
    leadingSeparation = null;
    fdButton = null;
    bkButton = null;
    fdGlue = null;
    fdComponents.clear();
    fdComponents = null;
    bkComponents.clear();
    bkComponents = null;

    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.removePerformanceListener(perfListener);
    perfListener = null;
  }

  /**
   * Sets the orientation of the toolbar. The orientation must have either the
   * value <code>HORIZONTAL</code> or <code>VERTICAL</code>. If
   * <code>orientation</code> is an invalid value, an exception will be thrown.
   * @param     o       The new orientation -- either <code>HORIZONTAL</code>
   *                    or <code>VERTICAL</code>
   */
  public void setOrientation(int o)
  {
    if (o != getOrientation()) {
      super.setOrientation(o);
      layoutChanged = true;
      fdButton = null;
      bkButton = null;
      fdGlue = null;
      repaint();
      modified = true;
    }
  }

  /**
   * Adds to a visual group a new <code>JButton</code> which dispatches the
   * action.
   * As with <code>JToolBar.add(Action)</code>, this is not the preferred
   * method for adding <code>Action</code>s to a container. Instead it is
   * recommended to configure a control with an action using
   * <code>setAction</code>, and then add that control directly to the
   * container.
   * @param     group   The visual group to which to add the
   *                    <code>JButton</code>.
   * @param     a       The <code>Action</code> object to add as a new button.
   * @exception NullPointerException    Thrown if <code>group</code> is
   *                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   */
  public JButton add(VisualGroup group, Action a)
    throws NullPointerException, IllegalArgumentException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (!vGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    JButton b = createActionComponent(a);
    group.add(b);
    registerComponent(b);
    layoutChanged = true;
    modified = true;
    return b;
  }

  /**
   * Adds to a visual group a new <code>JButton</code> which dispatches the
   * action.
   * As with <code>JToolBar.add(Action)</code>, this is not the preferred
   * method for adding <code>Action</code>s to a container. Instead it is
   * recommended to configure a control with an action using
   * <code>setAction</code>, and then add that control directly to the
   * container.
   * @param     group   The visual group to which to add the
   *                    <code>JButton</code>.
   * @param     a       The <code>Action</code> object to add as a new button.
   * @param     name    The name to give to the button.
   * @exception NullPointerException    Thrown if <code>group</code> is
   *                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   */
  public JButton add(VisualGroup group, Action a, String name)
    throws NullPointerException, IllegalArgumentException
  {
    JButton b = add(group, a);
    try {
      getESlateHandle(b).setUniqueComponentName(name);
    } catch (RenamingForbiddenException e) {
    }
    modified = true;
    return b;
  }

  /**
   * Adds a component to a visual group.
   * @param     group   The visual group to which to add the component.
   * @param     c       The component to add.
   * @return    The component argument.
   * @exception NullPointerException    Thrown if <code>group</code> or
   *                    <code>c</code> is <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   */
  public Component add(VisualGroup group, Component c)
    throws NullPointerException, IllegalArgumentException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (c == null) {
      throw new NullPointerException(resources.getString("nullComponent"));
    }
    if (!vGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    group.add(c);
    registerComponent(c);
    layoutChanged = true;
    modified = true;
    return c;
  }

  /**
   * Adds a component to a visual group.
   * @param     group   The visual group to which to add the component.
   * @param     c       The component to add.
   * @param     name    The name to give to the component.
   * @return    The component argument.
   * @exception NullPointerException    Thrown if <code>group</code> or
   *                    <code>c</code> is <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   */
  public Component add(VisualGroup group, Component c, String name)
    throws NullPointerException, IllegalArgumentException
  {
    c = add(group, c);
    try {
      getESlateHandle(c).setUniqueComponentName(name);
    } catch (RenamingForbiddenException e) {
    }
    modified = true;
    return c;
  }

  /**
   * Adds a component to a visual group.
   * @param     group   The visual group to which to add the component.
   * @param     c       The component to add.
   * @param     i       The position in the visual group at which to add the
   *                    component.
   * @return    The component argument.
   * @exception NullPointerException    Thrown if <code>group</code> or
   *                    <code>c</code> is <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>i</code> has
   *                                            an illegal value
   */
  public Component add(VisualGroup group, Component c, int i)
    throws NullPointerException, IllegalArgumentException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (c == null) {
      throw new NullPointerException(resources.getString("nullComponent"));
    }
    if (!vGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    group.add(i, c);
    registerComponent(c);
    layoutChanged = true;
    modified = true;
    return c;
  }

  /**
   * Adds a component to a visual group.
   * @param     group   The visual group to which to add the component.
   * @param     c       The component to add.
   * @param     i       The position in the visual group at which to add the
   *                    component.
   * @param     name    The name to give to the component.
   * @return    The component argument.
   * @exception NullPointerException    Thrown if <code>group</code> or
   *                    <code>c</code> is <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>i</code> has
   *                                            an illegal value
   */
  public Component add(VisualGroup group, Component c, int i, String name)
    throws NullPointerException, IllegalArgumentException
  {
    c = add(group, c, i);
    try {
      getESlateHandle(c).setUniqueComponentName(name);
    } catch (RenamingForbiddenException e) {
    }
    modified = true;
    return c;
  }

  /**
   * Removes a component from a visual group.
   * @param     group   The visual group from which to remove the component.
   * @param     c       The component to remove.
   * @exception NullPointerException    Thrown if <code>group</code> or
   *                    <code>c</code> is <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   */
  public void remove(VisualGroup group, Component c)
    throws NullPointerException, IllegalArgumentException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (c == null) {
      throw new NullPointerException(resources.getString("nullComponent"));
    }
    if (!vGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    group.remove(c);
    unregisterComponent(c);
    modified = true;
    layoutChanged = true;
  }

  /**
   * Removes a component from a visual group.
   * @param     group   The visual group from which to remove the component.
   * @param     i       The position in the visual group of the component to
   *                    be removed.
   * @exception NullPointerException    Thrown if <code>group</code> is
   *                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>i</code> has
   *                                            an illegal value
   */
  public void remove(VisualGroup group, int i)
    throws NullPointerException, IllegalArgumentException,
           ArrayIndexOutOfBoundsException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (!vGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    Component c = group.getComponent(i);
    group.remove(i);
    unregisterComponent(c);
    modified = true;
    layoutChanged = true;
  }

  /**
   * Adds to the toolbar a new <code>JButton</code> which dispatches the
   * action.
   * The button will be added at the end of the last visual group on the
   * toolbar; if the toolbar contains no visual groups, one will be created.
   * As with <code>JToolBar.add(Action)</code>, this is not the preferred
   * method for adding <code>Action</code>s to a container. Instead it is
   * recommended to configure a control with an action using
   * <code>setAction</code>, and then add that control directly to the
   * container.
   * <EM>Note:</EM> This method is provided mainly for compatibility with
   * <code>JToolBar</code>.
   * @param     a       The <code>Action</code> object to add as a new button.
   */
  public JButton add(Action a)
  {
    JButton b = add(getLastVisualGroup(), a);
    modified = true;
    return b;
  }

  /**
   * Adds a component to the toolbar.
   * The component will be added at the end of the last visual group on the
   * toolbar; if the toolbar contains no visual groups, one will be created.
   * <EM>Note:</EM> This method is provided mainly for compatibility with
   * <code>JToolBar</code>.
   * @param     c       The component to add.
   * @exception NullPointerException    Thrown if <code>c</code> is
   *                                    <code>null</code>.
   */
  public Component add(Component c) throws NullPointerException
  {
    Component comp = add(getLastVisualGroup(), c);
    modified = true;
    return comp;
  }

  /**
   * Adds a component to the toolbar at the specified index. If a
   * <code>JButton</code> is being added, it is initially set to be disabled.
   * <EM>Note:</EM> This method is provided mainly for compatibility with
   * <code>JToolBar</code>.
   */
  protected void addImpl(Component comp, Object constraints, int index)
  {
    add(comp, index);
    // Copied from JToolBar.java
    if (comp instanceof JButton) {
      ((JButton)comp).setDefaultCapable(false);
    }
    modified = true;
  }

  /**
   * Adds a component to the toolbar.
   * The component will be added to the appropriate visual group, so that it
   * is placed at the spcified position.
   * <EM>Note:</EM> This method is provided mainly for compatibility with
   * <code>JToolBar</code>.
   * @param     c       The component to add.
   * @param     index   The position in the toolbar at which to add the
   *                    component.
   * @return    The component argument.
   * @exception NullPointerException    Thrown if <code>c</code> is
   *                                    <code>null</code>.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>i</code> has
   *                                            an illegal value
   */
  public Component add(Component c, int index)
    throws NullPointerException, ArrayIndexOutOfBoundsException
  {
    if (index < 0) {
      return add(c);
    }else{
      if (c == null) {
        throw new NullPointerException(resources.getString("nullComponent"));
      }
      int nComponents = getComponentCount();
      if ((index < 0) || (index > nComponents)) {
        throw new ArrayIndexOutOfBoundsException(
          resources.getString("badIndex")
        );
      }

      if (index == nComponents) {
        return add(getLastVisualGroup(), c);
      }else{
        // Try locating the visual group containing the component at the
        // specified location, or any component following that, and placing
        // the component in that group, at the location of the located
        // component.
        for (int i=index; i<nComponents; i++) {
          Component comp = getComponentAtIndex(i);
          int nGroups = vGroups.size();
          for (int j=0; j<nGroups; j++) {
            VisualGroup group = vGroups.get(j);
            int k = group.getComponentIndex(comp);
            if (k >= 0) {
              return add(group, c, k);
            }
          }
        }
        // If we didn't find anything, the location must have pointed to a
        // separator. Add the component at the end of the last visual group.
        return add(getLastVisualGroup(), c);
      }
    }
  }

  /**
   * Removes a component from the toolbar.
   * The component will be removed from the visual group that contains it.
   * <EM>Note:</EM> This method is provided mainly for compatibility with
   * <code>JToolBar</code>.
   * @param     c       The component to remove.
   * @exception NullPointerException    Thrown if <code>c</code> is
   *                                    <code>null</code>.
   */
  public void remove(Component c) throws NullPointerException
  {
    if (scrolling.booleanValue()) {
      super.remove(c);
      modified = true;
    }else{
      if (c == null) {
        throw new NullPointerException(resources.getString("nullComponent"));
      }
      int n = vGroups.size();
      for (int i=0; i<n; i++) {
        VisualGroup group = vGroups.get(i);
        if (group.contains(c)) {
          remove(group, c);
          modified = true;
          return;
        }
      }
    }
  }

  /**
   * Removes a component from the toolbar.
   * The component will be removed from the visual group that contains it.
   * <EM>Note:</EM> This method is provided mainly for compatibility with
   * <code>JToolBar</code>.
   * @param     index   The position in the toolbar of the component to
   *                    be removed.
   * @exception ArrayIndexOutOfBoundsException  Thrown when <code>i</code> has
   *                                            an illegal value
   */
  public void remove(int index)
    throws ArrayIndexOutOfBoundsException
  {
    if (scrolling.booleanValue()) {
      super.remove(index);
      modified = true;
    }else{
      int nComponents = getComponentCount();
      if ((index < 0) || (index >= nComponents)) {
        throw new ArrayIndexOutOfBoundsException(
          resources.getString("badIndex")
        );
      }

      // Try locating the visual group containing the component at the
      // specified location.
      Component comp = getComponentAtIndex(index);
      int nGroups = vGroups.size();
      for (int j=0; j<nGroups; j++) {
        VisualGroup group = vGroups.get(j);
        int k = group.getComponentIndex(comp);
        if (k >= 0) {
          remove(group, k);
          modified = true;
          return;
        }
      }
      // If we didn't find anything, the location must have pointed to a
      // separator. Do nothing.
    }
  }

  /**
   * Returns the last visual group in the toolbar. If the toolbar contains no
   * visual groups, a default visual group is created.
   * @return    The last visual group in the toolbar.
   */
  private VisualGroup getLastVisualGroup()
  {
    int n = vGroups.size();
    if (n > 0) {
      return vGroups.get(n - 1);
    }else{
      return addUniqueVisualGroup(resources.getString("group"));
    }
  }

  /**
   * Removes all the components from the toolbar.
   */
  public void removeAll()
  {
    Component[] tools = getTools();
    int n = tools.length;
    for (int i=0; i<n; i++) {
      unregisterComponent(tools[i]);
    }
    n = vGroups.size();
    for (int i=0; i<n; i++) {
      try {
        vNames.unbind(vGroups.get(i).getName());
      } catch (NameServiceException e) {
      }
    }
    vGroups.clear();
    n = bGroups.size();
    for (int i=0; i<n; i++) {
      try {
        bNames.unbind(bGroups.get(i).getName());
      } catch (NameServiceException e) {
      }
    }
    bNames.dumpNames();
    bGroups.clear();
    toolInfo.clear();
    layoutChanged = true;

    super.removeAll();
    modified = true;
  }

  /**
   * Registers a component with E-Slate, makes the component a child of the
   * toolbar, and associates it with ESlateToolBar information.
   * @param     c       The component to register.
   */
  private void registerComponent(Component c)
  {
    if (c instanceof JComponent) {
      // The getPreferredSize() method of JComponents with preferred size
      // null return their optimum size, which is what we want. This is the
      // case with the default swing components, but their E-Slate
      // counterparts specify explicit preferred sizes in their constructors,
      // so that they can be visible on the desktop.
      ((JComponent)c).setPreferredSize(null);
    }
    ESlateHandle h = getESlateHandle(c);
    getESlateHandle().add(h);
    ToolInfo info = new ToolInfo();
    // Add each installed ToolBarListener to the tool being registered, via a
    // TBLWrapper.
    synchronized (tbListeners) {
      int n = tbListeners.size();
      for (int i=0; i<n; i++) {
        TBLWrapper tblw = tblWrappers.get(i);
        c.addMouseListener(tblw);
        c.addMouseMotionListener(tblw);
        if (c instanceof AbstractButton) {
          ((AbstractButton)c).addActionListener(tblw);
        }else{
          if (c instanceof JComboBox) {
            ((JComboBox)c).addActionListener(tblw);
          }
        }
      }
      toolInfo.put(c, info);
    }
    c.addComponentListener(this);
  }

  /**
   * Removes a component's E-Slate handle from the toolbar's list of children.
   * @param     c       The component to remove.
   */
  private void unregisterComponent(Component c)
  {
    ESlateHandle h = getESlateHandle(c);
    getESlateHandle().remove(h);
    // Remove from the tool the TBLWrappers corresponding to each installed
    // ToolBarListener.
    synchronized (tbListeners) {
      int n = tbListeners.size();
      for (int i=0; i<n; i++) {
        TBLWrapper tblw = tblWrappers.get(i);
        c.removeMouseListener(tblw);
        c.removeMouseMotionListener(tblw);
        if (c instanceof AbstractButton) {
          ((AbstractButton)c).removeActionListener(tblw);
        }else{
          if (c instanceof JComboBox) {
            ((JComboBox)c).removeActionListener(tblw);
          }
        }
      }
      toolInfo.remove(c);
    }
    c.removeComponentListener(this);
    // Remove the tool from any button group that contains it.
    if (c instanceof AbstractButton) {
      AbstractButton ab = (AbstractButton)c;
      int n = bGroups.size();
      for (int i=0; i<n; i++) {
        ESlateButtonGroup bg = bGroups.get(i);
        if (bg.contains(ab)) {
          bg.remove(ab);
        }
      }
    }
  }

//  /**
//   * Invoked by the <code>componentNameChanged()</code> method of the
//   * listeners installed in hosted components, to handle the renaming of a
//   * hosted component.
//   * @param     e       The event to process.
//   */
//  private void hostedComponentNameChanged(ComponentNameChangedEvent e)
//  {
//    // FIXME: Is this needed?
//  }

  /**
   * Returns the E-Slate handle of an arbitrary component. This method will
   * try to get the component's handle by hook or by crook:
   * <UL>
   * <LI>If the component is an ESlatePart, it will call the component's
   * <code>getESlateHandle()</code> method.</LI>
   * <LI>If the component has a <code>getESlateHandle()</code> method, it will
   * find it and invoke it.</LI>
   * <LI>If the component is registered in the current microworld, the
   * corresponding handle will be retrieved.</LI>
   * <LI>If the component is already a child of the toolbar, its handle will
   * be returned. This might succeed if the toolbar has not been
   * placed in a microworld (e.g., because the component that uses it has
   * not made the toolbar's handle a child of its own handle.</LI>
   * <LI>If none of the above succeeds, the component is registered with
   * E-Slate, and the handle of the newly registered component is returned.
   * </UL>
   * @param     compo   The component.
   * @return    The requested handle. If the component is not an E-Slate
   *            component, this method return null.
   */
  ESlateHandle getESlateHandle(Object compo)
  {
    // Can't do anything if the component is null...
    if (compo == null) {
      return null;
    }

    // First, do the obvious: if the component implements the ESlatePart
    // interface, simply call its getESlateHandle() method.
    if (compo instanceof ESlatePart) {
      return ((ESlatePart)(compo)).getESlateHandle();
    }

    // Then check if the component implements the getESlateHandle() method,
    // even if it doesn't declare that it implements the ESlatePart interface.
    Method m = null;
    try {
      Class<?>[] args = new Class[0];
      Class<?> cl = compo.getClass();
      m = cl.getMethod("getESlateHandle", args);
      if (m.getReturnType() != ESlateHandle.class) {
        m = null;
      }
    } catch (Exception e) {
      m = null;
    }
    if (m != null) {
      try {
        return (ESlateHandle)(m.invoke(compo, new Object[0]));
      } catch (Exception e) {
      }
    }

    // Then check the current microworld, to see if the component is
    // registered there.
    if (handle != null) {
      ESlateMicroworld mw = handle.getESlateMicroworld();
      if (mw != null) {
        ESlateHandle h = mw.getComponentHandle(compo);
        if (h != null) {
          return h;
        }
      }
    }

    // Then check the toolbar's children, to see if the component is already a
    // child of the toolbar. This might succeed if the toolbar has not been
    // placed in a microworld (e.g., because the component that uses it has
    // not made the toolbar's handle a child of its own handle.
    ESlateHandle[] children = getESlateHandle().getChildHandles();
    int nChildren = children.length;
    for (int i=0; i<nChildren; i++) {
      ESlateHandle h = children[i];
      Object o = h.getComponent();
      if (compo.equals(o)) {
        return h;
      }
    }

    // If all else fails, register the component with E-Slate and return the
    // newly registered handle.

    return ESlate.registerPart(compo);
  }

  /**
   * Removes all the components from a Visual group.
   * @param     group   The visual group.
   * @exception NullPointerException    Thrown if <code>group</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   */
  public void removeAll(VisualGroup group)
    throws NullPointerException, IllegalArgumentException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (!vGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    int nComponents = group.getComponentCount();
    for (int i=0; i<nComponents; i++) {
      Component c = group.getComponent(i);
      unregisterComponent(c);
    }
    group.removeAll();
    layoutChanged = true;
    modified = true;
  }

  /**
   * Specifies whether the components in a visual group will be drawn on the
   * toolbar.
   * @param     group   The visual group.
   * @param     visible True if yes, false otherwise.
   * @exception NullPointerException    Thrown if <code>group</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   */
  public void setVisible(VisualGroup group, boolean visible)
    throws NullPointerException, IllegalArgumentException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (!vGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    group.setVisible(visible);
    layoutChanged = true;
    modified = true;
  }

  /**
   * Checks whether the components in a visual group will be drawn on the
   * toolbar.
   * @param     group   The visual group.
   * @exception NullPointerException    Thrown if <code>group</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   */
  public boolean isVisible(VisualGroup group)
    throws NullPointerException, IllegalArgumentException
  {
    if (group == null) {
      throw new NullPointerException(resources.getString("nullGroup"));
    }
    if (!vGroups.contains(group)) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    return group.isVisible();
  }

  /**
   * Specifies whether the components in a visual group will be drawn on the
   * toolbar.
   * @param     groupName       The name of the visual group.
   * @param     visible         True if yes, false otherwise.
   * @exception NullPointerException    Thrown if <code>groupName</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   */
  public void setGroupVisible(String groupName, boolean visible)
    throws NullPointerException, IllegalArgumentException
  {
    if (groupName == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    VisualGroup vg = getVisualGroup(groupName);
    if (vg == null) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    vg.setVisible(visible);
    layoutChanged = true;
    modified = true;
  }

  /**
   * Checks whether the components in a visual group will be drawn on the
   * toolbar.
   * @param     groupName       The name of the visual group.
   * @return    True if yes, false otherwise.
   * @exception NullPointerException    Thrown if <code>groupName</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                    does not belong to this toolbar.
   *
   */
  public boolean isGroupVisible(String groupName)
    throws NullPointerException, IllegalArgumentException
  {
    if (groupName == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    VisualGroup vg = getVisualGroup(groupName);
    if (vg == null) {
      throw new IllegalArgumentException(resources.getString("foreignGroup"));
    }
    return vg.isVisible();
  }

  /**
   * Specifies whether a particular tool will be drawn on the toolbar.
   * If the visual group to which the tool belongs is not visible, then
   * the tool will <em>not</em> be drawn, regardless of the value
   * specified.
   * @param     tool    The tool.
   * @param     visible True if the tool will be drawn, false otherwise.
   * @exception NullPointerException    Thrown if <code>c</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the tool does not
   *                                            belong to this toolbar.
   */
  public void setVisible(Component tool, boolean visible)
    throws NullPointerException, IllegalArgumentException
  {
    if (tool == null) {
      throw new NullPointerException(resources.getString("nullComponent"));
    }
    ToolInfo info = toolInfo.get(tool);
    if (info == null) {
      throw new IllegalArgumentException(resources.getString("foreignTool"));
    }
    boolean oldVisible = info.isVisible();
    if (visible != oldVisible) {
      info.setVisible(visible);
      layoutChanged = true;
      modified = true;
    }
  }

  /**
   * Checks whether a particular tool will be drawn on the toolbar.
   * If the visual group to which the tool belongs is not visible, then
   * the tool will <em>not</em> be drawn, regardless of the speciied
   * value.
   * @param     tool    The tool.
   * @return    True if the tool will be drawn, false otherwise.
   * @exception NullPointerException    Thrown if <code>c</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the tool does not
   *                                            belong to this toolbar.
   */
  public boolean isVisible(Component tool)
    throws NullPointerException, IllegalArgumentException
  {
    if (tool == null) {
      throw new NullPointerException(resources.getString("nullComponent"));
    }
    ToolInfo info = toolInfo.get(tool);
    if (info == null) {
      throw new IllegalArgumentException(resources.getString("foreignTool"));
    }
    return info.isVisible();
  }

  /**
   * Specifies whether a particular tool will be drawn on the toolbar.
   * If the visual group to which the tool belongs is not visible, then
   * the tool will <em>not</em> be drawn, regardless of the value
   * specified.
   * @param     name    The name of the tool.
   * @param     visible True if the tool will be drawn. false otherwise.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the tool does not
   *                                            belong to this toolbar.
   */
  public void setToolVisible(String name, boolean visible)
    throws NullPointerException, IllegalArgumentException
  {
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    Component tool = getTool(name);
    if (tool == null) {
      throw new IllegalArgumentException(resources.getString("foreignTool"));
    }
    ToolInfo info = toolInfo.get(tool);
    boolean oldVisible = info.isVisible();
    if (visible != oldVisible) {
      info.setVisible(visible);
      layoutChanged = true;
      modified = true;
    }
  }

  /**
   * Specifies whether a particular tool will be drawn on the toolbar.
   * If the visual group to which the tool belongs is not visible, then
   * the tool will <em>not</em> be drawn, regardless of the value
   * specified.
   * @param     tool    The tool.
   * @param     visible True if the tool will be drawn. false otherwise.
   * @exception NullPointerException    Thrown if <code>tool</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the tool does not
   *                                            belong to this toolbar.
   */
  public void setToolVisible(Component tool, boolean visible)
    throws NullPointerException, IllegalArgumentException
  {
    if (tool == null) {
      throw new NullPointerException(resources.getString("nullTool"));
    }
    if (!toolInfo.containsKey(tool)) {
      throw new IllegalArgumentException(resources.getString("foreignTool"));
    }
    ToolInfo info = toolInfo.get(tool);
    boolean oldVisible = info.isVisible();
    if (visible != oldVisible) {
      info.setVisible(visible);
      layoutChanged = true;
      modified = true;
    }
  }

  /**
   * Checks whether a particular tool will be drawn on the toolbar.
   * If the visual group to which the tool belongs is not visible, then
   * the tool will <em>not</em> be drawn, regardless of the speciied
   * value.
   * @param     name    The name of the tool.
   * @return    True if the component will be drawn, false otherwise.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the tool does not
   *                                            belong to this toolbar.
   */
  public boolean isToolVisible(String name)
    throws NullPointerException, IllegalArgumentException
  {
    if (name == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    Component tool = getTool(name);
    if (tool == null) {
      throw new IllegalArgumentException(resources.getString("foreignTool"));
    }
    ToolInfo info = toolInfo.get(tool);
    return info.isVisible();
  }


  /**
   * Checks whether a particular tool will be drawn on the toolbar.
   * If the visual group to which the tool belongs is not visible, then
   * the tool will <em>not</em> be drawn, regardless of the speciied
   * value.
   * @param     tool    The tool.
   * @return    True if the component will be drawn, false otherwise.
   * @exception NullPointerException    Thrown if <code>tool</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the tool does not
   *                                            belong to this toolbar.
   */
  public boolean isToolVisible(Component tool)
    throws NullPointerException, IllegalArgumentException
  {
    if (tool == null) {
      throw new NullPointerException(resources.getString("nullTool"));
    }
    if (!toolInfo.containsKey(tool)) {
      throw new IllegalArgumentException(resources.getString("foreignTool"));
    }
    ToolInfo info = toolInfo.get(tool);
    return info.isVisible();
  }

  /**
   * Returns the number of tools that have been added to the toolbar.
   * @return    The number of tools that have been added to the toolbar.
   */
  private int getToolCount()
  {
    int n = 0;
    int nGroups = vGroups.size();
    for (int i=0; i<nGroups; i++) {
      n += vGroups.get(i).getComponentCount();
    }
    return n;
  }

  /**
   * Returns the tools that have been added to the toolbar.
   * @return    The tools that have been added to the toolbar.
   */
  public Component[] getTools()
  {
    Component[] compos = new Component[getToolCount()];
    int nGroups = vGroups.size();
    for (int i=0, c=0; i<nGroups; i++) {
      VisualGroup group = vGroups.get(i);
      int nCompos = group.getComponentCount();
      for (int j=0; j<nCompos; j++) {
        compos[c++] = group.getComponent(j);
      }
    }
    return compos;
  }

  /**
   * Returns the E-Slate handles of the tools that have been added to the
   * toolbar.
   * @return    The E-Slate handles of the tools that have been added to the
   *            toolbar.
   */
  private ESlateHandle[] getToolHandles()
  {
    ESlateHandle[] handles = new ESlateHandle[getToolCount()];
    int nGroups = vGroups.size();
    for (int i=0, e=0; i<nGroups; i++) {
      VisualGroup group = vGroups.get(i);
      int nCompos = group.getComponentCount();
      for (int j=0; j<nCompos; j++) {
        handles[e++] = getESlateHandle(group.getComponent(j));
      }
    }
    return handles;
  }

  /**
   * Returns the names of the tools that have been added to the
   * toolbar.
   * @return    The names of the tools that have been added to the
   *            toolbar.
   */
  private String[] getToolNames()
  {
    String[] names = new String[getToolCount()];
    int nGroups = vGroups.size();
    for (int i=0, n=0; i<nGroups; i++) {
      VisualGroup group = vGroups.get(i);
      int nCompos = group.getComponentCount();
      for (int j=0; j<nCompos; j++) {
        names[n++] = getESlateHandle(group.getComponent(j)).getComponentName();
      }
    }
    return names;
  }

  /**
   * Returns a tool contained in the toolbar that has a given name.
   * @param     name    The name of the toolbar.
   * @return    The tool that has the given name, or <code>null</code> if no
   *            such tool exists.
   */
  public Component getTool(String name)
  {
    ESlateHandle h = handle.getChildHandle(name);
    if (h != null) {
      return (Component)(h.getComponent());
    }else{
      return null;
    }
  }


  /**
   * Returns the names of the tools contained in the button groups of the
   * toolbar.
   * @return    An array containing an array with the names of the tools in
   *            each of the button groups of the toolbar.
   */
  private String[][] getButtonGroupToolNames()
  {
    int nGroups = bGroups.size();
    String[][] names = new String[nGroups][];
    for (int i=0; i<nGroups; i++) {
      ESlateButtonGroup group = bGroups.get(i);
      AbstractButton[] ab = group.getButtons();
      int n = ab.length;
      names[i] = new String[n];
      for (int j=0; j<n; j++) {
        names[i][j] = getESlateHandle(ab[j]).getComponentName();
      }
    }
    return names;
  }

  /**
   * Returns the information associated with the tools that have been added
   * to the toolbar.
   * @return    The information associated with the tools that have been added
   *            to the toolbar.
   */
  private ToolInfo[] getToolInfo()
  {
    ToolInfo[] info = new ToolInfo[getToolCount()];
    int nGroups = vGroups.size();
    for (int i=0, ci=0; i<nGroups; i++) {
      VisualGroup group = vGroups.get(i);
      int nCompos = group.getComponentCount();
      for (int j=0; j<nCompos; j++) {
        info[ci++] = toolInfo.get(group.getComponent(j));
      }
    }
    return info;
  }

  /**
   * Finds in which visual group and at what position in the visual group
   * a given tool is placed.
   * @param     c       The component. If the component does not belong to any
   *                    visual group (i.e., it has not been placed in the
   *                    toolbar), this method returns <code>null</code>.
   * @return    The location of the component.
   */
  public ToolLocation locateTool(Component c)
  {
    if (c != null) {
      int nGroups = vGroups.size();
      for (int i=0; i<nGroups; i++) {
        VisualGroup group = vGroups.get(i);
        int nCompos = group.getComponentCount();
        for (int j=0; j<nCompos; j++) {
          if (c.equals(group.getComponent(j))) {
            return new ToolLocation(i, j);
          }
        }
      }
    }
    return null;
  }

  /**
   * Save the component's state.
   * @param     oo      The stream to which the state will be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(saveTimer);

    if (skipWriteExternal) {
      oo.writeObject(null);
      pm.stop(saveTimer);
      pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
      return;
    }else{
      skipWriteExternal = true;
    }

    try {
      ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 10);

      // Save orientation.
      map.put(ORIENTATION, getOrientation());

      // Save tools.
      /*
      boolean dynBorders = isDynamicBorders();
      if (dynBorders) {
        // Make sure that tools have their original borders, so that they are
        // stored correctly.
        setDynamicBorders(false);
      }
      */
      getESlateHandle().saveChildren(map, TOOLS, getToolHandles());
      /*
      if (dynBorders) {
        setDynamicBorders(true);
      }
      */

      // Save visual groups.
      map.put(VISUAL_GROUPS, vGroups.toArray());

      // Save number of components in each visual group.
      int nGroups = vGroups.size();
      int[] groupSizes = new int[nGroups];
      for (int i=0; i<nGroups; i++) {
        groupSizes[i] = vGroups.get(i).getComponentCount();
      }
      map.put(VISUAL_GROUP_SIZES, groupSizes);

      // Save button groups.
      // Saving an array of ESlateButtonGroups saves the serial version UID of
      // the ESlateButtonGroup class, ButtonGroup, which is different on
      // different Java versions. Since the only part of the button group
      // state that we save is the button group's name, we simply store the
      // names of button groups.
      //map.put(BUTTON_GROUPS, bGroups.toArray());
      int nBgs = bGroups.size();
      String[] bgNames = new String[nBgs];
      for (int i=0 ;i<nBgs; i++) {
        bgNames[i] = bGroups.get(i).getName();
      }
      map.put(BUTTON_GROUPS, bgNames);

      // Save the names of the components in each button group.
      map.put(BUTTON_GROUP_TOOL_NAMES, getButtonGroupToolNames());

      // Save tool names.
      map.put(TOOL_NAMES, getToolNames());

      // Save tool information.
      map.put(TOOL_INFO, getToolInfo());

      // Save leading separation.
      map.put(LEADING_SEPARATION, leadingSeparation);

      // Save dynamic borders property.
      map.put(DYNAMIC_BORDERS, isDynamicBorders());

      // Save border.
      Border bd = getBorder();
      if (!(bd instanceof UIResource)) {
        // Don't bother saving the default border. Apart from saving us some
        // work, restoring the border under a different platform, where the
        // particular look and feel is not available, will fail.
        map.put(BORDER, ESlateUtils.getBorderDescriptor(bd, this));
      }

      // Save separation.
      map.put(SEPARATION, separation);

      // Save "centered" property.
      map.put(CENTERED, centered);

      // Save "border painted" property.
      map.put(BORDER_PAINTED, isBorderPainted());

      // Save "floatable" property.
      map.put(FLOATABLE, isFloatable());

      // Save name.
      map.put(NAME, getName());

      // Save sizes.
      map.put(MINIMUM_SIZE, getMinimumSize());
      map.put(MAXIMUM_SIZE, getMaximumSize());
      map.put(PREFERRED_SIZE, prefSize);

      // Store "modified" property.
      map.put(MODIFIED, modified);

      // Store "menu enabled" property.
      map.put(MENU_ENABLED, menuEnabled);

      oo.writeObject(map);
    } finally {
      skipWriteExternal = false;
    }

    pm.stop(saveTimer);
    pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Replace the object restored by readExternal with null, if this object was
   * a reference from a hosted component which should not have been saved.
   * @return    Either <code>this</code> or <code>null</code>.
   */
  private Object readResolve() throws ObjectStreamException
  {
    if (isBogus) {
      return null;
    }else{
      return this;
    }
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from which the state will be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    initialize(oi);
  }

  /**
   * Paint the component.
   * @param     g       The graphics context in which to paint the component.
   */
  public void paint(Graphics g)
  {
    if (layoutChanged) {
      relayout();
    }
    super.paint(g);
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
   * Specifies whether the borders of hosted buttons and combo boxes will be
   * drawn dynamically or not. With dynamic drawing, borders are only painted
   * when the mouse is over a tool or if the tool is a selected toggle button.
   * @param     enabled True to enable dynamic drawing, false to disable.
   */
  public void setDynamicBorders(boolean enabled)
  {
    setRollover(enabled);
  }

  /**
   * Checks whether the borders of hosted buttons and combo boxes are
   * drawn dynamically or not.
   * @return    True if borders are drawn dynamically, false otherwise.
   */
  public boolean isDynamicBorders()
  {
    return isRollover();
  }

  /**
   * Associates a piece of text with a tool in the toolbar.
   * @param     tool    The tool.
   * @param     text    The text to associate with the tool.
   * @exception NullPointerException    Thrown if <code>tool</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the tool does not
   *                                            belong to this toolbar.
   */
  public void setAssociatedText(Component tool, String text)
    throws NullPointerException, IllegalArgumentException
  {
    if (tool == null) {
      throw new NullPointerException(resources.getString("nullComponent"));
    }
    ToolInfo info = toolInfo.get(tool);
    if (info == null) {
      throw new IllegalArgumentException(resources.getString("foreignTool"));
    }
    info.setAssociatedText(text);
    modified = true;
  }

  /**
   * Associates a piece of text with a tool in the toolbar.
   * @param     toolName        The name of the tool.
   * @param     text            The text to associate with the tool.
   * @exception NullPointerException    Thrown if <code>toolName</code> is
   *                                    <code>null</code>
   * @exception IllegalArgumentException        Thrown if the toolbar does not
   *                            contain a tool with the specified name.
   */
  public void setAssociatedText(String toolName, String text)
    throws NullPointerException, IllegalArgumentException
  {
    if (toolName == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    Component tool = getTool(toolName);
    if (tool == null) {
      throw new IllegalArgumentException(resources.getString("foreignTool"));
    }
    ToolInfo info = toolInfo.get(tool);
    info.setAssociatedText(text);
    modified = true;
  }

  /**
   * Returns the text associated with a tool in the toolbar.
   * @param     tool    The tool.
   * @return    The text associated with a tool in the toolbar. If no text has
   *            been associated with the toolbar, then if the component is a
   *            JComponent, its tool tip text is returned, otherwise
   *            <code>null</code> is returned.
   * @exception NullPointerException    Thrown if <code>tool</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the tool does not
   *                                            belong to this toolbar.
   */
  public String getAssociatedText(Component tool)
    throws NullPointerException, IllegalArgumentException
  {
    if (tool == null) {
      throw new NullPointerException(resources.getString("nullComponent"));
    }
    ToolInfo info = toolInfo.get(tool);
    if (info == null) {
      throw new IllegalArgumentException(resources.getString("foreignTool"));
    }
    String text = info.getAssociatedText();
    if ((text == null) && (tool instanceof JComponent)) {
      text = ((JComponent)tool).getToolTipText();
    }
    return text;
  }

  /**
   * Returns the text associated with a tool in the toolbar.
   * @param     toolName        The name of the tool.
   * @return    The text associated with a tool in the toolbar.
   * @exception NullPointerException    Thrown if <code>toolName</code> is
   *                                    <code>null</code>
   * @exception IllegalArgumentException        Thrown if the toolbar does not
   *                            contain a tool with the specified name.
   */
  public String getAssociatedText(String toolName)
    throws NullPointerException, IllegalArgumentException
  {
    if (toolName == null) {
      throw new NullPointerException(resources.getString("nullName"));
    }
    Component tool = getTool(toolName);
    if (tool == null) {
      throw new IllegalArgumentException(resources.getString("foreignTool"));
    }
    ToolInfo info = toolInfo.get(tool);
    return info.getAssociatedText();
  }

  /**
   * Gets the visual group layout of the toolbar.
   * @return    A description of the visual groups of the toolbar.
   */
  public VisualGroupLayout getVisualGroupLayout()
  {
    return new VisualGroupLayout(this);
  }

  /**
   * Sets the visual group layout of the toolbar.
   * @param     layout  A description of the visual groups of the toolbar.
   */
  public void setVisualGroupLayout(VisualGroupLayout layout)
  {
    // Remove old visual groups.
    int n = vGroups.size();
    for (int i=0; i<n; i++) {
      try {
        vNames.unbind(vGroups.get(i).getName());
      } catch (NameServiceException e) {
      }
    }
    vGroups.clear();

    VisualGroupEditorGroupInfoBaseArray groups = layout.groups;
    int nGroups = groups.size();
    ArrayList<Object> namesList = new ArrayList<Object>();
    // Create new visual groups.
    for (int i=0; i<nGroups; i++) {
      VisualGroupEditorGroupInfo gInfo = groups.get(i);
      String gName = gInfo.name;
      VisualGroup vg = new VisualGroup(gName);
      vg.setVisible(gInfo.visible);
      try {
        vNames.bind(gName, vg);
      } catch (NameServiceException nse) {
      }
      vGroups.add(vg);
      VisualGroupEditorToolInfoBaseArray tools = gInfo.tools;
      int nTools = tools.size();
      // Add tools to visual group.
      for (int j=0; j<nTools; j++) {
        VisualGroupEditorToolInfo tInfo = tools.get(j);
        //String tName = tInfo.name;
        Object tool = tInfo.tool;
        Component comp;
        if (tool instanceof Class) {
          try {
            comp = (Component)(((Class<?>)tool).newInstance());
          } catch (Exception e) {
            Thread.dumpStack();
            continue;
          }
          vg.add(comp);
          registerComponent(comp);
        }else{
          comp = (Component)tool;
          vg.add(comp);
        }
        namesList.add(comp);
        namesList.add(tInfo.name);
        ToolInfo ti = toolInfo.get(comp);
        ti.setVisible(tInfo.visible);
        ti.setAssociatedText(tInfo.text);
      }
    }

    // Remove components that are not in the new layout.
    ESlateHandle h = getESlateHandle();
    ESlateHandle[] children = h.getChildHandles();
    int nChildren = children.length;
    for (int i=0; i<nChildren; i++) {
      ESlateHandle childHandle = children[i];
      Component c = (Component)(childHandle.getComponent());
      if (!containsTool(c)) {
        unregisterComponent(c);
      }
    }

    // Rename components.
    // First rename them to something silly...
    children = h.getChildHandles();
    n = children.length;
    for (int i=0; i<n; i++) {
      try {
        children[i].setUniqueComponentName(
          // Anyone who uses this name for their component deserves
          // what they get!
          "This+is+a+@#$%^&*()_+|~reserved+name+don't+use!"
        );
      } catch (RenamingForbiddenException rfe) {
      }
    }
    // ...then rename them to their final names.
    n = namesList.size();
    for (int i=0; i<n; i+=2) {
      Component c = (Component)(namesList.get(i));
      String name = (String)(namesList.get(i+1));
      try {
        getESlateHandle(c).setComponentName(name);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    layoutChanged = true;
    modified = true;
    repaint();
  }

  /**
   * Checks whether the toolbar contains a specific tool.
   * @param     tool    The tool to check.
   * @return    True if teh toolbar contains <code>tool</code>, false
   *            otherwise.
   */
  public boolean containsTool(Component tool)
  {
    boolean found = false;
    int nGroups = vGroups.size();
    for (int j=0; j<nGroups; j++) {
      VisualGroup vg = vGroups.get(j);
      ComponentBaseArray components = vg.components;
      if (components.contains(tool)) {
        found = true;
        break;
      }
    }
    return found;
  }

  /**
   * Gets the button group layout of the toolbar.
   * @return    A description of the button groups of the toolbar.
   */
  public ButtonGroupLayout getButtonGroupLayout()
  {
    return new ButtonGroupLayout(this);
  }

  /**
   * Sets the Button group layout of the toolbar.
   * @param     layout  A description of the button groups of the toolbar.
   */
  public void setButtonGroupLayout(ButtonGroupLayout layout)
  {
    // Remove old button groups.
    int nGroups = bGroups.size();
    for (int i=0; i<nGroups; i++) {
      ESlateButtonGroup bg = bGroups.get(i);
      try {
        bNames.unbind(bg.getName());
      } catch (NameServiceException nse) {
      }
      AbstractButton[] b = bg.getButtons();
      int nButtons = b.length;
      for (int j=0; j<nButtons; j++) {
        bg.remove(b[j]);
      }
    }
    bGroups.clear();

    ButtonGroupEditorGroupInfoBaseArray groups = layout.groups;
    nGroups = groups.size();
    ArrayList<Object> namesList = new ArrayList<Object>();
    // Create new button groups.
    for (int i=0; i<nGroups; i++) {
      ButtonGroupEditorGroupInfo bInfo = groups.get(i);
      ESlateButtonGroup bg;
      try {
        bg = addButtonGroup(bInfo.name);
      } catch (NameUsedException nue) {
        // Can't happen.
        bg = null;
      }
      ButtonGroupEditorToolInfoBaseArray tools = bInfo.tools;
      int nTools = tools.size();
      for (int j=0; j<nTools; j++) {
        ButtonGroupEditorToolInfo tInfo = tools.get(j);
        AbstractButton button = tInfo.tool;
        bg.add(button);
        namesList.add(button);
        namesList.add(tInfo.name);
      }
    }
    ButtonGroupEditorToolInfoBaseArray freeButtons = layout.freeButtons;
    int nFree = freeButtons.size();
    for (int i=0; i<nFree; i++) {
      ButtonGroupEditorToolInfo tInfo = freeButtons.get(i);
      namesList.add(tInfo.tool);
      namesList.add(tInfo.name);
    }

    // Rename buttons.
    // First rename them to something silly...
    ESlateHandle[] children = getESlateHandle().getChildHandles();
    int n = children.length;
    for (int i=0; i<n; i++) {
      ESlateHandle h = children[i];
      if (h.getComponent() instanceof AbstractButton) {
        try {
          h.setUniqueComponentName(
            // Anyone who uses this name for their component deserves
            // what they get!
            "This+is+a+@#$%^&*()_+|~reserved+name+don't+use!"
          );
        } catch (RenamingForbiddenException rfe) {
        }
      }
    }
    // ...then rename them to their final names.
    n = namesList.size();
    for (int i=0; i<n; i+=2) {
      Component c = (Component)(namesList.get(i));
      String name = (String)(namesList.get(i+1));
      try {
        getESlateHandle(c).setComponentName(name);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    layoutChanged = true;
    repaint();
    modified = true;
  }

  /**
   * Specifies whether the contents of the toolbar should be centered within
   * the toolbar.
   * @param     centered        True if yes, false if no.
   */
  public void setCentered(boolean centered)
  {
    if (this.centered != centered) {
      this.centered = centered;
      layoutChanged = true;
      repaint();
      modified = true;
    }
  }

  /**
   * Checks whether the contents of the toolbar are centered within
   * the toolbar.
   * @return    True if yes, false if no.
   */
  public boolean isCentered()
  {
    return centered;
  }

  /**
   * Add a listener for toolbar events.
   * @param     listener        The listener to add.
   */
  public void addToolBarListener(ToolBarListener listener)
  {
    synchronized(tbListeners) {
      if (!tbListeners.contains(listener)) {
        tbListeners.add(listener);
        // Add the listener to each of the tools in the toolbar via a
        // TBLWrapper.
        TBLWrapper tblw = new TBLWrapper(listener);
        tblWrappers.add(tblw);
        Component[] tools = getTools();
        int n = tools.length;
        for (int i=0; i<n; i++) {
          Component c = tools[i];
          c.addMouseListener(tblw);
          c.addMouseMotionListener(tblw);
          if (c instanceof AbstractButton) {
            ((AbstractButton)c).addActionListener(tblw);
          }else{
            if (c instanceof JComboBox) {
              ((JComboBox)c).addActionListener(tblw);
            }
          }
        }
      }
    }
  }

  /**
   * Remove a listener for toolbar events.
   * @param     listener        The listener to remove.
   */
  public void removeToolBarListener(ToolBarListener listener)
  {
    synchronized(tbListeners) {
      int ind = tbListeners.indexOf(listener);
      if (ind >= 0) {
        tbListeners.remove(ind);
        TBLWrapper tblw = tblWrappers.get(ind);
        tblWrappers.remove(ind);
        Component[] tools = getTools();
        int n = tools.length;
        // Remove from each tool in the toolbar the TBLWrappers corresponding
        // to the listener being removed.
        for (int i=0; i<n; i++) {
          Component c = tools[i];
          c.removeMouseListener(tblw);
          c.removeMouseMotionListener(tblw);
          if (c instanceof AbstractButton) {
            ((AbstractButton)c).removeActionListener(tblw);
          }else{
            if (c instanceof JComboBox) {
              ((JComboBox)c).removeActionListener(tblw);
            }
          }
        }
      }
    }
  }

  /**
   * Handles component resized events.
   * @param     e       The event to handle.
   */
  public void componentResized(ComponentEvent e)
  {
    layoutChanged = true;
    repaint();
  }

  /**
   * Handles component hidden events. Required by ComponentListener
   * interface--not used.
   * @param     e       The event to handle.
   */
  public void componentHidden(ComponentEvent e)
  {
  }

  /**
   * Handles component moved events. Required by ComponentListener
   * interface--not used.
   * @param     e       The event to handle.
   */
  public void componentMoved(ComponentEvent e)
  {
  }

  /**
   * Handles component shown events. Required by ComponentListener
   * interface--not used.
   * @param     e       The event to handle.
   */
  public void componentShown(ComponentEvent e)
  {
  }

  /**
   * Returns the preferred size of the toolbar.
   * @return    The preferred size of the toolbar.
   */
  public Dimension getPreferredSize()
  {
    if (prefSize != null) {
      return prefSize;
    }else{
      if ((actualPrefSize == null) || layoutChanged) {
        if (getComponentCount() == 0) {
          // The preferred size of an ampty toolbar is 0, which is bad for a
          // free-standing tool bar. Make it somewhat wider, and high enough
          // for a button to fit inside it.
          int h = new JButton("test").getPreferredSize().height;
          return new Dimension(320, h);
        }else{
          relayout();
        }
      }
      return actualPrefSize;
    }
  }

  /**
   * Sets the preferred size of the toolbar.
   * @param     size    The preferred size of the toolbar.
   */
  public void setPreferredSize(Dimension size)
  {
    prefSize = size;
    super.setPreferredSize(size);
  }

  /**
   * Specify the object that can set the toolbar to its default state.
   * @param     setter  The object that can set the toolbar to its default
   *                    state.
   */
  public void setDefaultStateSetter(DefaultStateSetter setter)
  {
    defaultStateSetter = setter;
  }

  /**
   * Returns the object that can set the toolbar to its default state.
   * @return    The object that can set the toolbar to its default state.
   */
  public DefaultStateSetter getDefaultStateSetter()
  {
    return defaultStateSetter;
  }

  /**
   * Sets the toolbar to its default state by invoking the default state
   * setter specified by <code>setDefaultStateSetter()</code>. The default
   * state is whatever the default state setter wants it to be!
   * If no default state setter has been specified, this method does nothing.
   */
  public void setDefaultState()
  {
    if (defaultStateSetter != null) {
      defaultStateSetter.setDefaultState(this);
    }
  }

  /**
   * Specifies whether the toolbar has been modified from its "stable" state.
   * The toolbar's stable state is either its state immediately after
   * construction, or the state in which it is when
   * <code>setModified(false)</code> is called. Use this method, in
   * conjunction with <code>isModified()</code>, if you want to know whether
   * the toolbar has been modified since some point in time.
   * @param     modified        True to specify that the toolbar has been
   *                            modified, false to specify that it hasn't or
   *                            that it is now in its stable state.
   */
  public void setModified(boolean modified)
  {
    this.modified = modified;
  }

  /**
   * Checks whether the toolbar has been modified from its "stable" state.
   * The toolbar's stable state is either its state immediately after
   * construction, or the state in which it is when
   * <code>setModified(false)</code> is called. Use this method, in
   * conjunction with <code>setModified()</code>, if you want to know whether
   * the toolbar has been modified since some point in time.
   * @return    True if the toolbar has been modified, false if it hasn't.
   */
  public boolean isModified()
  {
    return modified;
  }

  /**
   * Specifies whether a popup menu will appear when pressing the mouse on
   * unused space.
   * @param     enabled True if yes, false if no.
   */
  public void setMenuEnabled(boolean enabled)
  {
    menuEnabled = enabled;
  }

  /**
   * Checks whether a popup menu will appear when pressing the mouse on
   * unused space.
   * @return    True if yes, false if no.
   */
  public boolean isMenuEnabled()
  {
    return menuEnabled;
  }

  /**
   * Processes mouse presses on the unused space of the toolbar. Used to pop
   * up a menu.
   * @param     e       The event received when the mouse was pressed.
   */
  private void processMousePress(MouseEvent e)
  {
    if (menuEnabled) {
      // Check for popup menu mouse button. Since isPopupTrigger does not
      // appear to be working under Windows, we also check explicitly for
      // the right button.
      if (e.isPopupTrigger() ||
          (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
        getPopup().show(this, e.getX(), e.getY());
      }
    }
  }

  /**
   * Returns the toolbar's popup menu, creating it the first time this method
   * is invoked.
   * @return    The toolbar's popup menu.
   */
  private ESlateJPopupMenu getPopup()
  {
    if (popup == null) {
      popup = new ESlateJPopupMenu();
      popup.setLightWeightPopupEnabled(false);
      JMenuItem removeItem =
        new JMenuItem(resources.getString("removeFromParent"));
      removeItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          removeFromParent();
        }
      });
      popup.add(removeItem);
      // Right mouse button drags floatable toolbars, making a mess when we
      // want to access the popup menu. Install a listener that will
      // temporarily make the toolbar non-floatable while the popup menu is
      // displayed.
      popup.addPopupMenuListener(new PopupMenuListener()
      {
        private boolean floatable;
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
        {
          setFloatable(floatable);
        }
        public void popupMenuWillBecomeVisible(PopupMenuEvent e)
        {
          floatable = isFloatable();
          setFloatable(false);
        }
        public void popupMenuCanceled(PopupMenuEvent e)
        {
        }
      });
    }
    return popup;
  }

  /**
   * Removes the toolbar from its parent's AWT and E-Slate hierarchy.
   * If the toolbar has been modified from its stable state
   * (<code>isModified()&nbsp;==&nbsp;true</code>), the user is first asked
   * for confirmation.
   */
  private void removeFromParent()
  {
    int remove;
    if (modified) {
      remove = ESlateOptionPane.showConfirmDialog(
        this,
        resources.getString("confirmRemove"),
        resources.getString("confirm"),
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
      );
    }else{
      remove = JOptionPane.YES_OPTION;
    }
    if (remove == JOptionPane.YES_OPTION) {
      ESlateHandle parentHandle;
      if (handle != null) {
        parentHandle = handle.getParentHandle();
      }else{
        parentHandle = null;
      }
      Container parentComponent = getParent();

      if (parentHandle != null) {
        Object p = parentHandle.getComponent();
        if ((p != null) &&
            p.equals(handle.getESlateMicroworld().getESlateHandle().getComponent())) {
          ESlateOptionPane.showMessageDialog(
            this,
            resources.getString("noTopLevel"),
            resources.getString("notice"),
            JOptionPane.WARNING_MESSAGE
          );
          return;
        }
      }

      if (parentComponent != null) {
        parentComponent.remove(this);
        revalidate(parentComponent);
      }
      if (parentHandle != null) {
        // The AWT parent may have made changes to the AWT hierarchy after
        // the toolbar was removed. Make sure that the toolbar's E-Slate
        // handle can be removed before trying to remove it.
        if (!handle.isDisposed() && parentHandle.contains(handle)) {
          parentHandle.remove(handle);
        }
      }
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

}
