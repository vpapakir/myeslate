package gr.cti.eslate.mapModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

public class TimeEra extends JDialog {

    public TimeEra(MapBackground mb) {
        super(new JFrame(),MapCreator.bundleCreator.getString("timeera"),true);
        setSize(380,265);
        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        //Localization
        rbnAllEras.setText(MapCreator.bundleCreator.getString("alleras"));
        rbnSpecific.setText(MapCreator.bundleCreator.getString("specific"));
        lblFrom.setText(MapCreator.bundleCreator.getString("from"));
        lblTo.setText(MapCreator.bundleCreator.getString("to"));
        lblFormat.setText(MapCreator.bundleCreator.getString("dateformat"));
        lblFormat.setToolTipText(MapCreator.bundleCreator.getString("dateformattip"));
        cbxFormat.setToolTipText(MapCreator.bundleCreator.getString("dateformattip"));
        lblName.setText(MapCreator.bundleCreator.getString("backgrounddescr"));
        btnOK.setText(MapCreator.bundleCreator.getString("ok"));
        btnCancel.setText(MapCreator.bundleCreator.getString("cancel"));

        //Initialization
        ButtonGroup bg=new ButtonGroup();
        bg.add(rbnAllEras);
        bg.add(rbnSpecific);
        returnCode=NONE;
        if (mb.getDateFrom()==null || mb.getDateTo()==null)
            rbnAllEras.setSelected(true);
        else
            rbnSpecific.setSelected(true);
        txtName.setText(mb.getFilename());

        //Initialize from-to dates
        try {
            SimpleDateFormat sdf=new SimpleDateFormat(mb.getDateFormatPattern());
            try {
                txtFrom.setText(sdf.format(mb.getDateFrom()));
            } catch(Throwable e) {
                /*No from date*/
            }
            try {
                txtTo.setText(sdf.format(mb.getDateTo()));
            } catch(Throwable e) {
                /*No to date*/
            }
        } catch(Throwable th) {
            /*No dates at all*/
        }

        //Date Formats
        Date d=new Date();
        dmy=new SimpleDateFormat("dd/MM/yyyy");
        dmyg=new SimpleDateFormat("dd/MM/yyyy G");
        my=new SimpleDateFormat("MM/yyyy");
        myg=new SimpleDateFormat("MM/yyyy G");
        y=new SimpleDateFormat("yyyy");
        yg=new SimpleDateFormat("yyyy G");
        cbxFormat.addItem(dmy.format(d));
        cbxFormat.addItem(dmyg.format(d));
        cbxFormat.addItem(my.format(d));
        cbxFormat.addItem(myg.format(d));
        cbxFormat.addItem(y.format(d));
        cbxFormat.addItem(yg.format(d));
        try {
            String s=mb.getDateFormatPattern();
            if (s.equals("dd/MM/yyyy"))
                cbxFormat.setSelectedIndex(0);
            else if (s.equals("dd/MM/yyyy G"))
                cbxFormat.setSelectedIndex(1);
            else if (s.equals("MM/yyyy"))
                cbxFormat.setSelectedIndex(2);
            else if (s.equals("MM/yyyy G"))
                cbxFormat.setSelectedIndex(3);
            else if (s.equals("yyyy"))
                cbxFormat.setSelectedIndex(4);
            else if (s.equals("yyyy G"))
                cbxFormat.setSelectedIndex(5);
        } catch(Throwable t) {
                cbxFormat.setSelectedIndex(0);
        }

        //OK button
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtName.getText().equals("")) {
                    JOptionPane.showMessageDialog(TimeEra.this,MapCreator.bundleCreator.getString("nobackname"),"",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (rbnSpecific.isSelected()) {
                    try {
                        switch(cbxFormat.getSelectedIndex()) {
                            case 0:
                                dateFormatPattern="dd/MM/yyyy";
                                from=dmy.parse(txtFrom.getText());
                                to=dmy.parse(txtTo.getText());
                                break;
                            case 1:
                                dateFormatPattern="dd/MM/yyyy G";
                                from=dmyg.parse(txtFrom.getText());
                                to=dmyg.parse(txtTo.getText());
                                break;
                            case 2:
                                dateFormatPattern="MM/yyyy";
                                from=my.parse(txtFrom.getText());
                                to=my.parse(txtTo.getText());
                                break;
                            case 3:
                                dateFormatPattern="MM/yyyy G";
                                from=myg.parse(txtFrom.getText());
                                to=myg.parse(txtTo.getText());
                                break;
                            case 4:
                                dateFormatPattern="yyyy";
                                from=y.parse(txtFrom.getText());
                                to=y.parse(txtTo.getText());
                                break;
                            case 5:
                                dateFormatPattern="yyyy G";
                                from=yg.parse(txtFrom.getText());
                                to=yg.parse(txtTo.getText());
                                break;
                        }
                    } catch(Throwable t) {
                        JOptionPane.showMessageDialog(TimeEra.this,MapCreator.bundleCreator.getString("errordate"),"",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (from.after(to)) {
                        JOptionPane.showMessageDialog(TimeEra.this,MapCreator.bundleCreator.getString("errordatesbeforeafter"),"",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    returnCode=SPECIFIC;
                } else {
                    from=to=null;
                    returnCode=ALL_DATES;
                }
                setVisible(false);
            }
        });

        //Cancel button
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                from=to=null;
                returnCode=NONE;
                setVisible(false);
            }
        });

