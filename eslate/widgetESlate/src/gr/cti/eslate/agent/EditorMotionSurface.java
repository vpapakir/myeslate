package gr.cti.eslate.agent;

import gr.cti.eslate.utils.TaggedIntegerPropertyEditor;

public class EditorMotionSurface extends TaggedIntegerPropertyEditor {
    public EditorMotionSurface() {
        super("motionSurface",getPos(),getPosNames());
    }

    private static int[] getPos() {
        int[] ints=new int[2];
        ints[0]=0;
        ints[1]=1;
        return ints;
    }

    private static String[] getPosNames() {
        String[] name=new String[2];
        name[0]=AgentBeanInfo.bundle.getString("motionPlane");
        name[1]=AgentBeanInfo.bundle.getString("motionEarth");
        return name;
    }
}
