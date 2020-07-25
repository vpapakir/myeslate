package gr.cti.eslate.sharedObject;

/**
 * A shared object wrapping an Integer value.
 *
 * @author      Thanasis Mantes
 * @author      Giorgos Vasiliou
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */

import gr.cti.eslate.base.sharedObject.*;

public class IntegerSO extends SharedObject {
   private Integer integer;

   /**
    * The constructor.
    * @param   app The associated ESlateHandle instance.
    */
   public IntegerSO(gr.cti.eslate.base.ESlateHandle app) {
      super(app);
   }

   /**
    * Sets the Integer value.
    * @param   integer    The value.
    */
   public void setInteger(int integer) {
      if ((this.integer == null) || (this.integer.intValue() != integer)) {
         this.integer=new Integer(integer);
         // Create an event
         SharedObjectEvent soe = new SharedObjectEvent(this);
         // Notify the listeners
         fireSharedObjectChanged(soe);
      }
   }

   /**
    * Sets the Integer (class) value.
    * @param   integer    The value.
    */
   public void setInteger(Integer integer) {
      if (areDifferent(this.integer, integer)) {
         this.integer=integer;
         // Create an event
         SharedObjectEvent soe = new SharedObjectEvent(this);
         // Notify the listeners
         fireSharedObjectChanged(soe);
      }
   }

   /**
    * @return  The Integer (class) value of the shared object.
    */
   public Integer getInteger() {
       return integer;
   }

   /**
    * This method is unsafe when the content of the SharedObject is undefined. It returns
    * a default value. Use <code>getInteger()</code> if not sure about the validity.
    */
   public int getIntegerValue() {
      if (integer!=null)
         return integer.intValue();
      else
         return 0;
   }
}
