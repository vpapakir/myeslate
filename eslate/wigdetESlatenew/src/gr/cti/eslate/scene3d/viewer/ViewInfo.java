
/*
 * Created on 13 Δεκ 2005
 *
 */
package gr.cti.eslate.scene3d.viewer;

//~--- non-JDK imports --------------------------------------------------------

import gr.cti.eslate.math.linalg.*;
//~--- JDK imports ------------------------------------------------------------

import java.awt.geom.Rectangle2D;

import javax.media.opengl.*;

//~--- classes ----------------------------------------------------------------

/**
 * Created by A.Mantes at 27 Αυγ 2003, 1:15:02 μμ
 *
 * NOTE: ViewInfo is a class that should be in the viewer, not in the
 * renderer of a 3d model. The renderer should use it directly from the
 * viewer, just as it does with the camera. Camera should actually provide
 * viewinfo for renderers, since viewinfo is only related to view frustum
 * matters. However, there are cases that the frustum should change from the
 * renderer...
 */
public class ViewInfo {
    Vec4f[]         viewplanes = new Vec4f[6];    /* view frustum plane equations */
    private float[] proj       = new float[16];
    private float[] modl       = new float[16];
    private float[] clip       = new float[16];

    //~--- constructors -------------------------------------------------------

    public ViewInfo() {}

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param gl
     */
    public void calculateViewplanes(GL gl) {
        calculateViewplanes(gl, 1f, 1f, 1f, 1f);
    }

    /**
     * Method description
     *
     *
     * @param gl
     * @param offsetLeft
     * @param offsetRight
     * @param offsetTop
     * @param offsetBottom
     */
    public void calculateViewplanes(GL gl, float offsetLeft,
                                    float offsetRight, float offsetTop,
                                    float offsetBottom) {

        /**
         * FIXME : this can definately be done a lot faster, although it's
         * not too much since it occurs once in a frame
         */
        float t;

        /* Get the current PROJECTION matrix from OpenGL */
        gl.glGetFloatv(GL.GL_PROJECTION_MATRIX, proj, 0);

        /* Get the current MODELVIEW matrix from OpenGL */
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, modl, 0);

        /* Combine the two matrices (multiply projection by modelview) */
        clip[0] = modl[0] * proj[0] + modl[1] * proj[4] + modl[2] * proj[8]
                  + modl[3] * proj[12];
        clip[1] = modl[0] * proj[1] + modl[1] * proj[5] + modl[2] * proj[9]
                  + modl[3] * proj[13];
        clip[2] = modl[0] * proj[2] + modl[1] * proj[6] + modl[2] * proj[10]
                  + modl[3] * proj[14];
        clip[3] = modl[0] * proj[3] + modl[1] * proj[7] + modl[2] * proj[11]
                  + modl[3] * proj[15];
        clip[4] = modl[4] * proj[0] + modl[5] * proj[4] + modl[6] * proj[8]
                  + modl[7] * proj[12];
        clip[5] = modl[4] * proj[1] + modl[5] * proj[5] + modl[6] * proj[9]
                  + modl[7] * proj[13];
        clip[6] = modl[4] * proj[2] + modl[5] * proj[6] + modl[6] * proj[10]
                  + modl[7] * proj[14];
        clip[7] = modl[4] * proj[3] + modl[5] * proj[7] + modl[6] * proj[11]
                  + modl[7] * proj[15];
        clip[8] = modl[8] * proj[0] + modl[9] * proj[4] + modl[10] * proj[8]
                  + modl[11] * proj[12];
        clip[9] = modl[8] * proj[1] + modl[9] * proj[5] + modl[10] * proj[9]
                  + modl[11] * proj[13];
        clip[10] = modl[8] * proj[2] + modl[9] * proj[6] + modl[10] * proj[10]
                   + modl[11] * proj[14];
        clip[11] = modl[8] * proj[3] + modl[9] * proj[7] + modl[10] * proj[11]
                   + modl[11] * proj[15];
        clip[12] = modl[12] * proj[0] + modl[13] * proj[4]
                   + modl[14] * proj[8] + modl[15] * proj[12];
        clip[13] = modl[12] * proj[1] + modl[13] * proj[5]
                   + modl[14] * proj[9] + modl[15] * proj[13];
        clip[14] = modl[12] * proj[2] + modl[13] * proj[6]
                   + modl[14] * proj[10] + modl[15] * proj[14];
        clip[15] = modl[12] * proj[3] + modl[13] * proj[7]
                   + modl[14] * proj[11] + modl[15] * proj[15];

