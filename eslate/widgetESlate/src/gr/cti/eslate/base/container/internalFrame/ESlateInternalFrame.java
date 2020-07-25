package gr.cti.eslate.base.container.internalFrame;

import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.container.DesktopItem;
import gr.cti.eslate.base.container.DesktopPane;
import gr.cti.eslate.base.container.ESlateContainer;
import gr.cti.eslate.base.container.Microworld;
import gr.cti.eslate.base.container.event.ESlateInternalFrameEvent;
import gr.cti.eslate.base.container.event.ESlateInternalFrameListener;
import gr.cti.eslate.base.effect.EffectInterface;
import gr.cti.eslate.base.effect.EffectRunner;
import gr.cti.eslate.panel.PanelComponent;
import gr.cti.eslate.shapedComponent.ExternalizableShape;
import gr.cti.eslate.shapedComponent.ShapeChangedListener;
import gr.cti.eslate.shapedComponent.ShapeChangedListenerSupport;
import gr.cti.eslate.shapedComponent.ShapeType;
import gr.cti.eslate.shapedComponent.ShapedComponent;
import gr.cti.eslate.utils.BooleanWrapper;
import gr.cti.eslate.utils.CustomBorder;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.BoolBaseArray;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.StringBaseArray;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultDesktopManager;
import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;

/**
 * This is an internal frame implementation. It provides basic "skin" capabilities
 * giving hooks to wrap around the content area panels that contain graphics, functionality
 * or both.
 * @author  Giorgos Vasiliou
 * @version 1.0
 */

public class ESlateInternalFrame extends JComponent implements WindowConstants, RootPaneContainer, DesktopItem, ESlatePart, Externalizable, ShapedComponent {
    public static final int version = 3;
    public static final int FRAME_STATE_VERSION = 200;
    /* The default min and max sizes an ESlateInternalFrame can have.
     */
    public static final Dimension DEFAULT_MIN_SIZE = new Dimension(58, 23);
    public static final Dimension DEFAULT_MAX_SIZE = new Dimension(30000, 30000);
    /* These are the borders used for the ESlateInternalFrame. The first 8 are used
     * to indicate that the frame can be resized.
     */
    private static Border WEST_BORDER = null;// = new CompoundBorder(new EmptyBorder(3, 0, 3, 3), new DegradatedColorMatteBorder(0, 3, 0, 0));
    private static Border EAST_BORDER = null;// = new CompoundBorder(new EmptyBorder(3, 3, 3, 0), new DegradatedColorMatteBorder(0, 0, 0, 3));
    private static Border NORTH_BORDER = null;// = new CompoundBorder(new EmptyBorder(0, 3, 3, 3), new DegradatedColorMatteBorder(3, 0, 0, 0));
    private static Border SOUTH_BORDER = null;// = new CompoundBorder(new EmptyBorder(3, 3, 0, 3), new DegradatedColorMatteBorder(0, 0, 3, 0));
    private static Border SOUTH_WEST_BORDER = null;// = new CompoundBorder(new EmptyBorder(3, 0, 0, 3), new DegradatedColorMatteBorder(0, 3, 3, 0));
    private static Border SOUTH_EAST_BORDER = null;// = new CompoundBorder(new EmptyBorder(3, 3, 0, 0), new DegradatedColorMatteBorder(0, 0, 3, 3));
    private static Border NORTH_EAST_BORDER = null;// = new CompoundBorder(new EmptyBorder(0, 3, 3, 0), new DegradatedColorMatteBorder(3, 0, 0, 3));
    private static Border NORTH_WEST_BORDER = null;// = new CompoundBorder(new EmptyBorder(0, 0, 3, 3), new DegradatedColorMatteBorder(3, 3, 0, 0));
    private static Border NORTH_SOUTH_WEST_EAST_BORDER = null;// = new DegradatedColorMatteBorder(3, 3, 3, 3);
    private static LineBorder ORANGE_LINE_BORDER = null;// = new LineBorder(Color.orange, 1);
    private static LineBorder GRAY_LINE_BORDER = null;// = new LineBorder(Color.lightGray, 1);

/*    public final static Border WEST_BORDER = new CompoundBorder(new EmptyBorder(3, 0, 3, 3), new DegradatedColorMatteBorder(0, 3, 0, 0));
    public final static Border EAST_BORDER = new CompoundBorder(new EmptyBorder(3, 3, 3, 0), new DegradatedColorMatteBorder(0, 0, 0, 3));
    public final static Border NORTH_BORDER = new CompoundBorder(new EmptyBorder(0, 3, 3, 3), new DegradatedColorMatteBorder(3, 0, 0, 0));
    public final static Border SOUTH_BORDER = new CompoundBorder(new EmptyBorder(3, 3, 0, 3), new DegradatedColorMatteBorder(0, 0, 3, 0));
    public final static Border SOUTH_WEST_BORDER = new CompoundBorder(new EmptyBorder(3, 0, 0, 3), new DegradatedColorMatteBorder(0, 3, 3, 0));
    public final static Border SOUTH_EAST_BORDER = new CompoundBorder(new EmptyBorder(3, 3, 0, 0), new DegradatedColorMatteBorder(0, 0, 3, 3));
    public final static Border NORTH_EAST_BORDER = new CompoundBorder(new EmptyBorder(0, 3, 3, 0), new DegradatedColorMatteBorder(3, 0, 0, 3));
    public final static Border NORTH_WEST_BORDER = new CompoundBorder(new EmptyBorder(0, 0, 3, 3), new DegradatedColorMatteBorder(3, 3, 0, 0));
    public final static Border NORTH_SOUTH_WEST_EAST_BORDER = new DegradatedColorMatteBorder(3, 3, 3, 3);
    private final static LineBorder ORANGE_LINE_BORDER = new LineBorder(Color.orange, 1);
    private static final LineBorder GRAY_LINE_BORDER = new LineBorder(Color.lightGray, 1);
*/    /** The normal border of the ESlateInternalFrame.
     */
    public static final Border FRAME_NORMAL_BORDER = new EmptyBorder(3,3,3,3); //new LineBorder(new Color(0,0,0,0), 2);
    /** The border of the ESlateInternalFrame when it is active and its title panel is
     *  no visible.
     */
//    public static final Border FRAME_SELECTED_BORDER = new CompoundBorder(new EmptyBorder(2,2,2,2), ORANGE_LINE_BORDER);
    private static Border FRAME_SELECTED_BORDER = null; //new CompoundBorder(new EmptyBorder(2,2,2,2), ORANGE_LINE_BORDER);
    /**
     * The serial version UID for persistence.
     */
    private final static long serialVersionUID=1000L;
    /**
     * The frame's ESlate handle. Each frame has a handle, which is added to its contained
     * component's handle. This way the frame is accesible by scripting. Furthermore,
     * the frame's contents are also accesible by scripting as well as their plugs are
     * accessible by the plug handler.
     */
    private ESlateHandle handle;
    /**
     * This is the "host" (parent) ESlatePart of the frame. Although the frame hosts
     * the ESlatePart visualy, the ESlatePart hosts the frame in the component hierarchy.
     */
    protected ESlateHandle componentHandle;
    /**
     * This action can be added to any button that is meant by the user the "close" button.
     */
    public Action closeAction;
    /**
     * This action can be added to any button that is meant by the user the "maximize" button.
     */
    public Action maximizeAction;
    /**
     * This action can be added to any button that is meant by the user the "iconify" button.
     */
    public Action iconifyAction;
    /**
     * This action can be added to any button that is meant by the user the "restore" button.
     */
    public Action restoreAction;

    /**
     * The listener responsible for resizing.
     */
    protected InternalFrameBorderListener borderListener;
    /**
     * The listener responsible for dragging around the content area.
     */
    protected InternalFrameDragAroundListener dragAroundListener;
    /**
     * The listener responsible for property changes.
     */
    protected InternalFramePropertyChangeListener propertyChangeListener;
    /**
     * This listener listens to the parent bounds change.
     */
    protected ComponentListener componentListener;
    /**
     * This listener listens to the components added to or removed from
     * the content pane of the panel. If any of these, or the content pane itself,
     * is an ESlatePart object, the frame constructs a handle (if it hasn't done this
     * already) and adds the frame's handle to the ESlatePart's handle.
     */
    protected ContainerListener containerListener;
    /**
     * The frame's parent bounds.
     */
    private Rectangle parentBounds = null;
    /**
     * The bounds the frame had before it has been maximized.
     */
    private Rectangle restoreBounds = null;
    /**
     * The title component.
     */
    protected JComponent titlePane;
    /**
     * The north component.
     */
    protected JComponent north;
    /**
     * The south component.
     */
    protected JComponent south;
    /**
     * The east component.
     */
    protected JComponent east;
    /**
     * The west component.
     */
    protected JComponent west;
    /**
     * Indicates the frame ability to be dragged from the north pane.
     */
    protected boolean dragNorth;
    /**
     * Indicates the frame ability to be dragged from the south pane.
     */
    protected boolean dragSouth;
    /**
     * Indicates the frame ability to be dragged from the west pane.
     */
    protected boolean dragWest;
    /**
     * Indicates the frame ability to be dragged from the east pane.
     */
    protected boolean dragEast;
    /**
     * Indicates if the north pane is visible.
     */
    protected boolean northVisible;
    /**
     * Indicates if the south pane is visible.
     */
    protected boolean southVisible;
    /**
     * Indicates if the east pane is visible.
     */
    protected boolean eastVisible;
    /**
     * Indicates if the west pane is visible.
     */
    protected boolean westVisible;
    /**
     * Indicates if the close Button is visible.
     */
    protected boolean closeButtonVisible = true;
    /**
     * Indicates if the minimize button is visible.
     */
    protected boolean minimizeButtonVisible = true;
    /**
     * Indicates if the naximize button is visible.
     */
    protected boolean maximizeButtonVisible = true;
    /**
     * Indicates if the help Button is visible.
     */
    protected boolean helpButtonVisible = true;
    /**
     * Indicates if the info Button is visible.
     */
    protected boolean infoButtonVisible = true;
    /**
     * Indicates if the plug Button is visible.
     */
    protected boolean plugButtonVisible = true;
    /**
     * Indicates whether the component's max size will be respected while resizing
     * an ESlateInternalFrame or the default max size of the ESlateInternalFrame
     * will be used.
     */
    protected boolean maxSizeRespected = false;
    /**
     * Indicates whether the component's min size will be respected while resizing
     * an ESlateInternalFrame or the default min size of the ESlateInternalFrame
     * will be used.
     */
    protected boolean minSizeRespected = false;
    /** Adjusts the ability of a component to get activated. A component that has this
     *  flag false, can never be activated.
     */
    protected boolean activatable = true;
    /** Reports if the name if the component is editable from the ESlateHandle's
     *  MenuPanel.
     */
    protected boolean componentNameChangeableFromMenuBar = true;
    /**
     * The content pane that holds the real contents, without the title pane.
     * <p>
     * This pane holds the surrounding components (usually panels) that implement the basic
     * "skin" support. The CENTER of this pane is the user defined content pane.
     * All the calls to <code>getContentPane()</code> and <code>setContentPane()</code>
     * on the frame are directed to the user defined pane, which means that this pane,
     * which contains the skin implementation, cannot change.
     */
    protected JPanel contentPane;
    /**
     * The pane that exists in the root pane of the frame.
     * <p>
     * This pane holds the contentPane (true content and surrounding components)
     * and the titlePane.
     */
    protected JPanel framePane;
    /**
     * A handle to the user defined content pane.
     * <p>
     * The user defined content pane resides into another pane (see <code>contentPane</code>)
     * which provides the surrounding components to implement basic skin support.
     */
    protected Container content;
    /**
     * This bundle is used both by the frame and its BeanInfo.
     */
    protected static ResourceBundle bundle;

    protected static final String CLOSE_CMD = "Close";
    protected static final String ICONIFY_CMD = "Minimize";
    protected static final String RESTORE_CMD = "Restore";
    protected static final String MAXIMIZE_CMD = "Maximize";
    protected static final String MOVE_CMD = "Move";
    protected static final String SIZE_CMD = "Size";

   /**
     * The <code>JRootPane</code> instance that manages the
     * <code>contentPane</code>
     * and optional <code>menuBar</code> for this frame, as well as the
     * <code>glassPane</code>.
     *
     * @see JRootPane
     * @see RootPaneContainer
     */
    protected RootPane rootPane;

    /**
     * If true then calls to <code>add</code> and <code>setLayout</code>
     * cause an exception to be thrown.
     */
    protected boolean rootPaneCheckingEnabled = false;

    /** The frame can be closed. */
    protected boolean closable;
    /** The frame has been closed. */
    protected boolean isClosed;
    /** The frame can be expanded to the size of the desktop pane. */
    protected boolean maximizable;
    /**
     * The frame has been expanded to its maximum size.
     * @see #maximizable
     */
    protected boolean isMaximum;
    /**
     * The frame can "iconized" (shrunk down and displayed as
     * an icon-image).
     */
    protected boolean iconifiable;
    /**
     * The frame has been iconized.
     * @see #iconifiable
     */
    protected boolean isIcon = true;
    /** The frame's size can be changed. */
    protected boolean resizable;
    /** The frame is currently selected (active). */
    protected boolean isSelected;
    /** The frame is a modal one. No other frame can be selected until this one is closed.
     */
    protected boolean isModal = false;
    /** The layer a modal frame belonged to, before it became modal.
     */
    private int _prevLayer = -1;
    /** The icon shown in the top-left corner of the frame. */
//    protected Icon frameIcon;

    /** The frame title is visible. */
    protected boolean titlePanelVisible = false;
    /** Component activation policy. */
    protected boolean compActMouseClick;

    private boolean opened;

    private Rectangle normalBounds = null;

    private int defaultCloseOperation = DISPOSE_ON_CLOSE;

    private Component lastFocusOwner;

    /** Bound property name. */
    public final static String CONTENT_PANE_PROPERTY = "contentPane";
    /** Bound property name. */
    public final static String MENU_BAR_PROPERTY = "menuBar";
    /** Bound property name. */
    public final static String TITLE_PROPERTY = "title";
    /** Bound property name. */
    public final static String LAYERED_PANE_PROPERTY = "layeredPane";
    /** Bound property name. */
    public final static String ROOT_PANE_PROPERTY = "rootPane";
    /** Bound property name. */
    public final static String GLASS_PANE_PROPERTY = "glassPane";
    /** Bound property name. */
    public final static String FRAME_ICON_PROPERTY = "frameIcon";
    /** Bound property name. */
    public final static String CLOSABLE_PROPERTY = "closable";
    /** Bound property name. */
    public final static String RESIZABLE_PROPERTY = "resizable";
    /** Bound property name. */
    public final static String MAXIMIZABLE_PROPERTY = "maximizable";
    /** Bound property name. */
    public final static String ICONIFIABLE_PROPERTY = "iconifiable";
    /** Bound property name. */
    public final static String NAME_EDITABLE_PROPERTY = "nameEditable";
    /** Bound property name. */
    public final static String HELP_BUTTON_VISIBLE_PROPERTY = "helpButtonVisible";
    /** Bound property name. */
    public final static String INFO_BUTTON_VISIBLE_PROPERTY = "infoButtonVisible";
    /** Bound property name. */
    public final static String PLUG_BUTTON_VISIBLE_PROPERTY = "plugButtonVisible";

    /**
     * Constrained property name indicated that this frame has
     * selected (active) status.
     */
    public final static String IS_SELECTED_PROPERTY = "selected";
    /** Constrained property name indicating that the frame is closed. */
    public final static String IS_CLOSED_PROPERTY = "closed";
    /** Constrained property name indicating that the frame is maximized. */
    public final static String IS_MAXIMUM_PROPERTY = "maximum";
    /** Constrained property name indicating that the frame is iconified. */
    public final static String IS_ICON_PROPERTY = "icon";
    /** Bound property name. */
    public final static String IS_TITLE_PANEL_VISIBLE_PROPERTY = "titlepanelvisible";
    /** Bound property name. */
    public final static String IS_COMPONENT_ACTIVATED_ON_MOUSE_CLICK_PROPERTY = "componentactivation";

    private static DesktopManager sharedDesktopManager;
    public boolean disableMaximizeChange = false;
    private Border prevBorder = FRAME_NORMAL_BORDER;
    /** Determines whether the active ESlateInternalFrame will be highlighted in the microworld
     */
    private boolean activeStateDisplayed = true;
    /* Holds a reference to the parent DesktopPane, if the JDesktopPane that hosts the
     * Frame is of this class.
     */
    private DesktopPane desktopPane = null;
///    private GlassPaneDispatcher glassPaneDispatcher = null;
    /** The ESlateContainer in which the ESlateInternalFrame operates. George Vasiliou implemented
     *  the ESlateInternalFrame so that it is completely independent from its host, in our case the
     *  ESlateContainer. This was respected for some time during the evolution of the
     *  ESlateInternalFrame. However at the point when mechanism of the users rights on the
     *  microworld was revised, it was decided that for performance reasons, the ESlateInternalFrame
     *  should be hooked to the ESlateContainer. Two example:
     *  * Eventhough the removal of components from a microworld may not be allowed, a script can
     *    access the ESlateInternalFrame of a component and call it's setClosable() method with the
     *    parameter 'true'. In this case the component removal was indirectly permitted. To disable
     *    this, the setClosable() method has to check the 'componentRemovalAllowed' flag of the
     *    Microworld which is open in the ESlateContainer. This could be done without the
     *    ESlateInternalFrame knowing the ESlateContainer (through VetoableChangeEvents), however this
     *    proves to be a performance hit in situations like view switching in the microworld.
     *  * For every change to ESlateInternalFrame properties which have to do with properties of
     *    the Microworld which may e locked, the 'isLocked' flag of the microworld has to be checked.
     *    Doing this indirectly (through VetoableChangeEvents, would be a performance hit.
     *  Therefore from version 3 of the ESlateInternalFrame, the ESlateContainer has become a
     *  property of the ESlateContainer.
     */
    ESlateContainer container = null;
    /** This variable adjusts the sensitivity of the border of the ESlateInternalFrame to
     *  resize operations. The initial value is the insets of the ESlateInternalFrame.
     */
//    Insets borderActivationInsets = null;
    /** This variable holds the component hosted in the north side of the ESlateInternalFrame,
     *  only if this component has custom border. This component maybe the component of the
     *  ESlateInternalFrame or the component of the north SkinPane.
     */
    CustomBorder customNorthBorderComponent = null;
    /** This variable holds the component hosted in the south side of the ESlateInternalFrame,
     *  only if this component has custom border. This component maybe the component of the
     *  ESlateInternalFrame or the component of the south SkinPane.
     */
    CustomBorder customSouthBorderComponent = null;
    /** This variable holds the component hosted in the east side of the ESlateInternalFrame,
     *  only if this component has custom border. This component maybe the component of the
     *  ESlateInternalFrame or the component of the east SkinPane.
     */
    CustomBorder customEastBorderComponent = null;
    /** This variable holds the component hosted in the west side of the ESlateInternalFrame,
     *  only if this component has custom border. This component maybe the component of the
     *  ESlateInternalFrame or the component of the west SkinPane.
     */
    CustomBorder customWestBorderComponent = null;
    /** Indicates if the frame is currently being resized */
    private boolean resizing = false;
    /** When an iconified ESlateInternalFrame is restored it's state is not applied
     *  at once. It's state is saved in the following variable and will be restored
     *  as soon as the component becomes de-iconified.
     */
//    ESlateFieldMap stateToBeRestoredLater = null;
    StorageStructure stateToBeRestoredLater = null;
    /** The default locale-dependent name of the frame's handle.
     */
    static String frameHandleName;
    /**
     *  Utility class which runs an effect on the ESlateInternalFrame.
     */
    EffectRunner effectRunner = null;
    /**
     *
     */
    int clipType = ShapeType.RECTANGLE;
    Shape clipShape = null;
    Rectangle shapeBounds = null;
    /** Utility that manipulates listeners for the changes of the
     *  <code>clipShape</code> of the ESlateInternalFrame.
     */
    private ShapeChangedListenerSupport shapeChangedListenerSupport = new ShapeChangedListenerSupport(this);
    /** Determines whether paint() will do it's normal job, or if it will just
     *  call super.paint(), avoiding any clipping or effect.
     */
    private boolean simplePaint = false;

    long start = System.currentTimeMillis();

    /**
     * Creates a non-resizable, non-closable, non-maximizable,
     * non-iconifiable <code>ESlateInternalFrame</code>.
     */
    public ESlateInternalFrame() {
        this(null, false, false, false, false);
    }

    /**
     * Creates a non-resizable, non-closable, non-maximizable,
     * non-iconifiable <code>ESlateInternalFrame</code>.
     */
    public ESlateInternalFrame(ESlateContainer container) {
        this(container, false, false, false, false);
    }

    /**
     * Creates a non-closable, non-maximizable, non-iconifiable
     * <code>ESlateInternalFrame</code> with resizability specified.
     * @param resizable  if true, the frame can be resized
     */
    public ESlateInternalFrame(ESlateContainer container, boolean resizable) {
        this(container, resizable, false, false, false);
    }

    /**
     * Creates a non-maximizable, non-iconifiable <code>ESlateInternalFrame</code>
     * with resizability and closability specified.
     *
     * @param resizable  if true, the frame can be resized
     * @param closable   if true, the frame can be closed
     */
    public ESlateInternalFrame(ESlateContainer container, boolean resizable, boolean closable) {
        this(container, resizable, closable, false, false);
    }

    /**
     * Creates a non-iconifiable <code>ESlateInternalFrame</code>
     * with resizability, closability, and maximizability specified.
     *
     * @param resizable   if true, the frame can be resized
     * @param closable    if true, the frame can be closed
     * @param maximizable if true, the frame can be maximized
     */
    public ESlateInternalFrame(ESlateContainer container, boolean resizable, boolean closable,
                          boolean maximizable) {
        this(container, resizable, closable, maximizable, false);
    }

public static long constructorTimer = 0;
    /**
     * Creates an <code>ESlateInternalFrame</code>
     * with resizability, closability, maximizability, and iconifiability
     * specified.  All constructors defer to this one.
     *
     * @param resizable   if true, the frame can be resized
     * @param closable    if true, the frame can be closed
     * @param maximizable if true, the frame can be maximized
     * @param iconifiable if true, the frame can be iconified
     */
    public ESlateInternalFrame(ESlateContainer container, boolean resizable, boolean closable,
                                boolean maximizable, boolean iconifiable) {
        super();
        this.container = container;
        setLayout(new BorderLayout());
        setRootPane(createRootPane());
        this.resizable = resizable;
        this.closable = closable;
        this.maximizable = maximizable;
        isMaximum = false;
        this.iconifiable = iconifiable;
        isIcon = true;
		setVisible(false);
        setOpaque(false);
        setRootPaneCheckingEnabled(true);
        dragNorth=dragSouth=dragEast=dragWest=true;
        northVisible=southVisible=eastVisible=westVisible=false;
        helpButtonVisible=infoButtonVisible=plugButtonVisible=true;
        /* While a microworld is loading all the ESlateInternalFrames are created
         * with the title panels invisible. The visibility of the title panels is
         * adjusted when the state of the ESlateInternalFrames is restored.
         * If an ESlateInternalFrame is created while a microworld is not loading
         * it's title panel will be visible.
         */
        titlePanelVisible=!container.isMicroworldLoading(); // true;
        compActMouseClick=true;
        if (bundle == null) {
            bundle=ResourceBundle.getBundle("gr.cti.eslate.base.container.internalFrame.ESlateInternalFrameResourceBundle", Locale.getDefault());
            frameHandleName = bundle.getString("frame");
        }
        //Create the actions for maximizing etc. These actions can be added by the
        //user to any button.
        createActions();

        //Make the frame pane.
        framePane=new JPanel();
        framePane.setLayout(new BorderLayout());
        framePane.setOpaque(false);
        //Make a default user defined content pane.
        content=new JPanel();
        content.setLayout(new BorderLayout());
        ((JPanel) content).setOpaque(false);
        //Make the content pane, which holds "skin" components.
        contentPane=new JPanel();
        OverlapBorderLayout layout=new OverlapBorderLayout();
        contentPane.setLayout(layout);
        contentPane.setOpaque(false);
        contentPane.add(content,BorderLayout.CENTER);

        framePane.add(contentPane,BorderLayout.CENTER);
        rootPane.setContentPane(framePane); //getRootPane()

        //Initialize the border and the mouse listeners for resizing and dragging around
        setBorder(FRAME_NORMAL_BORDER);
        borderListener=new InternalFrameBorderListener();
        addMouseListener(borderListener);
        addMouseMotionListener(borderListener);
        dragAroundListener=new InternalFrameDragAroundListener();
        componentListener=new DesktopResizedHandler();
        propertyChangeListener=new InternalFramePropertyChangeListener();
        addPropertyChangeListener(propertyChangeListener);
        containerListener=new InternalFrameContentContainerListener();

	//Initialize the listener for the glass pane dispatcher.
///        glassPaneDispatcher = new GlassPaneDispatcher();
///        getGlassPane().setVisible(true);
        getGlassPane().setVisible(false);
///	getGlassPane().addMouseListener(glassPaneDispatcher);
///       	getGlassPane().addMouseMotionListener(glassPaneDispatcher);

        content.addContainerListener(containerListener);

        //Make the default title pane.
///        titlePane=new JPanel();
///        titlePane.setBackground(SystemColor.activeCaption);
//        titlePane.setPreferredSize(new Dimension(100,20));
///        setTitlePanel(titlePane);

constructorTimer = constructorTimer + (System.currentTimeMillis()-start);
//System.out.println("ET ESlateInternalFrame() end: " + (System.currentTimeMillis()-start));
/*        setNorth(createArea());
        north.setOpaque(true);

        setSouth(createArea());
        south.setOpaque(true);

        setWest(createArea());
        west.setOpaque(true);

        setEast(createArea());
        east.setOpaque(true);
*/
    }
    /**
     * Creates the property change listener for this frame.
     */
    protected PropertyChangeListener createPropertyChangeListener(){
        return new InternalFramePropertyChangeListener();
    }

