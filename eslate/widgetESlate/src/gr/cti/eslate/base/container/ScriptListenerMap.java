package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.base.container.event.MicroworldListener;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.IntBaseArray;

import java.awt.Component;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.objectspace.jgl.Array;
import com.objectspace.jgl.HashMap;


public class ScriptListenerMap {
    private Array scriptListenerArray = new Array();
    private transient Array objectArray = new Array();
    private transient Array handleArray = new Array();
    private transient ESlateContainer eSlateContainer;
    private transient ScriptListenerHandleNode scriptListenerTreeTopNode = null;
//    private static final String STORAGE_FORMAT_VERSION = "1.1";
    private static final int STORAGE_FORMAT_VERSION = 2;
    /** This Array was introduced when late ScriptListener binding was supported
     *  in a microworld. This support should be temporal.
     *  See locateAndAttachUnattachedListeners().
     */
    private Array unattachedListenerArray = new Array();
    /* Flag that signals that a call to a priviledged API is internal and not external.
     * This flag guarantees that E-Slate itself and other trusted entities, like the
     * microworld scripts, will have access to the priviledged API, when this access is
     * blocked by the microworld properties.
     */
    private boolean internalCall = false;

    public ScriptListenerMap(ESlateContainer container) {
        this.eSlateContainer = container;
    }

    public void addScriptListener(Object object, ScriptListener listener, ESlateHandle handle) {
        if (object == null || listener == null || handle == null) throw new NullPointerException();
        scriptListenerArray.add(listener);
        objectArray.add(object);
        handleArray.add(handle);
        if (scriptListenerTreeTopNode != null) {
            if (addScriptListenerNode(handle, listener, object) == null)
                System.out.println("Serious inconsistency error in ScriptListenerMap addScriptListener()");
        }
    }


    public void clear() {
        for (int i=0; i<scriptListenerArray.size(); i++) {
            ScriptListener sl = (ScriptListener) scriptListenerArray.at(i);

            if (sl.scriptLanguage == ScriptListener.LOGO && sl.logoHandler != null) {
                sl.logoHandler.eSlateHandle = null;
            }
            if (sl.scriptLanguage == ScriptListener.JAVASCRIPT && sl.jsHandler != null) {
                sl.jsHandler.eSlateHandle = null;
            }
        }
        scriptListenerArray.clear();
        objectArray.clear();
        handleArray.clear();
        if (scriptListenerTreeTopNode != null) {
            scriptListenerTreeTopNode.clear();
            scriptListenerTreeTopNode = null;
        }
//        if (eSlateContainer.scriptDialog != null) {
//            eSlateContainer.scriptDialog.setScriptListenerTree(null);
//        }
    }

    public int size() {
        return scriptListenerArray.size();
    }

    public void removeScriptListener(ScriptListener listener) {
        int index = scriptListenerArray.indexOf(listener);
        if (index != -1) {
            if (scriptListenerTreeTopNode != null) {
                ScriptListenerNode node = getScriptListenerNode((ESlateHandle) handleArray.at(index), listener);
                if (node != null) {
                    ((ScriptListenerTreeNode) node.getParent()).remove(node);
                    node.clear();
                }else
                    System.out.println("Serious inconsistency error in ScriptListenerMap removeScriptListener()");
            }

            ScriptListener sl = (ScriptListener) scriptListenerArray.at(index);
            if (sl.scriptLanguage == ScriptListener.LOGO && sl.logoHandler != null) {
                sl.logoHandler.eSlateHandle = null;
            }
            if (sl.scriptLanguage == ScriptListener.JAVASCRIPT && sl.jsHandler != null) {
                sl.jsHandler.eSlateHandle = null;
            }
            if (sl.listener != null && sl.scriptLanguage == ScriptListener.JAVA) {
                Method nullifyMethod = ESlateContainerUtils.findMethod(sl.listener, "dispose");
                if (nullifyMethod == null)
                    System.out.println("nullifyMethod: " + nullifyMethod + ", listener: " + sl.listener.getClass().getName());
                if (nullifyMethod != null) {
                    try{
//System.out.println("Calling dispose method of listener " + sl.listener.getClass().getName() + " on component " + handleArray.at(index));
                        nullifyMethod.invoke(sl.listener, new Object[] {});
                    }catch (Throwable thr) {
                        System.out.println("Error while calling the dispose method of listener " + sl.listener.getClass().getName() + " on component " + handleArray.at(index));
                    }
                }
            }

            scriptListenerArray.remove(index);
            objectArray.remove(index);
            handleArray.remove(index);
        }
//System.out.println("removeScriptListener() scriptListenerArray size: " + scriptListenerArray.size());
    }

    public void removeScriptListeners(ESlateHandle handle) {
        for (int i=handleArray.size()-1; i>=0; i--) {
            ESlateHandle h = (ESlateHandle) handleArray.at(i);
            if (!(handle == h || handle.hasChild(h)))
                continue;

            ScriptListener sl = (ScriptListener) scriptListenerArray.at(i);
            if (sl.scriptLanguage == ScriptListener.LOGO && sl.logoHandler != null) {
                sl.logoHandler.eSlateHandle = null;
            }
            if (sl.scriptLanguage == ScriptListener.JAVASCRIPT && sl.jsHandler != null) {
                sl.jsHandler.eSlateHandle = null;
            }
            if (sl.scriptLanguage == ScriptListener.JAVA) {
                Method nullifyMethod = ESlateContainerUtils.findMethod(sl.listener, "dispose");
                if (nullifyMethod == null)
                    System.out.println("nullifyMethod: " + nullifyMethod + "  listener: " + sl.listener.getClass().getName());
                if (nullifyMethod != null) {
                    try{
                        nullifyMethod.invoke(sl.listener, new Object[] {});
                    }catch (Throwable thr) {
                        System.out.println("Error while calling the dispose method of listener " + sl.listener.getClass().getName() + " on component " + handle);
                    }
                }
            }
            scriptListenerArray.remove(i);
            objectArray.remove(i);
            handleArray.remove(i);
//System.out.println("removeScriptListener() scriptListenerArray size: " + scriptListenerArray.size());
        }
        ScriptListenerHandleNode node = findHandleNode(scriptListenerTreeTopNode, handle);
        if (node != null) {
            ESlateComposer composer = null;
            if (eSlateContainer instanceof ESlateComposer)
                composer = (ESlateComposer) eSlateContainer;
            if (composer != null && composer.scriptDialog != null)
                composer.scriptDialog.selectMicroworldNode(true); //node, true);
            node.removeFromParent();
            node.clear();
            if (composer != null && composer.scriptDialog != null)
                composer.scriptDialog.updateScriptTree();
        }
    }

    /* Listener look-up service. The script listener look-up is based on the object for which
     * the listener is registered and not the path to the object. So for a example for an AWT
     * component which is both accesible from the component and the object hierarchy, the
     * script listener attached to it will be retrieved no matter through which path the object
     * is visited. Also this makes irrelevant the question which of the two paths should be saved
     * the onject's script listener.
     */
    public ScriptListener[] getScriptListeners(Object obj) {
        if (obj == null) return null;
        if (objectArray.size() == 0) return new ScriptListener[0];
        ScriptListener[] sl = new ScriptListener[objectArray.count(obj)];
        int index = 0;
        int count = 0;
        while ((index = objectArray.indexOf(index, objectArray.size()-1, obj)) != -1) {
            sl[count] = (ScriptListener) scriptListenerArray.at(index);
            index++;
            count++;
        }
//        System.out.println("getScriptListeners() sl.length: " + sl.length);
        return sl;
    }

    /** Returns the ScriptListeners registered with the specified component. If the
     *  second argument is true, then the ScriptListeners of all the nested components to
     *  this component are returned too. If the flag is false, only the ScriptListeners of
     *  the component are returned.
     */
    public ScriptListener[] getScriptListeners(ESlateHandle handle, boolean nestedCompsToo) {
        ArrayList listeners = new ArrayList();
        for (int i=0; i<handleArray.size(); i++) {
            ESlateHandle h = (ESlateHandle) handleArray.at(i);
            if (h == handle || (nestedCompsToo && handle.hasChild(h)))
                listeners.add(scriptListenerArray.at(i));
        }
        ScriptListener[] sl = new ScriptListener[listeners.size()];
        for (int i=0; i<sl.length; i++)
            sl[i] = (ScriptListener) listeners.get(i);
        return sl;
    }

