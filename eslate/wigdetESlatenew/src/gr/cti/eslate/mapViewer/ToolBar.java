package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * The toolbar.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	4.0.0, 3-May-2001
 */
public class ToolBar extends ImageJPanel implements Externalizable {
	/**
	 * Creates the toolbar with vertical orientation.
	 */
	public ToolBar() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setOpaque(false);
		setBorder(new EmptyBorder(1,1,1,1));
		setAlignmentX(LEFT_ALIGNMENT);
		setAlignmentY(TOP_ALIGNMENT);
	}
	/**
	 * Creates the toolbar with the specified orientation.
	 * @orientation One of VERTICAL, HORIZONTAL.
	 */
	public ToolBar(int orientation) {
		this();
		setOrientation(orientation);
	}
	/**
	 * Gets the default ButtonGroup that ToggleButton Tools use. One may use other ButtonGroups
	 * as well, but this is the one that most of the Tools use.
	 * @return  The default ButtonGroup.
	 */
	public ButtonGroup getDefaultButtonGroup() {
		return defbg;
	}
	/**
	 * Sets the orientation.
	 * Valid values VERTICAL, HORIZONTAL.
	 */
	protected void setOrientation(int orientation) {
		Component[] c=getComponents();
		this.orientation=orientation;
		removeAll();
		if (orientation==VERTICAL){
			setAlignmentX(CENTER_ALIGNMENT);
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		}else if (orientation==HORIZONTAL)
			setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		for (int i=0;i<c.length;i++) {
			super.add(c[i]);
			if (c[i] instanceof Tool)
				((Tool) c[i]).setOrientation(orientation);
		}
		revalidate();
	}

	int getOrientation() {
		return orientation;
	}
	/**
	 * Adds a component. The id is a string defining the name of the tool internally.
	 * This is used in Externalization and is needed to ensure that new versions with more
	 * tools will function properly, replacing the correct default tools with the saved ones.
	 * @throws  IllegalArgumentException    If the id given is null, or if the key already exists.
	 */
	public Component add(String id,Component c) {
		if (id==null || components.containsKey(id))
			throw new IllegalArgumentException("Null tool-key or tool-key already exists in the toolbar");
		try {
			if (orientation==VERTICAL)
				((JComponent) c).setAlignmentX(CENTER_ALIGNMENT);
			else
				((JComponent) c).setAlignmentY(CENTER_ALIGNMENT);
		} catch(ClassCastException e) {
			//Not a JComponent, cannot align it.
		}
		components.put(id,c);
		order.add(id);
		return super.add(c);
	}
	/**
	 * Adds a component. The id is a string defining the name of the tool internally.
	 * This is used in Externalization and is needed to ensure that new versions with more
	 * tools will function properly, replacing the correct default tools with the saved ones.
	 * @throws  IllegalArgumentException    If the id given is null, or if the key already exists.
	 */
	public Component add(String id,int index,Component c) {
		if (id==null || components.containsKey(id))
			throw new IllegalArgumentException("Null tool-key or tool-key already exists in the toolbar");

		Component[] cs=getComponents();
		removeAll();
		for (int i=0;i<index;i++){
			super.add(cs[i]);
			align(cs[i]);       //ADDED BY N
		}
		super.add(c);
		align(c);               //ADDED BY N

		for (int i=index;i<cs.length;i++){
			super.add(cs[i]);
			align(cs[i]);       //ADDED BY N
		}
		components.put(id,c);
		order.add(index,id);
		return c;

	}


///////////////////////////////////////////BELOW METHOD ADDED BY N\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	void align(Component c){
		 try {
			if (orientation==VERTICAL){
				((JComponent) c).setAlignmentX(CENTER_ALIGNMENT);
				((JComponent) c).setAlignmentY(CENTER_ALIGNMENT);
			}else{
				((JComponent) c).setAlignmentY(CENTER_ALIGNMENT);
			}
		} catch(ClassCastException e) {
			//Not a JComponent, cannot align it.
		}
	}

