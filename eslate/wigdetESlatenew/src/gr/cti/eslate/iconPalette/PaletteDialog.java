package gr.cti.eslate.iconPalette;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PaletteDialog extends JDialog {
	String functionality;

	boolean addToPalette;

	JCheckBox checkBoxAdd;

	Font dialogFont;

	protected ResourceBundle infoBundle=ResourceBundle.getBundle("gr.cti.eslate.iconEditor.InfoBundle",Locale.getDefault());

	public PaletteDialog(Point location) {

		dialogFont=new Font("dialog",Font.PLAIN,12);

		// dialog properties
		setTitle(infoBundle.getString("alert"));
		setLocation(location.x,location.y);
		setSize(new Dimension(300,150));
		setResizable(false);
		setModal(true);

		// *** dialog panels and layout
		// The message Panel
		JPanel messagePanel=new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel,BoxLayout.X_AXIS));

		JLabel messageLabel=new JLabel(infoBundle.getString("saveThisPalette?"),loadImageIcon("Images/question.gif"," "),JLabel.LEFT);
		messagePanel.add(Box.createHorizontalStrut(10));
		messagePanel.add(messageLabel);
		messagePanel.add(Box.createHorizontalGlue());

		// The button Panel
		JPanel buttonPanel=new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		// The buttons of the dialog --> YES, NO, CANCEL
		JButton yesButton=new JButton(infoBundle.getString("yes"));
		JButton noButton=new JButton(infoBundle.getString("no"));
		JButton cancelButton=new JButton(infoBundle.getString("Cancel"));
		Dimension buttonSize=new Dimension(40,25);
		Dimension cancelSize=new Dimension(50,25);

		yesButton.setFont(dialogFont);
		noButton.setFont(dialogFont);
		cancelButton.setFont(dialogFont);
		yesButton.setPreferredSize(buttonSize);
		noButton.setPreferredSize(buttonSize);
		cancelButton.setPreferredSize(cancelSize);
		yesButton.setMaximumSize(buttonSize);
		noButton.setMaximumSize(buttonSize);
		cancelButton.setMaximumSize(cancelSize);
		yesButton.setMargin(new Insets(1,1,1,1));
		noButton.setMargin(new Insets(1,1,1,1));
		cancelButton.setMargin(new Insets(1,1,1,1));

		buttonPanel.add(yesButton);
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(noButton);
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(cancelButton);

		// The check box panel
		JPanel checkBoxPanel=new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel,BoxLayout.X_AXIS));
		checkBoxAdd=new JCheckBox(infoBundle.getString("addtopalette"));
		checkBoxPanel.add(Box.createHorizontalStrut(10));
		checkBoxPanel.add(checkBoxAdd);
		checkBoxPanel.add(Box.createHorizontalGlue());

		// The mainPanel
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(messagePanel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(buttonPanel);
		mainPanel.add(checkBoxPanel);
		mainPanel.add(Box.createVerticalStrut(10));

		getContentPane().add(mainPanel);

		// Listeners
		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addToPalette)
					functionality="SaveAndAdd";
				else
					functionality="Save";
				dispose();
			}
		});

		noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addToPalette)
					functionality="DiscartAndAdd";
				else
					functionality="Discart";
				dispose();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				functionality="cancel";
				dispose();
			}
		});

		checkBoxAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkBoxAdd.isSelected())
					addToPalette=true;
				else
					addToPalette=false;
			}
		});

	}

	public ImageIcon loadImageIcon(String filename,String description) {
		try {
			URL u=this.getClass().getResource(filename);
			if (u != null)
				return new ImageIcon(u,description);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}// loadImageIcon

	public String getFunctionality() {
		return functionality;
	}
}