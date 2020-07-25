package gr.cti.eslate.database;

import gr.cti.eslate.base.BadPlugAliasException;
import gr.cti.eslate.base.ComponentNameChangedEvent;
import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.FemaleSingleIFSingleConnectionProtocolPlug;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.MultipleInputPlug;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.PlugNotConnectedException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.base.sharedObject.SharedObject;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.AttributeLockedException;
import gr.cti.eslate.database.engine.BooleanTableField;
import gr.cti.eslate.database.engine.CImageIcon;
import gr.cti.eslate.database.engine.DBase;
import gr.cti.eslate.database.engine.DateTableField;
import gr.cti.eslate.database.engine.DoubleTableField;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.InsufficientPrivilegesException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.InvalidFieldTypeException;
import gr.cti.eslate.database.engine.InvalidKeyFieldException;
import gr.cti.eslate.database.engine.InvalidPathException;
import gr.cti.eslate.database.engine.InvalidTitleException;
import gr.cti.eslate.database.engine.NoFieldsInTableException;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.StringTableField;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.TableNotExpandableException;
import gr.cti.eslate.database.engine.TimeTableField;
import gr.cti.eslate.database.engine.URLTableField;
import gr.cti.eslate.database.engine.UnableToInitializeDatabaseException;
import gr.cti.eslate.database.engine.UnableToOverwriteException;
import gr.cti.eslate.database.engine.UnableToSaveException;
import gr.cti.eslate.database.engine.event.ActiveTableChangedEvent;
import gr.cti.eslate.database.engine.event.DatabaseAdapter;
import gr.cti.eslate.database.engine.event.TableAddedEvent;
import gr.cti.eslate.database.engine.event.TableRemovedEvent;
import gr.cti.eslate.database.view.DBView;
import gr.cti.eslate.eslateToolBar.ESlateToolBar;
import gr.cti.eslate.eslateToolBar.ToolLocation;
import gr.cti.eslate.event.ColumnListener;
import gr.cti.eslate.event.ColumnMovedEvent;
import gr.cti.eslate.sharedObject.TableSO;
import gr.cti.eslate.tableModel.event.ActiveRecordChangedEvent;
import gr.cti.eslate.tableModel.event.ColumnAddedEvent;
import gr.cti.eslate.tableModel.event.ColumnEditableStateChangedEvent;
import gr.cti.eslate.tableModel.event.ColumnHiddenStateChangedEvent;
import gr.cti.eslate.tableModel.event.ColumnKeyChangedEvent;
import gr.cti.eslate.tableModel.event.ColumnRemovedEvent;
import gr.cti.eslate.tableModel.event.ColumnReplacedEvent;
import gr.cti.eslate.tableModel.event.DatabaseTableModelAdapter;
import gr.cti.eslate.tableModel.event.DatabaseTableModelListener;
import gr.cti.eslate.tableModel.event.RecordAddedEvent;
import gr.cti.eslate.tableModel.event.RecordRemovedEvent;
import gr.cti.eslate.tableModel.event.RowOrderChangedEvent;
import gr.cti.eslate.tableModel.event.SelectedRecordSetChangedEvent;
import gr.cti.eslate.tableModel.event.TableHiddenStateChangedEvent;
import gr.cti.eslate.tableModel.event.TableRenamedEvent;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ConfigurableEtchedBorder;
import gr.cti.eslate.utils.ESlateClipboard;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.StringBaseArray;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.objectspace.jgl.Array;
import com.objectspace.jgl.HashMap;

public class Database extends JPanel implements ESlatePart, Externalizable{
    //public static final String FORMAT_VERSION = "1.7.7";
    // For 201 --> 202 DatabaseTableViewStructures is not stored any more. The dbTables save their UI characteristics
    //                 themselves, so the Database saves the dbTables it contains.
    //
    public static final int FORMAT_VERSION = 202; //Database1.8.0
    static final long serialVersionUID = -2184520157829672626L;
    transient protected DBTable activeDBTable = null;
    int activeDBTableIndex = -1; //..
    transient protected ArrayList dbTables = new ArrayList();
//    transient protected Font UIFont = new Font("Helvetica", Font.PLAIN, 12); //..0

    transient MyMenuBar menu; //..
    transient protected JPanel toolMenuPanel = new JPanel(true); //..
    transient protected MouseListener menuMouseListener;
    transient JCheckBoxMenuItem miPrevDB1, miPrevDB2, miPrevDB3, miPrevDB4; //..

    static final ImageIcon radioButtonIcon = new ImageIcon(Database.class.getResource("images/purple-bullet.gif"));
    static final ImageIcon radioButtonResetIcon = new ImageIcon(Database.class.getResource("images/lightGray2.gif"));

    boolean tabTransparent = false;

    boolean componentModified = false;

    //Actions
    NewDBAction newDBAction;
    OpenDBAction openDBAction;
    SaveDBAction saveDBAction;
    FindAction findAction;
    FindNextAction findNextAction;
    FindPrevAction findPrevAction;
    public AddRecordAction addRecordAction;
    RemoveRecordAction removeRecordAction;
    RemoveSelRecordAction removeSelRecordAction;
    AddFieldAction addFieldAction;
    RemoveFieldAction removeFieldAction;
    FieldPropertiesAction fieldPropertiesAction;
    SelectAllRecsAction selectAllRecsAction;
    ClearRecAction clearRecAction;
    InvertRecAction invertRecAction;
    PromoteSelRecsAction promoteSelRecsAction;
    SortAscAction sortAscAction;
    SortDescAction sortDescAction;
    CutAction cutAction;
    CopyAction copyAction;
    PasteAction pasteAction;
    ImageEditAction imageEditAction;
    InsFilePathAction insFilePathAction;
    FieldSelectAllAction fieldSelectAllAction;
    CloseAction closeAction;
    DescriptionAction descriptionAction;
    RenameAction renameAction;
    SaveAsAction saveAsAction;
    ImportTableAction importTableAction;
    ExportTableAction exportTableAction;
    UserLevelAction userLevelAction;
    PropertiesAction propertiesAction;
    NewTableAction newTableAction;
    TableAutoAction tableAutoAction;
    TableAddAction tableAddAction;
    TableRemoveAction tableRemoveAction;
    TableSaveAsAction tableSaveAsAction;
    TableSaveUISettingsAction tableSaveUISettingsAction;
    TableDescriptionAction tableDescriptionAction;
    TableRenameAction tableRenameAction;
    TableHiddenAction tableHiddenAction;
    DatabaseTableSortAction databaseTableSortAction;
    TableUnionAction tableUnionAction;
    TableIntersectAction tableIntersectAction;
    TableJoinAction tableJoinAction;
    TableThJoinAction tableThJoinAction;
    TableAutoResizeOffAction tableAutoResizeOffAction;
    TableAutoResizeLastAction tableAutoResizeLastAction;
    TableAutoResizeAllAction tableAutoResizeAllAction;
    TableInfoAction tableInfoAction;
    TablePrefAction tablePrefAction;
    FieldEditableAction fieldEditableAction;
    FieldRemovableAction fieldRemovableAction;
    FieldKeyAction fieldKeyAction;
    FieldCalculateAction fieldCalculateAction;
    FieldHiddenAction fieldHiddenAction;
    FieldDoubleTypeAction fieldDoubleTypeAction;
    FieldStrTypeAction fieldStrTypeAction;
    FieldBoolTypeAction fieldBoolTypeAction;
    FieldDateTypeAction fieldDateTypeAction;
    FieldTimeTypeAction fieldTimeTypeAction;
    FieldURLTypeAction fieldURLTypeAction;
    FieldImageTypeAction fieldImageTypeAction;
    FieldClearSelectionAction fieldClearSelectionAction;
    FieldWidthAction fieldWidthAction;
    FieldFreezeAction fieldFreezeAction;


    /* Default settings for the entire database.
     */
    public String defaultDateFormat = "dd/MM/yyyy"; //..
    transient protected String defaultTimeFormat = "H:mm"; //..
    transient protected static boolean defaultNonEditableFieldsHighlighted = false; //..
    transient protected static Color defaultHighlightColor = Color.yellow; //..
    transient protected static Color defaultSelectionColor = new Color(0,0,128); //..
    transient protected static Color defaultActiveRecordColor = Color.yellow; //..
    transient protected static Color defaultGridColor = Color.black; //..
    transient protected static Color defaultBackgroundColor = Color.white; //..
    transient protected static Color defaultIntegerColor = Color.black; //..
    transient protected static Color defaultDoubleColor = Color.black; //..
    transient protected static Color defaultFloatColor = Color.black; //..
    transient protected static Color defaultBooleanColor = Color.black; //..
    transient protected static Color defaultStringColor = Color.black; //..
    transient protected static Color defaultDateColor = Color.black; //..
    transient protected static Color defaultTimeColor = Color.black; //..
    transient protected static Color defaultURLColor = Color.black; //..
    transient protected static Font defaultTableFont = new Font("Helvetica", Font.PLAIN, 12); //..
    transient protected static int defaultRowHeight = 20; //..
    transient protected static boolean defaultHorLinesVisible = true; //..
    transient protected static boolean defaultVertLinesVisible = true; //..
    transient protected static boolean defaultSimultaneousFieldRecordSelection = false; //..
    transient protected static boolean defaultHeaderIconsVisible = false; //..
    transient protected static int defaultAutoResizeMode = JTable.AUTO_RESIZE_OFF; //..
    transient protected boolean defaultExponentialNumberFormatUsed = false; //..
    transient protected boolean defaultShowIntegerPartOnly = false; //..
    transient protected boolean defaultDecimalSeparatorAlwaysShown = false; //..
    transient protected char defaultDecimalSeparator = ','; //..
    transient protected boolean defaultThousandSeparatorUsed = true; //..
    transient protected char defaultThousandSeparator = '.'; //..
    /** Enables or disables the use of the 'star-like' button on the upper-left corner of
     *  each jTable in the Database, to change the expansion status of the jTable's header.
     */
    private boolean tableHeaderExpansionChangeAllowed = true;

    /* Header icons.
     */
    private static ImageIcon stringTypeIcon = null;
    private static ImageIcon integerTypeIcon = null;
    private static ImageIcon floatTypeIcon = null;
    private static ImageIcon doubleTypeIcon = null;
    private static ImageIcon booleanTypeIcon = null;
    private static ImageIcon imageTypeIcon = null;
    private static ImageIcon dateTypeIcon = null;
    private static ImageIcon timeTypeIcon = null;
    private static ImageIcon urlTypeIcon = null;
    private static ImageIcon stringTypeKeyIcon = null;
    private static ImageIcon integerTypeKeyIcon = null;
    private static ImageIcon floatTypeKeyIcon = null;
    private static ImageIcon doubleTypeKeyIcon = null;
    private static ImageIcon booleanTypeKeyIcon = null;
    private static ImageIcon imageTypeKeyIcon = null;
    private static ImageIcon dateTypeKeyIcon = null;
    private static ImageIcon timeTypeKeyIcon = null;
    private static ImageIcon urlTypeKeyIcon = null;

    transient protected Database dbComponent;
    transient JPanel emptyPanel = null; //..
    transient JPanel labelPanel; //..
    transient JPanel emptyDB = null; //..
    transient JPanel emptyDBLabelPanel; //..
    transient JPanel dbIsLoadingPanel; //..
    transient ESlateFileDialog fileDialog = null;
    transient protected DBase db = null; //..
    transient protected JTabbedPane tabs = null;
    transient JPanel contentFrame;

    transient protected Array previousOpenDBs = new Array(); //..
    transient int numOfPreviouslyOpenedDBs = 0; //..
    transient Array miPrevDBArray = new Array(); //..
    transient Array addedMI = new Array(); //..
    transient final int PREVIOUS_DB_LIST_FIRST_MENU_ITEM_POS = 15; //..

    String currDelimiter = ",";
//1    boolean blockingNewRecord = false;
//1    boolean pendingNewRecord = false;

    protected transient Array columnListeners = new Array();
//    public transient QueryComponentListener queryComponentListener;
    protected transient gr.cti.eslate.database.engine.event.DatabaseListener databaseAdapter;
    protected transient DatabaseTableModelListener tableListener;
    protected transient PropertyChangeListener pcl, tablePcl;
    boolean iterateEvent = true;

    protected static int UPDATE_ACTIVE_TABLE1_POS = 0; //IE
    protected static int UPDATE_ACTIVE_TABLE2_POS = 1; //IE
    protected static int UPDATE_ACTIVE_TABLE3_POS = 2; //IE
    protected static int STANDARD_TOOLBAR_VISIBLE_POS = 3;   //IE
    protected static int FORMATTING_TOOLBAR_VISIBLE_POS = 4; //IE

    JPanel mainPanel;
    JPanel tabPanel;
    //MyStatusBar statusBar;
    FontMetrics timesRomanBold28Fm;
//    transient DatabaseSO databaseSO;
//    transient Locale locale;
    transient static ResourceBundle infoBundle = ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", Locale.getDefault());
    transient protected String errorStr, warningStr;
//    transient Plug databasePin,
    transient Plug tableImportPlug;
    transient FemaleSingleIFSingleConnectionProtocolPlug databaseImportPlug;
//    boolean foreignDatabase = false;
    boolean databaseEditable = false;

    transient boolean tableWasRemoved = false; //..

    transient JPopupMenu toolBarMenu = new JPopupMenu();
    transient JCheckBoxMenuItem standardToolBarMenuItem;
    transient JCheckBoxMenuItem formattingToolBarMenuItem;
    //transient JPanel standardToolBarPanel/*, formattingToolBarPanel*/;
    JPanel standardToolBarPanel, formattingToolBarPanel;
    transient ColorPanel colorPanel;
    transient GridChooser gridChooserPanel;
    transient JPopupMenu colorPopupMenu, gridPopupMenu;
    transient JPopupMenu visiblePopupMenu;
    protected final static int BACKGROUND_COLOR_TYPE = 0; //IE
    protected final static int FOREGROUND_COLOR_TYPE = 1; //IE
    protected final static int GRID_COLOR_TYPE = 2; //IE
    protected final static int SELECTION_COLOR_TYPE = 3; //IE
    protected final static int ACTIVE_RECORD_COLOR_TYPE = 4; //IE
    protected final static int NUMBER_COLOR_TYPE = 5; //IE
    protected final static int ALPHANUMERIC_COLOR_TYPE = 6; //IE
    protected final static int BOOLEAN_COLOR_TYPE = 7; //IE
    protected final static int DATE_COLOR_TYPE = 8; //IE
    protected final static int TIME_COLOR_TYPE = 9; //IE
    protected final static int URL_COLOR_TYPE = 10; //IEprotected

    /* This variable is used to cancel the action behind toolbar buttons
     * which display pop-up menus, when the button is clicked even number of times.
     */
    transient protected int mouseClicksSinceEnteredButton; //IE
    transient protected FindDialog findDialog = null;//IE
    private static final Color tablePinColor = new Color(102, 88, 187);
    transient protected boolean scrollToActiveRecord = true;

    public static final int NOVICE_USER_MODE = 0;
    public static final int ADVANCED_USER_MODE = 1;
    transient protected int userMode = NOVICE_USER_MODE;  //IE
    protected static final String adminPass = "e"; //IE
    transient protected boolean displayHiddenTables; //IE
    transient private boolean destroyInProgress = false;
    transient protected boolean hasBeenDestroyed = false;
    transient private boolean acceptingConnection = false;

    boolean dataBaseSaved = false;

    transient protected ESlateHandle eSlateHandle = null; //ESlate.registerPart(this);

    /* This variable is used to determine whether the IconEditor component is present in the current
     * environment. This is checked in the constructor.
     */
    transient static protected Class iconEditorClass = null;
    //MyStandardToolbar standardToolBar;
/* This structure holds the remoteDatabaseTableModelListeners. These are
     * DatabaseTableModelListeners which are attached to the Tables created when
     * tables are exported from the Database, through the jTable plugs. This structure
     * keeps each listener associated with the local and the remote Tables.
     */
    transient RemoteTableModelListenerStructure remoteTableModelListenerStructure = new RemoteTableModelListenerStructure();
    /* Holds all the view settings for the DBase currently viewed in the Database component. */
    DBView dbView = new DBView();
    /* Persists the TableViewStructures for the dbTables of the Database. It created in
     * writeExternal() and is only used while the Database component is restored as part
     * of the microworld restore process. Shouldn't be used anywhere else.
     */
    DatabaseTableViewStructures databaseTableViewStructures = null;
    /* The parent of type Frame that contains the Database. It is used for modal
     * dialog ownership.
     */
    Frame topLevelFrame = null;
    private final static String compo_version = "1.9.3";
    Border toolMenuPanelBorder = new ConfigurableEtchedBorder(ConfigurableEtchedBorder.RAISED, Color.gray, Color.black, false, true, false, false);
    /** Timers which measure functions of the Table */
    PerformanceTimer loadTimer, saveTimer, constructorTimer, initESlateAspectTimer;
    /* The listener that notifies about changes to the state of the PerformanceManager */
    PerformanceListener perfListener = null;
    /** Flag which indicates whether the DataBase runs on a Windows machine */
    boolean isWindowsPlatform = false;
    FormattingToolbarController formattingToolbarController;
    protected StandardToolbarController standardToolbarController;
    protected StatusToolbarController statusToolbarController;
    /** A hash map which stores the DBTables restored by Database's readExternal(), keyed by the title of the Table
     *  these DBTables used to show, when stored.
     */
    private HashMap restoredDBTables = new HashMap();


