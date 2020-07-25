package gr.cti.eslate.math.linalg;


/** Represents a rotation with single-precision components */

public class Rot {
    // Representation is a quaternion. Element 0 is the scalar part (=
    // cos(theta/2)), elements 1..3 the imaginary/"vector" part (=
    // sin(theta/2) * axis).
    private double q0;
    private double q1;
    private double q2;
    private double q3;

    /** Default constructor initializes to the identity quaternion */
    public Rot() {
        init();
    }

    public Rot(Rot arg) {
        set(arg);
    }

    /** Axis does not need to be normalized but must not be the zero
     vector. Angle is in radians. */
    public Rot(Vec3d axis, double angle) {
        set(axis, angle);
    }

    /** Re-initialize this quaternion to be the identity quaternion "e"
     (i.e., no rotation) */
    public void init() {
        q0 = 1;
        q1 = q2 = q3 = 0;
    }

    /** Test for "approximate equality" -- performs componentwise test
     to see whether difference between all components is less than
     epsilon. */
    public boolean withinEpsilon(Rot arg, double epsilon) {
        return ((Math.abs(q0 - arg.q0) < epsilon) &&
                (Math.abs(q1 - arg.q1) < epsilon) &&
                (Math.abs(q2 - arg.q2) < epsilon) &&
                (Math.abs(q3 - arg.q3) < epsilon));
    }

    /** Axis does not need to be normalized but must not be the zero
     vector. Angle is in radians. */
    public void set(Vec3d axis, double angle) {
        double halfTheta = angle / 2.0;

        q0 = Math.cos(halfTheta);
        double sinHalfTheta = Math.sin(halfTheta);
        Vec3d realAxis = new Vec3d(axis);

        realAxis.normalize();
        q1 = realAxis.x() * sinHalfTheta;
        q2 = realAxis.y() * sinHalfTheta;
        q3 = realAxis.z() * sinHalfTheta;
    }
    
    public void set(double q0, double q1, double q2, double q3){
        this.q0 = q0;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
    }

    public void set(Rot arg) {
        q0 = arg.q0;
        q1 = arg.q1;
        q2 = arg.q2;
        q3 = arg.q3;
    }

    /** Returns angle (in radians) and mutates the given vector to be
     the axis. */
    public double get(Vec3d axis) {
        // FIXME: Is this numerically stable? Is there a better way to
        // extract the angle from a quaternion?
        // NOTE: remove (float) to illustrate compiler bug
        double retval = (2.0 * Math.acos(q0));

        axis.set(q1, q2, q3);
        double len = axis.length();

        if (len == 0.0) {
            axis.set(0, 0, 1);
        } else {
            axis.scale(1.0 / len);
        }
        return retval;
    }

    /** Returns inverse of this rotation; creates new rotation */
    public Rot inverse() {
        Rot tmp = new Rot(this);

        tmp.invert();
        return tmp;
    }

    /** Mutate this quaternion to be its inverse. This is equivalent to
     the conjugate of the quaternion. */
    public void invert() {
        q1 = -q1;
        q2 = -q2;
        q3 = -q3;
    }

    /** Length of this quaternion in four-space */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /** This dotted with this */
    public double lengthSquared() {
        return (q0 * q0 +
                q1 * q1 +
                q2 * q2 +
                q3 * q3);
    }

    /** Make this quaternion a unit quaternion again. If you are
     composing dozens of quaternions you probably should call this
     periodically to ensure that you have a valid rotation. */
    public void normalize() {
        double len = length();

        q0 /= len;
        q1 /= len;
        q2 /= len;
        q3 /= len;
    }

    /** Returns this * b, in that order; creates new rotation */
    public Rot times(Rot b) {
        Rot tmp = new Rot();

        tmp.mul(this, b);
        return tmp;
    }

    /** Compose two rotations: this = A * B in that order. NOTE that
     because we assume a column vector representation that this
     implies that a vector rotated by the cumulative rotation will be
     rotated first by B, then A. NOTE: "this" must be different than
     both a and b. */
    public void mul(Rot a, Rot b) {
        q0 = (a.q0 * b.q0 - a.q1 * b.q1 -
                    a.q2 * b.q2 - a.q3 * b.q3);
        q1 = (a.q0 * b.q1 + a.q1 * b.q0 +
                    a.q2 * b.q3 - a.q3 * b.q2);
        q2 = (a.q0 * b.q2 + a.q2 * b.q0 -
                    a.q1 * b.q3 + a.q3 * b.q1);
        q3 = (a.q0 * b.q3 + a.q3 * b.q0 +
                    a.q1 * b.q2 - a.q2 * b.q1);
    }

