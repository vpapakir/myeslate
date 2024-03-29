package gr.cti.eslate.database.engine;

//Debugging
import java.io.PrintStream;


import gr.cti.eslate.database.engine.event.*;
import gr.cti.eslate.tableModel.event.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.database.view.DBView;
import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.sharedObject.*;
import gr.cti.eslate.database.engine.plugs.TablePlug;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.StringBaseArray;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Component;
import java.awt.Container;
import java.awt.Color;

import com.objectspace.jgl.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.awt.Image;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.HashMap;
import java.io.Externalizable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.beans.PropertyVetoException;


/**
 * @version 2.0, May 01
 */
public class DBase implements Externalizable, ESlatePart {
static long attachTimersTime = 0;
    /** The version of the storage format of the DBase class
     */
//    public static final String STR_FORMAT_VERSION = "2.0";
    // 3 --> 4 : databaseTables was converted to ArrayList from Array.
    // 4 --> 5 : the children Tables are saved using the saveChildObjects() instead of simply saving their ArrayList
    public static final int FORMAT_VERSION = 5;
    /** Do not use. It'll be removed.
     */
//    public static ESlateHandle microworldForReadExternal = null;

    /** The title of the DBase.
     */
    protected String title = null;
    /** The file which stores the DBase.
     */
    protected File dbfile = null;
    /** The Array of Tables contained in the DBase.
     */
    private ArrayList databaseTables = new ArrayList();
    /** The active (working) Table.
     */
    protected transient Table activeTable;
    /** The active (working) Table index.
     */
    protected transient int activeTableIndex = -1;
    /** The Array of the files of the Tables contained in the DBase.
     */
//    protected Array CTableFiles = new Array();
    /** The valid BinaryPredicates for each of the supported data types.
     */
///    protected static HashMap operators = new HashMap();
    /** Special purpose BinaryPredicates for the operations <i>join</i>, <i>th-join</i>
     *  and <i>intersection</i>.
     */
///    protected static HashMap dataTypeComparators = new HashMap();

    /** Database-wide date format for processing of date values.
     */
    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy:HH");
    /** Database-wide time format for processing of time values.
     */
    private String metadata = new String();

    static SimpleDateFormat stf = new SimpleDateFormat("H:mm:ss");
    static final Integer INTEGER_FOR_NULL_VALUE = new Integer(IntegerTableField.NULL); //1 new Integer(-2101237214);
    static final String  BOOLEAN_FOR_NULL_VALUE = new String("-9701237214");
    static final String  STRING_FOR_NULL_VALUE = new String("-9701237214");
    static final Double  DOUBLE_FOR_NULL_VALUE = new Double(DoubleTableField.NULL); //1 new Double("-9701237214");
    static final Float   FLOAT_FOR_NULL_VALUE = new Float(FloatTableField.NULL);
    static final String  URL_FOR_NULL_VALUE = new String("http://-9701237214");
    static final Date    DATE_FOR_NULL_VALUE =  sdf.parse("1/1/0:11", new ParsePosition(0));
    static final Date    TIME_FOR_NULL_VALUE =  stf.parse("00:00:25", new ParsePosition(0));
    static final CImageIcon  IMAGE_FOR_NULL_VALUE = new CImageIcon(); //has to be assigned a default image

    public ESlateFileDialog fileDialog = null; //new FileDialog(new JFrame());
    protected transient boolean isModified = false;
    private transient boolean newDatabase = false;

    protected transient ArrayList databaseListeners = new ArrayList();
    protected transient ArrayList databasePropertyChangeListeners = new ArrayList();
    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    protected VetoableChangeListener tableVetoListener = null;

    static final long serialVersionUID = 12;

    /** This Array stores database-specific attributes, which are not used by the database engine, but
     *  by the database UI. So it's up to the UI builder to store any attributes in this Array, which
     *  need to persist from session to session.
     */
    public Array UIProperties = new Array();
    protected static ResourceBundle resources = ResourceBundle.getBundle("gr.cti.eslate.database.engine.DBResourceBundle", Locale.getDefault());


    private String databaseCreatorName = null;
    private String databaseCreatorSurname = null;
    private CDate creationDate = null;
    private CDate lastModified = null;
    private boolean _protected = false;
    private String _password = null;
    private boolean locked = false;
    private boolean tableRemovalAllowed = true;
    private boolean tableAdditionAllowed = true;
    private boolean tableExportationAllowed = true;
    private boolean hiddenTablesDisplayed = false;
    /** Contains all the UI settings of the DBase. This class gives the DBase document the
     *  chance to 'carry' the settings which adjust its view in the Database component.
     */
    DBView dbView = null;
    ESlateHandle handle = null;
//    SharedObjectPlug databasePin, tableImportPin;
    MaleSingleIFMultipleConnectionProtocolPlug dbasePlug;
    FemaleSingleIFMultipleConnectionProtocolPlug tableImportPlug;
//    DatabaseSO databaseSO = null;
    /* This listener is added to the ESlateHandle of each Table in the DBase. This
     * listener enables a Table handle to be re-parented to this DBase, when it is
     * removed from the its current parent-handle.
     */
/*    public ESlateListener tableReparentListener = new ESlateAdapter() {
        public void parentChanged(ParentChangedEvent e) {
*/            /* The Table handle */
//            ESlateHandle h = (ESlateHandle) e.getSource();
            /* The previous handle parent */
//            ESlateHandle oldParent = e.getOldParent();
            /* Re-parent to the DBase handle the Table handle whose parent has
             * changed under the following conditions:
             * 1. The Table handle has no parent
             * 2. The previous parent of the Table handle is not this DBase, i.e.
             *    the ParentChangedEvent was not fired when the Table was removed from
             *    this DBase.
             * 3. The handle of the DBase is not being disposed. We check for it
             *    using the 'handle.getComponentName() != null' check.
             */
/*            if (h.getParentHandle() == null &&
                oldParent != handle &&
                handle.getComponentName() != null) {
                System.out.println("DBase tableReparentListener h: " + h + ", to handle: "+ handle);
                handle.add(h);
//                try{
//                    h.getPins()[0].disconnectPin(tableImportPin);
//                }catch (PinNotConnectedException exc) {
//                    exc.printStackTrace();
//                }
            }
        }
    };
*/
    /* Flag which prevents the 'removeTable()' action of the 'tableImportPin'
     * disconnection listener, when the disconnection is the result of the
     * table removal itself.
     */
    boolean tableBeingRemoved = false;
    /** Timers which measure functions of the DBase */
    PerformanceTimer loadTimer, saveTimer, constructorTimer, initESlateAspectTimer, fieldMapTimer;
    /* The listener that notifies about changes to the state of the PerformanceManager */
    PerformanceListener perfListener = null;


    public void setDatabaseCreatorName(String name) {
        if (databaseCreatorName == null || databaseCreatorName.length() == 0) {
            databaseCreatorName = name;
            isModified = true;
        }
    }


    public String getDatabaseCreatorName() {
        return databaseCreatorName;
    }


    public void setDatabaseCreatorSurname(String surname) {
        if (databaseCreatorSurname == null || databaseCreatorSurname.length() == 0) {
            databaseCreatorSurname = surname;
            isModified = true;
        }
    }


    public String getDatabaseCreatorSurname() {
        return databaseCreatorSurname;
    }


    public void setCreationDate(CDate date) {
        if (creationDate == null)
            creationDate = date;
    }


    public CDate getCreationDate() {
        return creationDate;
    }


    public void setLastModified(CDate date) {
        CDate tmp = lastModified;
        lastModified = date;
        propertyChangeSupport.firePropertyChange("Last modified", tmp, lastModified);
    }


    public CDate getLastModified() {
        return lastModified;
    }


    public boolean isProtected() {
        return _protected;
    }


    public void protect(String password) {
        if (password != null && password.length() > 0) {
            _password = password;
            _protected = true;
            locked = true;
        }
    }


    public void unlock(String password) {
        if (locked == false || _password == null || password == null || password.length() == 0)
            return;
        if (_password.equals(password))
            locked = false;
    }


    public boolean checkPassword(String password) {
        if (_password == null || _password.length()==0 || password==null)
            return false;
        return (_password.equals(password));
    }


    public boolean isLocked() {
        return locked;
    }


    public boolean isTableRemovalAllowed() {
        return tableRemovalAllowed;
    }


    /**
     *  Enables or disables the table removal from the database. Triggers a PropertyChangeEvent for the
     *  bound property <i>tableRemovalAllowed</i>. The name of the property is
     *  "Table removal allowed".
     *  @param allowed
     */
    public void setTableRemovalAllowed(boolean allowed) {
        if (!locked && tableRemovalAllowed != allowed) {
            tableRemovalAllowed = allowed;
            isModified = true;
            propertyChangeSupport.firePropertyChange("Table removal allowed", new Boolean(!allowed), new Boolean(allowed));
        }
    }


    public boolean isTableAdditionAllowed() {
        return tableAdditionAllowed;
    }


    /**
     *  Enables or disables the addition of tables in the database. Triggers a PropertyChangeEvent for the
     *  bound property <i>tableAdditionAllowed</i>. The name of the property is
     *  "Table addition allowed".
     *  @param allowed
     */
    public void setTableAdditionAllowed(boolean allowed) {
        if (!locked && tableAdditionAllowed != allowed) {
            tableAdditionAllowed = allowed;
            isModified = true;
            propertyChangeSupport.firePropertyChange("Table addition allowed", new Boolean(!allowed), new Boolean(allowed));
        }
    }


    public boolean isTableExportationAllowed() {
        return tableExportationAllowed;
    }


    /**
     *  Enables or disables the exportation of tables from the database. Triggers a PropertyChangeEvent for the
     *  bound property <i>tableExportationAllowed</i>. The name of the property is
     *  "Table exportation allowed".
     *  @param allowed
     */
    public void setTableExportationAllowed(boolean allowed) {
        if (!locked && tableExportationAllowed != allowed) {
            tableExportationAllowed = allowed;
            isModified = true;
            propertyChangeSupport.firePropertyChange("Table exportation allowed", new Boolean(!allowed), new Boolean(allowed));
        }
    }


    public boolean isHiddenTablesDisplayed() {
        return hiddenTablesDisplayed;
    }


    /**
     *  Enables or disables the display of hidden tables. Triggers a PropertyChangeEvent for the
     *  bound property <i>hiddenTablesDisplayed</i>. The name of the property is
     *  "Hidden tables displayed".
     *  @param displayed
     */
    public void setHiddenTablesDisplayed(boolean displayed) {
        if (!locked && hiddenTablesDisplayed != displayed) {
            hiddenTablesDisplayed = displayed;
            isModified = true;
            propertyChangeSupport.firePropertyChange("Hidden tables displayed", new Boolean(!displayed), new Boolean(displayed));
        }
    }


    /** Stores descriptive information about the database.
     */
    public void setMetadata(String metadata) {
        if (metadata == null && this.metadata != null) {
            this.metadata = "";
            setModified();
        }
        if (metadata == this.metadata)
            return;
        if (metadata != null && this.metadata != null && !metadata.equals(this.metadata)) {
            this.metadata = metadata;
            setModified();
        }
    }


    public String getMetadata() {
        return metadata;
    }


    public synchronized void addPropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }


    public synchronized void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }


    public synchronized void addDatabaseListener(DatabaseListener dl) {
        if (databaseListeners.indexOf(dl) == -1)
            databaseListeners.add(dl);
//        System.out.println("databaseListeners: " + databaseListeners);
    }


    /**
     * @deprecated  As of DBEngine version 1.9, replaced by {@link #addDatabaseListener(gr.cti.eslate.database.engine.event.DatabaseListener)}
     */
    public synchronized void addDatabaseListener(gr.cti.eslate.event.DatabaseListener dl) {
        System.out.println("This microworld contains a gr.cti.eslate.event.DatabaseListener attached to the Database component.\n" +
                            "This listener type is now obsolete, so the listener will not be added. As a result \n" +
                            "the microworld will loose some functionality. If you want this functionality, it is suggested, that \n" +
                            "you load this microworld with E-Slate version 1.2 or 1.3, save the listener's action to \n" +
                            "a file, dettach the listener, then open the microworld with a higher version of E-Slate and \n" +
                            "reattach the listener.");
    }

    public synchronized void removeDatabaseListener(DatabaseListener dl) {
//        for (int i=0; i<databaseListeners.size(); i++)
//            System.out.println(i + ": " + databaseListeners.get(i));
        int index = databaseListeners.indexOf(dl);
        if (index != -1)
            databaseListeners.remove(index);
//        System.out.println("databaseListeners: " + databaseListeners);
    }


    protected void fireActiveTableChanged(ActiveTableChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseListener) dl.get(i)).activeTableChanged(e);
    }

    protected void fireTableAdded(TableAddedEvent e) {
        ArrayList dl;
//        System.out.println("Here231");
        synchronized(this) {dl = (ArrayList) databaseListeners.clone();}
//        System.out.println("Here232");
        for (int i=0; i<dl.size(); i++)
            ((DatabaseListener) dl.get(i)).tableAdded(e);
//        System.out.println("Here233");
    }

    protected void fireTableRemoved(TableRemovedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseListener) dl.get(i)).tableRemoved(e);
    }

    protected void fireTableReplaced(TableReplacedEvent e) {
        ArrayList dl;
//        System.out.println("Here231");
        synchronized(this) {dl = (ArrayList) databaseListeners.clone();}
//        System.out.println("Here232");
        for (int i=0; i<dl.size(); i++)
            ((DatabaseListener) dl.get(i)).tableReplaced(e);
//        System.out.println("Here233");
    }

    protected void fireDatabaseRenamed(DatabaseRenamedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseListener) dl.get(i)).databaseRenamed(e);
    }


    /**
     *  Activates the table at <i>index</i>.
     *  @param index The index of the table to be activated.
     *  @return <i>true</i>, if the table is activated. <i>false</i>, if an invalid index
     *          is specified
     */
    public boolean activateTable(int index, boolean fireEvent) {
        int previousActiveTableIndex = -1;
        if (activeTable != null && databaseTables != null)
            previousActiveTableIndex = databaseTables.indexOf(activeTable);

        if (index < databaseTables.size() && index >= 0) {
            activeTable = (Table) databaseTables.get(index);
            CDate.dateFormat = activeTable.dateFormat;
            CTime.timeFormat = activeTable.timeFormat;
            newDatabase = false;
            if (fireEvent)
                fireActiveTableChanged(new ActiveTableChangedEvent(this, previousActiveTableIndex, index));
//            setModified();
            activeTableIndex = index;
//            System.out.println("DBase Activated table at: " + activeTableIndex);
            return true;
        }else{
            return false;
        }
    }


    /** Activates the first non-hidden table of the database. If no
     *  such table exists the active table does not change.
     *  @return The index of the first non-hidden table, or -1
     *  if no table exists in the database or all the tables are
     *  hidden.
     */
    public int activateFirstNonHiddenTable() {
        int previousActiveTableIndex = -1;
        if (activeTable != null && databaseTables != null)
            previousActiveTableIndex = databaseTables.indexOf(activeTable);

        int index = -1;
        for (int i=0; i<getTableCount(); i++) {
            if (!((Table) databaseTables.get(i)).isHidden()) {
                index = i;
                break;
            }
        }

        if (index == -1)
            return index;

        activeTable = (Table) databaseTables.get(index);
        CDate.dateFormat = activeTable.dateFormat;
        CTime.timeFormat = activeTable.timeFormat;
        newDatabase = false;

        fireActiveTableChanged(new ActiveTableChangedEvent(this, previousActiveTableIndex, index));
//            setModified();
        activeTableIndex = index;
        return index;
    }


    /** Returns the index of the visible table to which <i>tableIndex</i>
     *  corresponds. An application which uses the DBase API and does
     *  not display hidden tables can use this method to translate the index
     *  of the active Table in the database to the index of the visible table
     *  in the application itself.
     *  @return The index of the visible table, or -1 if <i>tableIndex</i>
     *  corresponds to a hidden table, or -2 if <i>tableIndex</i> is invalid,
     *  i.e. less than 0 or greater than the number of Tables in the DBase.
     */

    public int toVisibleTableIndex(int tableIndex) {
//        System.out.println("DBase tableIndex: " + tableIndex);
        if (tableIndex < 0 || tableIndex >= getTableCount())
            return -2;

        if (hiddenTablesDisplayed)
            return tableIndex;

//        System.out.println("getTableAt(tableIndex).isHidden(): " + getTableAt(tableIndex).isHidden());
        if (getTableAt(tableIndex).isHidden())
            return -1;

        int visibleTableIndex = tableIndex;
        for (int i=0; i<tableIndex; i++) {
//            System.out.println("i: " + i + ", getTableAt(i).isHidden(): " + getTableAt(i).isHidden());
            if (getTableAt(i).isHidden())
                visibleTableIndex--;
        }
        return visibleTableIndex;
    }


    /** Translates the <i>tableIndex</i> to the real index of Table in
     *  the DBase. This method is useful to applications which do not
     *  display the hidden tables and provides the actual Table index for
     *  the visible Table at <i>tableIndex</i>.
     *  @return The actual index in the DBase of the visible table at
     *  <i>tableIndex</i>, or -1 if <i>tableIndex</i> is invalid.
     */
    public int toTableIndex(int tableIndex) {
        if (tableIndex < 0)
            return -1;
        if (hiddenTablesDisplayed)
            return tableIndex;

        /* Find the index of the proper Table in the DBase to be
         * activated. Avoid all the hidden tables.
         */
        int TableIndex = tableIndex;
        int numOfHiddenPriorToIndex = 0;
        for (int i=0; i<getTableCount(); i++) {
            if (getTableAt(i).isHidden())
                numOfHiddenPriorToIndex++;
            else
                TableIndex--;
            if (TableIndex < 0)
                break;
        }
/*        while (TableIndex >= 0) {
            if (((Table) Tables.get(TableIndex)).isHidden())
                numOdHiddenPriorToIndex++;
            else
                TableIndex--;
        }
*/
        TableIndex = tableIndex + numOfHiddenPriorToIndex;

        /* The Table at TableIndex may be hidden, so move to
         * the next visible table after TableIndex.
         */
/*        while (true) {
            if (TableIndex < getTableCount() && getTableAt(TableIndex).isHidden())
                TableIndex++;
            else
                break;
        }
*/
        if (!(TableIndex < getTableCount())) {
//            System.out.println("real index: " + TableIndex);
            return -1;
        }

//        System.out.println("real index: " + TableIndex);
        return TableIndex;
    }


    private final boolean _activateTable(int index) {
        int previousActiveTableIndex = -1;
        if (activeTable != null && databaseTables != null)
            previousActiveTableIndex = databaseTables.indexOf(activeTable);

        if (index < databaseTables.size() && index >= 0) {
            activeTable = (Table) databaseTables.get(index);
            CDate.dateFormat = activeTable.dateFormat;
            CTime.timeFormat = activeTable.timeFormat;
            newDatabase = false;
//            System.out.println("Firing ActiveTableChangedEvent event");
            fireActiveTableChanged(new ActiveTableChangedEvent(this, previousActiveTableIndex, index));
//            setModified();
            activeTableIndex = index;
            return true;
        }else{
            return false;
        }
    }


    /** Returns the active (working) table of the DBase.
     */
    public Table getActiveTable() {
        return activeTable;
    }

    /** Returns the index of the active (working) table of the DBase.
     */
    public int getActiveTableIndex() {
        return activeTableIndex;
    }


    /** Returns the DBase's title.
     */
    public String getTitle() {
        return title;
    }

    private final String _getTitle() {
        return title;
    }

    /** Sets the DBase's title.
     */
    public void setTitle(String title) {
        String oldTitle = _getTitle();
        setModified();
        newDatabase = false;
        this.title = title;
        fireDatabaseRenamed(new DatabaseRenamedEvent(this, oldTitle, title));
        setHandleNameToDBaseTitle(true);
    }

    void setHandleNameToDBaseTitle(boolean fireDBaseRenamedEvent) {
        if (handle != null) {
            if (!handle.getComponentName().equals(title)) {
                try{
//                    handle.setUniqueComponentName(title);
                    handle.setComponentName(title);
                }catch (RenamingForbiddenException exc) {
                    return;
                }catch (NameUsedException exc) {}
            }
            /* The DBase title and its handle's name have to be the same.
             * 'setUniqueComponentName()' may have set a slighlty different title
             * from the one requested. If this is the case, then we set againg the
             * title of the DBase.
             */
            if (!handle.getComponentName().equals(title)) {
                if (fireDBaseRenamedEvent)
                    setTitle(handle.getComponentName());
                else
                    _setTitle(handle.getComponentName());
            }
        }
    }

    private final void _setTitle(String t) {
        setModified();
        newDatabase = false;
        title = t;
        setHandleNameToDBaseTitle(false);
    }


    /** Returns the number of Tables contained in the DBase.
     */
    public int getTableCount() {
        return databaseTables.size();
    }


    /** Returns the number of hidden Tables contained in the DBase.
     */
    public int getHiddenTableCount() {
        int hiddenCount = 0;
        for (int i=0; i<databaseTables.size(); i++) {
            if (((Table) databaseTables.get(i)).isHidden())
                hiddenCount++;
        }
        return hiddenCount;
    }


    /** Sets the <i>isModified</i> flag of the database.
     */
     public void setModified() {
        isModified = true;
     }

     private void resetModifiedFlag() {
        isModified = false;
        for (int i=0; i<databaseTables.size(); i++)
            ((Table) databaseTables.get(i)).isModified = false;
     }

    /** Returns the Table with the given <i>title</i>. Returns <i>null</i>, if no Table
     *  with this title exists in the DBase.
     */
    public Table getTable(String title) {
        for (int i=0; i<databaseTables.size(); i++) {
            if (((Table) databaseTables.get(i)).getTitle().equals(title))
                return ((Table) databaseTables.get(i));
        }
        return null;
    }


    /** Returns the Table at <i>index</i> in the DBase. Returns <i>null</i>, if
     *  an invalid index is supplied.
     */
    public Table getTableAt(int index) {
        if (index < 0 || index >= databaseTables.size())
            return null;
        return ((Table) databaseTables.get(index));
    }


    /** Returns the index of a Table in the DBase. If <i>Table</i> is null,
     *  an IllegalArgumentException is thrown. If the table does not exist in the
     *  DBase -1 is returned. Otherwise the index of the table is returned. This
     *  is the real Table index and not the visibleIndex, which takes into account
     *  hidden tables.
     */
    public int indexOf(Table table) {
        if (table == null)
            throw new IllegalArgumentException("Null Table supplied to indexOf");

//table       if (!table.database.equals(this))
//table            return -1;
        int counter = 0;
        int numOfTablesInDatabase = getTableCount();
        while (counter<numOfTablesInDatabase) {
            if (table.equals(getTableAt(counter)))
                return counter;
            counter++;
        }
        return -1;
    }

    /** Returns the index of a Table in the DBase. If <i>title</i> is null,
     *  an IllegalArgumentException is thrown. If the table does not exist in the
     *  DBase -1 is returned. Otherwise the index of the table is returned. This
     *  is the real Table index and not the visibleIndex, which takes into account
     *  hidden tables.
     *  @param title The title of the table whose index is requested.
     *  @return The Table's index or -1, if there is no Table with this title in the DBase.
     */
    public int indexOf(String title) {
        if (title == null)
            throw new IllegalArgumentException("Null title supplied to indexOf");

//table       if (!table.database.equals(this))
//table            return -1;
        int counter = 0;
        int numOfTablesInDatabase = getTableCount();
        while (counter<numOfTablesInDatabase) {
            if (title.equals(getTableAt(counter).getTitle()))
                return counter;
            counter++;
        }
        return -1;
    }

    /** Returns an Array with the titles of the Tables in the DBase.
     */
    public ArrayList getTableTitles() {
        ArrayList titles = new ArrayList();
        String title;
        for (int i=0; i<databaseTables.size(); i++) {
            title = ((Table) databaseTables.get(i)).getTitle();
            if (title != null && title.trim().length() != 0)
                titles.add(title);
        }

        return titles;
    }


    /** Reports if this DBase is a new one, i.e. a DBase object which has just
     *  been instantiated, contains no Tables and has not been saved.
     */
    public boolean isNewDatabase() {
        return newDatabase;
    }


    /** Reports if this database has been modified during its lifetime.
     */
    public final boolean isModified() {
        if (isModified)
            return true;
        else{
            for (int k = 0; k<databaseTables.size(); k++) {
                if (((Table) databaseTables.get(k)).isModified)
                    return true;
            }
        }
        return false;
//        return isModified;
    }


    public final File getDBFile() {
        return dbfile;
    }


    /** Sets the <i>dbFile</i> variable, where the DBase is stored. This method
     *  assumes that the <i>file</i> argument is valid.
     */
    public final void setDBFile(File file) {
        dbfile = file;
    }


//    public final Array getTableFiles() {
//        return TableFiles;
//    }


    public final ArrayList getTables() {
        return databaseTables;
    }


    /** Returns the valid comparators for the given Java data type. Returns <i>null</i>, if
     *  an invalid <i>dataType</i> is supplied.
     */
    public HashMap getComparatorsForDataType(Class dataType) {
        return AbstractTableField.getComparatorsForFieldType(AbstractTableField.getFieldClassForDataType(dataType));
/*        if (operators.find(dataType) == operators.end())
            return null;

        ArrayList comparators = new ArrayList();
        HashMap h = (HashMap) operators.get(dataType);
        Enumeration enum = h.keys();
        while (enum.hasMoreElements())
            comparators.add(enum.nextElement());
        return comparators;
*/
    }


    /** Opens a DBase from a URL. This method restores all the Tables in
     *  the DBase, too. The first table in the database becomes the active one.
     *  @param url The url of the DBase.
     *  @param container The frame for which the cursor will be set its default wait state, during
     *  the lengthy Table recover operations. If <i>null</i> is supplied, then this method will not
     *  provide any status information through the system's cursor.
     *  opened. To do this, the Array of the already open databases must be supplied to this method. This
     *  parameter may be <i>null</i>, in which case no check is made. Also, if this Array contains anything
     *  but DBase objects, the check will silently skip these objects.
     *  @return The restored database or <i>null</i>, if for any reason the DBase could not be restored.
     */
    public static DBase openDBase(URL url, Component container) {
        java.awt.Cursor defaultCursor = java.awt.Cursor.getDefaultCursor();
        java.awt.Cursor waitCursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR);
        JFrame messageFrame = new JFrame();
