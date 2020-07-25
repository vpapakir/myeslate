package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ScriptManager;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.StringBaseArray;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class JavascriptHandler implements InvocationHandler, Externalizable {
    static final long serialVersionUID = 12;
    public static final String STR_FORMAT_VERSION = "1.0";
    static final int FORMAT_VERSION = 2;

    transient ESlateHandle eSlateHandle = null;
//    transient Tokenizer tokenizer = null;
//    transient Machine logoMachine = null;
//    transient InterpEnviron logoEnvironment = null;
//    transient InterpreterThread logoThread = null;
    String varInitializationScript = "";
    transient String script = "";
    String methodName = "";
    transient Method[] eventGetterMethods;
    Class listenerClass = null;
    private ListenerSecurityProxy securityProxy = null;

    public JavascriptHandler() {
    }

    public JavascriptHandler(String methodName, Class listenerType) {
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
/*                if (returnType.equals(String.class)) {
                    getterMethods.add(eventMethods[i]);
                }else if (returnType.isPrimitive() || returnType.isArray()) {
*/
                    getterMethods.add(eventMethods[i]);
//if (returnType.isArray())
//System.out.println("Array getter: " + eventMethods[i]);
//                }
            }
        }
        eventGetterMethods = new Method[getterMethods.size()];
        for (int i=0; i<getterMethods.size(); i++)
            eventGetterMethods[i] = (Method) getterMethods.get(i);
    }

    public void setMethod(String methodName) {
        this.methodName = methodName;
    }

    public void setRuntimeInfo(ESlateHandle handle) {
        this.eSlateHandle = handle;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("JS PROXY METHOD: " + method.getName());
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

//        System.out.println("Javascript executing..." + method.getName());


        StringBaseArray scriptMgrRegisteredNames = new StringBaseArray();
        ScriptManager scriptMgr = ScriptManager.getScriptManager();
        Object event = args[0];
//        System.out.println("args[0]: " + args[0]);
        scriptMgr.registerName("_theCurrentEvent", event);
        scriptMgrRegisteredNames.add("_theCurrentEvent");
        String headerScript = "\nvar event=_theCurrentEvent;";

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
                varInitializationScript = varInitializationScript + " \nvar " + variable + "=" + eventGetterMethods[i].invoke(event, new Object[0]) + ';';
            }else  if (returnType.isArray()) {
                Object array = eventGetterMethods[i].invoke(event, new Object[0]);
                int length = java.lang.reflect.Array.getLength(array);
                String arrayElementList = new String("{");
                for (int k=0; k<length; k++) {
                    if (k != 0)
                        arrayElementList = arrayElementList + ", ";
                    arrayElementList = arrayElementList + java.lang.reflect.Array.get(array, k);
                }
                arrayElementList = arrayElementList + '}';
                System.out.println("Declaring array arrayElementList: var " +  variable + "=" + arrayElementList + ";");
                varInitializationScript = varInitializationScript + "\nvar " + variable + "=" + arrayElementList + ';';
            }else{
                Object varValue = eventGetterMethods[i].invoke(event, new Object[0]);
                String varName = "_jsVariable" + variable;
//                scriptMgr.registerName(varName, varValue);
//System.out.println("Registering value: " + varValue.getClass() + ", under name: " + varName);
//                scriptMgrRegisteredNames.add(varName);
                varInitializationScript = varInitializationScript + "\nvar " + variable + "=_theCurrentEvent." + eventGetterMethods[i].getName() + "()";
//System.out.println("var " + variable + "=_theCurrentEvent." + eventGetterMethods[i].getName() + "()");
//                varInitializationScript = varInitializationScript + "\nvar " + variable + "=\"" +  + "\";";
//System.out.println("variable: " + variable + " = " + eventGetterMethods[i].invoke(event, new Object[0]) +", class: " + eventGetterMethods[i].invoke(event, new Object[0]).getClass());
            }
        }

