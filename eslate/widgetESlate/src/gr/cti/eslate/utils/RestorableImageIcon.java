package gr.cti.eslate.utils;

import javax.swing.Icon;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.io.*;
import javax.imageio.*;
import Acme.*;

/**
 * This class encapsulates an Image.
 *
 * @version     2.0.9, 28-Jun-2006
 * @author      George Tsironis, based on Sun sources.
 * @author      Kriton Kyrimis
 * @see         gr.cti.eslate.utils.NewRestorableImageIcon
 * @deprecated  Objects of this class are not serialized in a Java
 *              version-independent manner, even though this had been the
 *              original intention of this class. Use class
 *              {@link gr.cti.eslate.utils.NewRestorableImageIcon}
 *              instead, and only use this class for backwards compatibility
 *              with previous versions of your components.
 */
public class RestorableImageIcon implements Icon, Serializable
{
  //static final long serialVersionUID = 532615968316031794L;
  static final long serialVersionUID = 1583625010319046227L;

  transient Image image;
  transient int loadStatus = 0;
  ImageObserver imageObserver;
  String description = null;
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

  private int saveFormat = PNG;

  protected final static Component component = new Component()
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
  };
  protected final static MediaTracker tracker = new MediaTracker(component);

  int width = -1;
  int height = -1;

  /**
   * Creates a RestorableImageIcon from the specified file. The image will
   * be preloaded by using MediaTracker to monitor the loading state
   * of the image.
   * @param     filename        The name of the file containing the image.
   * @param     description     A brief textual description of the image.
   * @see       #RestorableImageIcon(String)
   */
  public RestorableImageIcon(String filename, String description)
  {
    try {
      image = ImageIO.read(new File(filename));
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    this.description = description;
    loadImage(image);
  }

  /**
   * Creates a RestorableImageIcon from the specified file. The image will
   * be preloaded by using MediaTracker to monitor the loading state
   * of the image. The specified String can be a file name or a
   * file path. When specifying a path, use the Internet-standard
   * forward-slash ("/") as a separator. For example, specify:<pre>
   *    new RestorableImageIcon("images/myImage.gif")
   * </pre>
   * (The string is converted to an URL, so the forward-slash works
   * on all systems.)
   *
   * @param     filename        A String specifying a filename or path.
   */
  public RestorableImageIcon (String filename)
  {
    this(filename, filename);
  }

  /**
   * Creates a RestorableImageIcon from the specified URL. The image will
   * be preloaded by using MediaTracker to monitor the loaded state
   * of the image.
   * @param     location        The URL for the image.
   * @param     description     A brief textual description of the image.
   * @see #RestorableImageIcon(String)
   */
  public RestorableImageIcon(URL location, String description)
  {
    try {
      image = ImageIO.read(location);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    this.description = description;
    loadImage(image);
  }

  /**
   * Creates a RestorableImageIcon from the specified URL. The image will
   * be preloaded by using MediaTracker to monitor the loaded state
   * of the image.
   */
  public RestorableImageIcon (URL location)
  {
    this(location, location.toExternalForm());
  }

  /**
   * Creates a RestorableImageIcon from the image.
   * @param     image           The image.
   * @param     description     A brief textual description of the image.
   */
  public RestorableImageIcon(Image image, String description)
  {
    this(image);
    this.description = description;
  }

  /**
   * Creates a RestorableImageIcon from an image object.
   * @param     image           The image.
   */
  public RestorableImageIcon (Image image)
  {
    this.image = image;
    Object o = image.getProperty("comment", imageObserver);
    if (o instanceof String) {
      description = (String) o;
    }
    loadImage(image);
  }

  /**
   * Creates a RestorableImageIcon from an array of bytes which were
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
  public RestorableImageIcon (byte[] imageData, String description)
  {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
      try {
        image = ImageIO.read(bais);
      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }
      bais.close();
    } catch (Exception e) {
      image = null;
    }
    if (image == null) {
      return;
    }
    this.description = description;
    loadImage(image);
  }

  /**
   * Creates a RestorableImageIcon from an array of bytes which were
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
  public RestorableImageIcon (byte[] imageData)
  {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
      try {
        image = ImageIO.read(bais);
      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }
      bais.close();
    } catch (Exception e) {
      image = null;
    }
    if (image == null) {
      return;
    }
    Object o = image.getProperty("comment", imageObserver);
    if (o instanceof String) {
      description = (String) o;
    }
    loadImage(image);
  }

  /**
   * Creates a RestorableImageIcon from an input stream containing the bytes
   * of an image file in a supported image format, such as PNG, JPG, or GIF.
   * @param     s               The input stream from which the data will be
   *                            read.
   * @param     description     A brief textual description of the image.
   */
  public RestorableImageIcon(InputStream s, String description)
  {
    try {
      image = ImageIO.read(s);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    /*Object o = */image.getProperty("comment", imageObserver);
    this.description = description;
    loadImage(image);
  }

  /**
   * Creates a RestorableImageIcon from an input stream containing the bytes
   * of an image file in a supported image format, such as PNG, JPG, or GIF.
   * @param     s       The input stream from which the data will be read.
   */
  public RestorableImageIcon(InputStream s)
  {
    try {
      image = ImageIO.read(s);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    Object o = image.getProperty("comment", imageObserver);
    if (o instanceof String) {
      description = (String) o;
    }
    loadImage(image);
  }

  /**
   * Creates an uninitialized image icon.
   */
  public RestorableImageIcon() {
  }

  /**
   * Wait for the image to load
   */
  protected void loadImage(Image image)
  {
    synchronized(tracker) {
      tracker.addImage(image, 0);
      try {
        tracker.waitForID(0, 5000);
      } catch (InterruptedException e) {
        System.out.println("INTERRUPTED while loading Image");
      }
      loadStatus = tracker.statusID(0, false);
      tracker.removeImage(image, 0);

      width = image.getWidth(imageObserver);
      height = image.getHeight(imageObserver);
    }
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
    return loadStatus;
  }

  /**
   * Returns the RestorableImageIcon's Image.
   * @return    The requested image.
   */
  public Image getImage()
  {
    return image;
  }

  /**
   * Set the image displayed by this icon.
   * @param     image   The image to set.
   */
  public void setImage(Image image)
  {
    this.image = image;
    loadImage(image);
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
    return description;
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
    this.description = description;
  }

  /**
   * Paints the Icon.
   */
  public synchronized void paintIcon(Component c, Graphics g, int x, int y)
  {
    if (imageObserver == null) {
      g.drawImage(image, x, y, c);
    }else{
      g.drawImage(image, x, y, imageObserver);
    }
  }

  /**
   * Get the width of the RestoreableImageIcon.
   * @return    The requested width.
   */
  public int getIconWidth()
  {
    return width;
  }

  /**
   * Get the height of the RestorableImageIcon.
   * @return    The requested height.
   */
  public int getIconHeight()
  {
    return height;
  }

  /**
   * Set the image observer for the image.  Set this
   * property if the RestorableImageIcon contains an animated GIF, so
   * the observer is notified to update its display.
   * For example:
   * <pre>
   *     icon = new RestorableImageIcon(...)
   *     button.setIcon(icon);
   *     icon.setImageObserver(button);
   * </pre>
   * @param     observer        The image observer to set.
   */
  public void setImageObserver(ImageObserver observer)
  {
    imageObserver = observer;
  }

  /**
   *  Return the image observer for the image
   * @return    The requested image observer.
   */
  public ImageObserver getImageObserver()
  {
    return imageObserver;
  }

  /**
   * Read the serialized state of the object.
   * @param     s       The stream from which to read the state.
   */
  private void readObject(ObjectInputStream s)
    throws ClassNotFoundException, IOException
  {
    s.defaultReadObject();

    image = ImageIO.read(s);
    loadImage(image);
  }

  /**
   * Serialize the object.
   * @param     s       The stream to which to write the state.
   * @exception IOException     Thrown if writing fails.
   */
  private void writeObject(ObjectOutputStream s) throws IOException
  {
    s.defaultWriteObject();

    saveImage(saveFormat, s);
  }

  /**
   * Specifies what format will be used when serializing the object.
   * @param     format          The format of the image: one of
   *                            <code>GIF</code>, <code>PNG</code>,
   *                            <code>JPEG</code>, <code>BMP</code>.
   * @exception IllegalArgumentException        Thrown if <code>format</code>
   *                            is not one of
   *                            <code>GIF</code>, <code>PNG</code>,
   *                            <code>JPEG</code>, <code>BMP</code>.
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
   * Saves the image of the RestorableImageIcon to a stream.
   * @param     format  The format of the image: one of GIF, PNG, JPEG, BMP.
   * @param     s       The stream to which to save the image.
   * @exception IllegalArgumentException        Thrown if an
   *                            unsupported format is specified.
   * @exception IOException     Thrown if saving fails.
   */
  public void saveImage(int format, OutputStream s)
    throws IllegalArgumentException, IOException
  {
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

}