    /**
     * Creates the actions for the frame buttons.
     */
    protected void createActions() {
        closeAction=new CloseAction();
        maximizeAction=new MaximizeAction();
        iconifyAction=new IconifyAction();
        restoreAction=new RestoreAction();
    }

    public void registerFrameAsPart(ESlateHandle componentHandle) {
        //Do the E-Slate stuff
        handle = getESlateHandle();
//        if (handle==null) {
//            handle=ESlate.registerPart(this);
/*            handle.addESlateListener(new ESlateAdapter() {
                public void handleDisposed(HandleDisposalEvent e) {
                    System.out.println("Disposed frame's handle");
                }
            });
        }
*/
        componentHandle.add(handle);
        customNorthBorderComponent = null;
        customSouthBorderComponent = null;
        customWestBorderComponent = null;
        customEastBorderComponent = null;
        if (CustomBorder.class.isAssignableFrom(componentHandle.getComponent().getClass())) {
            CustomBorder cb = (CustomBorder) componentHandle.getComponent();
            if (cb.hasCustomTopBorder())
                customNorthBorderComponent = cb;
            if (cb.hasCustomBottomBorder())
                customSouthBorderComponent = cb;
            if (cb.hasCustomEastBorder())
                customEastBorderComponent = cb;
            if (cb.hasCustomWestBorder())
                customWestBorderComponent = cb;
        }
        this.componentHandle = componentHandle;
        try {
            handle.setUniqueComponentName(frameHandleName);
            if (!container.isMicroworldLoading())
                setTitlePanel(new ESlateInternalFrameTitlePanel(this, titlePanelVisible));
        } catch(Exception e) {e.printStackTrace();}
/*        if (north != null)
            handle.add(((ESlatePart) north).getESlateHandle());
        if (south != null)
            handle.add(((ESlatePart) south).getESlateHandle());
        if (west != null)
            handle.add(((ESlatePart) west).getESlateHandle());
        if (east != null)
            handle.add(((ESlatePart) east).getESlateHandle());
*/
    }

    /**
     * Unregisters the frame from its hosting ESlatePart, if any exists.
     */
    public void unregisterFrameAsPart() {
//System.out.println("unregisterFrameAsPart() called componentHandle: " + componentHandle);
        if (componentHandle==null)
            return;
        componentHandle.remove(handle);
        handle.toBeDisposed(false, new BooleanWrapper(false));
        handle.dispose();
        handle = null;
//        host.getESlateHandle().remove(handle);
        componentHandle=null;
    }
    /**
     * Returns a handle to the title component of the frame.
     */
    public JComponent getTitlePanel() {
    	if (titlePane==null)
            setTitlePanel(new ESlateInternalFrameTitlePanel(this, titlePanelVisible));
        return titlePane;
    }
    /**
     * Sets the title component of the frame.
     */
    public void setTitlePanel(JComponent c) {
        if (titlePane!=null) {
            framePane.remove(titlePane);
            titlePane.removeMouseListener(dragAroundListener);
            titlePane.removeMouseMotionListener(dragAroundListener);
        }
        titlePane = c;
        titlePane.addMouseListener(dragAroundListener);
        titlePane.addMouseMotionListener(dragAroundListener);

        framePane.add(c,BorderLayout.NORTH);
    }

    /**
     * Returns a handle to the north component of the frame.
     */
    public JComponent getNorth() {
        return north;
    }
    /**
     * Sets the north component of the frame.
     */
    public void setNorth(JComponent c) {
        if (north == c) return;
        if (north!=null) {
            contentPane.remove(north);
            if (ESlatePart.class.isAssignableFrom(north.getClass())) {
                ESlateHandle h = ((ESlatePart) north).getESlateHandle();
                if (handle != null) handle.remove(h);
                if (h.getParentHandle() == null) {
                    h.toBeDisposed(false, new BooleanWrapper(false));
                    h.dispose();
                }
            }
            north.removeMouseListener(dragAroundListener);
            north.removeMouseMotionListener(dragAroundListener);
            customNorthBorderComponent = null;
        }
        north = c;
        contentPane.add(north,BorderLayout.NORTH);
        north.setVisible(northVisible);
        if (northVisible) {
            adjustCustomNorthBorderComponent();
            if (handle != null && ESlatePart.class.isAssignableFrom(c.getClass())) {
                ESlateHandle h = ((ESlatePart) c).getESlateHandle();
                try{
                    h.setUniqueComponentName(bundle.getString("north"));
                }catch (RenamingForbiddenException exc) {}
                handle.add(h);
            }
        }
        if (isDraggableFromNorth() && north != null) {
            north.addMouseListener(dragAroundListener);
            north.addMouseMotionListener(dragAroundListener);
        }
        if (north!=null && isNorthVisible())
            contentPane.add(c,BorderLayout.NORTH);
    }

    public JComponent getSouth() {
        return south;
    }

    public void setSouth(JComponent c) {
        if (south == c) return;
        if (south!=null) {
            contentPane.remove(south);
            if (ESlatePart.class.isAssignableFrom(south.getClass())) {
                ESlateHandle h = ((ESlatePart) south).getESlateHandle();
                if (handle != null) handle.remove(h);
                if (h.getParentHandle() == null) {
                    h.toBeDisposed(false, new BooleanWrapper(false));
                    h.dispose();
                }
            }
            south.removeMouseListener(dragAroundListener);
            south.removeMouseMotionListener(dragAroundListener);
            customSouthBorderComponent = null;
        }
        south = c;
        contentPane.add(south,BorderLayout.SOUTH);
        south.setVisible(southVisible);
        if (southVisible) {
            adjustCustomSouthBorderComponent();
            if (handle != null && ESlatePart.class.isAssignableFrom(c.getClass())) {
                ESlateHandle h = ((ESlatePart) c).getESlateHandle();
                try{
                    h.setUniqueComponentName(bundle.getString("south"));
                }catch (RenamingForbiddenException exc) {}
                handle.add(h);
            }
        }
        if (isDraggableFromSouth() && south != null) {
            south.addMouseListener(dragAroundListener);
            south.addMouseMotionListener(dragAroundListener);
        }
        if (south!=null && isSouthVisible())
            contentPane.add(c,BorderLayout.SOUTH);
    }

    public JComponent getWest() {
        return west;
    }

    public void setWest(JComponent c) {
        if (west == c) return;
        if (west!=null) {
            contentPane.remove(west);
            if (ESlatePart.class.isAssignableFrom(west.getClass())) {
                ESlateHandle h = ((ESlatePart) west).getESlateHandle();
                if (handle != null) handle.remove(h);
                if (h.getParentHandle() == null) {
                    h.toBeDisposed(false, new BooleanWrapper(false));
                    h.dispose();
                }
            }
            west.removeMouseListener(dragAroundListener);
            west.removeMouseMotionListener(dragAroundListener);
            customWestBorderComponent = null;
        }
        west = c;
        contentPane.add(west,BorderLayout.WEST);
        west.setVisible(westVisible);
        if (westVisible) {
            adjustCustomWestBorderComponent();
            if (handle != null && ESlatePart.class.isAssignableFrom(c.getClass())) {
                ESlateHandle h = ((ESlatePart) c).getESlateHandle();
                try{
                    h.setUniqueComponentName(bundle.getString("west"));
                }catch (RenamingForbiddenException exc) {}
                handle.add(h);
            }
        }
        if (isDraggableFromWest() && west != null) {
            west.addMouseListener(dragAroundListener);
            west.addMouseMotionListener(dragAroundListener);
        }
        if (west!=null && isWestVisible())
            contentPane.add(c,BorderLayout.WEST);
    }

    public JComponent getEast() {
        return east;
    }

    public void setEast(JComponent c) {
        if (east == c) return;
        if (east!=null) {
            contentPane.remove(east);
            if (ESlatePart.class.isAssignableFrom(east.getClass())) {
                ESlateHandle h = ((ESlatePart) east).getESlateHandle();
                if (handle != null) handle.remove(h);
                if (h.getParentHandle() == null) {
                    h.toBeDisposed(false, new BooleanWrapper(false));
                    h.dispose();
                }
            }
            east.removeMouseListener(dragAroundListener);
            east.removeMouseMotionListener(dragAroundListener);
            customEastBorderComponent = null;
        }
        east = c;
        contentPane.add(east,BorderLayout.EAST);
        east.setVisible(eastVisible);
//        east.setPreferredSize(new Dimension(10, 0));
        if (eastVisible) {
            adjustCustomEastBorderComponent();
            if (handle != null && ESlatePart.class.isAssignableFrom(c.getClass())) {
                ESlateHandle h = ((ESlatePart) c).getESlateHandle();
                try{
                    h.setUniqueComponentName(bundle.getString("east"));
                }catch (RenamingForbiddenException exc) {}
                handle.add(h);
            }
        }
        if (isDraggableFromEast() && east != null) {
            east.addMouseListener(dragAroundListener);
            east.addMouseMotionListener(dragAroundListener);
        }
        if (east!=null && isEastVisible())
            contentPane.add(c,BorderLayout.EAST);
    }

    /** Sets the 'customNorthBorderComponent' whenever the 'north' SkinPane changes or its
     *  visibility changes.
     */
    private void adjustCustomNorthBorderComponent() {
        Object northComp = (north == null)? componentHandle.getComponent(): north;
        customNorthBorderComponent = null;
        if (northComp != null) {
            if (CustomBorder.class.isAssignableFrom(northComp.getClass())) {
                CustomBorder cb = (CustomBorder) northComp;
//                if (cb.hasCustomTopBorder())
                    customNorthBorderComponent = cb;
            }
        }
    }

    /** Sets the 'customSouthBorderComponent' whenever the 'south' SkinPane changes or its
     *  visibility changes.
     */
    private void adjustCustomSouthBorderComponent() {
        Object southComp = (south == null)? componentHandle.getComponent(): south;
        customSouthBorderComponent = null;
        if (southComp != null) {
            if (CustomBorder.class.isAssignableFrom(southComp.getClass())) {
                CustomBorder cb = (CustomBorder) southComp;
//                if (cb.hasCustomBottomBorder())
                    customSouthBorderComponent = cb;
            }
        }
    }

    /** Sets the 'customWestBorderComponent' whenever the 'west' SkinPane changes or its
     *  visibility changes.
     */
    private void adjustCustomWestBorderComponent() {
        Object westComp = (west == null)? componentHandle.getComponent(): west;
        customWestBorderComponent = null;
        if (westComp != null) {
            if (CustomBorder.class.isAssignableFrom(westComp.getClass())) {
                CustomBorder cb = (CustomBorder) westComp;
//                if (cb.hasCustomWestBorder())
                    customWestBorderComponent = cb;
            }
        }
    }

    /** Sets the 'customEastBorderComponent' whenever the 'east' SkinPane changes or its
     *  visibility changes.
     */
    private void adjustCustomEastBorderComponent() {
        Object eastComp = (east == null)? componentHandle.getComponent(): east;
        customEastBorderComponent = null;
        if (eastComp != null) {
            if (CustomBorder.class.isAssignableFrom(eastComp.getClass())) {
                CustomBorder cb = (CustomBorder) eastComp;
//                if (cb.hasCustomEastBorder())
                    customEastBorderComponent = cb;
            }
        }
    }

    /**
     * Sets a property which defines if the frame is draggable from the north pane.
     */
    public void setDraggableFromNorth(boolean b) {
        if (dragNorth == b) return;
        dragNorth=b;
        if (north != null) {
            if (dragNorth) {
                north.addMouseListener(dragAroundListener);
                north.addMouseMotionListener(dragAroundListener);
            }else{
                north.removeMouseListener(dragAroundListener);
                north.removeMouseMotionListener(dragAroundListener);
            }
        }
    }
    /**
     * Returns true if the frame is draggable from the north pane.
     */
    public boolean isDraggableFromNorth() {
        return dragNorth;
    }
    /**
     * Sets a property which defines if the frame is draggable from the south pane.
     */
    public void setDraggableFromSouth(boolean b) {
        if (dragSouth == b) return;
        dragSouth=b;
        if (south != null) {
            if (dragSouth) {
                south.addMouseListener(dragAroundListener);
                south.addMouseMotionListener(dragAroundListener);
            }else{
                south.removeMouseListener(dragAroundListener);
                south.removeMouseMotionListener(dragAroundListener);
            }
        }
    }
    /**
     * Returns true if the frame is draggable from the south pane.
     */
    public boolean isDraggableFromSouth() {
        return dragSouth;
    }
    /**
     * Sets a property which defines if the frame is draggable from the west pane.
     */
    public void setDraggableFromWest(boolean b) {
        if (dragWest == b) return;
        dragWest=b;
        if (west != null) {
            if (dragWest) {
                west.addMouseListener(dragAroundListener);
                west.addMouseMotionListener(dragAroundListener);
            }else{
                west.removeMouseListener(dragAroundListener);
                west.removeMouseMotionListener(dragAroundListener);
            }
        }
    }
    /**
     * Returns true if the frame is draggable from the west pane.
     */
    public boolean isDraggableFromWest() {
        return dragWest;
    }
    /**
     * Sets a property which defines if the frame is draggable from the east pane.
     */
    public void setDraggableFromEast(boolean b) {
        if (dragEast == b) return;
        dragEast=b;
        if (east != null) {
            if (dragEast) {
                east.addMouseListener(dragAroundListener);
                east.addMouseMotionListener(dragAroundListener);
            }else{
                east.removeMouseListener(dragAroundListener);
                east.removeMouseMotionListener(dragAroundListener);
            }
        }
    }
    /**
     * Returns true if the frame is draggable from the east pane.
     */
    public boolean isDraggableFromEast() {
        return dragEast;
    }

    /**
     * Sets a property which defines if the north pane is visible.
     */
    public void setNorthVisible(boolean b) {
        if (b==northVisible)
            return;
        northVisible=b;
        if (northVisible && north == null)
            setNorth(createArea(SwingConstants.NORTH));
        if (b) {
            adjustCustomNorthBorderComponent();
            if (handle != null && ESlatePart.class.isAssignableFrom(north.getClass()))
                 ((ESlatePart) north).getESlateHandle().setVisible(true);
/*            if (handle != null && ESlatePart.class.isAssignableFrom(north.getClass())) {
                ESlateHandle h = ((ESlatePart) north).getESlateHandle();
                if (h.getParentHandle() != handle) {
                    try{
                        h.setUniqueComponentName(bundle.getString("north"));
                    }catch (RenamingForbiddenException exc) {}
                }
                handle.add(h);
            }
*/
        }else{
//            contentPane.remove(north);
            if (handle != null && ESlatePart.class.isAssignableFrom(north.getClass()))
                 ((ESlatePart) north).getESlateHandle().setVisible(false);
        }
        if (north != null) {
            north.setVisible(northVisible);
            contentPane.validate();
            contentPane.repaint();
        }
    }
    /**
     * Returns true if the north pane is visible.
     */
    public boolean isNorthVisible() {
        return northVisible;
    }
    /**
     * Sets a property which defines if the south pane is visible.
     */
    public void setSouthVisible(boolean b) {
        if (b==southVisible)
            return;
        southVisible=b;
        if (southVisible && south == null)
            setSouth(createArea(SwingConstants.SOUTH));
        if (b) {
            adjustCustomSouthBorderComponent();
            if (handle != null && ESlatePart.class.isAssignableFrom(south.getClass()))
                ((ESlatePart) south).getESlateHandle().setVisible(true);
/*            if (handle != null && ESlatePart.class.isAssignableFrom(south.getClass())) {
                ESlateHandle h = ((ESlatePart) south).getESlateHandle();
                if (h.getParentHandle() != handle) {
                    try{
                        h.setUniqueComponentName(bundle.getString("south"));
                    }catch (RenamingForbiddenException exc) {}
                    handle.add(h);
                }
            }
*/
        }else{
            if (handle != null && ESlatePart.class.isAssignableFrom(south.getClass()))
                ((ESlatePart) south).getESlateHandle().setVisible(false);
        }
        if (south != null) {
            south.setVisible(southVisible);
            contentPane.validate();
            contentPane.repaint();
        }
    }
    /**
     * Returns true if the south pane is visible.
     */
    public boolean isSouthVisible() {
        return southVisible;
    }
    /**
     * Sets a property which defines if the east pane is visible.
     */
    public void setEastVisible(boolean b) {
        if (b==eastVisible)
            return;
        eastVisible=b;
        if (eastVisible && east == null)
            setEast(createArea(SwingConstants.EAST));
        if (b) {
            adjustCustomEastBorderComponent();
            if (handle != null && ESlatePart.class.isAssignableFrom(east.getClass()))
                ((ESlatePart) east).getESlateHandle().setVisible(true);
/*            if (handle != null && ESlatePart.class.isAssignableFrom(east.getClass())) {
                ESlateHandle h = ((ESlatePart) east).getESlateHandle();
                if (h.getParentHandle() != handle) {
                    try{
                        h.setUniqueComponentName(bundle.getString("east"));
                    }catch (RenamingForbiddenException exc) {}
                    handle.add(h);
                }
            }
*/
        }else{
            if (handle != null && ESlatePart.class.isAssignableFrom(east.getClass()))
                ((ESlatePart) east).getESlateHandle().setVisible(false);
        }
        if (east != null) {
            east.setVisible(eastVisible);
            contentPane.validate();
            contentPane.repaint();
        }
    }
    /**
     * Returns true if the east pane is visible.
     */
    public boolean isEastVisible() {
        return eastVisible;
    }
    /**
     * Sets a property which defines if the west pane is visible.
     */
    public void setWestVisible(boolean b) {
        if (b==westVisible)
            return;
        westVisible=b;
        if (westVisible && west == null)
            setWest(createArea(SwingConstants.WEST));
        if (b) {
            adjustCustomWestBorderComponent();
            if (handle != null && ESlatePart.class.isAssignableFrom(west.getClass()))
                ((ESlatePart) west).getESlateHandle().setVisible(true);
/*            if (handle != null && ESlatePart.class.isAssignableFrom(west.getClass())) {
                ESlateHandle h = ((ESlatePart) west).getESlateHandle();
                if (h.getParentHandle() != handle) {
                    try{
                        h.setUniqueComponentName(bundle.getString("west"));
                    }catch (RenamingForbiddenException exc) {}
                    handle.add(h);
                }
            }
*/
        }else{
            if (handle != null && ESlatePart.class.isAssignableFrom(west.getClass()))
                ((ESlatePart) west).getESlateHandle().setVisible(false);
        }
        if (west != null) {
            west.setVisible(westVisible);
            contentPane.validate();
            contentPane.repaint();
        }
    }
    /**
     * Returns true if the west pane is visible.
     */
    public boolean isWestVisible() {
        return westVisible;
    }
    /**
     * Close button visibility.
     */
    public final void setCloseButtonVisible(boolean b) {
//error        container.verifySettingChangePriviledge();
        setCloseButtonVisibleInternal(b);
    }
    private final void setCloseButtonVisibleInternal(boolean b) {
        if (closeButtonVisible == b) return;
        closeButtonVisible=b;
        if (titlePane instanceof ESlateInternalFrameTitlePanel) {
            ESlateInternalFrameTitlePanel panel = (ESlateInternalFrameTitlePanel) titlePane;
            panel.adjustCloseButton();
//            panel.closeButton.getParent().remove(panel.closeButton);
//            panel.validate();
//            ((ESlateInternalFrameTitlePanel) titlePane).revalidateButtons();
        }
    }
    /**
     * Close button visibility.
     */
    public final boolean isCloseButtonVisible() {
        return closeButtonVisible;
    }

    /**
     * Minimize button visibility.
     */
    public final void setMinimizeButtonVisible(boolean b) {
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
//error        container.verifySettingChangePriviledge();
        setMinimizeButtonVisibleInternal(b);
    }

