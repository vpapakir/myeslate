package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.EventListener;

public class ScriptListener implements java.io.Serializable {
	static final long serialVersionUID = 12;
//    public static final String STR_FORMAT_VERSION = "2.1";
	/* Changes
	 *   From 3 to 4:
	 *      Added innerClassNames, currentLine.
	 *   From 4 to 5:
     *      The source of the ScriptListener is not stored as part of the state
	 *      of the ScriptListener. It is stored in the ESlateContainer.SCRIPT_DIR_NAME
	 *      directory of the microworld structfile.
	 */
    public static final int FORMAT_VERSION = 5;
    public static final int LOGO = 0;
    public static final int JAVA = 1;
    public static final int JAVASCRIPT = 2;
    transient String componentName; // Not used since 0.9.7
    /** The name of the script file in the structured storage file. This value is re-assigned every-time the microwold
     * is saved.
     */
    transient String scriptFilename = null;
    transient String scriptName;
    private transient String script;
	/** The EventListener */
    transient Object listener;
	/** The java object the listener is attached to */
	transient Object target;
	/** The method which adds 'listener' to its 'target'. */
	transient Method addListenerMethod = null;
	/** The method which removes the 'listener' from its 'target' */
	transient Method removeListenerMethod = null;
    transient String methodName;
    transient String listenerClassName;
    /* The following is needed for Java ScriptListeners, whose listeners are
     * auto-generated descendants of some EventListener. This info is needed in
     * the constructor of the ScriptListenerNode, in order to locate the listener's
     * EventSetDescriptor.
     */
    transient String listenerSuperIFName;
    /* Until version 2.1 'listenerClassBytes' was of type byte[]. Then it changed
     * to byte[][], so that any inner classes that a listener defines, can be stored
     * along with the main listener class. The main listener class, always occupies
     * the position 0 of the 'listenerClassBytes'.
     */
    transient byte[][] listenerClassBytes;
	/** Stores the class names of all the inner classes
	 *  of the Script.
	 */
	transient String[] innerClassNames = new String[0];
//js    Boolean scriptInLogo = null;
    transient int scriptLanguage = LOGO;
    transient HierarchicalComponentPath2 pathToComponent = null;
    transient LogoScriptHandler logoHandler = null;
    transient JavascriptHandler jsHandler = null;
	/** This flag is set to 'true' when a component is saved individually, or when
     *  a component is copied in the clipboard. Normally the sources of the scripts
	 *  of the ScriptListeners are not saved with the ScriptListeners. They are stored
	 *  in individual files in the microworld structfile. However when a component is
	 *  saved in a file or in the clipboard, we want the sources of its scripts to
	 *  persist too.
	 *  @see #writeObject(ObjectOutputStream)
	 */
	static boolean saveScriptListenerSource = false;
	/**
	 * The current line of the listener's script.
	 */
	int currentLine = 0;

    private ScriptListener() {
    }

    public ScriptListener(String componentName, String methodName, String scriptName,
    String script, EventListener listener, Class listenerClass,
    byte[][] listenerClassBytes, int scriptLang, HierarchicalComponentPath2 path,
    InvocationHandler handler) {
//        if (componentName == null || componentName.trim().length() == 0)
//            throw new NullPointerException();
//        System.out.println("methodName: " + methodName);
        if (methodName == null || methodName.trim().length() == 0)
            throw new NullPointerException();
//        System.out.println("scriptName: " + scriptName);
        if (scriptName == null || scriptName.trim().length() == 0)
            throw new NullPointerException();
//        System.out.println("script: " + script);
        if (script == null || script.trim().length() == 0)
            throw new NullPointerException();
//        System.out.println("listener: " + listener);
        if (listener == null)
            throw new NullPointerException();
//        System.out.println("listenerClassBytes.lenth: " + listenerClassBytes.length);
//        if (listenerClassBytes == null || listenerClassBytes.length == 0)
//            throw new NullPointerException();
//        System.out.println("scriptInLogo: " + scriptInLogo);
//js        if (scriptInLogo == null)
        if (scriptLang != LOGO && scriptLang != JAVA && scriptLang != JAVASCRIPT)
            throw new NullPointerException();
        if (path == null)
            throw new NullPointerException("The script listener component path must not be null");

        this.componentName = componentName;
        this.methodName = methodName;
        this.scriptName = scriptName;
        this.script = script;
        this.listener = listener;
        this.listenerClassName = listenerClass.getName();
        Class[] ifs = listenerClass.getInterfaces();
        if (scriptLang == JAVA && ifs.length > 0)
            listenerSuperIFName = ifs[0].getName();
        this.listenerClassBytes = listenerClassBytes;
//js        this.scriptInLogo = scriptInLogo;
        scriptLanguage = scriptLang;
        this.pathToComponent = path;
//System.out.println("pathToComponent: " + pathToComponent);
        if (handler != null) {
            if (LogoScriptHandler.class.isAssignableFrom(handler.getClass()))
                this.logoHandler = (LogoScriptHandler) handler;
            else
                this.jsHandler = (JavascriptHandler) handler;
        }
    }

