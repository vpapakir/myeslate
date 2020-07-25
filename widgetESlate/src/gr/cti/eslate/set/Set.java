package gr.cti.eslate.set;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.text.*;
import java.util.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.database.engine.event.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.tableModel.event.*;
import gr.cti.eslate.utils.*;

import gr.cti.eslate.eslateButton.*;
import gr.cti.eslate.eslateComboBox.*;
import gr.cti.eslate.eslateToggleButton.*;
import gr.cti.eslate.eslateToolBar.*;

/**
 * This class implements a component that can display up to three database
 * queries in the form of Venn diagrams.
 * <P>
 * <B>Component plugs:</B>
 * <UL>
 * <LI><B>Database</B> This is a single input protocol plug.
 * The plug's color is Color.magenta.
 * </LI>
 * </UL>
 * <P>
 * <B>Logo primitives:</B>
 * <UL>
 * <LI><B>SELECTSUBSET [x y]</B> Selects the subset at the given coordinates.
 * </LI>
 * <LI><B>SELECTSUBSET [a b c]</B> Selects a subset by specifying whether it
 * belongs to one of the three ellipses that the set can display: a specifies
 * if the subset belongs to the first (top) ellipse, b in the second (left),
 * and c in the third (right).</LI>
 * <LI><B>CLEARSELECTEDSUBSET</B> Clears the selected subset.</LI>
 * <LI><B>QUERYINSET</B> Returns the description of the selected subset.</LI>
 * <LI><B>DELETEELLIPSE [x y]</B> Deletes the ellipse(s) at the given
 * coordinates.</LI>
 * <LI><B>DELETEELLIPSE [a b c]</B> Deletes ellipse(s) by specifying which of
 * the three ellipses that the set can display should be deleted: a specifies
 * the first (top) ellipse, b the second (left), and c the third (right).</LI>
 * <LI><B>NEWELLIPSE</B> Creates a new ellipse.</LI>
 * <LI><B>ACTIVATEELLIPSE [x y]</B> Activates the ellipse at the given
 * coordinates.</LI>
 * <LI><B>ACTIVATEELLIPSE n</B> Activates the given ellipse: 0=top, 1=bottom
 * left, 2=bottom right.</LI>
 * <LI><B>DEACTIVATEELLIPSE</B> Deactivates the currently active ellipse.</LI>
 * <LI><B>SETTABLEINSET name</B> Select the table to display.</LI>
 * <LI><B>TABLEINSET</B> Return the name of the displayed table.</LI>
 * <LI><B>TABLESINSET</B> Return the names of the tables that can be
 * displayed.</LI>
 * <LI><B>SETPROJECTIONFIELD name</B> Select the field projected onto the
 * component.</LI>
 * <LI><B>PROJECTIONFIELD</B> Return the name of the field projected onto the
 * component.</LI>
 * <LI><B>PROJECTIONFIELDS</B> Return the names of the fields that can be
 * projected onto the component.</LI>
 * <LI><B>SETCALCULATIONTYPE name</B> Select the calculation operation to
 * perform.</LI>
 * <LI><B>CALCULATIONTYPE</B> Return the name of the calculation operation
 * that is being performed.</LI>
 * <LI><B>CALCULATIONTYPES</B> Return the names of the calculation operations
 * that can be performed.</LI>
 * <LI><B>SETCALCULATIONFIELD name</B> Select the field on which calculations
 * will be performed.</LI>
 * <LI><B>CALCULATIONFIELD</B> Return the name of the field on which
 * calculations are being performed.</LI>
 * <LI><B>CALCULATIONFIELDS</B> Return the names of the fields on which
 * calculations can be performed.</LI>
 * <LI><B>PROJECTFIELD yesno</B> Specify whether the selected projection field
 * should be displayed.</LI>
 * <LI><B>PROJECTINGFIELDS</B> Return whether the selected projection field is
 * being displayed.</LI>
 * <LI><B>CALCULATEINSET yesno</B> Specify whether individual elements or
 * calculated data should be displayed in the set panels.</LI>
 * <LI><B>CALCULATINGINSET</B> Returns whether calculations are
 * displayed.</LI>
 * </UL>
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 * @see         gr.cti.eslate.set.SetDisplayPanel
 * @see         gr.cti.eslate.set.SetPanel
 */
