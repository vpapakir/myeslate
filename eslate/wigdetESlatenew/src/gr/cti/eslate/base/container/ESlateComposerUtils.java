package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.eclipse.jdt.internal.compiler.batch.Main;

import com.objectspace.jgl.Array;

/**
 * Utilities used by ESlateComposer.
 * <p><code>
 * User: Yiorgos Tsironis
 * </code>
 */
public class ESlateComposerUtils {
    ESlateComposer composer = null;

    public ESlateComposerUtils(ESlateComposer composer) {
        this.composer = composer;
    }

    protected String createLogoScriptListener(Object object, EventSetDescriptor eventDescriptor, Method method, Array getterMethods, String script) {
        java.io.File tmpDir = composer.getESlateUtilities().getTmpDir();
        String className = eventDescriptor.getListenerType().getName();
        int periodIndex = className.lastIndexOf(".");
        if (periodIndex != -1)
            className = className.substring(periodIndex+1);

        className = className + "_" + ESlateContainerUtils.createUniqueId(0);
        String listenerFileName = className + ".java";
        java.io.File listenerFile = new java.io.File(tmpDir, listenerFileName);
        Method listenerMethods[] = eventDescriptor.getListenerType().getMethods();
//	      Method listenerMethods[] = eventDescriptor.getListenerMethods();

      	// Open the new java source file,
      	java.io.PrintWriter out = null;
      	try {
              OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(listenerFile));
//\	          java.io.FileWriter fout = new java.io.FileWriter(listenerFile);
      	    out = new java.io.PrintWriter(fw);
      	} catch (Exception ex) {
	          System.err.println("Couldn't open hookup file " + listenerFile);
      	    System.err.println("   " + ex);
	          return null;
      	}
	out.println("// Automatically generated event hookup file.");
      	out.println("");
      	out.println("package gr.cti.eslate.base;");

	      out.println("import java.lang.reflect.Method;");
	      out.println("import virtuoso.logo.Machine;");
	      out.println("import virtuoso.logo.InterpEnviron;");
	      out.println("import virtuoso.logo.Tokenizer;");
	      out.println("import virtuoso.logo.LogoList;");
	      out.println("import virtuoso.logo.InterpreterThread;");
	      out.println("import virtuoso.logo.LanguageException;");
	      out.println("import virtuoso.logo.ThrowException;");
	      out.println("import gr.cti.eslate.base.ESlateHandle;");
	      out.println("import " + eventDescriptor.getListenerType().getName() + ";");

      	// Now do any imports on argument types.
        Array importedClasses = new Array();
        importedClasses.add(eventDescriptor.getListenerType().getName());
	      for (int k = 0; k < listenerMethods.length; k++) {
	          Class argTypes[] = listenerMethods[k].getParameterTypes();
    	      for (int i = 0; i < argTypes.length; i++) {
		            Class type = argTypes[i];
          		  while (type.isArray()) {
		                type = type.getComponentType();
            		}
            		if (type.isPrimitive()) {
            		    continue;
            		}
      	  	    String typeName = type.getName();
            		if (importedClasses.indexOf(typeName) == -1) {
          	  	    out.println("import " + typeName + ";");
		                importedClasses.add(typeName);
            		}
	          }
	      }


