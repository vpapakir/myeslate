package gr.cti.eslate.base;

import gr.cti.eslate.base.sharedObject.*;
import java.awt.Color;
import java.util.*;

import javax.swing.*;

/**
 * Implements single input, multiple output shared object plugs.
 *
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public class SingleInputMultipleOutputPlug extends SharedObjectPlug
{
  /**
   * Constructs a single input, multiple output shared object plug.
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
   * @param     l               A listener for receiving notification
   *                            events whenever the shared object of the
   *                            plug connected to this plug changes.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public SingleInputMultipleOutputPlug(ESlateHandle handle,
                                      ResourceBundle bundle, String key,
                                      Color cl, Class shObjClass,
                                      SharedObject so, SharedObjectListener l)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, shObjClass, so, l);
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
  public ImageIcon getIcon()
  {
    int providers = internals.providerPlugs.size();
    int dependents = internals.dependentPlugs.size();

    if (providers > 0 && dependents > 0) {
      if (icon_io == null) {
        icon_io = createPlugIcon(Plug.class.getResource("simo_io.gif"));
      }
      return icon_io;
    } else {
      if (providers > 0) {
        if (icon_i == null) {
          icon_i = createPlugIcon(Plug.class.getResource("simo_i.gif"));
        }
        return icon_i;
      } else {
        if (dependents > 0) {
          if (icon_o == null) {
            icon_o = createPlugIcon(Plug.class.getResource("simo_o.gif"));
          }
          return icon_o;
        } else {
          if (icon == null) {
            icon = createPlugIcon(Plug.class.getResource("simo.gif"));
          }
          return icon;
        }
      }
    }
  }

  /**
   * Icon shown when plug is not connected.
   */
  private ImageIcon icon = null;

  /**
   * Icon shown when plug's input part is connected.
   */
  private ImageIcon icon_i = null;

  /**
   * Icon shown when plug's output part is connected.
   */
  private ImageIcon icon_o = null;

  /**
   * Icon shown when plug's input and output parts are both connected.
   */
  private ImageIcon icon_io = null;
}
