package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.sharedObject.*;
import java.net.URL;
/**
 * A shared object wrapping a URL value.
 *
 * @author      Thanasis Mantes
 * @author      Giorgos Vasiliou
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */

public class UrlSO extends SharedObject {
   private URL url;

   /**
    * The constructor.
    * @param   app The associated ESlateHandle instance.
    */
   public UrlSO(gr.cti.eslate.base.ESlateHandle app) {
      super(app);
   }

   /**
    * Sets the URL value.
    * @param   url    The value.
    */
   public void setURL(URL url) {
      if (areDifferent(this.url, url)) {
         this.url=url;
         // Create an event
         SharedObjectEvent soe = new SharedObjectEvent(this);
         // Notify the listeners
         fireSharedObjectChanged(soe);
      }
   }

   /**
    * Return the URL value of the shared object.
    * @return  The URL value of the shared object.
    */
   public URL getURL() {
      return url;
   }
}