public class Set extends JPanel
                 implements ESlatePart, AsSet, Serializable, Externalizable
{
  private final static String version = "2.0.4";
  private final static int saveVersion = 1;
  JTextField statusBar = new CursorTextField()
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    public void updateUI()
    {
      super.updateUI();
      setBackground(UIManager.getColor("control"));
    }
  };
  private ESlateToolBar toolBar = null;
  private JTabbedPane contents = new JTabbedPane();
  private JPanel emptyPanel = new JPanel()
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    public void updateUI()
    {
      super.updateUI();
      setBackground(UIManager.getColor("controlShadow"));
    }
  };
  private CardPanel cardPanel = new CardPanel();
  AbstractButton selButton;
  AbstractButton selSetButton;
  AbstractButton selOvalButton;
  AbstractButton delButton ;
  AbstractButton lastButton = null;
  ESlateButton newButton;
  //private ESlateButton copyButton;
  private AbstractButton projButton;
  private ESlateComboBox projField;
  private VirtualComboBox vProjField = new VirtualComboBox();
  private AbstractButton calcButton;
  ESlateComboBox calcKey;
  VirtualComboBox vCalcKey = new VirtualComboBox();
  ESlateComboBox calcOp;
  VirtualComboBox vCalcOp = new VirtualComboBox();
  private JPanel mainPanel = new JPanel();
  private ESlateHandle handle;
  ResourceBundle resources = null;
  NumberFormat nf = null;
  private ArrayList<SetDisplayPanel> setPanels =
    new ArrayList<SetDisplayPanel>();
  private DBase db = null;
  private DatabaseAdapter dbAdapter = null;
  private DatabaseTableModelAdapter tableAdapter = null;
  private boolean activateDB = true;
  private boolean showLabels = true;
  private boolean uniformProjection = false;
  private boolean toolBarVisible = true;
  //private static Color defaultFillColor = new Color(0xC0FFFF);
  private static Color defaultFillColor = new Color(0, 192, 0);
  private Color fillColor = defaultFillColor;
  //private static Color defaultBgColor = new Color(196, 196, 165);
  private static Color defaultBgColor = new Color(180, 151, 120);
  private Color bgColor = defaultBgColor;
  private final static Dimension preferredSize = new Dimension(480, 320);
  private final static Dimension minimumSize = new Dimension(320, 240);
  private boolean removingToolbarFromSet = false;
  // Obsolete, used only when reading the saved state of older versions of the
  // component.
  private final static int SAVEVERSION = 2;
  //private final static int SAVEREVISION = 0;

  private int nTables = -1;
  private int selectedIndex = -1;
  private String[] titles = null;
  private int[] placeWidths = null;
  private int[] placeHeights = null;
  private boolean[] delayedPlacements = null;
  private int[] projFields = null;
  private int[][] areaProjFields = null;
  private int[] calcOps = null;
  private int[] calcKeys = null;
  private int[] nOvals = null;
  private String[] selected = null;
  private String[] selTexts = null;
  private int[][] ovalsInUse = null;
  private boolean[] oval1InUse = null;
  private boolean[] oval2InUse = null;
  private boolean[] oval3InUse = null;
  private String[] query1 = null;
  private String[] query2 = null;
  private String[] query3 = null;
  @SuppressWarnings("unchecked")
  private ArrayList[] xs = null;
  @SuppressWarnings("unchecked")
  private ArrayList[] ys = null;
  private int[] activeOvals = null;

  final static int COUNT = 0;
  final static int PERCENT_TOTAL = 1;
  final static int TOTAL = 2;
  final static int MEAN = 3;
  final static int MEDIAN = 4;
  final static int SMALLEST = 5;
  final static int LARGEST = 6;
  final static int PERCENT = 7;

  private static String calcOpNames[];

  /**
   * Indicates whether individual elements or calculated data should be
   * displayed.
   */
  boolean calculate = false;
  /**
   * Indicates whether mouse clicks delete queries.
   */
  boolean deleteQuery = false;
  /**
   * Indicates whether mouse clicks select queries.
   */
  boolean selecting = false;
  /**
   * Indicates whether mouse clicks select ellipses.
   */
  boolean selectOval = false;

  static {
    ResourceBundle res = ResourceBundle.getBundle(
      "gr.cti.eslate.set.SetResource", Locale.getDefault()
    );
    calcOpNames = new String[] {
      res.getString("count"),
      res.getString("percentTotal"),
      res.getString("total"),
      res.getString("mean"),
      res.getString("median"),
      res.getString("smallest"),
      res.getString("largest"),
      res.getString("percent")
    };
  }

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

  // StorageStructure keys.
  private final static String CALCULATE = "calculate";
  private final static String DELETE_QUERY = "deleteQuery";
  private final static String SELECTING = "selecting";
  private final static String SELECTOVAL = "selectOval";
  private final static String PROJECT = "project";
  private final static String N_TABLES = "nTables";
  private final static String CONNECTED = "connected";
  private final static String SELECTED_INDEX = "selectedIndex";
  private final static String TITLES = "titles";
  private final static String PLACE_WIDTHS = "placeWidths";
  private final static String PLACE_HEIGHTS = "placeHeights";
  private final static String DELAYED_PLACEMENTS = "delayedPlacements";
  private final static String PROJ_FIELDS = "projFields";
  private final static String AREA_PROJ_FIELDS = "areaProjFields";
  private final static String CALC_OPS = "calcOps";
  private final static String CALC_KEYS = "calcKeys";
  private final static String N_OVALS = "nOvals";
  private final static String SELECTED = "selected";
  private final static String SEL_TEXTS = "seltexts";
  private final static String OVALS_IN_USE = "ovalsInUse";
  private final static String OVAL1_IN_USE = "oval1InUse";
  private final static String OVAL2_IN_USE = "oval2InUse";
  private final static String OVAL3_IN_USE = "oval3InUse";
  private final static String QUERY1 = "query1";
  private final static String QUERY2 = "query2";
  private final static String QUERY3 = "query3";
  private final static String XS = "xs";
  private final static String YS = "ys";
  private final static String ACTIVE_OVALS = "activeOvals";
  private final static String SHOW_LABELS = "showLabels";
  private final static String UNIFORM_PROJECTION = "uniformProjection";
  private final static String BG_COLOR = "bgColor";
  private final static String FILL_COLOR = "fillColor";
  private final static String LAST_BUTTON = "lastButton";
  private final static String HOSTED = "hosted";
  private final static String MINIMUM_SIZE = "minimumSize";
  private final static String MAXIMUM_SIZE = "maximumSIze";
  private final static String PREFERRED_SIZE = "preferredSize";
  private final static String HAVE_TOOLBAR = "haveToolbar";
  private final static String TOOL_LOCATIONS = "toolLocations";
  private final static String TOOLBAR_VISIBLE = "toolBarVisible";
  private final static String LOCALE = "locale";

  private final static int NOTHING = -100;

  private final static String EMPTY = "empty";
  private final static String CONTENTS = "contents";

  static final long serialVersionUID = -258062641493471606L;

  /**
   * Create a set component.
   */
  public Set()
  {
    super();
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.set.SetResource", Locale.getDefault()
    );

    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);

    initializeCommon();
    initialize();

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }

  /**
   * Create a set component from an <code>ObjectInput</code>. The component's
   * state is initialized directly from the contents of the provided input
   * stream.
   * @param     oi      The <code>ObjectInput</code> from which to initialize
   *                    the component.
   * @exception Exception       Thrown if constructing the toolbar fails.
   */
  public Set(ObjectInput oi) throws Exception
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.set.SetResource", Locale.getDefault()
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

    pm.displayTime(constructorTimer, "", "ms");
  }

  /**
   * Common initialization for all types of constructor.
   */
  private void initializeCommon()
  {
    nf = NumberFormat.getNumberInstance();

    statusBar.setFont(UIManager.getFont("TextField.font"));
    statusBar.setText(" ");
    statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));

    emptyPanel.setBorder(BorderFactory.createLoweredBevelBorder());

    contents.setPreferredSize(new Dimension(320, 200));
    contents.setMinimumSize(new Dimension(320, 200));
    contents.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        SetPanel s = null;
        try {
          s = currentSetPanel();
          if (db != null && activateDB) {
            int selIndex = contents.getSelectedIndex();
            Table table = db.getTable(contents.getTitleAt(selIndex));
            int index = db.indexOf(table);
            db.activateTable(index, true);
            s.updateParentProjField();
          }
        } catch (Exception ex) {
        }
        updateComboBoxes(s);
        activateDB = true;
      }
    });

    setLayout(new BorderLayout());

    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(statusBar, BorderLayout.SOUTH);

    cardPanel.add(emptyPanel, EMPTY);
    cardPanel.add(contents, CONTENTS);
    //mainPanel.add(contents, BorderLayout.CENTER);
    mainPanel.add(cardPanel, BorderLayout.CENTER);

    add(mainPanel, BorderLayout.CENTER);

    contents.setBorder(new BevelBorder(BevelBorder.LOWERED));

    mainPanel.addContainerListener(new ContainerAdapter(){
      public void componentRemoved(ContainerEvent e)
      {
        Set.this.componentRemoved(e);
      }
    });
  }

  /**
   * Initialize component from scratch.
   */
  private void initialize()
  {
    setPreferredSize(Set.preferredSize);
    setMinimumSize(Set.minimumSize);

    toolBar = createToolBar(null);
    setupToolBar();
    if (handle != null) {
      handle.add(toolBar.getESlateHandle());
    }

    mainPanel.add(toolBar, BorderLayout.NORTH);

    enableToolBarIfEmpty(false);

    selecting = !selButton.isSelected();
    selectOval = selOvalButton.isSelected();
  }

  /**
   * Initialize component from an <code>ObjectInput</code>.
   * @param     oi      The <code>ObjectInput</code> from which to initialize
   *                    the component.
   * @exception IOException     Thrown if reading from <code>oi</code> fails.
   * @exception ClassNotFoundException  Thrown if one of the classes being
   *                    read from <code>oi</code> is not in the class path.
   */
  @SuppressWarnings(value={"unchecked"})
  private void initialize(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(loadTimer);

    Object obj = (oi.readObject());
    if (obj instanceof StorageStructure) {
      StorageStructure map = (StorageStructure)obj;

      nTables = map.get(N_TABLES, 0);

      selectedIndex = map.get(SELECTED_INDEX, 0);

      titles = (String[])(map.get(TITLES));
      placeWidths = (int[])(map.get(PLACE_WIDTHS));
      placeHeights = (int[])(map.get(PLACE_HEIGHTS));
      delayedPlacements = (boolean[])(map.get(DELAYED_PLACEMENTS));
      projFields = (int[])(map.get(PROJ_FIELDS));
      if (projFields != null) {
        areaProjFields = new int[nTables][8];
        for (int i=0; i<nTables; i++) {
          for (int j=0; j<8; j++) {
            areaProjFields[i][j] = projFields[i];
          }
        }
      }else{
        areaProjFields = (int[][])(map.get(AREA_PROJ_FIELDS));
      }
      calcOps = (int[])(map.get(CALC_OPS));
      calcKeys = (int[])(map.get(CALC_KEYS));
      nOvals = (int[])(map.get(N_OVALS));
      selected = (String[])(map.get(SELECTED));
      selTexts = (String[])(map.get(SEL_TEXTS));
      ovalsInUse = (int[][])(map.get(OVALS_IN_USE));
      oval1InUse = (boolean[])(map.get(OVAL1_IN_USE));
      oval2InUse = (boolean[])(map.get(OVAL2_IN_USE));
      oval3InUse = (boolean[])(map.get(OVAL3_IN_USE));
      query1 = (String[])(map.get(QUERY1));
      query2 = (String[])(map.get(QUERY2));
      query3 = (String[])(map.get(QUERY3));
      //xs = (Vector[])(map.get(XS));
      //ys = (Vector[])(map.get(YS));
      Object o = map.get(XS);
      if (o instanceof Vector[]) {
        Vector[] vv = (Vector[])o;
        int nVectors = vv.length;
        xs = new ArrayList[nVectors];
        for (int i=0; i<nVectors; i++) {
          Vector v = vv[i];
          int n = v.size();
          xs[i] = new ArrayList<Integer>(n);
          for (int j=0; j<n; j++) {
            xs[i].add((Integer)v.get(j));
          }
        }
      }else{
        xs = (ArrayList<Integer>[])o;
      }
      o = map.get(YS);
      if (o instanceof Vector[]) {
        Vector[] vv = (Vector[])o;
        int nVectors = vv.length;
        ys = new ArrayList[nVectors];
        for (int i=0; i<nVectors; i++) {
          Vector v = vv[i];
          int n = v.size();
          ys[i] = new ArrayList<Integer>(n);
          for (int j=0; j<n; j++) {
            ys[i].add((Integer)v.get(j));
          }
        }
      }else{
        ys = (ArrayList<Integer>[])o;
      }
      activeOvals = (int[])(map.get(ACTIVE_OVALS));
      showLabels = map.get(SHOW_LABELS, true);
      uniformProjection = map.get(UNIFORM_PROJECTION, false);
      bgColor = new Color(map.get(BG_COLOR, colorToInt(defaultBgColor)));
      fillColor = new Color(map.get(FILL_COLOR, colorToInt(defaultFillColor)));
      // Older versions of the set component did not store the "connected"
      // information, and were unable to distinguish between restoring an
      // unconnected set and a set connected to a DB with 0 tables, causing an
      // exception when trying to use the set in the former case. As DBs with
      // 0 tables are rather uncommon, assume that this is never the case, so
      // that older microworlds with unconnected sets can be restored.
      boolean connected = map.get(CONNECTED, (nTables > 0));
      setMinimumSize(map.get(MINIMUM_SIZE, Set.minimumSize));
      setMaximumSize(map.get(MAXIMUM_SIZE, getMaximumSize()));
      setPreferredSize(map.get(PREFERRED_SIZE, Set.preferredSize));
      boolean haveToolBar = map.get(HAVE_TOOLBAR, false);
      ToolLocation[] toolLocations = (ToolLocation[])(map.get(TOOL_LOCATIONS));
      boolean tBarVisible = map.get(TOOLBAR_VISIBLE, true);
      String locale = map.get(LOCALE, (String)null);
      if (!connected) {
        nTables = -1;
      }
      if (handle != null) {
        //DBase.microworldForReadExternal = handle.getESlateMicroworld();
        if (haveToolBar) {
          if (toolBar != null) {
            if (toolBarVisible) {
              removingToolbarFromSet = true;
              mainPanel.remove(toolBar);
              removingToolbarFromSet = false;
            }
            handle.remove(toolBar.getESlateHandle());
            toolBar = null;
          }
          toolBarVisible = false;
        }

        handle.restoreChildren(map, HOSTED);

        if (haveToolBar) {
          // Find which of our children is a toolbar.
          ESlateHandle[] tbs = handle.getChildrenOfType(
            new Class[]{ESlateToolBar.class}
          );
          if (tbs.length > 0) {
            ESlateHandle h = tbs[0];  // There should be exactly one.
            toolBar = (ESlateToolBar)(h.getComponent());

            identifyTools(toolLocations);  // Update references to tools in the
                                           // toolbar.
            setupToolBar();     // Add listeners.
            localizeToolBar(locale);
            enableToolBarIfEmpty(false);
          }
        }else{
          if (toolBar == null) {
            toolBar = createToolBar(null);
            setupToolBar();     // Add listeners.
            handle.add(toolBar.getESlateHandle());
            toolBarVisible = false;
          }
        }
        setToolBarVisible(tBarVisible);
        //DBase.microworldForReadExternal = null;
      }
      calculate = map.get(CALCULATE, false);
      if (calcButton != null) {
        calcButton.setSelected(calculate);
      }

      deleteQuery = map.get(DELETE_QUERY, false);
      selecting = map.get(SELECTING, false);
      selectOval = map.get(SELECTOVAL, false);
      int last = map.get(LAST_BUTTON, 0);
      if (deleteQuery) {
        if (delButton != null) {
          delButton.setSelected(true);
        }
        switch (last) {
          case 0:
            lastButton = selButton;
            break;
          case 1:
            lastButton = selSetButton;
            break;
          case 2:
            lastButton = selOvalButton;
            break;
          default:
            lastButton = null;
            break;
        }
      }else{
        if (selecting) {
          if (selectOval) {
            if (selOvalButton != null) {
              selOvalButton.setSelected(true);
            }
            lastButton = selOvalButton;
          }else{
            if (selSetButton != null) {
              selSetButton.setSelected(true);
            }
            lastButton = selSetButton;
          }
        }else{
          if (selButton != null) {
            selButton.setSelected(true);
          }
          lastButton = selButton;
        }
      }

      SetPanel.project = map.get(PROJECT, false);
      if (projButton != null) {
        projButton.setSelected(SetPanel.project);
      }

      updateComboBoxStatus();

    }else{
      oldReadExternal(oi, obj);
      showLabels = true;
      uniformProjection = false;
      bgColor = defaultBgColor;
      fillColor = defaultFillColor;
    }
    // If a microworld is not being restored, then ignore any of the
    // saved state that will be used when the component is connected to a
    // database. This will make the component behave better during copy/paste.
    if ((handle == null) ||
        (handle.getESlateMicroworld() == null) ||
        (!handle.getESlateMicroworld().isLoading())) {
      deallocateArrays();
      if (projField != null) {
        projField.removeAllItems();
      }
      vProjField.removeAllItems();
      if (calcOp != null) {
        calcOp.removeAllItems();
      }
      vCalcOp.removeAllItems();
    }

    pm.stop(loadTimer);
    pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Creates the toolbar for the set component.
   * @param     tb      A toolbar to reuse as the component's toolbar.
   *                    Specify <code>null</code> to create a new toolbar.
   * @return The toolbar for the set componnet.
   */
  private ESlateToolBar createToolBar(ESlateToolBar tb)
  {
    if (tb == null) {
      tb = new ESlateToolBar();
    }else{
      tb.removeAll();
    }

    tb.setFloatable(false);
    //tb.setLeadingSeparation(new Dimension(2, 2));

    newButton = new ESlateButton();
    selButton = new ESlateToggleButton();
    selSetButton = new ESlateToggleButton();
    selOvalButton = new ESlateToggleButton();
    delButton = new ESlateToggleButton();
    //copyButton = new ESlateButton();
    projButton = new ESlateToggleButton();
    projField = new ESlateComboBox();
    calcButton = new ESlateToggleButton();
    calcKey = new ESlateComboBox();
    calcOp = new ESlateComboBox();

    String newName = resources.getString("new");
    String selName = resources.getString("select");
    String selSetName = resources.getString("selectSet");
    String selOvalName = resources.getString("selectOval");
    String delName = resources.getString("delete");
    //String copyName = resources.getString("copy");
    String projBName = resources.getString("project");
    String projFName = resources.getString("projField");
    String calcBName = resources.getString("calculate");
    String calcOName = resources.getString("calcOp");
    String calcKName = resources.getString("calcKey");

    newButton.setToolTipText(newName);
    selButton.setToolTipText(selName);
    selSetButton.setToolTipText(selSetName);
    selOvalButton.setToolTipText(selOvalName);
    delButton.setToolTipText(delName);
    //copyButton.setToolTipText(copyName);
    projButton.setToolTipText(projBName);
    projField.setToolTipText(projFName);
    calcButton.setToolTipText(calcBName);
    calcOp.setToolTipText(calcOName);
    calcKey.setToolTipText(calcKName);

    //Border radioButtonBorder = UIManager.getBorder("ToggleButton.border");
    //Border comboBoxBorder = UIManager.getBorder("ComboBox.border");

    delButton.setIcon(new ImageIcon(getClass().getResource("images/del.gif")));
    delButton.setMargin(new Insets(1, 1, 1, 1));
    //delButton.setBorder(radioButtonBorder);
    delButton.setBorderPainted(true);
    //delButton.setBorder(new EmptyBorder(2, 0, 2, 0));
    //delButton.setBorderPainted(false);
    //delButton.setPressedIcon(new ImageIcon(getClass().getResource("del2.gif")));
    //delButton.setSelectedIcon(
    //  new ImageIcon(getClass().getResource("del2.gif"))
    //);
    delButton.setFocusPainted(false);
    delButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);

    newButton.setIcon(new ImageIcon(getClass().getResource("images/new.gif")));
    newButton.setMargin(new Insets(0, 0, 0, 0));
    //newButton.setBorder(new EmptyBorder(2, 0, 2, 0));
    //newButton.setBorderPainted(false);
    //newButton.setPressedIcon(new ImageIcon(getClass().getResource("new2.gif")));
    //newButton.setSelectedIcon(
    //  new ImageIcon(getClass().getResource("new2.gif"))
    //);
    newButton.setFocusPainted(false);
    newButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);

    selButton.setIcon(new ImageIcon(getClass().getResource("images/sel.gif")));
    selButton.setMargin(new Insets(1, 1, 1, 1));
    //selButton.setBorder(radioButtonBorder);
    selButton.setBorderPainted(true);
    //selButton.setBorder(new EmptyBorder(2, 0, 2, 0));
    //selButton.setBorderPainted(false);
    //selButton.setPressedIcon(new ImageIcon(getClass().getResource("sel2.gif")));
    //selButton.setSelectedIcon(
    //  new ImageIcon(getClass().getResource("sel2.gif"))
    //);
    selButton.setFocusPainted(false);
    selButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);

    selSetButton.setIcon(
      new ImageIcon(getClass().getResource("images/selset.gif"))
    );
    selSetButton.setMargin(new Insets(1, 1, 1, 1));
    //selSetButton.setBorder(radioButtonBorder);
    selSetButton.setBorderPainted(true);
    //selSetButton.setBorder(new EmptyBorder(2, 0, 2, 0));
    //selSetButton.setBorderPainted(false);
    //selSetButton.setPressedIcon(
    //  new ImageIcon(getClass().getResource("selset2.gif"))
    //);
    //selSetButton.setSelectedIcon(
    //  new ImageIcon(getClass().getResource("selset2.gif"))
    //);
    selSetButton.setFocusPainted(false);
    selSetButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);

    selOvalButton.setIcon(
      new ImageIcon(getClass().getResource("images/seloval.gif"))
    );
    selOvalButton.setMargin(new Insets(1, 1, 1, 1));
    //selOvalButton.setBorder(radioButtonBorder);
    selOvalButton.setBorderPainted(true);
    //selOvalButton.setBorder(new EmptyBorder(2, 0, 2, 0));
    //selOvalButton.setBorderPainted(false);
    //selOvalButton.setPressedIcon(
    //  new ImageIcon(getClass().getResource("seloval2.gif"))
    //);
    //selOvalButton.setSelectedIcon(
    //  new ImageIcon(getClass().getResource("seloval2.gif"))
    //);
    selOvalButton.setFocusPainted(false);
    selOvalButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
