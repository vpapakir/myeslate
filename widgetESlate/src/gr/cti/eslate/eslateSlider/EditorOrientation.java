package gr.cti.eslate.eslateSlider;


import gr.cti.eslate.utils.*;


public class EditorOrientation extends TaggedIntegerPropertyEditor {
    public EditorOrientation() {
        super("Orientation", getPos(), getPosNames());
    }

    private static int[] getPos() {
        int[] ints = new int[] {0, 1};

        return ints;
    }

    private static String[] getPosNames() {
        String[] name = new String[2];

        name[0] = ESlateSliderBeanInfo.bundleMessages.getString("horizontal");
        name[1] = ESlateSliderBeanInfo.bundleMessages.getString("vertical");
        return name;
    }
}
