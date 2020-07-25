package gr.cti.eslate.agent;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class AgentFaceWizardStep1 extends JPanel {

    AgentFaceWizardStep1(Agent a) {
        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        //Initialization
        ButtonGroup bg=new ButtonGroup();
        bg.add(rbnChoice1);
        bg.add(rbnChoice2);
        Color tr=new Color(0,0,0,0);
        txtDesc.setSelectedTextColor(txtDesc.getForeground());
        txtDesc.setSelectionColor(tr);
        txtChoice1.setSelectedTextColor(txtChoice1.getForeground());
        txtChoice1.setSelectionColor(tr);
        txtChoice2.setSelectedTextColor(txtChoice2.getForeground());
        txtChoice2.setSelectionColor(tr);
        if (a.getAutomaticallyProducePhases())
            rbnChoice1.setSelected(true);
        else
            rbnChoice2.setSelected(true);

        //Localization
        txtDesc.setText(AgentBeanInfo.bundle.getString("step1desc"));
        txtChoice1.setText(AgentBeanInfo.bundle.getString("step1choice1"));
        txtChoice2.setText(AgentBeanInfo.bundle.getString("step1choice2"));

        //Listeners
        txtChoice1.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                rbnChoice1.setSelected(true);
            }
        });
        txtChoice2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                rbnChoice2.setSelected(true);
            }
        });
    }

    protected int getSelection() {
        if (rbnChoice1.isSelected())
            return AUTOMATIC;
        else
            return MANUAL;
    }

    private void jbInit() throws Exception {
        this.setLayout(smartLayout2);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setOpaque(false);
        txtDesc.setText("This area contains the description of the step");
        txtDesc.setEditable(false);
        rbnChoice1.setOpaque(false);
        txtChoice1.setLineWrap(true);
        txtChoice1.setWrapStyleWord(true);
        txtChoice1.setOpaque(false);
        txtChoice1.setText("First choice");
        txtChoice1.setEditable(false);
        rbnChoice2.setOpaque(false);
        txtChoice2.setLineWrap(true);
        txtChoice2.setWrapStyleWord(true);
        txtChoice2.setOpaque(false);
        txtChoice2.setText("Second choice");
        txtChoice2.setEditable(false);
        this.add(txtDesc, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.add(rbnChoice1, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.EdgeAnchor(txtChoice1, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(txtChoice1, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
        this.add(txtChoice1, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(rbnChoice1, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(txtDesc, Anchor.Bottom, Anchor.Below, Anchor.Top, 20),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.add(rbnChoice2, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 20),
					new com.thwt.layout.EdgeAnchor(txtChoice2, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(txtChoice2, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
        this.add(txtChoice2, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(rbnChoice2, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(txtChoice1, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
    }

    protected static final int AUTOMATIC=1;
    protected static final int MANUAL=2;

    private SmartLayout smartLayout2 = new SmartLayout();
    private LargeFontJTextArea txtDesc = new LargeFontJTextArea();
    private JRadioButton rbnChoice1 = new JRadioButton();
    private LargeFontJTextArea txtChoice1 = new LargeFontJTextArea();
    private JRadioButton rbnChoice2 = new JRadioButton();
    private LargeFontJTextArea txtChoice2 = new LargeFontJTextArea();
}
