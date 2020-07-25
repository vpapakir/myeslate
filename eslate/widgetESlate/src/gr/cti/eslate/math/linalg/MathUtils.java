package gr.cti.eslate.math.linalg;


public class MathUtils {

    public static final double TWO_PI=Math.PI*2;

    /** Makes an arbitrary vector perpendicular to <B>src</B> and
     inserts it into <B>dest</B>. Returns false if the source vector
     was equal to (0, 0, 0). */
    public static boolean makePerpendicular(Vec3f src,
        Vec3f dest) {
        if ((src.x() == 0.0f) && (src.y() == 0.0f) && (src.z() == 0.0f)) {
            return false;
        }

        if (src.x() != 0.0f) {
            if (src.y() != 0.0f) {
                dest.set(-src.y(), src.x(), 0.0f);
            } else {
                dest.set(-src.z(), 0.0f, src.x());
            }
        } else {
            dest.set(1.0f, 0.0f, 0.0f);
        }
        return true;
    }

    /** Returns 1 if the sign of the given argument is positive; -1 if
     negative; 0 if 0. */
    public static int sgn(float f) {
        if (f > 0) {
            return 1;
        } else if (f < 0) {
            return -1;
        }
        return 0;
    }

    public static float clamp(float val, float min, float max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }

    public static double clamp(double val, double min, double max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }

    public static int clamp(int val, int min, int max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }

    public static int log2(int num) {
        int x = 1;
        int l = 0;

        while (x < num) {
            x = x << 1;
            ++l;
        }
        return l;
    }

    public static boolean isPowerOf2(int arg) {
        int numSetBits = 0;

        for (int i = 0; i < 32; i++) {
            if ((arg & 1) != 0) ++numSetBits;
            if (numSetBits > 1) return false;
            arg = arg >>> 1;
        }
        if (numSetBits == 1) return true;
        return false;
    }

    public static boolean isOdd(int num) {
        return ((num % 2) != 0);
    }

    public static boolean isEven(int num) {
        return ((num % 2) == 0);
    }

    public static int power2(int pow) {
        return 1 << pow;
    }
    
    public static double  dec2rad(double x){
        return ((Math.PI / 180.0) * (x));
    }

    public static double  rad2dec(double x){
        return ((180.0 / Math.PI) * (x));
    }

   // public static boolean  isPowerOf2(int x){
   //     return (!((x) & ((x) - 1)) && !!(x));
    //}

    public static float sqr(float x){
        return x*x;
    }

    public static double sqr(double x){
        return x*x;
    }

    public static int sqr(int x){
        return x*x;
    }

    public static float distance(Vec2f p, Vec2f q){
        return (float) Math.sqrt(distanceSqr(p, q));
    }

    public static float distanceSqr(Vec2f p, Vec2f q){
        return  sqr(p.x()-q.x())+sqr(p.y()-q.y());
    }

    public static float distance(Vec3f p, Vec3f q){
        return (float) Math.sqrt(distanceSqr(p, q));
    }

    public static float distanceSqr(Vec3f p, Vec3f q){
        return  sqr(p.x()-q.x())+sqr(p.y()-q.y())+sqr(p.z()-q.z());
    }

    public static double distance(Vec3d p, Vec3d q){
        return Math.sqrt(distanceSqr(p, q));
    }

    public static double distanceSqr(Vec3d p, Vec3d q){
        double x1 = p.x(), x2 = q.x(); 
        double y1 = p.y(), y2 = q.y(); 
        double z1 = p.z(), z2 = q.z(); 
        double dx = x1-x2, dy = y1-y2, dz = z1-z2; 
        return  dx*dx+dy*dy+dz*dz;
    }

    public static double angleDiff(float phi, float theta){

        float thetaDiff = phi - theta;
        if (thetaDiff > Math.PI)  thetaDiff -= TWO_PI;
        else if (thetaDiff < -Math.PI) thetaDiff += TWO_PI;

        return thetaDiff;
    }

    public static double machineEpsilon(){
        double i = 1.0;
        while (true){
            if (i+1==1)
                return i;
            i=i/2;
        }
    }

}
