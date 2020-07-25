package gr.cti.eslate.tableInspector;

import gr.cti.eslate.utils.TaggedIntegerPropertyEditor;

public class EditorImageAlignment extends TaggedIntegerPropertyEditor {
    public EditorImageAlignment() {
        super("imageAlignment",getPos(),getPosNames());
    }

    private static int[] getPos() {
        int[] ints=new int[2];
        ints[0]=0;
        ints[1]=1;
        return ints;
    }

    private static String[] getPosNames() {
        String[] name=new String[2];
        name[0]=TableInspectorBeanInfo.bundle.getString("left");
        name[1]=TableInspectorBeanInfo.bundle.getString("right");
        return name;
    }
}
