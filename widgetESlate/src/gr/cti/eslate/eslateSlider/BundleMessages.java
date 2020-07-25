package gr.cti.eslate.eslateSlider;


import java.util.ListResourceBundle;


public class BundleMessages extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {

            {"componentName", "Slider component"},
            {"name", "ESlateSlider"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th.Mantes (2000)"},
            //{"funding", "© Computer Technology Institute"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},

            //Bean Info

            {"ESlateSlider", "Slider"},

            {"Background", "Background"},
            {"setBackgroundTip", "Sets slider's background"},

            {"Foreground", "Τext color"},
            {"setForegroundTip", "Sets slider's text color"},

            {"Border", "Border"},
            {"setBorderTip", "Sets border"},

            {"Font", "Font"},
            {"setFontTip", "Sets font"},

            {"Value", " Value"},
            {"setValueTip", "Sets slider's value"},

            {"ToolTipText", "ToolTip Text"},
            {"setToolTipTextTip", "Sets slider's ToolTipText"},

            {"MinorTickSpacing", "Minor Tick Spacing"},
            {"setMinorTickSpacingTip", "Sets slider's minor tick spacing"},

            {"MajorTickSpacing", "Major tick spacing"},
            {"setMajorTickSpacingTip", "Sets slider's major tick spacing"},

            {"PreferredSize", "Preferred size"},
            {"setPreferredSizeTip", "Sets slider's preferred size "},

            {"MaximumSize", "Maximum size"},
            {"setMaximumSizeTip", "Sets slider's maximum size"},

            {"MinimumSize", "Minimum size"},
            {"setMinimumSizeTip", "Sets slider's minimum size "},

            {"Minimum", "Minimum"},
            {"setMinimumTip", "Sets slider's minimum value"},

            {"Maximum", "Maximum"},
            {"setMaximumTip", "Sets slider's maximum value"},

            {"SnapToTicks", "Snap to ticks"},
            {"setSnapToTicksTip", "Determines if snap to ticks mode is enabled"},

            {"PaintTrack", "Paint track"},
            {"setPaintTrackTip", "Defines if slider track is painted"},

            {"PaintTicks", "Paint ticks"},
            {"setPaintTicksTip", "Defines if scale ticks are painted "},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Defines if slider is opaque"},

            {"Name", "Name"},
            {"setNameTip", "Sets slider's name"},

            {"Orientation", "Orientation"},
            {"setOrientationTip", "Sets slider's orientation"},

            {"Inverted", "Inverted Value"},
            {"setInvertedTip", "Defines if values are inverted"},

            {"AlignmentX", "Axis X alignment"},
            {"setAlignmentXTip", "Sets axis X alignment  "},

            {"AlignmentY", "Axis Y alignment"},
            {"setAlignmentYTip", "Sets axis Y alignment"},

            {"DoubleBuffered", "DoubleBuffered"},
            {"setDoubleBufferedTip", "Returns whether the receiving button should use a buffer to paint"},

            {"Layout", "Layout"},
            {"setLayoutTip", "Sets slider's layout"},

            {"PaintLabels", "Paint labels"},
            {"setPaintLabelsTip", "Defines if labels are painted "},

            {"Enabled", "Enabled"},
            {"setEnabledTip", "Sets slider to be enabled"},

            {"StateChanged", "State changed"},
            {"setStateChangedTip", "Component's state has changed"},

            {"PlugsUsed", "Plugs Use"},
            {"setPlugsUsedTip", "Sets whether the component's plugs should be created for use"},

            {"horizontal", "HORIZONTAL"},
            {"vertical", "VERTICAL"},
            {"ConstructorTimer", "ESlateSlider constructor"},
            {"LoadTimer", "ESlateSlider load"},
            {"SaveTimer", "ESlateSlider save"},

            {"InitESlateAspectTimer", "ESlateSlider E-Slate aspect creation"},
        };
}
