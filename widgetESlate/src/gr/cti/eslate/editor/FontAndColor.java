package gr.cti.eslate.editor;

import java.awt.*;
import java.io.*;

import gr.cti.eslate.utils.*;

/**
 * This class encapsulates a font and its foreground and background colors.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class FontAndColor implements Externalizable
{
  /**
   * The encapsulated font.
   */
  public Font font;
  /**
   * The foreground color of the encapsulated font.
   */
  public Color fore;
  /**
   * The background color of the encapsulated font.
   */
  public Color back;

  private final static String FAMILY = "family";
  private final static String STYLE = "style";
  private final static String SIZE = "size";
  private final static String FORE = "fore";
  private final static String BACK = "back";
  private final static int saveFormatVersion = 1;

  static final long serialVersionUID = 1L;

  /**
   * Construct a new font / color encapsulation.
   * @param     font    The encapsulated font.
   * @param     fore    The foreground color of the encapsulated font.
   * @param     back    The background color of the encapsulated font.
   */
  public FontAndColor(Font font, Color fore, Color back)
  {
    super();
    this.font = font;
    this.fore = fore;
    this.back = back;
  }

  /**
   * Construct an empty font / color encapsulation.
   */
  public FontAndColor()
  {
    super();
    font = null;
    fore = null;
    back = null;
  }

  /**
   * Save the font / color encapsulation.
   * @param     out     The stream on which to save the data.
   * @exception IOException     Thrown if an I/O error occurs.
   */
  public void writeExternal(ObjectOutput out) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(saveFormatVersion, 5);
    if (font != null) {
      String family = fixFamily(font.getFamily());
      map.put(FAMILY, family);
      map.put(STYLE, font.getStyle());
      map.put(SIZE, font.getSize());
    }
    if (fore != null) {
      map.put(FORE, new Integer(fore.getRGB()));
    }
    if (back != null) {
      map.put(BACK, new Integer(back.getRGB()));
    }
    out.writeObject(map);
  }

  /**
   * Load the font / color encapsulation.
   * @param     in      The stream from which to load the data.
   * @exception IOException             Thrown if an I/O error occurs.
   * @exception ClassNotFoundException  Thrown ifthe class for an object being
   *                                    restored cannot be found.
   */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)(in.readObject());
    String family = (String)(map.get(FAMILY));
    if (family != null) {
      family = fixFamily(family); // For compatibility with older versions.
      int style = map.get(STYLE, 0);
      int size = map.get(SIZE, 0);
      font = new Font(family, style, size);
    }else{
      font = null;
    }
    Integer fg = (Integer)(map.get(FORE));
    if (fg != null) {
      fore = new Color(fg.intValue(), true);
    }
    Integer bg = (Integer)(map.get(BACK));
    if (bg != null) {
      back = new Color(bg.intValue(), true);
    }
  }

  /**
   * Trim style names from the family names of standard (monospaced, serif,
   * sansserif, dialog) fonts.  Standard fonts contain the style as part of
   * the family name, causing havoc when trying to re-create fonts from the
   * family names.
   * @param     family  A font family name.
   * @return    If <code>family</code> begins with one of the standard names
   *            (monospaced, serif, sansserif, dialog), followed by a period,
   *            then the period and everything following it is trimmed off,
   *            abd the result is returned. Otherwise, <code>family</code> is
   *            returned unmodified.
   */
  private static String fixFamily(String family)
  {
    if (family.startsWith("monospaced.")) {
      return "monospaced";
    }
    if (family.startsWith("dialog.")) {
      return "dialog";
    }
    if (family.startsWith("sansserif.")) {
      return "sansserif";
    }
    if (family.startsWith("serif.")) {
      return "serif";
    }
    return family;
  }
}
