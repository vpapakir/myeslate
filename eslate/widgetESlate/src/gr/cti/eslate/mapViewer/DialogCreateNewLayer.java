package gr.cti.eslate.mapViewer;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class DialogCreateNewLayer extends JDialog {
    JLabel lblGiveName = new JLabel();
    SmartLayout smartLayout1 = new SmartLayout();
    JTextField txtName = new JTextField();
    JRadioButton rbnThis = new JRadioButton();
    JRadioButton rbnAll = new JRadioButton();
    JButton btnOK = new JButton();
    JButton btnCancel = new JButton();

    DialogCreateNewLayer(Frame frame, String title, boolean modal) {
        super(frame, title, modal);

        setSize(400,175);

        try  {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        setModal(true);
        setResizable(false);

        lblGiveName.setText(MapViewer.messagesBundle.getString("givelayername"));
        txtName.setText(MapViewer.messagesBundle.getString("defaultlayername"));
        ButtonGroup bg=new ButtonGroup();
        rbnThis.setText(MapViewer.messagesBundle.getString("showinthisregion"));
        bg.add(rbnThis);

        rbnAll.setText(MapViewer.messagesBundle.getString("showinallregions"));
        bg.add(rbnAll);

        rbnThis.setSelected(true);

        btnOK.setText(MapViewer.messagesBundle.getString("ok"));
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtName.getText().equals("")) {
                    JOptionPane.showMessageDialog(getOwner(),MapViewer.messagesBundle.getString("noname"),"",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                setVisible(false);
            }
        });

        btnCancel.setText(MapViewer.messagesBundle.getString("cancel"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtName.setText("");
                setVisible(false);
            }
        });

        //When closing the window without pressing buttons, serve as cancel
        addWindowListener(new WindowAdapter() {
            public void windowClosing(ComponentEvent e) {
                btnCancel.doClick();
            }
        });
    }

    public DialogCreateNewLayer(Frame owner) {
        this(owner, "", true);
    }

    protected String getLayerName() {
        return txtName.getText();
    }

    protected boolean isShowInAllSelected() {
        return rbnAll.isSelected();
    }

    protected boolean isShowInThisSelected() {
        return rbnThis.isSelected();
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
        lblGiveName.setText("Give layer name:");
        this.getContentPane().setLayout(smartLayout1);
        rbnThis.setOpaque(false);
        rbnThis.setText("Show in this region only");
        rbnAll.setOpaque(false);
        rbnAll.setText("Show in all regions");
        btnOK.setText("OK");
        btnCancel.setText("Cancel");
        this.getContentPane().add(lblGiveName, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(txtName, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(lblGiveName, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(rbnThis, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 25),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 25),
					new com.thwt.layout.EdgeAnchor(txtName, Anchor.Bottom, Anchor.Below, Anchor.Top, 8),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(rbnAll, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 25),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 25),
					new com.thwt.layout.EdgeAnchor(rbnThis, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(btnOK, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(btnCancel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
    }
}