    private final void setMinimizeButtonVisibleInternal(boolean b) {
        if (minimizeButtonVisible == b) return;
        minimizeButtonVisible=b;
        if (titlePane instanceof ESlateInternalFrameTitlePanel) {
            ((ESlateInternalFrameTitlePanel) titlePane).adjustMinimizeButton();
//            ((ESlateInternalFrameTitlePanel) titlePane).revalidateButtons();
        }
    }
    /**
     * Minimize button visibility.
     */
    public final boolean isMinimizeButtonVisible() {
        return minimizeButtonVisible;
    }
    /**
     * Maximize button visibility.
     */
    public final void setMaximizeButtonVisible(boolean b) {
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
//error        container.verifySettingChangePriviledge();
        setMaximizeButtonVisibleInternal(b);
    }
    private final void setMaximizeButtonVisibleInternal(boolean b) {
        if (maximizeButtonVisible == b) return;
        maximizeButtonVisible=b;
        if (titlePane instanceof ESlateInternalFrameTitlePanel) {
            ((ESlateInternalFrameTitlePanel) titlePane).adjustMaximizeButton();
        }
    }
    /**
     * Maximize button visibility.
     */
    public final boolean isMaximizeButtonVisible() {
        return maximizeButtonVisible;
    }
    /**
     * Help button visibility.
     */
    public void setHelpButtonVisible(boolean b) {
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
//error        container.verifySettingChangePriviledge();
        setHelpButtonVisibleInternal(b);
    }
    private final void setHelpButtonVisibleInternal(boolean b) {
        if (helpButtonVisible == b) return;
        helpButtonVisible=b;
        if (componentHandle != null && componentHandle.isMenuPanelCreated())
            componentHandle.getMenuPanel().setHelpButtonVisible(helpButtonVisible);
        if (titlePane != null)
            titlePane.validate();
        firePropertyChange(HELP_BUTTON_VISIBLE_PROPERTY, !helpButtonVisible, helpButtonVisible);
    }
    /**
     * Bar button visibility.
     */
    public final boolean isHelpButtonVisible() {
        return helpButtonVisible;
    }
    /**
     * Bar button visibility.
     */
    public final void setInfoButtonVisible(boolean b) {
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
//error        container.verifySettingChangePriviledge();
        setInfoButtonVisibleInternal(b);
    }
    private final void setInfoButtonVisibleInternal(boolean b) {
        if (infoButtonVisible == b) return;
        infoButtonVisible=b;
        if (componentHandle != null && componentHandle.isMenuPanelCreated())
            componentHandle.getMenuPanel().setInfoButtonVisible(infoButtonVisible);
        if (titlePane != null)
            titlePane.validate();
        firePropertyChange(INFO_BUTTON_VISIBLE_PROPERTY, !infoButtonVisible, infoButtonVisible);
    }
    /**
     * Bar button visibility.
     */
    public final boolean isInfoButtonVisible() {
        return infoButtonVisible;
    }
    /**
     * Bar button visibility.
     */
    public final void setPlugButtonVisible(boolean b) {
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
//error        container.verifySettingChangePriviledge();
        setPlugButtonVisibleInternal(b);
    }
    private final void setPlugButtonVisibleInternal(boolean b) {
        if (plugButtonVisible == b) return;
        plugButtonVisible=b;
        if (componentHandle != null && componentHandle.isMenuPanelCreated())
            componentHandle.getMenuPanel().setPlugButtonVisible(plugButtonVisible);
        if (titlePane != null)
            titlePane.validate();
        firePropertyChange(PLUG_BUTTON_VISIBLE_PROPERTY, !plugButtonVisible, plugButtonVisible);
    }
    /**
     * Bar button visibility.
     */
    public final boolean isPlugButtonVisible() {
        return plugButtonVisible;
    }
    /**
     * Whether the maximum size of the component will be respected while resizing.
     * By default the maximum size the ESlateInternalFrame can get is DEFAULT_MAX_SIZE,
     * i.e. the component's maximum size is not respected.
     */
    public void setMaxSizeRespected(boolean b) {
        maxSizeRespected = b;
    }
    /**
     * Returns whether the component's maximum size is recpected while resizing the
     * ESlateInternalFrame.
     */
    public boolean isMaxSizeRespected() {
        return maxSizeRespected;
    }
    /**
     * Whether the minimum size of the component will be respected while resizing.
     * By default the minimum size the ESlateInternalFrame can get is DEFAULT_MIN_SIZE,
     * i.e. the component's minimum size is not respected.
     */
    public void setMinSizeRespected(boolean b) {
        minSizeRespected = b;
    }
    /**
     * Returns whether the component's minimum size is recpected while resizing the
     * ESlateInternalFrame.
     */
    public boolean isMinSizeRespected() {
        return minSizeRespected;
    }
    /**
     *  Returns the title of the frame
     */
    public String getTitle() {
        if (componentHandle != null)
            return componentHandle.getComponentName();
//        if (titlePane instanceof ESlateInternalFrameTitlePanel) {
//            return ((ESlateInternalFrameTitlePanel) titlePane).getTitle();
//        }
        return null;
    }
    /**
     *  Sets the color of the 'titlePane'.
     */
    public void setTitleColor(Color c) {
        titlePane.setBackground(c);
//        if (titlePane instanceof ESlateInternalFrameTitlePanel)
//            return ((ESlateInternalFrameTitlePanel) titlePane).getTitle();
    }
    /**
     *  Returns the background color of the 'titlePane'.
     */
    public Color getTitleColor() {
        return titlePane.getBackground();
    }
    /**
     *  Sets whether the name of the component in the E-Slate menubar is editable.
     */
    public void setComponentNameChangeableFromMenuBar(boolean enabled) {
        if (componentNameChangeableFromMenuBar == enabled) return;
        componentNameChangeableFromMenuBar = enabled;
        if (componentHandle == null) return;
//        if (componentHandle.getMenuPanel().isRenamingAllowed() == enabled)
//            return;
        if (componentHandle.isMenuPanelCreated())
            componentHandle.getMenuPanel().setRenamingAllowed(enabled);
        firePropertyChange(NAME_EDITABLE_PROPERTY, !enabled, enabled);
    }
    /**
     *  Reports whether the component's name is editable in the E-Slate menu panel.
     */
    public boolean isComponentNameChangeableFromMenuBar() {
        return componentNameChangeableFromMenuBar;
//        if (componentHandle == null) return false;
//        return componentHandle.getMenuPanel().isRenamingAllowed();
    }
    /* This overriden 'setBorder()' keeps track whether the FRAME_NORMAL_BORDER or
     * the FRAME_SELECTED_BORDER is used.
     */
    public void setBorder(Border b) {
        if (isMaximum) {
            super.setBorder(null);
            return;
        }
//Thread.currentThread().dumpStack();
        super.setBorder(b);
        if (b == null)
            return;
//        System.out.println("b: " + b + ", b.equals(FRAME_NORMAL_BORDER): " + b.equals(FRAME_NORMAL_BORDER));
        if (b.equals(FRAME_NORMAL_BORDER))
            prevBorder = FRAME_NORMAL_BORDER;
        else if (b.equals(getFrameSelectedBorder()))
            prevBorder = getFrameSelectedBorder();

    }

    /* Restores the last one of the 'FRAME_NORMAL_BORDER'/'FRAME_SELECTED_BORDER' used
     * on this ESlateInternalFrame.
     */
    public void restoreBorder() {
        if (getBorder() != prevBorder)
        setBorder(prevBorder);
//System.out.println("restoreBorder() called");
//Thread.currentThread().dumpStack();
//        if (prevBorder == FRAME_SELECTED_BORDER) {
//            System.out.print("Restoring orevBorder FRAME_SELECTED_BORDER");
//        }
    }
    /**
     * Returns the size of the frame, when not iconified or maximized.
     * If iconified or maximized, it returns the size the frame did
     * have before it was iconified or maximized, respectively.
     */
    public Dimension getRealSize() {
        if (!isIcon() && !isMaximum())
            return getSize();
        return restoreBounds.getSize();
    }
    /**
     * Called by the constructor to set up the <code>JRootPane</code>.
     * @return a new <code>JRootPane</code>
     * @see JRootPane
     */
    protected RootPane createRootPane() {
        return new RootPane();
    }

    /**
     * Notification from the <code>UIManager</code> that the L&F has changed.
     * Replaces the current UI object with the latest version from the
     * <code>UIManager</code>.
     *
     * @see JComponent#updateUI
     */
    public void updateUI() {
        invalidate();
    }

    /**
     * Returns whether calls to <code>add</code> and
     * <code>setLayout</code> cause an exception to be thrown.
     *
     * @return true if <code>add</code> and <code>setLayout</code>
     *         are checked
     * @see #addImpl
     * @see #setLayout
     * @see #setRootPaneCheckingEnabled
     */
    protected boolean isRootPaneCheckingEnabled() {
        return rootPaneCheckingEnabled;
    }


    /**
     * Determines whether calls to <code>add</code> and
     * <code>setLayout</code> cause an exception to be thrown.
     *
     * @param enabled  a boolean value, true if checking is to be
     *        enabled, which cause the exceptions to be thrown
     *
     * @see #addImpl
     * @see #setLayout
     * @see #isRootPaneCheckingEnabled
     */
    protected void setRootPaneCheckingEnabled(boolean enabled) {
        rootPaneCheckingEnabled = enabled;
    }

    /**
     * Creates a runtime exception with a message like:
     * <pre>
     * "Do not use ESlateInternalFrame.add() use ESlateInternalFrame.getContentPane().add() instead"
     * </pre>
     *
     * @param op  a <code>String</code> indicating the attempted operation;
     *		in the example above, the operation string is "add"
     */
    private Error createRootPaneException(String op) {
        String type = getClass().getName();
        return new Error(
            "Do not use " + type + "." + op + "() use "
                          + type + ".getContentPane()." + op + "() instead");
    }


    /**
     * By default, children may not be added directly to a this component,
     * they must be added to its <code>contentPane</code> instead.
     * For example:
     * <pre>
     * thisComponent.getContentPane().add(child)
     * </pre>
     * An attempt to add to directly to this component will cause an
     * runtime exception to be thrown.  Subclasses can disable this
     * behavior.
     *
     * @see #setRootPaneCheckingEnabled
     * @exception Error if called with <code>isRootPaneChecking</code> true
     */
    protected void addImpl(Component comp, Object constraints, int index)
    {
        if(isRootPaneCheckingEnabled()) {
            throw createRootPaneException("add");
        }
        else {
            super.addImpl(comp, constraints, index);
        }
    }

    /**
     * Removes the specified component from this container.
     * @param comp the component to be removed
     * @see #add
     */
    public void remove(Component comp) {
	int oldCount = getComponentCount();
	super.remove(comp);
	if (oldCount == getComponentCount()) {
	    // Client mistake, but we need to handle it to avoid a
	    // common object leak in client applications.
	    getContentPane().remove(comp);
	}
    }


    /**
     * By default the layout of this component may not be set,
     * the layout of its <code>contentPane</code> should be set instead.
     * For example:
     * <pre>
     * thisComponent.getContentPane().setLayout(new BorderLayout())
     * </pre>
     * An attempt to set the layout of this component will cause an
     * runtime exception to be thrown.  Subclasses can disable this
     * behavior.
     *
     * @param manager the <code>LayoutManager</code>
     * @see #setRootPaneCheckingEnabled
     * @exception Error if called with <code>isRootPaneChecking</code> true
     */
    public void setLayout(LayoutManager manager) {
        if(isRootPaneCheckingEnabled()) {
            throw createRootPaneException("setLayout");
        }
        else {
            super.setLayout(manager);
        }
    }

    /**
     * Returns the L&F object that renders this component.
     *
     * @return the <code>ESlateInternalFrameUI</code> object that renders
     *		this component
     */
    public ComponentUI getUI() {
        return (ComponentUI)ui;
    }

    /**
     * Sets the UI delegate for this <code>ESlateInternalFrame</code>.
     */
    public void setUI(ComponentUI ui) {
        boolean checkingEnabled = isRootPaneCheckingEnabled();
        try {
            setRootPaneCheckingEnabled(false);
            super.setUI(ui);
        }
        finally {
            setRootPaneCheckingEnabled(checkingEnabled);
        }
    }


//////////////////////////////////////////////////////////////////////////
/// Property Methods
//////////////////////////////////////////////////////////////////////////

    /**
     * Returns the current <code>JMenuBar</code> for this
     * <code>ESlateInternalFrame</code>, or <code>null</code>
     * if no menu bar has been set.
     *
     * @return  the <code>JMenuBar</code> used by this frame
     */
/*    public JMenuBar getJMenuBar() {
	    return getRootPane().getJMenuBar();
//JR
    }
*/
//    /**
//     * Sets the <code>JMenuBar</code> for this <code>ESlateInternalFrame</code>.
//     *
//     * @param m  the <code>JMenuBar</code> to use in this frame
//     */
/*    public void setJMenuBar(JMenuBar m){
        JMenuBar oldValue = getJMenuBar();
        getRootPane().setJMenuBar(m);
//JR
        firePropertyChange(MENU_BAR_PROPERTY, oldValue, m);
    }
*/
    // implements javax.swing.RootPaneContainer
    public Container getContentPane() {
        return content;
    }


    /**
     * Sets this <code>ESlateInternalFrame</code>'s content pane.
     *
     * @param c  the <code>contentPane</code> object for this frame
     *
     * @exception java.awt.IllegalComponentStateException (a runtime
     *           exception) if the content pane parameter is <code>null</code>
     * @see RootPaneContainer#getContentPane
     */
    public void setContentPane(Container c) {
        if (c instanceof ESlatePart)
            setContentPane(c, ((ESlatePart) c).getESlateHandle());
        else
            throw new RuntimeException("Cannot add Container " + c.getClass() + " to the ESlateInternalFrame. It is not an ESlatePart");
    }

    public void setContentPane(Container c, ESlateHandle handle) {
        Container oldValue = content;
        contentPane.remove(content);
        content.removeContainerListener(containerListener);
        //Unregister the frame because the contents changed.
        unregisterFrameAsPart();
        content=c;
        //If the content pane itself is an ESlatePart, register the frame on it.
        //If not, check if any of its top level components is an ESlatePart and register.
        //If none is, the frame remains unregistered.
//        if (content instanceof ESlatePart)
//            registerFrameAsPart((ESlatePart) content);
        registerFrameAsPart(handle);
//        else {
//            Component[] cs=content.getComponents();
//            for (int i=0;i<cs.length;i++)
//                if (cs[i] instanceof ESlatePart) {
//                    registerFrameAsPart((ESlatePart)cs[i]);
//                    break;
//                }
//        }
        content.addContainerListener(containerListener);
        contentPane.add(content,BorderLayout.CENTER);

        firePropertyChange(CONTENT_PANE_PROPERTY, oldValue, c);
    }

    public void setHostedComponent(Component component, ESlateHandle compoHandle) {
        if (compoHandle.getComponent() != component)
            throw new RuntimeException("The handle does not correspond to the provided component");
        content.removeAll();
        try{
            if (!isIcon) { //!isHeavy(component) || !isIcon) {
                content.add(component);
            }else{
                registerFrameAsPart(compoHandle);
                content.removeContainerListener(containerListener);
            }
        }catch (Exception exc) {}
        if (!ESlatePart.class.isAssignableFrom(component.getClass())) {
System.out.println("The component is not an ESlatePart");
            registerFrameAsPart(compoHandle);
        }
    }

    boolean isHeavy(Component comp) {
//        if (comp.getClass().getName().indexOf("tv") != -1)
//            return true;
        return false;
    }

    /**
     * Returns the <code>layeredPane</code> object for this frame.
     *
     * @return the <code>layeredPane</code> object
     * @see RootPaneContainer#setLayeredPane
     * @see RootPaneContainer#getLayeredPane
     */
    public JLayeredPane getLayeredPane() {
        return rootPane.getLayeredPane();
//JR
    }


    /**
     * Sets this <code>ESlateInternalFrame</code>'s <code>layeredPane</code>
     * property.
     * @param layered the <code>layeredPane</code> object for this frame
     *
     * @exception java.awt.IllegalComponentStateException (a runtime
     *           exception) if the layered pane parameter is <code>null</code>
     */
    public void setLayeredPane(JLayeredPane layered) {
        JLayeredPane oldValue = getLayeredPane();
        rootPane.setLayeredPane(layered);
//JR
        firePropertyChange(LAYERED_PANE_PROPERTY, oldValue, layered);
    }

    /**
     * Returns the <code>glassPane</code> object for this frame.
     *
     * @return the <code>glassPane</code> object
     */
    public Component getGlassPane() {
        return rootPane.getGlassPane();
//JR
    }


    /**
     * Sets this <code>ESlateInternalFrame</code>'s <code>glassPane</code>
     * property.
     * @param glass the <code>glassPane</code> object for this frame
     */
    public void setGlassPane(Component glass) {
        Component oldValue = getGlassPane();
        rootPane.setGlassPane(glass);
//JR
///        glassPaneDispatcher = new GlassPaneDispatcher();
///	glass.addMouseListener(glassPaneDispatcher);
///       	glass.addMouseMotionListener(glassPaneDispatcher);
        firePropertyChange(GLASS_PANE_PROPERTY, oldValue, glass);
    }

    /**
     * Returns the <code>rootPane</code> object for this frame.
     *
     * @return the <code>rootPane</code> property
     */
    public JRootPane getRootPane() {
        return null; //rootPane;
    }

    public RootPane getInternalFrameRootPane() {
        return rootPane;
    }


    /**
     * Sets the <code>rootPane</code> property.
     * This method is called by the constructor.
     *
     * @param root  the new <code>rootPane</code> object
     */
    protected void setRootPane(RootPane root) {
        if(rootPane != null) {
            remove(rootPane);
        }
//        JRootPane oldValue = getRootPane();
        rootPane = root;
        if(rootPane != null) {
            boolean checkingEnabled = isRootPaneCheckingEnabled();
            try {
                setRootPaneCheckingEnabled(false);
                add(rootPane, BorderLayout.CENTER);
            }
            finally {
                setRootPaneCheckingEnabled(checkingEnabled);
            }
        }
//        firePropertyChange(ROOT_PANE_PROPERTY, oldValue, root);
    }

    /**
     * Sets that this <code>ESlateInternalFrame</code> can be closed by
     * some user action.
     * @param b a boolean value, where true means the frame can be closed
     */
    public void setClosable(boolean b) {
        if (closable == b) return;
        closable = b;
        if (titlePane instanceof ESlateInternalFrameTitlePanel) {
            ESlateInternalFrameTitlePanel panel = (ESlateInternalFrameTitlePanel) titlePane;
            panel.adjustCloseButton();
/*            if (!closable)
                panel.closeButton.getParent().remove(panel.closeButton);
            else{
                int pos = 0;
                if (iconifiable)
                    pos++;
                if (maximizable)
                    pos++;
                panel.btns.add(panel.closeButton, pos);
            }
            panel.validate();
*/
        }
        firePropertyChange(CLOSABLE_PROPERTY, !closable, closable);
    }

    /**
     * Returns whether this <code>ESlateInternalFrame</code> be closed by
     * some user action.
     * @return true if the frame can be closed
     */
    public boolean isClosable() {
        return closable;
    }

    /**
     * Returns whether this <code>ESlateInternalFrame</code> is currently closed.
     * @return true if the frame is closed
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Calling this method with a value of <code>true</code> to close
     * the frame.
     *
     * @param b a boolean, where true means "close the frame"
     * @exception PropertyVetoException when the attempt to set the
     *            property is vetoed by the <code>ESlateInternalFrame</code>
     */
    public void setClosed(boolean b) throws PropertyVetoException {
        if (!closable) return;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (container != null && !container.isMicroworldClosing()) {
            Microworld microworld = container.getMicroworld();
            if (microworld != null && b)
                microworld.checkActionPriviledge(microworld.isComponentRemovalAllowed(), "componentRemovalAllowed");
        }

        if (isClosed == b) {
            return;
        }

        if (isModal) {
            isModal = false;
            if (!isIcon()) {
                if (desktopPane != null)
                    desktopPane.setModalFrameVisible(isModal, this);
            }
//            setModal(false);
            setSelected(false);
        }

        Boolean oldValue = isClosed ? Boolean.TRUE : Boolean.FALSE;
        Boolean newValue = b ? Boolean.TRUE : Boolean.FALSE;
//	if (b) {
//	  fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_CLOSING);
//	}
        fireVetoableChange(IS_CLOSED_PROPERTY, oldValue, newValue);
        isClosed = b;
        if (b)
            doDefaultCloseAction();
	firePropertyChange(IS_CLOSED_PROPERTY, oldValue, newValue);
        disposeListeners();
    }

    /**
     * Sets that the <code>ESlateInternalFrame</code> can be resized by some
     * user action.
     *
     * @param b  a boolean, where true means the frame can be resized
     */
    public void setResizable(boolean b) {
        if (resizable == b) return;
        Boolean oldValue = resizable ? Boolean.TRUE : Boolean.FALSE;
        Boolean newValue = b ? Boolean.TRUE : Boolean.FALSE;
        resizable = b;
        firePropertyChange(RESIZABLE_PROPERTY, oldValue, newValue);
    }

    /**
     * Returns whether the <code>ESlateInternalFrame</code> can be resized
     * by some user action.
     *
     * @return true if the frame can be resized
     */
    public boolean isResizable() {
        // don't allow resizing when maximized.
        return isMaximum ? false : resizable;
    }

    /**
     * Sets that the <code>ESlateInternalFrame</code> can be made an
     * icon by some user action.
     *
     * @param b  a boolean, where true means the frame can be iconified
     */
    public void setIconifiable(boolean b) {
        if (iconifiable == b) return;
        iconifiable = b;
        if (titlePane instanceof ESlateInternalFrameTitlePanel) {
            ESlateInternalFrameTitlePanel panel = (ESlateInternalFrameTitlePanel) titlePane;
            panel.adjustMinimizeButton();
/*            if (!iconifiable)
                panel.iconifyButton.getParent().remove(panel.iconifyButton);
            else
                panel.btns.add(panel.iconifyButton, 0);
            panel.validate();
*/
        }
        firePropertyChange(ICONIFIABLE_PROPERTY, !iconifiable, iconifiable);
    }

    /**
     * Returns whether the <code>ESlateInternalFrame</code> can be
     * iconified by some user action.
     *
     * @return true if the frame can be iconified
     */
    public boolean isIconifiable() {
        return iconifiable;
    }

    /**
     * Returns whether the <code>ESlateInternalFrame</code> is currently iconified.
     *
     * @return true if the frame is iconified
     */
    public boolean isIcon() {
        return isIcon;
    }

