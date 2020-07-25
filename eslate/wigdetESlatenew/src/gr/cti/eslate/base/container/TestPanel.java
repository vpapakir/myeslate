package gr.cti.eslate.base.container;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import gr.cti.eslate.base.*;

/**
 * Quick &amp; dirty hack to make it possible to test the
 * PerformanceManager's GUI without waiting for it to be incorporated in the
 * E-Slate desktop.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class TestPanel extends JPanel implements ESlatePart, Externalizable
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private transient ESlateHandle h;
  private transient
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
  private transient boolean initted = false;

  public ESlateHandle getESlateHandle()
  {
    return h;
  }

  public TestPanel()
  {
    super();
    setLayout(new BorderLayout());
    pm.setEnabled(true);
    h = ESlate.registerPart(this);
    setPreferredSize(new Dimension(300, 300));

    JPanel panel = new JPanel();
    JButton getButton = new JButton("Save");
    getButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        try {
          FileOutputStream fos = new FileOutputStream("C:\\temp\\foo");
          ObjectOutputStream oos = new ObjectOutputStream(fos);
          oos.writeObject(pm.getState());
          oos.close();
        } catch (Exception ioe) {
          ioe.printStackTrace();
        }
      }
    });
    panel.add(getButton);
    JButton setButton = new JButton("Load");
    setButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        try {
          FileInputStream fis = new FileInputStream("C:\\temp\\foo");
          ObjectInputStream ois = new ObjectInputStream(fis);
          pm.setState((PerformanceManagerState)(ois.readObject()));
          ois.close();
        } catch (Exception ioe) {
          ioe.printStackTrace();
        }
      }
    });
    panel.add(setButton);
    add(panel, BorderLayout.NORTH);
  }

  public void paint(Graphics g)
  {
    if (!initted) {
      pm.setESlateMicroworld(h.getESlateMicroworld());
      add(pm.getGUI(), BorderLayout.CENTER);
      initted = true;
    }
    super.paint(g);
  }

  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
  }

  public void writeExternal(ObjectOutput oo) throws IOException
  {
  }
}
