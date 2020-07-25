package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Path segment properties dialog bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 26-Apr-2000
 */
public class BundleSegmentProperties_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"title",           "Ιδιότητες τμήματος μονοπατιού"},
        {"name",            "Όνομα"},
        {"strokeas",        "Ζωγράφισε το τμήμα ως:"},
        {"straightline",    "Μία συνεχόμενη γραμμή."},
        {"dottedline",      "Μία γραμμή από κουκίδες."},
        {"width",           ", που έχει πλάτος"},
        {"paintas",         "Χρωμάτισε το τμήμα με:"},
        {"solidcolor",      "Ένα συμπαγές χρώμα."},
        {"gradientcolor",   "Βαθμωτή αλλαγή χρώματος."},
        {"define",          "Ορισμός"},
        {"gradientstart",   "Χρώμα έναρξης"},
        {"gradientend",     "Χρώμα τερματισμού"},
        {"ok",              "Εντάξει"},
        {"cancel",          "Ακύρωση"},
        {"apply",           "Εφαρμογή"},
        {"transparency",    "Διαφάνεια"},
        {"colors",          "Χρώματα"},
    };
}
