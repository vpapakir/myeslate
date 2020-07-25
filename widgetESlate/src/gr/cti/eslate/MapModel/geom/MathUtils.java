package gr.cti.eslate.mapModel.geom;

import java.awt.Point;
import java.awt.geom.IllegalPathStateException;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import sun.awt.geom.Crossings;

/**
 * Mathematical utilities and constants.
 * 
 * @author George Vasiliou
 */
public class MathUtils {
	/**
	 * Computes the angle difference from a1 to a2, how many radians you have to add to a1 to get a2.
	 * 
	 * @param a1
	 *            the first angle.
	 * @param a2
	 *            the second angle.
	 * @return the difference of angles a1 and a2 in radians.
	 */
	public static final double angleDifference(double a1,double a2) {
		double diff=a2 - a1;
		if (diff > PI)
			diff-=TWO_PI;
		else if (diff < -PI)
			diff+=TWO_PI;
		return diff;
	}

	/**
	 * Normalizes the angle, putting it inside [0,2�).
	 * 
	 * @param a
	 *            the angle to normalize.
	 * @return the angle normalized.
	 */
	public static final double normalize(double a) {
		while (a < 0)
			a+=TWO_PI;
		while (a >= TWO_PI)
			a-=TWO_PI;
		return a;
	}
	
	/**
	 * Normalizes the angle in degrees, putting it inside [0,360).
	 * 
	 * @param a
	 *            the angle to normalize.
	 * @return the angle normalized.
	 */
	public static final double normalizeDegrees(double a) {
		while (a < 0)
			a+=360;
		while (a >= 360)
			a-=360;
		return a;
	}
	
	/**
	 * Finds the slope of a segment, relative to the upright, anticlockwise.
	 */
	public static double slope(Point a,Point b) {
		if (a.x == b.x) {
			if (a.y <= b.y)
				return PI2;
			else
				return THREE_PI2;
		}
		if (a.y == b.y) {
			if (a.x >= b.x)
				return PI;
			else
				return 0;
		}
		double w=Math.atan(((float) (a.y - b.y)) / (a.x - b.x));
		if (a.x > b.x)
			w+=PI;
		return w;
	}

	/**
	 * Returns the index of the direction of the point <code>q</code> relative to a vector specified by
	 * <code>p1-p2</code>.
	 * 
	 * @param p1
	 *            the origin point of the vector
	 * @param p2
	 *            the final point of the vector
	 * @param q
	 *            the point to compute the direction to
	 * 
	 * @return 1 if q is counter-clockwise (left) from p1-p2
	 * @return -1 if q is clockwise (right) from p1-p2
	 * @return 0 if q is collinear with p1-p2
	 */
	public static int orientationIndex(Point2D p1,Point2D p2,Point2D q) {
		return orientationIndex(p1.getX(),p1.getY(),p2.getX(),p2.getY(),q.getX(),q.getY());
	}

	/**
	 * Returns the index of the direction of the point <code>q</code> relative to a vector specified by
	 * <code>p1-p2</code>.
	 * 
	 * @param p1
	 *            the origin point of the vector
	 * @param p2
	 *            the final point of the vector
	 * @param q
	 *            the point to compute the direction to
	 * 
	 * @return 1 if q is counter-clockwise (left) from p1-p2
	 * @return -1 if q is clockwise (right) from p1-p2
	 * @return 0 if q is collinear with p1-p2
	 */
	public static int orientationIndex(double p1x,double p1y,double p2x,double p2y,double qx,double qy) {
		// travelling along p1->p2, turn counter clockwise to get to q return 1,
		// travelling along p1->p2, turn clockwise to get to q return -1,
		// p1, p2 and q are colinear return 0.
		double dx1=p2x - p1x;
		double dy1=p2y - p1y;
		double dx2=qx - p2x;
		double dy2=qy - p2y;
		return signOfDet2x2(dx1,dy1,dx2,dy2);
	}

