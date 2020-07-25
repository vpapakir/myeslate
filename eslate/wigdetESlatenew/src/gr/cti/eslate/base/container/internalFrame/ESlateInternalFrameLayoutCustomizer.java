package gr.cti.eslate.base.container.internalFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * This dialog customizes the OverlapBorderLayout of the ESlateInternalFrame.
 * @author  Giorgos Vasiliou
 * @version 1.0
 */
class ESlateInternalFrameLayoutCustomizer extends JDialog {
    private ESlateInternalFrame esframe;
    private OverlapBorderLayout layout,demoLayout;
    private JPanel main = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JPanel demo = new JPanel();
    private JPanel center = new JPanel();
    private JCheckBox now = new JCheckBox();
    private JCheckBox eon = new JCheckBox();
    private JCheckBox soe = new JCheckBox();
    private JCheckBox wos = new JCheckBox();
    private JButton ok = new JButton();
    private Component component1;

    public ESlateInternalFrameLayoutCustomizer(ESlateInternalFrame frame) {
        super((Frame) SwingUtilities.getAncestorOfClass(Frame.class,frame), "", true);
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        layout=frame.getSkinLayout();
        esframe=frame;

        //Localize
        ResourceBundle bundle=ResourceBundle.getBundle("gr.cti.eslate.base.container.internalFrame.ESlateInternalFrameResourceBundle", Locale.getDefault());
        now.setText(bundle.getString("northOverWest"));
        eon.setText(bundle.getString("eastOverNorth"));
        soe.setText(bundle.getString("southOverEast"));
        wos.setText(bundle.getString("westOverSouth"));
        ok.setText(bundle.getString("ok"));
        setTitle(bundle.getString("layoutcustomizer"));

        center.setBorder(new EmptyBorder(4,4,4,4));

        //Build the demo panel
        JPanel n=new JPanel();
        n.setPreferredSize(new Dimension(100,25));
        n.setBackground(Color.white);
        n.setBorder(new LineBorder(Color.black));

        JPanel s=new JPanel();
        s.setPreferredSize(new Dimension(100,25));
        s.setBackground(Color.white);
        s.setBorder(new LineBorder(Color.black));

        JPanel e=new JPanel();
        e.setPreferredSize(new Dimension(25,100));
        e.setBackground(Color.white);
        e.setBorder(new LineBorder(Color.black));

        JPanel w=new JPanel();
        w.setPreferredSize(new Dimension(25,100));
        w.setBackground(Color.white);
        w.setBorder(new LineBorder(Color.black));

        JPanel c=new JPanel();
        c.setPreferredSize(new Dimension(100,100));
        c.setBackground(Color.white);

        demoLayout=new OverlapBorderLayout();
        demo.setLayout(demoLayout);
        demo.add(n,BorderLayout.NORTH);
        demo.add(s,BorderLayout.SOUTH);
        demo.add(e,BorderLayout.EAST);
        demo.add(w,BorderLayout.WEST);
        demo.add(c,BorderLayout.CENTER);

        demoLayout.setNorthOverWest(layout.isNorthOverWest());
        demoLayout.setEastOverNorth(layout.isEastOverNorth());
        demoLayout.setSouthOverEast(layout.isSouthOverEast());
        demoLayout.setWestOverSouth(layout.isWestOverSouth());

        now.setSelected(layout.isNorthOverWest());
        eon.setSelected(layout.isEastOverNorth());
        soe.setSelected(layout.isSouthOverEast());
        wos.setSelected(layout.isWestOverSouth());

        //Add the listeners
        now.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                demoLayout.setNorthOverWest(now.isSelected());
                demo.revalidate();
            }
        });
        eon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                demoLayout.setEastOverNorth(eon.isSelected());
                demo.revalidate();
            }
        });
        soe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                demoLayout.setSouthOverEast(soe.isSelected());
                demo.revalidate();
            }
        });
        wos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                demoLayout.setWestOverSouth(wos.isSelected());
                demo.revalidate();
            }
        });

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layout.setNorthOverWest(demoLayout.isNorthOverWest());
                layout.setEastOverNorth(demoLayout.isEastOverNorth());
                layout.setSouthOverEast(demoLayout.isSouthOverEast());
                layout.setWestOverSouth(demoLayout.isWestOverSouth());
                dispose();
                esframe.invalidate();
                esframe.validate();
                esframe.repaint();
            }
        });

        pack();
        setResizable(false);
    }

    void jbInit() throws Exception {
        component1 = Box.createVerticalStrut(12);
        main.setLayout(borderLayout1);
        this.getContentPane().setLayout(borderLayout2);
        demo.setPreferredSize(new Dimension(100, 10));
        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        now.setText("North over West");
        eon.setText("East over North");
        soe.setText("South over East");
        wos.setText("West over South");
        ok.setText("OK");
        getContentPane().add(main, BorderLayout.CENTER);
        main.add(demo, BorderLayout.EAST);
        main.add(center, BorderLayout.CENTER);
        center.add(now, null);
        center.add(eon, null);
        center.add(soe, null);
        center.add(wos, null);
        center.add(component1, null);
        center.add(ok, null);
    }
}