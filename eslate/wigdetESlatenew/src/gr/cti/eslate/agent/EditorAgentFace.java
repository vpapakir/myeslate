package gr.cti.eslate.agent;

import gr.cti.eslate.utils.NoBorderButton;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyEditorSupport;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

public class EditorAgentFace extends PropertyEditorSupport {
    private NoBorderButton button;

    public EditorAgentFace() {
        super();
        button=new NoBorderButton() {
            public Dimension getPreferredSize() {
                ///Calculate icon height///
                int ySize=1;
                FontMetrics fm=button.getFontMetrics(button.getFont());
                //Base label
                ySize+=fm.getHeight()+1;
                //Base icon
                Dimension fs=getAValue().getFaceSize(0); //0 degrees
                ySize+=fs.height+1;
                //Faces label
                ySize+=fm.getHeight()+1;
                //Description label
                ySize+=fm.getHeight()+1;
                return new Dimension(1,ySize);
            }
        };
        button.setFont(new Font(button.getFont().getName(),button.getFont().getStyle(),button.getFont().getSize()));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgentFaceDialog d=new AgentFaceDialog(getAValue());
                Point p=new Point(button.getX(),button.getY());
                SwingUtilities.convertPointToScreen(p,button.getParent());
                d.setLocation(p.x,p.y);
                d.setVisible(true);
                getAValue().callRepaintOnHosts();
                paintIcon();
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                paintIcon();
            }
        });
    }
    /**
     * Paints the icon displayed on the button.
     */
    private void paintIcon() {
        ///Calculate icon size///
        int maxX; int ySize=1;
        FontMetrics fm=button.getFontMetrics(button.getFont());
        //Base label
        String baseImage=AgentBeanInfo.bundle.getString("phasebase");
        int baseImagew=fm.stringWidth(baseImage);
        maxX=baseImagew;
        ySize+=fm.getHeight()+1;
        //Base icon
        Dimension fs=getAValue().getFaceSize(0); //0 degrees
        maxX=Math.max(fs.width,maxX);
        ySize+=fs.height+1;
        //Faces label
        String faces=AgentBeanInfo.bundle.getString("phases")+": "+getAValue().getNumberOfPhases();
        int facesw=fm.stringWidth(faces);
        maxX=Math.max(facesw,maxX);
        ySize+=fm.getHeight()+1;
        //Description label
        String desc;
        if (getAValue().getAutomaticallyProducePhases())
            desc=AgentBeanInfo.bundle.getString("faceauto");
        else
            desc=AgentBeanInfo.bundle.getString("facemanual");
        int descw=fm.stringWidth(desc);
        maxX=Math.max(descw,maxX);
        ySize+=fm.getHeight()+1;

        ///Paint image///
        int yPos=0;
        maxX+=2;
        BufferedImage im=new BufferedImage(maxX,ySize,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2=(Graphics2D) im.getGraphics();
        g2.setFont(button.getFont());
        g2.setPaint(button.getForeground());
        //Faces label
        g2.drawString(faces,(maxX-facesw)/2,yPos+fm.getAscent());
        yPos+=fm.getHeight()+1;
        //Base label
        g2.drawString(baseImage,(maxX-baseImagew)/2,yPos+fm.getAscent());
        yPos+=fm.getHeight()+1;
        //Base icon
        BufferedImage fim=new BufferedImage(fs.width,fs.height,BufferedImage.TYPE_INT_ARGB);
        getAValue().paintFace(fim.getGraphics(),0); //0 degrees
        g2.drawImage(fim,(maxX-fs.width)/2,yPos,null);
        yPos+=fs.height+1;
        //Description label
        g2.drawString(desc,(maxX-descw)/2,yPos+fm.getAscent());

        button.setIcon(new ImageIcon(im));
        button.invalidate();
        button.repaint();
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