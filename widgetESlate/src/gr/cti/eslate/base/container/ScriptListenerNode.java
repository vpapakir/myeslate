package gr.cti.eslate.base.container;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;


/* ScriptListenerNode is a node used in the script listener tree to store
 * the actual script listeners and not the intermediate handles.
 * ScriptListenerHandleNode is used to store handles in this tree.
 */
public class ScriptListenerNode implements TreeNode, ScriptDialogNodeInterface {
    ScriptListener listener = null;
    // The decsriptor of the event to which ScriptListenet 'listener' is attached.
    EventSetDescriptor eventDescriptor = null;
    Object object = null; //The object to which the listener is attached
    String nodeName = null;
    MutableTreeNode parent = null;
    String[] eventVariables = null;

    public ScriptListenerNode(ScriptListener listener, Object object) {
        this.listener = listener;
        this.object = object;

        /* Name the node after the display name of the method descriptor for the method
         * of the event listener for which this ScriptListener was created.
         */
        EventSetDescriptor[] eventDescriptors = null;
        java.beans.BeanInfo objInfo = BeanInfoFactory.getBeanInfo(object.getClass());
        boolean named = false;
        if (objInfo != null) {
            eventDescriptors = objInfo.getEventSetDescriptors();
            /* Find the appropriate EventSetDescriptor. To locate the event descriptor
             * we use the listener's class name, which is stored in the ScriptListener.
             * In the case of Java scripts, the event listener is a descendant of some
             * known EventListener, whose class in the case of nodes for new(uncompiled)
             * listeners has not been created yet. So instead of looking for the event
             * descriptor using the ScriptListener's 'listenerClassName', we use the
             * ScriptListener's 'listenerSuperclassName'.
             */
            String listenerClassName = listener.getListenerClassName();
            if (listener.getScriptLanguage() == ScriptListener.JAVA)
                listenerClassName = listener.getListenerSuperIFName();
//System.out.println("ScriptListenerNode listenerClassName: " + listenerClassName);
            for (int i=0; i<eventDescriptors.length; i++) {
//System.out.println("eventDescriptors[i].getListenerType().getName(): " + eventDescriptors[i].getListenerType().getName());
                if (eventDescriptors[i].getListenerType().getName().equals(listenerClassName)) {
//                if (eventDescriptors[i].getListenerType().isAssignableFrom(listenerClass)) {
                    eventDescriptor = eventDescriptors[i];
//System.out.println("FOUND eventDescriptor: " + eventDescriptor);
//System.out.println();
                    MethodDescriptor[] methodDescriptors = eventDescriptor.getListenerMethodDescriptors();
                    // Find the appropriate method descripor and set its display name as
                    // the name of this node.
                    for (int k=0; k<methodDescriptors.length; k++) {
                        if (methodDescriptors[k].getMethod().getName().equals(listener.getMethodName())) {
                            nodeName = methodDescriptors[k].getDisplayName();
                            getEventVariables(methodDescriptors[k]);
                            named = true;
                            break;
                        }
                    }
                }
                if (named) break;
            }
        }
        if (!named)
            nodeName = listener.getMethodName();
    }

/*    public void add(MutableTreeNode node) {
        throw new RuntimeException("Cannot add nodes to a ScriptListenerNode");
    }

    public void insert(MutableTreeNode newChild, int childIndex) {
        throw new RuntimeException("Cannot insert nodes into a ScriptListenerNode");
    }
*/
    public Enumeration children() {
        return new Enumeration() {
            public boolean hasMoreElements() {
                return false;
            }
            public Object nextElement() {
                return null;
            }
        };
/*        return new Enumeration() {
            int currentPos = 0;
            int handleCount = childHandles.size();
            int total = handleCount + childListeners.size();
            public boolean hasMoreElements() {
                return (currentPos < total);
            }
            public Object nextElement() {
                Object result = null;
                if (currentPos < handleCount)
                    result = childHandles.get(currentPos);
                else
                    result = childListeners.get(currentPos-handleCount);
                currentPos++;
                return result;
            }
        };
*/
    }

    public boolean getAllowsChildren() {
        return false;
    }

    public TreeNode getChildAt(int childIndex) {
        return null;
/*        int handleCount = childHandles.size();
        if (childIndex < handleCount)
            return (TreeNode) childHandles.get(childIndex);
        return (TreeNode) childListeners.get(childIndex-handleCount);
*/
    }

    public int getChildCount() {
        return 0;
    }

    public int getIndex(TreeNode node) {
        return -1;
    }

