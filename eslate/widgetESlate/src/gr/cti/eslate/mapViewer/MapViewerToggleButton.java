package gr.cti.eslate.mapViewer;

import gr.cti.eslate.eslateToggleButton.ESlateToggleButton;
import gr.cti.eslate.eslateToolBar.ESlateToolBar;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.Icon;

/**
 * The ToggleTool for the Toolbar.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	4.0.0, 2-Apr-2002
 */
public class MapViewerToggleButton extends ESlateToggleButton implements ToolComponent {

	public MapViewerToggleButton() {
		super();
		ttchanged=htchanged=false;
		//This forces the icon to be saved. I used this trick when we wanted the user to add
		//tools as well. To force them save their icon I did this. The other constructor
		//(which is used only by my component makes it false.
		icchanged=true;
	}

	public void setIcon(Icon ic) {
		super.setIcon(ic);
		setDisabledIcon(null);
		icchanged=true;
		repaint();
	}

	/**
	 * Defines that the tool has the default icon.
	 */
	public void clearIconFlag() {
		icchanged=false;
	}
	/**
	 * Indicates a change in the icon.
	 */
	public boolean isIconChanged() {
		return icchanged;
	}
	/**
	 * Defines that the tool has the default help text.
	 */
	public void clearHelpTextFlag() {
		htchanged=false;
	}
	/**
	 * Indicates a change in the help text.
	 */
	public boolean isHelpTextChanged() {
		return htchanged;
	}
	/**
	 * Defines that the tool has the default tooltip text.
	 */
	public void clearToolTipTextFlag() {
		ttchanged=false;
	}
	/**
	 * Indicates a change in the tooltip text.
	 */
	public boolean isToolTipTextChanged() {
		return ttchanged;
	}

	/**
	 * The help text is written in the status/message bar when the tool is selected.
	 */
	public void setHelpText(String ht) {
		help=ht;
		htchanged=true;
		if (getESlateHandle().getParentHandle()!=null)
			((ESlateToolBar)getESlateHandle().getParentHandle().getComponent()).setAssociatedText(this,ht);
	}
	/**
	 * The help text is written in the status/message bar when the tool is selected.
	 */
	public String getHelpText() {
		if (htchanged)
			return help;
		else if (getESlateHandle().getParentHandle()!=null)
			return ((ESlateToolBar)getESlateHandle().getParentHandle().getComponent()).getAssociatedText(this);
		return null;
	}

	public void setToolTipText(String tt) {
		super.setToolTipText(tt);
		ttchanged=true;
	}

	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		//Keeps the current image to override ESlateButton's readExternal
		Icon ic=null;
		StorageStructure ht=(StorageStructure) in.readObject();
		icchanged=ht.get("icchanged",false);
		ttchanged=ht.get("ttchanged",false);
		htchanged=ht.get("htchanged",false);
		boolean lic=icchanged;
		boolean ltc=ttchanged;
		boolean lhc=htchanged;
		super.readExternal(in);
		setDisabledIcon(null);
		if (ltc)
			setToolTipText(ht.get("tooltiptext",getToolTipText()));
		if (lhc)
			help=ht.get("helptext",getHelpText());
		setVisible(ht.get("visible",true));
		icchanged=lic;
		ttchanged=ltc;
		htchanged=lhc;
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("ttchanged",ttchanged);
		if (ttchanged)
			ht.put("tooltiptext",getToolTipText());
		ht.put("htchanged",htchanged);
		if (htchanged)
			ht.put("helptext",getHelpText());
		ht.put("icchanged",icchanged);
		ht.put("visible",isVisible());
		out.writeObject(ht);
		super.writeExternal(out);
	}

	/**
	 * These booleans show if there has been a change in a default value of the property.
	 * This value is saved to help minimizing the saved information as well as making sure
	 * that the new versions will show the new default values, if any.
	 */
	private boolean ttchanged,htchanged,icchanged;
	String help;
}