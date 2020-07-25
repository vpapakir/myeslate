package gr.cti.eslate.editor;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import org.netbeans.editor.*;

/**
 * This class provides static methods for the handling of font options.
 *
 * @version     2.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public class FontOptions
{
  /**
   * The constructor is private, as there is no need to create instances of
   * this object.
   */
  private FontOptions()
  {
  }

  /**
   * Sets the font for a particular coloring of a particular editor kit.
   * @param     kitClass        The class of the editor kit.
   * @param     coloring        The name of the coloring.
   * @param     font            The font to set.
   */
  public static void setFont(Class kitClass, String coloring, Font font)
  {
    FontAndColor fac = getFontAndColor(kitClass, coloring);
    Coloring newColoring = new Coloring(
      font, Coloring.FONT_MODE_DEFAULT, fac.fore, fac.back
    );
    setColoring(kitClass, coloring, newColoring);
  }

  /**
   * Returns the font for a particular coloring of a particular editor kit.
   * @param     kitClass        The class of the editor kit.
   * @param     coloring        The name of the coloring.
   * @return    The requested font.
   */
  public static Font getFont(Class kitClass, String coloring)
  {
    FontAndColor fac = getFontAndColor(kitClass, coloring);
    return fac.font;
  }

  /**
   * Returns the font and color for a particular coloring of a particular
   * editor kit.
   * @param     kitClass        The class of the editor kit.
   * @param     coloring        The name of the coloring.
   * @return    The requested font and color.
   */
  public static FontAndColor getFontAndColor(Class kitClass, String coloring)
  {
    Coloring c = SettingsUtil.getColoring(kitClass, coloring, false);
    Coloring c2 = SettingsUtil.getColoring(kitClass, "default", false);
    Font font = c.getFont();
    if (font == null) {
      font = c2.getFont();
    }
    Color fore = c.getForeColor();
    if (fore == null) {
      fore = c2.getForeColor();
    }
    Color back = c.getBackColor();
    if (back == null) {
      back = c2.getBackColor();
    }
    return new FontAndColor(font, fore, back);
  }

  /**
   * Sets the font and color for a particular coloring of a particular
   * editor kit.
   * @param     kitClass        The class of the editor kit.
   * @param     coloring        The name of the coloring.
   * @param     fac             The font and color to set.
   */
  public static void setFontAndColor(Class kitClass, String coloring,
                                     FontAndColor fac)
  {
    Coloring newColoring = new Coloring(
      fac.font, Coloring.FONT_MODE_DEFAULT, fac.fore, fac.back
    );
    setColoring(kitClass, coloring, newColoring);
  }

  /**
   * Sets the font size for all colorings of all supported editor kits
   * to the same value. The colorings referring to the status bar are not
   * affected.
   * @param     size    The new font size.
   */
  public static void setFontSize(int size)
  {
    for (int i=0; i<Editor.supportedKits.length; i++) {
      setFontSize(Editor.supportedKits[i], size);
    }
  }

  /**
   * Sets the font size for all colorings of a particular editor kit to the
   * same value. The colorings referring to the status bar are not affected.
   * @param     kitClass        The class of the editor kit.
   * @param     size            The new font size.
   */
  @SuppressWarnings(value={"unchecked"})
  public static void setFontSize(Class kitClass, int size)
  {
    Map cm = SettingsUtil.getColoringMap(kitClass, false, true);
    if (cm != null) {
      FontAndColor statusBarFAC = getFontAndColor(kitClass, "status-bar");
      FontAndColor statusBarBoldFAC =
        getFontAndColor(kitClass, "status-bar-bold");
      Iterator it = cm.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry)it.next();
        Object value = entry.getValue();
        if (value instanceof Coloring) {
          Coloring c = (Coloring)value;
          Font font = c.getFont();
          if (font != null) {
            font = font.deriveFont((float)size);
            Coloring newColoring = new Coloring(
              font, Coloring.FONT_MODE_DEFAULT, c.getForeColor(), c.getBackColor()
            );
            entry.setValue(newColoring);
          }
        }
      }
      SettingsUtil.setColoringMap(kitClass, cm, false);
      statusBarFAC.back = UIManager.getColor("Panel.background");
      statusBarFAC.fore = UIManager.getColor("Panel.foreground");
      setFontAndColor(kitClass, "status-bar", statusBarFAC);
      setFontAndColor(kitClass, "status-bar-bold", statusBarBoldFAC);
    }
  }

  /**
   * Sets the font for all colorings of all supported editor kits
   * to the same font. The colorings referring to the status bar are not
   * affected.
   * @param     family  The name of the new font.
   */
  public static void setFont(String family)
  {
    for (int i=0; i<Editor.supportedKits.length; i++) {
      setFont(Editor.supportedKits[i], family);
    }
  }

  /**
   * Sets the font for all colorings of a particular editor kit to the
   * same value. The colorings referring to the status bar are not affected.
   * @param     kitClass        The class of the editor kit.
   * @param     family          The name of the new font.
   */
  @SuppressWarnings(value={"unchecked"})
  public static void setFont(Class kitClass, String family)
  {
    Map cm = SettingsUtil.getColoringMap(kitClass, false, true);
    if (cm != null) {
      FontAndColor statusBarFAC = getFontAndColor(kitClass, "status-bar");
      FontAndColor statusBarBoldFAC =
        getFontAndColor(kitClass, "status-bar-bold");
      Iterator it = cm.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry)it.next();
        Object value = entry.getValue();
        if (value instanceof Coloring) {
          Coloring c = (Coloring)value;
          Font font = c.getFont();
          if (font != null) {
            if (!font.getFamily().equals(family)) {
              font = new Font(family, font.getStyle(), font.getSize());
              Coloring newColoring = new Coloring(
                font, Coloring.FONT_MODE_DEFAULT,
                c.getForeColor(), c.getBackColor()
              );
              entry.setValue(newColoring);
            }
          }
        }
      }
      SettingsUtil.setColoringMap(kitClass, cm, false);
      statusBarFAC.back = UIManager.getColor("Panel.background");
      statusBarFAC.fore = UIManager.getColor("Panel.foreground");
      setFontAndColor(kitClass, "status-bar", statusBarFAC);
      setFontAndColor(kitClass, "status-bar-bold", statusBarBoldFAC);
    }
  }

  /**
   * Ensures that the status bar has the correct colors specified by the UI.
   */
  public static void updateStatusBarFromUI()
  {
    for (int i=0; i<Editor.supportedKits.length; i++) {
      Class kitClass = Editor.supportedKits[i];
      FontAndColor statusBarFAC = getFontAndColor(kitClass, "status-bar");
      FontAndColor statusBarBoldFAC =
        getFontAndColor(kitClass, "status-bar-bold");
      statusBarFAC.back = UIManager.getColor("Panel.background");
      statusBarFAC.fore = UIManager.getColor("Panel.foreground");
      setFontAndColor(kitClass, "status-bar", statusBarFAC);
      setFontAndColor(kitClass, "status-bar-bold", statusBarBoldFAC);
    }
  }

  /**
   * Sets the coloring for a particular coloring of a particular editor kit.
   * @param     kitClass        The class of the editor kit.
   * @param     coloring        The name of the coloring.
   * @param     c               The coloring sto set.
   */
  @SuppressWarnings(value={"unchecked"})
  private static void setColoring(Class kitClass, String coloring, Coloring c)
  {
    Map cm = SettingsUtil.getColoringMap(kitClass, false, true);
    if (cm != null) {
      Iterator it = cm.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry)it.next();
        Object value = entry.getValue();
        String key = (String)(entry.getKey());
        if (value instanceof Coloring) {
          if (coloring.equals(key)) {
            entry.setValue(c);
            break;
          }
        }
      }
      SettingsUtil.setColoringMap(kitClass, cm, false);
    }
  }

  /**
   * Returns the names of all the colorings of a particular editor kit.
   * @param     kitClass        The class of the editor kit.
   * @return    An array containing the names of the colorings.
   */
  @SuppressWarnings(value={"unchecked"})
  public static String[] getColoringNames(Class kitClass)
  {
    Map cm = SettingsUtil.getColoringMap(kitClass, false, true);
    ArrayList a = new ArrayList();
    if (cm != null) {
      Iterator it = cm.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry)it.next();
        String key = (String)(entry.getKey());
        // Allowing the user to change the font of the status bar can cause
        // havoc, so we hide the relevant entries.
        if (!key.startsWith("status-bar")) {
          a.add(entry.getKey());
        }
      }
    }
    int n = a.size();
    String s[] = new String[n];
    for (int i=0; i<n; i++) {
      s[i] = (String)(a.get(i));
    }
    return s;
  }

  /**
   * Sets the foreground color of a particular coloring of a particular editor
   * kit.
   * @param     kitClass        The class of the editor kit.
   * @param     coloring        The name of the coloring.
   * @param     color           The new foreground color.
   */
  public static void setForeColor(Class kitClass, String coloring, Color color)
  {
    FontAndColor fac = getFontAndColor(kitClass, coloring);
    Coloring newColoring = new Coloring(
      fac.font, Coloring.FONT_MODE_DEFAULT, color, fac.back
    );
    setColoring(kitClass, coloring, newColoring);
  }

  /**
   * Returns the foreground color of a particular coloring of a particular
   * editor kit.
   * @param     kitClass        The class of the editor kit.
   * @param     coloring        The name of the coloring.
   * @return    The requested color.
   */
  public static Color getForeColor(Class kitClass, String coloring)
  {
    FontAndColor fac = getFontAndColor(kitClass, coloring);
    return fac.fore;
  }

  /**
   * Sets the background color of a particular coloring of a particular editor
   * kit.
   * @param     kitClass        The class of the editor kit.
   * @param     coloring        The name of the coloring.
   * @param     color           The new background color.
   */
  public static void setBackColor(Class kitClass, String coloring, Color color)
  {
    FontAndColor fac = getFontAndColor(kitClass, coloring);
    Coloring newColoring = new Coloring(
      fac.font, Coloring.FONT_MODE_DEFAULT, fac.fore, color
    );
    setColoring(kitClass, coloring, newColoring);
  }

  /**
   * Returns the background color of a particular coloring of a particular
   * editor kit.
   * @param     kitClass        The class of the editor kit.
   * @param     coloring        The name of the coloring.
   * @return    The requested color.
   */
  public static Color getBackColor(Class kitClass, String coloring)
  {
    FontAndColor fac = getFontAndColor(kitClass, coloring);
    return fac.back;
  }
}
