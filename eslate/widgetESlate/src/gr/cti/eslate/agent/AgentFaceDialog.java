package gr.cti.eslate.agent;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

public class AgentFaceDialog extends JDialog {

    public AgentFaceDialog(Agent a) {
        super((Frame) SwingUtilities.getAncestorOfClass(Frame.class,a),AgentBeanInfo.bundle.getString("definephases"),true);
        setSize(450,370);
        setContentPane(new AgentFaceWizard(a));
    }
}
