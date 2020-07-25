package gr.cti.eslate.mapModel;

import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.ImageTableField;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.imageEditor.ImageEditorDialog;
import gr.cti.eslate.protocol.IPointLayerView;
import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class PointLayerProperties extends JDialog {

	PointLayerProperties(final Map map) {
		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		firstTimeShown=true;
		setSize(530,472);
		setModal(true);
		setTitle(MapCreator.bundleCreator.getString("viewproperties"));

		//Localization
		lblView.setText(MapCreator.bundleCreator.getString("pointview"));
		rbtnCircle.setText(MapCreator.bundleCreator.getString("ascircle"));
		rbtnSameIcons.setText(MapCreator.bundleCreator.getString("asicons"));
		cbxFilled.setText(MapCreator.bundleCreator.getString("filledcircle"));
		lblPixels.setText(MapCreator.bundleCreator.getString("pixels"));
		btnClose.setText(MapCreator.bundleCreator.getString("close"));
		lblNormal.setText(MapCreator.bundleCreator.getString("normal"));
		lblSelected.setText(MapCreator.bundleCreator.getString("selected"));
		lblHighlighted.setText(MapCreator.bundleCreator.getString("highlighted"));
		btnDefNormal.setToolTipText(MapCreator.bundleCreator.getString("define"));
		btnDefSelected.setToolTipText(MapCreator.bundleCreator.getString("define"));
		btnDefHighlighted.setToolTipText(MapCreator.bundleCreator.getString("define"));
		rbnMultipleUnbound.setText(MapCreator.bundleCreator.getString("asmultipleiconsunbound"));
		rbtnMultipleIcons.setText(MapCreator.bundleCreator.getString("asmultipleicons"));
		lblHelpMultiple.setText(MapCreator.bundleCreator.getString("helpmultiple"));
		lblBoundOnField.setText(MapCreator.bundleCreator.getString("boundfield"));
		btnAdd.setText(MapCreator.bundleCreator.getString("addline"));
		btnDelete.setText(MapCreator.bundleCreator.getString("deleteline"));
		btnClrNormal.setToolTipText(MapCreator.bundleCreator.getString("clearicon"));
		btnClrSelected.setToolTipText(MapCreator.bundleCreator.getString("clearicon"));
		btnClrHighlighted.setToolTipText(MapCreator.bundleCreator.getString("clearicon"));

		//Initializations
		cmbEditor=new JComboBox();
		cmbEditor.setEditable(true);
		cmbEditor.setRenderer(new EditorFieldComboRenderer());
		txtPixels.setHorizontalAlignment(JNumberField.RIGHT);
		lblNormalIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblNormalIcon.setVerticalAlignment(SwingConstants.CENTER);
		lblSelectedIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedIcon.setVerticalAlignment(SwingConstants.CENTER);
		lblHighlightedIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblHighlightedIcon.setVerticalAlignment(SwingConstants.CENTER);
		rbtnCircle.setBackground(Color.yellow);
		rbtnSameIcons.setBackground(Color.yellow);
		rbnMultipleUnbound.setBackground(Color.yellow);
		rbtnMultipleIcons.setBackground(Color.yellow);

		//Multiple Icons Table
		tableModel=new MyTableModel(0);
		jTable=new JTable(tableModel);
		jTable.setRowHeight(24);
		jTable.setRowSelectionAllowed(false);
		jTable.setColumnSelectionAllowed(false);
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable.setDefaultRenderer(Object.class,new EditorFieldComboRenderer());
		//This listener is being added and removed in order to activate-deactivate the table
		final MouseListener tableMList=new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				rbtnMultipleIcons.setSelected(true);
			}
			public void mouseClicked(MouseEvent e) {
				rbtnMultipleIcons.setSelected(true);
				if (e.getClickCount()==2) {
					int mousePressedAtRow = jTable.rowAtPoint(e.getPoint());
					int mousePressedAtColumn = jTable.columnAtPoint(e.getPoint());
					if (tableModel.getColumnClass(mousePressedAtColumn).equals(ImageIcon.class)) {
						setCursor(Helpers.waitCursor);
						jTable.removeMouseListener(this);
						while (ie==null) ;
						if (tableModel.getValueAt(mousePressedAtRow,mousePressedAtColumn)!=null) {
							if (tableModel.getValueAt(mousePressedAtRow,mousePressedAtColumn) instanceof NewRestorableImageIcon)
								//change due to ImageEditor chages (N) //ie.setImage(new ImageIcon(((NewRestorableImageIcon) tableModel.getValueAt(mousePressedAtRow,mousePressedAtColumn)).getImage()));
								ie.setImage(((NewRestorableImageIcon) tableModel.getValueAt(mousePressedAtRow,mousePressedAtColumn)).getImage());
							else
								//change due to ImageEditor chages (N)//ie.setImage((ImageIcon) tableModel.getValueAt(mousePressedAtRow,mousePressedAtColumn));
								ie.setImage(((NewRestorableImageIcon) tableModel.getValueAt(mousePressedAtRow,mousePressedAtColumn)).getImage());
						} else
							ie.clear();
						ie.show();
						if (ie.getReturnCode()==ImageEditorDialog.IMAGE_EDITOR_OK) {
							//change due to ImageEditor chages (N)//tableModel.setValueAt(newIcon(ie.getImage()),mousePressedAtRow,mousePressedAtColumn);
							tableModel.setValueAt(newIcon(new NewRestorableImageIcon(ie.getImage())),mousePressedAtRow,mousePressedAtColumn);
							jTable.repaint();
						}
						jTable.addMouseListener(this);
						setCursor(Helpers.normalCursor);
					}
				}
			}
		};
		//This listener is being added and removed in order to activate-deactivate the table
		final ListSelectionListener tableLList=new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int[] r=jTable.getSelectedRows();
				if (r.length==0)
					btnDelete.setEnabled(false);
				else
					btnDelete.setEnabled(true);
			}
		};
		jTable.addMouseListener(tableMList);
		//Combo cell editor for values
		javax.swing.table.TableColumn valueColumn=jTable.getColumnModel().getColumn(0);
		final DefaultCellEditor ce=new DefaultCellEditor(cmbEditor);
		valueColumn.setCellEditor(ce);
		scrlTable.getViewport().setView(jTable);

		//Field ComboBox
		cmbField.setRenderer(new LayerInfo.ComboRenderer());
		cmbField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbField.getSelectedIndex()==-1)
					return;
				rbtnMultipleIcons.setSelected(true);
				AbstractTableField field=(AbstractTableField) cmbField.getSelectedItem();
				setCursor(Helpers.waitCursor);
				cmbEditor.removeAllItems();
				if (field.getDataType()==ImageTableField.DATA_TYPE) {
					scrlTable.setVisible(false);
					btnAdd.setEnabled(false);
					btnDelete.setEnabled(false);
				} else {
					scrlTable.setVisible(true);
					btnAdd.setEnabled(true);
					btnDelete.setEnabled(true);
					try {
						ArrayList data=table.getColumn(field.getName());
						ArrayList toPut=new ArrayList();
						Object datum;
						for (int i=0;i<data.size();i++) {
							datum=data.get(i);
							if ((!toPut.contains(datum)) && (datum!=null))
								toPut.add(datum);
						}
						Collections.sort(toPut);
						DefaultComboBoxModel cmbm=new DefaultComboBoxModel();
						for (int i=0;i<toPut.size();i++)
							cmbm.addElement(toPut.get(i));
						cmbEditor.setModel(cmbm);
					} catch(Throwable ex) {}
				}
				setCursor(Helpers.normalCursor);
			}
		});

		//Radius textbox
		txtPixels.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				rbtnCircle.setSelected(true);
			}
		});
		//Filled checkbox
		cbxFilled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rbtnCircle.setSelected(true);
			}
		});

		//Define normal icon
		btnDefNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Helpers.waitCursor);
				while (ie==null) ;
				if (lblNormalIcon.getIcon()!=null) {
					if (lblNormalIcon.getIcon() instanceof NewRestorableImageIcon)
						//change due to ImageEditor changes (N)//ie.setImage(new ImageIcon(((NewRestorableImageIcon) lblNormalIcon.getIcon()).getImage()));
						ie.setImage(((NewRestorableImageIcon) lblNormalIcon.getIcon()).getImage());
					else
						//change due to ImageEditor changes (N)//ie.setImage((ImageIcon) lblNormalIcon.getIcon());
						ie.setImage(((ImageIcon) lblNormalIcon.getIcon()).getImage());
				} else
					ie.clear();
				ie.show();
				if (ie.getReturnCode()==ImageEditorDialog.IMAGE_EDITOR_OK) {
					lblNormalIcon.setIcon(newIcon(new NewRestorableImageIcon(ie.getImage())));
				}
				rbtnSameIcons.setSelected(true);
				setCursor(Helpers.normalCursor);
			}
		});

		//Define selected icon
		btnDefSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Helpers.waitCursor);
				while (ie==null) ;
				if (lblSelectedIcon.getIcon()!=null) {
					if (lblSelectedIcon.getIcon() instanceof NewRestorableImageIcon)
						//change due to ImageEditor changes (N)//ie.setImage(new ImageIcon(((NewRestorableImageIcon) lblSelectedIcon.getIcon()).getImage()));
						ie.setImage(((NewRestorableImageIcon) lblSelectedIcon.getIcon()).getImage());
					else
						//change due to ImageEditor changes (N)//ie.setImage((ImageIcon) lblSelectedIcon.getIcon());
						ie.setImage(((ImageIcon) lblSelectedIcon.getIcon()).getImage());
				} else
					ie.clear();
				ie.show();
				if (ie.getReturnCode()==ImageEditorDialog.IMAGE_EDITOR_OK) {
					//change due to ImageEditor changes//lblSelectedIcon.setIcon(newIcon(ie.getImage()));
					lblSelectedIcon.setIcon(newIcon(new NewRestorableImageIcon(ie.getImage())));
				}
				rbtnSameIcons.setSelected(true);
				setCursor(Helpers.normalCursor);
			}
		});

		//Define highlighted icon
		btnDefHighlighted.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Helpers.waitCursor);
				while (ie==null) ;
				if (lblHighlightedIcon.getIcon()!=null) {
					if (lblHighlightedIcon.getIcon() instanceof NewRestorableImageIcon)
						//change due to ImageEditor changes//ie.setImage(new ImageIcon(((NewRestorableImageIcon) lblHighlightedIcon.getIcon()).getImage()));
						ie.setImage(((NewRestorableImageIcon) lblHighlightedIcon.getIcon()).getImage());
					else
						//change due to ImageEditor changes//ie.setImage((ImageIcon) lblHighlightedIcon.getIcon());
						ie.setImage(((ImageIcon) lblHighlightedIcon.getIcon()).getImage());
				} else
					ie.clear();
				ie.show();
				if (ie.getReturnCode()==ImageEditorDialog.IMAGE_EDITOR_OK) {
					lblHighlightedIcon.setIcon(newIcon(new NewRestorableImageIcon(ie.getImage())));
				}
				rbtnSameIcons.setSelected(true);
				setCursor(Helpers.normalCursor);
			}
		});

		//Button Add line on table
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.addRow();
				jTable.revalidate();
				jTable.repaint();
				rbtnMultipleIcons.setSelected(true);
			}
		});

		//Button Delete line from table
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] r=jTable.getSelectedRows();
				for (int i=r.length-1;i>-1;i--)
					tableModel.deleteRow(r[i]);
				jTable.clearSelection();
				jTable.revalidate();
				jTable.repaint();
				rbtnMultipleIcons.setSelected(true);
			}
		});

		//Clear buttons
		btnClrNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNormalIcon.setIcon(null);
			}
		});
		btnClrSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblSelectedIcon.setIcon(null);
			}
		});
		btnClrHighlighted.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblHighlightedIcon.setIcon(null);
			}
		});

		//Button Close
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		//Buttongroup
		ButtonGroup bg=new ButtonGroup();
		bg.add(rbtnCircle);
		bg.add(rbtnSameIcons);
		bg.add(rbnMultipleUnbound);
		bg.add(rbtnMultipleIcons);
		rbtnCircle.doClick();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ie=new ImageEditorDialog((JFrame) SwingUtilities.getAncestorOfClass(JFrame.class,map),20,20);
				ie.setLocation(Math.max(0,getX()+(getWidth()-ie.getWidth())/2),Math.max(0,getY()+(getHeight()-ie.getHeight())/2));
			}
		});
	}

	protected void setTable(Table table) {
		if ((table!=null) && !(table.equals(this.table))) {
			this.table=table;
			tableModel=new MyTableModel(table.getRecordCount());
			jTable.setModel(tableModel);
			javax.swing.table.TableColumn valueColumn=jTable.getColumnModel().getColumn(0);
			valueColumn.setCellEditor(new DefaultCellEditor(cmbEditor));
			if (rbtnMultipleIcons.isSelected())
				rbtnMultipleIcons.doClick();
			jTable.invalidate();
			jTable.revalidate();
			jTable.sizeColumnsToFit(JTable.AUTO_RESIZE_ALL_COLUMNS);
			jTable.repaint();
			cmbField.removeAllItems();
			for (int i=0;i<table.getFieldCount();i++)
				try {
					cmbField.addItem(table.getTableField(i));
				} catch(InvalidFieldIndexException e) {}
			//If a db table exists, multiple icons may be used.
			rbnMultipleUnbound.setEnabled(true);
			rbtnMultipleIcons.setEnabled(true);
			lblHelpMultiple.setVisible(false);
		} else if (table==null) {
			cmbField.removeAllItems();
			rbnMultipleUnbound.setEnabled(false);
			rbtnMultipleIcons.setEnabled(false);
			lblHelpMultiple.setVisible(true);
		}
	}

	protected void setLayer(PointLayer l) {
		if (firstTimeShown) {
			firstTimeShown=false;
			//PAINT_AS_CIRCLE
			txtPixels.setText(""+l.getCircleRadius());
			cbxFilled.setSelected(l.isCircleFilled());
			//PAINT_AS_SAME_ICONS
			lblNormalIcon.setIcon(l.getSingleNormalIcon());
			lblSelectedIcon.setIcon(l.getSingleSelectedIcon());
			lblHighlightedIcon.setIcon(l.getSingleHighlightedIcon());
			//PAINT_AS_MULTIPLE_ICONS
			try {
				cmbField.setSelectedIndex(table.getFieldIndex(l.getIconBase()));
			} catch(Exception e) {}
			if (l.getNormalIcons().size()>tableModel.getRowCount()+1) {
				tableModel=new MyTableModel(l.getNormalIcons().size()+2);
				jTable.setModel(tableModel);
			}
			int counter=0;
			Iterator it=l.getKeys().iterator();
			while (it.hasNext()) {
				Object key=it.next();
				if (key!=null) {
					tableModel.setValueAt(key,counter,0);
					tableModel.setValueAt(l.getNormalIcons().get(key),counter,1);
					tableModel.setValueAt(l.getSelectedIcons().get(key),counter,2);
					tableModel.setValueAt(l.getHighlightedIcons().get(key),counter,3);
					counter++;
				}
			}
		}

		if (l.getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS)
			rbtnSameIcons.setSelected(true);
		else if (rbnMultipleUnbound.isEnabled() && l.getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS) {
			if (l.getIconBase()!=null && l.getIconBase().isHidden())
				rbnMultipleUnbound.setSelected(true);
			else
				rbtnMultipleIcons.setSelected(true);
		} else
			rbtnCircle.setSelected(true);
	}

	protected boolean isCircleSelected() {
		return rbtnCircle.isSelected();
	}

	protected boolean isSameIconsSelected() {
		return rbtnSameIcons.isSelected();
	}

	protected boolean isMultipleIconsSelected() {
		return rbtnMultipleIcons.isSelected();
	}

	protected boolean isMultipleIconsUnboundSelected() {
		return rbnMultipleUnbound.isSelected();
	}

	protected int getCircleRadius() {
		try {
			return (new Integer(txtPixels.getText())).intValue();
		} catch(Exception e) {
			return 0;
		}
	}

	protected boolean isCircleFilled() {
		return cbxFilled.isSelected();
	}

	protected Icon getNormalIcon() {
		return lblNormalIcon.getIcon();
	}

	protected Icon getSelectedIcon() {
		return lblSelectedIcon.getIcon();
	}

	protected Icon getHighlightedIcon() {
		return lblHighlightedIcon.getIcon();
	}

	protected MyTableModel getTableModel() {
		return tableModel;
	}

	protected AbstractTableField getIconBase() {
		return (AbstractTableField) cmbField.getSelectedItem();
	}

	private Icon newIcon(Icon ic) {
		BufferedImage bi=new BufferedImage(ic.getIconWidth(),ic.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		ic.paintIcon(this,bi.getGraphics(),0,0);
		return new ImageIcon(bi);
	}

	private void jbInit() throws Exception {
		this.setTitle("Icon Properties");
		this.getContentPane().setLayout(smartLayout1);
		Font labelFont=new java.awt.Font(lblView.getFont().getName(), 1, lblView.getFont().getSize());
		lblView.setFont(labelFont);
		lblView.setText("View as");
		rbtnCircle.setOpaque(false);
		rbtnCircle.setText("As circle");
		rbtnCircle.setFont(labelFont);
		rbtnSameIcons.setOpaque(false);
		rbtnSameIcons.setText("Same Icons");
		rbtnSameIcons.setFont(labelFont);
		cbxFilled.setOpaque(false);
		cbxFilled.setSelected(true);
		cbxFilled.setText("Filled");
		lblPixels.setFont(labelFont);
		lblPixels.setText("pixels.");
		txtPixels.setText("6");
		btnClose.setText("Close");
		lblNormal.setText("Normal");
		lblSelected.setText("Selected");
		lblHighlighted.setText("Highlighted");
		btnDefNormal.setFocusPainted(false);
		btnDefNormal.setIcon(new ImageIcon(gr.cti.eslate.mapModel.PointLayerProperties.class.getResource("images/iconEditor.gif")));
		btnDefNormal.setMargin(new Insets(0, 0, 0, 0));
		lblNormalIcon.setBorder(BorderFactory.createEtchedBorder());
		lblNormalIcon.setBackground(Color.white);
		lblNormalIcon.setOpaque(true);
		lblSelectedIcon.setBorder(BorderFactory.createEtchedBorder());
		lblSelectedIcon.setBackground(Color.white);
		lblSelectedIcon.setOpaque(true);
		lblHighlightedIcon.setBorder(BorderFactory.createEtchedBorder());
		lblHighlightedIcon.setBackground(Color.white);
		lblHighlightedIcon.setOpaque(true);
		btnDefSelected.setFocusPainted(false);
		btnDefSelected.setIcon(new ImageIcon(gr.cti.eslate.mapModel.PointLayerProperties.class.getResource("images/iconEditor.gif")));
		btnDefSelected.setMargin(new Insets(0, 0, 0, 0));
		btnDefHighlighted.setFocusPainted(false);
		btnDefHighlighted.setIcon(new ImageIcon(gr.cti.eslate.mapModel.PointLayerProperties.class.getResource("images/iconEditor.gif")));
		btnDefHighlighted.setMargin(new Insets(0, 0, 0, 0));
		rbtnMultipleIcons.setOpaque(false);
		rbtnMultipleIcons.setText("Multiple Icons");
		rbtnMultipleIcons.setFont(labelFont);
		lblBoundOnField.setText("Bound on field:");
		btnAdd.setText("Add Line");
		btnDelete.setText("Delete Line");
		btnClrNormal.setFocusPainted(false);
		btnClrNormal.setIcon(new ImageIcon(gr.cti.eslate.mapModel.PointLayerProperties.class.getResource("images/clear.gif")));
		btnClrNormal.setMargin(new Insets(0, 0, 0, 0));
		btnClrSelected.setFocusPainted(false);
		btnClrSelected.setIcon(new ImageIcon(gr.cti.eslate.mapModel.PointLayerProperties.class.getResource("images/clear.gif")));
		btnClrSelected.setMargin(new Insets(0, 0, 0, 0));
		btnClrHighlighted.setFocusPainted(false);
		btnClrHighlighted.setIcon(new ImageIcon(gr.cti.eslate.mapModel.PointLayerProperties.class.getResource("images/clear.gif")));
		btnClrHighlighted.setMargin(new Insets(0, 0, 0, 0));
		rbnMultipleUnbound.setOpaque(false);
		rbnMultipleUnbound.setText("Multiple Icons");
		rbnMultipleUnbound.setFont(labelFont);
		lblHelpMultiple.setText("To select multiple database must exist");
		this.getContentPane().add(lblView, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(rbtnCircle, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.EdgeAnchor(lblView, Anchor.Bottom, Anchor.Below, Anchor.Top, 15),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(cbxFilled, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(rbtnCircle, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(rbtnCircle, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblPixels, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(txtPixels, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(rbtnCircle, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(rbtnCircle, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.getContentPane().add(txtPixels, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 50),
					new com.thwt.layout.EdgeAnchor(rbtnCircle, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(rbtnCircle, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(rbtnSameIcons, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.EdgeAnchor(cbxFilled, Anchor.Bottom, Anchor.Below, Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnClose, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 15),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblNormal, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(rbtnSameIcons, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(lblNormalIcon, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblSelected, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblNormal, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblHighlighted, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblNormal, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(lblHighlightedIcon, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblNormalIcon, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 34),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 34),
					new com.thwt.layout.EdgeAnchor(lblNormal, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.25, Anchor.Right, Anchor.Right, 0.0, 0)));
		this.getContentPane().add(lblSelectedIcon, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 34),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 34),
					new com.thwt.layout.EdgeAnchor(lblNormalIcon, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0)));
		this.getContentPane().add(lblHighlightedIcon, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 34),
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 34),
					new com.thwt.layout.EdgeAnchor(lblNormalIcon, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.75, Anchor.Right, Anchor.Left, 0.0, 0)));
		this.getContentPane().add(btnDefNormal, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblNormalIcon, Anchor.Bottom, Anchor.Below, Anchor.Top, 2),
					new com.thwt.layout.FractionAnchor(lblNormalIcon, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnDefSelected, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnDefNormal, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(lblSelectedIcon, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnDefHighlighted, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnDefNormal, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(lblHighlightedIcon, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(rbtnMultipleIcons, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.EdgeAnchor(lblHelpMultiple, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblBoundOnField, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 35),
					new com.thwt.layout.EdgeAnchor(rbtnMultipleIcons, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(cmbField, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 35),
					new com.thwt.layout.EdgeAnchor(lblBoundOnField, Anchor.Right, Anchor.Right, Anchor.Left, 15),
					new com.thwt.layout.EdgeAnchor(rbtnMultipleIcons, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -2, 1.0)));
		this.getContentPane().add(btnAdd, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(cmbField, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -10, 1.0)));
		this.getContentPane().add(btnDelete, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnAdd, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 1),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -10, 1.0)));
		this.getContentPane().add(scrlTable, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 20),
					new com.thwt.layout.EdgeAnchor(btnAdd, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.EdgeAnchor(btnClose, Anchor.Top, Anchor.Above, Anchor.Bottom, 10)));
		this.getContentPane().add(btnClrNormal, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnDefNormal, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(btnDefNormal, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnClrSelected, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnDefSelected, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(btnDefSelected, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnClrHighlighted, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnDefHighlighted, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(btnDefHighlighted, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(rbnMultipleUnbound, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.EdgeAnchor(btnDefNormal, Anchor.Bottom, Anchor.Below, Anchor.Top, 15),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblHelpMultiple, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(rbnMultipleUnbound, Anchor.Bottom, Anchor.Below, Anchor.Top, 2),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
	}

	class EditorFieldComboRenderer extends JLabel implements ListCellRenderer, TableCellRenderer {
		EditorFieldComboRenderer() {
			setHorizontalAlignment(SwingConstants.LEFT);
			setHorizontalTextPosition(SwingConstants.LEFT);
		}

		JLabel render(Object value) {
			setIcon(null);
			if (value instanceof Double)
				setText(table.getNumberFormat().format((Double) value));
			else if (value instanceof Date)
				setText(table.getDateFormat().format((Date) value));
			else if (value instanceof Icon) {
				setText(null);
				setIcon((Icon) value);
			} else if (value!=null)
				setText(value.toString());
			else
				setText("");
			return this;
		}

		public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
			return render(value);
		}

		public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col) {
			return render(value);
		}
	}

	private JTable jTable;
	private MyTableModel tableModel;
	private Table table;
	private JComboBox cmbEditor;
	protected boolean firstTimeShown;

	//UI elements generated by JBuilder
	private SmartLayout smartLayout1 = new SmartLayout();
	private JLabel lblView = new JLabel();
	private JRadioButton rbtnCircle = new JRadioButton() {
		public void paintComponent(Graphics g) {
			if (isSelected())
				setOpaque(true);
			else
				setOpaque(false);
			super.paintComponent(g);
			PointLayerProperties.this.repaint();
		}
	};
	private JRadioButton rbtnSameIcons = new JRadioButton() {
		public void paintComponent(Graphics g) {
			if (isSelected())
				setOpaque(true);
			else
				setOpaque(false);
			super.paintComponent(g);
			PointLayerProperties.this.repaint();
		}
	};
	private JCheckBox cbxFilled = new JCheckBox();
	private JLabel lblPixels = new JLabel();
	private JNumberField txtPixels = new JNumberField("6");
	private JButton btnClose = new JButton();
	private JLabel lblNormal = new JLabel();
	private JLabel lblSelected = new JLabel();
	private JLabel lblHighlighted = new JLabel();
	private JButton btnDefNormal = new JButton();
	private SmartLayout smartLayout2 = new SmartLayout();
	private JLabel lblNormalIcon = new JLabel();
	private SmartLayout smartLayout3 = new SmartLayout();
	private JLabel lblSelectedIcon = new JLabel();
	private JLabel lblHighlightedIcon = new JLabel();
	private JButton btnDefSelected = new JButton();
	private JButton btnDefHighlighted = new JButton();
	private JRadioButton rbtnMultipleIcons = new JRadioButton() {
		public void paintComponent(Graphics g) {
			if (isSelected())
				setOpaque(true);
			else
				setOpaque(false);
			super.paintComponent(g);
			PointLayerProperties.this.repaint();
		}
	};
	private JLabel lblBoundOnField = new JLabel();
	private JComboBox cmbField = new JComboBox();
	private JButton btnAdd = new JButton();
	private JButton btnDelete = new JButton();
	private JScrollPane scrlTable = new JScrollPane();
	private ImageEditorDialog ie;
	private JButton btnClrNormal = new JButton();
	private JButton btnClrSelected = new JButton();
	private JButton btnClrHighlighted = new JButton();
	private JRadioButton rbnMultipleUnbound = new JRadioButton() {
		public void paintComponent(Graphics g) {
			if (isSelected())
				setOpaque(true);
			else
				setOpaque(false);
			super.paintComponent(g);
			PointLayerProperties.this.repaint();
		}
	};
	private JLabel lblHelpMultiple = new JLabel();
}
