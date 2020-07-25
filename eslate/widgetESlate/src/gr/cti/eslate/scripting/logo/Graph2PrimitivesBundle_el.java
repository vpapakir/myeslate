package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the graph component primitive group.
 * 
 * @version     1.0.7, 28-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class Graph2PrimitivesBundle_el extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		{ "GRAPH.ADDFUNCTION", "ΓΡΑΦΗΜΑ.ΕΙΣΑΓΩΓΗΣΥΝΑΡΤΗΣΗΣ" },
		{ "GRAPH.REMOVEFUNCTION", "ΓΡΑΦΗΜΑ.ΑΦΑΙΡΕΣΗΣΥΝΑΡΤΗΣΗΣ" },
		{ "GRAPH.FUNCTIONDOMAIN", "ΓΡΑΦΗΜΑ.ΠΕΔΙΟΟΡΙΣΜΟΥΣΥΝΑΡΤΗΣΗΣ" },
		{ "GRAPH.FUNCTIONCOLOR", "ΓΡΑΦΗΜΑ.ΧΡΩΜΑΣΥΝΑΡΤΗΣΗΣ" },
		{ "GRAPH.ZOOM", "ΓΡΑΦΗΜΑ.ΕΜΒΑΘΥΝΣΗ" },
		{ "GRAPH.VIEW", "ΓΡΑΦΗΜΑ.ΘΕΣΗΠΡΟΒΟΛΗΣ" }, 
		{ "GRAPH.PLOT", "ΓΡΑΦΗΜΑ.ΣΗΜΕΙΟ" }, 
		{ "GRAPH.PLOTCOLOR", "ΓΡΑΦΗΜΑ.ΧΡΩΜΑΣΗΜΕΙΩΝ" }, 
		{ "GRAPH.PLOTCLEAR", "ΓΡΑΦΗΜΑ.ΚΑΘΑΡΙΣΜΟΣΣΗΜΕΙΩΝ" },

        { "GRAPH.SETXAXISFIELD", "ΓΡΑΦΗΜΑ.ΘΕΣΕΠΕΔΙΟΑΞΟΝΑΧ" },
        { "GRAPH.SETYAXISFIELD", "ΓΡΑΦΗΜΑ.ΘΕΣΕΠΕΔΙΟΑΞΟΝΑΨ" },
        { "GRAPH.XAXISFIELD", "ΓΡΑΦΗΜΑ.ΠΕΔΙΟΑΞΟΝΑΧ" },
        { "GRAPH.YAXISFIELD", "ΓΡΑΦΗΜΑ.ΠΕΔΙΟΑΞΟΝΑΨ" },
        { "GRAPH.XAXISMIN", "ΓΡΑΦΗΜΑ.ΕΛΑΧΙΣΤΟΑΞΟΝΑΧ" },
        { "GRAPH.XAXISMAX", "ΓΡΑΦΗΜΑ.ΜΕΓΙΣΤΟΑΞΟΝΑΧ" },
        { "GRAPH.SETXAXISMIN", "ΓΡΑΦΗΜΑ.ΘΕΣΕΕΛΑΧΙΣΤΟΑΞΟΝΑΧ" },
        { "GRAPH.SETXAXISMAX", "ΓΡΑΦΗΜΑ.ΘΕΣΕΜΕΓΙΣΤΟΑΞΟΝΑΧ" },
        { "GRAPH.YAXISMIN", "ΓΡΑΦΗΜΑ.ΕΛΑΧΙΣΤΟΑΞΟΝΑΨ" },
        { "GRAPH.YAXISMAX", "ΓΡΑΦΗΜΑ.ΜΕΓΙΣΤΟΑΞΟΝΑΨ" },
        { "GRAPH.XAXISORIGIN", "ΓΡΑΦΗΜΑ.ΑΡΧΗΑΞΟΝΑΧ" },
        { "GRAPH.YAXISORIGIN", "ΓΡΑΦΗΜΑ.ΑΡΧΗΑΞΟΝΑΨ" },
        { "GRAPH.SETXAXISORIGIN", "ΓΡΑΦΗΜΑ.ΘΕΣΕΑΡΧΗΑΞΟΝΑΧ" },

		{ "GRAPH.ADDDATASET", "ΓΡΑΦΗΜΑ.ΠΡΟΣΘΕΣΕΣΥΝΟΛΟΔΕΔΟΜΕΝΩΝ" },
		{ "GRAPH.SETDATASET", "ΓΡΑΦΗΜΑ.ΘΕΣΕΣΥΝΟΛΟΔΕΔΟΜΕΝΩΝ" },
		{ "GRAPH.REMOVEDATASET", "ΓΡΑΦΗΜΑ.ΑΦΑΙΡΕΣΕΣΥΝΟΛΟΔΕΔΟΜΕΝΩΝ" },
		{ "GRAPH.SETELEMENT", "ΓΡΑΦΗΜΑ.ΘΕΣΕΣΤΟΙΧΕΙΟ" },
		{ "GRAPH.ADDELEMENT", "ΓΡΑΦΗΜΑ.ΠΡΟΣΘΕΣΕΣΤΟΙΧΕΙΟ" },
		{ "GRAPH.REMOVEELEMENT", "ΓΡΑΦΗΜΑ.ΑΦΑΙΡΕΣΕΣΤΟΙΧΕΙΟ" },
		{ "GRAPH.ELEMENT", "ΓΡΑΦΗΜΑ.ΣΤΟΙΧΕΙΟ" },
		{ "GRAPH.DATASET", "ΓΡΑΦΗΜΑ.ΣΥΝΟΛΟΔΕΔΟΜΕΝΩΝ" },
		{ "GRAPH.TITLES", "ΓΡΑΦΗΜΑ.ΤΙΤΛΟΙ" },
		{ "GRAPH.SETTITLE", "ΓΡΑΦΗΜΑ.ΘΕΣΕΤΙΤΛΟ" },

        { "CantUseX", "Δεν μπορείτε να χρησιμοποιήσετε το πεδίο {0} στον άξονα των Χ" },
        { "CantUseY", "Δεν μπορείτε να χρησιμοποιήσετε το πεδίο {0} στον άξονα των Ψ" },
	};
}
