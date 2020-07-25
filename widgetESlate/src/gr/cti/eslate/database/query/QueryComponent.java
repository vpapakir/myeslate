package gr.cti.eslate.database.query;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.DefaultCellEditor;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.ResourceBundle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.awt.Cursor;
import java.io.Externalizable;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ChangeEvent;
import java.util.ArrayList;
import java.util.Locale;
import com.objectspace.jgl.Array;
import com.objectspace.jgl.LessNumber;
import com.objectspace.jgl.Sorting;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.sharedObject.DatabaseSO;
import gr.cti.eslate.sharedObject.TableSO;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.event.*;
import gr.cti.eslate.utils.ESlateFieldMap;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.typeArray.IntBaseArray;


public class QueryComponent extends JPanel implements
        ESlatePart, Externalizable { //, SharedObjectListener {

    /* The current version of the storage format of the QueryComponent class
    */
    public static final int STR_FORMAT_VERSION = 200;
    static final long serialVersionUID = 12;
    public static final String QBE = "QBE";
    public static final String TEXT_QUERY = "TEXT_QUERY";

    protected static ClipboardOwner defaultClipboardOwner = new ClipboardObserver();
    static class ClipboardObserver implements ClipboardOwner {
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
        }
    }

    /* The DBase on which queries take place.
    */
    DBase db;

    protected ResourceBundle infoBundle;
    String errorStr, warningStr;

    JPanel emptyPanel = null;
    boolean emptyPanelVisible;
    static int noDatabaseLabelHeight;
    static int noDatabaseLabelWidth;
    JPanel emptyDB = null;
    int myLevels;
    protected boolean firstTime=true;
    protected boolean emptyDBVisible;
    protected JLabel emptyDBLabel;
    static int emptyDBLabelWidth;
    JPanel toolContentPane;
    static ImageIcon stringIcon, integerIcon, doubleIcon, booleanIcon, urlIcon, dateIcon, timeIcon, imageIcon;
    static ImageIcon keyStringIcon, keyIntegerIcon, keyDoubleIcon, keyBooleanIcon, keyURLIcon, keyDateIcon, keyTimeIcon, keyImageIcon;

    protected JTabbedPane tabs = null;

    protected QueryViewStructure queryViewStructure = new QueryViewStructure();
    protected QueryDatabaseListener dbaseListener;
    protected QueryTableModelListener tableModelListener;
    protected PropertyChangeListener dbasePcl, tablePcl;
    protected boolean startViewValue;
    protected boolean headerIconsVisible = false;
    protected boolean iterateEvent = true;

    protected JButton clearQuery, execute, addRow, removeRow , pressedTool;
    protected JToggleButton headerStatusButton, queryPane;
    protected JToggleButton newSelection, addToSelection, selectFromSelection, removeFromSelection;
    protected JPanel mainPanel;

    final Toolbar toolPanel;
    int selectionMode = -1, previousSelectionMode = -1;
    boolean selectionButtonPressed = false;
    Array selectionModeButtons;
    boolean proceed = false; //Used in textQueryPanel in JList ListSelectionListeners

    JPanel viewerPanel;
    QueryTextView textView = null; //new QueryTextView(this, null);
    int startingHeight=0;
    /* The main panel of the component.
    */
    JPanel componentPanel;

    /* The shared object.
    */
//    protected DatabaseSO databaseSO;

    /* This is the structure that maps the tables created by queries and exported
    * by table plugs, to the tables they were created from.
    */
    QueryResultStructure queryResults = new QueryResultStructure();
    /* This class contains all the plugs through which the query result tables
    * can be exported to other components.
    */
    QueryResultTablePlugs resultTablePlugs = new QueryResultTablePlugs(this);

    private static final Color tablePinColor = new Color(102, 88, 187);
    protected boolean hiddenTablesDisplayed = false;
    private boolean destroyInProgress = false;

    protected ESlateHandle eSlateHandle;
    private QueryComponent queryComponent;
    /* Specifies the period when the component is loading its state */
    private boolean loadingState = false;
    /* This listener is added to the ESlateHandle of the hosted DBase. This
    * listener enables a DBase handle to be re-parented to this QueryComponent,
    * when it is removed from the its current parent-handle.
    */
    FemaleSingleIFSingleConnectionProtocolPlug databasePin;
    /** Timers which measure functions of the Table */
    PerformanceTimer loadTimer, saveTimer, constructorTimer, initESlateAspectTimer;
    /* The listener that notifies about changes to the state of the PerformanceManager */
    PerformanceListener perfListener = null;


    public ESlateHandle getESlateHandle() {
        if (eSlateHandle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.init(initESlateAspectTimer);

            eSlateHandle = ESlate.registerPart(this);
            eSlateHandle.addESlateListener(new ESlateAdapter() {
                public void handleDisposed(HandleDisposalEvent e) {
                    // TO BE REMOVED
                    if (db != null) {
                        //System.out.println("QueryComponent handleDisposed()");
                        if (dbaseListener != null) {
                            db.removeDatabaseListener(dbaseListener);
                            dbaseListener = null;
                        }
                        if (tableModelListener != null) {
                            for (int i=0; i<db.getTableCount(); i++)
                                db.getTableAt(i).removeTableModelListener(tableModelListener);
                            tableModelListener = null;
                        }
                        if (dbasePcl != null) {
                            db.removePropertyChangeListener(dbasePcl);
                            dbasePcl = null;
                            for (int i=0; i<db.getTableCount(); i++)
                                db.getTableAt(i).removePropertyChangeListener(tablePcl);
                            tablePcl = null;
                        }
                        queryViewStructure.clear();
                        proceed = false;
                        iterateEvent = true;
                        headerIconsVisible = false;
                        tabs = null;
                        if (componentPanel != null && mainPanel != null) {
                            componentPanel.remove(mainPanel);
                            mainPanel = null;
                        }
                        if (viewerPanel != null) {
                            viewerPanel.removeAll();
                            viewerPanel=null;
                        }
                        if (textView != null) {
                            textView.destroy();
                            textView=null;
                        }
                    }

                    if (!emptyPanelVisible) {
                        showEmptyPanel();
                    }
                    execute.setEnabled(false);
                    clearQuery.setEnabled(false);
                    headerStatusButton.setVisible(false);
                    removeRow.setVisible(false);
                    addRow.setVisible(false);
                    queryPane.setSelected(false);
                    queryPane.setBorderPainted(false);
                    queryPane.setEnabled(false);

                    removeResultTableHandlesFromQueryHandle();
                    destroyTableHandles();
                    db = null;

                    PerformanceManager pm = PerformanceManager.getPerformanceManager();
                    PerformanceTimerGroup ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.CONSTRUCTOR);
                    pm.removePerformanceTimerGroup(ptg, constructorTimer);
                    ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.LOAD_STATE);
                    pm.removePerformanceTimerGroup(ptg, loadTimer);
                    ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.SAVE_STATE);
                    pm.removePerformanceTimerGroup(ptg, saveTimer);
                    ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.INIT_ESLATE_ASPECT);
                    pm.removePerformanceTimerGroup(ptg, initESlateAspectTimer);
                    pm.removePerformanceListener(perfListener);
                    perfListener = null;
                }
            });

            String info[] = {infoBundle.getString("part"),
                             infoBundle.getString("design"),
                             infoBundle.getString("development"),
                             //                         infoBundle.getString("funding"),
                             infoBundle.getString("copyright"),
            };
            ESlateInfo eSlateInfo=new ESlateInfo(infoBundle.getString("compo"), info);
            eSlateHandle.setInfo(eSlateInfo);

            //Database pin
            try{
                databasePin = new FemaleSingleIFSingleConnectionProtocolPlug(
                        this.eSlateHandle,
                        infoBundle,
                        "QueryComponentMsg12",
                        Color.magenta,
                        DBase.class);
                databasePin.setHostingPlug(true);
                eSlateHandle.addPlug(databasePin);
                databasePin.addDisconnectionListener(new DisconnectionListener() {
                    public void handleDisconnectionEvent(DisconnectionEvent e) {
                        if (db != null) {
                            if (dbaseListener != null) {
                                db.removeDatabaseListener(dbaseListener);
                                dbaseListener = null;
                            }
                            if (tableModelListener != null) {
                                for (int i=0; i<db.getTableCount(); i++)
                                    db.getTableAt(i).removeTableModelListener(tableModelListener);
                                tableModelListener = null;
                            }
                            if (dbasePcl != null) {
                                db.removePropertyChangeListener(dbasePcl);
                                dbasePcl = null;
                                for (int i=0; i<db.getTableCount(); i++)
                                    db.getTableAt(i).removePropertyChangeListener(tablePcl);
                                tablePcl = null;
                            }
                            queryViewStructure.clear();
                            proceed = false;
                            iterateEvent = true;
                            headerIconsVisible = false;
                            tabs = null;
                            if (componentPanel != null && mainPanel != null) {
                                componentPanel.remove(mainPanel);
                                mainPanel = null;
                            }
                            if (viewerPanel != null) {
                                viewerPanel.removeAll();
                                viewerPanel=null;
                            }
                            if (textView != null) {
                                textView.destroy();
                                textView=null;
                            }
                        }

                        if (!emptyPanelVisible) {
                            showEmptyPanel();
                        }
                        execute.setEnabled(false);
                        clearQuery.setEnabled(false);
                        headerStatusButton.setVisible(false);
                        removeRow.setVisible(false);
                        addRow.setVisible(false);
                        queryPane.setSelected(false);
                        queryPane.setBorderPainted(false);
                        queryPane.setEnabled(false);

                        removeResultTableHandlesFromQueryHandle();
                        destroyTableHandles();
                        db = null;
                    }
                });
                databasePin.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        DBase dbase = (DBase) e.getPlug().getHandle().getComponent();
                        loadDBase(dbase);
                        //                       System.out.println("Connected");
                    }
                });
            } catch (InvalidPlugParametersException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            } catch (PlugExistsException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            try{
                eSlateHandle.setUniqueComponentName(infoBundle.getString("compo1"));
            }catch (gr.cti.eslate.base.RenamingForbiddenException exc) {
                System.out.println("Serious error in QueryComponent: can initialize component's name");
            }

            pm.stop(initESlateAspectTimer);
            pm.displayElapsedTime(initESlateAspectTimer, eSlateHandle, "", "ms");
        }
        return eSlateHandle;
    }

    public void readExternal(java.io.ObjectInput in) throws java.io.IOException, ClassNotFoundException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(loadTimer);
        try{
            loadingState = true;
            ESlateFieldMap fieldMap = (ESlateFieldMap) in.readObject();
            int dataVersion = 100;
            try{
                dataVersion = new Integer(fieldMap.getDataVersion()).intValue();
            }catch (Exception exc) {}

            BorderDescriptor bd = (BorderDescriptor) fieldMap.get("BorderDescriptor");
            if (bd != null) {
                try{
                    setBorder(bd.getBorder());
                }catch (Throwable thr) {}
            }
            setAddRowButtonVisible(fieldMap.get("AddRowButtonVisible", true));
            setAddToSelectionButtonVisible(fieldMap.get("AddToSelectionButtonVisible", true));
            setClearQueryButtonVisible(fieldMap.get("ClearQueryButtonVisible", true));
//            boolean b = fieldMap.get("ExecuteButtonVisible", true);
            setExecuteButtonVisible(fieldMap.get("ExecuteButtonVisible", true));
            setHeaderStatusButtonVisible(fieldMap.get("HeaderStatusButtonVisible", true));
            setNewSelectionButtonVisible(fieldMap.get("NewSelectionButtonVisible", true));
            setQueryPaneButtonVisible(fieldMap.get("QueryPaneButtonVisible", true));
            setRemoveFromSelectionButtonVisible(fieldMap.get("RemoveFromSelectionButtonVisible", true));
            setRemoveRowButtonVisible(fieldMap.get("RemoveRowButtonVisible", true));
            setSelectFromSelectionButtonVisible(fieldMap.get("SelectFromSelectionButtonVisible", true));

            DBase dbase = null;
            if (dataVersion < 200) {
                dbase = (DBase) fieldMap.get("CDatabase");
                if (dbase != null) {
//                    dbase.getESlateHandle().setESlateMicroworld(eSlateHandle.getESlateMicroworld());
                    eSlateHandle.add(dbase.getESlateHandle());
                }
            }else
                eSlateHandle.restoreChildren(in, fieldMap, "CDatabase");

            loadingState = false;
        }catch (java.io.EOFException exc) {
            System.out.println("EOFException occured in QueryComponent's readExternal(). The loaded microworld must have been saved when the QueryComponent had an empty writeExternal() - no state. This exception is consumed.");
            loadingState = false;
        }
        pm.stop(loadTimer);
        pm.displayElapsedTime(loadTimer, getESlateHandle(), "", "ms");
    }

    public void writeExternal(java.io.ObjectOutput out) throws java.io.IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(saveTimer);
        ESlateFieldMap fieldMap = new ESlateFieldMap(new Integer(STR_FORMAT_VERSION).toString(), 10);
