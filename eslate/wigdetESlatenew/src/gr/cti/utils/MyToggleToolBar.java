//Title:        Planar E-Slate component's ToolBar
//Version:      1.0 (17-2-1999)
//Copyright:    Copyright (c) 1999
//Author:       George Birbilis
//Company:      CTI
//Description:  Your description


package gr.cti.utils;

import java.awt.*;
import javax.swing.*;

public class MyToggleToolBar extends JToggleToolBar //this now extends JToggleToolbar to allow only one ToggleButton to be pressed at a time
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  public MyToggleToolBar(){
   super();
   setFloatable(false);
  }

  public JButton add(Action a){ //overriding add2(Action) to customize the buttons' appearance (remove text labels etc.)
   JButton b=super.add(a);
   b.setToolTipText(b.getText()); //should use the SHORT_DESCRIPTION(?)
   b.setText(""); //don't show text label

   b.setMaximumSize(new Dimension(23,21));
   b.setPreferredSize(new Dimension(23,21));

   b.setMargin(new Insets(0,0,0,0));
   b.setFocusPainted(false); //27-8-1998: don't show focus rectangle
   b.setRequestFocusEnabled(false); //2-9-1998
   return b;
  }

  public AbstractButton/*JToggleButton*/ add(ToggleAction a){ //overriding add2(Action) to customize the buttons' appearance (remove text labels etc.) //28Jul1999: changed to return AbstactButton instead of JToggleButton cause Java2 compiler showed this as an error 
   JToggleButton b=(JToggleButton)super.add(new ToggleActionButton(a));
   b.setToolTipText(b.getText()); //should use the SHORT_DESCRIPTION(?)
   b.setText(""); //don't show text label

   b.setMaximumSize(new Dimension(23,23));
   b.setPreferredSize(new Dimension(23,23));

   b.setMargin(new Insets(0,0,0,0));
   b.setFocusPainted(false); //27-8-1998: don't show focus rectangle
   b.setRequestFocusEnabled(false); //2-9-1998
   return b;
  }

}
