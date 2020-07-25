package gr.cti.eslate.database;

import java.util.ListResourceBundle;


public class DatabaseBeanInfoBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"UserMode",        "User mode"},
        {"UserModeTip",     "Specify the user level in which the Database component operates"},
        {"propertyChange",  "Property change"},
        {"mouseEntered",    "Mouse entered"},
        {"mouseExited",     "Mouse exited"},
        {"mouseMoved",      "Mouse moved"},
        {"vetoableChange",  "Vetoable change"},
        {"componentHidden", "Component hidden"},
        {"componentShown",  "Component shown"},
        {"columnMoved",     "Column moved"},
        {"MenuBarVisible",                "Menu bar visible"},
        {"MenuBarVisibleTip",             "Adjust the visibility of the Database's menu system"},
        {"StandardToolBarVisible",        "Standard toolbar visible"},
        {"StandardToolBarVisibleTip",     "Adjust the visibility of the Database's standard toolbar"},
        {"FormattingToolBarVisible",      "Formatting toolbar visible"},
        {"FormattingToolBarVisibleTip",   "Adjust the visibility of the Database's formatting toolbar"},
        {"ToolBar",                       "Toolbar"},
        {"LoadedDatabase",                "Loaded database"},
        {"Border",                        "Border"},
        {"BorderTip",                     "Adjust the Database's border"},
        {"StatusBarVisible",              "Status bar visible"},
        {"StatusBarVisibleTip",           "Adjust the visibility of the Database's status bar"},
        {"TransparentTab",                "Tabbed pane transparent"},
        {"TransparentTabTip",             "Adjust the opaqueness of the Tabbed pane"},
        {"TableFont",                     "Table Header Font"},
        {"TableFontTip",                  "Set the font of the jTable headers"},
        {"FieldsHeaderFont",              "Fields Header Font"},
        {"FieldsHeaderFontTip",           "Set the font of the fields headers"},
        {"TableHeaderExpansionChangeAllowed",   "Table headers expansion state change allowed"},
        {"TableHeaderExpansionChangeAllowedTip","Turns on/off the ability to alter the state of the headers of the jTable's in the Database"},
    };
}

