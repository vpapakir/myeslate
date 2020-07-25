package gr.cti.eslate.utils;

import javax.swing.Icon;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.io.*;
import javax.accessibility.*;
import javax.imageio.*;
import javax.swing.*;

import Acme.*;

/**
 * This class encapsulates an ImageIcon. It provides the same functionality as
 * ImageIcon, and can be serialized in a Java version-independent manner.
 *
 * @version     2.0.9, 28-Jun-2006
 * @author      Kriton Kyrimis
 */
public class NewRestorableImageIcon
  extends ImageIcon  // So that Swing can use the ImageIcon functionality
  implements Icon, Serializable
{
  //static final long serialVersionUID = 532615968316031794L;
  static final long serialVersionUID = -4430479151350855914L;

  /**
   * Specifies that the icon's image should be saved in GIF format.
   */
  public final static int GIF = 0;
  /**
   * Specifies that the icon's image should be saved in PNG format.
   */
  public final static int PNG = 1;
  /**
   * Specifies that the icon's image should be saved in JPEG format.
   */
  public final static int JPEG = 2;
  /**
   * Specifies that the icon's image should be saved in JPEG format.
   */
  public final static int JPG = JPEG;
  /**
   * Specifies that the icon's image should be saved in BMP format.
   */
  public final static int BMP = 3;

  /**
   * The format in which the image will be saved when the class is serialized.
   */
  private int saveFormat = PNG;
  /**
   * The encapsulated ImageIcon.
   */
  transient private ImageIcon imageIcon;

  /**
   * Save format version.
   */
  private final static int saveVersion = 2;

  // StorageStructure keys.
  private final static String IMAGE = "1";
  private final static String FORMAT = "2";
  private final static String DESCRIPTION = "3";

  /**
   * Creates a NewRestorableImageIcon from the specified file. The image will
   * be preloaded by using MediaTracker to monitor the loading state
   * of the image.
   * @param     filename        The name of the file containing the image.
   * @param     description     A brief textual description of the image.
   * @see       #NewRestorableImageIcon(String)
   */
  public NewRestorableImageIcon(String filename, String description)
  {
    // First try creating the icon using standard java, which is faster.
    // If this fails, try again using ImageIO, which supports more formats.
    imageIcon = new ImageIcon(filename, description);
    if (imageIcon.getIconWidth() < 0) {
      try {
        imageIcon =
          new ImageIcon(ImageIO.read(new File(filename)), description);
      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }
    }
  }

  /**
   * Creates a NewRestorableImageIcon from the specified file. The image will
   * be preloaded by using MediaTracker to monitor the loading state
   * of the image. The specified String can be a file name or a
   * file path. When specifying a path, use the Internet-standard
   * forward-slash ("/") as a separator. For example, specify:<pre>
   *    new NewRestorableImageIcon("images/myImage.gif")
   * </pre>
   * (The string is converted to an URL, so the forward-slash works
   * on all systems.)
   *
   * @param     filename        A String specifying a filename or path.
   */
  public NewRestorableImageIcon (String filename)
  {
    // First try creating the icon using standard java, which is faster.
    // If this fails, try again using ImageIO, which supports more formats.
    imageIcon = new ImageIcon(filename);
    if (imageIcon.getIconWidth() < 0) {
      try {
        imageIcon = new ImageIcon(ImageIO.read(new File(filename)));
      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }
    }
  }

  /**
   * Creates a NewRestorableImageIcon from the specified URL. The image will
   * be preloaded by using MediaTracker to monitor the loaded state
   * of the image.
   * @param     location        The URL for the image.
   * @param     description     A brief textual description of the image.
   * @see #NewRestorableImageIcon(String)
   */
  public NewRestorableImageIcon(URL location, String description)
  {
    // First try creating the icon using standard java, which is faster.
    // If this fails, try again using ImageIO, which supports more formats.
    imageIcon = new ImageIcon(location, description);
    if (imageIcon.getIconWidth() < 0) {
      try {
        imageIcon = new ImageIcon(ImageIO.read(location), description);
      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }
    }
  }

  /**
   * Creates a NewRestorableImageIcon from the specified URL. The image will
   * be preloaded by using MediaTracker to monitor the loaded state
   * of the image.
   */
  public NewRestorableImageIcon (URL location)
  {
    // First try creating the icon using standard java, which is faster.
    // If this fails, try again using ImageIO, which supports more formats.
    imageIcon = new ImageIcon(location);
    if (imageIcon.getIconWidth() < 0) {
      try {
        imageIcon = new ImageIcon(ImageIO.read(location));
      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }
    }
  }

  /**
   * Creates a NewRestorableImageIcon from the image.
   * @param     image           The image.
   * @param     description     A brief textual description of the image.
   */
  public NewRestorableImageIcon(Image image, String description)
  {
    imageIcon = new ImageIcon(image, description);
  }

  /**
   * Creates a NewRestorableImageIcon from an image object.
   * @param     image           The image.
   */
  public NewRestorableImageIcon (Image image)
  {
    imageIcon = new ImageIcon(image);
  }

  /**
   * Creates a NewRestorableImageIcon from an array of bytes which were
   * read from an image file containing a supported image format,
   * such as GIF or JPEG.  Normally this array is created
   * by reading an image using Class.getResourceAsStream(), but
   * the byte array may also be statically stored in a class.
   *
   * @param     imageData       An array of pixels in an image format supported
   *                            by the AWT Toolkit, such as GIF or JPEG.
   * @param     description     A brief textual description of the image.
   * @see       java.awt.Toolkit#createImage
   */
  public NewRestorableImageIcon (byte[] imageData, String description)
  {
    // First try creating the icon using standard java, which is faster.
    // If this fails, try again using ImageIO, which supports more formats.
    imageIcon = new ImageIcon(imageData, description);
    if (imageIcon.getIconWidth() < 0) {
      try {
        InputStream s = new ByteArrayInputStream(imageData);
        imageIcon = new ImageIcon(ImageIO.read(s), description);
        s.close();
      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }
    }
  }

  /**
   * Creates a NewRestorableImageIcon from an array of bytes which were
   * read from an image file containing a supported image format,
   * such as GIF or JPEG.  Normally this array is created
   * by reading an image using Class.getResourceAsStream(), but
   * the byte array may also be statically stored in a class.
   *
   * @param     imageData       An array of pixels in an image format
   *                            supported by the AWT Toolkit, such as GIF or
   *                            JPEG.
   * @see       java.awt.Toolkit#createImage
   */
  public NewRestorableImageIcon (byte[] imageData)
  {
    // First try creating the icon using standard java, which is faster.
    // If this fails, try again using ImageIO, which supports more formats.
    imageIcon = new ImageIcon(imageData);
    if (imageIcon.getIconWidth() < 0) {
      try {
        InputStream s = new ByteArrayInputStream(imageData);
        imageIcon = new ImageIcon(ImageIO.read(s));
        s.close();
      } catch (IOException ioe) {
      }
    }
  }

  /**
   * Creates a NewRestorableImageIcon from an input stream containing the bytes
   * of an image file in a supported image format, such as PNG, JPG, or GIF.
   * <EM>Note:</EM> This method will read the entire stream, so make sure that
   * the stream does not contain anything other than the image.
   * @param     s               The input stream from which the data will be
   *                            read.
   * @param     description     A brief textual description of the image.
   */
  public NewRestorableImageIcon(InputStream s, String description)
  {
    this(streamToByte(s), description);
  }

  /**
   * Creates a NewRestorableImageIcon from an input stream containing the bytes
   * of an image file in a supported image format, such as PNG, JPG, or GIF.
   * <EM>Note:</EM> This method will read the entire stream, so make sure that
   * the stream does not contain anything other than the image.
   * @param     s       The input stream from which the data will be read.
   */
  public NewRestorableImageIcon(InputStream s)
  {
    this(streamToByte(s));
  }

  /**
   * Creates an uninitialized image icon.
   */
  public NewRestorableImageIcon()
  {
    imageIcon = new ImageIcon();
  }

  /**
   * Get the description of the image.  This is meant to be a brief
   * textual description of the object.  For example, it might be
   * presented to a blind user to give an indication of the purpose
   * of the image.
   * @return    The requested description.
   */
  public String getDescription()
  {
    return imageIcon.getDescription();
  }

  /**
   * Get the height of the NewRestorableImageIcon.
   * @return    The requested height.
   */
  public int getIconHeight()
  {
    return imageIcon.getIconHeight();
  }

  /**
   * Get the width of the NewRestoreableImageIcon.
   * @return    The requested width.
   */
  public int getIconWidth()
  {
    return imageIcon.getIconWidth();
  }

  /**
   * Returns the NewRestorableImageIcon's Image.
   * @return    The requested image.
   */
  public Image getImage()
  {
    return imageIcon.getImage();
  }

  /**
   * Returns the status of the image loading operation.
   * @return    The loading status as defined by java.awt.MediaTracker.
   * @see       java.awt.MediaTracker#ABORTED
   * @see       java.awt.MediaTracker#ERRORED
   * @see       java.awt.MediaTracker#COMPLETE
   */
  public int getImageLoadStatus()
  {
    return imageIcon.getImageLoadStatus();
  }

  /**
   *  Return the image observer for the image
   * @return    The requested image observer.
   */
  public ImageObserver getImageObserver()
  {
    return imageIcon.getImageObserver();
  }

  /**
   * Paints the Icon.
   */
  public synchronized void paintIcon(Component c, Graphics g, int x, int y)
  {
    imageIcon.paintIcon(c, g, x, y);
  }

  /**
   * Paints the Icon after scaling it to a specified size.
   * @param     c       The component on which to paint the icon.
   * @param     g       The graphics context on which to paint the icon.
   * @param     x       The X coordinate at which to paint the icon.
   * @param     y       The Y coordinate at which to paint the icon.
   * @param     w       The width of the scale dicon.
   * @param     h       The height of the scaled icon.
   */
  public void paintIcon(Component c, Graphics g, int x, int y, int w, int h)
  {
    Image im = imageIcon.getImage();
    Image im2 = im.getScaledInstance(w, h, Image.SCALE_DEFAULT);
    g.drawImage(im2, x, y, null);
  }

  /**
   * Set the description of the image.  This is meant to be a brief
   * textual description of the object.  For example, it might be
   * presented to a blind user to give an indication of the purpose
   * of the image.
   * @param     description     The description of the image.
   */
  public void setDescription(String description)
  {
    imageIcon.setDescription(description);
  }

  /**
   * Set the image displayed by this icon.
   * @param     image   The image to set.
   */
  public void setImage(Image image)
  {
    imageIcon.setImage(image);
  }

  /**
   * Set the image observer for the image.  Set this
   * property if the NewRestorableImageIcon contains an animated GIF, so
   * the observer is notified to update its display.
   * For example:
   * <pre>
   *     icon = new NewRestorableImageIcon(...)
   *     button.setIcon(icon);
   *     icon.setImageObserver(button);
   * </pre>
   * @param     observer        The image observer to set.
   */
  public void setImageObserver(ImageObserver observer)
  {
    imageIcon.setImageObserver(observer);
  }

  /**
   * Read the serialized state of the object.
   * @param     s       The stream from which to read the state.
   */
  private void readObject(ObjectInputStream s)
    throws ClassNotFoundException, IOException
  {
    NRIInputStream nis = new NRIInputStream(s);
    if (nis.isNewNRIFormat()) {
      nis.skip(NRIInputStream.HEADER_SIZE);
      StorageStructure map = (StorageStructure)s.readObject();
      byte[] imageData = (byte[])map.get(IMAGE);
      // First try creating the icon using standard java, which is faster.
      // If this fails, try again using ImageIO, which supports more formats.
      imageIcon = new ImageIcon(imageData);
      if (imageIcon.getIconWidth() < 0) {
        try {
          InputStream is = new ByteArrayInputStream(imageData);
          imageIcon = new ImageIcon(ImageIO.read(is));
          is.close();
        } catch (IOException ioe) {
        }
      }
      saveFormat = map.get(FORMAT, PNG);
      String description = map.get(DESCRIPTION, "");
      imageIcon.setDescription(description);
    }else{
      imageIcon = new ImageIcon(ImageIO.read(nis));
      try {
        saveFormat = s.readInt();
      } catch (IOException ioe) {
        saveFormat = PNG;
      }
      try {
        String description = s.readUTF();
        if (description == null) {
          description = "";
        }
        imageIcon.setDescription(description);
      } catch (IOException ioe) {
      }
    }
  }

  /**
   * Serialize the object.
   * @param     s       The stream to which to write the state.
   * @exception IOException     Thrown if writing fails.
   */
  private void writeObject(ObjectOutputStream s) throws IOException
  {
    /*
    saveImage(saveFormat, s);
    s.writeInt(saveFormat);
    String description = imageIcon.getDescription();
    if (description == null) {
      description = "";
    }
    s.writeUTF(description);
    */
    // When reading the data, we use this header to recognize that the
    // data are in the new format.
    s.write((int)'N');
    s.write((int)'R');
    s.write((int)'I');

    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 3);
    byte[] bytes;
    Image image = imageIcon.getImage();
    //try {
      switch (saveFormat) {
        case GIF:
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          GifEncoder enc = new GifEncoder(image, bos);
          enc.encode();
          bos.close();
          bytes = bos.toByteArray();
          break;
        case JPEG:
          bytes = ImageTools.saveImage(image, "JPG");
        case PNG:
          bytes = ImageTools.saveImage(image, "PNG");
          break;
        case BMP:
          bytes = ImageTools.saveImage(image, "BMP");
          break;
        default:
          throw
            new IllegalArgumentException("Unsupported format: " + saveFormat);
      }
    //} catch (JimiException je) {
    //  throw new IOException(je.getMessage());
    //}
    if (bytes == null) {
      throw new IOException("Null image");
    }
    map.put(IMAGE, bytes);
    map.put(FORMAT, saveFormat);
    map.put(DESCRIPTION, imageIcon.getDescription());

    s.writeObject(map);
  }

  /**
   * Specifies what format will be used when serializing the object.
   * @param     format          The format of the image: one of GIF, PNG,
   *                            JPEG, BMP.
   * @exception IllegalArgumentException        Thrown if an unknown format is
   *                            specified.
   */
  public void setSaveFormat(int format) throws IllegalArgumentException
  {
    switch (format) {
      case GIF:
      case JPEG:
      case PNG:
      case BMP:
        saveFormat = format;
        break;
      default:
          throw new IllegalArgumentException("Unsupported format: " + format);
    }
  }

  /**
   * Saves the image of the NewRestorableImageIcon to a stream. This method can
   * be used to create an image file in one of the supported formats.
   * @param     format  The format of the image: one of GIF, PNG, JPEG, BMP.
   * @param     s       The stream to which to save the image.
   * @exception IllegalArgumentException        Thrown if an
   *                            unsupported format is specified.
   * @exception IOException     Thrown if saving fails.
   */
  public void saveImage(int format, OutputStream s)
    throws IllegalArgumentException, IOException
  {
    Image image = imageIcon.getImage();

    switch (format) {
      case GIF:
        GifEncoder enc = new GifEncoder(image, s);
        enc.encode();
        break;
      case JPEG:
        ImageIO.write(ImageTools.getRenderedImage(image, "JPG"), "JPG", s);
        break;
      case PNG:
        ImageIO.write(ImageTools.getRenderedImage(image, "PNG"), "PNG", s);
        break;
      case BMP:
        ImageIO.write(ImageTools.getRenderedImage(image, "BMP"), "BMP", s);
        break;
      default:
        throw new IllegalArgumentException("Unsupported format: " + format);
    }
  }

  /**
   * Returns all the bytes in an input stream.
   * @param     s       The input stream.
   * @return    A byte array containing the bytes from the current point in
   *            the input stream to its end. <EM>Warning:</EM> If the stream
   *            is infinite (e.g., generated by a program in a loop), this
   *            method will never return, allocating all available memory!
   */
  private static byte[] streamToByte(InputStream s)
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    int BUFSIZE = 1024;
    byte[] b = new byte[BUFSIZE];
    int len;
    try {
      while ((len = s.read(b, 0, BUFSIZE)) >= 0) {
        bos.write(b, 0, len);
      }
      bos.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return bos.toByteArray();
  }

  /**
   * Gets the AccessibleContext associated with the encapsulated ImageIcon.
   * @return    The AccessibleContext associated with the encapsulated
   *            ImageIcon.
   */
  public AccessibleContext getAccessibleCntext()
  {
    return imageIcon.getAccessibleContext();
  }

  /**
   * Retuns a string representation of this image.
   * @return    A string representation of this image.
   */
  public String toString()
  {
    return imageIcon.toString();
  }

}
