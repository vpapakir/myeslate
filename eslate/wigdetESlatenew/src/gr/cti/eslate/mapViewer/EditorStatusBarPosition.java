package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.TaggedIntegerPropertyEditor;

public class EditorStatusBarPosition extends TaggedIntegerPropertyEditor {
    public EditorStatusBarPosition() {
        super("statusbarPosition",getPos(),getPosNames());
    }

    private static int[] getPos() {
        int[] ints=new int[2];
        ints[0]=0;
        ints[1]=1;
        return ints;
    }

    private static String[] getPosNames() {
        String[] name=new String[2];
        name[0]=MapViewerBeanInfo.bundle.getString("up");
        name[1]=MapViewerBeanInfo.bundle.getString("down");
        return name;
    }
}