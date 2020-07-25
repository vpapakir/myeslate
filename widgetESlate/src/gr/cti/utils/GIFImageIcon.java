//Title:        GIFImageIcon
//Version:      18Dec2001
//Copyright:    Copyright (c) 1998-2001
//Author:       George Birbilis
//Company:      CTI
//Description:  An ImageIcon that persists as a .GIF

package gr.cti.utils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import Acme.*;

public class GIFImageIcon
 extends ImageIcon         //29-12-1998
 implements Externalizable
{

 static final long serialVersionUID = 2711999L; //27-1-1999: serial-version, so that new vers load OK

 public GIFImageIcon(){}

 public GIFImageIcon(Image img){
  super(img);
 }

 public GIFImageIcon(ImageIcon icon){ //14Dec1999
  super(icon.getImage());
 }

 public void writeExternal(ObjectOutput out) throws IOException{
  try{
   out.writeObject(getDescription());
   ByteArrayOutputStream buffer=new ByteArrayOutputStream();
   GifEncoder ge = new GifEncoder(getImage(),buffer);
         ge.encode();
         buffer.close();
   out.writeObject(buffer.toByteArray()); //ByteArrayOutputStream is not serializable: so we get an extra third memory copy of our data :-(
   buffer.reset(); //3-2-1999: was hogging the memory, now trying to free some memory, since the garbage collector doesn't seem to do it
  }catch(Exception e){
   System.err.println(e);
   throw new IOException("GIFImageIcon.writeExtenal failed");
  }
 }

 public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
  try{
   setDescription((String)in.readObject());
   setImage(Toolkit.getDefaultToolkit().createImage((byte[])in.readObject()));
  }catch(Exception e){
   System.err.println(e);
   throw new IOException("GIFImageIcon.readExtenal failed");
  }
 }

}

