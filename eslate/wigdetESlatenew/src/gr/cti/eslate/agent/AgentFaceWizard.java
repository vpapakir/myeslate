package gr.cti.eslate.agent;

import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class AgentFaceWizard extends JPanel {

    AgentFaceWizard(Agent a) {
        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        this.agent=a;

        //Localization
        btnPrevious.setText(AgentBeanInfo.bundle.getString("previous"));
        btnNext.setText(AgentBeanInfo.bundle.getString("next"));
        btnCancel.setText(AgentBeanInfo.bundle.getString("cancel"));

        //Initialization
        step1=new AgentFaceWizardStep1(a);
        step1.setPreferredSize(new Dimension(100,100));
        step2=new AgentFaceWizardStep2(a);
        step2.setPreferredSize(new Dimension(100,100));
        step31=new AgentFaceWizardStep31(a);
        step31.setPreferredSize(new Dimension(100,100));
        step32=new AgentFaceWizardStep32(a);
        step32.setPreferredSize(new Dimension(100,100));

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });

        setStep(1);
    }

    private class NextStep1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setCursor(Helpers.waitCursor);
            setStep(2);
            setCursor(Helpers.normalCursor);
        }
    };

    private class NextStep2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setCursor(Helpers.waitCursor);
            if (step1.getSelection()==step1.AUTOMATIC)
                setStep(31);
            else
                setStep(32);
            setCursor(Helpers.normalCursor);
        }
    };

    private class NextStep31 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setCursor(Helpers.waitCursor);
            if (step31.getIcon()!=null) {
                agent.clearPhases();
                agent.setNumberOfPhases(step2.getPhases());
                agent.setAutomaticallyProducePhases(true);
                Icon[] icons=Helpers.producePhases(AgentFaceWizard.this,step31.getIcon(),step2.getPhases());
                if (step2.getPhases()==1)
                    //0 and 360 are the same normalized angle. Trick!
                    agent.setPhaseIcon(0,360-1E-6,icons[0]);
                else {
                    double angleStep=360d/step2.getPhases();
                    double startAngle=-(angleStep/2);
                    for (int i=0;i<icons.length;i++) {
                        agent.setPhaseIcon(startAngle,startAngle+angleStep,icons[i]);
                        startAngle+=angleStep;
                    }
                }
            }
            closeDialog();
            setCursor(Helpers.normalCursor);
        }
    };

    private class NextStep32 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setCursor(Helpers.waitCursor);
            TableModel icons=step32.getIcons();
            agent.clearPhases();
            agent.setNumberOfPhases(step2.getPhases());
            agent.setAutomaticallyProducePhases(false);
            if (step2.getPhases()==1)
                //0 and 360 are the save normalized angle. Trick!
                agent.setPhaseIcon(0,360-1E-6,(Icon) icons.getValueAt(0,1));
            else {
                double angleStep=360d/step2.getPhases();
                double startAngle=-(angleStep/2);
                for (int i=0;i<icons.getRowCount();i++) {
                    Object icn=icons.getValueAt(i,1);
	                if (icn instanceof Icon)
	                    agent.setPhaseIcon(startAngle,startAngle+angleStep,(Icon) icn);
	                else
	                    agent.setPhaseIcon(startAngle,startAngle+angleStep,new NewRestorableImageIcon((Image) icn));
                    startAngle+=angleStep;
                }
            }
            closeDialog();
            setCursor(Helpers.normalCursor);
        }
    };

    private class PreviousStep2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setCursor(Helpers.waitCursor);
            setStep(1);
            setCursor(Helpers.normalCursor);
        }
    };

    private class PreviousStep3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setCursor(Helpers.waitCursor);
            setStep(2);
            setCursor(Helpers.normalCursor);
        }
    };

    private void setStep(int i) {
        switch (i) {
            case 1:
                scrDesc.setViewportView(step1);
                btnPrevious.setVisible(false);
                revalidate();
                btnNext.removeActionListener(nextListener);
                nextListener=new NextStep1();
                btnNext.addActionListener(nextListener);
                btnPrevious.removeActionListener(previousListener);
                previousListener=null;
                break;
            case 2:
                scrDesc.setViewportView(step2);
                btnPrevious.setVisible(true);
                btnNext.setText(AgentBeanInfo.bundle.getString("next"));
                revalidate();
                btnNext.removeActionListener(nextListener);
                nextListener=new NextStep2();
                btnNext.addActionListener(nextListener);
                btnPrevious.removeActionListener(previousListener);
                previousListener=new PreviousStep2();
                btnPrevious.addActionListener(previousListener);
                break;
            case 31:
                scrDesc.setViewportView(step31);
                btnNext.setText(AgentBeanInfo.bundle.getString("finish"));
                revalidate();
                btnNext.removeActionListener(nextListener);
                nextListener=new NextStep31();
                btnNext.addActionListener(nextListener);
                btnPrevious.removeActionListener(previousListener);
                previousListener=new PreviousStep3();
                btnPrevious.addActionListener(previousListener);
                break;
            case 32:
                step32.setSlices(step2.getPhases());
                scrDesc.setViewportView(step32);
                btnNext.setText(AgentBeanInfo.bundle.getString("finish"));
                revalidate();
                btnNext.removeActionListener(nextListener);
                nextListener=new NextStep32();
                btnNext.addActionListener(nextListener);
                btnPrevious.removeActionListener(previousListener);
                previousListener=new PreviousStep3();
                btnPrevious.addActionListener(previousListener);
                break;
        }
    }

    private void closeDialog() {
        //Close the dialog
        getParent().getParent().getParent().setVisible(false);
    }

    private void jbInit() throws Exception {
        this.setLayout(smartLayout1);
        btnCancel.setText("Cancel");
        btnNext.setText("Next >");
        scrDesc.setBorder(BorderFactory.createLoweredBevelBorder());
        scrDesc.setOpaque(false);
        lblWizard.setHorizontalAlignment(SwingConstants.CENTER);
        lblWizard.setIcon(new ImageIcon(gr.cti.eslate.agent.AgentFaceWizard.class.getResource("images/wizard.gif")));
        btnPrevious.setText("< Previous");
        this.add(lblWizard, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 1),
					new com.thwt.layout.EdgeAnchor(scrDesc, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(scrDesc, Anchor.Height, Anchor.Same, Anchor.Height, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 25, 1.0)));
        this.add(btnCancel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 10),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.add(btnNext, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.EdgeAnchor(btnCancel, Anchor.Left, Anchor.Left, Anchor.Right, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        this.add(scrDesc, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 3),
					new com.thwt.layout.EdgeAnchor(lblWizard, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(btnCancel, Anchor.Top, Anchor.Above, Anchor.Bottom, 10)));
        this.add(btnPrevious, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.EdgeAnchor(btnNext, Anchor.Left, Anchor.Left, Anchor.Right, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
    }

    private Agent agent;
    //UI elements
    private AgentFaceWizardStep1 step1;
    private AgentFaceWizardStep2 step2;
    private AgentFaceWizardStep31 step31;
    private AgentFaceWizardStep32 step32;
    private ActionListener nextListener,previousListener;
    //UI by JBuilder
    private SmartLayout smartLayout1 = new SmartLayout();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JButton btnCancel = new JButton();
    private JButton btnNext = new JButton();
    private JScrollPane scrDesc = new JScrollPane();
    private JLabel lblWizard = new JLabel();
    private JButton btnPrevious = new JButton();
}