//        ResourceBundle resources = ResourceBundle.getBundle("gr.cti.eslate.database.engine.DBResourceBundle", Locale.getDefault());

        /* Read the database file.
         */
        DBase newDatabase = null;
        ObjectInputStream in;
        try{
//          System.out.println("dbFile: " + dbFile + " getPath(): " + dbFile.getPath());
            in = new ObjectInputStream(new BufferedInputStream(url.openStream(), 20000));
            Object d = in.readObject();
            if (CDatabase.class.isAssignableFrom(d.getClass()))
                newDatabase = new DBase((CDatabase) d);
            else
                newDatabase = (DBase) d;
        }catch (Exception e1) {
//          System.out.println(e1.getClass().getName() + ", " + e1.getMessage());
            e1.printStackTrace();
            if (container != null) {
                container.setCursor(defaultCursor);
                ESlateOptionPane.showMessageDialog(container, "1" + resources.getString("CDatabaseMsg1") + url + "\"", resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }else
                ESlateOptionPane.showMessageDialog(messageFrame, "2" + resources.getString("CDatabaseMsg1") + url + "\"", resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
         }

/*        String urlDir = url.toString();
        if (urlDir.lastIndexOf('/') != -1)
            urlDir = urlDir.substring(0, urlDir.lastIndexOf('/') + 1);

//        System.out.println("urlDir: " + urlDir);

        if (newDatabase.getTables() == null || newDatabase.getTables().size() == 0) {
        }else{
*/
            /* Restore each Table of the database.
             */
/*            Table ct = null;
            Array ctableFiles = newDatabase.getTableFiles();
//                    System.out.println("ctableFiles: " + ctableFiles);
            for (int i=0; i<ctableFiles.size(); i++) {
                System.out.println("----------FILE--------: " + ctableFiles.get(i));
*/
                /* The Table file will be read from the same URL as the database. We replace the
                 * last portion of the url with the name of the file, after we extract the directory
                 * name from it.
                 */
/*                String name = ((File) ctableFiles.get(i)).toString();
                String fileSeparator = System.getProperty("file.separator");
                int lastSeparator;
                if ((lastSeparator = name.lastIndexOf(fileSeparator)) != -1)
                    name = name.substring(lastSeparator+1);

                name = urlDir + name;
                URL u;
                try{
                    u = new URL(name);
                }catch (java.net.MalformedURLException e) {
                    System.out.println("MalformedException");
                    return null;
                }

                System.out.println("u: " + u);

                try{
                    if (container != null)
                        container.setCursor(waitCursor);
                    in = new ObjectInputStream(u.openStream());
                    ct = (Table) in.readObject();
                }catch (Exception e1) {
                    if (container != null) {
                        container.setCursor(defaultCursor);
                        ESlateOptionPane.showMessageDialog(container, resources.getString("CDatabaseMsg2") + ((File) ctableFiles.get(i)).getPath() + resources.getString("CDatabaseMsg3"), resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    }else
                        ESlateOptionPane.showMessageDialog(messageFrame, resources.getString("CDatabaseMsg2") + ((File) ctableFiles.get(i)).getPath() + resources.getString("CDatabaseMsg3"), resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    ctableFiles.remove(i);
                    i--;
                    newDatabase.setModified();
                    continue;
                 }
//                        System.out.println("Here2.8");
//                try{
//                    newDatabase.restoreTable(ct);
//                }catch (TableNotBelongsToDatabaseException e1) {System.out.println("Serious inconsistency error in CDatabase openCDatabase:  (4)"); continue;}
//                        System.out.println("Here2.85");
                newDatabase.activateTable(i, true);
//                        System.out.println("Here2.9");
//                        System.out.println(db.getActiveTable());

                if (container != null)
                    container.setCursor(defaultCursor);
            }

        }
        if (container != null)
            container.setCursor(defaultCursor);

        return newDatabase;
*/
//table        for (int i=0; i<newDatabase.databaseTables.size(); i++) {
//table            ((Table) newDatabase.databaseTables.get(i)).database = newDatabase;
//            newDatabase.fireTableAdded(new TableAddedEvent(newDatabase));
//table        }

        if (!(newDatabase.databaseTables.size() == 0))
            newDatabase._activateTable(0);

        if (container != null)
            container.setCursor(defaultCursor);

        return newDatabase;
   }


    /** Opens a DBase from the file it is serialized in. This method restores all the Tables in
     *  the DBase, too. The first table in the database becomes the active one.
     *  @param fileName The full path to the DBase file.
     *  @param container The frame for which the cursor will be set its default wait state, during
     *  the lengthy Table recover operations. If <i>null</i> is supplied, then this method will not
     *  provide any status information through the system's cursor.
     *  @param openDatabases <i>openDBase</i> can prohibit the user from opening a DBase which is already
     *  opened. To do this, the Array of the already open databases must be supplied to this method. This
     *  parameter may be <i>null</i>, in which case no check is made. Also, if this Array contains anything
     *  but DBase objects, the check will silently skip these objects.
     *  @return The restored database or <i>null</i>, if for any reason the DBase could not be restored.
     */
    public static DBase openDBase(ESlateHandle nextParent, String fileName, Component container, ArrayList openDatabases) {
        ESlateHandle prevStaticParent = ESlateHandle.nextParent;
        ESlateHandle.nextParent = nextParent;
        java.awt.Cursor defaultCursor = java.awt.Cursor.getDefaultCursor();
        java.awt.Cursor waitCursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR);
        JFrame messageFrame = new JFrame();
//        ResourceBundle resources = ResourceBundle.getBundle("gr.cti.eslate.database.engine.DBResourceBundle", Locale.getDefault());

//        System.out.println("Obf 4");
        String dbFileName = fileName;
        File dbFile = null;
        try{
            dbFile = DBase.analyzeFileName(dbFileName, "cdb");
//          System.out.println("dbFileName: " + dbFileName);
//          System.out.println("dbFile: " + dbFile);
        }catch (InvalidPathException e1) {
            if (container != null) {
                container.setCursor(defaultCursor);
                ESlateOptionPane.showMessageDialog(container, e1.message, resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }else
                ESlateOptionPane.showMessageDialog(messageFrame, e1.message, resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
            ESlateHandle.nextParent = prevStaticParent;
            return null;
         }

        /* Check if among the already open DBases in the client application, which uses this API,
         * there exists a DBase, which was deserialized from this specific file, we are about to
         * deserialize.
         */
        if (openDatabases != null) {
            DBase db;
            for (int i=0; i<openDatabases.size(); i++) {
                try{
                    db = (DBase) openDatabases.get(i);
                }catch (ClassCastException e) {continue;}
                if (db != null && dbFile.equals(db.getDBFile())) {
                    if (container != null) {
                        container.setCursor(defaultCursor);
                        ESlateOptionPane.showMessageDialog(container, resources.getString("CDatabaseMsg4"), resources.getString("Warning"), JOptionPane.WARNING_MESSAGE);
                    }else
                        ESlateOptionPane.showMessageDialog(messageFrame, resources.getString("CDatabaseMsg4"), resources.getString("Warning"), JOptionPane.WARNING_MESSAGE);
                    ESlateHandle.nextParent = prevStaticParent;
                    return null;
                }
            }
        }

        /* Read the database file.
         */
//        System.out.println("Obf 5");
        DBase newDatabase = null;
        ObjectInputStream in;
        try{
//            *** Database open time metrics ***                                                        // 26Kb  73Kb  384Kb (in millisecons)
//            in = new ObjectInputStream(new FileInputStream(dbFile));                                  // 915  2718   8396
//            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dbFile)));         //1291  1118   4825
//            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dbFile), 20000));  // 825  1503   4568
//            System.out.println("Here1. dbFile: " + dbFile);
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dbFile), 30000));    // 371  1375   3820
//            System.out.println("Here2. in: " + in);
//            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dbFile), 50000));  // 403  1438   4311
//            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dbFile), 100000)); // 266  1823   4255
//            long tm =  System. currentTimeMillis();
            Object d = in.readObject();
            if (CDatabase.class.isAssignableFrom(d.getClass()))
                newDatabase = new DBase((CDatabase) d);
            else
                newDatabase = (DBase) d;
//            System.out.println("Here3. newDatabase: " + newDatabase + ", table count: " + newDatabase.getTableCount());
            in = null;
//            System.out.println("Here4");
//            long tm2 = System. currentTimeMillis();
//            System.out.println("Before: " + tm + ", After: " + tm2 + ", ellapsed time: " + (tm2-tm));
//            java.lang.Runtime rt = java.lang.Runtime.getRuntime();
//            System.out.println("----- " + "Total mem: " + rt.totalMemory() + ", free mem: " + rt.freeMemory() + ", used mem: " + (rt.totalMemory() - rt.freeMemory()));
//            System.out.println("Obf 6");
        }catch (Exception e1) {
//            System.out.println("In openDBase: " + e1.getClass().getName() + ", " + e1.getMessage());
            e1.printStackTrace();
            if (container != null) {
                container.setCursor(defaultCursor);
                ESlateOptionPane.showMessageDialog(container, resources.getString("CDatabaseMsg1") + dbFile.getPath() + "\"", resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }else
                ESlateOptionPane.showMessageDialog(messageFrame, resources.getString("CDatabaseMsg1") + dbFile.getPath() + "\"", resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
//            System.out.println("Obf 7");
            ESlateHandle.nextParent = prevStaticParent;
            return null;
         }

        /* In case the "dbFile" variable of the database which was just opened is different
         * from the file from which the database was actually read -this means that the
         * database file has been move/renamed by external means- then sychronize this variable
         * with the new file.
         */
        if (newDatabase.getDBFile() == null || !newDatabase.getDBFile().equals(dbFile)) {
//            System.out.println("Obf 8");
            newDatabase.setModified();
            newDatabase.setDBFile(dbFile);
        }

        if (container != null)
            container.setCursor(defaultCursor);

        if (!(newDatabase.databaseTables.size() == 0))
            newDatabase._activateTable(0);

        ESlateHandle.nextParent = prevStaticParent;
        return newDatabase;
    }


//    private void writeObject(ObjectOutputStream out) throws IOException {
    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(saveTimer);

        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION); //, 15);
        fieldMap.put("Title", title);
        fieldMap.put("DBFile", dbfile);
        /* Save only the Tables that belong to this DBase. Don't save any tables
         * which have been imported and belong elsewhere. It is the responsibility
         * of each Table placeholder to save its own Tables.
         */
        ArrayList myTables = new ArrayList();
System.out.println("databaseTables.size(): " + databaseTables.size());
        for (int i=0; i<databaseTables.size(); i++) {
            Table t = (Table) databaseTables.get(i);
            if (t.handle == null || t.handle.getParentHandle() == handle)
                myTables.add(t);
        }
//        System.out.println("myTables.size(): " + myTables.size());
        // Create an ArrayList with the handles of the children Tables
        ArrayList childrenTableHandles = new ArrayList(myTables.size());
        for (int i=0; i<myTables.size(); i++)
            childrenTableHandles.add(((Table) myTables.get(i)).getESlateHandle());
System.out.println("childrenTableHandles.size(): " + childrenTableHandles.size());
        handle.saveChildObjects(fieldMap, "Children Tables", childrenTableHandles);
//        fieldMap.put("CTables", myTables);


//System.out.println("handle.toArray().length: " + databaseTables);
//System.out.println("handle.getESlateMicroworld(): " + handle.getESlateMicroworld());
//System.out.println("handle.toArray()[0].getESlateMicroworld(): " + handle.toArray()[0].getESlateMicroworld());
//        handle.saveChildren(out, fieldMap, "DatabaseTables", handle.toArray());
        fieldMap.put("UIProperties", UIProperties);
        fieldMap.put("Creator name", databaseCreatorName);
        fieldMap.put("Creator surname", databaseCreatorSurname);
        fieldMap.put("Creation date", creationDate);
        fieldMap.put("Last modified", lastModified);
        fieldMap.put("Locked", _protected);
        fieldMap.put("Password", _password);
        fieldMap.put("Table removal allowed", tableRemovalAllowed);
        fieldMap.put("Table addition allowed", tableAdditionAllowed);
        fieldMap.put("Table exportation allowed", tableExportationAllowed);
        fieldMap.put("Hidden tables displayed", hiddenTablesDisplayed);
        fieldMap.put("Metadata", metadata);
        fieldMap.put("DBView", dbView);

        out.writeObject(fieldMap);
//        myTables.clear();
//        myTables = null;
        resetModifiedFlag();

        pm.stop(saveTimer);
        // The save time counted includes the sum of the load times of the Tables of the DBase.
        // Substract the save times of the tables, so that the proper DBase save time is recorded.
        long totalTableSaveTime = 0;
        for (int i=0; i<databaseTables.size(); i++)
            totalTableSaveTime = totalTableSaveTime + pm.getElapsedTime(((Table) databaseTables.get(i)).saveTimer);
//        pm.subtract(saveTimer, totalTableSaveTime);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
    }

//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        ESlateHandle prevStaticParent = ESlateHandle.nextParent;
        ESlateHandle.nextParent = getESlateHandle();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
//long start = System.currentTimeMillis();
        pm.init(loadTimer);
//System.out.println("DBase readExternal() till here 0.1: " + (System.currentTimeMillis()-start));
        pm.init(fieldMapTimer);
long start = System.currentTimeMillis();
//        ESlateFieldMap.reportTimes = true;
//        StorageStructure fieldMap = (StorageStructure) in.readObject();
        StorageStructure fieldMap = (StorageStructure) in.readObject();
//        ESlateFieldMap fieldMap = (ESlateFieldMap) in.readObject();
        pm.stop(fieldMapTimer);
        pm.displayTime(fieldMapTimer, handle, "", "ms");
//        Enumeration enum = fieldMap.keys();
//        while (enum.hasMoreElements())
//            System.out.println("FieldMap element: " + enum.nextElement());
//System.out.println("DBase readExternal() till here 0.2: " + (System.currentTimeMillis()-start));
        String dataVersionStr = fieldMap.getDataVersion();
		int dataVersion = fieldMap.getDataVersionID();
        String tl = (String) fieldMap.get("Title");
//System.out.println("title: " + tl);
        if (tl != null) {
            tl = tl.replace('.', '$');
            _setTitle(tl);
        }
//System.out.println("2. title: " + getTitle());
        dbfile = (File) fieldMap.get("DBFile");

        /* If version "1.0" of the DBase storage format, an Array of CTables was stored.
         */
        if (dataVersion >= 5) {
            Object[] childrenTableHandles = handle.restoreChildObjects(fieldMap,  "Children Tables");
/*if (childrenTableHandles != null)
System.out.println("Restored childrenTableHandles: " + childrenTableHandles.length + ", DBase: " + this);
else
System.out.println("Restored NULL childrenTableHandles");
System.out.println("databaseTables: " + databaseTables.size());
*/
            if (childrenTableHandles != null) {
                for (int i=0; i<childrenTableHandles.length; i++) {
                    Table t = (Table) ((ESlateHandle) childrenTableHandles[i]).getComponent();
//System.out.println("childrenTableHandles[" + i + "]: " + childrenTableHandles[i]);
                    if (!databaseTables.contains(t))
                        databaseTables.add(t);
                }
         }
        }else if (dataVersion == 4) {
			databaseTables = (ArrayList) fieldMap.get("CTables");
		}else{
			if (dataVersionStr.equals("1.0")) {
				Array dbTables = (Array) fieldMap.get("CTables");
				databaseTables.clear();
				if (dbTables == null) {
					databaseTables = new ArrayList();
				}else{
					for (int i=0; i<dbTables.size(); i++) {
						databaseTables.add(dbTables.at(i));
					}
				}
				/* If the 'databaseTables' contains CTables instead of Tables, then
				 * convert them Tables.
				 */
				for (int i=0; i<databaseTables.size(); i++) {
					Object t = databaseTables.get(i);
					if (CTable.class.isAssignableFrom(t.getClass()))
						databaseTables.set(i, new Table((CTable) t));
				}
			}else{
	//System.out.println("handle: " + handle);
				Array dbTables = (Array) fieldMap.get("CTables");
	//            System.out.println("databaseTables: " + databaseTables);
				if (dbTables == null) {
					databaseTables = new ArrayList();
				}else{
					databaseTables.clear();
					for (int i=0; i<dbTables.size(); i++) {
						databaseTables.add(dbTables.at(i));
					}
				}
	//System.out.println("databaseTables.size(): " + databaseTables.size());
			}
		}
//System.out.println("DBase readExternal() till here 1: " + (System.currentTimeMillis()-start));
        /* Get the table handles and add them to the DBase handle.
         */
        if (dataVersion < 5) {
        if (handle != null) {
            for (int i=0; i<databaseTables.size(); i++) {
                String tableTitle = ((Table) databaseTables.get(i)).getTitle();
                ESlateHandle tableHandle = ((Table) databaseTables.get(i)).getESlateHandle();
//System.out.println("DBase readExternal() till here 1.1: " + (System.currentTimeMillis()-start));
                handle.add(tableHandle);
//System.out.println("DBase readExternal() till here 1.2: " + (System.currentTimeMillis()-start));
//                tableHandle.setESlateMicroworld(handle.getESlateMicroworld());
                try{
                    tableImportPlug.connectPlug(((Table) databaseTables.get(i)).getTablePlug());
                }catch (PlugNotConnectedException exc) {exc.printStackTrace();}
//                    handle.add(tableHandle);
//System.out.println("DBase readExternal() till here 1.3: " + (System.currentTimeMillis()-start));
                /* Re-apply the title of the Table, which may have changed as a result of
                 * the ESlateHandle of the Table being entered in the defaultMicroworld and
                 * then at the top level of the current microworld.
                 */
                try{
                    ((Table) databaseTables.get(i)).setTitle(tableTitle);
                }catch (Exception exc) {}
            }

//System.out.println("DBase attachTimersTime: " + attachTimersTime) ;
//attachTimersTime = 0;
//System.out.println("DBase's tables attachTimersTime: " + Table.attachTimersTime) ;
//Table.attachTimersTime = 0;
        }
        }

//System.out.println("DBase readExternal() till here 2: " + (System.currentTimeMillis()-start));

        UIProperties = (Array) fieldMap.get("UIProperties");
        databaseCreatorName = fieldMap.get("Creator name", (String) null);
        databaseCreatorSurname = fieldMap.get("Creator surname", (String) null);
        creationDate = (CDate) fieldMap.get("Creation date", (Object) null);
        lastModified = (CDate) fieldMap.get("Last modified", (Object) null);
        _protected = fieldMap.get("Locked", false);
        _password = fieldMap.get("Password", (String) null);
        tableRemovalAllowed = fieldMap.get("Table removal allowed", true);
        tableAdditionAllowed = fieldMap.get("Table addition allowed", true);
        tableExportationAllowed = fieldMap.get("Table exportation allowed", true);
        hiddenTablesDisplayed = fieldMap.get("Hidden tables displayed", false);
        metadata = fieldMap.get("Metadata", "");
        dbView = (DBView) fieldMap.get("DBView");

        if (_protected)
            locked = true;
        else
            locked = false;

        isModified = false;
        newDatabase = false;
        databaseListeners = new ArrayList();
        databasePropertyChangeListeners = new ArrayList();
        propertyChangeSupport = new PropertyChangeSupport(this);
//        System.out.println("Obf 1");
//        initDatabase();

//table        for (int i=0; i<databaseTables.size(); i++)
//table            ((Table) databaseTables.get(i)).database = this;
        if (!(databaseTables.size() == 0))
            this._activateTable(0);

//System.out.println("READ DBASE " + handle + " time: " + (System.currentTimeMillis()-start));
        pm.stop(loadTimer);
        // The load time counted includes the sun of the load times of the Tables of the DBase.
        // Substract the load times of the tables, so that the proper DBase load time is recorded.
//        long totalTableLoadTime = 0;
//        for (int i=0; i<databaseTables.size(); i++)
//            totalTableLoadTime = totalTableLoadTime + pm.getElapsedTime(((Table) databaseTables.get(i)).loadTimer);
//        pm.subtract(loadTimer, totalTableLoadTime);
//System.out.println("DBase readExternal() till here 4: " + (System.currentTimeMillis()-start));
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");

        ESlateHandle.nextParent = prevStaticParent;
    }


    /** Checks if the title of the given table is unique in the DBase.
     *  If the table's title is null, false is returned.
     */
    public static boolean hasUniqueTitle(Table table, DBase cdb) {
        if (table == null || cdb == null)
            throw new IllegalArgumentException(resources.getString("CDatabaseMsg91"));
        /* If the table possesses a title, then check if this title is
         * unique in the DBase.
         */
        String title = table.getTitle();
        if (title != null) {
            ArrayList databaseTablesTitles = cdb.getTableTitles();
            ArrayList databaseTables = cdb.getTables();
//            System.out.println("getTableTitles: " + cdb.getTableTitles());
            boolean sameTitleFound = false;
            for (int i =0; i<databaseTablesTitles.size(); i++) {
                if (((String) databaseTablesTitles.get(i)).equals(title)) {
                    /* If the table that carries the same title is the given table itself
                     * continue.
                     */
                    if (((Table) databaseTables.get(i)).equals(table))
                        continue;
//                    System.out.println(databaseTables.get(i) + ", table: " + table);
                    sameTitleFound = true;
                    break;
                }
            }
            if (sameTitleFound)
                return false;
        }else
            return false;
        return true;
    }


    /** Generates a unique title for the Table <i>table</i> in the DBase <i>cdb</i>.
     * If the table's title is null a default localized variaton of the default title
     * 'Untitled' is produced. The unique title is returned, but the table's title
     * is left intact.
     */
    public static String generateUniqueTitle(Table table, DBase cdb) {
        String title = table.getTitle();
        if (title == null)
            title = resources.getString("CDatabaseMsg100");

        ArrayList databaseTables = cdb.getTables();
        /* if there exists a table in the DBase with the same title, and
         * _addTable() was given the right to differentiate the new table's
         * title, then the new table's title is altered by appending an
         * ";<number>" extension to its end.
         */
        String newTitle;
        boolean titleChanged = false;
        int extension = 1;
        newTitle = title + ';' + extension;
        boolean sameTitleFound;
        while (true) {
            extension++;
            sameTitleFound = false;
            for (int z=0; z<databaseTables.size(); z++) {
                if (((Table) databaseTables.get(z)).getTitle() != null && ((Table) databaseTables.get(z)).getTitle().equals(newTitle)) {
                    newTitle = title + ';' + extension;
                    sameTitleFound = true;
                    titleChanged = true;
                    break;
                }
            }
            if (!sameTitleFound)
                break;
        }

        return(newTitle);
    }


    /** Adds Table <i>table</i> to the DBase. The new table becomes the active table
     *  in the DBase. The title of the new table has to be unique in the
     *  DBase or else the method will fail. The current title of the table is
     *  used.
     *  @see #addTable(gr.cti.eslate.database.engine.Table, java.lang.String, boolean)
     *  @exception InvalidTitleException hrown when the new table's title is the
     *  same as the title of another table in the DBase.
     */
    public boolean addTable(Table table, boolean differentiateTitle)
    throws InvalidTitleException, InsufficientPrivilegesException {
        if (!tableAdditionAllowed)
            throw new InsufficientPrivilegesException(resources.getString("CDatabaseMsg93") + databaseCreatorName
            + " " + databaseCreatorSurname + resources.getString("CDatabaseMsg94"));

        if (_addTable(table, table.getTitle(), differentiateTitle)) {
            /* When a Table is added to a DBase, its handle is added to the DBase's handle.
             * This happens only when the Table's handle doesn't already have a parent, i.e.
             * the Table does not already belong to some other DBase.
             */
            addTableHandleToDBaseHandle(table);
            ESlateHandle tableHandle = table.getESlateHandle();
            try{
//                table.getESlateHandle().setESlateMicroworld(getESlateHandle().getESlateMicroworld()); // make sure the tableImportPlug has been created
                tableImportPlug.connectPlug(table.getTablePlug());
            }catch (PlugNotConnectedException exc) {}
            return true;
        }else
            return false;

    }


    /**
     *  Adds Table <i>table</i> to the DBase. The new table becomes the active table
     *  in the DBase. The title of the new table has to be unique in the
     *  DBase or else the method will fail.
     *  @param table The table to be added to the database.
     *  @param title The title with which the table will be inserted in the DBase.
     *  @param differentiateTitle When this flag is set the method is authorized
     *  to give the new table a new title, if its title is already used by another
     *  table in the DBase. The new title is formed by applying an extension
     *  of the form <i>;<number></i> to <i>title</i> parameter.
     *  @return <i>true</i>, if the table is succesfully added. <i>false</i>, if <i>table</i>
     *          is <i>null</i> or if <i>table</i> already exists in the database.
     *  @exception InvalidTitleException Thrown when the new table's title is the
     *  same as the title of another table in the DBase.
     */
    public boolean addTable(Table table, String title, boolean differentiateTitle)
    throws InvalidTitleException, InsufficientPrivilegesException {
        if (!tableAdditionAllowed)
            throw new InsufficientPrivilegesException(resources.getString("CDatabaseMsg93") + databaseCreatorName
            + " " + databaseCreatorSurname + resources.getString("CDatabaseMsg94"));

        if (table == null) return false;

        int i =0;
        if (!databaseTables.isEmpty()) {
            while (i<databaseTables.size() && !table.equals((Table) databaseTables.get(i))) i++;
            if (i!=databaseTables.size()) return false;
        }

        String oldTitle = table.getTitle();
        try{
            table.setTitle(title); //_setTitle
        }catch (PropertyVetoException exc) {}
        if (title == null)
            differentiateTitle = true;
        if (!hasUniqueTitle(table, this)) {
            if (!differentiateTitle)
                throw new InvalidTitleException(resources.getString("CDatabaseMsg89") + resources.getString("CDatabaseMsg98") + title + resources.getString("CDatabaseMsg99"));
            else{
                try{
                    table.setTitle(generateUniqueTitle(table, this)); //_setTitle
                }catch (PropertyVetoException exc) {}
            }
        }

        databaseTables.add(table);
        table.addVetoableChangeListener(tableVetoListener);
//        TableFiles.add(table.file);
//        System.out.println("In addTable: " + table.database);
//        activeTable = table;
        setModified();
        newDatabase = false;

//        System.out.println("Database == firing tableAdded event");
        /* When a Table is added to a DBase, its handle is added to the DBase's handle.
         * This happens only when the Table's handle doesn't already have a parent, i.e.
         * the Table does not already belong to some other DBase.
         */
        addTableHandleToDBaseHandle(table);
        ESlateHandle tableHandle = table.getESlateHandle();
        try{
//            table.getESlateHandle().setESlateMicroworld(getESlateHandle().getESlateMicroworld()); // make sure the tableImportPlug has been created
            tableImportPlug.connectPlug(table.getTablePlug());
        }catch (PlugNotConnectedException exc) {}
//        if (handle != null && tableHandle.getParentHandle() == null)
//            handle.add(tableHandle);
        fireTableAdded(new TableAddedEvent(this, table));

//        System.out.println("oldTitle: " + oldTitle + ", new title: " + table.getTitle());
///        table.fireTableRenamed(new TableRenamedEvent(table, Tables.indexOf(table), oldTitle, table.getTitle()));

//        System.out.println("Database addTable == firing activeTableChanged event");
        _activateTable(databaseTables.size()-1);

        return true;
    }

    /* The 'addTableHandleToDBaseHandle' parameter specifies whether the Table's handle will be added to
     * the DBase's handle or not. If the Table is imported from the tableImportPin, then
     * its handle should not be added to the DBase, since some external component holds
     * the Table.
     */
    private final boolean _addTable(Table table, String title, boolean differentiateTitle) throws InvalidTitleException {
        if (table == null) return false;

        int i =0;
        if (!databaseTables.isEmpty()) {
            while (i<databaseTables.size() && !table.equals((Table) databaseTables.get(i))) i++;
            if (i!=databaseTables.size()) return false;
        }

        String oldTitle = table.getTitle();
        try{
            if (title != null)
                table.setTitle(title); //__setTitle
        }catch (PropertyVetoException exc) {}
//System.out.println("addTable() exists table with the same name as table " + oldTitle);
        if (title == null)
            differentiateTitle = true;
        if (!hasUniqueTitle(table, this)) {
            if (!differentiateTitle) {
                throw new InvalidTitleException(resources.getString("CDatabaseMsg89") + resources.getString("CDatabaseMsg98") + title + resources.getString("CDatabaseMsg99"));
            }else{
                try{
                    table.setTitle(generateUniqueTitle(table, this)); //_setTitle
                }catch (PropertyVetoException exc) {exc.printStackTrace();}
            }
        }

        databaseTables.add(table);
        table.addVetoableChangeListener(tableVetoListener);
//        TableFiles.add(table.file);
//        System.out.println("In addTable: ");
//        System.out.println("In addTable: " + table.database);
//        activeTable = table;
        setModified();
        newDatabase = false;

        fireTableAdded(new TableAddedEvent(this, table));
//        System.out.println("title: " + title);
//        System.out.println("oldTitle: " + oldTitle + ", new title: " + table.getTitle());
///        table.fireTableRenamed(new TableRenamedEvent(table, databaseTables.indexOf(table), null, table.getTitle()));
        _activateTable(databaseTables.size()-1);
        return true;
    }

    private void addTableHandleToDBaseHandle(Table table) {
        if (table == null) return;
        ESlateHandle tableHandle = table.getESlateHandle();
        if (tableHandle == null) return;
        if (tableHandle.getParentHandle() == null)
            getESlateHandle().add(tableHandle);
    }

    /**
     *  Adds Table <i>table</i>, which was restored from a serialized file to the DBase,
     *  to which it belongs. <i>restoreTable</i> cannot be used in the place of <i>addTable</i>,
     *  because it cannot add a Table to a DBase, which does not already contain this
     *  Table.
     *  @param table The restored table to be added to the database.
     *  @return <i>true</i>, if the table is succesfully added. <i>false</i>, if <i>table</i>
     *          is <i>null</i> or if <i>table</i> already exists in the database.
     *  @exception TableNotBelongsToDatabaseException Thrown when <i>restoreTable</i> is
     *          called with a Table, which does not belong to the DBase.
     */
