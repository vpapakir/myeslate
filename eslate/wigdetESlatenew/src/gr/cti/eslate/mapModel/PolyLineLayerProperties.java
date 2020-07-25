package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.IPolyLineLayerView;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class PolyLineLayerProperties extends JDialog {
	PolyLineLayerProperties() {
		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				btnClose.doClick();
				setVisible(false);
			}
		});

		//Localization
		lblView.setText(MapCreator.bundleCreator.getString("lineview"));
		rbtnStraight.setText(MapCreator.bundleCreator.getString("straight"));
		rbtnDashed.setText(MapCreator.bundleCreator.getString("dashed"));
		rbtnDotted.setText(MapCreator.bundleCreator.getString("dotted"));
		lblWidth.setText(MapCreator.bundleCreator.getString("width"));
		lblPixels.setText(MapCreator.bundleCreator.getString("linepixels"));
		btnClose.setText(MapCreator.bundleCreator.getString("close"));
		lblErrorTol.setText(MapCreator.bundleCreator.getString("errorTolerance"));
		lblErrorTol2.setText(MapCreator.bundleCreator.getString("errorTolerance2"));
		cmbAntialias.addItem(MapCreator.bundleCreator.getString("antialiasdefault"));
		cmbAntialias.addItem(MapCreator.bundleCreator.getString("antialiason"));
		cmbAntialias.addItem(MapCreator.bundleCreator.getString("antialiasoff"));

		//Initializations
		setTitle(MapCreator.bundleCreator.getString("viewproperties"));
		setSize(370,340);
		setModal(true);
		setResizable(false);
		txtErrorTol.setValidChars("012345678-.");

		//Button close
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		//Buttongroup
		ButtonGroup bg=new ButtonGroup();
		bg.add(rbtnStraight);
		bg.add(rbtnDashed);
		bg.add(rbtnDotted);
		rbtnStraight.doClick();
	}

	protected void setLayer(PolyLineLayer l) {
		if (firstTimeShown) {
			firstTimeShown=false;
			switch (l.getPaintMode()) {
				case IPolyLineLayerView.PAINT_AS_STRAIGHT_LINE:
					rbtnStraight.setSelected(true);
					break;
				case IPolyLineLayerView.PAINT_AS_DASHED_LINE:
					rbtnDashed.setSelected(true);
					break;
				case IPolyLineLayerView.PAINT_AS_DOTTED_LINE:
					rbtnDotted.setSelected(true);
					break;
			}
			txtWidth.setText(""+l.getLineWidth());
			switch (l.getAntialiasState()) {
				case 0:
					cmbAntialias.setSelectedIndex(2);
					break;
				case 1:
					cmbAntialias.setSelectedIndex(1);
					break;
				default:
					cmbAntialias.setSelectedIndex(0);
			}
			try {
				txtErrorTol.setText((new Float(l.getErrorTolerance())).toString());
			} catch(Throwable ex) {
				txtErrorTol.setText("-1.0");
			}
		}
	}

	protected boolean isStraightLineSelected() {
		return rbtnStraight.isSelected();
	}

	protected boolean isDashedLineSelected() {
		return rbtnDashed.isSelected();
	}

	protected boolean isDottedLineSelected() {
		return rbtnDotted.isSelected();
	}

	protected int getLineWidth() {
		try {
			return (new Integer(txtWidth.getText())).intValue();
		} catch(NumberFormatException e) {
			return 0;
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(smartLayout1);
		lblView.setText("View as");
		rbtnStraight.setBackground(Color.yellow);
		rbtnStraight.setText("Straight line");
		rbtnDashed.setBackground(Color.yellow);
		rbtnDashed.setText("Dashed line");
		rbtnDotted.setBackground(Color.yellow);
		rbtnDotted.setText("Dotted line");
		lblWidth.setText("Line width");
		txtWidth.setText("1");
		txtWidth.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPixels.setText("pixels.");
		btnClose.setText("Close");
		this.getContentPane().add(lblView, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(rbtnStraight, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 35),
					new com.thwt.layout.EdgeAnchor(lblView, Anchor.Bottom, Anchor.Below, Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(rbtnDashed, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 35),
					new com.thwt.layout.EdgeAnchor(rbtnStraight, Anchor.Bottom, Anchor.Below, Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(rbtnDotted, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 35),
					new com.thwt.layout.EdgeAnchor(rbtnDashed, Anchor.Bottom, Anchor.Below, Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblWidth, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 35),
					new com.thwt.layout.EdgeAnchor(txtWidth, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(txtWidth, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.getContentPane().add(txtWidth, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 40),
					new com.thwt.layout.EdgeAnchor(lblWidth, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(rbtnDotted, Anchor.Bottom, Anchor.Below, Anchor.Top, 20),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblPixels, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(txtWidth, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(txtWidth, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(txtWidth, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.getContentPane().add(cmbAntialias, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 35),
					new com.thwt.layout.EdgeAnchor(lblPixels, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(btnClose, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 10),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblErrorTol, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(cmbAntialias, Anchor.Bottom, Anchor.Below, Anchor.Top, 15),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(lblErrorTol2, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(lblErrorTol, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(txtErrorTol, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 35),
					new com.thwt.layout.EdgeAnchor(lblErrorTol2, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 50, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
	}

	private boolean firstTimeShown=true;
	//UI elements generated by JBuilder
	private JLabel lblView = new JLabel();
	private SmartLayout smartLayout1 = new SmartLayout();
	private JRadioButton rbtnStraight = new JRadioButton() {
		public void paintComponent(Graphics g) {
			if (isSelected())
				setOpaque(true);
			else
				setOpaque(false);
			super.paintComponent(g);
			PolyLineLayerProperties.this.repaint();
		}
	};
	private JRadioButton rbtnDashed = new JRadioButton() {
		public void paintComponent(Graphics g) {
			if (isSelected())
				setOpaque(true);
			else
				setOpaque(false);
			super.paintComponent(g);
			PolyLineLayerProperties.this.repaint();
		}
	};
	private JRadioButton rbtnDotted = new JRadioButton() {
		public void paintComponent(Graphics g) {
			if (isSelected())
				setOpaque(true);
			else
				setOpaque(false);
			super.paintComponent(g);
			PolyLineLayerProperties.this.repaint();
		}
	};
	private JLabel lblWidth = new JLabel();
	private JNumberField txtWidth = new JNumberField("1");
	private JLabel lblPixels = new JLabel();
	private JButton btnClose = new JButton();
	JComboBox cmbAntialias = new JComboBox();
	JNumberField txtErrorTol = new JNumberField("-1.0");
	private JLabel lblErrorTol = new JLabel();
	private JLabel lblErrorTol2 = new JLabel();
}