//        if (getBorder() != null) {
        try {
            BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);
            fieldMap.put("BorderDescriptor", bd);
        }catch (Throwable thr) {
            thr.printStackTrace();
        }
//        }
        fieldMap.put("AddRowButtonVisible", isAddRowButtonVisible());
        fieldMap.put("AddToSelectionButtonVisible", isAddToSelectionButtonVisible());
        fieldMap.put("ClearQueryButtonVisible", isClearQueryButtonVisible());
//        System.out.println("Writing out ExecuteButtonVisible: " + isExecuteButtonVisible());
        fieldMap.put("ExecuteButtonVisible", isExecuteButtonVisible());
        fieldMap.put("HeaderStatusButtonVisible", isHeaderStatusButtonVisible());
        fieldMap.put("NewSelectionButtonVisible", isNewSelectionButtonVisible());
        fieldMap.put("QueryPaneButtonVisible", isQueryPaneButtonVisible());
        fieldMap.put("RemoveFromSelectionButtonVisible", isRemoveFromSelectionButtonVisible());
        fieldMap.put("RemoveRowButtonVisible", isRemoveRowButtonVisible());
        fieldMap.put("SelectFromSelectionButtonVisible", isSelectFromSelectionButtonVisible());

//        if (db != null && db.getESlateHandle().getParentHandle() == eSlateHandle)
//            fieldMap.put("CDatabase", db);
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
            eSlateHandle.saveChildren(out, fieldMap, "CDatabase", new ESlateHandle[] {dbaseHandle});

        out.writeObject(fieldMap);
        pm.stop(saveTimer);
        pm.displayElapsedTime(saveTimer, getESlateHandle(), "", "ms");
    }

    public QueryComponent() {
        super(true);
        infoBundle = ResourceBundle.getBundle("gr.cti.eslate.database.query.InfoBundle", Locale.getDefault());

        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(constructorTimer);

        setBorder(new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED));

        startViewValue=false;
        setLayout(new BorderLayout());
        errorStr = infoBundle.getString("Error");
        warningStr = infoBundle.getString("Warning");
        queryComponent = this;

        initialize();

        toolPanel = new Toolbar();
        BoxLayout bl1 = new BoxLayout(toolPanel, BoxLayout.X_AXIS);
        toolPanel.setLayout(bl1);
        addComponentsToToolPanel();


        toolContentPane = new JPanel(true);
        toolContentPane.setLayout(new BorderLayout());
        toolContentPane.add(toolPanel, BorderLayout.CENTER);
        toolContentPane.setBorder(new CompoundBorder(new EtchedBorder(),
                new EmptyBorder(1, 0, 1, 0)));
        componentPanel.add(toolContentPane, BorderLayout.NORTH);

        add(componentPanel, BorderLayout.CENTER);

        pm.stop(constructorTimer);
        pm.displayElapsedTime(constructorTimer, getESlateHandle(), "", "ms");
    }

    public Dimension getPreferredSize() {
        return new Dimension(372,300);
    }

    public DBase getDB() {
        return db;
    }

/*    public void handleSharedObjectEvent(SharedObjectEvent soe) {
SharedObject so = soe.getSharedObject();
try {
if (so instanceof DatabaseSO) {
loadDBase(((DatabaseSO) so).getDatabase());
}
} catch (Exception e) {
e.printStackTrace();
}
}
*/
    /**    protected void addDBaseHandleToQueryHandle(DBase dbase) {
     if (dbase == null || eSlateHandle == null) return;
     ESlateHandle dbHandle = dbase.getESlateHandle();
     ///        dbHandle.addESlateListener(dbReparentListener);
     **/        /* The DBase's handle is added to the Database's handle only if it does
     * not already have a father. Since the components which are parented by
     * the ESlate desktop have not parent, we make explicit check about whether
     * they are contained on the desktop.
     */
    /**        System.out.println("QC addDBaseHandleToQueryHandle dbHandle.getParentHandle(): " + dbHandle.getParentHandle() + ", !isDesktopComponent(dbHandle): " + !isDesktopComponent(dbHandle));
     if (dbHandle.getParentHandle() == null && !isDesktopComponent(dbHandle)) {
     System.out.println("QC addDBaseHandleToQueryHandle() dbHandle: " + dbHandle + ", dbHandle.getParentHandle(): " + dbHandle.getParentHandle());
     eSlateHandle.add(dbHandle);
     System.out.println("QC 2. addDBaseHandleToQueryHandle() dbHandle: " + dbHandle + ", dbHandle.getParentHandle(): " + dbHandle.getParentHandle());
     }
     }
     **/
    //To be removed with the dependency to ESlateContainer
/*    public boolean isDesktopComponent(ESlateHandle handle) {
if (eSlateHandle.getESlateMicroworld() == null || eSlateHandle.getESlateMicroworld().getMicroworldEnvironment() == null)
return false;
return ((gr.cti.eslate.base.container.ESlateContainer) eSlateHandle.getESlateMicroworld().getMicroworldEnvironment()).componentOnDesktop(handle);
}
*/
    protected void removeResultTableHandlesFromQueryHandle() {
        if (db == null || eSlateHandle == null) return;
//System.out.println("QC removeResultTableHandlesFromQueryHandle() eSlateHandle: " + eSlateHandle);
//        ESlateHandle dbHandle = db.getESlateHandle();
        ESlateHandle[] childHandles = eSlateHandle.toArray();
        for (int i=0; i<childHandles.length; i++) {
//System.out.println("QC 1. removeResultTableHandlesFromQueryHandle() childHandles[i]: " + childHandles[i] + ", childHandles[i].getParentHandle(): " + childHandles[i].getParentHandle());
///            childHandles[i].removeESlateListener(dbReparentListener);
            if (childHandles[i].getParentHandle() == eSlateHandle &&
                    Table.class.isAssignableFrom(childHandles[i].getComponent().getClass()))
                eSlateHandle.remove(childHandles[i]);
//System.out.println("QC 3. removeResultTableHandlesFromQueryHandle() childHandles[i]: " + childHandles[i] + ", childHandles[i].getParentHandle(): " + childHandles[i].getParentHandle() + ", !isDesktopComponent(childHandles[i]): " + !isDesktopComponent(childHandles[i]));
/*            if (childHandles[i].getParentHandle() == null && !isDesktopComponent(childHandles[i])) {
//                System.out.println("Calling toBeDisposed on: " + childHandles[i].getComponentName());
childHandles[i].toBeDisposed(false, new BooleanWrapper(true));
//                System.out.println("Calling dispose on: " + childHandles[i].getComponentName());
childHandles[i].dispose();
}
*/
        }
    }

    public void loadDBase(DBase dbase) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (db != null) {
            removeResultTableHandlesFromQueryHandle();
            destroyTableHandles();
            if (dbaseListener != null) {
                db.removeDatabaseListener(dbaseListener);
                dbaseListener = null;
            }

            if (tableModelListener != null) {
                for (int i=0; i<db.getTableCount(); i++)
                    db.getTableAt(i).removeTableModelListener(tableModelListener);
                tableModelListener = null;
            }

            if (dbasePcl != null) {
                db.removePropertyChangeListener(dbasePcl);
                dbasePcl = null;
                for (int i=0; i<db.getTableCount(); i++)
                    db.getTableAt(i).removePropertyChangeListener(tablePcl);
                tablePcl = null;
            }
        }

        DBase oldDb = db;
        db = dbase;

        if (db == null) {
            if (!emptyPanelVisible) {
                queryViewStructure.clear();
                proceed = false;
                iterateEvent = true;
                headerIconsVisible = false;
                tabs = null;

                if (componentPanel != null && mainPanel != null) {
                    componentPanel.remove(mainPanel);
                    mainPanel = null;
                }
                if (viewerPanel != null) {
                    viewerPanel.removeAll();
                    viewerPanel=null;
                }
                if (textView != null) {
                    textView.destroy();
                    textView=null;
                }

                showEmptyPanel();
                execute.setEnabled(false);
                clearQuery.setEnabled(false);
                headerStatusButton.setEnabled(false);
                headerStatusButton.setSelected(false);
                addRow.setEnabled(false);
                queryPane.setSelected(false);
                queryPane.setBorderPainted(false);
                queryPane.setEnabled(false);
                queryPane.revalidate();
            }

            /* Destroy the protocol pins of the tables -query results-
            * if they exist.
            */
//                    DBase tmp = db;  //1.5
//                    db = oldDb;  //1.5
//                    destroyTableHandles();
//                    db = tmp;    //1.5
        } else {
///            addDBaseHandleToQueryHandle(db);
            if (db.getTableCount() == 0) {
                showEmptyDBPanel();
                componentPanel.setSize(getSize());
                emptyDB.validate();
                emptyDB.doLayout();
                emptyDB.repaint(); //paintImmediately(emptyDB.getVisibleRect());
                validate();
                doLayout();
                repaint();
                createDatabaseListener();
                createTableModelListener();
                db.addDatabaseListener(dbaseListener);
                for (int i=0; i<db.getTableCount(); i++)
                    db.getTableAt(i).addTableModelListener(tableModelListener);
                queryComponent.createDatabasePropertyChangeListener();
                queryComponent.createTablePropertyChangeListener();
                db.addPropertyChangeListener(dbasePcl);
                for (int i=0; i<db.getTableCount(); i++)
                    db.getTableAt(i).addPropertyChangeListener(tablePcl);

                hiddenTablesDisplayed = db.isHiddenTablesDisplayed();
                execute.setEnabled(false);
                clearQuery.setEnabled(false);
                headerStatusButton.setEnabled(false);
                headerStatusButton.setSelected(false);
                queryPane.setSelected(false);
                addRow.setEnabled(false);
                queryPane.setEnabled(false);

                /* Destroy the protocol pins of the tables -query results-
                * if they exist.
                */
//                        DBase tmp = db;  //1.5
//                        db = oldDb;  //1.5
//                        destroyTableHandles();
//                        db = tmp;    //1.5
            } else {
                if (emptyPanelVisible) {
                    componentPanel.remove(emptyPanel);
                    emptyPanelVisible = false;
                }else if (emptyDBVisible) {
                    componentPanel.remove(emptyDB);
                    emptyDBVisible = false;
                    componentPanel.validate();
                    componentPanel.repaint();
                }
                hiddenTablesDisplayed = db.isHiddenTablesDisplayed();
                createQueryPanels();
                componentPanel.validate();
                componentPanel.doLayout();
                componentPanel.repaint();
                queryComponent.createDatabaseListener();
                createTableModelListener();
                db.addDatabaseListener(dbaseListener);
                for (int i=0; i<db.getTableCount(); i++)
                    db.getTableAt(i).addTableModelListener(tableModelListener);
                createDatabasePropertyChangeListener();
                createTablePropertyChangeListener();
                db.addPropertyChangeListener(dbasePcl);
                for (int i=0; i<db.getTableCount(); i++)
                    db.getTableAt(i).addPropertyChangeListener(tablePcl);

                /* Create the pins for the query results for each table.
                */
//                   createTablePins(db);
            }
        }
        setCursor(Cursor.getDefaultCursor());
    }
/* ======================================================================
* 				Properties
* ======================================================================
*/

