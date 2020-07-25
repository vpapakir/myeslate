package gr.cti.eslate.tableInspector;

import java.util.ListResourceBundle;

public class TooltipBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"copy",        "Copy record to clipboard"},
        {"paste",       "Paste record from clipboard"},
        {"show",        "Show all the records/Show selected records"},
        {"next",        "Next"},
        {"previous",    "Previous"},
        {"submitquery", "Submit query"},
        {"clear",       "Reset query"},
        {"ioquery",     "In Query/Out of the Query"},
        {"icon",        "Icon"},
        {"followlink",  "You may click for more information"},
        {"showhide",    "Show/Hide attributes"},
        {"back",        "Previous page"},
        {"forward",     "Next page"},
        {"refresh",     "Refresh"},
        {"notquery",        "Click to ignore query value: \""},
        {"query",           "Click to select a value to perform query"},
        {"querytick",       "Click to select this value to perform query"},
	};
}