    /* Creates a ScriptListener without an actual EventListener. This is used
     * when defining new ScriptListeners in ObjecteventPanel.
     */
    public static final ScriptListener createEmptyScriptListener(
                          String methodName, Class listenerType,
                          int scriptLang, HierarchicalComponentPath2 path) {
        if (methodName == null || methodName.trim().length() == 0)
            throw new NullPointerException("Cannot create a ScriptListener with null method name");
        if (listenerType == null)
            throw new NullPointerException("Cannot create the ScriptListener. The listener's type cannot be null");
        if (scriptLang != LOGO && scriptLang != JAVA && scriptLang != JAVASCRIPT)
            throw new NullPointerException("Cannot create the ScriptListener. The language identifier is unknown");
        if (path == null)
            throw new NullPointerException("Cannot create the ScriptListener. The script listener component path must not be null");

        ScriptListener listener = new ScriptListener();
        listener.methodName = methodName;
        listener.scriptName = "";
        listener.script = "";
        listener.listenerClassName = listenerType.getName();
        Class[] ifs = listenerType.getInterfaces();
        if (scriptLang == JAVA && ifs.length > 0)
            listener.listenerSuperIFName = ifs[0].getName();
        listener.scriptLanguage = scriptLang;
        listener.pathToComponent = path;
        listener.listenerClassBytes = new byte[1][0];
        return listener;
    }

    public void setScriptName(String scriptName) {
        if (scriptName == null || scriptName.trim().length() == 0)
            return;
        this.scriptName = scriptName;
    }

    public void setScript(String script) {
        if (script == null)
            return;
        this.script = script;
        if (scriptLanguage == LOGO)
            logoHandler.setScript(script);
        else if (scriptLanguage == JAVASCRIPT)
            jsHandler.setScript(script);
    }

    /* Used to store any inner classes that a java event listener may define.
     */
    void addInnerClass(String innerClassName, byte[] listenerBytes) {
        int length = listenerClassBytes.length;
        byte[][] tmp = new byte[length+1][0];
        for (int i=0; i<listenerClassBytes.length; i++)
            tmp[i] = listenerClassBytes[i];
        tmp[length] = listenerBytes;
		String[] tmpClassNames = new String[length+1];
		if (innerClassNames == null) {
			innerClassNames = new String[0];
		}
		for (int i=0; i<innerClassNames.length; i++)
			tmpClassNames[i] = innerClassNames[i];
		tmpClassNames[length] = innerClassName;

        listenerClassBytes = tmp;
		innerClassNames = tmpClassNames;
    }

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getMethodName() {
        return methodName;
    }
    public String getScriptName() {
        return scriptName;
    }
    public String getScript() {
        return script;
    }
    public EventListener getListener() {
        return (EventListener) listener;
    }

    public byte[][] getListenerClassBytes() {
        return listenerClassBytes;
    }