    public void putScriptListener(Object object, ScriptListener listener, ESlateHandle handle) {
        if (object == null) return;
        if (listener == null || listener.methodName == null ||
            (listener.scriptLanguage != ScriptListener.LOGO && listener.scriptLanguage != ScriptListener.JAVA && listener.scriptLanguage != ScriptListener.JAVASCRIPT) ||
            handle == null)
            return;

        ScriptListener oldListener = getScriptListener(object, listener.methodName, listener.scriptLanguage);
        if (oldListener == null)
            addScriptListener(object, listener, handle);
        else{
            int index = scriptListenerArray.indexOf(oldListener);
            scriptListenerArray.put(index, listener);
            // Update the proper ScriptListenerNode
            if (scriptListenerTreeTopNode != null) {
                ScriptListenerNode node = getScriptListenerNode(handle, oldListener);
                if (node != null)
                    node.listener = listener;
                else
                    System.out.println("Serious inconsistency error in ScriptListenerMap putScriptListener()");
            }
        }
    }

    protected ScriptListener getScriptListener(Object object, String methodName, int language) {
        if (object == null) return null;
        if (methodName == null) return null;
//        System.out.println("objectArray.size(): " + objectArray.size());
        if (objectArray.size() == 0) return null;

        ScriptListener scriptListener = null;
//        System.out.println("getScriptListener():  " + object + ", methodName: " + methodName + ", inLogo: " + inLogo);
        ScriptListener[] scriptListeners = getScriptListeners(object);
        if (scriptListeners != null) {
            for (int i=0; i<scriptListeners.length; i++) {
//               System.out.println("scriptListeners[j].getMethodName(): " + scriptListeners[i].getMethodName());
                if (scriptListeners[i].getMethodName().equals(methodName) && scriptListeners[i].scriptLanguage == language) {
                    scriptListener = scriptListeners[i];
                    break;
                }
            }
        }
        return scriptListener;
    }

    public int getScriptListenerCount() {
        return scriptListenerArray.size();
    }

    public int indexOf(ScriptListener listener) {
        return scriptListenerArray.indexOf(listener);
    }

    public Object getObject(int index) {
        return objectArray.at(index);
    }

    public ScriptListener getScriptListener(int index) {
        return (ScriptListener) scriptListenerArray.at(index);
    }

    public ESlateHandle getHandle(int index) {
        return (ESlateHandle) handleArray.at(index);
    }

