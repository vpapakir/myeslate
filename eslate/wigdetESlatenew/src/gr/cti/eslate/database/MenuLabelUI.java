/*
 * @(#)BasicLabelUI.java	1.48 98/02/02
 *
 * Copyright (c) 1997 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package gr.cti.eslate.database;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.Serializable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;

/**
 * A Windows L&F implementation of LabelUI.  This implementation
 * is completely static, i.e. there's only one UIView implementation
 * that's shared by all JLabel objects.
 * <p>
 * Warning: serialized objects of this class will not be compatible with
 * future swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between Swing1.0 applications.  It will
 * not be possible to load serialized Swing1.0 objects with future releases
 * of Swing.  The JDK1.2 release of Swing will be the compatibility
 * baseline for the serialized form of Swing objects.
 *
 * @version 1.48 02/02/98
 * @author Hans Muller
 */
public class MenuLabelUI extends LabelUI
	implements Serializable, PropertyChangeListener
{
    protected static MenuLabelUI labelUI = new MenuLabelUI();

    /**
     * Forwards the call to SwingUtilities.layoutCompoundLabel().
     * This method is here so that a subclass could do Label specific
     * layout and to shorten the method name a little.
     *
     * @see SwingUtilities#layoutCompoundLabel
     */
    protected String layoutCL(
        JLabel label,
        FontMetrics fontMetrics,
      	String text,
      	Icon icon,
      	Rectangle viewR,
      	Rectangle iconR,
      	Rectangle textR)
    {
        return SwingUtilities.layoutCompoundLabel(
            fontMetrics,
	          text,
      	    icon,
	          label.getVerticalAlignment(),
      	    label.getHorizontalAlignment(),
	          label.getVerticalTextPosition(),
      	    label.getHorizontalTextPosition(),
	          viewR,
	          iconR,
      	    textR,
	          label.getIconTextGap());
    }

    /**
     * Paint clippedText at textX, textY with the labels foreground color.
     *
     * @see #paint
     * @see #paintDisabledText
     */
    protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY)
    {
      	int accChar = l.getDisplayedMnemonic();
    	  g.setColor(l.getForeground());
    	  BasicGraphicsUtils.drawString(g, s, accChar, textX, textY);
    }


    /**
     * Paint clippedText at textX, textY with background.lighter() and then
     * shifted down and to the right by one pixel with background.darker().
     *
     * @see #paint
     * @see #paintEnabledText
     */
    protected void paintDisabledText(JLabel l, Graphics g, String s, int textX, int textY)
    {
    	int accChar = l.getDisplayedMnemonic();
    	Color background = l.getBackground();
    	g.setColor(background.brighter());
    	BasicGraphicsUtils.drawString(g, s, accChar, textX + 1, textY + 1);
    	g.setColor(background.darker());
    	BasicGraphicsUtils.drawString(g, s, accChar, textX, textY);
    }


    /**
     * Paint the label text in the foreground color, if the label
     * is opaque then paint the entire background with the background
     * color.  The Label text is drawn by paintEnabledText() or
     * paintDisabledText().  The locations of the label parts are computed
     * by layoutCL.
     *
     * @see #paintEnabledText
     * @see #paintDisabledText
     * @see #layoutCL
     */
    public void paint(Graphics g, JComponent c)
    {
    	JLabel label = (JLabel)c;
    	String text = label.getText();
    	Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

    	if ((icon == null) && (text == null)) {
	        return;
    	}

    	FontMetrics fm = g.getFontMetrics();
    	Rectangle iconR = new Rectangle();
    	Rectangle textR = new Rectangle();
    	Rectangle viewR = new Rectangle(c.getSize());
    	Insets viewInsets = c.getInsets();

    	viewR.x = viewInsets.left;
    	viewR.y = viewInsets.top;
    	viewR.width -= (viewInsets.left + viewInsets.right);
    	viewR.height -= (viewInsets.top + viewInsets.bottom);

    	String clippedText = layoutCL(label, fm, text, icon, viewR, iconR, textR);

    	if (icon != null) {
	        icon.paintIcon(c, g, iconR.x, iconR.y);
    	}

    	if (text != null) {
	        int textX = textR.x;
    	    int textY = textR.y + fm.getAscent();

    	    if (label.isEnabled()) {
		          paintEnabledText(label, g, clippedText, textX, textY);
	        }
	        else {
      		    paintDisabledText(label, g, clippedText, textX, textY);
          }
	    }
    }


    public Dimension getPreferredSize(JComponent c)
    {
    	JLabel label = (JLabel)c;
    	String text = label.getText();
    	Icon icon = label.getIcon();
    	Insets insets = label.getInsets();
    	Font font = label.getFont();

    	int dx = insets.left + insets.right;
    	int dy = insets.top + insets.bottom;

    	if ((icon == null) && ((text == null) || ((text != null) && (font == null)))) {
	        return new Dimension(dx, dy);
    	}
    	else if ((text == null) || ((icon != null) && (font == null))) {
	        return new Dimension(icon.getIconWidth() + dx, icon.getIconHeight() + dy);
    	}
    	else {
	        FontMetrics fm = label.getToolkit().getFontMetrics(font);

    	    Rectangle iconR = new Rectangle();
	        Rectangle textR = new Rectangle();
    	    Rectangle viewR = new Rectangle(dx, dy, Short.MAX_VALUE, Short.MAX_VALUE);
	        layoutCL(label, fm, text, icon, viewR, iconR, textR);
    	    Dimension rv = iconR.union(textR).getSize();
	        rv.width += dx;
    	    rv.height += dy;
	        return rv;
    	}
    }


    /**
     * @return getPreferredSize(c)
     */
    public Dimension getMinimumSize(JComponent c) {
    	return getPreferredSize(c);
    }

    /**
     * @return getPreferredSize(c)
     */
    public Dimension getMaximumSize(JComponent c) {
    	return getPreferredSize(c);
    }


    public void installUI(JComponent c) {
        LookAndFeel.installColorsAndFont(c, "Label.background", "Label.foreground", "Label.font");
        installKeyboardActions(c);		// for labelFor/accel binding
        c.addPropertyChangeListener(this);	// "   "              "
    }

    public void uninstallUI(JComponent c) {
        uninstallKeyboardActions(c);
        c.removePropertyChangeListener(this);
    }

    protected void installKeyboardActions(JComponent c) {
    	  JLabel l = (JLabel) c;
        int dka = l.getDisplayedMnemonic();
      	Component lf = l.getLabelFor();
        c.resetKeyboardActions();
      	if ((dka != 0) && (lf != null)) {
	          l.registerKeyboardAction(
        	    new PressAction(l,lf),
		          KeyStroke.getKeyStroke(dka,ActionEvent.ALT_MASK,false),
      		    JComponent.WHEN_IN_FOCUSED_WINDOW);
	      }
    }

    protected void uninstallKeyboardActions(JComponent c) {
        c.resetKeyboardActions();
    }

    public static ComponentUI createUI(JComponent c) {
      	return labelUI;
    }

    public void propertyChange(PropertyChangeEvent e) {
      	if (e.getPropertyName().equals("labelFor") ||
	          e.getPropertyName().equals("displayedMnemonic")) {
      	    installKeyboardActions((JLabel) e.getSource());
      	}
    }

    // When the accelerator is pressed, temporarily make the JLabel
    // focusTraversable by registering a WHEN_FOCUSED action for the
    // release of the accelerator.  Then give it focus so it can
    // prevent unwanted keyTyped events from getting to other components.
    class PressAction extends AbstractAction {
      	JLabel	  owner;
        Component labelFor;

        PressAction(JLabel l, Component c) {
	          super("nothing");
      	    owner = l;
	          labelFor = c;
      	}

      	public void actionPerformed(ActionEvent e) {
	          owner.registerKeyboardAction(
      		    new ReleaseAction(owner,labelFor),
		          KeyStroke.getKeyStroke(owner.getDisplayedMnemonic(),
                                     ActionEvent.ALT_MASK,true),
                                     JComponent.WHEN_FOCUSED);

	          // Need this if the accelerator is released before the ALT key
      	    //
      	    owner.registerKeyboardAction(
		          new ReleaseAction(owner,labelFor),
      		    KeyStroke.getKeyStroke(0,ActionEvent.ALT_MASK,true),
		          JComponent.WHEN_FOCUSED);

      	    owner.requestFocus();
        }

      	public boolean isEnabled() {
	          return owner.isEnabled();
      	}
    }

    // On the release of the accelerator, remove the keyboard action
    // that allows the label to take focus and then give focus to the
    // labelFor component.
    class ReleaseAction extends AbstractAction {
	      JLabel	  owner;
        Component labelFor;

        ReleaseAction(JLabel l, Component c) {
    	    super("giveFocusToLabelFor");
	        owner = l;
    	    labelFor = c;
	      }

      	public void actionPerformed(ActionEvent e) {
	          owner.unregisterKeyboardAction(
               KeyStroke.getKeyStroke(owner.getDisplayedMnemonic(),
			  		   ActionEvent.ALT_MASK,true));
      	    owner.unregisterKeyboardAction(
		          KeyStroke.getKeyStroke(0,ActionEvent.ALT_MASK,true));
      	    labelFor.requestFocus();
        }

      	public boolean isEnabled() {
	          return owner.isEnabled();
      	}
    }
}

