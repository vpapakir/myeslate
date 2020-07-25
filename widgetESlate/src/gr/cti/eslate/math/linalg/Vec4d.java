package gr.cti.eslate.math.linalg;


/** 4-element single-precision vector */

public class Vec4d {
    private double x;
    private double y;
    private double z;
    private double w;

    public Vec4d() {}

    public Vec4d(Vec4d arg) {
        set(arg);
    }

    public Vec4d(double x, double y, double z, double w) {
        set(x, y, z, w);
    }

    public Vec4d copy() {
        return new Vec4d(this);
    }

    public void set(Vec4d arg) {
        set(arg.x, arg.y, arg.z, arg.w);
    }

    public void set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /** Sets the ith component, 0 <= i < 4 */
    public void set(int i, double val) {
        switch (i) {
        case 0:
            x = val;
            break;

        case 1:
            y = val;
            break;

        case 2:
            z = val;
            break;

        case 3:
            w = val;
            break;

        default:
            throw new IndexOutOfBoundsException();
        }
    }

    /** Gets the ith component, 0 <= i < 4 */
    public double get(int i) {
        switch (i) {
        case 0:
            return x;

        case 1:
            return y;

        case 2:
            return z;

        case 3:
            return w;

        default:
            throw new IndexOutOfBoundsException();
        }
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public double w() {
        return w;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double dot(Vec4d arg) {
        return x * arg.x + y * arg.y + z * arg.z + w * arg.w;
    }

    public double length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return this.dot(this);
    }

    public void normalize() {
        double len = length();

        if (len == 0.0) return;
        scale(1.0 / len);
    }

    /** Returns this * val; creates new vector */
    public Vec4d times(double val) {
        Vec4d tmp = new Vec4d(this);

        tmp.scale(val);
        return tmp;
    }

    /** this = this * val */
    public void scale(double val) {
        x *= val;
        y *= val;
        z *= val;
        w *= val;
    }

    /** Returns this + arg; creates new vector */
    public Vec4d plus(Vec4d arg) {
        Vec4d tmp = new Vec4d();

        tmp.add(this, arg);
        return tmp;
    }

    /** this = this + b */
    public void add(Vec4d b) {
        add(this, b);
    }

    /** this = a + b */
    public void add(Vec4d a, Vec4d b) {
        x = a.x + b.x;
        y = a.y + b.y;
        z = a.z + b.z;
        w = a.w + b.w;
    }

    /** Returns this + s * arg; creates new vector */
    public Vec4d addScaled(float s, Vec4d arg) {
        Vec4d tmp = new Vec4d();

        tmp.addScaled(this, s, arg);
        return tmp;
    }

    /** this = a + s * b */
    public void addScaled(Vec4d a, double s, Vec4d b) {
        x = a.x + s * b.x;
        y = a.y + s * b.y;
        z = a.z + s * b.z;
        w = a.w + s * b.w;
    }

    /** Returns this - arg; creates new vector */
    public Vec4d minus(Vec4d arg) {
        Vec4d tmp = new Vec4d();

        tmp.sub(this, arg);
        return tmp;
    }

    /** this = this - b */
    public void sub(Vec4d b) {
        sub(this, b);
    }

    /** this = a - b */
    public void sub(Vec4d a, Vec4d b) {
        x = a.x - b.x;
        y = a.y - b.y;
        z = a.z - b.z;
        w = a.w - b.w;
    }

    public Vecf toVecf() {
        Vecf out = new Vecf(4);

        for (int i = 0; i < 4; i++) {
            out.set(i, (float) get(i));
        }
        return out;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", "+w+")";
    }
}