	out.println("");
      	out.println("public class " + className + " implements " + eventDescriptor.getListenerType().getName() + " {"); //", java.io.Serializable {");
        out.println("    transient ESlateHandle eSlateHandle = null;");
        out.println("    transient Tokenizer tokenizer = null;");
        out.println("    transient Machine logoMachine = null;");
        out.println("    transient InterpEnviron logoEnvironment = null;");
        out.println("    transient InterpreterThread logoThread = null;");
        out.println("    String varInitializationScript;");
        out.println("    String script;");
      	out.println("");
        for (int k = 0; k < listenerMethods.length; k++) {
            out.println("");
            out.print("    public void " + listenerMethods[k].getName() + "(");
            Class argTypes[] = listenerMethods[k].getParameterTypes();
      	    for (int i = 0; i < argTypes.length; i++) {
	              if (i > 0) {
		                out.print(", ");
    	          }
		            // Figure out the string for the argument type.  We have
            		// have to treat array types specially.
          	  	Class type = argTypes[i];
            		String typeName = "";
            		while (type.isArray()) {
		                typeName = "[]" + typeName;
            		    type = type.getComponentType();
              	}
            		typeName = type.getName() + typeName;

	              out.print(typeName + " arg" + i);
            }
      	    out.print(")");
	          if ("vetoableChange".equals(listenerMethods[k].getName())) {
	              out.print("     throws java.beans.PropertyVetoException");
    	      }

  	        out.println(" {");
	          if (method.getName().equals(listenerMethods[k].getName())) {
                out.println("        if (tokenizer == null) return;");
                out.println("        varInitializationScript = \"\";");
                out.println("//        System.out.println(\"" + method + " called\");");
                for (int i=0; i<getterMethods.size(); i++) {
                    String methodName = ((Method) getterMethods.at(i)).getName();
                    out.println("//        System.out.println(\"" + methodName + ": \" + arg0." +  methodName +"());");
                    String variable;
                    if (methodName.startsWith("get"))
                        variable = methodName.substring(3);
                    else
                        variable = methodName;

                    Class returnType = ((Method) getterMethods.at(i)).getReturnType();
                    if (returnType.equals(int.class) || returnType.equals(short.class) || returnType.equals(long.class)
                        || returnType.equals(double.class))
                        out.println("        varInitializationScript = varInitializationScript + \" " + '\\' + 'n' + "make " + '\\' + '\"' + variable + "\" + " + "\" |\"" + " + arg0." +  methodName +"() + \"|\";");
                    else  if (returnType.isArray()) {
//                        out.println("        varInitializationScript = varInitializationScript + \" " + '\\' + 'n' + "make " + '\\' + '\"' + variable + "\" + " + "\" \" + \"" + '\\' + '\"' + "|\" + " + "arg0." +  methodName +"() + \"|\";");
                        out.println("        Object array = arg0." +  methodName +"();");
                        out.println("        int length = java.lang.reflect.Array.getLength(array);");
                        out.println("        String arrayElementList = new String(\"[\");");
                        out.println("        for (int i=0; i<length; i++)");
                        out.println("            arrayElementList = arrayElementList + " + "\"" + " " + "\"" + " + \"|\" + " + "java.lang.reflect.Array.get(array, i) + \"|\"; ");
//                        out.println("            arrayElementList = arrayElementList + " + "\"" + " \\" + '\"' + "\"" + " + \"|\" + " + "java.lang.reflect.Array.get(array, " + i + ") + \"|\"; ");
                        out.println("        arrayElementList = arrayElementList + \']\';");
                        out.println("//        System.out.println(\"arrayElementList: \" + arrayElementList);");
                        out.println("        varInitializationScript = varInitializationScript + \" " + '\\' + 'n' + "make " + '\\' + '\"' + variable + "\" + " + "\" \" + " + "arrayElementList" + ";");
                        out.println();
                    }else
                        out.println("        varInitializationScript = varInitializationScript + \" " + '\\' + 'n' + "make " + '\\' + '\"' + variable + "\" + " + "\" \" + \"" + '\\' + '\"' + "|\" + " + "arg0." +  methodName +"() + \"|\";");
                }
                out.println("        varInitializationScript = varInitializationScript + \" " + '\\' + 'n' + "make " + '\\' + '\"' + "source" + "\" + " + "\" \" + \"" + '\\' + '\"' + "|\" + " + "eSlateHandle.getComponentName() + \"|\";");
                out.println("        varInitializationScript = varInitializationScript + '" + '\\' + 'n' + "';");
                out.println("//        System.out.println(\"varInitializationScript: \" + varInitializationScript);");
                out.println("        script = \"" + script + "\";");
                out.println("//        System.out.println(\"script: \" + script);");
                out.println();
                out.println("        String totalScript = varInitializationScript + \"" + script + "\";");
                out.println("//        System.out.println(\"TOTAL SCRIPT:  \" + totalScript);");
                out.println("//        System.out.println(\"tokenizer: \" + tokenizer);");
                out.println("//        System.out.println(\"logoMachine: \" + logoMachine);");
                out.println("//        System.out.println(\"logoThread: \" + logoThread);");
                out.println("//        System.out.println(\"logoEnvironment: \" + logoEnvironment);");
                out.println();
                out.println("        try{");
                out.println("//        System.out.println(\"Scriptlistener 1 \");");
                out.println("            LogoList l = tokenizer.tokenize(totalScript);");
                out.println("//        System.out.println(\"Scriptlistener 2 \");");
                out.println("            l.getRunnable(logoMachine).execute(logoEnvironment);");
                out.println("//        System.out.println(\"Scriptlistener 3 \");");
                out.println("        }catch (LanguageException exc) {");
                out.println("             System.out.println(\"LanguageException: \" + exc.getMessage());");
                out.println("             try{");
                out.println("                 logoThread.outStream().putLine(exc.getMessage());");
                out.println("             }catch (LanguageException excpt) {");
                out.println("                 System.out.println(\"LanguageException: \" + excpt.getMessage());");
                out.println("             }");
                out.println("        }catch (ThrowException exc) {");
                out.println("             System.out.println(\"ThrowException: \" + exc.getMessage());");
                out.println("             try{");
                out.println("                 logoThread.outStream().putLine(exc.getMessage());");
                out.println("             }catch (LanguageException excpt) {");
                out.println("                 System.out.println(\"LanguageException: \" + excpt.getMessage());");
                out.println("             }");
                out.println("        }");
      	    }
	          out.println("    }");
      	}
        out.println("");
        out.println("    public void setLogoRuntime(ESlateHandle handle, Machine logoMachine, InterpEnviron logoEnvironment, InterpreterThread logoThread, Tokenizer tokenizer) {");
        out.println("        this.eSlateHandle = handle;");
        out.println("        this.logoMachine = logoMachine;");
        out.println("        this.logoEnvironment = logoEnvironment;");
        out.println("        this.logoThread = logoThread;");
        out.println("        this.tokenizer = tokenizer;");
        out.println("//        System.out.println(\"SETTING TOKENIZER TO: \" + tokenizer);");
        out.println("    }");
      	out.println("}");
      	out.close();
        return listenerFile.getPath();
    }

    public void attachLogoScript(Object object, String script, String methodName) {
//        ESlateHandle handle = container.componentScriptListeners.getContainingObjectHandle(object);
        ESlateHandle handle = composer.getMicroworld().getESlateMicroworld().getComponentHandle(object);
        ResourceBundle logoScriptDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.LogoScriptDialogBundle", Locale.getDefault());
        if (handle != null) {
            String componentName = handle.getComponentName();
            if (componentName == null) {
                /* A null componentName implies that the component no longer exists, i.e. it
                 * has been removed from the microworld.
                 */
                ESlateOptionPane.showMessageDialog(composer, logoScriptDialogBundle.getString("CompileDenialMsg1") + logoScriptDialogBundle.getString("CompileDenialMsg2"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Container topLevelContainer = composer.getTopLevelAncestor();
        topLevelContainer.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        EventSetDescriptor[] eventDescriptors = null;
        Class compoClass = object.getClass();
        BeanInfo compoInfo = BeanInfoFactory.getBeanInfo(compoClass); //compoClass.getSuperclass());
        if (compoInfo == null) return;
        eventDescriptors = compoInfo.getEventSetDescriptors();
        if (eventDescriptors == null) {
//            System.out.println("Failing 1");
            return;
        }

        /* Find the EventSetDescriptor that contains the method named 'methodName' */
        EventSetDescriptor eventDescriptor = null;
        Method listenerMethod = null;
        boolean found = false;
        for (int i=0; i<eventDescriptors.length; i++) {
            Method[] methods = eventDescriptors[i].getListenerMethods();
            for (int k=0; k<methods.length; k++) {
                if (methods[k].getName().equals(methodName)) {
                    eventDescriptor = eventDescriptors[i];
                    listenerMethod = methods[k];
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        if (eventDescriptor == null || listenerMethod == null) {
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
//            System.out.println("Failing 2");
            throw new IllegalArgumentException("Component \"" +  object + "\" does not support event \"" + methodName + '\"');
//            return;
        }


        /* Find the ScriptListener for the 'methodName' of the Java object 'object' */
        ScriptListener[] scriptListeners = composer.getScriptListenerMap().getScriptListeners(object);
        ScriptListener logoScriptListener = null;
        if (scriptListeners != null) {
            for (int i=0; i<scriptListeners.length; i++) {
                if (scriptListeners[i].getMethodName().equals(methodName)) {
//js                    if (scriptListeners[i].scriptInLogo.booleanValue())
                    if (scriptListeners[i].scriptLanguage == ScriptListener.LOGO)
                        logoScriptListener = scriptListeners[i];
                    if (logoScriptListener != null)
                        break;
                }
            }
        }
        /* Get the EventListener of the ScriptListener */
        java.util.EventListener currentListener = null;
        if (logoScriptListener != null)
            currentListener = (java.util.EventListener) logoScriptListener.listener;

        // if the new script is null or empty then remove the EventListener, if any exists.
        if (script == null || script.trim().length() == 0) {
            if (currentListener != null) {
                try {
                    Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
                    removeListenerMethod.invoke(object, new Object[] {currentListener});
                }catch (IllegalAccessException exc) {
                    System.out.println("IllegalAccessException while adding listener");
                }catch (IllegalArgumentException exc) {
                    System.out.println("IllegalArgumentException while adding listener");
                }catch (java.lang.reflect.InvocationTargetException exc) {
                    System.out.println("InvocationTargetException while adding listener");
                }
            }
            composer.getScriptListenerMap().removeScriptListener(logoScriptListener);
            composer.setMicroworldChanged(true);

            topLevelContainer.setCursor(Cursor.getDefaultCursor());
//            System.out.println("Failing 3");
            return;
        }

        // if the script hasn't been changed, return.
        if (script != null && logoScriptListener != null && logoScriptListener.getScript() != null && script.equals(logoScriptListener.getScript())) {
//            if (scriptName != getScriptName()) {
//                scriptName = getScriptName();
//            }
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
//            System.out.println("Failing 4");
            return;
        }

        String formulatedScript = formulateScript(script);

        /* Find the variables which will be made available to the Logo script.
         * These variables are the ones that are conveyed by the event object
         * received by the listener. This will be done only the first time the
         * listener is created.
         */
        Class[] paramClasses =listenerMethod.getParameterTypes();
        Class eventClass = paramClasses[0];
        Method[] eventMethods = eventClass.getMethods();
        Array getterMethods = new Array();
        for (int i=0; i<eventMethods.length; i++) {
            Class returnType = eventMethods[i].getReturnType();
            if (Modifier.isPublic(eventMethods[i].getModifiers()) &&
                returnType != null &&
                !returnType.getName().equals("void") &&
                eventMethods[i].getParameterTypes().length == 0) {
                if (returnType.equals(String.class))
                    getterMethods.add(eventMethods[i]);
                else if (returnType.isPrimitive() && !returnType.isArray())
                    getterMethods.add(eventMethods[i]);
            }
        }
        String[] eventVariables = new String[getterMethods.size()];
        for (int i=0; i<getterMethods.size(); i++) {
            if (((Method) getterMethods.at(i)).getName().startsWith("get"))
                eventVariables[i] = ((Method) getterMethods.at(i)).getName().substring(3);
            else
                eventVariables[i] = ((Method) getterMethods.at(i)).getName();
        }

        // Now create the listener's java file
        String listenerFileName = createLogoScriptListener(object, eventDescriptor, listenerMethod, getterMethods, formulatedScript);
        if (listenerFileName == null) {
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
//            System.out.println("Failing 5");
            return;
        }

        Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, composer);
        String listenerClassFileName = compileJavaFile(
                                              topLevelFrame,
                                              listenerFileName,
                                              listenerMethod.getName(),
                                              logoScriptDialogBundle);
        if (composer.getESlateUtilities().existsInTmpDir(listenerFileName))
            ESlateContainerUtils.deleteFile(listenerFileName);
        if (listenerClassFileName == null) {
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
//            System.out.println("Failing 6");
            return;
        }

        SimpleClassLoader loader = SimpleClassLoader.listenerLoader;

        // Load the produced listener class
        Class listenerClass = null;
        byte[] listenerClassBytes = null;
        try{
            listenerClassBytes = SimpleClassLoader.getByteArray(listenerClassFileName);
            ESlateContainerUtils.deleteFile(listenerClassFileName);
            listenerClass = loader.loadClassFromByteArray(listenerClassBytes);
        }catch (ClassNotFoundException exc) {
            System.out.println("ClassNotFoundException while loading listener class");
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//            System.out.println("Failing 7");
            return;
        }catch (java.io.IOException exc) {
            System.out.println("IOException while loading listener class from file " + listenerClassFileName);
            exc.printStackTrace();
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//            System.out.println("Failing 8");
            return;
        }
        //Make an instance of the listener class
        Object hookup = null;
        try{
            hookup = listenerClass.newInstance();
            /* Set the Logo runtime environment of the listener instance. We must call the
             * setLogoRuntime method. To do it we get the methods fo the listener's class,
             * secarch for the method with name "setLogoRuntime" and the invoke it"
             */
            Method[] listenerMethods = listenerClass.getMethods();
            Method logoRuntimeSetterMethod = null;
            for (int i=0; i<listenerMethods.length; i++) {
                if (listenerMethods[i].getName().equals("setLogoRuntime")) {
                    logoRuntimeSetterMethod = listenerMethods[i];
                    break;
                }
            }
            if (composer.getLogoMachine() == null) {
                composer.initLogoEnvironment();
                composer.startWatchingMicroworldForPrimitiveGroups();
            }

            logoRuntimeSetterMethod.invoke(hookup,
                      new Object[] {
                          handle,
                          composer.getLogoMachine(),
                          composer.getLogoEnvironment(),
                          composer.getLogoThread(),
                          composer.getTokenizer()}
            );
        }catch (InstantiationException exc) {
            System.out.println("InstantiationException while instantiating listener object");
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }catch (IllegalAccessException exc) {
            System.out.println("IllegalAccessException while instantiating listener object");
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }catch (java.lang.reflect.InvocationTargetException exc) {
            System.out.println("InvocationTargetException while invoking \"setLogoRuntime\" of listener object");
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add the instance of the listener class to the component.
        Method addListenerMethod = eventDescriptor.getAddListenerMethod();
        try{
            if (currentListener != null) {
                // Remove the installed listener
                Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
                removeListenerMethod.invoke(object, new Object[] {currentListener});
            }
            addListenerMethod.invoke(object, new Object[] {hookup});
//            updateComponentScriptListeners();
            String scriptName = listenerMethod.getName();

            String[] pathStr = new String[1];
            pathStr[0] = handle.getComponentName();
            HierarchicalComponentPath2 path = new HierarchicalComponentPath2(pathStr);
            ScriptListener newScriptListener = new ScriptListener("", //componentName,
                                                          listenerMethod.getName(),
                                                          scriptName,
                                                          script,
                                                          (java.util.EventListener) hookup,
                                                          listenerClass,
                                                          new byte[][] {listenerClassBytes},
                                                          ScriptListener.LOGO, //Logo script
                                                          path,
                                                          null);


            composer.getScriptListenerMap().putScriptListener(object, newScriptListener, handle);

        }catch (IllegalAccessException exc) {
            System.out.println("IllegalAccessException while adding listener");
            ESlateOptionPane.showMessageDialog(composer, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (IllegalArgumentException exc) {
            System.out.println("IllegalArgumentException while adding listener");
            ESlateOptionPane.showMessageDialog(composer, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (java.lang.reflect.InvocationTargetException exc) {
            System.out.println("InvocationTargetException while adding listener");
            ESlateOptionPane.showMessageDialog(composer, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        topLevelContainer.setCursor(Cursor.getDefaultCursor());
    }

//    public void attachJavaScript(String compoName, String script, String methodName) {
//    }

    public void attachJavaScript(Object object, String[] script, String methodName) {
        StringBuffer scriptBuff = new StringBuffer();
        for (int i=0; i<script.length; i++) {
            scriptBuff.append(script[i]);
            scriptBuff.append('\n');
        }
        attachJavaScript(object, scriptBuff.toString(), methodName);
    }

    public void attachJavaScript(Object object, String script, String methodName) {
        if (object == null)
            throw new NullPointerException("attachJavaScript(): Cannot attach a script to a null object");
        if (methodName == null)
            throw new NullPointerException("attachJavaScript(): Null methodName supplied");
        if (composer.getMicroworld() == null)
            throw new IllegalArgumentException("There is no open microworld");

        /* Check if the object name is provided instead of the object itself */
        if (object.getClass().equals(String.class)) {
            String compoName = (String) object;
            object = composer.getMicroworld().getESlateMicroworld().getComponent(compoName);
            if (object == null)
                throw new IllegalArgumentException("No component named \"" +  compoName + "\" exists in the microworld");
        }

//        ESlateHandle handle = container.componentScriptListeners.getContainingObjectHandle(object);
        ESlateHandle handle = composer.getMicroworld().getESlateMicroworld().getComponentHandle(object);
        ResourceBundle javaCodeDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.JavaCodeDialogBundle", Locale.getDefault());
        if (handle != null) {
            String componentName = handle.getComponentName();
            if (componentName == null) {
                /* A null componentName implies that the component no longer exists, i.e. it
                 * has been removed from the microworld.
                 */
                ESlateOptionPane.showMessageDialog(composer, javaCodeDialogBundle.getString("CompileDenialMsg1") + javaCodeDialogBundle.getString("CompileDenialMsg2"), javaCodeDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }


        Container topLevelContainer = composer.getTopLevelAncestor();
        topLevelContainer.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        EventSetDescriptor[] eventDescriptors = null;
        Class compoClass = object.getClass();
        BeanInfo compoInfo = BeanInfoFactory.getBeanInfo(compoClass); //compoClass.getSuperclass());
        if (compoInfo == null) return;
        eventDescriptors = compoInfo.getEventSetDescriptors();
        if (eventDescriptors == null) {
//            System.out.println("Failing 1");
            return;
        }

        /* Find the EventSetDescriptor that contains the method named 'methodName' */
        EventSetDescriptor eventDescriptor = null;
        Method listenerMethod = null;
        boolean found = false;
        for (int i=0; i<eventDescriptors.length; i++) {
            Method[] methods = eventDescriptors[i].getListenerMethods();
            for (int k=0; k<methods.length; k++) {
//                System.out.println("methods[k].getName(): " + methods[k].getName() + ", methodName: " + methodName);
                if (methods[k].getName().equals(methodName)) {
                    eventDescriptor = eventDescriptors[i];
                    listenerMethod = methods[k];
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        if (eventDescriptor == null || listenerMethod == null) {
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            throw new IllegalArgumentException("Component \"" +  object + "\" does not support event \"" + methodName + '\"');
//            return;
        }


        /* Find the ScriptListener for the 'methodName' of the Java object 'object' */
        ScriptListener[] scriptListeners = composer.getScriptListenerMap().getScriptListeners(object);
        ScriptListener javaScriptListener = null;
        if (scriptListeners != null) {
            for (int i=0; i<scriptListeners.length; i++) {
                if (scriptListeners[i].getMethodName().equals(methodName)) {
//js                    if (!scriptListeners[i].scriptInLogo.booleanValue())
                    if (scriptListeners[i].scriptLanguage == ScriptListener.JAVA)
                        javaScriptListener = scriptListeners[i];
                    if (javaScriptListener != null)
                        break;
                }
            }
        }
        /* Get the EventListener of the ScriptListener */
        java.util.EventListener currentListener = null;
        if (javaScriptListener != null)
            currentListener = (java.util.EventListener) javaScriptListener.listener;

        // if the new script is null or empty then remove the EventListener, if any exists.
        if (script == null || script.trim().length() == 0) {
            if (currentListener != null) {
                try {
                    Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
                    removeListenerMethod.invoke(object, new Object[] {currentListener});
                }catch (IllegalAccessException exc) {
                    System.out.println("IllegalAccessException while adding listener");
                }catch (IllegalArgumentException exc) {
                    System.out.println("IllegalArgumentException while adding listener");
                }catch (java.lang.reflect.InvocationTargetException exc) {
                    System.out.println("InvocationTargetException while adding listener");
                }
            }
            composer.getScriptListenerMap().removeScriptListener(javaScriptListener);
            composer.setMicroworldChanged(true);

            topLevelContainer.setCursor(Cursor.getDefaultCursor());
//            System.out.println("Failing 3");
            return;
        }

        // if the script hasn't been changed, return.
        if (script != null && javaScriptListener != null && javaScriptListener.getScript() != null && script.equals(javaScriptListener.getScript())) {
//            if (scriptName != getScriptName()) {
//                scriptName = getScriptName();
//            }
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
//            System.out.println("Failing 4");
            return;
        }

        formulateScript(script);

        // Now create the listener's java file
        String listenerFileName = createNewJavaScriptListener(object, eventDescriptor, listenerMethod, script);
//        String listenerFileName = container.containerUtils.createLogoScriptListener(object, eventDescriptor, listenerMethod, getterMethods, formulatedScript);
        if (listenerFileName == null) {
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
//            System.out.println("Failing 5");
            return;
        }

        Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, composer);
        String listenerClassFileName = compileJavaFile(
                                              topLevelFrame,
                                              listenerFileName,
                                              listenerMethod.getName(),
                                              javaCodeDialogBundle);
        if (composer.getESlateUtilities().existsInTmpDir(listenerFileName))
            ESlateContainerUtils.deleteFile(listenerFileName);
        // if the compilation fails, then remove the current listener, if it exists
        if (listenerClassFileName == null) {
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            if (currentListener != null) {
                try {
                    Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
                    removeListenerMethod.invoke(object, new Object[] {currentListener});
                }catch (IllegalAccessException exc) {
                    System.out.println("IllegalAccessException while adding listener");
                }catch (IllegalArgumentException exc) {
                    System.out.println("IllegalArgumentException while adding listener");
                }catch (java.lang.reflect.InvocationTargetException exc) {
                    System.out.println("InvocationTargetException while adding listener");
                }
            }
            composer.getScriptListenerMap().removeScriptListener(javaScriptListener);
            composer.setMicroworldChanged(true);

//            System.out.println("Failing 6");
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            return;
        }

        SimpleClassLoader loader = SimpleClassLoader.listenerLoader;

        // Load the produced listener class
        Class listenerClass = null;
        byte[] listenerClassBytes = null;
        try{
            listenerClassBytes = SimpleClassLoader.getByteArray(listenerClassFileName);
            ESlateContainerUtils.deleteFile(listenerClassFileName);
            listenerClass = loader.loadClassFromByteArray(listenerClassBytes);
        }catch (ClassNotFoundException exc) {
            System.out.println("ClassNotFoundException while loading listener class");
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, javaCodeDialogBundle.getString("CompileFailureMessage3"), javaCodeDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//            System.out.println("Failing 7");
            return;
        }catch (java.io.IOException exc) {
            System.out.println("IOException while loading listener class from file " + listenerClassFileName);
            exc.printStackTrace();
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, javaCodeDialogBundle.getString("CompileFailureMessage3"), javaCodeDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//            System.out.println("Failing 8");
            return;
        }
        //Make an instance of the listener class
        Object hookup = null;
        try{
            hookup = listenerClass.newInstance();
//            System.out.println("Created new instance of listener class: " + hookup);
            /* Set the eSlateHandle variable of the listener instance. We must call the
             * setESlateHandle method. To do it we get the methods fo the listener's class,
             * secarch for the method with name "setESlateHandle" and the invoke it"
             */
            Method[] listenerMethods = listenerClass.getMethods();
            Method handleSetterMethod = null;
            for (int i=0; i<listenerMethods.length; i++) {
                if (listenerMethods[i].getName().equals("setESlateHandle")) {
                    handleSetterMethod = listenerMethods[i];
                    break;
                }
            }
            handleSetterMethod.invoke(hookup, new Object[] {handle});
        }catch (InstantiationException exc) {
            System.out.println("InstantiationException while instantiating listener object");
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, javaCodeDialogBundle.getString("CompileFailureMessage3"), javaCodeDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }catch (IllegalAccessException exc) {
            System.out.println("IllegalAccessException while instantiating listener object or while setting listener's eSlateHandle");
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, javaCodeDialogBundle.getString("CompileFailureMessage3"), javaCodeDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }catch (IllegalArgumentException exc) {
            System.out.println("IllegalArgumentException while setting listener's eSlateHandle");
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, javaCodeDialogBundle.getString("CompileFailureMessage3"), javaCodeDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }catch (java.lang.reflect.InvocationTargetException exc) {
            System.out.println("InvocationTargetException while setting listener's eSlateHandle");
            topLevelContainer.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(composer, javaCodeDialogBundle.getString("CompileFailureMessage3"), javaCodeDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add the instance of the listener class to the component.
        Method addListenerMethod = eventDescriptor.getAddListenerMethod();
        try{
            if (currentListener != null) {
                // Remove the installed listener
                Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
                removeListenerMethod.invoke(object, new Object[] {currentListener});
            }
            addListenerMethod.invoke(object, new Object[] {hookup});
//            System.out.println("Adding new listener");
//            updateComponentScriptListeners();
            String[] pathStr = new String[1];
            pathStr[0] = handle.getComponentName();
            HierarchicalComponentPath2 path = new HierarchicalComponentPath2(pathStr);
            ScriptListener newScriptListener = new ScriptListener("", //componentName,
                                                          listenerMethod.getName(),
                                                          listenerMethod.getName(),
                                                          script,
                                                          (java.util.EventListener) hookup,
                                                          hookup.getClass(),
                                                          new byte[][] {listenerClassBytes},
                                                          ScriptListener.JAVA, //Java script
                                                          path,
                                                          null);

            composer.getScriptListenerMap().putScriptListener(object, newScriptListener, handle);
        }catch (IllegalAccessException exc) {
            System.out.println("IllegalAccessException while adding listener");
            ESlateOptionPane.showMessageDialog(composer, javaCodeDialogBundle.getString("CompileFailureMessage3"), javaCodeDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (IllegalArgumentException exc) {
            System.out.println("IllegalArgumentException while adding listener");
            ESlateOptionPane.showMessageDialog(composer, javaCodeDialogBundle.getString("CompileFailureMessage3"), javaCodeDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (java.lang.reflect.InvocationTargetException exc) {
            System.out.println("InvocationTargetException while adding listener");
            ESlateOptionPane.showMessageDialog(composer, javaCodeDialogBundle.getString("CompileFailureMessage3"), javaCodeDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        topLevelContainer.setCursor(Cursor.getDefaultCursor());
    }

    /* Creates the file of a new Java script listener and returns its full path.
     */
    public String createNewJavaScriptListener(Object component, EventSetDescriptor eventDescriptor, Method method, String script) {
        java.io.File tmpDir = composer.getESlateUtilities().getTmpDir(); //new java.io.File(System.getProperty("user.home"));
        String className = eventDescriptor.getListenerType().getName();
        int periodIndex = className.lastIndexOf(".");
        if (periodIndex != -1)
            className = className.substring(periodIndex+1);

        className = className + "_" + ESlateContainerUtils.createUniqueId(0);
        String lisFileName = className + ".java";
        java.io.File listenerFile = new java.io.File(tmpDir, lisFileName);
        Method listenerMethods[] = eventDescriptor.getListenerType().getMethods();
//	      Method listenerMethods[] = eventDescriptor.getListenerMethods();

      	// Open the new java source file,
      	java.io.PrintWriter out = null;
      	try {
              OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(listenerFile));
//\            java.io.FileWriter fout = new java.io.FileWriter(listenerFile);
      	    out = new java.io.PrintWriter(fout);
      	} catch (Exception ex) {
	          System.err.println("Couldn't open hookup file " + listenerFile);
      	    System.err.println("   " + ex);
	          return null;
      	}
        out.println("/* Automatically generated event listener file. You should edit with great care");
        out.println(" * any code contained within blocks starting with '[*' and ending with '*]',");
        out.println(" * in order for the listener to compile and work properly. You should specify");
        out.println(" * the action of the listener in the listener's basic method, e.g. ");
        out.println(" * 'actionperformed()' for ActionListeners. You may supplement the listener");
        out.println(" * with additional methods and/or classes.");
        out.println(" */");
      	out.println("");
      	out.println("// [*");

      	out.println("package gr.cti.eslate.base;");

      	out.println("");
        out.println("import gr.cti.eslate.base.ESlateMicroworld;");
        out.println("import gr.cti.eslate.base.ESlateHandle;");
        out.println("import gr.cti.eslate.base.container.ListenerSecurityProxy;");
        out.println("import " + eventDescriptor.getListenerType().getName() + ";");

      	// Now do any imports on argument types.
        Array importedClasses = new Array();
        importedClasses.add(eventDescriptor.getListenerType().getName());
        for (int k = 0; k < listenerMethods.length; k++) {
            Class argTypes[] = listenerMethods[k].getParameterTypes();
        for (int i = 0; i < argTypes.length; i++) {
                      Class type = argTypes[i];
                    while (type.isArray()) {
                          type = type.getComponentType();
                  }
                  if (type.isPrimitive()) {
                      continue;
                  }
              String typeName = type.getName();
                  if (importedClasses.indexOf(typeName) == -1) {
                      out.println("import " + typeName + ";");
                          importedClasses.add(typeName);
                  }
            }
        }

        out.println("");
      	out.println("public class " + className + " implements " + eventDescriptor.getListenerType().getName() + " {"); //", java.io.Serializable {");
        out.println("    /* The current E-Slate microworld which contains the component");
        out.println("     */");
        out.println("    transient ESlateMicroworld microworld = null;");
        out.println("    /* The ESlateHandle of the component");
        out.println("     */");
        out.println("    transient ESlateHandle handle = null;");
        out.println("    /* The source (component) of the event");
        out.println("     */");
        out.println("    transient Object source = null;");
        out.println("    /* The security proxy of the listener. Use its methods to get access to the");
        out.println("     * priviledged API of E-Slate, when the microworld settings forbid access");
        out.println("     * to parts of this API.");
        out.println("     */");
        out.println("    private transient ListenerSecurityProxy proxy = null;");
      	out.println("");

//        out.println("    String script;");

        for (int k = 0; k < listenerMethods.length; k++) {
            out.println("");
            out.print("    public "  + listenerMethods[k].getReturnType() + ' ' + listenerMethods[k].getName() + "(");
            Class argTypes[] = listenerMethods[k].getParameterTypes();
      	    for (int i = 0; i < argTypes.length; i++) {
                if (i > 0) {
                    out.print(", ");
                }
		// Figure out the string for the argument type.  We have
            	// have to treat array types specially.
                Class type = argTypes[i];
                String typeName = "";
                while (type.isArray()) {
                    typeName = "[]" + typeName;
                    type = type.getComponentType();
              	}
            	typeName = type.getName() + typeName;
                out.print(typeName + " arg" + i);
            }
      	    out.print(")");

            if ("vetoableChange".equals(listenerMethods[k].getName())) {
                out.print("     throws java.beans.PropertyVetoException");
            }

  	    out.println(" {");
	    if (method.getName().equals(listenerMethods[k].getName())) {
//                out.println("        // Three variables are preset for use in the listener. These are:");
//                out.println("        // * eSlateHandle: the ESlateHandle of the component, which fires the event");
//                out.println("        // * microworld: the ESlateMicroworld in which the component participates");
//                out.println("        // * component: an Object reference to the component, which fires the event. To");
//                out.println("        //              be able to call methods on the \"component\", you must cast it to");
//                out.println("        //              object type, which is \"" + component.getClass() + "\".");
//                out.println("//        System.out.println(\"" + method + " called\");");
//                out.println("//        System.out.println(\"microworld: \" + microworld);");
                out.println("// *]");
                out.println(script);
//                out.println("        script = \"" + script + "\";");
//                out.println("        System.out.println(\"script: \" + script);");
              	out.println("// [*");
      	    }
            out.println("    }");
      	}
        out.println("");
        out.println("    public void setESlateHandle(ESlateHandle handle, ListenerSecurityProxy proxy) {");
        out.println("        this.handle = handle;");
        out.println("        this.microworld = handle.getESlateMicroworld();");
        out.println("        this.source = handle.getComponent();");
        out.println("        this.securityProxy = proxy;");
        out.println("    }");
      	out.println("// *]");
      	out.println("}");
      	out.close();
        return listenerFile.getPath();
    }

    /* Creates a new Java script listener for the specified component's event and
     * returns the listener's source and the listener's class name.
     */
    public static String[] initializeJavaScriptListener(Object component, EventSetDescriptor eventDescriptor, Method method) { //, String script) {
//i        java.io.File tmpDir = getTmpDir(); //new java.io.File(System.getProperty("user.home"));
        String className = eventDescriptor.getListenerType().getName();
        int periodIndex = className.lastIndexOf(".");
        if (periodIndex != -1)
            className = className.substring(periodIndex+1);

        className = className + "_" + ESlateContainerUtils.createUniqueId(0);
//i        java.io.File listenerFile = new java.io.File(tmpDir, lisFileName);
        Method listenerMethods[] = eventDescriptor.getListenerType().getMethods();
//	      Method listenerMethods[] = eventDescriptor.getListenerMethods();

      	// Open the new java source file,
      	StringBuffer listenerBuffer = new StringBuffer();
        listenerBuffer.append("/* Automatically generated event listener file. You should edit with great care\n");
        listenerBuffer.append(" * any code contained within blocks starting with '[*' and ending with '*]',\n");
        listenerBuffer.append(" * in order for the listener to compile and work properly. You should specify\n");
        listenerBuffer.append(" * the action of the listener in the listener's basic method, e.g. \n");
        listenerBuffer.append(" * 'actionperformed()' for ActionListeners. You may supplement the listener\n");
        listenerBuffer.append(" * with additional methods and/or classes.\n");
        listenerBuffer.append(" */\n");
      	listenerBuffer.append("\n");
      	listenerBuffer.append("// [*\n");

      	listenerBuffer.append("package gr.cti.eslate.base;\n");

      	listenerBuffer.append("\n");
        listenerBuffer.append("import gr.cti.eslate.base.ESlateMicroworld;\n");
        listenerBuffer.append("import gr.cti.eslate.base.ESlateHandle;\n");
        listenerBuffer.append("import gr.cti.eslate.base.container.ListenerSecurityProxy;\n");
        listenerBuffer.append("import " + eventDescriptor.getListenerType().getName() + ";\n");

      	// Now do any imports on argument types.
        Array importedClasses = new Array();
        importedClasses.add(eventDescriptor.getListenerType().getName());
        for (int k = 0; k < listenerMethods.length; k++) {
            Class argTypes[] = listenerMethods[k].getParameterTypes();
            for (int i = 0; i < argTypes.length; i++) {
                Class type = argTypes[i];
                while (type.isArray()) {
                    type = type.getComponentType();
                }
                if (type.isPrimitive()) {
                    continue;
                }
                 String typeName = type.getName();
                 if (importedClasses.indexOf(typeName) == -1) {
                    listenerBuffer.append("import " + typeName + ";\n");
                    importedClasses.add(typeName);
                }
            }
        }

        listenerBuffer.append("\n");
      	listenerBuffer.append("public class " + className + " implements " + eventDescriptor.getListenerType().getName() + " {\n"); //", java.io.Serializable {");
        listenerBuffer.append("    /* The current E-Slate microworld which contains the component\n");
        listenerBuffer.append("     */\n");
        listenerBuffer.append("    transient ESlateMicroworld microworld = null;\n");
        listenerBuffer.append("    /* The ESlateHandle of the component\n");
        listenerBuffer.append("     */\n");
        listenerBuffer.append("    transient ESlateHandle handle = null;\n");
        listenerBuffer.append("    /* The source (component) of the event\n");
        listenerBuffer.append("     */\n");
        listenerBuffer.append("    transient Object source = null;\n");
        listenerBuffer.append("    /* The security proxy of the listener. Use its methods to get access to the\n");
        listenerBuffer.append("     * priviledged API of E-Slate, when the microworld settings forbid access\n");
        listenerBuffer.append("     * to parts of this API.\n");
        listenerBuffer.append("     */\n");
        listenerBuffer.append("    private transient ListenerSecurityProxy securityProxy = null;\n");
      	listenerBuffer.append("\n");

//        listenerBuffer.append("    String script;");

        for (int k = 0; k < listenerMethods.length; k++) {
            listenerBuffer.append("\n");
            listenerBuffer.append("    public "  + listenerMethods[k].getReturnType() + ' ' + listenerMethods[k].getName() + "(");
            Class argTypes[] = listenerMethods[k].getParameterTypes();
      	    for (int i = 0; i < argTypes.length; i++) {
                if (i > 0) {
                    listenerBuffer.append(", ");
                }
		// Figure out the string for the argument type.  We have
            	// have to treat array types specially.
                Class type = argTypes[i];
                String typeName = "";
                while (type.isArray()) {
                    typeName = "[]" + typeName;
                    type = type.getComponentType();
              	}
            	typeName = type.getName() + typeName;
                listenerBuffer.append(typeName + " arg" + i);
            }
      	    listenerBuffer.append(")");

            if ("vetoableChange".equals(listenerMethods[k].getName())) {
                listenerBuffer.append("     throws java.beans.PropertyVetoException");
            }

  	    listenerBuffer.append(" {\n");
	    if (method.getName().equals(listenerMethods[k].getName())) {
//                listenerBuffer.append("        // Three variables are preset for use in the listener. These are:");
//                listenerBuffer.append("        // * eSlateHandle: the ESlateHandle of the component, which fires the event");
//                listenerBuffer.append("        // * microworld: the ESlateMicroworld in which the component participates");
//                listenerBuffer.append("        // * component: an Object reference to the component, which fires the event. To");
//                listenerBuffer.append("        //              be able to call methods on the \"component\", you must cast it to");
//                listenerBuffer.append("        //              object type, which is \"" + component.getClass() + "\".");
//                listenerBuffer.append("//        System.out.println(\"" + method + " called\");");
//                listenerBuffer.append("//        System.out.println(\"microworld: \" + microworld);");
                listenerBuffer.append("// *]\n");
//                listenerBuffer.append(script);
                listenerBuffer.append('\n');
//                listenerBuffer.append("        script = \"" + script + "\";");
//                listenerBuffer.append("        System.out.println(\"script: \" + script);");
              	listenerBuffer.append("// [*\n");
      	    }
            listenerBuffer.append("    }\n");
      	}
        listenerBuffer.append("\n");
        listenerBuffer.append("    public void setESlateHandle(ESlateHandle handle, ListenerSecurityProxy proxy) {\n");
        listenerBuffer.append("        this.handle = handle;\n");
        listenerBuffer.append("        this.microworld = handle.getESlateMicroworld();\n");
        listenerBuffer.append("        this.source = handle.getComponent();\n");
        listenerBuffer.append("        this.securityProxy = proxy;\n");
        listenerBuffer.append("    }\n");
      	listenerBuffer.append("// *]\n");
      	listenerBuffer.append("}\n");
        return new String[] {listenerBuffer.toString(), className};
    }

    /* Compiles a .java file to a .class file using the jikes compiler. The path to the
     * .class file is returned.
     */
    public static String compileJavaFile(Frame owner, String sourceFileName, String eventOrScriptName, ResourceBundle bundle) {
        if (sourceFileName == null)
            return null;
        try{
            String classpath = System.getProperty("java.class.path");
            File javaLibFolder = new File(System.getProperty("java.home"), "lib");
            FileFilter jarFileFilter = new FileFilter() {
                public boolean accept(File pathname) {
                    if (pathname.getPath().endsWith(".jar"))
                        return true;
                    return false;
                }
            };
            File[] jarFiles = javaLibFolder.listFiles(jarFileFilter);
            for (int i = 0; i < jarFiles.length; i++) {
                File jarFile = jarFiles[i];
                classpath = jarFile.getAbsolutePath() + ';' + classpath;
            }
            char pathSeparator = System.getProperty("path.separator").charAt(0);
            int pathSeparatorIndex = 0, lastIndex = 0;
            String jarFileName;
            File jarFile;
            StringBuffer rightClasspath = new StringBuffer();
			rightClasspath.append(ESlateContainerUtils.getScriptDir().getPath());
			rightClasspath.append(System.getProperty("file.separator"));
			rightClasspath.append(pathSeparator);
            while ((pathSeparatorIndex = classpath.indexOf(pathSeparator, pathSeparatorIndex)) != -1) {
                jarFileName = classpath.substring(lastIndex, pathSeparatorIndex);
                jarFile = new File(jarFileName);
                if (jarFile.exists()) {
                    rightClasspath.append(jarFileName);
                    rightClasspath.append(pathSeparator);
                }
                pathSeparatorIndex++;
                lastIndex = pathSeparatorIndex;
            }
            // Handle the last classpath entry, if exists.
            jarFileName = classpath.substring(lastIndex);
            if (jarFileName != null && jarFileName.trim().length() != 0) {
                jarFile = new File(jarFileName);
                if (jarFile.exists())
                    rightClasspath.append(jarFileName);
            }
            classpath = rightClasspath.toString();

            //String encoding = sun.io.CharToByteConverter.getDefault().getCharacterEncoding();
            String encoding = java.nio.charset.Charset.defaultCharset().toString();
            if (encoding.startsWith("ISO"))
                encoding = "ISO-" + encoding.substring(4);
            encoding = encoding.replace('_', '-');

            /* Through the Java code editor the user may save the source .java file anywhere on
             * the user's machine. If the source file is saved at the root of the drive of the
             * E-Slate installation, jikes cannot find the file in order to compile it. The
             * reason for this is unknown. So in order to support compilation of files that exist at this
             * specific place, we turn the absolute file location (e.g. "c:\listener.java") into a
             * relative to the directory of the E-Slate installation location (e.g. "..\..\listener.java".
             * This seems to work. All these have been tested only on the Windows OS.
             */
            if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1 && ESlateContainerUtils.getDepth(sourceFileName) == 0 && ESlateContainerUtils.existAtSameDrive(sourceFileName, ESlateContainer.getPathToContainerJar())) {
                String sourceFileNameWithoutPath = ESlateContainerUtils.getFileNameFromPath(sourceFileName, false);
                int depthToESlateInstDir = ESlateContainerUtils.getDepth(ESlateContainer.getPathToContainerJar())-1;
                StringBuffer sb = new StringBuffer();
                String fileSeparator = System.getProperty("file.separator");
                for (int i=0; i<depthToESlateInstDir; i++) {
                    sb.append("..");
                    sb.append(fileSeparator);
                }
                sb.append(sourceFileNameWithoutPath);
                sourceFileName = sb.toString();
            }
            
            /* This replaces unicode characters in the source file by unicode escapes, e.g. \u0343
            Reader reader=new BufferedReader(new InputStreamReader(new FileInputStream(sourceFileName),encoding));
            StringBuilder sb=new StringBuilder();
            StringBuilder numb = new StringBuilder();
            int c;
            while ((c=reader.read())>-1) {
            	if(c > '\177') {
            		sb.append("\\u");
            		String s1 = Integer.toHexString(c);
            		numb.delete(0,numb.length());
            		numb.append(s1);
            		numb.reverse();
            		int l = 4 - numb.length();
            		for(int i1 = 0; i1 < l; i1++)
            			numb.append('0');
            		
            		for(int j1 = 0; j1 < 4; j1++)
            			sb.append(numb.charAt(3 - j1));
            	} else {
            		sb.append((char) c);
            	}
            }
            reader.close();
            PrintWriter writer=new PrintWriter(new FileOutputStream(sourceFileName));
            writer.write(sb.toString());
            writer.close(); 
            */          
            ByteArrayOutputStream outStream=new ByteArrayOutputStream(); 
            PrintWriter pw = new PrintWriter(outStream);
            String[] args=new String[] {"-nowarn","-encoding",encoding,"-1.5","-classpath",'\"'+classpath+'\"',sourceFileName};
            boolean success=(new Main(pw,pw,false)).compile(args);
            if (!success) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outStream.toByteArray())));
                String s = "";
                String line;
                while ((line = errorReader.readLine()) != null) {
                    s = s + line + '\n';
                }
                ErrorDialog ed = new ErrorDialog(owner, bundle.getString("CompileFailureMessage"), eventOrScriptName, s);
                ed.showDialog(owner);
                return null;
            }
        }catch (java.io.IOException exc) {
            exc.printStackTrace();
            ESlateOptionPane.showMessageDialog(owner, bundle.getString("CompileFailureMessage3"), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String classFileName = sourceFileName.substring(0, sourceFileName.lastIndexOf(".")) + ".class";
        return classFileName;
    }

    /* This method parses the given script and locates escape characters. It then
     * alters the escape characters is such a waythat when the script is printed to
     * the java file, the escape characters are printed correctly.
     */
    public static String formulateScript(String script) {
        StringBuffer scriptBuffer = new StringBuffer(script);
        for (int i=0; i<scriptBuffer.length(); i++) {
            switch (scriptBuffer.charAt(i)) {
                case '\n':
                    scriptBuffer.setCharAt(i, '\\');
                    scriptBuffer.insert(++i, 'n');
                    break;
                case '\"':
                    scriptBuffer.setCharAt(i, '\\');
                    scriptBuffer.insert(++i, '\"');
                    break;
                case '\\':
                    scriptBuffer.setCharAt(i, '\\');
                    scriptBuffer.insert(++i, '\\');
                    break;
                case '\t':
                    scriptBuffer.setCharAt(i, '\\');
                    scriptBuffer.insert(++i, 't');
                    break;
                case '\r':
                    scriptBuffer.setCharAt(i, '\\');
                    scriptBuffer.insert(++i, 'r');
                    break;
                case '\'':
                    scriptBuffer.setCharAt(i, '\\');
                    scriptBuffer.insert(++i, '\'');
                    break;
            }
        }
        return new String(scriptBuffer);
    }
    protected static String findPathToJikes() {
        if (ESlateComposer.pathToJikes == null) {

            String containerPath = ESlateComposer.getPathToContainerJar().trim();
            String fileSeparator = System.getProperty("file.separator");
//            System.out.println("1. containerPath: " + containerPath + ", fileSeparator: " + fileSeparator + ", containerPath.endsWith(pathSeparator): " + containerPath.endsWith(fileSeparator));
            if (containerPath.endsWith(fileSeparator)) {
//            	System.out.println("Ends with separator: " + containerPath);
                containerPath = containerPath.substring(0, containerPath.length() - fileSeparator.length());
//                System.out.println("2. containerPath: " + containerPath);
            }
            File containerDir = new File(containerPath);
            if (!containerDir.exists() || ! containerDir.isDirectory()) {
                return null;
            }
//            System.out.println("containerDir: " + containerDir);
            File jikesDir = null;
            try{
                jikesDir = new File(containerDir.getParent());
                if (jikesDir == null)
                    return null;
//                  System.out.println("1. jikesDir: " + jikesDir);
//                jikesDir = new File(jikesDir.getParent());
                if (jikesDir == null)
                    return null;
//                  System.out.println("2. jikesDir: " + jikesDir);
            }catch (Exception exc) {
                return null;
            }
            jikesDir = new File(jikesDir, "bin");
//              System.out.println("3. jikesDir: " + jikesDir);
            if (!jikesDir.exists() || ! jikesDir.isDirectory()) {
                  System.out.println("Cannot find the jikes compiler");
                return null;
            }
            try{
//                  System.out.println("4. jikesDir: " + jikesDir);
                return jikesDir.getCanonicalPath();
            }catch (java.io.IOException exc) {
                return null;
            }
        }else
            return ESlateComposer.pathToJikes;
    }
}

/* Displays in a text area the output of the compilation process of a Java source file.
 */
class ErrorDialog extends JDialog {
    Locale locale;
    ResourceBundle errorDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    protected Font errorAreaUIFont = new Font("Courier", Font.PLAIN, 12);
    private boolean localeIsGreek = false;
    JScrollPane scrollPane;
    JTextArea errorArea;


    public ErrorDialog(Frame parentFrame, String message, String scriptName, String compilerOutput) {
        super(parentFrame, true);

        locale = Locale.getDefault();
        errorDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ErrorDialogBundle", locale);
        if (errorDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.ErrorDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(errorDialogBundle.getString("DialogTitle") + scriptName + "\"");
        errorArea = new JTextArea();
        if (localeIsGreek)
            errorArea.setFont(errorAreaUIFont);
        scrollPane = new JScrollPane(errorArea);

        // The button panel (OK)
        JButton OKButton = new JButton(errorDialogBundle.getString("OK"));
//        if (localeIsGreek)
//            OKButton.setFont(greekUIFont);
        Color color128 = new Color(0, 0, 128);
        OKButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        OKButton.setMaximumSize(buttonSize);
        OKButton.setPreferredSize(buttonSize);
        OKButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        OKButton.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(OKButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(10,5,5,5));

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        //Initialize the dialog
        errorArea.setText(message + '\n' + compilerOutput);

        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    // Displays the dialog
    public void showDialog(Component centerAroundComp) {
        pack();
        setSize(600, 500);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        int x, y;
        if (centerAroundComp == null || !centerAroundComp.isVisible()) {
            x = (screenSize.width/2) - (getSize().width/2);
            y = (screenSize.height/2) - (getSize().height/2);
        }else{
            Rectangle compBounds = centerAroundComp.getBounds();
            java.awt.Point compLocation = centerAroundComp.getLocationOnScreen();
    //        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
            x = compLocation.x + compBounds.width/2 - getSize().width/2;
            y = compLocation.y + compBounds.height/2-getSize().height/2;
            if (x+getSize().width > screenSize.width)
                x = screenSize.width - getSize().width;
            if (y+getSize().height > screenSize.height)
                y = screenSize.height - getSize().height;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
        setLocation(x, y);
        setVisible(true);
    }
}



/* Used by compileJavaFile() method of ESlateContainerUtils */
class WaitForCompiler implements Runnable {
    Process process;

    public WaitForCompiler(Process p) {
        this.process = p;
    }

    public void run() {
        try{
            process.waitFor();
        }catch (java.lang.InterruptedException exc) {
            System.out.println("InterruptedException while compiling");
        }
    }
}



