package gr.cti.eslate.scene3d.viewer;

//~--- non-JDK imports --------------------------------------------------------

import gr.cti.eslate.math.linalg.*;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

//~--- classes ----------------------------------------------------------------

/**
 * Created by IntelliJ IDEA.
 * User: mantesat
 * Date: 5 Ιαν 2005
 * Time: 12:20:50 μμ
 * To change this template use Options | File Templates.
 */
public class MouseWheelController implements MouseWheelListener {
    Viewer3D viewer;

    //~--- constructors -------------------------------------------------------

    public MouseWheelController(Viewer3D viewer) {
        this.viewer = viewer;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param e
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        Camera c = viewer.getActiveCamera();

        if (c.isFlying()) {
            c.flightController.handleEvent(e);
        } else {
            c.isMoving = true;

            // First, compute ray from screen coordinates
            int x = e.getX();
            int y = e.getY();

            /**
             * Java uses a top-left origin window, while OpenGL uses a bottom left. This
             * means that y = viewHeight-y
             */
            Vec2i screenCoords    = new Vec2i(x, c.getViewHeight() - y);
            Mat4d modelViewMatrix = new Mat4d();

            c.getModelViewMatrix(new Vec3d(), modelViewMatrix);

            Mat4d projectionMatrix = new Mat4d();

            c.getProjectionMatrix(projectionMatrix);

            /**
             * Z must be a value between 0 and 1, and it seems to must be negative,
             * for the point on the ray to be inside the frustum (but why?)
             */
            Vec3d spaceCoords =
                ProjectionTransform.screenToSpaceCoordinates(screenCoords,
                    modelViewMatrix, projectionMatrix,
                    new Dimension(c.getViewWidth(), c.getViewHeight()), -0.75);    // 0.75 is a "good" z value, can be different

            // Calculate distance to move, concerning wheel rotation
            Vec3d  pos            = c.position();
            double distanceToMove = -e.getWheelRotation() * pos.y() / 10;

            // Calculate parametric value t in the ray equation, since we have the two points of the ray.
            double t = distanceToMove / MathUtils.distance(pos, spaceCoords);

            // Finally, get new position's coords.
            double xP = pos.x() + t * (spaceCoords.x() - pos.x());
            double yP = pos.y() + t * (spaceCoords.y() - pos.y());
            double zP = pos.z() + t * (spaceCoords.z() - pos.z());

//            viewer.animator.setAnimation(new GoToAnimation(c, pos.toFloat(),
//                    (new Vec3d(xP, yP, zP)).toFloat()));
            c.isMoving = false;
        }
    }
}