//System.out.println("event: " + event.getClass());
        // Register the 'source' variable.
        ESlateContainer container = null;
        if (eSlateHandle != null) {
            container = (ESlateContainer) eSlateHandle.getRootHandle().getComponent();
            if (eSlateHandle != container.getESlateHandle()) {
                String compoName = eSlateHandle.getComponentPathName(); //.getComponentName();
//System.out.println("compoName: " + compoName);
                if (compoName != null) {
                    String jsCompoName = compoName.replace(' ', '_');
                    jsCompoName = jsCompoName.replace('.', '$');
//System.out.println("jsCompoName: " + jsCompoName);
                    headerScript = headerScript + "\nvar source=" + jsCompoName + ';';
                    // Register the 'sourceHandleName' variable
                    headerScript = headerScript + "\nvar sourceHandleName=\"" + compoName + "\";";
                }else{
                    headerScript = headerScript + "\nvar source=null;";
                    // Register the 'sourceHandleName' variable
                    headerScript = headerScript + "\nvar sourceHandleName=null;";
                }
            }else{
                headerScript = headerScript + "\nvar source=_theCurrentESlateContainer;";
                // Register the 'sourceHandleName' variable
                headerScript = headerScript + "\nvar sourceHandleName=\"" + eSlateHandle.getComponentName() + "\";";
            }
        }

        // Register the ESlateMicroworld variable under the name 'microworld'.
        if (eSlateHandle != null)
            headerScript = headerScript + "\nvar microworld=_theCurrentESlateMicroworld;";

        // Register the ESlateContainer variable under the name 'eslate'.
//System.out.println("JavascriptHandler lookup: " + gr.cti.eslate.base.ScriptManager.getScriptManager().lookup("_theCurrentESlateContainer"));
        if (eSlateHandle != null)
            headerScript = headerScript + "\nvar eslate=_theCurrentESlateContainer;";

        headerScript = headerScript + varInitializationScript + '\n';// + script;
//        System.out.println("TOTAL SCRIPT:  " + totalScript);

        if (container.currentlyOpenMwdFile != null)
            ScriptUtils.getInstance().loadMicroworldScripts(container, container.currentlyOpenMwdFile);
        String[] s = stripFunctions(script);
        String strippedScript = s[0];
        String functionScript = s[1];
//System.out.println("functionScript: " + functionScript);
//System.out.println("=================================================================");
//System.out.println("=================================================================");
//System.out.println("strippedScript: " + strippedScript);

        String totalScript = "function thisIsAFullFunction() {" + strippedScript + "\n}\n thisIsAFullFunction()";
        if (eSlateHandle != null) { // && eSlateHandle.getESlateMicroworld() != null) {
//            ESlateContainer container = (ESlateContainer) eSlateHandle.getESlateMicroworld().getMicroworldEnvironment();
            container.evaluateJSScript(headerScript);
            try{
                securityProxy.grantAccess();
                if (functionScript != null && functionScript.trim().length() != 0)
                    container.evaluateJSScript(functionScript);
                container.evaluateJSScript(totalScript);
            }finally{
                securityProxy.revokeAccess();
            }
        }else
            System.out.println("Unable to execute javascript script. Probably the microworld has been closed");

        for (int i=scriptMgrRegisteredNames.size()-1; i>=0; i--)
            scriptMgr.unregisterName(scriptMgrRegisteredNames.get(i));
        return null;
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION, 2);
        fieldMap.put("Method name", methodName);
        fieldMap.put("Listener class", listenerClass);
        out.writeObject(fieldMap);
//        System.out.println("JavaScriptHandler writeExternal() size: " + ESlateContainerUtils.getFieldMapContentLength(fieldMap));
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        methodName = (String) fieldMap.get("Method name");
        listenerClass = (Class) fieldMap.get("Listener class");
        initializeHandler();
    }

    public String[] stripFunctions(String script) {
        StringBuffer functionBuffer = new StringBuffer();
        StringBuffer scriptBuffer = new StringBuffer(script);
        int index = -1;
        int scriptLength = scriptBuffer.length();
        while (index < scriptLength && (index = script.indexOf("function ", index+1)) != -1) {
            int startIndex = index;
            int endIndex = startIndex;
            char ch = scriptBuffer.charAt(endIndex);
            while (ch != '{') {
                if (ch == '}')
                    break;
                endIndex++;
                ch = scriptBuffer.charAt(endIndex);
            }
            if (ch != '{') {
                index = endIndex++;
                continue;
            }else{
                if (script.substring(startIndex + "function ".length(), endIndex).indexOf("function ") != -1) {
                    index = startIndex++;
                    continue;
                }
            }

            int cbraceCount = 1;
            while (cbraceCount != 0) {
                endIndex++;
                ch = scriptBuffer.charAt(endIndex);
                while (ch != '{' && ch != '}') {
                    endIndex++;
                    ch = scriptBuffer.charAt(endIndex);
                }
                if (ch == '{')
                    cbraceCount++;
                if (ch == '}')
                    cbraceCount--;
            }
            String function = scriptBuffer.substring(startIndex, endIndex+1);
            scriptBuffer.replace(startIndex, endIndex+1, "");
            script = scriptBuffer.toString();
            functionBuffer.append(function);
            functionBuffer.append('\n');
            index = startIndex;
        }

        String[] result = new String[] {scriptBuffer.toString(), functionBuffer.toString()};
        return result;
    }
}
