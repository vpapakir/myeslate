package gr.cti.eslate.agent;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

/**
 * A spin button.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 09-May-2000
 */
public class SpinButton extends JComponent {
    /**
     * Construct a spin button with no up and down limits.
     */
    public SpinButton() {
        try  {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        txtNumber.setValidChars(JNumberField.DECIMAL_DIGITS);
        txtNumber.setText("0");
        txtNumber.setHorizontalAlignment(txtNumber.RIGHT);
        txtNumber.setMaxDigits(4);
        txtNumber.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (txtNumber.getText().equals(""))
                    setValue(downLimit);
                else if (getValue()>upLimit)
                    setValue(upLimit);
                else if (getValue()<downLimit)
                    setValue(downLimit);
            }
        });

        btnDown.setFocusPainted(false);
        btnDown.setRequestFocusEnabled(false);
        btnUp.setFocusPainted(false);
        btnUp.setRequestFocusEnabled(false);

        upLimit=Integer.MAX_VALUE;
        downLimit=-upLimit;

        btnUp.addMouseListener(new MouseAdapter() {
            SpinUpThread it;
            public void mousePressed(MouseEvent e) {
                increase();
                it=new SpinUpThread();
                it.start();
            }
            public void mouseReleased(MouseEvent e) {
                if (it!=null) {
                    it.stopped=true;
                    it=null;
                }
            }
        });

        btnDown.addMouseListener(new MouseAdapter() {
            SpinDownThread it;
            public void mousePressed(MouseEvent e) {
                decrease();
                it=new SpinDownThread();
                it.start();
            }
            public void mouseReleased(MouseEvent e) {
                if (it!=null) {
                    it.stopped=true;
                    it=null;
                }
            }
        });
    }
    /**
     * Construct a spin button with no up and down limits and <em>init</em> initial value.
     */
    public SpinButton(int init) {
        this();
        txtNumber.setText(""+init);
    }
    /**
     * Construct a spin button with <em>upLimit</em> up limit, <em>downLimit</em> down limit and <em>init</em> initial value.
     */
    public SpinButton(int init,int downLimit,int upLimit) {
        this();
        txtNumber.setText(""+init);
        this.downLimit=downLimit;
        this.upLimit=upLimit;
    }
    /**
     * Increases the spin value by one.
     */
    public void increase() {
        int i=getValue();
        if (i<upLimit)
            txtNumber.setText(""+(i+1));
        else if (i>upLimit)
            setValue(upLimit);
    }
    /**
     * Decreases the spin value by one.
     */
    public void decrease() {
        int i=getValue();
        if (i>downLimit)
            txtNumber.setText(""+(i-1));
        else if (i<downLimit)
            setValue(downLimit);
    }
    /**
     * Sets the value.
     */
    public void setValue(int i) {
        txtNumber.setText(""+i);
    }
    /**
     * Gets the value.
     * @return  The value shown by the spin button.
     */
    public int getValue() {
        try {
            return (int) txtNumber.getlong();
        } catch(NumberFormatException e) {
            return downLimit;
        }
    }

    public Dimension getPreferredSize() {
        Dimension d1=txtNumber.getPreferredSize();
        Dimension d2=btnUp.getPreferredSize();
        return new Dimension(d1.width+d2.width,d1.height+d2.height);
    }

    private void jbInit() throws Exception {
        btnUp.setIcon(new ImageIcon(gr.cti.eslate.agent.SpinButton.class.getResource("images/up.gif")));
        btnUp.setMargin(new Insets(0, 1, 0, 1));
        this.setLayout(smartLayout1);
        btnDown.setIcon(new ImageIcon(gr.cti.eslate.agent.SpinButton.class.getResource("images/down.gif")));
        btnDown.setMargin(new Insets(0, 1, 0, 1));
        this.add(btnUp, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 0),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.Bottom, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
        this.add(btnDown, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 0),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.Top, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
        this.add(txtNumber, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 0),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 0),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 0),
					new com.thwt.layout.EdgeAnchor(btnDown, Anchor.Left, Anchor.Left, Anchor.Right, 0)));
    }

    private class SpinUpThread extends Thread {
        protected boolean stopped=false;
        public void run() {
            try {
                sleep(500);
            } catch(InterruptedException e) {}

            while (!stopped) {
                increase();
                try {
                    sleep(60);
                } catch(InterruptedException e) {}
            }
        }
    };

    private class SpinDownThread extends Thread {
        protected boolean stopped=false;
        public void run() {
            try {
                sleep(500);
            } catch(InterruptedException e) {}

            while (!stopped) {
                decrease();
                try {
                    sleep(60);
                } catch(InterruptedException e) {}
            }
        }
    };

    //
    private int downLimit,upLimit;
    //UI members
    private SmartLayout smartLayout1 = new SmartLayout();
    private JButton btnUp = new JButton();
    private JButton btnDown = new JButton();
    private JNumberField txtNumber = new JNumberField("0");
}
