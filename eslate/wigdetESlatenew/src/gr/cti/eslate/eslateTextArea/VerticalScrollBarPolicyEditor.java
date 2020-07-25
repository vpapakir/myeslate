package gr.cti.eslate.eslateTextArea;


import gr.cti.eslate.utils.*;


public class VerticalScrollBarPolicyEditor extends TaggedIntegerPropertyEditor {
    public VerticalScrollBarPolicyEditor() {
        super("VerticalScrollBarPolicy", getPos(), getPosNames());
    }

    private static int[] getPos() {
        int[] ints = new int[] {20, 21, 22};

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
