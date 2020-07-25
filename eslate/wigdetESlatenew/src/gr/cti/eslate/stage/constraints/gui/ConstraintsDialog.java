//Title:        Stage
//Version:      
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage.constraints.gui;

import java.awt.*;
import javax.swing.*;
import gr.cti.eslate.stage.constraints.models.IConstraintContainer;

public class ConstraintsDialog extends JDialog
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JList jList1 = new JList();
  JToolBar jToolBar1 = new JToolBar();
  JButton btnRemove = new JButton();
  JButton btnInfo = new JButton();

  public ConstraintsDialog(IConstraintContainer container){
   this();
   jList1.setModel(new ConstraintsListModel(container));
  }

  public ConstraintsDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public ConstraintsDialog() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    btnRemove.setAlignmentX((float) 0.5);
    btnRemove.setAlignmentY((float) 0.5);
    btnRemove.setText("Remove");
    btnInfo.setAlignmentX((float) 0.5);
    btnInfo.setAlignmentY((float) 0.5);
    btnInfo.setText("Info");
    jToolBar1.setOrientation(JToolBar.VERTICAL);
    getContentPane().add(panel1);
    panel1.add(jList1, BorderLayout.CENTER);
    panel1.add(jToolBar1, BorderLayout.EAST);
    jToolBar1.add(btnRemove, null);
    jToolBar1.add(btnInfo, null);
  }
}
 