    public int getScriptLanguage() {
        return scriptLanguage;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION, 2);
//js        fieldMap.put("Logo script", scriptInLogo); //Removed in format version 2.1
        fieldMap.put("Listener class name", listenerClassName);
        fieldMap.put("Listener class bytes", listenerClassBytes);
		fieldMap.put("Inner class names", innerClassNames);
        fieldMap.put("Component name", componentName);
        fieldMap.put("Method name", methodName);
        fieldMap.put("Script name", scriptName);
		if (saveScriptListenerSource) {
//System.out.println("ScriptListener writeObject() saving script: " + script);
			fieldMap.put("Script", script); // Change in version 5 of the storage format.
		}
        fieldMap.put("Script file name", scriptFilename);
		fieldMap.put("Current line", currentLine);
        fieldMap.put("PathToComponent", pathToComponent);
//System.out.println("ScriptListener Writing out pathToComponent: " + pathToComponent);
        fieldMap.put("Logo Handler", logoHandler);

        //2.1 format additions
        fieldMap.put("JS Handler", jsHandler);
        fieldMap.put("Script language", scriptLanguage);
//        fieldMap.put("Listener super class name", listenerSuperclassName);

        out.writeObject(fieldMap);
//        System.out.println("ScriptListener writeObject() size: " + ESlateContainerUtils.getFieldMapContentLength(fieldMap) + ", componentName: " + componentName + ", listenerClassName: " + listenerClassName);
/*        System.out.println("script.length: " + script.length());
        int length = 0;
        for (int i=0; i<listenerClassBytes.length; i++) {
            length = length + listenerClassBytes[i].length;
        }
        System.out.println("listenerClassBytes.length: " + length);
*/
    }

    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        Object firstObj = in.readObject();
        if (!StorageStructure.class.isAssignableFrom(firstObj.getClass())) {
            // Old time readExtermal()
            oldTimeReadObject(in, firstObj);
            return;
        }

        StorageStructure fieldMap = (StorageStructure) firstObj;
		int storageFormatVersion = fieldMap.getDataVersionID();
        Boolean scriptInLogo = (Boolean) fieldMap.get("Logo script");
        scriptLanguage = fieldMap.get("Script language", -1);
        if (scriptLanguage == -1 && scriptInLogo != null) {
            if (scriptInLogo.booleanValue())
                scriptLanguage = LOGO;
            else
                scriptLanguage = JAVA;
        }
        listenerClassName = (String) fieldMap.get("Listener class name");

        /* Backward compatibility. Before version 2.1 listenerClassBytes was
         * of type byte[]. Then it was changed to byte[][], so that inner
         * classes of the listeners can be stored, too.
         */
        Object obj = fieldMap.get("Listener class bytes");
        if (obj != null) {
            if (byte[].class.isAssignableFrom(obj.getClass())) {
                listenerClassBytes = new byte[1][0];
                listenerClassBytes[0] = (byte[]) obj;
            }else{
                listenerClassBytes = (byte[][]) obj;
            }
        }
//        listenerClassBytes = (byte[][]) fieldMap.get("Listener class bytes");
/*        Class listenerClass = null;
        SimpleClassLoader loader = SimpleClassLoader.ourLoader;
        try{
            listenerClass = loader.loadClass(listenerClassName);
//            System.out.println("The class is already loaded");
        }catch (ClassNotFoundException exc) {
            listenerClass = loader.loadClassFromByteArray(listenerClassBytes);
//            System.out.println("The class was reloaded from the byte array");
        }
*/
        componentName = (String) fieldMap.get("Component name");
        methodName = (String) fieldMap.get("Method name");
        scriptName = (String) fieldMap.get("Script name");
        scriptFilename = (String) fieldMap.get("Script file name");
        script = (String) fieldMap.get("Script");
		currentLine = fieldMap.get("Current line", 1);
		innerClassNames = (String[]) fieldMap.get("Inner class names");
        pathToComponent = (HierarchicalComponentPath2) fieldMap.get("PathToComponent");

        /* Since format version 2.0 the logo script binding mechanism uses Proxies instead
         * of listener classes which are generated dynamically using jikes. So instead of
         * storing class bytecode, we store 'LogoScriptHandlers' for Logo scripts only since
         * version 2.0.
         */
