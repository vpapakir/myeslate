package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.sharedObject.*;
import java.awt.*;
/**
 * A shared object wrapping a color value.
 *
 * @author      Thanasis Mantes
 * @author      Giorgos Vasiliou
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */

public class ColorSO extends SharedObject {

   private Color color ;

   /**
    * The constructor.
    * @param   app The associated ESlateHandle instance.
    */
   public ColorSO(gr.cti.eslate.base.ESlateHandle app) {
      super(app);
   }

   /**
    * Sets the Color value.
    * @param   color    The value.
    */
   public void setColor(Color color){
      if (areDifferent(this.color, color)) {
         this.color=color;
         // Create an event
         SharedObjectEvent soe = new SharedObjectEvent(this);
         // Notify the listeners
         fireSharedObjectChanged(soe);
      }
   }

   /**
    * @return  The Color value of the shared object.
    */
   public Color getColor() {
      return color;
   }
}


