package gr.cti.eslate.agent;

import gr.cti.eslate.imageEditor.ImageEditorDialog;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class AgentFaceWizardStep31 extends JPanel {

	AgentFaceWizardStep31(Agent a) {
		try  {
			jbInit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		this.agent=a;

		//Initialization
		Color tr=new Color(0,0,0,0);
		txtDesc.setSelectedTextColor(txtDesc.getForeground());
		txtDesc.setSelectionColor(tr);
		txtDesc2.setSelectedTextColor(txtDesc.getForeground());
		txtDesc2.setSelectionColor(tr);

		//Localization
		txtDesc.setText(AgentBeanInfo.bundle.getString("step3desc"));
		txtDesc2.setText(AgentBeanInfo.bundle.getString("step3desc2"));
		btnDefine.setText(AgentBeanInfo.bundle.getString("step3define"));

		//Preset the base image, if it exists.
		Dimension fs=agent.getFaceSize(0);
		if (fs.width!=0 && fs.height!=0) {
			BufferedImage fim=new BufferedImage(fs.width,fs.height,BufferedImage.TYPE_INT_ARGB);
			agent.paintFace(fim.getGraphics(),0); //0 degrees
			lblBaseImage.setIcon(new ImageIcon(fim));
		}

		btnDefine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				ImageEditorDialog ie=new ImageEditorDialog((JFrame) SwingUtilities.getAncestorOfClass(JFrame.class,AgentFaceWizardStep31.this),32,32);
				ie.setLocation(Math.max(0,getX()+(getWidth()-ie.getWidth())/2),Math.max(0,getY()+(getHeight()-ie.getHeight())/2));
				if (lblBaseImage.getIcon()!=null)
					//try {
						ie.setIcon((ImageIcon) lblBaseImage.getIcon());
					//} catch(gr.cti.eslate.iconEditor.IconTooBigException ex) {}
				ie.setVisible(true);
				if (ie.getReturnCode()==ImageEditorDialog.IMAGE_EDITOR_OK)
					lblBaseImage.setIcon(ie.getIcon());
				getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}

	protected Icon getIcon() {
		return lblBaseImage.getIcon();
	}

	private void jbInit() throws Exception {
		txtDesc.setLineWrap(true);
		txtDesc.setWrapStyleWord(true);
		txtDesc.setOpaque(false);
		txtDesc.setText("jTextArea1");
		this.setLayout(smartLayout1);
		lblBaseImage.setBackground(Color.white);
		lblBaseImage.setBorder(BorderFactory.createLoweredBevelBorder());
		lblBaseImage.setOpaque(true);
		lblBaseImage.setHorizontalAlignment(SwingConstants.CENTER);
		btnDefine.setText("Define");
		txtDesc2.setLineWrap(true);
		txtDesc2.setWrapStyleWord(true);
		txtDesc2.setOpaque(false);
		txtDesc2.setText("jTextArea1");
		this.add(txtDesc, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(lblBaseImage, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnDefine, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 20, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 20, 1.0)));
		this.add(btnDefine, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(txtDesc, Anchor.Bottom, Anchor.Below, Anchor.Top, 30),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 30, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -6, 1.0)));
		this.add(txtDesc2, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.EdgeAnchor(lblBaseImage, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
	}

	private Agent agent;
	//UI Members
	private SmartLayout smartLayout1 = new SmartLayout();
	private LargeFontJTextArea txtDesc = new LargeFontJTextArea();
	private JLabel lblBaseImage = new JLabel();
	private JButton btnDefine = new JButton();
	private LargeFontJTextArea txtDesc2 = new LargeFontJTextArea();

}