/*
    copyButton.setIcon(
      new ImageIcon(getClass().getResource("images/copy.gif"))
    );
    //copyButtonButton.setMargin(new Insets(0, 0, 0, 0));
    //copyButton.setBorder(new EmptyBorder(2, 0, 2, 0));
    //copyButton.setBorderPainted(false);
    //copyButton.setPressedIcon(
    //  new ImageIcon(getClass().getResource("copy2.gif"))
    //);
    //copyButton.setSelectedIcon(
    //  new ImageIcon(getClass().getResource("copy2.gif"))
    //);
    copyButton.setFocusPainted(false);
    copyButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
    copyButton.setEnabled(false);
*/
    projButton.setIcon(
      new ImageIcon(getClass().getResource("images/proj.gif"))
    );
    projButton.setMargin(new Insets(1, 1, 1, 1));
    //projButton.setBorder(new EmptyBorder(2, 0, 2, 2));
    //projButton.setBorderPainted(false);
    //projButton.setPressedIcon(
    //  new ImageIcon(getClass().getResource("proj2.gif"))
    //);
    //projButton.setSelectedIcon(
    //  new ImageIcon(getClass().getResource("proj2.gif"))
    //);
    projButton.setFocusPainted(false);
    projButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);

    //projField.setBorder(comboBoxBorder);
    projField.setFont(UIManager.getFont("ComboBox.font"));
    projField.setAlignmentY(JComponent.CENTER_ALIGNMENT);
    projField.setPreferredSize(null);
    Dimension prefSize = new Dimension(50, projField.getPreferredSize().height);
    projField.setPreferredSize(prefSize);
    projField.setMaximumSize(projField.getPreferredSize());
    projField.setSize(projField.getPreferredSize());

    calcButton.setIcon(
      new ImageIcon(getClass().getResource("images/calc.gif"))
    );
    calcButton.setMargin(new Insets(1, 1, 1, 1));
    //calcButton.setBorder(new EmptyBorder(2, 0, 2, 2));
    //calcButton.setBorderPainted(false);
    //calcButton.setPressedIcon(
    //  new ImageIcon(getClass().getResource("calc2.gif"))
    //);
    //calcButton.setSelectedIcon(
    //  new ImageIcon(getClass().getResource("calc2.gif"))
    //);
    calcButton.setFocusPainted(false);
    calcButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);

    //calcOp.setBorder(comboBoxBorder);
    calcOp.setFont(UIManager.getFont("ComboBox.font"));
    int n = calcOpNames.length;
    for (int i=0; i<n; i++) {
      calcOp.addItem(calcOpNames[i]);
      vCalcOp.addItem(calcOpNames[i]);
    }
    calcOp.setSelectedItem(resources.getString("count"));
    vCalcOp.setSelectedItem(resources.getString("count"));
    calcOp.setMaximumSize(calcOp.getPreferredSize());
    calcOp.setAlignmentY(JComponent.CENTER_ALIGNMENT);

    //calcKey.setBorder(comboBoxBorder);
    calcKey.setFont(UIManager.getFont("ComboBox.font"));
    calcKey.setAlignmentY(JComponent.CENTER_ALIGNMENT);
    calcKey.setPreferredSize(null);
    prefSize = new Dimension(50, calcKey.getPreferredSize().height);
    calcKey.setPreferredSize(prefSize);
    calcKey.setMaximumSize(calcKey.getPreferredSize());
    calcKey.setSize(calcKey.getPreferredSize());

    delButton.setSelected(false);
    selButton.setSelected(true);
    selSetButton.setSelected(false);
    selOvalButton.setSelected(false);

    lastButton = selButton;

    VisualGroup vg;
    vg = tb.addVisualGroup();
    tb.add(vg, newButton, newName);
    tb.add(vg, delButton, delName);
    vg = tb.addVisualGroup();
    tb.add(vg, selButton, selName);
    tb.add(vg, selSetButton, selSetName);
    tb.add(vg, selOvalButton, selOvalName);
