package gr.cti.eslate.math.linalg;

/**
 * Created by IntelliJ IDEA.
 * User: mantesat
 * Date: 24 Δεκ 2004
 * Time: 12:11:47 μμ
 * To change this template use Options | File Templates.
 */

/** Interpolates between two Vec3ds. */

public class Vec3dInterpolator {
    private Vec3d start;
    private Vec3d end;
    private Vec3d tmp = new Vec3d();
//    boolean firstTime ;

    public void setup(Vec3d start, Vec3d end) {
        this.start = new Vec3d(start);
        this.end = new Vec3d(end);
//        firstTime = true;
    }

    /** alpha ranges from 0.0 to 1.0; 0.0 is the start position/look-at
     pair and 1.0 the end pair. */
    public Vec3d interpolate(float alpha) {
        if (alpha < 0) alpha = 0;
        if (alpha > 1) alpha = 1;
        tmp.set(start);
        tmp.scale(1.0f - alpha);
        tmp.addScaled(tmp, alpha, end);
/*        if (firstTime){
            tmp.set(start);
            firstTime = false;
        }*/
        return tmp;
    }

    /** Call interpolate() first to compute this value. Do not mutate
     the returned vector. */
    public Vec3d value() {
        return tmp;
    }
}
