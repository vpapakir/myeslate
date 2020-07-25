package gr.cti.eslate.eslateList;


import gr.cti.eslate.utils.*;


public class SelectionModePropertyEditor extends TaggedIntegerPropertyEditor {
    public SelectionModePropertyEditor() {
        super("SelectionMode", getMode(), getModeNames());
    }

    private static int[] getMode() {
        int[] ints = new int[] {0, 1, 2};

        return ints;
    }

    private static String[] getModeNames() {
        String[] name = new String[3];

        name[0] = ESlateListBeanInfo.bundleMessages.getString("single selection");
        name[1] = ESlateListBeanInfo.bundleMessages.getString("single interval selection");
        name[2] = ESlateListBeanInfo.bundleMessages.getString("multiple interval selection");

        return name;
    }
}