        /* Extract the numbers for the RIGHT plane */
        viewplanes[0] = new Vec4f();
        viewplanes[0].set(0, clip[3] - clip[0]);
        viewplanes[0].set(1, clip[7] - clip[4]);
        viewplanes[0].set(2, clip[11] - clip[8]);
        viewplanes[0].set(3, clip[15] - clip[12]);

        /* Normalize the result */
        t = (float) Math.sqrt(viewplanes[0].get(0) * viewplanes[0].get(0)
                              + viewplanes[0].get(1) * viewplanes[0].get(1)
                              + viewplanes[0].get(2) * viewplanes[0].get(2));
        viewplanes[0].scale(1 / t);
        viewplanes[0].set(3, viewplanes[0].get(3) * offsetLeft);

        /* Extract the numbers for the LEFT plane */
        viewplanes[1] = new Vec4f();
        viewplanes[1].set(0, clip[3] + clip[0]);
        viewplanes[1].set(1, clip[7] + clip[4]);
        viewplanes[1].set(2, clip[11] + clip[8]);
        viewplanes[1].set(3, clip[15] + clip[12]);

        /* Normalize the result */
        t = (float) Math.sqrt(viewplanes[1].get(0) * viewplanes[1].get(0)
                              + viewplanes[1].get(1) * viewplanes[1].get(1)
                              + viewplanes[1].get(2) * viewplanes[1].get(2));
        viewplanes[1].scale(1 / t);
        viewplanes[1].set(3, viewplanes[1].get(3) * offsetRight);

        /* Extract the BOTTOM plane */
        viewplanes[2] = new Vec4f();
        viewplanes[2].set(0, clip[3] + clip[1]);
        viewplanes[2].set(1, clip[7] + clip[5]);
        viewplanes[2].set(2, clip[11] + clip[9]);
        viewplanes[2].set(3, clip[15] + clip[13]);

        /* Normalize the result */
        t = (float) Math.sqrt(viewplanes[2].get(0) * viewplanes[2].get(0)
                              + viewplanes[2].get(1) * viewplanes[2].get(1)
                              + viewplanes[2].get(2) * viewplanes[2].get(2));
        viewplanes[2].scale(1 / t);
        viewplanes[2].set(3, viewplanes[2].get(3) * offsetBottom);

        /* Extract the TOP plane */
        viewplanes[3] = new Vec4f();
        viewplanes[3].set(0, clip[3] - clip[1]);
        viewplanes[3].set(1, clip[7] - clip[5]);
        viewplanes[3].set(2, clip[11] - clip[9]);
        viewplanes[3].set(3, clip[15] - clip[13]);

        /* Normalize the result */
        t = (float) Math.sqrt(viewplanes[3].get(0) * viewplanes[3].get(0)
                              + viewplanes[3].get(1) * viewplanes[3].get(1)
                              + viewplanes[3].get(2) * viewplanes[3].get(2));
        viewplanes[3].scale(1 / t);
        viewplanes[3].set(3, viewplanes[3].get(3) * offsetTop);

        /* Extract the FAR plane */
        viewplanes[4] = new Vec4f();
        viewplanes[4].set(0, clip[3] - clip[2]);
        viewplanes[4].set(1, clip[7] - clip[6]);
        viewplanes[4].set(2, clip[11] - clip[10]);
        viewplanes[4].set(3, clip[15] - clip[14]);

        /* Normalize the result */
        t = (float) Math.sqrt(viewplanes[4].get(0) * viewplanes[4].get(0)
                              + viewplanes[4].get(1) * viewplanes[4].get(1)
                              + viewplanes[4].get(2) * viewplanes[4].get(2));
        viewplanes[4].scale(1 / t);

        /* Extract the NEAR plane */
        viewplanes[5] = new Vec4f();
        viewplanes[5].set(0, clip[3] + clip[2]);
        viewplanes[5].set(1, clip[7] + clip[6]);
        viewplanes[5].set(2, clip[11] + clip[10]);
        viewplanes[5].set(3, clip[15] + clip[14]);

        /* Normalize the result */
        t = (float) Math.sqrt(viewplanes[5].get(0) * viewplanes[5].get(0)
                              + viewplanes[5].get(1) * viewplanes[5].get(1)
                              + viewplanes[5].get(2) * viewplanes[5].get(2));
        viewplanes[5].scale(1 / t);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public Vec4f[] getViewPlanes() {
        return viewplanes;
    }

