package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.eslate.utils.sound.ESlateSound;
import gr.cti.eslate.utils.sound.SoundUtils;
import gr.cti.structfile.Entry;
import gr.cti.structfile.StructFile;
import gr.cti.structfile.StructOutputStream;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.ObjectBaseArray;

import java.awt.Component;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.SwingUtilities;


public class SoundListenerMap {
    private ArrayList currentlyPlayingSounds = new ArrayList();
    private ArrayList soundListenerArray = new ArrayList();
    private transient ArrayList objectArray = new ArrayList();
    private transient ArrayList handleArray = new ArrayList();
    private transient ESlateContainer container;
//    private static final String STORAGE_FORMAT_VERSION = "1";
    private static final int STORAGE_FORMAT_VERSION = 2;

    SoundListener microworldOpenedSL = null;
    SoundListener microworldClosingSL = null;
    SoundListener componentIconifiedSL = null;
    SoundListener componentRestoredSL = null;
    SoundListener componentMaximizedSL = null;
    SoundListener componentActivatedSL = null;
    SoundListener componentDeactivatedSL = null;
    SoundListener componentClosingSL = null;
    SoundListener viewActivatedSL = null;

    public SoundListenerMap(ESlateContainer container) {
        this.container = container;
    }

    public void addSoundListener(Object object, SoundListener listener, ESlateHandle handle) {
//System.out.println("object: " + object);
//System.out.println("listener: " + listener);
//System.out.println("handle: " + handle);
        if (object == null || listener == null || handle == null) throw new NullPointerException("SoundListenerMap Cannot store soundListener cause one or more of 'object', 'listener', 'handle' is null");
        soundListenerArray.add(listener);
        objectArray.add(object);
        handleArray.add(handle);
    }


    public void clear() {
        /* clear() is called when the microworld is closing. Stop all the currently playing
         * sounds.
         */
//System.out.println("currentlyPlayingSounds.size(): " + currentlyPlayingSounds.size());
        for (int i=0; i<currentlyPlayingSounds.size(); i++)
            SoundUtils.stopSound((ESlateSound) currentlyPlayingSounds.get(i));
        currentlyPlayingSounds.clear();

        soundListenerArray.clear();
        objectArray.clear();
        handleArray.clear();
    }

    public int size() {
        return soundListenerArray.size();
    }

    public void removeSoundListener(SoundListener listener) {
        int index = soundListenerArray.indexOf(listener);
        if (index != -1) {
            soundListenerArray.remove(index);
            objectArray.remove(index);
            handleArray.remove(index);
        }
    }

    public boolean containsListenerFor(Class listenerClass, String methodName) {
        for (int i=0; i<soundListenerArray.size(); i++) {
            SoundListener sl = (SoundListener) soundListenerArray.get(i);
            if (sl.methodName.equals(methodName) && listenerClass.isAssignableFrom(sl.listenerClass))
                return true;
        }
        return false;
    }

    public void removeSoundListeners(ESlateHandle handle) {
        int index = handleArray.indexOf(handle);
        while (index != -1) {
            SoundListener sl = (SoundListener) soundListenerArray.get(index);
            soundListenerArray.remove(index);
            objectArray.remove(index);
            handleArray.remove(index);
            index = handleArray.indexOf(handle);
        }
    }

    /* Listener look-up service. The script listener look-up is based on the object for which
     * the listener is registered and not the path to the object. So for a example for an AWT
     * component which is both accesible from the component and the object hierarchy, the
     * script listener attached to it will be retrieved no matter through which path the object
     * is visited. Also this makes irrelevant the question which of the two paths should be saved
     * the onject's script listener.
     */
    public SoundListener[] getSoundListeners(Object obj) {
        if (obj == null) return null;
        if (objectArray.size() == 0) return new SoundListener[0];
        int objCount = 0;
        ObjectBaseArray array = new ObjectBaseArray();
        for (int i=0; i<objectArray.size(); i++) {
            if (obj == objectArray.get(i)) {
                array.add(soundListenerArray.get(i));
                objCount++;
            }
        }
        SoundListener[] sl = new SoundListener[objCount];
        for (int i=0; i<objCount; i++)
            sl[i] = (SoundListener) array.get(i);
        return sl;
    }

    public void putSoundListener(Object object, SoundListener listener, ESlateHandle handle) {
        if (object == null) return;
        if (listener == null || listener.methodName == null || handle == null)
            return;

        SoundListener oldListener = getSoundListener(object, listener.methodName);
        if (oldListener == null)
            addSoundListener(object, listener, handle);
        else{
            int index = soundListenerArray.indexOf(oldListener);
            soundListenerArray.set(index, listener);
        }
    }

