package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class LayoutPropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"EditLayout",      "Edit layout"},
        {"UnknownLayout",   "Unkwown layout manager"},
        {LayoutPropertyEditor.NONE, "No layout manager"},
        {LayoutPropertyEditor.BORDER, "Border layout"},
        {LayoutPropertyEditor.FLOW, "Flow layout"},
        {LayoutPropertyEditor.GRID, "Grid layout"},
        {LayoutPropertyEditor.BOX, "Box layout"},
        {LayoutPropertyEditor.CARD, "Card layout"},
    };
}

