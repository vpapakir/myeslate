package gr.cti.eslate.logo;

import java.util.ListResourceBundle;

/**
 * Greek language resources for Logo component's bean info.
 * @version     2.0.0, 24-May-2006
 * @author      G. Birbilis
 * @author      G. Tsironis
 * @author      N. Drossos
 * @author      K.Kyrimis
 */
public class LogoBeanInfoBundle_el_GR extends ListResourceBundle {
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
        {"statusBarVisible",      "Γραμμή κατάστασης ορατή"},

        {"menuBarVisibleTip",     "Ρύθμιση της ορατότητας της γραμμής μενού"},
        {"toolBarVisibleTip",     "Ρύθμιση της ορατότητας της γραμμής εργαλείων"},
        {"statusBarVisibleTip",   "Ρύθμιση της ορατότητας της γραμμής κατάστασης"},
        {"border",                "Περίγραμμα"},
        {"borderTip",             "Ρύθμιση της ορατότητας του περιγράμματος"},


        {"hasFontSelector", "Εμφάνιση ρυθμίσεων γραμματοσειράς στη γραμμή εργαλείων"},
        {"execQueueMaxSize", "Μέγιστο μέγεθος ουράς εντολών (-1=απεριόριστο)"},
        {"hasFontSelectorTip", "Εμφάνιση ρυθμίσεων γραμματοσειράς στη γραμμή εργαλείων"},
        {"execQueueMaxSizeTip", "Μέγιστο μέγεθος ουράς εντολών (-1=απεριόριστο)"},

        {"lineNumbersVisibleInProgramArea", "Εμφάνιση αριθμών γραμμής στην περιοχή προγράμματος"},
        {"lineNumbersVisibleInProgramAreaTip", "Εμφάνιση αριθμών γραμμής στην περιοχή προγράμματος"},

        {"lineNumbersVisibleInOutputArea", "Εμφάνιση αριθμών γραμμής στην περιοχή εξόδου"},
        {"lineNumbersVisibleInOutputAreaTip", "Εμφάνιση αριθμών γραμμής στην περιοχή εξόδου"}
    };
}