/*    public void setStartViewValue(boolean whichView) {
if (firstTime) {
startViewValue=whichView;
queryMode.clear();
queryMode.add(new Boolean(whichView));
queryPane.setSelected(!whichView);
addRow.setVisible(whichView);
removeRow.setVisible(whichView);
headerStatusButton.setVisible(whichView);
} else {
return;
}
}

public boolean isStartViewValue() {
return startViewValue;
}

*/
    private void showTheWarning() {
        ESlateOptionPane.showMessageDialog(QueryComponent.this,
                infoBundle.getString("propertyWarning"),
                warningStr,
                JOptionPane.WARNING_MESSAGE);
    }

    private void fixProperty(javax.swing.AbstractButton tempButton, boolean nextState) {
        if(!nextState) {
            if (tempButton.getParent() != null) {
                toolPanel.remove(tempButton);
                toolPanel.revalidate();
            } else {
                return;
            }
        } else {
            if (tempButton.getParent() != null) {
                return;
            } else {
                toolPanel.add(tempButton);
                toolPanel.revalidate();
            }
        }
    }

    public void setExecuteButtonVisible(boolean myBoolean) {
        if (!myBoolean && !loadingState) {
            ESlateOptionPane.showMessageDialog(QueryComponent.this,
                    infoBundle.getString("propertyWarning2"),
                    warningStr,
                    JOptionPane.WARNING_MESSAGE);
        }
        fixProperty(execute, myBoolean);
    }

    public boolean isExecuteButtonVisible() {
        return (execute.getParent() != null && execute.isVisible()); //isShowing();
    }
    public void setClearQueryButtonVisible(boolean myBoolean) {
        fixProperty(clearQuery, myBoolean);

    }

    public boolean isClearQueryButtonVisible() {
        return (clearQuery.getParent() != null && clearQuery.isVisible());
    }


    public void setAddRowButtonVisible(boolean myBoolean) {
        /* It depends on the selected view*/
        if (queryViewStructure.isQBEModeActive()) {
            fixProperty(addRow, myBoolean);
        } else {
            if (!loadingState)
                showTheWarning();
        }
    }

    public boolean isAddRowButtonVisible() {
        return (addRow.getParent() != null && addRow.isVisible()); //isShowing();
    }

    public void setRemoveRowButtonVisible(boolean myBoolean) {
        /* It depends on the selected view*/
        if (queryViewStructure.isQBEModeActive()) {
            fixProperty(removeRow, myBoolean);
        } else {
            if (!loadingState)
                showTheWarning();
        }
    }

    public boolean isRemoveRowButtonVisible() {
        return (removeRow.getParent() != null && removeRow.isVisible());
    }

    public void setHeaderStatusButtonVisible(boolean myBoolean) {
        /* It depends on the selected view*/
        if (queryViewStructure.isQBEModeActive()) {
            fixProperty(headerStatusButton, myBoolean);
        } else {
            if (!loadingState)
                showTheWarning();
        }
    }

    public boolean isHeaderStatusButtonVisible() {
        return (headerStatusButton.getParent() != null && headerStatusButton.isVisible());
    }

    public void setQueryPaneButtonVisible(boolean myBoolean) {
        fixProperty(queryPane, myBoolean);
    }

    public boolean isQueryPaneButtonVisible() {
        return (queryPane.getParent() != null && queryPane.isVisible());
    }

    public void setNewSelectionButtonVisible(boolean myBoolean) {
        fixProperty(newSelection, myBoolean);
    }

    public boolean isNewSelectionButtonVisible() {
        return (newSelection.getParent() != null && newSelection.isVisible());
    }

    public void setAddToSelectionButtonVisible(boolean myBoolean) {
        fixProperty(addToSelection , myBoolean);
    }

    public boolean isAddToSelectionButtonVisible() {
        return (addToSelection.getParent() != null && addToSelection.isVisible());
    }

    public void setRemoveFromSelectionButtonVisible(boolean myBoolean) {
        fixProperty(removeFromSelection , myBoolean);
    }

    public boolean isRemoveFromSelectionButtonVisible() {
        return (removeFromSelection.getParent() != null && removeFromSelection.isVisible());
    }

    public void setSelectFromSelectionButtonVisible(boolean myBoolean) {
        fixProperty(selectFromSelection, myBoolean);
    }

    public boolean isSelectFromSelectionButtonVisible() {
        return (selectFromSelection.getParent() != null && selectFromSelection.isVisible());
    }


    protected void createEmptyDBPanel() {
        emptyDB = new JPanel(true) {
            public void updateUI() {
                super.updateUI();
                setBackground(UIManager.getColor("control"));
            }
        };
        emptyDB.setLayout(new BorderLayout());
//        emptyDB.setBackground(Color.lightGray);
//        emptyDB.setForeground(Color.darkGray);
        emptyDBLabel = new JLabel(infoBundle.getString("QueryComponentMsg13"));
        Font f = new Font("TimesRoman", Font.BOLD + Font.ITALIC, 20);
        emptyDBLabel.setFont(f);
        emptyDBLabel.setEnabled(false);
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(f);
        emptyDBLabelWidth = fm.stringWidth(infoBundle.getString("QueryComponentMsg13"));
        emptyDBLabel.setBorder(new EmptyBorder(0, (this.getSize().width - emptyDBLabelWidth)/2, 0, (this.getSize().width - emptyDBLabelWidth)/2-10));
        emptyDB.add(emptyDBLabel, BorderLayout.CENTER);
        emptyDB.setBorder(new EtchedBorder());
        emptyDB.revalidate();
        emptyDB.doLayout();
        emptyDB.paintImmediately(emptyDB.getVisibleRect());
    }


    protected void createEmptyPanel() {
        emptyPanel = new JPanel(true){
            public void updateUI() {
                super.updateUI();
                setBackground(UIManager.getColor("controlShadow"));
            }
        };
        emptyPanel.setLayout(new BorderLayout());
        emptyPanel.setBackground(UIManager.getColor("controlShadow"));
    }


    protected void checkToolStatus() {
        int activeTableIndex = queryViewStructure.activeTableIndex;
        boolean isEmpty = queryViewStructure.getQTablePanel(activeTableIndex).getQTableModel().isEmpty();
        if (isEmpty && execute.isEnabled()) {
            execute.setEnabled(false);
            clearQuery.setEnabled(false);
        }

        if (!isEmpty && !execute.isEnabled()) {
            execute.setEnabled(true);
            clearQuery.setEnabled(true);
        }
    }


    private void checkRemoveRowStatus() {
        int activeTableIndex = queryViewStructure.activeTableIndex;
        JTable qTable = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
        removeRow.setEnabled(qTable.isEditing());
    }


    public void enableRemoveRowButton() {
        removeRow.setEnabled(true);
    }


    public boolean activateTableAt(int index) {
        if (index < 0 || index > queryViewStructure.size()-1) {
            return false;
        }
        if (queryViewStructure.activeTableIndex == index)
            return true;
        Table table = queryViewStructure.getTable(index);
        int dbTableIndex = db.indexOf(table);
        if (dbTableIndex == -1) {
            return false;
        }

        //System.out.println("activeTableIndex: " + queryViewStructure.activeTableIndex + ", index: " + index);
        if (textView != null && queryViewStructure.activeTableIndex != -1) {
            // Here we save the information needed because the activeQTableIndex
            // still remembers the showing Panels Information
            queryViewStructure.getTextViewInfo(queryViewStructure.activeTableIndex).saveInfo(textView.getFieldListSelectionIndex(),
                    textView.getQueryAreaText());
        }
        textView = new QueryTextView(this, queryViewStructure.getTextViewInfo(index).myListModel);

        queryViewStructure.activeTableIndex = index;
        db.activateTable(dbTableIndex, iterateEvent);
        tabs.setSelectedIndex(index);

        if (queryViewStructure.isQBEModeActive(index) == true) {
            if (queryViewStructure.getQTablePanel(index).getQTableModel().isEmpty()) {
                execute.setEnabled(false);
                clearQuery.setEnabled(false);
            } else {
                execute.setEnabled(true);
                clearQuery.setEnabled(true);
            }

            if (queryViewStructure.getQTablePanel(index).getQTable().getColumnCount() > 0) {
                addRow.setEnabled(true);
                headerStatusButton.setEnabled(true);
            } else {
                addRow.setEnabled(false);
                headerStatusButton.setEnabled(false);
            }
            checkRemoveRowStatus();
            queryPane.setSelected(false);
            viewerPanel.removeAll();
            viewerPanel.add(queryViewStructure.getQTablePanel(index));
            viewerPanel.repaint();
        } else {
            //  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //  !!  Here we should add/remove the views                !!
            //  !!  according the boolean values for the active Table. !!
            //  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            viewerPanel.removeAll();
            viewerPanel.add(textView, BorderLayout.CENTER);
            textView.loadNewInfo(queryViewStructure.getTextViewInfo(index));
            String s = textView.getQueryAreaText();

            if (s == null || s.length() == 0) {
                execute.setEnabled(false);
                clearQuery.setEnabled(false);
            } else {
                execute.setEnabled(true);
                clearQuery.setEnabled(true);
            }
            addRow.setEnabled(false);
            removeRow.setEnabled(false);
            headerStatusButton.setEnabled(false);
            queryPane.setSelected(true);
            queryPane.setBorderPainted(true);
        }
        //mainPanel.invalidate();
        viewerPanel.revalidate();
        viewerPanel.doLayout();

        tabs.validate();
        return true;
    }

    public void deactivateTableAt(int index) {
        int numOfTables = queryViewStructure.size();
        if (index < 0 || index >= numOfTables)
            return;
        if (index==0) {
            if (numOfTables == 1)
                queryViewStructure.activeTableIndex = -1;
            else
                activateTableAt(0);
        } else {
            activateTableAt(index-1);
        }
    }

    public void closeActiveQTable() {
        int index;
        int tabCount = tabs.getTabCount();
        if (tabCount == 1) {
            this.remove(tabs);
            tabs = null;
            if (emptyDB == null)
                createEmptyDBPanel();
            add(emptyDB, BorderLayout.CENTER);
            queryComponent.validate();
            refreshQueryComponent();
            return;
        }

        index = tabs.getSelectedIndex();
        if (index == (tabCount-1)) {
            activateTableAt(index-1);
            tabs.removeTabAt(index);
            refreshQueryComponent();
        }else{
            tabs.removeTabAt(index);
            activateTableAt(index);
            tabs.validate();
            refreshQueryComponent();
        }
    }


    public void refreshQueryComponent() {
        componentPanel.validate();
        componentPanel.repaint();
    }


    protected final JTable createQTable(Table table) {
        QTableModel qTableModel = new QTableModel(table, this);

        /* Create the table.
        */
        JTable qTable = new JTable();
        qTable.setAutoCreateColumnsFromModel(false);
        qTable.setModel(qTableModel);
        qTable.createDefaultColumnsFromModel();
        qTable.setRowHeight(25);

        qTable.setColumnSelectionAllowed(false);
        qTable.setRowSelectionAllowed(false);
        qTable.setCellSelectionEnabled(false);
        qTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DefaultTableColumnModel tcm = (DefaultTableColumnModel) qTable.getColumnModel();
        TableColumn col;
        TableFieldBaseArray tableFields = table.getFields();
        AbstractTableField f;
        JTextField tf = new JTextField();
        createTextFieldKeyListener(tf);

        for (int k=0; k<tcm.getColumnCount(); k++) {
            col = tcm.getColumn(k);
            f = tableFields.get(k);
            col.setHeaderRenderer(new QueryHeaderRenderer(f.getName(), f.getDataType(), !table.isPartOfTableKey(f)/*1f.isKey()*/, f.isCalculated(), this));
            DefaultTableCellRenderer dcr = new DefaultTableCellRenderer();
            dcr.setHorizontalAlignment(SwingConstants.LEFT);
            col.setCellRenderer(dcr);
            DefaultCellEditor dce = new DefaultCellEditor(tf);
            dce.setClickCountToStart(1);
            col.setCellEditor(dce);
            col.setIdentifier(((QTableModel) qTable.getModel()).getColumnName(k));
            col.setHeaderValue(((QTableModel) qTable.getModel()).getColumnName(k));
            if (f.getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class) || (f.isHidden() && !table.isHiddenFieldsDisplayed())) {

                col.setMinWidth(0);
                col.setMaxWidth(0);
                col.setWidth(0);
                col.setResizable(false);
            } else {
                col.setMinWidth(40);
                col.setMaxWidth(1000);
            }

            int colWidth;
            if ((colWidth = (queryComponent.getSize().width-20) / (qTable.getColumnCount() - table.getImageFieldCount())) > 70) {
                for (int i=0; i<qTable.getColumnCount(); i++)
                    qTable.getColumnModel().getColumn(i).setWidth(colWidth);
            } else {
                for (int i=0; i<qTable.getColumnCount(); i++)
                    qTable.getColumnModel().getColumn(i).setWidth(110);
            }
        }

        qTable.setSelectionBackground(Color.white);
        qTable.setSelectionForeground(Color.black);
        return qTable;
    }


    protected void displayHeaderIcons(boolean visible) {
        if (visible) {
            AbstractTableField f;
            Class fieldType;
            boolean isDate;
            QueryHeaderRenderer header = null;
            TableColumn col;
            TableCellRenderer cr;
            JTable qTable;
            TableFieldBaseArray tableFields;

            for (int k=0; k<queryViewStructure.size(); k++) {
                int tableIndex = db.toTableIndex(k);
                if (tableIndex<0) {
                    System.out.println("Serious inconsistency error in QueryComponent displayHeaderIcons(): (1)");
                    continue;
                }
                qTable = queryViewStructure.getQTablePanel(k).getQTable();
                Table table = (Table) db.getTableAt(tableIndex);
                tableFields = table.getFields();
                for (int i=0; i<qTable.getColumnCount(); i++) {
                    col = (TableColumn) qTable.getColumnModel().getColumn(i);
                    header = (QueryHeaderRenderer) col.getHeaderRenderer();

                    // Array tableFields is not re-arranged everytime the order of the table's fields
                    // changes, like Array "tableColumns" does. The correspondence between the two Arrays
                    // is based on each column's (TableColumn, actually) "modelIndex" API value.

                    f = tableFields.get(col.getModelIndex());

                    header.setIcon(f.getDataType(), !table.isPartOfTableKey(f)/*1f.isKey()*/, this);
                    header.setVerticalAlignment(SwingConstants.TOP);
                    header.setHorizontalAlignment(SwingConstants.CENTER);
                    header.setVerticalTextPosition(SwingConstants.BOTTOM);
                    header.setHorizontalTextPosition(SwingConstants.CENTER);

                    header.validate();
                }
                JTableHeader th = qTable.getTableHeader();
                th.validate();
                th.resizeAndRepaint();
            }
        } else {
            QueryHeaderRenderer hr;
            JTable qTable;
            for (int k=0; k<queryViewStructure.size(); k++) {
                qTable = queryViewStructure.getQTablePanel(k).getQTable();
                for (int i=0; i<qTable.getColumnCount(); i++) {
                    hr = (QueryHeaderRenderer) qTable.getColumnModel().getColumn(i).getHeaderRenderer();
                    hr.setIcon(null);
                    hr.validate();
                }
                JTableHeader th = qTable.getTableHeader();
                th.validate();
                th.resizeAndRepaint();
            }
        }
    }


    protected String createQuery(int qTableIndex) {
        /* If the "textQueryPanel" is visible.
        */
        if (queryViewStructure.isQBEModeActive(qTableIndex) == false) {
            //String queryString = ((JTextArea) queryAreas.at(qTableIndex)).getText();
            String queryString = textView.getQueryAreaText();
//            System.out.println("QueryAreaString Before:\t"+queryString);
            /* Remove any new-line characters, which may exist in "queryString".
            */
            int index = queryString.indexOf('\n');
            while (index != -1) {
                queryString = queryString.substring(0, index) + queryString.substring(index+1);
                index = queryString.indexOf('\n');
            }
//            System.out.println("QueryAreaString After:\t"+queryString);
            return queryString;
        }

        QTableModel qModel = queryViewStructure.getQTablePanel(qTableIndex).getQTableModel(); //(QTableModel) QTableModels.at(qTableIndex);
        StringBuffer query = new StringBuffer("");

        String cellData;

        for (int k=0; k<qModel.getRowCount(); k++) {
            if (qModel.isRowEmpty(k))
                continue;

            query.append('(');

            for (int i=0; i<qModel.getColumnCount(); i++) {
//                System.out.println("---------" + i + " -------------");
                cellData = (String) qModel.getValueAt(k, i);
                if (cellData != null && cellData.length() != 0) {
                    String columnName = qModel.getColumnName(i) + ' ';
                    int columnNameLength = columnName.length()+1;

                    query.append('(');
                    StringBuffer cellDataBuff = new StringBuffer(cellData);
                    int l=0;
                    char c;

                    while (l<cellDataBuff.length()) {
                        switch (cellDataBuff.charAt(l)) {
                            case '<':
                                cellDataBuff.insert(l, columnName);
                                l = l+columnNameLength;
                                try{
                                    c = cellDataBuff.charAt(l);
                                    if (cellDataBuff.charAt(l) == '=')
                                        l++;
                                    else if (c == '>' || c == '<' || c == '!') {
                                        ESlateOptionPane.showMessageDialog(QueryComponent.this,
                                                infoBundle.getString("QueryComponentMsg14") + cellDataBuff.charAt(l-1) + c + "\"",
                                                errorStr,
                                                JOptionPane.ERROR_MESSAGE);
                                        return null;
                                    }
                                }catch (StringIndexOutOfBoundsException e) {}
                                break;
                            case '>':
                                cellDataBuff.insert(l, columnName);
                                l = l+columnNameLength;
                                try{
                                    c = cellDataBuff.charAt(l);
                                    if (cellDataBuff.charAt(l) == '=')
                                        l++;
                                    else if (c == '>' || c == '<' || c == '!') {
                                        ESlateOptionPane.showMessageDialog(QueryComponent.this,
                                                infoBundle.getString("QueryComponentMsg14") + cellDataBuff.charAt(l-1) + c + "\"",
                                                errorStr,
                                                JOptionPane.ERROR_MESSAGE);
                                        return null;
                                    }
                                }catch (StringIndexOutOfBoundsException e) {}
                                break;
                            case '=':
                                cellDataBuff.insert(l, columnName);
                                l = l+columnNameLength;

                                try{
                                    c = cellDataBuff.charAt(l);
                                    if (c == '>' || c == '<' || c == '!' || c == '=') {
                                        ESlateOptionPane.showMessageDialog(QueryComponent.this,
                                                infoBundle.getString("QueryComponentMsg14") + cellDataBuff.charAt(l-1) + c + "\"",
                                                errorStr,
                                                JOptionPane.ERROR_MESSAGE);
                                        return null;
                                    }
                                }catch (StringIndexOutOfBoundsException e) {}
                                break;
                            case '!':
                                try{
                                    c = cellDataBuff.charAt(l+1);
                                }catch (StringIndexOutOfBoundsException e) {l++; break;}
                                if (c == '=') {
                                    cellDataBuff.insert(l, columnName);
                                    l = l+1+columnNameLength;
                                }else{
                                    l++;
                                    break;
                                }

                                try{
                                    c = cellDataBuff.charAt(l);
                                    if (c == '>' || c == '<' || c == '!' || c == '=') {
                                        ESlateOptionPane.showMessageDialog(QueryComponent.this,
                                                infoBundle.getString("QueryComponentMsg14") + "!=" + c + "\"",
                                                errorStr,
                                                JOptionPane.ERROR_MESSAGE);
                                        return null;
                                    }
                                }catch (StringIndexOutOfBoundsException e) {}
                                break;
                            case '"':
                                try{
//                            cellDataBuff.setCharAt(l, ' ');
                                    l++;
                                    while (cellDataBuff.charAt(l) != '"') l++;
                                    //                            cellDataBuff.setCharAt(l, ' ');
                                }catch (StringIndexOutOfBoundsException e) {
                                    ESlateOptionPane.showMessageDialog(QueryComponent.this,
                                            infoBundle.getString("QueryComponentMsg15"),
                                            errorStr,
                                            JOptionPane.ERROR_MESSAGE);
                                    return null;
                                }
                                l++;
                            case 'c':
                                String test;
                                try{
                                    test = cellDataBuff.toString().substring(l, l+8);
                                }catch (StringIndexOutOfBoundsException e) {l++; break;}
                                if (test.compareTo("contains") == 0) {
                                    cellDataBuff.insert(l, columnName);
                                    l = l+7+columnNameLength;

                                    try{
                                        c = cellDataBuff.charAt(l);
                                        if (c == '>' || c == '<' || c == '=' || c == '!') {
                                            ESlateOptionPane.showMessageDialog(QueryComponent.this,
                                                    infoBundle.getString("QueryComponentMsg14") + "contains" + c + "\"",
                                                    errorStr,
                                                    JOptionPane.ERROR_MESSAGE);
                                            return null;
                                        }
                                    }catch (StringIndexOutOfBoundsException e) {}
                                    break;
                                }else{
                                    try{
                                        test = cellDataBuff.toString().substring(l, l+9);
                                    }catch (StringIndexOutOfBoundsException e) {l++; break;}
                                    if (test.compareTo("contained") == 0) {
                                        cellDataBuff.insert(l, columnName);
                                        l = l+8+columnNameLength;

                                        try{
                                            c = cellDataBuff.charAt(l);
                                            if (c == '>' || c == '<' || c == '=' || c == '!') {
                                                ESlateOptionPane.showMessageDialog(QueryComponent.this,
                                                        infoBundle.getString("QueryComponentMsg14") + "contained" + c + "\"",
                                                        errorStr,
                                                        JOptionPane.ERROR_MESSAGE);
                                                return null;
                                            }
                                        }catch (StringIndexOutOfBoundsException e) {}
                                        break;
                                    }
                                }
                                l++;
                                break;
                            default:
                                l++;
                        }
                    }

                    query.append(cellDataBuff.toString());
                    query.append(") " + infoBundle.getString("LowerAND")+" ");
                }

            } //second for statement

            String queryString = query.toString();
            if (queryString.length() != 0)
                queryString = queryString.substring(0, queryString.lastIndexOf(infoBundle.getString("LowerAND")));
            query = new StringBuffer(queryString);

            query.append(") " + infoBundle.getString("LowerOR")+" ");
        } // first for statement

        String queryString = query.toString();
        if (queryString.length() != 0)
            queryString = queryString.substring(0, queryString.lastIndexOf(infoBundle.getString("LowerOR")));

        return queryString;
    }


    protected QueryTextViewInfo createTextQueryPanel(Table table) {
        // Proper Initialization of the interface
        DefaultListModel fieldListModel = new DefaultListModel();
        TableFieldBaseArray fields = table.getFields();
        for (int i=0; i<fields.size(); i++) {
            AbstractTableField fld = (AbstractTableField) fields.get(i);
            if (fld.getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class) || (fld.isHidden() && !table.isHiddenFieldsDisplayed()))
                continue;
            else
                fieldListModel.addElement(((AbstractTableField) fields.get(i)).getName());
        }
        QueryTextViewInfo queryTextViewInfo = new QueryTextViewInfo(fieldListModel);
        fieldListModel=null;
        return queryTextViewInfo;
    }


    protected void createDatabaseListener() {
        if (dbaseListener == null)
            dbaseListener = new QueryDatabaseListener(this);
    }

    protected void createTableModelListener() {
        if (tableModelListener == null)
            tableModelListener = new QueryTableModelListener(this);
    }

    protected void createDatabasePropertyChangeListener() {
        if (dbasePcl != null) return;
        dbasePcl = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String propertyName = ev.getPropertyName();
//                System.out.println(propertyName + ", old: " + ev.getOldValue() + ", new: " + ev.getNewValue());
                if (propertyName.equals("Hidden tables displayed")) {
                    if (db == null)
                        return;
                    boolean display = ((Boolean) ev.getNewValue()).booleanValue();
                    if (hiddenTablesDisplayed == display)
                        return;
                    else
                        hiddenTablesDisplayed = display;

                    if (!display) {
                        int removedCount = 0;
                        for (int i=0; i<db.getTableCount(); i++) {
                            Table table = db.getTableAt(i);
                            if (table.isHidden()) {
                                int index = queryViewStructure.getTableIndex(table); //db.indexOf(table);
                                if (index == -1)
                                    continue;
                                deactivateTableAt(index);
                                queryViewStructure.removeTable(table);
                                tabs.removeTabAt(index); //index-removedCount);

                                /* Destroy this table's protocol pin.
                                */
                                destroyTableHandle(table.getTitle());
                                removedCount++;
                            }
                        }
                        viewerPanel.validate();
                        viewerPanel.repaint();
                        if (tabs != null) {
                            tabs.validate();
                            tabs.repaint();
                            if (tabs.getTabCount() == 0) {
                                queryViewStructure.activeTableIndex = -1;

                                if (emptyDB == null) {
                                    createEmptyDBPanel();
                                    emptyDB.setBounds(0, 0, queryComponent.getSize().width, queryComponent.getSize().height); //-(menu.getHeight()));
                                }
                                componentPanel.remove(mainPanel);
                                mainPanel = null;
                                tabs = null;
                                componentPanel.add(emptyDB, BorderLayout.CENTER);
                                emptyDBVisible = true;

                                headerStatusButton.setEnabled(false);
                                execute.setEnabled(false);
                                clearQuery.setEnabled(false);
                                headerStatusButton.setEnabled(false);
                                addRow.setEnabled(false);
                                queryPane.setSelected(false);
                                queryPane.setEnabled(false);
                            }
                        }
                    }else{ //display==true
                        for (int i=0; i<db.getTableCount(); i++) {
                            Table table = db.getTableAt(i);
                            if (table.isHidden()) {
                                addTable(table, i);
                            }
                        }
                    }//display
                    refreshQueryComponent();
                }
            }
        };
    }

    protected void createTablePropertyChangeListener() {
        if (tablePcl != null) return;
        tablePcl = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String propertyName = ev.getPropertyName();
//                System.out.println(propertyName + ", old: " + ev.getOldValue() + ", new: " + ev.getNewValue());
                if (propertyName.equals("Hidden fields displayed")) {
                    Table table = (Table) ev.getSource();
                    int index = queryViewStructure.getTableIndex(table);
                    if (index < 0) {
                        return;
                    }

                    JTable qTable = queryViewStructure.getQTablePanel(index).getQTable();
                    QueryTextViewInfo info = queryComponent.queryViewStructure.getTextViewInfo(index);
                    AbstractTableField f;
                    for (int i=0; i<table.getFieldCount(); i++) {
                        try{
                            f = table.getTableField(i);
                            TableColumn col = qTable.getColumn(f.getName());

                            if (((Boolean) ev.getNewValue()).booleanValue()) {
                                if (f.isHidden() && !f.getDataType().getName().equals("gr.cti.eslate.database.engine.CImageIcon")) {
                                    col.setMaxWidth(1000);
                                    col.setWidth(70);
                                    col.setMinWidth(40);
                                    col.setResizable(true);
                                    info.myListModel.addElement(f.getName());
                                }
                            }else{
                                if (f.isHidden()) {
                                    col.setMinWidth(0);
                                    col.setWidth(0);
                                    col.setMaxWidth(0);
                                    col.setResizable(false);
                                    info.myListModel.removeElement(f.getName());
                                }
                            }
                        }catch (InvalidFieldIndexException exc) {}
                    }

                    if (index == queryViewStructure.activeTableIndex) {
                        queryComponent.textView.loadNewInfo(info);
                        // ((JList) fieldListArray.at(visibleIndex)).repaint();
                        qTable.getTableHeader().repaint();
                        qTable.repaint();
                    }
                }else if (propertyName.equals("Field reordering allowed")) {
                    Table table = (Table) ev.getSource();
                    int index = queryViewStructure.getTableIndex(table);
                    if (index < 0) {
                        return;
                    }

                    JTable qTable = queryViewStructure.getQTablePanel(index).getQTable();
                    qTable.getTableHeader().setReorderingAllowed(((Boolean) ev.getNewValue()).booleanValue());
                }
            }
        };
    }


    protected void addTable(Table newTable, int pos) {
        if (tabs == null) {
            if (emptyDBVisible)
                componentPanel.remove(emptyDB);
            initializeTabPane();
        }
        createQueryPanel(newTable, pos);
        if (pos == -1)
            pos = queryViewStructure.size()-1;
        if (textView == null)
            textView = new QueryTextView(this, queryViewStructure.getTextViewInfo(pos).myListModel);

//        textView.loadNewInfo(queryViewStructure.getTextViewInfo(pos));

        // Create the protocol pin for this table.
        createTablePin(newTable, -1);
        componentPanel.validate();
        componentPanel.paintImmediately(componentPanel.getVisibleRect());
    }

    protected void showEmptyPanel() {
        if (emptyDBVisible) {
            componentPanel.remove(emptyDB);
            emptyDBVisible = false;
        }
        emptyPanelVisible = true;
        componentPanel.add(emptyPanel, BorderLayout.CENTER);
        componentPanel.validate();
        componentPanel.doLayout();
//        componentPanel.paintImmediately(componentPanel.getVisibleRect());
        queryComponent.validate();
        queryComponent.repaint();
    }


    protected void showEmptyDBPanel() {
        if (emptyPanelVisible) {
            componentPanel.remove(emptyPanel);
            emptyPanelVisible = false;
        }
        emptyDBVisible = true;
        componentPanel.add(emptyDB, BorderLayout.CENTER);
    }


    protected void initializeTabPane() {
        if (tabs != null) return;
        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout());
        tabs = new JTabbedPane() {
            public Dimension getPreferredSize() {
                int width;
                int levels=getTabRunCount();

                try {
                    width=queryComponent.getPreferredSize().width;
                } catch (Exception ec) {
                    return new Dimension(0,0);
                }
                int height=0;

                if (levels==getTabCount()) {
                    levels=myLevels;
                }
                //if (tabs.getSelectedIndex() == 0 && levels>1) {

                //     height = queryComponent.startingHeight+5;

                // Fix the multiple line tabbedpane.
                if (levels>1)
                    height = queryComponent.startingHeight+10*levels+4*(levels-1);
                else
                    height = queryComponent.startingHeight+5;
                myLevels=levels;
                return new Dimension(width, height);
            }
        };


        viewerPanel = new JPanel(true);
        viewerPanel.setLayout(new BorderLayout());
        mainPanel.add(tabs, BorderLayout.NORTH);
        mainPanel.add(viewerPanel, BorderLayout.CENTER);

        componentPanel.add(mainPanel, BorderLayout.CENTER);

        viewerPanel.setVisible(true);
        tabs.setVisible(true);

        tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!iterateEvent) {
                    return;
                }
                /* If the last tab was removed from "tabs".
                */
                if (tabs.getSelectedIndex() == -1){
                    return;
                }
                if (tabs.getSelectedIndex() >= queryViewStructure.size()){
                    return;
                }
                activateTableAt(tabs.getSelectedIndex());

                if (!queryViewStructure.isQBEModeActive(queryViewStructure.activeTableIndex)) {
                    addRow.setVisible(false);
                    removeRow.setVisible(false);
                    headerStatusButton.setVisible(false);
                } else {
                    addRow.setVisible(true);
                    removeRow.setVisible(true);
                    headerStatusButton.setVisible(true);
                }

            }
        });
    }

    void createQueryPanels() {
        initializeTabPane();
        for (int i=0; i<db.getTableCount(); i++) {
            Table table = (Table) db.getTableAt(i);
            if (db.isHiddenTablesDisplayed() || !table.isHidden()) {
                addTable(table, -1);
            }
        }

        if (db.getActiveTable() == null || (db.getActiveTable().isHidden() && !db.isHiddenTablesDisplayed())) {
            db.activateFirstNonHiddenTable();
        }else{
            int visibleIndex = db.toVisibleTableIndex(db.getActiveTableIndex());
            if (visibleIndex == -2) {
                db.activateFirstNonHiddenTable();
                visibleIndex = db.toVisibleTableIndex(db.getActiveTableIndex());
            }
            iterateEvent = false;
            activateTableAt(visibleIndex);
            iterateEvent = true;
        }
    }

    public void createTextFieldKeyListener(JTextField tf) {
        tf.addKeyListener(new TextFieldKeyListener());
    }


    public void createQueryPanel(Table table, int pos) {
//      System.out.println("in createQueryPanel :\t"+pos);
        firstTime=false;
        QueryTextViewInfo info = createTextQueryPanel(table);
        class SmallPanel extends JPanel{
            public Dimension getPreferredSize() {
                return new Dimension(100,2);
            }

            public void updateUI() {
                super.updateUI();
                setBackground(UIManager.getColor("control"));
            }
        }
        SmallPanel small = new SmallPanel();
        small.setVisible(false);

        JTable qTable;
        String title;
        qTable = createQTable(table);

        title = table.getTitle();
        QTablePanel qTablePanel = new QTablePanel(qTable);

        if (pos == -1) {
            /* Create the new tab.
            */
            tabs.addTab(title,small);
            startingHeight=tabs.getBoundsAt(0).height;
            queryViewStructure.addTable(table, TEXT_QUERY, qTablePanel, info);
        }else{
            tabs.insertTab(title,null,small,null,pos);
            queryViewStructure.insertTable(table, TEXT_QUERY, qTablePanel, info, pos);
        }

        qTable.getTableHeader().setUpdateTableInRealTime(true);

        int i = queryViewStructure.size()-1;

//            Array columnPositions = ((DBTable) ((Array) dbComponent.getDBTables()).at(i)).getColumnOrder();
/*1        if (table.getTableView() != null) {
            DefaultTableColumnModel dtcm = (DefaultTableColumnModel) qTable.getColumnModel();
            Array columnPositions = table.getTableView().getColumnOrder();
            int order;
            for (int k=0; k<columnPositions.size(); k++) {
                order = columnPositions.indexOf(new Integer(k));
                if (order != k) {
                    dtcm.moveColumn(order, k);
                    Integer tmp = (Integer) columnPositions.at(order);
                    columnPositions.remove(order);
                    columnPositions.insert(k, tmp);
                }
            }

        }
1*/
        qTable.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int activeTableIndex = queryViewStructure.activeTableIndex;
                JTable qTable1 = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
                if (qTable1.isEditing())
                    enableRemoveRowButton();
            }
            public void mousePressed(MouseEvent e) {
                int activeTableIndex = queryViewStructure.activeTableIndex;
                JTable qTable1 = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
                int columnIndex = qTable1.columnAtPoint(e.getPoint());
                int rowIndex = qTable1.rowAtPoint(e.getPoint());
                if (rowIndex == -1 || columnIndex == -1)
                    return;
                qTable1.setColumnSelectionInterval(columnIndex, columnIndex);
                qTable1.setRowSelectionInterval(rowIndex, rowIndex);
                /* If this was not a right click.
                */
                if (e.getModifiers() != e.BUTTON3_MASK) {
                    qTable1.editCellAt(rowIndex, columnIndex);
                    ((JTextField) qTable1.getEditorComponent()).getCaret().setVisible(true);
                }
            }
        });

        qTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
        qTable.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int activeTableIndex = queryViewStructure.activeTableIndex;
                JTable qTable1 = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();

                int selectedRow = qTable1.getSelectedRow();
                int selectedCol = qTable1.getSelectedColumn();
                if (selectedRow == -1 || selectedCol==-1)
                    return;

                qTable1.editCellAt(selectedRow, selectedCol);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        qTable.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int activeTableIndex = queryViewStructure.activeTableIndex;
                JTable qTable1 = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();

                int selectedRow = qTable1.getSelectedRow();
                int selectedCol = qTable1.getSelectedColumn();
                if (selectedRow == -1 || selectedCol==-1)
                    return;

                qTable1.editCellAt(selectedRow, selectedCol);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        /* If the query component is connected to a Database component, then
        * column reordering is not allowed. It can only be done through the
        * Database component.
        */
        execute.setEnabled(false);
        clearQuery.setEnabled(false);
        if (table.getFieldCount() > 0) {
            headerStatusButton.setEnabled(true);
            addRow.setEnabled(true);
        }else{
            headerStatusButton.setEnabled(false);
            addRow.setEnabled(false);
        }
        queryPane.setEnabled(true);
        queryPane.setSelected(false);

    }

