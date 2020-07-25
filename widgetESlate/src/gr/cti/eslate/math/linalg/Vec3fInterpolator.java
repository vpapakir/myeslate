package gr.cti.eslate.math.linalg;

/** Interpolates between two Vec3fs. */

public class Vec3fInterpolator {
    private Vec3f start;
    private Vec3f end;
    private Vec3f tmp = new Vec3f();
    private Vec3f tmp2 = new Vec3f();
//    boolean firstTime ;

    public void setup(Vec3f start, Vec3f end) {
        this.start = new Vec3f(start);
        this.end = new Vec3f(end);
//        firstTime = true;
    }

    /** alpha ranges from 0.0 to 1.0; 0.0 is the start position/look-at
     pair and 1.0 the end pair. */
    public Vec3f interpolate(float alpha) {
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
    public Vec3f value() {
        return tmp;
    }
}
