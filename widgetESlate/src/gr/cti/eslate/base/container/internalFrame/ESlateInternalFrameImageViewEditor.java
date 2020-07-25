package gr.cti.eslate.base.container.internalFrame;

import gr.cti.eslate.utils.TaggedIntegerPropertyEditor;


public class ESlateInternalFrameImageViewEditor extends TaggedIntegerPropertyEditor {
    public ESlateInternalFrameImageViewEditor() {
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
        name[0]=ESlateInternalFrameBeanInfo.bundle.getString("centered");
        name[1]=ESlateInternalFrameBeanInfo.bundle.getString("stretched");
        name[2]=ESlateInternalFrameBeanInfo.bundle.getString("tiled");
        return name;
    }
}