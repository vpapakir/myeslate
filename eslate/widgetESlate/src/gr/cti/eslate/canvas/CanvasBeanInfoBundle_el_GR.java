package gr.cti.eslate.canvas;

import java.util.ListResourceBundle;


/**
 * @version     2.0.7, 14-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class CanvasBeanInfoBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"mouseEntered",    "Είσοδος ποντικού"},
        {"mouseExited",     "Έξοδος ποντικού"},
        {"mouseMoved",      "Μετακίνηση ποντικού"},

        {"propertyChange",  "Αλλαγή ιδιότητας"},
        {"vetoableChange",  "Ακυρώσιμη αλλαγή ιδιότητας"},

        {"componentHidden", "Απόκρυψη ψηφίδας"},
        {"componentShown",  "Εμφάνιση ψηφίδας"},

        {"menuBarVisible",        "Γραμμή μενού ορατή"},
        {"toolBarVisible",        "Γραμμή εργαλείων ορατή"},

        {"menuBarVisibleTip",     "Ρύθμιση της ορατότητας της γραμμής μενού"},
        {"toolBarVisibleTip",     "Ρύθμιση της ορατότητας της γραμμής εργαλείων"},
        {"border",                "Περίγραμμα"},
        {"borderTip",             "Ρύθμιση της ορατότητας του περιγράμματος"},

        {"fixedTurtlePageSizes", "Σταθερά μεγέθη σελίδων χελωνών"},
        {"fixedTurtlePageSizesTip", "Ορίστε αν τα μεγέθη τών σελίδων χελωνών θα είναι σταθερά"},

        {"foregroundColor", "Χρώμα μολυβιού"},
        {"backgroundColorTip", "Επιλογή χρώματος γόμας"},
        {"foregroundColorTip", "Επιλογή χρώματος μολυβιού"},
        {"backgroundColor", "Χρώμα γόμας"}

    };
}

