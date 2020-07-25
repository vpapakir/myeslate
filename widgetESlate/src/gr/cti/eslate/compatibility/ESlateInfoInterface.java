package gr.cti.eslate.compatibility;

import gr.cti.eslate.base.*;

/**
 * This interface defines the method that all E-Slate Applets must
 * implement, to provide information for their "about" dialog box.
 * This interface defines method
 * <PRE>
 * public ESlateInfo getInfo()
 * </PRE>
 * which returns information about an E-Slate component, to be displayed in
 * the component's "about" dialog box. All E-Slate Applets must implement
 * this method.
 * <P>
 * Example:
 * <PRE>
 * public ESlateInfo getInfo()
 * {
 *   String[] info = {
 *     "Part of the E-Slate environment of educational components",
 *     "(http://www.cti.gr/RD3/EduTech/components.html)",
 *     "Copyright 1996-1998 Computer Technology Institute"
 *   };
 *   return new ESlateInfo("Clock component", info);
 * }
 * </PRE>
 * @see gr.cti.eslate.base.ESlateInfo
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
interface ESlateInfoInterface
{
  /**
   * Returns information about an avakeeo component, to be displayed in
   * the component's "about" dialog box. 
   * <P>
   * Example:
   * <PRE>
   * public ESlateInfo getInfo()
   * {
   *   String[] info = {
   *     "Part of the E-Slate environment of educational components",
   *     "(http://www.cti.gr/RD3/EduTech/components.html)",
   *     "Copyright 1996-1998 Computer Technology Institute"
   *   };
   *   return new ESlateInfo("Clock component", info);
   * }
   * </PRE>
   * @see gr.cti.eslate.base.ESlateInfo
   */
  public ESlateInfo getInfo();
}
