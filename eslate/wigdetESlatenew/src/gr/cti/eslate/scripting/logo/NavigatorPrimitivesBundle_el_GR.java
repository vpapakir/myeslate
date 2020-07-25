package gr.cti.eslate.scripting.logo;

import java.util.ListResourceBundle;

/**
 * Greek language resources for navigator component primitives.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.0, 2-Jun-2006
 */
public class NavigatorPrimitivesBundle_el_GR extends ListResourceBundle
{
  public Object [][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"NAVIGATOR.SETLOCATION","�������.�������"},
    {"NAVIGATOR.LOCATION","�������.����"},
    {"NAVIGATOR.GOHOME", "�������.����������"}, //24Jun2000
    {"NAVIGATOR.FORWARD", "�������.�������"}, //24Jun2000
    {"NAVIGATOR.BACK", "�������.����"}, //24Jun2000
    {"NAVIGATOR.STOP","�������.�����"}, //15Mar2000
    {"NAVIGATOR.REFRESH","�������.��������"} //24Jun2000        
  };

}
