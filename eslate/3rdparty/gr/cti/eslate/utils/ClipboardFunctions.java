package gr.cti.eslate.utils;

//package gr;

//package myPackage;

import java.lang.String;
import java.net.URL;
import gr.cti.eslate.base.ESlateHandle;
import javax.swing.Icon;
import java.awt.Image;
import javax.swing.ImageIcon;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import com.sun.image.codec.jpeg.*;
import java.lang.String;
import java.io.File;



public class ClipboardFunctions {

   public native static void copyToClipboard(String filename);

   public native static void pasteFromClipboard(String filename);

   public native static boolean existImageInClipboard();


   static {
     try{
       System.loadLibrary("callvcclipboard");
     } catch (UnsatisfiedLinkError e){
        System.err.println("I can not load the library the visual c++ library");
        e.printStackTrace();
     }
   }  //end of loading the vc++ dll

   static {
     try{
       System.loadLibrary("ClipboardData");
     } catch (UnsatisfiedLinkError e){
        System.err.println("Sorry, I can not load the library the visual basic library");
        e.printStackTrace();
     }
   }  //end of loading the vc++ dll


   public static void copyToClipboard(NewRestorableImageIcon icon){
       try {
           System.out.println("inside new copyToClipboard:");
           File filename = File.createTempFile("copyImgToClip", ".bmp");   ////se ayto to arxeio apothikeyetai h eikona toy NewRestoRableImageIcon: jpg, gif
//File filename = new File("C:\\copyImgToClip001.bmp");
           System.out.println("temporary file name : " + filename.toString());
           FileOutputStream imagefilename = new FileOutputStream(filename);
           try{
             icon.saveImage(NewRestorableImageIcon.BMP, imagefilename);
//             icon.saveImage(NewRestorableImageIcon.JPG, imagefilename);
             imagefilename.flush();
             imagefilename.close();

             copyToClipboard(filename.toString());

             filename.delete();

             System.out.println("exist image in clipboard " + existImageInClipboard());
           }catch (Exception exc) {exc.printStackTrace();}
        }
        catch(Exception e){e.printStackTrace();}
   } //end of copy to clipboard (NewRestorableIcon)


   public static NewRestorableImageIcon pasteImageFromClipboard(){
       try {
           System.out.println("inside new pasteFromClipboard: ");
           File filename = File.createTempFile("pasteImgFromClip", ".bmp");   ////se ayto to arxeio  tha apothikeytei h eikona toy clipboard jpg, gif
           System.out.println("temporary file name : " + filename.toString());
           if (filename.toString().length() != 0) {
               pasteFromClipboard(filename.toString());
               NewRestorableImageIcon icon = new NewRestorableImageIcon (filename.toString());
               System.out.println("inside function, icon width " + icon.getIconWidth());
               filename.delete();

               return icon;
           }
           else
              return null;
        }catch(Exception e){
           e.printStackTrace();
           return null;
        }
   } //end of paste from clipboard ()


/*
   public static void main(String args[]) {
     System.out.println("Inside ClipboardFunctions...\n");

     //COPY TO CLIPBOARD
   // NewRestorableImageIcon icon = new NewRestorableImageIcon("C:/logo.gif");
   // System.out.println("Icon height " + icon.getIconHeight());
   // copyToClipboard(icon);

     //PASTE FROM CLIPBOARD
    // NewRestorableImageIcon newicon = pasteImageFromClipboard();
    // System.out.print("Icon width " + newicon.getIconWidth());
   }

*/
}