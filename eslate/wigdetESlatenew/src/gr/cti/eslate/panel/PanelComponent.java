package gr.cti.eslate.panel;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.base.container.internalFrame.*;
import gr.cti.eslate.base.effect.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.shapedComponent.*;
import gr.cti.eslate.utils.*;

import gr.cti.typeArray.*;

/**
 * This class implements a panel that can contain other components.
 * Components are added to the panel component's
 * content pane using the E-Slate workbench's component
 * editor. If a hosted component is an E-Slate component, its plugs are added
 * to the panel component's hierarchy.
 * <P>
 * <B>Component plugs:</B>
 * Those of the hosted E-Slate components.
 * <P>
 * <B>Logo primitives:</B>
 * <UL>
 * <LI><B>PANEL.LISTCOMPONENTS</B> returns a list of the names of all hosted
 * E-Slate components.</LI>
 * </UL>
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 */
public class PanelComponent extends JRootPane
  implements ESlatePart, Externalizable, Serializable, AsPanel,
             ContainerListener, ComponentListener, ESlateListener,
             ShapedComponent
{
  /**
   * Display background image in the top left corner.
   */
  public final static int BG_NONE = 0;
  /**
   * Center background image.
   */
  public final static int BG_CENTERED = 1;
  /**
   * Stretch background image.
   */
  public final static int BG_STRETCHED = 2;
  /**
   * Tile background image.
   */
  public final static int BG_TILED = 3;
  private final static String version = "2.0.3";
  /**
   * Format version for saving panels.
   */
  private final static int STORAGE_VERSION = 1;
  /**
   * Format version for saving the contents of the panel.
   */
  private final static int SAVE_COMPONENTS_VERSION = 1;
  private ESlateHandle handle = null;
  static ResourceBundle resources =
    ResourceBundle.getBundle(
      "gr.cti.eslate.panel.PanelResource", Locale.getDefault()
    );
  ContentPane cp;
  private JComponent gp;
  private boolean designMode = false;
  private Cursor forbiddenCursor = null;
  private ComponentBaseArray selectedComponents = new ComponentBaseArray();
  private Rectangle[] resizeHandles;
  private int resizeCursors[] = new int[] {
    Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
    Cursor.W_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR,
    Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
  };
  private Rectangle tmpRect1 = new Rectangle();
  private Rectangle tmpRect2 = new Rectangle();
  private Point tmpPt = new Point();
  private int resizeDir = -1;
  private boolean alignToGrid = false;
  private Color selectionColor = Color.red;
  private boolean skipWriteExternal = false;
  private boolean inActivateComponent = false;
  private Point clickPoint = null;
  private boolean selecting = false;
  private int offX = 0;
  private int offY = 0;
  private int clipType = ShapeType.RECTANGLE;
  private Polygon clipPoly = null;
  Shape clipShape = null;
  Rectangle shapeBounds = null;
  private MouseInputListener designModeMouseInputListener;
  private Point currentPoint = null;
  private Component lastComp = null;
  static ComponentBaseArray exclude = new ComponentBaseArray();
  private PanelFileFilter fileFilter;
  private final static String PANEL_EXT = ".panel";
  private final static String PCONT_EXT = ".pcont";
  private boolean simplePaint = false;
  private ShapeChangedListenerSupport listeners;
  private EffectRunner effectRunner = null;
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
  private JFileChooser fileChooser = null;
  private File currentDir = null;
  private final static int TOP_LEFT = 0;
  private final static int TOP = 1;
  private final static int TOP_RIGHT = 2;
  private final static int LEFT = 3;
  private final static int RIGHT = 4;
  private final static int BOTTOM_LEFT = 5;
  private final static int BOTTOM = 6;
  private final static int BOTTOM_RIGHT = 7;
  private final static Dimension winSize = new Dimension(320, 240);
  private final static Dimension minWinSize = new Dimension(10, 10);
  private final static Dimension maxWinSize =
    new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
  private ActivationHandleGroup ahg;

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

  private final static String BACKGROUND = "background";
  private final static String BORDER = "border";
  private final static String LAYOUT = "layout";
  private final static String LAYOUT_PARAMETERS = "layoutParameters";
  private final static String COMPONENT_NAMES = "componentNames";
  private final static String COMPONENT_CLASSES = "componentClasses";
  private final static String COMPONENT_WIDTHS = "componentWidths";
  private final static String COMPONENT_HEIGHTS = "componentHeights";
  private final static String COMPONENT_XS = "componentXs";
  private final static String COMPONENT_YS = "componentYs";
  private final static String RIGHT_RENAMES = "rightRenames";
  private final static String DESIGN_MODE = "designMode";
  private final static String BACKGROUND_IMAGE = "backgroundImage";
  private final static String TRANSPARENT = "transparent";
  private final static String BG_STYLE = "bgStyle";
  private final static String SELECTED_COMPONENT = "selectedComponent";
  private final static String SELECTED_COMPONENTS = "selectedComponents";
  private final static String GRID_STEP = "gridStep";
  private final static String GRID_COLOR = "gridColor";
  private final static String GRID_VISIBLE = "gridVisible";
  private final static String ALIGN_TO_GRID = "alignToGrid";
  private final static String SELECTION_COLOR = "selectionColor";
  private final static String CONSTRAINTS = "constraints";
  private final static String COMPONENTS = "components";
  private final static String CHILDREN = "children";
  private final static String MINIMUM_SIZE = "minimumSize";
  private final static String MAXIMUM_SIZE = "maximumSIze";
  private final static String PREFERRED_SIZE = "preferredSize";
  private final static String CLIP_TYPE = "clipType";
  private final static String CLIP_POLY_X = "clipPolyX";
  private final static String CLIP_POLY_Y = "clipPolyY";
  private final static String CLIP_SHAPE = "clipShape";
  static final long serialVersionUID = 1L;

//  static {
//    Toolkit.getDefaultToolkit().getSystemEventQueue().push(
//      new PanelEventQueue()
//    );
//  };

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
   * Create a panel component.
   */
  public PanelComponent()
  {
    super();

    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);

    setPreferredSize(PanelComponent.winSize);
    setMinimumSize(PanelComponent.minWinSize);
    setMaximumSize(PanelComponent.maxWinSize);

    cp = new ContentPane();
    cp.initted = true;
    cp.addContainerListener(this);
    setContentPane(cp);
    // As far as Swing is concerned the panel is a transparent component.
    // This is necessary for clipping to arbitrary shapes to work...
    setOpaque(false);
    // ...so we implement our own transparency mechanism.
    setTransparent(false);
    gp = (JComponent)(getGlassPane());
    //gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    gp.setVisible(false);
    designModeMouseInputListener = new MouseInputAdapter()
    {
      public void mousePressed(MouseEvent e)
      {
        designModeMousePressed(e);
      }
      public void mouseReleased(MouseEvent e)
      {
        designModeMouseReleased(e);
      }
      public void mouseDragged(MouseEvent e)
      {
        designModeMouseDragged(e);
      }
      public void mouseMoved(MouseEvent e)
      {
        designModeMouseMoved(e);
      }
      public void mouseClicked(MouseEvent e)
      {
        designModeMouseClicked(e);
      }
    };
    setBorder(new OneLineBevelBorder(OneLineBevelBorder.RAISED));
    setDesignMode(false);
    resizeHandles = new Rectangle[8];
    for (int i=0; i<8; i++) {
      resizeHandles[i] = new Rectangle();
    }
    registerKeyboardAction(
      new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
          left();
        }
      },
      KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false),
      JComponent.WHEN_FOCUSED
    );
    registerKeyboardAction(
      new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
          right();
        }
      },
      KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false),
      JComponent.WHEN_FOCUSED
    );
    registerKeyboardAction(
      new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
          up();
        }
      },
      KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false),
      JComponent.WHEN_FOCUSED
    );
    registerKeyboardAction(
      new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
          down();
        }
      },
      KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false),
      JComponent.WHEN_FOCUSED
    );

    addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent e)
      {
        if (clipType == ShapeType.ELLIPSE) {
          // Recalculate cached clip shape and bounds.
          Ellipse2D.Float el =
            new Ellipse2D.Float(0, 0, getWidth(), getHeight());
          clipShape = el;
          shapeBounds = el.getBounds();
          repaint();
          // Fire shape changed listeners
          fireShapeChangedListeners();
        }
      }
    });
   listeners = new ShapeChangedListenerSupport(this);
