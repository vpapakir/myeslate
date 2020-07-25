package gr.cti.eslate.math.linalg;

import gr.cti.eslate.math.linalg.exceptions.DimensionMismatchException;

/** Arbitrary-length double-precision vector class.
 *
 */

public class Vecd {
    private double[] data;

    public Vecd(int n) {
        data = new double[n];
    }

    public Vecd(Vecd arg) {
        data = new double[arg.data.length];
        System.arraycopy(arg.data, 0, data, 0, data.length);
    }

    public int length() {
        return data.length;
    }

    public double get(int i) {
        return data[i];
    }

    public void set(int i, double val) {
        data[i] = val;
    }

    public Vec3d toVec3d() throws DimensionMismatchException {
        if (length() != 3)
            throw new DimensionMismatchException();
        Vec3d out = new Vec3d();

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
