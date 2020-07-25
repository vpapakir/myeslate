package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Transparency;
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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToolTip;

/**
 * The ButtonTool for the Toolbar.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
public class JButtonTool extends gr.cti.eslate.utils.NoBorderButton implements Tool,Externalizable {
	/**
	 * Used only by the externalization mechanism.
	 */
	public JButtonTool() {
	}

	public JButtonTool(ImageIcon icon,String tooltip,MapViewer viewr) {
		super(icon);
		setToolTipText(tooltip);
		setMargin(new Insets(0,0,0,0));
		setFocusPainted(false);
		setRequestFocusEnabled(false);
		setOpaque(false);
		setBorderPolicy(ONMOUSE);
		this.viewer=viewr;
		ttchanged=htchanged=false;
		//This forces the icon to be saved. I used this trick when we wanted the user to add
		//tools as well. To force them save their icon I did this. The other constructor
		//(which is used only by my component makes it false.
		icchanged=true;
	}
	/**
	 * Internal use only. Used by internaly made tools.
	 */
	JButtonTool(ImageIcon icon,String tooltip,MapViewer viewr,boolean donotForceIconSave) {
		this (icon,tooltip,viewr);
		icchanged=false;
	}

	/**
	 * Overriden to show the tooltip in the panel font.
	 */
	public JToolTip createToolTip() {
		JToolTip t = super.createToolTip();
		t.setFont(getFont());
		return t;
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

	public void setHelpText(String ht) {
	}

	public String getHelpText() {
		return "";
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
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("opaque",isOpaque());
		ht.put("selected",isSelected());
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

		out.writeObject(ht);
	}

	private NewRestorableImageIcon wrapIcon(Icon i) {
		BufferedImage bi=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(i.getIconWidth(),i.getIconHeight(),Transparency.TRANSLUCENT);
		i.paintIcon(this,bi.getGraphics(),0,0);
		return new NewRestorableImageIcon(bi);
	}

	private int bPolicy;
	private MapViewer viewer;
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

	//Externalization
	static final long serialVersionUID=3000L;
}

