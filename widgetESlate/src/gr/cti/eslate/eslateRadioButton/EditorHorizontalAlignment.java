package gr.cti.eslate.eslateRadioButton;


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

        name[0] = ESlateRadioButtonBeanInfo.bundleMessages.getString("left");
        name[1] = ESlateRadioButtonBeanInfo.bundleMessages.getString("center");
        name[2] = ESlateRadioButtonBeanInfo.bundleMessages.getString("right");
        name[3] = ESlateRadioButtonBeanInfo.bundleMessages.getString("leading");
        name[4] = ESlateRadioButtonBeanInfo.bundleMessages.getString("trailing");
        return name;
    }
}
