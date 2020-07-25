package gr.cti.eslate.scene3d.viewer;

//~--- non-JDK imports --------------------------------------------------------

import gr.cti.eslate.math.linalg.*;

//~--- JDK imports ------------------------------------------------------------

import com.sun.opengl.util.GLUT;

import java.awt.*;

import java.text.*;

import javax.media.opengl.GL;

//~--- classes ----------------------------------------------------------------

/** Rudimentary control panel displaying some outputs as text. Used the same way a HUD iz used in jet fighter planes */
public class HeadUpDisplay /* implements GLEnum */ {
    private static final Vec3f        FOREGROUND = new Vec3f(1, 1, 1);
    private static final int          FONT       = GLUT.BITMAP_HELVETICA_12;
    private static final NumberFormat dot2Fmt    =
        new DecimalFormat("######.00");

    //~--- fields -------------------------------------------------------------

    int         fTimesNew1, fTimesNewBMF;
    private int width, height;
    private GL gl;
    private GLUT glut = new GLUT();

    //~--- constructors -------------------------------------------------------

    public HeadUpDisplay() {}

    //~--- methods ------------------------------------------------------------

    private void color(GL gl, Vec3f c) {
        gl.glColor3f(c.x(), c.y(), c.z());
    }

    /**
     * Method description
     *
     *
     * @param gl
     * @param glu
     * @param glut
     * @param fps
     * @param altitudeAboveGround
     * @param xpos
     * @param zpos
     * @param yawAngle
     * @param pitchAngle
     * @param rollAngle
     */
    public void draw(GLHandle handle, Camera camera, float fps) {
        
        gl = handle.getGL();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, width, 0, height, -10000, 10000);    // , width, 0.0, height);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glDisable(GL.GL_DEPTH_TEST);
        color(gl, FOREGROUND);

        // Try to draw an artificial horizon ball
        gl.glPushMatrix();
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslated(100, 100, 0);
        double pitchAngle = camera.getPitchAngle();
        double yawAngle = camera.getYawAngle();
        double rollAngle = camera.getRollAngle();
        gl.glRotated(MathUtils.rad2dec(pitchAngle), 1, 0, 0);
        gl.glRotated(MathUtils.rad2dec(yawAngle), 0, 1, 0);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glClipPlane(GL.GL_CLIP_PLANE0, new double[] { 0, 1, 0, 0 }, 0);
        gl.glEnable(GL.GL_CLIP_PLANE0);
        gl.glColor3f(53f / 255, 148f / 255, 225f / 255);
        glut.glutSolidSphere(100, 30, 30);
        gl.glDisable(GL.GL_CLIP_PLANE0);
        gl.glClipPlane(GL.GL_CLIP_PLANE0, new double[] { 0, -1, 0, 0 }, 0);
        gl.glEnable(GL.GL_CLIP_PLANE0);
        gl.glColor3f(187f / 255, 123f / 255, 66f / 255);
        glut.glutSolidSphere(100, 30, 30);
        gl.glDisable(GL.GL_CLIP_PLANE0);
        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glPopMatrix();

        float radius  = 0.15f;
        float DEG2RAD = (float) 3.14159 / 180;
        int   max     = Math.max(width, height);

        gl.glBegin(GL.GL_LINE_LOOP);

        for (int i = 0; i < 360; i++) {
            float degInRad = i * DEG2RAD;

            gl.glVertex2f((float) Math.cos(degInRad) * radius * (max)
                          + width / 2, (float) Math.sin(degInRad) * radius
                            * (max) + height / 2);
        }

        gl.glEnd();
        radius = 0.008f;
        gl.glBegin(GL.GL_LINE_LOOP);

        for (int i = 0; i < 360; i++) {
            float degInRad = i * DEG2RAD;

            gl.glVertex2f((float) Math.cos(degInRad) * radius * (max)
                          + width / 2, (float) Math.sin(degInRad) * radius
                            * (max) + height / 2);
        }

