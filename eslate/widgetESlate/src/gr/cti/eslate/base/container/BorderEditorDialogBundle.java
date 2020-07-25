package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class BorderEditorDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",         "Edit border"},
        {"BorderProperties",    "Border properties"},
        {"OK",                  "OK"},
        {"Cancel",              "Cancel"},
        {"Apply",               "Apply"},
        {BorderEditorDialog.BEVELBORDER,  BorderEditorDialog.BEVELBORDER},
        {BorderEditorDialog.SOFTBEVELBORDER, BorderEditorDialog.SOFTBEVELBORDER},
        {BorderEditorDialog.EMPTYBORDER, BorderEditorDialog.EMPTYBORDER},
        {BorderEditorDialog.LINEBORDER, BorderEditorDialog.LINEBORDER},
        {BorderEditorDialog.ETCHEDBORDER, BorderEditorDialog.ETCHEDBORDER},
        {BorderEditorDialog.TITLEDBORDER, BorderEditorDialog.TITLEDBORDER},
        {BorderEditorDialog.ONELINEBEVELBORDER, BorderEditorDialog.ONELINEBEVELBORDER},
        {BorderEditorDialog.NOTOPONELINEBEVELBORDER, BorderEditorDialog.NOTOPONELINEBEVELBORDER},
        {BorderEditorDialog.MATTEBORDER, BorderEditorDialog.MATTEBORDER},
        {BorderEditorDialog.COMPOUNDBORDER, BorderEditorDialog.COMPOUNDBORDER},
        {BorderEditorDialog.NOBORDER, BorderEditorDialog.NOBORDER},
    };
}
