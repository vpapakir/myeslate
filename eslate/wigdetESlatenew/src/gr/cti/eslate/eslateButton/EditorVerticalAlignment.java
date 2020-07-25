package gr.cti.eslate.eslateButton;


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

        name[0] = ESlateButtonBeanInfo.bundleMessages.getString("top");
        name[1] = ESlateButtonBeanInfo.bundleMessages.getString("center");
        name[2] = ESlateButtonBeanInfo.bundleMessages.getString("bottom");

        return name;
    }

}
