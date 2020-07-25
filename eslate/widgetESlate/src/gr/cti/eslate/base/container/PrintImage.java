package gr.cti.eslate.base.container;

import java.awt.*;
import java.io.*;
import javax.print.*;
import javax.print.attribute.*;

import gr.cti.eslate.utils.*;

/**
 * This class provides a static method to print images.
 *
 * @author      Kriton Kyrimis
 */
public class PrintImage
{
  /**
   * Print an image.
   * @param     image   The image to print.
   */
  public static void print(Image image) throws IOException, PrintException
  {
    DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
    PrintService[] services =
      PrintServiceLookup.lookupPrintServices(null, null);
    PrintService svc = PrintServiceLookup.lookupDefaultPrintService();
    PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
    PrintService selection = ServiceUI.printDialog(
      null, 100, 100, services, svc, flavor, attrs
    );
    if (selection == null) {
      return;
    }

    DocPrintJob pJob = selection.createPrintJob();

    NewRestorableImageIcon ic = new NewRestorableImageIcon(image);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ic.saveImage(NewRestorableImageIcon.PNG, bos);
    bos.close();
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    Doc doc = new SimpleDoc(bis, flavor, null);

    pJob.print(doc, attrs);
    bis.close();
  }

/*
  public static void main(String[] args)
  {
    try {
      javax.swing.ImageIcon ic = new javax.swing.ImageIcon(args[0]);
      print(ic.getImage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
*/
}
