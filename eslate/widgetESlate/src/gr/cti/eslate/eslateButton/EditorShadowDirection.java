package gr.cti.eslate.eslateButton;


import gr.cti.eslate.utils.*;


public class EditorShadowDirection extends TaggedIntegerPropertyEditor {

    public EditorShadowDirection() {
        super("ShadowDirection", getPos(), getPosNames());
    }

    private static int[] getPos() {
        int[] ints = new int[] {1, 2, 3, 4, 5, 6, 7, 8};

        return ints;
    }

    private static String[] getPosNames() {
        String[] name = new String[8];

        name[0] = ESlateButtonBeanInfo.bundleMessages.getString("east");
        name[1] = ESlateButtonBeanInfo.bundleMessages.getString("north_east");
        name[2] = ESlateButtonBeanInfo.bundleMessages.getString("north");
        name[3] = ESlateButtonBeanInfo.bundleMessages.getString("north_west");
        name[4] = ESlateButtonBeanInfo.bundleMessages.getString("west");
        name[5] = ESlateButtonBeanInfo.bundleMessages.getString("south_west");
        name[6] = ESlateButtonBeanInfo.bundleMessages.getString("south");
        name[7] = ESlateButtonBeanInfo.bundleMessages.getString("south_east");
        return name;
    }

}

