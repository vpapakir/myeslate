package gr.cti.eslate.database;

import javax.swing.*;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Dimension;
import java.util.Locale;
import java.util.ResourceBundle;
import gr.cti.eslate.base.*;
import java.awt.Cursor;
import gr.cti.eslate.database.engine.*;
import javax.swing.event.*;
import java.awt.Color;
import gr.cti.eslate.utils.*;
import java.awt.FileDialog;
import java.awt.event.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.Rectangle;
import com.objectspace.jgl.Array;
import javax.swing.table.TableColumn;

public class MyMenuBar extends JMenuBar{
    String menuString;
    transient protected JMenu tableMenu, databaseMenu, fieldMenu, recordMenu;
//    transient protected Font UIFont = new Font("Helvetica", Font.PLAIN, 12);
    transient ResourceBundle infoBundle;
    transient Locale locale;
    transient JCheckBoxMenuItem miFieldFreeze, ci1, ci2, ci3, ci4, ci5, miTableHidden;
    transient JMenu miTableAutoResize, dataTypeMenu;
    transient JRadioButtonMenuItem miTableAutoResizeOff, miTableAutoResizeLast, miTableAutoResizeAll, doubleType;
    transient JMenuItem miFieldSelectAll, miFieldClearSelection, miFieldWidth, miRecordNew, miRecordRemoveSelected;
    transient JMenuItem miDBImport, miDBExport, miTableNew, miTableAuto, miTableAdd, miTableRemove, miTableSaveAs;
//1    transient JMenuItem miTableSaveUISettings;
    transient JMenuItem miRecordSelectedFirst, miDBUserLevel, miDBProperties;
    transient JMenuItem miDBNew, miDBOpen, miDBClose, miDBDescription, miDBRename, miDBSave, miDBSaveAs;


    transient JMenuItem miExit;
    transient JMenuItem miTableDescription, miTableRename, miTableFind, miTableFindNext, miTableFindPrev; //..
    transient JMenuItem miTableSort, miTableUnion, miTableIntersect, miTableJoin, miTableThJoin, miTableInfo; //..
    transient JMenuItem ciTablePref, ciFieldNew, ciFieldEdit, ciFieldRemove, miSortAscending, miSortDescending; //..
    transient JRadioButtonMenuItem strType, boolType, dateType, timeType, urlType, imageType; //..
    transient JMenuItem miRecordRemoveActive, miRecordSelectAll, miRecordInvertSelection, miRecordClearSelection; //..

//    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
//    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
//    public static final int NOVICE_USER_MODE = 0;
//    public static final int ADVANCED_USER_MODE = 1;
//    protected static int UPDATE_ACTIVE_TABLE1_POS = 0; //IE
//    protected static int UPDATE_ACTIVE_TABLE2_POS = 1; //IE
//    protected static int UPDATE_ACTIVE_TABLE3_POS = 2; //IE


//    Dimension menuItemDimension;
    Insets chkBoxMenuItemInsets = new Insets(2, 11, 2, 0);
    Database dBase;
//    static transient ESlateFileDialog fileDialog = DBase.fileDialog;
    boolean menuBarVisible = true;

//    String errorStr;
//    String warningStr;
//    String currDelimiter = ",";

    public MyMenuBar(Database db) {
        super();
        dBase = db;

        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);

//        errorStr = infoBundle.getString("Error");
//        warningStr = infoBundle.getString("Warning");

        createDataBaseMenu();
        createTableMenu();
        createFieldMenu();
        createRecordMenu();
        add(Box.createGlue());
        attachMenuActions();
    }

    // DATABASE MENU
    private void createDataBaseMenu() {
//        menuItemDimension = new Dimension(300, 19);
	      menuString = infoBundle.getString("DatabaseMenu");
	      databaseMenu = (JMenu)add(new JMenu(menuString));

        // DATABASE-->NEW
        menuString = infoBundle.getString("DatabaseMenuNew");
        miDBNew = new JMenuItem(menuString, dBase.newDBAction.getEnabledIcon());
        miDBNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK, false));
        miDBNew.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBNew);


        // DATABASE-->OPEN
        menuString = infoBundle.getString("DatabaseMenuOpen");
        miDBOpen = new JMenuItem(menuString, dBase.openDBAction.getEnabledIcon());
        miDBOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK, false));
        miDBOpen.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBOpen);

        // DATABASE-->CLOSE
        menuString = infoBundle.getString("DatabaseMenuClose");
        miDBClose = new JMenuItem(menuString);
        miDBClose.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBClose);

        databaseMenu.add(new JSeparator());

        // DATABASE-->DESCRIPTION
        menuString = infoBundle.getString("Description");
        miDBDescription = new JMenuItem(menuString);
        miDBDescription.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBDescription);


        // DATABASE-->RENAME
        menuString = infoBundle.getString("DatabaseMenuRename");
        miDBRename = new JMenuItem(menuString);
        miDBRename.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBRename);

        databaseMenu.add(new JSeparator());

        // DATABASE-->SAVE
        menuString = infoBundle.getString("DatabaseMenuSave");

        miDBSave = new JMenuItem(menuString, dBase.saveDBAction.getEnabledIcon());
        miDBSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK, false));
        miDBSave.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBSave);

        // DATABASE-->SAVEAS
        menuString = infoBundle.getString("DatabaseMenuSaveAs");
        miDBSaveAs = new JMenuItem(menuString);
        miDBSaveAs.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBSaveAs);

        databaseMenu.add(new JSeparator());

        // DATABASE-->IMPORT TABLE
        menuString = infoBundle.getString("DatabaseMenuImport");
        miDBImport = new JMenuItem(menuString);
        miDBImport.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBImport);


        // DATABASE-->EXPORT TABLE
        menuString = infoBundle.getString("DatabaseMenuExport");
        miDBExport = new JMenuItem(menuString);
        miDBExport.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBExport);

        databaseMenu.add(new JSeparator());

