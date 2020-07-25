/*
 * Created on 7 Ιουν 2006
 *
 */
package gr.cti.eslate.protocol;

import gr.cti.eslate.math.linalg.Vec3d;
import gr.cti.eslate.scene3d.viewer.Object3D;



public interface IObject3D {

    public abstract Object3D getObjectModel();
    
    public abstract Vec3d getPosition();
    public abstract Vec3d getOrientation();
    public abstract Vec3d getScale();
    
}
