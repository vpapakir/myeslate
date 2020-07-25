package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class ObjectEventPanelBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",     "Γεγονότα"},
        {"Edit",            "Ορισμός ενέργειας"},
        {"CompileFailureMessage", "Η διαδικασία compilation τελείωσε με απρόσμενα λάθη. Πιθανότατα δεν έχει τεθεί σωστά το classpath"},
        {"CompileFailureMessage2","Δεν είναι δυνατή η μετάφραση του κώδικα. Ο μεταφραστής \"jikes\" δεν μπορεί να βρεθεί"},
        {"Close",           "Κλείσιμο"},
        {"Error",           "Λάθος"},
        {"NoComponent",     "Καμία ενεργή ψηφίδα"},
    };
}
