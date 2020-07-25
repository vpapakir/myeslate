package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.OneLineBevelBorder;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * The statusbar.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
public class StatusBar extends ImageJPanel implements Externalizable {

	public StatusBar() {
		setLayout(new GridBagLayout());

		setBorder(new EmptyBorder(0,0,1,0));
		setOpaque(false);
		busyB=false;
	}

	StatusBar(MapViewer mapViewer) {
		this();
		this.mapViewer=mapViewer;
	}

	void setBusyIconPanelVisible(boolean b){
		if (b) {
			if (busy==null) {
				if (notBusyIcon==null)
					notBusyIcon=new ImageIcon(StatusBar.class.getResource("images/whiteball.gif"));
				busy=new JLabel(notBusyIcon);
				busy.setBorder(new CompoundBorder(new CompoundBorder(getBorder(),new OneLineBevelBorder(SoftBevelBorder.LOWERED)),getBorder()));
				if (lc==null)
					lc=new GridBagConstraints();
				lc.fill=GridBagConstraints.VERTICAL;
				lc.weightx=0;
				lc.weighty=1;
				lc.gridwidth=1;
				lc.gridx=1;
				lc.gridy=0;
				add(busy);
				((GridBagLayout) getLayout()).setConstraints(busy,lc);
			}
			busy.setVisible(true);
		} else {
			if (busy!=null)
				busy.setVisible(false);
			else
				busyIconVisible=false;
		}
	}

	boolean isBusyIconPanelVisible(){
		return (busy!=null && busy.isVisible());
	}


	void setScale(String scs) {
		if (scale==null) {
			scale=new JLabel("") {
				/**
				 * Overriden to show the tooltip in the panel font.
				 */
				public JToolTip createToolTip() {
					JToolTip t = super.createToolTip();
					t.setFont(getFont());
					return t;
				}
			};
			if (message!=null)
				scale.setBorder(message.getBorder());
			else if (busy!=null)
				scale.setBorder(busy.getBorder());
			else
				scale.setBorder(new CompoundBorder(new CompoundBorder(getBorder(),new OneLineBevelBorder(SoftBevelBorder.LOWERED)),getBorder()));
//			if (message!=null)
//				scale.setFont(message.getFont());
//			else
//				scale.setFont(new Font("Helvetica",Font.PLAIN,11));
			scale.setForeground(getForeground());
			if (lc==null)
				lc=new GridBagConstraints();
			lc.fill=GridBagConstraints.NONE;
			lc.weightx=0;
			lc.weighty=1;
			lc.gridwidth=1;
			lc.gridx=2;
			lc.gridy=0;
			add(scale);
			((GridBagLayout) getLayout()).setConstraints(scale,lc);
		}
		scale.setText(scs);
		scale.setToolTipText(MapViewer.messagesBundle.getString("mapscale"));
		invalidate();
		doLayout();
		if (scs==null || (scs!=null && scs.equals("")))
			scale.setToolTipText(null);
	}

	String getScale() {
		if (scale!=null)
			return scale.getText();
		else
			return null;
	}

	void setBusy(boolean b) {
		if (b==isBusy())
			return;
		if (isVisible()) {
			if (busy==null) {
				busy=new JLabel(notBusyIcon);
				busy.setVisible(busyIconVisible);
				busy.setBorder(new CompoundBorder(new CompoundBorder(getBorder(),new OneLineBevelBorder(SoftBevelBorder.LOWERED)),getBorder()));
				if (lc==null)
					lc=new GridBagConstraints();
				lc.fill=GridBagConstraints.VERTICAL;
				lc.weightx=0;
				lc.weighty=1;
				lc.gridwidth=1;
				lc.gridx=1;
				lc.gridy=0;
				add(busy);
				((GridBagLayout) getLayout()).setConstraints(busy,lc);
			}
			if (b && busyIcon==null)
				busyIcon=new ImageIcon(StatusBar.class.getResource("images/orangeball.gif"));
			else if (!b && notBusyIcon==null)
				notBusyIcon=new ImageIcon(StatusBar.class.getResource("images/whiteball.gif"));

			busy.setIcon((b?busyIcon:notBusyIcon));
			//The paintImmediately call sometimes scrambles the graphics.
			//Avoid it when possible. Finishing work doesn't need it.
			if (b && getParent().isValid())
				busy.paintImmediately(busy.getVisibleRect());
			else
				busy.repaint();
		}
		this.busyB=b;
	}
	boolean isBusy() {
		return busyB;
	}
	/**
	 * @sec When zero, no timer is started.
	 */
	public void setMessage(String mes,int sec) { //changed to public for outsiders to set messages (N)
		if (message==null) {
			message=new MessageLabel(" ");
			if (scale!=null)
				message.setBorder(scale.getBorder());
			else if (busy!=null)
				message.setBorder(busy.getBorder());
			else
				message.setBorder(new CompoundBorder(new CompoundBorder(getBorder(),new OneLineBevelBorder(SoftBevelBorder.LOWERED)),getBorder()));
			if (scale!=null)
				message.setFont(scale.getFont());
			else
				message.setFont(new Font("Helvetica",Font.PLAIN,11));
			message.setForeground(getForeground());
			if (lc==null)
				lc=new GridBagConstraints();
			lc.fill=GridBagConstraints.BOTH;
			lc.weightx=1;
			lc.weighty=1;
			lc.gridwidth=1;
			lc.gridx=0;
			lc.gridy=0;
			add(message);
			((GridBagLayout) getLayout()).setConstraints(message,lc);
		}
		if (sec!=0) {
			if (messageTimer==null)
				messageTimer=new Timer(150,message);
			if (messageTimer.isRunning())
				messageTimer.stop();
			messageTimer.setInitialDelay(sec*1000);
			messageTimer.start();
		}
		message.setText(mes);
		message.setToolTipText(mes);
		invalidate();
		doLayout();
		message.repaint();
	}
	/**
	 * Clears the message from the statusbar.
	 */
	void clearMessage() {
		if (message!=null) {
			message.setText(" ");
			message.setToolTipText(null);
			if (messageTimer.isRunning())
				messageTimer.stop();
			invalidate();
			validate();
		}
	}
	/**
	 * Sets the foreground color to all the contained components.
	 */
	public void setForeground(Color color) {
		super.setForeground(color);
		//Null only in the initialization
		if (message!=null)
			message.setForeground(color);
		if (scale!=null)
			scale.setForeground(color);
		repaint();
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
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		setOpaque(ht.get("opaque",isOpaque()));
		setVisible(ht.get("visible",isVisible()));
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("opaque",isOpaque());
		ht.put("visible",isVisible());
		out.writeObject(ht);
		out.flush();
	}


/////////////////////////////////////////////////////////////////////

	class MessageLabel extends JLabel implements ActionListener {
		MessageLabel(String s) {
			super(s);
		}
		MessageLabel(String s,ImageIcon ii,int i) {
			super(s,ii,i);
		}
		/**
		 * Overriden to show the tooltip in the panel font.
		 */
		public JToolTip createToolTip() {
			JToolTip t = super.createToolTip();
			t.setFont(getFont());
			return t;
		}
		public void actionPerformed(ActionEvent e) {
			StatusBar.this.clearMessage();
		}
	}

	private JLabel scale,busy;
	private MessageLabel message;
	private Timer messageTimer;
	private ImageIcon busyIcon,notBusyIcon;
	private GridBagConstraints lc;
	private MapViewer mapViewer;

	private boolean busyB;
	private boolean busyIconVisible=true;

	static final int NORTH=0;
	static final int SOUTH=1;
	//Externalization
	static final long serialVersionUID=3000L;
}