/*    public ESlateInfo getInfo()
{
String info[] = {
infoBundle.getString("part"),
infoBundle.getString("design"),
infoBundle.getString("development"),
infoBundle.getString("funding"),
infoBundle.getString("copyright")
};
return new ESlateInfo(infoBundle.getString("compo"), info);
}*/


    protected void createTablePins(DBase db) {
        for (int i=0; i<db.getTableCount(); i++) {
            if (db.isHiddenTablesDisplayed() || !db.getTableAt(i).isHidden())
                createTablePin(db.getTableAt(i), -1);
        }
    }


    protected void createTablePin(Table table, int pos) {
//        try{
//            Class tableSOClass = Class.forName("gr.cti.eslate.sharedObject.TableSO");
//            try{
//                TableSO tableSO = new TableSO(eSlateHandle);
//                Pin tableSOPin = new MultipleOutputPin(this.eSlateHandle, infoBundle.getString("ResultTable") + table.getTitle() + "\"", tablePinColor,
//                                                             tableSOClass, tableSO);

        /* This is the table in which the results of the queries issued on
        * 'table' will be stored. This happens only if the 'tableSOPin' is
        * connected, i.e. exported.
        */
        IntBaseArray fieldIndices = new IntBaseArray();
        for (int i=0; i<table.getFieldCount(); i++)
            fieldIndices.add(i);
System.out.print("QueryComponent fieldIndices: ");
for (int i=0; i<fieldIndices.size(); i++)
    System.out.print(fieldIndices.get(i) + ", ");
System.out.println("");
        TableView resultTable = null;
        try {
            resultTable = new TableView(table, fieldIndices, table.getSelectedSubset());
        } catch (InvalidRecordIndexException e) {
            System.out.println("Serious inconsistency error in QueryComponent createTablePin(): (0.1)");
            e.printStackTrace();
        } catch (InvalidFieldIndexException e) {
            System.out.println("Serious inconsistency error in QueryComponent createTablePin(): (0.2)");
            e.printStackTrace();
        }
        /* We create the handle of the result table, so that it won't be
        * stored by the Database components it is imported to.
        */
        ESlateHandle tableHandle = resultTable.getESlateHandle();
        eSlateHandle.add(tableHandle);
        String tableName = infoBundle.getString("ResultTable") + table.getTitle() + "\"";
        try{
            resultTable.setTitle(tableName);
            resultTable.resetModified();
            tableHandle.setComponentName(tableName);
        }catch (NameUsedException exc) {
            try{
                tableHandle.setUniqueComponentName(tableName);
                resultTable.setTitle(tableHandle.getComponentName());
                resultTable.resetModified();
            }catch (Exception exc1) {
                eSlateHandle.remove(tableHandle);
                System.out.println("Serious inconsistency error in QueryComponent createTablePin(): (5)");
            }
        }catch (RenamingForbiddenException exc) {
            System.out.println("Serious inconsistency error in QueryComponent createTablePin(): (4)");
        }catch (InvalidTitleException exc) {
            System.out.println("Serious inconsistency error in QueryComponent createTablePin(): (2)");
        }catch (java.beans.PropertyVetoException exc) {
            System.out.println("Serious inconsistency error in QueryComponent createTablePin(): (3)");
        }
        queryResults.addResultTable(table, resultTable);
//                tableSO.setTable(resultTable);
        Plug tableSOPin = resultTable.getTablePlug();
        ESlateMicroworld mwd = eSlateHandle.getESlateMicroworld();
        if (mwd != null) {
            try{
                mwd.setPlugAliasForLoading(tableSOPin, eSlateHandle, new String[]{tableName});
            }catch (Exception exc) {
                System.out.println("createTablePins(): Unable to redirect old table plugs to the plugs of the internal Table components");
            }
        }
//                eSlateHandle.addPin(tableSOPin);
/*                tableSOPin.addConnectionListener(new ConnectionListener() {
public void handleConnectionEvent(ConnectionEvent e) {
queryComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
Pin tablePin = (Pin) e.getOwnPin();
//                        Table sourceTable = resultTablePlugs.getTable(tablePin);
//                        Table resultTable = queryResults.getResultTable(sourceTable);
if (e.getType() == Pin.OUTPUT_CONNECTION) {
Pin oppositeSidePin = e.getPin();
SharedObject tableSO = (TableSO) tablePin.getSharedObject();
SharedObjectEvent soe = new SharedObjectEvent(QueryComponent.this, tableSO);
((SharedObjectListener)(oppositeSidePin.getSharedObjectListener())).handleSharedObjectEvent(soe);
}

queryComponent.setCursor(Cursor.getDefaultCursor());
}
});

tableSOPin.addDisconnectionListener(new DisconnectionListener() {
public void handleDisconnectionEvent(DisconnectionEvent e) {
Pin tablePlug = (Pin) e.getOwnPin();
Table resultTable = ((TableSO) tablePlug.getSharedObject()).getTable();
Table sourceTable = resultTablePlugs.getTable(tablePlug);
queryResults.removeResultTable(resultTable);
}
});
*/
        resultTablePlugs.addPlug(table, tableSOPin);
//            } catch (InvalidPinParametersException e) {
//              System.out.println("1 "  + e.getMessage());
//            }
//              catch (PinExistsException e) {
//              System.out.println("2 " + e.getMessage());
//            }
//        }catch (ClassNotFoundException e) {System.out.println(e.getMessage());}
//         catch (NoClassDefFoundError e) {System.out.println(e.getMessage());}
    }


    protected void destroyTableHandles() {
/*        ESlateHandle[] handles = eSlateHandle.toArray();
for (int i=0; i<handles.length; i++) {
eSlateHandle.remove(handles[i]);
handles[i].toBeDisposed(false, new BooleanWrapper(true));
System.out.println("QueryComponent 1 Disposing table handle: " + handles[i]);
handles[i].dispose();
}
*/
/*        try{
for (int i=0; i<resultTablePlugs.getPlugCount(); i++) {
Pin p = (Pin) resultTablePlugs.getPlug(i);
p.disconnect();
eSlateHandle.removePin(p);
}
}catch (NoSuchPinException exc) {
System.out.println("Serious inconsistency error in QueryComponent destroyTablePins(): 1");
}catch (IllegalArgumentException exc) {
System.out.println("Serious inconsistency error in QueryComponent destroyTablePins(): 2");
}
*/
        resultTablePlugs.clearPlugs();
    }


    protected void destroyTableHandle(String sourceTableName) {
//        System.out.println("destroyTableHandle sourceTableName: " + sourceTableName);
        Plug plug = resultTablePlugs.getPlug(sourceTableName);
        ESlateHandle handle = plug.getHandle();
        if (handle != null && handle.getParentHandle() == eSlateHandle)
            eSlateHandle.remove(handle);
/*        if (handle.getParentHandle() == null && !isDesktopComponent(handle)) {
handle.toBeDisposed(false, new BooleanWrapper(true));
handle.dispose();
}
*/
/*        Pin plug = resultTablePlugs.getPlug(sourceTableName);
try{
plug.disconnect();
eSlateHandle.removePin(plug);
resultTablePlugs.removePlug(plug);
}catch (NoSuchPinException exc) {
System.out.println("Serious inconsistency error in QueryComponent destroyTablePin(): 1");
}catch (IllegalArgumentException exc) {
System.out.println("Serious inconsistency error in QueryComponent destroyTablePin(): 2");
}
*/
        queryResults.removeSourceTable(sourceTableName);
    }


    public void executeQueryCommand() {
        int activeTableIndex = queryViewStructure.activeTableIndex;
        if (queryViewStructure.isQBEModeActive(activeTableIndex)) {
            JTable qTable = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
            if (qTable.isEditing())
                ((DefaultCellEditor) ((DefaultTableColumnModel) qTable.getColumnModel()).getColumn(qTable.getEditingColumn()).getCellEditor()).stopCellEditing();
        }

        String query = createQuery(activeTableIndex);
        if (query == null)
            return;
//                System.out.println(query);

//                if (queryComponent.dbComponent!=null) {
        queryComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
            Table activeTable = db.getActiveTable();
            LogicalExpression le = new LogicalExpression(activeTable, query, selectionMode, true);

            Plug resultExportPlug = resultTablePlugs.getPlug(activeTable);
            /* If the query results on this table are exported, then
            * update the exported tables.
            */
            if (resultTablePlugs.isPlugConnected(resultExportPlug)) {
                Table sourceTable = resultTablePlugs.getTable(resultExportPlug);
                TableView resultTable = queryResults.getResultTable(activeTable);
                try {
                    resultTable.setViewRecords(sourceTable.getSelectedSubset());
                } catch (InvalidRecordIndexException e) {
                    e.printStackTrace();
                } catch (TableNotExpandableException e) {
                    e.printStackTrace();  
                }
///                Table resultTable = DBase.createTableFromSelectedSet(sourceTable);
///                queryResults.updateResultTableOfSourceTable(sourceTable, resultTable);
            }
            queryComponent.setCursor(Cursor.getDefaultCursor());
        }catch (InvalidLogicalExpressionException ex) {
            ex.printStackTrace();
            queryComponent.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(QueryComponent.this,
                    ex.getMessage(),
                    errorStr,
                    JOptionPane.ERROR_MESSAGE);
//                         System.out.println(ex.getMessage());
        }
