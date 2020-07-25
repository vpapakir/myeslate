package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.TaggedIntegerPropertyEditor;

public class EditorToolBorder extends TaggedIntegerPropertyEditor {
    public EditorToolBorder() {
        super("borderPolicy",getPos(),getPosNames());
    }

    private static int[] getPos() {
        int[] ints=new int[3];
        ints[0]=0;
        ints[1]=1;
        ints[2]=2;
        return ints;
    }

    private static String[] getPosNames() {
        String[] name=new String[3];
        name[0]=MapViewerBeanInfo.bundle.getString("always");
        name[1]=MapViewerBeanInfo.bundle.getString("never");
        name[2]=MapViewerBeanInfo.bundle.getString("onmouse");
        return name;
    }
}
