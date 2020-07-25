package gr.cti.eslate.scene3d.viewer;

//~--- non-JDK imports --------------------------------------------------------

import gr.cti.eslate.scene3d.viewer.actions.CameraAction;
import gr.cti.eslate.scene3d.viewer.actions.FlyAction;
import gr.cti.eslate.scene3d.viewer.actions.LeapAction;
import gr.cti.eslate.scene3d.viewer.event.CameraEvent;
import gr.cti.eslate.scene3d.viewer.event.CameraEventMulticaster;
import gr.cti.eslate.scene3d.viewer.event.CameraListener;
import gr.cti.eslate.math.linalg.*;
import gr.cti.eslate.math.linalg.filter.SinFilter;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Comparator;

import javax.media.opengl.GL;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.glu.GLU;

import javax.swing.JFileChooser;
import javax.swing.Timer;

import com.sun.media.sound.MidiUtils.TempoCache;

//~--- classes ----------------------------------------------------------------

/**
 *  The camera maintains a position and orientation in world-coordinate space. Clients
 *  can request a single-precision camera matrix for a given higher-precision (currently
 *  double-precision) origin. This enables visualization of large datasets which have been
 *  split up into regions which locally can be visualized with single-precision floating-point
 *  numbers, but in which the global position must be maintained with higher precision to
 *  avoid jitter. This class can be extended to higher precision by replacing the Vec3d
 *   references and extending related classes appropriately.
 */
public class Camera {
    private static final float PI_OVER_TWO     = (float) (Math.PI / 2);
    public static final int    NORMAL_MODE     = 0;
    public static final int    DEBUG_MODE      = 1;
    private static final float ANGLE_INCREMENT = PI_OVER_TWO / 90;

    //~--- fields -------------------------------------------------------------

    private Rot      orientation = new Rot();
    ViewInfo         viewInfo    = new ViewInfo();
    private Mat4d    tmpMat      = new Mat4d();
    private Vec3d    tempPos     = new Vec3d();
    private Rot      roll        = new Rot(),
                     pitch       = new Rot(),
                     yaw         = new Rot();
    double[]         projdata    = new double[16];
    private Mat4d    projMat     = new Mat4d();
    private FlyAction flyAction;
    
    class PinpointTimer extends Timer{
        int ticks = 0;
        
        PinpointTimer(){
            super(30, new ActionListener(){
                
                public void actionPerformed(ActionEvent e){    
                    pinpointTimer.tick();
                }

            });
        }
        
        private void tick(){
            if (ticks ==30){
                pinpointTimer.stop();
                viewer3d.canvas.repaint();
                return;
            }
            if ((System.nanoTime()-pinpointStartTime)<1e9){
                ticks+=1;              
            }else
                pinpointTimer.stop();
            viewer3d.canvas.repaint();
                
        }
        
        public void start(){
            ticks = 0;
            super.start();
        }      
    }
    
    PinpointTimer pinpointTimer = new PinpointTimer();
    
    private long pinpointStartTime = 0;
    Vec3d pinpointPosition = null;

    /**
     * Its not wise to process every single event that comes from outside, too
     * much render load. Instead, an event queue is maintained for each event
     * type, and during periodic intervals, the last event in queues is handled,
     * and all other events -of the same type- are disposed. Note that when
     * flushing last-arrived events for every type, the event that came first is
     * flushed first!
     */

    // FIXME/NOTE: Maybe the same policy should be followed for the entire event
    // handling, and for the viewer3D?
    private ArrayList orientationChangedEventQueue = new ArrayList(),    // The event queue for rotations
                      positionChangedEventQueue = new ArrayList();    // The event queue
    int              mode             = NORMAL_MODE;
    public float     lengthIncrement  = 50.0f;
    public FlightController flightController = new FlightController(this);
    