/*
    vg = tb.addVisualGroup();
    tb.add(vg, copyButton, copyName);
*/
    vg = tb.addVisualGroup();
    tb.add(vg, projButton, projBName);
    tb.add(vg, projField, projFName);
    vg = tb.addVisualGroup();
    tb.add(vg, calcButton, calcBName);
    tb.add(vg, calcOp, calcOName);
    //tb.addSeparator(new Dimension(2, 0));
    tb.add(vg, calcKey, calcKName);
    //tb.setBorder(new BevelBorder(BevelBorder.LOWERED));
    tb.setBorder(
      new CompoundBorder(
        new BevelBorder(BevelBorder.LOWERED),
        new EmptyBorder(1, 1, 1, 0)
      )
    );

    ButtonGroup group = tb.addButtonGroup();
    group.add(delButton);
    group.add(selButton);
    group.add(selSetButton);
    group.add(selOvalButton);

    tb.setModified(false);

    tb.setDefaultStateSetter(new DefaultStateSetter()
    {
      public void setDefaultState(ESlateToolBar toolBar)
      {
        Set.this.toolBar = createToolBar(toolBar);
        setupToolBar();
        enableToolBarIfEmpty(false);
      }
    });

    return tb;
  }

  /**
   * Identifies the tools in a toolbar that has just been loaded, updating
   * the references to these tools.
   * @param     loc     The locations of the tools in the toolbar.
   */
  private void identifyTools(ToolLocation[] loc)
  {
    VisualGroup[] groups = toolBar.getVisualGroups();

    if (loc[0] != null) {
      newButton = (ESlateButton)
        (groups[loc[0].visualGroup].getComponent(loc[0].toolIndex));
    }else{
      newButton = null;
    }
    if (loc[1] != null) {
      delButton = (AbstractButton)
        (groups[loc[1].visualGroup].getComponent(loc[1].toolIndex));
    }else{
      delButton = null;
    }
    if (loc[2] != null) {
      selButton = (AbstractButton)
        (groups[loc[2].visualGroup].getComponent(loc[2].toolIndex));
    }else{
      selButton = null;
    }
    if (loc[3] != null) {
      selSetButton = (AbstractButton)
        (groups[loc[3].visualGroup].getComponent(loc[3].toolIndex));
    }else{
      selSetButton = null;
    }
    if (loc[4] != null) {
      selOvalButton = (AbstractButton)
        (groups[loc[4].visualGroup].getComponent(loc[4].toolIndex));
    }else{
      selOvalButton = null;
    }
    if (loc[5] != null) {
      projButton = (AbstractButton)
        (groups[loc[5].visualGroup].getComponent(loc[5].toolIndex));
    }else{
      projButton = null;
    }
    if (loc[6] != null) {
      projField = (ESlateComboBox)
        (groups[loc[6].visualGroup].getComponent(loc[6].toolIndex));
    }else{
      projField = null;
    }
    if (loc[7] != null) {
      calcButton = (AbstractButton)
        (groups[loc[7].visualGroup].getComponent(loc[7].toolIndex));
    }else{
      calcButton = null;
    }
    if (loc[8] != null) {
      calcOp = (ESlateComboBox)
        (groups[loc[8].visualGroup].getComponent(loc[8].toolIndex));
    }else{
      calcOp = null;
    }
    if (loc[9] != null) {
      calcKey = (ESlateComboBox)
        (groups[loc[9].visualGroup].getComponent(loc[9].toolIndex));
    }else{
      calcKey = null;
    }
  }

  /**
   * Returns an array containing the current locations of the tools in the
   * toolbar.
   * @return    An array containing the current locations of the tools in the
   *            toolbar.
   */
  private ToolLocation[] getToolLocations()
  {
    int nTools = 10;
    ToolLocation loc[] = new ToolLocation[nTools];

    if (toolBarVisible) {
      loc[0] = toolBar.locateTool(newButton);
      loc[1] = toolBar.locateTool(delButton);
      loc[2] = toolBar.locateTool(selButton);
      loc[3] = toolBar.locateTool(selSetButton);
      loc[4] = toolBar.locateTool(selOvalButton);
      loc[5] = toolBar.locateTool(projButton);
      loc[6] = toolBar.locateTool(projField);
      loc[7] = toolBar.locateTool(calcButton);
      loc[8] = toolBar.locateTool(calcOp);
      loc[9] = toolBar.locateTool(calcKey);
    }else{
      for (int i=0; i<nTools; i++) {
        loc[i] = null;
      }
    }

    return loc;
  }

  /**
   * Does the necessary adjustments to the toolbar (addition of listeners)
   * so that it will function properly.
   */
  private void setupToolBar()
  {
    if (delButton != null) {
      delButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          delButton_actionPerformed(e);
        }
      });
    }
    if (newButton != null) {
      newButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          newButton_actionPerformed(e);
        }
      });
    }
    if (selButton != null) {
      selButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          selButton_actionPerformed(e);
        }
      });
    }
    if (selSetButton != null) {
      selSetButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          selButton_actionPerformed(e);
        }
      });
    }
    if (selOvalButton != null) {
      selOvalButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          selButton_actionPerformed(e);
        }
      });
    }
    if (projButton != null) {
      projButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          projButton_actionPerformed(e);
        }
      });
    }
    if (projField != null) {
      projField.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          projField_actionPerformed(e);
        }
      });
    }
    if (calcButton != null) {
      calcButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          calcButton_actionPerformed(e);
        }
      });
    }
    if (calcOp != null) {
      calcOp.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          calcOp_actionPerformed(e);
        }
      });
    }
    if (calcKey != null) {
      calcKey.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          calcKey_actionPerformed(e);
        }
      });
    }
  }

  /**
   * Updates locale-dependent parameters of the toolbar, if necessary.
   * @param     oldLocale       The name of the locale under which the toolbar
   *                            had been saved.
   */
  private void localizeToolBar(String oldLocale)
  {
    String newLocale = Locale.getDefault().toString();
    if (!newLocale.equals(oldLocale)) {
      if (newButton != null) {
        newButton.setToolTipText(resources.getString("new"));
      }
      if (selButton != null) {
        selButton.setToolTipText(resources.getString("select"));
      }
      if (selSetButton != null) {
        selSetButton.setToolTipText(resources.getString("selectSet"));
      }
      if (selOvalButton != null) {
        selOvalButton.setToolTipText(resources.getString("selectOval"));
      }
      if (delButton != null) {
        delButton.setToolTipText(resources.getString("delete"));
      }
      //if (copyButton != null) {
      //  copyButton.setToolTipText(resources.getString("copy"));
      //}
      if (projButton != null) {
        projButton.setToolTipText(resources.getString("project"));
      }
      if (projField != null) {
        projField.setToolTipText(resources.getString("projField"));
      }
      if (calcButton != null) {
        calcButton.setToolTipText(resources.getString("calculate"));
      }
      if (calcOp != null) {
        calcOp.setToolTipText(resources.getString("calcOp"));
      }
      if (calcKey != null) {
        calcKey.setToolTipText(resources.getString("calcKey"));
      }
    }
  }

  /**
   * Select a new field for projecting the currently displayed table.
   * @param     e       The event sent when the user selects a new field from
   *                    the projection field combo box.
   */
  private void projField_actionPerformed(ActionEvent e)
  {
    if (projField.isEnabled()) {
      try {
        vProjField.setSelectedIndex(projField.getSelectedIndex());
        SetPanel s = currentSetPanel();
        setProjField(s, actualProjectionField());
        s.repaint();
      } catch (Exception ex) {
      }
    }
  }

  /**
   * Calculate the projection field index to pass to a set panel, based on the
   * current selection of the projection field combo box.
   * @return    If the currently selected projection field is the last in the
   *            combo box, return NOTHING, else return the index of the
   *            currently selected projection field.
   */
  private int actualProjectionField()
  {
    int n = projField.getSelectedIndex();
    if (n == (projField.getItemCount() - 1)) {
      n = NOTHING;
    }
    return n;
  }

  /**
   * Specify whether individual elements or calculated data should be
   * displayed in the set panels.
   */
  private void calcButton_actionPerformed(ActionEvent e)
  {
    calculate = calcButton.isSelected();
    updateComboBoxStatus();
    SetPanel s = currentSetPanel();
    if (s != null) {
      s.repaint();
    }
  }

  /**
   * Select a new field for performing calculations on the currently displayed
   * table.
   * @param     e       The event sent when the user selects a new field from
   *                    the calculation key combo box.
   */
  private void calcKey_actionPerformed(ActionEvent e)
  {
    try {
      int selIndex = calcKey.getSelectedIndex();
      vCalcKey.setSelectedIndex(selIndex);
      SetPanel s = currentSetPanel();
      s.calcKey = selIndex;
      if (calculate) {
        s.repaint();
      }
    } catch (Exception ex) {
    }
  }

  /**
   * Select a new calculation operation.
   * @param     e       The event sent when the user selects a new calculation
   *                    operation from the calculation operation combo box.
   */
  void calcOp_actionPerformed(ActionEvent e)
  {
    try {
      int selIndex = calcOp.getSelectedIndex();
      vCalcOp.setSelectedIndex(selIndex);
      SetPanel s = currentSetPanel();
      s.calcOp = selIndex;
      updateComboBoxes(s);
      if (calculate) {
        s.repaint();
      }
    } catch (Exception ex) {
    }
  }

  /**
   * Process clicks on the "select elements", the "select set", or the
   * "select ellipse" button.
   * @param     e       The event sent when the user clicks on the button.
   */
  private void selButton_actionPerformed(ActionEvent e)
  {
    deleteQuery = false;
    selecting = !selButton.isSelected();
    selectOval = selOvalButton.isSelected();
    if (selButton.isSelected()) {
      lastButton = selButton;
    }else{
      if (selOvalButton.isSelected()) {
        lastButton = selOvalButton;
      }else{
        lastButton = selSetButton;
      }
    }
  }

  /**
   * Process clicks on the "delete" button.
   * @param     e       The event sent when the user clicks on the "delete"
   *                    button.
   */
  private void delButton_actionPerformed(ActionEvent e)
  {
    deleteQuery = delButton.isSelected();
  }

  /**
   * Process clicks on the "new" button.
   * @param     e       The event sent when the user clicks on the "new"
   *                    button.
   */
  private void newButton_actionPerformed(ActionEvent e)
  {
    SetPanel s = currentSetPanel();
    s.addQuery();
  }

  /**
   * Process clicks on the "project selected field" button.
   * @param     e       The event sent when the user clicks on the "project
   *                    selected field" button.
   */
  private void projButton_actionPerformed(ActionEvent e)
  {
    SetPanel.project = projButton.isSelected();
    updateComboBoxStatus();
    SetPanel s = currentSetPanel();
    if (s != null) {
      s.repaint();
    }
  }

  /**
   * Returns the component's E-Slate handle. Required by the ESlatePart
   * interface.
   */
  public ESlateHandle getESlateHandle()
  {
    if (handle == null) {
      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      pm.eSlateAspectInitStarted(this);
      pm.init(initESlateAspectTimer);

      handle = ESlate.registerPart(this);

      initESlate();

      pm.stop(initESlateAspectTimer);
      pm.eSlateAspectInitEnded(this);
      pm.displayTime(initESlateAspectTimer, handle, "", "ms");
    }
    return handle;
  }

  /**
   * Initializes the E-Slate functionality of the component.
   */
  private void initESlate()
  {
    //handle = ESlate.registerPart(this);

    String[] info = {
      resources.getString("credits1"),
      resources.getString("credits2"),
      resources.getString("credits3"),
      resources.getString("credits4"),
      resources.getString("credits5"),
    };
    handle.setInfo(
      new ESlateInfo(
        resources.getString("componentName") + ", " +
          resources.getString("version") + " " + version,
        info
      )
    );
    try {
      handle.setUniqueComponentName(resources.getString("name"));
    } catch (RenamingForbiddenException e) {
    }
    try {
//      Plug plug = new SingleInputPlug(handle, resources, "database",
//                                 Color.magenta,
//                                 gr.cti.eslate.sharedObject.DatabaseSO.class,
//                                 this);
      ProtocolPlug plug = new FemaleSingleIFSingleConnectionProtocolPlug(
        handle, resources, "database", Color.magenta, DBase.class
      );
      plug.setHostingPlug(true);
      plug.addDisconnectionListener(new DisconnectionListener() {
        public void handleDisconnectionEvent(DisconnectionEvent e)
        {
          removeTables();
          if (db != null) {
            int n = db.getTableCount();
            for (int i=0; i<n; i++) {
              db.getTableAt(i).removeTableModelListener(tableAdapter);
            }
            db.removeDatabaseListener(dbAdapter);
          }
          tableAdapter = null;
          dbAdapter = null;
          db = null;
        }
      });
      plug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          connect(e);
        }
      });
      handle.addPlug(plug);
    } catch (Exception e) {
      return;
    }

    handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.SetPrimitives");
    handle.addESlateListener( new ESlateAdapter() {
      public void handleDisposed(HandleDisposalEvent e)
      {
        dispose();
      }
    });

    if (toolBar != null) {
      handle.add(toolBar.getESlateHandle());
    }
  }

  @SuppressWarnings(value={"unchecked"})
  /**
   * Handler for connection events, connecting a database to the set.
   * @param     ce      The connection event to handle.
   */
  private void connect(ConnectionEvent ce)
  {
    activateDB = false;
    if (db != null) {
      int n = db.getTableCount();
      for (int i=0; i<n; i++) {
        db.getTableAt(i).removeTableModelListener(tableAdapter);
      }
      db.removeDatabaseListener(dbAdapter);
      dbAdapter = null;
      tableAdapter = null;
      db = null;
    }

    // Remove any previous panels.
    contents.removeAll();
    setPanels.clear();
    enableToolBarIfEmpty(false);

    // Add panels for the new database.
    ProtocolPlug p = (ProtocolPlug)(ce.getPlug());
    db = (DBase)(p.getProtocolImplementor());
    ArrayList tables = null;
    int size;
    if (db != null) {
      tables = db.getTables();
      size = tables.size();
    }else{
      size = 0;
    }
    int nActualTables = 0;
    boolean tmpUniform = uniformProjection;
    uniformProjection = false;
    for (int i=0; i<size; i++) {
      Table table = (Table)(tables.get(i));
      if (nTables < 0) {
        // Not restoring connection.
        if (!table.isHidden()) {
          enableToolBarIfEmpty(true);
          SetPanel s = new SetPanel(table, this, bgColor, fillColor);
          s.calcOp = vCalcOp.getSelectedIndex();
          s.calcKey = vCalcKey.getSelectedIndex();
          SetDisplayPanel sdp = new SetDisplayPanel(s, showLabels);
          contents.add(table.getTitle(), sdp);
          setPanels.add(sdp);
          s.placeData();
        }
      }else{
        // Restoring connection.
        if (!table.isHidden()) {
          int panelID = -1;
          for (int j=0; j<nTables; j++) {
            if (table.getTitle().equals(titles[j])) {
              panelID = j;
              break;
            }
          }
          enableToolBarIfEmpty(true);
          SetPanel s = new SetPanel(table, this, bgColor, fillColor);
          s.placeWidth = placeWidths[panelID];
          s.placeHeight = placeHeights[panelID];
          s.delayedPlacement = delayedPlacements[panelID];
          for (int j=0; j<8; j++) {
            s.setProjField(j, areaProjFields[panelID][j]);
          }
          s.calcOp = calcOps[panelID];
          s.calcKey = calcKeys[panelID];
          SetDisplayPanel sdp = new SetDisplayPanel(s, showLabels);
          contents.add(table.getTitle(), sdp);
          setPanels.add(sdp);
          s.placeData(
            (ArrayList<Integer>)xs[panelID], (ArrayList<Integer>)ys[panelID]
          );
          s.nOvals = nOvals[panelID];
          s.selected = selected[panelID];
          s.selectOvalsFromSelection();
          s.selText = selTexts[panelID];
          s.ovalsInUse = ovalsInUse[panelID];
          s.oval[0].inUse = oval1InUse[panelID];
          s.oval[1].inUse = oval2InUse[panelID];
          s.oval[2].inUse = oval3InUse[panelID];
          s.oval[0].query = query1[panelID];
          s.oval[1].query = query2[panelID];
          s.oval[2].query = query3[panelID];
          s.updateParentProjField();
          if (activeOvals != null) {
            s.activeOval = activeOvals[panelID];
          }else{
            s.activeOval = -1;
          }
          nActualTables++;
          for (int j=0; j<3; j++) {
            if (s.oval[j].inUse) {
              s.label[j].setText(s.oval[j].query);
              s.label[j].showLabel();
            }
          }
        }
      }
    }
    if (nTables >= 0 && nActualTables !=nTables) {
      ESlateOptionPane.showMessageDialog(
        this, resources.getString("wrongTableNumber"),
        resources.getString("error"), JOptionPane.ERROR_MESSAGE
      );
    }
    if (nTables >= 0) {
      if (nActualTables == nTables) {
        contents.setSelectedIndex(selectedIndex);
        db.activateTable(selectedIndex, true);
      }
      deallocateArrays();
    }else{
      if (db != null) {
        int index;
        // In version 1.9 of the database engine, DBase.getActiveTableIndex()
        // is buggy, returning 0 for empty databases. As we don't know how
        // this will be fixed (returning a negative number or throwing an
        // exception), we make sure that our code works either way, including
        // not fixing the DB engine.
        try {
          index = db.getActiveTableIndex();
        } catch (Exception e) {
          index = -1;
        }
        int panelIndex = panelIndex(index);
        if (panelIndex >= 0) {
          contents.setSelectedIndex(panelIndex);
        }
      }
    }
    SetPanel sp = currentSetPanel();
    if (sp != null) {
      sp.updateParentProjField();
    }
    uniformProjection = tmpUniform;
    if (db != null) {
      dbAdapter = new DatabaseAdapter() {
        public void activeTableChanged(ActiveTableChangedEvent e)
        {
          contents.setSelectedIndex(panelIndex(e.getToIndex()));
        }
        public void tableAdded(TableAddedEvent e)
        {
          addPanel(e.getTable());
        }
        public void tableRemoved(TableRemovedEvent e)
        {
          removePanel(panelIndex(e.getTableIndex()));
        }
        public void tableReplaced(TableReplacedEvent e)
        {
          Table oldTable = e.getReplacedTable();
          Table newTable = e.getNewTable();
          int index = panelIndex(oldTable.getTitle());
          removePanel(index);
          addPanel(newTable, index);
        }
      };
      db.addDatabaseListener(dbAdapter);

      tableAdapter = new DatabaseTableModelAdapter() {
        public void tableRenamed(TableRenamedEvent e)
        {
          int index = panelIndex(e.getNewTitle());
          if (index == -1) {
            index = panelIndex(e.getOldTitle());
          }
          renamePanel(e.getNewTitle(), index);
        }
        public void columnAdded(ColumnAddedEvent e)
        {
          Table t = (Table)(e.getSource());
          updateComboBoxes(
            getSetPanel(setPanels.get(panelIndex(db.indexOf(t))))
          );
        }
        public void columnRemoved(ColumnRemovedEvent e)
        {
          updateProjectionFieldsAfterFieldRemoval(e);
          Table t = (Table)(e.getSource());
          updateComboBoxes(
            getSetPanel(setPanels.get(panelIndex(db.indexOf(t))))
          );
        }
        public void columnRenamed(ColumnRenamedEvent e)
        {
          Table t = (Table)(e.getSource());
          updateComboBoxes(
            getSetPanel(setPanels.get(panelIndex(db.indexOf(t))))
          );
        }
        public void columnTypeChanged(ColumnTypeChangedEvent e)
        {
          Table t = (Table)(e.getSource());
          updateComboBoxes(
            getSetPanel(setPanels.get(panelIndex(db.indexOf(t))))
          );
        }
        public void recordAdded(RecordAddedEvent e) {
          Table t = (Table)(e.getSource());
          SetPanel s =
            getSetPanel(setPanels.get(panelIndex(db.indexOf(t))));
          s.addElement(e.getRecordIndex());
          s.repositionElement(e.getRecordIndex());
        }
        //public void emptyRecordAdded(RecordAddedEvent e)
        //{
        //  Table t = (Table)(e.getSource());
        //  SetPanel s =
        //    getSetPanel(setPanels.get(panelIndex(db.indexOf(t))));
        //  s.addElement(e.getRecordIndex());
        //  s.repositionElement(e.getRecordIndex());
        //}
        public void recordRemoved(RecordRemovedEvent e)
        {
          Table t = (Table)(e.getSource());
          int tb = db.indexOf(t);
          if (tb >= 0) {
            SetPanel s =
              getSetPanel(setPanels.get(panelIndex(tb)));
            s.removeElement(e.getRecordIndex());
          }
        }
        public void cellValueChanged(CellValueChangedEvent e)
        {
          Table t = (Table)(e.getSource());
          SetPanel s =
            getSetPanel(setPanels.get(panelIndex(db.indexOf(t))));
          s.repositionElement(e.getRecordIndex());
          s.repaint();
        }
        public void selectedRecordSetChanged(SelectedRecordSetChangedEvent e)
        {
          Table t = (Table)(e.getSource());
          newSelection(panelIndex(db.indexOf(t)), e.getQueryString());
        }
        public void activeRecordChanged(ActiveRecordChangedEvent e)
        {
          Table t = (Table)(e.getSource());
          SetPanel s =
            getSetPanel(setPanels.get(panelIndex(db.indexOf(t))));
          s.repaint();
        }
      };
      int n = db.getTableCount();
      for (int i=0; i<n; i++) {
        db.getTableAt(i).addTableModelListener(tableAdapter);
      }
    }
  }

  /**
   * Adds a new set panel.
   * @param     table   The table that the panel will represent.
   */
  private void addPanel(Table table)
  {
    addPanel(table, -1);
  }

  /**
   * Adds a new set panel at a paticular position.
   * @param     table   The table that the panel will represent.
   * @param     index   The position of the table. If the position is
   *                    negative, then the panel is added at the end of the
   *                    set of existing panels.
   */
  private void addPanel(Table table, int index)
  {
    enableToolBarIfEmpty(true);
    SetPanel s = new SetPanel(table, this, bgColor, fillColor);
    s.calcOp = vCalcOp.getSelectedIndex();
    s.calcKey = vCalcKey.getSelectedIndex();
    SetDisplayPanel sdp = new SetDisplayPanel(s, showLabels);
    if (index < 0) {
      contents.add(table.getTitle(), sdp);
      setPanels.add(sdp);
    }else{
      contents.add(sdp, index);
      contents.setTitleAt(index, table.getTitle());
      setPanels.add(index, sdp);
    }
    s.placeData();
    table.addTableModelListener(tableAdapter);
  }

  /**
   * Removes a set panel.
   * @param index       The index of the panel to remove.
   */
  private void removePanel(int index)
  {
    SetDisplayPanel s = setPanels.get(index);
    setPanels.remove(index);
    contents.remove(s);
    enableToolBarIfEmpty(false);
    s.setPanel.table.removeTableModelListener(tableAdapter);
  }

  /**
   * Renames a set panel.
   * @param     newName The new name of the panel.
   * @param     index   The panel's index.
   */
  private void renamePanel(String newName, int index)
  {
    contents.setTitleAt(index, newName);
  }

  /**
   * Save the component's state.
   * @param     oo      The stream where the state should be saved.
   */
  @SuppressWarnings(value={"unchecked"})
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(saveTimer);

    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 25);
    
    map.put(CALCULATE, calculate);
    map.put(DELETE_QUERY, deleteQuery);
    map.put(SELECTING, selecting);
    map.put(SELECTOVAL, selectOval);
    map.put(PROJECT, SetPanel.project);
    int last = 0;
    if (lastButton == null || lastButton.equals(selButton)) {
      last = 0;
    }else{
      if (lastButton.equals(selSetButton)) {
        last = 1;
      }else{
        if (lastButton.equals(selOvalButton)) {
          last = 2;
        }
      }
    }
    map.put(LAST_BUTTON, last);
    nTables = contents.getTabCount();
    allocateArrays();
    map.put(N_TABLES, nTables);
    selectedIndex = contents.getSelectedIndex();
    map.put(SELECTED_INDEX, selectedIndex);
    for (int i=0; i<nTables; i++) {
      SetPanel s = getSetPanel(setPanels.get(i));
      titles[i] = contents.getTitleAt(i);
      placeWidths[i] = s.placeWidth;
      placeHeights[i] = s.placeHeight;
      delayedPlacements[i] = s.delayedPlacement;
      areaProjFields[i] = s.getProjFields();
      calcOps[i] = s.calcOp;
      calcKeys[i] = s.calcKey;
      nOvals[i] = s.nOvals;
      selected[i] = s.selected;
      selTexts[i] = s.selText;
      ovalsInUse[i] = s.ovalsInUse;
      oval1InUse[i] = s.oval[0].inUse;
      oval2InUse[i] = s.oval[1].inUse;
      oval3InUse[i] = s.oval[2].inUse;
      query1[i] = s.oval[0].query;
      query2[i] = s.oval[1].query;
      query3[i] = s.oval[2].query;
      int nElements = s.elements.size();
      xs[i] = new ArrayList<Integer>(nElements);
      ys[i] = new ArrayList<Integer>(nElements);
      for (int j=0; j<nElements; j++) {
        SetElement e = s.elements.get(j);
        xs[i].add(e.x);
        ys[i].add(e.y);
      }
      activeOvals[i] = s.activeOval;
    }
    map.put(TITLES, titles);
    map.put(PLACE_WIDTHS, placeWidths);
    map.put(PLACE_HEIGHTS, placeHeights);
    map.put(DELAYED_PLACEMENTS, delayedPlacements);
    map.put(AREA_PROJ_FIELDS, areaProjFields);
    map.put(CALC_OPS, calcOps);
    map.put(CALC_KEYS, calcKeys);
    map.put(N_OVALS, nOvals);
    map.put(SELECTED, selected);
    map.put(SEL_TEXTS, selTexts);
    map.put(OVALS_IN_USE, ovalsInUse);
    map.put(OVAL1_IN_USE, oval1InUse);
    map.put(OVAL2_IN_USE, oval2InUse);
    map.put(OVAL3_IN_USE, oval3InUse);
    map.put(QUERY1, query1);
    map.put(QUERY2, query2);
    map.put(QUERY3, query3);
    map.put(XS, xs);
    map.put(YS, ys);
    map.put(ACTIVE_OVALS, activeOvals);
    map.put(SHOW_LABELS, showLabels);
    map.put(UNIFORM_PROJECTION, uniformProjection);
    map.put(BG_COLOR, colorToInt(bgColor));
    map.put(FILL_COLOR, colorToInt(fillColor));
    map.put(CONNECTED, (db != null));
    map.put(MINIMUM_SIZE, getMinimumSize());
    map.put(MAXIMUM_SIZE, getMaximumSize());
    map.put(PREFERRED_SIZE, getPreferredSize());
    map.put(HAVE_TOOLBAR, true);
    map.put(TOOL_LOCATIONS, getToolLocations());
    map.put(TOOLBAR_VISIBLE, toolBarVisible);
    map.put(LOCALE, Locale.getDefault().toString());