//                }
        removeRow.setEnabled(false);

//                System.out.println("execute(): " + removeRow.isEnabled());
    }

    private void initialize() {
        //Create datatype imageIcons
        stringIcon = new ImageIcon(getClass().getResource("images/string.gif"));
        integerIcon = new ImageIcon(getClass().getResource("images/integer.gif"));
        doubleIcon = new ImageIcon(getClass().getResource("images/double2.gif"));
        booleanIcon = new ImageIcon(getClass().getResource("images/boolean.gif"));
        imageIcon = new ImageIcon(getClass().getResource("images/image.gif"));
        timeIcon = new ImageIcon(getClass().getResource("images/time.gif"));
        dateIcon = new ImageIcon(getClass().getResource("images/date.gif"));
        urlIcon = new ImageIcon(getClass().getResource("images/url.gif"));
        keyStringIcon = new ImageIcon(getClass().getResource("images/keyString.gif"));
        keyIntegerIcon = new ImageIcon(getClass().getResource("images/keyInteger.gif"));
        keyDoubleIcon = new ImageIcon(getClass().getResource("images/keyDouble.gif"));
        keyBooleanIcon = new ImageIcon(getClass().getResource("images/keyBoolean.gif"));
        keyImageIcon = new ImageIcon(getClass().getResource("images/keyImage.gif"));
        keyTimeIcon = new ImageIcon(getClass().getResource("images/keyTime.gif"));
        keyDateIcon = new ImageIcon(getClass().getResource("images/keyDate.gif"));
        keyURLIcon = new ImageIcon(getClass().getResource("images/keyUrl.gif"));


        componentPanel = new JPanel(true);
        componentPanel.setLayout(new BorderLayout());
        componentPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (emptyPanelVisible) {
                    emptyPanel.setBorder(new EmptyBorder(
                            (getSize().height - 50)/2 - noDatabaseLabelHeight/2 - 15,
                            (getSize().width - noDatabaseLabelWidth+2)/2-25,
                            (getSize().height - 50)/2 - noDatabaseLabelHeight/2 - 15,
                            (getSize().width - noDatabaseLabelWidth+2)/2-25
                    ));
                    emptyPanel.validate();
                }

                if (emptyDBVisible) {
                    emptyDBLabel.setBorder(new EmptyBorder(0, (getSize().width - emptyDBLabelWidth)/2-10, 0, (getSize().width - emptyDBLabelWidth)/2-10));
                    emptyDB.validate();
                }
            }
        });

        // Put the buttons customization code here
        Dimension buttonSize = new Dimension(27, 24);
        Insets zeroInsets = new Insets(0,0,0,0);
        final Insets newInsets = new Insets(0,0,1,0);
        Insets queryPaneInsets = new Insets(1,0,0,0);

        headerStatusButton = new JToggleButton(new ImageIcon(this.getClass().getResource("images/lightball1.gif")),
                false);
        headerStatusButton.setSelectedIcon(new ImageIcon(this.getClass().getResource("images/lightball.gif")));
        headerStatusButton.setToolTipText(infoBundle.getString("QueryComponentMsg1"));
        headerStatusButton.setMargin(zeroInsets);
        headerStatusButton.setRequestFocusEnabled(false);
        headerStatusButton.setMinimumSize(buttonSize);
        headerStatusButton.setMaximumSize(buttonSize);
        headerStatusButton.setPreferredSize(buttonSize);
        headerStatusButton.setBorderPainted(false);

        //  ImageIcon icon = new ImageIcon(this.getClass().getResource("images/answer_good.gif"));
        ImageIcon icon = new ImageIcon(this.getClass().getResource("images/gogo.gif"));
        icon.setImage(icon.getImage().getScaledInstance(19, 16, Image.SCALE_DEFAULT));
        execute = new JButton(icon);
        execute.setToolTipText(infoBundle.getString("QueryComponentMsg2"));
        execute.setMargin(zeroInsets);
        execute.setRequestFocusEnabled(false);
        execute.setMinimumSize(buttonSize);
        execute.setMaximumSize(buttonSize);
        execute.setPreferredSize(buttonSize);
        execute.setBorderPainted(false);

        // icon = new ImageIcon(this.getClass().getResource("images/answer_bad1.gif"));
        icon = new ImageIcon(this.getClass().getResource("images/sercova.gif"));
        clearQuery = new JButton(icon);
        clearQuery.setToolTipText(infoBundle.getString("QueryComponentMsg3"));
        clearQuery.setMargin(zeroInsets);
        clearQuery.setRequestFocusEnabled(false);
        clearQuery.setMinimumSize(buttonSize);
        clearQuery.setMaximumSize(buttonSize);
        clearQuery.setPreferredSize(buttonSize);
        clearQuery.setBorderPainted(false);

        //icon = new ImageIcon(this.getClass().getResource("images/addRow.gif"));//gr_plus1.gif"));
        icon = new ImageIcon(this.getClass().getResource("images/plus.gif"));//gr_plus1.gif"));
        addRow = new JButton(icon);
        addRow.setToolTipText(infoBundle.getString("QueryComponentMsg4"));
        addRow.setMargin(zeroInsets);
        addRow.setRequestFocusEnabled(false);
        addRow.setMinimumSize(buttonSize);
        addRow.setMaximumSize(buttonSize);
        addRow.setPreferredSize(buttonSize);
        addRow.setBorderPainted(false);



        //icon = new ImageIcon(this.getClass().getResource("images/pi_minus1.gif"));
        icon = new ImageIcon(this.getClass().getResource("images/newMinus.gif"));
        removeRow = new JButton(icon);
        removeRow.setToolTipText(infoBundle.getString("QueryComponentMsg5"));
        removeRow.setMargin(zeroInsets);
        removeRow.setRequestFocusEnabled(false);
        removeRow.setMinimumSize(buttonSize);
        removeRow.setMaximumSize(buttonSize);
        removeRow.setPreferredSize(buttonSize);
        removeRow.setHorizontalAlignment(SwingConstants.CENTER);
        removeRow.setEnabled(false);
        removeRow.setBorderPainted(false);

        icon = new ImageIcon(this.getClass().getResource("images/pouch.gif"));
        newSelection = new JToggleButton(icon);
        newSelection.setToolTipText(infoBundle.getString("QueryComponentMsg6"));
        newSelection.setRequestFocusEnabled(false);
        newSelection.setMargin(zeroInsets);
        newSelection.setMinimumSize(buttonSize);
        newSelection.setMaximumSize(buttonSize);
        newSelection.setPreferredSize(buttonSize);
        newSelection.setHorizontalAlignment(SwingConstants.CENTER);


        icon = new ImageIcon(this.getClass().getResource("images/ppp+.gif"));
        addToSelection = new JToggleButton(icon);
        addToSelection.setToolTipText(infoBundle.getString("QueryComponentMsg7"));
        addToSelection.setRequestFocusEnabled(false);
        addToSelection.setMargin(zeroInsets);
        addToSelection.setMinimumSize(buttonSize);
        addToSelection.setMaximumSize(buttonSize);
        addToSelection.setPreferredSize(buttonSize);
        addToSelection.setHorizontalAlignment(SwingConstants.CENTER);
        addToSelection.setBorderPainted(false);


        icon = new ImageIcon(this.getClass().getResource("images/pouch-.gif"));
        removeFromSelection = new JToggleButton(icon);
        removeFromSelection.setToolTipText(infoBundle.getString("QueryComponentMsg8"));
        removeFromSelection.setRequestFocusEnabled(false);
        removeFromSelection.setMargin(zeroInsets);
        removeFromSelection.setMinimumSize(buttonSize);
        removeFromSelection.setMaximumSize(buttonSize);
        removeFromSelection.setPreferredSize(buttonSize);
        removeFromSelection.setHorizontalAlignment(SwingConstants.CENTER);
        removeFromSelection.setBorderPainted(false);


        icon = new ImageIcon(this.getClass().getResource("images/pppsel.gif"));
        selectFromSelection = new JToggleButton(icon);
        selectFromSelection.setToolTipText(infoBundle.getString("QueryComponentMsg9"));
        selectFromSelection.setRequestFocusEnabled(false);
        selectFromSelection.setMargin(zeroInsets);
        selectFromSelection.setMinimumSize(buttonSize);
        selectFromSelection.setMaximumSize(buttonSize);
        selectFromSelection.setPreferredSize(buttonSize);
        selectFromSelection.setHorizontalAlignment(SwingConstants.CENTER);
        selectFromSelection.setBorderPainted(false);


        selectionModeButtons = new Array();
        selectionModeButtons.add(newSelection);
        selectionModeButtons.add(addToSelection);
        selectionModeButtons.add(removeFromSelection);
        selectionModeButtons.add(selectFromSelection);

        newSelection.setSelected(true);
        selectionMode = 0;
        newSelection.setBorder(new BevelBorder(BevelBorder.LOWERED));

        queryPane = new JToggleButton(new ImageIcon(this.getClass().getResource("images/ac.gif")), false);
        queryPane.setSelectedIcon(new ImageIcon(this.getClass().getResource("images/table.gif")));
        queryPane.setToolTipText(infoBundle.getString("QueryComponentMsg10"));
        queryPane.setRequestFocusEnabled(false);
        queryPane.setMinimumSize(buttonSize);
        queryPane.setMaximumSize(buttonSize);
        queryPane.setPreferredSize(buttonSize);
        queryPane.setHorizontalAlignment(SwingConstants.CENTER);
        queryPane.setVerticalAlignment(SwingConstants.CENTER);
        queryPane.setVerticalAlignment(SwingConstants.TOP);
        queryPane.setMargin(queryPaneInsets);
        queryPane.setBorderPainted(false);

        headerStatusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (headerStatusButton.isSelected()) {
                    headerIconsVisible = true;
                    displayHeaderIcons(true);
                }else{
                    headerIconsVisible = false;
                    displayHeaderIcons(false);
                }
            }
        });

        MouseListener BorderHighlightListener = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                ((javax.swing.AbstractButton)e.getSource()).setBorderPainted(true);
            }
            public void mouseExited(MouseEvent e) {
                ((javax.swing.AbstractButton)e.getSource()).setBorderPainted(false);
            }
        };

        execute.addMouseListener(BorderHighlightListener);
        clearQuery.addMouseListener(BorderHighlightListener);
        addRow.addMouseListener(BorderHighlightListener);
        removeRow.addMouseListener(BorderHighlightListener);
        headerStatusButton.addMouseListener(BorderHighlightListener);

        queryPane.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!queryPane.isSelected()) {
                    queryPane.setBorderPainted(true);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!queryPane.isSelected()) {
                    queryPane.setBorderPainted(false);
                }
            }
            public void mousePressed(MouseEvent e) {
                if (queryPane.isSelected()) {
                    queryPane.setBorderPainted(true);
//                    queryPane.setMargin(new Insets(0,1,0,0));
                }
            }
            public void mouseReleased(MouseEvent e) {

            }
        });

        execute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeQueryCommand();
            }
        });

        this.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeQueryCommand();
            }
        },KeyStroke.getKeyStroke(KeyEvent.VK_F3,0),this.WHEN_IN_FOCUSED_WINDOW);


        clearQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int activeTableIndex = queryViewStructure.activeTableIndex;
                JTable qTable = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
                if (qTable.isEditing())
                    ((DefaultCellEditor) ((DefaultTableColumnModel) qTable.getColumnModel()).getColumn(qTable.getEditingColumn()).getCellEditor()).cancelCellEditing();

                removeRow.setEnabled(false);
                queryViewStructure.getQTablePanel(activeTableIndex).getQTableModel().empty();
