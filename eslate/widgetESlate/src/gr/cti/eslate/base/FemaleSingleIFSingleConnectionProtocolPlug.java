package gr.cti.eslate.base;

import java.awt.Color;
import java.util.*;

import javax.swing.*;

/**
 * Implements plugs that accept a single connection without an associated
 * shared object. In the associated protocol, these plugs will play the
 * "left" role, requiring the "right" side to implement a number of
 * interfaces, while the "right" side will not demand anything from the "left"
 * side.
 * E-Slate uses roles to assign different icons to the
 * plugs, so that the user may have a visual indication as to which plug may
 * be connected to which.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public class FemaleSingleIFSingleConnectionProtocolPlug
  extends ProtocolPlug implements ISingleIFProtocolPlug
{
  /**
   * Constructs a plug that accepts a single connection without an associated
   * shared object. In the associated protocol, this plug will play the
   * "right" role, requiring the "left" side to implement a number of
   * interfaces, while the "left" side will not demand anything from the
   * "right" side.
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
   * @param     protocol        The Java interface that objects connecting
   *                            through this plug must implement.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public FemaleSingleIFSingleConnectionProtocolPlug(ESlateHandle handle,
                                          ResourceBundle bundle, String key,
                                          Color cl, Class protocol)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, null, protocol, LEFT_ROLE, null);
  }

  /**
   * Constructs a plug that accepts a single connection without an associated
   * shared object. In the associated protocol, this plug will play the
   * "right" role, requiring the "left" side to implement a number of
   * interfaces, while the "left" side will not demand anything from the
   * "right" side.
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
   * @param     reqProtocols    A list of the Java interfaces that objects
   *                            connecting through this plug must implement.
   * @param     optProtocols    A list of the Java interfaces that objects
   *                            connecting through this plug may optionally
   *                            implement.
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public FemaleSingleIFSingleConnectionProtocolPlug(ESlateHandle handle,
                                          ResourceBundle bundle, String key,
                                          Color cl, Vector reqProtocols,
                                          Vector optProtocols)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, null, reqProtocols, optProtocols,
          LEFT_ROLE, null);
  }

  /**
   * Returns the icon that represents the plug.
   * @return    The icon.
   */
  @SuppressWarnings("unchecked")
  public ImageIcon getIcon()
  {
    if (internals.protocolPlugs.size() == 0) {
      if (icon == null) {
        Class c = Plug.class;
        icon = createPlugIcon(c.getResource("scpa.gif"));
      }
      return icon;
    } else {
      if (icon_c == null) {
        icon_c = createPlugIcon(Plug.class.getResource("scpa_c.gif"));
      }
      return icon_c;
    }
  }

  /**
   * Icon shown when plug is not connected.
   */
  private ImageIcon icon = null;

  /**
   * Icon shown when plug is connected.
   */
  private ImageIcon icon_c = null;
}
