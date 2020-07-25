package gr.cti.eslate.logo;

import java.util.ListResourceBundle;

/**
 * English language resources for Logo component's bean info.
 * @version     2.0.0, 24-May-2006
 * @author      G. Birbilis
 * @author      G. Tsironis
 * @author      N. Drossos
 * @author      K. Kyrimis
 */
public class LogoBeanInfoBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"mouseEntered",    "Mouse entered"},
        {"mouseExited",     "Mouse exited"},
        {"mouseMoved",      "Mouse moved"},

        {"propertyChange",  "Property change"},
        {"vetoableChange",  "Vetoable change"},

        {"componentHidden", "Component hidden"},
        {"componentShown",  "Component shown"},

        {"menuBarVisible",        "Menu bar visible"},
        {"toolBarVisible",        "Tool bar visible"},
        {"statusBarVisible",      "Status bar visible"},

        {"menuBarVisibleTip",     "Adjust the visibility of the menu bar"},
        {"toolBarVisibleTip",     "Adjust the visibility of the tool bar"},
        {"statusBarVisibleTip",   "Adjust the visibility of the status bar"},

        {"border",                "Border"},
        {"borderTip",             "Adjust the visibility of Border"},

        {"hasFontSelector", "Toolbar has font selector"},
        {"execQueueMaxSize", "Execution queue maximum size (-1=unlimited)"},
        {"hasFontSelectorTip", "Toolbar has font selector"},
        {"execQueueMaxSizeTip", "Execution queue maximum size (-1=unlimited)"},

        {"lineNumbersVisibleInProgramArea", "Show line numbers in program area"},
        {"lineNumbersVisibleInProgramAreaTip", "Show line numbers in program area"},

        {"lineNumbersVisibleInOutputArea", "Show line numbers in output area"},
        {"lineNumbersVisibleInOutputAreaTip", "Show line numbers in output area"}

    };
}