	public static int signOfDet2x2(double x1,double y1,double x2,double y2) {
		// returns -1 if the determinant is negative,
		// returns 1 if the determinant is positive,
		// retunrs 0 if the determinant is null.
		int sign;
		double swap;
		double k;
		long count=0;

		// callCount++; // debugging only

		sign=1;

		/*
		 * testing null entries
		 */
		if ((x1 == 0.0) || (y2 == 0.0)) {
			if ((y1 == 0.0) || (x2 == 0.0)) {
				return 0;
			} else if (y1 > 0) {
				if (x2 > 0) {
					return -sign;
				} else {
					return sign;
				}
			} else {
				if (x2 > 0) {
					return sign;
				} else {
					return -sign;
				}
			}
		}
		if ((y1 == 0.0) || (x2 == 0.0)) {
			if (y2 > 0) {
				if (x1 > 0) {
					return sign;
				} else {
					return -sign;
				}
			} else {
				if (x1 > 0) {
					return -sign;
				} else {
					return sign;
				}
			}
		}

		/*
		 * making y coordinates positive and permuting the entries
		 */
		/*
		 * so that y2 is the biggest one
		 */
		if (0.0 < y1) {
			if (0.0 < y2) {
				if (y1 <= y2) {
					;
				} else {
					sign=-sign;
					swap=x1;
					x1=x2;
					x2=swap;
					swap=y1;
					y1=y2;
					y2=swap;
				}
			} else {
				if (y1 <= -y2) {
					sign=-sign;
					x2=-x2;
					y2=-y2;
				} else {
					swap=x1;
					x1=-x2;
					x2=swap;
					swap=y1;
					y1=-y2;
					y2=swap;
				}
			}
		} else {
			if (0.0 < y2) {
				if (-y1 <= y2) {
					sign=-sign;
					x1=-x1;
					y1=-y1;
				} else {
					swap=-x1;
					x1=x2;
					x2=swap;
					swap=-y1;
					y1=y2;
					y2=swap;
				}
			} else {
				if (y1 >= y2) {
					x1=-x1;
					y1=-y1;
					x2=-x2;
					y2=-y2;
					;
				} else {
					sign=-sign;
					swap=-x1;
					x1=-x2;
					x2=swap;
					swap=-y1;
					y1=-y2;
					y2=swap;
				}
			}
		}

		/*
		 * making x coordinates positive
		 */
		/*
		 * if |x2| < |x1| one can conclude
		 */
		if (0.0 < x1) {
			if (0.0 < x2) {
				if (x1 <= x2) {
					;
				} else {
					return sign;
				}
			} else {
				return sign;
			}
		} else {
			if (0.0 < x2) {
				return -sign;
			} else {
				if (x1 >= x2) {
					sign=-sign;
					x1=-x1;
					x2=-x2;
					;
				} else {
					return -sign;
				}
			}
		}

		/*
		 * all entries strictly positive x1 <= x2 and y1 <= y2
		 */
		while (true) {
			count=count + 1;
			k=Math.floor(x2 / x1);
			x2=x2 - k * x1;
			y2=y2 - k * y1;

			/*
			 * testing if R (new U2) is in U1 rectangle
			 */
			if (y2 < 0.0) {
				return -sign;
			}
			if (y2 > y1) {
				return sign;
			}

			/*
			 * finding R'
			 */
			if (x1 > x2 + x2) {
				if (y1 < y2 + y2) {
					return sign;
				}
			} else {
				if (y1 > y2 + y2) {
					return -sign;
				} else {
					x2=x1 - x2;
					y2=y1 - y2;
					sign=-sign;
				}
			}
			if (y2 == 0.0) {
				if (x2 == 0.0) {
					return 0;
				} else {
					return -sign;
				}
			}
			if (x2 == 0.0) {
				return sign;
			}

			/*
			 * exchange 1 and 2 role.
			 */
			k=Math.floor(x1 / x2);
			x1=x1 - k * x2;
			y1=y1 - k * y2;

			/*
			 * testing if R (new U1) is in U2 rectangle
			 */
			if (y1 < 0.0) {
				return sign;
			}
			if (y1 > y2) {
				return -sign;
			}

			/*
			 * finding R'
			 */
			if (x2 > x1 + x1) {
				if (y2 < y1 + y1) {
					return -sign;
				}
			} else {
				if (y2 > y1 + y1) {
					return sign;
				} else {
					x1=x2 - x1;
					y1=y2 - y1;
					sign=-sign;
				}
			}
			if (y1 == 0.0) {
				if (x1 == 0.0) {
					return 0;
				} else {
					return sign;
				}
			}
			if (x1 == 0.0) {
				return -sign;
			}
		}
	}
	
