/*
 * Created on 12 Ïêô 2005
 *
 */
package gr.cti.eslate.math.linalg;

import java.awt.Dimension;

public class ProjectionTransform {
    
    /** Some temp Mat4d matrices, and Vec4d vectors, to be reused in screenToSpaceCoordinates
    routine to avoid unnesesary initializations */
    
    private static Mat4d invertProj = new Mat4d();
    private static Mat4d invertModel = new Mat4d();
    
    private static Vec4d temp1 = new Vec4d();
    private static Vec4d temp2 = new Vec4d();
    private static Vec4d temp3 = new Vec4d();
    
    /**
     * Transforms space coordinates to screen coordinates for a given view window.
     * 
     * @param spaceCoords       The coordinates in space
     * @param modelViewMatrix   Camera's modelview matrix
     * @param projectionMatrix  Camera's projection matrix
     * @param viewSize          View window size
     * 
     * @return                  A new Vec2i with screen coordinates.
     */
    
    public static Vec2i spaceToScreenCoordinates(Vec3d spaceCoords, 
                                                 Mat4d modelViewMatrix, 
                                                 Mat4d projectionMatrix, 
                                                 Dimension viewSize){
        
        // First. go to homogenous coordinates
        Vec4d onSpaceCoords = new Vec4d(spaceCoords.x(), spaceCoords.y(), spaceCoords.z(),1);
        Vec4d onScreenCoords = new Vec4d();
        
        // Compute viewing matrix and find screen space coords.
        Mat4d viewMatrix = new Mat4d();
        viewMatrix.mul(projectionMatrix, modelViewMatrix);
        viewMatrix.xformVec(onSpaceCoords,onScreenCoords);
        
        // Normalize result and compute clip coordinates 
        onScreenCoords.scale(1/onScreenCoords.w());
        int x = (int) ((viewSize.width*onScreenCoords.x()+viewSize.width)/2);
        int y = (int) ((viewSize.height*onScreenCoords.y()+viewSize.height)/2);
        return new Vec2i(x,y);
    }
    
    /**
     * Trasforms screen coordinates to coordinates in space, by computing a ray from the camera
     * position, and using a depth value. Note that depth value must be a value between 0 and 1, and
     * must be negative in order for the point to be "in front" of the camera.
     * @param screenCoords      Screen coordinates. Note that Java uses a top left origin window 
     *                          coordinate system, instead of OpenGL's bottom-left origin. 
     * 
     * @param modelViewMatrix   Camera's modelview matrix
     * @param projectionMatrix  Camera's projection matrix
     * @param viewSize          View window size
     * @param depth             Depth value. depth must be a value between 0 and 1, and it  
     *                          must be negative, for the point on the ray to be inside the frustum. 
     *                          
     * @return                  A new Vec3d containing coordinates in space.
     */
    
    public static Vec3d screenToSpaceCoordinates(Vec2i screenCoords, 
                                                 Mat4d modelViewMatrix, 
                                                 Mat4d projectionMatrix, 
                                                 Dimension viewSize,
                                                 double depth){
        return screenToSpaceCoordinates(screenCoords, 
                                        modelViewMatrix, 
                                        projectionMatrix, 
                                        viewSize.width,
                                        viewSize.height,
                                        depth);
    }
    
    public static Vec3d screenToSpaceCoordinates(Vec2i screenCoords, 
            Mat4d modelViewMatrix, 
            Mat4d projectionMatrix, 
            int windowWidth,
            int windowHeight,
            double depth){
        
            double x = (2.0d*screenCoords.x())/windowWidth-1;
            double y = (2.0d*screenCoords.y())/windowHeight-1; 
            double z = 2*depth-1;
            double w = 1.0d;
            Vec4d vec = new Vec4d(x,y,z,w);
            
            /** To invert a projection matrix, no complicated matrix calculations are needed.
         
                       Projection matrix        Inverse projection matrix
                     
                         a  0   0   0               1/a 0   0   0   
                         0  b   0   0     --->      0   1/b 0   0
                         0  0   c   d               0   0   0   1/e
                         0  0   e   0               0   0   1/d -c/de                     
            **/
            
            invertProj.set(0,0,1/projectionMatrix.get(0,0));
            invertProj.set(1,1,1/projectionMatrix.get(1,1));
            invertProj.set(2,3,1/projectionMatrix.get(3,2));
            invertProj.set(3,2,1/projectionMatrix.get(2,3));
            invertProj.set(3,3,-projectionMatrix.get(2,2)/projectionMatrix.get(2,3)*projectionMatrix.get(3,2)); 
                      
            // To invert a modelview matrix, transpose its upper left 3x3 matrix (rotation), and translate with the 
            // inverse translation vector. invertRigid() does this job.
            invertModel.set(0,0,modelViewMatrix.get(0,0));
            invertModel.set(0,1,modelViewMatrix.get(0,1));
            invertModel.set(0,2,modelViewMatrix.get(0,2));
            invertModel.set(0,3,modelViewMatrix.get(0,3));
            invertModel.set(1,0,modelViewMatrix.get(1,0));
            invertModel.set(1,1,modelViewMatrix.get(1,1));
            invertModel.set(1,2,modelViewMatrix.get(1,2));
            invertModel.set(1,3,modelViewMatrix.get(1,3));
            invertModel.set(2,0,modelViewMatrix.get(2,0));
            invertModel.set(2,1,modelViewMatrix.get(2,1));
            invertModel.set(2,2,modelViewMatrix.get(2,2));
            invertModel.set(2,3,modelViewMatrix.get(2,3));       
            invertModel.set(3,0,modelViewMatrix.get(3,0));
            invertModel.set(3,1,modelViewMatrix.get(3,1));
            invertModel.set(3,2,modelViewMatrix.get(3,2));
            invertModel.set(3,3,modelViewMatrix.get(3,3)); 
            invertModel.invertRigid();
            Vec4d t1 = new Vec4d();
            invertProj.xformVec(vec,t1);
            t1.scale(1/t1.w());
            Vec4d t2 = new Vec4d();
            invertModel.xformVec(t1,t2);
            
            return new Vec3d(t2.x(), t2.y(), t2.z());
    }
    
}
