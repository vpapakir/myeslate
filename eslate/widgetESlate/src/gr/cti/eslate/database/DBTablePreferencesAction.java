/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 7 Íןו 2002
 * Time: 7:18:24 לל
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import com.objectspace.jgl.Array;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.database.view.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;

import java.awt.*;


public class DBTablePreferencesAction extends AbstractAction {
    DBTable dbTable;
    String errorStr;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    transient ResourceBundle infoBundle;
    transient Locale locale;
    protected static int UPDATE_ACTIVE_TABLE1_POS = 0; //IE
    protected static int UPDATE_ACTIVE_TABLE2_POS = 1; //IE
    protected static int UPDATE_ACTIVE_TABLE3_POS = 2; //IE


    public DBTablePreferencesAction(DBTable dbTable, String name) {
        super(name);
        this.dbTable = dbTable;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public void actionPerformed(ActionEvent e) {
        execute(true, true, true, null);
    }

    void execute(boolean fieldPropertiesScopeIsTable, boolean colorPropertiesScopeIsTable,
                 boolean advancedPropertiesScopeIsTable, Database database) {
//1        dBase.statusToolbarController.setMessageLabelInWaitState();
//1        dBase.dbComponent.setCursor(waitCursor);
        dbTable.setCursor(waitCursor);

//        boolean fieldPropertiesScopeIsTable = (dBase.dbView.getFieldPropertiesScope() == DBView.TABLE_SCOPE);
//        boolean colorPropertiesScopeIsTable = (dBase.dbView.getColorPropertiesScope() == DBView.TABLE_SCOPE);
//        boolean advancedPropertiesScopeIsTable = (dBase.dbView.getAdvancedPropertiesScope() == DBView.TABLE_SCOPE);

/*1        PreferencesDialog.inputValues.clear();
        if (fieldPropertiesScopeIsTable) {
            if (dbTable.table.getTitle() != null)
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg63") + dbTable.table.getTitle() + "\""));
            else
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg64")));
        }else
            PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg65")));
1*/
//1        gr.cti.eslate.database.view.TableView tableView = dbTable.viewStructure.tableView;

//1        PreferencesDialog.activeDBTableTitle = dbTable.table.getTitle();
        Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, dbTable);
        PreferencesDialog prefDialog = new PreferencesDialog(topLevelFrame, dbTable, database);

        prefDialog.setHighlightNonEditable(dbTable.isNonEditableFieldsHighlighted());
//1        PreferencesDialog.inputValues.add(new Boolean(dbTable.isNonEditableFieldsHighlighted()));
        prefDialog.setDateFormatStr(dbTable.table.getDateFormat().toPattern());
//1        PreferencesDialog.inputValues.add(dbTable.table.getDateFormat().toPattern());
        prefDialog.setTimeFormatStr(dbTable.table.getTimeFormat().toPattern());
//1        PreferencesDialog.inputValues.add(dbTable.table.getTimeFormat().toPattern());
        prefDialog.setTableScopeForFieldPropertiesChange(fieldPropertiesScopeIsTable);
//1        PreferencesDialog.inputValues.add(new Boolean(fieldPropertiesScopeIsTable));
/*1        if (colorPropertiesScopeIsTable) {
            if (dbTable.table.getTitle() != null)
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg66") + dbTable.table.getTitle() + "\""));
            else
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg67")));
        }else
            PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg68")));
1*/
        prefDialog.setBackgroundColor(dbTable.getBackgroundColor());
//1        PreferencesDialog.inputValues.add(dbTable.getBackgroundColor());
        prefDialog.setSelectionBackground(dbTable.getSelectionBackground());
//1        PreferencesDialog.inputValues.add(dbTable.getSelectionBackground());
        prefDialog.setGridColor(dbTable.getGridColor());
//1        PreferencesDialog.inputValues.add(dbTable.getGridColor());
        prefDialog.setActiveRecordColor(dbTable.getActiveRecordColor());
//1        PreferencesDialog.inputValues.add(dbTable.getActiveRecordColor());
        prefDialog.setIntegerColor(dbTable.getDataTypeColor(IntegerTableField.DATA_TYPE));
//        PreferencesDialog.inputValues.add(dbTable.getDataTypeColor(IntegerTableField.DATA_TYPE));
        prefDialog.setDoubleColor(dbTable.getDataTypeColor(DoubleTableField.DATA_TYPE));
        prefDialog.setFloatColor(dbTable.getDataTypeColor(FloatTableField.DATA_TYPE));
//1        PreferencesDialog.inputValues.add(dbTable.getDataTypeColor(DoubleTableField.DATA_TYPE));
        prefDialog.setStringColor(dbTable.getDataTypeColor(StringTableField.DATA_TYPE));
//1        PreferencesDialog.inputValues.add(dbTable.getDataTypeColor(StringTableField.DATA_TYPE));
        prefDialog.setBooleanColor(dbTable.getDataTypeColor(BooleanTableField.DATA_TYPE));
//1        PreferencesDialog.inputValues.add(dbTable.getDataTypeColor(BooleanTableField.DATA_TYPE));
        prefDialog.setDateColor(dbTable.getDataTypeColor(DateTableField.DATA_TYPE));
//1        PreferencesDialog.inputValues.add(dbTable.getDataTypeColor(DateTableField.DATA_TYPE));
        prefDialog.setTimeColor(dbTable.getDataTypeColor(TimeTableField.DATA_TYPE));
//1        PreferencesDialog.inputValues.add(dbTable.getDataTypeColor(TimeTableField.DATA_TYPE));
        prefDialog.setUrlColor(dbTable.getDataTypeColor(URLTableField.DATA_TYPE));
//1        PreferencesDialog.inputValues.add(dbTable.getDataTypeColor(URLTableField.DATA_TYPE));
        prefDialog.setTableScopeForColorPropertiesChange(colorPropertiesScopeIsTable);
//1        PreferencesDialog.inputValues.add(new Boolean(colorPropertiesScopeIsTable));
/*1        if (advancedPropertiesScopeIsTable) {
            if (dbTable.table.getTitle() != null)
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg69") + dbTable.table.getTitle() + "\""));
            else
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg70")));
        }else
            PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg71")));
1*/
        prefDialog.setTableFont(dbTable.getTableFont());
//1        PreferencesDialog.inputValues.add(dbTable.getTableFont());
        prefDialog.setRowHeight(dbTable.getRowHeight());
//        PreferencesDialog.inputValues.add(new Integer(dbTable.getRowHeight()));
        prefDialog.setHorizontalLinesVisible(dbTable.isHorizontalLinesVisible());
//1        PreferencesDialog.inputValues.add(new Boolean(dbTable.isHorizontalLinesVisible()));
        prefDialog.setVerticalLinesVisible(dbTable.isVerticalLinesVisible());
//1        PreferencesDialog.inputValues.add(new Boolean(dbTable.isVerticalLinesVisible()));
        prefDialog.setSimultaneousFieldRecordActivation(dbTable.isSimultaneousFieldRecordActivation());
//1        PreferencesDialog.inputValues.add(new Boolean(dbTable.isSimultaneousFieldRecordActivation()));
        prefDialog.setHeaderIconsVisible(dbTable.isHeaderIconsVisible());
//1        PreferencesDialog.inputValues.add(new Boolean(dbTable.isHeaderIconsVisible()));
        prefDialog.setTableScopeForAdvancedPropertiesChange(advancedPropertiesScopeIsTable);
//1        PreferencesDialog.inputValues.add(new Boolean(advancedPropertiesScopeIsTable));
        prefDialog.setOnlyIntegerPart(dbTable.table.isShowIntegerPartOnly());
//1        PreferencesDialog.inputValues.add(new Boolean(dbTable.table.isShowIntegerPartOnly()));
        prefDialog.setExponentiationFormatUsed(dbTable.table.isExponentialFormatUsed());
//1        PreferencesDialog.inputValues.add(new Boolean(dbTable.table.isExponentialFormatUsed()));
        prefDialog.setDecimalSeparatorAlwaysVisible(dbTable.table.isDecimalSeparatorAlwaysShown());
//1        PreferencesDialog.inputValues.add(new Boolean(dbTable.table.isDecimalSeparatorAlwaysShown()));
        char[] c = new char[1];
        c[0] = dbTable.table.getDecimalSeparator();
        prefDialog.setDecimalSeparator(new String(c));
//1        PreferencesDialog.inputValues.add(new String(c));
        prefDialog.setThousandSeparatorVisible(dbTable.table.isThousandSeparatorUsed());
//1        PreferencesDialog.inputValues.add(new Boolean(dbTable.table.isThousandSeparatorUsed()));
        c[0] = dbTable.table.getThousandSeparator();
        prefDialog.setThousandSeparator(new String(c));
//1        PreferencesDialog.inputValues.add(new String(c));

        prefDialog.showDialog(topLevelFrame);

        /* If the OK button was clicked.
         */
        if (prefDialog.returnCode == PreferencesDialog.DIALOG_OK) {
//1            dBase.statusToolbarController.setMessageLabelInWaitState();
            dbTable.setCursor(waitCursor);

            fieldPropertiesScopeIsTable = prefDialog.isTableScopeForFieldPropertiesChange(); //1 ((Boolean) PreferencesDialog.outputValues.at(4)).booleanValue();
            if (database != null) {
                if (fieldPropertiesScopeIsTable)
                    database.dbView.setFieldPropertiesScope(DBView.TABLE_SCOPE);
                else
                    database.dbView.setFieldPropertiesScope(DBView.DATABASE_SCOPE);
            }
/*            if (dBase.updateActiveTable1 != ((Boolean) dBase.db.UIProperties.at(UPDATE_ACTIVE_TABLE1_POS)).booleanValue()) {
                dBase.db.UIProperties.put(UPDATE_ACTIVE_TABLE1_POS, new Boolean(dBase.updateActiveTable1));
                dBase.db.setModified();
            }
*/
//1            if (!PreferencesDialog.inputValues.at(1).equals(PreferencesDialog.outputValues.at(1))) {
            if (dbTable.isNonEditableFieldsHighlighted() == prefDialog.isHighlightNonEditable()) {

                if (!fieldPropertiesScopeIsTable) {
                    database.defaultNonEditableFieldsHighlighted = prefDialog.isHighlightNonEditable(); //1((Boolean) PreferencesDialog.outputValues.at(1)).booleanValue();
                    DBTable dbTable;
                    for (int i=0; i<database.dbTables.size(); i++) {
                        dbTable = (DBTable) database.dbTables.get(i);
                        dbTable.setNonEditableFieldsHighlighted(prefDialog.isHighlightNonEditable()/*1(Boolean) PreferencesDialog.outputValues.at(1)).booleanValue()*/);
                    }
                }else{
                    dbTable.setNonEditableFieldsHighlighted(prefDialog.isHighlightNonEditable()); //1 ((Boolean) PreferencesDialog.outputValues.at(1)).booleanValue());
                }
            }

            String dateFormat = (String) prefDialog.getDateFormatStr(); //1 PreferencesDialog.outputValues.at(2);
            String timeFormat = (String) prefDialog.getTimeFormatStr(); //1 PreferencesDialog.outputValues.at(3);

//1            if (!dateFormat.equals(PreferencesDialog.inputValues.at(1))) {
            if (!dbTable.table.getDateFormat().toPattern().equals(dateFormat)) {
                if (!fieldPropertiesScopeIsTable) {
                    database.defaultDateFormat = dateFormat;
                    /* Inform all the Tables of the DBase in this Database that
                     * the "dateFormat" has changed. Update it in all the tables.
                     */
                    for (int i=0; i<database.dbTables.size(); i++) {
                        ((DBTable) database.dbTables.get(i)).table.setDateFormat(dateFormat);
//                        ((DBTable) dBase.dbTables.get(i)).dbTable.UIProperties.put(DBTable.DATE_FORMAT_POS, dateFormat);
                        ((DBTable) database.dbTables.get(i)).table.setModified();
                    }
                }else{
                    /* Inform only the active DBTable.
                     */
                    dbTable.table.setDateFormat(dateFormat);
//                    dBase.activeDBTable.dbTable.UIProperties.put(DBTable.DATE_FORMAT_POS, dateFormat);
                    dbTable.table.setModified();
                }

                /* Update the Date fields of the currently visible DBTable.
                 */
                AbstractTableField fld;
                for (int i=0; i<dbTable.tableColumns.size(); i++) {
                    AbstractDBTableColumn column = dbTable.tableColumns.get(i);
                    fld = column.tableField; //cf (AbstractTableField) dbTable.tableFields.get(i);
//                    if (fld.getFieldType().getName().equals("java.util.Date") && fld.isDate()) {
                    if (fld.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) {// && fld.isDate()) {
                        dbTable.refreshField(column.tableColumn); //cf (TableColumn) dbTable.tableColumns.get(i));
                    }
                }
            }
//1            if (!timeFormat.equals(PreferencesDialog.inputValues.at(2))) {
            if (!dbTable.table.getTimeFormat().toPattern().equals(timeFormat)) {
                if (!fieldPropertiesScopeIsTable) {
                    database.defaultTimeFormat = timeFormat;
                    /* Inform all the Tables of the DBase in this Database that
                     * the "timeFormat" has changed. Update it in all the tables.
                     */
                    for (int i=0; i<database.dbTables.size(); i++) {
                        ((DBTable) database.dbTables.get(i)).table.setTimeFormat(timeFormat);
//                        ((DBTable) dBase.dbTables.get(i)).dbTable.UIProperties.put(DBTable.TIME_FORMAT_POS, timeFormat);
                        ((DBTable) database.dbTables.get(i)).table.setModified();
                    }
                }else{
                  /* Inform only the DBTable.
                   */
                  dbTable.table.setTimeFormat(timeFormat);
//                  dBase.activeDBTable.dbTable.UIProperties.put(DBTable.TIME_FORMAT_POS, timeFormat);
                  dbTable.table.setModified();
                }

                /* Update the Date fields of the currently visible DBTable.
                 */
                AbstractTableField fld;
                AbstractDBTableColumn column = null;
                for (int i=0; i<dbTable.tableColumns.size(); i++) {
                    column = dbTable.tableColumns.get(i);
                    fld = column.tableField; //cf (AbstractTableField) dbTable.tableFields.get(i);
                    if (fld.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) { // && (!(fld.isDate()))) {
                        dbTable.refreshField(column.tableColumn); //cf (TableColumn) dbTable.tableColumns.get(i));
                    }
                }
            }

            boolean showIntegerPartOnly = prefDialog.isOnlyIntegerPart(); //1 ((Boolean) PreferencesDialog.outputValues.at(26)).booleanValue();
            boolean useExponentialFormat = prefDialog.isExponentiationFormatUsed(); //1 ((Boolean) PreferencesDialog.outputValues.at(27)).booleanValue();
            boolean alwaysShowDecimalSeparator = prefDialog.isDecimalSeparatorAlwaysVisible(); //((Boolean) PreferencesDialog.outputValues.at(28)).booleanValue();
            String decimStr = prefDialog.getDecimalSeparator();
            char decimSeparator = (decimStr.length() > 0)? decimStr.charAt(0): ' ';
//1            char decimSeparator = ((String) PreferencesDialog.outputValues.at(29)).length() > 0? ((String) PreferencesDialog.outputValues.at(29)).charAt(0): ' ';
            boolean useThousandSeparator = prefDialog.isThousandSeparatorVisible(); //(Boolean) PreferencesDialog.outputValues.at(30)).booleanValue();
            String thousandStr = prefDialog.getThousandSeparator();
            char thousandSeparator = (thousandStr.length() > 0)? thousandStr.charAt(0): ' ';
//1            char thousandSeparator = ((String) PreferencesDialog.outputValues.at(31)).length() > 0? ((String) PreferencesDialog.outputValues.at(31)).charAt(0): ' ';

            if (dbTable.table.isShowIntegerPartOnly() != showIntegerPartOnly) {
//1            if (!PreferencesDialog.outputValues.at(26).equals(PreferencesDialog.inputValues.at(26))) {
                if (!fieldPropertiesScopeIsTable) {
                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
                    database.defaultShowIntegerPartOnly = showIntegerPartOnly;
                    for (int i=0; i<database.dbTables.size(); i++)
                        ((DBTable) database.dbTables.get(i)).table.setShowIntegerPartOnly(showIntegerPartOnly);
                }else{
                    /* Inform only the active DBTable.
                     */
                    dbTable.table.setShowIntegerPartOnly(showIntegerPartOnly);
                }
            }
            if (dbTable.table.isExponentialFormatUsed() != useExponentialFormat) {
//1            if (!PreferencesDialog.outputValues.at(27).equals(PreferencesDialog.inputValues.at(27))) {
                if (!fieldPropertiesScopeIsTable) {
                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
                    database.defaultExponentialNumberFormatUsed = useExponentialFormat;
                    for (int i=0; i<database.dbTables.size(); i++)
                        ((DBTable) database.dbTables.get(i)).table.setExponentialFormatUsed(useExponentialFormat);
                }else{
                    /* Inform only the active DBTable.
                     */
                    dbTable.table.setExponentialFormatUsed(useExponentialFormat);
                }
            }
            if (dbTable.table.isDecimalSeparatorAlwaysShown() != alwaysShowDecimalSeparator) {
//1            if (!PreferencesDialog.outputValues.at(28).equals(PreferencesDialog.inputValues.at(28))) {
                if (!fieldPropertiesScopeIsTable) {
                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
                    database.defaultDecimalSeparatorAlwaysShown = alwaysShowDecimalSeparator;
                    for (int i=0; i<database.dbTables.size(); i++)
                        ((DBTable) database.dbTables.get(i)).table.setDecimalSeparatorAlwaysShown(alwaysShowDecimalSeparator);
                }else{
                    /* Inform only the active DBTable.
                     */
                    dbTable.table.setDecimalSeparatorAlwaysShown(alwaysShowDecimalSeparator);
                }
            }
            if (dbTable.table.getDecimalSeparator() != decimSeparator) {
//1            if (!PreferencesDialog.outputValues.at(29).equals(PreferencesDialog.inputValues.at(29))) {
                if (!fieldPropertiesScopeIsTable) {
                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
                    database.defaultDecimalSeparator = decimSeparator;
                    for (int i=0; i<database.dbTables.size(); i++)
                        ((DBTable) database.dbTables.get(i)).table.setDecimalSeparator(decimSeparator);
                }else{
                    /* Inform only the active DBTable.
                     */
                    dbTable.table.setDecimalSeparator(decimSeparator);
                }
            }
            if (dbTable.table.isThousandSeparatorUsed() != useThousandSeparator) {
//1            if (!PreferencesDialog.outputValues.at(30).equals(PreferencesDialog.inputValues.at(30))) {
                if (!fieldPropertiesScopeIsTable) {
                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
                    database.defaultThousandSeparatorUsed = useThousandSeparator;
                    for (int i=0; i<database.dbTables.size(); i++)
                        ((DBTable) database.dbTables.get(i)).table.setThousandSeparatorUsed(useThousandSeparator);
                }else{
                    /* Inform only the active DBTable.
                     */
                    dbTable.table.setThousandSeparatorUsed(useThousandSeparator);
                }
            }
            if (dbTable.table.getThousandSeparator() != thousandSeparator) {
//1            if (!PreferencesDialog.outputValues.at(31).equals(PreferencesDialog.inputValues.at(31))) {
                if (!fieldPropertiesScopeIsTable) {
                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
                    database.defaultThousandSeparator = thousandSeparator;
                    for (int i=0; i<database.dbTables.size(); i++)
                        ((DBTable) database.dbTables.get(i)).table.setThousandSeparator(thousandSeparator);
                }else{
                    /* Inform only the active DBTable.
                     */
                    dbTable.table.setThousandSeparator(thousandSeparator);
                }
            }

            colorPropertiesScopeIsTable = prefDialog.isTableScopeForColorPropertiesChange(); //1 ((Boolean) PreferencesDialog.outputValues.at(17)).booleanValue();
            if (database != null) {
                if (colorPropertiesScopeIsTable)
                    database.dbView.setColorPropertiesScope(DBView.TABLE_SCOPE);
                else
                    database.dbView.setColorPropertiesScope(DBView.DATABASE_SCOPE);
            }

            /* Color changes should affect only the active DBTable.
             */
            boolean backgroundRepainted = false;
            if (colorPropertiesScopeIsTable) {
                Color clr = prefDialog.getBackgroundColor(); //1 (Color) PreferencesDialog.outputValues.at(6);
                if (!dbTable.getBackgroundColor().equals(clr)) {
                    dbTable.setBackgroundColor(clr);
                    backgroundRepainted = true;
                }
                clr = prefDialog.getGridColor(); //1 (Color) PreferencesDialog.outputValues.at(7);
                if (!dbTable.getGridColor().equals(clr))
                    dbTable.setGridColor(clr);
                clr = prefDialog.getSelectionBackground(); //1 (Color) PreferencesDialog.outputValues.at(8);
                if (!dbTable.getSelectionBackground().equals(clr))
                    dbTable.setSelectionBackground(clr);
                clr = prefDialog.getActiveRecordColor(); //1 (Color) PreferencesDialog.outputValues.at(9);
                if ((!dbTable.getActiveRecordColor().equals(clr)) || backgroundRepainted)
                    dbTable.setActiveRecordColor(clr);
                clr = prefDialog.getSelectionForeground();
                if ((!dbTable.getSelectionForeground().equals(clr)) || backgroundRepainted)
                    dbTable.setSelectionForeground(clr);
                clr = prefDialog.getHighlightColor(); //1 (Color) PreferencesDialog.outputValues.at(9);
                if ((!dbTable.getHighlightColor().equals(clr)) || backgroundRepainted)
                    dbTable.setHighlightColor(clr);

/*1                dbTable.setDataTypeColors((Color) PreferencesDialog.outputValues.at(10),
                                                (Color) PreferencesDialog.outputValues.at(11),
                                                (Color) PreferencesDialog.outputValues.at(12),
                                                (Color) PreferencesDialog.outputValues.at(13),
                                                (Color) PreferencesDialog.outputValues.at(14),
                                                (Color) PreferencesDialog.outputValues.at(15),
                                                (Color) PreferencesDialog.outputValues.at(16));
1*/
                dbTable.setDataTypeColors(prefDialog.getIntegerColor(), prefDialog.getFloatColor(),
                                          prefDialog.getDoubleColor(), prefDialog.getStringColor(),
                                          prefDialog.getBooleanColor(), prefDialog.getDateColor(),
                                          prefDialog.getTimeColor(), prefDialog.getUrlColor());

            }else{
                /* Color changes should affect all the dbTables in the database.
                 */
                Color clr1 = prefDialog.getBackgroundColor(); //1 (Color) PreferencesDialog.outputValues.at(6);
                Color clr2 = prefDialog.getGridColor(); //(Color) PreferencesDialog.outputValues.at(7);
                Color clr3 = prefDialog.getSelectionBackground(); //Color) PreferencesDialog.outputValues.at(8);
                Color clr4 = prefDialog.getActiveRecordColor(); //Color) PreferencesDialog.outputValues.at(9);
                Color clr5 = prefDialog.getHighlightColor();
                Color clr6 = prefDialog.getSelectionForeground();
                DBTable tabl;
                for (int i=0; i<database.dbTables.size(); i++) {
                    tabl = (DBTable) database.dbTables.get(i);
                    if (!tabl.getBackgroundColor().equals(clr1)) {
                        tabl.setBackgroundColor(clr1);
                        backgroundRepainted = true;
                    }
                    if (!tabl.getGridColor().equals(clr2))
                        tabl.setGridColor(clr2);
                    if (!tabl.getSelectionBackground().equals(clr3))
                        tabl.setSelectionBackground(clr3);
                    if (!tabl.getActiveRecordColor().equals(clr4) || backgroundRepainted)
                        tabl.setActiveRecordColor(clr4);
                    if (!tabl.getHighlightColor().equals(clr5) || backgroundRepainted)
                        tabl.setHighlightColor(clr5);
                    if (!tabl.getSelectionForeground().equals(clr6) || backgroundRepainted)
                        tabl.setSelectionForeground(clr6);

/*1                    tabl.setDataTypeColors((Color) PreferencesDialog.outputValues.at(10),
                                                (Color) PreferencesDialog.outputValues.at(11),
                                                (Color) PreferencesDialog.outputValues.at(12),
                                                (Color) PreferencesDialog.outputValues.at(13),
                                                (Color) PreferencesDialog.outputValues.at(14),
                                                (Color) PreferencesDialog.outputValues.at(15),
                                                (Color) PreferencesDialog.outputValues.at(16));
1*/
                    tabl.setDataTypeColors(prefDialog.getIntegerColor(), prefDialog.getFloatColor(),
                                           prefDialog.getDoubleColor(), prefDialog.getStringColor(),
                                           prefDialog.getBooleanColor(), prefDialog.getDateColor(),
                                           prefDialog.getTimeColor(), prefDialog.getUrlColor());
                }

                database.defaultBackgroundColor = clr1;
                database.defaultGridColor = clr2;
                database.defaultSelectionColor = clr3;
                database.defaultHighlightColor = clr4;
                database.defaultIntegerColor = prefDialog.getIntegerColor(); //1 Color) PreferencesDialog.outputValues.at(10);
                database.defaultDoubleColor = prefDialog.getDoubleColor(); //1 Color) PreferencesDialog.outputValues.at(11);
                database.defaultFloatColor = prefDialog.getFloatColor();
                database.defaultStringColor = prefDialog.getStringColor(); //1 (Color) PreferencesDialog.outputValues.at(12);
                database.defaultBooleanColor = prefDialog.getBooleanColor(); //1 (Color) PreferencesDialog.outputValues.at(13);
                database.defaultDateColor = prefDialog.getDateColor(); //1 (Color) PreferencesDialog.outputValues.at(14);
                database.defaultTimeColor = prefDialog.getTimeColor(); //(Color) PreferencesDialog.outputValues.at(15);
                database.defaultURLColor = prefDialog.getUrlColor(); //1 (Color) PreferencesDialog.outputValues.at(16);
            }

/* This boolean variable is used to track if the header of the
             * jTable's has been changed through the preferences. In this
             * case we have to redraw the whole tabbedPane and not the
             * jTable, as in the rest of the cases.
             */
            boolean headerChanged = false;

            advancedPropertiesScopeIsTable = prefDialog.isTableScopeForAdvancedPropertiesChange(); //1 (Boolean) PreferencesDialog.outputValues.at(25)).booleanValue();
            if (database != null) {
                if (advancedPropertiesScopeIsTable)
                    database.dbView.setAdvancedPropertiesScope(DBView.TABLE_SCOPE);
                else
                    database.dbView.setAdvancedPropertiesScope(DBView.DATABASE_SCOPE);
            }

            Font newFont = prefDialog.getTableFont(); //1 (Font) PreferencesDialog.outputValues.at(19);
            int newRowHeight = prefDialog.getRowHeight(); //1 new Integer((String) PreferencesDialog.outputValues.at(20)).intValue();
            boolean horLinesVisible = prefDialog.isHorizontalLinesVisible(); //(Boolean) PreferencesDialog.outputValues.at(21)).booleanValue();
            boolean vertLinesVisible = prefDialog.isVerticalLinesVisible(); //1 ((Boolean) PreferencesDialog.outputValues.at(22)).booleanValue();
            boolean simultaneous = prefDialog.isSimultaneousFieldRecordActivation(); //1 ((Boolean) PreferencesDialog.outputValues.at(23)).booleanValue();
            boolean headerIcons = prefDialog.isHeaderIconsVisible(); //1 ((Boolean) PreferencesDialog.outputValues.at(24)).booleanValue();

            if (advancedPropertiesScopeIsTable) {
                /* Changes in advanced preferences should affect only the activeDBTable.
                 */
                if (!newFont.equals(dbTable.getTableFont()))
                    dbTable.setTableFont(newFont);
                if (newRowHeight != dbTable.getRowHeight())
                    dbTable.setRowHeight(newRowHeight);
                if (horLinesVisible != dbTable.isHorizontalLinesVisible())
                    dbTable.setHorizontalLinesVisible(horLinesVisible);
                if (vertLinesVisible != dbTable.isVerticalLinesVisible())
                    dbTable.setVerticalLinesVisible(vertLinesVisible);
                if (simultaneous != dbTable.isSimultaneousFieldRecordActivation())
                    dbTable.setSimultaneousFieldRecordActivation(simultaneous);
                if (headerIcons != dbTable.isHeaderIconsVisible()) {
                    headerChanged = true;
                    dbTable.setHeaderIconsVisible(headerIcons);
                }
            }else{
                /* Changes in advanced preferences should affect should affect the entire
                 * database.
                 */
                DBTable tabl;
                for (int i=0; i<database.dbTables.size(); i++) {
                    tabl = (DBTable) database.dbTables.get(i);
                    if (!newFont.equals(tabl.getTableFont()))
                        tabl.setTableFont(newFont);
                    if (newRowHeight != tabl.getRowHeight())
                        tabl.setRowHeight(newRowHeight);
                    if (horLinesVisible != tabl.isHorizontalLinesVisible())
                        tabl.setHorizontalLinesVisible(horLinesVisible);
                    if (vertLinesVisible != tabl.isVerticalLinesVisible())
                        tabl.setVerticalLinesVisible(vertLinesVisible);
                    if (simultaneous != tabl.isSimultaneousFieldRecordActivation())
                        tabl.setSimultaneousFieldRecordActivation(simultaneous);
                    if (headerIcons != tabl.isHeaderIconsVisible()) {
                        headerChanged = true;
                        tabl.setHeaderIconsVisible(headerIcons);
                    }
                }

                database.defaultTableFont = newFont;
                database.defaultRowHeight = newRowHeight;
                database.defaultHorLinesVisible = horLinesVisible;
                database.defaultVertLinesVisible = vertLinesVisible;
                database.defaultSimultaneousFieldRecordSelection = simultaneous;
                database.defaultHeaderIconsVisible = headerIcons;
            }

            if (!headerChanged) {
                /* Refresh the currently active DBTable, in order for the changes to take
                 * place.
                 */
                dbTable.refresh();
            }else{
/* Resize and repaint the jTable's header.
                 */
                dbTable.jTable.getTableHeader().resizeAndRepaint();
            }

            dbTable.setCursor(defaultCursor);
//1            dBase.statusToolbarController.setMessageLabelColor(Color.white);
//1            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);

        }else{
            dbTable.setCursor(defaultCursor);
        }
    }

}
