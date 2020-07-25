package gr.cti.eslate.math.linalg;


/** Interpolates between two sets of camera and look-at positions by
 interpolating r, theta, and phi. It chooses the shortest path of
 interpolation of theta. */

public class SphericalCoordInterpolator {
    private static final float TWO_PI = (float) (Math.PI * 2.0);

    // FIXME: this can be generalized to support motion of the end
    // position and look-at positions, but care must be taken to not let
    // the direction of theta interpolation change.
    private Vec3f startPos;
    private Vec3f startLookAt;
    private Vec3f endPos;
    private Vec3f endLookAt;

    // Intermediate results in spherical coordinates
    // (RTP == radius theta phi; placed in x,y,z slots)
    private Vec3f startRTP = new Vec3f();
    private Vec3f endRTP = new Vec3f();

    private float thetaDiff;

    private Vec3f tmp = new Vec3f();
    private Vec3f curPos = new Vec3f();
    private Vec3f curLookAt = new Vec3f();

    public void setup(Vec3f startPos, Vec3f startLookAt,
        Vec3f endPos, Vec3f endLookAt) {
        this.startPos = new Vec3f(startPos);
        this.startLookAt = new Vec3f(startLookAt);
        this.endPos = new Vec3f(endPos);
        this.endLookAt = new Vec3f(endLookAt);

        toSphericalCoords(startPos, startLookAt, startRTP);
        toSphericalCoords(endPos, endLookAt, endRTP);

        computeThetaDiff();
    }

    /** alpha ranges from 0.0 to 1.0; 0.0 is the start position/look-at
     pair and 1.0 the end pair. */
    public void interpolate(float alpha) {
        if (alpha < 0) alpha = 0;
        if (alpha > 1) alpha = 1;

        tmp.setX((1.0f - alpha) * startRTP.x() + alpha * endRTP.x());
        // Theta gets special treatment
        tmp.setY(startRTP.y() + alpha * thetaDiff);
        tmp.setZ((1.0f - alpha) * startRTP.z() + alpha * endRTP.z());
        curLookAt.set(startLookAt);
        curLookAt.scale(1.0f - alpha);
        curLookAt.addScaled(curLookAt, alpha, endLookAt);
        fromSphericalCoords(tmp, curLookAt, curPos);
    }

    /** Call interpolate() first to compute this value. Do not mutate
     the returned vector. */
    public Vec3f position() {
        return curPos;
    }

    /** Call interpolate() first to compute this value. Do not mutate
     the returned vector. */
    public Vec3f lookAt() {
        return curLookAt;
    }

    //----------------------------------------------------------------------
    // Internals only below this point
    //

    private void computeThetaDiff() {
        float theta1 = startRTP.y();
        float theta2 = endRTP.y();

        thetaDiff = theta2 - theta1;
        if (thetaDiff > Math.PI)  thetaDiff -= TWO_PI;
        else if (thetaDiff < -Math.PI) thetaDiff += TWO_PI;
    }

    private static void toSphericalCoords(Vec3f pos,
        Vec3f lookAt,
        Vec3f rtp) {
        Vec3f diff = pos.minus(lookAt);
        float r = diff.length();

        rtp.setX(r);
        float theta = (float) Math.atan2(diff.z(), diff.x());

        // Move zero-crossing to other side of sphere
        if (theta < 0) theta += TWO_PI;
        rtp.setY(theta);
        rtp.setZ((float) Math.asin(MathUtils.clamp(diff.y() / r, -1, 1)));
    }

    private static void fromSphericalCoords(Vec3f rtp,
        Vec3f origin,
        Vec3f pos) {
        float cosPhi = (float) Math.cos(rtp.z());
        float sinPhi = (float) Math.sin(rtp.z());
        float cosTheta = (float) Math.cos(rtp.y());
        float sinTheta = (float) Math.sin(rtp.y());

        pos.setX(rtp.x() * cosPhi * cosTheta);
        pos.setY(rtp.x() * sinPhi);
        pos.setZ(rtp.x() * cosPhi * sinTheta);
        pos.add(origin);
    }
}
