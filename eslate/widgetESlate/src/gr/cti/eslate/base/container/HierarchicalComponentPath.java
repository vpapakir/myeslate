package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlatePart;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Method;

import com.objectspace.jgl.Array;


public class HierarchicalComponentPath { //implements java.io.Serializable {
    public static final int METHOD_NAME = 0;
    public static final int AWT_COMPONENT_NAME = 1;
    /* A HierarchicalComponentPath always starts from an ESlatePart, i.e. a component
     * which owns an ESlateHandle. The path marks the way to get to an object starting
     * from 'topHandle'. The 'topHandle' does not have to be a first level ESlateHandle.
     * It can be at any depth in the ESlate component hierarchy. E-Slate (the patform)
     * has the ability to track down a handle, no matter how deep down the component
     * hierarchy that might be. So we use this ability of the E-Slate platform and
     * combine it with the ability of the HierarchicalComponentPath to further track
     * down objects, which are not part of the E-Slate component hierarchy.
     */
    ESlateHandle topHandle = null;
    String[] path = null;
    int[] nodeType = null;
    int[] methodIndex = null; //Used for methods with indices only, .i.e. methods that have a single int index parameter
    static final long serialVersionUID = 12;

    public HierarchicalComponentPath() {
        path = new String[0];
        nodeType = new int[0];
        methodIndex = new int[0];
    }

    /** Constructs a new HierarchicalComponentPath which is based on an existing one.
     *  The last 'fromDepth' elements of the original path are included in the new
     *  path.
     */
    public HierarchicalComponentPath(HierarchicalComponentPath hp, int fromDepth) {
        int size = hp.path.length - fromDepth;
        path = new String[size];
        nodeType = new int[size];
        methodIndex = new int[size];
        for (int i=0; i<path.length; i++) {
            path[i] = hp.path[i+fromDepth];
            nodeType[i] = nodeType[i+fromDepth];
            methodIndex[i] = methodIndex[i+fromDepth];
        }
    }

    public HierarchicalComponentPath(String[] path, int[] typeArray, int[] indexArray) {
        this.path = path;
        this.nodeType = typeArray;
        if (indexArray == null) {
            indexArray = new int[path.length];
            for (int i=0; i<indexArray.length; i++)
                indexArray[i] = -1;
        }
        this.methodIndex = indexArray;
    }

    public HierarchicalComponentPath(String[] path) {
        this.path = path;
        nodeType = new int[path.length];
        methodIndex = new int[path.length];
        for (int i=0; i<nodeType.length; i++) {
            nodeType[i] = AWT_COMPONENT_NAME;
            methodIndex[i] = -1;
        }
    }

    public boolean append(String name, int type, int index) {
        if (type != METHOD_NAME && type != AWT_COMPONENT_NAME)
            return false;

        int depth = path.length;
        resize(depth+1);
        path[depth] = name;
        nodeType[depth] = type;
        methodIndex[depth] = index;
        return true;
    }

    public boolean insert(int pos, String name, int type, int index) {
        if (type != METHOD_NAME && type != AWT_COMPONENT_NAME)
            return false;
        int length = path.length;
        if (pos < 0 || pos >= length)
            return false;

        String[] tmp = new String[length+1];
        int[] tmp2 = new int[length+1];
        int[] tmp3 = new int[length+1];
        for (int i=0; i<pos; i++) {
            tmp[i] = path[i];
            tmp2[i] = nodeType[i];
            tmp3[i] = methodIndex[i];
        }
        tmp[pos] = name;
        tmp2[pos] = type;
        tmp3[pos] = index;
        for (int i=pos+1; i<length+1; i++) {
            tmp[i] = path[i];
            tmp2[i] = nodeType[i];
            tmp3[i] = methodIndex[i];
        }

        path = tmp;
        nodeType = tmp2;
        methodIndex = tmp3;
        return true;
    }

    private void resize(int size) {
        String[] tmp = new String[size];
        int[] tmp2 = new int[size];
        int[] tmp3 = new int[size];
        for (int i=0; i<path.length; i++) {
            tmp[i] = path[i];
            tmp2[i] = nodeType[i];
            tmp3[i] = methodIndex[i];
        }
        path = tmp;
        nodeType = tmp2;
        methodIndex = tmp3;
    }

