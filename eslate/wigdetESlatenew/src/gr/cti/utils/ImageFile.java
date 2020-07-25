package gr.cti.utils;

import java.io.*;

import java.awt.*;
import java.awt.image.*;
import java.awt.FileDialog;

import javax.imageio.*;
import javax.swing.*;

import Acme.*;

import gr.cti.eslate.utils.*;


/**
 * @version     2.0.17, 27-Jun-2007
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class ImageFile implements FilenameFilter{
    public static String dir="";

    public static Image loadImage(String message) { //this is static: Image img=ImageFile.loadImage(); is its typical use
     ESlateFileDialog imageFileDialog;
     ImageIcon myicon=null; //set to null to avoid compile error that "myicon might not be initialized"
     //Image image=null;//26-1-1999//

     imageFileDialog = new ESlateFileDialog(new JFrame(),message,FileDialog.LOAD,".gif");
//doesn't work// imageFileDialog.setFilenameFilter(new ImageFile()); //29-12-1998
//     imageFileDialog.setTitle(message);
     //imageFileDialog.setMode(FileDialog.LOAD);
     imageFileDialog.setDirectory(dir);
     String[] exts={".gif",".jpg"}; //14-1-1999: now using default extensions
     imageFileDialog.setDefaultExtension(exts); //12-1-1999 (!!! change Added to Default)

     String iconExtension ="*.gif;*.jpg";
         imageFileDialog.setFile(iconExtension); //14-1-1999: ESlateFileDialog not doing this: we must do it to emulate a display filter
         imageFileDialog.show();

         String filename = imageFileDialog.getFile(); //shall hold the filename even if loading fails
        try{
            if (filename!=null){
             filename = imageFileDialog.getDirectory() + filename;
//12-1-1999//             String s=filename.toLowerCase();
//12-1-1999//             if (!(s.endsWith(".gif") || s.endsWith(".jpg"))) filename=filename.concat(".gif"); //27-8-1998
    //System.out.println(filename);
             myicon = new ImageIcon(filename);
             //image=Toolkit.getDefaultToolkit().getImage(filename);//26-1-1999//
             //System.out.println("Xsize="+myicon.getIconWidth());
             //System.out.println("Ysize="+myicon.getIconHeight());
            }
            else
                System.err.println("Not a valid image");
        }
        catch (/*NullPointer*/Exception ed) { //12-1-1999
            System.err.println(ed+"exception in load image");
            }
        //catch (Exception e) {
        //    JOptionPane.showMessageDialog(new JFrame(),"The file you selected is not a valid image file"+e.getMessage(), "",JOptionPane.OK_OPTION);
        //}

//        if(image!=null){ //26-1-1999//
        if (myicon!=null) {
         dir=imageFileDialog.getDirectory(); //17-10-1998: keep the directory if success
         return myicon.getImage();
//          return image; //26-1-1999//
        }
        else return null;
    }

//--------- adapted from PixMaker.java --------

  public static void saveImage(Image img, String message)
  {
    ESlateFileDialog fd = new ESlateFileDialog(new JFrame(),message,FileDialog.SAVE);
    //doesn't work// fd.setFilenameFilter(new ImageFile()); //29-12-1998
    //fd.setFile("*.gif"); //29-12-1998: show only ".gif" files when saving
    fd.setFile("*.bmp"); // show only ".bmp" files when saving
    fd.setDirectory(dir);
    fd.setDefaultExtension("bmp");
    fd.show();
    if (fd.getFile() != null) {
      String filename = fd.getDirectory()+fd.getFile(); //27-8-1998
      //System.out.println(filename);
      //saveGIF(filename,img);
      saveBMP(filename,img);
      dir=fd.getDirectory(); //17-10-1998: keep the directory if success
    }else{
      System.err.println("Save File Failed");
    }
  }

  /**
   * Saves an image as a GIF file.
   * @param     fileName        The name of the file where the image will be
   *                            saved.
   * @param     img     The image to save.
   * @return    The full path name of the saved file. If t hfile was not
   *            saved, <code>null</code> is returned.
   */
  public static String saveGIF(String fileName, Image img)
  {
    File file = new File(fileName);
    try {
      // Try using ImageIO's GIF writer (available from version 1.6 of Java),
      // as this will produce superior results.
      RenderedImage ri = ImageTools.getRenderedImage(img, "GIF");
      if (!ImageIO.write(ri, "GIF", file)) {
        // Java runtime does not have a GIF encoder. Downscale the image to an
        // indexed image and save it using the Acme encoder.
        BufferedImage bi = new BufferedImage(
          img.getWidth(null), img.getHeight(null),
          BufferedImage.TYPE_BYTE_INDEXED
        );
        Graphics g = bi.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        BufferedOutputStream bos =
          new BufferedOutputStream(new FileOutputStream(file));
        GifEncoder enc = new GifEncoder(bi, bos);
        enc.encode();
        bos.close();
      }
      // Return a fully qualified filename for the saved image.
      return file.getAbsolutePath();
    } catch(IOException e) {
      System.err.println(e);
      return null; //if failed
    }
  }

  /**
   * Saves an image as a BMP file.
   * @param     fileName        The name of the file where the image will be
   *                            saved.
   * @param     img     The image to save.
   * @return    The full path name of the saved file. If the file was not
   *            saved, <code>null</code> is returned.
   */
  public static String saveBMP(String fileName, Image img)
  {
    File file = new File(fileName);
    try {
      RenderedImage ri = ImageTools.getRenderedImage(img, "BMP");
      if (!ImageIO.write(ri, "BMP", file)) {
        return null;
      }
      // Return a fully qualified filename for the saved image.
      return file.getAbsolutePath();
    } catch(IOException e) {
      System.err.println(e);
      return null; //if failed
    }
  }

  public boolean accept(File dir,String filename){ //29-12-1998: FilenameFilter interface
   //System.out.println("Filtering "+filename);
   String s=filename.toLowerCase();
   return(s.endsWith(".gif") || s.endsWith(".jpg"));
  }

}