	/**
	 * Does not work for quad or cubic paths.
	 */
    public static int pointCrossingsForPath(PathIterator pathiterator,double d,double d1) {
		if (pathiterator.isDone())
			return 0;
		double ad[]=new double[6];
		if (pathiterator.currentSegment(ad)!=0)
			throw new IllegalPathStateException("missing initial moveto in path definition, iterator class "+pathiterator.getClass()+" returned "+pathiterator.currentSegment(ad));
		pathiterator.next();
		double d2=ad[0];
		double d3=ad[1];
		double d4=d2;
		double d5=d3;
		int i=0;
		for (;!pathiterator.isDone();pathiterator.next())
			switch (pathiterator.currentSegment(ad)) {
			default:
				break;

			case 0: // '\0'
				if (d5!=d3)
					i+=pointCrossingsForLine(d,d1,d4,d5,d2,d3);
				d2=d4=ad[0];
				d3=d5=ad[1];
				break;

			case 1: // '\001'
				double d6=ad[0];
				double d9=ad[1];
				i+=pointCrossingsForLine(d,d1,d4,d5,d6,d9);
				d4=d6;
				d5=d9;
				break;

			case 2: // '\002'
			case 3: // '\003'
				System.err.println("Unknown case in switch.");
				break;

			case 4: // '\004'
				if (d5!=d3)
					i+=pointCrossingsForLine(d,d1,d4,d5,d2,d3);
				d4=d2;
				d5=d3;
				break;
			}

		if (d5!=d3)
			i+=pointCrossingsForLine(d,d1,d4,d5,d2,d3);
		return i;
	}

	public static int pointCrossingsForLine(double d,double d1,double d2,double d3,double d4,double d5) {
		if (d1<d3&&d1<d5)
			return 0;
		if (d1>=d3&&d1>=d5)
			return 0;
		if (d>=d2&&d>=d4)
			return 0;
		if (d<d2&&d<d4)
			return d3>=d5 ? -1 : 1;
		double d6=d2+((d1-d3)*(d4-d2))/(d5-d3);
		if (d>=d6)
			return 0;
		else
			return d3>=d5 ? -1 : 1;
	}

	public static boolean intersects(PathIterator pi,double x,double y,double w,double h) {
		Crossings c=Crossings.findCrossings(pi,x,y,x + w,y + h);
		return (c == null || !c.isEmpty());
	}

	public static boolean contains(PathIterator pi,double x,double y,double w,double h) {
		Crossings c=Crossings.findCrossings(pi,x,y,x + w,y + h);
		return (c != null && c.covers(y,y + h));
	}
	
	public static final double PI=Math.PI;

	public static final double PI2=Math.PI / 2d;

	public static final double THREE_PI2=3d * Math.PI / 2d;

	public static final double TWO_PI=2d * Math.PI;

	/**
	 * Precalculated natural logarithm of 2.
	 */
	public static double LN_2=Math.log(2);

	/**
	 * Source version information (CVS).
	 */
	public static final String SRC_VERSION_INFO="$Header: /usr/local/cvsroot/MapModel/src/gr/talent/map/model/MathUtils.java,v 1.5 2007/05/15 15:16:39 vasiliou Exp $";
}