/*
    if ((handle!=null) && (db != null)) {
      ESlateHandle dbh = db.getESlateHandle();
      if (handle.equals(dbh.getParentHandle())) {
        handle.saveChildren(map, HOSTED, new ESlateHandle[]{dbh});
      }
    }
*/

    if (handle != null) {
      ESlateHandle dbh = null;
      boolean saveDB = false;
      if (db != null) {
        dbh = db.getESlateHandle();
        if (handle.equals(dbh.getParentHandle())) {
          saveDB = true;
        }
      }
      ArrayList children = new ArrayList();
      if (saveDB) {
        children.add(dbh);
      }
      if (toolBar != null) {
        children.add(toolBar.getESlateHandle());
      }
      handle.saveChildren(map, HOSTED, children);
    }

    oo.writeObject(map);

    deallocateArrays();

    pm.stop(saveTimer);
    pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Convert a color to an integer.
   * @param     color   The color to convert.
   * @return    A number consisting of the red component in bits 16-23, the
   * green component in bits 8-15, and the blue component in bits 0-7.
   */
  private static int colorToInt(Color color)
  {
    return (color.getRed() << 16) + (color.getGreen() << 8) + color.getBlue();
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    initialize(oi);
  }

  /**
   * Read the state of a component stored in the original save format.
   * @param     oi              The stream from where the state should be
   *                            loaded.
   * @param     firstObj        The first object stored in the component's
   *                            state, which has already been read from the
   *                            stream by readExternal.
   */
  @SuppressWarnings(value={"unchecked"})
  private void oldReadExternal(ObjectInput oi, Object firstObj)
    throws IOException, ClassNotFoundException
  {
    int version = ((Integer)firstObj).intValue();
    int revision = ((Integer)(oi.readObject())).intValue();
    if (version != SAVEVERSION) {
      throw new IOException(
        resources.getString("badVersion1") +
        version + "." + revision + 
        resources.getString("badVersion2") +
        SAVEVERSION +
        resources.getString("badVersion3")
      );
    }

    try {
      calculate = ((Boolean)(oi.readObject())).booleanValue();
      calcButton.setSelected(calculate);
      
      deleteQuery = ((Boolean)(oi.readObject())).booleanValue();

      selecting = ((Boolean)(oi.readObject())).booleanValue();
      if (deleteQuery) {
        delButton.setSelected(deleteQuery);
        lastButton = selButton;
      }else{
        if (selecting) {
          selSetButton.setSelected(true);
          lastButton = selSetButton;
        }else{
          selButton.setSelected(true);
          lastButton = selButton;
        }
      }

      SetPanel.project = ((Boolean)(oi.readObject())).booleanValue();
      projButton.setSelected(SetPanel.project);

      updateComboBoxStatus();

      nTables = ((Integer)(oi.readObject())).intValue();
      allocateArrays();

      selectedIndex = ((Integer)(oi.readObject())).intValue();

      for (int i=0; i<nTables; i++) {
        titles[i] = (String)(oi.readObject());
        placeWidths[i] = ((Integer)(oi.readObject())).intValue();
        placeHeights[i] = ((Integer)(oi.readObject())).intValue();
        delayedPlacements[i] = ((Boolean)(oi.readObject())).booleanValue();
        projFields[i] = ((Integer)(oi.readObject())).intValue();
        for (int j=0; j<nTables; j++) {
          for (int k=0; k<8; k++) {
            areaProjFields[j][k] = projFields[j];
          }
        }
        calcOps[i] = ((Integer)(oi.readObject())).intValue();
        calcKeys[i] = ((Integer)(oi.readObject())).intValue();
        nOvals[i] = ((Integer)(oi.readObject())).intValue();
        selected[i] = (String)(oi.readObject());
        selTexts[i] = (String)(oi.readObject());
        ovalsInUse[i] = (int[])(oi.readObject());
        oval1InUse[i] = ((Boolean)(oi.readObject())).booleanValue();
        query1[i] = (String)(oi.readObject());
        oval2InUse[i] = ((Boolean)(oi.readObject())).booleanValue();
        query2[i] = (String)(oi.readObject());
        oval3InUse[i] = ((Boolean)(oi.readObject())).booleanValue();
        query3[i] = (String)(oi.readObject());
        int nElements = ((Integer)(oi.readObject())).intValue();
        xs[i] = new ArrayList<Integer>(nElements);
        ys[i] = new ArrayList<Integer>(nElements);
        for (int j=0; j<nElements; j++) {
          xs[i].add((Integer)oi.readObject());
          ys[i].add((Integer)oi.readObject());
        }
        activeOvals = null;
      }
    } catch (IOException ex) {
      deallocateArrays();
      throw ex;
    }

    // Older versions of the set component did not store the "connected"
    // information, and were unable to distinguish between restoring an
    // unconnected set and a set connected to a DB with 0 tables, causing an
    // exception when trying to use the set in the former case. As DBs with
    // 0 tables are rather uncommon, assume that this is never the case, so
    // that older microworlds with unconnected sets can be restored.
    if (nTables == 0) {
      nTables = -1;
    }
  }

  /**
   * Allocates the arrays used during loading and saving of the component's
   * state.
   */
  private void allocateArrays()
  {
    titles = new String[nTables];
    placeWidths = new int[nTables];
    placeHeights = new int[nTables];
    delayedPlacements = new boolean[nTables];
    projFields = new int[nTables];
    areaProjFields = new int[nTables][8];
    calcOps = new int[nTables];
    calcKeys = new int[nTables];
    nOvals = new int[nTables];
    selected = new String[nTables];
    selTexts = new String[nTables];
    ovalsInUse = new int[nTables][3];
    oval1InUse = new boolean[nTables];
    oval2InUse = new boolean[nTables];
    oval3InUse = new boolean[nTables];
    query1 = new String[nTables];
    query2 = new String[nTables];
    query3 = new String[nTables];
    xs = new ArrayList[nTables];
    ys = new ArrayList[nTables];
    activeOvals = new int[nTables];
  }

  /**
   * Deallocates the arrays used during loading and saving of the component's
   * state.
   */
  private void deallocateArrays()
  {
    nTables = -1;
    selectedIndex = -1;
    titles = null;
    placeWidths = null;
    placeHeights = null;
    delayedPlacements = null;
    projFields = null;
    areaProjFields = null;
    calcOps = null;
    calcKeys = null;
    nOvals = null;
    selected = null;
    selTexts = null;
    ovalsInUse = null;
    oval1InUse = null;
    oval2InUse = null;
    oval3InUse = null;
    query1 = null;
    query2 = null;
    query3 = null;
    xs = null;
    ys = null;
    activeOvals = null;
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
   * Run the component as an application.
   * @param     args    Application's arguments (not used).
   */
  public static void main(String args[])
  {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
      JFrame f = new JFrame();
      f.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          e.getWindow().setVisible(false);
          System.exit(0);
        }
      });
      Set s = new Set();
      f.setTitle(s.resources.getString("name"));
      f.getContentPane().add(s);
      f.pack();
      f.setVisible(true);
    } catch (Exception e) {
    }
  }

  /**
   * Handles changes in the selected record set of a table.
   * @param     index   The index of the set element corresponding to the
   *                    table whose record set has changed.
   * @param     query   The text of the query that resulted in the selection.
   */
  private void newSelection(int index, String query)
  {
    SetPanel s = getSetPanel(setPanels.get(index));
    s.markSelectedElements();
    if (query != null) {
      s.processQuery(query);
    };
  }

  /**
   * Updates the combo boxes in the component with values from the currently
   * selected panel.
   * @param     s       The currently selected panel.
   */
  private void updateComboBoxes(SetPanel s)
  {
    // Updating the combo boxes will invoke their action performed handlers,
    // resulting in the destruction of these fields, so keep copies before
    // we start fooling around.
    int oldProjField = 0;
    int oldCalcOp = 0;
    int oldCalcKey = 0;
    if (s != null) {
      int pf = s.getProjField();
      if (pf == NOTHING) {
        pf = vProjField.getItemCount() - 1;
      }
      oldProjField = pf >= 0 ? pf : 0 ;
      oldCalcOp = s.calcOp >= 0 ? s.calcOp : 0;
      oldCalcKey = s.calcKey >= 0 ? s.calcKey : 0;
    }

    if (vProjField.getItemCount() > 0) {
      if (projField != null) {
        projField.removeAllItems();
      }
      vProjField.removeAllItems();
    }
    if (vCalcKey.getItemCount() > 0) {
      if (calcKey != null) {
        calcKey.removeAllItems();
      }
      vCalcKey.removeAllItems();
    }
    int size1 = 0;
    int size2 = 0;
    if (s != null) {
      String[] fields = s.getFieldNames();
      size1 = fields.length;
      for (int i=0; i<size1; i++) {
        if (projField != null) {
          projField.addItem(fields[i]);
        }
        vProjField.addItem(fields[i]);
      }
      if (projField != null) {
        projField.addItem(resources.getString("none"));
      }
      vProjField.addItem(resources.getString("none"));

      if (oldCalcOp >= TOTAL) {
        if (oldCalcOp == PERCENT) {
          fields = s.getFieldNames(Boolean.class);
        }else{
          fields = s.getFieldNames(Number.class);
        }
        size2 = fields.length;
        for (int i=0; i<size2; i++) {
          if (calcKey != null) {
            calcKey.addItem(fields[i]);
          }
          vCalcKey.addItem(fields[i]);
        }
      }
    }
    if (projField != null) {
      projField.setPreferredSize(null);
      if (size1 == 0) {
        Dimension prefSize =
          new Dimension(50, projField.getPreferredSize().height);
        projField.setPreferredSize(prefSize);
      }
      projField.setMaximumSize(projField.getPreferredSize());
      projField.setSize(projField.getPreferredSize());
    }
    if (calcKey != null) {
      calcKey.setPreferredSize(null);
      if (size2 == 0) {
        Dimension prefSize =
          new Dimension(50, calcKey.getPreferredSize().height);
        calcKey.setPreferredSize(prefSize);
      }
      calcKey.setMaximumSize(calcKey.getPreferredSize());
      calcKey.setSize(calcKey.getPreferredSize());
    }

    if (s != null) {
      try {
        int nItems = vProjField.getItemCount();
        if (oldProjField >= nItems) {
          // Projection field was NOTHING--i.e., the last one--and we are
          // updating the combo boxes after a field deletion, rsulting in
          // oldProjField having too high a value.
          oldProjField = nItems - 1;
        }
        if (projField != null) {
          projField.setSelectedIndex(oldProjField);
        }
        vProjField.setSelectedIndex(oldProjField);
      } catch (IllegalArgumentException e) {
        setProjField(s, vProjField.getSelectedIndex());
      }
      try {
        if (calcKey != null) {
          calcKey.setSelectedIndex(oldCalcKey);
        }
        vCalcKey.setSelectedIndex(oldCalcKey);
      } catch (IllegalArgumentException e) {
        s.calcKey = vCalcKey.getSelectedIndex();
      }
      try {
        if (calcOp != null) {
          calcOp.setSelectedIndex(oldCalcOp);
        }
        vCalcOp.setSelectedIndex(oldCalcOp);
      } catch (IllegalArgumentException e) {
        s.calcOp = vCalcOp.getSelectedIndex();
      }
    }
  }

  /**
   * Update the projection fields of a set panel after removing a field from
   * the associated table.
   * @param     e       The event signaling the removal of the field.
   */
  private void updateProjectionFieldsAfterFieldRemoval(ColumnRemovedEvent e)
  {
    String columnName = e.getColumnName();
    int n = vProjField.getItemCount();
    int index = -1;
    for (int i=0; i<n; i++) {
      if (columnName.equals((String)(vProjField.getItemAt(i)))) {
        index = i;
        break;
      }
    }
    if (index >= 0) {
      Table t = (Table)(e.getSource());
      int pIndex = panelIndex(db.indexOf(t));
      SetPanel s = getSetPanel(setPanels.get(pIndex));
      int[] pf = s.getProjFields();
      for (int i=0; i<8; i++) {
        if (pf[i] == index) {
          s.setProjField(i, NOTHING);
        }
        if (pf[i] > index) {
          s.setProjField(i, pf[i]-1);
        }
      }
      if ((pIndex == contents.getSelectedIndex()) &&
          (vProjField.getSelectedIndex() == index)) {
        int selIndex = projField.getItemCount()-1;
        if (projField != null) {
          projField.setSelectedIndex(selIndex);
        }
        vProjField.setSelectedIndex(selIndex);
      }
    }
  }

  /**
   * Remove all tables from the components.
   */
  private void removeTables()
  {
    contents.removeAll();
    setPanels.clear();
    updateComboBoxes(null);
    enableToolBarIfEmpty(false);
    repaint();
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
    ESlateHandle.removeAllRecursively(this);
    statusBar = null;
    toolBar = null;
    cardPanel.removeAll();
    contents = null;
    emptyPanel = null;
    delButton = null;
//    copyButton = null;
    selButton = null;
    selSetButton = null;
    selOvalButton = null;
    lastButton = null;
    projButton = null;
    projField = null;
    vProjField = null;
    calcButton = null;
    calcKey = null;
    vCalcKey = null;
    calcOp = null;
    vCalcOp = null;
    mainPanel = null;
    resources = null;
    nf = null;
    if (db != null) {
      int n = db.getTableCount();
      for (int i=0; i<n; i++) {
        db.getTableAt(i).removeTableModelListener(tableAdapter);
      }
      db.removeDatabaseListener(dbAdapter);
      db = null;
    }
    dbAdapter = null;
    tableAdapter = null;
    if (setPanels != null) {
      setPanels.clear();
      setPanels = null;
    }
    deallocateArrays();

    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.removePerformanceListener(perfListener);
    perfListener = null;
  }