        txtFrom.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                rbnSpecific.setSelected(true);
            }
        });

        txtTo.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                rbnSpecific.setSelected(true);
            }
        });

        cbxFormat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rbnSpecific.setSelected(true);
            }
        });
    }

    String name() {
        return txtName.getText();
    }

    void jbInit() throws Exception {
        panel1.setLayout(smartLayout1);
        rbnAllEras.setText("All time eras");
        rbnSpecific.setText("Specific time era");
        lblFrom.setText("From");
        lblTo.setText("To");
        lblFormat.setText("Date Format");
        btnOK.setText("OK");
        btnCancel.setText("Cancel");
        lblName.setText("Name:");
        getContentPane().add(panel1);
        panel1.add(rbnAllEras, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 15),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 50),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(rbnSpecific, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 15),
					new com.thwt.layout.EdgeAnchor(rbnAllEras, Anchor.Bottom, Anchor.Below, Anchor.Top, 2),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(lblFrom, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 15),
					new com.thwt.layout.EdgeAnchor(rbnSpecific, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(lblTo, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 15),
					new com.thwt.layout.EdgeAnchor(rbnSpecific, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(txtFrom, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 15),
					new com.thwt.layout.EdgeAnchor(lblFrom, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.45, Anchor.Right, Anchor.Right, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(txtTo, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 15),
					new com.thwt.layout.EdgeAnchor(lblTo, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.55, Anchor.Right, Anchor.Left, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(lblFormat, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(txtTo, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(cbxFormat, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblFormat, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(btnOK, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 10),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(btnCancel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 10),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(lblName, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 15),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 15),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        panel1.add(txtName, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 15),
					new com.thwt.layout.EdgeAnchor(lblName, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.FractionAnchor(lblName, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
    }

    private JPanel panel1 = new JPanel();
    private SmartLayout smartLayout1 = new SmartLayout();
    private JRadioButton rbnAllEras = new JRadioButton();
    private JRadioButton rbnSpecific = new JRadioButton();
    private JLabel lblFrom = new JLabel();
    private JLabel lblTo = new JLabel();
    private JTextField txtFrom = new JTextField();
    private JTextField txtTo = new JTextField();
    private JLabel lblFormat = new JLabel();
    private JComboBox cbxFormat = new JComboBox();
    private JButton btnOK = new JButton();
    private JButton btnCancel = new JButton();
    private SimpleDateFormat dmy,dmyg,my,myg,y,yg;
    Date from,to;
    String dateFormatPattern;
    int returnCode;
    static final int NONE=0;
    static final int ALL_DATES=1;
    static final int SPECIFIC=2;
    JLabel lblName = new JLabel();
    JTextField txtName = new JTextField();
}