    public int depth() {
        if (path == null) return 0;
        return path.length;
    }

    /**
     * Returns the path in not notation, that is <part1>.<part2>...<partN>
     */
    public String getPathInDotNotation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            sb.append(path[i]);
            if (i != path.length-1) sb.append('.');
        }
        return sb.toString();
    }
    /**
     * Returns the part of the path at the specified index.
     * @param index the index of the path's part.
     * @return the part
     */
    public String getPathPart(int index) {
        return path[index];
    }

    /**
     * Sets the part of the part of the path at the specified index.
     * @param index the index of the part
     * @param value the new value of the part
     */
    public void setPathPart(int index, String value) {
        path[index] = value;
    }

    public String toString() {
        if (path == null) return "";

        StringBuffer str = new StringBuffer();
        for (int i=0; i<path.length; i++) {
            str.append("(");
            str.append(path[i]);
            str.append(", ");
            str.append(nodeType[i]);
            str.append(", ");
            str.append(methodIndex[i]);
            str.append(") ");
            if (i != path.length-1)
                str.append("--> ");
        }
        return str.toString();
    }

    static Object getObject(Object parentObject, String name, int type, int objectIndex) {
//System.out.println("parentObject: " + parentObject.getClass().getName() + ", name: " + name + ", type: " + type + ", HierarchicalComponentPath2.AWT_COMPONENT_NAME: " + HierarchicalComponentPath2.AWT_COMPONENT_NAME);
        if (name == null) return null;
        if (type != HierarchicalComponentPath2.AWT_COMPONENT_NAME &&
            type != HierarchicalComponentPath2.METHOD_NAME)
            return null;

        if (type == HierarchicalComponentPath2.AWT_COMPONENT_NAME) {
            if (!Container.class.isAssignableFrom(parentObject.getClass()))
                return null;
            Container container = (Container) parentObject;
            Component[] comps = null;
            if (javax.swing.JMenu.class.isAssignableFrom(container.getClass()))
                comps = ((javax.swing.JMenu) container).getMenuComponents();
            else
                comps = container.getComponents();
            for (int i=0; i<comps.length; i++) {
                /* If the container has not been serialized (e.g. if the whole E-Slate component
                 * is Externalizable (not Serializable) and this container is not explicitly saved,
                 * then the component names which were established during saveMap() have been lost.
                 * Therefore we have to rename the container's components.
                 */
                if (comps[i].getName() == null) {
                  nameContainerComponents(container);
                }
//System.out.println("name: " + name + ", comps[i].getName(): " + comps[i].getName());
                if (name.equals(comps[i].getName())) {
//                    System.out.println("getObject() returning: " + comps[i]);
                    return comps[i];
                }
            }
        }else if (type == HierarchicalComponentPath2.METHOD_NAME) {
            try{
                Method getter = null;
                Object obj = null;
                if (objectIndex == -1) {
                    //System.out.println("ScriptListenerMap parentObject: " + parentObject.getClass() + ", method: " + name);
                    /* Check for old time Database listeners (before DBEngine version 1.9/ESlate 1.4) */
                    if (name.equals("getCDatabase")) {
                            System.out.println("This microworld contains a gr.cti.eslate.event.DatabaseListener attached to a Database.\n" +
                                                "This listener type is now obsolete, so the listener will not be added. As a result \n" +
                                                "the microworld will loose some functionality. If you want this functionality, it is suggested, that \n" +
                                                "you load this microworld with E-Slate version 1.2 or 1.3, save the listener's action to\n " +
                                                "a file, dettach the listener, then open the microworld with a higher version of E-Slate and\n " +
                                                "reattach the listener.");
                            return null;
                    };
//System.out.println("name: " + name + ", parentObject: " + parentObject);
                    getter = parentObject.getClass().getMethod(name, new Class[0]);
                    obj = getter.invoke(parentObject, new Object[0]);
                }else{
                    getter = parentObject.getClass().getMethod(name, new Class[] {int.class});
                    obj = getter.invoke(parentObject, new Object[] {new Integer(objectIndex)});
                }
                return obj;
            }catch (Throwable thr) {
                thr.printStackTrace();
                return null;
            }
        }
//        System.out.println("getComponent()  Returning null");
        return null;
    }

    /* Given a special component name (the ones used in scriptListeners to track the component
     * hierarchy to a component) return an String array which contains the names of all of the
     * components in the hierarchy. The name of the top-most component is the name of its
     * ESlateHandle (the top-most component HAS to be an ESlatePart). The format of the component
     * names stored in script listeners is straight forward. It contains the names of all  of the
     * the components in the hierarchy, delimited by '$' characters. Thus the '$' character is invalid
     * (cannot be used) in component names.
     */
    static String[] getComponentHierarchyNames(String compoName) {
        if (compoName == null || compoName.trim().length() == 0)
            return null;

        Array nameArray = new Array();
        /* Special case -- The component is a top level one i.e. one with handle
         */
        if (compoName.indexOf('$') == -1)
            nameArray.add(compoName);
        else{
            int lastSeparatorIndex = 0, separatorIndex = 0;
            while ((separatorIndex = compoName.indexOf('$', lastSeparatorIndex)) != -1) {
                nameArray.add(compoName.substring(lastSeparatorIndex, separatorIndex));
                lastSeparatorIndex = separatorIndex+1;
            }
            nameArray.add(compoName.substring(lastSeparatorIndex));
        }

        int size = nameArray.size();
//        System.out.println("getComponentHierarchyNames() nameArray.size(): "+ nameArray.size());
        String[] names = new String[size];
        for (int i=0; i<size; i++) {
            names[i] = (String) nameArray.at(i);
//            System.out.print(names[i] + "-->");
        }
//        System.out.println();
        return names;
    }

