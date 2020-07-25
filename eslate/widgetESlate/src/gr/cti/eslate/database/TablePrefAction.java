package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Cursor;
import java.awt.Color;
import java.util.ArrayList;
import com.objectspace.jgl.Array;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.database.view.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import java.awt.Font;


public class TablePrefAction extends AbstractAction {
    Icon iconDisabled, iconEnabled;
    Database dBase;
//    String errorStr;
//    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
//    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
//    transient ResourceBundle infoBundle;
//    transient Locale locale;
//    protected static int UPDATE_ACTIVE_TABLE1_POS = 0; //IE
//    protected static int UPDATE_ACTIVE_TABLE2_POS = 1; //IE
//    protected static int UPDATE_ACTIVE_TABLE3_POS = 2; //IE


    public TablePrefAction(Database db, String name){
        super(name);
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
//        locale=ESlateMicroworld.getCurrentLocale();
//        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
//        errorStr = infoBundle.getString("Error");
    }

    public TablePrefAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        super(name);
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
//        locale=ESlateMicroworld.getCurrentLocale();
//        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
//        errorStr = infoBundle.getString("Error");
    }

    public void actionPerformed(ActionEvent e) {
        DBTable dbTable = dBase.activeDBTable;
        if (dbTable != null) {
            boolean fieldPropertiesScopeIsTable = (dBase.dbView.getFieldPropertiesScope() == DBView.TABLE_SCOPE);
            boolean colorPropertiesScopeIsTable = (dBase.dbView.getColorPropertiesScope() == DBView.TABLE_SCOPE);
            boolean advancedPropertiesScopeIsTable = (dBase.dbView.getAdvancedPropertiesScope() == DBView.TABLE_SCOPE);
            dbTable.tablePreferencesAction.execute(fieldPropertiesScopeIsTable, colorPropertiesScopeIsTable,
            advancedPropertiesScopeIsTable, dBase);
        }
/*1        dBase.statusToolbarController.setMessageLabelInWaitState();
        dBase.dbComponent.setCursor(waitCursor);

        boolean fieldPropertiesScopeIsTable = (dBase.dbView.getFieldPropertiesScope() == DBView.TABLE_SCOPE);
        boolean colorPropertiesScopeIsTable = (dBase.dbView.getColorPropertiesScope() == DBView.TABLE_SCOPE);
        boolean advancedPropertiesScopeIsTable = (dBase.dbView.getAdvancedPropertiesScope() == DBView.TABLE_SCOPE);

        PreferencesDialog.inputValues.clear();
        if (fieldPropertiesScopeIsTable) {
            if (dBase.activeDBTable.table.getTitle() != null)
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg63") + dBase.activeDBTable.table.getTitle() + "\""));
            else
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg64")));
        }else
            PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg65")));

        gr.cti.eslate.database.view.TableView tableView = dBase.activeDBTable.viewStructure.tableView;
        PreferencesDialog.inputValues.add(new Boolean(tableView.isNonEditableFieldsHighlighted()));
        PreferencesDialog.inputValues.add(dBase.db.getActiveTable().getDateFormat().toPattern());
        PreferencesDialog.inputValues.add(dBase.db.getActiveTable().getTimeFormat().toPattern());
        PreferencesDialog.inputValues.add(new Boolean(fieldPropertiesScopeIsTable));
        if (colorPropertiesScopeIsTable) {
            if (dBase.activeDBTable.table.getTitle() != null)
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg66") + dBase.activeDBTable.table.getTitle() + "\""));
            else
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg67")));
        }else
            PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg68")));

        PreferencesDialog.inputValues.add(tableView.getBackgroundColor());
        PreferencesDialog.inputValues.add(tableView.getGridColor());
        PreferencesDialog.inputValues.add(tableView.getSelectionBackground());
        PreferencesDialog.inputValues.add(tableView.getHighlightColor());
        PreferencesDialog.inputValues.add(tableView.getIntegerColor());
        PreferencesDialog.inputValues.add(tableView.getDoubleColor());
        PreferencesDialog.inputValues.add(tableView.getStringColor());
        PreferencesDialog.inputValues.add(tableView.getBooleanColor());
        PreferencesDialog.inputValues.add(tableView.getDateColor());
        PreferencesDialog.inputValues.add(tableView.getTimeColor());
        PreferencesDialog.inputValues.add(tableView.getUrlColor());
        PreferencesDialog.inputValues.add(new Boolean(colorPropertiesScopeIsTable));
        if (advancedPropertiesScopeIsTable) {
            if (dBase.activeDBTable.table.getTitle() != null)
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg69") + dBase.activeDBTable.table.getTitle() + "\""));
            else
                PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg70")));
        }else
            PreferencesDialog.inputValues.add(new String(infoBundle.getString("DatabaseMsg71")));

        PreferencesDialog.inputValues.add(tableView.getTableFont());
        PreferencesDialog.inputValues.add(new Integer(tableView.getRowHeight()));
        PreferencesDialog.inputValues.add(new Boolean(tableView.isHorizontalLinesVisible()));
        PreferencesDialog.inputValues.add(new Boolean(tableView.isVerticalLinesVisible()));
        PreferencesDialog.inputValues.add(new Boolean(tableView.isSimultaneousFieldRecordActivation()));
        PreferencesDialog.inputValues.add(new Boolean(tableView.isHeaderIconsVisible()));
        PreferencesDialog.inputValues.add(new Boolean(advancedPropertiesScopeIsTable));
        PreferencesDialog.inputValues.add(new Boolean(dBase.activeDBTable.table.isShowIntegerPartOnly()));
        PreferencesDialog.inputValues.add(new Boolean(dBase.activeDBTable.table.isExponentialFormatUsed()));
        PreferencesDialog.inputValues.add(new Boolean(dBase.activeDBTable.table.isDecimalSeparatorAlwaysShown()));
        char[] c = new char[1];
        c[0] = dBase.activeDBTable.table.getDecimalSeparator();
        PreferencesDialog.inputValues.add(new String(c));
        PreferencesDialog.inputValues.add(new Boolean(dBase.activeDBTable.table.isThousandSeparatorUsed()));
        c[0] = dBase.activeDBTable.table.getThousandSeparator();
        PreferencesDialog.inputValues.add(new String(c));

        PreferencesDialog.activeDBTableTitle = dBase.activeDBTable.table.getTitle();
        PreferencesDialog pf = new PreferencesDialog(dBase.topLevelFrame, dBase.db.getActiveTable(), dBase.dbComponent);
*/
        /* If the OK button was clicked.
         */
/*1        if (PreferencesDialog.clickedButton == 1) {
            dBase.statusToolbarController.setMessageLabelInWaitState();
            dBase.dbComponent.setCursor(waitCursor);

            fieldPropertiesScopeIsTable = ((Boolean) PreferencesDialog.outputValues.at(4)).booleanValue();
            if (fieldPropertiesScopeIsTable)
                dBase.dbView.setFieldPropertiesScope(DBView.TABLE_SCOPE);
            else
                dBase.dbView.setFieldPropertiesScope(DBView.DATABASE_SCOPE);

            if (!PreferencesDialog.inputValues.at(1).equals(PreferencesDialog.outputValues.at(1))) {

                if (!fieldPropertiesScopeIsTable) {
                    dBase.defaultNonEditableFieldsHighlighted = ((Boolean) PreferencesDialog.outputValues.at(1)).booleanValue();
                    DBTable dbTable;
                    for (int i=0; i<dBase.dbTables.size(); i++) {
                        dbTable = (DBTable) dBase.dbTables.at(i);
                        dbTable.setNonEditableFieldsHighlighted(((Boolean) PreferencesDialog.outputValues.at(1)).booleanValue());
                    }
                }else{
                    dBase.activeDBTable.setNonEditableFieldsHighlighted(((Boolean) PreferencesDialog.outputValues.at(1)).booleanValue());
                }
            }

            String dateFormat = (String) PreferencesDialog.outputValues.at(2);
            String timeFormat = (String) PreferencesDialog.outputValues.at(3);

            if (!dateFormat.equals(PreferencesDialog.inputValues.at(1))) {
                if (!fieldPropertiesScopeIsTable) {
                    dBase.defaultDateFormat = dateFormat;
1*/                    /* Inform all the Tables of the DBase in this Database that
                     * the "dateFormat" has changed. Update it in all the tables.
                     */
/*1                    for (int i=0; i<dBase.dbTables.size(); i++) {
                        ((DBTable) dBase.dbTables.at(i)).table.setDateFormat(dateFormat);
//                        ((DBTable) dBase.dbTables.at(i)).dbTable.UIProperties.put(DBTable.DATE_FORMAT_POS, dateFormat);
                        ((DBTable) dBase.dbTables.at(i)).table.setModified();
                    }
                }else{
1*/                    /* Inform only the active DBTable.
                     */
/*1                    dBase.activeDBTable.table.setDateFormat(dateFormat);
//                    dBase.activeDBTable.dbTable.UIProperties.put(DBTable.DATE_FORMAT_POS, dateFormat);
                    dBase.activeDBTable.table.setModified();
                }

1*/                /* Update the Date fields of the currently visible DBTable.
                 */
/*1                AbstractTableField fld;
                for (int i=0; i<dBase.activeDBTable.tableColumns.size(); i++) {
                    fld = (AbstractTableField) dBase.activeDBTable.tableFields.get(i);
//                    if (fld.getFieldType().getName().equals("java.util.Date") && fld.isDate()) {
                    if (fld.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) {// && fld.isDate()) {
                        dBase.activeDBTable.refreshField((TableColumn) dBase.activeDBTable.tableColumns.get(i));
                    }
                }
            }
            if (!timeFormat.equals(PreferencesDialog.inputValues.at(2))) {
                if (!fieldPropertiesScopeIsTable) {
                    dBase.defaultTimeFormat = timeFormat;
1*/                    /* Inform all the Tables of the DBase in this Database that
                     * the "timeFormat" has changed. Update it in all the tables.
                     */
/*1                    for (int i=0; i<dBase.dbTables.size(); i++) {
                        ((DBTable) dBase.dbTables.at(i)).table.setTimeFormat(timeFormat);
//                        ((DBTable) dBase.dbTables.at(i)).dbTable.UIProperties.put(DBTable.TIME_FORMAT_POS, timeFormat);
                        ((DBTable) dBase.dbTables.at(i)).table.setModified();
                    }
                }else{
1*/                  /* Inform only the active DBTable.
                   */
/*1                  dBase.activeDBTable.table.setTimeFormat(timeFormat);
//                  dBase.activeDBTable.dbTable.UIProperties.put(DBTable.TIME_FORMAT_POS, timeFormat);
                  dBase.activeDBTable.table.setModified();
                }
1*/
                /* Update the Date fields of the currently visible DBTable.
                 */
/*1                AbstractTableField fld;
                for (int i=0; i<dBase.activeDBTable.tableColumns.size(); i++) {
                    fld = (AbstractTableField) dBase.activeDBTable.tableFields.get(i);
                    if (fld.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) { // && (!(fld.isDate()))) {
                        dBase.activeDBTable.refreshField((TableColumn) dBase.activeDBTable.tableColumns.get(i));
                    }
                }
            }

            boolean showIntegerPartOnly = ((Boolean) PreferencesDialog.outputValues.at(26)).booleanValue();
            boolean useExponentialFormat = ((Boolean) PreferencesDialog.outputValues.at(27)).booleanValue();
            boolean alwaysShowDecimalSeparator = ((Boolean) PreferencesDialog.outputValues.at(28)).booleanValue();
            char decimSeparator = ((String) PreferencesDialog.outputValues.at(29)).length() > 0? ((String) PreferencesDialog.outputValues.at(29)).charAt(0): ' ';
            boolean useThousandSeparatorBox = ((Boolean) PreferencesDialog.outputValues.at(30)).booleanValue();
            char thousandSeparator = ((String) PreferencesDialog.outputValues.at(31)).length() > 0? ((String) PreferencesDialog.outputValues.at(31)).charAt(0): ' ';

            if (!PreferencesDialog.outputValues.at(26).equals(PreferencesDialog.inputValues.at(26))) {
                if (!fieldPropertiesScopeIsTable) {
1*/                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
/*1                    dBase.defaultShowIntegerPartOnly = showIntegerPartOnly;
                    for (int i=0; i<dBase.dbTables.size(); i++)
                        ((DBTable) dBase.dbTables.at(i)).table.setShowIntegerPartOnly(showIntegerPartOnly);
                }else{
1*/                    /* Inform only the active DBTable.
                     */
/*1                    dBase.activeDBTable.table.setShowIntegerPartOnly(showIntegerPartOnly);
                }
            }
            if (!PreferencesDialog.outputValues.at(27).equals(PreferencesDialog.inputValues.at(27))) {
                if (!fieldPropertiesScopeIsTable) {
1*/                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
/*1                    dBase.defaultExponentialNumberFormatUsed = useExponentialFormat;
                    for (int i=0; i<dBase.dbTables.size(); i++)
                        ((DBTable) dBase.dbTables.at(i)).table.setExponentialFormatUsed(useExponentialFormat);
                }else{
1*/                    /* Inform only the active DBTable.
                     */
/*1                    dBase.activeDBTable.table.setExponentialFormatUsed(useExponentialFormat);
                }
            }
            if (!PreferencesDialog.outputValues.at(28).equals(PreferencesDialog.inputValues.at(28))) {
                if (!fieldPropertiesScopeIsTable) {
1*/                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
/*1                    dBase.defaultDecimalSeparatorAlwaysShown = alwaysShowDecimalSeparator;
                    for (int i=0; i<dBase.dbTables.size(); i++)
                        ((DBTable) dBase.dbTables.at(i)).table.setDecimalSeparatorAlwaysShown(alwaysShowDecimalSeparator);
                }else{
1*/                    /* Inform only the active DBTable.
                     */
/*1                    dBase.activeDBTable.table.setDecimalSeparatorAlwaysShown(alwaysShowDecimalSeparator);
                }
            }
            if (!PreferencesDialog.outputValues.at(29).equals(PreferencesDialog.inputValues.at(29))) {
                if (!fieldPropertiesScopeIsTable) {
1*/                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
/*1                    dBase.defaultDecimalSeparator = decimSeparator;
                    for (int i=0; i<dBase.dbTables.size(); i++)
                        ((DBTable) dBase.dbTables.at(i)).table.setDecimalSeparator(decimSeparator);
                }else{
1*/                    /* Inform only the active DBTable.
                     */
/*1                    dBase.activeDBTable.table.setDecimalSeparator(decimSeparator);
                }
            }
            if (!PreferencesDialog.outputValues.at(30).equals(PreferencesDialog.inputValues.at(30))) {
                if (!fieldPropertiesScopeIsTable) {
1*/                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
/*1                    dBase.defaultThousandSeparatorUsed = useThousandSeparatorBox;
                    for (int i=0; i<dBase.dbTables.size(); i++)
                        ((DBTable) dBase.dbTables.at(i)).table.setThousandSeparatorUsed(useThousandSeparatorBox);
                }else{
1*/                    /* Inform only the active DBTable.
                     */
/*1                    dBase.activeDBTable.table.setThousandSeparatorUsed(useThousandSeparatorBox);
                }
            }
            if (!PreferencesDialog.outputValues.at(31).equals(PreferencesDialog.inputValues.at(31))) {
                if (!fieldPropertiesScopeIsTable) {
1*/                    /* Inform all the Tables of the DBase in this Database that
                     * the "numberFormat" has changed. Update it in all the tables.
                     */
/*1                    dBase.defaultThousandSeparator = thousandSeparator;
                    for (int i=0; i<dBase.dbTables.size(); i++)
                        ((DBTable) dBase.dbTables.at(i)).table.setThousandSeparator(thousandSeparator);
                }else{
1*/                    /* Inform only the active DBTable.
                     */
/*1                    dBase.activeDBTable.table.setThousandSeparator(thousandSeparator);
                }
            }

            colorPropertiesScopeIsTable = ((Boolean) PreferencesDialog.outputValues.at(17)).booleanValue();
            if (colorPropertiesScopeIsTable)
                dBase.dbView.setColorPropertiesScope(DBView.TABLE_SCOPE);
            else
                dBase.dbView.setColorPropertiesScope(DBView.DATABASE_SCOPE);

1*/            /* Color changes should affect only the active DBTable.
             */
/*1            boolean backgroundRepainted = false;
            if (colorPropertiesScopeIsTable) {
                Color clr = (Color) PreferencesDialog.outputValues.at(6);
                if (!dBase.activeDBTable.getBackgroundColor().equals(clr)) {
                    dBase.activeDBTable.setBackgroundColor(clr);
                    backgroundRepainted = true;
                }
                clr = (Color) PreferencesDialog.outputValues.at(7);
                if (!dBase.activeDBTable.getGridColor().equals(clr))
                    dBase.activeDBTable.setGridColor(clr);
                clr = (Color) PreferencesDialog.outputValues.at(8);
                if (!dBase.activeDBTable.getSelectionBackground().equals(clr))
                    dBase.activeDBTable.setSelectionBackground(clr);
                clr = (Color) PreferencesDialog.outputValues.at(9);
                if ((!dBase.activeDBTable.getHighlightColor().equals(clr)) || backgroundRepainted)
                    dBase.activeDBTable.setHighlightColor(clr);

                dBase.activeDBTable.setDataTypeColors((Color) PreferencesDialog.outputValues.at(10),
                                                (Color) PreferencesDialog.outputValues.at(11),
                                                (Color) PreferencesDialog.outputValues.at(12),
                                                (Color) PreferencesDialog.outputValues.at(13),
                                                (Color) PreferencesDialog.outputValues.at(14),
                                                (Color) PreferencesDialog.outputValues.at(15),
                                                (Color) PreferencesDialog.outputValues.at(16));

            }else{
1*/                /* Color changes should affect all the dbTables in the database.
                 */
/*1                Color clr1 = (Color) PreferencesDialog.outputValues.at(6);
                Color clr2 = (Color) PreferencesDialog.outputValues.at(7);
                Color clr3 = (Color) PreferencesDialog.outputValues.at(8);
                Color clr4 = (Color) PreferencesDialog.outputValues.at(9);
                DBTable tabl;
                for (int i=0; i<dBase.dbTables.size(); i++) {
                    tabl = (DBTable) dBase.dbTables.at(i);
                    if (!tabl.getBackgroundColor().equals(clr1)) {
                        tabl.setBackgroundColor(clr1);
                        backgroundRepainted = true;
                    }
                    if (!tabl.getGridColor().equals(clr2))
                        tabl.setGridColor(clr2);
                    if (!tabl.getSelectionBackground().equals(clr3))
                        tabl.setSelectionBackground(clr3);
                    if (!tabl.getHighlightColor().equals(clr4) || backgroundRepainted)
                        tabl.setHighlightColor(clr4);

                    tabl.setDataTypeColors((Color) PreferencesDialog.outputValues.at(10),
                                                (Color) PreferencesDialog.outputValues.at(11),
                                                (Color) PreferencesDialog.outputValues.at(12),
                                                (Color) PreferencesDialog.outputValues.at(13),
                                                (Color) PreferencesDialog.outputValues.at(14),
                                                (Color) PreferencesDialog.outputValues.at(15),
                                                (Color) PreferencesDialog.outputValues.at(16));
                }

                dBase.defaultBackgroundColor = clr1;
                dBase.defaultGridColor = clr2;
                dBase.defaultSelectionColor = clr3;
                dBase.defaultHighlightColor = clr4;
                dBase.defaultIntegerColor = (Color) PreferencesDialog.outputValues.at(10);
                dBase.defaultDoubleColor = (Color) PreferencesDialog.outputValues.at(11);
                dBase.defaultStringColor = (Color) PreferencesDialog.outputValues.at(12);
                dBase.defaultBooleanColor = (Color) PreferencesDialog.outputValues.at(13);
                dBase.defaultDateColor = (Color) PreferencesDialog.outputValues.at(14);
                dBase.defaultTimeColor = (Color) PreferencesDialog.outputValues.at(15);
                dBase.defaultURLColor = (Color) PreferencesDialog.outputValues.at(16);
            }
1*/
            /* This boolean variable is used to track if the header of the
             * jTable's has been changed through the preferences. In this
             * case we have to redraw the whole tabbedPane and not the
             * jTable, as in the rest of the cases.
             */
/*1            boolean headerChanged = false;

            advancedPropertiesScopeIsTable = ((Boolean) PreferencesDialog.outputValues.at(25)).booleanValue();
            if (advancedPropertiesScopeIsTable)
                dBase.dbView.setAdvancedPropertiesScope(DBView.TABLE_SCOPE);
            else
                dBase.dbView.setAdvancedPropertiesScope(DBView.DATABASE_SCOPE);

            Font newFont = (Font) PreferencesDialog.outputValues.at(19);
            int newRowHeight = new Integer((String) PreferencesDialog.outputValues.at(20)).intValue();
            boolean horLinesVisible = ((Boolean) PreferencesDialog.outputValues.at(21)).booleanValue();
            boolean vertLinesVisible = ((Boolean) PreferencesDialog.outputValues.at(22)).booleanValue();
            boolean simultaneous = ((Boolean) PreferencesDialog.outputValues.at(23)).booleanValue();
            boolean headerIcons = ((Boolean) PreferencesDialog.outputValues.at(24)).booleanValue();

            if (advancedPropertiesScopeIsTable) {
1*/                /* Changes in advanced preferences should affect only the activeDBTable.
                 */
/*1                if (!newFont.equals(dBase.activeDBTable.getTableFont()))
                    dBase.activeDBTable.setTableFont(newFont);
                if (newRowHeight != dBase.activeDBTable.getRowHeight())
                    dBase.activeDBTable.setRowHeight(newRowHeight);
                if (horLinesVisible != dBase.activeDBTable.isHorLinesVisible())
                    dBase.activeDBTable.setHorLinesVisible(horLinesVisible);
                if (vertLinesVisible != dBase.activeDBTable.isVertLinesVisible())
                    dBase.activeDBTable.setVertLinesVisible(vertLinesVisible);
                if (simultaneous != dBase.activeDBTable.isSimultaneousFieldRecordActivation())
                    dBase.activeDBTable.setSimultaneousFieldRecordActivation(simultaneous);
                if (headerIcons != tableView.isHeaderIconsVisible()) {
                    headerChanged = true;
                    dBase.activeDBTable.setHeaderIconsVisible(headerIcons);
                }
            }else{
1*/                /* Changes in advanced preferences should affect should affect the entire
                 * database.
                 */
/*1                DBTable tabl;
                for (int i=0; i<dBase.dbTables.size(); i++) {
                    tabl = (DBTable) dBase.dbTables.at(i);
                    if (!newFont.equals(tabl.getTableFont()))
                        tabl.setTableFont(newFont);
                    if (newRowHeight != tabl.getRowHeight())
                        tabl.setRowHeight(newRowHeight);
                    if (horLinesVisible != tabl.isHorLinesVisible())
                        tabl.setHorLinesVisible(horLinesVisible);
                    if (vertLinesVisible != tabl.isVertLinesVisible())
                        tabl.setVertLinesVisible(vertLinesVisible);
                    if (simultaneous != tabl.isSimultaneousFieldRecordActivation())
                        tabl.setSimultaneousFieldRecordActivation(simultaneous);
                    if (headerIcons != tabl.isHeaderIconsVisible()) {
                        headerChanged = true;
                        tabl.setHeaderIconsVisible(headerIcons);
                    }
                }

                dBase.defaultTableFont = newFont;
                dBase.defaultRowHeight = newRowHeight;
                dBase.defaultHorLinesVisible = horLinesVisible;
                dBase.defaultVertLinesVisible = vertLinesVisible;
                dBase.defaultSimultaneousFieldRecordSelection = simultaneous;
                dBase.defaultHeaderIconsVisible = headerIcons;
            }

            if (!headerChanged) {
1*/                /* Refresh the currently active DBTable, in order for the changes to take
                 * place.
                 */
/*1                dBase.activeDBTable.refresh();
            }else{
1*/                /* Resize and repaint the jTable's header.
                 */
/*1                dBase.activeDBTable.jTable.getTableHeader().resizeAndRepaint();
            }

            dBase.dbComponent.setCursor(defaultCursor);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);

        }else{
            dBase.dbComponent.setCursor(defaultCursor);
        }
1*/
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.ciTablePref.setEnabled(b);
    }
}