    /** Turns this rotation into a 3x3 rotation matrix. NOTE: only
     mutates the upper-left 3x3 of the passed Mat4f. Implementation
     from B. K. P. Horn's _Robot Vision_ textbook. */
    public void toMatrix(Mat4d mat) {
        double q00 = q0 * q0;
        double q11 = q1 * q1;
        double q22 = q2 * q2;
        double q33 = q3 * q3;

        // Diagonal elements
        mat.set(0, 0, q00 + q11 - q22 - q33);
        mat.set(1, 1, q00 - q11 + q22 - q33);
        mat.set(2, 2, q00 - q11 - q22 + q33);
        // 0,1 and 1,0 elements
        double q03 = q0 * q3;
        double q12 = q1 * q2;

        mat.set(0, 1, 2.0f * (q12 - q03));
        mat.set(1, 0, 2.0f * (q03 + q12));
        // 0,2 and 2,0 elements
        double q02 = q0 * q2;
        double q13 = q1 * q3;

        mat.set(0, 2, 2.0f * (q02 + q13));
        mat.set(2, 0, 2.0f * (q13 - q02));
        // 1,2 and 2,1 elements
        double q01 = q0 * q1;
        double q23 = q2 * q3;

        mat.set(1, 2, 2.0f * (q23 - q01));
        mat.set(2, 1, 2.0f * (q01 + q23));
    }

    /** Turns the upper left 3x3 of the passed matrix into a rotation.
     Implementation from Watt and Watt, _Advanced Animation and
     Rendering Techniques_.
     @see Mat4f getRotation */
    public void fromMatrix(Mat4d mat) {
        // FIXME: Should reimplement to follow Horn's advice of using
        // eigenvector decomposition to handle roundoff error in given
        // matrix.

        double tr, s;
        int i, j, k;

        tr = mat.get(0, 0) + mat.get(1, 1) + mat.get(2, 2);
        if (tr > 0.0) {
            s = Math.sqrt(tr + 1.0f);
            q0 = s * 0.5f;
            s = 0.5f / s;
            q1 = (mat.get(2, 1) - mat.get(1, 2)) * s;
            q2 = (mat.get(0, 2) - mat.get(2, 0)) * s;
            q3 = (mat.get(1, 0) - mat.get(0, 1)) * s;
        } else {
            i = 0;
            if (mat.get(1, 1) > mat.get(0, 0))
                i = 1;
            if (mat.get(2, 2) > mat.get(i, i))
                i = 2;
            j = (i + 1) % 3;
            k = (j + 1) % 3;
            s = Math.sqrt((mat.get(i, i) - (mat.get(j, j) + mat.get(k, k))) + 1.0f);
            setQ(i + 1, s * 0.5f);
            s = 0.5f / s;
            q0 = (mat.get(k, j) - mat.get(j, k)) * s;
            setQ(j + 1, (mat.get(j, i) + mat.get(i, j)) * s);
            setQ(k + 1, (mat.get(k, i) + mat.get(i, k)) * s);
        }
    }

    /** Rotate a vector by this quaternion. Implementation is from
     Horn's _Robot Vision_. NOTE: src and dest must be different
     vectors. */
    public void rotateVector(Vec3d src, Vec3d dest) {
        // NOTE: uncomment these to illustrate compiler bug with line numbers
        //    Vec3f qCrossX = new Vec3f();
        //    Vec3f qCrossXCrossQ = new Vec3f();
        Vec3d qVec = new Vec3d(q1, q2, q3);

        Vec3d qCrossX = qVec.cross(src);
        Vec3d qCrossXCrossQ = qCrossX.cross(qVec);

        qCrossX.scale(2.0f * q0);
        qCrossXCrossQ.scale(-2.0f);
        dest.add(src, qCrossX);
        dest.add(dest, qCrossXCrossQ);
    }

    /** Rotate a vector by this quaternion, returning newly-allocated result. */
    public Vec3d rotateVector(Vec3d src) {
        Vec3d tmp = new Vec3d();

        rotateVector(src, tmp);
        return tmp;
    }

    public String toString() {
        return "(" + q0 + ", " + q1 + ", " + q2 + ", " + q3 + ")";
    }

    private void setQ(int i, double val) {
        switch (i) {
        case 0:
            q0 = val;
            break;

        case 1:
            q1 = val;
            break;

        case 2:
            q2 = val;
            break;

        case 3:
            q3 = val;
            break;

        default:
            throw new IndexOutOfBoundsException();
        }
    }

    /* Algorithm followed here is described in the "Matrix and Quaternion FAQ",
    *  section Q37. http://www.cs.ualberta.ca/~andreas/math/matrfaq_latest.html
    */

    public Vec3d getDegreeAngles(){
        double angleX,angleY,angleZ;
        double x,y,C;
        double RADIANS = 360/Math.PI/2;
        Mat4d mat = new Mat4d();
        toMatrix(mat);
        angleY = Math.asin(mat.get(0,2));        /* Calculate Y-axis angle */
        C = Math.cos(angleY);

        if (Math.abs(C) > 0.005){             /* Gimball lock? */
            x =  mat.get(2,2);           /* No, so get X-axis angle */
            y = -mat.get(1,2);

            angleX  = Math.atan2( y, x );
            x =  mat.get(0,0);            /* Get Z-axis angle */
            y = -mat.get(0,1);

            angleZ  = Math.atan2( y, x );
        }else{                                 /* Gimball lock has occurred */
            angleX  = 0;                      /* Set X-axis angle to zero */
            x = mat.get(1,1);                 /* And calculate Z-axis angle */
            y = mat.get(1,0);
            angleZ  = -Math.atan2( y, x );
            //System.out.println("GIMBALL LOCK");
        }
        //if (angleX < 0) angleX += 360;
        //if (angleY < 0) angleY += 360;
        //if (angleZ < 0) angleZ += 360;



        Vec3d vec = new Vec3d(angleX,angleY,angleZ);
        System.out.println(vec.x()*RADIANS+", "+ vec.y()*RADIANS+", "+ vec.z()*RADIANS);
        return vec;
    }
    
