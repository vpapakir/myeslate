package gr.cti.eslate.eslateMenuBar;


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
            {"componentName", "Menu system component"},
            {"name", "ESlateMenuSystem"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"design", "Design idea : G.Tsironis "},
            {"development", "Development : Th. Mantes "},
            {"funding", "� Computer Technology Institute"},
            {"copyright", "Copyright ���������� ���������� ���������� ����������� �����������-��.��� 1993-2008.\n�������� ��������� ������� ��� ��� Conceptum AE ��� ���DUS AE.\n�� ������ 2.0 ��������� �� ����� ����� (dual license), ���� ������� �� ����  ��� GNU-GPL License ��� ���  L-GPL license.\n\n����������� � ��������, ������� ��� �������� ������������ �������� � �������� ������ ��������� ���������\n���������� ���� ��������� ������ 2.0 ��� ���� �������� ������� ��� ����� ������ ��� �� �����������������\n��� �� ���������� ���� �� �� �������� ���� � ��������� �� ����� ������� �/��� ����������� ������."},
            {"version", "version"},

            //Bean Info
            {"ESlateMenuBar", "Menu System"},

            {"Menus...", "Menus..."},
            {"New Node ", "New Node"},
            {"PathChanged", "Path changed"},
            {"<<Separator>>", "<<Separator>>"},

            {"Menus", "Menu editor"},
            {"setMenusTip", "Sets menuBar's children"},

            {"Background", "Background"},
            {"setBackgroundTip", "Sets menuBar's background"},

            {"Foreground", "�ext color"},
            {"setForegroundTip", "Sets menuBar's text color"},

            {"Border", "Border"},
            {"setBorderTip", "Sets menuBar's Border"},

            {"Font", "Font"},
            {"setFontTip", "Sets menuBar's Font"},

            {"BorderPainted", "BorderPainted"},
            {"setBorderPaintedTip", "Make border visible or hide it"},

            {"ToolTipText", "TooltipText"},
            {"setToolTipTextTip", "Sets menuBar's tooltiptext"},

            {"Opaque", "Opaque"},
            {"setOpaqueTip", "Defines if menuBar is opaque"},

            {"Name", "Name"},
            {"setNameTip", "MenuBar's name "},

            {"AlignmentX", "X axis alignment"},
            {"setAlignmentXTip", " Defines axis X alignment"},

            {"AlignmentY", "Y axis alignment"},
            {"setAlignmentYTip", "Defines axis Y alignment"},

            {"DoubleBuffered", "DoubleBuffered"},
            {"setDoubleBufferedTip", "Returns whether the receiving menuBar should use a buffer to paint"},

            {"Layout", "Layout"},
            {"setLayoutTip", "Defines space layout"},

            {"Margin", "Margin"},
            {"setMarginTip", "Defines margin"},

            {"Enabled", "Enabled"},
            {"setEnabledTip", "Defines if menuBar is enabled"},

            {"MaximumSize", "Maximum size"},
            {"setMaximumSizeTip", "Defines menubar's maximum size"},

            {"PreferredSize", "Preferred size"},
            {"setPreferredSizeTip", "Defines menubar's preferred size"},

            {"MinimumSize", "Minimum size"},
            {"setMinimumSizeTip", "Defines menubar's minimum size"},

            {"Location", "Location"},
            {"setLocationTip", "Defines menubar's exact location"},

            {"Autoscrolls", "Auto scrolling"},
            {"setAutoscrollsTip", "Defines if auto scrolling is enabled"},

            {"Visible", "Visible"},
            {"setVisibleTip", "Defines if menubar is visible"},

            {"RequestFocusEnabled", "Request Focus Enabled"},
            {"setRequestFocusEnabledTip", "Sets whether the receiving menubar can obtain the focus by calling requestFocus"},

            {"MenuScrollSpeed", "Menu scrolling speed"},
            {"setMenuScrollSpeedTip", " Sets the scrolling speed of the menubar's menus"},

            {"menuItemCanSendEventAgain", "Item can send multiple selection events"},
            {"menuItemCanSendEventAgainTip", " If checked, then a menu item can send events even if it is already selected"},

            {"LoadTimer", "MenuBar load"},
            {"SaveTimer", "MenuBar save"},
            {"ConstructorTimer", "MenuBar constructor"},
            {"InitESlateAspectTimer", "MenuBar e-Slate part creation"},

        };
}
