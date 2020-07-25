package gr.cti.eslate.object3D;

import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.ProtocolPlug;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.RightMultipleConnectionProtocolPlug;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.SingleInputMultipleOutputPlug;
import gr.cti.eslate.base.SingleInputPlug;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.math.linalg.Vec3d;
import gr.cti.eslate.object3D.loaders.obj.ObjFile;
import gr.cti.eslate.object3D.loaders.obj.ObjModel;
import gr.cti.eslate.protocol.IObject3D;
import gr.cti.eslate.protocol.IViewer3D;
import gr.cti.eslate.sharedObject.NumberSO;
import gr.cti.eslate.sharedObject.StringSO;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.KeyDoesntExistException;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Color;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This is a component for ESlate2, a simple encapsulator for Wavefront(.obj) files rendering.
 * File: Object3D.java, original project: Object3D
 * @author mantesat
 * @since 7 Ιουν 2006 , 12:16:11 μμ
 */
public class Object3D implements ESlatePart, Externalizable, IObject3D{
    
    private Vec3d position = new Vec3d();
    private Vec3d orientation = new Vec3d();
    private Vec3d scale = new Vec3d(1,1,1);
    private String filePath;
    private ESlateHandle handle;
    private ProtocolPlug viewerPlug;
    private static ResourceBundle bundleMessages;
    private ObjModel objModel;
    private ObjFile  objFile;
    SharedObjectPlug plug;
    private IViewer3D viewer3D;
    private NumberSO xposSO, yposSO, zposSO, pitchAngleSO, yawAngleSO, rollAngleSO, scalexSO, scaleySO, scalezSO;
    private StringSO filePathSO;
    static final long serialVersionUID = -10L;
    private final static String version = "1.0.8";
    private boolean visible = true;
    private static final int FORMAT_VERSION = 1;
    
    public Object3D(){
    }
    
    public gr.cti.eslate.scene3d.viewer.Object3D getObjectModel(){
        return objModel;
    }
    
    public void loadModelFromFile(String path){
        
        if (path ==null || path.length()==0)
            return;
        // destroy old model resources
        if (objModel!= null)
            objModel.dispose();
        File f = new File(path);
        if (!f.exists()){
            // try relative path case
            try{
                path = new URL(handle.getESlateMicroworld().getDocumentBase(),path).toString();
//                s.replaceAll("/", File.pathSeparator);
//                s.replaceAll("\\", File.pathSeparator);
                path = path.substring(6,path.length());
                f = new File(path);
            }catch(MalformedURLException exc){
                filePath=null;
                return;
            }
        }

        if (!f.exists() || (f.exists() && f.isDirectory())){
            // sorry, not able to find the file...
            filePath=null;
            return;
        }
        
        ObjFile of = new ObjFile();
        of.load(path);
        filePath = path;
        if (objModel==null)
            objModel = new ObjModel(of);
        else
            objModel.setObjFile(of);
        
    }
    
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
    
    /* (non-Javadoc)
     * @see gr.cti.eslate.base.ESlatePart#getESlateHandle()
     */
    public ESlateHandle getESlateHandle() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.object3D.BundleMessages", Locale.getDefault());
        // TODO Auto-generated method stub
        if (handle==null){
            handle = ESlate.registerPart(this);
            xposSO = new NumberSO(handle,0);
            yposSO = new NumberSO(handle,0);
            zposSO = new NumberSO(handle,0);
            pitchAngleSO = new NumberSO(handle,0);
            yawAngleSO = new NumberSO(handle,0);
            rollAngleSO = new NumberSO(handle,0);
            scalexSO = new NumberSO(handle,0);
            scaleySO = new NumberSO(handle,0);
            scalezSO = new NumberSO(handle,0);
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.Object3DPrimitives");
            try {
                handle.setUniqueComponentName(bundleMessages.getString("Object3D"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            
            handle.setInfo(getInfo());
        
        
            //To a host
            try {
                Class protocol=Class.forName("gr.cti.eslate.protocol.IViewer3D");
                viewerPlug=new RightMultipleConnectionProtocolPlug(handle,bundleMessages,"Object3D",new Color(220,107,154),protocol);
                viewerPlug.setHostingPlug(false);
                viewerPlug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        //When connected...
                        viewer3D = (IViewer3D) ((ProtocolPlug)e.getPlug()).getProtocolImplementor();
                    }
                });
                viewerPlug.addDisconnectionListener(new DisconnectionListener() {
                    public void handleDisconnectionEvent(DisconnectionEvent e) {
                        //When disconnected...
                        viewer3D = null;
                    }
                });
                handle.addPlug(viewerPlug);
    
            
                SharedObjectListener so1 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        position.setX(((NumberSO) e.getSharedObject()).value().doubleValue());
                        if (viewer3D!=null)
                            viewer3D.refresh();
                        
                    }
                };
        
