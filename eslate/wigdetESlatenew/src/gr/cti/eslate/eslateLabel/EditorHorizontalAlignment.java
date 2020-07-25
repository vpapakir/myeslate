package gr.cti.eslate.eslateLabel;


import gr.cti.eslate.utils.TaggedIntegerPropertyEditor;


public class EditorHorizontalAlignment extends TaggedIntegerPropertyEditor {
    public EditorHorizontalAlignment() {
        super("HorizontalAlignment", getPos(), getPosNames());

    }

    private static int[] getPos() {
        int[] ints = new int[] {2, 0, 4, 10, 11};

        return ints;
    }

    private static String[] getPosNames() {
        String[] name = new String[5];

        name[0] = ESlateLabelBeanInfo.bundleMessages.getString("left");
        name[1] = ESlateLabelBeanInfo.bundleMessages.getString("center");
        name[2] = ESlateLabelBeanInfo.bundleMessages.getString("right");
        name[3] = ESlateLabelBeanInfo.bundleMessages.getString("leading");
        name[4] = ESlateLabelBeanInfo.bundleMessages.getString("trailing");
        return name;
    }
}
