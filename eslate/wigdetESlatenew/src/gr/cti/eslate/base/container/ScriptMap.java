package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.BufferedOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

public class ScriptMap implements Externalizable {
    public static final int STR_FORMAT_VERSION = 1;
    static final long serialVersionUID = 12;
    GlobalScriptTopNode parentScriptNode = null;
    ArrayList scripts = new ArrayList();
	boolean classesExtracted = false;

    public ScriptMap() {
    }

	void addScript(Script script) {
		if (scripts.contains(script)) return;
		scripts.add(script);
		if (parentScriptNode != null) {
			ScriptNode node = new ScriptNode(script, parentScriptNode);
			parentScriptNode.add(node);
		}
	}

	/**
	 * Returna a ScriptNode adjacent to the given one.
	 * @param node The node relative to which a new node is returned.
	 * @param previous Determines the serach direction.
	 * @return The ScriptNode before are after the given node, if exists.
	 */
	ScriptNode getScriptNode(ScriptNode node, boolean previous) {
		int index = scripts.indexOf(node.getScript());
		if (index == -1) return null;
		if (previous) index--;
		else index++;
		if (index < 0 || index >=scripts.size()) {
			return null;
		}
		Script script = (Script) scripts.get(index);
		return getScriptNode(script);
	}

	void removeScript(Script script) {
		// If the script to be removed is contained in the 'scripts' ArrayList.
		if (scripts.remove(script)) {
			if (parentScriptNode != null) {
				ScriptNode node = getScriptNode(script);
				parentScriptNode.scriptNodes.remove(node);
				node.parent = null;
			}
		}
	}

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure ss = (StorageStructure) in.readObject();
        scripts = (ArrayList) ss.get("Scripts");
		// Clear the class files of the scripts that may exists in the script dir
		ESlateContainerUtils.removeDirectoryContents(ESlateContainerUtils.scriptDir);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fm2 = new ESlateFieldMap2(STR_FORMAT_VERSION, 1);
        fm2.put("Scripts", scripts);
        out.writeObject(fm2);
    }

    void clear(ESlateContainer container) {
        scripts.clear();
        if (parentScriptNode != null) {
            Enumeration enumeration = parentScriptNode.children();
            while (enumeration.hasMoreElements())
                ((ScriptNode) enumeration.nextElement()).parent = null;
			parentScriptNode.removeAllChildren();
            parentScriptNode = null;
        }
		// Clear the class files of the scripts that may exists in the script dir
		ESlateContainerUtils.removeDirectoryContents(ESlateContainerUtils.scriptDir);
    }

	public ScriptNode getScriptNode(Script s) {
		if (parentScriptNode == null) return null;
		if (s == null) return null;
		Enumeration enumeration = parentScriptNode.children();
		while (enumeration.hasMoreElements()) {
			ScriptNode sn = (ScriptNode) enumeration.nextElement();
			if (sn.getScriptContainer() == s) {
				return sn;
			}
		}
		return null;
	}

	public boolean containsScript(String fullClassName) {
		for (int i=0; i<scripts.size(); i++) {
			if (((Script) scripts.get(i)).getFullClassName().equals(fullClassName)) {
				return true;
			}
		}
		return false;
	}

    GlobalScriptTopNode getScriptTree(ResourceBundle bundle) {
        if (parentScriptNode == null)
            createScriptTree(bundle);
        return parentScriptNode;
    }

    private void createScriptTree(ResourceBundle bundle) {
        if (parentScriptNode != null) return;
        parentScriptNode = new GlobalScriptTopNode(bundle.getString("Java classes"));
        for (int i=0; i<scripts.size(); i++) {
            ScriptNode node = new ScriptNode((Script) scripts.get(i), parentScriptNode);
            parentScriptNode.add(node);
        }
    }

	/**
	 * This method extracts the classes of all the Scripts of the ScriptMap into
	 * files int the 'scriptDir'. This is needed in order for jikes to compile
	 * any class which uses these classes.
	 */
	public void extractClassFiles() {
		if (classesExtracted) return;
		classesExtracted = true;
		File scriptDir = ESlateContainerUtils.getScriptDir();
		for (int i=0; i<scripts.size(); i++) {
			Script script = (Script) scripts.get(i);
			File outputDir = scriptDir;
			if (script.getPackage().length > 0) {
				for (int k=0; k<script.getPackage().length; k++) {
					outputDir = new File(outputDir, script.getPackage()[k]);
				}
				if (!outputDir.exists() && !outputDir.mkdirs()) {
					System.out.println("Could not create directory " + outputDir);
				}
			}
			File scriptClassFile = new File(outputDir, script.getClassName() + ".class");
			try {
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(scriptClassFile));
				out.write(script.listenerClassBytes[0], 0, script.listenerClassBytes[0].length);
				out.flush();
				out.close();
			} catch (Exception ex) {
				System.err.println("Couldn't extract script " + script.getClassName());
				System.err.println("   " + ex);
			}
			// Also extract all the inner classes
			for (int k=1; k<script.listenerClassBytes.length; k++) {
				File innerClassFile = new File(outputDir, script.innerClassNames[k]);
				try {
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(innerClassFile));
					out.write(script.listenerClassBytes[k], 0, script.listenerClassBytes[k].length);
					out.flush();
					out.close();
				} catch (Exception ex) {
					System.err.println("Couldn't extract inner script class " +script.innerClassNames[k]);
					System.err.println("   " + ex);
				}

			}
		}
	}
}

class GlobalScriptTopNode implements MutableTreeNode {
    ArrayList scriptNodes = new ArrayList();
    MutableTreeNode parent = null;
    Object name = null;

    public GlobalScriptTopNode(Object name) {
        this.name = name;
    }

    public void add(ScriptNode node) {
        scriptNodes.add(node);
    }

    public TreeNode getChildAt(int childIndex) {
        return (ScriptNode) scriptNodes.get(childIndex);
    }

    public int getChildCount() {
        return scriptNodes.size();
    }
    public TreeNode getParent() {
        return parent;
    }
    public int getIndex(TreeNode node) {
        return scriptNodes.indexOf(node);
    }
    public boolean getAllowsChildren() {
        return true;
    }
    public boolean isLeaf() {
        return false;
    }
    public void insert(MutableTreeNode child, int index) {
        scriptNodes.add(index, child);
    }
    public void remove(int index) {
        scriptNodes.remove(index);
    }
    public void remove(MutableTreeNode node) {
        scriptNodes.remove(node);
    }
    public void setUserObject(Object object) {
        name = object;
    }
    public void removeFromParent() {
        parent.remove(this);
        parent = null;
    }
    public void setParent(MutableTreeNode newParent) {
        parent = newParent;
    }
    public Enumeration children() {
        Enumeration enumeration = new Enumeration() {
            int index = 0;
            public boolean hasMoreElements() {
                if (index < scriptNodes.size())
                    return true;
                return false;
            }
            public Object nextElement() {
                Object obj = GlobalScriptTopNode.this.scriptNodes.get(index);
                index++;
                return obj;
            }
        };
        return enumeration;
    }
	public void removeAllChildren() {
		scriptNodes.clear();
	}
}
