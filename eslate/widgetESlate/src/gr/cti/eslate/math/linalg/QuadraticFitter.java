package gr.cti.eslate.math.linalg;

import gr.cti.eslate.math.linalg.exceptions.DimensionMismatchException;
import gr.cti.eslate.math.linalg.exceptions.SingularMatrixException;


/** Fits a quadratic polynomial (i.e., a parabola) to a set of
 data. The resulting curve can then be sampled at various points.
 Useful for smoothing data that is well-modeled by a parabola. See
 Strang, _Introduction to Linear Algebra_, for a good reference. */

public class QuadraticFitter {
    // Equation is c + dt + et^2 = b
    private float c;
    private float d;
    private float e;

    public QuadraticFitter() {}

    /** Fits a set of data sampled at the specified times. Must have > 3
     sample points, or throws DimensionMismatchException. */
    public void fit(Vecf times, Vecf samples)
        throws DimensionMismatchException,
            SingularMatrixException {
        if (times.length() != samples.length()) {
            throw new DimensionMismatchException("Vectors must be equal length");
        }
        if (times.length() < 4) {
            throw new DimensionMismatchException("Must have at least 4 sample points");
        }

        /** Use Vandermonde matrices to find the c,d,e,b coefficients in polynomial
         */
        // Fit using least squares: A'Ax = A'b
        // Construct A matrix
        Matrixf a = new Matrixf(times.length(), 3);

        for (int i = 0; i < times.length(); i++) {
            float t = times.get(i);

            a.set(i, 0, 1);
            a.set(i, 1, t);
            a.set(i, 2, t * t);
        }
        Matrixf at = a.transpose();
        Mat3f atai = at.mul(a).toMat3f();

        if (!atai.invert()) {
            System.err.println("A'A: " + atai);
            throw new SingularMatrixException("Unable to invert A'A");
        }
        Vec3f atb = at.mul(samples).toVec3f();
        Vec3f x = new Vec3f();

        atai.xformVec(atb, x);
        // Coefficients are in x vector
        c = x.x();
        d = x.y();
        e = x.z();
    }

    /** Queries the curve resulting from a call to fit(). */
    public float sample(float t) {
        return c + d * t + e * t * t;
    }
}
