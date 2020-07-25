package gr.cti.eslate.imageEditor;

import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageDialog extends JDialog {
	JButton okButton;

	// protected ResourceBundle infoBundle = ResourceBundle.getBundle("gr.cti.eslate.iconEditor.InfoBundle",
	// Locale.getDefault());

	public ImageDialog(NewRestorableImageIcon img) {
		// setTitle(infoBundle.getString("imgprev"));
		setModal(true);
		JPanel mainPanel=new JPanel();
		JPanel imgPanel=new JPanel();
		JPanel okPanel=new JPanel();

		JLabel imgLabel=new JLabel(img);
		okButton=new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		imgPanel.setLayout(new BoxLayout(imgPanel,BoxLayout.X_AXIS));
		imgPanel.add(Box.createHorizontalGlue());
		imgPanel.add(imgLabel);
		imgPanel.add(Box.createHorizontalGlue());

		okPanel.setLayout(new BoxLayout(okPanel,BoxLayout.X_AXIS));
		okPanel.add(Box.createHorizontalGlue());
		okPanel.add(okButton);
		okPanel.add(Box.createHorizontalGlue());

		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(imgPanel);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(okPanel);
		mainPanel.add(Box.createVerticalGlue());
		getContentPane().add(mainPanel);
	}

}