/*    public boolean restoreTable(Table table) throws TableNotBelongsToDatabaseException {
        if (table == null) return false;
        System.out.println("Here221");
        int i =0;
        if (!TableFiles.isEmpty()) {
            System.out.println("Here222");
            while (i<TableFiles.size() && !table.file.equals((File) TableFiles.get(i))) i++;
            if (i==TableFiles.size()) {
                System.out.println("Here223");
                throw new TableNotBelongsToDatabaseException(resources.getString("CDatabaseMsg11"));
            }
        }else{
            System.out.println("Here224");
            throw new TableNotBelongsToDatabaseException(resources.getString("CDatabaseMsg11"));
        }

        System.out.println("Here225");
        i =0;
        if (!databaseTables.isEmpty()) {
                System.out.println("Here226");
            while (i<databaseTables.size() && !table.equals((Table) databaseTables.get(i))) i++;
            if (i!=databaseTables.size()) return false;
        }
        System.out.println("Here227");

        databaseTables.add(table);
         System.out.println("Here228");

        table.database = this;
        System.out.println("Here229");
        fireTableAdded(new TableAddedEvent(this));
        System.out.println("Here230");
        return true;
    }
*/

    /**
     *  Creates and returns an instance of a Java File at the specified path.
     *  @param s1 The absolute or relative path name of the file, or simply the name
     *  of the file if it is created in the working directory.
     *  @param fileExtension The extension of the file.
     *  @exception InvalidPathException Thrown when:
     *  <ul>
     *  <li> the supplied path is <i>null</i>,
     *  <li> the supllied path does not exist, or
     *  <li> an invalid file name is given.
     *  </ul>
     */
    public static File analyzeFileName(String s1, String fileExtension) throws InvalidPathException {
        File file = null;
//        ResourceBundle resources = ResourceBundle.getBundle("gr.cti.eslate.database.engine.DBResourceBundle", Locale.getDefault());
        if (s1==null || s1.length() == 0)
            throw new InvalidPathException(resources.getString("CDatabaseMsg12"));

        s1 = s1.trim();

        String fileSeparator = System.getProperty("file.separator");
        int lastSeparatorIndex = s1.lastIndexOf(fileSeparator);
        String directory;
        if (lastSeparatorIndex != -1) {
            directory = s1.substring(0, lastSeparatorIndex);
            if (fileSeparator.equals("\\")) {
                StringBuffer s = new StringBuffer(directory);
                for (int i=0; i<directory.length(); i++) {
                    if (s.charAt(i) == '\\') {
                        s.insert(i, '\\');
                        i++;
                    }
                }
            }
        }else
            directory = System.getProperty("user.dir");

        if (directory.indexOf(fileSeparator) == -1)
            directory = directory+fileSeparator;

        File dir = new File(directory);
        if (!dir.exists())
            throw new InvalidPathException(resources.getString("CDatabaseMsg13") + dir.getPath() + resources.getString("CDatabaseMsg14"));

        if (!dir.isDirectory())
            throw new InvalidPathException(resources.getString("CDatabaseMsg13") + dir.getPath() + resources.getString("CDatabaseMsg15"));

        fileExtension = '.' + fileExtension;
        String fileName = s1.substring(lastSeparatorIndex+1).trim();
        if (fileName.equals("") || fileName.equals(".") || fileName.equals(fileExtension))
            throw new InvalidPathException(resources.getString("CDatabaseMsg16") + fileName + "\"");

        if (fileName.indexOf(fileExtension) == -1)
            fileName = fileName + fileExtension;
        else if (fileName.indexOf(fileExtension) != fileName.length()-fileExtension.length())
            fileName = fileName + fileExtension;

        file = new File(dir, fileName);
        return file;
    }


    /**
     *  Imports a delimited ascii file into a new Table, which becomes active. The
     *  supported delimeters are ',' and ';'. Both quoted and unquoted data are supported.
     *  @param  fileName The absolute or relative path of the ascii file.
     *  @param  delim The delimiter used in <i>fileName</i>.
     *  @exception InvalidPathException If <i>fileName</i> does not exist.
     *  @exception UnableToReadFileException If the file exists but cannot be read due to I/O problems.
     *  @exception EmptyFileException If the file is empty.
     *  @exception InvalidTextFileException If:
     *              <ul>
     *              <li> A null or dublicate field name is read from the ascii file.
     *              <li> A record has less/more fields than the fields of the table, e.g. incorrect use of delimeters in the ascii file.
     *              </ul>
     *  @exception InvalidDelimeterException If an unsupported delimiter char is supplied.
     */
    public void importTableFromTextFile(String fileName, char delim) throws InvalidPathException,
    UnableToReadFileException, EmptyFileException, InvalidTextFileException,
    InvalidDelimeterException, InsufficientPrivilegesException {
        if (!tableAdditionAllowed)
            throw new InsufficientPrivilegesException(resources.getString("CDatabaseMsg93") + databaseCreatorName
            + " " + databaseCreatorSurname + resources.getString("CDatabaseMsg94"));

        FileReader fr = null;
        try{
            fr = new FileReader(fileName);
        }catch (FileNotFoundException e) {throw new InvalidPathException(resources.getString("CDatabaseMsg17") + fileName + resources.getString("CDatabaseMsg18"));}

        BufferedReader br = new BufferedReader(fr, 30000);
        String line;

        /* Initialize the new table, which will accept the imported data.
         */
        Table table = new Table();

        try{
            if (!(delim==',') && !(delim == ';')) {
                br.close();
                throw new InvalidDelimeterException(resources.getString("CDatabaseMsg19") + delim + resources.getString("CDatabaseMsg20"));
            }

            /* First line: contains field names.
             */
            try{
                line = br.readLine();
            }catch (IOException e) {br.close(); throw new UnableToReadFileException(resources.getString("CDatabaseMsg21") + fileName + "\"");}

            if (line == null) {
                br.close();
                throw new EmptyFileException(resources.getString("CDatabaseMsg17") + fileName + resources.getString("CDatabaseMsg22"));
            }

            /* Read the names of the fields.
             */
            int delimIndex = 0;
            int prevIndex = delimIndex;
            int fieldCount = 0;
            int quoteCount=0;
            int quoteIndex;
            String colName;
            while ((delimIndex = line.indexOf(delim, delimIndex)) != -1) {
                colName = line.substring(prevIndex, delimIndex).trim();
                quoteIndex = -1;
                quoteCount = 0;
                try{
                    while ((quoteIndex = colName.indexOf('"', ++quoteIndex)) != -1)
                    quoteCount++;
                }catch (StringIndexOutOfBoundsException e) {};
//                  System.out.println("quoteCount: " + quoteCount);
                if ((quoteCount%2) != 0) {
                    delimIndex++;
                    continue;
                }
                if (colName.length()>0 && colName.charAt(0) == '"' && colName.charAt(colName.length()-1) == '"') {
                    colName = colName.substring(1, colName.length()-1);
//                    System.out.println("colName: " + colName);
                }

                if (colName.equals("")) {
                    br.close();
//                    System.out.println("Exc1");
                    throw new InvalidTextFileException(resources.getString("CDatabaseMsg17") + fileName + resources.getString("CDatabaseMsg23"));
                }

//            System.out.println(colName);
                try{
                    table.addField(colName, String.class/*"String"*/);
                }catch (InvalidFieldNameException e) {
//                    e.printStackTrace();
                    br.close();throw new InvalidTextFileException(resources.getString("CDatabaseMsg17") + fileName + resources.getString("CDatabaseMsg24") + "(" + e.message +")");
                }catch (InvalidKeyFieldException e) {
//                    e.printStackTrace();
                    br.close();  throw new InvalidTextFileException(resources.getString("CDatabaseMsg17") + fileName + resources.getString("CDatabaseMsg24"));
                }catch (InvalidFieldTypeException e) {
//                    e.printStackTrace();
                    br.close(); throw new InvalidTextFileException(resources.getString("CDatabaseMsg17") + fileName + resources.getString("CDatabaseMsg24"));
                }

                fieldCount++;
                delimIndex++;
                prevIndex = delimIndex;
            }

            colName = line.substring(prevIndex).trim();
            if (colName.length()>0 && colName.charAt(0) == '"' && colName.charAt(colName.length()-1) == '"')
                colName = colName.substring(1, colName.length()-1);
            if (colName.equals("")) {
                br.close();
                System.out.println("Exc5");
                throw new InvalidTextFileException(resources.getString("CDatabaseMsg17") + fileName + resources.getString("CDatabaseMsg23"));
            }
//        System.out.println(colName);
            try{
                table.addField(colName, String.class); //"String");
            }catch (InvalidFieldNameException e) {
                e.printStackTrace();
                br.close(); throw new InvalidTextFileException(resources.getString("CDatabaseMsg17") + fileName + resources.getString("CDatabaseMsg24") + " (" + e.message +")");
            }catch (InvalidKeyFieldException e) {
                e.printStackTrace();
                br.close();  throw new InvalidTextFileException(resources.getString("CDatabaseMsg17") + fileName + resources.getString("CDatabaseMsg24"));
            }catch (InvalidFieldTypeException e) {
                e.printStackTrace();
                br.close(); throw new InvalidTextFileException(resources.getString("CDatabaseMsg17") + fileName + resources.getString("CDatabaseMsg24"));
            }
            fieldCount++;

            ArrayList rec = null;
//            ArrayList rec = new ArrayList();
//            rec.ensureCapacity(fieldCount);
//            String tata = "";
//            for (int i=0; i<fieldCount; i++)
//                rec.add(tata);
            /* Read the rest of the file.
             */
            String data;
            int dataCount;
            int recCount = 0;
            quoteCount=0;
            boolean continuedLine = false;
            String prevLine = null;
            try{
                while ((line = br.readLine()) != null) {
                    if (continuedLine) {
                        continuedLine = false;
                        line = prevLine + line;
                    }else{
                        rec = new ArrayList(fieldCount);
                        for (int i=0; i<fieldCount; i++)
                            rec.add(null);
//                        rec.ensureCapacity(fieldCount);
                        recCount++;
                    }
//                    System.out.println("line: " + line);
                    delimIndex = 0;
                    dataCount = -1;
                    prevIndex = delimIndex;
                    while ((delimIndex = line.indexOf(delim, delimIndex)) != -1) {
//                        System.out.println("  comma: " + delimIndex);
                        data = line.substring(prevIndex, delimIndex).trim();
//                        System.out.println("dataCount: " + dataCount + ", data: " + data);
                        /* Check if the found delimeter, is part of the value of the field
                         * and not a delimeter.
                         */
//                        System.out.println("data: " + data);
                        quoteIndex = -1;
                        quoteCount = 0;
                        try{
                            while ((quoteIndex = data.indexOf('"', ++quoteIndex)) != -1)
                                quoteCount++;
                        }catch (StringIndexOutOfBoundsException e) {};
//                        System.out.println("quoteCount: " + quoteCount);
                        if ((quoteCount%2) != 0) {
                            delimIndex++;
                            continue;
                        }

                        if (data.length()>0 && data.charAt(0) == '"' && data.charAt(data.length()-1) == '"')
                            data = data.substring(1, data.length()-1);

                        if (data.length()==0) {
                            dataCount++;
                            rec.set(dataCount, null);
                            delimIndex++;
                            prevIndex = delimIndex;
//                        System.out.println("null");
                            continue;
                        }

                        dataCount++;
//                        System.out.println("Putting " + data + " to " + rec + " at " + dataCount);
                        rec.set(dataCount, data);

                        delimIndex++;
                        prevIndex = delimIndex;
//                    System.out.println("data:" + data + " Comma: " + delimIndex);
                    }

//                System.out.println("1. fieldCount: " + fieldCount + " dataCount: " + dataCount + " fieldCount: " + fieldCount);

                    /* The last piece of data in the line is read in here.
                     */
                    data = line.substring(prevIndex).trim();
                    if (data.length()>0 && data.charAt(0) == '"' && data.charAt(data.length()-1) == '"')
                        data = data.substring(1, data.length()-1);
                    if (data.length()==0) {
                        dataCount++;
//                        System.out.println("Putting to rec: null");
                        rec.set(dataCount, null);
                    }else{
//                        System.out.println("Putting to rec: " + data);
                        dataCount++;
                        rec.set(dataCount, data);
                    }

                    /* If the just read line contains less fields that it should, then don't
                     * throw an exception immediately. Instead read-in the next line and paste
                     * this line to the beggining of the next one. It may be the case that this
                     * line does not contain all the fields because it is continued in the next
                     * line (this happens actually with MS Access tables).
                     */
                    if ((dataCount+1) < fieldCount) {
                        prevLine = line;
                        continuedLine = true;
//                        throw new InvalidTextFileException("File \"" + fileName + "\" is not valid. Record " +recCount+ " contains " + dataCount + " fields instead of " + fieldCount);
                    }else{

                        try{
//                            System.out.println("Adding record..");
                            table.addRecord(rec, false);
                        }catch (TableNotExpandableException e) {}
                         catch (NoFieldsInTableException e) {
//                            System.out.println("Exc10 " + recCount);
                            e.printStackTrace();
                            br.close(); throw new InvalidTextFileException(resources.getString("CDatabaseMsg26") + recCount);} // + ": " + e.message);}
                         catch (DuplicateKeyException e) {
//                            System.out.println("Exc11 " + recCount);
                            e.printStackTrace();
                            br.close(); throw new InvalidTextFileException(resources.getString("CDatabaseMsg26") + recCount);} // + ": " + e.message);}
                         catch (NullTableKeyException e) {
//                            System.out.println("Exc12 " + recCount);
//                            e.printStackTrace();
                            br.close(); throw new InvalidTextFileException(resources.getString("CDatabaseMsg26") + recCount);} // + ": " + e.message);}
                         catch (InvalidDataFormatException e) {
                            e.printStackTrace();
//                            System.out.println("Exc13 " + recCount);
                            br.close(); throw new InvalidTextFileException(resources.getString("CDatabaseMsg26") + recCount);} // + ": " + e.message);}
                    }

                }
            }catch (IOException e) {br.close();
//                System.out.println("Exc14 " + recCount);
                e.printStackTrace();
                throw new UnableToReadFileException(resources.getString("CDatabaseMsg21") + fileName + "\"");}
             catch (IndexOutOfBoundsException e) {br.close();
//                System.out.println("Exc15 " + recCount);
                e.printStackTrace();
                throw new InvalidTextFileException(resources.getString("CDatabaseMsg28") + fileName + resources.getString("CDatabaseMsg29") + recCount + resources.getString("CDatabaseMsg30") + fieldCount+")");}

            br.close();
        }catch (Exception e) {
            e.printStackTrace();
//            System.out.println("Exc16 ");
//            System.out.println(e.getClass() + ", " + e.getMessage());
//            e.printStackTrace();
            throw new UnableToReadFileException(resources.getString("CDatabaseMsg21") + fileName + "\"");}

//        try{
        String title;
        if (fileName.indexOf(System.getProperty("file.separator")) != -1)
            title = fileName.substring(fileName.lastIndexOf(System.getProperty("file.separator"))+1);
        else
            title = fileName;

//            _addTable(table, title, true, true);
        addTableHandleToDBaseHandle(table);
        try{
//            table.getESlateHandle().setESlateMicroworld(handle.getESlateMicroworld());
            tableImportPlug.connectPlug(table.getTablePlug());
        }catch (PlugNotConnectedException exc) {
            exc.printStackTrace();
        }
/*        }catch (InvalidTitleException e) {
            //This should never occur
            System.out.println("Serious inconsistency error in DBase importTableFromTextFile() : (1)");
            return;
        }
*/
//        _activateTable(databaseTables.size()-1);
//        System.out.println("importTableFromTextFile: " + table.getRecordCount());
//        activeTable = table;
    }


    /**
     *  Exports a Table to an delimited ascii file. Two delimeters are supported: ';' and
     *  ','. Data may be stored in the file quoted or as is.
     *  @param fileName The absolute or relative path of the ascii file, to which the table will be exported.
     *  @param delim The delimiter character to be used.
     *  @param quoteData <i>true</i> to generated quoted data. <i>false</i>, to store data as is.
     *  @return <i>true</i>, if the operation is succesfull. <i>false</i>, if the Table contains
     *          no fields and therefore no data.
     *  @exception  UnableToWriteFileException If the supplied path is incorrect, or the
     *              name of the file is invalid, or an I/O problem occurs.
     *  @exception  InvalidDelimeterException If an unsupported delimiter is supplied.
     */
    public boolean exportTableToTextFile(Table table, String fileName, char delim, boolean quoteData)
    throws UnableToWriteFileException, InvalidDelimeterException, InsufficientPrivilegesException {

        if (!tableExportationAllowed)
            throw new InsufficientPrivilegesException(resources.getString("CDatabaseMsg93") + databaseCreatorName
            + " " + databaseCreatorSurname + resources.getString("CDatabaseMsg96"));

        table.exportTableToTextFile(fileName, delim, quoteData);
        return true;
    }


    /**
     *  Saves the DBase. This operation first saves the Tables contained in the
     *  database (only the ones that were modified) and then saves the database itself.
     *  When a database is saved for the first time, the user is asked for the file to
     *  which it will be saved. The user may supply an absolute or relative path name or simply
     *  the name of the file, if it should be stored in the working directory. The file
     *  receives a default ".cdb" extension.
     *  @exception  InvalidPathException If the supplied file names for the database's Tables
     *              or the database itself are incorrect.
     *  @exception  UnableToSaveException If some I/O error occurs.
     *  @exception  UnableToOverwriteException If the file specified for a Table already
     *              exists and is used by some other Table in the same database.
     *  @see    #analyzeFileName(java.lang.String, java.lang.String)
     */
    public void save() throws InvalidPathException, UnableToSaveException,
    UnableToOverwriteException {

        File file = dbfile;
        String s1 = null;
        if (file == null) {
//            System.out.print("Database file name: ");
//            String s1 = getStringSTDIN();
            String title;
            if (this._getTitle() == null)
                title = resources.getString("CDatabaseMsg35");
            else
                title = resources.getString("CDatabaseMsg35") + "\"" + this._getTitle() + "\"";
/*            if (fileDialog == null) {
                fileDialog = new FileDialog(new JFrame(), title, FileDialog.SAVE);
                fileDialog.setDirectory(System.getProperty("user.dir"));
            }else{
*/
            if (fileDialog == null)
                fileDialog = getFileDialog();

                fileDialog.setTitle(title);
                fileDialog.setMode(FileDialog.SAVE);
//            }
            if (this._getTitle() == null)
                fileDialog.setFile("database1.cdb");
            else
                fileDialog.setFile(this._getTitle() + ".cdb");
            fileDialog.setDefaultExtension("cdb");
            fileDialog.show();
            s1 = fileDialog.getFile();

            if (s1 == null) return;
//            System.out.println("s1: " + s1);

            try{
                file = analyzeFileName(fileDialog.getDirectory()+s1, "cdb");
            }catch (InvalidPathException e) {throw new InvalidPathException(e.message);}

            /* FileDialog has already performed this check.
                if (file.exists()) {
                    System.out.print("Overwrite existing file? ");
                    String s2 = getStringSTDIN();
                    if (!s2.trim().toLowerCase().equals("y"))
                        return;
                }
            */

        }

//        System.out.println(file.getPath());

        File previousFile = null;
        boolean titleSetToFileName = false;
        try{
            /* If the title of the DBase is not set, then set it to the name
             * of the file in which it is stored.
             */
            if (this._getTitle() == null || this._getTitle().trim().length() == 0) {
                /* '.' character is used as the E-Slate component path delimeter.
                 * Therefore it cannot be part of the name of an ESlateHandle.
                 * DBase names are used as handle names. Therefore we exclude
                 * dots from the handles name, if they exist
                 */
                int dotIndex = s1.indexOf('.');
                if (dotIndex != -1) {
                    String fileNameTitle = s1.substring(0, dotIndex);
                    this.setTitle(fileNameTitle); //_setTitle
                }else
                    this.setTitle(s1); //_setTitle
                titleSetToFileName = true;
            }

            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file), 30000));
            /* Set this DBase data member ("dbfile") here, so as to be stored.
             */
            previousFile = dbfile;
            dbfile = file;
            CDate d = new CDate(new Date(System.currentTimeMillis()));
            setCreationDate(d);
            setLastModified(d);
            out.writeObject(this);
//            writeExternal(out);
            out.flush();
            out.close();
            resetModifiedFlag();
            newDatabase = false;
        }catch (IOException e) {
            e.printStackTrace();
            if (titleSetToFileName)
                this._setTitle(null);
            dbfile = previousFile;
            throw new UnableToSaveException(resources.getString("CDatabaseMsg36") + title + resources.getString("CDatabaseMsg37") + file.getPath() + "\"");
        }

    }


    /**
     *  Saves the CDatabase, if any changes occured. This operation first saves the Tables contained in the
     *  database (only the ones that were modified) and then saves the database itself.
     *  When a database is saved for the first time, the user is asked for the file to
     *  which it will be saved. The user may supply an absolute or relative path name or simply
     *  the name of the file, if it should be stored in the working directory. The file
     *  receives a default ".cdb" extension.
     *  @param      noConfirmation If <i>false</i>, <i>saveChanges</i> will ask for confirmation
     *              before saving each table or the database itself. If <i>true</i>,
     *              <i>saveChanges</i> works silently.
     *  @exception  InvalidPathException If the supplied file names for the database's Tables
     *              or the database itself are incorrect.
     *  @exception  UnableToSaveException If some I/O error occurs.
     *  @exception  UnableToOverwriteException If the file specified for a Table already
     *              exists and is used by some other Table in the same database.
     *  @see    #analyzeFileName(java.lang.String, java.lang.String)
     */
    public void saveChanges(boolean noConfirmation, Component container) throws InvalidPathException, UnableToSaveException,
    UnableToOverwriteException {

        String dbTitle = _getTitle();
        if (dbTitle == null) dbTitle = "";
//        System.out.println("Saving database "  + TableFiles);
        boolean saveNeeded = isModified();

        if (saveNeeded) {
            if (noConfirmation == false) {
                if ((ESlateOptionPane.showConfirmDialog(new JFrame(), resources.getString("CDatabaseMsg41") + dbTitle + resources.getString("CDatabaseMsg39"),
                    resources.getString("CDatabaseMsg42"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.NO_OPTION)
                        return;
            }

            if (container != null)
                container.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

            File file = dbfile;
            String s1 = null;
            if (file == null) {
//                System.out.print("Database file name: ");
//              String s1 = getStringSTDIN();
                String title;
                if (this._getTitle() == null)
                    title = resources.getString("CDatabaseMsg35");
                else
                    title = resources.getString("CDatabaseMsg35") + "\"" + this._getTitle() + "\"";
/*                if (fileDialog == null) {
                    fileDialog = new FileDialog(new JFrame(), title, FileDialog.SAVE);
                    fileDialog.setDirectory(System.getProperty("user.dir"));
                }else{
*/
                if (fileDialog == null)
                    fileDialog = getFileDialog();

                fileDialog.setTitle(title);
                fileDialog.setMode(FileDialog.SAVE);
                fileDialog.setDefaultExtension("cdb");
//                }
                if (this._getTitle() == null)
                    fileDialog.setFile("database1.cdb");
                else
                    fileDialog.setFile(this._getTitle() + ".cdb");
                fileDialog.show();
                s1 = fileDialog.getFile();

                if (s1 == null) return;
//                System.out.println("s1: " + s1);

                try{
                    file = analyzeFileName(fileDialog.getDirectory()+s1, "cdb");
                }catch (InvalidPathException e) {throw new InvalidPathException(e.message);}

           }

//            System.out.println(file.getPath());

            File previousFile = null;
            boolean titleSetToFileName = false;
            boolean[] modifiedFlags = new boolean[this.getTableCount()];
            boolean databaseModified = isModified;
            try{
                /* If the title of the DBase is not set, then set it to the name
                 * of the file in which it is stored.
                 */
                if (this._getTitle() == null || this._getTitle().trim().length() == 0) {
                /* '.' character is used as the E-Slate component path delimeter.
                 * Therefore it cannot be part of the name of an ESlateHandle.
                 * DBase names are used as handle names. Therefore we exclude
                 * dots from the handles name, if they exist
                 */
                int dotIndex = s1.indexOf('.');
                if (dotIndex != -1) {
                    String fileNameTitle = s1.substring(0, dotIndex);
                    this.setTitle(fileNameTitle); //_setTitle
                }else
                    this.setTitle(s1); //_setTitle
                    titleSetToFileName = true;
                }

                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file), 30000));
                /* Set this DBase data member ("dbfile") here, so as to be stored.
                 */
                previousFile = dbfile;
                dbfile = file;
                for (int i=0; i<databaseTables.size(); i++) {
                    modifiedFlags[i] = ((Table) databaseTables.get(i)).isModified;
                    ((Table) databaseTables.get(i)).isModified = false;
                }
                isModified = false;
                CDate d = new CDate(new Date(System.currentTimeMillis()));
                setCreationDate(d);
                setLastModified(d);
//                writeExternal(out);
                out.writeObject(this);
                out.flush();
                out.close();
                newDatabase = false;
            }catch (IOException e) {
//                e.printStackTrace();
                /* Save was unsuccessful, so set back the properties of this
                 * database.
                 */
                if (titleSetToFileName)
                    this._setTitle(null);
                dbfile = previousFile;
                isModified = databaseModified;
                for (int i=0; i<databaseTables.size(); i++)
                    ((Table) databaseTables.get(i)).isModified = modifiedFlags[i];
                if (container != null)
                    container.setCursor(java.awt.Cursor.getDefaultCursor());
                throw new UnableToSaveException(resources.getString("CDatabaseMsg36") + title + resources.getString("CDatabaseMsg37") + file.getPath() + "\"");
            }

            if (container != null)
                container.setCursor(java.awt.Cursor.getDefaultCursor());
        }
    }


    /**
     *  Saves the DBase to the specified file. This file becomes the database's default
     *  storage file. The file receives a default ".cdb" extension.
     *  @param fileName The name of the file.
     *  @exception InvalidPathException If the supplied file names for the database's Tables
     *              or the database itself are incorrect.
     *  @exception  UnableToSaveException If some I/O error occurs.
     *  @exception  UnableToOverwriteException If the file specified for a Table already
     *              exists and is used by some other Table in the same database.
     *  @see    #save()
     *  @see    #analyzeFileName(java.lang.String, java.lang.String)
     */
    public void saveAs(String fileName) throws InvalidPathException, UnableToSaveException,
    UnableToOverwriteException {

        File file = dbfile;

        try{
            file = analyzeFileName(fileName, "cdb");
        }catch (InvalidPathException e) {throw new InvalidPathException(e.message);}

        if (file.exists()) {
            if (!file.canWrite())
                throw new UnableToSaveException(resources.getString("CDatabaseMsg43") + file.getPath() + "\"");
        }

//        System.out.println(file.getPath());

        File previousFile = null;
        boolean titleSetToFileName = false;
        try{
            /* If the title of the DBase is not set, then set it to the name
             * of the file in which it is stored.
             */
            if (this._getTitle() == null || this._getTitle().trim().length() == 0) {
                String fl = getFileNameFromPath(fileName, false);
                int dotIndex = fl.indexOf('.');
                if (dotIndex != -1) {
                    String fileNameTitle = fl.substring(0, dotIndex);
                    this.setTitle(fileNameTitle); //_setTitle
                }else
                    this.setTitle(fl); //_setTitle
                titleSetToFileName = true;
            }

            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file), 30000));
            /* Set this DBase data member ("dbfile") here, so as to be stored.
             */
            previousFile = dbfile;
            dbfile = file;
            CDate d = new CDate(new Date(System.currentTimeMillis()));
            setCreationDate(d);
            setLastModified(d);
            out.writeObject(this);
//            writeExternal(out);
            out.flush();
            out.close();
            resetModifiedFlag();
/*            isModified = false;
            for (int i=0; i<Tables.size(); i++)
                ((Table) Tables.get(i)).isModified = false;
*/
            newDatabase = false;
        }catch (IOException e) {
            e.printStackTrace();
            if (titleSetToFileName)
                this._setTitle(null);
            dbfile = previousFile;
            throw new UnableToSaveException(resources.getString("CDatabaseMsg36") + title + resources.getString("CDatabaseMsg37") + file.getPath() + "\"");
        }
    }


    public static String getFileNameFromPath(String path, boolean trimExtension) {
        if (path == null)
            return null;

        String fileSeparator = System.getProperty("file.separator");
        int index = path.lastIndexOf(fileSeparator);
        if (index == -1)
            index = path.lastIndexOf('/'); // For URLs

        if (index != -1 && (index + 1) < path.length()) {
            if (trimExtension) {
                String str = path.substring(index+1);
                int extIndex = str.indexOf('.');
                if (extIndex != -1)
                    return str.substring(0, extIndex);
                else
                    return str;
            }else
                return path.substring(index+1);
        }
        return null;
    }


    /**
     *  Creates a new Table instance, adds it to the DBase and activates it.
     */
    public void newTable() throws InsufficientPrivilegesException {
        if (!tableAdditionAllowed)
            throw new InsufficientPrivilegesException(resources.getString("CDatabaseMsg93") + databaseCreatorName
            + " " + databaseCreatorSurname + resources.getString("CDatabaseMsg94"));

        Table table = new Table();
//        Tables.add(table);
//        activeTable = table;
//        _activateTable(Tables.size()-1);
        addTableHandleToDBaseHandle(table);
        try{
//            table.getESlateHandle().setESlateMicroworld(handle.getESlateMicroworld());
            tableImportPlug.connectPlug(table.getTablePlug());
        }catch (PlugNotConnectedException exc) {
            System.out.println("Cannot import the table in the DBase 1");
        }
/*        try{
            _addTable(table, null, false, true);
        }catch (InvalidTitleException e) {}
*/
        setModified();
        newDatabase = false;
//        fireTableAdded(new TableAddedEvent(this, table));
    }


    /**
     *  Saves the active Table of the DBase. If the Table is saved for the first time
     *  the user is prompted for the name of the file. An absolute or relative path may be specified
     *  or just the name of the file, if it should be stored in the working directory. If the
     *  table has not been modified since the last time it was saved, then it is not saved. The
     *  Table's file receives a default ".ctb" extension.
     *  @return <i>true</i>, if the table is saved succesfully. <i>false</i> if the table
     *          need not be saved, because it is not modified, or table saving was cancelled
     *          by the user.
     *  @exception  InvalidPathException If the supplied file name for a new Table
     *              is incorrect.
     *  @exception  UnableToSaveException If some I/O error occurs.
     *  @exception  UnableToOverwriteException If the file specified for a new Table already
     *              exists and is used by some other Table in the same database.
     *  @see #analyzeFileName(java.lang.String, java.lang.String)
     */
/*    public boolean saveTable() throws InvalidPathException, UnableToSaveException,
    UnableToOverwriteException {
        return _saveTable();
    }
*/
/*    private final boolean _saveTable() throws InvalidPathException, UnableToSaveException,
    UnableToOverwriteException {

        File file = activeTable.file;
*/
        /* The title of the FileDialog.
         */
/*        String s1 = null;
//        System.out.println("In saveTable(): activeTable.file = " + activeTable.file);
        if (activeTable.file == null) {
            String title;
            if (activeTable._getTitle() == null)
                title = resources.getString("CDatabaseMsg44");
            else
                title = resources.getString("CDatabaseMsg44") + " \"" + activeTable._getTitle() + "\"";
//            System.out.println("In saveTable(): dialog title = " + title + ", " + fileDialog);
*/
/*            if (fileDialog == null) {
                fileDialog = new FileDialog(new Frame(), title, FileDialog.SAVE);
                fileDialog.setDirectory(System.getProperty("user.dir"));
            }else{
*/
/*                fileDialog.setTitle(title);
                fileDialog.setMode(FileDialog.SAVE);
//            }
            if (activeTable._getTitle() == null)
                fileDialog.setFile("table1.ctb");
            else
                fileDialog.setFile(activeTable._getTitle() + ".ctb");

//            System.out.println("In saveTable(): Showing the dialog:" + fileDialog);
            fileDialog.setVisible(true);
            s1 = fileDialog.getFile();

            if (s1 == null) return false;

//            System.out.println("s1: " + s1);
//            System.out.print("File name: ");
//            String s1 = getStringSTDIN();

            try{
                file = analyzeFileName(fileDialog.getDirectory()+s1, "ctb");
            }catch (InvalidPathException e) {throw new InvalidPathException(e.message);}

            if (file.exists()) {
*/
                /* If the "file" in which the new table will be saved already exists, then
                   if it is currently loaded in some other table in the database, prohibit
                   the user from overwriting it. If not the ask him if he wants to
                   overwrite and proceed accordingly.
                 */
/*                Table existingOpenTable = null;
                String path = file.getPath();
                for (int i=0; i<Tables.size(); i++) {
                    if (((Table) Tables.get(i)).file != null && ((Table) Tables.get(i)).file.getPath().equals(path)) {
                        existingOpenTable = (Table) Tables.get(i);
                        break;
                    }
                }
                if (existingOpenTable != null)
                    throw new UnableToOverwriteException(resources.getString("CDatabaseMsg45") + file.getPath() + resources.getString("CDatabaseMsg46") + existingOpenTable._getTitle() + resources.getString("CDatabaseMsg47"));
                else{
//                    System.out.print("Overwrite existing file? ");
//                    String s2 = getStringSTDIN();
//                    if (!s2.trim().toLowerCase().equals("y"))
                }
            }

        }

//        System.out.println(file.getPath());

        if (activeTable.isModified || activeTable.file == null || !activeTable.file.exists()) {
            File previousFile = null;
            boolean titleSetToFileName = false;
            try{
*/
                /* If the title of the Table is not set, then set it to the name
                 * of the file in which it is stored.
                 */
/*                if (activeTable._getTitle() == null || activeTable._getTitle().trim().length() == 0) {
                    activeTable.setTitle(s1);
                    titleSetToFileName = true;
                }

                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
*/
                /* Set this Table data members here, so as to be stored.
                 */