        gl.glEnd();
        drawLine(gl, (float) width / 2, (float) height / 2 + 0.008f * max,
                 (float) width / 2, (float) height / 2 + 20);
        drawLine(gl, (float) width / 2 - 0.008f * max, (float) height / 2,
                 (float) width / 2 - 25, (float) height / 2);
        drawLine(gl, (float) width / 2 + 0.008f * max, (float) height / 2,
                 (float) width / 2 + 25, (float) height / 2);
        drawLine(gl, (float) width / 8, (float) 7 * height / 8,
                 (float) 7 * width / 8, (float) 7 * height / 8);
        drawLine(gl, (float) width / 8, (float) 7 * height / 8,
                 (float) width / 8, (float) 7 * height / 8 + 20);
        drawLine(gl, (float) 7 * width / 8, (float) 7 * height / 8,
                 (float) 7 * width / 8, (float) 7 * height / 8 + 20);
        drawRect(gl, (float) 19 * width / 40, (float) 7 * height / 8 + 3,
                 width / 20, height / 30);

        int degrees = -(int) (yawAngle / DEG2RAD) % 360;

        if (degrees < 0) {
            degrees = 360 + degrees;
        }

        String degString = "";

        if (degrees < 10) {
            degString = degString + "00" + degrees;
        } else if (degrees < 100) {
            degString = degString + "0" + degrees;
        } else {
            degString = degString + degrees;
        }

        textAt(gl, glut, (float) (20 * width / 40 - 9) / width,
               (float) (7 * height / 8 + 8) / height, "" + degString);

        int k = 1;
        int j = 0;

        for (int i = degrees - 20; i < degrees; i++) {
            if (i % 5 == 0) {
                drawLine(gl, (float) width / 8 + k * width / 60,
                         (float) 7 * height / 8,
                         (float) width / 8 + k * width / 60,
                         (float) 7 * height / 8 + 20);

                if (i < 0) {
                    j = 360 + i;
                } else {
                    j = i;
                }

                textAt(gl, glut,
                       (float) (width / 8 + k * width / 60 - 9) / width,
                       (float) (7 * height / 8 + 23) / height, "" + j);
            } else {
                drawLine(gl, (float) width / 8 + k * width / 60,
                         (float) 7 * height / 8,
                         (float) width / 8 + k * width / 60,
                         (float) 7 * height / 8 + 10);
            }

            k++;
        }

////////////////////////DRAW PITCH DATA//////////////////////////////////////////////////////////////
        degrees = (int) (pitchAngle / DEG2RAD) % 360;
        gl.glLoadIdentity();
        gl.glTranslatef(width / 2, height / 2, 0);
        gl.glRotatef((float) MathUtils.rad2dec(-rollAngle), 0, 0, 1);
        gl.glTranslatef(-width / 2, -height / 2, 0);
        k = 0;
        j = 0;

        for (int i = degrees - 15; i < degrees + 15; i++) {
            if (i % 5 == 0) {
                if (i >= 0) {
                    drawLine(gl, (float) width * 14 / 32,
                             (float) 2 * height / 8 + k * height / 60,
                             (float) width * 8 / 32,
                             (float) 2 * height / 8 + k * height / 60);
                    drawLine(gl, (float) width * 18 / 32,
                             (float) 2 * height / 8 + k * height / 60,
                             (float) width * 24 / 32,
                             (float) 2 * height / 8 + k * height / 60);
                } else {
                    gl.glEnable(GL.GL_LINE_STIPPLE);
                    gl.glLineStipple(1, (new Integer(0x00FF)).shortValue());
                    drawLine(gl, (float) width * 14 / 32,
                             (float) 2 * height / 8 + k * height / 60,
                             (float) width * 8 / 32,
                             (float) 2 * height / 8 + k * height / 60);
                    drawLine(gl, (float) width * 18 / 32,
                             (float) 2 * height / 8 + k * height / 60,
                             (float) width * 24 / 32,
                             (float) 2 * height / 8 + k * height / 60);
                    gl.glDisable(GL.GL_LINE_STIPPLE);
                }

                textAt(gl, glut, (float) 7 / 32,
                       (float) (2 * height / 8 + k * height / 60 - 6)
                       / height, " " + i);
                textAt(gl, glut, 24.2f / 32,
                       (float) (2 * height / 8 + k * height / 60 - 6)
                       / height, " " + i);
            }

            k++;
        }