//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	/**
	 * Do not use this method. A <code>RuntimeException</code> will be thrown.
	 * You should only use <code>add(String,Component)</code> or
	 * <code>add(String,int,Component)</code>.
	 */
	public Component add(Component c) {
		throw new RuntimeException("This method is not implemented (and will not be!). Please use add(String,Component) or add(String,int,Component).");
	}
	/**
	 * Do not use this method. A <code>RuntimeException</code> will be thrown.
	 * You should only use <code>add(String,Component)</code> or
	 * <code>add(String,int,Component)</code>.
	 */
	public Component add(Component c,int index) {
		throw new RuntimeException("This method is not implemented (and will not be!). Please use add(String,Component) or add(String,int,Component).");
	}
	/**
	 * Do not use this method. A <code>RuntimeException</code> will be thrown.
	 * You should only use <code>add(String,Component)</code> or
	 * <code>add(String,int,Component)</code>.
	 */
	public void add(Component c,Object contstraint) {
		throw new RuntimeException("This method is not implemented (and will not be!). Please use add(String,Component) or add(String,int,Component).");
	}
	/**
	 * Do not use this method. A <code>RuntimeException</code> will be thrown.
	 * You should only use <code>add(String,Component)</code> or
	 * <code>add(String,int,Component)</code>.
	 */
	public void add(Component c,Object constraint,int index) {
		throw new RuntimeException("This method is not implemented (and will not be!). Please use add(String,Component) or add(String,int,Component).");
	}

	public void revalidate() {
		if (components!=null) {
			removeAll();
			for (int i=0;i<order.size();i++) {
				Component c=(Component) components.get(order.get(i));
				if (c.isVisible())
					super.add(c);
			}
		}
		super.revalidate();
	}
	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		for (int i=0;i<getComponentCount();i++)
			((Component) getComponents()[i]).setFont(f);
	}
	/**
	 * Gets a tool by its key-name.
	 */
	public Component getTool(String key) {
		return (Component) components.get(key);
	}
	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		setOrientation(ht.get("___$toolbarorientation",ht.get("toolbarorientation",orientation)));
		setOpaque(ht.get("___$opaque",ht.get("opaque",isOpaque())));
		setVisible(ht.get("___$visible",ht.get("visible",isVisible())));
		//Restore the tools
		ArrayList savedcompos=(ArrayList) ht.get("___$savedcompos");
		Iterator it;
		//For old version saved Toolbar, the keys are not saved as well, so get only the
		//ones added by the component.
		if (savedcompos==null)
			it=components.keySet().iterator();
		else
			it=savedcompos.iterator();
		int counter=-1;
		while (it.hasNext()) {
			counter++;
			AbstractButton b=null, o=null;
			String key=(String) it.next();
			Object chkj=components.get(key);
			if (!(chkj instanceof JComponent) && chkj!=null)
				continue;
			JComponent bb=(JComponent) ht.get(key);
			if (bb instanceof AbstractButton)
				b=(AbstractButton) bb;
			if (chkj==null) {
				if (bb instanceof JToggleTool)
					chkj=new JToggleTool(null,"","",(MapViewer) SwingUtilities.getAncestorOfClass(MapViewer.class,ToolBar.this),null,(((JToggleTool)bb).isondefaultbttngrp)?getDefaultButtonGroup():null);
				else if (bb instanceof JButtonTool)
					chkj=new JButtonTool(null,"",(MapViewer) SwingUtilities.getAncestorOfClass(MapViewer.class,ToolBar.this));
				else
					continue;
				add(key,counter,(Component) chkj);
			}
			JComponent oo=(JComponent) chkj;
			if (oo instanceof AbstractButton)
				o=(AbstractButton) oo;
			if (b!=null) {
				if (o!=null) {
					//This means it is a button
					if (b.getIcon()!=null)
						o.setIcon(b.getIcon());
					if (b.getSelectedIcon()!=null)
						o.setSelectedIcon(b.getSelectedIcon());
					if (b.getRolloverIcon()!=null)
						o.setRolloverIcon(b.getRolloverIcon());
					if (b.getPressedIcon()!=null)
						o.setPressedIcon(b.getPressedIcon());
					if (b.isSelected()) {
						final AbstractButton act=o;
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								act.doClick();
							}
						});
					}
				}
				if (((Tool) b).getHelpText()!=null)
					((Tool) oo).setHelpText(((Tool) b).getHelpText());
