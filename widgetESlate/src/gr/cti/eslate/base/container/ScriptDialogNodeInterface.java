package gr.cti.eslate.base.container;

/**
 * <p>Title: ScriptDialogNodeInterface</p>
 * <p>Description: This interface is implemented by both <code>ScriptListenerNode</code>
 * and <code>ScriptNode</code>. It provides the base to handle both types of nodes in
 * the ScriptDialog.
 * </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

public interface ScriptDialogNodeInterface {
	/**
	 * Returns the node's name.
	 */
	public String getName();
	public String getScript();
	/**
	 * Returns the event's variable. Useful only for ScriptListenerNodes.
	 */
	public String[] getEventVariables();
	public int getScriptLanguage();
	public void setName(String name);
	/**
	 * Returns either a ScriptListener or a Script.
	 */
	public Object getScriptContainer();
	public byte[] getListenerClassBytes(int index);
	public void setListenerClassBytes(byte[][] bytes);
	public void setListenerClassBytes(int index, byte[] bytes);
	public void addInnerClass(String innerClassName, byte[] innerClassBytes);
	/** Returns the currentLine of the node. When a node's script
	 *  is displayed, the editor goes to this line.
	 */
	public int getCurrentLine();
	/**
	 * Sets the current line for a node. The line is set when the script is removed
	 * from the editor (when it is closed), in order to display the script at this
	 * line when the script is edited again.
	 */
	public void setCurrentLine(int line);
	/**
	 * Gets rid of the existing inner classes.
	 */
	public void resetInnerClasses();
}
