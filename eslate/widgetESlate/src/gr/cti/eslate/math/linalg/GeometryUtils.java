/*
 * Created on 24 Ìבס 2005
 *
 */
package gr.cti.eslate.math.linalg;

/**
 * @author mantesat
 *
 */
public class GeometryUtils {
    
    // Code which computes the distance of a point from a triangle in 3d space. Partitioning any polyhedron into a set
    // of triangles, one can easily use this code to find distance of a point to the polyhedron. Check theory for
    // partitioning S-T planes around triangle projections on x-y, x-z, y-z planes on Eberly's book/site.
    		    
    public static double distanceFromTriangle(Vec3d position, Vec3d a, Vec3d b, Vec3d c){
        Vec3d kDiff = a.minus(position);
        Vec3d edge0 = b.minus(a);
        Vec3d edge1 = c.minus(a);
        double fA00 = edge0.lengthSquared();
        double fA01 = edge0.dot(edge1);
        double fA11 = edge1.lengthSquared();
        double fB0 = kDiff.dot(edge0);
        double fB1 = kDiff.dot(edge1);
        double fC = kDiff.lengthSquared();
        double det = Math.abs(fA00*fA11-fA01*fA01);
        double fS = fA01*fB1-fA11*fB0;
        double fT = fA01*fB0-fA00*fB1;
        double sqrDistance;

        if ( fS + fT <= det ){
            if ( fS < 0.0 ){
                if ( fT < 0.0 ){  // region 4                
                    if ( fB0 < 0.0 ){
                        fT = 0.0;
                        if ( -fB0 >= fA00 ){
                            fS = 1.0;
                            sqrDistance = fA00+(2.0)*fB0+fC;
                        }else{
                            fS = -fB0/fA00;
                            sqrDistance = fB0*fS+fC;
                        }
                    }else{
                        fS = (double)0.0;
                        if ( fB1 >= (double)0.0 ){
                            fT = (double)0.0;
                            sqrDistance = fC;
                        }else if ( -fB1 >= fA11 ){
                            fT = (double)1.0;
                            sqrDistance = fA11+((double)2.0)*fB1+fC;
                        }else{
                            fT = -fB1/fA11;
                            sqrDistance = fB1*fT+fC;
                        }
                    }
                }else{  // region 3
                    fS = (double)0.0;
                    if ( fB1 >= (double)0.0 ){
                        fT = (double)0.0;
                        sqrDistance = fC;
                    }else if ( -fB1 >= fA11 ){
                        fT = (double)1.0;
                        sqrDistance = fA11+((double)2.0)*fB1+fC;
                    }else{
                        fT = -fB1/fA11;
                        sqrDistance = fB1*fT+fC;
                    }
                }
            }else if ( fT < (double)0.0 ){  // region 5
                fT = (double)0.0;
                if ( fB0 >= (double)0.0 ){
                    fS = (double)0.0;
                    sqrDistance = fC;
                }else if ( -fB0 >= fA00 ){
                    fS = (double)1.0;
                    sqrDistance = fA00+((double)2.0)*fB0+fC;
                }else{
                    fS = -fB0/fA00;
                    sqrDistance = fB0*fS+fC;
                }
            }else{  // region 0
                // minimum at interior point
                double fInvDet = ((double)1.0)/det;
                fS *= fInvDet;
                fT *= fInvDet;
                sqrDistance = fS*(fA00*fS+fA01*fT+((double)2.0)*fB0) +
                    fT*(fA01*fS+fA11*fT+((double)2.0)*fB1)+fC;
            }
        }else{
            double fTmp0, fTmp1, fNumer, fDenom;

            if ( fS < 0.0 ){  // region 2
                fTmp0 = fA01 + fB0;
                fTmp1 = fA11 + fB1;
                if ( fTmp1 > fTmp0 ){
                    fNumer = fTmp1 - fTmp0;
                    fDenom = fA00-2.0f*fA01+fA11;
                    if ( fNumer >= fDenom ){
                        fS = 1.0;
                        fT = 0.0;
                        sqrDistance = fA00+2.0*fB0+fC;
                    }else{
                        fS = fNumer/fDenom;
                        fT = 1.0 - fS;
                        sqrDistance = fS*(fA00*fS+fA01*fT+2.0f*fB0) +
                            fT*(fA01*fS+fA11*fT+2.0*fB1)+fC;
                    }
                }else{
                    fS = 0.0;
                    if ( fTmp1 <= 0.0 ){
                        fT = 1.0;
                        sqrDistance = fA11+2.0*fB1+fC;
                    }else if ( fB1 >= 0.0 ){
                        fT = 0.0;
                        sqrDistance = fC;
                    }else{
                        fT = -fB1/fA11;
                        sqrDistance = fB1*fT+fC;
                    }
                }
            }
            else if ( fT < 0.0 ){  // region 6
                fTmp0 = fA01 + fB1;
                fTmp1 = fA00 + fB0;
                if ( fTmp1 > fTmp0 ){
                    fNumer = fTmp1 - fTmp0;
                    fDenom = fA00-2.0*fA01+fA11;
                    if ( fNumer >= fDenom ){
                        fT = 1.0;
                        fS = 0.0;
                        sqrDistance = fA11+2.0*fB1+fC;
                    }else{
                        fT = fNumer/fDenom;
                        fS = 1.0 - fT;
                        sqrDistance = fS*(fA00*fS+fA01*fT+2.0*fB0) +
                            fT*(fA01*fS+fA11*fT+2.0*fB1)+fC;
                    }
                }else{
                    fT = 0.0;
                    if ( fTmp1 <= 0.0 ){
                        fS = 1.0;
                        sqrDistance = fA00+2.0*fB0+fC;
                    }else if ( fB0 >= 0.0 ){
                        fS = 0.0;
                        sqrDistance = fC;
                    }else{
                        fS = -fB0/fA00;
                        sqrDistance = fB0*fS+fC;
                    }
                }
            }else{  // region 1
                fNumer = fA11 + fB1 - fA01 - fB0;
                if ( fNumer <= 0.0 ){
                    fS = 0.0;
                    fT = 1.0;
                    sqrDistance = fA11+2.0*fB1+fC;
                }else{
                    fDenom = fA00-2.0f*fA01+fA11;
                    if ( fNumer >= fDenom ){
                        fS = 1.0;
                        fT = 0.0;
                        sqrDistance = fA00+2.0*fB0+fC;
                    }else{
                        fS = fNumer/fDenom;
                        fT = 1.0 - fS;
                        sqrDistance = fS*(fA00*fS+fA01*fT+2.0*fB0) +
                            fT*(fA01*fS+fA11*fT+2.0*fB1)+fC;
                    }
                }
            }
        }

        // account for numerical round-off error
        if ( sqrDistance < 0.0 )
            sqrDistance = 0.0;

        //Vec3d closestPoint0 = position;
        //Vec3d closestPoint1 = a.add(edge0.scale(fS)).add(edge1.scale(fT));

        return Math.sqrt(sqrDistance);
    }

}
