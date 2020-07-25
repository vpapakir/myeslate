package gr.cti.eslate.webWindow;


import java.util.ListResourceBundle;


public class BrowserBundle extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"URL", "URL"},
            {"URL2", "URL2"},
            // component info
            {"componentName", "WebBrowser component"},
            {"name", "ESlateWebBrowser"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th. Mantes"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},

            {"Connecting to ", "Connecting to "},
            //Bean Info
            {"ESlateWebBrowser", "WebBrowser"},

            {"BrowserMenuBarVisible", "Menubar visible"},
            {"setMenuBarVisibleTip", "Sets the menubar to be visible or not"},
            {"BrowserLocationPanelVisible", "URL field visible"},
            {"setLocationPanelVisibleTip", "Sets the URL field to be visible or not"},
            {"BrowserButtonPanelVisible", "Button toolbar visible"},
            {"setButtonPanelVisibleTip", "Sets the button toolbar to be visible or not"},
            {"BrowserStatusPanelVisible", "Status bar visible"},
            {"setStatusPanelVisibleTip", "Sets the status bar to be visible or not"},

            {"ClearHistory", "Clear History"},
            {"Proxy Settings", "Proxy Settings"},
            {"Set Font...", "Set Font..."},
            {"Character Encoding...", "Characted Encoding..."},
            {"Character Encoding", "Characted Encoding"},
            {"Find...", "Find..."},
            {"Find Text", "Find Text"},
            {"FindNext", "Find Next   F3"},
            {"Open...", "Open..."},
            {"About...", "About..."},
            {"About Message", "About Message"},
            {"Show Debug Messages", "Show Debug Messages"},
            {"IFRAMES Enabled", "IFRAMES Enabled"},
            {"Frames Enabled", "Frames Enabled"},
            {"Render While Parsing", "Render While Parsing"},
            {"Progressive Image Display", "Progressive Image Display"},
            {"Always Sizable Frames", "Always Sizable Frames"},
            {"Enable Tool Tips", "Enable Tool Tips"},
            {"Load Applets", "Load Applets"},
            {"Load Images", "Load Images"},
            {"Close", "Close"},
            {"File", "File"},
            {"Edit", "Edit"},
            {"Help", "Help"},
            {"Unload Page", "Unload Page"},

            {"Open", "Open"},
            {"Back", "Back"},
            {"Forward", "Forward"},
            {"Home", "Home"},
            {"Reload", "Reload"},
            {"Print", "Print"},
            {"Search", "Search"},
            {"Stop", "Stop"},
            {"About", "About"},

            {"Use Proxy", "Use proxy server"},
            {"Port:", "Port:"},
            {"Host:", "Host:"},
            {"OK", "OK"},
            {"Cancel", "Cancel"},
            {"Text", "Text:"},
            {"FindText", "Find text"},
            {"TextNotFound", "Text not found!"},
            {"Location:", "Location : "},
            {"Encoding:", "Encoding : "},
            {"NOResults", "No Results "},
            {"TextNotFound", "Text was not found"},

            {"Connecting to ", "Connecting to "},
            {"Reading content from ", "Reading content from "},
            {"Error occured!", "Error occured!"},

            {"HomeURL", "Homepage URL"},
            {"setHomeURLTip", "Sets the homepage's URL"},

            {"SearchURL", "Search page URL"},
            {"setSearchURLTip", "Sets the search page's URL"},

            {"Location", "URL"},
            {"setLocationTip", "Sets the URL"},

            {"Foreground", "Τext color"},
            {"setForegroundTip", "Sets button's text color"},

            {"Border", "Border"},
            {"setBorderTip", "Sets button's Border"},

            {"Font", "Font"},
            {"setFontTip", "Sets Button's Font"},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Sets opacity in HTML page region"},

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

            {"CharacterEncoding", "Character encoding"},
            {"setCharacterEncodingTip", "Sets the character encoding"},

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

            {"PlugsUsed", "Plugs Use"},
            {"setPlugsUsedTip", "Sets whether the component's plugs should be created for use"},

            {"Enabled", "Enabled"},
            {"setEnabledTip", "Defines if button is enabled"},

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

            {"MousePressedOnLink", "Mouse pressed on link"},
            {"setMousePressedTip", "Mouse was pressed on a link"},


            {"MouseReleased", "Mouse Released"},
            {"setMousePressedTip", "Mouse was released inside component's area"},

            {"MouseReleasedOnLink", "Mouse released on link"},
            {"setMousePressedTip", "Mouse was released on a link"},

            {"LinkClicked", "Link clicked"},
            {"setLinkClickedTip", "A click was performed on a link"},

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

            {"LoadTimer", "Browser load"},
            {"SaveTimer", "Browser save"},

            {"left", "LEFT"},
            {"center", "CENTER"},
            {"right", "RIGHT"},
            {"leading", "LEADING"},
            {"trailing", "TRAILING"},

            {"top", "TOP"},
            {"bottom", "BOTTOM"},
            {"ConstructorTimer", "WebBrowser constructor"},
            {"LoadTimer", "WebBrowser load"},
            {"SaveTimer", "WebBrowser save"},
            {"InitESlateAspectTimer", "WebBrowser E-Slate aspect creation"},
            {"WebWindowTimer",  "WebWindow construction"},

            {"MenuBarUsed", "Use menu bar"},
            {"ToolBarUsed", "Use toolbar"},
            {"LocationPanelUsed", "Use URL bar"},
            {"HtmlPaneBorder", "HTML pane border"},

            {"setMenuBarUsedTip", "Defines if menubar will be used"},
            {"setToolBarUsedTip", "Defines if toolbar will be used"},
            {"setLocationPanelUsedTip", "Defines if URL bar will be used"},
            {"setHtmlPaneBorderTip", "Defines the HTML pane's border"},

        };

}
