package gr.cti.eslate.spinButton;


import java.util.ListResourceBundle;


public class SpinButtonBundleMessages extends ListResourceBundle {
    public Object[][] getContents() {
        //        System.out.println("Got in here");
        return contents;
    }

    static final Object[][] contents = {
            {"Text", "Text"},
            {"Date", "Date"},
            {"Time", "Time"},
            {"Number", "Numeric value"},
            // component info
            {"componentName", "Button component"},
            {"name", "ESlateButton"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th. Mantes"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},
            //Bean Info

            {"DateDataModel", "DATEDATA"},
            {"TimeDataModel", "TIMEDATA"},
            {"NumberDataModel", "NUMBERDATA"},
            {"EnumeratedDataModel", "ENUMERATEDDATA"},
            {"SECONDS", "SECONDS"},
            {"MINUTES", "MINUTES"},
            {"HOURS", "HOURS"},
            {"DAYS", "DAYS"},
            {"MONTHS", "MONTHS"},
            {"YEARS", "YEARS"},
            {"SpinButton", "Spin button"},

            {"MinimumNumber", "Minimum value"},
            {"setMinimumNumberTip", "Sets the minimum numeric value of the spin button"},

            {"MaximumNumber", "Maximum value"},
            {"setMaximumNumberTip", "Sets the maximum numeric value of the spin button"},

            {"MinimumDate", "Minimum date"},
            {"setMinimumDateTip", "Sets the minimum date value of the spin button"},

            {"MaximumDate", "Maximum date"},
            {"setMaximumDateTip", "Sets the maximum date value of the spin button"},

            {"MinimumTime", "Minimum time"},
            {"setMinimumTimeTip", "Sets the minimum time value of the spin button"},

            {"MaximumTime", "Maximum time"},
            {"setMaximumTimeTip", "Sets the maximum time value of the spin button"},

            {"Elements", "Discrete values ..."},
            {"setElementsTip", "Sets the discrete values of the enumerated model"},

            {"DefineElements", "Define discrete values"},
            {"setDefineElementsTip", "Sets the discrete values of the enumerated model"},

            {"Define Elements", "Define discrete values"},
            {"setDefineElementsTip", "Sets the discrete values of the enumerated model"},

            {"DialogTitle", "Define Enumerated model elements"},
            {"OK", "OK"},
            {"Cancel", "Cancel"},
            {"Input", "Element modification"},
            {"New element", "New element"},
            {"New value", "New value"},
            {"UpButtonTip", "Advance selected element"},
            {"DownButtonTip", "Denote selected elements"},
            {"AddButtonTip", "Insert new element"},
            {"DeleteButtonTip", "Remove selected elements"},

            {"Format", "Time format"},
            {"setFormatTip", "Sets time value format"},

            {"DateFormat", "Date format"},
            {"setDateFormatTip", "Sets date value format"},

            {"TimeRate", "TimeRate"},
            {"setTimeRateTip", "Sets time rate"},

            {"DateRate", "DateRate"},
            {"setDateRateTip", "Sets date rate"},

            {"ModelType", "Model type"},
            {"setModelTypeTip", "Sets data model type"},

            {"Step", "Increase/Decrease step"},
            {"setStepTip", "Sets the increase/decrease step"},

            {"model", "Model"},
            {"setmodelTip", "Sets the data model"},


            //action Event
            {"ActionPerformed", "Action performed"},
            {"setActionPerformedTip", "An action happened"},

            {"componentHidden", "Component hidden"},
            {"setcomponentHiddenTip", "Makes component invisible"},

            {"componentShown", "Component shown"},
            {"setcomponentShownTip", "Makes component visible"},

            {"mouseEntered", "Mouse entered"},
            {"setmouseEnteredTip", "Mouse entered component's occupied space"},

            {"MousePressed", "Mouse pressed"},
            {"setMousePressedTip", "Mouse was pressed inside component's area"},

            {"FocusGained", "Focus gained "},
            {"setFocusGainedTip", "Focus was gained by this component"},

            {"FocusLost", "Focus lost"},
            {"setFocusLostTip", "Focus was lost by this component"},

            {"ValueChanged", "Value changed"},
            {"setValueChangedTip", "SpinButton value has changed"},

            {"MouseReleased", "Mouse Released"},
            {"setMousePressedTip", "Mouse was released inside component's area"},

            {"MouseClicked", "Mouse Clicked"},
            {"setMouseClickedTip", "Mouse was clicked inside component's area"},

            {"mouseExited", "Mouse exited"},
            {"setmouseExitedTip", "Mouse left component's occupied space"},

            {"mouseMoved", "Mouse moved"},
            {"setmouseMovedTip", "Mouse has moved"},

            {"propertyChange", "Property change"},
            {"setpropertyChangeTip", "A component's property has changed"},

            {"vetoableChange", "Vetoable change"},
            {"setvetoableChangeTip", "A change that is approved/disapproved"},
            

            {"left", "LEFT"},
            {"center", "CENTER"},
            {"right", "RIGHT"},
            {"leading", "LEADING"},
            {"trailing", "TRAILING"},

            {"top", "TOP"},
            {"bottom", "BOTTOM"},

        };

}
