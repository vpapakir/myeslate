package gr.cti.eslate.mapViewer;

import java.awt.Image;

import javax.swing.ImageIcon;

class Helpers {
    /**
     * Scales the image retaining its proportions to fit in the rectangle, or the image if it already fits
     * (i.e. no magnification allowed).
     */
    static Image scaleImageOnRect(Image img,int rwidth,int rheight) {
        int iw=img.getWidth(null);
        int ih=img.getHeight(null);
        int w,h;
        //First check if no downscale is needed.
        if (img.getWidth(null)<=rwidth && img.getHeight(null)<=rheight)
            return img;
        //The image will fit in width
        if (rwidth*ih/iw<=rheight) {
            w=rwidth;
            h=rwidth*ih/iw;
        //The image will fit in height
        } else {
            w=rheight*iw/ih;
            h=rheight;
        }
        return img.getScaledInstance(w,h,Image.SCALE_SMOOTH);
    }
    /**
     * Loads an image from the jar file.
     */
    static ImageIcon loadImageIcon(String filename) {
        Helpers h=new Helpers();
        try {
            return new ImageIcon(Helpers.class.getResource(filename));
        } catch(Exception e) {
            System.out.println("Error loading Image Icon '"+filename+"'");
            try {
                return new ImageIcon(Helpers.class.getResource("images/notfound.gif"));
            } catch(Exception e1) {
            }
        }
        return null;
    }
}
