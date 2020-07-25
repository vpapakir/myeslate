package gr.cti.eslate.eslateList;


import java.util.ListResourceBundle;


public class ListBundle extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"ESlateList", "List"},
            {"List Element", "List Element"},
            {"Define elements", "Define Elements.."},
            {"DialogTitle", "Define list elements"},
            {"OK", "OK"},
            {"Cancel", "Cancel"},
            {"Input", "Element modification"},
            {"New element", "New element"},
            {"UpButtonTip", "Advance selected layer"},
            {"DownButtonTip", "Denote selected layer"},
            {"AddButtonTip", "Insert new layer"},
            {"DeleteButtonTip", "Remove selected layers"},
            {"single selection", "SINGLE_SELECTION"},
            {"single interval selection", "SINGLE_INTERVAL_SELECTION"},
            {"multiple interval selection", "MULTIPLE_INTERVAL_SELECTION"},

            {"componentName", "List component"},
            {"name", "ESlateList"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th.Mantes"},
            //{"funding", "© Computer Technology Institute"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},
            //Bean Info
            {"Model", "Model"},
            {"setModelTip", "Sets List's model"},

            {"Enabled", "Enabled"},
            {"setEnabledTip", "Sets list enabled"},

            {"FixedCellWidth", "Fixed cell width"},
            {"setFixedCellWidthTip", "Sets cell width"},

            {"FixedCellHeight", "Fixed cell height"},
            {"setFixedCellHeightTip", "Sets cell height"},

            {"Background", "Background"},
            {"setBackgroundTip", "Sets list's background"},

            {"SelectionBackground", "Selection background"},
            {"setSelectionBackgroundTip", "Sets list's selection background"},

            {"Foreground", "Text color"},
            {"setForegroundTip", "Sets list's text color"},

            {"SelectionForeground", "Selection text color"},
            {"setSelectionForegroundTip", "Sets list's text color"},

            {"SelectionMode", "SelectionMode"},
            {"setSelectionModeTip", "Sets list's selection mode"},

            {"Border", "Border"},
            {"setBorderTip", "Sets list's Border"},

            {"Font", "Font"},
            {"setFontTip", "Sets list's Font"},

            {"ToolTipText", "TooltipText"},
            {"setToolTipTextTip", "Sets list's tooltiptext"},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Defines if button is opaque"},

            {"Name", "Name"},
            {"setNameTip", "list's name "},

            {"AlignmentX", "X axis alignment"},
            {"setAlignmentXTip", " Defines axis X alignment"},

            {"AlignmentY", "Y axis alignment"},
            {"setAlignmentYTip", "Defines axis Y alignment"},

            {"DoubleBuffered", "DoubleBuffered"},
            {"setDoubleBufferedTip", "Returns whether the receiving button should use a buffer to paint"},

            {"ValueIsAdjusting", "Adjustable value"},
            {"setValueIsAdjustingTip", "Sets value adjustability"},

            {"VisibleRowCount", "Visible row count"},
            {"setVisibleRowCountTip", "Sets visible row count"},

            {"MaximumSize", "Μaximum size"},
            {"setMaximumSizeTip", "Sets maximum size"},

            {"MinimumSize", "Minimum size"},
            {"setMinimumSizeTip", "Sets minimum size "},

            {"PreferredSize", "Preferred size"},
            {"setPreferredSizeTip", "Sets preferred size "},

            {"PlugsUsed", "Plugs Use"},
            {"setPlugsUsedTip", "Sets whether the component's plugs should be created for use"},

            {"RequestFocusEnabled", "Request focus enabled"},
            {"setRequestFocusEnabledTip", "Sets the focus request property of the component"},


            {"SelectedIndex", "Selected index"},
            {"setSelectedIndexTip", "Sets the selected index "},

            {"MousePressed", "Mouse pressed"},
            {"setMousePressedTip", "Mouse was pressed inside component's area"},

            {"FocusGained", "Focus gained "},
            {"setFocusGainedTip", "Focus was gained by this component"},

            {"FocusLost", "Focus lost"},
            {"setFocusLostTip", "Focus was lost by this component"},


            {"MouseReleased", "Mouse released"},
            {"setMousePressedTip", "Mouse was released inside component's area"},

            {"ValueChanged", "Value changed"},
            {"setValueChangedTip", "A specific value of the component was changed"},

            {"SelectionChanged", "Selection changed"},
            {"setSelectionChangedTip", "The selected item has changed"},

            {"ConstructorTimer", "ESlateList constructor"},
            {"LoadTimer", "ESlateList load"},
            {"SaveTimer", "ESlateList save"},

            {"InitESlateAspectTimer", "ESlateList E-Slate aspect creation"},

        };
}