/*
    registerKeyboardAction(
      new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
          System.out.println("*** Saving component");
          try {
            FileOutputStream fos =
              new FileOutputStream("C:\\Kyrimis\\java\\foo");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            PanelComponent.this.writeExternal(oos);
            oos.close();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      },
      KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false),
      JComponent.WHEN_FOCUSED
    );
    registerKeyboardAction(
      new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
          System.out.println("*** Loading component");
          Component[] compos = getHostedComponents();
          for (int i=0; i<compos.length; i++) {
            cp.remove(compos[i]);
          }
          try {
            FileInputStream fis =
              new FileInputStream("C:\\Kyrimis\\java\\foo");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PanelComponent.this.readExternal(ois);
            ois.close();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      },
      KeyStroke.getKeyStroke(KeyEvent.VK_L, 0, false),
      JComponent.WHEN_FOCUSED
    );
*/
    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }

  /**
   * E-Slate specific initializations.
   */
  private void eSlateInit()
  {
    //handle = ESlate.registerPart(this);

    // Register supported Logo primitive group.
    handle.addPrimitiveGroup(
      "gr.cti.eslate.scripting.logo.PanelPrimitives"
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

    ahg = new ActivationHandleGroup(
      handle,
      new Class[]{Component.class},
      new Class[]{ESlateInternalFrame.class},
      ActivationHandleGroup.DIRECT_CHILDREN
    );
    ahg.addActiveHandleListener(new ActiveHandleListener()
    {
      public void activeHandleChanged(ActiveHandleEvent e)
      {
        ESlateHandle h = e.getNewActiveHandle();
        Component c;
        if (h != null) {
          c = (Component)(h.getComponent());
        }else{
          c = null;
        }
        selectComponent(c, true);
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
   * Run the component as an application--useful for debugging.
   * @param     args    Application's arguments (not used).
   */
  public static void main(String[] args)
  {
    MainFrame f = new MainFrame("", winSize);

    PanelComponent panel = new PanelComponent();

    f.add("Center", panel);
    f.pack();

    f.setTitle(PanelComponent.resources.getString("name"));

    f.setVisible(true);
  }

  /**
   * Free resources. After invoking this method, the component is unusable.
   */
  public void dispose()
  {
    if (handle != null) {
      handle.dispose();
      //handle = null;
    }
    clipType = ShapeType.RECTANGLE;
    clipPoly = null;
    clipShape = null;
    shapeBounds = null;
    ESlateHandle.removeAllRecursively(this);
    cp.dispose();
    cp = null;
    forbiddenCursor = null;
    selectedComponents.clear();
    selectedComponents = null;
    resizeHandles = null;
    resizeCursors = null;
    tmpRect1 = null;
    tmpRect2 = null;
    tmpPt = null;
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.removePerformanceListener(perfListener);
    perfListener = null;
    designModeMouseInputListener = null;
    lastComp = null;
    fileChooser = null;
    currentDir = null;
    fileFilter = null;
  }

  /**
   * Save the component's state.
   * @param     oo      The stream to which the state will be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(saveTimer);

    // The hosted components have a reference to this panel, which is their
    // parent. Writing the field map will cause this panel's writeExternal
    // method to be invoked multiple times, once for each hosted component.
    // Apart from being wasteful, this also causes damage, resulting in null
    // pointer exceptions. By setting the skipWriteExternal flag, the panel
    // knows that it has already saved its state, and simply writes a null to
    // the output stream.
    if (skipWriteExternal) {
      oo.writeObject(null);
      pm.stop(saveTimer);
      pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
      return;
    }

    try {
      skipWriteExternal = true;
      ESlateFieldMap2 map = new ESlateFieldMap2(STORAGE_VERSION, 10);

      Color bg = cp.getBackground();
      map.put(
        BACKGROUND,
        (bg.getRed() << 16) + (bg.getGreen() << 8) + bg.getBlue()
      );
      if (getBackground() == null) {
        // Some borders use the component's background color, so make sure we
        // have one, to avoid getting a null pointer exception.
        setBackground(bg);
      }
      map.put(BORDER, ESlateUtils.getBorderDescriptor(getBorder(), this));
      LayoutManager lm = cp.getLayout();
      String layoutClass;
      if (lm != null) {
        layoutClass = lm.getClass().getName();
      }else{
        layoutClass = null;
      }
      map.put(LAYOUT, layoutClass);
      if (lm != null) {
        int layoutParameters[];
        if (lm instanceof BorderLayout) {
          BorderLayout bl = (BorderLayout)lm;
          layoutParameters = new int[2];
          layoutParameters[0] = bl.getHgap();
          layoutParameters[1] = bl.getVgap();
        }else{
          if (lm instanceof BoxLayout) {
            BoxLayout bl = (BoxLayout)lm;
            layoutParameters = new int[1];
            float xAlignment = bl.getLayoutAlignmentX(cp);
            if (xAlignment == 0.5f) {   // Kludge
              layoutParameters[0] = BoxLayout.X_AXIS;
            }else{
              layoutParameters[0] = BoxLayout.Y_AXIS;
            }
          }else{
            if (lm instanceof CardLayout) {
              CardLayout cl = (CardLayout)lm;
              layoutParameters = new int[2];
              layoutParameters[0] = cl.getHgap();
              layoutParameters[1] = cl.getVgap();
            }else{
              if (lm instanceof FlowLayout) {
                FlowLayout fl = (FlowLayout)lm;
                layoutParameters = new int[3];
                layoutParameters[0] = fl.getAlignment();
                layoutParameters[1] = fl.getHgap();
                layoutParameters[2] = fl.getVgap();
              }else{
                if (lm instanceof GridLayout) {
                  GridLayout gl = (GridLayout)lm;
                  layoutParameters = new int[4];
                  layoutParameters[0] = gl.getRows();
                  layoutParameters[1] = gl.getColumns();
                  layoutParameters[2] = gl.getHgap();
                  layoutParameters[3] = gl.getVgap();
                }else{
                  layoutParameters = new int[0];
                }
              }
            }
          }
        }
        map.put(LAYOUT_PARAMETERS, layoutParameters);
      }

      Component[] compos = getHostedComponents();
      int nComponents = compos.length;
      String[] componentClasses = new String[nComponents];
      String[] componentNames = new String[nComponents];
      int[] componentWidths = new int[nComponents];
      int[] componentHeights = new int[nComponents];
      int[] componentXs = new int[nComponents];
      int[] componentYs = new int[nComponents];
      Object[] children = new Object[nComponents];
      for (int i=0; i<nComponents; i++) {
        componentClasses[i] = compos[i].getClass().getName();
        ESlateHandle h = getESlateHandle(compos[i]);
        if (h != null) {
          children[i] = h;
          componentNames[i] = h.getComponentName();
        }else{
          children[i] = compos[i];
          componentNames[i] = null;
        }
        Component c = compos[i];
        componentWidths[i] = c.getWidth();
        componentHeights[i] = c.getHeight();
        componentXs[i] = c.getX();
        componentYs[i] = c.getY();
      }
      map.put(COMPONENT_CLASSES, componentClasses);
      map.put(COMPONENT_NAMES, componentNames);
      map.put(COMPONENT_WIDTHS, componentWidths);
      map.put(COMPONENT_HEIGHTS, componentHeights);
      map.put(COMPONENT_XS, componentXs);
      map.put(COMPONENT_YS, componentYs);
      map.put(DESIGN_MODE, designMode);
      map.put(TRANSPARENT, isTransparent());
      map.put(BG_STYLE, getBackgroundImageStyle());

      Icon bgImage = getBackgroundImage();
      if (bgImage != null) {
        NewRestorableImageIcon nrii;
        if (bgImage instanceof NewRestorableImageIcon) {
          nrii = (NewRestorableImageIcon)bgImage;
        }else{
          if (bgImage instanceof ImageIcon) {
            nrii = new NewRestorableImageIcon(((ImageIcon)bgImage).getImage());
          }else{
            nrii = null;
          }
        }
        if (nrii != null) {
          map.put(BACKGROUND_IMAGE, nrii);
        }
      }

      int n = cp.getComponentCount();
      int nSelected = selectedComponents.size();
      IntBaseArray iba = new IntBaseArray();
      for (int i=0; i<nSelected; i++) {
        for (int j=0; j<n; j++) {
          if (selectedComponents.get(i).equals(cp.getComponent(j))) {
            iba.add(j);
            break;
          }
        }
      }
      map.put(SELECTED_COMPONENTS, iba);

      map.put(GRID_STEP, getGridStep());
      map.put(GRID_COLOR, getGridColor());
      map.put(GRID_VISIBLE, isGridVisible());
      map.put(ALIGN_TO_GRID, isAlignToGrid());
      map.put(SELECTION_COLOR, getSelectionColor());

      int nConstraints = cp.getComponentCount();
      Object[] constraints = new Object[nConstraints];
      for (int i=0; i<nConstraints; i++) {
        constraints[i] = cp.constraints.get(cp.getComponent(i));
      }
      map.put(CONSTRAINTS, constraints);
      map.put(MINIMUM_SIZE, getMinimumSize());
      map.put(MAXIMUM_SIZE, getMaximumSize());
      map.put(PREFERRED_SIZE, getPreferredSize());
      map.put(CLIP_TYPE, clipType);
      if ((clipType == ShapeType.POLYGON) && (clipPoly != null)) {
        map.put(CLIP_POLY_X, clipPoly.xpoints);
        map.put(CLIP_POLY_Y, clipPoly.ypoints);
      }else{
        if ((clipType == ShapeType.FREEHAND) && (clipShape != null)) {
          map.put(
            CLIP_SHAPE,
            new gr.cti.eslate.shapedComponent.ExternalizableShape(clipShape)
          );
        }
      }
      getESlateHandle().saveChildObjects(map, CHILDREN, children);

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
    //gr.cti.eslate.utils.Timer t = new gr.cti.eslate.utils.Timer();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(loadTimer);

    // Remove all components from the panel.
    Component[] components = getHostedComponents();
    for (int i=0; i<components.length; i++) {
      cp.remove(components[i]);
    }

    StorageStructure map = (StorageStructure)(oi.readObject());

    // Our writeExternal method will simply write a null when invoked from
    // within itself. When reading this null, do nothing.
    if (map == null) {
      isBogus = true;
      pm.stop(loadTimer);
      pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
      return;
    }

    Object[] compos = (Object[])(map.get(COMPONENTS));

    Integer bgColor = (Integer)(map.get(BACKGROUND));
    if (bgColor != null) {
      cp.setBackground(new Color(bgColor.intValue()));
    }
    BorderDescriptor bd = (BorderDescriptor)(map.get(BORDER));
    if (bd != null) {
      setBorder(bd.getBorder());
    }
    String layoutClass = (String)(map.get(LAYOUT));
    int[] lp = (int[])(map.get(LAYOUT_PARAMETERS));
    boolean isBorderLayout = false;
    if (layoutClass != null) {
      if (layoutClass.equals(BorderLayout.class.getName())) {
        cp.setLayout(new BorderLayout(lp[0], lp[1]));
        isBorderLayout = true;
      }else{
        if (layoutClass.equals(BoxLayout.class.getName())) {
          cp.setLayout(new BoxLayout(cp, lp[0]));
        }else{
          if (layoutClass.equals(CardLayout.class.getName())) {
            cp.setLayout(new CardLayout(lp[0], lp[1]));
          }else{
            if (layoutClass.equals(FlowLayout.class.getName())) {
              cp.setLayout(new FlowLayout(lp[0], lp[1], lp[2]));
            }else{
              if (layoutClass.equals(GridLayout.class.getName())) {
                cp.setLayout(new GridLayout(lp[0], lp[1], lp[2], lp[3]));
              }else{
                // Unknown layout; invoke the zero-argument constructor, and
                // hope that this works.
                try {
                  cp.setLayout(
                    (LayoutManager)(Class.forName(layoutClass).newInstance())
                  );
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
          }
        }
      }
    }else{
      cp.setLayout(null);
    }
    String[] componentClasses = (String [])map.get(COMPONENT_CLASSES);
    String[] componentNames = (String [])map.get(COMPONENT_NAMES);
    int[] componentWidths = (int [])map.get(COMPONENT_WIDTHS);
    int[] componentHeights = (int [])map.get(COMPONENT_HEIGHTS);
    int[] componentXs = (int [])map.get(COMPONENT_XS);
    int[] componentYs = (int [])map.get(COMPONENT_YS);
    boolean dMode =
      map.get(RIGHT_RENAMES, false) | map.get(DESIGN_MODE, false);
    NewRestorableImageIcon bgImage =
      (NewRestorableImageIcon)(map.get(BACKGROUND_IMAGE));
    boolean transparent = map.get(TRANSPARENT, false);
    int bgStyle = map.get(BG_STYLE, BG_CENTERED);
    IntBaseArray selComps = (IntBaseArray)(map.get(SELECTED_COMPONENTS));
    int selComp = -1;
    if (selComps == null) {
      selComp = map.get(SELECTED_COMPONENT, -1);
    }
    int gStep = map.get(GRID_STEP, 20);
    Color gColor = map.get(GRID_COLOR, Color.black);
    boolean gVisible = map.get(GRID_VISIBLE, false);
    boolean gAlign = map.get(ALIGN_TO_GRID, false);
    Color sColor = map.get(SELECTION_COLOR, Color.red);
    Object[] constraints = (Object[])(map.get(CONSTRAINTS));
    setMinimumSize(map.get(MINIMUM_SIZE, PanelComponent.minWinSize));
    setMaximumSize(map.get(MAXIMUM_SIZE, PanelComponent.maxWinSize));
    setPreferredSize(map.get(PREFERRED_SIZE, (Dimension)null));
    clipType = map.get(CLIP_TYPE, ShapeType.RECTANGLE);
    if (clipType == ShapeType.POLYGON) {
      int[] xx = (int [])(map.get(CLIP_POLY_X));
      int[] yy = (int [])(map.get(CLIP_POLY_Y));
      if ((xx != null) && (yy != null)) {
        clipPoly = new Polygon(xx, yy, xx.length);
      }else{
        clipPoly = new Polygon();
      }
      shapeBounds = clipPoly.getBounds();
    }else{
      if (clipType == ShapeType.FREEHAND) {
        gr.cti.eslate.shapedComponent.ExternalizableShape s =
          (gr.cti.eslate.shapedComponent.ExternalizableShape)(map.get(CLIP_SHAPE));
        if (s != null) {
          clipShape = s.getShape();
          shapeBounds = clipShape.getBounds();
        }else{
          clipShape = null;
          shapeBounds = null;
        }
      }
      // Fire shape changed listeners
      fireShapeChangedListeners();
    }

    // To ensure that the mode change is actually made.
    setDesignMode(!dMode);
    setDesignMode(dMode);

    int nComponents = componentClasses.length;
    Object[] children = null;
    if (compos == null) {
      children = getESlateHandle().restoreChildObjects(map, CHILDREN);
    }
    for (int i=0; i<nComponents; i++) {
      Component compo;
      if (compos != null) {
        // If children were stored directly in the field map, place each child
        // in the panel's microworld and assign it its correct name.
        compo = (Component)(compos[i]);
        if (componentNames[i] != null) {
          ESlateHandle h = getESlateHandle(compo);
          if (h != null) {
            //setESlateMicroworld(h, getESlateHandle().getESlateMicroworld());
            try {
              h.setComponentName(componentNames[i]);
            } catch (Exception e) {
            }
          }
        }
      }else{
        // If children were saved using saveChildObjects(), then
        // restoreChildObjects() will have restored child E-Slate components
        // correctly in the component hierarchy, so we merely need to
        // retrieve each child from the array returned by
        // restoreChildObjects().
        Object child = children[i];
        if (child instanceof ESlateHandle) {
          compo = (Component)(((ESlateHandle)child).getComponent());
        }else{
          compo = (Component)child;
        }
      }
      if ((constraints != null) && isBorderLayout && (constraints[i] != null)) {
        cp.add(compo, constraints[i]);
      }else{
        cp.add(compo);
        if (constraints != null) {
          cp.constraints.put(compo, constraints[i]);
        }
      }
      compo.setBounds(
        componentXs[i], componentYs[i],
        componentWidths[i], componentHeights[i]
      );
    }

    setTransparent(transparent);
    setBackgroundImageStyle(bgStyle);
    if (bgImage != null) {
      setBackgroundImage(bgImage);
    }
    selectedComponents.clear();
    if (selComps != null) {
      int n = selComps.size();
      for (int i=0; i<n; i++) {
        selectedComponents.add(cp.getComponent(selComps.get(i)));
      }
      if (n > 0) {
        activateComponent(cp.getComponent(selComps.get(n-1)));
      }
    }else{
      if (selComp >= 0) {
        Component c = cp.getComponent(selComp);
        selectedComponents.add(c);
        activateComponent(c);
      }
    }
    setGridStep(gStep);
    setGridColor(gColor);
    setGridVisible(gVisible);
    setAlignToGrid(gAlign);
    setSelectionColor(sColor);

    repaint();

    pm.stop(loadTimer);
    pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
    //long t0 = t.lapse();
    //System.out.println("*** " + getESlateHandle() + " : " + t0);
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
   * Handle the event received when a component was added to the panel.
   * @param     e The event received.
   */
  public void componentAdded(ContainerEvent e)
  {
    if (!cp.ignoreContainerEvents) {
      Component compo = e.getChild();
      ESlateHandle h = getESlateHandle(compo);
      if (h != null) {
        //setESlateMicroworld(h, handle.getESlateMicroworld());
        handle.add(h);
        if (h.isMenuPanelCreated()) {
          ESlateUtils.removeMenuPanel(compo);
        }
        h.addESlateListener(this);
      }
      compo.addComponentListener(this);
      if (cp.getLayout() == null) {
        compo.setSize(compo.getPreferredSize());
      }
      revalildateRootJComponent();
      if (compo instanceof JComponent) {
        ((JComponent)compo).revalidate();
      }
    }
  }

  /**
   * Handle the event received when a component was removed from the panel.
   * @param     e The event received.
   */
  public void componentRemoved(ContainerEvent e)
  {
    if (!cp.ignoreContainerEvents) {
      Component compo = e.getChild();
      int n = selectedComponents.size();
      for (int i=0; i<n; i++) {
        if (compo.equals(selectedComponents.get(i))) {
          selectedComponents.remove(i);
          break;
        }
      }
      n = selectedComponents.size();
      if (n > 0) {
        activateComponent(selectedComponents.get(n-1));
      }else{
        activateComponent(null);
      }
      ESlateHandle h = getESlateHandle(compo);
      if (h != null) {
        h.removeESlateListener(this);
        handle.remove(h);
        h.dispose();
      }
      compo.removeComponentListener(this);
      revalildateRootJComponent();
    }
  }

  /**
   * Revalidates the topmost JComponent containing the panel component.
   */
  private void revalildateRootJComponent()
  {
    Container root = this;
    Container parent = null;
    Container lastJComponent = null;
    while ((parent=root.getParent()) != null) {
      root = parent;
      if (parent instanceof JComponent) {
        lastJComponent = parent;
      }
    }
    if (lastJComponent != null) {
      ((JComponent)lastJComponent).revalidate();
    }
  }


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
   * <LI>If all else fails, the component will be registered with E-Slate, and
   * the resulting handle will be returned.</LI>
   * </UL>
   * @param     compo   The component.
   * @return    The requested handle. If the component is not an E-Slate
   *            component, this method return null.
   */
  private ESlateHandle getESlateHandle(Object compo)
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

/*
    // Then check the default microworld, to see if the component is
    // registered there.
    ESlateMicroworld defaultMw = ESlateMicroworld.getDefaultMicroworld();
    ESlateHandle h = defaultMw.getComponentHandle(compo);
    if (h != null) {
      return h;
    }
*/

    // Then check the current microworld, to see if the component is
    // registered there.
    if (handle != null) {
      ESlateMicroworld mw = handle.getESlateMicroworld();
      // Return whatever we got: either the handle, or null.
      if (mw != null) {
        ESlateHandle h = mw.getComponentHandle(compo);
        if (h != null) {
          return h;
        }
      }
    }

    return ESlate.registerPart(compo);
  }

//  private static Class[] setMWArgTypes = new Class[]{ESlateMicroworld.class};
//  private static Object[] setMWArgs = new Object[1];
//  /**
//   * Sets the E-Slate microworld of an E-Slate handle.
//   */
//  private static void setESlateMicroworld(ESlateHandle h, ESlateMicroworld mw)
//  {
//    long t0 = System.currentTimeMillis();
//    // Invoke h.setESlateMicroworld(ESlateMicroworld) using introspection
//    // rather than directly, so that the component may work with newer
//    // versions of E-Slate, where the setESlateMicroworld method is no longer
//    // available.
//    try {
//      Method m = h.getClass().getMethod("setESlateMicroworld", setMWArgTypes);
//      setMWArgs[0] = mw;
//      m.invoke(h, setMWArgs);
//    }catch (Exception e) {
//      // If the handle's setESlateMicroworld method does not exist, then we
//      // don't need to do anything.
//    }
//    setMWArgs[0] = null;
//  }

  /**
   * Returns the AWT components hosted by the panel.
   * @return    An array containing references to the hosted components.
   */
  public Component[] getHostedComponents()
  {
    return cp.getComponents();
  }

  /**
   * Returns the number of AWT components hosted by the panel.
   * @return    The requested number.
   */
  public int getHostedComponentCount()
  {
    return cp.getComponentCount();
  }

  /**
   * Specifies whether mouse clicks are processed by the panel component
   * or if they will be forwarded to the hosted components themselves.
   * @param     flag    If true, mouse clicks are processed by the panel
   *                    component.
   */
  public void setDesignMode(boolean flag)
  {
    if (designMode != flag) {
      designMode = flag;
      if (designMode) {
        gp.setVisible(true);
        gp.addMouseListener(designModeMouseInputListener);
        gp.addMouseMotionListener(designModeMouseInputListener);
      }else{
        gp.setVisible(false);
        gp.removeMouseListener(designModeMouseInputListener);
        gp.removeMouseMotionListener(designModeMouseInputListener);
      }
    }
    repaint();
  }

  /**
   * Checks whether mouse clicks are processed by the panel component
   * or if they are being forwarded to the hosted components themselves.
   * @return    True, if mouse clicks are processed by the panel component,
   *            false otherwise.
   */
  public boolean isDesignMode()
  {
    return designMode;
  }

  /**
   * Returns the component at a given point in the content pane.
   * @param     pt      The point in the content pane.
   * @return    The component at <code>pt<code>. If there is no component at
   *            that point, <code>null</code> is returned.
   */
  private Component compoAt(Point pt)
  {
    Component c = cp.getComponentAt(pt);
    if (cp.equals(c)) {
      c = null;
    }
    return c;
  }

  /**
   * Creates the popup menu.
   * @param     popup   The current popup menu.
   * @return    If <code>popup</code> is not null, then it is returned as is.
   *            Otherwise, a new popup menu is created and returned.
   */
  private ESlateJPopupMenu createPopupMenu(ESlateJPopupMenu popup)
  {
    if (popup == null) {
      popup = new ESlateJPopupMenu();
      popup.setLightWeightPopupEnabled(false);
    }
    return popup;
  }

  /**
   * Pops up the popup menu in the glass pane.
   * @param     e       The mouse pressed/released event received at the glass
   * pane.
   */
  private void popMenu(MouseEvent e)
  {
    Point gPoint = e.getPoint();
    Point cPoint = SwingUtilities.convertPoint(gp, gPoint, cp);
    Component c = compoAt(cPoint);
    int id = e.getID();
    switch (id) {
      case MouseEvent.MOUSE_PRESSED:
        ESlateJPopupMenu popup = null;
        if ((c != null) &&
            (handle != null) && (handle.getESlateMicroworld() != null) &&
            handle.getESlateMicroworld().isRenamingAllowed()) {
          ESlateHandle h = getESlateHandle(c);
          if (h != null) {
            popup = createPopupMenu(popup);
            JMenuItem item = new PanelMenuItem(
              resources.getString("rename") + " \"" +
                h.getComponentName() + "\"",
              h
            );
            item.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent e)
              {
                PanelMenuItem item = (PanelMenuItem)(e.getSource());
                rename(item.getHandle());
              }
            });
            popup.add(item);
          }
        }
        JMenuItem item;
        if (c != null) {
          popup = createPopupMenu(popup);
          String text = resources.getString("clone");
          ESlateHandle h = getESlateHandle(c);
          if (h != null) {
            text = text + " \"" + h.getComponentName() + "\"";
          }
          item = new PanelMenuItem(text, c);
          item.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              PanelMenuItem item = (PanelMenuItem)(e.getSource());
              try {
                Component c = item.getCompo();
                Component nc = cloneComponent(c);
                selectComponent(nc, true);
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
          });
          popup.add(item);

          text = resources.getString("remove");
          if (h != null) {
            text = text + " \"" + h.getComponentName() + "\"";
          }
          item = new PanelMenuItem(text, c);
          item.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              PanelMenuItem item = (PanelMenuItem)(e.getSource());
              cp.remove(item.getCompo());
              repaint();
            }
          });
          popup.add(item);

          if (cp.getLayout() == null) {
            if (h != null) {
              text = resources.getString("sendBack1") +
                " \"" + h.getComponentName() + "\"" +
                resources.getString("sendBack2");
            }else{
              text = resources.getString("sendBack3");
            }
            item = new PanelMenuItem(text, c);
            item.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent e)
              {
                PanelMenuItem item = (PanelMenuItem)(e.getSource());
                moveToBottom(item.getCompo());
                repaint();
              }
            });
            popup.add(item);
          }
        }

        if (selectedComponents.size() > 0) {
          if (popup != null) {
            popup.addSeparator();
          }else{
            popup = createPopupMenu(popup);
          }
          item = new JMenuItem(resources.getString("cloneSelected"));
          item.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              ComponentBaseArray comps =
                (ComponentBaseArray)(selectedComponents.clone());
              int n = comps.size();
              boolean clear = true;
              for (int i=0; i<n; i++) {
                try {
                  Component c = comps.get(i);
                  Component nc = cloneComponent(c);
                  selectComponent(nc, clear);
                  clear = false;
                } catch (Exception ex) {
                  ex.printStackTrace();
                }
              }
            }
          });
          popup.add(item);
          item = new JMenuItem(resources.getString("removeSelected"));
          item.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              ComponentBaseArray comps =
                (ComponentBaseArray)(selectedComponents.clone());
              int n = comps.size();
              for (int i=0; i<n; i++) {
                Component c = comps.get(i);
                cp.remove(c);
              }
              repaint();
            }
          });
          popup.add(item);
        }

        if (popup == null) {
          popup = createPopupMenu(popup);
        }
        item = new JMenuItem(resources.getString("readPanel..."));
        item.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            readPanel();
          }
        });
        popup.add(item);
        item = new JMenuItem(resources.getString("savePanel..."));
        item.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            savePanel();
          }
        });
        popup.add(item);
        if (getHostedComponentCount() > 0) {
          item = new JMenuItem(resources.getString("saveContents..."));
          item.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              saveContents();
            }
          });
          popup.add(item);
        }
        item = new JMenuItem(resources.getString("readContents..."));
        item.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            readContents();
          }
        });
        popup.add(item);

        if (popup != null) {
          popup.show(gp, gPoint.x, gPoint.y);
        }else{
          if (forbiddenCursor == null) {
            Toolkit tk = getToolkit();
            Image im = tk.createImage(
              PanelComponent.class.getResource("images/forbidden.gif")
            );
            forbiddenCursor =  tk.createCustomCursor(
              im, new Point(16, 16), "forbidden"
            );
          }
          gp.setCursor(forbiddenCursor);
        }
        break;
      case MouseEvent.MOUSE_RELEASED:
        if (c != null) {
          gp.setCursor(c.getCursor());
        }else{
          gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        break;
    }
  }

  /**
   * Pops up a dialog box asking the user to rename a hosted E-Slate
   * component.
   * @param     h       The E-Slate handle of the component to rename.
   */
  private void rename(ESlateHandle h)
  {
    String name = (String)ESlateOptionPane.showInputDialog(
      gp,
      resources.getString("enterName") + " \"" + h.getComponentName() + "\"",
      resources.getString("renamingComponent"),
      JOptionPane.QUESTION_MESSAGE,
      null,
      null,
      h.getComponentName()
    );
    if ((name != null) && !name.equals("")) {
      String message;
      try {
        h.setComponentName(name);
        message = null;
      } catch (NameUsedException e) {
        message = resources.getString("nameUsed");
      } catch (RenamingForbiddenException e) {
        message = resources.getString("renamingForbidden");
      }
      if (message != null) {
        ESlateOptionPane.showMessageDialog(
          gp, message, resources.getString("error"), JOptionPane.ERROR_MESSAGE
        );
      }
    }
  }

  /**
   * Inserts a cloned copy of a given component to the panel.
   * @param     component       The component to clone.
   * @return    The inserted copy.
   */
  private Component cloneComponent(Component component) throws Exception
  {
    // Copy component
    Class<?> c = component.getClass();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    boolean oldSkipWriteExternal = skipWriteExternal;
    skipWriteExternal = true;
    try {
      if (component instanceof Externalizable) {
        ((Externalizable)component).writeExternal(oos);
      }else{
        if (component instanceof Serializable) {
          oos.writeObject(component);
        }
      }
    } finally {
      skipWriteExternal = oldSkipWriteExternal;
    }
    oos.close();
    StorageStructure info = null;
    ESlateHandle h = null;
    if (handle != null) {
      h = getESlateHandle(component);
      if (h != null) {
        info = h.saveSubTreeInfo();
      }
    }
    Rectangle bounds = component.getBounds();

    // Paste component
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bis);
    Component newComponent;
    if (component instanceof Externalizable) {
      newComponent = (Component)(c.newInstance());
    }else{
      newComponent = (Component)(ois.readObject());
    }
    addComponent(newComponent);
    if (component instanceof Externalizable) {
      ((Externalizable)newComponent).readExternal(ois);
    }
    ois.close();
    ESlateHandle newH = getESlateHandle(newComponent);
    if (newH != null) {
      newH.restoreSubTreeInfo(info);
    }
    int gStep = getGridStep();
    bounds.x += gStep;
    bounds.y += gStep;
    newComponent.setBounds(bounds);

    return newComponent;
  }

  /**
   * Saves the contents of the panel into a file.
   */
  private void saveContents()
  {
    try {
      fileChooser = getFileChooser();
      fileFilter.setType(PanelFileFilter.PANEL_CONTENTS);
      if (currentDir != null) {
        fileChooser.setCurrentDirectory(currentDir);
      }
      fileChooser.setDialogTitle(resources.getString("saveContents"));
      if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File f = fileChooser.getSelectedFile();
        currentDir = f.getParentFile();
        String name = f.getName();
        if (!name.endsWith(PCONT_EXT) && (name.indexOf('.') < 0)) {
          f = new File(currentDir, name + PCONT_EXT);
        }
        if (f.exists()) {
          String message =
            resources.getString("fileExists1") +
            f.getName() +
            resources.getString("fileExists2");
          int ok = ESlateOptionPane.showConfirmDialog(
            this, message, resources.getString("confirm"),
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
          );
          if (ok != JOptionPane.YES_OPTION) {
            return;
          }
        }
        FileOutputStream fos = new FileOutputStream(f);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        saveComponents(oos);
        oos.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      ESlateOptionPane.showMessageDialog(
        this, e.getMessage(), resources.getString("error"),
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  /**
   * Reads the saved contents of the panel into the panel from a file.
   */
  private void readContents()
  {
    try {
      fileChooser = getFileChooser();
      fileFilter.setType(PanelFileFilter.PANEL_CONTENTS);
      if (currentDir != null) {
        fileChooser.setCurrentDirectory(currentDir);
      }
      fileChooser.setDialogTitle(resources.getString("readContents"));
      if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        File f = fileChooser.getSelectedFile();
        //currentDir = f.getParentFile();
        FileInputStream fis = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        readComponents(ois);
        ois.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      ESlateOptionPane.showMessageDialog(
        this, e.getMessage(), resources.getString("error"),
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  /**
   * Reads the state of the panel from a file.
   */
  private void readPanel()
  {
    try {
      fileChooser = getFileChooser();
      fileFilter.setType(PanelFileFilter.PANELS);
      if (currentDir != null) {
        fileChooser.setCurrentDirectory(currentDir);
      }
      fileChooser.setDialogTitle(resources.getString("readPanel"));
      if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        File f = fileChooser.getSelectedFile();
        //currentDir = f.getParentFile();
        FileInputStream fis = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        readExternal(ois);
        ois.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      ESlateOptionPane.showMessageDialog(
        this, e.getMessage(), resources.getString("error"),
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  /**
   * Saves the state of the panel into a file.
   */
  private void savePanel()
  {
    try {
      fileChooser = getFileChooser();
      fileFilter.setType(PanelFileFilter.PANELS);
      if (currentDir != null) {
        fileChooser.setCurrentDirectory(currentDir);
      }
      fileChooser.setDialogTitle(resources.getString("savePanel"));
      if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File f = fileChooser.getSelectedFile();
        currentDir = f.getParentFile();
        String name = f.getName();
        if (!name.endsWith(PANEL_EXT) && (name.indexOf('.') < 0)) {
          f = new File(currentDir, name + PANEL_EXT);
        }
        if (f.exists()) {
          String message =
            resources.getString("fileExists1") +
            f.getName() +
            resources.getString("fileExists2");
          int ok = ESlateOptionPane.showConfirmDialog(
            this, message, resources.getString("confirm"),
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
          );
          if (ok != JOptionPane.YES_OPTION) {
            return;
          }
        }
        FileOutputStream fos = new FileOutputStream(f);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        writeExternal(oos);
        oos.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      ESlateOptionPane.showMessageDialog(
        this, e.getMessage(), resources.getString("error"),
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  /**
   * Returns the file chooser, creating it if necessary.
   * @return    The file chooser.
   */
  private JFileChooser getFileChooser()
  {
    if (fileChooser == null) {
      fileChooser = new JFileChooser();
      fileChooser.setMultiSelectionEnabled(false);
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileFilter = new PanelFileFilter();
      fileChooser.addChoosableFileFilter(fileFilter);
      fileChooser.setFileFilter(fileFilter);
    }
    fileChooser.setSelectedFile(null);
    return fileChooser;
  }

  /**
   * Saves the contents of the panel.
   * @param     os      The stream where the contents will be saved.
   * @exception Exception       Thrown when saving fails.
   */
  private void saveComponents(ObjectOutputStream os) throws Exception
  {
    Component[] comps = getHostedComponents();
    int nComps = comps.length;

    os.writeInt(SAVE_COMPONENTS_VERSION);
    os.writeInt(nComps);

    for (int i=0; i<nComps; i++) {
      Component component = comps[i];

      // Save component class;
      os.writeUTF(comps[i].getClass().getName());

      // Save component
      boolean oldSkipWriteExternal = skipWriteExternal;
      skipWriteExternal = true;
      try {
        if (component instanceof Externalizable) {
          ((Externalizable)component).writeExternal(os);
        }else{
          if (component instanceof Serializable) {
            os.writeObject(component);
          }
        }
      } finally {
        skipWriteExternal = oldSkipWriteExternal;
      }

      // Save connections etc.
      StorageStructure info = null;
      ESlateHandle h = null;
      if (handle != null) {
        h = getESlateHandle(component);
        if (h != null) {
          info = h.saveSubTreeInfo();
        }
      }
      os.writeObject(info);

      // Save bounds.
      Rectangle bounds = component.getBounds();
      os.writeObject(bounds);
    }
  }

  /**
   * Reads the saved contents of the panel into the panel.
   * @param     is      The stream from where the contents will read.
   */
  private void readComponents(ObjectInputStream is)
  {
    try {
      /*int version = */is.readInt();
      int nComps = is.readInt();

      for (int i=0; i<nComps; i++) {
        // Read component class.
        String className = is.readUTF();
        Class<?> cl = Class.forName(className);

        // Restore component.
        Component newComponent;
        boolean externalizable;
        if (Externalizable.class.isAssignableFrom(cl)) {
          externalizable = true;
        }else{
          externalizable = false;
        }
        if (externalizable) {
          newComponent = (Component)(cl.newInstance());
        }else{
          newComponent = (Component)(is.readObject());
        }
        addComponent(newComponent);
        if (externalizable) {
          ((Externalizable)newComponent).readExternal(is);
        }

        // Restore connections, etc.
        StorageStructure info = (StorageStructure)(is.readObject());
        ESlateHandle newH = getESlateHandle(newComponent);
        if (newH != null) {
          newH.restoreSubTreeInfo(info);
        }

        // Restore bounds.
        Rectangle bounds = (Rectangle)(is.readObject());
        newComponent.setBounds(bounds);
      }
    } catch (Exception e) {
      e.printStackTrace();
      ESlateOptionPane.showMessageDialog(
        gp, e.getMessage(), resources.getString("error"),
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  /**
   * Adds a component to the panel's content pane, taking into account the
   * current layout manager.
   * @param     c       The component to add.
   */
  private void addComponent(Component c)
  {
    LayoutManager lm = cp.getLayout();
    if (lm == null) {
      cp.add(c, 0);
    }else{
      cp.add(c);
    }
  }

  /**
   * Returns a list of all hosted components.
   * @return    An array containing references to all hosted components.
   */
  public Object[] listHostedComponents()
  {
    int n = cp.getComponentCount();
    Object[] compos = new Component[n];
    for (int i=0; i<n; i++) {
      compos[i] = cp.getComponent(i);
    }
    return compos;
  }

  /**
   * Returns a list of all hosted E-Slate components.
   * @return    An array containing references to all hosted components
   *            that have an E-Slate handle associated with them.
   *            Other components are omitted from the list.
   */
  public Object[] listHostedESlateComponents()
  {
    int n = cp.getComponentCount();
    ArrayList<Object> compos = new ArrayList<Object>(n);
    for (int i=0; i<n; i++) {
      Object comp = cp.getComponent(i);
      ESlateHandle h = getESlateHandle(comp);
      if (h != null) {
        compos.add(comp);
      }
    }
    return compos.toArray();
  }

  /**
   * Returns a list of the E-Slate handles of all hosted E-Slate components.
   * @return    An array containing references to the E-Slate handles of all
   *            hosted components that have an E-Slate handle associated with
   *            them. Other components are omitted from the list.
   */
  public ESlateHandle[] listHostedESlateHandles()
  {
    int n = cp.getComponentCount();
    ESlateHandleBaseArray handles = new ESlateHandleBaseArray(n);
    for (int i=0; i<n; i++) {
      Object comp = cp.getComponent(i);
      ESlateHandle h = getESlateHandle(comp);
      if (h != null) {
        handles.add(h);
      }
    }
    n = handles.size();
    ESlateHandle[] h = new ESlateHandle[n];
    for (int i=0; i<n; i++) {
      h[i] = handles.get(i);
    }
    return h;
  }

  /**
   * Sets the layout manager of the panel's content pane.
   * @param     m       The layout manager to set.
   */
  public void setContentPaneLayout(LayoutManager m)
  {
    cp.setLayout(m);
    if (m != null) {
      gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    selectedComponents.clear();
    activateComponent(null);
  }

  /**
   * Returns the layout manager of the panel's content pane.
   * @return    The requested layout manager. If the component has been
   *            disposed, this method returns <code>null</code>.
   */
  public LayoutManager getContentPaneLayout()
  {
    if (cp != null) {
      return cp.getLayout();
    }else{
      return null;
    }
  }

  /**
   * Sets the background of the panel's content pane.
   * @param     color   The new background color.
   */
  public void setContentPaneBackground(Color color)
  {
    cp.setBackground(color);
    // Just in case...
    setBackground(color);
  }

  /**
   * Returns the background of the panel's content pane.
   * @return    The requested color.
   */
  public Color getContentPaneBackground()
  {
    return cp.getBackground();
  }

  /**
   * Sets the background image.
   * @param     image   The image to set. If image is null, the panel will
   *                    have no background image.
   */
  public void setBackgroundImage(Icon image)
  {
    cp.setBackgroundImage(image);
    repaint();
  }

  /**
   * Returns the background image.
   * @return    The requested image or null if the panel has no background
   * image.
   */
  public Icon getBackgroundImage()
  {
    return cp.getBackgroundImage();
  }

  /**
   * Specifies whether the component is transparent.
   * @param     status  True if yes, false if no.
   */
  public void setTransparent(boolean status)
  {
    //super.setOpaque(!status);
    cp.setTransparent(status);
    repaint();
  }

  /**
   * Checks whether the component is transparent.
   * @return    True if yes, false if no.
   */
  public boolean isTransparent()
  {
    return cp.isTransparent();
  }

  /**
   * Specifies whether the alignment grid is visible.
   * @param     status  True if yes, false if no.
   */
  public void setGridVisible(boolean status)
  {
    cp.setGridVisible(status);
    repaint();
  }

  /**
   * Checks whether the alignment grid is visible.
   * @return    True if yes, false if no.
   */
  public boolean isGridVisible()
  {
    return cp.isGridVisible();
  }

  /**
   * Sets the step of the alignment grid.
   * @param     step    The step of the alignment grid. If the step is less
   *                    than or equal to 0, the step is set to 1.
   */
  public void setGridStep(int step)
  {
    cp.setGridStep(step);
    repaint();
  }

  /**
   * Returns the step of the alignment grid.
   * @return    The requested value.
   */
  public int getGridStep()
  {
    return cp.getGridStep();
  }

  /**
   * Sets the color of the alignment grid.
   * @param     color   The color of the alignment grid.
   */
  public void setGridColor(Color color)
  {
    cp.setGridColor(color);
    repaint();
  }

  /**
   * Returns the color of the alignment grid.
   * @return    The requested color.
   */
  public Color getGridColor()
  {
    return cp.getGridColor();
  }

  /**
   * Specifies whether components should be aligned to a grid.
   * @param     status  True if yes, false if no.
   */
  public void setAlignToGrid(boolean status)
  {
    alignToGrid = status;
    if (alignToGrid) {
      int n = cp.getComponentCount();
      for (int i=0; i<n; i++) {
        Component c = cp.getComponent(i);
        Point loc = c.getLocation();
        c.setLocation(snapToGrid(loc));
      }
      repaint();
    }
  }

  /**
   * Checks whether components are being aligned to a grid.
   */
  public boolean isAlignToGrid()
  {
    return alignToGrid;
  }

  /**
   * Sets the color of the frame that handles the selected component.
   * @param     color   The color of the frame.
   */
  public void setSelectionColor(Color color)
  {
    selectionColor = color;
    repaint();
  }

  /**
   * Returns the color of the frame that handles the selected component.
   * @return    The requested color.
   */
  public Color getSelectionColor()
  {
    return selectionColor;
  }

  /**
   * Returns the color used to draw the shape outline in the polygon editor.
   * @return    The color returned by <code>getSelectionColor()</code>.
   */
  public Color getOutlineColor()
  {
    return selectionColor;
  }

  /**
   * Sets the style of the background image.
   * @param     style   One of BG_NONE, BG_CENTERED, BG_STRETCHED, or BG_TILED.
   *                    Other values are treated as equivalent to BG_NONE.
   */
  public void setBackgroundImageStyle(int style)
  {
    int bgStyle;
    switch (style) {
      case BG_NONE:
      case BG_CENTERED:
      case BG_STRETCHED:
      case BG_TILED:
        bgStyle = style;
        break;
      default:
        bgStyle = BG_NONE;
    }
    cp.setBackgroundImageStyle(bgStyle);
    repaint();
  }

  /**
   * Returns the style of the background image.
   * @return    One of BG_NONE, BG_CENTERED, BG_STRETCHED, or BG_TILED.
   */
  public int getBackgroundImageStyle()
  {
    return cp.getBackgroundImageStyle();
  }

  /**
   * Adjust the cursor depending on its position.
   * @param     pt  The position of the cursor.
   */
  private void adjustCursor(Point pt)
  {
    for (int i=0; i<8; i++) {
      if (resizeHandles[i].contains(pt)) {
        gp.setCursor(Cursor.getPredefinedCursor(resizeCursors[i]));
        return;
      }
    }
    boolean inSelected = false;
    int n = selectedComponents.size();
    for (int i=0; i<n; i++) {
      Component c = selectedComponents.get(i);
      tmpRect1.x = c.getX();
      tmpRect1.y = c.getY();
      tmpRect1.width = c.getWidth();
      tmpRect1.height = c.getHeight();
      Rectangle r = SwingUtilities.convertRectangle(cp, tmpRect1, gp);
      if (r.contains(pt)) {
        inSelected = true;
        break;
      }
    }
    if (inSelected) {
      gp.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }else{
      gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  /**
   * Selects a component in the panel.
   * @param     c       The component to select. If <code>c</code> is null,
   *                    then all components become unselected.
   * @param     clear   If true, all previously selected components will be
   *                    deselected.
   */
  private void selectComponent(Component c, boolean clear)
  {
    if (!inActivateComponent) {
      if (clear) {
        selectedComponents.clear();
      }
      if (c != null) {
        if (!selectedComponents.contains(c)) {
          selectedComponents.add(c);
        }
        moveToTop(c);
      }
      activateComponent(c);
      repaint();
    }
  }

  /**
   * Toggles the selection of a component in the panel.
   * @param     c       The component to toggle its selection.
   */
  private void toggleSelectComponent(Component c)
  {
    if (c != null) {
      int ind = selectedComponents.indexOf(c);
      if (ind < 0) {
        selectedComponents.add(c);
        activateComponent(c);
      }else{
        selectedComponents.remove(ind);
        int n = selectedComponents.size();
        if (n > 0) {
          activateComponent(selectedComponents.get(n-1));
        }else{
          activateComponent(null);
        }
      }
      moveToTop(c);
    }
    repaint();
  }

  /**
   * Makes a component in the panel appear above all other components in the
   * panel. This method does nothing if the layout of the panel's content pane
   * is not <code>null</code>.
   * @param     c       The component to move to the top.
   */
  private void moveToTop(Component c)
  {
    if (cp.getLayout() == null) {
      // Remove/add the selected component to make it appear on top.
      boolean tmpIgnore = cp.ignoreContainerEvents;
      cp.ignoreContainerEvents = true;
      cp.remove(c);
      cp.add(c, 0);
      cp.ignoreContainerEvents = tmpIgnore;
    }
  }

  /**
   * Makes a component in the panel appear below all other components in the
   * panel. This method does nothing if the layout of the panel's content pane
   * is not <code>null</code>.
   * @param     c       The component to move to the bottom.
   */
  private void moveToBottom(Component c)
  {
    if (cp.getLayout() == null) {
      // Remove/add the selected component to make it appear at the bottom.
      boolean tmpIgnore = cp.ignoreContainerEvents;
      cp.ignoreContainerEvents = true;
      cp.remove(c);
      cp.add(c);
      cp.ignoreContainerEvents = tmpIgnore;
    }
  }

  /**
   * Makes a component the active component in the panel.
   * @param     c       The component to activate.
   */
  private void activateComponent(Component c)
  {
    if (ahg != null) {
      inActivateComponent = true;
      try {
        ESlateHandle h = getESlateHandle(c);
        ahg.setActiveHandle(h);
      } finally {
        inActivateComponent = false;
      }
    }
  }

  /**
   * Process mouse presses on the glass pane in design mode.
   * @param     e       The event generated by a mouse press.
   */
  private void designModeMousePressed(MouseEvent e)
  {
    if (forwardEvent(e)) {
      return;
    }
    if (isMenuButton(e)) {
      popMenu(e);
    }else{
      if (cp.getLayout() == null) {
        Point gPoint = e.getPoint();
        Point cPoint = SwingUtilities.convertPoint(gp, gPoint, cp);
        resizeDir = -1;
        if (selectedComponents.size() == 1) {
          for (int i=0; i<8; i++) {
            if (resizeHandles[i].contains(cPoint)) {
              resizeDir = i;
              break;
            }
          }
        }
        if (resizeDir < 0) {
          // Request the focus, so that the arrow keys move the selected
          // components in this frame.
          requestFocus();
          clickPoint = cPoint;
          currentPoint = clickPoint;
          Component c = compoAt(cPoint);
          if (c == null) {
            selecting = true;
          }else{
            if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
              toggleSelectComponent(c);
            }else{
              selectComponent(c, true);
            }
            adjustCursor(gPoint);
            if (selectedComponents.size() > 0) {
              Rectangle selBounds = getSelBounds();
              offX = selBounds.x - clickPoint.x;
              offY = selBounds.y - clickPoint.y;
            }else{
              offX = 0;
              offY = 0;
            }
          }
        }
      }
    }
  }

  /**
   * Process mouse releases on the glass pane in design mode.
   * @param     e       The event generated by a mouse release.
   */
  private void designModeMouseReleased(MouseEvent e)
  {
    lastComp = null;
    if (forwardEvent(e)) {
      return;
    }
    if (isMenuButton(e)) {
      popMenu(e);
    }else{
      if (selecting) {
        try {
          Point gPoint = e.getPoint();
          currentPoint = SwingUtilities.convertPoint(gp, gPoint, cp);
          int x = Math.min(clickPoint.x, currentPoint.x);
          int y = Math.min(clickPoint.y, currentPoint.y);
          int w = Math.abs(clickPoint.x - currentPoint.x) + 1;
          int h = Math.abs(clickPoint.y - currentPoint.y) + 1;
          Rectangle r = new Rectangle(x, y, w, h);
          Component[] comp;
          if ((w == 1) && (h == 1)) {
            Component c = compoAt(currentPoint);
            if (c != null) {
              comp = new Component[]{c};
            }else{
              comp = new Component[0];
            }
          }else{
            comp = getHostedComponents();
          }
          int n = comp.length;
          boolean toggle;
          if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
            toggle = true;
          }else{
            toggle = false;
          }
          if (n == 0) {
            if (!toggle) {
              selectComponent(null, true);
            }
          }else{
            boolean selectingFirst = true;
            for (int i=0; i<n; i++) {
              if (r.intersects(comp[i].getBounds())) {
                if (toggle) {
                  toggleSelectComponent(comp[i]);
                }else{
                  selectComponent(comp[i], selectingFirst);
                  selectingFirst = false;
                }
              }
            }
            if (selectingFirst && !toggle) {
              selectComponent(null, true);
            }
          }
          repaint();
        } finally {
          clickPoint = null;
          currentPoint = null;
          selecting = false;
        }
      }
    }
  }

  /**
   * Process mouse drags on the glass pane in design mode.
   * @param     e       The event generated by a mouse release.
   */
  private void designModeMouseDragged(MouseEvent e)
  {
    if (lastComp != null) {
      MouseEvent e2 = SwingUtilities.convertMouseEvent(gp, e, lastComp);
      sendEvent(lastComp, e2);
      return;
    }
    if (forwardEvent(e)) {
      return;
    }
    if (!selecting && (cp.getLayout() == null) && !isMenuButton(e) &&
        (selectedComponents.size() > 0)) {
      Point gPoint = e.getPoint();
      Point cPoint = SwingUtilities.convertPoint(gp, gPoint, cp);
      int origNewX = cPoint.x;
      int origNewY = cPoint.y;
      if (alignToGrid) {
        cPoint = snapToGrid(cPoint);
      }
      int newX = cPoint.x;
      int newY = cPoint.y;
      Component selectedComponent = selectedComponents.get(0);
      int x = selectedComponent.getX();
      int y = selectedComponent.getY();
      int w = selectedComponent.getWidth();
      int h = selectedComponent.getHeight();
      Rectangle newBounds = null;
      boolean moved = false;
      switch (resizeDir) {
        case TOP_LEFT:
          newBounds =
            newBounds(selectedComponent, newX, newY, w+x-newX, h+y-newY);
          break;
        case TOP:
          newBounds = newBounds(selectedComponent, x, newY, w, h+y-newY);
          break;
        case TOP_RIGHT:
          newBounds = newBounds(selectedComponent, x, newY, newX-x+1, h+y-newY);
          break;
        case LEFT:
          newBounds = newBounds(selectedComponent, newX, y, w+x-newX, h);
          break;
        case RIGHT:
          newBounds = newBounds(selectedComponent, x, y, newX-x+1, h);
          break;
        case BOTTOM_LEFT:
          newBounds = newBounds(selectedComponent, newX, y, w+x-newX, newY-y+1);
          break;
        case BOTTOM:
          newBounds = newBounds(selectedComponent, x, y, w, newY-y+1);
          break;
        case BOTTOM_RIGHT:
          newBounds = newBounds(selectedComponent, x, y, newX-x+1, newY-y+1);
          break;
        default:  // Move selected components
          Rectangle selBounds = getSelBounds();
          int n = selectedComponents.size();
          int snapX = 0;
          int snapY = 0;
          for (int i=0; i<n; i++) {
            selectedComponent = selectedComponents.get(i);
            Rectangle oldBounds = selectedComponent.getBounds();
            int dx = oldBounds.x - selBounds.x;
            int dy = oldBounds.y - selBounds.y;
            w = selectedComponent.getWidth();
            h = selectedComponent.getHeight();
            newBounds = newBounds(
              selectedComponent, origNewX+dx+offX, origNewY+dy+offY, w, h
            );
            if (alignToGrid) {
              if (i == 0) {
                Point newCorner = snapToGrid(newBounds.x, newBounds.y);
                snapX = newBounds.x - newCorner.x;
                snapY = newBounds.y - newCorner.y;
              }
              newBounds.x -= snapX;
              newBounds.y -= snapY;
            }
            selectedComponent.setBounds(newBounds);
          }
          moved = true;
          break;
      }
      if (!moved) {
        selectedComponent.setBounds(newBounds);
      }
    }else{
      Point gPoint = e.getPoint();
      currentPoint = SwingUtilities.convertPoint(gp, gPoint, cp);
    }
    repaint();
  }

  /**
   * Moves the selected components by a specified distance.
   * @param     dx      The distance to move the components horizontally.
   * @param     dy      The distance to move the components vertically.
   */
  private void moveSelectedComponents(int dx, int dy)
  {
    int n = selectedComponents.size();
    for (int i=0; i<n; i++) {
      Component selectedComponent = selectedComponents.get(i);
      Rectangle oldBounds = selectedComponent.getBounds();
      int x = oldBounds.x;
      int y = oldBounds.y;
      int w = oldBounds.width;
      int h = oldBounds.height;
      Rectangle newBounds = new Rectangle(x+dx, y+dy, w, h);
      selectedComponent.setBounds(newBounds);
    }
  }

  /**
   * Process mouse moves on the glass pane in design mode.
   * @param     e       The event generated by a mouse move.
   */
  private void designModeMouseMoved(MouseEvent e)
  {
    if (forwardEvent(e)) {
      return;
    }
    //if (!isMenuButton(e) && designMode && (selectedComponents.size() > 0) &&
        //(cp.getLayout() == null)) {
      adjustCursor(e.getPoint());
    //}
  }

  /**
   * Process mouse clicks on the glass pane in design mode.
   * @param     e       The event generated by a mouse click.
   */
  private void designModeMouseClicked(MouseEvent e)
  {
    if (forwardEvent(e)) {
      return;
    }
  }

  /**
   * Check if a mouse event occurred over a panel component contained inside
   * this panel. If so, and this second panel contains a component at the
   * point of the event, the event is forwarded to that second panel.
   * @param     e       The event to process.
   * @return    True if the event was forwarded, false otherwise.
   */
  private boolean forwardEvent(MouseEvent e)
  {
    int x = e.getX();
    int y = e.getY();
    Point p = SwingUtilities.convertPoint(gp, x, y, cp);
    Component c = cp.getComponentAt(p);
    boolean result = false;
    if ((c != null) && (c instanceof PanelComponent)) {
      PanelComponent panel = (PanelComponent)c;
      Point p2 = SwingUtilities.convertPoint(gp, x, y, panel.cp);
      if (panel.designMode) {
        Component c2 = panel.cp.getComponentAt(p2);
        if ((c2 != null) && !c2.equals(panel.cp)) {
          result = true;
        }
        if (!result) {
          Point p3 = SwingUtilities.convertPoint(gp, x, y, panel.gp);
          Rectangle[] rh = panel.resizeHandles;
          for (int i=0; i<8; i++) {
            if (rh[i].contains(p3)) {
              result = true;
              break;
            }
          }
        }
        if (result) {
          Component glassPane = panel.getGlassPane();
          MouseEvent e2 = SwingUtilities.convertMouseEvent(gp, e, glassPane);
          sendEvent(glassPane, e2);
          if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            lastComp = glassPane;
          }else{
            lastComp = null;
          }
        }else{
          lastComp = null;
        }
      }
    }
    return result;
  }

//  /**
//   * Converts a <code>MouseEvent</code> delivered to a component to a
//   * <code>MouseEvent</code> of a different type, suitable for delivery to
//   * another component.
//   * @param     from    The component to which the event was delivered.
//   * @param     e       The event that was delivered.
//   * @param     to      The component for which the event will be converted.
//   * @param     id      The type of the new event.
//   * @return    The converted event.
//   */
//  private static MouseEvent convertMouseEvent(
//    Component from, MouseEvent e, Component to, int id)
//  {
//    Point p = SwingUtilities.convertPoint(from, e.getX(), e.getY(), to);
//    MouseEvent e2 = new MouseEvent(
//      to,
//      id,
//      e.getWhen(),
//      e.getModifiers(),
//      p.x,
//      p.y,
//      e.getClickCount(),
//      e.isPopupTrigger()
//    );
//    return e2;
//  }

  /**
   * Returns the component under the panel, at a given point.
   * @param     x       The X coordinate of the point, relative to the glass
   *                    pane.
   * @param     y       The Y coordinate of the point, relative to the glass
   *                    pane.
   */
  Component getComponentUnderAt(int x, int y)
  {
    Container parent = this;
    do {
      Container par = parent.getParent();
      if (par instanceof Container) {
        parent = par;
      }else{
        break;
      }
    } while (true);
    Point pt = SwingUtilities.convertPoint(gp, x, y, parent);
    Component intf = SwingUtilities.getAncestorOfClass(
      ESlateInternalFrame.class,
      this
    );
    if (intf != null) {
      exclude.add(intf);
    }else{
      exclude.add(this);
    }
    Component comp = findComponentAt(parent, exclude, pt.x, pt.y);
    return comp;
  }

  /**
   * Performs the functionality of findComponentAt, except that it ignores a
   * given set of components. The code was shamelessly ripped off
   * Container.java.
   * @param     start   The container from which to start looking for
   *                    components.
   * @param     exclude If any of the components in this list are encountered,
   *                    search will continue.
   * @param     x       The X coordinate of the location at which to look for
   *                    components.
   * @param     y       The Y coordinate of the location at which to look for
   *                    components.
   * @return    The deepest component at (x,y). If a component in the
   *            <code>exclude</code> list is at that position, then the
   *            deepest component <em>under</em> it will be returned,
   *            instead.
   */
  @SuppressWarnings(value={"deprecation"})
  private static Component findComponentAt(
    Container start, ComponentBaseArray exclude, int x, int y)
  {
    if (!(start.contains(x, y) && start.isVisible())) {
      return null;
    }
    int ncomponents = start.getComponentCount();
    Component component[] = start.getComponents();

    // Two passes: see comment in sun.awt.SunGraphicsCallback
    for (int i = 0 ; i < ncomponents ; i++) {
      Component comp = component[i];
      if (comp != null &&
          !(comp.getPeer() instanceof java.awt.peer.LightweightPeer) &&
          !exclude.contains(comp)) {
        if (comp instanceof ESlateInternalFrameTitlePanel) {
          return comp;
        }
        if (comp instanceof Container) {
          comp = findComponentAt(
            (Container)comp, exclude, x - comp.getX(), y - comp.getY()
          );
        } else {
          comp = comp.getComponentAt(x - comp.getX(), y - comp.getY());
        }
        if (comp != null && comp.isVisible()) {
          return comp;
        }
      }
    }
    for (int i = 0 ; i < ncomponents ; i++) {
      Component comp = component[i];
      if (comp != null &&
          comp.getPeer() instanceof java.awt.peer.LightweightPeer &&
          !exclude.contains(comp)) {
        if (comp instanceof ESlateInternalFrameTitlePanel) {
          return comp;
        }
        if (comp instanceof Container) {
          comp = findComponentAt(
            (Container)comp, exclude, x - comp.getX(), y - comp.getY()
          );
        } else {
          comp = comp.getComponentAt(x - comp.getX(), y - comp.getY());
        }
        if (comp != null && comp.isVisible()) {
          return comp;
        }
      }
    }
    return start;
  }

  /**
   * Forwards a mouse event to a component.
   * @param     c       The component to which to forward the event.
   * @param     e       The event to forward to teh component.
   */
  private void sendEvent(Component c, MouseEvent e)
  {
    c.dispatchEvent(e);
  }

  /**
   * Returns the bounds of the selected components.
   * @return    The bounds of the selected components.
   */
  private Rectangle getSelBounds()
  {
    tmpRect2 = selectedComponents.get(0).getBounds();
    int n = selectedComponents.size();
    for (int i=1; i<n; i++) {
      tmpRect2 = tmpRect2.union(selectedComponents.get(i).getBounds());
    }
    return tmpRect2;
  }

  // ComponentListener interface

  public void componentHidden(ComponentEvent e)
  {
  }
  public void componentMoved(ComponentEvent e)
  {
    repaint();
  }
  public void componentResized(ComponentEvent e)
  {
    Component comp = e.getComponent();
    if (comp instanceof JComponent) {
      ((JComponent)comp).revalidate();
    }else{
      comp.invalidate();
      comp.validate();
    }
    repaint();
  }
  public void componentShown(ComponentEvent e)
  {
  }

  /**
   * Paint the panel without clipping and without highlighting selected
   * components.
   * @param     g       The graphics context in which to paint the component.
   */
  public void simplePrint(Graphics g)
  {
    simplePaint = true;
    super.print(g);
    simplePaint = false;
  }

  /**
   * Paint the component.
   * @param     g       The graphics context in which to paint the component.
   */
  public void paint(Graphics g)
  {
    if (simplePaint) {
      super.paint(g);
      return;
    }
    Graphics2D g2 = (Graphics2D)g;
    clip(g2);
    if ((effectRunner != null) && effectRunner.isEffectRunning()) {
      effectRunner.getEffect().realizeEffect(g2);
    }
    super.paint(g);
    if (designMode && (selectedComponents.size() > 0) &&
        (cp.getLayout()==null)) {
      g.setColor(selectionColor);
      int n = selectedComponents.size();
      for (int i=0; i<n; i++) {
        Component selectedComponent = selectedComponents.get(i);
        int x = selectedComponent.getX();
        int y = selectedComponent.getY();
        int s = 7;
        int s2 = s / 2;
        int w = selectedComponent.getWidth();
        int w2 = w / 2;
        int h = selectedComponent.getHeight();
        int h2 = h / 2;

        g.drawRect(x, y, w, h);

        // Draw resize handles if we have only one selected component.
        if (n == 1) {
          setHandleRectangle(TOP_LEFT, x-s2, y-s2, s, s);
          setHandleRectangle(TOP, x+w2-s2, y-s2, s, s);
          setHandleRectangle(TOP_RIGHT, x+w-s2, y-s2, s, s);
          setHandleRectangle(LEFT, x-s2, y+h2-s2, s, s);
          setHandleRectangle(RIGHT, x+w-s2, y+h2-s2, s, s);
          setHandleRectangle(BOTTOM_LEFT, x-s2, y+h-s2, s, s);
          setHandleRectangle(BOTTOM, x+w2-s2, y+h-s2, s, s);
          setHandleRectangle(BOTTOM_RIGHT, x+w-s2, y+h-s2, s, s);

          for (int j=0; j<8; j++) {
            g2.fill(resizeHandles[j]);
          }
        }
      }
    }
    if (selecting) {
      int x = Math.min(clickPoint.x, currentPoint.x);
      int y = Math.min(clickPoint.y, currentPoint.y);
      int w = Math.abs(clickPoint.x - currentPoint.x) + 1;
      int h = Math.abs(clickPoint.y - currentPoint.y) + 1;
      g.setColor(Color.black);
      g.setXORMode(Color.white);
      g.drawRect(x, y, w, h);
    }
  }

  /**
   * Clips the component to the selected clip shape.
   * @param     g       The component's graphics context.
   */
  private void clip(Graphics2D g)
  {
    switch (clipType) {
      case ShapeType.RECTANGLE:
        // No clipping necessary.
        break;
      case ShapeType.ELLIPSE:
        // Clip to the ellipse specified by the component's bounding box.
        if (clipShape != null) {
          g.clip(clipShape);
        }
        break;
      case ShapeType.POLYGON:
        // Clip to the specified polygon.
        if ((clipPoly != null) && (clipPoly.npoints > 2)) {
          g.clip(clipPoly);
        }
        break;
      case ShapeType.FREEHAND:
        // Clip to the specified shape.
        if (clipShape != null) {
          g.clip(clipShape);
        }
    }
  }

  /**
   * Check whether a point is inside the panel, taking the clipping shape
   * into consideration.
   * <EM>Note:</EM> <code>inside</code> may be deprecated in favor of
   * <code>contains</code>, but this is the method that does the actual job in
   * Sun's implementation, so this is the one that we override.
   * @param     x       The X coordinate of the point.
   * @param     y       The Y coordinate of the point.
   */
  @SuppressWarnings(value={"deprecation"})
  public boolean inside(int x, int y)
  {
    boolean result;
    switch (clipType) {
      case ShapeType.POLYGON:
        if (clipPoly != null) {
          Rectangle r = shapeBounds;//clipPoly.getBounds();
          if (r.contains(x, y)) {
            result = clipPoly.contains(x, y);
          }else{
            result = false;
          }
        }else{
          result = super.inside(x, y);
        }
        break;
      case ShapeType.ELLIPSE:
      case ShapeType.FREEHAND:
        if (clipShape != null) {
          Rectangle r = shapeBounds;//clipShape.getBounds();
          if (r.contains(x, y)) {
            result = clipShape.contains(x, y);
          }else{
            result = false;
          }
        }else{
          result = super.inside(x, y);
        }
        break;
      case ShapeType.RECTANGLE:
      default:
        result = super.inside(x, y);
      break;
    }
    return result;
  }

  /**
   * Specifies the shape type to which the panel is being clipped.
   * @param     shape   The clip shape tape.
   */
  public void setClipShapeType(ShapeType shape)
  {
    clipType = shape.type;
    clipPoly = null;
    if (clipType == ShapeType.ELLIPSE) {
      Ellipse2D.Float el = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
      clipShape = el;
      shapeBounds = el.getBounds();
    }else{
      clipShape = null;
      shapeBounds = null;
    }

    // To ensure that changes appropriate to the design mode and the new clip
    // shape are made.
    boolean mode = designMode;
    setDesignMode(!mode);
    setDesignMode(mode);

    repaint();

    // Fire shape changed listeners
    fireShapeChangedListeners();
  }

  /**
   * Returns the type of shape to which the panel is being clipped.
   * @return    A <code>ShapeType</code> instance.
   */
  public ShapeType getClipShapeType()
  {
    return new ShapeType(clipType, this);
  }

  /**
   * Specifies the shape to which the panel is being clipped. This method
   * should be invoked after <code>setClipShapeType</code> has been invoked.
   * It is meant to be invoked by pressing the "edit shape" button in the
   * property editor.
   */
  public void setClipShape(Shape s)
  {
    switch (clipType) {
      case ShapeType.ELLIPSE:
      case ShapeType.FREEHAND:
        clipPoly = null;
        clipShape = s;
        shapeBounds = s.getBounds();
        break;
      case ShapeType.POLYGON:
        clipPoly = (Polygon)s;
        clipShape = null;
        shapeBounds = s.getBounds();
        break;
      case ShapeType.RECTANGLE:
      default:
        clipPoly = null;
        clipShape = null;
        shapeBounds = null;
    }
    repaint();

    // Fire shape changed listeners
    fireShapeChangedListeners();
  }

  /**
   * Returns the shape to which the panel is being clipped.
   * @return    The shape to which the panel is being clipped. If the
   *            component is a regular, rectangular component,
   *            <code>null</code> is returned.
   */
  public Shape getClipShape()
  {
    switch (clipType) {
      case ShapeType.ELLIPSE:
      case ShapeType.FREEHAND:
        return clipShape;
      case ShapeType.POLYGON:
        return clipPoly;
      case ShapeType.RECTANGLE:
      default:
        return null;
    }
  }

  /**
   * Add a listener for shape changed events.
   * @param     l       The listener to add.
   */
  public void addShapeChangedListener(ShapeChangedListener l)
  {
    listeners.addShapeChangedListener(l);
  }

  /**
   * Remove a listener for shape changed events.
   * @param     l       The listener to remove.
   */
  public void removeShapeChangedListener(ShapeChangedListener l)
  {
    listeners.removeShapeChangedListener(l);
  }

  /**
   * Fire listeners for shape changed events.
   */
  private void fireShapeChangedListeners()
  {
    Shape s;
    switch (clipType) {
      case ShapeType.ELLIPSE:
      case ShapeType.FREEHAND:
        s = clipShape;
        break;
      case ShapeType.POLYGON:
        s = clipPoly;
        break;
      case ShapeType.RECTANGLE:
      default:
        s = null;
        break;
    }
    listeners.fireShapeChangedListeners(clipType, s);
  }

  /**
   * Computes the new bounds of a selected component, based on
   * a given set of suggested values.
   * @param     selectedComponent       The component.
   * @param     x                       The suggested new x position.
   * @param     y                       The suggested new y position.
   * @param     width                   The suggested new width.
   * @param     height                  The suggested new height.
   * @return    The actual new bounds for the currently selected component.
   */
  private Rectangle newBounds(
    Component selectedComponent, int x, int y, int width, int height)
  {
    if (width < 0) {
      width = 0;
    }
    if (height < 0) {
      height = 0;
    }
/*
    // This code adjusts the bounds taking the minimum and maximum size of the
    // component under consideration. Unfortunately, respecting min and max
    // size makes resizing components that use too restrictive values for
    // these dimensions impossible, so we comment it out. [Sob!]
    //
    Dimension minSize = selectedComponent.getMinimumSize();
    Dimension maxSize = selectedComponent.getMaximumSize();
    int minW = minSize.width;
    int maxW = maxSize.width;
    int minH = minSize.height;
    int maxH = maxSize.height;
    Point sLoc = selectedComponent.getLocation();
    int sX = sLoc.x;
    int sY = sLoc.y;
    if (width < minW) {
      if (x != sX) {
        x += (width - minW);
      }
      width = minW;
    }
    if (height < minH) {
      if (y != sY) {
        y += (height - minH);
      }
      height = minH;
    }
    if (width > maxW) {
      if (x != sX) {
        x += (width - maxW);
      }
      width = maxW;
    }
    if (height > maxH) {
      if (y != sY) {
        y += (height - maxH);
      }
      height = maxH;
    }
*/
    tmpRect1.x = x;
    tmpRect1.y = y;
    tmpRect1.width = width;
    tmpRect1.height = height;
    return tmpRect1;
  }

  /**
   * Returns the point of the alignment grid that is closest to a given
   * point.
   * @param     pt      The given point.
   * @return    The requested point.
   */
  private Point snapToGrid(Point pt)
  {
    int step = getGridStep();
    if (step == 1) {
      return pt;
    }else{
      int signX;
      if (pt.x >= 0) {
        signX = 1;
      }else{
        signX = -1;
        pt.x = -pt.x;
      }
      int signY;
      if (pt.y >= 0) {
        signY = 1;
      }else{
        signY = -1;
        pt.y = -pt.y;
      }
      int step2 = step / 2;
      tmpPt.x = ((pt.x + step2) / step) * step * signX;
      tmpPt.y = ((pt.y + step2) / step) * step * signY;
      return tmpPt;
    }
  }

  /**
   * Returns the point of the alignment grid that is closest to a given
   * point.
   * @param     x       The x coordinate of the given point.
   * @param     y       The y coordinate of the given point.
   * @return    The requested point.
   */
  private Point snapToGrid(int x, int y)
  {
    tmpPt.x = x;
    tmpPt.y = y;
    return snapToGrid(tmpPt);
  }

  /**
   * Sets the parameters of the rectangle correponding to one of the resize
   * handles of the currently selected component.
   * @param     which   The resize handle whose rectangle's parameters will be
   *                    set.
   * @param     x       The x coordinate of the rectangle.
   * @param     y       The y coordinate of the rectangle.
   * @param     width   The width of the rectangle.
   * @param     height  The height of the rectangle.
   */
  private void setHandleRectangle(int which, int x, int y, int width, int
                                  height)
  {
    Rectangle r = resizeHandles[which];
    r.x = x;
    r.y = y;
    r.width = width;
    r.height = height;
  }

  /**
   * Checks whether a mouse event refers to the "menu" button.
   * @param     e       The event to check.
   * @return    True if yes, false if no.
   */
  private boolean isMenuButton(MouseEvent e)
  {
    // Check for popup menu mouse button. Since isPopupTrigger does
    // not appear to be working under Windows, we also check
    // explicitly for the right button.
    return
      (e.isPopupTrigger() || (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0);
  }

  /**
   * Move selected components to the left.
   */
  private void left()
  {
    if (designMode && (cp.getLayout()==null)) {
      int dx;
      if (alignToGrid) {
        dx = -getGridStep();
      }else{
        dx = -1;
      }
      moveSelectedComponents(dx, 0);
    }
  }

  /**
   * Move selected components to the right.
   */
  private void right()
  {
    if (designMode && (cp.getLayout()==null)) {
      int dx;
      if (alignToGrid) {
        dx = getGridStep();
      }else{
        dx = 1;
      }
      moveSelectedComponents(dx, 0);
    }
  }

  /**
   * Move selected components up.
   */
  private void up()
  {
    if (designMode && (cp.getLayout()==null)) {
      int dy;
      if (alignToGrid) {
        dy = -getGridStep();
      }else{
        dy = -1;
      }
      moveSelectedComponents(0, dy);
    }
  }

  /**
   * Move selected components down.
   */
  private void down()
  {
    if (designMode && (cp.getLayout()==null)) {
      int dy;
      if (alignToGrid) {
        dy = getGridStep();
      }else{
        dy = 1;
      }
      moveSelectedComponents(0, dy);
    }
  }

  /**
   * Set a UI effect.
   * @param     effect  The effect to set.
   */
  public void setEffect(EffectInterface effect)
  {
    if (effectRunner == null) {
      effectRunner = new EffectRunner(this);
    }
    effectRunner.setEffect(effect);
  }

  /**
   * Retrieve the current UI effect associated with this component.
   * @return    The current UI effect associated with this component.
   */
  public EffectInterface getEffect()
  {
    if (effectRunner != null) {
      return effectRunner.getEffect();
    }else{
      return null;
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

  public void parentChanged(ParentChangedEvent e)
  {
    ESlateHandle childHandle = e.getComponent();
    ESlateHandle newParent = e.getNewParent();
    ESlateHandle oldParent = e.getOldParent();
    ESlateHandle h = getESlateHandle();
    if (h.equals(oldParent) && !(h.equals(newParent))) {
      Object child = childHandle.getComponent();
      if (child instanceof Component) {
        cp.ignoreContainerEvents = true;
        cp.remove((Component)child);
        cp.ignoreContainerEvents = false;
      }
    }
  }

  public void componentNameChanged(ComponentNameChangedEvent e)
  {
  }

  public void disposingHandle(HandleDisposalEvent e)
  {
  }

  public void handleDisposed(HandleDisposalEvent e)
  {
  }

  /**
   * <code>FileFilter</code> for the file dialog.
   */
  private class PanelFileFilter extends javax.swing.filechooser.FileFilter
  {
    /**
     * Accept panel contents.
     */
    final static int PANEL_CONTENTS = 0;
    /**
     * Accept panels.
     */
    final static int PANELS = 1;
    /**
     * Indicates whether the filter accepts panel contents or panels.
     */
    private int type = PANEL_CONTENTS;

    /**
     * Construct the file filter.
     */
    PanelFileFilter()
    {
      super();
    }

    /**
     * Specify whether the filter accepts panels or panel contents.
     * @param   type    One of PANEL_CONTENTS, PANELS.
     */
    void setType(int type)
    {
      this.type = type;
    }

    /**
     * Returns the extension of the currently accepted files.
     * @return  The extension of the currently accepted files.
     */
    private String getExtension()
    {
      if (type == PANEL_CONTENTS) {
        return PCONT_EXT;
      }else{
        return PANEL_EXT;
      }
    }

    /**
     * Check a file for acceptance.
     * @param   f       The file to check.
     * @return  True if the file is accepted, false otherwise.
     */
    public boolean accept(File f)
    {
      if (f.isDirectory() || (f.getName().endsWith(getExtension()))) {
        return true;
      }else{
        return false;
      }
    }

    /**
     * Returns the description of the files accepted by this filter.
     * @return  The description of the files accepted by this filter.
     */

    public String getDescription()
    {
      if (type == PANEL_CONTENTS) {
        return resources.getString("panelContents") + " (.pcont)";
      }else{
        return resources.getString("panels") + " (.panel)";
      }
    }
  }

}
