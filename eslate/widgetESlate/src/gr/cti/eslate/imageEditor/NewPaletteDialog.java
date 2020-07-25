package gr.cti.eslate.imageEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class NewPaletteDialog extends JDialog {
	JButton okButton;

	Font dialogFont;

	JButton cancelButton;

	protected ResourceBundle iEBundle=ResourceBundle.getBundle("gr.cti.eslate.imageEditor.IEBundle",Locale.getDefault());

	boolean ok;

	JTextField field1, field2;

	public NewPaletteDialog(Frame owner,String title,int numberOfFields) {
		super(owner);
		dialogFont=new Font("dialog",Font.PLAIN,11);

		UIManager.put("ToolTip.font",dialogFont);
		UIManager.put("Panel.font",dialogFont);

		// dialog's properties
		Dimension dialogDim=new Dimension(0,0);
		if (numberOfFields == 2)
			dialogDim=new Dimension(210,150);
		if (numberOfFields == 1)
			dialogDim=new Dimension(210,110);
		setSize(dialogDim);
		setResizable(false);
		setTitle(title);
		setModal(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

		// button panel
		JPanel buttonPanel=new JPanel();
		okButton=new JButton(iEBundle.getString("ok"));
		cancelButton=new JButton(iEBundle.getString("cancel"));
		okButton.setFont(dialogFont);
		cancelButton.setFont(dialogFont);
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(okButton);
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(cancelButton);
		buttonPanel.add(Box.createHorizontalGlue());

		// fields Panel
		JPanel fieldsPanel=new JPanel();
		field1=new JTextField();
		field2=new JTextField();
		field1.setColumns(3);
		field2.setColumns(3);
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel,BoxLayout.Y_AXIS));
		JPanel field1Panel=new JPanel();
		field1Panel.setLayout(new BoxLayout(field1Panel,BoxLayout.X_AXIS));

		JPanel field2Panel=new JPanel();
		field2Panel.setLayout(new BoxLayout(field2Panel,BoxLayout.X_AXIS));

		Dimension labelDim=new Dimension(180,30);
		JLabel l1;
		if (numberOfFields == 2)
			l1=new JLabel(iEBundle.getString("totalColors"));
		else
			l1=new JLabel(iEBundle.getString("totalIcons"));

		l1.setFont(dialogFont);
		l1.setPreferredSize(labelDim);
		l1.setMinimumSize(labelDim);
		l1.setMaximumSize(labelDim);
		JLabel l2=new JLabel(iEBundle.getString("customColors"));
		l2.setFont(dialogFont);
		l2.setPreferredSize(labelDim);
		l2.setMinimumSize(labelDim);
		l2.setMaximumSize(labelDim);

		field1Panel.add(l1);
		field1Panel.add(field1);
		field1Panel.add(Box.createHorizontalGlue());

		field2Panel.add(Box.createHorizontalGlue());
		field2Panel.add(l2);
		field2Panel.add(field2);
		field2Panel.add(Box.createHorizontalGlue());

		fieldsPanel.add(field1Panel);
		if (numberOfFields == 2)
			fieldsPanel.add(field2Panel);

		Insets borderInset=new Insets(8,8,8,5);
		buttonPanel.setBorder(new EmptyBorder(borderInset));

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH,fieldsPanel);
		getContentPane().add(BorderLayout.SOUTH,buttonPanel);

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okOperation();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelOperation();
			}
		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				ok=false;
				setVisible(false);
			}
		});
	}

	public void showDialog(Component centerAround) {
		Dimension screenSize=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x, y;
		if (centerAround == null || !centerAround.isVisible()) {
			x=(screenSize.width / 2) - (getSize().width / 2);
			y=(screenSize.height / 2) - (getSize().height / 2);
		} else {
			Rectangle compBounds=centerAround.getBounds();
			java.awt.Point compLocation=centerAround.getLocationOnScreen();
			x=compLocation.x + compBounds.width / 2 - getSize().width / 2;
			y=compLocation.y + compBounds.height / 2 - getSize().height / 2;
			if (x + getSize().width > screenSize.width)
				x=screenSize.width - getSize().width;
			if (y + getSize().height > screenSize.height)
				y=screenSize.height - getSize().height;
			if (x < 0)
				x=0;
			if (y < 0)
				y=0;
		}
		setLocation(x,y);
		setVisible(true);
	}

	public int getNumberOfElements() {
		if (!ok)
			return -1;
		int value;
		try {
			String numberOfElements=field1.getText();
			value=Integer.valueOf(numberOfElements).intValue();
		} catch (Exception e) {
			return -1;
		}
		return value;
	}

	public int getNumberOfCustomElements() {
		if (!ok)
			return -1;
		int value;
		try {
			String numberOfCustomElements=field2.getText();
			value=Integer.valueOf(numberOfCustomElements).intValue();
		} catch (Exception e) {
			return -1;
		}
		return value;
	}

	private void okOperation() {
		ok=true;
		setVisible(false);
	}

	private void cancelOperation() {
		ok=false;
		setVisible(false);
	}

	protected void processKeyEvent(KeyEvent keyevent) {
		if (keyevent.getKeyCode() == KeyEvent.VK_ENTER) {
			if (cancelButton.hasFocus())
				cancelOperation();
			else
				okOperation();
		}
		if (keyevent.getKeyCode() == KeyEvent.VK_TAB) {
			transferFocus();
		}
		if (keyevent.getKeyCode() == KeyEvent.VK_ESCAPE) {
			ok=false;
			setVisible(false);
		}
	}

}