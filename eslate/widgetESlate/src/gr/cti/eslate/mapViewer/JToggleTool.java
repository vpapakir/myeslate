package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Cursor;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;


/**
 * The ToggleTool for the Toolbar.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
public class JToggleTool extends gr.cti.eslate.utils.NoBorderToggleButton implements Tool,Externalizable {
	/**
	 * Used only by the externalization mechanism.
	 */
	public JToggleTool() {
	}

	public JToggleTool(ImageIcon icon,String tooltip,String hlp,MapViewer viewr,MouseInputListener mouse,ButtonGroup buttonGroup) {
		super(icon);
		setToolTipText(tooltip);
		setMargin(new Insets(1,1,1,1));
		setFocusPainted(false);
		setRequestFocusEnabled(false);
		setOpaque(false);
		setBorderPolicy(ONMOUSE);
		this.viewer=viewr;
		this.help=hlp;
		ttchanged=htchanged=false;
		//This forces the icon to be saved. I used this trick when we wanted the user to add
		//tools as well. To force them save their icon I did this. The other constructor
		//(which is used only by my component makes it false.
		icchanged=true;
		mouseListener=mouse;
		if (viewer!=null && viewer.getMapPane()!=null)
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (mouseListener!=null) {
						JToggleTool.this.viewer.getMapPane().removeAllListeners();
						JToggleTool.this.viewer.getMapPane().addMouseInputListener(mouseListener);
					}
					if (help!=null)
						viewer.getStatusBar().setMessage(help,0);
				}
			});
		if (buttonGroup!=null) {
			this.buttonGroup=buttonGroup;
			buttonGroup.add(this);
		}
	}
	/**
	 * Internal use only. Used by internaly made tools.
	 */
	JToggleTool(ImageIcon icon,String tooltip,String hlp,MapViewer viewr,MouseInputListener mouse,ButtonGroup buttonGroup,boolean donotForceIconSave) {
		this (icon,tooltip,hlp,viewr,mouse,buttonGroup);
		icchanged=false;
	}
	/**
	 * This MouseInputListener will be added on the map pane when this tool is selected.
	 */
	public void setMouseInputListener(MouseInputListener mil) {
		mouseListener=mil;
	}
	/**
	 * Overriden to show the tooltip in the panel font.
	 */
	public JToolTip createToolTip() {
		JToolTip t = super.createToolTip();
		t.setFont(getFont());
		return t;
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if ((!enabled) && (isSelected())) {
			if (buttonGroup!=null)
				buttonGroup.setSelected(null,true);
			viewer.getStatusBar().clearMessage();
			viewer.getMapPane().removeAllListeners();
			viewer.getMapPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void setIcon(Icon ic) {
		setDisabledIcon(null);
		super.setIcon(ic);
		icchanged=true;
		repaint();
	}

	public void setVisible(boolean b) {
		super.setVisible(b);
		if (viewer!=null) {
			viewer.getToolBar().invalidate();
			viewer.getToolBar().validate();
			viewer.getToolBar().repaint();
		}
	}
	/**
	 * The help text is written in the status/message bar when the tool is selected.
	 */
	public void setHelpText(String ht) {
		help=ht;
		htchanged=true;
	}
	/**
	 * The help text is written in the status/message bar when the tool is selected.
	 */
	public String getHelpText() {
		return help;
	}

	public boolean isHelpTextChanged() {
		return htchanged;
	}

	public void setToolTipText(String tt) {
		super.setToolTipText(tt);
		ttchanged=true;
	}

	public void setBorderPolicy(int bp) {
		if (bp==ALWAYS) {
			setBorderPainted(true);
			removeMouseListener(mList);
			removeItemListener(iList);
		} else if (bp==NEVER) {
			setBorderPainted(false);
			removeMouseListener(mList);
			removeItemListener(iList);
		} else if (bp==ONMOUSE) {
			setBorderPainted(false);
			addMouseListener(mList);
			addItemListener(iList);
		} else
			throw new IllegalArgumentException("MAPVIEWER#200004061848: Invalid border policy");
		bPolicy=bp;
	}

	public int getBorderPolicy() {
		return bPolicy;
	}

	public void setOrientation(int o) {
	}
	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		setSelected(ht.get("selected",false));
		setOpaque(ht.get("opaque",isOpaque()));
		setVisible(ht.get("visible",isVisible()));
		setBorderPolicy(ht.get("borderpolicy",getBorderPolicy()));
		ttchanged=ht.get("ttchanged",false);
		if (ttchanged)
			setToolTipText(ht.get("tooltiptext",getToolTipText()));
		htchanged=ht.get("htchanged",false);
		if (htchanged)
			setHelpText(ht.get("helptext",getHelpText()));
		icchanged=ht.get("icchanged",false);
		if (icchanged)
			setIcon((Icon) ht.get("icon",getIcon()));
		if (ht.get("selectedIcon",(Icon) null)!=null)
			setSelectedIcon((Icon) ht.get("selectedIcon"));
		if (ht.get("rolloverIcon",(Icon) null)!=null)
			setRolloverIcon((Icon) ht.get("rolloverIcon"));
		if (ht.get("pressedIcon",(Icon) null)!=null)
			setPressedIcon((Icon) ht.get("pressedIcon"));
		isondefaultbttngrp=ht.get("isondefaultbttngrp",false);
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("selected",isSelected());
		ht.put("opaque",isOpaque());
		ht.put("visible",isVisible());
		ht.put("borderpolicy",getBorderPolicy());
		ht.put("ttchanged",ttchanged);
		if (ttchanged)
			ht.put("tooltiptext",getToolTipText());
		ht.put("htchanged",htchanged);
		if (htchanged)
			ht.put("helptext",getHelpText());
		ht.put("icchanged",icchanged);
		if (icchanged)
			if (getIcon()!=null)
				ht.put("icon",wrapIcon(getIcon()));
		if (getSelectedIcon()!=null) {
			if (getSelectedIcon() instanceof NewRestorableImageIcon)
				ht.put("selectedIcon",getSelectedIcon());
			else
				ht.put("selectedIcon",wrapIcon(getSelectedIcon()));
		}
		if (getRolloverIcon()!=null) {
			if (getRolloverIcon() instanceof NewRestorableImageIcon)
				ht.put("rolloverIcon",getRolloverIcon());
			else
				ht.put("rolloverIcon",wrapIcon(getRolloverIcon()));
		}
		if (getPressedIcon()!=null) {
			if (getPressedIcon() instanceof NewRestorableImageIcon)
				ht.put("pressedIcon",getPressedIcon());
			else
				ht.put("pressedIcon",wrapIcon(getPressedIcon()));
		}

		ToolBar tb=(ToolBar) SwingUtilities.getAncestorOfClass(ToolBar.class,this);
		if (tb!=null && buttonGroup==tb.getDefaultButtonGroup())
			ht.put("isondefaultbttngrp",true);

		out.writeObject(ht);
	}

	private NewRestorableImageIcon wrapIcon(Icon i) {
		BufferedImage bi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(i.getIconWidth(),i.getIconHeight(),Transparency.TRANSLUCENT);
		i.paintIcon(this,bi.getGraphics(),0,0);
		return new NewRestorableImageIcon(bi);
	}

	private int bPolicy;
	private MapViewer viewer;
	private MouseInputListener mouseListener;
	private ButtonGroup buttonGroup;
	private String help;
	/**
	 * These booleans show if there has been a change in a default value of the property.
	 * This value is saved to help minimizing the saved information as well as making sure
	 * that the new versions will show the new default values, if any.
	 */
	private boolean ttchanged,htchanged,icchanged;
	private MouseListener mList=new MouseAdapter() {
		public void mouseExited(MouseEvent mouseevent) {
			if (isEnabled() && !isSelected()) {
				setBorderPainted(false);
				repaint();
			}
		}

		public void mouseEntered(MouseEvent mouseevent) {
			if (isEnabled() && !isSelected()) {
				setBorderPainted(true);
				repaint();
			}
		}
	};
	private ItemListener iList=new ItemListener() {
		public void itemStateChanged(ItemEvent itemevent) {
			setBorderPainted(isSelected());
			repaint();
		}
	};
	//Used only when restoring
	boolean isondefaultbttngrp;
	//Externalization
	static final long serialVersionUID=3000L;
}
