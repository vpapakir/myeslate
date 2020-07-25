package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class SoundListener implements Externalizable {
    public static final int FORMAT_VERSION = 1;
    String soundName;
    String pathToSound;
    /* The name of the file in the strcut file that stores the sound for this SoundListener.
     * This field gets a value the first time the microworld is saved, after the sound
     * has been loaded to the microworld from some external source (file system).
     */
    String embeddedFileName;

    /* This field holds the value the 'embeddedFileName' had, when the microworld was loaded.
     * When the microworld is saved, the current value of 'embeddedFileName' is cheched against
     * the value of this field and if they differ, then a new sound has been attached to this
     * SoundListener. This means that the file of the previous sound will have to be removed from
     * the microworld file, and the file of the new sound has to be copied in it.
     */
    String previousEmbeddedFileName = null;

    Object listener;
    String methodName;
    HierarchicalComponentPath2 pathToComponent = null;
    static final long serialVersionUID = 12;
    SoundHandler soundHandler;
    Class listenerClass;

    /** This constructor is provided only for the Externalization mechanism. It shouldn't be
     *  used for constructing instances of this class.
     */
    public SoundListener() {
    }

    public SoundListener(String soundName, String pathToSound, String methodName, HierarchicalComponentPath2 pathToComponent, SoundHandler sh, Class listenerClass) {
        this.soundName = soundName;
        this.pathToSound = pathToSound;
        this.methodName = methodName;
        this.pathToComponent = pathToComponent;
        this.soundHandler = sh;
        if (sh.soundListener == null)
            sh.soundListener = this;
        this.listenerClass = listenerClass;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        soundName = (String) fieldMap.get("Sound name");
        pathToSound = (String) fieldMap.get("Sound path");
        embeddedFileName = (String) fieldMap.get("File name in struct file");
        previousEmbeddedFileName = embeddedFileName;
        methodName = (String) fieldMap.get("Method name");
        pathToComponent = (HierarchicalComponentPath2) fieldMap.get("Path to component");
//        soundHandler = (SoundHandler) fieldMap.get("Sound handler");
        soundHandler = new SoundHandler(methodName);
        soundHandler.soundListener = this;
        String listenerClassName = (String) fieldMap.get("Listener class name");
        try{
            listenerClass = Class.forName(listenerClassName);
            listener = java.lang.reflect.Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                              new Class[] { listenerClass },
                                              soundHandler);
        }catch (Throwable exc) {
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Sound name", soundName);
        fieldMap.put("Sound path", pathToSound);
        fieldMap.put("Method name", methodName);
        fieldMap.put("Path to component", pathToComponent);
//        fieldMap.put("Sound handler", soundHandler);
        fieldMap.put("Listener class name", listenerClass.getName());
        fieldMap.put("File name in struct file", embeddedFileName);
        previousEmbeddedFileName = embeddedFileName;
        out.writeObject(fieldMap);
//        System.out.println("SoundListener writeExternal() size: " + ESlateContainerUtils.getFieldMapContentLength(fieldMap));
    }

}