    public ESlateHandle getESlateHandle() {
        if (eSlateHandle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);

            eSlateHandle = ESlate.registerPart(this);

            eSlateHandle.addESlateListener(new ESlateAdapter() {
                public void handleDisposed(HandleDisposalEvent e) {
                    destroy();
                    PerformanceManager pm = PerformanceManager.getPerformanceManager();
                    pm.removePerformanceListener(perfListener);
                    perfListener = null;
                }
                public void disposingHandle(HandleDisposalEvent e) {
                    // nothing has changed so return
                    if (db == null && !componentModified) return;

                    // use dbModified in order to check both db.isModified and componentModified
                    // and to avoid nullPointerException in the case that db is null
                    boolean dbModified = false;
                    if (db != null)
                        dbModified = db.isModified();

                    // Component is closing atomically
                    if (e.cancellationRespected){
                        if (dbModified){
                            e.stateChanged = true;
//System.out.println("Here 1");
                            int question = JOptionPane.showConfirmDialog(null,infoBundle.getString("SaveDB"), infoBundle.getString("warnClose"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
//System.out.println("Here 2");
                            // yes--> Save the db and quit
                            // if pressed cancel in fileDialog then veto
                            if (question == JOptionPane.YES_OPTION){
                                saveDBAction.execute();
                                if (db.isModified())
                                    e.vetoDisposal = true;
                                else
                                    closeDatabase(false, false);
                            }
                            // no --> don't save the db and quit
                            else if (question == JOptionPane.NO_OPTION){
                                closeDatabase(false, false);
                            }
                            // cancel--> veto
                            else{
                                e.vetoDisposal = true;
                            }
                        }
                    }
                    // close microworld
                    else{
                        // inform the microworld that smth has changed
                        if (dbModified || componentModified){
                            e.stateChanged = true;
                        }
                    }
                }
                public void componentNameChanged(ComponentNameChangedEvent e) {
                }
            });
            eSlateHandle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.DatabasePrimitives");
            eSlateHandle.setInfo(new ESlateInfo(
                        infoBundle.getString("compo") + compo_version,
                        new String[] {
                            infoBundle.getString("part"),
                            infoBundle.getString("development"),
                            infoBundle.getString("contribution"),
                            infoBundle.getString("copyright")
                        }));

            if (formattingToolbarController.getToolbar() != null)
                eSlateHandle.add(formattingToolbarController.getToolbar().getESlateHandle());
            if (standardToolbarController.getToolbar() != null)
                eSlateHandle.add(standardToolbarController.getToolbar().getESlateHandle());
            if (statusToolbarController.getToolbar() != null)
                eSlateHandle.add(statusToolbarController.getToolbar().getESlateHandle());

            SharedObjectListener sol1 = new SharedObjectListener() {
                public void handleSharedObjectEvent(SharedObjectEvent soe) {
                    if (hasBeenDestroyed)
                        return;
                    SharedObject so=soe.getSharedObject();
                    if (so instanceof TableSO) {
                        TableSO tableSO = (TableSO) so;

                        Table remoteTable = tableSO.getTable();
                        Table localTable = remoteTable;
                        if (localTable == null) return;
                    }
                }
            };

            /* The Database's Table multiple input pin.
             * Through this pin tables for other sources (i.e. other Database components are inserted
             * in the Database component.
             */
            try {
                tableImportPlug = new MultipleInputPlug(this.eSlateHandle, infoBundle, "Table", tablePinColor, gr.cti.eslate.sharedObject.TableSO.class, sol1);
                eSlateHandle.addPlug(tableImportPlug);
                tableImportPlug.setVisible(false);
                tableImportPlug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        /* Redirect any connection to the 'tableImportPlug' of the hosted
                         * DBase.
                         */
                        Plug remotePin = e.getPlug();
                        try{
                            remotePin.disconnectPlug(tableImportPlug);
                        }catch (PlugNotConnectedException exc) {}
                        if (db == null)
                            createNewDBase(true);
                        Plug dbaseTableImportPin = db.getESlateHandle().getPlugs()[1];
                        try{
                            remotePin.connectPlug(dbaseTableImportPin);
                        }catch (PlugNotConnectedException exc) {
                        }
                        setCursor(defaultCursor);
                    }
                });
                tableImportPlug.addDisconnectionListener(new DisconnectionListener() {
                    public void handleDisconnectionEvent(DisconnectionEvent e) {
                        if (hasBeenDestroyed)
                            return;

                        /* When another component's jTable pin is disconnected from the Database,
                         * the the jTable which was imported in the Database stops being connected
                         * to the external jTable and becomes an internal Database jTable. Therefore
                         * the "importedTables" HashTable has to be updated.
                         */
                        Plug pin = e.getPlug();
                        String tableName = null;
                        Table table = ((TableSO) ((SharedObjectPlug) pin).getSharedObject()).getTable();
                        DBTable dbTable = getDBTableOfTable(table);
                        if (dbTable != null)
                            removeDBTable(dbTable, false);
                    }
                });

            } catch (InvalidPlugParametersException e) {
                ESlateOptionPane.showMessageDialog(this, e.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } catch (PlugExistsException e) {
                ESlateOptionPane.showMessageDialog(this, e.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            /* The Database's DBase single input pin.
             * Through this pin DBases for other sources (i.e. other Database components are inserted
             * in the Database component.
             */
            try {
                databaseImportPlug = new FemaleSingleIFSingleConnectionProtocolPlug(
                                            eSlateHandle,
                                            infoBundle,
                                            "DatabaseEntry",
                                            Color.magenta,
                                            DBase.class);
                eSlateHandle.addPlug(databaseImportPlug);
                databaseImportPlug.setHostingPlug(true);
                databaseImportPlug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        DBase newDBase = (DBase) e.getPlug().getHandle().getComponent();
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        if (newDBase != db) {
                            dbComponent.getDB(newDBase, true);
                            setActionStatus();
                            setDBMenuRestStatus();
                            setTableMenuRestStatus();
                            setFieldMenuRestStatus();
                        }
                        setCursor(defaultCursor);
                    }
                });
                databaseImportPlug.addDisconnectionListener(new DisconnectionListener() {
                    public void handleDisconnectionEvent(DisconnectionEvent e) {
                        if (acceptingConnection || hasBeenDestroyed)
                            return;
                        ESlateHandle dbHandle = null;
                        if (db != null) {
                            dbHandle = db.getESlateHandle();
                            closeDatabase(false, false);

                        }
                        setActionStatus();
                        setDBMenuRestStatus();
                        setTableMenuRestStatus();
                        setFieldMenuRestStatus();
                    }
                });

            } catch (InvalidPlugParametersException e) {
                ESlateOptionPane.showMessageDialog(this, e.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } catch (PlugExistsException e) {
                ESlateOptionPane.showMessageDialog(this, e.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            try{
                eSlateHandle.setUniqueComponentName(infoBundle.getString("compo1"));
            }catch (RenamingForbiddenException exc) {}

            pm.eSlateAspectInitEnded(this);
            pm.stop(initESlateAspectTimer);
            pm.displayTime(initESlateAspectTimer, eSlateHandle, "", "ms");
        }
        return eSlateHandle;
    }

    protected void addDBaseHandleToDatabaseHandle(DBase dbase) {
        if (dbase == null || eSlateHandle == null) return;
        ESlateHandle dbHandle = dbase.getESlateHandle();

        /* The DBase's handle is added to the Database's handle only if it does
         * not already have a father.
         */
        if (dbHandle.getParentHandle() == null) {
            eSlateHandle.add(dbHandle);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(500, 400);
    }

    public void destroy() {
        java.lang.Runtime rt = java.lang.Runtime.getRuntime();

        if (destroyInProgress) { // || destroyCalled()) {
            System.out.println("Database destroy() rejected. destroyInProgress: " + destroyInProgress); // + ", destroyCalled(): " + destroyCalled());
            return;
        }
        destroyInProgress = true;
        hasBeenDestroyed = true;

        try{
            tableImportPlug.disconnect();
            eSlateHandle.removePlug(tableImportPlug);
        }catch (Exception exc) {
        }

        tableImportPlug = null;

        // PROBLEM with ColumnRendererEditors
        if (dbTables != null) {
            for (int i=0; i<dbTables.size(); i++) {
                for (int k=0; k<((DBTable) dbTables.get(i)).tableColumns.size(); k++) {
                    DefaultCellEditor editor = ((DBTable) dbTables.get(i)).tableColumns.get(k).editor;
//cf                    ColumnRendererEditor cr =  ((DBTable) dbTables.get(i)).tableColumns.get(k).colRendererEditor;
//cf                    if (cr.keyListener2 != null)
//cf                        cr.editorComponent.removeKeyListener(cr.keyListener2);
//cf                    if (cr.keyListener1 != null)
//cf                        cr.editorComponent.removeKeyListener(cr.keyListener1);
//cf                    cr.keyListener2 = null;
//cf                    cr.keyListener1 = null;
//cf                    cr.dbTable = null;
//cf                    cr.renderer = null;
//cf                    cr.editor = null;
/*cf                    if (cr.booleanValues != null) {
                        BooleanValues bv = cr.booleanValues;
                        bv.removeAllItems();
                        for (int j=0;j<bv.itemListenerArray.size();j++) {
                            java.awt.event.ItemListener il = (java.awt.event.ItemListener) bv.itemListenerArray.at(j);
                            if (il != null)
                                bv.removeItemListener(il);
                        }
                        if (bv.actionListener != null)
                            bv.removeActionListener(bv.actionListener);
                        bv.itemListenerArray.clear();
                        bv.itemListenerArray = null;
                        bv.actionListener = null;
                    }
                    cr.booleanValues = null;  // remove static
                    cr.editorComponent = null; //1.0.1, rendererComponent;
                    cr.dbTable = null;
                    cr.fld = null;
*/
                }
            }
        }
        if (db != null) {
            if (databaseAdapter != null)
                db.removeDatabaseListener(databaseAdapter);
            db.unregisterVetoListener();
            if (tableListener != null) {
                ArrayList ctables = db.getTables();
                for (int i=0; i<ctables.size(); i++)
                    ((Table) ctables.get(i)).removeTableModelListener(tableListener);
            }
            if (tablePcl != null) {
                for (int i=0; i<db.getTableCount(); i++)
                    db.getTableAt(i).removePropertyChangeListener(tablePcl);
            }
            tablePcl = null;
            if (pcl != null)
                db.removePropertyChangeListener(pcl);
            db = null;
        }

        activeDBTable = null;

        if (menu.databaseMenu != null) {
            menu.databaseMenu.removeAll();
            menu.databaseMenu = null;
        }

        if (menu.tableMenu != null) {
            menu.tableMenu.removeAll();
            menu.tableMenu = null;
        }

        if (menu.fieldMenu != null) {
            menu.fieldMenu.removeAll();
            menu.fieldMenu = null;
        }
        if (menu.recordMenu != null) {
            menu.recordMenu.removeAll();
            menu.recordMenu = null;
        }

        if (menu != null) {
            if (menuMouseListener != null)
                menu.removeMouseListener(menuMouseListener);
            menu.removeAll();
            menu = null;
        }

        if (standardToolBarPanel != null){
            standardToolBarPanel.removeAll();
            standardToolBarPanel = null;
        }

        if (formattingToolBarPanel != null){
            formattingToolBarPanel.removeAll();
            formattingToolBarPanel = null;
        }

        standardToolbarController.destroyToolbar();
        formattingToolbarController.destroyToolbar();
        statusToolbarController.destroyToolbar();

        if (toolMenuPanel != null) {
            toolMenuPanel.removeAll();
            toolMenuPanel = null;
        }
        menuMouseListener = null;

        defaultDateFormat = null;
        defaultTimeFormat = null;
        defaultHighlightColor = null;
        defaultSelectionColor = null;
        defaultActiveRecordColor = null;
        defaultGridColor = null;
        defaultBackgroundColor = null;
        defaultIntegerColor = null;
        defaultDoubleColor = null;
        defaultBooleanColor = null;
        defaultStringColor = null;
        defaultDateColor = null;
        defaultTimeColor = null;
        defaultURLColor = null;
        defaultTableFont = null;

        dbComponent = null;
        if (emptyPanel != null) {
            emptyPanel.removeAll();
            emptyPanel = null;
        }
        if (labelPanel != null) {
            labelPanel.removeAll();
            labelPanel = null;
        }


        if (emptyDB != null) {
            emptyDB.removeAll();
            emptyDB = null;
        }
        if (emptyDBLabelPanel != null) {
            emptyDBLabelPanel.removeAll();
            emptyDBLabelPanel = null;
        }
        if (dbIsLoadingPanel != null) {
            dbIsLoadingPanel.removeAll();
            dbIsLoadingPanel = null;
        }
        tabs = null;
        if (contentFrame != null) {
            contentFrame.removeAll();
            contentFrame = null;
        }

        if (previousOpenDBs != null) {
            previousOpenDBs.clear(); previousOpenDBs = null;
        }
        if (miPrevDBArray != null) {
            miPrevDBArray.clear(); miPrevDBArray = null;
        }
        if (addedMI != null) {
            addedMI.clear(); addedMI = null;
        }
        currDelimiter = null;
        if (columnListeners != null) {
            columnListeners.clear(); columnListeners = null;
        }
        databaseAdapter = null;
        tableListener = null;
        pcl = null;
        tablePcl = null;

        if (mainPanel != null) {
            mainPanel.removeAll();
            mainPanel = null;
        }
        if (tabPanel != null) {
            tabPanel.removeAll();
            tabPanel = null;
        }

        /*statusBar.destroyElements();
        if (statusBar != null) {
            statusBar.removeAll();
            statusBar = null;
        }*/
        timesRomanBold28Fm = null;
//1        locale = null;
//1        infoBundle = null;
        errorStr = null; warningStr = null;

        if (dbTables != null) {
            for (int i=0; i<dbTables.size(); i++)
                ((DBTable) dbTables.get(i)).removeAll();
            dbTables.clear();
            dbTables = null;
        }

        if (toolBarMenu != null) {
            toolBarMenu.removeAll();
            toolBarMenu = null;
        }
        standardToolBarMenuItem = null;
        formattingToolBarMenuItem = null;

        //standardToolBar.destroyElements();
        /*if (standardToolBarPanel != null) {
            standardToolBarPanel.removeAll();
            standardToolBarPanel = null;
        }*/
        if (colorPanel != null) {
            colorPanel.removeAll();
            colorPanel = null;
        }
        if (gridChooserPanel != null) {
            gridChooserPanel.removeAll();
            gridChooserPanel = null;
        }
        if (colorPopupMenu != null) {
            colorPopupMenu.removeAll();
            colorPopupMenu = null;
        }
        if (gridPopupMenu != null) {
            gridPopupMenu.removeAll();
            gridPopupMenu = null;
        }
        if (visiblePopupMenu != null) {
            visiblePopupMenu.removeAll();
            visiblePopupMenu = null;
        }
        if (findDialog != null) {
            findDialog.removeAll();
            findDialog = null;
        }
        destroyInProgress = false;

        rt.runFinalization();
        rt.gc();
    }


    public Database() {
        super();
//1        locale = ESlateMicroworld.getCurrentLocale();
//1        infoBundle = ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.constructionStarted(this);
        pm.init(constructorTimer);

        initializeCommon();
        initialize();

        pm.constructionEnded(this);
        pm.stop(constructorTimer);
        pm.displayTime(constructorTimer, "������������� ����������� ������", "ms");
    }

    public Database(java.io.ObjectInput oi) throws Exception{
        super();
//1        locale=ESlateMicroworld.getCurrentLocale();
//1        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.constructionStarted(this);
        pm.init(constructorTimer);

        initializeCommon();

        pm.stop(constructorTimer);
        pm.constructionEnded(this);

        getESlateHandle();
        initialize(oi);
        pm.displayTime(constructorTimer, "������������� ����������� ������", "ms");
    }

    private void initializeCommon(){
        // Actions
        newDBAction = new NewDBAction(this, "NewAction");
        openDBAction = new OpenDBAction(this, "OpenAction");
        saveDBAction = new SaveDBAction(this, "SaveAction");
        findAction = new FindAction(this, "FindAction");
        findNextAction = new FindNextAction(this, "FindNextAction");
        findPrevAction = new FindPrevAction(this, "FindPrevAction");
        addRecordAction = new AddRecordAction(this, "AddRecordAction");
        removeRecordAction = new RemoveRecordAction(this, "RemoveRecordAction");
        removeSelRecordAction = new RemoveSelRecordAction(this, "RemoveSelRecordAction");
        addFieldAction = new AddFieldAction(this, "AddFieldAction");
        removeFieldAction = new RemoveFieldAction(this, "RemoveFieldAction");
        fieldPropertiesAction = new FieldPropertiesAction(this, "FieldPropertiesAction");
        selectAllRecsAction = new SelectAllRecsAction(this, "SelectAllRecsAction");
        clearRecAction = new ClearRecAction(this, "ClearRecAction");
        invertRecAction = new InvertRecAction(this, "InvertRecAction");
        promoteSelRecsAction = new PromoteSelRecsAction(this, "PromoteSelRecsAction");
        sortAscAction = new SortAscAction(this, "SortAscAction");
        sortDescAction = new SortDescAction(this, "SortDescAction");
        cutAction = new CutAction(this, "CutAction");
        copyAction = new CopyAction(this, "CopyAction");
        pasteAction = new PasteAction(this, "PasteAction");
        imageEditAction = new ImageEditAction(this, "ImageEditAction");
        insFilePathAction = new InsFilePathAction(this, "InsertFilePathAction");
        fieldSelectAllAction = new FieldSelectAllAction(this, "FieldSelectAllAction");
        closeAction = new CloseAction(this, "CloseAction");
        descriptionAction = new DescriptionAction(this, "DescriptionAction");
        renameAction = new RenameAction(this, "RenameAction");
        saveAsAction = new SaveAsAction(this, "SaveAsAction");
        importTableAction = new ImportTableAction(this, "ImportTableAction");
        exportTableAction = new ExportTableAction(this, "ExportTableAction");
        userLevelAction = new UserLevelAction(this, "UserLevelAction");
        propertiesAction = new PropertiesAction(this, "PropertiesAction");
        newTableAction = new NewTableAction(this, "NewTableAction");
        tableAutoAction = new TableAutoAction(this, "TableAutoAction");
        tableAddAction = new TableAddAction(this, "TableAddAction");
        tableRemoveAction = new TableRemoveAction(this, "TableRemoveAction");
        tableSaveAsAction = new TableSaveAsAction(this, "TableSaveAsAction");
        tableSaveUISettingsAction = new TableSaveUISettingsAction(this, "TableSaveUISettingsAction");
        tableDescriptionAction = new TableDescriptionAction(this, "TableDescriptionAction");
        tableRenameAction = new TableRenameAction(this, "TableRenameAction");
        tableHiddenAction = new TableHiddenAction(this, "TableHiddenAction");
        databaseTableSortAction = new DatabaseTableSortAction(this, "DatabaseTableSortAction");
        tableUnionAction = new TableUnionAction(this, "TableUnionAction");
        tableIntersectAction = new TableIntersectAction(this, "TableIntersectAction");
        tableJoinAction = new TableJoinAction(this, "TableJoinAction");
        tableThJoinAction = new TableThJoinAction(this, "TableThJoinAction");
        tableAutoResizeOffAction = new TableAutoResizeOffAction(this, "TableAutoResizeOffAction");
        tableAutoResizeLastAction = new TableAutoResizeLastAction(this, "TableAutoResizeLastAction");
        tableAutoResizeAllAction = new TableAutoResizeAllAction(this, "TableAutoResizeAllAction");
        tableInfoAction = new TableInfoAction(this, "TableInfoAction");
        tablePrefAction = new TablePrefAction(this, "TablePrefAction");
        fieldEditableAction = new FieldEditableAction(this, "FieldEditableAction");
        fieldRemovableAction = new FieldRemovableAction(this, "FieldRemovableAction");
        fieldKeyAction = new FieldKeyAction(this, "FieldKeyAction");
        fieldCalculateAction = new FieldCalculateAction(this, "FieldCalculateAction");
        fieldHiddenAction = new FieldHiddenAction(this, "FieldHiddenAction");
        fieldDoubleTypeAction = new FieldDoubleTypeAction(this, "FieldDoubleTypeAction");
        fieldStrTypeAction = new FieldStrTypeAction(this, "FieldStrTypeAction");
        fieldBoolTypeAction = new FieldBoolTypeAction(this, "FieldBoolTypeAction");
        fieldDateTypeAction = new FieldDateTypeAction(this, "FieldDateTypeAction");
        fieldTimeTypeAction = new FieldTimeTypeAction(this, "FieldTimeTypeAction");
        fieldURLTypeAction = new FieldURLTypeAction(this, "FieldURLTypeAction");
        fieldImageTypeAction = new FieldImageTypeAction(this, "FieldImageTypeAction");
        fieldClearSelectionAction = new FieldClearSelectionAction(this, "FieldClearSelectionAction");
        fieldWidthAction = new FieldWidthAction(this, "FieldWidthAction");
        fieldFreezeAction = new FieldFreezeAction(this, "FieldFreezeAction");

        formattingToolbarController = new FormattingToolbarController(this);
        standardToolbarController = new StandardToolbarController(this);
        statusToolbarController = new StatusToolbarController(this);

        standardToolBarPanel = new JPanel();
        formattingToolBarPanel = new JPanel();
        standardToolBarPanel.setLayout(new BoxLayout(standardToolBarPanel, BoxLayout.X_AXIS));
        formattingToolBarPanel.setLayout(new BoxLayout(formattingToolBarPanel, BoxLayout.X_AXIS));

        /* Set the Windows flag. This is needed to know when to use the native utilities, like
         * the clipboard functions.
         */
        isWindowsPlatform = System.getProperty("os.name").toLowerCase().startsWith("windows");
        errorStr = infoBundle.getString("Error");
        warningStr = infoBundle.getString("Warning");

        try{
            iconEditorClass = Class.forName("gr.cti.eslate.imageEditor.ImageEditor");
        }catch (ClassNotFoundException exc) {
            iconEditorClass = null;
        }

        standardToolBarMenuItem = new JCheckBoxMenuItem(infoBundle.getString("Standard"));
//        standardToolBarMenuItem.setFont(UIFont);
        formattingToolBarMenuItem = new JCheckBoxMenuItem(infoBundle.getString("Formatting"));
//        formattingToolBarMenuItem.setFont(UIFont);

        mainPanel = new JPanel(true);
        this.contentFrame = mainPanel;

        tabPanel = new JPanel(true);
        tabPanel.setLayout(new BorderLayout(0, 1));

        /* Create the status bar.
         */
        //statusBar = new MyStatusBar(this);
        //tabPanel.add(statusBar, BorderLayout.SOUTH);

        dbComponent = this;
        /* Set the L&F to the Windows L&F.
         */
        mainPanel.setLayout(new BorderLayout(5,0));
        /* Create the toolbars.
         */
        //toolMenuPanel.setLayout(new GridLayout(3, 1));
        toolMenuPanel.setLayout(new BoxLayout(toolMenuPanel, BoxLayout.Y_AXIS));
        /* Create the toolbar pop-up menu.
         */
        toolBarMenu.setLightWeightPopupEnabled(false);
        toolBarMenu.setBorderPainted(true);

        toolBarMenu.add((JMenuItem) standardToolBarMenuItem);
        toolBarMenu.add((JMenuItem) formattingToolBarMenuItem);

        standardToolBarMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setStandardToolbarVisible(standardToolBarMenuItem.isSelected());
            }
        });

        formattingToolBarMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setFormattingToolBarVisible(formattingToolBarMenuItem.isSelected());
            }
        });

        menuMouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (visiblePopupMenu != null && visiblePopupMenu.isVisible())
                    visiblePopupMenu.setVisible(false);
                if (e.getModifiers() == e.BUTTON3_MASK) {
                    if (isStandardToolbarVisible())
                        standardToolBarMenuItem.setSelected(true);
                    if (isFormattingToolBarVisible())
                        formattingToolBarMenuItem.setSelected(true);
                    toolBarMenu.show(dbComponent.toolMenuPanel, e.getX(), e.getY());
                }
            }
        };

        menu = new MyMenuBar(this);

        miPrevDB1 = new JCheckBoxMenuItem();
//        miPrevDB1.setFont(UIFont);
        miPrevDB1.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        miPrevDB2 = new JCheckBoxMenuItem();
//        miPrevDB2.setFont(UIFont);
        miPrevDB2.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        miPrevDB3 = new JCheckBoxMenuItem();
//        miPrevDB3.setFont(UIFont);
        miPrevDB3.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        miPrevDB4 = new JCheckBoxMenuItem();
//        miPrevDB4.setFont(UIFont);
        miPrevDB4.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        miPrevDB1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toolMenuPanel.repaint();
                openDB(miPrevDB1.getText());
            }
        });
        miPrevDB2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toolMenuPanel.repaint();
                openDB(miPrevDB2.getText());
            }
        });
        miPrevDB3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toolMenuPanel.repaint();
                openDB(miPrevDB3.getText());
            }
        });
        miPrevDB4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toolMenuPanel.repaint();
                openDB(miPrevDB4.getText());
            }
        });


        miPrevDBArray.add(miPrevDB1);
        miPrevDBArray.add(miPrevDB2);
        miPrevDBArray.add(miPrevDB3);
        miPrevDBArray.add(miPrevDB4);
        menu.addMouseListener(menuMouseListener);

        toolMenuPanel.add(menu);
        toolMenuPanel.setBorder(toolMenuPanelBorder);
        mainPanel.add(toolMenuPanel, BorderLayout.NORTH);

        /* Create the pop-up menu which contains the color choose panel
         * This pop-up menu is displayed when one of the color buttons
         * in the toolbar is pressed.
         */
        colorPopupMenu = new JPopupMenu();
        colorPanel = new ColorPanel(this, null);
        colorPanel.setFocusable(false);
        colorPanel.getColorBoxChooser().addActiveColorListener(new ActiveColorListener() {
            public void activeColorChanged(ActiveColorEvent e) {
                ColorBoxChooser cbc = (ColorBoxChooser) e.getSource();
                Color color = cbc.getActiveColor();
                if (colorPopupMenu.isVisible())
                    colorPopupMenu.setVisible(false);
                int colorType = cbc.getColorType();
                boolean updateAllTables = colorPanel.getUpdateAllTables();
                switch (colorType) {
                    case BACKGROUND_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setBackgroundColor(color);
                        }else
                            activeDBTable.setBackgroundColor(color);
                        break;
                    case FOREGROUND_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setDataTypeColors(color, color, color, color, color, color, color, color);
                        }else
                            activeDBTable.setDataTypeColors(color, color, color, color, color, color, color, color);
                        break;
                    case NUMBER_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setDataTypeColor(DoubleTableField.DATA_TYPE, color);
                        }else
                            activeDBTable.setDataTypeColor(DoubleTableField.DATA_TYPE, color);
                        break;
                    case ALPHANUMERIC_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setDataTypeColor(StringTableField.DATA_TYPE, color);
                        }else
                            activeDBTable.setDataTypeColor(StringTableField.DATA_TYPE, color);
                        break;
                    case BOOLEAN_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setDataTypeColor(BooleanTableField.DATA_TYPE, color);
                        }else
                            activeDBTable.setDataTypeColor(BooleanTableField.DATA_TYPE, color);
                        break;
                    case DATE_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setDataTypeColor(DateTableField.DATA_TYPE, color);
                        }else
                            activeDBTable.setDataTypeColor(DateTableField.DATA_TYPE, color);
                        break;
                    case TIME_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setDataTypeColor(TimeTableField.DATA_TYPE, color);
                        }else
                            activeDBTable.setDataTypeColor(TimeTableField.DATA_TYPE, color);
                        break;
                    case URL_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setDataTypeColor(URLTableField.DATA_TYPE, color);
                        }else
                            activeDBTable.setDataTypeColor(URLTableField.DATA_TYPE, color);
                        break;
                    case GRID_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setGridColor(color);
                        }else
                            activeDBTable.setGridColor(color);
                        break;
                    case SELECTION_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setSelectionBackground(color);
                        }else
                            activeDBTable.setSelectionBackground(color);
                        break;
                    case ACTIVE_RECORD_COLOR_TYPE:
                        if (updateAllTables) {
                            for (int i=0; i<dbTables.size(); i++)
                                ((DBTable) dbTables.get(i)).setActiveRecordColor(color);
                        }else
                            activeDBTable.setActiveRecordColor(color);
                        break;
                }
                activeDBTable.repaint();

            }
        });

        colorPopupMenu.add(colorPanel);
        colorPopupMenu.pack();
        colorPopupMenu.setLightWeightPopupEnabled(true);
        colorPopupMenu.setBorderPainted(true);

        /* This pop-up menu displays the different types of grids, from which
         * the user may choose one.
         */
        gridPopupMenu = new JPopupMenu();
        gridChooserPanel = new GridChooser(activeDBTable);
        gridChooserPanel.setFocusable(false);
        gridPopupMenu.add(gridChooserPanel);
        gridPopupMenu.pack();
        gridPopupMenu.setLightWeightPopupEnabled(true);
        gridPopupMenu.setBorderPainted(true);

        /* Initialize the visiblePopupMenu variable
         */
        visiblePopupMenu = colorPopupMenu;

        emptyPanel = new JPanel(true) {
            public void updateUI() {
                super.updateUI();
                setBackground(UIManager.getColor("controlShadow"));
            }
            public boolean isFocusable() {
                return false;
            }
        };
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        mainPanel.add(emptyPanel, BorderLayout.CENTER);

        JLabel noDatabase = new JLabel(infoBundle.getString("DatabaseMsg35"));
        Font labelFont1 = new Font("TimesRoman", Font.BOLD, 28);
        noDatabase.setFont(labelFont1);
        timesRomanBold28Fm = Toolkit.getDefaultToolkit().getFontMetrics(labelFont1);
        noDatabase.setForeground(new Color(0,0,128));
        noDatabase.setAlignmentX(CENTER_ALIGNMENT);
        noDatabase.setAlignmentY(CENTER_ALIGNMENT);
        noDatabase.setFocusable(false);

        labelPanel = new JPanel(true);
        int stringWidth = timesRomanBold28Fm.stringWidth(infoBundle.getString("DatabaseMsg35"));
        labelPanel.setPreferredSize(new Dimension(0, 0));
        labelPanel.setMinimumSize(new Dimension(0, 0));
        labelPanel.setMaximumSize(new Dimension(0, 0));
        labelPanel.setFocusable(false);
        emptyDB = createEmptyDBPanel();
        dbIsLoadingPanel = createDBIsLoadingPanel();

        initDatabaseAdapter();
        initDatabaseTableModelListener();
        initPropertyChangeListener();
        initTablePropertyChangeListener();