                plug = new SingleInputMultipleOutputPlug(handle, bundleMessages, "XPos", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        xposSO, so1);
                plug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                position.setX(so.value().doubleValue());
                                if (viewer3D!=null)
                                    viewer3D.refresh();
                            }
                        }
                    }
                );
                handle.addPlug(plug);
                
                SharedObjectListener so2 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        position.setY(((NumberSO) e.getSharedObject()).value().doubleValue());
                        if (viewer3D!=null)
                            viewer3D.refresh();
                    }
                };
        
                plug = new SingleInputMultipleOutputPlug(handle, bundleMessages, "YPos", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        yposSO, so2);
                plug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                position.setY(so.value().doubleValue());
                                if (viewer3D!=null)
                                    viewer3D.refresh();
                            }
                        }
                    }
                );
                handle.addPlug(plug);
                
                SharedObjectListener so3 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        position.setZ(((NumberSO) e.getSharedObject()).value().doubleValue());
                        if (viewer3D!=null)
                            viewer3D.refresh();
                    }
                };
        
                plug = new SingleInputMultipleOutputPlug(handle, bundleMessages, "ZPos", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        zposSO, so3);
                plug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                position.setZ(so.value().doubleValue());
                                if (viewer3D!=null)
                                    viewer3D.refresh();
                            }
                        }
                    }
                );
                handle.addPlug(plug);
                
                SharedObjectListener so4 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        orientation.setX(((NumberSO) e.getSharedObject()).value().doubleValue());
                        if (viewer3D!=null)
                            viewer3D.refresh();
                    }
                };
        
                plug = new SingleInputMultipleOutputPlug(handle, bundleMessages, "PitchAngle", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        pitchAngleSO, so4);
                plug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                orientation.setX(so.value().doubleValue());
                                if (viewer3D!=null)
                                    viewer3D.refresh();
                            }
                        }
                    }
                );
                handle.addPlug(plug);
                
                SharedObjectListener so5 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        orientation.setY(((NumberSO) e.getSharedObject()).value().doubleValue());
                        if (viewer3D!=null)
                            viewer3D.refresh();
                    }
                };
        
                plug = new SingleInputMultipleOutputPlug(handle, bundleMessages, "YawAngle", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        yawAngleSO, so5);
                plug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                orientation.setY(so.value().doubleValue());
                                if (viewer3D!=null)
                                    viewer3D.refresh();
                            }
                        }
                    }
                );
                handle.addPlug(plug);
                
                SharedObjectListener so6 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        orientation.setZ(((NumberSO) e.getSharedObject()).value().doubleValue());
                        if (viewer3D!=null)
                            viewer3D.refresh();
                    }
                };
        
                plug = new SingleInputMultipleOutputPlug(handle, bundleMessages, "RollAngle", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        rollAngleSO, so6);
                plug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                orientation.setZ(so.value().doubleValue());
                                if (viewer3D!=null)
                                    viewer3D.refresh();
                            }
                        }
                    }
                );
                handle.addPlug(plug);
                
                SharedObjectListener so7 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        scale.setX(((NumberSO) e.getSharedObject()).value().doubleValue());
                        if (viewer3D!=null)
                            viewer3D.refresh();
                    }
                };
        
                plug = new SingleInputMultipleOutputPlug(handle, bundleMessages, "ScaleX", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        scalexSO, so7);
                plug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                scale.setX(so.value().doubleValue());
                                if (viewer3D!=null)
                                    viewer3D.refresh();
                            }
                        }
                    }
                );
                handle.addPlug(plug);
                
                SharedObjectListener so8 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        scale.setY(((NumberSO) e.getSharedObject()).value().doubleValue());
                        if (viewer3D!=null)
                            viewer3D.refresh();
                    }
                };
        
                plug = new SingleInputMultipleOutputPlug(handle, bundleMessages, "ScaleY", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        scaleySO, so8);
                plug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                scale.setY(so.value().doubleValue());
                                if (viewer3D!=null)
                                    viewer3D.refresh();
                            }
                        }
                    }
                );
                handle.addPlug(plug);
                
                SharedObjectListener so9 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        scale.setZ(((NumberSO) e.getSharedObject()).value().doubleValue());
                        if (viewer3D!=null)
                            viewer3D.refresh();
                    }
                };
        
                plug = new SingleInputMultipleOutputPlug(handle, bundleMessages, "ScaleZ", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        scalezSO, so9);
                plug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                scale.setZ(so.value().doubleValue());
                                if (viewer3D!=null)
                                    viewer3D.refresh();
                            }
                        }
                    }
                );
                handle.addPlug(plug);
                
                SharedObjectListener so10 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        loadModelFromFile(((StringSO) e.getSharedObject()).getString());
                        if (viewer3D!=null)
                            viewer3D.refresh();
                    }
                };
        
                plug = new SingleInputPlug(handle, bundleMessages, "3DModelFilePath", new Color(139,117,0),
                        gr.cti.eslate.sharedObject.StringSO.class,so10);
                plug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                StringSO so = (StringSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                loadModelFromFile(so.getString());
                                if (viewer3D!=null)
                                    viewer3D.refresh();
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

        return handle;
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
            setPosition(pos);
            
            Vec3d orientation = new Vec3d(fieldMap.getDouble("pitch"),
                    fieldMap.getDouble("yaw"),
                    fieldMap.getDouble("roll"));
            setOrientation(orientation);
            Vec3d scale = new Vec3d(fieldMap.getDouble("scaleX"),
                    fieldMap.getDouble("scaleY"),
                    fieldMap.getDouble("scaleZ"));
            setScale(scale);
            if (fieldMap.containsKey("File")){
                String file = fieldMap.getString("File");
                loadModelFromFile(file);
            }
            setVisible(fieldMap.getBoolean("Visible"));      
        }catch (KeyDoesntExistException exc){
            exc.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        fieldMap.put("posX", position.x());
        fieldMap.put("posY", position.y());
        fieldMap.put("posZ", position.z());

        fieldMap.put("pitch", orientation.x());
        fieldMap.put("yaw", orientation.y());
        fieldMap.put("roll", orientation.z());

        fieldMap.put("scaleX", scale.x());
        fieldMap.put("scaleY", scale.y());
        fieldMap.put("scaleZ", scale.z()); 
        fieldMap.put("File", filePath);
        fieldMap.put("Visible", visible);  

        out.writeObject(fieldMap);     
    }

    /* (non-Javadoc)
     * @see gr.cti.eslate.protocol.IObject3D#getOrientation()
     */
    public Vec3d getOrientation() {    
        return new Vec3d(orientation);
    }

    /* (non-Javadoc)
     * @see gr.cti.eslate.protocol.IObject3D#getPosition()
     */
    public Vec3d getPosition() {
        return position.copy();
    }

    /* (non-Javadoc)
     * @see gr.cti.eslate.protocol.IObject3D#getScale()
     */
    public Vec3d getScale() {
        return scale.copy();
    }
    
    public void setPosition(Vec3d position){
        this.position.set(position);
        if (viewer3D!=null)
            viewer3D.refresh();
    }
    
    public void setOrientation(Vec3d orientation){
        this.orientation.set(orientation);
        if (viewer3D!=null)
            viewer3D.refresh();
    }
    
    public void setScale(Vec3d scale){
        this.scale.set(scale);
        if (viewer3D!=null)
            viewer3D.refresh();
    }
    
    public void setVisible(boolean visible){
        this.visible = visible;
        if (viewer3D!=null)
            viewer3D.refresh();
    }
    
    public boolean isVisible(){
        return visible;
    }

}
