package gr.cti.eslate.utils;

import java.util.Hashtable;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Component;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicBorders;

import com.sun.java.swing.plaf.windows.WindowsBorders;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.OneLineBevelBorder;
import gr.cti.eslate.utils.StorageStructure;

/**
 * Utility for converting borders to a form that can be stored using a stable
 * format.
 *
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
@SuppressWarnings(value={"unchecked"})
public class BorderDescriptor implements java.io.Externalizable {
    public static final int STR_FORMAT_VERSION = 3;
    static final long serialVersionUID = 12;
    Class borderClass = null;
    Hashtable borderAttributes = new Hashtable();
//    boolean borderAnalyzed = false;
    byte[] unknownBorderState = null;

    public BorderDescriptor(Border border) {
//        if (border == null)  throw new NullPointerException();
        if (border != null)
            borderClass = border.getClass();
        else
            borderClass = null;
    }

    public BorderDescriptor() {
    }

    public Class getBorderClass() {
        return borderClass;
    }

    /* Tries to save the state of an unknown border, i.e. a border which can not be
     * analyzed by one of the 'analyze...()' methods. An unknown border will persist
     * only if it implements the Externalizable i/f. This happens because all borders
     * normally extend the AbstractBorder class, which is Serializable. So they are
     * all Serializable and they run the danger that their state won't be restorable
     * if the AbstractBorder class changes in an incompatible way. So only Externalizable
     * borders are saved.
     */
    protected void analyzeUnknownBorder(Border border) {
        if (border != null && borderClass != null) {
            if (Externalizable.class.isAssignableFrom(border.getClass())) {
                try{
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(stream);
                    ((Externalizable) border).writeExternal(oos);
                    oos.flush();
                    unknownBorderState = stream.toByteArray();
                    oos.close();
                }catch (Throwable thr) {
                    System.out.println("Problems while saving the state of the border of class " + border.getClass().getName() + '.');
                    unknownBorderState = new byte[0];
                    thr.printStackTrace();
                }
            }else{
//                System.out.println("Unable to save the state of the border of class " + border.getClass().getName() + ". The border is not Externalizable.");
                unknownBorderState = new byte[0];
            }
        }
    }

    /**
     * Explore the LineBorder's attributes
     */
    protected void analyzeBorderAttributes(LineBorder border) {
        Color c = border.getLineColor();
        int thickNess = border.getThickness();
        borderAttributes.put("Thickness", new Integer(thickNess));
        if (c != null)
            borderAttributes.put("Color", c);
    }

    /**
     * Explore the MatteBorder's attributes
     */
    protected void analyzeBorderAttributes(MatteBorder border, Component comp) {
        /* This try/catch has to do with the fact that MatteBorders acquired
         * the getMatteColor() and getTileIcon() methods in Java 1.3. These
         * methods were obsolete before 1.3.
         */
        try{
            Insets insets = border.getBorderInsets(comp);
            if (insets != null)
                borderAttributes.put("Insets", insets);
            Color c = border.getMatteColor();
            javax.swing.Icon icon = border.getTileIcon();
            if (icon != null) {
                if (ImageIcon.class.isAssignableFrom(icon.getClass())) {
                    icon = new NewRestorableImageIcon(((ImageIcon) icon).getImage());
                }
                if (icon != null && NewRestorableImageIcon.class.isAssignableFrom(icon.getClass()))
                    borderAttributes.put("Icon", icon);
            }
            if (c != null)
                borderAttributes.put("Color", c);
        }catch (Throwable thr) {
            thr.printStackTrace();
            System.out.println("Border " + border + " was only partially analyzed.");
        }
    }

    /**
     * Explore the EmptyBorder's attributes
     */
    protected void analyzeBorderAttributes(EmptyBorder border, Component comp) {
        Insets insets = border.getBorderInsets(comp);
        if (insets != null)
            borderAttributes.put("Insets", insets);
    }

    /**
     * Explore the OneLineBevelBorder's attributes
     */
    protected void analyzeBorderAttributes(OneLineBevelBorder border, Component comp) {
        int bevelType = border.getBevelType();
        boolean visible = comp.isVisible();
        if (!visible)
            comp.setVisible(true);
        Color highlightColor = border.getHighlightOuterColor(comp);
        Color shadowColor = border.getShadowOuterColor(comp);
        if (!visible)
            comp.setVisible(visible);

        borderAttributes.put("BevelType", new Integer(bevelType));
        if (highlightColor != null)
            borderAttributes.put("HighlightInnerColor", highlightColor);
        if (shadowColor != null)
            borderAttributes.put("ShadowColor", shadowColor);
    }

    /**
     * Explore the NoTopOneLineBevelBorder's attributes
     */
    protected void analyzeBorderAttributes(NoTopOneLineBevelBorder border, Component comp) {
        int bevelType = border.getBevelType();
        Color highlightColor = border.getHighlightOuterColor(comp);
        Color shadowColor = border.getShadowOuterColor(comp);

        borderAttributes.put("BevelType", new Integer(bevelType));
        if (highlightColor != null)
            borderAttributes.put("HighlightInnerColor", highlightColor);
        if (shadowColor != null)
            borderAttributes.put("ShadowColor", shadowColor);
    }

    /**
     * Explore the BevelBorder's attributes
     */
    protected void analyzeBorderAttributes(BevelBorder border, Component comp) {
        int bevelType = border.getBevelType();
        Color highlightInnerColor = border.getHighlightInnerColor(comp);
        Color highlightOuterColor = border.getHighlightOuterColor(comp);
        Color shadowInnerColor = border.getShadowInnerColor(comp);
        Color shadowOuterColor = border.getShadowOuterColor(comp);

        borderAttributes.put("BevelType", new Integer(bevelType));
        if (highlightInnerColor != null)
            borderAttributes.put("HighlightInnerColor", highlightInnerColor);
        if (highlightOuterColor != null)
            borderAttributes.put("HighlightOuterColor", highlightOuterColor);
        if (shadowInnerColor != null)
            borderAttributes.put("ShadowInnerColor", shadowInnerColor);
        if (shadowOuterColor != null)
            borderAttributes.put("ShadowOuterColor", shadowOuterColor);
    }

    /**
     * Explore the EtchedBorder's attributes
     */
    protected void analyzeBorderAttributes(EtchedBorder border, Component comp) {
        int etchType = border.getEtchType();
        Color highlightColor = border.getHighlightColor(comp);
        Color shadowColor = border.getShadowColor(comp);

        borderAttributes.put("EtchType", new Integer(etchType));
        if (highlightColor != null)
            borderAttributes.put("HighlightColor", highlightColor);
        if (shadowColor != null)
            borderAttributes.put("ShadowColor", shadowColor);
    }

    /**
     * Explore the TitledBorder's attributes
     */
    protected void analyzeBorderAttributes(TitledBorder border, Component comp) {
        String title = border.getTitle();
        int titleJustification = border.getTitleJustification();
        int titlePosition = border.getTitlePosition();
        Font font = border.getTitleFont();
        Color color = border.getTitleColor();
        BorderDescriptor bd = ESlateUtils.getBorderDescriptor(border.getBorder(), comp);

        if (title != null)
            borderAttributes.put("Title", title);
        borderAttributes.put("TitleJustification", new Integer(titleJustification));
        borderAttributes.put("TitlePosition", new Integer(titlePosition));
        if (font != null)
            borderAttributes.put("Font", font);
        if (color != null)
            borderAttributes.put("Color", color);
        if (border != null)
            borderAttributes.put("Border", bd);
    }

    /**
     * Explore the CompoundBorder's attributes
     */
    protected void analyzeBorderAttributes(CompoundBorder border, Component comp) {
        BorderDescriptor bd1 = ESlateUtils.getBorderDescriptor(border.getInsideBorder(), comp);
        BorderDescriptor bd2 = ESlateUtils.getBorderDescriptor(border.getOutsideBorder(), comp);

        if (bd1 != null)
            borderAttributes.put("InsideBorder", bd1);
        if (bd2 != null)
            borderAttributes.put("OutsideBorder", bd2);
    }

    /**
     * Explore the attributes of one of the basic borders.
     * @param   border  The border whose attributes should be analyzed.
     * @return  True if the border was recognized as one of the basic
     *          borders, false otherwise.
     */
    protected boolean analyzeBasicBorder(Border border)
    {
      String s = border.getClass().getName();
      if (s.equals("javax.swing.plaf.basic.BasicBorders$ButtonBorder") ||
          s.equals("javax.swing.plaf.basic.BasicBorders$RadioButtonBorder") ||
          s.equals("javax.swing.plaf.basic.BasicBorders$ToggleButtonBorder") ||
          s.equals("javax.swing.plaf.basic.BasicBorders$MenuBarBorder") ||
          s.equals("javax.swing.plaf.basic.BasicBorders$SplitPaneBorder") ||
          s.equals("javax.swing.plaf.basic.BasicBorders$SplitPaneDividerBorder") ||
          s.equals("javax.swing.plaf.basic.BasicBorders$TextFieldBorder") ||
          s.equals("javax.swing.plaf.basic.BasicBorders$ProgressBarBorder") ||
          s.equals("javax.swing.plaf.basic.BasicBorders$InternalFrameBorder") ||
          s.equals("javax.swing.plaf.basic.BasicBorders$MarginBorder") ||

          s.equals("com.sun.java.swing.plaf.windows.WindowsBorders$ProgressBarBorder") ||
          s.equals("com.sun.java.swing.plaf.windows.WindowsBorders$ToolBarBorder") ||
          s.equals("com.sun.java.swing.plaf.windows.WindowsBorders$FocusCellHighlightBorder") ||
          s.equals("com.sun.java.swing.plaf.windows.WindowsBorders$TableHeaderBorder") ||
          s.equals("com.sun.java.swing.plaf.windows.WindowsBorders$InternalFrameBorder")) {
        borderAttributes.put("BasicBorderClass", s);
        return true;
      }else{
        return false;
      }
    }

    /**
     * Restores one of the basic borders.
     * @return  The restored border. If the border is not one of the basic
     *          borders, <code>null</code> is returned.
     */
    private Border restoreBasicBorder()
    {
      String s = (String)borderAttributes.get("BasicBorderClass");
      if (s != null) {
        return restoreBasicBorder(s);
      }else{
        return null;
      }
    }

    /**
     * Restores one of the basic borders.
     * @param   s       The name of the class of the border to restore.
     * @return  The restored border. If the name of the class of the border
     *          is not one of the basic borders, <code>null</code> is returned.
     */
    private Border restoreBasicBorder(String s)
    {
      if (s.equals("javax.swing.plaf.basic.BasicBorders$ButtonBorder")) {
        return BasicBorders.getButtonBorder();
      }
      if (s.equals("javax.swing.plaf.basic.BasicBorders$RadioButtonBorder")) {
        return BasicBorders.getRadioButtonBorder();
      }
      if (s.equals("javax.swing.plaf.basic.BasicBorders$ToggleButtonBorder")) {
        return BasicBorders.getToggleButtonBorder();
      }
      if (s.equals("javax.swing.plaf.basic.BasicBorders$MenuBarBorder")) {
        return BasicBorders.getMenuBarBorder();
      }
      if (s.equals("javax.swing.plaf.basic.BasicBorders$SplitPaneBorder")) {
        return BasicBorders.getSplitPaneBorder();
      }
      if (s.equals("javax.swing.plaf.basic.BasicBorders$SplitPaneDividerBorder")) {
        return BasicBorders.getSplitPaneDividerBorder();
      }
      if (s.equals("javax.swing.plaf.basic.BasicBorders$TextFieldBorder")) {
        return BasicBorders.getTextFieldBorder();
      }
      if (s.equals("javax.swing.plaf.basic.BasicBorders$ProgressBarBorder")) {
        return BasicBorders.getProgressBarBorder();
      }
      if (s.equals("javax.swing.plaf.basic.BasicBorders$InternalFrameBorder")) {
        return BasicBorders.getInternalFrameBorder();
      }
      if (s.equals("javax.swing.plaf.basic.BasicBorders$MarginBorder")) {
        return new BasicBorders.MarginBorder();
      }
      if (s.equals("com.sun.java.swing.plaf.windows.WindowsBorders$ProgressBarBorder")) {
        return WindowsBorders.getProgressBarBorder();
      }
      if (s.equals("com.sun.java.swing.plaf.windows.WindowsBorders$ToolBarBorder")) {
        return WindowsBorders.getToolBarBorder();
      }
      if (s.equals("com.sun.java.swing.plaf.windows.WindowsBorders$FocusCellHighlightBorder")) {
        return WindowsBorders.getFocusCellHighlightBorder();
      }
      if (s.equals("com.sun.java.swing.plaf.windows.WindowsBorders$TableHeaderBorder")) {
        return WindowsBorders.getTableHeaderBorder();
      }
      if (s.equals("com.sun.java.swing.plaf.windows.WindowsBorders$InternalFrameBorder")) {
        return WindowsBorders.getInternalFrameBorder();
      }
      return null;
    }

    public Hashtable getAttributes() {
        return borderAttributes;
    }

    public Border getBorder() {
//System.out.println("BorderDescriptor borderClass: " + borderClass);
        if (borderClass == null) return null;

        Border border = null;
        if (unknownBorderState == null) {
            if (LineBorder.class.isAssignableFrom(borderClass)) {
                Color color = (Color) borderAttributes.get("Color");
                int thickness = ((Integer) borderAttributes.get("Thickness")).intValue();
                border = new LineBorder(color, thickness);
            }else if (MatteBorder.class.isAssignableFrom(borderClass)) {
                Color color = (Color) borderAttributes.get("Color");
                Insets insets = (Insets) borderAttributes.get("Insets");
                Icon icon = (Icon) borderAttributes.get("Icon");
                if (icon == null)
                    border = new MatteBorder(insets.top, insets.left, insets.bottom, insets.right, color);
                else
                    border = new MatteBorder(insets.top, insets.left, insets.bottom, insets.right, icon);
            }else if (EmptyBorder.class.isAssignableFrom(borderClass)) {
                Insets insets = (Insets) borderAttributes.get("Insets");
                border = new EmptyBorder(insets);
            }else if (OneLineBevelBorder.class.isAssignableFrom(borderClass)) {
                int bevelType = ((Integer) borderAttributes.get("BevelType")).intValue();
                Color highlightColor = (Color) borderAttributes.get("HighlightColor");
                Color shadowColor = (Color) borderAttributes.get("ShadowColor");
                border = new OneLineBevelBorder(bevelType, highlightColor, shadowColor);
            }else if (NoTopOneLineBevelBorder.class.isAssignableFrom(borderClass)) {
                int bevelType = ((Integer) borderAttributes.get("BevelType")).intValue();
                Color highlightColor = (Color) borderAttributes.get("HighlightColor");
                Color shadowColor = (Color) borderAttributes.get("ShadowColor");
                border = new NoTopOneLineBevelBorder(bevelType, highlightColor, shadowColor);
            }else if (SoftBevelBorder.class.isAssignableFrom(borderClass)) {
                int bevelType = ((Integer) borderAttributes.get("BevelType")).intValue();
                Color hightlightInnerColor = (Color) borderAttributes.get("HighlightInnerColor");
                Color highlightOuterColor = (Color) borderAttributes.get("HighlightOuterColor");
                Color shadowInnerColor = (Color) borderAttributes.get("ShadowInnerColor");
                Color shadowOuterColor = (Color) borderAttributes.get("ShadowOuterColor");
                border = new SoftBevelBorder(bevelType, highlightOuterColor, hightlightInnerColor, shadowOuterColor, shadowInnerColor);
            }else if (BevelBorder.class.isAssignableFrom(borderClass)) {
                int bevelType = ((Integer) borderAttributes.get("BevelType")).intValue();
                Color hightlightInnerColor = (Color) borderAttributes.get("HighlightInnerColor");
                Color highlightOuterColor = (Color) borderAttributes.get("HighlightOuterColor");
                Color shadowInnerColor = (Color) borderAttributes.get("ShadowInnerColor");
                Color shadowOuterColor = (Color) borderAttributes.get("ShadowOuterColor");
                border = new BevelBorder(bevelType, highlightOuterColor, hightlightInnerColor, shadowOuterColor, shadowInnerColor);
            }else if (EtchedBorder.class.isAssignableFrom(borderClass)) {
                int etchType = ((Integer) borderAttributes.get("EtchType")).intValue();
                Color hightlightColor = (Color) borderAttributes.get("HighlightColor");
                Color shadowColor = (Color) borderAttributes.get("ShadowColor");
                border = new EtchedBorder(etchType, hightlightColor, shadowColor);
            }else if (TitledBorder.class.isAssignableFrom(borderClass)) {
                String title = (String) borderAttributes.get("Title");
                int titleJustification = ((Integer) borderAttributes.get("TitleJustification")).intValue();
                int titlePosition = ((Integer) borderAttributes.get("TitlePosition")).intValue();
                Font font = (Font) borderAttributes.get("Font");
                Color color = (Color) borderAttributes.get("Color");
                Border innerBorder = ((BorderDescriptor) borderAttributes.get("Border")).getBorder();
                border = new TitledBorder(innerBorder, title, titleJustification, titlePosition, font, color);
            }else if (CompoundBorder.class.isAssignableFrom(borderClass)) {
                Border insideBorder = ((BorderDescriptor) borderAttributes.get("InsideBorder")).getBorder();
                Border outsideBorder = ((BorderDescriptor) borderAttributes.get("OutsideBorder")).getBorder();
                border = new CompoundBorder(outsideBorder, insideBorder);
            }else{
              border = restoreBasicBorder();
              if (border == null) {
                // Try to call the zero-arg constuctor of this border, if it exists.
                try{
                    border = (Border) borderClass.newInstance();
                }catch (Throwable thr) {
                    return null;
                }
              }
            }
        }else{
          // Try to restore one of the basic borders. This will restore any
          // basic borders for which support was added agfter the border
          // descriptor was saved.
          border = restoreBasicBorder(borderClass.getName());
          if (border == null) {
            /* Try to restore an unknown border. First we try to create and return the
             * default instance of the border, by calling the zero-arg constructor.
             * If this constructor does not exist, then null is returned. Then if the
             * border is Externalizable, we restore its state. Otherwise the already
             * created border instance is returned.
             */
            try{
                border = (Border) borderClass.newInstance();
                if (border != null && Externalizable.class.isAssignableFrom(borderClass)) {
                    try{
                        ByteArrayInputStream stream = new ByteArrayInputStream(unknownBorderState);
                        ObjectInputStream ois = new ObjectInputStream(stream);
                        ((Externalizable) border).readExternal(ois);
                        ois.close();
                    }catch (Throwable thr) {
                      thr.printStackTrace();
                    }
                }else;
//                    System.out.println("Unable to restore the state of border " + borderClass.getName() + ". The border is not Externalizable.");
            }catch (Throwable thr2) {
                System.out.println("Unable to instantiate a border of class " + borderClass.getName() + ". Probably there is no zero-argument constructor in this class");
                //thr2.printStackTrace();
                System.out.println(
                  thr2.getClass().getName() + ": " + thr2.getMessage()
                );
            }
          }
        }

        return border;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STR_FORMAT_VERSION, 3);
//1.0        fieldMap.put("BorderClass", borderClass);
        if (borderClass != null)
            fieldMap.put("BorderClassName", borderClass.getName());
        fieldMap.put("BorderAttributes", borderAttributes);
        fieldMap.put("UnknownBorderState", unknownBorderState);
        out.writeObject(fieldMap);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        /* In data format version 3.0, support for unknown border classes was added,
         * through the 'unknownBorderState' variable and the 'analyzeUmnownBorder()' method.
         */
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        if (fieldMap.getDataVersion().startsWith("1."))
            borderClass = (Class) fieldMap.get("BorderClass");
        else{
            String borderClassName = (String) fieldMap.get("BorderClassName");
            if (borderClassName != null) {
                try{
                    borderClass = Class.forName(borderClassName);
                }catch (Throwable thr) {
                    System.out.println("Error while restoring border of class: " + borderClassName);
                    thr.printStackTrace();
                }
            }else
                borderClass = null;
        }
        borderAttributes = (Hashtable) fieldMap.get("BorderAttributes");
        unknownBorderState = (byte[]) fieldMap.get("UnknownBorderState", (Object) null);
    }
}
