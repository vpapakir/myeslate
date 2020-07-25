package gr.cti.eslate.eslateLabel;


import gr.cti.eslate.utils.TaggedIntegerPropertyEditor;


public class EditorVerticalTextPosition extends TaggedIntegerPropertyEditor {
    public EditorVerticalTextPosition() {
        super("VerticalTextPosition", getPos(), getPosNames());
    }

    private static int[] getPos() {
        int[] ints = new int[] {1, 0, 3};

        return ints;
    }

    private static String[] getPosNames() {
        String[] name = new String[3];

        name[0] = ESlateLabelBeanInfo.bundleMessages.getString("top");
        name[1] = ESlateLabelBeanInfo.bundleMessages.getString("center");
        name[2] = ESlateLabelBeanInfo.bundleMessages.getString("bottom");

        return name;
    }
}
