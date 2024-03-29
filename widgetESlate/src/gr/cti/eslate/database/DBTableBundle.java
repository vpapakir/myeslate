/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 29 ��� 2002
 * Time: 8:10:20 ��
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import java.util.ListResourceBundle;

public class DBTableBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"ComponentName",           "Table Editor"},
        {"ConstructorTimer",        "Table Editor constructor"},
        {"LoadTimer",               "Table Editor load"},
        {"SaveTimer",               "Table Editor save"},
        {"InitESlateAspectTimer",   "Table Editor e-Slate part creation"},
        {"ImportTable",             "Import table"},
        {"Yes",                     "Yes"},
        {"No",                      "No"},
        {"OK",                      "OK"},
        {"Cancel",                  "Cancel"},
        {"Confirmation",            "Confirmation"},
        {"Confirm removal",         "Confirm removal"},
        {"Error",                   "Error"},
        {"Warning",                 "Warning"},
        {"Number",                  "Number"},
        {"Alphanumeric",            "Alphanumeric"},
        {"Boolean (Yes/No)",        "Boolean (Yes/No)"},
        {"Date",                    "Date"},
        {"Time",                    "Time"},
        {"CDate",                   "Date"},
        {"CTime",                   "Time"},
        {"URL",                     "URL"},
        {"Image",                   "Image"},
        {"Boolean",                 "Boolean"},

        {"Cut",                     "Cut"},
        {"Copy",                    "Copy"},
        {"Paste",                   "Paste"},
        {"Icon Editor",             "Icon editor..."},
        {"Find Action",             "Find..."},
        {"FindNext Action",         "Find next"},
        {"FindPrevious Action",     "Find previous"},
        {"Multi-column sort Action","Sort..."},
        {"SelectAllRecs Action",    "Select all records"},
        {"Select All Fields Action",                "Select all fields"},
        {"Table Remove Record Action",              "Remove active record..."},
        {"Table Remove Selected Records Action",    "Remove selected records..."},
        {"HomeAction",                              "Home"},
        {"EndAction",                               "End"},
        {"Promote Action",                          "Promote selected records"},

        {"DBTableMsg1",             "Changing the data type of field \""},
        {"DBTableMsg2",             "\" WILL result to loss of data. Do you want to continue?"},
        {"DBTableMsg3",             "Cannot sort tables on fields of type Image"},
        {"DBTableMsg4",             "The new record cannot be inserted in the jTable until its key values are set correctly"},
        {"DBTableMsg5",             "Display field data type icons"},
        {"DBTableMsg6",             "Hide field data type icons"},
        {"DBTableMsg7",             "Open image"},
        {"DBTableMsg8",             "Are you sure you want to remove the active record?"},
        {"DBTableMsg9",             "Changing the key of the jTable may alter its data. Do you want to continue?"},
        {"DatabaseMsg21",           "Continue with the rest of the changes?"},
        {"DatabaseMsg22",           "Continue?"},
        {"DatabaseMsg23",           "Fields of \"Image\" data type cannot be part of the jTable's key"},
        {"DatabaseMsg27",           "Cannot change the formula of calculated field \""},
        {"DatabaseMsg28",           "\", because it is part of the jTable's key"},
        {"DatabaseMsg75",           "Are you sure you want to remove the selected fields?"},
        {"DatabaseMsg76",           "Are you sure you want to remove the selected records?"},
        {"DatabaseMsg90",           "Invalid record number"},
        {"GridChooser1",            "Both horizontal and vertical lines visible"},
        {"GridChooser2",            "Only horizontal grid lines visible"},
        {"GridChooser3",            "Only vertical grid lines visible"},
        {"GridChooser4",            "Grid invisible"},
        {"NewFieldDialogMsg1",      "New field"},
        {"IconEditActionMsg1",      "The icon was not saved in a separate file. Since this fields stores just references to image files, the icon will not be saved"},
        {"AddRecordActionName",     "Add record"},
        {"Not Found",               " not found"},
        {"StartSearchFromTop1",      "No more occurences of "},
        {"StartSearchFromTop2",      ". Press F3 to start search from top"},
        {"StartSearchFromBottom1",   "No more occurences of "},
        {"StartSearchFromBottom2",   ". Press Shift+F3 to start search from bottom"},
        {"Find What",               "Find What"},  //FindDialog
        {"Up",                      "Up"},
        {"Down",                    "Down"},
        {"Direction",               "Direction"},
        {"Search Parameters",       "Search Parameters"},
        {"Match Case",              "Match Case"},
        {"Search Selected Columns", "Search Selected Columns"},
        {"Search Selected Rows",    "Search Selected Rows"},
        {"Find First",              "Find First"},
        {"Find Next",               "Find Next"},
        {"Close",                   "Close"},
        {"Find in Table",           "Find in Table"},
        {"SortField",               "Sort"},
        {"SortFieldAsc",            "Sort ascending"},
        {"SortFieldDesc",           "Sort descending"},
        {"EditFieldDialogMsg",      "Edit field"},
        {"FieldProperties",         "Properties..."},
        {"CurrencyField",           "Currency field"},
        {"AddField",                "Add field..."},
        {"FieldRemove",             "Remove selected fields"},
        {"TableMenuProperties",     "Information..."},
        {"TableMenuPreferences",    "Preferences..."},
        {"Description",             "Description..."},
        {"GridChooser",             "Adjust grid"},
        {"ExportTable",             "Export to text file..."},


        {"Border",                  "Border"},
        {"BorderTip",               "Adjust the Database's border"},
        {"TableHeaderExpansionChangeAllowed",       "Header expandable"},
        {"TableHeaderExpansionChangeAllowedTip",    "The header of the grid can expand to show column data types"},
        {"SortFromColumnHeadersEnabled",            "Sort from column headers enabled"},
        {"SortFromColumnHeadersEnabledTip",         "Determines if the a column will be storted when its header is clicked"},
        {"TablePopupEnabled",                       "Table pop-up menu enabled"},
        {"TablePopupEnabledTip",                    "Enables the pop-up menu displayed when the table is right-clicked"},
        {"ColumnPopupEnabled",                      "Column pop-up menu enabled"},
        {"ColumnPopupEnabledTip",                   "Enables the pop-up menu displayed when a column's header is right-clicked"},
        {"HeaderFont",                              "Column header font"},
        {"HeaderFontTip",                           "Column header font"},
        {"CalculatedFieldHeaderFont",               "Calculated column header font"},
        {"CalculatedFieldHeaderFontTip",            "The font of the header of calculated columns"},
        {"CalculatedFieldHeaderForeground",         "Calculated column header foreground"},
        {"CalculatedFieldHeaderForegroundtTip",     "The foreground color of the header of calculated columns"},
        {"HeaderForeground",                        "Column header foreground"},
        {"HeaderForegroundTip",                     "The foreground color of the header of columns"},
        {"HeaderBackground",                        "Column header background"},
        {"HeaderBackgroundTip",                     "The background color of the header of columns"},
        {"VerticalRowBarBrightColor",               "Vertical record bar bright color"},
        {"VerticalRowBarBrightColorTip",            "The bright color used by the vertical record bar"},
        {"VerticalRowBarColor",                     "Vertical record bar color"},
        {"VerticalRowBarColor",                     "The color used by the vertical record bar"},
        {"VerticalRowBarDarkColorTip",              "Vertical record bar dark color"},
        {"VerticalRowBarDarkColorTip",              "The dark color used by the vertical record bar"},
        {"TwoColorBackgroundEnabled",               "Two colors for backround"},
        {"TwoColorBackgroundEnabledTip",            "Use different background color for successive records"},
        {"OddRowColor",                             "Odd record color"},
        {"OddRowColorTip",                          "The background color of odd records"},
        {"EvenRowColor",                            "Even record color"},
        {"EvenRowColorTip",                         "The background color of even records"},
        {"RecordSelectionChangeAllowed",            "Allow change of record selection"},
        {"RecordSelectionChangeAllowedTip",         "Determines if the record selection can change through the UI"},
        {"ActiveRecordDrawMode",                    "Active record draw mode"},
        {"ActiveRecordDrawModeTip",                 "Customizes the way the active record in indicated in the grid"},
        {"SelectedRecordDrawMode",                  "Selected record draw mode"},
        {"SelectedRecordDrawModeTip",               "Customizes the way the selected records of the grid are displayed"},
        {"NoIndication",                            "�� �ndication"},
        {"OnRowBarOnly",                            "On the row bar only"},
        {"OnTableOnly",                             "On the table only"},
        {"OnRowBarAndTable",                        "On both the row and the table"},
        {"propertyChange",                          "Property change"},
        {"mouseEntered",                            "Mouse entered"},
        {"mouseExited",                             "Mouse exited"},
        {"mouseMoved",                              "Mouse moved"},
        {"vetoableChange",                          "Vetoable change"},
        {"componentHidden",                         "Component hidden"},
        {"componentShown",                          "Component shown"},
        {"dataImportAllowed",                       "Data import allowed"},
        {"dataImported",                            "Data imported"},
        {"dataExported",                            "Data exported"},
        {"transferableCreated",                     "Transferable created"},
        {"DBTable Popup Name",                      "Table pop-up menu"},
        {"Column Popup Name",                       "Column pop-up menu"},
        {"ResetColumnPopup",                        "Do you want to reset the column pop-up menu?"},
        {"ResetColumnPopupTitle",                   "Reset menu"},

    };
}
