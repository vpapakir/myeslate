//Title:        Canvas
//Version:      29Mar2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      CTI
//Description:  Avakeeo's Canvas component

package gr.cti.eslate.canvas.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import gr.cti.eslate.canvas.Res;

/**
 * @version     2.0.5, 8-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class NewPageDialog extends JDialog {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  private boolean canceled;
  JPanel panel1 = new JPanel();
  JButton btnOK = new JButton();
  JButton btnCancel = new JButton();
  IntegerTextField edWidth = new IntegerTextField();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  IntegerTextField edHeight = new IntegerTextField();
  JLabel jLabel3 = new JLabel();
  JTextField edName = new JTextField();
  public static int TURTLE_PAGE = 0;
  public static int DRAWING_PAGE = 1;

/////////////////////

 public String getPageName(){
  String name=edName.getText();
  return (name.equals(""))?null:name;
 }

 public int getPageWidth(){
  try{ return Integer.parseInt(edWidth.getText()); }
  catch(NumberFormatException e){ return 0; }
 }

 public int getPageHeight(){
  try{ return Integer.parseInt(edHeight.getText()); }
  catch(NumberFormatException e){ return 0; }
 }

 public boolean isCanceled(){
  return canceled;
 }

/////////////////////
/*
  private void centerFrameInScreen(Frame frame){ //29Mar2000
   if(frame==null) return; //!!!
   //Center the window
   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   Dimension frameSize = frame.getSize();
   if (frameSize.height > screenSize.height)
    frameSize.height = screenSize.height;
   if (frameSize.width > screenSize.width)
    frameSize.width = screenSize.width;
   frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
  }
*/
  public NewPageDialog(Frame frame, String title, int type, boolean modal) {
    super(frame, title, modal);

    canceled=true;
    try  {
      jbInit(type);
      pack();

      //centerFrameInScreen(frame); //29Mar2000
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public NewPageDialog(Frame owner, int type) {
    //this(new JFrame(), "", false); //don't pass null: need a frame reference in order to center it in the screen
    this(owner, "", type, false); //don't pass null: need a frame reference in order to center it in the screen
  }

/////////////////////
  @SuppressWarnings(value={"deprecation"})
  void jbInit(int type) throws Exception {
    panel1.setLayout(null);

    btnOK.setBounds(new Rectangle(57, 119, 87, 23));
    btnOK.setNextFocusableComponent(btnCancel);
    btnOK.setText(Res.localize("OK"));
    btnOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnOK_actionPerformed(e);
      }
    });

    btnCancel.setBounds(new Rectangle(185, 119, 87, 23));
    btnCancel.setNextFocusableComponent(edName);
    btnCancel.setText(Res.localize("Cancel"));
    btnCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnCancel_actionPerformed(e);
      }
    });

    edWidth.setBounds(new Rectangle(141, 47, 155, 22));
    edWidth.setNextFocusableComponent(edHeight);

    jLabel1.setBounds(new Rectangle(4, 8, 129, 17));
    jLabel1.setRequestFocusEnabled(false);
    jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel1.setText(Res.localize("Page's name :"));

    jLabel2.setBounds(new Rectangle(22, 52, 111, 17));
    jLabel2.setRequestFocusEnabled(false);
    jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel2.setText(Res.localize("width (X) :"));

    edHeight.setBounds(new Rectangle(141, 72, 155, 22));
    edHeight.setNextFocusableComponent(btnOK);

    jLabel3.setBounds(new Rectangle(22, 76, 111, 17));
    jLabel3.setRequestFocusEnabled(false);
    jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel3.setText(Res.localize("height (Y) :"));

    edName.setBounds(new Rectangle(141, 9, 174, 22));
    edName.setNextFocusableComponent(edWidth);
    edName.setBounds(new Rectangle(141, 9, 154, 22));

    this.setTitle(Res.localize("New page"));
    this.setModal(true);

    panel1.setMaximumSize(new Dimension(316, 149));
    panel1.setMinimumSize(new Dimension(316, 149));
    panel1.setNextFocusableComponent(edName);
    panel1.setPreferredSize(new Dimension(316, 149));
    panel1.setRequestFocusEnabled(false);

    getContentPane().add(panel1);

    panel1.add(edName, null); //[*] add first, so that it has the focus
    panel1.add(btnCancel, null);
    panel1.add(btnOK, null);
    panel1.add(jLabel1, null);
    if (type == DRAWING_PAGE) {
      panel1.add(jLabel3, null);
      panel1.add(edHeight, null);
      panel1.add(jLabel2, null);
      panel1.add(edWidth, null);
    }

    //edName.requestFocus(); //won't work, use [*] instead
  }

  void btnOK_actionPerformed(ActionEvent e) {
   canceled=false;
   setVisible(false);
  }

  void btnCancel_actionPerformed(ActionEvent e) {
   canceled=true;
   setVisible(false);
  }
}