/*                previousFile = activeTable.file;
                activeTable.isModified = false;
                activeTable.file = file;
                out.writeObject(activeTable);
                out.flush();
                out.close();
                int activeTableIndex = Tables.indexOf(activeTable);
//                System.out.println("activeTableIndex: " + activeTableIndex + ", TableFiles: " + TableFiles);
                TableFiles.put(activeTableIndex, activeTable.file);
            }catch (IOException e) {
//                System.out.println("Error in saveTable(): " + e.getClass().getName() + ", " + e.getMessage());
                e.printStackTrace();
                if (titleSetToFileName)
                    activeTable.setTitle(null);
                activeTable.file = previousFile;
                activeTable.isModified = true;
                throw new UnableToSaveException(resources.getString("CDatabaseMsg48") + activeTable._getTitle() + resources.getString("CDatabaseMsg49") + file.getPath() + "\"");
            }

            return true;
        }else
            return false;
    }
*/

    /**
     *  Loads the Table stored in the specified <i>.ctb</i> file in the DBase and makes it the
     *  active one. If there is a table in the DBase which has the same title as the loaded
     *  Table, then the title of the new Table acquires an extension of the form ';<number>'.
     *  The loaded table becomes the active table of the database.
     *  @param fileName The name of the file.
     *  @exception  InvalidPathException If the supplied file name is incorrect or if the file
     *              does not exist.
     *  @exception  UnableToOpenException If some I/O error occurs or if the specified file does not
     *              contain a valid Table object.
     *  @see #analyzeFileName(java.lang.String, java.lang.String)
     */
    public void loadTable(String fileName) throws InvalidPathException, UnableToOpenException,
    InsufficientPrivilegesException {
//        System.out.println();
//        System.out.println("In openTable()");
        if (!tableAdditionAllowed)
            throw new InsufficientPrivilegesException(resources.getString("CDatabaseMsg93") + databaseCreatorName
            + " " + databaseCreatorSurname + resources.getString("CDatabaseMsg94"));

        Table openedTable;
        File file = null;

        try{
            file = analyzeFileName(fileName, "ctb");
        }catch (InvalidPathException e) {throw new InvalidPathException(e.message);}

        if (!file.exists())
           throw new InvalidPathException(resources.getString("CDatabaseMsg17") + file.getPath() + resources.getString("CDatabaseMsg18"));

        /* Check if the "file" which is about to be opened, is already opened and displayed
         * in some other table in the existing database. If it is, then check if this table
         * is modified, i.e. contains a different version of the table from the one
         * stored in the "file". If this is true, then ask the user if he wants to revert
         * the table's condition to the initial one, which is stored in the "file". If the
         * answer is "y", read the "file" into the "existingOpenTable" and activate this
         * table of the database. If the answer is anything else, then just activate the
         * "existingOpenTable". If the table is not modified then simply activate
         * "existingOpenTable". Finally if the contents of the "file" are not currently
         * loaded to some table in the database, then proceed to read the "file" into a
         * new table, which is added to the database.
         */
/*        String path = file.getPath();
        Table existingOpenTable = null;
        int index = 0;
        for (index=0; index<Tables.size(); index++) {
            if (((Table) Tables.get(index)).file != null && ((Table) Tables.get(index)).file.getPath().equals(path)) {
//                System.out.println("path: " + path + ", index: " + index);
                existingOpenTable = (Table) Tables.get(index);
                break;
            }
        }

        if (existingOpenTable != null) {
            if (existingOpenTable.isModified) {
//                System.out.print("Revert to original table? ");
//                String s2 = getStringSTDIN();
//                if (s2.trim().toLowerCase().equals("y"))
                if ((ESlateOptionPane.showConfirmDialog(new JFrame(), resources.getString("CDatabaseMsg50"), resources.getString("CDatabaseMsg51"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION))
                    try{
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                        existingOpenTable = (Table) in.readObject();
//                        System.out.println("Tables before: " + Tables + " index: " + index);
                        Tables.put(index, existingOpenTable);
//                        System.out.println("Tables after: " + Tables);
                    }catch (Exception e) {
                        e.printStackTrace();
//                        System.out.println("Error--1: "  + e.getClass().getName());
                        throw new UnableToOpenException(resources.getString("CDatabaseMsg52") + file.getPath() + "\"");}
            }
            _activateTable(index);
            return index;
        }
*/
        try{
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file), 20000));
//            openedTable = (CTable) in.readObject();
            Object tableStructure = in.readObject();
            if (CTable.class.isAssignableFrom(tableStructure.getClass()))
                openedTable = new Table((CTable) tableStructure);
            else
                openedTable = (Table) tableStructure;

            addTableHandleToDBaseHandle(openedTable);
            try{
//                openedTable.getESlateHandle().setESlateMicroworld(handle.getESlateMicroworld());
                tableImportPlug.connectPlug(openedTable.getTablePlug());
            }catch (PlugNotConnectedException exc) {
                System.out.println("Cannot import the table in the DBase 2");
            }
/*             try{
                _addTable(openedTable, openedTable.getTitle(), true, true);
             }catch (InvalidTitleException e) {
                System.out.println("Serious inconsistency error in DBase loadTable(): unable to generate unique name for the loaded table");
                openedTable.setTitle(null);
             }
*/
//            activeTable = openedTable;
//            _activateTable(Tables.size()-1);
            /* If the openedTable's "file" variable is different from the file from which the table
             * was opened -which means that this file was moved/renamed by some external means- then
             * synchronize the "file" variable of the table. Also save the table, so that this variable
             * is set persistently. Finally update the "TableFiles" Array of the DBase.
             */
