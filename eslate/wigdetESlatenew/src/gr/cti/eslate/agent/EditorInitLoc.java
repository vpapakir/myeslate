package gr.cti.eslate.agent;

import gr.cti.eslate.utils.NoBorderButton;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * This editor gives the initial position to the agent. It is active
 * only when the agent is not positioned.
 */
public class EditorInitLoc extends PropertyEditorSupport {
    private NoBorderButton button;

    public EditorInitLoc() {
        super();
        button=new NoBorderButton(AgentBeanInfo.bundle.getString("initUndefined"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String longts=JOptionPane.showInputDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,(Component) getValue()),AgentBeanInfo.bundle.getString("initLongt"));
                    if (longts==null)
                        return;
                    double longt=Double.parseDouble(longts);
                    String lats=JOptionPane.showInputDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,(Component) getValue()),AgentBeanInfo.bundle.getString("initLat"));
                    if (lats==null)
                        return;
                    double lat=Double.parseDouble(lats);
                    try {
                        getAValue().setLongLat(longt,lat);
                        button.setText(AgentBeanInfo.bundle.getString("initDefined"));
                        button.setEnabled(false);
                    } catch(AgentRefusesToChangePositionException ex) {
                        JOptionPane.showMessageDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,(Component) getValue()),AgentBeanInfo.bundle.getString("invalidInit"),"",JOptionPane.ERROR_MESSAGE);
                    }
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog((Frame) SwingUtilities.getAncestorOfClass(Frame.class,(Component) getValue()),AgentBeanInfo.bundle.getString("invalidInit"),"",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (getAValue().isPositioned()) {
                    button.setText(AgentBeanInfo.bundle.getString("initDefined"));
                    button.setEnabled(false);
                }
            }
        });
    }
    /**
     * @return The editor is a button which brings up a dialog.
     */
    public Component getCustomEditor() {
        return button;
    }
    /**
     * The original value cast to Agent.
     */
    private Agent getAValue() {
        return (Agent) super.getValue();
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}