package gr.cti.eslate.base.container;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

public class ScriptNode implements TreeNode, ScriptDialogNodeInterface {
    Script script = null;
    TreeNode parent = null;

    public ScriptNode(Script script, TreeNode parent) {
        this.script = script;
        this.parent = parent;
    }
    public TreeNode getChildAt(int childIndex) {
        return null;
    }
    public int getChildCount() {
        return 0;
    }
    public TreeNode getParent() {
        return parent;
    }
    public int getIndex(TreeNode node) {
        return -1;
    }
    public boolean getAllowsChildren() {
        return false;
    }
    public boolean isLeaf() {
        return true;
    }
    public Enumeration children() {
        return null;
    }

	public Object getUserObject() {
		return script;
	}

	public String toString() {
		return script.toString();
	}

	// Methods of the ScriptDialogNodeInterface
	public void setName(String name) {
		script.setClassName(name);
	}

	public int getScriptLanguage() {
		return ScriptListener.JAVA;
	}

	public String[] getEventVariables() {
		return new String[0];
	}

	public String getName() {
		return script.getClassName();
	}

    public String getScript() {
        return script.getScript();
    }

	public Object getScriptContainer() {
		return script;
	}

	public byte[] getListenerClassBytes(int index) {
		return script.getListenerClassBytes()[index];
	}

	public void setListenerClassBytes(byte[][] bytes) {
        script.listenerClassBytes = bytes;
//		script.setListenerClassBytes(bytes);
	}

	public void setListenerClassBytes(int index, byte[] bytes) {
        byte[][] listenerClassByte = script.getListenerClassBytes();
        listenerClassByte[index] = bytes;
//		script.listenerClassBytes[index] = bytes;
	}

	public void addInnerClass(String innerClassName, byte[] innerClassBytes) {
		script.addInnerClass(innerClassName, innerClassBytes);
	}

	public int getCurrentLine() {
		return script.currentLine;
	}

	public void setCurrentLine(int line) {
		script.currentLine = line;
	}

	public void resetInnerClasses() {
		script.resetInnerClasses();
	}

}
