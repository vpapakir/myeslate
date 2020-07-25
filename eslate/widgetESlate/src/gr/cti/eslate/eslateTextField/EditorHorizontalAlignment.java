package gr.cti.eslate.eslateTextField;


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

        name[0] = ESlateTextFieldBeanInfo.bundleMessages.getString("left");
        name[1] = ESlateTextFieldBeanInfo.bundleMessages.getString("center");
        name[2] = ESlateTextFieldBeanInfo.bundleMessages.getString("right");
        name[3] = ESlateTextFieldBeanInfo.bundleMessages.getString("leading");
        name[4] = ESlateTextFieldBeanInfo.bundleMessages.getString("trailing");
        return name;
    }
}
