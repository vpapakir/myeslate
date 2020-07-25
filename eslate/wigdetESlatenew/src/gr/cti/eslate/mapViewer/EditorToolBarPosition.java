package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.TaggedIntegerPropertyEditor;

public class EditorToolBarPosition extends TaggedIntegerPropertyEditor {
    public EditorToolBarPosition() {
        super("toolbarPosition",getPos(),getPosNames());
    }

    private static int[] getPos() {
        int[] ints=new int[4];
        ints[0]=0;
        ints[1]=1;
        ints[2]=2;
        ints[3]=3;
        return ints;
    }

    private static String[] getPosNames() {
        String[] name=new String[4];
        name[0]=MapViewerBeanInfo.bundle.getString("up");
        name[1]=MapViewerBeanInfo.bundle.getString("down");
        name[2]=MapViewerBeanInfo.bundle.getString("right");
        name[3]=MapViewerBeanInfo.bundle.getString("left");
        return name;
    }
}