    /*
     * public Vec4f[] getViewPlanes(Rectangle scissorRectangle){
     *   // A view frustum using a scissor rectangle is a frustum
     *   // with its top,bottom, and side planes shifted. Near and
     *   // far planes are not to be touched.
     *  Vec4f[] planes = getViewPlanes();
     *  planes[1].set(3,planes[1].get(3)-planes[1].get(0)*scissorRectangle.x);
     *  planes[2].set(3,planes[2].get(3)-planes[2].get(1)*scissorRectangle.y);
     * }
     */

    /**
     * Method description
     *
     *
     * @param vertices
     *
     * @return
     */
    public boolean isBoxInFrustum(Vec3d[] vertices) {
        Vec4f vec;
        float vec0, vec1, vec2, vec3;

        for (int i = 0; i < 6; i++) {
            vec  = viewplanes[i];
            vec0 = vec.get(0);
            vec1 = vec.get(1);
            vec2 = vec.get(2);
            vec3 = vec.get(3);

            if (vec0 * vertices[0].x() + vec1 * vertices[0].y()
                    + vec2 * vertices[0].z() + vec3 > 0) {
                continue;
            }

            if (vec0 * vertices[1].x() + vec1 * vertices[1].y()
                    + vec2 * vertices[1].z() + vec3 > 0) {
                continue;
            }

            if (vec0 * vertices[2].x() + vec1 * vertices[2].y()
                    + vec2 * vertices[2].z() + vec3 > 0) {
                continue;
            }

            if (vec0 * vertices[3].x() + vec1 * vertices[3].y()
                    + vec2 * vertices[3].z() + vec3 > 0) {
                continue;
            }

            if (vec0 * vertices[4].x() + vec1 * vertices[4].y()
                    + vec2 * vertices[4].z() + vec3 > 0) {
                continue;
            }

            if (vec0 * vertices[5].x() + vec1 * vertices[5].y()
                    + vec2 * vertices[5].z() + vec3 > 0) {
                continue;
            }

            if (vec0 * vertices[6].x() + vec1 * vertices[6].y()
                    + vec2 * vertices[6].z() + vec3 > 0) {
                continue;
            }

            if (vec0 * vertices[7].x() + vec1 * vertices[7].y()
                    + vec2 * vertices[7].z() + vec3 > 0) {
                continue;
            }

            return false;
        }

        return true;
    }

    /**
     * Method description
     *
     *
     * @param x
     * @param y
     * @param z
     * @param size_x
     * @param size_y
     * @param size_z
     *
     * @return
     */
    public boolean isBoxInFrustum(double x, double y, double z, double size_x,
                                  double size_y, double size_z) {
        Vec4f vec;
        float vec0, vec1, vec2, vec3;

        for (int i = 0; i < 6; i++) {
            vec  = viewplanes[i];
            vec0 = vec.get(0);
            vec1 = vec.get(1);
            vec2 = vec.get(2);
            vec3 = vec.get(3);

            if (vec0 * x + vec1 * y + vec2 * z + vec.get(3) > 0) {
                continue;
            }

            if (vec0 * (x + size_x) + vec1 * y + vec2 * z + vec3 > 0) {
                continue;
            }

            if (vec0 * x + vec1 * (y + size_y) + vec2 * z + vec3 > 0) {
                continue;
            }

            if (vec0 * (x + size_x) + vec1 * (y + size_y) + vec2 * z + vec3
                    > 0) {
                continue;
            }

            if (vec0 * x + vec1 * y + vec2 * (z + size_z) + vec3 > 0) {
                continue;
            }

            if (vec0 * (x + size_x) + vec1 * y + vec2 * (z + size_z) + vec3
                    > 0) {
                continue;
            }

            if (vec0 * x + vec1 * (y + size_y) + vec2 * (z + size_z) + vec3
                    > 0) {
                continue;
            }

            if (vec.get(0) * (x + size_x) + vec1 * (y + size_y)
                    + vec2 * (z + size_z) + vec3 > 0) {
                continue;
            }

            return false;
        }

        return true;
    }

    /**
     *
     *
     * @param point
     * @return
     * @deprecated Use isPointInFrustum instead
     */
    public boolean isPointInFrustum(Vec3d point) {
        return isPointInFrustum(point, 0);
    }