    /**
     * Iconizes and de-iconizes the frame.
     *
     * @param b a boolean, where true means to iconify the frame and
     *          false means to de-iconify it
     * @exception PropertyVetoException when the attempt to set the
     *            property is vetoed by the <code>JInternalFrame</code>
     */
    /* The state of an ESlateInternalFrame is not applied when the microworld is
     * loaded, if the frame is iconified. It is applied the first time the frame
     * is de-iconified. See applyState().
     */
    public void setIcon(boolean b) throws PropertyVetoException {
        if (isIcon == b) {
            return;
        }
        if (b && !iconifiable)
            return;

//System.out.println("b: " + b + ", stateToBeRestoredLater: " + stateToBeRestoredLater);
        /* If the frame is de-iconified and it's state has not been restored,
        * restore it now.
        */
        if (!b && stateToBeRestoredLater != null) {
            StorageStructure state = stateToBeRestoredLater;
            stateToBeRestoredLater = null;
//            applyState(state, false, false);
            applyRestOfState(state); //, false, false);
        }

        if (b && !isMaximum) {
            restoreBounds=getBounds();
        }

        //Changes the icon of the iconify button
        if (titlePane instanceof ESlateInternalFrameTitlePanel)
            ((ESlateInternalFrameTitlePanel) titlePane).setIcon(b);

        /* If an internal frame is being iconified before it has a
        parent, (e.g., client wants it to start iconic), create the
        parent if possible so that we can place the icon in its
        proper place on the desktop. I am not sure the call to
        validate() is necessary, since we are not going to display
        this frame yet */

        Container c = getParent();
        if (c != null && c.getPeer() == null) {
            c.addNotify();
            addNotify();
        }
        validate();

        Boolean oldValue = isIcon ? Boolean.TRUE : Boolean.FALSE;
        Boolean newValue = b ? Boolean.TRUE : Boolean.FALSE;
        fireVetoableChange(IS_ICON_PROPERTY, oldValue, newValue);
        isIcon = b;
//System.out.println("setIcon(" + isIcon + ") component: " + componentHandle + ", modal: " + isModal());
        if (isIcon) {
            isMaximum=false;
            //Changes the icon of the maximize button
            if (titlePane instanceof ESlateInternalFrameTitlePanel)
                ((ESlateInternalFrameTitlePanel) titlePane).setMaximum(false);
//            container.setComponentPanelVisible(true);
/*            if (componentHandle != null) {
Component component = (Component) componentHandle.getComponent();
if (component != null && isHeavy(component)) {
System.out.println("--------------- REMOVING component " + componentHandle);
content.remove(component);
}
}
*/
        }else{
            if (componentHandle != null) {
                Component component = (Component) componentHandle.getComponent();
                if (component.getParent() == null) {
                    content.add(component, BorderLayout.CENTER);
                    Integer lr = new Integer(getLayer());
// The following was part of the code till version 1.8.7. It's wrong cause it changes the layer of the ESlateInternalFrame
//                    if (lr.intValue() == 0)
//                        lr = gr.cti.eslate.base.container.LayerInfo.DEFAULT_LAYER_Z_ORDER;
                    container.getDesktopPane().add(this, lr);
                }
            }
/*            if (componentHandle != null && componentHandle.getComponent() != null && isHeavy((Component) componentHandle.getComponent())) {
System.out.println("---------------- ADDING component " + componentHandle);
content.add((Component) componentHandle.getComponent());
}
*/
        }
        /* Iconified modal frames are temporarily removed from the list of the modal frames of
        the DesktopPane. They are added again, when they become de-iconified
        */
        if (isModal) {
            if (desktopPane == null)
                getDesktopPane();
            desktopPane.setModalFrameVisible(!isIcon, this);
        }

        firePropertyChange(IS_ICON_PROPERTY, oldValue, newValue);
        if (b)
            fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_ICONIFIED);
        else
            fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_RESTORED); //DEICONIFIED);
    }

    /**
     * Sets that the <code>ESlateInternalFrame</code> can be maximized by
     * some user action.
     *
     * @param b a boolean  where true means the frame can be maximized
     */
    public void setMaximizable(boolean b) {
        if (maximizable == b) return;

        maximizable = b;
        if (titlePane instanceof ESlateInternalFrameTitlePanel) {
            ESlateInternalFrameTitlePanel panel = (ESlateInternalFrameTitlePanel) titlePane;
            panel.adjustMaximizeButton();
/*            if (!maximizable)
                panel.maximizeButton.getParent().remove(panel.maximizeButton);
            else{
                int pos = 0;
                if (iconifiable)
                    pos++;
                panel.btns.add(panel.maximizeButton, pos);
            }
            panel.validate();
*/
        }
        firePropertyChange(MAXIMIZABLE_PROPERTY, !maximizable, maximizable);
    }

    /**
     * Returns whether the <code>ESlateInternalFrame</code> can be maximized
     * by some user action.
     *
     * @return true if the frame can be maximized
     */
    public boolean isMaximizable() {
        return maximizable;
    }

    /**
     * Returns whether the <code>ESlateInternalFrame</code> is currently maximized.
     *
     * @return true if the frame is maximized
     */
    public boolean isMaximum() {
        return isMaximum;
    }

    /**
     * Maximizes and restores the frame.  A maximized frame is resized to
     * fully fit the <code>JDesktopPane</code> area associated with the
     * <code>ESlateInternalFrame</code>.
     * A restored frame's size is set to the <code>ESlateInternalFrame</code>'s
     * actual size.
     *
     * @param b  a boolean, where true maximizes the frame and false
     *           restores it
     * @exception PropertyVetoException when the attempt to set the
     *            property is vetoed by the <code>JInternalFrame</code>
     */
    public void setMaximum(boolean b) throws PropertyVetoException {
        if (disableMaximizeChange) return;
        if (isMaximum == b) {
            return;
        }
        if (b && !maximizable) return;

        if (b && !isIcon)
            restoreBounds=getBounds();

        //Changes the icon of the maximize button
        if (titlePane instanceof ESlateInternalFrameTitlePanel)
            ((ESlateInternalFrameTitlePanel) titlePane).setMaximum(b);

        Boolean oldValue = isMaximum ? Boolean.TRUE : Boolean.FALSE;
        Boolean newValue = b ? Boolean.TRUE : Boolean.FALSE;
        fireVetoableChange(IS_MAXIMUM_PROPERTY, oldValue, newValue);
	/* setting isMaximum above the event firing means that
	   property listeners that, for some reason, test it will
	   get it wrong... See, for example, getNormalBounds() */
        isMaximum = b;
        if (isMaximum) {
            /* Nullify the border, or else due to the EmptyBorder the component won't
             * reach the edges of the desktop.
             */
            setBorder(null);
        }else{
            if (isTitlePanelVisible())
                setBorder(FRAME_NORMAL_BORDER);
            else{
                if (activeStateDisplayed)
                    setBorder(getFrameSelectedBorder());
                else
                    setBorder(FRAME_NORMAL_BORDER);
//                System.out.println("3. Setting border to FRAME_SELECTED_BORDER");
            }
        }
        firePropertyChange(IS_MAXIMUM_PROPERTY, oldValue, newValue);
        if (isMaximum) {
            isIcon=false;
            //Changes the icon of the iconify button
            if (titlePane instanceof ESlateInternalFrameTitlePanel)
                ((ESlateInternalFrameTitlePanel) titlePane).setIcon(false);
        }

	if (b)
	  fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED);
	else
	  fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_RESTORED); //DEICONIFIED);
    }

    /**
     * Selects and deselects the ESlateInternalFrame.
     * An internalFrame normally draws its title bar differently if it is
     * the selected frame, which indicates to the user that this
     * internalFrame has the focus.
     *
     * @param selected  a boolean, where true means the frame is selected
     *                  (currently active) and false means it is not
     * @exception PropertyVetoException when the attempt to set the
     *            property is vetoed by the receiver.
     */
    public void setSelected(boolean selected) throws PropertyVetoException {
        if (container.isMicroworldLoading()) return;
        getDesktopPane();
//System.out.println("setSelected() desktopPane.isModalFrameVisible(): " + desktopPane.isModalFrameVisible());
//Thread.currentThread().dumpStack();
        if (selected && desktopPane != null && desktopPane.isModalFrameVisible() && !isModal)
            selected = false;
//System.out.println(componentHandle.getComponentName() + "  setSelected(" + selected + ") isSelected: " + isSelected);
//        if (selected && componentHandle.getComponentName().equals("map"))
//            Thread.currentThread().dumpStack();
        if ((isSelected == selected)) { // || (selected && !isShowing())) {
            return;
        }
//System.out.println("ESlateInternalFrame setSelected(): " + getTitle() + ", selected: " + selected + ", isSelected: " + isSelected);
        Boolean oldValue = isSelected ? Boolean.TRUE : Boolean.FALSE;
        Boolean newValue = selected ? Boolean.TRUE : Boolean.FALSE;
        fireVetoableChange(IS_SELECTED_PROPERTY, oldValue, newValue);

	/* We don't want to leave focus in the previously selected
	   frame, so we have to set it to *something* in case it
	   doesn't get set in some other way (as if a user clicked on
	   a component that doesn't request focus).  If this call is
	   happening because the user clicked on a component that will
	   want focus, then it will get transfered there later.

	   We test for parent.isShowing() above, because AWT throws a
	   NPE if you try to request focus on a lightweight before its
	   parent has been made visible */

	if(selected) {
	    getContentPane().requestFocus();
	}
        isSelected = selected;
//System.out.println("ESLATEINTERNALFRAME " + getTitle() + ", setSelected(): " + isSelected);
        firePropertyChange(IS_SELECTED_PROPERTY, oldValue, newValue);
	if (isSelected)
	  fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_ACTIVATED);
	else
	  fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_DEACTIVATED);

		if (titlePane instanceof ESlateInternalFrameTitlePanel)
			((ESlateInternalFrameTitlePanel) titlePane).setActive(isSelected);
		if (activeStateDisplayed) {
            if (!titlePanelVisible) {
                if (isSelected) {
                    setBorder(getFrameSelectedBorder());
//                System.out.println("4. Setting border to FRAME_SELECTED_BORDER");
//                    Thread.currentThread().dumpStack();
                }else
//                System.out.println("Setting border to FRAME_NORMAL_BORDER");
                    setBorder(FRAME_NORMAL_BORDER);
            }
        }

//        repaint();
        if (!selected) {
/**            if (compActMouseClick)
                getGlassPane().setVisible(true);
            else
                getGlassPane().setVisible(false);
**/
        }else{
            moveToFront();
///            getGlassPane().setVisible(false);
            /* If the activated frame is modal, then add it to the modal frames of the DesktopPane.
             */
            if (desktopPane != null && isModal)
                desktopPane.setModalFrameVisible(true, this);
        }
        if (borderListener!=null)
        	borderListener.mouseIsDown = false;
    }

    /**
     * Returns whether the ESlateInternalFrame is the currently "selected" or
     * active frame.
     *
     * @return true if the frame is currently selected (active)
     * @see #setSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /** Sets the modality of this frame. A modal frame is always displayed on top of all the
     *  others. While it is displayed, no other frame can be selected, until it is either
     *  closed or its modality is reset.
     */
    public void setModal(boolean modal) {
        if (isModal == modal) return;
        getDesktopPane();
//        if (desktopPane != null && (desktopPane.isModalFrameVisible() && !isModal)) return;
        isModal = modal;
//System.out.println("setModal(" + isModal + ") component: " + getTitle() + " isIcon: " + isIcon());
        if (modal) {
            try{
                setSelected(true);
            }catch (PropertyVetoException exc) {
                System.out.println("Inconsistency error in ESlateInternalFrame setModal(). A frame cannot be modal and inactive");
                return;
            }
        }
        /* If the frame whose modality changed is not iconified, then inform the DesktopPane to
           change its list of modal frames.
         */
        if (!isIcon()) {
            if (desktopPane != null)
                desktopPane.setModalFrameVisible(modal, this);
        }
        if (isModal) {
            _prevLayer = getLayer();
//System.out.println("Frame " + getTitle() + " recording _prevLayer: " + _prevLayer);
            setLayer(1000); //JLayeredPane.MODAL_LAYER.intValue());
//System.out.println("Frame " + getTitle() + " setting layer to: " + getLayer());
        }else{
//System.out.println("Frame " + getTitle() + " resetting layer to: " + _prevLayer);
            setLayer(_prevLayer);
            _prevLayer = -1;
//System.out.println("Frame " + getTitle() + " layer reset to: " + getLayer());
        }
    }

    public boolean isModal() {
        return isModal;
    }

    /**
     * Sets an image to be displayed in the titlebar of the frame (usually
     * in the top-left corner).
     *
     * @param icon the Icon to display in the title bar
     */
/*    public void setFrameIcon(Icon icon) {
        Icon oldIcon = frameIcon;
        if (icon!=null && !(icon instanceof NewRestorableImageIcon)) {
            BufferedImage bi=new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
            icon.paintIcon(this,bi.getGraphics(),0,0);
            icon=new NewRestorableImageIcon(bi);
        }
        frameIcon = icon;
        if (titlePane instanceof ESlateInternalFrameTitlePanel)
            ((ESlateInternalFrameTitlePanel) titlePane).setFrameIcon(frameIcon);
        firePropertyChange(FRAME_ICON_PROPERTY, oldIcon, icon);
    }
*/
    /**
     * Returns the image displayed in the title bar of the frame (usually
     * in the top-left corner).
     *
     * @return the Icon displayed in the title bar
     * @see #setFrameIcon
     */
/*    public Icon getFrameIcon()  {
        return frameIcon;
    }
*/
    /** Convenience method that moves this component to position 0 if its
      * parent is a JLayeredPane.
      */
    public void moveToFront() {
        if(getParent() != null && getParent() instanceof JLayeredPane) {
            JLayeredPane l =  (JLayeredPane)getParent();
            rootPane.componentChangingLayer = true;
            //l.moveToFront(this);
            rootPane.componentChangingLayer = false;
        }
    }

    /** Convenience method that moves this component to position -1 if its
      * parent is a JLayeredPane.
      */
    public void moveToBack() {
        if(getParent() != null && getParent() instanceof JLayeredPane) {
            JLayeredPane l =  (JLayeredPane)getParent();
            rootPane.componentChangingLayer = true;
            l.moveToBack(this);
            rootPane.componentChangingLayer = false;
        }
    }

    public void removeNotify() {
        if (!rootPane.componentChangingLayer) {
            super.removeNotify();
        }
    }

    /**
     * Convenience method for setting the layer attribute of this component.
     *
     * @param layer  an Integer object specifying this frame's desktop layer
     * @see JLayeredPane
     */
     //* @beaninfo
     //*     expert: true
     //*     description: Specifies what desktop layer is used.
     //*/
    public void setLayer(Integer layer) {
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (container != null && !container.isMicroworldLoading()) {
            Microworld microworld = container.getMicroworld();
            if (microworld != null)
                microworld.checkActionPriviledge(microworld.isMwdLayerMgmtAllowed(), "mwdLayerMgmtAllowed");
        }

        if(getParent() != null && getParent() instanceof JLayeredPane) {
            // Normally we want to do this, as it causes the LayeredPane
            // to draw properly.
            JLayeredPane p = (JLayeredPane)getParent();
            p.setLayer(this, layer.intValue(), p.getPosition(this));
        } else {
             // Try to do the right thing
             JLayeredPane.putLayer(this, layer.intValue());
             if(getParent() != null) {
                getParent().repaint();
			}
        }
        /* The following setVisible(false) is extra and is needed cause when the layer of
         * a heavyweight component changes, the component is drawn in the microworld, even if it
         * is iconified. Maybe this is corrected in Java 1.4. It's not sure that this works
         * correctly. This is needed only for heavyweights.
         */
        if (isIcon) {content.setVisible(false);content.setVisible(true);}
    }

    /**
     * Convenience method for setting the layer attribute of this component.
     * The method setLayer(Integer) should be used for layer values predefined
     * in JLayeredPane. When using setLayer(int), care must be taken not to
     * accidentally clash with those values.
     *
     * @param layer  an int specifying this frame's desktop layer
     * @see #setLayer(Integer)
     * @see JLayeredPane
     */
     //* @beaninfo
     //*     expert: true
     //*     description: Specifies what desktop layer is used.
     //*/
    public void setLayer(int layer) {
      this.setLayer(new Integer(layer));
    }

    /** Convenience method for getting the layer attribute of this component.
     *
     * @return  an Integer object specifying this frame's desktop layer
     * @see JLayeredPane
      */
    public int getLayer() {
        return JLayeredPane.getLayer(this);
    }

    /** Convenience method that searches the anscestor heirarchy for a
      * JDesktop instance.
      *
      * @return the JDesktopPane this frame belongs to, or null if none
      *         is found
      */
    public DesktopPane getDesktopPane() {
        if (desktopPane == null) {
            Container p;

            // Search upward for desktop
            p = getParent();
            while(p != null && !(p instanceof JDesktopPane))
                p = p.getParent();
            if (p != null && DesktopPane.class.isAssignableFrom(p.getClass()))
                desktopPane = (DesktopPane) p;
            return (DesktopPane)p;
        }
        return desktopPane;
    }

    /**
     * If the ESlateInternalFrame is not in maximized state, return
     * getBounds(); otherwise, return the bounds that the
     * ESlateInternalFrame would be restored to.
     *
     * @return the bounds of this frame when in the normal state
     * @since 1.3
     */
    public Rectangle getNormalBounds() {

      /* we used to test (!isMaximum) here, but since this
	 method is used by the property listener for the
	 IS_MAXIMUM_PROPERTY, it ended up getting the wrong
	 answer... Since normalBounds get set to null when the
	 frame is restored, this should work better */

      if (normalBounds != null) {
	return normalBounds;
      } else {
	return getBounds();
      }
    }

    /**
     * Sets the normal bounds for this frame, the bounds that
     * the frame would be restored to from its maximized state.
     * This method is intended for use only by desktop managers.
     *
     * @param r the bounds that the frame should be restored to
     * @since 1.3
     */
    public void setNormalBounds(Rectangle r) {
	normalBounds = r;
    }

    /**
     * This method directs the internal frame to restore focus to the
     * last subcomponent that had focus. This is used by the UI when
     * the user selected the frame, e.g., by clicking on the title bar.
     */

    public void restoreSubcomponentFocus() {
        this.getContentPane().requestFocus();
    }

    /*
     * Creates a new EventDispatchThread to dispatch events from. This
     * method returns when stopModal is invoked.
     */
    synchronized void startModal() {
	/* Since all input will be blocked until this dialog is dismissed,
	 * make sure its parent containers are visible first (this component
	 * is tested below).  This is necessary for JApplets, because
	 * because an applet normally isn't made visible until after its
	 * start() method returns -- if this method is called from start(),
	 * the applet will appear to hang while an invisible modal frame
	 * waits for input.
	 */
	if (isVisible() && !isShowing()) {
	    Container parent = this.getParent();
	    while (parent != null) {
		if (parent.isVisible() == false) {
		    parent.setVisible(true);
		}
		parent = parent.getParent();
	    }
	}

        try {
            if (SwingUtilities.isEventDispatchThread()) {
                EventQueue theQueue = getToolkit().getSystemEventQueue();
                while (isVisible()) {
                    // This is essentially the body of EventDispatchThread
                    AWTEvent event = theQueue.getNextEvent();
                    Object src = event.getSource();
                    // can't call theQueue.dispatchEvent, so I pasted its body here
                    if (event instanceof ActiveEvent) {
                        ((ActiveEvent) event).dispatch();
                    } else if (src instanceof Component) {
                        ((Component) src).dispatchEvent(event);
                    } else if (src instanceof MenuComponent) {
                        ((MenuComponent) src).dispatchEvent(event);
                    } else {
                        System.err.println("unable to dispatch event: " + event);
                    }
                }
            } else
                while (isVisible())
                    wait();
        } catch(InterruptedException e){}
    }

    /*
     * Stops the event dispatching loop created by a previous call to
     * <code>startModal</code>.
     */
    synchronized void stopModal() {
        notifyAll();
    }

    /**
     * Moves and resizes this component.  Unlike other components,
     * this implementation also forces re-layout, so that frame
     * decorations such as the title bar are always redisplayed.
     *
     * @param x  an int giving the component's new horizontal position
     *           measured in pixels from the left of its container
     * @param y  an int giving the component's new vertical position,
     *           measured in pixels from the bottom of its container
     * @param width  an int giving the component's new width in pixels
     * @param height an int giving the component's new height in pixels
     */
    public void reshape(int x, int y, int width, int height) {
        super.reshape(x, y, width, height);
//		if (!container.getRepaintManager().isActive()) return;
        validate();
        repaint();
    }

///////////////////////////
// Frame/Window equivalents
///////////////////////////

    /**
     * Adds the specified internal frame listener to receive internal frame events from
     * this internal frame.
     * @param l the internal frame listener
     */
    public void addESlateInternalFrameListener(ESlateInternalFrameListener l) {  // remind: sync ??
      listenerList.add(ESlateInternalFrameListener.class, l);
      // remind: needed?
      enableEvents(0);   // turn on the newEventsOnly flag in Component.
    }

    /**
     * Removes the specified internal frame listener so that it no longer
     * receives internal frame events from this internal frame.
     * @param l the internal frame listener
     */
    public void removeESlateInternalFrameListener(ESlateInternalFrameListener l) {  // remind: sync??
      listenerList.remove(ESlateInternalFrameListener.class, l);
    }

    // remind: name ok? all one method ok? need to be synchronized?
    protected void fireInternalFrameEvent(int id){
      Object[] listeners = listenerList.getListenerList();
      ESlateInternalFrameEvent e = null;
      for (int i = listeners.length -2; i >=0; i -= 2){
	if (listeners[i] == ESlateInternalFrameListener.class){
	  if (e == null){
	    e = new ESlateInternalFrameEvent(this, id);
	    //	    System.out.println("InternalFrameEvent: " + e.paramString());
	  }
	  switch(e.getID()) {
	  case ESlateInternalFrameEvent.INTERNAL_FRAME_OPENED:
	    ((ESlateInternalFrameListener)listeners[i+1]).internalFrameOpened(e);
	    break;
	  case ESlateInternalFrameEvent.INTERNAL_FRAME_CLOSING:
	    ((ESlateInternalFrameListener)listeners[i+1]).internalFrameClosing(e);
	    break;
	  case ESlateInternalFrameEvent.INTERNAL_FRAME_CLOSED:
	    ((ESlateInternalFrameListener)listeners[i+1]).internalFrameClosed(e);
	    break;
	  case ESlateInternalFrameEvent.INTERNAL_FRAME_ICONIFIED:
	    ((ESlateInternalFrameListener)listeners[i+1]).internalFrameIconified(e);
	    break;
	  case ESlateInternalFrameEvent.INTERNAL_FRAME_RESTORED: //DEICONIFIED:
	    ((ESlateInternalFrameListener)listeners[i+1]).internalFrameRestored(e); //Deiconified(e);
	    break;
	  case ESlateInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED:
	    ((ESlateInternalFrameListener)listeners[i+1]).internalFrameMaximized(e); //Deiconified(e);
	    break;
	  case ESlateInternalFrameEvent.INTERNAL_FRAME_ACTIVATED:
	    ((ESlateInternalFrameListener)listeners[i+1]).internalFrameActivated(e);
	    break;
	  case ESlateInternalFrameEvent.INTERNAL_FRAME_DEACTIVATED:
	    ((ESlateInternalFrameListener)listeners[i+1]).internalFrameDeactivated(e);
	    break;
	  default:
	    break;
	  }
	}
      }
      /* we could do it off the event, but at the moment, that's not how
	 I'm implementing it */
      //      if (id == InternalFrameEvent.INTERNAL_FRAME_CLOSING) {
      //	  doDefaultCloseAction();
      //      }
    }

    void doDefaultCloseAction() {
//        fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_CLOSING);
        switch(defaultCloseOperation) {
          case DO_NOTHING_ON_CLOSE:
	    break;
          case HIDE_ON_CLOSE:
            setVisible(false);
	    if (isSelected())
                try {
                    setSelected(false);
                } catch (PropertyVetoException pve) {}

	    /* should this activate the next frame? that's really
	       desktopmanager's policy... */
            break;
          case DISPOSE_ON_CLOSE:
              try {
		fireVetoableChange(IS_CLOSED_PROPERTY, Boolean.FALSE,
				   Boolean.TRUE);
		isClosed = true;
		firePropertyChange(IS_CLOSED_PROPERTY, Boolean.FALSE,
				   Boolean.TRUE);
		dispose();
	      } catch (PropertyVetoException pve) {}
              break;
          default:
              break;
        }
    }

    /**
     * Sets the operation which will happen by default when
     * the user initiates a "close" on this window.
     * The possible choices are:
     * <p>
     * <ul>
     * <li>DO_NOTHING_ON_CLOSE - do not do anything - require the
     * program to handle the operation in the windowClosing
     * method of a registered InternalFrameListener object.
     * <li>HIDE_ON_CLOSE - automatically hide the window after
     * invoking any registered InternalFrameListener objects
     * <li>DISPOSE_ON_CLOSE - automatically hide and dispose the
     * window after invoking any registered InternalFrameListener objects
     * </ul>
     * <p>
     * The value is set to DISPOSE_ON_CLOSE by default.
     * @see #getDefaultCloseOperation
     */
    public void setDefaultCloseOperation(int operation) {
        this.defaultCloseOperation = operation;
    }

   /**
    * Returns the default operation which occurs when the user
    * initiates a "close" on this window.
    * @see #setDefaultCloseOperation
    */
    public int getDefaultCloseOperation() {
        return defaultCloseOperation;
    }

    /**
     * Causes subcomponents of this JInternalFrame to be laid out at their
     * preferred size.
     * @see       java.awt.Window#pack
     */
    public void pack() {
        Container parent = getParent();
        if (parent != null && parent.getPeer() == null) {
            parent.addNotify();
            addNotify();
        }
        setSize(getPreferredSize());
        validate();
    }

    /**
     * Shows this internal frame, and brings it to the front.
     * <p>
     * If this window is not yet visible, <code>show</code>
     * makes it visible. If this window is already visible,
     * then this method brings it to the front.
     * @see       java.awt.Window#show
     * @see       java.awt.Window#toFront
     * @see       java.awt.Component#setVisible
     */
    public void show() {
        Container parent = getParent();
        if (parent != null && parent.getPeer() == null) {
            parent.addNotify();
            addNotify();
        }
        validate();

	// bug 4149505
	if (!opened) {
	  fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_OPENED);
	  opened = true;
	}

	toFront();
	if (!isVisible()) { super.show(); }

	if (isIcon) {
	  return;
	}

/*        if (!isSelected()) {
            try {
                setSelected(true);
            } catch (PropertyVetoException pve) {}
        }
*/
    }

    /**
     * Disposes of this internal frame. If the frame is not already
     * closed, a frame-closed event is posted.
     */
    public void dispose() {
        if (!closable) return;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (container != null && !container.isMicroworldClosing()) {
            Microworld microworld = container.getMicroworld();
            if (microworld != null)
                microworld.checkActionPriviledge(microworld.isComponentRemovalAllowed(), "componentRemovalAllowed");
        }

        if (isVisible()) {
            setVisible(false);
        }
        if (isSelected()) {
            try {
                setSelected(false);
            } catch (PropertyVetoException pve) {}
        }
        if (!isClosed) {
	  firePropertyChange(IS_CLOSED_PROPERTY, Boolean.FALSE, Boolean.TRUE);
	  isClosed = true;
	}
	fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_CLOSED);
    }

    /**
     * Brings this internal frame to the front.
     * Places this internal frame  at the top of the stacking order
     * and makes the corresponding adjustment to other visible windows.
     * @see       java.awt.Window#toFront
     * @see       #moveToFront
     */
    public void toFront() {
        moveToFront();
    }

