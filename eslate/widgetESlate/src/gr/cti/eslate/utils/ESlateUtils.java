package gr.cti.eslate.utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import gr.cti.eslate.base.*;

/**
 * Various utility methods.
 *
 * @author      George Tsironis
 * @version     2.0.10, 4-Jul-2006
 */
public class ESlateUtils
{
  /**
   * Converts borders to border descriptors that can be serialized in a
   * version-independent manner.
   */
  public static BorderDescriptor
    getBorderDescriptor( Border border, Component comp)
  {
    BorderDescriptor bd = new BorderDescriptor(border);

    if (border == null) return bd;

    if (LineBorder.class.isAssignableFrom(border.getClass())) {
      bd.analyzeBorderAttributes((LineBorder) border);
    }else if (MatteBorder.class.isAssignableFrom(border.getClass())) {
      bd.analyzeBorderAttributes((MatteBorder) border, comp);
    }else if (EmptyBorder.class.isAssignableFrom(border.getClass())) {
      bd.analyzeBorderAttributes((EmptyBorder) border, comp);
    }else if (OneLineBevelBorder.class.isAssignableFrom(border.getClass())) {
      bd.analyzeBorderAttributes((OneLineBevelBorder) border, comp);
    }else if (NoTopOneLineBevelBorder.class.isAssignableFrom(border.getClass())) {
      bd.analyzeBorderAttributes((NoTopOneLineBevelBorder) border, comp);
    }else if (SoftBevelBorder.class.isAssignableFrom(border.getClass())) {
      bd.analyzeBorderAttributes((SoftBevelBorder) border, comp);
    }else if (BevelBorder.class.isAssignableFrom(border.getClass())) {
      bd.analyzeBorderAttributes((BevelBorder) border, comp);
    }else if (EtchedBorder.class.isAssignableFrom(border.getClass())) {
      bd.analyzeBorderAttributes((EtchedBorder) border, comp);
    }else if (TitledBorder.class.isAssignableFrom(border.getClass())) {
      bd.analyzeBorderAttributes((TitledBorder) border, comp);
    }else if (CompoundBorder.class.isAssignableFrom(border.getClass())) {
      bd.analyzeBorderAttributes((CompoundBorder) border, comp);
    }else{
      if (!bd.analyzeBasicBorder(border)) {
        bd.analyzeUnknownBorder(border);
/*        throw new IllegalArgumentException(
          "Cannot analyze border: " + border + "."
        );
*/
      }
    }

    return bd;
  }

  /**
   * Removes the E-Slate menu panel from a component, if it has one.
   * @param     component       The component.
   * @return    True if the menu panel was removed, false otherwise.
   */
  public static boolean removeMenuPanel(Component component)
  {
    if (component instanceof Container) {
      Container container = (Container)component;
      int nComponents = container.getComponentCount();
      for (int i=0; i<nComponents; i++) {
        Component c = container.getComponent(i);
        if (c instanceof ESlateHandle.MenuPanel) {
          container.remove(c);
          if (container instanceof JComponent) {
            ((JComponent)c).revalidate();
          }else{
            c.invalidate();
            c.validate();
          }
          return true;
        }else{
          if (c instanceof Container) {
            boolean removed = removeMenuPanel(c);
            if (removed) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
}
