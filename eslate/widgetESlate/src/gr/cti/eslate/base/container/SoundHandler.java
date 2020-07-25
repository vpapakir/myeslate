package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.eslate.utils.sound.ESlateSound;
import gr.cti.eslate.utils.sound.ESlateSoundEvent;
import gr.cti.eslate.utils.sound.ESlateSoundListener;
import gr.cti.eslate.utils.sound.SoundUtils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Vector;


public class SoundHandler implements InvocationHandler, Externalizable {
    static final long serialVersionUID = 12;
    public static final int FORMAT_VERSION = 1;
    String methodName = "";
    SoundListener soundListener;
    ESlateContainer container;
long start;

    public SoundHandler(String methodName) {
        this.methodName = methodName;
    }

    public void setMethod(String methodName) {
        this.methodName = methodName;
    }

    public void setSoundListener(SoundListener listener) {
        this.soundListener = listener;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("SoundHandler PROXY METHOD: " + method.getName());
        if (method.getName().equals("equals")) {
            Object obj = args[0];
//            System.out.println(obj);
            if (obj == proxy)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        }
        if (method.getName().equals("hashCode")) {
            return new Integer(hashCode());
        }
        if (method.getName().equals("toString")) {
            return this.toString();
        }
        if (!methodName.equals(method.getName())) return null;

//System.out.println("SoundHandler PROXY METHOD: " + method.getName());
//System.out.println("soundListener: " + soundListener);// + ", soundListener.pathToSound: " + soundListener.pathToSound);
        ESlateSound sound = null;
        if (soundListener.embeddedFileName != null) {
            Vector path = new Vector();
            path.add("Sounds");
            path.add(soundListener.embeddedFileName);
start = System.currentTimeMillis();
            sound = gr.cti.eslate.utils.sound.SoundUtils.playSound(container.currentlyOpenMwdFile, container.currentlyOpenMwdFileName, path);
        }else{
start = System.currentTimeMillis();
            sound = SoundUtils.playSound(soundListener.pathToSound);
        }


        /* Don't add the sound for the MicroworldClosing event to the list of playing sounds.
         * This sound must keep playing when the microworld closes.
         */
        if (!(soundListener.methodName.equals("microworldClosing") && gr.cti.eslate.base.container.event.MicroworldListener.class.isAssignableFrom(soundListener.listenerClass)))
            container.soundListenerMap.addPlayingSound(sound);
        sound.addSoundListener(new ESlateSoundListener() {
            public void soundStopped(ESlateSoundEvent e) {
                System.out.println("ET: " + (System.currentTimeMillis()-start));
                container.soundListenerMap.removePlayingSound((ESlateSound) e.getSource());
            }
        });
        return null;
//        System.out.println("Javascript executing..." + method.getName());
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Method name", methodName);
        out.writeObject(fieldMap);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        methodName = (String) fieldMap.get("Method name");
//        initializeHandler();
    }

}
