package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.sharedObject.*;
import java.util.Date;

/**
 * A shared object wrapping a date value.
 *
 * @author      Thanasis Mantes
 * @author      Giorgos Vasiliou
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class DateSO extends SharedObject {
   private Date date;
   private boolean isChanging;

   /**
    * The constructor.
    * @param   app The associated ESlateHandle instance.
    */
   public DateSO(gr.cti.eslate.base.ESlateHandle app) {
      super(app);
   }

   /**
    * Sets the Date value.
    * @param   date    The value.
    */
   public void setDate(Date date) {
      if (areDifferent(this.date, date)) {
         this.date=date;
         // Create an event
         SharedObjectEvent soe = new SharedObjectEvent(this);
         // Notify the listeners
         fireSharedObjectChanged(soe);
      }
   }

   /**
    * Return the Date value of the shared object.
    * @return  The Date value of the shared object.
    */
   public Date getDate() {
      return date;
   }

   public void setChanging(boolean changing){
      isChanging = changing;
   }

   public boolean isChanging(){
      return isChanging;
   }

}