//js        if (!scriptInLogo.booleanValue() || fieldMap.getDataVersion().equals("1.0")) {
        String dataVersion = fieldMap.getDataVersion();
        if (scriptLanguage == JAVA || dataVersion.equals("1.0")) {
            Class listenerClass = loadJavaListener();
			if (listenerClass == null) return;
                //System.out.println("The class was reloaded from the byte array");

            // If this is a Java script, then create the listener the usual way
//js            if (!scriptInLogo.booleanValue()) {
            if (scriptLanguage == JAVA) {
				instantiateJavaListener(listenerClass);
            }else{
                /* If this is a Logo script, then we have to create the listener through the
                 * LogoScriptHandler and not through the old way. This is straightforward, except
                 * one part. The 'listenerClass' is a custom class that implements an EventListener
                 * interface and was generated and compiled automatically in a previous session. The
                 * listener class is a Class and not an interface. The Proxy mechanism requires
                 * interfaces. So we check if the class is one of the automaticcaly generated classes
                 * -this check should never fail normally. This is done by checking if the
                 * 'listenerClassName' contains '_'. If it does, then we take the first interface it
                 * implements and make it the 'listenerClass' for which a 'LogoScriptHandler'
                 * is created.
                 */
                if (listenerClassName.indexOf('_') != -1)
                    listenerClass = listenerClass.getInterfaces()[0];
                logoHandler = new LogoScriptHandler(methodName, listenerClass);
                logoHandler.setScript(script);
                listener = java.lang.reflect.Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                  new Class[] { listenerClass },
                                                  logoHandler);
            }
        }else{
            /* Since version 2.1 jsHandler was added. */
            logoHandler = (LogoScriptHandler) fieldMap.get("Logo Handler");
            jsHandler = (JavascriptHandler) fieldMap.get("JS Handler");
            try{
                if (logoHandler != null) {
                    logoHandler.setScript(script);
                    listener = java.lang.reflect.Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                  new Class[] { logoHandler.listenerClass },
                                                  logoHandler);
                    listenerClassName = logoHandler.listenerClass.getName();
                }else{
                    jsHandler.setScript(script);
                    listener = java.lang.reflect.Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                  new Class[] { jsHandler.listenerClass },
                                                  jsHandler);
                    listenerClassName = jsHandler.listenerClass.getName();
                }
            }catch (Throwable thr) {
                thr.printStackTrace();
            }
        }
    }

    private void oldTimeReadObject(java.io.ObjectInputStream in, Object firstObj)
    throws java.io.IOException, ClassNotFoundException {
//    try{
        Boolean scriptInLogo = (Boolean) firstObj; //(Boolean) in.readObject();
        if (scriptInLogo.booleanValue())
            scriptLanguage = LOGO;
        else
            scriptLanguage = JAVA;
        listenerClassName = (String) in.readObject();
        byte[] b = (byte[]) in.readObject();
        listenerClassBytes = new byte[1][0];
        listenerClassBytes[0] = b;
        Class listenerClass = null;
        SimpleClassLoader loader = SimpleClassLoader.listenerLoader;
        try{
            listenerClass = loader.loadClass(listenerClassName);
//            System.out.println("The class is already loaded");
        }catch (ClassNotFoundException exc) {
            listenerClass = loader.loadClassFromByteArray(listenerClassBytes[0]);
//            System.out.println("The class was reloaded from the byte array");
        }
        componentName = (String) in.readObject();
        methodName = (String) in.readObject();
        scriptName = (String) in.readObject();
        script = (String) in.readObject();
        try{
            HierarchicalComponentPath path = (HierarchicalComponentPath) in.readObject();
            if (path != null) {
                if (pathToComponent == null) pathToComponent = new HierarchicalComponentPath2();
                pathToComponent.nodeType = path.nodeType;
                pathToComponent.path = path.path;
            }
        }catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("No path to listener's source component");
        }

        /* If a 'componentName' is restored but the 'pathToComponent' is null, then
         * this is a 0.9.6 (or earlier) E-Slate version script listener. Try to create
         * a HierarchicalComponentPath out of the restored 'componentName'.
         */
        if (componentName != null && componentName.length() != 0 && (pathToComponent == null || pathToComponent.path == null)) {
//            System.out.println("Converting 0.9.6 script listener for component " + componentName);
            pathToComponent = new HierarchicalComponentPath2(HierarchicalComponentPath.getComponentHierarchyNames(componentName));
        }