/*    public boolean contains(int x, int y) {
//System.out.println("contains() of " + componentHandle);
        Dimension size = getSize();
        if (x>=size.width/2 && x<size.width && y>=0 && y<size.height)
            return true;
        return false;
    }
*/
    /**
     * Sends this internal frame to the back.
     * Places this internal frame  at the bottom of the stacking order
     * and makes the corresponding adjustment to other visible windows.
     * @see       java.awt.Window#toBack
     * @see       #moveToBack
     */
    public void toBack() {
        moveToBack();
    }

    /** Sets a effect to be played on the UI of the ESlateInternalFrame. The
     *  effect is played as soon as it is set.
     *  @param effectID One of
     *  <ul>
     *  <li> EffectRunner.NO_EFFECT Stops the current effect
     *  <li> EffectRunner.COMPOSITE_EFFECT. The component gradually fades in.
     *  <li> EffectRunner.CLIPPING_EFFECT. An ellipse, which starts from the center of
     *  the component and  grows to cover the whole component is displayed. Only
     *  the portion of the component's UI within the ellipse is displayed.
     *  <li> EffectRunner.INTERSECTION_EFFECT. A rectangle is drawn inside the component.
     *  Depending on the paremeters of the effect, the component may appear to
     *  start becoming visible from its center to its edges or vice versa.
     *  </ul>
     */
    public void setEffectID(int effectID) {
        if (effectRunner == null)
            effectRunner = new EffectRunner(this);
        effectRunner.setEffectID(effectID);
    }

    /** Returns the effect id of the current effect. If the effect is of an
     *  unknown to the EffectRunner type, EffectRunner.NO_EFFECT is returned.
     */
    public int getEffectID() {
        if (effectRunner == null)
            return EffectRunner.NO_EFFECT;
        return effectRunner.getEffectID();
    }

    /** Through this method an unknow effect can be set. Also a known effect
     *  can be initialized and configured, before it is applied to the
     *  ESlateInternaFrame.
     */
    public void setEffect(EffectInterface effect) {
        if (effectRunner == null)
            effectRunner = new EffectRunner(this);
        effectRunner.setEffect(effect);
    }

    public EffectInterface getEffect() {
        if (effectRunner == null)
            return null;
        return effectRunner.getEffect();
    }

    /**
     * Custom paint, which honors the current effect and the clip shape.
     * @param g The graphics class.
     */
    public void paint(Graphics g) {
        if (simplePaint) {
            super.paint(g);
        }else{
//			if (!container.getRepaintManager().isActive()) return;
            Graphics2D g2 = (Graphics2D)g;
            Shape clip = getClipShape();
            if (clip != null)
                g2.clip(clip);

            if (effectRunner != null && effectRunner.isEffectRunning())
                effectRunner.getEffect().realizeEffect(g);
//Thread.currentThread().dumpStack();
            super.paint(g);
        }
    }

    /**
     * Gets the warning string that is displayed with this window.
     * Since an internal frame is always secure (since it's fully
     * contained within a window which might need a warning string)
     * this method always returns null.
     * @return    null
     * @see       java.awt.Window#getWarningString
     */
    public final String getWarningString() {
        return null;
    }

    /**
     * Foo property for layout customization.
     */
    public void setFooLayoutCustomizer(ESlateInternalFrame b) {
    }
    /**
     * Foo property for layout customization.
     */
    public ESlateInternalFrame getFooLayoutCustomizer() {
        return this;
    }
    /**
     * Returns the OverlapBorderLayout of the frame that lays out the "skin" panels.
     */
    public OverlapBorderLayout getSkinLayout() {
        return (OverlapBorderLayout) contentPane.getLayout();
    }

    public void invalidate() {
        super.invalidate();
        if (contentPane!=null)
            contentPane.invalidate();
    }
    /** Returns the proper DesktopManager. Calls getDesktopPane() to
      * find the JDesktop component and returns the desktopManager from
      * it. If this fails, it will return a default DesktopManager that
      * should work in arbitrary parents.
      */
    protected DesktopManager getDesktopManager() {
	if(getDesktopPane() != null
	   && getDesktopPane().getDesktopManager() != null)
	    return getDesktopPane().getDesktopManager();
	if(sharedDesktopManager == null)
	  sharedDesktopManager = createDesktopManager();
	return sharedDesktopManager;
    }

    protected DesktopManager createDesktopManager(){
      return new DefaultDesktopManager();
    }

    /** Checks if any other visible frame in the DesktopPane is modal.
     */
    boolean otherVisibleModalFramesExist() {
        getDesktopPane();
//System.out.println("InternalFrameDragAroundListener desktopPane.isModalFrameVisible(): " + desktopPane.isModalFrameVisible());
        int visibleModalFrameCount = desktopPane.getVisibleModalFrameCount();
        if (isModal) visibleModalFrameCount--;
        if (desktopPane != null && visibleModalFrameCount > 0) { // desktopPane.isModalFrameVisible() && !isModal) {
            return true;
        }
        return false;
    }

    /** Sets the sensitivity of the resize border, i.e. the area on the component which when the
     *  cursor steps in, the resize border appears.
     */
/*    public void setBorderActivationInsets(Insets insets) {
        if (insets == null) return;
        borderActivationInsets = insets;
System.out.println("setBorderActivationInsets(): " + borderActivationInsets);
    }
*/
    /** Adjusts the sensitivity of the resize border.
     */
/*    public Insets getBorderActivationInsets() {
        if (borderActivationInsets == null)
            borderActivationInsets = getInsets();
        return borderActivationInsets;
    }
*/
    /** Adjusts the appearance of the resize borders at the edge of the ESlateInternalFrame and
     *  the cursors which have to do with the ability to resize. This method is called in the
     *  mouseMoved() of 3 MouseMotionListeners: the 'borderListener' added to the ESlateInternalFrame,
     *  the 'dragAroundListener' added to the SkinPanes and the 'titlePane' and the
     *  'glassPaneDispatcher' added to the GlassPane.
     *  The ESlateInternalFrame keeps track of the components which exist at its edges. If these
     *  components implement the 'CustomBorder' i/f, the the components themselves are ashked to
     *  specify which cursor and border the ESlateInternalFrame should use as the mouse moves on
     *  the surface of the component. At all times (whether the component supports custom border
     *  or not) this method shows the proper border, when the cursor is between the component hosted
     *  in the ESlateInternalFrame and the edges of the ESlateInternalFrame (this is the area of
     *  the invisible empty border, which is returned by getInsets()).
     */
    void adjustFrameBorderOnMouseMove(Component source, Point p) {
        if(!isResizable())
            return;
        Insets i = getInsets(); //borderActivationInsets;
//System.out.println("source: " + source.getClass() + ", p: " + p);
//System.out.println("p.getY(): " + p.y + ", getHeight(): " + getHeight() + ", getHeight() - i.bottom: " + (getHeight() - i.bottom));
        boolean borderSet = false;
        boolean hasCustomTopBorder =  (customNorthBorderComponent != null && customNorthBorderComponent.hasCustomTopBorder());
//System.out.println("hasCustomTopBorder: " + hasCustomTopBorder + ", customNorthBorderComponent: " + customNorthBorderComponent);
        boolean hasCustomBottomBorder =  (customSouthBorderComponent != null && customSouthBorderComponent.hasCustomBottomBorder());
//System.out.println("hasCustomBottomBorder: " + hasCustomBottomBorder + ", customSouthBorderComponent: " + customSouthBorderComponent);
        boolean hasCustomWestBorder =  (customWestBorderComponent != null && customWestBorderComponent.hasCustomWestBorder());
//System.out.println("hasCustomWestBorder: " + hasCustomWestBorder + ", customWestBorderComponent: " + customWestBorderComponent);
        boolean hasCustomEastBorder =  (customEastBorderComponent != null && customEastBorderComponent.hasCustomEastBorder());

        /* First check if the mouse is in the insets of the ESlateInternalFrame. The ability to resize
         * the frame is provided only in this case, so only then the cursor changes to indicate
         * ability to resize. Also the proper border is set here.
         */
        if(p.x <= i.left) {
            if(p.y < borderListener.resizeCornerSize + i.top) {
                setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
//System.out.println("1. setBorder() NORTH_WEST_BORDER");
                setBorder(ESlateInternalFrame.getNorthWestBorder());
                borderSet = true;
            }else if(p.y > getHeight() - borderListener.resizeCornerSize - i.bottom) {
                setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                setBorder(ESlateInternalFrame.getSouthWestBorder());
                borderSet = true;
            }else{
                setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                setBorder(ESlateInternalFrame.getWestBorder());
                borderSet = true;
            }
        } else if(p.x >= getWidth() - i.right) {
            if(p.y < borderListener.resizeCornerSize + i.top) {
                setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                setBorder(ESlateInternalFrame.getNorthEastBorder());
                borderSet = true;
            }else if(p.y > getHeight() - borderListener.resizeCornerSize - i.bottom) {
                setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                setBorder(ESlateInternalFrame.getSouthEastBorder());
                borderSet = true;
            }else{
                setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                setBorder(ESlateInternalFrame.getEastBorder());
                borderSet = true;
            }
        } else if(p.y <= i.top) {
            if(p.x < borderListener.resizeCornerSize + i.left) {
                setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
//System.out.println("2. setBorder() NORTH_WEST_BORDER");
                setBorder(ESlateInternalFrame.getNorthWestBorder());
                borderSet = true;
            }else if(p.x > getWidth() - borderListener.resizeCornerSize - i.right) {
                setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                setBorder(ESlateInternalFrame.getNorthEastBorder());
                borderSet = true;
            }else{
                setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                setBorder(ESlateInternalFrame.getNorthBorder());
                borderSet = true;
            }
        } else if(p.y >= getHeight() - i.bottom) {
            if(p.x < borderListener.resizeCornerSize + i.left) {
                setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                setBorder(ESlateInternalFrame.getSouthWestBorder());
                borderSet = true;
            }else if(p.x > getWidth() - borderListener.resizeCornerSize - i.right) {
                setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                setBorder(ESlateInternalFrame.getSouthEastBorder());
                borderSet = true;
            }else{
//System.out.println("Setting cursor to Cursor.S_RESIZE_CURSOR");
                setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                setBorder(ESlateInternalFrame.getSouthBorder());
                borderSet = true;
            }
        }

        /* If the border of the ESlateInternalFrame was not set above, then the cursor
         * is not in the insets area of the ESlateInternalFrame. This means that the
         * component cannot be resized from the current point and therefore the cursor
         * must be set to the default cursor.
         */
        if (!borderSet) {
//System.out.println("Setting cursor to Cursor.DEFAULT_CURSOR");
            setCursor(defaultCursor); //Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        /* If the border has not already been set (which means that the cursor is somewhere
         * in the component and not at the insets of the ESlateInternalFrame), then if we
         * ask the component (if it supports custom borders) to give us the realative location
         * of the point on its surface. Before we do that the point has to be converted to
         * the coordinate system of the component which occupies each edge (might be the
         * same component, i.e. the component of the ESlateInternalFrame). This conversion
         * is necessary only in the case that the point's coordinates are in the coordinate
         * space of the ESlateInternalFrame or the GlassPane, which is always the case (even
         * when the 'dragAroundListener''s mouseMoved() is called, in which case the source
         * is the SkinPane. Check the listener.
         */
        if (!borderSet) {
            if (hasCustomTopBorder) {
//                if (!SkinPane.class.isAssignableFrom(source.getClass()))
                Point p1 = SwingUtilities.convertPoint(source, p, (Component) customNorthBorderComponent);
                int relativeLoc = customNorthBorderComponent.getPointRelativeLocation(p1);
                if (relativeLoc == CustomBorder.NORTH) {
                    setBorder(ESlateInternalFrame.getNorthBorder());
                    borderSet = true;
                }else if (relativeLoc == CustomBorder.NORTH_EAST) {
                    setBorder(ESlateInternalFrame.getNorthEastBorder());
                    borderSet = true;
                }else if (relativeLoc == CustomBorder.NORTH_WEST) {
//System.out.println("3. setBorder() NORTH_WEST_BORDER");
                    setBorder(ESlateInternalFrame.getNorthWestBorder());
                    borderSet = true;
                }
            }
            if (!borderSet && hasCustomBottomBorder) {
//                if (!SkinPane.class.isAssignableFrom(source.getClass()))
                Point p1 = SwingUtilities.convertPoint(source, p, (Component) customSouthBorderComponent);
                int relativeLoc = customSouthBorderComponent.getPointRelativeLocation(p1);
                if (relativeLoc == CustomBorder.SOUTH) {
                    setBorder(ESlateInternalFrame.getSouthBorder());
                    borderSet = true;
                }else if (relativeLoc == CustomBorder.SOUTH_EAST) {
                    setBorder(ESlateInternalFrame.getSouthEastBorder());
                    borderSet = true;
                }else if (relativeLoc == CustomBorder.SOUTH_WEST) {
                    setBorder(ESlateInternalFrame.getSouthWestBorder());
                    borderSet = true;
                }
            }
            if (!borderSet && hasCustomWestBorder) {
//                if (!SkinPane.class.isAssignableFrom(source.getClass()))
//System.out.println("1.5 p: " + p);
                Point p1 = SwingUtilities.convertPoint(source, p, (Component) customWestBorderComponent);
//System.out.println("2. p: " + p + ", customWestBorderComponent: " + ((SkinPane) customWestBorderComponent).getContentPane().getComponent(0).getClass());
                int relativeLoc = customWestBorderComponent.getPointRelativeLocation(p1);
                if (relativeLoc == CustomBorder.WEST) {
                    setBorder(ESlateInternalFrame.getWestBorder());
                    borderSet = true;
                }else if (relativeLoc == CustomBorder.NORTH_WEST) {
//System.out.println("4. setBorder() NORTH_WEST_BORDER");
                    setBorder(ESlateInternalFrame.getNorthWestBorder());
                    borderSet = true;
                }else if (relativeLoc == CustomBorder.SOUTH_WEST) {
                    setBorder(ESlateInternalFrame.getSouthWestBorder());
                    borderSet = true;
                }
            }
            if (!borderSet && hasCustomEastBorder) {
//                if (!SkinPane.class.isAssignableFrom(source.getClass()))
//System.out.println("customEastBorderComponent: "+ customEastBorderComponent.getClass());
                Point p1 = SwingUtilities.convertPoint(source, p, (Component) customEastBorderComponent);
                int relativeLoc = customEastBorderComponent.getPointRelativeLocation(p1);
                if (relativeLoc == CustomBorder.EAST) {
                    setBorder(ESlateInternalFrame.getEastBorder());
                    borderSet = true;
                }else if (relativeLoc == CustomBorder.NORTH_EAST) {
                    setBorder(ESlateInternalFrame.getNorthEastBorder());
                    borderSet = true;
                }else if (relativeLoc == CustomBorder.SOUTH_EAST) {
                    setBorder(ESlateInternalFrame.getSouthEastBorder());
                    borderSet = true;
                }
            }
        }
//System.out.println("borderSet: " + borderSet);
        if (!borderSet) {
            restoreBorder();
        }
    }

    // ======= begin optimized frame dragging defence code ==============

    boolean isDragging = false;
    boolean danger = false;

    protected void paintComponent(Graphics g) {
      if (isDragging) {
	//	   System.out.println("ouch");
         danger = true;
      }

      super.paintComponent(g);
   }

    // ======= end optimized frame dragging defence code ==============

    /**
     * This listener handles dragging around inside the container area.
     */
    protected class InternalFrameDragAroundListener extends MouseInputAdapter implements SwingConstants {
	// _x & _y are the mousePressed location in absolute coordinate system
        int _x, _y;
	// __x & __y are the mousePressed location in source view's coordinate system
	int __x, __y;
        Rectangle startingBounds;

	public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) return;

            // Forward the event to any mouseListener added to the ESlateInternalFrameTitlePanel
            Object source = e.getSource();
            if (source instanceof ESlateInternalFrameTitlePanel) {
                ESlateInternalFrameTitlePanel panel = (ESlateInternalFrameTitlePanel) source;
                for (int i=0; i<panel.mouseListeners.size(); i++) {
                    ((MouseListener) panel.mouseListeners.get(i)).mouseClicked(e);
                }
            }
	}

        public void mouseReleased(MouseEvent e) {
            getDesktopManager().endDraggingFrame(ESlateInternalFrame.this);
            _x = 0;
            _y = 0;
            __x = 0;
            __y = 0;
            startingBounds = null;

            // Forward the event to any mouseListener added to the ESlateInternalFrameTitlePanel
            Object source = e.getSource();
            if (source instanceof ESlateInternalFrameTitlePanel) {
                ESlateInternalFrameTitlePanel panel = (ESlateInternalFrameTitlePanel) source;
                Component target = SwingUtilities.getDeepestComponentAt(panel, e.getX(), e.getY());
                if (target != null && !panel.isEmptySpace(target)) { //.target != null && target != panel) {
                    MouseEvent newEvent = SwingUtilities.convertMouseEvent(panel, e, target);
                    target.dispatchEvent(newEvent);
                }else{
                    for (int i=0; i<panel.mouseListeners.size(); i++) {
                        ((MouseListener) panel.mouseListeners.get(i)).mouseReleased(e);
                    }
                }
            }
        }

        public void mousePressed(MouseEvent e) {
            if (!ESlateInternalFrame.this.isModal && otherVisibleModalFramesExist()) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            Object source = e.getSource();
            Component target = null;
            boolean eventInEmptySpace = false;
            if (source instanceof ESlateInternalFrameTitlePanel) {
                ESlateInternalFrameTitlePanel panel = (ESlateInternalFrameTitlePanel) source;
                target = SwingUtilities.getDeepestComponentAt(panel, e.getX(), e.getY());
                if (target != null)
                    eventInEmptySpace = panel.isEmptySpace(target);
//            }
                if (eventInEmptySpace) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        /* Double-click maximizes the ESlateInternalFrame, excepti if ocuured
                         * on the ESlateInternalFrameTitlePanel's icon, in which case the
                         * component is closed.
                         */
                        if (e.getClickCount() > 1) {
                            if (target != null && target != ((ESlateInternalFrameTitlePanel) source).icon) {
                                if(isMaximizable()) {
                                    if(!isMaximum())
                                        try { setMaximum(true); } catch (PropertyVetoException e2) { }
                                    else
                                        try { setMaximum(false); } catch (PropertyVetoException e3) { }
                                }
                            }
                        }else{
                            Point p = SwingUtilities.convertPoint((Component)e.getSource(),
                                        e.getX(), e.getY(), null);
                            __x = e.getX();
                            __y = e.getY();
                            _x = p.x;
                            _y = p.y;
                            startingBounds = getBounds();
                        }
                    }
                }else{
//                    ESlateInternalFrameTitlePanel panel = (ESlateInternalFrameTitlePanel) source;
                    MouseEvent newEvent = SwingUtilities.convertMouseEvent(panel, e, target);
                    target.dispatchEvent(newEvent);
                }
            }else{
                Point p = SwingUtilities.convertPoint((Component)e.getSource(),
                            e.getX(), e.getY(), null);
                __x = e.getX();
                __y = e.getY();
                _x = p.x;
                _y = p.y;
                startingBounds = getBounds();
            }

//System.out.println("mousePressed() eventInEmptySpace: " + eventInEmptySpace);
//            if (eventInEmptySpace) {
/**            try{
                if (!ESlateInternalFrame.this.isSelected())
                    ESlateInternalFrame.this.setSelected(true);
            }catch (PropertyVetoException exc) {}
**/
        }
