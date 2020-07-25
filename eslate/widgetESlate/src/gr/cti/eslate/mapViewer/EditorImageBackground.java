package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.TaggedIntegerPropertyEditor;

public class EditorImageBackground extends TaggedIntegerPropertyEditor {
    public EditorImageBackground() {
        super("imageViewPolicy",getImg(),getImgNames());
    }

    private static int[] getImg() {
        int[] ints=new int[3];
        ints[0]=0;
        ints[1]=1;
        ints[2]=2;
        return ints;
    }

    private static String[] getImgNames() {
        String[] name=new String[3];
        name[0]=MapViewerBeanInfo.bundle.getString("normal");
        name[1]=MapViewerBeanInfo.bundle.getString("stretched");
        name[2]=MapViewerBeanInfo.bundle.getString("tiled");
        return name;
    }
}