    Object saveMap() {
        /* First check if there are any handles registered in the ScriptListenerMap
         * which are disposed. Handles like that may exists for second or lower level
         * components, which are disposed without E-Slate's knowledge. The scripts of
         * these  handles will be removed from the ScriptListenerMap, before it is
         * saved.
         */
        for (int i=0; i<objectArray.size(); i++) {
            ESlateHandle handle = (ESlateHandle) handleArray.at(i);
//System.out.println("handle: " + handle);
            if (handle.isDisposed()) {
                removeScriptListeners(handle);
                i--;
            }
        }

//System.out.println("saveMap() objectArray.size(): " + objectArray.size());
        StringBuffer componentName = new StringBuffer();
        /* Check that all the objects which have been registered as script listener
         * holders. Any of these objects which are AWT components shall have their
         * script listeners saved using the AWT component path. This won't work for
         * AWT components which belong to AWT containers that host multiple components
         * one at a time. Such a container is the JTabbedPane.
         */
        for (int i=0; i<objectArray.size(); i++) {
            ESlateHandle handle = (ESlateHandle) handleArray.at(i);
            Object obj = objectArray.at(i);
//System.out.println("saveMap() handle: " + handle + ", obj: " + obj);
//            System.out.println("saveMap() obj: " + obj);
            if (Component.class.isAssignableFrom(obj.getClass()) &&
                SwingUtilities.getAncestorOfClass(javax.swing.JTabbedPane.class, (Component) obj) == null) {
//                System.out.println("saveMap() AWT case");
                Component comp = (Component) objectArray.at(i);
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
                comp = (Component) objectArray.at(i);

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
                ((ScriptListener) scriptListenerArray.at(i)).componentName = new String(componentName);
//                System.out.println("i: " + i + ". Saving path for component: " + objectArray.at(i));
                ((ScriptListener) scriptListenerArray.at(i)).pathToComponent = new HierarchicalComponentPath2(path, nodeType);
//            System.out.println("saveMap   componentName: " + componentName);
//            System.out.println("Script: " + ((ScriptListener) scriptListenerArray.at(i)).script);
//            System.out.println("Script in logo: " + ((ScriptListener) scriptListenerArray.at(i)).scriptInLogo);
                componentName.setLength(0);
            }
        }
//        for (int i=0; i<objectArray.size(); i++)
//            System.out.println("saveMap() pathToComponent: " + ((ScriptListener) scriptListenerArray.at(i)).pathToComponent);


        /* The handle may not be a top level component handle. It might be the
         * handle of a nested component. Therefore we have to track not just the
         * name of the handle, but the whole chain of handle names until the
         * top level handle will be found. This way we will be able to locate
         * the object to which thiw handle refers while restoring the
         * ScriptListenerMap.
         */
        ESlateHandle mwdHandle = eSlateContainer.getMicroworld().getESlateMicroworld().getESlateHandle();
        for (int i=0; i<objectArray.size(); i++) {
            ESlateHandle handle = (ESlateHandle) handleArray.at(i);
//System.out.println("SCRIPT LISTENER MAP saveMap() handle: " + handle + ", obj: " + objectArray.at(i).getClass());
            String fullHandleName = null;
            /* For any script which is attached to the ESlateContainer itself (and this is the case
             * with the events of the MicroworldListener, which refer to a
             * gr.cti.eslate.base.container.Microworld and thus cannot be attached/saved with
             * the handle of the gr.cti.eslate.base.ESlateMicroworld), then the statement:
             *            handle.getComponentPath(eSlateContainer.getESlateHandle())
             * returns null (the relative path of the ESlateContainer's handle to itself is null).
             * In this cases we set the ScriptListener's HierarchyComponentPath's first part to
             * "ESlateContainer". While loading the ScriptListeners of a microworld, this is
             * checked, so that the above ScriptListeners will be attached to the ESlateContainer's
             * handle. Seee locateScriptableObjects() below.
             * ------------------------------------------------------------------------------------
             * The above are not true anymore. They used to be true for an old version of the
             * platform. The latest version returns '.', when the path of a component from itself
             * is asked, using getComponentPath(). So the 'ESlateContainer' notation was aborted
             * and when saving a MicroworldListener script the first part of the HierarchicalComponentPath
             * of the ScriptListener contains '.'.
             */
/*
            if (handle == eSlateContainer.getESlateHandle())
                fullHandleName = "ESlateContainer";
            else
*/
            fullHandleName = handle.getComponentPath(eSlateContainer.getESlateHandle());
            ((ScriptListener) scriptListenerArray.at(i)).pathToComponent.path[0] = fullHandleName;
//System.out.println("saveMap() ((ScriptListener) scriptListenerArray.at(i)).pathToComponent: " + ((ScriptListener) scriptListenerArray.at(i)).pathToComponent);
        }

        // If the microworld's scripts(their textual form) have been loaded, then they will be saved to new files, now that
        // the microworld is being saved. Set the file name of each script's file. This file name will be used later in the
        // process of saving the microworld (see ScriptUtils.saveLoadedScripts())
        if (ScriptUtils.getInstance().scriptsLoaded) {
            for (int i=0; i<scriptListenerArray.size(); i++) {
                ((ScriptListener) scriptListenerArray.at(i)).scriptFilename = String.valueOf(i);
            //    System.out.println("SCRIPT LISTENER MAP saving scriptListener: " + ((ScriptListener) scriptListenerArray.at(i)).getMethodName() + ", " + ((ScriptListener) scriptListenerArray.at(i)).pathToComponent);
            }
        }

        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_FORMAT_VERSION);
        fieldMap.put("ScriptListener Array", scriptListenerArray);
        /* The 'unattachedListenerArray' should be saved, since the initially unattached
         * listeners (after microworld loading finishes) may not be attached until the
         * microworld is closed.
         */
        fieldMap.put("Unattached listeners", unattachedListenerArray);
        return fieldMap;
    }

    ESlateFieldMap2 saveComponentScriptListeners(ESlateHandle eSlateHandle) {
        /* First check if the handle is disposed. If its is, then remove its listeners
         * from the ScriptListenerMap and return null.
         */
        if (eSlateHandle.isDisposed()) {
            removeScriptListeners(eSlateHandle);
            return null;
        }

        /* Find all the objects for which ScriptListeners are registered in the ScriptListenerMap
         * under this handle.
         */
        IntBaseArray indices = new IntBaseArray();
        for (int i=0; i<objectArray.size(); i++) {
            ESlateHandle h = (ESlateHandle) handleArray.at(i);
            if (eSlateHandle == h || eSlateHandle.hasChild(h))
                indices.add(i);
        }
        // Gather the listeners to be saved.
        ArrayList componentScriptListeners = new ArrayList();
        for (int i=0; i<indices.size(); i++)
            componentScriptListeners.add(scriptListenerArray.at(indices.get(i)));

        StringBuffer componentName = new StringBuffer();
        /* Check that all objects which are related to the saved component
         * (nested or the component itself) and are script listener
         * holders. Any of these objects which are AWT components shall have their
         * script listeners saved using the AWT component path. This won't work for
         * AWT components which belong to AWT containers that host multiple components
         * one at a time. Such a container is the JTabbedPane.
         */
        for (int i=0; i<indices.size(); i++) {
            ESlateHandle handle = (ESlateHandle) handleArray.at(indices.get(i));
            Object obj = objectArray.at(indices.get(i));
            if (Component.class.isAssignableFrom(obj.getClass()) &&
                SwingUtilities.getAncestorOfClass(javax.swing.JTabbedPane.class, (Component) obj) == null) {
                Component comp = (Component) obj; //objectArray.at(i);
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
                comp = (Component) obj; //objectArray.at(i);

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
                ((ScriptListener) componentScriptListeners.get(i)).componentName = new String(componentName);
//                System.out.println("i: " + i + ". Saving path for component: " + objectArray.at(i));
                ((ScriptListener) componentScriptListeners.get(i)).pathToComponent = new HierarchicalComponentPath2(path, nodeType);
//            System.out.println("saveMap   componentName: " + componentName);
//            System.out.println("Script: " + ((ScriptListener) scriptListenerArray.at(i)).script);
//            System.out.println("Script in logo: " + ((ScriptListener) scriptListenerArray.at(i)).scriptInLogo);
                componentName.setLength(0);
            }
        }

        for (int i=0; i<indices.size(); i++) {
            ESlateHandle handle = (ESlateHandle) handleArray.at(indices.get(i));
            String fullHandleName = handle.getComponentPath(eSlateHandle);
            ((ScriptListener) componentScriptListeners.get(i)).pathToComponent.path[0] = fullHandleName;
        }
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_FORMAT_VERSION);
        fieldMap.put("ScriptListener Array", componentScriptListeners);
        return fieldMap;
    }

    /** Loads and re-attached to the supplied component the script listeners which are in the
     *  supplied in the ArrayList. The target components/objects of the listeners are located
     *  relatively to 'eSlateHandle'.
     */
    void loadComponentScriptListeners(ESlateHandle eSlateHandle, ArrayList componentScriptListeners) { //ESlateFieldMap fm) {
        IntBaseArray listenerIndices = new IntBaseArray();
        for (int i=0; i<componentScriptListeners.size(); i++) {
//System.out.println("compoName: " + ((ScriptListener) componentScriptListeners.get(i)).componentName);
            HierarchicalComponentPath2 compLoc = ((ScriptListener) componentScriptListeners.get(i)).pathToComponent;
//System.out.println("locateScriptableObjects() compLoc: " + compLoc);
            if (compLoc == null) {
                compLoc = new HierarchicalComponentPath2();
            }
            if (compLoc.path == null || compLoc.path.length == 0) {
//System.out.println("Fail 1");
                componentScriptListeners.remove(i);
                i--;
                continue;
            }

            /* Find the handle to which the object the script should be reattached to, belongs.
             * The handle can be either the restored component, or some other component
             * nested in it.
             */
//System.out.println("compLoc.path[0]: " + compLoc.path[0]);
            ESlateHandle handle = eSlateHandle.getHandle(compLoc.path[0]);
//System.out.println("handle: " + handle);
            if (handle == null) {
                componentScriptListeners.remove(i);
                i--;
                continue;
            }
            Object object = null;
//-            System.out.println("names.length: " + names.length);
            if (compLoc.path.length == 1)
                object = handle.getComponent();
            else{
                object = HierarchicalComponentPath.getNestedObject(handle, handle.getComponent(), compLoc);
            }
//System.out.println("object: " + object);
            if (object != null) {
//                System.out.println("locateScriptableObjects: " + object + ", " + handle);
                addScriptListener(object, (ScriptListener) componentScriptListeners.get(i), handle);
                listenerIndices.add(objectArray.size()-1);
            }else{
                System.out.println("Fail 3");
                componentScriptListeners.remove(i);
                i--;
                continue;
            }
        }
        attachScriptListeners(eSlateContainer, listenerIndices);
    }

    /** This is a temporary way to achieve ScriptListener late-binding for components
     *  which are not instantiated upon microworld start-up, but later on. At the
     *  time this solution was introduced such components are the SkinPanes of the
     *  ESlateInternalFrames and their contents. The state of the ESlateInternalFrames
     *  which are iconified when the microworld is loaded is restored, when the first
     *  become de-iconified. At this point any ScriptListeners which belong to any of
     *  the ESlateInternalFrame's SkinPanes (or its components) are re-attached.
     *  This solution should be a temporary one.
     */
    public void locateAndAttachUnattachedListeners(ESlateHandle handle) {
//System.out.println("locateAndAttachUnattachedListeners: " + unattachedListenerArray.size());
        if (unattachedListenerArray.size() == 0) return;
        ArrayList listenersToBeAttached = new ArrayList();
        IntBaseArray listenerIndices = new IntBaseArray();
        String handlePath = handle.getComponentPath(handle.getTopmostMicroworldHandle().getParentHandle());
//System.out.println("handlePath: " + handlePath);
        /* The listeners to be re-attached are those whose handle path (stored in
         * position 0 of the path of the 'pathToComponent' of the listener) starts
         * with the handle path of the method's argument.
         */
        for (int i=0; i<unattachedListenerArray.size(); i++) {
            ScriptListener sl = (ScriptListener) unattachedListenerArray.at(i);
//System.out.println("sl.pathToComponent.path[0]: " + sl.pathToComponent.path[0] + ", sl.pathToComponent.toString().startsWith(handlePath): " + sl.pathToComponent.path[0].startsWith(handlePath));
            if (sl.pathToComponent.path[0].startsWith(handlePath)) {
                sl.pathToComponent.path[0] = sl.pathToComponent.path[0].substring(handlePath.length());
                if (sl.pathToComponent.path[0].length() == 0)
                    sl.pathToComponent.path[0] = ".";
//System.out.println("2. sl.pathToComponent.path[0]: " + sl.pathToComponent.path[0]);
                listenersToBeAttached.add(sl);
                unattachedListenerArray.remove(i);
                i--;
            }
        }
//System.out.println("listenersToBeAttached: " + listenersToBeAttached.size());
        if (listenersToBeAttached.size() > 0)
            loadComponentScriptListeners(handle, listenersToBeAttached);
    }

    protected void loadMap(Object obj) { //ObjectInputStream in) throws IOException, ClassNotFoundException {
//        System.out.println("loadMap: " + obj);
        clear();
//        Object obj = null;
        try{
//            obj = in.readObject();
            if (StorageStructure.class.isAssignableFrom(obj.getClass())) {
                scriptListenerArray = (Array) ((StorageStructure) obj).get("ScriptListener Array", (Object) null);
//                System.out.println("scriptListenerArray: " + scriptListenerArray);
            }else
                scriptListenerArray = (Array) obj;
            /* Browse through the listeners array and locate these listeners whose 'listener' field
             * is null. These listeners failed to load. Remove them from the 'scriptListenerArray'.
             */
            for (int i=scriptListenerArray.size()-1; i>=0; i--) {
                if (((ScriptListener) scriptListenerArray.at(i)).listener == null) {
//System.out.println("loadMap()  listener: " + ((ScriptListener) scriptListenerArray.at(i)).listenerClassName);
//System.out.println("Removing listener : " + 1);
                    scriptListenerArray.remove(i);
                }
            }
        }catch (java.lang.ClassCastException exc) {
            /* In the versions prior to 0.9.6 the scriptListeners where stored in a HashMap. Try to
             * convert them to the new format.
             */
            HashMap map = (HashMap) obj;
//            System.out.println("map: " + map);
            java.util.Enumeration enumeration = map.keys();
            while (enumeration.hasMoreElements()) {
                Object obj1 = enumeration.nextElement();
                ScriptListener[] listeners = (ScriptListener[]) map.get(obj1);
//                System.out.println("obj1: " + obj1 + ", listeners: " + listeners.getClass());
                for (int i=0; i<listeners.length; i++)
                    scriptListenerArray.add(listeners[i]);
            }
        }
/*        for (int i=0; i<scriptListenerArray.size(); i++) {
            System.out.println("Script method: " + ((ScriptListener) scriptListenerArray.at(i)).methodName);
            System.out.println("Script: " + ((ScriptListener) scriptListenerArray.at(i)).script);
            System.out.println("Script in logo: " + ((ScriptListener) scriptListenerArray.at(i)).scriptInLogo);
        }
*/
//        System.out.println("loadMap() end");
    }


