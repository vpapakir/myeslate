package gr.cti.eslate.agent;

import java.awt.Color;

import javax.swing.JPanel;
import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class AgentFaceWizardStep2 extends JPanel {

    AgentFaceWizardStep2(Agent a) {
        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        //Initialization
        Color tr=new Color(0,0,0,0);
        txtDesc.setSelectedTextColor(txtDesc.getForeground());
        txtDesc.setSelectionColor(tr);
        spnNumber.setValue(a.getNumberOfPhases());

        //Localization
        txtDesc.setText(AgentBeanInfo.bundle.getString("step2desc"));
    }

    protected int getPhases() {
        return spnNumber.getValue();
    }

    private void jbInit() throws Exception {
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setOpaque(false);
        txtDesc.setText("jTextArea1");
        this.setLayout(smartLayout1);
        this.add(txtDesc, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.add(spnNumber, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(txtDesc, Anchor.Bottom, Anchor.Below, Anchor.Top, 30),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 50, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -6, 1.0)));
    }

    private SmartLayout smartLayout1 = new SmartLayout();
    private LargeFontJTextArea txtDesc = new LargeFontJTextArea();
    private SpinButton spnNumber = new SpinButton(1,1,360);
}
