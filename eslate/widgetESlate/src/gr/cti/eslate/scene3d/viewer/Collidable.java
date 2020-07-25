/*
 * Created on 4 Ιουλ 2006
 *
 */
package gr.cti.eslate.scene3d.viewer;

import gr.cti.eslate.math.linalg.Vec3d;

//~--- non-JDK imports --------------------------------------------------------

//~--- interfaces -------------------------------------------------------------

/**
 * Created by A.Mantes at 26 Φεβ 2004, 3:21:20 μμ
 */

/**
 * The Collidable interface helps determine the camera behaviour when it collides
 * with IObject3Ds that implement it
 */
public interface Collidable {

    /**
     * Returns true if collision occurs with the IObjec3D when camera is positioned
     * to the specified position
     * @param position
     * @return A boolean, indicating whether collision happened or not.
     */
    public boolean collisionOccurs(Vec3d position);

    //~--- get methods --------------------------------------------------------

    /**
     * Returns a legal position when camera tries to be positioned to the specified
     * position. If collision occurs,(always according to the IObjec3D implementing
     * the Collidable interface), then this method returns the position the camera should
     * take, so as not to collide, or go "inside" the IObjec3D.
     * @param possiblePosition The candidate position for the camera
     * @return A Vec3d, with a legal position for the camera.
     */
    public Vec3d getLegalPosition(Vec3d possiblePosition);
}