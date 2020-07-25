package gr.cti.eslate.base;

import gr.cti.eslate.base.sharedObject.*;
import java.awt.Color;
import java.util.*;

import javax.swing.*;

/**
 * Implements single output shared object plugs.
 *
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public class SingleOutputPlug extends SharedObjectPlug
{
  /**
   * Constructs a single output shared object plug.
   * @param     handle          The E-Slate handle owned by the component to
   *                            which the plug will belong.
   * @param     bundle          The resource bundle containing the name of the
   *                            plug. This parameter can be null, in which
   *                            case <code>key</code> is the actual name of
   *                            the plug. Note that if <code>bundle</code> is
   *                            null and the name of the plug depens on the
   *                            locale, then it will not be possible to
   *                            restore plug connections in a microworld that
   *                            is loaded in a different locale from the
   *                            one in which it was created.
   * @param     key             The resource bundle key to the name of the
   *                            plug. If <code>bundle</code> is null,
   *                            then <code>key</code> is the actual name of
   *                            the plug. See the note above.
   * @param     cl              The color of the plug.
   * @param     shObjClass      The class of the sharable object shared
   *                            by the plug.
   * @param     so              The shared object associated with the plug.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public SingleOutputPlug(ESlateHandle handle, ResourceBundle bundle,
                         String key, Color cl, Class shObjClass,
                         SharedObject so)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, null);
  }

  /**
   * Returns the icon that represents the plug.
   * @return    The icon.
   */
  public ImageIcon getIcon()
  {
    if (internals.dependentPlugs.size() == 0) {
      if (icon == null) {
        icon = createPlugIcon(Plug.class.getResource("so.gif"));
      }
      return icon;
    } else {
      if (icon_o == null) {
        icon_o = createPlugIcon(Plug.class.getResource("so_o.gif"));
      }
      return icon_o;
    }
  }

  /**
   * Icon shown when plug is not connected.
   */
  private ImageIcon icon = null;

  /**
   * Icon shown when plug is connected.
   */
  private ImageIcon icon_o = null;
}