//                ((QTableModel) QTableModels.at(activeQTableIndex)).empty();

                execute.setEnabled(false);
                clearQuery.setEnabled(false);
                qTable.validate();
                qTable.paintImmediately(qTable.getVisibleRect());

                /* Clear the JTextArea of the textQueryPanel, too.
                */
                //  ((JTextArea) queryAreas.at(activeQTableIndex)).setText("");
                textView.clearQueryArea();
                queryViewStructure.getTextViewInfo(activeTableIndex).saveInfo(
                        textView.getFieldListSelectionIndex(),
                        textView.getQueryAreaText()
                );
            }
        });

        addRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int activeTableIndex = queryViewStructure.activeTableIndex;
                JTable qTable = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
                queryViewStructure.getQTablePanel(activeTableIndex).getQTableModel().addRow();
//                ((QTableModel) QTableModels.at(activeQTableIndex)).addRow();

                qTable.revalidate();
                qTable.repaint();
                qTable.scrollRectToVisible(qTable.getCellRect(qTable.getRowCount()-1, 0, true)); //newfp.getBounds());
            }
        });

        removeRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int activeTableIndex = queryViewStructure.activeTableIndex;
                JTable qTable = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
                if (!qTable.isEditing())
                    return;

                int rowIndex = qTable.getEditingRow();
                ((DefaultCellEditor) ((DefaultTableColumnModel) qTable.getColumnModel()).getColumn(qTable.getEditingColumn()).getCellEditor()).cancelCellEditing();

                queryViewStructure.getQTablePanel(activeTableIndex).getQTableModel().removeRow(rowIndex);

                JScrollPane scrollpane = (JScrollPane) qTable.getParent().getParent();
                scrollpane.validate();
                scrollpane.paintImmediately(scrollpane.getVisibleRect());

                queryComponent.removeRow.setEnabled(false);
                checkToolStatus();
            }
        });

        queryPane.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int activeTableIndex = queryViewStructure.activeTableIndex;
                if (queryPane.isSelected()) {
                    // This will not work whith the new TabbedPane approach.....
                    viewerPanel.removeAll();
                    viewerPanel.add(textView);
                    textView.loadNewInfo(queryViewStructure.getTextViewInfo(activeTableIndex));
                    //((CardLayout) ((JPanel) tabs.getComponentAt(activeQTableIndex)).getLayout()).first((JPanel)tabs.getComponentAt(activeQTableIndex));
                } else {
                    viewerPanel.removeAll();
                    viewerPanel.add(queryViewStructure.getQTablePanel(activeTableIndex));

                    //          ((CardLayout) ((JPanel) tabs.getComponentAt(activeQTableIndex)).getLayout()).last((JPanel) tabs.getComponentAt(activeQTableIndex));
                    JTable qTable = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
                    if (qTable.isEditing())
                        ((DefaultCellEditor) ((DefaultTableColumnModel) qTable.getColumnModel()).getColumn(qTable.getEditingColumn()).getCellEditor()).stopCellEditing();
                }

                String QBEqueryString = createQuery(activeTableIndex);
                queryViewStructure.inverseViewMode(activeTableIndex);
                String textQueryPanelString = createQuery(activeTableIndex);

                if (queryViewStructure.isQBEModeActive(activeTableIndex)) {
                    addRow.setVisible(true);
                    removeRow.setVisible(true);
                    headerStatusButton.setVisible(true);

                    if (!QBEqueryString.equals(textQueryPanelString)) {
                        queryPane.paintImmediately(queryPane.getVisibleRect());
                        ESlateOptionPane.showMessageDialog(QueryComponent.this,
                                infoBundle.getString("QueryComponentMsg11"),
                                warningStr,
                                JOptionPane.WARNING_MESSAGE);
                    }

                    JTable qTable1 = queryViewStructure.getQTablePanel(activeTableIndex).getQTable();
                    if (qTable1.getColumnCount() > 0) {
                        addRow.setEnabled(true);
                        headerStatusButton.setEnabled(true);
                    }else{
                        addRow.setEnabled(false);
                        removeRow.setEnabled(false);
                    }

                    checkRemoveRowStatus();
                }else{
                    /* When moving from the QBE panel to the textQueryPanel, copy the existing
                    * query to the "queryArea" of the textQueryPanel, if exists.
                    */

                    if (QBEqueryString != null && QBEqueryString.length() != 0)
                        textView.setQueryAreaText(QBEqueryString);
                    addRow.setEnabled(false);
                    removeRow.setEnabled(false);
                    headerStatusButton.setEnabled(false);
                    addRow.setVisible(false);
                    removeRow.setVisible(false);
                    headerStatusButton.setVisible(false);
                }
                viewerPanel.revalidate();
                viewerPanel.doLayout();
                viewerPanel.paintImmediately(viewerPanel.getVisibleRect());
            }
        });

        execute.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                pressedTool = execute;
            }
            public void mouseReleased(MouseEvent e) {
                pressedTool = null;
            }
        });

        clearQuery.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                pressedTool = clearQuery;
            }
            public void mouseReleased(MouseEvent e) {
                pressedTool = null;
            }
        });

        addRow.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                pressedTool = addRow;
            }
            public void mouseReleased(MouseEvent e) {
                pressedTool = null;
            }
        });

        removeRow.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                pressedTool = removeRow;
            }
            public void mouseReleased(MouseEvent e) {
                pressedTool = null;
            }
        });

        newSelection.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (selectionMode == 0)
                    return;

                JToggleButton butt = (JToggleButton) selectionModeButtons.at(selectionMode);
                butt.setBorderPainted(false);
                previousSelectionMode = selectionMode;
                selectionMode = 0;
                selectionButtonPressed = true;

                butt = (JToggleButton) selectionModeButtons.at(selectionMode);

                newSelection.setBorder(new BevelBorder(BevelBorder.LOWERED));   //new CompoundBorder(new EtchedBorder(), new SoftBevelBorder(SoftBevelBorder.LOWERED)));
                butt.setBorderPainted(true);
                newSelection.paintImmediately(butt.getVisibleRect());
            }
            public void mouseReleased(MouseEvent e) {
                selectionButtonPressed = false;
            }
        });

        addToSelection.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (selectionMode == 1)
                    return;

                JToggleButton butt = (JToggleButton) selectionModeButtons.at(selectionMode);
                butt.setBorderPainted(false);

                previousSelectionMode = selectionMode;
                selectionButtonPressed = true;
                selectionMode = 1;
                butt = (JToggleButton) selectionModeButtons.at(selectionMode);

                butt.setBorder(new BevelBorder(BevelBorder.LOWERED));
                butt.setBorderPainted(true);
                butt.setMargin(newInsets);
                butt.repaint();
                //butt.paintImmediately(butt.getVisibleRect());
            }
            public void mouseReleased(MouseEvent e) {
                selectionButtonPressed = false;
            }
        });

        removeFromSelection.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (selectionMode == 2)
                    return;

                JToggleButton butt = (JToggleButton) selectionModeButtons.at(selectionMode);
                butt.setBorderPainted(false);
                previousSelectionMode = selectionMode;
                selectionButtonPressed = true;
                selectionMode = 2;
                butt = (JToggleButton) selectionModeButtons.at(selectionMode);
                butt.setBorder(new BevelBorder(BevelBorder.LOWERED));
                butt.setBorderPainted(true);
                butt.paintImmediately(butt.getVisibleRect());
            }
            public void mouseReleased(MouseEvent e) {
                selectionButtonPressed = false;
            }
        });

        selectFromSelection.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (selectionMode == 3)
                    return;
                JToggleButton butt = (JToggleButton) selectionModeButtons.at(selectionMode);
                butt.setBorderPainted(false);
                previousSelectionMode = selectionMode;
                selectionButtonPressed = true;
                selectionMode = 3;
                selectFromSelection.setBorder(new BevelBorder(BevelBorder.LOWERED));
                selectFromSelection.setBorderPainted(true);
                selectFromSelection.paintImmediately(selectFromSelection.getVisibleRect());
            }
            public void mouseReleased(MouseEvent e) {
                selectionButtonPressed = false;
            }
        });

        createEmptyPanel();
        createEmptyDBPanel();

        /* Initially the component is blank.
        */
        showEmptyPanel();
        execute.setEnabled(false);
        clearQuery.setEnabled(false);
        headerStatusButton.setEnabled(false);
        headerStatusButton.setSelected(false);
        addRow.setEnabled(false);
        queryPane.setSelected(false);
        queryPane.setEnabled(false);

        headerStatusButton.setVisible(startViewValue);
        addRow.setVisible(startViewValue);
        removeRow.setVisible(startViewValue);
    }

    private void addComponentsToToolPanel() {

        toolPanel.add(Box.createHorizontalStrut(2));
        toolPanel.add(execute);
        toolPanel.add(clearQuery);
        toolPanel.add(Box.createHorizontalStrut(30));
        toolPanel.add(newSelection);
        toolPanel.add(addToSelection);
        toolPanel.add(removeFromSelection);
        toolPanel.add(selectFromSelection);
        toolPanel.add(Box.createHorizontalStrut(30));
        toolPanel.add(queryPane);
        toolPanel.add(Box.createHorizontalStrut(30));
        toolPanel.add(addRow);
        toolPanel.add(removeRow);
        queryPane.validate();
        queryPane.doLayout();
        toolPanel.add(headerStatusButton);
        toolPanel.add(Box.createGlue());
        toolPanel.setAlignmentX(RIGHT_ALIGNMENT);
        toolPanel.validate();
        toolPanel.doLayout();
    }

    public void updateUI() {
        if (emptyDB != null && emptyDB.getParent() == null)
            SwingUtilities.updateComponentTreeUI(emptyDB);
        if (emptyPanel != null && emptyPanel.getParent() == null)
            SwingUtilities.updateComponentTreeUI(emptyPanel);
        if (mainPanel != null && mainPanel.getParent() == null)
            SwingUtilities.updateComponentTreeUI(mainPanel);
        if (newSelection != null && newSelection.getParent() == null)
            newSelection.updateUI();
        if (addToSelection != null && addToSelection.getParent() == null)
            addToSelection.updateUI();
        if (removeFromSelection != null && removeFromSelection.getParent() == null)
            removeFromSelection.updateUI();
        if (selectFromSelection != null && selectFromSelection.getParent() == null)
            selectFromSelection.updateUI();
        if (queryPane != null && queryPane.getParent() == null)
            queryPane.updateUI();
        if (headerStatusButton != null && headerStatusButton.getParent() == null)
            headerStatusButton.updateUI();
        if (removeRow != null && removeRow.getParent() == null)
            removeRow.updateUI();
        if (addRow != null && addRow.getParent() == null)
            addRow.updateUI();
        if (execute != null && execute.getParent() == null)
            execute.updateUI();
        if (clearQuery != null && clearQuery.getParent() == null)
            clearQuery.updateUI();
        if (textView != null && textView.getParent() == null)
            SwingUtilities.updateComponentTreeUI(textView);
        if (textView != null && textView.getParent() != null) {
            for (int i=0; i<queryViewStructure.size(); i++)
                SwingUtilities.updateComponentTreeUI(queryViewStructure.getQTablePanel(i));
        }
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
        PerformanceTimerGroup constructorGlobalGroup =
                pm.getPerformanceTimerGroupByID(PerformanceManager.CONSTRUCTOR);
        PerformanceTimerGroup eslateAspectGlobalGroup =
                pm.getPerformanceTimerGroupByID(PerformanceManager.INIT_ESLATE_ASPECT);
        PerformanceTimerGroup saveGlobalGroup =
                pm.getPerformanceTimerGroupByID(PerformanceManager.SAVE_STATE);
        PerformanceTimerGroup loadGlobalGroup =
                pm.getPerformanceTimerGroupByID(PerformanceManager.LOAD_STATE);

        // Special case: E-Slate aspect initialization timer. This timer is
        // created individually, because it has to be added to the default global
        // group INIT_ESLATE_ASPECT first and then to the performance timer group
        // of the component. It is the only way to get a measurement by this
        // timer: if the timer is created as a child of the performance timer
        // group of the component, then the component's E-Slate handle (and
        // therefore its E-Slate aspect) will have been created before the timer,
        // making it impossible to measure the time required for the
        // initialization of the component's E-Slate aspect using this timer.
        initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                eslateAspectGlobalGroup, infoBundle.getString("InitESlateAspectTimer"), true
        );

        // Get the performance timer group for this component.
        PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(getESlateHandle());
        // Add the "initESlateAspectTimer" to the component's performance timer
        // group.
        pm.addPerformanceTimerGroup(compoTimerGroup, initESlateAspectTimer);

        // If the component's timers have not been constructed yet, then
        // construct them. During constuction, the timers are also attached.
        constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, infoBundle.getString("ConstructorTimer"), true
        );
        loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, infoBundle.getString("LoadTimer"), true
        );
        saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, infoBundle.getString("SaveTimer"), true
        );
        pm.addPerformanceTimerGroup(constructorGlobalGroup, constructorTimer);
        pm.addPerformanceTimerGroup(loadGlobalGroup, loadTimer);
        pm.addPerformanceTimerGroup(saveGlobalGroup, saveTimer);
    }


    public static void main(String args[]) {
        MainFrame frame = new MainFrame("", new Dimension(500, 330));
        frame.setForeground(Color.black);
        frame.setBackground(Color.lightGray);

        QueryComponent q = new QueryComponent();

        frame.removeAll();
        frame.setLayout(new BorderLayout());
        frame.add("Center", q);
//	    frame.setTitle(f.db.getTitle());
        frame.pack();
        // q.init();
//	    frame.setSize(500, 300);
        frame.setVisible(true);
    }
}

