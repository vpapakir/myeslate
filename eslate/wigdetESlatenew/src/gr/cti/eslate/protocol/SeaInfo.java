package gr.cti.eslate.protocol;

public interface SeaInfo extends MotionInfo {
    /**
     * @ return Given the <em>depth</em> this function returns <em>true</em> if it is posible to dive in this depth.
     * (and don't crash on a coral!)
     */
    public boolean isValidDepth(double depth);
}




