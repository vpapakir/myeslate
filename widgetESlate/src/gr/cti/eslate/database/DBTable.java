package gr.cti.eslate.database;

import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.plugs.TablePlug;
import gr.cti.eslate.event.ColumnListener;
import gr.cti.eslate.event.ColumnMovedEvent;
import gr.cti.eslate.imageEditor.ImageEditor;
import gr.cti.eslate.imageEditor.ImageEditorDialog;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.utils.DataImportAllowedEvent;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.BoolBaseArray;
import gr.cti.typeArray.StringBaseArray;
import gr.cti.eslate.database.view.TableView;
import gr.cti.eslate.database.view.FieldView;
import gr.cti.eslate.tableModel.event.*;
import gr.cti.eslate.tableModel.event.TableModelListener;
import gr.cti.eslate.eslateLabel.ESlateLabel;
import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.eslateMenuBar.ESlatePopupMenu;
import gr.cti.eslate.eslateMenuBar.ESlateMenuItem;
import gr.cti.eslate.eslateMenuBar.ESlateMenu;
import gr.cti.eslate.eslateMenuBar.ESlateCheckMenuItem;

import com.objectspace.jgl.Array;
import com.objectspace.jgl.BinaryPredicate;
import com.objectspace.jgl.IntArray;
import com.objectspace.jgl.LessString;
import com.objectspace.jgl.GreaterString;
import com.objectspace.jgl.GreaterNumber;
import com.objectspace.jgl.LessString;
import com.objectspace.jgl.LessNumber;
import com.objectspace.jgl.Sorting;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.MathEF;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class DBTable extends JPanel implements ClipboardOwner, ESlatePart, Externalizable {
    public boolean printInputMap = false;
    // From version 2 --> 3 : tablePopup became a DBTablePopupMenu. It used to be an ESlatePopupMenu.
    //                        columnPopup became a DBTableColPopupMenu. It used to be an ESlatePopupMenu.
    // From version 3 --> 4 : AbstractDBTableColumn and all its descendads were introduced. There is one such instance
    //                        per column, they are stored in the 'tableColumns' array, which is persistent.
    public static final int FORMAT_VERSION = 4;
    static final long serialVersionUID = 8600181685030758774L;
    static ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.database.DBTableBundle", Locale.getDefault());
    private static final Color SELECTED_HEADER_BGRD_GREEN = new Color(0, 119, 175); //new Color(0, 255, 191);
    /* The background color color of the cell being edited.
    */
    static Color editedCellBackColor = new Color(40, 235, 185);
    static FindDialog findDialog = null;
    // Constants for the activeRecordDrawMode and selectedRecordDrawMode
    /** Constant used to specify that the active/selected record should be indicated only on the vertical row bar.*/
    public final static int ON_ROW_BAR_ONLY = 1;
    /** Constant used to specify that the active/selected record should be indicated only on the table.*/
    public static final int ON_TABLE_ONLY = 2;
    /** Constant used to specify that the active/selected record should be indicated only on the vertical row bar and table.*/
    public static final int ON_ROW_BAR_AND_TABLE = 3;
    /** Constant used to specify that there should be no ondication of the active/selected recrods in the table UI.*/
    public static final int NO_INDICATION = 4;

    JTable jTable;
    Table table;
    Database dbComponent;

    EscapeAction escapeAction;
    PageUpAction pageUpAction;
    PageDownAction pageDownAction;
    LeftAction leftAction;
    RightAction rightAction;
    UpAction upAction;
    DownAction downAction;
    ShiftUpAction shiftUpAction;
    ShiftDownAction shiftDownAction;
    HomeAction homeAction;
    EndAction endAction;
    TabAction tabAction;
    ShiftTabAction shiftTabAction;
    EnterAction enterAction;
    F2Action f2Action;
    TableRemoveRecordAction removeRecordAction = null;
    TableCutAction cutAction = null;
    TableEditableAction tableEditableAction = null;
    TableCopyAction copyAction = null;
    TablePasteAction pasteAction = null;
    TableImageEditAction imageEditAction = null;
    TableAddRecordAction addRecordAction = null;
    TableFindAction findAction = null;
    TableFindNextAction findNextAction = null;
    TableFindPrevAction findPrevAction = null;
    TableSortAction tableSortAction = null;
    TableSelectAllRecsAction selectAllRecsAction = null;
    TablePromoteSelRecsAction promoteSelRecsAction = null;
    TableFieldSelectAllAction fieldSelectAllAction = null;
    TableRemoveSelRecordAction removeSelRecordAction = null;
    FieldSortAction fieldSortAction = null;
    FieldSortAscendingAction fieldSortAscAction = null;
    FieldSortDescendingAction fieldSortDescAction = null;
    TableFieldPropertiesAction fieldPropertiesAction = null;
    TableAddFieldAction addFieldAction = null;
    TableRemoveFieldAction removeFieldAction = null;
    DBTablePreferencesAction tablePreferencesAction = null;
    DBTableInfoAction tableInfoAction = null;
    DBTableDescriptionAction tableDescriptionAction = null;
    DBTableExportToTextFileAction exportToTextFileAction = null;
    FieldCurrencyAction numericFieldCurrencyAction = null;


//    protected boolean isEditing = false;
    /* 1-1 correspondence between Arrays "tableColumns" and "tableFields". The correspondence
    * is column movement independent. This is guaranteed by Array "tableColumnsIndex", which
    * keeps the index in "tableFields" to which a column in "tableColumns" corresponds.
    */
    DBTableColumnBaseArray tableColumns = new DBTableColumnBaseArray();

//    ArrayList tableColumns = new ArrayList();
//    TableFieldBaseArray tableFields;

    DBTableModel tableModel;
//cf    ArrayList colRendererEditors = new ArrayList();

    /* Preferences variables.
    */
//    String timeFormat;
//    String dateFormat;
//    protected Font integerFont;
//    protected Font doubleFont;
//    protected Font stringFont;
//    protected Font booleanFont;
//    protected Font dateFont;
//    protected Font timeFont;
//    protected Font urlFont;

//    final static int DATE_FORMAT_POS = 0;
//    final static int TIME_FORMAT_POS = 1;
//    final static int NON_EDITABLE_HIGHLIGHTED_POS = 2;
//    final static int INTEGER_COLOR_POS = 3;
//    final static int FLOAT_COLOR_POS = 4;
//    final static int STRING_COLOR_POS = 5;
//    final static int BOOLEAN_COLOR_POS = 6;
//    final static int DATE_COLOR_POS = 7;
//    final static int TIME_COLOR_POS = 8;
//    final static int URL_COLOR_POS = 9;
//    final static int TABLE_FONT_POS = 10;
//    final static int HIGHLIGHT_COLOR_POS = 11;
//    final static int SELECTION_COLOR_POS = 12;
//    final static int GRID_COLOR_POS = 13;
//    final static int BACKGROUND_COLOR_POS = 14;
//    final static int ROW_HEIGHT_POS = 15;
//    final static int HOR_LINES_VISIBLE_POS = 16;
//    final static int VERT_LINES_VISIBLE_POS = 17;
//    final static int SIMULTANEOUS_FIELD_RECORD_SELECTION_POS = 18;
//    final static int HEADER_ICONS_VISIBLE_POS = 19;
//    final static int AUTO_RESIZE_MODE_POS = 20;
//    final static int FIELD_ORDER_POS = 21;
//    final static int ACTIVE_RECORD_COLOR_POS = 22;

    protected int pendingRecordIndex;
    JScrollPane scrollpane;

//1    protected boolean notAgain = false;
    /* The following boolean is used to avoid double request serving when the first one
    * is already processed. For example with double click on a field header, the field editing
    * dialog appears. If the user manages to double click on the field's header again before the
    * first dialog appers, then he gets two dialogs.
    */
    private boolean alreadyHandlingRequest = false;
    protected transient int mousePressedAtRow = 0;  //IE
    protected transient int mousePressedAtRowBck = -1; //IE
    protected transient int mousePressedAtColumn = 0; //IE
    /** 'dropAtColumn' and 'dropAtRow' track the cell over which the mouse was released. Currently
     *  they are used by the drag and drop mechanism.
     */
    int dropAtColumn = -1, dropAtRow = -1;
    protected transient int lastShiftClick = -1; //IE
    protected transient int firstShiftUpDownKey = -1; //IE
    private transient boolean mouseWasPressed = false;
//1    Color headerBgrColor = null;
    protected transient Color selectedHeaderBackgroundColor;  //IE
    protected transient boolean isTableInitializing = false;  //IE

    // The menu items of the table's and the columns' pop-up menus
    private ESlateCheckMenuItem tableEditableItem = null;
//    private JMenuItem cutMItem;
//    private JMenuItem copyMItem;
//    private JMenuItem pasteMItem;
//    private JMenuItem iconEditMItem, findMItem, findNextMItem, findPrevMItem, promoteSelRecsMItem;
//    private JMenuItem removeRecMItem, removeSelRecMItem, addRecMItem, fieldNewMItem2;
//    private JMenuItem fieldPropsMItem, fieldNewMItem, fieldRemoveMItem, fieldSortAscMItem, fieldSortDescMItem;
//    private JMenuItem tablePropertiesMItem, tableDescriptionMItem, tablePreferencesMItem, gridChooserMenu;
    DBTablePopupMenu tablePopup = null;
    DBTableColPopupMenu columnPopup = null;

    /* FileDialog for image fields.
    */
    protected transient ESlateFileDialog iconFileDialog = null; //= new FileDialog(new JFrame());

    /* For record selection events, which originate from record selection in
    * the jTable, not to iterate.
    */
    boolean iterateEvent = true;

    /* The upper and lower corner panels of the verical row bar
    */
    UpperCornerPanel ucp;
    LowerCornerPanel lcp;
    VerticalRowBar rowBar;
    int activeRow = -1;
    protected ArrayList colSelectionStatus = new ArrayList();
    /* This variable is set when VK_ESCAPE is pressed during record insertion. This way
    * the obsolete(cause it's been removed) rejected record will not be evaluated in
    * activeRecordChanged() of the DatabaseAdapter of the Database.
    */
//1    protected boolean recordInsertionCancelled = false;

    /* This variable exists just to avoid execution of the code in the columnSelectionChanged()
    * which is by default called whenever the user clicks inside the jTable.
    */
    int activeColumn = -1;

    MouseInputListener tableHeaderMouseListener = null;
    TableColumnModelListener tableColumnModelListener = null;
//    MouseListener tableMouseListener = null;
    CellEditorListener tableCellEditorListener = null;
    TableModelListener tableModelListener = null;
//1    TableViewStructure viewStructure = null;
    boolean scrollToActiveRecord = true;
    /** The single FileDialog instance used by the DBTable. If the DBTable is hosted in a Database component, it
     *  uses the Database's file dialog. It uses its own file dialog, only when it is not hosted in a Database.
     */
    ESlateFileDialog fileDialog = null;
    /** This Dimension object is used to store temporal values when calling getSize(). */
    Dimension tempDim = new Dimension();
    /** The GridChooser panel, through which the type of the DBTable's grid is selected.*/
    GridChooser gridChooser = null;
    /** The ExplicitLayout constraints for 'scrollpane', the unique child of the DBTable. */
    ExplicitConstraints scrollpaneCons = null;
    /** The layout of the DBTable. */
    ExplicitLayout expLayout = null;
    /** The utility that shows tips, on top of the DBTable. */
    TipManager tipManager = new TipManager(this);
    /** The manager used to find tokens in the Table. */
    SearchManager searchManager = null;
    /** The visual order of the columns of the DBTable. */
    private IntBaseArray columnOrder = null;
    /** The array with state of all the AbstractDBTableColumns of the DBTable. This array is read in readExternal(),
     * but cannot be used until the DBTable is connected back to its Table. So this variable stores this state, until
     * it is re-applied.
     */
    private ESlateFieldMap2[] columnState = null;
    /** A HashMap with the FieldViewProperties of all the columns of the DBTable. This HashMap is filled when
     *  a DBTable is saved and it's contents are used only when the DBTable is re-created.
     */
    HashMap fieldViewPropertiesMap = null;
    // Reports if the DBTable has been modified.
    private boolean modified = false;
    /** The ESlateHandle of the DBTable. */
    ESlateHandle handle = null;
    /** The plug with which the DBTable is connected to Tables. */
    FemaleSingleIFSingleConnectionProtocolPlug tableImportPlug = null;
    /** Timers which measure functions of the Table */
    PerformanceTimer loadTimer, saveTimer, constructorTimer, initESlateAspectTimer;
    /* The listener that notifies about changes to the state of the PerformanceManager */
    PerformanceListener perfListener = null;

    // PROPERTIES
    /** Determines if the header of the DBTable can be expanded to show the types of the fields. */
    boolean tableHeaderExpansionChangeAllowed = true;
    /** Determines if the sorting form a column's header is enabled. */
    boolean sortFromColumnHeadersEnabled = true;
    /** Determines if the menu which pops up when the JTable is right-clicked is enabled. If not enabled,
     *  the pop-up will not be displayed.
     */
    boolean tablePopupEnabled = true;
    /** Determines if the menu which pops up when the header of a column is right-clicked is enabled. If not enabled,
     *  the pop-up will not be displayed.
     */
    boolean columnPopupEnabled = true;
    /** The font of the headers of the columns which are not calculated. */
    Font headerFont = new Font("TimesRoman", Font.PLAIN, 16);
    /** The font of the headers of the columns which are calculated. */
    Font calculatedFieldHeaderFont = new Font("Courier", Font.PLAIN, 16);
    /** The foreground of the headers of the columns which are calculated. */
    Color calculatedFieldHeaderForeground = new Color(244,9,123);
    /** The foreground of the headers of the columns which are not calculated. */
    Color headerForeground = Color.black;
    /** The background of the headers of the columns of the DBTable. */
    Color headerBackground = UIManager.getColor("control");
    /** The bright color used by the DBTable's VerticalRowBar. */
    Color verticalRowBarBrightColor = UIManager.getColor("controlLtHighlight");
    /** The background color used by the DBTable's VerticalRowBar. */
    Color verticalRowBarColor = UIManager.getColor("control");
    /** The dark color used by the DBTable's VerticalRowBar. */
    Color verticalRowBarDarkColor = UIManager.getColor("controlShadow"); //new Color(235, 235, 235);
    /** Detemines if the non-editable fields are highlighted */
    boolean nonEditableFieldsHighlighted = false;
    /** The foreground Color for fields of Integer data type. */
    Color integerColor = UIManager.getColor("textText");
    /** The foreground Color for fields of Double data type. */
    Color doubleColor = integerColor;
    /** The foreground Color for fields of Float data type. */
    Color floatColor = integerColor;
    /** The foreground Color for fields of Boolean data type. */
    Color booleanColor = integerColor;
    /** The foreground Color for fields of String data type. */
    Color stringColor = integerColor;
    /** The foreground Color for fields of Date data type. */
    Color dateColor = integerColor;
    /** The foreground Color for fields of Time data type. */
    Color timeColor = integerColor;
    /** The foreground Color for fields of URL data type. */
    Color urlColor = integerColor;
    /** The font of the cells of all the DBTable. */
    Font tableFont = UIManager.getFont("Table.font");
    /** The color used to highlight the non-editable fields. */
    Color highlightColor = Color.yellow;
    /** The background color of the selected records. */
    Color selectionBackground = new Color(0,0,128);
    /** The foreground color of the selected records. */
    Color selectionForeground = Color.white;
    /** The color of the active record of the DBTable.*/
    Color activeRecordColor = Color.yellow;
    /** The color of the DBTable's grid. */
    Color gridColor = Color.black;
    /** The background color of the cells of the DBTable. */
    Color backgroundColor = Color.white;
    /** The height of the records in the DBTable.*/
    int rowHeight = 20;
    /** The visibility of the horizontal lines.*/
    boolean horizontalLinesVisible = true;
    /** The visibility of the vertical lines.*/
    boolean verticalLinesVisible = true;
    /** Whether both the field and the record of the active cell will be activated */
    boolean simultaneousFieldRecordActivation = false;
    /** Determines if the data type icons of the column header are visible, i.e. the headers are expanded. */
    boolean headerIconsVisible = false;
    /** The auto-resize mode of the columns of the DBTable. */
    int autoResizeMode = JTable.AUTO_RESIZE_OFF;;
    /** Dictates if two colors should be used for the record background. */
    boolean twoColorBackgroundEnabled = false;
    /** The odd row background color. It is used when the background is two-colored. */
    Color oddRowColor = backgroundColor;
    /** The even row background color. It is used when the background is two-colored. */
    Color evenRowColor = backgroundColor;
    /** Enables or disables record selection from the UI of the DBTable. */
    boolean recordSelectionChangeAllowed = true;
    /** The visualization method of the active record */
    int activeRecordDrawMode = ON_ROW_BAR_AND_TABLE;
    /** The visualization method of the selected records */
    int selectedRecordDrawMode = ON_ROW_BAR_AND_TABLE;

    /** The border used to surround the active cell. */
    LineBorder activeCellBorder = new LineBorder(activeRecordColor);
    /** The inter-cell spacing */
    Dimension intercellSpacing = new Dimension(1, 1);
    // DragSource variables
//    private DragSource dragSource = null;
//    private DragGestureListener dragGestureListener = null;
//    private DragSourceListener dragSourceListener = null;
    // DropTarget variables
    private DropTarget dropTarget = null;
    private DropTargetListener dropTargetListener = null;
    private DBTableTransferHandler dbTableTransferHandler = null;
    // This variable exists just to store the added TransferHandlerLIstener, while the jTable has not yet been initialized.
    private TransferHandlerListener transferHandlerListener = null;


    public DBTable() {
        initDBTable(null, null/*1, null*/);
    }

    public DBTable(Table table, Database database/*, TableViewStructure structure*/) {
        initDBTable(table, database/*, structure*/);
    }

    private void initDBTable(Table table, Database database/*1, TableViewStructure structure*/) {
/*        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("focusOwner")) {
                    System.out.println("Focus owner changed: " + KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
                }
            }

        });
*/
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.constructionStarted(this);
        pm.init(constructorTimer);

        expLayout = new ExplicitLayout();
        setLayout(expLayout);
//        setRequestFocusEnabled(false);
        createActions();
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!tablePopupEnabled) return;
                if (!SwingUtilities.isRightMouseButton(e)) return;
                int column = -1;
                if (jTable != null)
                    column = jTable.getSelectedColumn();
                getTablePopupMenu(activeRow, column).show(DBTable.this, e.getX(), e.getY());
            }
        });
        // Focus listener that transfers the focus to the JTable, when the DBTable acquires focus.
        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (jTable != null)
                    jTable.requestFocus();
            }
        });

        dbTableTransferHandler = new DBTableTransferHandler(this);
        createJTable();
        setTransferHandler(dbTableTransferHandler);
        if (table != null) {
            isTableInitializing = true;
            setTable(table, database);
            isTableInitializing = false;
        }
        setModified(false);

        pm.constructionEnded(this);
        pm.stop(constructorTimer);
        pm.displayTime(constructorTimer, "DBTable constuctor", "ms");
    }

    /**
     * Initializes the JTable of the DBTable and its scrollpane. This is done only once, when the DBTable is constructed.
     */
    private void createJTable() {
        // Create the jTable's data model
        tableModel = new DBTableModel(this);
//        if (jTable != null)
//            jTable.setTransferHandler(null);
        /* Create the jTable.
         */
        jTable = new CustomJTable();
//        jTable.setUI(new MyTableUI());
        jTable.setAutoCreateColumnsFromModel(false);
        jTable.setModel(tableModel);
        jTable.setDragEnabled(true);
        jTable.setTransferHandler(dbTableTransferHandler);
        jTable.getTableHeader().setTransferHandler(jTable.getTransferHandler());
        initializeJTableDropTarget();
//        setDropTarget(dropTarget);
        jTable.setDropTarget(dropTarget);
        // If a TransferHandlerListener was added to the DBTable before the jTable was initialized, then now that
        // the jTable is initialized add the listener to it.
        if (transferHandlerListener != null) {
            try{
                ((DBTableTransferHandler) jTable.getTransferHandler()).addTransferHandlerListener(transferHandlerListener);
                transferHandlerListener = null;
            }catch (TooManyListenersException thr) {}
        }

        scrollpane = new JScrollPane(jTable);
        setAutoscrolls(false);
        jTable.setAutoscrolls(true);
        scrollpane.setRequestFocusEnabled(false);
        scrollpane.setFocusable(false);
        scrollpane.setBorder(null);
        scrollpaneCons = new ExplicitConstraints(scrollpane);
        scrollpaneCons.setX(ContainerEF.left(this));
        scrollpaneCons.setY(ContainerEF.top(this));
        scrollpaneCons.setWidth(ContainerEF.width(this));
        scrollpaneCons.setHeight(ContainerEF.height(this));
        add(scrollpane, scrollpaneCons);
//        add(scrollpane, BorderLayout.CENTER);

        scrollpane.setRowHeaderView(rowBar = new VerticalRowBar(this, scrollpane, jTable));
        scrollpane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
            public void adjustmentValueChanged(AdjustmentEvent e) {
//                scrollpane.repaint();
                rowBar.repaint();
            }
        });

        scrollpane.setCorner(scrollpane.UPPER_LEFT_CORNER, ucp= new UpperCornerPanel(this));
        scrollpane.setCorner(scrollpane.LOWER_LEFT_CORNER, lcp = new LowerCornerPanel(this));

        jTable.setBorder(null);
        jTable.setDefaultEditor(java.lang.String.class, null);
        jTable.setDefaultEditor(java.lang.Double.class, null);
        jTable.setDefaultEditor(java.lang.Number.class, null);
        jTable.setDefaultEditor(java.lang.Boolean.class, null);
        jTable.setDefaultEditor(java.lang.Object.class, null);

        registerTableKeyboardActions();
    }

    private void initializeJTableDropTarget() {
        if (dropTargetListener == null) {
            dropTargetListener = new DropTargetListener() {
                public void dragEnter(DropTargetDragEvent e) {
                    if (!((DBTableTransferHandler) jTable.getTransferHandler()).canImport(jTable, e.getCurrentDataFlavors(), e.getLocation()))
                        e.rejectDrag();
    //                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                }

                public void dragOver(DropTargetDragEvent e) {
//System.out.println("dragOver()");
                    if (!((DBTableTransferHandler) jTable.getTransferHandler()).canImport(jTable, e.getCurrentDataFlavors(), e.getLocation()))
                        e.rejectDrag();
//                    if (!isDragOK(e.getLocation()))
//                        e.rejectDrag();
                }

                public void dropActionChanged(DropTargetDragEvent e) {
                }

                public void dragExit(DropTargetEvent e) {
                }

                public void drop(DropTargetDropEvent e) {
//System.out.println("component: " + e.getDropTargetContext().getComponent());
                    dropAtColumn = jTable.columnAtPoint(e.getLocation());
                    dropAtRow = jTable.rowAtPoint(e.getLocation());
//                    if (isDragOK(dropAtRow, dropAtColumn)) {
                        if (((DBTableTransferHandler) jTable.getTransferHandler()).importData(jTable, e.getTransferable(), e.getLocation()))
                            e.acceptDrop(e.getDropAction());
//                    }
                    e.dropComplete(true);
                }

/*                private boolean isDragOK(Point p) {
                    if (!DBTable.this.table.isDataChangeAllowed())
                        return false;
                    int column = jTable.columnAtPoint(p);
                    int row = jTable.rowAtPoint(p);
                    return isDragOK(row, column);
                }

                private boolean isDragOK(int row, int column) {
//System.out.println("isDragOK() row: " + row + ", column: " + column);
                    if (column == -1 || row == -1)
                        return false;
                    AbstractTableField field = getTableField(column);
                    if (field == null)
                        return false;
                    return field.isEditable();
                }
*/
            };
        }
        dropTarget = new DropTarget(jTable, dropTargetListener);
    }

    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);

            handle = ESlate.registerPart(this);
            handle.addESlateListener(new ESlateAdapter() {
                public void handleDisposed(HandleDisposalEvent e) {
                    PerformanceManager pm = PerformanceManager.getPerformanceManager();
                    pm.removePerformanceListener(perfListener);
                    perfListener = null;
                }
            });
            try{
                handle.setUniqueComponentName(bundle.getString("ComponentName"));
            }catch (RenamingForbiddenException exc) {exc.printStackTrace();}

            if (dbComponent == null) {
                /* The DBTable's Table single input plug, through which one Table can be connected to this DBTable.
                 * This plug is created only if the DBTable is outside a Database. When it is inside a Database, then
                 * its Table is given to it by the Database and this Table belongs to the DBase of the Database.
                 */
                try {
                    tableImportPlug = new FemaleSingleIFSingleConnectionProtocolPlug(
                                                handle,
                                                bundle,
                                                "ImportTable",
                                                TablePlug.TABLE_PLUG_COLOR,
                                                Table.class);
                    tableImportPlug.setHostingPlug(true);
                    handle.addPlug(tableImportPlug);
                    tableImportPlug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            Table remoteTable = (Table) e.getPlug().getHandle().getComponent();
                            if (remoteTable == null) return;
                            setTable(remoteTable, dbComponent);
                        }
                    });
                    tableImportPlug.addDisconnectionListener(new DisconnectionListener() {
                        public void handleDisconnectionEvent(DisconnectionEvent e) {
                            removeTable();
                        }
                    });

                } catch (InvalidPlugParametersException e) {
                    ESlateOptionPane.showMessageDialog(this, e.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                } catch (PlugExistsException e) {
                    ESlateOptionPane.showMessageDialog(this, e.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }

            pm.eSlateAspectInitEnded(this);
            pm.stop(initESlateAspectTimer);
            pm.displayTime(initESlateAspectTimer, handle, "", "ms");
        }
        return handle;
    }

    private void initTableModelListener() {
        if (table == null) return;
        if (tableModelListener == null) {
            tableModelListener = new DatabaseTableModelAdapter() {
                public void cellValueChanged(CellValueChangedEvent event) {
                    int row = table.rowForRecord(event.getRecordIndex());
                    if (event.affectsOtherCells())
                        refreshRow(row, (activeRow == row));
                    else{
                        int colIndex = jTable.convertColumnIndexToView(tableModel.getColumnIndex(event.getColumnName()));
                        jTable.repaint(jTable.getCellRect(row, colIndex, true));
                    }

                }
                public void columnAdded(ColumnAddedEvent e) {
                    AbstractTableField field = null;
                    try{
                        field = table.getTableField(e.getColumnIndex());
                    }catch (Exception exc) {
                        return;
                    }
                    if (field == null)
                        return;
                    UIAddField(field);
                }
                public void columnRenamed(ColumnRenamedEvent e) {
                    String newFieldName = e.getNewName();
                    String oldFieldName = e.getOldName();
                    UIRenameField(newFieldName, oldFieldName);
                }
                public void columnTypeChanged(ColumnTypeChangedEvent e) {
                    Table table = (Table) e.getSource();
                    // Get again the TableFields, cause when the type of a field changes, what actually happens is that
                    // the field whose data type changed was replaced by a new field of the new data type.
    //                tableFields = table.getFields();
                    String fieldName = e.getColumnName();
                    AbstractTableField field;
                    try{
                        field = table.getTableField(fieldName);
                    }catch (InvalidFieldNameException exc) {return;}

                    Class prevFieldtype = null;
                    try{
                        prevFieldtype = Class.forName(e.getPrevType());
                    }catch (Exception exc) {
                        return;
                    }
                    String previousDataType = AbstractTableField.getInternalDataTypeName(prevFieldtype);
                    String newFieldType = AbstractTableField.getInternalDataTypeName(field);

                    UIChangeFieldType(field, previousDataType);
                }
                public void columnKeyChanged(ColumnKeyChangedEvent e) {
                    String fieldName = e.getColumnName();
                    UIChangeKey(fieldName);
                }
                public void columnRemoved(ColumnRemovedEvent e) {
                    String fieldName = e.getColumnName();
                    UIRemoveColumn(fieldName, e.getColumnIndex());
                }
                public void calcColumnReset(CalcColumnResetEvent e) {
                    String fieldName = e.getColumnName();
                    UISwitchCalcFieldToNormal(fieldName);
                }
                public void calcColumnFormulaChanged(CalcColumnFormulaChangedEvent e) {
                    String fieldName = e.getColumnName();
                    String prevDataType = e.getPreviousDataType();
                    UIChangeCalcFieldFormula(fieldName, prevDataType);
                }
                public void columnEditableStateChanged(ColumnEditableStateChangedEvent e) {
                    String fieldName = e.getColumnName();
                    UISetFieldEditable(fieldName);
                }
                public void columnHiddenStateChanged(ColumnHiddenStateChangedEvent e) {
                    String fieldName = e.getColumnName();
                    UISetFieldHidden(fieldName);
                }
                public void recordAdded(RecordAddedEvent e) {
                    if (e.moreToBeAdded()) return;
                    acceptNewRecord();
                }
                public void recordRemoved(RecordRemovedEvent e) {
                    int rowIndex = e.getRowIndex();
                    if (rowIndex == -1) {
                        int recordIndex = e.getRecordIndex();
                        for (int i=0; i<table.recordIndex.size(); i++) {
                            if (table.recordIndex.get(i) == recordIndex) {
                                rowIndex = i;
                                break;
                            }
                        }
                    }
    //System.out.println("recordRemoved() UIRemoveRecord(" + rowIndex + ") e.isChanging(): " + e.isChanging());
                    UIRemoveRecord(rowIndex, e.isChanging());
                }
                public void activeRecordChanged(ActiveRecordChangedEvent e) {
                    int previousActiveRecord = e.getPreviousActiveRecord();
                    int activeRecord = e.getActiveRecord();

                    RecordEntryStructure res = table.getRecordEntryStructure();
                    if (res.isRecordAdditionPending()) {
                        if (activeRecord != -1 && res.getPendingRecordIndex() != activeRecord) {
                            if (!commitNewRecord())
                                return;
                        }
                    }else{
                        stopCellEditing();
                        if (previousActiveRecord != -1) {
                            refreshRow(table.rowForRecord(previousActiveRecord), false);
                        }
                        UIUpdateActiveRecord();
                    }
                }
                public void rowOrderChanged(RowOrderChangedEvent e) {
                    TableColumn col;
                    HeaderRenderer hr;
                    for (int i=0; i<tableColumns.size(); i++) {
                        col = jTable.getColumn(jTable.getColumnName(i));
                        hr = ((HeaderRenderer) col.getHeaderRenderer());
                        hr.updateFieldIcons(jTable.getColumnName(i));
                    }
                    findNextAction.startOver = false;
                    findPrevAction.startFromBottom = false;
                    activeRow = table.rowForRecord(table.getActiveRecord());
                    scrollVertically(activeRow);
                    refresh();
                }

                public void currencyFieldChanged(ColumnEvent event) {
                    String fieldName = event.getColumnName();
                    for (int i=0; i<tableColumns.size(); i++) {
                        if (tableColumns.get(i).tableField.getName().equals(fieldName)) {
                            AbstractDBTableColumn column = tableColumns.get(i);
                            if (column instanceof NumericDBTableColumn)
                                ((NumericDBTableColumn) column).setCurrency(((CurrencyField) column.tableField).getCurrency());
                        }
                    }
                }

                public void selectedRecordSetChanged(SelectedRecordSetChangedEvent e) {
                    IntBaseArray selectedSubset = table.getSelectedSubset();
                    alterRecordSelection(selectedSubset);
                    if (selectedSubset.size() > 0) {
                        promoteSelRecsAction.setEnabled(true);
                        removeSelRecordAction.setEnabled(true);
                    }else{
                        promoteSelRecsAction.setEnabled(false);
                        removeSelRecordAction.setEnabled(false);
                    }
                }
            };
        }
        table.addTableModelListener(tableModelListener);
    }

    void stopCellEditing() {
        if (!isEditing()) return;
        try{
            ((DefaultCellEditor) tableColumns.get(jTable.getEditingColumn()).tableColumn.getCellEditor()).stopCellEditing();
        }catch (Exception exc) {
//1            setEditing(false);
        }
    }

    void cancelCellEditing() {
        if (!isEditing()) return;
        try{
            ((DefaultCellEditor) tableColumns.get(jTable.getEditingColumn()).tableColumn.getCellEditor()).cancelCellEditing();
        }catch (Exception exc) {
//1            setEditing(false);
        }
    }

    FindDialog getFindDialog() {
        if (dbComponent != null)
            return dbComponent.getFindDialog();
        if (findDialog == null)
            findDialog = new FindDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class, this), this);
        return findDialog;
    }

    ESlateFileDialog getFileDialog() {
        if (dbComponent != null)
            return dbComponent.getFileDialog();
        else{
            if (fileDialog == null) {
                Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this);
                fileDialog = new ESlateFileDialog(topLevelFrame);
            }
            return fileDialog;
        }
    }

    /** Returns the GridChooser panel, which sets the type of the grid of the dbTables. The GridChooser is
     *  updated for this DBTable.
     */
    GridChooser getGridChooser() {
        if (gridChooser == null)
            gridChooser = new GridChooser(this);
        return gridChooser;
    }

    /** If the DBTable is hosted in a Database and the <code>visiblePopupMenu</code> is set and visible, then
     *  make it invisible.
     */
    void closeDatabasePopupMenu() {
        if (dbComponent == null) return;
        if (dbComponent.visiblePopupMenu != null && dbComponent.visiblePopupMenu.isVisible())
            dbComponent.visiblePopupMenu.setVisible(false);
    }

    /** Returns the rectangle in the UI of the DBTable occupied by record at <code>row</code>. The rectangle
     *  does not contain the part of the <code>rowBar</code> which belongs to the record. */
    Rectangle getRowRectangle(int row) {
        Rectangle rect = jTable.getCellRect(row, 0, true);
        rect.width = jTable.getSize(tempDim).width;
        return rect;
    }

    /** Repaints the area of the record at <code>row</code>.
      * @param row The index of the row to be repainted.
     *  @param isActive Whether this row is active or not.
     *  @return The rectangle of the row.
     *  @see #getRowRectangle(int)
     */

    Rectangle refreshRow(int row, boolean isActive) {
        Rectangle rect = getRowRectangle(row);
        jTable.repaint(rect);
        rowBar.repaint(rowBar.getRectangle(row, rect));
/*        if (!isActive)
            rowBar.resetActiveRow(row);
        else
            rowBar.drawActiveRow(rowBar.getGraphics(), false);
*/
        return rect;
    }

    void UIUpdateActiveRecord() {
//long start = System.currentTimeMillis();
        int activeRecord = table.getActiveRecord();
        int recordCount = table.getRecordCount();
        if (activeRecord != -1) {
            int activeRecordRow = table.rowForRecord(activeRecord);
//1            jTable.getSelectionModel().setSelectionInterval(activeRecordRow, activeRecordRow);
            activeRow = activeRecordRow;

            Rectangle rect = getRowRectangle(activeRow);
//            rect.width = jTable.getSize().width;
            jTable.repaint(rect);
            rect = rowBar.getRectangle(activeRow, rect);
            rowBar.repaint(rect);
//            rowBar.drawActiveRow(rowBar.getGraphics(), false);
//            rowBar.repaint();
            if (scrollToActiveRecord) {
                jTable.scrollRectToVisible(rect);

/*1              // This block scrolls the table so that the activeRecord is always in the middle.
                int newYPosition = jTable.getCellRect(activeRow, 0, true).y - (scrollpane.getViewport().getExtentSize().height/2);
                if (newYPosition < 0)
                    newYPosition = 0;

                int viewHeight = scrollpane.getViewport().getViewSize().height;
                if (newYPosition > viewHeight - scrollpane.getViewport().getExtentSize().height)
                    newYPosition = viewHeight - scrollpane.getViewport().getExtentSize().height;

                if (newYPosition >= 0 && scrollpane.getViewport().getViewPosition().y != newYPosition) {
                    scrollpane.getViewport().setViewPosition(new Point(
                            scrollpane.getViewport().getViewPosition().x,
                            newYPosition
                    ));
                }else
                    rowBar.repaint();
*/
//                refresh();

            }
            removeRecordAction.setEnabled(true);

        }else{
            rowBar.repaint(rowBar.getRectangle(activeRow, null));
//            rowBar.resetActiveRow(activeRow);
            activeRow = -1;
            jTable.getSelectionModel().clearSelection();
            removeRecordAction.setEnabled(false);
        }
//System.out.println("UIUpdateActiveRecord time: " + (System.currentTimeMillis()-start));
    }

    /** Removes the Table which is displayed in the DBTable. */
    private void removeTable() {
        if (table == null) return;
//        removeAll();
//        jTable = null;
//        dropTargetListener = null;
//        dropTarget = null;
        table.removeTableModelListener(tableModelListener);
        table = null;
        for (int i=jTable.getColumnCount()-1; i>=0; i--)
            jTable.removeColumn(jTable.getColumnModel().getColumn(i));
//        scrollpane = null;
        jTable.getColumnModel().removeColumnModelListener(getTableColumnModelListener());
        if (tableColumns != null) {
            tableColumns.clear();
        }
//        if (tableFields != null) {
//            tableFields.clear();
//        }
//\        tableModel.table = null;
//        tableModel = null;
//cf        if (colRendererEditors != null) {
//cf            colRendererEditors.clear();
//cf        }
        pendingRecordIndex = -1;
        alreadyHandlingRequest = false;
        mousePressedAtRow = 0;
        mousePressedAtRowBck = -1;
        mousePressedAtColumn = 0;
        lastShiftClick = -1;
        firstShiftUpDownKey = -1;
        mouseWasPressed = false;
        selectedHeaderBackgroundColor = null;
        isTableInitializing = false;
        iterateEvent = true;
//        ucp = null;
//        lcp = null;
//        rowBar = null;
        activeRow = -1;
        if (colSelectionStatus != null) {
            colSelectionStatus.clear();
        }
        activeColumn = -1;
        tableHeaderMouseListener = null;
        tableColumnModelListener = null;
//        tableMouseListener = null;
        tableCellEditorListener = null;
//1        viewStructure = null;
        scrollToActiveRecord = true;
//        scrollpaneCons = null;
        columnOrder = null;
        columnState = null;
        repaint();
    }

    /** This method assigns a Table to the DBTable It initializes the whole DBTable based on the input Table. */
    void setTable(Table dbaseTable, Database database/*1, TableViewStructure viewStructure*/) {
        if (table != null)
            removeTable();
        if (dbaseTable == null) {
            throw new NullPointerException();
        }
        this.table = dbaseTable;
        initTableModelListener();
        this.dbComponent = database;
        // If the DBTable is hosted in a Database, then add its handle to the Database's handle.
        if (dbComponent != null)
            dbComponent.getESlateHandle().add(getESlateHandle());
//1        if (structure == null)
//1            viewStructure = new TableViewStructure(table);
//1        else
//1            viewStructure = structure;

        // Create the jTable's data model
//        tableModel = new DBTableModel(this);
//\        tableModel.setTable(table);

        /* Create the jTable.
         */


        jTable.createDefaultColumnsFromModel();
        // The resize mode of the jTable's columns is initialized here, cause it is used in 'initializeTableColumns()'.
        jTable.setAutoResizeMode(autoResizeMode);
        initializeTableColumns(table);
        /* Initialize the recordIndex.
        */
        // Put the jTable and header into a scrollPane
/*        scrollpane = new JScrollPane(jTable);
        setAutoscrolls(false);
        jTable.setAutoscrolls(true);
        scrollpane.setRequestFocusEnabled(false);
        scrollpane.setBorder(null);
        scrollpaneCons = new ExplicitConstraints(scrollpane);
        scrollpaneCons.setX(ContainerEF.left());
        scrollpaneCons.setY(ContainerEF.top());
        scrollpaneCons.setWidth(ContainerEF.width());
        scrollpaneCons.setHeight(ContainerEF.height());
        add(scrollpane, scrollpaneCons);
//        add(scrollpane, BorderLayout.CENTER);

        scrollpane.setRowHeaderView(rowBar = new VerticalRowBar(this, scrollpane, jTable));
        scrollpane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
            public void adjustmentValueChanged(AdjustmentEvent e) {
//                scrollpane.repaint();
                rowBar.repaint();
            }
        });

        scrollpane.setCorner(scrollpane.UPPER_LEFT_CORNER, ucp= new UpperCornerPanel(this));
        scrollpane.setCorner(scrollpane.LOWER_LEFT_CORNER, lcp = new LowerCornerPanel(this));

        jTable.setBorder(null);

        //Disable default column editors
        jTable.setDefaultEditor(java.lang.String.class, null);
        jTable.setDefaultEditor(java.lang.Double.class, null);
        jTable.setDefaultEditor(java.lang.Number.class, null);
        jTable.setDefaultEditor(java.lang.Boolean.class, null);
        jTable.setDefaultEditor(java.lang.Object.class, null);
*/
        // Note:
        // You can speed up resizing repaints by turning off live cell
        // updates like this:
        jTable.getTableHeader().setUpdateTableInRealTime(false);

/* Initialize the jTable's time and date fields according to timeFormat and dateFormat
* respectively.
*/
//        dbTable.setDateFormat(dateFormat);
//        dbTable.setTimeFormat(timeFormat);

//1        jTable.setRowHeight(viewStructure.tableView.getRowHeight());
        /* Initialize the font colors of the jTable's fields.
        */
/*1        setDataTypeColors(viewStructure.tableView.getIntegerColor(),
                viewStructure.tableView.getDoubleColor(),
                viewStructure.tableView.getStringColor(),
                viewStructure.tableView.getBooleanColor(),
                viewStructure.tableView.getDateColor(),
                viewStructure.tableView.getTimeColor(),
                viewStructure.tableView.getUrlColor());
1*/        /* Initialize the background color.
        */
//1        paintBackground();
        /* Paint the non-editable columns */
//1        paintNonEditableColumns();
        /* Initialize the record/column selection color.
        */
//1        paintSelection();
        /* Initialize the active record color.
        */
//1        paintActiveRecord();
        /* Iitialize the color of the grid.
        */
//1        jTable.setGridColor(viewStructure.tableView.getGridColor());
//1        setTableFont(viewStructure.tableView.getTableFont());

        /* Set the jTable's column selection mode.
         */
/*1        jTable.setColumnSelectionAllowed(viewStructure.tableView.isSimultaneousFieldRecordSelection());
        if (jTable.getColumnSelectionAllowed())
            selectedHeaderBackgroundColor = SELECTED_HEADER_BGRD_GREEN;
        else
            selectedHeaderBackgroundColor = headerBackground;
1*/
        jTable.setRowSelectionAllowed(false);

        /* Set the auto-resize mode.
        */
/*1        jTable.setAutoResizeMode(viewStructure.tableView.getAutoResizeMode());

        if (viewStructure.tableView.isHeaderIconsVisible())
            displayHeaderIcons(true);

        if (viewStructure.tableView.isHorizontalLinesVisible())
            jTable.setShowHorizontalLines(true);
        else
            jTable.setShowHorizontalLines(false);
        if (viewStructure.tableView.isVerticalLinesVisible())
            jTable.setShowVerticalLines(true);
        else
            jTable.setShowVerticalLines(false);
1*/

//        jTable.addMouseListener(getTableMouseListener());
///        jTable.getColumnModel().addColumnModelListener(getTableColumnModelListener());
        jTable.getTableHeader().addMouseListener(getTableHeaderMouseListener());
        jTable.getTableHeader().addMouseMotionListener(getTableHeaderMouseListener());

        activeRow = table.rowForRecord(table.getActiveRecord());

        jTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        for (int m=0; m<tableColumns.size(); m++)
            colSelectionStatus.add(Boolean.FALSE);
        jTable.getColumnModel().getSelectionModel().clearSelection();
        /* If there are fields in the jTable, initialize the column selection to
        * the first column. This is force by the JTable itself, which upon intialization
        * has its first column selected.
        */
        if (table.getFieldCount() > 0) {
            colSelectionStatus.set(0, Boolean.TRUE);
            jTable.getColumnModel().getSelectionModel().setSelectionInterval(0, 0);
//            columnSelectionChangedAction();
        }

        //Initialization of JTable
        jTable.setShowHorizontalLines(horizontalLinesVisible);
        jTable.setShowVerticalLines(verticalLinesVisible);
        intercellSpacing.height = (horizontalLinesVisible)? 1:0;
        intercellSpacing.width = (verticalLinesVisible)? 1:0;
        jTable.setIntercellSpacing(intercellSpacing);
        jTable.setColumnSelectionAllowed(simultaneousFieldRecordActivation);
        jTable.setGridColor(gridColor);
        jTable.setRowHeight(rowHeight);
        boolean higlightActiveRecordInTable = (activeRecordDrawMode == ON_TABLE_ONLY || activeRecordDrawMode == ON_ROW_BAR_AND_TABLE);
        boolean higlightSelectedRecordsInTable = (selectedRecordDrawMode == ON_TABLE_ONLY || selectedRecordDrawMode == ON_ROW_BAR_AND_TABLE);
        TableColumnModel tcm = jTable.getColumnModel();
        for (int i=0; i<tcm.getColumnCount(); i++) {
            DBCellRenderer renderer = (DBCellRenderer) tcm.getColumn(i).getCellRenderer();
            renderer.setActiveRecordBackgroundDrawn(higlightActiveRecordInTable);
            renderer.setSelectedRecordsBackgroundDrawn(higlightSelectedRecordsInTable);
        }
        setDataTypeColors(integerColor, floatColor, doubleColor, stringColor, booleanColor, dateColor, timeColor, urlColor);

        if (dbComponent == null) {
            revalidate();
            doLayout();
            rowBar.repaint();
            repaint();
            jTable.requestFocus();
        }
        TableColumnModel tcm1 = jTable.getColumnModel();
        Graphics gr = null;
        if (dbComponent != null)
            gr = dbComponent.getGraphics();
        else
            gr = getGraphics();
        for (int i=0; i<tcm1.getColumnCount(); i++) {
//System.out.println("updatePreferredHeight() of " + jTable.getColumnName(i) + ", width: " + tcm1.getColumn(i).getWidth() + ", preferred width: " + tcm1.getColumn(i).getPreferredWidth());
            ((HeaderRenderer) tcm1.getColumn(i).getHeaderRenderer()).fieldNameLabel.updatePreferredHeight(tcm1.getColumn(i).getWidth(), gr);
//System.out.println("Height: " + ((HeaderRenderer) tcm1.getColumn(i).getHeaderRenderer()).fieldNameLabel.getPreferredSize().height);
            ((HeaderRenderer) tcm1.getColumn(i).getHeaderRenderer()).revalidate();
        }
        jTable.getTableHeader().invalidate();
        jTable.getTableHeader().doLayout();
        jTable.getTableHeader().revalidate();
        jTable.revalidate();
        //dbComponent.standardToolBar.updateToolBarStatus();
/*??        dbComponent.setActionStatus();
        dbComponent.setDBMenuRestStatus();
        dbComponent.setFieldMenuRestStatus();
        dbComponent.setTableMenuRestStatus();
*/
    }

    private void registerTableKeyboardActions() {
//        SwingUtilities.replaceUIActionMap(jTable, null);
//        SwingUtilities.replaceUIInputMap(jTable, WHEN_FOCUSED, null);
        ActionMap map = jTable.getActionMap();
        map.setParent(null);
        jTable.getInputMap(WHEN_FOCUSED).setParent(null);
        jTable.getInputMap(WHEN_IN_FOCUSED_WINDOW).setParent(null);
        jTable.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).setParent(null);
/*        Object[] mapKeys = map.allKeys();
        for (int i=0; mapKeys != null && i<mapKeys.length; i++)
            System.out.println("Before Action: " + mapKeys[i]);
*/
        ActionMap jTableActionMap = new ActionMap();
        jTableActionMap.put(pageUpAction.getValue(AbstractAction.NAME), pageUpAction);
        jTableActionMap.put(pageDownAction.getValue(AbstractAction.NAME), pageDownAction);
        jTableActionMap.put(upAction.getValue(AbstractAction.NAME), upAction);
        jTableActionMap.put(downAction.getValue(AbstractAction.NAME), downAction);
        jTableActionMap.put(leftAction.getValue(AbstractAction.NAME), leftAction);
        jTableActionMap.put(rightAction.getValue(AbstractAction.NAME), rightAction);
        jTableActionMap.put(shiftUpAction.getValue(AbstractAction.NAME), shiftUpAction);
        jTableActionMap.put(shiftDownAction.getValue(AbstractAction.NAME), shiftDownAction);
        jTableActionMap.put(tabAction.getValue(AbstractAction.NAME), tabAction);
        jTableActionMap.put(shiftTabAction.getValue(AbstractAction.NAME), shiftTabAction);
        jTableActionMap.put(homeAction.getValue(AbstractAction.NAME), homeAction);
        jTableActionMap.put(endAction.getValue(AbstractAction.NAME), endAction);
        jTableActionMap.put(findAction.getValue(AbstractAction.NAME), findAction);           // To check
        jTableActionMap.put(findNextAction.getValue(AbstractAction.NAME), findNextAction);   // To check
        jTableActionMap.put(findPrevAction.getValue(AbstractAction.NAME), findPrevAction);   // To check
        jTableActionMap.put(selectAllRecsAction.getValue(AbstractAction.NAME), selectAllRecsAction);
        jTableActionMap.put(promoteSelRecsAction.getValue(AbstractAction.NAME), promoteSelRecsAction);
        jTableActionMap.put(fieldSelectAllAction.getValue(AbstractAction.NAME), fieldSelectAllAction);
        jTableActionMap.put(copyAction.getValue(AbstractAction.NAME), copyAction);
        jTableActionMap.put(cutAction.getValue(AbstractAction.NAME), cutAction);
        jTableActionMap.put(pasteAction.getValue(AbstractAction.NAME), pasteAction);
        jTableActionMap.put(removeRecordAction.getValue(AbstractAction.NAME), removeRecordAction);
        jTableActionMap.put(removeSelRecordAction.getValue(AbstractAction.NAME), removeSelRecordAction);
        jTableActionMap.put(escapeAction.getValue(AbstractAction.NAME), escapeAction);
        jTableActionMap.put(enterAction.getValue(AbstractAction.NAME), enterAction);
        jTableActionMap.put(f2Action.getValue(AbstractAction.NAME), f2Action);
        jTableActionMap.put(fieldPropertiesAction.getValue(AbstractAction.NAME), fieldPropertiesAction);
        jTableActionMap.put(removeFieldAction.getValue(AbstractAction.NAME), removeFieldAction);
        jTableActionMap.put(fieldSortAscAction.getValue(AbstractAction.NAME), fieldSortAscAction);
        jTableActionMap.put(fieldSortDescAction.getValue(AbstractAction.NAME), fieldSortDescAction);

//        mapKeys = jTableActionMap.keys();
        jTable.setActionMap(jTableActionMap);
/*        map = jTable.getActionMap();
        mapKeys = map.allKeys();
        for (int i=0; mapKeys != null && i<mapKeys.length; i++)
            System.out.println("After Action: " + mapKeys[i]);
*/
        InputMap jTableInputMap = new InputMap();
        // PAGE_UP (previous screen of records action)
        jTableInputMap.put((KeyStroke) pageUpAction.getValue(AbstractAction.ACCELERATOR_KEY), pageUpAction.getValue(AbstractAction.NAME));
        // PAGE_DOWN (next screen of records action)
        jTableInputMap.put((KeyStroke) pageDownAction.getValue(AbstractAction.ACCELERATOR_KEY), pageDownAction.getValue(AbstractAction.NAME));
        // UP (previous record action)
        jTableInputMap.put((KeyStroke) upAction.getValue(AbstractAction.ACCELERATOR_KEY), upAction.getValue(AbstractAction.NAME));
        // DOWN (next record action)
        jTableInputMap.put((KeyStroke) downAction.getValue(AbstractAction.ACCELERATOR_KEY), downAction.getValue(AbstractAction.NAME));
        // LEFT (next field action)
        jTableInputMap.put((KeyStroke) leftAction.getValue(AbstractAction.ACCELERATOR_KEY), leftAction.getValue(AbstractAction.NAME));
        // RIGHT (previous field action)
        jTableInputMap.put((KeyStroke) rightAction.getValue(AbstractAction.ACCELERATOR_KEY), rightAction.getValue(AbstractAction.NAME));
        // SHIFT+UP (shift up action)
        jTableInputMap.put((KeyStroke) shiftUpAction.getValue(AbstractAction.ACCELERATOR_KEY), shiftUpAction.getValue(AbstractAction.NAME));
        // SHIFT+DOWN (shift down action)
        jTableInputMap.put((KeyStroke) shiftDownAction.getValue(AbstractAction.ACCELERATOR_KEY), shiftDownAction.getValue(AbstractAction.NAME));
        // HOME (first record action)
        jTableInputMap.put((KeyStroke) homeAction.getValue(AbstractAction.ACCELERATOR_KEY), homeAction.getValue(AbstractAction.NAME));
        // END (last record action)
        jTableInputMap.put((KeyStroke) endAction.getValue(AbstractAction.ACCELERATOR_KEY), endAction.getValue(AbstractAction.NAME));
        // TAB (edit next cell action)
        jTableInputMap.put((KeyStroke) tabAction.getValue(AbstractAction.ACCELERATOR_KEY), tabAction.getValue(AbstractAction.NAME));
        // SHIFT+TAB (edit previous cell action)
        jTableInputMap.put((KeyStroke) shiftTabAction.getValue(AbstractAction.ACCELERATOR_KEY), shiftTabAction.getValue(AbstractAction.NAME));
        // CTRL+F (find Action)
        jTableInputMap.put((KeyStroke) findAction.getValue(AbstractAction.ACCELERATOR_KEY), findAction.getValue(AbstractAction.NAME));
        // F3 (find next action)
        jTableInputMap.put((KeyStroke) findNextAction.getValue(AbstractAction.ACCELERATOR_KEY), findNextAction.getValue(AbstractAction.NAME));
        // SHIFT+F3 (find previous action)
        jTableInputMap.put((KeyStroke) findPrevAction.getValue(AbstractAction.ACCELERATOR_KEY), findPrevAction.getValue(AbstractAction.NAME));
        // CTRL+A (select all records action)
        jTableInputMap.put((KeyStroke) selectAllRecsAction.getValue(AbstractAction.ACCELERATOR_KEY), selectAllRecsAction.getValue(AbstractAction.NAME));
        // CTRL+HOME (promote selected records action)
        jTableInputMap.put((KeyStroke) promoteSelRecsAction.getValue(AbstractAction.ACCELERATOR_KEY), promoteSelRecsAction.getValue(AbstractAction.NAME));
        // CTRL+SHIFT+A (select all fields action)
        jTableInputMap.put((KeyStroke) fieldSelectAllAction.getValue(AbstractAction.ACCELERATOR_KEY), fieldSelectAllAction.getValue(AbstractAction.NAME));
        // CTRL_C (copy action)
        jTableInputMap.put((KeyStroke) copyAction.getValue(AbstractAction.ACCELERATOR_KEY), copyAction.getValue(AbstractAction.NAME));
        // CTRL_X (cut action)
        jTableInputMap.put((KeyStroke) cutAction.getValue(AbstractAction.ACCELERATOR_KEY), cutAction.getValue(AbstractAction.NAME));
        // CTRL_V (pasteMItem action)
        jTableInputMap.put((KeyStroke) pasteAction.getValue(AbstractAction.ACCELERATOR_KEY), pasteAction.getValue(AbstractAction.NAME));
        // DELETE (delete active record action)
        jTableInputMap.put((KeyStroke) removeRecordAction.getValue(AbstractAction.ACCELERATOR_KEY), removeRecordAction.getValue(AbstractAction.NAME));
        // SHIFT+DELETE (delete selected record action)
        jTableInputMap.put((KeyStroke) removeSelRecordAction.getValue(AbstractAction.ACCELERATOR_KEY), removeSelRecordAction.getValue(AbstractAction.NAME));
        // ESCAPE (stop editinf/new record action)
        jTableInputMap.put((KeyStroke) escapeAction.getValue(AbstractAction.ACCELERATOR_KEY), escapeAction.getValue(AbstractAction.NAME));
        // ENTER (start/stop edit action)
        jTableInputMap.put((KeyStroke) enterAction.getValue(AbstractAction.ACCELERATOR_KEY), enterAction.getValue(AbstractAction.NAME));
        // F2 (start edit action)
        jTableInputMap.put((KeyStroke) f2Action.getValue(AbstractAction.ACCELERATOR_KEY), f2Action.getValue(AbstractAction.NAME));
        // CTRL+I (field properties action)
        jTableInputMap.put((KeyStroke) fieldPropertiesAction.getValue(AbstractAction.ACCELERATOR_KEY), fieldPropertiesAction.getValue(AbstractAction.NAME));
        // CTRL+DELETE (remove selected fields action)
        jTableInputMap.put((KeyStroke) removeFieldAction.getValue(AbstractAction.ACCELERATOR_KEY), removeFieldAction.getValue(AbstractAction.NAME));
        // CTRL+UP (field sort ascending action)
        jTableInputMap.put((KeyStroke) fieldSortAscAction.getValue(AbstractAction.ACCELERATOR_KEY), fieldSortAscAction.getValue(AbstractAction.NAME));
        // CTRL+DOWN (field sort descending action)
        jTableInputMap.put((KeyStroke) fieldSortDescAction.getValue(AbstractAction.ACCELERATOR_KEY), fieldSortDescAction.getValue(AbstractAction.NAME));
        jTable.setInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, jTableInputMap);

//        ComponentInputMap whenInFocusedWindowMap = new ComponentInputMap(this);
//        jTable.setInputMap(WHEN_IN_FOCUSED_WINDOW, whenInFocusedWindowMap);

        // Create the action of the DBTable
//        ActionMap map = getActionMap();
//        map.setParent(null);
        getInputMap(WHEN_FOCUSED).setParent(null);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).setParent(null);
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).setParent(null);
//        ActionMap dbTableActionMap = new ActionMap();
//        dbTableActionMap.put(addFieldAction.getValue(AbstractAction.NAME), addFieldAction);
//        dbTableActionMap.put(addRecordAction.getValue(AbstractAction.NAME), addRecordAction);
//        setActionMap(dbTableActionMap);

        InputMap dbTableInputMap = new InputMap();
        // INSERT (new record action)
        dbTableInputMap.put((KeyStroke) addRecordAction.getValue(AbstractAction.ACCELERATOR_KEY), addRecordAction.getValue(AbstractAction.NAME));
        // CTRL+INSERT (add field action)
        dbTableInputMap.put((KeyStroke) addFieldAction.getValue(AbstractAction.ACCELERATOR_KEY), addFieldAction.getValue(AbstractAction.NAME));
        setInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, dbTableInputMap);

        findNextAction.setEnabled(false);
        findPrevAction.setEnabled(false);

/*        KeyStroke[] strokes = jTable.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).allKeys();
        for (int i=0; strokes != null && i<strokes.length; i++)
            System.out.println("Before strokes: " + strokes[i]);
        jTable.setInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, jTableInputMap);
        strokes = jTable.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).allKeys();
        for (int i=0; strokes != null && i<strokes.length; i++)
            System.out.println("After strokes: " + strokes[i]);
*/
        /* Unregister the already registered KeyStrokes VK_UP and VK_DOWN. Below our actions
        * for these Keystrokes are defined.
        */
/*        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP   , 0));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN , 0));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT , 0));

        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_HOME , 0));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_END , 0));

        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP   , KeyEvent.SHIFT_MASK));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN , KeyEvent.SHIFT_MASK));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.SHIFT_MASK));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT , KeyEvent.SHIFT_MASK));

        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_TAB  ,     0));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_TAB  , KeyEvent.SHIFT_MASK));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,     0));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_MASK));

        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));

        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        jTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
*/
        /* Registration of all the DBTable Keystrokes.
        */
        // CTRL+F (find Action)
/*        jTable.registerKeyboardAction(dbComponent.findAction, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK), WHEN_IN_FOCUSED_WINDOW);

        // SHIFT+F3 (find previous action)
        jTable.registerKeyboardAction(dbComponent.findPrevAction, KeyStroke.getKeyStroke(KeyEvent.VK_F3, KeyEvent.SHIFT_MASK), WHEN_IN_FOCUSED_WINDOW);

        // F3 (find next)
        jTable.registerKeyboardAction(dbComponent.findNextAction, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), WHEN_IN_FOCUSED_WINDOW);

        // CTRL+A (select all records)
        jTable.registerKeyboardAction(dbComponent.selectAllRecsAction, KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);//WHEN_IN_FOCUSED_WINDOW);

        // CTRL+P (promote selected records)
        jTable.registerKeyboardAction(dbComponent.promoteSelRecsAction, KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK), WHEN_IN_FOCUSED_WINDOW);

        // CTRL+SHIFT+A (select all fields)
        jTable.registerKeyboardAction(dbComponent.fieldSelectAllAction, KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK | InputEvent.SHIFT_MASK), WHEN_IN_FOCUSED_WINDOW);

        //CTRL_C (copy)
        jTable.registerKeyboardAction(copyAction, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), WHEN_IN_FOCUSED_WINDOW);

        //CTRL_X (cut)
        jTable.registerKeyboardAction(cutAction, KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK), WHEN_IN_FOCUSED_WINDOW);

        //CTRL_V (pasteMItem)
        jTable.registerKeyboardAction(pasteAction, KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK), WHEN_IN_FOCUSED_WINDOW);

        //VK_DELETE (delete active record)
        jTable.registerKeyboardAction(dbComponent.removeRecordAction, KeyStroke.getKeyStroke("DELETE"), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);//WHEN_IN_FOCUSED_WINDOW);

        //SHIFT+VK_DELETE (delete selected record)
        jTable.registerKeyboardAction(dbComponent.removeSelRecordAction, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.SHIFT_MASK), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);//WHEN_IN_FOCUSED_WINDOW);

        //VK_INSERT
        jTable.registerKeyboardAction(addRecordAction, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), WHEN_IN_FOCUSED_WINDOW);

        //VK_ESCAPE
        jTable.registerKeyboardAction(escapeAction, KeyStroke.getKeyStroke("ESCAPE"), WHEN_FOCUSED);

        //VK_PAGE_UP
        jTable.registerKeyboardAction(pageUpAction, KeyStroke.getKeyStroke("PAGE_UP"), WHEN_IN_FOCUSED_WINDOW);

        //VK_PAGE_DOWN
        jTable.registerKeyboardAction(pageDownAction, KeyStroke.getKeyStroke("PAGE_DOWN"), WHEN_IN_FOCUSED_WINDOW);

        //VK_LEFT
        jTable.registerKeyboardAction(leftAction, KeyStroke.getKeyStroke("LEFT"), this.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);//  WHEN_IN_FOCUSED_WINDOW);

        //VK_RIGHT
        jTable.registerKeyboardAction(rightAction, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);//WHEN_IN_FOCUSED_WINDOW);

        //VK_UP
        jTable.registerKeyboardAction(upAction, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);//WHEN_IN_FOCUSED_WINDOW);
        //VK_DOWN
        jTable.registerKeyboardAction(downAction, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);//WHEN_IN_FOCUSED_WINDOW);

        //SHIFT+VK_UP
        jTable.registerKeyboardAction(shiftUpAction, KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);//WHEN_IN_FOCUSED_WINDOW);
        //SHIFT+VK_DOWN
        jTable.registerKeyboardAction(shiftDownAction, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);//WHEN_IN_FOCUSED_WINDOW); //WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
///////////////////////////
        //VK_HOME
        jTable.registerKeyboardAction(homeAction, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //VK_END
        jTable.registerKeyboardAction(endAction, KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //VK_TAB
        jTable.registerKeyboardAction(tabAction, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //SHIFT+VK_TAB
        jTable.registerKeyboardAction(shiftTabAction, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //VK_ENTER
        jTable.registerKeyboardAction(enterAction, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //VK_F2
        jTable.registerKeyboardAction(f2Action, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
*/
    }

    private MouseInputListener getTableHeaderMouseListener() {
        if (tableHeaderMouseListener == null) {
            tableHeaderMouseListener = new MouseInputAdapter() {
                boolean dragOccured = false;
                public void mouseReleased(MouseEvent e) {
                    boolean tmpDragOccured = dragOccured;
                    dragOccured = false;
                    if (!sortFromColumnHeadersEnabled) return;
                    if (e.isControlDown() || e.isAltDown() || e.isMetaDown() || e.isShiftDown()) return;
                    if (!SwingUtilities.isLeftMouseButton(e)) return;
//                    if (e.getClickCount() > 1)
//                        return;
                    // If the column was not dragged and if the mouse was released inside the rectangle of
                    // the header of the column which is selected, the sort the column.
                    if (!tmpDragOccured) {
                        int selectedColIndex = jTable.getSelectedColumn();
                        Rectangle selectedColHeaderRect = jTable.getTableHeader().getHeaderRect(selectedColIndex);
                        if (selectedColHeaderRect.contains(e.getPoint()))
                            fieldSortAction.execute();
                    }
                }
                public void mouseDragged(MouseEvent e) {
                    dragOccured = true;
                }
                public void mousePressed(MouseEvent e) {
                    tipManager.resetTip();

                    closeDatabasePopupMenu();

                    /* If a jTable cell was edited when a column is selected, then stop the cell editing.
                     * This results in the evaluation of the edited value of the cell.
                     */
                    stopCellEditing();

                    if (SwingUtilities.isMiddleMouseButton(e)) return;

                    DefaultTableColumnModel dcm = (DefaultTableColumnModel) jTable.getColumnModel();
                    int index = jTable.getTableHeader().columnAtPoint(e.getPoint());
                    if (index == -1) {
                        return;
                    }
//System.out.println("Mouse pressed on " + jTable.getColumn(jTable.getColumnName(index)) + ", header: " + jTable.getColumn(jTable.getColumnName(index)).getHeaderRenderer());
                    boolean deselecting = false;
                    if (e.isControlDown()) {
                        if (((Boolean) colSelectionStatus.get(index)).booleanValue()) //jTable.isColumnSelected(index))
                            deselecting = true;
                        if (jTable.getSelectedColumnCount() == 1 && !jTable.getColumnSelectionAllowed())
                            deselecting = false;

                    }

                    if (!jTable.getColumnSelectionAllowed()) {
                        jTable.setColumnSelectionAllowed(true);
                    }

                    if (deselecting) {
                        int[] selColumns = jTable.getSelectedColumns();
                        dcm.getSelectionModel().clearSelection();
                        for (int i=0; i<selColumns.length; i++) {
                            if (selColumns[i] != index) {
                                dcm.getSelectionModel().addSelectionInterval(selColumns[i], selColumns[i]);
                            }
                        }
                        colSelectionStatus.set(index, Boolean.FALSE);
                        updateColumnHeaderSelection();
                        jTable.getSelectionModel().setSelectionInterval(activeRow, activeRow);
                    }else{
                        jTable.getSelectionModel().setSelectionInterval(activeRow, activeRow);
                        if (!e.isControlDown()) {
                            for (int m=0; m<colSelectionStatus.size(); m++)
                                colSelectionStatus.set(m, Boolean.FALSE);
                            colSelectionStatus.set(index, Boolean.TRUE);
                            dcm.getSelectionModel().setSelectionInterval(index, index);
                            /* The very special case when a DBTable is initialized. By default
                            * the first column is selected, so when the user clicks the header
                            * of the first column, no columnSelectionChangedAction() takes place
                            * cause the selectection actually does not change.
                            */
                            if (index == 0) {
                                columnSelectionChangedAction();
                            }
                        }else{
                            colSelectionStatus.set(index, Boolean.TRUE);
                            dcm.getSelectionModel().addSelectionInterval(index, index);
                        }
                        refreshField(index);
                    }
                    if (jTable.getSelectedColumnCount() == 0){
                        /* When "simultaneousFieldRecordActivation" is "false", column selection is not allowed.
                         * However in the special case, when a field header is clicked, we set "columnSelectionAllowed"
                         * to "true", so that the column is selected. "columnSelectionAllowed" turns again to "false"
                         * when VK_LEFT/VK_RIGHT/VK_UP/VK_DOWN is pressed or the user clicks inside the jTable.
                         * Turning "columnSelectionAllowed" to false, resets all te existing selections (both
                         * row and column) in the jTable, so we keep track of them and reselect them afterwards.
                         */
                        if (jTable.getColumnSelectionAllowed() != isSimultaneousFieldRecordActivation()) {
                            int[] selectedRows = jTable.getSelectedRows();
                            jTable.setColumnSelectionAllowed(false);
                            for (int i=0; i<colSelectionStatus.size(); i++) {
                                if (((Boolean) colSelectionStatus.get(i)).booleanValue())
                                    jTable.getColumnModel().getSelectionModel().setSelectionInterval(i, i);
                            }
                            for (int i=0; i<selectedRows.length; i++)
                                jTable.getSelectionModel().addSelectionInterval(selectedRows[i], selectedRows[i]);
                        }
                    }

                    if (dbComponent != null)
                        dbComponent.updateFieldMenuRestStatus();

                    // If the column's header was right-clicked, then show the column pop-up menu
                    if (SwingUtilities.isRightMouseButton(e) && columnPopupEnabled) {
                        System.out.println("Showing field pop-up");
                        getColumnPopupMenu().show((Component) e.getSource(), e.getX(), e.getY());
                    }
                }
            };
        }
        return tableHeaderMouseListener;
    }

    private TableColumnModelListener getTableColumnModelListener() {
        if (tableColumnModelListener == null) {
            tableColumnModelListener = new TableColumnModelListener() {
                public void columnSelectionChanged(ListSelectionEvent e) {
                    columnSelectionChangedAction();
                }
                public void columnAdded(TableColumnModelEvent e) {
//1                    int i = jTable.getColumnCount()-1;
//1                    if (columnOrder.indexOf(i) == -1)
//1                        columnOrder.add(i);
//1                    if (viewStructure.tableView.getColumnOrder().indexOf(i) == -1)
//1                        viewStructure.tableView.getColumnOrder().add(i);
                    colSelectionStatus.add(Boolean.FALSE);
                    jTable.sizeColumnsToFit(false);
                }
                public void columnRemoved(TableColumnModelEvent e) {
                }
                public void columnMoved(TableColumnModelEvent e) {
                    /* Storing the new order of columns in the Table.UIProperties Array.
                    */
//1                    Array columnOrder = viewStructure.tableView.getColumnOrder();
//1                    int from = columnOrder.get(e.getFromIndex());
                    Boolean selFrom = (Boolean) colSelectionStatus.get(e.getFromIndex());
//1                    columnOrder.set(e.getFromIndex(), columnOrder.get(e.getToIndex()));
                    colSelectionStatus.set(e.getFromIndex(), colSelectionStatus.get(e.getToIndex()));
//1                    columnOrder.set(e.getToIndex(), from);
                    colSelectionStatus.set(e.getToIndex(), selFrom);
        //                dbTable.UIProperties.put(FIELD_ORDER_POS, columnOrder);

                    AbstractDBTableColumn tc = tableColumns.get(e.getFromIndex());
                    tableColumns.set(e.getFromIndex(), tableColumns.get(e.getToIndex()));
                    tableColumns.set(e.getToIndex(), tc);

                    int[] indices = jTable.getSelectedColumns();
                    TableColumn col1;
                    HeaderRenderer hr;
                    boolean selected;
                    for (int m = 0; m<tableColumns.size(); m++) {
                        selected = false;
                        int viewIndex = jTable.convertColumnIndexToView(m);
                        for (int i=0; i<indices.length; i++) {
                            if (viewIndex ==  indices[i]) {
                                selected = true;
                                break;
                            }
                        }

                        col1 = tableColumns.get(viewIndex).tableColumn;
                        hr = (HeaderRenderer) col1.getHeaderRenderer();
                        AbstractTableField f1 = tableColumns.get(m).tableField; //(AbstractTableField) tableFields.get(m);
                        if (selected) {
                            hr.setBackground(selectedHeaderBackgroundColor);
                            if (!f1.isCalculated())
                                hr.setForeground(Color.white);
                        }else{
                            if (f1.isCalculated()) {
                                    hr.setBackground(headerBackground);
                            }else{
                                    hr.setBackground(headerBackground);
                                hr.setForeground(Color.black);
                            }
                        }
                    }
                    jTable.getTableHeader().repaint();
                    if (dbComponent != null) {
                        dbComponent.fireColumnMoved(new ColumnMovedEvent(
                                DBTable.this,
                                dbComponent.dbTables.indexOf(DBTable.this),
                                (String) jTable.getColumnModel().getColumn(e.getFromIndex()).getIdentifier(),
                                (String) jTable.getColumnModel().getColumn(e.getToIndex()).getIdentifier())
                        );
                    }

                    if (!isTableInitializing)
                        setModified(true);
                }
                public void columnMarginChanged(ChangeEvent e) {}
            };
        }

        return tableColumnModelListener;
    }

/*    private MouseListener getTableMouseListener() {
        if (tableMouseListener == null) {
            tableMouseListener = new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    tipManager.resetTip();
                    closeDatabasePopupMenu();

                    mousePressedAtRowBck = mousePressedAtRow;
                    mousePressedAtRow = jTable.rowAtPoint(e.getPoint());
                    mousePressedAtColumn = jTable.columnAtPoint(e.getPoint());
                    if (mousePressedAtRow == -1 || mousePressedAtColumn == -1)
                        return;
*/
                    /* When "simultaneousFieldRecordActivation" is "false", column selection is not allowed.
                    * However in the special case, when a field header is clicked, we set "columnSelectionAllowed"
                    * to "true", so that the column is selected. "columnSelectionAllowed" turns again to "false"
                    * when VK_LEFT/VK_RIGHT/VK_UP/VK_DOWN is pressed or the user clicks inside the jTable.
                    * Turning "columnSelectionAllowed" to false, resets all te existing selections (both
                    * row and column) in the jTable, so we keep track of them and reselect them afterwards.
                    */
/*                    if (jTable.getColumnSelectionAllowed() != isSimultaneousFieldRecordActivation()) {
                        int[] selectedRows = jTable.getSelectedRows();
                        jTable.setColumnSelectionAllowed(false);
                        for (int i=0; i<colSelectionStatus.size(); i++) {
                            if (((Boolean) colSelectionStatus.get(i)).booleanValue())
                                jTable.getColumnModel().getSelectionModel().setSelectionInterval(i, i);
                        }
                        for (int i=0; i<selectedRows.length; i++)
                            jTable.getSelectionModel().addSelectionInterval(selectedRows[i], selectedRows[i]);
                    }
                    stopCellEditing();

                    if (e.isShiftDown() || e.isControlDown()) {
                        int selColCount = 0;
                        for (int k=0; k<colSelectionStatus.size(); k++) {
                            if (((Boolean)  colSelectionStatus.get(k)).booleanValue())
                                selColCount++;
                        }
                        if ((!((Boolean) colSelectionStatus.get(mousePressedAtColumn)).booleanValue()) || selColCount > 1) {
                            for (int i=0; i<colSelectionStatus.size(); i++)
                                colSelectionStatus.set(i, Boolean.FALSE);
                            colSelectionStatus.set(mousePressedAtColumn, Boolean.TRUE);
                            jTable.getColumnModel().getSelectionModel().setSelectionInterval(mousePressedAtColumn, mousePressedAtColumn);
                            updateColumnHeaderSelection();
                        }
                    }else{
    //1                    if (!dbComponent.pendingNewRecord) {
                            int selColCount = 0;
                            for (int k=0; k<colSelectionStatus.size(); k++) {
                                if (((Boolean)  colSelectionStatus.get(k)).booleanValue())
                                    selColCount++;
                            }
                            if ((!((Boolean) colSelectionStatus.get(mousePressedAtColumn)).booleanValue()) || selColCount > 1) {
                                for (int m=0; m<colSelectionStatus.size(); m++)
                                    colSelectionStatus.set(m, Boolean.FALSE);
    //                              jTable.getColumnModel().getSelectionModel().clearSelection();
                                colSelectionStatus.set(mousePressedAtColumn, Boolean.TRUE);
                                jTable.getColumnModel().getSelectionModel().setSelectionInterval(mousePressedAtColumn, mousePressedAtColumn);
                            }else{
*/                                /* The column is already selected, but it may already be actually selected by a click on the
                                * header. So set the column selection again, so that a refresh will be performed.
                                */
/*                                jTable.getColumnModel().getSelectionModel().clearSelection();
                                jTable.getColumnModel().getSelectionModel().setSelectionInterval(mousePressedAtColumn, mousePressedAtColumn);
                            }
                            setActiveRow(mousePressedAtRow);
    //1                    }
                    }

                    if (*//*1!dbComponent.pendingNewRecord && *//*e.getClickCount() == 2) {
                        AbstractDBTableColumn column = tableColumns.get(mousePressedAtColumn);
                        int fieldIndex = column.tableColumn.getModelIndex();
                        AbstractTableField fld = column.tableField; //(AbstractTableField) tableFields.get(fieldIndex);
*/                        /* When a cell is right or left mouse clicked twice, editing starts.
                        * Stop cell editing, when it is the result of a double right-mouse click.
                        */
/*                        if (e.getModifiers() == e.BUTTON3_MASK) {
                            cancelCellEditing();
                        }

                        if (fld.isEditable() && table.isDataChangeAllowed()) {
                            editCellAt(mousePressedAtRow, mousePressedAtColumn);
*/                            /* If the field is of Boolean data type, show the cell editor's pop-up
                            */
/*                        }
                        if (fld.getDataType().equals(CImageIcon.class)) {
                            if (iconFileDialog != null && iconFileDialog.getFile() != null) {
                                String fileName = iconFileDialog.getDirectory() + iconFileDialog.getFile();
                                ((DBTableModel) jTable.getModel()).setValueAt(fileName, mousePressedAtRowBck, fieldIndex);
                                jTable.repaint(jTable.getCellRect(mousePressedAtRowBck, fieldIndex, true));
                            }
                        }
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    //IE tests
                    if (isEditing() || (iconFileDialog != null && iconFileDialog.isVisible()))
                        return;
                    int rowIndex;
                    if (e.isShiftDown()) {
                        rowIndex = jTable.getSelectedRow();
                        if (lastShiftClick != -1) {
                            if ((lastShiftClick > rowIndex && rowIndex >= mousePressedAtRow) || (lastShiftClick < rowIndex && rowIndex <= mousePressedAtRow))
                                rowIndex = lastShiftClick;
                            else if ((lastShiftClick > mousePressedAtRow && mousePressedAtRow >= rowIndex) || (lastShiftClick < mousePressedAtRow && mousePressedAtRow <= rowIndex))
                                rowIndex = lastShiftClick;
                        }
*/
                        /* The last row at which the user shift-clicked is hold, due to the
                        * custom functionality of shift-clicking.
                        */
/*                        lastShiftClick = mousePressedAtRow;
                    }else{
                        rowIndex = jTable.rowAtPoint(e.getPoint());
                        lastShiftClick = -1;
                    }

                    int numOfRows = (rowIndex > mousePressedAtRow)? rowIndex-mousePressedAtRow+1 : mousePressedAtRow-rowIndex+1;
                    int startingIndex = (rowIndex > mousePressedAtRow)? mousePressedAtRow : rowIndex;

                    if (rowIndex == -1)
                        return;

                    if (e.getModifiers() == e.BUTTON3_MASK && tablePopupEnabled) {
                        getTablePopupMenu(mousePressedAtRow, mousePressedAtColumn).show(jTable, e.getX(), e.getY());
                    }

                    //Array recIndicesToBeSelected = new Array();
                    IntBaseArray recIndicesToBeSelected = new IntBaseArray();
                    //Array recIndicesToBeDeselected = new Array();
                    IntBaseArray recIndicesToBeDeselected = new IntBaseArray();

                    if (e.isControlDown() && !e.isShiftDown()) {
                        Array rowsToRefresh = new Array();
                        for (int i=0; i<numOfRows; i++) {
                            if (!table.isRecordSelected(table.recordIndex.get(startingIndex+i)))
                            //recIndicesToBeSelected.add(new Integer(dbTable.recordIndex[startingIndex+i]));
                                recIndicesToBeSelected.add(table.recordIndex.get(startingIndex+i));
                            else
                            //recIndicesToBeDeselected.add(new Integer(dbTable.recordIndex[startingIndex+i]));
                                recIndicesToBeDeselected.add(table.recordIndex.get(startingIndex+i));
                            rowsToRefresh.add(new Integer(startingIndex+i));
                        }

                        iterateEvent = false;
                        if (recordSelectionChangeAllowed)
                            table.addToSelectedSubset(recIndicesToBeSelected.toArray());
                        iterateEvent = false;
                        if (recordSelectionChangeAllowed)
                            table.removeFromSelectedSubset(recIndicesToBeDeselected.toArray());

                        setActiveRow(mousePressedAtRow);
                        if (!jTable.isColumnSelected(mousePressedAtColumn)) {
                            colSelectionStatus.set(mousePressedAtColumn, Boolean.TRUE);
                            jTable.getColumnModel().getSelectionModel().addSelectionInterval(mousePressedAtColumn, mousePressedAtColumn);
                        }

                        if (mousePressedAtRow != rowIndex) {
                            setActiveRow(rowIndex);
                            rowBar.repaint();
                        }else{
                            rowBar.resetActiveRow(mousePressedAtRow);
                            rowBar.drawActiveRow(rowBar.getGraphics(), false);
                        }

                        refreshRows(rowsToRefresh);
                    }

                    if (!e.isControlDown() && e.isShiftDown()) {
                        Array rowsToRefresh = new Array();
                        for (int i=0; i<numOfRows; i++) {
                            if (!table.isRecordSelected(table.recordIndex.get(startingIndex+i))) {
                                //recIndicesToBeSelected.add(new Integer(dbTable.recordIndex[startingIndex+i]));
                                recIndicesToBeSelected.add(table.recordIndex.get(startingIndex+i));
                                rowsToRefresh.add(new Integer(startingIndex+i));
                            }
                        }
                        iterateEvent = false;
                        if (recordSelectionChangeAllowed)
                            table.addToSelectedSubset(recIndicesToBeSelected.toArray());

                        setActiveRow(mousePressedAtRow);
                        if (!jTable.isColumnSelected(mousePressedAtColumn)) {
                            colSelectionStatus.set(mousePressedAtColumn, Boolean.FALSE);
                            jTable.getColumnModel().getSelectionModel().addSelectionInterval(mousePressedAtColumn, mousePressedAtColumn);
                        }

                        rowBar.repaint();
                        refreshRows(rowsToRefresh);
                    }

                    if (!e.isControlDown() && !e.isShiftDown() && mousePressedAtRow != rowIndex) {
                        //recIndicesToBeDeselected = (Array) dbTable.getSelectedSubset().clone();
                        recIndicesToBeDeselected = (IntBaseArray) table.getSelectedSubset().clone();
                        Array rowsToRefresh = new Array();
                        for (int i=0; i<numOfRows; i++) {
                            //recIndicesToBeSelected.add(new Integer(dbTable.recordIndex[startingIndex+i]));
                            recIndicesToBeSelected.add(table.recordIndex.get(startingIndex+i));
                            rowsToRefresh.add(new Integer(startingIndex+i));
                        }

*/                        /* Find those indices in recIndicesToBeDeselected, which also
                        * exist in recIndicesToBeSelected and remove them from recIndicesToBeDeselected.
                        */
/*                        for (int i=0; i<recIndicesToBeDeselected.size(); i++) {
                            //if (recIndicesToBeSelected.indexOf(recIndicesToBeDeselected.at(i)) != -1)
                            if (recIndicesToBeSelected.indexOf(recIndicesToBeDeselected.get(i)) != -1)
                                recIndicesToBeDeselected.remove(i);
                        }
                        iterateEvent = false;
                        if (recordSelectionChangeAllowed)
                            table.removeFromSelectedSubset(recIndicesToBeDeselected.toArray());
                        iterateEvent = false;
                        if (recordSelectionChangeAllowed)
                            table.addToSelectedSubset(recIndicesToBeSelected.toArray());

                        setActiveRow(rowIndex);
                        for (int l=0; l<recIndicesToBeDeselected.size(); l++)
                                //rowsToRefresh.add(recIndicesToBeDeselected.at(l));
                            rowsToRefresh.add(new Integer(recIndicesToBeDeselected.get(l)));
                        rowBar.repaint();
                        refreshRows(rowsToRefresh);
                    }
                    if (dbComponent != null)
                        dbComponent.updateFieldMenuRestStatus();
                }
            };
        }

        return tableMouseListener;
    }
*/
    /**
     * Returns (and creates, if necessary) the pop-up menu which appears when right-clicking on the DBTable's records.
     * @return
     */
    public DBTablePopupMenu getTablePopupMenu() {
        return getTablePopupMenu(0, 0);
    }

    /**
     * Resets the pop-up menu which which appears when right-clicking on the DBTable's records. This happens only if the
     * pop-up menu has been created. This method brings the pop-up menu to its default state in regard to its contents.
     * A new instance of the <code>DBTablePopupMenu</code> is created, so any existing references are invalid.
     * <code>getTablePopupMenu()</code> should be called to get a reference to the new instance.
     */
    public void resetTablePopupMenu() {
        if (tablePopup != null) {
            tablePopup = null;
            getTablePopupMenu(0, 0);
        }
    }

    private DBTablePopupMenu getTablePopupMenu(int row, int column) {
        if (tablePopup == null) {
            tablePopup = new DBTablePopupMenu(this); //ESlatePopupMenu();
            getESlateHandle().add(tablePopup.getESlateHandle());
        }

        AbstractTableField field = null;
//cf        int fieldIndex = -1;
        if (table != null) {
//cf            fieldIndex = jTable.convertColumnIndexToModel(column);
//cf            if (fieldIndex != -1)
//cf                field = table.getTableField(fieldIndex); //tableFields.get(fieldIndex);
            if (column != -1)
                field = tableColumns.get(column).tableField;
        }
        int recordIndex = -1;
        if (table != null)
            recordIndex = table.recordForRow(row);
/*        if (recordIndex == -1) {
            // probably this is a new record, whose addition is pending. In this case, don't allow any action.
            cutAction.setEnabled(false);
            copyAction.setEnabled(false);
            imageEditAction.setEnabled(false);
            removeRecordAction.setEnabled(false);
            removeSelRecordAction.setEnabled(false);
            promoteSelRecsAction.setEnabled(false);
            pasteAction.setEnabled(false);
            findAction.setEnabled(false);
            findNextAction.setEnabled(false);
            findPrevAction.setEnabled(false);
            tablePreferencesAction.setEnabled(false);
            tableInfoAction.setEnabled(false);
            tableDescriptionAction.setEnabled(false);
            addFieldAction.setEnabled(false);
            addRecordAction.setEnabled(false);
            return tablePopup;
        }
*/
        Object cellContents = null;
//cf        if (fieldIndex != -1 && recordIndex != -1)
//cf            cellContents = table.riskyGetCell(fieldIndex, recordIndex);
        try{
            if (field != null && recordIndex != -1)
                cellContents = field.getCellObject(recordIndex);
        }catch (InvalidCellAddressException exc) {}
        boolean cellEditable = false;
        if (field != null)
            cellEditable = field.isEditable() && table.isDataChangeAllowed();

        if (table == null) {
            cutAction.setEnabled(false);
            copyAction.setEnabled(false);
            imageEditAction.setEnabled(false);
            removeRecordAction.setEnabled(false);
            removeSelRecordAction.setEnabled(false);
            promoteSelRecsAction.setEnabled(false);
            pasteAction.setEnabled(false);
            findAction.setEnabled(false);
            findNextAction.setEnabled(false);
            findPrevAction.setEnabled(false);
            tablePreferencesAction.setEnabled(false);
            tableInfoAction.setEnabled(false);
            tableDescriptionAction.setEnabled(false);
            exportToTextFileAction.setEnabled(false);
            addFieldAction.setEnabled(false);
            addRecordAction.setEnabled(false);
            if (tablePopup.gridChooserMenu != null)
                tablePopup.gridChooserMenu.setEnabled(false);
        }else{
            if (cellEditable && cellContents != null)
                cutAction.setEnabled(true);
            else
                cutAction.setEnabled(false);
            if (cellEditable)
                pasteAction.setEnabled(true);
            else
                pasteAction.setEnabled(false);
            if (cellContents != null)
                copyAction.setEnabled(true);
            else
                copyAction.setEnabled(false);
            if (Database.iconEditorClass != null && cellEditable && field.getDataType().equals(CImageIcon.class))
                imageEditAction.setEnabled(true);
            else
                imageEditAction.setEnabled(true);

            if (table.getFieldCount() == 0)
                addRecordAction.setEnabled(false);
            else
                addRecordAction.setEnabled(true);
            tablePreferencesAction.setEnabled(true);
            tableInfoAction.setEnabled(true);
            tableDescriptionAction.setEnabled(true);
            exportToTextFileAction.setEnabled(true);
            addFieldAction.setEnabled(true);
            findAction.setEnabled(true);
            if (tablePopup.gridChooserMenu != null)
                tablePopup.gridChooserMenu.setEnabled(true);
        }

        return tablePopup;
    }

    /**
     * Returns (and creates, if necessary) the pop-up menu which appears by right-clicking on the header of any column
     * of the DBTable.
     * @return The column pop-up menu.
     */
    public DBTableColPopupMenu getColumnPopupMenu() {
        if (columnPopup == null) {
            columnPopup = new DBTableColPopupMenu(this); //ESlatePopupMenu();
            getESlateHandle().add(columnPopup.getESlateHandle());
        }

        int selectedColumnIndex = jTable.getSelectedColumn();
        columnPopup.geCurrencyFieldMItem().setSelected(false);
        if (selectedColumnIndex == -1) {
            fieldPropertiesAction.setEnabled(false);
            numericFieldCurrencyAction.setEnabled(false);
            removeFieldAction.setEnabled(false);
            fieldSortAscAction.setEnabled(false);
            fieldSortDescAction.setEnabled(false);
        }else{
            AbstractTableField field = tableColumns.get(selectedColumnIndex).tableField;
            if (field instanceof CurrencyField) {
                numericFieldCurrencyAction.setEnabled(true);
                if (((CurrencyField) field).getCurrency() != -1)
                    columnPopup.geCurrencyFieldMItem().setSelected(true);
            }else
                numericFieldCurrencyAction.setEnabled(false);
            fieldPropertiesAction.setEnabled(true);
            removeFieldAction.setEnabled(true);
            fieldSortAscAction.setEnabled(true);
            fieldSortDescAction.setEnabled(true);
        }

        return columnPopup;
    }

/*1    private CellEditorListener createCellEditorListener() {
        CellEditorListener listener = new CellEditorListener() {
            public void editingStopped(ChangeEvent e) {
//1                isEditing = false;
                int activeTableIndex = dbComponent.tabs.getSelectedIndex();
                ((DBTable) dbComponent.dbTables.at(activeTableIndex)).jTable.requestFocus();
                dbComponent.setCutCopyPasteStatus();
            }

            public void editingCanceled(ChangeEvent e) {
//1                isEditing = false;
                int activeTableIndex = dbComponent.tabs.getSelectedIndex();
                ((DBTable) dbComponent.dbTables.at(activeTableIndex)).jTable.requestFocus();
                dbComponent.setCutCopyPasteStatus();
            }
        };

        return listener;
    }
1*/
    private void createActions() {
        escapeAction = new EscapeAction(this, "EscapeAction");
        pageUpAction = new PageUpAction(this, "PageUpAction");
        pageDownAction = new PageDownAction(this, "PageDownAction");
        leftAction = new LeftAction(this, "LeftAction");
        rightAction = new RightAction(this, "RightAction");
        upAction = new UpAction(this, "UpAction");
        downAction = new DownAction(this, "DownAction");
        shiftUpAction = new ShiftUpAction(this, "ShiftUpAction");
        shiftDownAction = new ShiftDownAction(this, "ShiftDownAction");
        homeAction = new HomeAction(this, bundle.getString("HomeAction"));
        endAction = new EndAction(this, bundle.getString("EndAction"));
        tabAction = new TabAction(this, "TabAction");
        shiftTabAction = new ShiftTabAction(this, "ShiftTabAction");
        enterAction = new EnterAction(this, "EnterAction");
        f2Action = new F2Action(this, "F2Action");
        removeRecordAction = new TableRemoveRecordAction(this, bundle.getString("Table Remove Record Action"));
        cutAction = new TableCutAction(this, bundle.getString("Cut"));
        tableEditableAction = new TableEditableAction(this, "Table Editable");
        copyAction = new TableCopyAction(this, bundle.getString("Copy"));
        pasteAction = new TablePasteAction(this, bundle.getString("Paste"));
        imageEditAction = new TableImageEditAction(this, bundle.getString("Icon Editor"));
        addRecordAction = new TableAddRecordAction(this, bundle.getString("AddRecordActionName"));
        findAction = new TableFindAction(this, bundle.getString("Find Action"));
        tableSortAction = new TableSortAction(this, bundle.getString("Multi-column sort Action"));
        findNextAction = new TableFindNextAction(this, bundle.getString("FindNext Action"));
        findPrevAction = new TableFindPrevAction(this, bundle.getString("FindPrevious Action"));
        selectAllRecsAction = new TableSelectAllRecsAction(this, bundle.getString("SelectAllRecs Action"));
        promoteSelRecsAction = new TablePromoteSelRecsAction(this, bundle.getString("Promote Action"));
        fieldSelectAllAction = new TableFieldSelectAllAction(this, bundle.getString("Select All Fields Action"));
        removeSelRecordAction = new TableRemoveSelRecordAction(this, bundle.getString("Table Remove Selected Records Action"));
        fieldSortAction = new FieldSortAction(this, bundle.getString("SortField"));
        fieldSortAscAction = new FieldSortAscendingAction(this, bundle.getString("SortFieldAsc"));
        fieldSortDescAction = new FieldSortDescendingAction(this, bundle.getString("SortFieldDesc"));
        fieldPropertiesAction = new TableFieldPropertiesAction(this, bundle.getString("FieldProperties"));
        numericFieldCurrencyAction = new FieldCurrencyAction(this, this.bundle.getString("CurrencyField"));
        removeFieldAction = new TableRemoveFieldAction(this, bundle.getString("FieldRemove"));
        addFieldAction = new TableAddFieldAction(this, bundle.getString("AddField"));
        tablePreferencesAction = new DBTablePreferencesAction(this, bundle.getString("TableMenuPreferences"));
        tableInfoAction = new DBTableInfoAction(this, bundle.getString("TableMenuProperties"));
        tableDescriptionAction = new DBTableDescriptionAction(this, bundle.getString("Description"));
        exportToTextFileAction = new DBTableExportToTextFileAction(this, bundle.getString("ExportTable"));

        ActionMap map = getActionMap();
        map.setParent(null);
        ActionMap dbTableActionMap = new ActionMap();

        dbTableActionMap.put(pageUpAction.getValue(AbstractAction.NAME), pageUpAction);
        dbTableActionMap.put(pageDownAction.getValue(AbstractAction.NAME), pageDownAction);
        dbTableActionMap.put(upAction.getValue(AbstractAction.NAME), upAction);
        dbTableActionMap.put(downAction.getValue(AbstractAction.NAME), downAction);
        dbTableActionMap.put(leftAction.getValue(AbstractAction.NAME), leftAction);
        dbTableActionMap.put(rightAction.getValue(AbstractAction.NAME), rightAction);
        dbTableActionMap.put(shiftUpAction.getValue(AbstractAction.NAME), shiftUpAction);
        dbTableActionMap.put(shiftDownAction.getValue(AbstractAction.NAME), shiftDownAction);
        dbTableActionMap.put(tabAction.getValue(AbstractAction.NAME), tabAction);
        dbTableActionMap.put(shiftTabAction.getValue(AbstractAction.NAME), shiftTabAction);
        dbTableActionMap.put(homeAction.getValue(AbstractAction.NAME), homeAction);
        dbTableActionMap.put(endAction.getValue(AbstractAction.NAME), endAction);
        dbTableActionMap.put(findAction.getValue(AbstractAction.NAME), findAction);           // To check
        dbTableActionMap.put(findNextAction.getValue(AbstractAction.NAME), findNextAction);   // To check
        dbTableActionMap.put(findPrevAction.getValue(AbstractAction.NAME), findPrevAction);   // To check
        dbTableActionMap.put(tableSortAction.getValue(AbstractAction.NAME), tableSortAction);
        dbTableActionMap.put(selectAllRecsAction.getValue(AbstractAction.NAME), selectAllRecsAction);
        dbTableActionMap.put(promoteSelRecsAction.getValue(AbstractAction.NAME), promoteSelRecsAction);
        dbTableActionMap.put(fieldSelectAllAction.getValue(AbstractAction.NAME), fieldSelectAllAction);
        dbTableActionMap.put(copyAction.getValue(AbstractAction.NAME), copyAction);
        dbTableActionMap.put(tableEditableAction.getValue(AbstractAction.NAME), tableEditableAction);
        dbTableActionMap.put(cutAction.getValue(AbstractAction.NAME), cutAction);
        dbTableActionMap.put(pasteAction.getValue(AbstractAction.NAME), pasteAction);
        dbTableActionMap.put(removeRecordAction.getValue(AbstractAction.NAME), removeRecordAction);
        dbTableActionMap.put(removeSelRecordAction.getValue(AbstractAction.NAME), removeSelRecordAction);
        dbTableActionMap.put(escapeAction.getValue(AbstractAction.NAME), escapeAction);
        dbTableActionMap.put(enterAction.getValue(AbstractAction.NAME), enterAction);
        dbTableActionMap.put(f2Action.getValue(AbstractAction.NAME), f2Action);
        dbTableActionMap.put(fieldPropertiesAction.getValue(AbstractAction.NAME), fieldPropertiesAction);
        dbTableActionMap.put(numericFieldCurrencyAction.getValue(AbstractAction.NAME), numericFieldCurrencyAction);
        dbTableActionMap.put(removeFieldAction.getValue(AbstractAction.NAME), removeFieldAction);
        dbTableActionMap.put(fieldSortAscAction.getValue(AbstractAction.NAME), fieldSortAscAction);
        dbTableActionMap.put(fieldSortDescAction.getValue(AbstractAction.NAME), fieldSortDescAction);
        dbTableActionMap.put(tablePreferencesAction.getValue(AbstractAction.NAME), tablePreferencesAction);
        dbTableActionMap.put(tableInfoAction.getValue(AbstractAction.NAME), tableInfoAction);
        dbTableActionMap.put(tableDescriptionAction.getValue(AbstractAction.NAME), tableDescriptionAction);
        dbTableActionMap.put(exportToTextFileAction.getValue(AbstractAction.NAME), exportToTextFileAction);
        dbTableActionMap.put(imageEditAction.getValue(AbstractAction.NAME), imageEditAction);

        dbTableActionMap.put(addFieldAction.getValue(AbstractAction.NAME), addFieldAction);
        dbTableActionMap.put(addRecordAction.getValue(AbstractAction.NAME), addRecordAction);
        setActionMap(dbTableActionMap);
    }

    private void initializeTableColumns(Table dbaseTable) {
        TableFieldBaseArray tableFields = dbaseTable.getFields();
        DefaultTableColumnModel tcm = (DefaultTableColumnModel) jTable.getColumnModel();
        TableColumn col;
// cf        ColumnRendererEditor cr;
        AbstractTableField f;
//cf        tableColumns = new ArrayList();
        tableColumns = new DBTableColumnBaseArray();
        int defColumnWidth;
//1        int autoResizeMode = viewStructure.tableView.getAutoResizeMode();
        if (autoResizeMode == jTable.AUTO_RESIZE_OFF || autoResizeMode == jTable.AUTO_RESIZE_LAST_COLUMN)
            defColumnWidth = FieldViewProperties.DEFAULT_FIELD_WIDTH;
        else{
            if (tableFields.size() != 0)
                defColumnWidth = (getSize().width-20) / tableFields.size();
            else
                defColumnWidth = 20;
        }
//1        Font tableFont = viewStructure.tableView.getTableFont();
        for (int i=0; i<tcm.getColumnCount(); i++) {
            col = tcm.getColumn(i);

            f = tableModel.getField(i);
            if (fieldViewPropertiesMap != null) {
                FieldViewProperties fieldViewProperties = (FieldViewProperties) fieldViewPropertiesMap.get(f.getName());
                if (fieldViewProperties != null) {
                    if (!f.isHidden() || dbaseTable.isHiddenFieldsDisplayed()) {
                        col.setWidth(fieldViewProperties.getFieldWidth());
                        col.setMinWidth(fieldViewProperties.getFieldMinWidth());
                        col.setMaxWidth(fieldViewProperties.getFieldMaxWidth());
                        col.setPreferredWidth(fieldViewProperties.getPreferredWidth());
                        col.setResizable(fieldViewProperties.isResizable());
                    }else{
                        col.setMinWidth(0);
                        col.setWidth(0);
                        col.setMaxWidth(0);
                        col.setResizable(true);
                    }
                }
            }

            col.setIdentifier(f.getName());

            String fieldType = f.getDataType().getName();

            if (f == null) {
                System.out.println("Serious inconsistency error in DBTable DBTable(): (1)");
                return;
            }

            HeaderRenderer hr = new HeaderRenderer(f, this);
            col.setHeaderRenderer(hr);

/*cf            cr = new ColumnRendererEditor(this, f, tableFont);
            col.setCellRenderer(cr.renderer);
            if (f.isEditable() && dbaseTable.isDataChangeAllowed()) {
                col.setCellEditor(cr.editor);
                if (cr != null && cr.editor != null)
                    cr.editor.setClickCountToStart(2);
            }else
                col.setCellEditor(null);

            colRendererEditors.add(cr);

            if (cr.editor != null) {
//1                if (tableCellEditorListener == null)
//1                    tableCellEditorListener = createCellEditorListener();
//1                cr.editor.addCellEditorListener(tableCellEditorListener);
            }
*/
        }

        /* Restore the order of the columns
         */
        if (columnOrder != null) {
/*1 System.out.print("columnOrder: ");
for(int i=0; i<columnOrder.size(); i++)
    System.out.print(columnOrder.get(i) + ", ");
System.out.println();
1*/
            /* The Array of the column orders, which is stored in the dbTable.UIProperties
            * contains the order of the fields in the following form:
            *   {a1, a2, a3, a4, ..., aN} (a1, ... aN are ints)
            * where a1 is the index of the column which should be first, a2 is the index of
            * the column in the JTable which should be second....
            * This Array is used to produce the realIndex array, which has the following form:
            *  {b1, b2, b3, ..., bN} (b1, ..., bN are ints)
            * where b1 is the index at which the first column of the JTable should be, b2 is
            * the index at which the second column of the JTable should be, ...
            * (The JTable's columns at this point are ordered according to the TableFields in
            * the Table. The order of the TableFields in the Table is standard and does not
            * change. It is the order in which the TableFields were created.
            */
    //1        Array columnOrder = viewStructure.tableView.getColumnOrder();
            int[] realIndex = new int[columnOrder.size()]; // columnOrder.clone();
    //        System.out.println("columnOrder: " + columnOrder);
            for (int i=0; i<columnOrder.size(); i++)
                realIndex[columnOrder.get(i)] = i;

            int order;
            for (int k=0; k<realIndex.length; k++) {
                /* Find the current index of the column which should be at index 'k'.
                */
                order = 0;
                while (order < realIndex.length && realIndex[order] != k)
                    order++;
                if (order == realIndex.length)
                    continue;
                if (order != k) {
                    /* Move the column from its current order to k.
                    */
                    tcm.moveColumn(order, k);

                    /* Reorder the realIndex array in the exact same way the columns were
                    * re-ordered.
                    */
                    if (k < order) {
                        int tmp = realIndex[k];
                        realIndex[k] = realIndex[order];
                        for (int i = k+1; i<=order; i++) {
                            int tmp2 = realIndex[i];
                            realIndex[i] = tmp;
                            tmp = tmp2;
                        }
                    }else{
                        int tmp = realIndex[order];
                        realIndex[order] = realIndex[k];
                        for (int i = order+1; i<=k; i++) {
                            int tmp2 = realIndex[i];
                            realIndex[i] = tmp;
                            tmp = tmp2;
                        }
                    }
                }
            }
        }

        /* Now that the column order has been restored, it's time to fill the
        * tableColumns Array.
        */
        for (int i=0; i<tcm.getColumnCount(); i++)
            tableColumns.add(AbstractDBTableColumn.createDBTableColumn(this, tcm.getColumn(i), tableFields.get(jTable.convertColumnIndexToModel(i))));
        // Assign the state of the AbstractDBTableColumns of the DBTable
        if (columnState != null) {
            for (int i=0; i<tableColumns.size(); i++)
                tableColumns.get(i).applyState(columnState[i]);
            columnState = null;
        }
//cf            tableColumns.add(tcm.getColumn(i));
         jTable.getColumnModel().addColumnModelListener(getTableColumnModelListener());
    }

    /** Sets the height of the rows of the DBTable.*/
    public void setRowHeight(int height) {
        if (height == rowHeight)
            return;
        rowHeight = height;
//1        viewStructure.tableView.setRowHeight(height);
        if (!isTableInitializing) {
//            dbTable.UIProperties.put(ROW_HEIGHT_POS, new Integer(rowHeight));
            setModified(modified);
//            System.out.println("Setting modified 2");
        }
        if (jTable != null)
            jTable.setRowHeight(height);
    }

    /** Returns the height of the rows in the DBTable.*/
    public int getRowHeight() {
        return rowHeight;
//        return viewStructure.tableView.getRowHeight();
    }

    /** Adjusts the visibility of the horizontal lines of the DBTable.*/
    public void setHorizontalLinesVisible(boolean visible) {
        if (visible == horizontalLinesVisible)
            return;
        horizontalLinesVisible = visible;
//1        viewStructure.tableView.setHorizontalLinesVisible(visible);
//        dbTable.UIProperties.put(HOR_LINES_VISIBLE_POS, new Boolean(horLinesVisible));
        setModified(true);
//            System.out.println("Setting modified 3");
        intercellSpacing.height = (horizontalLinesVisible)? 1:0;
        if (jTable != null) {
            jTable.setShowHorizontalLines(visible);
            jTable.setIntercellSpacing(intercellSpacing);
        }
    }

    /** Reports the visibility of the horizontal lines of the DBTable.*/
    public boolean isHorizontalLinesVisible() {
        return horizontalLinesVisible; //1 viewStructure.tableView.isHorizontalLinesVisible();
    }

    /** Adjusts the visibility of the vertical lines of the DBTable.*/
    public void setVerticalLinesVisible(boolean visible) {
        if (visible == verticalLinesVisible)
            return;
        verticalLinesVisible = visible;
//1        viewStructure.tableView.setVerticalLinesVisible(visible);
//        dbTable.UIProperties.put(VERT_LINES_VISIBLE_POS, new Boolean(vertLinesVisible));
        setModified(true);
//            System.out.println("Setting modified 3");
        intercellSpacing.width = (verticalLinesVisible)? 1:0;
        if (jTable != null) {
            jTable.setShowVerticalLines(visible);
            jTable.setIntercellSpacing(intercellSpacing);
        }
    }

    /** Reports the visibility of the vertical lines of the DBTable.*/
    public boolean isVerticalLinesVisible() {
        return verticalLinesVisible;
//1        return viewStructure.tableView.isVerticalLinesVisible();
    }


    public void setTableFont(Font f) {
        if (f.equals(tableFont))
            return;

        tableFont = f;
//1        viewStructure.tableView.setTableFont(f);
//            dbTable.UIProperties.put(TABLE_FONT_POS, tableFont);
//            System.out.println("Setting modified 4");
        setModified(true);
//        System.out.println("Setting font to: " + f);
        for (int i=0;i<tableColumns.size();i++) {
            tableColumns.get(i).renderer.setFont(f);
            if (tableColumns.get(i).editorComponent != null)
                tableColumns.get(i).editorComponent.setFont(f);
/*cf            ColumnRendererEditor cr = tableColumns.get(i).colRendererEditor;
            cr.renderer.setFont(f);
            if (cr.editorComponent != null)
                cr.editorComponent.setFont(f);
*/
/*1.0.1            ((ColumnRendererEditor) colRendererEditors.get(i)).rendererComponent.setFont(f);
((ColumnRendererEditor) colRendererEditors.get(i)).editorComponent.setFont(f);
*/
        }
    }


    public Font getTableFont() {
        return tableFont;
//1        return viewStructure.tableView.getTableFont();
    }

    /** Sets whether both the field and the record of the active cell should be activated. */
    public void setSimultaneousFieldRecordActivation(boolean simultaneous) {
        if (simultaneous == simultaneousFieldRecordActivation)
            return;
        simultaneousFieldRecordActivation = simultaneous;
//1        viewStructure.tableView.setSimultaneousFieldRecordActivation(simultaneous);
//        dbTable.UIProperties.put(SIMULTANEOUS_FIELD_RECORD_SELECTION_POS, new Boolean(simultaneousFieldRecordActivation));
        setModified(true);
        jTable.setColumnSelectionAllowed(simultaneous);
        int[] selectedRows = jTable.getSelectedRows();
        for (int k=0; k<selectedRows.length; k++)
            jTable.getSelectionModel().addSelectionInterval(selectedRows[k], selectedRows[k]);

        if (jTable.getColumnSelectionAllowed())
            selectedHeaderBackgroundColor = SELECTED_HEADER_BGRD_GREEN;
        else
            selectedHeaderBackgroundColor = headerBackground;
        updateColumnHeaderSelection();
        jTable.getTableHeader().repaint();
        jTable.repaint();
    }

    /** Reports if both the record the field are activated, when a cell becomes active. */
    public boolean isSimultaneousFieldRecordActivation() {
        return simultaneousFieldRecordActivation; //1 viewStructure.tableView.isSimultaneousFieldRecordActivation();
    }

    /** Determines if the data type icons of the column header are visible, i.e. the headers are expanded. */
    public void setHeaderIconsVisible(boolean visible) {
        if (visible == headerIconsVisible)
            return;
        headerIconsVisible = visible;
        setModified(true);
//            System.out.println("Setting modified 6");
        displayHeaderIcons(visible);
    }

    /** Reports if the data type icons of the column header are visible, i.e. the headers are expanded. */
    public boolean isHeaderIconsVisible() {
        return headerIconsVisible; //1 viewStructure.tableView.isHeaderIconsVisible();
    }

    /** Determines if the non-editable fields are highlightes. */
    public void setNonEditableFieldsHighlighted(boolean highlight) {
        if (highlight == nonEditableFieldsHighlighted)
            return;
        nonEditableFieldsHighlighted = highlight;

//1        viewStructure.tableView.setNonEditableFieldsHighlighted(highlight);
//    	dbTable.UIProperties.put(NON_EDITABLE_HIGHLIGHTED_POS, new Boolean(highlight));
        setModified(true);
    }

    /** Reports if the non-editable fields are highlighted. */
    public boolean isNonEditableFieldsHighlighted() {
        return nonEditableFieldsHighlighted;
//1        return viewStructure.tableView.isNonEditableFieldsHighlighted();
    }

    void highlightNonEditableFields(boolean on) {
//1        Color backgroundColor = viewStructure.tableView.getBackgroundColor();
//1        Color highlightColor = viewStructure.tableView.getHighlightColor();
            TableColumnModel tcm = jTable.getColumnModel();
            for (int i=0; i<tcm.getColumnCount(); i++) {
                AbstractTableField field = getTableField(i);
                if (!field.isEditable()) continue;
                if (on) {
                    ((DBCellRenderer) tcm.getColumn(i).getCellRenderer()).setBackgroundColor(highlightColor);
                }else{
                    ((DBCellRenderer) tcm.getColumn(i).getCellRenderer()).setBackgroundColor(backgroundColor);
                }
            }
/*1            for (int i=0; i<tableColumns.size(); i++) {
                if (((TableColumn) tableColumns.get(i)).getCellEditor() == null) {
                    try{
                        AbstractTableField fld = table.getTableField(((TableColumn) tableColumns.get(i)).getIdentifier().toString());
                        if (fld.isEditable() && table.isDataChangeAllowed())
                            ((DBCellRenderer) ((TableColumn) tableColumns.get(i)).getCellRenderer()).setBackgroundColor(backgroundColor);
                        else
                            ((DBCellRenderer) ((TableColumn) tableColumns.get(i)).getCellRenderer()).setBackgroundColor(highlightColor);
                    }catch (InvalidFieldNameException exc) {
                        System.out.println("Serious inconsistency error in DBTable highlightNonEditableFieldsBox(): (1)");
                    }
                }else
                    ((DBCellRenderer) ((TableColumn) tableColumns.get(i)).getCellRenderer()).setBackgroundColor(backgroundColor);

            }
        }else{
            for (int i=0; i<tableColumns.size(); i++) {
//                if (((TableColumn) tableColumns.get(i)).getCellEditor() == null) {
//1.0.1                    ((DefaultCellRenderer) ((TableColumn) tableColumns.get(i)).getCellRenderer()).setBackgroundColor(backgroundColor);
                ((DBCellRenderer) ((TableColumn) tableColumns.get(i)).getCellRenderer()).setBackgroundColor(backgroundColor);

            }
        }
1*/
        jTable.repaint();
    }

    public void refreshField(TableColumn col) {
        if (jTable.getRowCount() == 0)
            return;

        int upperCorner = scrollpane.getViewport().getViewPosition().y;
        int portHeight = scrollpane.getViewport().getExtentSize().height;
        int lowerCorner = upperCorner + portHeight;
        int topRow = jTable.rowAtPoint(new Point(0, upperCorner));
        int lastRow = jTable.rowAtPoint(new Point(0, lowerCorner));
/* If the rows in the jTable are not enough to fill the view port, then
* lastRow == -1. In this case set lastRow to be the lastRow of the jTable.
*/
        if (lastRow == -1)
            lastRow = jTable.getRowCount();
//        System.out.println("topRow: " + topRow + ", lastRow: " + lastRow);
        if (topRow > lastRow) return;

        Rectangle rectToRefresh = null;
        int colIndex = jTable.convertColumnIndexToView(tableModel.getColumnIndex(col.getIdentifier()));
        for (int i=topRow; i<=lastRow; i++) {
//            System.out.println("Cell: " + i + ", " + colIndex + ", rect: " + jTable.getCellRect(i, colIndex, false));
            if (rectToRefresh == null)
                rectToRefresh = jTable.getCellRect(i, colIndex, false);
            else
                rectToRefresh.add(jTable.getCellRect(i, colIndex, false));
        }

//        System.out.println("rectToRefresh: " + rectToRefresh);
        jTable.repaint(rectToRefresh);

//        jTable.paintImmediately(rectToRefresh);
    }


    public void refreshField(int index) {
        if (jTable.getRowCount() == 0)
            return;

        int upperCorner = scrollpane.getViewport().getViewPosition().y;
        int portHeight = scrollpane.getViewport().getExtentSize().height;
        int lowerCorner = upperCorner + portHeight;
        int topRow = jTable.rowAtPoint(new Point(0, upperCorner));
        int lastRow = jTable.rowAtPoint(new Point(0, lowerCorner));
/* If the rows in the jTable are not enough to fill the view port, then
* lastRow == -1. In this case set lastRow to be the lastRow of the jTable.
*/
        if (lastRow == -1)
            lastRow = jTable.getRowCount();
//        System.out.println("topRow: " + topRow + ", lastRow: " + lastRow);
        if (topRow > lastRow) return;

        Rectangle rectToRefresh = null;
//        System.out.println("Refreshing field at: " + index);
        for (int i=topRow; i<=lastRow; i++) {
            if (rectToRefresh == null)
                rectToRefresh = jTable.getCellRect(i, index, false);
            else
                rectToRefresh.add(jTable.getCellRect(i, index, false));
        }

//        System.out.println("rectToRefresh: " + rectToRefresh);
        jTable.repaint(rectToRefresh);
//        jTable.paintImmediately(rectToRefresh);
    }


    public void setDataTypeColors(Color integerClr, Color floatClr, Color doubleClr, Color stringClr,
                                  Color booleanClr, Color dateClr, Color timeClr, Color urlClr) {

//1        viewStructure.tableView.setIntegerColor(integerClr);
        integerColor = integerClr;
        floatColor = floatClr;
//            dbTable.UIProperties.put(INTEGER_COLOR_POS, integerColor);
//1            viewStructure.tableView.setDoubleColor(doubleClr);
//            dbTable.UIProperties.put(FLOAT_COLOR_POS, doubleColor);
//1            viewStructure.tableView.setStringColor(stringClr);
        stringColor = stringClr;
//            dbTable.UIProperties.put(STRING_COLOR_POS, stringColor);
//1            viewStructure.tableView.setBooleanColor(booleanClr);
        booleanColor = booleanClr;
//            dbTable.UIProperties.put(BOOLEAN_COLOR_POS, booleanColor);
//1            viewStructure.tableView.setDateColor(dateClr);
        dateColor = dateClr;
//            dbTable.UIProperties.put(DATE_COLOR_POS, dateColor);
//1            viewStructure.tableView.setTimeColor(timeClr);
        timeColor = timeClr;
//            dbTable.UIProperties.put(TIME_COLOR_POS, timeColor);
//            viewStructure.tableView.setUrlColor(urlClr);
        urlColor = urlClr;
//            dbTable.UIProperties.put(URL_COLOR_POS, urlColor);
//            System.out.println("Setting modified 7");
        setModified(true);

        if (table == null) return;
        for (int i=0; i<tableColumns.size(); i++) {
//tci            fldType = ((AbstractTableField) tableFields.at(((Integer) tableColumnsIndex.at(i)).intValue())).getFieldType().getName();
            AbstractDBTableColumn column = tableColumns.get(i);
            Class fldType = column.getDataType(); //1 ((AbstractTableField) tableFields.get(((TableColumn) tableColumns.get(i)).getModelIndex())).getDataType().getName();
            if (fldType.equals(IntegerTableField.DATA_TYPE))
                ((DBCellRenderer) column.tableColumn.getCellRenderer()).setForegroundColor(integerColor/*viewStructure.tableView.getIntegerColor()*/);
            else if (fldType.equals(DoubleTableField.DATA_TYPE))
                ((DBCellRenderer) column.tableColumn.getCellRenderer()).setForegroundColor(doubleColor/*viewStructure.tableView.getDoubleColor()*/);
            else if (fldType.equals(FloatTableField.DATA_TYPE))
                ((DBCellRenderer) column.tableColumn.getCellRenderer()).setForegroundColor(floatColor/*viewStructure.tableView.getDoubleColor()*/);
            else if (fldType.equals(StringTableField.DATA_TYPE))
                ((DBCellRenderer) column.tableColumn.getCellRenderer()).setForegroundColor(stringColor/*viewStructure.tableView.getStringColor()*/);
            else if (fldType.equals(BooleanTableField.DATA_TYPE))
                ((DBCellRenderer) column.tableColumn.getCellRenderer()).setForegroundColor(booleanColor/*viewStructure.tableView.getBooleanColor()*/);
            else if (fldType.equals(DateTableField.DATA_TYPE)) //"java.util.Date")) {
//tci                if (((AbstractTableField) tableFields.at(((Integer) tableColumnsIndex.at(i)).intValue())).isDate())
//                if (((TableField) tableFields.at(((TableColumn) tableColumns.get(i)).getModelIndex())).isDate())
                ((DBCellRenderer) column.tableColumn.getCellRenderer()).setForegroundColor(dateColor/*viewStructure.tableView.getDateColor()*/);
            else if (fldType.equals(TimeTableField.DATA_TYPE)) //"java.util.Date")) {
//                else
                ((DBCellRenderer) column.tableColumn.getCellRenderer()).setForegroundColor(timeColor/*viewStructure.tableView.getTimeColor()*/);
            else if (fldType.equals(URLTableField.DATA_TYPE))
                ((DBCellRenderer) column.tableColumn.getCellRenderer()).setForegroundColor(urlColor/*viewStructure.tableView.getUrlColor()*/);

        }
    }

    /** Sets the foreground color for the columns of the specified data type. */
    protected void setDataTypeColor(Class dataType, Color color) {
        if (color == null) return;
        if (dataType.equals(DoubleTableField.DATA_TYPE)) { //bundle.getString("Number"))) {
            if (color.equals(doubleColor/*viewStructure.tableView.getDoubleColor()*/))
                return;
            else{
                doubleColor = color;
//1                viewStructure.tableView.setDoubleColor(color);
//                dbTable.UIProperties.put(FLOAT_COLOR_POS, doubleColor);
            }
        }else if (dataType.equals(StringTableField.DATA_TYPE)) { //bundle.getString("Alphanumeric"))) {
            if (color.equals(stringColor/*viewStructure.tableView.getStringColor()*/))
                return;
            else{
                stringColor = color;
//1                viewStructure.tableView.setStringColor(color);
//                dbTable.UIProperties.put(STRING_COLOR_POS, stringColor);
            }
        }else if (dataType.equals(BooleanTableField.DATA_TYPE)) { //bundle.getString("Boolean"))) {
            if (color.equals(booleanColor/*viewStructure.tableView.getBooleanColor()*/))
                return;
            else{
                booleanColor = color;
//1                viewStructure.tableView.setBooleanColor(color);
//                dbTable.UIProperties.put(BOOLEAN_COLOR_POS, booleanColor);
            }
        }else if (dataType.equals(DateTableField.DATA_TYPE)) { //bundle.getString("Date"))) {
            if (color.equals(dateColor/*viewStructure.tableView.getDateColor()*/))
                return;
            else{
                dateColor = color;
//                viewStructure.tableView.setDateColor(color);
//                dbTable.UIProperties.put(DATE_COLOR_POS, dateColor);
            }
        }else if (dataType.equals(TimeTableField.DATA_TYPE)) {
            if (color.equals(timeColor/*viewStructure.tableView.getTimeColor()*/))
                return;
            else{
                timeColor = color;
//1                viewStructure.tableView.setTimeColor(color);
//                dbTable.UIProperties.put(TIME_COLOR_POS, dateColor);
            }
        }else if (dataType.equals(URLTableField.DATA_TYPE)) {
            if (color.equals(urlColor/*viewStructure.tableView.getUrlColor()*/))
                return;
            else{
                urlColor = color;
//1                viewStructure.tableView.setUrlColor(color);
//                dbTable.UIProperties.put(URL_COLOR_POS, urlColor);
            }
        }else if (dataType.equals(IntegerTableField.DATA_TYPE)) {
            if (color.equals(integerColor))
                return;
            else
                integerColor = color;
        }else if (dataType.equals(FloatTableField.DATA_TYPE)) {
            if (color.equals(floatColor))
                return;
            else
                floatColor = color;
        }
        setModified(true);

        String fldType;
        AbstractTableField fld;
        for (int i=0; i<tableColumns.size(); i++) {
            AbstractDBTableColumn column = tableColumns.get(i);
            fld = column.tableField;
            if (AbstractTableField.localizedNameForDataType(fld).equals(dataType))
                ((DBCellRenderer) column.tableColumn.getCellRenderer()).setForegroundColor(color);
        }
/*cf        for (int i=0; i<tableColumns.size(); i++) {
            fld = (AbstractTableField) tableFields.get(((TableColumn) tableColumns.get(i)).getModelIndex());
//            System.out.println(fld.getName() + ", " + TableField.localizedNameForDataType(fld) + ", " + dataType);
            if (AbstractTableField.localizedNameForDataType(fld).equals(dataType)) {
                ((DBCellRenderer) ((TableColumn) tableColumns.get(i)).getCellRenderer()).setForegroundColor(color);
            }
        }
*/
    }

    /** Returns the foreground color used for the columns of the specified data type.*/
    public Color getDataTypeColor(Class dataType) {
        if (dataType.equals(IntegerTableField.DATA_TYPE))
            return integerColor;
        if (dataType.equals(FloatTableField.DATA_TYPE))
            return floatColor;
        if (dataType.equals(DoubleTableField.DATA_TYPE))
            return doubleColor;
        if (dataType.equals(StringTableField.DATA_TYPE))
            return stringColor;
        if (dataType.equals(BooleanTableField.DATA_TYPE))
            return booleanColor;
        if (dataType.equals(DateTableField.DATA_TYPE))
            return dateColor;
        if (dataType.equals(TimeTableField.DATA_TYPE))
            return timeColor;
        if (dataType.equals(URLTableField.DATA_TYPE))
            return urlColor;
        return null;
    }

    /** Sets the background color of the cells of the DBTable.*/
    public void setBackgroundColor(Color color) {
        if (color == null) return;
        if (color.equals(backgroundColor))
            return;
        backgroundColor = color;
//1        viewStructure.tableView.setBackgroundColor(color);
//        dbTable.UIProperties.put(BACKGROUND_COLOR_POS, backgroundColor);
//            System.out.println("Setting modified 8");
        setModified(true);
        if (jTable != null) {
            TableColumnModel tcm = jTable.getColumnModel();
            for (int i=0; i<tcm.getColumnCount(); i++) {
                AbstractTableField field = getTableField(i);
                if (!field.isEditable()) continue;
                ((DBCellRenderer) tcm.getColumn(i).getCellRenderer()).setBackgroundColor(backgroundColor);
            }
            jTable.repaint();
        }
//        paintNonEditableColumns();
    }

    /** Returns the background color of the cells of the DBTable.*/
    public Color getBackgroundColor() {
        return backgroundColor; //1 viewStructure.tableView.getBackgroundColor();
    }


    /** Sets the background color of the selected records of the DBTable. */
    public void setSelectionBackground(Color color) {
        if (color == null) return;
        if (color.equals(selectionBackground))
            return;

        selectionBackground = color;
        setModified(true);
    }

    /** Returns the background color of the selected records of the DBTable. */
    public Color getSelectionBackground() {
        return selectionBackground;
    }

    /** Sets the foreround color of the selected records of the DBTable. */
    public void setSelectionForeground(Color color) {
        if (color == null) return;
        if (color.equals(selectionForeground))
            return;

        selectionForeground = color;
        setModified(true);
    }

    /** Returns the foreground color of the selected records of the DBTable. */
    public Color getSelectionForeground() {
        return selectionForeground;
    }

    /** Sets the color with which the background of the active record is painted.*/
    public void setActiveRecordColor(Color color) {
        if (color == null) return;
        if (color.equals(activeRecordColor))
            return;
        activeRecordColor = color;
//1        viewStructure.tableView.setActiveRecordColor(color);
//        dbTable.UIProperties.put(ACTIVE_RECORD_COLOR_POS, activeRecordColor);
        // Set the color of the line border of the active cell
        activeCellBorder = new LineBorder(activeRecordColor);
        setModified(true);
//            System.out.println("Setting modified 9");
    }

    /** Returns the color with which the background of the active record is painted.*/
    public Color getActiveRecordColor() {
        return activeRecordColor; //1 viewStructure.tableView.getActiveRecordColor();
    }


    /** Set the color of the DBTable's grid. */
    public void setGridColor(Color color) {
        if (color == null) return;
        if (color.equals(gridColor))
            return;

        gridColor = color;
//1        viewStructure.tableView.setGridColor(color);
//            dbTable.UIProperties.put(GRID_COLOR_POS, gridColor);
//            System.out.println("Setting modified 10");
        setModified(true);
        if (jTable != null)
            jTable.setGridColor(color);
    }

    /** Returns the color of the DBTable's grid.*/
    public Color getGridColor() {
        return gridColor;
//1        return viewStructure.tableView.getGridColor();
    }

    /** Sets the background color the non-editable fields have, when the non editable fields are highlighted in
     *  the DBTable.
     */
    public void setHighlightColor(Color color) {
        if (color == null) return;
        if (color.equals(getHighlightColor()))
            return;

        highlightColor = color;
//1        viewStructure.tableView.setHighlightColor(color);
//        dbTable.UIProperties.put(HIGHLIGHT_COLOR_POS, highlightColor);
        setModified(true);
        paintNonEditableColumns();
//            System.out.println("Setting modified 11");
    }

    /** Returns the background color of the non-editable fields of the DBTable, when they are highlighted. */
    public Color getHighlightColor() {
        return highlightColor;
//1        return viewStructure.tableView.getHighlightColor();
    }


    protected void paintNonEditableColumns() {
        if (nonEditableFieldsHighlighted/*viewStructure.tableView.isNonEditableFieldsHighlighted()*/) {
            Color hColor = highlightColor;
            for (int i=0; i<tableColumns.size(); i++) {
                if (tableColumns.get(i).tableColumn.getCellEditor() == null)
                    ((DBCellRenderer) tableColumns.get(i).tableColumn.getCellRenderer()).setBackgroundColor(hColor);
            }
        }
    }

    /** Detrmines if the row backround will be painted with a solid color or with two colors. When this property is
     *  disabled, the <code>backgroundColor</code> is used to paint the background of the rows which are neither selected
     *  not active. When the property is enabled, the <code>oddRowColor</code> and <code>evenRowColor</code> are used
     *  to paint the background -depending on the series- of the rows which are neither selected nor active.
     */
    public void setTwoColorBackgroundEnabled(boolean enabled) {
        if (twoColorBackgroundEnabled == enabled) return;
        twoColorBackgroundEnabled = enabled;
        if (jTable != null)
            jTable.repaint();
        setModified(true);
    }

    /** Reports if one solid color or two colors are used to paint the background of the rows which are neither
     *  selected nor active.
     *  @see #setTwoColorBackgroundEnabled(boolean)
     */
    public boolean isTwoColorBackgroundEnabled() {
        return twoColorBackgroundEnabled;
    }

    /** Returns the background color of the odd rows, when two colors are used for the row background.
     *  @see #setTwoColorBackgroundEnabled(boolean)
     */
    public Color getOddRowColor() {
        return oddRowColor;
    }

    /** Sets the background color of the odd rows. The color is used only when two-colors row background is enabled.
     *  @see #setTwoColorBackgroundEnabled(boolean)
     */
    public void setOddRowColor(Color oddRowColor) {
        if (oddRowColor == null) return;
        if (oddRowColor.equals(this.oddRowColor)) return;
        this.oddRowColor = oddRowColor;
        if (jTable != null)
            jTable.repaint();
        setModified(true);
    }

    /** Returns the background color of the even rows, when two colors are used for the row background.
     *  @see #setTwoColorBackgroundEnabled(boolean)
     */
    public Color getEvenRowColor() {
        return evenRowColor;
    }

    /** Sets the background color of the even rows. The color is used only when two-colors row background is enabled.
     *  @see #setTwoColorBackgroundEnabled(boolean)
     */
    public void setEvenRowColor(Color evenRowColor) {
        if (evenRowColor == null) return;
        if (evenRowColor.equals(this.evenRowColor)) return;
        this.evenRowColor = evenRowColor;
        if (jTable != null)
            jTable.repaint();
        setModified(true);
    }

/*    public void setExponentialNumberFormatUsed(boolean used) {
if (used != isExponentialNumberFormatUsed()) {
exponentialNumberFormatUsed = used;
dbTable.UIProperties.put(EXPONENTIAL_NUMBER_FORMAT_USED_POS, new Boolean(exponentialNumberFormatUsed));
dbTable.setModified();
}
}


public boolean isExponentialNumberFormatUsed() {
return exponentialNumberFormatUsed;
}
*/

/*    protected void setTable(Table table) {
this.dbTable = table;
}
*/

    public void refresh() {
        Rectangle rect = getVisibleRect();
        repaint(rect);
//        paintImmediately(rect);
    }


    protected void displayHeaderIcons(boolean visible) {
        if (table == null) return;
        if (visible) {
            AbstractTableField f;
            Class fieldType;
//            boolean isDate;
            HeaderRenderer header = null;
            TableColumn col;
            TableCellRenderer cr;

            for (int i=0; i<tableColumns.size(); i++) {
                AbstractDBTableColumn column = tableColumns.get(i);
                col = column.tableColumn; //cf (TableColumn) tableColumns.get(i);
                header = (HeaderRenderer) col.getHeaderRenderer();

//                cr = (TableCellRenderer) col.getCellRenderer();

//tci                f = (TableField) tableFields.at(((Integer) tableColumnsIndex.at(i)).intValue());
/* Array tableFields is not re-arranged everytime the order of the jTable's fields
* changes, like Array "tableColumns" does. The correspondence between the two Arrays
* is based on each column's (TableColumn, actually) "modelIndex" API value.
*/
                f = column.tableField; //cf (AbstractTableField) tableFields.get(col.getModelIndex());
                header.setIcon(f.getDataType(), !table.isPartOfTableKey(f) /*!f.isKey()*/, /*f.isDate(),*/ this);
            }
        }else{
            HeaderRenderer hr;
            for (int i=0; i<tableColumns.size(); i++) {
                AbstractDBTableColumn column = tableColumns.get(i);
                hr = (HeaderRenderer) column.tableColumn.getHeaderRenderer();
                hr.setIcon(null);
//                hr.resizeAndRepaint();
           }
        }
        jTable.getTableHeader().resizeAndRepaint();
    }


    public boolean addColumn(String fieldName, boolean isKey, boolean isCalculated, Class type,
                             boolean isEditable, boolean isRemovable, String formula) {
//        DBTableModel tm = (DBTableModel) jTable.getModel();
//        System.out.println("TableModel column count: " + tm.getColumnCount());

        try{
            if (isCalculated)
                table.addCalculatedField(fieldName,
                        formula,
//                                          isKey,
                        isRemovable,
                        false);
            else
                table.addField(fieldName,
                        type,
                        isEditable,
//                                isKey,
                        isRemovable,
                        false);

        }catch (InvalidFieldNameException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE); return false;}
        catch (InvalidKeyFieldException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE); return false;}
        catch (InvalidFieldTypeException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE); return false;}
        catch (InvalidFormulaException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE); return false;}
        catch (IllegalCalculatedFieldException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE); return false;}
        catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE); return false;}

//        dbTable.printTable();
//        System.out.println(fieldName + " : " + tm.getColumnCount() + ", " + (dbTable.getFieldCount()-1));
        AbstractTableField newfld;

//        try{
//            newfld = dbTable.getTableField(fieldName);
//        }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in DBTable addColumn(): (1)"); return false;}

//        UIAddField(newfld);
        return true;
    }


    public boolean removeColumn(String fieldName) {
        try{
//            System.out.println("removeColumn1");
            table.removeField(fieldName);
//            System.out.println("removeColumn2");
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in DBTable removeColumn() : (1)"); return false;}
        catch (TableNotExpandableException e) {ESlateOptionPane.showMessageDialog(this, e.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE); return false;}
        catch (CalculatedFieldExistsException e) {ESlateOptionPane.showMessageDialog(this, e.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE); return false;}
        catch (FieldNotRemovableException e) {ESlateOptionPane.showMessageDialog(this, e.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE); return false;}
        catch (AttributeLockedException e) {ESlateOptionPane.showMessageDialog(this, e.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE); return false;}

        return true;
    }


    public void changeFieldType(AbstractTableField f, Class dataType, boolean promptUser) {
        Class previousDataType = f.getDataType();
        String existingType = f.getDataType().getName().substring(f.getDataType().getName().lastIndexOf('.')+1);
        if (existingType.equals("CImageIcon"))
            existingType = "Image";
        if (!dataType.equals(existingType)) {
            AbstractTableField newField = null;
            try{
                newField = table.changeFieldType(f.getName(), dataType, false);
            }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in DBTable changeFieldType: (2)"); return;}

            catch (InvalidFieldTypeException e1) {ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
            catch (FieldIsKeyException e1) {ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
            catch (InvalidTypeConversionException e1) {ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
            catch (FieldNotEditableException e1) {ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
            catch (DependingCalcFieldsException e1) {ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
            catch (AttributeLockedException e1) {ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
            catch (DataLossException e1) {
                if (promptUser) {
                    Object[] options2 = { bundle.getString("OK"), bundle.getString("Cancel") };
                    if (!(JOptionPane.showOptionDialog(this, bundle.getString("DBTableMsg1") + f.getName() + bundle.getString("DBTableMsg2"), bundle.getString("Warning"),
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options2, options2[1]) == JOptionPane.OK_OPTION))
                        return;
                }
                try{
                    newField = table.changeFieldType(f.getName(), dataType, true);
                }catch (InvalidFieldNameException e2) {System.out.println("Serious inconsistency error in DBTable changeFieldType: (3)"); return;}
                catch (InvalidFieldTypeException e2) {System.out.println("Serious inconsistency error in DBTable changeFieldType: (4)"); return;}
                catch (FieldIsKeyException e2) {System.out.println("Serious inconsistency error in DBTable changeFieldType: (5))"); return;}
                catch (InvalidTypeConversionException e2) {System.out.println("Serious inconsistency error in DBTable changeFieldType: (6)"); return;}
                catch (FieldNotEditableException e2) {System.out.println("Serious inconsistency error in DBTable changeFieldType: (7)"); return;}
                catch (DependingCalcFieldsException e2) {ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
                catch (DataLossException e2) {return;}
                catch (AttributeLockedException e2) {ESlateOptionPane.showMessageDialog(this, e2.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
            }
            // The new field which was created by changeFieldType() has to be updated in the proper AbstractDBTableColumn
            for (int i=0; i<tableColumns.size(); i++) {
                if (tableColumns.get(i).tableField == f) {
                    AbstractDBTableColumn column = tableColumns.get(i);
                    tableColumns.set(i, AbstractDBTableColumn.createDBTableColumn(this, column.tableColumn, newField));
//                    tableColumns.get(i).tableField = newField;
                    break;
                }
            }
        }
    }

    protected boolean commitNewRecord() {
        if (!table.getRecordEntryStructure().isRecordAdditionPending())
            return true;
        try{
            table.getRecordEntryStructure().commitNewRecord(false);
            setClickCountToStartEditing(2);
        }catch (NoFieldsInTableException exc) {
            ESlateOptionPane.showMessageDialog(DBTable.this, exc.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            editCellAt(table.getRecordEntryStructure().getPendingRecordIndex(), 0);
            setActiveRow(-1);
            jTable.revalidate();
            return false;
        }catch (DuplicateKeyException exc) {
            ESlateOptionPane.showMessageDialog(DBTable.this, exc.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            editCellAt(table.getRecordEntryStructure().getPendingRecordIndex(), 0);
            setActiveRow(-1);
            jTable.revalidate();
            return false;
        }
        return true;
    }

    void cancelRecordAddition() {
        if (!table.getRecordEntryStructure().isRecordAdditionPending())
            return;
        if (isEditing())
            cancelCellEditing();
        table.getRecordEntryStructure().cancelRecordAddition();
        setClickCountToStartEditing(2);
        setActiveRow(tableModel.getRowCount()-1);
        jTable.revalidate();
        jTable.repaint();
    }

    /**
     * Sets the number of mouse clicks which start cell editing. This number is always 2, unless a new record is being
     * interactively added, in which case it is 1.
     * @param numOfClicks The number of mouse clicks which start cell editing.
     */
    private void setClickCountToStartEditing(int numOfClicks) {
        for (int i=0; i<tableColumns.size(); i++) {
            DefaultCellEditor editor = tableColumns.get(i).editor;
            if (editor != null) editor.setClickCountToStart(numOfClicks);
/*cf            ColumnRendererEditor cre = tableColumns.get(i).colRendererEditor;
            if (cre.editor != null)
                cre.editor.setClickCountToStart(numOfClicks);
*/
        }
    }

    protected void newRecord() { //throws Exception {
        if (!commitNewRecord())
            return;
        int currActiveRecord = table.getActiveRecord();
        setActiveRow(-1);
        try{
            tableModel.addRow();
//            e.printStackTrace();
//            System.out.println(e.getClass().getName() + ", " + e.getMessage());
//            throw new Exception(e.getMessage());
        }catch (Exception e1) {
            ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            setActiveRow(currActiveRecord);
            return;
        }

        if (table.hasKey()) {
            removeRecordAction.setEnabled(false);
        }
//        pendingRecordIndex = table.getRecordCount()-1;

        /* Programmatically start editing the cell of the new record and of the first in the jTable's
        * view editable column. If no column in the jTable is editable, then simply insert a new
        * empty record at the end of the jTable, which cannot be edited.
        */
        int editColumn = -1;
        TableColumn col;
        for (int i=0; i<tableColumns.size(); i++) {
            col = jTable.getColumn(jTable.getColumnName(i));
            if (((DBTableModel) jTable.getModel()).getField(col.getModelIndex()).isEditable()) {
                editColumn = i;
                break;
            }
        }

        /* Change the background color for the inserted record.
        */
        if (jTable.getColumnSelectionAllowed() != simultaneousFieldRecordActivation)
            jTable.setColumnSelectionAllowed(false);
        for (int m=0; m<colSelectionStatus.size(); m++)
            colSelectionStatus.set(m, Boolean.FALSE);
        colSelectionStatus.set(editColumn, Boolean.TRUE);
        jTable.getColumnModel().getSelectionModel().setSelectionInterval(editColumn, editColumn);

        int pendingRecIndex = table.getRecordEntryStructure().getPendingRecordIndex();
//System.out.println("Editing cell at: " + pendingRecIndex + ", " + editColumn);
        if (editColumn != -1) {
            editCellAt(pendingRecIndex, editColumn); // == 0)? 0 : dbTable.getRecordCount());
            /* If the field is of Boolean data type, show the cell editor's pop-up
            */
        }
        jTable.repaint(jTable.getCellRect(pendingRecIndex, editColumn, true));

        setClickCountToStartEditing(1);
/*cf        for (int i=0; i<tableColumns.size(); i++) {
            ColumnRendererEditor cr = tableColumns.get(i).colRendererEditor;
            if (cr.editor != null)
                cr.editor.setClickCountToStart(1);
        }
*/
        scrollpane.validate();
        scrollpane.repaint(scrollpane.getVisibleRect());
        jTable.scrollRectToVisible(jTable.getCellRect(pendingRecIndex, 0, true)); //newfp.getBounds());
    }


    protected void moveSelectedRecordsToStart() {
/*        for (int i=0; i<recordIndex.length; i++)
System.out.print(recordIndex[i] + ", ");
System.out.println();
*/
//        activeRow = rowForRecord(dbTable.getActiveRecord());
        table.promoteSelectedRecords();

        if (activeRow != -1) {
            scrollToActiveRecord = false;
            UIUpdateActiveRecord();
            scrollToActiveRecord = true;
/*            int prevActiveRow = activeRow;
int rowToActivate = rowForRecord(dbTable.getActiveRecord());
dbComponent.setActiveRow(rowToActivate);
activeRow = rowToActivate;
System.out.println("prevActiveRow: " + prevActiveRow + ", activeRow: " + activeRow);
rowBar.resetActiveRow(prevActiveRow);
rowBar.drawActiveRow(rowBar.getGraphics(), false);
jTable.getSelectionModel().addSelectionInterval(activeRow, activeRow);
*/
        }
/*jTable.removeRowSelectionInterval(0, tableModel.getRowCount()-1);
jTable.addRowSelectionInterval(0, recIndices.length-1);
*/
        refresh();
    }

    protected void removeActiveRecord() {
        removeRecord(activeRow, false);
    }

    protected void removeSelectedRecords() {
        gr.cti.eslate.database.engine.IntBaseArrayDesc selectedRecords = table.getSelectedSubset();
//        System.out.println("DBTable selected: " + dbTable.getSelectedSubset());
//        System.out.println("DBTable unselected: " + dbTable.getUnSelectedSubset());

        for (int i=0; i<selectedRecords.size(); i++)
            selectedRecords.set(i, table.rowForRecord(selectedRecords.get(i))); //put(i, new Integer(dbTable.rowForRecord(((Integer) selectedRecords.at(i)).intValue())));

        /* The rowIndices array has to be sorted in ascending order.
        */
//        Sorting.sort(selectedRecords, new LessNumber());
        selectedRecords.sort(true);
        int[] rowIndices = new int[selectedRecords.size()];
        for (int i=0; i<selectedRecords.size(); i++)
            rowIndices[i] = selectedRecords.get(i); //((Integer) selectedRecords.at(i)).intValue();

        removeRecords(rowIndices);
        /* Adjusting the vertical scrollbar, to reflect the new size of the view of the
        * "scrollpane", after the records are deleted.
        */
/*            Dimension currViewSize = scrollpane.getViewport().getView().getSize();
currViewSize = new Dimension(currViewSize.width, currViewSize.height - ((rowIndices.length-1) * rowHeight));
scrollpane.getViewport().getView().setSize(currViewSize);
scrollpane.getViewport().getView().validate();
scrollpane.getViewport().paintImmediately(new Rectangle(scrollpane.getViewport().getViewSize().width, scrollpane.getViewport().getViewSize().height));

refresh();
*/
    }


    protected void removeRecords(int[] rowIndices) {
        for (int i=0; i<rowIndices.length-1; i++) {
            removeRecord(rowIndices[i]-i, true);
        }

        /* Remove the last of the rows in rowIndices array and mark this as the
        * last record removal.
        */
//        System.out.println("length: " + rowIndices.length +", row: " + rowIndices[rowIndices.length-1]);
        removeRecord(rowIndices[rowIndices.length-1]-(rowIndices.length-1), false);
    }


    /* Removes the record which is displayed at row with index rowIndex of the DBTable.
    */
    protected void removeRecord(int rowIndex, boolean isChanging) {
        System.out.println("Removing row: " + rowIndex);
        try{
            table.removeRecord(table.recordIndex.get(rowIndex), rowIndex, isChanging);
        }catch (TableNotExpandableException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error") + "1", JOptionPane.ERROR_MESSAGE);
            return;
        }
        catch (InvalidRecordIndexException e1) {
            System.out.println("Serious inconsistency error in DBTable removeRecord(): (1)");
            return;
        }
    }


    protected void UIRemoveRecord(int rowIndex, boolean isChanging) {
//        System.out.println("UIRemoveRecord rowIndex: " + rowIndex);
/*        System.out.println("rowIndex: " + rowIndex);
for (int i=0; i<recordIndex.length; i++)
System.out.print(recordIndex[i] +", ");
System.out.println();
*/
/*        int tmp[] = new int[dbTable.getRecordCount()];
for (int i=0; i<rowIndex; i++) {
if (dbTable.recordIndex[i] > dbTable.recordIndex[rowIndex])
tmp[i] = dbTable.recordIndex[i]-1;
else
tmp[i] = dbTable.recordIndex[i];
}
for (int i=rowIndex; i<tmp.length; i++) {
if (dbTable.recordIndex[i+1] > dbTable.recordIndex[rowIndex])
tmp[i] = dbTable.recordIndex[i+1]-1;
else
tmp[i] = dbTable.recordIndex[i+1];
}

dbTable.recordIndex = tmp;
*/
/*        for (int i=0; i<dbTable.recordIndex.length; i++)
System.out.print(dbTable.recordIndex[i] +", ");
System.out.println();
*/
        if (!isChanging) {
//            System.out.println("rowIndex: " + rowIndex + ", recordCount: " + dbTable.getRecordCount());
            if (rowIndex == table.getRecordCount())
                setActiveRow(table.getRecordCount()-1);
            else
                setActiveRow(rowIndex);

//            System.out.println("UIRemoveRecord: " + activeRow + ", table.getRecordCount(): " + table.getRecordCount() + ", table: " + table.getTitle());
            if (activeRow != -1) { //when no record is left in the jTable
                int activeRecordYPosition = jTable.getCellRect(activeRow, 0, true).y;

                int currentViewYPosition = scrollpane.getViewport().getViewPosition().y;
                if (activeRecordYPosition < currentViewYPosition ||
                        activeRecordYPosition > currentViewYPosition + scrollpane.getViewport().getExtentSize().height)
                    currentViewYPosition = activeRecordYPosition;

                int currentViewXPosition = scrollpane.getViewport().getViewPosition().x;
//                System.out.println("currentViewXPosition: " + currentViewXPosition + ", currentViewYPosition: " + currentViewYPosition);
                scrollpane.getViewport().setViewPosition(new Point(
                        currentViewXPosition,
                        currentViewYPosition
                ));
                scrollpane.getViewport().validate();
                scrollpane.getViewport().doLayout();
            }else{
                scrollpane.getViewport().validate();
                scrollpane.getViewport().doLayout();
            }
            rowBar.repaint();
        }
    }


    public void refreshScrollpane() {
        scrollpane.getViewport().validate();
        scrollpane.getViewport().doLayout();
        scrollpane.getViewport().repaint(scrollpane.getViewport().getVisibleRect());
//        scrollpane.getViewport().paintImmediately(scrollpane.getViewport().getVisibleRect());
        refresh();
    }


    protected void removeAllRecords() {
        if (table.getRecordCount() == 0)
            return;
        for (int i=table.recordIndex.size()-1; i>0; i--) {
            try{
                table.removeRecord(table.recordIndex.get(i), i, true);
            }catch (TableNotExpandableException e1) {
                ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            catch (InvalidRecordIndexException e1) {
                System.out.println("Serious inconsistency error in DBTable removeAllRecords(): (1) + record index: " + i);
                return;
            }
        }
        /* Remove the last record and mark this record removal as the last one.
        */
        try{
            table.removeRecord(table.recordIndex.get(0), 0, false);
        }catch (TableNotExpandableException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        catch (InvalidRecordIndexException e1) {
            System.out.println("Serious inconsistency error in DBTable removeAllRecords(): (2) + record index: 0");
            return;
        }
        table.recordIndex = null;
    }

    /* Removes the record with index 'recordIndex' in the Table.
    */
    protected void removeRecordAt(int recordIndex, boolean isChanging) {
//        System.out.println("Removing record: " + recordIndex);

        try{
            table.removeRecord(recordIndex, -1, isChanging);
        }catch (TableNotExpandableException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        catch (InvalidRecordIndexException e1) {
            System.out.println("Serious inconsistency error in DBTable removeRecord(): (1)");
            return;
        }
    }


    protected void invertSelection() {
        table.invertSelection();
        refresh();
    }


    public void sortOnField(String fieldName, boolean ascending, int fromRowIndex, int toRowIndex, boolean updateActiveRow) {
        try{
            table.sortOnField(fieldName, ascending, fromRowIndex, toRowIndex);
        }catch (InvalidFieldException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.getMessage(), bundle.getString("Warning"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (updateActiveRow && activeRow != -1) {
            UIUpdateActiveRecord();

            int activeRowYPosition = jTable.getCellRect(activeRow, 0, true).y;
            if (activeRowYPosition < scrollpane.getViewport().getViewPosition().y ||
                    activeRowYPosition > scrollpane.getViewport().getViewPosition().y + scrollpane.getViewport().getExtentSize().height - rowHeight/*viewStructure.tableView.getRowHeight()*/) {

                int newYPosition = activeRowYPosition - (scrollpane.getViewport().getExtentSize().height/2);
                if (newYPosition < 0)
                    newYPosition = 0;
                scrollpane.getViewport().setViewPosition(new Point(
                        scrollpane.getViewport().getViewPosition().x,
                        newYPosition
                ));
            }

            refresh();
        }
    }


    static Array arrayForNull = new Array();

    public void sortOnMultipleFields(TableFieldBaseArray fieldsToSortOn, BoolBaseArray ascending) throws InvalidFieldException {
        if (fieldsToSortOn.size() == 0)
            return;
        table.sortOnFields(fieldsToSortOn, ascending);


/*        String fldName;
        AbstractTableField fld;
        int lastRecordIndex = table.getRecordCount()-1;
//        System.out.println("fieldsToSortOn: " + fieldsToSortOn);
        for (int i=0; i<fieldsToSortOn.size(); i++) {
            fldName = (String) fieldsToSortOn.get(i);
//            try{
                fld = table.getTableField(fldName);
//            }catch (InvalidFieldNameException e) {System.out.println("Serious inconcistency error in DBTable sortOnMultipleFields() : (1)"); return;}
            fieldsToSortOn.set(i, fld);
        }

        sortOnField(((AbstractTableField) fieldsToSortOn.get(0)).getName(), ((Boolean) ascending.get(0)).booleanValue(), 0, lastRecordIndex, false);

//        fieldsToSortOn.remove(1);
        if (fieldsToSortOn.size() == 1) {
            refresh();
System.out.println("Returning here 1");
            return;
        }

        int fromRecordIndex = -1, toRecordIndex=0, fieldIndex, fieldIndex2;
        while (true) {
            try{
                fieldIndex = table.getFieldIndex(((AbstractTableField) fieldsToSortOn.get(0)).getName());
                fieldIndex2 = table.getFieldIndex(((AbstractTableField) fieldsToSortOn.get(1)).getName());
            }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in DBTable sortOnMultipleFields() : (2)"); return;}

            if (fromRecordIndex == -1)
                fromRecordIndex = 0;
            else{
                fromRecordIndex = toRecordIndex;
                if (fromRecordIndex+1 >= lastRecordIndex) {
//                    System.out.println("Breaking --> " + dbTable.riskyGetCell(fieldIndex, recordIndex[fromRecordIndex]));
                    break;
                }else
                    fromRecordIndex++;
            }
            toRecordIndex = fromRecordIndex+1;
//            System.out.println(dbTable.riskyGetCell(fieldIndex, recordIndex[fromRecordIndex]) + ", " + dbTable.riskyGetCell(fieldIndex, recordIndex[toRecordIndex]));
            Object o11, o22;
            o11 = table.riskyGetCell(fieldIndex, table.recordIndex.get(fromRecordIndex));
            o22 = table.riskyGetCell(fieldIndex, table.recordIndex.get(toRecordIndex));
            if (o11 == null)
                o11 = arrayForNull;
            if (o22 == null)
                o22 = arrayForNull;
//            System.out.println("o11: " + o11 + ", o22: " + o22);
            if (o11.equals(o22)) {
                if (toRecordIndex < lastRecordIndex) {
                    toRecordIndex++;
                    o22 = table.riskyGetCell(fieldIndex, table.recordIndex.get(toRecordIndex));

                    if (o22 == null)
                        o22 = arrayForNull;
//                    System.out.println("o22: " + o22);
                    while (toRecordIndex <= lastRecordIndex && o11.equals((o22 = table.riskyGetCell(fieldIndex, table.recordIndex.get(toRecordIndex))))) {
                        System.out.println("toRecordIndex: " + toRecordIndex);
                        toRecordIndex++;
                    }
                    toRecordIndex--;
                    System.out.println("Exiting with toRecordIndex: " + toRecordIndex);
                }else
                    break;
            }else{
                toRecordIndex--;
//                System.out.println("Continuing --> " + dbTable.riskyGetCell(fieldIndex, recordIndex[fromRecordIndex]));
                continue;
            }


            System.out.println("Sorting --> from " + table.riskyGetCell(fieldIndex, fromRecordIndex) + "  to  " + table.riskyGetCell(fieldIndex, toRecordIndex));
            System.out.println("Sorting --> from " + table.riskyGetCell(fieldIndex2, fromRecordIndex) + "  to  " + table.riskyGetCell(fieldIndex2, toRecordIndex));
            System.out.println("fromRecordIndex: " + fromRecordIndex + ", toRecordIndex: " + toRecordIndex);
            sortOnField(((AbstractTableField) fieldsToSortOn.get(1)).getName(), ((Boolean) ascending.get(1)).booleanValue(), fromRecordIndex, toRecordIndex, false);
//            sortOnField(((AbstractTableField) fieldsToSortOn.get(1)).getName(), ((Boolean) ascending.get(1)).booleanValue(), table.recordForRow(fromRecordIndex), table.recordForRow(toRecordIndex), false);

            if (fieldsToSortOn.size() > 2) {
                Object o1, o2;
                boolean nullAndDifferent;
                int fromRecordIndex2 = -1, toRecordIndex2=0, fieldIndex21, fieldIndex22;
                while (true) {
                    try{
                        fieldIndex21 = table.getFieldIndex(((AbstractTableField) fieldsToSortOn.get(1)).getName());
                        fieldIndex22 = table.getFieldIndex(((AbstractTableField) fieldsToSortOn.get(2)).getName());
                    }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in DBTable sortOnMultipleFields() : (3)"); return;}

                    if (fromRecordIndex2 == -1)
                        fromRecordIndex2 = fromRecordIndex;
                    else{
                        fromRecordIndex2 = toRecordIndex2;
                        if (fromRecordIndex2+1 >= lastRecordIndex) {
//                            System.out.println("Breaking --> " + dbTable.riskyGetCell(fieldIndex21, recordIndex[fromRecordIndex2]));
                            break;
                        }else
                            fromRecordIndex2++;
                    }
                    toRecordIndex2 = fromRecordIndex2+1;
//                    System.out.println(dbTable.riskyGetCell(fieldIndex21, recordIndex[fromRecordIndex2]) + ", " + dbTable.riskyGetCell(fieldIndex21, recordIndex[toRecordIndex2]));
                    o1 = table.riskyGetCell(fieldIndex21, table.recordIndex.get(fromRecordIndex2));
                    o2 = table.riskyGetCell(fieldIndex21, table.recordIndex.get(toRecordIndex2));
                    if (o1 == null)
                        o1 = arrayForNull;
                    if (o2 == null)
                        o2 = arrayForNull;

                    if (!o1.equals(o2)) {
                        toRecordIndex2--;
//                        System.out.println("Continuing --> " + dbTable.riskyGetCell(fieldIndex21, recordIndex[fromRecordIndex2]));
                        continue;
                    }else{
                        toRecordIndex2++;
                        while (toRecordIndex2 <= lastRecordIndex) {
                            o1 = table.riskyGetCell(fieldIndex21, table.recordIndex.get(fromRecordIndex2));
                            o2 = table.riskyGetCell(fieldIndex21, table.recordIndex.get(toRecordIndex2));
                            if (o1 == null)
                                o1 = arrayForNull;
                            if (o2 == null)
                                o2 = arrayForNull;
                            if (!o1.equals(o2))
                                break;
                            else
                                toRecordIndex2++;
                        }
                        toRecordIndex2--;
                    }


//                    System.out.println("Sorting --> from " + dbTable.riskyGetCell(fieldIndex22, recordIndex[fromRecordIndex2]) + "  to  " + dbTable.riskyGetCell(fieldIndex22, recordIndex[toRecordIndex2]));
                    sortOnField(((AbstractTableField) fieldsToSortOn.get(2)).getName(), ((Boolean) ascending.get(2)).booleanValue(), fromRecordIndex2, toRecordIndex2, false);
                }
            }
        }

        if (activeRow != -1) {
            UIUpdateActiveRecord();
*/
/*            int prevActiveRow = activeRow;
int rowToActivate = rowForRecord(dbTable.getActiveRecord());
dbComponent.setActiveRow(rowToActivate);
activeRow = rowToActivate;
System.out.println("prevActiveRow: " + prevActiveRow + ", activeRow: " + activeRow);
rowBar.resetActiveRow(prevActiveRow);
rowBar.drawActiveRow(rowBar.getGraphics(), false);
jTable.getSelectionModel().addSelectionInterval(activeRow, activeRow);
*/
/*            int activeRowYPosition = jTable.getCellRect(activeRow, 0, true).y;
            if (activeRowYPosition < scrollpane.getViewport().getViewPosition().y ||
                    activeRowYPosition > scrollpane.getViewport().getViewPosition().y + scrollpane.getViewport().getExtentSize().height - rowHeight) {

                int newYPosition = activeRowYPosition - (scrollpane.getViewport().getExtentSize().height/2);
                if (newYPosition < 0)
                    newYPosition = 0;
                scrollpane.getViewport().setViewPosition(new Point(
                        scrollpane.getViewport().getViewPosition().x,
                        newYPosition
                ));
            }
        }
*/
//        refresh();

    }


    /** Adjusts the color of a field's cell renderer to be the color of the data type of the
     *  field.
     */
    protected void adjustCellRendererColor(AbstractTableField f, DBCellRenderer dcr) {
        String fldType = f.getDataType().getName();
//1        TableView tableView = viewStructure.tableView;
        if (fldType.equals(IntegerTableField.DATA_TYPE))
            dcr.setForegroundColor(integerColor/*tableView.getIntegerColor()*/);
        else if (fldType.equals(FloatTableField.DATA_TYPE))
            dcr.setForegroundColor(floatColor/*tableView.getDoubleColor()*/);
        else if (fldType.equals(DoubleTableField.DATA_TYPE))
            dcr.setForegroundColor(doubleColor/*tableView.getDoubleColor()*/);
        else if (fldType.equals(DoubleTableField.DATA_TYPE))
            dcr.setForegroundColor(doubleColor/*tableView.getDoubleColor()*/);
        else if (fldType.equals(StringTableField.DATA_TYPE))
            dcr.setForegroundColor(stringColor/*tableView.getStringColor()*/);
        else if (fldType.equals(BooleanTableField.DATA_TYPE))
            dcr.setForegroundColor(booleanColor/*tableView.getBooleanColor()*/);
//        else if (fldType.equals("java.util.Date")) {
        else if (fldType.equals(DateTableField.DATA_TYPE))
//            if (f.isDate())
            dcr.setForegroundColor(dateColor/*tableView.getDateColor()*/);
        else if (fldType.equals(TimeTableField.DATA_TYPE))
//            else
            dcr.setForegroundColor(timeColor/*tableView.getTimeColor()*/);
        else if (fldType.equals(URLTableField.DATA_TYPE))
            dcr.setForegroundColor(urlColor/*tableView.getUrlColor()*/);

        if (nonEditableFieldsHighlighted && (!f.isEditable() || !table.isDataChangeAllowed()))
            dcr.setBackgroundColor(highlightColor/*viewStructure.tableView.getHighlightColor()*/);
        else
            dcr.setBackgroundColor(backgroundColor/*viewStructure.tableView.getBackgroundColor()*/);
//        jTable.setSelectionBackground(selectionBackground);
//1        paintSelection();
    }


    private int findFirstNonCalculatedEmptyField(AbstractTableField calcFld) {
        int emptyFldIndex = -1;
        /* Search for the first field on which the calculated field depends,
        * which is empty (null). If one exists, then this is to be edited.
        */
//        System.out.println("findFirstNonCalculatedEmptyField----  " + calcFld.getName());
        for (int i=0; i<calcFld.getDependsOnFields().size(); i++) {
            AbstractTableField dFld = null;
            try{
                dFld = table.getTableField(calcFld.getDependsOnFields().get(i));
            }catch (InvalidFieldIndexException e) {System.out.println("Serious inconsistency error in DBTable findFirstNonCalculatedEmptyField: (2)"); return -1;}
//            System.out.println("Checking " + dFld.getName());
            if ((!dFld.isCalculated()) && table.riskyGetCell(calcFld.getDependsOnFields().get(i), table.recordIndex.get(pendingRecordIndex)) == null) {
                emptyFldIndex = jTable.convertColumnIndexToView(calcFld.getDependsOnFields().get(i));
                break;
            }
        }
        for (int i=0; emptyFldIndex != -1 && i<calcFld.getDependsOnFields().size(); i++) {
            AbstractTableField f = null;
            try{
                f = table.getTableField(calcFld.getDependsOnFields().get(i));
            }catch (InvalidFieldIndexException e) {System.out.println("Serious inconsistency error in DBTable findFirstNonCalculatedEmptyField: (1)"); return -1;}
            emptyFldIndex = findFirstNonCalculatedEmptyField(f);
        }

//        System.out.println("emptyFldIndex: " + emptyFldIndex);
        return (emptyFldIndex == -1)? -1 : jTable.convertColumnIndexToView(calcFld.getDependsOnFields().get(emptyFldIndex));

    }


/*1    protected void checkAndStorePendingRecord() {
        if (notAgain) {
//            System.out.println("In checkAndStorePendingRecord: Exiting because of \"notAgain\"");
            notAgain=false;
            return;
        }

        if (dbComponent.pendingNewRecord) {
            if (dbComponent.blockingNewRecord)
                notAgain = true;
        }

        checkAndStorePendingRecord2();
    }
1*/

//1    protected boolean checkAndStorePendingRecord2() {

//        System.out.println("EditingRow: " + jTable.editingRow() + "   Blocking: " + dbComponent.blockingNewRecord + "   Pending: " + dbComponent.pendingNewRecord);

/* If a jTable cell was being edited when a condition which triggers the end of the interactive insertion
* of the new record, then stop the cell editing. This results in the evaluation of the edited
* value of the cell.
*/
/*1        if (dbComponent.pendingNewRecord) {
            if (dbComponent.blockingNewRecord) {
                if (!((DBTableModel) jTable.getModel()).messageDelivered) {
                    ESlateOptionPane.showMessageDialog(dbComponent, bundle.getString("DBTableMsg4") , bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    ((DBTableModel) jTable.getModel()).messageDelivered = false;
                }

//                        try{
                int emptyKeyFieldIndex = -1;
                AbstractTableField keyFld = null;
                for (int i=0; i<((DBTableModel) jTable.getModel()).getColumnCount(); i++) {
                    keyFld = ((DBTableModel) jTable.getModel()).getField(i);
                    if (table.isPartOfTableKey(keyFld) && table.riskyGetCell(i, table.recordIndex.get(pendingRecordIndex)) == null) {
                        emptyKeyFieldIndex = i;
                        break;
                    }
                }

//                System.out.println("emptyKeyFieldIndex: " + emptyKeyFieldIndex + ", keyFld.isCalculated(): " +keyFld.isCalculated());

                if (emptyKeyFieldIndex != -1) {
1*/                    /* If the "keyFld" is not calculated then edit it.
                    */
/*1                    if (!keyFld.isCalculated()) {
                        int colIndex = jTable.convertColumnIndexToView(emptyKeyFieldIndex);
                        for (int m=0; m<colSelectionStatus.size(); m++)
                            colSelectionStatus.set(m, Boolean.FALSE);
                        colSelectionStatus.set(colIndex, Boolean.TRUE);
                        jTable.getColumnModel().getSelectionModel().setSelectionInterval(colIndex, colIndex);
                        jTable.getSelectionModel().setSelectionInterval(pendingRecordIndex, pendingRecordIndex);
                        editCellAt(pendingRecordIndex, colIndex);
//                        System.out.println("Exiting 1");
                        return false;
                    }else{
//                        System.out.println("Editing cell at: " + ((Integer) keyFld.getDependsOnFields().at(0)).intValue());
                        int emptyFldIndex = -1, notEmptyNonCalculatedFieldIndex = -1;
1*/
                        /* Search for the first field on which the calculated field depends,
                        * which is empty (null). If one exists, then this is to be edited.
                        */
//                        System.out.println("findFirstNonCalculatedEmptyField----  " + keyFld.getName());
/*1                        for (int i=0; i<keyFld.getDependsOnFields().size(); i++) {
                            AbstractTableField dFld = null;
                            try{
                                dFld = table.getTableField(keyFld.getDependsOnFields().get(i));
                            }catch (InvalidFieldIndexException e1) {System.out.println("Serious inconsistency error in DBTable DBTable(): (2)"); return false;}
                            if (!dFld.isCalculated() && dFld.isEditable()) {
1*/                                /* Find the first editable non-Calculated not empty field. In case
                                * no empty field, on which the key calculated field depends on, is
                                * found empty, the cell of the first editable non-Calculated not
                                * empty field will be edited.
                                */
/*1                                if (notEmptyNonCalculatedFieldIndex == -1)
                                    notEmptyNonCalculatedFieldIndex = jTable.convertColumnIndexToView(keyFld.getDependsOnFields().get(i));
//                                System.out.println(dbTable.riskyGetCell(((Integer) keyFld.getDependsOnFields().at(i)).intValue(), recordIndex[pendingRecordIndex]));
                                if (table.riskyGetCell(keyFld.getDependsOnFields().get(i), table.recordIndex.get(pendingRecordIndex)) == null) {
                                    emptyFldIndex = jTable.convertColumnIndexToView(keyFld.getDependsOnFields().get(i));
                                    break;
                                }
                            }
                        }

//                        System.out.println("EMPTYFLDINDX: " + emptyFldIndex);
                        if (emptyFldIndex != -1) {
                            for (int m=0; m<colSelectionStatus.size(); m++)
                                colSelectionStatus.set(m, Boolean.FALSE);
                            colSelectionStatus.set(emptyFldIndex, Boolean.TRUE);
                            jTable.getColumnModel().getSelectionModel().setSelectionInterval(emptyFldIndex, emptyFldIndex);
                            jTable.getSelectionModel().setSelectionInterval(pendingRecordIndex, pendingRecordIndex);
                            editCellAt(pendingRecordIndex, emptyFldIndex);
//                            System.out.println("Exiting 2");
                            return false;
                        }else{
1*/                            /* Edit the first non-Calculated editable field from the key
                            * calculated field's "depends-On" list, if one exists.
                            */
/*1                            if (notEmptyNonCalculatedFieldIndex != -1) {
                                for (int m=0; m<colSelectionStatus.size(); m++)
                                    colSelectionStatus.set(m, Boolean.FALSE);
                                colSelectionStatus.set(notEmptyNonCalculatedFieldIndex, Boolean.TRUE);
                                jTable.getColumnModel().getSelectionModel().setSelectionInterval(notEmptyNonCalculatedFieldIndex, notEmptyNonCalculatedFieldIndex);
                                jTable.getSelectionModel().setSelectionInterval(pendingRecordIndex, pendingRecordIndex);
                                editCellAt(pendingRecordIndex, notEmptyNonCalculatedFieldIndex);
//                                System.out.println("Exiting 3");
                                return false;
                            }else{
1*/
                                /* Edit the first in series non-Calculated editable field of the jTable.
                                */
/*1                                AbstractTableField f1;
                                for (int i=0; i<tableFields.size(); i++) {
                                    f1 = (AbstractTableField) tableFields.get(i);

                                    if (!f1.isCalculated() && f1.isEditable()) {
                                        emptyFldIndex = jTable.convertColumnIndexToView(i);
                                        break;
                                    }
                                }
                                if (emptyFldIndex != -1) {
                                    for (int m=0; m<colSelectionStatus.size(); m++)
                                        colSelectionStatus.set(m, Boolean.FALSE);
                                    colSelectionStatus.set(emptyFldIndex, Boolean.TRUE);
                                    jTable.getColumnModel().getSelectionModel().setSelectionInterval(emptyFldIndex, emptyFldIndex);
                                    jTable.getSelectionModel().setSelectionInterval(pendingRecordIndex, pendingRecordIndex);
                                    editCellAt(pendingRecordIndex, emptyFldIndex);
                                    return false;
                                }

                            }
                        }

                    }
                    return false;
                }else{
1*/
                    /* Edit the first in series non-Calculated editable field of the jTable.
                    */
/*1                    int emptyFldIndex=-1;
                    AbstractTableField f1;
                    for (int i=0; i<tableFields.size(); i++) {
                        f1 = (AbstractTableField) tableFields.get(i);

                        if (!f1.isCalculated() && f1.isEditable()) {
                            emptyFldIndex = jTable.convertColumnIndexToView(i);
                            break;
                        }
                    }
                    if (emptyFldIndex != -1) {
                        for (int m=0; m<colSelectionStatus.size(); m++)
                            colSelectionStatus.set(m, Boolean.FALSE);
                        colSelectionStatus.set(emptyFldIndex, Boolean.TRUE);
                        jTable.getColumnModel().getSelectionModel().setSelectionInterval(emptyFldIndex, emptyFldIndex);
                        jTable.getSelectionModel().setSelectionInterval(pendingRecordIndex, pendingRecordIndex);
                        editCellAt(pendingRecordIndex, emptyFldIndex);
                        return false;
                    }
                }

                return false;
            }

            dbComponent.pendingNewRecord = false;
            for (int i=0; i<colRendererEditors.size(); i++) {
                ColumnRendererEditor cre = (ColumnRendererEditor) colRendererEditors.get(i);
                if (cre.editor != null) {
                    cre.editor.setClickCountToStart(2);
                }
            }
            paintSelection();

            refresh();
            return true;
        }
        return true;
    }
1*/


    public void alterRecordSelection(IntBaseArray recordIndices) {
        if (!iterateEvent) {
            iterateEvent = true;
            return;
        }


// Unfortunately I have to repaint the whole jTable, cause I don't have
// the indices of the previously selected records.
/*        IntArray recordIndexArray = new IntArray(recordIndex);

int[] selectedRows = new int[recordIndices.size()];
for (int i=0; i<recordIndices.size(); i++) {
index = recordIndexArray.indexOf(recordIndices.at(i));
//            jTable.addRowSelectionInterval(index, index);
selectedRows[i] = index;
}

refreshRows(selectedRows);
*/
        rowBar.repaint();
        jTable.repaint();
    }

    /** Specifies the auto-resize mode of the DBTable's columns. One of:
     *  <ul>
     *  <li> JTable.AUTO_RESIZE_LAST_COLUMN
     *  <li> JTable.AUTO_RESIZE_NEXT_COLUMN
     *  <li> JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
     *  <li> JTable.AUTO_RESIZE_ALL_COLUMNS
     *  <li> JTable.AUTO_RESIZE_OFF
     *  </ul>
     */
    public void setAutoResizeMode(int resizeMode) {
        if (resizeMode != JTable.AUTO_RESIZE_OFF &&
                resizeMode != JTable.AUTO_RESIZE_NEXT_COLUMN &&
                resizeMode != JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS &&
                resizeMode != JTable.AUTO_RESIZE_LAST_COLUMN &&
                resizeMode != JTable.AUTO_RESIZE_ALL_COLUMNS)
            return;
        if (autoResizeMode == resizeMode) return;

        autoResizeMode = resizeMode;
//1        viewStructure.tableView.setAutoResizeMode(resizeMode);
//            dbTable.UIProperties.put(AUTO_RESIZE_MODE_POS, new Integer(autoResizeMode));
        setModified(true);
        if (jTable != null)
            jTable.setAutoResizeMode(resizeMode);
    }

    /** Returns the column auto-resize mode of the DBTable. One of JTable.AUTO_RESIZE_LAST_COLUMN, JTable.AUTO_RESIZE_ALL_COLUMNS,
     *  JTable.AUTO_RESIZE_OFF.
     */
    public int getAutoResizeMode() {
        return autoResizeMode; //1 viewStructure.tableView.getAutoResizeMode();
    }

    public void addWidthChangeListener() {
        for (int i=0; i<tableColumns.size(); i++) {
            TableColumn col = tableColumns.get(i).tableColumn; //cf (TableColumn) tableColumns.get(i);
            /* This PropertyChangeListener is used to keep track of the column's width, when the user
            * interactively resizes the column. A PropertyChangeEvent is posted, whenever the columns
            * width changes. The new value is stored in the "UIProperties" Array of the respective
            * "AbstractTableField".
            */
            col.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
// System.out.println("property: " + evt.getPropertyName() + ", newValue: " + evt.getNewValue());
                    //if (evt.getPropertyName().equals("columWidth")) {
                    if (evt.getPropertyName().equals("width")) {
                        setModified(true);
/*1                        AbstractTableField f2 = (AbstractTableField) tableFields.get(((TableColumn) evt.getSource()).getModelIndex());
                        FieldView fieldView = viewStructure.getFieldView(f2.getName());
                        int width = fieldView.getFieldWidth();
                        int newWidth = ((Integer) evt.getNewValue()).intValue();
                        if (!isTableInitializing && width != newWidth) {
                            fieldView.setFieldWidth(newWidth);
                            setModified(true);
                            jTable.invalidate();
                        }
1*/
                    }
                }
            });
        }
    }

    protected void editCellAt(int recIndex, int colIndex) {
//Thread.currentThread().dumpStack();
        AbstractTableField fld;
        try{
            fld = table.getTableField(jTable.convertColumnIndexToModel(colIndex));
        }catch (InvalidFieldIndexException e) {return;}

        if (!fld.isEditable() || !table.isDataChangeAllowed()) //jTable.isCellEditable(recIndex, colIndex))
            return;
        if (fld.getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class)) {// ((TableField) tableFields.at(((TableColumn) tableColumns.get(colIndex)).getModelIndex())).getFieldType().equals(gr.cti.eslate.database.engine.CImageIcon.class))
            int record = table.recordIndex.get(recIndex);
            String fileName = null;
            try{
                //IE tests
                CImageIcon ic = (CImageIcon) table.riskyGetCell(table.getFieldIndex(fld), record);
                if (ic != null) {
                    fileName = ic.getFileName();
                }
            }catch (InvalidFieldException exc) {
                System.out.println("Serious inconsistency error in DBTable editCellAt(): (1)");
            }
            ESlateFileDialog fileDialog = getIconFileDialog();
            if (fileName != null)
                fileDialog.setDirectory(fileName);
            if (fileDialog.isShowing()) {
                return;
            }
            fileDialog.show();
        }else{
            jTable.editCellAt(recIndex, colIndex);
            JComponent ec = tableColumns.get(colIndex).editorComponent; //cf ((ColumnRendererEditor) colRendererEditors.get(jTable.convertColumnIndexToModel(colIndex))).editorComponent;
            ec.setBackground(editedCellBackColor);
            if (ec instanceof JTextField) {
                JTextField tf = (JTextField) ec;
                ((javax.swing.JTextField) ec).getCaret().setVisible(true);
                ((javax.swing.JTextField) ec).selectAll();
            }
            /* If the field is of Boolean data type, show the cell editor's pop-up
            */
            if (fld.getDataType().equals(java.lang.Boolean.class)) {
//                ec.requestFocus();
//                ((BooleanValues) ec).setSelectedIndex(1);
                ec.setVisible(true);
                ((BooleanValues) ec).showPopup();
//                ((BooleanValues) ((DefaultCellEditor) ((TableColumn) tableColumns.get(jTable.getEditingColumn())).getCellEditor()).getComponent()).showPopup();

            }else{
                ec.requestFocus();
                ec.setVisible(true);
            }
            ec.repaint();
//1            isEditing = true;
        }
    }

    public Table getTable() {
        return table;
    }

    public JTable getJTable() {
        return jTable;
    }

/*    protected void changeTableModel(Table table) {
Table prevCTable = this.dbTable;
this.dbTable = table;
//        this.dbTable.UIProperties = prevCTable.UIProperties;
viewStructure = new TableViewStructure(table);
// Alter the jTable's data model
tableModel.setTable(table);
*/
    /* Initialize the recordIndex.
    */
/*        table.recordIndex = new IntBaseArray(table.getRecordCount());
for (int i=0; i<table.recordIndex.size(); i++) {
table.recordIndex.set(i, i);
}

// Re-initialize the jTable environment
DefaultTableColumnModel tcm = (DefaultTableColumnModel) jTable.getColumnModel();
TableColumn col;
ColumnRendererEditor cr;
AbstractTableField f;
tableColumns = new ArrayList();
int defColumnWidth;
int autoResizeMode = viewStructure.tableView.getAutoResizeMode();
if (autoResizeMode == jTable.AUTO_RESIZE_OFF || autoResizeMode == jTable.AUTO_RESIZE_LAST_COLUMN)
defColumnWidth = FieldView.DEFAULT_FIELD_WIDTH;
else{
if (tableFields.size() != 0)
defColumnWidth = (dbComponent.getSize().width-20) / tableFields.size();
else
defColumnWidth = 20;
}

Font tableFont = viewStructure.tableView.getTableFont();
for (int i=0; i<tcm.getColumnCount(); i++) {
col = tcm.getColumn(i);

//            tableColumns.add(col);
f = tableModel.getField(tableModel.getColumnIndex(col.getIdentifier()));
FieldView fieldView = viewStructure.getFieldView(f.getName());
*/            /* Access the UIProperties Array of field "f". If it's empty, then set column's max width,
* min width and width to the default values and store them in the Array. Otherwise, use
* the stored values.
*/
//            if (f.UIProperties.isEmpty()) {
/*            col.setPreferredWidth(defColumnWidth);
col.setMinWidth(FieldView.DEFAULT_FIELD_MIN_WIDTH);
col.setMaxWidth(FieldView.DEFAULT_FIELD_MAX_WIDTH);
fieldView.setFieldWidth(defColumnWidth);

//            f.UIProperties.add(new Integer(defColumnWidth));
//                f.UIProperties.add(new Integer(40));
//                f.UIProperties.add(new Integer(1000));
table.setModified();
*/
/*            col.setIdentifier(f.getName());

String fieldType = f.getDataType().getName();

if (f == null) {
System.out.println("Serious inconsistency error in DBTable changeTableModel(): (1)");
return;
}
*/
//            HeaderRenderer hr = new HeaderRenderer(f.getName(), f.getDataType(), !f.isKey(), /*f.isDate(),*/ f.isCalculated(), this);
/*            col.setHeaderRenderer(hr);

cr = new ColumnRendererEditor(this, f, tableFont);
col.setCellRenderer(cr.renderer);
if (f.isEditable() && table.isDataChangeAllowed()) {
col.setCellEditor(cr.editor);
if (cr.editor != null)
cr.editor.setClickCountToStart(2);
}else
col.setCellEditor(null);

colRendererEditors.add(cr);

if (cr.editor != null) {
cr.editor.addCellEditorListener(new CellEditorListener() {
public void editingStopped(ChangeEvent e) {
isEditing = false;
int activeTableIndex = dbComponent.tabs.getSelectedIndex();
((DBTable) dbComponent.dbTables.at(activeTableIndex)).jTable.requestFocus();
dbComponent.setCutCopyPasteStatus();
}

public void editingCanceled(ChangeEvent e) {
isEditing = false;
int activeTableIndex = dbComponent.tabs.getSelectedIndex();
((DBTable) dbComponent.dbTables.at(activeTableIndex)).jTable.requestFocus();
dbComponent.setCutCopyPasteStatus();
}
});
}
}
jTable.getTableHeader().setUpdateTableInRealTime(false);

tableFields = table.getFields();

tableColumns.clear();
for (int i=0; i<tcm.getColumnCount(); i++) {
tableColumns.add(tcm.getColumn(i));
}

this.dbTable.setActiveRecord(-1);
activeRow = -1;
rowBar.repaint();
paintBackground();
paintNonEditableColumns();
paintSelection();
paintActiveRecord();
}
*/

    protected void renameField(AbstractTableField field, String newName, String oldName) {
        try{
            table.renameField(oldName, newName);
        }catch (InvalidFieldNameException e) {System.out.println("InvalidFieldNameException"); return;}
        catch (FieldNameInUseException e) {System.out.println("FieldNameInUseException"); return;}
    }

    protected void UIRenameField(String newName, String oldName) {
        TableColumn col = jTable.getColumn(oldName);
        col.setIdentifier(newName);
        col.setHeaderValue(newName);
        ((HeaderRenderer) col.getHeaderRenderer()).setText(newName);
        JTableHeader h = jTable.getTableHeader();
        h.validate();
        h.resizeAndRepaint(); //paintImmediately(activeDBTable.jTable.getTableHeader().getVisibleRect());
//1        viewStructure.fieldRenamed(oldName, newName);
//t        h.repaint(h.getVisibleRect());
//        h.paintImmediately(h.getVisibleRect());
    }


    public void UIAddField(AbstractTableField newfld) {
        String fieldName = newfld.getName();
        TableColumn col = new TableColumn(table.getFieldCount()-1); // As of version 0.7 newfld.getName());
        col.setIdentifier(fieldName);
        col.setHeaderValue(fieldName);

        /* Freeze the width of the existing columns
        */
/*1        for (int i=0; i<tableColumns.size(); i++) {
            TableColumn cl = (TableColumn) tableColumns.get(i);
            int width = cl.getPreferredWidth();
            cl.setMaxWidth(width);
        }
1*/
        jTable.getColumnModel().addColumn(col);

//1        FieldView fieldView = viewStructure.fieldAdded(fieldName);
        /* Restore the maximum widths of the rest of the columns in the jTable.
         */
/*1        for (int i=0; i<tableColumns.size(); i++) {
            TableColumn cl = (TableColumn) tableColumns.get(i);
            FieldView fView = viewStructure.getFieldView((String) cl.getIdentifier());
//            try{
//                int width = ((Integer) dbTable.getTableField((String) cl.getIdentifier()).UIProperties.at(2)).intValue();
            int width = fView.getFieldMaxWidth();
            cl.setMaxWidth(width);
//            }catch (InvalidFieldNameException exc) {
//                System.out.println("Serious inconsistency error in DBTable UIAddField(): (1)");
//            }
        }
1*/
        HeaderRenderer hr = new HeaderRenderer(newfld, this);
        col.setHeaderRenderer(hr);
        tableColumns.add(AbstractDBTableColumn.createDBTableColumn(this, col, newfld));
        /* This PropertyChangeListener is used to keep track of the column's width, when the user
        * interactively resizes the column. A PropertyChangeEvent is posted, whenever the columns
        * width changes. The new value is stored in the "UIProperties" Array of the respective
        * "AbstractTableField".
        */
        col.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
// System.out.println("property: " + evt.getPropertyName());
                if (evt.getPropertyName().equals("width")) {
                    setModified(true);
/*1                    AbstractTableField f2 = (AbstractTableField) tableFields.get(((TableColumn) evt.getSource()).getModelIndex());
                    FieldView fieldView = viewStructure.getFieldView(f2.getName());
                    int width = fieldView.getFieldWidth();
                    int newWidth = ((Integer) evt.getNewValue()).intValue();
//                    if (!f2.UIProperties.isEmpty() && !f2.UIProperties.at(0).equals(evt.getNewValue())) {
                    if (width != newWidth) {
//                        f2.UIProperties.put(0, evt.getNewValue());
                        fieldView.setFieldWidth(newWidth);
                        setModified(true);
                        scrollpane.getViewport().validate();
                        scrollpane.getViewport().doLayout();
                    }
1*/
                }
            }
        });

//cf        tableFields = table.getFields();
//tci        tableColumnsIndex.add(new Integer(tableColumns.size()-1));
        col.setMinWidth(FieldView.DEFAULT_FIELD_MIN_WIDTH);
        col.setMaxWidth(FieldView.DEFAULT_FIELD_MAX_WIDTH);
//        newfld.UIProperties.add(new Integer(0));
//        newfld.UIProperties.add(new Integer(40));
//        newfld.UIProperties.add(new Integer(1000));

        int colWidth;
//1        int autoResizeMode = viewStructure.tableView.getAutoResizeMode();
        if (autoResizeMode == jTable.AUTO_RESIZE_OFF || autoResizeMode == jTable.AUTO_RESIZE_LAST_COLUMN) {
            colWidth = FieldViewProperties.DEFAULT_FIELD_WIDTH;
//            newfld.UIProperties.put(0, new Integer(110));
//1            fieldView.setFieldWidth(FieldView.DEFAULT_FIELD_WIDTH);
//1            col.setPreferredWidth(colWidth);
            col.setWidth(colWidth);
            for (int i=0; i<tableColumns.size(); i++) {
                AbstractDBTableColumn column = tableColumns.get(i);
                column.tableColumn.setWidth(column.tableColumn.getWidth());
            }
//1                ((TableColumn) tableColumns.get(i)).setPreferredWidth(((TableColumn) tableColumns.get(i)).getPreferredWidth());
        }else{
            /* Resize all the existing columns, so that all the columns in the jTable will have equal
            * width. This happens only if the the average column width is more than 70, which means
            * that there are only a few columns in the jTable.
            */
            if ((colWidth = (getSize().width-20) / tableColumns.size()) > 80) {
                for (int i=0; i<tableColumns.size(); i++) {
                    TableColumn col1 = tableColumns.get(i).tableColumn;
//1                    AbstractTableField f1 = (AbstractTableField) tableFields.get(col1.getModelIndex());
//1                    FieldView fView = viewStructure.getFieldView(f1.getName());
                    /* Adjusts the columns minimum width, if it is greater than the width it will be given.
                    */
                    if (col1.getMinWidth() > colWidth) {
                        col1.setMinWidth(colWidth);
//1                        fView.setFieldMinWidth(colWidth);
//                        f1.UIProperties.put(1, new Integer(colWidth));
                    }
                    tableColumns.get(i).tableColumn.setPreferredWidth(colWidth);
//1                    fView.setFieldWidth(colWidth);
//                    f1.UIProperties.put(0, new Integer(colWidth));
                }
            }else{
                colWidth = 80;
//1                fieldView.setFieldWidth(80);
//                newfld.UIProperties.put(0, new Integer(80));
            }
        }


//        HeaderRenderer hr = new HeaderRenderer(newfld, this);
//        col.setHeaderRenderer(hr);
//cf        ColumnRendererEditor cr = tableColumns.get(tableColumns.size()-1).colRendererEditor;
//cf        DefaultCellEditor dce;
//cf        cr = new ColumnRendererEditor(this, newfld, tableFont/*viewStructure.tableView.getTableFont()*/);
//cf        col.setCellRenderer(cr.renderer);
//cf        adjustCellRendererColor(newfld, cr.renderer);
//cf        if (newfld.isEditable() && table.isDataChangeAllowed()) {
//cf            col.setCellEditor(cr.editor);
//cf            if (cr.editor != null)
//cf                cr.editor.setClickCountToStart(2);
//cf        }else
//cf            col.setCellEditor(null);

//cf        colRendererEditors.add(cr);

//cf        if (cr.editor != null) {
/*1            cr.editor.addCellEditorListener(new CellEditorListener() {
                public void editingStopped(ChangeEvent e) {
//1                    isEditing = false;
                    int activeTableIndex = dbComponent.tabs.getSelectedIndex();
                    ((DBTable) dbComponent.dbTables.at(activeTableIndex)).jTable.requestFocus();
//                    dbComponent.tabs.requestFocus();
                }

                public void editingCanceled(ChangeEvent e) {
//1                    isEditing = false;
                    int activeTableIndex = dbComponent.tabs.getSelectedIndex();
                    ((DBTable) dbComponent.dbTables.at(activeTableIndex)).jTable.requestFocus();
                }
            });
1*/
//cf        }

        if (!addRecordAction.isEnabled())
            addRecordAction.setEnabled(true);
        refresh();
    }


    protected void UIRemoveColumn(String fieldName, int fieldIndex) {
        TableColumn col = null;
        DBCellRenderer renderer = null;
        int colIndex = -1, modelIndex=fieldIndex;
        for (int i=0; i<tableColumns.size(); i++) {
//System.out.println("modelIndex: " + modelIndex + ", ((TableColumn) tableColumns.get(i)).getModelIndex(): " + ((TableColumn) tableColumns.get(i)).getModelIndex());
            if (tableColumns.get(i).tableColumn.getModelIndex() == modelIndex) {
                colIndex = i;
                col = tableColumns.get(i).tableColumn;
                break;
            }
        }
//        System.out.println("colIndex: " + colIndex);
        try{
//            col = jTable.getColumn(fieldName);
            renderer = (DBCellRenderer) col.getCellRenderer();
//            colIndex = tableColumns.indexOf(col);
            modelIndex = col.getModelIndex();

            /* Take care of the columnOrder Array.
            */
            int remColumnIndex = colIndex;
//1            Array columnOrder = viewStructure.tableView.getColumnOrder();
//1            viewStructure.fieldRemoved(fieldName);
//            System.out.println("remColumnIndex: " + remColumnIndex);
//            System.out.println("dbComponent.foreignDatabase: " + dbComponent.foreignDatabase);
//            System.out.println("dbComponent.remoteTableModelListenerStructure.isImportedTable(dbTable): " + dbComponent.remoteTableModelListenerStructure.isImportedTable(dbTable));
//            if (!dbComponent.foreignDatabase && !dbComponent.remoteTableModelListenerStructure.isImportedTable(dbTable)) {
//                System.out.println("Removing remColumnIndex: " + remColumnIndex);
//1            columnOrder.remove(remColumnIndex);
//            }
//            System.out.println("UIRemoveColumn columnOrder: " + columnOrder);
            colSelectionStatus.remove(colIndex);
//1            int colIndex1;
//1            for (int i=0; i<columnOrder.size(); i++) {
//1                colIndex1 = columnOrder.get(i);
//1                if (colIndex1 > remColumnIndex)
//1                    columnOrder.set(i, colIndex1-1);
//1            }

            jTable.getColumnModel().getSelectionModel().removeIndexInterval(colIndex, colIndex);
            jTable.getColumnModel().removeColumn(col);

            /* If this was the last column in the Table, then remove all the records.
            */
            if (jTable.getColumnModel().getColumnCount() == 0)
                removeAllRecords();

            try{
                table.getTableField(fieldName);
//cf                tableFields.remove(modelIndex);
            }catch (InvalidFieldNameException e) {}

/*cf            ColumnRendererEditor existing;
            int colRendererEditorsIndex = -1;
            for (int i=0; i<colRendererEditors.size(); i++) {
                existing = (ColumnRendererEditor) colRendererEditors.get(i);
                if (renderer.equals(existing.renderer)) {
                    colRendererEditorsIndex = i;
                    break;
                }
            }

            if (colRendererEditorsIndex == -1) {
                System.out.println("Serious inconsistency error in DBTable removeColumn: (2)");
                return;
            }

            colRendererEditors.remove(colRendererEditorsIndex);
*/
            /* Update Arrays "tableColumns" and "tableColumnsIndex".
            */
//cf            tableColumns.remove(col);
            tableColumns.remove(colIndex);
//cf            tableFields = table.getFields();
            /* Decrease by 1 the "modelIndex" of all the "TableColumn's", which have a higher "modelIndex"
            * than the one being removed.
            */
            for (int i=0; i<tableColumns.size(); i++) {
//cf                TableColumn tc = (TableColumn) tableColumns.get(i);
                TableColumn tc = tableColumns.get(i).tableColumn;
                if (tc.getModelIndex() > modelIndex)
                    tc.setModelIndex(tc.getModelIndex()-1);
            }
/*tci            tableColumnsIndex.remove(colIndex);
int k;
for (int i=0; i<tableColumnsIndex.size(); i++)  {
k = ((Integer) tableColumnsIndex.at(i)).intValue();
if (k > colIndex)
tableColumnsIndex.put(i, new Integer(k-1));
}
*/
        }catch (Exception e) {
            System.out.println(e.getClass().getName() + ", " + e.getMessage());
            e.printStackTrace();
        }

        //ERROR: The following line does not work. As a result the removed column
        //remains selected (28/1/99)
        jTable.getColumnModel().getSelectionModel().clearSelection();
        columnSelectionChangedAction();

        refresh();
        return;
    }

    protected void UIChangeFieldType(AbstractTableField f, String previousDataType) {
//cf        TableColumn col = jTable.getColumn(f.getName());
//cf        ((HeaderRenderer) col.getHeaderRenderer()).setIcon(f.getDataType(), !table.isPartOfTableKey(f)/*!f.isKey()*/, /*f.isDate(),*/ this);
//        jTable.getTableHeader().paintImmediately(jTable.getTableHeader().getVisibleRect());
/*cf        jTable.getTableHeader().repaint(jTable.getTableHeader().getVisibleRect());

        DBCellRenderer renderer = (DBCellRenderer) col.getCellRenderer();
        ColumnRendererEditor existing;
        int colRendererEditorsIndex = -1;
        for (int i=0; i<tableColumns.size(); i++) {
            existing = tableColumns.get(i).colRendererEditor; //cf (ColumnRendererEditor) colRendererEditors.get(i);
            if (renderer.equals(existing.renderer)) {
                colRendererEditorsIndex = i;
                break;
            }
        }

        if (colRendererEditorsIndex == -1) {
            System.out.println("Serious inconsistency error in DBTable changeFieldType: (1)");
            return;
        }
*/
        /* If the field whose type was changed was of "Image" data type, then get
        * rid of the old ColumnRendererEditor, because the renderers of the
        * ColumnRendererEditors for Image fields have a specific "setValue" method.
        * Also, if the new data type of the field is "Image", then we have to create
        * a new ColumnRendererEditor for this field.
        */
/*cf        if (previousDataType.equals(CImageIcon.class) || f.getDataType().equals(CImageIcon.class)) {
            ColumnRendererEditor newcre = new ColumnRendererEditor(this, f, tableFont */ /*viewStructure.tableView.getTableFont()*/ /*cf );
            newcre.renderer = tableColumns.get(colRendererEditorsIndex).renderer;
            col.setCellRenderer(newcre.renderer);
            adjustCellRendererColor(f, newcre.renderer);
            if (f.isEditable() && table.isDataChangeAllowed()) {
                col.setCellEditor(newcre.editor);
                if (newcre.editor != null)
                    newcre.editor.setClickCountToStart(2);
            }else
                col.setCellEditor(null);
//cf            colRendererEditors.set(colRendererEditorsIndex, newcre);
            tableColumns.get(colRendererEditorsIndex).colRendererEditor = newcre;
        }else{
//cf            ColumnRendererEditor cre = (ColumnRendererEditor) colRendererEditors.get(colRendererEditorsIndex);
            ColumnRendererEditor cre = tableColumns.get(colRendererEditorsIndex).colRendererEditor;
            cre.renderer = tableColumns.get(colRendererEditorsIndex).renderer;
System.out.println("UIChangeFieldType() cre.renderer: " + cre.renderer + ", col: " + col);
            cre.updateColumnRenderer(f);
            cre.updateColumnEditor(f);
            col.setCellRenderer(cre.renderer);
            if (f.isEditable() && table.isDataChangeAllowed())
                col.setCellEditor(cre.editor);
            else
                col.setCellEditor(null);
*/            /* Adjust the color of the column's cell renderer
            */
/*cf            adjustCellRendererColor(f, cre.renderer);
        }

        refreshField(col);
*/
    }


    public void changeKey(String fieldName, boolean includeInKey, boolean promptUser) {
//        TableColumn col = (TableColumn) jTable.getColumn(fieldName);
        try{
            AbstractTableField f = table.getTableField(fieldName);
            if (table.isPartOfTableKey(f)/*f.isKey()*/ == includeInKey)
                return;

            if (f.getDataType().equals(CImageIcon.class) && !table.isPartOfTableKey(f)/*f.isKey()*/) {
                ESlateOptionPane.showMessageDialog(this, bundle.getString("DatabaseMsg23"), bundle.getString("Warning"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (table.getRecordCount() > 1) {
                if (promptUser) {
                    Object[] yes_no = {bundle.getString("OK"), bundle.getString("Cancel")};
                    JOptionPane pane = new JOptionPane(bundle.getString("DBTableMsg9"), JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION, javax.swing.UIManager.getIcon("OptionPane.warningIcon"), yes_no, bundle.getString("OK"));
                    javax.swing.JDialog dialog = pane.createDialog(this, bundle.getString("Warning"));
                    dialog.show();
                    Object option = pane.getValue();

                    if (option == bundle.getString("Cancel") || option == null)
                        return;
                }
            }
            if (!table.isPartOfTableKey(f)/*f.isKey()*/) {
                try{
                    table.addToKey(f.getName());
                }catch (FieldAlreadyInKeyException e1) {e1.printStackTrace(); System.out.println("Serious inconsistency error in DBTable changeKey(): (1)"); return;}
                catch (FieldContainsEmptyCellsException e1) {
                    ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
                catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in DBTable changeKey(): (2)"); return;}
                catch (TableNotExpandableException e1) {
                    ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
                catch (InvalidKeyFieldException e1) {
                    ESlateOptionPane.showMessageDialog(this, bundle.getString("DatabaseMsg23"), bundle.getString("Warning"), JOptionPane.WARNING_MESSAGE);
                }
                catch (AttributeLockedException e1) {
                    ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }else{
                try{
                    table.removeFromKey(f.getName());
                }catch (FieldIsNotInKeyException e1) {System.out.println("Serious inconsistency error in DBTable changeKey(): (3)"); return;}
                catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in DBTable changeKey(): (4)"); return;}
                catch (TableNotExpandableException e1) {
                    ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
                catch (AttributeLockedException e1) {
                    ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }
            refresh();
        }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in DBTable changeKey(): (5)"); return;}
    }


    protected void UIChangeKey(String fieldName) {
        TableColumn col = (TableColumn) jTable.getColumn(fieldName);
        try{
            AbstractTableField f = table.getTableField(fieldName);
            if (headerIconsVisible/*viewStructure.tableView.isHeaderIconsVisible()*/) {
                ((HeaderRenderer) col.getHeaderRenderer()).setIcon(f.getDataType(), !table.isPartOfTableKey(f)/*f.isKey()*/, /*f.isDate(),*/ this);
//                jTable.getTableHeader().paintImmediately(jTable.getTableHeader().getVisibleRect());
                jTable.getTableHeader().repaint(jTable.getTableHeader().getVisibleRect());
            }
            refreshScrollpane();
        }catch (InvalidFieldNameException exc) {};
    }


    public void switchCalcFieldToNormal(String fieldName) {
        try{
            table.switchCalcFieldToNormal(fieldName);
        }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in DBTable switchCalcFieldToNormal(): (1)"); return;}
        catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }


    protected void UISwitchCalcFieldToNormal(String fieldName) {
        TableColumn col = (TableColumn) jTable.getColumn(fieldName);
        ((HeaderRenderer) col.getHeaderRenderer()).updateHeaderFont();
//         jTable.getTableHeader().paintImmediately(jTable.getTableHeader().getVisibleRect());
        jTable.getTableHeader().repaint(jTable.getTableHeader().getVisibleRect());
    }


    public void setFieldEditable(String fieldName, boolean isEditable) {
        try{
            table.setFieldEditable(fieldName, isEditable);
        }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in DBTable setFieldEditable(): (17)");}
        catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }


    protected void UISetFieldEditable(String fieldName) {
        TableColumn col = jTable.getColumn(fieldName);
        try{
            AbstractTableField f = table.getTableField(fieldName);
            if (!f.isEditable()) {
                col.setCellEditor(null);
            }else{
                for (int i=0; i<tableColumns.size(); i++) {
                    AbstractDBTableColumn column = tableColumns.get(i);
                    if (column.tableField.getName().equals(fieldName))
                        column.tableColumn.setCellEditor(column.editor);
                }
/*cf                DBCellRenderer renderer = (DBCellRenderer) col.getCellRenderer();
                ColumnRendererEditor existing;
                int colRendererEditorsIndex = -1;
                for (int i=0; i<tableColumns.size(); i++) {
                    existing = tableColumns.get(i).colRendererEditor;
                    if (renderer.equals(existing.renderer)) {
                        colRendererEditorsIndex = i;
                        break;
                    }
                }

                if (colRendererEditorsIndex == -1) {
                    System.out.println("Serious inconsistency error in DBTable UISetFieldEditable(): (1)");
                    return;
                }

                col.setCellEditor(tableColumns.get(colRendererEditorsIndex).colRendererEditor.editor);
*/
            }
//1            highlightNonEditableFieldsBox(viewStructure.tableView.isNonEditableFieldsHighlighted());
            highlightNonEditableFields(nonEditableFieldsHighlighted);
        }catch (InvalidFieldNameException exc) {
            System.out.println("Serious inconsistency error in DBTable UISetFieldEditable(): (2)");
        }
    }


    public void setFieldRemovable(String fieldName, boolean isRemovable) {
        try{
            table.setFieldRemovable(fieldName, isRemovable);
        }catch (InvalidFieldNameException exc) {
            System.out.println("Serious inconsistency error in DBTable setFieldRemovable(): (1)");
        }catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }


    public void changeCalcFieldFormula(String fieldName, String formula) {
        AbstractTableField f;
        try{
            f = table.getTableField(fieldName);
        }catch (InvalidFieldNameException exc) {
            System.out.println("Serious inconsistency error in DBTable changeCalcFieldFormula(): (1)");
            return;
        }

        if (f.isCalculated() && !f.getTextFormula().equals(formula)) {
            try{
//                String typ = f.getFieldType().getName();
                if (!table.changeCalcFieldFormula(fieldName, formula)) {
//                            JOptionPane.showMessageDialog(dbComponent, infoBundle.getString("DatabaseMsg27") + f.getName() + infoBundle.getString("DatabaseMsg28"), errorStr, JOptionPane.ERROR_MESSAGE);
                    Object[] ok = {bundle.getString("OK")};
                    JOptionPane pane = new JOptionPane(bundle.getString("DatabaseMsg27") + f.getName() + bundle.getString("DatabaseMsg28"), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, javax.swing.UIManager.getIcon("OptionPane.errorIcon"), ok, bundle.getString("OK"));
                    javax.swing.JDialog dialog = pane.createDialog(this, bundle.getString("Error"));
                    dialog.show();
                }
            }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in DBTable changeCalcFieldFormula(): (1)");}
            catch (InvalidFormulaException e1) {
                ESlateOptionPane.showMessageDialog(this, e1.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }catch (AttributeLockedException e1) {
                ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }

    }


    protected void UIChangeCalcFieldFormula(String fieldName, String previousDataType) {
        AbstractTableField f;
        try{
            f = table.getTableField(fieldName);
        }catch (InvalidFieldNameException e) {
            System.out.println("Serious inconsistency error in DBTable UIChangeCalcFieldFormula(): (1)");
            return;
        }

        TableColumn col = jTable.getColumn(fieldName);
        ((HeaderRenderer) col.getHeaderRenderer()).setIcon(f.getDataType(), !table.isPartOfTableKey(f)/*f.isKey()*/, /*f.isDate(),*/ this);
//        jTable.getTableHeader().paintImmediately(jTable.getTableHeader().getVisibleRect());
        jTable.getTableHeader().repaint(jTable.getTableHeader().getVisibleRect());

        /* If the type of a calculated field has changed as a result of editing its
        * formula, then make all the arrangements which have to do with the
        * apperance of the field. These arrangements invlve the column's
        * "CellRenderer".
        */
        if (!previousDataType.equals(f.getDataType().getName())) {
            for (int i=0; i<tableColumns.size(); i++) {
                AbstractDBTableColumn column = tableColumns.get(i);
                if (column.tableField.getName().equals(fieldName)) {
                    tableColumns.set(i, AbstractDBTableColumn.createDBTableColumn(this, column.tableColumn, f));
                    break;
                }
            }
/*cf            DBCellRenderer renderer = (DBCellRenderer) col.getCellRenderer();
            ColumnRendererEditor existing;
            int colRendererEditorsIndex = -1;
            for (int i=0; i<tableColumns.size(); i++) {
                existing = tableColumns.get(i).colRendererEditor;
                if (renderer.equals(existing.renderer)) {
                    colRendererEditorsIndex = i;
                    break;
                }
            }

            if (colRendererEditorsIndex == -1) {
                System.out.println("Serious inconsistency error in Database ciFieldEdit: (2)");
                return;
            }

            tableColumns.get(colRendererEditorsIndex).colRendererEditor.updateColumnRenderer(f);
            tableColumns.get(colRendererEditorsIndex).colRendererEditor.updateColumnEditor(f);
*/
            /* Adjust the color of the column's cell renderer
            */
/*cf            adjustCellRendererColor(f,
                    tableColumns.get(colRendererEditorsIndex).colRendererEditor.renderer);
*/
        }
        repaint();
//        refreshField(col);
//        dbComponent.refreshDatabase();
    }


    public void setFieldHidden(String fieldName, boolean isHidden) {
        try{
            table.setFieldHidden(fieldName, isHidden);
        }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in DBTable setFieldHidden(): (1)");}
        catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }


    protected void UISetFieldHidden(String fieldName) {
        if (table.isHiddenFieldsDisplayed())
            return;

        TableColumn col = jTable.getColumn(fieldName);
        try{
            AbstractTableField f = table.getTableField(fieldName);
            if (!f.isHidden()) {
                col.setMaxWidth(FieldView.DEFAULT_FIELD_MAX_WIDTH);
                col.setPreferredWidth(FieldView.DEFAULT_FIELD_WIDTH);
//                col.setWidth(FieldView.DEFAULT_FIELD_WIDTH);
                col.setMinWidth(FieldView.DEFAULT_FIELD_MIN_WIDTH);
//                f.UIProperties.put(0, new Integer(110));
//                f.UIProperties.put(1, new Integer(40));
//                f.UIProperties.put(2, new Integer(1000));
//                col.setResizable(true);
                int hiddenColIndex = jTable.getColumnModel().getColumnIndex(fieldName);
                for (int m=0; m<colSelectionStatus.size(); m++)
                    colSelectionStatus.set(m, Boolean.FALSE);
                colSelectionStatus.set(hiddenColIndex, Boolean.TRUE);
                jTable.getColumnModel().getSelectionModel().setSelectionInterval(hiddenColIndex, hiddenColIndex);
            }else{
//1                FieldView fieldView = viewStructure.getFieldView(f.getName());
                col.setMinWidth(0);
                col.setPreferredWidth(0);
//                col.setWidth(0);
                col.setMaxWidth(0);
//                f.UIProperties.put(0, new Integer(0));
//1                fieldView.setFieldWidth(0);
//                f.UIProperties.put(1, new Integer(0));
//1                fieldView.setFieldMinWidth(0);
//                f.UIProperties.put(2, new Integer(0));
//1                fieldView.setFieldMaxWidth(0);
//                col.setResizable(false);

                // Select the first visible column in the jTable
                int firstVisibleColumnIndex = 0;
                AbstractTableField fld;
//1                Array columnOrder = viewStructure.tableView.getColumnOrder();
//1                for (; firstVisibleColumnIndex < tableColumns.size(); firstVisibleColumnIndex++) {
//1                    col = (TableColumn) tableColumns.get(firstVisibleColumnIndex);
                for (; firstVisibleColumnIndex < jTable.getColumnCount(); firstVisibleColumnIndex++) {
                    col = jTable.getColumnModel().getColumn(firstVisibleColumnIndex);
                    int fieldIndex = jTable.convertColumnIndexToModel(firstVisibleColumnIndex);
                    try{
                        fld = table.getTableField(fieldIndex);
//1                        fld = table.getTableField(columnOrder.get(firstVisibleColumnIndex));
                        if (!fld.isHidden())
                            break;
                    }catch (InvalidFieldIndexException exc) {
                        System.out.println("Serious inconsistency error in DBTable UISetFieldHidden : (1)");
                    }
                }
                if (firstVisibleColumnIndex < tableColumns.size()) {
                    for (int m=0; m<colSelectionStatus.size(); m++)
                        colSelectionStatus.set(m, Boolean.FALSE);
                    colSelectionStatus.set(firstVisibleColumnIndex, Boolean.TRUE);
                    jTable.getColumnModel().getSelectionModel().setSelectionInterval(firstVisibleColumnIndex, firstVisibleColumnIndex);
                }else{
                    for (int m=0; m<colSelectionStatus.size(); m++)
                        colSelectionStatus.set(m, Boolean.FALSE);
                    jTable.getColumnModel().getSelectionModel().clearSelection();
                }
            }
        }catch (InvalidFieldNameException exc) {
            System.out.println("Serious inconsistency error in DBTable UISetFieldHidden(): (2)");
        }
    }


    protected void UIDisplayHiddenFields(boolean display) {
        TableColumn col;
        AbstractTableField fld1;
//1        Array columnOrder = viewStructure.tableView.getColumnOrder();
        if (display) {
//1            for (int i=0; i<tableColumns.size(); i++) {
            for (int i=0; i<jTable.getColumnCount(); i++) {
                col = jTable.getColumnModel().getColumn(i);
//1                col = (TableColumn) tableColumns.get(i);
                try{
//1                    fld1 = table.getTableField(columnOrder.get(i));
                    fld1 = table.getTableField(jTable.convertColumnIndexToModel(i));
                    if (fld1.isHidden()) {
                        col.setMaxWidth(FieldView.DEFAULT_FIELD_MAX_WIDTH);
                        col.setPreferredWidth(FieldView.DEFAULT_FIELD_WIDTH);
//                        col.setWidth(FieldView.DEFAULT_FIELD_WIDTH);
                        col.setMinWidth(FieldView.DEFAULT_FIELD_MIN_WIDTH);
//                        col.setResizable(true);
                    }
                }catch (InvalidFieldIndexException exc) {
                    System.out.println("Serious inconsistency error in DBTable UIDisplayHiddenFields(): 1");
                }
            }
        }else{
//1            for (int i=0; i<tableColumns.size(); i++) {
            for (int i=0; i<jTable.getColumnCount(); i++) {
//1                col = (TableColumn) tableColumns.get(i);
                col = jTable.getColumnModel().getColumn(i);
                try{
                    fld1 = table.getTableField(jTable.convertColumnIndexToModel(i));
//1                    fld1 = table.getTableField(columnOrder.get(i));
                    if (fld1.isHidden()) {
                        col.setMinWidth(0);
                        col.setPreferredWidth(0);
//                        col.setWidth(0);
                        col.setMaxWidth(0);
                    }
                }catch (InvalidFieldIndexException exc) {
                    System.out.println("Serious inconsistency error in DBTable UIDisplayHiddenFields(): 1");
                }
            }

            // Set the selected column, if the currently selected one becomes invisible.
            int selColIndex = jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
//            if (selColIndex == -1) //No column selected
//                selColIndex = 0;
            if (jTable.getSelectedColumnCount()!=0) {
                if (jTable.getSelectedColumnCount() > 1) {
                    for (int m=0; m<colSelectionStatus.size(); m++)
                        colSelectionStatus.set(m, Boolean.FALSE);
                    jTable.getColumnModel().getSelectionModel().clearSelection();
                }else{
                    try{
//1                        fld1 = table.getTableField(columnOrder.get(selColIndex));
                        fld1 = table.getTableField(jTable.convertColumnIndexToModel(selColIndex));
                        if (fld1.isHidden()) {
                            // Select the first visible column in the jTable
                            int firstVisibleColumnIndex = 0;
                            AbstractTableField fld;
                            for (; firstVisibleColumnIndex < tableColumns.size(); firstVisibleColumnIndex++) {
                                col = tableColumns.get(firstVisibleColumnIndex).tableColumn;
                                try{
//                                    fld = table.getTableField(columnOrder.get(firstVisibleColumnIndex));
                                    fld = table.getTableField(jTable.convertColumnIndexToModel(firstVisibleColumnIndex));
                                    if (!fld.isHidden()) {
                                        break;
                                    }
                                }catch (InvalidFieldIndexException exc) {
                                    System.out.println("Serious inconsistency error in DBTable UISetFieldHidden : (2)");
                                }
                            }

                            if (firstVisibleColumnIndex < tableColumns.size()) {
                                for (int m=0; m<colSelectionStatus.size(); m++)
                                    colSelectionStatus.set(m, Boolean.FALSE);
                                colSelectionStatus.set(firstVisibleColumnIndex, Boolean.TRUE);
                                jTable.getColumnModel().getSelectionModel().setSelectionInterval(firstVisibleColumnIndex, firstVisibleColumnIndex);
                            }else{
                                for (int m=0; m<colSelectionStatus.size(); m++)
                                    colSelectionStatus.set(m, Boolean.FALSE);
                                jTable.getColumnModel().getSelectionModel().clearSelection();
                            }
                        }
                    }catch (InvalidFieldIndexException exc) {
                        System.out.println("Serious inconsistency error in DBTable UISetFieldHidden : (3)");
                        for (int m=0; m<colSelectionStatus.size(); m++)
                            colSelectionStatus.set(m, Boolean.FALSE);
                        jTable.getColumnModel().getSelectionModel().clearSelection();
                    }
                }
            }
        }
        jTable.getTableHeader().resizeAndRepaint();
        jTable.revalidate();
        refresh();
    }


    public void setCell(String fieldName, int recIndex, Object value) {
        try{
            table.setCell(fieldName, recIndex, value);
        }catch (InvalidFieldNameException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (InvalidCellAddressException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        catch (NullTableKeyException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        catch (InvalidDataFormatException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        catch (DuplicateKeyException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(this, e1.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
//        ((DBTableModel) jTable.getModel()).setValueAt(value, recIndex, columnIndex);
    }


    protected void acceptNewRecord() {
        /* Add an new entry to array "recordIndex".
        */
/*        int[] tmp = new int[dbTable.getRecordCount()];
for (int i =0; i<tmp.length-1; i++)
tmp[i] = recordIndex[i];
tmp[tmp.length-1] = dbTable.getRecordCount()-1;
recordIndex = tmp;
*/

        scrollpane.getViewport().validate();
        scrollpane.getViewport().doLayout();
//        scrollpane.getViewport().paintImmediately(scrollpane.getViewport().getVisibleRect());
        scrollpane.getViewport().repaint(scrollpane.getViewport().getVisibleRect());
        refresh();
//        jTable.scrollRectToVisible(jTable.getCellRect(jTable.getRowCount(), 0, true)); //newfp.getBounds());
//1        dbComponent.refreshDatabase();
    }

    protected void addEmptyRecord() {
        try{
            tableModel.addRow();
        }catch (Exception e) {
            ESlateOptionPane.showMessageDialog(this, e.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }


    protected void addRecord(Table ctable, int recordIndex) {
        ArrayList recordData;
        try{
            recordData = ctable.getRecord(recordIndex);
        }catch (InvalidRecordIndexException e) {return;}

        AbstractTableField f;
        int k=0;
        for (int i=0; i<ctable.getFieldCount(); i++) {
            try{
                f = ctable.getTableField(i);
                if (f.isCalculated()) {
                    recordData.remove(i-k);
                    k++;
                }
            }catch (InvalidFieldIndexException e) {
                System.out.println("Serious inconsistency error in DBTable addNewRecord(): (1)");
                return;
            }
        }
        try{
            this.table.addRecord(recordData, false);
        }catch (InvalidDataFormatException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        catch (NullTableKeyException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        catch (TableNotExpandableException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        catch (DuplicateKeyException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        catch (NoFieldsInTableException exc) {
            ESlateOptionPane.showMessageDialog(this, exc.message, bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }


    protected void selectRecord(int recordNumber) {
        if (recordNumber < 0 || recordNumber >= table.getRecordCount()) {
            ESlateOptionPane.showMessageDialog(this, bundle.getString("DatabaseMsg90"), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selCol = jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
        /* If the jTable is being edited, stop cell editing.
        */
        stopCellEditing();

        /* When "simultaneousFieldRecordActivation" is "false", column selection is not allowed.
        * However in the special case, when a field header is clicked, we set "columnSelectionAllowed"
        * to "true", so that the column is selected. "columnSelectionAllowed" turns again to "false"
        * when VK_LEFT/VK_RIGHT/VK_UP/VK_DOWN is pressed or the user clicks inside the jTable.
        * Turning "columnSelectionAllowed" to false, resets all te existing selections (both
        * row and column) in the jTable, so we keep track of them and reselect them afterwards.
        */
        if (jTable.getColumnSelectionAllowed() != isSimultaneousFieldRecordActivation()) {
            int[] selectedRows = jTable.getSelectedRows();
//            int selectedColumn = -1;
//            selectedColumn = jTable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
            jTable.setColumnSelectionAllowed(false);
            for (int i=0; i<colSelectionStatus.size(); i++) {
                if (((Boolean) colSelectionStatus.get(i)).booleanValue())
                    jTable.getColumnModel().getSelectionModel().setSelectionInterval(i, i);
            }
//            if (selectedColumn != -1)
//                jTable.getColumnModel().getSelectionModel().setSelectionInterval(selectedColumn, selectedColumn);
            for (int k=0; k<selectedRows.length; k++)
                jTable.getSelectionModel().addSelectionInterval(selectedRows[k], selectedRows[k]);
        }

        if (selCol != -1)
            jTable.scrollRectToVisible(jTable.getCellRect(recordNumber, selCol, true));
        else
            jTable.scrollRectToVisible(jTable.getCellRect(recordNumber, 0, true));
        iterateEvent = false;
        table.setSelectedSubset(table.recordIndex.get(recordNumber));
        setActiveRow(recordNumber);
    }

    public void refreshRows(int[] rowIndices) {
        Rectangle rectToRefresh = null;
        int fieldCount = table.getFieldCount();
        for (int k=0; k<rowIndices.length; k++) {
            for (int i=0; i<fieldCount; i++) {
                if (rectToRefresh == null)
                    rectToRefresh = jTable.getCellRect(rowIndices[k], i, true);
                else
                    rectToRefresh.add(jTable.getCellRect(rowIndices[k], i, true));
//                System.out.print(rowIndices[k]);
            }
        }

//        jTable.paintImmediately(rectToRefresh);
        jTable.repaint(rectToRefresh);
    }


    public void refreshRows(Array rowIndices) {
        Rectangle rectToRefresh = null;
        int fieldCount = table.getFieldCount();
        for (int k=0; k<rowIndices.size(); k++) {
            for (int i=0; i<fieldCount; i++) {
                if (rectToRefresh == null)
                    rectToRefresh = jTable.getCellRect(((Integer) rowIndices.at(k)).intValue(), i, true);
                else
                    rectToRefresh.add(jTable.getCellRect(((Integer) rowIndices.at(k)).intValue(), i, true));
            }
        }

        jTable.repaint(rectToRefresh);
//        jTable.paintImmediately(rectToRefresh);
    }


    public void refreshRecord(int recordIndex) {
        Rectangle rectToRefresh = null;
        IntArray recordIndexArray = new IntArray(table.recordIndex.toArray());
        int rowIndex = recordIndexArray.indexOf(new Integer(recordIndex));
        int fieldCount = table.getFieldCount();
        for (int i=0; i<fieldCount; i++) {
            if (rectToRefresh == null)
                rectToRefresh = jTable.getCellRect(rowIndex, i, true);
            else
                rectToRefresh.add(jTable.getCellRect(rowIndex, i, true));
        }

        jTable.repaint(rectToRefresh);
//        jTable.paintImmediately(rectToRefresh);
    }


    public void refreshRecords(Array recordIndices) {
        Rectangle rectToRefresh = null;
        IntArray recordIndexArray = new IntArray(table.recordIndex.toArray());
        int fieldCount = table.getFieldCount();
        for (int k=0; k<recordIndices.size(); k++) {
            int rowIndex = recordIndexArray.indexOf(recordIndices.at(k));
            for (int i=0; i<fieldCount; i++) {
                if (rectToRefresh == null)
                    rectToRefresh = jTable.getCellRect(rowIndex, i, true);
                else
                    rectToRefresh.add(jTable.getCellRect(rowIndex, i, true));
            }
        }
        jTable.repaint(rectToRefresh);
//        jTable.paintImmediately(rectToRefresh);
    }


    /* Returns the indices of the rows at which the Table records at recordIndices
    * are displayed.
    */
    public int[] rowsForRecords(int[] recordIndices) {
        IntArray recordIndexArray = new IntArray(table.recordIndex.toArray());
        int[] rowIndices = new int[recordIndices.length];
        for (int i=0; i<recordIndices.length; i++)
            rowIndices[i] = recordIndexArray.indexOf(new Integer(recordIndices[i]));
        return rowIndices;
    }


    /* Returns the index of the record which is displayed inside <i>row</i> of the DBTable.
    */
    public int recordForRow(int row) {
        return table.recordIndex.get(row);
    }

    /** Returns the AbstractTableField behind the column at <code>columnIndexInView</code>.
     *  <code>columnIndexInView</code> is the position of the column in the view (JTable).
     */
    public AbstractTableField getTableField(int columnIndexInView) {
        return tableColumns.get(columnIndexInView).tableField;
//cf        return tableFields.get(jTable.convertColumnIndexToModel(columnIndexInView));
    }

    /** Returns the TableColumn which displays the AbstractTableField at the specified index in the
     *  model.
     */
    public TableColumn getColumn(int fieldIndexInModel) {
        return jTable.getColumnModel().getColumn(jTable.convertColumnIndexToView(fieldIndexInModel));
    }

    /**
     * Returns the AbstractDBTableColumn with the specified name.
     * @param columnName
     * @return
     */
    public AbstractDBTableColumn getDBTableColumn(String columnName) {
        for (int i=0; i<tableColumns.size(); i++) {
            if (tableColumns.get(i).tableField.getName().equals(columnName))
                return tableColumns.get(i);
        }
        return null;
    }

    /**
     * Returns a copy of the array of AbstractDBTableColumns of the DBTable.
     * @return The AbstractDBTableColumns of the DBTable.
     */
    public DBTableColumnBaseArray getDBTableColumns() {
        return (DBTableColumnBaseArray) tableColumns.clone();
    }

    public void setActiveRow(int row) {
        if (row == activeRow) return;
        if (row < 0 || row >= table.recordIndex.size())
            table.setActiveRecord(-1);
        else{
            scrollToActiveRecord = false;
            table.setActiveRecord(table.recordIndex.get(row));
            scrollToActiveRecord = true;
        }
    }
    
    public void setActiveCell(int row, int column, boolean autoScroll) {
        if (autoScroll)
            jTable.scrollRectToVisible(jTable.getCellRect(row, column, true));
        colSelectionStatus.set(column, Boolean.TRUE);
        jTable.getColumnModel().getSelectionModel().setSelectionInterval(column, column);
        setActiveRow(row);
//        jTable.repaint();
    }

    /** Scrolls the table only in the vertical direction, so that <code>row</code> becomes visible. */
    public void scrollVertically(int row) {
        Rectangle rect = jTable.getCellRect(row, 0, true);
        rect.x = scrollpane.getViewport().getViewPosition().x;
        jTable.scrollRectToVisible(rect);
    }

    /** Copies the contents of the cells at <code>row</code>, <code>column</code> to the system clipboard.
     *  Image copying is supported only on Windows. The value of the cell is set to <code>null</code>.
     */
    public void cutCellContents(int row, int column) {
        if (!table.isDataChangeAllowed())
            return;
        if (row < 0 || row >= table.getRecordCount() || column < 0 || column >= table.getFieldCount())
            return;
        int fieldIndex = jTable.convertColumnIndexToModel(column);
        AbstractTableField field = tableColumns.get(column).tableField; //cf tableFields.get(fieldIndex);
        int recordIndex = table.recordForRow(row);
        if (!field.isEditable())
            return;

        Object cellContent = tableModel.getValueAt(row, column); //)table.riskyGetCell(fieldIndex, recordIndex);
        if (cellContent == null)
            return;

        setClipboardContent(cellContent);
        tableModel.setValueAt(null, row, column);
    }

    /** Copies the contents of the cells at <code>row</code>, <code>column</code> to the system clipboard.
     *  Image copying is supported only on Windows.
     */
    public void copyCellContents(int row, int column) {
        if (row < 0 || row >= table.getRecordCount() || column < 0 || column >= table.getFieldCount())
            return;
        int recordIndex = table.recordForRow(row);
        int fieldIndex = jTable.convertColumnIndexToModel(column);
        AbstractTableField field = tableColumns.get(column).tableField; //cf tableFields.get(fieldIndex);
        Object cellContent = tableModel.getValueAt(row, column); // table.riskyGetCell(fieldIndex, recordIndex);
        if (cellContent == null)
            return;
        setClipboardContent(cellContent);
        pasteAction.setEnabled(true);
        //dbComponent.standardToolBar.pasteMItem.setEnabled(true);
        //dbComponent.standardToolbarController.setPasteEnabled(true);
    }

    boolean runningOnWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    private void setClipboardContent(Object value) {
        if (value == null) return;
        if (runningOnWindows() && value.getClass().equals(CImageIcon.class)) {
            NewRestorableImageIcon icon = new NewRestorableImageIcon(((CImageIcon) value).getImage());
            gr.cti.eslate.utils.ClipboardFunctions.copyToClipboard(icon);
            icon.getImage().flush();
//            System.out.println("copyCell icon: " + icon.getIconWidth() + ", row: " + row + ", column: " + column);
            icon = null;
        }else{
            String strValue = value.toString();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection ss = new StringSelection(strValue);
            clipboard.setContents(ss, this);
        }
    }

    /** Pastes the contents of the system clipboard to the cell at <code>row</code>, <code>column</code>. Pasting
     *  images is supported only on Windows. Paste fails if the Table or the target field are not editable.
     */
    public void pasteCellContents(int row, int column) {
        if (row < 0 || row >= table.getRecordCount() || column < 0 || column >= table.getFieldCount())
            return;
        if (!table.isDataChangeAllowed())
            return;
        int fieldIndex = jTable.convertColumnIndexToModel(column);
        AbstractTableField field = tableColumns.get(column).tableField; //cf tableFields.get(fieldIndex);
        if (!field.isEditable())
            return;

        Object transferableToPaste = null;
        if (field.getDataType().equals(CImageIcon.class)) {
            if (runningOnWindows() && gr.cti.eslate.utils.ClipboardFunctions.existImageInClipboard()) {
                try{
                File tmpFile = File.createTempFile("test"+new java.util.Random().nextInt(), ".bmp");
                String fileName = tmpFile.getAbsolutePath(); //"c:\\test_" + new java.util.Random().nextInt() + ".bmp";
                gr.cti.eslate.utils.ClipboardFunctions.pasteFromClipboard(fileName);
                NewRestorableImageIcon icon = new NewRestorableImageIcon(fileName);
                transferableToPaste = new CImageIcon();
                ((CImageIcon) transferableToPaste).setImage(icon.getImage());
//                        System.out.println("pasteCell icon: " + icon.getIconWidth() + ", row: " + row + ", column: " + column);
                icon.getImage().flush();
                icon = null;
                tmpFile.deleteOnExit();
                }catch (IOException exc) {}
            }else
                return;
        }else{
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable clipboardContent = clipboard.getContents(this);
            if ((clipboardContent != null) && (clipboardContent.isDataFlavorSupported (DataFlavor.stringFlavor))) {
                try {
                    transferableToPaste = (String) clipboardContent.getTransferData(DataFlavor.stringFlavor);
                }catch (Exception e) {
                    e.printStackTrace ();
                }
            }
        }
        if (transferableToPaste != null) {
            tableModel.setValueAt(transferableToPaste, row, column);
            cutAction.setEnabled(true);
            copyAction.setEnabled(true);
        }
    }

    public void lostOwnership (Clipboard parClipboard, Transferable parTransferable) {
       	// System.out.println ("Lost ownership");
    }

    protected void columnSelectionChangedAction() {
/*        if (activeColumn == jTable.getSelectedColumn())
return;
else
activeColumn = jTable.getSelectedColumn();
*/
        if (activeColumn != jTable.getSelectedColumn())
            activeColumn = jTable.getSelectedColumn();

        updateColumnHeaderSelection();

        jTable.getTableHeader().repaint();
        //dbComponent.standardToolBar.setSortButtonStatus();
        if (dbComponent != null) {
            dbComponent.setSortButtonStatus();
            //dbComponent.standardToolBar.setCutCopyPasteStatus();
            dbComponent.setCutCopyPasteStatus();
            dbComponent.standardToolbarController.updateIconEditStatus();
            dbComponent.standardToolbarController.updateInsertFilePathStatus();
        }
    }


    protected void updateColumnHeaderSelection() {
        int[] indices = jTable.getSelectedColumns();

        TableColumn col1;
        AbstractTableField fld;
        HeaderRenderer hr;
//System.out.println("updateColumnHeaderSelection() colSelectionStatus.size(): " + colSelectionStatus.size() + ", tableColumns.size(): " + tableColumns.size());
        for (int i=0; i<colSelectionStatus.size(); i++) {
            AbstractDBTableColumn column = tableColumns.get(i);
            col1 = column.tableColumn;
            hr = (HeaderRenderer) col1.getHeaderRenderer(); //.headerLabel;
            fld = column.tableField; //cf (AbstractTableField) tableFields.get(col1.getModelIndex());
            if (((Boolean) colSelectionStatus.get(i)).booleanValue()) {
                hr.setBackground(selectedHeaderBackgroundColor);
                if (!fld.isCalculated())
                    hr.setForeground(headerForeground); //color128);
            }else{
                hr.setBackground(headerBackground); //Color.lightGray);
                if (!fld.isCalculated())
                    hr.setForeground(headerForeground);
            }
        }
    }

    public String toString() {
        if (table == null)
            return "No Table";
        return table.getTitle();
    }

    /** Enables or disables the use of the 'star-like' button on the upper-left corner of
     *  each jTable in the Database, to change the expansion status of the jTable's header.
     */
    public void setTableHeaderExpansionChangeAllowed(boolean allowed) {
        if (tableHeaderExpansionChangeAllowed == allowed) return;
        tableHeaderExpansionChangeAllowed = allowed;
        if (ucp != null)
            ucp.repaint();
        setModified(true);
    }

    /** Reports if the expansion status of the header of every jTable in the Database can
     *  be changed.
     */
    public boolean isTableHeaderExpansionChangeAllowed() {
        return tableHeaderExpansionChangeAllowed;
    }

    /** Adjusts the ability to sort the DBTable on one of its columns by clicking on the column's header. */
    public void setSortFromColumnHeadersEnabled(boolean enabled) {
        if (enabled == sortFromColumnHeadersEnabled) return;
        sortFromColumnHeadersEnabled = enabled;
        setModified(true);
    }

    /** Reports if the DBTable can be sorted by clicking on a column's header. */
    public boolean isSortFromColumnHeadersEnabled() {
        return sortFromColumnHeadersEnabled;
    }

    /** The table pop-up menu is displayed when the JTable is right-clicked. This happens only if it is enabled. */
    public void setTablePopupEnabled(boolean enabled) {
        if (tablePopupEnabled == enabled) return;
        tablePopupEnabled = enabled;
        setModified(true);
    }

    /** Returns if the table pop-up menu is enabled. */
    public boolean isTablePopupEnabled() {
        return tablePopupEnabled;
    }

    /** The column pop-up menu is displayed when the header of a column is right-clicked. This happens only if it is enabled. */
    public void setColumnPopupEnabled(boolean enabled) {
        if (columnPopupEnabled == enabled) return;
        columnPopupEnabled = enabled;
        if (columnPopupEnabled && columnPopup != null) {
            if (ESlateOptionPane.showConfirmDialog(this, bundle.getString("ResetColumnPopup"), bundle.getString("ResetColumnPopupTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                if (handle != null) {
                    handle.remove(columnPopup.getESlateHandle());
                    columnPopup.getESlateHandle().dispose();
                }
                columnPopup = null;
            }
        }
        setModified(true);
    }

    /** Returns if the column pop-up menu is enabled. */
    public boolean isColumnPopupEnabled() {
        return columnPopupEnabled;
    }

    /** Sets the font of the headers of the columns, which are not calculated. */
    public void setHeaderFont(Font f) {
        if (f == null) return;
        if (headerFont.equals(f)) return;
        headerFont = f;
        if (jTable != null) {
            for (int i=0; i<tableColumns.size(); i++) {
                if (tableColumns.get(i).tableField.isCalculated()) continue;
                ((HeaderRenderer) tableColumns.get(i).tableColumn.getHeaderRenderer()).updateHeaderFont();
            }
/*            TableFieldBaseArray tableFields = table.getFields();
            for (int i=0; i<jTable.getColumnCount(); i++) {
                TableColumn col = jTable.getColumnModel().getColumn(i);
                int fieldIndex = jTable.convertColumnIndexToModel(i);
                if (tableFields.get(fieldIndex).isCalculated()) continue;
                ((HeaderRenderer) col.getHeaderRenderer()).updateHeaderFont();
            }
*/
            jTable.getTableHeader().resizeAndRepaint();
        }
        setModified(true);
    }

    /** Returns the font of the headers of the columns, which are not calculated. */
    public Font getHeaderFont() {
        return headerFont;
    }

    /** Sets the font of the headers of the columns, which are calculated. */
    public void setCalculatedFieldHeaderFont(Font f) {
        if (f == null) return;
        if (calculatedFieldHeaderFont.equals(f)) return;
        calculatedFieldHeaderFont = f;
        if (jTable != null) {
            for (int i=0; i<tableColumns.size(); i++) {
                if (!tableColumns.get(i).tableField.isCalculated()) continue;
                ((HeaderRenderer) tableColumns.get(i).tableColumn.getHeaderRenderer()).updateCalculatedFieldHeaderFont();
            }
/*            TableFieldBaseArray tableFields = table.getFields();
            for (int i=0; i<jTable.getColumnCount(); i++) {
                TableColumn col = jTable.getColumnModel().getColumn(i);
                int fieldIndex = jTable.convertColumnIndexToModel(i);
                if (!tableFields.get(fieldIndex).isCalculated()) continue;
                ((HeaderRenderer) col.getHeaderRenderer()).updateCalculatedFieldHeaderFont();
            }
*/
            jTable.getTableHeader().resizeAndRepaint();
        }
        setModified(true);
    }

    /** Returns the font of the headers of the columns, which are calculated. */
    public Font getCalculatedFieldHeaderFont() {
        return calculatedFieldHeaderFont;
    }
    /** Sets the foreground of the headers of the columns, which are calculated. */
    public void setCalculatedFieldHeaderForeground(Color c) {
        if (c == null) return;
        if (c.equals(calculatedFieldHeaderForeground)) return;
        calculatedFieldHeaderForeground = c;
        TableFieldBaseArray tableFields = table.getFields();
        for (int i=0; i<jTable.getColumnCount(); i++) {
            TableColumn col = jTable.getColumnModel().getColumn(i);
            int fieldIndex = jTable.convertColumnIndexToModel(i);
            if (!tableFields.get(fieldIndex).isCalculated()) continue;
            ((HeaderRenderer) col.getHeaderRenderer()).updateCalculatedFieldHeaderFont();
        }
        jTable.getTableHeader().repaint();
        setModified(true);
    }

    /** Returns the foreground of the headers of the columns, which are calculated. */
    public Color getCalculatedFieldHeaderForeground() {
        return calculatedFieldHeaderForeground;
    }

    /** Sets the foreground of the headers of the columns, which are not calculated. */
    public void setHeaderForeground(Color c) {
        if (c == null) return;
        if (c.equals(headerForeground)) return;
        headerForeground = c;
        if (jTable != null) {
            TableFieldBaseArray tableFields = table.getFields();
            for (int i=0; i<jTable.getColumnCount(); i++) {
                TableColumn col = jTable.getColumnModel().getColumn(i);
                int fieldIndex = jTable.convertColumnIndexToModel(i);
                if (tableFields.get(fieldIndex).isCalculated()) continue;
                ((HeaderRenderer) col.getHeaderRenderer()).updateHeaderFont();
            }
            jTable.getTableHeader().repaint();
        }
        setModified(true);
    }

    /** Returns the foreground of the headers of the columns, which are not calculated. */
    public Color getHeaderForeground() {
        return headerForeground;
    }

    /** Sets the background of the headers of the columns, which are not calculated. */
    public void setHeaderBackground(Color c) {
        if (c == null) return;
        if (headerBackground.equals(c)) return;
        headerBackground = c;
        if (jTable != null) {
            for (int i=0; i<tableColumns.size(); i++) {
                TableColumn col = tableColumns.get(i).tableColumn;
                ((HeaderRenderer) col.getHeaderRenderer()).setBackground(headerBackground);
            }
    /*        for (int i=0; i<jTable.getColumnCount(); i++) {
                TableColumn col = jTable.getColumnModel().getColumn(i);
                int fieldIndex = jTable.convertColumnIndexToModel(i);
                ((HeaderRenderer) col.getHeaderRenderer()).updateHeaderFont();
            }
    */
            jTable.getTableHeader().repaint();
        }
        setModified(true);
    }

    /** Returns the background of the headers of the columns, which are not calculated. */
    public Color getHeaderBackground() {
        return headerBackground;
    }

    /** Sets the bright color used by the vertical row bar of the DBTable. */
    public void setVerticalRowBarBrightColor(Color c) {
        if (c == null) return;
        if (verticalRowBarBrightColor.equals(c)) return;
        verticalRowBarBrightColor = c;
        if (rowBar != null)
            rowBar.repaint();
        setModified(true);
    }

    /** Returns the bright color used by the DBTable's vertical row bar. */
    public Color getVerticalRowBarBrightColor() {
        return verticalRowBarBrightColor;
    }

    /** Sets the background color used by the vertical row bar of the DBTable. */
    public void setVerticalRowBarColor(Color c) {
        if (c == null) return;
        if (c.equals(verticalRowBarColor)) return;
        verticalRowBarColor = c;
        if (rowBar != null)
            rowBar.repaint();
        setModified(true);
    }

    /** Returns the background color used by the DBTable's vertical row bar. */
    public Color getVerticalRowBarColor() {
        return verticalRowBarColor;
    }

    /** Sets the dark color used by the vertical row bar of the DBTable. */
    public void setVerticalRowBarDarkColor(Color c) {
        if (c == null) return;
        if (c.equals(verticalRowBarDarkColor)) return;
        verticalRowBarDarkColor = c;
        if (rowBar != null)
            rowBar.repaint();
        setModified(true);
    }

    /** Returns the dark color used by the DBTable's vertical row bar. */
    public Color getVerticalRowBarDarkColor() {
        return verticalRowBarDarkColor;
    }

    /** Returns the way the active record is indicated in the DBTable.
     *  @see #setActiveRecordDrawMode(int)
     */
    public int getActiveRecordDrawMode() {
        return activeRecordDrawMode;
    }

    /** Specifies the way the active record is indicated in the DBTable.
     *  @param activeRecordDrawMode The mode according to which the active row is displayed. One of:
     *  <ul>
     *  <li> NO_INDICATION
     *  <li> ON_ROW_BAR_ONLY
     *  <li> ON_TABLE_ONLY
     *  <li> ON_ROW_BAR_AND_TABLE
     *  </ul>
     */
    public void setActiveRecordDrawMode(int activeRecordDrawMode) {
        if (activeRecordDrawMode != NO_INDICATION && activeRecordDrawMode != ON_ROW_BAR_ONLY &&
                activeRecordDrawMode != ON_ROW_BAR_AND_TABLE &&
                activeRecordDrawMode != ON_TABLE_ONLY)
            return;
        this.activeRecordDrawMode = activeRecordDrawMode;
        if (jTable != null) {
            boolean higlightActiveRecordInTable = (activeRecordDrawMode == ON_TABLE_ONLY || activeRecordDrawMode == ON_ROW_BAR_AND_TABLE);
            TableColumnModel tcm = jTable.getColumnModel();
            for (int i=0; i<tcm.getColumnCount(); i++)
                ((DBCellRenderer) tcm.getColumn(i).getCellRenderer()).setActiveRecordBackgroundDrawn(higlightActiveRecordInTable);
            if (activeRow != -1)
                refreshRow(activeRow, true);
            if (activeRecordDrawMode == ON_ROW_BAR_AND_TABLE || activeRecordDrawMode == ON_ROW_BAR_ONLY)
                rowBar.displayActiveRecord = true;
            else
                rowBar.displayActiveRecord = false;
        }
    }

    /** Returns the way the selected records is indicated in the DBTable.
     *  @see #setSelectedRecordDrawMode(int)
     */
    public int getSelectedRecordDrawMode() {
        return selectedRecordDrawMode;
    }

    /** Specifies the way the selected records are indicated in the DBTable.
     *  @param selectedRecordDrawMode The mode according to which the selected rows are displayed. One of:
     *  <ul>
     *  <li> NO_INDICATION
     *  <li> ON_ROW_BAR_ONLY
     *  <li> ON_TABLE_ONLY
     *  <li> ON_ROW_BAR_AND_TABLE
     *  </ul>
     */
    public void setSelectedRecordDrawMode(int selectedRecordDrawMode) {
        if (selectedRecordDrawMode != NO_INDICATION && selectedRecordDrawMode != ON_ROW_BAR_ONLY &&
                selectedRecordDrawMode != ON_ROW_BAR_AND_TABLE &&
                selectedRecordDrawMode != ON_TABLE_ONLY)
            return;
        this.selectedRecordDrawMode = selectedRecordDrawMode;
        if (jTable != null) {
            boolean higlightSelectedRecordsInTable = (selectedRecordDrawMode == ON_TABLE_ONLY || selectedRecordDrawMode == ON_ROW_BAR_AND_TABLE);
            TableColumnModel tcm = jTable.getColumnModel();
            for (int i=0; i<tcm.getColumnCount(); i++)
                ((DBCellRenderer) tcm.getColumn(i).getCellRenderer()).setSelectedRecordsBackgroundDrawn(higlightSelectedRecordsInTable);
            if (selectedRecordDrawMode == ON_ROW_BAR_AND_TABLE || selectedRecordDrawMode == ON_ROW_BAR_ONLY)
                rowBar.displayRecordSelection = true;
            else
                rowBar.displayRecordSelection = false;
            jTable.repaint();
            rowBar.repaint();
        }
    }

    public boolean isEditing() {
        return jTable.isEditing();
    }

    /** Sets the ability to change the record selection from within the UI of the DBTable. */
    public void setRecordSelectionChangeAllowed(boolean allowed) {
        if (recordSelectionChangeAllowed == allowed) return;
        recordSelectionChangeAllowed = allowed;
        setModified(true);
    }

    /** Reports if the record selection an be changed through the UI of the DBTable. */
    public boolean isRecordSelectionChangeAllowed() {
        return recordSelectionChangeAllowed;
    }

/*    protected void resetEditing() {
isEditing = false;
}
*/
/*1    public void setEditing(boolean editing) {
        isEditing = editing;
    }
*/

//1    public IntBaseArray getColumnOrder() {
//1        return columnOrder;
//1        return (Array) viewStructure.tableView.getColumnOrder().clone();
//1    }

    public void updateUI() {
        super.updateUI();
        if (scrollpane != null) {
            scrollpane.updateUI();
            scrollpane.setBorder(null);
            if (simultaneousFieldRecordActivation/*viewStructure.tableView.isSimultaneousFieldRecordActivation()*/)
                selectedHeaderBackgroundColor = SELECTED_HEADER_BGRD_GREEN;
            else
                selectedHeaderBackgroundColor = headerBackground;
//            System.out.println("updateUI() headerBgrColor: " + headerBgrColor);
//            System.out.println("updateUI() selectedHeaderBackgroundColor: " + selectedHeaderBackgroundColor);
        }
        if (tablePopup != null) {
            if (tablePopup.cutMItem != null)
                tablePopup.cutMItem.updateUI();
            if (tablePopup.copyMItem != null)
                tablePopup.copyMItem.updateUI();
            if (tablePopup.pasteMItem != null)
                tablePopup.pasteMItem.updateUI();
            if (tablePopup.iconEditMItem != null)
                tablePopup.iconEditMItem.updateUI();
        }
        verticalRowBarBrightColor = UIManager.getColor("controlLtHighlight");
        verticalRowBarColor = UIManager.getColor("control");
        verticalRowBarDarkColor = UIManager.getColor("controlShadow");
    }

    ESlateFileDialog getIconFileDialog() {
        if (iconFileDialog == null) {
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this);
            iconFileDialog = new ESlateFileDialog(topLevelFrame);
            iconFileDialog.setTitle(bundle.getString("DBTableMsg7"));
            iconFileDialog.setMode(FileDialog.LOAD);
            String[] extensions = new String[2];
            extensions[0] = "gif";
            extensions[1] = "jpg";
            iconFileDialog.setDefaultExtension(extensions);
            iconFileDialog.setFile("*.gif; *.jpg");
        }
        return iconFileDialog;
    }

    SearchManager getSearchManager() {
        if (searchManager == null)
            searchManager = new SearchManager();
        return searchManager;
    }

    /** Searches the DBTable for cells which contain the <code>token</code>. The search can be parameterized.
     *  @param token The token to search for.
     *  @param searchProps The parameters of the search.
     *  @param startFromRow The row from which the search will start.
     *  @param startFromColumn The column from which the search will start.
     *  @param includeCell Determines if the cell at <code>startFromRow</code>, <code>startFromColumn</code>
     *         will be included in the search.
     *  @return The address of the cell which was found, or null if no cell was found. The address of the cell
     *          is provided in the coordinates of the DBTable and not the underlying Table.
     *  @see   TableSearchProperties
     */
    public CellAddress find(String token, TableSearchProperties searchProps, int startFromRow, int startFromColumn, boolean includeCell) {
        searchManager.ignoreCase = searchProps.ignoreCase;
        searchManager.selectedFieldsOnly = searchProps.selectedFieldsOnly;
        searchManager.selectedRecordsOnly = searchProps.selectedRecordsOnly;
        searchManager.upwards = searchProps.upwards;
        searchManager.setToken(token);
        return searchManager.find(startFromRow, startFromColumn, includeCell);
    }

    /** Searches the DBTable for cells which contain the <code>token</code>. The search is performed with the
     *  parameters of the last performed search.
     *  @param token The token to search for.
     *  @param startFromColumn The column from which the search will start.
     *  @param includeCell Determines if the cell at <code>startFromRow</code>, <code>startFromColumn</code>
     *         will be included in the search.
     *  @return The address of the cell which was found, or null if no cell was found. The address of the cell
     *          is provided in the coordinates of the DBTable and not the underlying Table.
     *  @see   #find(String, TableSearchProperties, int, int, boolean)
     */
    public CellAddress find(String token, int startFromRow, int startFromColumn, boolean includeCell) {
        searchManager.setToken(token);
        return searchManager.find(startFromRow, startFromColumn, includeCell);
    }

    /** Searches the DBTable for cells which contain the <code>token</code>. The search is performed with the
     *  parameters of the last performed search.
     *  @param token The token to search for.
     *  @param startFromRow The row from which the search will start.
     *  @param startFromColumn The column from which the search will start.
     *  @param includeCell Determines if the cell at <code>startFromRow</code>, <code>startFromColumn</code>
     *         will be included in the search.
     *  @param upwards The direction of the search.
     *  @return The address of the cell which was found, or null if no cell was found. The address of the cell
     *          is provided in the coordinates of the DBTable and not the underlying Table.
     *  @see   #find(String, TableSearchProperties, int, int, boolean)
     */
    public CellAddress find(String token, int startFromRow, int startFromColumn, boolean includeCell, boolean upwards) {
        searchManager.setToken(token);
        searchManager.upwards = upwards;
        return searchManager.find(startFromRow, startFromColumn, includeCell);
    }

    /** Searches the DBTable for cells which contain the <code>token</code>. The search is performed with the
     *  parameters of the last performed search. This version of find is used by the <code>TableFindNextAction'
     *  </code> and <code>TableFindPrevAction</code> actions. It is for internal use.
     *  @param startFromRow The row from which the search will start.
     *  @param startFromColumn The column from which the search will start.
     *  @param includeCell Determines if the cell at <code>startFromRow</code>, <code>startFromColumn</code>
     *         will be included in the search.
     *  @param upwards The direction of the search.
     *  @return The address of the cell which was found, or null if no cell was found. The address of the cell
     *          is provided in the coordinates of the DBTable and not the underlying Table.
     *  @see   #find(String, TableSearchProperties, int, int, boolean)
     */
    CellAddress find(int startFromRow, int startFromColumn, boolean includeCell, boolean upwards) {
        getSearchManager().upwards = upwards;
        return searchManager.find(startFromRow, startFromColumn, includeCell);
    }

    class SearchManager {
        boolean ignoreCase = true;
        boolean selectedFieldsOnly = false;
        boolean selectedRecordsOnly = false;
        boolean upwards = false;
        private String token = null;

        /** Reports if the SEerachManager is enabled. The manager is enabled if there are records in the table and
         *  if the 'token' to search for has been set.
         * @return The enabled status of the manager.
         */
        public boolean isEnabled() {
            return (token != null && table.getRecordCount() != 0);
        }

        void setToken(String value) {
            if (value == null || value.length() == 0)
                value = null;
            token = value;
            if (token == null) {
                findPrevAction.setEnabled(false);
                findNextAction.setEnabled(false);
                if (dbComponent != null) {
                    dbComponent.findPrevAction.setEnabled(false);
                    dbComponent.findNextAction.setEnabled(false);
                }
            }else{
                findPrevAction.setEnabled(true);
                findNextAction.setEnabled(true);
                if (dbComponent != null) {
                    dbComponent.findPrevAction.setEnabled(true);
                    dbComponent.findNextAction.setEnabled(true);
                }
            }
        }

        String getToken() {
            return token;
        }

        CellAddress find(int startFromRow, int startFromColumn, boolean includeCell) {
            if (token == null || startFromRow < 0 || startFromColumn < 0)
                return null;
            if (table.getRecordCount() == 0)
                return null;
//System.out.println("startFromRow: " + startFromRow);

            // Find the fields which will be searched.
            // The fields in the array have to be arranged according to their visual order.
            StringBaseArray namesOfColumnsToBeSearched = new StringBaseArray();
            if (selectedFieldsOnly) {
                int[] selectedColumnIndices = jTable.getSelectedColumns();
//                int selectedColumn = jTable.getSelectedColumn();
//                Array columnOrder = viewStructure.tableView.getColumnOrder();
                int[] fieldIndices = jTable.getSelectedColumns();
                for (int i=0; i<fieldIndices.length; i++)
                    namesOfColumnsToBeSearched.add(jTable.getColumnName(selectedColumnIndices[i])); //tableModel.getColumnName(((Integer) columnOrder.at(fieldIndices[i])).intValue()));
            }else{
                for (int i=0; i<jTable.getColumnCount(); i++) {
                    namesOfColumnsToBeSearched.add(jTable.getColumnName(i)); //tableModel.getColumnName(((Integer) columnOrder.at(i)).intValue()));
                }
            }
//System.out.println("namesOfColumnsToBeSearched: " + namesOfColumnsToBeSearched);
            if (namesOfColumnsToBeSearched.size() == 0)
                return null;

            // Find the records which will be searched.
            // The records in the 'recordIndicesToBeSearched' have to be arranged according to their visual order.
            IntBaseArray recordIndicesToBeSearched = null;
            if (selectedRecordsOnly) {
                recordIndicesToBeSearched = new IntBaseArray(table.getSelectedSubset().size());
                int rowCount = table.getRecordCount();
                for (int i=0; i<rowCount; i++) {
                    int record = table.recordForRow(i);
                    if (table.isRecordSelected(record)) {
                        recordIndicesToBeSearched.add(record);
                    }
                }
            }else{
                int rowCount = table.getRecordCount();
                recordIndicesToBeSearched = new IntBaseArray(rowCount);
                for (int i=0; i<rowCount; i++)
                    recordIndicesToBeSearched.add(table.recordForRow(i));
            }

            if (recordIndicesToBeSearched.size() == 0)
                return null;

            // Find the record index and the field index, from which the search will start
            int indexInNamesOfColumnsToBeSearched = -1;

            int indexInRecordIndicesToBeSearched = -1;
            // We get the record index of startFromRow and search for it in the 'recordIndicesToBeSearched' array.
            int startFromRecIndex = table.recordForRow(startFromRow);
            indexInRecordIndicesToBeSearched = recordIndicesToBeSearched.indexOf(startFromRecIndex);
            // If the record from which search should start is not in the 'recordIndicesToBeSearched' array
            // and the flag 'selectedRecordsOnly' is set, then we try to find the row closest to 'startFromRow'
            // which is selected and start search from there.
            if (indexInRecordIndicesToBeSearched == -1 && selectedRecordsOnly) {
                if (upwards) {
                    // If the direction is upwards, then the closest selected row is the one we find traversing the
                    // 'recordIndicesToBeSearched' array which is less than 'startFromRow'.
                    int recordIndicesToBeSearchedSize = recordIndicesToBeSearched.size();
                    for (int k=recordIndicesToBeSearchedSize-1; k>=0; k--) {
                        if (startFromRow > table.rowForRecord(recordIndicesToBeSearched.get(k))) {
                            indexInRecordIndicesToBeSearched = k;
                            // In this case the column to start search from is the last column in the
                            // 'namesOfColumnsToBeSearched' array.
                            indexInNamesOfColumnsToBeSearched = tableColumns.size()-1;
                            break;
                        }
                    }
                }else{
                    // If the direction is downwards, then the closest selected row is the one we find traversing the
                    // 'recordIndicesToBeSearched' array which is greater than 'startFromRow'.
                    for (int k=0; k<recordIndicesToBeSearched.size(); k++) {
                        if (startFromRow < table.rowForRecord(recordIndicesToBeSearched.get(k))) {
                            indexInRecordIndicesToBeSearched = k;
                            // In this case the column to start search from is the first column in the
                            // 'namesOfColumnsToBeSearched' array.
                            indexInNamesOfColumnsToBeSearched = 0;
                            break;
                        }
                    }
                }
            }

            if (indexInRecordIndicesToBeSearched == -1 || indexInRecordIndicesToBeSearched >= recordIndicesToBeSearched.size())
                return null;

            // If the index of the field in the 'namesOfColumnsToBeSearched' array has not yet been set, then we
            // try to figure it out using 'startFromColumn'.
//System.out.println("startFromColumn: " + startFromColumn);
            if (indexInNamesOfColumnsToBeSearched == -1) {
                // If the cell at ('startFromRow', 'startFromColumn') should not be part of the search, then
                // we increase/decrease -based on the search direction- the 'startFromColumn'. If
                // 'startFromColumn' gets an invalid value -<0 or >namesOfColumnsToBeSearched.size()-
                // then we increase/decrease the 'indexInRecordIndicesToBeSearched'.
                if (!includeCell) {
                    if (upwards) {
                        startFromColumn--;
                        if (startFromColumn < 0) {
                            if (indexInRecordIndicesToBeSearched == 0)
                                return null;
                            indexInRecordIndicesToBeSearched--;
                            startFromColumn = namesOfColumnsToBeSearched.size()-1;
                        }
                    }else{
                        startFromColumn++;
                        if (startFromColumn >= namesOfColumnsToBeSearched.size()) {
                            if (indexInRecordIndicesToBeSearched == recordIndicesToBeSearched.size()-1)
                                return null;
                            indexInRecordIndicesToBeSearched++;
                            startFromColumn = 0;
                        }
                    }
                }
                indexInNamesOfColumnsToBeSearched = startFromColumn;
            }

            if (indexInNamesOfColumnsToBeSearched == -1 || indexInNamesOfColumnsToBeSearched >= namesOfColumnsToBeSearched.size())
                return null;

            try{
                CellAddress result = table.find(namesOfColumnsToBeSearched, recordIndicesToBeSearched.toArray(), token, ignoreCase, indexInNamesOfColumnsToBeSearched, indexInRecordIndicesToBeSearched, !upwards);
//try{
//System.out.println("result: " + result + "value: " + table.getCell(result.fieldIndex, result.recordIndex));
//}catch (Throwable thr) {}
                // Convert the cell co-ordinates from model to view (record/field to row/column)
                if (result != null) {
                    result.fieldIndex = jTable.convertColumnIndexToView(result.fieldIndex);
                    result.recordIndex = table.rowForRecord(result.recordIndex);
                }
                return result;
            }catch (Throwable thr) {
                thr.printStackTrace();
                return null;
            }
        }
    }

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
//System.out.println("DBTable writeExternal()");
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(saveTimer);

        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        // Save the Table only if it is a child of the DBTable and not simply hosted.
        if (table != null && table.getESlateHandle().getParentHandle() == getESlateHandle())
            fieldMap.put("Table", table);

        fieldMap.put("HeaderExpansionStateChangeAllowed", tableHeaderExpansionChangeAllowed);
        fieldMap.put("SortFromColumnHeadersEnabled", sortFromColumnHeadersEnabled);
        fieldMap.put("TablePopupEnabled", tablePopupEnabled);
        fieldMap.put("ColumnPopupEnabled", columnPopupEnabled);
        fieldMap.put("HeaderFont", headerFont);
        fieldMap.put("CalculatedFieldHeaderFont", calculatedFieldHeaderFont);
        fieldMap.put("HeaderForeground", headerForeground);
        fieldMap.put("CalculatedFieldHeaderForeground", calculatedFieldHeaderForeground);
        fieldMap.put("HeaderBackground", headerBackground);
        fieldMap.put("verticalRowBarDarkColor", verticalRowBarDarkColor);
        fieldMap.put("verticalRowBarColor", verticalRowBarColor);
        fieldMap.put("verticalRowBarBrightColor", verticalRowBarBrightColor);
        fieldMap.put("NonEditableFieldsHighlighted", nonEditableFieldsHighlighted);
        fieldMap.put("IntegerColor", integerColor);
        fieldMap.put("DoubleColor", doubleColor);
        fieldMap.put("FloatColor", floatColor);
        fieldMap.put("BooleanColor", booleanColor);
        fieldMap.put("StringColor", stringColor);
        fieldMap.put("DateColor", dateColor);
        fieldMap.put("TimeColor", timeColor);
        fieldMap.put("URLColor", urlColor);
        fieldMap.put("TableFont", tableFont);
        fieldMap.put("HighlightColor", highlightColor);
        fieldMap.put("SelectionBackground", selectionBackground);
        fieldMap.put("SelectionForeground", selectionForeground);
        fieldMap.put("GridColor", gridColor);
        fieldMap.put("backgroundColor", backgroundColor);
        fieldMap.put("RowHeight", rowHeight);
        fieldMap.put("HorizontalLinesVisible", horizontalLinesVisible);
        fieldMap.put("VerticalLinesVisible", verticalLinesVisible);
        fieldMap.put("SimultaneousFieldRecordSelection", simultaneousFieldRecordActivation);
        fieldMap.put("HeaderIconsVisible", headerIconsVisible);
        fieldMap.put("AutoResizeMode", autoResizeMode);
        fieldMap.put("ActiveRecordColor", activeRecordColor);
        fieldMap.put("TwoColorBackgroundEnabled", twoColorBackgroundEnabled);
        fieldMap.put("OddRowColor", oddRowColor);
        fieldMap.put("EvenRowColor", evenRowColor);
        fieldMap.put("RecordSelectionAllowed", recordSelectionChangeAllowed);
        fieldMap.put("ActiveRecordDrawMode", activeRecordDrawMode);
        fieldMap.put("SelectedRecordDrawMode", selectedRecordDrawMode);

        // Generate the array with the order of the columns in the JTable, if the DBTable contains a JTable.
        // This array is used to set the columns back to their proper position, when the DBTable is restored,
        // cause when the columns are created they are ordered base on the order of the AbstractTableFields in the Table.
        // The contents of the columnOrder are explained in the initializeTableColumns() method.
        // Also store the view properties of the DBTable's columns.
        IntBaseArray columnOrder = null;
        if (jTable != null) {
            columnOrder = new IntBaseArray();
            fieldViewPropertiesMap = new HashMap();
//System.out.println("Saving fieldViewPropertiesMap: " + fieldViewPropertiesMap);
            for (int i=0; i<jTable.getColumnCount(); i++) {
                columnOrder.add(jTable.convertColumnIndexToModel(i));
                TableColumn col = jTable.getColumnModel().getColumn(i);
                FieldViewProperties props = new FieldViewProperties();
                props.setFieldMaxWidth(col.getMaxWidth());
                props.setFieldMinWidth(col.getMinWidth());
                props.setFieldWidth(col.getWidth());
                props.setFieldPreferredWidth(col.getPreferredWidth());
                props.setResizable(col.getResizable());
                fieldViewPropertiesMap.put(jTable.getColumnName(i), props);
            }
        }
        fieldMap.put("ColumnOrder", columnOrder);
        fieldMap.put("FieldViewPropertiesMap", fieldViewPropertiesMap);

        // Store the state of the AbstractDBTableCOlumns of the DBTable
        ESlateFieldMap2[] maps = new ESlateFieldMap2[tableColumns.size()];
        for (int i=0; i<tableColumns.size(); i++)
            maps[i] = tableColumns.get(i).recordState();
        fieldMap.put("Table Column State", maps);

        // Save the DBTable's children
//        int childCount = 0;
        ArrayList children = new ArrayList();
        if (tablePopup != null) {
            fieldMap.put("TablePopup Name", tablePopup.getESlateHandle().getComponentName());
            children.add(tablePopup.getESlateHandle());
        }
        if (columnPopup != null) {
            fieldMap.put("ColumnPopup Name", columnPopup.getESlateHandle().getComponentName());
            children.add(columnPopup.getESlateHandle());
        }
        getESlateHandle().saveChildObjects(fieldMap, "DBTable children", children);

        out.writeObject(fieldMap);

        pm.stop(saveTimer);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
        setModified(false);
    }

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        ESlateHandle prevStaticParent = ESlateHandle.nextParent;
        ESlateHandle.nextParent = getESlateHandle();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(loadTimer);
        StorageStructure ss = (StorageStructure) in.readObject();
		int dataVersion = ss.getDataVersionID();

        // First restore the visual order of the columns, so that if the DBTable was hosting or was the parent
        // of a Table, the initializeTableColumns() method will restore the visual order of the columns.
        columnOrder = (IntBaseArray) ss.get("ColumnOrder");
        // Also restore the fields' view properties.
        fieldViewPropertiesMap = (HashMap) ss.get("FieldViewPropertiesMap");
        // Restore the state of the AbstractDBTableColumns
        columnState = (ESlateFieldMap2[]) ss.get("Table Column State");

        tableHeaderExpansionChangeAllowed = ss.get("HeaderExpansionStateChangeAllowed", true);
        sortFromColumnHeadersEnabled = ss.get("SortFromColumnHeadersEnabled", true);
        tablePopupEnabled = ss.get("TablePopupEnabled", true);
        columnPopupEnabled = ss.get("ColumnPopupEnabled", true);
        setHeaderFont((Font) ss.get("HeaderFont"));
        setCalculatedFieldHeaderFont((Font) ss.get("CalculatedFieldHeaderFont"));
        setHeaderForeground((Color) ss.get("HeaderForeground"));
        setCalculatedFieldHeaderForeground((Color) ss.get("CalculatedFieldHeaderForeground"));
        setHeaderBackground((Color) ss.get("HeaderBackground"));
        setVerticalRowBarDarkColor((Color) ss.get("verticalRowBarDarkColor"));
        setVerticalRowBarColor((Color) ss.get("verticalRowBarColor"));
        setVerticalRowBarBrightColor((Color) ss.get("verticalRowBarBrightColor"));
        setNonEditableFieldsHighlighted(ss.get("NonEditableFieldsHighlighted", false));
        integerColor = ss.get("IntegerColor", integerColor);
        doubleColor = ss.get("DoubleColor", doubleColor);
        floatColor = ss.get("FloatColor", floatColor);
        booleanColor = ss.get("BooleanColor", booleanColor);
        stringColor = ss.get("StringColor", stringColor);
        dateColor = ss.get("DateColor", dateColor);
        timeColor = ss.get("TimeColor", timeColor);
        urlColor = ss.get("URLColor", urlColor);
        setTableFont((Font) ss.get("TableFont", tableFont));
        setHighlightColor(ss.get("HighlightColor", highlightColor));
        setSelectionBackground(ss.get("SelectionBackground", selectionBackground));
        setSelectionForeground(ss.get("SelectionForeground", selectionForeground));
        setGridColor(ss.get("GridColor", gridColor));
        setBackgroundColor(ss.get("backgroundColor", backgroundColor));
        setRowHeight(ss.get("RowHeight", rowHeight));
        setHorizontalLinesVisible(ss.get("HorizontalLinesVisible", horizontalLinesVisible));
        setVerticalLinesVisible(ss.get("VerticalLinesVisible", verticalLinesVisible));
        setSimultaneousFieldRecordActivation(ss.get("SimultaneousFieldRecordSelection", simultaneousFieldRecordActivation));
        setHeaderIconsVisible(ss.get("HeaderIconsVisible", headerIconsVisible));
        setAutoResizeMode(ss.get("AutoResizeMode", autoResizeMode));
        setActiveRecordColor(ss.get("ActiveRecordColor", activeRecordColor));
        twoColorBackgroundEnabled = ss.get("TwoColorBackgroundEnabled", twoColorBackgroundEnabled);
        twoColorBackgroundEnabled = ss.get("TwoColorBackgroundEnabled", twoColorBackgroundEnabled);
        oddRowColor = ss.get("OddRowColor", oddRowColor);
        evenRowColor = ss.get("EvenRowColor", evenRowColor);
        recordSelectionChangeAllowed = ss.get("RecordSelectionAllowed", true);
        activeRecordDrawMode = ss.get("ActiveRecordDrawMode", ON_ROW_BAR_AND_TABLE);
        selectedRecordDrawMode = ss.get("SelectedRecordDrawMode", ON_ROW_BAR_AND_TABLE);

        Table t = (Table) ss.get("Table");
        if (t != null) {
            String tableTitle = t.getTitle();
            getESlateHandle().add(t.getESlateHandle());
            try{
                tableImportPlug.connectPlug(t.getTablePlug());
            }catch (PlugNotConnectedException exc) {exc.printStackTrace();}
            /* Re-apply the title of the Table, which may have changed as a result of
             * the ESlateHandle of the Table being entered in the defaultMicroworld and
             * then at the top level of the current microworld.
             */
            try{
                t.setTitle(tableTitle);
            }catch (Exception exc) {}
        }
//System.out.println("table: " + table + ", tableColumns: " + tableColumns);
        // Restore the DBTable's children
        Object[] children = getESlateHandle().restoreChildObjects(ss, "DBTable children");
        if (children != null && children.length != 0) {
            // If the 'tablePopup' was saved, then assign the variable 'tablePopup' to the ESlatePopupMenu which
            // was restored.
            String tablePopupName = (String) ss.get("TablePopup Name");
//System.out.println("tablePopupName: " + tablePopupName);
            if (tablePopupName != null) {
                if (dataVersion < 3) {
                    ESlateHandle popupMenuHandle = getESlateHandle().getChildHandle(tablePopupName);
                    getESlateHandle().remove(popupMenuHandle);
                    tablePopup = null;
                    getTablePopupMenu(0, 0);
                }else{
                    tablePopup = (DBTablePopupMenu) getESlateHandle().getChildHandle(tablePopupName).getComponent();
                    tablePopup.locateMenuItems(this);
                }
            }
            String columnPopupName = (String) ss.get("ColumnPopup Name");
//System.out.println("columnPopupName: " + columnPopupName);
            if (columnPopupName != null) {
                if (dataVersion < 3) {
                    ESlateHandle popupMenuHandle = getESlateHandle().getChildHandle(columnPopupName);
                    getESlateHandle().remove(popupMenuHandle);
                    columnPopup = null;
                    getColumnPopupMenu();
                }else{
                    columnPopup = (DBTableColPopupMenu) getESlateHandle().getChildHandle(columnPopupName).getComponent();
                    columnPopup.locateMenuItems(this);
                }
            }
        }

        pm.stop(loadTimer);
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
        ESlateHandle.nextParent = prevStaticParent;
        setModified(false);
    }

    /** Reports if the DBTable has been modified. */
    boolean isModified() {
        return modified;
    }

    /** Sets the modified flag of the DBTable. */
    void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * This method creates and adds a PerformanceListener to the E-Slate's
     * Performance Manager. The PerformanceListener attaches the component's
     * timers when the Performance Manager becomes enabled.
     */
    private void createPerformanceManagerListener(PerformanceManager pm) {
        if (perfListener != null) return;
        perfListener = new PerformanceAdapter() {
            public void performanceManagerStateChanged(PropertyChangeEvent e) {
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
        // Get the performance timer group for this component.
        PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);
        // Construct and attach the component's timers.
        constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, bundle.getString("ConstructorTimer"), true
        );
        loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, bundle.getString("LoadTimer"), true
        );
        saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, bundle.getString("SaveTimer"), true
        );
        initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, bundle.getString("InitESlateAspectTimer"), true
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

    public synchronized void addTransferHandlerListener(TransferHandlerListener thl) throws TooManyListenersException {
//System.out.println("DBTable addTransferHandlerListener() thl: " + thl);
        if (jTable == null) {
            transferHandlerListener = thl;
//Thread.currentThread().dumpStack();
        }else
            ((DBTableTransferHandler) jTable.getTransferHandler()).addTransferHandlerListener(thl);
    }

    public void removeTransferHandlerListener(TransferHandlerListener thl) {
        if (jTable == null)
            transferHandlerListener = null;
        else
            ((DBTableTransferHandler) jTable.getTransferHandler()).removeTransferHandlerListener(thl);
    }

    /**
     * Returns the vertical row bar which lays on the left side of the DBTable.
     * @return
     */
    public VerticalRowBar getVerticalRowBar() {
        return rowBar;
    }

    /** Re-arranges the columns of the DBTable, so that they match the order in which
     *  the names of the columns appear in the <code>fieldNames</code> array.
     * @param fieldNames A list with the new order of the columns of the JTable. The list must contain all the
     * TableColumns of the JTable.
     */
    public void setColumnOrder(StringBaseArray fieldNames) {
        // First get the names of all the columns of the JTable of the DBTable, in the order in which they appear
        Enumeration en = jTable.getColumnModel().getColumns();
        StringBaseArray columnNames = new StringBaseArray();
        while (en.hasMoreElements())
            columnNames.add((String) ((TableColumn) en.nextElement()).getIdentifier());
        // For each entry in the 'columnNames' make an entry in the 'order' base array, which contains the new index
        // the column should have in the JTable.
        IntBaseArray order = new IntBaseArray();
        for (int i=0; i<columnNames.size(); i++)
            order.add(fieldNames.indexOf(columnNames.get(i)));
/*
System.out.print("adjustDBTableColumnOrder() fieldNames: ");
for (int i=0; i<fieldNames.size(); i++)
    System.out.print(fieldNames.get(i) + ", ");
System.out.println("");
System.out.print("columnNames: ");
for (int i=0; i<columnNames.size(); i++)
    System.out.print(columnNames.get(i) + ", ");
System.out.println("");
System.out.print("order: ");
for (int i=0; i<order.size(); i++)
    System.out.print(order.get(i) + ", ");
System.out.println("");
*/

        // Move the columns. Tricky, have to do an example on paper to see how this works.
        for (int i=0; i<columnNames.size(); i++) {
            int oldIndex = order.indexOf(i);
//System.out.println("Moving from " + oldIndex + " to " + i);
            shift(order, oldIndex, i);
//print(order);
            jTable.moveColumn(oldIndex, i);
        }
    }

    /**
     * Shifts all the integers between <code>to</code> and <code>from</code> of the the specified IntBaseArray.
     * @param order The IntBaseArray.
     * @param from
     * @param to
     */
    void shift(IntBaseArray order, int from, int to) {
        if (from == to) return;
        // Shift upwards
        if (to > from) {
            int o = order.get(from);
            for (int i=from; i<to; i++) {
//                System.out.println("order.set(" + i + ") order.get(i+1): " + order.get(i+1));
                order.set(i, order.get(i+1));
            }
            order.set(to, o);
//            System.out.println("order.set(" + to + ") o: " + o);
        }else{ // Shift downwards
            int o = order.get(from);
            for (int i=from; i>to; i--) {
//                System.out.println("order.set(" + i + ") order.get(i-1): " + order.get(i-1));
                order.set(i, order.get(i-1));
            }
            order.set(to, o);
//            System.out.println("order.set(" + to + ") o: " + o);
        }
    }

    // Debug method
    private void print(IntBaseArray order) {
        System.out.print("order: ");
        for (int i=0; i<order.size(); i++)
            System.out.print(order.get(i) + ", ");
        System.out.println("");
    }

    /**
     * Makes sure that the DBTable contains only the columns whose names are contained in the supplied list. This
     * method will throw an exception if the model of the DBTable is a <code>gr.cti.eslate.database.engine.Table</code>
     * and not a <code>gr.cti.eslate.database.engine.TableView</code>. Removing a column from a <code>Table</code> is
     * permanent, while removing a column from a <code>TableView</code> is not!
     * @param fieldNames The list of the names of the columns that must be visible.
     */
    public void setColumns(StringBaseArray fieldNames) {
        if (!(table instanceof gr.cti.eslate.database.engine.TableView))
            throw new RuntimeException("setColumns() is available only when the underlying model is a TableView");
        gr.cti.eslate.database.engine.TableView tableView = (gr.cti.eslate.database.engine.TableView) table;
        StringBaseArray fldNames = (StringBaseArray) fieldNames.clone();
        try {
            StringBaseArray tableViewFieldNames = tableView.getFieldNames();
            for (int i=tableViewFieldNames.size()-1; i>=0; i--)
                tableView.setFieldRemovable(tableViewFieldNames.get(i), true);
            for (int i=0;i<tableViewFieldNames.size();i++) {
                String tableViewFieldName = tableViewFieldNames.get(i);
                if (fldNames.indexOf(tableViewFieldName) == -1) {
                    // Remove from tableView existent elements that don't exist at selectedTableFieldsListModel.
                    tableView.removeFieldFromView(tableView.getBaseTable().getFieldIndex(tableViewFieldName), false);
                }else
                    // Remove from selectedTableFieldsListModel common elements between tableView and selectedTableFieldsListModel.
                    fldNames.removeElements(tableViewFieldName);
            }
            // Go through remaining elements in fldNames.
            for (int i=0;i<fldNames.size();i++) {
                String fieldName = fldNames.get(i);
                int fieldIndex = tableView.getBaseTable().getFieldIndex(fieldName);
                // Add at tableView non existent elements that exist at selectedTableFieldsListModel.
                tableView.addFieldToView(fieldIndex, false);
            }
        }
        catch (Throwable ex) {
                ex.printStackTrace();
        }
    }

    /**
     * Exports the plug which is used to connect models (Tables and TableViews) to this DBTable.
     * @return
     */
    public FemaleSingleIFSingleConnectionProtocolPlug getTableImportPlug() {
        return tableImportPlug;
    }


    /**
     * Returns the DBTable's 'find' action, i.e. the action which shows the find dialog.
     * @return
     */
    public TableFindAction getFindAction() {
        return findAction;
    }

    /**
     * Returns the DBTable's 'find next' action.
     * @return
     */
    public TableFindNextAction getFindNextAction() {
        return findNextAction;
    }

    /**
     * Returns the DBTable's 'find previous' action.
     * @return
     */
    public TableFindPrevAction getFindPreviousAction() {
        return findPrevAction;
    }

    /**
     * Returns the DBTable's 'sort' action, i.e. the action which displays the sort-on-multiple-fields dialog.
     * @return
     */
    public TableSortAction getTableSortAction() {
        return tableSortAction;
    }

    /**
     * Returns the DBTable's 'export' action, i.e. the action which exports the Table of the Table Editor to a
     * text file.
     */
    public DBTableExportToTextFileAction getExportToTextFileAction() {
        return exportToTextFileAction;
    }

    /** Called whenever the number of rows in the table changes. */
    void rowNumberChanged() {
        if (table.getRecordCount() == 0) {
            if (findAction.isEnabled()) {
                findAction.setEnabled(false);
                findNextAction.setEnabled(false);
                findPrevAction.setEnabled(false);
                removeRecordAction.setEnabled(false);
            }
        }else{
            if (!findAction.isEnabled()) {
                findAction.setEnabled(true);
                findNextAction.setEnabled(true);
                findPrevAction.setEnabled(true);
            }
        }
/*        TableCutAction cutAction = null;
        TableEditableAction tableEditableAction = null;
        TableCopyAction copyAction = null;
        TablePasteAction pasteAction = null;
        TableImageEditAction imageEditAction = null;
        TableAddRecordAction addRecordAction = null;
        TableFindAction findAction = null;
        TableFindNextAction findNextAction = null;
        TableFindPrevAction findPrevAction = null;
        TableSortAction tableSortAction = null;
        TableSelectAllRecsAction selectAllRecsAction = null;
        TablePromoteSelRecsAction promoteSelRecsAction = null;
        TableFieldSelectAllAction fieldSelectAllAction = null;
        TableRemoveSelRecordAction removeSelRecordAction = null;
        FieldSortAction fieldSortAction = null;
        FieldSortAscendingAction fieldSortAscAction = null;
        FieldSortDescendingAction fieldSortDescAction = null;
        TableFieldPropertiesAction fieldPropertiesAction = null;
        TableAddFieldAction addFieldAction = null;
        TableRemoveFieldAction removeFieldAction = null;
        DBTablePreferencesAction tablePreferencesAction = null;
        DBTableInfoAction tableInfoAction = null;
        DBTableDescriptionAction tableDescriptionAction = null;
*/
    }

    public TableFieldPropertiesAction getFieldPropertiesAction() {
        return fieldPropertiesAction;
    }

    /**
     * Returns the row bar, i.e. the bar at the left of the table which contains a cell for each record and reports
     * the activation and selection status of the record (also adjusts this status).
     * @return
     */
    public VerticalRowBar getRowBar() {
        return rowBar;
    }


    /**
     * Displays a dialog through which the user can adjust the currency exchange rates with Euro as the basis. The
     * exchange rates are directly set to the supplied CurrencyManager
     * @param currencyManager The CurrencyManager to which the new exchange rates will be applied.
     * @param currencies The list of currencies for which the exhange rates will be set. The ids of the currencies are
     *                   described by the CyrrencyManager.
     * @param owner  The owner of the dialog.
     * @param component  The component around which the dialog will be centered.
     * @param rateDBTable The DBTable to be used as the UI of the exchange rate Table. The user of the method has the
     *        chance to pre-configure the DBTable as needed. If no DBTable is given, then a default DBTable with specific
     *        UI characteristics will be used.
     * @see CurrencyManager
     */
    public static void showCurrencyExchangeRateDialog(CurrencyManager currencyManager, int[] currencies, Frame owner, Component component, DBTable rateDBTable) {
        if (currencyManager == null) currencyManager = CurrencyManager.getCurrencyManager();
        final CurrencyManager _currencyManager = currencyManager;
        final int[] _currencies = currencies;
        if (_currencies == null || _currencies.length == 0) return;
        if (owner == null)
            owner = new JFrame();
//        if (component == null) component = owner;

        final Table table = new Table("Currency rates");
        final DoubleTableField rateField;
        try{
            AbstractTableField currencyField = table.addField("�������", StringTableField.DATA_TYPE, false, false, false);
            rateField = (DoubleTableField) table.addField("�������� �� Euro", DoubleTableField.DATA_TYPE, true, false, false);
            RecordEntryStructure res = table.getRecordEntryStructure();
            for (int i=0; i<_currencies.length; i++) {
                res.startRecordEntry();
                String symbol = _currencyManager.getSymbol(_currencies[i]);
                if (symbol == null || symbol.trim().length() == 0) {
                    res.cancelRecordAddition();
                    continue;
                }
                res.setCell(currencyField, symbol);
                res.setCell(rateField, _currencyManager.getExchangeRate(_currencies[i]));
                boolean moreRecsToBeAdded = (i==_currencies.length-1);
                res.commitNewRecord(moreRecsToBeAdded);
            }
            table.setRecordRemovalAllowed(false);
            table.setRecordAdditionAllowed(false);
            table.setFieldAdditionAllowed(false);
            table.setFieldRemovalAllowed(false);
        }catch (Throwable thr) { thr.printStackTrace(); return;}


        if (rateDBTable == null) {
            rateDBTable = new DBTable();
            rateDBTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            rateDBTable.setTableHeaderExpansionChangeAllowed(false);
            rateDBTable.setColumnPopupEnabled(false);
            rateDBTable.setTablePopupEnabled(false);
            rateDBTable.setSelectedRecordDrawMode(NO_INDICATION);
            rateDBTable.setActiveRecordDrawMode(ON_ROW_BAR_ONLY);
            rateDBTable.setHorizontalLinesVisible(false);
            rateDBTable.setVerticalLinesVisible(false);
            rateDBTable.setTwoColorBackgroundEnabled(true);
            rateDBTable.setHeaderFont(rateDBTable.getHeaderFont().deriveFont(Font.BOLD));
            Font tableFont = rateDBTable.getTableFont();
            rateDBTable.setTableFont(tableFont.deriveFont(Font.ITALIC, (float) tableFont.getSize()+4f));
            rateDBTable.setRowHeight(rateDBTable.getRowHeight()+4);
//            if (evenRowColor != null) rateDBTable.setEvenRowColor(evenRowColor);
//            if (oddRowColor != null) rateDBTable.setOddRowColor(oddRowColor);
        }
        rateDBTable.setTable(table, null);

        JButton okButton = new JButton("�������");
        JButton cancelButton = new JButton("�����");

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setBorder(new EmptyBorder(8, 0, 5, 0));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(16));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());

        final JDialog rateDialog = new JDialog(owner, "������� ���������", true);
        rateDialog.getContentPane().setLayout(new BorderLayout());
        rateDialog.getContentPane().add(rateDBTable, BorderLayout.CENTER);
        rateDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i=0; i<table.getRecordCount(); i++) {
                    try{
                        double rate = rateField.getCell(i).doubleValue();
                        _currencyManager.setExchangeRate(_currencies[i], rate);
                    }catch (Throwable thr) {thr.printStackTrace();}
                }
                rateDialog.dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rateDialog.dispose();
            }
        });

        rateDialog.setSize(300, 200);
        rateDialog.setLocationRelativeTo(component);
        rateDialog.show();
    }

    /** A subclass of the JTable, with customized key handling */
    class CustomJTable extends JTable {
        /** This flag is set while the left mouse button is pressed and before any dragging occurs. */
        boolean mousePressedNoDragOccured = false;
        // Stores the point at which the last mouse press occured
        Point mousePressAtPoint = new Point();

        public CustomJTable() {
//            enableEvents(AWTEvent.HIERARCHY_EVENT_MASK);
            // In 1.4.2 this is the only way to listen to componentResized events on the HeaderRenderers...
            getTableHeader().addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    TableColumn resizingCol = getTableHeader().getResizingColumn();
                    if (resizingCol == null) return;
                    HeaderRenderer hr = (HeaderRenderer) resizingCol.getHeaderRenderer();
                    hr.adjustHeaderHeightWhileResizing();
                }
            });

/*            getTableHeader().addHierarchyListener(new HierarchyListener() {
                public void hierarchyChanged(HierarchyEvent e) {
                    if ((e.getChangeFlags() | HierarchyEvent.SHOWING_CHANGED) == HierarchyEvent.SHOWING_CHANGED) {
                        System.out.println("TABLE HEADER visible");
                        if (table != null) {
                            if (isShowing()) {
                                public void run() {
                                    System.out.println("Showing table " + table.getTitle() + ", tableColumns.size(): " + tableColumns.size());
                                    for (int i=0; i<tableColumns.size(); i++)
                                        ((HeaderRenderer) tableColumns.get(i).tableColumn.getHeaderRenderer()).adjustHeaderHeight();
                                    jTable.getTableHeader().resizeAndRepaint();
                                }
                            }else
                                System.out.println("Hiding table " + table.getTitle());
                        }
                    }

                }

            });
*/
        }
/*        public void editingCanceled(ChangeEvent e) {
            super.editingCanceled(e);
            requestFocus();
System.out.println("Requesting focus 1");
        }

        public void editingStopped(ChangeEvent e) {
            super.editingStopped(e);
            requestDefaultFocus();
System.out.println("Requesting focus 2");
        }
*/

        protected void processMouseEvent(MouseEvent e) {
/*System.out.println("processMouseEvent() (e.getID() == MouseEvent.MOUSE_RELEASED): " + (e.getID() == MouseEvent.MOUSE_RELEASED));
            if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                mouseReleasedAtRow = jTable.rowAtPoint(e.getPoint());
                mouseReleasedAtColumn = jTable.columnAtPoint(e.getPoint());
                System.out.println("mouseReleasedAtRow: " + mouseReleasedAtRow);
            }
*/
            super.processMouseEvent(e);

//            System.out.println("processMouseEvent()");
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
//System.out.println("MOUSE_PRESSED");
                tipManager.resetTip();
                closeDatabasePopupMenu();

                if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e))
                    mousePressedNoDragOccured = true;
                else
                    mousePressedNoDragOccured = false;

                mousePressAtPoint = e.getPoint();
                mousePressedAtRowBck = mousePressedAtRow;
                mousePressedAtRow = jTable.rowAtPoint(mousePressAtPoint);
                mousePressedAtColumn = jTable.columnAtPoint(mousePressAtPoint);
                if (mousePressedAtRow == -1 || mousePressedAtColumn == -1)
                    return;

                /* When "simultaneousFieldRecordActivation" is "false", column selection is not allowed.
                * However in the special case, when a field header is clicked, we set "columnSelectionAllowed"
                * to "true", so that the column is selected. "columnSelectionAllowed" turns again to "false"
                * when VK_LEFT/VK_RIGHT/VK_UP/VK_DOWN is pressed or the user clicks inside the jTable.
                * Turning "columnSelectionAllowed" to false, resets all te existing selections (both
                * row and column) in the jTable, so we keep track of them and reselect them afterwards.
                */
                if (jTable.getColumnSelectionAllowed() != isSimultaneousFieldRecordActivation()) {
                    int[] selectedRows = jTable.getSelectedRows();
                    jTable.setColumnSelectionAllowed(false);
                    for (int i=0; i<colSelectionStatus.size(); i++) {
                        if (((Boolean) colSelectionStatus.get(i)).booleanValue())
                            jTable.getColumnModel().getSelectionModel().setSelectionInterval(i, i);
                    }
                    for (int i=0; i<selectedRows.length; i++)
                        jTable.getSelectionModel().addSelectionInterval(selectedRows[i], selectedRows[i]);
                }
                if (isEditing())
                    stopCellEditing();

                if (e.isShiftDown() || e.isControlDown()) {
                    int selColCount = 0;
                    for (int k=0; k<colSelectionStatus.size(); k++) {
                        if (((Boolean)  colSelectionStatus.get(k)).booleanValue())
                            selColCount++;
                    }
                    if ((!((Boolean) colSelectionStatus.get(mousePressedAtColumn)).booleanValue()) || selColCount > 1) {
                        for (int i=0; i<colSelectionStatus.size(); i++)
                            colSelectionStatus.set(i, Boolean.FALSE);
                        colSelectionStatus.set(mousePressedAtColumn, Boolean.TRUE);
                        jTable.getColumnModel().getSelectionModel().setSelectionInterval(mousePressedAtColumn, mousePressedAtColumn);
                        updateColumnHeaderSelection();
                    }
                }else{
//1                    if (!dbComponent.pendingNewRecord) {
                        int selColCount = 0;
                        for (int k=0; k<colSelectionStatus.size(); k++) {
                            if (((Boolean)  colSelectionStatus.get(k)).booleanValue())
                                selColCount++;
                        }
                        if ((!((Boolean) colSelectionStatus.get(mousePressedAtColumn)).booleanValue()) || selColCount > 1) {
                            for (int m=0; m<colSelectionStatus.size(); m++)
                                colSelectionStatus.set(m, Boolean.FALSE);
//                              jTable.getColumnModel().getSelectionModel().clearSelection();
                            colSelectionStatus.set(mousePressedAtColumn, Boolean.TRUE);
                            jTable.getColumnModel().getSelectionModel().setSelectionInterval(mousePressedAtColumn, mousePressedAtColumn);
                        }else{
                            /* The column is already selected, but it may already be actually selected by a click on the
                            * header. So set the column selection again, so that a refresh will be performed.
                            */
                            jTable.getColumnModel().getSelectionModel().clearSelection();
                            jTable.getColumnModel().getSelectionModel().setSelectionInterval(mousePressedAtColumn, mousePressedAtColumn);
                        }
                        setActiveRow(mousePressedAtRow);
//1                    }
                }

                if (/*1!dbComponent.pendingNewRecord && */e.getClickCount() == 2) {
                    int fieldIndex = tableColumns.get(mousePressedAtColumn).tableColumn.getModelIndex();
                    AbstractTableField fld = tableColumns.get(mousePressedAtColumn).tableField; //cf (AbstractTableField) tableFields.get(fieldIndex);
                    /* When a cell is right or left mouse clicked twice, editing starts.
                    * Stop cell editing, when it is the result of a double right-mouse click.
                    */
                    if (e.getModifiers() == e.BUTTON3_MASK) {
                        cancelCellEditing();
                    }

                    if (fld.isEditable() && table.isDataChangeAllowed()) {
                        DBTable.this.editCellAt(mousePressedAtRow, mousePressedAtColumn);
                        /* If the field is of Boolean data type, show the cell editor's pop-up
                        */
                    }
                    if (fld.getDataType().equals(CImageIcon.class)) {
                        if (iconFileDialog != null && iconFileDialog.getFile() != null) {
                            String fileName = iconFileDialog.getDirectory() + iconFileDialog.getFile();
                            ((DBTableModel) jTable.getModel()).setValueAt(fileName, mousePressedAtRowBck, fieldIndex);
                            jTable.repaint(jTable.getCellRect(mousePressedAtRowBck, fieldIndex, true));
                        }
                    }
                }
            }else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
//                System.out.println("MOUSE RELEASED");
                if (DBTable.this.isEditing() || (iconFileDialog != null && iconFileDialog.isVisible()))
                    return;
                mousePressedNoDragOccured = false;
                int rowIndex;
                if (e.isShiftDown()) {
                    rowIndex = jTable.getSelectedRow();
                    if (lastShiftClick != -1) {
                        if ((lastShiftClick > rowIndex && rowIndex >= mousePressedAtRow) || (lastShiftClick < rowIndex && rowIndex <= mousePressedAtRow))
                            rowIndex = lastShiftClick;
                        else if ((lastShiftClick > mousePressedAtRow && mousePressedAtRow >= rowIndex) || (lastShiftClick < mousePressedAtRow && mousePressedAtRow <= rowIndex))
                            rowIndex = lastShiftClick;
                    }

                    /* The last row at which the user shift-clicked is hold, due to the
                    * custom functionality of shift-clicking.
                    */
                    lastShiftClick = mousePressedAtRow;
                }else{
                    rowIndex = jTable.rowAtPoint(e.getPoint());
                    lastShiftClick = -1;
                }

                int numOfRows = (rowIndex > mousePressedAtRow)? rowIndex-mousePressedAtRow+1 : mousePressedAtRow-rowIndex+1;
                int startingIndex = (rowIndex > mousePressedAtRow)? mousePressedAtRow : rowIndex;

                if (rowIndex == -1)
                    return;

                if (e.getModifiers() == e.BUTTON3_MASK && tablePopupEnabled) {
                    getTablePopupMenu(mousePressedAtRow, mousePressedAtColumn).show(jTable, e.getX(), e.getY());
                }

                //Array recIndicesToBeSelected = new Array();
                IntBaseArray recIndicesToBeSelected = new IntBaseArray();
                //Array recIndicesToBeDeselected = new Array();
                IntBaseArray recIndicesToBeDeselected = new IntBaseArray();

                if (e.isControlDown() && !e.isShiftDown()) {
                    Array rowsToRefresh = new Array();
                    for (int i=0; i<numOfRows; i++) {
                        if (!table.isRecordSelected(table.recordIndex.get(startingIndex+i)))
                        //recIndicesToBeSelected.add(new Integer(dbTable.recordIndex[startingIndex+i]));
                            recIndicesToBeSelected.add(table.recordIndex.get(startingIndex+i));
                        else
                        //recIndicesToBeDeselected.add(new Integer(dbTable.recordIndex[startingIndex+i]));
                            recIndicesToBeDeselected.add(table.recordIndex.get(startingIndex+i));
                        rowsToRefresh.add(new Integer(startingIndex+i));
                    }

                    iterateEvent = false;
                    if (recordSelectionChangeAllowed)
                        table.addToSelectedSubset(recIndicesToBeSelected.toArray());
                    iterateEvent = false;
                    if (recordSelectionChangeAllowed)
                        table.removeFromSelectedSubset(recIndicesToBeDeselected.toArray());

                    setActiveRow(mousePressedAtRow);
                    if (!jTable.isColumnSelected(mousePressedAtColumn)) {
                        colSelectionStatus.set(mousePressedAtColumn, Boolean.TRUE);
                        jTable.getColumnModel().getSelectionModel().addSelectionInterval(mousePressedAtColumn, mousePressedAtColumn);
                    }

                    if (mousePressedAtRow != rowIndex) {
                        setActiveRow(rowIndex);
                        rowBar.repaint();
                    }else{
                        rowBar.resetActiveRow(mousePressedAtRow);
                        rowBar.drawActiveRow(rowBar.getGraphics(), false);
                    }

                    refreshRows(rowsToRefresh);
                }

                if (!e.isControlDown() && e.isShiftDown()) {
                    Array rowsToRefresh = new Array();
                    for (int i=0; i<numOfRows; i++) {
                        if (!table.isRecordSelected(table.recordIndex.get(startingIndex+i))) {
                            //recIndicesToBeSelected.add(new Integer(dbTable.recordIndex[startingIndex+i]));
                            recIndicesToBeSelected.add(table.recordIndex.get(startingIndex+i));
                            rowsToRefresh.add(new Integer(startingIndex+i));
                        }
                    }
                    iterateEvent = false;
                    if (recordSelectionChangeAllowed)
                        table.addToSelectedSubset(recIndicesToBeSelected.toArray());

                    setActiveRow(mousePressedAtRow);
                    if (!jTable.isColumnSelected(mousePressedAtColumn)) {
                        colSelectionStatus.set(mousePressedAtColumn, Boolean.FALSE);
                        jTable.getColumnModel().getSelectionModel().addSelectionInterval(mousePressedAtColumn, mousePressedAtColumn);
                    }

                    rowBar.repaint();
                    refreshRows(rowsToRefresh);
                }

                if (!e.isControlDown() && !e.isShiftDown() && mousePressedAtRow != rowIndex) {
                    //recIndicesToBeDeselected = (Array) dbTable.getSelectedSubset().clone();
                    recIndicesToBeDeselected = (IntBaseArray) table.getSelectedSubset().clone();
                    Array rowsToRefresh = new Array();
                    for (int i=0; i<numOfRows; i++) {
                        //recIndicesToBeSelected.add(new Integer(dbTable.recordIndex[startingIndex+i]));
                        recIndicesToBeSelected.add(table.recordIndex.get(startingIndex+i));
                        rowsToRefresh.add(new Integer(startingIndex+i));
                    }

                    /* Find those indices in recIndicesToBeDeselected, which also
                    * exist in recIndicesToBeSelected and remove them from recIndicesToBeDeselected.
                    */
                    for (int i=0; i<recIndicesToBeDeselected.size(); i++) {
                        //if (recIndicesToBeSelected.indexOf(recIndicesToBeDeselected.at(i)) != -1)
                        if (recIndicesToBeSelected.indexOf(recIndicesToBeDeselected.get(i)) != -1)
                            recIndicesToBeDeselected.remove(i);
                    }
                    iterateEvent = false;
                    if (recordSelectionChangeAllowed)
                        table.removeFromSelectedSubset(recIndicesToBeDeselected.toArray());
                    iterateEvent = false;
                    if (recordSelectionChangeAllowed)
                        table.addToSelectedSubset(recIndicesToBeSelected.toArray());

                    setActiveRow(rowIndex);
                    for (int l=0; l<recIndicesToBeDeselected.size(); l++)
                            //rowsToRefresh.add(recIndicesToBeDeselected.at(l));
                        rowsToRefresh.add(new Integer(recIndicesToBeDeselected.get(l)));
                    rowBar.repaint();
                    refreshRows(rowsToRefresh);
                }
                if (dbComponent != null)
                    dbComponent.updateFieldMenuRestStatus();
            }
        }

        protected void processMouseMotionEvent(MouseEvent e) {
            super.processMouseMotionEvent(e);
            // DnD will start only if the mouse has move more than 3 pixels away from where it was originally pressed
            // and if it's not already in process.
            if (e.getID() == MouseEvent.MOUSE_DRAGGED && mousePressedNoDragOccured) {
                if (mousePressAtPoint.distance(e.getPoint()) <= 3 )
                    return;
//                System.out.println("exportAsDrag()");
                TransferHandler th = getTransferHandler();
                th.exportAsDrag(this, e, TransferHandler.COPY);
                dropAtRow = -1;
                dropAtColumn = -1;
                mousePressedNoDragOccured = false;
            }
//            System.out.println("processMouseMotionEvent()");
        }
        /** Customized 'processKeyBinding()'. The normal JTable 'processKeyBinding()' starts cell editing
         *  (in the JTable way), when there is no Action defined for the processed keystroke.
         */
        protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
            InputMap map = getInputMap(condition);
            ActionMap am = getActionMap();

            if(map != null && am != null && isEnabled()) {
                Object binding = map.get(ks);
if (false)
    if (e.isControlDown())
        System.out.print("Ctrl+");
if (false)
    System.out.println(e.getKeyText(e.getKeyCode()) + "--> binding: " + binding + ", map: " + map + ", condition: " + condition);
                Action action = (binding == null) ? null : am.get(binding);
if (false)
    System.out.println("action: " + action + ", am: " + am);
                if (action != null) {
                    return SwingUtilities.notifyAction(action, ks, e, this, e.getModifiers());
                }
            }
            return super.processKeyBinding(ks, e, condition,  pressed);
        }

        /** Process the key events which start cell editing */
        protected void processComponentKeyEvent(KeyEvent e) {
//System.out.println("char: " + e.getKeyChar() + ", isActionKey(): " + e.isActionKey() + ", keyTyped(): " + (e.getID() == KeyEvent.KEY_TYPED));
            if (e.getID() != KeyEvent.KEY_PRESSED)
                return;
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                firstShiftUpDownKey = -1;
            }

//System.out.println("ActionKey: " + e.isActionKey() + ", text: " + e.getKeyText(e.getKeyCode()));
            // If the SHIFT key is down and 'e.getKeyText()' returns "Shift", then no other key is pressed together with SHIFT.
            if (e.isControlDown() || e.isAltDown() || e.isMetaDown() || (e.isShiftDown() && e.getKeyText(e.getKeyCode()).equals("Shift"))) {
                return;
            }
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_TAB ||
                    keyCode == KeyEvent.VK_DELETE)
                return;

            char c = e.getKeyChar();
            if (e.isActionKey())
                return;
//            e.consume();
            if (table.getRecordCount() == 0)
                return;
            if (activeRow == -1)
                return;
            int i = getSelectedColumn();
            int k = activeRow; //jTable.getSelectedRow();
            if (!isCellEditable(k, i))
                return;
            if (tableColumns.get(i).tableField.getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class))
                return;
//cf            if (((AbstractTableField) tableFields.get(((TableColumn) tableColumns.get(i)).getModelIndex())).getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class))
//cf                return;

            // This starts cell editing and selects the whole value of the cell.
            DBTable.this.editCellAt(k, i);

//cf            JComponent ec = ((ColumnRendererEditor) colRendererEditors.get(convertColumnIndexToModel(i))).editorComponent;
            JComponent ec = tableColumns.get(i).editorComponent;

//            ec.setVisible(true);
//            repaint(getCellRect(k, i, true));
//            ec.requestFocus();
//            ec.grabFocus();
//\            if (ec instanceof JTextField) {
//\                ((JTextField) ec).setText(String.valueOf(c));
//\            }
//            ec.setBackground(editedCellBackColor);
/*            if (ec.getClass().equals(javax.swing.JTextField.class)) {
                JTextField tf = (JTextField) ec;
                int textLength = tf.getText().length();
                tf.getCaret().setVisible(true);
                tf.setSelectionStart(tf.getText().length());
            }
*/
        }
    } // CustomJTable

}

/** This is the class of the TransferHandler use to move/copy cells and records of the DBTable to other destinations and
 *  import dragged data.
 */
class DBTableTransferHandler extends TransferHandler {
    DBTable dbTable = null;
    Point dropLocation = null, dragLocation = null;
    Component dragSource = null;
    ArrayList transferHandlerListeners = new ArrayList();

    public synchronized void addTransferHandlerListener(gr.cti.eslate.utils.TransferHandlerListener thl) throws TooManyListenersException {
        if (transferHandlerListeners.isEmpty()){
            transferHandlerListeners.add(thl);
        }else
            throw new TooManyListenersException("Only one TransferHandlerListener can be added to a TransferHandler");
    }

    public void removeTransferHandlerListener(gr.cti.eslate.utils.TransferHandlerListener thl) {
        transferHandlerListeners.remove(thl);
    }

    protected Transferable fireTransferableCreated(JComponent dragSource) {
        TransferableCreatedEvent tce = new TransferableCreatedEvent(this, dragSource);
        return ((gr.cti.eslate.utils.TransferHandlerListener) transferHandlerListeners.get(0)).transferableCreated(tce);
    }

    protected boolean fireDataImportAllowed(JComponent destination, DataFlavor[] flavors, Point dragLocation) {
        gr.cti.eslate.utils.DataImportAllowedEvent die = new gr.cti.eslate.utils.DataImportAllowedEvent(this, destination, flavors, dragLocation);
        return ((gr.cti.eslate.utils.TransferHandlerListener) transferHandlerListeners.get(0)).dataImportAllowed(die);
    }

    protected boolean fireDataImported(JComponent destination, Transferable transferable, Point dropLocation) {
        gr.cti.eslate.utils.DataImportedEvent die = new gr.cti.eslate.utils.DataImportedEvent(this, destination, transferable, dropLocation);
        return ((gr.cti.eslate.utils.TransferHandlerListener) transferHandlerListeners.get(0)).dataImported(die);
    }

    protected void fireDataExported(JComponent sourceComponent, Transferable transferable, int action) {
        gr.cti.eslate.utils.DataExportedEvent dee = new gr.cti.eslate.utils.DataExportedEvent(this, sourceComponent, transferable, action);
        ((gr.cti.eslate.utils.TransferHandlerListener) transferHandlerListeners.get(0)).dataExported(dee);
    }

    DBTableTransferHandler(DBTable dbTable) {
        this.dbTable = dbTable;
    }

    protected Transferable createTransferable(JComponent c) {
        // Creates a new Transferable object
        // with the correct DataFlavors etc.
        if (transferHandlerListeners.size() != 0) {
            Transferable transferable = fireTransferableCreated(c);
            if (transferable != null)
                return transferable;
        }
//System.out.println("mousePressedAtColumn: " + dbTable.mousePressedAtColumn + " mousePressedAtRow: " + dbTable.mousePressedAtRow);
        if (c instanceof JTable) {
            if (dbTable.mousePressedAtColumn == -1 || dbTable.mousePressedAtRow == -1)
                return null;
            Object value = dbTable.tableModel.getValueAt(dbTable.mousePressedAtRow, dbTable.mousePressedAtColumn);
            if (value == null)
                return null;
            // The exported value is always a String
//System.out.println("createTransferable() c: " + c.getClass() + ", StringSelection");
            dragSource = c;
            return new StringSelection(value.toString());
        }else if (c instanceof VerticalRowBar) {
//System.out.println("createTransferable() c: " + c.getClass() + ", TransferableRow");
            dragSource = c;
            return new TransferableRow(new DBTableRow(dbTable.getESlateHandle().getComponentPathName(), dbTable.activeRow));
        }
        dragSource = null;
        return null;
    }

    public int getSourceActions(JComponent c) {
        // Allows the data of the JTable to be both copied and moved.
        return COPY_OR_MOVE;
    }

    public boolean canImport(JComponent dest, DataFlavor[] flavors, Point location) {
        dragLocation = location;
        boolean canImport = canImport(dest, flavors);
        dragLocation = null;
        return canImport;
    }

    public boolean canImport(JComponent dest, DataFlavor[] flavors) {
        if (transferHandlerListeners.size() != 0) {
            if (fireDataImportAllowed(dest, flavors, dragLocation))
                return true;
        }
//System.out.println("DBTableTransferHandler canImport() dest: " + dest.getClass());
        if (dest instanceof JTable) {
            if (dragLocation != null && !isJTableDragOK(dragLocation))
                return false;
            // Can import any DataFlavor.
            return true;
        }else if (dest instanceof VerticalRowBar) {
//System.out.println("flavors.length: " + flavors.length);
            if (dragSource == dbTable.rowBar)
                return true;
            for (int i=0; i<flavors.length; i++) {
                if (flavors[i].getRepresentationClass() == TransferableRow.DBTABLE_ROW_FLAVOR.getRepresentationClass())
                    return true;
            }
        }else if (dest instanceof JTableHeader) {
            return false;
        }
        return false;
    }

    protected void exportDone(JComponent source, Transferable data, int action) {
        if (transferHandlerListeners.size() != 0)
            fireDataExported(source, data, action);
        if (source == dbTable.jTable) {
//System.out.println("mousePressedAtColumn: " + dbTable.mousePressedAtColumn + ", dropAtColumn: " + dbTable.dropAtColumn + ", mousePressedAtRow: " + dbTable.mousePressedAtRow + ", dropAtRow: " + dbTable.dropAtRow);
            if (dbTable.dropAtColumn == -1 || dbTable.dropAtRow == -1)
                return;
            if (dbTable.mousePressedAtColumn == dbTable.dropAtColumn && dbTable.mousePressedAtRow == dbTable.dropAtRow)
                return;  // Nothing to do, since the source and the destination of the drag is the same cell in the same JTable
        //System.out.println("action: " + action + " COPY: " + COPY + ", MOVE: " + MOVE + ", C_M: " + COPY_OR_MOVE + ", none: " + NONE);
            if (action == MOVE) {
                // Remove the data from the cell it was dragged from
        //System.out.println("exportDone() mousePressedAtColumn: " + mousePressedAtColumn + ", mousePressedAtRow: " + mousePressedAtRow);
                if (dbTable.mousePressedAtColumn == -1 || dbTable.mousePressedAtRow == -1)
                    return;
                dbTable.tableModel.setValueAt(null, dbTable.mousePressedAtRow, dbTable.mousePressedAtColumn);
            }
        }
        dragSource = null;
    }

    /** Called by the DropTargetListener installed on the DropTarget of the JTable. It imports
     *  the dropped data to the JTable.
     * @param src
     * @param transferable
     * @return
     */
    public boolean importData(JComponent src, Transferable transferable, Point point) {
        dropLocation = point;
        boolean successfulDrop = importData(src, transferable);
        dropLocation = null;
        return successfulDrop;
    }

    public boolean importData(JComponent src, Transferable transferable) {
//System.out.println("importData src: " + src.getClass());
        if (transferHandlerListeners.size() != 0) {
            if (fireDataImported(src, transferable, dropLocation))
                return true;
        }
        if (src instanceof VerticalRowBar) {
            if (src == dragSource || transferable.isDataFlavorSupported(TransferableRow.DBTABLE_ROW_FLAVOR)) {
                try{
                    DBTableRow dbTableRow = (DBTableRow) transferable.getTransferData(TransferableRow.DBTABLE_ROW_FLAVOR);
                    if (src == dbTable.rowBar && dropLocation != null) {
                        int droppedOnRow = dbTable.jTable.rowAtPoint(dropLocation);
                        int draggedRow = dbTableRow.getRow();
                        dbTable.table.moveRow(draggedRow, droppedOnRow);
                        return true;
                    }
                    return false;
                }catch (Throwable thr) {
                    return false;
                }
            }
            return false;
        }else{
            if (!dbTable.table.isDataChangeAllowed())
                return false;
            if (dbTable.dropAtColumn == -1 || dbTable.dropAtRow == -1)
                return false;
            if (!isJTableDragOK(dbTable.dropAtRow, dbTable.dropAtColumn))
                return false;

            // Ok, here's the tricky part...
    //            System.out.println("Receiving data from " + src);
    //            System.out.println("Transferable object is: " + transferable);
    //            System.out.println("Valid data flavors: ");
            DataFlavor[] flavors = transferable.getTransferDataFlavors();
            DataFlavor listFlavor = null;
            DataFlavor objectFlavor = null;
            DataFlavor readerFlavor = null;
            int lastFlavor = flavors.length - 1;

            // Check the flavors and see if we find one we like.
            // If we do, save it.
            for (int f = 0; f <= lastFlavor; f++) {
    //                System.out.println("  " + flavors[f]);
                if (flavors[f].isFlavorJavaFileListType()) {
                    listFlavor = flavors[f];
                }
                if (flavors[f].isFlavorSerializedObjectType()) {
                    objectFlavor = flavors[f];
                }
                if (flavors[f].isRepresentationClassReader()) {
                    readerFlavor = flavors[f];
                }
            }

            // Ok, now try to display the content of the drop.
            try {
                DataFlavor bestTextFlavor = DataFlavor.selectBestTextFlavor(flavors);
                BufferedReader br = null;
                if (bestTextFlavor != null) {
    //                    System.out.println("Best text flavor: " + bestTextFlavor.getMimeType());
    //                    System.out.println("Content:");
                    Reader r = bestTextFlavor.getReaderForText(transferable);
                    br = new BufferedReader(r);
                    String line = br.readLine();
                    StringBuffer buffer = new StringBuffer();
                    while (line != null) {
                        buffer.append(line);
    //                        System.out.println(line);
                        line = br.readLine();
                    }
                    br.close();
                    dbTable.tableModel.setValueAt(buffer.toString(), dbTable.dropAtRow, dbTable.dropAtColumn);
                }else if (listFlavor != null) {
                    java.util.List list = (java.util.List)transferable.getTransferData(listFlavor);
                    System.out.println(list);
                    StringBuffer buffer = new StringBuffer();
                    for (int i=0; i<list.size()-1; i++) {
                        Object obj = list.get(i);
                        if (obj == null)
                            buffer.append("null, ");
                        else
                            buffer.append(obj.toString() + ", ");
                    }
                    Object obj = list.get(list.size()-1);
                    if (obj == null)
                        buffer.append("null");
                    else
                        buffer.append(obj.toString());
                    dbTable.tableModel.setValueAt(buffer.toString(), dbTable.dropAtRow, dbTable.dropAtColumn);
                }else if (objectFlavor != null) {
                    Object data = transferable.getTransferData(objectFlavor);
    //                    System.out.println("Data is a java object:\n" + data);
                    if (data != null)
                        data = data.toString();
                    dbTable.tableModel.setValueAt(data, dbTable.dropAtRow, dbTable.dropAtColumn);
                }else if (readerFlavor != null) {
                    System.out.println("Data is an InputStream:");
                    StringBuffer buffer = new StringBuffer();
                    br = new BufferedReader((Reader)transferable.getTransferData(readerFlavor));
                    String s = br.readLine();
                    while (s != null) {
                        buffer.append(s);
                        s = br.readLine();
    //                        System.out.println(s);
                    }
                    br.close();
                    dbTable.tableModel.setValueAt(buffer.toString(), dbTable.dropAtRow, dbTable.dropAtColumn);
                }else {
                    // Don't know this flavor type yet...
                    System.out.println("No text representation to show.");
                }
    //                System.out.println("\n\n");
            }
            catch (Exception e) {
                System.out.println("Caught exception decoding transfer:");
                System.out.println(e);
                return false;
            }
            return true;
        }
    }

    private boolean isJTableDragOK(Point p) {
        if (!dbTable.table.isDataChangeAllowed())
            return false;
        int column = dbTable.jTable.columnAtPoint(p);
        int row = dbTable.jTable.rowAtPoint(p);
        return isJTableDragOK(row, column);
    }

    private boolean isJTableDragOK(int row, int column) {
//System.out.println("isDragOK() row: " + row + ", column: " + column);
        if (column == -1 || row == -1)
            return false;
        AbstractTableField field = dbTable.getTableField(column);
        if (field == null)
            return false;
        return field.isEditable();
    }

}




class UpperCornerPanel extends JPanel {
    protected DBTable dbTable;
    protected boolean mousePressed = false;
    Image asteriskImg = new ImageIcon(getClass().getResource("images/asterisk.gif")).getImage();
    int preferredWidth = 23;

    protected UpperCornerPanel(DBTable dbTable) {
        super(true);
        this.dbTable = dbTable;
        enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK);
        setRequestFocusEnabled(false);
//1        getBackground = dbTable.scrollpane.getBackground();
//1        getBackground = new Color(getBackground.getRed()-10, getBackground.getGreen()-10, getBackground.getBlue()-10);
//1        verticalRowBarDarkColor = getBackground.darker();

        // Tool tip does not work???
//t        setToolTipText(dbTable.dbComponent.infoBundle.getString("DBTableMsg5"));
    }

    public void paint(Graphics grph) {
        if (grph == null) return;
        int height = getSize().height;
        grph.setColor(dbTable.verticalRowBarColor); //new Color(175, 175, 175));
        grph.fillRect(1,2,preferredWidth-2,height-2);
        boolean headerExpansionChangeAllowed = dbTable.tableHeaderExpansionChangeAllowed;

        if (!mousePressed) {
            // Horizontal lines
            grph.setColor(dbTable.verticalRowBarBrightColor);
            grph.drawLine(1, 1, preferredWidth, 1);
            grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
            grph.drawLine(1, height-1, preferredWidth, height-1);

            // Vertical lines
            grph.setColor(dbTable.verticalRowBarBrightColor);
            grph.drawLine(1, 0, 1, height);
            grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
            grph.drawLine(preferredWidth-1, 0, preferredWidth-1, height);
            if (headerExpansionChangeAllowed)
                grph.drawImage(asteriskImg, 8, (height-9)/2, null);
        }else if (headerExpansionChangeAllowed) {
            grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
            grph.drawLine(1, 1, preferredWidth, 1);
            grph.setColor(dbTable.verticalRowBarColor); //Color.gray);
            grph.drawLine(1, 2, preferredWidth, 2);
            grph.setColor(dbTable.verticalRowBarBrightColor);
            grph.drawLine(1, height-1, preferredWidth, height-1);

            // Vertical lines
            grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
            grph.drawLine(1, 1, 1, height);
            grph.setColor(dbTable.verticalRowBarColor); //Color.gray);
            grph.drawLine(2, 1, 2, height);
            grph.setColor(dbTable.verticalRowBarColor); //new Color(200, 200, 200));
            grph.drawLine(preferredWidth-2, 1, preferredWidth-2, height);
            grph.setColor(dbTable.verticalRowBarBrightColor);
            grph.drawLine(preferredWidth-1, 1, preferredWidth-2, height);
            grph.drawImage(asteriskImg, 9, (height-9)/2+1, null);
        }
    }

    protected void processMouseEvent(MouseEvent e) {
        if (!dbTable.tableHeaderExpansionChangeAllowed) return;

        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            if (dbTable.dbComponent != null && dbTable.dbComponent.visiblePopupMenu != null && dbTable.dbComponent.visiblePopupMenu.isVisible())
                dbTable.dbComponent.visiblePopupMenu.setVisible(false);
            mousePressed = true;
            paint(getGraphics());
        }
        if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            if (this.getVisibleRect().contains(e.getPoint())) {
                boolean iconsVisible = dbTable.isHeaderIconsVisible();
/*t                if (!iconsVisible)
setToolTipText(dbTable.dbComponent.infoBundle.getString("DBTableMsg6"));
else
setToolTipText(dbTable.dbComponent.infoBundle.getString("DBTableMsg5"));
*/                dbTable.setHeaderIconsVisible(!iconsVisible);
            }

            mousePressed = false;
            paint(getGraphics());
        }
    }

    public boolean isFocusable() {
        return false;
    }
}


class LowerCornerPanel extends JPanel {
    DBTable dbTable;
    int preferredWidth = 23;

    protected LowerCornerPanel(DBTable dbTable) {
        super(true);
        this.dbTable = dbTable;
        setRequestFocusEnabled(false);
    }

    public void paint(Graphics grph) {
        if (grph == null) return;
        int height = getSize().height;
        grph.setColor(dbTable.verticalRowBarColor); //new Color(175, 175, 175));
        grph.fillRect(1,2,preferredWidth-2,height-2);

        // Horizontal lines
        grph.setColor(dbTable.verticalRowBarColor);
        grph.drawLine(1, 0, preferredWidth, 0);
//        grph.setColor(Color.darkGray);
//        grph.drawLine(1, 0, 23, 0);
        grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
        grph.drawLine(1, height-1, preferredWidth, height-1);
        grph.setColor(dbTable.verticalRowBarColor);
        grph.drawLine(1, height, preferredWidth, height);

        // Vertical lines
        grph.setColor(dbTable.verticalRowBarColor);
        grph.drawLine(1, 0, 1, height);
        grph.setColor(dbTable.verticalRowBarDarkColor); //Color.darkGray);
        grph.drawLine(preferredWidth-1, 0, preferredWidth-1, height);
    }

    public boolean isFocusable() {
        return false;
    }
}


class TipManager {
    /** The offset from the left and right edge of the host component, inside which the tip is drawn. The tip
     *  can will be drawn between the area from the left edge of the component plus OFFSET to the right edge of the
     *  component minus OFFSET.
      */
    public static final int OFFSET = 20;
    /** The space between:
     * <ul>
     * <li> the right edge of the tip and the first character
     * <li> the left edge and the least character of the tip.
     * </ul>
     */
    public static final int TEXT_OFFSET = 5;
    DBTable dbTable = null;
    ESlateLabel tip = null;
    boolean tipVisible = false;
    private boolean fontInitialized = false;
    private Dimension tempDim = new Dimension();

    public TipManager(DBTable dbTable) {
        this.dbTable = dbTable;
    }

    private void createTip() {
        tip = new ESlateLabel();
        tip.setOpaque(true);
        tip.setMultilineMode(true);
        tip.setBorder(new OneLineBevelBorder(SoftBevelBorder.RAISED));
        tip.setBackground(new Color(253, 254, 226));
        tip.setHorizontalAlignment(JLabel.CENTER);
        tip.setVerticalAlignment(JLabel.CENTER);
        tip.setRequestFocusEnabled(false);
        tip.setFocusable(false);
    }

    /** Shows a tip on top of the cell at <code>row</code> and <code>column</code>. If <code>column</code> is -1, then
     *  the first column is used. The width of the tip may be specified. It cannot exceed the width of the DBTable.
     *  The tip will be centered on top of the cell.
     * @param text The tip.
     * @param row  The row of the cell.
     * @param column The column of the cell.
     * @param width The width of the tip. The specified width can be <code>-1</code>. In this case the width of the
     * DBTable will be used.
     */
    public void showTip(String text, int row, int column, int width) {
        Rectangle rect = null;
        int cellWidth = -1;
        // Get the rectangle of the row or the cell, if the column was given.
        if (column == -1)
            rect = dbTable.getRowRectangle(row);
        else{
            rect = dbTable.jTable.getCellRect(row, column, true);
            cellWidth = rect.width;
        }

        // Transform the rectangle's x and y to DBTable co-ordinates.
        Point viewPosition = dbTable.scrollpane.getViewport().getViewPosition();
        rect.y = rect.y - viewPosition.y;
        rect.x = rect.x - viewPosition.x;

        if (tip == null) createTip();
        tip.setText(text);
        if (!fontInitialized) {
//            tip.setFont(tip.getFont().deriveFont(Font.BOLD));
            fontInitialized = true;
        }

        // The available width is the whole width of the DBTable minus the left and right offsets.
        int availableWidth = dbTable.getSize(tempDim).width-OFFSET-OFFSET;
        // If a cell address was given and the width of the cell is greater than the 'availablewidth'
        // the the cell is partially visible, so disregard its width. There is no point in centering
        // the tip around the cell in this case. The tip is centered around the whole DBTable.
        if (cellWidth > availableWidth)
            cellWidth = -1;
        // If 'width' was nos specified, then let it be the whole 'availableWidth'
        if (width == -1) {
            width = availableWidth;
        }else{
            // If 'width' was specified and is greater than 'availableWidth', then truncate it.
            if (width > availableWidth)
                width = availableWidth;
        }

        // Get the required width to show the whole text in one line.
        FontMetrics fm = tip.getFontMetrics(tip.getFont());
        int textWidth = (int) fm.getStringBounds(text, dbTable.getGraphics()).getWidth();

        // If the required width to show the whole text in one line is less than 'width', then
        // set width to the required width.
        if (textWidth + TEXT_OFFSET + TEXT_OFFSET < width)
            width = textWidth + TEXT_OFFSET + TEXT_OFFSET;

        // Center the tip either around the whole DBTable (if cellWidth == -1), or around the cell.
        int startX = 0;
        if (cellWidth == -1)
            startX = OFFSET + (availableWidth - width)/2;
        else
            startX = dbTable.rowBar.preferredWidth + rect.x + (cellWidth/2) - (width/2); //(availableWidth - width) - cellWidth/2;

        // Rectify centering results, if the tip exceeds the right edge of the DBTable.
        if (startX + width > dbTable.getSize(tempDim).width - OFFSET)
            startX = startX - (dbTable.getSize(tempDim).width - OFFSET - width);

        // This is the height of one line
        int lineHeight = fm.getHeight();
//System.out.println("tip.getLineCount(): " + tip.getLineCount(width, dbTable.getGraphics()) + ", for width: " + width);
        // Gets the number of lines required to show the tip with the desired 'width' Add a vertical offset of 4.
        int tipHeight = lineHeight * tip.getLineCount(width, dbTable.getGraphics()) + 4; //numOfLines;
        // Show the tip
        popupTip(startX, rect.y-tipHeight, width, tipHeight);
    }

    private void popupTip(int x, int y, int width, int height) {
        FontMetrics fm = tip.getFontMetrics(tip.getFont());
        ExplicitConstraints ec2 = new ExplicitConstraints(tip);
        ec2.setX(ContainerEF.left(dbTable).add(x));
        ec2.setY(ContainerEF.top(dbTable).add(y));
        ec2.setWidth(MathEF.constant(width));
        ec2.setHeight(MathEF.constant(height));
//System.out.println("ec2: " + ec2.getXValue(expLayout) + ", " + ec2.getYValue((ExplicitLayout) getLayout()) + ", " + ec2.getHeightValue((ExplicitLayout) getLayout()) + ", " + ec2.getWidthValue((ExplicitLayout) getLayout()));
        dbTable.remove(dbTable.scrollpane);
        dbTable.add(tip, ec2);
        dbTable.add(dbTable.scrollpane, dbTable.scrollpaneCons);
        tip.setVisible(true);
        tip.revalidate();
        dbTable.expLayout.layoutContainer(dbTable);
        tipVisible = true;
        dbTable.repaint(tip.getBounds());
    }

    void showTip(String text, int x, int y) {
        if (tip == null)
            createTip();
        tip.setText(text);
        if (!fontInitialized) {
            tip.setFont(tip.getFont().deriveFont(Font.BOLD));
            fontInitialized = true;
        }
        FontMetrics fm = tip.getFontMetrics(tip.getFont());
        int width = fm.stringWidth(text) + 15;
//        int numOfLines = 1;
        if (width > dbTable.getSize(tempDim).width-x) {
            int newWidth = tempDim.width-x-x;
//            numOfLines = width/newWidth;
//            if (width%newWidth != 0)
//                numOfLines++;
            width = newWidth;
        }
        int height = fm.getHeight();
        height = height * tip.getLineCount(width, dbTable.getGraphics()) + 4; //numOfLines;
        popupTip(x, y, width, height);

//System.out.println("size(): " + tip.getSize() + ", loc: " + tip.getLocation());
    }

    public void resetTip() {
        if (tipVisible) {
            Rectangle rect = tip.getBounds();
            dbTable.remove(tip);
            dbTable.repaint(rect);
            tipVisible = false;
        }
    }
}

