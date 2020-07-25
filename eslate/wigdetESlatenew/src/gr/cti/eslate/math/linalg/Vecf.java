package gr.cti.eslate.math.linalg;


import gr.cti.eslate.math.linalg.exceptions.DimensionMismatchException;


/** Arbitrary-length single-precision vector class.
 *
 */

public class Vecf {
    private float[] data;

    public Vecf(int n) {
        data = new float[n];
    }

    public Vecf(Vecf arg) {
        data = new float[arg.data.length];
        System.arraycopy(arg.data, 0, data, 0, data.length);
    }

    public int length() {
        return data.length;
    }

    public float get(int i) {
        return data[i];
    }

    public void set(int i, float val) {
        data[i] = val;
    }

    public Vec2f toVec2f() throws DimensionMismatchException {
        if (length() != 2)
            throw new DimensionMismatchException();
        Vec2f out = new Vec2f();

        for (int i = 0; i < 2; i++) {
            out.set(i, get(i));
        }
        return out;
    }

    public Vec3f toVec3f() throws DimensionMismatchException {
        if (length() != 3)
            throw new DimensionMismatchException();
        Vec3f out = new Vec3f();

        for (int i = 0; i < 3; i++) {
            out.set(i, get(i));
        }
        return out;
    }

    public Veci toInt() {
        Veci out = new Veci(length());

        for (int i = 0; i < length(); i++) {
            out.set(i, (int) get(i));
        }
        return out;
    }
}