//    static Object getNestedObject(ESlateContainer eSlateContainer, Object root, HierarchicalComponentPath2 compLocation) {
    public static Object getNestedObject(ESlateHandle rootHandle, Object root, HierarchicalComponentPath2 compLocation) {
        if (compLocation.path == null || compLocation.path.length == 0) return null;
//        System.out.println("getNestedObject() compLocation: " + compLocation + ", root: " + root);
        Object comp = null;
        for (int i=0; i<compLocation.path.length; i++) {
            if (i == 0) {
/*                if (compLocation.path[0].equals("ESlateContainer"))
                    comp = eSlateContainer;
                else{
*/                    /* The first component has to be a top level component WITH an ESlateHandle in the
                     * 'currentMicroworld'.
                     */
                    try{
//                        ESlateHandle h = eSlateContainer.getESlateHandle().getHandle(compLocation.path[0]);
                        ESlateHandle h = rootHandle.getHandle(compLocation.path[0]);
                        if (h != null)
                            comp = h.getComponent(); // .microworld.eslateMwd.getComponent(compLocation.path[0]);
                        else
                            comp = null;
                    }catch (Exception exc) {return null;}
//                }
            }else{
//                System.out.println("getObject() compLocation.path[i]: " + compLocation.path[i] + ", root: " + root);
                comp = getObject(root, compLocation.path[i], compLocation.nodeType[i], compLocation.methodIndex[i]);
            }
//              }
//            System.out.println("1. comp: " + comp);
            if (comp == null) {
                System.out.println("Unable to find object for compLocation: " + compLocation + ", i: " + i);
                return null;
            }
//            if (Container.class.isAssignableFrom(comp.getClass()))
                root = comp;
//            else
//                return null;
        }
//        System.out.println("getNestedObject() returning: " + comp.getClass());
        return comp;
    }

    /* Gives a unique name (among its siblings) to this component, based on the names of its
     * siblings and of course its class name.
     */
    public static void nameComponent(Component comp) {
        String compoName = comp.getName();
        if (compoName == null)
            compoName = comp.getClass().getName().substring(comp.getClass().getName().lastIndexOf('.')+1);

        Container parent = comp.getParent();
        if (parent == null) {
            comp.setName(compoName);
            return;
        }

        /*
         * 1  Use the name returned by the Component's getName() method. If this name is null
         *    use the component's class name as its name.
         * 2  If the produced name is already used by a sibling of this object, then create
         *    a new unique component name by appending a '_' and a number to the class name,
         *    which remains the base of the component.
         */
        int count = 0;
        boolean checkAgain = true;
        while (checkAgain) {
            checkAgain = false;
            for (int i=0; i<parent.getComponentCount(); i++) {
                if (compoName.equals(parent.getComponent(i).getName())) {
                    count++;
                    if (count == 1)
                        compoName = compoName + '_' + count;
                    else{
                        int sepIndex = compoName.indexOf('_');
                        compoName = compoName.substring(0, sepIndex+1) + count;
                    }
                    checkAgain = true;
                }
            }
        }

        comp.setName(compoName);
    }

    /* Gives a unique name (among its siblings) to each AWT/Swing component in this Container, based
     * on the names of its siblings and of course its class name.
     */
    static void nameContainerComponents(Container parent) {
//        System.out.println("nameContainerComponents() parent: " + parent);
        if (parent == null) return;
        Component[] components = parent.getComponents();
        String[] givenNames = new String[components.length];
        for (int i=0; i<components.length; i++) {
            Component comp = components[i];
            String compoName = comp.getName();
            if (compoName == null)
                compoName = comp.getClass().getName().substring(comp.getClass().getName().lastIndexOf('.')+1);
            else{
                givenNames[i] = compoName;
                continue;
            }

            /*
             * 1  Use the name returned by the Component's getName() method. If this name is null
             *    use the component's class name as its name.
             * 2  If the produced name is already used by a sibling of this object, then create
             *    a new unique component name by appending a '_' and a number to the class name,
             *    which remains the base of the component.
             */
            int count = 0;
            boolean checkAgain = true;
            while (checkAgain) {
                checkAgain = false;
                for (int k=0; k<i; k++) {
                    if (compoName.equals(givenNames[k])) {
                        count++;
                        if (count == 1)
                            compoName = compoName + '_' + count;
                        else{
                            int sepIndex = compoName.indexOf('_');
                            compoName = compoName.substring(0, sepIndex+1) + count;
                        }
                        checkAgain = true;
                    }
                }
            }

            comp.setName(compoName);
            givenNames[i] = compoName;
        }
    }

    static ESlateHandle getContainingAWTObjectHandle(ESlateContainer eSlateContainer, Object object) {
        if (eSlateContainer.getMicroworld() == null) return null;
        Object[] mwdObjects = eSlateContainer.getMicroworld().getESlateMicroworld().getComponents();
//        if (ESlatePart.class.isAssignableFrom(object.getClass()))
//            return ((ESlatePart) object).getESlateHandle();
        if (ESlateContainerUtils.implementsInterface(object.getClass(), ESlatePart.class))
            return ESlateContainerUtils.getESlateHandle(object);

        if (Container.class.isAssignableFrom(object.getClass())) {
            Container container = (Container) object;
            boolean found = false;
            while (container != null && (!ESlateContainerUtils.implementsInterface(object.getClass(), ESlatePart.class))) { //(!ESlatePart.class.isAssignableFrom(container.getClass()))) {
                /* We also check with object equality cause there are components which have been
                 * turned into ESlateParts by ESlate, without being ESlateParts themselves.
                 */
                for (int i=0; i<mwdObjects.length; i++) {
                    if (mwdObjects[i] == container) {
                        found = true;
                        break;
                    }
                }
                if (found) break;
                container = container.getParent();
            }

            if (container != null) {
//                if (ESlatePart.class.isAssignableFrom(container.getClass()))
//                    return ((ESlatePart) container).getESlateHandle();
                if (ESlateContainerUtils.implementsInterface(container.getClass(), ESlatePart.class))
                    return ESlateContainerUtils.getESlateHandle(container);
                else
                    return eSlateContainer.getMicroworld().getESlateMicroworld().getComponentHandle(container);
            }
        }
        return null;
    }

}