package gr.cti.eslate.mapViewer;

import gr.cti.eslate.database.engine.ImageTableField;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.imageEditor.ImageEditorDialog;
import gr.cti.eslate.mapModel.PointLayer;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IPointLayerView;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class DialogObject extends JDialog {
	private SmartLayout smartLayout1 = new SmartLayout();
	private JLabel lblDefLayer = new JLabel();
	private JComboBox cmxLayer = new JComboBox();
	private JButton btnNewLayer = new JButton();
	private JLabel lblDefIcon = new JLabel();
	private JLabel lblIcon = new JLabel();
	private JButton btnDefIcon = new JButton();
	private JCheckBox chxAlways = new JCheckBox();
	private JLabel lblInfo = new JLabel();
	private JButton btnOK = new JButton();
	private JButton btnCancel = new JButton();
	protected static int OK_PRESSED=1;
	protected static int CANCEL_PRESSED=0;
	private IMapView map;
	private ILayerView[] layers;
	private int returnValue;
	private NewRestorableImageIcon icon;
	private JButton btnClear = new JButton();
	private Thread startIE;
	private ImageEditorDialog ie;

	DialogObject(final Frame owner,IMapView mp,ILayerView[] layers,ILayerView defaultLayer,NewRestorableImageIcon ic,boolean showCheck) {
		super(owner);
		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		setSize(520,220);
		this.map=mp;
		this.layers=layers;
		returnValue=CANCEL_PRESSED;
		setResizable(true);
		setModal(true);
		lblDefLayer.setText(MapViewer.messagesBundle.getString("chooselayer"));
		cmxLayer.setEditable(false);
		cmxLayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmxLayer.getSelectedIndex()==-1) {
					iconWidgetsEnable(false);
					return;
				}
				IPointLayerView lv=(IPointLayerView) cmxLayer.getSelectedItem();
				if (lv.getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS && lv.getIconBase()!=null  && (lv.getIconBase().isHidden() || lv.getIconBase().getDataType()==ImageTableField.DATA_TYPE))
					iconWidgetsEnable(true);
				else
					iconWidgetsEnable(false);
			}
		});
		chxAlways.setText(MapViewer.messagesBundle.getString("alwaysinsert"));
		chxAlways.setSelected(false);
		lblInfo.setFont(new Font(lblInfo.getFont().getName(),lblInfo.getFont().getStyle(),lblInfo.getFont().getSize()-2));
		lblInfo.setText(MapViewer.messagesBundle.getString("alwaysinsertinfo"));
		if (!showCheck) {
			chxAlways.setEnabled(false);
			lblInfo.setEnabled(false);
		}
		if (ic!=null)
			setIcon(ic);

		btnOK.setText(MapViewer.messagesBundle.getString("ok"));
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmxLayer.getSelectedItem()==null) {
					JOptionPane.showMessageDialog(DialogObject.this,MapViewer.messagesBundle.getString("promptnewlayer"),"",JOptionPane.ERROR_MESSAGE);
					return;
				}
				returnValue=OK_PRESSED;
				setVisible(false);
			}
		});

		btnCancel.setText(MapViewer.messagesBundle.getString("cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnValue=CANCEL_PRESSED;
				setVisible(false);
			}
		});

		lblDefIcon.setText(MapViewer.messagesBundle.getString("defineicon"));
		btnDefIcon.setToolTipText(MapViewer.messagesBundle.getString("newicon"));
		btnDefIcon.setFocusPainted(false);
		btnDefIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDefIcon.setEnabled(false);
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				if (ie==null) {
					try {
						startIE.setPriority(Thread.MAX_PRIORITY-1);
					} catch(Throwable t) {
						//Probably the thread stopped.
					}
					while (ie==null)
						;//Do nothing
				}
				if (getIcon()!=null)
					//try {
						//cahnge due to imageEditor changes//ie.setImage(new ImageIcon(((NewRestorableImageIcon) getIcon()).getImage()));
						ie.setImage(((NewRestorableImageIcon) getIcon()).getImage());
					//} catch(Exception ex) {
					//    ex.printStackTrace();
					//}
				ie.show();
				btnDefIcon.setEnabled(true);
				if (ie.getReturnCode()==ImageEditorDialog.IMAGE_EDITOR_OK) {
					setIcon(new NewRestorableImageIcon(ie.getImage()));
					((JComponent) getContentPane()).revalidate();
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		btnNewLayer.setText(MapViewer.messagesBundle.getString("newlayer"));
		btnNewLayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				DialogCreateNewLayer nl=new DialogCreateNewLayer((Frame) getOwner());
				java.awt.Point pp=new java.awt.Point(getX(),getY());
				nl.setLocation(pp.x+(getWidth()-nl.getWidth())/2,pp.y+(getHeight()-nl.getHeight())/2);
				nl.setVisible(true);
				if (!nl.getLayerName().equals("")) {
					PointLayer pl=new PointLayer(nl.getLayerName());
					//Add a table to the database, if a database exists
					if (map.getDatabase()!=null) {
						Table table=new Table(nl.getLayerName());
						try {
							table.addField(PointLayer.ICON_ID,String.class);
							table.addField(MapViewer.messagesBundle.getString("field"),String.class);
							table.setFieldHidden(PointLayer.ICON_ID,true);
							map.getDatabase().addTable(table,true);
							pl.setTable(table);
							pl.setIconBase(table.getTableField(PointLayer.ICON_ID));
							pl.setTipBase(table.getTableField(MapViewer.messagesBundle.getString("field")));
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}
					pl.setEditable(true);
					pl.setPaintMode(IPointLayerView.PAINT_AS_MULTIPLE_ICONS);
					Iterator it=map.getRegionViews();
					IRegionView rv; ILayerView added;
					while (it.hasNext()) {
						rv=(IRegionView) it.next();
						added=rv.addLayer(pl);
						if (rv.equals(map.getActiveRegionView())) {
							added.setVisible(true);
							cmxLayer.addItem(added);
							cmxLayer.setSelectedItem(added);
							cmxLayer.repaint();
						} else if (nl.isShowInAllSelected()) {
							added.setVisible(true);
						} else {
							added.setVisible(false);
						}
					}
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		//
		btnClear.setToolTipText(MapViewer.messagesBundle.getString("clearicon"));
		btnClear.setFocusPainted(false);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setIcon(null);
			}
		});

		//Fill the combo box. Currently only with point layers.
		cmxLayer.setRenderer(new ComboRenderer());
		for (int i=0;i<layers.length;i++)
			if ((layers[i].isEditable()) && (layers[i] instanceof IPointLayerView))
				cmxLayer.addItem(layers[i]);
		if (defaultLayer!=null)
			cmxLayer.setSelectedItem(defaultLayer);

		//By default prompt for a new layer when there is no candidate layer.
		if (cmxLayer.getItemCount()==0)
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					btnNewLayer.doClick();
				}
			});

		//This thread starts the ImageEditor to save time when clicking the define button.
		//The thread starts with normal priority but if IE is needed it turns to MAX.
		startIE=new Thread() {
			public void run() {
				ie=new ImageEditorDialog(owner,20,20);
			}
		};
		startIE.start();
	}

	protected int getReturnValue() {
		return returnValue;
	}

	protected boolean isDefaultDefined() {
		return chxAlways.isSelected();
	}

	protected ILayerView getInsertLayer() {
		return (ILayerView) cmxLayer.getSelectedItem();
	}

	protected void setIcon(NewRestorableImageIcon ic) {
		icon=ic;
		lblIcon.setIcon(ic);
		if (ic!=null)
			btnClear.setEnabled(true);
		else
			btnClear.setEnabled(false);
	}

	protected NewRestorableImageIcon getIcon() {
		return icon;
	}

	private void iconWidgetsEnable(boolean b) {
		lblDefIcon.setEnabled(b);
		lblIcon.setEnabled(b);
		btnDefIcon.setEnabled(b);
		if (getIcon()!=null)
			btnClear.setEnabled(true);
		else
			btnClear.setEnabled(false);
	}
	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		for (int i=0;i<getComponentCount();i++)
			((Component) getComponents()[i]).setFont(f);
	}

	private void jbInit() throws Exception {
		lblDefLayer.setText("Give the layer for the new object");
		this.getContentPane().setLayout(smartLayout1);
		btnNewLayer.setText("New");
		lblDefIcon.setText("Define the icon that will be used");
		lblIcon.setBackground(Color.white);
		lblIcon.setBorder(BorderFactory.createEtchedBorder());
		lblIcon.setOpaque(true);
		btnDefIcon.setIcon(new ImageIcon(gr.cti.eslate.mapViewer.DialogObject.class.getResource("images/iconEditor.gif")));
		btnDefIcon.setMargin(new Insets(0, 0, 0, 0));
		chxAlways.setText("Always use these settings");
		lblInfo.setText("(Click right mouse button to change)");
		btnOK.setText("OK");
		btnCancel.setText("Cancel");
		btnClear.setIcon(new ImageIcon(gr.cti.eslate.mapViewer.DialogObject.class.getResource("images/clear.gif")));
		btnClear.setMargin(new Insets(0, 0, 0, 0));
		this.getContentPane().add(lblDefLayer, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(cmxLayer, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(btnNewLayer, Anchor.Left, Anchor.Left, Anchor.Right, 3),
					new com.thwt.layout.EdgeAnchor(lblDefLayer, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnNewLayer, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(cmxLayer, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(cmxLayer, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.getContentPane().add(lblDefIcon, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(cmxLayer, Anchor.Bottom, Anchor.Below, Anchor.Top, 6),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblIcon, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblDefIcon, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnDefIcon, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblIcon, Anchor.Left, Anchor.Left, Anchor.Right, 10),
					new com.thwt.layout.EdgeAnchor(btnNewLayer, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(lblIcon, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.getContentPane().add(chxAlways, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnDefIcon, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblInfo, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(chxAlways, Anchor.Bottom, Anchor.Below, Anchor.Top, -2),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnOK, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -15),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnCancel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 15),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnClear, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblIcon, Anchor.Right, Anchor.Right, Anchor.Left, 10),
					new com.thwt.layout.EdgeAnchor(btnDefIcon, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(lblIcon, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
	}
}