//      menuItemDimension = new Dimension(300, 19);
        menuString = infoBundle.getString("DatabaseUserLevel");
        miDBUserLevel = new JMenuItem(menuString);
        miDBUserLevel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBUserLevel);

        // DATABASE-->PROPERTIES
        menuString = infoBundle.getString("DatabaseProperties");
        miDBProperties = new JMenuItem(menuString);
        miDBProperties.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        databaseMenu.add(miDBProperties);

        // DATABASE-->EXIT
        menuString = infoBundle.getString("DatabaseMenuExit");
        miExit = new JMenuItem(menuString);
    }

    private void createTableMenu() {
        tableMenu = new JMenu(infoBundle.getString("TableMenu"));
        // TABLE MENU
	      add(tableMenu);
//     menuItemDimension = new Dimension(250, 19);

        // TABLE-->NEW
        menuString = infoBundle.getString("TableMenuNew");
        miTableNew = new JMenuItem(menuString);
        tableMenu.add(miTableNew);

        // TABLE-->AUTOCREATE
        miTableAuto = new JMenuItem(infoBundle.getString("TableMenuAuto"));
        tableMenu.add(miTableAuto);

        tableMenu.add(new JSeparator());

        // TABLE-->LOAD
        menuString = infoBundle.getString("TableMenuLoad");
        miTableAdd = new JMenuItem(menuString);
        tableMenu.add(miTableAdd);

        // TABLE-->REMOVE
        menuString = infoBundle.getString("TableMenuRemove");
        miTableRemove = new JMenuItem(menuString);
        tableMenu.add(miTableRemove);

        // TABLE-->SAVEAS
        menuString = infoBundle.getString("TableMenuSaveAs");
        miTableSaveAs = new JMenuItem(menuString);
        tableMenu.add(miTableSaveAs);

        // TABLE-->SAVE UI SETTINGS
//1        menuString = infoBundle.getString("TableMenuSaveUISettings");
//1        miTableSaveUISettings = new JMenuItem(menuString);
//1        tableMenu.add(miTableSaveUISettings);

        tableMenu.add(new JSeparator());

        // TABLE-->DESCRIPTION
        menuString = infoBundle.getString("Description");
        miTableDescription = new JMenuItem(menuString);
        tableMenu.add(miTableDescription);

        // TABLE-->RENAME
        menuString = infoBundle.getString("TableMenuRename");
        miTableRename = new JMenuItem(menuString);
        tableMenu.add(miTableRename);

        // TABLE-->HIDDEN
        miTableHidden = new JCheckBoxMenuItem(infoBundle.getString("TableMenuHidden"));
//        miTableHidden.setFont(UIFont);
        miTableHidden.setMargin(chkBoxMenuItemInsets);
        tableMenu.add(miTableHidden);

        tableMenu.add(new JSeparator());

        // TABLE-->FIND
        menuString = infoBundle.getString("TableMenuFind");
        miTableFind = new JMenuItem(menuString,dBase.findAction.getEnabledIcon());
        miTableFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK, false));
        tableMenu.add(miTableFind);

        // TABLE-->FIND NEXT
        menuString = infoBundle.getString("TableMenuFindNext");
        miTableFindNext = new JMenuItem(menuString, dBase.findNextAction.getEnabledIcon());
        miTableFindNext.setAccelerator(KeyStroke.getKeyStroke("F3"));
        tableMenu.add(miTableFindNext);


        // TABLE-->FIND PREVIOUS
        menuString = infoBundle.getString("TableMenuFindPrevious");
        miTableFindPrev = new JMenuItem(menuString,dBase.findPrevAction.getEnabledIcon());
        miTableFindPrev.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, KeyEvent.SHIFT_MASK, false));
        tableMenu.add(miTableFindPrev);


        // TABLE-->SORT
        menuString = infoBundle.getString("TableMenuSort");
        miTableSort = new JMenuItem(menuString);
        tableMenu.add(miTableSort);

        tableMenu.add(new JSeparator());

        //TABLE-->UNION
        menuString = infoBundle.getString("TableMenuUnion");
        miTableUnion = new JMenuItem(menuString);
        tableMenu.add(miTableUnion);

        // TABLE-->INTERSECTION
        menuString = infoBundle.getString("TableMenuIntersect");
        miTableIntersect = new JMenuItem(menuString);
        tableMenu.add(miTableIntersect);

        // TABLE-->JOIN
        menuString = infoBundle.getString("TableMenuJoin");
        miTableJoin = new JMenuItem(menuString);
        tableMenu.add(miTableJoin);

        //TABLE-->TH-JOIN
        menuString = infoBundle.getString("TableMenuThJoin");
        miTableThJoin = new JMenuItem(menuString);
        tableMenu.add(miTableThJoin);

	      tableMenu.add(new JSeparator());

        menuString = infoBundle.getString("TableMenuAutoResize");
        miTableAutoResize = new JMenu(menuString);
        //miTableAutoResize.setMargin(chkBoxMenuItemInsets);

        // TABLE-->AUTORESIZE FIELDS-->OFF
        menuString = infoBundle.getString("TableMenuAutoResizeOff");
        miTableAutoResizeOff = new JRadioButtonMenuItem(menuString);
//        miTableAutoResizeOff.setFont(UIFont);

        // TABLE-->AUTORESIZE FIELDS-->LAST FIELD
        menuString = infoBundle.getString("TableMenuAutoResizeLast");
        miTableAutoResizeLast = new JRadioButtonMenuItem(menuString);
//        miTableAutoResizeLast.setFont(UIFont);

        // TABLE-->AUTORESIZE FIELDS-->ALL FIELDS
        menuString = infoBundle.getString("TableMenuAutoResizeAll");
        miTableAutoResizeAll = new JRadioButtonMenuItem(menuString);
