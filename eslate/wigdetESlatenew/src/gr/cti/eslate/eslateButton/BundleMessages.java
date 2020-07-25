package gr.cti.eslate.eslateButton;


import java.util.ListResourceBundle;


public class BundleMessages extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"Title", "Title"},
            {"Color", "Color"},
            {"Pressed", "Selection"},
            // component info
            {"componentName", "Button component"},
            {"name", "ESlateButton"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th. Mantes"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},
            //Bean Info
            {"ESlateButton", "Button"},

            {"Background", "Background"},
            {"setBackgroundTip", "Sets button's background"},

            {"Foreground", "Text color"},
            {"setForegroundTip", "Sets button's text color"},

            {"Border", "Border"},
            {"setBorderTip", "Sets button's Border"},

            {"Font", "Font"},
            {"setFontTip", "Sets Button's Font"},

            {"BorderPainted", "BorderPainted"},
            {"setBorderPaintedTip", "Make border visible or hide it"},

            {"Icon", "Icon"},
            {"setIconTip", "Sets button's icon "},

            {"SelectedIcon", "SelectedIcon"},
            {"setSelectedIconTip", "Sets button's SelectedIcon"},

            {"RolloverIcon", "RolloverIcon"},
            {"setRolloverIconTip", "Sets button's RolloverIcon "},

            {"PressedIcon", "PressedIcon"},
            {"setPressedIconTip", "Sets button's pressed icon "},

            {"Text", "Text"},
            {"setTextTip", "Sets button's text"},

            {"ToolTipText", "TooltipText"},
            {"setToolTipTextTip", "Sets button's tooltiptext"},

            {"ContentAreaFilled", "Content area filled"},
            {"setContentAreaFilledTip", "Defines if inner area is color-filled"},

            {"Selected", "Selected"},
            {"setSelectedTip", "Sets button to be selected"},

            {"RolloverEnabled", "Rollover enabled"},
            {"setRolloverEnabledTip", "Button's responce when mouse is passing over "},

            {"VerticalAlignment", "Vertical alignment"},
            {"setVerticalAlignmentTip", "Defines Vertical alignment"},

            {"VerticalTextPosition", "Vertical text position"},
            {"setVerticalTextPositionTip", "Defines Vertical text position"},

            {"HorizontalTextPosition", "Horizontal text position"},
            {"setHorizontalTextPositionTip", "Defines Horizontal text position"},

            {"HorizontalAlignment", "Horizontal alignment"},
            {"setHorizontalAlignmentTip", "Defines Horizontal alignment"},

            {"FocusPainted", "Focus painted"},
            {"setFocusPaintedTip", "Defines if button is focus painted or not"},

            {"PressedIcon", "Pressed icon"},
            {"setPressedIconTip", "Defines pressed button's icon "},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Defines if button is opaque"},

            {"Name", "Name"},
            {"setNameTip", "Button's name "},

            {"DisabledIcon", "Disabled icon"},
            {"setDisabledIconTip", "Defines the disabled button's icon"},

            {"DisabledSelectedIcon", "Disabled selected icon"},
            {"setDisabledSelectedIconTip", "Defines the disabled selected icon"},

            {"AlignmentX", "X axis alignment"},
            {"setAlignmentXTip", " Defines axis X alignment"},

            {"AlignmentY", "Y axis alignment"},
            {"setAlignmentYTip", "Defines axis Y alignment"},

            {"DoubleBuffered", "DoubleBuffered"},
            {"setDoubleBufferedTip", "Returns whether the receiving button should use a buffer to paint"},

            {"Layout", "Layout"},
            {"setLayoutTip", "Defines space layout"},

            {"Margin", "Margin"},
            {"setMarginTip", "Defines margin"},

            {"Enabled", "Enabled"},
            {"setEnabledTip", "Defines if button is enabled"},

            {"RequestFocusEnabled", "RequestFocusEnabled"},
            {"setRequestFocusEnabledTip", "Defines if button is RequestFocusEnabled"},

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

            {"3DLabel", "3DLabel"},
            {"set3DLabelTip", "Defines if label is 3D enabled or not"},

            {"Alignment", "Alignment"},
            {"setAlignmentTip", "Sets the alignment of the text"},

            {"BorderColor", "Text's Border Color"},
            {"setBorderColorTip", "Sets the border color of the text"},

            {"BorderWidth", "Text's Border Width"},
            {"setBorderWidthTip", "Sets the border width of the text"},

            {"ShadowColor", "Text's Shadow Color"},
            {"setShadowColorTip", "Sets the shadow color for the text"},

            {"ShadowDirection", "Text's Shadow Direction"},
            {"setShadowDirectionTip", "Sets the text's shadow direction"},

            {"ShadowWidth", "Text's Shadow Width"},
            {"setShadowWidthTip", "Sets the text's shadow width"},

            {"PlugsUsed", "Plugs Use"},
            {"setPlugsUsedTip", "Sets whether the component's plugs should be created for use"},

            {"east", "EAST"},
            {"north_east", "NORTH_EAST"},
            {"north", "NORTH"},
            {"north_west", "NORTH_WEST"},
            {"west", "WEST"},
            {"south_west", "SOUTH_WEST"},
            {"south", "SOUTH"},
            {"south_east", "SOUTH_EAST"},

            {"ConstructorTimer", "ESlateButton constructor"},
            {"LoadTimer", "ESlateButton load"},
            {"SaveTimer", "ESlateButton save"},

            {"InitESlateAspectTimer", "ESlateButton E-Slate aspect creation"},
        };

}