//        javax.swing.FocusManager.disableSwingFocusManager();

        /* Initialize the key bindings for JTextFields, so that the
         * cut/copy/paste operations  feed the ESlateClipboard and not
         * the system clipboard.
         */
        removeDefaultKeyBindings();

        emptyPanel.add(Box.createGlue());
        emptyPanel.add(labelPanel);
        emptyPanel.add(Box.createGlue());

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                if (event.getID() == AncestorEvent.ANCESTOR_ADDED) {
                    Component topComponent = Database.this.getTopLevelAncestor();
                    if (Frame.class.isAssignableFrom(topComponent.getClass()))
                        topLevelFrame = (Frame) topComponent;
                }
            }
            public void ancestorRemoved(AncestorEvent event) {}
            public void ancestorMoved(AncestorEvent event) {}
        });

        setFocusable(false);
    }

    private void initialize(){
        setStandardToolbarVisible(true);
        setFormattingToolBarVisible(true);
        setStatusBarVisible(true);
        setBorder(new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED));
        /* Set the status of the tools in the standard toolbar.
         */
        setActionStatus();
        setDBMenuRestStatus();
        setTableMenuRestStatus();
        setFieldMenuRestStatus();
        formattingToolbarController.setToolBarStatus();

        /* Adjust the component behaviour based on user mode
         */
        if (userMode == NOVICE_USER_MODE) {
            try{
                if (menu.miTableHidden != null)
                    menu.tableMenu.remove(menu.miTableHidden);
            }catch (NullPointerException exc) {
                System.out.println("Peculiar NullPointerException in tableMenu.remove()");
            }
            menu.miTableAuto.setVisible(false);

            try{
                menu.fieldMenu.remove(menu.ci1);
                menu.fieldMenu.remove(menu.ci2);
                menu.fieldMenu.remove(menu.ci5);
            }catch (NullPointerException exc) {
                System.out.println("Peculiar NullPointerException in tableMenu.remove()");
            }
        }else{
            menu.tableMenu.insert(menu.miTableHidden, 9);
            menu.miTableAuto.setVisible(true);
            menu.fieldMenu.insert(menu.ci5, 6);
            menu.fieldMenu.insert(menu.ci2, 4);
            menu.fieldMenu.insert(menu.ci1, 4);
        }
    }

    private void initialize(java.io.ObjectInput in) throws IOException, ClassNotFoundException{
        ESlateHandle prevStaticParent = ESlateHandle.nextParent;
        ESlateHandle.nextParent = getESlateHandle();

        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(loadTimer);

        Object firstObj = in.readObject();
        if (!StorageStructure.class.isAssignableFrom(firstObj.getClass())) {
            // Old time readExtermal()
            oldTimeReadExternal(in, firstObj);
            return;
        }

        StorageStructure fieldMap = (StorageStructure) firstObj;
        int dataVersion = 180;
        try{
            //dataVersion = new Integer(fieldMap.getDataVersion()).intValue();
            dataVersion = fieldMap.getDataVersionID();
        }catch (Exception exc) {}


        DBase newDB = null;
        if (dataVersion >= 200) {
            eSlateHandle.restoreChildren(/*in,*/ fieldMap, "CDatabase");
            ESlateHandle[] childrenHandles = eSlateHandle.toArray();
            for (int i=0; i<childrenHandles.length; i++) {
                if (DBase.class.isAssignableFrom(childrenHandles[i].getComponent().getClass())) {
                    newDB = (DBase) childrenHandles[i].getComponent();
                    break;
                }
            }
        }else{
            Object obj = fieldMap.get("CDatabase");
            if (obj != null) {
                newDB = new DBase(obj);
            }
        }

        if (dataVersion >= 201) {
            // restoring the toolbars
            // Restoring formatting toolbar
            if (formattingToolbarController.getToolbar() != null){
                formattingToolBarPanel.removeAll();
                toolMenuPanel.remove(formattingToolBarPanel);
                getESlateHandle().remove(formattingToolbarController.getToolbar().getESlateHandle());
                formattingToolbarController.destroyToolbar();
            }

            Object[] objects = getESlateHandle().restoreChildObjects((StorageStructure)fieldMap, "formattingToolbarChild");
            if (objects != null && objects.length > 0){
                // restoring the reference of the toolbar
                formattingToolbarController.setToolbar((ESlateToolBar)((ESlateHandle)objects[0]).getComponent());
                ToolLocation[] loc=(ToolLocation[]) fieldMap.get("formattingToolbarLocations");
                if (loc==null)
                	formattingToolbarController.restoreOld();
                else
                	formattingToolbarController.identifyTools(loc);
                formattingToolbarController.attachFormattingToolBarActions();
                setFormattingToolBarVisible(fieldMap.get("FormattingToolBarVisible", true));

                formattingToolBarPanel.add(formattingToolbarController.getToolbar());
                formattingToolBarPanel.add(Box.createGlue());
                toolMenuPanel.add(formattingToolBarPanel, toolMenuPanel.getComponentCount());
            }
            // restoring Standard toolbar
            if (standardToolbarController.getToolbar() != null){
                standardToolBarPanel.removeAll();
                toolMenuPanel.remove(standardToolBarPanel);
                getESlateHandle().remove(standardToolbarController.getToolbar().getESlateHandle());
                standardToolbarController.destroyToolbar();
            }

            objects = getESlateHandle().restoreChildObjects((StorageStructure)fieldMap, "standardToolbarChild");
            if (objects != null && objects.length > 0){
                // restoring the reference of the toolbar
                standardToolbarController.setToolbar((ESlateToolBar)((ESlateHandle)objects[0]).getComponent());
                ToolLocation[] loc=(ToolLocation[]) fieldMap.get("standardToolbarLocations");
                if (loc==null)
                	standardToolbarController.restoreOld();
                else
                	standardToolbarController.identifyTools(loc);
                standardToolbarController.attachStandardToolBarActions();
                setStandardToolbarVisible(fieldMap.get("StandardToolBarVisible", true));
                int pos = 0;
                if (isMenuBarVisible())
                    pos++;
                standardToolBarPanel.add(standardToolbarController.getToolbar());
                standardToolBarPanel.add(Box.createGlue());
                toolMenuPanel.add(standardToolBarPanel, pos);
            }
            // restoring Status toolbar
            if (statusToolbarController.getToolbar() != null){
                tabPanel.remove(statusToolbarController.getToolbar());
                getESlateHandle().remove(statusToolbarController.getToolbar().getESlateHandle());
                statusToolbarController.destroyToolbar();
            }

            objects = getESlateHandle().restoreChildObjects((StorageStructure)fieldMap, "statusToolbarChild");
            if (objects != null && objects.length > 0){
                // restoring the reference of the toolbar
                statusToolbarController.setToolbar((ESlateToolBar)((ESlateHandle)objects[0]).getComponent());
                ToolLocation[] loc=(ToolLocation[]) fieldMap.get("statusToolbarLocations");
                if (loc==null)
                	statusToolbarController.restoreOld();
                else
                	statusToolbarController.identifyTools(loc);
                statusToolbarController.attachStatusBarActions();
                setStatusBarVisible(fieldMap.get("StatusBarVisible", true));
                tabPanel.add(statusToolbarController.getToolbar(), BorderLayout.SOUTH);
            }
        }else{
            System.out.println("Reading version without ESlateToolBar");
            setStandardToolbarVisible(fieldMap.get("StandardToolBarVisible", true));
            setFormattingToolBarVisible(fieldMap.get("FormattingToolBarVisible", true));
            setStatusBarVisible(fieldMap.get("StatusBarVisible", true));
        }


        String newDBTitle = null;
        if (newDB != null)
            newDBTitle = newDB.getTitle();
        //boolean b1 = fieldMap.get("StandardToolBarVisible", true);
        //setStandardToolbarVisible(b1);

        setUserMode(fieldMap.get("UserMode", NOVICE_USER_MODE));
        setMenuBarVisible(fieldMap.get("MenuBarVisible", true));
        BorderDescriptor bd = (BorderDescriptor) fieldMap.get("BorderDescriptor");
        try{
            setBorder(bd.getBorder());
        }catch (Throwable thr) {}

        //setStatusBarVisible(fieldMap.get("StatusBarVisible", true));
        setTabTransparent(fieldMap.get("Tabbed Pane Opaque", false));
        setTableHeaderExpansionChangeAllowed(fieldMap.get("TableHeaderExpansionChangeAllowed", true));

        dbView = (DBView) fieldMap.get("DBView");
        if (dbView == null)
            dbView = new DBView();
//1        databaseTableViewStructures = (DatabaseTableViewStructures) fieldMap.get("Table View Structures");

        /* Specify the plug aliases which transfer connections from the plugs that used
         * to exist before data format version 200 to the new plugs introduced in
         * version 200 (nesting was introduced in this version).
         */
        ESlateMicroworld mwd = eSlateHandle.getESlateMicroworld();
        if (mwd != null && dataVersion < 200) {
            ESlateHandle dbHandle = null;
            if (newDB != null)
                dbHandle = newDB.getESlateHandle();
            try{
                if (dbHandle != null)
                    mwd.setPlugAliasForLoading(dbHandle.getPlugs()[0], eSlateHandle, new String[]{"DatabaseMsg36"}, mwd.ALIAS_OUTPUT);
                mwd.setPlugAliasForLoading(databaseImportPlug, eSlateHandle, new String[]{"DatabaseMsg36"}, mwd.ALIAS_INPUT);
            }catch (BadPlugAliasException exc) {
                System.out.println("Unable to specify plug alias for the old CDatabase import/export plug");
            }
            if (dbHandle != null) {
                ESlateHandle[] tableHandles = dbHandle.toArray();
                for (int i=0; i<newDB.getTableCount(); i++) {
                    try{
                        //Find the proper Table handle
                        ESlateHandle tableHandle = null;
                        String title = newDB.getTableAt(i).getTitle();
                        for (int k=0; k<tableHandles.length; k++) {
                            if (tableHandles[k].getComponentName().equals(title)) {
                                tableHandle = tableHandles[k];
                                break;
                            }
                        }
                        if (tableHandle != null)
                            mwd.setPlugAliasForLoading(tableHandle.getPlugs()[0], eSlateHandle, new String[]{title, "DatabaseMsg36"});
                    }catch (Exception exc) {
                        System.out.println("createTablePins(): Unable to redirect old jTable plugs to the plugs of the internal Table components");
                    }
                }
            }
        }

        // Restore the DBTable of the Database (introduced in version 202)
        if (dataVersion >= 202) {
            // Get the titles of the Tables of the DBTables, which were saved as children of the Database. When the
            // restored DBEngine is reconnected to the Database, we will use this info to match the restored DBTables
            // to the Tables of the DBEngine they showed, when the Database was saved.
            StringBaseArray tableTitles = (StringBaseArray) fieldMap.get("Stored DBTables Table titles");
            dbTables.clear();
            Object[] dbTableChildren = getESlateHandle().restoreChildObjects((StorageStructure)fieldMap, "DBTables");
            // If 'tableTitles' is null, the fill it with random titles. This will happen for early microworld of
            // storage format 202.
            if (tableTitles == null) {
                tableTitles = new StringBaseArray();
                for (int i=0; i<dbTableChildren.length; i++)
                    tableTitles.add("Table" + i);
            }
            restoredDBTables.clear();
            if (dbTableChildren != null) {
                for (int i=0; i<dbTableChildren.length; i++) {
//                    System.out.println("1. Adding DBTable dbTableChildren[i]: " + ((ESlateHandle) dbTableChildren[i]).getComponent());
                    // The restored DBTables are put in the restoredDBTables structure, keyed with the titles of the Tables
                    // they were showing when they were stored. This structure is used in 'getDB()' to reconnect the
                    // each DBTable to the proper Table.
                    restoredDBTables.put(tableTitles.get(i), ((ESlateHandle) dbTableChildren[i]).getComponent());
///                    dbTables.add(((ESlateHandle) dbTableChildren[i]).getComponent());
                }
            }
        }else{
            // For older versions we have to create the DBTables here empty. When the DBase handle is connected to
            // the Database, the getDB() method is called, which expects to find the DBTables ready with their
            // restored state. Obviously the state that is now saved by DBTable and was up to now (FORMAT_VERSION 202)
            // stored in gr.cti.eslate.database.engine.TableView(s) and gr.cti.eslate.database.engine.FieldView(s) is
            // lost. For pre 202 Databases the DBTables have their default state.
            if (newDB != null) {
///                for (int i=0; i<newDB.getTableCount(); i++) {
///                    dbTables.add(new DBTable());
///                }
            }
        }


        if (newDB != null) {
            eSlateHandle.add(newDB.getESlateHandle());
            if (dataVersion < 200) {
                System.out.println("Pre 2.0 Database storage format. Manually connecting plugs");
                try{
                    newDB.getESlateHandle().getPlugs()[0].connectPlug(databaseImportPlug);
                }catch (PlugNotConnectedException exc) {
                    exc.printStackTrace();
                }
            }
        }else
            dbTables = new ArrayList();

        componentModified = false;

        try{
            // this is the right position of the next code
            if (dataVersion >= 179) {
                setTableHeaderFont((Font)fieldMap.get("TableHeaderFont"));
                setFieldsHeaderFont((Font)fieldMap.get("FieldsHeaderFont"));
            }
        }catch(NumberFormatException e) {
            if (dataVersion == 200) {
                setTableHeaderFont((Font)fieldMap.get("TableHeaderFont"));
                setFieldsHeaderFont((Font)fieldMap.get("FieldsHeaderFont"));
            }else
                System.out.println("Database version before 1.7.8");
        }

        pm.stop(loadTimer);
        // The load time includes the time needed to load the DBase, if one was loaded.
        // Therefore this time has to be subtracted
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");

        ESlateHandle.nextParent = prevStaticParent;
    }

    public void setStandardToolbarVisible(boolean visible) {
        if (visible){
            if (standardToolbarController.getToolbar() == null){
                standardToolbarController.setDefaultState();
                getESlateHandle().add(standardToolbarController.getToolbar().getESlateHandle());
                setActionStatus();
                setDBMenuRestStatus();
                setTableMenuRestStatus();
                setFieldMenuRestStatus();
                int pos = 0;
                if (isMenuBarVisible())
                    pos++;
                //toolMenuPanel.add(standardToolbarController.getToolbar(), pos);
                standardToolBarPanel.removeAll();
                standardToolBarPanel.add(standardToolbarController.getToolbar());
                standardToolBarPanel.add(Box.createGlue());
                toolMenuPanel.add(standardToolBarPanel, pos);
            }
        }
        else{
            if (standardToolbarController.getToolbar() == null)
              return;
        }
        standardToolbarController.getToolbar().setVisible(visible);
        adjustToolMenuPanelBorder();

        mainPanel.revalidate();
        mainPanel.validate();
        mainPanel.doLayout();
        mainPanel.repaint(mainPanel.getVisibleRect());
        componentModified = true;
    }

    public boolean isStandardToolbarVisible() {
        if (standardToolbarController.getToolbar() == null)
            return false;
        return standardToolbarController.getToolbar().isVisible();
    }

    public void setStatusBarVisible(boolean visible) {
        if (visible){
            if (statusToolbarController.getToolbar() == null){
                statusToolbarController.setDefaultState();
                getESlateHandle().add(statusToolbarController.getToolbar().getESlateHandle());
                tabPanel.add(statusToolbarController.getToolbar(), BorderLayout.SOUTH);
            }
        }
        else{
            if (statusToolbarController.getToolbar() == null)
              return;
        }
        statusToolbarController.getToolbar().setVisible(visible);
        mainPanel.validate();
        mainPanel.doLayout();
        mainPanel.repaint(mainPanel.getVisibleRect());
        componentModified = true;
    }

    public boolean isStatusBarVisible() {
        if (statusToolbarController.getToolbar() == null)
            return false;
        return statusToolbarController.getToolbar().isVisible();
    }


    public void setTabTransparent(boolean transparent){
        if (tabTransparent == transparent) return;
        tabTransparent = transparent;
        mainPanel.setOpaque(!transparent);
        tabPanel.setOpaque(!transparent);
        setOpaque(!transparent);
        mainPanel.validate();
        mainPanel.doLayout();
        mainPanel.repaint(mainPanel.getVisibleRect());
        componentModified = true;
    }

    public boolean isTabTransparent(){
        return tabTransparent;
    }

    public void setFormattingToolBarVisible(boolean visible) {
        if (visible){
            if (formattingToolbarController.getToolbar() == null){
                formattingToolbarController.setDefaultState();
                getESlateHandle().add(formattingToolbarController.getToolbar().getESlateHandle());
                formattingToolbarController.setToolBarStatus();
                //toolMenuPanel.add(formattingToolbarController.getToolbar(), toolMenuPanel.getComponentCount());
                formattingToolBarPanel.removeAll();
                formattingToolBarPanel.add(formattingToolbarController.getToolbar());
                formattingToolBarPanel.add(Box.createGlue());
                toolMenuPanel.add(formattingToolBarPanel, toolMenuPanel.getComponentCount());
            }
        }
        else{
            if (formattingToolbarController.getToolbar() == null)
              return;
        }
        formattingToolbarController.getToolbar().setVisible(visible);
        adjustToolMenuPanelBorder();

        mainPanel.revalidate();
        mainPanel.validate();
        mainPanel.doLayout();
        mainPanel.repaint(mainPanel.getVisibleRect());
        componentModified = true;
    }


    public boolean isFormattingToolBarVisible() {
        if (formattingToolbarController.getToolbar() == null)
            return false;
        return formattingToolbarController.getToolbar().isVisible();
    }

    public void setMenuBarVisible(boolean visible) {
        if (menu.menuBarVisible == visible) return;
        menu.menuBarVisible = visible;
        if (menu.menuBarVisible){
            toolMenuPanel.add(menu, 0);
            adjustToolMenuPanelBorder();
        }
        else{
            toolMenuPanel.remove(menu);
            adjustToolMenuPanelBorder();
        }
        mainPanel.validate();
        mainPanel.doLayout();
        mainPanel.repaint(mainPanel.getVisibleRect());
        componentModified = true;
    }

    public boolean isMenuBarVisible() {
        return menu.menuBarVisible;
    }

    private void adjustToolMenuPanelBorder() {
        if (toolMenuPanel.getComponentCount() == 0) {
            if (toolMenuPanel.getBorder() != null)
                toolMenuPanel.setBorder(null);
        }else{
            if (toolMenuPanel.getBorder() == null)
                toolMenuPanel.setBorder(toolMenuPanelBorder);
        }
    }

    private void initDatabaseTableModelListener() {
        tableListener = new DatabaseTableModelAdapter() {
            public void tableHiddenStateChanged(TableHiddenStateChangedEvent e) {
                if (db.isHiddenTablesDisplayed()) {
                    setTableMenuRestStatus();
                    return;
                }
                statusToolbarController.setMessageLabelInWaitState();
                dbComponent.setCursor(waitCursor);

                Table ctable = (Table) e.getSource();
                int visibleIndex = getDBTableIndex(ctable);
                boolean hide = e.isHidden();
                if (!hide) {
                    DBTable dbt;
                    dbt = new DBTable(ctable, dbComponent/*1, null*/);
                    if (dbTables.size() == 0)
                        dbComponent.mainPanel.add(tabPanel, BorderLayout.CENTER);
                    insertDBTableToDatabase(dbt, db.indexOf(ctable));
                    if (eSlateHandle != null)
	                dbt.addWidthChangeListener();
	                activateDBTable(dbt);
                }else{
                    if (visibleIndex < 0) {
                        System.out.println("Serious inconsistency error in Database tableHiddenStateChanged(): (1)");
                        statusToolbarController.setMessageLabelColor(Color.white);
                        updateNumOfSelectedRecords(activeDBTable);
                        dbComponent.setCursor(defaultCursor);
                        return;
                    }
                    dbTables.remove(visibleIndex);
                    tabs.removeTabAt(visibleIndex);
                    if (dbTables.size() == 0) {
                        dbComponent.mainPanel.remove(tabPanel);
                        activeDBTable = null;
                    }else{
                        if (visibleIndex == 0)
                            activateDBTableAt(0);
                        else
                            activateDBTableAt(visibleIndex-1);
                    }
                }

                refreshDatabase();
                statusToolbarController.setMessageLabelColor(Color.white);
                if (activeDBTable != null)
                    updateNumOfSelectedRecords(activeDBTable);
                setActionStatus();
                setDBMenuRestStatus();
                setTableMenuRestStatus();
                setFieldMenuRestStatus();
                if (formattingToolbarController.getToolbar() != null)
                    formattingToolbarController.setToolBarStatus();
                dbComponent.setCursor(defaultCursor);
            }

/*            public void columnRenamed(ColumnRenamedEvent e) {
                Table ctable = (Table) e.getSource();
                String newFieldName = e.getNewName();
                String oldFieldName = e.getOldName();

                DBTable dbTable = getDBTableOfTable(ctable);
                if (dbTable != null)
                    dbTable.UIRenameField(newFieldName, oldFieldName);
            }
*/
            public void columnAdded(ColumnAddedEvent e) {
/*
                Table ctable = (Table) e.getSource();
                AbstractTableField field = null;
                try{
                    field = ctable.getTableField(e.getColumnIndex());
                }catch (Exception exc) {
                    return;
                }
                if (field == null)
                    return;

                DBTable dbTable = getDBTableOfTable(ctable);
                if (dbTable != null)
                    dbTable.UIAddField(field);
*/
                Table ctable = (Table) e.getSource();
                DBTable dbTable = getDBTableOfTable(ctable);
                if (dbTable != null && dbTable == activeDBTable)
                    updateAddRecordStatus();
            }

            public void columnRemoved(ColumnRemovedEvent e) {
                Table ctable = (Table) e.getSource();
                DBTable dbTable = getDBTableOfTable(ctable);
                if (dbTable != null && dbTable == activeDBTable)
                    updateAddRecordStatus();
//                    dbTable.UIRemoveColumn(fieldName);
            }
/*
            public void columnTypeChanged(ColumnTypeChangedEvent e) {
                Table ctable = (Table) e.getSource();
                String fieldName = e.getColumnName();
                AbstractTableField field;
                try{
                    field = ctable.getTableField(fieldName);
                }catch (InvalidFieldNameException exc) {return;}

                Class prevFieldtype = null;
                try{
                    prevFieldtype = Class.forName(e.getPrevType());
                }catch (Exception exc) {
                    return;
                }
                String previousDataType = AbstractTableField.getInternalDataTypeName(prevFieldtype);
                String newFieldType = AbstractTableField.getInternalDataTypeName(field);

                DBTable dbTable = getDBTableOfTable(ctable);
                if (dbTable != null)
                    dbTable.UIChangeFieldType(field, previousDataType);
            }
1*/
            public void columnKeyChanged(ColumnKeyChangedEvent e) {
                Table ctable = (Table) e.getSource();

                DBTable dbTable = getDBTableOfTable(ctable);
                if (dbTable == activeDBTable) {
                    statusToolbarController.updateStatusBar(dbTable);
                    updateNumOfRecordsLabel();
                }
            }
/*1
            public void calcColumnReset(CalcColumnResetEvent e) {
                Table ctable = (Table) e.getSource();
                String fieldName = e.getColumnName();

                DBTable dbTable = getDBTableOfTable(ctable);
                if (dbTable != null)
                    dbTable.UISwitchCalcFieldToNormal(fieldName);
            }

1*/
            public void columnEditableStateChanged(ColumnEditableStateChangedEvent e) {
                Table table = (Table) e.getSource();
                String fieldName = e.getColumnName();

                DBTable dbTable = getDBTableOfTable(table);
                if (dbTable == activeDBTable) {
                    try{
                        AbstractTableField f = table.getTableField(fieldName);
                        if (iconEditorClass != null && f.getDataType().equals(CImageIcon.class))
                            standardToolbarController.setImageEditEnabled(f.isEditable());
                    }catch (Throwable thr) {}
                }
            }

/*1            public void columnRemovableStateChanged(ColumnRemovableStateChangedEvent e) {
                Table ctable = (Table) e.getSource();
                String fieldName = e.getColumnName();
            }
1*/
            public void columnHiddenStateChanged(ColumnHiddenStateChangedEvent e) {
                Table table = (Table) e.getSource();
                DBTable dbTable = getDBTableOfTable(table);
                if (dbTable != activeDBTable) {
                    dbTable.jTable.revalidate();
                    dbTable.refresh();
                }
            }

/*1            public void calcColumnFormulaChanged(CalcColumnFormulaChangedEvent e) {
                Table ctable = (Table) e.getSource();
                String fieldName = e.getColumnName();
                String prevDataType = e.getPreviousDataType();

                DBTable dbTable = getDBTableOfTable(ctable);
                if (dbTable != null)
                    dbTable.UIChangeCalcFieldFormula(fieldName, prevDataType);
            }

            public void cellValueChanged(CellValueChangedEvent e) {
                //System.out.println("CellValue changed " + e.getNewValue());
                Table table = (Table) e.getSource();
                String fieldName = e.getColumnName();
                int recordIndex = e.getRecordIndex();

                DBTable dbTable = getDBTableOfTable(table);
                if (dbTable != null) {
                    if (e.affectsOtherCells())
                        dbTable.refresh();
                    else{
                        dbTable.refreshField(dbTable.jTable.getColumn(fieldName));
                    }
                }
            }
*/
            public void recordAdded(RecordAddedEvent e) {
                if (e.moreToBeAdded()) return;
                Table ctable = (Table) e.getSource();
//                int recordIndex = e.getRecordIndex();

//                DBTable dbTable = getDBTableOfTable(ctable);
//                if (dbTable != null && dbTable == activeDBTable)
//                        updateAddRecordStatus();
//                if (dbTable != null)
//                    dbTable.acceptNewRecord();
                updateSelectAll_InvertRecSelectionStatus();
                updateNumOfRecordsLabel();
            }

            public void recordRemoved(RecordRemovedEvent e) {
                if (e.isChanging()) return;
                DBTable dbTable = getDBTableOfTable((Table) e.getSource());
                if (dbTable == activeDBTable) {
                    updateNumOfRecordsLabel();
                    updateNumOfSelectedRecords(dbTable);
                }
                updateAddRecordStatus();
                updateSelectAll_InvertRecSelectionStatus();
            }

            public void tableRenamed(TableRenamedEvent e) {
                Table ctable = (Table) e.getSource();
                String newTitle = e.getNewTitle();
                String oldTitle = e.getOldTitle();
                int index = getDBTableIndex(ctable);
                if (index != -1) {
                    tabs.setTitleAt(index, newTitle);
                    tabs.validate();
                    tabs.repaint();
                }
            }

            public void rowOrderChanged(RowOrderChangedEvent e) {
                Table ctable = (Table) e.getSource();
                int visibleIndex = getDBTableIndex(ctable);
                if (visibleIndex == -1) {
                    System.out.println("Serious inconsistency error in Database rowOrderChanged(): (1)");
                    return;
                }
                DBTable targetDBTable = (DBTable) dbTables.get(visibleIndex);
                //DBTable dbTable = getDBTableOfTable(dbTable);
                UIUpdateActiveRecord(targetDBTable);
/*                TableColumn col;
                HeaderRenderer hr;
                for (int i=0; i<targetDBTable.tableColumns.size(); i++) {
                    col = targetDBTable.jTable.getColumn(targetDBTable.jTable.getColumnName(i));
                    hr = ((HeaderRenderer) col.getHeaderRenderer());
                    hr.updateFieldIcons(targetDBTable.jTable.getColumnName(i));
                }
                targetDBTable.refresh();
*/
            }

            public void activeRecordChanged(ActiveRecordChangedEvent e) {
                Table ctable = (Table) e.getSource();
//                int previousActiveRecord = e.getPreviousActiveRecord();
//                int activeRecord = e.getActiveRecord();

                int visibleIndex = getDBTableIndex(ctable);
                if (visibleIndex == -1) {
                    System.out.println("Serious inconsistency error in Database activeRecordChanged(): (1)");
                    return;
                }

                DBTable targetDBTable = (DBTable) dbTables.get(visibleIndex);
/*                //Move to JBuilder version
                if (pendingNewRecord && targetDBTable.pendingRecordIndex != activeRecord && !targetDBTable.recordInsertionCancelled) {
                    if (!targetDBTable.checkAndStorePendingRecord2()) {
                        targetDBTable.table.setActiveRecord(previousActiveRecord);
                        throw new RuntimeException("New record is pending. Cannot change the active record");
                    }
                }

                if (targetDBTable.isEditing()) {
                    try{
                        ((DefaultCellEditor) ((TableColumn) targetDBTable.tableColumns.get(targetDBTable.jTable.getEditingColumn())).getCellEditor()).stopCellEditing();
                    }catch (Exception exc) {
                        targetDBTable.resetEditing();
                    }
                }
*/

                UIUpdateActiveRecord(targetDBTable);
                scrollToActiveRecord = true;
            }

            public void selectedRecordSetChanged(SelectedRecordSetChangedEvent e) {
                Table ctable = (Table) e.getSource();
                int visibleIndex = getDBTableIndex(ctable);
                if (visibleIndex == -1) {
                    return;
                }

                IntBaseArray selectedSubset = ctable.getSelectedSubset();
//                ((DBTable) dbTables.get(visibleIndex)).alterRecordSelection(selectedSubset);
                if (activeDBTable.equals((DBTable) dbTables.get(visibleIndex))) {
                    updateNumOfSelectedRecords(selectedSubset.size());
                    updateRemoveRec_ClearRecSelection_PromoteStatus();
                }
            }

            public void columnReplaced(ColumnReplacedEvent e) {}
        };
    }


    private void initDatabaseAdapter() {
        databaseAdapter = new DatabaseAdapter() {
            public void  activeTableChanged(ActiveTableChangedEvent e) {
                int index = e.getToIndex();
                int visibleIndex = db.toVisibleTableIndex(index);
                if (visibleIndex == -1) {
                    db.activateFirstNonHiddenTable();
                    return;
                }
                if (visibleIndex == -2)
                    return;

                if (tabs != null && (visibleIndex != tabs.getSelectedIndex() || tableWasRemoved)) {
                    if (tableWasRemoved)
                        tableWasRemoved = false;
                    iterateEvent = false;
                    activateDBTableAt(visibleIndex);
                    iterateEvent = true;
                }
            }

            public void tableAdded(TableAddedEvent e) {
                statusToolbarController.setMessageLabelInWaitState();
                dbComponent.setCursor(waitCursor);
                Table table = e.getTable();
                if (!db.isHiddenTablesDisplayed() && table.isHidden()) return;
                int tableIndex = ((DBase) e.getSource()).indexOf(table);
//                TableViewStructure tvs = null;
                /* When a microworld is loading, the DBTable UI settings supercede in
                 * priority the UI settings of the individual DBase Tables. So when
                 * connections to external tables are restored, instead of creating the
                 * DBTable with the UI settings of the Table's TableView, we apply the
                 * UI settings the DBTable had when the Database component was saved
                 * as part of the microworld save.
                 */
//1                if (eSlateHandle.getESlateMicroworld().isLoading() && databaseTableViewStructures != null)
//1                    tvs = databaseTableViewStructures.getTableViewStructure(table.getTitle());
                DBTable dbt = new DBTable(table, dbComponent/*1, tvs*/);
                if (tabs == null) {
                    createTabs();
                }

                dbTables.add(dbt);
                addDBTableToDatabase(dbt);
       	        dbt.addWidthChangeListener();
                statusToolbarController.setMessageLabelColor(Color.white);
                updateNumOfSelectedRecords(activeDBTable);
                dbComponent.setCursor(defaultCursor);
            }

            public void tableRemoved(TableRemovedEvent e) {
                statusToolbarController.setMessageLabelInWaitState();
                dbComponent.setCursor(waitCursor);
                Table removedTable = e.getRemovedTable();
                DBTable dbt = getDBTableOfTable(removedTable);
                detachTableListeners(removedTable);
                if (dbt != null) {
                    int dbtIndex = dbTables.indexOf(dbt);
                    int tabCount = tabs.getTabCount();
                    if (tabCount == 1) {
                        mainPanel.remove(tabPanel);
                        dbTables.remove(dbtIndex);
                        activeDBTable = null;
                        //For some strange reason the Table count becomes zero
                        //afterwards
                        if (db.getTableCount() == 1) {
                            tabPanel.remove(tabs);
                            tabs = null;
                            mainPanel.add(emptyDB, BorderLayout.CENTER);
                        }else
                            tabs.removeTabAt(dbtIndex);
                        dbComponent.validate();
                        refreshDatabase();
                        statusToolbarController.setMessageLabelColor(Color.white);
                        setActionStatus();
                        setDBMenuRestStatus();
                        setTableMenuRestStatus();
                        setFieldMenuRestStatus();
                        if (formattingToolbarController.getToolbar() != null)
                            formattingToolbarController.setToolBarStatus();
                        dbComponent.setCursor(defaultCursor);
                        return;
                    }

                    tableWasRemoved = true;
                    dbTables.remove(dbtIndex);
                    tabs.removeTabAt(dbtIndex);
                    refreshDatabase();
                }

       	        dbt = null;
                statusToolbarController.setMessageLabelColor(Color.white);
                updateNumOfSelectedRecords(activeDBTable);
                setActionStatus();
                setDBMenuRestStatus();
                setTableMenuRestStatus();
                setFieldMenuRestStatus();
                setFieldMenuRestStatus();
                if (formattingToolbarController.getToolbar() != null)
                    formattingToolbarController.updateToolBarStatus();
                dbComponent.setCursor(defaultCursor);
            }

/*            public void tableReplaced(TableReplacedEvent e) {
                Table newTable = e.getNewTable();
                Table replacedTable = e.getReplacedTable();
                statusToolbarController.setMessageLabelInWaitState();
                dbComponent.setCursor(waitCursor);
                DBTable dbTable = null;
                for (int i=0; i<dbTables.size(); i++) {
                    if (((DBTable) dbTables.get(i)).dbTable.equals(replacedTable)) {
                        dbTable = (DBTable) dbTables.get(i);
                        break;
                    }
                }
                if (dbTable == null) {
                    statusToolbarController.setMessageLabelColor(Color.white);
                    updateNumOfSelectedRecords(activeDBTable);
                    dbComponent.setCursor(defaultCursor);
                    return;
                }

                dbTable.removeAllRecords();
                dbTable.changeTableModel(newTable);
                dbTable.jTable.revalidate();
                dbTable.jTable.repaint();
                refreshDatabase();

                int ctableIndex = db.toVisibleTableIndex(db.indexOf(newTable));
                if (ctableIndex == -1)
                    return;

                statusToolbarController.setMessageLabelColor(Color.white);
                updateNumOfSelectedRecords(activeDBTable);
                dbComponent.setCursor(defaultCursor);

            }
*/
        };
    }

    private void initPropertyChangeListener() {
        pcl = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String propertyName = ev.getPropertyName();
                if (propertyName.equals("Hidden tables displayed")) {
                    displayHiddenTables(((Boolean) ev.getNewValue()).booleanValue());
                }
            }
        };
    }

    private void initTablePropertyChangeListener() {
        tablePcl = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String propertyName = ev.getPropertyName();
                if (propertyName.equals("Hidden fields displayed")) {
                    Table ctable = (Table) ev.getSource();
                    DBTable dbTable = getDBTableOfTable(ctable);
                    if (dbTable != null)
                        dbTable.UIDisplayHiddenFields(((Boolean) ev.getNewValue()).booleanValue());
                }else if (propertyName.equals("Record addition allowed"))
                    updateAddRecordStatus();
                 else if (propertyName.equals("Record removal allowed")) {
                    if (activeDBTable != null)
                        updateRemoveRecordStatus(activeDBTable.activeRow);
                    updateRemoveRec_ClearRecSelection_PromoteStatus();
                }else if (propertyName.equals("Field addition allowed")){
                    setActionStatus();
                    setDBMenuRestStatus();
                    setTableMenuRestStatus();
                    setFieldMenuRestStatus();
                    updateFieldMenuRestStatus();
                    updateActionStatus();
                }
                 else if (propertyName.equals("Field removal allowed")){
                    setActionStatus();
                    setDBMenuRestStatus();
                    setTableMenuRestStatus();
                    setFieldMenuRestStatus();
                    updateFieldMenuRestStatus();
                    updateActionStatus();
                 }
                 else if (propertyName.equals("Field reordering allowed")) {
                    Table ctable = (Table) ev.getSource();
                    DBTable dbTable = getDBTableOfTable(ctable);
                    if (dbTable != null)
                        dbTable.jTable.getTableHeader().setReorderingAllowed(((Boolean) ev.getNewValue()).booleanValue());
                 }else if (propertyName.equals("Table renaming allowed")) {
                    Table ctable = (Table) ev.getSource();
                    DBTable dbTable = getDBTableOfTable(ctable);
                    if (dbTable == activeDBTable)
                        tableRenameAction.setEnabled(ctable.isTableRenamingAllowed());
                 }else if (propertyName.equals("Data change allowed")) {
                    Table ctable = (Table) ev.getSource();
                    DBTable dbTable = getDBTableOfTable(ctable);
                    if (dbTable == null)
                        return;
                    boolean dataChangeAllowed = ((Boolean) ev.getNewValue()).booleanValue();
                    String fieldName;
                    AbstractTableField fld;
                    if (!dataChangeAllowed) {
                        for (int i=0; i<ctable.getFieldCount(); i++) {
                            try{
                                fld = ctable.getTableField(i);
                            }catch (InvalidFieldIndexException exc) {
                                continue;
                            }
                            fieldName = fld.getName();
                            TableColumn col = (TableColumn) dbTable.jTable.getColumn(fieldName);

                            if (fld.isEditable())
                                col.setCellEditor(null);
                        }
                    }else{
                        for (int i=0; i<ctable.getFieldCount(); i++) {
                            try{
                                fld = ctable.getTableField(i);
                            }catch (InvalidFieldIndexException exc) {
                                continue;
                            }
                            fieldName = fld.getName();
                            TableColumn col = (TableColumn) dbTable.jTable.getColumn(fieldName);

                            if (fld.isEditable()) {
                                AbstractDBTableColumn column = null;
                                for (int k=0; k<dbTable.tableColumns.size(); k++) {
                                    AbstractDBTableColumn clm = dbTable.tableColumns.get(k);
                                    if (column.tableField == fld) {
                                        column = clm;
                                        break;
                                    }
                                }
/*cf                                DBCellRenderer renderer = (DBCellRenderer) col.getCellRenderer();
                                ColumnRendererEditor existing;
                                int colRendererEditorsIndex = -1;
                                for (int k=0; k<dbTable.tableColumns.size(); k++) {
                                    existing = dbTable.tableColumns.get(k).colRendererEditor;
                                    if (renderer.equals(existing.renderer)) {
                                        colRendererEditorsIndex = k;
                                        break;
                                    }
                                }
*/
                                if (column == null) {
                                    System.out.println("Serious inconsistency error in Database PropertyChangeListener(): (1)");
                                    continue;
                                }

                                column.tableColumn.setCellEditor(column.editor);
//cf                                dbTable.tableColumns.get(colRendererEditorsIndex).tableColumn.setCellEditor(.editor;
                            }
                        }
                    }
                    if (dbTable == activeDBTable)
                        dbTable.highlightNonEditableFields(dbTable.isNonEditableFieldsHighlighted());
                 }
            }
        };
    }

    public void refreshDatabase() {
        mainPanel.validate();
        mainPanel.repaint();
    }


    public DBTable getActiveDBTable() {
        return activeDBTable;
    }


    public DBTable getDBTableAt(int i) {
        if (i < dbTables.size())
            return (DBTable) dbTables.get(i);
        else
            return null;
    }


    public void addDBTableToDatabase(DBTable dbTable) {
        if (dbTable == null || dbTable.table == null) //t || dbTable.dbTable.getDBase() == null)
            throw new NullPointerException();
        attachTableListeners(dbTable.table);

        dbTable.setBorder(null);
        if (dbTable.table.getTitle() == null || dbTable.table.getTitle().trim().length() == 0)
            tabs.addTab(infoBundle.getString("DatabaseMsg37"), dbTable);
        else
            tabs.addTab(dbTable.table.getTitle(), dbTable);

/* When no record is selected, by default JTable selects the first row. So update
         * the list of selected records of the jTable.
         */
        refreshDatabase();

/* Initialize the activeDBTable, if this jTable is the first one in the Database
         */
        if (dbTables.size() == 1) {
            if (db.getTableCount() > 1) //Contains only the menu & toolbar panel
                mainPanel.add(tabPanel, BorderLayout.CENTER);

            iterateEvent = false;
            activateDBTable(dbTable);
            iterateEvent = true;
            if (formattingToolbarController.getToolbar() != null)
                formattingToolbarController.setToolBarStatus();
        }
    }

    private void attachTableListeners(Table table) {
        table.addTableModelListener(tableListener);
        table.addPropertyChangeListener(tablePcl);
    }

    private void detachTableListeners(Table table) {
        table.removeTableModelListener(tableListener);
        table.removePropertyChangeListener(tablePcl);
    }


    public void insertDBTableToDatabase(DBTable dbTable, int pos) {
        if (dbTable == null || dbTable.table == null)
            throw new NullPointerException();

        dbTables.add(pos, dbTable);

        dbTable.setBorder(null);
        if (tabs == null)
            createTabs();
        if (dbTable.table.getTitle() == null || dbTable.table.getTitle().trim().length() == 0)
            tabs.insertTab(infoBundle.getString("DatabaseMsg37"), null, dbTable, null, pos);
        else
            tabs.insertTab(dbTable.table.getTitle(), null, dbTable, null, pos);

/* Initialize the activeDBTable, if this jTable is the first one in the Database
         */
        if (dbTables.size() == 1) {
            iterateEvent = false;
            activateDBTable(dbTable);
            iterateEvent = true;
            if (formattingToolbarController.getToolbar() != null)
                formattingToolbarController.setToolBarStatus();
        }
    }


    public void removeDBTable(DBTable dbTable, boolean promptToStore) {
        if (dbTable == null || dbTable.table == null) //t || activeDBTable.dbTable.getDBase() == null)
            throw new NullPointerException();

        Table table = dbTable.table;
        try{
            if (!db.removeTable(dbTable.table, promptToStore))
                return;
        }catch (InsufficientPrivilegesException exc) {
            ESlateOptionPane.showMessageDialog(dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
            return;
        }

        detachTableListeners(table);
        /* If the removed Table was imported to the Database through the
         * tableImportPlug, then disconnect its Pin from the tableImportPlug.
         */
        Plug removedTablePin = table.getTablePlug();
        if (removedTablePin == null) return;
        Plug[] providerPins = tableImportPlug.getProviders();
        for (int i=0; i<providerPins.length; i++) {
            if (providerPins[i] == removedTablePin) {
                try{
                    tableImportPlug.disconnectPlug(removedTablePin);
                    break;
                }catch (PlugNotConnectedException exc) {}
            }
        }
    }


    public boolean activateDBTable(DBTable dbTable) {
        if (dbTable == null) throw new NullPointerException();
        int index;
        if ((index = dbTables.indexOf(dbTable)) == -1) return false;

        int realIndex = db.toTableIndex(index);
        if (realIndex < 0)
            return false;
        db.activateTable(realIndex, iterateEvent);
        activeDBTable = (DBTable) dbTables.get(index);
        tabs.setSelectedIndex(index);
        activeDBTableIndex = index;
        activeDBTable.jTable.requestFocus();

/* If there is no record selected in the Table, then make the jTable
         * unable to select rows. This is the only way to prohibit the default
         * selection of the first row of the JTable, when no record is selected
         * in the Table. Record selection is permitted again after any user
         * action (key press, mouse press).
         */
        if (activeDBTable.table.getSelectedSubset().size() == 0) {
            setCutCopyPasteStatus();
        }
        setSortButtonStatus();
        statusToolbarController.updateStatusBar(activeDBTable);
        if (formattingToolbarController.getToolbar() != null)
            formattingToolbarController.updateToolBarStatus();
        setActionStatus();
        setDBMenuRestStatus();
        setTableMenuRestStatus();
        setFieldMenuRestStatus();
        updateFieldMenuRestStatus();
        updateActionStatus();
        return true;
    }


    public boolean activateDBTableAt(int index) {
        if (index < 0 || index > dbTables.size()-1) return false;
        int realIndex = db.toTableIndex(index);
        if (realIndex < 0)
            return false;

        db.activateTable(realIndex, iterateEvent);
        activeDBTable = (DBTable) dbTables.get(index);
        activeDBTableIndex = index;
        tabs.setSelectedIndex(index);

/* If there is no record selected in the Table, then make the jTable
         * unable to select rows. This is the only way to prohibit the default
         * selection of the first row of the JTable, when no record is selected
         * in the Table. Record selection is permitted again after any user
         * action (key press, mouse press).
         */
        if (activeDBTable.table.getSelectedSubset().size() == 0) {
            setCutCopyPasteStatus();
        }
        setSortButtonStatus();

        statusToolbarController.updateStatusBar(activeDBTable);

        if (formattingToolbarController.getToolbar() != null)
            formattingToolbarController.updateToolBarStatus();
        setActionStatus();
        setDBMenuRestStatus();
        setTableMenuRestStatus();
        setFieldMenuRestStatus();
        updateFieldMenuRestStatus();
        updateActionStatus();
        return true;
    }

    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);


    protected DBase openDatabase(String fullPath, Component component, ArrayList openDatabases) {
        DBase newDatabase = DBase.openDBase(getESlateHandle(), fullPath, dbComponent, openDatabases);
        return newDatabase;
    }


    protected DBase openDatabase(URL url, Component component) {
        DBase newDatabase = DBase.openDBase(url, dbComponent);
        return newDatabase;
    }


    protected void openDB(Object fullPath) {
        /* Close the currently open database, if it exists.
         */
        boolean emptyPanelVisible = false;
        if (db != null) {
            if (closeDatabase(true, true) == 1) {
                dbComponent.setCursor(defaultCursor);
                return;
            }
        }else
            emptyPanelVisible = true;

        JPanel panelToWhichToAddMessage;
        if (mainPanel.getComponentCount() > 1) {
          panelToWhichToAddMessage = (mainPanel.getComponent(1).equals(emptyPanel))? emptyPanel : emptyDB;
          panelToWhichToAddMessage.remove(1);
          panelToWhichToAddMessage.add(dbIsLoadingPanel, 1);
          panelToWhichToAddMessage.validate();
          panelToWhichToAddMessage.doLayout();
          mainPanel.paintImmediately(mainPanel.getVisibleRect());
        }else
          panelToWhichToAddMessage = null;

        dbComponent.setCursor(waitCursor);
        ArrayList openDatabases = new ArrayList();
        openDatabases.add(db);
        DBase newDatabase;
        if (fullPath.getClass().equals(java.lang.String.class)) {
             newDatabase = openDatabase((String) fullPath, dbComponent, openDatabases);
        }else if (fullPath.getClass().equals(java.net.URL.class)) {
            newDatabase = openDatabase((URL) fullPath, dbComponent);
        }else{
            dbComponent.setCursor(defaultCursor);
            if (panelToWhichToAddMessage != null) {
                panelToWhichToAddMessage.remove(dbIsLoadingPanel);
                if (panelToWhichToAddMessage.equals(emptyDB))
                    panelToWhichToAddMessage.add(emptyDBLabelPanel, 1);
                else
                    panelToWhichToAddMessage.add(labelPanel, 1);
                panelToWhichToAddMessage.validate();
                panelToWhichToAddMessage.doLayout();
                mainPanel.paintImmediately(mainPanel.getVisibleRect());
            }
            return;
        }


        if (newDatabase == null) {
            dbComponent.setCursor(defaultCursor);
            if (panelToWhichToAddMessage != null) {
                panelToWhichToAddMessage.remove(dbIsLoadingPanel);
                if (panelToWhichToAddMessage.equals(emptyDB))
                    panelToWhichToAddMessage.add(emptyDBLabelPanel, 1);
                else
                    panelToWhichToAddMessage.add(labelPanel, 1);
                panelToWhichToAddMessage.validate();
                panelToWhichToAddMessage.doLayout();
                mainPanel.paintImmediately(mainPanel.getVisibleRect());
            }
            return;
        }

        visiblePopupMenu = colorPopupMenu;
        dbComponent.setCursor(waitCursor);

        try{
            newDatabase.getESlateHandle().getPlugs()[0].connectPlug(databaseImportPlug);
        }catch (PlugNotConnectedException exc) {
            exc.printStackTrace();
        }
    }

    protected void getDB(DBase newDatabase, boolean paintImmediately) {
        if (newDatabase == null) {
            /* Close the currently open database, if it exists.
             */
            boolean emptyPanelVisible = false;
            if (db != null) {
                if (databaseEditable)
                    closeDatabase(false, true);
                else
                    closeDatabase(false, false);

                if (eSlateHandle != null) {
                }
            }else
                emptyPanelVisible = true;
            db = null;
            if (eSlateHandle != null) {
            }
            dbComponent.setCursor(defaultCursor);
            return;
        }

        if (newDatabase.getDBView() != null) {
            dbView.setAdvancedPropertiesScope(newDatabase.getDBView().getAdvancedPropertiesScope());
            dbView.setColorPropertiesScope(newDatabase.getDBView().getColorPropertiesScope());
            dbView.setFieldPropertiesScope(newDatabase.getDBView().getFieldPropertiesScope());
        }else{
            dbView.setAdvancedPropertiesScope(DBView.DATABASE_SCOPE);
            dbView.setColorPropertiesScope(DBView.DATABASE_SCOPE);
            dbView.setFieldPropertiesScope(DBView.DATABASE_SCOPE);
        }

        visiblePopupMenu = colorPopupMenu;
        dbComponent.setCursor(waitCursor);
        JPanel panelToWhichToAddMessage;
        if (mainPanel.getComponentCount() > 1) {
            panelToWhichToAddMessage = (mainPanel.getComponent(1).equals(emptyPanel))? emptyPanel : emptyDB;
            panelToWhichToAddMessage.remove(1);
            panelToWhichToAddMessage.add(dbIsLoadingPanel, 1);
            panelToWhichToAddMessage.validate();
            panelToWhichToAddMessage.doLayout();
            if (paintImmediately)
                mainPanel.paintImmediately(mainPanel.getVisibleRect());
        }else
            panelToWhichToAddMessage = null;

        /* Close the currently open database, if it exists.
         */
        boolean emptyPanelVisible = false;
        if (db != null) {
            closeDatabase(true, false);
            if (eSlateHandle != null) {
            }
        }else
            emptyPanelVisible = true;

        db = newDatabase;
        db.setFileDialog(getFileDialog());
        if (emptyPanelVisible)
            dbComponent.mainPanel.remove(emptyPanel);

        displayHiddenTables = db.isHiddenTablesDisplayed();

        if (db.getTables() != null && db.getTables().size() != 0 && tabs == null) {
            dbComponent.mainPanel.remove(emptyPanel);
            createTabs();
//1            dbTables.clear();
        }

        DBTable dbt;
        for (int i=0; i<db.getTableCount(); i++) {
            if (displayHiddenTables || !db.getTableAt(i).isHidden()) {
/*1                TableViewStructure tvs = null;
                if (databaseTableViewStructures != null) {
                    if (eSlateHandle.getESlateMicroworld().isLoading())
                        tvs = databaseTableViewStructures.getTableViewStructure(db.getTableAt(i).getTitle());
                }
1*/
//1                dbt = new DBTable(db.getTableAt(i), dbComponent/*1, tvs*/);
                String tableTitle = db.getTableAt(i).getESlateHandle().getComponentName();
                dbt = (DBTable) restoredDBTables.get(tableTitle);
//if (dbt != null)
//System.out.println("getDB() dbTable: " + dbt);
//                dbt = (DBTable) dbTables.get(i);// Since format version 202 new DBTable(db.getTableAt(i), dbComponent/*1, tvs*/);
                if (dbt == null)
                    dbt = new DBTable(db.getTableAt(i), dbComponent);
                else
                    dbt.setTable(db.getTableAt(i), dbComponent);
                dbTables.add(dbt);
                addDBTableToDatabase(dbt);
                activateDBTable(dbt);
                dbt.addWidthChangeListener();
            }
        }
        restoredDBTables.clear();

        setCursor(defaultCursor);
        updateRecentDBList();

        db.addDatabaseListener(databaseAdapter);
        db.addPropertyChangeListener(pcl);
        /* Set the sharedObject.
         */
        if (eSlateHandle != null) {
        }

        if (panelToWhichToAddMessage != null) {
            panelToWhichToAddMessage.remove(dbIsLoadingPanel);
            if (panelToWhichToAddMessage.equals(emptyDB))
                panelToWhichToAddMessage.add(emptyDBLabelPanel, 1);
            else
                panelToWhichToAddMessage.add(labelPanel, 1);

            panelToWhichToAddMessage.validate();
            panelToWhichToAddMessage.doLayout();
            if (paintImmediately)
                mainPanel.paintImmediately(mainPanel.getVisibleRect());
        }
        statusToolbarController.setMessageLabelColor(Color.white);

        setActionStatus();
        setDBMenuRestStatus();
        setTableMenuRestStatus();
        setFieldMenuRestStatus();
        if (formattingToolbarController.getToolbar() != null)
            formattingToolbarController.setToolBarStatus();
    }

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        initialize(in);
    }

    private void oldTimeReadExternal(java.io.ObjectInput in, Object firstObj)
    throws IOException, ClassNotFoundException {
        DBase newDB = (DBase) firstObj;
        java.util.Hashtable tableNameMap = (java.util.Hashtable) in.readObject();
        try{
            boolean b1 = ((Boolean) in.readObject()).booleanValue();
            boolean b2 = ((Boolean) in.readObject()).booleanValue();
            setStandardToolbarVisible(b1);
            setFormattingToolBarVisible(b2);
        }catch (Exception exc) {}
        try{
            setUserMode(((Integer) in.readObject()).intValue());
        }catch (Exception exc) {}

        try{
            boolean b1 = ((Boolean) in.readObject()).booleanValue();
            setMenuBarVisible(b1);
        }catch (Exception exc) {}

        getDB(newDB, false);
    }

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(saveTimer);

        componentModified = false;
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        /* Check if there exists a childer of type DBase. If it exists then save it using
         * saveChildren().
         */
        ESlateHandle[] handles = eSlateHandle.toArray();
        ESlateHandle dbaseHandle = null;
        for (int i=0; i<handles.length; i++) {
            if (DBase.class.isAssignableFrom(handles[i].getComponent().getClass())) {
                dbaseHandle = handles[i];
                break;
            }
        }
        if (dbaseHandle != null)
            eSlateHandle.saveChildren(/*out,*/ fieldMap, "CDatabase", new ESlateHandle[] {dbaseHandle});

        // Save the DBTable children of the Database
        if (dbTables != null) {
            // Find the DBTables which are children of the Database.
            // The DBTables which are stored by the Database are the ones:
            // 1. Whose parent is the Database.
            // 2. Which contain a Table.
            // 3. Which contain a Table whose handle is the handle of the DBase hosted by the Database
            ArrayList dbTableChildHandles = new ArrayList();
            // 'storedDBTableTableTitles' holds the titles of the Tables of the DBTables to be saved.
            StringBaseArray storedDBTableTableTitles = new StringBaseArray();
            for (int i=0; i<dbTables.size(); i++) {
                DBTable dbTable = (DBTable) dbTables.get(i);
                if (dbTable.getESlateHandle().getParentHandle() == eSlateHandle) {
                    if (db != null && dbTable.table != null && dbTable.table.getESlateHandle().getParentHandle() == db.getESlateHandle()) {
                        dbTableChildHandles.add(dbTable.getESlateHandle());
                        storedDBTableTableTitles.add(dbTable.table.getESlateHandle().getComponentName());
                    }
                }
            }
System.out.println("Saving dbTableChildHandles: " + dbTableChildHandles);
            if (dbaseHandle != null)
                eSlateHandle.saveChildObjects(/*out,*/ fieldMap, "DBTables", dbTableChildHandles.toArray());
            fieldMap.put("Stored DBTables Table titles", storedDBTableTableTitles);
        }

