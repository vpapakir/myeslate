package gr.cti.eslate.math.linalg;

/**
 * Created by A.Mantes at 5 Οκτ 2004, 1:06:06 μμ
 */
/** 3-element single-precision vector */

public class Vec3i {

    private int x;
    private int y;
    private int z;

    public Vec3i() {}

    public Vec3i(Vec3i arg) {
        set(arg);
    }

    public Vec3i(int x, int y, int z) {
        set(x, y, z);
    }

    public Vec3i copy() {
        return new Vec3i(this);
    }

    /** Convert to double-precision */
    public Vec3d toDouble() {
        return new Vec3d(x, y, z);
    }

    public void set(Vec3i arg) {
        set(arg.x, arg.y, arg.z);
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** Sets the ith component, 0 <= i < 3 */
    public void set(int i, int val) {
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

        default:
            throw new IndexOutOfBoundsException();
        }
    }

    /** Gets the ith component, 0 <= i < 3 */
    public int get(int i) {
        switch (i) {
        case 0:
            return x;

        case 1:
            return y;

        case 2:
            return z;

        default:
            throw new IndexOutOfBoundsException();
        }
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int dot(Vec3i arg) {
        return x * arg.x + y * arg.y + z * arg.z;
    }

    public int length() {
        return (int) Math.sqrt(lengthSquared());
    }

    public int lengthSquared() {
        return this.dot(this);
    }

    public void normalize() {
        int len = length();

        if (len == 0.0f) return;
        scale(1 / len);
    }

    /** Returns this * val; creates new vector */
    public Vec3i times(int val) {
        Vec3i tmp = new Vec3i(this);

        tmp.scale(val);
        return tmp;
    }

    /** this = this * val */
    public void scale(int val) {
        x *= val;
        y *= val;
        z *= val;
    }

    /** Returns this + arg; creates new vector */
    public Vec3i plus(Vec3i arg) {
        Vec3i tmp = new Vec3i();

        tmp.add(this, arg);
        return tmp;
    }

    /** this = this + b */
    public void add(Vec3i b) {
        add(this, b);
    }

    /** this = a + b */
    public void add(Vec3i a, Vec3i b) {
        x = a.x + b.x;
        y = a.y + b.y;
        z = a.z + b.z;
    }

    /** Returns this + s * arg; creates new vector */
    public Vec3i addScaled(int s, Vec3i arg) {
        Vec3i tmp = new Vec3i();

        tmp.addScaled(this, s, arg);
        return tmp;
    }

    /** this = a + s * b */
    public void addScaled(Vec3i a, int s, Vec3i b) {
        x = a.x + s * b.x;
        y = a.y + s * b.y;
        z = a.z + s * b.z;
    }

    /** Returns this - arg; creates new vector */
    public Vec3i minus(Vec3i arg) {
        Vec3i tmp = new Vec3i();

        tmp.sub(this, arg);
        return tmp;
    }

    /** this = this - b */
    public void sub(Vec3i b) {
        sub(this, b);
    }

    /** this = a - b */
    public void sub(Vec3i a, Vec3i b) {
        x = a.x - b.x;
        y = a.y - b.y;
        z = a.z - b.z;
    }

    /** Returns this cross arg; creates new vector */
    public Vec3i cross(Vec3i arg) {
        Vec3i tmp = new Vec3i();

        tmp.cross(this, arg);
        return tmp;
    }

    /** this = a cross b. NOTE: "this" must be a different vector than
     both a and b. */
    public void cross(Vec3i a, Vec3i b) {
        x = a.y * b.z - a.z * b.y;
        y = a.z * b.x - a.x * b.z;
        z = a.x * b.y - a.y * b.x;
    }

    public Vecf toVecf() {
        Vecf out = new Vecf(3);

        for (int i = 0; i < 3; i++) {
            out.set(i, get(i));
        }
        return out;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
