package gr.cti.eslate.eslateMenuBar;


import java.util.ListResourceBundle;


public class PopupBundleMessages extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"List Element", "List Element"},

            {"DialogTitle", "Define menu bar menus"},
            {"OK", "OK"},
            {"Cancel", "Cancel"},
            {"<<Separator>>", "<<Separator>>"},
            {"Input", "Element modification"},
            {"New Node ", "New Node"},
            {"UpButtonTip", "Move upwards"},
            {"DownButtonTip", "Move downwards"},
            {"AddButtonTip", "Insert new"},
            {"DeleteButtonTip", "Remove selected"},
            {"SeparatorButtonTip", "Insert separator"},
            {"CheckButtonTip", "Add check menu item "},
        };
}
