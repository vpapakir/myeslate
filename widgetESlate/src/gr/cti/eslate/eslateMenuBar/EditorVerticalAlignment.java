package gr.cti.eslate.eslateMenuBar;


import gr.cti.eslate.utils.*;


public class EditorVerticalAlignment extends TaggedIntegerPropertyEditor {
    public EditorVerticalAlignment() {
        super("VerticalAlignment", getPos(), getPosNames());
    }

    private static int[] getPos() {
        int[] ints = new int[] {1, 0, 3};

        return ints;
    }

    private static String[] getPosNames() {
        String[] name = new String[3];

        name[0] = ESlateMenuBeanInfo.bundleMessages.getString("top");
        name[1] = ESlateMenuBeanInfo.bundleMessages.getString("center");
        name[2] = ESlateMenuBeanInfo.bundleMessages.getString("bottom");

        return name;
    }
}