//        if (db != null && db.getESlateHandle().getParentHandle() == eSlateHandle) //!foreignDatabase)
//            fieldMap.put("CDatabase", db);
//        fieldMap.put("TableNameMap", tableNameMap);
    //    System.out.println("write External 1");
        //fieldMap.put("StandardToolBarVisible", standardToolBarVisible);
        fieldMap.put("StandardToolBarVisible", isStandardToolbarVisible());
        fieldMap.put("FormattingToolBarVisible", isFormattingToolBarVisible());
        fieldMap.put("UserMode", userMode);
        fieldMap.put("MenuBarVisible", menu.menuBarVisible);
        //System.out.println("here0");
        //if (getBorder() != null) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);
                fieldMap.put("BorderDescriptor", bd);
            }catch (Throwable thr) {
                thr.printStackTrace();
            }
        //}
        //System.out.println("here00");
        fieldMap.put("StatusBarVisible", isStatusBarVisible());
        fieldMap.put("Tabbed Pane Opaque", tabTransparent);
        fieldMap.put("TableHeaderExpansionChangeAllowed", tableHeaderExpansionChangeAllowed);

        //System.out.println("here1");
        fieldMap.put("TableHeaderFont",getTableHeaderFont());
        //System.out.println("here2");
        fieldMap.put("FieldsHeaderFont",getFieldsHeaderFont());
        //System.out.println("here3");

        //Store the Database view settings
        fieldMap.put("DBView", dbView);
        //Create and store the list of the TableViewStructures