//        }

        public void mouseMoved(MouseEvent e) {
//System.out.println("Calling adjustFrameBorderOnMouseMove() from mouseMoved() of InternalFrameDragAroundListener resizing: " + resizing);
            if (resizing) return;
            Point p = SwingUtilities.convertPoint((Component)e.getSource(), e.getPoint(), ESlateInternalFrame.this);
            adjustFrameBorderOnMouseMove(ESlateInternalFrame.this, p);
//            ESlateInternalFrame.this.restoreBorder();
        }

        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) return;
	    if ( startingBounds == null ) {
                return;
//                mousePressed(e);
            }

            Point p;
	    int newX, newY, newW, newH;
            int deltaX;
            int deltaY;
	    Dimension min;
	    Dimension max;
            p = SwingUtilities.convertPoint((Component)e.getSource(),
                                        e.getX(), e.getY(), null);

            // Handle a MOVE
            if (isMaximum()) {
                return;  // don't allow moving of maximized frames.
            }
            Insets i = getInsets();
            int pWidth, pHeight;
            Dimension s = getParent().getSize();
            pWidth = s.width;
            pHeight = s.height;


            newX = startingBounds.x - (_x - p.x);
            newY = startingBounds.y - (_y - p.y);
            // Make sure we stay in-bounds
            if(newX + i.left <= -__x)
                newX = -__x - i.left;
            if(newY + i.top <= -__y)
                newY = -__y - i.top;
            if(newX + __x + i.right > pWidth)
                newX = pWidth - __x - i.right;
            if(newY + __y + i.bottom > pHeight)
                newY =  pHeight - __y - i.bottom;

            getDesktopManager().dragFrame(ESlateInternalFrame.this, newX, newY);
	}

        public void mouseExited(MouseEvent e) {
            if (resizing) return;
            // Forward the event to any mouseListener added to the ESlateInternalFrameTitlePanel
            Object source = e.getSource();
            if (source instanceof ESlateInternalFrameTitlePanel) {
                ESlateInternalFrameTitlePanel panel = (ESlateInternalFrameTitlePanel) source;
                for (int i=0; i<panel.mouseListeners.size(); i++) {
                    ((MouseListener) panel.mouseListeners.get(i)).mouseExited(e);
                }
            }
            restoreBorder();
        }

        public void mouseEntered(MouseEvent e) {
            // Forward the event to any mouseListener added to the ESlateInternalFrameTitlePanel
            Object source = e.getSource();
            if (source instanceof ESlateInternalFrameTitlePanel) {
                ESlateInternalFrameTitlePanel panel = (ESlateInternalFrameTitlePanel) source;
                for (int i=0; i<panel.mouseListeners.size(); i++) {
                    ((MouseListener) panel.mouseListeners.get(i)).mouseEntered(e);
                }
            }
        }
    };

    /**
     * This listener handles resizing from the border.
     */
    protected class InternalFrameBorderListener extends MouseInputAdapter implements SwingConstants {
	// _x & _y are the mousePressed location in absolute coordinate system
        int _x, _y;
	// __x & __y are the mousePressed location in source view's coordinate system
	int __x, __y;
        Rectangle startingBounds;
        int resizeDir;
        boolean mouseIsDown = false;

        protected final int RESIZE_NONE  = 0;

        int resizeCornerSize = 16;

	public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() > 1 && e.getSource() == getNorth()) {
		if(isIconifiable() && isIcon()) {
                    try { setIcon(false); } catch (PropertyVetoException e2) { }
		} else if(isMaximizable()) {
                    if(!isMaximum())
                        try { setMaximum(true); } catch (PropertyVetoException e2) { }
                    else
                        try { setMaximum(false); } catch (PropertyVetoException e3) { }
		}
            }
	}

        public void mouseReleased(MouseEvent e) {
	    if(resizeDir == RESIZE_NONE)
	        getDesktopManager().endDraggingFrame(ESlateInternalFrame.this);
	    else
	        getDesktopManager().endResizingFrame(ESlateInternalFrame.this);

            _x = 0;
            _y = 0;
            __x = 0;
            __y = 0;
            startingBounds = null;
            resizeDir = RESIZE_NONE;
            mouseIsDown = false;
            resizing = false;
        }

        public void mousePressed(MouseEvent e) {
            if (!isModal && otherVisibleModalFramesExist()) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            mouseIsDown = true;
            Point p = SwingUtilities.convertPoint((Component)e.getSource(),
                        e.getX(), e.getY(), null);
            __x = e.getX();
            __y = e.getY();
            _x = p.x;
            _y = p.y;
            startingBounds = getBounds();

/**            if (ESlateInternalFrame.this.isComponentActivatedOnMouseClick()) {
                if(!isSelected()) {
                    try { setSelected(true); } catch (PropertyVetoException e1) { }
                }
            }
**/
            if(!isResizable() || e.getSource() == getNorth()) {
                resizeDir = RESIZE_NONE;
		getDesktopManager().beginDraggingFrame(ESlateInternalFrame.this);
                return;
            }

            if(e.getSource() == ESlateInternalFrame.this) {
                Insets i = getInsets();
                if(e.getX() <= i.left) {
                    if(e.getY() < resizeCornerSize + i.top)
                        resizeDir = NORTH_WEST;
                    else if(e.getY() > getHeight()
					- resizeCornerSize - i.bottom)
                        resizeDir = SOUTH_WEST;
                    else
                        resizeDir = WEST;
                } else if(e.getX() >= getWidth() - i.right) {
                    if(e.getY() < resizeCornerSize + i.top)
                        resizeDir = NORTH_EAST;
                    else if(e.getY() > getHeight()
				- resizeCornerSize - i.bottom)
                        resizeDir = SOUTH_EAST;
                    else
                        resizeDir = EAST;
                } else if(e.getY() <= i.top) {
                    if(e.getX() < resizeCornerSize + i.left)
                        resizeDir = NORTH_WEST;
                    else if(e.getX() > getWidth()
				- resizeCornerSize - i.right)
                        resizeDir = NORTH_EAST;
                    else
                        resizeDir = NORTH;
                } else if(e.getY() >= getHeight() - i.bottom) {
                    if(e.getX() < resizeCornerSize + i.left)
                        resizeDir = SOUTH_WEST;
                    else if(e.getX() > getWidth()
				- resizeCornerSize - i.right)
                        resizeDir = SOUTH_EAST;
                    else
                        resizeDir = SOUTH;
                }
	        getDesktopManager().beginResizingFrame(ESlateInternalFrame.this, resizeDir);
                resizing = true;
                return;
            }
        }

        public void mouseDragged(MouseEvent e) {
	    if ( startingBounds == null ) {
	      // (STEVE) Yucky work around for bug ID 4106552
		 return;
	    }

            Point p;
	    int newX, newY, newW, newH;
            int deltaX;
            int deltaY;
	    Dimension min;
	    Dimension max;
            p = SwingUtilities.convertPoint((Component)e.getSource(),
                                        e.getX(), e.getY(), null);

            // Handle a MOVE
            if(e.getSource() == getNorth()) {
                if (isMaximum()) {
                    return;  // don't allow moving of maximized frames.
                }
		Insets i = getInsets();
		int pWidth, pHeight;
		Dimension s = getParent().getSize();
		pWidth = s.width;
		pHeight = s.height;


	        newX = startingBounds.x - (_x - p.x);
	        newY = startingBounds.y - (_y - p.y);
		// Make sure we stay in-bounds
		if(newX + i.left <= -__x)
		    newX = -__x - i.left;
		if(newY + i.top <= -__y)
		    newY = -__y - i.top;
		if(newX + __x + i.right > pWidth)
		    newX = pWidth - __x - i.right;
		if(newY + __y + i.bottom > pHeight)
		    newY =  pHeight - __y - i.bottom;

		getDesktopManager().dragFrame(ESlateInternalFrame.this, newX, newY);
                return;
            }

            if(!isResizable()) {
                return;
            }

            if (minSizeRespected)
      	        min = getMinimumSize();
            else
                min = DEFAULT_MIN_SIZE;
            if (maxSizeRespected)
    	        max = getMaximumSize();
            else
                max = DEFAULT_MAX_SIZE;
            deltaX = _x - p.x;
            deltaY = _y - p.y;

	    newX = getX();
	    newY = getY();
	    newW = getWidth();
	    newH = getHeight();

            switch(resizeDir) {
            case RESIZE_NONE:
                return;
            case NORTH:
		if(startingBounds.height + deltaY < min.height)
		    deltaY = -(startingBounds.height - min.height);
		else if(startingBounds.height + deltaY > max.height)
		    deltaY = (startingBounds.height - min.height);

		newX = startingBounds.x;
		newY = startingBounds.y - deltaY;
		newW = startingBounds.width;
		newH = startingBounds.height + deltaY;
                break;
            case NORTH_EAST:
		if(startingBounds.height + deltaY < min.height)
		    deltaY = -(startingBounds.height - min.height);
		else if(startingBounds.height + deltaY > max.height)
		    deltaY = (startingBounds.height - min.height);

		if(startingBounds.width - deltaX < min.width)
		    deltaX = (startingBounds.width - min.width);
		else if(startingBounds.width - deltaX > max.width)
		    deltaX = -(startingBounds.width - min.width);

		newX = startingBounds.x;
		newY = startingBounds.y - deltaY;
		newW = startingBounds.width - deltaX;
		newH = startingBounds.height + deltaY;
                break;
            case EAST:
		if(startingBounds.width - deltaX < min.width)
		    deltaX = (startingBounds.width - min.width);
		else if(startingBounds.width - deltaX > max.width)
		    deltaX = -(startingBounds.width - min.width);

		newW = startingBounds.width - deltaX;
		newH = startingBounds.height;
                break;
            case SOUTH_EAST:
		if(startingBounds.width - deltaX < min.width)
		    deltaX = (startingBounds.width - min.width);
		else if(startingBounds.width - deltaX > max.width)
		    deltaX = -(startingBounds.width - min.width);

		if(startingBounds.height - deltaY < min.height)
		    deltaY = (startingBounds.height - min.height);
		else if(startingBounds.height - deltaY > max.height)
		    deltaY = -(startingBounds.height - min.height);

		newW = startingBounds.width - deltaX;
		newH = startingBounds.height - deltaY;
                break;
            case SOUTH:
		if(startingBounds.height - deltaY < min.height)
		    deltaY = (startingBounds.height - min.height);
		else if(startingBounds.height - deltaY > max.height)
		    deltaY = -(startingBounds.height - min.height);

 		newW = startingBounds.width;
		newH = startingBounds.height - deltaY;
                break;
            case SOUTH_WEST:
		if(startingBounds.height - deltaY < min.height)
		    deltaY = (startingBounds.height - min.height);
		else if(startingBounds.height - deltaY > max.height)
		    deltaY = -(startingBounds.height - min.height);

		if(startingBounds.width + deltaX < min.width)
		    deltaX = -(startingBounds.width - min.width);
		else if(startingBounds.width + deltaX > max.width)
		    deltaX = (startingBounds.width - min.width);

		newX = startingBounds.x - deltaX;
		newY = startingBounds.y;
		newW = startingBounds.width + deltaX;
		newH = startingBounds.height - deltaY;
                break;
            case WEST:
		if(startingBounds.width + deltaX < min.width)
		    deltaX = -(startingBounds.width - min.width);
		else if(startingBounds.width + deltaX > max.width)
		    deltaX = (startingBounds.width - min.width);

		newX = startingBounds.x - deltaX;
		newY = startingBounds.y;
		newW = startingBounds.width + deltaX;
		newH = startingBounds.height;
                break;
            case NORTH_WEST:
		if(startingBounds.width + deltaX < min.width)
		    deltaX = -(startingBounds.width - min.width);
		else if(startingBounds.width + deltaX > max.width)
		    deltaX = (startingBounds.width - min.width);

		if(startingBounds.height + deltaY < min.height)
		    deltaY = -(startingBounds.height - min.height);
		else if(startingBounds.height + deltaY > max.height)
		    deltaY = (startingBounds.height - min.height);

		newX = startingBounds.x - deltaX;
		newY = startingBounds.y - deltaY;
		newW = startingBounds.width + deltaX;
		newH = startingBounds.height + deltaY;
                break;
            default:
                return;
            }

	    getDesktopManager().resizeFrame(ESlateInternalFrame.this, newX, newY, newW, newH);
	}

        public void mouseMoved(MouseEvent e)    {
            ESlateInternalFrame frame = (ESlateInternalFrame) e.getSource();

//System.out.println("Calling adjustFrameBorderOnMouseMove() from mouseMoved() of borderListener resizing: " + resizing);
            if (frame == ESlateInternalFrame.this && !resizing)
                adjustFrameBorderOnMouseMove((Component)e.getSource(), e.getPoint());
//            frame.restoreBorder();
//	    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

        public void mouseExited(MouseEvent e)    {
            if (!mouseIsDown && !resizing) {
                setCursor(defaultCursor); //Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                ESlateInternalFrame frame = (ESlateInternalFrame) e.getSource();
                frame.restoreBorder();
            }
	}
    };

///    protected class GlassPaneDispatcher implements MouseInputListener {
      /* This variable gets set, when the user mouse presses in the GlassPane inside
       * the area of the ESlateInternalFrame's title panel. It is used to lock the
       * target of the mouseDragged() events that might follow. This is needed because
       * if the user mouse presses and drags very fast, then the drag events may
       * be delivered to the component inside the ESlateInternalFrame instead of the
       * title pane. This disables the dragging of the ESlateInternalFrame, which
       * was the original intention of the user, since (s)he clicked on the title pane
       * area of the GlassPane.
       */
///      boolean mousePressedOnTitlePane = false;

      /**
       * When inactive, mouse events are forwarded as appropriate either to
       * the UI to activate the frame or to the underlying child component.
       *
       * In keeping with the MDI messaging model (which JInternalFrame
       * emulates), only the mousePressed event is forwarded to the UI
       * to activate the frame.  The mouseEntered, mouseMoved, and
       * MouseExited events are forwarded to the underlying child
       * component, using methods derived from those in Container.
       * The other mouse events are purposely ignored, since they have
       * no meaning to either the frame or its children when the frame
       * is inactive.
       */
/**      public void mousePressed(MouseEvent e) {
System.out.println("GlassPaneDispatcher() mousePressed()");
        if (!isModal && otherVisibleModalFramesExist()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        if (ESlateInternalFrame.this.getTitlePanel().getParent() != null && ESlateInternalFrame.this.getTitlePanel().contains(e.getX(), e.getY()))
            mousePressedOnTitlePane = true;
        else{
            mousePressedOnTitlePane = false;
            // Activate the frame, if its not already active and if its activated on click
            if (borderListener != null) {
                if (ESlateInternalFrame.this.isComponentActivatedOnMouseClick()) {
                    if(!isSelected()) {
                        try { setSelected(true); } catch (PropertyVetoException e1) { }
                    }
                }
            }
        }
	// fix for 4152560
       	forwardMouseEvent(e);
      }
**/
      /**
     * Forward the mouseEntered event to the underlying child container.
     * @see #mousePressed
     */
/**      public void mouseEntered(MouseEvent e) {
        forwardMouseEvent(e);
      }
**/
      /**
     * Forward the mouseMoved event to the underlying child container.
     * @see #mousePressed
     */
/**      public void mouseMoved(MouseEvent e) {
//System.out.println("GlassPane listener mouseMoved");
        adjustFrameBorderOnMouseMove((Component)e.getSource(), e.getPoint());
        forwardMouseEvent(e);
      }
**/
      /**
     * Forward the mouseExited event to the underlying child container.
     * @see #mousePressed
     */
/**      public void mouseExited(MouseEvent e) {
        if (!resizing)
            restoreBorder();
        forwardMouseEvent(e);
      }
**/
      /**
     * Ignore mouseClicked events.
     * @see #mousePressed
     */
/**      public void mouseClicked(MouseEvent e) {
        forwardMouseEvent(e);
      }
**/
      /**
     * Ignore mouseReleased events.
     * @see #mousePressed
     */
/**      public void mouseReleased(MouseEvent e) {
	//System.out.println("forward release");
	forwardMouseEvent(e);
        mouseEventTarget=null;
        mousePressedOnTitlePane = false;
      }
**/
    /**
     * Ignore mouseDragged events.
     * @see #mousePressed
     */
/**    public void mouseDragged(MouseEvent e) {
        if (!isModal && otherVisibleModalFramesExist())
            return;

        if (mousePressedOnTitlePane) {
            setMouseTarget(ESlateInternalFrame.this.getTitlePanel(),e);
            retargetMouseEvent(e.getID(),e);
            return;
        }
        //Without this, the check on mouseEventTarget, the dragging of the window
        //did not happen if one moved the mouse very quickly because the mouse target
        //was not the grabbed pane (the mouse went outside the grabbed pane).
        //This assures that if the mouse is pressed on a pane from witch the user may drag the frame around
        //and then dragged, the event target will always be the same pane, for all drag
        //events until the mouse is released.
        if (mouseEventTarget!=null) {
            setMouseTarget(mouseEventTarget,e);
            retargetMouseEvent(e.getID(),e);
        } else
            forwardMouseEvent(e);
    }
**/
    /**
     * Forward a mouse event to the current mouse target, setting it
     * if necessary.
     */
/**    private void forwardMouseEvent(MouseEvent e) {
//        Component target = findComponentAt(rootPane.getLayeredPane(), //getRootPane()
//                                           e.getX(), e.getY());
    Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), contentPane);
    Component target = SwingUtilities.getDeepestComponentAt(contentPane, p.x, p.y);
//    if (e.getID() != MouseEvent.MOUSE_MOVED && e.getID() != MouseEvent.MOUSE_EXITED && e.getID() != MouseEvent.MOUSE_ENTERED)
//        System.out.println("1. forwardMouseEvent(): " + target);
        if (target == null) {
              target = findComponentAt(rootPane.getLayeredPane(), e.getX(), e.getY());
//if (e.getID() != MouseEvent.MOUSE_MOVED && e.getID() != MouseEvent.MOUSE_EXITED && e.getID() != MouseEvent.MOUSE_ENTERED)
//              System.out.println("2. forwardMouseEvent(): " + target);
        }

        if (target != mouseEventTarget) {
	  setMouseTarget(target, e);
        }
        retargetMouseEvent(e.getID(), e);
      }

    private Component mouseEventTarget = null;
**/
    /**
     * Find the lightweight child component which corresponds to the
     * specified location.  This is similar to the new 1.2 API in
     * Container, but we need to run on 1.1.  The other changes are
     * due to Container.findComponentAt's use of package-private data.
     */
/**    private Component findComponentAt(Container c, int x, int y) {
        if (!c.contains(x, y)) {
	  return c;
        }
        int ncomponents = c.getComponentCount();
        Component component[] = c.getComponents();
        for (int i = 0 ; i < ncomponents ; i++) {
	  Component comp = component[i];
	  Point loc = comp.getLocation();
	  if ((comp != null) && (comp.contains(x - loc.x, y - loc.y)) &&
	      (comp.getPeer() instanceof java.awt.peer.LightweightPeer) &&
	      (comp.isVisible() == true)) {
	    // found a component that intersects the point, see if there
	    // is a deeper possibility only if it is not one of the drag areas
            if (comp==titlePane || comp==north || comp==south || comp==east || comp==west)
                return comp;
            else if (comp instanceof Container) {
	      Container child = (Container) comp;
	      Point childLoc = child.getLocation();
	      Component deeper = findComponentAt(child,
						 x - childLoc.x, y - childLoc.y);
	      if (deeper != null) {
		return deeper;
	      }
	    } else {
	      return comp;
	    }
	  }
        }
        return c;
      }
**/
      /*
     * Set the child component to which events are forwarded, and
     * synthesize the appropriate mouseEntered and mouseExited events.
     */
/**    private void setMouseTarget(Component target, MouseEvent e) {
        if (mouseEventTarget != null) {
	  retargetMouseEvent(MouseEvent.MOUSE_EXITED, e);
        }
        mouseEventTarget = target;
        if (mouseEventTarget != null) {
	  retargetMouseEvent(MouseEvent.MOUSE_ENTERED, e);
        }
    }
**/
    /**
     * Dispatch an event clone, retargeted for the current mouse target.
     */
