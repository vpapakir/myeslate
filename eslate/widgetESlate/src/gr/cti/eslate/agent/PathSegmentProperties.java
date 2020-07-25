package gr.cti.eslate.agent;

import gr.cti.eslate.protocol.IAgent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

public class PathSegmentProperties extends JDialog {

    public PathSegmentProperties(PathSegment ps,IAgent agent) {
        super((Frame) SwingUtilities.getAncestorOfClass(Frame.class,(Component) agent));
        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        this.ps=ps;
        this.agent=agent;

        ButtonGroup bg=new ButtonGroup();
        bg.add(rbnStraightLine);
        bg.add(rbnDotted);
        bg=new ButtonGroup();
        bg.add(rbnSolidColor);
        bg.add(rbnGradientColor);

        //Localize
        java.util.ResourceBundle r=Agent.bundleSegmentProperties;
        setTitle(r.getString("title"));
        lblName.setText(r.getString("name"));
        lblStroke.setText(r.getString("strokeas"));
        rbnStraightLine.setText(r.getString("straightline"));
        rbnDotted.setText(r.getString("dottedline"));
        lblWidth.setText(r.getString("width"));
        lblPaintAs.setText(r.getString("paintas"));
        rbnSolidColor.setText(r.getString("solidcolor"));
        rbnGradientColor.setText(r.getString("gradientcolor"));
        btnSolid.setText(r.getString("define"));
        btnGradientStart.setText(r.getString("define"));
        lblGradientStart.setText(r.getString("gradientstart"));
        lblGradEnd.setText(r.getString("gradientend"));
        btnGradientEnd.setText(r.getString("define"));
        btnOK.setText(r.getString("ok"));
        btnCancel.setText(r.getString("cancel"));
        btnApply.setText(r.getString("apply"));

        //Listeners
        btnSolid.addActionListener(new ColorActionListener(btnSolid));
        btnSolid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rbnSolidColor.setSelected(true);
                invalidate();
                repaint();
            }
        });
        btnGradientStart.addActionListener(new ColorActionListener(btnGradientStart));
        btnGradientStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rbnGradientColor.setSelected(true);
                invalidate();
                repaint();
            }
        });
        btnGradientEnd.addActionListener(new ColorActionListener(btnGradientEnd));
        btnGradientEnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rbnGradientColor.setSelected(true);
                invalidate();
                repaint();
            }
        });
        sldSolid.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                rbnSolidColor.setSelected(true);
                invalidate();
                repaint();
            }
        });
        sldGradientStart.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                rbnGradientColor.setSelected(true);
                invalidate();
                repaint();
            }
        });
        sldGradientEnd.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                rbnGradientColor.setSelected(true);
                invalidate();
                repaint();
            }
        });
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyChanges();
                setVisible(false);
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        btnApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyChanges();
            }
        });

        //Initialize
        txtName.setText(ps.getName());
        if (ps.getStrokeAs()==PathSegment.STROKE_AS_DOTTED_LINE)
            rbnDotted.setSelected(true);
        else
            rbnStraightLine.setSelected(true);
        if (ps.getPaintAs()==PathSegment.PAINT_AS_GRADIENT_COLOR)
            rbnGradientColor.setSelected(true);
        else
            rbnSolidColor.setSelected(true);
        Color c;
        c=ps.getSolidColor();
        btnSolid.setBackground(new Color(c.getRed(),c.getGreen(),c.getBlue()));
        btnSolid.setForeground(getProperColor(btnSolid.getBackground()));
        sldSolid.setValue((100-(int) (c.getAlpha()/255f*100)));
        c=ps.getGradientStart();
        btnGradientStart.setBackground(new Color(c.getRed(),c.getGreen(),c.getBlue()));
        btnGradientStart.setForeground(getProperColor(btnGradientStart.getBackground()));
        sldGradientStart.setValue((100-(int) (c.getAlpha()/255f*100)));
        c=ps.getGradientEnd();
        btnGradientEnd.setBackground(new Color(c.getRed(),c.getGreen(),c.getBlue()));
        btnGradientEnd.setForeground(getProperColor(btnGradientEnd.getBackground()));
        sldGradientEnd.setValue((100-(int) (c.getAlpha()/255f*100)));
        txtWidth.setValidChars(JNumberField.DECIMAL_DIGITS);
        txtWidth.setText(""+ps.getWidth());
    }
    /**
     * Applies the changes done to the path segment.
     */
    private void applyChanges() {
        ps.setName(txtName.getText());
        Color c;
        c=btnSolid.getBackground();
        ps.setSolidColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)((100-sldSolid.getValue())*255f/100)));
        c=btnGradientStart.getBackground();
        ps.setGradientStart(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)((100-sldGradientStart.getValue())*255f/100)));
        c=btnGradientEnd.getBackground();
        ps.setGradientEnd(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)((100-sldGradientEnd.getValue())*255f/100)));
        if (rbnDotted.isSelected())
            ps.setStrokeAs(PathSegment.STROKE_AS_DOTTED_LINE);
        else
            ps.setStrokeAs(PathSegment.STROKE_AS_STRAIGHT_LINE);
        if (rbnGradientColor.isSelected())
            ps.setPaintAs(PathSegment.PAINT_AS_GRADIENT_COLOR);
        else
            ps.setPaintAs(PathSegment.PAINT_AS_SOLID_COLOR);
        ps.setWidth(txtWidth.getinteger());
        ((Agent) agent).plugHandler.repaintTrail();
    }
    /**
     * Calculates a proper foreground color for the given background color.
     */
    private Color getProperColor(Color c) {
        if ((c.getRed()+c.getGreen()+c.getBlue())/3>128)
            return Color.black;
        else
            return Color.white;
    }

    void jbInit() throws Exception {
        lblStroke.setText("Stroke segment as:");
        this.getContentPane().setLayout(smartLayout1);
        rbnStraightLine.setOpaque(false);
        rbnStraightLine.setText("Straight line");
        rbnDotted.setOpaque(false);
        rbnDotted.setText("Dottted line");
        lblPaintAs.setText("Paint segment with:");
        rbnSolidColor.setOpaque(false);
        rbnSolidColor.setText("One, solid color.");
        rbnGradientColor.setOpaque(false);
        rbnGradientColor.setText("Gradient color");
        btnSolid.setText("Define");
        btnGradientStart.setText("Define");
        sldSolid.setOpaque(false);
        lblGradientStart.setText("Gradient start");
        lblGradEnd.setText("Gradient end");
        btnGradientEnd.setText("Define");
        sldGradientStart.setOpaque(false);
        sldGradientEnd.setOpaque(false);
        pnlPreview.setBackground(Color.white);
        pnlPreview.setBorder(BorderFactory.createLineBorder(Color.black));
        btnOK.setText("OK");
        btnCancel.setText("Cancel");
        lblName.setText("Name");
        txtName.setText("Journey from Greece to Italy");
        btnApply.setText("Apply");
        lblWidth.setText("of width");
        this.getContentPane().add(lblStroke, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(txtName, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(rbnStraightLine, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.EdgeAnchor(lblStroke, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(rbnDotted, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.EdgeAnchor(rbnStraightLine, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(lblPaintAs, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(txtWidth, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(rbnSolidColor, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.EdgeAnchor(lblPaintAs, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(rbnGradientColor, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.EdgeAnchor(btnSolid, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(sldSolid, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnSolid, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(btnSolid, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(btnSolid, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(rbnSolidColor, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(sldSolid, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 30, 1.0)));
        this.getContentPane().add(btnGradientStart, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblGradientStart, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(sldSolid, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.25, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 30, 1.0)));
        this.getContentPane().add(lblGradientStart, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(rbnGradientColor, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.25, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(lblGradEnd, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblGradientStart, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.75, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(btnGradientEnd, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnGradientStart, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(sldSolid, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.75, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 30, 1.0)));
        this.getContentPane().add(sldGradientStart, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnGradientStart, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(btnGradientStart, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(btnGradientStart, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(sldGradientEnd, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(btnGradientEnd, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(btnGradientEnd, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(btnGradientEnd, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(pnlPreview, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 20),
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 20),
					new com.thwt.layout.EdgeAnchor(sldGradientStart, Anchor.Bottom, Anchor.Below, Anchor.Top, 10)));
        this.getContentPane().add(btnCancel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(lblName, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 5),
					new com.thwt.layout.EdgeAnchor(txtName, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 3, 1.0)));
        this.getContentPane().add(txtName, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 4),
					new com.thwt.layout.EdgeAnchor(lblName, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(btnOK, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.EdgeAnchor(btnCancel, Anchor.Left, Anchor.Left, Anchor.Right, 20),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(btnApply, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.EdgeAnchor(btnCancel, Anchor.Right, Anchor.Right, Anchor.Left, 20),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.getContentPane().add(lblWidth, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(rbnDotted, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(txtWidth, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
        this.getContentPane().add(txtWidth, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 60),
					new com.thwt.layout.EdgeAnchor(lblWidth, Anchor.Right, Anchor.Right, Anchor.Left, 10),
					new com.thwt.layout.EdgeAnchor(rbnDotted, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
    }

    private PathSegment ps;
    //UI members
    private SmartLayout smartLayout1 = new SmartLayout();
    private JLabel lblStroke = new JLabel();
    private JRadioButton rbnStraightLine = new JRadioButton();
    private JRadioButton rbnDotted = new JRadioButton();
    private JLabel lblPaintAs = new JLabel();
    private JRadioButton rbnSolidColor = new JRadioButton();
    private JRadioButton rbnGradientColor = new JRadioButton();
    private MyJSlider sldSolid = new MyJSlider();
    private JButton btnSolid = new JButton();
    private JButton btnGradientStart = new JButton();
    private JLabel lblGradientStart = new JLabel();
    private JLabel lblGradEnd = new JLabel();
    private JButton btnGradientEnd = new JButton();
    private MyJSlider sldGradientStart = new MyJSlider();
    private MyJSlider sldGradientEnd = new MyJSlider();
    private JPanel pnlPreview = new JPanel() {
        public void paintComponent(Graphics g) {
            int w=getWidth(); int h=getHeight();
            Graphics2D g2=(Graphics2D) g;
            g2.setPaint(Color.white);
            g2.fillRect(0,0,w,h);
            Color c=btnGradientStart.getBackground();
            Color start=new Color(c.getRed(),c.getGreen(),c.getBlue(),(100-sldGradientStart.getValue())*255/100);
            c=btnGradientEnd.getBackground();
            Color end=new Color(c.getRed(),c.getGreen(),c.getBlue(),(100-sldGradientEnd.getValue())*255/100);
            g2.setPaint(new GradientPaint(0,h/2,start,w,h/2,end));
            g2.fillRect(0,0,w,h);
        }
    };
    private JButton btnOK = new JButton();
    private JButton btnCancel = new JButton();
    private JLabel lblName = new JLabel();
    private JTextField txtName = new JTextField();
    private JButton btnApply = new JButton();
    private JLabel lblWidth = new JLabel();
    private JNumberField txtWidth = new JNumberField(2);
    private IAgent agent;

    /**
     * Implements the ActionListener for the color buttons.
     */
    private class ColorActionListener implements ActionListener {
        private JButton button;
        ColorActionListener(JButton button) {
            super();
            this.button=button;
        }
        public void actionPerformed(ActionEvent e) {
            Color cl=JColorChooser.showDialog(PathSegmentProperties.this,Agent.bundleSegmentProperties.getString("colors"),button.getBackground());
            if (cl!=null) {
                button.setBackground(cl);
                button.setForeground(getProperColor(cl));
                pnlPreview.repaint();
            }
        }
    };
    /**
     * Version of a JSlider that paints its value as a percentage in the middle.
     */
    private class MyJSlider extends JSlider {
        MyJSlider() {
            super();
            setRequestFocusEnabled(false);
            addChangeListener(new AlphaChangeListener(this));
            addMouseListener(new AlphaMouseListener(this));
            setValue(0);
            setToolTipText(Agent.bundleSegmentProperties.getString("transparency")+": "+getValue()+"%");
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            String mes=getValue()+" %";
            int mw=getFontMetrics(getFont()).stringWidth(mes);
            int mh=getFontMetrics(getFont()).getAscent();
            g.setColor(new Color(255,255,255,175));
            g.fillRect((getWidth()-mw)/2,(getHeight()-mh)/2,mw,mh);
            g.setColor(Color.black);
            g.drawString(mes,(getWidth()-mw)/2,(getHeight()+mh)/2-2);
        }
    };
    /**
     * Implements the ChangeListener for the alpha sliders.
     */
    private class AlphaChangeListener implements ChangeListener {
        private JSlider slider;
        AlphaChangeListener(JSlider slider) {
            super();
            this.slider=slider;
        }
        public void stateChanged(ChangeEvent e) {
            slider.setToolTipText(Agent.bundleSegmentProperties.getString("transparency")+": "+slider.getValue()+"%");
            slider.repaint();
        }
    };
    private class AlphaMouseListener extends MouseAdapter {
        private JSlider slider;
        private int initial;
        private int reshow;
        AlphaMouseListener(JSlider slider) {
            super();
            this.slider=slider;
        }
        public void mouseEntered(MouseEvent e) {
            initial=ToolTipManager.sharedInstance().getInitialDelay();
            reshow=ToolTipManager.sharedInstance().getReshowDelay();
            slider.setToolTipText(Agent.bundleSegmentProperties.getString("transparency")+": "+slider.getValue()+"%");
            ToolTipManager.sharedInstance().setInitialDelay(1);
            ToolTipManager.sharedInstance().setReshowDelay(1);
        }
        public void mouseExited(MouseEvent e) {
            ToolTipManager.sharedInstance().setInitialDelay(initial);
            ToolTipManager.sharedInstance().setReshowDelay(reshow);
        }
    };
}

