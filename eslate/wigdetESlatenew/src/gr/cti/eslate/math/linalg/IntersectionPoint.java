package gr.cti.eslate.math.linalg;


public class IntersectionPoint {
    private Vec3f intPt;
    private float t;

    public IntersectionPoint() {
        intPt = new Vec3f();
    }

    public Vec3f getIntersectionPoint() {
        return intPt;
    }

    public void setIntersectionPoint(Vec3f newPt) {
        this.intPt.set(newPt);
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }
}
