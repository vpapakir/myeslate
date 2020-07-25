package gr.cti.eslate.stage;

import java.util.ListResourceBundle;

/**
 * Greek language resources for the stage component bean info
 *
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 * @version     2.0.9, 21-Nov-2006
 */
public class StageBeanInfoBundle_el_GR extends ListResourceBundle {
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

        {"keyPressed", "Πάτημα πλήκτρου"},
        {"keyReleased", "Ξεπάτημα πλήκτρου"},
        {"keyTyped", "Εισαγωγή πλήκτρου"},

        {"menuBarVisible",        "Γραμμή μενού ορατή"},
        {"menuBarVisibleTip",     "Ρύθμιση της ορατότητας της γραμμής μενού"},
        {"toolBarVisible",        "Γραμμή εργαλείων ορατή"},
        {"toolBarVisibleTip",     "Ρύθμιση της ορατότητας της γραμμής εργαλείων"},
        {"border",                "Περίγραμμα"},
        {"borderTip",             "Ρύθμιση της ορατότητας του περιγράμματος"},

        {"objectMovementEnabled", "Αντικείμενα κινητά μεσω ποντικιού"},
        {"objectMovementEnabledTip", "Επιτρέπει στα αντικείμενα να κινούνται μέσω ποντικιού"},

        {"controlPointMovementEnabled", "Σημεία ελέγου κινητά μέσω ποντικιού"},
        {"controlPointMovementEnabledTip", "Επιτρέπει στα σημεία ελέγχου να κινούνται μέσω ποντικιού"},

        {"viewMovementEnabled", "Όψη κινητή μέσω ποντικιού"},
        {"viewMovementEnabledTip", "Επιτρέπει την μετακίνηση τής όψης τής σκηνής μέσω ποντικιού"},

        {"objectsAdjustable", "Αντικείμενα διαμορφώσιμα μέσω ποντικιού"},
        {"objectsAdjustableTip", "Επιτρέπει τη διαμόρφωση αντικειμένων μέσω ποντικιού"},

        {"coordinatesVisible", "Συντεταγμένες ορατές"},
        {"coordinatesVisibleTip", "Ρύθμιση της ορατότητας των συντεταγμένων"},

        {"gridVisible", "Πλέγμα ορατό"},
        {"gridVisibleTip", "Ρύθμιση της ορατότητας του πλέγματος"},

        {"gridSize", "Μέγεθος πλέγματος"},
        {"gridSizeTip", "Ρύθμιση του μεγέθους του πλέγματος"},

        {"axisVisible", "Αξονες ορατοί"},
        {"axisVisibleTip", "Ρύθμιση της ορατότητας των αξόνων"},

        {"controlPointsVisible", "Σημεία ελέγχου ορατά"}, //12May2000
        {"controlPointsVisibleTip", "Ρύθμιση της ορατότητας των σημείων ελέγχου"}, //12May2000

        {"marksOverShapes", "Αξονες & Πλέγμα πάνω από σχήματα"},
        {"marksOverShapesTip", "Επιτρέπει σε ’ξονες και πλέγμα να βρίσκονται πάνω από σχήματα"},

        {"image","εικόνα"} //12May2000

    };
}

