package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the bar chart component primitive group.
 * 
 * @version     1.0.6, 21-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class BarChartPrimitivesBundle_en extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = { 
		{ "BARCHART.ZOOM", "BARCHART.ZOOM" },

        { "BARCHART.SETXAXISFIELD", "BARCHART.SETXAXISFIELD"},
        { "BARCHART.SETYAXISFIELD", "BARCHART.SETYAXISFIELD"},
        { "BARCHART.XAXISFIELD", "BARCHART.XAXISFIELD" },
        { "BARCHART.YAXISFIELD", "BARCHART.YAXISFIELD" },
        { "BARCHART.YAXISMIN", "BARCHART.YAXISMIN" },
        { "BARCHART.YAXISMAX", "BARCHART.YAXISMAX" },
        { "BARCHART.YAXISORIGIN", "BARCHART.YAXISORIGIN" },

		{ "BARCHART.ADDNUMBERDATASET", "BARCHART.ADDNUMBERDATASET" },
		{ "BARCHART.ADDNAMEDATASET", "BARCHART.ADDNAMEDATASET" },
		{ "BARCHART.SETDATASET", "BARCHART.SETDATASET" },
		{ "BARCHART.REMOVEDATASET", "BARCHART.REMOVEDATASET" },
		{ "BARCHART.SETELEMENT", "BARCHART.SETELEMENT" },
		{ "BARCHART.ADDELEMENT", "BARCHART.ADDELEMENT" },
		{ "BARCHART.REMOVEELEMENT", "BARCHART.REMOVEELEMENT" },
		{ "BARCHART.ELEMENT", "BARCHART.ELEMENT" },
		{ "BARCHART.DATASET", "BARCHART.DATASET" },
		{ "BARCHART.TITLES", "BARCHART.TITLES" },
		{ "BARCHART.SETTITLE", "BARCHART.SETTITLE" },

        { "CantUseX", "You cannot use field {0} in the X axis"},
        { "CantUseY", "You cannot use field {0} in the Y axis"},
	};
}