//        miTableAutoResizeAll.setFont(UIFont);

        miTableAutoResize.add(miTableAutoResizeOff);
        miTableAutoResize.add(miTableAutoResizeLast);
        miTableAutoResize.add(miTableAutoResizeAll);
        tableMenu.add(miTableAutoResize);

        // TABLE-->PROPERTIES
        menuString = infoBundle.getString("TableMenuProperties");
        miTableInfo = new JMenuItem(menuString);
        tableMenu.add(miTableInfo);

        // TABLE-->PREFERENCES
        menuString = infoBundle.getString("TableMenuPreferences");
        ciTablePref = new JMenuItem(menuString);
        tableMenu.add(ciTablePref);
    }
    private void createFieldMenu() {
        //FIELD MENU
        menuString = infoBundle.getString("FieldMenu");
	      fieldMenu = (JMenu)add(new JMenu(menuString)); //"Field"));
//     menuItemDimension = new Dimension(200, 19);

        // FIELD-->NEW
        menuString = infoBundle.getString("FieldMenuNew");
        ciFieldNew = new JMenuItem(menuString, dBase.addFieldAction.getEnabledIcon());
        fieldMenu.add(ciFieldNew);

        // FIELD-->EDIT
        menuString = infoBundle.getString("FieldMenuEdit");
        ciFieldEdit = new JMenuItem(menuString, dBase.fieldPropertiesAction.getEnabledIcon());
        fieldMenu.add(ciFieldEdit);

        // FIELD-->REMOVE
        menuString = infoBundle.getString("FieldMenuRemove");
        ciFieldRemove = new JMenuItem(menuString, dBase.removeFieldAction.getEnabledIcon());
        fieldMenu.add(ciFieldRemove);

        fieldMenu.add(new JSeparator());

        // FIELD-->EDITABLE
        ci1 = new JCheckBoxMenuItem(infoBundle.getString("FieldMenuEditable"));
        ci1.setMargin(chkBoxMenuItemInsets);
//        ci1.setFont(UIFont);
        fieldMenu.add(ci1);

        //FIELD-->REMOVABLE
        ci2 = new JCheckBoxMenuItem(infoBundle.getString("FieldMenuRemovable"));
        ci2.setMargin(chkBoxMenuItemInsets);
//        ci2.setFont(UIFont);
        fieldMenu.add(ci2);

        //FIELD-->KEY
        menuString = infoBundle.getString("FieldMenuKey");
	      ci3 = new JCheckBoxMenuItem(menuString);
        ci3.setMargin(chkBoxMenuItemInsets);
//        ci3.setFont(UIFont);
        fieldMenu.add(ci3);

        //FIELD-->CALCULATED
        menuString = infoBundle.getString("FieldMenuCalculated");
	      ci4 = new JCheckBoxMenuItem(menuString);
        ci4.setMargin(chkBoxMenuItemInsets);
//        ci4.setFont(UIFont);
        fieldMenu.add(ci4);

        //FIELD-->HIDDEN
        ci5 = new JCheckBoxMenuItem(infoBundle.getString("FieldMenuHidden"));
        ci5.setMargin(chkBoxMenuItemInsets);
//        ci5.setFont(UIFont);
        fieldMenu.add(ci5);

        menuString = infoBundle.getString("FieldMenuDataType");
        dataTypeMenu = new JMenu(menuString); //"Data type");
        dataTypeMenu.setMargin(chkBoxMenuItemInsets);
        menuString = infoBundle.getString("Number");
        doubleType = new JRadioButtonMenuItem(menuString); //"Number");
//        doubleType.setFont(UIFont);
        menuString = infoBundle.getString("Alphanumeric");
        strType = new JRadioButtonMenuItem(menuString); //"Alphanumeric");
//        strType.setFont(UIFont);
        menuString = infoBundle.getString("Boolean (Yes/No)");
	boolType = new JRadioButtonMenuItem(menuString); //"Boolean (Yes/No)");
//        boolType.setFont(UIFont);
        menuString = infoBundle.getString("Date");
        dateType = new JRadioButtonMenuItem(menuString); //"Date");
//        dateType.setFont(UIFont);
        menuString = infoBundle.getString("Time");
        timeType = new JRadioButtonMenuItem(menuString); //"Time");
//        timeType.setFont(UIFont);
        menuString = infoBundle.getString("URL");
        urlType = new JRadioButtonMenuItem(menuString); //"URL");
//        urlType.setFont(UIFont);
        menuString = infoBundle.getString("Image");
        imageType = new JRadioButtonMenuItem(menuString); //"Image");
//        imageType.setFont(UIFont);

        dataTypeMenu.add(doubleType);
        dataTypeMenu.add(strType);
        dataTypeMenu.add(boolType);
        dataTypeMenu.add(dateType);
        dataTypeMenu.add(timeType);
        dataTypeMenu.add(urlType);
        dataTypeMenu.add(imageType);
        fieldMenu.add(dataTypeMenu);

        fieldMenu.addSeparator();

        // FIELD-->SORT-->ASCENDING
        menuString = infoBundle.getString("FieldMenuSortAsc");
        miSortAscending = new JMenuItem(menuString, dBase.sortAscAction.getEnabledIcon());
        fieldMenu.add(miSortAscending);

        // FIELD-->SORT-->DESCENDING
        menuString = infoBundle.getString("FieldMenuSortDesc");
        miSortDescending = new JMenuItem(menuString, dBase.sortDescAction.getEnabledIcon());
        fieldMenu.add(miSortDescending);

        fieldMenu.addSeparator();

        // FIELD-->SELECT ALL
        menuString = infoBundle.getString("FieldMenuSelectAll");
        miFieldSelectAll = new JMenuItem(menuString);
        miFieldSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK+KeyEvent.SHIFT_MASK, false));
        fieldMenu.add(miFieldSelectAll);

        // FIELD-->CLEAR SELECTION
        menuString = infoBundle.getString("FieldMenuClearSel");
        miFieldClearSelection = new JMenuItem(menuString);
        fieldMenu.add(miFieldClearSelection);

        // FIELD-->WIDTH
        menuString = infoBundle.getString("FieldMenuWidth");
        miFieldWidth = new JMenuItem(menuString);
        fieldMenu.add(miFieldWidth);

        // FIELD-->FREEZE
        menuString = infoBundle.getString("FieldMenuFreeze");
        miFieldFreeze = (JCheckBoxMenuItem) fieldMenu.add(new JCheckBoxMenuItem(menuString));