//        scriptInLogo = Boolean.TRUE;
//        System.out.println("Reading listener...");
        try{
            listener = listenerClass.newInstance();
        }catch (Exception exc) {
            System.out.println("Cant restore scriptListener");
        }
//        System.out.println("1. listener: " + listener);

//    }catch (Exception exc1) {
//        exc1.printStackTrace();
//        System.out.println(exc1.getMessage() + ", " + exc1.getClass());
//    }
    }

	void attachListener() {
		if (listener == null) return;

		if (addListenerMethod == null) {
			System.out.println("addListenerMethod is null. Unable to attach listener: " + listener);
			return;
		}
		if (target == null) {
			System.out.println("target is null. Unable to attach listener: " + listener);
			return;
		}
		try{
			addListenerMethod.invoke(target, new Object[] {listener});
		}catch (IllegalAccessException exc) {
			System.out.println("IllegalAccessException while adding listener");
		}catch (IllegalArgumentException exc) {
			System.out.println("IllegalArgumentException while adding listener");
		}catch (java.lang.reflect.InvocationTargetException exc) {
			System.out.println("InvocationTargetException while adding listener");
		}
	}

	void dettachListener() {
		if (listener == null) return;

		if (removeListenerMethod == null) {
			System.out.println("removeListenerMethod is null. Unable to dettach listener: " + listener);
			return;
		}
		if (target == null) {
			System.out.println("target is null. Unable to dettach listener: " + listener);
			return;
		}
		try{
			removeListenerMethod.invoke(target, new Object[] {listener});
		}catch (IllegalAccessException exc) {
			System.out.println("IllegalAccessException while removing listener");
		}catch (IllegalArgumentException exc) {
			System.out.println("IllegalArgumentException while removing listener");
		}catch (java.lang.reflect.InvocationTargetException exc) {
			System.out.println("InvocationTargetException while removing listener");
		}
	}

	Class loadJavaListener() {
		SimpleClassLoader loader = SimpleClassLoader.listenerLoader;
		Class listenerClass = null;
		try{
//System.out.println("listenerClassName: " + listenerClassName);
			listenerClass = loader.loadClass(listenerClassName);
//            System.out.println("The class is already loaded");
		}catch (ClassNotFoundException exc) {
			//System.out.println("loading from " + listenerClassBytes[0]);
			try{
				listenerClass = loader.loadClassFromByteArray(listenerClassBytes[0]);
				// If the listener declared any inner class, then reload them too.
				for (int i=1; i<listenerClassBytes.length; i++) {
					loader.loadClassFromByteArray(listenerClassBytes[i]);
				}
			}catch (Throwable thr) {
				thr.printStackTrace();
				System.out.println("Unable to load listener: " + listenerClassName);
				return null;
			}
		}
		return listenerClass;
	}

	void instantiateJavaListener(Class listenerClass) {
		try{
			listener = listenerClass.newInstance();
//System.out.println("ScriptListener listenerClassName: " + listenerClassName);
			listenerSuperIFName = listenerClass.getInterfaces()[0].getName();
//System.out.println("ScriptListener listenerSuperIFName: " + listenerSuperIFName);
		}catch (Exception exc) {
			exc.printStackTrace();
			System.out.println("Cant restore scriptListener");
		}

	}

	public void resetInnerClasses() {
		innerClassNames = new String[0];
		listenerClassBytes = new byte[0][0];
	}

    public String getListenerClassName() {
        return listenerClassName;
    }

    public String getListenerSuperIFName() {
        return listenerSuperIFName;
    }

/*
    public void setListenerClassBytes(byte[][] listenerClassBytes) {
        this.listenerClassBytes = listenerClassBytes;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }
*/
}


