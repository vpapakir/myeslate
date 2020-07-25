package gr.cti.eslate.navigator;

import java.awt.Color;
import java.net.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.sharedObject.*;

import gr.cti.eslate.navigator.models.*;

/**
 * Plugs for Navigator component.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.0, 2-Jun-2006
 */
public class Plugs implements SharedObjectListener
{
  private INavigator navigator;
  private UrlSO urlSO;
  private StringSO stringSO;

  /**
   * Construct the plugs.
   * @param     navigator       The browser used to display URLs in the
   *                            navigator component.
   * @param     handle          The E-Slate handle of the navigator component.
   */
  public Plugs(INavigator navigator, ESlateHandle handle)
  {
    this.navigator = navigator;

    if (handle == null) {
      return;
    }

    handle.setMenuLightWeight(false); //15Mar2000: using heavy-weight plug popup

    // "URL" plug
    urlSO = new UrlSO(handle);
    try {
      SingleInputMultipleOutputPlug plug1 = new SingleInputMultipleOutputPlug(
        handle, Res.getResourceBundle(), "URL", new Color(188, 143, 143),
        Class.forName("gr.cti.eslate.sharedObject.UrlSO"), urlSO, this
      );
      handle.addPlug(plug1);
      plug1.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get URL from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            UrlSO so =
              (UrlSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
            URL url = null;
            try {
              url = so.getURL();
              String urlString = url.toString();
              Plugs.this.navigator.setCurrentLocation(urlString);
              urlSO.setURL(url);
              stringSO.setString(urlString);
            } catch(Exception ex) {
              errlog("setCurrentLocation(" + url + ") failed!");
            }
          }
        }
      });
    } catch(Exception exception1) {
      errlog(exception1.getMessage() + "\nError creating the UrlSO plug");
    }

    // "URL as string" plug
    stringSO = new StringSO(handle);
    try {
      SingleInputMultipleOutputPlug plug2 = new SingleInputMultipleOutputPlug(
        handle, Res.getResourceBundle(), "URL as string",
        new Color(139, 117, 0),
        Class.forName("gr.cti.eslate.sharedObject.StringSO"), stringSO, this
      );
      handle.addPlug(plug2);
      plug2.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get URL from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            StringSO so =
              (StringSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
            String urlString = so.getString();
            URL url = null;
            try {
              try {
                url = new URL(urlString);
              } catch (MalformedURLException ex) {
              }
              Plugs.this.navigator.setCurrentLocation(urlString);
              urlSO.setURL(url);
              stringSO.setString(urlString);
            } catch(Exception ex) {
              errlog("setCurrentLocation(" + url + ") failed!");
            }
          }
        }
      });
    } catch(Exception exception2) {
      errlog(exception2.getMessage() + "\nError creating the StringSO plug");
    }
  }

  /**
   * Handler for shared object events.
   * @param     soe     The event to handle.
   */
  public void handleSharedObjectEvent(SharedObjectEvent soe)
  {
    if (navigator == null) {
      return;
    }
    SharedObject so = soe.getSharedObject();
    //Vector path = soe.getPath();
    URL url = null;
    String urlString = null;
    try {
      if (so instanceof UrlSO) {
        url = ((UrlSO)so).getURL();
        urlString = url.toString();
      }else{
        if (so instanceof StringSO) {
          urlString = ((StringSO)so).getString();
          try {
            url = new URL(urlString);
          } catch (MalformedURLException e) {
          }
        }else{
          return;
        }
      }
      navigator.setCurrentLocation(urlString);
      //if (!path.contains(handle.getComponent())) {
      //  urlSO.setURL(url, path);
      //  stringSO.setString(urlString, path);
      //}
      urlSO.setURL(url);
      stringSO.setString(urlString);
    } catch(Exception e) {
      errlog("setCurrentLocation(" + url + ") failed!");
    }
  }

  /**
   * Exports a URL to the components connected to the "URL" and "URL as string"
   * plugs.
   * @param     url     The URL to export.
   */
  void exportURL(String url)
  {
    try {
      urlSO.setURL(new URL(url));
    } catch (MalformedURLException e) {
    }
    stringSO.setString(url);
  }

  /**
   * Displays an error message.
   * @param     msg     The message to display.
   */
  private void errlog(String msg)
  {
    System.err.println(msg);
  }
}