//        miFieldFreeze.setFont(UIFont);
        miFieldFreeze.setMargin(chkBoxMenuItemInsets);
    }

    /*blic void setMiTableAuto(Dimension dim) {
        miTableAuto.setMaximumSize(dim);
        miTableAuto.setMinimumSize(dim);
        miTableAuto.setPreferredSize(dim);
    } */

    public void setMenuEnabled(boolean enabled) {
        databaseMenu.setEnabled(enabled);
        tableMenu.setEnabled(enabled);
        fieldMenu.setEnabled(enabled);
        recordMenu.setEnabled(enabled);
    }

  /*  public void destroyIcons() {
        findIcon.getImage().flush();
        findPrevIcon.getImage().flush();
        removeRecIcon.getImage().flush();
        sortAscIcon.getImage().flush();
        sortDescIcon.getImage().flush();
        addFieldIcon.getImage().flush();
        removeFieldIcon.getImage().flush();
        fieldPropertiesIcon.getImage().flush();
        addRecIcon.getImage().flush();
        removeSelRecsIcon.getImage().flush();
        selectAllRecsIcon.getImage().flush();
        promoteSelectedRecsIcon.getImage().flush();
        findDisabledIcon.getImage().flush();
        findNextDisabledIcon.getImage().flush();
        findPrevDisabledIcon.getImage().flush();
        removeRecDisabledIcon.getImage().flush();
        saveDBDisabledIcon.getImage().flush();
        sortAscDisabledIcon.getImage().flush();
        sortDescDisabledIcon.getImage().flush();
        addFieldDisabledIcon.getImage().flush();
        removeFieldDisabledIcon.getImage().flush();
        fieldPropertiesDisabledIcon.getImage().flush();
        addRecDisabledIcon.getImage().flush();
        removeSelRecsDisabledIcon.getImage().flush();
        selectAllRecsDisabledIcon.getImage().flush();
        invertRecSelectionDisabledIcon.getImage().flush();
        clearRecSelectionDisabledIcon.getImage().flush();
        promoteSelectedRecsDisabledIcon.getImage().flush();
        findIcon = null; findNextIcon = null; findPrevIcon = null; removeRecIcon = null;
        newDBIcon = null; openDBIcon = null; saveDBIcon = null;
        sortAscIcon = null; sortDescIcon = null; addFieldIcon = null; removeFieldIcon = null;
        fieldPropertiesIcon = null;
        addRecIcon = null; removeSelRecsIcon = null; selectAllRecsIcon = null;
        invertRecSelectionIcon = null; clearRecSelectionIcon = null; promoteSelectedRecsIcon = null;
        findDisabledIcon = null; findNextDisabledIcon = null; findPrevDisabledIcon = null;
        removeRecDisabledIcon = null; saveDBDisabledIcon = null;
        sortAscDisabledIcon = null; sortDescDisabledIcon = null; addFieldDisabledIcon = null;
        removeFieldDisabledIcon = null; fieldPropertiesDisabledIcon = null;
        addRecDisabledIcon = null; removeSelRecsDisabledIcon = null;
        selectAllRecsDisabledIcon = null; invertRecSelectionDisabledIcon = null;
        clearRecSelectionDisabledIcon = null; promoteSelectedRecsDisabledIcon = null;
    } */

    private void createRecordMenu(){
        // RECORD MENU
        menuString = infoBundle.getString("RecordMenu");
	      recordMenu = (JMenu)add(new JMenu(menuString));
//	      menuItemDimension = new Dimension(280, 19);

        // RECORD-->NEW
        menuString = infoBundle.getString("RecordMenuNew");
        miRecordNew = new JMenuItem(menuString, dBase.addRecordAction.getEnabledIcon());
        miRecordNew.setAccelerator(KeyStroke.getKeyStroke("INSERT"));
        recordMenu.add(miRecordNew);

        // RECORD-->REMOVE SELECTED
        menuString = infoBundle.getString("RecordMenuRemoveSelected");
        miRecordRemoveSelected = new JMenuItem(menuString, dBase.removeSelRecordAction.getEnabledIcon());
        miRecordRemoveSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.SHIFT_MASK, false));
        recordMenu.add(miRecordRemoveSelected);

        // RECORD-->REMOVE ACTIVE RECORD
        menuString = infoBundle.getString("RecordMenuRemoveActive");
        miRecordRemoveActive = new JMenuItem(menuString, dBase.removeRecordAction.getEnabledIcon());
        miRecordRemoveActive.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        recordMenu.add(miRecordRemoveActive);

        recordMenu.addSeparator();

        // RECORD-->SELECT ALL
        menuString = infoBundle.getString("RecordMenuSelectAll");
        miRecordSelectAll = new JMenuItem(menuString, dBase.selectAllRecsAction.getEnabledIcon());
        miRecordSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK, false));
        recordMenu.add(miRecordSelectAll);

        // RECORD-->INVERT SELECTION
        menuString = infoBundle.getString("RecordMenuInvertSel");
        miRecordInvertSelection = new JMenuItem(menuString, dBase.invertRecAction.getEnabledIcon());
        recordMenu.add(miRecordInvertSelection);

        // RECORD-->CLEAR SELECTION
        menuString = infoBundle.getString("RecordMenuClearSel");
        miRecordClearSelection = new JMenuItem(menuString, dBase.clearRecAction.getEnabledIcon());
        recordMenu.add(miRecordClearSelection);

        // RECORD-->SELECTED FIRST
        menuString = infoBundle.getString("RecordMenuSelectedFirst");
        miRecordSelectedFirst = new JMenuItem(menuString, dBase.promoteSelRecsAction.getEnabledIcon());
        miRecordSelectedFirst.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, KeyEvent.CTRL_MASK, false));
        recordMenu.add(miRecordSelectedFirst);
    }

    private void attachMenuActions() {
        // DATABASE-->NEW
        miDBNew.addActionListener(dBase.newDBAction);
        // DATABASE-->OPEN
        miDBOpen.addActionListener(dBase.openDBAction);
        // DATABASE-->CLOSE
        miDBClose.addActionListener(dBase.closeAction);
        // DATABASE-->DESCRIPTION
        miDBDescription.addActionListener(dBase.descriptionAction);
        // DATABASE-->RENAME
        miDBRename.addActionListener(dBase.renameAction);
        // DATABASE-->SAVE
        miDBSave.addActionListener(dBase.saveDBAction);
        // DATABASE-->SAVEAS
        miDBSaveAs.addActionListener(dBase.saveAsAction);

        // DATABASE-->IMPORT TABLE
	      miDBImport.addActionListener(dBase.importTableAction);

        // DATABASE-->EXPORT TABLE
	      miDBExport.addActionListener(dBase.exportTableAction);

        // DATABASE-->USER LEVEL
        miDBUserLevel.addActionListener(dBase.userLevelAction);


        // DATABASE-->PROPERTIES
        miDBProperties.addActionListener(dBase.propertiesAction);

        // DATABASE-->EXIT
	      /*miExit.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
                /* Remove any pending record */
/*        if (dBase.pendingNewRecord) {
                    try{
                        dBase.activeDBTable.dbTable.removeRecord(dBase.activeDBTable.dbTable.getRecordCount()-1, dBase.activeDBTable.dbTable.getRecordCount()-1, false);
                    }catch (InvalidRecordIndexException e1) {}
                     catch (TableNotExpandableException e1) {}
                    if (dBase.blockingNewRecord) {
                        dBase.blockingNewRecord = false;
                        databaseMenu.setEnabled(true);
                        tableMenu.setEnabled(true);
                        fieldMenu.setEnabled(true);
                        recordMenu.setEnabled(true);
                        dBase.activeDBTable.dbTable.resetPendingNewRecord();
                    }
                    dBase.pendingNewRecord = false;
                    dBase.activeDBTable.paintSelection();
                }

                /* Check if at least one of the tables of the database has been modified, when the
                 * database itself has not been modified. In this case set the "isModified" flag of
                 * the database, so that the user will be prompted if he wants to save changes before
                 * exiting or not. (The "isModified" flag of the database is set, only when the database
                 * itself changes, as a result of adding/removing tables from it, and not when a jTable
                 * in the database changes.
                 */
/*        if (dBase.db != null && dBase.dbTables != null && !dBase.dbTables.isEmpty() && !dBase.db.isModified()) {
                    DBTable dbTable;
                    for (int i=0; i<dBase.dbTables.size(); i++) {
                        dbTable = (DBTable) dBase.dbTables.at(i);
                        if (dbTable.dbTable.isModified()) {
                            dBase.db.setModified();
                            break;
                        }
                    }
                }

                if (dBase.db!=null && !dBase.db.isNewDatabase() && dBase.db.isModified()) {
                    String dbTitle = dBase.dbComponent.db.getTitle();
                    String dialogTitle;
                    if (dbTitle == null || dbTitle.trim().length()==0)
                        dialogTitle = infoBundle.getString("DatabaseMsg48");
                    else
                        dialogTitle = infoBundle.getString("DatabaseMsg49") + dbTitle + "\" ?";

                    JOptionPane op = new JOptionPane(dialogTitle, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
                    JDialog dialog = op.createDialog(dBase.dbComponent, infoBundle.getString("DatabaseMsg12"));
                    dialog.show();
                    Object value = op.getValue();
                    if (((Integer) value).intValue() == JOptionPane.YES_OPTION) {
                        try{
                            dBase.dbComponent.setCursor(waitCursor);
                            dBase.db.saveChanges(true, dBase.dbComponent);
                            dBase.updateRecentDBList();
                        }catch (UnableToSaveException e1) {
                            dBase.statusBar.messageLabel.setForeground(Color.white);
                            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                            dBase.dbComponent.setCursor(defaultCursor);
                            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                            return;
                         }
                         catch (InvalidPathException e1) {
                            dBase.statusBar.messageLabel.setForeground(Color.white);
                            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                            dBase.dbComponent.setCursor(defaultCursor);
                            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                            return;
                         }
                         catch (UnableToOverwriteException e1) {
                            dBase.statusBar.messageLabel.setForeground(Color.white);
                            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                            dBase.dbComponent.setCursor(defaultCursor);
                            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                            return;
                         }
                    }else if (((Integer) value).intValue() == JOptionPane.CANCEL_OPTION) {
                        dBase.statusBar.messageLabel.setForeground(Color.white);
                        dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                        dBase.dbComponent.setCursor(defaultCursor);
                        return;
                    }
                }

                dBase.statusBar.messageLabel.setForeground(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                dBase.dbComponent.setCursor(defaultCursor);
		        System.exit(0);
	        }
	      });*/

        // TABLE MENU ACTIONS
        miTableNew.addActionListener(dBase.newTableAction);
        miTableAuto.addActionListener(dBase.tableAutoAction);
        miTableAdd.addActionListener(dBase.tableAddAction);
        miTableRemove.addActionListener(dBase.tableRemoveAction);
        miTableSaveAs.addActionListener(dBase.tableSaveAsAction);
//1        miTableSaveUISettings.addActionListener(dBase.tableSaveUISettingsAction);
        miTableDescription.addActionListener(dBase.tableDescriptionAction);
        miTableRename.addActionListener(dBase.tableRenameAction);
        miTableHidden.addActionListener(dBase.tableHiddenAction);
        miTableFind.addActionListener(dBase.findAction);
        miTableFindNext.addActionListener(dBase.findNextAction);
        miTableFindPrev.addActionListener(dBase.findPrevAction);
        miTableSort.addActionListener(dBase.databaseTableSortAction);
        miTableUnion.addActionListener(dBase.tableUnionAction);
        miTableIntersect.addActionListener(dBase.tableIntersectAction);
        miTableJoin.addActionListener(dBase.tableJoinAction);
        miTableThJoin.addActionListener(dBase.tableThJoinAction);
        miTableAutoResizeOff.addActionListener(dBase.tableAutoResizeOffAction);
        miTableAutoResizeLast.addActionListener(dBase.tableAutoResizeLastAction);
        miTableAutoResizeAll.addActionListener(dBase.tableAutoResizeAllAction);
        miTableInfo.addActionListener(dBase.tableInfoAction);
	ciTablePref.addActionListener(dBase.tablePrefAction);

        // FIELD MENU ACTIONS
        ciFieldNew.addActionListener(dBase.addFieldAction);
        ciFieldRemove.addActionListener(dBase.removeFieldAction);
        ciFieldEdit.addActionListener(dBase.fieldPropertiesAction);
        ci1.addActionListener(dBase.fieldEditableAction);
        ci2.addActionListener(dBase.fieldRemovableAction);
        ci3.addActionListener(dBase.fieldKeyAction);
        ci4.addActionListener(dBase.fieldCalculateAction);
        ci5.addActionListener(dBase.fieldHiddenAction);
        doubleType.addActionListener(dBase.fieldDoubleTypeAction);
        strType.addActionListener(dBase.fieldStrTypeAction);
        boolType.addActionListener(dBase.fieldBoolTypeAction);
        dateType.addActionListener(dBase.fieldDateTypeAction);
        timeType.addActionListener(dBase.fieldTimeTypeAction);
        urlType.addActionListener(dBase.fieldURLTypeAction);
        imageType.addActionListener(dBase.fieldImageTypeAction);
        miSortAscending.addActionListener(dBase.sortAscAction);
        miSortDescending.addActionListener(dBase.sortDescAction);
	miFieldSelectAll.addActionListener(dBase.fieldSelectAllAction);
	miFieldClearSelection.addActionListener(dBase.fieldClearSelectionAction);
	miFieldWidth.addActionListener(dBase.fieldWidthAction);
	miFieldFreeze.addActionListener(dBase.fieldFreezeAction);


        //FIELD MENU LISTENER
/*fieldMenu.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                if (dBase.visiblePopupMenu !=null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);

                /* If a jTable cell was edited when the menu was selected, then stop the cell editing.
                 * This results in the evaluation of the edited value of the cell.
                 */
/*        if (dBase.activeDBTable != null && dBase.activeDBTable.jTable.isEditing())
                    ((DefaultCellEditor) ((TableColumn) dBase.activeDBTable.tableColumns.at(dBase.activeDBTable.jTable.getEditingColumn())).getCellEditor()).stopCellEditing();

                if (dBase.dbComponent.pendingNewRecord) {
                    dBase.dbComponent.pendingNewRecord = false;
                    for (int i=0; i<dBase.activeDBTable.colRendererEditors.size(); i++) {
                        ColumnRendererEditor cre = (ColumnRendererEditor) dBase.activeDBTable.colRendererEditors.at(i);
                        if (cre.editor != null) {
                            cre.editor.setClickCountToStart(2);
                        }
                    }
                    dBase.activeDBTable.paintSelection();
                    dBase.activeDBTable.jTable.repaint();
                }

                if (dBase.activeDBTable == null) {
                    ciFieldEdit.setEnabled(false);
                    ci1.setEnabled(false);
                    ci1.setSelected(false);
                    ci2.setEnabled(false);
                    ci2.setSelected(false);
                    ci3.setEnabled(false);
                    ci3.setSelected(false);
                    ci4.setSelected(false);
                    ci4.setEnabled(false);
                    ci5.setSelected(false);
                    ci5.setEnabled(false);
                    ciFieldNew.setEnabled(false);
                    ciFieldRemove.setEnabled(false);
                    dataTypeMenu.setEnabled(false);
                    miFieldSelectAll.setEnabled(false);
                    miFieldClearSelection.setEnabled(false);
                    miFieldWidth.setEnabled(false);
                    miFieldFreeze.setEnabled(false);
   	                miFieldFreeze.setSelected(false);
                    miSortAscending.setEnabled(false);
                    miSortDescending.setEnabled(false);
                    return;
                }else{
                    if (dBase.activeDBTable.dbTable.isFieldAdditionAllowed())
                        ciFieldNew.setEnabled(true);
                    else
                        ciFieldNew.setEnabled(false);
                    if (dBase.activeDBTable.tableColumns.size() > 0) {
                        miFieldSelectAll.setEnabled(true);
                    }else{
                        miFieldSelectAll.setEnabled(false);
                    }
                }

                int numOfSelectedColumns;
                if (!dBase.activeDBTable.simultaneousFieldRecordActivation) {
                    /* No column selection is allowed, so "numOfSelectedColumns" = 0.
                     * However, when a field header is clicked, we explicitly set "columnSelectionAllowed"
                     * to "true", so that the field will be selected. In this case calculate the selected
                     * columns.
                     */
/*            if (dBase.activeDBTable.jTable.getColumnSelectionAllowed())
                        numOfSelectedColumns = dBase.activeDBTable.jTable.getSelectedColumnCount();
                    else
                        numOfSelectedColumns = 0;
                }else
                    numOfSelectedColumns = dBase.activeDBTable.jTable.getSelectedColumnCount();

                if (numOfSelectedColumns == 1) {
                    ciFieldEdit.setEnabled(true);
                    if (dBase.activeDBTable.dbTable.isFieldRemovalAllowed())
                        ciFieldRemove.setEnabled(true);
                    else
                        ciFieldRemove.setEnabled(false);
                    if (dBase.activeDBTable.simultaneousFieldRecordActivation)
                        miFieldClearSelection.setEnabled(false);
                    else
                        miFieldClearSelection.setEnabled(true);
                    miFieldWidth.setEnabled(true);
	                int index = dBase.activeDBTable.jTable.getSelectedColumn();
	                TableColumn col1 = (TableColumn) dBase.activeDBTable.tableColumns.at(index);
	                if (col1.getMinWidth() == col1.getMaxWidth())
	                    miFieldFreeze.setSelected(true);
	                else
	                    miFieldFreeze.setSelected(false);
                    miFieldFreeze.setEnabled(true);
                    miSortAscending.setEnabled(true);
                    miSortDescending.setEnabled(true);
                    TableColumn col = (TableColumn) dBase.activeDBTable.tableColumns.at(dBase.activeDBTable.jTable.getSelectedColumn());
                    String identifier = (String) col.getIdentifier();
                    try{
                        TableField f = dBase.activeDBTable.dbTable.getTableField(identifier);

                        if (f.isFieldEditabilityChangeAllowed())
                            ci1.setEnabled(true);
                        else
                            ci1.setEnabled(false);
                        if (f.isFieldRemovabilityChangeAllowed())
                            ci2.setEnabled(true);
                        else
                            ci2.setEnabled(false);

                        if (dBase.activeDBTable.dbTable.isKeyChangeAllowed() && f.isFieldKeyAttributeChangeAllowed())
                            ci3.setEnabled(true);
                        else
                            ci3.setEnabled(false);
                        if (f.isFieldHiddenAttributeChangeAllowed())
                            ci5.setEnabled(true);
                        else
                            ci5.setEnabled(false);
                        dataTypeMenu.setEnabled(true);

                        if (f.isEditable())
                            ci1.setSelected(true);
                        else
                            ci1.setSelected(false);
                        if (f.isRemovable())
                            ci2.setSelected(true);
                        else
                            ci2.setSelected(false);
                        if (f.isKey())
                            ci3.setSelected(true);
                        else
                            ci3.setSelected(false);
                        if (f.isCalculated()) {
                            ci1.setEnabled(false);
                            ci4.setSelected(true);
                            if (f.isCalcFieldResetAllowed())
                                ci4.setEnabled(true);
                            else
                                ci4.setEnabled(false);
                        }else{
                            if (f.isFieldEditabilityChangeAllowed())
                                ci1.setEnabled(true);
                            else
                                ci1.setEnabled(false);
                            ci4.setEnabled(false);
                            ci4.setSelected(false);
                        }
                        if (f.isHidden())
                            ci5.setSelected(true);
                        else
                            ci5.setSelected(false);

                        doubleType.setSelected(false);
                        doubleType.setIcon(radioButtonResetIcon);
                        strType.setSelected(false);
                        strType.setIcon(radioButtonResetIcon);
                        boolType.setSelected(false);
                        boolType.setIcon(radioButtonResetIcon);
                        dateType.setSelected(false);
                        dateType.setIcon(radioButtonResetIcon);
                        timeType.setSelected(false);
                        timeType.setIcon(radioButtonResetIcon);
                        urlType.setSelected(false);
                        urlType.setIcon(radioButtonResetIcon);
                        imageType.setSelected(false);
                        imageType.setIcon(radioButtonResetIcon);
                        if (dBase.activeDBTable.dbTable.isDataChangeAllowed() && f.isFieldDataTypeChangeAllowed()) {
                            doubleType.setEnabled(true);
                            strType.setEnabled(true);
                            boolType.setEnabled(true);
                            dateType.setEnabled(true);
                            timeType.setEnabled(true);
                            urlType.setEnabled(true);
                            imageType.setEnabled(true);
                        }else{
                            doubleType.setEnabled(false);
                            strType.setEnabled(false);
                            boolType.setEnabled(false);
                            dateType.setEnabled(false);
                            timeType.setEnabled(false);
                            urlType.setEnabled(false);
                            imageType.setEnabled(false);
                        }
                        String fldType = f.getFieldType().getName().substring(f.getFieldType().getName().lastIndexOf('.')+1, f.getFieldType().getName().length());
                        if (fldType.equals("Double")) {
                            doubleType.setIcon(radioButtonIcon);
                        }else if (fldType.equals("String")) {
                            strType.setIcon(radioButtonIcon);
                        }else if (fldType.equals("Boolean")) {
                            boolType.setIcon(radioButtonIcon);
                        }else if (fldType.equals("Date")) {
                            if (f.isDate()) {
                                dateType.setIcon(radioButtonIcon);
                            }else{
                                timeType.setIcon(radioButtonIcon);
                            }
                        }else if (fldType.equals("URL")) {
                            urlType.setIcon(radioButtonIcon);
                        }else if (fldType.equals("CImageIcon")) {
                            imageType.setIcon(radioButtonIcon);
                        }
                    }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in Database createMenuBar(): (16)"); return;}
                }else{
                    miSortAscending.setEnabled(false);
                    miSortDescending.setEnabled(false);
                    ciFieldEdit.setEnabled(false);
                    ci1.setEnabled(false);
                    ci1.setSelected(false);
                    ci2.setEnabled(false);
                    ci2.setSelected(false);
                    ci3.setEnabled(false);
                    ci3.setSelected(false);
                    ci4.setSelected(false);
                    ci4.setEnabled(false);
                    ci5.setSelected(false);
                    ci5.setEnabled(false);
                    dataTypeMenu.setEnabled(false);

                    if (numOfSelectedColumns > 1) {
                        miFieldClearSelection.setEnabled(true);
                        if (dBase.activeDBTable.dbTable.isFieldRemovalAllowed())
                            ciFieldRemove.setEnabled(true);
                        else
                            ciFieldRemove.setEnabled(false);
                        miFieldWidth.setEnabled(true);

	                    int[] indices = dBase.activeDBTable.jTable.getSelectedColumns();
	                    TableColumn col1 = (TableColumn) dBase.activeDBTable.tableColumns.at(indices[0]);
	                    boolean areAllFrozen = true;
	                    if (col1.getMinWidth() == col1.getMaxWidth()) {
        	                for (int i=1; i<indices.length; i++) {
	                            col1 = (TableColumn) dBase.activeDBTable.tableColumns.at(indices[i]);
        	                    if (col1.getMinWidth() == col1.getMaxWidth())
        	                        continue;
        	                    areAllFrozen = false;
        	                    break;
        	                }
        	            }else
        	                areAllFrozen = false;

        	            if (areAllFrozen)
        	                miFieldFreeze.setSelected(true);
        	            else
        	                miFieldFreeze.setSelected(false);
                        miFieldFreeze.setEnabled(true);
                    }else{
                        miFieldClearSelection.setEnabled(false);
                        ciFieldRemove.setEnabled(false);
                        miFieldWidth.setEnabled(false);
                        miFieldFreeze.setEnabled(false);
      	                miFieldFreeze.setSelected(false);
                    }
                }
            }
        });
        */
        // RECORD MENU ACTIONS
        miRecordNew.addActionListener(dBase.addRecordAction);
        miRecordRemoveSelected.addActionListener(dBase.removeSelRecordAction);
        miRecordRemoveActive.addActionListener(dBase.removeRecordAction);
        miRecordSelectAll.addActionListener(dBase.selectAllRecsAction);
        miRecordInvertSelection.addActionListener(dBase.invertRecAction);
        miRecordClearSelection.addActionListener(dBase.clearRecAction);
        miRecordSelectedFirst.addActionListener(dBase.promoteSelRecsAction);


        // recordMenu menu listener
/*recordMenu.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                if (dBase.visiblePopupMenu !=null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);

                /* If a jTable cell was edited when the menu was selected, then stop the cell editing.
                 * This results in the evaluation of the edited value of the cell.
                 */
/*        if (dBase.activeDBTable != null && dBase.activeDBTable.jTable.isEditing())
                    ((DefaultCellEditor) ((TableColumn) dBase.activeDBTable.tableColumns.at(dBase.activeDBTable.jTable.getEditingColumn())).getCellEditor()).stopCellEditing();

                if (dBase.dbComponent.pendingNewRecord) {
                    dBase.dbComponent.pendingNewRecord = false;
                    for (int i=0; i<dBase.activeDBTable.colRendererEditors.size(); i++) {
                        ColumnRendererEditor cre = (ColumnRendererEditor) dBase.activeDBTable.colRendererEditors.at(i);
                        if (cre.editor != null) {
                            cre.editor.setClickCountToStart(2);
                        }
                    }
                    dBase.activeDBTable.paintSelection();
                    dBase.activeDBTable.jTable.repaint();
                }

                if (dBase.activeDBTable == null) {
                    miRecordNew.setEnabled(false);
                    miRecordRemoveSelected.setEnabled(false);
                    miRecordRemoveActive.setEnabled(false);
                    miRecordSelectedFirst.setEnabled(false);
                    miRecordSelectAll.setEnabled(false);
                    miRecordClearSelection.setEnabled(false);
                    miRecordInvertSelection.setEnabled(false);
                }else{
                        if (dBase.activeDBTable.dbTable.getFieldCount() == 0)
                            miRecordNew.setEnabled(false);
                        else{
                            if (dBase.activeDBTable.dbTable.isRecordAdditionAllowed())
                                miRecordNew.setEnabled(true);
                            else
                                miRecordNew.setEnabled(false);
                        }

                    int numOfSelectedRows = dBase.activeDBTable.dbTable.getSelectedSubset().size();
                    if (numOfSelectedRows == 0) {
                        miRecordRemoveSelected.setEnabled(false);
                        miRecordSelectedFirst.setEnabled(false);
                        miRecordClearSelection.setEnabled(false);
                    }else{
                            if (dBase.activeDBTable.dbTable.isRecordRemovalAllowed())
                                miRecordRemoveSelected.setEnabled(true);
                            else
                                miRecordRemoveSelected.setEnabled(false);
                        miRecordSelectedFirst.setEnabled(true);
                        miRecordClearSelection.setEnabled(true);
                    }
                    if (dBase.activeDBTable.activeRow == -1)
                        miRecordRemoveActive.setEnabled(false);
                    else{
                            if (dBase.activeDBTable.dbTable.isRecordRemovalAllowed())
                                miRecordRemoveActive.setEnabled(true);
                            else
                                miRecordRemoveActive.setEnabled(false);
                    }

                    if (dBase.activeDBTable.dbTable.getRecordCount() != 0 &&
                        dBase.activeDBTable.dbTable.getRecordCount() > dBase.activeDBTable.jTable.getSelectedRowCount())
                        miRecordSelectAll.setEnabled(true);
                    else
                        miRecordSelectAll.setEnabled(false);

                    if (dBase.activeDBTable.dbTable.getRecordCount() != 0)
                        miRecordInvertSelection.setEnabled(true);
                    else
                        miRecordInvertSelection.setEnabled(false);
                }
            }
        });*/
    }

}
