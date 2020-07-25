package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the bar chart component primitive group.
 * 
 * @version     1.0.6, 21-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class BarChartPrimitivesBundle_el extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		{ "BARCHART.ZOOM", "ΡΑΒΔΟΓΡΑΜΜΑ.ΕΜΒΑΘΥΝΣΗ" },

        { "BARCHART.SETXAXISFIELD", "ΡΑΒΔΟΓΡΑΜΜΑ.ΘΕΣΕΠΕΔΙΟΑΞΟΝΑΧ" },
        { "BARCHART.SETYAXISFIELD", "ΡΑΒΔΟΓΡΑΜΜΑ.ΘΕΣΕΠΕΔΙΟΑΞΟΝΑΨ" },
        { "BARCHART.XAXISFIELD", "ΡΑΒΔΟΓΡΑΜΜΑ.ΠΕΔΙΟΑΞΟΝΑΧ" },
        { "BARCHART.YAXISFIELD", "ΡΑΒΔΟΓΡΑΜΜΑ.ΠΕΔΙΟΑΞΟΝΑΨ" },
        { "BARCHART.YAXISMIN", "ΡΑΒΔΟΓΡΑΜΜΑ.ΕΛΑΧΙΣΤΟΑΞΟΝΑΨ" },
        { "BARCHART.YAXISMAX", "ΡΑΒΔΟΓΡΑΜΜΑ.ΜΕΓΙΣΤΟΑΞΟΝΑΨ" },
        { "BARCHART.YAXISORIGIN", "ΡΑΒΔΟΓΡΑΜΜΑ.ΑΡΧΗΑΞΟΝΑΨ" },

		{ "BARCHART.ADDNUMBERDATASET", "ΡΑΒΔΟΓΡΑΜΜΑ.ΠΡΟΣΘΕΣΕΣΥΝΟΛΟΑΡΙΘΜΩΝ" },
		{ "BARCHART.ADDNAMEDATASET", "ΡΑΒΔΟΓΡΑΜΜΑ.ΠΡΟΣΘΕΣΕΣΥΝΟΛΟΟΝΟΜΑΤΩΝ" },
		{ "BARCHART.SETDATASET", "ΡΑΒΔΟΓΡΑΜΜΑ.ΘΕΣΕΣΥΝΟΛΟΔΕΔΟΜΕΝΩΝ" },
		{ "BARCHART.REMOVEDATASET", "ΡΑΒΔΟΓΡΑΜΜΑ.ΑΦΑΙΡΕΣΕΣΥΝΟΛΟΔΕΔΟΜΕΝΩΝ" },
		{ "BARCHART.SETELEMENT", "ΡΑΒΔΟΓΡΑΜΜΑ.ΘΕΣΕΣΤΟΙΧΕΙΟ" },
		{ "BARCHART.ADDELEMENT", "ΡΑΒΔΟΓΡΑΜΜΑ.ΠΡΟΣΘΕΣΕΣΤΟΙΧΕΙΟ" },
		{ "BARCHART.REMOVEELEMENT", "ΡΑΒΔΟΓΡΑΜΜΑ.ΑΦΑΙΡΕΣΕΣΤΟΙΧΕΙΟ" },
		{ "BARCHART.ELEMENT", "ΡΑΒΔΟΓΡΑΜΜΑ.ΣΤΟΙΧΕΙΟ" },
		{ "BARCHART.DATASET", "ΡΑΒΔΟΓΡΑΜΜΑ.ΣΥΝΟΛΟΔΕΔΟΜΕΝΩΝ" },
		{ "BARCHART.TITLES", "ΡΑΒΔΟΓΡΑΜΜΑ.ΤΙΤΛΟΙ" },
		{ "BARCHART.SETTITLE", "ΡΑΒΔΟΓΡΑΜΜΑ.ΘΕΣΕΤΙΤΛΟ" },

        { "CantUseX", "Δεν μπορείτε να χρησιμοποιήσετε το πεδίο {0} στον άξονα των Χ" },
        { "CantUseY", "Δεν μπορείτε να χρησιμοποιήσετε το πεδίο {0} στον άξονα των Ψ" },
	};
}
