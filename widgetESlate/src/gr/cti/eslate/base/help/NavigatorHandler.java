package gr.cti.eslate.base.help;

import javax.help.event.*;
import java.net.*;
import gr.cti.eslate.utils.browser.*;

/**
 * Tracking events from the navigators.
 * When something changes at the navigators(TOC, index, search)
 * we get the correct new referenced page and pass to the browser
 * to show it.
 * @author      George Dimitrakopoulos
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
class NavigatorHandler implements HelpModelListener
{
  //private ResourceBundle errorInfo =
  //  ResourceBundle.getBundle(
  //    "gr.cti.eslate.base.help.ErrorMessageBundle",
  //    ESlateMicroworld.getCurrentLocale()
  //  );
  private ESlateBrowser browser;

  public NavigatorHandler(ESlateBrowser Browser)
  {
    this.browser = Browser;
  }

  public void idChanged(HelpModelEvent e)
  {
    URL pickedURL = e.getURL();
    String thesis = pickedURL.toString();
    int badCharacter = thesis.lastIndexOf('\\');
    if (badCharacter > 0) {
      String url = thesis.substring(badCharacter+1, thesis.length());
      String path = thesis.substring(0, badCharacter) + "/";
      thesis = path + url;
    }
    try {
      browser.setCurrentLocation(thesis);
    } catch (Exception ex) {
    }
    HelpSystemViewer.num = 0;
  }
}