/*            if (openedTable.file != null && !openedTable.file.equals(file)) {
                in.close();
                openedTable.file = file;
                TableFiles.put(TableFiles.size()-1, openedTable.file);
                try{
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                    out.writeObject(openedTable);
                    out.flush();
                    out.close();
                }catch (IOException e) {System.out.println("Serious inconsistency error in DBase openTable() : (1)");}
            }
*/
//            System.out.println("Tables: " + Tables);
        }catch (Exception e) {
            e.printStackTrace();
//            System.out.println("------------------------------ERROR--: "  + e.getClass().getName() + ", " +e.getMessage());
            throw new UnableToOpenException(resources.getString("CDatabaseMsg52") + file.getPath() + resources.getString("CDatabaseMsg53"));
        }

        return;
    }


    /**
     *  Removes the specified Table from the DBase. The user
     *  is asked if the Table should be saved, before it is removed. Unless this table is
     *  the rightmost one in the database, the next table in series is activated. If it is
     *  the rightmost, the previous one is activated.
     *  @param promptToStore Whether the table will be given the chance to be saved before
     *         it is removed from the DBase.
     *  @return <i>true</i>, if the table is closed succesfully. <i>false</i>, if
     *          despite the user's will, the table failed to be saved. In this case the
     *          table is not closed.
     *  @see    #analyzeFileName(java.lang.String, java.lang.String)
     */
    public boolean removeTable(Table table, boolean promptToStore) throws InsufficientPrivilegesException {
//System.out.println("Removing table: " + table);
        if (!databaseTables.contains(table))
            throw new IllegalArgumentException("Cannot remove table " + table.getTitle() + " from the database " + getTitle() + ", because the table does nt belong to it");

        if (!tableRemovalAllowed)
            throw new InsufficientPrivilegesException(resources.getString("CDatabaseMsg93") + databaseCreatorName
            + " " + databaseCreatorSurname + resources.getString("CDatabaseMsg95"));

        if (promptToStore && table.getFieldCount() != 0) {
//            System.out.println(activeTable.getTitle());
//            if (activeTable.isModified || activeTable.file == null || !activeTable.file.exists()) {
//                String s1 = getStringSTDIN();
                String title, msg, s1;
                if (table.getTitle() == null) {
                    title = resources.getString("CDatabaseMsg44");
                    msg = resources.getString("CDatabaseMsg54");
                }else{
                    title = resources.getString("CDatabaseMsg44") + " \"" + table.getTitle() + "\"";
                    msg = resources.getString("CDatabaseMsg55") + table.getTitle() + resources.getString("CDatabaseMsg56");
                }

                Object[] yes_no_cancel = {resources.getString("Yes"), resources.getString("No"), resources.getString("Cancel")};

                JOptionPane pane = new JOptionPane(msg,
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    javax.swing.UIManager.getIcon("OptionPane.questionIcon"),
                    yes_no_cancel,
                    resources.getString("Yes"));
                javax.swing.JDialog dialog = pane.createDialog(new JFrame(), title);
                dialog.show();
                Object option = pane.getValue();

                if (option == resources.getString("Cancel") || option == null)
                    return false;

//                if ((ESlateOptionPane.showConfirmDialog(new JFrame(), msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)) {
                if (option == resources.getString("Yes")) {
                    try {
                        if (fileDialog == null) {
                            fileDialog = getFileDialog();
                            fileDialog.setDirectory(System.getProperty("user.dir"));
                        }
                        fileDialog.setTitle(title);
                        fileDialog.setMode(FileDialog.SAVE);
                        if (table.getTitle() == null)
                            fileDialog.setFile("table1.ctb");
                        else
                            fileDialog.setFile(table.getTitle() + ".ctb");

                        fileDialog.setDefaultExtension("ctb");
                        fileDialog.setVisible(true);
                        s1 = fileDialog.getFile();

                        if (s1 != null)
                            exportTable(table, fileDialog.getDirectory()+s1, true);
                        else
                            return false;
                    }catch (InvalidPathException e) {
//                        System.out.println(e.message);
                        return false;
                    }
                     catch (UnableToSaveException e) {
//                        System.out.println(e.message);
                        return false;
                     }
                }
            //}
        }
        int index = databaseTables.indexOf(table);
//System.out.println("Removing index: " + index); // + " activeTable: " + activeTable);
        fireTableRemoved(new TableRemovedEvent(this, (Table) databaseTables.get(index), index));
        table.removeVetoableChangeListener(tableVetoListener);
        databaseTables.remove(index);
        if (handle != null && !handle.isDisposed()) {
            ESlateHandle tableHandle = table.getESlateHandle();
            if (!tableHandle.isDisposed() && tableImportPlug.isConnected(tableHandle.getPlugs()[0])) {
                try{
                    tableBeingRemoved = true;
System.out.println("Disconnecting table plug tableHandle: " + tableHandle + ", tableHandle.getPlugs()[0]: " + tableHandle.getPlugs()[0]);
System.out.println("tableImportPlug.isConnected(tableHandle.getPlugs()[0]: " + tableImportPlug.isConnected(tableHandle.getPlugs()[0]));
                    tableImportPlug.disconnectPlug(tableHandle.getPlugs()[0]);
                    tableBeingRemoved = false;
                }catch (PlugNotConnectedException exc) {
                    tableBeingRemoved = false;
//                exc.printStackTrace();
                }
            }
/*            if (tableHandle.getParentHandle() == handle) {
                handle.remove(tableHandle);
                if (tableHandle.getParentHandle() == null && !isDesktopComponent(tableHandle)) {
                    tableHandle.toBeDisposed(false, new BooleanWrapper(false));
                    tableHandle.dispose();
                }
            }
*/
        }
//        TableFiles.remove(index);

//        System.out.println("In closeTable: Tables = " + Tables);
//        System.out.println("In closeTable: TableFiles = " + TableFiles);
        if (activeTable == table) {
            if (databaseTables.isEmpty()) {
                activeTable = null;
                activeTableIndex = -1;
            }else if (databaseTables.size() == 1) {
    //            activeTable = (Table) Tables.get(0);
                _activateTable(0);
            }else{
                if (index == databaseTables.size()) {
    //                activeTable = (Table) Tables.get(index-1);
                    _activateTable(index-1);
                }else{
    //                activeTable = (Table) Tables.get(index);
                    _activateTable(index);
                }
            }
        }

/*System.out.print("After removal table names: " );
Array tableNames = getTableTitles();
for (int i=0; i<tableNames.size(); i++)
System.out.print(tableNames.get(i) + ", ");
System.out.println();
*/
        setModified();
        newDatabase = false;
        return true;
    }


    /**
     *  Exports the specified Table to the specified <i>.ctb</i> file.
     *  @param fileName The name of the file.
     *  @return <i>true</i>, if the table is saved succesfully. <i>false</i>, if the operation
     *          was cancelled by the user.
     *  @exception  InvalidPathException If the supplied file name is incorrect.
     *  @exception  UnableToSaveException If the file cannot be writen because it is in use,
     *              or some I/O problem occurs.
     *  @see    #analyzeFileName(java.lang.String, java.lang.String)
     */
    public boolean exportTable(Table table, String fileName, boolean noOverwriteConfirmation)
    throws InvalidPathException, UnableToSaveException, InsufficientPrivilegesException {
        if (!tableExportationAllowed)
            throw new InsufficientPrivilegesException(resources.getString("CDatabaseMsg93") + databaseCreatorName
            + " " + databaseCreatorSurname + resources.getString("CDatabaseMsg96"));

        table.exportTable(fileName, noOverwriteConfirmation);
        return true;
    }


    /**
     *  Joins the active Table with another Table. The result is a new Table, which
     *  is stored in the database of the first table. This Table becomes active.
     *  @param table The table to which the active table is joined.
     *  @return <i>true</i>, if the Join operation succeeds. <i>false</i>, if
     *          <i>table</i> is <i>null</i>, or the active table itself.
     *  @exception  JoinNoCommonFieldsException If no fields with common names and data types
     *              exist between the two tables.
     *  @exception  JoinUnableToPerformException If any of the common fields is of <b>Sound</b>
     *              data type. Join on <b>Sound</b> fields is not supported.
     */
    public final boolean join(Table table) throws JoinNoCommonFieldsException,
    JoinUnableToPerformException {

        /* Don't join the active table with itself.
         */
        if (table == null || table.equals(activeTable))
            return false;

        ArrayList fieldsToBeRenamed = new ArrayList();
        /* Find the fields with common names in the tables to be joined.
         * The names of these fields are stored in "commonFields" Array.
         */
        ArrayList commonFields = activeTable.equalFieldNames(table);
//        System.out.println(commonFields);

        /* Exclude from the "commonFields" Set those fields which exist in both
         * table, but do not have the same data type. Also exclude all the fields
         * with common names but whose data type is "Image".
         * For the rest of the fields find and insert in the "equalComparators"
         * Array the BinaryPredicate(s) that will be used to compare the records
         * to be joined.
         */
         AbstractTableField f1;
         AbstractTableField f2;
         ArrayList equalComparators = new ArrayList();
         for (int k=0; k<commonFields.size(); k++) {
            String s = (String) commonFields.get(k);
            try{
                f1 = activeTable.getTableField(s);
                f2 = table.getTableField(s);
            }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in Database join(): (1)"); return false;}
            if (!f1.hasEqualType(f2) || f1.getDataType().equals(CImageIcon.class) || f2.getDataType().equals(CImageIcon.class)) {
                /* Field f2 has to be renamed in order to be inserted in the new table.
                 * This happens because the intersection table cannot contain two fields
                 * with the same name.
                 */
                fieldsToBeRenamed.add(f2.getName());
                commonFields.remove(k);
                k--;
            }else{
                equalComparators.add(f1.getComparatorFor("="));
//                equalComparators.add(((DataTypeComparators) dataTypeComparators.get(f1.getDataType().getName())).getEqualComparator());
            }
         }
         f1 = f2 = null;
//         System.out.println("CommonFields: " + commonFields);
//         System.out.println("fieldsToBeRenamed: " + fieldsToBeRenamed);

        if (commonFields.isEmpty())
            throw new JoinNoCommonFieldsException(resources.getString("CDatabaseMsg59") + activeTable.getTitle() + resources.getString("CDatabaseMsg60") + table.getTitle() + resources.getString("CDatabaseMsg61"));

        /* Get the data of the two fields with the same name and type in the
         * two tables to be joined. Initially we join the just on
         * one of the common fields, the first one detected in "commonFields" Set,
         * which is not of data type "Sound".
         */
        String firstCommonFieldName = null;
        boolean soundFieldFound = false;
        try{
            for (int i=0; i<commonFields.size(); i++) {
                if (!(activeTable.getTableField((String) commonFields.get(i))).getDataType().getName().equals("Sound field type???")) {
                    firstCommonFieldName = (String) commonFields.get(i);
                    break;
                }
                soundFieldFound = true;
            }
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error at CDatabase join(): (6)"); return false;}

        if (soundFieldFound || firstCommonFieldName == null)
            throw new JoinUnableToPerformException(resources.getString("CDatabaseMsg62") + activeTable.getTitle() + resources.getString("CDatabaseMsg60") + table.getTitle() + resources.getString("CDatabaseMsg63"));

        int firstCommonFieldIndex = 0;
        try{
            firstCommonFieldIndex = activeTable.getFieldIndex(firstCommonFieldName);
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in DBase join(): (2)"); return false;}

        try{
            f1 = activeTable.getTableField(firstCommonFieldName);
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in DBase join(): (3)");return false;}

//1        String className = f1.getDataType().getName();
//1        className = className.substring(className.lastIndexOf('.') + 1);
        ObjectComparator comparator = null;
        Object valueToReplaceNull;
        if (f1.getDataType().equals(IntegerTableField.DATA_TYPE)) {
//            try{
                comparator = IntegerTableField.getComparator(">"); // new GreaterNumber(java.lang.Class.forName("java.lang.Integer"));
//            }catch (ClassNotFoundException e) {}
            valueToReplaceNull = INTEGER_FOR_NULL_VALUE;
        }else if (f1.getDataType().equals(DoubleTableField.DATA_TYPE)) {
            comparator = DoubleTableField.getComparator(">"); // new GreaterNumber(java.lang.Class.forName("java.lang.Double"));
            valueToReplaceNull = DOUBLE_FOR_NULL_VALUE;
        }else if (f1.getDataType().equals(FloatTableField.DATA_TYPE)) {
            comparator = FloatTableField.getComparator(">"); // new GreaterNumber(java.lang.Class.forName("java.lang.Double"));
            valueToReplaceNull = FLOAT_FOR_NULL_VALUE;
        }else if (f1.getDataType().equals(StringTableField.DATA_TYPE)) {
            comparator = StringTableField.getComparator(">"); //new GreaterString();
            valueToReplaceNull = STRING_FOR_NULL_VALUE;
        }else if (f1.getDataType().equals(BooleanTableField.DATA_TYPE)) {
            comparator = StringTableField.getComparator(">"); //new GreaterString();
            valueToReplaceNull = BOOLEAN_FOR_NULL_VALUE;
        }else if (f1.getDataType().equals(URLTableField.DATA_TYPE)) {
            comparator = StringTableField.getComparator(">"); //new GreaterString();
            valueToReplaceNull = URL_FOR_NULL_VALUE;
        }else if (f1.getDataType().equals(DateTableField.DATA_TYPE)) {
            comparator = DateTableField.getComparator(">"); //new GreaterDate();
            valueToReplaceNull = DATE_FOR_NULL_VALUE;
        }else if (f1.getDataType().equals(TimeTableField.DATA_TYPE)) {
            comparator = TimeTableField.getComparator(">"); //new GreaterDate();
            valueToReplaceNull = TIME_FOR_NULL_VALUE;
        }else if (f1.getDataType().equals(ImageTableField.DATA_TYPE)) {
            comparator = ImageTableField.getComparator(">"); //new GreaterHashComparator();
            valueToReplaceNull = IMAGE_FOR_NULL_VALUE;
        }else{
            System.out.println("Serious inconsistency error in DBase join(): (5)");
            return false;
        }

/*        ArrayList field1 = new ArrayList(), field2, recIndices1 = new ArrayList(), recIndices2 = new ArrayList();
        try {
//            field1 = activeTable.getField(firstCommonFieldName);
//            field2 = table.getField(firstCommonFieldName);
            field1 = activeTable.tableFields.get(firstCommonFieldIndex).getDataArrayList();
            field2 = table.tableFields.get(firstCommonFieldIndex).getDataArrayList();
        }catch (Throwable thr) {System.out.println("Serious inconsistency error in DBase join(): (2)"); return false;}
*/
        /* Filter the two Arrays and substitute the null values with
         * predefined values for each data type. This way we avoid
         * program crashing, when BinaryPredicate(s) execute.
         */
/*        ArrayList fld1Clone = (ArrayList) field1.clone();
        ArrayList fld2Clone = (ArrayList) field2.clone();
        field1 = field2 = null;
        for (int i=0; i<fld1Clone.size(); i++) {
            if (fld1Clone.get(i) == null)
                fld1Clone.set(i, valueToReplaceNull);
        }
        for (int i=0; i<fld2Clone.size(); i++) {
            if (fld2Clone.get(i) == null)
                fld2Clone.set(i, valueToReplaceNull);
        }
*/        /* Create two new Arrays, one for each field's data Array. In each
         * such Array store the record index of for each value of the field.
         */
/*        recIndices1.ensureCapacity(fld1Clone.size());
        recIndices2.ensureCapacity(fld2Clone.size());
        for (int i=0; i<fld1Clone.size(); i++)
            recIndices1.add(new Integer(i));
        for (int i=0; i<fld2Clone.size(); i++)
            recIndices2.add(new Integer(i));

//        System.out.println(recIndices1);
//        System.out.println(recIndices2);
*/

        /* Use this special version of Sorting.sort (i.e. Mysorting.sort) in order
         * to sort the two index Arrays ("recIndices1" and "recIndices2") based on the
         * values of the "fld1Clone" and "fld2Clone" Arrays.
         * We do this because Arrays "fld1Clone" and "fld2Clone" have to be sorted in order
         * for the join arlgorithm, that follows, to function properly. Also
         * this way the intesection will give us the indices of the records that will
         * finally be joined, instead of just the common values between the two Arrays.
         */
///        MyArrayListSorting.sort(fld1Clone, comparator, recIndices1);
///        MyArrayListSorting.sort(fld2Clone, comparator, recIndices2);
        IntBaseArray recIndices1 = f1.getOrder(comparator);
        IntBaseArray recIndices2 = f2.getOrder(comparator);

        /* Store in Arrays "record1" and "record2" the result of the join
         * of Arrays "fld1Clone" and "fld1Clone". This result doesn't consist of the common
         * values between "fld1" and "fld2Clone", but the indices of the records for
         * which common values exist in "fld1Clone" and "fld2Clone". So in the resulting
         * Arrays it is guaranteed that the record of "activeTable" whose index is
         * stored at position A of "record1", has the same value with the record of
         * "table" whose index is stored at position A of "record2", on the common
         * field. So the two records whose indices are stored at position A in Arrays
         * "record1" and "record2" respectively, will be joined. The following
         * algorithm covers 1-1, 1-N, N-1 and M-N relations.
         */
        int index1=0, index2 = 0;
        ArrayList record1 = new ArrayList(), record2 = new ArrayList();

//        while (index1<fld1Clone.size() && index2<fld2Clone.size()) {
        while (index1<f1.size() && index2<f2.size()) {
            if (f1.compareTo(recIndices1.get(index1), comparator, f2, recIndices2.get(index2))) {
//            if ( comparator.execute( fld1Clone.get(index1), fld2Clone.get(index2) ) ) {
                index1++;
            }else if (f2.compareTo(recIndices2.get(index2), comparator, f1, recIndices1.get(index1))) {
//            }else if ( comparator.execute( fld2Clone.get(index2), fld1Clone.get(index1))) {
                index2++;
            }else{
                record1.add(new Integer(recIndices1.get(index1)));
                record2.add(new Integer(recIndices2.get(index2)));
//                Object o = fld2Clone.get(index2);
                Object o = f2.getObjectAt(recIndices2.get(index2));
                index2++;
                int temp2 = index2;
                while (index2 < f2.size() && f2.getObjectAt(recIndices2.get(index2)).equals(o)) {
//                while (index2 < fld2Clone.size() && fld2Clone.get(index2).equals(o)) {
                    record1.add(new Integer(recIndices1.get(index1)));
                    record2.add(new Integer(recIndices2.get(index2)));
                    index2++;
                }
                index2 = temp2;
                if ((index1+1) < f1.size() && f1.getObjectAt(recIndices1.get(index1)).equals(f1.getObjectAt(recIndices1.get(index1+1))))
//                if ((index1+1) < fld1Clone.size() && fld1Clone.get(index1).equals(fld1Clone.get(index1+1)))
                    index2--;
                index1++;
            }
        }
        recIndices1 = recIndices2 = null;
//        fld1Clone = fld2Clone = null;

        /* Create the resulting Table. Add all the fields of the active table
         * to this new Table. Add only the fields of the second Table whose
         * names are not contained in the "commonFields" Set to the resulting
         * field. This way common fields are added only once to the resulting Table.
         */
        Table result = new Table();
        TableFieldBaseArray fieldsOfActiveTable = activeTable.tableFields;
        for (int i=0; i<fieldsOfActiveTable.size(); i++) {
            AbstractTableField f = fieldsOfActiveTable.get(i);

            /*Date and Time fields use the same underlying Java data type
             *which is "java.util.Date". So in order to add a Time or Date
             *field to the new Table from the ones which got joined
             *we have to query this field on is isDate member.
             */
//t            if (f.getFieldType().getName().equals("java.util.Date")) {
//t                if (!f.isDate()) {
            if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CTime")) {
                try{
                    result._addField(f.getName(), CTime.class/*"time"*/, true, true, f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }else if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) {
                try{
                    result._addField(f.getName(), CDate.class/*"date"*/, true, true, f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }else if (f.getDataType().equals(CImageIcon.class)) {
                    try{
                        result._addField(f.getName(), CImageIcon.class/*"Image"*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
            }else{
                try{
                    result._addField(f.getName(), f.getDataType()/*.toString().substring( f.getFieldType().toString().lastIndexOf('.')+1 )*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
            }
        }
        fieldsOfActiveTable = null;

        StringBaseArray existingNames = result.getFieldNames();
        TableFieldBaseArray fieldsOfTable = table.tableFields;
        /* "secondTableFieldIndices" is used later on to add the records to
         *  the new Table.
         */
        ArrayList secondTableFieldIndices = new ArrayList();
        for (int i=0; i<fieldsOfTable.size(); i++) {
            AbstractTableField f = fieldsOfTable.get(i);
            String fieldName = f.getName();
            if (commonFields.indexOf(fieldName) == -1) {
                /* If this field's name is equal to the name of one of the fields in
                 * "activeTable", then add the title of the "table" as prefix to this
                 * field's name. This way firstly the field will be defined in the
                 * resulting table "result", which cannot hold multiple fields with the
                 * same name, and secondly the user will be able to distinguish the
                 * origin of the fields with the same name.
                 */
                if (fieldsToBeRenamed.indexOf(fieldName) != -1)
                    fieldName = table.getTitle() + ":" + fieldName;
                int count = 0;
                for (int m=0; m<existingNames.size(); m++) {
                    String existingName = (String) existingNames.get(m);
                    int indx = existingName.indexOf(fieldName);
                    if (indx == 0)
                      count++;
                }
                if (count != 0)
                    fieldName = fieldName + ";" + (count+1);

//t                if (f.getFieldType().getName().equals("java.util.Date")) {
//t                    if (!f.isDate()) {
                if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CTime")) {
                    try{
                        result._addField(fieldName, CTime.class/*"time"*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
                }else if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) {
                    try{
                        result._addField(fieldName, CDate.class/*"date"*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
                }else if (f.getDataType().equals(CImageIcon.class)) {
                        try{
                            result._addField(fieldName, CImageIcon.class/*"Image"*/, true, true, f.isHidden(), true);
                        }catch (InvalidFieldNameException e) {}
                         catch (InvalidKeyFieldException e) {}
                         catch (InvalidFieldTypeException e) {}
                         catch (AttributeLockedException e) {}
                }else{
                    try{
                        result._addField(fieldName, f.getDataType()/*.toString().substring( f.getFieldType().toString().lastIndexOf('.')+1 )*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
                }
                secondTableFieldIndices.add(new Integer(i));
            }
        }
        fieldsOfTable = null;

        /* If the common fields among the two tables, to be joined, are more
         * than one, then we have to check the rest of the common fields (i.e.
         * beyond the first, upon which the join above took place) to see
         * if the pairs of matching on the first field records in Arrays "record1"
         * and "record2", do match on the rest of the fields too. These pairs for
         * which this is false, are removed from "record1" and "record2" arrays.
         */
        boolean different;
        OrderedSetIterator iter2;
        int field1Index, field2Index;

//        System.out.println(record1);
//        System.out.println(record2);

        if (commonFields.size() > 1) {
            int n = commonFields.indexOf(firstCommonFieldName);
            /* Remove the field whic was used in the intersection above from
             * "commonFields" Array. Remove the corresponding equal comparator
             * from "equalComparators" Array.
             */
            commonFields.remove(n);
            equalComparators.remove(n);
            Object o1, o2;
            for (int i=0; i<record1.size(); i++) {
//                iter2 = commonFields.begin();
//                iter2.advance();
                different = false;
                for (int k=0; k<commonFields.size(); k++) {
//                while (iter2.hasMoreElements()) {
                    String fName = (String) commonFields.get(k);
                    try{
                        field1Index = activeTable.getFieldIndex(fName);
                        field2Index = table.getFieldIndex(fName);
                        o1 = activeTable.riskyGetCell(field1Index, ((Integer) record1.get(i)).intValue());
                        o2 = table.riskyGetCell(field2Index, ((Integer) record2.get(i)).intValue());
                        if (o1 == null || o2 == null) {
                            if (o1 == o2)
                                continue;
                            else{
                                different = true;
                                break;
                            }
                        }
                        if (!((BinaryPredicate) equalComparators.get(k)).execute(o1, o2)) {
                            different = true;
                            break;
                        }
                    }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in Database join(): (4)");}
                }
                if (different) {
                    record1.remove(i);
                    record2.remove(i);
                    i--;
                }
            }//for
        }

        /* Insert the records formed by the pairs of record indices in arrays
         * "record1" and "record2" into the "result" Table.
         */
        ArrayList recordEntryForm = new ArrayList();
        for (int i=0; i<record1.size(); i++) {
            for (int k=0; k<activeTable.getFieldCount(); k++)
                recordEntryForm.add(activeTable.riskyGetCell(k, ((Integer) record1.get(i)).intValue()));
            for (int k=0; k<secondTableFieldIndices.size(); k++) {
                /* "secondTableFieldIndices" is used to avoid using values of the common
                 * fields in the second table.
                 */
                int fieldIndex = ((Integer) secondTableFieldIndices.get(k)).intValue();
                recordEntryForm.add(table.riskyGetCell(fieldIndex, ((Integer) record2.get(i)).intValue()));
            }

            try {
//                if (!result.addRecord(recordEntryForm))
                result.addRecord(recordEntryForm, false);
                recordEntryForm.clear();
            }catch (TableNotExpandableException e) {System.out.println(e.message); recordEntryForm.clear();}
             catch (NoFieldsInTableException e) {System.out.println(e.message);recordEntryForm.clear();}
             catch (DuplicateKeyException e) {System.out.println(e.message);recordEntryForm.clear();}
             catch (NullTableKeyException e) {System.out.println(e.message);recordEntryForm.clear();}
             catch (InvalidDataFormatException e) {System.out.println(e.message);recordEntryForm.clear();}
        }

        /* Set the title of the new table to "Join: <table1>-<table2>.
         * Add the table to this database and make it the active one.
         */
        try{
            String title = resources.getString("CDatabaseMsg64") + activeTable.getTitle() + "-" + table.getTitle() + ")";
            addTableHandleToDBaseHandle(result);
//            result.getESlateHandle().setESlateMicroworld(handle.getESlateMicroworld());
            tableImportPlug.connectPlug(result.getTablePlug());
        }catch (PlugNotConnectedException exc) {
            System.out.println("Cannot add Table to the DBase 3");
        }
//            _addTable(result, title, true, true);
//        }catch (InvalidTitleException e) {
            //this normally never occurs
//            System.out.println("Serious inconsistency error in DBase join() : the title given to the result table already exists");
//        }
//        activeTable = result;
//        _activateTable(Tables.size()-1);

        return true;
    }


    /**
         *  Performs the th-Join operation between the active Table and the specified one. The
         *  resulting Table is saved in the same database as the active Table and becomes the
         *  active one. th-join is based on relational expressions between pairs of fields from
         *  the two tables.
         *  @param table The second table.
         *  @param fields1 An Array of field names from the active table.
         *  @param Operators An Array of relational operators, e.g. <, >=, !=, contains, ...
         *  @param fields2 An Array of field names from the second table.
         *  @return <i>true</i>, if the operation is successful. <i>false</i>, if:
         *          <ul>
         *          <li> The specified table is <i>null</i>.
         *          <li> The active table is joined to itself.
         *          <li> Arrays <i>fields1</i> and <i>fields2</i> contain anything but strings.
         *          </ul>
         *
         *  @exception  JoinMissingFieldException If Arrays <i>fields1</i> and <i>fields2</i>
         *              do not contain the same number of field names.
         *  @exception  JoinMissingFieldOrOperatorException If there are less operators than
         *              fields or less fields than operators, or any of the three input Arrays
         *              is <i>null</i>.
         *  @exception  InvalidFieldNameException If a field name in Array <i>fields1</i> does
         *              not correspond to a field in the active table, or a field name in
         *              <i>fields2</i> does not correspond to a field in the second table.
         *  @exception  JoinIncompatibleFieldTypesException If the fields in one of the
         *              relational expressions have incompatible data types.
         *  @exception  InvalidOperatorException If the relational comparator used between two
         *              fields in a pair of the th-join expression is invalid, i.e. cannot be
         *              used for fields of this data type.
         *  @exception  JoinUnableToPerformException If any of the Arrays <i>fields1</i> or
         *              <i>fields2</i> is empty, or contains one or more elements of data type
         *              other than <i>java.lang.String</i>.
         */
    /* th-join() should not be given a condition between fields of type "Sound".
     */
    public final boolean th_join(Table table, StringBaseArray fields1, ArrayList Operators, StringBaseArray fields2)
    throws JoinMissingFieldException, JoinMissingFieldOrOperatorException,
    InvalidFieldNameException, JoinIncompatibleFieldTypesException,
    InvalidOperatorException, JoinUnableToPerformException {

        /* Don't th-join a table with itself.
         */
         if (table == null || table.equals(activeTable))
            return false;

        if (fields1 == null)
            throw new JoinMissingFieldOrOperatorException(resources.getString("CDatabaseMsg65") + activeTable.getTitle() + "\"");
        if (fields2 == null)
            throw new JoinMissingFieldOrOperatorException(resources.getString("CDatabaseMsg65") + table.getTitle() + "\"");
        if (Operators == null)
            throw new JoinMissingFieldOrOperatorException(resources.getString("CDatabaseMsg66"));
        /* Check if the three Arrays contain an equal number of elements.
         */
//        System.out.println("fields1: " + fields1 + ", " + Operators + ", " + fields2);
        if (fields1.size()!= fields2.size())
            throw new JoinMissingFieldException(resources.getString("CDatabaseMsg67"));
        if (fields1.size() != Operators.size())
            throw new JoinMissingFieldOrOperatorException(resources.getString("CDatabaseMsg68"));
        if (fields1.size()==0)
            throw new JoinUnableToPerformException(resources.getString("CDatabaseMsg69") + activeTable.getTitle() + resources.getString("CDatabaseMsg70"));

        /* Check whether all the contents of Arrays "fields1", "Operators" and "fields2" are of
         * String data type.
         */
        try{
            String s;
            for (int i=0; i<fields1.size(); i++) {
                s = fields1.get(i);
                if (s == null)
                    throw new JoinUnableToPerformException(resources.getString("CDatabaseMsg71"));
                s = (String) Operators.get(i);
                if (s == null)
                    throw new JoinUnableToPerformException(resources.getString("CDatabaseMsg72"));
                s = fields2.get(i);
                if (s == null)
                    throw new JoinUnableToPerformException(resources.getString("CDatabaseMsg71"));
            }
        }catch (ClassCastException e) {throw new JoinUnableToPerformException(resources.getString("CDatabaseMsg73"));}

        /* Check if the field names contained in Arrays "fields1" and "fields2" exist in tables
         * "activeTable" and "table", respectively. Replace these names with the equivalent
         * fields in the two Arrays.
         */
        TableFieldBaseArray f1 = new TableFieldBaseArray();
        TableFieldBaseArray f2 = new TableFieldBaseArray();
        int k = 0;
        try{
            for (k=0; k<fields1.size(); k++) {
                AbstractTableField f = activeTable.getTableField((String) fields1.get(k));
                f1.add(f);
            }
        }catch (InvalidFieldNameException e) {throw new InvalidFieldNameException(resources.getString("CDatabaseMsg74") + fields1.get(k) + resources.getString("CDatabaseMsg75") + activeTable.getTitle());}
         catch (Exception e) {System.out.println("The Array parameters should contain only Strings"); return false;}

        try{
            for (k=0; k<fields1.size(); k++) {
                AbstractTableField f = table.getTableField((String) fields2.get(k));
                f2.add(f);
            }
        }catch (InvalidFieldNameException e) {throw new InvalidFieldNameException(resources.getString("CDatabaseMsg74") + fields2.get(k) + resources.getString("CDatabaseMsg75") + table.getTitle());}
         catch (Exception e) {System.out.println("The Array parameters should contain only Strings"); return false;}

        /* Check if the corresponding fields in the two Arrays have identical data types.
         */
        for (int i=0; i<fields1.size(); i++) {
//            System.out.println(((TableField) fields1.get(i)).getFieldType().getName() + ", " + ((TableField) fields2.get(i)).getFieldType().getName());
            if (f1.get(i).getDataType().getName().equals(f2.get(i).getDataType().getName()))
//t            && ((TableField) fields1.get(i)).isDate()==((TableField) fields2.get(i)).isDate())
                continue;

            throw new JoinIncompatibleFieldTypesException(resources.getString("CDatabaseMsg76") + f1.get(i).getName() + resources.getString("CDatabaseMsg77") + f2.get(i).getName() + resources.getString("CDatabaseMsg78"));
        }

        /* Check if the supplied operators in the "Operators" list match the
         * types of the fields they will act on. If they do, then replace
         * these operators in their "Operators" list with the corresponding
         * function objects, which are looked up at the global Database field
         * "operators".
         */
//        HashMapIterator validOperatorsIter;
//        HashMap validOperators;
        String Operator;
        ObjectComparator comparator = null;

        String firstOperator = (String) Operators.get(0);
        Object valueToReplaceNull = null;
        for (int i=0; i<Operators.size(); i++) {
            Operator = (String) Operators.get(i);

            comparator = f1.get(i).getComparatorFor(Operator);
//            validOperatorsIter = operators.find(f1.get(i).getDataType().getName());
//            validOperators = (HashMap) validOperatorsIter.value();

//1            String className = f1.get(i).getDataType().getName();
//1            className = className.substring(className.lastIndexOf('.') + 1);
/*1            if (className.equals("CImageIcon"))
                className = "Image";
            else if (className.equals("CDate"))
                className = "Date";
            else if (className.equals("CTime"))
                className = "Time";
1*/
            if (comparator == null) {
//            if ((validOperators.count(Operator)) == 0) {
/*t                if (className.equals("Date")) {
                    if (!((TableField) fields1.get(i)).isDate())
                        className = "Time";
                }
t*/
                throw new InvalidOperatorException(resources.getString("CDatabaseMsg79") + Operator + resources.getString("CDatabaseMsg80") + f1.get(i).getLocalizedDataTypeName() + resources.getString("CDatabaseMsg81"));
            }else{
/* Find the function object (e.g. LessNumber()) associated with
                 * this comparator and replace with it the comparator in the
                 * "operators" Array.
                 */
                 if (i==0) {
                    /* The "Operator" of the first th-join condition is not substituted
                     * by the proper BinaryPredicate for the fields' data type. The
                     * BinaryPredicate which is used is different from the proper one
                     * for operators = and !=. For the rest of the operators it's the proper.
                     * This has to do with the th-join algorithm.
                     */
                    if (Operator.equals("=")) {
                        if (f1.get(i).getDataType() == IntegerTableField.DATA_TYPE) {
//                            try{
                                Operators.set(i, IntegerTableField.getComparator(">")); //new GreaterNumber(java.lang.Class.forName("java.lang.Integer")));
//                            }catch (ClassNotFoundException e) {}
                        }else if (f1.get(i).getDataType() == DoubleTableField.DATA_TYPE) {
                            Operators.set(i, DoubleTableField.getComparator(">")); //new GreaterNumber(java.lang.Class.forName("java.lang.Double")));
                        }else if (f1.get(i).getDataType() == FloatTableField.DATA_TYPE) {
                            Operators.set(i, FloatTableField.getComparator(">")); //new GreaterNumber(java.lang.Class.forName("java.lang.Double")));
                        }else if (f1.get(i).getDataType() == StringTableField.DATA_TYPE)
                            Operators.set(i, StringTableField.getComparator(">")); //new GreaterString());
                        else if (f1.get(i).getDataType() == BooleanTableField.DATA_TYPE)
                            Operators.set(i, StringTableField.getComparator(">")); ///new GreaterString());
                        else if (f1.get(i).getDataType() == StringTableField.DATA_TYPE)
                            Operators.set(i, StringTableField.getComparator(">")); //new GreaterString());
                        else if (f1.get(i).getDataType() == URLTableField.DATA_TYPE)
                            Operators.set(i, StringTableField.getComparator(">")); //new GreaterString());
                        else if (f1.get(i).getDataType() == DateTableField.DATA_TYPE)
                            Operators.set(i, DateTableField.getComparator(">")); //new GreaterDate());
                        else if (f1.get(i).getDataType() == TimeTableField.DATA_TYPE)
                            Operators.set(i, TimeTableField.getComparator(">")); //new GreaterDate());
                        else if (f1.get(i).getDataType() == ImageTableField.DATA_TYPE)
                            Operators.set(i, ImageTableField.getComparator(">")); //new GreaterHashComparator());
                    }else if (Operator.equals("!=")) {
                        if (f1.get(i).getDataType() == IntegerTableField.DATA_TYPE) {
//                            try{
                                Operators.set(i, IntegerTableField.getComparator(">=")); //new GreaterEqualNumber(java.lang.Class.forName("java.lang.Integer")));
//                            }catch (ClassNotFoundException e) {}
                        }else if (f1.get(i).getDataType() == DoubleTableField.DATA_TYPE) {
                                Operators.set(i, DoubleTableField.getComparator(">="));  //new GreaterEqualNumber(java.lang.Class.forName("java.lang.Double")));
                        }else if (f1.get(i).getDataType() == FloatTableField.DATA_TYPE) {
                                Operators.set(i, FloatTableField.getComparator(">="));  //new GreaterEqualNumber(java.lang.Class.forName("java.lang.Double")));
                        }else if (f1.get(i).getDataType() == StringTableField.DATA_TYPE)
                            Operators.set(i, StringTableField.getComparator(">=")); //new GreaterEqualString());
                        else if (f1.get(i).getDataType() == BooleanTableField.DATA_TYPE)
                            Operators.set(i, StringTableField.getComparator(">="));  //new GreaterEqualString());
                        else if (f1.get(i).getDataType() == URLTableField.DATA_TYPE)
                            Operators.set(i, StringTableField.getComparator(">="));  //new GreaterEqualString());
                        else if (f1.get(i).getDataType() == DateTableField.DATA_TYPE)
                            Operators.set(i, DateTableField.getComparator(">=")); //new GreaterEqualDate());
                        else if (f1.get(i).getDataType() == ImageTableField.DATA_TYPE)
                            Operators.set(i, ImageTableField.getComparator(">=")); //new GreaterEqualHashComparator());
                    }else{
//                        validOperatorsIter = validOperators.find(Operator);
//                        Operators.set(i, validOperatorsIter.value());
                        Operators.set(i, comparator);
                    }
                    if (f1.get(i).getDataType() == IntegerTableField.DATA_TYPE)
                        valueToReplaceNull = INTEGER_FOR_NULL_VALUE;
                    else if (f1.get(i).getDataType() == DoubleTableField.DATA_TYPE)
                        valueToReplaceNull = DOUBLE_FOR_NULL_VALUE;
                    else if (f1.get(i).getDataType() == FloatTableField.DATA_TYPE)
                        valueToReplaceNull = FLOAT_FOR_NULL_VALUE;
                    else if (f1.get(i).getDataType() == StringTableField.DATA_TYPE)
                        valueToReplaceNull = STRING_FOR_NULL_VALUE;
                    else if (f1.get(i).getDataType() == BooleanTableField.DATA_TYPE)
                        valueToReplaceNull = BOOLEAN_FOR_NULL_VALUE;
                    else if (f1.get(i).getDataType() == URLTableField.DATA_TYPE)
                        valueToReplaceNull = URL_FOR_NULL_VALUE;
                    else if (f1.get(i).getDataType() == DateTableField.DATA_TYPE)
//t                        if (((TableField) fields1.get(0)).isDate())
                        valueToReplaceNull = DATE_FOR_NULL_VALUE;
                    else if (f1.get(i).getDataType() == TimeTableField.DATA_TYPE)
                        valueToReplaceNull = TIME_FOR_NULL_VALUE;
                    else if (f1.get(i).getDataType() == ImageTableField.DATA_TYPE)
                        valueToReplaceNull = IMAGE_FOR_NULL_VALUE;
                    else{
                        System.out.println("Serious inconsistency error in DBase th_join(): (6)");
                        return false;
                    }

                 }else{
//                    validOperatorsIter = validOperators.find(Operator);
//                    Operators.set(i, validOperatorsIter.value());
                     Operators.set(i, comparator);
                 }
            }
        }
//System.out.println("here 1");
        /* Get the data of the two fields which constitute the first th-join condition.
         */
/*        ArrayList field1, field2, recIndices1 = new ArrayList(), recIndices2 = new ArrayList();
        try {
//            field1 = activeTable.getField(f1.get(0).getName());
//            field2 = table.getField(f2.get(0).getName());
            field1 = f1.get(0).getDataArrayList();
            field2 = f2.get(0).getDataArrayList();
        }catch (Throwable thr) {System.out.println("Serious inconsistency error in DBase th-join(): (2)"); return false;}
*/
        /* Create two new Arrays, one for each field's data Array. In each
         * such Array store the record index of for each value of the field.
         */
/*        recIndices1.ensureCapacity(field1.size());
        recIndices2.ensureCapacity(field2.size());

        for (int i=0; i<field1.size(); i++)
            recIndices1.add(new Integer(i));
        for (int i=0; i<field2.size(); i++)
            recIndices2.add(new Integer(i));
//        System.out.println(field2);
*/
        /* Use this special version of Sorting.sort (i.e. Mysorting.sort) in order
         * to sort the two index Arrays ("recIndices1" and "recIndices2") based on the
         * values of the "field1" and "field2" Arrays. So while sorting the clone of
         * field1, for example, field1 is left intact, while "recIndices1" is sorted
         * acording to the field1's clone.
         * We do this because Arrays "field1" and "field2" have to be sorted in order
         * for the th-join arlgorithm, that follows, to function properly. Also
         * this way th-join will give us the indices of the records that will
         * finally be th-joined, instead of just the common values between the two Arrays.
         */
//System.out.println("here 2");
//        System.out.println(field1);
/*        ArrayList fld1 = (ArrayList) field1.clone();
        ArrayList fld2 = (ArrayList) field2.clone();
        BinaryPredicate comparator = (BinaryPredicate) Operators.get(0);

//        System.out.println("valueToReplaceNull: " + valueToReplaceNull);
        if (valueToReplaceNull != null) {
            for (int i=0; i<fld1.size(); i++) {
                if (fld1.get(i) == null)
                    fld1.set(i, valueToReplaceNull);
            }
            for (int i=0; i<fld2.size(); i++) {
                if (fld2.get(i) == null)
                    fld2.set(i, valueToReplaceNull);
            }
        }
*/
/*Kollaei edw. Test case:
J, 1, id, > id
  mexri o pinakas apotelesma na exei 519 records.
J, 1, id <= id Edw kollaei mesa sthn quickSortLoop()
Pinakes:
(1)
        first.addField("id", "integer");
        first.addField("b", "boolean");
        first.addField("time", "Double");
        first.addField("surname", "string");
        first.addField("hour", "time");

        String[][] testData2 = {
            {"1", "2", "4", "3"},
            {"false", "true", "false", "true"},
            {"44.24", "120.1", "0.2", "-102.001"},
            {"you", "g", "everybody", "manolis"},
            {"2:20", "22:50", "1:15", "00:10"}
        };

        Array a = new Array();
        try{
            for (int k=0; k<4; k++) {
//                Array a = new Array();
                for (int l=0; l<5; l++)
                    a.add(testData2[l][k]);
                first.addRecord(a);
            }
        }catch (TableNotExpandableException e) {System.out.println(e.message);}
         catch (NoFieldsInTableException e) {System.out.println(e.message);}
         catch (DuplicateKeyException e) {System.out.println(e.message);}
         catch (NullTableKeyException e) {System.out.println(e.message);}
         catch (InvalidDataFormatException e) {System.out.println(e.message);}
(2)
        String[][] testData = {
            {"manolis", "manalis", "vasilis", "aggeliki", "Xrusanthi", "george"},
            {"5", "4", "2", "1", "0", "4"},
            {"2.3", "0.2", "-20.1", "44.24", "2.312", "33.21"},
            {"true", "false", "true", "false", "false", "true"},
            {"1/1/1995", "2/3/1998", "3/1/1995", "4/8/2010", "30/7/1999", "1/9/1993"},
            {"2:20", "3:20", "1:15", "2:10", "20:25", "00:24"},
            {"http://1/1", "http://2/2", "http://3/3","http://4/4","http://5/5","http://6/6"},
            {"thanasis", "ilias", "basw", "petros", "Xrusanthi", "george"},
            {"0", "5", "8", "13", "24", "-5"},
            {"2.3", "2.25", "20.1", "4.24", "2312", "332.1"},
            {"true", "true", "false", "true", "false", "true"},
            {"1/1/1995", "20/5/1998", "13/12/1980", "24/8/2100", "30/7/1999", "1/9/1995"},
            {"20:20", "13:20", "12:15", "12:20", "20:25", "10:24"},
            {"http://1/1", "http://20/20", "http://30/30","http://40/40","http://5/5","http://george/60"}
        };
        second.addField("name", "string");
        second.addField("id", "integer");
        second.addField("salary", "Double");
        second.addField("male?", "boolean");
        second.addField("date", "date");
        second.addField("time", "time");
        second.addField("url", "url");

        try{
            for (int k=0; k<6; k++) {
                for (int l=0; l<7; l++)
                    a.add(testData[l][k]);
                second.addRecord(a);
            }
        }catch (TableNotExpandableException e) {System.out.println(e.message);}
         catch (NoFieldsInTableException e) {System.out.println(e.message);}
         catch (DuplicateKeyException e) {System.out.println(e.message);}
         catch (NullTableKeyException e) {System.out.println(e.message);}
         catch (InvalidDataFormatException e) {System.out.println(e.message);}
*/
///        MyArrayListSorting.sort( fld1, comparator, recIndices1);
///        MyArrayListSorting.sort(fld2, comparator, recIndices2);
        comparator = (ObjectComparator) Operators.get(0);
        AbstractTableField fld1 = f1.get(0);
        AbstractTableField fld2 = f2.get(0);
        IntBaseArray recIndices1 = fld1.getOrder(comparator);
        IntBaseArray recIndices2 = fld2.getOrder(comparator);

//System.out.println("here 3");
/*        for (int i =0; i<recIndices1.size(); i++)
            System.out.println("1: " + field1.get(((Integer) recIndices1.get(i)).intValue()));// + ", " + recIndices1.get(i));
        for (int i =0; i<field2.size(); i++)
           System.out.println("2: " + field2.get(((Integer) recIndices2.get(i)).intValue()));//field2.get(i) + ", " + recIndices2.get(i));
*/

/*TH-JOIN algorithm main part.
         * Store in Arrays "record1" and "record2" the result of the intersection
         * of Arrays "field1" and "field2". This isn't the common intersection which
         * is based on equal values (oprator "="). It can be based on any of the
         * operators =, !=, contains, contained, <, >, <=, >=.  The resulting Arrays
         * "record1" and "record2" contain the matching records' indices from tables
         * "activeTable" and "table" respectively. So in the resulting
         * Arrays it is guaranteed that the record of "activeTable" whose index is
         * stored at position A of "record1", satisfies the given comparator with the
         * corresponding value with of the record of "table" whose index is stored
         * at position A of "record2", on the common field. The two records
         * whose indices are stored at position A in Arrays "record1" and "record2"
         * respectively, will be joined, if the rest of the conditions of the
         * th-join operation on other fields are also met by these records.
         */
//System.out.println("here 4");

        int index1=0, index2 = 0;
        ArrayList record1 = new ArrayList(), record2 = new ArrayList();
        if (firstOperator.equals("=")) {
            while (index1<fld1.size() && index2<fld2.size()) {
                if (fld1.getObjectAt(recIndices1.get(index1))==null) {
                    index1++;
                    continue;
                }
                if (fld2.getObjectAt(recIndices2.get(index2))==null) {
                    index2++;
                    continue;
                }
                if (comparator.execute(fld1.getObjectAt(recIndices1.get(index1)), fld2.getObjectAt(recIndices2.get(index2)))) {
                    index1++;
                }else if (comparator.execute(fld2.getObjectAt(recIndices2.get(index2)), fld1.getObjectAt(recIndices1.get(index1)))) {
                    index2++;
                }else{
                    record1.add(new Integer(recIndices1.get(index1)));
                    record2.add(new Integer(recIndices2.get(index2)));
                    Object o = fld2.getObjectAt(recIndices2.get(index2));
                    index2++;
                    int temp2 = index2;
                    while (index2 < fld2.size() && fld2.getObjectAt(recIndices2.get(index2))!=null && fld2.getObjectAt(recIndices2.get(index2)).equals(o)) {
                        record1.add(new Integer(recIndices1.get(index1)));
                        record2.add(new Integer(recIndices2.get(index2)));
                        index2++;
                    }
                    index2 = temp2;
                    if ((index1+1) < fld1.size() && fld1.getObjectAt(recIndices1.get(index1)).equals(fld1.getObjectAt(recIndices1.get(index1+1))))
                        index2--;
                    index1++;
                }
            }
        }else if (firstOperator.equals("!=")) {
            for (index1=0; index1<fld1.size(); index1++) {
                if (fld1.getObjectAt(recIndices1.get(index1)) == null)
                    continue;
                index2=0;
                while (index2<fld2.size() && (fld2.getObjectAt(recIndices2.get(index2)) != null && !comparator.execute(fld1.getObjectAt(recIndices1.get(index1)), fld2.getObjectAt(recIndices2.get(index2))))) {
//                    System.out.println("1--- " +  field1.get(((Integer) recIndices1.get(index1)).intValue()) + " " + field2.get(((Integer) recIndices2.get(index2)).intValue()) );
                    record1.add(new Integer(recIndices1.get(index1)));
                    record2.add(new Integer(recIndices2.get(index2)));
                    index2++;
                }
                while (index2<fld2.size() && (fld2.getObjectAt(recIndices2.get(index2)) == null || fld1.getObjectAt(recIndices1.get(index1)).equals(fld2.getObjectAt(recIndices2.get(index2))))) {
//                    System.out.println("2--- " + field1.get(((Integer) recIndices1.get(index1)).intValue()) + " " + field2.get(((Integer) recIndices2.get(index2)).intValue()) );
                    index2++;
                }
                for (int i=index2; i<fld2.size(); i++) {
                    if (fld2.getObjectAt(recIndices2.get(i)) != null) {
                        record1.add(new Integer(recIndices1.get(index1)));
                        record2.add(new Integer(recIndices2.get(i)));
                    }
//                    System.out.println("Adding: " + recIndices1.get(index1) + " " + recIndices2.get(i));
                }
            }
        }else if (firstOperator.equals(this.resources.getString("contains")) || firstOperator.equals(this.resources.getString("contained"))) {
            for (index1=0; index1<fld1.size(); index1++) {
                for (index2=0; index2<fld2.size(); index2++) {
                    if (fld1.getObjectAt(recIndices1.get(index1))!= null &&
                    fld2.getObjectAt(recIndices2.get(index2)) != null &&
                    comparator.execute(fld1.getObjectAt(recIndices1.get(index1)), fld2.getObjectAt(recIndices2.get(index2)))) {
//                        System.out.println(field1.get(((Integer) recIndices1.get(index1)).intValue()) + " " + field2.get(((Integer) recIndices2.get(index2)).intValue()) + ": true");
                        record1.add(new Integer(recIndices1.get(index1)));
                        record2.add(new Integer(recIndices2.get(index2)));
                    }
                }
            }

        }else{ //rest of operators: <,>.>=.<=
            for (index1=0; index1<fld1.size(); index1++) {
                if (fld1.getObjectAt(recIndices1.get(index1)) == null)
                    continue;
                index2=0;
//                System.out.println(field1.get(((Integer) recIndices1.get(index1)).intValue()) + ", " + field2.get(((Integer) recIndices2.get(index2)).intValue()));
                while (index2<fld2.size() && (fld2.getObjectAt(recIndices2.get(index2)) == null || !comparator.execute(fld1.getObjectAt(recIndices1.get(index1)), fld2.getObjectAt(recIndices2.get(index2))))) {
//                    System.out.println( field1.get(((Integer) recIndices1.get(index1)).intValue()) + " " + field2.get(((Integer) recIndices2.get(index2)).intValue()) );
                    index2++;
                }
                for (int i=index2; i<fld2.size(); i++) {
                    record1.add(new Integer(recIndices1.get(index1)));
                    record2.add(new Integer(recIndices2.get(i)));
//                    System.out.println("Adding: " + recIndices1.get(index1) + " " + recIndices2.get(i));
                }
            }
        }
//System.out.println("here 5");

        recIndices1 = recIndices2 = null;
//        field1 = field2 = null;

//        System.out.println(record1);
//        System.out.println(record2);

        /* Substitute the elements of the Arrays "fields1" and "fields2", which are
         * "AbstractTableField"s which belong to tables "activeTable" and "table",
         * respectively, with the indices of these fields in their tables. This
         * information is needed below.
         */
        IntBaseArray fields1Indices = new IntBaseArray();
        IntBaseArray fields2Indices = new IntBaseArray();
        try{
            for (int i=0; i<fields1.size(); i++)
                fields1Indices.add(i, activeTable.getFieldIndex(f1.get(i).getName()));
            for (int i=0; i<fields2.size(); i++)
                fields2Indices.set(i, table.getFieldIndex(f2.get(i).getName()));
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in DBase th_join(): (5) --> " + e.message);}

        /* If the fields, on which th-join is performed, are more
         * than one, then we have to check the rest of the them (i.e.
         * beyond the first, upon which the join above took place) to see
         * if the pairs of matching on the first field condition records in Arrays "record1"
         * and "record2", do match on the rest of the field conditions too. These pairs for
         * which this is false, are removed from "record1" and "record2" arrays.
         */
        if (fields1.size()>1) {
            boolean failed;
            Object o1, o2;
            for (int i=0; i<record1.size(); i++) {
                failed = false;
                for (int l=1; l<fields1.size(); l++) {
                    comparator = (ObjectComparator) Operators.get(l);
//                    System.out.print(comparator + " " + fields1.get(l) + " " + activeTable._riskyGetCell(((Integer) fields1.get(l)).intValue(), ((Integer) record1.get(i)).intValue()) );
//                    System.out.println(" " + fields2.get(l) + " " + table._riskyGetCell(((Integer) fields2.get(l)).intValue(), ((Integer) record2.get(i)).intValue()));
                    o1 = activeTable.riskyGetCell(fields1Indices.get(l), ((Integer) record1.get(i)).intValue());
                    o2 = table.riskyGetCell(fields2Indices.get(l), ((Integer) record2.get(i)).intValue());
                    if (o1 == null || o2 == null || !comparator.execute(o1, o2)) {
                        failed = true;
                        break;
                    }
                }
                if (failed) {
                    record1.remove(i);
                    record2.remove(i);
                    i--;
                }
            }//for
        }//if (fields1.size()>1)

//        System.out.println(record1);
//        System.out.println(record2);

//System.out.println("here 6");

        Table result = new Table();
        ArrayList commonFieldNames = activeTable.equalFieldNames(table);
        /* Add the fields of the "activeTable" to the "result" table.
         */
        TableFieldBaseArray activeTableFields = activeTable.tableFields;
        for (int i=0; i<activeTableFields.size(); i++) {
            AbstractTableField f = activeTableFields.get(i);
//t            if (f.getFieldType().getName().equals("java.util.Date")) {
//t                if (f.isDate()) {
            if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) {
                try{
                    result._addField(f.getName(), f.getDataType()/*.toString().substring( f.getFieldType().toString().lastIndexOf('.')+1 )*/, true, true, f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }else if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CTime")) {
                try{
                    result._addField(f.getName(), CTime.class/*"time"*/, true, true, f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }else if (f.getDataType().equals(CImageIcon.class)) {
                    try{
                        result._addField(f.getName(), CImageIcon.class/*"Image"*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
            }else{
                try{
                    result._addField(f.getName(), f.getDataType()/*.toString().substring( f.getFieldType().toString().lastIndexOf('.')+1 )*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
            }
        }

        StringBaseArray existingNames = result.getFieldNames();
        TableFieldBaseArray tableFields = table.getFields();
        for (int i=0; i<tableFields.size(); i++) {
            AbstractTableField f = tableFields.get(i);
            String fieldName = f.getName();
            /* If this field's name is equal to the name of one of the fields in
             * "activeTable", then add the title of the "table" as prefix to this
             * field's name. This way firstly the field will be defined in the
             * resulting table "result", which cannot hold multiple fields with the
             * same name, and secondly the user will be able to distinguish the
             * origin of the fields with the same name.
             */
            if (commonFieldNames.indexOf(fieldName) != -1)
                fieldName = table.getTitle() + ":" + fieldName;
            if (commonFieldNames.indexOf(fieldName) != -1)
                fieldName = table.getTitle() + ":" + fieldName;
            int count = 0;
            for (int m=0; m<existingNames.size(); m++) {
                String existingName = (String) existingNames.get(m);
                int indx = existingName.indexOf(fieldName);
                if (indx == 0)
                    count++;
            }
            if (count != 0)
                fieldName = fieldName + ";" + (count+1);

//t            if (f.getFieldType().getName().equals("java.util.Date")) {
//t                if (f.isDate()) {
            if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) {
                try{
                    result._addField(fieldName, f.getDataType()/*.toString().substring( f.getFieldType().toString().lastIndexOf('.')+1 )*/, true, true, f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }else if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CTime")) {
                try{
                    result._addField(fieldName, CTime.class/*"time"*/, true, true, f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }else if (f.getDataType().equals(CImageIcon.class)) {
                    try{
                        result._addField(fieldName, CImageIcon.class/*"Image"*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
            }else{
                try{
                    result._addField(fieldName, f.getDataType()/*.toString().substring( f.getFieldType().toString().lastIndexOf('.')+1 )*/, true, true, f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }

        }

        /* Insert the records formed by the pairs of record indices in arrays
         * "record1" and "record2" into the "result" Table.
         */
//System.out.println("here 7");

        ArrayList recordEntryForm = new ArrayList();
        for (int i=0; i<record1.size(); i++) {
            for (int m=0; m<activeTable.getFieldCount(); m++)
                recordEntryForm.add(activeTable.riskyGetCell(m, ((Integer) record1.get(i)).intValue()));
            for (int m=0; m<table.getFieldCount(); m++)
                recordEntryForm.add(table.riskyGetCell(m, ((Integer) record2.get(i)).intValue()));

            try {
//                if (!result.addRecord(recordEntryForm))
                result.addRecord(recordEntryForm, false);
                recordEntryForm.clear();
            }catch (TableNotExpandableException e) {System.out.println(e.message); recordEntryForm.clear();}
             catch (NoFieldsInTableException e) {System.out.println(e.message);recordEntryForm.clear();}
             catch (DuplicateKeyException e) {System.out.println(e.message);recordEntryForm.clear();}
             catch (NullTableKeyException e) {System.out.println(e.message);recordEntryForm.clear();}
             catch (InvalidDataFormatException e) {System.out.println(e.message);recordEntryForm.clear();}
        }

        /* Set the title of the new table to "Th-join: <table1>-<table2>.
         * Add the table to this database and make it the active one.
         */
        try{
            String title = resources.getString("CDatabaseMsg82") + activeTable.getTitle() + "-" + table.getTitle()+")";
            addTableHandleToDBaseHandle(result);
//            result.getESlateHandle().setESlateMicroworld(handle.getESlateMicroworld());
            tableImportPlug.connectPlug(result.getTablePlug());
        }catch (PlugNotConnectedException exc) {
            System.out.println("Cannot add Table to the DBase 4");
        }
//            _addTable(result, title, true, true);
//        }catch (InvalidTitleException e) {
            //this normally never occurs
//            System.out.println("Serious inconsistency error in DBase th-join() : the title given to the result table already exists");
//        }
//        activeTable = result;
//        _activateTable(Tables.size()-1);

//        result.printTable();
        return true;
    }


    /**
     *  Intersects the active Table with the specified one. The new Table is stored in
     *  the database of the active Table and is activated.
     *  @param table The second table.
     *  @return <i>true</i>, if the operation succeeds. <i>false</i>, if the second table
     *          is <i>null</i>, or the active table itself.
     *  @exception  IntersectionTablesNotIdenticalException If the two tables are not
     *              identical in structure.
     *  @exception  IntersectionUnableToPerformException If the any of the tables has no
     *              fields, or contains fields of type <b>Sound</b>.
     */
    public final boolean intersection(Table table) throws IntersectionTablesNotIdenticalException,
    IntersectionUnableToPerformException {

        /* Don't intersect a table with itself.
         */
        if (table == null || table.equals(activeTable))
            return false;

        /* Check if the two tables contain fields and if they contain an
         * equal number of fields.
         */
        if (activeTable.getFieldCount() == 0)
            throw new IntersectionUnableToPerformException(resources.getString("CDatabaseMsg69") + activeTable.getTitle() + resources.getString("CDatabaseMsg70"));
        if (activeTable.getFieldCount() != table.getFieldCount())
            throw new IntersectionTablesNotIdenticalException(resources.getString("CDatabaseMsg59") + activeTable.getTitle() + resources.getString("CDatabaseMsg60") + table.getTitle() + resources.getString("CDatabaseMsg83"));

        /* Check if the names of the fields of the two tables are identical.
         */
        ArrayList commonFieldNames = activeTable.equalFieldNames(table);
        if (commonFieldNames.size() != activeTable.getFieldCount())
            throw new IntersectionTablesNotIdenticalException(resources.getString("CDatabaseMsg59") + activeTable.getTitle() + resources.getString("CDatabaseMsg60") + table.getTitle() + resources.getString("CDatabaseMsg83"));
        int[] fieldIndex = new int[activeTable.getFieldCount()];
        ArrayList equalComparators = new ArrayList();
        /* Check if the two tables have fields with identical data types.
         * Find the corresponding field's index in "activeTable" for each
         * field in "table". Put the indices in "fieldIndex" array.
         * Create the Array "equalComparators", which contains the comparator
         * used for each field's data type in "table".
         */
        StringBaseArray activeTableFieldNames = activeTable.getFieldNames();
        TableFieldBaseArray tableFields = table.tableFields;
        for (int i=0; i<tableFields.size(); i++) {
            AbstractTableField f = tableFields.get(i);
            fieldIndex[i] =  activeTableFieldNames.indexOf(f.getName());
try{
//            equalComparators.add(((DataTypeComparators) dataTypeComparators.get(f.getDataType().getName())).getEqualComparator());
            equalComparators.add(f.getComparatorFor("="));
}catch (Exception e) {System.out.println(e.getClass().getName()); return false;}
            try{
                if (!f.hasEqualType(activeTable.getTableField(fieldIndex[i])))
                    throw new IntersectionTablesNotIdenticalException(resources.getString("CDatabaseMsg59") + activeTable.getTitle() + resources.getString("CDatabaseMsg60") + table.getTitle() + resources.getString("CDatabaseMsg83") + ". " + resources.getString("CDatabaseMsg84") +
                      f.getName() + resources.getString("CDatabaseMsg60") + activeTable.getTableField(fieldIndex[i]).getName() + resources.getString("CDatabaseMsg85"));
            }catch (InvalidFieldIndexException e) {System.out.println("Serious inconsistency error in DBase intersection: (1)"); return false;}
        }

        AbstractTableField f;
        TableFieldBaseArray flds = activeTable.tableFields;
        Table result = new Table();
        for (int i=0; i<activeTable.getFieldCount(); i++) {
            f = flds.get(i);

//t            if (f.getFieldType().getName().equals("java.util.Date")) {
//t                if (f.isDate()) {
            if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) {
                try{
                    result._addField(f.getName(), f.getDataType()/*.toString().substring( f.getFieldType().toString().lastIndexOf('.')+1 )*/, f.isEditable(), f.isRemovable(), f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }else if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CTime")) {
                try{
                    result._addField(f.getName(), CTime.class/*"time"*/, f.isEditable(), f.isRemovable(), f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }else if (f.getDataType().equals(CImageIcon.class)) {
                    try{
                        result._addField(f.getName(), CImageIcon.class/*"Image"*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
            }else{
                try{
                    result._addField(f.getName(), f.getDataType()/*.toString().substring( f.getFieldType().toString().lastIndexOf('.')+1 )*/, f.isEditable(), f.isRemovable(), f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
            }
        }


        /* Get the data of the two fields with the same name in the
         * two tables to be intersected. We search for a field whose type
         * is not "Sound", because currently intersection on sounds is not
         * supported.
         */
        int fldIndex = 0;
        boolean nonImageOrSoundFieldFound = false;
        AbstractTableField aField = null;
        while (fldIndex < activeTable.tableFields.size()) {
            aField = activeTable.tableFields.get(fldIndex);
            if (!aField.getDataType().getName().equals("Sound data type???") &&
                !aField.getDataType().equals(CImageIcon.class)) {
                nonImageOrSoundFieldFound = true;
                break;
            }
            fldIndex++;
        }
        if (!nonImageOrSoundFieldFound)
            throw new IntersectionUnableToPerformException(resources.getString("CDatabaseMsg86"));
//            throw new IntersectionUnableToPerformException("Unable to intersect tables that contain \"Sound\" fields");

///        ArrayList field1, field2, recIndices1 = new ArrayList(), recIndices2 = new ArrayList();
        AbstractTableField fld1, fld2 = null;
        try {
            fld1 = activeTable.getTableField(aField.getName());
///            field1 = fld1.getDataArrayList();
            fld2 = table.getTableField(aField.getName());
///            field2 = fld2.getDataArrayList();
//            field1 = activeTable.getField(aField.getName());
//            field2 = table.getField(aField.getName());
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in DBase intersection(): (2)"); return false;}

//        ArrayList fld1Clone = (ArrayList) field1.clone();
//        ArrayList fld2Clone = (ArrayList) field2.clone();
///        field1 = field2 = null;

        String className = aField.getDataType().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        Object valueToReplaceNull;
        if (className.equals("Integer"))
            valueToReplaceNull = INTEGER_FOR_NULL_VALUE;
        else if (className.equals("Double"))
            valueToReplaceNull = DOUBLE_FOR_NULL_VALUE;
        else if (className.equals("String"))
            valueToReplaceNull = STRING_FOR_NULL_VALUE;
        else if (className.equals("Boolean"))
            valueToReplaceNull = BOOLEAN_FOR_NULL_VALUE;
        else if (className.equals("URL"))
            valueToReplaceNull = URL_FOR_NULL_VALUE;
        else if (className.equals("CDate"))
//t            if (aField.isDate())
            valueToReplaceNull = DATE_FOR_NULL_VALUE;
        else if (className.equals("CTime"))
            valueToReplaceNull = TIME_FOR_NULL_VALUE;
        else if (className.equals("CImageIcon"))
            valueToReplaceNull = IMAGE_FOR_NULL_VALUE;
        else{
            System.out.println("Serious inconsistency error in DBase intersection(): (3)");
            return false;
        }

/*        for (int i=0; i<fld1Clone.size(); i++) {
            if (fld1Clone.get(i) == null)
                fld1Clone.set(i, valueToReplaceNull);
        }
        for (int i=0; i<fld2Clone.size(); i++) {
            if (fld2Clone.get(i) == null)
                fld2Clone.set(i, valueToReplaceNull);
        }
*/
        /* Create two new Arrays, one for each field's data Array. In each
         * such Array store the record index of for each value of the field.
         */
/*        recIndices1.ensureCapacity(fld1Clone.size());
        recIndices2.ensureCapacity(fld2Clone.size());
        for (int i=0; i<fld1Clone.size(); i++)
            recIndices1.add(new Integer(i));
        for (int i=0; i<fld2Clone.size(); i++)
            recIndices2.add(new Integer(i));
*/
        className = aField.getDataType().getName();
///        ObjectComparator comparator = ((DataTypeComparators) dataTypeComparators.get(className)).getComparator();
        ObjectComparator comparator = aField.getComparatorFor(">");
        if (aField.getDataType().equals(Boolean.class))
            comparator = StringTableField.getComparator(">");

        /* Use this special version of Sorting.sort (i.e. Mysorting.sort) in order
         * to sort the two index Arrays ("recIndices1" and "recIndices2") based on the
         * values of the "fld1Clone" and "fld2Clone" Arrays.
         * We do this because Arrays "fld1Clone" and "fld2Clone" have to be sorted in order
         * for the intersection algorithm, that follows, to function properly. Also
         * this way the intesection will give us the indices of the records that will
         * finally be joined, instead of just the common values between the two Arrays.
         */
///        MyArrayListSorting.sort(fld1Clone, comparator, recIndices1);
///        MyArrayListSorting.sort(fld2Clone, comparator, recIndices2);
        IntBaseArray recIndices1 = fld1.getOrder(comparator);
        IntBaseArray recIndices2 = fld2.getOrder(comparator);

        /* Store in Arrays "record1" and "record2" the result of the intersection
         * of Arrays "fld1Clone" and "fld2Clone". This result doesn't consist of the common
         * values between "fld1Clone" and "fld2Clone", but the indices of the records for
         * which common values exist in "fld1Clone" and "fld2Clone". So in the resulting
         * Arrays it is guaranteed that the record of "activeTable" whose index is
         * stored at position A of "record1", has the same value with the record of
         * "table" whose index is stored at position A of "record2", on the common
         * field. So as soon as we have this information, we'll be able to compare
         * the values of all the necessary fields of only the records that have a
         * common value in the already chosen field. So we avoid a lot of unnecessary
         * comparisons.
         */
        int index1=0, index2 = 0;
        ArrayList record1 = new ArrayList(), record2 = new ArrayList();

        while (index1<fld1.size() && index2<fld2.size()) {
            if (comparator.execute( fld1.getObjectAt(recIndices1.get(index1)), fld2.getObjectAt(recIndices2.get(index2)))) {
                index1++;
            }else if (comparator.execute(fld2.getObjectAt(recIndices2.get(index2)), fld1.getObjectAt(recIndices1.get(index1)))) {
                index2++;
            }else{
                record1.add(new Integer(recIndices1.get(index1)));
                record2.add(new Integer(recIndices2.get(index2)));
///                Object o = fld2Clone.get(index2);
                Object o = fld2.getObjectAt(recIndices2.get(index2));
                index2++;
                int temp2 = index2;
                while (index2 < fld2.size() && fld2.getObjectAt(recIndices2.get(index2)).equals(o)) {
                    record1.add(new Integer(recIndices1.get(index1)));
                    record2.add(new Integer(recIndices2.get(index2)));
                    index2++;
                }
                index2 = temp2;
                if ((index1+1) < fld1.size() && fld1.getObjectAt(recIndices1.get(index1)).equals(fld1.getObjectAt(recIndices2.get(index1+1))))
                    index2--;
                index1++;
            }
        }
        recIndices1 = recIndices2 = null;
//        fld1Clone = fld2Clone = null;

//        System.out.println(record1);
//        System.out.println(record2);

        /* Now "record1" Array contains the indices of all the records in
         * "activeTable", which have a common value with some record in "table"
         * on the field chosen above. We compare the fields of the records whose
         * indices are in "record1" with the corresponding fields of the records
         * in "table", which agree on the field chosen above.
         */
//        ArrayList commonRecords = new ArrayList();
        int numOfFields = activeTable.getFieldCount();
        boolean common;
        int correspRecord, rec;
        Object o1, o2;
        for (int i=0; i< record1.size(); i++) {
            common = true;
            rec = ((Integer) record1.get(i)).intValue();  //Index of record in "activeTable"
            correspRecord = ((Integer) record2.get(i)).intValue(); //Index of the record in "table", which has the same value on the chosen field, with the record with index "rec" in "activeTable"
            for (int k=0; k<numOfFields; k++) {
//                System.out.println(activeTable._riskyGetCell(fieldIndex[k], rec) + ", " + table._riskyGetCell(k, correspRecord));

                /* Don't check for equality among fields of type "Image".
                 */
                try{
                    AbstractTableField fld = activeTable.getTableField(fieldIndex[k]);
                    if (fld.getDataType().equals(CImageIcon.class))
                        continue;
                }catch (InvalidFieldIndexException e) {System.out.println("Serious inconsistency error in DBase intersection() : (4)");}

                o1 = activeTable.riskyGetCell(fieldIndex[k], rec);
                o2 = table.riskyGetCell(k, correspRecord);
                if (o1 == null || o2 == null) {
//                    System.out.println("In null: " + o1 + ", " + o2 + " -- " + table.riskyGetField(k).getName() + "," + rec + "   " + activeTable.riskyGetField(fieldIndex[k]).getName() + "," + correspRecord);
                    if (o1 == o2)
                        continue;
                    else{
                        common = false;
                        break;
                    }
                }
                if (!((BinaryPredicate) equalComparators.get(k)).execute((Object) activeTable.riskyGetCell(fieldIndex[k], rec), table.riskyGetCell(k, correspRecord))) {
//                    System.out.println(activeTable._riskyGetCell(fieldIndex[k], rec) + ", " + table._riskyGetCell(k, correspRecord));
                    common = false;
                    break;
                }
            }

            /* If the two records have equal values in all their fields then insert the
             * record of "activeTable" in the "result" Table.
             */
            if (common) {
                try{
                    result.addRecord(activeTable.getRow(rec), false);
                }catch (InvalidRecordIndexException e) {System.out.println("Serious inconsistency error at DBase intersection(): (5)"); return false;}
                 catch (InvalidDataFormatException e) {System.out.println("Serious inconsistency error at DBase intersection(): (6)"); return false;}
                 catch (TableNotExpandableException e) {System.out.println("Serious inconsistency error at DBase intersection(): (7)"); return false;}
                 catch (NoFieldsInTableException e) {System.out.println("Serious inconsistency error at DBase intersection(): (8)"); return false;}
                 catch (DuplicateKeyException e) {System.out.println("Serious inconsistency error at DBase intersection(): (9)"); return false;}
                 catch (NullTableKeyException e) {System.out.println("Serious inconsistency error at DBase intersection(): (10)"); return false;}

                /* Avoid comparing the same record of "activeTable" with other records
                 * in table, with which it may have the same value on the field chosen
                 * above. The reason is that this record is already inserted in the
                 * "result" of the intersection.
                 */
                while (i+1<record1.size() && record1.get(i).equals(record1.get(i+1)))
                    i++;
            }
        }

        /* Set the title of the new table to "Intersection: <table1>-<table2>.
         * Add the table to this database and make it the active one.
         */
        try{
            String title = resources.getString("CDatabaseMsg87") + activeTable.getTitle() + "-" + table.getTitle()+")";
            addTableHandleToDBaseHandle(result);
//            result.getESlateHandle().setESlateMicroworld(handle.getESlateMicroworld());
            tableImportPlug.connectPlug(result.getTablePlug());
        }catch (PlugNotConnectedException exc) {
            System.out.println("Cannot add Table to the DBase 4");
        }
//            _addTable(result, title, true, true);
//        }catch (InvalidTitleException e) {
            //this normally never occurs
//            System.out.println("Serious inconsistency error in DBase intersection() : the title given to the result table already exists");
//        }
//        activeTable = result;
//        _activateTable(Tables.size()-1);

        return true;
    }


    /**
     *  Performs the union operation between the active Table and the specified one. The
     *  new Table is stored in the database of the active Table and is activated.
     *  @param table The second table.
     *  @return <i>true</i>, upon successful termination. <i>false</i>, if <i>table</i> is
     *          <i>null</i>, or the active table itself.
     *  @exception  UnionUnableToPerformException If any of the two tables contains no fields.
     *  @exception  UnionTablesNotIdenticalException If the two tables are not identical
     *              in structure.
     */
    public final boolean union(Table table) throws UnionUnableToPerformException,
    UnionTablesNotIdenticalException {
        /* Don't unite a table with itself.
         */
        if (table == null || table.equals(activeTable))
            return false;

        /* Check if the two tables contain fields and if they contain an
         * equal number of fields.
         */
        if (activeTable.getFieldCount() == 0)
            throw new UnionUnableToPerformException(resources.getString("CDatabaseMsg69") + activeTable.getTitle() + resources.getString("CDatabaseMsg70"));
        if (activeTable.getFieldCount() != table.getFieldCount())
            throw new UnionTablesNotIdenticalException(resources.getString("CDatabaseMsg59") + activeTable.getTitle() + resources.getString("CDatabaseMsg60") + table.getTitle() + resources.getString("CDatabaseMsg83"));

        /* Check if the names of the fields of the two tables are identical.
         */
        ArrayList commonFieldNames = activeTable.equalFieldNames(table);
        if (commonFieldNames.size() != activeTable.getFieldCount())
            throw new UnionTablesNotIdenticalException(resources.getString("CDatabaseMsg59") + activeTable.getTitle() + resources.getString("CDatabaseMsg60") + table.getTitle() + resources.getString("CDatabaseMsg83"));

        int[] fieldIndex = new int[activeTable.getFieldCount()];
        /* Check if the two tables have fields with identical data types.
         * Find the corresponding field's index in "activeTable" for each
         * field in "table". Put the indices in "fieldIndex" array.
         */
        StringBaseArray tableFieldNames = table.getFieldNames();
        TableFieldBaseArray activeTableFields = activeTable.tableFields;
        for (int i=0; i<activeTableFields.size(); i++) {
            AbstractTableField f = activeTableFields.get(i);
            fieldIndex[i] =  tableFieldNames.indexOf(f.getName());
            try{
                if (!f.hasEqualType(table.getTableField(fieldIndex[i])))
                    throw new UnionTablesNotIdenticalException(resources.getString("CDatabaseMsg59") + activeTable.getTitle() + resources.getString("CDatabaseMsg60") + table.getTitle() + resources.getString("CDatabaseMsg83") + ". " + resources.getString("CDatabaseMsg84") +
                      f.getName() + resources.getString("CDatabaseMsg60") + activeTable.getTableField(i).getName() + resources.getString("CDatabaseMsg85"));
            }catch (InvalidFieldIndexException e) {System.out.println("Serious inconsistency error in DBase intersection: (11)"); return false;}
        }

        AbstractTableField f;
        TableFieldBaseArray flds = activeTable.tableFields;
        Table result = new Table();
        for (int i=0; i<activeTable.getFieldCount(); i++) {
            f = flds.get(i);

//t            if (f.getFieldType().getName().equals("java.util.Date")) {
//t                if (f.isDate()) {
            if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) {
                try{
                    result._addField(f.getName(), f.getDataType()/*.toString().substring( f.getFieldType().toString().lastIndexOf('.')+1 )*/, true, true, f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }else if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CTime")) {
                try{
                    result._addField(f.getName(), CTime.class/*"time"*/, true, true, f.isHidden(), true);
                }catch (InvalidFieldNameException e) {}
                 catch (InvalidKeyFieldException e) {}
                 catch (InvalidFieldTypeException e) {}
                 catch (AttributeLockedException e) {}
            }else if (f.getDataType().equals(CImageIcon.class)) {
                    try{
                        result._addField(f.getName(), CImageIcon.class/*"Image"*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
            }else{
                try{
                    result._addField(f.getName(), f.getDataType()/*.toString().substring( f.getFieldType().toString().lastIndexOf('.')+1 )*/, true, true, f.isHidden(), true);
                    }catch (InvalidFieldNameException e) {}
                     catch (InvalidKeyFieldException e) {}
                     catch (InvalidFieldTypeException e) {}
                     catch (AttributeLockedException e) {}
            }
        }

        try{
            /* Add the records of "activeTable" to the "result" table.
             */
            for (int i=0; i<activeTable.getRecordCount(); i++)
                result.addRecord(activeTable.getRow(i), false);

            /* Add the records of "table" to the "result" table.
             */
            ArrayList recordEntryForm = new ArrayList();
            for (int i=0; i<table.getRecordCount(); i++) {
                for (int k=0; k<table.getFieldCount(); k++)
                    recordEntryForm.add(table.riskyGetCell(fieldIndex[k], i));

                result.addRecord(recordEntryForm, false);
                recordEntryForm.clear();
            }
        }catch (InvalidRecordIndexException e) {System.out.println("Serious inconsistency error at DBase union(): (1)"); return false;}
         catch (InvalidDataFormatException e) {System.out.println("Serious inconsistency error at DBase union(): (1)"); return false;}
         catch (TableNotExpandableException e) {System.out.println("Serious inconsistency error at DBase union(): (1)"); return false;}
         catch (NoFieldsInTableException e) {System.out.println("Serious inconsistency error at DBase union(): (1)"); return false;}
         catch (DuplicateKeyException e) {System.out.println("Serious inconsistency error at DBase union(): (1)"); return false;}
         catch (NullTableKeyException e) {System.out.println("Serious inconsistency error at DBase union(): (1)"); return false;}

        /* Set the title of the new table to "Union: <table1>-<table2>.
         * Add the table to this database and make it the active one.
         */
        try{
            String title = resources.getString("CDatabaseMsg88") + activeTable.getTitle() + "-" + table.getTitle()+")";
            addTableHandleToDBaseHandle(result);
//            result.getESlateHandle().setESlateMicroworld(handle.getESlateMicroworld());
            tableImportPlug.connectPlug(result.getTablePlug());
        }catch (PlugNotConnectedException exc) {
            System.out.println("Cannot add Table to the DBase 4");
        }
//            _addTable(result, title, true, true);
//        }catch (InvalidTitleException e) {
            //this normally never occurs
//            System.out.println("Serious inconsistency error in DBase union() : the title given to the result table already exists");
//        }
//        activeTable = result;
//        _activateTable(Tables.size()-1);

        return true;
    }


    /** Creates an empty -without records- replicate of the Table <i>table</i> and
     *  returns it. The Table is not inserted in the DBase.
     *  @param table The table, whose replicate will be created.
     */
    public static Table createEmptyTableReplicate(Table table) {
        Table newTable = new Table(table.getTitle());
        AbstractTableField field;
        for (int i=0; i<table.getFieldCount(); i++) {
            try{
                field = table.getTableField(i);
            }catch (InvalidFieldIndexException e) {
                System.out.println("Serious inconsistency error in DBase createEmptyTableReplicate(): (1)");
                continue;
            }
            try{
                newTable.addField(field);
            }catch (Exception e) {
                System.out.println("Serious inconsistency error in DBase createEmptyTableReplicate(): (2)");
                continue;
            }
        }
        return newTable;
    }


    /** Creates an empty -without records- replicate of the Table <i>table</i> and
     *  returns it. The Table is not inserted in the DBase.
     *  @param table The table, whose replicate will be created.
     */
    public static Table createTableClone(Table table) {
        Table newTable = new Table();
        AbstractTableField field;
        for (int i=0; i<table.getFieldCount(); i++) {
            try{
                field = table.getTableField(i);
            }catch (InvalidFieldIndexException e) {
                System.out.println("Serious inconsistency error in DBase createTableClone(): (1)");
                continue;
            }
            try{
                newTable.addField(field);
            }catch (Exception e) {
                System.out.println("Serious inconsistency error in DBase createTableClone(): (2)");
                continue;
            }
        }

        ArrayList recEntryForm = new ArrayList();
        recEntryForm.ensureCapacity(newTable.getFieldCount());

        AbstractTableField fld;
        for (int i=0; i<table.getRecordCount(); i++) {
            for (int k=0; k<table.getFieldCount(); k++) {
                if (!table.tableFields.get(k).isCalculated())
                    recEntryForm.add(table.riskyGetCell(k, i));
            }
            try{
                newTable.addRecord(recEntryForm, false);
            }catch (Exception e) {
                System.out.println(e.getClass() + ", " + e.getMessage()); //"Serious inconsistency error in DBase createTableClone(): (3)");
            }
            recEntryForm.clear();
        }

        // Copy the cells of the calculated fields which do not depend on any other fields
        for (int k=0; k<table.getFieldCount(); k++) {
            AbstractTableField cf = table.tableFields.get(k);
            if (cf.isCalculated() && cf.calculatedValue != null) {
                try{
                    int index = table.getFieldIndex(cf);
//                    ArrayList cfContents = table.getField(index);
                    ArrayList cfContents = cf.getDataArrayList();
                    cfContents = (ArrayList) cfContents.clone();
                    ArrayList newcfContents = newTable.tableFields.get(index).getDataArrayList();
                    for (int i=0; i<cfContents.size(); i++)
                        newcfContents.add(cfContents.get(i));
                }catch (Exception exc) {}
            }
        }

        newTable._setTitle(table.getTitle());

        try{
            newTable.setTableRenamingAllowed(table.isTableRenamingAllowed());
            newTable.setRecordAdditionAllowed(table.isRecordAdditionAllowed());
            newTable.setRecordRemovalAllowed(table.isRecordRemovalAllowed());
            newTable.setFieldAdditionAllowed(table.isFieldAdditionAllowed());
            newTable.setFieldRemovalAllowed(table.isFieldRemovalAllowed());
            newTable.setFieldReorderingAllowed(table.isFieldReorderingAllowed());
            newTable.setFieldPropertyEditingAllowed(table.isFieldPropertyEditingAllowed());
            newTable.setDataChangeAllowed(table.isDataChangeAllowed());
            newTable.setKeyChangeAllowed(table.isKeyChangeAllowed());
        }catch (PropertyVetoException exc) {
            System.out.println("Inconsistency in DBase createTableClone(). PropertyVetoException should not occur");
            exc.printStackTrace();
        }
        if (table.getMetadata() != null)
            newTable.setMetadata(new String(table.getMetadata()));
        if (table.selectedSubset != null)
            newTable.selectedSubset = (IntBaseArrayDesc) table.selectedSubset.clone();
        if (table.unselectedSubset != null)
            newTable.unselectedSubset = (IntBaseArrayDesc) table.unselectedSubset.clone();
//1        if (table.sdf != null)
//1            newTable.sdf = (SimpleDateFormat) table.sdf.clone();
        if (table.dateFormat != null)
            newTable.dateFormat = (SimpleDateFormat) table.dateFormat.clone();
//1        if (table.stf != null)
//1            newTable.stf = (SimpleDateFormat) table.stf.clone();
        if (table.timeFormat != null)
            newTable.timeFormat = (SimpleDateFormat) table.timeFormat.clone();
        if (table.numberFormat != null)
            newTable.numberFormat = /*(DoubleNumberFormat)*/ (TableNumberFormat) table.numberFormat.clone();
//1        newTable.pendingNewRecord = table.pendingNewRecord;
//1        if (table.UIProperties != null)
//1            newTable.UIProperties = (Array) table.UIProperties.clone();
        newTable.isHidden = table.isHidden;

        int recCount = table.getRecordCount();
        for (int i=0; i<recCount; i++)
            newTable.recordSelection.set(i, table.recordSelection.get(i));

        if (table.recordIndex != null)
            newTable.recordIndex = (IntBaseArray) table.recordIndex.clone();

        return newTable;
    }


    /** Creates a new Table which has the exact field structure as Table
     *  <i>table</i> and contains only the selected records of <i>table</i>
     *  The new Table is not added to the database.
     *  @return The new Table.
     */
    public static Table createTableFromSelectedSet(Table table) {
        Table newTable = createEmptyTableReplicate(table);
        IntBaseArrayDesc selectedSubset = (IntBaseArrayDesc) table.getSelectedSubset().clone();
        ArrayList recEntryForm = new ArrayList();
        recEntryForm.ensureCapacity(newTable.getFieldCount());
        for (int i=0; i<selectedSubset.size(); i++) {
            int selRecord = selectedSubset.get(i);
//            System.out.println("selRecord: " + selRecord);
            for (int k=0; k<table.getFieldCount(); k++) {
                if (!table.tableFields.get(k).isCalculated())
                    recEntryForm.add(table.riskyGetCell(k, selRecord));
            }
            try{
                newTable.addRecord(recEntryForm, false);
            }catch (Exception e) {
                System.out.println("Serious inconsistency error in DBase createTableFromSelectedSet(): (1)");
            }
            recEntryForm.clear();
        }

        newTable._setTitle(table.getTitle());
        return newTable;
    }


    /** Replaces an existing table <i>table1</i> in the current DBase with Table <i>table2</i>.
     * <b> NEEDS TO BE CHECKED </b>
     *  @param table1 The Table of the DBase to be replaced.
     *  @param table2 The Table to replace <i>table1</i>
     *  @param keepTitle Determines whether <i>table2</i> will keep its title, or will acquire <i>table1's</i> title.
     *  @returns <i>True</i>, if replacement succeeded. <i>false</i>, if <i>table1</i> does not belong
     *           to the current DBase, in which case no replacement takes place.
     */
    public boolean replaceTable(Table table1, Table table2, boolean keepTitle) {
//        System.out.println("table1.database: " + table1.database.getTitle());
//table        if (table1.database == null || !table1.database.equals(this))
//table            return false;

        int index = databaseTables.indexOf(table1);
        if (index == -1)
            return false;
        databaseTables.set(index, table2);
//table        table2.database = this;
        setModified();
        newDatabase = false;
        if (!keepTitle)
            table2._setTitle(table1.getTitle());
        if (activeTable.equals(table1))
            activeTable = table2;
//        table1 = null;

        if (!hasUniqueTitle(table2, this)) {
            String oldTitle = table2.getTitle();
            table2._setTitle(generateUniqueTitle(table2, this));
            table2.fireTableRenamed(oldTitle);
        }

        fireTableReplaced(new TableReplacedEvent(this, table2, table1));
        return true;
    }


    /** Updates an existing table <i>table1</i> with the selected records of Table <i>table2</i>.
     *  The altered Table will acquire the field structure of the table from which it
     *  receives its contents.
     *  @param table1 The Table of the DBase to be updated.
     *  @param table2 The Table from which <i>table1</i> acquires both the fields and the selected records.
     *  @returns The updated Table, or null if <i>table1</i> is not part of the DBase.
     */
    public Table updateTableFromSelectedSet(Table table1, Table table2) {
//table        if (!table1.database.equals(this))
//table            return null;

        Table table = createTableFromSelectedSet(table2);
//        System.out.println("Creating new Table with title: " + table.getTitle());
        String oldTitle = table.getTitle();
//table        table.database = this;
        int index = databaseTables.indexOf(table1);
        if (index == -1)
            return null;
        databaseTables.set(index, table);
//table        table.database = this;
        setModified();
        newDatabase = false;
        if (activeTable.equals(table1))
            activeTable = table;
//        table1 = null;

        if (!hasUniqueTitle(table, this)) {
            table._setTitle(generateUniqueTitle(table, this));
            table.fireTableRenamed(oldTitle);
        }

        fireTableReplaced(new TableReplacedEvent(this, table, table1));
        return table;
    }


    /** Initializes a new DBase instance.
     */
    protected boolean initDatabase() {
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.constructionStarted(this);
        pm.init(constructorTimer);

        Locale locale=Locale.getDefault();
//        resources=ResourceBundle.getBundle("gr.cti.eslate.database.engine.DBResourceBundle", locale);

/*        if (DBase.operators == null || DBase.operators.isEmpty()) {
            HashMap h = new HashMap();
    //Integer
            try{
                h.add("<", new LessNumber(java.lang.Class.forName("java.lang.Integer")));
                h.add("<=", new LessEqualNumber(java.lang.Class.forName("java.lang.Integer")));
                h.add(">", new GreaterNumber(java.lang.Class.forName("java.lang.Integer")));
                h.add(">=", new GreaterEqualNumber(java.lang.Class.forName("java.lang.Integer")));
                h.add("!=", new NotEqualNumber(java.lang.Class.forName("java.lang.Integer")));
                h.add("=", new EqualNumber(java.lang.Class.forName("java.lang.Integer")));
            }catch (ClassNotFoundException e) {System.out.println("Serious inconsistency error in DBase initDatabase(): (1)"); return false;}
            operators.add("java.lang.Integer", h);
    //Double
            h = new HashMap();
            try{
                h.add("<", new LessNumber(java.lang.Class.forName("java.lang.Double")));
                h.add("<=", new LessEqualNumber(java.lang.Class.forName("java.lang.Double")));
                h.add(">", new GreaterNumber(java.lang.Class.forName("java.lang.Double")));
                h.add(">=", new GreaterEqualNumber(java.lang.Class.forName("java.lang.Double")));
                h.add("!=", new NotEqualNumber(java.lang.Class.forName("java.lang.Double")));
                h.add("=", new EqualNumber(java.lang.Class.forName("java.lang.Double")));
            }catch (ClassNotFoundException e) {System.out.println("Serious inconsistency error in DBase initDatabase(): (2)"); return false;}
            operators.add("java.lang.Double", h);
    //String
            h = new HashMap();
            h.add("=", new EqualString());
            h.add("<=", new LessEqualString());
            h.add("<", new LessString());
            h.add(">", new GreaterString());
            h.add(">=", new GreaterEqualString());
            h.add("!=", new NotEqualString());
            h.add(this.resources.getString("contained"), new ContainedInString());
            h.add(this.resources.getString("contains"), new ContainsString());
            operators.add("java.lang.String", h);
    //Boolean
            h = new HashMap();
            h.add("=", new EqualString());
            h.add("!=", new NotEqualString());
            operators.add("java.lang.Boolean", h);
    //URL
            h = new HashMap();
            h.add(this.resources.getString("contains"), new ContainsString());
            h.add(this.resources.getString("contained"), new ContainedInString());
            operators.add("java.net.URL", h);
    //Date & Time
            h = new HashMap();
            h.add("=", new EqualDate());
            h.add("!=", new NotEqualDate());
            h.add("<", new LessDate());
            h.add("<=", new LessEqualDate());
            h.add(">", new GreaterDate());
            h.add(">=", new GreaterEqualDate());
            operators.add("gr.cti.eslate.database.engine.CDate", h);
            operators.add("gr.cti.eslate.database.engine.CTime", h);
    //Image
            h = new HashMap();
    //        h.add("=", new EqualHashComparator());
    //        h.add("!=", new NotEqualHashComparator());
            operators.add("gr.cti.eslate.database.engine.CImageIcon", h);
        }
*/
/*        if (DBase.dataTypeComparators == null || DBase.dataTypeComparators.isEmpty()) {
*/            /* Filling the dataTypeComparators HashMap
             */
/*            DataTypeComparators cmp;
    //Integer
            try{
                cmp = new DataTypeComparators(new GreaterNumber(java.lang.Class.forName("java.lang.Integer")), new EqualNumber(java.lang.Class.forName("java.lang.Integer")));
            }catch (ClassNotFoundException e) {System.out.println("Serious inconsistency error in DBase initDatabase(): (3)"); return false;}
            dataTypeComparators.add("java.lang.Integer", cmp);
            try{
                cmp = new DataTypeComparators(new GreaterNumber(java.lang.Class.forName("java.lang.Double")), new EqualNumber(java.lang.Class.forName("java.lang.Double")));
            }catch (ClassNotFoundException e) {System.out.println("Serious inconsistency error in DBase initDatabase(): (4)"); return false;}
            dataTypeComparators.add("java.lang.Double", cmp);
            cmp = new DataTypeComparators(new GreaterString(), new EqualString());
            dataTypeComparators.add("java.lang.String", cmp);
            cmp = new DataTypeComparators(new GreaterString(), new EqualString());
            dataTypeComparators.add("java.lang.Boolean", cmp);
            cmp = new DataTypeComparators(new GreaterString(), new EqualString());
            dataTypeComparators.add("java.net.URL", cmp);
            cmp = new DataTypeComparators(new GreaterDate(), new EqualDate());
    //t        dataTypeComparators.add("java.util.Date", cmp);
            dataTypeComparators.add("gr.cti.eslate.database.engine.CDate", cmp);
            dataTypeComparators.add("gr.cti.eslate.database.engine.CTime", cmp);
            cmp = new DataTypeComparators(new GreaterHashComparator(), new EqualHashComparator());
            dataTypeComparators.add("gr.cti.eslate.database.engine.CImageIcon", cmp);
        }
*/
        tableVetoListener = new VetoableChangeListener() {
            public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
                Table table = (Table) evt.getSource();
                String propertyName = evt.getPropertyName();
                if (propertyName.equals("Table name")) {
                    /* If there exists a table in the database with the new name, then
                     * veto the table's name change.
                     */
                    Table tbl = getTable((String) evt.getNewValue());
                    if (tbl != null) {
                        throw new PropertyVetoException(
                            resources.getString("CDatabaseMsg119") + table.getTitle() +
                            resources.getString("CDatabaseMsg120") + evt.getNewValue() +
                            resources.getString("CDatabaseMsg121") +
                            getTitle() + "\".", evt);
                    }
                }

                if (!isLocked()) return;
                if (propertyName.equals("Record addition allowed")) {
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg101") + table.getTitle() + "\"" +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Record removal allowed")) {
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg102") + table.getTitle() + "\"" +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Field addition allowed")) {
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg103") + table.getTitle() + "\"" +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Field removal allowed")) {
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg104") + table.getTitle() + "\"" +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Field reordering allowed")) {
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg105") + table.getTitle() + "\"" +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Key change allowed")) {
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg106") + table.getTitle() + "\"" +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Hidden fields displayed")) {
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg107") + table.getTitle() + "\"" +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Data change allowed")) {
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg108") + table.getTitle() +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Hidded attribute change allowed")) {
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg109") + table.getTitle() +
                        resources.getString("CDatabaseMsg110") +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Field editability change allowed")) {
                    String fieldName = null;
                    if (AbstractTableField.class.isAssignableFrom(evt.getOldValue().getClass()))
                        fieldName = ((AbstractTableField) evt.getOldValue()).getName();
                    else
                        fieldName = ((AbstractTableField) evt.getNewValue()).getName();
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg111") + fieldName +
                        resources.getString("CDatabaseMsg112") + table.getTitle() +
                        resources.getString("CDatabaseMsg110") +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Field removability change allowed")) {
                    String fieldName = null;
                    if (AbstractTableField.class.isAssignableFrom(evt.getOldValue().getClass()))
                        fieldName = ((AbstractTableField) evt.getOldValue()).getName();
                    else
                        fieldName = ((AbstractTableField) evt.getNewValue()).getName();
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg113") + fieldName +
                        resources.getString("CDatabaseMsg112") + table.getTitle() +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Field data type change allowed")) {
                    String fieldName = null;
                    if (AbstractTableField.class.isAssignableFrom(evt.getOldValue().getClass()))
                        fieldName = ((AbstractTableField) evt.getOldValue()).getName();
                    else
                        fieldName = ((AbstractTableField) evt.getNewValue()).getName();
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg114") + fieldName +
                        resources.getString("CDatabaseMsg112") + table.getTitle() +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Calculated field reset allowed")) {
                    String fieldName = null;
                    if (AbstractTableField.class.isAssignableFrom(evt.getOldValue().getClass()))
                        fieldName = ((AbstractTableField) evt.getOldValue()).getName();
                    else
                        fieldName = ((AbstractTableField) evt.getNewValue()).getName();
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg115") + fieldName +
                        resources.getString("CDatabaseMsg112") + table.getTitle() +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Key attribute change allowed")) {
                    String fieldName = null;
                    if (AbstractTableField.class.isAssignableFrom(evt.getOldValue().getClass()))
                        fieldName = ((AbstractTableField) evt.getOldValue()).getName();
                    else
                        fieldName = ((AbstractTableField) evt.getNewValue()).getName();
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg116") + fieldName +
                        resources.getString("CDatabaseMsg112") + table.getTitle() +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Field hidden attribute change allowed")) {
                    String fieldName = null;
                    if (AbstractTableField.class.isAssignableFrom(evt.getOldValue().getClass()))
                        fieldName = ((AbstractTableField) evt.getOldValue()).getName();
                    else
                        fieldName = ((AbstractTableField) evt.getNewValue()).getName();
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg117") + fieldName +
                        resources.getString("CDatabaseMsg112") + table.getTitle() +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }else if (propertyName.equals("Formula attribute change allowed")) {
                    String fieldName = null;
                    if (AbstractTableField.class.isAssignableFrom(evt.getOldValue().getClass()))
                        fieldName = ((AbstractTableField) evt.getOldValue()).getName();
                    else
                        fieldName = ((AbstractTableField) evt.getNewValue()).getName();
                    throw new PropertyVetoException(
                        resources.getString("CDatabaseMsg118") + fieldName +
                        resources.getString("CDatabaseMsg112") + table.getTitle() +
                        resources.getString("Database1") + getTitle() +
                        resources.getString("DBLocked"), evt);
                }
            }
        };

        pm.constructionEnded(this);
        pm.stop(constructorTimer);
        pm.displayTime(constructorTimer, "��������� �����", "ms");
//        pm.displayTime(constructorTimer, getESlateHandle(), "ms");

        return true;
    }

    /**
     *  Creates a new empty DBase.
     *  @exception UnableToInitializeDatabaseException If the database cannot be initialized.
     */
    public DBase() throws UnableToInitializeDatabaseException {
//        System.out.println("DBase constructor 1");
        title = resources.getString("Database");
        if (!initDatabase())
            throw new UnableToInitializeDatabaseException();
        setModified();
        newDatabase = true;
    }

    public DBase(Object db) {
        if (!CDatabase.class.isAssignableFrom(db.getClass()))
            return;

        handle = getESlateHandle();
//        if (handle.getParentHandle() == null)
//            microworldForReadExternal.add(handle);

        CDatabase _db = (CDatabase) db;
        if (_db.title != null) {
            _db.title = _db.title.replace('.', '$');
            _setTitle(_db.title);
        }
        dbfile = _db.dbfile;
        databaseTables = _db.getTables();
        /* If the 'databaseTables' contains CTables instead of Tables, then
         * convert them Tables.
         */
        for (int i=0; i<databaseTables.size(); i++) {
            Object t = databaseTables.get(i);
            if (CTable.class.isAssignableFrom(t.getClass()))
                databaseTables.set(i, new Table((CTable) t));
        }
        for (int i=0; i<databaseTables.size(); i++) {
            Table t = (Table) databaseTables.get(i);
            String tableTitle = t.getTitle();
            ESlateHandle tableHandle = t.getESlateHandle();

//                    tableHandle.setESlateMicroworld(handle.getESlateMicroworld());
            handle.add(tableHandle);
            try{
//                addTableHandleToDBaseHandle(t);
//                if (microworldForReadExternal != null) {
//                    handle.setESlateMicroworld(microworldForReadExternal);
//                    ((Table) databaseTables.get(i)).getESlateHandle().setESlateMicroworld(microworldForReadExternal);
//                }
                tableImportPlug.connectPlug(((Table) databaseTables.get(i)).getTablePlug());
            }catch (PlugNotConnectedException exc) {}
            ((Table) databaseTables.get(i))._setTitle(tableTitle);
        }

        UIProperties = _db.UIProperties;
        databaseCreatorName = _db.getDatabaseCreatorName();
        databaseCreatorSurname = _db.getDatabaseCreatorSurname();
        creationDate = _db.getCreationDate();
        lastModified = _db.getLastModified();
        _protected = _db.isProtected();
        _password = _db._password;
        tableRemovalAllowed = _db.isTableRemovalAllowed();
        tableAdditionAllowed = _db.isTableAdditionAllowed();
        tableExportationAllowed = _db.isTableExportationAllowed();
        hiddenTablesDisplayed = _db.isHiddenTablesDisplayed();
        metadata = _db.getMetadata();

        if (_protected)
            locked = true;
        else
            locked = false;

        isModified = false;
        newDatabase = false;
        databaseListeners = new ArrayList();
        databasePropertyChangeListeners = new ArrayList();
        propertyChangeSupport = new PropertyChangeSupport(this);
//        System.out.println("Obf 1");
        initDatabase();

//table        for (int i=0; i<databaseTables.size(); i++)
//table            ((Table) databaseTables.get(i)).database = this;
        if (!(databaseTables.size() == 0))
            this._activateTable(0);
    }

    public void unregisterVetoListener() {
        for (int i=0; i<databaseTables.size(); i++)
            ((Table) databaseTables.get(i)).removeVetoableChangeListener(tableVetoListener);
    }

    /**
     *  Creates a new empty DBase with the specified title.
     *  @exception UnableToInitializeDatabaseException If the database cannot be initialized.
     */
    public DBase(String title) throws UnableToInitializeDatabaseException {
//        System.out.println("DBase constructor 2");
        _setTitle(title);
        if (!initDatabase())
            throw new UnableToInitializeDatabaseException();
        setModified();
        newDatabase = true;
    }

    public void setDBView(DBView view) {
        this.dbView = view;
    }

    public DBView getDBView() {
        return dbView;
    }

    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);

            handle = ESlate.registerPart(this);
            attachTimers();
            try {
//                Class soClass=Class.forName("gr.cti.eslate.sharedObject.DatabaseSO");
//                databaseSO=new DatabaseSO(handle);
//                databaseSO.setDatabase(this);

//                databasePin = new MultipleOutputPlug(handle, resources, "Database", Color.magenta, gr.cti.eslate.sharedObject.DatabaseSO.class, databaseSO);
                dbasePlug = new MaleSingleIFMultipleConnectionProtocolPlug(
                                        handle,
                                        resources,
                                        "Database",
                                        Color.magenta);
//System.out.println("DBase getESlateHandle() dbasePlug time: " + (System.currentTimeMillis()-s1));
//System.out.println("DBase getESlateHandle() 1.1: " + (System.currentTimeMillis()-start));
                java.util.Vector exportedif = new java.util.Vector();
                exportedif.addElement(DBase.class);
                dbasePlug.setExportedInterfaces(exportedif);
                dbasePlug.setHostingPlug(true);
                dbasePlug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
//System.out.println("DBase dbasePlug handleConnectionEvent() mwd: " + e.getPlug().getHandle().getESlateMicroworld().getName());
//System.out.println("DBase dbasePlug handleConnectionEvent() mwd: " + dbasePlug.getHandle().getESlateMicroworld().getName());
                    }
                });
                dbasePlug.addDisconnectionListener(new DisconnectionListener() {
                    public void handleDisconnectionEvent(DisconnectionEvent e) {
                    }
                });
                handle.addPlug(dbasePlug);
            }catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("Something went wrong during pin creation: " + exc.getMessage());
            }
            try{
                if (getTitle() != null)
                    setHandleNameToDBaseTitle(true);
                else
                    handle.setUniqueComponentName(resources.getString("Database"));
            }catch (RenamingForbiddenException exc) {exc.printStackTrace();}


/*            SharedObjectListener sol1 = new SharedObjectListener() {
                public void handleSharedObjectEvent(SharedObjectEvent soe) {
                    SharedObject so=soe.getSharedObject();
                    if (so instanceof TableSO) {
                        TableSO tableSO = (TableSO) so;

                        Table remoteTable = tableSO.getTable();
                        if (remoteTable == null) return;
                        System.out.println("remoteTable.getFieldCount(): " + remoteTable.getFieldCount());

                        try{
                            String proposedTableName = remoteTable.getTitle();
                            Plug pin = tableSO.getPlug();
*/                            /* When a Table is imported in the DBase through the 'tableImportPin'
                             * we add it to the DBase without altering its parent handle. This is
                             * because the table belongs to some external component. However, if
                             * that component is destroyed, then the DBase becomes the parent of
                             * the imported table. This is checked by an ESlateAdapter, which is
                             * added to the imported Table's handle. A Table may be imported into
                             * multiple DBases. The first ESlateAdapter whose 'disposingHandle()'
                             * method is called, will re-parent the Table to the DBase which
                             * defined the adapter.
                             */
/*                            if (!databaseTables.contains(remoteTable))
                                _addTable(remoteTable, remoteTable.getTitle(), true, false);
                            String tableName = getTableAt(getTableCount()-1).getTitle();
                        }catch (InvalidTitleException exc) {
                              ESlateOptionPane.showMessageDialog(new JFrame(), exc.getMessage(), resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                              return;
                        }
                    }
                }
            };
*/
            /* The Database's Table multiple input pin.
             * Through this pin tables for other sources (i.e. other Database components are inserted
             * in the Database component.
             */
            try {
//long s = System.currentTimeMillis();
//                tableImportPin = new MultipleInputPlug(handle, resources, "Table", TablePlug.TABLE_PLUG_COLOR, gr.cti.eslate.sharedObject.TableSO.class, sol1);
                tableImportPlug = new FemaleSingleIFMultipleConnectionProtocolPlug(
                                            handle,
                                            resources,
                                            "ImportTable",
                                            TablePlug.TABLE_PLUG_COLOR,
                                            Table.class);
//System.out.println("DBase getESlateHandle() tableImportPlug time: " + (System.currentTimeMillis()-s));
                tableImportPlug.setHostingPlug(true);
                handle.addPlug(tableImportPlug);
                tableImportPlug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
//System.out.println("DBase tableImportPlug handleConnectionEvent() mwd: " + e.getPlug().getHandle().getESlateMicroworld().getName());
//System.out.println("DBase tableImportPlug handleConnectionEvent() mwd: " + tableImportPlug.getHandle().getESlateMicroworld().getName());
                        Table remoteTable = (Table) e.getPlug().getHandle().getComponent();
//System.out.println("DBase tableImportPlug remoteTable: " + remoteTable + ", " + remoteTable.getTitle());
//                        Table remoteTable = tableSO.getTable();
                        if (remoteTable == null) return;
//                        System.out.println("remoteTable.getFieldCount(): " + remoteTable.getFieldCount());

                        try{
                            String proposedTableName = remoteTable.getTitle();
//                            Plug pin = tableSO.getPlug();
                            /* When a Table is imported in the DBase through the 'tableImportPin'
                             * we add it to the DBase without altering its parent handle. This is
                             * because the table belongs to some external component. However, if
                             * that component is destroyed, then the DBase becomes the parent of
                             * the imported table. This is checked by an ESlateAdapter, which is
                             * added to the imported Table's handle. A Table may be imported into
                             * multiple DBases. The first ESlateAdapter whose 'disposingHandle()'
                             * method is called, will re-parent the Table to the DBase which
                             * defined the adapter.
                             */
                            if (!databaseTables.contains(remoteTable))
                                _addTable(remoteTable, remoteTable.getTitle(), true);
                            String tableName = getTableAt(getTableCount()-1).getTitle();
                        }catch (InvalidTitleException exc) {
                              ESlateOptionPane.showMessageDialog(new JFrame(), exc.getMessage(), resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                              return;
                        }
//                        System.out.println("remoteTable: " +remoteTable);
//                        System.out.println("remoteTable handle disposed: " +remoteTable.getESlateHandle().isDisposed());
//                        System.out.println("databaseTables: " + databaseTables.size());
                    }
                });
                tableImportPlug.addDisconnectionListener(new DisconnectionListener() {
                    public void handleDisconnectionEvent(DisconnectionEvent e) {
//System.out.println("tableImportPlug handleDisconnectionEvent() tableBeingRemoved: " + tableBeingRemoved);
                        if (tableBeingRemoved) return;
                        /* When a disconnection event arrives for a Table which was imported
                         * to DBase through the 'tableImportPin', then the table will be
                         * removed from the DBase.
                         */
                        Plug plug = e.getPlug();
//                        Table table = ((TableSO) ((SharedObjectPlug) pin).getSharedObject()).getTable();
                        Table table = (Table) plug.getHandle().getComponent();
//                        System.out.println("handleDisconnectionEvent table: " + table);
                        ESlateHandle handle = table.getESlateHandle();
//                        System.out.println("handle.isDisposed(): " + handle.isDisposed());
//                        System.out.println("handle.getParentHandle(): " + handle.getParentHandle());
                        if (table != null) {
                            boolean tmp = tableRemovalAllowed;
                            tableRemovalAllowed = true;
                            try{
                                if (databaseTables.contains(table))
                                    removeTable(table, false);
                            }catch (InsufficientPrivilegesException exc) {}
                            tableRemovalAllowed = tmp;
                        }
//                        System.out.println("databaseTables: " + databaseTables.size());
                    }
                });

            } catch (InvalidPlugParametersException e) {
                ESlateOptionPane.showMessageDialog(new JFrame(), e.getMessage(), resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
            } catch (PlugExistsException e) {
                ESlateOptionPane.showMessageDialog(new JFrame(), e.getMessage(), resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }

            handle.addESlateListener(new ESlateAdapter() {
//                private ESlateHandle[] tableHandles = null;

                public void disposingHandle(HandleDisposalEvent e) {
//                    System.out.println("DBase disposingHandle(): " + handle.getComponentName());
                }

                public void handleDisposed(HandleDisposalEvent e) {
                    PerformanceManager pm = PerformanceManager.getPerformanceManager();
/*                    PerformanceTimerGroup ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.CONSTRUCTOR);
                    pm.removePerformanceTimerGroup(ptg, constructorTimer);
                    ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.LOAD_STATE);
                    pm.removePerformanceTimerGroup(ptg, loadTimer);
                    ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.SAVE_STATE);
                    pm.removePerformanceTimerGroup(ptg, saveTimer);
                    ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.INIT_ESLATE_ASPECT);
*/
//                    pm.removePerformanceTimerGroup(ptg, initESlateAspectTimer);
                    pm.removePerformanceListener(perfListener);
                    perfListener = null;
//                    System.out.println("DBase handleDisposed(): " + getTitle());
                }

                public void componentNameChanged(ComponentNameChangedEvent e) {
                    String newName = e.getNewName();
                    if (getTitle().equals(newName)) return;
                    setTitle(newName);
                }
            });

            //Add the handles of the Tables contained in the DBase to its handle
            for (int i=0; i<databaseTables.size(); i++) {
                ESlateHandle tableHandle = ((Table) databaseTables.get(i)).getESlateHandle();
                handle.add(tableHandle);
            }

            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.DBasePrimitives");

            pm.eSlateAspectInitEnded(this);
            pm.stop(initESlateAspectTimer);
            pm.displayTime(initESlateAspectTimer, "", "ms");
        }

        return handle;
    }

    //To be removed with the dependency to ESlateContainer
//    public boolean isDesktopComponent(ESlateHandle handle) {
//        return ((gr.cti.eslate.base.container.ESlateContainer) handle.getESlateMicroworld().getMicroworldEnvironment()).componentOnDesktop(handle);
//    }

    public MaleSingleIFMultipleConnectionProtocolPlug getDBasePlug() {
        return dbasePlug;
    }

    public FemaleSingleIFMultipleConnectionProtocolPlug getTableImportPlug() {
        return tableImportPlug;
    }

    /** Returns the fileDialog used by some of the operations of the DBase. These
     *  operations are save(), saveChanges() and removeTable().
     */
    public ESlateFileDialog getFileDialog() {
        if (fileDialog == null) {
            Frame topLevelFrame = null;
            if (handle != null && handle.getESlateMicroworld() != null)
                topLevelFrame = handle.getESlateMicroworld().getMicroworldEnvironmentFrame();
            fileDialog = new ESlateFileDialog(topLevelFrame);
        }
        return fileDialog;
    }

    /** One can set the FileDialog to be used by the DBase. This ensures that there
     *  is dialog reusability between the DBase and the application which uses it.
     */
    public void setFileDialog(ESlateFileDialog dialog) {
        if (dialog != null)
            fileDialog = dialog;
    }

    /** Creates the PerformanceListener which watches for changes in the enabled state of the
     *  PerformanceManager. If the manager is enabled, the component timers are re-attached.
     */
    private void createPerformanceManagerListener(PerformanceManager pm) {
        if (perfListener != null) return;
        perfListener = new PerformanceAdapter() {
            public void performanceManagerStateChanged(java.beans.PropertyChangeEvent e) {
                boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
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
long start = System.currentTimeMillis();
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
/*        PerformanceTimerGroup constructorGlobalGroup =
          pm.getPerformanceTimerGroupByID(PerformanceManager.CONSTRUCTOR);
        PerformanceTimerGroup eslateAspectGlobalGroup =
          pm.getPerformanceTimerGroupByID(PerformanceManager.INIT_ESLATE_ASPECT);
        PerformanceTimerGroup saveGlobalGroup =
          pm.getPerformanceTimerGroupByID(PerformanceManager.SAVE_STATE);
        PerformanceTimerGroup loadGlobalGroup =
          pm.getPerformanceTimerGroupByID(PerformanceManager.LOAD_STATE);
*/
        // Special case: E-Slate aspect initialization timer. This timer is
        // created individually, because it has to be added to the default global
        // group INIT_ESLATE_ASPECT first and then to the performance timer group
        // of the component. It is the only way to get a measurement by this
        // timer: if the timer is created as a child of the performance timer
        // group of the component, then the component's E-Slate handle (and
        // therefore its E-Slate aspect) will have been created before the timer,
        // making it impossible to measure the time required for the
        // initialization of the component's E-Slate aspect using this timer.
/*        initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
          eslateAspectGlobalGroup, resources.getString("InitESlateAspectTimer"), true
        );
*/
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
      fieldMapTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        loadTimer, resources.getString("FieldMapTimer"), true
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

/*        ESlateHandle h = getESlateHandle();
        PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(h);
        // Add the "initESlateAspectTimer" to the component's performance timer
        // group.
//        pm.addPerformanceTimerGroup(compoTimerGroup, initESlateAspectTimer);

        // If the component's timers have not been constructed yet, then
        // construct them. During constuction, the timers are also attached.
//        constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
//          compoTimerGroup, resources.getString("ConstructorTimer"), true
//        );
        loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
          compoTimerGroup, resources.getString("LoadTimer"), true
        );
        saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
          compoTimerGroup, resources.getString("SaveTimer"), true
        );
//        pm.registerPerformanceTimerGroup(pm.CONSTRUCTOR, constructorTimer, h);
        pm.registerPerformanceTimerGroup(pm.LOAD_STATE, loadTimer, h);
        pm.registerPerformanceTimerGroup(pm.SAVE_STATE, saveTimer, h);
*/
/*        pm.addPerformanceTimerGroup(constructorGlobalGroup, constructorTimer);
        pm.addPerformanceTimerGroup(loadGlobalGroup, loadTimer);
        pm.addPerformanceTimerGroup(saveGlobalGroup, saveTimer);
*/
attachTimersTime = System.currentTimeMillis()-start;
    }

    /** Returns the time recorded by the DBase's 'loadTimer', if the timer is active.
     */
    public long getLoadTime() {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        if (loadTimer == null) return 0;
        return pm.getElapsedTime(loadTimer);
    }

    /** Returns the time recorded by the DBase's 'saveTimer', if the timer is active.
     */
    public long getSaveTime() {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        if (saveTimer == null) return 0;
        return pm.getElapsedTime(saveTimer);
    }

    public static void main(String argv[]) {
        Runtime runtime = Runtime.getRuntime();
        DBase db = null;
        try{
            db = new DBase("First");
        }catch (UnableToInitializeDatabaseException e) {System.exit(1);}

        Table first = new Table();
//table        first.database = db;
        try{
            db._addTable(first, null, false);
        }catch (InvalidTitleException e) {}
        Table second = new Table();
//table        second.database = db;
        try{
            db._addTable(second, null, false);
        }catch (InvalidTitleException e) {}

//System.out.println("db.databaseTables: " + db.databaseTables);
        db.activeTable = (Table) db.databaseTables.get(0);

        try{
            first.addField("id1", Integer.class); //"integer");
            first.addField("male1?", Boolean.class); //"boolean");
            first.addField("salary1", Double.class); //"Double");
            first.addField("name1", String.class); //"string");
            first.addField("time", CTime.class); //"time");
        }catch (InvalidFieldNameException e) {System.out.println(e.message);}
         catch (InvalidKeyFieldException e) {System.out.println(e.message);}
         catch (InvalidFieldTypeException e) {System.out.println(e.message);}
         catch (AttributeLockedException e) {System.out.println(e.getMessage());}

        ArrayList al = new ArrayList();
        al.add(new Integer(10));
        al.add(Boolean.TRUE);
        al.add(new Double(10));
        al.add("dsfsafas");
        al.add("11:15");
System.out.println("Started adding records to first Table");
long st = System.currentTimeMillis();
        for (int i=0; i<300000; i++) {
            try{
                first.addRecord(al, false);
            }catch (Throwable thr) {
                thr.printStackTrace();
            }
        }
System.out.println("Time to add " + first.getRecordCount() + " records: " + (System.currentTimeMillis()-st));
System.out.println("addRecTime1: " + first.addRecTime1);
System.out.println("addRecTime2: " + first.addRecTime2);
System.out.println("addRecTime3: " + first.addRecTime3);
System.out.println("addRecTime4: " + first.addRecTime4);
System.out.println("addRecTime5: " + first.addRecTime5);
System.out.println("addRecTime6: " + first.addRecTime6);
//System.out.println("addRecTime7: " + first.addRecTime6);

        Array a = new Array();
/*        String[][] testData2 = {
            {"1", "2", "3", "4"},
            {"false", "true", null, null},
            {"44.24", "120.1", "33.21", null},
            {"you", "me", null, "everybody"},
            {"2:20", "22:50", "1:15", "1:15"}
        };

        ArrayList a = new ArrayList();
        try{
            for (int k=0; k<4; k++) {
//                Array a = new Array();
                for (int l=0; l<5; l++)
                    a.add(testData2[l][k]);
                first.addRecord(a);
                a.clear();
            }
        }catch (TableNotExpandableException e) {System.out.println(e.message);}
         catch (NoFieldsInTableException e) {System.out.println(e.message);}
         catch (DuplicateKeyException e) {System.out.println(e.message);}
         catch (NullTableKeyException e) {System.out.println(e.message);}
         catch (InvalidDataFormatException e) {System.out.println(e.message);}
*/
/*        //Filling the second Table with data
        char[] test = new char[20];
        for (int i=0; i<20; i++) {
            test[i] = (char)('a' + i);
        }

        first.addField("id", "integer");
        first.addField("b", "boolean");
        first.addField("name3", "string");
        first.addField("name4", "string");

        System.out.println(runtime.totalMemory() + "  " + runtime.freeMemory() + " Used: " + (runtime.totalMemory()-runtime.freeMemory()));


        try {
            for (int i=0; i<1000; i++) {
                a.add((new Integer(i)).toString());
                a.add(new Boolean(false));
                a.add("abc");
//                a.add("abcdefsdfgdaskjfgsakjsdf");
                a.add(new String(test, i%19, 20-(i%19)));
                first.addRecord(a);
            }
        }catch (TableNotExpandableException e) {System.out.println(e.message);}
         catch (NoFieldsInTableException e) {System.out.println(e.message);}
         catch (DuplicateKeyException e) {System.out.println(e.message);}
         catch (NullTableKeyException e) {System.out.println(e.message);}
         catch (InvalidDataFormatException e) {System.out.println(e.message);}
*/
/*        String[][] testData = {
            {"manolis", "george", "vasilis", "aggeliki", "Xrusanthi", null},
            {"1", "2", "30", "40", "50", null},
            {"2.3", "33.21", "-20.1", "44.24", "2.312", "33.21"},
            {"true", null, "true", "false", "false", null},
            {"1/1/1995", "2/3/1998", "3/1/1995", "4/8/2010", "30/7/1999", "1/9/1993"},
            {"2:20", "1:15", "1:15", "2:10", "20:25", "1:15"},
            {"http://1/1", "http://2/2", "http://3/3","http://4/4","http://5/5","http://6/6"},
            {"20:20", "13:20", "12:15", "12:20", "20:25", "10:24"},
            {"0", "2", "30", null, "24", "-5"},
            {"thanasis", "ilias", "basw", "petros", "Xrusanthi", "george"},
            {"2.3", "2.25", "20.1", "4.24", "2312", "332.1"},
            {"true", "true", "false", "true", "false", "true"},
            {"1/1/1995", "20/5/1998", "13/12/1980", "24/8/2100", "30/7/1999", "1/9/1995"},
//            {"20:20", "13:20", "12:15", "12:20", "20:25", "10:24"},
            {"http://1/1", "http://20/20", "http://30/30","http://40/40","http://5/5","http://george/60"}
        };
        try{
            second.addField("name", String.class); //"string");
            second.addField("id", Integer.class); //"integer");
            second.addField("salary", Double.class); //"Double");
            second.addField("male?", Boolean.class); //"boolean");
            second.addField("date", CDate.class); //"date");
            second.addField("time", CTime.class); //"time");
            second.addField("url", URL.class); //"url");
//            second.addField("name2", "string");
//            second.addField("salary2", "Double");
//            second.addField("male2?", "boolean");
//            second.addField("date2", "date");
            second.addField("time2", CTime.class); //"time");
            second.addField("id2", Integer.class); //"integer");
//            second.addField("url2", "url");
        }catch (InvalidFieldNameException e) {System.out.println(e.message);}
         catch (InvalidKeyFieldException e) {System.out.println(e.message);}
         catch (InvalidFieldTypeException e) {System.out.println(e.message);}
         catch (AttributeLockedException e) {System.out.println(e.getMessage());}

        try{
            for (int k=0; k<6; k++) {
                for (int l=0; l<9; l++)
                    a.add(testData[l][k]);
                second.addRecord(a);
                a.clear();
            }
        }catch (TableNotExpandableException e) {System.out.println(e.message);}
         catch (NoFieldsInTableException e) {System.out.println(e.message);}
         catch (DuplicateKeyException e) {System.out.println(e.message);}
         catch (NullTableKeyException e) {System.out.println(e.message);}
         catch (InvalidDataFormatException e) {System.out.println(e.message);}
*/
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;

        try {
            System.out.print(">> ");
            while (!(s = in.readLine()).equals("x")) {
//                System.out.println(s + "   " + s.length());

            if (s.length() > 0) {
            char c = s.charAt(0);
            switch (c) {
                case 'r':
                    try {
                        db.activeTable.addRecord(db.activeTable.fillRecordEntryFormSTDIN(), false);
                    }catch (TableNotExpandableException e) {System.out.println(e.message);}
                     catch (NoFieldsInTableException e) {System.out.println(e.message);}
                     catch (DuplicateKeyException e) {System.out.println(e.message);}
                     catch (NullTableKeyException e) {System.out.println(e.message);}
                     catch (InvalidDataFormatException e) {System.out.println(e.message);}
                    break;
                case 'f':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    System.out.print("Data type: ");
                    String s2 = db.getStringSTDIN();
                    try{
                        db.activeTable._addField(s1, Class.forName(s2), true, true, false, true);
                    }catch (InvalidFieldNameException e) {System.out.println(e.message);}
                     catch (InvalidKeyFieldException e) {System.out.println(e.message);}
                     catch (InvalidFieldTypeException e) {System.out.println(e.message);}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
					 catch (Throwable thr) {thr.printStackTrace();}
                    break;}
                case 'k':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    System.out.print("Data type: ");
                    String s2 = db.getStringSTDIN();
                    try{
                        db.activeTable.addKeyField(s1, Class.forName(s2), true);
                    }catch (InvalidFieldNameException e) {System.out.println(e.message);}
                     catch (InvalidKeyFieldException e) {System.out.println(e.message);}
                     catch (InvalidFieldTypeException e) {System.out.println(e.message);}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
					 catch (Throwable thr) {thr.printStackTrace();}
                    break;}
                case 'c':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    System.out.print("Data type: ");
                    String s2 = db.getStringSTDIN();
                    try{
                        db.activeTable.addField(s1, Class.forName(s2));
                    }catch (InvalidFieldNameException e) {System.out.println(e.message);}
                     catch (InvalidKeyFieldException e) {System.out.println(e.message);}
                     catch (InvalidFieldTypeException e) {System.out.println(e.message);}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
					 catch (Throwable thr) {thr.printStackTrace();}
                    break;}
                case 'm':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    try {
                        db.activeTable.removeField(s1, false);
                    }catch (InvalidFieldNameException e) {System.out.println(e.message);}
                     catch (TableNotExpandableException e) {System.out.println(e.message);}
                     catch (CalculatedFieldExistsException e) {System.out.println(e.message);}
                     catch (FieldNotRemovableException e) {System.out.println(e.message);}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
                    break;}
                case 'M':
                    {System.out.print("Record index: ");
                    Integer i = new Integer(db.getStringSTDIN());
                    try{
                        db.activeTable.removeRecord(i.intValue(), -1, false);
                    }catch (TableNotExpandableException e) {System.out.println(e.message);}
                     catch (InvalidRecordIndexException e) {System.out.println(e.message);}
                    break;}
                case 'e':
                    {System.out.print("Record addition allowed?: ");
                    Boolean b = new Boolean(db.getStringSTDIN());
                    try{
                        db.activeTable.setRecordAdditionAllowed(b.booleanValue());
                    }catch (PropertyVetoException exc) {
                        System.out.println(exc.getMessage());
                    }
                    break;}
                case 'E':
                    System.out.println("Record addition allowed: " +db.activeTable.isRecordAdditionAllowed());
                    break;
                case 'F':
                    {System.out.print("Field index: ");
                    try {
                        Integer i = new Integer(db.getStringSTDIN());
                        System.out.println(db.activeTable.getField(i.intValue()));
                    }catch (InvalidFieldIndexException e) {System.out.println(e.message);}
                    break;}
                case 't':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    try {
                        System.out.println(db.activeTable.getField(s1));
                        System.gc();
                    }catch (InvalidFieldNameException e) {e.printStackTrace();};
                    break;}
                case 'i':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    try{
                        System.out.println(db.activeTable.getFieldIndex(s1));
                    }catch (InvalidFieldNameException e) {e.printStackTrace();};
                    break;}
                case 'n':
                    {System.out.print("Field index: ");
                    try {
                        Integer i = new Integer(db.getStringSTDIN());
                        System.out.println("getFieldname(): " + db.activeTable.getFieldName(i.intValue()));
                    }catch (InvalidFieldIndexException e) {System.out.println(e.message);}
                    break;}
                case 'R':
                    {System.out.print("Record index: ");
                    String s4 = db.getStringSTDIN();
                    Integer i = new Integer(s4);
                    try{
                        System.out.println(db.activeTable.getRow(i.intValue()));
                    }catch (InvalidRecordIndexException e ) {System.out.println(e.message);}
                    break;}
                case 'o':
                    {System.out.println(db.activeTable.getRecordCount());
/*                    ArrayList a1 = db.activeTable.getTableData();
                    ArrayList a2 = db.activeTable.getTableData();
                    for (int i=0; i<a1.size(); i++) {
                        System.out.println((Object) ((ArrayList) a1.get(0)).get(0));
                        System.out.println((Object) ((ArrayList) a2.get(0)).get(0));
                    }
*/
                    System.out.println(runtime.totalMemory() + "  " + runtime.freeMemory() + " Used: " + (runtime.totalMemory()-runtime.freeMemory()));
                    break;}
                case 'u':
                    System.out.println(db.activeTable.getFieldCount());
                    break;
                case 's':
                    System.out.println(db.activeTable.tableFields);
                    break;
                case 'g':
                    {System.out.print("Field index: ");
                    Integer i = new Integer(db.getStringSTDIN());
                    System.out.print("Record index: ");
                    Integer k = new Integer(db.getStringSTDIN());
                    try {
                        System.out.println("getCell(): " + db.activeTable.getCell(i.intValue(), k.intValue()));
                    }catch (InvalidCellAddressException e) {e.printStackTrace();}
                    break;}
                case 'G':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    System.out.print("Record index: ");
                    Integer k = new Integer(db.getStringSTDIN());
                    try {
                        System.out.println("getCell(): " + db.activeTable.getCell(s1, k.intValue()));
                    }catch (InvalidCellAddressException e) {e.printStackTrace();}
                    break;}
                case 'h':
                    {System.out.print("Field index: ");
                    Integer i = new Integer(db.getStringSTDIN());
                    System.out.print("Record index: ");
                    Integer k = new Integer(db.getStringSTDIN());
                    System.out.print("Value: ");
                    String s1 = db.getStringSTDIN();
                    if (s1.equals("")) s1 = null;
                    try {
                        System.out.println("setCell(): " + db.activeTable.setCell(i.intValue(), k.intValue(), s1));
                    }catch (InvalidCellAddressException e) {System.out.println(e.message);}
                     catch (InvalidDataFormatException e) {System.out.println(e.message);}
                     catch (NullTableKeyException e) {System.out.println(e.message);}
                     catch (DuplicateKeyException e) {System.out.println(e.message);}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
                    break;}
                case 'H':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    System.out.print("Record index: ");
                    Integer k = new Integer(db.getStringSTDIN());
                    System.out.print("Value: ");
                    String s2 = db.getStringSTDIN();
                    if (s2.equals("")) s2 = null;
                    try {
                        System.out.println("setCell(): " + db.activeTable.setCell(s1, k.intValue(), s2));
                    }catch (InvalidFieldNameException e) {e.printStackTrace();}
                     catch (InvalidCellAddressException e) {e.printStackTrace();}
                     catch (InvalidDataFormatException e) {System.out.println(e.message);}
                     catch (NullTableKeyException e) {System.out.println(e.message);}
                     catch (DuplicateKeyException e) {System.out.println(e.message);}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
                    break;}
                case 'p':
                    db.activeTable.printTable();
                    break;
                case 'P':
//                    db.activeTable.printPrimaryKeyMap();
                    break;
                case 'a':
                    {System.out.print("Field name: ");
                    String s2 = db.getStringSTDIN();
                    try {
                        db.activeTable.addToKey(s2);
                    }catch (FieldContainsEmptyCellsException e) {System.out.println(e.message);}
                     catch (FieldAlreadyInKeyException e) {System.out.println(e.message);}
                     catch (InvalidFieldNameException e) {System.out.println(e.message);}
                     catch (TableNotExpandableException e) {System.out.println(e.message);}
                     catch (InvalidKeyFieldException e) {System.out.println(e.getMessage());}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
                    break;}
                case 'A':
                    {System.out.print("Field name: ");
                    String s2 = db.getStringSTDIN();
                    try {
                        db.activeTable.removeFromKey(s2);
                    }catch (FieldIsNotInKeyException e) {System.out.println(e.message);}
                     catch (InvalidFieldNameException e) {System.out.println(e.message);}
                     catch (TableNotExpandableException e) {System.out.println(e.message);}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
                    break;}
                case 'l':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    System.out.print("Data type: ");
                    String s2 = db.getStringSTDIN();
                    try {
                        db.activeTable.changeFieldType(s1, Class.forName(s2), true);
                    }catch (FieldIsKeyException e) {e.printStackTrace();}
                     catch (FieldNotEditableException e) {System.out.println(e.message);}
                     catch (InvalidFieldNameException e) {System.out.println(e.message);}
                     catch (InvalidFieldTypeException e) {System.out.println(e.message);}
                     catch (InvalidTypeConversionException e) {System.out.println(e.message);}
                     catch (DataLossException e) {}
                     catch (DependingCalcFieldsException e) {System.out.println(e.message);}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
                     catch (Throwable thr) {thr.printStackTrace();}
                    break;}
/*                case 'q':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    System.out.print("Operand: ");
                    String s2 = db.getStringSTDIN();
                    try {
                        System.out.println(QEquals(s1, s2));
                    }catch (InvalidOperandException e) {e.printStackTrace();}
                    break;}
                case 'Q':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    System.out.print("Operator: ");
                    String s2 = db.getStringSTDIN();
                    System.out.print("Operand: ");
                    String s3 = db.getStringSTDIN();
                    try {
                        System.out.println(QOperator(s1, s2, s3));
                    }catch (InvalidOperandException e) {e.printStackTrace();}
                    break;}
*/                case 'y':
                    {System.out.print("Query string: ");
                    String s1 = db.getStringSTDIN();
                    try{
                        LogicalExpression le = new LogicalExpression(db.activeTable, s1, 0, true);
                    }catch (InvalidLogicalExpressionException e) {e.printStackTrace();}
                    break;}
                case 'X':
                    System.out.println(runtime.totalMemory() + "  " + runtime.freeMemory() + " Used: " + (runtime.totalMemory()-runtime.freeMemory()));
                    break;
                case 'C':
                    System.gc();
                    break;
                case 'T':
                    {System.out.print("Table index: ");
                    Integer k = new Integer(db.getStringSTDIN());
                    System.out.println("Exists?: " + db.activateTable(k.intValue(), false));
                    System.out.println("Index: " + k + ", title: " + db.activeTable.getTitle());
                    break;}
                case 'j':
                    {System.out.print("Table index: ");
                    Integer k = new Integer(db.getStringSTDIN());
                    try{
                        db.join((Table) db.databaseTables.get(k.intValue()));
                    }catch (JoinNoCommonFieldsException e) {System.out.println(e.message);}
                     catch (JoinUnableToPerformException e) {System.out.println(e.message);}
                    System.gc();
                    break;}
                case 'L':
                    System.out.println(db.activeTable.getFieldNames());
                    break;
                case 'J':
                    {System.out.print("Table index: ");
                    Integer k = new Integer(db.getStringSTDIN());
                    String s1 = "a";
                    StringBaseArray fields1 = new StringBaseArray();
                    ArrayList operators = new ArrayList();
                    StringBaseArray fields2 = new StringBaseArray();

                    System.out.print("Fields1: ");
                    s1 = db.getStringSTDIN();
                    while (!s1.equals("")) {
                        fields1.add(s1);
                        System.out.print("Fields1: ");
                        s1 = db.getStringSTDIN();
                    }
                    System.out.print("Operators: ");
                    s1 = db.getStringSTDIN();
                    while (!s1.equals("")) {
                        operators.add(s1);
                        System.out.print("Operators: ");
                        s1 = db.getStringSTDIN();
                    }
                    System.out.print("Fields2: ");
                    s1 = db.getStringSTDIN();
                    while (!s1.equals("")) {
                        fields2.add(s1);
                        System.out.print("Fields2: ");
                        s1 = db.getStringSTDIN();
                    }
                    try{
                        db.th_join((Table) db.databaseTables.get(k.intValue()), fields1, operators, fields2);
                    }catch (JoinMissingFieldOrOperatorException e) {System.out.println(e.message);}
                     catch (JoinMissingFieldException e) {System.out.println(e.message);}
                     catch (InvalidFieldNameException e) {System.out.println(e.message);}
                     catch (JoinIncompatibleFieldTypesException e) {System.out.println(e.message);}
                     catch (InvalidOperatorException e) {System.out.println(e.message);}
                     catch (JoinUnableToPerformException e) {System.out.println(e.message);}
                    break;}
                case 'N':
                    {System.out.print("Table index: ");
                    Integer k = new Integer(db.getStringSTDIN());
                    try{
                        db.intersection((Table) db.databaseTables.get(k.intValue()));
                    }catch (IntersectionTablesNotIdenticalException e) {System.out.println(e.message);}
                    catch (IntersectionUnableToPerformException e) {System.out.println(e.message);}
                    break;}
                case 'U':
                    {System.out.print("Table index: ");
                    Integer k = new Integer(db.getStringSTDIN());
                    try{
                        db.union((Table) db.databaseTables.get(k.intValue()));
                    }catch (UnionTablesNotIdenticalException e) {System.out.println(e.message);}
                    catch (UnionUnableToPerformException e) {System.out.println(e.message);}
                    break;}
                case 'O':
                    {System.out.print("Operand string: ");
                    String s1 = db.getStringSTDIN();
                    try{
                        NumericOperandExpression le = new NumericOperandExpression(s1, db.activeTable);
                    }catch (InvalidOperandExpressionException e) {System.out.println(e.message);}
                     catch (InvalidDataFormatException e) {System.out.println(e.message);}
                    break;}
                case 'S':
                    {System.out.print("Operand string: ");
                    String s1 = db.getStringSTDIN();
                    try{
                        StringOperandExpression le = new StringOperandExpression(s1, db.activeTable);
                    }catch (InvalidOperandExpressionException e) {System.out.println(e.message);}
                     catch (InvalidDataFormatException e) {System.out.println(e.message);}
                    break;}
                case 'D':
                    {System.out.print("Operand string: ");
                    String s1 = db.getStringSTDIN();
                    try{
                        DateOperandExpression le = new DateOperandExpression(s1, s1, true, db.activeTable, 1);
                    }catch (InvalidOperandExpressionException e) {System.out.println(e.message);}
                     catch (InvalidDataFormatException e) {System.out.println(e.message);}
                     catch (IllegalDateOperandExpression e) {System.out.println(e.message);}
                    break;}
                case 'b':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    System.out.print("Formula: ");
                    String s2 = db.getStringSTDIN();
                    try{
                        db.activeTable.addCalculatedField(s1, s2, false, true);
                    }catch (IllegalCalculatedFieldException e) {System.out.println(e.message);}
                     catch (InvalidFormulaException e) {System.out.println(e.message);}
                     catch (InvalidFieldNameException e) {System.out.println(e.message);}
                     catch (InvalidKeyFieldException e) {System.out.println(e.message);}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
                    break;}
                case 'B':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    System.out.print("Formula: ");
                    String s2 = db.getStringSTDIN();
                    try{
                        db.activeTable.changeCalcFieldFormula(s1, s2);
                    }catch (InvalidFieldNameException e) {System.out.println(e.message);}
                     catch (InvalidFormulaException e) {System.out.println(e.message);}
                     catch (AttributeLockedException e) {System.out.println(e.getMessage());}
                    break;}
                case 'q':
                    {System.out.print("Field name: ");
                    String s1 = db.getStringSTDIN();
                    AbstractTableField f;
                    try{
                        f = db.activeTable.getTableField(s1);
                    }catch (InvalidFieldNameException e) {break;}
                    System.out.println("DependsOnFields: " + f.getDependsOnFields() + " DependentCalcFields: " + f.getDependentCalcFields());
                    break;}
                case 'Q':
                    {
                        try{
                            db.newTable();
                        }catch (InsufficientPrivilegesException e) {System.out.println(e.getMessage());}
                        break;
                    }
/*                case 'v':
                    try{
                        db._saveTable();
                    }catch (UnableToSaveException e) {System.out.println(e.message);}
                     catch (InvalidPathException e) {System.out.println(e.message);}
                     catch (UnableToOverwriteException e) {System.out.println(e.message);}
                    break;
*/
                case 'V':
                    {System.out.print("File name: ");
                    String s1 = db.getStringSTDIN();
                    try{
                        db.exportTable(db.activeTable, s1, false);
                    }catch (UnableToSaveException e) {System.out.println(e.message);}
                     catch (InvalidPathException e) {System.out.println(e.message);}
                     catch (InsufficientPrivilegesException e) {System.out.println(e.getMessage());}
                    break;}
                case 'z':
                    try{
                        db.save();
                    }catch (UnableToSaveException e) {System.out.println(e.message);}
                     catch (InvalidPathException e) {System.out.println(e.message);}
                     catch (UnableToOverwriteException e) {System.out.println(e.message);}
                    break;
                case 'Z':
                    {System.out.print("Database file name: ");
                    String s1 = db.getStringSTDIN();
                    try{
                        db.saveAs(s1);
                    }catch (UnableToSaveException e) {System.out.println(e.message);}
                     catch (InvalidPathException e) {System.out.println(e.message);}
                     catch (UnableToOverwriteException e) {System.out.println(e.message);}
                    break;}
/*                case 'd':
                    {File file = null;

                    System.out.print("Database file name: ");
                    String s1 = db.getStringSTDIN();

                    try{
                        file = db.analyzeFileName(s1, "cdb");
                     }catch (InvalidPathException e) {System.out.println(e.message); break;}

                    if (!file.exists()) {
                       System.out.println("File \"" + file.getPath() + "\" does not exist");
                       break;
                    }
//                       throw new InvalidPathException("File \"" + file.getPath() + "\" does not exist");
*/
                    /* Check if the "file" which is about to be opened, contains the
                     * database which is already open in this component. If it does then
                     * inform the user that the database file he specified is already open.
                     * If not, then open the "file".
                     */
/*                    if (db.dbfile != null) {
                        String path = db.dbfile.getPath();
                        if (path.equals(file.getPath())) {
                            System.out.println("This database is already open");
                            break;
                        }
                    }

                    try{
                        ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(file));
                        db = (DBase) inStream.readObject();
                        db.databaseTables = new Array();
//                        Array tableFiles = (Array) db.TableFiles.clone();
//                        db.TableFiles.clear();

                        FileInputStream fis;
                        ObjectInputStream stream2;
                        Table tab;
                        File fl;
//                        System.out.println("size: " + db.TableFiles.size());
                        for (int i=0; i<tableFiles.size(); i++) {
                            fl = (File) tableFiles.get(i);
//                            System.out.println("fl: " + fl);
                            if (fl.exists()) {
                                try{
                                    fis = new FileInputStream(fl);
                                    stream2  = new ObjectInputStream(fis);
                                    tab = (Table) stream2.readObject();
                                    db._addTable(tab);
                                }catch (Exception e) {e.printStackTrace(); System.out.println("Unable to load file \"" + fl.getPath() + "\"");}
                             }else{
                                System.out.println("File \"" + fl.getPath() + "\" does not exist");
                             }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Unable to open database file \"" + file.getPath() + "\". Probably it is not a valid \"database\" file");
//                        throw new UnableToOpenException("Unable to open file \"" + file.getPath() + "\". Probably it is not a valid \"table\" file");
                        break;
                    }
                    if (db.databaseTables.size() > 0)
                        db.activeTable = (Table) db.databaseTables.get(0);
                     else
                        db.activeTable = null;
                    break;}
*/                case 'w':
                    {System.out.print("File name: ");
                    String s1 = db.getStringSTDIN();
                    try{
                        db.loadTable(s1);
                    }catch (UnableToOpenException e) {System.out.println(e.message);}
                     catch (InvalidPathException e) {System.out.println(e.message);}
                     catch (InsufficientPrivilegesException e) {System.out.println(e.getMessage());}
                    break;}
                case 'W':
                    {
                        try{
                            db.removeTable(db.activeTable, true);
                        }catch (InsufficientPrivilegesException e) {System.out.println(e.getMessage());}
                        break;
                    }
                case 'Y':
                    {System.out.print("Text file name: ");
                    String fileName = db.getStringSTDIN();
                    System.out.print("Delimeter: " );
                    String s1 = db.getStringSTDIN();
                    char delim = s1.trim().charAt(0);
                    try{
                        db.importTableFromTextFile(fileName, delim);
                    }catch (UnableToReadFileException e) {System.out.println(e.message);}
                     catch (InvalidPathException e) {System.out.println(e.message);}
                     catch (EmptyFileException e) {System.out.println(e.message);}
                     catch (InvalidTextFileException e) {System.out.println(e.message);}
                     catch (InvalidDelimeterException e) {System.out.println(e.message);}
                     catch (InsufficientPrivilegesException e) {System.out.println(e.getMessage());}
                    break;}
                case 'I':
                    {System.out.print("Text file name: ");
                    String fileName = db.getStringSTDIN().trim();
                    System.out.print("Use delimeter: " );
                    String s1 = db.getStringSTDIN();
                    char delim = s1.trim().charAt(0);
                    System.out.print("Quote data? " );
                    s1 = db.getStringSTDIN();
                    boolean quoteData = false;
                    if (s1.trim().equals("y"))
                        quoteData = true;
                    try{
                        db.exportTableToTextFile(db.getActiveTable(), fileName, delim, quoteData);
                    }catch (UnableToWriteFileException e) {System.out.println(e.message);}
                     catch (InvalidDelimeterException e) {System.out.println(e.message);}
                     catch (InsufficientPrivilegesException e) {System.out.println(e.getMessage());}
                    break;}
                case '1':
//table                    System.out.println(db.activeTable.getDBase());
                    break;
            }

            System.out.print(">> ");
            }//if (s.length() > 0)
            } //while
            System.exit(0);
        }catch(IOException e) {
            e.printStackTrace();
        }


    }

    public String getStringSTDIN() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;

        try {
            s = in.readLine();
            return s;
        }catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}


/** This class contains the binary predicates used for each data type in
 * <b>join</b>, <b>th-join</b> and <b>intersection</b> operations. For each data type (1) a "comparator"
 * is stored, which is used for sorting reasons in the above methods and (2) an
 * "equalComparator" is stored for comparing for equality among objects of the same class.
 */
/*class DataTypeComparators {
    private BinaryPredicate comparator;
    private BinaryPredicate equalComparator;

    protected DataTypeComparators(BinaryPredicate a, BinaryPredicate b) {
        comparator = a;
        equalComparator = b;
    }

    protected BinaryPredicate getComparator() {
        return comparator;
    }

    protected BinaryPredicate getEqualComparator() {
        return equalComparator;
    }
}
*/
