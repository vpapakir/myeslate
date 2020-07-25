package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * <p>Title: Script</p>
 * <p>Description: A Script is the container for a Java Class. It takes care
 * of storing and loading (also class-loading) and editing of the Class. The
 * microworld author can use this classes as in any IDE (instantiate objects...).
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

public class Script implements Externalizable {
	/*
	 * From 1 to 3: (2 was skipped cause of a mistake)
	 *    The source of the Script is not part of the stored state of the script.
	 *    It is stored in the directory ESlateContainer.SCRIPT_DIR_NAME of the
     *    structfile of the microworld.
     */
    public static final int FORMAT_VERSION = 3;
    static final long serialVersionUID = 12;
	public static final int PLAIN_CLASS = 1;
	public static final int SINGLETON = 2;
    transient String script;
    transient byte[][] listenerClassBytes;
	/** Stores the class names of all the inner classes
	 *  of the Script.
	 */
	transient String[] innerClassNames = new String[0];
	/**
	 * The name of the Script's class.
	 */
	private String className = null;
	/**
	 * The package of the Script.
	 */
	private String[] scriptPackage = null;
	/** The full class name (package+className) of the Script.
     */
	String fullClassName = null;
	/**
	 * The current line of the script.
	 */
	int currentLine = 0;

    public Script() {
    }

    /**
     * The textual representation of the script.
     */
    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public byte[][] getListenerClassBytes() {
        return listenerClassBytes;
    }

	/**
	 * Constructs a new script with the specified class name (scripts are
	 * actually classes.
	 * @param className The class(script) name
	 * @param classType One of Script.PLAIN_CLASS, Script.SINGLETON
	 */
	public Script(String[] packageName, String className, int classType) {
		if (className == null || className.trim().length() == 0) {
			throw new IllegalArgumentException("Invalid script class name");
		}
		if (classType != PLAIN_CLASS && classType != SINGLETON) {
			throw new IllegalArgumentException("Invalid script class type");
		}
		this.className = className;
		if (packageName == null) packageName = new String[0];
		this.scriptPackage = packageName;
		calculateFullClassName();

		StringBuffer buff = new StringBuffer();
		if (scriptPackage.length != 0) {
			buff.append("package ");
			for (int i=0; i<scriptPackage.length-1; i++) {
				buff.append(scriptPackage[i]);
				buff.append('.');
			}
			buff.append(scriptPackage[scriptPackage.length-1]);
			buff.append(";\n");
			buff.append('\n');
			buff.append('\n');
		}
		if (classType == PLAIN_CLASS) {
			buff.append("public class " + className);
			buff.append(" {\n");
			buff.append("    public " + className + "() {\n");
			buff.append("    }\n");
			buff.append("}");
		}else{
			buff.append("public class " + className);
			buff.append(" {\n");
			buff.append("    private static " + className + " instance = null;\n");
			buff.append('\n');
			buff.append("    private " + className + "() {\n");
			buff.append("    }\n");
			buff.append('\n');
			buff.append("    public static " + className + " getInstance() {\n");
			buff.append("        if (instance == null)\n");
			buff.append("            instance = new " + className + "();\n");
			buff.append("        return instance;\n");
			buff.append("    }\n");
			buff.append("}");
		}
		script = buff.toString();
//		modified = true;
	}

/*	public boolean isModified() {
		return modified;
	}

	void setModified(boolean modified) {
		this.modified = modified;
	}
*/
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure ss = (StorageStructure) in.readObject();
		int storageFormatVersion = ss.getDataVersionID();
		className = (String) ss.get("Class name");
		scriptPackage = (String[]) ss.get("Package");
		fullClassName = (String) ss.get("Full class name");
		if (storageFormatVersion < 3) {
			script = (String) ss.get("Script");
		}
		currentLine = ss.get("Current line", 1);
		innerClassNames = (String[]) ss.get("Inner class names");
        listenerClassBytes = (byte[][]) ss.get("Class bytecode");

        Class listenerClass = null;
		// Every Script is loaded by a new SimpleClassLoader, one for each Script.
        SimpleClassLoader loader = SimpleClassLoader.getNewInstance(fullClassName);
        try{
            listenerClass = loader.loadClassFromByteArray(listenerClassBytes[0]);
            // If the listener declared any inner class, then reload them too.
            for (int i=1; i<listenerClassBytes.length; i++) {
                loader.loadClassFromByteArray(listenerClassBytes[i]);
            }
        }catch (Throwable thr) {
            thr.printStackTrace();
            System.out.println("Unable to load script");
            return;
        }
    }

	/* Used to store any inner classes that a java script may define.
	 */
	void addInnerClass(String innerClassName, byte[] listenerBytes) {
//System.out.println("addInnerClass() " + innerClassName + ", listenerClassBytes.length: " + listenerClassBytes.length);
		int newLength = listenerClassBytes.length+1;
		byte[][] tmp = new byte[newLength][0];
		for (int i=0; i<listenerClassBytes.length; i++)
			tmp[i] = listenerClassBytes[i];
		tmp[newLength-1] = listenerBytes;
		String[] tmpClassNames = new String[newLength];
		for (int i=0; i<innerClassNames.length; i++) {
			tmpClassNames[i] = innerClassNames[i];
		}
		tmpClassNames[newLength-1] = innerClassName;

		listenerClassBytes = tmp;
		innerClassNames = tmpClassNames;
	}

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fm2 = new ESlateFieldMap2(FORMAT_VERSION, 6);
		fm2.put("Class name", className);
		fm2.put("Package", scriptPackage);
//        fm2.put("Script", script); Changed from storage format version 2
		fm2.put("Full class name", fullClassName);
        fm2.put("Class bytecode", listenerClassBytes);
		fm2.put("Inner class names", innerClassNames);
		fm2.put("Current line", currentLine);
        out.writeObject(fm2);
    }

	public String toString() {
		return fullClassName;
	}

	public void setPackage(String[] p) {
		scriptPackage = p;
		calculateFullClassName();
	}

	public String[] getPackage() {
		return scriptPackage;
	}

	public void setClassName(String className) {
		this.className = className;
		calculateFullClassName();
	}
	public String getClassName() {
		return className;
	}

	public String getFullClassName() {
		return fullClassName;
	}

	/**
	 * Calculates the full class name of a Script. The full class name is
	 * created by combining the 'scriptPackage' and the 'className'.
	 */
	private void calculateFullClassName() {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<scriptPackage.length; i++) {
			buff.append(scriptPackage[i]);
			buff.append('.');
		}
		buff.append(className);
		fullClassName = buff.toString();
	}

	public void resetInnerClasses() {
		innerClassNames = new String[0];
		listenerClassBytes = new byte[0][0];
	}

/*
    public void setListenerClassBytes(byte[][] listenerClassBytes) {
        this.listenerClassBytes = listenerClassBytes;
    }
*/

}