/*    private Component getComponent(Container container, String componentName) {
//        System.out.println("getComponent() componentName: " + componentName);
        if (componentName == null) return null;
        Component[] comps = container.getComponents();
        for (int i=0; i<comps.length; i++) {
*/
            /* If the container has not been serialized (e.g. if the whole E-Slate component
             * is Externalizable (not Serializable) and this container is not explicitly saved,
             * then the component names which were established during saveMap() have been lost.
             * Therefore we have to rename the container's components.
             */
/*            if (comps[i].getName() == null) {
              nameContainerComponents(container);
            }
            if (componentName.equals(comps[i].getName()))
                return comps[i];
        }
//        System.out.println("getComponent()  Returning null");
        return null;
    }
*/

/*    private Component getNestedComponent(Container container, String[] componentNames) {
        if (componentNames == null || componentNames.length == 0) return null;
        Component comp = null;
        for (int i=0; i<componentNames.length; i++) {
            System.out.println("componentNames: " + componentNames[i]);
            if (i == 0) {
*/              /* The first component has to be a top level component WITH an ESlateHandle in the
                 * 'currentMicroworld'.
                 */
/*                try{
                    comp = (Component) eSlateContainer.currentMicroworld.getComponent(componentNames[0]);;
                }catch (Exception exc) {return null;}
            }else
                comp = getComponent(container, componentNames[i]);
            System.out.println("comp: " + comp);
            if (comp == null) return null;
            if (Container.class.isAssignableFrom(comp.getClass()))
                container = (Container) comp;
            else
                return null;
        }
        return comp;
    }
*/

    void locateScriptableObjects(ESlateMicroworld microworld) {
//        System.out.println("locateScriptableObjects() scriptListenerArray.size(): " + scriptListenerArray.size());
        for (int i=0; i<scriptListenerArray.size(); i++) {
//            System.out.println("compoName: " + ((ScriptListener) scriptListenerArray.at(i)).componentName);
            HierarchicalComponentPath2 compLoc = ((ScriptListener) scriptListenerArray.at(i)).pathToComponent;
//            System.out.println("locateScriptableObjects() compLoc: " + compLoc);
            if (compLoc == null) {
                compLoc = new HierarchicalComponentPath2();
            }
//            System.out.println("locateScriptableObjects pathToComponent: " + compLoc);
            if (compLoc.path == null || compLoc.path.length == 0) {
//                System.out.println("Fail 1");
                scriptListenerArray.remove(i);
                i--;
                continue;
            }

            /* Any HierarchyComponentPath that starts from the ESlateContainer's handle, has
             * a first part with the name "ESlateContainer".
             * This was true in storage format version "1". Since 1.1 the first part of a
             * HierarchicalComponentPath which starts from the handle of the ESlateContainer
             * is '.'. (Note: the first part of a HeirarchicalComponentPath 'points' to the handle
             * closet to the object to which the script is attached). This is the real path,
             * cause all the paths are recorded from the ESlateContainer's handle and down.
             */
//System.out.println("SCRIPT LISTENER MAP looking for handle: " + compLoc.path[0]);
            ESlateHandle handle = eSlateContainer.getESlateHandle().getHandle(compLoc.path[0]); // microworld.getComponentHandle(compLoc.path[0]);
            if (handle == null && compLoc.path[0].equals("ESlateContainer")) {
                handle = eSlateContainer.getESlateHandle();
                compLoc.path[0] = ".";
            }
//System.out.println("locateScriptableObjects()  handle: " + handle);
            if (handle == null) {
//                System.out.println("Fail 2");
                /* If the component of a listener cannot be located, put the
                 * ScriptListener in the 'unattachedListenerArray'. Therefore the
                 * ScriptListener will have a chance to be re-attached later in
                 * the microworld's life.
                 */
                unattachedListenerArray.add(scriptListenerArray.at(i));
                scriptListenerArray.remove(i);
                i--;
                continue;
            }
            Object object = null;
//-            System.out.println("names.length: " + names.length);
            if (compLoc.path.length == 1)
                object = handle.getComponent();
            else{
                object = HierarchicalComponentPath.getNestedObject(eSlateContainer.getESlateHandle(), handle.getComponent(), compLoc);
            }
//            System.out.println("locateScriptableObjects()  object: " + object);
            if (object != null) {
//                System.out.println("locateScriptableObjects: " + object + ", " + handle);
                handleArray.add(handle);
                objectArray.add(object);
            }else{
//                System.out.println("Fail 3");
                scriptListenerArray.remove(i);
                i--;
                continue;
            }
        }
    }

    /** Re-attaches ScriptListeners, which have already been added to the ScriptListenerMap.
     *  This means that the listeners themselves, their objects and the respective handles
     *  have already been added to the Arrays of the ScriptListenerMap. The second argument
     *  of this method specifies which of the listeners in the ScriptListenerMap will be
     *  re-attached. This is needed when a component is loaded in the microworld, from its file.
     *  If the argument is null, then all the listeners of the ScriptListenerMap are re-attached.
     */
    void attachScriptListeners(ESlateContainer container, IntBaseArray listenerIndices) {
        if (listenerIndices == null) {
            listenerIndices = new IntBaseArray();
            for (int i=0; i<objectArray.size(); i++)
                listenerIndices.add(i);
        }
        ArrayList objects = new ArrayList();
        ArrayList handles = new ArrayList();
        ArrayList listeners = new ArrayList();
        for (int i=0; i<listenerIndices.size(); i++) {
            objects.add(objectArray.at(listenerIndices.get(i)));
            handles.add(handleArray.at(listenerIndices.get(i)));
            listeners.add(scriptListenerArray.at(listenerIndices.get(i)));
        }

        /* First we group the listeners based on the objects (actually java components) they are
        * to be attached to. This happens so that to avoid the overhead of continuously asking
        * for the EventSetDescriptors of the same object.
        */
        Array objectMap = new Array();
        Array listenerMap = new Array();
        Array handleMap = new Array();
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
            ScriptListener[] sl = new ScriptListener[listenerIndicesOfSameObject.size()];
            for (int k=0; k<sl.length; k++)
                sl[k] = (ScriptListener) listeners.get(listenerIndicesOfSameObject.get(k));

            objectMap.add(obj);
            listenerMap.add(sl);
            handleMap.add(handles.get(i));
        }

        container.getMicroworld().attachScriptListeners(objectMap, handleMap, listenerMap, listenerIndices);
        for (int i=0; i<objectMap.size(); i++) {
            Object component = objectMap.at(i);
            /* Get the EventSetDescriptors of the component. This is the whole set of
            * EventSetDescriptor's for the events that the component supports(generates).
            */
            EventSetDescriptor[] eventDescriptors = null;
            Class compoClass = component.getClass();
            Introspector.setBeanInfoSearchPath(new String[]
            {"javax.swing"});
            BeanInfo compoInfo = BeanInfoFactory.getBeanInfo(compoClass); //, compoClass.getSuperclass());
            if (compoInfo == null) return;

            ScriptListener[] scriptListeners = (ScriptListener[]) listenerMap.at(i);
            for (int k=0; k<scriptListeners.length; k++) {
                ScriptListener sl = scriptListeners[k];
                eventDescriptors = compoInfo.getEventSetDescriptors();
                /* Find the EventSetDcriptor, whose addListener method() accepts as an argument
                * the listener we want to add to the component.
                */
                EventSetDescriptor eventDescriptor = null;
                for (int t=0; t<eventDescriptors.length; t++) {
                    //                        System.out.println(eventDescriptors[i].getListenerType() + " --> " + sl.listener.getClass() + ", assignable: " + eventDescriptors[i].getListenerType().isAssignableFrom(sl.listener.getClass()));
                    if (eventDescriptors[t].getListenerType().isAssignableFrom(sl.listener.getClass())) {
                        eventDescriptor = eventDescriptors[t];
                        break;
                    }
                }
//                    System.out.println(k + ". eventDescriptor: " + eventDescriptor);
                if (eventDescriptor != null) {
                    // Add the listener to the component.
                    Method addListenerMethod = eventDescriptor.getAddListenerMethod();
                    Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
                    sl.addListenerMethod = addListenerMethod;
                    sl.removeListenerMethod = removeListenerMethod;
                    sl.target = component;
                }
            }
        }
        for (int i=0; i<scriptListenerArray.size(); i++) {
            ((ScriptListener) scriptListenerArray.at(i)).attachListener();
        }
    }


