package gr.cti.eslate.protocol;

public interface AirInfo extends MotionInfo {
    /**
     * @ return Given the <em>height</em> this function returns <em>true</em> if it is posible to fly in this height.
     * (and don't crash on a mountain!)
     */
    public boolean isValidHeight(double height);
}

