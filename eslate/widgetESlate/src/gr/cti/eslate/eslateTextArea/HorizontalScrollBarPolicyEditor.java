package gr.cti.eslate.eslateTextArea;


import gr.cti.eslate.utils.*;


public class HorizontalScrollBarPolicyEditor extends TaggedIntegerPropertyEditor {
    public HorizontalScrollBarPolicyEditor() {
        super("HorizontalScrollBarPolicy", getPos(), getPosNames());
    }

    private static int[] getPos() {
        int[] ints = new int[] {30, 31, 32};

        return ints;
    }

    private static String[] getPosNames() {
        String[] name = new String[3];

        name[0] = ESlateTextAreaBeanInfo.bundleMessages.getString("if needed");
        name[1] = ESlateTextAreaBeanInfo.bundleMessages.getString("never");
        name[2] = ESlateTextAreaBeanInfo.bundleMessages.getString("always");
        return name;
    }

}
