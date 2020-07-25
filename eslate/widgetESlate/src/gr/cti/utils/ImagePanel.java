//Title:        utilsBirb
//Version:      7Jun2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import javax.swing.ImageIcon;

import java.awt.Image;
import java.awt.Graphics;

public class ImagePanel
 extends javax.swing.JComponent
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 //FIELDs//

 private Image image;
 private boolean autoresizable;
 private boolean centered;
 private boolean stretched;

 
 //CONSTRUCTORs//

 public ImagePanel() {
 }

 public ImagePanel(Image i){
  setImage(i);
 }

 public ImagePanel(ImageIcon i){
  setImage(i);
 }


 //PROPERTIEs//

 public int getImageWidth(){
  return (image!=null)?image.getWidth(this):0;
 }

 public int getImageHeight(){
  return (image!=null)?image.getHeight(this):0;
 }

 //autoResizable property//

 public void setAutoResizable(boolean flag){
  autoresizable=flag;
  if(flag) resizeToImageSize();
 }

 public boolean isAutoResizable(){
  return autoresizable;
 }

 //centered property//

 public void setCentered(boolean flag){
  centered=flag;
  repaint();
 }

 public boolean isCentered(){
  return centered;
 }

 //stretched property//

 public void setStretched(boolean flag){
  stretched=flag;
  repaint();
 }

 public boolean isStretched(){
  return stretched;
 }

 //image property//

 public void setImage(Image i){
  image=i;
  repaint(); //update the display
 }

 public Image getImage(){
  return image;
 }


 //METHODs//

 public void paintComponent(Graphics g){
  super.paintComponent(g); //to fill the background if the "opaque" property is true

  if(image==null) return;
  if(stretched)
   g.drawImage(image,
               0,0,getWidth()-1,getHeight()-1, //dest
               0,0,getImageWidth()-1,getImageHeight()-1, //source
               this);
  else if(centered)
   g.drawImage(image,
               (int)Math.round(getWidth()/2d-getImageWidth()/2d),
               (int)Math.round(getHeight()/2d-getImageHeight()/2d),
               this);
  else
   g.drawImage(image,0,0,this);
 }

 public void resizeToImageSize(){
  setSize(getImageWidth(),getImageHeight()); //for null images it will resize to width=0 and height=0
 }

 public void setImage(ImageIcon i){
  setImage((i!=null)?i.getImage():(Image)null); //call setImage(java.awt.Image) - that one updates the display
 }

}
