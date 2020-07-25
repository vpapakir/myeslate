package gr.cti.eslate.navigator;

import java.util.ListResourceBundle;


public class NavigatorBeanInfoBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"mouseEntered",      "Mouse entered"},
        {"mouseExited",       "Mouse exited"},
        {"mouseMoved",        "Mouse moved"},

        {"propertyChange",    "Property change"},
        {"vetoableChange",    "Vetoable change"},

        {"componentHidden",   "Component hidden"},
        {"componentShown",    "Component shown"},

        {"menuBarVisible",    "Menu bar visible"},
        {"toolBarVisible",    "Tool bar visible"},
        {"addressBarVisible", "Address bar visible"},
        {"statusBarVisible",  "Status bar visible"},

        {"navigatorClass", "Navigation supplier Java class"},

        {"home", "Home location"}, //26Jun2000
        {"currentLocation", "Current location"}, //26Jun2000

        {"navigatorClassTip", "Java class implementing the web browser"},
        {"addressBarVisibleTip", "Specify if the address bar should appear"},
        {"toolBarVisibleTip", "Specify if the tool bar should appear"},
        {"statusBarVisibleTip", "Specify if the status bar should appear"},
        {"menuBarVisibleTip", "Specify if the menu bar should appear"},
        {"homeTip", "Specify the home page"},
        {"currentLocationTip", "Specify the current location"}
    };
}

