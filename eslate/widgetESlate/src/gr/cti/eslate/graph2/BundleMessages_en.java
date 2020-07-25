package gr.cti.eslate.graph2;

import java.util.ListResourceBundle;

/**
 * @version     1.0.6, 18-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class BundleMessages_en extends ListResourceBundle {
	static final Object[][] contents = {
			{ "componentName", "Graph component" },
			{ "name", "Graph" },
			{ "credits1",
					"Part of the E-Slate environment (http://E-Slate.cti.gr)" },
			{ "credits2", "Development: Augustine Grillakis, Kriton Kyrimis" },
			{ "credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα." },
			{ "version", "version" },
			
			{ "edit", "Edit" },
			{ "function", "Function f(x)" },
			{ "domain", "Domain" },
			{ "from", "From" },
			{ "to", "To" },
//			{ "step", "Step" },
			{ "color", "Color" },
			{ "stroke", "Stroke" },
			{ "plotColor", "Plot color" },
			{ "translation", "Translation" },
			{ "rotation", "Rotation" },
			{ "rotationAngle", "Rotation angle" },
			{ "degrees", "Degrees" },
			{ "scale", "Scale" },
			{ "scaleFactor", "Scale factor" },
			{ "remove", "Remove" },
			{ "menu", "Menu" },
			{ "buttonUp", "Move up" },
			{ "buttonDown", "Move down" },
			{ "buttonLeft", "Move left" },
			{ "buttonRight", "Move right" },
			{ "buttonReset", "Axes start" },
			{ "buttonNormalize", "Normalization" },
			{ "buttonZoomIn", "Zoom in" },
			{ "buttonZoomOut", "Zoom out" },
			{ "spinnerDomainStart", "Axis X start" },
			{ "spinnerDomainEnd", "Axis X end" },
			{ "comboBoxX", "Axis X field" },
			{ "comboBoxY", "Axis Y field" },
			{ "showHide", "Show/Hide" },
			{ "points", "Points" },
			{ "x", "x:" },
			{ "y", "y:" },
			
			{ "expressionProperty", "Function f(x) =" },
			{ "expressionPropertyTip", "Function f(x)" },
			{ "expressionVisibleProperty", "Function insertion visible" },
			{ "expressionVisiblePropertyTip", "Visibility of function insertion" },
			
			{ "expressionPlug", "Function f(x)=" },
			{ "tablePlug", "Table" },

                        { "dataSetExists", "Data set {0} already exists" },
                        { "notLocalTable", "You have not defined any data sets" },
			
			// Bar chart
			{ "componentNameBarChart", "Bar chart component" },
			{ "nameBarChart", "Bar chart" },
			{ "category", "Category" },
			{ "value", "Value" },
			{ "comboBoxCategory", "Category field" },
			{ "comboBoxValue", "Value field" },
	};

	protected Object[][] getContents() {
		return contents;
	}
}
