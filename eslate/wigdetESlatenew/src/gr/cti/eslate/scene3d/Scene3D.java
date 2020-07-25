/*
 * Created on 29 Ìבת 2006
 *
 */
package gr.cti.eslate.scene3d;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.protocol.IObject3D;
import gr.cti.eslate.protocol.IViewer3D;
import gr.cti.eslate.scene3d.viewer.*;
import gr.cti.eslate.scene3d.viewer.event.CameraListener;
import gr.cti.eslate.sharedObject.NumberSO;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.KeyDoesntExistException;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.eslate.math.linalg.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.media.opengl.GL;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Scene3D extends JPanel implements ESlatePart, Externalizable, IViewer3D{

    private ESlateHandle handle = null;
    private Viewer3D viewer3D;
    private boolean plugsCreated = false;
    private NumberSO hAngleSO, vAngleSO, xPosSO, yPosSO, zPosSO;
    SharedObjectPlug plug;
    private static final int FORMAT_VERSION = 1;
    private static ResourceBundle bundleMessages;
    private ProtocolPlug viewerPlug;
    private HashMap<IObject3D, Object3DBind> obj3DHash = new HashMap<IObject3D,Object3DBind>();
    private CartesianSystemHelper cartesianSystemHelper;
    boolean cartesianHelperVisible = true;
    private Vec3d boundingBoxDimensions = new Vec3d(10000,10000,10000);
    
    static final long serialVersionUID = -10L;
    private final static String version = "1.0.8";

    public Scene3D(){
        super();
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.scene3d.BundleMessages", Locale.getDefault());
        setLayout(new BorderLayout());
        add(viewer3D = new Viewer3D());
        setBorder(BorderFactory.createEtchedBorder());
        cartesianSystemHelper = new CartesianSystemHelper(this);
        viewer3D.addObject3D(cartesianSystemHelper);
        viewer3D.getActiveCamera().setNearClip(100);
        viewer3D.getActiveCamera().setFarClip(1000000);
        setPreferredSize(new Dimension(400,400));
    }
    
    public Vec3d getCameraPosition(){
        return viewer3D.getActiveCamera().position();
    }
    
    public void setCameraPosition(Vec3d position){
        viewer3D.getActiveCamera().setPosition(position);
    }
    
    public Vec3d getCameraOrientation(){
        return viewer3D.getActiveCamera().orientation();
    }
    
    public void setCameraOrientation(Vec3d orientation){
        viewer3D.getActiveCamera().setOrientation((float) MathUtils.dec2rad(orientation.x()), 
                                                  (float) MathUtils.dec2rad(orientation.y()), 
                                                  (float) MathUtils.dec2rad(orientation.z()));
    }
    
    public void addCameraListener(CameraListener listener){
        viewer3D.getActiveCamera().addCameraListener(listener);
    }
    
    public void removeCameraListener(CameraListener listener){
        viewer3D.getActiveCamera().removeCameraListener(listener);
    }
    
    public double getCameraPitchAngle(){
        return viewer3D.getActiveCamera().getPitchAngle();
    }
    
    public void setCameraPitchAngle(double angle){
        Camera c = viewer3D.getActiveCamera();
        c.setOrientation((float) angle, c.getYawAngle(), c.getRollAngle());
    }
    
    public double getCameraYawAngle(){
        return viewer3D.getActiveCamera().getYawAngle();
    }
    
    public void setCameraYawAngle(double angle){
        Camera c = viewer3D.getActiveCamera();
        c.setOrientation(c.getPitchAngle(),(float) angle, c.getRollAngle());
    }
    
    public void setCartesianHelperVisible(boolean visible){
        viewer3D.repaint();
        cartesianHelperVisible = visible;
    }
    
    public boolean isCartesianHelperVisible(){
        return cartesianHelperVisible;
    }
    
    /* (non-Javadoc)
     * @see gr.cti.eslate.base.ESlatePart#getESlateHandle()
     */
    public ESlateHandle getESlateHandle() {
        if (handle == null) {

            handle = ESlate.registerPart(this);
            try {
                handle.setUniqueComponentName(bundleMessages.getString("Viewer3D"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            //handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.ButtonPrimitives");
            handle.setInfo(getInfo());

            handle.addESlateListener(new ESlateAdapter() {
                    public void handleDisposed(HandleDisposalEvent e) {
                        
                    }
                }
            );

            // When handle is created, there is no need for plugs to be created too. If the component is created by
            // a user, then the user is responsible to use the right property method (or editor) to create plus. Plug state is
            // stored and retrieved with the component's state.

            setPlugsUsed(true);

        }
        return handle;
    }
    
    /**
     * Returns Copyright information.
     * @return  The Copyright information.
     */
    private ESlateInfo getInfo() {
        String[] info = {
                bundleMessages.getString("part"),
                bundleMessages.getString("development"),
                bundleMessages.getString("copyright")
            };

        return new ESlateInfo(
                bundleMessages.getString("componentName") + " " +
                bundleMessages.getString("version") + " " + version,
                info);
    }
    
    public void setPlugsUsed(boolean create) {
        if (handle != null) {
            if (create)
                createPlugs();
            else
                destroyPlugs();
            plugsCreated = create;
        } else
            plugsCreated = false;
    }

    public boolean getPlugsUsed() {
        return plugsCreated;
    }
    
    private void createPlugs() {
        if (handle == null)
            return;
            hAngleSO = new NumberSO(handle, 0);
            vAngleSO = new NumberSO(handle, 0);
            xPosSO = new NumberSO(handle, 0);
            yPosSO = new NumberSO(handle, 0);
            zPosSO = new NumberSO(handle, 0);
            
        try {
            // from an object3D
            Class protocol=Class.forName("gr.cti.eslate.protocol.IObject3D");
            
            viewerPlug=new LeftMultipleConnectionProtocolPlug(handle,bundleMessages,"Viewer3D",new Color(220,107,154),protocol);
            viewerPlug.setHostingPlug(true);
            viewerPlug.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent e) {
                    //When connected...
                    IObject3D obj3D = (IObject3D) ((ProtocolPlug)e.getPlug()).getProtocolImplementor();
                    addIObject3D(obj3D);
                    
                }
            });
            viewerPlug.addDisconnectionListener(new DisconnectionListener() {
                public void handleDisconnectionEvent(DisconnectionEvent e) {
                    //When disconnected...
                    IObject3D obj3D = (IObject3D) ((ProtocolPlug)e.getPlug()).getProtocolImplementor();
                    removeIObject3D(obj3D);
                }
            });
            handle.addPlug(viewerPlug);
            
            
            SharedObjectListener so1 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        Camera camera = viewer3D.getActiveCamera();
                        viewer3D.getActiveCamera().setOrientation(camera.getPitchAngle(),
                                (float) (2*Math.PI-MathUtils.dec2rad(((NumberSO) e.getSharedObject()).value().floatValue())),
                                camera.getRollAngle());
                    }
                };

            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "HorizontalAngle", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        hAngleSO, so1);
            plug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                            Camera camera = viewer3D.getActiveCamera();
                            camera.setOrientation(camera.getPitchAngle(),
                                    (float) (2*Math.PI-MathUtils.dec2rad(so.value().floatValue())),
                                    camera.getRollAngle());
                        }
                    }
                }
            );
            handle.addPlug(plug);
            
            SharedObjectListener so2 = new SharedObjectListener() {
                public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                    Camera camera = viewer3D.getActiveCamera();
                    camera.setOrientation(
                            (float) (2*Math.PI-MathUtils.dec2rad(((NumberSO) e.getSharedObject()).value().floatValue())),
                            camera.getYawAngle(),
                            camera.getRollAngle());
                }
            };

            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "VerticalAngle", new Color(135, 206, 250),
                    gr.cti.eslate.sharedObject.NumberSO.class,
                    vAngleSO, so2);
            plug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                            Camera camera = viewer3D.getActiveCamera();
                            camera.setOrientation(
                                    (float) (2*Math.PI-MathUtils.dec2rad(so.value().floatValue())),
                                    camera.getYawAngle(),
                                    camera.getRollAngle());
                        }
                    }
                }
            );
            handle.addPlug(plug);
            
            SharedObjectListener so3 = new SharedObjectListener() {
                public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                    Camera camera = viewer3D.getActiveCamera();
                    Vec3d pos = camera.position();
                    pos.set(((NumberSO) e.getSharedObject()).value().doubleValue(),pos.y(),pos.z());
                    camera.setPosition(pos);
                           
                }
            };

            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "XPos", new Color(135, 206, 250),
                    gr.cti.eslate.sharedObject.NumberSO.class,
                    xPosSO, so3);
            plug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                            Camera camera = viewer3D.getActiveCamera();
                            Vec3d pos = camera.position();
                            pos.set(so.value().doubleValue(),pos.y(),pos.z());
                            camera.setPosition(pos);
                        }
                    }
                }
            );
            handle.addPlug(plug);
            
            SharedObjectListener so4 = new SharedObjectListener() {
                public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                    Camera camera = viewer3D.getActiveCamera();
                    Vec3d pos = camera.position();
                    pos.set(pos.x(),((NumberSO) e.getSharedObject()).value().doubleValue(),pos.z());
                    camera.setPosition(pos);
                }
            };

            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "YPos", new Color(135, 206, 250),
                    gr.cti.eslate.sharedObject.NumberSO.class,
                    yPosSO, so4);
            plug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                            Camera camera = viewer3D.getActiveCamera();
                            Vec3d pos = camera.position();
                            pos.set(pos.x(),so.value().doubleValue(),pos.z());
                            camera.setPosition(pos);
                        }
                    }
                }
            );
            handle.addPlug(plug);
            
            SharedObjectListener so5 = new SharedObjectListener() {
                public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                    Camera camera = viewer3D.getActiveCamera();
                    Vec3d pos = camera.position();
                    pos.set(pos.x(),pos.y(),((NumberSO) e.getSharedObject()).value().doubleValue());
                    camera.setPosition(pos);
                }
            };

            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "ZPos", new Color(135, 206, 250),
                    gr.cti.eslate.sharedObject.NumberSO.class,
                    zPosSO, so5);
            plug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                            Camera camera = viewer3D.getActiveCamera();
                            Vec3d pos = camera.position();
                            pos.set(pos.x(),pos.y(),so.value().doubleValue());
                            camera.setPosition(pos);
                        }
                    }
                }
            );
        handle.addPlug(plug);
        } catch (InvalidPlugParametersException e) {
            e.printStackTrace();
        } catch (PlugExistsException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void destroyPlugs() {
        
    }
    
    private void addIObject3D(IObject3D obj3d){     
        Object3DBind obj3DBind = new Object3DBind(obj3d);
        obj3DHash.put(obj3d, obj3DBind);

        viewer3D.removeObject3D(cartesianSystemHelper);
        
        viewer3D.addObject3D(obj3DBind);
        viewer3D.addObject3D(cartesianSystemHelper);
        System.out.println("Added object");
    }
    
    private void removeIObject3D(IObject3D obj3d){
        Object3DBind obj3DBind = obj3DHash.get(obj3d);
        viewer3D.removeObject3D(obj3DBind);
        obj3DHash.remove(obj3d);
    }
    
    /* (non-Javadoc)
     * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Object firstObj = in.readObject();

        StorageStructure fieldMap = (StorageStructure) firstObj;
        try{
            Vec3d pos = new Vec3d(fieldMap.getDouble("posX"),
                                  fieldMap.getDouble("posY"),
                                  fieldMap.getDouble("posZ"));
            viewer3D.getActiveCamera().setPosition(pos);
            
            float pitch = fieldMap.getFloat("pitch");
            float yaw = fieldMap.getFloat("yaw");
            float roll = fieldMap.getFloat("roll");
            viewer3D.getActiveCamera().setOrientation(pitch,yaw,roll);
            setCartesianHelperVisible(fieldMap.getBoolean("CartesianHelperVisible"));      
        }catch (KeyDoesntExistException exc){
            exc.printStackTrace();
        }        
    }

    /* (non-Javadoc)
     * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        Camera c = viewer3D.getActiveCamera();
        Vec3d position = c.position();
        fieldMap.put("posX", position.x());
        fieldMap.put("posY", position.y());
        fieldMap.put("posZ", position.z());

        fieldMap.put("pitch", c.getPitchAngle());
        fieldMap.put("yaw", c.getYawAngle());
        fieldMap.put("roll", c.getRollAngle());

        fieldMap.put("CartesianHelperVisible", isCartesianHelperVisible());

        out.writeObject(fieldMap);          
    }
        
    class Object3DBind extends gr.cti.eslate.scene3d.viewer.Object3D{
        
        IObject3D obj3d;
        Object3DBind(IObject3D obj3d){
            this.obj3d = obj3d;
        }
        boolean initialized = false;
        /* (non-Javadoc)
         * @see gr.talent.viewer3d.Object3D#dispose()
         */
        @Override
        public void dispose() {
            // TODO Auto-generated method stub
            
        }

        /* (non-Javadoc)
         * @see gr.talent.viewer3d.Object3D#initialize(gr.talent.viewer3d.GLHandle)
         */
        @Override
        public void initialize(GLHandle arg0) {            
        }

        /* (non-Javadoc)
         * @see gr.talent.viewer3d.Object3D#render(gr.talent.viewer3d.GLHandle)
         */
        @Override
        public int render(GLHandle glHandle) {
            if (!initialized){
                initialize(glHandle);
                initialized = true;
            }
            
            if (!((gr.cti.eslate.object3D.Object3D) obj3d).isVisible())
                return 0;
            if (obj3d.getObjectModel()==null)
                return 0;
            Camera camera = glHandle.getCamera();
            GL gl = glHandle.getGL();
            gl.glEnable(GL.GL_LIGHTING);
            gl.glEnable(GL.GL_LIGHT0);
            float[] mat_shininess ={50.0f};
            float[] mat_specular = new float[]{ 1.0f, 0.0f, 0.0f, 0.0f };
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, mat_specular, 0);  
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, mat_specular, 0);  
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, mat_specular, 0);  
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, mat_shininess, 0);
            gl.glPushMatrix();
            Vec3d pos = obj3d.getPosition();
            Vec3d orientation = obj3d.getOrientation();
            Vec3d scale = obj3d.getScale();
            gl.glTranslated(pos.x(), pos.y(), pos.z());
            
            gl.glRotated(orientation.y(),0,1,0);
            gl.glRotated(orientation.x(),1,0,0);
            gl.glRotated(orientation.z(),0,0,1);
            gl.glScaled(scale.x(),scale.y(),scale.z());
            ((Object3D) obj3d.getObjectModel()).render(glHandle);
            gl.glPopMatrix();
            gl.glDisable(GL.GL_LIGHTING);
            gl.glDisable(GL.GL_LIGHT0);
            gl.glDisable(GL.GL_BLEND);
            gl.glDisable(GL.GL_CULL_FACE);
            return 0;
        }

        /* (non-Javadoc)
         * @see gr.talent.viewer3d.Object3D#renderStateChanged()
         */
        @Override
        public void renderStateChanged() {
            // TODO Auto-generated method stub
            super.renderStateChanged();
        }
        
        
    }


    /* (non-Javadoc)
     * @see gr.cti.eslate.protocol.IViewer3D#refresh()
     */
    public void refresh() {
        // TODO Auto-generated method stub
        if (viewer3D != null){
            viewer3D.repaint();
        }
    }
    
    public Vec3d getBoundingBoxDimensions(){
        return boundingBoxDimensions; 
    }
    
    public void setBoundingBoxDimensions(Vec3d dimensions){
        boundingBoxDimensions.set(dimensions); 
    }
}