//				((Tool) oo).setBorderPolicy(((Tool) b).getBorderPolicy());
			}
			if (bb!=null) {
				if (bb.getToolTipText()!=null)
					oo.setToolTipText(bb.getToolTipText());
				if (bb instanceof ZoomSlider) {
					((ZoomSlider) oo).setMinimum(((ZoomSlider) bb).getMinimum());
					((ZoomSlider) oo).setMaximum(((ZoomSlider) bb).getMaximum());
					((ZoomSlider) oo).setValue(((ZoomSlider) bb).getValue());
				}
				oo.setVisible(bb.isVisible());
				oo.setOpaque(bb.isOpaque());
			}
		}
	}
	/**
	 * Externalization input to use with ESlateToolBar.
	 */
	public void compatibilityReadExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		if ("1.0".equals(ht.getDataVersion())) {
			Enumeration keys=ht.keys();
			while (keys.hasMoreElements()) {
				String key=(String) keys.nextElement();
				Object o=ht.get(key);
				if (o instanceof Component) {
					if (((Component) o).isVisible())
						add(key,(Component) o);
				}
			}
		} else {
			setOrientation(ht.get("___$toolbarorientation",ht.get("toolbarorientation",orientation)));
			setOpaque(ht.get("___$opaque",ht.get("opaque",isOpaque())));
			setVisible(ht.get("___$visible",ht.get("visible",isVisible())));
			//Restore the tools
			//String[] tn=new String[] {"navigate","activate","browse","select","selectCircle","pan","edit","goIn","goOut","rotate","meter","identify","layerVisib","legend","miniature","grid","spot","zoomRect"};
			ArrayList sc=(ArrayList) ht.get("___$savedcompos");
			int counter=-1;
			if (sc!=null) {
				for (int i=0;i<sc.size();i++) {
				//for (int i=0;i<tn.length;i++) {
					AbstractButton chkj,bb;
					try {
						bb=(AbstractButton) ht.get((String) sc.get(i));
						if (bb==null)
							continue; //Tool does not exist
						counter++;
					} catch(Throwable t) {continue;}
					add((String)sc.get(i),counter,bb);
				}
			}
		}
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		//Put ___ in front of key names to avoid interference with key names given by the user
		ht.put("___$toolbarorientation",orientation);
		ht.put("___$opaque",isOpaque());
		ht.put("___$visible",isVisible());
		//Save the tools
		Enumeration e=components.keys();
		//Put the key names ordered. This must be done for the tools that are added by the
		//user, which don't know their position in the toolbar.
		Component[] c=getComponents();
		ArrayList savecompos=new ArrayList(c.length);
		//Populate the array
		for (int i=0;i<c.length;i++)
			savecompos.add(null);
		while (e.hasMoreElements()) {
			String key=(String) e.nextElement();
			Component cmp=(Component) components.get(key);
			//Find the order of the component
			for (int i=0;i<c.length;i++)
				if (c[i]==cmp) {
					savecompos.set(i,key);
					break;
				}
			ht.put(key,components.get(key));
		}
		//Trim the array
		for (int i=savecompos.size()-1;i>-1;i--)
			if (savecompos.get(i)==null)
				savecompos.remove(i);

		ht.put("___$savedcompos",savecompos);
		out.writeObject(ht);
		out.flush();
	}

	static final int HORIZONTAL=0;
	static final int VERTICAL=1;
	static final int NORTH=0;
	static final int SOUTH=1;
	static final int EAST=2;
	static final int WEST=3;

	//Externalization
	static final long serialVersionUID=3000L;

	private int orientation;
	private Hashtable components=new Hashtable();
	private ArrayList order=new ArrayList();
	private ButtonGroup defbg=new ButtonGroup() {
			/**
			 * This method is overriden to ensure that after deselecting all tools,
			 * no mouse listeners or cursors will exist in the map pane.
			 */
			public void setSelected(ButtonModel m,boolean b) {
				MapViewer viewer=(MapViewer) SwingUtilities.getAncestorOfClass(MapViewer.class,ToolBar.this);
				if (m==null && viewer!=null) {
					viewer.getMapPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					viewer.getMapPane().removeAllListeners();
				}
				super.setSelected(m,b);
			}
		};

}