    protected SoundListener getSoundListener(Object object, String methodName) {
        if (object == null) return null;
        if (methodName == null) return null;
//        System.out.println("objectArray.size(): " + objectArray.size());
        if (objectArray.size() == 0) return null;

        SoundListener soundListener = null;
//        System.out.println("getScriptListener():  " + object + ", methodName: " + methodName + ", inLogo: " + inLogo);
        SoundListener[] soundListeners = getSoundListeners(object);
        if (soundListeners != null) {
            for (int i=0; i<soundListeners.length; i++) {
//               System.out.println("scriptListeners[j].getMethodName(): " + scriptListeners[i].getMethodName());
                if (soundListeners[i].methodName.equals(methodName)) {
                    soundListener = soundListeners[i];
                    break;
                }
            }
        }
        return soundListener;
    }

    public int indexOf(SoundListener listener) {
        return soundListenerArray.indexOf(listener);
    }

    public Object getObject(int index) {
        return objectArray.get(index);
    }

    public SoundListener getSoundListener(int index) {
        return (SoundListener) soundListenerArray.get(index);
    }

    public ESlateHandle getHandle(int index) {
        return (ESlateHandle) handleArray.get(index);
    }

    /** Copies the sound files to the specified structured storage file and returns an
     *  ESlatefieldMap with the mapping between the component events and the sounds. The
     *  second argument provides the component to be saved. This component's sounds and
     *  all the sounds of the components which are nested in is are saved.
     */
    protected StorageStructure saveMap(StructFile targetFile, ESlateHandle componentHandle) {
        /* First check if there are any handles registered in the ScriptListenerMap
         * which are disposed. Handles like that may exists for second or lower level
         * components, which are disposed without E-Slate's knowledge. The scripts of
         * these  handles will be removed from the ScriptListenerMap, before it is
         * saved.
         */
        for (int i=0; i<objectArray.size(); i++) {
            ESlateHandle handle = (ESlateHandle) handleArray.get(i);
            if (handle.isDisposed()) {
                removeSoundListeners(handle);
                i--;
            }
        }

        /* Next gather the indices of the SoundListeners to be saved */
        IntBaseArray soundListenersIndices = new IntBaseArray();
        for (int i=0; i<handleArray.size(); i++) {
            ESlateHandle h = (ESlateHandle) handleArray.get(i);
            if (componentHandle == h ||componentHandle.hasChild(h))
                soundListenersIndices.add(i);
        }

//System.out.println("saveMap() objectArray.size(): " + objectArray.size());
        StringBuffer componentName = new StringBuffer();
        /* Check that all the objects which have been registered as script listener
         * holders. Any of these objects which are AWT components shall have their
         * script listeners saved using the AWT component path. This won't work for
         * AWT components which belong to AWT containers that host multiple components
         * one at a time. Such a container is the JTabbedPane.
         */
        for (int i=0; i<soundListenersIndices.size(); i++) {
            ESlateHandle handle = (ESlateHandle) handleArray.get(soundListenersIndices.get(i));
            SoundListener sl = (SoundListener) soundListenerArray.get(soundListenersIndices.get(i));
            Object obj = objectArray.get(soundListenersIndices.get(i));
//System.out.println("saveMap() obj: " + obj);
            if (Component.class.isAssignableFrom(obj.getClass()) &&
                SwingUtilities.getAncestorOfClass(javax.swing.JTabbedPane.class, (Component) obj) == null) {
//                System.out.println("saveMap() AWT case");
                Component comp = (Component) obj; //objectArray.get(i);
                int depth = 1;
                while (comp != null && !(handle.getComponent() == comp)) {
                    depth++;
                    comp = comp.getParent();
                }
                /* This may be an AWT component which is reached through some combination of AWT and
                 * object hierarchy and which does not belong (AWT-wise) to the top level E-Slate
                 * component, from which the path starts.
                 */
//                System.out.println("saveMap() comp: " + comp);
                if (comp == null) continue;
                comp = (Component) obj; //objectArray.get(i);

                /* Check if there exist any other components at the same parent as the component
                 * whose ScriptListener is saved, that are of the same class as the component.
                 * If there exist such components, then the name of the component will have to be
                 * altered, so that it includes the '_x' part, which differenciates it from the
                 * rest of the sibling components of the same class.
                 */
                if (depth > 1) {
                    Component[] siblings = comp.getParent().getComponents();
                    if (siblings != null) {
                        String extension = "";
                        int count = 1;
                        for (int k=0; comp != siblings[k]; k++) {
                            if (comp.getClass().getName().equals(siblings[k].getClass().getName())) {
                                extension = "_" + count;
                                count++;
                            }
                        }
                        String className = comp.getClass().getName();
                        if (className.indexOf('.') != -1)
                            className = className.substring(className.lastIndexOf('.')+1);
                        comp.setName(className+extension);
                    }
                }

                String[] path = new String[depth];
                int[] nodeType = new int[depth];
                while (comp != null && !(handle.getComponent() == comp)) { //(!ESlatePart.class.isAssignableFrom(comp.getClass()))) {
                    depth--;
                    /* The component path to be stored is formed by the names of the components that
                     * exist on the path of the AWT component hierarchy that leads to the component.
                     * So make sure that all these components on the path do have a name. If they don't
                     * have a name, give them one. This is done during microworld saving and the component
                     * names are only used during the microworld loading. So any changes to the component
                     * names during the microworld session do not cause any harm.
                     */
                    if (comp.getName() == null || comp.getName().trim().length() == 0)
                        HierarchicalComponentPath.nameComponent(comp);
                    path[depth] = comp.getName();
                    nodeType[depth] = HierarchicalComponentPath2.AWT_COMPONENT_NAME;
//                    System.out.println("Name: " + comp.getName() + ", comp: " + comp.getClass());

                    componentName.insert(0, comp.getName());
                    componentName.insert(0, '$');
                    comp = comp.getParent();
                }
                if (comp == null) continue;
                path[0] = handle.getComponentName();
                nodeType[0] = HierarchicalComponentPath2.AWT_COMPONENT_NAME;
                componentName.insert(0, handle.getComponentName());

                /* Name the scriptListener after the component's Java Component hierarchy. The top
                 * node in this hierarchy is the containing object's handle name. Then save the
                 * ScriptListener with this name. This name will provide the basis to re-attach the
                 * listener, when the microworld is loaded.
                 */
                sl.pathToComponent = new HierarchicalComponentPath2(path, nodeType);
                componentName.setLength(0);
            }
        }

        /* The handle may not be a top level component handle. It might be the
         * handle of a nested component. Therefore we have to track not just the
         * name of the handle, but the whole chain of handle names until the
         * top level handle will be found. This way we will be able to locate
         * the object to which this handle refers while restoring the
         * ScriptListenerMap.
         */
        ESlateHandle microworldHandle = container.microworld.eslateMwd.getESlateHandle();
        ESlateHandle containerHandle = container.getESlateHandle();
        for (int i=0; i<soundListenersIndices.size(); i++) {
            ESlateHandle handle = (ESlateHandle) handleArray.get(soundListenersIndices.get(i));
            SoundListener sl = (SoundListener) soundListenerArray.get(soundListenersIndices.get(i));
            StringBuffer fullHandleName = new StringBuffer("");
/*
            if (handle == container.getESlateHandle())
                fullHandleName.append("ESlateContainer");
            else
*/
            fullHandleName.append(handle.getComponentPath(componentHandle));
//System.out.println("handle: " + handle + ", componentHandle: " + componentHandle + ", handle.getComponentPath(componentHandle): " + handle.getComponentPath(componentHandle));
            sl.pathToComponent.setPathPart(0,fullHandleName.toString());
//System.out.println("handle: " + handle + ", sl.pathToComponent.path[0]: " + ((SoundListener) soundListenerArray.get(i)).pathToComponent.path[0] + ", sl.pathToComponent: " + sl.pathToComponent);
        }

        ArrayList savedSoundListeners = new ArrayList();
        /** If all the sounds of the microworld are saved */
        if (componentHandle == container.getESlateHandle()) {// microworld.eslateMwd.getESlateHandle()) {
            boolean allSoundsSaved = synchronizeSoundDirectory(targetFile, container.currentlyOpenMwdFile, savedSoundListeners);
//System.out.println("savedSoundListeners.size(): " + savedSoundListeners.size());
            if (!allSoundsSaved) {
                System.out.println("Some sounds which are still part of the microworld could not be saved");
                DetailedErrorDialog dialog = new DetailedErrorDialog(container.parentFrame);
                String message = container.containerBundle.getString("ContainerMsg60");
                dialog.setMessage(message);
                dialog.appendToDetails(container.containerBundle.getString("ContainerMsg61") + '\n');
//                dialog.createNewLine();
//                for (int i=0; i<unsavedSounds.size(); i++)
//                    dialog.appendToDetails(((SoundListener) unsavedSounds.get(i)).pathToSound + '\n');
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
//                unsavedSounds.clear();
            }
        }else{ /* If a component is saved */
            Entry soundDir = null;
            try{
                soundDir = getSoundDirInStructFile(targetFile);
                if (soundDir == null)
                    return null;
                targetFile.changeDir(soundDir);
            }catch (Throwable thr) {
                System.out.println("Unable to save new sounds. Cannot changeDir() to the sound directory");
                DetailedErrorDialog dialog = new DetailedErrorDialog(container.parentFrame);
                String message = container.containerBundle.getString("ContainerMsg59");
                dialog.setMessage(message);
                dialog.appendThrowableMessage(thr);
                dialog.createNewLine();
                dialog.appendThrowableStackTrace(thr);
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
                return null;
            }
            boolean unSavedSoundsExist = false;
            for (int i=0; i<soundListenersIndices.size(); i++) {
                SoundListener originalSL = (SoundListener) soundListenerArray.get(soundListenersIndices.get(i));
                SoundListener sl = new SoundListener(originalSL.soundName, originalSL.pathToSound,
                                                     originalSL.methodName, originalSL.pathToComponent,
                                                     originalSL.soundHandler, originalSL.listenerClass);
                sl.embeddedFileName = originalSL.embeddedFileName;
//System.out.println(originalSL.pathToComponent + ",  " + sl.pathToComponent);
//System.out.println("sl.pathToSound: " + sl.pathToSound + ", sl.previousEmbeddedFileName: " + sl.previousEmbeddedFileName + ", sl.embeddedFileName: " + sl.embeddedFileName);
                if (sl.embeddedFileName != null) {
                    /* Copy the embedded sound file to the new structured storage file. This
                     * happens only when a saved microworld is being saved to a new file.
                     */
//                    if (container.currentlyOpenMwdFile != null && !container.currentlyOpenMwdFile.getFile().getAbsolutePath().equals(targetFile.getFile().getAbsolutePath())) {
//System.out.println("GOT IN HERE!");
//System.out.println(container.currentlyOpenMwdFile.getFile().getAbsolutePath() + ", " + targetFile.getFile().getAbsolutePath());
                        Vector sourcePath = new Vector();
                        sourcePath.addElement("Sounds"); sourcePath.addElement(sl.embeddedFileName);
                        Vector destPath = new Vector();
                        destPath.addElement("Sounds");
                        try{
                            container.currentlyOpenMwdFile.copySubFile(sourcePath, StructFile.ABSOLUTE_PATH, targetFile, destPath, StructFile.ABSOLUTE_PATH);
                            sl.embeddedFileName = originalSL.embeddedFileName;
    //                        sl.previousEmbeddedFileName = sl.embeddedFileName;
                            savedSoundListeners.add(sl);
                        }catch (Throwable thr) {
                            thr.printStackTrace();
                            System.out.println("Unable to save sound");
                            unSavedSoundsExist = true;
                            soundListenersIndices.removeElements(i);
                            i--;
                            continue;
                        }
//                    }
                }else if (sl.pathToSound != null) {
                    File f = new File(sl.pathToSound);
                    try{
                        targetFile.copyFile(f);
                        String fileName = "sound_" + ESlateContainerUtils.createUniqueId(i);
                        targetFile.renameFile(f.getName(), fileName);
                        sl.embeddedFileName = fileName;
                        sl.pathToSound = null;
//                        sl.embeddedFileName = f.getName();
//                        sl.previousEmbeddedFileName = sl.embeddedFileName;
                        savedSoundListeners.add(sl);
                    }catch (Throwable thr) {
                        unSavedSoundsExist = true;
                        soundListenersIndices.removeElements(i);
                        i--;
                        continue;
                    }
                }
            }
        }

        try{
            targetFile.changeToParentDir();
        }catch (Throwable thr) {}
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_FORMAT_VERSION);

//for (int i=0; i<savedSoundListeners.size(); i++)
//    System.out.println("Saving listener: " + ((SoundListener) savedSoundListeners.get(i)).pathToComponent + ", " + ((SoundListener) savedSoundListeners.get(i)).embeddedFileName);
        fieldMap.put("SoundListener Array", savedSoundListeners);
        return fieldMap;
    }

    /* This method re-attaches the SoundListeners contained in the ESlateFieldMap argument to the
     * components of a microworld.
     */
    void loadMap(StorageStructure fm) { //ObjectInputStream in) throws IOException, ClassNotFoundException {
//        System.out.println("loadMap: " + fm);
        clear();
        if (fm == null)
            return;

//        Object obj = null;
        try{
//            obj = in.readObject();
            ArrayList soundListeners = (ArrayList) fm.get("SoundListener Array", (Object) null);
//            if (soundListeners != null)
//                System.out.println("soundListeners.size(): " + soundListeners.size());
            IntBaseArray listenerIndicesForLocatedObjects = locateSoundDestinations(soundListeners, container.getESlateHandle()); //container.microworld.eslateMwd.getESlateHandle());
//System.out.println("listenerIndicesForLocatedObjects: " + listenerIndicesForLocatedObjects.size());
            attachSoundListeners(null); //listenerIndicesForLocatedObjects);
        }catch (java.lang.ClassCastException exc) {
        }
    }

    /* Loads the sounds of a component and re-attaches them to its events. */
    void loadComponentSounds(StructFile componentFile, ESlateHandle handle, StorageStructure fm) {
        /* If the microworld is a new one and hasn't been saved yet, then the sound files cannot
         * be copied from the component's file. So we abort sound loading.
         */
        if (container.currentlyOpenMwdFile == null) return;

        ArrayList soundListeners = (ArrayList) fm.get("SoundListener Array");
        IntBaseArray listenerIndicesForLocatedObjects = locateSoundDestinations(soundListeners, handle); //handle.getParentHandle());
//System.out.println("listenerIndicesForLocatedObjects: " + listenerIndicesForLocatedObjects.size());
        attachSoundListeners(listenerIndicesForLocatedObjects);

        /* Now we have to copy the loaded sounds from the structfile of the component file,
         * to the structfile of the microworld.
         */
        StructFile mwdFile = container.currentlyOpenMwdFile;
        try{
            mwdFile.changeToRootDir();
            if (!mwdFile.fileExists("Sounds"))
                mwdFile.createDirectory("Sounds");
        }catch (Throwable thr) {
            System.out.println("Could not copy sound files from the component file \"" + componentFile.getFile().getName() + "\" to the microworld file");
            System.out.println(thr.getMessage());
//            thr.printStackTrace();
            return;
        }
        Vector destPath = new Vector();
        destPath.addElement("Sounds");
        for (int i=0; i<listenerIndicesForLocatedObjects.size(); i++) {
            SoundListener sl = (SoundListener) soundListenerArray.get(listenerIndicesForLocatedObjects.get(i));
            Vector sourcePath = new Vector();
            sourcePath.addElement("Sounds");
            sourcePath.addElement(sl.embeddedFileName);
            try{
                componentFile.copySubFile(sourcePath, StructFile.ABSOLUTE_PATH, mwdFile, destPath, StructFile.ABSOLUTE_PATH);
            }catch (Throwable thr) {
                System.out.println("Could not copy sound file \"" + sl.embeddedFileName + "\" from the component file \"" + componentFile.getFile().getName() + "\" to the microworld file");
                System.out.println(thr.getMessage());
//                thr.printStackTrace();
            }
        }
        mwdFile.flushCache();
    }

    /** Locates the components to which the sound listeners should be attached to, while loading
     *  a microworld or a component. The sound listeners whose object/components must be located
     *  are supplied through the first argument. The second argument, is the component whose
     *  listeners are located (can be a microworld or any component).
     */
    IntBaseArray locateSoundDestinations(ArrayList soundListeners, ESlateHandle componentHandle) { //ESlateMicroworld microworld) {
        IntBaseArray listenerAddedToSoundListenerMapIndices = new IntBaseArray();
        for (int i=0; i<soundListeners.size(); i++) {
            SoundListener sl = (SoundListener) soundListeners.get(i);
//System.out.println("locateSoundDestinations() soundListener: " + sl.pathToComponent + ", componentHandle: " + componentHandle);
//            System.out.println("compoName: " + ((ScriptListener) scriptListenerArray.at(i)).componentName);
            HierarchicalComponentPath2 compLoc = sl.pathToComponent;
//System.out.println("locateSoundDestinations() compLoc: " + compLoc);
            if (compLoc == null) {
                compLoc = new HierarchicalComponentPath2();
            }
            if (compLoc.depth() == 0) {
//                System.out.println("Fail 1");
                soundListeners.remove(i);
                i--;
                continue;
            }

            ESlateHandle handle = componentHandle.getHandle(compLoc.getPathPart(0));
//System.out.println("locateSoundDestinations()  componentHandle: " + componentHandle + ", handle: " + handle + ", compLoc.path[0]: " + compLoc.path[0]);
            if (handle == null) {
                /* Special care for the gr.cti.eslate.base.container.Microworld object, whose component
                 * path starts from ESlateContainer. See BeanInfoDialog.
                 */
                if (container.getESlateHandle().getComponentName().equals(compLoc.getPathPart(0)))
                    handle = container.getESlateHandle();
                else{
//                    System.out.println("Fail 2");
                    soundListeners.remove(i);
                    i--;
                    continue;
                }
            }
            Object object = null;
//-            System.out.println("names.length: " + names.length);
            if (compLoc.depth() == 1)
                object = handle.getComponent();
            else{
                object = HierarchicalComponentPath.getNestedObject(container.getESlateHandle(), handle.getComponent(), compLoc);
            }
//System.out.println("locateSoundDestinations()  object: " + object);
            if (object != null) {
//System.out.println("located handle: " + handle);
                addSoundListener(object, sl, handle);
                sl.soundHandler.container = container;
                listenerAddedToSoundListenerMapIndices.add(handleArray.size()-1);
            }else{
//                System.out.println("Fail 3");
                soundListeners.remove(i);
                i--;
                continue;
            }
        }
        return listenerAddedToSoundListenerMapIndices;
    }

    /** Re-attaches the specified listeners to their events. The listeners to be re-aatached are
     *  already in the SoundListenerMap. The method takes as argument an array with the indices
     *  of the listeners to be re-attached in the SoundListenerMap. This is needed when a component
     *  is loaded in the microworld, from its file. If the argument is null, then all the
     *  listeners of the ScriptListenerMap are re-attached.
     */
    void attachSoundListeners(IntBaseArray listenerIndices) {
        if (listenerIndices == null) {
            listenerIndices = new IntBaseArray();
            for (int i=0; i<objectArray.size(); i++)
                listenerIndices.add(i);
        }
        ArrayList objects = new ArrayList();
        ArrayList handles = new ArrayList();
        ArrayList listeners = new ArrayList();
        for (int i=0; i<listenerIndices.size(); i++) {
            int index = listenerIndices.get(i);
//System.out.println("attachSoundListeners listenerIndices(" + i + "): " + index);
            objects.add(objectArray.get(index));
            handles.add(handleArray.get(index));
            listeners.add(soundListenerArray.get(index));
        }

        /* First we group the listeners based on the objects (actually java components) they are
         * to be attached to. This happens so that to avoid the overhead of continuously asking
         * for the EventSetDescriptors of the same object.
         */
         ArrayList objectMap = new ArrayList();
         ArrayList listenerMap = new ArrayList();
         ArrayList handleMap = new ArrayList();
//         System.out.println("objectArray: " + objectArray);
//         System.out.println("scriptListenerArray: " + scriptListenerArray);
         for (int i=0; i<objects.size(); i++) {
            Object obj = objects.get(i);
            if (objectMap.contains(obj))
                continue;

            IntBaseArray listenerIndicesOfSameObject = new IntBaseArray();
            listenerIndicesOfSameObject.add(i);
            for (int k=i+1; k<objects.size(); k++) {
                if (obj == objects.get(k)) {
                    listenerIndicesOfSameObject.add(k);
                }
            }
            SoundListener[] sl = new SoundListener[listenerIndicesOfSameObject.size()];
            for (int k=0; k<sl.length; k++) {
                sl[k] = (SoundListener) listeners.get(listenerIndicesOfSameObject.get(k));
            }

//System.out.println("Adding obj: " + obj.getClass() + ", sl: " + sl + ", handle: " + handles.get(i));
            objectMap.add(obj);
            listenerMap.add(sl);
            handleMap.add(handles.get(i));
         }

         for (int i=0; i<objectMap.size(); i++) {
            Object component = objectMap.get(i);
            /* Get the EventSetDescriptors of the component. This is the whole set of
             * EventSetDescriptor's for the events that the component supports(generates).
             */
            EventSetDescriptor[] eventDescriptors = null;
            Class compoClass = component.getClass();
//                    System.out.println("compoClass: " + compoClass +", compoClass.getSuperclass(): " + compoClass.getSuperclass());
            Introspector.setBeanInfoSearchPath(new String[]
                                            {"javax.swing"});
//                    String[] paths = Introspector.getBeanInfoSearchPath();
//                    for (int i=0; i<paths.length; i++)
//                        System.out.println("path: " + paths[i]);
            BeanInfo compoInfo = BeanInfoFactory.getBeanInfo(compoClass); //, compoClass.getSuperclass());
            if (compoInfo == null) return;

//                    System.out.println("compoInfo: " + compoInfo);

            eventDescriptors = compoInfo.getEventSetDescriptors();
//                    System.out.println("eventDescriptors: " + eventDescriptors + " , eventDescriptors.length: " + eventDescriptors.length);
//                System.out.println("Load 2");

            SoundListener[] soundListeners = (SoundListener[]) listenerMap.get(i);
//            System.out.println("scriptListeners.length: " + scriptListeners.length);
//            System.out.println("scriptListeners[0]: " +  scriptListeners[0].script);
//            System.out.println("scriptListeners[1]: " +  scriptListeners[1].script);

            // Add each script listener to the component
            for (int k=0; k<soundListeners.length; k++) {
                SoundListener sl = soundListeners[k];
                if (sl.listenerClass == null || sl.soundHandler == null) {
                    removeSoundListener(sl);
                    continue;
                }
                /* Find the EventSetDcriptor, whose addListener method() accepts as an argument
                 * the listener we want to add to the component.
                 */
                EventSetDescriptor eventDescriptor = null;
                for (int t=0; t<eventDescriptors.length; t++) {
  //                        System.out.println(eventDescriptors[i].getListenerType() + " --> " + sl.listener.getClass() + ", assignable: " + eventDescriptors[i].getListenerType().isAssignableFrom(sl.listener.getClass()));
                    if (eventDescriptors[t].getListenerType().isAssignableFrom(sl.listenerClass)) {
                        eventDescriptor = eventDescriptors[t];
                        break;
                    }
                }
  //                    System.out.println(k + ". eventDescriptor: " + eventDescriptor);
                if (eventDescriptor != null) {
                    // Add the listener to the component.
                    Method addListenerMethod = eventDescriptor.getAddListenerMethod();
//System.out.println("CALLED the addListenerMethod method of the component addListenerMethod: " + addListenerMethod.getName() + ", component: " + component.getClass());
                    try{
                        addListenerMethod.invoke(component, new Object[] {sl.listener});
                    }catch (IllegalAccessException exc) {
                        System.out.println("IllegalAccessException while adding listener");
                    }catch (IllegalArgumentException exc) {
                        System.out.println("IllegalArgumentException while adding listener");
                    }catch (java.lang.reflect.InvocationTargetException exc) {
                        System.out.println("InvocationTargetException while adding listener");
                    }
                }
            }
        }  // scriptListeners' binding
    }

    Entry getSoundDirInStructFile(StructFile file) {
        if (file == null) return null;
        try{
            file.changeDir(file.getRootEntry());
            Entry soundDir = null;
            try{
                soundDir = file.findEntry("Sounds");
            }catch (IOException exc) {}
            if (soundDir == null) {
                file.createDirectory("Sounds");
            }
            return file.findEntry("Sounds");
        }catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(container.parentFrame);
            String message = container.containerBundle.getString("ContainerMsg58");
            dialog.setMessage(message);
            dialog.appendThrowableMessage(thr);
            dialog.createNewLine();
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, container, true);
            return null;
        }
    }

    static void createUniqueFileName(SoundListener listener, int offset) {
        listener.embeddedFileName = "sound_" + ESlateContainerUtils.createUniqueId(offset);
    }

    boolean removeSoundFile(StructFile file, String fileName) {
        if (fileName == null || fileName.trim().length() == 0)
            return false;
        try{
            file.deleteEntry(fileName);
        }catch (Throwable thr) {
            System.out.println("Unable to delete sound file " + fileName + " from microworld file");
        }
        return true;
    }

    void addPlayingSound(ESlateSound sound) {
        if (currentlyPlayingSounds.indexOf(sound) == -1)
            currentlyPlayingSounds.add(sound);
    }

    void removePlayingSound(ESlateSound sound) {
        currentlyPlayingSounds.remove(sound);
    }

    void invalidatePreviousEmbeddedFileNames() {
        for (int i=0; i<soundListenerArray.size(); i++)
            ((SoundListener) soundListenerArray.get(i)).previousEmbeddedFileName = null;
    }

    /* Returns if all the sound files of the SoundListeners were saved. */
    boolean synchronizeSoundDirectory(StructFile targetFile, StructFile sourceFile, ArrayList savedSoundListeners) {
        try{
            targetFile.changeToRootDir();
            if (!targetFile.fileExists("Sounds"))
                targetFile.createDirectory("Sounds");
            targetFile.changeDir("Sounds");
        }catch (Throwable thr) {
            System.out.println("Unable to save sound files to structured storage file \"" + targetFile.getFile().getName() + "\".");
            thr.printStackTrace();
            return false;
        }

        /* First we import all the external sound files */
        boolean allSaved = true;
        for (int i=0; i<soundListenerArray.size(); i++) {
            SoundListener sl = (SoundListener) soundListenerArray.get(i);
            boolean externalFile = (sl.pathToSound != null);
            if (externalFile) {
                boolean b = copyExternalSoundFile2(targetFile, sl, i);
                if (!b) {
                    allSaved = false;
                    removeSoundListener(sl);
                    i--;
                }else{
                    sl.pathToSound = null;
//                    savedSoundListeners.add(sl);
                }
            }
        }

        /* Then we delete all these internal sound files, which are not referenced by any
         * SoundListener.
         */
        ArrayList unSavedSoundListeners = new ArrayList();
        try{
            Vector internalFiles = targetFile.list();
            String[] internalFileNames = new String[internalFiles.size()];
            for (int i=0; i<internalFiles.size(); i++) {
                internalFileNames[i] = ((Entry) internalFiles.get(i)).getName();
//System.out.println("internalFiles(" + i + "): " + internalFiles.get(i) + ", internalFileNames[i]: " + internalFileNames[i]);
            }

            boolean[] internalFileUsed = new boolean[internalFileNames.length];
            for (int i=0; i<soundListenerArray.size(); i++) {
                SoundListener sl = (SoundListener) soundListenerArray.get(i);
                /* If a SoundListener has a reference to an internal file, which does not
                 * exist in the "Sounds" directory, then this is an unsaved SoundListener.
                 */
                boolean internalFileExists = false;
                for (int k=0; k<internalFileNames.length; k++) {
                    if (internalFileNames[k].equals(sl.embeddedFileName)) {
                        internalFileExists = true;
                        internalFileUsed[k] = true;
                        savedSoundListeners.add(sl);
                        break;
                    }
                }
//                if (!internalFiles.removeElement(sl.embeddedFileName))
                if (!internalFileExists)
                    unSavedSoundListeners.add(sl);
            }
            for (int i=0; i<internalFileUsed.length; i++) {
                if (!internalFileUsed[i])
                    targetFile.deleteFile(internalFileNames[i]); //((Entry) internalFiles.get(i)).getName());
            }
//            for (int i=0; i<internalFileNames.length; i++)
//                targetFile.deleteFile(internalFileNames[i]); //((Entry) internalFiles.get(i)).getName());
        }catch (Throwable thr) {
            System.out.println("Error while removing uneeded sound files from the microworld file \"" + targetFile.getFile().getAbsolutePath() + "\".");
            System.out.println(thr.getMessage());
            thr.printStackTrace();
        }

        /* If there exist unsaved sound files, then most probably the 'targetFile' is a new
         * structfile, to which data are copied from the 'sourceFile'. In this case move
         * files from the 'sourceFile' to the 'targetFile'.
         */
//System.out.println("unSavedSoundListeners.size(): " + unSavedSoundListeners.size());
        if (unSavedSoundListeners.size() != 0) {
            if (sourceFile == null) {
                System.out.println("Not all sound files were saved");
                return false;
            }
            Vector destPath = new Vector();
            destPath.addElement("Sounds");
            for (int i=0; i<unSavedSoundListeners.size(); i++) {
                SoundListener sl = (SoundListener) unSavedSoundListeners.get(i);
                try{
                    Vector sourcePath = new Vector();
                    sourcePath.addElement("Sounds");
                    sourcePath.addElement(sl.embeddedFileName);
                    sourceFile.copySubFile(sourcePath, StructFile.ABSOLUTE_PATH, targetFile, destPath, StructFile.ABSOLUTE_PATH);
                    savedSoundListeners.add(sl);
                }catch (Throwable thr) {
                    allSaved = false;
System.out.println("Unable to copy sound file \"" + sl.embeddedFileName + "\" from structured storage file \"" + sourceFile.getFile().getAbsolutePath() + "\" to structured storage file \"" + targetFile.getFile().getAbsolutePath());
                    System.out.println(thr.getMessage());
                    thr.printStackTrace();
                }
            }
        }
        try{
            targetFile.changeToRootDir();
        }catch (Throwable thr) {
            System.out.println(thr.getMessage());
            thr.printStackTrace();
        }
        return allSaved;
    }

    boolean copyExternalSoundFile2(StructFile file, SoundListener sl, int randomize) {
        byte[] buff = new byte[1024];
//        if (sl.pathToSound == null || sl.pathToSound.trim().length() == 0) {
//            sl.pathToSound = null;
//            return false;
//        }
        File f = new File(sl.pathToSound);
        if (!f.exists()) {
            sl.pathToSound = null;
            sl.embeddedFileName = null;
            System.out.println("Unable to save sound into microworld file. External file " + f.getAbsolutePath() + " does not exis");
            return false;
        }
        if (sl.embeddedFileName == null) {
            createUniqueFileName(sl, randomize);
//            System.out.println("Unable to save external sound file " + sl.pathToSound + " into microworld file. Internal error: the embedded sound file name has not been assigned");
//            return false;
        }

        Entry soundFile = null;
        try{
            soundFile = file.createFile(sl.embeddedFileName);
        }catch (Throwable thr) {
            thr.printStackTrace();
            sl.embeddedFileName = null;
            System.out.println("Unable to create sound file in microworld file for sound " + sl.pathToSound);
            return false;
        }
        BufferedInputStream inStream = null;
        try{
            inStream = new BufferedInputStream(new FileInputStream(f));
        }catch (Throwable thr) {
            System.out.println("Unable to open external sound file " + sl.pathToSound);
            sl.embeddedFileName = null;
            return false;
        }
        try{
            BufferedOutputStream outStream = new BufferedOutputStream(new StructOutputStream(file, soundFile));
            int fileSize = inStream.available();
            int totalBytesRead = 0;
//System.out.println("fileSize: " + fileSize);
            while (totalBytesRead != fileSize) {
//System.out.println("Reading 1024 bytes starting from: " + totalBytesRead);
                int byteCount = inStream.read(buff, 0, 1024);
//System.out.println("Read " + byteCount + " bytes");
                totalBytesRead = totalBytesRead + byteCount;
                outStream.write(buff, 0, byteCount);
            }
            inStream.close();
            outStream.flush();
            outStream.close();
        }catch (Throwable thr) {
            thr.printStackTrace();
            sl.embeddedFileName = null;
            System.out.println("Unable to copy sound file " + sl.pathToSound + " into microworld file.");
            return false;
        }
        return true;
    }

}