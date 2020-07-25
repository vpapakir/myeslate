package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class LayoutPropertyEditorBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"EditLayout",      "Αλλαγή διάταξης"},
        {"UnknownLayout",   "Άγνωστος χειριστής διάταξης"},
        {LayoutPropertyEditor.NONE, "Κανένας χειριστής διάταξης"},
        {LayoutPropertyEditor.BORDER, "Διάταξη συνόρων"},
        {LayoutPropertyEditor.FLOW, "Διάταξη ροής"},
        {LayoutPropertyEditor.GRID, "Διάταξη πλέγματος"},
        {LayoutPropertyEditor.BOX, "Διάταξη κουτιού"},
        {LayoutPropertyEditor.CARD, "Διάταξη καρτών"},
    };
}
