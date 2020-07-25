//Title:        Physics E-Slate component's ToolBar
//Version:      1.0 (23-3-1999)
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Your description


package gr.cti.eslate.stage;

import java.awt.*;
import javax.swing.*;
import gr.cti.utils.*;

/**
 * @version     2.0.0, 26-May-2006
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class MyToolBar extends JToolBar
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  public MyToolBar(){
   super();
   setFloatable(false);
  }

  public JButton add(JButton b){
   Res.setupButton(b,b.getText());

   b.setMaximumSize(new Dimension(23,23));
   b.setPreferredSize(new Dimension(23,23));

   b.setMargin(new Insets(0,0,0,0));
   //b.setFocusPainted(false); //27-8-1998: don't show focus rectangle
   b.setRequestFocusEnabled(false); //2-9-1998

   super.add(b); //use ancestor's add, not ours! (else we'll get an infinite loop)
   return b;
  }

  public JButton add(Action a){ //overriding add2(Action) to customize the buttons' appearance (remove text labels etc.)
   JButton b=super.add(a);
   b.setToolTipText(b.getText()); //should use the SHORT_DESCRIPTION(?)
   b.setText(""); //don't show text label

   b.setMaximumSize(new Dimension(23,23));
   b.setPreferredSize(new Dimension(23,23));

   b.setMargin(new Insets(0,0,0,0));
   //b.setFocusPainted(false); //27-8-1998: don't show focus rectangle
   b.setRequestFocusEnabled(false); //2-9-1998
   return b;
  }

  public JToggleButton add(ToggleAction a){ //overriding add2(Action) to customize the buttons' appearance (remove text labels etc.)
   JToggleButton b=(JToggleButton)super.add(new ToggleActionButton(a));
   b.setToolTipText(b.getText()); //should use the SHORT_DESCRIPTION(?)
   b.setText(""); //don't show text label

   b.setMaximumSize(new Dimension(23,23));
   b.setPreferredSize(new Dimension(23,23));

   b.setMargin(new Insets(0,0,0,0));
   //b.setFocusPainted(false); //27-8-1998: don't show focus rectangle
   b.setRequestFocusEnabled(false); //2-9-1998
   return b;
  }

}
