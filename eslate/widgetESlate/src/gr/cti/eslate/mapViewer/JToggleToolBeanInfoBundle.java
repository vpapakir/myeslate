package gr.cti.eslate.mapViewer;

import java.util.ListResourceBundle;

/**
 * MapBeanInfo Bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 05-Apr-2000
 */
public class JToggleToolBeanInfoBundle extends ListResourceBundle {
	public Object [][] getContents() {
		return contents;
	}

	static final Object[][] contents={
		{"icon",                    "Icon"},
		{"iconTip",                 "The basic icon of the tool"},
		{"selectedIcon",            "Icon selected"},
		{"selectedIconTip",         "The icon of the tool when it is selected"},
		{"disabledIcon",            "Icon disabled"},
		{"disabledIconTip",         "The icon of the tool when it is disabled"},
		{"disabledSelectedIcon",    "Icon disabled selected"},
		{"disabledSelectedIconTip", "The icon of the tool when it is disabled and selected"},
		{"rolloverIcon",            "Icon mouse rollover"},
		{"rolloverIconTip",         "The icon of the tool when the mouse rolls over it"},
		{"rolloverSelectedIcon",    "Icon mouse rollover selected"},
		{"rolloverSelectedIconTip", "The icon of the tool when the mouse rolls over it and it is selected"},
		{"pressedIcon",             "Icon pressed"},
		{"pressedIconTip",          "The icon of the tool when it is pressed"},
		{"toolTipText",             "Tip text"},
		{"toolTipTextTip",          "This tip pops up in a small window when the mouse stops on the tool"},
		{"helpText",                "Help"},
		{"helpTextTip",             "This help text appears in the status bar when the tool is activated"},
		{"opaque",                  "Opaque"},
		{"opaqueTip",               "The background of the tool may be opaque or transparent"},
		{"selected",                "Selected"},
		{"selectedTip",             "Selects or deselects the tool"},
		{"visible",                 "Visible"},
		{"visibleTip",              "Select if the tool will appear in the tool bar"},
		{"borderPolicy",            "Border"},
		{"borderPolicyTip",         "Select when the border of the tool will be painted"},
		{"value",                   "Value"},
		{"valueTip",                "As a percentage defines the zoom level"},
		{"visibleOnToolBar",        "Tool visible on Toolbar"},
		{"visibleOnToolBarTip",     "Shows or hides the tool on the toolbar"},
		{"dropDownButtonVisible",   "Drop-down menu button visible"},
		{"dropDownButtonVisibleTip","Shows or hides the little arrow button"},
	};
}
