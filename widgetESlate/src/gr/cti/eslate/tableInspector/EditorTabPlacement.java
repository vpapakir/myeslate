package gr.cti.eslate.tableInspector;

import gr.cti.eslate.utils.TaggedIntegerPropertyEditor;

import javax.swing.SwingConstants;

public class EditorTabPlacement extends TaggedIntegerPropertyEditor {
    public EditorTabPlacement() {
        super("tabPlacement",getPos(),getPosNames());
    }

    private static int[] getPos() {
        int[] ints=new int[4];
        ints[0]=SwingConstants.TOP;
        ints[1]=SwingConstants.BOTTOM;
        ints[2]=SwingConstants.LEFT;
        ints[3]=SwingConstants.RIGHT;
        return ints;
    }

    private static String[] getPosNames() {
        String[] name=new String[4];
        name[0]=TableInspectorBeanInfo.bundle.getString("top");
        name[1]=TableInspectorBeanInfo.bundle.getString("bottom");
        name[2]=TableInspectorBeanInfo.bundle.getString("left");
        name[3]=TableInspectorBeanInfo.bundle.getString("right");
        return name;
    }
}
