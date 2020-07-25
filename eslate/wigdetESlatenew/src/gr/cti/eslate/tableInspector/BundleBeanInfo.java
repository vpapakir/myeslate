package gr.cti.eslate.tableInspector;

import java.util.ListResourceBundle;

/**
 * Table Browser BeanInfo bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 09-May-2000
 */
public class BundleBeanInfo extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"percentField",                "Field name width"},
        {"percentFieldtip",             "It is a percentage of the component width"},
        {"toolNavigateVisible",         "Tools navigate visible"},
        {"toolNavigateVisibletip",      "Controls the visibility of navigate tool (arrows)"},
        {"toolSelectViewVisible",       "Tool view selection visible"},
        {"toolSelectViewVisibletip",    "Controls the visibility of view selection tool"},
        {"toolShowRecordVisible",       "Tool hide/show record visible"},
        {"toolShowRecordVisibletip",    "Controls the visibility of hide/show record tool"},
        {"toolSelectViewDefaultShowAll","Default view selection \"all\""},
        {"toolSelectViewDefaultShowAlltip","The value is applied upon new table connection"},
        {"toolQueryVisible",            "Tools query visible "},
        {"toolQueryVisibletip",         "Controls the visibility of the query tools"},
        {"toolbarVisible",              "Toolbar visible"},
        {"toolbarVisibletip",           "Controls the visibility of the toolbar"},
        {"messagebarVisible",           "Messagebar visible"},
        {"messagebarVisibletip",        "Controls the visibility of the messagebar"},
        {"backgroundImage",             "Background image"},
        {"backgroundImagetip",          "Sets or clears the background image"},
        {"opaque",                      "Opaque"},
        {"opaquetip",                   "The component background may be or may not be transparent"},
        {"imageAlignment",              "Background image alignment"},
        {"imageAlignmenttip",           "The position of the background image, if any"},
        {"tabPlacement",                "Tab placement"},
        {"tabPlacementtip",             "Where the names of the table are shown"},
        {"topleft",                     "TOP-LEFT"},
        {"centered",                    "CENTERED"},
        {"top",                         "TOP"},
        {"bottom",                      "BOTTOM"},
        {"left",                        "LEFT"},
        {"right",                       "RIGHT"},
        {"widgetsOpaque",               "Widgets opaque"},
        {"widgetsOpaquetip",            "Shown in conjuction with component transparency"},
        {"background",                  "Component background color"},
        {"backgroundtip",               "Shown only if the component is opaque"},
        {"border",                      "Border"},
        {"bordertip",                   "Controls the component border"},
        {"font",                        "Font"},
        {"fonttip",                     "The font is applied to labels, tooltips etc"},
        {"tabVisible",                  "Tabs visible"},
        {"tabVisibletip",               "When hidden, you won't be able to browse between tables!"},
        {"queryType",                   "Query type"},
        {"queryTypetip",                "The way queries are done. To disable queries hide the query tool."},
        {"queryticks",                  "TICKS"},
        {"querycombo",                  "LIST"},
        {"rowBackgroundColor",          "Field background"},
        {"rowSelectedBackgroundColor",  "Field background of selected records"},
        {"rowForegroundColor",          "Field foreground"},
        {"rowSelectedForegroundColor",  "Field foreground of selected records"},
        {"rowBorderPainted",            "Paint border around fields"},
        {"rowBorderPaintedtip",         "Paints or doesn't paint a border around the value of the fields"},
        {"followActiveRecord",          "Always show the active record"},
        {"followActiveRecordtip",       "Control whether the record shown will always be the active record in the database table"},
        {"activeTabChanged",            "Active tab changed"},
        {"activeRecordBrowserRecordChanged","Record shown changed"},
    };
}
