//Title:        Stage
//Version:      
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage.constraints.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class ConstraintInfoDialog extends JDialog
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  JPanel panel1 = new JPanel();
  JList jList1 = new JList();
  BorderLayout borderLayout1 = new BorderLayout();
  JToolBar jToolBar1 = new JToolBar();
  JButton jButton2 = new JButton();
  JButton jButton3 = new JButton();
  Component component1;
  JButton jButton1 = new JButton();
  TitledBorder titledBorder1;

  public ConstraintInfoDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public ConstraintInfoDialog() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    component1 = Box.createVerticalStrut(8);
    titledBorder1 = new TitledBorder("");
    panel1.setLayout(borderLayout1);
    jToolBar1.setOrientation(JToolBar.VERTICAL);
    jButton2.setText("jButton2");
    jButton3.setText("jButton3");
    jButton1.setText("jButton1");
    jList1.setBorder(titledBorder1);
    titledBorder1.setTitle("Members:");
    getContentPane().add(panel1);
    panel1.add(jList1, BorderLayout.CENTER);
    panel1.add(jToolBar1, BorderLayout.EAST);
    jToolBar1.add(jButton3, null);
    jToolBar1.add(jButton2, null);
    jToolBar1.add(component1, null);
    jToolBar1.add(jButton1, null);
  }
}
 