        // gl.glRotatef(-rollAngle, 0,0,1);
        // gl.glTranslatef(height/2,width/2, 0);
        // gl.glLoadIdentity();
        // uderscreen info quad drawing follows

        /*
         * gl.glBegin(GL_QUADS);
         * //    gl.glBegin(GL_LINE_LOOP);
         * float bottomY = BOTTOM_EDGE_SCREEN_FRAC;
         * float topY = bottomY + 1.5f * ((float) FONT_HEIGHT / (float) height);
         * float leftX  = 0.5f - (WIDTH_SCREEN_FRAC / 2.0f);
         * float rightX = 0.5f + (WIDTH_SCREEN_FRAC / 2.0f);
         * quad(leftX,  topY,
         * leftX,  bottomY,
         * rightX, bottomY,
         * rightX, topY);
         * gl.glEnd();
         *
         */

        // gl.glClear(gl.GL_MODELVIEW);
        // gl.glDisable(GL_BLEND);
        color(gl, FOREGROUND);

        float textY  = 0.96f;
        float factor = 0.035f;    // (float) bottomY + 0.01f * ((float) FONT_HEIGHT / (float) height);

        textAt(gl, glut, 0.8f, textY, "FPS: " + dot2Fmt.format(fps));

        // textAt(0.8f, textY - factor, "PPF: " + dot1Fmt.format(ppf / 1000.0f) + "k");
        Vec3d position = camera.position();
        textAt(gl, glut, 0.8f, textY - 2 * factor,
               "Height (m): " + dot2Fmt.format(position.y()));

        // textAt(0.8f, textY - 3 * factor, "X (m): " + dot2Fmt.format(xpos));
        // textAt(0.8f, textY - 4 * factor, "Y (m): " + dot2Fmt.format(zpos));
        GraphicsEnvironment ge =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs    = ge.getScreenDevices();
        int              bytes = gs[0].getAvailableAcceleratedMemory();

        textAt(gl, glut, 0.8f, textY - 3 * factor,
               "VGA memory left : " + bytes);
        textAt(gl, glut, 0.8f, textY - 5 * factor, "Long : " + position.x());
        textAt(gl, glut, 0.8f, textY - 6 * factor, "Lat  : " + position.z());

//      fillRect(200, 200, 10, 10);
//      fillCircle(400, 400, 32);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    private void drawLine(GL gl, float sx, float sy, float ex, float ey) {
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(sx, sy);
        gl.glVertex2f(ex, ey);
        gl.glEnd();
    }

    private void drawRect(GL gl, float sx, float sy, int width, int height) {
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex2f(sx, sy);
        gl.glVertex2f(sx + width, sy);
        gl.glVertex2f(sx + width, sy + height);
        gl.glVertex2f(sx, sy + height);
        gl.glEnd();
    }

    private void fillCircle(GL gl, float sx, float sy, float radius) {
        radius = 0.008f;
        gl.glBegin(GL.GL_POLYGON);

        float DEG2RAD = (float) 3.14159 / 180;
        int   max     = Math.max(width, height);

        for (int i = 0; i < 360; i++) {
            float degInRad = i * DEG2RAD;

            gl.glVertex2f((float) Math.cos(degInRad) * radius * (max) + sx,
                          (float) Math.sin(degInRad) * radius * (max) + sy);
        }

        gl.glEnd();
    }

    private void fillRect(GL gl, float sx, float sy, int width, int height) {
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex2f(sx, sy);
        gl.glVertex2f(sx + width, sy);
        gl.glVertex2f(sx + width, sy + height);
        gl.glVertex2f(sx, sy + height);
        gl.glEnd();
    }

    private void textAt(GL gl, GLUT glut, float xFrac, float yFrac,
                        String string) {
        gl.glRasterPos2d(width * xFrac, height * yFrac);
        glut.glutBitmapString(FONT, string);
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        this.width  = width;
        this.height = height;
    }

}