/**    void retargetMouseEvent(int id, MouseEvent e) {
        // fix for bug #4202966 -- hania
        // When retargetting a mouse event, we need to translate
        // the event's coordinates relative to the target.
        Point p = SwingUtilities.convertPoint((Component) e.getSource(), //getLayeredPane(),
                                              e.getX(), e.getY(),
                                              mouseEventTarget);
//if (e.getID() != MouseEvent.MOUSE_MOVED && e.getID() != MouseEvent.MOUSE_EXITED && e.getID() != MouseEvent.MOUSE_ENTERED)
//    System.out.println("retargeted: " + mouseEventTarget + ", p: " + p);
        MouseEvent retargeted = new MouseEvent(mouseEventTarget,
                                               id,
                                               e.getWhen(),
                                               e.getModifiers(),
                                               p.x,
                                               p.y,
                                               e.getClickCount(),
                                               e.isPopupTrigger());
//        if (id == MouseEvent.MOUSE_PRESSED)
//System.out.println("retargetMouseEvent() MOUSE_PRESSED to mouseEventTarget: " + mouseEventTarget.getClass() + ", point: " + retargeted.getPoint());
//        else if (id == MouseEvent.MOUSE_RELEASED)
//System.out.println("retargetMouseEvent() MOUSE_RELEASED to mouseEventTarget: " + mouseEventTarget.getClass() + ", point: " + retargeted.getPoint());
//        else
//System.out.println("retargetMouseEvent() id: " + id + " MOUSE_PRESSED: " + MouseEvent.MOUSE_PRESSED + " MOUSE_RELEASED: " + MouseEvent.MOUSE_RELEASED + ", to mouseEventTarget: " + mouseEventTarget.getClass() + ", point: " + retargeted.getPoint());
//System.out.println("mouseEventTarget: " + mouseEventTarget.getClass());
        mouseEventTarget.dispatchEvent(retargeted);
      }
    }
**/
    protected class InternalFrameContentContainerListener implements ContainerListener {
        public void componentAdded(ContainerEvent e) {
            //If the frame is already hosted by an ESlatePart, ignore the addition.
            if (componentHandle!=null)
                return;
            //Otherwise, register.
            if (e.getChild() instanceof ESlatePart) {
                ESlateHandle h = ((ESlatePart) e.getChild()).getESlateHandle();
                registerFrameAsPart(h);
            }else{
                /* Normally this exception should be thrown, so that no-one can add
                 * a component that is not an ESlatePart to an ESlateInternalFrame.
                 * However, for speed reasons we comment it out for the moment.
                 */
//                throw new RuntimeException("Cannot add Container " + e.getChild().getClass() + " to the ESlateInternalFrame. It is not an ESlatePart");
            }
        }
        //If the frame's host is removed from its contents, unregister the frame.
        public void componentRemoved(ContainerEvent e) {
//System.out.println("componentRemoved called");
            if (e.getChild().equals(componentHandle.getComponent())) {
                unregisterFrameAsPart();
//System.out.println("Removing containerListener");
                content.removeContainerListener(containerListener);
                containerListener = null;
            }
        }
    }

    /**
     * This action should be fired when the close button is pressed.
     */
    class CloseAction extends AbstractAction {
        public CloseAction() {
	    super(CLOSE_CMD);
        }

        public void actionPerformed(ActionEvent e) {
	    if (isClosable()) {
                if ((ESlateOptionPane.showConfirmDialog(ESlateInternalFrame.this, bundle.getString("FrameMsg1") + componentHandle.getComponentName() + bundle.getString("FrameMsg3"),
                   bundle.getString("FrameMsg2"),
                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.YES_OPTION)
                    fireInternalFrameEvent(ESlateInternalFrameEvent.INTERNAL_FRAME_CLOSING);
//		doDefaultCloseAction();
	    }
	}
    }

    /**
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    class MaximizeAction extends AbstractAction {
        public MaximizeAction() {
	    super(MAXIMIZE_CMD);
        }

        public void actionPerformed(ActionEvent e) {
	    if (isMaximizable()) {
	        if (!isMaximum()) {
		    try {
                        setMaximum(true);
                    } catch (PropertyVetoException e5) {}
		} else {
		    try {
		        setMaximum(false);
		    } catch (PropertyVetoException e6) {}
		}
	    }
	}
    }

    /**
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    class IconifyAction extends AbstractAction {
        public IconifyAction() {
	    super(ICONIFY_CMD);
        }

        public void actionPerformed(ActionEvent e) {
	    if (isIconifiable()) {
	        if (!isIcon()) {
		    try {
                        setIcon(true);
                    } catch (PropertyVetoException e1) {}
	        } else {
		    try {
                        setIcon(false);
                    } catch (PropertyVetoException e1) {}
	        }
	    }
	}
    }

    /**
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    class RestoreAction extends AbstractAction {
        public RestoreAction() {
	    super(RESTORE_CMD);
        }

        public void actionPerformed(ActionEvent e) {
	    if (isMaximizable() && isMaximum()) {
	        try {
                    setMaximum(false);
                } catch (PropertyVetoException e4) {}
	    } else if (isIconifiable() && isIcon()) {
    	        try {
                    setIcon(false);
                } catch (PropertyVetoException e4) {}
	    }
	}
    }

    /**
     * The property change listener of the frame.
     */
    public class InternalFramePropertyChangeListener implements PropertyChangeListener {
        private boolean componentListenerAdded = false;
        /** Detects changes in state from the JInternalFrame and handles actions.*/
        public void propertyChange(PropertyChangeEvent evt) {
            String prop = (String)evt.getPropertyName();
            ESlateInternalFrame f = (ESlateInternalFrame)evt.getSource();
            Object newValue = evt.getNewValue();
            Object oldValue = evt.getOldValue();
            // ASSERT(frame == f) - This should always be true

            if (ESlateInternalFrame.ROOT_PANE_PROPERTY.equals(prop)) {
                if (oldValue != null)
                    remove((Component)oldValue);
                if (newValue != null)
                    add((Component)newValue);
            /// Handle the action events from the Frame
            } else if (ESlateInternalFrame.IS_CLOSED_PROPERTY.equals(prop)) {
                if (newValue == Boolean.TRUE){
                  if ((getParent() != null) && componentListenerAdded) {
                    getParent().removeComponentListener(componentListener);
                  }
                  try { setClosed(true); } catch(PropertyVetoException exc) { }
                }
            } else if (ESlateInternalFrame.IS_MAXIMUM_PROPERTY.equals(prop)) {
                if (newValue == Boolean.TRUE)
                  {
                  try { setMaximum(true); } catch(PropertyVetoException exc) { }
                  }
                else
                  {
                  try { setMaximum(false); } catch(PropertyVetoException exc) { }
                  }
            } else if (ESlateInternalFrame.IS_ICON_PROPERTY.equals(prop)) {
                if (newValue == Boolean.TRUE) {
                  try { setIcon(true); } catch(PropertyVetoException exc) { }
                }
                else{
                  try { setIcon(false); } catch(PropertyVetoException exc) { }
                }
            } else if (ESlateInternalFrame.IS_SELECTED_PROPERTY.equals(prop)) {
                Component glassPane = f.getGlassPane();
                if (newValue == Boolean.TRUE && oldValue == Boolean.FALSE) {
                  try { setSelected(true); } catch(PropertyVetoException exc) { }
                    //	glassPane.removeMouseListener(glassPaneDispatcher);
                    //	glassPane.removeMouseMotionListener(glassPaneDispatcher);
//                    glassPane.setVisible(false);
                } else if (newValue == Boolean.FALSE && oldValue == Boolean.TRUE) {
                  try { setSelected(false); } catch(PropertyVetoException exc) { }
                    //	glassPane.addMouseListener(glassPaneDispatcher);
                    //	glassPane.addMouseMotionListener(glassPaneDispatcher);
//                    glassPane.setVisible(true);
                }
            } else if (prop.equals("ancestor")) {
                if ((getParent() != null) && !componentListenerAdded ) {
                    f.getParent().addComponentListener(componentListener);
                    componentListenerAdded = true;
                    parentBounds = f.getParent().getBounds();
                }
            }
        }
    }


    protected class DesktopResizedHandler extends ComponentAdapter {
      /**
       * Invoked when a JInternalFrame's parent's size changes.
       */
      public void componentResized(ComponentEvent e) {
	//
	// Get the JInternalFrame's parent container size
	//
        Rectangle parentNewBounds = ((Component) e.getSource()).getBounds();

        //
        // Resize the internal frame if it is maximized and relocate
        // the associated icon as well.
        //
        if ( isMaximum() ) {
            setBounds(0, 0, parentNewBounds.width, parentNewBounds.height);
        }

	//
	// Update the new parent bounds for next resize.
	//
	if ( !parentBounds.equals(parentNewBounds) ) {
	  parentBounds = parentNewBounds;
	}
	//
	// Validate the component tree for this container.
	//
	validate();
      }
    }


    //
    // DesktopItemInterface methods.
    //

    public void setDesktopItemLocation(Point p) {
        setLocation(p.x,p.y);
    }
    public void setDesktopItemLocation(int x, int y) {
        setLocation(x,y);
    }
    public Point getDesktopItemLocation() {
        return getLocation();
    }
    public Point getDesktopItemLocation(Point p) {
        return getLocation(p);
    }
    /* Returns the location the frame should be at, when restored.
     */
    public Point getDesktopItemRestoreLocation() {
        return restoreBounds.getLocation();
    }
    /* Returns the size the frame should have, when restored.
     */
    public Dimension getDesktopItemRestoreSize() {
        return restoreBounds.getSize();
    }
    public Rectangle getDesktopItemBounds() {
        return getBounds();
    }
    public Rectangle getDesktopItemBounds(Rectangle bounds) {
        return getBounds(bounds);
    }
    public void setDesktopItemResizable(boolean resizable) {
        setResizable(resizable);
    }
    public boolean isDesktopItemResizable() {
        return isResizable();
    }
    public void setDesktopItemBounds(Rectangle bounds) {
        setBounds(bounds);
    }
    public void setDesktopItemBounds(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }
    public void setActive(boolean b) throws PropertyVetoException {
        setSelected(b);
    }

    public boolean isActive() {
        return isSelected();
    }
    public boolean displaysESlateMenuBar() {
        return true;
    }
    public boolean usesGlassPane() {
        return true;
    }
    public Dimension getDesktopItemSize() {
        return getSize();
    }
    public Dimension getDesktopItemSize(Dimension d) {
        return getSize(d);
    }
    public void setDesktopItemSize(Dimension size) {
        setSize(size);
    }
    public void setDesktopItemSize(int width, int height) {
        setSize(width, height);
    }
    public void setTitlePanelVisible(boolean visible) {
        boolean oldValue=titlePanelVisible;
        if (visible==oldValue)
            return;

        Rectangle frameBounds = getBounds();
        if (!isMaximum) {
            if (visible) {
                frameBounds.y = frameBounds.y - ESlateInternalFrameTitlePanel.PANEL_HEIGHT;
                frameBounds.height = frameBounds.height + ESlateInternalFrameTitlePanel.PANEL_HEIGHT;
            }else{
                frameBounds.y = frameBounds.y + ESlateInternalFrameTitlePanel.PANEL_HEIGHT;
                frameBounds.height = frameBounds.height - ESlateInternalFrameTitlePanel.PANEL_HEIGHT;
            }
            setBounds(frameBounds);
        }
        adjustTitlePaneVisibility(visible);
    }

    /* This method does the actual switching of the visibility of the title pane.
     * It is provided separately, because it is used when restoring views, where the
     * bounds of the frame are recorded irrespectively of the visibility of the
     * 'titlePane', so they have to be re-applied without the switching of the
     * visibility of the 'titlePane' affecting them.
     */
    private void adjustTitlePaneVisibility(boolean visible) {
///        if (titlePane == null) return;
//System.out.println("adjustTitlePaneVisibility() " + componentHandle.getComponentName() + ", visible: " + visible);
        if (!visible) {
            if (titlePane != null)
                framePane.remove(titlePane);
        }else{
            if (titlePane == null)
                setTitlePanel(new ESlateInternalFrameTitlePanel(this, true));
            ((ESlateInternalFrameTitlePanel) titlePane).createUI();
            if (componentHandle != null && componentHandle.isMenuPanelCreated()) {
                componentHandle.getMenuPanel().setPlugButtonVisible(plugButtonVisible);
                componentHandle.getMenuPanel().setHelpButtonVisible(helpButtonVisible);
                componentHandle.getMenuPanel().setInfoButtonVisible(infoButtonVisible);
            }
            framePane.add(titlePane,BorderLayout.NORTH);
        }

        if (titlePane != null) {
            framePane.validate();
            framePane.repaint();
        }
        titlePanelVisible=visible;
        firePropertyChange(IS_TITLE_PANEL_VISIBLE_PROPERTY, !visible, visible);
        if (!isMaximum && !isIcon && isSelected && activeStateDisplayed) {
            if (visible)
                setBorder(FRAME_NORMAL_BORDER);
            else{
                setBorder(getFrameSelectedBorder());
//                System.out.println("1. Setting border to FRAME_SELECTED_BORDER");
            }
        }
    }

    public boolean isTitlePanelVisible() {
        if (titlePane == null) return false;
        return titlePanelVisible;
    }

    public void setComponentActivatedOnMouseClick(boolean b) {
        boolean oldValue=compActMouseClick;
        if (b==compActMouseClick)
            return;
//System.out.println("ESlateINternalFrame: " + getTitle() + ", setComponentActivatedOnMouseClick() b: " + b + ", compActMouseClick: " + compActMouseClick);
        compActMouseClick=b;
///        getGlassPane().setVisible(compActMouseClick);
        firePropertyChange(IS_COMPONENT_ACTIVATED_ON_MOUSE_CLICK_PROPERTY, oldValue, b);
    }

    public boolean isComponentActivatedOnMouseClick() {
        return compActMouseClick;
    }

    /** Determines whether the ESlateInternalFrame will be highlighted (using borders)
     *  when it is active, i.e. selected.
     */
    public void setActiveStateDisplayed(boolean display) {
        if (activeStateDisplayed == display) return;
        activeStateDisplayed = display;
        if (isSelected) {
            if (titlePane instanceof ESlateInternalFrameTitlePanel)
                ((ESlateInternalFrameTitlePanel) titlePane).setActive(display);
            if (!titlePanelVisible) {
                if (display) {
                    setBorder(getFrameSelectedBorder());
//                System.out.println("2. Setting border to FRAME_SELECTED_BORDER");
                }else
                    setBorder(FRAME_NORMAL_BORDER);
            }
        }
    }

    /** Reports whether the activation state of the ESlateInternalFrame is indicated
     *  visually.
     */
    public boolean isActiveStateDisplayed() {
        return activeStateDisplayed;
    }

    //
    // ESlatePart interface methods.
    //
    public ESlateHandle getESlateHandle() {
        if (handle==null)
            handle=ESlate.registerPart(this);
        return handle;
    }
    /**
     * Externalization input.
     */
    public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
//        ESlateFieldMap fm=(ESlateFieldMap) in.readObject();
        StorageStructure fm=(StorageStructure) in.readObject();
        applyState(fm, true);
    }
    /**
     * Externalization output.
     */
    public void writeExternal(ObjectOutput out) throws IOException {
//        ESlateFieldMap fm = recordState(true);
        StorageStructure fm = recordState(true);
        out.writeObject(fm);
//        System.out.println("ESlateInternalFrame writeExternal() size: " + gr.cti.eslate.base.container.ESlateContainerUtils.getFieldMapContentLength(fm));
    }

    /* 'recordSkinState' determines if the ESlateInternalFame's skin will be stored.
     * This is provided for performance reasons. Restoring a view whose frame's skin
     * state has been persisted is a lot slower that restoring a view whose frames'
     * skins have not been persisted. Whether the ESlateInternalFrame Skin state will
     * persist or not is depending on two rules:
     * 1. When a microworld is saved the Skin state of the frames always is persisted.
     * 2. There is a microworld flag through which the user sepecifies if the frames'
     *    skins should persist on a per view basis. This flag is adjusted through the
     *    microworld settings dialog.
     * If an ESlateInternalFrame's state hasn't been restored yet (the component was
     * iconified when the microworld started), then recordStare() will return the state
     * the component should have (stateToBeRestoredLater) instead of its state. The
     * 'stateToBeRestoredLater' will be updated with the info about the ESlateInternalFrame's
     * bounds, icon state and 'restoreBounds', which is always restored.
     */
//    public ESlateFieldMap recordState(boolean recordSkinState) {
    public StorageStructure recordState(boolean recordSkinState) {
        /* If the original state of the component has not been applied yet (because
         * it was and still is iconified, when applyState() was called), the
         * recordState() should return that state. See applyState().
         */
//        if (componentHandle.getComponentName().equals("JBrowser"))
//if (componentHandle.getComponentName().equals("attributeTree"))
//    System.out.println("recordState() stateToBeRestoredLater: " + stateToBeRestoredLater);
        if (stateToBeRestoredLater != null) {
            StorageStructure state = (StorageStructure) stateToBeRestoredLater.clone();
            state.put("Bounds",getDesktopItemBounds());
//            if (componentHandle.getComponentName().equals("attributeTree"))
//                System.out.println("attributeTree recordState() bounds: " + getDesktopItemBounds());
            if (restoreBounds != null) {
                state.put("Restore Location", getDesktopItemRestoreLocation());
                state.put("Restore Size", getDesktopItemRestoreSize());
            }
//            if (componentHandle.getComponentName().equals("Browser"))
//                System.out.println("Browser recordtate()  restoreBounds: " + restoreBounds);
            state.put("Icon",isIcon());
			if (isModal) {
				state.put("Layer", new Integer(_prevLayer));
			}else{
				state.put("Layer", getLayer());
			}
            if (!recordSkinState) {
                state.put("north", "notStored");
                state.put("south", "notStored");
                state.put("west", "notStored");
                state.put("east", "notStored");
            }
            return state;
        }

        ESlateFieldMap2 fm=new ESlateFieldMap2(FRAME_STATE_VERSION);
        fm.put("Title", getTitle());
        fm.put("Frame Handle Name", handle.getComponentName());
        fm.put("Bounds",getDesktopItemBounds());
//if (componentHandle.getComponentName().equals("MapViewer"))
//    System.out.println("attributeTree recordState() bounds: " + getDesktopItemBounds());
        if (restoreBounds != null) {
//            if (componentHandle.getComponentName().equals("Browser"))
//                System.out.println("Browser recordtate()  restoreBounds: " + restoreBounds);
            fm.put("Restore Location", getDesktopItemRestoreLocation());
            fm.put("Restore Size", getDesktopItemRestoreSize());
        }
///        fm.put("Frame Icon",getFrameIcon());
        fm.put("Closable",isClosable());
        fm.put("CloseButtonVisible", isCloseButtonVisible());
//        fm.put("isCloseButtonVisible",isCloseButtonVisible());
        fm.put("Closed",false); //isClosed());
        fm.put("ComponentActivatedOnMouseClick",isComponentActivatedOnMouseClick());
        fm.put("ComponentNameChangeableFromMenuBar", isComponentNameChangeableFromMenuBar());
        fm.put("Resizable",resizable); //isDesktopItemResizable());
        fm.put("DraggableFromEast",isDraggableFromEast());
        fm.put("DraggableFromNorth",isDraggableFromNorth());
        fm.put("DraggableFromSouth",isDraggableFromSouth());
        fm.put("DraggableFromWest",isDraggableFromWest());
        fm.put("EastVisible",isEastVisible());
        fm.put("HelpButtonVisible",isHelpButtonVisible());
        fm.put("Icon",isIcon());
        fm.put("Iconifiable",isIconifiable());
        fm.put("MinimizeButtonVisible",isMinimizeButtonVisible());
        fm.put("InfoButtonVisible",isInfoButtonVisible());
        fm.put("Maximizable",isMaximizable());
        fm.put("MaximizeButtonVisible",isMaximizeButtonVisible());
        fm.put("Maximum",isMaximum());
//        fm.put("isMinMaxButtonVisible",isMinMaxButtonVisible());
        fm.put("NorthVisible",isNorthVisible());
        fm.put("PlugButtonVisible",isPlugButtonVisible());
        fm.put("Selected",isSelected());
        fm.put("Modal", isModal());
//System.out.println("Recordinf state ESlateInternalFrame " + getTitle() + ", selected: " + isSelected());
        fm.put("SouthVisible",isSouthVisible());
        fm.put("TitlePanelVisible",isTitlePanelVisible());
        fm.put("WestVisible",isWestVisible());
        if (isModal) {
            fm.put("Layer", new Integer(_prevLayer));
        }else{
            fm.put("Layer", getLayer());
        }
//Thread.currentThread().dumpStack();
        fm.put("Shape type", clipType);
        if (clipShape != null)
            fm.put("Shape", new ExternalizableShape(clipShape));

        //Layout saving
        OverlapBorderLayout layout=(OverlapBorderLayout) contentPane.getLayout();
        fm.put("NorthOverWest",layout.isNorthOverWest());
        fm.put("EastOverNorth",layout.isEastOverNorth());
        fm.put("SouthOverEast",layout.isSouthOverEast());
        fm.put("WestOverSouth",layout.isWestOverSouth());

//System.out.println("recordSkinState: " + recordSkinState);
        if (recordSkinState) {
            //ESlateInternalFrame skin saving
            if (north != null) {
                try{
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    ((SkinPane) north).writeExternal(oos);
                    oos.flush();
                    oos.close();
                    fm.put("north", bos.toByteArray());
                }catch (Throwable thr) {
                    thr.printStackTrace();
                    System.out.println("Unable to persist north area of ESlateInternalFrame " + componentHandle.getComponentName());
                    fm.put("north", null);
                }
            }

            if (south != null) {
                try{
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    ((SkinPane) south).writeExternal(oos);
                    oos.flush();
                    fm.put("south", bos.toByteArray());
                    oos.close();
                }catch (Throwable thr) {
                    thr.printStackTrace();
                    System.out.println("Unable to persist south area of ESlateInternalFrame " + componentHandle.getComponentName());
                    fm.put("south", null);
                }
            }

            if (east != null) {
                try{
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    ((SkinPane) east).writeExternal(oos);
                    oos.flush();
                    fm.put("east", bos.toByteArray());
                    oos.close();
                }catch (Throwable thr) {
                    thr.printStackTrace();
                    System.out.println("Unable to persist east area of ESlateInternalFrame " + componentHandle.getComponentName());
                    fm.put("east", null);
                }
            }

            if (west != null) {
                try{
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    ((SkinPane) west).writeExternal(oos);
                    oos.flush();
                    fm.put("west", bos.toByteArray());
                    oos.close();
                }catch (Throwable thr) {
                    thr.printStackTrace();
                    System.out.println("Unable to persist west area of ESlateInternalFrame " + componentHandle.getComponentName());
                    fm.put("west", null);
                }
            }
        }else{
            fm.put("north", "notStored");
            fm.put("south", "notStored");
            fm.put("west", "notStored");
            fm.put("east", "notStored");
        }
        return fm;
    }

/*    public void applyState(ComponentViewInfo info) {
        if (info == null) {
            try{
                setIcon(true);
            }catch (PropertyVetoException exc) {}
            return;
        }

        if (info.frameState == null) {
            setClosable(info.closable);
            setIconifiable(info.iconifiable);
            setMaximizable(info.maximizable);
            setTitlePanelVisible(info.barVisibilityFrameState);
            setHelpButtonVisible(info.helpButtonVisibilityFrameState);
            setInfoButtonVisible(info.infoButtonVisibilityFrameState);
            setPlugButtonVisible(info.pinButtonVisibilityFrameState);
            setComponentNameChangeableFromMenuBar(info.activeTitleFrameState);
            setResizable(info.resizable);
            try{
                if (!info.iconifiable)
                    setIconifiable(true);
                setIcon(info.isIcon);
                if (!info.iconifiable)
                    setIconifiable(info.iconifiable);
            }catch (PropertyVetoException exc) {}
            try{
  //                System.out.println("applyView() view: " + view.viewName + ", info.isMaximum: " + info.isMaximum);
                if (!info.maximizable)
                    setMaximizable(true);
                if (info.isMaximum) {
                    if (isMaximum())
                        setMaximum(false);
                    setSize(new Dimension(info.width, info.height));
                    setLocation(new Point(info.xLocation, info.yLocation));
                }
                setMaximum(info.isMaximum);
                if (!info.isMaximum) {
                    setSize(new Dimension(info.width, info.height));
                    setLocation(new Point(info.xLocation, info.yLocation));
                }
                if (!info.maximizable)
                    setMaximizable(info.maximizable);
            }catch (PropertyVetoException exc) {}
  //if                fr.setFrozen(info.isFrozen);
            setComponentActivatedOnMouseClick(info.componentActivatedOnMousePress);

            setLayer(info.layer);
        }else
            applyState(info.frameState);
    }
*/
public static long applyStateTimer = 0;
    /** Applies the state contained in the ESlateFieldMap to the ESlateInternalFrame.
     *  If the new state specifies that the frame is iconified, then it's state is
     *  stored in the variable 'stateToBeRestoredLater' and it is not applied. This
     *  speeds-up microworld construction. The stored state will be re-applied, the
     *  first time the component will become de-iconified, if this happens.
     *  @param fm The new state of the ESlateInternalFrame
     *  @param restoringView True, if applyState() is called while a microworld is being
     *         loaded, or a view is being applied.
     */
//    public void applyState(ESlateFieldMap fm, boolean restoringView) {
    public void applyState(StorageStructure fm, boolean restoringView) {
        applyState(fm, restoringView, true);
    }

    /** This special private version of a applyState() is used internally only. When the
     *  'doInitial' is false, then the restores that exist before checking the 'restoringView'
     *  variable and setting the value of 'stateToBeRestoredLater' do not happen. This happens
     *  only when applyState() calls itself (see below), in which case this restorations have
     *  already happened and we do not want them to be overriden by the new state which is restored
     *  by the internal call.
     */
    private void applyState(StorageStructure fm, boolean restoringView, boolean doInitial) {
//        if (componentHandle.getComponentName().equals("Browser")) {
//            System.out.println("Browser applyState()");
//            Thread.currentThread().dumpStack();
//        }
//System.out.println("APPLYSTATE of ESLATEINTERNALFRAME");
start = System.currentTimeMillis();
        if (doInitial) {
        try {
            Rectangle frameBounds = (Rectangle)fm.get("Bounds",getDesktopItemBounds());
            setDesktopItemBounds(frameBounds);
//if (componentHandle.getComponentName().equals("WebButtonPanel")) {
//    System.out.println("WebButtonPanel restoring bounds: " + frameBounds + ", fm.get(\"Icon\"): " + fm.get("Icon"));
//    Thread.currentThread().dumpStack();
//}

//            if (componentHandle.getComponentName().equals("Browser"))
//                System.out.println("Browser applyState() bounds: " + frameBounds);
            Point restoreLocation = (Point) fm.get("Restore Location");
            Dimension restoreSize = (Dimension) fm.get("Restore Size");
            if (restoreLocation != null && restoreSize != null) {
                restoreBounds= new Rectangle(restoreLocation, restoreSize);
            }else
                restoreBounds = null;
//            if (componentHandle.getComponentName().equals("Browser"))
//                System.out.println("MapViewer applyState()  restoreBounds: " + restoreBounds);
            setIcon(fm.get("Icon",isIcon()));
			if (!isModal) {
				setLayer(fm.get("Layer", getLayer()));
			}else{
				_prevLayer = fm.get("Layer", getLayer());
			}
        } catch(PropertyVetoException e) {}
        }
        if (restoringView) {
            if (isIcon) {
                stateToBeRestoredLater = fm;
//if (componentHandle.getComponentName().equals("WebButtonPanel"))
//    System.out.println("Storing stateToBeRestoredLater");
                return;
            }
//System.out.println("applyState stateToBeRestoredLater: " + stateToBeRestoredLater);
        }
        if (stateToBeRestoredLater != null) {
            // Very rare case. If a microworld starts and a component is iconified, then its state
            // hasn't yet fully been restored. In such a case if a view is applied (see applyView()
            // in ESlateContainer) before the component is de-iconified and in this view the
            // component is also iconified, then the original state of the component stored in
            // variable 'stateToBeRestoredLater' would be lost, unless we apply it now, and then
            // apply the new state 'fm', with which applyState() was called.
            StorageStructure state = stateToBeRestoredLater;
            stateToBeRestoredLater = null;
//if (componentHandle.getComponentName().equals("MapViewer"))
//    System.out.println("Re-applying state");
//            applyState(state, false, false);
            applyRestOfState(state); //, false, false);
        }

        applyRestOfState(fm);
//System.out.println("Fully restoring " + componentHandle.getComponentName());
//        System.out.println("applyState Bounds: " + (Rectangle)fm.get("Bounds"));
//        setTitle((String) fm.get("Title", getTitle()));
/*        Rectangle frameBounds = (Rectangle)fm.get("Bounds",getDesktopItemBounds());
        setDesktopItemBounds(frameBounds);
*/

applyStateTimer = applyStateTimer + (System.currentTimeMillis()-start);
/*        if (south != null && !isSouthVisible())
            south = null;
        if (north != null && !isNorthVisible())
            north = null;
        if (west != null && !isWestVisible())
            west = null;
        if (east != null && !isEastVisible())
            east = null;
*/
//System.out.println("ET applyState() end: " + (System.currentTimeMillis()-start) + ", " + componentHandle);

//System.out.println("Panel creation time: " + (long)(System.currentTimeMillis()-startTime));
/*        _north.setContentPaneLayout(new BorderLayout());
        _north.setBorder(null);
        _north.getESlateHandle().getMenuPanel().setVisible(false);
        _north.setPreferredSize(new Dimension(10,10));
*/
//        setNorth(_north);
/*        _south.setContentPaneLayout(new BorderLayout());
        _south.setBorder(null);
        _south.getESlateHandle().getMenuPanel().setVisible(false);
        _south.setPreferredSize(new Dimension(10,10));
*/
//        setSouth(_south);
/*        _east.setContentPaneLayout(new BorderLayout());
        _east.setBorder(null);
        _east.getESlateHandle().getMenuPanel().setVisible(false);
        _east.setPreferredSize(new Dimension(10,10));
*/
//        setEast(_east);
/*        _west.setContentPaneLayout(new BorderLayout());
        _west.setBorder(null);
        _west.getESlateHandle().getMenuPanel().setVisible(false);
        _west.setPreferredSize(new Dimension(10,10));
*/
//        setWest(_west);

    }

    private void applyRestOfState(StorageStructure fm) {
        boolean titleVisible = fm.get("TitlePanelVisible",isTitlePanelVisible());
        adjustTitlePaneVisibility(titleVisible);
/*        Point restoreLocation = (Point) fm.get("Restore Location");
        Dimension restoreSize = (Dimension) fm.get("Restore Size");
        if (restoreLocation != null && restoreSize != null) {
            restoreBounds= new Rectangle(restoreLocation, restoreSize);
        }else
            restoreBounds = null;
*/
///        setFrameIcon(fm.get("Frame Icon",getFrameIcon()));
        setClosable(fm.get("Closable",isClosable()));
//        setCloseButtonVisible(fm.get("isCloseButtonVisible",isCloseButtonVisible()));
        try {
            setClosed(fm.get("Closed", isClosed()));
        } catch(PropertyVetoException e) {}
        this.setCloseButtonVisibleInternal(fm.get("CloseButtonVisible",this.isCloseButtonVisible()));
        setComponentActivatedOnMouseClick(fm.get("ComponentActivatedOnMouseClick",isComponentActivatedOnMouseClick()));
///        getGlassPane().setVisible(isComponentActivatedOnMouseClick());
        setComponentNameChangeableFromMenuBar(fm.get("ComponentNameChangeableFromMenuBar", isComponentNameChangeableFromMenuBar()));
        setDesktopItemResizable(fm.get("Resizable",isDesktopItemResizable()));
        setDraggableFromEast(fm.get("DraggableFromEast",isDraggableFromEast()));
        setDraggableFromNorth(fm.get("DraggableFromNorth",isDraggableFromNorth()));
        setDraggableFromSouth(fm.get("DraggableFromSouth",isDraggableFromSouth()));
        setDraggableFromWest(fm.get("DraggableFromWest",isDraggableFromWest()));
//        setEastVisible(fm.get("EastVisible",isEastVisible()));
        setHelpButtonVisibleInternal(fm.get("HelpButtonVisible",isHelpButtonVisible()));
/*        try {
            setIcon(fm.get("Icon",isIcon()));
        } catch(PropertyVetoException e) {}
*/
        setIconifiable(fm.get("Iconifiable",isIconifiable()));
        setMinimizeButtonVisibleInternal(fm.get("MinimizeButtonVisible",isMinimizeButtonVisible()));
        setInfoButtonVisibleInternal(fm.get("InfoButtonVisible",isInfoButtonVisible()));
        setMaximizable(fm.get("Maximizable",isMaximizable()));
        try {
            setMaximum(fm.get("Maximum",isMaximum()));
        } catch(PropertyVetoException e) {}
        setMaximizeButtonVisibleInternal(fm.get("MaximizeButtonVisible",isMaximizeButtonVisible()));
        setModal(fm.get("Modal", isModal()));
//        setMinMaxButtonVisible(fm.get("isMinMaxButtonVisible",isMinMaxButtonVisible()));
//        setNorthVisible(fm.get("NorthVisible",isNorthVisible()));
        setPlugButtonVisibleInternal(fm.get("PlugButtonVisible",isPlugButtonVisible()));
        try {
            setSelected(fm.get("Selected",isSelected()));
//System.out.println("applyState() ESlateInternalFrame " + getTitle() + " fm.get(\"Selected\"): " + fm.get("Selected") + ", isSelected(): " + isSelected());
        } catch(PropertyVetoException e) {}
//        setSouthVisible(fm.get("SouthVisible",isSouthVisible()));
//        setWestVisible(fm.get("WestVisible",isWestVisible()));

        setClipShapeType(new ShapeType(fm.get("Shape type", ShapeType.RECTANGLE), this));
        ExternalizableShape sh = (ExternalizableShape) fm.get("Shape");
        if (sh != null) {
            if (clipType == ShapeType.POLYGON) {
                setClipShape(sh.getPolygon());
            }else{
                setClipShape(sh.getShape());
            }
        }

        //Layout retrieval
        OverlapBorderLayout layout=(OverlapBorderLayout) contentPane.getLayout();
        layout.setNorthOverWest(fm.get("NorthOverWest",layout.isNorthOverWest()));
        layout.setEastOverNorth(fm.get("EastOverNorth",layout.isEastOverNorth()));
        layout.setSouthOverEast(fm.get("SouthOverEast",layout.isSouthOverEast()));
        layout.setWestOverSouth(fm.get("WestOverSouth",layout.isWestOverSouth()));
//        JComponent _north = (JComponent) fm.get("north");

//System.out.println("ET applyState() 1 title: " + getTitle() + " time: " + (System.currentTimeMillis()-start));
        // Restore the state of the ESlateInternalFrame's areas
        Object obj = fm.get("north");
//System.out.println("applyState() north obj: " + obj);
        if (obj == null || (String.class.isAssignableFrom(obj.getClass()) && ((String) obj).equals("notStored"))) {
        }else{
            SkinPane _north = null;
//            if (obj == null)
//                _north = createArea(SwingConstants.NORTH);
//            else if (SkinPane.class.isAssignableFrom(obj.getClass()))
            if (SkinPane.class.isAssignableFrom(obj.getClass()))
                _north = (SkinPane) obj;
            else if (PanelComponent.class.isAssignableFrom(obj.getClass())) {
                _north = createArea(SwingConstants.NORTH);
                if (((PanelComponent) obj).getContentPane().getComponentCount() > 0)
                    _north.getContentPane().add(((PanelComponent) obj).getContentPane().getComponent(0));
            }else{
                byte[] northState = (byte[]) obj;
                _north = createArea(SwingConstants.NORTH);
                if (northState != null) {
                    try{
//System.out.println("ET applyState() 2: " + (System.currentTimeMillis()-start));
                        ByteArrayInputStream bis = new ByteArrayInputStream(northState);
//                        BufferedInputStream buffStream  = new BufferedInputStream(bis, northState.length);
                        ObjectInputStream ois = new ObjectInputStream(bis); //buffStream);
                        _north.readExternal(ois);
                        ois.close();
//System.out.println("ET applyState() 2: " + (System.currentTimeMillis()-start));
                    }catch (Throwable thr) {
                        _north = createArea(SwingConstants.NORTH);
                        System.out.println("Unable to restore the north area of the ESlateInternalFrame " + componentHandle);
                    }
                }
            }
            setNorth(_north);
            /* If there is a south SkinPane and it is an ESlatePart, then after
             * restoring it, attach any pending, unattached ScriptListeners to it
             * or its contents.
             */
            if (_north != null && ESlatePart.class.isAssignableFrom(_north.getClass()))
                container.getScriptListenerMap().locateAndAttachUnattachedListeners(((ESlatePart) _north).getESlateHandle());
        }

        obj = fm.get("south");
        if (obj == null || (String.class.isAssignableFrom(obj.getClass()) && ((String) obj).equals("notStored"))) {
        }else{
            SkinPane _south = null;
//            if (obj == null)
//                _south = createArea(SwingConstants.SOUTH);
//            else if (SkinPane.class.isAssignableFrom(obj.getClass()))
            if (SkinPane.class.isAssignableFrom(obj.getClass()))
                _south = (SkinPane) obj;
            else if (PanelComponent.class.isAssignableFrom(obj.getClass())) {
                _south = createArea(SwingConstants.SOUTH);
                if (((PanelComponent) obj).getHostedComponentCount() > 0)
                    _south.getContentPane().add(((PanelComponent) obj).getHostedComponents()[0]);
            }else{
                byte[] southState = (byte[]) obj;
                _south = createArea(SwingConstants.SOUTH);
                if (southState != null) {
                    try{
                        ByteArrayInputStream bis = new ByteArrayInputStream(southState);
//                        BufferedInputStream buffStream  = new BufferedInputStream(bis);
                        ObjectInputStream ois = new ObjectInputStream(bis); //buffStream);
                        _south.readExternal(ois);
                        ois.close();
                    }catch (Throwable thr) {
                        _south = createArea(SwingConstants.SOUTH);
                        thr.printStackTrace();
                        System.out.println("Unable to restore the south area of the ESlateInternalFrame " + componentHandle.getComponentName());
                    }
                }
            }
            setSouth(_south);
            /* If there is a south SkinPane and it is an ESlatePart, then after
             * restoring it, attach any pending, unattached ScriptListeners to it
             * or its contents.
             */
            if (_south != null && ESlatePart.class.isAssignableFrom(_south.getClass()))
                container.getScriptListenerMap().locateAndAttachUnattachedListeners(((ESlatePart) _south).getESlateHandle());
        }

        obj = fm.get("west");
//System.out.println("West: " + obj);
        if (obj == null || (String.class.isAssignableFrom(obj.getClass()) && ((String) obj).equals("notStored"))) {
        }else{
            SkinPane _west = null;
//            if (obj == null)
//                _west = createArea(SwingConstants.WEST);
//            else if (SkinPane.class.isAssignableFrom(obj.getClass()))
            if (SkinPane.class.isAssignableFrom(obj.getClass()))
                _west = (SkinPane) obj;
            else if (PanelComponent.class.isAssignableFrom(obj.getClass())) {
                _west = createArea(SwingConstants.WEST);
                if (((PanelComponent) obj).getHostedComponentCount() > 0)
                    _west.getContentPane().add(((PanelComponent) obj).getHostedComponents()[0]);
            }else{
                byte[] westState = (byte[]) obj;
                _west = createArea(SwingConstants.WEST);
                if (westState != null) {
                    try{
                        ByteArrayInputStream bis = new ByteArrayInputStream(westState);
                        BufferedInputStream buffStream  = new BufferedInputStream(bis);
                        ObjectInputStream ois = new ObjectInputStream(bis); //buffStream);
                        _west.readExternal(ois);
                        ois.close();
                    }catch (Throwable thr) {
                        _west = createArea(SwingConstants.WEST);
                        System.out.println("Unable to restore the west area of the ESlateInternalFrame " + componentHandle.getComponentName());
                    }
                }
            }
            setWest(_west);
            /* If there is a south SkinPane and it is an ESlatePart, then after
             * restoring it, attach any pending, unattached ScriptListeners to it
             * or its contents.
             */
            if (_west != null && ESlatePart.class.isAssignableFrom(_west.getClass()))
                container.getScriptListenerMap().locateAndAttachUnattachedListeners(((ESlatePart) _west).getESlateHandle());
        }

        obj = fm.get("east");
        if (obj == null || (String.class.isAssignableFrom(obj.getClass()) && ((String) obj).equals("notStored"))) {
        }else{
            SkinPane _east = null;
//            if (obj == null)
//                _east = createArea(SwingConstants.EAST);
//            else if (SkinPane.class.isAssignableFrom(obj.getClass()))
            if (SkinPane.class.isAssignableFrom(obj.getClass()))
                _east = (SkinPane) obj;
            else if (PanelComponent.class.isAssignableFrom(obj.getClass())) {
                _east = createArea(SwingConstants.EAST);
                if (((PanelComponent) obj).getHostedComponentCount() > 0)
                    _east.getContentPane().add(((PanelComponent) obj).getHostedComponents()[0]);
            }else{
                byte[] eastState = (byte[]) obj;
                _east = createArea(SwingConstants.EAST);
                if (eastState != null) {
                    try{
                        ByteArrayInputStream bis = new ByteArrayInputStream(eastState);
                        BufferedInputStream buffStream  = new BufferedInputStream(bis);
                        ObjectInputStream ois = new ObjectInputStream(bis); //buffStream);
                        _east.readExternal(ois);
                        ois.close();
                    }catch (Throwable thr) {
                        _east = createArea(SwingConstants.EAST);
                        System.out.println("Unable to restore the east area of the ESlateInternalFrame " + componentHandle.getComponentName());
                    }
                }
            }
            setEast(_east);
            /* If there is a south SkinPane and it is an ESlatePart, then after
             * restoring it, attach any pending, unattached ScriptListeners to it
             * or its contents.
             */
            if (_east != null && ESlatePart.class.isAssignableFrom(_east.getClass()))
                container.getScriptListenerMap().locateAndAttachUnattachedListeners(((ESlatePart) _east).getESlateHandle());
        }

        setNorthVisible(fm.get("NorthVisible",isNorthVisible()));
        setSouthVisible(fm.get("SouthVisible",isSouthVisible()));
        setWestVisible(fm.get("WestVisible",isWestVisible()));
        setEastVisible(fm.get("EastVisible",isEastVisible()));
    }

    private SkinPane createArea(int pos) {
//long cstart = System.currentTimeMillis();
//Thread.currentThread().dumpStack();
        SkinPane pane = new SkinPane();
        if (handle == null)
            getESlateHandle();
//System.out.println("createArea() 1: " + (System.currentTimeMillis()-cstart));
        ESlateHandle paneHandle = pane.getESlateHandle();
        handle.add(paneHandle);
//System.out.println("createArea() 2: " + (System.currentTimeMillis()-cstart));
        try{
            if (pos == SwingConstants.NORTH)
                paneHandle.setUniqueComponentName(bundle.getString("north"));
            if (pos == SwingConstants.SOUTH)
                paneHandle.setUniqueComponentName(bundle.getString("south"));
            if (pos == SwingConstants.WEST)
                paneHandle.setUniqueComponentName(bundle.getString("west"));
            if (pos == SwingConstants.EAST)
                paneHandle.setUniqueComponentName(bundle.getString("east"));
        }catch (RenamingForbiddenException exc) {}
//System.out.println("createArea " + pos + ", time: " + (System.currentTimeMillis()-cstart));

        return pane;
/*        PanelComponent p = new PanelComponent();
        p.setContentPaneLayout(new BorderLayout());
        p.setBorder(null);
        p.getESlateHandle().getMenuPanel().setVisible(false);
//        p.getESlateHandle().getMenuPanel().getParent().remove(p.getESlateHandle().getMenuPanel());
        return p;
*/    }

    private void disposeListeners() {
/*        if (north != null) {
System.out.println("north not null");
((SkinPane) north).getESlateHandle().dispose();
        }
        if (south != null) {
System.out.println("north not null");
((SkinPane) south).getESlateHandle().dispose();
        }
        if (west != null) {
System.out.println("north not null");
((SkinPane) west).getESlateHandle().dispose();
        }
        if (east != null) {
System.out.println("north not null");
((SkinPane) east).getESlateHandle().dispose();
        }
*/
        removeMouseListener(borderListener);
        removeMouseMotionListener(borderListener);
        removePropertyChangeListener(propertyChangeListener);
        propertyChangeListener = null;
///	getGlassPane().removeMouseListener(glassPaneDispatcher);
///       	getGlassPane().removeMouseMotionListener(glassPaneDispatcher);
///        glassPaneDispatcher = null;
        content.removeContainerListener(containerListener);
        containerListener = null;
        if (titlePane!=null) {
            framePane.remove(titlePane);
            titlePane.removeMouseListener(dragAroundListener);
            titlePane.removeMouseMotionListener(dragAroundListener);
            if (ESlateInternalFrameTitlePanel.class.isAssignableFrom(titlePane.getClass()))
                ((ESlateInternalFrameTitlePanel) titlePane).dispose();
            remove(titlePane);
            titlePane = null;
        }
        if (isDraggableFromNorth() && north != null) {
            north.removeMouseListener(dragAroundListener);
            north.removeMouseMotionListener(dragAroundListener);
        }
        if (isDraggableFromSouth() && south != null) {
            south.removeMouseListener(dragAroundListener);
            south.removeMouseMotionListener(dragAroundListener);
        }
        if (isDraggableFromWest() && west != null) {
            west.removeMouseListener(dragAroundListener);
            west.removeMouseMotionListener(dragAroundListener);
        }
        if (isDraggableFromEast() && east != null) {
            east.removeMouseListener(dragAroundListener);
            east.removeMouseMotionListener(dragAroundListener);
        }
//        listenerList.clear();
        if (getParent() != null)
            getParent().removeComponentListener(componentListener);
        closeAction = null;
        iconifyAction = null;
        maximizeAction = null;
        restoreAction = null;
        rootPane.removeAll();
        removeAll();
//        rootPane = null;
        borderListener = null;
        dragAroundListener = null;
        customNorthBorderComponent = null;
        customSouthBorderComponent = null;
        customWestBorderComponent = null;
        customEastBorderComponent = null;
        propertyChangeListener = null;
        componentListener = null;
        handle = null;
        componentHandle = null;
    }

    private static Border getWestBorder() {
        if (WEST_BORDER == null)
            WEST_BORDER = new CompoundBorder(new EmptyBorder(3, 0, 3, 3), new DegradatedColorMatteBorder(0, 3, 0, 0));
        return WEST_BORDER;
    }
    private static Border getEastBorder() {
        if (EAST_BORDER == null)
            EAST_BORDER = new CompoundBorder(new EmptyBorder(3, 3, 3, 0), new DegradatedColorMatteBorder(0, 0, 0, 3));
        return EAST_BORDER;
    }
    private static Border getNorthBorder() {
        if (NORTH_BORDER == null)
            NORTH_BORDER = new CompoundBorder(new EmptyBorder(0, 3, 3, 3), new DegradatedColorMatteBorder(3, 0, 0, 0));
        return NORTH_BORDER;
    }
    private static Border getSouthBorder() {
        if (SOUTH_BORDER == null)
            SOUTH_BORDER = new CompoundBorder(new EmptyBorder(3, 3, 0, 3), new DegradatedColorMatteBorder(0, 0, 3, 0));
        return SOUTH_BORDER;
    }
    private static Border getSouthWestBorder() {
        if (SOUTH_WEST_BORDER == null)
            SOUTH_WEST_BORDER = new CompoundBorder(new EmptyBorder(3, 0, 0, 3), new DegradatedColorMatteBorder(0, 3, 3, 0));
        return SOUTH_WEST_BORDER;
    }
    private static Border getSouthEastBorder() {
        if (SOUTH_EAST_BORDER == null)
            SOUTH_EAST_BORDER = new CompoundBorder(new EmptyBorder(3, 3, 0, 0), new DegradatedColorMatteBorder(0, 0, 3, 3));
        return SOUTH_EAST_BORDER;
    }
    private static Border getNorthEastBorder() {
        if (NORTH_EAST_BORDER == null)
            NORTH_EAST_BORDER = new CompoundBorder(new EmptyBorder(0, 3, 3, 0), new DegradatedColorMatteBorder(3, 0, 0, 3));
        return NORTH_EAST_BORDER;
    }
    private static Border getNorthWestBorder() {
        if (NORTH_WEST_BORDER == null)
            NORTH_WEST_BORDER = new CompoundBorder(new EmptyBorder(0, 0, 3, 3), new DegradatedColorMatteBorder(3, 3, 0, 0));
        return NORTH_WEST_BORDER;
    }
    private static Border getNorthSouthWestEastEastBorder() {
        if (NORTH_SOUTH_WEST_EAST_BORDER == null)
            NORTH_SOUTH_WEST_EAST_BORDER = new DegradatedColorMatteBorder(3, 3, 3, 3);
        return NORTH_SOUTH_WEST_EAST_BORDER;
    }
    private static Border getOrangeLineBorder() {
        if (ORANGE_LINE_BORDER == null)
            ORANGE_LINE_BORDER = new LineBorder(Color.orange, 1);
        return ORANGE_LINE_BORDER;
    }
    private static Border getGrayLineBorder() {
        if (GRAY_LINE_BORDER == null)
            GRAY_LINE_BORDER = new LineBorder(Color.lightGray, 1);
        return GRAY_LINE_BORDER;
    }
    private static Border getFrameSelectedBorder() {
        if (FRAME_SELECTED_BORDER == null)
            FRAME_SELECTED_BORDER = new CompoundBorder(new EmptyBorder(2,2,2,2), getOrangeLineBorder());
        return FRAME_SELECTED_BORDER;
    }

    // The ActorInterface methods
    public StringBaseArray getAnimationVariablesNames() {
        return null;
    }
    public IntBaseArray getVarValues() {
        return null;
    }
    public void setAnimatedVariables(BoolBaseArray p0) {
    }
    public void setVarValues(IntBaseArray p0) {
    }
    public void onStage() {
    }
    public void offStage() {
    }

    // The ShapedComponent methods
    /**
     * Specifies the shape type to which the ESlateInternalFrame is being clipped.
     * @param	shape	The clip shape tape.
     */
    public void setClipShapeType(ShapeType shape) {
        clipType = shape.type;
        if (clipType == ShapeType.ELLIPSE) {
            Ellipse2D.Float el = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
            clipShape = el;
            shapeBounds = el.getBounds();
        }else{
            clipShape = null;
            shapeBounds = null;
        }

        repaint();
    }

    /**
     * Returns the type of shape to which the ESlateInternalFrame is being clipped.
     * @return	A <code>ShapeType</code> instance.
     */
    public ShapeType getClipShapeType() {
        return new ShapeType(clipType, this);
    }

    /**
     * Specifies the shape to which the ESlateInternalFrame is being clipped. This method
     * should be invoked after <code>setClipShapeType</code> has been invoked.
     * It is meant to be invoked by pressing the "edit shape" button in the
     * property editor.
     */
    public void setClipShape(Shape s) {
        switch (clipType) {
            case ShapeType.ELLIPSE:
            case ShapeType.FREEHAND:
                clipShape = s;
                shapeBounds = s.getBounds();
                break;
            case ShapeType.POLYGON:
                clipShape = s;
                shapeBounds = s.getBounds();
                break;
            case ShapeType.RECTANGLE:
            default:
                clipShape = null;
                shapeBounds = null;
        }
        repaint();
    }

    /**
     * Returns the shape to which the ESlateInternalFrame is being clipped.
     * @return	The shape to which the ESlateInternalFrame is being clipped. If the
     *		component is a regular, rectangular component,
     *		<code>null</code> is returned.
     */
    public Shape getClipShape() {
        return clipShape;
/*        switch (clipType) {
            case ShapeType.ELLIPSE:
            case ShapeType.POLYGON:
            case ShapeType.FREEHAND:
                return clipShape;
            case ShapeType.RECTANGLE:
            default:
                return null;
        }
*/
    }

    public Color getOutlineColor() {
        return Color.red;
    }

    /**
     * Check whether a point is inside the ESlateInternal, taking the clipping shape
     * into consideration.
     * <EM>Note:</EM> <code>inside</code> may be deprecated in favor of
     * <code>contains</code>, but this is the method that does the actual job in
     * Sun's implementation, so this is the one that we override.
     * @param	x	The X coordinate of the point.
     * @param	y	The Y coordinate of the point.
     */
    public boolean inside(int x, int y) {
        boolean result;
        switch (clipType) {
            case ShapeType.POLYGON:
            case ShapeType.ELLIPSE:
            case ShapeType.FREEHAND:
                if (clipShape != null) {
//                    Rectangle r = clipShape.getBounds();
                    if (shapeBounds.contains(x, y)) {
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
     * Paint the component without doing any clipping or other extra work. This
     * method will be used by the shape property editor to obtain a snapshot
     * of the component.
     * @param g
     */
    public void simplePrint(java.awt.Graphics g) {
        simplePaint = true;
        super.print(g);
        simplePaint = false;
    }

    public void addShapeChangedListener(ShapeChangedListener scl) {
        shapeChangedListenerSupport.addShapeChangedListener(scl);
    }

    public void removeShapeChangedListener(ShapeChangedListener scl) {
        shapeChangedListenerSupport.removeShapeChangedListener(scl);
    }

	Cursor defaultCursor = null;
	/** Sets the defaultCursor used by setCursor() commands in the code
     * of the ESlateInternalFrame, who want to set the cursor to its default
	 * state. If Cursor.getDefaultCursor() was used, then any cursor that
	 * the hosted component sets, would be neglected.
	 */
	public void setDefaultCursor(Cursor c) {
		defaultCursor = c;
		setCursor(c);
	}

	public boolean isFocusTraversable() {
		return false;
	}

    public void adjustTitlePanelButtons(boolean minimizeButtonVisible, boolean maximizeButtonVisible, boolean closeButtonVisible,
            boolean helpButtonVisible, boolean pinButtonVisible, boolean infoButtonVisible) {
        setMaximizeButtonVisibleInternal(minimizeButtonVisible);
        setMaximizeButtonVisibleInternal(maximizeButtonVisible);
        setCloseButtonVisibleInternal(closeButtonVisible);
//if        frame.setComponentNameChangeableFromMenuBar(container.currentView.controlBarTitleActive);
        setHelpButtonVisibleInternal(helpButtonVisible);
        setPlugButtonVisibleInternal(pinButtonVisible);
        setInfoButtonVisibleInternal(infoButtonVisible);
    }


    /** Override this method to ensure that if the shape of the component is
     *  Ellipse (id: ShapeType.ELLIPSE), the ellipse will get resized, as the
     *  component resizes.
     */
    protected void processComponentEvent(ComponentEvent e) {
        super.processComponentEvent(e);
        if (e.getID() == ComponentEvent.COMPONENT_RESIZED) {
            if (clipType == ShapeType.ELLIPSE) {
                // Recalculate cached clip shape and bounds.
                Ellipse2D.Float el =
                  new Ellipse2D.Float(0, 0, getWidth(), getHeight());
                clipShape = el;
                shapeBounds = el.getBounds();
                repaint();
            }
        }
    }


    //For test purposes
    public static final void main(String[] args) {
        JFrame f=new JFrame();
        f.setSize(500,400);
        final ESlateInternalFrame ei=new ESlateInternalFrame();
        ei.setBounds(100,100,200,200);
        ei.setVisible(true);
        ei.setResizable(true);
        ei.setMaximizable(true);
        ei.setClosable(true);
        ei.setIconifiable(true);
//        ei.getContentPane().add(new gr.cti.eslate.clock.Clock());
        ei.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                System.out.println(e.getPropertyName()+" "+e.getNewValue());
            }
        });
        f.getContentPane().setLayout(null);
        f.getContentPane().setBackground(SystemColor.desktop);
        f.getContentPane().add(ei);

        ESlateInternalFrame ei2=new ESlateInternalFrame(null);
        ei2.setBounds(100,200,200,200);
        ei2.setVisible(true);
        ei2.setResizable(true);
        ei2.setMaximizable(true);
        ei2.setClosable(true);
        ei2.setIconifiable(true);
//        ei2.setContentPane(new gr.cti.eslate.clock.Clock());
        f.getContentPane().add(ei2);

        f.setVisible(true);
    }
}

class DegradatedColorMatteBorder extends MatteBorder {
    int top = 0, bottom = 0, left = 0, right = 0;

    public DegradatedColorMatteBorder(int top, int left, int bottom, int right) {
        super(top, left, bottom, right, Color.lightGray);
//System.out.println("Constructor ESlateInternalFrame DegradatedColorMatteBorder()");
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    /**
     * Paints the border for the specified component with the specified
     * position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        int h = height;
        int w = width;
        g.translate(x, y);

//        System.out.println("x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);
//        Object obj = ((ESlateInternalFrame) c).eSlateHandle.getComponent();
        Color color = ((ESlateInternalFrame) c).getDesktopPane().getBackground(); //((Component) obj).getBackground();
        if (left != 0) {
          g.setColor(color);
          g.drawLine(2, 0, 2, h);
          g.drawLine(1, 0, 1, h);
          g.setColor(Color.orange); //color.brighter());
          g.drawLine(0, 0, 0, height);
        }
        if (top != 0) {
          g.setColor(color);
          g.drawLine(1, 1, w, 1);
          g.drawLine(1, 2, w, 2);
          g.setColor(Color.orange); //.brighter());
          g.drawLine(0, 0, w, 0);
        }
        if (bottom != 0) {
          g.setColor(color);
          g.drawLine(1, height-2, w, height-2);
          g.drawLine(1, height, w, height);
          g.setColor(Color.orange); //color.brighter());
          g.drawLine(0, height-1, w, height-1);
        }
        if (right != 0) {
          g.setColor(color);
          g.drawLine(width-2, 1, width-2, h-2);
          g.drawLine(width, 1, width, h-2);
          g.setColor(Color.orange); //color.brighter());
          g.drawLine(width-1, 0, width-1, h);
        }
        g.translate(-x, -y);
        g.setColor(oldColor);
    }
}
