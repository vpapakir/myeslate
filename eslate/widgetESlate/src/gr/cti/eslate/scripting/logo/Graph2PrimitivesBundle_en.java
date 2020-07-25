package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the graph component primitive group.
 * 
 * @version     1.0.6, 21-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class Graph2PrimitivesBundle_en extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = { 
		{ "GRAPH.ADDFUNCTION", "GRAPH.ADDFUNCTION" },
		{ "GRAPH.REMOVEFUNCTION", "GRAPH.REMOVEFUNCTION" },
		{ "GRAPH.FUNCTIONDOMAIN", "GRAPH.FUNCTIONDOMAIN" },
		{ "GRAPH.FUNCTIONCOLOR", "GRAPH.FUNCTIONCOLOR" },
		{ "GRAPH.ZOOM", "GRAPH.ZOOM" },
		{ "GRAPH.VIEW", "GRAPH.VIEW" }, 
		{ "GRAPH.PLOT", "GRAPH.PLOT" }, 
		{ "GRAPH.PLOTCOLOR", "GRAPH.PLOTCOLOR" }, 
		{ "GRAPH.PLOTCLEAR", "GRAPH.PLOTCLEAR" }, 

        { "GRAPH.SETXAXISFIELD", "GRAPH.SETXAXISFIELD"},
        { "GRAPH.SETYAXISFIELD", "GRAPH.SETYAXISFIELD"},
        { "GRAPH.XAXISFIELD", "GRAPH.XAXISFIELD" },
        { "GRAPH.YAXISFIELD", "GRAPH.YAXISFIELD" },
        { "GRAPH.XAXISMIN", "GRAPH.XAXISMIN" },
        { "GRAPH.XAXISMAX", "GRAPH.XAXISMAX" },
        { "GRAPH.SETXAXISMIN", "GRAPH.SETXAXISMIN" },
        { "GRAPH.SETXAXISMAX", "GRAPH.SETXAXISMAX" },
        { "GRAPH.YAXISMIN", "GRAPH.YAXISMIN" },
        { "GRAPH.YAXISMAX", "GRAPH.YAXISMAX" },
        { "GRAPH.XAXISORIGIN", "GRAPH.XAXISORIGIN" },
        { "GRAPH.YAXISORIGIN", "GRAPH.YAXISORIGIN" },
        { "GRAPH.SETXAXISORIGIN", "GRAPH.SETXAXISORIGIN" },

		{ "GRAPH.ADDDATASET", "GRAPH.ADDDATASET" },
		{ "GRAPH.SETDATASET", "GRAPH.SETDATASET" },
		{ "GRAPH.REMOVEDATASET", "GRAPH.REMOVEDATASET" },
		{ "GRAPH.SETELEMENT", "GRAPH.SETELEMENT" },
		{ "GRAPH.ADDELEMENT", "GRAPH.ADDELEMENT" },
		{ "GRAPH.REMOVEELEMENT", "GRAPH.REMOVEELEMENT" },
		{ "GRAPH.ELEMENT", "GRAPH.ELEMENT" },
		{ "GRAPH.DATASET", "GRAPH.DATASET" },
		{ "GRAPH.TITLES", "GRAPH.TITLES" },
		{ "GRAPH.SETTITLE", "GRAPH.SETTITLE" },

        { "CantUseX", "You cannot use field {0} in the X axis"},
        { "CantUseY", "You cannot use field {0} in the Y axis"},
	};
}
