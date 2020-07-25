package gr.cti.eslate.protocol;

import java.awt.Color;
import java.awt.Font;

/**
 * Describes label fonts.
 * @author Giorgos Vasiliou
 * @version 1.0
 */

public interface LabelFont {
	public abstract Font getFont();
	public abstract Color getForeground();
	public abstract Color getBackground();
}