package org.cbook.cbookif;

import javax.swing.Icon;

/**
 * Interface for CBook widgets. Gives access to an instance, the editor. 
 */
public interface CBookWidgetIF {
	
	/**
	 * A new CBook Widget instance.
	 * @param context
	 * @return instance
	 */
	CBookWidgetInstanceIF getInstance(CBookContext context);
	
	/**
	 * A new CBook Widget editor
	 * @param context
	 * @return editor
	 */
	CBookWidgetEditIF   getEditor(CBookContext context);
	
	/**
	 * A localized description of this widget.
	 * This string is used in a tool to select the right widget.
	 * @return a string
	 */
	String toString();
	
	/**
	 * An small icon representing this widget.
	 * If you have an image, you can convert this to an icon with <tt>new ImageIcon(image)</tt>
	 * The recommended size is 16x16 pixels.
	 * @see java.awt.Image
	 * @see javax.swing.ImageIcon#ImageIcon(java.awt.Image)
	 * @return an icon
	 */
	Icon getIcon();
}
