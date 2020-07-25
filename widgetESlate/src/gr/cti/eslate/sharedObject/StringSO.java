package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.sharedObject.*;
/**
 * A shared object wrapping an alphanumeric value.
 *
 * @author      Thanasis Mantes
 * @author      Giorgos Vasiliou
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */

public class StringSO extends SharedObject {
   private String string;

   /**
    * The constructor.
    * @param   app The associated ESlateHandle instance.
    */

   public StringSO(gr.cti.eslate.base.ESlateHandle app) {
      super(app);
   }

   /**
    * Sets the String value.
    * @param   string    The value.
    */

   public void setString(String string) {
      if (areDifferent(this.string, string)) {
         this.string=string;
         // Create an event
         SharedObjectEvent soe = new SharedObjectEvent(this);
         // Notify the listeners
         fireSharedObjectChanged(soe);
      }
   }

   /**
    * @return  The String value of the shared object.
    */
   public String getString() {
       return string;
   }

}