    /**
     * Method description
     *
     *
     * @param v
     * @param radius
     *
     * @return
     */
    public boolean isPointInFrustum(Vec3d v, double radius) {

        /**
         * FIXME : Martin Baker suggested some simplifications in point-in-frustum
         * calculations "Still, for each sphere, you may need to do as many as six
         * sphere-center-to-plane distance calculations.
         *
         * ...Or do you?
         *
         * If you have a relatively 'normal' symmetrical view frustum, there are some
         * simplifications that save a lot of CPU time in calculating those six distances.
         * Notice that:
         * In the plane equation mentioned above, (A,B,C) is the surface normal of the
         * plane (I make them all face inwards) and D is the distance from the origin
         * to the plane - measured such that D is positive for planes that face the origin
         * and negative from those that don't. For near and far planes, you know that A
         * and B are zero, C is either +1 or -1 and D is plus or minus the distance to the
         * plane. For the left and right plane B is zero and so is D. For the top and bottom
         * planes A is zero and so is D. C is the same for the left and right planes and
         * also the same for top and bottom planes. A is positive for the left plane and
         * negative for the right, but the magnitudes are the same. B is positive for the
         * top plane and negative for the bottom, but the magnitudes are the same.
         *
         * Knowing this can make testing much cheaper. Just a handful of
         * multiplies per sphere in general."
         */
        double d;
        Vec4f  vec;

        for (int i = 0; i < 6; i++) {
            vec = viewplanes[i];
            d   = v.x() * vec.get(0) + v.y() * vec.get(1) + v.z() * vec.get(2)
                  + vec.get(3);

            // System.out.println("Distance from plane "+ i+" :"+d);
            if (d <= -radius) {
                return false;    /* completely outside view volume */
            }
        }

        return true;
    }

    /**
     * Method description
     *
     *
     * @param rect
     *
     * @return
     */
    public boolean isRectClipped(Rectangle2D rect) {
        Vec3d v1 = new Vec3d(rect.getMinX(), 0, rect.getMinY());
        Vec3d v2 = new Vec3d(rect.getMinX(), 0, rect.getMaxY());
        Vec3d v3 = new Vec3d(rect.getMaxX(), 0, rect.getMinY());
        Vec3d v4 = new Vec3d(rect.getMaxX(), 0, rect.getMaxY());

        if (!isPointInFrustum(v1, 0) ||!isPointInFrustum(v2, 0)
                ||!isPointInFrustum(v3, 0) ||!isPointInFrustum(v4, 0)) {
            return false;
        }

        return true;
    }

    // ***** THIS IS THE OLD WAY OF COMPUTING FRUSTUM

    /**
     * Quite elegant, its not accurate and correct though. Needs to be
     * revisited
     */
    // gl.glGetDoublev(gl.GL_MODELVIEW_MATRIX, mv);
    //
    //
    // gl.glGetDoublev(gl.GL_PROJECTION_MATRIX, mp);
    //
    // for (int i = 0; i < 4; i++){
    // for (int j = 0; j < 4; j++){
    // for (int k = 0; k < 4; k++){
    // mvp[i][j] += mp[4*k+i]*mv[4*j+k];
    // }
    // }
    // }
    //
    // float d = 1.0f;
    // for (int i = 0; i < 6; i++){
    // d is used for the distance from side walls of frustum not near and
    // far
    // ones
    // float d = 1.0f;
    // if (i < 4){
    // d = 1f;
    // } else{
    // d = 1.0f;
    // FIXME : Remember to check here how the windowing frustum effect can
    // be
    // achieved
    // probably its is closely related to the VIEW_VOLUME_SIZE
    // d = i < 4 ? 1-0.75f : 1.0f;
    // }
    // int k = i/2;
    // /**
    // * Rule here is : plane[j] = (i & 1 ? +mpv[k][j] : -mpv[k][j]) - d *
    // mpv[3][j];
    // */
    //
    // for (int j = 0; j < 4; j++){
    // plane.set(j, (float) (i%2==0?-mvp[k][j]:mvp[k][j]-d*mvp[3][j]));
    // plane.set(j, (float) (Math.pow(-1, (i))*mvp[k][j]-d*mvp[3][j]));
    //
    // }
    // float l = (float)
    // Math.sqrt(plane.get(0)*plane.get(0)+plane.get(1)*plane.get(1)+plane.get(2)*plane.get(2));
    // viewInfo.viewplanes[i] = plane.times(1/l);
    // }
}
