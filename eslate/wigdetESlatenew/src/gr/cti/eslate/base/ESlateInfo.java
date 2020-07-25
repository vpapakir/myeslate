package gr.cti.eslate.base;

/**
 * This class encapsulates the information shown in an E-Slate component's
 * "about" dialog box.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.base.ESlateHandle
 */
public class ESlateInfo
{
  String componentName;
  String[] componentInfo;

 /**
  * Constructs an ESlateInfo structure.
  * @param      name    The name of the component described by the structure.
  * @param      info    An array of strings comprising the information about
  *                     the component. Each of these strings is displayed in
  *                     a separate line on the component's "about" dialog box.
  *                     This parameter can be null, if there is no information
  *                     available for this component.
  * <P>
  * Example:
  * <PRE>
  * String[] info = {
  *   "Part of the E-Slate environment of educational components",
  *   "(http://www.cti.gr/RD3/EduTech/components.html)",
  *   "Copyright 1996-1998 Computer Technology Institute"
  * };
  * </PRE>
  */
  public ESlateInfo(String name, String[] info)
  {
    super();
    componentName = name;
    componentInfo = info;
  }
}
