package gr.cti.eslate.eslateLabel;


import java.util.ListResourceBundle;


public class BundleMessages extends ListResourceBundle {
    public Object[][] getContents() {

        return contents;
    }

    static final Object[][] contents = {

            {"Title", "Text"},
            // component info
            {"componentName", "Label component"},
            {"name", "ESlateLabel"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th. Mantes"},
            //{"funding", "© Computer Technology Institute"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},
            
            {"E-Slate", "E-Slate"},

            //Bean Info
            {"ESlateLabel", "Label"},

            {"PlugsUsed", "Plugs Use"},
            {"setPlugsUsedTip", "Sets whether the component's plugs should be created for use"},

            {"Background", "Background"},
            {"setBackgroundTip", "Sets label's background"},

            {"Border", "Border"},
            {"setBorderTip", "Sets label's border"},

            {"Foreground", "Text color"},
            {"setForegroundTip", "Sets label's text color"},

            {"Font", "Font"},
            {"setFontTip", "Sets label's Font"},

            {"Enabled", "Enabled"},
            {"setEnabledTip", "Defines if label is enabled or not"},

            {"Icon", "Icon"},
            {"setIconTip", "Sets label's icon "},

            {"Text", "Text"},
            {"setTextTip", "Sets label's text"},

            {"ToolTipText", "TooltipText"},
            {"setToolTipTextTip", "Sets label's tooltiptext"},

            {"VerticalAlignment", "Vertical alignment"},
            {"setVerticalAlignmentTip", "Defines vertical alignment"},

            {"HorizontalAlignment", "Horizontal alignment"},
            {"setHorizontalAlignmentTip", "Defines horizontal alignment"},

            {"VerticalTextPosition", "Vertical text position"},
            {"setVerticalTextPositionTip", "Defines vertical text position"},

            {"HorizontalTextPosition", "Horizontal text position"},
            {"setHorizontalTextPositionTip", "Defines horizontal text position"},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Defines if button is opaque"},

            {"IconTextGap", "Icon - Text gap"},
            {"setIconTextGapTip", "Defines the space between icon and text"},

            {"Name", "Name"},
            {"setNameTip", "Label name "},

            {"DisabledIcon", "Disabled icon"},
            {"setDisabledIconTip", "Defines the disabled label's icon"},

            {"AlignmentX", "X axis alignment"},
            {"setAlignmentXTip", " Defines axis X alignment"},

            {"AlignmentY", "Y axis alignment"},
            {"setAlignmentYTip", "Defines axis Y alignment"},

            {"DoubleBuffered", "DoubleBuffered"},
            {"setDoubleBufferedTip", "Returns whether the receiving button should use a buffer to paint"},

            {"Layout", "Layout"},
            {"setLayoutTip", "Defines space layout"},
            

            {"PreferredSize", "Preferred size"},
            {"setPreferredSizeTip", "Sets slider's preferred size "},

            {"MaximumSize", "Maximum size"},
            {"setMaximumSizeTip", "Sets slider's maximum size"},

            {"MinimumSize", "Minimum size"},
            {"setMinimumSizeTip", "Sets slider's minimum size "},

            {"MousePressed", "Mouse pressed"},
            {"setMousePressedTip", "Mouse was pressed inside component's area"},

            {"FocusGained", "Focus gained "},
            {"setFocusGainedTip", "Focus was gained by this component"},

            {"FocusLost", "Focus lost"},
            {"setFocusLostTip", "Focus was lost by this component"},


            {"MouseReleased", "Mouse Released"},
            {"setMousePressedTip", "Mouse was released inside component's area"},


            {"left", "LEFT"},
            {"center", "CENTER"},
            {"right", "RIGHT"},
            {"leading", "LEADING"},
            {"trailing", "TRAILING"},

            {"top", "TOP"},
            {"bottom", "BOTTOM"},

            {"ConstructorTimer", "ESlateLabel constructor"},
            {"LoadTimer", "ESlateLabel load"},
            {"SaveTimer", "ESlateLabel save"},

            {"ConstructorTimer", "ESlateComboBox constructor"},
            {"LoadTimer", "ESlateComboBox load"},
            {"SaveTimer", "ESlateComboBox save"},

            {"InitESlateAspectTimer", "ESlateLabel E-Slate aspect creation"},

            {"MultilineMode",       "Multi line mode"},
            {"MultilineModeTip",    "Enable multi line mode"},
            
            {"actorName", "label"},
            {"x", "X"},
            {"y", "Y"},
            {"width", "Width"},
            {"height", "Height"},
            {"animationPlug", "Animation"},
        };

}
