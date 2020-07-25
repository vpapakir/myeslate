package gr.cti.eslate.base.container;
import gr.cti.eslate.base.ComponentNameChangedEvent;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateListener;
import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.JavaScriptManager;
import gr.cti.eslate.base.PrimitiveGroupAddedEvent;
import gr.cti.eslate.base.PrimitiveGroupAddedListener;
import gr.cti.eslate.base.ScriptManager;
import gr.cti.eslate.base.container.event.ActiveComponentChangedEvent;
import gr.cti.eslate.base.container.event.ESlateComponentEvent;
import gr.cti.eslate.base.container.event.ESlateComponentListener;
import gr.cti.eslate.base.container.event.ESlateContainerListener;
import gr.cti.eslate.base.container.event.MicroworldComponentEvent;
import gr.cti.eslate.base.container.event.MicroworldEvent;
import gr.cti.eslate.base.container.event.MicroworldListener;
import gr.cti.eslate.base.container.event.MicroworldViewEvent;
import gr.cti.eslate.base.container.event.MwdHistoryChangedEvent;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.base.help.HelpSetLoader;
import gr.cti.eslate.base.help.HelpSystemViewer;
import gr.cti.eslate.imageChooser.ImageChooser;
import gr.cti.eslate.scripting.LogoScriptable;
import gr.cti.eslate.scripting.NameSupport;
import gr.cti.eslate.scripting.logo.ComponentPrimitives;
import gr.cti.eslate.scripting.logo.ScriptableObjectsNameSupport;
import gr.cti.eslate.utils.BooleanWrapper;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.eslate.utils.WorkArea;
import gr.cti.eslate.utils.sound.ESlateSound;
import gr.cti.eslate.utils.sound.ESlateSoundEvent;
import gr.cti.eslate.utils.sound.ESlateSoundListener;
import gr.cti.eslate.utils.sound.SoundUtils;
import gr.cti.structfile.Entry;
import gr.cti.structfile.StructFile;
import gr.cti.structfile.StructInputStream;
import gr.cti.structfile.StructOutputStream;
import gr.cti.typeArray.BoolBaseArray;
import gr.cti.typeArray.StringBaseArray;

import java.applet.Applet;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.PaintEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.help.HelpSet;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultDesktopManager;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.MouseInputAdapter;

import virtuoso.logo.CaselessString;
import virtuoso.logo.Console;
import virtuoso.logo.InterpEnviron;
import virtuoso.logo.InterpreterThread;
import virtuoso.logo.LanguageException;
import virtuoso.logo.LogoList;
import virtuoso.logo.MyMachine;
import virtuoso.logo.Tokenizer;

import com.objectspace.jgl.Array;
import com.objectspace.jgl.HashMap;
import com.objectspace.jgl.OrderedMap;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.SuccessStatus;
// PageLayout packages
/* import com.klg.jclass.page.JCFlow;
   import com.klg.jclass.page.JCPrinter;
   import com.klg.jclass.page.JCDocument;
   import com.klg.jclass.page.adobe.pdf.JCPDFPrinter;
*/
import org.cbook.cbookif.Constants;

public class ESlateContainer extends JPanel {
	
	public static final boolean EMBEDDED_MODE=true;
	private static final long serialVersionUID = 1L;
	// The current version of the ESlateContainer.
    final private static String _version = "1.9.0"; //"0.9.8";
    /* 1.0 --> 1.1 RestorableImageIcons changed to NewRestorableImageIcons
     * 2.0 --> 3 METADATA_FILE exists in microworld structured storage files.
     *         Since this version the MWD_STR_FORMAT_VERSION is no longer a String but an int.
	 *   4 --> 5 The sources of the microworld scripts are not saved as part of
     *         the state of the Scripts/ScriptListeners of the microworld. They
     *         are stored in the directory 'SCRIPT_DIR_NAME' in the root directory
	 *         of the microworld structfile.
     */
    public final static int MWD_STR_FORMAT_VERSION = 5; //4; //"2.0";
    public static final int COMPONENT_STATE_VERSION = 1;
    public final static String METADATA_FILE = "Microworld Metatada";
    public final static String SOUND_THEME_FILE_NAME = "sounds.est";
	// The top-level directory in a microworld's structfile, where the sources
	// of the microworld scripts are stored (one file per script).
	public static final String SCRIPT_DIR_NAME = "Microworld scripts";

    public final static int PRINTER = 1;
//    public final static int PDF_FILE = 2;
//    public final static int RTF_FILE = 3;
    public final static int FILE = 4;

    public transient final static ImageIcon ESLATE_LOGO = new ImageIcon(ESlateContainer.class.getResource("images/eslateLogo.gif"));
    public static final int UP_SIDE =   1;
    public static final int DOWN_SIDE = 2;
    public static final int LEFT_SIDE = 3;
    public static final int RIGHT_SIDE= 4;
    public static final int ALL_SIDES = 5;
    // Register to the Logo name space
   private NameSupport nameSupport = new ScriptableObjectsNameSupport();
    // Maker values
//    JCheckBox closeBox;
//    JCheckBox maxBox;
//    JCheckBox iconBox;
//    JCheckBox resizeBox;
//    JTextField titleField;
//    JTextField layerField;
//    JButton closeAllButton;
//    JButton makeButton;
    protected static Color defaultContainerBgrColor = java.awt.SystemColor.desktop; //new Color(0, 127, 127);
    protected Color emptyContainerBgrColor = UIManager.getColor("controlShadow");
////nikosM
    //The maximum file size that can be read in and transferred
//    protected static final int MAX_FILE_LENGTH = 1700000;
////nikosM
    public static final int DONT_SAVE = 1;
    public static final int SAVE = 0;
    public static final int CANCEL = 2;

    DesktopPane lc;
    protected ContainerScrollPane scrollPane;
    static public ResourceBundle containerBundle;
    private Locale locale;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    protected ESlateContainer container;
    /* The top most Component in the component hierarchy of the ESlateContainer */
    protected Component parentComponent;
    /* If the parentComponent in not null and is of Frame class, then the following
     * variable points to it.
     */
    protected Frame parentFrame;
    /* The parentComponent's 'setTitle' method, if it exists.It is used to set the
     * ESlateContainer's title -setContainerTitle() method.
     */
    private Method methodSetTitle = null;
    protected TitlePanel titlePanel = null;
    /* Adjusts whether the titlePanel is enabled(i.e visible) or not.
     */
    boolean containerTitleEnabled = true;

    protected MicroworldComponents mwdComponents;

/*0    private Array heavyweightComponents;
    Array components;
    private Array componentFrameIndex;
    Array componentFrames;
    Array eSlateHandles;
*/
    /* Holds the history of selections of frames in the microworld.
     */
    SelectionHistory compSelectionHistory;

    /* This structure holds the information about the registered components */
    protected InstalledComponentStructure installedComponents;
///// beginning of mantes code
    InstalledLookAndFeelStructure installedLAFs;

///// end of mantes code

//// nikosM code
    public WebServerAccessWorlds webServerMicrosHandle;
//// nikosM end of code

//    OrderedMap installedComponentNames;
    /* Which of the installed components are available to the end user
     * This Hashtable contains as many entries as the "installedComponentNames" Hashtable.
     */
//    OrderedMap componentAvailability;
    /* The registered web sites
     */
    public OrderedMap webSites;

/// nikosM change
    /* Which of the registered web sites are available to the end-user. This OrderedMap
     * contains as many entries as the webSites OrderedMap.
     */
//    private OrderedMap webSiteAvailability;
    protected OrderedMap webSiteAvailability;
/// end of nikosM code

    /* The common dirs (upload/download area) of each registered web site.
     */
    private OrderedMap webSiteCommonDirs;
    /* The name of the microworld file to be loaded on start-up.
     */
    String preloadMwdFileName;
    /* The site of the microworld file to be loaded on start-up, if this file resides on the net.
     */
    String preloadMwdSiteAddress;
    /*  pathToContainerJar is used to locate and store the path to the ESlateContainer<xxx>.jar
     *  archieve, if this is in the classpath. The directory in which this archieve
     * resides is used to store the "container.properties" archieve.
     */
    transient static String pathToContainerJar=null;
    static{
    	if(!EMBEDDED_MODE) {
    		pathToContainerJar = findPathToContainerJar();
    	}
    }
    /* The file which stores the container's properties.
     */
    static String CONTAINER_PROPERTIES = getLocalizedContainerPropertiesFile();
    private transient File containerPropertiesFile = new File(CONTAINER_PROPERTIES);
    private transient static File binFolder = null;
    /* The file which stores temporary files loaded from web servers.
     */
    private String tmpDirName = null;
    File tmpFile = null;
    int serial=0;
    /* This HashTable holds the registered script listeners. The HashTable contains an
     * entry for each component. The key of the entry is the component name. The value
     * is a ScriptListener[] array. A ScriptListener is a listener which carries a script.
     * The listener has been dynamically bidded to a component's event. When the event on
     * the component occurs the script is executed by the Logo machine.
     */
    protected ScriptListenerMap componentScriptListeners = new ScriptListenerMap(this);
    /** This is the structure that stores all the SoundListeners of a microworld.
     */
    protected SoundListenerMap soundListenerMap = new SoundListenerMap(this);

//    protected String activityTitle = "";
//mv    protected Border microworldBorder = null;
    /* The border the microworld has, when microworldBorder == null. This border is assigned
     * at the initialization of the Container.
     */
    Border defaultMicroworldBorder;
//mv    protected Color borderColor = null;
//mv    protected NewRestorableImageIcon borderIcon = null;
//mv    protected int borderType = 0;
//mv    protected Color backgroundColor = null;
//mv    protected NewRestorableImageIcon backgroundIcon = null;
    /* The display mode oscf the icon, which is rendered in the Container's background.
     * Valid values: ImageChooser.NO_IMAGE, ImageChooser.CENTER_IMAGE, ImageChooser.FIT_IMAGE
     *               ImageChooser.TILE_IMAGE.
     */
//mv    protected int backgroundIconDisplayMode = ImageChooser.NO_IMAGE;
//mv    protected int backgroundType = UIDialog.ICON_COLORED_BACKGROUND;
//mv    protected int outerBorderType = 0;

    final static private Integer NOFRAME = new Integer(-1);
    /* The following 3 boolean variables are used inside listeners and help determine
     * whether the listener's actions is to be performed or ignored.
     */
    boolean servingComponentEvent = false, disableComponentMoveListener = false;
    boolean autoExpandOccured = false;
    /* 'autoExpandOccured2' is used to mark a drag session (a series of frame drag events
     * which occured between a single pair of mouse-pressed / mouse-released events). The
     * special about this session is that the microworld has expanded to the right or bottom.
     */
    boolean autoExpandOccured2 = false;

    protected UIDialog uiDialog = null;
    protected ESlateFileDialog fileDialog = null;
    /* The initial (upon start-up) value of the directory of the file dialog of the ESlateContainer */
    protected String currentFileDialogDir = null;
    /* The one and only file dialog used for all image/icon loading/saving in the E-Slate
     * Container environment. */
    public static ESlateFileDialog iconFileDialog = null;
    public String currentlyOpenMwdFileName = null;
    protected StructFile currentlyOpenMwdFile = null;
//// nikosM change
//    protected String webSite = null;
//// nikosM end of change

    protected boolean openFileRemote = false;

    protected Microworld microworld;
    private ESlateHandle containerHandle = null; //ESlate.registerPart(this);

//// nikosM change
    /* The name of the dir, where mirror files and directories for remote sites
     * are created in order to be accessed thourg the WebFileDialog.
     */
//    String webSitesLocalDirName;
//// nikosM end of change

    /* The Desktop Manager used by the Container.
     */
    DefaultDesktopManager containerDesktopManager = null;
    protected boolean menuBarVisible = true, componentBarEnabled = true;
    /* The currently active frame of the microworld.
     */
//0    ESlateInternalFrame activeFrame;
    /* ***  Microworld properties ***
     */
/*mv    protected boolean minimizeAllowed = true, maximizeAllowed = true, closeAllowed = true;
    protected boolean controlBarsVisible = true, nameChangeAllowed = true, controlBarTitleActive = true;
    protected boolean helpButtonVisible = true, pinButtonVisible = true, infoButtonVisible = true;
    protected boolean resizeAllowed = true, moveAllowed = true;
    protected boolean componentInstantiationAllowed = true, componentRemovalAllowed = true;
    protected boolean mwdBgrdChangeAllowed = true, mwdStorageAllowed = true;
    protected boolean mwdPinViewEnabled = true, mwdTitleEnabled = true, mwdPopupEnabled = true;
    protected boolean menuBarVisible = true, outlineDragEnabled, componentBarEnabled = true;
    protected boolean componentActivationMethodChangeAllowed = true, componentFrozenStateChangeAllowed = true;
    protected boolean mwdResizable = true, mwdAutoExpandable = false, mwdAutoScrollable = false;
    protected boolean componentsMoveBeyondMwdBounds = true, desktopDraggable = false;
    protected boolean menuSystemHeavyWeight = false;
*/
    protected boolean javaConsoleEnabled = true;

    boolean microworldChanged = false;
    ESlateListener componentNameListener = null;
    /* The 'componentVisibilityListener' is used in order to hide an ESlateInternalFrame, when
     * the components which is included by it becomes invisible and vice-versa. Without this
     * listener the ESlateInternalFrame remains empty on the screen, when its component becomes
     * invisible. This listener was commented out becuase of the following defficiency:
     * The component listeners are not notified on the main thread. I assume that they are notified
     * on the AWT thread. This has unexpected results, when the components setVisible() method is
     * called continuesly. Therefore, for the time being this functionality is commented out (vers. 0.9.7).
     */
// 0.9.7    ComponentAdapter componentVisibilityListener = null;

    // Logo machine
    protected MyMachine logoMachine;
    protected InterpEnviron logoEnvironment;
    protected InterpreterThread logoThread;
    protected Tokenizer tokenizer;
    LogoOutputConsole loc;
    PrimitiveGroupAddedListener primGrpListener;

    // Javascript
    /* This variables is set when the first time the ESlateContainer needs to use the
     * the Javascript scripting engine. This happens when:
     * - A javascript script is attached to a component event
     * - A microworld is loaded which contains javascript scripts for component events.
     */
    boolean javascriptInUse = false;
    /* The script engine for Javascript
     */
    JavaScriptManager jsScriptManager = null;

    //Java console
    ESlateConsoles consoles = null;
    private final java.io.PrintStream standardOut = System.out;
    private final java.io.PrintStream standardErr = System.err;
    /* The list of the microworlds that have been opened previously.
     */
    protected RecentMicroworldList microworldList;

    /* The history of the microworlds that have been opened in the Container. The
     * history is complete different from the microworlds that are stored in the
     * microworld list:
     * 1. because it keeps timing info, i.e. the sequence of the microworlds.
     * 2. a microworld can exist many times in the history, while only once in the "microworldList".
     */
    HistoryList mwdHistory;
////nikosM
    boolean updatingHistoryFlag;
/////nikosM end
//    boolean mwdHistoryDialogIsDisplayed = false;

    /* The list of the container listeners.
     */
    protected transient Array containerListeners = new Array();
    /* "EMPTY_FRAME" is a Frame which exists in any time in the container. It is not displayed. It's
     * sole use is to become acitvated, when none of the other frames is activated.
     */
//    public static final ESlateInternalFrame EMPTY_FRAME = new ESlateInternalFrame(); //"Empty Frame");
    /* Used when a frame is destroyed, cause Swing sends an non-understandable number of events in
     * an uncontrollable sequence.
     */
    /* The following string holds the path to the microworld file, which will be opened when the
     * Container initializes. This file name is passed from the ESlateContainerFrame class, which
     * receives it from the main() of the ESlateContainer. All these happen when the user double
     * clicks on an .mwd file. This variable has nothing to do with the preloaded mwd file, whose
     * path is saved in the 'container.properties' archieve.
     */
    public String startupMicroworldFileName = null;
    /* Marks the microworld loading time.
     */
    protected boolean loadingMwd = false;
    /** Flag that is true while a microworld is closing.
     */
    boolean closingMwd = false;
    /* The microworld file extensions used in the open and save file dialogs.
     */
    String[] mwdFileExtensions = null;
    /* Because this class has grown to become a monster and it is difficult to manage it in
     * JBuilder, we introduce a new class, where we put code that would normally end-up in the
     * ESlateContainer class. The name of the new class is ESlateContainerUtils. We construct an
     * instance of this class in the ESlateContainer's constructor and maintain it in the
     * 'containerUtils' variable.
     */
    protected ESlateContainerUtils containerUtils = new ESlateContainerUtils(this);
    /* This is the list of all the JFrames which are currently open and in which the user edits
     * scripts for the events of the components of the ESlateContainer. This list is used to avoid
     * having more than 1 open JFrames for the script of the same event of the same component.
     */
//    ListenerDialogList listenerDialogList = new ListenerDialogList(); // One script dialog is used since version 1.8, the ScriptDialog
    /* The one and only one event dialog, that can be instantiated during an ESlateContainer
     * session. This variable holds the existing eventDialog and becomes 'null', when this
     * dialog is destroyed. When there exists an event dialog and the user instantiates one
     * more for another component, then the contents of the current dialog are replaced by the
     * events of the other component.
     */
    // This class was invaldated since verison 0.9.6 of ESlateContainer
//    EventDialog eventDialog = null;
    /* These variables are used when a component is moved or resized beyond the left/top Container
     * borders. In this situation the Container space is augmented in steps equal to the grid step.
     * This way the relative positions of all of the components of the microworld to the grid are
     * maintained.
     */
    int jumpedPixelsX = 0, jumpedPixelsY;
    /* These menu items are used in the microworld pop-up menu.
     */
    JMenuItem toggleComponentBarItem, componentPasteItem, back, forward, pinViewItem;

    /* The one and only one instance of JEdit that can exist during an ESlateContainer session.
     */
//    protected JavaEditor javaEditor = null;
    /* This HashMap holds all the existing JavaCodeDialogInstances. It also binds each
     * instance with one and only one JEdit Buffer. So when the java script for a component
     * event is asked, a new Buffer may be created in JEdit or an existing Buffer may come up.
     */
    protected HashMap javaCodeDialogMap = new HashMap();
    /* The following HashMap contains the names of the layers of the 'currentMicroworld'. Each
     * name is attached to a uniqyue integer id, specifying the z-order of the layer.
     */
    protected LayerInfo mwdLayers = new LayerInfo(this);
    /* The active LayerDialog. I was forced to use a JFRame for this dialog instead of a
     * JDialog. This happened because the JEditList popup cannot be displayed beyond
     * the edje of a JDialog. So we keep track of the Layerdialog frame in order to bring
     * it in front of the other windows (if it exists), when it is started again.
     */
    LayerDialog layerDialog = null;
    /* The Container's split pane */
    protected ContainerSplitPane splitPane;
    /* The page setup info */
    PageInfo pageInfo = new PageInfo();

    /* The one and only ActionListener added to all the menu items which perform
     * microworld packaging.
     */
//    PackMicroworldActionListener packListener = null;
    /* This boolean variable is set while the container is being initializing,
       during start-up
     */
    boolean isContainerInitializing = false;
    /* This SecurityManager disables the installation of any security manager */
//    MySecurityManager securityManager = null;
    /* The current (default) microworld view */
    protected MicroworldCurrentView currentView = null;
    /* The list of the microworld's views */
    MicroworldViewList mwdViews = new MicroworldViewList(this);
    /* The 'Pack' menu in the microworld popup menu (the one displayed with right click */
    JMenu pack;
    public boolean componentInitializing = false;
    Point nextIconLocation = new Point(20, 30);
    ESlateInternalFrameUtils frameUtils = new ESlateInternalFrameUtils(this);
    SoundTheme currentSoundTheme;
    /* The central SoundFileChooser of E-Slate */
    SoundFileChooser soundFileChooser = null;
    /** The list of the registered SoundTheme files
     */
    ArrayList soundThemeFiles = new ArrayList();
    /** The list of custom profiles of microworld settings which have been registered to
     *  E-Slate.
     */
    MicroworldSettingsProfile[] mwdSettingsProfiles = null;
    /** This is the component that is displayed on-top of all the other components in a microworld,
     *  when a modal component is shown or while a microworld is being loaded. The second case is
     *  optional. See setDisplayGlassPaneWhileLoading(). When a modal component is shown, all the
     *  other components (except the modal) are covered by the GlassPane, which captures all mouse
     *  events, thus enabling modality. The GlassPane is a JPanel descendant and it visual
     *  characteristics and contents can be changed (see getGlassPane() in ESlateContainer). By
     *  default the GlassPane is semi-transparent, without any content.
     */
    GlassPane glass = null;
    /** The ESlateInternalFrame that hosts the GlassPane of the ESlateContainer. */
    private GlassInternalFrame glassFrame = null;
    /** Determines if the GlassPane will automatically be displayed while a microworld is being loaded. */
    private boolean glassPaneVisibleOnMicroworldLoad = false;

    /* "componentPanel" is the panel in which the component icons of the microworld are drawn.
     */
    ComponentPanel componentBar;
    /* "southPanel" contains the "componentPanel".
     */
    JPanel southPanel;
    JPanel northPanel;
//    long start = 0;
    /* This flag is used by E-Slate itself, in order to have access to its Priviledged API.
     * Whenever E-Slate calls a method which is part of the above API, is sets this flag to
     * true. After the method call returns, the flag is reset. Without setting this flag,
     * E-Slate is treated by the priviledged API as a foreign caller, and thus depending on
     * the settings of the microworld, the calls may be rejected.
     */
    boolean internalFunction = false;
    /* The PerformanceTimers/TimerGroups of E-Slate */
    PerformanceTimer saveTimer, mwdCloseTimer, viewApplyTimer, loadTimer;
    PerformanceTimerGroup loadTimerGroup;
    PerformanceTimer fileReadTimer, componentInstantiationTimer, frameIconTimer, scriptSoundLoadTimer, mwdPropertiesRestoreTimer, handleRegistrationTimer, mwdLoadedListenerTimer;
    static PerformanceTimer startupTimer;
    /** The Performance Manager dialog of E-Slate */
    private PerformanceManagerDialog perfMgrDialog = null;
    ESlateEventQueue eventQueue = null;
    ESlateRepaintManager repaintManager = null;
    Action microworldCloseAction = null, microworldLoadAction = null, microworldNewAction = null;
    Action microworldNewViewAction = null, microworldPrintAction = null, microworldPropertiesAction = null;
    Action microworldSaveAction = null, microworldSaveAsAction = null;
    Action microworldForwardAction = null, microworldBackAction = null;
    /* The one and only ActionListener added to all the menu items which perform
     * microworld packaging.
     */
    Action microworldPackAction = null;
    Action componentCutAction = null, componentCopyAction = null, componentPasteAction = null;
    Action componentRenameAction = null, componentRemoveAction = null;
    Action componentResizableAction = null, componentActivateOnClickAction = null;
//\    Action containerSettingsAction = null, gridEditorAction = null;
    Action microworldHelpEditorAction = null, openConsolesAction = null, plugEditorAction = null,showComponentBarAction;
//\    Action pmEditorAction = null, scriptEditorAction = null, viewEditorAction = null;
    Action containerHelpAction = null, componentHelpAction = null, componentInfoAction = null;
	Action componentSettingsAction = null;
    /** This boolean tracks whether E-Slate is in full screen mode.
     */
    private boolean fullScreenMode = false;
    /** The structure which holds the scripts which are not attached to component events.
     */
    protected ScriptMap scriptMap = new ScriptMap();
    
    /**
     * Tip panel for container, to show how to load a microworld.
     */
    private JToolTip tooltip;
    
    public static ESlateContainerFrame fr = null;

    public ESlateHandle getESlateHandle() {
        if (containerHandle == null) {
            containerHandle = ESlate.registerPart(this);
            try{
                containerHandle.setComponentName(containerBundle.getString("ESlate")); //"ESlateContainer"); //
            }catch (Exception exc) {
                System.out.println("Error while localizing the name of the E-Slate's handle");
            }
        }
        return containerHandle;
    }

    private static String getLocalizedContainerPropertiesFile() {
		if(Locale.getDefault().equals("el_GR")) {
			System.out.println("DBG: Selecting Greek properties file");
			//return "/gr/cti/eslate/base/container/container_EL.properties";
			return "container_EL.properties";
		} else {
			System.out.println("DBG: Selecting non-Greek properties file");
			//return "/gr/cti/eslate/base/container/container.properties";
			return "container.properties";
		}
	}

	/** Returns the Microworld which is currently open in E-Slate, if there is one.
     *  @see Microworld
     */
    public Microworld getMicroworld() {
        return microworld;
    }

    /** Returns the desktop used by E-Slate to layout the microworld.
     */
    public DesktopPane getDesktopPane() {
        return lc;
    }

    /** Returns the resource bundle of the ESlateContainer class.
     */
    public ResourceBundle getContainerBundle() {
        return containerBundle;
    }

    /** Retuns a structure that contains all the ESlateComponent's of the microworld.
     *  This structure enables the finding and managing the ESlateComponents of the
     *  microworld.
     */
    public MicroworldComponents getMicroworldComponentIndex() {
        return mwdComponents;
    }

    public ESlateContainerUtils getESlateUtilities() {
        return containerUtils;
    }

    public String[] getSupportedPrimitiveGroups() {
        return new String[] {
            "gr.cti.eslate.scripting.logo.ContainerPrimitives",
            "gr.cti.eslate.scripting.logo.ContainerComponentPrimitives"
        };
    }

/*    public ESlateContainer(long s)    {
        super(true);
        start = s;
//System.out.println("ET ESlateContainer(): " + (System.currentTimeMillis()-start));
        isContainerInitializing = true;
        container = this;
        initialize();
    }
*/
    public ESlateContainer()    {
        super(true);
//        isContainerInitializing = true;
        container = this;
//        initialize();
        //Help system care
        if (pathToContainerJar!=null)
        	System.setProperty("java.class.path",System.getProperty("java.class.path")+";"+pathToContainerJar+"\\help");
    }

    /** Use this constuctor to specify the component in which the
     *  ESlateContainer is added. This info is need mainly for proper
     *  cursor support.
     */
/*    public ESlateContainer(Component parentComponent) {
        super(true);
//        long millis1= System.currentTimeMillis();
        isContainerInitializing = true;
        container = this;
        this.parentComponent = parentComponent;
        initialize();
//        long millis2= System.currentTimeMillis();
//        System.out.println("Constructor end. Time: " + (millis2-millis1));
    }
*/

    public void initialize() {
//System.out.println("ET initialize start: " + (System.currentTimeMillis()-start));
//        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
//if        EMPTY_FRAME.container = this;

//        containerHandle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.ContainerPrimitives");
        isContainerInitializing = true;
        setLayout(new BorderLayout());
        lc = new DesktopPane(this);

        String OSName = System.getProperty("os.name");
        if (OSName.toLowerCase().startsWith("windows")) {
            containerDesktopManager = new ESlateContainerWindowsDesktopManager(this);
            ((JDesktopPane) lc).setDesktopManager(containerDesktopManager);
        }else
            containerDesktopManager = (DefaultDesktopManager) ((DesktopPane) lc).getDesktopManager();

      	lc.setOpaque(false);
        scrollPane = new ContainerScrollPane(lc); //, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setOpaque(false);
        ((DesktopPane) lc).setScrollPane(scrollPane);
        splitPane = new ContainerSplitPane(this);

        defaultMicroworldBorder = new CompoundBorder(scrollPane.getViewportBorder(), new EmptyBorder(0, 0, 0, 0)); //1,0,1,1));
        scrollPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
//                System.out.println("The scrollPane was resized");
                /* If there is no open microworld in the Container, then set the size of the DesktopPane
                 * to be equal to the size of the Viewport of the scrollPane.
                 */
                if (microworld == null) {
                    Dimension viewPortDim = scrollPane.getViewport().getSize();
//                    lc.setMaximumSize(viewPortDim);
//                    lc.setMinimumSize(viewPortDim);
//                    lc.setPreferredSize(viewPortDim);
//                    System.out.println("Setting DesktopPane size to : " + viewPortDim);
//                    scrollPane.revalidate();
                }else{
                    currentView.desktopHeight = lc.getSize().height;
                    currentView.desktopWidth = lc.getSize().width;
                    /* Check if the new size of the Desktop pane fits all the components. If not, then set
                     * the Desktop pane to as big as it is required to exactly contain all the components.
                     * If it does, then adjust the size of the Desktop pane to be equal to the size of the
                     * view port.
                     */
/*                    Dimension lcDim = scrollPane.getViewport().getSize();
                    int maxX=0, maxY=0, frameIndex;
                    for (int i=0; i<componentFrameIndex.size(); i++) {
                        Component comp;
                        if ((frameIndex = ((Integer) componentFrameIndex.at(i)).intValue()) != -1)
                            comp = (Component) componentFrames.at(frameIndex);
                        else
                            comp = (Component) components.at(i);

                        Rectangle compoBounds = comp.getBounds();
                        if (maxX < (compoBounds.x + compoBounds.width))
                            maxX = compoBounds.x + compoBounds.width;
                        if (maxY < (compoBounds.y + compoBounds.height))
                            maxY = compoBounds.y + compoBounds.height;
                    }
                    if (lcDim.width < maxX || lcDim.height < maxY) {
//                        System.out.println("Resizing Desktop pane to fit the components");
                        lcDim = new Dimension(maxX, maxY);
                        lc.setMaximumSize(lcDim);
                        lc.setMinimumSize(lcDim);
                        lc.setPreferredSize(lcDim);
                        scrollPane.revalidate();
                    }else{
                        Dimension viewPortDim = scrollPane.getViewport().getSize();
                        lc.setMaximumSize(viewPortDim);
                        lc.setMinimumSize(viewPortDim);
                        lc.setPreferredSize(viewPortDim);
                        scrollPane.revalidate();
                    }
*/
                }
            }
        });

//        EMPTY_FRAME.setVisible(false);
//        lc.add(EMPTY_FRAME, new Integer(1));

//        scrollPane.getViewport().setBackingStoreEnabled(true);

        locale = Locale.getDefault();
        containerBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ContainerBundle", locale);

        initializeActions();
        registerContainerActions();

        // Register to the Logo name space
        nameSupport.register(this, containerBundle.getString("ESlate")); // "Microworld");

        mwdComponents = new MicroworldComponents();
//        componentCount = 0;
//0        components = new Array();
//0        heavyweightComponents = new Array();
//0        componentFrameIndex = new Array();
//0        componentFrames = new Array();
        compSelectionHistory = new SelectionHistory();
        installedComponents = new InstalledComponentStructure();

////beginning of mantes code
        installedLAFs = new InstalledLookAndFeelStructure();
//// end of mantes code

//        installedComponentNames = new OrderedMap(new com.objectspace.jgl.LessString());
//        componentAvailability = new OrderedMap(new com.objectspace.jgl.LessString());
        webSites = new OrderedMap(new com.objectspace.jgl.LessString());
        webSiteAvailability = new OrderedMap(new com.objectspace.jgl.LessString());
        webSiteCommonDirs = new OrderedMap(new com.objectspace.jgl.LessString());
//0        eSlateHandles = new Array();


        if(!EMBEDDED_MODE) {
        	containerPropertiesFile = getContainerPropertiesFile();
            if (containerPropertiesFile == null) {
                try{
                    containerPropertiesFile = createContainerPropertiesFile();
                    System.out.println("DBG: Read properties file using createContainerPropertiesFile()");
                }catch (UnableToCreatePropertiesFileException exc) {
                    containerPropertiesFile = null;
                    System.out.println("DBG: could not read properties file");
                }
            }
	        try{
	            tmpDirName = ESlateContainerUtils.tmpDir.getCanonicalPath(); //new File(System.getProperty("user.home"))
	        }catch (IOException exc) {
	            tmpDirName = ESlateContainerUtils.tmpDir.toString(); //new File(System.getProperty("user.home")).toString();
	        }catch (NullPointerException exc) {
	        	tmpDirName = new File(System.getProperty("user.home")).toString();
	        }
	        tmpFile = new File(tmpDirName, "tmp.mwd");
        } 

//        northPanel = new JPanel(true);
//        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

        microworldList = new RecentMicroworldList(this);
        mwdHistory = new HistoryList();
///////nikosM
        updatingHistoryFlag=false;
//////nikosM end

        microworld = null;
        lc.setBackground(emptyContainerBgrColor);
//mv        currentView.backgroundColor = lc.getBackground();

        componentNameListener = new ESlateAdapter() {
            public void componentNameChanged(ComponentNameChangedEvent event) {
                ESlateContainer.this.componentNameChanged(event.getNewName(), event.getOldName());
            }
        };

        /* Add the right-button mose listener to the desktop pane.
         */
//        lc.addMouseListener(createContainerRightMouseButtonListener());
        MouseInputAdapter m = createDesktopDragMouseListener();
        lc.addMouseListener(m);
        lc.addMouseMotionListener(m);

        /* Due to compatibility reasons to the old AWT, Swing TextFields do not propagate the
         * VK_ENTER key events to the default buttons, when they exist in a JRootPane. Instead
         * ActionEvents are fired. However in the Dialogs we need the defaultButton (usually OK)
         * to be clicked when ENTER is pressed, no matter which component has the focus. Therefore
         * we borrowed this piece of code from the java docs of JTextField, which disables the
         * default VK_ENTER keystroke.
         */
        javax.swing.KeyStroke enter = javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        javax.swing.text.Keymap map = new javax.swing.JTextField().getKeymap();
        map.removeKeyStrokeBinding(enter);

        northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        add(northPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        add(southPanel, BorderLayout.SOUTH);

        webServerMicrosHandle= new WebServerAccessWorlds(ESlateContainer.this);

        /* Create and add the ancestor listener which creates and remove the modal
         * dialog window listener.
         */
        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                if (event.getID() == AncestorEvent.ANCESTOR_ADDED) {
                    findParentComponent();
                    /*parentComponent.invalidate();
                    parentComponent.validate();
                    if(parentFrame != null) {
                    	parentFrame.invalidate();
                    	parentFrame.validate();
                    }*/
                    
                    
                    /*if(parentComponent instanceof JApplet) {
                    	((JApplet) parentComponent).resize(parentComponent.getSize().width+1, parentComponent.getSize().height+1);
                    }
                    parentComponent.setSize(parentComponent.getSize().width+1, parentComponent.getSize().height+1);
                    if(parentComponent instanceof JApplet) {
                    	((JApplet) parentComponent).resize(parentComponent.getSize().width-1, parentComponent.getSize().height-1);
                    }
                    parentComponent.setSize(parentComponent.getSize().width-1, parentComponent.getSize().height-1);
                    parentComponent.repaint();
                    parentComponent.invalidate();
                    parentComponent.validate();*/

//                  parentComponent = container.getTopLevelAncestor();
//                    if (Frame.class.isAssignableFrom(parentComponent.getClass()))
//                        parentFrame = (Frame) parentComponent;
                }
            }
            public void ancestorRemoved(AncestorEvent event) {
            }
            public void ancestorMoved(AncestorEvent event) {
            }
        });

        PerformanceManager.getPerformanceManager().addPerformanceListener(new PerformanceAdapter() {
            public void performanceManagerStateChanged(java.beans.PropertyChangeEvent e) {
                boolean b = ((Boolean) e.getNewValue()).booleanValue();
                if (b)
                    initializePerformanceManager();
            }
        });
        if (PerformanceManager.getPerformanceManager().isEnabled())
            initializePerformanceManager();


        // Create a custom Event Queue for the E-Slate applications
        eventQueue = new ESlateEventQueue();
        if(!EMBEDDED_MODE) {
        	Toolkit.getDefaultToolkit().getSystemEventQueue().push(eventQueue);
        } else {
        	/*AccessController.doPrivileged(new PrivilegedAction() {
        		public Object run() {
        			Toolkit.getDefaultToolkit().getSystemEventQueue().push(eventQueue);
        			return null;
        		}
        	});*/
        }

        repaintManager = new ESlateRepaintManager(this);
        RepaintManager.setCurrentManager(repaintManager);
//System.out.println("ET initialize end: " + (System.currentTimeMillis()-start));
        tooltip=new JToolTip();
        tooltip.setTipText(containerBundle.getString("loadmwtip"));
        FontMetrics fm=getFontMetrics(tooltip.getFont());
        tooltip.setSize((int) (fm.stringWidth(tooltip.getTipText())/2.5),fm.getHeight()*6);
    }

    /**
     * Finds the top-level AWT container of the ESlateContainer panel.
     */
    void findParentComponent() {
        if (parentComponent != null) return;
        parentComponent = container.getTopLevelAncestor();
        if (Frame.class.isAssignableFrom(parentComponent.getClass()))
            parentFrame = (Frame) parentComponent;
    }

    void activateComponent(int x, int y) {
        if (microworld == null) return;
		// First check if the point belongs to the currently active component.
		// If it does, the do nothing.
/*		ESlateComponent currActiveComp = mwdComponents.activeComponent;
		if (currActiveComp != null && currActiveComp.frame != null) {
			if (currActiveComp.frame.contains(x - currActiveComp.frame.getX(), y - currActiveComp.frame.getY())) {
				return;
			}
		}
*/
//System.out.println("lc.getComponentAt(): " + lc.getComponentAt(x, y));
		// The desktop pane does all the work of finding the top-most component at x, y
		Component comp = lc.getComponentAt(x, y);
		if (comp != null) {
			if (comp instanceof ESlateInternalFrame) {
				ESlateInternalFrame fr = (ESlateInternalFrame) comp;
				if (!fr.isActive() && fr.isComponentActivatedOnMouseClick()) {
					try{
						fr.setActive(true);
					}catch (Throwable thr) {thr.printStackTrace();}
				}
			}
		}

/*        int compCount = mwdComponents.size();
        ESlateComponentArray compsToBeActivated = new ESlateComponentArray();
        for (int i=0; i<compCount; i++) {
            ESlateComponent comp = mwdComponents.components.get(i);
            if (comp.isIcon()) continue;
            if (comp.frame != null) {
				if (comp.isActive()) continue; // We've checked the active component above
//System.out.println("comp: " + comp + ", contains: " + comp.frame.contains(x - comp.frame.getX(), y - comp.frame.getY()));
//System.out.println("x: " + x + ", y: " + y + ", comp.frame.getX(): " + comp.frame.getX() + ", comp.frame.getY(): " + comp.frame.getY());
//System.out.println("x - comp.frame.getX()" + (x - comp.frame.getX()) + ", y - comp.frame.getY(): " + (y - comp.frame.getY()));
                if (comp.frame.contains(x - comp.frame.getX(), y - comp.frame.getY()))
                    if (comp.frame.isComponentActivatedOnMouseClick())
                        compsToBeActivated.add(comp);
            }
        }
        ESlateComponent compToBeActivated = null;
		int compToBeActivatedIndex = -1;
        for (int i=0; i<compsToBeActivated.size(); i++) {
            ESlateComponent comp = compsToBeActivated.get(i);
			if (compToBeActivated == null) {
				compToBeActivated = comp;
				compToBeActivatedIndex = lc.getIndexOf(comp.frame);
			}else{
				if (compToBeActivatedIndex > lc.getIndexOf(comp.frame)) {
					compToBeActivated = comp;
					compToBeActivatedIndex = lc.getIndexOf(comp.frame);
				}
			}
System.out.println("comp: " + comp + ", index: " + lc.getIndexOf(comp.frame));
*/
/*            if (compToBeActivated == null) {
                compToBeActivated = comp;
                continue;
            }
            if (compToBeActivated.getLayer() < comp.getLayer())
                compToBeActivated = comp;
            else if (compToBeActivated.getLayer() == comp.getLayer()) {
                if (mwdComponents.activeComponent == comp)
                    compToBeActivated = comp;
            }
*/
/*        }
        if (compToBeActivated != null) {
            try{
//System.out.println("Activating " + compToBeActivated);
                compToBeActivated.setActive(true);
            }catch (Throwable thr) {thr.printStackTrace();}
        }
*/
    }

    /**
     * Fetchs the top-most (deepest) lightweight component that is interested
     * in receiving mouse events.
     */
/*    public java.awt.Component getMouseEventTrg(int x, int y, boolean includeSelf) {
        ESlateContainerFrame fr = (ESlateContainerFrame) parentComponent;
	int ncomponents = fr.getComponentCount(); //this.ncomponents;
        Component component[] = fr.getComponents(); //this.component;
	for (int i = 0 ; i < ncomponents ; i++) {
	    Component comp = component[i];
	    if ((comp != null) && (comp.contains(x - comp.getLocation().x, y - comp.getLocation().y)) &&
		(comp.getPeer() instanceof java.awt.peer.LightweightPeer) &&
		(comp.isVisible() == true)) {
		// found a component that intersects the point, see if there is
		// a deeper possibility.
		if (comp instanceof Container) {
		    Container child = (Container) comp;
                    System.out.println("Calling getMouseEventTarget() on : " + child.getClass());
//		    Component deeper = child.getMouseEventTarget(x - child.x, y - child.y, includeSelf);
//		    if (deeper != null) {
//			return deeper;
                        return null;
//		    }
		} else {
*/
/*		    if ((comp.mouseListener != null) ||
			((comp.eventMask & AWTEvent.MOUSE_EVENT_MASK) != 0) ||
			(comp.mouseMotionListener != null) ||
			((comp.eventMask & AWTEvent.MOUSE_MOTION_EVENT_MASK) != 0)) {
*/
			// there isn't a deeper target, but this component is a target
///			return comp;
//		    }
/*		}
	    }
	}

	boolean isPeerOK;
	boolean	isMouseOverMe;
	boolean	isMouseListener = false;
	boolean	isMotionListener = false;

	isPeerOK = (getPeer() instanceof java.awt.peer.LightweightPeer) || includeSelf;
	isMouseOverMe = contains(x,y);
*/
/*	isMouseListener = (mouseListener != null) ||
			  ((eventMask & AWTEvent.MOUSE_EVENT_MASK) != 0);
	isMotionListener = (mouseMotionListener != null) ||
			   ((eventMask & AWTEvent.MOUSE_MOTION_EVENT_MASK) != 0);
*/
	// didn't find a child target, return this component if it's a possible target
/*	if ( isMouseOverMe && isPeerOK && (isMouseListener || isMotionListener) ) {
	    return this;
	}
	// no possible target
	return null;
    }
*/

    /**
     * Called when the name of a component changes and updates the ESlateContainer's UI.
     * @param newName the new name of the component
     * @param oldName the previous name of the component
     */
    protected void componentNameChanged(String newName, String oldName) {
        if (microworld == null || loadingMwd)
            return;
//                System.out.println("newName: " + newName + ", oldName: " + oldName);
//                Thread.currentThread().dumpStack();


/*
        // Update the component icon on the component bar, if the bar is visible
        if (componentBar != null && componentBar.getParent() != null)
            componentBar.setIconName(newName, oldName);
//                System.out.println("newName: " + newName + ", oldName: " + oldName);
        if (newName.equals(oldName))
            return;

        // Find the menu item named "oldName" and change its name to "newName"
        if (menuPanel != null) {
            javax.swing.MenuElement[] elements = menuPanel.componentsMenu.getPopupMenu().getSubElements();
            JCheckBoxMenuItem targetItem = null;
            for (int i=0; i<elements.length; i++) {
                if (((JCheckBoxMenuItem) elements[i]).getText().equals(oldName)) {
                    targetItem = (JCheckBoxMenuItem) elements[i];
                    break;
                }
            }

            if (targetItem != null)
                targetItem.setText(newName);
            else{
                if (!loadingMwd)
                    System.out.println("Serious inconsistency error in ComponentNameChangedListener componentNameChanged(): Can't find menu item to rename");
            }
        }
*/

        // If the component is an invisible one, then change the name in its text area
        ESlateComponent eslateComponent = mwdComponents.getComponent(newName);
        if (eslateComponent != null && eslateComponent.icon != null)
            eslateComponent.icon.setName(newName);

        /* Rename the ComponentViewInfo for this component in all the existing views.
         * The 'currentView' need not be updated, since it does not carry any
         * ComponentViewInfo.
         */
        int viewNum = mwdViews.viewList.length;
        for (int i=0; i<viewNum; i++)
            mwdViews.viewList[i].renameComponent(eslateComponent, oldName, newName);


        /* Rename the ScriptListenerHandleNode for this handle, if the listenerTree of the
         * componentScriptListeners has been initialized.
         */
        if (!loadingMwd && componentScriptListeners != null && eslateComponent != null)
            componentScriptListeners.componentRenamed(eslateComponent.handle);


        // Rename the entry for this component in the componentScriptListeners HashMap, if one exists
/*                if (!isLoadingMwd && componentScriptListeners.count(oldName) != 0) {
                    Object value = componentScriptListeners.remove(oldName);
                    System.out.println("componentScriptListeners name change: " + " value: " + value + ", newName: " + newName);
                    componentScriptListeners.put(newName, value);
                }
*/
    }

    protected void initLogoEnvironment() {
//        System.out.println("Initializing the logo runtime environment");
        // Initialize the Logo machine
        tokenizer = new Tokenizer();
        Console logoConsole = new LogoConsole();
        logoMachine = new MyMachine(logoConsole, new virtuoso.logo.PrimitiveGroup[0]);
        logoThread = new InterpreterThread(logoMachine, logoConsole);
        logoEnvironment = new InterpEnviron(logoThread);
        try{
            logoMachine.prepare();
        }catch (virtuoso.logo.SetupException exc) {
            System.out.println("Unable to set up the logo machine: " + exc.getMessage());
        }

        // The following statement has substituted the commented block below since 0.9.8.1.
        logoMachine.loadDefaultPrimitives();

        try{
            logoMachine.componentPrimitives=new ComponentPrimitives();
            logoMachine.installPrimitives(logoMachine.componentPrimitives);
/*            logoMachine.loadPrimitives("virtuoso.logo.lib.StandardPrimitives");
            logoMachine.loadPrimitives("virtuoso.logo.lib.ExtFilePrimitives");
            logoMachine.loadPrimitives("virtuoso.logo.lib.FilePrimitives");
            logoMachine.loadPrimitives("virtuoso.logo.lib.LibraryPrimitives");
            logoMachine.loadPrimitives("virtuoso.logo.lib.LoaderPrimitives");
            logoMachine.loadPrimitives("virtuoso.logo.lib.NetworkPrimitives");
            logoMachine.loadPrimitives("virtuoso.logo.lib.ShellPrimitives");
            logoMachine.loadPrimitives("virtuoso.logo.lib.ThreadPrimitives");
            logoMachine.loadPrimitives("gr.cti.eslate.scripting.logo.ReflectionPrimitives");

            System.out.println("Loading ComponentPrimitives");
            logoMachine.loadPrimitives("gr.cti.eslate.scripting.logo.ComponentPrimitives"); // was ComponentPrimitives
            logoMachine.loadPrimitives("gr.cti.eslate.scripting.logo.ContainerPrimitives");
*/
        }catch (LanguageException exc) {
            System.out.println("Unable to load primitive group: " + exc.getMessage());
        }

//        ComponentPrimitives cprim = (ComponentPrimitives) logoMachine.getPrimitiveGroup("gr.cti.eslate.scripting.logo.ComponentPrimitives");
//        logoMachine.componentPrimitives = cprim;
//        System.out.println("initLogoEnvironment(): " + logoMachine.componentPrimitives);
        if (consoles == null)
            initializeESlateConsoles();
        loc = consoles.getLogoConsole();// new LogoOutputConsole();
        consoles.setLogoConsoleEnabled(true);
        logoThread.setOutStream(new CaselessString("Logo Output Console"), loc);
    }

    void registerJavascriptVariables() {
        /* Register the ESlateContainer and ESlateMicroworld beans. These correspond
         * to the current ESlateContainer and the currentMicroworld, which runs in it.
         * They are registered under difficult to use names, so that the entries in the
         * BSF registry won't be overriden by mistake. Before registering them we test
         * if they already exist in the registry. If the ESlateContainer already exists,
         * we don't re-register it. If the ESlateMicroworld is already registered, then
         * it points to the previous microworld. In this case we unregister the bean
         * and then re-register the current ESlateMicroworld.
         */
        javascriptInUse = true;
        if (jsScriptManager == null)
            jsScriptManager = ScriptManager.getScriptManager().getJavaScriptManager(getESlateHandle());
        if (consoles == null)
            initializeESlateConsoles();
        consoles.setJavascriptConsoleEnabled(true);
        ScriptManager scriptMgr = ScriptManager.getScriptManager();
        if (scriptMgr.lookup("_theCurrentESlateContainer") == null) {
//            System.out.println("Registering the ESlateContainer to ScriptManager");
            scriptMgr.registerName("_theCurrentESlateContainer", this);
        }
        if (scriptMgr.lookup("_theCurrentESlateMicroworld") != null) {
//            System.out.println("Unregistering the current ESlateMicroworld from ScriptManager");
            scriptMgr.unregisterName("_theCurrentESlateMicroworld");
        }
//        System.out.println("registerJavascriptVariables() currentMicroworld: " + currentMicroworld);
        if (microworld != null) {
            scriptMgr.registerName("_theCurrentESlateMicroworld", microworld.eslateMwd);
        }
//            System.out.println("Container registered? " + scriptMgr.lookup("_theCurrentESlateContainer"));
//            System.out.println("Microworld registered? " + scriptMgr.lookup("_theCurrentESlateMicroworld"));
    }

    void unregisterMicroworldJavascriptVariable() {
        // Unregister the microworld which closes from the BSF manager.
        ScriptManager scriptMgr = ScriptManager.getScriptManager();
        if (scriptMgr.lookup("_theCurrentESlateMicroworld") != null) {
//            System.out.println("Unregistering the current ESlateMicroworld from ScriptManager");
            scriptMgr.unregisterName("_theCurrentESlateMicroworld");
        }
    }

    /* Performs last-minute memory cleanup.
     */
    protected void finalizeContainer() {
        if (containerHandle != null) containerHandle.dispose();
        if (logoMachine != null) logoMachine.tearDown();
    }

    /* Resets the container environment, i.e. all the variables that adjust the functionality
     * and the look of the environment.
     */
    protected void resetContainerEnv() {
//System.out.println("resetContainerEnv() loadingMwd: " + loadingMwd);
        servingComponentEvent = false;
        disableComponentMoveListener = false;

//0        activeFrame = null;
        microworld.eslateMwd.setSelectedComponent(null);
//        System.out.println("setSelectedComponent 2");
        setMoveAllowedInternal(true);
/*mv        minimizeAllowed = true;
        maximizeAllowed = true;
        closeAllowed = true;
        controlBarsVisible = true;
        nameChangeAllowed = true;
        controlBarTitleActive = true;
        helpButtonVisible = true;
        pinButtonVisible = true;
        infoButtonVisible = true;
        resizeAllowed = true;
        setMoveAllowed(true);
        componentInstantiationAllowed = true;
        componentRemovalAllowed = true;
        mwdBgrdChangeAllowed = true;
        mwdStorageAllowed = true;
        mwdPinViewEnabled = true;
        mwdTitleEnabled = true;
        mwdPopupEnabled = true;
        mwdPopupEnabled = true;
        menuBarVisible = true;
        setOutlineDragEnabled(false);
        componentFrozenStateChangeAllowed = true;
        componentActivationMethodChangeAllowed = true;
        setMenuSystemHeavyWeight(false);
*/
        // Remove the primitive group listener
        if (primGrpListener != null)
            microworld.eslateMwd.removePrimitiveGroupAddedListener(primGrpListener);

        if (javascriptInUse) {
            unregisterMicroworldJavascriptVariable();
            jsScriptManager = null;
            javascriptInUse = false;
            ScriptManager.deactivateScriptManager();
        }

//        System.out.println("Num of handles: " + currentMicroworld.getHandles().length);
//        System.out.println("Num of handles in default mwd: " + currentMicroworld.getDefaultMicroworld().getHandles().length);

//m        getESlateHandle().setESlateMicroworld(null);
        // Remove the script listeners which were added to the Microworld
        componentScriptListeners.removeScriptListeners(microworld.eslateMwd.getESlateHandle());
        // Also remove the script listeners which were added to E-Slate. The script listeners
        // attached to the Microworld, are saved in the ScriptListenerMap with the handle of
        // the ESlateContainer, rather that the handle of the ESlateMicroworld of Microworld.
        componentScriptListeners.removeScriptListeners(containerHandle);

        componentScriptListeners.clear();
        scriptMap.clear(this);
        soundListenerMap.clear();

        containerHandle.remove(microworld.eslateMwd.getESlateHandle());
//        System.out.println("Num of handles: " + currentMicroworld.getHandles().length);
//        System.out.println("Num of handles in default mwd: " + currentMicroworld.getDefaultMicroworld().getHandles().length);
//        ESlateHandle[] h = currentMicroworld.getDefaultMicroworld().getHandles();
//        for (int i=0; i<h.length; i++)
//            System.out.println("Handle name: " + h[i].getComponentName());

//        System.out.println("Microworld environment: " + currentMicroworld.getMicroworldEnvironment());
        microworld.eslateMwd.setMicroworldEnvironment(null);
//        System.out.println("Microworld environment: " + currentMicroworld.getMicroworldEnvironment());
        microworld.eslateMwd.dispose();
        microworld.eslateMwd = null;
        Microworld tmp = microworld;
        microworld = null;
        PerformanceManager.getPerformanceManager().setESlateMicroworld(null);
        tmp.fireMicroworldClosed(new MicroworldEvent(tmp));
        tmp.removeMicroworldListeners();
        tmp = null;
        /* The default microworld is the currentMicroworld, which has been disposed. To
         * avoid keeping a reference to a disposed microworld, set the ESlateMicroworld's
         * default microworld to null.
         */
//default ESlateMicroworld.setDefaultMicroworld(null);
        try{
            if (currentlyOpenMwdFile != null)
                currentlyOpenMwdFile.close();
        }catch (Throwable thr) {
            System.out.println("Unable to close the microworld's file");
        }
        currentlyOpenMwdFileName = null;
        currentlyOpenMwdFile = null;

//// nikosM
        webServerMicrosHandle.webSite = null;
//// nikosM end
        openFileRemote = false;

        setMicroworldBackground(UIDialog.ICON_COLORED_BACKGROUND, emptyContainerBgrColor, null, ImageChooser.NO_IMAGE);
        setMicroworldBorder(UIDialog.NO_BORDER, null, null, null);
        setOuterBorder(UIDialog.OUTER_BORDER_NONE);


        currentView.backgroundColor = lc.getBackground();
        setContainerTitle(null);
        if (logoMachine != null)
            logoMachine.componentPrimitives.theESlateMicroworld = null;
        /* Close the JavaEditor and get rid of all the JavaCodeDialogs.
         */
/*        if (eventDialog != null && eventDialog.javaEditor != null) {
            eventDialog.javaEditor.close();
            eventDialog.javaCodeDialogMap.clear();
            eventDialog.javaEditor = null;
        }
*/
        /* If the scrollbars are visible adjust the size of the DesktopPane
         */
        mwdLayers.resetInternal();
        if (layerDialog != null) {
            layerDialog.dispose();
            layerDialog = null;
        }

        setComponentsMoveBeyondMwdBoundsInternal(true);
        setMwdAutoExpandableInternal(false);
        setMwdAutoScrollableInternal(false);
        setDesktopDraggableInternal(false);
//        setESlateComponentBarEnabled(true);

        // The phollowing to statements make the scrollpane view shrink, if this is needed.
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Dimension newSize = new Dimension(20, 20); //scrollPane.getViewport().getExtentSize();
        lc.setMinimumSize(newSize);
        lc.setPreferredSize(newSize);
        lc.setMaximumSize(new Dimension(1300, 1300));
        scrollPane.getViewport().setViewPosition(new Point(0, 0));

        if (currentView.mwdResizable) {
            scrollPane.invalidate();
            scrollPane.doLayout();
            scrollPane.revalidate();
        }
        //if an mwd is being loading, then this mwd will set the resizability of the microworld
        if (!currentView.mwdResizable && !loadingMwd)
            setMwdResizableInternal(true);

        currentView = null;
//        if (menuPanel != null)
//            menuPanel.microworldViewMenu.removeAll();
        mwdViews.clearInternal();
		// Reset the microworld icon
		if (parentFrame != null) parentFrame.setIconImage(ESLATE_LOGO.getImage());
        setMicroworldChanged(false);
    }

    /* readContainerProperties() is called here, because if a microworld is to be preloaded
     * the ESlateContainer has to be added to its Java container, before the microworld is
     * loaded. Otherwise no off-screen images will be created for the new microworld and this
     * causes serious problems to various components, e.g. Canvas, Query. addNotify() is
     * guaranteed to be called after the ESlateContainer component is added to its Java container.
     */
    public void addNotify() {
        super.addNotify();
        findParentComponent();
        // Before Java 1.4.2, if a microworld was to be loaded after starting the environment, this would take place
        // in initialize2() during the execution of addNotify(). In Java 1.4.2 this caused 2 problems: 1st if the
        // microworld contained menus, these would not display their pop-ups until another window was activated and
        // then the environment's window was re-activated. 2nd the microworld load progress dialog would not display
        // the proper content's (when these are customized).
        // To overcome this initialize2() does not load anymore the start-up microworld. Also initialize to is called
        // by addNotify() only when a start-up microworld is not specified. If a start-up microworld is specified,
        // it's loading occurs in main(), where initialize2() is also called.
        if (startupMicroworldFileName == null)
            initialize2();
        /* papakiru: sound theme is not needed anymore */
        // playSystemSound(SoundTheme.START_SOUND);
        isContainerInitializing = false;
//System.out.println("ET addNotify() end: " + (System.currentTimeMillis()-start));
    }

	public void initialize2() {
        // Read the container properties file
        if (!readContainerProperties()) {
            // Set an adequate container size
            //Container topLevelAncestor = this.getTopLevelAncestor();
            //if (startupMicroworldFileName == null && topLevelAncestor != null && !(topLevelAncestor instanceof ESlateContainerWindow))
                //topLevelAncestor.setBounds(0, 0, 500, 500);
        }
//        container.evaluateJSScript("java.lang.System.out.println(\"Testing\");");

//        boolean frameAlreadyShown = false;
        //closeSplashWindow();

        //Create the installed and available components' menu items
//        componentNew.populateComponentPaletteMenu();
        //Create the web site menu items in the LOAD and SAVE AS menus
//        createWebSiteMenuItems();

        /* Assign the variable which holds the name of the directory, where
         * temporary files and directories which mirror remote directories and
         * files are created.
         */
		if (!EMBEDDED_MODE) {
			// //nikosM

			webServerMicrosHandle.webSitesLocalDirName = tmpDirName
					+ System.getProperty("file.separator")
					+ containerBundle.getString("WebSiteLocalDirName");
			/*
			 * System.out.println(tmpDirName);
			 * System.out.println(System.getProperty("file.separator"));
			 * System.out
			 * .println(containerBundle.getString("WebSiteLocalDirName"));
			 */
			/*
			 * If this directory does not exist, then create it.
			 */
			File webSitesLocalDir = new File(
					webServerMicrosHandle.webSitesLocalDirName);
			if (!webSitesLocalDir.exists())
				webSitesLocalDir.mkdir();
			// Recreate the web site mirror dirs, in case they have been
			// deleted.
			webServerMicrosHandle.updateWebSiteMirrorRootDirs();
			// //nikosM end
		}

        /* When no start-up microworld is specified and there is no microworld to preload, the container
         * starts containing a new, empty microworld.
         */
        if (microworld == null && startupMicroworldFileName == null) {
        	System.out.println("DBG: no start-up microworld is specified");
            createNewMicroworld();
        }

//        if (!frameAlreadyShown);
//            closeSplashWindow();

        //readESlateSoundTheme();

//        System.out.println("initialize2() end : " + System.currentTimeMillis());
    }


    public final void setActiveComponentHighlighted(boolean highlighted) {
        if (microworld == null) return;
        microworld.checkSettingChangePriviledge();
        setActiveComponentHighlightedInternal(highlighted);
    }
    private final void setActiveComponentHighlightedInternal(boolean highlighted) {
        if (microworld == null) return;
        if (currentView.activeComponentHighlighted == highlighted) return;
        currentView.activeComponentHighlighted = highlighted;
        ESlateComponent[] visualComponents = mwdComponents.getVisualComponents();
        for (int i=0; i<visualComponents.length; i++)
            visualComponents[i].frame.setActiveStateDisplayed(highlighted);
        setMicroworldChanged(true);
    }
    public final boolean isActiveComponentHighlighted() {
        return currentView.activeComponentHighlighted;
    }

    /** Adjusts whether the desktop should automatically scroll when a component
     *  is activated, so that it is contained in the visible part of the scrollpane's
     *  view.
     */
    public final void setEnsureActiveComponentAlwaysVisible(boolean ensure) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setEnsureActiveComponentAlwaysVisibleInternal(ensure);
    }
    private final void setEnsureActiveComponentAlwaysVisibleInternal(boolean ensure) {
        if (microworld == null) return;
        if (currentView.ensureActiveComponentVisible == ensure) return;
        currentView.ensureActiveComponentVisible = ensure;
        setMicroworldChanged(true);
    }
    public final boolean isEnsureActiveComponentAlwaysVisible() {
        return currentView.ensureActiveComponentVisible;
    }

    /** Adjusts whether microworld printing is allowed in the current microworld view.
     */
    public final void setMicroworldPrintAllowed(boolean allowed) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setMicroworldPrintAllowedInternal(allowed);
    }
    private final void setMicroworldPrintAllowedInternal(boolean allowed) {
        if (microworld == null) return;
        if (currentView.mwdPrintAllowed == allowed) return;
        currentView.mwdPrintAllowed = allowed;
        setMicroworldChanged(true);
    }
    /** Reports if microworld printing is allowed in the current microworld view.
     */
    public final boolean isMicroworldPrintAllowed() {
        return currentView.mwdPrintAllowed;
    }

    /** Adjusts whether page set-up for printing is allowed in the current microworld view.
     */
    public final void setPageSetupAllowed(boolean allowed) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setPageSetupAllowedInternal(allowed);
    }
    private final void setPageSetupAllowedInternal(boolean allowed) {
        if (microworld == null) return;
        if (currentView.mwdPageSetupAllowed == allowed) return;
        currentView.mwdPageSetupAllowed = allowed;
        setMicroworldChanged(true);
    }
    /** Reports if page set-up for printing is allowed in the current microworld view.
     */
    public final boolean isPageSetupAllowed() {
        return currentView.mwdPageSetupAllowed;
    }

    /** Adjusts whether component printing is allowed in the current microworld view.
     */
    public final void setComponentPrintAllowed(boolean allowed) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setComponentPrintAllowedInternal(allowed);
    }
    private final void setComponentPrintAllowedInternal(boolean allowed) {
        if (microworld == null) return;
        if (currentView.componentPrintAllowed == allowed) return;
        currentView.componentPrintAllowed = allowed;
        setMicroworldChanged(true);
    }
    /** Reports if component printing is allowed in the current microworld view.
     */
    public final boolean isComponentPrintAllowed() {
        return currentView.componentPrintAllowed;
    }

    /** Adjusts whether component minimize is allowed in the current microworld view.
     */
    public final void setComponentMinimizeAllowed(boolean allowed) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setComponentMinimizeAllowedInternal(allowed);
    }
    private final void setComponentMinimizeAllowedInternal(boolean allowed) {
        if (microworld == null) return;
        if (currentView.componentMinimizeAllowed == allowed) return;
        currentView.componentMinimizeAllowed = allowed;
        ESlateInternalFrame[] frames = container.mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++)
            frames[i].setIconifiable(allowed);
        setMicroworldChanged(true);
    }
    /** Reports if component minimize is allowed in the current microworld view.
     */
    public final boolean isComponentMinimizeAllowed() {
        return currentView.componentMinimizeAllowed;
    }

    /** Adjusts whether component maximize is allowed in the current microworld view.
     */
    public final void setComponentMaximizeAllowed(boolean allowed) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setComponentMaximizeAllowedInternal(allowed);
    }
    private final void setComponentMaximizeAllowedInternal(boolean allowed) {
        if (microworld == null) return;
        if (currentView.componentMaximizeAllowed == allowed) return;
        currentView.componentMaximizeAllowed = allowed;
        ESlateInternalFrame[] frames = container.mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++)
            frames[i].setMaximizable(allowed);
        setMicroworldChanged(true);
    }
    /** Reports if component maximize is allowed in the current microworld view.
     */
    public final boolean isComponentMaximizeAllowed() {
        return currentView.componentMaximizeAllowed;
    }

    protected void validateTree() {
    	synchronized(getTreeLock()) {
            super.validateTree();
        }
    }

    public void setPinViewVisible(boolean visible) {
        if (visible) {
            if (microworld != null) {
                microworld.eslateMwd.getPlugViewWindow().setVisible(true);
                if (microworld.eslateMwd.getPlugViewWindow().getState() == JFrame.ICONIFIED)
                    microworld.eslateMwd.getPlugViewWindow().setState(JFrame.NORMAL);
                microworld.eslateMwd.getPlugViewWindow().toFront();
            }
        }else{
            if (microworld.eslateMwd != null)
                microworld.eslateMwd.getPlugViewWindow().dispose();
        }
    }

    public boolean isPinViewVisible() {
        if (microworld.eslateMwd != null)
            return microworld.eslateMwd.getPlugViewWindow().isShowing();
        return false;
    }

    /* Gets the name under which a component is registered in the Container. The
     * class of the component is supplied.
     */
    public String getNameFromInstalledComponent(Class componentClass) {
        String componentClassName = componentClass.getName();
        String componentName = installedComponents.getName(componentClassName);
        return componentName;
/*        if (installedComponentNames.countValues(componentClassName) == 0)
            return null;
        return (String) installedComponentNames.keys(componentClassName).nextElement();
*/
    }

    /* Given the name under which a component is registered in the ESlateContainer, return
     * the Java class of the component.
     */
    public Class getRegisteredComponentClass(String registeredComponentName) {
/*        if (installedComponentNames.count(registeredComponentName) == 0)
            return null;
        try{
            return Class.forName((String) installedComponentNames.get(registeredComponentName));
        }catch (ClassNotFoundException exc) {
            return null;
        }
*/
        String className = installedComponents.getClassName(registeredComponentName);
        if (className == null)
            return null;
        try{
            return Class.forName(className);
        }catch (ClassNotFoundException exc) {
            return null;
        }
    }


    public ESlateComponent createComponent(String componentClassName, Point location, Dimension size) {
        if (microworld == null) return null;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (!loadingMwd)
            microworld.checkActionPriviledge(microworld.isComponentInstantiationAllowed(), "componentInstantiationAllowed");

//        if (lc.isModalFrameVisible()) return null;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
//        if (componentClassName.equals("gr.cti.eslate.ICEBrowser.ICEBrowserWrapper"))
//            enableSecurityManager();
//0        Component[] comp = instantiateComponent((parentComponent==null) ? container : parentComponent, componentClassName);
        Object object = instantiateComponent(container, componentClassName);
//        if (componentClassName.equals("gr.cti.eslate.ICEBrowser.ICEBrowserWrapper"))
//            disableSecurityManager();

        ESlateInternalFrame w = null;
        UILessComponentIcon icon = null;
        if (object != null) {
            // Store the heavyweigh component, if exists.
//0            heavyweightComponents.add(comp[1]);

//0            Component component = (comp[1] == null)? comp[0]:comp[1];
            ESlateHandle handle = registerComponentToESlateMicroworld(object);
            if (Component.class.isAssignableFrom(object.getClass())) {
                Component component = (Component) object;
                Dimension initialSize = null;
                Point initialLocation = location;
                if (size == null)
                    initialSize = component.getPreferredSize();
                else
                    initialSize = size;

//0            ESlateInternalFrame w = addComponent(comp[0], handle, ESlatePart.class.isInstance(component), initialSize);
                boolean isAnESlatePart = ESlateContainerUtils.implementsInterface(component.getClass(), ESlatePart.class);
//            ESlateInternalFrame w = addComponent(component, handle, ESlatePart.class.isInstance(component), initialSize);
                w = addComponent(component, handle, isAnESlatePart, initialSize, initialLocation);
                if (w == null) {
                    handle.dispose();
                    setCursor(Cursor.getDefaultCursor(), false);
                    return null;
                }
//if                w.formulateTitlePanel();
//i                w.adjustTitlePanelToMicroworldSettings();
                frameUtils.adjustTitlePanelToMicroworldSettings(w);
            }else{
                icon = new UILessComponentIcon(handle);
//System.out.println("Constructing icon here 1");
                /* The UILessComponentIcons are iconified when created. This happens
                 * so as to avoid the construction of its GUI. De-iconify it below.
                 */
                addUILessComponent(icon);
                icon.setIcon(false);
            }
//                    System.out.println("1. microworld changed = true");
//            addComponentItemToComponentsMenu(handle.getComponentName());
//            addComponentItemToHelpMenu(handle);

            DesktopItem desktopItem = null;
            if (w == null)
                desktopItem = icon;
            else
                desktopItem = w;
            ESlateComponent eslateComponent = new ESlateComponent(this, handle, (w!=null), desktopItem, object);
            attachESlateComponentListener(eslateComponent);
            mwdComponents.components.add(eslateComponent);
            playSystemSound(SoundTheme.COMPONENT_NEW_SOUND);

            try {
                eslateComponent.desktopItem.setActive(true);
            }catch (PropertyVetoException exc) {}

            // Update the component bar, if it is visible
//            if (componentBar != null && componentBar.getParent() != null) {
//                ComponentIcon ci = componentBar.addComponentIcon(handle.getComponentName());
//                ci.setSelection(true);
//            }

            ESlateContainer.this.setMicroworldChanged(true);
            setCursor(Cursor.getDefaultCursor(), false);
            return eslateComponent;
        }
        setCursor(Cursor.getDefaultCursor(), false);
        return null;
    }

    private void attachESlateComponentListener(ESlateComponent eslateComponent) {
        // Create and add the ESlateComponentListener
        eslateComponent.addESlateComponentListener(new ESlateComponentListener() {
            public void componentActivated(ESlateComponentEvent e) {
                ESlateComponent component = (ESlateComponent) e.getSource();
//System.out.println("componentActivated component: " + component + ", bounds: " + component.frame.getBounds());

//                    System.out.println("componentActivated mwdComponents.activeComponent: " + mwdComponents.activeComponent);
                if (mwdComponents.activeComponent != null && mwdComponents.activeComponent != component) {
                    try{
//                            System.out.println("Deactivating component: " + mwdComponents.activeComponent);
                        mwdComponents.activeComponent.desktopItem.setActive(false);
                    }catch (PropertyVetoException exc) {}
                }

                ESlateInternalFrame frame = component.frame;
                /* This occurs when a frame is destroyed. Swing bug probably.
                 */
                if (frame != null && (frame.isIcon() || frame.getTitle() == null))
                    return;

                setMicroworldChanged(true);
                mwdComponents.activeComponent = component;
//System.out.println("mwdComponents.activeComponent : " + mwdComponents.activeComponent);
                microworld.eslateMwd.setSelectedComponent(component.handle);

                if (frame != null && !loadingMwd) {
                    ensureFrameIsVisible(frame, frame.getBounds(), currentView.mwdAutoExpandable && currentView.mwdResizable);
                }
                compSelectionHistory.setFirst(component);
                if (component.container instanceof ESlateComposer) {
                    ESlateComposer composer = (ESlateComposer) component.container;
                    // Update the componentBar, if it is visible.
                    if (composer.componentBar != null && composer.componentBar.getParent() != null)
                        composer.componentBar.selectComponentIcon(component.handle.getComponentName());
                }

                if (frame != null && component.object instanceof java.awt.Component) {
                    ((Component) component.object).requestFocus();
                }else
                    component.icon.requestFocus();

                if (!soundListenerMap.containsListenerFor(MicroworldListener.class, "componentActivated"))
                    playSystemSound(SoundTheme.COMPONENT_ACTIVATED_SOUND);

                containerUtils.fireActiveComponentChanged(new ActiveComponentChangedEvent(ESlateContainer.this));
                microworld.fireComponentActivated(new MicroworldComponentEvent(microworld, component));
            }
            public void componentDeactivated(ESlateComponentEvent e) {
                ESlateComponent component = (ESlateComponent) e.getSource();
//                System.out.println("componentDeactivated component: " + component);

/*                ESlateInternalFrame fr = component.frame;
                if (fr != null) {
                    if (fr.getTitle() == null)
                        return;
//if                    if (!fr.isFrozen()) {
                        if (fr.isTitlePanelVisible()) {
                            fr.setTitleColor(ESlateInternalFrameTitlePanel.INACTIVE_COLOR); //ifESlateInternalFrame.TITLE_BG_COLOR);
                        }else{
                            if (fr.isMaximum())
                                fr.setBorder(null);
                            else
                                fr.setBorder(OldESlateInternalFrame.FRAME_NORMAL_BORDER); //new CompoundBorder(((CompoundBorder) fr.getBorder()).getOutsideBorder(), new javax.swing.border.LineBorder(Color.lightGray)));
                        }
//if                    }
                }
*/
                mwdComponents.activeComponent = null;
                microworld.eslateMwd.setSelectedComponent(null);
                microworld.fireComponentDeactivated(new MicroworldComponentEvent(microworld, component));
                setMicroworldChanged(true);
            }
            public void componentIconified(ESlateComponentEvent e) {
                ESlateComponent component = (ESlateComponent) e.getSource();
//                    ESlateInternalFrame fr = component.frame;
                setActiveComponent(component, false);
                // Update the componentBar, if it is visible.
                if (component.container instanceof ESlateComposer) {
                    ESlateComposer composer = (ESlateComposer) component.container;
                    if (composer.componentBar != null && composer.componentBar.getParent() != null) {
                        String compoName = component.handle.getComponentName();
                        if (composer.componentBar.getComponentIcon(compoName) != null)
                            composer.componentBar.getComponentIcon(compoName).setSelection(false);
                    }
                }

                if (!soundListenerMap.containsListenerFor(MicroworldListener.class, "componentIconified"))
                    playSystemSound(SoundTheme.COMPONENT_HIDDEN_SOUND);

                microworld.fireComponentIconified(new MicroworldComponentEvent(microworld, component));
            }
            public void componentRestored(ESlateComponentEvent e) {
                ESlateComponent component = (ESlateComponent) e.getSource();
                setActiveComponent(component, true);
                ESlateInternalFrame fr = component.frame;
                if (fr != null) {
                    /* When a microworld is packed, components which are iconified (invisible) are not
                     * taken into account. Therefore when an iconified component is de-iconified in a
                     * microworld which has been packed, there is a chance that it won't be visible
                     * anymore. The following code ensures the size of the Desktop pane will get
                     * adjusted to contain the de-iconified component, if it doesn't already do so.
                     */
                    /* Make sure the frame's component is visible. The component may have become
                     * invisible through the property editor, in which case the whole frame gets
                     * iconified. When restoring such a frame, its component must become visible.
                     */
                    if (Component.class.isAssignableFrom(component.handle.getComponent().getClass())) {
                        Component comp = (Component) component.handle.getComponent();
                        if (!comp.isVisible())
                            comp.setVisible(true);
                    }
                    ensureFrameIsVisible(fr, fr.getBounds(), currentView.mwdAutoExpandable && currentView.mwdResizable);
                }

                if (!soundListenerMap.containsListenerFor(MicroworldListener.class, "componentRestored"))
                    playSystemSound(SoundTheme.COMPONENT_SHOWN_SOUND);

                microworld.fireComponentRestored(new MicroworldComponentEvent(microworld, component));
            }
            public void componentClosed(ESlateComponentEvent e) {
//                    System.out.println("componentClosed event");
                //Focus may generate problems, so pass the focus to the desktop pane.
                requestFocus(); //lc.requestFocus();

                /* We make the assumption that this method is called only when the
                 * frame is closed through its close control and not programmatically,
                 * i.e. when its dispose() is called. This assumption stands in Swing1.1.1b1
                 * but not in Swing1.1.
                 */
                ESlateComponent eslateComponent = (ESlateComponent) e.getSource();
                if (eslateComponent == null) {
                    /* Something goes wrong here in Java 1.3. Two internalFrameClosed arrive
                     * for each frame that is closed. Ignore the second event.
                     */
                    return;
                }

                Object removedComponent = eslateComponent.object;
                if (removedComponent != null) {
                    compSelectionHistory.removeComponent(eslateComponent);
                    String compoName = e.getComponentName(); //eslateComponent.handle.getComponentName();
                    if (eslateComponent.container instanceof ESlateComposer) {
                        ESlateComposer composer = (ESlateComposer) eslateComponent.container;
                        composer.removeComponentItemFromComponentsMenu(compoName);
                        composer.removeComponentItemToHelpMenu(eslateComponent.handle);
                        // Update the componentBar, if it is visible.
                        if (composer.componentBar != null && composer.componentBar.getParent() != null)
                            composer.componentBar.removeComponentIcon(compoName);
                    }

                    mwdComponents.remove(eslateComponent);
                    microworld.fireComponentClosed(new MicroworldComponentEvent(microworld, eslateComponent));


                    setMicroworldChanged(true);
                }else
                    System.out.println("Serious inconsistency error in ESlateContainer internalFrameClosing() : 1");

                if (!closingMwd) {
                    if (!soundListenerMap.containsListenerFor(MicroworldListener.class, "componentClosed"))
                        playSystemSound(SoundTheme.COMPONENT_CLOSED_SOUND);
                }
//                setActiveComponent(eslateComponent, false);
                if (mwdComponents.activeComponent == null)
                    scrollPane.invalidateHighlightRect();

            }
            public void componentClosing(ESlateComponentEvent e) {
                microworld.fireComponentClosing(new MicroworldComponentEvent(microworld, (ESlateComponent) e.getSource()));
            }
            public void componentMaximized(ESlateComponentEvent e) {
                ESlateComponent comp = (ESlateComponent) e.getSource();
                try{
                    if (!comp.isActive())
                        comp.setActive(true);
                }catch (PropertyVetoException exc) {}
                if (!soundListenerMap.containsListenerFor(MicroworldListener.class, "componentMaximized"))
                    playSystemSound(SoundTheme.COMPONENT_MAXIMIZED_SOUND);
                microworld.fireComponentMaximized(new MicroworldComponentEvent(microworld, (ESlateComponent) e.getSource()));
            }
        });
    }

    /* Examine the component. If the component is an ESlatePart, then
     * get its ESlateHandle and register it to the current microworld.
     * If the component is not an ESlatePart, register it to ESlate, creating
     * an ESlateHandle for it. Then register the ESlateHandle to the
     * currentMicroworld.
     */
    protected ESlateHandle registerComponentToESlateMicroworld(Object component) {
//long start = System.currentTimeMillis();
        ESlateHandle eSlateHandle = ESlateContainerUtils.getESlateHandle(component);
//System.out.println("registerComponentToESlateMicroworld(): " + (System.currentTimeMillis()-start) + ", handle: " + eSlateHandle);
//        if (ESlatePart.class.isInstance(component))
//            eSlateHandle = ((ESlatePart) component).getESlateHandle();
//        else
//            eSlateHandle = ESlate.registerPart(component);
        if (eSlateHandle == null)
            eSlateHandle = ESlate.registerPart(component);

        /* Before 0.9.8.2 E-Slate release the ESlateHandles were created in the default
         * microworld of the Platform and here they were moved to the actual microworld
         * the belong to. Since 0.9.8.2 the 'currentMicroworld' is the Platform's default
         * microworld, so the ;line below is not needed. See bug #526.
         */
//default        System.out.println("registerComponentToESlateMicroworld() eSlateHandle.getESlateMicroworld(): "+ eSlateHandle.getESlateMicroworld().getName());
//m        eSlateHandle.setESlateMicroworld(microworld.eslateMwd);
//System.out.println("Registering to microworld component : " + eSlateHandle);
        microworld.eslateMwd.getESlateHandle().add(eSlateHandle);
        eSlateHandle.addESlateListener(componentNameListener);

        /* Add to the new handle the primitives with operations the Container performs
         * on components in the microworlds. Such operations are move, destroy, ...
         * These operations are registered through the components (their handles) and
         * not by the Container itself, so that the syntax for accessing this piece of
         * functionality is the same as the syntax used to access individual component
         * functionality. For example to set a Database's cell the user types:
         * tell [Database_1] setCell "1 "FieldA 120
         * In much the same way, the user types the following to move the Database component:
         * tell [Database_1] move 10 10
         * though in the later case, it is the Container that actually owns the setComponentLocation()
         * method that performs the task. This behaviour is achieved by adding the primitive
         * group to the components' handles and not the Container handle and by special code
         * in the primitives' actions.
         */
        /* The primitive group need only be added once and not again and again for each new
         * component.
         */
//0        if (eSlateHandles.size() == 0)
//        if (mwdComponents.size() == 0)
//            eSlateHandle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.ContainerComponentPrimitives");

//0        eSlateHandles.add(eSlateHandle);
        return eSlateHandle;
    }


    /** Instantiates one component of the given class. The zero-argument constructor
     *  of the component is called. The method returns an instance of the component.
     */
    static Object instantiateComponent(ESlateContainer container, String componentClassName) {
        Component parentComponent = (container.parentComponent==null) ? container : container.parentComponent;
        Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parentComponent);
        if (topLevelFrame == null && Frame.class.isAssignableFrom(parentComponent.getClass()))
            topLevelFrame = (Frame) parentComponent;

        try{
            container.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
            Class componentClass = SimpleClassLoader.getListenerLoader().loadClass(componentClassName);
//            Class componentClass = Class.forName(componentClassName);

            Class[] constuctorArgClasses = new Class[0];
            Constructor constructor = componentClass.getConstructor(constuctorArgClasses);

            Object[] constructorArgs = new Object[0];
            Object component = constructor.newInstance(constructorArgs);

//0            Component[] comps = new Component[2];
//0            comps[1] = null;
            // If the component is an Applet descendant, invoke its init() and start() methods
            if (Applet.class.isInstance(component)) {
                ((Applet) component).init();
                ((Applet) component).start();
//                if (JApplet.class.isInstance(component)) {
//                    comps[1] = component;
//                    component = ((JApplet) component).getContentPane();
//                }
            }

//0            comps[0] = component;
//0            return comps;
            return component;
        } catch (ClassNotFoundException exc) {
            System.out.println("1. " + exc.getClass() + ", msg: " + exc.getMessage());
            container.setCursor(Cursor.getDefaultCursor(), false);
            exc.printStackTrace();
            if (container.loadingMwd) {
				container.eventQueue.postMicroworldFinalRepaintEvent();
			}

            if (parentComponent.isVisible())
                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("Component") + " " + componentClassName + containerBundle.getString("ContainerMsg1"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (ClassCastException exc) {
            container.setCursor(Cursor.getDefaultCursor(), false);
			if (container.loadingMwd) {
				container.eventQueue.postMicroworldFinalRepaintEvent();
			}
            if (parentComponent.isVisible())
                ESlateOptionPane.showMessageDialog(parentComponent, "Only descendants of the class java.awt.Component can be instantiated", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        }catch (InvocationTargetException exc) {
//            exc.printStackTrace();
            container.setCursor(Cursor.getDefaultCursor(), false);
            exc.getTargetException().printStackTrace();
			if (container.loadingMwd) {
				container.eventQueue.postMicroworldFinalRepaintEvent();
			}
            if (parentComponent.isVisible()) {
//                exc.printStackTrace(printWriter);
                DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                dialog.setMessage(containerBundle.getString("ContainerMsg2") + componentClassName + ".");
                dialog.appendToDetails(containerBundle.getString("ContainerMsg3") + "\n");
                dialog.appendThrowableStackTrace(exc.getTargetException());
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            }
            return null;
        }catch (InstantiationException exc) {
//            exc.printStackTrace();
            container.setCursor(Cursor.getDefaultCursor(), false);
			if (container.loadingMwd) {
				container.eventQueue.postMicroworldFinalRepaintEvent();
			}
            if (parentComponent.isVisible()) {
//                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg2") + componentClassName + containerBundle.getString("ContainerMsg4"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                dialog.setMessage(containerBundle.getString("ContainerMsg2") + componentClassName + containerBundle.getString("ContainerMsg4"));
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            }
            return null;
        }catch (IllegalAccessException exc) {
            container.setCursor(Cursor.getDefaultCursor(), false);
			if (container.loadingMwd) {
				container.eventQueue.postMicroworldFinalRepaintEvent();
			}
            if (parentComponent.isVisible()) {
//                exc.printStackTrace(printWriter);
                DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                dialog.setMessage(containerBundle.getString("ContainerMsg2") + componentClassName + containerBundle.getString("ContainerMsg5"));
                dialog.appendThrowableStackTrace(exc); //setDetails(writer.toString());
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            }
            return null;
        }catch (IllegalArgumentException exc) {
            container.setCursor(Cursor.getDefaultCursor(), false);
			if (container.loadingMwd) {
				container.eventQueue.postMicroworldFinalRepaintEvent();
			}
            if (parentComponent.isVisible()) {
//                exc.printStackTrace(printWriter);
                DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                dialog.setMessage(containerBundle.getString("ContainerMsg2") + componentClassName);
                dialog.appendThrowableStackTrace(exc); //setDetails(writer.toString());
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            }
            return null;
        }catch (NoSuchMethodException exc) {
            System.out.println("2. " + exc.getClass() + ", msg: " + exc.getMessage());
            container.setCursor(Cursor.getDefaultCursor(), false);
			if (container.loadingMwd) {
				container.eventQueue.postMicroworldFinalRepaintEvent();
			}
            if (parentComponent.isVisible()) {
//                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("Component") + " " + componentClassName + containerBundle.getString("ContainerMsg1"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                dialog.setMessage(containerBundle.getString("Component") + " " + componentClassName + containerBundle.getString("ContainerMsg1"));
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            }
            return null;
        }catch (SecurityException exc) {
            container.setCursor(Cursor.getDefaultCursor(), false);
			if (container.loadingMwd) {
				container.eventQueue.postMicroworldFinalRepaintEvent();
			}
            if (parentComponent.isVisible()) {
//                exc.printStackTrace(printWriter);
                DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                dialog.setMessage(containerBundle.getString("ContainerMsg2") + componentClassName);
                dialog.appendThrowableStackTrace(exc); //.setDetails(writer.toString());
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            }
            return null;
        }catch (NoClassDefFoundError thr) {
            System.out.println("cause: " + thr.getCause());
            thr.printStackTrace();
            return null;
        }catch (Throwable thr) {
            thr.printStackTrace();
            container.setCursor(Cursor.getDefaultCursor(), false);
			if (container.loadingMwd) {
				container.eventQueue.postMicroworldFinalRepaintEvent();
			}
            if (parentComponent.isVisible()) {
//                thr.printStackTrace(printWriter);
                DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                dialog.setMessage(containerBundle.getString("ContainerMsg2") + componentClassName);
                dialog.appendToDetails(containerBundle.getString("ContainerMsg6") + thr.getClass() + ", " + thr.getMessage() + "\n\n");
                dialog.appendThrowableStackTrace(thr);
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            }
            return null;
        }
    }

    /** Instantiates one component of the given class. The zero-argument constructor
     *  of the component is called. The method returns an instance of the component.
     */
//    protected static Component readSerializableComponent(Component parentComponent, String componentClassName, ObjectInputStream in) {
    protected static Component readSerializableComponent(ESlateContainer container, String componentClassName, ObjectInputStream in) {
        try{
            container.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
            Class componentClass = Class.forName(componentClassName);

//            Class[] constuctorArgClasses = new Class[0];
//            Constructor constructor = componentClass.getConstructor(constuctorArgClasses);

//            Object[] constructorArgs = new Object[0];
//            Component component = (Component) constructor.newInstance(constructorArgs);
            Component component = (Component) in.readObject();

            Component[] comps = new Component[2];
            comps[1] = null;
            // If the component is an Applet descendant, invoke its init() method
            if (Applet.class.isInstance(component)) {
                ((Applet) component).init();
                if (JApplet.class.isInstance(component)) {
                    comps[1] = component;
                    component = ((JApplet) component).getContentPane();
                }
            }

            comps[0] = component;
//0            return comps;
            return component;
        } catch (ClassNotFoundException exc) {
            System.out.println("3. " + exc.getClass() + ", msg: " + exc.getMessage());
            exc.printStackTrace();
            container.setCursor(Cursor.getDefaultCursor(), false);
            if (container.isVisible())
                ESlateOptionPane.showMessageDialog(container, containerBundle.getString("Component") + " " + componentClassName + containerBundle.getString("ContainerMsg1"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (IOException exc) {
            container.setCursor(Cursor.getDefaultCursor(), false);
            if (container.isVisible())
                ESlateOptionPane.showMessageDialog(container, containerBundle.getString("Component") + " " + componentClassName + containerBundle.getString("ContainerMsg25"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /* This method should go away, since Applets are now embeddable into JInternalFrames.
     * Also all the methods above and below that return a two-element Component array, should
     * be checked.
     */
    protected static Component[] initApplet(Component component) {
        Component[] comps = new Component[2];
        comps[1] = null;
        // If the component is an Applet descendant, invoke its init() method
        if (Applet.class.isInstance(component)) {
            ((Applet) component).init();
            if (JApplet.class.isInstance(component)) {
                comps[1] = component;
                component = ((JApplet) component).getContentPane();
            }
        }

        comps[0] = component;
        return comps;
    }

    /* Reports if the given class is only Serializable and not Extrnalizable. A class is
     * only Serializable if it does not implement the Externalizable interface and:
     * 1. implements the Serializable interface.
     * 2. any of its superclasses implements the Serializable interface.
     * Because of some problem with components, which extend the Serializable JPanel -
     * then problem has to do with a non-Serializable inner class of the ToolTip manager-
     * which hangs the Container UI, these components are not considered Serializable. (Swing 1.1.1b1)
     */
    protected boolean classIsSerializableOnly(String className) {
        try{
            Class componentClass = Class.forName(className);
            boolean implementsExternalizable = false;
            while (componentClass != null) {
                Class[] interfaces = componentClass.getInterfaces();
//                for (int i=0; i<interfaces.length; i++)
//                    System.out.println(interfaces[i] +", ");
//                System.out.println();
                for (int i=0; i<interfaces.length; i++) {
                    if (interfaces[i].getName().equals("java.io.Externalizable")) {
                        implementsExternalizable = true;
                        break;
                    }
                }
                if (implementsExternalizable)
                    break;
                else
                    componentClass = componentClass.getSuperclass();
            }

            // If the class is not Externalizable, check if it is Serializable.
            componentClass = Class.forName(className);
            if (!implementsExternalizable) {
                boolean implementsSerializableOnly = false;
                while (componentClass != null) {
                    Class[] interfaces2 = componentClass.getInterfaces();
                    for (int i=0; i<interfaces2.length; i++) {
                        if (interfaces2[i].getName().equals("java.io.Serializable")) {
                            implementsSerializableOnly = true;
                            break;
                        }
                    }

                    // Because of the tooltip manager
//                    if (componentClass.getName().equals("javax.swing.JPanel"))
//                        return false;

                    if (implementsSerializableOnly)
                        return true;
                    else
                        componentClass = componentClass.getSuperclass();
                }
                return implementsSerializableOnly;
            }else
                return false;
        }catch (Exception exc) {return false;}
    }

    ESlateInternalFrame addComponent(Component component, ESlateHandle eSlateHandle, boolean isESlatePart, Dimension initialSize, Point initialLocation) {
//        System.out.println("initialSize: " + initialSize);
//        System.out.println("In addComponent()");
        componentInitializing = true;
        ESlateInternalFrame w = createComponentFrame(eSlateHandle);
        try{
            w.setIcon(false);
        }catch (PropertyVetoException exc) {
            System.out.println("Exception while de-iconifying frame in addComponent()");
        }
        if (w == null)
            return null;
//        componentCount++;
        Insets frameBorderInsets = new Insets(0, 0, 0, 0);
        if (w.getBorder() != null)
            frameBorderInsets = w.getBorder().getBorderInsets(w);
//        System.out.println("Border insets: " + frameBorderInsets);
        int frameWidth = initialSize.width + frameBorderInsets.left + frameBorderInsets.right;
        if (w.isWestVisible())
            frameWidth += w.getWest().getPreferredSize().height;
        if (w.isEastVisible())
            frameWidth += w.getEast().getPreferredSize().height;
        int frameHeight = initialSize.height + frameBorderInsets.top + frameBorderInsets.bottom;
        if (w.isNorthVisible())
            frameHeight += w.getNorth().getPreferredSize().height;
        if (w.isSouthVisible())
            frameHeight += w.getSouth().getPreferredSize().height;
        if (w.isTitlePanelVisible()) {
            frameHeight += w.getTitlePanel().getPreferredSize().height;
//System.out.println("w.getTitlePanel().getPreferredSize().height: " + w.getTitlePanel().getPreferredSize().height);
        }
//if        int frameHeight = initialSize.height + ESlateInternalFrame.NORTHPANE_HEIGHT + frameBorderInsets.top + frameBorderInsets.bottom;

//????        if (isESlatePart) //Remove 20 cause the ESlatePart's menuBar was removed from the part
//????            frameHeight = frameHeight - 20;
        if (frameWidth < OldESlateInternalFrame.MIN_SIZE.width)
            frameWidth = OldESlateInternalFrame.MIN_SIZE.width;
//        System.out.println("Frame size: " + frameWidth + ", " + frameHeight);
        w.setSize(new Dimension(frameWidth, frameHeight));
//        System.out.println("New size: " + w.getSize());
//0        w.setLocation(scrollPane.getViewport().getViewPosition().x + 20*((components.size()+1)%10), scrollPane.getViewport().getViewPosition().y + 20*((components.size()+1)%10));
        int componentNum = mwdComponents.size();
        if (initialLocation == null)
            w.setLocation(scrollPane.getViewport().getViewPosition().x + 20*((componentNum+1)%10), scrollPane.getViewport().getViewPosition().y + 20*((componentNum+1)%10));
        else
            w.setLocation(initialLocation);
//        w.setVisible(true);
//if        component.addMouseListener(w.frameUI.newBorderListener);
//if        component.addMouseMotionListener(w.frameUI.newBorderListener);

//0        componentFrames.add(w);
//1        frameSelectionHistory.addFrame(w);
///        lc.add(w, LayerInfo.DEFAULT_LAYER_Z_ORDER);

//0        components.add(component);
//0        componentFrameIndex.add(new Integer(componentFrames.size()-1));

//1        setFrameSelected(w, true);

///        w.setVisible(true);
//System.out.println("Frame bounds 1: " + w.getBounds());
        componentInitializing = false;
        return w;
    }

static long addFrameTimer = 0;
    ESlateInternalFrame addComponent(Component component, ESlateHandle eSlateHandle, boolean useFrame, int xLocation, int yLocation, int width, int height) {
        if (useFrame) {
            ESlateInternalFrame w = createComponentFrame(eSlateHandle);
long start = System.currentTimeMillis();
            if (w == null)
                return null;
///            w.setSize(new Dimension(width, height));
///            w.setLocation(new Point(xLocation, yLocation));
//            w.getContentPane().add(component);

//0            componentFrames.add(w);
//1            frameSelectionHistory.addFrame(w);
///            lc.add(w, LayerInfo.DEFAULT_LAYER_Z_ORDER);

//0            components.add(component);
//0            componentFrameIndex.add(new Integer(componentFrames.size()-1));

///            w.setVisible(true);

//System.out.println("Frame bounds 2: " + w.getBounds());
//1            setFrameSelected(w, true);
addFrameTimer = addFrameTimer + (System.currentTimeMillis()-start);
            return w;
        }else{
            if (JComponent.class.isInstance(component))
                ((JComponent) component).setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED), ((JComponent) component).getBorder()));
            lc.add(component, LayerInfo.DEFAULT_LAYER_Z_ORDER);
            component.setSize(new Dimension(width, height));
            component.setLocation(new Point(xLocation, yLocation));
            component.validate();
            return null;
//0            components.add(component);
//0            componentFrameIndex.add(NOFRAME);
        }
    }

    public boolean createNewMicroworld() {
        return createNewMicroworld(true);
    }

    /* Starts a new empty microworld.
     */
    public boolean createNewMicroworld(boolean checkForChanges) {
        if (microworld != null) {
            if (!closeMicroworld(checkForChanges))
                return false;
        }

        /* Check if the default microworld opened at the initialization of the Platform
         * exists. If it does, get rid of it. This microworld is only used when the
         * components run outside E-Slate, i.e. directly in the browser as applets. E-Slate
         * makes no use of this microworld. See bug #526.
         */
//        System.out.println("Default microworld: " + ESlateMicroworld.getDefaultMicroworld());
/*default        if (ESlateMicroworld.getDefaultMicroworld() != null) {
//            System.out.println("Default microworld: " + ESlateMicroworld.getDefaultMicroworld().getName());
            containerHandle.setESlateMicroworld(null);
            ESlateMicroworld.getDefaultMicroworld().dispose();
        }
*/
        microworld = new Microworld(new ESlateMicroworld(), this);
        /* Set the new microworld as the default microworld of the E-Slate Platform. The
         * Platform adds the ESlateHandle of each frsh component to its default microworld.
         * If this microworld does not exist, the Platform re-creates it. Before 0.9.8.2
         * E-Slate release, the ESlateHandles were first registered at the default microworld
         * and then E-Slate moved them to the 'currentMicroworld'. To have the ESlateHandles
         * initialized directly in the 'currentMicroworld', we specify it as the default
         * Platform microworld. This way no handle microworld transition will occur. See bug #526.
         */
//default ESlateMicroworld.setDefaultMicroworld(currentMicroworld);

        microworld.eslateMwd.setMicroworldEnvironment(this);
        getESlateHandle().add(microworld.eslateMwd.getESlateHandle());
//m        getESlateHandle().setESlateMicroworld(microworld.eslateMwd);
        microworld.fireMicroworldCreated(new MicroworldEvent(microworld));
        PerformanceManager.getPerformanceManager().setESlateMicroworld(microworld.eslateMwd);

        //Start watching for primitive groups
        //TEST
        if (logoMachine != null)
            startWatchingMicroworldForPrimitiveGroups();

        //Initialize the microworld settings
        microworld.eslateMwd.setInfoWindowEnabled(false);

        currentView = new MicroworldCurrentView(this, null);
        currentView.container = this;
        currentView.viewName = containerBundle.getString("Microworld current view");
        currentView.menuBarVisible = menuBarVisible;
		if (!loadingMwd) {
			setContainerTitle(null);
			setMicroworldBackground(UIDialog.ICON_COLORED_BACKGROUND, defaultContainerBgrColor, null, 0);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}

		NewRestorableImageIcon eslate=new NewRestorableImageIcon(ESlateContainer.class.getResource("images/eslate.png"));
		setMicroworldBackground(UIDialog.ICON_COLORED_BACKGROUND,currentView.backgroundColor,eslate,ImageChooser.CENTER_IMAGE);
		// ((Frame) SwingUtilities.getAncestorOfClass(Frame.class,this)).setTitle(container.microworld.eslateMwd.getName());
		
		mwdLayers.resetInternal();

        //if (!loadingMwd)
           //setMenuBarVisibleInternal(true);

        setMicroworldChanged(false);
        return true;
    }

//    void setScreenFrozen(boolean frozen) {
//System.out.println("setScreenFrozen(" + frozen + ")");
/*        if (scrollPane.freezeScreen == frozen) return;
        scrollPane.freezeScreen = frozen; //setVisible(true);
        lc.freezeScreen = frozen;
//        rootPane.getGlassPane().setVisible(frozen);
        if (glassPaneVisibleOnMicroworldLoad) {
            if (frozen) {
    //System.out.println("pinting immediately");
                setGlassPaneVisible(true);
                lc.paintImmediately(lc.getVisibleRect());
            }else{
    //            SwingUtilities.invokeLater(new Runnable() {
    //                public void run() {
                        setGlassPaneVisible(false);
    //                }
    //            });
            }
        }
        lc.setVisible(!frozen);
*/
//    }

    public boolean closeMicroworld(boolean checkForChanges) {
        //Report the used memory
//        java.lang.Runtime rt = java.lang.Runtime.getRuntime();
//        System.out.println("MEMORY---> Before closing microworld. Total mem: " + rt.totalMemory() + ", free mem: " + rt.freeMemory() + ", used mem: " + (rt.totalMemory() - rt.freeMemory()));
//System.out.println("closeMicroworld() loadingMwd: " + loadingMwd);
        if (microworld == null)
            return true;

        if (checkForChanges && microworldChanged) {
//            if (loadingMwd) setScreenFrozen(false); //scrollPane.freezeScreen = false; //setVisible(true);
            int k = promptToSaveCurrentMicroworld();
            if (k == CANCEL) {
//                if (loadingMwd) setScreenFrozen(false);//scrollPane.freezeScreen = false; //setVisible(true);
                return false;
            }
            if (k == SAVE) {
                boolean needsSave = microworldChanged;
                if (!saveMicroworld(true)) {
                    microworldChanged = needsSave;
//                    if (loadingMwd) setScreenFrozen(false);//scrollPane.freezeScreen = false; //setVisible(true);
                    return false;
                }
            }
//            if (loadingMwd) setScreenFrozen(true);//scrollPane.freezeScreen = true; //setVisible(false);
//                saveMicroworld();
        }

        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(mwdCloseTimer);
        closingMwd = true;
        /* Unlock the microworld, while it is closing */
        microworld.setLocked(false);

        boolean playDefaultMwdCloseSound = true;
        if (soundListenerMap.containsListenerFor(MicroworldListener.class, "microworldClosing"))
            playDefaultMwdCloseSound = false;

        microworld.fireMicroworldClosing(new MicroworldEvent(microworld));
        microworld.disableMicroworldListeners();
        if (parentComponent != null)
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        clearMicroworld();

        resetContainerEnv();

//        // Deselect the first item in the microworldListMenu
//        if (menuPanel != null && menuPanel.microworldReopenMenu != null && menuPanel.microworldReopenMenu.getItemCount() > 0) {
//            AbbreviatedCheckBoxMenuItem firstItem = (AbbreviatedCheckBoxMenuItem) menuPanel.microworldReopenMenu.getItem(0);
//            if (firstItem != null)
//                firstItem.setSelected(false);
//        }

//        rt.gc();
//        System.out.println("MEMORY---> Microworld closed. Total mem: " + rt.totalMemory() + ", free mem: " + rt.freeMemory() + ", used mem: " + (rt.totalMemory() - rt.freeMemory()));
        if (parentComponent != null)
            setCursor(Cursor.getDefaultCursor(), false);

        if (playDefaultMwdCloseSound)
            playSystemSound(SoundTheme.MWD_CLOSED_SOUND);

		// Clear the list of the components to be repainted, when the RepaintManager
		// gets activated.
		repaintManager.componentsToRepaintAfterMicroworldLoad.clear();
		// Reset the 'scriptsLoaded' flag of the ScriptUtils
		ScriptUtils.getInstance().scriptsLoaded = false;

        closingMwd = false;
        pm.stop(mwdCloseTimer);
//        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.displayTime(mwdCloseTimer, "","ms");

        // Remove all the Performance Timer groups of the closing microworld.
        pm.resetGlobalPerformanceTimerGroups();
        pm.setState(null);
        /* Re-attach the E-Slate timer groups. */
        if (pm.isEnabled())
            initializePerformanceManager();

        return true;
    }

/*    public void renameMicroworld(String newName) {
        if (microworld == null)
            return;
        String currentName = microworld.eslateMwd.getName();
        String newName = (String) ESlateOptionPane.showInputDialog(ESlateContainer.this,
                              containerBundle.getString("ContainerMsg23"),
                              containerBundle.getString("ContainerMsg24"),
                              JOptionPane.QUESTION_MESSAGE,
                              null,
                              null,
                              currentName);

        setMicroworldName(newName);
*//*        if (newName != null) {
            try{
                currentMicroworld.setName(newName);
                setContainerTitle(newName);
            }catch (gr.cti.eslate.base.NameUsedException exc) {
                ESlateOptionPane.showMessageDialog(parentComponent, exc.getMessage(), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }
*/
/*    }
*/
    public void renameActiveComponent() {
//0        if (activeFrame == null)
        if (mwdComponents.activeComponent == null)
            return;
//0        ESlateHandle handle = (ESlateHandle) eSlateHandles.at(componentFrames.indexOf(activeFrame));
        if (mwdComponents.activeComponent.frame != null) {
            ESlateHandle handle = mwdComponents.activeComponent.handle;
            String currentName = handle.getComponentName();
            String newName = (String) ESlateOptionPane.showInputDialog(ESlateContainer.this,
                                  containerBundle.getString("ContainerMsg21"),
                                  containerBundle.getString("ContainerMsg22"),
                                  JOptionPane.QUESTION_MESSAGE,
                                  null,
                                  null,
                                  currentName);

            if (newName != null) {
                try{
                    handle.setComponentName(newName);
                }catch (gr.cti.eslate.base.NameUsedException exc) {
                    ESlateOptionPane.showMessageDialog(ESlateContainer.this, exc.getMessage(), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }catch (gr.cti.eslate.base.RenamingForbiddenException exc) {
                    ESlateOptionPane.showMessageDialog(ESlateContainer.this, exc.getMessage(), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }else{ //If this is an invisible component
            mwdComponents.activeComponent.icon.setEditModeOn(true);
        }
    }

    public void setMicroworldName(String mwdName) {
        if (microworld == null || mwdName == null)
            return;
        microworld.setTitle(mwdName);
    }

    public String getMicroworldName() {
        if (microworld == null)
            return null;
        return microworld.eslateMwd.getName();
    }

    /* Returns the number of top-level components inside E-Slate
     */
    public int getTopLevelComponentCount() {
        if (microworld == null)
            return 0;
        return mwdComponents.size();// microworld.eslateMwd.getHandles().length-1; // -1 cause there is a handle for the Container, too
    }

    public void setMicroworldChanged(boolean changed) {
//        if (changed)
//            Thread.currentThread().dumpStack();
//        System.out.println("Mwd changed false============================");
        microworldChanged = changed;
    }

    public boolean isMicroworldChanged() {
        return microworldChanged;
    }

    int promptToSaveCurrentMicroworld() {
//        System.out.println("microworldChanged: " + microworldChanged);
//        System.out.println("promptToSaveCurrentMicroworld mwdStorageAllowed: " + mwdStorageAllowed);
        if (microworld == null)
            return DONT_SAVE;
        if (!microworld.mwdStorageAllowed)
            return DONT_SAVE;

        /* Notify all the components that they are going to be disposed, without giving them
         * the chance to veto the destruction. At the same time check if the state of the
         * components is modified and adjust the 'microworldChanged' flag.
         */
//0        for (int i=0; i<eSlateHandles.size(); i++) {
        for (int i=0; i<mwdComponents.size(); i++) {
//0            boolean[] disposalAttrs = notifyForDisposal((ESlateHandle) eSlateHandles.at(i), false);
            boolean[] disposalAttrs = notifyForDisposal(mwdComponents.components.get(i).handle, false);
//            System.out.println("Veto: " + disposalAttrs[0]);
//            System.out.println("State changed: " + disposalAttrs[1]);

            if (!microworldChanged && disposalAttrs[1])
                setMicroworldChanged(true);
        }

        if (!microworldChanged)
            return DONT_SAVE;
        /* If the microworld is empty and the background is the default
         * then close the current microworld without prompting
         */
        if (mwdComponents == null || mwdComponents.size() == 0 &&
           (currentView.backgroundType==1 && lc.getBackground().equals(defaultContainerBgrColor)) &&
           currentView.borderType == UIDialog.NO_BORDER && currentView.outerBorderType == UIDialog.OUTER_BORDER_NONE &&
           !microworldChanged)
            return DONT_SAVE;

/*        if ((ESlateOptionPane.showConfirmDialog(new JFrame(), containerBundle.getString("ContainerMsg19") + currentMicroworld.getName() + "\"",
          containerBundle.getString("ContainerMsg20"),
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.YES_OPTION)
            return true;
        else
            return false;
*/
        String mwdName = microworld.eslateMwd.getName();
        if (!microworld.microworldNameUserDefined)
            mwdName = ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true);
        String title, msg;
        if (mwdName == null || mwdName.equals("null")) {
            title = containerBundle.getString("ContainerMsg20");
            msg = containerBundle.getString("ContainerMsg19") + containerBundle.getString("ContainerMsg32");
        }else{
            title = containerBundle.getString("ContainerMsg20");
            msg = containerBundle.getString("ContainerMsg19") + " \"" + mwdName + containerBundle.getString("ContainerMsg32");
        }
        Object[] yes_no_cancel = {containerBundle.getString("Yes"), containerBundle.getString("No"), containerBundle.getString("Cancel")};

        JOptionPane pane = new JOptionPane(msg,
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_CANCEL_OPTION,
            javax.swing.UIManager.getIcon("OptionPane.questionIcon"),
            yes_no_cancel,
            containerBundle.getString("Yes"));
//        contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        javax.swing.JDialog dialog = pane.createDialog(parentFrame, title);
        playSystemSound(SoundTheme.QUESTION_SOUND);
        dialog.setVisible(true);
        dialog.dispose();
        Object option = pane.getValue();
        // When the JOptionPane closes by pressing the ESC button, the option variable has an Integer value of -1.
        if (option == null || option.toString().equals("-1") || option == containerBundle.getString("Cancel") || option == null)
            return CANCEL; // CANCEL

//                if ((ESlateOptionPane.showConfirmDialog(new JFrame(), msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)) {
        if (option == containerBundle.getString("Yes"))
            return SAVE;
        else
            return DONT_SAVE; // NO
    }

    void promptToClearMicroworld() {
        if (microworld == null || microworld.eslateMwd.getHandles().length == 0)
            return;

//        contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        if ((ESlateOptionPane.showConfirmDialog(parentFrame, containerBundle.getString("ContainerMsg28"),
          containerBundle.getString("ContainerMsg29"),
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.YES_OPTION) {
              clearMicroworld();
        }else{
              return;
        }
    }

    public void clearMicroworld() {
        if (microworld == null) return;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (!closingMwd)
            microworld.checkActionPriviledge(microworld.isComponentRemovalAllowed(), "componentRemovalAllowed");

//        System.out.println("clearMicroworld() --> components.size(): " + components.size());
        requestFocus(); //lc.requestFocus();
        int reparentType = microworld.eslateMwd.getReparentType();
        microworld.eslateMwd.setReparentType(ESlateMicroworld.REPARENT_NEVER);
        for (int i=mwdComponents.size()-1; i>=0; i--) {
//        while (mwdComponents.size() != 0) {
//            Component comp = (heavyweightComponents.at(i) == null)? components.at(i):heavyweightComponents.at(i
/*0            Component comp = (Component) components.at(0);
            int frameIndex = ((Integer) componentFrameIndex.at(0)).intValue();
            ESlateInternalFrame intFr = (ESlateInternalFrame) componentFrames.at(frameIndex);

            String compName = intFr.getTitle();
//            try{
//                intFr.setClosed(true);
//            }catch (java.beans.PropertyVetoException exc) {}
*/
            ESlateComponent ecomponent = mwdComponents.components.get(i);
            removeComponent(ecomponent, false, false);
        }
        lc.removeAll();
        lc.invalidate();
        lc.doLayout();
        scrollPane.repaint();

        mwdComponents.clear();
/*0        components.clear();
        heavyweightComponents.clear();
        componentFrames.clear();
*/
        compSelectionHistory.clear();
/*0        componentFrameIndex.clear();
        eSlateHandles.clear();
*/
//0        activeFrame = null;
        microworld.eslateMwd.setSelectedComponent(null);
//        System.out.println("setSelectedComponent 3");

//        System.out.println("2. microworld changed = true");
//        System.out.println("microworldChanged 2");
        setMicroworldChanged(true);
        microworld.eslateMwd.setReparentType(reparentType);
    }

    boolean removeActiveComponent() {
        ESlateComponent activeComponent = mwdComponents.activeComponent;
        if (activeComponent == null)
            return false;
        if ((ESlateOptionPane.showConfirmDialog(parentComponent, containerBundle.getString("ContainerMsg30") + activeComponent.handle.getComponentName() + containerBundle.getString("ContainerMsg32"),
            containerBundle.getString("ContainerMsg31"),
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.YES_OPTION)
            return removeComponent(activeComponent, true, true);
        return false;
    }

    /* Removes the specified component from the microworld.
     */
    public boolean removeComponent(Object object, boolean notifyForDisposal, boolean cancellationRespected) {
        if (microworld == null) return false;
        ESlateComponent component = getComponent(object);
        if (component != null)
            return removeComponent(component, notifyForDisposal, cancellationRespected);
        return false;
    }

    /* Removes the component with the given name from the microworld.
     */
    public boolean removeComponent(String compoName, boolean notifyForDisposal, boolean cancellationRespected) {
        if (microworld == null) return false;
        ESlateComponent component = getComponent(compoName);
        if (component != null)
            return removeComponent(component, notifyForDisposal, cancellationRespected);
        return false;
    }

    /* Calls the 'toBeDisposed()' method of handle. The component returns two pieces of
     * information. Firstly, if it accepts its destruction, through the return value of
     * 'toBeDisposed(). Secondly, if its state has changed and thus the microworld has
     * changed, so the user should be prompted to save. (See ESR1 at the TeamCenter server
     * for more details. 'notifyForDisposal()' returns these two pieces of info in a boolean
     * array with two elements.
     */
    boolean[] notifyForDisposal(ESlateHandle handle, boolean cancellationRespected) {
        boolean[] results = new boolean[2];
        BooleanWrapper compoStateChangedWrapper = new BooleanWrapper(false);

        // veto info
        results[0] = handle.toBeDisposed(cancellationRespected, compoStateChangedWrapper);
        // state modification info
        results[1] = compoStateChangedWrapper.getValue();
        return results;
    }

    /** Removes from the microworld the component hosted in the given ESlateInternalFrame.
      * If 'notifyForDisposal' is set, then the component is notified before it is disposed.
      * This gives the component the chance to ask permission from the user to save its state.
      * The component can also veto its disposal. The veto will be respected only if the
      * 'cancellationRespected' is set to true.
      */
     /* 'notifyForDisposal' is set only when the component is atomically destroyed. If the
      * component is destroyed as a result of the microworld beign closed, then the notification
      * is send in 'closeMicroworld()'.
      */
    public boolean removeComponent(ESlateComponent component, boolean notifyForDisposal, boolean cancellationRespected) {
        if (microworld == null) return false;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (!closingMwd)
            microworld.checkActionPriviledge(microworld.isComponentRemovalAllowed(), "componentRemovalAllowed");

        ESlateHandle parentHandle = component.handle.getParentHandle();
        if (notifyForDisposal) {
            boolean[] disposalAttrs = notifyForDisposal(component.handle, cancellationRespected);
//            System.out.println("Veto: " + disposalAttrs[0]);
//            System.out.println("State changed: " + disposalAttrs[1]);

            if (cancellationRespected && disposalAttrs[0])
                return false;
            if (disposalAttrs[1])
                setMicroworldChanged(true);
        }

        /* When a component is removed from a microworld, it is possible that it gets
         * re-parented to some other component, which currently hosts it, without being
         * its parent, i.e. the host component's handle is not the parent of the removed
         * component's handle. 'notifyForDisposal()' above notifies the component to be
         * removed. As a result the disposingHandle() of any ESlateAdapter added to the
         * componet's handle is called. In this method the host may try to re-parent the
         * removed component, so that even though it is removed from the E-Slate desktop
         * it will still continue to exist, hosted in the new parent. If this happens,
         * then remove the component from the desktop, without destroying its handle.
         */
        String compoName = component.handle.getComponentName();
        Object object = component.handle.getComponent();
        requestFocus(); //lc.requestFocus();

        component.fireComponentClosing(new ESlateComponentEvent(component, ESlateComponentEvent.COMPONENT_CLOSING, component.handle.getComponentName()));
        setActiveComponent(component, false);
        DesktopItem desktopItem = component.desktopItem;
        UILessComponentIcon desktopIcon = component.icon;
        ESlateInternalFrame fr = component.frame;
        boolean forceFrameSelection = false;
        if (fr != null) //if && fr.isFrozen())
            lc.remove((Component) object);

        if (fr != null && fr.isIcon()) {
//if            lc.remove(fr.getDesktopIcon());
        }

//1        frameSelectionHistory.removeFrame(fr);
        compSelectionHistory.removeComponent(component);
//        if (desktopItem.isActive())
//            setActiveComponent(component, false);

        desktopItem.setVisible(false);

        try{
            desktopItem.setClosed(true);
            component.fireComponentClosed(new ESlateComponentEvent(component, ESlateComponentEvent.COMPONENT_CLOSED, component.handle.getComponentName()));
            if (fr != null) {
//if                containerDesktopManager.closeFrame(fr);
                frameUtils.removeComponentListener(fr);
                frameUtils.removeTitlePanelMouseListener(fr);
                frameUtils.removePropertyChangeListener(fr);
                frameUtils.removeFrameListener(fr);
                fr.dispose();
//                frameUtils.removePropertyChangeListener(fr);
//                frameUtils.removeFrameListener(fr);
            }
            //Dispose all the Logo script frames which have to do with this component
//            java.awt.Frame[] listenerFrames = listenerDialogList.getComponentListenerFrames(compoName, true);
//            for (int i=0; i<listenerFrames.length; i++) {
//                listenerDialogList.removeListener(listenerFrames[i]);
//                listenerFrames[i].dispose();
//            }
            for (int i=0; i<mwdViews.viewList.length; i++)
                mwdViews.viewList[i].removeComponentInfo(compoName);
//            mwdComponents.remove(component);
//            System.out.println("Component " + name + " dropped");
        }catch (PropertyVetoException exc) {
            System.out.println("PropertyVetoException");
        }catch (Exception exc) { //General exception from the hosted component
            exc.printStackTrace();
            System.out.println("Exception " + exc.getClass().getName() + " raised when frame " + fr.getTitle() + " was closed");
        }

        /* Here we gather the component's listeners cause if the component is not re-parented
         * it is destroyed by remove() below. So its handle does not function afterwards and
         * therefore we cannot get its nested handles and remove the script listeners of
         * the nested components. To overcome this, we get here all the listeners of the removed
         * component and its nested components, and if the component is not re-parented, we
         * remove them.
         */
        ScriptListener[] scriptListeners = componentScriptListeners.getScriptListeners(component.handle, true);

//System.out.println("1. component.handle.getParentHandle(): " + component.handle.getParentHandle());
        /* Give the component a chance to be re-parented, if it is nested in another component
         * too. This applies to its subcomponents too.
         */
        if (component.handle.getParentHandle() == microworld.eslateMwd.getESlateHandle()) {
//System.out.println("Removing component: " + component.handle);
            microworld.eslateMwd.getESlateHandle().remove(component.handle);
        }
//System.out.println("component.handle.getParentHandle(): " + component.handle.getParentHandle());

        // Clear the ESlateComponentListeners which were added to the component when created.
        component.eslateComponentListeners.clear();

        /* If the component did not reparent, then remove its script listeners and dispose it */
        if (component.handle.getParentHandle() == null) {
            for (int i=0; i<scriptListeners.length; i++)
                componentScriptListeners.removeScriptListener(scriptListeners[i]);
            // We have to call the following to remove the node for the scriptListenerTree
            componentScriptListeners.removeScriptListeners(component.handle);
            soundListenerMap.removeSoundListeners(component.handle);
            try{
                component.handle.dispose();
            }catch (Throwable thr) {
                DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
                String message = containerBundle.getString("ContainerMsg35");
                if (compoName != null)
                    message = message + " \"" + compoName + "\". ";
                else
                    message = message + ". ";
                message = message + '\n' + containerBundle.getString("ContainerMsg34");

                dialog.setMessage(message);
                dialog.setDetails("Exception while disposing component's handle. Continuing... " + '\n');
                dialog.appendThrowableMessage(thr);
                dialog.createNewLine();
                dialog.appendThrowableStackTrace(thr);
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
            }
        }

        component.handle = null;
        if (desktopIcon != null)
            desktopIcon.handle = null;

        lc.revalidate();
        lc.repaint();
        return true;
//        System.out.println("Removing componentscriptListeners for : " + name + ". Count: " + componentScriptListeners.count(name));
    }

/*
    public void frameComponents() {
*/
/*        Array currentlyFramedComponents = new Array();
        for (int i=0; i<componentFrames.size(); i++)
            currentlyFramedComponents.add(((JInternalFrame) componentFrames.at(i)).getContentPane().getComponent(0));
*/
/*        ESlateInternalFrame fr;
        for (int i=0; i<components.size(); i++) {
            Component comp = (Component) components.at(i);
            lc.remove(comp);

            fr = (ESlateInternalFrame) componentFrames.at(i);
            fr.getContentPane().add(comp);
            fr.setVisible(true);
*/
/*            if (!currentlyFramedComponents.contains(comp)) {
                JInternalFrame fr = createEmptyInternalFrame((ESlateHandle) eSlateHandles.at(i));
                Point p = comp.getLocation();
                Dimension dim = comp.getSize();

                if (JComponent.class.isInstance(comp))
                    ((JComponent) comp).setBorder(((CompoundBorder) ((JComponent) comp).getBorder()).getInsideBorder());
                fr.getContentPane().add(comp);
                fr.setLocation(p);
                fr.setSize(dim);
                lc.add(fr, new Integer(3));
                componentFrames.insert(i, fr);
                componentFrameIndex.put(i, new Integer(i));
            }
*/
/*        }
    }
*/

    public void ensureFrameIsVisible(ESlateInternalFrame frame, Rectangle compoBounds, boolean allowMwdExpansion) {
        if (loadingMwd) return;
        if (currentView != null && !currentView.ensureActiveComponentVisible) return;
//        System.out.println("ensureFrameIsVisible compoBounds: " + compoBounds);
//        System.out.println("ensureFrameIsVisible lc.getSize(): " + lc.getSize());
//        Thread.currentThread().dumpStack();

        int compoXEnd = compoBounds.x+compoBounds.width;
        int compoXStart = compoBounds.x;
        int compoYEnd = compoBounds.y+compoBounds.height;
        int compoYStart = compoBounds.y;

        Dimension desktopSize = lc.getSize();
        Dimension newDesktopSize = lc.getSize();
        int xShift = 0, yShift = 0;
        if (compoXStart < 0) {
            newDesktopSize.width = desktopSize.width - compoXStart;
            xShift = - compoXStart;
        }
        if (compoXEnd > desktopSize.width)
            newDesktopSize.width = newDesktopSize.width + (compoXEnd - desktopSize.width);
        if (compoYStart < 0) {
            newDesktopSize.height = desktopSize.height - compoYStart;
            yShift = - compoYStart;
        }
        if (compoYEnd > desktopSize.height)
            newDesktopSize.height = newDesktopSize.height + (compoYEnd - desktopSize.height);

//        System.out.println("newDesktopSize: " + newDesktopSize);
//        System.out.println("xShift: " + xShift + ", yShift: " + yShift);
        if (allowMwdExpansion) {
            lc.setPreferredSize(newDesktopSize);
            lc.setMinimumSize(newDesktopSize);
            lc.setMaximumSize(newDesktopSize);
            scrollPane.getViewport().setViewSize(newDesktopSize);

            if (xShift != 0 || yShift != 0) {
                for (int i=0; i<mwdComponents.size(); i++) {
                    ESlateInternalFrame fr = mwdComponents.components.get(i).frame;
                    container.frameUtils.getFrameAdapter(fr).disableMoveListener = true;
                    fr.setLocation(fr.getLocation().x + xShift, fr.getLocation().y + yShift);
                }
            }
        }

        Rectangle visibleArea = scrollPane.getViewport().getViewRect();
        Point componentLocation = null;
        Point componentEdge = null;
        if (frame != null) {
            componentLocation = frame.getDesktopItemLocation();
            componentEdge = new Point(componentLocation.x + frame.getSize().width, componentLocation.y + frame.getSize().height);
        }else{
            componentLocation = new Point(compoXStart, compoYStart);
            componentEdge = new Point(compoXEnd, compoYEnd);
        }

        if (frame != null) {
            /* Make sure the top-left edge of the component is visible, even when the component's
             * width is greater than the width of the visible area of the desktop.
             */
            Rectangle bounds = frame.getBounds();
            if (bounds.width > visibleArea.width)
                bounds.width = visibleArea.width;
            if (bounds.height > visibleArea.height)
                bounds.height = visibleArea.height;
//            System.out.println("ensureFrameIsVisible() frame.getBounds(): " + frame.getBounds() + ", bounds: " + bounds);
            lc.scrollRectToVisible(bounds);
        }else
            lc.scrollRectToVisible(compoBounds);
    }

    public void ensureFrameIsVisibleOld(Rectangle compoBounds, boolean allowMwdExpansion) {
        if (loadingMwd) return;
        if (currentView != null && !currentView.ensureActiveComponentVisible) return;
//        System.out.println("ensureFrameIsVisible compoBounds: " + compoBounds);
//        System.out.println("ensureFrameIsVisible lc.getSize(): " + scrollPane.getViewport().getViewSize());
//        Thread.currentThread().dumpStack();

        Rectangle visibleArea = scrollPane.getViewport().getViewRect();
//        System.out.println("ensureFrameIsVisible visibleArea: " + visibleArea);
        int comboXEnd = compoBounds.x+compoBounds.width;
        int comboXStart = compoBounds.x;
        int comboYEnd = compoBounds.y+compoBounds.height;
        int comboYStart = compoBounds.y;
        if (comboXEnd > (visibleArea.x + visibleArea.width) || comboYEnd > (visibleArea.y + visibleArea.height)) {
            int startX = visibleArea.x;
            int startY = visibleArea.y;
            if (comboXEnd > (visibleArea.x + visibleArea.width))
                startX = comboXEnd-visibleArea.width;
            if (!allowMwdExpansion && startX + visibleArea.width > lc.getSize().width)
                startX = lc.getSize().width - visibleArea.width;
            if (comboYEnd > (visibleArea.y + visibleArea.height))
                startY = comboYEnd-visibleArea.height;
            if (!allowMwdExpansion && startY + visibleArea.height > lc.getSize().height)
                startY = lc.getSize().height - visibleArea.height;

//            System.out.println("Before lc.getsize(): " + lc.getSize() + ", (startX + visibleArea.width): " + (startX + visibleArea.width) + ", (startY + visibleArea.height): " + (startY + visibleArea.height));
            /* If the compoBounds exceed the current size of the microworld, then the microworld has
             * to be resized to include the specified area, before this area becomes the visible part
             * of the microworld.
             */
//            System.out.println("startX: " + startX + ", startY: " + startY);
            if ((startX + visibleArea.width) > lc.getSize().width) {
                if (allowMwdExpansion) {
                    if ((startY + visibleArea.height) > lc.getSize().height) {
//                        System.out.println("Block 1");
                        Dimension newSize = new Dimension(startX + visibleArea.width, startY + visibleArea.height);
//t                        lc.setPreferredSize(newSize);
//t                        lc.setMinimumSize(newSize);
//t                        lc.setMaximumSize(newSize);
//t                        scrollPane.getViewport().setViewSize(newSize);
    //                    System.out.println("Setting view position 2");
                        scrollPane.revalidate();
                    }else{
//                        System.out.println("Block 2");
                        Dimension newSize = new Dimension(startX + visibleArea.width, lc.getSize().height);
//t                        lc.setPreferredSize(newSize);
//t                        lc.setMinimumSize(newSize);
//t                        lc.setMaximumSize(newSize);
//t                        scrollPane.getViewport().setViewSize(newSize);
    //                    System.out.println("Setting view position 3");
                        scrollPane.revalidate();
                    }
                }
            }else{
                if (allowMwdExpansion) {
                    if (startY + visibleArea.height > lc.getSize().height) {
//                        System.out.println("Block 3");
                        Dimension newSize = new Dimension(lc.getSize().width, startY + visibleArea.height);
//t                        lc.setPreferredSize(newSize);
//t                        lc.setMinimumSize(newSize);
//t                        lc.setMaximumSize(newSize);
//t                        scrollPane.getViewport().setViewSize(newSize);
    //                    System.out.println("Setting view position 4");
                        scrollPane.revalidate();
                    }
                }
            }
            Rectangle newVisibleArea = new Rectangle(startX, startY, visibleArea.width, visibleArea.height);
//            System.out.println("ensureFrameIsVisible newVisibleArea: " + newVisibleArea);
//            System.out.println("Setting view position 5 to: " + new Point(startX, startY) + ", newVisibleArea: " + newVisibleArea);
            scrollPane.getViewport().setViewPosition(new Point(startX, startY));
            scrollPane.getViewport().revalidate();
            scrollPane.revalidate();
            scrollPane.repaint();
        }else if (comboXStart < visibleArea.x || comboYStart < visibleArea.y) {
//            System.out.println("comboXStart: " + comboXStart + ", comboYStart: " + comboYStart);
            int startX = visibleArea.x;
            int startY = visibleArea.y;
            if (comboXStart < visibleArea.x)
                    startX = comboXStart;
            if (!allowMwdExpansion && startX < 0)
                startX = 0;
            if (comboYStart < visibleArea.y)
                startY = comboYStart;
            if (!allowMwdExpansion && startY < 0)
                startY = 0;

            /* The frame may be iconified and its locationmay be in the negative space. This can
             * ocurr after multiple microworld packs. In this case the whole microworld has to
             * change size and all the microworld's components will be shifted to the right and down
             * until the de-iconified frame gets positive coordinates.
             */
            Dimension newSize = new Dimension(lc.getSize().width, lc.getSize().height);
            if (comboXStart < 0) {
                newSize.width = -comboXStart + lc.getSize().width;
                startX = 0;
            }
            if (comboYStart < 0) {
                newSize.height = -comboYStart + lc.getSize().height;
                startY = 0;
            }

            /* If the frame whose bounds are passed as this method's argument was in the
             * negative space.
             */
            if (allowMwdExpansion && (newSize.width != lc.getSize().width || newSize.height != lc.getSize().height)) {
//t                lc.setPreferredSize(newSize);
//t                lc.setMinimumSize(newSize);
//t                lc.setMaximumSize(newSize);
//t                scrollPane.getViewport().setViewSize(newSize);

                /* Find the frame whose bounds are the 'compoBounds'
                 */
                ESlateInternalFrame thisFrame = null;
                ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
                for (int i=0; i<frames.length; i++) {
                    ESlateInternalFrame fr = frames[i];
                    if (fr.getBounds().equals(compoBounds)) {
                        thisFrame = fr;
                        break;
                    }
                }
                if (thisFrame != null) {
                    /* Move the rest from the the right and down.
                     */
//0                    for (int i=0; i<componentFrames.size(); i++) {
                    for (int i=0; i<frames.length; i++) {
//0                        ESlateInternalFrame fr = (ESlateInternalFrame) componentFrames.at(i);
                        ESlateInternalFrame fr = frames[i];
                        gr.cti.eslate.base.container.ESlateInternalFrameUtils.ESlateInternalFrameComponentAdapter frameAdapter = frameUtils.getFrameAdapter(fr);
                        fr.removeComponentListener(frameAdapter);
                        fr.setLocation(fr.getLocation().x-comboXStart, fr.getLocation().y-comboYStart);
                        fr.addComponentListener(frameAdapter);
                    }
                    /* Set the location of the frame for which this method is called to
                     * the start of the scrollpane's view.
                     */
                        gr.cti.eslate.base.container.ESlateInternalFrameUtils.ESlateInternalFrameComponentAdapter frameAdapter = frameUtils.getFrameAdapter(thisFrame);
//if                    ESlateInternalFrameAdapter frameAdapter = thisFrame.getFrameAdapter();
                    thisFrame.removeComponentListener(frameAdapter);
                    if (comboXStart < 0) comboXStart = 0;
                    if (comboYStart < 0) comboYStart = 0;
                    thisFrame.setLocation(comboXStart, comboYStart);
//                    System.out.println("Setting thisFrame's location to : " + comboXStart + ", " + comboYStart);
                    thisFrame.addComponentListener(frameAdapter);
                }
                scrollPane.revalidate();
            }

            Rectangle newVisibleArea = new Rectangle(startX, startY, visibleArea.width, visibleArea.height);
//            System.out.println("ensureFrameIsVisible newVisibleArea: " + newVisibleArea);
            scrollPane.getViewport().setViewPosition(new Point(startX, startY));
//            System.out.println("Setting view position 6 to: " + new Point(startX, startY));
            scrollPane.revalidate();
        }
    }

    /* When the left panel of the split pane appears, this method ensures that all the
     * scrollpane of the desktop will be properly anjusted, so that no InternalFrame
     * exceeds the desktop limits.
     */
    public void ensureDesktopContainsAllFrames() {
        Dimension lcDim = scrollPane.getViewport().getSize();
        int maxX=0, maxY=0, frameIndex;
//0        for (int i=0; i<componentFrameIndex.size(); i++) {
        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++) {
/*0            Component comp;
            if ((frameIndex = ((Integer) componentFrameIndex.at(i)).intValue()) != -1)
                comp = (Component) componentFrames.at(frameIndex);
            else
                comp = (Component) components.at(i);
            Rectangle compoBounds = comp.getBounds();
*/
            Rectangle compoBounds = frames[i].getBounds();
            if (maxX < (compoBounds.x + compoBounds.width))
                maxX = compoBounds.x + compoBounds.width;
            if (maxY < (compoBounds.y + compoBounds.height))
                maxY = compoBounds.y + compoBounds.height;
        }
        if (lcDim.width < maxX || lcDim.height < maxY) {
//            System.out.println("Resizing Desktop pane to fit the components");
            lcDim = new Dimension(maxX, maxY);
            lc.setMaximumSize(lcDim);
            lc.setMinimumSize(lcDim);
            lc.setPreferredSize(lcDim);
            scrollPane.revalidate();
        }else{
//            System.out.println("Resizing Desktop pane to fit the components");
            Dimension viewPortDim = scrollPane.getViewport().getSize();
            lc.setMaximumSize(viewPortDim);
            lc.setMinimumSize(viewPortDim);
            lc.setPreferredSize(viewPortDim);
            scrollPane.revalidate();
        }
    }

static long createFrameTimer = 0;
static long setHostedTimer = 0;
/*static long timer2 = 0;
static long timer3 = 0;
static long timer4 = 0;
static long timer5 = 0;
static long timer6 = 0;
static long timer7 = 0;
*/
    ESlateInternalFrame createComponentFrame(ESlateHandle eSlateHandle) {
        ESlateInternalFrame w;
        w = new ESlateInternalFrame(this, true, true, true, true); //if ESlateContainer.this, eSlateHandle);
long start = System.currentTimeMillis();
        try{
            w.setHostedComponent((Component) eSlateHandle.getComponent(), eSlateHandle);
setHostedTimer = setHostedTimer + (System.currentTimeMillis()-start);
        }catch (Exception exc) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            String compoName = eSlateHandle.getComponentName();
            String message = containerBundle.getString("ContainerMsg46");
            if (compoName != null)
                message = message + " \"" + compoName + "\". ";
            else
                message = message + ". ";

            dialog.setMessage(message);
            dialog.appendThrowableMessage(exc);
            dialog.createNewLine();
            dialog.appendThrowableStackTrace(exc);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
            return null;
        }

//long start7 = System.currentTimeMillis();
//        java.awt.Image img = BeanInfoFactory.get16x16BeanIcon(eSlateHandle.getComponent().getClass());
//        if (img != null)
//            w.setFrameIcon(new NewRestorableImageIcon(img)); //.getBeanIcon(eSlateHandle.getComponent().getClass(), BeanInfo.ICON_COLOR_16x16);
//timer7 = timer7 + (System.currentTimeMillis()-start7);

/*        w.addESlateInternalFrameListener(new ESlateInternalFrameAdapter() {
            public void internalFrameActivated(ESlateInternalFrameEvent e) {
            }

            public void internalFrameDeactivated(ESlateInternalFrameEvent e) {
            }
            public void internalFrameIconified(ESlateInternalFrameEvent e) {
            }
            public void internalFrameDeiconified(ESlateInternalFrameEvent e) {
            }
            public void internalFrameClosed(ESlateInternalFrameEvent e) {
            }
        });
*/
//long start2 = System.currentTimeMillis();
        w.setActiveStateDisplayed(currentView.activeComponentHighlighted);
//timer2 = timer2 + (System.currentTimeMillis()-start2);
//long start3 = System.currentTimeMillis();
        frameUtils.addComponentListener(w);
//timer3 = timer3 + (System.currentTimeMillis()-start3);
//long start4 = System.currentTimeMillis();
        frameUtils.addTitlePanelMouseListener(w);
//timer4 = timer4 + (System.currentTimeMillis()-start4);
//long start5 = System.currentTimeMillis();
        frameUtils.addPropertyChangeListener(w);
//timer5 = timer5 + (System.currentTimeMillis()-start5);
//long start6 = System.currentTimeMillis();
        frameUtils.addFrameListener(w);
//timer6 = timer6 + (System.currentTimeMillis()-start6);
createFrameTimer = createFrameTimer + (System.currentTimeMillis()-start);
        return w;
    }

    /* packMicroworld() ignores the iconified(invisible) frames.
     */
    public void packMicroworld(int side) {
        if (side == UP_SIDE) {
            int minY = getTopMostComponentYLocation();
            if (minY > 0) {
                moveComponents(0, minY);
                Dimension currDesktopSize = lc.getSize();
                currDesktopSize.height = currDesktopSize.height - minY;
                resizeDesktop(currDesktopSize);
                setMicroworldChanged(true);
            }
        }else if (side == DOWN_SIDE) {
            int maxY = getMicroworldEdges().y;
            if (maxY < scrollPane.getViewport().getExtentSize().height)
                maxY = scrollPane.getViewport().getExtentSize().height;
            Dimension currDesktopSize = lc.getSize();
            int verticalTrailingMwdEmptySpace = currDesktopSize.height - maxY;
            if (verticalTrailingMwdEmptySpace > 0) {
                currDesktopSize.height = currDesktopSize.height - verticalTrailingMwdEmptySpace;
                resizeDesktop(currDesktopSize);
                setMicroworldChanged(true);
            }
        }else if (side == LEFT_SIDE) {
            int minX = getLeftMostComponentXLocation();
            if (minX > 0) {
                moveComponents(minX, 0);
                Dimension currDesktopSize = lc.getSize();
                currDesktopSize.width = currDesktopSize.width - minX;
                resizeDesktop(currDesktopSize);
                setMicroworldChanged(true);
            }
        }else if (side == RIGHT_SIDE) {
            int maxX = getMicroworldEdges().x;
            if (maxX < scrollPane.getViewport().getExtentSize().width)
                maxX = scrollPane.getViewport().getExtentSize().width;
            Dimension currDesktopSize = lc.getSize();
            int horizontalTrailingMwdEmptySpace = currDesktopSize.width - maxX;
//                System.out.println("maxX: " + maxX + ", horizontalTrailingMwdEmptySpace: " + horizontalTrailingMwdEmptySpace);
            if (horizontalTrailingMwdEmptySpace > 0) {
                currDesktopSize.width = currDesktopSize.width - horizontalTrailingMwdEmptySpace;
                resizeDesktop(currDesktopSize);
                setMicroworldChanged(true);
            }
        }else if (side == ALL_SIDES) {
            Point p = getMicroworldEdges();
            int maxX = p.x;
            int maxY = p.y;
            int minX = 0, minY = 0;
            if (scrollPane.getHorizontalScrollBar().isVisible()) {
                minX = getLeftMostComponentXLocation();
                setMicroworldChanged(true);
            }
            if (scrollPane.getVerticalScrollBar().isVisible()) {
                minY = getTopMostComponentYLocation();
                setMicroworldChanged(true);
            }
//            System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY);
            if (minX > 0 || minY > 0)
                moveComponents(minX, minY);

            Dimension newSize = new Dimension(maxX-minX, maxY-minY);
            resizeDesktop(newSize);
            scrollPane.getViewport().setViewPosition(new Point(0, 0)); //p.x - (newSize.width - scrollPane.getSize().width), p.y));
    //        System.out.println("Setting view position 9");
            scrollPane.revalidate();
        }else
            throw new IllegalArgumentException("Invalid argument supplied to ESlateContainer packMicroworld()");
    }

    private int getTopMostComponentYLocation() {
        int minY = Integer.MAX_VALUE, frameIndex = -1;
//1        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        ESlateComponent[] components = mwdComponents.components.toArray();
        for (int i=0; i<components.length; i++) {
            Rectangle compoBounds = components[i].desktopItem.getDesktopItemBounds();
            if (minY > compoBounds.y)
                minY = compoBounds.y;
        }
/*0        for (int i=0; i<componentFrameIndex.size(); i++) {
            Component comp;
            if ((frameIndex = ((Integer) componentFrameIndex.at(i)).intValue()) != -1) {
                comp = (Component) componentFrames.at(frameIndex);
                if (((ESlateInternalFrame) comp).isIcon())
                    continue;
            }else
                comp = (Component) components.at(i);

            Rectangle compoBounds = comp.getBounds();
            if (minY > compoBounds.y)
                minY = compoBounds.y;
        }
*/
        return minY;
    }

    private int getLeftMostComponentXLocation() {
        int minX = Integer.MAX_VALUE, frameIndex = -1;
        ESlateComponent[] components = mwdComponents.components.toArray();
//1        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        for (int i=0; i<components.length; i++) {
            Rectangle compoBounds = components[i].desktopItem.getDesktopItemBounds();
            if (minX > compoBounds.x)
                minX = compoBounds.x;
        }
/*0        for (int i=0; i<componentFrameIndex.size(); i++) {
            Component comp;
            if ((frameIndex = ((Integer) componentFrameIndex.at(i)).intValue()) != -1) {
                comp = (Component) componentFrames.at(frameIndex);
                if (((ESlateInternalFrame) comp).isIcon())
                    continue;
            }else
                comp = (Component) components.at(i);

            Rectangle compoBounds = comp.getBounds();
            if (minX > compoBounds.x)
                minX = compoBounds.x;
        }
*/
        return minX;
    }

    private Point getMicroworldEdges() {
        int maxX=0, maxY=0, frameIndex;
//1        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        ESlateComponent[] components = mwdComponents.components.toArray();
        for (int i=0; i<components.length; i++) {
            Rectangle compoBounds = components[i].desktopItem.getDesktopItemBounds();
            if (maxX < (compoBounds.x + compoBounds.width))
                maxX = compoBounds.x + compoBounds.width;
            if (maxY < (compoBounds.y + compoBounds.height))
                maxY = compoBounds.y + compoBounds.height;
/*0        for (int i=0; i<componentFrameIndex.size(); i++) {
            Component comp;
            if ((frameIndex = ((Integer) componentFrameIndex.at(i)).intValue()) != -1) {
                comp = (Component) componentFrames.at(frameIndex);
                ESlateInternalFrame fr = (ESlateInternalFrame) comp;
//                System.out.println("Frame: " + fr.getTitle());
                if (fr.isIcon()) {
//                    System.out.println("Continuing...");
                    continue;
                }
            }else
                comp = (Component) components.at(i);

            Rectangle compoBounds = comp.getBounds();
            if (maxX < (compoBounds.x + compoBounds.width))
                maxX = compoBounds.x + compoBounds.width;
            if (maxY < (compoBounds.y + compoBounds.height))
                maxY = compoBounds.y + compoBounds.height;
*/
        }
        return new Point(maxX, maxY);
    }

    /* Used only by packMicroworld() */
    private void moveComponents(int dx, int dy) {
//1        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        ESlateComponent[] components = mwdComponents.components.toArray();
//0        for (int i=0; i<componentFrameIndex.size(); i++) {
        for (int i=0; i<components.length; i++) {
            /* Components are ALWAYS inside their frames, so this check is no longer necessary
             */
//            Component comp;
//            if ((frameIndex = ((Integer) componentFrameIndex.at(i)).intValue()) != -1)
//            comp = (Component) componentFrames.at(frameIndex);
//            else
//                comp = (Component) components.at(i);
//            Point p = comp.getLocation();
//            ESlateInternalFrame fr = (ESlateInternalFrame) comp;
//0            ESlateInternalFrame fr = (ESlateInternalFrame) componentFrames.at(i);
            DesktopItem desktopItem = components[i].desktopItem;
            Point p = desktopItem.getDesktopItemLocation();
            /* Temporarily remove the ComponentListener attached to the frame, so that no
             * action is taken because of the re-positioning of the ESlateInternalFrame.
             */
            gr.cti.eslate.base.container.ESlateInternalFrameUtils.ESlateInternalFrameComponentAdapter frameAdapter = null;
            if (components[i].frame != null)
                frameAdapter = frameUtils.getFrameAdapter(components[i].frame);
            if (frameAdapter != null)
                components[i].frame.removeComponentListener(frameAdapter);
            desktopItem.setDesktopItemLocation(p.x-dx, p.y-dy);
            if (frameAdapter != null)
                components[i].frame.addComponentListener(frameAdapter);
        }
    }

    private void resizeDesktop(Dimension newSize) {
        lc.setPreferredSize(newSize);
        lc.setMinimumSize(newSize);
        lc.setMaximumSize(newSize);
        scrollPane.getViewport().setViewSize(newSize);
        scrollPane.revalidate();
    }

/*    protected void adjustContainerInterface() {
        if (uiDialog == null) {
            Frame frame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this);
            uiDialog = new UIDialog(frame, this);
        }
//        uiDialog.setTitle(currentMicroworld.getName());
        if (currentView.getMicroworldBorder() != null)
            uiDialog.setBorderInsets(currentView.getMicroworldBorderInsets()); //currentView.microworldBorder.getBorderInsets(scrollPane));
        uiDialog.setBorderType(currentView.borderType);
        if (currentView.borderIcon != null) {
            uiDialog.setBorderIcon(currentView.borderIcon);
        }
        if (currentView.borderColor != null)
            uiDialog.setBorderColor(currentView.borderColor);
        uiDialog.setBackgroundType(currentView.backgroundType);
        if (currentView.getBackgroundIcon(mwdViews, currentlyOpenMwdFile) != null) {
            uiDialog.setBackgroundIcon(currentView.getBackgroundIcon(mwdViews, currentlyOpenMwdFile));
            uiDialog.setBackgroundIconDisplayMode(currentView.backgroundIconDisplayMode);
        }else
            uiDialog.setBackgroundIconDisplayMode(ImageChooser.NO_IMAGE);
        if (currentView.backgroundColor != null)
            uiDialog.setBackgroundColor(currentView.backgroundColor);
//        System.out.println("outerBorderType: " + outerBorderType);
        uiDialog.setOuterBorderType(currentView.outerBorderType);
        uiDialog.showDialog(ESlateContainer.this);

        if (uiDialog.getReturnCode() == UIDialog.DIALOG_OK) {
            //Set the microworld's border
            setMicroworldBorder(uiDialog.getBorderType(), uiDialog.getBorderColor(), uiDialog.getBorderIcon(), uiDialog.getBorderInsets());

            //Set the microworld's outer border
            setOuterBorder(uiDialog.getOuterBorderType());

            //Set the microworld's background
            setMicroworldBackground(uiDialog.getBackgroundType(), uiDialog.getBackgroundColor(), uiDialog.getBackgroundIcon(), uiDialog.getBackgroundIconDisplayMode());

        }
    }
*/
    public void setMinimizeAllowed(String componentName, boolean minimizeAllowed) {
        ESlateComponent component = getComponent(componentName);
        if (component == null) return;
        DesktopItem desktopItem = component.desktopItem;
        if (desktopItem.isIconifiable() == minimizeAllowed) return;
        desktopItem.setIconifiable(minimizeAllowed);
        setMicroworldChanged(true);
    }

    public boolean isMinimizeAllowed(String componentName) {
        ESlateComponent component = getComponent(componentName);
        if (component == null) return false;
        return component.desktopItem.isIconifiable();
    }

    public void setMaximizeAllowed(String componentName, boolean maximizeAllowed) {
        ESlateComponent component = getComponent(componentName);
        if (component == null) return;
        DesktopItem desktopItem = component.desktopItem;
        if (desktopItem.isMaximizable() == maximizeAllowed) return;
        desktopItem.setMaximizable(maximizeAllowed);
        setMicroworldChanged(true);
    }

    public boolean isMaximizeAllowed(String componentName) {
        ESlateComponent component = getComponent(componentName);
        if (component == null) return false;
        return component.desktopItem.isMaximizable();
    }

    public void setCloseAllowed(String componentName, boolean closable) {
        ESlateComponent component = getComponent(componentName);
        if (component == null) return;
        DesktopItem desktopItem = component.desktopItem;
        if (desktopItem.isClosable() == closable) return;
        desktopItem.setClosable(closable);
        setMicroworldChanged(true);
    }

    public boolean isCloseAllowed(String componentName) {
        ESlateComponent component = getComponent(componentName);
        if (component == null) return false;
        return component.desktopItem.isClosable();
    }

    public void setControlBarTitleActive(String componentName, boolean titleActive) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.displaysESlateMenuBar()) return;
        ESlateInternalFrame frame = component.frame;
        if (frame == null) return;
        if (frame.isComponentNameChangeableFromMenuBar() == titleActive) return;
        frame.setComponentNameChangeableFromMenuBar(titleActive);
        setMicroworldChanged(true);
    }

    public boolean isControlBarTitleActive(String componentName) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.displaysESlateMenuBar()) return false;
        ESlateInternalFrame frame = component.frame;
        if (frame == null) return false;
        return frame.isComponentNameChangeableFromMenuBar();
    }

    public void setPinButtonVisible(String componentName, boolean pinControlEnabled) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.displaysESlateMenuBar()) return;
        ESlateInternalFrame frame = component.frame;
        if (frame == null) return;
        if (frame.isPlugButtonVisible() == pinControlEnabled) return;
        frame.setPlugButtonVisible(pinControlEnabled);
        setMicroworldChanged(true);
    }

    public boolean isPinButtonVisible(String componentName) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.displaysESlateMenuBar()) return false;
        ESlateInternalFrame frame = component.frame;
        if (frame == null) return false;
        return frame.isPlugButtonVisible();
    }

    public void setHelpButtonVisible(String componentName, boolean helpControlEnabled) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.displaysESlateMenuBar()) return;
        ESlateInternalFrame frame = component.frame;
        if (frame == null) return;
        if (frame.isHelpButtonVisible() == helpControlEnabled) return;
        frame.setHelpButtonVisible(helpControlEnabled);
        setMicroworldChanged(true);
    }

    public boolean isHelpButtonVisible(String componentName) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.displaysESlateMenuBar()) return false;
        ESlateInternalFrame frame = component.frame;
        if (frame == null) return false;
        return frame.isHelpButtonVisible();
    }

    public void setInfoButtonVisible(String componentName, boolean infoControlEnabled) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.displaysESlateMenuBar()) return;
        ESlateInternalFrame frame = component.frame;
        if (frame == null) return;
        if (frame.isInfoButtonVisible() == infoControlEnabled) return;
        frame.setInfoButtonVisible(infoControlEnabled);
        setMicroworldChanged(true);
    }

    public boolean isInfoButtonVisible(String componentName) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.displaysESlateMenuBar()) return false;
        ESlateInternalFrame frame = component.frame;
        if (frame == null) return false;
        return frame.isInfoButtonVisible();
    }

    public void setResizable(String componentName, boolean resizeAllowed) {
        ESlateComponent component = getComponent(componentName);
        if (component == null) return;
        DesktopItem desktopItem = component.desktopItem;
        if (desktopItem.isDesktopItemResizable() == resizeAllowed) return;
        desktopItem.setDesktopItemResizable(resizeAllowed);
        setMicroworldChanged(true);
    }

    public boolean isResizable(String componentName) {
        ESlateComponent component = getComponent(componentName);
        if (component == null) return false;
        return component.desktopItem.isDesktopItemResizable();
    }

    public void setTitlePanelVisible(String componentName, boolean visible) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.displaysESlateMenuBar()) return;
        DesktopItem desktopItem = component.desktopItem;
        if (desktopItem == null) return;
        /* This checks for old-type frozen components and unfreezes them */
        ESlateInternalFrame frame = component.frame;
//if        if (visible && frame != null && frame.isFrozen())
//if            frame.setFrozen(false);
        if (desktopItem.isTitlePanelVisible() == visible) return;
        desktopItem.setTitlePanelVisible(visible);
        setMicroworldChanged(true);
    }

    public boolean isTitlePanelVisible(String componentName) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.displaysESlateMenuBar()) return false;
        DesktopItem desktopItem = component.desktopItem;
        if (desktopItem == null) return false;
        return desktopItem.isTitlePanelVisible();
    }

/*    public void setFrozen(String componentName, boolean frozen) {
        ESlateInternalFrame frame = getComponentFrame(componentName);
        if (frame == null) return;
        if (frame.isFrozen() == frozen) return;
        frame.setFrozen(frozen);
        microworldChanged = true;
    }

    public boolean isFrozen(String componentName) {
        ESlateInternalFrame frame = getComponentFrame(componentName);
        if (frame == null) return false;
        return frame.isFrozen();
    }
*/
    public void setComponentActivatedOnMouseClick(String componentName, boolean activate) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.usesGlassPane()) return;
        ESlateInternalFrame frame = component.frame;
        if (frame == null) return;
        if (frame.isComponentActivatedOnMouseClick() == activate) return;
        frame.setComponentActivatedOnMouseClick(activate);
        setMicroworldChanged(true);
    }

    public boolean isComponentActivatedOnMouseClick(String componentName) {
        ESlateComponent component = getComponent(componentName);
        if (!component.desktopItem.usesGlassPane()) return false;
        ESlateInternalFrame frame = component.frame;
        if (frame == null) return false;
        return frame.isComponentActivatedOnMouseClick();
    }

    public final void setMinimizeButtonVisible(boolean visible) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setMinimizeButtonVisibleInternal(visible);
    }
    private final void setMinimizeButtonVisibleInternal(boolean visible) {
        if (microworld == null) return;
        if (currentView.minimizeButtonVisible == visible) return;
        currentView.minimizeButtonVisible = visible;
        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++)
            frames[i].setMinimizeButtonVisible(visible);
//0        for (int i=0; i<componentFrames.size(); i++)
//0            ((ESlateInternalFrame) componentFrames.at(i)).setFrameIconifiable(minimizeAllowed);
        setMicroworldChanged(true);
    }
    public boolean isMinimizeButtonVisible() {
        return currentView.minimizeButtonVisible;
    }

    public final void setMaximizeButtonVisible(boolean visible) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setMaximizeButtonVisibleInternal(visible);
    }
    private final void setMaximizeButtonVisibleInternal(boolean visible) {
        if (microworld == null) return;
        if (currentView.maximizeButtonVisible == visible) return;
        currentView.maximizeButtonVisible = visible;
        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++)
            frames[i].setMaximizeButtonVisible(visible);
//0        for (int i=0; i<componentFrames.size(); i++)
//0            ((ESlateInternalFrame) componentFrames.at(i)).setFrameMaximizable(maximizeAllowed);
        setMicroworldChanged(true);
    }
    public final boolean isMaximizeButtonVisible() {
        return currentView.maximizeButtonVisible;
    }

    public final void setCloseButtonVisible(boolean visible) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setCloseButtonVisibleInternal(visible);
    }
    private final void setCloseButtonVisibleInternal(boolean visible) {
        if (microworld == null || currentView.closeButtonVisible == visible) return;
        currentView.closeButtonVisible = visible;
        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
//        ESlateComponent[] components = mwdComponents.components.toArray();
        for (int i=0; i<frames.length; i++)
            frames[i].setCloseButtonVisible(visible);
//0        for (int i=0; i<componentFrames.size(); i++)
//0            ((ESlateInternalFrame) componentFrames.at(i)).setFrameClosable(currentView.closeAllowed);
        setMicroworldChanged(true);
    }
    public final boolean isCloseButtonVisible() {
        return currentView.closeButtonVisible;
    }

    public final void setControlBarTitleActive(boolean titleActive) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setControlBarTitleActiveInternal(titleActive);
    }
    private final void setControlBarTitleActiveInternal(boolean titleActive) {
        if (microworld == null || currentView.controlBarTitleActive == titleActive) return;
        currentView.controlBarTitleActive = titleActive;
        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++)
            frames[i].setComponentNameChangeableFromMenuBar(currentView.controlBarTitleActive);
//0        for (int i=0; i<componentFrames.size(); i++)
//0            ((ESlateInternalFrame) componentFrames.at(i)).setComponentNameChangeableFromMenuBar(currentView.controlBarTitleActive);
        setMicroworldChanged(true);
    }
    public boolean isControlBarTitleActive() {
        return currentView.controlBarTitleActive;
    }

    public final void setHelpButtonVisible(boolean helpControlEnabled) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setHelpButtonVisibleInternal(helpControlEnabled);
    }
    private final void setHelpButtonVisibleInternal(boolean helpControlEnabled) {
        if (microworld == null || currentView.helpButtonVisible == helpControlEnabled) return;
        currentView.helpButtonVisible = helpControlEnabled;
        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++)
            frames[i].setHelpButtonVisible(currentView.helpButtonVisible);
//0        for (int i=0; i<componentFrames.size(); i++)
//0            ((ESlateInternalFrame) componentFrames.at(i)).setHelpButtonVisible(currentView.helpButtonVisible);
        setMicroworldChanged(true);
    }
    public final boolean isHelpButtonVisible() {
        return currentView.helpButtonVisible;
    }

    public final void setPinButtonVisible(boolean pinControlEnabled) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setPinButtonVisibleInternal(pinControlEnabled);
    }
    private final void setPinButtonVisibleInternal(boolean pinControlEnabled) {
        if (microworld == null || currentView.pinButtonVisible == pinControlEnabled) return;
        currentView.pinButtonVisible = pinControlEnabled;
        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++)
            frames[i].setPlugButtonVisible(currentView.pinButtonVisible);
//0        for (int i=0; i<componentFrames.size(); i++)
//0            ((ESlateInternalFrame) componentFrames.at(i)).setPinButtonVisible(currentView.pinButtonVisible);
        setMicroworldChanged(true);
    }
    public final boolean isPinButtonVisible() {
        return currentView.pinButtonVisible;
    }

    public final void setInfoButtonVisible(boolean infoControlEnabled) {
        if (microworld == null || currentView.infoButtonVisible == infoControlEnabled) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setInfoButtonVisibleInternal(infoControlEnabled);
    }
    private final void setInfoButtonVisibleInternal(boolean infoControlEnabled) {
        if (microworld == null || currentView.infoButtonVisible == infoControlEnabled) return;
        currentView.infoButtonVisible = infoControlEnabled;
        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++)
            frames[i].setInfoButtonVisible(currentView.infoButtonVisible);
//0        for (int i=0; i<componentFrames.size(); i++)
//0            ((ESlateInternalFrame) componentFrames.at(i)).setInfoButtonVisible(currentView.infoButtonVisible);
        setMicroworldChanged(true);
    }
    public final boolean isInfoButtonVisible() {
        return currentView.infoButtonVisible;
    }

    public final void setResizeAllowed(boolean resizeAllowed) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setResizeAllowedInternal(resizeAllowed);
    }
    final void setResizeAllowedInternal(boolean resizeAllowed) {
        if (microworld == null || currentView.resizeAllowed == resizeAllowed) return;
        currentView.resizeAllowed = resizeAllowed;
//1        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        ESlateComponent[] components = mwdComponents.components.toArray();
        for (int i=0; i<components.length; i++)
            components[i].desktopItem.setDesktopItemResizable(resizeAllowed);
//0        for (int i=0; i<componentFrames.size(); i++)
//0            ((ESlateInternalFrame) componentFrames.at(i)).setResizable(resizeAllowed);
        setMicroworldChanged(true);
    }
    public final boolean isResizeAllowed() {
        return currentView.resizeAllowed;
    }


    public final void toggleComponentPanel() {
        if (microworld != null)
            microworld.checkActionPriviledge(microworld.eslateComponentBarEnabled, "eslateComponentBarEnabled");
        toggleComponentPanelInternal();
    }
    final void toggleComponentPanelInternal() {
    	setComponentPanelVisible(!isComponentPanelVisible());
    }
    
    public void setComponentPanelVisible(boolean visible) {
    	if (isComponentPanelVisible()==visible)
    		return;
        if (componentBar == null)
            componentBar = new ComponentPanel(ESlateContainer.this);

        if (visible) {
            componentBar.synchronizeComponentPanel();
            southPanel.add(componentBar);
        }else{
            southPanel.remove(componentBar);
        }
        southPanel.revalidate();
        scrollPane.revalidate();
    }
    
    public boolean isComponentPanelVisible() {
    	return componentBar!=null && componentBar.getParent()!=null;
    }

    public final void setControlBarsVisible(boolean controlBarsVisible) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setControlBarsVisibleInternal(controlBarsVisible);
    }
    private final void setControlBarsVisibleInternal(boolean controlBarsVisible) {
        if (microworld == null || currentView.controlBarsVisible == controlBarsVisible) return;
        currentView.controlBarsVisible = controlBarsVisible;
        ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++) {
//if            if (frames[i].isFrozen())
//if                frames[i].setFrozen(false);
            frames[i].setTitlePanelVisible(controlBarsVisible);
        }
//0        for (int i=0; i<componentFrames.size(); i++) {
            /* This unfreezes the old-type frozen components. This mode is not supported anymore.
             */
/*0            ESlateInternalFrame fr = (ESlateInternalFrame) componentFrames.at(i);
            if (fr.isFrozen())
                fr.setFrozen(false);
            fr.setTitlePanelVisible(controlBarsVisible);
        }
*/
        setMicroworldChanged(true);
    }
    public final boolean isControlBarsVisible() {
        return currentView.controlBarsVisible;
    }

    public final void setMoveAllowed(boolean moveAllowed) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setMoveAllowedInternal(moveAllowed);
    }
    private final void setMoveAllowedInternal(boolean moveAllowed) {
        if (microworld == null || currentView.moveAllowed == moveAllowed) return;
        currentView.moveAllowed = moveAllowed;
        if (!moveAllowed)
            ((DesktopPane) lc).disableFrameDragging();
        else
            ((DesktopPane) lc).enableFrameDragging();
        setMicroworldChanged(true);
    }
    public final boolean isMoveAllowed() {
        return currentView.moveAllowed;
    }

/*    public void setNameChangeAllowed(boolean nameChangeAllowed) {
        if (microworld == null) return;
        if (microworld.isLocked())
            throw new MicroworldLockedException(containerBundle.getString("MicroworldLockedException"));
        if (microworld.eslateMwd.isRenamingAllowed() != nameChangeAllowed) {
            currentView.nameChangeAllowed = nameChangeAllowed;
            microworld.eslateMwd.setRenamingAllowed(nameChangeAllowed);
//            System.out.println("microworldChanged 7");
            setMicroworldChanged(true);
        }
    }
    public boolean isNameChangeAllowed() {
        return currentView.nameChangeAllowed;
    }

    public void setComponentInstantiationAllowed(boolean componentInstantiationAllowed) {
        if (microworld == null) return;
        if (microworld.isLocked())
            throw new MicroworldLockedException(containerBundle.getString("MicroworldLockedException"));
        if (componentInstantiationAllowed != currentView.componentInstantiationAllowed) {
            currentView.componentInstantiationAllowed = !currentView.componentInstantiationAllowed;
//            System.out.println("microworldChanged 8");
            setMicroworldChanged(true);
        }
    }
    public boolean isComponentInstantiationAllowed() {
        return currentView.componentInstantiationAllowed;
    }

    public void setComponentRemovalAllowed(boolean componentRemovalAllowed) {
        if (microworld == null)
            return;
        if (microworld.isLocked())
            throw new MicroworldLockedException(containerBundle.getString("MicroworldLockedException"));
        if (currentView.componentRemovalAllowed != componentRemovalAllowed) {
            currentView.componentRemovalAllowed = !currentView.componentRemovalAllowed;
            //Disable the close buttons on the control bars
            if (!currentView.componentRemovalAllowed) {
                ESlateInternalFrame[] frames = mwdComponents.getComponentFrames();
                for (int i=0; i<frames.length; i++)
                    frames[i].setClosable(currentView.componentRemovalAllowed);
//0                for (int i=0; i<componentFrames.size(); i++)
//0                    ((ESlateInternalFrame) componentFrames.at(i)).setFrameClosable(currentView.componentRemovalAllowed);
            }
//            System.out.println("microworldChanged 9");
            setMicroworldChanged(true);
        }
    }
    public boolean isComponentRemovalAllowed() {
        return currentView.componentRemovalAllowed;
    }
*/
    public final void setMwdBgrdChangeAllowed(boolean mwdBgrdChangeAllowed) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setMwdBgrdChangeAllowedInternal(mwdBgrdChangeAllowed);
    }
    final void setMwdBgrdChangeAllowedInternal(boolean mwdBgrdChangeAllowed) {
        if (microworld == null) return;
        if (currentView.mwdBgrdChangeAllowed != mwdBgrdChangeAllowed) {
            currentView.mwdBgrdChangeAllowed = !currentView.mwdBgrdChangeAllowed;
//            System.out.println("microworldChanged 10");
            setMicroworldChanged(true);
        }
    }
    public final boolean isMwdBgrdChangeAllowed() {
        return currentView.mwdBgrdChangeAllowed;
    }

    public final void setPlugConnectionChangeAllowed(boolean plugConnectionChangeAllowed) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setPlugConnectionChangeAllowedInternal(plugConnectionChangeAllowed);
    }

    private final void setPlugConnectionChangeAllowedInternal(boolean plugConnectionChangeAllowed) {
        if (microworld == null) return;
        if (currentView.plugConnectionChangeAllowed != plugConnectionChangeAllowed) {
            currentView.plugConnectionChangeAllowed = !currentView.plugConnectionChangeAllowed;
            for (int i=0; i<mwdComponents.size(); i++) {
                mwdComponents.components.get(i).handle.getMenuPanel().setPlugViewWindowEnabled(currentView.plugConnectionChangeAllowed);
            }
/*0            for (int i=0; i<eSlateHandles.size(); i++) {
                ((ESlateHandle) eSlateHandles.at(i)).getMenuPanel().setPinViewWindowEnabled(currentView.mwdPinViewEnabled);
            }
*/
//            System.out.println("microworldChanged 12");
            setMicroworldChanged(true);
        }
    }
    public boolean isPlugConnectionChangeAllowed() {
        return currentView.plugConnectionChangeAllowed;
    }

    /** Shortcut-method to setMicroworldNameUserDefined() of Microworld class. */
    public void setMicroworldNameUserDefined(boolean userDefined) {
        if (microworld == null) return;
        microworld.setMicroworldNameUserDefined(userDefined);
    }

    public boolean isMicroworldNameUserDefined() {
        return microworld.microworldNameUserDefined;
    }

    public final void setOutlineDragEnabled(boolean enabled) {
        if (microworld == null) return;
        microworld.checkSettingChangePriviledge();
        setOutlineDragEnabledInternal(enabled);
    }
    private final void setOutlineDragEnabledInternal(boolean enabled) {
        if (microworld == null || currentView.outlineDragEnabled == enabled) return;
        currentView.outlineDragEnabled = enabled;
        if (currentView.outlineDragEnabled) {
            lc.putClientProperty("JDesktopPane.dragMode", "outline");
//            lc.putClientProperty("JDesktopPane.dragMode", "faster");
        }else{
            lc.putClientProperty("JDesktopPane.dragMode", null);
        }
        setMicroworldChanged(true);
    }
    public final boolean isOutlineDragEnabled() {
        return currentView.outlineDragEnabled;
    }

/*    public void setComponentFrozenStateChangeAllowed(boolean allowed) {
        if (componentFrozenStateChangeAllowed == allowed) return;
        componentFrozenStateChangeAllowed = allowed;
        microworldChanged = true;
    }
    public boolean isComponentFrozenStateChangeAllowed() {
        return componentFrozenStateChangeAllowed;
    }
*/
    public final void setComponentActivationMethodChangeAllowed(boolean activated) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setComponentActivationMethodChangeAllowedInternal(activated);
    }
    private final void setComponentActivationMethodChangeAllowedInternal(boolean activated) {
        if (microworld == null || currentView.componentActivationMethodChangeAllowed == activated) return;
        currentView.componentActivationMethodChangeAllowed = activated;
        setMicroworldChanged(true);
    }
    public final boolean isComponentActivationMethodChangeAllowed() {
        return currentView.componentActivationMethodChangeAllowed;
    }

    public void setMicroworldBorder(int borderType, Color borderColor, NewRestorableImageIcon borderIcon, Insets insets) {
        /* The variable "microworldBorder" is simply used to store the insets
         * of the microwrold's inner border. It does not store the actual border,
         * since the actual border may be the outer border, or a combination of
         * an inner and an outer border.
         */
        currentView.borderType = borderType;
        if (borderType == UIDialog.NO_BORDER) {
            currentView.setMicroworldBorder(this, null); //currentView.microworldBorder = null;
            scrollPane.setViewportBorder(defaultMicroworldBorder);
            currentView.borderIcon = null;
        }else if (borderType == UIDialog.COLOR_BORDER) {
            currentView.borderColor = borderColor;
            currentView.borderIcon = null;
            currentView.setMicroworldBorder(this, null); //currentView.microworldBorder = null;
//            Insets insets = ;
            currentView.setMicroworldBorder(this, new MatteBorder(insets.top, insets.left, insets.bottom, insets.right, currentView.borderColor));
            scrollPane.setViewportBorder(currentView.getMicroworldBorder());
        }else if (borderType == UIDialog.ICON_BORDER) {
            currentView.borderIcon = borderIcon;
//            Insets insets = uiDialog.getBorderInsets();
            currentView.setMicroworldBorder(this, new MatteBorder(insets.top, insets.left, insets.bottom, insets.right, borderIcon));
            scrollPane.setViewportBorder(currentView.getMicroworldBorder());
        }
        setMicroworldChanged(true);
//        System.out.println("microworldChanged 14");
    }

    // Adjusts the microworld's outer border
    public void setOuterBorder(int outerBorderType) {
        currentView.outerBorderType = outerBorderType;
        if (outerBorderType == UIDialog.OUTER_BORDER_RAISED) {
            if (currentView.getMicroworldBorder() != null)
                scrollPane.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED), currentView.getMicroworldBorder()));
            else
                scrollPane.setBorder(new BevelBorder(BevelBorder.RAISED));
        }else if (outerBorderType == UIDialog.OUTER_BORDER_LOWERED) {
            if (currentView.getMicroworldBorder() != null)
                scrollPane.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED), currentView.getMicroworldBorder()));
            else
                scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
        }else if (outerBorderType == UIDialog.OUTER_BORDER_NONE) {
            if (currentView.getMicroworldBorder() != null)
                scrollPane.setBorder(currentView.getMicroworldBorder());
            else
                scrollPane.setBorder(defaultMicroworldBorder);
        }
        setMicroworldChanged(true);
//        System.out.println("microworldChanged 15");
    }

    // Set the background properties of the microworld
    public void setMicroworldBackground(int backgroundType, Color backgroundColor, NewRestorableImageIcon backgroundIcon, int iconDisplayMode) {
        if (backgroundIcon!=null)
        	backgroundIcon.setSaveFormat(NewRestorableImageIcon.PNG);
        currentView.backgroundType = backgroundType;
//        if (backgroundColor != null)
//        if (backgroundIcon != null)
        if (backgroundType == uiDialog.BACKGROUND_NONE) {
//            System.out.println("setMicroworldBackground 1");
            lc.setBackground(UIManager.getColor("controlShadow"));
            ((DesktopPane) lc).setIcon(null);
//            System.out.println("setMicroworldBackground 1.2");
            lc.repaint();
            if (currentView.getBackgroundIcon(mwdViews, currentlyOpenMwdFile) != null) {
//                currentView.getBackgroundIcon(currentlyOpenMwdFile).getImage().flush();
                currentView.setBackgroundIcon(null);
//            System.out.println("Flushing icon");
//                this.backgroundIcon = null;
            }
        }else if (backgroundType == uiDialog.ICON_COLORED_BACKGROUND) {
//            System.out.println("setMicroworldBackground 2");
            currentView.backgroundColor = backgroundColor;
            if (backgroundIcon == null) {
                if (currentView.getBackgroundIcon(mwdViews, currentlyOpenMwdFile) != null) {
                    ((DesktopPane) lc).setIcon(null);
//                    System.out.println("Flushing icon");
//                    currentView.backgroundIcon.getImage().flush();
                    currentView.setBackgroundIcon(null);
                }
                currentView.backgroundIconDisplayMode = ImageChooser.NO_IMAGE;
            }else{
                currentView.setBackgroundIcon(backgroundIcon);
                if (iconDisplayMode == ImageChooser.CENTER_IMAGE ||
                  iconDisplayMode == ImageChooser.FIT_IMAGE ||
                  iconDisplayMode == ImageChooser.TILE_IMAGE)
                    currentView.backgroundIconDisplayMode = iconDisplayMode;
            }

//            System.out.println("Setting JDestopPane icon to: " + backgroundIcon);
            ((DesktopPane) lc).setIcon(backgroundIcon);
            ((DesktopPane) lc).setIconDisplayMode(currentView.backgroundIconDisplayMode);
            lc.setBackground(backgroundColor);
            lc.repaint();
        }/*else if (backgroundType == uiDialog.ICON_BACKGROUND) {
            this.backgroundIcon = backgroundIcon;
            ((DesktopPane) lc).setTileIcon(backgroundIcon);
            lc.repaint();
        }
*/
        setMicroworldChanged(true);
//        System.out.println("microworldChanged 16");
    }

    // Set the background properties of the microworld
    public void setBackgroundColor(Color backgroundColor) {
        currentView.backgroundColor = backgroundColor;
        if (currentView.backgroundType == uiDialog.ICON_COLORED_BACKGROUND) {
            lc.setBackground(backgroundColor);
            lc.repaint();
        }
    }

    public Color getBackgroundColor() {
        if (currentView == null) return null;
        return currentView.backgroundColor;
    }

    public void setContainerTitleEnabled(boolean enabled) {
        if (containerTitleEnabled == enabled) return;
        containerTitleEnabled = enabled;
        if (titlePanel != null)
            titlePanel.setVisible(containerTitleEnabled);
    }

    public boolean isContainerTitleEnabled() {
        return containerTitleEnabled;
    }

    private String getAppName() {
        return ""; //containerBundle.getString("ESlate");
    }

    // Adjust the microworld's title
    public void setContainerTitle(String title) {
        if (!containerTitleEnabled) return;
//        System.out.println("setContainerTitle()");
        String activityTitle = getAppName(); //containerBundle.getString("ESlate");

        if (title != null) {
            if (activityTitle != null && activityTitle.trim().length() != 0)
                activityTitle = activityTitle + " - ";
            activityTitle = activityTitle + title;
        }
        /* Check if the parentComponent contains a method named setTitle(). If it
         * does, then use it to set the container's title. If not, then create your
         * own title panel and add it to the top of the Container.
         */
        boolean useTitlePanel = false;
        if (methodSetTitle == null && parentComponent != null) {
            try{

                Class superClass = parentComponent.getClass();
                while (methodSetTitle == null && superClass != null) {
                    Method[] methods = superClass.getDeclaredMethods();
                    for (int i=0; i<methods.length; i++) {
                        if (methods[i].getName().equals("setTitle")) {
                            methodSetTitle = methods[i];
                            break;
                        }
                    }
                    superClass = superClass.getSuperclass();
                }
            }catch (Throwable exc) {
                methodSetTitle = null;
            }
        }
        if (methodSetTitle != null) {
            try{
                String[] args = new String[1];
                args[0] = activityTitle;
//                                        System.out.println("methodSetTitle: " + methodSetTitle);
                methodSetTitle.invoke(parentComponent, args);
            }catch (Throwable exc) {
                System.out.println(exc.getClass() + ", " + exc.getMessage());
                useTitlePanel = true;
            }
        }else{
            useTitlePanel = true;
        }
        if (methodSetTitle == null || useTitlePanel) {
            if (titlePanel == null) {
                titlePanel = new TitlePanel(activityTitle, currentView.borderColor);
                Component menuBar = (this instanceof ESlateContainer)?null:container.getComponent(0);
                JPanel topPanel = new JPanel(true);
                topPanel.setLayout(new BorderLayout());
                topPanel.add(titlePanel, BorderLayout.NORTH);
                if (menuBar != null) {
                    topPanel.add(menuBar, BorderLayout.SOUTH);
                    container.remove(menuBar);
                }
                container.add(topPanel, BorderLayout.NORTH);
                container.revalidate();
            }else{
                titlePanel.setTitle(activityTitle, currentView.borderColor);
            }
        }
    }

    /** Open the file dialog in the specified mode and with the specified message
      * in order to pick a file on the file system.
      * @param save Detrmines whether as SAVE or LOAD file dialog will open
      * @param message The title of the file dialog.
      * @param fileName The initial file name.
      * @param extensions The extensions used by the file dialog.
      * @return The file chosen or null if the dialog is cancelled.
      */
    public String getSystemFile(boolean save, String message, String fileName, String[] extensions) {
        int fileDialogMode = FileDialog.SAVE;
        if (!save)
            fileDialogMode = FileDialog.LOAD;

        if (fileDialog == null) {
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this);
            fileDialog = new ESlateFileDialog(topLevelFrame, message, fileDialogMode);
            if (currentFileDialogDir != null) {
                if (new File(currentFileDialogDir).exists())
                    fileDialog.setDirectory(currentFileDialogDir);
            }
        }

        if (fileDialog.isShowing()) {
            return null;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        fileDialog.setTitle(message);
        fileDialog.setMode(fileDialogMode);
        if (fileName == null) {
            /* Formulate the string to use in fileDialog.setFile(). This string contains all the
             * valid microworld file extensions as they are declared in the container.properties file.
             */
            if (extensions != null) {
                String extensionStr = "";
                for (int i=0; i<extensions.length-1; i++)
                    extensionStr = extensionStr + "*." + extensions[i] + "; ";
                extensionStr = extensionStr + "*." + extensions[extensions.length-1];
                fileDialog.setFile(extensionStr);
                fileDialog.setDefaultExtension((String[]) extensions.clone());
            }
        }else{
            fileDialog.setFile(fileName);
            if (extensions != null)
                fileDialog.setDefaultExtension((String[]) extensions.clone());
        }

        /* Center the fileDialog inside the Container.
         */
        fileDialog.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        Rectangle compBounds = this.getBounds();
        java.awt.Point compLocation = this.getLocationOnScreen();
//        System.out.println("compBounds: " + compBounds + ", compLocation: " + compLocation);
        Dimension fileDialogSize = new Dimension(426, 264);
//        System.out.println("File dialog size: " + fileDialogSize);
        x = compLocation.x + compBounds.width/2 - fileDialogSize.width/2;
        y = compLocation.y + compBounds.height/2-fileDialogSize.height/2;
        if (x+fileDialogSize.width > screenSize.width)
            x = screenSize.width - fileDialogSize.width;
        if (y+fileDialogSize.height > screenSize.height)
            y = screenSize.height - fileDialogSize.height;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        fileDialog.setLocation(x, y);
        fileDialog.show();

        /* When a big microworld file gets loaded, the area of the ESlateContainer which
         * is covered by the file dialog does not get repainted until the microworld is
         * loaded. Thus the ESlateContainer contains a gray area for some time, which
         * depends on the microworld load time. I tried to correct it using invokeLater()
         * but it didn't work. Thus I use paintImmediately here. This happens only on
         * Windows OS. I assume that the fileDialog size is 'fileDialogSize' and that
         * it's left corner is a screen location 2, 10.
         */
/*        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") != -1) {
            Rectangle eslateRect = new Rectangle(compLocation.x, compLocation.y, compBounds.width, compBounds.height);
            Rectangle fileDialogRect = new Rectangle(2, 10, fileDialogSize.width+20, fileDialogSize.height+20);
            Rectangle intersection = eslateRect.intersection(fileDialogRect);
            Point p = intersection.getLocation();
            System.out.println("intersection: " + intersection);
            SwingUtilities.convertPointFromScreen(p, this);
            intersection.x = p.x;
            intersection.y = p.y;
            System.out.println("2. intersection: " + intersection);
            paintImmediately(intersection);
        }
*/
        /* The code above was cancelled cause if the user moves the fileDialog somewhere
         * else from the place it first appeared, then the code won't work. There is not
         * way to catch events (Window, Component events) on the fileDialog, because it's
         * native, so there is no way to know where exactly on the screen it lays. Threfore
         * we repaint the whole ESlateContainer.
         */
        paintImmediately(getVisibleRect());

        /* When in English locale FileDialog's getFile() returns null when the path contains
         * greek letters.
         */
        String mwdFileName = fileDialog.getFile();
//        System.out.println("getSystemFile() mwdFileName: " + fileDialog.getFile());
//        System.out.println("getSystemFile() mwdFileName: " + fileDialog.getDirectory());
        if (mwdFileName == null) {
            setCursor(Cursor.getDefaultCursor(), false);
            return null;
        }
        String currDirectory = fileDialog.getDirectory();
        mwdFileName = currDirectory+mwdFileName;

        setCursor(Cursor.getDefaultCursor(), false);
        return mwdFileName;
    }

    /** Saves the current microworld to the file it was loaded from.
      */
    public boolean saveMicroworld(boolean enableWaitDialog) {
        if (microworld == null)
            return false;

        /* If this microworld was loaded from a file, then save it back to the
         * file it was loaded from.
         */
        if (currentlyOpenMwdFileName != null) {
            if (!openFileRemote) {
System.out.println("currentlyOpenMwdFileName: " + currentlyOpenMwdFileName);
                if (!saveAs(currentlyOpenMwdFileName, enableWaitDialog)) {
//                    ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg16") + currentlyOpenMwdFileName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }else{
//                    System.out.println("1. saveMicroworld(): setting microworld changed to false");
                    setMicroworldChanged(false);
                    return true;
                }
            }else{
////nikosM
                if (!webServerMicrosHandle.saveFileToServer(webServerMicrosHandle.webSite, currentlyOpenMwdFileName)) {
//// nikosM end
                    ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg11"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }else{
//                    System.out.println("2. saveMicroworld(): setting microworld changed to false");
                    setMicroworldChanged(false);
                    return true;
                }
            }
        }else{
            /* If this is a new microworld, which is saved for the first time, then prompt
             * for a local file.
             */
            /* This method is an action controlled by a microworld setting. When the setting forbits
             * the action, there is no way the action can be taked by anyone no matter if the microworld
             * is locked or not.
             */
            microworld.checkActionPriviledge(microworld.mwdStorageAllowed, "mwdStorageAllowed");

            String fileName = getSystemFile(true, containerBundle.getString("ContainerMsg7"), null, mwdFileExtensions);
            if (fileName != null) {
                if (saveAs(fileName, enableWaitDialog)) {
                    currentlyOpenMwdFileName = fileName;
                    if (!microworld.microworldNameUserDefined)
                        setContainerTitle(ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
                    openFileRemote = false;
//// nikosM
                    webServerMicrosHandle.webSite = null;
                    // Update the history list
                    mwdHistory.addToHistory(fileName);
                    containerUtils.fireMwdHistoryChanged(new MwdHistoryChangedEvent(this));
                    microworldList.updateMicroworldList(fileName,false);
//// nikosM end
//                    System.out.println("3. saveMicroworld(): setting microworld changed to false");
                    setMicroworldChanged(false);
                }else
                    return false;
            }else
                return false;
            return true;
        }
    }

    public void saveAsLocalMicroworld(boolean enableWaitDialog) {
        if (microworld == null) return;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        microworld.checkActionPriviledge(microworld.mwdStorageAllowed, "mwdStorageAllowed");

        String currentFileName = null;
        if (currentlyOpenMwdFileName != null)
            currentFileName = ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, false);
        String fileName = getSystemFile(true, containerBundle.getString("ContainerMsg7"), currentFileName, mwdFileExtensions);
        if (fileName != null) {
            if (saveAs(fileName, enableWaitDialog)) {
                currentlyOpenMwdFileName = fileName;
                if (!microworld.microworldNameUserDefined)
                    setContainerTitle(ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
                openFileRemote = false;
//// nikosM
                webServerMicrosHandle.webSite = null;
                // Update the history list
                mwdHistory.addToHistory(fileName);
                containerUtils.fireMwdHistoryChanged(new MwdHistoryChangedEvent(ESlateContainer.this));
//                microworldList.updateMicroworldList(fileName);
                microworldList.updateMicroworldList(fileName,false);
//// nikosM end
                setMicroworldChanged(false);
            }
        }
    }

////nikosM saveAs
    boolean saveAs(String mwdFileName, boolean enableWaitDialog) {
        if (microworld == null) return false;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        microworld.checkActionPriviledge(microworld.mwdStorageAllowed, "mwdStorageAllowed");

        WaitDialogTimerTask wtt = null;
        if (enableWaitDialog) {
             wtt = new WaitDialogTimerTask(this, false, false);
             wtt.setDialogTitle(microworld.getMwdSaveProgressMsg());
             wtt.setProgressInfoDisplayed(microworld.isMwdSaveProgressInfoDisplayed());
             wtt.setDialogTitleColor(microworld.getProgressDialogTitleColor());
             wtt.setDialogTitleFont(microworld.getProgressDialogTitleFont());
             wtt.timer.schedule(wtt, new java.util.Date(System.currentTimeMillis()+2000));
             wtt.setMessage(containerBundle.getString("Saving1"));
        }
        boolean result=saveAs(mwdFileName,wtt/*waitDialog*/);
        if (wtt != null)
            wtt.disposeDialog();
/*        if (enableWaitDialog && wtt!= null) {
             wtt.disposeDialog();
             timer.cancel();
        }
*/
        return result;
    }
////nikosM end of saveAs


    /**
     * Returns the state of the microworld which has to do with the ESlateContainer.
     */
    protected ESlateFieldMap2 getState() {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(MWD_STR_FORMAT_VERSION);
        fieldMap.put("Script listeners", componentScriptListeners.saveMap());
        fieldMap.put("Global Scripts", scriptMap);
//            componentScriptListeners.saveMap(out); //xml

        /* Store the Microworld properties */
        fieldMap.put("Microworld properties", microworld.getMicroworldProperties());

        /* Store the views of the microworld */
        fieldMap.put("Microworld View List", mwdViews);

        /* Create the ComponentViewInfo structure which fully describes the presentation
         * attributes of each component. This info is tracked down here cause -just before
         * it is saved- so that it is accurate.
         */
        currentView.viewPositionX = scrollPane.getViewport().getViewPosition().x;
        currentView.viewPositionY = scrollPane.getViewport().getViewPosition().y;
//            System.out.println("saveAs(): " + mwdComponents.activeComponent);
        if (mwdComponents.activeComponent != null)
            currentView.activeComponentName = mwdComponents.activeComponent.handle.getComponentName();
        currentView.createDesktopItemInfos(this, true);
        currentView.microworldInfoSaved = true;
        fieldMap.put("Microworld View", currentView);

        fieldMap.put("DesktopSize", lc.getPreferredSize());

        ESlateComponent[] arrangedComponents = arrangeMicroworldComponentBasedOnSelectionHistory();
        Array componentClassNames = new Array();
        for (int i=0; i<arrangedComponents.length; i++) {
              Object component;
              component = arrangedComponents[i].object;
              componentClassNames.add(component.getClass().getName());
        }
        fieldMap.put("ComponentClassNames", componentClassNames);
        fieldMap.put("ComponentFrameIndex", new Array());
        /* Save the components which are not Externalizable, but are Serializable.
         * The state of Externalizable components is saved by E-Slate itself in
         * saveMicroworld.
         */
//0            for (int i=0; i<newComponents.size(); i++) {
        for (int i=0; i<arrangedComponents.length; i++) {
            Object component;
                component = arrangedComponents[i].object;
            if (classIsSerializableOnly((String) component.getClass().getName())) {
                try{
                    boolean isButton = false;
                    boolean opaque = false;
                    if (JButton.class.isAssignableFrom(component.getClass())) {
                        isButton = true;
                        opaque = ((JButton) component).isOpaque();
                    }
                    if (JComponent.class.isAssignableFrom(component.getClass()))
                        ((JComponent) component).resetKeyboardActions();
                        fieldMap.put("Component" + i, component);
                    if (isButton)
                        ((JButton) component).setOpaque(opaque);
                }catch (Exception exc) {
                    System.out.println("Unable to serialize component \"" +
//0                              ((ESlateHandle) eSlateHandles.at(i)).getComponentName() +
                          arrangedComponents[i].handle.getComponentName() +
                          "\" of class " + component.getClass().getName() +
                          ". Exception: " + exc.getClass().getName() + ", " + exc.getMessage());
                    exc.printStackTrace();
                }
            }
//                }
        }
        // Component names
        String[] compNames = new String[arrangedComponents.length];
        for (int i=0; i<arrangedComponents.length; i++) {
            String name = arrangedComponents[i].handle.getComponentName();
            compNames[i] = name;
        }

        fieldMap.put("ComponentNames", compNames);
        fieldMap.put("MwdLayers", mwdLayers);
        fieldMap.put("MwdSize", getMicroworldSize());
        fieldMap.put("NextIconLocation", nextIconLocation);
//PM            fieldMap.put("Performance Manager State", PerformanceManager.getPerformanceManager().getState());
        return fieldMap;
    }
    /* Saves the current microworld to the specified file
     */
    protected boolean saveAs(String mwdFileName, WaitDialogTimerTask wtt) {
        PerformanceManager.getPerformanceManager().init(saveTimer);
        int exists = StructFile.NEW;

        StructFile structFile = null;
        ObjectOutputStream out = null;

      boolean saveSuccessfull = false;
       try{
            File f = new File(mwdFileName);
            // This trick of re-assigning the value of 'mwdFileName' is needed, cause sometimes
            // saveAs() is called with a file name which instead of file.separators contains
            // the URL separator '/'.
            mwdFileName = f.getAbsolutePath();
            if (f.exists() && !f.canWrite()) {
                setCursor(Cursor.getDefaultCursor(), false);
                DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
                String message = containerBundle.getString("ContainerMsg16") + currentlyOpenMwdFileName + "\". " + containerBundle.getString("ContainerMsg45");
                dialog.setMessage(message);
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
                return false;
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);

			// 'savingAs' is true, when a microworld loaded from a structfile is
			// saved to a different structfile. It's false in all other occassions
			// including when the microworld is saved for the first time.
			boolean savingAs = false;
			// 'savingToSameFile' is true, when the microworld is saved to the structfile
            // it was loaded from. It's false for microworld which are saved for the first time.
			boolean savingToSameFile = false;
			if (currentlyOpenMwdFileName != null) {
				if (mwdFileName.equals(currentlyOpenMwdFileName)) savingToSameFile = true;
				else savingAs = true;
			}else{
				ScriptUtils.getInstance().scriptsLoaded = true;
			}


            if (savingToSameFile && microworld.mwdAutoBackupEnabled) {
                if (wtt != null) {
                    wtt.setProgress(wtt.getProgress() + 5);
                    wtt.setMessage(containerBundle.getString("CreatingMwdBackup"));
                }
                if(!EMBEDDED_MODE) {
                	createCopyOfMicroworldFile();
                }
            }

            structFile = microworld.eslateMwd.createMicroworldFile(mwdFileName); //new StructFile(mwdFileName, exists);
            /* If saveAs takes place, then createMicroworldFile() above closes the currentlyOpenMwdFile structfile.
             * We have to reopen it to copy the sounds. At the end of saveAs, the previously opened
             * stuctfile is closed again.
             */

            /* If smth went wrong, then unless "createMicroworldFile()" has thrown an exception, a null
             * structFile has been returned. In this case we assume that "createMicroworldFile()" has already
             * displayed a message explaining the reason of the failure and the methods exits returning false.
             */
            if (structFile == null) {
                setCursor(Cursor.getDefaultCursor(), false);
                return false;
            }

            Entry entry = structFile.createFile(ESlateMicroworld.CONTAINER_INFO);
           /* Store the microworld metadata in a separate file in the structured storage file */
           microworld.saveMetadata(structFile, METADATA_FILE);

            /* If the microworld is saved for the first time, or saved into another file,
             * then make the 'bgrIconFileName's of the icons of the microworld's views
             * null, so that all the icons get saved by the 'saveViewIcons()' of the MicroworldViewList.
             */
            if (currentlyOpenMwdFileName == null || !currentlyOpenMwdFileName.equals(mwdFileName)) {
                for (int i=0; i<mwdViews.viewList.length; i++)
                    mwdViews.viewList[i].bgrIconFileName = null;
                currentView.bgrIconFileName = null;
            }
            mwdViews.saveViewIcons(this, structFile);

            out = new ObjectOutputStream(new BufferedOutputStream(new StructOutputStream(structFile, entry), 30000));
            ESlateFieldMap2 state = getState();
            state.put("Sound listeners", soundListenerMap.saveMap(structFile, container.getESlateHandle())); //.microworld.eslateMwd.getESlateHandle()));
            out.writeObject(state);
            out.flush();
            out.close();

            //Ask E-Slate to save the components' state
            microworld.eslateMwd.saveMicroworld(structFile, wtt);
            /* Set the document base of the microworld again. This is need when a Save As
             * operation is executed.
             */
            String docBase = mwdFileName;
            docBase = docBase.substring(0, docBase.lastIndexOf(System.getProperty("file.separator"))+1);
            microworld.eslateMwd.setDocumentBase(new File(docBase));

            microworld.eslateMwd.finishedLoadingOrSaving(structFile); //closeMicroworldFile(structFile); //structFile.close();
            setCursor(Cursor.getDefaultCursor(), false);

			// Time to save the sources of the microworld's Scripts and
			// ScriptListeners. In only two circumnstances we need to save them:
			// 1. If the target structfile is different from the source structfile (which is not null, i.e. first time save)
			// 2. If the scripts have been loaded to memory.
			// So when the scripts have not been loaded and the operation is a 'save'
			// and not a 'save as' operation, the scripts need not be saved.
//System.out.println("saveAS() scriptsLoaded: " + ScriptUtils.getInstance().scriptsLoaded + ", savingToSameFile: " + savingToSameFile);
			if (ScriptUtils.getInstance().scriptsLoaded || !savingToSameFile) {
				ScriptUtils.getInstance().saveMicroworldScripts(this, structFile, currentlyOpenMwdFile);
			}

            /* In case 'Save As' is performed, we have to close the original structfile */
            if (savingAs && !openFileRemote) { //currentlyOpenMwdFileName != null && (!mwdFileName.equals(currentlyOpenMwdFileName))) {
//System.out.println("Closing currentlyOpenMwdFile " + currentlyOpenMwdFile.getFile());
                 currentlyOpenMwdFile.close();
            }
            currentlyOpenMwdFile = structFile;
            saveSuccessfull = true;
        }catch (gr.cti.eslate.base.WritingException exc) {
            System.out.println("WritingException while saving structured file: " + exc.getClass() + ", " + exc.getMessage());
            try{
                out.close();
                microworld.eslateMwd.finishedLoadingOrSaving(structFile);//closeMicroworldFile(structFile); //structFile.close();
            } catch (Exception exc1) {
                System.out.println("Unable to close file: " + structFile);
            }

            setCursor(Cursor.getDefaultCursor(), false);
//            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg26") + " " + exc.getMessage(), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            String message = containerBundle.getString("ContainerMsg26");
            dialog.setMessage(message);
            dialog.appendThrowableMessage(exc);
            dialog.createNewLine();
            dialog.appendThrowableStackTrace(exc);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
            lc.revalidate();
            lc.paintImmediately(lc.getVisibleRect());
//            return false;
        }catch (Throwable thr) {
            System.out.println("Throwable while saving structured file: " + thr.getClass() + ", " + thr.getMessage());
            thr.printStackTrace();
            try{
                out.close();
                microworld.eslateMwd.finishedLoadingOrSaving(structFile); //closeMicroworldFile(structFile); //structFile.close();
            } catch (Exception exc1) {
                System.out.println("Unable to close file: " + structFile);
            }
            setCursor(Cursor.getDefaultCursor(), false);
//            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg16") + currentlyOpenMwdFileName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            String message = containerBundle.getString("ContainerMsg16") + currentlyOpenMwdFileName + "\"";
            dialog.setMessage(message);
            dialog.appendThrowableMessage(thr);
            dialog.createNewLine();
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
        }finally{
//            if (wtt != null) {
//                wtt.cancel();
//                wtt.disposeDialog();
//            }
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.stop(saveTimer);
            pm.displayTime(saveTimer, "", "ms");
            PerformanceTimerGroup saveGlobalGroup = pm.getPerformanceTimerGroupByID(PerformanceManager.SAVE_STATE);
            pm.displayTime(saveGlobalGroup, "", "ms");
            if (!saveSuccessfull && microworld.backupExists) {
                if ((ESlateOptionPane.showConfirmDialog(ESlateContainer.this, containerBundle.getString("ContainerMsg86"),
                    //containerBundle.getString("ESlate"),
                    getAppName(),
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.YES_OPTION) {

                    String restoredFileName = restoreMicroworldBackup();
                    if (restoredFileName != null)
                        ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg87") + ' ' + restoredFileName + '.', getAppName()/*containerBundle.getString("ESlate")*/, JOptionPane.INFORMATION_MESSAGE);
                    else
                        ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg88"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return saveSuccessfull;
    }

    public File saveByteArray(byte[] serverFileStream, File targetFile) {
        if (serverFileStream == null)
            return null;

/*        try {
        java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.ByteArrayInputStream(serverFileStream));
        dis.skipBytes(1024);
        long l = dis.readLong();
//        System.out.println("Header block size: " + l);
        dis.close();
        }catch (IOException exc) {}
*/
//        System.out.println("Writing tmp file. Content size: " + serverFileStream.length);

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(targetFile);
            fos.write(serverFileStream);
            fos.flush();
        }catch (IOException exc) {
            System.out.println("writeServerFile exception 1: " + exc.getClass() + ", " + exc.getMessage());
            return null;
        }
        try{
            fos.close();
        }catch (IOException exc) {
            System.out.println("writeServerFile exception closing file: " + targetFile + ", exception: " + exc.getClass() + ", " + exc.getMessage());
            return null;
        }
        return targetFile;
    }

///////////////// nikosM start of code here


    /**
     * Opens the load remote mw dialog.
     */
	void openLoadRemoteMicroworldDialog() {
		if (this instanceof ESlateComposer)
			containerUtils.forceMenuClose(new JMenu[] {((ESlateComposer) this).menuPanel.microworldMenu,((ESlateComposer) this).menuPanel.microworldLoad});
//      container.containerUtils.forceMenuClose();
//      composer.setLoadingMwd(true);
////nikosM
      WebFileDialog webFileDialog = new WebFileDialog(this, false); //createWebFileDialog();
//      WebFileDialog webFileDialog = container.webServerMicrosHandle.openWebFileDialog();
      if (webFileDialog != null) {
          int whichButtonPressed=webFileDialog.showOpenDialog(parentComponent);
          if ((webFileDialog.getSelectedFile() != null)&&(whichButtonPressed!=webFileDialog.CANCEL_OPTION)) {
////nikosM end
              if (!createNewMicroworld()) {
//                  composer.setLoadingMwd(false);
                  return;
              }
////nikosM
//              byte[] fileBytes;
              if ((webFileDialog.isRemoteFile())&&(webServerMicrosHandle.openRemoteMicroWorld(webFileDialog.getWebSite(),webFileDialog.getWebFile(),true))) {
//              if (webFileDialog.isRemoteFile() && ((fileBytes = container.openServerFile(webFileDialog.getWebSite(), webFileDialog.getWebFile())) != null)) {
//                  File tmpServerFile = container.writeServerFile(fileBytes, container.tmpFile); //webFileDialog.getServerFileStream(), tmpFile);
//                  if (tmpServerFile != null) {
//                      System.out.println("Wrote file: " + tmpFile + ", exists? " + tmpFile.exists());
//                      if (container.load(container.tmpFile.getAbsolutePath(), true, true)) {
                          openFileRemote = true;
                          webServerMicrosHandle.webSite = webFileDialog.getWebSite();
//////nikosM end
                          currentlyOpenMwdFileName = webFileDialog.getWebFile();
                          if (microworld != null && !microworld.microworldNameUserDefined)
                              setContainerTitle(ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
//                      }else;
//                          ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg17") + tmpFile.toString() + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//                      System.out.println("2. temp file deleted? " + tmpFile.delete());
//                  }
              }else{
////nikosM new
//                  if (container.load(webFileDialog.getSelectedFile().toString(), true, true)) {
                  if (loadLocalMicroworld(webFileDialog.getSelectedFile().toString(), true, true)) {
////nikosM new end
                      openFileRemote = false;
/////////nikosM
                      webServerMicrosHandle.webSite = null;
/////////nikosM end
                      currentlyOpenMwdFileName = webFileDialog.getSelectedFile().toString();
                      if (microworld != null && !microworld.microworldNameUserDefined)
                          setContainerTitle(ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
                  }else;
//                      ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg17") + webFileDialog.getSelectedFile().toString() + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
              }
          }
      }
//      composer.setLoadingMwd(false);
//      System.out.println("currentlyOpenMwdFileName: " + currentlyOpenMwdFileName +
//                         ", openFileRemote: " + openFileRemote + ", webSite: " + webSite);
	}

	/** Loads a microworld from a remote location file, saved at c:\windows\tmp.mwd file.
     * The 'enableWaitDialog' parameter adjusts whether a WaitDialog should appear, during
     * the loading of microworlds which take more than 3 secs to start. The mwdFileName
     * contains the string that will be sent to the reopen list
     * @param mwdFileName The microworld to  load.
     * @param updateHistory Whether the histry of loaded microworlds should be updated.
     * @param wtt The task which tracks the progress of the remote microworld loading.
     * @param delayInMillis The delay after the microworld starts loading that the load progress dialog should become
     *        visible.
     * @return Whether the microworld was loaded successfully.
     */
    public boolean loadRemoteMicroworld(String mwdFileName, boolean updateHistory, WaitDialogTimerTask wtt, int delayInMillis) {
//    System.out.println("LoadRemoteMicroworld");
    String messageOnReopenList=mwdFileName;
    mwdFileName=container.tmpFile.getAbsolutePath();
//        setLoadingMwd(true);
        if (mwdFileName == null) {
//            setLoadingMwd(false);
            return true;
        }

        /* Check if the file exists. If not warn the user and abort */
        if (!new File(mwdFileName).exists()) {
            Object[] arguments = {mwdFileName};
            String msg = java.text.MessageFormat.format(
                containerBundle.getString("ContainerMsg40") + " \"" +
                "{0}" + "\" " +
                containerBundle.getString("ContainerMsg41"), arguments);
//            ESlateOptionPane.showMessageDialog(parentComponent, msg, containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(msg);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
//            setLoadingMwd(false);
            return false;
        }

////nikosM currentMicroworld change
		/* If there is an open microworld, then close it before opening the new one.
		 * createNewMicroworld() does this job, but we want to do it here, before
		 * the 'setLoadingMwd(true)' is called, cause when this is called the RepaintManager
		 * is disabled and any dialog which prompts the user to store the previous microworld
		 * is not displayed.
		 */
        if (microworld != null) {
			/* Check the special case that the file name of the currently open microworld is the
			 * same as the file name of the microworld to be opened. In this case ask the user if
			 * he wants to revert to the original state of the microworld.
			 */
			if (currentlyOpenMwdFileName != null && currentlyOpenMwdFileName.equals(mwdFileName)) {
////nikosM currentMicroworld change end
				if ((ESlateOptionPane.showConfirmDialog(ESlateContainer.this, containerBundle.getString("ContainerMsg27") + mwdFileName + '"' +  containerBundle.getString("?"),
						/*containerBundle.getString("ESlate")*/getAppName(),
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.YES_OPTION) {
					closeMicroworld(false);
				}else{
//					setLoadingMwd(false);
					return false;
				}
			}else{
				if (!closeMicroworld(true))
					return false;
			}
        }

		setLoadingMwd(true);

		// createNewMicroworld() should not fail, as any prevous microworld has
		// already been closed.
        if (!createNewMicroworld()) {
//            System.out.println("loadLocalMicroworld() createNewMicroworld() setLoadingMwd(false)");
            setLoadingMwd(false);
            return false;
        }

        if (load(mwdFileName, true, wtt, delayInMillis)) {
            // We do the following with the file, cause sometimes the mwdFileName instead of
            // file.separators it contains the url separtor '/'.
            File fl = new File(mwdFileName);
            currentlyOpenMwdFileName = fl.getAbsolutePath();
            if (!microworld.microworldNameUserDefined) {
//                System.out.println("currentlyOpenMwdFileName: " + currentlyOpenMwdFileName + ", getFileNameFromPath(currentlyOpenMwdFileName, true): " + ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
                setContainerTitle(ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
            }
            openFileRemote = true;
//// nikosM
            webServerMicrosHandle.webSite = null;
//// nikosM end
            if (updateHistory) {
                // Update the history list
                updatingHistoryFlag=true;
                mwdHistory.addToHistory(/*mwdFileName*/messageOnReopenList+"*");
                containerUtils.fireMwdHistoryChanged(new MwdHistoryChangedEvent(this));
                updatingHistoryFlag=false;
            }
//            microworldList.updateMicroworldList(mwdFileName);
            microworldList.updateMicroworldList(messageOnReopenList+"*",true);
            if (!soundListenerMap.containsListenerFor(MicroworldListener.class, "microworldLoaded"))
                playSystemSound(SoundTheme.MWD_OPENED_SOUND);
////nikosM end
        }else{
//            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg17") + mwdFileName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            setLoadingMwd(false);
            return false;
        }

//        System.out.println("currentlyOpenMwdFileName: " + currentlyOpenMwdFileName +
//                           ", openFileRemote: " + openFileRemote + ", webSite: " + webSite);
        setLoadingMwd(false);
        return true;
    }
//////////////// nikosM end of code


    /** Loads a microworld from a .mwd file. Should be used only for .mwd files in the
     * local file system. The 'enableWaitDialog' parameter adjusts whether a microworld load progress dialog
     * should appear, when the microworld load time exceeds an ammount of time.
     * This method simply calls <code>loadLocalMicroworld(mwdFileName, updateHistory, enableWaitDialog, delayInMillis)
     * with the default delay of 3000 millis.
     * @param mwdFileName The full path name of the microworld file.
     * @param updateHistory Whether the history o loaded microworlds should be updated.
     * @param enableWaitDialog Whether the microworld load progress dialog should be displayed.
     * @return Whether the microworld was successfully loaded.
     */
    public boolean loadLocalMicroworld(String mwdFileName, boolean updateHistory, boolean enableWaitDialog) {
        System.out.println(mwdFileName);
    	return loadLocalMicroworld(mwdFileName, updateHistory,  enableWaitDialog, 3000);
    }

    /** Loads a microworld from a .mwd file. Should be used only for .mwd files in the
     * local file system. The 'enableWaitDialog' parameter adjusts whether a WaitDialog
     * should appear, during the loading of microworlds which take more than 3 secs to start.
     * @param mwdFileName The full path name of the microworld file.
     * @param updateHistory Whether the history o loaded microworlds should be updated.
     * @param enableWaitDialog Whether the microworld load progress dialog should be displayed.
     * @param delayInMillis The delay after the microworld starts loading that the progress dialog should become visible.
     * @return Whether the microworld was successfully loaded.
     */
    public boolean loadLocalMicroworld(String mwdFileName, boolean updateHistory, boolean enableWaitDialog, int delayInMillis) {
//        setLoadingMwd(true);
        if (mwdFileName == null) {
//            setLoadingMwd(false);
            return true;
        }

        /* Check if the file exists. If not warn the user and abort */
        if (!new File(mwdFileName).exists()) {
            Object[] arguments = {mwdFileName};
            String msg = java.text.MessageFormat.format(
                containerBundle.getString("ContainerMsg40") + " \"" +
                "{0}" + "\" " +
                containerBundle.getString("ContainerMsg41"), arguments);
//            ESlateOptionPane.showMessageDialog(parentComponent, msg, containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            /*papakiru*/
            //DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            //dialog.setMessage(msg);
            //ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            /*papakiru*/
//            setLoadingMwd(false);
            return false;
        }

//System.out.println("loadLocalMicroworld():  " + mwdFileName);
//System.out.println("1. loadingMwd: " + loadingMwd);
		/* If there is an open microworld, then close it before opening the new one.
		 * createNewMicroworld() does this job, but we want to do it here, before
		 * the 'setLoadingMwd(true)' is called, cause when this is called the RepaintManager
		 * is disabled and any dialog which prompts the user to store the previous microworld
         * is not displayed.
		 */
        if (microworld != null) {
			/* Check the special case that the file name of the currently open microworld is the
	 		 * same as the file name of the microworld to be opened. In this case ask the user if
	 		 * he wants to revert to the original state of the microworld.
			 */
			if (currentlyOpenMwdFileName != null && currentlyOpenMwdFileName.equals(mwdFileName)) {
				if(!EMBEDDED_MODE) {
					if ((ESlateOptionPane.showConfirmDialog(ESlateContainer.this, containerBundle.getString("ContainerMsg27") + mwdFileName + '"' + containerBundle.getString("?"),
							/*containerBundle.getString("ESlate")*/getAppName(),
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.YES_OPTION) {
						closeMicroworld(false);
					}else{
						return false;
					}
				} else {
					return false;
				}
			}else{
				if (!closeMicroworld(true))
					return false;
			}
        }

		setLoadingMwd(true);
		// createNewMicroworld() should not fail, as any prevous microworld has
		// already been closed.
		//try {
	        if (!createNewMicroworld()) {
	//            System.out.println("loadLocalMicroworld() createNewMicroworld() setLoadingMwd(false)");
	            setLoadingMwd(false);
	            return false;
	        }
		//} catch(NullPointerException e) {
			// do nothing
		//}

        if (load(mwdFileName, true, enableWaitDialog, delayInMillis)) {
            // We do the following with the file, cause sometimes the mwdFileName instead of
            // file.separators it contains the url separtor '/'.
            File fl = new File(mwdFileName);
            currentlyOpenMwdFileName = fl.getAbsolutePath();
            if (!microworld.microworldNameUserDefined) {
//                System.out.println("currentlyOpenMwdFileName: " + currentlyOpenMwdFileName + ", getFileNameFromPath(currentlyOpenMwdFileName, true): " + ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
                setContainerTitle(ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
            }
            openFileRemote = false;
//// nikosM
            webServerMicrosHandle.webSite = null;
            if (updateHistory) {
                // Update the history list
                mwdHistory.addToHistory(mwdFileName);
                containerUtils.fireMwdHistoryChanged(new MwdHistoryChangedEvent(this));
            }
            microworldList.updateMicroworldList(currentlyOpenMwdFileName,false);
            if (!soundListenerMap.containsListenerFor(MicroworldListener.class, "microworldLoaded"))
                playSystemSound(SoundTheme.MWD_OPENED_SOUND);
////nikosM end
        }else{
//            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg17") + mwdFileName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            setLoadingMwd(false);
            return false;
        }

//        System.out.println("currentlyOpenMwdFileName: " + currentlyOpenMwdFileName +
//                           ", openFileRemote: " + openFileRemote + ", webSite: " + webSite);
        setLoadingMwd(false);
        return true;
    }

    /* If microworld loading takes more that 3 seconds, then the WaitDialog is constructed and
     * displayed to the user. This is the default behaviout, which in certain circumstances may
     * not be desirable. Therefore the load() method accepts the 'enableWaitDialog' parameter
     * through which the appearance of the WaitDialog may be disabled alltogether.
     */
////nikosM change of load
/*    protected boolean load(String mwdFileName, boolean displayErrorMessage, boolean enableWaitDialog, int delayInMillis) {
        WaitDialogTimerTask wtt = null;
        if (enableWaitDialog) {
            wtt = new WaitDialogTimerTask(this, true, isContainerInitializing);
            wtt.timer.schedule(wtt, new java.util.Date(System.currentTimeMillis()+delayInMillis));
            wtt.setMessage(container.containerBundle.getString("Loading1"));
        }
        boolean result=load(mwdFileName, displayErrorMessage, enableWaitDialog, wtt);
        if (enableWaitDialog) {
            wtt.disposeDialog();
            wtt.timer.cancel();
        }
        return result;
    }
*/


    protected boolean load(String mwdFileName, boolean displayErrorMessage, boolean enableWaitDialog, int delayInMillis) {
////nikosM currentMicroworld change
        if (microworld.eslateMwd == null || mwdFileName == null || !new File(mwdFileName).exists())
////nikosM currentMicroworld change end
            return false;

        WaitDialogTimerTask wtt = null;
        if (enableWaitDialog) {
            wtt = new WaitDialogTimerTask(this, true, isContainerInitializing);
            wtt.timer.schedule(wtt, new java.util.Date(System.currentTimeMillis()+delayInMillis));
            wtt.setMessage(container.containerBundle.getString("Loading1"));
        }

        boolean loaded = load(mwdFileName, displayErrorMessage, wtt, delayInMillis);
        if (wtt != null) {
            wtt.disposeDialog();
            wtt.timer.cancel();
        }
        return loaded;
    }

    protected boolean load(String mwdFileName, boolean displayErrorMessage, WaitDialogTimerTask wtt, int delayInMillis) {
long start2 = System.currentTimeMillis();
        StructFile structFile = null;
        ObjectInputStream in = null;
////nikosM end

        PerformanceManager pm = PerformanceManager.getPerformanceManager();
long loadTime = 0, frameIconCreationTime = 0;
/*long propStart = 0, propStart1 = 0;
long componentInstantiationTime = 0, scriptSoundLoadTime = 0, handleRegistrationTime = 0;
long addComponentTimer = 0, restTimer = 0, addIconTimer = 0;
*/
        long fileReadTime = 0;
        try{

//            pm.init(loadTimer);
loadTime = System.currentTimeMillis();
//            pm.init(fileReadTimer);
//            pm.displayTime(fileReadTimer, " 1 ", "ms");
            fileReadTime = System.currentTimeMillis();
//System.out.println("DISPLAY FILEREADERTIMER....");
//            System.out.println("tmpFile: " + tmpFile + ", exists: " + tmpFile.exists() + ", size: " + tmpFile.length() + ", canRead: " + tmpFile.canRead());
            structFile = microworld.eslateMwd.openMicroworldFile(mwdFileName); // new StructFile(mwdFileName, StructFile.OLD);

            Entry mwdEntry = structFile.findEntry(ESlateMicroworld.CONTAINER_INFO); //"Microworld properties");
            StructInputStream sis = new StructInputStream(structFile, mwdEntry);
            in = new ObjectInputStream(sis); //new BufferedInputStream(sis, 30000));

            //Load the microworlds scriptListeners
            componentScriptListeners.clear();
            soundListenerMap.clear();
            scriptMap.clear(this);
//            System.out.println("Loading componentScriptListeners....");
//            componentScriptListeners = (ScriptListenerMap) in.readObject();

            boolean namesRestored = false;
            StorageStructure state = (StorageStructure) in.readObject();
            String dataFormat = state.getDataVersion();
            int dataFormatID = state.getDataVersionID();
//            pm.stop(fileReadTimer);
//            pm.displayTime(fileReadTimer, " 2 ", "ms");
            fileReadTime = System.currentTimeMillis()-fileReadTime;
//System.out.println("fileReadTime: " + fileReadTime);

//            String dataFormat = "none";
//            int dataFormatID = -1;
            StorageStructure soundListeners = null;
//            StorageStructure state = null;
            gr.cti.eslate.base.container.PerformanceManagerState pms = null;
            /* 'dataFormatID' was introduced from MWD_STR_FORMAT_VERSION '3'.
             * Up to that version MWD_STR_FORMAT_VERSION used to be a String
             * with values up to "2.0". Since version 3, it became an int and
             * these are the first microworlds for which 'dataFormatId' gets
             * value different from -1.
             */
            pms = (PerformanceManagerState) state.get("Performance Manager State");
//System.out.println("pms: " + pms);
            PerformanceManager.getPerformanceManager().setState(pms);
            pm.init(mwdPropertiesRestoreTimer);
//propStart = System.currentTimeMillis();
            // Load the scripts of the microworld. Before verion 4 of the
            // microworld storage format, the scripts' text was stored as
            // part of the ScriptListener or Script class. Since version
            // 4 the source of the scripts is stored in the SCRIPT_DIR_NAME
            // directory of the microworld's structfile. So whenever a
            // pre 5 version microworld is loaded we set the 'scriptsLoaded'
            // flag of the ScriptUtils singleton
            ScriptMap map = (ScriptMap) state.get("Global Scripts");
            if (map != null)
                scriptMap = map;
            componentScriptListeners.loadMap(state.get("Script listeners"));
            // Load the microworld sounds.
            soundListeners = (StorageStructure) state.get("Sound listeners");
//System.out.println("dataFormatID: " + dataFormatID);

//                    soundListenerMap.loadMap(fieldMap.get("Sound listeners"));

            /* We restore the size of the microword here, so that it won't change
             * when components start being added to the microworld. The desktop size
             * used to be the last thing to be restored in load().
             */
            Dimension mwdSize = (Dimension) state.get("MwdSize");

            /* Load and set the Microworld properties */
            StorageStructure mwdProperties = (StorageStructure) state.get("Microworld properties");
            if (mwdProperties != null) {
                microworld.applyMicroworldProperties(mwdProperties);
                if (wtt != null) {
                    wtt.setDialogTitle(microworld.getMwdLoadProgressMsg());
                    wtt.setProgressInfoDisplayed(microworld.isMwdLoadProgressInfoDisplayed());
                    wtt.setDialogTitleColor(microworld.getProgressDialogTitleColor());
                    wtt.setDialogTitleFont(microworld.getProgressDialogTitleFont());
                    wtt.setMicroworldProgressDialogInfoRead(true);
                }
            }else{
                /* If this is an old microworld, then enable the appearance of the
                 * WaitDialog.
                 */
                if (wtt != null)
                    wtt.setMicroworldProgressDialogInfoRead(true);
            }
//                    if (dataFormatID >= 3)
                microworld.loadMetadata(structFile, METADATA_FILE);

            /* If component renaming is not allowed, enable it while loading takes
             * place, so that the components get their proper names.
             */
            boolean renamingAllowed = microworld.isComponentNameChangeAllowed();
            if (!renamingAllowed)
                microworld.setComponentNameChangeAllowedInternal(true);
            boolean mwdRenamingAllowed = microworld.isMwdNameChangeAllowed();
            if (!mwdRenamingAllowed)
                microworld.setMwdNameChangeAllowed(true);

            /* Load the microworld views */
            MicroworldViewList mwdViewList = (MicroworldViewList) state.get("Microworld View List");
            mwdViewList.container = this;
            if (mwdViewList != null) {
                mwdViews = mwdViewList;
                MicroworldView[] viewList = mwdViews.viewList;
                if (this instanceof ESlateComposer) {
                    if (((ESlateComposer) this).menuPanel != null) {
                        if (viewList != null) {
                            for (int i=0; i<viewList.length; i++)
                                ((ESlateComposer) this).menuPanel.microworldViewMenu.addItem(viewList[i].viewName);
                        }
                    }
                }
            }

//                    System.out.println("Loading MicroworldView");
            DesktopItemViewInfo[] desktopItemInfo = null;
            /* The 'currentView' used to be of 'MicroworldView' class. Now its
             * class has changed to 'MicroworldCurrentView'. When restoring
             * an old microworld, the necessary conversion has to be made.
             */
            MicroworldView currView = (MicroworldView) state.get("Microworld View");
            if (!MicroworldCurrentView.class.isAssignableFrom(currView.getClass())) {
                currentView = new MicroworldCurrentView(this, currView);
                currentView.viewName = containerBundle.getString("Microworld current view");
                currentView.desktopItemInfo = currView.desktopItemInfo;
            }else
                currentView = (MicroworldCurrentView) currView;
            if (currentView == null) {
                currentView = new MicroworldCurrentView(this, null);
                currentView.viewName = containerBundle.getString("Microworld current view");
                currentView.container = this;
                currentView.menuBarVisible = menuBarVisible;
            }else
                desktopItemInfo = currentView.desktopItemInfo;
            if (this instanceof ESlateComposer)
                ((ESlateComposer) this).setMenuSystemHeavyWeight(currentView.menuSystemHeavyWeight);
//                    System.out.println("Loading ComponentViewInfos");
//                    ComponentViewInfo[] componentInfo = (ComponentViewInfo[]) fieldMap.get("Component Info");
            if (desktopItemInfo == null)
                desktopItemInfo = new DesktopItemViewInfo[0];
            /* The following method moves some attricutes that used to belong to
             * MicroworldView class, to the Microworld class.
             */
            currentView.propagatePropsToMicroworld(microworld);

//\                    setMenuBarVisibleInternal(currentView.menuBarVisible); //_menuVisible);
            setOutlineDragEnabledInternal(currentView.outlineDragEnabled); //_outlineDrag);

            setMicroworldBorder(currentView.borderType,
                                currentView.borderColor,
                                currentView.borderIcon,
                                currentView.getMicroworldBorderInsets());

            setOuterBorder(currentView.outerBorderType);

            setMwdAutoScrollableInternal(currentView.mwdAutoScrollable);
            setComponentsMoveBeyondMwdBoundsInternal(currentView.componentsMoveBeyondMwdBounds);
            setMwdAutoExpandableInternal(currentView.mwdAutoExpandable);
            setDesktopDraggableInternal(currentView.desktopDraggable);
            setActiveComponentHighlightedInternal(currentView.activeComponentHighlighted);
            int horBarVisibility = currentView.horizontalScrollbarPolicy; //fieldMap.get("HorScrollBarPolicy", JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            int vertBarVisibility = currentView.verticalScrollbarPolicy; //fieldMap.get("VertScrollBarPolicy", JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            boolean scrollPaneNeedsValidation = false;
            if (scrollPane.getVerticalScrollBarPolicy() != vertBarVisibility) {
                scrollPane.setVerticalScrollBarPolicy(vertBarVisibility);
                scrollPaneNeedsValidation = true;
            }
            if (scrollPane.getHorizontalScrollBarPolicy() != horBarVisibility) {
                scrollPane.setHorizontalScrollBarPolicy(horBarVisibility);
                scrollPaneNeedsValidation = true;
            }
            if (scrollPaneNeedsValidation)
                scrollPane.revalidate();

            setMicroworldBackground(currentView.backgroundType,
                                    currentView.backgroundColor,
                                    currentView.getBackgroundIcon(mwdViews, structFile),
                                    currentView.backgroundIconDisplayMode);
            lc.paintBgrOnly = true;
            lc.paintImmediately(lc.getVisibleRect());
            lc.paintBgrOnly = false;

//System.out.println("load() Calling setMenuBarVisible()");
            final boolean _resizable = currentView.mwdResizable; // fieldMap.get("MwdResizable", true);

            int xpos = currentView.viewPositionX; //fieldMap.get("ViewPosition.X", 0);
            int ypos = currentView.viewPositionY; //fieldMap.get("ViewPosition.Y", 0);
            setMoveAllowedInternal(currentView.moveAllowed); //fieldMap.get("MoveAllowed", true));
            Array componentClassNames = (Array) state.get("ComponentClassNames");
            String names[] = (String[]) state.get("ComponentNames");
            int progressUnit = 1;
            if (componentClassNames.size() > 0)
                progressUnit = 100 / (componentClassNames.size()*2);
//                System.out.println("componentClassNames.size(): " + componentClassNames.size() + ", progressUnit: " + progressUnit);
            Array frameIndices = (Array) state.get("ComponentFrameIndex");

            pm.stop(mwdPropertiesRestoreTimer);

            pm.reset(componentInstantiationTimer);
            pm.reset(frameIconTimer);
            pm.reset(scriptSoundLoadTimer);
            pm.reset(handleRegistrationTimer);
            for (int i=0; i<componentClassNames.size(); i++) {
                pm.start(componentInstantiationTimer);

                String componentClassName = (String) componentClassNames.at(i);
                Class componentClass = null;
                componentClass = Class.forName(componentClassName);
                boolean visualComponent = ESlateContainerUtils.isVisualComponent(componentClass);

                if (wtt != null) {
                    /* The message which is displayed in the WaitDialog informs the user about the
                     * component which is being constructed. The component is identified through
                     * the name it has in the restored microworld. If that name is null, then the
                     * user name for the component's class is used. If no user name for the component's
                     * class exists, then the component's class name is used in the message.
                     */
                    String message = containerBundle.getString("Loading2");
                    if (names != null && names[i] != null)
                        message = message + '\'' + names[i] + '\'';
                    else{
                        String compUserName = null;
                        if (installedComponents != null)
                            compUserName = installedComponents.getName((String) componentClassNames.at(i));
                        if (compUserName != null)
                            message = message + '\'' + compUserName + '\'';
                        else
                            message = message + componentClassNames.at(i);
                    }
                    wtt.setProgress(wtt.getProgress() + progressUnit);
                    wtt.setMessage(message);
                }

//0                        Component[] comp;
                Object comp;
                Object heavyComponent, _component;
//                System.out.println("saveAs: " + componentClassNames.at(i) + ", classIsSerializableOnly: " + classIsSerializableOnly((String) componentClassNames.at(i)));
                /* If the component is not Externalizable, but it is Serializable, then create
                 * it from its stored state in the microworld file. However, if the component is
                 * Externalizable, then it was stored by E-Slate in saveMicroworld() and its state
                 * will be restored by E-Slate in loadMicroworld(). In this case, the container just
                 * instantiates a component of the specified class.
                 */
//                System.out.println("Restoring class: " + componentClassNames.at(i));
                boolean ESR2ConstructorUsed = false;
                if (classIsSerializableOnly((String) componentClassNames.at(i))) {
                    _component = state.get("Component"+i);
//0                            comp = new Component[2];
//0                            comp[0] = _component;
                    comp = _component;
                    if (visualComponent)
                        initApplet((Component) _component);
//                        comp = readSerializableComponent((parentComponent==null) ? container : parentComponent, (String) componentClassNames.at(i), in);
                    if (comp == null)
                        comp = instantiateComponent(container, (String) componentClassNames.at(i));
                }else{
                    comp = microworld.eslateMwd.instantiateComponent(componentClass, microworld.eslateMwd.getESlateHandle(), names[i]);
                    if (comp == null)
                        comp = instantiateComponent(container, (String) componentClassNames.at(i));
                    else
                        ESR2ConstructorUsed = true;
                }
                pm.stop(componentInstantiationTimer);
//System.out.println("LOAD() Construction time: " + constrTime + ", " + componentClassNames.at(i));
//                        System.out.println("Instantiating: " + componentClassNames.at(i));
                if (comp != null) {
//0                            heavyweightComponents.add(comp[1]);
//                    System.out.println("comp: " + comp[0].getClass().getName());
//0                            Component component = (comp[1] == null)? comp[0]:comp[1];
                    pm.start(handleRegistrationTimer);
                    ESlateHandle handle = null;
                    if (ESR2ConstructorUsed) {
                        /* If the component was constructed by the platform using the ESR2
                         * constructor, then it has already been registered to the microworld.
                         * In this case just perform the rest of the stuff the
                         * registerComponentToESlateMicroworld() does.
                         */
                        handle = microworld.eslateMwd.getComponentHandle(comp);
                        handle.addESlateListener(componentNameListener);
                    }else
                        handle = registerComponentToESlateMicroworld(comp);
                    pm.stop(handleRegistrationTimer);
//                            UILessComponentIcon icon = null;
//                            ESlateInternalFrame fr = null;
//System.out.println("LOAD() ESlate registration time: " + regTime);
long frameIconCreationTime1 = System.currentTimeMillis();
                    pm.start(frameIconTimer);
                    DesktopItem item = null;
                    ESlateComponent eslateComponent = null;
                    if (visualComponent) {
                        Component component = (Component) comp; //(comp[1] == null)? comp[0]:comp[1];
                        boolean useFrame = true;
                        ComponentViewInfo componentInfo = (ComponentViewInfo) desktopItemInfo[i];
//long start45 = System.currentTimeMillis();
                        ESlateInternalFrame fr = addComponent(component,
                                          handle,
                                          true,
//0                                         useFrame,
//                                 ( ((Integer) frameIndices.atc(i)).intValue() == NOFRAME.intValue() )? false:true,
                                          0, //componentInfo.xLocation, //xLocation[i],
                                          0, //componentInfo.yLocation, //yLocation[i],
                                          0, //componentInfo.width, //width[i],
                                          0); //componentInfo.height); //height[i]);
// 0.9.7                    component.addComponentListener(componentVisibilityListener);
//addComponentTimer = addComponentTimer + (System.currentTimeMillis()-start45);
                        if (fr == null) {
                            handle.dispose();
                            continue;
                        }
                        item = fr;
                        eslateComponent = new ESlateComponent(this, handle, visualComponent, item, handle.getComponent());
//0                            if (useFrame) {
                        if (true) {
                            //Set the frames' properties
                            fr.applyState(componentInfo.frameState, true);
                            fr.getESlateHandle().setUniqueComponentName(((String) componentInfo.frameState.get("Frame Handle Name", fr.getESlateHandle().getComponentName())));
                        }
                    }else{
                        InvisibleComponentViewInfo componentInfo = (InvisibleComponentViewInfo) desktopItemInfo[i];
                        UILessComponentIcon icon = new UILessComponentIcon(handle);
                        item = icon;
                        eslateComponent = new ESlateComponent(this, handle, visualComponent, item, handle.getComponent());
//long start23 = System.currentTimeMillis();
//                                addUILessComponent(icon);
//addIconTimer = addIconTimer + (System.currentTimeMillis()-start23);
                        icon.applyState(componentInfo.iconState);
                    }

                    attachESlateComponentListener(eslateComponent);
                    mwdComponents.components.add(eslateComponent);
                    compSelectionHistory.addComponent(eslateComponent);
//restTimer = restTimer + (System.currentTimeMillis()-start21);
//System.out.println("restTimer: " + restTimer);
                    pm.stop(frameIconTimer);
frameIconCreationTime = frameIconCreationTime + System.currentTimeMillis()-frameIconCreationTime1;
System.out.println("Instantiated " + eslateComponent + " at " + (System.currentTimeMillis()-start2));
//System.out.println("Component: " + eslateComponent + ",   Diff: " + (frameIconCreationTime - restTimer - addComponentTimer - addIconTimer  - ESlateInternalFrame.applyStateTimer - UILessComponentIcon.constructorTimer - UILessComponentIcon.applyStateTimer));
                }else
                    throw new Exception();
//System.out.println("frameIconCreationTime: " + frameIconCreationTime);
//System.out.println("restTimer: " + restTimer + ", addComponentTimer: " + addComponentTimer + ", addIconTimer: " + addIconTimer);
//System.out.println("LOAD() compo: " + componentClassNames.at(i) + ", " + constrTime + ", " + regTime + ", " + frameTime + ", total: " + (System.currentTimeMillis()-start));
            }
//System.out.println("LOAD() " + totalConstrTime + ", " + totalRegTime + ", " + totalFrameTime + ", total: " + (System.currentTimeMillis()-start1));
//System.out.println();
            pm.start(mwdPropertiesRestoreTimer);
//propStart1 = System.currentTimeMillis();
            if (mwdComponents.size() != 0)
                mwdComponents.activeComponent = mwdComponents.components.get(0);

            /* When a microworld which sets the menu bar visibility to false is
             * loaded, then if the component bar is displayed, it should be hidded
             * Otherwise the microworld will appear without a menu bar and with the
             * component bar visible. In this case there is no obvious way to hide
             * the component bar, since the toggle button exists in the hidden menubar.
             */
            if (this instanceof ESlateComposer) {
                ESlateComposer composer = (ESlateComposer) this;
                if (!menuBarVisible && composer.componentBar != null && composer.componentBar.getParent() != null)
                    composer.toggleComponentPanelInternal();

                composer.setMenuSystemHeavyWeightInternal(currentView.menuSystemHeavyWeight); //fieldMap.get("MenuSystemHeavyWeight", false));
            }
//                    setNameChangeAllowed(currentView.nameChangeAllowed);

            /* ESlate restores the names of the components, before calling loadMicroworld().
             * This ensures that the components component names will be restored correctly.
             * Each handle has a name from the second it is created. It is possible that while trying
             * to restore the ESlateHandle names, a restored name may collide with one of the default
             * names of the components, whose names have not yet been restored. In this case we cannot
             * restore the name of the component. The workaround we are using in such cases is:
             * 1. we give the component, whose name cannot be restored a unique name
             * 2. we rerun the component restore procedure for all the components.
             * In the next run, the component whose name conflicted with the name which could not
             * be restored, has a new name (the restored one) and no conflict will occur.
             */
            String compNames[] = (String[]) state.get("ComponentNames");
            if (compNames != null) {
                namesRestored = true;
                boolean rerunNaming = true;
                while (rerunNaming) {
                    rerunNaming = false;
                    for (int i=0; i<mwdComponents.size(); i++) {
                        ESlateHandle hdl = mwdComponents.components.get(i).handle;
                        try{
                            if (!hdl.getComponentName().equals(compNames[i]))
                                hdl.setComponentName(compNames[i]);
                        }catch (gr.cti.eslate.base.NameUsedException ex) {
                            System.out.println("NameUsedException while restoring component names");
                            rerunNaming = true;
                            // Find a unique name, which is actually an integer id.
//0                                    ESlateHandle handle = ((ESlateInternalFrame) componentFrames.at(i)).eSlateHandle;
                            ESlateHandle handle = mwdComponents.components.get(i).handle;
                            String currentName = handle.getComponentName();
                            for (int id=0; ; id++) {
                                boolean idExists = false;
                                String proposedName = new Integer(id).toString();
                                for (int k=0; k<compNames.length; k++) {
                                    if (proposedName.equals(compNames[k])) {
                                        idExists = true;
                                        break;
                                    }
                                }
                                if (idExists) continue;
                                for (int k=0; k<mwdComponents.size(); k++) {
//                                    System.out.println("id: " + id + " Checking " + k + " Existing compo name: " + ((ESlateInternalFrame) componentFrames.at(k)).eSlateHandle.getComponentName() + " equals: " + ((ESlateInternalFrame) componentFrames.at(k)).eSlateHandle.getComponentName().equals(proposedName));
                                    if (proposedName.equals( mwdComponents.components.get(k).handle.getComponentName() )) {
                                        idExists = true;
                                        break;
                                    }
                                }
                                if (idExists) continue;
//                                System.out.println("Renaming " + currentName + " to " + proposedName);
                                handle.setComponentName(proposedName);
                                break;
                            }
                        }catch (gr.cti.eslate.base.RenamingForbiddenException ex) {
                            System.out.println("RenamingForbiddenException: shouldn't normally occur.");
                        }catch (IllegalArgumentException ex) {
                            System.out.println("IllegalArgumentException: " + ex + "... It shouldn't normally occur.");
                        }
                    }
                }
            }

            mwdLayers = (LayerInfo) state.get("MwdLayers");
            mwdLayers.container = this;
            if (!renamingAllowed)
                microworld.setComponentNameChangeAllowedInternal(false);
            if (!mwdRenamingAllowed)
                microworld.setMwdNameChangeAllowed(false);
                Point p2 = (Point) state.get("NextIconLocation");
                if (p2 != null)
                    nextIconLocation = p2;

//propStart = propStart + System.currentTimeMillis() - propStart1;
                pm.stop(mwdPropertiesRestoreTimer);

            in.close();
//            PerformanceManager.getPerformanceManager().setState(pms);

long et2 = System.currentTimeMillis();
            /* Ask the platform to read the components' state. If the microworld file contains the
             * names of the components and they have been restored, then the platform, should not
             * try to restore the names again. This check ensures compatibility with microworlds
             * saved before 0.9.6 ESlateContainer version. Since 0.9.6 ESlateContainer -and not the
             * platform- saves the components' names and is responsible for restoring them.
             */
//System.out.println("Calling loadMicroworld");
            try{
                if (namesRestored) {
//                    System.out.println("Names have been RESTORED");
                    microworld.eslateMwd.loadMicroworld(structFile, false, wtt);
                }else
                    microworld.eslateMwd.loadMicroworld(structFile, true, wtt);
            }catch (gr.cti.eslate.base.PartialReadException exc1) {
//System.out.println("Called loadMicroworld");
////nikosM new
                if (wtt != null) {
                    wtt.disposeDialog();
//                    timer.cancel();
                }

////nikosM new end
                eventQueue.postMicroworldFinalRepaintEvent();
                DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
                String message = containerBundle.getString("ContainerMsg17") + mwdFileName + "\"";
                dialog.setMessage(message);
                dialog.appendThrowableMessage(exc1);
                dialog.createNewLine();
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
            }

//System.out.println("LOAD() component state read time: " + (System.currentTimeMillis()-et2));

            if (wtt != null)
                wtt.setMessage(null);
            microworld.eslateMwd.finishedLoadingOrSaving(structFile);//closeMicroworldFile(structFile); //structFile.close();
            setCursor(Cursor.getDefaultCursor(), false);
//            System.out.println("load(): setting microworld changed to false");
//            System.out.println("currentMicroworld.getName(): " + currentMicroworld.getName());
            if (microworld.microworldNameUserDefined)
                setContainerTitle(microworld.eslateMwd.getName());

            if (this instanceof ESlateComposer) {
                ESlateComposer composer = (ESlateComposer) this;
                if (composer.menuPanel != null) {
                    for (int i=0; i<mwdComponents.size(); i++)
                        composer.addComponentItemToComponentsMenu(mwdComponents.components.get(i).handle.getComponentName());
                    for (int i=0; i<mwdComponents.size(); i++)
                        composer.addComponentItemToHelpMenu(mwdComponents.components.get(i).handle);
                }
            }

et2 = System.currentTimeMillis();
            // Bind scriptListeners to microworld's components
//scriptSoundLoadTime = System.currentTimeMillis();
            pm.start(scriptSoundLoadTimer);
            componentScriptListeners.locateScriptableObjects(microworld.eslateMwd);
            componentScriptListeners.attachScriptListeners(ESlateContainer.this, null);
			soundListenerMap.loadMap(soundListeners);

            pm.stop(scriptSoundLoadTimer);
//scriptSoundLoadTime = System.currentTimeMillis()-scriptSoundLoadTime;

            // Set the visible area of the Desktop pane
            lc.setPreferredSize(mwdSize);
            lc.setMinimumSize(mwdSize);
            lc.setMaximumSize(mwdSize);
            scrollPane.getViewport().setViewSize(mwdSize);
            scrollPane.getViewport().scrollRectToVisible(new Rectangle(xpos, ypos, mwdSize.width, mwdSize.height));
//System.out.println("Scrolling scrollpane to xpos: " + xpos + ", ypos: " + ypos + ", mwdSize.width: " + mwdSize.width + ", mwdSize.height: " + mwdSize.height);
            /* One ESlateInternalFrame componentMoved event is generated when the mwd is restored. This event
             * alters the y-position of the microworld from where it is restored above. We use this small
             * trick to avoid this undesired behaviour.
             */
            disableComponentMoveListener = true;

            // Mark the active component
//            System.out.println("currentView.activeComponentName: " + currentView.activeComponentName);

            if (dataFormat.startsWith("1.")) {
                if (mwdComponents.size() != 0) {
                    ESlateComponent ecomponent = mwdComponents.components.get(0);
                    setActiveComponent(ecomponent, true);
                    if (ecomponent != mwdComponents.activeComponent)
                        mwdComponents.activeComponent = ecomponent;
                }
            }else{
               ESlateComponent ecomponent = mwdComponents.getComponent(currentView.activeComponentName);
                if (ecomponent != null) {
                    setActiveComponent(ecomponent, true);

                    if (ecomponent != mwdComponents.activeComponent)
                        mwdComponents.activeComponent = ecomponent;
                }
            }

            /* Synchronize the componentBar, if it is visible
             */
/*
            if (this instanceof ESlateComposer) {
                ESlateComposer composer = (ESlateComposer) this;
                if (composer.componentBar != null && composer.componentBar.getParent() != null)
                    composer.componentBar.synchronizeComponentPanel();
            }
*/

            setMicroworldChanged(false);

            if (wtt != null)
                wtt.setProgress(100);

//System.out.println("LOAD() total time: " + (System.currentTimeMillis()-start2));
            currentlyOpenMwdFile = structFile;

            microworldLoaded(state);
            return true;
        }catch (IOException exc) {
            eventQueue.postMicroworldFinalRepaintEvent();
            exc.printStackTrace();
            setCursor(Cursor.getDefaultCursor(), false);
////nikosM new
            if (wtt != null) {
                wtt.setProgress(100);
//                wtt.disposeDialog();
//                timer.cancel();
            }
////nikosM new end
            if (displayErrorMessage) {
                DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
                String message = containerBundle.getString("ContainerMsg42") + mwdFileName + "\"";

                dialog.setMessage(message);
                dialog.appendThrowableMessage(exc);
                dialog.createNewLine();
                dialog.appendThrowableStackTrace(exc);
//                setScreenFrozen(false);//scrollPane.freezeScreen = false;//setVisible(true);
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
            }


            System.out.println("Exception while opening structured file: " + exc.getClass() + ", " + exc.getMessage());
            if (in != null && structFile != null) {
                try{
                    System.out.println("Closing struct file: " + structFile);
                    if (in != null)
                        in.close();
                    if (structFile != null)
                        microworld.eslateMwd.finishedLoadingOrSaving(structFile);//closeMicroworldFile(structFile); //structFile.close();
                } catch (Exception exc1) {
                    System.out.println("Unable to close file: " + structFile);
                }
            }
            closeMicroworld(false);
            return false;
        }catch (ClassNotFoundException exc) {
            eventQueue.postMicroworldFinalRepaintEvent();
            exc.printStackTrace();
            setCursor(Cursor.getDefaultCursor(), false);
////nikosM new
//            if (wtt != null) {
//                wtt.disposeDialog();
////                timer.cancel();
//            }
////nikosM new end
            if (displayErrorMessage) {
                DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
                String message = containerBundle.getString("ContainerMsg42") + mwdFileName + "\"";

                dialog.setMessage(message);
                dialog.appendThrowableMessage(exc);
                dialog.createNewLine();
                dialog.appendThrowableStackTrace(exc);
//                setScreenFrozen(false);//scrollPane.freezeScreen = false; //setVisible(true);
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
            }
            System.out.println("Exception while opening structured file: " + exc.getClass() + ", " + exc.getMessage());
            try{
                if (in != null)
                    in.close();
                if (structFile != null)
                    microworld.eslateMwd.finishedLoadingOrSaving(structFile);//closeMicroworldFile(structFile); //structFile.close();
            } catch (Exception exc1) {
                System.out.println("Unable to close file: " + structFile);
            }
            closeMicroworld(false);
            return false;
        }catch (Throwable thr) {
            eventQueue.postMicroworldFinalRepaintEvent();
            setCursor(Cursor.getDefaultCursor(), false);
            thr.printStackTrace();

////nikosM new
//            if (enableWaitDialog) {
//                wtt.disposeDialog();
////                timer.cancel();
//            }

////nikosM new end

            if (displayErrorMessage) {
                DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
                String message = containerBundle.getString("ContainerMsg42") + mwdFileName + "\"";

                dialog.setMessage(message);
                dialog.appendThrowableMessage(thr);
                dialog.createNewLine();
                dialog.appendThrowableStackTrace(thr);
//                setScreenFrozen(false);//scrollPane.freezeScreen = false;//setVisible(true);
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
            }
            System.out.println("Exception while opening structured file: " + thr.getClass() + ", " + thr.getMessage());
            try{
                if (in != null)
                    in.close();
                if (structFile != null)
                    microworld.eslateMwd.finishedLoadingOrSaving(structFile);//closeMicroworldFile(structFile); //structFile.close();
            } catch (Exception exc1) {
                System.out.println("Unable to close file: " + structFile);
            }

            closeMicroworld(false);
            return false;
        }finally {
loadTime = System.currentTimeMillis()-loadTime;
//            pm.stop(loadTimer);
//            pm.stop(fileReadTimer);
            pm.stop(componentInstantiationTimer);
            pm.stop(handleRegistrationTimer);
            pm.stop(frameIconTimer);
            pm.stop(scriptSoundLoadTimer);
            pm.stop(mwdPropertiesRestoreTimer);
//            pm.displayTime(fileReadTimer, "", "ms");
            if (pm.isEnabled() && fileReadTimer.isDisplayEnabled())
                System.out.println("������ ������������ \"�������� ������� �����������\": " + fileReadTime + " ms");
            pm.displayTime(componentInstantiationTimer, "", "ms");
//System.out.println("System componentInstantiationTime: " + componentInstantiationTime);
            pm.displayTime(handleRegistrationTimer, "", "ms");
//System.out.println("System handleRegistrationTime: " + handleRegistrationTime);
            pm.displayTime(frameIconTimer, "", "ms");
//System.out.println("System frameIconCreationTime: " + frameIconCreationTime);
            pm.displayTime(scriptSoundLoadTimer, "", "ms");
//System.out.println("System scriptSoundLoadTime: " + scriptSoundLoadTime);
            pm.displayTime(mwdPropertiesRestoreTimer, "", "ms");
//System.out.println("System propStart: " + propStart);

/*            PerformanceTimerGroup ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.INIT_ESLATE_ASPECT);
            pm.displayAccumulativeTime(ptg, PerformanceTimerGroup.THOROUGH, "", "ms");
            ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.LOAD_STATE);
            pm.displayAccumulativeTime(ptg, PerformanceTimerGroup.THOROUGH, "", "ms");
            ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.CONSTRUCTOR);
            pm.displayAccumulativeTime(ptg, PerformanceTimerGroup.THOROUGH, "", "ms");
*/
//            pm.displayTime(loadTimer, "", "ms");
//            if (pm.isEnabled() && loadTimer.isDisplayEnabled())
                System.out.println("������ ������������ \"�������� �����������\": " + loadTime + " ms");
            pm.displayTime(loadTimerGroup, "", "ms");
            PerformanceTimerGroup constructorGlobalGroup = pm.getPerformanceTimerGroupByID(PerformanceManager.CONSTRUCTOR);
            pm.displayTime(constructorGlobalGroup, "", "ms");
            PerformanceTimerGroup loadGlobalGroup = pm.getPerformanceTimerGroupByID(PerformanceManager.LOAD_STATE);
            pm.displayTime(loadGlobalGroup, "", "ms");
            PerformanceTimerGroup initESlateAspectGlobalGroup = pm.getPerformanceTimerGroupByID(PerformanceManager.INIT_ESLATE_ASPECT);
            pm.displayTime(initESlateAspectGlobalGroup, "", "ms");

//            if (enableWaitDialog) {
//                wtt.disposeDialog();
//                wtt.timer.cancel();
//            }


//System.out.println("Total frameIconCreationTime: " + frameIconCreationTime);
ESlateInternalFrame.constructorTimer = 0;
ESlateInternalFrame.applyStateTimer = 0;
UILessComponentIcon.constructorTimer = 0;
UILessComponentIcon.applyStateTimer = 0;

        }
    }

    /**
     * Called when the microworld finished loading. This method may be overriden by any subclasses in order
     * to load their state.
     * @param state the structure which holds the state of the microworld. This state may contain entries
     * by subclasses.
     */
    protected void microworldLoaded(StorageStructure state) {
    }

///=================================CODE FROM MANTES - START HERE==============================////////
   protected void readLookAndFeelProperties(String properties, HashMap sectionContentsMap,Enumeration enumeration){                                      //
      /* If no LAF section exists in the container.properties, then install the default
       * Look&Feels.
       */
      //boolean LAFsectionExists = (findRegistrySectionStart(properties, "[installed lookandfeels]") != -1);
      //if (!LAFsectionExists) {
	   //installedLAFs.add(null,"javax.swing.plaf.metal.MetalLookAndFeel",true);
          if (installedLAFs.getSize() == 0) {
              try{
                  installedLAFs.add(containerBundle.getString("NativeL&F"), UIManager.getSystemLookAndFeelClassName(), true);              //
                  installedLAFs.add(containerBundle.getString("JavaL&F"), UIManager.getCrossPlatformLookAndFeelClassName(), true);              //
              } catch(DuplicateEntryException e) {
            	  
              } catch (NullPointerException e) {
            	  System.out.println("DUNNO!");
              }
              
    /*        // Registers the installed L&Fs. We don't use it, cause the Motif L&F damages
              // a bit the ESlate layout.
              UIManager.LookAndFeelInfo[] LAFinfo = UIManager.getInstalledLookAndFeels();
              for (int i=0; i<LAFinfo.length; i++) {
                  try{                                                             //
                      installedLAFs.add(LAFinfo[i].getName(), LAFinfo[i].getClassName(), true);              //
                  }catch(DuplicateEntryException e) {}
              }
    */
          }
     //}

      sectionContentsMap = readRegistrySectionIntoHashMap(properties, "[installed lookandfeels]");   //
      Enumeration lafNames = sectionContentsMap.keys();                                              //
      StringBaseArray names = new StringBaseArray(sectionContentsMap.size());                        //
      StringBaseArray clsnames = new StringBaseArray(sectionContentsMap.size());                     //
      BoolBaseArray avail = new BoolBaseArray(sectionContentsMap.size());                            //
      while (lafNames.hasMoreElements()) {                                                           //
         String lafName = (String) lafNames.nextElement();                                           //
         names.add(lafName);                                                                         //
         clsnames.add((String) sectionContentsMap.get(lafName));                                     //
      }                                                                                              //
                                                                                                     //
      /* Parse the properties String and locate the availability of the installed                    //
      * components. The availability of an installed component depends on whether                    //
      * it is available to the end user to use.                                                      //
      */                                                                                             //
                                                                                                     //
      sectionContentsMap = readRegistrySectionIntoHashMap(properties, "[available lookandfeels]");   //
      lafNames = sectionContentsMap.keys();                                                         //
      if (!lafNames.hasMoreElements()) {
    	  names.clear();
    	  clsnames.clear();
      } else while (lafNames.hasMoreElements()) {                                                           //
         String lafName = (String) lafNames.nextElement();                                           //
                                                                                                     //
         /* Add the component's availabilty status in componentAvailability, only                    //
         * if the component is installed, i.e. there exists an entry in the                          //
         * installedComponents HashMap for this component.                                           //
         */                                                                                          //
                                                                                                     //
         int index = names.indexOf(lafName);                                                         //
         if (index != -1)                                                                            //
            avail.add(new Boolean((String) sectionContentsMap.get(lafName)).booleanValue());        //
         else{                                                                                       //
            names.remove(index);                                                                     //
            clsnames.remove(index);                                                                  //
         }                                                                                           //
      }                                                                                              //

      /* Since a container.properties file may be moved to a computer with different OS
       * from the one it was taken from, we have to check the correctness of the "NativeL&F"
       * entry. If this is not correct, then we should change it to the current platform's
       * default L&F.
       */
      int nativeLAFIndex = names.indexOf(containerBundle.getString("NativeL&F"));
      if (nativeLAFIndex != -1) {
          String propNativeLAFClassName = clsnames.get(nativeLAFIndex);
          String nativeLAFClassName = UIManager.getSystemLookAndFeelClassName();
          if (propNativeLAFClassName != null && !propNativeLAFClassName.equals(nativeLAFClassName)) {
              /* If there exist other entries in the properties file with the new class name of
               * the native platform's L&F, then remove these entries.
               */
              int index = -1;
              while ((index = clsnames.indexOf(nativeLAFClassName)) != -1) {
                  names.remove(index);
                  clsnames.remove(index);
                  avail.remove(index);
              }
              clsnames.set(nativeLAFIndex, UIManager.getSystemLookAndFeelClassName());
          }
      }
                                                                                                            //
      /* populate the installedLAFs structure */                                                     //
      for (int g=0; g<names.size(); g++) {
          try{                                                             //
             installedLAFs.add(names.get(g), clsnames.get(g), avail.get(g));                             //
          }catch(DuplicateEntryException e) {}
      }
   }                                                                                                 //
                                                                                                     //
                                                                                                     //
///=================================CODE FROM MANTES - END HERE==============================//////////

   private static String properties_tmp;

   private String getTmpDir() {
		return System.getProperty("java.io.tmpdir")
				+ System.getProperty("file.separator");
	}
   
    protected boolean readContainerProperties() {
        System.out.println("readContainerProperties() containerPropertiesFile: " + containerPropertiesFile);
    	if (containerPropertiesFile == null) {
    		System.out.println("DBG: containerPropertiesFile was found to be null");
    		return false;
    	} else {
    		System.out.println("DBG: containerPropertiesFile was found");
    	}
    	
        String properties = "";
        boolean updatePropertiesFile = false;
        
    	if(EMBEDDED_MODE) {
    		if(Locale.getDefault().equals("el_GR")) {
    			properties = ESlateContainerProperties.getContainerProperties_EL();
    		} else {
    			properties = ESlateContainerProperties.getContainerProperties();
    		}
    		
    		/*try {
    			BufferedReader br = new BufferedReader(new InputStreamReader( Thread.currentThread().getContextClassLoader().getResourceAsStream(CONTAINER_PROPERTIES) ), 1024);
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                	System.out.println("DBG: Reading from " + CONTAINER_PROPERTIES + ": " + line);
                    sb.append(line);
                    sb.append("|");
                }
                properties = sb.toString();
                br.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}*/
    		
    		/*try {
    			BufferedReader br = new BufferedReader(new InputStreamReader( this.getClass().getResourceAsStream(CONTAINER_PROPERTIES) ), 1024);
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                	System.out.println("DBG: Reading from " + CONTAINER_PROPERTIES + ": " + line);
                    sb.append(line);
                    sb.append("|");
                }
                properties = sb.toString();
                br.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}*/
    		
    		/*InputStream zipStream = null;
            URL resource = Thread.currentThread().getContextClassLoader().getResource(CONTAINER_PROPERTIES);
            if (resource != null) {
                try {
                    URLConnection urlConnection = resource.openConnection();
                    urlConnection.setUseCaches(false);
                    zipStream = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(zipStream), 1024);
	                StringBuffer sb = new StringBuffer();
	                String line;
	                while ((line = br.readLine()) != null) {
	                	System.out.println("DBG: Reading from " + CONTAINER_PROPERTIES + ": " + line);
	                    sb.append(line);
	                    sb.append("|");
	                }
	                properties = sb.toString();
	                br.close();
	                zipStream.close();
                } catch (Exception e) {
                	System.out.println("DBG: Error in readContainerProperties() " + e.getMessage());
                }
            }
    		if(properties_tmp == null) {
    			
    		}
    		properties = properties_tmp;*/
    	} else {
	        try{
	        	//BufferedReader br = new BufferedReader(new FileReader(containerPropertiesFile), 1024);
	        	//System.out.println("available char sets: " + Charset.availableCharsets());
	            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
	        	BufferedReader br = new BufferedReader(new InputStreamReader( Thread.currentThread().getContextClassLoader().getResourceAsStream(CONTAINER_PROPERTIES) ), 1024);
	        	StringBuffer sb = new StringBuffer();
	            String line;
	            while ((line = br.readLine()) != null) {
	            	System.out.println("DBG: Reading from " + CONTAINER_PROPERTIES + ": " + line);
	                sb.append(line);
	                sb.append("|");
	            }
	            properties = sb.toString();
	            br.close();
	            if(properties.length() < 5) {
	            	if(Locale.getDefault().equals("el_GR")) {
	        			properties = ESlateContainerProperties.getContainerProperties_EL();
	        		} else {
	        			properties = ESlateContainerProperties.getContainerProperties();
	        		}
	            }
	        } catch (Exception ex1) {
	        	ex1.printStackTrace();
	        	if(Locale.getDefault().equals("el_GR")) {
	    			properties = ESlateContainerProperties.getContainerProperties_EL();
	    		} else {
	    			properties = ESlateContainerProperties.getContainerProperties();
	    		}
	        }
    	}
    	
    	try {

            /* Parse the properties String and locate the installed components
             */
            HashMap sectionContentsMap = null;
            Enumeration enumeration = null;
            readLookAndFeelProperties(properties, sectionContentsMap, enumeration);
            if (this instanceof ESlateComposer) {
                ESlateComposer composer = (ESlateComposer) this;
                if (composer.menuPanel != null)
                    composer.menuPanel.updateLookFeelMenu(installedLAFs);
           }

            sectionContentsMap = readRegistrySectionIntoHashMap(properties, "[installed components]");
            Enumeration compNames = sectionContentsMap.keys();
            StringBaseArray names = new StringBaseArray(sectionContentsMap.size());
            StringBaseArray clsnames = new StringBaseArray(sectionContentsMap.size());
            BoolBaseArray avail = new BoolBaseArray(sectionContentsMap.size());
            BoolBaseArray visual = new BoolBaseArray(sectionContentsMap.size());
            StringBaseArray group=new StringBaseArray(sectionContentsMap.size());
            while (compNames.hasMoreElements()) {
                String compName = (String) compNames.nextElement();
//                installedComponentNames.add(compName, sectionContentsMap.get(compName));
                names.add(compName);
                String clsName = (String) sectionContentsMap.get(compName);
                int parenIndex = clsName.indexOf('(');
                if (parenIndex == -1) {
                    updatePropertiesFile = true;
                    clsnames.add(clsName);
                    boolean v = true;
                    try{
                        Class cls = Class.forName(clsName);
                        if (!Component.class.isAssignableFrom(cls))
                            v = false;
                    }catch (Throwable thr) {}
                    visual.add(v);
                }else{
                    clsnames.add(clsName.substring(0, parenIndex));
                    int cp=clsName.indexOf(')');
                    if (cp!=-1) {
	                    String visualStr = clsName.substring(parenIndex+1,cp);
	                    if (visualStr.startsWith("false"))
	                        visual.add(false);
	                    else
	                        visual.add(true);
                    } else
                    	visual.add(true);
                }
                int gs=clsName.indexOf('#');
                if (gs!=-1)
                	group.add(clsName.substring(gs+1));
                else
                	group.add(null);
            }

            /* Parse the properties String and locate the availability of the installed
             * components. The availability of an installed component depends on whether
             * it is available to the end user to use.
             */
/*            enumeration = installedComponentNames.keys();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                componentAvailability.put(key, Boolean.TRUE);
            }
*/
            sectionContentsMap = readRegistrySectionIntoHashMap(properties, "[available components]");
            compNames = sectionContentsMap.keys();
            while (compNames.hasMoreElements()) {
                String compName = (String) compNames.nextElement();
                /* Add the component's availabilty status in componentAvailability, only
                 * if the component is installed, i.e. there exists an entry in the
                 * installedComponents HashMap for this component.
                 */
/*                if (componentAvailability.count(compName) == 1)
                    componentAvailability.put(compName, new Boolean((String) sectionContentsMap.get(compName)));
*/
                int index = names.indexOf(compName);
                if (index != -1)
                    avail.add(new Boolean((String) sectionContentsMap.get(compName)).booleanValue());
                else{
                    names.remove(index);
                    clsnames.remove(index);
                    group.remove(index);
                }
            }

            /* populate the installedComponents structure */
            
            for (int g=0; g<names.size(); g++) {
            	System.out.println(names.get(g) + "==" + clsnames.get(g));
            	if(EMBEDDED_MODE) {
            		if( !installedComponents.containsComponentName(names.get(g)) ) {
            			installedComponents.add(names.get(g), clsnames.get(g), avail.get(g), visual.get(g),group.get(g));
            		}
            	} else {
            		installedComponents.add(names.get(g), clsnames.get(g), avail.get(g), visual.get(g),group.get(g));
            	}
            }
//           System.out.println(installedComponents);

            /* Parse the properties String and locate the registered web sites
             */
            sectionContentsMap = readRegistrySectionIntoHashMap(properties, "[web sites]");
            Enumeration webSiteNames = sectionContentsMap.keys();
            String webSiteName;
            while (webSiteNames.hasMoreElements()) {
                webSiteName = (String) webSiteNames.nextElement();
                webSites.add(webSiteName, sectionContentsMap.get(webSiteName));
            }

            /* Parse the properties String and locate the availability of the registered
             * web sites. The availability of an registered web site depends on whether
             * it is available to the end user to use.
             */
            enumeration = webSites.keys();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                webSiteAvailability.put(key, Boolean.TRUE);
            }

            sectionContentsMap = readRegistrySectionIntoHashMap(properties, "[available web sites]");
            webSiteNames = sectionContentsMap.keys();
            while (webSiteNames.hasMoreElements()) {
                webSiteName = (String) webSiteNames.nextElement();
                /* Add the web site's availability status in webSiteAvailability, only
                 * if the web site is installed, i.e. there exists an entry in the
                 * webSites OrderedMap for this web site.
                 */
                if (webSiteAvailability.count(webSiteName) == 1)
                    webSiteAvailability.put(webSiteName, new Boolean((String) sectionContentsMap.get(webSiteName)));
            }

            /* Parse the properties String and locate the commonDirs of the registered
             * web sites.
             */
            enumeration = webSites.keys();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                webSiteCommonDirs.put(key, "");
            }

            sectionContentsMap = readRegistrySectionIntoHashMap(properties, "[site common folders]");
            webSiteNames = sectionContentsMap.keys();
            while (webSiteNames.hasMoreElements()) {
                webSiteName = (String) webSiteNames.nextElement();
                /* Add the web site's availability status in webSiteAvailability, only
                 * if the web site is installed, i.e. there exists an entry in the
                 * webSites OrderedMap for this web site.
                 */
                if (webSiteCommonDirs.count(webSiteName) == 1)
                    webSiteCommonDirs.put(webSiteName, sectionContentsMap.get(webSiteName));
            }

            /* Parse the properties String, locate the "Preload Microworld" section and read it.
             */
            sectionContentsMap = readRegistrySectionIntoHashMap(properties, "[preload microworld]");
            if (sectionContentsMap.size() == 0)
                preloadMwdFileName = null;
            else{
                enumeration = sectionContentsMap.keys();
                while (enumeration.hasMoreElements()) {
                    String preloadFile = (String) enumeration.nextElement();
                    preloadMwdFileName = (String) sectionContentsMap.get(preloadFile);
//                    System.out.println("preloadMwdFileName: " + preloadMwdFileName);
                }
            }

            /* Parse the properties String, locate the "container properties" section and read it.
             */
            sectionContentsMap = readRegistrySectionIntoHashMap(properties, "[container properties]");
            // Java console enabled property
            String javaConsoleEnabledStr = (String) sectionContentsMap.get("Enable Java Console");
            javaConsoleEnabled = false;
            if (javaConsoleEnabledStr != null)
                javaConsoleEnabled = new Boolean(javaConsoleEnabledStr).booleanValue();
            if (javaConsoleEnabled)
                redirectOutputToJavaConsoleInternal();
            else
                useStandardJavaOutput();
            if (this instanceof ESlateComposer) {
                ESlateComposer composer = (ESlateComposer) this;
                if (composer.menuPanel != null)
                    composer.menuPanel.toolsJavaConsoleEnabled.setSelected(javaConsoleEnabled);
            }

            // Splitpane divider location property
            String dividerStr = (String) sectionContentsMap.get("Splitpane Divider Location");
            if (dividerStr != null) {
                try{
                    int dividerLocation = Integer.parseInt(dividerStr);
                    splitPane.previousDividerLocation = dividerLocation;
                }catch (Exception exc) {}
            }
            // Current file dialog directory
            currentFileDialogDir = (String) sectionContentsMap.get("Current directory");
//            System.out.println("Restored currentFileDialogDir: " + currentFileDialogDir);

            // The current sort field and direction of the installed component structure
            int sortField = InstalledComponentStructure.NAME;
            boolean directionUp = true;
            try{
                String sortFieldStr = (String) sectionContentsMap.get("Sort on field");
                if (sortFieldStr != null)
                    sortField = new Integer(sortFieldStr).intValue();
//                System.out.println("sortField: " + sortField);
                String directionUpStr = (String) sectionContentsMap.get("Sort direction up");
                if (directionUpStr != null)
                    directionUp = new Boolean(directionUpStr).booleanValue();
//                System.out.println("directionUp: " + directionUp);
            }catch (Throwable thr) {
                System.out.println("Error while reading properties 'Sort on field' and 'Sort direction up'");
            }
//            System.out.println("Calling setSortField() sortField: " + sortField + ", directionUp: " + directionUp);
            installedComponents.setSortField(sortField, directionUp);

            // L&F property
            String lfClassName = (String) sectionContentsMap.get("L&F");
            if (lfClassName != null) {
                try{
                    Class lfClass = Class.forName(lfClassName);
                    setESlateLAF(lfClass, false);
                }catch (ClassNotFoundException e3){
                    /* If for some reason -i.e. E-Slate was installed on another machine which
                     * lacks the jars of the last activated LAF- then the platform's native
                     * L&F is applied.
                     */
                    String nativeLAFClassName = installedLAFs.getClassName(containerBundle.getString("NativeL&F"));
                    if (nativeLAFClassName == null) {
                        nativeLAFClassName = UIManager.getSystemLookAndFeelClassName();
                        installedLAFs.add(containerBundle.getString("NativeL&F"), nativeLAFClassName, true);
                    }
                    try{
                        Class lfClass = Class.forName(nativeLAFClassName);
                        setESlateLAF(lfClass, true);
                    }catch (ClassNotFoundException e4){
                        ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg48") + '\"' + lfClassName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            String perfMonitorStr = (String) sectionContentsMap.get("Performance Monitor Enabled");
            if (perfMonitorStr != null)
                PerformanceManager.getPerformanceManager().setEnabled(new Boolean(perfMonitorStr).booleanValue());

/*            if (sectionContents.size() == 0)
                javaConsoleEnabled = false;
            else{
                enumeration = sectionContents.keys();
                while (enumeration.hasMoreElements()) {
                    String javaConsole = (String) enumeration.nextElement();
                    javaConsoleEnabled = new Boolean((String) sectionContents.get(javaConsole)).booleanValue();
                    if (javaConsoleEnabled) {
                        toolsJavaConsoleEnabled.setSelected(true);
                        redirectOutputToJavaConsole();
                    }else{
                        toolsJavaConsoleEnabled.setSelected(false);
                        useStandardJavaOutput();
                    }
//                    System.out.println("preloadMwdFileName: " + preloadMwdFileName);
                }
            }
*/
            /* Parse the properties String, locate the "reopened microworlds" section and read it.
             */
            Array secContents = readRegistrySectionIntoArray(properties, "[reopened microworlds]");
            microworldList.setContents(secContents);

            /* Parse the properties String, locate the "microworld history list" section and read it.
             */
            secContents = readRegistrySectionIntoArray(properties, "[microworld history list]");
//            System.out.println("secContents: " + secContents);
            if (secContents.size() != 0) {
                try{
                    int historyIndex = new Integer((String) secContents.at(0)).intValue();
                    for (int i=1; i<secContents.size(); i++) {
                        mwdHistory.addToHistory((String) secContents.at(i));
                    }
                    mwdHistory.setCurrentMicroworld(historyIndex);
                }catch (NumberFormatException exc) {}
            }

            /* Parse the properties String, locate the "container bounds" section and read it.
             */
            secContents = readRegistrySectionIntoArray(properties, "[container bounds]");
//            System.out.println("secContents: " + secContents);
            if (secContents.size() != 0) {
                try{
                    String boundsStr = (String) secContents.at(0);
                    int x = new Integer(boundsStr.substring(0, boundsStr.indexOf(',')).trim()).intValue();
                    boundsStr = boundsStr.substring(boundsStr.indexOf(',')+1);
                    int y = new Integer(boundsStr.substring(0, boundsStr.indexOf(',')).trim()).intValue();
                    boundsStr = boundsStr.substring(boundsStr.indexOf(',')+1).trim();
                    int width = 0, height = 0;
                    if (!boundsStr.equals("maximized")) {
                        width = new Integer(boundsStr.substring(0, boundsStr.indexOf(',')).trim()).intValue();
                        height = new Integer(boundsStr.substring(boundsStr.indexOf(',')+1).trim()).intValue();
                    }else{
/*                        int[] workArea = null;
                        if (containerUtils.isWindowsMachine())
                            workArea = gr.cti.eslate.utils.WorkareaFunctions.getAvailableWorkareaSize();
                        else{
                            workArea = new int[4];
                            workArea[0] = 0;
                            workArea[1] = 0;
                            workArea[2] = Toolkit.getDefaultToolkit().getScreenSize().width;
                            workArea[3] = Toolkit.getDefaultToolkit().getScreenSize().height;
                        }
                        x = workArea[0];
                        y = workArea[1];
                        width = workArea[2];
                        height = workArea[3];
*/
//System.out.println("x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);
                        x = 0;
                        y = 0;
                        width = Toolkit.getDefaultToolkit().getScreenSize().width;
                        height = Toolkit.getDefaultToolkit().getScreenSize().height;
                    }
                    Rectangle r=WorkArea.getWorkArea();
                    if (x + width > r.x + r.width)
                    	width = r.width;
                    if (y + height > r.y + r.height)
                    	height = r.height;
                    if (x < r.x) x = r.x;
                    if (y < r.y) y = r.y;

                    Container topLevelAncestor = this.getTopLevelAncestor();
                    if (topLevelAncestor != null && !(topLevelAncestor instanceof ESlateContainerWindow))
                        topLevelAncestor.setBounds(x, y, width, height);

                }catch (NumberFormatException exc) {}
            }else{
                Container topLevelAncestor = this.getTopLevelAncestor();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                if (topLevelAncestor != null && !(topLevelAncestor instanceof ESlateContainerWindow))
                    topLevelAncestor.setBounds(0, 0, screenSize.width, screenSize.height);
            }

            /* Parse the properties String, locate the "microworld file extensions" section and read it.
             */
            secContents = readRegistrySectionIntoArray(properties, "[microworld file extensions]");
//            System.out.println("secContents: " + secContents + ", secContents.size(): " + secContents.size());
            if (secContents.size() != 0) {
                mwdFileExtensions = new String[secContents.size()];
                for (int i=0; i<secContents.size(); i++) {
                    mwdFileExtensions[i] = (String) secContents.at(i);
//                    System.o1ut.println("mwdFileExtensions[i]: " + mwdFileExtensions[i]);
                }
            }else{
                mwdFileExtensions = new String[1];
                mwdFileExtensions[0] = "mwd";
                /* Since the [microworld file extensions] section does not exist in the
                 * 'container.properties. file, create it now, so that the user may edit
                 * the file and specify the desired extensions.
                 */
                updateMicroworldFileExtensionsSection();
            }
            if(!EMBEDDED_MODE) {

            /* Parse the properties String, locate the "Native Program Path" section and read it.
             */
            secContents = readRegistrySectionIntoArray(properties, "[native program path]");
            File binFolder = new File(pathToContainerJar);// + "..\\..\\bin");
            binFolder = binFolder.getParentFile();
            binFolder = new File(binFolder, "bin");
//System.out.println("binFolder: " + binFolder + ", binFolder.exists(): " + binFolder.exists());
            if (!binFolder.exists())
                binFolder = null;
            File f;
            for (int i=0; i<secContents.size(); i++) {
                String fileName = (String) secContents.at(i);
                if (fileName == null || fileName.trim().length() == 0) {
                    secContents.remove(i);
                    i--;
                    continue;
                }
                fileName = fileName.trim();
                f = new File(fileName);
                /* The folders can be specified by absolute or relative paths. If the
                 * file does not exist, then test if its path is specified relatively to the
                 * location of the 'container.properties' file.*/
                if (!f.exists())
                    f = new File(pathToContainerJar + fileName);
                if (!f.exists()) {
                    secContents.remove(i);
                    i--;
                    System.out.println("Invalid native program path entry \"" + f + "\"");
                    continue;
                }
                if (!f.isDirectory()) {
                    secContents.remove(i);
                    i--;
                    System.out.println("Invalid native program path entry \"" + f + "\". It should be a directory.");
                    continue;
                }
                secContents.put(i, f);
            }
            int numOfFolders = secContents.size();
            if (binFolder != null) numOfFolders++;
            File[] folders = new File[numOfFolders];
            for (int i=0; i<secContents.size(); i++) {
                folders[i] = (File) secContents.at(i);
//                System.out.println("Read path folder: " + folders[i]);
            }
            // The bin folder of the E-Slate installation is by default in the path
            if (binFolder != null) {
//                System.out.println("Registering bin folder");
                folders[folders.length-1] = binFolder;
            }
//System.out.println("ESlate.setNativeProgramFolders() folders.length: " + folders.length);
            ESlate.setNativeProgramFolders(folders);
    	}

            /* Parse the properties String, locate the "Default Background Color" section and read it.
             */
            secContents = readRegistrySectionIntoArray(properties, "[default background color]");
//            System.out.println("secContents: " + secContents + ", secContents.size(): " + secContents.size());
            if (secContents.size() != 0 && ((String) secContents.at(0)).trim().length() != 0) {
                String defaultBgrColorStr = (String) secContents.at(0);
                /* If the properties file does not contain an explicit color, but the
                 * "desktop" string, then under the windows L&F use the color of the
                 * Windows desktop. Under any other L&F use the default bgr color of
                 * the L&F for the DesktopPane.
                 */
                if (defaultBgrColorStr.toLowerCase().equals("desktop")) {
                    defaultContainerBgrColor = getLAFDesktopColor();
                }else{
                    //Parse the string to create the color
                    int red = 0, green = 0, blue = 0;
                    int firstCommaIndex = defaultBgrColorStr.indexOf(',');
                    /* The red part of the color */
                    if (firstCommaIndex != -1) {
                        try{
                            red = Integer.parseInt(defaultBgrColorStr.substring(0, firstCommaIndex).trim());
                        }catch (Throwable thr) {}

                        int secCommaIndex = defaultBgrColorStr.indexOf(',', firstCommaIndex+1);
                        /* The green part of the color */
                        if (secCommaIndex != -1) {
                            try{
                                green = Integer.parseInt(defaultBgrColorStr.substring(firstCommaIndex+1, secCommaIndex).trim());
                            }catch (Throwable thr) {}
                            /* The blue part of the color. */
                            try{
                                blue = Integer.parseInt(defaultBgrColorStr.substring(secCommaIndex+1).trim());
                            }catch (Throwable thr) {}
                        }else{
                            /* The rest of the string till its end refers to the green part of the color, i.e.
                             * there is not blue part for the color.
                             */
                            try{
                                green = Integer.parseInt(defaultBgrColorStr.substring(firstCommaIndex+1).trim());
                            }catch (Throwable thr) {}
                        }
                        defaultContainerBgrColor = new Color(red, green, blue);
                    }else{
                        /* The string contains only the red part of the color. The green and blue
                         * parts are obsolete.
                         */
                        try{
                            red = Integer.parseInt(defaultBgrColorStr.trim());
                            defaultContainerBgrColor = new Color(red, green, blue);
                        }catch (Throwable thr) {
                            /* Under the windows L&F use the color of the Windows
                             * desktop. Under any other L&F use the default bgr color
                             * of the L&F for the DesktopPane.
                             */
                            defaultContainerBgrColor = getLAFDesktopColor();
                        }
                    }
                }
//                System.out.println("defaultContainerBgrColor: " + defaultContainerBgrColor);
            }

            /* Parse the properties String, locate the "SoundThemes" section and read it.
             */
            secContents = readRegistrySectionIntoArray(properties, "[sound themes]");
            for (int i=0; i<secContents.size(); i++)
                soundThemeFiles.add(secContents.at(i));
        } catch (Throwable thr) {
            thr.printStackTrace();
            System.out.println("Unable to read properties file " + containerPropertiesFile);
            return false;
        }

/*        Enumeration enumeration = installedComponentNames.keys();
        System.out.println("======================================================");
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            System.out.println(key + ", " + installedComponentNames.get(key) + ", Available? " + componentAvailability.get(key));
        }
        System.out.println("preloadMwdFileName: " + preloadMwdFileName);
        System.out.println("======================================================");
*/
        // Load the specified microworld
        if (preloadMwdFileName != null) {
            //closeSplashWindow();
            if (parentComponent != null && ESlateContainerFrame.class.isAssignableFrom(parentComponent.getClass())) {
//                System.out.println("show() 2");
                ((ESlateContainerFrame) parentComponent).setVisible(true);
            }
            paintImmediately(getVisibleRect());

            setLoadingMwd(true);

            int commaIndex = preloadMwdFileName.indexOf(',');
            if (commaIndex != -1) {
                if (preloadMwdFileName.length() > commaIndex+2) {
                    preloadMwdSiteAddress = preloadMwdFileName.substring(commaIndex+2);
                    preloadMwdFileName = preloadMwdFileName.substring(0, commaIndex);
                }else{
                    preloadMwdSiteAddress = null;
                    preloadMwdFileName = null;
                }
            }else
                preloadMwdSiteAddress = null;

//            System.out.println("loading : "  + preloadMwdFileName);
//            System.out.println("site: " + preloadMwdSiteAddress);
            if (preloadMwdFileName != null) {
                if (preloadMwdSiteAddress == null) {
                    try{
                        if (createNewMicroworld()) {
                            /* If there was a problem while loading the microworld,
                             * then discard the whole microworld. We can't show a dialog message
                             * because this freezes the VM, as the Container is not visible yet.
                             */
                            if (!load(preloadMwdFileName, false, true, 0)) {
                                setLoadingMwd(false);
                                throw new Exception();
                            }

                            currentlyOpenMwdFileName = preloadMwdFileName;
                            if (!microworld.microworldNameUserDefined)
                                setContainerTitle(ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
                            openFileRemote = false;
//// nikosM
                            webServerMicrosHandle.webSite = null;
                            if (!soundListenerMap.containsListenerFor(MicroworldListener.class, "microworldLoaded"))
                                playSystemSound(SoundTheme.MWD_OPENED_SOUND);
//// nikosM end
                        }
                    }catch (Throwable thr) {
                        thr.printStackTrace();
                        setLoadingMwd(false);
                        closeMicroworld(false);
                    }
                }else{
                    try{
//// nikosM
//                        byte[] fileBytes = webServerMicrosHandle.openServerFile(preloadMwdSiteAddress, preloadMwdFileName);
                        if (!webServerMicrosHandle.openRemoteMicroWorld(preloadMwdSiteAddress,preloadMwdFileName,false))
//System.out.print("preloadMwdFileName"+preloadMwdFileName);
                            return true;
/*                        File tmpServerFile = writeServerFile(fileBytes, tmpFile);
                        if (tmpServerFile != null) {
                            if (createNewMicroworld()) {
                                if (load(tmpFile.getAbsolutePath(), false, true)) {
                                    openFileRemote = true;
                                    container.webServerMicrosHandle.webSite = preloadMwdSiteAddress;
                                    currentlyOpenMwdFileName = preloadMwdFileName;
                                    if (!currentView.mwdTitleEnabled)
                                        setContainerTitle(ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
                                }else{
                                    tmpFile.delete();
                                    throw new Exception();
                                }
                                tmpFile.delete();
                            }

                        }
*/
////nikosM end
                    }catch (Throwable thr) {
                        thr.printStackTrace();
                        setLoadingMwd(false);
                        closeMicroworld(false);
                    }
                }
            }
            setLoadingMwd(false);
        }
        if (updatePropertiesFile) {
        	System.out.println("Updating container properties file");
            writeContainerProperties();
        }

//System.out.println("ET readContainerProperties() end: " + (System.currentTimeMillis()-start));
        System.out.println("readContainerProperties() END");
        return true;
    }

    private HashMap readRegistrySectionIntoHashMap(String registryString, String sectionHeader) {
        HashMap sectionContents = new HashMap(true);
        int sectionStart = registryString.toLowerCase().indexOf(sectionHeader);
        if (sectionStart != -1) {
            sectionStart = sectionStart + sectionHeader.length();
            int registryStringLength = registryString.length();
            while (registryString.charAt(sectionStart) != '|')
                sectionStart++;
            sectionStart++;
            int tokenStart = sectionStart;
            while (true) {
                int tokenEnd = registryString.indexOf('=', tokenStart);
                if (tokenEnd == -1)
                    break;
                String registryKey = registryString.substring(tokenStart, tokenEnd).trim();
                if (registryKey.length() == 0 || registryKey.indexOf('|') != -1)
                    break;
                if (tokenEnd < registryString.length()-1)
                    tokenStart = tokenEnd+1;
                else
                    break;
                tokenEnd = registryString.indexOf('|', tokenStart);
                if (tokenEnd == -1)
                    break;
                String registryValue = registryString.substring(tokenStart, tokenEnd).trim();
                if (registryValue.length() == 0 || registryValue.indexOf('|') != -1)
                    break;
                sectionContents.add(registryKey, registryValue);
                if (tokenEnd < registryString.length()-1)
                    tokenEnd = tokenEnd+1;
                else
                    break;
                while (tokenEnd != registryStringLength && registryString.charAt(tokenEnd) == ' ') tokenEnd++;
                if (tokenEnd == registryStringLength || registryString.charAt(tokenEnd) == '|')
                    break;
                tokenStart = tokenEnd;
            }
        }
        return sectionContents;
    }

    private Array readRegistrySectionIntoArray(String registryString, String sectionHeader) {
//        System.out.println("SECTION: " + sectionHeader);
        Array sectionContents = new Array();
        int sectionStart = registryString.toLowerCase().indexOf(sectionHeader);
        if (sectionStart != -1) {
            sectionStart = sectionStart + sectionHeader.length();
            int registryStringLength = registryString.length();
            while (registryString.charAt(sectionStart) != '|')
                sectionStart++;
            sectionStart++;
            int tokenStart = sectionStart;
            while (true) {
                int tokenEnd = registryString.indexOf('|', tokenStart);
                if (tokenStart == tokenEnd) //empty line
                    break;
                if (tokenEnd == -1)
                    break;
                String registryItem = registryString.substring(tokenStart, tokenEnd).trim();
                sectionContents.add(registryItem);
                tokenEnd++;
                while (tokenEnd != registryStringLength && registryString.charAt(tokenEnd) == ' ') tokenEnd++;
                if (tokenEnd == registryStringLength || registryString.charAt(tokenEnd) == '|')
                    break;
                tokenStart = tokenEnd;
            }
        }
        return sectionContents;
    }

    String[] readMicroworldSettingsProfiles() {
        if (containerPropertiesFile == null) return null;

        String properties;
        try{
//            BufferedReader br = new BufferedReader(new FileReader(containerPropertiesFile), 1024);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("|");
            }
            properties = sb.toString();
//            System.out.println("properties: " + properties);
            br.close();
            Array profileFiles = readRegistrySectionIntoArray(properties, "[microworld setting profiles]");
            String[] profileFileNames = new String[profileFiles.size()];
            for (int i=0; i<profileFileNames.length; i++)
                profileFileNames[i] = (String) profileFiles.at(i);
            return profileFileNames;
        }catch (Throwable thr) {
            System.out.println("readMicroworldSettingsProfiles(): Unable to read container properties");
            thr.printStackTrace();
            return null;
        }
    }

    protected void writeContainerProperties() {
        /* Read the registry again in to the properties string. The portions of this
         * string which have been read by readContainerProperties() and possibly changed
         * will be overwitten. After the new properties string is created, it will be
         * stored back in the registry.
         */
        String properties;
        try{
//            BufferedReader br = new BufferedReader(new FileReader(containerPropertiesFile), 1024);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("|");
            }
            properties = sb.toString();
            br.close();

///=================================CODE FROM MANTES - START HERE==============================////////////////////////////////////////
            int installedLAFIndex = findRegistrySectionStart(properties, "[installed lookandfeels]");                             //
            if (installedLAFIndex != -1){                                                                                            //                                                     //
                properties = truncateRegistrySection(properties, installedLAFIndex);                                                 //
            }else                                                                                                                    //
                installedLAFIndex = 0; //Write the new registry section at the beginning of the registry                             //
                                                                                                                                     //
            /* Create the string which contains the information about the installed look and feels.                                      //
             * Embed this string in the properties string.                                                                           //
             */                                                                                                                      //
            String installedLookAndFeelsStr = "[Installed LookAndFeels]|";                                                                                         //
            InstalledLookAndFeelStructure.LAFEntry[] entries2 = installedLAFs.getSortedEntries();                                    //
            if (entries2.length > 0) {                                                                                               //
                for (int i=0; i<entries2.length; i++) {                                                                              //
                    installedLookAndFeelsStr = installedLookAndFeelsStr + entries2[i].name + '=' + entries2[i].className + '|';      //
                }                                                                                                                    //
            }                                                                                                                        //
            installedLookAndFeelsStr = installedLookAndFeelsStr + '|';                                                           //
            StringBuffer propBuff1 = new StringBuffer(properties);                                                                //
            propBuff1.insert(installedLAFIndex, installedLookAndFeelsStr);                                                        //
            properties = propBuff1.toString();                                                                                    //
                                                                                                                                     //
            /* Update the "Available LookAndFeels" section of the registry                                                           //
             */                                                                                                                      //
            int availableLAFIndex = findRegistrySectionStart(properties, "[available lookandfeels]");                                //
            if (availableLAFIndex != -1)                                                                                             //
                properties = truncateRegistrySection(properties, availableLAFIndex);                                                 //
            else                                                                                                                     //
                availableLAFIndex = 0; //Write the new registry section at the beginning of the registry                             //
                                                                                                                                     //
            /* Create the string which contains the information about the available look and feels.                                  //
             * Embed this string in the properties string.                                                                           //
             */                                                                                                                      //
            String availableLookAndFeelsStr = "[Available LookAndFeels]|";                                                                                         //
            if (entries2.length > 0) {                                                                                               //
                for (int i=0; i<entries2.length; i++) {                                                                              //
                    availableLookAndFeelsStr = availableLookAndFeelsStr + entries2[i].name + '=' + entries2[i].availability + '|';   //
                }                                                                                                                    //
            }                                                                                                                        //
            availableLookAndFeelsStr = availableLookAndFeelsStr + '|';                                                           //
            StringBuffer propBuff2 = new StringBuffer(properties);                                                                //
            propBuff2.insert(availableLAFIndex, availableLookAndFeelsStr);                                                        //
            properties = propBuff2.toString();                                                                                    //
                                                                                                                                     //
///=================================CODE FROM MANTES - END HERE==============================//////////////////////////////////////////

            /* Update the "Installed Components" section of the registry
             */
            int installedCompIndex = findRegistrySectionStart(properties, "[installed components]");
            if (installedCompIndex != -1)
                properties = truncateRegistrySection(properties, installedCompIndex);
            else
                installedCompIndex = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the information about the installed components.
             * Embed this string in the properties string.
             */
            String installedComponentsStr;
            CompoEntry[] entries = installedComponents.getSortedEntries();
            if (entries.length > 0) {
//            if (installedComponentNames.size() > 0) {
                installedComponentsStr = "[Installed Components]|";
/*                Enumeration enumeration = installedComponentNames.keys();
                while (enumeration.hasMoreElements()) {
                    String key = (String) enumeration.nextElement();
                    installedComponentsStr = installedComponentsStr + key + '=' + (String) installedComponentNames.get(key) + '|';
                }
*/
                for (int i=0; i<entries.length; i++) {
                    boolean visualComponent = true;
                    try{
                        Class cls = Class.forName(entries[i].className);
                        if (!Component.class.isAssignableFrom(cls))
                            visualComponent = false;
                    }catch (Throwable thr) {}
                    installedComponentsStr = installedComponentsStr + entries[i].name + '=' + entries[i].className + '(' + visualComponent + ')';
                    if (entries[i].groupName==null || entries[i].groupName.trim().length()==0)
                    	installedComponentsStr+='|';
                    else
                    	installedComponentsStr+='#'+entries[i].groupName.trim()+'|';
                }
                installedComponentsStr = installedComponentsStr + '|';
                StringBuffer propBuff = new StringBuffer(properties);
                propBuff.insert(installedCompIndex, installedComponentsStr);
                properties = propBuff.toString();
            }

            /* Update the "Available Components" section of the registry
             */
            int availableCompIndex = findRegistrySectionStart(properties, "[available components]");
            if (availableCompIndex != -1)
                properties = truncateRegistrySection(properties, availableCompIndex);
            else
                availableCompIndex = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the information about the available components.
             * Embed this string in the properties string.
             */
            String availableComponentsStr;
            if (entries.length > 0) {
//            if (componentAvailability.size() > 0) {
                availableComponentsStr = "[Available Components]|";
//                Enumeration enumeration = componentAvailability.keys();
                for (int i=0; i<entries.length; i++) {
                    availableComponentsStr = availableComponentsStr + entries[i].name + '=' + entries[i].availability + '|';
                }
/*                while (enumeration.hasMoreElements()) {
                    String key = (String) enumeration.nextElement();
                    availableComponents = availableComponents + key + '=' + ((Boolean) componentAvailability.get(key)).toString() + '|';
                }
*/
                availableComponentsStr = availableComponentsStr + '|';
                StringBuffer propBuff = new StringBuffer(properties);
                propBuff.insert(availableCompIndex, availableComponentsStr);
                properties = propBuff.toString();
            }

            /* Update the "Registered web sites" section of the registry
             */
            int sectionIndex = findRegistrySectionStart(properties, "[web sites]");
            if (sectionIndex != -1)
                properties = truncateRegistrySection(properties, sectionIndex);
            else
                sectionIndex = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the information about the installed web sites.
             * Embed this string in the properties string.
             */
            String regWebSites;
            if (webSites.size() > 0) {
                regWebSites = "[Web Sites]|";
                Enumeration enumeration = webSites.keys();
                while (enumeration.hasMoreElements()) {
                    String key = (String) enumeration.nextElement();
                    regWebSites = regWebSites + key + '=' + (String) webSites.get(key) + '|';
                }
                regWebSites = regWebSites + '|';
                StringBuffer propBuff = new StringBuffer(properties);
                propBuff.insert(sectionIndex, regWebSites);
                properties = propBuff.toString();
            }

            /* Update the "Available web sites" section of the registry
             */
            sectionIndex = findRegistrySectionStart(properties, "[available web sites]");
            if (sectionIndex != -1)
                properties = truncateRegistrySection(properties, sectionIndex);
            else
                sectionIndex = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the information about the available web sites.
             * Embed this string in the properties string.
             */
            String regWebSiteAvailability;
            if (webSiteAvailability.size() > 0) {
                regWebSiteAvailability = "[Available Web Sites]|";
                Enumeration enumeration = webSiteAvailability.keys();
                while (enumeration.hasMoreElements()) {
                    String key = (String) enumeration.nextElement();
                    regWebSiteAvailability = regWebSiteAvailability + key + '=' + ((Boolean) webSiteAvailability.get(key)).toString() + '|';
                }
                regWebSiteAvailability = regWebSiteAvailability + '|';
                StringBuffer propBuff = new StringBuffer(properties);
                propBuff.insert(sectionIndex, regWebSiteAvailability);
                properties = propBuff.toString();
            }

            /* Update the "Site Common Forlders" section of the registry
             */
            sectionIndex = findRegistrySectionStart(properties, "[site common folders]");
            if (sectionIndex != -1)
                properties = truncateRegistrySection(properties, sectionIndex);
            else
                sectionIndex = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the information about the web site common dirs.
             * Embed this string in the properties string.
             */
            String regWebSiteCommonDirs;
            if (webSiteCommonDirs.size() > 0) {
                regWebSiteCommonDirs = "[Site Common Folders]|";
                Enumeration enumeration = webSiteCommonDirs.keys();
                while (enumeration.hasMoreElements()) {
                    String key = (String) enumeration.nextElement();
                    regWebSiteCommonDirs = regWebSiteCommonDirs + key + '=' + (String) webSiteCommonDirs.get(key) + '|';
                }
                regWebSiteCommonDirs = regWebSiteCommonDirs + '|';
                StringBuffer propBuff = new StringBuffer(properties);
                propBuff.insert(sectionIndex, regWebSiteCommonDirs);
                properties = propBuff.toString();
            }

            /* Update the "Preload Microworld" section of the registry
             */
            int sectionStart = findRegistrySectionStart(properties, "[preload microworld]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the information about the microworld file to
             * be loaded upon start-up. Embed this string in the registry string.
             */
            if (preloadMwdFileName != null) {
                String prealodMicroworldSection = "[Preload Microworld]|";
                if (preloadMwdSiteAddress != null && preloadMwdSiteAddress.trim().length() != 0)
                    prealodMicroworldSection = prealodMicroworldSection + "Microworld File" + '=' + preloadMwdFileName + ", " + preloadMwdSiteAddress + '|';
                else
                    prealodMicroworldSection = prealodMicroworldSection + "Microworld File" + '=' + preloadMwdFileName + '|';
//                prealodMicroworldSection = prealodMicroworldSection + "Microworld Site" + '=' + preloadMwdSiteAddress + '|';
                prealodMicroworldSection = prealodMicroworldSection + '|';
                StringBuffer propBuff = new StringBuffer(properties);
                propBuff.insert(sectionStart, prealodMicroworldSection);
                properties = propBuff.toString();
            }

            /* Update the "Container properties" section of the registry
             */
            sectionStart = findRegistrySectionStart(properties, "[container properties]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the values of the container's properties
             */
            String containerPropertiesSection = "[Container Properties]|";
            containerPropertiesSection = containerPropertiesSection + "Enable Java Console" + '=' + javaConsoleEnabled + '|';
            if (splitPane.isLeftPanelClosed())
                containerPropertiesSection = containerPropertiesSection + "Splitpane Divider Location" + '=' + splitPane.previousDividerLocation + '|';
            else
                containerPropertiesSection = containerPropertiesSection + "Splitpane Divider Location" + '=' + splitPane.getDividerLocation() + '|';
            if (fileDialog != null)
                containerPropertiesSection = containerPropertiesSection + "Current directory" + '=' + fileDialog.getDirectory() + '|';
            else if (currentFileDialogDir != null) {
                // This is the case when a directory has been read from container.properties, but the
                // 'fileDialog' has never been instantiated during this E-Slate session.
                containerPropertiesSection = containerPropertiesSection + "Current directory" + '=' + currentFileDialogDir + '|';
            }

            containerPropertiesSection = containerPropertiesSection + "Sort on field" + '=' + installedComponents.getCurrentSortField() + '|';
            containerPropertiesSection = containerPropertiesSection + "Sort direction up" + '=' + installedComponents.isSortDirectionUp() + '|';
            containerPropertiesSection = containerPropertiesSection + "L&F" + '=' + UIManager.getLookAndFeel().getClass().getName() + '|';
            containerPropertiesSection = containerPropertiesSection + "Performance Monitor Enabled" + '=' + PerformanceManager.getPerformanceManager().isEnabled() + '|';
            containerPropertiesSection = containerPropertiesSection + '|';
            StringBuffer propBuff = new StringBuffer(properties);
            propBuff.insert(sectionStart, containerPropertiesSection);
            properties = propBuff.toString();
//            System.out.println("WriteContainerProperties properties: " + properties);

            /* Update the "Reopened microworlds" section of the registry
             */
            sectionStart = findRegistrySectionStart(properties, "[reopened microworlds]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the file names of the last 10 (at most) opened microworlds.
             * Embed this string in the properties string.
             */
            String reopenedMwds;
//            if (menuPanel.microworldReopenMenu.getItemCount() > 0) {
            if (microworldList.size() > 0) {
                reopenedMwds = "[Reopened Microworlds]|";
////nikosM
//System.out.println("microworldList.size(): " + microworldList.size());
                int itemCount = microworldList.size();
                for (int i=itemCount-1; i>=0; i--) {
                    String mwdFileName = ((AbbreviatedCheckBoxMenuItem) microworldList.at(i)).getFullText();
                    if (((AbbreviatedCheckBoxMenuItem) microworldList.at(i)).isRemote())
                        mwdFileName+='*';
////nikosM end
                    reopenedMwds = reopenedMwds + mwdFileName + '|';
                }

/*                com.objectspace.jgl.SListIterator iter = microworldList.end();
                while (!iter.atBegin()) {
                    String mwdFileName = ((JCheckBoxMenuItem) iter.get()).getText();
                    reopenedMwds = reopenedMwds + mwdFileName + '|';
                    iter.advance();
                }
*/
                reopenedMwds = reopenedMwds + '|';
                propBuff = new StringBuffer(properties);
                propBuff.insert(sectionStart, reopenedMwds);
                properties = propBuff.toString();
            }

            /* Update the "Microworlds history list" section of the registry
             */
            sectionStart = findRegistrySectionStart(properties, "[microworld history list]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the history of the opened microworlds.
             * Embed this string in the properties string.
             */
            String mwds;
            int historySize = mwdHistory.getItemCount();
//            if (menuPanel.microworldReopenMenu.getItemCount() > 0) {
            if (historySize > 0) {
                mwds = "[Microworld History List]|";
                mwds = mwds + mwdHistory.getHistoryIndex() + '|';
                for (int i=historySize-1; i>=0; i--) {
                    String mwdFileName = mwdHistory.getMicroworldAt(i);
////nikosM
                    if (mwdHistory.isRemoteMicroworldAt(i))
                        mwdFileName+="*";
////nikosM end
                    mwds = mwds + mwdFileName + '|';
                }
                mwds = mwds + '|';
                propBuff = new StringBuffer(properties);
                propBuff.insert(sectionStart, mwds);
                properties = propBuff.toString();
            }

            /* Update the "Container Bounds" section of the registry
             */
            sectionStart = findRegistrySectionStart(properties, "[container bounds]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the bounds of the Container's window.
             * Embed this string in the properties string.
             */
            String sizeStr = "[Container Bounds]|";
            Container topLevelAncestor = this.getTopLevelAncestor();
            sizeStr = sizeStr + topLevelAncestor.getBounds().x + ',' + topLevelAncestor.getBounds().y + ',';
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension containerSize = topLevelAncestor.getSize();
            Rectangle r=WorkArea.getWorkArea();
            Dimension workAreaSize = null;
            if (r != null)
                workAreaSize = new Dimension(r.width,r.height);
            else
                workAreaSize = screenSize;
//            System.out.println("containerSize: " + containerSize + ", workAreaSize: " + workAreaSize);
            if (screenSize.equals(containerSize) || containerSize.equals(workAreaSize) || (containerSize.width > screenSize.width && containerSize.height > screenSize.height))
                sizeStr = sizeStr + "maximized |";
            else
                sizeStr = sizeStr + containerSize.width + ',' + containerSize.height + '|';
            sizeStr = sizeStr + '|';
            propBuff = new StringBuffer(properties);
            propBuff.insert(sectionStart, sizeStr);
            properties = propBuff.toString();

            /* Update the "Microworld File Extensions" section of the registry
             */
            sectionStart = findRegistrySectionStart(properties, "[microworld file extensions]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the valid microworld file extensions displayed in
             * the OPEN and SAVE file dialogs.
             * Embed this string in the properties string.
             */
            String mwdExtensions;
            if (mwdFileExtensions != null && mwdFileExtensions.length > 0) {
                mwdExtensions = "[Microworld File Extensions]|";
                int itemCount = mwdFileExtensions.length;
                for (int i=itemCount-1; i>=0; i--) {
                    String mwdExtension = mwdFileExtensions[i];
//                    System.out.println("writing: " + mwdExtension);
                    mwdExtensions = mwdExtensions + mwdExtension + '|';
                }
            }else{
                mwdExtensions = "[Microworld File Extensions]|";
                mwdExtensions = mwdExtensions + "mwd" + '|';
            }

            mwdExtensions = mwdExtensions + '|';
            propBuff = new StringBuffer(properties);
            propBuff.insert(sectionStart, mwdExtensions);
            properties = propBuff.toString();

            /* Update the "Native Program Path" section of the registry
             */
            sectionStart = findRegistrySectionStart(properties, "[native program path]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains all the directories of the native program path.
             * Embed this string in the properties string.
             */
            String nativePath = "[Native Program Path]|";
            File[] folders = ESlate.getNativeProgramFolders();
            File binFolder = getBinFolder(pathToContainerJar);
            if (!binFolder.exists())
                binFolder = null;
            if (folders.length > 0) {
//                System.out.println("folders.length: " + folders.length + ", binFolder: " + binFolder);
                int pathCount = folders.length;
                if (binFolder == null) {
                    for (int i=pathCount-1; i>=0; i--) {
                        nativePath = nativePath + folders[i].getCanonicalPath() + '|';
                    }
                }else{
                    for (int i=pathCount-1; i>=0; i--) {
                        // Don't write out the bin folder of the E-Slate installation
//                        System.out.println("folders[i]: " + folders[i].getCanonicalPath() + ", binFolder: " + binFolder.getCanonicalPath() + ", folders[i].equals(binFolder): " + folders[i].equals(binFolder));
                        if (!folders[i].equals(binFolder))
                            nativePath = nativePath + folders[i].getCanonicalPath() + '|';
                    }
                }
            }
            nativePath = nativePath + '|';
            propBuff = new StringBuffer(properties);
            propBuff.insert(sectionStart, nativePath);
            properties = propBuff.toString();


            /* Update the "Default Background Color" section of the registry
             */
            sectionStart = findRegistrySectionStart(properties, "[default background color]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the current default background color
             * Embed this string in the properties string.
             */
            String bgrColorStr;
            if (defaultContainerBgrColor != null) {
                bgrColorStr = "[Default Background Color]|";
                if (defaultContainerBgrColor.equals(getLAFDesktopColor()))
                    bgrColorStr = bgrColorStr + "desktop" + '|';
                else
                    bgrColorStr = bgrColorStr + defaultContainerBgrColor.getRed() + ',' +
                                defaultContainerBgrColor.getGreen() + ',' +
                                defaultContainerBgrColor.getBlue()
                                + '|';
            }else{
                bgrColorStr = "[Default Background Color]|";
            }
//            System.out.println("bgrColorStr: " + bgrColorStr + ", sectionStart: " + sectionStart);
            bgrColorStr = bgrColorStr + '|';
            propBuff = new StringBuffer(properties);
            propBuff.insert(sectionStart, bgrColorStr);
            properties = propBuff.toString();

            /* Update the "Sound Themes" section of the registry
             */
            sectionStart = findRegistrySectionStart(properties, "[sound themes]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the file names of the last 10 (at most) opened microworlds.
             * Embed this string in the properties string.
             */
            String soundThemesStr;
//            if (menuPanel.microworldReopenMenu.getItemCount() > 0) {
            if (soundThemeFiles.size() > 0) {
                soundThemesStr = "[Sound Themes]|";
                int itemCount = soundThemeFiles.size();
//                for (int i=itemCount-1; i>=0; i--) {
                for (int i=0; i<itemCount; i++) {
                    String themeFileName = (String) soundThemeFiles.get(i);
                    soundThemesStr = soundThemesStr + themeFileName + '|';
                }
                soundThemesStr = soundThemesStr + '|';
                propBuff = new StringBuffer(properties);
                propBuff.insert(sectionStart, soundThemesStr);
                properties = propBuff.toString();
            }

            /* Write the properties string in the registry.
             */
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
            int lineStartIndex = 0;
            int lineEndIndex = 0;
            while (lineStartIndex < properties.length()-1 && (lineEndIndex = properties.indexOf('|', lineStartIndex)) != -1) {
//                System.out.println("lineStartIndex: " + lineStartIndex + ", lineEndIndex: " + lineEndIndex + ", str: " + properties.substring(lineStartIndex, lineEndIndex));
                bw.write(properties.substring(lineStartIndex, lineEndIndex), 0, lineEndIndex-lineStartIndex);
                bw.newLine();
                lineStartIndex = lineEndIndex+1;
            }
            bw.close();
        }catch (Exception exc) {
            System.out.println("Unable to write properties file " + exc.getClass() + ", " + exc.getMessage());
            exc.printStackTrace();
            return;
        }
    }

    /* Updates only the "Microworld File Extensions" section of the registry.
     */
    private void updateMicroworldFileExtensionsSection() {
        String properties = getContainerPropertiesFileContents();

        /* Update the "Microworld File Extensions" section of the registry
         */
        int sectionStart = findRegistrySectionStart(properties, "[microworld file extensions]");
        if (sectionStart != -1)
            properties = truncateRegistrySection(properties, sectionStart);
        else
            sectionStart = 0; //Write the new registry section at the beginning of the registry

        /* Create the string which contains the valid microworld file extensions displayed in
         * the OPEN and SAVE file dialogs.
         * Embed this string in the properties string.
         */
        String mwdExtensions;
        if (mwdFileExtensions != null && mwdFileExtensions.length > 0) {
            mwdExtensions = "[Microworld File Extensions]|";
            int itemCount = mwdFileExtensions.length;
            for (int i=itemCount-1; i>=0; i--) {
                String mwdExtension = mwdFileExtensions[i];
//                System.out.println("writing: " + mwdExtension);
                mwdExtensions = mwdExtensions + mwdExtension + '|';
            }
        }else{
            mwdExtensions = "[Microworld File Extensions]|";
            mwdExtensions = mwdExtensions + "mwd" + '|';
        }

        mwdExtensions = mwdExtensions + '|';
        StringBuffer propBuff = new StringBuffer(properties);
        propBuff.insert(sectionStart, mwdExtensions);
        properties = propBuff.toString();

        writeContainerPropertiesFileContents(properties);
    }

    /* Updates only the "Container properties" section of the registry.
     */
    public void updateContainerPropertiesSection() {
        String properties = getContainerPropertiesFileContents();
        if (properties == null) return;

        /* Update the "Container properties" section of the registry
         */
        int sectionStart = findRegistrySectionStart(properties, "[container properties]");
        if (sectionStart != -1)
            properties = truncateRegistrySection(properties, sectionStart);
        else
            sectionStart = 0; //Write the new registry section at the beginning of the registry

        /* Create the string which contains the values of the container's properties
         */
        String containerPropertiesSection = "[Container Properties]|";
        containerPropertiesSection = containerPropertiesSection + "Enable Java Console" + '=' + javaConsoleEnabled + '|';
        if (splitPane.isLeftPanelClosed())
            containerPropertiesSection = containerPropertiesSection + "Splitpane Divider Location" + '=' + splitPane.previousDividerLocation + '|';
        else
            containerPropertiesSection = containerPropertiesSection + "Splitpane Divider Location" + '=' + splitPane.getDividerLocation() + '|';
        if (fileDialog != null)
            containerPropertiesSection = containerPropertiesSection + "Current directory" + '=' + fileDialog.getDirectory() + '|';
        else if (currentFileDialogDir != null) {
            // This is the case when a directory has been read from container.properties, but the
            // 'fileDialog' has never been instantiated during this E-Slate session.
            containerPropertiesSection = containerPropertiesSection + "Current directory" + '=' + currentFileDialogDir + '|';
        }

        containerPropertiesSection = containerPropertiesSection + "Sort on field" + '=' + installedComponents.getCurrentSortField() + '|';
        containerPropertiesSection = containerPropertiesSection + "Sort direction up" + '=' + installedComponents.isSortDirectionUp() + '|';
        containerPropertiesSection = containerPropertiesSection + "L&F" + '=' + UIManager.getLookAndFeel().getClass().getName() + '|';
        containerPropertiesSection = containerPropertiesSection + "Performance Monitor Enabled" + '=' + PerformanceManager.getPerformanceManager().isEnabled() + '|';

        containerPropertiesSection = containerPropertiesSection + '|';
        StringBuffer propBuff = new StringBuffer(properties);
        propBuff.insert(sectionStart, containerPropertiesSection);
        properties = propBuff.toString();

        writeContainerPropertiesFileContents(properties);
    }

    /* Updates only the "reopened microworlds" section of the registry.
     */
    void updateReopenedMicroworldsSection() {
        String properties;
        try{
//            BufferedReader br = new BufferedReader(new FileReader(containerPropertiesFile), 1024);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("|");
            }
            properties = sb.toString();
            br.close();

            /* Update the "Reopened microworlds" section of the registry
             */
            int sectionStart = findRegistrySectionStart(properties, "[reopened microworlds]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the file names of the last 10 (at most) opened microworlds.
             * Embed this string in the properties string.
             */
            String reopenedMwds;
//            if (menuPanel.microworldReopenMenu.getItemCount() > 0) {
            if (microworldList.size() > 0) {
                reopenedMwds = "[Reopened Microworlds]|";
                com.objectspace.jgl.ForwardIterator iter = microworldList.start();
                while (!iter.atEnd()) {
                    String mwdFileName = ((AbbreviatedCheckBoxMenuItem) iter.get()).getFullText();
////nikosM
                    if (((AbbreviatedCheckBoxMenuItem) iter.get()).isRemote())
                        mwdFileName+="*";
////nikosM end
                    reopenedMwds = reopenedMwds + mwdFileName + '|';
                    iter.advance();
                }
                reopenedMwds = reopenedMwds + '|';
                StringBuffer propBuff = new StringBuffer(properties);
                propBuff.insert(sectionStart, reopenedMwds);
                properties = propBuff.toString();
            }
////nikosM code
            reopenedMwds="";
////nikosM
            /* Update the "Microworlds history list" section of the registry
             */
            sectionStart = findRegistrySectionStart(properties, "[microworld history list]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the history of the opened microworlds.
             * Embed this string in the properties string.
             */
            String mwds;
            int itemCount = mwdHistory.getItemCount();
//            if (menuPanel.microworldReopenMenu.getItemCount() > 0) {
            if (itemCount > 0) {
                mwds = "[Microworld History List]|";
                mwds = mwds + mwdHistory.getHistoryIndex() + '|';
                for (int i=itemCount-1; i>=0; i--) {
                    String mwdFileName = mwdHistory.getMicroworldAt(i);
/////nikosM
                    if (mwdHistory.isRemoteMicroworldAt(i))
                        mwdFileName+="*";
                    mwds = mwds + mwdFileName + '|';
/////nikosM end
                }
                mwds = mwds + '|';
                StringBuffer propBuff = new StringBuffer(properties);
                propBuff.insert(sectionStart, mwds);
                properties = propBuff.toString();
            }

            /* Write the properties string in the registry.
             */
//\            BufferedWriter bw = new BufferedWriter(new FileWriter(containerPropertiesFile), 1024);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
            int lineStartIndex = 0;
            int lineEndIndex = 0;
            while (lineStartIndex < properties.length()-1 && (lineEndIndex = properties.indexOf('|', lineStartIndex)) != -1) {
                bw.write(properties.substring(lineStartIndex, lineEndIndex), 0, lineEndIndex-lineStartIndex);
                bw.newLine();
                lineStartIndex = lineEndIndex+1;
            }
            bw.close();
        }catch (Exception exc) {
            System.out.println("Unable to update the reopened microworlds section in the properties file " + exc.getClass() + ", " + exc.getMessage());
            return;
        }
    }

    /* Updates only the "Container Size" section of the registry.
     */
    void updateContainerSizeSection() {
        String properties;
        try{
//            BufferedReader br = new BufferedReader(new FileReader(containerPropertiesFile), 1024);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("|");
            }
            properties = sb.toString();
            br.close();

            /* Update the "Container Bounds" section of the registry
             */
            int sectionStart = findRegistrySectionStart(properties, "[container bounds]");
            if (sectionStart != -1)
                properties = truncateRegistrySection(properties, sectionStart);
            else
                sectionStart = 0; //Write the new registry section at the beginning of the registry

            /* Create the string which contains the bounds of the window that contains the Container.
             * Embed this string in the properties string.
             */
            String sizeStr = "[Container Bounds]|";
            Container topLevelAncestor = this.getTopLevelAncestor();
            sizeStr = sizeStr + topLevelAncestor.getBounds().x + ',' + topLevelAncestor.getBounds().y + ',';
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension containerSize = topLevelAncestor.getSize();
//            System.out.println("screenSize: " + screenSize);
//            System.out.println("containerSize: " + containerSize);
            Rectangle r=WorkArea.getWorkArea();
            Dimension workAreaSize = null;
            if (r != null)
                workAreaSize = new Dimension(r.width,r.height);
            else
                workAreaSize = screenSize;
//            System.out.println("containerSize: " + containerSize + ", workAreaSize: " + workAreaSize);
            if (screenSize.equals(containerSize) || containerSize.equals(workAreaSize) || (containerSize.width > screenSize.width && containerSize.height > screenSize.height))
                sizeStr = sizeStr + "maximized|";
            else
                sizeStr = sizeStr + containerSize.width + ',' + containerSize.height + '|';
            sizeStr = sizeStr + '|';
            StringBuffer propBuff = new StringBuffer(properties);
            propBuff.insert(sectionStart, sizeStr);
            properties = propBuff.toString();

            /* Write the properties string in the registry.
             */
//\            BufferedWriter bw = new BufferedWriter(new FileWriter(containerPropertiesFile), 1024);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
            int lineStartIndex = 0;
            int lineEndIndex = 0;
            while (lineStartIndex < properties.length()-1 && (lineEndIndex = properties.indexOf('|', lineStartIndex)) != -1) {
                bw.write(properties.substring(lineStartIndex, lineEndIndex), 0, lineEndIndex-lineStartIndex);
                bw.newLine();
                lineStartIndex = lineEndIndex+1;
            }
            bw.close();
        }catch (Exception exc) {
            System.out.println("Unable to update the Container Size section in the properties file " + exc.getClass() + ", " + exc.getMessage());
            return;
        }
    }

    /* Read the properties file into a String */
    public String getContainerPropertiesFileContents() {
        String properties;
        try{
//            BufferedReader br = new BufferedReader(new FileReader(containerPropertiesFile), 1024);
        	//BufferedReader br;
        	//if(!EMBEDDED_MODE) {
        	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
        	//} else {
        		//br = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")), 1024);
        	//}
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("|");
            }
            properties = sb.toString();
            br.close();
        }catch (Exception exc) {
            System.out.println("Unable to get the contents of the E-Slate properties file " + containerPropertiesFile + ". " + exc.getClass() + ", " + exc.getMessage());
            return null;
        }
        return properties;
    }

    public void writeContainerPropertiesFileContents(String contents) {
        /* Write the properties string in the registry.
         */
        try{
//\            BufferedWriter bw = new BufferedWriter(new FileWriter(containerPropertiesFile), 1024);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
            int lineStartIndex = 0;
            int lineEndIndex = 0;
            while (lineStartIndex < contents.length()-1 && (lineEndIndex = contents.indexOf('|', lineStartIndex)) != -1) {
                bw.write(contents.substring(lineStartIndex, lineEndIndex), 0, lineEndIndex-lineStartIndex);
                bw.newLine();
                lineStartIndex = lineEndIndex+1;
            }
            bw.close();
        }catch (Exception exc) {
            System.out.println("Unable to write the contents of the E-Slate properties file " + containerPropertiesFile + ". " + exc.getClass() + ", " + exc.getMessage());
            return;
        }
    }

    /* Find the beginning of a registry part.
     */
    int findRegistrySectionStart(String registry, String registryPart) {
        return registry.toLowerCase().indexOf(registryPart); //"[installed components]");
    }

    /* Truncate from the given registry string the section which starts at the specified
     * position.
     */
    String truncateRegistrySection(String properties, int sectionStart) {
        if (sectionStart != -1) {
            int sectionEnd = properties.indexOf('[', sectionStart+1);
            if (sectionEnd != -1) {
                /* In the following while loop check the case in which a '[' character
                 * is found, but it's not the start of the next section, but rather
                 * part of the contents of this section. A valid next section start
                 * has to have a '|' character before the '[' character.
                 */
                while (true) {
                    char c = properties.charAt(sectionEnd-1);
//System.out.println("truncateRegistrySection sectionEnd: " + sectionEnd + " (!(c == \'|\')): " + (!(c == '|')));
                    if (!(c == '|')) {
                        sectionEnd = properties.indexOf('[', sectionEnd+1);
                        if (sectionEnd == -1)
                            break;
                        continue;
                    }else
                        break;
                }
            }
            if (sectionEnd != -1)
                properties = properties.substring(0, sectionStart) + properties.substring(sectionEnd);
            else{
                sectionEnd =  sectionStart + 1;
                while (true) {
                    sectionEnd = properties.indexOf('|', sectionEnd);
                    int nextSeparatorIndex = properties.indexOf('|', sectionEnd+1);
                    if (nextSeparatorIndex == -1) {
                        properties = properties.substring(0, sectionStart);
                        break;
                    }
                    String str = properties.substring(sectionEnd+1, nextSeparatorIndex);
                    if (str.trim().length() == 0) {
                        if (nextSeparatorIndex < properties.length()-1)
                            nextSeparatorIndex++;
//                        System.out.println("nextSeparatorIndex: " + nextSeparatorIndex);
                        properties = properties.substring(0, sectionStart) + properties.substring(nextSeparatorIndex);
                        break;
                    }
                    sectionEnd++;
                }
            }
        }
        return properties;
    }

    protected File createContainerPropertiesFile() throws UnableToCreatePropertiesFileException {
        if (pathToContainerJar == null)
            throw new UnableToCreatePropertiesFileException("Unable to locate ESlateContainer<xxx>.jar file");

        File dir = new File(pathToContainerJar);
        if (!dir.exists() || !dir.isDirectory())
            throw new UnableToCreatePropertiesFileException("The path to ESlateContainer<xxx>.jar is not valid");

        File containerPropertiesFile = new File(dir, "container_EL.properties");
        try{
//\            BufferedWriter bw = new BufferedWriter(new FileWriter(containerPropertiesFile), 16);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(containerPropertiesFile), Charset.forName("UTF-8")), 1024);
//            bw.newLine();
            bw.close();
        }catch (IOException exc) {
            throw new UnableToCreatePropertiesFileException("Unable to create \"container.properties\": the file cannot be written");
        }

        return containerPropertiesFile;
    }

    protected File getContainerPropertiesFile() {
//        System.out.println("getContainerPropertiesFile() pathToContainerJar: " + pathToContainerJar);
        if (pathToContainerJar == null) {
            return null;
        }
        File dir = new File(pathToContainerJar);
        if (!dir.exists()) {
            return null;
        }
        File containerPropertiesFile = new File(dir, CONTAINER_PROPERTIES);
        if (!containerPropertiesFile.exists()) {
            return null;
        }
        return containerPropertiesFile;
    }

    private static String findPathToContainerJar() {
//        long millis = System.currentTimeMillis();
        String classpath = System.getProperty("java.class.path");
//        System.out.println("classpath: " + classpath + ", path separator: " + System.getProperty("path.separator").charAt(0));
        char pathSeparator = System.getProperty("path.separator").charAt(0);
        String containerJarName = "ESlateContainer_"; // + _version + ".jar";
        //if(EMBEDDED_MODE) {
        	//containerJarName = "widgetESlate";
        //}
//System.out.println("findPathToContainerJar() looking for : " + containerJarName);
//        System.out.println("classpath: " +  classpath.toLowerCase().indexOf(containerJarName.toLowerCase()));
        int lastIndex = classpath.toLowerCase().indexOf(containerJarName.toLowerCase());
        if (lastIndex != -1) {
            int firstIndex = lastIndex;
            while (firstIndex != -1 && classpath.charAt(firstIndex) != pathSeparator)
                firstIndex--;

//            long millis2 = System.currentTimeMillis();
//            System.out.println("findPathToContainerJar() duration: " + (millis2-millis));
            String containerJarClassPath = classpath.substring(firstIndex+1, lastIndex);

            if (!new File(containerJarClassPath).exists()) {
                containerJarClassPath = new File(System.getProperty("user.dir"), containerJarClassPath).getAbsolutePath();
                System.out.println("2. containerJarClassPath: " + containerJarClassPath);
            }else
                containerJarClassPath = new File(containerJarClassPath).getAbsolutePath();

//            addBinFolderToESlateNativeProgramFolders(containerJarClassPath);
            return containerJarClassPath;
        }else{
            /* Check if there exists a container.properties archieve in the extension directories
             * of the VM installation.
             */
            String extensionDir = System.getProperty("java.ext.dirs");
//            System.out.println("extensionDir: " + extensionDir);
            int pathSeparatorCount = 0;
            for (int i=0; i<extensionDir.length(); i++) {
                if (extensionDir.charAt(i) == pathSeparator)
                    pathSeparatorCount++;
            }
            pathSeparatorCount=pathSeparatorCount+1;
            String[] dirs = new String[pathSeparatorCount];
            int lastIndex2 = 0;
            for (int i=0; i<dirs.length; i++) {
                int index = extensionDir.indexOf(lastIndex2+1, pathSeparator);
                if (index == -1) index = extensionDir.length();
                dirs[i] = extensionDir.substring(lastIndex2, index);
                lastIndex2 = index;
            }
            for (int i=0; i<dirs.length; i++) {
                File f = new File(dirs[i], CONTAINER_PROPERTIES);
                if (f.exists()) {
//                    addBinFolderToESlateNativeProgramFolders(dirs[i]);
                    return dirs[i];
                }
            }

//            long millis2 = System.currentTimeMillis();
//            System.out.println("findPathToContainerJar() duration: " + (millis2-millis));
            return null;
        }
    }

    /**
     * Returns the path to the ESlateContainer<xxx>.jar
     */
    public static String getPathToContainerJar() {
        return pathToContainerJar;
    }

    public static File getBinFolder(String pathToContainerJar) {
        if (binFolder != null)
            return binFolder;
        if (pathToContainerJar == null)
            return null;
        File containerJarfolder = new File(pathToContainerJar);
//        containerJarfolder = containerJarfolder.getParentFile();
        containerJarfolder = containerJarfolder.getParentFile();
        binFolder = new File(containerJarfolder, "bin");
        if (!binFolder.exists())
            binFolder = null;
        return binFolder;
    }

    /* Bot used. The bin folder is by default added to the native programs folder in the
     * readContainerProperties() method.
     */
    private static void addBinFolderToESlateNativeProgramFolders(String pathToContainerJar) {
        if (binFolder == null)
            getBinFolder(pathToContainerJar);
        if (binFolder == null) return;
        File[] nativePrgFolders = ESlate.getNativeProgramFolders();
        File[] newPrgFolders = new File[nativePrgFolders.length+1];
        for (int i=0; i<nativePrgFolders.length; i++)
            newPrgFolders[i] = nativePrgFolders[i];
        newPrgFolders[newPrgFolders.length-1] = binFolder;
        ESlate.setNativeProgramFolders(newPrgFolders);
    }

    public File getTmpFile() {
        return tmpFile;
    }

    public String getESlateSoundThemePath() {
        if (pathToContainerJar == null)
            return "/home/vpapakir/workspace/ESlate-lite/demo/bugs01.wav";
        String soundThemeFile = pathToContainerJar + System.getProperty("file.separator") + SOUND_THEME_FILE_NAME;
        return soundThemeFile;
    }

    /* Attaches the "currentMicroworld" to the ComponentPrimitives' Group.
     * Registers "PrimitiveGroupAddedListener" to register new primitive
     * groups from freshly instantiated components to the logo machine.
     */
    public void startWatchingMicroworldForPrimitiveGroups(){
//System.out.println("startWatchingMicroworldForPrimitiveGroups() logoMachine.componentPrimitives: " + logoMachine.componentPrimitives);
        logoMachine.componentPrimitives.theESlateMicroworld=microworld.eslateMwd;

        primGrpListener = new PrimitiveGroupAddedListener() {
            public void primitiveGroupAdded(PrimitiveGroupAddedEvent e) { //25May1999
//                System.out.println("Logo: got PrimitiveGroupAddedEvent");
                //e.getSource() gives the handle that requested those prim groups
                loadPrimitiveGroups(e.getNames());

            }
        };
        microworld.eslateMwd.addPrimitiveGroupAddedListener(primGrpListener);

        /* Loading the primitives of the components which are already instantiated. */
        try{
            ESlateHandle[] v = microworld.eslateMwd.getHandles();
            for(int i=0; i<v.length; i++) {
                ESlateHandle handle = v[i];   /**/
//                System.out.println("Found a preexisting component named "+ handle.getComponentName());
                if (handle instanceof gr.cti.eslate.scripting.LogoScriptable)
                    loadPrimitiveGroups(((gr.cti.eslate.scripting.LogoScriptable)handle).getSupportedPrimitiveGroups());
            }
        }catch(Exception e){
            System.err.println(e+"\nCouldn't ask existing components for any Logo primitives they might implement");
        }
        try{ //27-3-1999: ask any ESlate-independent components registered for scripting, in case they need some PrimitiveGroups
            Object[] o=gr.cti.eslate.scripting.logo.ComponentPrimitives.scriptableObjects.getAllObjects();
            //26Jul1999: changed to use the INameService's i/f getAllObjects method: maybe should use one descendent
            //"Logo name service" that wraps both the ESlate naming and the global nameservice in it (an inner class of
            //Logo that can get the current microworld from Logo at any time)
            for(int i=0;i<o.length;i++) {
                Object a=o[i];
                //System.out.println("Found a preexisting scripting registered object named "+a.name);
                if (a instanceof gr.cti.eslate.scripting.LogoScriptable)
                    loadPrimitiveGroups(((gr.cti.eslate.scripting.LogoScriptable)a).getSupportedPrimitiveGroups());
            }
        }catch(Exception e){
            System.err.println(e+"\nCouldn't ask existing scriptable objects for any Logo primitives they might implement");
        }

    }

    public void loadPrimitiveGroups(String[] p){
//System.out.print("Loading primitive groups: ");
//        for (int i=0; i<p.length; i++)
//            System.out.print(p[i] + ", ");
//System.out.println();
        logoMachine.loadPrimitives(p);
    }

    private void initializeESlateConsoles() {
        java.io.OutputStream stream = new java.io.ByteArrayOutputStream(0);
        consoles = new ESlateConsoles(this, stream);
    }
    /* Redirect java output to the "javaOutputConsole"
     */
    public final void redirectOutputToJavaConsole() {
        if (microworld != null)
            microworld.checkActionPriviledge(microworld.consolesAllowed, "consolesAllowed");
        redirectOutputToJavaConsoleInternal();
    }

    final void redirectOutputToJavaConsoleInternal() {
        if (consoles == null)
            initializeESlateConsoles();

        consoles.setJavaConsoleEnabled(true);
//        consoles.setJavascriptConsoleEnabled(true);
        System.setOut(consoles);
        System.setErr(consoles);
    }

    /* Redirect java output to the standard output PrintStreams
     */
    public void useStandardJavaOutput() {
        if (consoles != null)
            consoles.setJavaConsoleEnabled(false);
//        consoles.setJavascriptConsoleEnabled(false);
        System.setOut(standardOut);
        System.setErr(standardErr);
    }

    public void evaluateLogoScript(String script) {
        if (logoMachine == null)
            initLogoEnvironment();
        try{
            LogoList l = tokenizer.tokenize(script);
//System.out.println("script: " + script);
            l.getRunnable(logoMachine).execute(logoEnvironment);
        }catch (LanguageException exc) {
             System.out.println("LanguageException: " + exc.getMessage());
            exc.printStackTrace();
             try{
                 logoThread.outStream().putLine(exc.getMessage());
             }catch (LanguageException excpt) {
                 System.out.println("LanguageException: " + excpt.getMessage());
             }
        }catch (virtuoso.logo.ThrowException exc) {
             System.out.println("ThrowException: " + exc.getMessage());
            exc.printStackTrace();
             try{
                 logoThread.outStream().putLine(exc.getMessage());
             }catch (LanguageException excpt) {
                 System.out.println("LanguageException: " + excpt.getMessage());
             }
        }
    }

    public void evaluateJSScript(String script) {
        if (!javascriptInUse)
            registerJavascriptVariables();

        try{
            consoles.setActiveOutputConsole(ESlateConsoles.JAVASCRIPT_CONSOLE);
            jsScriptManager.evaluateJavaScript(script);
            consoles.setActiveOutputConsole(ESlateConsoles.JAVA_CONSOLE);
        }catch (Exception exc) {
            /* Try to parse the error string, locate the line number which is reported and
             * correct it, so that it corresponds to the proper line number in the code editor.
             */
exc.printStackTrace();
            String errorMsg = exc.getMessage();
//            exc.printStackTrace();
            int errorLength = errorMsg.length();
            String searchForStr = "; line ";
            int index = errorMsg.indexOf(searchForStr);
            int lineNum = -1;
            if (index != -1) {
                //Get the line number and substract 22
                int start = index + searchForStr.length();
//System.out.println("start: " + start + ", errorMsg.charAt(): " + errorMsg.charAt(start));
                int end = start+1;
                while (end < errorLength && Character.isDigit(errorMsg.charAt(end)))
                    end++;
//System.out.println("end: " + end + ", errorLength: " + errorLength);
                if (end < errorLength) {
//                    System.out.println("errorMsg.substring(start, end): " + errorMsg.substring(start, end));
                    try{
                        lineNum = new Integer(errorMsg.substring(start, end)).intValue();
//                    System.out.println("lineNum: " + lineNum);
                        int actualLineNum = lineNum - 21;
                        StringBuffer buff = new StringBuffer(errorMsg);
                        buff.replace(start, end, new Integer(actualLineNum).toString());
                        errorMsg = buff.toString();
//                        if (actualLineNum <= 0)
//                            System.out.println("Error while declaring the variables of the processed event");
                    }catch (Throwable thr) {}
                }
            }
            System.out.println(errorMsg);
            if (lineNum != -1)
                System.out.println("Error line --> " + getLine(script, lineNum));
            consoles.setActiveOutputConsole(ESlateConsoles.JAVA_CONSOLE);
        }
    }

    private String getLine(String script, int line) {
        if (line <= 0)
            return "Invalid line number " + line;
        int lineCount = 0;
        int index = 0;
        int scriptLength = script.length();
        while (index < scriptLength && lineCount < line) {
            while (script.charAt(index) != '\n') index++;
            lineCount++;
            index++;
        }
        if (index >= scriptLength)
            return "Invalid line number " + line + ". The script has only " + lineCount + " lines.";
        else{
            int startIndex = index;
            while (index < scriptLength && script.charAt(index) != '\n') index++;
            return script.substring(startIndex, index);
        }
    }

    /** Caches the cursor each component with UI (those hosted in ESlateInternaFrames)
     *  in the microworld is currently using.
     */
    public ComponentCursors cacheComponentCursors() {
        ComponentCursors cursorCache = new ComponentCursors();
        cursorCache.storeCursors(this);
        return cursorCache;
/*        for (int i=0; i<mwdComponents.size(); i++) {
            ESlateComponent comp = mwdComponents.components.get(i);
            if (comp.frame != null) {
                comp.cursorToRestore = ((Component) comp.handle.getComponent()).getCursor();
            }
        }
*/
    }

    /** Clears the cursor cache for the components with UI (those hosted in ESlateInternaFrames)
     *  of the microworld.
     */
/*    public void clearComponentCursorCache() {
        for (int i=0; i<mwdComponents.size(); i++) {
            ESlateComponent comp = mwdComponents.components.get(i);
            comp.cursorToRestore = null;
        }
    }
*/
    /** This method sets the cursor to all the UI elements of E-Slate to 'eslateCursor'. However
     *  if the components' cursors have been cached (using cacheCursors()), then these cached
     *  cursors are restored to the visible components.
     */
    public void restoreComponentCursors(ComponentCursors cursorCache, Cursor eslateCursor) {
        super.setCursor(eslateCursor);
        lc.setCursor(eslateCursor);
        cursorCache.restoreCursors(this, eslateCursor);
/*        for (int i=0; i<mwdComponents.size(); i++) {
            ESlateComponent comp = mwdComponents.components.get(i);
            if (comp.frame != null) {
System.out.println("Component " + comp.getName() + ", eslateCursor: " + eslateCursor);
                comp.frame.setCursor(eslateCursor);
			}
*/
/*                if (comp.cursorToRestore != null)
                    ((Component) comp.handle.getComponent()).setCursor(comp.cursorToRestore);
                else
                    ((Component) comp.handle.getComponent()).setCursor(eslateCursor);
            }
*/
/*            if (comp.icon != null)
                comp.icon.setCursor(eslateCursor);
        }
*/
    }

    public void setCursor(Cursor cursor) {
        setCursor(cursor, true);
    }

    /** Changes the cursor to all the UI elements of E-Slate. if 'changeComponentCursors'
     *  is true, then the new cursor will also be assigned to the visible components of
     *  the current microworld. Otherwise, the components keep their cursors.
     */
    protected void setCursor(Cursor cursor, boolean changeComponentCursors) {
        if (loadingMwd) return;
        super.setCursor(cursor);
//Thread.currentThread().dumpStack();
        lc.setCursor(cursor);
        for (int i=0; i<mwdComponents.size(); i++) {
            ESlateComponent comp = mwdComponents.components.get(i);
            if (comp.frame != null) {
                comp.frame.setCursor(cursor);
                if (changeComponentCursors)
                    ((Component) comp.handle.getComponent()).setCursor(cursor);
            }
            if (comp.icon != null)
                comp.icon.setCursor(cursor);
        }
    }

    public Cursor getCursor() {
        return super.getCursor();
    }

    public ESlateComponent getComponent(Object component) {
        return mwdComponents.getComponent(component);
    }

    public ESlateComponent getComponent(String compoName) {
        return mwdComponents.getComponent(compoName);
    }

    public ESlateInternalFrame getComponentFrame1(Object component) {
        return mwdComponents.getComponentFrame(component);
/*0        int index = components.indexOf(component);
        if (index == -1)
            return null;
        String compoName = ((ESlateHandle) eSlateHandles.at(index)).getComponentName();
        for (int i=0; i<componentFrames.size(); i++) {
            if (((ESlateInternalFrame) componentFrames.at(i)).getTitle().equals(compoName))
                return (ESlateInternalFrame) componentFrames.at(i);
        }
        return null;
*/
    }

    public ESlateInternalFrame getComponentFrame1(String compoName) {
//        String compoName = ((ESlateHandle) eSlateHandles.at(index)).getComponentName();
        return mwdComponents.getComponentFrame(compoName);
/*0        for (int i=0; i<componentFrames.size(); i++) {
            if (((ESlateInternalFrame) componentFrames.at(i)).getTitle().equals(compoName))
                return (ESlateInternalFrame) componentFrames.at(i);
        }
        return null;
*/
    }

    public Vector getComponentFrames(int layerLevel) {
        Vector v = new Vector();
//0        for (int i=0; i<componentFrames.size(); i++) {
//0            if (((ESlateInternalFrame) componentFrames.at(i)).getLayer() == layerLevel)
        for (int i=0; i<mwdComponents.size(); i++) {
            ESlateInternalFrame fr = mwdComponents.components.get(i).frame;
            if (fr != null && fr.getLayer() == layerLevel)
                v.addElement(fr);
        }
        return v;
    }

    public Point getDesktopItemLocation(Object component) {
//1        JInternalFrame fr = getComponentFrame(component);
        ESlateComponent ecomponent = getComponent(component);
        if (ecomponent != null)
            return ecomponent.desktopItem.getDesktopItemLocation();
        else
            return null;
    }

    public void setDesktopItemLocation(Object component, Point newLocation) {
//        System.out.println("Component: " + component.getClass());
//        JInternalFrame fr = getComponentFrame(component);
        ESlateComponent ecomponent = getComponent(component);
        if (ecomponent != null) {
            ecomponent.desktopItem.setDesktopItemLocation(newLocation);
        }else
            System.out.println("Can't find component");
    }

    public void setDesktopItemSize(String compoName, Dimension size) {
//1        ESlateInternalFrame fr = getComponentFrame(compoName);
        ESlateComponent ecomponent = getComponent(compoName);
        if (ecomponent != null)
            ecomponent.desktopItem.setDesktopItemSize(size);
    }

    public void setDesktopItemSize(Object compo, Dimension size) {
//1        ESlateInternalFrame fr = getComponentFrame(compo);
        ESlateComponent ecomponent = getComponent(compo);
        if (ecomponent != null)
            ecomponent.desktopItem.setDesktopItemSize(size);
    }

    public Dimension getDesktopItemSize(String compoName) {
        ESlateComponent ecomponent = getComponent(compoName);
        if (ecomponent != null)
            return ecomponent.desktopItem.getDesktopItemSize();
/*1        if (currentMicroworld != null) {
            Object obj = currentMicroworld.getComponent(compoName);
            if (obj != null && Component.class.isAssignableFrom(obj.getClass()))
                return ((Component) obj).getSize();
        }
*/
        return null;
    }

    public Dimension getDesktopItemSize(Object component) {
        ESlateComponent ecomponent = getComponent(component);
        if (ecomponent != null)
            ecomponent.desktopItem.getDesktopItemSize();
/*1        if (currentMicroworld != null) {
            if (obj != null && Component.class.isAssignableFrom(obj.getClass()))
                return ((Component) obj).getSize();
        }
*/
        return null;
    }

    public void setDesktopItemBounds(String compoName, Rectangle bounds) {
//1        ESlateInternalFrame fr = getComponentFrame(compoName);
        ESlateComponent ecomponent = getComponent(compoName);
        if (ecomponent != null)
            ecomponent.desktopItem.setDesktopItemBounds(bounds);
    }

    public void setDesktopItemBounds(Object component, Rectangle bounds) {
//1        ESlateInternalFrame fr = getComponentFrame(obj);
        ESlateComponent ecomponent = getComponent(component);
        if (ecomponent != null)
            ecomponent.desktopItem.setDesktopItemBounds(bounds);
    }

    public Rectangle getDesktopItemBounds(String compoName) {
        ESlateComponent ecomponent = getComponent(compoName);
        if (ecomponent != null)
            return ecomponent.desktopItem.getDesktopItemBounds();
/*1        if (currentMicroworld != null) {
            Object obj = currentMicroworld.getComponent(compoName);
            if (obj != null && Component.class.isAssignableFrom(obj.getClass()))
                return ((Component) obj).getBounds();
        }
*/
        return null;
    }

    public Rectangle getDesktopItemBounds(Object component) {
        ESlateComponent ecomponent = getComponent(component);
        if (ecomponent != null)
            return ecomponent.desktopItem.getDesktopItemBounds();
/*1        if (currentMicroworld != null) {
            if (obj != null && Component.class.isAssignableFrom(obj.getClass()))
                return ((Component) obj).getBounds();
        }
*/
        return null;
    }

////nikosM
    public void back() {
////nikosM currentMicroworld change
        if (mwdHistory.canGoBack((microworld.eslateMwd != null))) {
////nikosM currentMicroworld change end
            updatingHistoryFlag=true;
////nikosM currentMicroworld change
            mwdHistory.back((microworld.eslateMwd != null));
////nikosM currentMicroworld change end
            String historyMicroworld=mwdHistory.getCurrentMicroworld();
//            System.out.println("historyMicroworld "+historyMicroworld);
            if (mwdHistory.isCurrentMicroworldRemote()) {
                   String webFile, webServer;
                   int c=historyMicroworld.indexOf(container.containerBundle.getString("OnServer"));
//                   System.out.println(c+" "+fileName);
                   webFile=historyMicroworld.substring(0,c);
                   String serverName=historyMicroworld.substring(c+container.containerBundle.getString("OnServer").length(),historyMicroworld.length());
                   System.out.println(serverName);
                   webServer=(String) container.webSites.get(serverName);
                   //System.out.println("__WebServer ="+webServer);
                   if (!container.webServerMicrosHandle.openRemoteMicroWorld(webServer,webFile,false)) {
                       ESlateOptionPane.showMessageDialog(null, container.containerBundle.getString("ContainerMsg12"), container.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//                       mwdHistory.forward();
                   }
            }
            else
                   loadLocalMicroworld(mwdHistory.getCurrentMicroworld(), false, true);
//                      mwdHistory.forward();
////if the loadLocalMicroworld returns false we have to refresh the HistoryList
////as it might has changed (e.g. if currentlyOpenMwdFileName.equals(mwdFileName)==true;
            containerUtils.fireMwdHistoryChanged(new MwdHistoryChangedEvent(this));
            updatingHistoryFlag=false;
        }
    }
////nikosM end
////nikosM
    public void forward() {
        if (mwdHistory.canGoForward()) {
            updatingHistoryFlag=true;
////nikosM currentMicroworld change
            if (microworld.eslateMwd != null)
////nikosM currentMicroworld change end
                mwdHistory.forward();
            String historyMicroworld=mwdHistory.getCurrentMicroworld();
            if (mwdHistory.isCurrentMicroworldRemote()) {
                   String webFile, webServer;
                   int c=historyMicroworld.indexOf(container.containerBundle.getString("OnServer"));
//                   System.out.println(c+" "+fileName);
                   webFile=historyMicroworld.substring(0,c);
                   String serverName=historyMicroworld.substring(c+container.containerBundle.getString("OnServer").length(),historyMicroworld.length());
                   System.out.println(serverName);
                   webServer=(String) container.webSites.get(serverName);
//                   System.out.println("____WebServer ="+webServer);
                   if (!container.webServerMicrosHandle.openRemoteMicroWorld(webServer,webFile,false)) {
                       ESlateOptionPane.showMessageDialog(null, container.containerBundle.getString("ContainerMsg12"), container.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//                       mwdHistory.back(true);
                  }
            }
            else
                loadLocalMicroworld(mwdHistory.getCurrentMicroworld(), false, true);
//                   mwdHistory.back(true);
////if the loadLocalMicroworld returns false we have to refresh the HistoryList
////as it might has changed (e.g. if currentlyOpenMwdFileName.equals(mwdFileName)==true;
            containerUtils.fireMwdHistoryChanged(new MwdHistoryChangedEvent(this));
            updatingHistoryFlag=false;
        }
    }
////nikosM end

    public synchronized void addContainerListener(ESlateContainerListener dl) {
        if (containerListeners.indexOf(dl) == -1)
            containerListeners.add(dl);
    }


    public synchronized void removeContainerListener(ESlateContainerListener dl) {
        if (containerListeners.indexOf(dl) != -1)
            containerListeners.remove(dl);
    }

    private MouseInputAdapter createDesktopDragMouseListener() {
        MouseInputAdapter ml = new MouseInputAdapter() {
            final Point INITIAL_POINT = new Point(-1, -1);
            Point prevPos = INITIAL_POINT;
            Cursor openHandCursor, closedHandCursor;

            public void mousePressed(MouseEvent e) {
                DesktopPane pane = (DesktopPane) e.getSource();
                if (pane.isModalFrameVisible()) return;
                prevPos = e.getPoint();
                SwingUtilities.convertPointToScreen(prevPos, lc);
                if (currentView != null && currentView.desktopDraggable) {
                    if (openHandCursor == null)
                        openHandCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                                            Toolkit.getDefaultToolkit().createImage(getClass().getResource("images/pancursorhand.gif")),
                                            new Point(15, 9),
                                            "Grab cursor");
                    getTopLevelAncestor().setCursor(openHandCursor); //Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }
            }
            public void mouseReleased(MouseEvent e) {
                DesktopPane pane = (DesktopPane) e.getSource();
                if (pane.isModalFrameVisible()) return;
                prevPos = INITIAL_POINT;
                if (currentView != null && currentView.desktopDraggable)
                    getTopLevelAncestor().setCursor(Cursor.getDefaultCursor());
            }
            public void mouseDragged(MouseEvent e) {
                DesktopPane pane = (DesktopPane) e.getSource();
                if (pane.isModalFrameVisible()) return;
                if (currentView == null || !currentView.desktopDraggable) return;

                if (closedHandCursor == null) {
                    closedHandCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                                        Toolkit.getDefaultToolkit().createImage(getClass().getResource("images/grabhand.gif")),
                                        new Point(15, 9),
                                        "Drag cursor");
                }
                if (getTopLevelAncestor().getCursor() != closedHandCursor)
                    getTopLevelAncestor().setCursor(closedHandCursor);

                Point currPoint = e.getPoint();
                SwingUtilities.convertPointToScreen(currPoint, lc);
                int deltaX = currPoint.x - prevPos.x;
                int deltaY = currPoint.y - prevPos.y;
                prevPos = currPoint;

                Point viewPos = scrollPane.getViewport().getViewPosition();
                viewPos.x = viewPos.x - deltaX;
                viewPos.y = viewPos.y - deltaY;
                if (viewPos.x < 0) viewPos.x = 0;
                if (viewPos.y < 0) viewPos.y = 0;
                if (viewPos.x > lc.getSize().width - scrollPane.getViewport().getExtentSize().width)
                    viewPos.x = lc.getSize().width - scrollPane.getViewport().getExtentSize().width;
                if (viewPos.y > lc.getSize().height - scrollPane.getViewport().getExtentSize().height)
                    viewPos.y = lc.getSize().height - scrollPane.getViewport().getExtentSize().height;
//                System.out.println("viewPos: " + viewPos);
                scrollPane.getViewport().setViewPosition(viewPos);
            }
        };
        return ml;
    }

    public void showActiveComponentHelp() {
        if (mwdComponents.activeComponent == null)
            return;
        mwdComponents.activeComponent.handle.showHelpWindow();
    }

    public void showActiveComponentInfo() {
        if (mwdComponents.activeComponent == null)
            return;
        mwdComponents.activeComponent.handle.showInfoDialog();
    }


/*    public void showMicroworldPropertiesDialog2() {
          if (microworld == null) return;
          MicroworldSettingsProfile profile = new MicroworldSettingsProfile(this);
          MwdPropertiesDialog2 mwdPropertiesDialog = new MwdPropertiesDialog2(parentFrame, this, profile);
          mwdPropertiesDialog.setMicroworldTitle(microworld.getTitle());
          mwdPropertiesDialog.setMicroworldSubject(microworld.getSubject());
          mwdPropertiesDialog.setMicroworldCompany(microworld.getCompany());
          mwdPropertiesDialog.setMicroworldAuthors(microworld.getAuthors());
          mwdPropertiesDialog.setMicroworldkeywords(microworld.getKeywords());
          mwdPropertiesDialog.setMicroworldComments(microworld.getComments());
          mwdPropertiesDialog.setMicroworldCategory(microworld.getCategoryName());
          mwdPropertiesDialog.setPassword(microworld.getSubTitle());
          boolean noMwdPassword = (microworld.getSubTitle() == null || microworld.getSubTitle().trim().length() == 0);
*/          /* The microworld settings profile are not read until the first time
           * the ProfileDialog is displayed from the MwdPropertiesDialog. All this time
           * the 'mwdSettingsProfiles' is null and until the custom profiles are read
           * and this variable holds these profiles, no profiles are passed to the
           * MwdPropertiesDialog.
           */
/*          if (mwdSettingsProfiles != null)
              mwdPropertiesDialog.setCustomProfiles(mwdSettingsProfiles);
          if (microworld.isLocked())
              if (!mwdPropertiesDialog.metadataPanel.unlockMicroworld())
                  return;
          if (!microworld.isLocked())
              mwdPropertiesDialog.showDialog(ESlateContainer.this);
          else
              return;
//          modalDialog = null;

          if (mwdPropertiesDialog.getReturnCode() == MwdPropertiesDialog2.DIALOG_OK) {
//              adjustMicroworldProperties(mwdPropertiesDialog.isMinimizeAllowed(),
//              System.out.println("mwdPropertiesDialog.isMinimizeAllowed(): " + mwdPropertiesDialog.isMinimizeAllowed());
//              System.out.println("mwdPropertiesDialog.isHelpControlEnabled(): " + mwdPropertiesDialog.isHelpControlEnabled());
*/
              /* If the microworld was just locked for the first time or its password
               * changed, then unlock it temporarily to set the properties and then lock it back.
               */
/*              boolean unlockedTemporarily = false;
              if ((noMwdPassword || !microworld.getSubTitle().equals(mwdPropertiesDialog.getPassword()))
              && microworld.isLocked()) {
                  microworld.setLocked(false);
                  unlockedTemporarily = true;
              }
              setMicroworldProperties(mwdPropertiesDialog);
              mwdSettingsProfiles = mwdPropertiesDialog.getCustomProfiles();
              updateMicroworldProfilesSection();
              if (unlockedTemporarily)
                  microworld.setLocked(true);
          }
    }

    void setMicroworldProperties(MwdPropertiesDialog2 mwdPropertiesDialog) {
        MicroworldSettingsProfile profile = mwdPropertiesDialog.getProfile();

        // COMPONENT BAR PROPERTIES
        setMinimizeButtonVisible(profile.minimizeButtonVisible);
        setMaximizeButtonVisible(profile.maximizeButtonVisible);
        setCloseButtonVisible(profile.closeButtonVisible);
        setControlBarTitleActive(profile.controlBarTitleActive);
        setHelpButtonVisible(profile.helpButtonVisible);
        setInfoButtonVisible(profile.infoButtonVisible);
        setPinButtonVisible(profile.plugButtonVisible);
        setControlBarsVisible(profile.controlBarsVisible);

        // COMPONENT PROPERTIES
        setResizeAllowed(profile.resizeAllowed);
        setMoveAllowed(profile.moveAllowed);
        setComponentActivationMethodChangeAllowed(profile.componentActivationMethodChangeAllowed);
        setActiveComponentHighlighted(profile.activeComponentHighlighted);
        setEnsureActiveComponentAlwaysVisible(profile.ensureActiveComponentVisible);
        setComponentMinimizeAllowed(profile.componentMinimizeAllowed);
        setComponentMaximizeAllowed(profile.componentMaximizeAllowed);

        // MICROWORLD PROPERTIES
        microworld.setComponentInstantiationAllowed(profile.componentInstantiationAllowed);
        microworld.setComponentRemovalAllowed(profile.componentRemovalAllowed);
        microworld.setComponentNameChangeAllowed(profile.componentNameChangeAllowed);
        setMwdBgrdChangeAllowed(profile.mwdBgrdChangeAllowed);
        microworld.setMwdStorageAllowed(profile.mwdStorageAllowed);
        setPlugConnectionChangeAllowed(profile.plugConnectionChangeAllowed);
        setMicroworldNameUserDefined(profile.microworldNameUserDefined);
        microworld.setMwdPopupEnabled(profile.mwdPopupEnabled);
        setMenuBarVisible(profile.menuBarVisible);
//        setOutlineDragEnabled(mwdPropertiesDialog.isOutlineDragEnabled());
        microworld.setESlateComponentBarEnabled(profile.eslateComponentBarEnabled);
//              setComponentFrozenStateChangeAllowed(mwdPropertiesDialog.isComponentFrozenStateChangeAllowed());
        setMwdResizable(profile.mwdResizable);
        setMicroworldSize(new Dimension(mwdPropertiesDialog.getMicroworldWidth(), mwdPropertiesDialog.getMicroworldHeight()));
        setMwdAutoScrollable(profile.mwdAutoScrollable);
        setComponentsMoveBeyondMwdBounds(profile.componentsMoveBeyondMwdBounds);
        setMwdAutoExpandable(profile.mwdAutoExpandable);
        setDesktopDraggable(profile.desktopDraggable);
        setMenuSystemHeavyWeight(profile.menuSystemHeavyWeight);
        microworld.setStoreSkinsPerView(profile.storeSkinOnAPerViewBasis);
        setScrollBarPolicies(profile.verticalScrollbarPolicy, profile.horizontalScrollbarPolicy);
        microworld.setTitle(mwdPropertiesDialog.getMicroworldTitle());
        microworld.setSubject(mwdPropertiesDialog.getMicroworldSubject());
        microworld.setCompany(mwdPropertiesDialog.getMicroworldCompany());
        microworld.setAuthors(mwdPropertiesDialog.getMicroworldAuthors());
        microworld.setKeywords(mwdPropertiesDialog.getMicroworldkeywords());
        microworld.setComments(mwdPropertiesDialog.getMicroworldComments());
        microworld.setCategoryName(mwdPropertiesDialog.getMicroworldCategory());
        microworld.setSubTitle(mwdPropertiesDialog.getPassword());
    }
*/
    public void setScrollBarPolicies(int vsbPolicy, int hsbPolicy) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();

        boolean scrollPaneNeedsValidation = false;
        if (scrollPane.getVerticalScrollBarPolicy() != vsbPolicy) {
            scrollPane.setVerticalScrollBarPolicy(vsbPolicy);
System.out.println("setScrollBarPolicies verticalScrollbarPolicy " + vsbPolicy);
            currentView.verticalScrollbarPolicy = vsbPolicy;
            setMicroworldChanged(true);
            scrollPaneNeedsValidation = true;
        }
        if (scrollPane.getHorizontalScrollBarPolicy() != hsbPolicy) {
            scrollPane.setHorizontalScrollBarPolicy(hsbPolicy);
            currentView.horizontalScrollbarPolicy = hsbPolicy;
            setMicroworldChanged(true);
            scrollPaneNeedsValidation = true;
        }
        if (scrollPaneNeedsValidation)
            scrollPane.revalidate();
    }


/*
    public final void toggleComponentPanel() {
        if (microworld != null)
            microworld.checkActionPriviledge(microworld.eslateComponentBarEnabled, "eslateComponentBarEnabled");
        toggleComponentPanelInternal();
    }
*/

/*
    final void toggleComponentPanelInternal() {
//        System.out.println("toggleComponentPanel()");
        if (componentBar == null)
            componentBar = new ComponentPanel(ESlateContainer.this);
//        else
//            componentBar.synchronizeComponentPanel();

        if (componentBar.getParent() == null) {
            componentBar.synchronizeComponentPanel();
            northPanel.add(componentBar);
            northPanel.add(Box.createVerticalStrut(2));
        }else{
            northPanel.remove(componentBar);
            northPanel.remove(northPanel.getComponentCount()-1);
        }
        if (menuPanel == null) {
            if (componentBar.getParent() == null)
                menuPanel.compoMenuLabel.setIcon(new ImageIcon(getClass().getResource("images/down.gif")));
            else
                menuPanel.compoMenuLabel.setIcon(new ImageIcon(getClass().getResource("images/up.gif")));
        }
        northPanel.revalidate();
        scrollPane.revalidate();
    }
*/


/*0    public ESlateInternalFrame getFrame(String componentName) {

        for (int i=0; i<componentFrames.size(); i++) {

            if (((ESlateInternalFrame) componentFrames.at(i)).getTitle().equals(componentName))

                return (ESlateInternalFrame) componentFrames.at(i);

        }

//        System.out.println("Serious inconsistency error in ESlateContainer getFrame() : (1)");

        return null;

    }

*/

    public boolean activateComponent(String title) {
        ESlateComponent component = mwdComponents.getComponent(title);
        if (component != null) {
//            try{
                if (setActiveComponent(component, true))
//                fr.setSelected(true);
                    return true;
                else
//            }catch (PropertyVetoException exc) {
                    return false;
//            }
        }
        return false;
    }

    public boolean setActiveComponent(ESlateComponent component, boolean active) {
//        System.out.println("setActiveComponent() component: " + component + ", active: " + active + " --- component.desktopItem.isActive(): " + component.desktopItem.isActive());
//        Thread.currentThread().dumpStack();
//System.out.println("setActiveComponent() : " + component + " active: " + active + " + desktopItem.isActive(): " + component.desktopItem.isActive());
        DesktopItem desktopItem = component.desktopItem;
        if (active && desktopItem.isActive())
            return true;
        if (!active && !desktopItem.isActive())
            return true;

        try{
//            System.out.println("setFrameSelected " + frame.getTitle() + ": " + selected);
            if (active) {
                /* Illegal to select an invisible frame.
                 */
//                System.out.println("setActiveComponent  desktopItem.isIcon(): " + desktopItem.isIcon());
                if (desktopItem.isIcon())
                    return false;
                desktopItem.setActive(true);
            }else{
                if (!active) {
                    ESlateComponent comp = compSelectionHistory.getFirstNotIconified(component);
//                    System.out.println("setActiveComponent getFirstNotIconified(): " + comp);
                    if (comp != null) {
//                        System.out.println("Selecting: " + fr.getTitle());
                        comp.desktopItem.setActive(true);
                    }else{
//                        System.out.println("Selecting EMPTY_FRAME");
                        desktopItem.setActive(false);
//                        EMPTY_FRAME.setSelected(true);
//1                        compSelectionHistory.setFirst(EMPTY_FRAME);
//0                        activeFrame = null;
                        mwdComponents.activeComponent = null;
                        microworld.eslateMwd.setSelectedComponent(null);
//                        System.out.println("setSelectedComponent 1");
//                        System.out.println("2. firing ActiveComponentChangedEvent");
                        containerUtils.fireActiveComponentChanged(new ActiveComponentChangedEvent(ESlateContainer.this));
                    }
                }
            }
            setMicroworldChanged(true);
            return true;
        }catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Exception " + exc.getClass() + " in ESlateContainer setFrameSelected()");
            return false;
        }
    }


    protected void initializeActions() {
        String actionString = containerBundle.getString("MicroworldClose");
        microworldCloseAction = new MicroworldCloseAction(this, actionString);
        actionString = containerBundle.getString("MicroworldLoadLocal");
        microworldLoadAction = new MicroworldLoadAction(this, actionString);
        actionString = containerBundle.getString("MicroworldNew");
        microworldNewAction = new MicroworldNewAction(this, actionString);
        actionString = containerBundle.getString("MicroworldNewView");
        microworldNewViewAction = new MicroworldNewViewAction(this, actionString);
        actionString = containerBundle.getString("PrintPrinter");
        microworldPrintAction = new MicroworldPrintAction(this, actionString);
//\        actionString = containerBundle.getString("MicroworldProperties");
//\        microworldPropertiesAction = new MicroworldPropertiesAction(this, actionString);
        actionString = containerBundle.getString("MicroworldSave");
        microworldSaveAction = new MicroworldSaveAction(this, actionString);
        actionString = containerBundle.getString("MicroworldSaveAsLocal");
        microworldSaveAsAction = new MicroworldSaveAsAction(this, actionString);
        actionString = containerBundle.getString("MicroworldForward");
        microworldForwardAction = new MicroworldForwardAction(this, actionString);
        actionString = containerBundle.getString("MicroworldBack");
        microworldBackAction = new MicroworldBackAction(this, actionString);
        actionString = containerBundle.getString("MicroworldPack");
        microworldPackAction = new MicroworldPackAction(this, actionString);

        // Component actions
        actionString = containerBundle.getString("ComponentCut");
        componentCutAction = new ComponentCutAction(this, actionString);
        actionString = containerBundle.getString("ComponentCopy");
        componentCopyAction = new ComponentCopyAction(this, actionString);
        actionString = containerBundle.getString("ComponentPaste");
        componentPasteAction = new ComponentPasteAction(this, actionString);
        actionString = containerBundle.getString("ComponentRename");
        componentRenameAction = new ComponentRenameAction(this, actionString);
        actionString = containerBundle.getString("ComponentRemove");
        componentRemoveAction = new ComponentRemoveAction(this, actionString);
        actionString = containerBundle.getString("ComponentResizable");
        componentResizableAction = new ComponentResizableAction(this, actionString);
        actionString = containerBundle.getString("ComponentActivatableByMousePress");
        componentActivateOnClickAction = new ComponentActivateOnClickAction(this, actionString);
		actionString = containerBundle.getString("ComponentSettings");
		componentSettingsAction = new ComponentSettingsAction(this, actionString);


//\        actionString = containerBundle.getString("PreferencesContainer");
//\        containerSettingsAction = new ContainerSettingsAction(this, actionString);
//\        actionString = containerBundle.getString("PreferencesGrid");
//\        gridEditorAction = new GridEditorAction(this, actionString);
        actionString = containerBundle.getString("MicroworldHelpCreate");
        microworldHelpEditorAction = new MicroworldHelpEditorAction(this, actionString);
        actionString = containerBundle.getString("PreferencesConsoles");
        openConsolesAction = new OpenConsolesAction(this, actionString);
        actionString = containerBundle.getString("PinView");
        plugEditorAction = new PlugEditorAction(this, actionString);
        actionString = containerBundle.getString("ShowComponentBarMI");
        showComponentBarAction = new ShowComponentBarAction(this, actionString);
//\        actionString = containerBundle.getString("PerformanceMonitorEdit");
//\        pmEditorAction = new PMEditorAction(this, actionString);
//\        actionString = "Script Editor";
//\        scriptEditorAction = new ScriptEditorAction(this, actionString);
//\        actionString = containerBundle.getString("ViewEditor");
//\        viewEditorAction = new ViewEditorAction(this, actionString);

        // Help actions
        actionString = containerBundle.getString("ESlateHelp");
        containerHelpAction = new ContainerHelpAction(this, actionString);
        actionString = containerBundle.getString("ComponentHelp");
        componentHelpAction = new ComponentHelpAction(this, actionString);
        actionString = containerBundle.getString("ComponentAbout");
        componentInfoAction = new ComponentInfoAction(this, actionString);
    }

    protected void registerContainerActions() {
        ActionMap containerActionMap = getActionMap();
        InputMap containerInputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Create a new microworld
        String actionName = (String) microworldNewAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, microworldNewAction);
        containerInputMap.put((KeyStroke) microworldNewAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Load a local microworld file
        actionName = (String) microworldLoadAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, microworldLoadAction);
        containerInputMap.put((KeyStroke) microworldLoadAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Save the microworld
        actionName = (String) microworldSaveAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, microworldSaveAction);
        containerInputMap.put((KeyStroke) microworldSaveAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );
        
        // Save microworld as
        actionName = (String) microworldSaveAsAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, microworldSaveAsAction);
        containerInputMap.put((KeyStroke) microworldSaveAsAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Close the microworld
        actionName = (String) microworldCloseAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, microworldCloseAction);
        containerInputMap.put((KeyStroke) microworldCloseAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Microworld settings
/*\
        actionName = (String) microworldPropertiesAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, microworldPropertiesAction);
        containerInputMap.put((KeyStroke) microworldPropertiesAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );
*/
        // Create a new view
        actionName = (String) microworldNewViewAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, microworldNewViewAction);
        containerInputMap.put((KeyStroke) microworldNewViewAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Print the microworld
        actionName = (String) microworldPrintAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, microworldPrintAction);
        containerInputMap.put((KeyStroke) microworldPrintAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Open the plug editor
        actionName = (String) plugEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, plugEditorAction);
        containerInputMap.put((KeyStroke) plugEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

/*\
        // Open the script editor
        actionName = (String) scriptEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, scriptEditorAction);
        containerInputMap.put((KeyStroke) scriptEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Open the view editor
        actionName = (String) viewEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, viewEditorAction);
        containerInputMap.put((KeyStroke) viewEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Open the E-Slate settings dialog
        actionName = (String) containerSettingsAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, containerSettingsAction);
        containerInputMap.put((KeyStroke) containerSettingsAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Open the grid dialog
        actionName = (String) gridEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, gridEditorAction);
        containerInputMap.put((KeyStroke) gridEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Open the performance manager UI
        actionName = (String) pmEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, pmEditorAction);
        containerInputMap.put((KeyStroke) pmEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );
*/
        // Open the microworld help editor
        actionName = (String) microworldHelpEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, microworldHelpEditorAction);
        containerInputMap.put((KeyStroke) microworldHelpEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );


        // Open the Java console
        actionName = (String) openConsolesAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, openConsolesAction);
        containerInputMap.put((KeyStroke) openConsolesAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );
        
        // Open remote mw
        actionName="LoadRemoteMicroworldAction";
        AbstractAction lrmw=new AbstractAction(actionName) {
			public void actionPerformed(ActionEvent arg0) {
				openLoadRemoteMicroworldDialog();
			}
        };
        containerActionMap.put(actionName,lrmw);
        containerInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R,KeyEvent.CTRL_DOWN_MASK),actionName);

        // Open the Logo dialog
/*        lc.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (loc != null)
                    consoles.showConsole(ESlateContainer.this);
//                    loc.showConsole(ESlateContainer.this); //ESlateContainer.this);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK), WHEN_IN_FOCUSED_WINDOW);
*/
        // E-Slate help
        actionName = (String) containerHelpAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, containerHelpAction);
        containerInputMap.put((KeyStroke) containerHelpAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );
/*        lc.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHelp();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), WHEN_IN_FOCUSED_WINDOW);
*/
/*\
        // View navigation accelerator keys
        registerKeyboardAction(new ViewNavigationListener(this, 1),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 2),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 3),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 4),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 5),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 6),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 7),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_7, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 8),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_8, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 9),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_9, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 0),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
*/

        // Cuts the active component
        actionName = (String) componentCutAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, componentCutAction);
        containerInputMap.put((KeyStroke) componentCutAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Copy the active component
        actionName = (String) componentCopyAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, componentCopyAction);
        containerInputMap.put((KeyStroke) componentCopyAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Paste a component from the clipboard (if exists) to the E-Slate desktop
        actionName = (String) componentPasteAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, componentPasteAction);
        containerInputMap.put((KeyStroke) componentPasteAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

		// Shows the component settings dialog
		actionName = (String) componentSettingsAction.getValue(AbstractAction.NAME);
		containerActionMap.put(actionName, componentSettingsAction);
		containerInputMap.put((KeyStroke) componentSettingsAction.getValue(AbstractAction.ACCELERATOR_KEY),
							  actionName
		);
    }

    public String[] getMwdFileExtensions() {
        return mwdFileExtensions;
    }


    public String getMicroworldDirectory() {
        if (microworld == null)
            return null;
        if (currentlyOpenMwdFileName == null)
            return null;

        int index = currentlyOpenMwdFileName.lastIndexOf(System.getProperty("file.separator"));
        return currentlyOpenMwdFileName.substring(0, index);
    }

    private void  closeSplashWindow() {
/*papakiru*/
//        if (!containerUtils.isWindowsMachine()) return;
//        JFrame stopSplashScreen = new JFrame("{FBF7BAC0-6ACE-11D3-A6FE-008048C7D6CD}");
//        javax.swing.JWindow win = new javax.swing.JWindow(stopSplashScreen);
//        win.setVisible(true);
    }

/*
    protected String findPathToJikes() {
        if (pathToJikes == null)
            pathToJikes = containerUtils.findPathToJikes();
        return pathToJikes;
    }
*/

    public ESlateHandle getActiveComponentHandle() {
        if (compSelectionHistory.getFirst() == null) return null;
        return compSelectionHistory.getFirst().handle;
    }

    public ESlateComponent getActiveComponent() {
        if (compSelectionHistory.getFirst() == null) return null;
        return compSelectionHistory.getFirst();
    }

    /** Returns the size of the whole microworld.
     */
    public Dimension getMicroworldSize() {
        return lc.getSize();
    }

    /** Returns the size of the currently visible part of the microworld, i.e. the
     *  size of the viewport of the DesktopPane.
     */
    public Dimension getMicroworldVisibleAreaSize() {
//System.out.println("scrollPane.getViewport().getSize(): " + scrollPane.getViewport().getSize());
        return scrollPane.getViewport().getSize();
    }

    /** Returns the position of the view, that is the point of the microworld area
     *  displayed at the upper left corner of the currently visible area.
     */
    public Point getViewPosition() {
        return scrollPane.getViewport().getViewPosition();
    }

    /** Sets the position of the microword's view, that is the point of the microworld area
     *  displayed at the upper left corner of the currently visible area.
     */
    public void setViewPosition(Point p) {
        scrollPane.getViewport().setViewPosition(p);
    }

    public void setMicroworldSize(Dimension dim) {
        /* There is a question whether setMicroworldSize() and setMwdResizable() should adjust the
         * size of the frame the ESlateContainer is hosted in, or should just adjust the protion of
         * the frame the ESlateContainer occupies, when the size is less than the frame's size.
         * The whole size thing needs reconsideration.
         * Most probably the size of the microworld should be independent of the size of the
         * ESlateContainer container, no matter if it's less or bigger that the container size. This
         * would provide the ability to have microworlds which occupy only a portion of the container,
         * even if they are bigger from it.
         */
        if (!currentView.mwdResizable) return;
        if (lc.getSize().equals(dim)) return;
//System.out.println("setMicroworldSize(" + dim + ") " + " current size: " + lc.getSize());

        setMicroworldChanged(true);
        currentView.desktopWidth = dim.width;
        currentView.desktopHeight = dim.height;
        int mwdWidth =dim.width;
        int mwdHeight = dim.height;
//        System.out.println("Current lc size:" + lc.getSize());
//        System.out.println("Current container size:" + getTopLevelAncestor().getSize());
//        System.out.println("mwdWidth: " + mwdWidth + ", mwdHeight: " + mwdHeight);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int containerWidth = getTopLevelAncestor().getSize().width;
        int containerHeight = getTopLevelAncestor().getSize().height;
        int newContainerWidth = containerWidth;
        int newContainerHeight = containerHeight;
        int widthDiff = containerWidth - scrollPane.getViewport().getExtentSize().width;
        int heightDiff = containerHeight - scrollPane.getViewport().getExtentSize().height;
        if (mwdWidth < containerWidth - widthDiff) {
            newContainerWidth = mwdWidth + widthDiff;
        }
        if (mwdHeight < containerHeight - heightDiff) {
            newContainerHeight = mwdHeight + heightDiff;
        }
//        System.out.println("widthDiff: " + widthDiff);
        if (mwdWidth > containerWidth - widthDiff) {
            if (mwdWidth + widthDiff > screenSize.width) {
//                System.out.println("Setting container width equal to screenwidth");
                newContainerWidth = screenSize.width;
            }else{
                newContainerWidth = mwdWidth + widthDiff;
//                System.out.println("1 newContainerWidth: " + newContainerWidth);
            }
        }
        if (mwdHeight > containerHeight - heightDiff) {
            if (mwdHeight + heightDiff > screenSize.height) {
//                System.out.println("Setting container height equal to screen height");
                newContainerHeight = screenSize.height;
            }else{
                newContainerHeight = mwdHeight + heightDiff;
//                System.out.println("1 newContainerHeight: " + newContainerHeight);
            }
        }

        Container topLevelAncestor = getTopLevelAncestor();
        if (java.awt.Frame.class.isAssignableFrom(topLevelAncestor.getClass()) && ((java.awt.Frame) topLevelAncestor).isResizable()) {
//            System.out.println("Resizable: " + ((java.awt.Frame) topLevelAncestor).isResizable());
            Dimension lcDim = new Dimension(mwdWidth, mwdHeight);
            Dimension containerDim = new Dimension(newContainerWidth, newContainerHeight);

            if (!containerDim.equals(getTopLevelAncestor().getSize())) {
//                System.out.println("Resing the topLevelAncestor containerDim: " + containerDim + ", getTopLevelAncestor().getSize(): " + getTopLevelAncestor().getSize());
                if (!(topLevelAncestor instanceof ESlateContainerWindow))
                    getTopLevelAncestor().setSize(containerDim);
                else
                    ((ESlateContainerWindow) getTopLevelAncestor()).setContainerSize(containerDim);
                getTopLevelAncestor().validate();
                getTopLevelAncestor().repaint();
            }
//            System.out.println("lcDim: " + lcDim);
            lc.setMaximumSize(lcDim);
            lc.setMinimumSize(lcDim);
            lc.setPreferredSize(lcDim);
            scrollPane.getViewport().setViewSize(lcDim);
            scrollPane.revalidate();
            lc.myValidate();
        }

//        System.out.println("New lc size:" + lc.getSize());
//        System.out.println("New container size:" + getTopLevelAncestor().getSize());
    }

    public final void setMwdResizable(boolean resizable) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setMwdResizableInternal(resizable);
    }
    private final void setMwdResizableInternal(boolean resizable) {
        if (microworld == null) return;
        /* There is a question whether setMicroworldSize() and setMwdResizable() should adjust the
         * size of the frame the ESlateContainer is hosted in, or should just adjust the protion of
         * the frame the ESlateContainer occupies, when the size is less than the frame's size.
         * The whole size thing needs reconsideration.
         * Most probably the size of the microworld should be independent of the size of the
         * ESlateContainer container, no matter if it's less or bigger that the container size. This
         * would provide the ability to have microworlds which occupy only a portion of the container,
         * even if they are bigger from it.
         */

        if (parentFrame == null) return;
//System.out.println("parentFrame.setResizable(" + resizable + ")" + ", parentFrame: " + parentFrame);
        if (parentFrame.isResizable() == resizable) return;
//        if (lc.getSize().width == scrollPane.getViewport().getExtentSize().width ||
//            lc.getSize().height == scrollPane.getViewport().getExtentSize().height) {
//        Container topLevelAncestor = getTopLevelAncestor();
//        if (java.awt.Frame.class.isAssignableFrom(topLevelAncestor.getClass())) {
//          if (parentFrame != null && parentFrame.isResizable() != resizable) {
            // When a JFrame becomes unresizable, its content area grows by 2 pixels
            // in both dimensions. Therefore the Desktop pane is resized.
//            ((java.awt.Frame) topLevelAncestor).setResizable(resizable);
            parentFrame.setResizable(resizable);
            parentFrame.invalidate();
            parentFrame.repaint();
//val            scrollPane.validateTree();
//System.out.println("parentFrame.setResizable(" + resizable + ")" );
//Thread.currentThread().dumpStack();
//        }
        currentView.mwdResizable = resizable;
        setMicroworldChanged(true);
//        }
    }
    public final void setMwdAutoExpandable(boolean expandable) {
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        if (microworld == null) return;
        microworld.checkSettingChangePriviledge();
        setMwdAutoExpandableInternal(expandable);
    }
    private final void setMwdAutoExpandableInternal(boolean expandable) {
        if (microworld == null) return;
        boolean tmp = currentView.mwdAutoExpandable;
        if (!currentView.componentsMoveBeyondMwdBounds)
            currentView.mwdAutoExpandable = false;
        else
            currentView.mwdAutoExpandable = expandable;
        if (tmp != currentView.mwdAutoExpandable)
            setMicroworldChanged(true);
//        System.out.println("mwdAutoExpandable:  " + mwdAutoExpandable);
    }
    public final boolean isMwdAutoExpandable() {
        return currentView.mwdAutoExpandable;
    }

    public void setMwdAutoScrollable(boolean scrollable) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setMwdAutoScrollableInternal(scrollable);
    }
    private final void setMwdAutoScrollableInternal(boolean scrollable) {
        if (microworld == null) return;
        if (currentView.mwdAutoScrollable != scrollable)
            setMicroworldChanged(true);
        currentView.mwdAutoScrollable = scrollable;
//        System.out.println("setMwdAutoScrollable(" + mwdAutoScrollable + ")");
    }
    public final boolean isMwdAutoScrollable() {
        return currentView.mwdAutoScrollable;
    }

    public void setComponentsMoveBeyondMwdBounds(boolean move) {
        if (microworld == null) return;
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts. It's the scripts responsibility to reset the value of this setting.
         */
        microworld.checkSettingChangePriviledge();
        setComponentsMoveBeyondMwdBoundsInternal(move);
    }
    private final void setComponentsMoveBeyondMwdBoundsInternal(boolean move) {
        if (microworld == null) return;
        if (currentView.componentsMoveBeyondMwdBounds != move)
            setMicroworldChanged(true);
        currentView.componentsMoveBeyondMwdBounds = move;
        setMwdAutoExpandableInternal(isMwdAutoExpandable());
    }
    public final boolean isComponentsMoveBeyondMwdBounds() {
        return currentView.componentsMoveBeyondMwdBounds;
    }

    public final void setDesktopDraggable(boolean draggable) {
        if (microworld == null) return;
        microworld.checkSettingChangePriviledge();
        setDesktopDraggableInternal(draggable);
    }
    private final void setDesktopDraggableInternal(boolean draggable) {
        if (currentView.desktopDraggable != draggable)
            setMicroworldChanged(true);
        currentView.desktopDraggable = draggable;
    }
    public final boolean isDesktopDraggable() {
        return currentView.desktopDraggable;
    }

    /** Prints the microworld. Does nothing if microworld printing is not allowed
     *  in the current microworld view.
     *  @param destination The print destination. One of ESlateContainer.PRINTER,
     *                     ESlateContainer.FILE.
     */
    public void printMicroworld(int destination) {
        if (microworld == null) return;
        if (currentView == null) return;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        microworld.checkActionPriviledge(currentView.isMwdPrintAllowed(), "mwdPrintAllowed");

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        if (destination != FILE && destination != PRINTER)
            return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        String iconFileName = null;
        if (destination == FILE) {
            iconFileName = getSystemFile(true, containerBundle.getString("ContainerMsg57"), ".jpg", new String[] {"jpg"});
            if (iconFileName == null) {
                setCursor(Cursor.getDefaultCursor(), false);
                return;
            }
        }else
            iconFileName = containerUtils.getTmpDir().getAbsolutePath() + System.getProperty("file.separator") + "print.jpg";
        BufferedImage img1 = getSnapshotImage(this);
        BufferedImage img = null;
        if (menuBarVisible) {
            Rectangle r = splitPane.getBounds();
            img = img1.getSubimage(r.x, r.y, r.width, r.height);
            img1.flush();
            img1 = null;
        }else
            img = img1;
        if (img == null) {
            setCursor(Cursor.getDefaultCursor(), false);
            return;
        }
        try{
//            NewRestorableImageIcon icon = new NewRestorableImageIcon(img);
//            File iconFile = new File(iconFileName);
//            FileOutputStream stream = new FileOutputStream(iconFile);
//            BufferedOutputStream str = new BufferedOutputStream(stream);
//            icon.saveImage(NewRestorableImageIcon.PNG, stream);
//            stream.flush();
//            stream.close();

            if (destination == PRINTER) {
                try{
                	PrintImage.print(img);
//                    printImage(iconFile.getAbsolutePath(),
//                               pageInfo.getTopMargin(),
//                               pageInfo.getLeftMargin(),
//                               pageInfo.getBottomMargin(),
//                               pageInfo.getRightMargin(),
//                               (pageInfo.isCenterOnPage()?1:0),
//                               (pageInfo.isFitToPage()?1:0),
//                               pageInfo.getScale());
                }catch (Throwable thr) {
                    ESlateOptionPane.showMessageDialog(parentComponent, thr.getMessage(), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
/*                gr.cti.eslate.utils.PrintImageFunctions.printImage(iconFile.getAbsolutePath(),
                                                                    margins.top, margins.bottom,
                                                                    margins.left, margins.right,
                                                                    (pageInfo.isCenterOnPage()?1:0),
                                                                    (pageInfo.isFitToPage()?1:0),
                                                                    pageInfo.getScale());
*/
            } else {
            	ImageIO.write(img,"jpg",new File(iconFileName));
            }
//            icon.getImage().flush();
//            icon = null;
        }catch (Throwable thr) {
            thr.printStackTrace();
        }
        setCursor(Cursor.getDefaultCursor(), false);
//        styleReportPrint(scrollPane.getViewport(), destination);
    }

    /** Prints the top-level component with the given name. Does nothing if component
     *  printing is not allowed in the current microworld view.
     *  @param componentName The name of the top level component to be printed.
     *  @param destination The print destination. One of ESlateContainer.PRINTER,
     *                     ESlateContainer.FILE.
     */
    public void printComponent(String componentName, int destination) {
        if (microworld == null || currentView == null) return;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        microworld.checkActionPriviledge(currentView.isComponentPrintAllowed(), "componentPrintAllowed");

        ESlateComponent component = getComponent(componentName);
        if (component == null) {
            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg53") + componentName + containerBundle.getString("ContainerMsg54"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (component.isIcon()) {
            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg55") + componentName + containerBundle.getString("ContainerMsg56"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (destination != FILE && destination != PRINTER)
            return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        String iconFileName = null;
        if (destination == FILE) {
            iconFileName = getSystemFile(true, containerBundle.getString("ContainerMsg57"), ".jpg", new String[] {"jpg"});
            if (iconFileName == null) {
                setCursor(Cursor.getDefaultCursor(), false);
                return;
            }
        }else
            iconFileName = containerUtils.getTmpDir().getAbsolutePath() + System.getProperty("file.separator") + "print.jpg";
        BufferedImage img1 = getSnapshotImage(this);
//        System.out.println("img1: " + img1.getWidth() + ", " + img1.getHeight());
        Rectangle r = ((Component) component.desktopItem).getBounds();
        r.x = r.x + 3;
        r.y = r.y + 3;
        r.width = r.width-6;
        r.height = r.height-6;
        System.out.println("r: " + r);
        r = SwingUtilities.convertRectangle(((Component) component.desktopItem).getParent(), r, this);
//        System.out.println("r: " + r);
        BufferedImage img = img1.getSubimage(r.x, r.y, r.width, r.height);
        img1.flush();
        img1 = null;
//        System.out.println("img: " + img.getWidth() + ", " + img.getHeight());
//        BufferedImage img = getSnapshotImage((Component) component.desktopItem);
        if (img == null) {
            setCursor(Cursor.getDefaultCursor(), false);
            return;
        }
        try{
//            NewRestorableImageIcon icon = new NewRestorableImageIcon(img);
//            File iconFile = new File(iconFileName);
//            FileOutputStream stream = new FileOutputStream(iconFile);
//            BufferedOutputStream str = new BufferedOutputStream(stream);
//            icon.saveImage(NewRestorableImageIcon.PNG, stream);
//            stream.flush();
//            stream.close();

            if (destination == PRINTER) {
                try{
                	PrintImage.print(img);
//                    printImage(iconFile.getAbsolutePath(),
//                               pageInfo.getTopMargin(),
//                               pageInfo.getLeftMargin(),
//                               pageInfo.getBottomMargin(),
//                               pageInfo.getRightMargin(),
//                               (pageInfo.isCenterOnPage()?1:0),
//                               (pageInfo.isFitToPage()?1:0),
//                               pageInfo.getScale());
                }catch (Throwable thr) {
                    thr.printStackTrace();
                    ESlateOptionPane.showMessageDialog(parentComponent, thr.getMessage(), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
/*                gr.cti.eslate.utils.PrintImageFunctions.printImage(iconFile.getAbsolutePath(),
                                                                    margins.top, margins.bottom,
                                                                    margins.left, margins.right,
                                                                    (pageInfo.isCenterOnPage()?1:0),
                                                                    (pageInfo.isFitToPage()?1:0),
                                                                    pageInfo.getScale());
*/
            } else
            	ImageIO.write(img,"jpg",new File(iconFileName));
//            icon.getImage().flush();
//            icon = null;
        }catch (Throwable thr) {
            thr.printStackTrace();
        }
        setCursor(Cursor.getDefaultCursor(), false);
    }

    /* Current shortcomings of the print mechanism
     * 1. Have to implement custom paint() for the desktop pane (lc) causes the
     *    JLayeredPane's paint() throws a NullPointerException. It seems feasible
     *    to draw 1 by 1 all the elements of the E-Slate desktop (background color/icon
     *    and then the frames according to the layer they belong to.
     * 2. PDF and RTF printing have standard paper size(A4) and orientation(PORTRAIT). A
     *    dialog should pop-up before PDF/RTF printing takes place, were the user can alter
     *    these parameters.
     */
/*    public void styleReportPrint(Component component, int destination) {
        if (component == null)
            return;

        if (destination == PRINTER)
            if (!containerUtils.createPrJob())
                return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        menuPanel.setPrintStatusOn(true);
*/        /* We initialize the page dimension to the dimension of the A4 page. In the future
         * a dialog must pop-up for PDF and RTF printing, asking for orientation and page size
         * info.
         */
/*        Dimension dim = new Dimension(595, 842);
        int resolution = 72;
        if (destination == PRINTER) {
            java.awt.PrintJob prJob = (java.awt.PrintJob) containerUtils.getPrJob().elementAt(0);
            dim = prJob.getPageDimension(); //new Dimension(800, 1100); //
            resolution = prJob.getPageResolution(); //72;
        }
//        System.out.println("dim: " + dim + ", resolution: " + resolution);
        BufferedImage image = getSnapshotImage(component);
        if (image == null) {
            setCursor(Cursor.getDefaultCursor(), false);
            menuPanel.setPrintStatusOn(false);
            return;
        }

        java.awt.Image[] images = null; // = createImageTiles(image, dim);
        if (pageInfo.isFitToPage()) {
            images = new java.awt.Image[1];
            Insets m = pageInfo.getMargin();
            int horMargin = m.left + m.right;
            int vertMargin = m.top + m.bottom;
            images[0] = image.getScaledInstance(dim.width-horMargin, dim.height-vertMargin, java.awt.Image.SCALE_DEFAULT);
            image.flush();
        }else{
            int scale = pageInfo.getScale();
            if (scale != 100) {
                int newWidth = (int) ((scale/100f) * image.getWidth(this));
                int newHeight = (int) ((scale/100f) * image.getHeight(this));
//                System.out.println("newWidth: " + newWidth + ", newHeight: " + newHeight);
                java.awt.Image img = image.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_DEFAULT);
                image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                image.createGraphics().drawImage(img, null, null);
                img.flush();
            }
            Insets m = pageInfo.getMargin();
            int horMarginPixels = (int) (((m.left + m.right)/2.54f)*resolution);
            int vertMarginPixels = (int) (((m.top + m.bottom)/2.54f)*resolution);
            images = createImageTiles(image, new Dimension(dim.width-horMarginPixels, dim.height-vertMarginPixels-3));
            image.flush();
        }
        //System.out.println("num of images: " + images.length);

        File printFile = new File(containerUtils.getTmpDir(), "print.pdf");
        if (destination == RTF_FILE || destination == PDF_FILE) {
            String fileName = null;
            if (destination == RTF_FILE)
                fileName = getSystemFile(true, containerBundle.getString("ContainerMsg38"), null, new String[] {"rtf"});
            else
                fileName = getSystemFile(true, containerBundle.getString("ContainerMsg39"), null, new String[] {"pdf"});
            if (fileName == null) {
                setCursor(Cursor.getDefaultCursor(), false);
                menuPanel.setPrintStatusOn(false);
                return;
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
            printFile = new File(fileName);
        }
        if (destination == RTF_FILE) {
            try{
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(printFile));
                inetsoft.report.io.Builder builder = inetsoft.report.io.Builder.getBuilder(inetsoft.report.io.Builder.RTF, os);
                StyleSheet report = createReport(dim, resolution, images);
                builder.write(report);
                os.close();
            }catch (Throwable thr) {
                thr.printStackTrace();
                DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
                String message = containerBundle.getString("ContainerMsg36");
                dialog.setMessage(message);
    //            dialog.setDetails("Exception while disposing component's handle. Continuing... " + '\n');
                dialog.appendThrowableMessage(thr);
                dialog.createNewLine();
                dialog.appendThrowableStackTrace(thr);
                ESlateContainerUtils.showDialog(dialog, container, true);
            }
            setCursor(Cursor.getDefaultCursor(), false);
            menuPanel.setPrintStatusOn(false);
            return;
       }

        PDFPrinter pdf = null;
        inetsoft.report.Size pageSize = new inetsoft.report.Size(dim.width, dim.height, resolution);
        try{
            pdf = new PDFPrinter(printFile);
*/            /* If a printer is the destination, then don't compress the PDF archieve, cause
             * it'll have to be decompressed again by the Acrobat viewer in order to be printed.
             * This saves some time.
             */
/*            if (destination == PRINTER) {
                pdf.setCompressImage(false);
                pdf.setCompressText(false);
            }
            pdf.setPageSize(pageSize);
            StyleSheet report = createReport(dim, resolution, images);
//            pdf.setPageSize(new inetsoft.report.Size(getSize().width, getSize().height, 72)); //.PAPER_A4);
            System.gc();
            report.print(pdf.getPrintJob());
            pdf.close();
        }catch (Throwable thr) {
            thr.printStackTrace();
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            String message = containerBundle.getString("ContainerMsg36");
            dialog.setMessage(message);
//            dialog.setDetails("Exception while disposing component's handle. Continuing... " + '\n');
            dialog.appendThrowableMessage(thr);
            dialog.createNewLine();
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDialog(dialog, container, true);
            try{
                pdf.close();
                containerUtils.finalizePrJob();
            }catch (Throwable exc) {}

            setCursor(Cursor.getDefaultCursor(), false);
            menuPanel.setPrintStatusOn(false);
            return;
        }
        //System.out.println("StyleReport done...");

        if (destination == PRINTER)
            printPDFFileToPrinter(printFile);
        else{
            setCursor(Cursor.getDefaultCursor(), false);
            menuPanel.setPrintStatusOn(false);
        }
    }

    StyleSheet createReport(Dimension pageDim, int resolution, java.awt.Image[] images) {
        inetsoft.report.Size pageSize = new inetsoft.report.Size(pageDim.width, pageDim.height, resolution);
        StyleSheet report = new StyleSheet();
//            report.addComponent(this);
        Insets m = pageInfo.getMargin();
        report.setMargin(new inetsoft.report.Margin(m.top/2.54d, m.left/2.54d, m.bottom/2.54d, m.right/2.54d));
        for (int i=0; i<images.length; i++){
            if (!pageInfo.isFitToPage() && pageInfo.isCenterOnPage() && i==(images.length-1)) {
                inetsoft.report.Size imageSize = new  inetsoft.report.Size(images[i].getWidth(this), images[i].getHeight(this), resolution);
//                    System.out.println("pageSize: " + pageSize + ", imageSize: " + imageSize);
                double topMargin = (pageSize.height-imageSize.height)/2d;
                double leftMargin = (pageSize.width-imageSize.width)/2d;
                if (leftMargin < 0) leftMargin = 0;
                if (topMargin < 0) topMargin = 0;
*/
//                    System.out.println("topMargin: " + topMargin + ", leftMargin: " + leftMargin);
//                    System.out.println("pageSize.width-imageSize.width-leftMargin: " + (pageSize.width-imageSize.width-leftMargin));
//                    System.out.println("pageSize.height-imageSize.height-topMargin: " + (pageSize.height-imageSize.height-topMargin));
                /* setCurrentAlignment() has an error in 2.3 and alligns only horizontally. To align in both dimensions,
                 * margins are used. However the margin way causes OutOfMemoryError, when applied to any page but the first.
                 * The following if-statement resolves this situation.
                 */
/*                if (i >= 1)
                    report.setCurrentAlignment(inetsoft.report.StyleConstants.V_CENTER|inetsoft.report.StyleConstants.H_CENTER);
                else
                    report.setMargin(new inetsoft.report.Margin(topMargin, leftMargin, pageSize.height-imageSize.height-topMargin, pageSize.width-imageSize.width-leftMargin));
            }
            report.addImage(images[i]);
            if (i == 0 && images.length == 1);
            else report.addPageBreak();
//                java.lang.Runtime rt = java.lang.Runtime.getRuntime();
//                System.out.println("imahe: " + i + " ----- MEMORY STATUS. Total heap size: " + rt.totalMemory() + ".  Free memory: " + rt.freeMemory() + ".  Used memory: " + (rt.totalMemory() - rt.freeMemory()) + ".");
            System.gc();
        }

        for (int i=0; i<images.length; i++)
            images[i].flush();

        return report;
    }

    void printPDFFileToPrinter(File file) {
        //System.out.println("Starting Acrobat");
        try{
            JFrame frame = new JFrame("Test Viewer");
            frame.getContentPane().setLayout(new BorderLayout());
            com.adobe.acrobat.Viewer viewer = new com.adobe.acrobat.Viewer();
            frame.getContentPane().add(viewer, BorderLayout.CENTER);
            viewer.activate();
//            frame.setSize(200, 300);

//            System.out.println("Reading file");
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file)); //"c:\\test.pdf"));
            viewer.setDocumentInputStream(fis);
            fis.close();
//            System.out.println("File read");

            viewer.execMenuItem(com.adobe.acrobat.ViewerCommand.Print_K);
            viewer.execMenuItem(com.adobe.acrobat.ViewerCommand.Close_K);
            viewer.deactivate();
            viewer.destroy();
            frame.dispose();
//            frame.show();
        }catch (Throwable thr) {
            thr.printStackTrace();
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            String message = containerBundle.getString("ContainerMsg36");
            dialog.setMessage(message);
//            dialog.setDetails("Exception while disposing component's handle. Continuing... " + '\n');
            dialog.appendThrowableMessage(thr);
            dialog.createNewLine();
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDialog(dialog, container, true);
            try{
                containerUtils.finalizePrJob();
            }catch (Exception exc) {}
            setCursor(Cursor.getDefaultCursor(), false);
            menuPanel.setPrintStatusOn(false);
            return;
        }
    }
*/
/*    public void pageLayoutPDF() {
        // Create a PDF printer, output to stdout
        try{
            BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream("c:\\test.pdf"));
            JCPDFPrinter printer = new JCPDFPrinter(fos); //new JCPDFPrinter(System.out);

            // Create a document using the PDF printer for formatting,
            // setting the page template to be a simple 8.5 x 11 Letter page
            JCDocument document = new JCDocument(printer, JCDocument.BLANK_8p5X11);

            // Instantiate a flow object on the document
            JCFlow flow = new JCFlow(document);

            flow.embedComponent(this); // Places the chart in the flow
//            flow.embedImage(imprint.plainImage);

	          // The following adds a caption to the embedded image:
	          flow.newLine();
            // Print some text to the document
            flow.print("Hello, World.");

            // Print the document to the PDF printer
            document.print();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Print Job Cancelled by user");
//            System.exit(1);
      	}

        try{
            JFrame frame = new JFrame("Test Viewer");
            frame.getContentPane().setLayout(new BorderLayout());
            System.out.println("Creating viewer");
            com.adobe.acrobat.Viewer viewer2 = new com.adobe.acrobat.Viewer();
            System.out.println("Viewer ready");
            frame.getContentPane().add(viewer2, BorderLayout.CENTER);
            viewer2.activate();
//            frame.setSize(200, 300);

            System.out.println("Reading file");
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream("c:\\test.pdf"));
            viewer2.setDocumentInputStream(fis);
            System.out.println("File read");
            viewer2.execMenuItem(com.adobe.acrobat.ViewerCommand.Print_K);

//            frame.show();
        }catch (Exception exc) {
            exc.printStackTrace();
        }

    }
*/

    /** Prints an image from its file. Printing is done using a native .exe
     */
    private void printImage(String fileName, float top, float left, float bottom, float right, int centerOnPage, int fitToPage, int scale) throws Exception {
      getBinFolder(pathToContainerJar);
      if (binFolder == null)
          throw new Exception(containerBundle.getString("PrintErrorMsg1"));
      //Check if the .exe which does the printing is in place.
      File printExe = new File(binFolder, "vbPrintImage.exe");
      if (!printExe.exists())
          throw new Exception(containerBundle.getString("PrintErrorMsg2") + " \"vbPrintImage.exe\" " + containerBundle.getString("PrintErrorMsg3"));

      gr.cti.eslate.utils.Print.printImage(fileName, (double) top, (double) left,
                                           (double) bottom, (double) right,
                                           (centerOnPage==1)?true:false,
                                           (fitToPage==1)?true:false, scale/100d, "", false, true);
/*      String command = printExe.getAbsolutePath() + ' ' + fileName + ' ' + margin.top + ' ' + margin.bottom + ' ' + margin.left + ' ' + margin.right + ' ' + centerOnPage + ' ' + fitToPage + ' ' + scale;
//      System.out.println("command: " + command);
      Process p = Runtime.getRuntime().exec(command);
*/
    }

/*p    public void oldStylePrint() {
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        java.awt.PageAttributes pageAttributes = new java.awt.PageAttributes();
        java.awt.JobAttributes jobAttributes = new java.awt.JobAttributes();
//        jobAttributes.setDialog(JobAttributes.DialogType.COMMON);
        java.awt.PrintJob job = toolkit.getPrintJob(parentFrame,
                    "Print Dialog?",
                    jobAttributes,
                    pageAttributes);

        if (job == null) {
            System.out.println("Canceled printing...");
            return;
        }

        Dimension pageSize = job.getPageDimension();
        System.out.println("page size: " + pageSize);
//        System.out.println("singlePagePrint() iconWidth: " + icon.getIconWidth() + ", iconHeight: " + icon.getIconHeight());

        Graphics page = job.getGraphics();
//        System.out.println("page: " + page);
        System.out.println("this.getSize(): " + this.getSize());
        page.translate((pageSize.width - getSize().width)/2, (pageSize.height - getSize().height)/2);

        this.printAll(page);

        page.dispose();
        job.end();
    }
*/
    /** Gets a snapshot of the specified AWT Component. The snapshot includes only
     *  the area of the component which is included in the specified Rectangle.
     */
    public static BufferedImage getSnapshotImage(Component c) { //only for Java2
        Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, c);
        if (topLevelFrame == null && Frame.class.isAssignableFrom(c.getClass()))
            topLevelFrame = (Frame) c;
        ESlateContainer container = (ESlateContainer) SwingUtilities.getAncestorOfClass(ESlateContainer.class, c);
        try{
            java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(c.getWidth(),c.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
            if(img!=null){
//                Graphics g=img.getGraphics(); //same as doing "img.createGraphics()"
                java.awt.Graphics2D g = (java.awt.Graphics2D)img.createGraphics();
//                g.clipRect(0, 0, c.getWidth(),c.getHeight());
                if(g!=null){
//                    java.awt.RenderingHints hints = new java.awt.RenderingHints(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
//                    g.addRenderingHints(hints);
                    c.paint(g); //draw the component's contents on the buffered image //seems to fail with null pointer exception in paint when using Java1.2, but it works OK with Java1.3rc3
                    g.dispose();
/*                    JDialog d = new JDialog(new JFrame(), true);
                    ImageIcon ii = new ImageIcon(img);
                    JScrollPane sp = new JScrollPane(new JLabel(ii));
                    d.getContentPane().setLayout(new BorderLayout());
                    d.getContentPane().add(sp, BorderLayout.CENTER);
                    d.setSize(300, 300);
                    d.setVisible(true);
*/
                    return img; //the one who called us should use the image and then dispose it's buffer calling flush() on it
                }
                img.flush(); //couldn't paint the component's contents, so dispose the image and return null
            }
            return null;
        }catch (Throwable thr) {
            thr.printStackTrace();
            DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
            String message = containerBundle.getString("ContainerMsg37");
            dialog.setMessage(message);
//            dialog.setDetails("Exception while disposing component's handle. Continuing... " + '\n');
            dialog.appendThrowableMessage(thr);
            dialog.createNewLine();
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, c, true);
            return null;
        }
    }


/*0    public void classicPrinterJob() {
        java.awt.print.PrinterJob job = PrinterJob.getPrinterJob();
        JComponent jcomp = (JComponent) componentFrames.at(0);
        if (job.printDialog()) {
            MyPrintable printable = new MyPrintable(jcomp);
            job.setPrintable(printable);
            try{
                job.print();
            }catch (Exception exc) {
                System.out.println(exc.getClass() + ": " + exc.getMessage());
            }
        }
    }
*/
/*0    public void classicImagePrinterJob() {
        java.awt.print.PrinterJob job = PrinterJob.getPrinterJob();
        BufferedImage img = getSnapshotImage((Component) componentFrames.at(0));
        PageFormat pf = job.defaultPage();
*/
    /*        if (pf.getOrientation() == pf.LANDSCAPE) {
                img = createLandscapeImage(img);
                pf.setOrientation(pf.PORTRAIT);
            }
            BufferedImage[] images = createImageTiles(img, pf.getPaper());
    */
/*0        if (job.printDialog()) {
            ImagePrintable printable = new ImagePrintable(img);
            job.setPrintable(printable);
            try{
                job.print();
            }catch (Exception exc) {
                System.out.println(exc.getClass() + ": " + exc.getMessage());
            }
        }
    }
*/
/*    public void printStylePrinterJob() {
        inetsoft.report.ReportEnv.setProperty("StyleReport.useCustomDriver", "true");

        java.awt.print.PrinterJob job = inetsoft.report.j2d.StylePrinter.getPrinterJob(); //PrinterJob.getPrinterJob();

        PageFormat pf = job.pageDialog(job.defaultPage());
    //                System.out.println("pf.getOrientation() == pf.LANDSCAPE: " + (pf.getOrientation() == pf.LANDSCAPE));
        lc.setDoubleBuffered(false);
        JComponent jcomp = (JComponent) componentFrames.at(0);
        BufferedImage img = getSnapshotImage((Component) componentFrames.at(0));
        lc.setDoubleBuffered(true);
//        System.out.println("Image size: " + img.getWidth() + ", " + img.getHeight());
*/
    /*        if (pf.getOrientation() == pf.LANDSCAPE) {
                img = createLandscapeImage(img);
                pf.setOrientation(pf.PORTRAIT);
            }
            BufferedImage[] images = createImageTiles(img, pf.getPaper());
    */
/*        StyleSheet report = new StyleSheet();
        report.setCurrentPainterLayout(StyleSheet.PAINTER_BREAKABLE);
*/
    /*        System.out.println("images.length: " + images.length);
            for (int i=0; i<images.length; i++) {
    //            report.addImage(images[i]);
                        report.addPainter(new ImagePainter(images[i], false));
    //            report.addPageBreak();
            }
    */
/*        report.addPainter(new ImagePainter(img, false));
        if (job.printDialog()) {
            inetsoft.report.j2d.StyleBook book = new inetsoft.report.j2d.StyleBook(report, pf);
            job.setPageable(book);
            try{
                job.print();
            }catch (Exception exc) {
                System.out.println(exc.getClass() + ": " + exc.getMessage());
            }
        }
    }
*/
    public BufferedImage[] createImageTiles(BufferedImage img, Dimension paperSize) {
        //Slash the image into tiles, if necessary.
//        System.out.println("paperSize: " + paperSize);
//        JDialog d = new JDialog(new JFrame(), true);
//        ImageIcon ii = new ImageIcon(img);
//        JScrollPane sp = new JScrollPane(new JLabel(ii));
//        d.getContentPane().setLayout(new BorderLayout());
//        d.getContentPane().add(sp, BorderLayout.CENTER);
//        d.setSize(300, 300);
//        d.setVisible(true);
        int paperWidth = paperSize.width;
        int paperHeight = paperSize.height;
        if (img.getWidth() > paperWidth || img.getHeight() > paperHeight) {
            int xTileNum = (int) (img.getWidth() / paperWidth);
            if (img.getWidth() % paperWidth != 0)
                xTileNum++;
            int yTileNum = (int) (img.getHeight() / paperHeight);
            if (img.getHeight() % paperHeight != 0)
                yTileNum++;
//            System.out.println("Have to perforn tiling xTileNum: " + xTileNum + ", yTileNum: " + yTileNum);

            int numOfImages = xTileNum * yTileNum;
            BufferedImage[] images = new BufferedImage[numOfImages];
            int imgIndex = 0;
            int currX = 0, currY = 0;
            int pw = (int) paperWidth;
            int ph = (int) paperHeight;
            for (int i=0; i<xTileNum; i++) {
                currY = 0;
                int tileW = (currX + pw <= img.getWidth())? pw : (int) img.getWidth()-currX;
                for (int j=0; j<yTileNum; j++) {
                    int tileH = (currY + ph <= img.getHeight())? ph : (int) img.getHeight()-currY;
//                    System.out.println("getsubImage() currX: " + currX + ", currY: " + currY + ", tileW: " + tileW + ", tileH: " + tileH);
                    images[imgIndex] = img.getSubimage(currX, currY, tileW, tileH);
                    imgIndex++;
                    currY = currY + tileH;
                }
                currX = currX + pw;
            }

/*            for (int i=0; i<images.length; i++) {
                ImageIcon ii2 = new ImageIcon(images[i]);
                javax.swing.JOptionPane.showMessageDialog(ESlateContainer.this,
                                 "Image " + i,
                                 "Tiles",
                                 javax.swing.JOptionPane.INFORMATION_MESSAGE,
                                 ii2);
            }
*/
            return images;
        }
        return new BufferedImage[] {img};
    }
/*
    public BufferedImage createLandscapeImage(BufferedImage inputImage) {
        AffineTransform atr = AffineTransform.getRotateInstance(Math.PI/2, inputImage.getHeight()/2, inputImage.getWidth()/2);
        BufferedImage img = new BufferedImage(inputImage.getHeight(), inputImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D gr = (java.awt.Graphics2D) img.getGraphics();
        gr.setTransform(atr);
        gr.drawImage(inputImage, (img.getWidth()-inputImage.getWidth())/2, (img.getHeight()-inputImage.getHeight())/2, null);
*/
    /*            AffineTransformOp atrop = new AffineTransformOp(atr, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage img2 = atrop.filter(img, new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_ARGB));
    */
    //                    System.out.println("Transformed image size: " + img2.getWidth() + ", " + img2.getHeight());
    /*            ImageIcon ii = new ImageIcon(img2);
            javax.swing.JOptionPane.showMessageDialog(ESlateContainer.this,
                             "It should print like this",
                             "Test",
                             javax.swing.JOptionPane.INFORMATION_MESSAGE,
                             ii);
    */
/*        inputImage.flush();
        return img;
    }
*/
/*  public StyleSheet createReport() {
        StyleSheet report = new StyleSheet();
        report.setCurrentPainterLayout(StyleSheet.PAINTER_BREAKABLE);
        System.out.println("lc.getPreferredSize(): " + lc.getPreferredSize());
//        report.addComponent(lc, 20, 5);
        report.addPainter(new ComponentPainter(lc, ComponentPainter.PRINTALL) {
            public boolean isScalable() {
                return false;
            }
*/
/*            public java.awt.Dimension getPreferredSize() {
                Dimension dim = lc.getPreferredSize();
                System.out.println("Painter getPreferredSize(): " + dim);
                return dim;
            }
*/
/*        });
        return report;
    }
*/

    public void pageSetupAction() {
        if (microworld == null || currentView == null) return;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        microworld.checkActionPriviledge(currentView.mwdPageSetupAllowed, "mwdPageSetupAllowed");

        PageSetupPanel dialog = new PageSetupPanel(parentFrame, pageInfo);
        ESlateContainerUtils.showDialog(dialog, this, false);
        if (dialog.getReturnCode() == dialog.DIALOG_OK)
            pageInfo = dialog.getPageInfo();
    }

    void setLoadingMwd(boolean loading) {
//        System.out.println("setLoadingMwd(" + loading + "), loadingMwd: " + loadingMwd);
        if (loading == loadingMwd)  return;
        if (loading)
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        loadingMwd = loading;
        if (!loading)
            setCursor(Cursor.getDefaultCursor(), false);
//        setScreenFrozen(loading);//scrollPane.freezeScreen = loading; //setVisible(!loading);
        if (!loadingMwd) {
            if (microworld != null) { //if microworld loading succeeded
                // Make the active component hightlighted
                if (mwdComponents != null && mwdComponents.activeComponent != null) {
                    if (mwdComponents.activeComponent.frame != null && mwdComponents.activeComponent.frame.isActiveStateDisplayed()) {
                        try{
                            mwdComponents.activeComponent.setActive(true);
//System.out.println("Manually selected component " + mwdComponents.activeComponent.getName());
                        }catch (Exception exc) {}
                    }
                }
                /* Set the microworld locked status, after loading has finished. */
                microworld.updateLockedStatusAfterMwdLoading();
                PerformanceManager pm = PerformanceManager.getPerformanceManager();
                pm.init(mwdLoadedListenerTimer);
long mwdLoadedListenerTime = System.currentTimeMillis();

                try{
					microworld.fireMicroworldLoaded(new MicroworldEvent(microworld));
				}catch (Throwable thr) {
					thr.printStackTrace();
				}
                pm.stop(mwdLoadedListenerTimer);
mwdLoadedListenerTime = System.currentTimeMillis()-mwdLoadedListenerTime;
//System.out.println("System mwdLoadedListenerTime: " + mwdLoadedListenerTime);
                pm.displayElapsedTime(mwdLoadedListenerTimer, "", "ms");
//                scrollPane.validateTree();
                setMicroworldChanged(false);
            }
        }
        if (!loadingMwd) {
            eventQueue.postMicroworldFinalRepaintEvent();
        }else{
            repaintManager.active = false;
        }
    }

    /** Returns true, only while a microworld is closing. */
    public final boolean isMicroworldClosing() {
        return closingMwd;
    }

    /** Returns true, only while a microworld is being loading. */
    public final boolean isMicroworldLoading() {
        return loadingMwd;
    }

/*    void enableSecurityManager() {
        if (securityManager == null)
            securityManager = new MySecurityManager();
        securityManager.enable();
        System.setSecurityManager(securityManager);
    }

    void disableSecurityManager() {
        if (securityManager == null)
            return;
        securityManager.disable();
        System.setSecurityManager(null);
    }
*/

    /* Arrange the ESlateComponents of the microworld in the order in which they were last
     * activated. The list of arranged ESlateComponents is returned to the caller. The list
     * also includes the non-visual components, which are placed after the visual.
     */

    ESlateComponent[] arrangeMicroworldComponentBasedOnSelectionHistory() {
        ESlateComponentArray newComponents = new ESlateComponentArray(); //[compSelectionHistory.size()];
        int f = 0;
        ESlateComponent ecomp;
        while ((ecomp = compSelectionHistory.get(f)) != null) { //1 && frame != EMPTY_FRAME) {
//0            newComponentFrames.insert(0, frame);
//0            newComponents.insert(0, frame.eSlateHandle.getComponent());
//1            ESlateComponent ecomp = mwdComponents.getComponent(frame);
            newComponents.add(0, ecomp);
            f++;
        }
        ESlateComponent comp;
        if (newComponents.size() != mwdComponents.size()) {
            for (int i=0; i<mwdComponents.size(); i++) {
                comp = mwdComponents.components.get(i);
                if (newComponents.indexOf(comp) == -1) {
                    if (comp.visualBean)
                        newComponents.add(comp); //(0, comp);
                    else
                        newComponents.add(comp);
                }
            }
        }
        return newComponents.toArray();
    }
    /* Arrange the 'components' and 'componentFrames' Arrays, so that the component appear in the

     * order in which they were last activated. This way the components will
     * be stored and re-created in this order, which is an important fact
     * for the correct Z-order restore of the internal frames.
     */
/*0    void arrangeMicroworldComponentBasedOnSelectionHistory(Array newComponents, Array newComponentFrames) {

        int f = 0;

        ESlateInternalFrame frame;
        while ((frame = compSelectionHistory.get(f)) != null && frame != EMPTY_FRAME) {
            newComponentFrames.insert(0, frame);
            newComponents.insert(0, frame.eSlateHandle.getComponent());
            f++;
        }
        Object comp;
        if (newComponents.size() != components.size()) {
            for (int i=0; i<components.size(); i++) {
                comp = components.at(i);
                if (newComponents.indexOf(comp) == -1)
                    newComponents.insert(0, comp);
            }
        }
        if (newComponentFrames.size() != componentFrames.size()) {
            for (int i=0; i<componentFrames.size(); i++) {
                frame = (ESlateInternalFrame) componentFrames.at(i);
                if (newComponentFrames.indexOf(frame) == -1)
                    newComponentFrames.insert(0, frame);
            }
        }
    }

*/

    /** Creates an new view with the specified name. The name can be null, in
     *  which case a default unique name will be given to new view. The new view
     *  is added to the MicroworldViewList. Does nothing if there is no opened microworld,
     *  or if view creation is not allowed in the opened microworld. The view is recorded
     *  in the list of the microworld views.
     *  The new view's microworld properties are a replica of the current microworld
     *  view's information.
     *  The component skin information is saved, if the microworld property 'storeSkinsPerView'
     *  is set.
     */
    public MicroworldView createNewView(String viewName) {
        return createNewView(viewName, true);
    }


    /** Creates an new view with the specified name. The name can be null, in
     *  which case a default unique name will be given to new view. The new view
     *  is added to the MicroworldViewList, if the 'recordView' argument is true. Does nothing if
     *  there is no opened microworld,
     *  or if view creation is not allowed in the opened microworld.
     *  The new view's microworld properties are a replica of the current microworld
     *  view's information.
     *  The component skin information is saved, if the microworld property 'storeSkinsPerView'
     *  is set.
     */
    public MicroworldView createNewView(String viewName, boolean recordView) {
        return createNewView(viewName, currentView, recordView);
    }

    /** Creates an new view with the specified name. The name can be null, in
     *  which case a default unique name will be given to new view. The new view
     *  is added to the MicroworldViewList, if 'recordView' is true. Does nothing if
     *  there is no opened microworld,
     *  or if view creation is not allowed in the opened microworld.
     *  The 'baseView' argument determines the view from which the view-specific microwold
     *  settings will be copied to the new view.
     *  The new view's microworld properties are a replica of the baseView's microworld
     *  information.
     *  The component skin information is saved, if the microworld property 'storeSkinsPerView'
     *  is set.
     */
    public MicroworldView createNewView(String viewName, MicroworldView baseView, boolean recordView) {
        return createNewView(viewName, baseView, recordView, microworld.isStoreSkinsPerView());
    }

    /** Creates an new view with the specified name. The name can be null, in
     *  which case a default unique name will be given to new view. Does nothing if
     *  there is no opened microworld,
     *  or if view creation is not allowed in the opened microworld.
     *  The 'baseView' argument determines the view from which the view-specific microwold
     *  settings will be copied to the new view.
     *  The 'recordView' argument determines if the view will be attached to the microworld or not.
     *  If false, the view is created and returned, but no reference to it is kept in the
     *  MicroworldViewList.
     *  The new view's microworld properties are a replica of the baseView's microworld
     *  information.
     *  'recordSkins' determines if the skins of the components will be stored as part of the new
     *  view's properties. If true, the skins will be recorded.
     */
    public MicroworldView createNewView(String viewName, MicroworldView baseView, boolean recordView, boolean recordSkins) {
        if (microworld == null) return null;

        MicroworldView mView = new MicroworldView(this, baseView);
        if (viewName != null)
            mView.viewName = viewName;
        mView.createDesktopItemInfos(this, recordSkins);
        if (recordView) {
            mwdViews.addView(mView);
            if (this instanceof ESlateComposer) {
                if (((ESlateComposer) this).menuPanel != null)
                    ((ESlateComposer) this).menuPanel.microworldViewMenu.addItem(mView.viewName);
            }
        }
        return mView;
    }

    public void applyView(MicroworldView view) {
        if (microworld == null || view == null) return;
        microworld.checkActionPriviledge(container.microworld.viewActivationAllowed, "viewActivationAllowed");

        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(viewApplyTimer);
//System.out.println("applyView() view: " + view.viewName + ", view.microworldInfoSaved: " + view.microworldInfoSaved);
        if (view.microworldInfoSaved) {
///            setMinimizeButtonVisibleInternal(view.minimizeButtonVisible);
///            setMaximizeButtonVisibleInternal(view.maximizeButtonVisible);
///            setCloseButtonVisibleInternal(view.closeButtonVisible);
///            setControlBarsVisibleInternal(view.controlBarsVisible);
//            setNameChangeAllowed(view.nameChangeAllowed);
///            setControlBarTitleActiveInternal(view.controlBarTitleActive);
///            setHelpButtonVisibleInternal(view.helpButtonVisible);
///            setPinButtonVisibleInternal(view.pinButtonVisible);
///            setInfoButtonVisibleInternal(view.infoButtonVisible);
///            setResizeAllowedInternal(view.resizeAllowed);
            setMoveAllowedInternal(view.moveAllowed);
//            setComponentInstantiationAllowed(view.componentInstantiationAllowed);
//            setComponentRemovalAllowed(view.componentRemovalAllowed);
            setMwdBgrdChangeAllowedInternal(view.mwdBgrdChangeAllowed);
//            setMwdStorageAllowed(view.mwdStorageAllowed);
            setPlugConnectionChangeAllowedInternal(view.plugConnectionChangeAllowed);
//            setMwdTitleEnabled(view.mwdTitleEnabled);

            setMicroworldBorder(view.borderType, view.borderColor, view.borderIcon, view.getMicroworldBorderInsets());

            if (view.oldFormatIconToBeStretched && view.backgroundIconDisplayMode == ImageChooser.FIT_IMAGE) {
                NewRestorableImageIcon backgroundIcon = view.getBackgroundIcon(mwdViews, currentlyOpenMwdFile);
                java.awt.Image stretchedImage = backgroundIcon.getImage().getScaledInstance(container.scrollPane.getViewport().getExtentSize().width, container.scrollPane.getViewport().getExtentSize().height, java.awt.Image.SCALE_DEFAULT);
                backgroundIcon.getImage().flush();
                backgroundIcon.setImage(stretchedImage);
                view.setBackgroundIcon(backgroundIcon);
                view.oldFormatIconToBeStretched = false;
            }

            setMicroworldBackground(view.backgroundType, view.backgroundColor, view.getBackgroundIcon(mwdViews, currentlyOpenMwdFile), view.backgroundIconDisplayMode);
            /* The following is necessary, so that the current view's icon is not saved, whenever the
             * microworld is saved.
             */
            currentView.bgrIconFileName = view.bgrIconFileName;

            setOuterBorder(view.outerBorderType);
//\            setMenuBarVisibleInternal(view.menuBarVisible);
//            setMwdPopupEnabled(view.mwdPopupEnabled);
            setOutlineDragEnabledInternal(view.outlineDragEnabled);
//            setComponentBarEnabled(view.componentBarEnabled);
    //        setComponentFrozenStateChangeAllowed(view.componentFrozenStateChangeAllowed);
            setComponentActivationMethodChangeAllowedInternal(view.componentActivationMethodChangeAllowed);
//\            setMenuSystemHeavyWeightInternal(view.menuSystemHeavyWeight);
            setMwdResizableInternal(view.mwdResizable);
            setMwdAutoExpandableInternal(view.mwdAutoExpandable);
            setMwdAutoScrollableInternal(view.mwdAutoScrollable);
            setComponentsMoveBeyondMwdBoundsInternal(view.componentsMoveBeyondMwdBounds);
            setDesktopDraggableInternal(view.desktopDraggable);
            setActiveComponentHighlightedInternal(view.activeComponentHighlighted);
            setEnsureActiveComponentAlwaysVisibleInternal(view.ensureActiveComponentVisible);
            setMicroworldPrintAllowedInternal(view.mwdPrintAllowed);
            setPageSetupAllowedInternal(view.mwdPageSetupAllowed);
            setComponentPrintAllowedInternal(view.componentPrintAllowed);
            setComponentMaximizeAllowedInternal(view.componentMaximizeAllowed);
            setComponentMinimizeAllowedInternal(view.componentMinimizeAllowed);

            scrollPane.getViewport().setViewPosition(new Point(view.viewPositionX, view.viewPositionY));
            scrollPane.setVerticalScrollBarPolicy(view.verticalScrollbarPolicy);
            scrollPane.setHorizontalScrollBarPolicy(view.horizontalScrollbarPolicy);
// To be included in the code
//            if (view.desktopWidth != -1 && view.desktopHeight != -1)
//                setMicroworldSize(new Dimension(view.desktopWidth, view.desktopHeight));
        }

        ESlateComponent[] comps = mwdComponents.components.toArray();
        for (int i=0; i<view.desktopItemInfo.length; i++) {
            DesktopItemViewInfo compoInfo = view.desktopItemInfo[i];
            String componentName = compoInfo.componentName;
            // Find the ESlateComponent to which this desktopItemInfo applies.
            ESlateComponent component = null;
            for (int k=0; k<comps.length; k++) {
                if (comps[k] != null && comps[k].getName().equals(componentName)) {
                    component = comps[k];
                    comps[k] = null;
                    break;
                }
            }
            if (component == null)
                continue;

            ESlateInternalFrame fr = component.frame;
            if (fr != null) {
                ComponentViewInfo info = (ComponentViewInfo) compoInfo;
                fr.applyState(info.frameState, false); //true); //false
            }else{
                InvisibleComponentViewInfo info = (InvisibleComponentViewInfo) compoInfo;
                UILessComponentIcon icon = component.icon;
//                System.out.println("icon: " + icon + ", info: " + info);
                icon.applyState(info.iconState);
            }
        }

        /* All these components for which no DesktopItemInfo has been recorded in the
         * applied view, make them visible.
         */
        for (int i=0; i<comps.length; i++) {
            if (comps[i] != null) {
                try{
                    comps[i].desktopItem.setIcon(true);
                }catch (PropertyVetoException exc) {}
            }
        }

        if (view.microworldInfoSaved) {
            ESlateComponent c = mwdComponents.getComponent(view.activeComponentName);
//    System.out.println("applyView() view.activeComponentName: " + view.activeComponentName + ", component: " + c);
            if (c != null) {
                try{
                    c.setActive(true);
                }catch (PropertyVetoException exc) {}
            }
        }

/*      The ESlateInternalFrames themselves restore their modality when switching views.
        mwdComponents.resetModalFrame();
        if (view.modalFrameName != null) {
            ESlateInternalFrame f = mwdComponents.getComponentFrame(view.modalFrameName);
            if (f != null)
                f.setModal(true);
        }
*/
        if (!soundListenerMap.containsListenerFor(MicroworldListener.class, "activeViewChanged"))
            playSystemSound(SoundTheme.VIEW_CHANGED_SOUND);
        microworld.fireActiveViewChanged(new MicroworldViewEvent(microworld, view));

        pm.stop(viewApplyTimer);
        pm.displayElapsedTime(viewApplyTimer, "", "ms");
    }

    public MicroworldViewList getViewList() {
        return mwdViews;
    }

    /** Displays the help dialog for E-Slate.
     */
    public void showHelp() {
        HelpSet hs = HelpSetLoader.load(ESlateContainer.class);
        final HelpSystemViewer hv = new HelpSystemViewer(hs, 750, 500);
        ESlateContainerUtils.showDialog(hv, null, false);
        hv.setTitle(container.containerBundle.getString("ESlateHelpTitle"));

        // ESCAPE HANDLER
        hv.getRootPane().registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                hv.dispatchEvent(new WindowEvent(hv, WindowEvent.WINDOW_CLOSING));
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    /** Displays the help for the currently open microworld.
     */
    public void showMicroworldHelp() {
        if (microworld == null) return;
        microworld.eslateMwd.getESlateHandle().showHelpWindow();
    }

/*    void addUILessComponent(UILessComponentIcon icon, Rectangle bounds) {
        lc.add(icon, LayerInfo.DEFAULT_LAYER_Z_ORDER);
        icon.setSize(bounds.width, bounds.height);
        icon.setLocation(bounds.x, bounds.y);
        java.awt.FontMetrics fm = icon.nameArea.getGraphics().getFontMetrics();
        Insets insets = icon.nameArea.getBorder().getBorderInsets(icon.nameArea);
//        icon.nameArea.setSize(fm.stringWidth(icon.getName())+insets.left+insets.right, fm.getHeight()+insets.top+insets.bottom);
        icon.adjustNameAreaSize();
        icon.setVisible(true);
        icon.validate();
    }
*/
    void addUILessComponent(UILessComponentIcon icon) {
        lc.add(icon, LayerInfo.ICON_LAYER_Z_ORDER);
//        java.awt.FontMetrics fm = icon.nameArea.getGraphics().getFontMetrics();
//        Insets insets = icon.nameArea.getBorder().getBorderInsets(icon.nameArea);
//        icon.nameArea.setSize(fm.stringWidth(icon.getName())+insets.left+insets.right, fm.getHeight()+insets.top+insets.bottom);
        icon.setLocation(nextIconLocation.x, nextIconLocation.y); //scrollPane.getViewport().getViewPosition().x+nextIconLocation.x, nextIconLocation.y);
        icon.adjustNameAreaSize();
        if (!loadingMwd && !scrollPane.getViewport().getViewRect().contains(nextIconLocation)) {
            scrollPane.getViewport().setViewPosition(nextIconLocation);
        }
        findNextIconLocation(icon);
        icon.setVisible(true);
        icon.validate();
    }

    private void findNextIconLocation(UILessComponentIcon icon) {
        nextIconLocation.y = nextIconLocation.y + icon.getSize().height;
        if (nextIconLocation.y > scrollPane.getViewport().getSize().height-icon.getSize().height-20) {
            nextIconLocation.y = 30;
            nextIconLocation.x = nextIconLocation.x + UILessComponentIcon.WIDTH + 10;
            if ((nextIconLocation.x + UILessComponentIcon.WIDTH) > scrollPane.getViewport().getSize().width)
                nextIconLocation.x = 20;
        }
    }

    public boolean componentOnDesktop(ESlateHandle handle) {
        return (mwdComponents.indexOf(handle.getComponent()) != -1);
    }

    /* Activates the Look&Feel which is registered with the specified user name.
     */
    public final boolean setESlateLAF(String lfName) {
        if (microworld != null)
            microworld.checkActionPriviledge(microworld.mwdLAFChangeAllowed, "mwdLAFChangeAllowed");

        String lfClassName = installedLAFs.getClassName(lfName);
        if (lfClassName == null) {
            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg47") + lfName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try{
            Class lfClass = Class.forName(lfClassName);
            return setESlateLAF(lfClass);
        }catch (ClassNotFoundException e3){
            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg48") + '\"' + lfClassName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /** Returns the user names of the installed L&Fs
     */
    public String[] getInstalledLAFNames() {
        return installedLAFs.getNames().toArray();
    }

    /** Gets the name under which a L&F is registered in the Container. The
     * class of the L&F is supplied.
     */
    public String getNameFromInstalledLookAndFeel(Class lfClass) {
        String lfClassName = lfClass.getName();
        String lfName = installedLAFs.getName(lfClassName);
        return lfName;
    }

    /** Given the name under which a component is registered in the ESlateContainer, return
     * the Java class of the component.
     */
    public Class getRegisteredLookAndFeelClass(String registeredLookAndFeelName) {
        String className = installedLAFs.getClassName(registeredLookAndFeelName);
        if (className == null)
            return null;
        try{
            return Class.forName(className);
        }catch (ClassNotFoundException exc) {
            return null;
        }
    }

    /** Activates the Look&Feel, whose class is provided.
     */
    public final boolean setESlateLAF(Class lfClass) {
        if (microworld != null)
            microworld.checkActionPriviledge(microworld.mwdLAFChangeAllowed, "mwdLAFChangeAllowed");
        return setESlateLAF(lfClass, true);
    }

    protected boolean setESlateLAF(Class lfClass, boolean updateContainerProperties) {
        if (lfClass == null)
            return false;
        if (lfClass.getName().equals(UIManager.getLookAndFeel().getClass().getName()))
            return false;
        if (microworld != null)
            microworld.checkActionPriviledge(microworld.mwdLAFChangeAllowed, "mwdLAFChangeAllowed");

        Color prevLAFDesktopColor = getLAFDesktopColor();
        String lfClassName = lfClass.getName();
        try{
//System.out.println("setESlateLAF() lfClassName: " + lfClassName);
            UIManager.setLookAndFeel(lfClassName);
            SwingUtilities.updateComponentTreeUI(getTopLevelAncestor());
            if (container.splitPane != null && container.splitPane.leftPanel != null)
                SwingUtilities.updateComponentTreeUI(container.splitPane.leftPanel);
            if (consoles != null && consoles.consoleDialog != null)
                SwingUtilities.updateComponentTreeUI(consoles.consoleDialog);
//            if (loc != null)
//                SwingUtilities.updateComponentTreeUI(loc.consoleDialog);
            if (uiDialog != null)
                SwingUtilities.updateComponentTreeUI(uiDialog);
            if (fileDialog != null)
                SwingUtilities.updateComponentTreeUI(fileDialog);
            if (iconFileDialog != null)
                SwingUtilities.updateComponentTreeUI(iconFileDialog);
//            for (int i=0; i<listenerDialogList.size(); i++)
//                SwingUtilities.updateComponentTreeUI((java.awt.Frame) listenerDialogList.listenerEditors.at(i));
//            if (javaEditor != null)
//                SwingUtilities.updateComponentTreeUI(javaEditor);
            if (layerDialog != null)
                SwingUtilities.updateComponentTreeUI(layerDialog);
//                SwingUtilities.updateComponentTreeUI(scriptDialog.scriptArea);

/*\
            if (mwdPopupMenu != null)
                SwingUtilities.updateComponentTreeUI(mwdPopupMenu);
            if (invisibleComponentPopupMenu != null)
                SwingUtilities.updateComponentTreeUI(invisibleComponentPopupMenu);
            if (componentPopupMenu != null)
                SwingUtilities.updateComponentTreeUI(mwdPopupMenu);
            if (componentBar != null) {
                SwingUtilities.updateComponentTreeUI(componentBar);
                if (componentBar.iconPopupMenu != null)
                    SwingUtilities.updateComponentTreeUI(componentBar.iconPopupMenu);
            }
*/
            emptyContainerBgrColor = UIManager.getColor("controlShadow");
            if (microworld != null)
                microworld.eslateMwd.updateLookAndFeel();
            else{
                if (lc != null)
                    lc.setBackground(emptyContainerBgrColor);
            }

            /* When switching L&F, the default color of the desktop is changed to.
             * The rule is that under the windows L&F the color of the windows desktop
             * is used. Under any other L&F the default color of the L&F for the
             * DesktopPane is used.
             * If the desktop currently has the desktop color of the previous LAF, then
             * its color is changed during L&F switching. Also if the
             * 'defaultContainerBgrColor' was equal to the default desktop color of the
             * previous LAF, then 'defaultContainerBgrColor' is updated too.
             */
            if (getBackgroundColor() != null && getBackgroundColor().equals(prevLAFDesktopColor))
                setBackgroundColor(getLAFDesktopColor());
            if (defaultContainerBgrColor.equals(prevLAFDesktopColor))
                defaultContainerBgrColor = getLAFDesktopColor();

            revalidate();
            repaint();
            if (updateContainerProperties) {
                container.updateContainerPropertiesSection();
            }
            return true;
        }catch (UnsupportedLookAndFeelException e1){
            String lfName = installedLAFs.getName(lfClassName);
            if (lfName != null)
                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg50") + lfName + containerBundle.getString("ContainerMsg51"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            else
                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg50") + lfClassName + containerBundle.getString("ContainerMsg51"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (IllegalAccessException e2){
            String lfName = installedLAFs.getName(lfClassName);
            if (lfName != null)
                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg49") + lfName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            else
                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg49") + lfClassName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (InstantiationException e3){
            String lfName = installedLAFs.getName(lfClassName);
            if (lfName != null)
                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg49") + lfName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            else
                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg49") + lfClassName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (ClassNotFoundException e3){
            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg48") + '\"' + lfClassName + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public void updateUI() {
        /* This method is part of an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (microworld != null)
            microworld.checkActionPriviledge(microworld.mwdLAFChangeAllowed, "mwdLAFChangeAllowed");
        super.updateUI();
    }

    private Color getLAFDesktopColor() {
        if (UIManager.getLookAndFeel().getClass().getName().equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"))
            return java.awt.SystemColor.desktop;
        else
            return UIManager.getColor("desktop");
    }

    public ESlateConsoles getConsoles() {
        return consoles;
    }

    /** Returns the SoundFileChooser of E-Slate. If the file chooser has not already
     *  been initialized, this method initializes it first.
     */
    public SoundFileChooser getSoundFileChooser() {
        if (soundFileChooser == null)
            soundFileChooser = new SoundFileChooser();
        return soundFileChooser;
    }

    /** A SoundTheme file contains a number of sounds that constitute the SoundTheme.
     *  The format of the SoundTheme structured storage file is the following: at the
     *  root directory the list of the sounds is contained in the file 'SoundList'. This
     *  list maps the SoundTheme's sounds to the actual sound files in the same archieve.
     *  The subdirectory 'sounds' contains the actual sound files. Each sound file has the
     *  same name as the file it was read from, when the sound was first included in the
     *  SoundTheme.
     *  When saving a SoundTheme, first we check if the SoundTheme file already exists.
     *  If it does not exist, it is created and the sound files are copied into it. If it
     *  exists, then we have to check which of the files of the SoundTheme contained in
     *  the structured storage file will be retained, which will be deleted and which
     *  will be overriden.
     */
    public void saveSoundTheme(SoundTheme theme, String fileName, String originThemeFile) {
        // This code fails miserably when the original theme file and the new
        // theme file are the same. Detect this case and save to a temporary
        // file, which will then be copied to its final destination.
        boolean haveTempFile = false;
        File tempFile = null;
        String actualFileName = null;
        try {
          if (new File(fileName).getAbsolutePath().equals(new File(originThemeFile).getAbsolutePath())) {
            haveTempFile = true;
            tempFile = File.createTempFile("temp", null);
            tempFile.deleteOnExit();
            actualFileName = fileName;
            fileName = tempFile.getAbsolutePath();
          }
        } catch (Exception e) {
          e.printStackTrace();
          haveTempFile = false;
          tempFile = null;
          actualFileName = null;
        }

        SoundTheme oldSoundTheme = null;
        StructFile soundThemeFile = null;
        boolean fileExists = false;
        File file = new File(fileName);
        try{
            if (tempFile == null && file.exists()) {
                soundThemeFile = new StructFile(fileName, StructFile.OLD);
                fileExists = true;
            }else{
                soundThemeFile = new StructFile(fileName, StructFile.NEW);
                try{
                    soundThemeFile.createDirectory("sounds");
                }catch (IOException exc) {
                    ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg62") + fileName + containerBundle.getString("Quote"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }catch (IOException exc) {
            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg62") + fileName + containerBundle.getString("Quote"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (fileExists) {
            try{
                ObjectInputStream ois = new ObjectInputStream(soundThemeFile.openInputFile("SoundList"));
                oldSoundTheme = (SoundTheme) ois.readObject();
//System.out.println("oldSoundTheme: " + oldSoundTheme);
                ois.close();
            }catch (Throwable thr) {
                thr.printStackTrace();
            }
            if (oldSoundTheme != null)
                if (!deleteObsoleteSounds(theme, oldSoundTheme, soundThemeFile))
                    return;
        }
        StructFile originStructFile = null;
        try{
            if (originThemeFile != null)
                originStructFile = new StructFile(originThemeFile, StructFile.OLD);
        }catch (IOException exc) {}
        saveNewSounds(theme, oldSoundTheme, soundThemeFile, originStructFile);
        try{
            if (originStructFile != null)
                originStructFile.close();
        }catch (IOException exc) {
            System.out.println("Unable to close sound theme  structFile: " + originStructFile.getFile());
        }
        try{
            soundThemeFile.close();
        }catch (IOException exc) {
            System.out.println("Unable to close sound theme  structFile: " + soundThemeFile.getFile());
        }

        // Copy the temporrary file to its final destination.
        if (tempFile != null) {
          try {
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(tempFile));
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(actualFileName));
            int c;
            while ((c=is.read()) >= 0) {
              os.write(c);
            }
            os.close();
            is.close();
            tempFile.delete();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
    }

    /** It accepts a StructFile which contains the SoundTheme 'oldTheme' and removes from
     *  this file all the sounds of the 'oldTheme', which are not present in the 'theme'.
     */
    private boolean deleteObsoleteSounds(SoundTheme theme, SoundTheme oldTheme, StructFile structFile) {
//System.out.println("deleteObsoleteSounds() theme: " + theme + "\noldTheme: " + oldTheme);
        try{
            structFile.changeToRootDir();
            structFile.changeDir("sounds");
        }catch (IOException exc) {
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parentComponent);
            if (topLevelFrame == null && Frame.class.isAssignableFrom(parentComponent.getClass()))
                topLevelFrame = (Frame) parentComponent;
            DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg62") + structFile.getFile().getAbsolutePath() + containerBundle.getString("Quote"));
            dialog.appendThrowableStackTrace(exc);
//            ESlateContainerUtils.showDialog(dialog, parentComponent, true);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            return false;
        }
        SystemSound[] oldThemeSounds = oldTheme.getThemeSounds();
        for (int i=0; i<oldThemeSounds.length; i++) {
            SystemSound oldSound = oldThemeSounds[i]; //oldTheme.getSound(i);
            String oldSoundFileName = (String) oldSound.getValue();
            if (oldSoundFileName != null) {
                String soundFileName = (String) theme.getSound(i).getValue();
//System.out.println("deleteObsoleteSounds() soundFileName: " + soundFileName + ", oldSoundFileName: " + oldSoundFileName);
                if (soundFileName == null || !soundFileName.equals(oldSoundFileName)) {
                    try{
                        /* The sound file will be deleted only of there is no other SystemSound
                         * in the SoundTheme file, that uses this sound file.
                         */
//System.out.println("i: " + i + ", getSoundCount(" + oldSoundFileName + "): " + oldTheme.getSoundCount(oldSoundFileName));
                        oldSound.setValue(null);
                        if (oldTheme.getSoundCount(oldSoundFileName) == 0) {
                            structFile.deleteFile(oldSoundFileName);
//System.out.println("Deleting sound " + oldSoundFileName + " from " + structFile.getFile().getAbsolutePath());
                        }else{
//System.out.println("Bypassing oldSoundFileName: " + oldSoundFileName);
                        }
                    }catch (IOException exc) {
                        exc.printStackTrace();
                        System.out.println("Error while saving sound theme: unable to delete old sound " + oldSoundFileName);
                    }
                }
            }
        }
        try{
            structFile.changeToParentDir();
        }catch (Throwable thr) {}
        return true;
    }

    /** saveNewSounds() pressumes the deleteObsoleteSounds() has already been called, so that
     *  any sounds of the previous SoundTheme (if exists), which are not present in the
     *  new SoundTheme, have been removed from the structured storage file.
     *  @param theme The sound theme to be saved.
     *  @param oldTheme The sound theme contained in the theme file, where 'theme' is to be saved.
     *  @param structFile The structured storage file in which 'theme' will be saved.
     *  @param originStructFile The structured storage file which contains 'theme', if exists. This
     *                          is the file from which sounds will be copied to 'structFile'.
     */
    private void saveNewSounds(SoundTheme theme, SoundTheme oldTheme, StructFile structFile, StructFile originStructFile) {
        try{
            structFile.changeToRootDir();
            ObjectOutputStream oos = new ObjectOutputStream(structFile.openOutputFile("SoundList"));
            oos.writeObject(theme);
            oos.flush();
            oos.close();
            structFile.changeDir("sounds");
        }catch (IOException exc) {
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parentComponent);
            if (topLevelFrame == null && Frame.class.isAssignableFrom(parentComponent.getClass()))
                topLevelFrame = (Frame) parentComponent;
            DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg62") + structFile.getFile().getAbsolutePath() + containerBundle.getString("Quote"));
            dialog.appendThrowableStackTrace(exc);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
        }
        for (int i=SoundTheme.ERROR_SOUND; i<=SoundTheme.COMPONENT_MAXIMIZED_SOUND; i++) {
//System.out.println("Getting sound: " + i);
            SystemSound sound = theme.getSound(i);
            String soundFileName = (String) sound.getValue();
            String oldSoundFileName = null;
            if (oldTheme != null)
                oldSoundFileName = (String) oldTheme.getSound(i).getValue();
//System.out.println("i: " + i + ", soundFileName: " + soundFileName + ", oldSoundFileName: " + oldSoundFileName);
            if (soundFileName != null && oldSoundFileName == null) { //sound.modified &&
                if (sound.pathToSound != null) {
                    File f = new File(sound.pathToSound);
                    if (f.exists()) {
                        try{
//System.out.println("Copying external file: " + f);
                            structFile.copyFile(f);
                            sound.pathToSound = null;
                        }catch (IOException exc) {
                            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parentComponent);
                            if (topLevelFrame == null && Frame.class.isAssignableFrom(parentComponent.getClass()))
                                topLevelFrame = (Frame) parentComponent;
                            DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                            dialog.setMessage("Error while saving sound theme:  unable to copy external sound file " + soundFileName + " into the structured storage file");
                            dialog.appendThrowableStackTrace(exc);
                            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
                        }
                    }
                }else{
                    if (originStructFile != null) {
//System.out.println("Copying internal sound file: " + sound.getValue());
                        try{
                            Vector path = new Vector();
                            path.add("sounds");
                            path.add(sound.getValue());
                            byte[] buff = new byte[1024];
                            BufferedInputStream bis = new BufferedInputStream(originStructFile.openInputFile(path, false));
                            BufferedOutputStream bos = new BufferedOutputStream(structFile.openOutputFile((String) sound.getValue()));
                            int fileSize = (int) originStructFile.findEntry(path, false).getSize();
                            int totalBytesRead = 0;
//System.out.println("fileSize: " + fileSize);
                            while (totalBytesRead != fileSize) {
//System.out.println("Reading 1024 bytes starting from: " + totalBytesRead);
                                int byteCount = bis.read(buff, 0, 1024);
//System.out.println("Read " + byteCount + " bytes");
                                totalBytesRead = totalBytesRead + byteCount;
                                bos.write(buff, 0, byteCount);
                            }
                            bis.close();
                            bos.flush();
                            bos.close();
                        }catch (Throwable thr) {
                            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parentComponent);
                            if (topLevelFrame == null && Frame.class.isAssignableFrom(parentComponent.getClass()))
                                topLevelFrame = (Frame) parentComponent;
                            DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                            dialog.setMessage("Error while saving sound theme:  unable to copy sound file " + sound.getValue() + " from structured storage file " + originStructFile.getFile().getAbsolutePath() + " into the new structured storage file");
                            dialog.appendThrowableStackTrace(thr);
                            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
                        }
                    }
                }
            }
        }
        try{
            structFile.changeToParentDir();
        }catch (Throwable thr) {}
    }

    private void readESlateSoundTheme() {
        String pathToESlateSoundTheme = getESlateSoundThemePath();
        try{
            if (! new File(pathToESlateSoundTheme).exists())
                return;
            StructFile soundThemeFile = new StructFile(pathToESlateSoundTheme, StructFile.OLD);
            ObjectInputStream ois = new ObjectInputStream(soundThemeFile.openInputFile("SoundList"));
//            if (currentSoundTheme == null)
//                currentSoundTheme = new SoundTheme(this);
            currentSoundTheme = (SoundTheme) ois.readObject();
            currentSoundTheme.container = this;
            ois.close();
        }catch (Throwable thr) {
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parentComponent);
            if (topLevelFrame == null && Frame.class.isAssignableFrom(parentComponent.getClass()))
                topLevelFrame = (Frame) parentComponent;
            DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg63") + pathToESlateSoundTheme + containerBundle.getString("Quote"));
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
        }
    }

    /** Plays one of the sounds of the current ESlate sound theme.
     *  @param id The id of the sound. See SoundTheme for the list of the valid sound ids.
     *  @see    gr.cti.eslate.base.container.SoundTheme
     */
    public ESlateSound playSystemSound(int id) {
        if (currentSoundTheme == null) return null;
        SystemSound sound = currentSoundTheme.getSound(id);
        if (sound == null) return null;
        if (sound.getValue() == null) return null;

        Vector path = new Vector();
        path.add("sounds");
        path.add(sound.getValue());
        String systemSoundThemePath = getESlateSoundThemePath();
        if (systemSoundThemePath == null) return null;
        try{
            ESlateSound eslSound = SoundUtils.playSound(new StructFile(systemSoundThemePath, StructFile.OLD), systemSoundThemePath, path);
            soundListenerMap.addPlayingSound(eslSound);
            eslSound.addSoundListener(new ESlateSoundListener() {
                public void soundStopped(ESlateSoundEvent e) {
    //                System.out.println("ET: " + (System.currentTimeMillis()-start));
                    soundListenerMap.removePlayingSound((ESlateSound) e.getSource());
                }
            });
            return eslSound;
        }catch (Throwable thr) {
            System.out.println("Unable to play sound from the ESlate sound theme");
            return null;
        }
    }

    /** Terminates E-Slate properly.
     */
    public void exit(final int exitCode) {
        ESlateSound eslSound = playSystemSound(SoundTheme.EXIT_SOUND);
        updateContainerSizeSection();
        updateContainerPropertiesSection();
        containerUtils.deleteTmpDir();
        if (eslSound != null) {
            eslSound.addSoundListener(new ESlateSoundListener() {
                public void soundStopped(ESlateSoundEvent e) {
                    finalizeContainer();
                    System.exit(exitCode);
                }
            });
        }else{
            finalizeContainer();
            System.exit(exitCode);
        }
    }

    /** This method returns the GlassPane used by ESlateContainer. The GlassPane is a JPanel which
     *  is displayed:
     *  <ul>
     *  <li> above all non-modal components, when a modal component is show(activated).
     *  <li> optionally, while a microworld is being loaded.
     *  </ul>
     *  By default the GlassPane has a semi-transparent background color and does not have
     *  any contents. However its appearance and contents can be changed.
     */
    public GlassPane getGlassPane() {
        if (glass == null)
            createGlassPane();
        return glass;
    }

    /** Adjusts the visibility of the GlassPane of the ESlateContainer */
    public void setGlassPaneVisible(boolean visible) {
        if (glass == null)
            glass = createGlassPane();
        if (glassFrame == null) {
            glassFrame = new GlassInternalFrame(this);
            glassFrame.getContentPane().add(glass);
        }
        if (glassFrame.isVisible() == visible) return;
        if (visible) {
            glassFrame.setSize(lc.getSize());
            glassFrame.setLocation(lc.getLocation());
//System.out.println("bottom layer: " + mwdLayers.getBottomLayerLevel() + ", top layer: " + mwdLayers.getTopLayerLevel() + ", default: " + LayerInfo.DEFAULT_LAYER_Z_ORDER);
            lc.add(glassFrame, LayerInfo.GLASS_FRAME_LAYER_Z_ORDER); //new Integer(mwdLayers.getTopLayerLevel()+1)); //new Integer(LayerInfo.DEFAULT_LAYER_Z_ORDER.intValue() + 1)); //.MODAL_LAYER);//, LayerInfo.DEFAULT_LAYER_Z_ORDER); //
            glassFrame.setVisible(true);
//System.out.println("Setting glasspane visible");
        }else{
            glassFrame.setVisible(false);
            lc.remove(glassFrame);
        }
    }

    /** Reports the visibility of the GlassPane of the ESlateContainer */
    public boolean isGlassPaneVisible() {
        if (glass == null)
            return false;
        return glassFrame.isVisible();
    }

    /** Instantiates the GlassPane. */
    private GlassPane createGlassPane() {
        return new GlassPane();
    }

    /** Determines whether the GlassPane will be used while a microworld is being loaded. By default
     *  it is not used.
     */
    public void setGlassPaneVisibleOnMicroworldLoad(boolean visible) {
        if (glassPaneVisibleOnMicroworldLoad == visible) return;
        glassPaneVisibleOnMicroworldLoad = visible;
    }

    public boolean isGlassPaneVisibleOnMicroworldLoad() {
        return glassPaneVisibleOnMicroworldLoad;
    }

    public final void grantAccess(String key) {
        if (microworld == null) return;
        microworld.grantAccess(key);
    }

    public final void revokeAccess() {
        if (microworld == null) return;
        microworld.revokeAccess();
    }

    public LayerInfo getMicroworldLayers() {
        return mwdLayers;
    }

    public void verifySettingChangePriviledge() {
        if (microworld == null) return;
        microworld.checkSettingChangePriviledge();
    }

    /** Returns the state of the requested component. */
    private StorageStructure getComponentState(ESlateComponent component) {
        if (component == null) return null;
        Object object = component.handle.getComponent();
        boolean isExternalizable = Externalizable.class.isAssignableFrom(object.getClass());
        boolean isSerializable = Serializable.class.isAssignableFrom(object.getClass());
//System.out.println("isExternalizable: " + isExternalizable + ", isSerializable: " + isSerializable + ", object: " + object.getClass());
        if (!isExternalizable && !isSerializable) {
            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("Component") + " \"" + component.handle.getComponentName() + "\" " + containerBundle.getString("ContainerMsg67"), containerBundle.getString("Warning"), JOptionPane.WARNING_MESSAGE);
            return null;
        }

        ESlateFieldMap2 fm = new ESlateFieldMap2(COMPONENT_STATE_VERSION);
        fm.put("Component class name", object.getClass().getName());
        fm.put("Externalizable", isExternalizable);
        try{
            fm.put("Component files", component.handle.packAllPrivateFiles());
        }catch (Throwable thr) {
            thr.printStackTrace();
        }

        try{
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            BufferedOutputStream buffStream = new BufferedOutputStream(byteStream);
            ObjectOutputStream stream = new ObjectOutputStream(buffStream);
            if (isExternalizable)
                ((Externalizable) object).writeExternal(stream);
            else
                stream.writeObject(object);
            stream.flush();
            fm.put("Component state", byteStream.toByteArray());
            stream.close();
        }catch (Throwable thr) {
//            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("Component") + " \"" + component.handle.getComponentName() + "\" " + containerBundle.getString("ContainerMsg67"), containerBundle.getString("Warning"), JOptionPane.WARNING_MESSAGE);
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("Component") + " \"" + component.handle.getComponentName() + "\" " + containerBundle.getString("ContainerMsg68"));
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            return null;
        }
        try{
            fm.put("Platform state", component.handle.saveSubTreeInfo());
            if (component.frame != null)
                fm.put("E-Slate state", component.frame.recordState(true));
            else
                fm.put("E-Slate state", component.icon.recordState());
			// Load the script listener sources, if needed.
			if (currentlyOpenMwdFile != null) {
				ScriptUtils.getInstance().loadMicroworldScripts(this, currentlyOpenMwdFile);
			}
            fm.put("Script listener state", componentScriptListeners.saveComponentScriptListeners(component.handle));
        }catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("Component") + " \"" + component.handle.getComponentName() + "\" " + containerBundle.getString("ContainerMsg68"));
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            return null;
        }

        return fm;
    }

    /** Saves the specified component to a file. */
    public void saveComponent(ESlateComponent component, String fileName) {
        if (fileName == null || component == null) return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        StorageStructure fm = getComponentState(component);
        if (fm == null) {
            setCursor(Cursor.getDefaultCursor(), false);
            return;
        }

        ObjectOutputStream oos = null;
        StructFile componentFile = null;
		// Set the 'saveScriptListenerSource' static flag of ScriptListener class, so as the
		// sources of the scripts to be saved too.
		ScriptListener.saveScriptListenerSource = true;
        try{
            componentFile = new StructFile(fileName, StructFile.NEW);
            fm.put("Sound state", soundListenerMap.saveMap(componentFile, component.handle));
            oos = new ObjectOutputStream(new BufferedOutputStream(componentFile.openOutputFile("Component State")));
            oos.writeObject(fm);
            oos.flush();
            oos.close();
            componentFile.close();
        }catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("Component") + " \"" + component.handle.getComponentName() + "\" " + containerBundle.getString("ContainerMsg68"));
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            if (oos != null) {
                try{
                    oos.close();
                }catch (Throwable thr1) {}
            }
        }
		ScriptListener.saveScriptListenerSource = false;
        setCursor(Cursor.getDefaultCursor(), false);
    }

    /** Copies the specified component to the E-Slate clipboard.
     */
    public void copyComponent(ESlateComponent component) {
        if (component == null) return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        StorageStructure fm = getComponentState(component);
        ObjectOutputStream oos = null;
		// Set the 'saveScriptListenerSource' static flag of ScriptListener class, so as the
		// sources of the scripts to be saved too.
		ScriptListener.saveScriptListenerSource = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
        try{
            oos = new ObjectOutputStream(new BufferedOutputStream(baos));
            oos.writeObject(fm);
            oos.flush();
            oos.close();
            byte[] state = baos.toByteArray();
            gr.cti.eslate.utils.ESlateClipboard.setContents(state);
        }catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("Component") + " \"" + component.handle.getComponentName() + "\" " + containerBundle.getString("ContainerMsg81"));
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            if (oos != null) {
                try{
                    oos.close();
                }catch (Throwable thr1) {}
            }
        }
        setCursor(Cursor.getDefaultCursor(), false);
		ScriptListener.saveScriptListenerSource = false;
    }

    /** Removes a component from E-Slate after copying it to the E-Slate clipboard.
     */
    public void cutComponent(ESlateComponent component) {
        if (component == null) return;
        copyComponent(component);
        removeComponent(component, true, false);
    }

    /** Pastes a component (if exists) from the E-Slate clipboard to the current microworld. The
     *  @param enableMessages If true, the method will display information dialogs, if the
     *         clipboard is empty, or does not contain a component.
     *  @param location The desktop location where the component will be inserted. If null, the
     *         default location is used.
     */
    public ESlateComponent pasteComponent(boolean enableMessages, Point location) {
        if (microworld == null) return null;

        Object obj = gr.cti.eslate.utils.ESlateClipboard.getContents();
        if (obj == null) {
            if (enableMessages)
                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg83"), containerBundle.getString("Info"), JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        if (!byte[].class.isAssignableFrom(obj.getClass())) {
            if (enableMessages)
                ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg84"), containerBundle.getString("Info"), JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        byte[] state = (byte[]) obj;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        StorageStructure compoState = null;
        ObjectInputStream ois = null;
        ESlateComponent component = null;
        try{
            ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(state)));
            compoState = (StorageStructure) ois.readObject();
            ois.close();
            component = createComponentFromState(compoState, location);
        }catch (InsufficientPriviledgeException exc) {
            exc.printStackTrace();
        }catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg82"));
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            if (ois != null) {
                try{
                    ois.close();
                }catch (Throwable thr1) {}
            }
        }
        setCursor(Cursor.getDefaultCursor(), false);
        return component;
    }

    /** Saves the specified component to a file. */
    public ESlateComponent loadComponent(String fileName) {
        if (fileName == null) return null;
//System.out.println("loadComponent 1 mwdComponents.activeComponent: " + mwdComponents.activeComponent);

        StorageStructure state = null;
        ObjectInputStream ois = null;
        ESlateComponent component = null;
        try{
            StructFile componentFile = new StructFile(fileName, StructFile.OLD);
            ois = new ObjectInputStream(new BufferedInputStream(componentFile.openInputFile("Component State")));
            state = (StorageStructure) ois.readObject();
            ois.close();
            component = createComponentFromState(state, null);
            StorageStructure soundState = (StorageStructure) state.get("Sound state");
            if (soundState != null) {
                if (soundState.keySet().size() != 0 && currentlyOpenMwdFile == null) {
                    DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
                    dialog.setMessage(containerBundle.getString("ContainerMsg78") + " \"" + component.handle.getComponentName() + "\" " + containerBundle.getString("ContainerMsg79"));
                    dialog.setDetails(containerBundle.getString("ContainerMsg80"));
                    ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
//                    ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg78") + " \"" + component.handle.getComponentName() + "\" " + containerBundle.getString("ContainerMsg79"), containerBundle.getString("Warning"), JOptionPane.WARNING_MESSAGE);
                }else
                    soundListenerMap.loadComponentSounds(componentFile, component.handle, soundState);
            }
            componentFile.close();
            return component;
        }catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg77") + " \"" + fileName + '\"');
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            if (ois != null) {
                try{
                    ois.close();
                }catch (Throwable thr1) {}
            }
        }

        return null;
    }

    private ESlateComponent createComponentFromState(StorageStructure state, Point location) {
        if (microworld == null) return null;
        /* This method is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (!loadingMwd)
            microworld.checkActionPriviledge(microworld.isComponentInstantiationAllowed(), "componentInstantiationAllowed");

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        String componentClassName = (String) state.get("Component class name");
        boolean isExternalizable = state.get("Externalizable", true);
        Class componentClass = null;
        try{
            componentClass = Class.forName(componentClassName);
        }catch (ClassNotFoundException exc) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg69") + " \"" + componentClassName + "\" " + containerBundle.getString("ContainerMsg70"));
            dialog.appendThrowableStackTrace(exc);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            setCursor(Cursor.getDefaultCursor(), false);
            return null;
        }
         catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg71") + " \"" + componentClassName + '\"');
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            setCursor(Cursor.getDefaultCursor(), false);
            return null;
        }
        boolean isClassExternalizable = Externalizable.class.isAssignableFrom(componentClass);
        boolean isClassSerializable = Serializable.class.isAssignableFrom(componentClass);
        if (isExternalizable != isClassExternalizable) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg69") + " \"" + componentClassName + "\" " + containerBundle.getString("ContainerMsg73"));
            if (isExternalizable)
                dialog.appendToDetails(containerBundle.getString("ContainerMsg73"));
            else
                dialog.appendToDetails(containerBundle.getString("ContainerMsg74"));
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            setCursor(Cursor.getDefaultCursor(), false);
            return null;
        }

        ESlateHandle handle = null;
        byte[] compoState = (byte[]) state.get("Component state");
        StorageStructure componentFiles = (StorageStructure) state.get("Component files");
        ObjectInputStream ois = null;
        Object object = null;
        try{
            ByteArrayInputStream byteStream = new ByteArrayInputStream(compoState);
            ois = new ObjectInputStream(new BufferedInputStream(byteStream));
            if (isExternalizable) {
                object = instantiateComponent(container, componentClassName);
                if (object == null) {
                    setCursor(Cursor.getDefaultCursor(), false);
                    return null;
                }
                handle = registerComponentToESlateMicroworld(object);
                handle.unpackAllPrivateFiles(componentFiles);
                ((Externalizable) object).readExternal(ois);
            }else{
                object = ois.readObject();
                handle = registerComponentToESlateMicroworld(object);
            }
            ois.close();
        }catch (Throwable thr) {
            thr.printStackTrace();
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg75") + " \"" + componentClassName + '\"');
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            if (ois != null) {
                try{
                    ois.close();
                }catch (Throwable thr1) {}
            }
            if (handle != null)
                handle.dispose();
            setCursor(Cursor.getDefaultCursor(), false);
            return null;
        }

//System.out.println("loadComponent 2 mwdComponents.activeComponent: " + mwdComponents.activeComponent);
        StorageStructure eslateCompoState = (StorageStructure) state.get("E-Slate state");
        ESlateInternalFrame w = null;
        UILessComponentIcon icon = null;
        if (Component.class.isAssignableFrom(object.getClass())) {
            Component component = (Component) object;
            w = createComponentFrame(handle);
//            lc.add(w, LayerInfo.DEFAULT_LAYER_Z_ORDER);
            eslateCompoState.put("Selected", false);
            w.applyState(eslateCompoState, false);
            if (location != null)
                w.setLocation(location);
            else{
                int componentNum = mwdComponents.size();
                w.setLocation(scrollPane.getViewport().getViewPosition().x + 20*((componentNum+1)%10), scrollPane.getViewport().getViewPosition().y + 20*((componentNum+1)%10));
            }
            w.setVisible(true);
        }else{
            icon = new UILessComponentIcon(handle);
            lc.add(icon, LayerInfo.ICON_LAYER_Z_ORDER);
            eslateCompoState.put("Active", false);
            icon.applyState(eslateCompoState);
            icon.adjustNameAreaSize();
            if (location != null)
                icon.setLocation(location);
            else{
                icon.setLocation(nextIconLocation.x, nextIconLocation.y);
                findNextIconLocation(icon);
            }
            icon.setVisible(true);
            icon.validate();
        }
        if (this instanceof ESlateComposer)  {
            ESlateComposer composer = (ESlateComposer) this;
            composer.addComponentItemToComponentsMenu(handle.getComponentName());
            composer.addComponentItemToHelpMenu(handle);
        }

        StorageStructure platformState = (StorageStructure) state.get("Platform state");
        try{
            handle.restoreSubTreeInfo(platformState);
        }catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg76") + " \"" + handle.getComponentName() + '\"');
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
            if (handle != null)
                handle.dispose();
            setCursor(Cursor.getDefaultCursor(), false);
            return null;
        }

        DesktopItem desktopItem = null;
        if (w == null)
            desktopItem = icon;
        else
            desktopItem = w;
        ESlateComponent eslateComponent = new ESlateComponent(this, handle, (w!=null), desktopItem, object);
        attachESlateComponentListener(eslateComponent);
        mwdComponents.components.add(eslateComponent);
        playSystemSound(SoundTheme.COMPONENT_NEW_SOUND);

        try {
            eslateComponent.desktopItem.setActive(true);
        }catch (PropertyVetoException exc) {exc.printStackTrace();}

        // Update the component bar, if it is visible
        if (this instanceof ESlateComposer)  {
            ESlateComposer composer = (ESlateComposer) this;
            if (composer.componentBar != null && composer.componentBar.getParent() != null) {
                ComponentIcon ci = composer.componentBar.addComponentIcon(handle.getComponentName());
                ci.setSelection(true);
            }
        }

        StorageStructure scriptListenerState = (StorageStructure) state.get("Script listener state");
        ArrayList compoScriptListeners = (ArrayList) scriptListenerState.get("ScriptListener Array");
		// If the sources of the scripts of the microworld have not been loaded, load them now
		if (currentlyOpenMwdFile != null) {
			ScriptUtils.getInstance().loadMicroworldScripts(this, currentlyOpenMwdFile);
		}
		ScriptUtils.getInstance().scriptsLoaded = true;
        componentScriptListeners.loadComponentScriptListeners(handle, compoScriptListeners);

//System.out.println("loadComponent 6 mwdComponents.activeComponent: " + mwdComponents.activeComponent);

        ESlateContainer.this.setMicroworldChanged(true);
        setCursor(Cursor.getDefaultCursor(), false);
        return eslateComponent;

    }

    /** Creates the help for a microworld.
     */
    void createMicroworldHelp() {
        if (microworld == null) return;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        try{
            microworld.eslateMwd.addHelp();
        }catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg85"));
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
        }
        setCursor(Cursor.getDefaultCursor(), false);
    }

    /** Edits the help for a microworld.
     */
    void editMicroworldHelp() {
        if (microworld == null) return;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        try{
            microworld.eslateMwd.editHelp();
        }catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("ContainerMsg89"));
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, parentComponent, true);
        }
        setCursor(Cursor.getDefaultCursor(), false);
    }

    /** Clears the microworld's help, if it exists.
     */
    void clearMicroworldHelp() {
        if (microworld == null) return;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        microworld.eslateMwd.clearHelp();
        setCursor(Cursor.getDefaultCursor(), false);
    }

    public void showPerformanceManager() {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        if (!pm.isEnabled()) return;
        if (perfMgrDialog == null) {
            JPanel performanceMgrPanel = pm.getGUI();
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this);
            if (topLevelFrame == null && Frame.class.isAssignableFrom(parentComponent.getClass()))
                topLevelFrame = (Frame) parentComponent;
            perfMgrDialog = new PerformanceManagerDialog(this, topLevelFrame);
            perfMgrDialog.pack();
        }
        ESlateContainerUtils.showDialog(perfMgrDialog, this, false);
    }

    private void constructESlateTimers(PerformanceTimerGroup eslateTimerGroup, PerformanceTimerGroup loadGlobalGroup) {
        if (saveTimer != null) return;
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        saveTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(eslateTimerGroup, containerBundle.getString("MicroworldSaveTimer"), true);
        loadTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(eslateTimerGroup, containerBundle.getString("MicroworldLoadTimer"), true);
        mwdCloseTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(eslateTimerGroup, containerBundle.getString("MicroworldCloseTimer"), true);
        viewApplyTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(eslateTimerGroup, containerBundle.getString("ViewSwitchTimer"), true);
        loadTimerGroup = (PerformanceTimerGroup) pm.createPerformanceTimerGroup(eslateTimerGroup/*loadGlobalGroup*/, containerBundle.getString("MicroworldLoadGroup"), false);
        fileReadTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(loadTimerGroup, containerBundle.getString("FileReadTimer"), true);
        componentInstantiationTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(loadTimerGroup, containerBundle.getString("ComponentInstantiationTimer"), true);
        handleRegistrationTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(loadTimerGroup, containerBundle.getString("HandleRegistrationTimer"), true);
        frameIconTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(loadTimerGroup, containerBundle.getString("FrameIconCreationTimer"), true);
        scriptSoundLoadTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(loadTimerGroup, containerBundle.getString("ScriptSoundLoadTimer"), true);
        mwdPropertiesRestoreTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(loadTimerGroup, containerBundle.getString("MwdPropertiesRestoreTimer"), true);
        mwdLoadedListenerTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(loadTimerGroup, containerBundle.getString("MwdLoadedListenerTimer"), true);
    }

    void initializePerformanceManager() {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
//System.out.println("initializePerformanceManager() pm.isEnabled(): " + pm.isEnabled());
        if (!pm.isEnabled()) return;

        PerformanceTimerGroup eslateTimerGroup = pm.getPerformanceTimerGroup(getESlateHandle());
        PerformanceTimerGroup saveGlobalGroup = pm.getPerformanceTimerGroupByID(PerformanceManager.SAVE_STATE);
        PerformanceTimerGroup loadGlobalGroup = pm.getPerformanceTimerGroupByID(PerformanceManager.LOAD_STATE);
        if (saveTimer == null)
            constructESlateTimers(eslateTimerGroup, loadGlobalGroup);
        else{
            /* The timers are dettached only when the microworld closes. If the PM is stopped
             * and then restarted, then the timers have not been dettached, so there is no
             * need to re-attach them.
             */
            if (saveTimer.getParentCount() == 0) {
                pm.addPerformanceTimerGroup(eslateTimerGroup, saveTimer);
                pm.addPerformanceTimerGroup(eslateTimerGroup, loadTimer);
                pm.addPerformanceTimerGroup(eslateTimerGroup, mwdCloseTimer);
                pm.addPerformanceTimerGroup(eslateTimerGroup, viewApplyTimer);
                pm.addPerformanceTimerGroup(eslateTimerGroup/*loadGlobalGroup*/, loadTimerGroup);
            }
        }
        /* The timers are dettached only when the microworld closes. If the PM is stopped
         * and then restarted, then the timers have not been dettached, so there is no
         * need to re-attach them.
         */
//        if (loadTimer.getParentCount() == 1) {
//            pm.addPerformanceTimerGroup(saveGlobalGroup, saveTimer);
//            pm.addPerformanceTimerGroup(loadGlobalGroup, loadTimer);
//        }
    }

    /** Creates a copy of the currently open microworld file. The copy is saved in the tmp
     *  directory of E-Slate with the name 'microworld.bck'.
     */
    private void createCopyOfMicroworldFile() {
        if (currentlyOpenMwdFileName == null) return;
//long time = System.currentTimeMillis();
        File copyFile = new File(containerUtils.getTmpDir(), "microworld.bck");
        microworld.backupExists = new File(currentlyOpenMwdFileName).renameTo(copyFile);
        microworld.backupExists = containerUtils.copyFile(currentlyOpenMwdFileName, copyFile);
    }

    /** Restores the microworld file back-up, if it exists. The restored file is placed at
     *  the same directory as the currently open microworld file. The name of the file is
     *  the same as the name of the currently open microworld file, with the exception that
     *  before the last period there exists a '.bck' part.
     */
    private String restoreMicroworldBackup() {
        if (!microworld.backupExists) return null;
        String fileName = currentlyOpenMwdFileName;
        if (fileName == null || fileName.length() == 0)
            fileName = containerBundle.getString("BackupCopy");
        int lastPeriodIndex = fileName.lastIndexOf('.');
        if (lastPeriodIndex != -1)
            fileName = fileName.substring(0, lastPeriodIndex);
        if (fileName.endsWith(".bck"))
            fileName = fileName.substring(0, fileName.length()-4);
        String targetFileName = fileName + ".bck." + mwdFileExtensions[0];
        File restoredFile = new File(targetFileName);
        int count = 1;
        while (restoredFile.exists()) {
            targetFileName = fileName + '_' + count +".bck." + mwdFileExtensions[0];
            restoredFile = new File(targetFileName);
            count++;
        }
        File sourceFile = new File(containerUtils.getTmpDir(), "microworld.bck");
        if (containerUtils.copyFile(sourceFile.getAbsolutePath(), restoredFile))
            return targetFileName;
        return null;
    }

    public ScriptListenerMap getScriptListenerMap() {
        return componentScriptListeners;
    }

    public MicroworldView getCurrentView() {
        return currentView;
    }

    public void setFullScreen(Frame fr, boolean fullScreen) {
//System.out.println("fullScreen: " + fullScreen + ", fullScreenMode: " + fullScreenMode);
        if (fullScreenMode == fullScreen) return;
        java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        java.awt.GraphicsDevice[] gd = ge.getScreenDevices();
        java.awt.GraphicsDevice screen = null;
        if (gd.length != 0)
            screen = gd[0];
        if (screen == null)
            return;
/*        if (fullScreen) {
            if (screen.isFullScreenSupported()) {
                try{
                    fr.setUndecorated(true);
                    screen.setFullScreenWindow(fr);
                }catch (Throwable thr) {
                    thr.printStackTrace();
                    screen.setFullScreenWindow(null);
                }
            }
            // If going full screen succeeds, install a keyboard listener, so that
            // we can exit this mode.
            ActionMap containerActionMap = getActionMap();
            InputMap containerInputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ExitFullScreenModeAction action = new ExitFullScreenModeAction(this, "FullScreen");
            String actionName = (String) action.getValue(AbstractAction.NAME);
            containerActionMap.put(actionName, action);
            containerInputMap.put((KeyStroke) action.getValue(AbstractAction.ACCELERATOR_KEY),
                                  actionName
            );
        }else{
            screen.setFullScreenWindow(null);
        }
*/
        fullScreenMode = fullScreen;
    }

    public boolean isFullScreen() {
        return fullScreenMode;
    }
    public void paint(Graphics g) {
        if (!repaintManager.active)
            return;
        super.paint(g);
        if (!(this instanceof ESlateComposer) && (microworld==null || microworld.getESlateMicroworld().getComponentCount()==0)) {
        	Graphics2D g2=(Graphics2D) g;
        	g2.translate((getWidth()-tooltip.getWidth())/2,getHeight()-tooltip.getHeight()-10);
        	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.70f));
        	tooltip.paint(g2);
        }
    }

	public ESlateRepaintManager getRepaintManager() {
		return repaintManager;
	}

    protected Rectangle getHighlightRect() {
        return scrollPane.highlightRect;
    }

    protected void setHighlightRect(Rectangle rect) {
        scrollPane.highlightRect = rect;
    }

    /** This is a kind of PaintEvent which is used to signal the time that the
     *  RepaintManager should start to repaint again, after microworld loading
     *  has finished. When microworld loading starts, the RepaintManager is
     *  de-activated(no painting occurs -except when the components'
     *  paintImmediately() is called, in which case the repainting is not
     *  controlled by the RepaintManager). While the microworld is loaded
     *  PaintEvents are queued. These events have the lowest priority among
     *  all the AWT events(they are executed last). Disabling there delivery
     *  through the ESlateEventQueue increases the speed of microworld loading.
     *  To enable the re-activation of the RepaintManager, just after the
     *  microworld loading finishes (see setLoadingMwd()) an event of the
     *  following type is queued. The ESlateEventQueue receives this event after
     *  all the other PaintEvents which were posted during the microworld
     *  loading, re-activates the RepaintManager and repaints the whole E-Slate.
     */
    class MicroworldFinalPaintEvent extends java.awt.event.PaintEvent {
        public MicroworldFinalPaintEvent() {
            super(ESlateContainer.this, PaintEvent.PAINT, new Rectangle(0,0,0,0));
//System.out.println("constructed MicroworldFinalPaintEvent");
        }
    }

    class ESlateEventQueue extends EventQueue {
		Window containerWindow = null;

		protected void postMicroworldFinalRepaintEvent() {
			MicroworldFinalPaintEvent event = new MicroworldFinalPaintEvent();
			postEvent(event);
		}
        protected void dispatchEvent(java.awt.AWTEvent event) {
            // Discard any Paint events which arrive while the RepaintManager is
            // deactivated. However if the PaintEvent is a MicroworldFinalPaintEvent
            // then enable the RepaintManager and repaint the whole ESlateContainer.
        	
            try {
				if (!repaintManager.active) {
				    int id = event.getID();
				    if (id == PaintEvent.PAINT || id == PaintEvent.UPDATE) {
   //System.out.println("Paint event, instanceof MicroworldFinalPaintEvent: " + (event instanceof MicroworldFinalPaintEvent));
				        if (event instanceof MicroworldFinalPaintEvent) {
//System.out.println("ESlateEventQueue processing MicroworldFinalPaintEvent");
				            repaintManager.active = true;
   //                    ESlateContainer.this.repaint();
				            Dimension sz = ESlateContainer.this.getSize();
				            ESlateContainer.this.paintImmediately(0, 0, sz.width, sz.height);
							for (int i=0; i<repaintManager.componentsToRepaintAfterMicroworldLoad.size(); i++) {
								Component comp = (Component) repaintManager.componentsToRepaintAfterMicroworldLoad.get(i);
//							System.out.println("Repainting: "+ comp.getClass());
								comp.invalidate();
								comp.doLayout();
								if (comp instanceof JComponent) {
									((JComponent) comp).revalidate();
								}
								comp.repaint();
							}
							repaintManager.componentsToRepaintAfterMicroworldLoad.clear();
//System.out.println("Repainted the whole microworld in " + (System.currentTimeMillis()-t));
				        }
				        return;
				    }
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
				System.out.println("DUNNO!");
			}

            if (event.getID() == MouseEvent.MOUSE_PRESSED) {
                MouseEvent e = (MouseEvent) event;
				Component source = (Component) e.getSource();
//System.out.println("source: " + source);
				if (containerWindow == null) {
					containerWindow = SwingUtilities.windowForComponent(ESlateContainer.this);
				}
//				System.out.println(containerWindow.getClass() + ", source: " + source.getClass() + ", sourceWindow: " + SwingUtilities.windowForComponent(source));
				// The sources of all the MousePressed events are the Frames/Windows in which
				// the mouse was pressed. We process only those mouse pressed events which
				// happen inside the ESlateContainer's window.
				if (source == containerWindow) {
//System.out.println("source: " + source.getClass() + ", e.getPoint(): " + e.getPoint());
					Component comp = SwingUtilities.getDeepestComponentAt(containerWindow, e.getPoint().x, e.getPoint().y);
					// Here we check if the target of the MousePressed event is a menu item which
					// belong to some menu of the environment. If this is the case, then don't activate
					// any component.
					// Problem with the ESlateMenuBar (later)
					if (comp != null && comp instanceof JMenuItem) {
					}else{
						Point p = SwingUtilities.convertPoint(source, e.getPoint(), scrollPane);
//						System.out.println("p: " + p);
						if (p.x > 0 && p.y > 0) {
							activateComponent(p.x, p.y);
						}
					}
				}
            }
            super.dispatchEvent(event);
//if (event.getID() == MouseEvent.MOUSE_PRESSED)
//System.out.println("After dispatching event source: " + event.getSource());
        }
    }

	protected java.awt.Image getAppIcon() {
		if (microworld == null) return ESLATE_LOGO.getImage();
		return microworld.getMicroworldIconImage();
	}

    /**
     * Starts the ESlate's runtime environment.
     * @param args
     * @see ESlateContainer#startESlate(String[], boolean)
     */
    public static void main(String[] args) {
        startESlate(args, true);
    }
    
    /**
     * Starts the E-Slate runtime or design time environment.
     *
     * @param args
     *            the command line args with which E-Slate starts up
     * @param runtimeEnv
     *            which environment will be initialized (runtime or design time)
     */
    public void startESlate3(String[] args, boolean runtimeEnv) {
        // System.out.println("Main start");
        // long millis1= System.currentTimeMillis();
        String testMicroworldFileName = null;
        int times = 0;
        String startupMicroworldFileName = null;
        boolean fullScreenMode = false;
        /*
         * System.out.println("args: " + args); if (args != null) {
         * System.out.println("args.length: " + args.length); for (int k=0;
         * k<args.length; k++) System.out.println("Arg " + k + ": " + args[k] );
         * }
         */
        java.util.HashMap<String, String> mwdParams = new java.util.HashMap<String, String>();

        boolean readActualLaunchParameter = false;
        if (args!=null&&args.length>=1) {
            // StringBuffer argStrBuffer = new StringBuffer();
            // for (int i=0; i<args.length; i++)
            // arg
            for (int i = 0; i<args.length; i++) {
                // System.out.println("args[i]: " + args[i]);
                if (args[i].startsWith("enablePerformanceMgr:true")) {
                    readActualLaunchParameter = true;
                    PerformanceManager pm = PerformanceManager
                            .getPerformanceManager();
                    pm.setEnabled(true);
                    ESlateContainer.startupTimer = (PerformanceTimer) pm
                            .createGlobalPerformanceTimerGroup(
                                    "E-Slate start-up", true);
                    // PM ESlateContainer.startupTimer.setActive(true,
                    // PerformanceTimer.DONT_ACTIVATE);
                    pm.init(ESlateContainer.startupTimer);
                    break;
                } else if (args[i].startsWith("testMwd:")) {
                    readActualLaunchParameter = true;
                    testMicroworldFileName = args[i].substring(8).trim();
                    if (testMicroworldFileName.startsWith("\""))
                        testMicroworldFileName = testMicroworldFileName
                                .substring(1);
                    if (testMicroworldFileName.endsWith("\""))
                        testMicroworldFileName = testMicroworldFileName
                                .substring(0, testMicroworldFileName.length()-2);
                    ;
                } else if (args[i].toLowerCase().startsWith("times:")) {
                    readActualLaunchParameter = true;
                    String tmp = args[i].substring(6).trim();
                    try {
                        times = Integer.valueOf(tmp).intValue();
                    } catch (Throwable thr) {
                    }
                } else if (args[i].startsWith("fullScreenMode:")) {
                    readActualLaunchParameter = true;
                    String tmp = args[i].substring(15);
                    try {
                        fullScreenMode = Boolean.valueOf(tmp).booleanValue();
                    } catch (Throwable thr) {
                    }
                } else if (args[i].toLowerCase().startsWith("startupmwd:")) {
                    readActualLaunchParameter = true;
                    startupMicroworldFileName = args[i].substring(11).trim();
                    // If 'startupMicroworldFileName' starts with the token
                    // '$user.dir', then this is substituted with the
                    // contents of the environment java variable 'user.dir'.
                    if (startupMicroworldFileName.startsWith("$user.dir")) {
                        String userDir = System.getProperty("user.dir");
                        if (userDir==null)
                            userDir = "";
                        startupMicroworldFileName = userDir
                                +startupMicroworldFileName.substring(9);
                    }
                    // Any other argument that follows "startupmwd" is
                    // considered part of the
                    // start-up microworld file name.
                    // (N) disabled for now
                    // i++;
                    // while (i < args.length) {
                    // startupMicroworldFileName = startupMicroworldFileName +
                    // ' ' + args[i];
                    // i++;
                    // }
                    // System.out.println("startupMicroworldFileName: " +
                    // startupMicroworldFileName);
                } else if (args[i].toLowerCase().startsWith("mwdparam:")) {
                    readActualLaunchParameter = true;
                    String param = args[i].substring(9).trim();
                    String[] pair = param.split(":");
                    if (pair.length==2&&pair[0].trim().length()>0
                            &&pair[1].trim().length()>0)
                        mwdParams.put(pair[0].trim(), pair[1].trim());
                } else {
                    // Default is startupmwd: . Code copied from above and
                    // removed substring
                    if (startupMicroworldFileName==null) {
                        startupMicroworldFileName = args[i].trim();
                        // If 'startupMicroworldFileName' starts with the token
                        // '$user.dir', then this is substituted with the
                        // contents of the environment java variable 'user.dir'.
                        if (startupMicroworldFileName.startsWith("$user.dir")) {
                            String userDir = System.getProperty("user.dir");
                            if (userDir==null)
                                userDir = "";
                            startupMicroworldFileName = userDir
                                    +startupMicroworldFileName.substring(9);
                        }
                    } else {
                        // Any other argument that follows "startupmwd" is
                        // considered part of the
                        // start-up microworld file name.
                        if (! readActualLaunchParameter)
                            startupMicroworldFileName = startupMicroworldFileName
                                    +' '+args[i];
                    }
                }
            }
        }

        ESlateContainerFrame fr = null;
        if (runtimeEnv)
            fr = new ESlateContainerFrame(args, testMicroworldFileName, times);
        else
            fr = new ESlateComposerFrame(args, testMicroworldFileName, times);
        fr.container.startupMicroworldFileName = startupMicroworldFileName;
        fr.container.setFullScreen(fr, fullScreenMode);

        // @see comments in addNotify()
        if (startupMicroworldFileName!=null) {
            fr.container.initialize2();
            // System.out.println("fr size: " + fr.getSize() +
            // ", fr content size: " + fr.getContentPane().getSize());
            if (fr.getWidth()==0||fr.getHeight()==0)
                fr.setBounds(0, 0, 1024,768);
            fr.setVisible(true);

            boolean isWeb = false;
            try {
                URL url = new URL(startupMicroworldFileName);

                URLConnection conn = url.openConnection();
                conn.connect();
                isWeb = true;
            } catch (MalformedURLException e) {
                // the URL is not in a valid form
            } catch (IOException e) {
                // the connection couldn't be established
            }

            String file = null;
            if (isWeb) {
                // site = startupMicroworldFileName.substring(0,
                // startupMicroworldFileName.lastIndexOf("/"));
                // file = startupMicroworldFileName.substring(
                // startupMicroworldFileName.lastIndexOf("/")+1,
                // startupMicroworldFileName.length());
                file = System.currentTimeMillis()+
                        startupMicroworldFileName.substring(startupMicroworldFileName.lastIndexOf('.'),
                                                            startupMicroworldFileName.length());

            }

            fr.container.initialize2();
            // System.out.println("fr size: " + fr.getSize() +
            // ", fr content size: " + fr.getContentPane().getSize());
            if (fr.getWidth()==0||fr.getHeight()==0)
                fr.setBounds(0, 0, 1024, 768);
            fr.setVisible(true);
            if (isWeb) {
                if (! fr.container.webServerMicrosHandle.openRemoteMicroWorld(
                        startupMicroworldFileName, file, false/*, mwdParams*/)) {
                    System.exit(0);
                }
            } /*else {
                // fr.container.paintImmediately(fr.container.getVisibleRect());
                if (! fr.container.loadLocalMicroworld(
                        startupMicroworldFileName, true, true, 0, mwdParams))
                    System.exit(0);
            }*/
            startupMicroworldFileName = null;
        } else
            fr.setVisible(true);

        // if (fr!=null && fr.container.getMicroworld().eslateMwd!=null){
        // System.out.println("fr.container.getMicroworld().eslateMwd :"+fr.container.getMicroworld().eslateMwd);
        // try{
        // System.out.println("MWD params :"+mwdParams);
        // fr.container.getMicroworld().eslateMwd.setMicroworldParameters(mwdParams);
        // }catch (Throwable e) {
        // e.printStackTrace();
        // }
        // }
    }

    /**
     * Starts the E-Slate runtime or design time environment.
     * @param args the command line args with which E-Slate starts up
     * @param runtimeEnv which environment will be initialized (runtime or design time)
     */
    static void startESlate(String[] args, boolean runtimeEnv) {
//        System.out.println("Main start");
//        long millis1= System.currentTimeMillis();
        String testMicroworldFileName = null;
        int times = 0;
        String startupMicroworldFileName = null;
        boolean fullScreenMode = false;
/*System.out.println("args: " + args);
if (args != null) {
    System.out.println("args.length: " + args.length);
    for (int k=0; k<args.length; k++)
        System.out.println("Arg " + k + ": " + args[k] );
}
*/
        if (args != null && args.length >= 1) {
//            StringBuffer argStrBuffer = new StringBuffer();
//            for (int i=0; i<args.length; i++)
//                arg
            for (int i=0; i<args.length; i++) {
//System.out.println("args[i]: " + args[i]);
                if (args[i].startsWith("enablePerformanceMgr:true")) {
                    PerformanceManager pm = PerformanceManager.getPerformanceManager();
                    pm.setEnabled(true);
                    ESlateContainer.startupTimer = (PerformanceTimer) pm.createGlobalPerformanceTimerGroup("E-Slate start-up", true);
//PM                    ESlateContainer.startupTimer.setActive(true, PerformanceTimer.DONT_ACTIVATE);
                    pm.init(ESlateContainer.startupTimer);
                    break;
                }else if (args[i].startsWith("testMwd:")) {
                    testMicroworldFileName = args[i].substring(8).trim();
                    if (testMicroworldFileName.startsWith("\""))
                        testMicroworldFileName = testMicroworldFileName.substring(1);
                    if (testMicroworldFileName.endsWith("\""))
                        testMicroworldFileName = testMicroworldFileName.substring(0, testMicroworldFileName.length()-2);;
                }else if (args[i].toLowerCase().startsWith("times:")) {
                    String tmp = args[i].substring(6).trim();
                    try{
                        times = Integer.valueOf(tmp).intValue();
                    }catch (Throwable thr) {}
                }else if (args[i].startsWith("fullScreenMode:")) {
                    String tmp = args[i].substring(15);
                    try{
                        fullScreenMode = Boolean.valueOf(tmp).booleanValue();
                    }catch (Throwable thr) {}
                }else if (args[i].toLowerCase().startsWith("startupmwd:")) {
                    startupMicroworldFileName = args[i].substring(11).trim();
                    // If 'startupMicroworldFileName' starts with the token '$user.dir', then this is substituted with the
                    // contents of the environment java variable 'user.dir'.
                    if (startupMicroworldFileName.startsWith("$user.dir")) {
                        String userDir = System.getProperty("user.dir");
                        if (userDir == null) userDir = "";
                        startupMicroworldFileName = userDir + startupMicroworldFileName.substring(9);
                    }
                    // Any other argument that follows "startupmwd" is considered part of the
                    // start-up microworld file name.
                    i++;
                    while (i < args.length) {
                        startupMicroworldFileName = startupMicroworldFileName + ' ' + args[i];
                        i++;
                    }
//System.out.println("startupMicroworldFileName: " + startupMicroworldFileName);
                }else {
                	//Default is startupmwd: . Code copied from above and removed substring
                	startupMicroworldFileName = args[i].trim();
                	// If 'startupMicroworldFileName' starts with the token '$user.dir', then this is substituted with the
                	// contents of the environment java variable 'user.dir'.
                	if (startupMicroworldFileName.startsWith("$user.dir")) {
                		String userDir = System.getProperty("user.dir");
                		if (userDir == null) userDir = "";
                		startupMicroworldFileName = userDir + startupMicroworldFileName.substring(9);
                	}
                	// Any other argument that follows "startupmwd" is considered part of the
                	// start-up microworld file name.
                	i++;
                	while (i < args.length) {
                		startupMicroworldFileName = startupMicroworldFileName + ' ' + args[i];
                		i++;
                	}
//System.out.println("startupMicroworldFileName: " + startupMicroworldFileName);
                }
            }
        }

        if (runtimeEnv)
            fr = new ESlateContainerFrame(args, testMicroworldFileName, times);
        else
            fr = new ESlateComposerFrame(args, testMicroworldFileName, times);
        fr.container.startupMicroworldFileName = startupMicroworldFileName;
        //fr.container.setFullScreen(fr, fullScreenMode);

        // @see comments in addNotify()
        if (startupMicroworldFileName != null) {
            fr.container.initialize2();
//System.out.println("fr size: " + fr.getSize() + ", fr content size: " + fr.getContentPane().getSize());
            if (fr.getWidth() == 0 || fr.getHeight() == 0)
                fr.setBounds(0, 0, 500, 500);
            fr.setVisible(true);
//            fr.container.paintImmediately(fr.container.getVisibleRect());
            if (!fr.container.loadLocalMicroworld(startupMicroworldFileName, true, true, 0))
                System.exit(0);
            startupMicroworldFileName = null;
        }else
            fr.setVisible(true);

    }
    
    /**
     * Starts the E-Slate runtime or design time environment.
     * @param args the command line args with which E-Slate starts up
     * @param runtimeEnv which environment will be initialized (runtime or design time)
     */
    public void startESlate2(String[] args2, boolean runtimeEnv) {
        System.out.println("Main start");
//        long millis1= System.currentTimeMillis();
    	String [] args = args2;
        String testMicroworldFileName = null;
        int times = 0;
        //String startupMicroworldFileName = null;
        boolean fullScreenMode = false;
/*System.out.println("args: " + args);
if (args != null) {
    System.out.println("args.length: " + args.length);
    for (int k=0; k<args.length; k++)
        System.out.println("Arg " + k + ": " + args[k] );
}
*/
        if (args != null && args.length >= 1) {
//            StringBuffer argStrBuffer = new StringBuffer();
//            for (int i=0; i<args.length; i++)
//                arg
            for (int i=0; i<args.length; i++) {
//System.out.println("args[i]: " + args[i]);
                if (args[i].startsWith("enablePerformanceMgr:true")) {
                    PerformanceManager pm = PerformanceManager.getPerformanceManager();
                    pm.setEnabled(true);
                    ESlateContainer.startupTimer = (PerformanceTimer) pm.createGlobalPerformanceTimerGroup("E-Slate start-up", true);
//PM                    ESlateContainer.startupTimer.setActive(true, PerformanceTimer.DONT_ACTIVATE);
                    pm.init(ESlateContainer.startupTimer);
                    break;
                }else if (args[i].startsWith("testMwd:")) {
                    testMicroworldFileName = args[i].substring(8).trim();
                    if (testMicroworldFileName.startsWith("\""))
                        testMicroworldFileName = testMicroworldFileName.substring(1);
                    if (testMicroworldFileName.endsWith("\""))
                        testMicroworldFileName = testMicroworldFileName.substring(0, testMicroworldFileName.length()-2);;
                }else if (args[i].toLowerCase().startsWith("times:")) {
                    String tmp = args[i].substring(6).trim();
                    try{
                        times = Integer.valueOf(tmp).intValue();
                    }catch (Throwable thr) {}
                }else if (args[i].startsWith("fullScreenMode:")) {
                    String tmp = args[i].substring(15);
                    try{
                        fullScreenMode = Boolean.valueOf(tmp).booleanValue();
                    }catch (Throwable thr) {}
                }else if (args[i].toLowerCase().startsWith("startupmwd:")) {
                    startupMicroworldFileName = args[i].substring(11).trim();
                    // If 'startupMicroworldFileName' starts with the token '$user.dir', then this is substituted with the
                    // contents of the environment java variable 'user.dir'.
                    if (startupMicroworldFileName.startsWith("$user.dir")) {
                        String userDir = System.getProperty("user.dir");
                        if (userDir == null) userDir = "";
                        startupMicroworldFileName = userDir + startupMicroworldFileName.substring(9);
                    }
                    // Any other argument that follows "startupmwd" is considered part of the
                    // start-up microworld file name.
                    i++;
                    while (i < args.length) {
                        startupMicroworldFileName = startupMicroworldFileName + ' ' + args[i];
                        i++;
                    }
//System.out.println("startupMicroworldFileName: " + startupMicroworldFileName);
                }else {
                	//Default is startupmwd: . Code copied from above and removed substring
                	startupMicroworldFileName = args[i].trim();
                	// If 'startupMicroworldFileName' starts with the token '$user.dir', then this is substituted with the
                	// contents of the environment java variable 'user.dir'.
                	if (startupMicroworldFileName.startsWith("$user.dir")) {
                		String userDir = System.getProperty("user.dir");
                		if (userDir == null) userDir = "";
                		startupMicroworldFileName = userDir + startupMicroworldFileName.substring(9);
                	}
                	// Any other argument that follows "startupmwd" is considered part of the
                	// start-up microworld file name.
                	i++;
                	while (i < args.length) {
                		startupMicroworldFileName = startupMicroworldFileName + ' ' + args[i];
                		i++;
                	}
//System.out.println("startupMicroworldFileName: " + startupMicroworldFileName);
                }
            }
        }

        this.setVisible(true);
    }
}

class TitlePanel extends JPanel {
    public TitlePanel(String title, Color borderColor) {
        super(true);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(UIManager.getColor("controlShadow"));
//        add(Box.createHorizontalStrut(7));
        JLabel titleLabel = new JLabel(title);
        Locale locale = Locale.getDefault();
        ResourceBundle containerBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ContainerBundle", locale);
        if (containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
            titleLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
        else{
            Font currFont = titleLabel.getFont();
            titleLabel.setFont(new Font(currFont.getFamily(), Font.BOLD, currFont.getSize()));
        }
        if (borderColor != null)
            titleLabel.setForeground(borderColor);
        else
            titleLabel.setForeground(Color.white);

        add(titleLabel);
        add(Box.createGlue());
        setBorder(new LineBorder(Color.yellow));
//        setBorder(new EmptyBorder(2, 7, 2, 0)); //new CompoundBorder(new BevelBorder(BevelBorder.RAISED), new EmptyBorder(1, 7, 1, 0)));
    }

    public void setTitle(String title, Color borderColor) {
        ((JLabel) getComponent(0)).setText(title);
        if (borderColor != null)
            ((JLabel) getComponent(0)).setForeground(borderColor);
        else
            ((JLabel) getComponent(0)).setForeground(Color.white);
    }
    
	/**
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		if (((JLabel) getComponent(0)).getText()==null || "".equals(((JLabel) getComponent(0)).getText()))
			return new Dimension(0,0);
		return super.getPreferredSize();
	}
}


abstract class WebSiteAction implements ActionListener {
    String address;

    public WebSiteAction(String address) {
        this.address = address;
    }
}

class InfoWindow extends javax.swing.JWindow implements Runnable {
    private JPanel p;
    private ImageIcon[] icons;
    private JLabel iconLabel;
    private Thread thread=null;
    private int currentIcon = 0;
    private int animationFrequency = 1000;

    public InfoWindow(Component parentComponent, String[] imageFileNames, String message, int animFrequency) {
        super(new JFrame()); //new JFrame());
//        System.out.println("locale: " + locale);

        icons = new ImageIcon[imageFileNames.length];
        int maxIconWidth = 0;
        int maxIconHeight = 0;
        for (int i=0; i<imageFileNames.length; i++) {
            icons[i] = new ImageIcon(getClass().getResource(imageFileNames[i]));
            if (maxIconWidth < icons[i].getIconWidth())
                maxIconWidth = icons[i].getIconWidth();
            if (maxIconHeight < icons[i].getIconHeight())
                maxIconHeight = icons[i].getIconHeight();
        }


        this.animationFrequency = animFrequency;

        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new CompoundBorder(new javax.swing.border.LineBorder(new Color(0, 0, 110), 3),
                    new EmptyBorder(10, 10, 10, 10)));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setAlignmentX(CENTER_ALIGNMENT);
/*
        Font greekUIFont = new Font("Helvetica", Font.ITALIC, 14);
        if (Locale.getDefault().toString().equals("el_GR"))
            messageLabel.setFont(greekUIFont);
        else
            messageLabel.setFont(new Font(messageLabel.getFont().getFamily(), messageLabel.getFont().getStyle(), 14));
*/

        java.awt.FontMetrics fm = messageLabel.getFontMetrics(messageLabel.getFont());
        Dimension dim = new Dimension(fm.stringWidth(messageLabel.getText()), 20);
        messageLabel.setMaximumSize(dim);
        messageLabel.setPreferredSize(dim);
        messageLabel.setMinimumSize(dim);

        if (dim.width > maxIconWidth)
            maxIconWidth = dim.width;

        iconLabel = new JLabel(icons[0]);
        iconLabel.setAlignmentX(CENTER_ALIGNMENT);
        p.add(iconLabel);
        p.add(messageLabel);

        dim = new Dimension(maxIconWidth+25, maxIconHeight+40);
        p.setPreferredSize(dim);
        p.setMinimumSize(dim);
        p.setMaximumSize(dim);

        setContentPane(p);

        pack();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
//        System.out.println("parentComponent.isVisible(): " + parentComponent.isVisible());
        if (parentComponent == null || !parentComponent.isVisible()) {
            x = (screenSize.width/2) - (getSize().width/2);
            y = (screenSize.height/2) - (getSize().height/2);
        }else{
            try{
                Rectangle compBounds = parentComponent.getBounds();
                java.awt.Point compLocation = parentComponent.getLocationOnScreen();
        //        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
                x = compLocation.x + compBounds.width/2 - getSize().width/2;
                y = compLocation.y + compBounds.height/2-getSize().height/2;
                if (x+getSize().width > screenSize.width)
                    x = screenSize.width - getSize().width;
                if (y+getSize().height > screenSize.height)
                    y = screenSize.height - getSize().height;
                if (x < 0) x = 0;
                if (y < 0) y = 0;
            }catch (Exception exc) {
                /* When the Container preloads a microworld upon start-up, an exception is
                 * thrown, because even though the parentComponent is not yet visible, the
                 * parentComponent.isVisible() method returns true.
                 */
                x = (screenSize.width/2) - (getSize().width/2);
                y = (screenSize.height/2) - (getSize().height/2);
            }
        }
        setLocation(x, y);
        setVisible(true);
    }

    public void run() {
        setVisible(true);
        p.paintImmediately(p.getVisibleRect());;
//        System.out.println("Is visible(): " + isVisible());
        int i=0;
        while (isVisible()) {
//            System.out.println("Repainting: " + p.getVisibleRect() + ", " + i);
            i++;
            currentIcon++;
            if (currentIcon == icons.length)
                currentIcon = 0;
            iconLabel.setIcon(icons[currentIcon]);
            p.paintImmediately(p.getVisibleRect());;
            if (thread != null) {
                try {
                    thread.sleep(animationFrequency);
                }catch (InterruptedException exc) {
                    System.out.println("InteruptedException");
                }
            }
        }
    }

    public void setInvokingThread(Thread thr) {
        thread = thr;
    }
}


class ESlateContainerWindowsDesktopManager extends com.sun.java.swing.plaf.windows.WindowsDesktopManager
    implements FrameDragAdjust {
    protected boolean frameDragEnabled = true;
    ESlateContainer container;
    int prevX=0, prevY=0;
    /* 'draggingFrame' gets set whenever a frame in being dragged around. It is reset
     * when dragging finishes.
     */
    boolean draggingFrame = false;
    /* 'resizingFrame' gets set whenever a frame in being resized. It is reset
     * when resizing finishes.
     */
    boolean resizingFrame = false;


    public ESlateContainerWindowsDesktopManager(ESlateContainer container) {
        this.container = container;
    }

    public void resizeFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
        if (!container.currentView.mwdAutoExpandable && container.currentView.componentsMoveBeyondMwdBounds) {
            /* Do not allow a frame to rezise beyond the edges of the visible part of the
             * desktop, so that it becomes completely hidden. The component resizes but it
             * can't become totally hidden. This policy is in effect in all cases but:
             * a. When components are not allowed to move beyond the desktop bounds, in
             *    which case no part of the visble area of the component can't hidde.
             * b. When the desktop is auto-expandable. In this case the desktop is always
             *    enlarged when part of the component resizes outside it, so no part of the
             *    component can become hidden.
             */
            Rectangle visibleDesktopPaneRect = container.scrollPane.getViewport().getViewRect();
            if (newX + f.getSize().width < visibleDesktopPaneRect.x+20)
                newX = visibleDesktopPaneRect.x - f.getSize().width + 20;
            else if (newX > visibleDesktopPaneRect.x + visibleDesktopPaneRect.width - 20)
                newX = visibleDesktopPaneRect.x + visibleDesktopPaneRect.width - 20;
            if (newY < visibleDesktopPaneRect.y-10)
                newY = visibleDesktopPaneRect.y-10;
            else if (newY > visibleDesktopPaneRect.y + visibleDesktopPaneRect.height - 20)
                newY = visibleDesktopPaneRect.y + visibleDesktopPaneRect.height - 20;

        }

        /* The following adjustment of 'newX' and 'newY' variables make possible the proper
         * resizing behaviour of the component when the Desktop pane has been enlarged (to the
         * left or top) in the special way which allows the components of the microworld to
         * maintain there relative positions to the grid.
         */
        newX = newX + container.jumpedPixelsX;
        newY = newY + container.jumpedPixelsY;
        super.resizeFrame(f, newX, newY, newWidth, newHeight);
        container.setMicroworldChanged(true);
    }

    public void beginResizingFrame(JComponent f, int direction) {
        super.beginResizingFrame(f, direction);
        resizingFrame = true;
    }

    public void endResizingFrame(JComponent f) {
        super.endResizingFrame(f);
        resizingFrame = false;
        /* Clear the 'jumpedPixelsX' and 'jumpedPixelsY' variables, which may have been set, if
         * the component has been resized beyond the leftmost and topmost borders of the Desktop
         * pane.
         */
        container.jumpedPixelsX = 0;
        container.jumpedPixelsY = 0;
        if (container.lc.snapToGrid)
            container.containerUtils.snapComponentToGrid((ESlateInternalFrame) f);
    }

    public void dragFrame(JComponent f, int newX, int newY) {
        if (!frameDragEnabled)
            return;
        /* If the microworld's border are not auto-expandable and the microworld's
         * component cannot move beyond its border, then limit their move inside the container area.
         */
        if (!container.currentView.mwdAutoExpandable && !container.currentView.componentsMoveBeyondMwdBounds) {
            if (newX < 0) {
                /* If the next drag stop is beyond the left horizontal border, then do
                 * allow it, unless the component is not exactly on the left border (x != 0).
                 * If the component in not on the left border, then put it there (x=0) and
                 * reject any next attempt to move it beyond the border.
                 */
                 newX = 0;
            }
            if (newY < 0) {
                newY = 0;
            }
            if (newX + f.getSize().width > container.lc.getSize().width) {
                newX = container.lc.getSize().width - f.getSize().width;
            }
            if (newY + f.getSize().height > container.lc.getSize().height) {
                newY = container.lc.getSize().height - f.getSize().height;
            }
        }else{
            /* Do not allow a frame to move beyond the edges of the visible part of the
             * desktop, so that it becomes completely hidden. The component can move beyond
             * the bounds of the desktop, but it can't become totally hidden. This policy is
             * in effect in all cases but:
             * a. When components are not allowed to move beyond the desktop bounds, in
             *    which case no part of the visble area of the component can't hidde.
             * b. When the desktop is auto-expandable. In this case the desktop is always
             *    enlarged when part of the component moves outside it, so no part of the
             *    component can become hidden.
             */
            Rectangle visibleDesktopPaneRect = container.scrollPane.getViewport().getViewRect();
            if (newX + f.getSize().width < visibleDesktopPaneRect.x+20)
                newX = visibleDesktopPaneRect.x - f.getSize().width + 20;
            else if (newX > visibleDesktopPaneRect.x + visibleDesktopPaneRect.width - 20)
                newX = visibleDesktopPaneRect.x + visibleDesktopPaneRect.width - 20;
            if (newY < visibleDesktopPaneRect.y-10)
                newY = visibleDesktopPaneRect.y-10;
            else if (newY > visibleDesktopPaneRect.y + visibleDesktopPaneRect.height - 20)
                newY = visibleDesktopPaneRect.y + visibleDesktopPaneRect.height - 20;

        }
        container.setMicroworldChanged(true);
//        System.out.println("1. newX: " + newX + ", newY: " + newY);
        if (newX < prevX) //movement to the left
            newX = newX + container.jumpedPixelsX;
        if (newY < prevY) //movement to the top
            newY = newY + container.jumpedPixelsY;
//        System.out.println("2. newX: " + newX + ", newY: " + newY);
        super.dragFrame(f, newX, newY);
        prevX = newX;
        prevY = newY;
    }

    // This dragFrame() was used before the problem with the dragging of a JInternalFrame to the
    // top or left of the Container was fixed.
/*    public void dragFrame(JComponent f, int newX, int newY) {
        if (frameDragEnabled) {
*/            /* If the microworld's border are not auto-expandable and the microworld's
             * component cannot move beyond its border, then limit their move here.
             */
/*            if (!container.mwdAutoExpandable && !container.componentsMoveBeyondMwdBounds) {
                if (newX < 0) {
*/                    /* If the next drag stop is beyond the left horizontal border, then do
                     * allow it, unless the component is not exactly on the left border (x != 0).
                     * If the component in not on the left border, then put it there (x=0) and
                     * reject any next attempt to move it beyond the border.
                     */
/*                    if (f.getLocation().x != 0)
                        newX = 0;
                    else
                        return;
                }
                if (newY < 0) {
                    if (f.getLocation().y != 0)
                        newY = 0;
                    else
                        return;
                }
                if (newX + f.getSize().width > container.lc.getSize().width) {
                    if (f.getLocation().x + f.getSize().width != container.lc.getSize().width)
                        newX = container.lc.getSize().width - f.getSize().width;
                    else
                        return;
                }
                if (newY + f.getSize().height > container.lc.getSize().height) {
                    if (f.getLocation().y + f.getSize().height != container.lc.getSize().height)
                        newY = container.lc.getSize().height - f.getSize().height;
                    else
                        return;
                }
            }

            container.setMicroworldChanged(true);
            System.out.println("1. dragFrame(): " + newX + ", " + newY + ", " + prevX + ", " + prevY);
*/            /* The very customized way in which a component is resized or dragged, when it reaches
             * the leftmost or topmost borders of the Container, result in the dragFrame being called
             * with the same newX and newY constantly, after the cursor exits the Desktop pane's borders.
             * We therefore improvise and call dragFrame with arguments which are each 3 points less
             * than the arguments of the last call.
             */
/*            if (container.jumpedPixelsX > 0 && newX >= prevX)
                newX = prevX-3;
            if (container.jumpedPixelsY > 0 && newY >= prevY)
                newY = prevY-3;
            System.out.println("2. dragFrame(): " + newX + ", " + newY + ", " + prevX + ", " + prevY);

            System.out.println("container.jumpedPixelsX: " + container.jumpedPixelsX);
            newX = newX + container.jumpedPixelsX;
            newY = newY + container.jumpedPixelsY;
            System.out.println("3. dragFrame(): " + newX + ", " + newY);
            super.dragFrame(f, newX, newY);
//            ((ESlateInternalFrame) f).paintBorder(f.getGraphics());
            if (container.jumpedPixelsX > 0 && newX >= prevX)
                prevX = prevX-3;
            else
                prevX = newX - container.jumpedPixelsX;
            if (container.jumpedPixelsY > 0 && newY >= prevY)
                prevY = prevY-3;
            else
                prevY = newY - container.jumpedPixelsY;
        }
    }
*/
    public void enableFrameDragging() {
        container.setMicroworldChanged(true);
//        System.out.println("microworldChanged 18");
        frameDragEnabled = true;
    }

    public void disableFrameDragging() {
        container.setMicroworldChanged(true);
//        System.out.println("microworldChanged 19");
        frameDragEnabled = false;
    }

    public boolean isFrameDraggingEnabled() {
        return frameDragEnabled;
    }

    // Overides the MDI functionality of the Desktop manager.
    public void activateFrame(JInternalFrame frame) {
        if (container.lc.isModalFrameVisible()) return;
        ESlateInternalFrame[] frames = container.mwdComponents.getComponentFrames();
        for (int i=0; i<frames.length; i++) {
//            System.out.println("frames[i]: " + frames[i]);
            frames[i].disableMaximizeChange = true;
        }
        super.activateFrame(frame);
        for (int i=0; i<frames.length; i++) {
//            ESlateInternalFrame f = (ESlateInternalFrame) container.componentFrames.at(i);
            frames[i].disableMaximizeChange = false;
        }
/*0        for (int i=0; i<container.componentFrames.size(); i++) {
            ESlateInternalFrame f = (ESlateInternalFrame) container.componentFrames.at(i);
            f.disableMaximizeChange = true;
        }
        super.activateFrame(frame);
        for (int i=0; i<container.componentFrames.size(); i++) {
            ESlateInternalFrame f = (ESlateInternalFrame) container.componentFrames.at(i);
            f.disableMaximizeChange = false;
        }
*/
    }
    public void beginDraggingFrame(JComponent f) {
        super.beginDraggingFrame(f);
        draggingFrame = true;
        if (f instanceof ESlateInternalFrame && ((ESlateInternalFrame) f).isComponentActivatedOnMouseClick())
            f.setBorder(OldESlateInternalFrame.NORTH_SOUTH_WEST_EAST_BORDER);
    }

    public void endDraggingFrame(JComponent f) {
        super.endDraggingFrame(f);
        draggingFrame = false;
//        ((ESlateInternalFrame) f).attachNormalBorder();
        /* Clear the 'jumpedPixelsX' and 'jumpedPixelsY' variables, which may have been set, if
         * the component has been dragged beyond the leftmost and topmost borders of the Desktop
         * pane.
         */
        container.jumpedPixelsX = 0;
        container.jumpedPixelsY = 0;
        if (container.lc.snapToGrid)
            container.containerUtils.snapComponentToGrid((ESlateInternalFrame) f);
        if (f instanceof ESlateInternalFrame)
        	((ESlateInternalFrame) f).restoreBorder(); //setBorder(ESlateInternalFrame.FRAME_NORMAL_BORDER);
        container.autoExpandOccured2 = false;
//        upperCornerGridCell.x = scrollPane.getViewport().getViewPosition().x - desktopPos.x % gridStep;
//        upperCornerGridCell.y = scrollPane.getViewport().getViewPosition().y - desktopPos.y % gridStep;
    }
}

interface FrameDragAdjust {
    public void enableFrameDragging();
    public void disableFrameDragging();
    public boolean isFrameDraggingEnabled();
}


////nikosM
/*
class WaitDialogTimerTask extends java.util.TimerTask implements ProgressBarInterface {
    WaitDialog dialog = null;
    ESlateContainer container;
    int progress = 0;
    String message = " ";
    boolean loading = true;
    int minimum = 0;
    int maximum = 100;

    public WaitDialogTimerTask(ESlateContainer container, boolean loading, boolean constructNow) {
        this.container = container;
        this.loading = loading;
        /* 'constructNow' is used to specify that the WaitDialog should be displayed
         * immediately and not after some time interval. This can be specified by sceduling
         * the TimerTask to a Date, before the current time. This works fine, as long as the
         * WaitDialog does not need to be displayed during E-Slate start-up. In this case
         * the WaitDialog does not appear at all. Probably this has to do with the fact that
         * the TimerStack runs on a thread different from the main tread. To overcome this
         * problem, I have enchanced the WaitDialogTimerTask's constructor to include the
         * 'constructNow' variable, which causes the WaitDialog to be constructed on the main thread
         * (the thread on which the constructor is being called).
         *
        if (constructNow)
            constructWaitDialog();
    }

    synchronized WaitDialog constructWaitDialog() {
        if (dialog != null) {
            System.out.println("Dialog already constructed");
            return null;
        }

        dialog = new WaitDialog(loading);
        dialog.setMessage(message);
        dialog.setProgress(progress);
        ESlateContainerUtils.showDialog(dialog, container, true);
        dialog.paintImmediately();
        return dialog;
    }

    public void run() {
        if (dialog == null)
            constructWaitDialog();
    }

    public void setProgress(int value) {
        progress = value;
        if (dialog != null)
            dialog.setProgress(value);
    }

    public int getProgress() {
        return progress;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMessage(String msg) {
        if (msg == null) msg = " ";
        message = msg;
        if (dialog != null)
            dialog.setMessage(message);
    }

    synchronized public void disposeDialog() {
//        System.out.println("disposeDialog() dialog: " + dialog);
        if (dialog != null) {
//            System.out.println("disposeDialog() trying to dispose()");
            /* Some peculiar Swing or Java2D bug causes the application to
             * stop executing (without crashing) if the waitDialog gets
             * disposed, during the ESlateContainer's start-up. The bug
             * is avoided if we hide the waitDialog, insted of disposing it.
             * However this leaves the first WaitDialog undisposed in each
             * E-Slate session which is initiated by double-clicking a .mwd archieve
             * (this is the only case in which a WaitDialog is displayed during the
             * E-Slate start-up).
             *
            if (container.isContainerInitializing)
                dialog.hide();
            else
                dialog.dispose();
//            System.out.println("Dialog was disposed");
            dialog = null;
        }
        cancel();
    }
}
*/
/*class MySecurityManager extends SecurityManager {
    boolean enabled = false;

    public void checkPermission(java.security.Permission perm) {
        if (!enabled) return;
//                System.out.println("Permission: " + perm.getName());
        if (perm.getName().equals("setSecurityManager"))
            super.checkPermission(perm);
    }

    void enable() {
        enabled = true;
    }

    void disable() {
        enabled = false;
    }
}
*/


/* OLD SAVE AS

//            out.writeObject(componentScriptListeners);
            componentScriptListeners.saveMap(out); //xml

            //Save the microworld properties
//            out.writeObject(activityTitle);
            //Save the microworld's size
            out.writeObject(lc.getPreferredSize());
            // Save the coordinates of the upper left corner of the visible area of the desktop pane
            out.writeObject(new Integer(scrollPane.getViewport().getViewPosition().x));
            out.writeObject(new Integer(scrollPane.getViewport().getViewPosition().y));

            //Save the microworld's properties
            out.writeObject(new Boolean(minimizeAllowed));
            out.writeObject(new Boolean(maximizeAllowed));
            out.writeObject(new Boolean(closeAllowed));
            out.writeObject(new Boolean(controlBarsVisible));
            out.writeObject(new Boolean(nameChangeAllowed));
            out.writeObject(new Boolean(controlBarTitleActive));
            out.writeObject(new Boolean(helpButtonVisible));
            out.writeObject(new Boolean(pinButtonVisible));
            out.writeObject(new Boolean(infoButtonVisible));
            out.writeObject(new Boolean(resizeAllowed));
            out.writeObject(new Boolean(moveAllowed));
            out.writeObject(new Boolean(componentInstantiationAllowed));
            out.writeObject(new Boolean(componentRemovalAllowed));
            out.writeObject(new Boolean(mwdBgrdChangeAllowed));
            out.writeObject(new Boolean(mwdStorageAllowed));

            out.writeObject(new Boolean(mwdPinViewEnabled));
*/
            /* Arrange the 'components' Array, so that the component appear in the
             * order in which they were last activated. This way the components will
             * be stored and re-created in this order, which is an important fact
             * for the correct Z-order restore of the internal frames.
             */
/*
            Array newComponents = new Array();
            Array newComponentFrames = new Array();
            int f = 0;
            ESlateInternalFrame frame;
            while ((frame = compSelectionHistory.get(f)) != null && frame != EMPTY_FRAME) {
                newComponentFrames.insert(0, frame);
                newComponents.insert(0, frame.eSlateHandle.getComponent());
                f++;
            }
            Object comp;
            if (newComponents.size() != components.size()) {
                for (int i=0; i<components.size(); i++) {
                    comp = components.at(i);
                    if (newComponents.indexOf(comp) == -1)
                        newComponents.insert(0, comp);
                }
            }
            if (newComponentFrames.size() != componentFrames.size()) {
                for (int i=0; i<componentFrames.size(); i++) {
                    frame = (ESlateInternalFrame) componentFrames.at(i);
                    if (newComponentFrames.indexOf(frame) == -1)
                        newComponentFrames.insert(0, frame);
                }
            }


            //Save the frames' properties
            boolean[] closableFramesState = new boolean[newComponents.size()];
            boolean[] iconifiableFramesState = new boolean[newComponents.size()];
            boolean[] maximizableFramesState = new boolean[newComponents.size()];
            boolean[] barVisibilityFramesState = new boolean[newComponents.size()];
            boolean[] helpButtonVisibilityFramesState = new boolean[newComponents.size()];
            boolean[] infoButtonVisibilityFramesState = new boolean[newComponents.size()];
            boolean[] pinButtonVisibilityFramesState = new boolean[newComponents.size()];
            boolean[] activeTitleFramesState = new boolean[newComponents.size()];
            boolean[] resizableFramesState = new boolean[newComponents.size()];
            boolean[] isIcon = new boolean[newComponents.size()];
            boolean[] isMaximum = new boolean[newComponents.size()];
            for (int i=0; i<newComponents.size(); i++) {
                ESlateInternalFrame fr = (ESlateInternalFrame) newComponentFrames.at(i);
                closableFramesState[i] = fr.isClosable();
                iconifiableFramesState[i] = fr.isIconifiable();
                maximizableFramesState[i] = fr.isMaximizable();
                barVisibilityFramesState[i] = fr.isTitlePanelVisible();
                helpButtonVisibilityFramesState[i] = fr.isHelpButtonVisible();
                infoButtonVisibilityFramesState[i] = fr.isInfoButtonVisible();
                pinButtonVisibilityFramesState[i] = fr.isPinButtonVisible();
                activeTitleFramesState[i] = fr.isComponentNameChangeableFromMenuBar();
                resizableFramesState[i] = fr.isResizable();
                isIcon[i] = fr.isIcon();
                isMaximum[i] = fr.isMaximum();
            }
            out.writeObject(closableFramesState);
            out.writeObject(iconifiableFramesState);
            out.writeObject(maximizableFramesState);
            out.writeObject(barVisibilityFramesState);
            out.writeObject(helpButtonVisibilityFramesState);
            out.writeObject(infoButtonVisibilityFramesState);
            out.writeObject(pinButtonVisibilityFramesState);
            out.writeObject(activeTitleFramesState);
            out.writeObject(resizableFramesState);
            out.writeObject(isIcon);
            out.writeObject(isMaximum);

            Array componentClassNames = new Array();
            for (int i=0; i<newComponents.size(); i++) {
                Component component;
                if (heavyweightComponents.at(i) != null)
                    component = (Component) heavyweightComponents.at(i);
                else
                    component = (Component) newComponents.at(i);
                componentClassNames.add(component.getClass().getName());
            }
//            System.out.println("componentClassNames: " + componentClassNames);
            out.writeObject(componentClassNames);
            out.writeObject(componentFrameIndex);

            //Save the frames' sizes and positions
            int[] width = new int[newComponents.size()];
            int[] height = new int[newComponents.size()];
            int[] xLocation = new int[newComponents.size()];
            int[] yLocation = new int[newComponents.size()];

            boolean microworldChangedBck = microworldChanged;
//            System.out.println("microworldChangedBck: " + microworldChangedBck);
            for (int i=0; i<newComponents.size(); i++) {
                int frameIndex = ((Integer) componentFrameIndex.at(i)).intValue();
//                System.out.println("frameIndex: " + frameIndex);
                if (frameIndex == NOFRAME.intValue()) {
                    width[i] = ((Component) newComponents.at(i)).getSize().width;
                    height[i] = ((Component) newComponents.at(i)).getSize().height;
                    xLocation[i] = ((Component) newComponents.at(i)).getBounds().x;
                    yLocation[i] = ((Component) newComponents.at(i)).getBounds().y;
                }else{
                    ESlateInternalFrame fr = (ESlateInternalFrame) newComponentFrames.at(frameIndex);
//                    System.out.println("Storing frame size: " + fr.getRealSize());
                    width[i] = fr.getRealSize().width;
                    height[i] = fr.getRealSize().height;
                    xLocation[i] = fr.getRealLocation().x;
                    yLocation[i] = fr.getRealLocation().y;
                    if (!fr.isTitlePanelVisible()) {
                        height[i] = height[i]+18;
                        yLocation[i] = yLocation[i]-18;
                    }
                }
            }

            out.writeObject(width);
            out.writeObject(height);
            out.writeObject(xLocation);
            out.writeObject(yLocation);
*/
            /* Save the components which are not Externalizable, but are Serializable.
             * The state of Externalizable components is saved by E-Slate itself in
             * saveMicroworld.
             */
/*            for (int i=0; i<newComponents.size(); i++) {
                Object component;
                if (heavyweightComponents.at(i) != null)
                    component = (Component) heavyweightComponents.at(i);
                else
                    component = (Component) newComponents.at(i);
//                System.out.println("saveAs: " + component.getClass().getName() + ", classIsSerializableOnly: " + classIsSerializableOnly((String) component.getClass().getName()));
                /// TESTING... Should be removed
//                if (component.getClass().getName().equals("gr.cti.eslate.database.Database"))
//                    ((gr.cti.eslate.database.Database) component).savePart(out);
//                else{
                if (classIsSerializableOnly((String) component.getClass().getName())) {
                    try{
                        boolean isButton = false;
                        boolean opaque = false;
                        if (JButton.class.isAssignableFrom(component.getClass())) {
                            isButton = true;
                            opaque = ((JButton) component).isOpaque();
                        }
                        if (JComponent.class.isAssignableFrom(component.getClass()))
                            ((JComponent) component).resetKeyboardActions();
*/
                        /* TESTING XML ARCHIEVER
                        if (component.getClass().getName().equals("gr.cti.eslate.database.Database")) {
                            System.out.println("Container saving database");
//                            archiver.XMLOutputStream out2 = new archiver.XMLOutputStream(System.out);
                            System.out.println("out: " + out);
                            out.writeObject((gr.cti.eslate.database.Database) component);
                            System.out.println("Database saved");
                        }else
                        */
//                            System.out.println("Saving component");
/*                            out.writeObject(component);
                        if (isButton)
                            ((JButton) component).setOpaque(opaque);
                    }catch (Exception exc) {
                        System.out.println("Unable to serialize component \"" +
                              ((ESlateHandle) eSlateHandles.at(i)).getComponentName() +
                              "\" of class " + component.getClass().getName() +
                              ". Exception: " + exc.getClass().getName() + ", " + exc.getMessage());
                        exc.printStackTrace();
                    }
                }
//                }
            }

            out.writeObject((microworldBorder==null)? null:microworldBorder.getBorderInsets(scrollPane));
            out.writeObject(borderColor);

            out.writeObject(borderIcon);
*/
            /* java.io.ByteArrayOutputStream buffer=new java.io.ByteArrayOutputStream();
            if (borderIcon != null) {
                Acme.GifEncoder ge = new Acme.GifEncoder(borderIcon.getImage(), buffer);
             	  ge.encode();
             	  buffer.close();
                out.writeObject(buffer.toByteArray());
                buffer.reset();
            }else
                out.writeObject(null);
            */
/*            out.writeObject(new Integer(borderType));
            out.writeObject(backgroundColor);

            out.writeObject(backgroundIcon);
*/
            /*
            if (backgroundIcon != null) {
                Acme.GifEncoder ge = new Acme.GifEncoder(backgroundIcon.getImage(), buffer);
             	  ge.encode();
             	  buffer.close();
                out.writeObject(buffer.toByteArray());
                buffer.reset();
            }else
                out.writeObject(null);
            */
/*            out.writeObject(new Integer(backgroundIconDisplayMode));
            out.writeObject(new Integer(backgroundType));
            out.writeObject(new Integer(outerBorderType));

            // Additional state
            boolean[] isFrozen = new boolean[newComponents.size()];
            for (int i=0; i<newComponents.size(); i++) {
                ESlateInternalFrame fr = (ESlateInternalFrame) newComponentFrames.at(i);
                isFrozen[i] = fr.isFrozen();
            }
            out.writeObject(isFrozen);
            out.writeObject(new Boolean(mwdTitleEnabled));
            out.writeObject(new Boolean(menuBarVisible));
            out.writeObject(new Boolean(mwdPopupEnabled));
            out.writeObject(new Boolean(outlineDragEnabled));
            boolean[] componentActivatedOnMousePress = new boolean[newComponents.size()];
            for (int i=0; i<newComponents.size(); i++) {
                ESlateInternalFrame fr = (ESlateInternalFrame) newComponentFrames.at(i);
                componentActivatedOnMousePress[i] = fr.isComponentActivatedOnMousePress();
            }
            out.writeObject(componentActivatedOnMousePress);
            out.writeObject(new Boolean(componentBarEnabled));
            out.writeObject(new Boolean(componentFrozenStateChangeAllowed));
            out.writeObject(new Boolean(componentActivationMethodChangeAllowed));
            out.writeObject(new Boolean(menuSystemHeavyWeight));
            // Component names
            String[] compNames = new String[newComponents.size()];
            for (int i=0; i<newComponents.size(); i++) {
                String name = ((ESlateInternalFrame) newComponentFrames.at(i)).eSlateHandle.getComponentName();
                compNames[i] = name;
            }
            out.writeObject(compNames);
            mwdLayers.writeExternal(out);
*/
            /* Save the component layer levels */
/*            int[] levels = new int[componentFrames.size()];
            for (int i=0; i<levels.length; i++) {
                levels[i] = ((ESlateInternalFrame) newComponentFrames.at(i)).getLayer();
//                System.out.println("Saving layer: " + levels[i] +" for component " + ((ESlateInternalFrame) componentFrames.at(i)).eSlateHandle.getComponentName());
            }
            out.writeObject(levels);
            out.writeObject(new Boolean(mwdResizable));
            out.writeObject(new Boolean(mwdAutoExpandable));
            out.writeObject(new Boolean(mwdAutoScrollable));
            out.writeObject(new Boolean(componentsMoveBeyondMwdBounds));
            out.writeObject(new Boolean(desktopDraggable));
            out.writeObject(new Integer(scrollPane.getHorizontalScrollBarPolicy()));
            out.writeObject(new Integer(scrollPane.getVerticalScrollBarPolicy()));
//            System.out.println("Saving mwd size: " + getMicroworldSize());
            out.writeObject(getMicroworldSize());
*/

class GlassInternalFrame extends gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame {
    public GlassInternalFrame(ESlateContainer container) {
        super(container);
//            ESlateInternalFrame w = new ESlateInternalFrame(this, true, true, true, true); //if ESlateContainer.this, eSlateHandle);
        setBorder(null);
        setResizable(false);
        setTitlePanelVisible(false);
    }

    public void setActive(boolean b) throws PropertyVetoException {
//        setSelected(b);
    }
    public void setSelected(boolean selected) throws PropertyVetoException {
    }

    public void setBorder(Border b) {
    }
}

