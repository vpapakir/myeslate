/*
 * Created on 4 Ιουλ 2006
 *
 */
package gr.cti.eslate.scene3d.viewer;

import gr.cti.eslate.math.linalg.Vec3d;


    /**
     * Created by A.Mantes at 25 Απρ 2004, 11:04:40 μμ
     */

    /**
     * The Restrainer interface helps determine the camera behaviour
     * when it goes too far from the object3d
     */
    public interface Restrainer {

        /**
         * Returns true if collision occurs with the IObjec3D when camera is
         * positioned to the specified position
         * @param position
         * @return
         */
        public boolean insideRestrainBox(Vec3d position);

        //~--- get methods --------------------------------------------------------

        /**
         * Returns a legal position when camera tries to be positioned to
         * the specified position. If restrain breaking occurs, always
         * according to the IObjec3D implementing the Restrain interface),
         * then this method returns the position the camera should take,
         * so as not to collide, or go "inside" the IObjec3D.
         * @param possiblePosition
         * @return
         */
        public Vec3d getLegalRestrainPosition(Vec3d possiblePosition);
}