    Timer eventTimer = new Timer(25, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            EventTimeStampBind b1 = flushEvent(orientationChangedEventQueue);
            EventTimeStampBind b2 = flushEvent(positionChangedEventQueue);

            if ((b1 == null) && (b2 == null)) {
                return;
            }

            if ((b1 == null) && (b2 != null)) {
                cameraListener.positionChanged(b2.event);
            } else if ((b1 != null) && (b2 == null)) {
                cameraListener.orientationChanged(b1.event);
            } else {
                if (bindComparator.compare(b1, b1) <= 0) {
                    cameraListener.orientationChanged(b1.event);
                    cameraListener.positionChanged(b2.event);
                } else {
                    cameraListener.positionChanged(b2.event);
                    cameraListener.orientationChanged(b1.event);
                }
            }
        }
    });
    
    private Vec3d cartesianPosition = new Vec3d();
    private CameraEventMulticaster cameraListener    = new CameraEventMulticaster();
    private EventTimeStampBindComparator bindComparator = new EventTimeStampBindComparator();
    private double       aspect;        // Aspect ratio
    private String       cameraName;    // A string identifier for the camera
    private double       fov;           // Y field of view
    public boolean       isOrbiting, isMoving, isLookingAround, isFlying,
                         isFlyingPaused;
    private double   nearClip, farClip;    // Near and far clipping planes distances.
    private boolean  oglStateDirty;
    private float    orbitDistance;
    private Vec3d    orbitPoint;
    private float    rollAngle, pitchAngle, yawAngle;
    private Viewer3D viewer3d;    // The camera owning viewer

    //~--- constructors -------------------------------------------------------

    private Camera() {
        projMat.makeIdent();
    }

    /**
     * Constructor for a camera object. Initial position is (0,0,0), and
     * initial orientation is (0,0,0).
     */
    public Camera(Viewer3D viewer3d, String name) {
        this();
        this.viewer3d = viewer3d;
        cameraName    = name;
        cartesianPosition.setX(00);
        cartesianPosition.setY(1000);
        cartesianPosition.setZ(00);

        // addCameraListener(recorder);
        eventTimer.setCoalesce(true);
        eventTimer.setRepeats(false);
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param l
     */
    public void addCameraListener(CameraListener l) {
        if (cameraListener != null) {
            cameraListener.add(l);
        }
    }

    /**
     * Moves the camera towards the lookAt vector direction for a specified
     * distance.
     *
     * @param distance
     */
    public void advance(float distance) {
        Vec2i screenCoords = new Vec2i(getViewWidth() / 2,
                                       getViewHeight() / 2);
        Mat4d modelViewMatrix = new Mat4d();

        getModelViewMatrix(new Vec3d(), modelViewMatrix);

        Mat4d projectionMatrix = new Mat4d();

        getProjectionMatrix(projectionMatrix);

        /**
         * Z must be a value between 0 and 1, and it seems to must be negative,
         * for the point on the ray to be inside the frustum (but why?)
         */
        Vec3d spaceCoords =
            ProjectionTransform.screenToSpaceCoordinates(screenCoords,
                modelViewMatrix, projectionMatrix,
                new Dimension(getViewWidth(), getViewHeight()), -0.75);    // 0.75 is a "good" z value, can be different

        // Calculate distance to move, concerning wheel rotation
        Vec3d pos            = position();
        float distanceToMove = distance;

        // Calculate parametric value t in the ray equation, since we have the two points of the ray.
        double t = distanceToMove / MathUtils.distance(pos, spaceCoords);

        // Finally, get new position's coords.
        double xP = pos.x() + t * (spaceCoords.x() - pos.x());
        double yP = pos.y() + t * (spaceCoords.y() - pos.y());
        double zP = pos.z() + t * (spaceCoords.z() - pos.z());

        setPosition(new Vec3d(xP, yP, zP));
    }

    void approachTo(double positionX, double positionZ, double positionY) {

        // System.out.println("approachTo called");
        double x    = positionX;
        double y    = positionY;
        double z    = positionZ;
        double xDis = Math.abs(position().x() - x);
        double yDis = Math.abs(position().y() - y);
        double zDis = Math.abs(position().z() - z);
        double newHeight;

        if (positionY >= position().y()) {
            newHeight = positionY - yDis / 3;
        } else {
            newHeight = positionY + yDis / 3;
        }

        double newX;

        if (x >= position().x()) {
            newX = x - xDis / 3;
        } else {
            newX = x + xDis / 3;
        }

        double newZ;

        if (z >= position().z()) {
            newZ = z - zDis / 3;
        } else {
            newZ = z + zDis / 3;
        }

        jumpTo(newX, newZ, newHeight);
    }

    /**
     * Gets the aspect ratio
     *
     * @return
     */
    public double aspectRatio() {
        return aspect;
    }

    /**
     * Gets the distance of the far clipping plane
     *
     * @return
     */
    public double farClip() {
        return farClip;
    }

    private void fireOrientationChanged(CameraEvent e) {

        // cameraListener.orientationChanged(e);
        orientationChangedEventQueue.add(0, new EventTimeStampBind(e,
                System.nanoTime()));

        if (!eventTimer.isRunning()) {
            eventTimer.start();
        }
    }

    private void firePositionChanged(CameraEvent e) {
        positionChangedEventQueue.add(0, new EventTimeStampBind(e,
                System.nanoTime()));

        if (!eventTimer.isRunning()) {
            eventTimer.start();
        }
    }

    private EventTimeStampBind flushEvent(ArrayList eventQueue) {
        EventTimeStampBind bind = null;

        synchronized (eventQueue) {
            if (!eventQueue.isEmpty()) {
                bind = (EventTimeStampBind) eventQueue.get(0);
                eventQueue.clear();
            }
        }

        return bind;
    }

    /**
     * Method description
     *
     *
     * @param pos
     * @param lookAt
     */
    public void flyTo(Vec3d pos, Vec3d lookAt) {
    	flyTo(pos,lookAt,null);
    }

    /**
     * Method description
     *
     *
     * @param pos
     * @param lookAt
     */
    public void flyTo(Vec3d pos, Vec3d lookAt,ActionListener listener) {
    	float d;
    	
    	/**
    	 * How the "d" lookAt distance is computed? Someone can look on an existing
    	 * or non existing point, which defines together with the point of his position,
    	 * a straight line. There MUST be a point on that line with distance "d"
    	 * from him
    	 */
    	d = MathUtils.distance(pos.toFloat(), lookAt.toFloat());
    	
    	Vec3f lookAtp =
    		new Vec3f((float) (-d * Math.sin(yawAngle) * Math.cos(pitchAngle)
    				+ position().x()), (float) (position().y()
    						+ d * Math.sin(pitchAngle)), (float) (d
    								* Math.cos(yawAngle)
    								* Math.cos(pitchAngle) + position().z()));
    	
    	  LeapAction action = new LeapAction(CameraAction.LINEAR,
    	        new SinFilter(),
    	        this,
    	       position().toFloat(),
    	        lookAtp,
    	        pos.toFloat(),
    	        lookAt.toFloat(),
    	        1);
          viewer3d.actions.add(action);
    }
    
    /**
     * Method description
     *
     *
     * @param lookAtPoint
     * @param yawAngle
     * @param height
     * @param distance
     */
    public void flyTo(Vec3d lookAtPoint, float yawAngle, float height,
                      float distance) {
        Vec3d pos = new Vec3d();

        pos.setX(lookAtPoint.x() + distance * Math.sin(yawAngle));
        pos.setY(height);
        pos.setZ(lookAtPoint.z() + distance * Math.cos(yawAngle));

        // System.out.println("lookat point : "+lookAtPoint);
        float d                  = MathUtils.distance(pos.toFloat(),
                                       lookAtPoint.toFloat());
        float yawAngularDistance = Math.abs(this.yawAngle - yawAngle);
        float pitchAngularDistance;

        if ((pos.x() == position().x()) && (pos.z() == position().z())) {
            pitchAngularDistance = (float) Math.abs(this.pitchAngle
                    - Math.PI / 2);
        } else {
            pitchAngularDistance = Math.abs(this.pitchAngle
                                            - (float) Math.asin(-(height
                                                - lookAtPoint.y()) / d));

            // System.out.println("PitchAngle NOW : "+MathUtils.rad2dec(this.pitchAngle));
            // System.out.println("PitchAngle TARGET : "+MathUtils.rad2dec((float) Math.asin(-(height-lookAtPoint.y())/d)));
        }

        flyTo(pos, lookAtPoint);
    }

    /**
     * Gets the vertical field of view
     *
     * @return
     */
    public double fovY() {
        return fov;
    }

    /**
     * Sets a new position and lookAtPoint for the camera. No animation
     *
     * @param position
     * @param lookAtPoint
     */
    public void goTo(Vec3d position, Vec3d lookAtPoint) {
        setPosition(position);
        lookAt(lookAtPoint, Vec3d.Y_AXIS);
        update();
    }

    /**
     * Causes the camera to jump to a new position, without changing view
     * angles. Animation duration can be set in seconds.
     */
    private void jumpTo(double positionX, double positionZ, double positionY) {

        // Replaced by "Animation" mechanisms

        
          CameraAction action = new CameraAction(CameraAction.LINEAR,
                new SinFilter(), // FIXME:  Filter used should be a parameter....
                this,
                position().toFloat(),
                null,
                new Vec3d(positionX, positionY, positionZ).toFloat(),
                null,
                1);
          viewer3d.actions.add(action);
         
    }

    void lookAround() {
        setOrientation(pitchAngle, yawAngle + 5 * ANGLE_INCREMENT, rollAngle);
    }

    /**
     * Causes the camera to orient itself so it is looking at the
     * specified point, biasing its orientation so its up vector is in
     * the plane in which both the up and (lookAt - position) vectors
     * lie. Call setPosition() before using this routine.  Results are
     * undefined if two or more of these vectors are coincident.
     *
     * @param lookAtPoint
     * @param upVector
     */
    public void lookAt(Vec3d lookAtPoint, Vec3d upVector) {

        // Since we reveted the coord system to be left handed, same
        // adjustments should be used here too.
        lookAtPoint.setZ(2 * position().z() - lookAtPoint.z());

        Vec3d eye   = position().minus(lookAtPoint);
        Vec3d back  = new Vec3d(eye);
        Vec3d right = new Vec3d();
        Vec3d up    = new Vec3d(upVector);

        // Eye points away from the look-at point
        right.cross(up, eye);
        up.cross(eye, right);
        right.normalize();
        back.normalize();
        up.normalize();

        Mat4d mat = new Mat4d();

        mat.makeIdent();

        // /mat.setTranslation(new Vec3d(1,1,-1));
        mat.setRotation(right, up, back);

        Rot rot = new Rot();

        rot.fromMatrix(mat);
        setOrientation(rot);
    }

    /**
     * Method description
     *
     *
     * @param lookAtPoint
     * @param yawAngle
     * @param height
     * @param distance
     */
    public void lookAt(Vec3d lookAtPoint, float yawAngle, float height,
                       float distance) {
        Vec3d pos = new Vec3d();

        pos.setX(lookAtPoint.x() + distance * Math.sin(yawAngle));
        pos.setY(height);
        pos.setZ(lookAtPoint.z() + distance * Math.cos(yawAngle));
        goTo(pos, lookAtPoint);
    }

    /**
     * Method description
     *
     *
     * @param xAxis
     * @param zAxis
     */
    public void move(float xAxis, float zAxis) {
        Rot rotYaw = new Rot();

        rotYaw.set(Vec3d.Y_AXIS, -yawAngle);

        Vec3d direction = new Vec3d(xAxis, 0, zAxis);

        direction = rotYaw.rotateVector(direction);
        direction.normalize();
        move(direction, Math.sqrt(xAxis * xAxis + zAxis * zAxis));
    }

    /**
     * Method description
     *
     *
     * @param direction
     * @param length
     */
    public void move(Vec3d direction, double length) {

        // Use line equation parametric form to move to given direction
        // for given length.
        direction.normalize();

        Vec3d p0 = position();
        Vec3d p1 = position();

        p1.add(direction);

        Vec3d newPos = p0.addScaled(length, p1.minus(p0));

        setPosition(newPos);
    }

    /**
     * Gets the distance of the near clipping plane
     *
     * @return
     */
    public double nearClip() {
        return nearClip;
    }

    void orbit() {
        yawAngle += 5 * ANGLE_INCREMENT;
        setPosition(new Vec3d(orbitDistance * Math.sin(yawAngle)
                              + orbitPoint.x(), position().y(),
                                  orbitDistance * Math.cos(yawAngle)
                                  + orbitPoint.z()));
        setOrientation(pitchAngle, yawAngle, rollAngle);
        lookAt(orbitPoint, Vec3d.Y_AXIS);
    }

    /**
     * Return the camera orientation in a vector containing Euler
     * (pitch, yaw, roll) angles
     *
     * @return
     */
    public Vec3d orientation() {
        return new Vec3d(pitchAngle, yawAngle, rollAngle);
    }
    
    public Vec3d direction(){
        Vec3d direction = new Vec3d(Vec3d.Z_AXIS);
        Vec3d result = orientation.rotateVector(direction);
        result.normalize();
        return result;
    }

    void pauseFlying(boolean pause) {
        isFlyingPaused = pause;
    }

    /**
     * Returns the position of the camera
     *
     * @return
     */
    public Vec3d position() {
        return cartesianPosition.copy();
    }

    /**
     * Method description
     *
     *
     * @param l
     */
    public void removeCameraListener(CameraListener l) {
        if (cameraListener != null) {
            cameraListener.remove(l);
        }
    }

    void retreatFrom(double positionX, double positionZ, double positionY) {

        // System.out.println("retreatFrom called");
        double x    = positionX;
        double y    = positionY;
        double z    = positionZ;
        double xDis = Math.abs(position().x() - x);
        double yDis = Math.abs(position().y() - y);
        double zDis = Math.abs(position().z() - z);
        double newHeight;

        if (positionY >= position().y()) {
            newHeight = position().y() - 2 * yDis;
        } else {
            newHeight = position().y() + 2 * yDis;
        }

        double newX;

        if (x >= position().x()) {
            newX = position().x() - 2 * xDis;
        } else {
            newX = position().x() + 2 * xDis;
        }

        double newZ;

        if (z >= position().z()) {
            newZ = position().z() - 2 * zDis;
        } else {
            newZ = position().z() + 2 * zDis;
        }

        jumpTo(newX, newZ, newHeight);
    }

    /**
     * Set both camera position and orientation.
     * @param position A Vec3d containing new position coordinates
     * @param angles A Vec3d conatining pitch, yaw and roll angles, in radians (and with this
     *               specific order).
     */
    public void setupCamera(Vec3d position, Vec3d angles) {

        // First, change position
        boolean positionChanged    = false,
                orientationChanged = false;
        if (position != null && !position.equals(cartesianPosition)) {    
            tempPos.set(position);
    
            if (viewer3d.objectsToView.size() > 0) {
                for (int i=0;i<viewer3d.objectsToView.size();i++){
                    Object3D obj3d = viewer3d.objectsToView.get(i);
        
                    if (obj3d instanceof Collidable) {
                        tempPos.set(((Collidable) obj3d).getLegalPosition(position));
                    }
        
                    if (obj3d instanceof Restrainer /* && !((Restrainer) obj3d).insideRestrainBox(tempPos) */) {
                        tempPos = ((Restrainer) obj3d).getLegalRestrainPosition(tempPos);
                    }
                }
            }
            this.cartesianPosition.set(tempPos);
            positionChanged = true;
        }

        // Then, change orientation
        if (angles!= null && (this.pitchAngle != angles.x() || this.rollAngle != angles.z() || this.yawAngle != angles.y())) {
            roll.set(Vec3d.Z_AXIS, angles.z());    // roll - angle round x axis
            pitch.set(Vec3d.X_AXIS, angles.x());    // pitch - angle round z axis (in our case phi)
            yaw.set(Vec3d.Y_AXIS, angles.y());    // yaw - angle round y axis (in our case theta)
    
            Rot orientation = new Rot(yaw.times(pitch).times(roll));
    
            if (!orientation.equals(this.orientation)) {
               
                this.orientation.set(orientation);
        
                // and update angle variables..... yaw/pitch/rollAngle
                pitchAngle = (float) angles.x();
                yawAngle   = (float) angles.y();
                rollAngle  = (float) angles.z();
                orientationChanged = true;
            }
        }

        // Not Roll capabilities (yet) ...
        // rollAngle=0;//(float) orientation.getAngles().z()/(float) (360*Math.PI*2);
        // fire away events
        if (positionChanged)
            firePositionChanged(new CameraEvent(this,CameraEvent.POSITION_CHANGED, position(), orientation()));
        if (orientationChanged)
            fireOrientationChanged(new CameraEvent(this,CameraEvent.ORIENTATION_CHANGED, position(), orientation()));


//        if (!isAnimating()) {
        viewer3d.canvas.repaint();
//        }
    }

    /**
     * Method description
     *
     *
     * @param position
     * @param lookAtPoint
     * @param upVector
     */
    public void setupCamera(Vec3d position, Vec3d lookAtPoint,
                            Vec3d upVector) {

        // First, change position
        boolean positionChanged    = false,
                orientationChanged = false;
        if ((position != null) &&!position.equals(cartesianPosition)) {
            this.cartesianPosition.set(position);

            for (int i=0;i<viewer3d.objectsToView.size();i++){
                Object3D obj3d = viewer3d.objectsToView.get(i);

                if (obj3d instanceof Collidable) {
                    tempPos.set(
                        ((Collidable) obj3d).getLegalPosition(position));
                    this.cartesianPosition.set(tempPos);
                }

                if (obj3d instanceof Restrainer /* && !((Restrainer) obj3d).insideRestrainBox(tempPos) */) {
                    tempPos =
                        ((Restrainer) obj3d).getLegalRestrainPosition(tempPos);
                    this.cartesianPosition.set(tempPos);
                }
            
            }

            positionChanged = true;
        }

        if (lookAtPoint != null) {

            // Since we reveted the coord system to be left handed, same
            // adjustments should be used here too.
            lookAtPoint.setZ(2 * position().z() - lookAtPoint.z());

            Vec3d eye   = position().minus(lookAtPoint);
            Vec3d back  = new Vec3d(eye);
            Vec3d right = new Vec3d();
            Vec3d up    = new Vec3d(upVector);

            // Eye points away from the look-at point
            right.cross(up, eye);
            up.cross(eye, right);
            right.normalize();
            back.normalize();
            up.normalize();

            Mat4d mat = new Mat4d();

            mat.makeIdent();

            // /mat.setTranslation(new Vec3d(1,1,-1));
            mat.setRotation(right, up, back);

            Rot rot = new Rot();

            rot.fromMatrix(mat);

            if (!rot.equals(this.orientation)) {
                this.orientation.set(rot);

                // and update angle variables..... yaw/pitch/rollAngle
                Vec3d angles = orientation.getAngles();

                pitchAngle = (float) angles.x();
                yawAngle   = (float) angles.y();
                rollAngle  = (float) angles.z();

                // Not Roll capabilities (yet) ...
                orientationChanged = true;
            }
        }

        // rollAngle=0;//(float) orientation.getAngles().z()/(float) (360*Math.PI*2);
        Vec3d pos         = position();
        Vec3d orientation = orientation();

        // fire away events
        if (positionChanged) {
            firePositionChanged(new CameraEvent(this,
                    CameraEvent.POSITION_CHANGED, pos, orientation));
        }

        if (orientationChanged) {
            fireOrientationChanged(new CameraEvent(this,
                    CameraEvent.ORIENTATION_CHANGED, pos, orientation));
        }

//      viewer3d.renderingWorkerThread.setDirty();
//        if (!isAnimating()) {
        viewer3d.canvas.repaint();
//        }
    }

    void startFlying() {
        flyAction = new FlyAction(this);
        viewer3d.actions.add(flyAction);
        flyAction.start(viewer3d.time.time());
        isFlying = true;
    }

    void startLookingAround() {
        isLookingAround = true;
    }

    void startOrbiting(Vec3d centerPoint) {
        isOrbiting    = true;
        orbitPoint    = centerPoint;
        orbitDistance =
            (float) Math.sqrt((centerPoint.x() - position().x())
                              * (centerPoint.x()
                                 - position().x()) + (centerPoint.z()
                                     - position().z()) * (centerPoint.z()
                                         - position().z()));
        lookAt(centerPoint, Vec3d.Y_AXIS);
    }

    void stopFlying() {
        flyAction.stop(viewer3d.time.time());
        flyAction = null;
        isFlying = false;
    }

    /**
     * Method description
     *
     */
    public void stopLookingAround() {
        isLookingAround = false;
    }

    /**
     * Method description
     *
     */
    public void stopOrbiting() {
        isOrbiting = false;

//      viewer3d.renderingWorkerThread.setDirty();
//        if (!isAnimating()) {
        viewer3d.canvas.repaint();
//        }
    }

    /**
     * Returns the camera "name"
     *
     * @return
     */
    public String toString() {
        return cameraName;
    }

    /**
     * Method description
     *
     */
    public void update() {
        oglStateDirty = true;

//      viewer3d.renderingWorkerThread.setDirty();
//        if (!isAnimating()) {
        viewer3d.canvas.repaint();
//        }
    }

    /**
     * Sets the OpenGL projection matrix according to the camera
     * parameters if they have changed since the last time this routine
     * was called or if the "force" argument is true. Matrix mode is
     * set to GL_PROJECTION during the operation and set to
     * GL_MODELVIEW afterward.
     */
    void updateOpenGL(GL gl, GLU glu, boolean force) {
        if (oglStateDirty || force) {
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();
            glu.gluPerspective(MathUtils.rad2dec(fov), aspect, nearClip,
                               farClip);

            // Read the matrix back for later queries to the viewing matrix
            // for view frustum culling
            gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projdata, 0);

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    projMat.set(i, j, (float) projdata[4 * j + i]);
                }
            }

            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glScalef(1, 1, -1);
            oglStateDirty = false;
        }
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public GLDrawable getGLDrawable() {
        return viewer3d.canvas;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int getMode() {
        return mode;
    }

    /**
     * Takes the local origin in double-precision coordinates and
     * returns a camera (or "modelview") matrix containing orientation
     * and position in single-precision coordinates.
     *
     * @param localOrigin
     * @param dest
     */
    public void getModelViewMatrix(Vec3d localOrigin, Mat4d dest) {
        dest.makeIdent();

        Vec3d pos = cartesianPosition.minus(localOrigin);

        dest.setTranslation(pos.x(), pos.y(), -pos.z());
        dest.setRotation(orientation);
        dest.invertRigid();

        // Following code is used to invert the z axis, so as to create a left-handed coordination system
        dest.set(0, 2, -dest.get(0, 2));
        dest.set(1, 2, -dest.get(1, 2));
        dest.set(2, 2, -dest.get(2, 2));
        dest.set(3, 2, -dest.get(3, 2));
    }

/// need to get/set real angles to camera and not only orientations

    /**
     * Method description
     *
     *
     * @return
     */
    public float getPitchAngle() {
        return pitchAngle;
    }

    /**
     * Returns the projection matrix for the camera in the dest Mat4d
     *
     * @param dest
     */
    public void getProjectionMatrix(Mat4d dest) {
        Mat4d ident = new Mat4d();

        ident.makeIdent();
        dest.mul(ident, projMat);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public float getRollAngle() {
        return rollAngle;
    }

    /**
     * Gets the camera view window height
     *
     * @return
     */
    public int getViewHeight() {
        return viewer3d.canvas.getHeight();
    }

    /**
     * Returns the viewinfo
     *
     * @return
     */
    public ViewInfo getViewInfo() {
        return viewInfo;
    }

    /**
     * Gets the camera view window width
     *
     * @return
     */
    public int getViewWidth() {
        return viewer3d.canvas.getWidth();
    }

    /**
     * Takes a local origin in double-precision coordinates (specify
     * the origin for a world-coordinate matrix) and returns a full
     * viewing matrix (including the projection and modelview matrices)
     * in single-precision coordinates.
     *
     * @param localOrigin
     * @param dest
     */
    public void getViewingMatrix(Vec3d localOrigin, Mat4d dest) {
        getModelViewMatrix(localOrigin, tmpMat);
        dest.mul(projMat, tmpMat);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public float getYawAngle() {
        return yawAngle;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isFlying() {
        return isFlying;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isLookingAround() {
        return isLookingAround;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isMoving() {
        return isMoving || isOrbiting || isLookingAround
               || (isFlying &&!isFlyingPaused);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isOrbiting() {
        return isOrbiting;
    }


    //~--- set methods --------------------------------------------------------

    /**
     * Sets the aspect ratio (width / height)
     *
     * @param aspect
     */
    public void setAspectRatio(double aspect) {
        this.aspect   = aspect;
        oglStateDirty = true;
    }

    /**
     * Sets the distance of the far clipping plane
     *
     * @param farClip
     */
    public void setFarClip(double farClip) {
        this.farClip  = farClip;
        oglStateDirty = true;
    }

    /**
     * Sets the vertical field of view
     *
     * @param fov
     */
    public void setFovY(double fov) {
        this.fov      = fov;
        oglStateDirty = true;
    }

    /**
     * Method description
     *
     *
     * @param mode
     */
    public void setMode(int mode) {
        if ((mode == Camera.NORMAL_MODE) || (mode == Camera.DEBUG_MODE)) {
            this.mode = mode;
        }
    }

    /**
     * Sets the distance of the near clipping plane
     *
     * @param nearClip
     */
    public void setNearClip(double nearClip) {
        this.nearClip = nearClip;
        oglStateDirty = true;
    }

    private void setOrientation(Rot orientation) {
        if (orientation.equals(this.orientation)) {
            return;
        }

        this.orientation.set(orientation);

        // and update angle variables..... yaw/pitch/rollAngle
        Vec3d angles = orientation.getAngles();

        pitchAngle = (float) angles.x();
        yawAngle   = (float) angles.y();
        rollAngle  = (float) angles.z();

        // Not Roll capabilities (yet) ...
        // rollAngle=0;//(float) orientation.getAngles().z()/(float) (360*Math.PI*2);
        fireOrientationChanged(new CameraEvent(this,
                CameraEvent.POSITION_CHANGED, position(), orientation()));

//      viewer3d.renderingWorkerThread.setDirty();
        viewer3d.canvas.repaint();
    }

    /**
     * Sets the camera orientation using pitch, yaw, roll angles
     *
     * @param pitchAngle
     * @param yawAngle
     * @param rollAngle
     */
    public void setOrientation(float pitchAngle, float yawAngle,
                               float rollAngle) {
        if ((this.pitchAngle == pitchAngle) && (this.rollAngle == rollAngle)
                && (this.yawAngle == yawAngle)) {
            return;
        }

        roll.set(Vec3d.Z_AXIS, rollAngle);    // roll - angle round x axis
        pitch.set(Vec3d.X_AXIS, pitchAngle);    // pitch - angle round z axis (in our case phi)
        yaw.set(Vec3d.Y_AXIS, yawAngle);    // yaw - angle round y axis (in our case theta)
        setOrientation(yaw.times(pitch).times(roll));
    }

    public Vec3d getForwardVector(){
        Mat4d modelView = new Mat4d();
        Mat4d projection = new Mat4d();
        
        getModelViewMatrix(new Vec3d(0, 0, 0), modelView);
        getProjectionMatrix(projection);

        Vec3d objCoords = ProjectionTransform.screenToSpaceCoordinates(new Vec2i(getViewWidth()/2, getViewHeight()/2), 
                                          modelView, 
                                          projection, 
                                          new Dimension(getViewWidth(), getViewHeight()),
                                          0.75);
        objCoords.sub(position());
        objCoords.normalize();
        return objCoords;
    }
    
    public Vec3d getUpVector(){
        Vec3d up = new Vec3d(Vec3d.Y_AXIS);
        Rot r=new Rot(Vec3d.Y_AXIS,rollAngle);
        Vec3d rotUp = r.rotateVector(up);
        rotUp.normalize();
        return rotUp;
    }
    
    public Vec3d getRightVector(){
        Vec3d fwdVector = getForwardVector();
        Vec3d upVector = getUpVector();
        return fwdVector.cross(upVector);    
    }
    
    /**
     * Sets the camera position
     *
     * @param position
     */
    public void setPosition(Vec3d position) {
        if (position.equals(cartesianPosition)) {
            return;
        }
        this.cartesianPosition.set(position);

        for (int i=0;i<viewer3d.objectsToView.size();i++){
            Object3D obj3d = viewer3d.objectsToView.get(i);
            tempPos.set(position);
            if (obj3d instanceof Collidable) {
                tempPos.set(((Collidable) obj3d).getLegalPosition(position));
                this.cartesianPosition.set(tempPos);
            }

            if (obj3d instanceof Restrainer) {
                tempPos = ((Restrainer) obj3d).getLegalRestrainPosition(tempPos);
                this.cartesianPosition.set(tempPos);
            }
        }

        firePositionChanged(new CameraEvent(this,
                CameraEvent.POSITION_CHANGED, position(), orientation()));

        // viewer3d.renderingWorkerThread.setDirty();
        viewer3d.canvas.repaint();
        
    }

    public void rotate(Vec3d rotationCenter, double pitchAngle, double yawAngle){
        // Avoid "gimball lock"-like rotations
        if (Math.abs(pitchAngle)>=(Math.PI/2-0.001))
            pitchAngle = (float) (pitchAngle>0?1:-1*(Math.PI/2-0.001));
        if (Math.abs(pitchAngle)<=0.001 || pitchAngle>0)
            pitchAngle = 0;
        double rotatingOrbit = MathUtils.distance(position(),rotationCenter);
        double orbitVerticalDistance = Math.abs(rotatingOrbit*Math.cos(pitchAngle));
        
        Vec3d pos = new Vec3d(rotationCenter.x()+orbitVerticalDistance*Math.sin(yawAngle), 
                              Math.abs(-rotationCenter.y()+rotatingOrbit*Math.sin(pitchAngle)), 
                              rotationCenter.z()-orbitVerticalDistance*Math.cos(yawAngle));
        setupCamera(pos, new Vec3d(pitchAngle, yawAngle, 0.0));
    }
    
    
    public void pinpointPosition(Vec3d position){
        this.pinpointPosition = position;
        pinpointStartTime = System.nanoTime();
        pinpointTimer.start();

    }
    //~--- inner classes ------------------------------------------------------

    // for position

    /** A small class, used to bind an event with a timestamp */
    private class EventTimeStampBind {
        CameraEvent event;
        long        nanosecTimeStamp;

        //~--- constructors ---------------------------------------------------

        EventTimeStampBind(CameraEvent e, long nanosecTimeStamp) {
            this.event            = e;
            this.nanosecTimeStamp = nanosecTimeStamp;
        }
    }


    /** and its comparator */
    private class EventTimeStampBindComparator implements Comparator {

        /**
         * Method description
         *
         *
         * @param o1
         * @param o2
         *
         * @return
         */
        public int compare(Object o1, Object o2) {
            EventTimeStampBind b1 = (EventTimeStampBind) o1;
            EventTimeStampBind b2 = (EventTimeStampBind) o2;

            if (b1.nanosecTimeStamp < b2.nanosecTimeStamp) {
                return -1;
            } else if (b1.nanosecTimeStamp > b2.nanosecTimeStamp) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
