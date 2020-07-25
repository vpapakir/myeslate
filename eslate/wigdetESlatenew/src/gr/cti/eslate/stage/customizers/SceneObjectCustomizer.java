//Title:        Stage/SceneObjectCustomizer
//Version:      22May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage's SceneObject customizer

package gr.cti.eslate.stage.customizers;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import gr.cti.eslate.imageEditor.ImageEditorDialog;

import gr.cti.eslate.stage.Res;
import gr.cti.eslate.stage.models.AsSceneObject;

import com.thwt.layout.*;

public class SceneObjectCustomizer
 extends BaseCustomizer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private AsSceneObject object; /**/
  private JLabel lblColor;      private JButton btnColor;
  private JLabel lblImage;      private JButton btnImage;

  private LayoutConstraint c1,c2,
                           d1,d2;
  //Shape imageEditorShape;

////////////////////////////////

  public SceneObjectCustomizer() {
   try{jbInit();}
   catch(Exception ex){ex.printStackTrace();}
  }

/////////////////////////////////////////////////

 public String getTitle(){
  return Res.localize("Scene Object");
 }

 public void setupWidgetsFromObject(){
  //System.out.println("SceneObject.setupWidgetsFromObject");
  if(object==null) return;
  //color//
  btnColor.setBackground(object.getColor());
  //image//
  Image i=object.getImage();
  btnImage.setIcon((i!=null)?new ImageIcon(i):null);
 }

 public void setObject(Object o){
  object=(AsSceneObject)o;
  setupWidgetsFromObject();
 }

 public Object getObject(){
  return null;
 }

/////////////////////////////////////////////////

  private static final int PADX=2;
  private static final int COLUMN2X=80;
  private static final int STEPY=10;
  private static final int LBLWIDTH=COLUMN2X-PADX-10; //12May2000
  private static final int LBLHEIGHT=20;
  private static final int IMAGE_HEIGHT=120;
  SmartLayout smartLayout = new SmartLayout(); //26Apr2000

  private void jbInit() throws Exception {
    String sc=":";
    String dots="...";

    ////////////////

    lblColor = new JLabel(Res.localize("Color")+sc);
    btnColor = new JButton(Res.localize("Change")+dots);
    btnColor.addActionListener(new java.awt.event.ActionListener() {
     public void actionPerformed(ActionEvent e) {
      btnColor_actionPerformed(e);
     }
    });

    ////////////////

    lblImage = new JLabel(Res.localize("Image")+sc);
    btnImage = new JButton(Res.localize("Change")+dots);
    btnImage.addActionListener(new java.awt.event.ActionListener() {
     public void actionPerformed(ActionEvent e) {
      btnImage_actionPerformed(e);
     }
    });

    ////////////////

    lblColor.setHorizontalAlignment(RIGHT);
    lblImage.setHorizontalAlignment(RIGHT);

    lblImage.setVerticalTextPosition(SwingConstants.CENTER); //26Apr2000: setting text's position now that we show an icon
    lblImage.setHorizontalTextPosition(SwingConstants.RIGHT); //26Apr2000: setting text's position now that we show an icon

    btnImage.setVerticalTextPosition(SwingConstants.BOTTOM); //22May2000: show label under any image and not next to it so that the text doesn't get cut
    btnImage.setHorizontalTextPosition(SwingConstants.CENTER); //22May2000

    smartLayout=new SmartLayout();
    this.setLayout(smartLayout); //using SmartLayout Manager

    //1st column//

    c1=new LayoutConstraint();
    c1.anchorToContainerTop(STEPY);
    c1.anchorToContainerLeft(PADX);
    c1.setFixedWidth(LBLWIDTH);
    c1.setFixedHeight(LBLHEIGHT);
    this.add(lblColor,c1);

    c2=new LayoutConstraint();
    c2.anchorToBottomOf(lblColor,STEPY);
    c2.anchorToContainerLeft(PADX);
    c2.setFixedWidth(LBLWIDTH);
    c2.setFixedHeight(LBLHEIGHT);
    this.add(lblImage,c2);

    //2nd column//

    d1=new LayoutConstraint();
    d1.sameHeightAs(lblColor);
    d1.anchorToContainerTop(STEPY);
    d1.anchorToContainerLeft(COLUMN2X);
    d1.anchorToContainerRight(PADX); //26Apr2000: fixed name number field whose end wasn't showing right aligned with the other number fields (had harcoded number 10, changed to PADX)
    this.add(btnColor,d1);

    d2=new LayoutConstraint();
    d2.setFixedHeight(IMAGE_HEIGHT); //22May2000: fixed image button to be taller (had made it short again by accident)
    d2.anchorToBottomOf(lblColor,STEPY);
    d2.anchorToContainerLeft(COLUMN2X);
    d2.anchorToContainerRight(PADX);
    this.add(btnImage, d2);

    //this.setMinimumSize(new Dimension(228, 136));
    //this.setPreferredSize(new Dimension(228, 134));

    /*
    java.util.Vector errors = smartLayout.checkLayout();
    if ((errors != null) && (errors.size() > 0)) {
       for (int i=0; i<errors.size(); i++) {
           System.err.println(errors.elementAt(i));
       }
    }
    */


  }

  private void setObjectIcon(ImageIcon i){
   btnImage.setIcon(i); //viewing the image on the "image widget"
   object.setImage((i!=null)?i.getImage():null); //pass null to remove any current image
  }

  private void setObjectColor(Color c){
   btnColor.setBackground(c); //viewing the color on the "color widget"
   object.setColor(c);
  }

  void btnColor_actionPerformed(ActionEvent e) {
   if(object==null) return; //?
   Color color=JColorChooser.showDialog(btnColor,Res.localize("Color"),object.getColor());
   setObjectColor(color);
   setObjectIcon(null); //15May2000: setting a new color removes any current image!
  }

  void btnImage_actionPerformed(ActionEvent e) { //12Jan2000
      ImageEditorDialog ic=new ImageEditorDialog(new Frame());
      Image img=object.getImage();
      if(img!=null){
          ImageIcon icon=new ImageIcon(img);
          try{
              ic.setImage(icon.getImage());
          } catch(Exception ex) {
              System.err.println(e);
          }
      }
      //if (imageEditorShape != null)
      //    ic.getImageEditor().setSelectedArea(imageEditorShape);
      ic.showDialog(this); //26Jan2000: show image editor even if image is null! (must have some way to make icon editor return a null image, else add a X button next to the edit image button)
      //imageEditorShape = ic.getImageEditor().getSelectedArea();
      Image im = ic.getImage();
      if (im != null) {
        setObjectIcon(new ImageIcon(im));
      }
  }
}