//1        databaseTableViewStructures = new DatabaseTableViewStructures(this);
//1        fieldMap.put("Table View Structures", databaseTableViewStructures);


        if (formattingToolbarController.getToolbar() != null){
            ESlateHandle[] children = new ESlateHandle[]{formattingToolbarController.getToolbar().getESlateHandle()};
            getESlateHandle().saveChildren(fieldMap, "formattingToolbarChild", children);
            fieldMap.put("formattingToolbarLocations",formattingToolbarController.getToolLocations());
        }
        if (standardToolbarController.getToolbar() != null){
            ESlateHandle[] children = new ESlateHandle[]{standardToolbarController.getToolbar().getESlateHandle()};
            getESlateHandle().saveChildren(fieldMap, "standardToolbarChild", children);
            fieldMap.put("standardToolbarLocations",standardToolbarController.getToolLocations());
        }
        if (statusToolbarController.getToolbar() != null){
            ESlateHandle[] children = new ESlateHandle[]{statusToolbarController.getToolbar().getESlateHandle()};
            getESlateHandle().saveChildren(fieldMap, "statusToolbarChild", children);
            fieldMap.put("statusToolbarLocations",statusToolbarController.getToolLocations());
        }

        out.writeObject(fieldMap);

        pm.stop(saveTimer);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new java.io.NotSerializableException("2323" + this.getClass().getName());
    }
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new java.io.NotSerializableException(this.getClass().getName());
    }

    protected void updateRecentDBList() {
        /* Add the opened database to the list "previousOpenDBs" of the opened databases.
         * The file names of these databases is stored in this list. Specifically,
         * "previousOpenDBs" stores the full filenames of the databases.
         */
        String newEntry;
        if (db.getDBFile() != null)
            newEntry = db.getDBFile().getPath();
        else
            return;

        /* Search "previousOpenDBs" to see if the name of the new database "newEntry" is
         * already contained in it.
         */
        boolean alreadyExists = false;
        int index = 0;
        for (int i=0; i<previousOpenDBs.size(); i++) {
            if (newEntry.equals((String) previousOpenDBs.at(i))) {
                alreadyExists = true;
                index = i;
                break;
            }
        }

        if (numOfPreviouslyOpenedDBs == 0)
            menu.getMenu(0).insertSeparator(PREVIOUS_DB_LIST_FIRST_MENU_ITEM_POS);

        if (!alreadyExists) {
            if (previousOpenDBs.size() == 4)
                previousOpenDBs.remove(3);

            previousOpenDBs.insert(0, newEntry);
            if (numOfPreviouslyOpenedDBs < 4) {
                numOfPreviouslyOpenedDBs++;
                ((JCheckBoxMenuItem) miPrevDBArray.at(numOfPreviouslyOpenedDBs-1)).setText((String) previousOpenDBs.at(0));
                menu.getMenu(0).insert((JCheckBoxMenuItem) miPrevDBArray.at(numOfPreviouslyOpenedDBs-1), PREVIOUS_DB_LIST_FIRST_MENU_ITEM_POS+1);
                addedMI.insert(0, (JCheckBoxMenuItem) miPrevDBArray.at(numOfPreviouslyOpenedDBs-1));
            }else{
                ((JCheckBoxMenuItem) addedMI.at(3)).setText(((JCheckBoxMenuItem) addedMI.at(2)).getText());
                ((JCheckBoxMenuItem) addedMI.at(2)).setText(((JCheckBoxMenuItem) addedMI.at(1)).getText());
                ((JCheckBoxMenuItem) addedMI.at(1)).setText(((JCheckBoxMenuItem) addedMI.at(0)).getText());
                ((JCheckBoxMenuItem) addedMI.at(0)).setText((String) previousOpenDBs.at(0));
            }
        }else{
            String temp = (String) previousOpenDBs.at(index);
            previousOpenDBs.remove(index);
            previousOpenDBs.insert(0, temp);

            for (int i=0; i<previousOpenDBs.size(); i++) {
                ((JCheckBoxMenuItem) addedMI.at(i)).setText((String) previousOpenDBs.at(i));
            }
        }
    }


    public Component getContentFrame() {
        return contentFrame;
    }


    public void createNewDBase(boolean emptyInNoviceMode) {
        dbComponent.setCursor(defaultCursor);
        boolean databaseWasOpen = false;
        if (db != null) {
            if (closeDatabase(true, true) == 1) {
                dbComponent.setCursor(defaultCursor);
                return;
            }
            databaseWasOpen = true;
        }

        try{
            DBase dbase = new DBase();
            ESlateHandle dbaseHandle = dbase.getESlateHandle();
            try{
                databaseImportPlug.connectPlug(dbaseHandle.getPlugs()[0]);
            }catch (PlugNotConnectedException exc) {
                exc.printStackTrace();
            }
            dbase.setFileDialog(getFileDialog());
            if (userMode == NOVICE_USER_MODE) {
                if (!emptyInNoviceMode)
                    addEmptyTable(dbase, 2, 1);
            }
            displayHiddenTables = dbase.isHiddenTablesDisplayed();
        }catch (UnableToInitializeDatabaseException e1) {
            ESlateOptionPane.showMessageDialog(dbComponent, infoBundle.getString("DatabaseMsg3"), errorStr, JOptionPane.ERROR_MESSAGE);
            return;
        }

        dbComponent.setCursor(defaultCursor);
    }


    public int closeDatabase(boolean substituteCurrentDatabase, boolean promptToStore) {
        if (db == null)
            return 0;
        if (!db.isNewDatabase() && db.isModified() && promptToStore) {
            try{
                String title, msg;
                if (db.getTitle() == null) {
                    title = infoBundle.getString("DatabaseMsg99");
                    msg = infoBundle.getString("DatabaseMsg48") + '?';
                }else{
                    title = infoBundle.getString("DatabaseMsg99");
                    msg = infoBundle.getString("DatabaseMsg48") + " \"" + db.getTitle() + "\"?";
                }

                Object[] yes_no_cancel = {infoBundle.getString("Yes"), infoBundle.getString("No"), infoBundle.getString("Cancel")};

                JOptionPane pane = new JOptionPane(msg,
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    javax.swing.UIManager.getIcon("OptionPane.questionIcon"),
                    yes_no_cancel,
                    infoBundle.getString("Yes"));
                javax.swing.JDialog dialog = pane.createDialog(new JFrame(), title);
                dialog.show();
                Object option = pane.getValue();

                if (option == infoBundle.getString("Cancel") || option == null)
                    return 1; // CANCEL

                if (option == infoBundle.getString("Yes")) {
                    db.saveChanges(true, dbComponent);
                }
                updateRecentDBList();
            }catch (UnableToSaveException e1) {
                ESlateOptionPane.showMessageDialog(dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                return 1;
            }
            catch (InvalidPathException e1) {
                ESlateOptionPane.showMessageDialog(dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                return 1;
            }
            catch (UnableToOverwriteException e1) {
                ESlateOptionPane.showMessageDialog(dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                return 1;
            }
        }
        menu.requestFocus();

        if (eSlateHandle != null) {
            /* Clear the HashMap which connects imported tables to the pins they were
             * exported from. But first release all the connections to pins that imported
             * tables in the Database.
             */
            tableImportPlug.disconnect();
        }

        db.removeDatabaseListener(databaseAdapter);
        db.unregisterVetoListener();
        db.removePropertyChangeListener(pcl);

        if (visiblePopupMenu != null)
            visiblePopupMenu.setVisible(false);
        visiblePopupMenu = null;

        if (dbTables != null) {
            for (int i=0; i<dbTables.size(); i++) {
                DBTable dbTable = (DBTable) dbTables.get(i);
                detachTableListeners(dbTable.table);
                dbTable.table.removeTableModelListener(dbTable.tableModelListener);
                dbTable.jTable.setRequestFocusEnabled(false);
                dbTable.jTable.setVisible(false);
                dbTable.jTable.setEnabled(false);
                dbTable.scrollpane.setRequestFocusEnabled(false);
                dbTable.scrollpane.removeAll();
                dbTable.removeAll();
                DBTableModel dbtm = (DBTableModel) dbTable.jTable.getModel();
//\                dbtm.table = null;
                dbtm.dbTable = null;
                if (tabs != null) tabs.remove(dbTable);
            }

            dbTables.clear();
        }

        db = null;
        databaseImportPlug.disconnect();
        if (tabs != null) {
            tabs.removeAll();
            tabPanel.remove(tabs);
            dbComponent.mainPanel.remove(tabPanel);
            dbComponent.mainPanel.validate();
            tabs = null;
            activeDBTable = null;
        }else{
            if (emptyDB != null && emptyDB.isVisible())
                dbComponent.mainPanel.remove(emptyDB);
        }

        if (!substituteCurrentDatabase)
            dbComponent.mainPanel.add(emptyPanel, BorderLayout.CENTER);

        String newName = "Database";
        int counter = 0;
        setActionStatus();
        setDBMenuRestStatus();
        setTableMenuRestStatus();
        setFieldMenuRestStatus();
        if (formattingToolbarController.getToolbar() != null)
            formattingToolbarController.setToolBarStatus();
        refreshDatabase();

        //The next line is very important for correct garbage collection
        //dbComponent.mainPanel.requestFocus();
        return 0;
    }


    protected JPanel createEmptyDBPanel() {
        JPanel emptyDB = new JPanel(true) {
            public void updateUI() {
                super.updateUI();
                setBackground(UIManager.getColor("control"));
            }
            public boolean isFocusable() {
                return false;
            }
        };
        emptyDB.setLayout(new BoxLayout(emptyDB, BoxLayout.Y_AXIS));
        emptyDB.setForeground(Color.black);

        emptyDBLabelPanel = new JPanel(true);
        emptyDBLabelPanel.setOpaque(false);
        emptyDBLabelPanel.setFocusable(false);

        JLabel emptyDBLabel = new JLabel(infoBundle.getString("DatabaseMsg73"));
        Font labelFont = new Font("TimesRoman", Font.BOLD + Font.ITALIC, 38);
        emptyDBLabel.setFont(labelFont);
        emptyDBLabel.setEnabled(false);
        emptyDBLabel.setAlignmentX(CENTER_ALIGNMENT);
        emptyDBLabel.setAlignmentY(CENTER_ALIGNMENT);
        emptyDBLabel.setFocusable(false);

        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(labelFont);
        int labelWidth = fm.stringWidth(infoBundle.getString("DatabaseMsg73"));
        emptyDBLabelPanel.add(emptyDBLabel);
        emptyDBLabelPanel.setAlignmentX(CENTER_ALIGNMENT);
        emptyDBLabelPanel.setAlignmentY(CENTER_ALIGNMENT);

        emptyDB.add(Box.createGlue());
        emptyDB.add(emptyDBLabelPanel);
        emptyDB.setFocusable(false);
        emptyDB.add(Box.createGlue());
        emptyDBLabelPanel.setPreferredSize(new Dimension(labelWidth, 50)); //300
        emptyDBLabelPanel.setMaximumSize(new Dimension(labelWidth, 50)); //300
        emptyDBLabelPanel.setMinimumSize(new Dimension(labelWidth, 50)); //300

        emptyDB.setBorder(new EtchedBorder());

        return emptyDB;
    }


    public JPanel createDBIsLoadingPanel() {
        JPanel dbIsLoading = new JPanel();
        dbIsLoading.setFocusable(false);
        JLabel lb = new JLabel(infoBundle.getString("DatabaseMsg92"));
        lb.setFont(new Font("TimesRoman", Font.ITALIC, 28));
        lb.setForeground(SystemColor.activeCaptionText);
        int stringWidth = timesRomanBold28Fm.stringWidth(infoBundle.getString("DatabaseMsg92"));
        dbIsLoading.setPreferredSize(new Dimension(stringWidth+15, 50));
        dbIsLoading.setMaximumSize(new Dimension(stringWidth+15, 50));
        dbIsLoading.setMinimumSize(new Dimension(stringWidth+15, 50));
        dbIsLoading.setOpaque(false);
        dbIsLoading.add(lb);
        lb.setAlignmentX(CENTER_ALIGNMENT);
        lb.setAlignmentY(CENTER_ALIGNMENT);
        lb.setFocusable(false);
        return dbIsLoading;
    }


    public synchronized void addColumnListener(ColumnListener cl) {
        if (columnListeners.indexOf(cl) == -1)
            columnListeners.add(cl);
    }


    public synchronized void removeColumnListener(ColumnListener cl) {
        if (columnListeners.indexOf(cl) != -1)
            columnListeners.remove(cl);
    }


    protected void fireColumnMoved(ColumnMovedEvent e) {
        Array cl;
        synchronized(this) {cl = (Array) columnListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ColumnListener) cl.at(i)).columnMoved(e);
    }


    protected void createTabMouseListener() {
        tabs.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (activeDBTable != null) {
                        if (tabs.getBoundsAt(activeDBTableIndex).contains(e.getPoint()))
                            tableRenameAction.execute();
                    }
                }
            }
        });
    }


    protected void createChangeListener() {
        tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
/*1                if (activeDBTable != null) {
                    activeDBTable.checkAndStorePendingRecord();
                }
                if (blockingNewRecord) {
                    tabs.setSelectedIndex(activeDBTableIndex);
                    return;
                }
1*/
                if (activeDBTable != null) {
                    if (!activeDBTable.commitNewRecord()) {
                        tabs.setSelectedIndex(activeDBTableIndex);
                        return;
                    }
                }

                dbComponent.activeDBTableIndex = dbComponent.tabs.getSelectedIndex();
                if (activeDBTableIndex != -1) {
                    activateDBTableAt(activeDBTableIndex);
                    if (activeDBTable != null) {
                        activeDBTable.jTable.requestFocus();
                        activeDBTable.refresh();
                        tableRenameAction.setEnabled(activeDBTable.table.isTableRenamingAllowed());
                    }
                }
                if (activeDBTable == null)
                    tableRenameAction.setEnabled(false);
            }
        });
    }


    protected void createTabs() {
        if (emptyDB != null && emptyDB.isVisible())
            dbComponent.mainPanel.remove(emptyDB);
        tabs = new JTabbedPane();
        tabs.setFocusable(false);
        if (tabTransparent)
            tabs.setOpaque(false);

        tabs.setBorder(null);
        tabs.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        tabs.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        tabPanel.add(tabs, BorderLayout.CENTER);
        dbComponent.mainPanel.add(tabPanel, BorderLayout.CENTER);
        tabs.setVisible(true);
        createChangeListener();
        createTabMouseListener();
    }

    public ArrayList getDbTables() {
        return dbTables;
    }

    public int getActiveDBTableIndex() {
        return activeDBTableIndex;
    }

    public Table createTable(Table ctable) {
        statusToolbarController.setMessageLabelInWaitState();
        dbComponent.setCursor(waitCursor);

        Table newTable = db.createEmptyTableReplicate(ctable);

        try{
            db.addTable(newTable, newTable.getTitle(), true);
    	}catch (InvalidTitleException exc) {
            dbComponent.setCursor(defaultCursor);
            statusToolbarController.setMessageLabelColor(Color.white);
            updateNumOfSelectedRecords(activeDBTable);
            ESlateOptionPane.showMessageDialog(dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
            dbComponent.setCursor(defaultCursor);
            return null;
        }
         catch (InsufficientPrivilegesException exc) {
            dbComponent.setCursor(defaultCursor);
            statusToolbarController.setMessageLabelColor(Color.white);
            updateNumOfSelectedRecords(activeDBTable);
            ESlateOptionPane.showMessageDialog(dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
            return null;
         }

/* Find the DBTable that contains Tables dbTable
         */
        DBTable table = null;
        for (int i=0; i<dbTables.size(); i++) {
            if (((DBTable) dbTables.get(i)).table.equals(ctable)) {
                table = (DBTable) dbTables.get(i);
                break;
            }
        }
        if (table != null)
            setColumnOrder(getActiveDBTable().jTable, table.jTable);

        dbComponent.setCursor(defaultCursor);
        statusToolbarController.setMessageLabelColor(Color.white);
        updateNumOfSelectedRecords(activeDBTable);
        return newTable;
	}


    public void updateTableFromSelectedSubset(Table ctable, Table replacedTable) {
/* Find the DBTable that contains Table dbTable
         */
        statusToolbarController.setMessageLabelInWaitState();
        dbComponent.setCursor(waitCursor);

        DBTable dbTable = getDBTableOfTable(replacedTable);

        if (dbTable == null) {
            dbComponent.setCursor(defaultCursor);
            statusToolbarController.setMessageLabelColor(Color.white);
            updateNumOfSelectedRecords(activeDBTable);
            return;
        }
        db.replaceTable(replacedTable, ctable, false);

        dbComponent.setCursor(defaultCursor);
        statusToolbarController.setMessageLabelColor(Color.white);
        updateNumOfSelectedRecords(activeDBTable);
    }

    public DBTable getDBTableOfTable(Table ctable) {
        DBTable dbTable = null;
        for (int i=0; i<dbTables.size(); i++) {
            if (((DBTable) dbTables.get(i)).table.equals(ctable)) {
                dbTable = (DBTable) dbTables.get(i);
                break;
            }
        }

        if (dbTable == null) {
            System.out.println("DBTable not found...");
        }

        return dbTable;
    }

    public int getDBTableIndex(Table ctable) {
        int index = -1;
        for (int i=0; i<dbTables.size(); i++) {
            if (((DBTable) dbTables.get(i)).table.equals(ctable)) {
                index = i;
                break;
            }
        }

        return index;
    }

    protected void setColumnOrder(JTable toTable, JTable fromTable) {
        DefaultTableColumnModel toDtcm = (DefaultTableColumnModel) toTable.getColumnModel();
        DefaultTableColumnModel fromDtcm = (DefaultTableColumnModel) fromTable.getColumnModel();

        TableColumn col;
        Array columnPositions = new Array();
        for (int k=0; k<fromTable.getColumnCount(); k++) {
            col = toDtcm.getColumn(k);
            columnPositions.add(new Integer(fromDtcm.getColumnIndex(col.getHeaderValue())));
        }

        int order;
        for (int k=0; k<columnPositions.size(); k++) {
            order = columnPositions.indexOf(new Integer(k));
            if (order != k) {
                toDtcm.moveColumn(order, k);
                Integer tmp = (Integer) columnPositions.at(order);
                columnPositions.remove(order);
                columnPositions.insert(k, tmp);

            }
        }
    }

    public DBase getDBase() {
        return db;
    }

    public void updateActiveRecord(DBTable dbTable, int activeRecord) {
        if (dbTable == null )
            return;
        if (activeRecord < 0 || activeRecord >= dbTable.table.recordIndex.size())
            dbTable.table.setActiveRecord(-1);
        else{
            scrollToActiveRecord = false;
            dbTable.table.setActiveRecord(activeRecord);
            scrollToActiveRecord = true;
        }
    }


/*    public void setActiveRow(int activeRow) {
        if (activeDBTable == null )
            return;
        if (activeRow < 0 || activeRow >= activeDBTable.table.recordIndex.size())
            activeDBTable.table.setActiveRecord(-1);
        else{
            scrollToActiveRecord = false;
            activeDBTable.table.setActiveRecord(activeDBTable.table.recordIndex.get(activeRow));
            scrollToActiveRecord = true;
        }
    }
*/
    public void updateActiveRecord(DBTable dbTable) {
        int recordCount = dbTable.table.getRecordCount();
        int activeRow = dbTable.activeRow;
        scrollToActiveRecord = false;
        activeDBTable.table.setActiveRecord(activeDBTable.table.recordIndex.get(activeRow));
        scrollToActiveRecord = true;
    }


    public void UIUpdateActiveRecord(DBTable dbTable) {
        int activeRecord = dbTable.table.getActiveRecord();
        int recordCount = dbTable.table.getRecordCount();
        if (activeRecord != -1) {
            int activeRecordRow = dbTable.table.rowForRecord(activeRecord);
/*            dbTable.jTable.getSelectionModel().setSelectionInterval(activeRecordRow, activeRecordRow);
            dbTable.activeRow = activeRecordRow;

            if (!dbTable.equals(activeDBTable))
                return;

            if (!scrollToActiveRecord)
                dbTable.rowBar.repaint();
            else{
                int newYPosition = dbTable.jTable.getCellRect(dbTable.activeRow, 0, true).y - (dbTable.scrollpane.getViewport().getExtentSize().height/2);
                if (newYPosition < 0)
                    newYPosition = 0;

                int viewHeight = dbTable.scrollpane.getViewport().getViewSize().height;
                if (newYPosition > viewHeight - dbTable.scrollpane.getViewport().getExtentSize().height)
                    newYPosition = viewHeight - dbTable.scrollpane.getViewport().getExtentSize().height;

                if (newYPosition >= 0 && dbTable.scrollpane.getViewport().getViewPosition().y != newYPosition) {
                    dbTable.scrollpane.getViewport().setViewPosition(new Point(
                        dbTable.scrollpane.getViewport().getViewPosition().x,
                        newYPosition
                    ));
                }else
                    dbTable.rowBar.repaint();
                    dbTable.refresh();
            }
*/
            removeRecordAction.setEnabled(true);

            // Set the status bar
            statusToolbarController.setRecordNumber(new Integer(activeRecordRow+1).toString());
            if (activeRecordRow == 0) {
                statusToolbarController.setPrevRecordEnabled(false);
                statusToolbarController.setFirstRecordEnabled(false);
            }else{
                statusToolbarController.setPrevRecordEnabled(true);
                statusToolbarController.setFirstRecordEnabled(true);
            }
            if (activeRecordRow == recordCount-1) {
                statusToolbarController.setLastRecordEnabled(false);
                statusToolbarController.setNextRecordEnabled(false);
            }else{
                statusToolbarController.setLastRecordEnabled(true);
                statusToolbarController.setNextRecordEnabled(true);
            }
        }else{
            dbTable.rowBar.resetActiveRow(dbTable.activeRow);
            dbTable.activeRow = -1;
            dbTable.jTable.getSelectionModel().clearSelection();

            if (!dbTable.equals(activeDBTable))
                return;

            removeRecordAction.setEnabled(false);
            // Set the status bar
            statusToolbarController.setRecordNumber("");
            statusToolbarController.setPrevRecordEnabled(false);
            statusToolbarController.setFirstRecordEnabled(false);
            statusToolbarController.setLastRecordEnabled(false);
            statusToolbarController.setNextRecordEnabled(false);
        }
        setCutCopyPasteStatus();
        standardToolbarController.updateIconEditStatus();
        standardToolbarController.updateInsertFilePathStatus();
    }


    public void updateNumOfSelectedRecords(int numOfSelectedRecords) {
        String message;
        if (numOfSelectedRecords == 0)
            message = infoBundle.getString("DatabaseMsg78");
        else if (numOfSelectedRecords == 1)
            message = numOfSelectedRecords + infoBundle.getString("DatabaseMsg79");
        else
            message = numOfSelectedRecords + infoBundle.getString("DatabaseMsg80");
        statusToolbarController.setMessageLabelText(message);

        int newMessageWidth = statusToolbarController.getMessageLabelSize(message); //helvPlain12Fm.stringWidth(message);
        int statusBarWidth = this.getSize().width;
        if (statusBarWidth-newMessageWidth >= 360) { //360 is the standard width of the rest of the elements in the status bar
            statusToolbarController.setMessageLabelSize(new Dimension(newMessageWidth, 15));
        }else{
            newMessageWidth = statusBarWidth - 360;
            if (newMessageWidth < 0) newMessageWidth = 0;
            statusToolbarController.setMessageLabelSize(new Dimension(newMessageWidth, 15));
        }


        if (statusToolbarController.getToolbar() != null){
            statusToolbarController.getToolbar().revalidate();
            statusToolbarController.getToolbar().repaint();
        }
    }

    public void updateNumOfSelectedRecords(DBTable dbTable) {
        if (dbTable == null)
            return;
        int numOfSelectedRecords = dbTable.table.getSelectedSubset().size();
        updateNumOfSelectedRecords(numOfSelectedRecords);
    }


    public void updateNumOfRecordsLabel() {
        if (activeDBTable != null) {
            int recordCount = activeDBTable.table.getRecordCount();
            statusToolbarController.setNumOfRecordsLabelText(infoBundle.getString("DatabaseMsg81") + recordCount);
        }else{
            statusToolbarController.setNumOfRecordsLabelText("");
        }
    }

    public void setTableHeaderFont(Font f){
        if (tabs == null) return;
          tabs.setFont(f);
    }

    public Font getTableHeaderFont() {
        if (tabs == null) return new Font("TimesRoman", Font.PLAIN, 16);
        return tabs.getFont();
    }

    public void setFieldsHeaderFont(Font f) {
        if (activeDBTable == null) return;
        if (activeDBTable.tableColumns == null) return;

        for (int i=0; i<activeDBTable.tableColumns.size(); i++){
            TableColumn col = activeDBTable.tableColumns.get(i).tableColumn;
            if (col == null) return;
            HeaderRenderer hr = ((HeaderRenderer) col.getHeaderRenderer());


            JTable table = activeDBTable.getJTable();

            JTableHeader h = table.getTableHeader();
            hr.setFont(f);
            h.validate();
            h.repaint(h.getVisibleRect());
        }
    }

    public Font getFieldsHeaderFont() {
        if (activeDBTable == null) return new Font("TimesRoman", Font.PLAIN, 16);;
        if (activeDBTable.tableColumns == null || activeDBTable.tableColumns.size() == 0)
            return new Font("TimesRoman", Font.PLAIN, 16);

        return activeDBTable.getHeaderFont();
/*        TableColumn col = (TableColumn) activeDBTable.tableColumns.get(0);
        if (col == null)
            return new Font("TimesRoman", Font.PLAIN, 16);
        HeaderRenderer hr = ((HeaderRenderer) col.getHeaderRenderer());
        return hr.getHeaderFont();
*/
    }


    public void setUserMode(int mode) {
        if (mode != ADVANCED_USER_MODE && mode != NOVICE_USER_MODE)
            return;

        if (mode == userMode)
            return;

        /* Change the component behaviour
         */
        int prevMode = userMode;
        userMode = mode;
        if (userMode == NOVICE_USER_MODE) {
            menu.tableMenu.remove(menu.miTableHidden);
            menu.miTableAuto.setVisible(false);

            menu.fieldMenu.remove(menu.ci1);
            menu.fieldMenu.remove(menu.ci2);
            menu.fieldMenu.remove(menu.ci5);
        }else{
            menu.miTableAuto.setVisible(true);
            menu.tableMenu.insert(menu.miTableHidden, 9);
            menu.fieldMenu.insert(menu.ci5, 6);
            menu.fieldMenu.insert(menu.ci2, 4);
            menu.fieldMenu.insert(menu.ci1, 4);
        }
        firePropertyChange("userMode", prevMode, userMode);
        componentModified = true;
    }


    public int getUserMode() {
        return userMode;
    }


    protected void fontChangeAction() {
        if (visiblePopupMenu != null && visiblePopupMenu.isVisible())
           visiblePopupMenu.setVisible(false);
        if (activeDBTable == null)
            return;

        String fontName = (String)formattingToolbarController.getFontName();
System.out.println("fontChangeAction() fontName: " + fontName);
        int style = Font.PLAIN;
        if (formattingToolbarController.isBoldSelected())
            style = style + Font.BOLD;
        if (formattingToolbarController.isItalicSelected())
            style = style + Font.ITALIC;
        Font newFont = new Font(fontName, style, new Integer(formattingToolbarController.getFontSize().toString()).intValue());
        if (!newFont.equals(activeDBTable.getTableFont())) {
System.out.println("Calling setTableFont() " + newFont);
	        activeDBTable.setTableFont(newFont);
	        activeDBTable.repaint();
	    }
    }

    protected void initializeColorPanel(Color color) {
        ColorBoxChooser colorBoxChooser = colorPanel.getColorBoxChooser();
        int colorIndex = colorBoxChooser.getColorIndex(color);
        if (colorIndex == -1)
            colorIndex = colorBoxChooser.addColor(color);
        colorBoxChooser.initActiveColorIndex(colorIndex);
    }


    protected void recordSelectAllAction() {
        activeDBTable.table.selectAll();
	      activeDBTable.refresh();
        setCutCopyPasteStatus();
    }


    public void printActiveTable() { //Originated from PixMaker.java

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        java.awt.PrintJob job = toolkit.getPrintJob(new JFrame(),"Print Dialog?", new java.util.Properties());

        if (job == null) {
            System.out.println("Canceled printing...");
            return;
        }

        Graphics page = job.getGraphics();

        Dimension size = activeDBTable.scrollpane.getSize();
        Dimension pagesize = job.getPageDimension();

        page.translate((pagesize.width - size.width)/2,(pagesize.height - size.height)/2);
        page.setClip(0,0,size.width,size.height);

        activeDBTable.scrollpane.paintAll(page);

        page.dispose();
        job.end();
    }


    private void removeDefaultKeyBindings() {
            JTextField tf = new JTextField();
            tf.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_V, java.awt.Event.CTRL_MASK));
            tf.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_C, java.awt.Event.CTRL_MASK));
            tf.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_X, java.awt.Event.CTRL_MASK));
    }


    public void addEmptyTable(DBase dbase, int colCount, int rowCount) {
        Table newTable = new Table(infoBundle.getString("New jTable"));
        for (int i=0; i<colCount; i++) {
            try{
	            newTable.addField(infoBundle.getString("Field") + (i+1),
	                               String.class,
	                               true,
//	                               false,
	                               true,
	                               false);
                }catch (InvalidFieldNameException e1) {ESlateOptionPane.showMessageDialog(dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);}
                 catch (InvalidKeyFieldException e1) {ESlateOptionPane.showMessageDialog(dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);}
                 catch (InvalidFieldTypeException e1) {ESlateOptionPane.showMessageDialog(dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);}
                 catch (AttributeLockedException e1) {ESlateOptionPane.showMessageDialog(dbComponent, e1.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);}
        }

        ArrayList emptyRecord = new ArrayList();
        for (int i=0; i<colCount; i++)
            emptyRecord.add(null);

        for (int i=0; i<rowCount; i++) {
            try{
                newTable.addRecord(emptyRecord, false);
            }catch (InvalidDataFormatException exc) {
                System.out.println("Serious inconsistency error in Database Table-->Auto-create : 1");
            }catch (NoFieldsInTableException exc) {
                System.out.println("Serious inconsistency error in Database Table-->Auto-create : 2");
            }catch (NullTableKeyException exc) {
                System.out.println("Serious inconsistency error in Database Table-->Auto-create : 3");
            }catch (DuplicateKeyException exc) {
                System.out.println("Serious inconsistency error in Database Table-->Auto-create : 4");
            }catch (TableNotExpandableException exc) {
                System.out.println("Serious inconsistency error in Database Table-->Auto-create : 5");
            }
        }

        try{
            dbase.addTable(newTable, newTable.getTitle(), true);
    	}catch (InvalidTitleException exc) {
            ESlateOptionPane.showMessageDialog(dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
            return;
        }
         catch (InsufficientPrivilegesException exc) {
            ESlateOptionPane.showMessageDialog(dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void displayHiddenTables(boolean display) {
        if (db == null)
            return;

        if (displayHiddenTables == display)
            return;

        dbComponent.setCursor(waitCursor);
        displayHiddenTables = display;
        if (displayHiddenTables) {
            int currActiveTableIndex = -1;
            if (dbTables.size() == 0) {
                if (db.getTableCount() > 0)
                    dbComponent.mainPanel.add(tabPanel, BorderLayout.CENTER);
            }else
                currActiveTableIndex = db.indexOf(activeDBTable.table);
            DBTable dbt;
            for (int i=0; i<db.getTableCount(); i++) {
                if (db.getTableAt(i).isHidden()) {
                    dbt = new DBTable(db.getTableAt(i), dbComponent/*1, null*/);
                    insertDBTableToDatabase(dbt, i);
                    dbt.addWidthChangeListener();
                }
            }
            if (currActiveTableIndex != -1)
                activateDBTableAt(currActiveTableIndex);
            else{
                if (dbTables.size() > 0)
                    activateDBTableAt(0);
            }
            refreshDatabase();
        }else{
            int currActiveTableIndex = -1;
            if (!activeDBTable.table.isHidden())
                currActiveTableIndex = db.indexOf(activeDBTable.table);

            int removedCount = 0;
            for (int i=0; i<db.getTableCount(); i++) {
                if (db.getTableAt(i).isHidden()) {
                    dbTables.remove(i-removedCount);
                    tabs.removeTabAt(i-removedCount);
                    removedCount++;
                }
            }
            if (currActiveTableIndex != -1)
                activateDBTableAt(db.toVisibleTableIndex(currActiveTableIndex));
            else{
                if (dbTables.size() == 0) {
                    dbComponent.mainPanel.remove(tabPanel);
                    activeDBTable = null;
                    setActionStatus();
                    setDBMenuRestStatus();
                    setTableMenuRestStatus();
                    setFieldMenuRestStatus();
                    formattingToolbarController.setToolBarStatus();
                }else
                    activateDBTableAt(0);
            }
            refreshDatabase();
        }
        dbComponent.setCursor(defaultCursor);
    }

    // Status (set-update) methods
    public void setDBMenuRestStatus() {
        if (!isMenuBarVisible())
            return;
        if (db == null) {
            closeAction.setEnabled(false);
            descriptionAction.setEnabled(false);
            renameAction.setEnabled(false);
            saveAsAction.setEnabled(false);
            exportTableAction.setEnabled(false);
            importTableAction.setEnabled(false);
        }else{
                closeAction.setEnabled(true);
                renameAction.setEnabled(true);
                saveDBAction.setEnabled(true);
            if (db.isTableAdditionAllowed())
              importTableAction.setEnabled(true);
           else
              importTableAction.setEnabled(false);
           if (db.getActiveTable() != null) {
                if (db.isTableExportationAllowed())
                    exportTableAction.setEnabled(true);
                else
                    exportTableAction.setEnabled(false);
            }else
                exportTableAction.setEnabled(false);
            descriptionAction.setEnabled(true);
            /* Specify the selected JCheckBoxMenuItem in the list of the recentry opened
             * databases. This item refers to the currently open database.
             */
            if (db!= null && previousOpenDBs.size() > 0 && db.getDBFile() != null) {
               ((JCheckBoxMenuItem) addedMI.at(0)).setSelected(true);
               for (int i=1; i<addedMI.size(); i++)
                ((JCheckBoxMenuItem) addedMI.at(i)).setSelected(false);
            }else if (db != null && previousOpenDBs.size() > 0 && db.getDBFile() == null) {
               for (int i=0; i<addedMI.size(); i++)
                ((JCheckBoxMenuItem) addedMI.at(i)).setSelected(false);
            }else if (db == null && previousOpenDBs.size() > 0) {
               for (int i=0; i<addedMI.size(); i++)
                ((JCheckBoxMenuItem) addedMI.at(i)).setSelected(false);
            }
        }
   }// end setDBMenuRestStatus

   public void setTableMenuRestStatus() {
        if (!isMenuBarVisible())
            return;
        if (db == null) {
            newTableAction.setEnabled(false);
            tableAddAction.setEnabled(false);
            tableAutoAction.setEnabled(false);
        }else{
            if (db.isTableAdditionAllowed()){
              newTableAction.setEnabled(true);
              tableAutoAction.setEnabled(true);
              tableAddAction.setEnabled(true);
           }
           else{
              newTableAction.setEnabled(false);
              tableAutoAction.setEnabled(false);
              tableAddAction.setEnabled(false);
           }
        }
        if (activeDBTable == null) {
            tableRemoveAction.setEnabled(false);
            tableSaveAsAction.setEnabled(false);
            tableSaveUISettingsAction.setEnabled(false);
            tableRenameAction.setEnabled(false);
            tableDescriptionAction.setEnabled(false);
            tableHiddenAction.setEnabled(false);
            tableJoinAction.setEnabled(false);
            tableIntersectAction.setEnabled(false);
            tableUnionAction.setEnabled(false);
            tableThJoinAction.setEnabled(false);
            tableInfoAction.setEnabled(false);
            databaseTableSortAction.setEnabled(false);
            menu.miTableAutoResize.setEnabled(false);
            tableAutoResizeOffAction.setEnabled(false);
            tableAutoResizeAllAction.setEnabled(false);
            tableAutoResizeLastAction.setEnabled(false);
            tablePrefAction.setEnabled(false);
        }
        else{
            tableRenameAction.setEnabled(activeDBTable.table.isTableRenamingAllowed());
            tableDescriptionAction.setEnabled(true);
            tableJoinAction.setEnabled(true);
            tableIntersectAction.setEnabled(true);
            tableUnionAction.setEnabled(true);
            tableThJoinAction.setEnabled(true);
            tableInfoAction.setEnabled(true);
            databaseTableSortAction.setEnabled(true);
            tablePrefAction.setEnabled(true);
            menu.miTableAutoResize.setEnabled(true);
            tableAutoResizeOffAction.setEnabled(true);
            tableAutoResizeAllAction.setEnabled(true);
            tableAutoResizeLastAction.setEnabled(true);
            if (db != null && db.isTableRemovalAllowed())
                tableRemoveAction.setEnabled(true);
            else
                tableRemoveAction.setEnabled(false);
            if (db != null && db.isTableExportationAllowed())
                tableSaveAsAction.setEnabled(true);
            else
                tableSaveAsAction.setEnabled(false);
            if (activeDBTable.table.isHiddenAttributeChangeAllowed())
                tableHiddenAction.setEnabled(true);
            else
                tableHiddenAction.setEnabled(false);
            if (activeDBTable.table.isHidden())
                menu.miTableHidden.setSelected(true);
            else
                menu.miTableHidden.setSelected(false);
            tableSaveUISettingsAction.setEnabled(true);
            //************
            updateAutoResizeActions();
        }
   } // setTableMenuRestStatus

   public void updateAutoResizeActions() {
      menu.miTableAutoResize.setEnabled(true);
      menu.miTableAutoResizeOff.setIcon(radioButtonResetIcon);
      menu.miTableAutoResizeLast.setIcon(radioButtonResetIcon);
      menu.miTableAutoResizeAll.setIcon(radioButtonResetIcon);

      int autoResizeMode = activeDBTable.autoResizeMode; //1 activeDBTable.viewStructure.tableView.getAutoResizeMode();
      if (autoResizeMode == activeDBTable.jTable.AUTO_RESIZE_OFF)
          menu.miTableAutoResizeOff.setIcon(radioButtonIcon);
      else if (autoResizeMode == activeDBTable.jTable.AUTO_RESIZE_LAST_COLUMN)
          menu.miTableAutoResizeLast.setIcon(radioButtonIcon);
      else if (autoResizeMode == activeDBTable.jTable.AUTO_RESIZE_ALL_COLUMNS)
          menu.miTableAutoResizeAll.setIcon(radioButtonIcon);
   }

   public void setFieldMenuRestStatus() {
        if (!isMenuBarVisible())
            return;
        if (activeDBTable  == null) {
            fieldPropertiesAction.setEnabled(false);
            fieldEditableAction.setEnabled(false);
            menu.ci1.setSelected(false);
            fieldRemovableAction.setEnabled(false);
            menu.ci2.setSelected(false);
            fieldKeyAction.setEnabled(false);
            menu.ci3.setSelected(false);
            fieldCalculateAction.setEnabled(false);
            menu.ci4.setSelected(false);
            fieldHiddenAction.setEnabled(false);
            menu.ci5.setSelected(false);
            addFieldAction.setEnabled(false);
            removeFieldAction.setEnabled(false);
            menu.dataTypeMenu.setEnabled(false);
            fieldSelectAllAction.setEnabled(false);
            fieldClearSelectionAction.setEnabled(false);
            fieldWidthAction.setEnabled(false);
            fieldFreezeAction.setEnabled(false);
            menu.miFieldFreeze.setSelected(false);
            sortAscAction.setEnabled(false);
            sortDescAction.setEnabled(false);
            return;
        }else{
            if (activeDBTable.table.isFieldAdditionAllowed())
                addFieldAction.setEnabled(true);
            else
                addFieldAction.setEnabled(false);

            if (activeDBTable.tableColumns.size() > 0)
                fieldSelectAllAction.setEnabled(true);
            else
                fieldSelectAllAction.setEnabled(false);
        }
   }

   public void updateFieldMenuRestStatus() {
      int numOfSelectedColumns;
      if (!activeDBTable.isSimultaneousFieldRecordActivation()) {
          /* No column selection is allowed, so "numOfSelectedColumns" = 0.
           * However, when a field header is clicked, we explicitly set "columnSelectionAllowed"
           * to "true", so that the field will be selected. In this case calculate the selected
           * columns.
           */
          if (activeDBTable.jTable.getColumnSelectionAllowed())
              numOfSelectedColumns = activeDBTable.jTable.getSelectedColumnCount();
          else
              numOfSelectedColumns = 0;
      }else
          numOfSelectedColumns = activeDBTable.jTable.getSelectedColumnCount();

      if (numOfSelectedColumns == 1) {
          fieldPropertiesAction.setEnabled(activeDBTable.table.isFieldPropertyEditingAllowed());
          if (activeDBTable.table.isFieldRemovalAllowed())
              removeFieldAction.setEnabled(true);
          else
              removeFieldAction.setEnabled(false);
          if (activeDBTable.isSimultaneousFieldRecordActivation())
              fieldClearSelectionAction.setEnabled(false);
          else
              fieldClearSelectionAction.setEnabled(true);
          fieldWidthAction.setEnabled(true);
          int index = activeDBTable.jTable.getSelectedColumn();
          TableColumn col1 = (TableColumn) activeDBTable.tableColumns.get(index).tableColumn;
          menu.miFieldFreeze.setSelected(!col1.getResizable());
/*
          if (col1.getMinWidth() == col1.getMaxWidth())
              menu.miFieldFreeze.setSelected(true);
          else
              menu.miFieldFreeze.setSelected(false);
*/
          fieldFreezeAction.setEnabled(true);
          sortAscAction.setEnabled(true);
          sortDescAction.setEnabled(true);
          TableColumn col = (TableColumn) activeDBTable.tableColumns.get(activeDBTable.jTable.getSelectedColumn()).tableColumn;
          String identifier = (String) col.getIdentifier();
          try{
              AbstractTableField f = activeDBTable.table.getTableField(identifier);

              if (f.isFieldEditabilityChangeAllowed() && activeDBTable.table.isFieldPropertyEditingAllowed())
                  fieldEditableAction.setEnabled(true);
              else
                  fieldEditableAction.setEnabled(false);
              if (f.isFieldRemovabilityChangeAllowed() && activeDBTable.table.isFieldPropertyEditingAllowed())
                  fieldRemovableAction.setEnabled(true);
              else
                  fieldRemovableAction.setEnabled(false);

              if (activeDBTable.table.isKeyChangeAllowed() && f.isFieldKeyAttributeChangeAllowed() && activeDBTable.table.isFieldPropertyEditingAllowed())
                  fieldKeyAction.setEnabled(true);
              else
                  fieldKeyAction.setEnabled(false);
              if (f.isFieldHiddenAttributeChangeAllowed() && activeDBTable.table.isFieldPropertyEditingAllowed())
                  fieldHiddenAction.setEnabled(true);
              else
                  fieldHiddenAction.setEnabled(false);

              menu.dataTypeMenu.setEnabled(true);

              if (f.isEditable())
                  menu.ci1.setSelected(true);
              else
                  menu.ci1.setSelected(false);
              if (f.isRemovable())
                  menu.ci2.setSelected(true);
              else
                  menu.ci2.setSelected(false);
              if (activeDBTable.table.isPartOfTableKey(f)/*f.isKey()*/)
                  menu.ci3.setSelected(true);
              else
                  menu.ci3.setSelected(false);
              if (f.isCalculated()) {
                  fieldEditableAction.setEnabled(false);
                  menu.ci4.setSelected(true);
                  if (f.isCalcFieldResetAllowed() && activeDBTable.table.isFieldPropertyEditingAllowed())
                      fieldCalculateAction.setEnabled(true);
                  else
                      fieldCalculateAction.setEnabled(false);
              }else{
                  if (f.isFieldEditabilityChangeAllowed() && activeDBTable.table.isFieldPropertyEditingAllowed())
                      fieldEditableAction.setEnabled(true);
                  else
                      fieldEditableAction.setEnabled(false);
                  fieldCalculateAction.setEnabled(false);
                  menu.ci4.setSelected(false);
              }
              if (f.isHidden())
                  menu.ci5.setSelected(true);
              else
                  menu.ci5.setSelected(false);

              updateDataTypes();
          }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in Database createMenuBar(): (16)"); return;}
      }else{
          sortAscAction.setEnabled(false);
          sortDescAction.setEnabled(false);
          fieldPropertiesAction.setEnabled(false);
          fieldEditableAction.setEnabled(false);
          menu.ci1.setSelected(false);
          fieldRemovableAction.setEnabled(false);
          menu.ci2.setSelected(false);
          fieldKeyAction.setEnabled(false);
          menu.ci3.setSelected(false);
          menu.ci4.setSelected(false);
          fieldCalculateAction.setEnabled(false);
          menu.ci5.setSelected(false);
          fieldHiddenAction.setEnabled(false);
          menu.dataTypeMenu.setEnabled(false);

          if (numOfSelectedColumns > 1) {
              fieldClearSelectionAction.setEnabled(true);
              if (activeDBTable.table.isFieldRemovalAllowed())
                  removeFieldAction.setEnabled(true);
              else
                  removeFieldAction.setEnabled(false);
              fieldWidthAction.setEnabled(true);

              int[] indices = activeDBTable.jTable.getSelectedColumns();
              TableColumn col1 = activeDBTable.tableColumns.get(indices[0]).tableColumn;
              boolean areAllFrozen = true;
              if (!col1.getResizable()) { //col1.getMinWidth() == col1.getMaxWidth()) {
                  for (int i=1; i<indices.length; i++) {
                      col1 = activeDBTable.tableColumns.get(indices[i]).tableColumn;
                      if (!col1.getResizable()) //(col1.getMinWidth() == col1.getMaxWidth())
                          continue;
                      areAllFrozen = false;
                      break;
                  }
              }else
                  areAllFrozen = false;
              if (areAllFrozen)
                  menu.miFieldFreeze.setSelected(true);
              else
                  menu.miFieldFreeze.setSelected(false);
              fieldFreezeAction.setEnabled(true);
        }else{
            fieldClearSelectionAction.setEnabled(false);
            removeFieldAction.setEnabled(false);
            fieldWidthAction.setEnabled(false);
            fieldFreezeAction.setEnabled(false);
            menu.miFieldFreeze.setSelected(false);
        }
      }
   }

   public void updateDataTypes() {
      TableColumn col = activeDBTable.tableColumns.get(activeDBTable.jTable.getSelectedColumn()).tableColumn;
      String identifier = (String) col.getIdentifier();
      try{
          AbstractTableField f = activeDBTable.table.getTableField(identifier);
          menu.doubleType.setSelected(false);
      menu.doubleType.setIcon(radioButtonResetIcon);
      menu.strType.setSelected(false);
      menu.strType.setIcon(radioButtonResetIcon);
      menu.boolType.setSelected(false);
      menu.boolType.setIcon(radioButtonResetIcon);
      menu.dateType.setSelected(false);
      menu.dateType.setIcon(radioButtonResetIcon);
      menu.timeType.setSelected(false);
      menu.timeType.setIcon(radioButtonResetIcon);
      menu.urlType.setSelected(false);
      menu.urlType.setIcon(radioButtonResetIcon);
      menu.imageType.setSelected(false);
      menu.imageType.setIcon(radioButtonResetIcon);
      if (activeDBTable.table.isDataChangeAllowed() && f.isFieldDataTypeChangeAllowed() && activeDBTable.table.isFieldPropertyEditingAllowed()) {
          fieldDoubleTypeAction.setEnabled(true);
          fieldStrTypeAction.setEnabled(true);
          fieldBoolTypeAction.setEnabled(true);
          fieldDateTypeAction.setEnabled(true);
          fieldTimeTypeAction.setEnabled(true);
          fieldURLTypeAction.setEnabled(true);
          fieldImageTypeAction.setEnabled(true);
      }else{
          fieldDoubleTypeAction.setEnabled(false);
          fieldStrTypeAction.setEnabled(false);
          fieldBoolTypeAction.setEnabled(false);
          fieldDateTypeAction.setEnabled(false);
          fieldTimeTypeAction.setEnabled(false);
          fieldURLTypeAction.setEnabled(false);
          fieldImageTypeAction.setEnabled(false);
      }

      String fldType = f.getDataType().getName().substring(f.getDataType().getName().lastIndexOf('.')+1, f.getDataType().getName().length());
          if (fldType.equals("Double"))
              menu.doubleType.setIcon(radioButtonIcon);
          else if (fldType.equals("String"))
              menu.strType.setIcon(radioButtonIcon);
          else if (fldType.equals("Boolean"))
              menu.boolType.setIcon(radioButtonIcon);
          else if (fldType.equals("CDate"))
                menu.dateType.setIcon(radioButtonIcon);
            else if (fldType.equals("CTime"))
                menu.timeType.setIcon(radioButtonIcon);
          else if (fldType.equals("URL"))
              menu.urlType.setIcon(radioButtonIcon);
          else if (fldType.equals("CImageIcon"))
              menu.imageType.setIcon(radioButtonIcon);
      }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in Database createMenuBar(): (16)"); return;}
   }

    public void setActionStatus() {
        if (db == null) {
            // database related actions
            saveDBAction.setEnabled(false);
                newDBAction.setEnabled(true);
                openDBAction.setEnabled(true);

            addFieldAction.setEnabled(false);
            removeFieldAction.setEnabled(false);
            fieldPropertiesAction.setEnabled(false);

            promoteSelRecsAction.setEnabled(false);

            cutAction.setEnabled(false);
            copyAction.setEnabled(false);
            pasteAction.setEnabled(false);
            imageEditAction.setEnabled(false);
            insFilePathAction.setEnabled(false);

        }else{
           // database related actions
           saveDBAction.setEnabled(true);
        }

        if (activeDBTable == null) {
            findAction.setEnabled(false);
            findNextAction.setEnabled(false);
            findPrevAction.setEnabled(false);
            selectAllRecsAction.setEnabled(false);
            addRecordAction.setEnabled(false);
            removeRecordAction.setEnabled(false);
            removeSelRecordAction.setEnabled(false);
            clearRecAction.setEnabled(false);
            invertRecAction.setEnabled(false);
        }else{
            findAction.setEnabled(true);
            if (activeDBTable.table.getFieldCount() == 0)
                addRecordAction.setEnabled(false);
            else{
                if (activeDBTable.table.isRecordAdditionAllowed())
                    addRecordAction.setEnabled(true);
                else
                    addRecordAction.setEnabled(false);
            }

            int numOfSelectedRows = activeDBTable.table.getSelectedRecordCount();
            if (numOfSelectedRows == 0) {
                removeSelRecordAction.setEnabled(false);
                promoteSelRecsAction.setEnabled(false);
                clearRecAction.setEnabled(false);
            }else{
                if (activeDBTable.table.isRecordRemovalAllowed())
                    removeSelRecordAction.setEnabled(true);
                else
                    removeSelRecordAction.setEnabled(false);
                promoteSelRecsAction.setEnabled(true);
                clearRecAction.setEnabled(true);
            }
            if (activeDBTable.activeRow == -1)
                removeRecordAction.setEnabled(false);
            else{
                if (activeDBTable.table.isRecordRemovalAllowed())
                    removeRecordAction.setEnabled(true);
                else
                    removeRecordAction.setEnabled(false);
            }

            if (activeDBTable.table.getRecordCount() != 0 &&
                activeDBTable.table.getRecordCount() > activeDBTable.activeRow)
                    selectAllRecsAction.setEnabled(true);
            else
                    selectAllRecsAction.setEnabled(false);

            if (activeDBTable.table.getRecordCount() != 0)
                invertRecAction.setEnabled(true);
            else
                invertRecAction.setEnabled(false);
        }
        //mainPanel.repaint(standardToolBarPanel.getVisibleRect());
    }

    public void updateActionStatus() {
        if (activeDBTable.table.getActiveRecord() != -1 && activeDBTable.jTable.getSelectedColumn() != -1) {
            copyAction.setEnabled(true);
            if (activeDBTable.table.isDataChangeAllowed()) {
                cutAction.setEnabled(true);
                if (ESlateClipboard.getContents() != null)
                    pasteAction.setEnabled(true);
                else
                    pasteAction.setEnabled(false);
            }else{
                cutAction.setEnabled(false);
                pasteAction.setEnabled(false);
            }
        }else{
            cutAction.setEnabled(false);
            copyAction.setEnabled(false);
            pasteAction.setEnabled(false);
        }
        if (activeDBTable.table.getFieldCount() != 0) {
            if (activeDBTable.table.isRecordAdditionAllowed())
                addRecordAction.setEnabled(true);
            else
                addRecordAction.setEnabled(false);
        }else
            addRecordAction.setEnabled(false);

        if (activeDBTable.table.getActiveRecord() != -1) {
            if (activeDBTable.table.isRecordRemovalAllowed())
                removeRecordAction.setEnabled(true);
            else
                removeRecordAction.setEnabled(false);
        }else
            removeRecordAction.setEnabled(false);

        if (activeDBTable.table.getSelectedRecordCount() != 0) {
            if (activeDBTable.table.isRecordRemovalAllowed())
                removeSelRecordAction.setEnabled(true);
            else
                removeSelRecordAction.setEnabled(false);
            clearRecAction.setEnabled(true);
            promoteSelRecsAction.setEnabled(true);
        }else{
            removeSelRecordAction.setEnabled(false);
            clearRecAction.setEnabled(false);
            promoteSelRecsAction.setEnabled(false);
        }

        if (activeDBTable.table.isFieldAdditionAllowed())
            addFieldAction.setEnabled(true);
        else
            addFieldAction.setEnabled(false);

        int numOfSelectedColumns;
        if (!activeDBTable.isSimultaneousFieldRecordActivation()) {
           /* No column selection is allowed, so "numOfSelectedColumns" = 0.
            * However, when a field header is clicked, we explicitly set "columnSelectionAllowed"
            * to "true", so that the field will be selected. In this case calculate the selected
            * columns.
            */
           if (activeDBTable.jTable.getColumnSelectionAllowed())
               numOfSelectedColumns = activeDBTable.jTable.getSelectedColumnCount();
           else
               numOfSelectedColumns = 0;
        }else
            numOfSelectedColumns = activeDBTable.jTable.getSelectedColumnCount();

        if (numOfSelectedColumns > 0) {
            if (activeDBTable.table.isFieldRemovalAllowed())
                removeFieldAction.setEnabled(true);
            else
                removeFieldAction.setEnabled(false);
        }else
            removeFieldAction.setEnabled(false);
        if (numOfSelectedColumns == 1) {
            fieldPropertiesAction.setEnabled(activeDBTable.table.isFieldPropertyEditingAllowed());
        }else{
            fieldPropertiesAction.setEnabled(false);
        }
        if (numOfSelectedColumns < 1 || numOfSelectedColumns > 3) {
            sortDescAction.setEnabled(false);
            sortAscAction.setEnabled(false);
        }else{
            sortDescAction.setEnabled(true);
            sortAscAction.setEnabled(true);
        }

        if (activeDBTable.table.getRecordCount() != 0) {
            selectAllRecsAction.setEnabled(true);
            invertRecAction.setEnabled(true);
            findAction.setEnabled(true);
        }else{
            selectAllRecsAction.setEnabled(false);
            invertRecAction.setEnabled(false);
            findAction.setEnabled(false);
        }

        if (findDialog != null && !findDialog.findWhatFieldEmpty) {
            findNextAction.setEnabled(true);
            findPrevAction.setEnabled(true);
        }else{
            findNextAction.setEnabled(false);
            findPrevAction.setEnabled(false);
        }

        if (activeDBTable.jTable.getSelectedColumn() == -1) {
            imageEditAction.setEnabled(false);
            insFilePathAction.setEnabled(false);
            return;
        }
        if (activeDBTable.table.getFieldCount() > 0) {
            int selectedColumn = activeDBTable.jTable.getSelectedColumn();
            if (selectedColumn != -1) {
                AbstractTableField fld = activeDBTable.getTableField(selectedColumn);
//1                String fieldName = activeDBTable.tableModel.getColumnName(((Integer) activeDBTable.viewStructure.tableView.getColumnOrder().at(selectedColumn)).intValue());
//1                AbstractTableField fld;
//1                try{
//1                    fld = activeDBTable.table.getTableField(fieldName);
//1                }catch (InvalidFieldNameException e) {
//1                    System.out.println("Serious inconsistency error in DBase setCutCopyPasteStatus(): 1");
//1                    return;
//1                }
                if (iconEditorClass != null && fld.isEditable() && fld.getDataType().getName().equals("gr.cti.eslate.database.engine.CImageIcon") && activeDBTable.table.isDataChangeAllowed())
                    imageEditAction.setEnabled(true);
                else
                    imageEditAction.setEnabled(false);
            }else
                imageEditAction.setEnabled(false);
        }else
            imageEditAction.setEnabled(false);
        if (activeDBTable.table.getFieldCount() > 0) {
        	int selectedColumn = activeDBTable.jTable.getSelectedColumn();
        	if (selectedColumn != -1 && activeDBTable.activeRow!=-1) {
        		AbstractTableField fld = activeDBTable.getTableField(selectedColumn);
        		if (fld.isEditable() && fld.getDataType().getName().equals("java.lang.String") && activeDBTable.table.isDataChangeAllowed())
        			insFilePathAction.setEnabled(true);
        		else
        			insFilePathAction.setEnabled(false);
        	}else
        		insFilePathAction.setEnabled(false);
        }else
        	insFilePathAction.setEnabled(false);
    }

    public void updateAddRecordStatus() {
        if (activeDBTable.table.getFieldCount() != 0 && activeDBTable.table.isRecordAdditionAllowed())
            addRecordAction.setEnabled(true);
        else
            addRecordAction.setEnabled(false);
    }

    public void updateRemoveRec_ClearRecSelection_PromoteStatus() {
        if (activeDBTable.table.getSelectedRecordCount() != 0) {
            if (activeDBTable.table.isRecordRemovalAllowed())
                removeSelRecordAction.setEnabled(true);
            else
                removeSelRecordAction.setEnabled(false);
            clearRecAction.setEnabled(true);
            promoteSelRecsAction.setEnabled(true);
        }else{
            removeSelRecordAction.setEnabled(false);
            clearRecAction.setEnabled(false);
            promoteSelRecsAction.setEnabled(false);
        }
    }

    public void updateSelectAll_InvertRecSelectionStatus() {
        if (activeDBTable.table.getRecordCount() != 0) {
            selectAllRecsAction.setEnabled(true);
            invertRecAction.setEnabled(true);
        }else{
            selectAllRecsAction.setEnabled(false);
            invertRecAction.setEnabled(false);
        }
    }

    public void updateRemoveRecordStatus(int activeRecord) {
        if (activeRecord != -1) {
            if (activeDBTable != null && activeDBTable.table.isRecordRemovalAllowed())
                removeRecordAction.setEnabled(true);
            else
                removeRecordAction.setEnabled(false);
        }else
            removeRecordAction.setEnabled(false);
    }


    protected void setCutCopyPasteStatus() {
        /* Don't check the status if the standard toolbar is not visible.
         */
        if (standardToolbarController.getToolbar() == null)
            return;

        if (!standardToolbarController.getToolbar().isVisible())
            return;

        boolean repaintNeeded = false;
        if (activeDBTable == null) {
            if (cutAction.isEnabled()) {
                cutAction.setEnabled(false);
                copyAction.setEnabled(false);
                pasteAction.setEnabled(false);
            }
        }else{
            if (activeDBTable.table.getFieldCount() > 0) {
                int selectedColumn = activeDBTable.jTable.getSelectedColumn();
                if (selectedColumn == -1) {
                    cutAction.setEnabled(false);
                    copyAction.setEnabled(false);
                    pasteAction.setEnabled(false);
                    return;
                }

                if (activeDBTable.table.getActiveRecord() == -1) {
                    cutAction.setEnabled(false);
                    copyAction.setEnabled(false);
                    pasteAction.setEnabled(false);
                    return;
                }

                AbstractTableField fld = activeDBTable.getTableField(selectedColumn);
/*1                String fieldName;
                try{
                  fieldName =  activeDBTable.tableModel.getColumnName(((Integer) activeDBTable.viewStructure.tableView.getColumnOrder().at(selectedColumn)).intValue());
                }catch (Exception exc) {
                    System.out.println("Serious error because of JTable behaviour when removing columns. Correcting...");
                    cutAction.setEnabled(false);
                    copyAction.setEnabled(false);
                    pasteAction.setEnabled(false);
                    return;
                }
                int fieldIndex;
                try{
                    fieldIndex = activeDBTable.table.getFieldIndex(fieldName);
                }catch (InvalidFieldNameException e) {
                    System.out.println("Serious inconsistency error in DBase setCutCopyPasteStatus(): 1");
                    return;
                }
*/
                if (activeDBTable.table.riskyGetCell(fld.getFieldIndex(), activeDBTable.table.getActiveRecord()) != null) {
                    if (!cutAction.isEnabled()) {
                        if (activeDBTable.table.isDataChangeAllowed())
                            cutAction.setEnabled(true);
                        copyAction.setEnabled(true);
                    }
                }else{
                    if (cutAction.isEnabled()) {
                        cutAction.setEnabled(false);
                        copyAction.setEnabled(false);
                    }
                }
                if (activeDBTable.table.isDataChangeAllowed()) {
//1                    if (!isClipboadEmpty())
//1                        pasteAction.setEnabled(true);
//1                    else
//1                        pasteAction.setEnabled(false);
                }else
                    pasteAction.setEnabled(false);
            }else{
                if (cutAction.isEnabled()) {
                    cutAction.setEnabled(false);
                    copyAction.setEnabled(false);
                    pasteAction.setEnabled(false);
                }
            }
        }
    }

//1    protected boolean isClipboadEmpty() {
//1        return (ESlateClipboard.getContents() == null && (isWindowsPlatform && !ClipboardFunctions.existImageInClipboard()));
//1    }

    protected void setSortButtonStatus() {
        /* Don't check the status if the standard toolbar is not visible.
         */
        if (standardToolbarController.getToolbar() == null)
            return;
        if (!standardToolbarController.getToolbar().isVisible())
            return;

        if (activeDBTable == null) {
            sortAscAction.setEnabled(false);
            sortDescAction.setEnabled(false);
            findAction.setEnabled(false);
            return;
        }

        int numOfSelectedColumns;
        if (!activeDBTable.isSimultaneousFieldRecordActivation()) {
           /* No column selection is allowed, so "numOfSelectedColumns" = 0.
            * However, when a field header is clicked, we explicitly set "columnSelectionAllowed"
            * to "true", so that the field will be selected. In this case calculate the selected
            * columns.
            */
           if (activeDBTable.jTable.getColumnSelectionAllowed())
               numOfSelectedColumns = activeDBTable.jTable.getSelectedColumnCount();
           else
               numOfSelectedColumns = 0;
        }else
           numOfSelectedColumns = activeDBTable.jTable.getSelectedColumnCount();

        if (numOfSelectedColumns > 0) {
            if (activeDBTable.table.isFieldRemovalAllowed())
                removeFieldAction.setEnabled(true);
            else{
                removeFieldAction.setEnabled(false);
            }
        }else{
            removeFieldAction.setEnabled(false);
        }

        if (numOfSelectedColumns == 1)
            fieldPropertiesAction.setEnabled(activeDBTable.table.isFieldPropertyEditingAllowed());
        else
            fieldPropertiesAction.setEnabled(false);

        if (numOfSelectedColumns < 1 || numOfSelectedColumns > 3) {
            if (!sortDescAction.isEnabled()) return;
            sortDescAction.setEnabled(false);
            sortAscAction.setEnabled(false);
        }else{
            if (sortDescAction.isEnabled()) return;
            sortDescAction.setEnabled(true);
            sortAscAction.setEnabled(true);
        }
    }


    /* This method restores the locked Database UI, after succesfull addition
     * of a new record. The Database UI is locked when a new record in a keyed
     * jTable is added.
     */
/*1    protected void unlockUI() {
        menu.databaseMenu.setEnabled(true);
        menu.tableMenu.setEnabled(true);
        menu.fieldMenu.setEnabled(true);
        menu.recordMenu.setEnabled(true);
        newDBAction.setEnabled(true);
        openDBAction.setEnabled(true);
        addFieldAction.setEnabled(true);
        findAction.setEnabled(true);
        if (findDialog != null && !findDialog.findWhatFieldEmpty) {
            findPrevAction.setEnabled(true);
            findNextAction.setEnabled(true);
        }
        int numOfSelectedColumns;
        if (!activeDBTable.isSimultaneousFieldRecordActivation()) {
1*/            /* No column selection is allowed, so "numOfSelectedColumns" = 0.
             * However, when a field header is clicked, we explicitly set "columnSelectionAllowed"
             * to "true", so that the field will be selected. In this case calculate the selected
             * columns.
             */
/*1            if (activeDBTable.jTable.getColumnSelectionAllowed())
                numOfSelectedColumns = activeDBTable.jTable.getSelectedColumnCount();
            else
                numOfSelectedColumns = 0;
        }else
            numOfSelectedColumns = activeDBTable.jTable.getSelectedColumnCount();

        if (numOfSelectedColumns > 0) {
            if (activeDBTable.table.isFieldRemovalAllowed())
                removeFieldAction.setEnabled(true);
        }

        if (activeDBTable.table.getSelectedRecordCount() > 0) {
            promoteSelRecsAction.setEnabled(true);
            clearRecAction.setEnabled(true);
            removeRecordAction.setEnabled(true);
        }
        if (activeDBTable.table.getSelectedRecordCount() < activeDBTable.table.getRecordCount())
            selectAllRecsAction.setEnabled(true);
        invertRecAction.setEnabled(true);
        addRecordAction.setEnabled(true);
        removeRecordAction.setEnabled(true);
        saveDBAction.setEnabled(true);
        tableRemoveAction.setEnabled(true);
        newTableAction.setEnabled(true);
        propertiesAction.setEnabled(true);
        tableAddAction.setEnabled(true);
        tableAutoAction.setEnabled(true);
        userLevelAction.setEnabled(true);

        closeAction.setEnabled(true);
        exportTableAction.setEnabled(true);
        importTableAction.setEnabled(true);
        renameAction.setEnabled(true);
        descriptionAction.setEnabled(true);
        saveAsAction.setEnabled(true);
    }
1*/
/*1    public void updateUI() {
        super.updateUI();
        if (emptyDB != null && emptyDB.getParent() == null)
            SwingUtilities.updateComponentTreeUI(emptyDB);
        if (emptyPanel != null && emptyPanel.getParent() == null)
            SwingUtilities.updateComponentTreeUI(emptyPanel);
        if (tabPanel != null && tabPanel.getParent() == null)
            SwingUtilities.updateComponentTreeUI(tabPanel);
        if (menu != null && menu.getParent() == null)
            SwingUtilities.updateComponentTreeUI(menu);
        if (dbIsLoadingPanel != null && dbIsLoadingPanel.getParent() == null)
            SwingUtilities.updateComponentTreeUI(dbIsLoadingPanel);
        if (statusToolbarController!= null)
            if (statusToolbarController.getToolbar() != null)
                SwingUtilities.updateComponentTreeUI(statusToolbarController.getToolbar());
        if (standardToolbarController!= null)
            if (standardToolbarController.getToolbar() != null)
                SwingUtilities.updateComponentTreeUI(standardToolbarController.getToolbar());
        if (formattingToolbarController!= null)
            if (formattingToolbarController.getToolbar() != null)
                SwingUtilities.updateComponentTreeUI(formattingToolbarController.getToolbar());
        if (toolBarMenu != null)
            SwingUtilities.updateComponentTreeUI(toolBarMenu);
        if (colorPopupMenu != null)
            SwingUtilities.updateComponentTreeUI(colorPopupMenu);
        if (gridPopupMenu != null)
            SwingUtilities.updateComponentTreeUI(gridPopupMenu);
        if (visiblePopupMenu != null)
            SwingUtilities.updateComponentTreeUI(visiblePopupMenu);
        if (menu != null) {
            if (menu.miTableHidden != null)
                menu.miTableHidden.updateUI();
            if (menu.miTableAuto != null)
                menu.miTableAuto.updateUI();
            if (menu.ci1 != null)
                menu.ci1.updateUI();
            if (menu.ci2 != null)
                menu.ci2.updateUI();
            if (menu.ci5 != null)
                menu.ci5.updateUI();
        }
        if (miPrevDB1 != null)
            miPrevDB1.updateUI();
        if (miPrevDB2 != null)
            miPrevDB2.updateUI();
        if (miPrevDB3 != null)
            miPrevDB3.updateUI();
        if (miPrevDB4 != null)
            miPrevDB4.updateUI();
    }
1*/

    FindDialog getFindDialog() {
        if (findDialog == null) {
            findDialog = new FindDialog(topLevelFrame, activeDBTable);
        }
        return findDialog;
    }

    public ESlateFileDialog getFileDialog() {
        if (fileDialog == null) {
            if (db != null && db.getFileDialog() != null)
                fileDialog = db.getFileDialog();
            else{
                fileDialog = new ESlateFileDialog(topLevelFrame);
                if (db != null)
                    db.setFileDialog(fileDialog);
            }
        }
        return fileDialog;
    }

    /** This method creates and add a PerformanceListener to the E-Slate's PerformanceManager.
     *  The PerformanceListener attaches the component's timers, when the PerformanceManager
     *  becomes enabled.
     */
    private void createPerformanceManagerListener(PerformanceManager pm) {
        if (perfListener != null) return;
        perfListener = new PerformanceAdapter() {
            public void performanceManagerStateChanged(java.beans.PropertyChangeEvent e) {
                boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
                /* Whenever the PerformanceManager is enabled, try to attach the timers */
                if (enabled)
                    attachTimers();
            }
        };
        pm.addPerformanceListener(perfListener);
    }

    /**
     * This method creates and attaches the component's timers. The timers are
     * created only once and are assigned to global variables. If the timers
     * have been already created, they are not re-created. If the timers have
     * been already attached, they are not attached again.
     * This method does not create any timers while the PerformanceManager is
     * disabled.
     */
    private void attachTimers() {
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
        // If the timers have already been constructed and attached, there is
        // nothing to do.
        if (timersCreated) {
            return;
        }
        // Get the default global performance timer groups for:
        // 1. Component construction
        // 2. Construction of the E-Slate aspect of the components
        // 3. Component save
        // 4. Component restore.
        // Special case: E-Slate aspect initialization timer. This timer is
        // created individually, because it has to be added to the default global
        // group INIT_ESLATE_ASPECT first and then to the performance timer group
        // of the component. It is the only way to get a measurement by this
        // timer: if the timer is created as a child of the performance timer
        // group of the component, then the component's E-Slate handle (and
        // therefore its E-Slate aspect) will have been created before the timer,
        // making it impossible to measure the time required for the
        // initialization of the component's E-Slate aspect using this timer.

      // Get the performance timer group for this component.
      PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);
      // Construct and attach the component's timers.
      constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, infoBundle.getString("ConstructorTimer"), true
      );
      loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, infoBundle.getString("LoadTimer"), true
      );
      saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, infoBundle.getString("SaveTimer"), true
      );
      initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, infoBundle.getString("InitESlateAspectTimer"), true
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

    /** Enables or disables the use of the 'star-like' button on the upper-left corner of
     *  each jTable in the Database, to change the expansion status of the jTable's header.
     */
    public void setTableHeaderExpansionChangeAllowed(boolean allowed) {
        if (tableHeaderExpansionChangeAllowed == allowed) return;
        tableHeaderExpansionChangeAllowed = allowed;
        for (int i=0; i<dbTables.size(); i++)
            ((DBTable) dbTables.get(i)).setTableHeaderExpansionChangeAllowed(tableHeaderExpansionChangeAllowed);
    }

    /** Reports if the expansion status of the header of every jTable in the Database can
     *  be changed.
     */
    public boolean isTableHeaderExpansionChangeAllowed() {
        return tableHeaderExpansionChangeAllowed;
    }


    /** Returns the icon used in the headers of fields of String data type in the dbTables. */
    public static final ImageIcon getStringTypeIcon() {
        if (stringTypeIcon == null)
            stringTypeIcon = new ImageIcon(Database.class.getResource("images/string.gif"));
        return stringTypeIcon;
    }
    /** Returns the icon used in the headers of fields of String data type, which are also part of the table key,
     *  in the dbTables.
     */
    public static final ImageIcon getStringTypeKeyIcon() {
        if (stringTypeKeyIcon == null)
            stringTypeKeyIcon = new ImageIcon(Database.class.getResource("images/keyString.gif"));
        return stringTypeKeyIcon;
    }
    /** Returns the icon used in the headers of the fields of Integer data type in the dbTables. */
    public static final ImageIcon getIntegerTypeIcon() {
        if (integerTypeIcon == null)
            integerTypeIcon = new ImageIcon(Database.class.getResource("images/integer.gif"));
        return integerTypeIcon;
    }
    /** Returns the icon used in the headers of fields of Integer data type, which are also part of the table key,
     *  in the dbTables.
     */
    public static final ImageIcon getIntegerTypeKeyIcon() {
        if (integerTypeKeyIcon == null)
            integerTypeKeyIcon = new ImageIcon(Database.class.getResource("images/keyInteger.gif"));
        return integerTypeKeyIcon;
    }

    /** Returns the icon used in the headers of the fields of Float data type in the dbTables. */
    public static final ImageIcon getFloatTypeIcon() {
        if (floatTypeIcon == null)
            floatTypeIcon = new ImageIcon(Database.class.getResource("images/float.gif"));
        return floatTypeIcon;
    }
    /** Returns the icon used in the headers of fields of Float data type, which are also part of the table key,
     *  in the dbTables.
     */
    public static final ImageIcon getFloatTypeKeyIcon() {
        if (floatTypeKeyIcon == null)
            floatTypeKeyIcon = new ImageIcon(Database.class.getResource("images/keyFloat.gif"));
        return floatTypeKeyIcon;
    }

    /** Returns the icon used in the headers of the fields of URL data type in the dbTables. */
    public static final ImageIcon getURLTypeIcon() {
        if (urlTypeIcon == null)
            urlTypeIcon = new ImageIcon(Database.class.getResource("images/url.gif"));
        return urlTypeIcon;
    }
    /** Returns the icon used in the headers of fields of URL data type, which are also part of the table key,
     *  in the dbTables.
     */
    public static final ImageIcon getURLTypeKeyIcon() {
        if (urlTypeKeyIcon == null)
            urlTypeKeyIcon = new ImageIcon(Database.class.getResource("images/keyUrl.gif"));
        return urlTypeKeyIcon;
    }
    /** Returns the icon used in the headers of the fields of Date data type in the dbTables. */
    public static final ImageIcon getDateTypeIcon() {
        if (dateTypeIcon == null)
            dateTypeIcon = new ImageIcon(Database.class.getResource("images/date.gif"));
        return dateTypeIcon;
    }
    /** Returns the icon used in the headers of fields of Date data type, which are also part of the table key,
     *  in the dbTables.
     */
    public static final ImageIcon getDateTypeKeyIcon() {
        if (dateTypeKeyIcon == null)
            dateTypeKeyIcon = new ImageIcon(Database.class.getResource("images/keyDate.gif"));
        return dateTypeKeyIcon;
    }
    /** Returns the icon used in the headers of fields of Time data type in the dbTables. */
    public static final ImageIcon getTimeTypeIcon() {
        if (timeTypeIcon == null)
            timeTypeIcon = new ImageIcon(Database.class.getResource("images/time.gif"));
        return timeTypeIcon;
    }
    /** Returns the icon used in the headers of fields of Time data type, which are also part of the table key,
     *  in the dbTables.
     */
    public static final ImageIcon getTimeTypeKeyIcon() {
        if (timeTypeKeyIcon == null)
            timeTypeKeyIcon = new ImageIcon(Database.class.getResource("images/keyTime.gif"));
        return timeTypeKeyIcon;
    }
    /** Returns the icon used in the headers of fields of Image data type in the dbTables. */
    public static final ImageIcon getImageTypeIcon() {
        if (imageTypeIcon == null)
            imageTypeIcon = new ImageIcon(Database.class.getResource("images/image.gif"));
        return imageTypeIcon;
    }
    /** Returns the icon used in the headers of fields of Image data type, which are also part of the table key,
     *  in the dbTables.
     */
    public static final ImageIcon getImageTypeKeyIcon() {
        if (imageTypeKeyIcon == null)
            imageTypeKeyIcon = new ImageIcon(Database.class.getResource("images/keyImage.gif"));
        return imageTypeKeyIcon;
    }
    /** Returns the icon used in the headers of fields of Boolean data type in the dbTables. */
    public static final ImageIcon getBooleanTypeIcon() {
        if (booleanTypeIcon == null)
            booleanTypeIcon = new ImageIcon(Database.class.getResource("images/boolean.gif"));
        return booleanTypeIcon;
    }
    /** Returns the icon used in the headers of fields of Boolean data type, which are also part of the table key,
     *  in the dbTables.
     */
    public static final ImageIcon getBooleanTypeKeyIcon() {
        if (booleanTypeKeyIcon == null)
            booleanTypeKeyIcon = new ImageIcon(Database.class.getResource("images/keyBoolean.gif"));
        return booleanTypeKeyIcon;
    }
    /** Returns the icon used in the headers of fields of Double data type in the dbTables. */
    public static final ImageIcon getDoubleTypeIcon() {
        if (doubleTypeIcon == null)
            doubleTypeIcon = new ImageIcon(Database.class.getResource("images/double2.gif"));
        return doubleTypeIcon;
    }
    /** Returns the icon used in the headers of fields of Double data type, which are also part of the table key,
     *  in the dbTables.
     */
    public static final ImageIcon getDoubleTypeKeyIcon() {
        if (doubleTypeIcon == null)
            doubleTypeIcon = new ImageIcon(Database.class.getResource("images/keyDouble.gif"));
        return doubleTypeIcon;
    }

    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //new ESlateLookAndFeel());
        } catch (Exception e) {
        }

        JFrame jfr = new JFrame("Test");
        jfr.setResizable(true);
        jfr.setSize(500, 600);

        Database f = new Database();
        jfr.getContentPane().add(f);

    	jfr.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        jfr.setVisible(true);
    }

}

class DBLoader implements Runnable {
    Database dbComponent;
    protected Object dbToLoad;

    protected DBLoader(Database dbComponent, Object dbToLoad) {
        this.dbComponent = dbComponent;
        this.dbToLoad = dbToLoad;
    }

    public void run() {
        dbComponent.menu.setMenuEnabled(false);
        dbComponent.openDB(dbToLoad);
        dbComponent.menu.setMenuEnabled(true);
    }
}
