package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;

import virtuoso.logo.InterpEnviron;
import virtuoso.logo.InterpreterThread;
import virtuoso.logo.Machine;
import virtuoso.logo.Tokenizer;


public class LogoScriptHandler implements InvocationHandler, Externalizable {
    static final long serialVersionUID = 12;
    public static final String STR_FORMAT_VERSION = "1.0";
    static final int FORMAT_VERSION = 2;

    transient ESlateHandle eSlateHandle = null;
    transient Tokenizer tokenizer = null;
    transient Machine logoMachine = null;
    transient InterpEnviron logoEnvironment = null;
    transient InterpreterThread logoThread = null;
    String varInitializationScript = "";
    transient String script = "";
    String methodName = "";
    transient Method[] eventGetterMethods;
    Class listenerClass = null;
    private ListenerSecurityProxy securityProxy = null;

    public LogoScriptHandler() {
    }

    LogoScriptHandler(String methodName, Class listenerType) {
        this.methodName = methodName;
        this.listenerClass = listenerType;
        initializeHandler();
    }

    void setSecurityProxy(ListenerSecurityProxy proxy) {
        this.securityProxy = proxy;
    }

    private void initializeHandler() {
        Method[] listenerMethods = listenerClass.getMethods();
        Method method = null;
        for (int i=0; i<listenerMethods.length; i++) {
            if (listenerMethods[i].getName().equals(methodName) && listenerMethods[i].getParameterTypes().length == 1) {
                method = listenerMethods[i];
                break;
            }
        }
        if (method == null) throw new RuntimeException("Cannot locate listeners method");
        Class[] paramClasses = method.getParameterTypes();
        Class eventClass = paramClasses[0];
        Method[] eventMethods = eventClass.getMethods();
        ArrayList getterMethods = new ArrayList();
        for (int i=0; i<eventMethods.length; i++) {
            Class returnType = eventMethods[i].getReturnType();
            if (java.lang.reflect.Modifier.isPublic(eventMethods[i].getModifiers()) &&
                returnType != null &&
                !returnType.getName().equals("void") &&
                eventMethods[i].getParameterTypes().length == 0) {
                if (returnType.equals(String.class)) {
                    getterMethods.add(eventMethods[i]);
                }else if (returnType.isPrimitive() || returnType.isArray()) {
                    getterMethods.add(eventMethods[i]);
                }
            }
        }
        eventGetterMethods = new Method[getterMethods.size()];
        for (int i=0; i<getterMethods.size(); i++)
            eventGetterMethods[i] = (Method) getterMethods.get(i);
    }

    public void setMethod(String methodName) {
        this.methodName = methodName;
    }

    public void setLogoRuntime(ESlateHandle handle, Machine logoMachine, InterpEnviron logoEnvironment, InterpreterThread logoThread, Tokenizer tokenizer) {
        this.eSlateHandle = handle;
        this.logoMachine = logoMachine;
        this.logoEnvironment = logoEnvironment;
        this.logoThread = logoThread;
        this.tokenizer = tokenizer;
//        System.out.println("SETTING TOKENIZER TO: " + tokenizer);
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("PROXY METHOD: " + method.getName());
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

//        System.out.println("Logo script executing..." + method.getName());
        if (tokenizer == null) return null;

        Object event = args[0];
//        System.out.println("args[0]: " + args[0]);
        varInitializationScript = "";
        for (int i=0; i<eventGetterMethods.length; i++) {
            String methodName = eventGetterMethods[i].getName();
            String variable;
            if (methodName.startsWith("get"))
                variable = methodName.substring(3);
            else
                variable = methodName;

            Class returnType = eventGetterMethods[i].getReturnType();
            if (returnType.equals(int.class) || returnType.equals(short.class) || returnType.equals(long.class)
                || returnType.equals(double.class)) {
                varInitializationScript = varInitializationScript + " \nmake \"" + variable + " \"|" + eventGetterMethods[i].invoke(event, new Object[0]) + "|";
            }else  if (returnType.isArray()) {
                Object array = eventGetterMethods[i].invoke(event, new Object[0]);
                int length = java.lang.reflect.Array.getLength(array);
                String arrayElementList = new String("[");
                for (int k=0; k<length; k++)
                    arrayElementList = arrayElementList + " |" + java.lang.reflect.Array.get(array, k) + "|";
                arrayElementList = arrayElementList + ']';
//                System.out.println("arrayElementList: " + arrayElementList);
                varInitializationScript = varInitializationScript + "\nmake \"" + variable + " " + arrayElementList;
            }else{
                varInitializationScript = varInitializationScript + "\nmake \"" + variable + " \"|" + eventGetterMethods[i].invoke(event, new Object[0]) + "|";
            }
        }
        if (eSlateHandle != null)
            varInitializationScript = varInitializationScript + " \nmake \"source" + " " + "\"|" + eSlateHandle.getComponentName() + "|";
//        System.out.println("varInitializationScript: " + varInitializationScript);

        ESlateContainer container = (ESlateContainer) eSlateHandle.getRootHandle().getComponent();
        if (container.currentlyOpenMwdFile != null)
            ScriptUtils.getInstance().loadMicroworldScripts(container, container.currentlyOpenMwdFile);
        
        String totalScript = varInitializationScript + '\n' + script;
//        System.out.println("TOTAL SCRIPT:  " + totalScript);

        if (eSlateHandle != null && eSlateHandle.getESlateMicroworld() != null) {
            try{
                securityProxy.grantAccess();
                ((ESlateContainer) eSlateHandle.getESlateMicroworld().getMicroworldEnvironment()).evaluateLogoScript(totalScript);
            }finally{
                securityProxy.revokeAccess();
            }
        }else
            System.out.println("Unable to execute Logo script. Probably the microworld has been closed");
        return null;
    }


    public void writeExternal(ObjectOutput out) throws IOException {

        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION, 2);
        fieldMap.put("Method name", methodName);
        fieldMap.put("Listener class", listenerClass);
        out.writeObject(fieldMap);
//        System.out.println("LogoScriptHandler writeExternal() size: " + ESlateContainerUtils.getFieldMapContentLength(fieldMap));
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        methodName = (String) fieldMap.get("Method name");
        listenerClass = (Class) fieldMap.get("Listener class");
        initializeHandler();
    }
}