    public TreeNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return true;
    }

    public String getNodeName() {
        return nodeName;
    }

    /* Returns the first node of type ScriptListenerHandleNode, which is parent of this
     * ScriptListenerNode.
     */
    public ScriptListenerHandleNode getParentHandleNode() {
        TreeNode parentNode = getParent();
        while (parentNode != null && !ScriptListenerHandleNode.class.isAssignableFrom(parentNode.getClass()))
            parentNode = parentNode.getParent();
        if (parentNode != null)
            return (ScriptListenerHandleNode) parentNode;
        else
            return null;
    }

    public void clear() {
        listener = null;
        eventDescriptor = null;
        object = null;
        nodeName = null;
        parent = null;
    }

    /* Find the variables which will be made available to the Logo/Javascript script.
     * These variables are the ones that are conveyed by the event object
     * received by the listener. This will be done only the first time the
     * listener node is created.
     */
    private void getEventVariables(MethodDescriptor methodDescriptor) {
        Class[] paramClasses = methodDescriptor.getMethod().getParameterTypes();
        Class eventClass = paramClasses[0];
        Method[] eventMethods = eventClass.getMethods();
        ArrayList getterMethods = new ArrayList();
        for (int i=0; i<eventMethods.length; i++) {
            Class returnType = eventMethods[i].getReturnType();
            if (Modifier.isPublic(eventMethods[i].getModifiers()) &&
                returnType != null &&
                !returnType.getName().equals("void") &&
                eventMethods[i].getParameterTypes().length == 0) {
                if (listener.getScriptLanguage() == ScriptListener.LOGO) {
                    if (returnType.equals(String.class)) {
                        getterMethods.add(eventMethods[i]);
                    }else if (returnType.isPrimitive() || returnType.isArray()) {
                        getterMethods.add(eventMethods[i]);
                    }
                }else if (listener.getScriptLanguage() == ScriptListener.JAVASCRIPT) {
                    getterMethods.add(eventMethods[i]);
                }
            }
        }
        int offset = 0;
        if (listener.getScriptLanguage() == ScriptListener.LOGO) {
            eventVariables = new String[getterMethods.size()+1];
            eventVariables[0] = "source";
            offset = 1;
        }else if (listener.getScriptLanguage() == ScriptListener.JAVASCRIPT) {
            eventVariables = new String[getterMethods.size()+5];
            eventVariables[0] = "source";
            eventVariables[1] = "sourceHandleName";
            eventVariables[2] = "event";
            eventVariables[3] = "microworld";
            eventVariables[4] = "eslate";
            offset = 5;
        }else{
//            eventVariables = new String[getterMethods.size()];
            eventVariables = new String[3];
            eventVariables[0] = "microworld";
            eventVariables[1] = "handle";
            eventVariables[2] = "source";
        }
        for (int i=0; i<getterMethods.size(); i++) {
            Method method = (Method) getterMethods.get(i);
            if (method.getName().startsWith("get"))
                eventVariables[i+offset] = method.getName().substring(3);
            else
                eventVariables[i+offset] = method.getName();
        }
    }

	// Methods of the ScriptDialogNodeInterface
	public void setName(String name) {
		listener.setScriptName(name);
	}

	public int getScriptLanguage() {
		return listener.getScriptLanguage();
	}

	public String[] getEventVariables() {
		return eventVariables;
	}

	public String getScript() {
		return listener.getScript();
	}

	public String getName() {
		return listener.getScriptName();
	}

	public Object getScriptContainer() {
		return listener;
	}

	public byte[] getListenerClassBytes(int index) {
		return listener.getListenerClassBytes()[index];
	}

	public void setListenerClassBytes(byte[][] bytes) {
		listener.listenerClassBytes =  bytes;
	}

	public void setListenerClassBytes(int index, byte[] bytes) {
        byte[][] listenerClassBytes = listener.getListenerClassBytes();
        listenerClassBytes[index] = bytes;
//		listener.listenerClassBytes[index] = bytes;
	}

	public void addInnerClass(String innerClassName, byte[] innerClassBytes) {
//System.out.println("innerClassName: " + innerClassName);
		listener.addInnerClass(innerClassName, innerClassBytes);
	}

	public int getCurrentLine() {
		return listener.currentLine;
	}

	public void setCurrentLine(int line) {
		if (listener != null) {
			listener.currentLine = line;
		}
	}

	public void resetInnerClasses() {
		listener.resetInnerClasses();
	}

    public ScriptListener getListener() {
        return listener;
    }

}