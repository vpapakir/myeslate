package gr.cti.eslate.scene3d.viewer;

public class Time {
    private long  baseTime = System.currentTimeMillis();
    private long  lastTime = System.currentTimeMillis();
    private float curTime;
    private float deltaT;

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public float deltaT() {
        return deltaT;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public float time() {
        return curTime;
    }

    /**
     * Method description
     *
     */
    public void update() {
        long tmpTime = System.currentTimeMillis();

        curTime  = (tmpTime - baseTime) / 1000.0f;
        deltaT   = (tmpTime - lastTime) / 1000.0f;
        lastTime = tmpTime;
    }
}
