package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.sharedObject.*;

/**
 * A shared object wrapping a boolean value.
 *
 * @author      Thanasis Mantes
 * @author      Giorgos Vasiliou
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class BooleanSO extends SharedObject {
    private Boolean bool;
    /**
     * The constructor.
     * @param   app The associated ESlateHandle instance.
     */
    public BooleanSO(gr.cti.eslate.base.ESlateHandle app) {
        super(app);
    }
    /**
     * Sets the boolean value.
     * @param   bool    The value.
     */
    public void setBoolean(boolean bool) {
        if ((this.bool == null) || this.bool.booleanValue() != bool) {
            this.bool=new Boolean(bool);
            // Create an event...
            SharedObjectEvent soe = new SharedObjectEvent(this);
            // Notify the listeners    
            fireSharedObjectChanged(soe); 
        }
    }

    /**
     * Sets the boolean value.
     * @param   bool    The value.
     */
    public void setBoolean(Boolean bool) {
        if (areDifferent(this.bool, bool)) {
            this.bool=bool;
            // Create an event
            SharedObjectEvent soe = new SharedObjectEvent(this);
            // Notify the listeners    
            fireSharedObjectChanged(soe); 
        }
    }
    
    /**
     * @return  The boolean value of the shared object.
     */
    public Boolean getBoolean() {
        return bool;
    }
    
    /**
     * This method is unsafe when the content of the SharedObject is undefined. It returns
     * a default value. Use <code>getBoolean()</code> if not sure about the validity.
     */
    public boolean getBooleanValue(){
        if (bool!=null)
          return bool.booleanValue();
        else
          return false;
    }


}
