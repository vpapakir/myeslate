package gr.cti.eslate.base;

import gr.cti.eslate.base.sharedObject.*;
import java.awt.Color;
import java.util.*;

import javax.swing.*;

/**
 * Implements single input shared object plugs.
 *
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public class SingleInputPlug extends SharedObjectPlug
{
  /**
   * Constructs a single input shared object plug.
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
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public SingleInputPlug(ESlateHandle handle, ResourceBundle bundle, String key,
                        Color cl, Class shObjClass, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, l);
    if (l == null) {
      InvalidPlugParametersException e =
        new InvalidPlugParametersException(resources.getString("noListener"));
      throw e;
    }
  }

  /**
   * Returns the icon that represents the plug.
   * @return    The icon.
   */
  public ImageIcon getIcon() {
    if (internals.providerPlugs.size() == 0) {
      if (icon == null) {
        icon = createPlugIcon(Plug.class.getResource("si.gif"));
      }
      return icon;
    } else {
      if (icon_i == null) {
        icon_i = createPlugIcon(Plug.class.getResource("si_i.gif"));
      }
      return icon_i;
    }
  }

  /**
   * Icon shown when plug is not connected.
   */
  private ImageIcon icon = null;

  /**
   * Icon shown when plug is connected.
   */
  private ImageIcon icon_i = null;
}