/* Thic class stores all the plugs for the results of the queries. For each
* table in the DBase of the QueryComponent, a plug, through which the results
* of the queries on the Table are exported, is created. Along with all the
* query result Table export plugs, this class also stores the source tables
* for which the results are exported, i.e. the tables on which the queries
* are performed.
*/
class QueryResultTablePlugs {
    private QueryComponent query;
    private ArrayList plugs = new ArrayList();
    private ArrayList tables = new ArrayList();

    public QueryResultTablePlugs(QueryComponent query) {
        this.query = query;
    }

    public void addPlug(Table table, Plug plug) {
        plugs.add(plug);
        tables.add(table);
    }

    public void removePlug(Plug plug) {
        int index = plugs.indexOf(plug);
        if (index != -1) {
            plugs.remove(index);
            tables.remove(index);
        }
    }

    public void removePlug(Table sourceTable) {
        int index = tables.indexOf(sourceTable);
        if (index != -1) {
            plugs.remove(index);
            tables.remove(index);
        }
    }

    public Plug getPlug(int index) {
        if (index >=0 && index <plugs.size())
            return (Plug) plugs.get(index);
        return null;
    }

    public Plug getPlug(String sourceTableName) {
        for (int i=0; i<tables.size(); i++) {
            if ( ((Table) tables.get(i)).getTitle().equals(sourceTableName))
                return (Plug) plugs.get(i);
        }
        return null;
    }

    public Plug getPlug(Table sourceTable) {
        int index = tables.indexOf(sourceTable);
        if (index != -1)
            return (Plug) plugs.get(index);
        return null;
    }

    public boolean isPlugConnected(String sourceTableName) {
        Plug plug = getPlug(sourceTableName);
        if (plug == null)
            return false;
        return isPlugConnected(plug);
    }

    public boolean isPlugConnected(Plug plug) {
        return (plug.getDependents().length != 0);
    }

    public void clearPlugs() {
        plugs.clear();
    }

    public int getPlugCount() {
        return plugs.size();
    }

    public Table getTable(Plug plug) {
        int index = plugs.indexOf(plug);
        if (index != -1)
            return (Table) tables.get(index);
        return null;
    }
}






