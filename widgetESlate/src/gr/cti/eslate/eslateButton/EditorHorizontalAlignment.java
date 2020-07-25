package gr.cti.eslate.eslateButton;


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

        name[0] = ESlateButtonBeanInfo.bundleMessages.getString("left");
        name[1] = ESlateButtonBeanInfo.bundleMessages.getString("center");
        name[2] = ESlateButtonBeanInfo.bundleMessages.getString("right");
        name[3] = ESlateButtonBeanInfo.bundleMessages.getString("leading");
        name[4] = ESlateButtonBeanInfo.bundleMessages.getString("trailing");
        return name;
    }
   
}
