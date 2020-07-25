package gr.cti.eslate.base;

import java.awt.Color;
import java.util.*;

import javax.swing.*;

/**
 * Implements plugs that accept a single connection without an associated
 * shared object. In the associated protocol, these plugs will play the
 * "right" role; the "left" side will require that  the "right" side implements
 * a number of interfaces, while the "right" side will not demand anything from
 * the "left" side.
 * E-Slate uses roles to assign different icons to the
 * plugs, so that the user may have a visual indication as to which plug may
 * be connected to which.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public class MaleSingleIFSingleConnectionProtocolPlug
  extends ProtocolPlug implements ISingleIFProtocolPlug
{
  /**
   * Constructs a plug that accepts a single connection without an associated
   * shared object. In the associated protocol, this plug will play the
   * "left" role; the right side will require that  the "left" side implements
   * a number of interfaces, while the "left" side will not demand anything
   * from the "right" side.
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
   *
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  public MaleSingleIFSingleConnectionProtocolPlug(ESlateHandle handle,
                                          ResourceBundle bundle, String key,
                                          Color cl)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl,  null,  null, RIGHT_ROLE, null);
  }

  /**
   * Constructs a plug that accepts a single connection without an associated
   * shared object. In the associated protocol, this plug will play the
   * "left" role; the right side will require that  the "left" side implements
   * a number of interfaces, while the "left" side will not demand anything
   * from the "right" side.
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
   * @param     exported        A vector of the Java interfaces that the
   *                            plug declares that the protocol implementor
   *                            implements. Setting this to null is equivalent
   *                            to declaring that the protocol implementor
   *                            implements all the interfaces that it actually
   *                            implements.
   * @exception InvalidPlugParametersException  This
   *                            exception is thrown when a plug constructor
   *                            is called with invalid parameters.
   */
  @SuppressWarnings("unchecked")
  public MaleSingleIFSingleConnectionProtocolPlug(ESlateHandle handle,
                                          ResourceBundle bundle, String key,
                                          Color cl, Vector exported)
    throws InvalidPlugParametersException
  {
    super(handle, bundle, key, cl, null, null, RIGHT_ROLE, null);
    setExportedInterfaces(exported);
  }

  /**
   * Returns the icon that represents the plug.
   * @return    The icon.
   */
  public ImageIcon getIcon()
  {
    if (internals.protocolPlugs.size() == 0) {
      if (icon == null) {
        icon = createPlugIcon(Plug.class.getResource("scpb.gif"));
      }
      return icon;
    } else {
      if (icon_c == null) {
        icon_c = createPlugIcon(Plug.class.getResource("scpb_c.gif"));
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
