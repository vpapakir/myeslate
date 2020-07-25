package gr.cti.eslate.utils;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
 * Utility methods used by both <code>RestorableImageIcon</code> and
 * <code>NewRestorableImageIcon</code>.
 *
 * @version     2.0.17, 27-Jun-2007
 * @author      Kriton Kyrimis.
 */
public class ImageTools
{
  /**
   * Constructor is private, as only static methods are provided.
   */
  private ImageTools()
  {
  }

  /**
   * Returns the bytes of an image converted to a given format.
   * @param     image   The image to save.
   * @param     format  The format to use.
   * @return    The bytes of the converted image.
   */
  public static byte[] saveImage(Image image, String format) throws IOException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ImageIO.write(getRenderedImage(image, format), format, bos);
    bos.close();
    return bos.toByteArray();
  }

  /**
   * Converts an image to a <code>RenderedImage</code>.
   * @param     image   The image to convert.
   * @param     format  The format with which the <code>RenderedImage</code>
   *                    should be compatible.
   * @return    If <code>image</code> is an instance of
   *            <code>RenderedImage</code>, then it is returned unmodified.
   *            Otherwise, a new <code>BufferedImage</code> is returned, onto
   *            which <code>image</code> has been copied.
   */
  public static RenderedImage getRenderedImage(Image image, String format)
  {
    if (image instanceof RenderedImage) {
      return (RenderedImage)image;
    }else{
      format = format.toUpperCase();
      int type;
      if ("GIF".equals(format) || "PNG".equals(format)) {
        type = BufferedImage.TYPE_INT_ARGB;
      }else{
        type = BufferedImage.TYPE_INT_RGB;
      }
      BufferedImage bi =
        new BufferedImage(image.getWidth(null), image.getHeight(null), type);
      Graphics g = bi.getGraphics();
      g.drawImage(image, 0, 0, null);
      g.dispose();
      return bi;
    }
  }
}