    /*
     * Algorithm and code from Martim Baker. 
     */
    
    public Vec3d getAnglesBaker() {
        double sqw = q0*q0;
        double sqx = q1*q1;
        double sqy = q2*q2;
        double sqz = q3*q3;
    	double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
    	double test = q1*q2 + q3*q0;
    	double heading, attitude, bank;
    	if (test > 0.499*unit) { // singularity at north pole
    		heading = 2 * Math.atan2(q1,q0);
    		attitude = Math.PI/2;
    		bank = 0;
    		return new Vec3d(attitude, heading, bank);
    	}
    	if (test < -0.499*unit) { // singularity at south pole
    		heading = -2 * Math.atan2(q1,q0);
    		attitude = -Math.PI/2;
    		bank = 0;
    		return new Vec3d(attitude, heading, bank);
    	}
        heading = Math.atan2(2*q2*q0-2*q1*q3 , sqx - sqy - sqz + sqw);
    	attitude = Math.asin(2*test/unit);
    	bank = Math.atan2(2*q1*q0-2*q2*q3 , -sqx + sqy - sqz + sqw);
    	return new Vec3d(attitude, heading, bank);
    }



    /* Algorithm followed here is described in the "3D Game Engine Design",
    *  David Eberly, pages 20-21 (Euler Angles) (and its the right one)!
    */

    public Vec3d getAngles(){
        double angleX,angleY,angleZ;
        double x,y,C;
        double RADIANS = 360/Math.PI/2;
        Mat4d mat = new Mat4d();
        toMatrix(mat);
        angleX = Math.asin(-mat.get(1,2));
        if (angleX < Math.PI/2){
            if (angleX > -Math.PI/2){
                angleY = Math.atan2(mat.get(0,2), mat.get(2,2));
                angleZ = Math.atan2(mat.get(1,0), mat.get(1,1));
            }else{
                angleY = Math.atan2(mat.get(0,1), mat.get(0,0));
                angleZ = 0;
            }
        }else{
            angleY = Math.atan2(mat.get(0,1), mat.get(0,0));
            angleZ = 0;
        }
        Vec3d vec = new Vec3d(angleX,angleY,angleZ);
        return vec;
    }

    /* Algorithm followed here is one that claims to avoid Gimball Lock
    /* Eric Reiss is the author , (http://jamaica.ee.pitt.edu/Eric/java3d/rotation.htm)
     */

   //FIXME  ... insert it when have some time

    public Vec3d getAnglesFromQuaternion(){
        normalize();
        double sqw = q3*q3;
        double sqx = q0*q0;
        double sqy = q1*q1;
        double sqz = q2*q2;
        double heading = Math.atan2(2.0 * (q0*q1 + q2*q3),(sqx - sqy - sqz + sqw));
        double bank = Math.atan2(2.0 * (q1*q2 + q0*q3),(-sqx - sqy + sqz + sqw));
        double attitude = Math.asin(-2.0 * (q0*q2 - q1*q3));
        return new Vec3d(attitude,heading,bank);
    }

    public Vec3d getAngles2(){
       double angleX,angleY,angleZ;
       double x,y,C;
       Mat4d mat = new Mat4d();
       toMatrix(mat);
       double RADIANS = 360/Math.PI/2;
       angleY = C = -Math.asin( mat.get(0,2));        /* Calculate Y-axis angle */
       C           =  Math.cos( angleY );
       //angleY    *= RADIANS;

       if ( Math.abs( C ) > 0.005 )             /* Gimball lock? */
         {
         x      =  mat.get(2,2) / C;           /* No, so get X-axis angle */
         y      = -mat.get(1,2)  / C;

         angleX  = Math.atan2(y, x ) ;//* RADIANS;

         x      =  mat.get(0,0) / C;            /* Get Z-axis angle */
         y      = -mat.get(0,1) / C;

         angleZ  = Math.atan2( y, x );// * RADIANS;
         }
       else                                 /* Gimball lock has occurred */
         {
         angleX  = 0;                      /* Set X-axis angle to zero */

         x      = mat.get(1,1);                 /* And calculate Z-axis angle */
         y      = mat.get(1,0);

         angleZ  = Math.atan2(y, x);// * RADIANS;
         }

       angleX = MathUtils.clamp( angleX, 0, Math.PI );  /* Clamp all angles to range */
       angleY = MathUtils.clamp( angleY, 0, Math.PI );
       angleZ = MathUtils.clamp( angleZ, 0, Math.PI );
       return new Vec3d(angleY, angleX, angleZ);

   }


}