//    void attachScriptListeners(ESlateContainer container, IntBaseArray listenerIndices) {
//        if (listenerIndices == null) {
//            listenerIndices = new IntBaseArray();
//            for (int i=0; i<objectArray.size(); i++)
//                listenerIndices.add(i);
//        }
//        ArrayList objects = new ArrayList();
//        ArrayList handles = new ArrayList();
//        ArrayList listeners = new ArrayList();
//        for (int i=0; i<listenerIndices.size(); i++) {
//            objects.add(objectArray.at(listenerIndices.get(i)));
//            handles.add(handleArray.at(listenerIndices.get(i)));
//            listeners.add(scriptListenerArray.at(listenerIndices.get(i)));
//        }
//
//        /* First we group the listeners based on the objects (actually java components) they are
//         * to be attached to. This happens so that to avoid the overhead of continuously asking
//         * for the EventSetDescriptors of the same object.
//         */
//         Array objectMap = new Array();
//         Array listenerMap = new Array();
//         Array handleMap = new Array();
////         System.out.println("objectArray: " + objectArray);
////         System.out.println("scriptListenerArray: " + scriptListenerArray);
//         for (int i=0; i<objects.size(); i++) {
//            Object obj = objects.get(i);
//            if (objectMap.contains(obj))
//                continue;
//            IntBaseArray listenerIndicesOfSameObject = new IntBaseArray();
//            listenerIndicesOfSameObject.add(i);
//            for (int k=i+1; k<objects.size(); k++) {
//                if (obj == objects.get(k)) {
//                    listenerIndicesOfSameObject.add(k);
//                }
//            }
//            ScriptListener[] sl = new ScriptListener[listenerIndicesOfSameObject.size()];
//            for (int k=0; k<sl.length; k++)
//                sl[k] = (ScriptListener) listeners.get(listenerIndicesOfSameObject.get(k));
//
//            objectMap.add(obj);
//            listenerMap.add(sl);
//            handleMap.add(handles.get(i));
//         }
//
//         for (int i=0; i<objectMap.size(); i++) {
//            Object component = objectMap.at(i);
//            /* Get the EventSetDescriptors of the component. This is the whole set of
//             * EventSetDescriptor's for the events that the component supports(generates).
//             */
//            EventSetDescriptor[] eventDescriptors = null;
//            Class compoClass = component.getClass();
////                    System.out.println("compoClass: " + compoClass +", compoClass.getSuperclass(): " + compoClass.getSuperclass());
//            Introspector.setBeanInfoSearchPath(new String[]
//                                            {"javax.swing"});
////                    String[] paths = Introspector.getBeanInfoSearchPath();
////                    for (int i=0; i<paths.length; i++)
////                        System.out.println("path: " + paths[i]);
//            BeanInfo compoInfo = BeanInfoFactory.getBeanInfo(compoClass); //, compoClass.getSuperclass());
//            if (compoInfo == null) return;
//
////                    System.out.println("compoInfo: " + compoInfo);
//
//            eventDescriptors = compoInfo.getEventSetDescriptors();
////                    System.out.println("eventDescriptors: " + eventDescriptors + " , eventDescriptors.length: " + eventDescriptors.length);
////                System.out.println("Load 2");
//
//            ScriptListener[] scriptListeners = (ScriptListener[]) listenerMap.at(i);
////            System.out.println("scriptListeners.length: " + scriptListeners.length);
////            System.out.println("scriptListeners[0]: " +  scriptListeners[0].script);
////            System.out.println("scriptListeners[1]: " +  scriptListeners[1].script);
//
//            // Add each script listener to the component
//            for (int k=0; k<scriptListeners.length; k++) {
//                ScriptListener sl = scriptListeners[k];
////                System.out.println("scriptListeners[k].script: " + scriptListeners[k].script);
////                System.out.println("scriptListeners[k].isScriptInLogo: " + scriptListeners[k].isScriptInLogo());
//                if (sl.scriptLanguage == ScriptListener.JAVA) {
//                    /* If the scriptListener does not carry a Logo script, but rather a
//                     * java program, then we have to set the preset variables of this program.
//                     * This is down by calling the "setESlateHandle" of the listener instace,
//                     * passing to it the component's handle. To do it we get the methods for
//                     * the listener's class, search for the method with name "setESlateHandle"
//                     * and then invoke it.
//                     */
//                    try{
///*                        Method[] listenerMethods = sl.getListener().getClass().getMethods();
//                        Method handleSetterMethod = null;
//                        for (int t=0; t<listenerMethods.length; t++) {
//                            if (listenerMethods[t].getName().equals("setESlateHandle")) {
//                                handleSetterMethod = listenerMethods[t];
//                                break;
//                            }
//                        }
//*/
//                        ESlateHandle hdl = (ESlateHandle) handleMap.at(i);
//                        /* Listeners for the events of the gr.cti.eslate.base.container.Microworld are saved
//                         * with a HierarchyComponentPath that starts with the handle of the ESlateContainer.
//                         * However these Java listener's setESlateHandle() has to be called with the
//                         * microworld's handle.
//                         */
//                        if (MicroworldListener.class.isAssignableFrom(sl.getListener().getClass()))
//                            hdl = container.getMicroworld().getESlateMicroworld().getESlateHandle();
//
//                        container.getMicroworld().activateListener(container, scriptListeners[k], hdl);
////                        handleSetterMethod.invoke(sl.getListener(), new Object[] {hdl});
//  //                            System.out.println("CALLED the setESlateHandle method of the script listener");
//                    }catch (IllegalAccessException exc) {
//                        System.out.println("IllegalAccessException while instantiating listener object or while setting listener's eSlateHandle");
//                        continue;
//                    }catch (IllegalArgumentException exc) {
//                        System.out.println("IllegalArgumentException while setting listener's eSlateHandle");
//                        continue;
//                    }catch (java.lang.reflect.InvocationTargetException exc) {
//                        System.out.println("InvocationTargetException while setting listener's eSlateHandle");
//                        continue;
//                    }
//                }else if (sl.scriptLanguage == ScriptListener.LOGO) { // If this is a Logo script listener
//                    /* If the logo runtime hasn't already been started, initialize it now.
//                     */
//                    if (eSlateContainer.logoMachine == null) {
//                        eSlateContainer.initLogoEnvironment();
//                        eSlateContainer.startWatchingMicroworldForPrimitiveGroups();
//                    }
//                    ESlateHandle listenerHandle = (ESlateHandle) handleMap.at(i);
//                    /* Listeners for the events of the gr.cti.eslate.base.container.Microworld are saved
//                     * with a HierarchyComponentPath that starts with the handle of the ESlateContainer.
//                     * However these Java listener's setESlateHandle() has to be called with the
//                     * microworld's handle.
//                     */
//                    if (MicroworldListener.class.isAssignableFrom(sl.getListener().getClass()))
//                        listenerHandle = container.microworld.eslateMwd.getESlateHandle();
//                    sl.logoHandler.setLogoRuntime(listenerHandle,
//                                                  container.logoMachine,
//                                                  container.logoEnvironment,
//                                                  container.logoThread,
//                                                  container.tokenizer);
//                    container.microworld.activateLogoHandler(sl);
//                }else if (sl.scriptLanguage == ScriptListener.JAVASCRIPT) { // If this is a JS script listener
//                    /* If the javascript runtime hasn't already been started, initialize it now.
//                     */
//                    if (!container.javascriptInUse)
//                        container.registerJavascriptVariables();
//                    /* Listeners for the events of the gr.cti.eslate.base.container.Microworld are saved
//                     * with a HierarchyComponentPath that starts with the handle of the ESlateContainer.
//                     * However these Java listener's setESlateHandle() has to be called with the
//                     * microworld's handle.
//                     */
//                    ESlateHandle listenerHandle = (ESlateHandle) handleMap.at(i);
//                    if (MicroworldListener.class.isAssignableFrom(sl.getListener().getClass()))
//                        listenerHandle = container.microworld.eslateMwd.getESlateHandle();
//                    sl.jsHandler.setRuntimeInfo(listenerHandle);
//                    container.microworld.activateJSHandler(sl);
//                }
//
//  //                    System.out.println("Load 3");
//
//                /* Find the EventSetDcriptor, whose addListener method() accepts as an argument
//                 * the listener we want to add to the component.
//                 */
//                EventSetDescriptor eventDescriptor = null;
//                for (int t=0; t<eventDescriptors.length; t++) {
//  //                        System.out.println(eventDescriptors[i].getListenerType() + " --> " + sl.listener.getClass() + ", assignable: " + eventDescriptors[i].getListenerType().isAssignableFrom(sl.listener.getClass()));
//                    if (eventDescriptors[t].getListenerType().isAssignableFrom(sl.listener.getClass())) {
//                        eventDescriptor = eventDescriptors[t];
//                        break;
//                    }
//                }
//  //                    System.out.println(k + ". eventDescriptor: " + eventDescriptor);
//                if (eventDescriptor != null) {
//                    // Add the listener to the component.
//                    Method addListenerMethod = eventDescriptor.getAddListenerMethod();
//					Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
//					sl.addListenerMethod = addListenerMethod;
//					sl.removeListenerMethod = removeListenerMethod;
//					sl.target = component;
////                          System.out.println("CALLED the addListenerMethod method of the component addListenerMethod: " + addListenerMethod.getName() + ", component: " + component.getClass());
///*                    try{
//                        addListenerMethod.invoke(component, new Object[] {sl.listener});
//                    }catch (IllegalAccessException exc) {
//                        System.out.println("IllegalAccessException while adding listener");
//                    }catch (IllegalArgumentException exc) {
//                        System.out.println("IllegalArgumentException while adding listener");
//                    }catch (java.lang.reflect.InvocationTargetException exc) {
//                        System.out.println("InvocationTargetException while adding listener");
//                    }
//*/
//                }
//            }
//        }  // scriptListeners' binding
//		for (int i=0; i<scriptListenerArray.size(); i++) {
//			((ScriptListener) scriptListenerArray.at(i)).attachListener();
//		}
//
//        // Update the ScriptDialog, if it is shown.
////        if (container.scriptDialog != null && container.microworld.componentEventMgmtAllowed)
////            container.scriptDialog.setScriptListenerTree(getScriptListenerTree());
//    }

    /* This method is called when re-arranging the AWT containment hierarchy inside one or more
     * components.
     */
    public void updateAWTObjectHandle(Object object) {
        int index = objectArray.indexOf(object);
        if (index == -1) return; //No script attached to this object
        ESlateHandle newHandle = HierarchicalComponentPath.getContainingAWTObjectHandle(eSlateContainer, object);
        if (!handleArray.at(index).equals(newHandle))
            handleArray.put(index, newHandle);
    }

    public ScriptListenerHandleNode getScriptListenerTree() {
        if (scriptListenerTreeTopNode != null)
            return scriptListenerTreeTopNode;
        return createScriptListenerTree();
    }

    private ScriptListenerHandleNode createScriptListenerTree() {
        scriptListenerTreeTopNode = new ScriptListenerHandleNode(eSlateContainer.getESlateHandle()); // eSlateContainer.containerBundle.getString("ESlate"));
        for (int i=0; i<handleArray.size(); i++) {
            ESlateHandle handle = (ESlateHandle) handleArray.at(i);
            ScriptListenerHandleNode handleNode = null;
            if (handle == eSlateContainer.getESlateHandle())
                handleNode = scriptListenerTreeTopNode;
            else{
                // Create the chain of handles from the root to this handle
                ArrayList parentHandles = new ArrayList();
                ESlateHandle parentHandle = handle.getParentHandle();
                while (parentHandle != null && parentHandle != eSlateContainer.getESlateHandle()) {
                    parentHandles.add(0, parentHandle);
                    parentHandle = parentHandle.getParentHandle();
                }
                ScriptListenerHandleNode tmpNode = scriptListenerTreeTopNode;
                for (int k=0; k<parentHandles.size(); k++) {
                    parentHandle = (ESlateHandle) parentHandles.get(k);
                    ScriptListenerHandleNode parentHandleNode = tmpNode.getHandleNode(parentHandle, false);
                    if (parentHandleNode == null) {
                        parentHandleNode = new ScriptListenerHandleNode(parentHandle);
                        tmpNode.add(parentHandleNode);
                    }
                    tmpNode = parentHandleNode;
                }

                // Create the node for the 'handle', if it does not already exist.
                handleNode = tmpNode.getHandleNode(handle, false);
                if (handleNode == null) {
                    handleNode = new ScriptListenerHandleNode(handle);
                    tmpNode.add(handleNode);
                }
            }

            createScriptListenerNode(handleNode,
                                     (ScriptListener) scriptListenerArray.at(i),
                                     objectArray.at(i));
        }

        return scriptListenerTreeTopNode;
    }

    /* Returns the ScriptListenerNode which carries the supplied 'listener' and is anywhere
     * beneath the ScriptListenerHandleNode of the supplied 'handle'. Only the
     * ScriptListenerMethodNodes and ScriptListenerUINodes at any depth beneath the
     * ScriptListenerHandleNode along with the ScriptListenerNodes of the
     * ScriptListenerHandleNode are searched.
     */
    private ScriptListenerNode getScriptListenerNode(ESlateHandle handle, ScriptListener listener) {
        ScriptListenerNode[] nodes = getScriptListenerNodes(handle, true);
        for (int i=0; i<nodes.length; i++) {
            if (nodes[i].getListener() == listener)
                return nodes[i];
        }
        return null;
    }

    /* Return the ScriptListenerNodes of ScriptListenerHandleNode with the specified 'handle'.
     * If the second arguent is 'false', then the method returns only the ScriptListenerNodes
     * which are directly attached to the requested ScriptListenerHandleNode.
     * If the second argument is 'true', then the ScriptListenerNodes of all
     * the nodes of type ScriptListenerMethodNode and ScriptListenerUINode,
     * which exist in the subtree of the requested ScriptListenerHandleNode along with the
     * ScriptListenerNodes of the ScriptListenerHandleNode are returned.
     */
    private ScriptListenerNode[] getScriptListenerNodes(ESlateHandle handle, boolean allListeners) {
        if (scriptListenerTreeTopNode == null) return new ScriptListenerNode[0];
        ScriptListenerHandleNode handleNode = findHandleNode(scriptListenerTreeTopNode, handle);
        if (handleNode == null)
            return new ScriptListenerNode[0];
        if (!allListeners)
            return handleNode.getScriptListenerNodes();
        else{
//            ScriptListenerNode[] nodes = handleNode.getScriptListenerNodes();
//            ArrayList nodeList = new ArrayList(nodes.length);
//            for (int i=0; i<nodes.length; i++)
//                nodeList.add(nodes[i]);
            ArrayList nodeList = getAllScriptListenerNodes(handleNode, new ArrayList());
            ScriptListenerNode[] nodes = new ScriptListenerNode[nodeList.size()];
            for (int i=0; i<nodes.length; i++)
                nodes[i] = (ScriptListenerNode) nodeList.get(i);
            return nodes;
        }
    }

    private ArrayList getAllScriptListenerNodes(ScriptListenerTreeNode node,
                                                           ArrayList nodes) {
        ArrayList result = new ArrayList();
        ScriptListenerMethodNode[] methodNodes = node.getMethodNodes();
        for (int i=0; i<methodNodes.length; i++) {
            nodes = getAllScriptListenerNodes(methodNodes[i], nodes);
//            for (int k=0; k<result.size(); i++)
//                nodes.add(result.get(k));
        }
        ScriptListenerUINode[] uiNodes = node.getUINodes();
        for (int i=0; i<uiNodes.length; i++) {
            nodes = getAllScriptListenerNodes(uiNodes[i], nodes);
//            for (int k=0; k<result.size(); i++)
//                nodes.add(result.get(k));
        }

        ScriptListenerNode[] nds = node.getScriptListenerNodes();
        for (int i=0; i<nds.length; i++)
            nodes.add(nds[i]);
        return nodes;
    }

    /**
     * Searches from tree of the handles for the node which stores the specified handle. The search starts
     * from the root of the tree.
     * @param handle the handle which is searched for.
     * @return the handle's node.
     */
    public ScriptListenerHandleNode findHandleNode(ESlateHandle handle) {
        return findHandleNode(scriptListenerTreeTopNode, handle);
    }

    /**  Finds the ScriptListenerHandleNode with the supplied handle recursively.
     *   @param node the node under which the node of the specified handle is searched.
     *   @param handle the handle whose node is searched
     */
    private ScriptListenerHandleNode findHandleNode(ScriptListenerHandleNode node, ESlateHandle handle) {
        if (node == null) return null;
        return node.getHandleNode(handle, false); //\ true);
/*
        if (node.getHandle() == handle)
            return node;
        ScriptListenerHandleNode nd = node.getHandleNode(handle);
        if (nd == null) {
            int handleNodeCount = node.getHandleNodeCount();
            for (int i=0; i<handleNodeCount; i++) {
                ScriptListenerHandleNode nd1 = findHandleNode((ScriptListenerHandleNode) node.childHandles.get(i), handle);
                if (nd1 != null)
                    return nd1;
            }
        }
        return nd;
*/
    }

    /* Creates and adds to the tree a new ScriptListener node, which contains the
     * supplied ScriptListener and Object. The listener is attached to the
     * ScriptListenerHandleNode of ESlateHandle 'handle'. This method will expand the
     * tree by adding all the necessary ScriptListenerHandleNodes (for the parent
     * handles of the supplied 'handle') / ScriptListenerMethodNodes and ScriptListenerUINodes
     * (from the ScriptListenerHandleNode of the supplied 'handle' to the actual object
     * to which the ScriptListener is attached), if any of the above does not already exist.
     */
    private ScriptListenerNode addScriptListenerNode(ESlateHandle handle,
                                                     ScriptListener listener,
                                                     Object object) {
        if (scriptListenerTreeTopNode == null) return null;
        // First we check if the node already exists in the ScriptListener tree
        ScriptListenerHandleNode handleNode = findHandleNode(scriptListenerTreeTopNode, handle);
        // If there exists no ScriptListenerHandleNode for this handle, then we create one.
        if (handleNode == null) {
            handleNode = createHandleNode(handle);
            if (handleNode == null) return null;
        }

        return createScriptListenerNode(handleNode, listener, object);
    }

    /* Creates a new node for the supplied handle and adds to to the proper parent
     * ScriptListenerHandleNode.
     */
    private ScriptListenerHandleNode createHandleNode(ESlateHandle handle) {
        if (scriptListenerTreeTopNode == null) return null;
        if (scriptListenerTreeTopNode.getHandle() == handle) return scriptListenerTreeTopNode;

        /* Create the array of all the parent handles of this handle.
         */
        ArrayList parentHandles = new ArrayList();
        ESlateHandle parentHandle = handle.getParentHandle();
        while (parentHandle != null && parentHandle != eSlateContainer.getESlateHandle()) {
            parentHandles.add(0, parentHandle);
            parentHandle = parentHandle.getParentHandle();
        }

        /* Find the bottom-most handle in the 'parentHandles' array for which
         * ScriptListenerHandleNode exists.
         */
        ScriptListenerHandleNode existingParentHandleNode = scriptListenerTreeTopNode;
        int i=0;
        for (; i<parentHandles.size(); i++) {
            ESlateHandle h = (ESlateHandle) parentHandles.get(i);
            ScriptListenerHandleNode nd = findHandleNode(existingParentHandleNode, h);
            if (nd == null) break;
            existingParentHandleNode = nd;
        }
        /* Create the ScriptListenerHandleNodes for all the handles in the 'parentHandles'
         * array, for which a node does not exist in the ScriptListener tree.
         */
        for (; i<parentHandles.size(); i++) {
            ScriptListenerHandleNode node = new ScriptListenerHandleNode((ESlateHandle) parentHandles.get(i));
            existingParentHandleNode.add(node);
            existingParentHandleNode = node;
        }

        /* Create the ScriptListenerHandleNode for the supplied 'handle' and add it
         * to the ScriptListenerHandleNode of the handle's parent.
         */
        ScriptListenerHandleNode node = new ScriptListenerHandleNode(handle);
        existingParentHandleNode.add(node);
        return node;
    }

    /* Special version of createScriptListenerNode(). The ScriptListeners which are attached to
     * the gr.cti.eslate.base.container.Microworld, should not be displayed under a UI node with
     * the name 'Microworld'. Rather they are redirected to the ScriptListenerHandleNode of the
     * currently open in E-Slate gr.cti.eslate.base.ESlateMicroworld. This is a trick to appear
     * the listeners of the E-Slate's Microworld as listeners of the platform's ESlateMicroworld.
     * The Microworld object of these listeners has a HierarchicalComponentPath which contains
     * two parts: the first is the ESlateContainer and the second is the method getMicroworld()
     * of ESlateContainer.
     */
    private ScriptListenerNode createMicroworldScriptListenerNode(ScriptListener listener, Object obj) {
        //Create and add the node for this scriptListener
        ScriptListenerNode listenerNode = new ScriptListenerNode(
                              listener,
                              obj);
        ScriptListenerHandleNode mwdNode = scriptListenerTreeTopNode.getHandleNode(eSlateContainer.getMicroworld().getESlateMicroworld().getESlateHandle(), false);
        if (mwdNode == null)
            mwdNode = createHandleNode(eSlateContainer.getMicroworld().getESlateMicroworld().getESlateHandle());
        mwdNode.add(listenerNode);
        return listenerNode;
    }

    /* Creates a new ScriptListenerNode for the supplied ScriptListener and Object. The
     * ScriptListenerNode will be added to the sub-tree whose root will be the supplied
     * ScriptListenerHandleNode. All the ScriptListenerMethodNodes and ScriptListenerUINodes
     * which lead from the supplied 'handleNode' to the ScriptListenerNode of the actual
     * 'obj' to which the ScriptListener is attached, are created, if they do not already
     * exist.
     */
    private ScriptListenerNode createScriptListenerNode(ScriptListenerHandleNode handleNode,
                                                        ScriptListener listener,
                                                        Object obj) {
        /* Make the ScriptListeners attached to gr.cti.eslate.base.container.Microworld appear
         * as ScriptListeners of the currently open ESlateMicroworld.
         */
        if (Microworld.class.isAssignableFrom(obj.getClass()))
            return createMicroworldScriptListenerNode(listener, obj);

        /* Create the ScriptListenerMethodNodes/ScriptListenerUINodes, if the
         * listener is not attached directly to an object with ESlateHandle, but
         * to a sub-object, which was accessed through one of the method/UI
         * hierarchies in the Component editor.
         */
//        System.out.println("ScriptListener path: " + listener.pathToComponent);
        ScriptListenerHandleNode hNode = handleNode;
        ScriptListenerMethodNode mNode = null;
        ScriptListenerUINode iNode = null;
        if (listener.pathToComponent.path.length > 1) {
            String[] path = listener.pathToComponent.path;
            /* After each iteration of the next for loop only one of the hNode/
             * mNode/iNode is not null. The lengthy code in the for loop could
             * be a lot smaller if we used interfaces. It's simple but stupid
             * code.
             */
            // The first entry in the path is the handle. We neglect it.
            for (int k=1; k<path.length; k++) {
                /* If the next element of the path belongs to the AWT world, then
                 * a ScriptListenerUINode is created and attached to the previous node.
                 */
                if (listener.pathToComponent.nodeType[k] == HierarchicalComponentPath.AWT_COMPONENT_NAME) {
                    if (hNode != null) {
                        ScriptListenerUINode nd = hNode.getUINode(path[k]);
                        if (nd == null) {
                            nd = new ScriptListenerUINode(path[k]);
                            hNode.add(nd);
                        }
                        hNode = null; iNode = nd;
                    }else if (mNode != null) {
                        ScriptListenerUINode nd = mNode.getUINode(path[k]);
                        if (nd == null) {
                            nd = new ScriptListenerUINode(path[k]);
                            mNode.add(nd);
                        }
                        mNode = null; iNode = nd;
                    }else if (iNode != null) {
                        ScriptListenerUINode nd = iNode.getUINode(path[k]);
                        if (nd == null) {
                            nd = new ScriptListenerUINode(path[k]);
                            iNode.add(nd);
                        }
                        iNode = nd;
                    }
                }else if (listener.pathToComponent.nodeType[k] == HierarchicalComponentPath.METHOD_NAME) {
                    /* If the next element of the path belongs is accesed through some
                     * method, then a ScriptListenerMethodNode is created and
                     * attached to the previous node.
                     */
                    if (hNode != null) {
                        ScriptListenerMethodNode nd = hNode.getMethodNode(path[k]);
                        if (nd == null) {
                            nd = new ScriptListenerMethodNode(path[k]);
                            hNode.add(nd);
                        }
                        hNode = null; mNode = nd;
                    }else if (mNode != null) {
                        ScriptListenerMethodNode nd = mNode.getMethodNode(path[k]);
                        if (nd != null) {
                            nd = new ScriptListenerMethodNode(path[k]);
                            mNode.add(nd);
                        }
                        mNode = nd;
                    }else if (iNode != null) {
                        ScriptListenerMethodNode nd = iNode.getMethodNode(path[k]);
                        if (nd == null) {
                            nd = new ScriptListenerMethodNode(path[k]);
                            iNode.add(nd);
                        }
                        iNode = null; mNode = nd;
                    }
                }
            }
        }

        //Create and add the node for this scriptListener
        ScriptListenerNode listenerNode = new ScriptListenerNode(
                              listener,
                              obj);
        if (hNode != null)
            hNode.add(listenerNode);
        else if (mNode != null)
            mNode.add(listenerNode);
        else
            iNode.add(listenerNode);

        return listenerNode;
    }

    /* Return the TreePath from the root of the ScriptListener tree, to the
     * ScriptListenerNode for the supplied ScriptListener. If 'excludeListenerNode'
     * it true, the returned path does not contain the ScriptListenerNode of the
     * supplied 'listener'.
     */
    public TreePath getPath(ScriptListener listener, boolean excludeListenerNode) {
        if (scriptListenerTreeTopNode == null) return null;
        int index = indexOf(listener);
        if (index == -1) return null;
        ESlateHandle handle = getHandle(index);
        Object obj = getObject(index);
        /* If the ScriptListener refers to an event of the gr.cti.eslate.base.container.Microworld,
         * then its path is ESlateContainer --> getMicroworld() (i.e. its path starts from the
         * ESlateContainer on which the getMicroworld() method returns the tager object. However
         * the nodes for these listeners are attached to the currently open ESlateMicroworld's
         * handle node, for simplicity to the end-user. So when the path for such a listener is
         * being asked for, then we set the handle where the search will take place to the
         * ESlateMicroworld's handle, rather than the actual ESlateContainer handle.
         * See also createMicroworldScriptListenerNode() below.
         */
        if (handle == scriptListenerTreeTopNode.getHandle() && Microworld.class.isAssignableFrom(obj.getClass()))
            handle = eSlateContainer.getMicroworld().getESlateMicroworld().getESlateHandle();
        TreeNode node = getScriptListenerNode(handle, listener);

        if (excludeListenerNode)
            node = node.getParent();
        ArrayList pathList = new ArrayList();
        while (node != null) {
            pathList.add(0, node);
            node = node.getParent();
        }
//        pathList.add(0, scriptListenerTreeTopNode);

        return new TreePath(pathList.toArray());
    }

    void reloadAllJavaScriptListeners() {
        for (int i=0; i<scriptListenerArray.size(); i++) {
            ScriptListener slistener = (ScriptListener) scriptListenerArray.at(i);
            if (slistener.scriptLanguage == ScriptListener.JAVA) {
                slistener.dettachListener();
                slistener.instantiateJavaListener(slistener.loadJavaListener());
                slistener.attachListener();
                // Re-activate the new listener, by calling its "setESlateHandle" method.
                ESlateHandle hdl = (ESlateHandle) handleArray.at(i);
                if (MicroworldListener.class.isAssignableFrom(slistener.getListener().getClass())) {
                    hdl = eSlateContainer.getMicroworld().getESlateMicroworld().getESlateHandle();
                }
                try{
                    eSlateContainer.getMicroworld().activateListener(slistener, hdl);
                }catch (Throwable thr) {}
            }
        }
    }

    /* Whenever a component's name is changed, this method renames the appropriate
     * ScriptListenerHandleNode and updates the ScriptDialog.
     */
    protected void componentRenamed(ESlateHandle handle) {
        if (scriptListenerTreeTopNode == null) return;
        ScriptListenerHandleNode handleNode = findHandleNode(scriptListenerTreeTopNode, handle);
        if (handleNode != null) {
            handleNode.setUserObject(handle.getComponentName());
            if (eSlateContainer instanceof ESlateComposer) {
                ESlateComposer composer = (ESlateComposer) eSlateContainer;
                if (composer.scriptDialog != null)
                    composer.scriptDialog.updateScriptTree();
            }
        }
    }
}