// Methods for the AsSet interface.

  /**
   * Selects the subset at the given coordinates.
   * @param     x       X coordinate of the subset.
   * @param     y       Y coordinate of the subset.
   */
  public void selectSubset(int x, int y)
  {
    SetPanel s = currentSetPanel();
    s.setSelected(null, true);
    s.select(x, y);
  }

  /**
   * Selects a subset.
   * @param     a       Subset belongs in the first (top) oval.
   * @param     b       Subset belongs in the second (left) oval.
   * @param     c       Subset belongs in the third (right) oval.
   */
  public void selectSubset(boolean a, boolean b, boolean c)
  {
    SetPanel s = currentSetPanel();
    s.setSelected(null, true);
    s.select(a, b, c);
  }

  /**
   * Clears the selected subset.
   */
  public void clearSelectedSubset()
  {
    SetPanel s = currentSetPanel();
    s.setSelected(null);
  }

  /**
   * Returns the description of the selected subset.
   * @return    The requested description.
   */
  public String getSelText()
  {
    SetPanel s = currentSetPanel();
    return s.selText;
  }

  /**
   * Deletes the ellipse(s) at the given coordinates.
   * @param     x       X coordinate of the ellipse(s) to delete.
   * @param     y       Y coordinate of the ellipse(s) to delete.
   */
  public void deleteEllipse(int x, int y)
  {
    SetPanel s = currentSetPanel();
    s.removeOvals(x, y);
  }

  /**
   * Deletes one or more ellipses.
   * @param     a       Delete the first (top ellipse).
   * @param     b       Delete the second (left) ellipse.
   * @param     c       Delete the third (right) ellipse.
   */
  public void deleteEllipse(boolean a, boolean b, boolean c)
  {
    SetPanel s = currentSetPanel();
    if (a) {
      s.removeOval(0);
    }
    if (b) {
      s.removeOval(1);
    }
    if (c) {
      s.removeOval(2);
    }
  }

  /**
   * Select the field projected onto the component for the currently selected
   * subset.
   * @param     name    The name of the field to project.
   * @exception SetException    Thrown if the requested field does not exist.
   */
  public void setProjectionField(String name) throws SetException
  {
    int size = vProjField.getItemCount();
    int i;
    for (i=0; i<size; i++) {
      String s = vProjField.getItemAt(i);
      if (s.equals(name)) {
        break;
      }
    }
    if (i < size) {
      if (projField != null) {
        projField.setSelectedIndex(i);
      }
      vProjField.setSelectedIndex(i);
    }else{
      SetException e = new SetException(
        resources.getString("badField1") + " " + name + " " +
        resources.getString("badField2")
      );
      throw e;
    }
  }

  /**
   * Return the name of the field projected onto the component for the
   * currently selected subset.
   * @return    The requested name.
   */
  public String getProjectionField()
  {
    return vProjField.getSelectedItem();
  }

  /**
   * Return the names of the fields that can be projected onto the component.
   * @return    An array containing the requested names.
   */
  public String[] getProjectionFields()
  {
    int n = vProjField.getItemCount();
    String names[] = new String[n];
    for (int i=0; i<n; i++) {
      names[i] = vProjField.getItemAt(i);
    }
    return names;
  }

  /**
   * Select the calculation operation to perform.
   * @param     name    The name of the operation.
   * @exception SetException    Thrown if the requested operation is not
   *                            supported.
   */
  public void setCalcOp(String name) throws SetException
  {
    int size = vCalcOp.getItemCount();
    int i;
    for (i=0; i<size; i++) {
      String s = (String)(vCalcOp.getItemAt(i));
      if (s.equals(name)) {
        break;
      }
    }
    if (i < size) {
      vCalcOp.setSelectedIndex(i);
      if (calcOp != null) {
        calcOp.setSelectedIndex(i);
      }else{
        SetPanel s = currentSetPanel();
        s.calcOp = i;
      }
    }else{
      SetException e = new SetException(
        resources.getString("badOp1") + " " + name + " " +
        resources.getString("badOp2")
      );
      throw e;
    }
  }

  /**
   * Return the name of the calculation operation that is being performed.
   * @return    The requested name.
   */
  public String getCalcOp()
  {
    return (String)(vCalcOp.getSelectedItem());
  }

  /**
   * Return the names of the calculation operations that can be performed.
   * @return    An array containing the requested names.
   */
  public static String[] getCalculationOperations()
  {
    int n = calcOpNames.length;
    String s[] = new String[n];
    System.arraycopy(calcOpNames, 0, s, 0, n);
    return s;
  }

  /**
   * Return the names of the calculation operations that can be performed.
   * Required by AsSet interface, where the method cannot be static.
   * @return    An array containing the requested names.
   */
  public String[] getCalcOps()
  {
    return getCalculationOperations();
  }

  /**
   * Select the field on which to perform
   * @param     name    The name of the operation.
   * @exception SetException    Thrown if the field does not exist, or the
   *                            currently selected operation cannot be
   *                            performed on this field.
   */
  public void setCalcKey(String name) throws SetException
  {
    int size = vCalcKey.getItemCount();
    int i;
    for (i=0; i<size; i++) {
      String s = vCalcKey.getItemAt(i);
      if (s.equals(name)) {
        break;
      }
    }
    if (i < size) {
      if (calcKey != null) {
        calcKey.setSelectedIndex(i);
      }
      vCalcKey.setSelectedIndex(i);
    }else{
      SetException e = new SetException(
        resources.getString("badKey") + ": " + name
      );
      throw e;
    }
  }

  /**
   * Return the name of the field on which calculations are being performed.
   * @return    The requested name.
   */
  public String getCalcKey()
  {
    return vCalcKey.getSelectedItem();
  }

  /**
   * Return the names of the fields on which calculations can be performed.
   * @return    An array containing the requested names.
   */
  public String[] getCalcKeys()
  {
    int n = vCalcKey.getItemCount();
    String s[] = new String[n];
    for (int i=0; i<n; i++) {
      s[i] = vCalcKey.getItemAt(i);
    }
    return s;
  }

  /**
   * Select the table to display.
   * @param     name    The name of the table.
   * @exception SetException    Thrown if the requested table does not exist.
   */
  public void setSelectedTable(String name) throws SetException
  {
    int size = contents.getTabCount();
    int i;
    for (i=0; i<size; i++) {
      String s = contents.getTitleAt(i);
      if (s.equals(name)) {
        break;
      }
    }
    if (i < size) {
      contents.setSelectedIndex(i);
    }else{
      SetException e = new SetException(
        resources.getString("badTable1") + " " + name + " " +
        resources.getString("badTable2")
      );
      throw e;
    }
  }

  /**
   * Return the name of the displayed table.
   * @return    The requested name.
   */
  public String getSelectedTable()
  {
    if (contents.getTabCount() > 0) {
      return contents.getTitleAt(contents.getSelectedIndex());
    }else{
      return null;
    }
  }

  /**
   * Return the names of the tables that can be displayed.
   * @return    An array containing the requested names.
   */
  public String[] getTables()
  {
    int size = contents.getTabCount();
    String[] s = new String[size];
    for (int i=0; i<size; i++) {
      s[i] = contents.getTitleAt(i);
    }
    return s;
  }

  /**
   * Specify whether the selected projection field should be displayed.
   * @param     status  True if yes, false otherwise.
   */
  public void setProject(boolean status)
  {
    projButton.setSelected(status);
    SetPanel.project = projButton.isSelected();
    updateComboBoxStatus();
    SetPanel s = currentSetPanel();
    if (s != null) {
      s.repaint();
    }
  }

  /**
   * Return whether the selected projection field is being displayed.
   * @return    True if yes, false if no.
   */
  public boolean isProjecting()
  {
    return projButton.isSelected();
  }

  /**
   * Specify whether calculations should be displayed.
   * @param     status  True if yes, false otherwise.
   */
  public void setCalculate(boolean status)
  {
    calcButton.setSelected(status);
    calculate = calcButton.isSelected();
    SetPanel s = currentSetPanel();
    if (s != null) {
      s.repaint();
    }
  }

  /**
   * Returns whether calculations are displayed.
   * @return    True if yes, false if no.
   */
  public boolean isCalculating()
  {
    return calcButton.isSelected();
  }

  /**
   * Returns the index of the set panel corresponding to a given table index.
   * @param     tableIndex      The index of the table.
   * @return    The index of the corresponding set panel or -1 if that table
   *            does not have a corresponding set panel.
   */
  private int panelIndex(int tableIndex)
  {
    if (tableIndex >= 0) {
      Table t = db.getTableAt(tableIndex);
      if (t != null) {
        return panelIndex(db.getTableAt(tableIndex).getTitle());
      }else{
        return -1;
      }
    }else{
      return -1;
    }
  }

  /**
   * Returns the index of the set panel corresponding to a given table index.
   * @param     title           The title of the set panel.
   * @return    The index of the corresponding set panel or -1 if that table
   *            does not have a corresponding set panel.
   */
  private int panelIndex(String title)
  {
    int n = contents.getTabCount();
    int result = -1;
    for (int i=0; i<n; i++) {
      if (title.equals(contents.getTitleAt(i))) {
        result = i;
        // Do not break. When creating new tables, the table component
        // assigns it a default name. If that name is already in use, then
        // it modifies it to make it unique. If we get a rename event for
        // such an event, then we want to return the index of the last table,
        // which is the last one. In all other cases, there is only one table
        // having a given name, so we just do a little more work.
      }
    }
    return result;
  }

  /**
   * Returns the SetPanel that is currently being displayed.
   * @return    The requested SetPanel or null if there is no panel currently
   *            being displayed.
   */
  private SetPanel currentSetPanel()
  {
    if (contents.getTabCount() <= 0) {
      return null;
    }else{
      return ((SetDisplayPanel)(contents.getSelectedComponent())).setPanel;
    }
  }

  /**
   * Returns the SetPanel contained in a given SetDisplayPanel.
   * param      sdp     The SetDisplayPanel.
   * @return    The requested SetPanel.
   */
  private SetPanel getSetPanel(SetDisplayPanel sdp)
  {
    return sdp.setPanel;
  }

  /**
   * Checks whether the query text is displayed next to each ellipse.
   * @return    True if yes, false if no.
   */
  public boolean isShowLabels()
  {
    return showLabels;
  }

  /**
   * Specifies whether the query text is displayed next to each ellipse.
   * @param     state   True if yes, false if no.
   */
  public void setShowLabels(boolean state)
  {
    if (state != showLabels) {
      int nPanels = setPanels.size();
      for (int i=0; i<nPanels; i++) {
        SetDisplayPanel sdp = setPanels.get(i);
        if (state) {
          sdp.showLabels();
        }else{
          sdp.hideLabels();
        }
      }
      showLabels = state;
    }
  }

  /**
   * Checks whether the same projection field is used for all subsets.
   * @return    True if yes, false if no.
   */
  public boolean isUniformProjection()
  {
    return uniformProjection;
  }

  /**
   * Specifies whether the same projection field is used for all subsets.
   * @param     state   True if yes, false if no.
   */
  public void setUniformProjection(boolean state)
  {
    if (state != uniformProjection) {
      uniformProjection = state;
      if (uniformProjection) {
        int nPanels = setPanels.size();
        for (int i=0; i<nPanels; i++) {
          SetPanel sp = setPanels.get(i).setPanel;
          int pf = sp.getProjField();
          if ((pf < 0) && (pf != NOTHING)) {
            pf = 0;
          }
          setProjField(sp, pf);
        }
        if (projField != null) {
          projField.setEnabled(true);
        }
        repaint();
      }else{
        currentSetPanel().updateParentProjField();
      }
    }
  }

  /**
   * Creates and activates a new ellipse.
   */
  public void newEllipse()
  {
    SetPanel s = currentSetPanel();
    s.addQuery();
  }

  /**
   * Activates the ellipse at the given coordinates.
   * @param     x       The x coordinate of the ellipse.
   * @param     y       The y coordinate of the ellipse.
   */
  public void activateEllipse(int x, int y)
  {
    SetPanel s = currentSetPanel();
    s.selectOval(x, y);
  }

  /**
   * Activates an ellipse.
   * @param     n       The ellipse to activate: 0=top, 1=bottom left,
   *                    2=bottom right.
   */
  public void activateEllipse(int n)
  {
    SetPanel s = currentSetPanel();
    s.selectOval(SetPanel.NO_OVAL);
    s.selectOval(n);
  }

  /**
   * Deactivates the active ellipse.
   */
  public void deactivateEllipse()
  {
    SetPanel s = currentSetPanel();
    s.selectOval(SetPanel.NO_OVAL);
  }

  /**
   * Changes the value of the projection field.
   * @param     value   The new value of the projection field. If
   *                    <code>value<code> is negative, the projection field is
   *                    disabled.
   */
  void changeProjField(int value)
  {
    if (value == NOTHING) {
      value = vProjField.getItemCount() - 1;
    }
    if (!uniformProjection) {
      if (projField != null) {
        if ((value >= 0) && SetPanel.project) {
          projField.setEnabled(true);
          projField.setSelectedIndex(value);
        }else{
          projField.setEnabled(false);
        }
      }
    }
  }

  /**
   * Set the projection field of a hosted set panel. If the uniform projection
   * fields is on, the projection fields of all possible subsets are set to
   * the given value, otherwise only the projection field of the currently
   * selected set is modified.
   * @param     s       The set panel.
   * @param     index   The new projection field.
   */
  private void setProjField(SetPanel s, int index)
  {
    if (uniformProjection) {
      for (int i=0; i<8; i++) {
        s.setProjField(i, index);
      }
    }else{
      s.setProjField(index);
    }
  }

  /**
   * Sets the color used for the background of the set panels.
   * @param     color   The color to use.
   */
  public void setBackgroundColor(Color color)
  {
    int nPanels = setPanels.size();
    for (int i=0; i<nPanels; i++) {
      SetPanel sp = setPanels.get(i).setPanel;
      sp.setBackgroundColor(color);
    }
    bgColor = color;
  }

  /**
   * Returns the color used for the background of the panel.
   * @return    The requested color.
   */
  public Color getBackgroundColor()
  {
    return bgColor;
  }

  /**
   * Sets the color used for the selected subset.
   * @param     color   The color to use.
   */
  public void setSelectionColor(Color color)
  {
    int nPanels = setPanels.size();
    for (int i=0; i<nPanels; i++) {
      SetPanel sp = setPanels.get(i).setPanel;
      sp.setSelectionColor(color);
    }
    fillColor = color;
  }

  /**
   * Returns the color used for the selected subset.
   * @return    The requested color.
   */
  public Color getSelectionColor()
  {
    return fillColor;
  }

  /**
   * Sets the activation state of all the gadgets in the tool bar if there are
   * no tables displayed in the component. In addition, the empty panel is
   * brought accordingly to the foreground or background.
   * @param     state   If false, all gadgets are disabled. If true and there
   *                    are no tables displayed in the component, all gadgets
   *                    are enabled.
   */
  private void enableToolBarIfEmpty(boolean state)
  {
    if (setPanels.size() == 0) {
      if (newButton != null) {
        newButton.setEnabled(state);
      }
      if (delButton != null) {
        delButton.setEnabled(state);
      }
      if (selButton != null) {
        selButton.setEnabled(state);
      }
      if (selSetButton != null) {
        selSetButton.setEnabled(state);
      }
      if (selOvalButton != null) {
        selOvalButton.setEnabled(state);
      }
      //if (copyButton != null) {
      //  copyButton.setEnabled(state);
      //}
      if (projButton != null) {
        projButton.setEnabled(state);
      }
      if (projField != null) {
        if (SetPanel.project && state) {
          projField.setEnabled(true);
        }else{
          projField.setEnabled(false);
        }
      }
      if (calcButton != null) {
        calcButton.setEnabled(state);
      }
      if (calculate && state) {
        if (calcOp != null) {
          calcOp.setEnabled(true);
        }
        if (calcKey != null) {
          calcKey.setEnabled(true);
        }
      }
      if (state) {
        cardPanel.showCard(CONTENTS);
      }else{
        cardPanel.showCard(EMPTY);
      }
    }
  }

  /**
   * Enables or disables the combo boxes depending on whether the
   * corresponding buttons are pressed or not.
   */
  private void updateComboBoxStatus()
  {
    if (projField != null) {
      if (SetPanel.project) {
        SetPanel s = currentSetPanel();
        if ((s != null) && (s.selected != null)) {
          projField.setEnabled(true);
        }else{
          projField.setEnabled(false);
        }
      }else{
        projField.setEnabled(false);
      }
    }
    if (calculate) {
      if (calcOp != null) {
        calcOp.setEnabled(true);
      }
      if (calcKey != null) {
        calcKey.setEnabled(true);
      }
    }else{
      if (calcOp != null) {
        calcOp.setEnabled(false);
      }
      if (calcKey != null) {
        calcKey.setEnabled(false);
      }
    }
  }

  /**
   * Specifies whether the component's toolbar will be shown.
   * @param     visible True if yes, false if no.
   */
  public void setToolBarVisible(boolean visible)
  {
    if (visible != toolBarVisible) {
      toolBarVisible = visible;
      if (toolBarVisible) {
        if (toolBar == null) {
          toolBar = createToolBar(null);
          handle.add(toolBar.getESlateHandle());
          setupToolBar();       // Add listeners.
          enableToolBarIfEmpty(false);
        }
        mainPanel.add(toolBar, BorderLayout.NORTH);
      }else{
        removingToolbarFromSet = true;
        mainPanel.remove(toolBar);
        removingToolbarFromSet = false;
      }
      revalidate();
    }
  }

  /**
   * Checks whether the component's toolbar is shown.
   * @return    True if yes, false if no.
   */
  public boolean isToolBarVisible()
  {
    return toolBarVisible;
  }

  /**
   * Handle the removal of components from the main panel. In particular,
   * watch for the removal of the toolbar from outside the program.
   * @param     e       The event received when a component is removed.
   */
  private void componentRemoved(ContainerEvent e)
  {
    if (!removingToolbarFromSet) {
      Component c = e.getChild();
      if (c.equals(toolBar)) {
        toolBar = null;
        toolBarVisible = false;
      }
    }
  }

  /**
   * Checks if the component has been iconified in the E-Slate desktop.
   * @return    True if the component has been iconified, false otherwise.
   */
  boolean isIconified()
  {
     Container c = this;
     while (c != null && ! (c instanceof ESlateInternalFrame)) {
       c = c.getParent();
     }
     if (c != null) {
       return !c.isVisible();
     }else{
       return false;
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
