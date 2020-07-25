package gr.cti.eslate.imageEditor;

import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ImageEditorDialog extends JDialog {
	ImageEditor iEditor;

	private Image returnImage;

	private boolean returnCode;

	public final static boolean IMAGE_EDITOR_OK=true;

	public final static boolean IMAGE_EDITOR_CANCEL=false;

	private final static int BUTTON_WIDTH=25;

	protected ResourceBundle iEBundle=ResourceBundle.getBundle("gr.cti.eslate.imageEditor.IEBundle",Locale.getDefault());

	public final static Color TRANSPARENT_COLOR=new Color(255,255,255,0);

	// constructor 1
	public ImageEditorDialog(Frame owner) {
		super(owner);
		iEditor=new ImageEditor();
		// Dialog's properties
		Dimension iEditorSize=iEditor.getPreferredSize();
		setSize(iEditorSize.width,iEditorSize.height + 3 * BUTTON_WIDTH);
		setModal(true);
		setTitle(iEBundle.getString("componame"));
		// Dialog's layout
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(iEditor);

		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.cancelButton);
		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.okButton);

		iEditor.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
		iEditor.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		});

		// ESCAPE HANDLER
		/*
		 * this.getRootPane().registerKeyboardAction(new ActionListener() { public void actionPerformed(ActionEvent e) {
		 * System.out.println("here1"); javax.swing.ButtonModel bm = iconEditor.cancelButton.getModel();
		 * bm.setArmed(true); bm.setPressed(true); } },
		 * javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
		 * javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
		 */
		this.getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iEditor.cancelButton.doClick();
				javax.swing.ButtonModel bm=iEditor.cancelButton.getModel();
				bm.setPressed(false);
			}
		},javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE,0,true),javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

		this.getRootPane().setDefaultButton(iEditor.okButton);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitAction();
			}
		});

	} // * end of constructor

	// constructor 2
	public ImageEditorDialog(Frame owner,int xDim,int yDim,Color backgr) {
		super(owner);
		iEditor=new ImageEditor(xDim,yDim,backgr);
		// Dialog's properties
		Dimension iEditorSize=iEditor.getPreferredSize();
		setSize(iEditorSize.width,iEditorSize.height + 3 * BUTTON_WIDTH);
		setModal(true);
		setTitle(iEBundle.getString("componame"));
		// Dialog's layout
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(iEditor);

		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.cancelButton);
		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.okButton);

		iEditor.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnCode=IMAGE_EDITOR_CANCEL;
				returnImage=null;
				dispose();
			}
		});
		iEditor.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// returnCode = IMAGE_EDITOR_OK;
				// returnImage = iEditor.canvas.getImage();
				// dispose();
				okAction();
			}
		});

		this.getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iEditor.cancelButton.doClick();
				javax.swing.ButtonModel bm=iEditor.cancelButton.getModel();
				bm.setPressed(false);
			}
		},javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE,0,true),javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

		this.getRootPane().setDefaultButton(iEditor.okButton);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitAction();
			}
		});
	} // * end of constructor

	// constructor 3
	public ImageEditorDialog(Frame owner,int xDim,int yDim) {
		super(owner);
		iEditor=new ImageEditor(xDim,yDim);
		// Dialog's properties
		Dimension iEditorSize=iEditor.getPreferredSize();
		setSize(iEditorSize.width,iEditorSize.height + 3 * BUTTON_WIDTH);
		setModal(true);
		setTitle(iEBundle.getString("componame"));
		// Dialog's layout
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(iEditor);

		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.cancelButton);
		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.okButton);

		iEditor.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnCode=IMAGE_EDITOR_CANCEL;
				returnImage=null;
				dispose();
			}
		});
		iEditor.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// returnCode = IMAGE_EDITOR_OK;
				// returnImage = iEditor.canvas.getImage();
				// dispose();
				okAction();
			}
		});

		this.getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iEditor.cancelButton.doClick();
				javax.swing.ButtonModel bm=iEditor.cancelButton.getModel();
				bm.setPressed(false);
			}
		},javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE,0,true),javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

		this.getRootPane().setDefaultButton(iEditor.okButton);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitAction();
			}
		});
	} // * end of constructor 3

	// //////////

	// constructor 4 using Dialog in constructor
	public ImageEditorDialog(Dialog owner) {
		super(owner);
		iEditor=new ImageEditor();
		// Dialog's properties
		Dimension iEditorSize=iEditor.getPreferredSize();
		setSize(iEditorSize.width,iEditorSize.height + 3 * BUTTON_WIDTH);
		setModal(true);
		setTitle(iEBundle.getString("componame"));
		// Dialog's layout
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(iEditor);

		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.cancelButton);
		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.okButton);

		iEditor.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
		iEditor.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		});

		// ESCAPE HANDLER
		/*
		 * this.getRootPane().registerKeyboardAction(new ActionListener() { public void actionPerformed(ActionEvent e) {
		 * System.out.println("here1"); javax.swing.ButtonModel bm = iconEditor.cancelButton.getModel();
		 * bm.setArmed(true); bm.setPressed(true); } },
		 * javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
		 * javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
		 */
		this.getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iEditor.cancelButton.doClick();
				javax.swing.ButtonModel bm=iEditor.cancelButton.getModel();
				bm.setPressed(false);
			}
		},javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE,0,true),javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

		this.getRootPane().setDefaultButton(iEditor.okButton);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitAction();
			}
		});

	} // * end of constructor 4

	// constructor 5
	public ImageEditorDialog(Dialog owner,int xDim,int yDim,Color backgr) {
		super(owner);
		iEditor=new ImageEditor(xDim,yDim,backgr);
		// Dialog's properties
		Dimension iEditorSize=iEditor.getPreferredSize();
		setSize(iEditorSize.width,iEditorSize.height + 3 * BUTTON_WIDTH);
		setModal(true);
		setTitle(iEBundle.getString("componame"));
		// Dialog's layout
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(iEditor);

		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.cancelButton);
		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.okButton);

		iEditor.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnCode=IMAGE_EDITOR_CANCEL;
				returnImage=null;
				dispose();
			}
		});
		iEditor.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// returnCode = IMAGE_EDITOR_OK;
				// returnImage = iEditor.canvas.getImage();
				// dispose();
				okAction();
			}
		});

		this.getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iEditor.cancelButton.doClick();
				javax.swing.ButtonModel bm=iEditor.cancelButton.getModel();
				bm.setPressed(false);
			}
		},javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE,0,true),javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

		this.getRootPane().setDefaultButton(iEditor.okButton);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitAction();
			}
		});
	} // * end of constructor 5

	// constructor 6
	public ImageEditorDialog(Dialog owner,int xDim,int yDim) {
		super(owner);
		iEditor=new ImageEditor(xDim,yDim);
		// Dialog's properties
		Dimension iEditorSize=iEditor.getPreferredSize();
		setSize(iEditorSize.width,iEditorSize.height + 3 * BUTTON_WIDTH);
		setModal(true);
		setTitle(iEBundle.getString("componame"));
		// Dialog's layout
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(iEditor);

		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.cancelButton);
		iEditor.rightPanel.add(Box.createVerticalStrut(2));
		iEditor.rightPanel.add(iEditor.okButton);

		iEditor.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnCode=IMAGE_EDITOR_CANCEL;
				returnImage=null;
				dispose();
			}
		});
		iEditor.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// returnCode = IMAGE_EDITOR_OK;
				// returnImage = iEditor.canvas.getImage();
				// dispose();
				okAction();
			}
		});

		this.getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iEditor.cancelButton.doClick();
				javax.swing.ButtonModel bm=iEditor.cancelButton.getModel();
				bm.setPressed(false);
			}
		},javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE,0,true),javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

		this.getRootPane().setDefaultButton(iEditor.okButton);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitAction();
			}
		});
	} // * end of constructor 6

	// /////////
	private void exitAction() {
		if (iEditor.iconPalette.isPaletteModified) {
			int questionPalette=JOptionPane.showConfirmDialog(null,iEBundle.getString("wannaSavePalette"),iEBundle.getString("alertPalette"),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (questionPalette == JOptionPane.YES_OPTION) {
				iEditor.iconPalette.save();
			} else if (questionPalette == JOptionPane.NO_OPTION) {

			}
		}
		if (iEditor.colorPalette.isPaletteModified) {
			int questionColorPalette=JOptionPane.showConfirmDialog(null,iEBundle.getString("wannaSaveColorPalette"),iEBundle.getString("alertColorPalette"),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (questionColorPalette == JOptionPane.YES_OPTION) {
				iEditor.colorPalette.save();
			} else if (questionColorPalette == JOptionPane.NO_OPTION) {

			}
		}
		if (!iEditor.imageModifiedForDialog)
			return;
		int question=JOptionPane.showConfirmDialog(null,iEBundle.getString("saving"),iEBundle.getString("alert"),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		if (question == JOptionPane.YES_OPTION) {
			okAction();
		} else if (question == JOptionPane.NO_OPTION) {
			cancelAction();
		}
	}

	private void okAction() {
		if (iEditor.iconPalette.isPaletteModified) {
			int questionPalette=JOptionPane.showConfirmDialog(null,iEBundle.getString("wannaSavePalette"),iEBundle.getString("alertPalette"),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (questionPalette == JOptionPane.YES_OPTION) {
				iEditor.iconPalette.save();
			} else if (questionPalette == JOptionPane.NO_OPTION) {

			}
		}
		returnCode=IMAGE_EDITOR_OK;
		returnImage=iEditor.canvas.getImage();
		// iEditor.canvas.deselectAll();
		iEditor.imageModifiedForDialog=false;
		dispose();
	}

	private void cancelAction() {
		returnCode=IMAGE_EDITOR_CANCEL;
		returnImage=null;
		iEditor.canvas.deselectAll();
		dispose();
	}

	// shows the iconEditorDialog around the given component
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

	// returns the image of the image editor
	// public ImageIcon getImage() {
	/*
	 * public ImageIcon getImage() { if (returnImage == null) return null; else return new ImageIcon(returnImage);
	 * //return new NewRestorableImageIcon(returnImage); }
	 */

	public Image getImage() {
		return returnImage;
	}

	public NewRestorableImageIcon getIcon() {
		if (returnImage == null)
			return null;
		else
			return new NewRestorableImageIcon(returnImage);
	}

	// sets the icon of the icon editor
	// public void setImage(ImageIcon im){
	/*
	 * public void setImage(ImageIcon im){ if (im == null){ iEditor.clearImage(); return; }
	 * 
	 * Image img = im.getImage(); int img_width = img.getWidth(null); int img_height = img.getHeight(null);
	 * iEditor.openImage(img,false); }
	 */

	public void setImage(Image img) {
		if (img == null) {
			iEditor.clearImage();
			return;
		}
		iEditor.openImage(img,false);
	}

	public void setIcon(Icon ico) {
		if (ico == null) {
			iEditor.clearImage();
			return;
		}
		BufferedImage img=new BufferedImage(ico.getIconWidth(),ico.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		ico.paintIcon(ImageEditorDialog.this,img.getGraphics(),0,0);
		iEditor.openImage(img,false);
	}

	// returns the image editor object
	public ImageEditor getImageEditor() {
		return iEditor;
	}

	public void clear() {
		iEditor.clearImage();
	}

	// returns whether cancel or ok is pressed
	public boolean getReturnCode() {
		return returnCode;
	}
	
}
