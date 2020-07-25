package gr.cti.eslate.imageEditor;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Applet to host Image Editor (Panel)
public class ImageEditorApplet extends JApplet {
	ImageEditor imEditor=new ImageEditor();

	ImageEditorDialog iconDialog;// = new ImageEditorDialog();

	JPanel mainPanel;

	JButton ok;

	Frame topFrame;

	public ImageEditorApplet() {

		System.out.println("here");
		ok=new JButton("ok");
		// imEditor.getESlateHandle();
		topFrame=(Frame) SwingUtilities.getAncestorOfClass(ImageEditorApplet.class,this);

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iconDialog=new ImageEditorDialog(topFrame,32,32,ImageEditorDialog.TRANSPARENT_COLOR);
				// iconDialog = new ImageEditorDialog(32,32, Color.green);
				try {
					// iconDialog.setImage(iconDialog.getImageEditor().loadImageIcon("Images/saveIconPalette.gif"," "));
					iconDialog.setImage(null);
				} catch (Exception eg) {
					eg.printStackTrace();
				}
				;

				iconDialog.showDialog(mainPanel);
			}
		});

		mainPanel=new JPanel();
		mainPanel.setLayout(new BorderLayout());
		// mainPanel.add(ok);
		mainPanel.add(BorderLayout.CENTER,imEditor);
		getContentPane().add(mainPanel);
	}
}