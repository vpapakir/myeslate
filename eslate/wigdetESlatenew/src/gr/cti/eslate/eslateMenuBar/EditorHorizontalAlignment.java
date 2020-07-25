package gr.cti.eslate.eslateMenuBar;


import gr.cti.eslate.utils.*;


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

        name[0] = ESlateMenuBeanInfo.bundleMessages.getString("left");
        name[1] = ESlateMenuBeanInfo.bundleMessages.getString("center");
        name[2] = ESlateMenuBeanInfo.bundleMessages.getString("right");
        name[3] = ESlateMenuBeanInfo.bundleMessages.getString("leading");
        name[4] = ESlateMenuBeanInfo.bundleMessages.getString("trailing");
        return name;
    }
}
