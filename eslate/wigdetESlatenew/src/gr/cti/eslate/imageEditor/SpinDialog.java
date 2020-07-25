package gr.cti.eslate.imageEditor;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
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

import pv.jfcx.JPVSpin;
import pv.jfcx.JPVSpinPlus;

public class SpinDialog extends JDialog {

	SpinBox spinBox1, spinBox2;

	JButton okButton;

	Font dialogFont;

	JButton cancelButton;

	protected ResourceBundle iEBundle=ResourceBundle.getBundle("gr.cti.eslate.imageEditor.IEBundle",Locale.getDefault());

	boolean ok;

	public SpinDialog(Frame owner,int init1,int init2) {
		super(owner);
		spinBox1=new SpinBox(1,init1,0,1024,true);
		spinBox2=new SpinBox(1,init2,0,1024,true);
		dialogFont=new Font("dialog",Font.PLAIN,11);

		UIManager.put("ToolTip.font",dialogFont);
		UIManager.put("Panel.font",dialogFont);

		JPanel northPanel=new JPanel();
		JPanel southPanel=new JPanel();
		northPanel.setLayout(new BorderLayout());
		southPanel.setLayout(new BorderLayout());

		// labels
		JLabel xdim=new JLabel(iEBundle.getString("xdim"));
		JLabel ydim=new JLabel(iEBundle.getString("ydim"));
		xdim.setFont(dialogFont);
		ydim.setFont(dialogFont);
		Dimension spinDimension=new Dimension(70,27);
		Dimension labelsDimension=new Dimension(spinDimension.width + 2,spinDimension.height);
		xdim.setPreferredSize(labelsDimension);
		xdim.setMinimumSize(labelsDimension);
		xdim.setMaximumSize(labelsDimension);
		ydim.setPreferredSize(labelsDimension);
		ydim.setMinimumSize(labelsDimension);
		ydim.setMaximumSize(labelsDimension);

		// dialog's properties
		Dimension dialogDim=new Dimension(3 * spinDimension.width,6 * spinDimension.height - 10);
		setSize(dialogDim);
		setResizable(false);
		setTitle(iEBundle.getString("select"));
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

		Insets borderInset=new Insets(8,8,8,5);
		buttonPanel.setBorder(new EmptyBorder(borderInset));
		northPanel.setBorder(new EmptyBorder(borderInset));
		southPanel.setBorder(new EmptyBorder(borderInset));

		southPanel.add(BorderLayout.WEST,ydim);
		southPanel.add(BorderLayout.CENTER,spinBox2);
		southPanel.add(BorderLayout.SOUTH,buttonPanel);

		northPanel.add(BorderLayout.WEST,xdim);
		northPanel.add(BorderLayout.CENTER,spinBox1);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH,northPanel);
		getContentPane().add(BorderLayout.SOUTH,southPanel);

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

	private void okOperation() {
		spinBox1.testLimit();
		spinBox2.testLimit();
		ok=true;
		setVisible(false);
	}

	private void cancelOperation() {
		ok=false;
		setVisible(false);
	}

	public int getUpValue() {
		return Integer.valueOf(spinBox1.getText()).intValue();
	}

	public int getDownValue() {
		return Integer.valueOf(spinBox2.getText()).intValue();
	}

	public void setUpInitValue(int value) {
		spinBox1.setInitValue(value);
	}

	public void setDownInitValue(int value) {
		spinBox2.setInitValue(value);
	}

	public boolean isOk() {
		return ok;
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

class SpinBox extends JTextField {
	JPVSpinPlus spinControl;

	// JTextField spinField;
	int step;

	boolean editable=true;

	String initValue;

	int beginValue;

	int endValue;

	private String validChars="0123456789";

	int previousValue;

	public SpinBox() {
		super();
		spinControl=new JPVSpinPlus(JPVSpinPlus.VERTICAL_CENTER);
		spinControl.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		setLayout(new BorderLayout());
		add(BorderLayout.EAST,spinControl);

		initValue="1";
		beginValue=1;
		endValue=1000;
		setColumns(4);
		setText(initValue);
		setEditable(editable);
		step=1;
		previousValue=1;

		spinControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getModifiers() == JPVSpin.DECREMENT) {
					String textString=getText();
					int currentText=Integer.valueOf(textString).intValue();
					if (((currentText - step) >= beginValue) && ((currentText - step) <= endValue))
						currentText=currentText - step;
					setText(String.valueOf(currentText));
				} else {
					String textString=getText();
					int currentText=Integer.valueOf(textString).intValue();
					if (((currentText + step) >= beginValue) && ((currentText + step) <= endValue))
						currentText=currentText + step;
					setText(String.valueOf(currentText));
				}
			}
		});
	}

	public SpinBox(int stp) {
		this();
		step=stp;
	}

	public SpinBox(int stp,int initVal,int beginVal,int endVal,boolean edit) {
		this();
		step=stp;
		initValue=String.valueOf(initVal);
		beginValue=beginVal;
		endValue=endVal;
		editable=edit;
		previousValue=initVal;
		setText(initValue);
	}

	public void testLimit() {
		if ((Integer.valueOf(getText()).intValue()) > endValue)
			setText(String.valueOf(endValue));
	}

	public void setStep(int stp) {
		step=stp;
	}

	public void setInitValue(int initVal) {
		initValue=String.valueOf(initVal);
		setText(initValue);
	}

	public void setBeginValue(int beginVal) {
		beginValue=beginVal;
	}

	public void setEndValue(int endVal) {
		endValue=endVal;
	}

	public int getStep() {
		return step;
	}

	public int getInitValue() {
		return Integer.valueOf(initValue).intValue();
	}

	public int getBeginValue() {
		return beginValue;
	}

	public int getEndValue() {
		return endValue;
	}

	public void setValidChars(String s) {
		validChars=s;
	}

	protected void processComponentKeyEvent(KeyEvent keyevent) {
		char c=keyevent.getKeyChar();

		if (getText().equals(""))
			setText(String.valueOf(previousValue));

		if ((getText().length()) > (getColumns()))
			setText(String.valueOf(previousValue));

		if ((Integer.valueOf(getText()).intValue()) < beginValue)
			setText(String.valueOf(beginValue));

		if (!Character.isISOControl(c) && !inString(validChars,c) && keyevent.getKeyCode() != KeyEvent.VK_LEFT && keyevent.getKeyCode() != KeyEvent.VK_RIGHT && keyevent.getKeyCode() != KeyEvent.VK_HOME && keyevent.getKeyCode() != KeyEvent.VK_END) {
			Toolkit.getDefaultToolkit().beep();
			keyevent.consume();
		} else
			super.processComponentKeyEvent(keyevent);

		previousValue=Integer.valueOf(getText()).intValue();
	}

	private boolean inString(String s,char c) {
		for (int i=0;i < s.length();i++)
			if (s.charAt(i) == c)
				return true;

		return false;
	}
}