package gr.cti.eslate.eslateCheckBox;


import java.util.ListResourceBundle;


public class BundleMessages extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"Title", "Title"},
            {"Color", "Color"},
            {"Pressed", "Boolean"},
            // component info
            {"componentName", "Check box component"},
            {"name", "ESlateCheckBox"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th. Mantes"},
            //{"funding", "© Computer Technology Institute"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},
            //Bean Info
            {"ESlateCheckBox", "CheckBox"},

            {"Background", "Background"},
            {"setBackgroundTip", "Sets checkbox's background"},

            {"Foreground", "Text color"},
            {"setForegroundTip", "Sets checkbox's text color"},

            {"Border", "Border"},
            {"setBorderTip", "Sets checkbox's Border"},

            {"Font", "Font"},
            {"setFontTip", "Sets checkbox's Font"},

            {"BorderPainted", "BorderPainted"},
            {"setBorderPaintedTip", "Make border visible or hide it"},

            {"Icon", "Icon"},
            {"setIconTip", "Sets checkbox's icon "},

            {"SelectedIcon", "SelectedIcon"},
            {"setSelectedIconTip", "Sets checkbox's SelectedIcon"},

            {"RolloverIcon", "RolloverIcon"},
            {"setRolloverIconTip", "Sets checkbox's RolloverIcon "},

            {"RolloverSelectedIcon", "RolloverSelectedIcon"},
            {"setRolloverSelectedIconTip", "Sets checkbox's RolloverSelectedIcon "},

            {"Text", "Text"},
            {"setTextTip", "Sets checkbox's text"},

            {"ToolTipText", "TooltipText"},
            {"setToolTipTextTip", "Sets checkbox's tooltiptext"},

            {"ContentAreaFilled", "Content area filled"},
            {"setContentAreaFilledTip", "Defines if inner area is color-filled"},

            {"Selected", "Selected"},
            {"setSelectedTip", "Sets CheckBox to be selected"},

            {"RolloverEnabled", "Rollover enabled"},
            {"setRolloverEnabledTip", "checkbox's responce when mouse is passing over "},

            {"VerticalAlignment", "Vertical alignment"},
            {"setVerticalAlignmentTip", "Defines Vertical alignment"},

            {"VerticalTextPosition", "Vertical text position"},
            {"setVerticalTextPositionTip", "Defines Vertical text position"},

            {"HorizontalTextPosition", "Horizontal text position"},
            {"setHorizontalTextPositionTip", "Defines Horizontal text position"},

            {"HorizontalAlignment", "Horizontal alignment"},
            {"setHorizontalAlignmentTip", "Defines Horizontal alignment"},

            {"FocusPainted", "Focus painted"},
            {"setFocusPaintedTip", "Defines if CheckBox is focus painted or not"},

            {"PressedIcon", "Pressed icon"},
            {"setPressedIconTip", "Defines pressed checkbox's icon "},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Defines if CheckBox is opaque"},

            {"Name", "Name"},
            {"setNameTip", "checkbox's name "},

            {"DisabledIcon", "Disabled icon"},
            {"setDisabledIconTip", "Defines the disabled checkbox's icon"},

            {"DisabledSelectedIcon", "Disabled selected icon"},
            {"setDisabledSelectedIconTip", "Defines the disabled selected icon"},

            {"AlignmentX", "X axis alignment"},
            {"setAlignmentXTip", " Defines axis X alignment"},

            {"AlignmentY", "Y axis alignment"},
            {"setAlignmentYTip", "Defines axis Y alignment"},

            {"DoubleBuffered", "DoubleBuffered"},
            {"setDoubleBufferedTip", "Returns whether the receiving CheckBox should use a buffer to paint"},

            {"Layout", "Layout"},
            {"setLayoutTip", "Defines space layout"},

            {"Margin", "Margin"},
            {"setMarginTip", "Defines margin"},

            {"Enabled", "Enabled"},
            {"setEnabledTip", "Defines if button is enabled"},

            {"PlugsUsed", "Plugs Use"},
            {"setPlugsUsedTip", "Sets whether the component's plugs should be created for use"},

            //action Event
            {"ActionPerformed", "Action performed"},
            {"setActionPerformedTip", "An action happened"},

            {"componentHidden", "Component hidden"},
            {"setcomponentHiddenTip", "Makes component invisible"},

            {"componentShown", "Component shown"},
            {"setcomponentShownTip", "Makes component visible"},

            {"mouseEntered", "Mouse entered"},
            {"setmouseEnteredTip", "Mouse entered component's occupied space"},

            {"mouseExited", "Mouse exited"},
            {"setmouseExitedTip", "Mouse left component's occupied space"},

            {"mouseMoved", "Mouse moved"},
            {"setmouseMovedTip", "Mouse has moved"},


            {"MousePressed", "Mouse pressed"},
            {"setMousePressedTip", "Mouse was pressed inside component's area"},

            {"FocusGained", "Focus gained "},
            {"setFocusGainedTip", "Focus was gained by this component"},

            {"FocusLost", "Focus lost"},
            {"setFocusLostTip", "Focus was lost by this component"},

            {"ItemStateChanged", "Item state changed"},
            {"setItemStateChangedTip", "The components state was changed"},

            {"StateChanged", "State changed"},

            {"MouseReleased", "Mouse Released"},
            {"setMousePressedTip", "Mouse was released inside component's area"},


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
            {"LoadTimer", "CheckBox load"},
            {"SaveTimer", "CheckBox save"},
            {"ConstructorTimer", "ESlateCheckBox constructor"},
            {"LoadTimer", "ESlateCheckBox load"},
            {"SaveTimer", "ESlateCheckBox save"},

            {"InitESlateAspectTimer", "ESlateCheckBox E-Slate aspect creation"},

            {"PreferredSize", "Preferred size"},
            {"setPreferredSizeTip", "Sets slider's preferred size "},

            {"MaximumSize", "Maximum size"},
            {"setMaximumSizeTip", "Sets slider's maximum size"},

            {"MinimumSize", "Minimum size"},
            {"setMinimumSizeTip", "Sets slider's minimum size "},
        };

}
