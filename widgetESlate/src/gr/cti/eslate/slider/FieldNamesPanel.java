//Title:        Slider
//Version:
//Copyright:    Copyright (c) 1999
//Author:       George Birbilis
//Company:      CTI
//Description:  FieldNamesPanel

package gr.cti.eslate.slider;

import java.awt.Color;

import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;

import com.helplets.awt.PercentLayout;

public class FieldNamesPanel extends JPanel implements SwingConstants{ //19Aug1999: made separate class
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  public FieldNamesPanel(JLabel varLabel,JLabel fromLabel,JLabel toLabel,JLabel stepLabel) {
   super();
   setBackground(Color.red); //16-7-1998

//   setBorder(new BevelBorder(BevelBorder.LOWERED));
   setBorder(new BevelBorder(BevelBorder.RAISED));

   setLayout(new PercentLayout()); //!!!

   varLabel.setForeground(Color.white); //16-7-1998
   fromLabel.setForeground(Color.white); //16-7-1998
   toLabel.setForeground(Color.white); //16-7-1998
   stepLabel.setForeground(Color.white); //16-7-1998

   varLabel.setHorizontalAlignment(RIGHT); //29-9-1998

   add("right=14",varLabel); //16-7-1988: added "right" to avoid collisions between "varLabel" and "fromLabel" //29-9-1998:removed "left=0,"
   add("left=15",fromLabel);
   add("left=70",toLabel);
   add("left=85",stepLabel);
  }

} //END-CLASS(FieldNamesPanel)

