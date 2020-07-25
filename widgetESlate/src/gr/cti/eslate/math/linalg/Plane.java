package gr.cti.eslate.math.linalg;


public class Plane {

    /** Normalized */
    private Vec3f normal;
    private Vec3f point;

    /** Constant for faster projection and intersection */
    float c;

    /** Default constructor initializes normal to (0, 1, 0) and point to
     (0, 0, 0) */
    public Plane(){
        normal = new Vec3f(0, 1, 0);
        point = new Vec3f(0, 0, 0);
        recalc();
    }

    /** Sets all parameters of plane. Plane has normal <b>normal</b> and
     goes through the point <b>point</b>. Normal does not need to be
     unit length but must not be the zero vector. */
    public Plane(Vec3f normal, Vec3f point) {
        this.normal = normal;
        this.normal.normalize();
        this.point = point;
        recalc();
    }

    /** Setter does some work to maintain internal caches. Normal does
     not need to be unit length but must not be the zero vector. */
    public void setNormal(Vec3f normal) {
        this.normal.set(normal);
        this.normal.normalize();
        recalc();
    }

    /** Normal is normalized internally, so <b>normal</b> is not
     necessarily equal to <code>plane.setNormal(normal);
     plane.getNormal();</code> */
    public Vec3f getNormal() {
        return normal;
    }

    /** Setter does some work to maintain internal caches */
    public void setPoint(Vec3f point) {
        this.point.set(point);
        recalc();
    }

    public Vec3f getPoint() {
        return point;
    }

    /** Project a point onto the plane */
    public void projectPoint(Vec3f pt,
        Vec3f projPt) {
        float scale = normal.dot(pt) - c;

        projPt.set(pt.minus(normal.times(normal.dot(point) - c)));
    }

    /** Intersect a ray with the plane. Returns true if intersection occurred, false
     otherwise. This is a two-sided ray cast. */
    public boolean intersectRay(Vec3f rayStart,
        Vec3f rayDirection,
        IntersectionPoint intPt) {
        float denom = normal.dot(rayDirection);

        if (denom == 0)
            return false;
        intPt.setT((c - normal.dot(rayStart)) / denom);
        intPt.setIntersectionPoint(rayStart.plus(rayDirection.times(intPt.getT())));
        return true;
    }
    
    /** Intersect a ray with the plane. Returns true if intersection occurred, false
    otherwise. This is a two-sided ray cast. */
   public void intersectRay(Vec3f rayStart,Vec3f rayDirection) {
       float denom = normal.dot(rayDirection);

       if (denom == 0)
           return;
       float d = (c - normal.dot(rayStart)) / denom;
       rayStart.add(rayDirection.times(d));
   }

    //----------------------------------------------------------------------
    // Internals only below this point
    //

    private void recalc() {
        c = normal.dot(point);
    }
}
